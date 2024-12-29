package windowSupplyInfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class WindowSupplyInfoMaker {
    public static File suppliesFile = new File(System.getProperty("user.dir") + File.separator + "supplies" + File.separator + "supplies.csv");
    public static JFrame frameSupplies;
    public static String[] tableHeaders = new String[7];

    public static void showSupplyInfo() {
        // Создаем новое окно JFrame
        frameSupplies = new JFrame("Данные о поставках");
        frameSupplies.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закрытие приложения при закрытии окна
        frameSupplies.setSize(900, 750); // Установка размера окна

        // Создаем панель, чтобы разместить компоненты
        JPanel panel = new JPanel(); // Создаем панель
        panel.setLayout(new GridBagLayout()); // Устанавливаем вертикальную компоновку
        GridBagConstraints gbc = new GridBagConstraints();

        //установка отступов и заполнения
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Добавление метки
        JLabel label = new JLabel("<html>1. Создайте на Wildberries поставки, но не бронируйте коэффициенты<br>2. Введите данные о созданных поставках.<br>В первую очередь вводите данные о наиболее приоритетных поставках<br>(если в одну секунду придут подходящие коэффициенты для 2 поставок, то будет забронирована сначала поставка, добавленная раньше)</html>", SwingConstants.CENTER);
        //Указываем, что метка будет размещена в первой колонке (индекс 0) сетки, управляемой GridBagLayout
        gbc.gridx = 0;
        //Указываем, что метка будет занимать две колонки в сетке
        gbc.gridwidth = 2;
        panel.add(label, gbc); // Добавление метки на панель

        //добавляем заголовки для выдапающих списков
        String[] lines = {
                "'Номер заказа' (смотреть в кабинете в данных о поставке)",
                "Тип упаковки",
                "Максимальный допустимый коэффициент",
                "Название склада",
                "Подходящая дата 1 в формате дд.мм.гг",
                "Подходящая дата 2 в формате дд.мм.гг",
                "Подходящая дата 3 в формате дд.мм.гг",
                "Подходящая дата 4 в формате дд.мм.гг",
                "Подходящая дата 5 в формате дд.мм.гг",
                "Подходящая дата 6 в формате дд.мм.гг",
                "Подходящая дата 7 в формате дд.мм.гг",
                "Подходящая дата 8 в формате дд.мм.гг",
                "Подходящая дата 9 в формате дд.мм.гг",
                "Подходящая дата 10 в формате дд.мм.гг",
                };

        //создаем пустые выпадающие списки
        JTextField preOrderIdField = new JTextField(10);
        JComboBox<String> boxTypeNameField = new JComboBox<>();
        JComboBox<Integer> maxCoefficientField = new JComboBox<>();
        JComboBox<String> warehouseNameField = new JComboBox<>();
        JTextField date1Field = new JTextField(10);
        JTextField date2Field = new JTextField(10);
        JTextField date3Field = new JTextField(10);
        JTextField date4Field = new JTextField(10);
        JTextField date5Field = new JTextField(10);
        JTextField date6Field = new JTextField(10);
        JTextField date7Field = new JTextField(10);
        JTextField date8Field = new JTextField(10);
        JTextField date9Field = new JTextField(10);
        JTextField date10Field = new JTextField(10);

        ArrayList<JTextField> dateFields = new ArrayList<>();
        dateFields.add(date1Field);
        dateFields.add(date2Field);
        dateFields.add(date3Field);
        dateFields.add(date4Field);
        dateFields.add(date5Field);
        dateFields.add(date6Field);
        dateFields.add(date7Field);
        dateFields.add(date8Field);
        dateFields.add(date9Field);
        dateFields.add(date10Field);

        //добавляем выпадающие списки
        DropListsAdder.addDropLists(lines, gbc, panel, preOrderIdField, boxTypeNameField, maxCoefficientField, warehouseNameField, dateFields);

        //создаем кнопку для добавления поставки
        JButton buttonAddSupply = new JButton("Добавить поставку");

        // Настраиваем параметры для кнопки
        //Указываем, что метка будет размещена в первой колонке (индекс 0) сетки
        gbc.gridx = 0;
        // Устанавливаем позицию после всех полей
        gbc.gridy = lines.length + 2;
        // Растягиваем на две колонки
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // Центрируем кнопку
        gbc.fill = GridBagConstraints.NONE; // Отменяем растягивание
        gbc.insets = new Insets(20, 5, 5, 5); // Добавляем отступ сверху

        panel.add(buttonAddSupply, gbc); // Добавляем кнопку на панель

        //если не существует папка supplies, то создаем
        if(!Files.isDirectory(Path.of(System.getProperty("user.dir") + File.separator + "supplies"))) {
            try {
                Files.createDirectory(Path.of(System.getProperty("user.dir") + File.separator + "supplies"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //заполняем массив заголовками csv файла
        tableHeaders[0] = "Номер заказа";
        tableHeaders[1] = "Тип короба";
        tableHeaders[2] = "Макс. коэффициент";
        tableHeaders[3] = "ID склада";
        tableHeaders[4] = "Статус бронирования";
        tableHeaders[5] = "Подходящие даты";

        //если файла supplies.csv нет, то создаем его и записываем в него заголовки столбцов
        if (!suppliesFile.isFile()) {
            try {
                suppliesFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //открываем supplies.csv
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(suppliesFile, true))) {
                //устанавливаем нормальное отображение
                bufferedWriter.write("\uFEFF");
                //записываем заголовки
                bufferedWriter.write(String.join(";", tableHeaders));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        TableDrawer.drawTable(gbc, panel);

        //добавляем функционал кнопке "Добавить поставку"
        ButtonAddSupplyActionListener.addActionListener(buttonAddSupply, preOrderIdField, boxTypeNameField, maxCoefficientField, warehouseNameField, dateFields, gbc, panel);

        //добавляем возможность скролла
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //добавляем panel во frameSupplies и устанавливаем видимость
        frameSupplies.getContentPane().add(scrollPane);
        frameSupplies.setVisible(true);
    }
}

