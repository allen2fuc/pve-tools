package asia.chengfu.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CheckBoxRenderer extends DefaultTableCellRenderer {
    private final JCheckBox checkBox;

    public CheckBoxRenderer() {
        checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Boolean) {
            checkBox.setSelected((Boolean) value);
        }
        checkBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        checkBox.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        return checkBox;
    }
}