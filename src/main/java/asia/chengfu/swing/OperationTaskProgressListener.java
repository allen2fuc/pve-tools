package asia.chengfu.swing;

import asia.chengfu.swing.api.TaskProgressListener;
import asia.chengfu.swing.api.VMAction;
import lombok.extern.slf4j.Slf4j;

import javax.swing.table.TableModel;

@Slf4j
public class OperationTaskProgressListener implements TaskProgressListener {

    private final TableModel tableModel;
    private final int row;

    public OperationTaskProgressListener(TableModel tableModel, int row) {
        this.tableModel = tableModel;
        this.row = row;
    }

    @Override
    public void onProgress(VMAction vmAction) {
        tableModel.setValueAt(getOperationBegins(vmAction), row, TableColumnConfig.STATUS_COLUMN_INDEX);
    }

    @Override
    public void onComplete(VMAction vmAction) {
        tableModel.setValueAt(getOperationEnds(vmAction), row, TableColumnConfig.STATUS_COLUMN_INDEX);
    }

    @Override
    public void onError(Exception e) {
        log.error("任务出错", e);
    }

    private static String getOperationBegins(VMAction vmAction) {
        return switch (vmAction) {
            case START -> "starting";
            case STOP -> "stopping";
            case DELETE -> "deleting";
            case REBOOT -> "restarting";
        };
    }

    private static String getOperationEnds(VMAction vmAction) {
        return switch (vmAction) {
            case DELETE -> "deleted";
            case STOP -> "stopped";
            default -> "running";
        };
    }
}