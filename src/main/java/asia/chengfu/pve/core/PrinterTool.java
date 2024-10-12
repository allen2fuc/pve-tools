package asia.chengfu.pve.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import org.dnsge.util.tableprinter.printer.TablePrinter;
import org.dnsge.util.tableprinter.row.TableRow;
import org.dnsge.util.tableprinter.table.DataSource;
import org.dnsge.util.tableprinter.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrinterTool {

    public static <T> void tablePrint(List<T> data){
        List<TableRow> tableRows = wrapTable(data);
        DataSource dataSource = DataSource.fromRows(tableRows);
        Table table = new Table(dataSource);
        TablePrinter.printTable(table);
    }

    private static <T> List<TableRow> wrapTable(List<T> list) {

        int size = list.size();

        var headerNames = new ArrayList<String>();
        var data = new ArrayList<TableRow>(size);

        for (int i = 0; i < list.size(); i++) {

            T b = list.get(i);

            Map<String, Object> map = BeanUtil.beanToMap(b);

            var values = new ArrayList<String>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {

                if (i == 0) {
                    headerNames.add(entry.getKey());
                }

                values.add(ObjUtil.toString(entry.getValue()));
            }

            data.add(new TableRow(headerNames, values));
        }

        return data;

    }

}
