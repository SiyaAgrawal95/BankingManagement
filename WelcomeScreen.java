import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen extends JFrame {
    
    public WelcomeScreen() {
        setTitle("🏦 Royal Bank of India");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(109, 213, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🏦 ROYAL BANK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel welcomeLabel = new JLabel("Welcome! Please select an option:");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        centerPanel.add(welcomeLabel, gbc);
        
        JButton customerButton = createStyledButton("👤 Customer Login", new Color(46, 204, 113));
        customerButton.addActionListener(e -> {
            dispose();
            new LoginWindow();
        });
        gbc.gridy = 1;
        centerPanel.add(customerButton, gbc);
        
        JButton createButton = createStyledButton("✨ Create New Account", new Color(241, 196, 15));
        createButton.addActionListener(e -> {
            dispose();
            new CreateAccountWindow();
        });
        gbc.gridy = 2;
        centerPanel.add(createButton, gbc);
        
        JButton adminButton = createStyledButton("🔐 Admin Panel", new Color(231, 76, 60));
        adminButton.addActionListener(e -> openAdminPanel());
        gbc.gridy = 3;
        centerPanel.add(adminButton, gbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("Your trusted banking partner 💙");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void openAdminPanel() {
        String password = JOptionPane.showInputDialog(this, "Enter Admin Password:", 
            "Admin Login", JOptionPane.QUESTION_MESSAGE);
        
        if (password != null) {
            BankManager manager = new BankManager();
            if (manager.adminLogin(password)) {
                dispose();
                new AdminPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect admin password!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}