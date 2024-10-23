package asia.chengfu.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;

public class OperationTaskProgressListener implements TaskProgressListener {
    private static final Logger logger = LoggerFactory.getLogger(OperationTaskProgressListener.class);

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
        logger.error("任务出错", e);
    }
}