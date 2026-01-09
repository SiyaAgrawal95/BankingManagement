import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateAccountWindow extends JFrame {
    private BankManager bankManager;
    private JTextField nameField, dobField, phoneField, emailField;
    private JTextField aadhaarField, panField, addressField;
    private JComboBox<String> genderBox, accountTypeBox;
    private JPasswordField passwordField, confirmPasswordField;
    private JTextField depositField;
    
    public CreateAccountWindow() {
        this.bankManager = new BankManager();
        
        setTitle("Create New Account - Royal Bank");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(142, 68, 173);
                Color color2 = new Color(243, 156, 18);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("✨ Open New Bank Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        JLabel infoLabel = new JLabel("(Account number will be auto-generated)");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.WHITE);
        
        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setOpaque(false);
        titleWrapper.add(titlePanel, BorderLayout.NORTH);
        titleWrapper.add(infoLabel, BorderLayout.CENTER);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainPanel.add(titleWrapper, BorderLayout.NORTH);
        
        // Form Panel with Scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Full Name
        addFormField(formPanel, gbc, row++, "Full Name *:", nameField = new JTextField(20));
        
        // Date of Birth
        addFormField(formPanel, gbc, row++, "Date of Birth (DD-MM-YYYY) *:", dobField = new JTextField(20));
        
        // Gender
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel genderLabel = new JLabel("Gender *:");
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(genderLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] genders = {"Male", "Female", "Other"};
        genderBox = new JComboBox<>(genders);
        genderBox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(genderBox, gbc);
        row++;
        
        // Phone Number
        addFormField(formPanel, gbc, row++, "Phone Number *:", phoneField = new JTextField(20));
        
        // Email
        addFormField(formPanel, gbc, row++, "Email Address *:", emailField = new JTextField(20));
        
        // Address
        addFormField(formPanel, gbc, row++, "Full Address *:", addressField = new JTextField(20));
        
        // Aadhaar Number
        addFormField(formPanel, gbc, row++, "Aadhaar Number (12 digits) *:", aadhaarField = new JTextField(20));
        
        // PAN Number
        addFormField(formPanel, gbc, row++, "PAN Number *:", panField = new JTextField(20));
        
        // Account Type
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel accountTypeLabel = new JLabel("Account Type *:");
        accountTypeLabel.setForeground(Color.WHITE);
        accountTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(accountTypeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] accountTypes = {"Savings Account", "Current Account", "Fixed Deposit"};
        accountTypeBox = new JComboBox<>(accountTypes);
        accountTypeBox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(accountTypeBox, gbc);
        row++;
        
        // Password
        addFormField(formPanel, gbc, row++, "Create Password *:", passwordField = new JPasswordField(20));
        
        // Confirm Password
        addFormField(formPanel, gbc, row++, "Confirm Password *:", confirmPasswordField = new JPasswordField(20));
        
        // Initial Deposit
        addFormField(formPanel, gbc, row++, "Initial Deposit (Min Rs.500) *:", depositField = new JTextField(20));
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        
        JButton createButton = createStyledButton("Create Account", new Color(46, 204, 113));
        JButton clearButton = createStyledButton("Clear Form", new Color(241, 196, 15));
        JButton backButton = createStyledButton("Back", new Color(149, 165, 166));
        
        createButton.addActionListener(e -> handleCreateAccount());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> {
            dispose();
            new WelcomeScreen();
        });
        
        buttonPanel.add(createButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(field, gbc);
    }
    
    private JButton createStyledButton(String text, Color color) {
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
    
    private void clearForm() {
        nameField.setText("");
        dobField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        aadhaarField.setText("");
        panField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        depositField.setText("");
        genderBox.setSelectedIndex(0);
        accountTypeBox.setSelectedIndex(0);
    }
    
    private void handleCreateAccount() {
        String name = nameField.getText().trim();
        String dob = dobField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String aadhaar = aadhaarField.getText().trim();
        String pan = panField.getText().trim().toUpperCase();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String depositStr = depositField.getText().trim();
        String accountType = (String) accountTypeBox.getSelectedItem();
        
        // Validation
        if (name.isEmpty() || dob.isEmpty() || phone.isEmpty() || email.isEmpty() || 
            address.isEmpty() || aadhaar.isEmpty() || pan.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty() || depositStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all mandatory fields!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!aadhaar.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "Aadhaar must be exactly 12 digits!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
            JOptionPane.showMessageDialog(this, "Invalid PAN format! (e.g., ABCDE1234F)", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double deposit;
        try {
            deposit = Double.parseDouble(depositStr);
            if (deposit < 500) {
                JOptionPane.showMessageDialog(this, "Minimum initial deposit is Rs.500!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String accountNumber = bankManager.createAccount(name, dob, gender, phone, email, 
                                                        address, aadhaar, pan, password, 
                                                        deposit, accountType);
        
        JTextArea messageArea = new JTextArea(
            "🎉 ACCOUNT CREATED SUCCESSFULLY!\n\n" +
            "═══════════════════════════════════════\n" +
            "Account Number: " + accountNumber + "\n" +
            "Account Holder: " + name + "\n" +
            "Account Type: " + accountType + "\n" +
            "Initial Balance: Rs." + String.format("%.2f", deposit) + "\n" +
            "═══════════════════════════════════════\n\n" +
            "⚠️ IMPORTANT: Please note down your account number!\n" +
            "You will need it to login to your account."
        );
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(this, new JScrollPane(messageArea),
            "Account Created", JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
        new WelcomeScreen();
    }
}