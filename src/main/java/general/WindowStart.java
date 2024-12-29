package general;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowStart {
    public static JFrame frameStart;
    public static String token;

    public static void main(String[] args) {
        // Создаем новое окно JFrame
        frameStart = new JFrame("Автобронирование поставок на Wildberries");
        frameStart.setSize(600, 100); // Установка размера окна
        frameStart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закрытие приложения при закрытии окна

        // Создаем панель, чтобы разместить компоненты
        JPanel panel = new JPanel(); // Создаем панель
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Устанавливаем вертикальную компоновку

        // Добавление метки
        JLabel label = new JLabel("Введите авторизационный токен", SwingConstants.CENTER);
        panel.add(label); // Добавление метки на панель

        JTextField textField = new JTextField();
        textField.setColumns(20); // Устанавливаем количество символов в поле
        panel.add(textField); // Добавление текстового поля на панель

        panel.add(textField); // Добавление текстовой области на панель

        // Создаем кнопку для подтверждения ввода
        JButton submitButton = new JButton("Далее");
        panel.add(submitButton); // Добавляем кнопку на панель

        // Добавляем слушатель действий для кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                token = textField.getText();
                WebSocketConnection.startWebSocketClient();
            }
        });

        // Добавляем панель в окно
        frameStart.getContentPane().add(panel); // Добавляем панель в контент окна
        frameStart.setVisible(true); // Делаем окно видимым
    }
}