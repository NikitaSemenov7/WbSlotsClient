package windowSupplyInfo;

import windowSupplyInfo.entities.MapBoxType;
import windowSupplyInfo.entities.Supply;
import windowSupplyInfo.entities.Warehouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static windowSupplyInfo.WindowSupplyInfoMaker.*;

public class ButtonAddSupplyActionListener {
    public static void addActionListener(JButton buttonAddSupply, JTextField preOrderIdField, JComboBox<String> boxTypeNameField, JComboBox<Integer> maxCoefficientField, JComboBox<String> warehouseNameField, ArrayList<JTextField> dateFields, GridBagConstraints gbc, JPanel panel) {
        // Добавляем слушатель действий для кнопки
        buttonAddSupply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int preOrderId = Integer.parseInt(preOrderIdField.getText());
                String boxTypeName = (String) boxTypeNameField.getSelectedItem();
                int maxCoefficient = (int) maxCoefficientField.getSelectedItem();
                String warehouseNameTable = (String) warehouseNameField.getSelectedItem();

                //получаем даты
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                ArrayList<LocalDate> dates = new ArrayList<>();
                for(JTextField jTextField: dateFields) {
                    String dateString = jTextField.getText();
                    LocalDate date = null;
                    if(!dateString.equals("")) {
                        try {
                            date = LocalDate.parse(dateString, dateFormatter);
                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(panel, "Неверный формат даты: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    dates.add(date);
                }

                //определяем warehouseId скалад, который выбран пользователем в выпадающем списке
                double warehouseId = 0;
                for(Warehouse warehouse: DropListsAdder.warehousesList) {
                    String warehouseNameWarehousesList = warehouse.getWarehouseName();
                    if(warehouseNameWarehousesList.equals(warehouseNameTable)) {
                        warehouseId = warehouse.getWarehouseId();
                    }
                }

                Supply newSupply = new Supply(
                        preOrderId,
                        MapBoxType.mapBoxType.get(boxTypeName),
                        maxCoefficient,
                        warehouseId,
                        dates);



                //записываем поставку в supplies.csv
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(suppliesFile, true))) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(newSupply.getPreOrderId() + ";");
                    bufferedWriter.write(newSupply.getBoxTypeId() + ";");
                    bufferedWriter.write(newSupply.getMaxCoefficient() + ";");
                    bufferedWriter.write(newSupply.getWarehouseId() + ";");
                    bufferedWriter.write(newSupply.isBooked() + ";");
                    bufferedWriter.write(newSupply.getDates() + ";");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //обновляем данные отображаемой в приложении таблицы
                TableDataRefresher.refreshTableData();
            }
        });
    }
}
