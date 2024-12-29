package windowSupplyInfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import windowSupplyInfo.entities.Warehouse;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static general.WindowStart.token;

public class DropListsAdder {
    public static List<Warehouse> warehousesList = new ArrayList<>();

    public static void addDropLists(String[] lines, GridBagConstraints gbc, JPanel panel, JTextField preOrderIdField, JComboBox<String> boxTypeNameField, JComboBox<Integer> maxCoefficientField, JComboBox<String> warehouseNameField, ArrayList<JTextField> dateFileds) {
        //добавляем выпадающие списки
        for (int i = 0; i < lines.length; i++) {
            JLabel warehousesLabel = new JLabel(lines[i], SwingConstants.LEFT);
            gbc.gridx = 0; // Первая колонка
            gbc.gridwidth = 1; // Одна ячейка
            gbc.gridy = i + 1; // Индекс строки
            panel.add(warehousesLabel, gbc);

            gbc.gridx = 1; // Вторая колонка для коэффициента
            switch (i) {
                case 0:
                    panel.add(preOrderIdField, gbc);
                    break;
                case 1:
                    boxTypeNameField.addItem("Короба");
                    boxTypeNameField.addItem("Монопаллеты");
                    boxTypeNameField.addItem("Суперсейф");
                    panel.add(boxTypeNameField, gbc);
                    break;
                case 2:
                    for (int j = 0; j <= 20; j++) {
                        maxCoefficientField.addItem(j);
                    }
                    panel.add(maxCoefficientField, gbc);
                    break;
                case 3:
                    //делаем http запрос на сервер для получения информации о складах
                    String urlString = "https://webexe.online/api/clients/warehouses?token=" + token;
                    try {
                        // Выполняем HTTP GET запрос
                        URL url = new URL(urlString);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = connection.getInputStream();
                            String jsonResponse = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
                            inputStream.close();

                            //сохраняем json файл
                            String jsonPath = System.getProperty("user.dir") + File.separator + "warehouses.json";
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String prettyJson = gson.toJson(gson.fromJson(jsonResponse, Object.class));
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonPath))){
                                writer.write(prettyJson);
                            }

                            //считываем из json в список объектов
                            ObjectMapper mapper = new ObjectMapper();
                            File jsonFile = new File(jsonPath);
                            warehousesList = mapper.readValue(jsonFile, new TypeReference<List<Warehouse>>() {});
                        } else {
                            System.out.println("Ошибка HTTP загрузка названий и id складов: " + responseCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //добавляем список складов в выпадающий список
                    for(Warehouse warehouse: warehousesList) {
                        warehouseNameField.addItem(warehouse.getWarehouseName());
                    }
                    panel.add(warehouseNameField, gbc);
                    break;
                case 4:
                    panel.add(dateFileds.get(0), gbc);
                    break;
                case 5:
                    panel.add(dateFileds.get(1), gbc);
                    break;
                case 6:
                    panel.add(dateFileds.get(2), gbc);
                    break;
                case 7:
                    panel.add(dateFileds.get(3), gbc);
                    break;
                case 8:
                    panel.add(dateFileds.get(4), gbc);
                    break;
                case 9:
                    panel.add(dateFileds.get(5), gbc);
                    break;
                case 10:
                    panel.add(dateFileds.get(6), gbc);
                    break;
                case 11:
                    panel.add(dateFileds.get(7), gbc);
                    break;
                case 12:
                    panel.add(dateFileds.get(8), gbc);
                    break;
                case 13:
                    panel.add(dateFileds.get(9), gbc);
                    break;
                case 14:
                    panel.add(dateFileds.get(10), gbc);
                    break;
            }
        }
    }
}
