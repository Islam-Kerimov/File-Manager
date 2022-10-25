package ru.develonica.controller;

import ru.develonica.model.exception.IncorrectCommandException;
import ru.develonica.model.exception.IncorrectDirectoryException;
import ru.develonica.model.exception.PermissionsException;
import ru.develonica.model.FileTree;
import ru.develonica.model.UserCommand;
import ru.develonica.view.FileManagerView;
import ru.develonica.view.MenuView;

import java.util.Scanner;

import static java.lang.System.getProperty;
import static java.lang.System.in;
import static ru.develonica.model.Item.EXIT;
import static ru.develonica.model.Item.LS;
import static ru.develonica.model.Item.REFRESH;
import static ru.develonica.model.Item.valueOf;

/**
 * Обработка действий пользователя в зависимости
 * от того, какую команду он ввел.
 *
 * @author Ислам Керимов
 */
public class FileManagerController {

    /**
     * Позиция корневой директории, по умолчанию это домашний каталог
     * текущего вошедшего в систему пользователя.
     */
    private static final String ROOT = getProperty("user.home");

    /** Контроллер выполняющий пункты меню. */
    private final MenuController menuController;

    /** Файловое дерево. */
    private final FileTree fileTree;

    /** Отображение меню команд. */
    private final MenuView menuView;

    /** Подтверждение пользовательских действий и вывод ошибок. */
    private final FileManagerView fileManagerView;

    /** Команда пользователя. */
    private String commandLine;

    public FileManagerController() {
        this.menuController = new MenuController();
        this.fileTree = new FileTree();
        this.menuView = new MenuView();
        this.fileManagerView = new FileManagerView();
    }

    /**
     * Обработка пользовательских команд в бесконечном цикле,
     * пока пользователь не введет команду <code>exit</code>.
     */
    public void start() {
        Scanner scanner = new Scanner(in);

        // создает корневую директорию.
        chooseRoot(scanner);

        // работа с командами из меню, пока пользователь не введет exit
        while (!getCommandLine(scanner)) {
            UserCommand userCommand = validate(commandLine);

            try {
                executeCommand(userCommand, scanner);
            } catch (IncorrectCommandException
                     | PermissionsException
                     | IncorrectDirectoryException e) {
                fileManagerView.errorMessage(e.getMessage());
            }

            // отобразить меню с командами.
            menuView.showInfo(fileTree.getCurrentFolder());
        }
    }

    /**
     * Валидация пользовательской команды.
     *
     * @param commandLine команда пользователя
     * @return объект <code>UserCommand</code> с переменным
     * <code>command</code> и <code>parameter</code>
     */
    private UserCommand validate(String commandLine) {
        String[] userCommand = commandLine.split(" ", 2);

        return new UserCommand(userCommand[0],
                userCommand.length > 1 ? userCommand[1] : null);
    }

    /**
     * Обработка пользовательской команды и передача полученной
     * информацию в Представление для отображения в консоли.
     *
     * @param userCommand команда пользователя
     * @throws IncorrectCommandException    если пользователь ввел некорректные
     *                                      данные
     * @throws PermissionsException         если произошла ошибка ввода/вывода
     * @throws IncorrectDirectoryException  если пользователь ввел некорректную
     *                                      директорию
     */
    private void executeCommand(UserCommand userCommand, Scanner scanner)
            throws IncorrectCommandException, PermissionsException, IncorrectDirectoryException {

        String command = userCommand.getCommand();
        String parameter = userCommand.getParameter();

        if (!command.toUpperCase().equals(LS.name())
                && !command.toUpperCase().equals(REFRESH.name())
                && (parameter == null || parameter.length() == 0)) {
            throw new IncorrectCommandException();
        }

        try {
            switch (valueOf(command.toUpperCase())) {
                case LS:
                    menuController.fileTree(fileTree);
                    break;
                case TOUCH:
                    menuController.createFile(parameter, fileTree.getCurrentFolder());
                case MKDIR:
                    menuController.createDirectory(parameter, fileTree.getCurrentFolder());
                case RM:
                    fileManagerView.confirmDelete(parameter);
                    if (getConfirm(scanner)) {
                        menuController.deleteFile(parameter, fileTree.getCurrentFolder());
                    }
                case RMDIR:
                    fileManagerView.confirmDelete(parameter);
                    if (getConfirm(scanner)) {
                        menuController.deleteDirectory(parameter, fileTree.getCurrentFolder());
                    }
                case CD:
                    menuController.changeDirectory(parameter, fileTree);
                case REFRESH:
                    menuController.refreshData(fileTree);
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectCommandException();
        }
    }

    /**
     * Запрос подтверждения удаления файла или папки.
     *
     * @return <code>true</code> если пользователь подтвердил удаление.
     */
    private boolean getConfirm(Scanner scanner) {
        commandLine = scanner.nextLine();

        // подтверждение удаления
        String confirm = "Y";
        return commandLine.equals(confirm);
    }

    /**
     * Получение команды от пользователя.
     *
     * @return <code>true</code> если команда равна <code>exit</code>.
     */
    private boolean getCommandLine(Scanner scanner) {
        commandLine = scanner.nextLine();

        return commandLine.toUpperCase().equals(EXIT.name());
    }

    /**
     * Создание корневой директории в файловом дереве.
     */
    private void chooseRoot(Scanner scanner) {
        fileManagerView.chooseRoot();

        commandLine = scanner.nextLine();

        try {
            menuController.createRoot(commandLine, fileTree, ROOT);
            menuView.showInfo(fileTree.getCurrentFolder());
        } catch (IncorrectDirectoryException | PermissionsException e) {
            fileManagerView.errorMessage(e.getMessage());
            chooseRoot(scanner);
        }
    }
}
