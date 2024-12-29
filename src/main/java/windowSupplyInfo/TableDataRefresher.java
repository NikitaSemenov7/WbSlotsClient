package windowSupplyInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static windowSupplyInfo.TableDrawer.tableModel;
import static windowSupplyInfo.WindowSupplyInfoMaker.suppliesFile;

public class TableDataRefresher {
    public static void refreshTableData() {
        String[][] suppliesInfo = getSuppliesData();

        //очищаем текущие данные в модели таблицы
        tableModel.setRowCount(0);

        //добавляем новые данные в модель таблицы
        for(String[] row: suppliesInfo){
            tableModel.addRow(row);
        }
    }

    public static String[][] getSuppliesData() {
        String[][] suppliesInfo = new String[][]{};

        //считываем из csv данные о поставка
        try (BufferedReader br = new BufferedReader(new FileReader(suppliesFile))) {
            List<String[]> list = new ArrayList<String[]>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                //считываем все line из csv в list
                list.add(values);
            }

            if (!list.isEmpty()) {
                //помещаем информацию о поставках в двумерный массив
                suppliesInfo = list.subList(1, list.size()).toArray(new String[0][]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suppliesInfo;
    }
}
