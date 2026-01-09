import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AdminPanel extends JFrame {
    private BankManager bankManager;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    
    public AdminPanel() {
        bankManager = new BankManager();
        
        setTitle("🔐 Admin Panel - Royal Bank");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(231, 76, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("🔐 ADMINISTRATOR PANEL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("Logout");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            new WelcomeScreen();
        });
        topPanel.add(backButton, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"Account Number", "Account Holder", "Balance (Rs)", "Total Transactions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        accountTable = new JTable(tableModel);
        accountTable.setFont(new Font("Arial", Font.PLAIN, 13));
        accountTable.setRowHeight(30);
        accountTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        accountTable.getTableHeader().setBackground(new Color(52, 152, 219));
        accountTable.getTableHeader().setForeground(Color.WHITE);
        
        loadAccountData();
        
        JScrollPane scrollPane = new JScrollPane(accountTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(236, 240, 241));
        
        JButton refreshButton = createAdminButton("🔄 Refresh", new Color(52, 152, 219));
        JButton viewDetailsButton = createAdminButton("👁️ View Details", new Color(155, 89, 182));
        JButton statsButton = createAdminButton("📊 Statistics", new Color(46, 204, 113));
        
        refreshButton.addActionListener(e -> {
            loadAccountData();
            JOptionPane.showMessageDialog(this, "Data refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        viewDetailsButton.addActionListener(e -> viewAccountDetails());
        statsButton.addActionListener(e -> showStatistics());
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(viewDetailsButton);
        bottomPanel.add(statsButton);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JButton createAdminButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void loadAccountData() {
        tableModel.setRowCount(0);
        ArrayList<Account> accounts = bankManager.getAllAccounts();
        
        for (Account account : accounts) {
            Object[] row = {
                account.getAccountNumber(),
                account.getAccountHolderName(),
                String.format("%.2f", account.getBalance()),
                account.getTransactionHistory().size()
            };
            tableModel.addRow(row);
        }
    }
    
    private void viewAccountDetails() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account first!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String accountNumber = (String) tableModel.getValueAt(selectedRow, 0);
        ArrayList<Account> accounts = bankManager.getAllAccounts();
        
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                showAccountDetailsWindow(account);
                break;
            }
        }
    }
    
    private void showAccountDetailsWindow(Account account) {
        JFrame detailsFrame = new JFrame("Account Details - " + account.getAccountNumber());
        detailsFrame.setSize(600, 500);
        detailsFrame.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        infoPanel.add(new JLabel("Account Number: " + account.getAccountNumber()));
        infoPanel.add(new JLabel("Account Holder: " + account.getAccountHolderName()));
        infoPanel.add(new JLabel("Balance: Rs." + String.format("%.2f", account.getBalance())));
        infoPanel.add(new JLabel("Total Transactions: " + account.getTransactionHistory().size()));
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder history = new StringBuilder("Transaction History:\n\n");
        for (String transaction : account.getTransactionHistory()) {
            history.append(transaction).append("\n");
        }
        historyArea.setText(history.toString());
        
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        detailsFrame.add(panel);
        detailsFrame.setVisible(true);
    }
    
    private void showStatistics() {
        ArrayList<Account> accounts = bankManager.getAllAccounts();
        
        int totalAccounts = accounts.size();
        double totalBalance = 0;
        int totalTransactions = 0;
        
        for (Account account : accounts) {
            totalBalance += account.getBalance();
            totalTransactions += account.getTransactionHistory().size();
        }
        
        double avgBalance = totalAccounts > 0 ? totalBalance / totalAccounts : 0;
        
        String stats = "📊 BANK STATISTICS\n\n" +
                      "Total Accounts: " + totalAccounts + "\n" +
                      "Total Bank Balance: Rs." + String.format("%.2f", totalBalance) + "\n" +
                      "Average Balance per Account: Rs." + String.format("%.2f", avgBalance) + "\n" +
                      "Total Transactions: " + totalTransactions;
        
        JOptionPane.showMessageDialog(this, stats, "Bank Statistics", JOptionPane.INFORMATION_MESSAGE);
    }
}