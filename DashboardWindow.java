import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardWindow extends JFrame {
    private Account account;
    private BankManager bankManager;
    private JLabel balanceLabel;
    
    public DashboardWindow(Account account, BankManager manager) {
        this.account = account;
        this.bankManager = manager;
        
        setTitle("Banking Dashboard");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(44, 62, 80);
                Color color2 = new Color(52, 152, 219);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 1, 5, 5));
        topPanel.setBackground(new Color(236, 240, 241));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel welcomeLabel = new JLabel("👋 Welcome, " + account.getAccountHolderName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        topPanel.add(welcomeLabel);
        
        JLabel accountLabel = new JLabel("Account Number: " + account.getAccountNumber());
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(accountLabel);
        
        balanceLabel = new JLabel("💰 Balance: Rs. " + String.format("%.2f", account.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        balanceLabel.setForeground(new Color(39, 174, 96));
        topPanel.add(balanceLabel);
        
        JLabel timestampLabel = new JLabel("Last updated: " + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        timestampLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        timestampLabel.setForeground(Color.GRAY);
        topPanel.add(timestampLabel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton depositButton = createDashboardButton("💵 Deposit", new Color(46, 204, 113));
        JButton withdrawButton = createDashboardButton("💸 Withdraw", new Color(231, 76, 60));
        JButton balanceButton = createDashboardButton("💰 Check Balance", new Color(52, 152, 219));
        JButton historyButton = createDashboardButton("📜 Transaction History", new Color(155, 89, 182));
        JButton profileButton = createDashboardButton("👤 Profile", new Color(241, 196, 15));
        JButton logoutButton = createDashboardButton("🚪 Logout", new Color(149, 165, 166));
        
        depositButton.addActionListener(e -> handleDeposit());
        withdrawButton.addActionListener(e -> handleWithdraw());
        balanceButton.addActionListener(e -> {
            updateBalance();
            JOptionPane.showMessageDialog(this, 
                "💰 Current Balance: Rs. " + String.format("%.2f", account.getBalance()),
                "Balance Inquiry", JOptionPane.INFORMATION_MESSAGE);
        });
        historyButton.addActionListener(e -> showTransactionHistory());
        profileButton.addActionListener(e -> showProfile());
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new WelcomeScreen();
            }
        });
        
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(balanceButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(profileButton);
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JButton createDashboardButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 60));
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
    
    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit (Rs):", 
            "Deposit Money", JOptionPane.QUESTION_MESSAGE);
        if (amountStr == null) return;
        
        try {
            double amount = Double.parseDouble(amountStr);
            if (account.deposit(amount)) {
                bankManager.updateAccount();
                updateBalance();
                JOptionPane.showMessageDialog(this, 
                    "✅ Deposit successful!\n\n" +
                    "Amount Deposited: Rs." + String.format("%.2f", amount) + "\n" +
                    "New Balance: Rs." + String.format("%.2f", account.getBalance()),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleWithdraw() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw (Rs):", 
            "Withdraw Money", JOptionPane.QUESTION_MESSAGE);
        if (amountStr == null) return;
        
        try {
            double amount = Double.parseDouble(amountStr);
            if (account.withdraw(amount)) {
                bankManager.updateAccount();
                updateBalance();
                JOptionPane.showMessageDialog(this, 
                    "✅ Withdrawal successful!\n\n" +
                    "Amount Withdrawn: Rs." + String.format("%.2f", amount) + "\n" +
                    "Remaining Balance: Rs." + String.format("%.2f", account.getBalance()),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Transaction failed!\n\nInsufficient balance or invalid amount!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showTransactionHistory() {
        JFrame historyFrame = new JFrame("Transaction History");
        historyFrame.setSize(600, 400);
        historyFrame.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        StringBuilder history = new StringBuilder();
        history.append("📜 TRANSACTION HISTORY\n");
        history.append("Account: ").append(account.getAccountNumber()).append("\n");
        history.append("=".repeat(70)).append("\n\n");
        
        if (account.getTransactionHistory().isEmpty()) {
            history.append("No transactions yet.");
        } else {
            for (String transaction : account.getTransactionHistory()) {
                history.append(transaction).append("\n");
            }
        }
        
        textArea.setText(history.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        historyFrame.add(scrollPane);
        historyFrame.setVisible(true);
    }
    
    private void showProfile() {
        String profileInfo = "👤 ACCOUNT PROFILE\n\n" +
                           "Account Holder: " + account.getAccountHolderName() + "\n" +
                           "Account Number: " + account.getAccountNumber() + "\n" +
                           "Current Balance: Rs." + String.format("%.2f", account.getBalance()) + "\n" +
                           "Total Transactions: " + account.getTransactionHistory().size();
        
        JOptionPane.showMessageDialog(this, profileInfo, "Profile", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateBalance() {
        balanceLabel.setText("💰 Balance: Rs. " + String.format("%.2f", account.getBalance()));
    }
}