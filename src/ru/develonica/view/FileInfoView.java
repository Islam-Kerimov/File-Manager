package ru.develonica.view;

import ru.develonica.model.FileInfo;
import ru.develonica.model.FileType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Set.of;
import static ru.develonica.model.FileType.ARCHIVE;
import static ru.develonica.model.FileType.DIR;
import static ru.develonica.model.FileType.DOCUMENT;
import static ru.develonica.model.FileType.JAVA_FILE;
import static ru.develonica.model.FileType.MUSIC;
import static ru.develonica.model.FileType.PICTURE;
import static ru.develonica.model.FileType.TEXT;
import static ru.develonica.model.FileType.VIDEO;

/**
 * Отображение пользователю таблицы со списком
 * файлов и директорий в выбранной позиции.
 */
public class FileInfoView {

    /** Делитель для выбора подходящего измерения размера. */
    public static final int DIVIDER = 1024;

    /** Список измерений размеров. */
    private static final String[] UNIT = {"bytes", "Kb", "Mb", "Gb", "Tb"};

    /** Список аттрибутов. */
    private static final String[] ATTRIBUTES = {"r", "w", "x"};

    /** Словарь типов объектов. */
    private static final Map<String, FileType> FILE_TYPES;

    /** Типы изображений. */
    private static final Set<String> PICTURE_EXTENSIONS = of("bmp,png,jpeg");

    /** Типы текстовых файлов. */
    private static final Set<String> TEXT_EXTENSIONS = of("txt,text,log");

    /** Типы документов. */
    private static final Set<String> DOCUMENT_EXTENSIONS = of("doc,docs,xls,pdf");

    /** Типы архивированных файлов. */
    private static final Set<String> ARCHIVE_EXTENSIONS = of("zip,rar,7z");

    /** Тип музыки. */
    private static final Set<String> MUSIC_EXTENSIONS = of("mp3");

    /** Типы файлов видео. */
    private static final Set<String> VIDEO_EXTENSIONS = of("avi,mov,mp4,mpg");

    /** Типы java файлов. */
    private static final Set<String> JAVA_EXTENSIONS = of("mpg,java,jar");

    // Имена заголовков таблицы
    private static final String NAME = "Name";
    private static final String FILE_TYPE = "Type";
    private static final String FILE_SIZE = "Size";
    private static final String ATTRIBUTE = "Attribute";
    private static final String TOTAL_OBJECTS = "Total objects";
    private static final String TOTAL_SIZE = "Total size";

    // Разделители таблицы
    private static final String SPACE = " ";
    private static final String LINE_SEPARATOR = "-";
    private static final String COLUMN_SEPARATOR = "|";
    private static final String TOP_LEFT_EDGE_TABLE = "┌";
    private static final String TOP_RIGHT_EDGE_TABLE = "┐";
    private static final String BOTTOM_LEFT_EDGE_TABLE = "└";
    private static final String BOTTOM_RIGHT_EDGE_TABLE = "┘";
    private static final String TOP_COLUMNS_SEPARATOR = "┬";
    private static final String BOTTOM_COLUMNS_SEPARATOR = "┴";

    // Размеры столбцов таблицы
    private static final int NAME_COLUMN_LENGTH = 64;
    private static final int TYPE_COLUMN_LENGTH = 10;
    private static final int SIZE_COLUMN_LENGTH = 10;
    private static final int ATTRIBUTE_COLUMN_LENGTH = 9;
    private static final int TOTAL_OBJECTS_COLUMN_LENGTH = 13;
    private static final int TOTAL_SIZE_COLUMN_LENGTH = 10;
    private static final int MAX_SIZE_COLUMN = 64;
    private static final int MAX_SIZE_TOTAL_COLUM = 96;

    // Строки-шаблоны для красивого отображения границ таблицы
    private static final String TABLE_TITLE_TOP_LINE;
    private static final String TABLE_TITLE;
    private static final String TABLE_TITLE_BOTTOM_LINE;
    private static final String COLUMN;
    private static final String LINE_AFTER_COLUMN;
    private static final String LAST_LINE_AFTER_COLUMN;
    private static final String TOTAL_COLUMN;
    private static final String LINE_AFTER_TOTAL_COLUMN;

