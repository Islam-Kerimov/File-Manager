package ru.develonica.model;

/**
 * Команда пользователя.
 */
public class UserCommand {

    /** Команда меню введенная пользователем. */
    private final String command;

    /** Параметр команды. */
    private final String parameter;

    public UserCommand(String command, String param) {
        this.command = command;
        this.parameter = param;
    }

    public String getCommand() {
        return command;
    }

    public String getParameter() {
        return parameter;
    }
}
