package asia.chengfu.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private JTextField addressField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Proxmox VE 登录");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel addressLabel = new JLabel("地址:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(addressLabel, gbc);

        addressField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(addressField, gbc);

        JLabel usernameLabel = new JLabel("用户名:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("密码:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        loginButton = new JButton("登录");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String address = addressField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    VMOperations vmOperations = new VMOperations(address, username, password);
                    new DisplayFrame(vmOperations);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "登录失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 初始化默认值
        initializeDefaultValue();

//        // 加载图标
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        Image icon = toolkit.getImage(LoginFrame.class.getResource("/logo.png")); // 图标文件路径，支持 png、jpg 等格式
//
//        // 设置图标
//        setIconImage(icon);

        // 初始化回车键事件
        loginButton.registerKeyboardAction(e -> loginButton.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(panel);
        setVisible(true);
    }

    private void initializeDefaultValue() {
        addressField.setText("https://192.168.1.1:8006");
        usernameField.setText("root@pam");
        passwordField.setText("123456");
    }
}