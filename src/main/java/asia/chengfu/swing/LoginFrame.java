package asia.chengfu.swing;

import asia.chengfu.swing.api.VMOperations;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Slf4j
public class LoginFrame extends JFrame {

    private final JTextField addressField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    private AES aes;
    private final File tempFile;

    private static final String LOGIN_CREDENTIALS_FILE = "login_credentials.txt";
    private static final String AES_KEY_FILE = "aes_key.bin";

    public LoginFrame() {

        // 加载或生成密钥
        loadOrGenerateKey();

        // 创建临时文件
        tempFile = FileUtil.file(FileUtil.getTmpDirPath(), LOGIN_CREDENTIALS_FILE);

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

        loginButton.addActionListener(new LoginActionListener());

        // 初始化默认值
        initializeDefaultValue();

        // 加载上次保存的凭证
        loadSavedCredentials();

        // 初始化回车键事件
        loginButton.registerKeyboardAction(e -> loginButton.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(panel);
        setVisible(true);
    }

    private void loadOrGenerateKey() {
        File keyFile = FileUtil.file(FileUtil.getTmpDirPath(), AES_KEY_FILE);
        if (keyFile.exists()) {
            byte[] keyBytes = FileUtil.readBytes(keyFile);
            this.aes = SecureUtil.aes(keyBytes);
        } else {
            Key key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue());
            FileUtil.writeBytes(key.getEncoded(), keyFile);
            this.aes = SecureUtil.aes(key.getEncoded());
        }
    }

    private void loadSavedCredentials() {
        if (!tempFile.exists()) {
            return;
        }

        try (BufferedInputStream fis = FileUtil.getInputStream(tempFile)) {
            String content = IoUtil.read(fis, CharsetUtil.CHARSET_UTF_8);
            String[] lines = content.split("\n");
            if (lines.length == 3) {
                addressField.setText(new String(aes.decrypt(Base64.decode(lines[0])), StandardCharsets.UTF_8));
                usernameField.setText(new String(aes.decrypt(Base64.decode(lines[1])), StandardCharsets.UTF_8));
                passwordField.setText(new String(aes.decrypt(Base64.decode(lines[2])), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            log.error("加载凭证失败", e);
        }
    }

    // 保存凭证
    private void saveCredentials(String address, String username, String password) throws IOException {
        try {
            String encryptedAddress = Base64.encode(aes.encrypt(address));
            String encryptedUsername = Base64.encode(aes.encrypt(username));
            String encryptedPassword = Base64.encode(aes.encrypt(password));

            @Cleanup BufferedOutputStream fos = FileUtil.getOutputStream(tempFile);
            IoUtil.write(fos, false, encryptedAddress.getBytes(StandardCharsets.UTF_8));
            IoUtil.write(fos, false, "\n".getBytes(StandardCharsets.UTF_8));
            IoUtil.write(fos, false, encryptedUsername.getBytes(StandardCharsets.UTF_8));
            IoUtil.write(fos, false, "\n".getBytes(StandardCharsets.UTF_8));
            IoUtil.write(fos, true, encryptedPassword.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("保存凭证失败", e);
        }
    }

    private void initializeDefaultValue() {
        addressField.setText("https://192.168.1.1:8006");
        usernameField.setText("root@pam");
        passwordField.setText("123456");
    }


    private class LoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 在这里执行登录操作
            String address = addressField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                VMOperations vmOperations = new VMOperations(address, username, password);
                new DisplayFrame(vmOperations);
                saveCredentials(address, username, password);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(LoginFrame.this, "登录失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}