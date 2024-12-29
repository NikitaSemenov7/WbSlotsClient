package general;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import static general.LoggerConfiguration.logger;

public class GoogleChromeOpener {

    public static BrowserContext openGoogleChrome() {
        System.out.println("Началось выполнение метода openGoogleChrome");
        logger.log(Level.INFO, "Началось выполнение метода openGoogleChrome");
        //создаем объект PlayWright
        Playwright playwright = null;
        try {
            playwright = Playwright.create();
        } catch (Exception e) {
            System.out.println("Ошибка создания объекта Playwright. e.getMessage - " + e.getMessage() + "." + " e.getCause - " + e.getCause());
            logger.log(Level.SEVERE, "Ошибка создания объекта Playwright. e.getMessage - " + e.getMessage() + "." + " e.getCause - " + e.getCause());
        }
        System.out.println("Создан объект Playwright " + playwright);
        logger.log(Level.INFO, "Создан объект Playwright " + playwright);

        String chromeProfilesDir = System.getProperty("user.dir") + File.separator + "chromeProfiles";
        System.out.println("Установлен путь в переменную chromeProfilesDir = " + chromeProfilesDir);
        logger.log(Level.INFO, "Установлен путь в переменную chromeProfilesDir = " + chromeProfilesDir);

        //если chromeProfilesDir не существует, то создаем
        if(!Files.isDirectory(Path.of(chromeProfilesDir))) {
            try {
                Files.createDirectory(Path.of(chromeProfilesDir));
                logger.log(Level.INFO,"Папка " + System.getProperty("user.dir") + File.separator + "chromeProfiles создана");
                System.out.println("Папка " + System.getProperty("user.dir") + File.separator + "chromeProfiles создана");
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Была попытка создать папку " + System.getProperty("user.dir") + File.separator + "chromeProfiles. Произошла ошибка" + e.getCause());
                System.out.println("Была попытка создать папку " + System.getProperty("user.dir") + File.separator + "chromeProfiles. Произошла ошибка" + e.getCause());
                throw new RuntimeException(e);
            }
        }

        System.out.println("Сейчас будет вызван метод getChromePath");
        logger.log(Level.INFO, "Сейчас будет вызван метод getChromePath");
        //получаем путь к Google Chrome
        String chromePath = getChromePath();

        //создаем BrowserContext
        BrowserContext browserContext = null;
        if (chromePath != null) {
            BrowserType.LaunchPersistentContextOptions options = new BrowserType.LaunchPersistentContextOptions()
                    .setHeadless(false)
                    .setExecutablePath(Path.of(chromePath));

            //открываем Google Chrome
            browserContext = playwright.chromium().launchPersistentContext(Path.of(chromeProfilesDir), options);
            logger.log(Level.INFO, "Google Chrome открыт");
            JOptionPane.showMessageDialog(WindowStart.frameStart, "Google Chrome открыт");

            Page page = browserContext.pages().get(0);
            page.navigate("https://seller.wildberries.ru/supplies-management/all-supplies");

            //ждем, пока сеть станет неактивной (то есть все страницы загружены)
            //если закомментировать, то на следующем этапе при проверке страница с редиректом или нет, всегда думает, что без редиректа
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } else {
            logger.log(Level.SEVERE, "Google Chrome не установлен на этом компьютере по стандартному пути");
            JOptionPane.showMessageDialog(WindowStart.frameStart, "Google Chrome не установлен на этом компьютере по стандартному пути. Скачать и установить Google Chrome можно по ссылке https://www.google.com.jm/chrome/");
        }
        return browserContext;
    }

    private static String getChromePath() {
        //получаем fileName в зависимости от операционной системы
        String fileName;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // На Windows
            fileName = "chrome.exe";
            JOptionPane.showMessageDialog(WindowStart.frameStart, "После нажатия 'OK' произойдет поиск файла " + fileName + " на вашем компьютере");
            logger.log(Level.INFO, "Определена операционная система Windows. Начинаем поиск файла " + fileName + " на всём компьютере");
            System.out.println("Определена операционная система Windows. Начинаем поиск файла " + fileName + " на всём компьютере");

            // Определяем начальные директории в зависимости от ОС
            File[] roots = File.listRoots();

            // Ищем файл в каждой корневой директории
            for (File root : roots) {
                File result = findFile(root, fileName);
                if (result != null) {
                    System.out.println("Файл " + fileName + " найден: " + result.getAbsolutePath());
                    logger.log(Level.INFO, "Файл " + fileName + " найден: " + result.getAbsolutePath());

                    //сохраняем путь к chrome.exe
                    return result.getAbsolutePath(); // Останавливаем поиск после нахождения файла
                }
            }
            logger.log(Level.SEVERE, "Файл " + fileName + " не найден. Установите Google Chrome на ваш компьютер");
            System.out.println("Файл " + fileName + " не найден. Установите Google Chrome на ваш компьютер");
            JOptionPane.showMessageDialog(WindowStart.frameStart, "Файл " + fileName + " не найден. Установите Google Chrome на ваш компьютер");
            return null;
        } else {
            // Не Windows
            logger.log(Level.SEVERE, "Открываем Google Chrome по адресу /Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
            System.out.println("Открываем Google Chrome по адресу /Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
            return "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
        }

    }

    // Метод для поиска файла в заданной директории (рекурсивно)
    private static File findFile(File directory, String fileName) {
        if (!directory.exists() || !directory.isDirectory()) {
            return null; // Если директория не существует или это не папка, выходим
        }

        File[] files = directory.listFiles(); // Получаем список файлов и папок
        if (files == null) {
            return null; // Если доступ к папке ограничен (например, нет прав), выходим
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Если это папка, рекурсивно ищем в ней
                File found = findFile(file, fileName);
                if (found != null) {
                    return found; // Если файл найден, возвращаем его
                }
            } else if (file.getName().equalsIgnoreCase(fileName)) {
                // Если это файл и его имя совпадает, возвращаем его
                return file;
            }
        }
        return null; // Если файл не найден в этой директории
    }
}
