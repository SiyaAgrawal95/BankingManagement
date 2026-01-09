import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private String aadhaarNumber;
    private String panNumber;
    private String password;
    private double balance;
    private String accountType;
    private String dateCreated;
    private ArrayList<String> transactionHistory;
    
    public Account(String accountNumber, String accountHolderName, String dateOfBirth, 
                   String gender, String phoneNumber, String email, String address,
                   String aadhaarNumber, String panNumber, String password, 
                   double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.aadhaarNumber = aadhaarNumber;
        this.panNumber = panNumber;
        this.password = password;
        this.balance = balance;
        this.accountType = accountType;
        this.transactionHistory = new ArrayList<>();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.dateCreated = LocalDateTime.now().format(formatter);
        
        addTransaction("Account opened with initial balance: Rs." + balance);
    }
    
    // Constructor for loading from file
    public Account(String accountNumber, String accountHolderName, String dateOfBirth, 
                   String gender, String phoneNumber, String email, String address,
                   String aadhaarNumber, String panNumber, String password, 
                   double balance, String accountType, String dateCreated) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.aadhaarNumber = aadhaarNumber;
        this.panNumber = panNumber;
        this.password = password;
        this.balance = balance;
        this.accountType = accountType;
        this.dateCreated = dateCreated;
        this.transactionHistory = new ArrayList<>();
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getAadhaarNumber() { return aadhaarNumber; }
    public String getPanNumber() { return panNumber; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getDateCreated() { return dateCreated; }
    public ArrayList<String> getTransactionHistory() { return transactionHistory; }
    
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
            addTransaction("Deposited: Rs." + String.format("%.2f", amount) + " | Balance: Rs." + String.format("%.2f", balance));
            return true;
        }
        return false;
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance = balance - amount;
            addTransaction("Withdrawn: Rs." + String.format("%.2f", amount) + " | Balance: Rs." + String.format("%.2f", balance));
            return true;
        }
        return false;
    }
    
    private void addTransaction(String transaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        transactionHistory.add(timestamp + " - " + transaction);
    }
    
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ACCOUNT_START\n");
        sb.append("Account Number: ").append(accountNumber).append("\n");
        sb.append("Account Holder: ").append(accountHolderName).append("\n");
        sb.append("Date of Birth: ").append(dateOfBirth).append("\n");
        sb.append("Gender: ").append(gender).append("\n");
        sb.append("Phone: ").append(phoneNumber).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Address: ").append(address).append("\n");
        sb.append("Aadhaar: ").append(aadhaarNumber).append("\n");
        sb.append("PAN: ").append(panNumber).append("\n");
        sb.append("Password: ").append(password).append("\n");
        sb.append("Balance: ").append(balance).append("\n");
        sb.append("Account Type: ").append(accountType).append("\n");
        sb.append("Date Created: ").append(dateCreated).append("\n");
        sb.append("Transaction History: ").append(String.join("||", transactionHistory)).append("\n");
        sb.append("ACCOUNT_END\n");
        sb.append("========================================\n");
        return sb.toString();
    }
    
    public void loadTransactionHistory(String historyString) {
        if (historyString != null && !historyString.isEmpty() && !historyString.equals("null")) {
            String[] transactions = historyString.split("\\|\\|");
            for (String transaction : transactions) {
                if (!transaction.trim().isEmpty()) {
                    transactionHistory.add(transaction);
                }
            }
        }
    }
}