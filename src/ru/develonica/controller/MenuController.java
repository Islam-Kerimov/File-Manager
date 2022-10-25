package ru.develonica.controller;

import ru.develonica.model.exception.IncorrectDirectoryException;
import ru.develonica.model.exception.PermissionsException;
import ru.develonica.model.FileInfo;
import ru.develonica.model.FileTree;
import ru.develonica.model.FileType;
import ru.develonica.thread.CustomThreadPool;
import ru.develonica.view.FileChangeView;
import ru.develonica.view.FileInfoView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;
import static java.lang.Runtime.getRuntime;
import static java.lang.Thread.sleep;
import static java.nio.file.Files.isExecutable;
import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.isWritable;
import static java.nio.file.Path.of;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;
import static java.util.Objects.requireNonNull;
import static ru.develonica.model.FileType.DIR;
import static ru.develonica.model.FileType.OTHER_FILE;

/**
 * Контроллер выполняющий команды пользователя.
 */
public class MenuController {

    /** Коэффициент увеличения размера потоков. */
    private static final int CORES_MULTIPLICATOR = 2;

    /** Размер пула потоков. */
    private static final int POOL_SIZE = getRuntime().availableProcessors() * CORES_MULTIPLICATOR;

    /** Отображение таблицы со списком файлов и директорий. */
    private final FileInfoView fileInfoView;

    /** Отображение результатов изменений с файлами и папками.*/
    private final FileChangeView fileChangeView;

    MenuController() {
        this.fileInfoView = new FileInfoView();
        this.fileChangeView = new FileChangeView();
    }

    /**
     * Передача актуального списка объектов файлового дерева в Представление.
     */
    public void fileTree(FileTree fileTree) {
        File directory = new File(fileTree.getRealPath());
        int length;
        try {
            length = requireNonNull(directory.listFiles()).length;
        } catch (NullPointerException e) {
            length = 0;
        }

        if (!fileTree.isFilesExist()) {
            // если пул потоков не выполнил все задачи и файловое дерево заполнена не полностью
            fileInfoView.displayAllDocuments(fileTree.getFileInfo(), length);
            fileTree.setFilesExist(true);
        } else {
            fileInfoView.displayAllDocuments(fileTree.getFileInfo(), fileTree.getSize());
        }

        // вывести общие параметры текущей директории
        totalInfo(fileTree);
    }

    /**
     * Передача в Представление общих параметров текущей директории
     * файлового дерева.
     *
     * @param fileTree файловое дерево.
     */
    private void totalInfo(FileTree fileTree) {
        List<Long> totalInfo = new ArrayList<>(2);
        totalInfo.add((long) fileTree.getTotalObjects());
        totalInfo.add(fileTree.getTotalSize());

        fileInfoView.displayTotalSizeAndObjects(totalInfo);
    }

    /**
     * Создание корневой папки в файловом дереве.
     *
     * @param rootDir абсолютный путь корневой папки
     * @throws IncorrectDirectoryException  если пользователь ввел некорректную
     *                                      директорию
     * @throws PermissionsException         если произошла ошибка ввода/вывода
     */
    public void createRoot(String rootDir, FileTree fileTree, String ROOT)
            throws PermissionsException, IncorrectDirectoryException {

        if (!new File(rootDir).exists() && !rootDir.equals("")) {
            throw new IncorrectDirectoryException();
        }

        rootDir = rootDir.equals("")
                ? ROOT
                : rootDir;

        // добавить '\' если пользователь не ввел в конце названия директории
        if ((rootDir.charAt(rootDir.length() - 1)) != '\\'
                && (rootDir.charAt(rootDir.length() - 1)) != '/') {
            rootDir += separator;
        }

        // сохранить в файловом дереве.
        saveData(fileTree, rootDir);
    }

    /**
     * Создание файла в директории, где находится пользователь.
     *
     * @param fileName имя файла
     * @throws PermissionsException если произошла ошибка ввода/вывода
     */
    public void createFile(String fileName, String currentFolder)
            throws PermissionsException {

        String pathName = currentFolder + separator + fileName;
        File file = new File(pathName);

        // вывести пользователю результат выполнения операции
        try {
            fileChangeView.showCreatingFile(file.createNewFile());
        } catch (IOException e) {
            throw new PermissionsException();
        }
    }

    /**
     * Создание папки в директории, где находится пользователь.
     *
     * @param dirName имя папки
     * @throws PermissionsException если произошла ошибка ввода/вывода
     */
    public void createDirectory(String dirName, String currentFolder)
            throws PermissionsException {

        String pathName = currentFolder + separator + dirName;
        File directory = new File(pathName);

        if (!directory.getParentFile().canWrite()) {
            throw new PermissionsException();
        }

        // вывести пользователю результат выполнения операции
        fileChangeView.showCreatingDirectory(directory.mkdir());
    }