    static {
        // инициализация типов объектов
        FILE_TYPES = new HashMap<>();
        PICTURE_EXTENSIONS.forEach(extension -> FILE_TYPES.put(extension, PICTURE));
        TEXT_EXTENSIONS.forEach(extension -> FILE_TYPES.put(extension, TEXT));
        DOCUMENT_EXTENSIONS.forEach(extension -> FILE_TYPES.put(extension, DOCUMENT));
        ARCHIVE_EXTENSIONS.forEach(extension -> FILE_TYPES.put(extension, ARCHIVE));
        MUSIC_EXTENSIONS.forEach(extension -> FILE_TYPES.put(extension, MUSIC));
        VIDEO_EXTENSIONS.forEach(extension -> FILE_TYPES.put(extension, VIDEO));
        JAVA_EXTENSIONS.forEach(extension -> FILE_TYPES.put(extension, JAVA_FILE));

        // formatters для отображения таблицы и его данных
        TABLE_TITLE_TOP_LINE = format("%s%%1$.%ds%9$s%%1$.%ds%9$s%%1$.%ds%9$s%%1$.%ds%9$s%%1$.%ds%9$s%%1$.%ds%s%n",
                TOP_LEFT_EDGE_TABLE,
                NAME_COLUMN_LENGTH,
                TYPE_COLUMN_LENGTH,
                SIZE_COLUMN_LENGTH,
                ATTRIBUTE_COLUMN_LENGTH,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH,
                TOP_RIGHT_EDGE_TABLE,
                TOP_COLUMNS_SEPARATOR);

        TABLE_TITLE = format("%s%%-%ds%1$s%%-%ds%1$s%%-%ds%1$s%%-%ds%1$s%%-%ds%1$s%%-%ds%1$s%n",
                COLUMN_SEPARATOR,
                NAME_COLUMN_LENGTH,
                TYPE_COLUMN_LENGTH,
                SIZE_COLUMN_LENGTH,
                ATTRIBUTE_COLUMN_LENGTH,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH);

        TABLE_TITLE_BOTTOM_LINE = format("%s%%1$.%ds%1$s%%1$.%ds%1$s%%1$.%ds%1$s%%1$.%ds%1$s%%1$.%ds%1$s%%1$.%ds%1$s%n",
                COLUMN_SEPARATOR,
                NAME_COLUMN_LENGTH,
                TYPE_COLUMN_LENGTH,
                SIZE_COLUMN_LENGTH,
                ATTRIBUTE_COLUMN_LENGTH,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH);

        COLUMN = format("%s%%-%d.%ds%1$s%%-%ds%1$s%%-%ds%1$s%%-%ds%1$s%%5$%ds%1$s%%5$%ds%1$s%n",
                COLUMN_SEPARATOR,
                NAME_COLUMN_LENGTH,
                MAX_SIZE_COLUMN,
                TYPE_COLUMN_LENGTH,
                SIZE_COLUMN_LENGTH,
                ATTRIBUTE_COLUMN_LENGTH,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH);

        LINE_AFTER_COLUMN = format("%s%%1$.%ds%1$s%%1$.%ds%1$s%%1$.%ds%1$s%%1$.%ds%1$s%%2$%ds%1$s%%2$%ds%1$s%n",
                COLUMN_SEPARATOR,
                NAME_COLUMN_LENGTH,
                TYPE_COLUMN_LENGTH,
                SIZE_COLUMN_LENGTH,
                ATTRIBUTE_COLUMN_LENGTH,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH);

        LAST_LINE_AFTER_COLUMN = format("%s%%1$.%ds%8$s%%1$.%ds%8$s%%1$.%ds%8$s%%1$.%ds%1$s%%1$.%ds%1$s%%1$.%ds%1$s%n",
                COLUMN_SEPARATOR,
                NAME_COLUMN_LENGTH,
                TYPE_COLUMN_LENGTH,
                SIZE_COLUMN_LENGTH,
                ATTRIBUTE_COLUMN_LENGTH,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH,
                BOTTOM_COLUMNS_SEPARATOR);

        TOTAL_COLUMN = format("%s%%%ds%1$s%%-%d.%3$ds%1$s%%-%d.%4$ds%1$s%n",
                COLUMN_SEPARATOR,
                MAX_SIZE_TOTAL_COLUM,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH);

        LINE_AFTER_TOTAL_COLUMN = format("%s%%1$.%ds%s%%1$.%ds%3$s%%1$.%ds%s%n",
                BOTTOM_LEFT_EDGE_TABLE,
                MAX_SIZE_TOTAL_COLUM,
                BOTTOM_COLUMNS_SEPARATOR,
                TOTAL_OBJECTS_COLUMN_LENGTH,
                TOTAL_SIZE_COLUMN_LENGTH,
                BOTTOM_RIGHT_EDGE_TABLE);
    }

