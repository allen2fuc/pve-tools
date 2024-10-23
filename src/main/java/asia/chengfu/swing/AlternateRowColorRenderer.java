package asia.chengfu.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

// 自定义单元格渲染器，用于交替行颜色
public class AlternateRowColorRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 奇数行颜色
        Color oddColor = new Color(240, 240, 240); // 淡灰色
        // 偶数行颜色
        Color evenColor = Color.WHITE; // 白色

        if (!isSelected) {
            // 设置奇偶行不同颜色
            if (row % 2 == 0) {
                cell.setBackground(evenColor);
            } else {
                cell.setBackground(oddColor);
            }
        } else {
            // 如果被选中，设置选中背景色
            cell.setBackground(table.getSelectionBackground());
        }

        return cell;
    }
}
