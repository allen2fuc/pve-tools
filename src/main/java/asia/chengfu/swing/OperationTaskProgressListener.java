package asia.chengfu.swing;

import asia.chengfu.swing.api.TaskProgressListener;
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
    public void onProgress(String progress) {
        tableModel.setValueAt(progress, row, TableColumnConfig.STATUS_COLUMN_INDEX);
    }

    @Override
    public void onComplete(String status) {
        tableModel.setValueAt(status, row, TableColumnConfig.STATUS_COLUMN_INDEX);
    }

    @Override
    public void onError(Exception e) {
        log.error("任务出错", e);
    }
}