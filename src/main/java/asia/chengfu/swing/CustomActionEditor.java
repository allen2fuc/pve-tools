package asia.chengfu.swing;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomActionEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomActionEditor.class);

    private CustomActionPanel panel;
    private int row;
    private JComboBox<String> nodeComboBox;
    private VMOperations vmOperations;
    private JTable table;

    public CustomActionEditor(VMOperations vmOperations, JComboBox<String> nodeComboBox, JTable table) {
        this.vmOperations = vmOperations;
        this.nodeComboBox = nodeComboBox;
        this.table = table;
        panel = new CustomActionPanel();
        panel.getStartButton().addActionListener(this);
        panel.getStopButton().addActionListener(this);
        panel.getRestartButton().addActionListener(this);
        panel.getDeleteButton().addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null; // 不需要返回值
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        int vmId = (int) table.getValueAt(row, 1); // 假设VM ID在第一列
        String action = source.getText();
        String nodeName = (String) nodeComboBox.getSelectedItem();

        try {
            OperationTaskProgressListener progressListener = new OperationTaskProgressListener(table.getModel(), row);
            int operator = VMOperations.RUNNING;
            String upid = null;
            if (action.equals("启动")) {
                operator = VMOperations.START;
                upid = vmOperations.startVM(vmId, nodeName);
            }else if (action.equals("停止")){
                operator = VMOperations.STOP;
                upid = vmOperations.stopVM(vmId, nodeName);
            }else if (action.equals("重启")){
                operator = VMOperations.RESTART;
                upid = vmOperations.restartVM(vmId, nodeName);
            }else if (action.equals("删除")){
                operator = VMOperations.DELETE;
                // 弹出确认提示框
                int confirm = JOptionPane.showConfirmDialog(
                        panel,
                        "你确定要删除所选的虚拟机吗？此操作无法撤销！",
                        "确认删除",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // 如果用户确认删除
                if (confirm == JOptionPane.YES_OPTION) {
                    upid = vmOperations.deleteVM(vmId, nodeName);
                }
            }

            if (StrUtil.isNotBlank(upid)){
                PveTaskUtil.trackTaskProgress(vmOperations, nodeName, upid, operator, progressListener);
            }
        } catch (Exception ex) {
            // 显示错误弹框
            JOptionPane.showMessageDialog(table, "操作失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            fireEditingStopped();
        }
    }
}