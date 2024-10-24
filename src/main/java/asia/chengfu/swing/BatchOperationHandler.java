package asia.chengfu.swing;

import asia.chengfu.swing.api.CatchUtils;
import asia.chengfu.swing.api.PveTaskUtil;
import asia.chengfu.swing.api.VMOperations;
import asia.chengfu.swing.api.VMAction;
import asia.chengfu.swing.bean.VMTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.TableModel;

@Slf4j
public class BatchOperationHandler {

    private final VMOperations vmOperations;
    private final TableModel tableModel;
    private final JComboBox<String> nodeComboBox;
    private final JComboBox<VMTemplate> templateComboBox;
    private final JTable table;

    public BatchOperationHandler(VMOperations vmOperations, TableModel tableModel, JComboBox<String> nodeComboBox, JComboBox<VMTemplate> templateComboBox, JTable table) {
        this.vmOperations = vmOperations;
        this.tableModel = tableModel;
        this.nodeComboBox = nodeComboBox;
        this.templateComboBox = templateComboBox;
        this.table = table;
    }

    public void performBatchOperation(VMAction vmAction) {
        String nodeName = (String) nodeComboBox.getSelectedItem();

        CatchUtils.catchRun(() -> {
            boolean flag = false;

            // 声明提示用户的变量
            boolean isDeletedShow = false;

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Boolean selected = (Boolean) tableModel.getValueAt(row, 0);
                if (selected) {

                    if (!isDeletedShow && VMAction.DELETE == vmAction) {
                        int confirm = showConfirmationDialog();
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }
                        // 已经提示过了
                        isDeletedShow = true;
                    }

                    int vmId = (int) tableModel.getValueAt(row, TableColumnConfig.VM_ID_COLUMN_INDEX);
                    OperationTaskProgressListener progressListener = new OperationTaskProgressListener(tableModel, row);

                    executeOperation(vmAction, vmId, nodeName, progressListener);
                    flag = true;
                }
            }

            if (!flag) {
                logAndShowMessage("未选择任何虚拟机进行{}操作", vmAction.name(), "请选择至少一个虚拟机", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            logAndShowMessage("批量操作 {} 成功", vmAction.name(), "操作成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            fetchAndDisplayVirtualMachines(nodeName, (VMTemplate) templateComboBox.getSelectedItem());
        }, ex -> {
            logAndShowMessage("批量操作失败：操作 {} 出错，原因: {}", vmAction.name(), "操作失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        });
    }

    private int showConfirmationDialog() {
        return JOptionPane.showConfirmDialog(table, "你确定要删除所选的虚拟机吗？此操作无法撤销！", "确认删除", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    private void logAndShowMessage(String logMessage, String operation, String userMessage, String title, int messageType) {
        log.info(logMessage, operation);
        JOptionPane.showMessageDialog(table, userMessage, title, messageType);
    }

    private void executeOperation(VMAction action, int vmId, String nodeName, OperationTaskProgressListener progressListener) {
        String upid = switch (action) {
            case START -> vmOperations.startVM(vmId, nodeName);
            case STOP -> vmOperations.stopVM(vmId, nodeName);
            case DELETE -> vmOperations.deleteVM(vmId, nodeName);
            case REBOOT -> vmOperations.restartVM(vmId, nodeName);
        };
        PveTaskUtil.trackTaskProgress(vmOperations, nodeName, upid, action, progressListener);
    }

    private void fetchAndDisplayVirtualMachines(String nodeName, VMTemplate template) {
        // 实现获取并显示虚拟机列表的逻辑
    }
}