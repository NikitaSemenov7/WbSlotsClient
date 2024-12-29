package general;

import javax.swing.*;
import javax.websocket.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.playwright.BrowserContext;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import windowSupplyInfo.WindowSupplyInfoMaker;

import static general.LoggerConfiguration.logger;
import static java.util.logging.Level.*;
import static java.util.logging.Level.INFO;


@ClientEndpoint
public class WebSocketConnection {
    private Session session;
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static boolean isSellerLogin = false;
    public static BrowserContext browserContext;

    public static void startWebSocketClient() {
        //настраиваем логгер
        LoggerConfiguration.setup();

        String uri = "wss://webexe.online/connect?token=" + WindowStart.token;
        WebSocketConnection client = new WebSocketConnection();
        boolean isConnected = client.connect(uri);
        System.out.println("is Connected установлено: " + isConnected);
        logger.log(INFO, "is Connected установлено: " + isConnected);
        if (isConnected) {
            //пытаемся открыть Google Chrome
            System.out.println("Вошли в блок if(isConnected). Сейчас будет вызван метод GoogleChromeOpener.openGoogleChrome()");
            logger.log(INFO, "Вошли в блок if(isConnected). Сейчас будет вызван метод GoogleChromeOpener.openGoogleChrome()");
            browserContext = GoogleChromeOpener.openGoogleChrome();
            if (browserContext != null) {
                while ((isSellerLogin = GoogleChromeLoginChecker.checkIsSellerLogin(browserContext)) == false) {
                    browserContext.close();
                    //открываем заного Google Chrome, чтобы обновился browserContext
                    browserContext = GoogleChromeOpener.openGoogleChrome();
                }
                if (isSellerLogin) {
                    WindowSupplyInfoMaker.showSupplyInfo();
                }
            }
        }
    }

    public boolean connect(String uri) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI(uri));
            JOptionPane.showMessageDialog(WindowStart.frameStart, "Токен принят. Соединение с сервером установлено");
            System.out.println("Токен принят. Соединение с сервером установлено");
            logger.log(INFO, "Токен принят. Соединение с сервером установлено");
            return true;
        } catch (Exception e) {
            String errorMessage = e.getCause().getMessage();
            JOptionPane.showMessageDialog(WindowStart.frameStart, "Ошибка: " + errorMessage);
            System.out.println("Ошибка: " + errorMessage);
            logger.log(SEVERE, "Ошибка: " + errorMessage);
            return false;
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.log(INFO, "Подключение с сервером установлено");
    }

    @OnMessage
    public void onMessage(String jsonResponse) {

        //сохраняем json в файл
        String jsonPath = System.getProperty("user.dir") + File.separator + "slots.json";
        if (!jsonResponse.equals("Client connected") && isSellerLogin) {
            logger.log(INFO, "Получен json ответ");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(gson.fromJson(jsonResponse, Object.class));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonPath))) {
                writer.write(prettyJson);
            } catch (Exception e) {
                e.printStackTrace();
            }


            //сохраняем json в лист с объектами slot
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(jsonPath);
            List<Slot> slotsList;
            try {
                slotsList = mapper.readValue(jsonFile, new TypeReference<List<Slot>>() {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //генерируем путь к папе journal
            String directoryName = System.getProperty("user.dir") + File.separator + "journal";

            //если папки journal нет, то создаем
            if(!Files.isDirectory(Path.of(directoryName))) {
                try {
                    Files.createDirectory(Path.of(directoryName));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //генерируем String путь к csv файлу журнала
            String fileName = directoryName + File.separator + dateFormatter.format(new Date()) + ".xlsx";

            //если файл в папке journal с текущей датой не существует, то создаем, записываем в него заголовки
            if (!Files.exists(Path.of(fileName))) {
                String[] headers = {"Время запроса", "Дата бронирования", "Коэффициент", "Название склада", "ID склада"};
                try (FileOutputStream fileOutputStream  = new FileOutputStream(fileName)) {
                    Workbook workbookJournal = new XSSFWorkbook();
                    Sheet sheetJournal = workbookJournal.createSheet();
                    Row rowHeaders = sheetJournal.createRow(0);
                    for (int i = 0; i < headers.length; i++) {
                        rowHeaders.createCell(i).setCellValue(headers[i]);
                    }
                    workbookJournal.write(fileOutputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //дописываем в файл слот
            try (FileInputStream fis = new FileInputStream(fileName);
                 Workbook workbook = new XSSFWorkbook(fis);
                 FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {

                Sheet sheet = workbook.getSheetAt(0);


                //записываем в sheet информацию о слотах
                writeSlots(slotsList, sheet);

                //записываем изменения в файл
                workbook.write(fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            SuitableSlotFounder.findSiutableSlot(slotsList);
        }
    }

    private void writeSlots(List<Slot> list, Sheet sheet) throws IOException {
        for (Slot slot : list) {
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);
            newRow.createCell(0).setCellValue(dateTimeFormatter.format(new Date()));
            Date date = slot.getDate();
            newRow.createCell(1).setCellValue(Months.values()[date.getMonth()] + " " + date.getDate());
            newRow.createCell(2).setCellValue(slot.getCoefficient());
            newRow.createCell(3).setCellValue(slot.getWarehouseName());
            newRow.createCell(4).setCellValue(slot.getWarehouseId());
        }
        logger.log(INFO, "Ответ записан");
    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.log(INFO, "Подключение закрыто: " + closeReason.getReasonPhrase()
                + " (статус код: " + closeReason.getCloseCode() + ")");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.log(SEVERE, "Ошибка: " + throwable.getMessage());
    }

}

