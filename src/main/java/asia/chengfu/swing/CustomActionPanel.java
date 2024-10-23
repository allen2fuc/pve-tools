package asia.chengfu.swing;

import javax.swing.*;
import java.awt.*;

public class CustomActionPanel extends JPanel {

    private JButton startButton;
    private JButton stopButton;
    private JButton restartButton;
    private JButton deleteButton;

    public CustomActionPanel() {
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

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JButton getRestartButton() {
        return restartButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}