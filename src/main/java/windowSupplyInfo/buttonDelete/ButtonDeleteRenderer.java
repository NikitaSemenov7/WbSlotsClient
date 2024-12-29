package windowSupplyInfo.buttonDelete;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonDeleteRenderer extends JButton implements TableCellRenderer {
    public ButtonDeleteRenderer() {
        setText("Удалить");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //возвращаем кнопку
        return this;
    }
}
