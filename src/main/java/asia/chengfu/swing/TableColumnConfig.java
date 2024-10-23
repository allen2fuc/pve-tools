package asia.chengfu.swing;

public class TableColumnConfig {

    // 列名
    public static final String[] COLUMN_NAMES = {
//        "选择", "VM ID", "名称", "状态", "内存", "CPU", "磁盘", "标签","进度","操作"
        "选择", "VM ID", "名称", "状态", "内存", "CPU", "磁盘", "标签","操作"
    };

    // 列索引
    public static final int SELECTION_COLUMN_INDEX = 0;
    public static final int VM_ID_COLUMN_INDEX = 1;
    public static final int NAME_COLUMN_INDEX = 2;
    public static final int STATUS_COLUMN_INDEX = 3;
    public static final int MEMORY_COLUMN_INDEX = 4;
    public static final int CPU_COLUMN_INDEX = 5;
    public static final int DISK_COLUMN_INDEX = 6;
    public static final int TAGS_COLUMN_INDEX = 7;
    public static final int OPERATION_COLUMN_INDEX = 8;

    // 防止实例化该类
    private TableColumnConfig() {
        // 私有构造方法防止实例化
    }
}
