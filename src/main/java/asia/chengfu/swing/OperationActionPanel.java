package asia.chengfu.swing;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class OperationActionPanel extends JPanel {

    private final JButton startButton;
    private final JButton stopButton;
    private final JButton restartButton;
    private final JButton deleteButton;

    public OperationActionPanel() {
        setLayout(new GridLayout(1, 4, 5, 5)); // 1行4列，间距5

        startButton = new JButton("启动");
        stopButton = new JButton("停止");
        restartButton = new JButton("重启");
        deleteButton = new JButton("删除");

        add(startButton);
        add(stopButton);
        add(restartButton);
        add(deleteButton);
    }
}