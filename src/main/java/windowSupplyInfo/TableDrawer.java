package windowSupplyInfo;

import windowSupplyInfo.buttonDelete.ButtonDeleteEditor;
import windowSupplyInfo.buttonDelete.ButtonDeleteRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class TableDrawer {
    public static JTable table;
    public static DefaultTableModel tableModel;


    public static void drawTable(GridBagConstraints gbc, JPanel panel) {
        //задаем данные заголовков для таблицы
        String[] columnNamesTable = new String[]{
                "Номер заказа",
                "Тип короба",
                "Макс. коэффициент",
                "ID склада",
                "Статус бронирования",
                "Даты",
                "Удаление"
        };


        //создаем модель таблицы с заголовками
        tableModel = new DefaultTableModel(columnNamesTable, 0);
        table = new JTable(tableModel);

        //делаем кнопки удаления
        table.getColumn("Удаление").setCellRenderer(new ButtonDeleteRenderer());
        table.getColumn("Удаление").setCellEditor(new ButtonDeleteEditor(new JCheckBox()));

        //делаем таблице возможность прокрутки
        JScrollPane scrollPane = new JScrollPane(table);

        //задаем расположение таблицы
        //Указываем, что таблица будет размещена в первой колонке (индекс 0) сетки
        gbc.gridx = 0;
        //указываем, что таблица будет размещена в 9 ячейке сетки, считая сверху
        gbc.gridy = 38;
        //указываем, что ячейка занимает всю доступную ширину
        gbc.weightx = 1;
        //указываем, что ячейка занимает всю доступную длину
        gbc.weighty = 1;
        //указываем, что таблица занимает все доступное пространство в ячейке, которой он размещен
        gbc.fill = GridBagConstraints.BOTH;

        //добавляем scrollPane в panel с настройками gbc
        panel.add(scrollPane, gbc);

        TableDataRefresher.refreshTableData();
    }
}
