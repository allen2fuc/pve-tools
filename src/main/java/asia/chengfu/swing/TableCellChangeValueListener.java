package asia.chengfu.swing;

import asia.chengfu.swing.api.VMOperations;
import cn.hutool.core.io.unit.DataSize;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

@Slf4j
public class TableCellChangeValueListener implements TableModelListener {

    private final VMOperations vmOperations;
    private final JComboBox<String> nodeComboBox;

    public TableCellChangeValueListener(VMOperations vmOperations, JComboBox<String> nodeComboBox) {
        this.vmOperations = vmOperations;
        this.nodeComboBox = nodeComboBox;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();

        // 只处理第 4 列（标签列）的修改
        if (column == TableColumnConfig.TAGS_COLUMN_INDEX
            || column == TableColumnConfig.MEMORY_COLUMN_INDEX
        ) {
            TableModel model = (TableModel) e.getSource();
            int vmId = (int) model.getValueAt(row, 1);  // 假设第2列存储VMID
            Object newValue = model.getValueAt(row, column);
            String nodeName = (String) nodeComboBox.getSelectedItem();

            JSONObject param = JSONUtil.createObj();

            try {
                if (column == TableColumnConfig.TAGS_COLUMN_INDEX) {
                    param.putOnce("tags", newValue);
                    // 调用 /api2/json/nodes/{node}/qemu/{vmid}/config 接口更新虚拟机标签
                    vmOperations.updateVMConfiguration(vmId, nodeName, param);
                }

                if(column == TableColumnConfig.MEMORY_COLUMN_INDEX){
                    DataSize dataSize = DataSize.parse(newValue.toString());
                    param.putOnce("memory", dataSize.toMegabytes());
                    vmOperations.updateVMConfiguration(vmId, nodeName, param);
                    // 提示用户重启才会生效
                    JOptionPane.showMessageDialog(null, "更新内存成功，请重启虚拟机后生效", "提示", JOptionPane.INFORMATION_MESSAGE);
                }


                log.info("更新vmId:{}，新值:{}成功", vmId, newValue);
            } catch (Exception ex) {
                log.error("更新vmId:{}，新值:{}失败", vmId, newValue, ex);
                JOptionPane.showMessageDialog(null, "更新值失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
