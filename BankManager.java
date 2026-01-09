import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class BankManager {
    private ArrayList<Account> accounts;
    private String filename = "accounts_data.txt";
    private static final String ADMIN_PASSWORD = "admin123";
    
    public BankManager() {
        accounts = new ArrayList<>();
        loadAccounts();
    }
    
    public String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;
        boolean unique = false;
        
        do {
            accountNumber = "RBI" + String.format("%010d", random.nextInt(1000000000));
            unique = true;
            
            for (Account acc : accounts) {
                if (acc.getAccountNumber().equals(accountNumber)) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);
        
        return accountNumber;
    }
    
    public String createAccount(String name, String dob, String gender, String phone, 
                               String email, String address, String aadhaar, String pan,
                               String password, double initialDeposit, String accountType) {
        String accountNumber = generateAccountNumber();
        Account newAccount = new Account(accountNumber, name, dob, gender, phone, email,
                                        address, aadhaar, pan, password, initialDeposit, accountType);
        accounts.add(newAccount);
        saveAccounts();
        return accountNumber;
    }
    
    public Account login(String accountNumber, String password) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber) && acc.getPassword().equals(password)) {
                return acc;
            }
        }
        return null;
    }
    
    public boolean adminLogin(String password) {
        return password.equals(ADMIN_PASSWORD);
    }
    
    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }
    
    private void saveAccounts() {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("╔════════════════════════════════════════════════════════════════════╗\n");
            writer.write("║           ROYAL BANK OF INDIA - ACCOUNT DATABASE                  ║\n");
            writer.write("║                 Confidential Information                          ║\n");
            writer.write("╚════════════════════════════════════════════════════════════════════╝\n\n");
            
            for (Account acc : accounts) {
                writer.write(acc.toFileString());
            }
            
            writer.write("\n\n");
            writer.write("═══════════════════════════════════════════════════════════════════════\n");
            writer.write("Total Accounts: " + accounts.size() + "\n");
            writer.write("Last Updated: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n");
            writer.write("═══════════════════════════════════════════════════════════════════════\n");
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }
    
    private void loadAccounts() {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            Account currentAccount = null;
            String accountNumber = null, name = null, dob = null, gender = null;
            String phone = null, email = null, address = null, aadhaar = null;
            String pan = null, password = null, accountType = null, dateCreated = null;
            double balance = 0;
            String transactionHistory = null;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.equals("ACCOUNT_START")) {
                    accountNumber = name = dob = gender = phone = email = address = null;
                    aadhaar = pan = password = accountType = dateCreated = transactionHistory = null;
                    balance = 0;
                } else if (line.startsWith("Account Number: ")) {
                    accountNumber = line.substring("Account Number: ".length());
                } else if (line.startsWith("Account Holder: ")) {
                    name = line.substring("Account Holder: ".length());
                } else if (line.startsWith("Date of Birth: ")) {
                    dob = line.substring("Date of Birth: ".length());
                } else if (line.startsWith("Gender: ")) {
                    gender = line.substring("Gender: ".length());
                } else if (line.startsWith("Phone: ")) {
                    phone = line.substring("Phone: ".length());
                } else if (line.startsWith("Email: ")) {
                    email = line.substring("Email: ".length());
                } else if (line.startsWith("Address: ")) {
                    address = line.substring("Address: ".length());
                } else if (line.startsWith("Aadhaar: ")) {
                    aadhaar = line.substring("Aadhaar: ".length());
                } else if (line.startsWith("PAN: ")) {
                    pan = line.substring("PAN: ".length());
                } else if (line.startsWith("Password: ")) {
                    password = line.substring("Password: ".length());
                } else if (line.startsWith("Balance: ")) {
                    balance = Double.parseDouble(line.substring("Balance: ".length()));
                } else if (line.startsWith("Account Type: ")) {
                    accountType = line.substring("Account Type: ".length());
                } else if (line.startsWith("Date Created: ")) {
                    dateCreated = line.substring("Date Created: ".length());
                } else if (line.startsWith("Transaction History: ")) {
                    transactionHistory = line.substring("Transaction History: ".length());
                } else if (line.equals("ACCOUNT_END")) {
                    if (accountNumber != null && name != null) {
                        currentAccount = new Account(accountNumber, name, dob, gender, phone, 
                                                    email, address, aadhaar, pan, password, 
                                                    balance, accountType, dateCreated);
                        if (transactionHistory != null) {
                            currentAccount.loadTransactionHistory(transactionHistory);
                        }
                        accounts.add(currentAccount);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
    
    public void updateAccount() {
        saveAccounts();
    }
}