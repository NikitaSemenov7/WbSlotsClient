package general;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import static windowSupplyInfo.TableDrawer.table;

public class SupplyBooker {
    public static void bookSupply(int preOrderId, Date dateSlot, int rowNum){
        //нажимаем по preOrderId (по потсавке)
        WebSocketConnection.browserContext.pages().get(0).navigate("https://seller.wildberries.ru/supplies-management/all-supplies/supply-detail/uploaded-goods?preorderId=" + preOrderId);

        //нажимаем забронировать поставку
        WebSocketConnection.browserContext.pages().get(0).getByText("Запланировать поставку").click();

        //переводим дату в формат как подписаны слоты в кабинете
        SimpleDateFormat formatter = new SimpleDateFormat("d MMMM, E", new Locale("ru", "RU"));
        String dateString = formatter.format(dateSlot);
        System.out.println("Будем бронировать дату - " + dateString);
        LoggerConfiguration.logger.log(Level.INFO, "Будем бронировать дату - " + dateString);


        WebSocketConnection.browserContext.pages().get(0)
                .locator("//td[contains(@class, 'Calendar-cell__3yrLIkkTHO')]//span[contains(text(), '" + dateString + "')]")
                .hover();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Ищем и нажимаем кнопку Выбрать внутри этого элемента
        WebSocketConnection.browserContext.pages().get(0)
                .locator("//td[.//*[contains(text(),'" + dateString + "')]]//button")
                .click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //нажимаем Запланировать
        WebSocketConnection.browserContext.pages().get(0)
                .locator("//button[.//*[text()='Запланировать']]")
                .click();

        //ждем 2 минуты загрузки страницы
        try {
            Thread.sleep(90000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //проверяем есть ли на странице слово Запланировано
        boolean isContainsZaplanirovano = WebSocketConnection.browserContext.pages().get(0).content().contains("Запланировано");

        System.out.println("Содержит ли страница Запланировано: " + isContainsZaplanirovano);
        LoggerConfiguration.logger.log(Level.INFO, "Содержит ли страница Запланировано: " + isContainsZaplanirovano);

        if(isContainsZaplanirovano) {
            //удаляем поставку симуляцией нажатия на кнопку удалить
            deleteSupply(rowNum);
            System.out.println("Содержит ли страница Запланировано: " + isContainsZaplanirovano);
            LoggerConfiguration.logger.log(Level.INFO, "Содержит ли страница Запланировано: " + isContainsZaplanirovano);
        }
    }

    private static void deleteSupply(int rowNum) {
        // Start editing the cell (this will trigger the button renderer/editor)
        table.editCellAt(rowNum, 7);

        // Retrieve the editor component (your DeleteButtonEditor button)
        Component editorComponent = table.getEditorComponent();
        if (editorComponent instanceof JButton) {
            JButton deleteButton = (JButton) editorComponent;

            // Simulate a button click
            deleteButton.doClick();
        }

        // Stop editing the cell to finalize the action
        table.getCellEditor().stopCellEditing();
    }
}
