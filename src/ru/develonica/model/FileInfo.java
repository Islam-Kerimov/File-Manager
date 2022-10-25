package ru.develonica.model;

/**
 * Информация об объектах файлового дерева.
 */
public class FileInfo {

    /** Имя объекта. */
    private String name;

    /** Тип объект. */
    private FileType fileType;

    /** Размер объекта. */
    private long fileSize;

    /** Аттрибуты объекта. */
    private Boolean[] attribute;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return fileType;
    }

    public void setType(FileType fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean[] getAttribute() {
        return attribute;
    }

    public void setAttribute(Boolean[] attribute) {
        this.attribute = attribute;
    }

    public FileInfo() {
        this.attribute = new Boolean[3];
    }
}
