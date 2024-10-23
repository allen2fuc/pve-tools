package asia.chengfu.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckBoxEditor extends DefaultCellEditor {
    private JCheckBox checkBox;

    public CheckBoxEditor() {
        super(new JCheckBox());
        checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
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