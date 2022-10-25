package ru.develonica.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static java.util.stream.Stream.generate;

/**
 * Пул потоков, который повторно использует фиксированное количество
 * потоков, работающих с общей неограниченной очередью. В любой момент
 * не более n потоков будут активно выполнять задачи. Если дополнительные
 * задачи отправляются, когда все потоки активны, они будут ждать в очереди,
 * пока поток не станет доступным. Потоки в пуле будут существовать
 * до тех пор, пока он не будет явно отключен.
 */
public class CustomThreadPool {

    /** Максимальное количество потоков, разрешенных в пуле. */
    private final int maxPoolSize;

    /** Флаг отключения приема новых задач на выполнение. */
    private final AtomicBoolean run;

    /** Очередь задач. */
    private final LinkedList<Runnable> taskList;

    /** Список потоков. */
    private final List<Thread> threadList;

    /** Список для инициализации потоков. */
    private final Supplier<Thread> threadSupplier;

    public CustomThreadPool(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        this.run = new AtomicBoolean(true);
        this.taskList = new LinkedList<>();
        this.threadList = new ArrayList<>(maxPoolSize);
        this.threadSupplier = () -> new CustomThread(this.taskList, this.run);

        init();
    }

    /**
     * Инициализация потоков в <code>threadList</code>,
     * чтобы они могли начать отслеживать <code>taskList</code>
     * на наличие новых задач.
     */
    private void init() {
        generate(threadSupplier).limit(maxPoolSize).forEach(thread -> {
            threadList.add(thread);
            thread.start();
        });
    }

    /**
     * Упорядоченное отключение, при котором ранее отправленные
     * задачи выполняются, но новые задачи не принимаются.
     */
    public void shutdown() {
        this.run.set(false);
    }

    /**
     * Отправляет на выполнение задачу Runnable.
     *
     * @param task задача на отправку.
     */
    public void submit(Runnable task) {
        synchronized (taskList) {
            if (run.get()) {
                if (taskList.size() < maxPoolSize) {
                    // создать новый поток для threadList
                    CustomThread customThread = new CustomThread(taskList, run);
                    threadList.add(customThread);
                    customThread.start();

                    // отправить задачу
                    taskList.add(task);
                } else {
                    // размер maxPoolSize уже достигнут
                    // просто отправить задачу
                    taskList.add(task);
                }
            }
        }
    }

    /**
     * Статический вложенный класс, который наследует класс
     * <code>Thread</code>, с возможностью отслеживать <code>taskList</code>
     * на наличие новых задач.
     */
    private static final class CustomThread extends Thread {
        private final LinkedList<Runnable> taskList;
        private final AtomicBoolean run;

        private CustomThread(LinkedList<Runnable> taskList, AtomicBoolean run) {
            this.taskList = taskList;
            this.run = run;
        }

        @Override
        public void run() {
            while (run.get()) {
                Runnable task;
                synchronized (taskList) {
                    // ожидает, пока нет задач для выполнения
                    task = taskList.poll();
                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
