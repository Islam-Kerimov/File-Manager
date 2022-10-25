package ru.develonica.model;

/**
 * Типы объектов в файловом дереве.
 */
public enum FileType {

    /** Директория. */
    DIR,

    /** Изображение. */
    PICTURE,

    /** Текстовый файл. */
    TEXT,

    /** Документ. */
    DOCUMENT,

    /** Архивированный файл. */
    ARCHIVE,

    /** Файла видео. */
    VIDEO,

    /** Файла музыка. */
    MUSIC,

    /** Java файл. */
    JAVA_FILE,

    /** Тип всех неучтенных файлов. */
    OTHER_FILE
}
