package ru.develonica.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Из объектов данного класса состоит файловое дерево
 */
public final class Node {

    /**
     * Объект, который хранит всю необходимую информацию для
     * отображения пользователю.
     */
    final List<FileInfo> fileInfo;

    /** Текущая директория в структуре файлового дерева. */
    final String currentFolder;

    /**
     * Реальный путь к директории, т.к. папка может быть
     * символьной ссылкой.
     */
    final String realPath;

    /** Файлы и папки находящиеся в текущей директории файлового дерева. */
    final List<Node> children;

    /** Ссылка на родительскую папку. */
    Node parent;

    /** Общий размер текущей директории. */
    long totalSize;

    /** Общее количество объектов в текущей директории. */
    int totalObjects;

    /** Проверка, добавлена ли директория в файловое дерево. */
    boolean isExists;

    /** Проверка, заполнена ли директория всеми объектами. */
    boolean isFilesExist;

    Node(String currentFolder, String realPath) {
        this.currentFolder = currentFolder;
        this.realPath = realPath;
        this.children = new ArrayList<>();
        this.fileInfo = new ArrayList<>();
    }
}
