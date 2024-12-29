package windowSupplyInfo.buttonDelete;

import windowSupplyInfo.TableDataRefresher;
import windowSupplyInfo.WindowSupplyInfoMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static windowSupplyInfo.WindowSupplyInfoMaker.tableHeaders;

public class ButtonDeleteEditor extends DefaultCellEditor {
    private JButton button;
    private int row;

    public ButtonDeleteEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //останавливаем редактирование
                fireEditingStopped();

                //получаем данные о поставках из csv
                String[][] oldSuppliesData = TableDataRefresher.getSuppliesData();

                //делаем данные о поставках без одной удаленной строки
                String[][] newSuppliesData = new String[oldSuppliesData.length - 1][];
                for (int i = 0; i < row; i++) {
                    newSuppliesData[i] = oldSuppliesData[i];
                }
                for (int i = row; i < newSuppliesData.length; i++) {
                    newSuppliesData[i] = oldSuppliesData[i + 1];
                }

                //записываем новые данные о поставках в csv
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(WindowSupplyInfoMaker.suppliesFile));
                    //делаем нормальное отображение
                    bufferedWriter.write("\uFEFF");
                    //записываем заголовки
                    bufferedWriter.write(String.join(";", tableHeaders));
                    //записывае новые данные о поставках
                    for(String[] supply: newSuppliesData) {
                        bufferedWriter.newLine();
                        bufferedWriter.write(String.join(";", supply));
                    }
                    bufferedWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                //обновляем данные в отображаемой таблице
                TableDataRefresher.refreshTableData();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return button;
    }
}
