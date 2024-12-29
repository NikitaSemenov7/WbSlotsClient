package general;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static windowSupplyInfo.WindowSupplyInfoMaker.suppliesFile;

public class SuitableSlotFounder {
    public static void findSiutableSlot(List<Slot> slotsList) {
        //считываем данные об имеющихся поставках из csv файла и обновляем таблицу
        if (!suppliesFile.isFile()) {
            System.out.println("Файл с поставками не создан");
            return;
        }

        //считываем поставки из csv
        String[][] suppliesInfo = new String[][]{};

        try (BufferedReader br = new BufferedReader(new FileReader(suppliesFile))) {
            List<String[]> suppliesList = new ArrayList<String[]>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                suppliesList.add(values);
            }

            // Получение названий столбцов и данных
            if (!suppliesList.isEmpty()) {
                suppliesInfo = suppliesList.subList(1, suppliesList.size()).toArray(new String[0][]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //для каждой поставки из списка получаем ее макс. коэффициент, id склада, preOrder id
        for (int rowNum = 0; rowNum < suppliesInfo.length; rowNum++) {
            String[] supply = suppliesInfo[rowNum];
            int preOrderId = Integer.parseInt(supply[0]);
            int maxCoefficientToBook = Integer.parseInt(supply[2]);
            double warehouseIdToBook = Double.parseDouble(supply[3]);

            //получаем массив дат
            String[] arrayDatesString = supply[5].substring(1, supply[5].length() - 1).split(", ");
            ArrayList<LocalDate> dates = new ArrayList<>();
            for (String dateString: arrayDatesString) {
                LocalDate date = null;
                if(!dateString.equals("null")) {
                    date = LocalDate.parse(dateString);
                }
                dates.add(date);
            }

            System.out.println("Ищем подходящий коэффициент для поставки с preOrderId - " + preOrderId);
            LoggerConfiguration.logger.log(Level.INFO, "Ищем подходящий коэффициент для поставки с preOrderId - " + preOrderId);

            //в slotsList пробуем найти подходящий слот. Если нашли, то бронируем
            for (int i = 0; i <= maxCoefficientToBook; i++) {
                for(Slot slot: slotsList) {
                    int coefficientSlot = slot.getCoefficient();
                    double warehouseIdSlot = slot.getWarehouseId();
                    Date dateSlot = slot.getDate();
                    LocalDate localDateSlot = dateSlot.toInstant()
                            .atZone(ZoneId.systemDefault()) // Установка временной зоны
                            .toLocalDate(); // Получение LocalDate


                    //добавить в условие, что дата подходящая
                    if(warehouseIdSlot == warehouseIdToBook
                            && coefficientSlot == i
                            && dates.contains(localDateSlot)) {
                        System.out.println("Подходящий слот найден");
                        LoggerConfiguration.logger.log(Level.INFO, "Подходящий слот найден");
                        SupplyBooker.bookSupply(preOrderId, dateSlot, rowNum);
                        break;
                    }
                }
            }
        }
    }
}
