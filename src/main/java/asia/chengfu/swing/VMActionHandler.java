package asia.chengfu.swing;

import asia.chengfu.swing.api.PveTaskUtil;
import asia.chengfu.swing.api.VMAction;
import asia.chengfu.swing.api.VMOperations;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
public class VMActionHandler implements ActionListener {

    private final JTable table;
    private final JComboBox<String> nodeComboBox;
    private final JPanel panel;
    private final VMOperations vmOperations;

    public VMActionHandler(JTable table, JComboBox<String> nodeComboBox, JPanel panel, VMOperations vmOperations) {
        this.table = table;
        this.nodeComboBox = nodeComboBox;
        this.panel = panel;
        this.vmOperations = vmOperations;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        int row = table.getSelectedRow();
        int vmId = (int) table.getValueAt(row, 1); // 假设VM ID在第一列
        String nodeName = (String) nodeComboBox.getSelectedItem();

        try {
            OperationTaskProgressListener progressListener = new OperationTaskProgressListener(table.getModel(), row);
            String upid = executeOperation(source.getText(), vmId, nodeName, progressListener);

            if (StrUtil.isNotBlank(upid)) {
                PveTaskUtil.trackTaskProgress(vmOperations, nodeName, upid, getOperator(source.getText()), progressListener);
            }
        } catch (Exception ex) {
            // 显示错误弹框
            JOptionPane.showMessageDialog(table, "操作失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            fireEditingStopped();
        }
    }

    private String executeOperation(String action, int vmId, String nodeName, OperationTaskProgressListener progressListener) throws Exception {
        switch (action) {
            case "启动":
                return vmOperations.startVM(vmId, nodeName);
            case "停止":
                return vmOperations.stopVM(vmId, nodeName);
            case "重启":
                return vmOperations.restartVM(vmId, nodeName);
            case "删除":
                if (showConfirmationDialog() == JOptionPane.YES_OPTION) {
                    return vmOperations.deleteVM(vmId, nodeName);
                }
                break;
            default:
                throw new IllegalArgumentException("未知的操作: " + action);
        }
        return null;
    }

    private int showConfirmationDialog() {
        return JOptionPane.showConfirmDialog(panel, "你确定要删除所选的虚拟机吗？此操作无法撤销！", "确认删除", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    private VMAction getOperator(String action) {
        return switch (action) {
            case "启动" -> VMAction.START;
            case "停止" -> VMAction.STOP;
            case "重启" -> VMAction.REBOOT;
            case "删除" -> VMAction.DELETE;
            default -> throw new IllegalArgumentException("未知的操作: " + action);
        };
    }

    private void fireEditingStopped() {
        // 实现编辑停止的逻辑
    }
}