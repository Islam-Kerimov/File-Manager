package ru.develonica.model.exception;

/**
 * Отображение ошибок пользовательских команд.
 */
public class IncorrectCommandException extends Exception {

    /** Сообщение об ошибке ввода команды. */
    private static final String INCORRECT_COMMAND = "Не корректная команда. Попробуйте еще раз.\n";

    public IncorrectCommandException() {
        super(INCORRECT_COMMAND);
    }
}