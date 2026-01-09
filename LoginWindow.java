import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame {
    private BankManager bankManager;
    private JTextField accountField;
    private JPasswordField passwordField;
    
    public LoginWindow() {
        bankManager = new BankManager();
        
        setTitle("Customer Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(52, 152, 219);
                Color color2 = new Color(155, 89, 182);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("👤 Customer Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setForeground(Color.WHITE);
        accountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(accountLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        accountField = new JTextField(20);
        accountField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(accountField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton loginButton = createStyledButton("Login", new Color(46, 204, 113));
        JButton backButton = createStyledButton("Back", new Color(149, 165, 166));
        
        loginButton.addActionListener(e -> handleLogin());
        backButton.addActionListener(e -> {
            dispose();
            new WelcomeScreen();
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void handleLogin() {
        String accountNumber = accountField.getText();
        String password = new String(passwordField.getPassword());
        
        if (accountNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Account account = bankManager.login(accountNumber, password);
        if (account != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + account.getAccountHolderName(), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new DashboardWindow(account, bankManager);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid account number or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}