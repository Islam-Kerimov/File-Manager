package ru.develonica.view;

import static java.lang.System.out;

/**
 * Отображение результатов изменений
 * с файлами и папками.
 */
public class FileChangeView {

    // отображение успешных или неудачных попыток создания/удаления файлов/директорий
    private static final String FILE_CREATED = "Файл успешно создан\n";
    private static final String FILE_NOT_CREATED = "Файл уже существует\n";
    private static final String DIRECTORY_CREATED = "Папка успешно создана\n";
    private static final String DIRECTORY_NOT_CREATED = "Папка уже существует\n";
    private static final String FILE_DELETED = "Файл удален\n";
    private static final String FILE_NOT_DELETED = "Такого файла не существует\n";
    private static final String DIRECTORY_DELETED = "Папка удалена\n";
    private static final String DIRECTORY_NOT_DELETED = "Такой папки не существует\n";

    /**
     * Отображение информации о попытке создания нового файла.
     *
     * @param file булево выражение попытки создания файла.
     */
    public void showCreatingFile(boolean file) {
        out.println(file
                ? FILE_CREATED
                : FILE_NOT_CREATED);
    }

    /**
     * Отображение информации о попытке создания новой директории.
     *
     * @param directory булево выражение попытки создания директории.
     */
    public void showCreatingDirectory(boolean directory) {
        out.println(directory
                ? DIRECTORY_CREATED
                : DIRECTORY_NOT_CREATED);
    }

    /**
     * Отображение информации о попытке удаления файла.
     *
     * @param file булево выражение попытки удаления файла.
     */
    public void showDeletingFile(boolean file) {
        out.println(file
                ? FILE_DELETED
                : FILE_NOT_DELETED);
    }

    /**
     * Отображение информации о попытке удаления директории.
     *
     * @param directory булево выражение попытки удаления директории.
     */
    public void showDeletingDirectory(boolean directory) {
        out.println(directory
                ? DIRECTORY_DELETED
                : DIRECTORY_NOT_DELETED);
    }
}
