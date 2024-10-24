package asia.chengfu.swing;

import javax.swing.*;
import java.awt.*;

public class CheckBoxEditor extends DefaultCellEditor {
    private final JCheckBox checkBox;

    public CheckBoxEditor() {
        super(new JCheckBox());
        checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(_ -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Boolean) {
            checkBox.setSelected((Boolean) value);
        }
        checkBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        checkBox.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        return checkBox;
    }
}