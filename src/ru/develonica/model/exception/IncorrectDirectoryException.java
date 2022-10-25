package ru.develonica.model.exception;

/**
 * Отображение ошибки выбора директории.
 */
public class IncorrectDirectoryException extends Exception {

    /** Сообщение об ошибке выбора директории. */
    private static final String INCORRECT_DIRECTORY = "Такой директории не существует. Попробуйте еще раз.\n";

    public IncorrectDirectoryException() {
        super(INCORRECT_DIRECTORY);
    }
}