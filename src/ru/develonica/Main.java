package ru.develonica;

import ru.develonica.controller.FileManagerController;

/**
 * Точка входа в программу.
 */
public class Main {

    /**
     * Создает и стартует контроллер
     * <code>FileManagerController</code>.
     */
    public static void main(String[] args) {
        FileManagerController controller = new FileManagerController();

        controller.start();
    }
}