    /**
     * Удаление файла из директории, в котором находится пользователь.
     *
     * @param fileName имя файла
     * @throws PermissionsException если произошла ошибка ввода/вывода
     */
    public void deleteFile(String fileName, String currentFolder)
            throws PermissionsException {

        Path filePath = of(currentFolder + separator + fileName);

        // вывести пользователю результат выполнения операции
        try {
            fileChangeView.showDeletingFile(Files.deleteIfExists(filePath));
        } catch (IOException e) {
            throw new PermissionsException();
        }
    }

    /**
     * Удаление папки из директории, в котором находится пользователь.
     *
     * @param dirName имя папки
     * @throws PermissionsException если произошла ошибка ввода/вывода
     */
    public void deleteDirectory(String dirName, String currentFolder)
            throws PermissionsException {

        Path dirPath = of(currentFolder + separator + dirName);

        try {
            Files.walk(dirPath)
                    .sorted(reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (NoSuchFileException e) {
            fileChangeView.showDeletingDirectory(dirPath.toFile().isDirectory());
            return;
        } catch (IOException e) {
            throw new PermissionsException();
        }

        // вывести пользователю результат выполнения операции
        fileChangeView.showDeletingDirectory(!dirPath.toFile().isDirectory());
    }

    /**
     * Переход по директориям в файловом дереве.
     *
     * @param directory параметр для перехода
     * @throws PermissionsException         если произошла ошибка ввода/вывода
     * @throws IncorrectDirectoryException  если пользователь ввел некорректную
     *                                      директорию
     */
    public void changeDirectory(String directory, FileTree fileTree)
            throws PermissionsException, IncorrectDirectoryException {

        if (directory.equals("/") || directory.equals("\\")) {
            fileTree.getBackFirst();
        } else if (directory.equals("..")) {
            if (!fileTree.getCurrentFolder().equals(fileTree.getFirstPath())) {
                fileTree.getBack();
            } else {
                fileTree.getBackFirst();
            }
        } else {
            File dir = new File(fileTree.getCurrentFolder() + separator + directory);

            if (dir.equals(new File(fileTree.getCurrentFolder()))) {
                return;
            }

            if (dir.isDirectory()) {
                saveData(fileTree, dir.toString());
            } else {
                throw new IncorrectDirectoryException();
            }
        }
    }

    /**
     * Обновление данных в текущей папке файлового дерева.
     */
    public void refreshData(FileTree fileTree) {
        fileTree.clean();
        fileTree.setFilesExist(false);
        File folder = new File(fileTree.getCurrentFolder());
        listAllFiles(folder, fileTree);
    }

    /**
     * Сохранение данных из текущей директории, в которой находится
     * пользователь в файловое дерево.
     *
     * @throws PermissionsException если файл не существует или произошла
     *                              ошибка ввода/вывода
     */
    private void saveData(FileTree fileTree, String currentFolder)
            throws PermissionsException {

        // добавить в структуру файлового дерева текущую директорию
        try {
            fileTree.addNode(currentFolder, of(currentFolder).toRealPath().toString());
        } catch (IOException e) {
            throw new PermissionsException();
        }

        // добавить в файловое дерево список файлов и папок, если там еще нет
        if (!fileTree.isExists()) {
            File folder = new File(fileTree.getRealPath());
            listAllFiles(folder, fileTree);
        } else {
            fileTree.setExists(false);
        }
    }

    /**
     * Занесение всех объектов текущей директории в файловое дерево
     * с помощью пула потоков.
     *
     * @param folder текущая директория
     */
    private void listAllFiles(File folder, FileTree fileTree) {

        File[] files = folder.listFiles();
        if (files != null) {
            // создает пул потоков и передает ему задачи
            CustomThreadPool threadPool = new CustomThreadPool(POOL_SIZE);
            stream(files)
                    .forEach(file -> threadPool.submit(() -> appendElement(file, fileTree)));

            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threadPool.shutdown();
        }
    }

    /**
     * Добавление элемента в текущую директорию файлового дерева.
     *
     * @param file файл/папка для добавления
     * @param fileTree файловое дерево
     */
    private void appendElement(File file, FileTree fileTree) {
        FileType fileType = file.isDirectory()
                ? DIR
                : OTHER_FILE;
        long fileSize = file.isDirectory()
                ? searchFiles(file)
                : file.length();
        Boolean[] attribute = {isReadable(file.toPath()),
                isWritable(file.toPath()),
                isExecutable(file.toPath())};

        // объект с корректными параметрами для отображения
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(file.getName());
        fileInfo.setType(fileType);
        fileInfo.setFileSize(fileSize);
        fileInfo.setAttribute(attribute);

        synchronized (this) {
            fileTree.addChild(fileInfo);
            fileTree.setTotalSize(fileTree.getTotalSize() + fileInfo.getFileSize());
            fileTree.setTotalObjects(fileTree.getTotalObjects() + 1);
        }
    }

    /**
     * Подсчет размера текущей папки рекурсивно.
     *
     * @param folder текущая директория
     * @return размер текущей директории в байтах
     */
    private long searchFiles(File folder) {
        long size = 0L;
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                size += file.isDirectory()
                        ? searchFiles(file)
                        : file.length();
            }
        }

        return size;
    }
}
