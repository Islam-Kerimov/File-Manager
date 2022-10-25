package ru.develonica.model;

import java.util.List;

/**
 * Структура файлового дерева.
 */
public class FileTree {

    /** Указатель на первую директорию. */
    private Node first;

    /** Указатель на последнюю директорию. */
    private Node last;

    /**
     * Этот метод добавляет директорию в структуру файлового дерева,
     * относительно его реального расположения.
     *
     * @param currentFolder текущая директория
     * @param realPath      реальный путь папки с символьной ссылкой
     */
    public void addNode(String currentFolder, String realPath) {
        Node newNode = new Node(currentFolder, realPath);
        Node tempLast = last;
        if (tempLast == null) {
            first = newNode;
            last = newNode;
        } else {
            for (int i = 0; i < tempLast.children.size(); i++) {
                if (newNode.currentFolder
                        .equals(tempLast.children.get(i).currentFolder)) {

                    last = tempLast.children.get(i);
                    last.isExists = true;
                    break;
                }
            }
            if (!last.isExists) {
                tempLast.children.add(newNode);
                last = newNode;
                last.parent = tempLast;
            }
        }
    }

    public int getSize() {
        return last.fileInfo.size();
    }

    public String getCurrentFolder() {
        return last.currentFolder;
    }

    public String getRealPath() {
        return last.realPath;
    }

    public String getFirstPath() {
        return first.currentFolder;
    }

    public boolean isExists() {
        return last.isExists;
    }

    public void setExists(boolean exists) {
        last.isExists = exists;
    }

    public boolean isFilesExist() {
        return last.isFilesExist;
    }

    public void setFilesExist(boolean files) {
        last.isFilesExist = files;
    }

    public void getBack() {
        last = last.parent;
    }

    public void getBackFirst() {
        last = first;
    }

    public void addChild(FileInfo fileInfo) {
        last.fileInfo.add(fileInfo);
    }

    public void clean() {
        last.fileInfo.clear();
        last.totalObjects = 0;
        last.totalSize = 0;
    }

    public List<FileInfo> getFileInfo() {
        return last.fileInfo;
    }

    public long getTotalSize() {
        return last.totalSize;
    }

    public int getTotalObjects() {
        return last.totalObjects;
    }

    public void setTotalSize(long size) {
        this.last.totalSize = size;
    }

    public void setTotalObjects(int object) {
        this.last.totalObjects = object;
    }
}
