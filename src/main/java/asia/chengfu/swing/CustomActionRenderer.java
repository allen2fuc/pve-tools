package asia.chengfu.swing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CustomActionRenderer extends CustomActionPanel implements TableCellRenderer {

    public CustomActionRenderer() {
        super();
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        String vmName = (String) table.getValueAt(row, 1); // 假设名称在第二列
//        boolean isTemplate = vmName.endsWith("-template");

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        return this;
    }
}