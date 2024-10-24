package asia.chengfu.swing;

import asia.chengfu.swing.api.CatchUtils;
import asia.chengfu.swing.api.VMOperations;
import asia.chengfu.swing.api.VMAction;
import asia.chengfu.swing.bean.VMTemplate;
import asia.chengfu.swing.bean.VirtualMachine;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class DisplayFrame extends JFrame {
    private final JComboBox<String> nodeComboBox;
    private final JComboBox<VMTemplate> templateComboBox;
    private final JTable resultTable;
    private final DefaultTableModel tableModel;
    private final VMOperations vmOperations;
    private boolean hasQueried = false;
    private final List<VirtualMachine> queriedVms = new ArrayList<>();
    private final JLabel resultLabel;

    private BatchOperationHandler batchOperationHandler;

    public DisplayFrame(VMOperations vmOperations) {
        this.vmOperations = vmOperations;

        setTitle("Proxmox VE 虚拟机查询");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        var panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        var nodeLabel = new JLabel("节点:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(nodeLabel, gbc);

        nodeComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nodeComboBox, gbc);

        var templateLabel = new JLabel("虚拟机模板:");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(templateLabel, gbc);

        templateComboBox = new JComboBox<>();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(templateComboBox, gbc);

        resultLabel = new JLabel("");
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(resultLabel, gbc);

        var queryAddPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 使用FlowLayout居中

        var queryButton = new JButton("查询");
        queryButton.addActionListener(new QueryClickActionListener());
        queryAddPanel.add(queryButton);

        var addButton = new JButton("新增");
        panel.add(addButton, gbc);
        queryAddPanel.add(addButton);

        var exitButton = new JButton("退出");
        // 退出到登陆界面
        exitButton.addActionListener(new ExitActionListener(this));
        panel.add(exitButton, gbc);
        queryAddPanel.add(exitButton);

        // 新增全选按钮
        var selectAllButton = new JButton("全选/取消全选");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(selectAllButton, gbc);

        // 创建一个新的 JPanel 来包含启动、停止、删除、重启按钮，并为其设置边框
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBorder(BorderFactory.createTitledBorder("操作"));

        // 新增启动按钮
        JButton startButton = new JButton("启动");
        actionPanel.add(startButton);

        JButton stopButton = new JButton("停止");
        actionPanel.add(stopButton);

        JButton deleteButton = new JButton("删除");
        actionPanel.add(deleteButton);

        JButton restartButton = new JButton("重启");
        actionPanel.add(restartButton);

        // 将queryAddPanel添加到主面板中
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;  // 让面板跨越多列
        gbc.anchor = GridBagConstraints.CENTER;  // 居中对齐
        panel.add(queryAddPanel, gbc);

        // 将操作按钮面板放入主面板中
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 4; // 让它跨越多列
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(actionPanel, gbc);

        tableModel = new DisplayTableModel();
        tableModel.addTableModelListener(new TableCellChangeValueListener(vmOperations, nodeComboBox));

        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(30);

        // 为所有类型设置自定义渲染器，确保每一列都渲染颜色
        DefaultTableCellRenderer renderer = new AlternateRowColorRenderer();
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }


        TableColumnModel columnModel = resultTable.getColumnModel();
        columnModel.getColumn(TableColumnConfig.SELECTION_COLUMN_INDEX).setMaxWidth(100); // 设置选择列的宽度
        columnModel.getColumn(TableColumnConfig.SELECTION_COLUMN_INDEX).setCellRenderer(new CheckBoxRenderer());
        columnModel.getColumn(TableColumnConfig.SELECTION_COLUMN_INDEX).setCellEditor(new CheckBoxEditor());

        columnModel.getColumn(TableColumnConfig.OPERATION_COLUMN_INDEX).setPreferredWidth(300); // 设置操作列的宽度
        columnModel.getColumn(TableColumnConfig.OPERATION_COLUMN_INDEX).setCellRenderer(new OperationActionRenderer());
        columnModel.getColumn(TableColumnConfig.OPERATION_COLUMN_INDEX).setCellEditor(new OperationActionEditor(vmOperations, nodeComboBox, resultTable));

        JScrollPane scrollPane = new JScrollPane(resultTable);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 8;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        addButton.addActionListener(_ -> {
            // 判断节点名称是否为空
            // 判断模版是否为空
            if (nodeComboBox.getSelectedItem() == null || templateComboBox.getSelectedItem() == null) {
                log.warn("新增虚拟机失败，节点或模板未选择");
                JOptionPane.showMessageDialog(DisplayFrame.this, "请选择节点和模板", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            log.info("新增虚拟机：节点={}, 模板={}", nodeComboBox.getSelectedItem(), templateComboBox.getSelectedItem());
            showAddPanel();
        });

        startButton.addActionListener(e -> performBatchOperation(VMAction.START));
        stopButton.addActionListener(e -> performBatchOperation(VMAction.STOP));
        deleteButton.addActionListener(e -> performBatchOperation(VMAction.DELETE));
        restartButton.addActionListener(e -> performBatchOperation(VMAction.REBOOT));

        // 为全选按钮添加事件监听器
        selectAllButton.addActionListener(_ -> toggleSelection());

        // 键盘事件绑定：按下回车键时执行查询
        getRootPane().setDefaultButton(queryButton);

        // 初始化节点下拉框
        initializeNodeComboBox();

        // 初始化模板下拉框
        initializeTemplateComboBox();

        initializeHandler();

        add(panel);
        setVisible(true);
    }

    private void initializeHandler() {
        this.batchOperationHandler = new BatchOperationHandler(vmOperations, resultTable.getModel(), nodeComboBox, templateComboBox, resultTable);
    }

    private void initializeNodeComboBox() {
        List<String> nodes = vmOperations.fetchNodes();
        nodes.sort(Comparator.comparing(String::toString));
        for (String node : nodes) {
            nodeComboBox.addItem(node);
        }
        if (!nodes.isEmpty()) {
            nodeComboBox.setSelectedIndex(0); // 默认选择第一个节点
        }
    }

    private void initializeTemplateComboBox() {
        CatchUtils.catchRun(() -> {
            String nodeName = (String) nodeComboBox.getSelectedItem();
            if (nodeName != null && !nodeName.isEmpty()) {
                JSONArray data = vmOperations.fetchVirtualMachinesFromAPI(nodeName);
                List<VMTemplate> vmTemplates = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    JSONObject vm = data.getJSONObject(i);
                    if (vm.getInt("template", 0) == 1) {
                        VMTemplate template = new VMTemplate(vm.getInt("vmid"), vm.getStr("name"));
                        vmTemplates.add(template);
                    }
                }

                // 验证模版是否存在，不存在则提示用户先去PVE界面创建模版
                if (vmTemplates.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "模版虚拟机不存在，请先在PVE界面创建模版", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                vmTemplates.sort(Comparator.comparing(VMTemplate::getName));

                vmTemplates.forEach(templateComboBox::addItem);
            }
        }, ex -> {
            JOptionPane.showMessageDialog(this, "获取模板虚拟机失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        });
    }

    private void performBatchOperation(VMAction vmAction) {
        batchOperationHandler.performBatchOperation(vmAction);
    }

    private void fetchAndDisplayVirtualMachines(String nodeName, VMTemplate vmTemplate) {
        CatchUtils.catchRun(() -> {
            JSONArray data = vmOperations.fetchVirtualMachinesFromAPI(nodeName);
            String prefix = getTemplateBasename(vmTemplate);
            filterAndSortVirtualMachines(data, prefix);
            updateTableWithVirtualMachines();
            hasQueried = true;

            if (!queriedVms.isEmpty()) {
                resultLabel.setForeground(Color.GREEN);  // 设置为绿色字体
                resultLabel.setText(queriedVms.size() + " 个虚拟机被找到");
            } else {
                resultLabel.setForeground(Color.RED);    // 设置为红色字体
                resultLabel.setText("没有找到虚拟机");
            }
        }, ex -> {
            JOptionPane.showMessageDialog(this, "获取虚拟机失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            resultLabel.setText("");
        });
    }

    private String extractIPv4Address(JSONArray interfaces) {
        for (int i = 0; i < interfaces.size(); i++) {
            JSONObject iface = interfaces.getJSONObject(i);
            JSONArray ipAddresses = iface.getJSONArray("ip-addresses");
            if (ipAddresses != null) {
                for (int j = 0; j < ipAddresses.size(); j++) {
                    JSONObject ip = ipAddresses.getJSONObject(j);
                    if ("ipv4".equals(ip.getStr("ip-address-type"))) {
                        return ip.getStr("ip-address");
                    }
                }
            }
        }
        return "N/A"; // 如果没有找到IP地址，返回N/A
    }

    private void filterAndSortVirtualMachines(JSONArray data, String vmName) {
        queriedVms.clear();
        for (int i = 0; i < data.size(); i++) {
            JSONObject vm = data.getJSONObject(i);
            String vmNameFromData = vm.getStr("name");
            int template = vm.getInt("template", 0);
            if ((vmName.isEmpty() || StrUtil.containsIgnoreCase(vmNameFromData, vmName)) && template == 0) {
                queriedVms.add(new VirtualMachine(
                        vm.getInt("vmid"),
                        vmNameFromData,
                        vm.getStr("status"),
                        DataSizeUtil.format(vm.getLong("maxmem")),
                        StrUtil.toString(vm.getInt("cpus")),
                        DataSizeUtil.format(vm.getLong("maxdisk")),
                        vm.getStr("tags", "")));
            }
        }
        queriedVms.sort(Comparator.comparing(VirtualMachine::name));
    }

    private void updateTableWithVirtualMachines() {
        tableModel.setRowCount(0);

        for (VirtualMachine vm : queriedVms) {
            Object[] row = {false,
                    vm.vmId(),
                    vm.name(),
                    vm.status(),
                    vm.memory(),
                    vm.cpu(),
                    vm.disk(),
                    vm.tags(),
                    ""}; // 操作列留空
            tableModel.addRow(row);
        }
    }

    private void showAddPanel() {
        JDialog addDialog = new JDialog(this, "新增虚拟机", true);
        addDialog.setSize(300, 200);
        addDialog.setLayout(new GridLayout(4, 2));
        addDialog.setLocationRelativeTo(null);

        JLabel countLabel = new JLabel("克隆数量:");
        JTextField countField = new JTextField();
        JButton submitButton = new JButton("提交");
        JButton cancelButton = new JButton("取消");

        addDialog.add(countLabel);
        addDialog.add(countField);
        addDialog.add(submitButton);
        addDialog.add(cancelButton);

        VMTemplate vmTemplate = (VMTemplate) templateComboBox.getSelectedItem();
        assert vmTemplate != null;
        String baseName = getTemplateBasename(vmTemplate);
        // 这里获取空缺的序号和最大序号，返回集合列表
        List<Integer> missingSeqs = getMissingSequenceNumbers(baseName);
        int maxSeq = getMaxSequenceNumber(baseName);

        submitButton.addActionListener(e -> {
            String countFieldText = countField.getText();
            // 验证只能输入数字，并且最小为1，最大为50，否则提示错误和范围
            if (!StrUtil.isNumeric(countFieldText) || Integer.parseInt(countFieldText) < 1 || Integer.parseInt(countFieldText) > 50) {
                JOptionPane.showMessageDialog(addDialog, "请输入正确的克隆数量，范围为1-50", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int count = Integer.parseInt(countFieldText);
            String nodeName = (String) nodeComboBox.getSelectedItem();
            int index = 0;
            for (int i = 0; i < count; i++) {
                try {
                    int newVmid = vmOperations.getNextAvailableVmid(nodeName);
                    Integer missSeq = CollUtil.get(missingSeqs, i);
                    if (missSeq != null) {
                        cloneAndStartVm(missSeq, newVmid, nodeName, vmTemplate, baseName);
                    } else {
                        int curSeq = maxSeq + index + 1;
                        cloneAndStartVm(curSeq, newVmid, nodeName, vmTemplate, baseName);
                        index++;
                    }
                    log.info("虚拟机克隆成功：VMID={}", newVmid);
                } catch (Exception ex) {
                    log.error("虚拟机克隆失败：错误信息={}", ex.getMessage());
                    JOptionPane.showMessageDialog(addDialog, "克隆失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            addDialog.dispose();
        });

        // 取消操作
        cancelButton.addActionListener(e -> addDialog.dispose());

        // 绑定按键事件：回车提交，Esc取消
        addDialog.getRootPane().registerKeyboardAction(e -> submitButton.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        addDialog.getRootPane().registerKeyboardAction(e -> addDialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);


        addDialog.setVisible(true);
    }

    private String getTemplateBasename(VMTemplate vmTemplate) {
        return ArrayUtil.firstNonNull(vmTemplate.getName().split("[_-]"));
    }

    private void cloneAndStartVm(int curSeq, int newVmid, String nodeName, VMTemplate vmTemplate, String baseName) {
        String seqSuffix = StrUtil.fill(StrUtil.toString(curSeq), '0', 2, true);
        log.info("正在克隆虚拟机：模板={}, 新VMID={}, 序列号={}, 节点={}", vmTemplate.getName(), newVmid, curSeq, nodeName);
        vmOperations.cloneVM(vmTemplate.getVmid(), newVmid, baseName + "-" + seqSuffix, nodeName);
        vmOperations.startVM(newVmid, nodeName);
    }

    private List<Integer> getMissingSequenceNumbers(String baseName) {
        // 如果没有查询过，则调用接口获取queriedVms值
        if (!hasQueried) {
            JSONArray data = vmOperations.fetchVirtualMachinesFromAPI(StrUtil.toString(nodeComboBox.getSelectedItem()));
            VMTemplate vmTemplate = (VMTemplate) templateComboBox.getSelectedItem();
            assert vmTemplate != null;
            String prefix = getTemplateBasename(vmTemplate);
            filterAndSortVirtualMachines(data, prefix);
            hasQueried = true;
        }
        List<Integer> missingSeqs = new ArrayList<>();
        // 默认从1开始
        int startSeq = 0;
        for (VirtualMachine vm : queriedVms) {
            startSeq++;

            String name = vm.name();
            if (name.startsWith(baseName + "-")) {
                try {
                    int seq = Integer.parseInt(name.substring(baseName.length() + 1));
                    if (seq != startSeq) {
                        missingSeqs.add(startSeq);
                        startSeq++;
                    }
                } catch (NumberFormatException ex) {
                    // Ignore invalid sequence number
                }
            }
        }
        return missingSeqs;
    }

    private void toggleSelection() {
        boolean selectAll = !isAnySelected();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            tableModel.setValueAt(selectAll, row, 0);
        }
    }

    private boolean isAnySelected() {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if ((Boolean) tableModel.getValueAt(row, 0)) {
                return true;
            }
        }
        return false;
    }

    private int getMaxSequenceNumber(String baseName) {
        int maxSeq = 0;
        for (VirtualMachine vm : queriedVms) {
            String name = vm.name();
            if (name.startsWith(baseName + "-")) {
                try {
                    int seq = Integer.parseInt(name.substring(baseName.length() + 1));
                    if (seq > maxSeq) {
                        maxSeq = seq;
                    }
                } catch (NumberFormatException ex) {
                    // Ignore invalid sequence number
                }
            }
        }
        return maxSeq;
    }


    private class QueryClickActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 验证节点和模板选择
            if (nodeComboBox.getSelectedItem() == null || templateComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "请选择节点和模板！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nodeName = (String) nodeComboBox.getSelectedItem();
            VMTemplate vmTemplate = (VMTemplate) templateComboBox.getSelectedItem();
            log.info("开始查询虚拟机, 节点: {}, 模板: {}", nodeName, vmTemplate != null ? vmTemplate.getName() : "未选择模板");

            tableModel.setRowCount(0);
            fetchAndDisplayVirtualMachines(nodeName, vmTemplate);
            resultTable.getColumnModel().getColumn(3).setCellEditor(new OperationActionEditor(vmOperations, nodeComboBox, resultTable));
            log.info("虚拟机查询完成数量：{}", queriedVms.size());
        }
    }

    private class DisplayTableModel extends DefaultTableModel {
        public DisplayTableModel() {
            super(TableColumnConfig.COLUMN_NAMES, 0);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == TableColumnConfig.TAGS_COLUMN_INDEX
                    || column == TableColumnConfig.SELECTION_COLUMN_INDEX
                    || column == TableColumnConfig.MEMORY_COLUMN_INDEX
                    || column == TableColumnConfig.OPERATION_COLUMN_INDEX;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == TableColumnConfig.SELECTION_COLUMN_INDEX ? Boolean.class : String.class; // 第一列为布尔值(选择框)
        }
    }
}