package general;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

import javax.swing.*;
import java.util.logging.Level;


public class GoogleChromeLoginChecker {
    public static boolean checkIsSellerLogin(BrowserContext browserContext) {
        // Получаем последнюю открытую страницу
        Page page = browserContext.pages().get(0);

        //если в ссылке есть слово about-portal или seller-auth, то просим пользователя залогиниться
        String currentUrl = page.url();
        if (currentUrl.contains("about-portal") || currentUrl.contains("seller-auth")) {
            LoggerConfiguration.logger.log(Level.WARNING, "Войдите в кабинет продавца. После входа нажмите 'ОК'");
            JOptionPane.showMessageDialog(WindowStart.frameStart, "Войдите в кабинет продавца. После входа нажмите 'ОК'");
            return false;
        }  else {
            LoggerConfiguration.logger.log(Level.INFO, "Вы залогинены в кабинете продавца");
            JOptionPane.showMessageDialog(WindowStart.frameStart, "Вы залогинены в кабинете продавца");
        }
        return true;
    }
}
