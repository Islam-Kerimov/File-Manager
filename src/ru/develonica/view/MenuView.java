package ru.develonica.view;

import static java.lang.String.format;
import static java.lang.System.out;

/**
 * Отображение пользователю пунктов меню приложения.
 */
public class MenuView {

    // Пункты меню приложения с возможными командами
    private static final String LIST_ALL = "Отобразить список файлов и директорий. Command: 'ls'";
    private static final String CREATE = "Создать файл или директорию. Commands: 'mkdir dir/'; 'touch file.txt'";
    private static final String DELETE = "Удалить файл или директорию. Commands: 'rmdir dir/'; 'rm file.txt'";
    private static final String CHANGE = "Переход по директориям. Commands: 'cd \\'; 'cd ..'; 'cd dir/'";
    private static final String UPDATE = "Обновить список файлов и директорий. Command: 'refresh'";
    private static final String EXIT = "Выход. Command: 'exit'";
    private static final String MENU;

    static {
        MENU = format("%s\n%s\n%s\n%s\n%s\n%s\n%n",
                LIST_ALL, CREATE, DELETE, CHANGE, UPDATE, EXIT);
    }

    /**
     * Отображает пункты меню пользователю.
     *
     * @param currentFolder текущая директория
     */
    public void showInfo(String currentFolder) {
        out.printf(MENU);
        out.println(currentFolder);
    }
}
