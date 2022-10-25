package ru.develonica.view;

import static java.lang.System.out;

/**
 * Класс Представление.
 */
public class FileManagerView {

    /** Выбор корневой директории для построения файлового дерева. */
    private static final String ROOT_POSITION = "Выберите начальную директорию или нажмите Enter, чтобы выбрать позицию по умолчанию: ";

    /** Подтверждение удаления файла/директории. */
    private static final String CONFIRM = "Вы уверены, что хотите удалить объект '%s'?\nЕсли ДА, то введите 'Y', иначе нажмите 'Enter' или любую другую клавишу: ";

    /**
     * Вывод сообщения об ошибке при работе с приложением.
     *
     * @param message текст ошибки.
     */
    public void errorMessage(String message) {
        out.println(message);
    }

    /** Вывод информации для выбора корневой директории. */
    public void chooseRoot() {
        out.print(ROOT_POSITION);
    }

    /**
     * Запрос подтверждения удаление файла/директории.
     *
     * @param object удаляемый объект.
     */
    public void confirmDelete(String object) {
        out.printf(CONFIRM, object);
    }
}