    /**
     * Отображение пользователю таблицы со списком
     * файлов и директорий в текущей позиции.
     *
     * @param child  список объектов
     * @param length длина всего списка объектов
     */
    public void displayAllDocuments(List<FileInfo> child, int length) {
        out.printf(TABLE_TITLE_TOP_LINE, LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN));
        out.printf(TABLE_TITLE, NAME, FILE_TYPE, FILE_SIZE, ATTRIBUTE, TOTAL_OBJECTS, TOTAL_SIZE);
        out.printf(TABLE_TITLE_BOTTOM_LINE, LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN));

        // выводить список объектов построчно
        for (int i = 0; i < length; i++) {
            FileInfo info;
            try {
                info = child.get(i);

                out.printf(COLUMN,
                        info.getName(),
                        convertType(info.getType(), info.getName()),
                        convertSize(info.getFileSize()),
                        convertAttribute(info.getAttribute()),
                        SPACE);
                if (i != length - 1) {
                    out.printf(LINE_AFTER_COLUMN, LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN), SPACE);
                } else {
                    out.printf(LAST_LINE_AFTER_COLUMN, LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN));
                }
            } catch (IndexOutOfBoundsException e) {
                // если пул потоков еще не заполнил файловое дерево всеми объектами в текущей директории
                i--;
            }
        }
    }

    /**
     * Отображение пользователю конечного результата
     * с общими данными текущей директории.
     *
     * @param totalInfo список всех объектов и общего размера
     *                  текущей директории файлового дерева.
     */
    public void displayTotalSizeAndObjects(List<Long> totalInfo) {
        out.printf(TOTAL_COLUMN, SPACE, totalInfo.get(0), convertSize(totalInfo.get(1)));
        out.printf(LINE_AFTER_TOTAL_COLUMN, LINE_SEPARATOR.repeat(MAX_SIZE_TOTAL_COLUM));
    }

    /**
     * Конвертация размера объекта.
     *
     * @param convertSize размер объекта
     * @return строку с подходящим размером и измерением.
     */
    private String convertSize(long convertSize) {
        int count = 0;
        while (convertSize >= DIVIDER) {
            convertSize /= DIVIDER;
            count++;
        }
        return convertSize + " " + UNIT[count];
    }

    /**
     * Получение аттрибут объекта.
     *
     * @param fileAttributes аттрибуты
     * @return строку с действующими аттрибутами
     */
    private String convertAttribute(Boolean[] fileAttributes) {
        StringBuilder attribute = new StringBuilder();

        for (int i = 0; i < fileAttributes.length; i++) {
            if (fileAttributes[i]) {
                attribute.append(ATTRIBUTES[i]);
            }
        }

        return attribute.toString();
    }

    /**
     * Получение конкретного типа объекта
     * из словаря типов <code>TYPE_TYPES</code>.
     *
     * @param fileType тип файла
     * @param file имя файла
     * @return конкретный тип файла
     */
    private FileType convertType(FileType fileType, String file) {
        if (!fileType.equals(DIR)) {
            // получить расширение объекта
            String extension = "";
            if (file.lastIndexOf(".") != -1
                    && file.lastIndexOf(".") != 0) {

                extension = file.substring(file.lastIndexOf(".") + 1);
            }
            // определить конкретный его тих
            for (Map.Entry<String, FileType> map : FILE_TYPES.entrySet()) {
                if (extension.equals(map.getKey())) {
                    fileType = map.getValue();
                    return fileType;
                }
            }
        }

        return fileType;
    }
}
