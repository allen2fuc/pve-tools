package asia.chengfu.swing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class OperationActionRenderer extends OperationActionPanel implements TableCellRenderer {

    public OperationActionRenderer() {
        super();
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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