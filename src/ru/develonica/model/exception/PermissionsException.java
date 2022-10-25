package ru.develonica.model.exception;

/**
 * Вывод ошибок при работе с файлами и директориями.
 */
public class PermissionsException extends Exception {

    /** Сообщение об ошибке работы нд файлом. */
    private static final String PERMISSION_DENIED = "Произошла ошибка ввода/вывода.\n" +
            "Возможно такого файла не существует или нет прав на создание или удаление файла\n";

    public PermissionsException() {
        super(PERMISSION_DENIED);
    }
}
