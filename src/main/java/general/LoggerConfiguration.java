package general;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfiguration {
    public static final Logger logger = Logger.getLogger(LoggerConfiguration.class.getName());
    private static final String LOG_DIRECTORY = System.getProperty("user.dir") + File.separator + "logs";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    //настраиваем логгер
    public static void setup() {
        //если папки logs не существует, то создаем
        if(!Files.isDirectory(Path.of(LOG_DIRECTORY))) {
            try {
                Files.createDirectory(Path.of(LOG_DIRECTORY));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            String dateString = dateFormatter.format(new Date());
            String logFilePath = LOG_DIRECTORY + File.separator + "log_" + dateString + ".log";

            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
