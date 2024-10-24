package asia.chengfu.swing;

import asia.chengfu.swing.api.VMOperations;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperationActionEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private final OperationActionPanel panel;
    private final VMActionHandler vmActionHandler;

    public OperationActionEditor(VMOperations vmOperations, JComboBox<String> nodeComboBox, JTable table) {
        panel = new OperationActionPanel();
        panel.getStartButton().addActionListener(this);
        panel.getStopButton().addActionListener(this);
        panel.getRestartButton().addActionListener(this);
        panel.getDeleteButton().addActionListener(this);
        vmActionHandler = new VMActionHandler(table, nodeComboBox, panel, vmOperations);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null; // 不需要返回值
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        vmActionHandler.actionPerformed(e);
    }
}