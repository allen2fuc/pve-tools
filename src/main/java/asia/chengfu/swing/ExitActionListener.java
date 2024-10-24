package asia.chengfu.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitActionListener implements ActionListener {
    private final Window window;

    public ExitActionListener(Window window) {
        this.window = window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 关闭当前界面，打开登陆界面
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        window.dispose();
    }
}
