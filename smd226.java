import java.util.*;
import java.sql.*;
class smd226{
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";
    public static String banks;
    public static String action;
    public static long accountID;
    public static long customerID;
    public static String cardType;
    public static float withdraw;
    public static float deposit;
    public static Connection conn = null;
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int mode = 0;
        do{
                try{
                    System.out.print("Enter Oracle User id: "); 
                    String userid = input.nextLine();
                    System.out.print("Enter oracle password for " + userid + ": ");
                    String passwd = input.nextLine();
                    conn = DriverManager.getConnection(DB_URL, userid, passwd);
                    System.out.println("Super! I'm connected");
                    //conn.close();
                }catch(SQLException ex){
                    ex.printStackTrace();
                    System.out.println("[Error]: Connect Error. Re-enter log-in data:");
                }
        } while(conn == null);

        do{
            try{
                System.out.print("Which interface would you like to use today? Only 3 are available today, interface 2, 4, and 7.\n 1. Bank Management\n 2. Account deposit/withdrawal\n 3. Payment on a loan or credit card\n 4. Opening of a new account \n 5. Obtaining a new or replacement credit or debit card \n 6. Taking out a new loan \n 7. Purchases using a card\n use 9 to exit\n");
                mode = input.nextInt();
                if(mode == 2){
                    accountChange();
                    break;
                }else if(mode == 4){
                    openAccount();
                    break;
                }else if(mode == 7){
                    PurchaseCard();
                    break;
                }else if(mode == 9){
                    System.out.println("You are exiting the interface");
                    break;
                }else{
                    System.out.println("Please select another mode this one is not offered at the moment");
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Invalid data given. Please re enter");
            }
        } while(mode != 9);
    }
    static void accountChange(){
        Scanner input2 = new Scanner(System.in);
        Boolean worked = false;
        do{
            try{
                System.out.print("What type of branch would you like to use today? A teller or an ATM?\n");
                banks = input2.nextLine();
            }catch(Exception e){
                System.out.println("Invalid data given. Please re enter." + e.getMessage());
            }
        } while(!(banks.equals("atm")) && !(banks.equals("ATM")) && !(banks.equals("teller")) && !(banks.equals("TELLER")));
        if(banks.equals("ATM") || banks.equals("atm")){
            do{
                try{
                    System.out.print("Hello, would you like to withdraw money today or check your balance of your account? If so please type in withdraw or check\n");
                    action = input2.nextLine();
                }catch(Exception e){
                    System.out.println("Invalid data given. Please re enter." + e.getMessage());
                }
            } while(!(action.equals("check")) && !(action.equals("withdraw")));
            if(action.equals("withdraw")){
                do{
                    try{
                        float newBalance = 0;
                        System.out.print("Please enter in your customer id: (If you would like to exit type in 0)\n");
                        customerID = input2.nextLong();
                        String findAccount = "Select Account_id from customer_accounts where C_id = ?";
                        PreparedStatement pstmt2 = conn.prepareStatement(findAccount);
                        pstmt2.setLong(1,customerID);
                        ResultSet r = pstmt2.executeQuery(); 
                        while(r.next()){
                            accountID = r.getInt("Account_id");
                        }
                        System.out.print("please enter in the amount that you would like to withdraw (Do not add a $ sign)\n");
                        withdraw = input2.nextFloat();
                        try{
                            withdraw = Float.valueOf(withdraw);
                        }catch(NumberFormatException e){
                            System.out.println("Invalid type of number entered. Please try a float next time.");
                            break;
                        }
                        String balanceE = "Select balance from Accounts where Account_id = ?";
                        PreparedStatement test = conn.prepareStatement(balanceE);
                        test.setLong(1,accountID);
                        ResultSet work = test.executeQuery();
                        while(work.next()){
                            newBalance = work.getFloat("Balance");
                        }
                        newBalance = withdraw; //before this was newBalance-=withdraw however for some reason that wasn't withdrawing the money but this is
                        String Account_withdrawal = "Update Accounts SET balance = (balance - ?) where Account_id = ? AND balance >= ?";
                        PreparedStatement wstmt = conn.prepareStatement(Account_withdrawal);
                        wstmt.setFloat(1, newBalance);
                        wstmt.setLong(2, accountID);
                        wstmt.setDouble(3, withdraw); 
                        int didIwithdraw = wstmt.executeUpdate();
                        if(didIwithdraw > 0){
                            System.out.println("withdrawal succesful.\n"); 
                            worked = true;
                        }else{
                        System.out.println("withdrawal was not succesful. The balance is less than the withdrawal amount.\n");
                        }
                    }catch(Exception e){
                         System.out.println("Invalid data given. Please re enter." + e.getMessage());
                    }
                } while(customerID != 0 && worked != true);
            }else{
                do{
                    try{
                        System.out.print("Please enter in your customer id: (If you would like to exit type in 0).\n"); //first we are given c_id which is used to find account_id and then find the balance
                        customerID = input2.nextLong();
                        String findAccount = "Select Account_id from customer_accounts where C_id = ?";
                        PreparedStatement pstmt2 = conn.prepareStatement(findAccount);
                        pstmt2.setLong(1,customerID);
                        ResultSet r = pstmt2.executeQuery(); 
                        while(r.next()){
                            accountID = r.getInt("Account_id");
                        }
                        String balances = "Select balance from Accounts where Account_id = ?";
                        PreparedStatement pstmt3 = conn.prepareStatement(balances);
                        pstmt3.setLong(1,accountID);
                        ResultSet resultset = pstmt3.executeQuery();
                        while(resultset.next()){
                            System.out.println("Your balance is: " + resultset.getFloat("balance") + "0");
                            worked = true;
                        }
                    }catch(Exception e){
                         System.out.println("Invalid data given. Please re enter." + e.getMessage());
                    }
                } while(customerID != 0 && worked != true);
            }
        }else{
            do{
                try{
                    System.out.print("Hello sir, my name is Ben. What would you like to do today, withdraw, deposit, or check your balance?\n (Use deposit to deposit money into the bank, withdraw to withdraw money, and check to check balance)\n");
                    action = input2.nextLine();
                }catch(Exception e){
                    System.out.println("Invalid data given. Please re enter." + e.getMessage());
                }
            } while(!(action.equals("withdraw")) && !(action.equals("deposit")) && !(action.equals("check")));
            if(action.equals("withdraw")){
                do{
                    try{
                        float newBalance = 0;
                        System.out.print("Please enter in your customer id: (If you would like to exit type in 0)\n");
                        customerID = input2.nextLong();
                        String findAccount = "Select Account_id from customer_accounts where C_id = ?";
                        PreparedStatement fstmt = conn.prepareStatement(findAccount);
                        fstmt.setLong(1,customerID);
                        ResultSet r = fstmt.executeQuery(); 
                        while(r.next()){
                            accountID = r.getInt("Account_id");
                        }
                        System.out.print("please enter in the amount that you would like to withdraw (Do not add a $ sign)\n");
                        withdraw = input2.nextFloat();
                        try{
                            withdraw = Float.valueOf(withdraw);
                        }catch(NumberFormatException e){
                            System.out.println("Invalid type of number entered. Please try a float next time.");
                            break;
                        }
                        String balanceE = "Select balance from Accounts where Account_id = ?";
                        PreparedStatement test = conn.prepareStatement(balanceE);
                        test.setLong(1,accountID);
                        ResultSet work = test.executeQuery();
                        while(work.next()){
                            newBalance = work.getFloat("Balance");
                        }
                        newBalance = withdraw; //before this was newBalance-=withdraw however for some reason that wasn't withdrawing the money but this is
                        String Account_withdrawal = "Update Accounts SET balance = (balance - ?) where Account_id = ? AND balance >= ?";
                        PreparedStatement wstmt = conn.prepareStatement(Account_withdrawal);
                        wstmt.setFloat(1, newBalance);
                        wstmt.setLong(2, accountID);
                        wstmt.setDouble(3, withdraw);
                        int didIwithdraw = wstmt.executeUpdate(); 
                        if(didIwithdraw > 0){
                            System.out.println("withdrawal succesful.\n"); 
                            worked = true;
                        }else{
                        System.out.println("withdrawal was not succesful. The balance is less than the withdrawal amount.\n");
                        }
                    }catch(Exception e){
                        System.out.println("Invalid data given. Please re enter." + e.getMessage());
                    }
                } while(customerID != 0 && worked != true);
            }else if(action.equals("deposit")){
                do{
                    try{
                        float deposit_amount = 0;
                        System.out.print("Please enter in your customer id: (If you would like to exit type in 0)\n");
                        customerID = input2.nextLong();
                        String findAccount = "Select Account_id from customer_accounts where C_id = ?";
                        PreparedStatement pstmt2 = conn.prepareStatement(findAccount);
                        pstmt2.setLong(1,customerID);
                        ResultSet r = pstmt2.executeQuery(); 
                        while(r.next()){
                            accountID = r.getInt("Account_id");
                        }
                        System.out.println("Please type the amount of money that you would like to deposit (Do not add $ sign)");
                        deposit = input2.nextFloat();
                        try{
                            deposit = Float.valueOf(deposit);
                        }catch(NumberFormatException e){
                            System.out.println("Invalid type of number entered. Please try a float next time.");
                            break;
                        }
                        String balanceDs = "Select balance from Accounts where Account_id = ?";
                        PreparedStatement test = conn.prepareStatement(balanceDs);
                        test.setLong(1,accountID);
                        ResultSet work = test.executeQuery();
                        while(work.next()){
                            deposit_amount = work.getFloat("Balance");
                        }
                        deposit_amount += deposit;
                        System.out.println(deposit_amount);
                        String Account_Deposit = "Update Accounts SET balance = ? where Account_id = ?";
                        PreparedStatement adstmt = conn.prepareStatement(Account_Deposit);
                        adstmt.setFloat(1, deposit_amount);
                        adstmt.setLong(2, accountID);
                        int didIdeposit = adstmt.executeUpdate();
                        if(didIdeposit > 0){
                            System.out.println("Deposit succesful.\n"); 
                            worked = true;
                        }else{
                        System.out.println("Deposit was not succesful. The balance is less than the withdrawal amount.\n");
                        }
                    }catch(Exception e){
                        System.out.println("Invalid data given. Please re enter." + e.getMessage());
                    }
                }while(customerID != 0 && worked != true);
            }else{
                do{
                    try{
                        System.out.print("Please enter in your customer id: (If you would like to exit type in 0).\n"); //first we are given c_id which is used to find account_id and then find the balance
                        customerID = input2.nextLong();
                        String findAccount = "Select Account_id from customer_accounts where c_id = ?";
                        PreparedStatement pstmt2 = conn.prepareStatement(findAccount);
                        pstmt2.setLong(1,customerID);
                        ResultSet r = pstmt2.executeQuery(); 
                        while(r.next()){
                            accountID = r.getInt("Account_id");
                        }
                        String balances = "Select balance from Accounts where Account_id = ?";
                        PreparedStatement pstmt3 = conn.prepareStatement(balances);
                        pstmt3.setLong(1,accountID);
                        ResultSet resultset = pstmt3.executeQuery();
                        while(resultset.next()){
                            System.out.println("Your balance is: " + resultset.getFloat("balance") + "0");
                            worked = true;
                        }
                    }catch(Exception e){
                         System.out.println("Invalid data given. Please re enter." + e.getMessage());
                    }
                } while(customerID != 0 && worked != true);
            }
        }
    }
    static void openAccount(){
        long newID;
        long newCID;
        long BranchCode = 0;
        float balanceF = 0;
        float balanceD = 0;
        String branches = "";
        String AccountType = "";
        String accept = "";
        do{
            try{
                Scanner input3 = new Scanner(System.in);
                System.out.println("Hello! Would you like to create a new account? If yes please type y otherwise type n");
                accept = input3.nextLine();
                if(accept.equals("n")){
                    break;
                }
                else if(!accept.equals("y")){
                    System.out.println("Invalid data entered, please try again");
                    break;
                }
                System.out.println("Please type in a 8 digit id that you would like to use for your account(At most 8 digits can be smaller)");
                newID = input3.nextLong();
                if(newID < 0 || newID > 99999999){
                    System.out.println("You entered a number that was too big please try again");
                    break;
                }
                System.out.println("Please type in another 8 digit id that will be used for your customer account(At most 8 digits can be smaller)");
                newCID = input3.nextLong();
                if(newCID < 0 || newCID > 99999999){
                    System.out.println("You entered a number that was too big please try again");
                    break;
                }
                while(!(branches.equals("North")) && !(branches.equals("South")) && !(branches.equals("uptown")) && !(branches.equals("downtown")) && !(branches.equals("West")) && !(branches.equals("East")) && !(branches.equals("NorthWest")) && !(branches.equals("NorthEast")) && !(branches.equals("SouthEast"))){
                    System.out.println("What branch would work best for you? (North, South, uptown, downtown, West, East, NorthWest, SouthWest, NorthEast, SouthEast)");
                    branches = input3.nextLine();
                }
                String branchC = "select branch_code from branch where name = ?";
                PreparedStatement pstmt5 = conn.prepareStatement(branchC);
                pstmt5.setString(1, branches);
                ResultSet rs = pstmt5.executeQuery();
                while(rs.next()){
                    BranchCode = rs.getInt("branch_Code");
                }
                while(!(AccountType.equals("Savings")) && !(AccountType.equals("Checking"))){
                    System.out.println("Which account would you like to make? Savings or Checking?");
                    AccountType = input3.nextLine();
                }
                if(AccountType.equals("Savings")){
                    while(balanceF < 300){
                        balanceF = 0;
                        System.out.println("How much money would you like to add to your account? The minimum balance has to be 300");
                        Float addBalance = input3.nextFloat();
                        balanceF += addBalance;
                    }
                        String AccountInfo = "INSERT INTO ACCOUNTS(Account_id, Branch_code, balance) VALUES (?,?,?)";
                        PreparedStatement pstmt6 = conn.prepareStatement(AccountInfo);
                        pstmt6.setLong(1, newID);
                        pstmt6.setLong(2, BranchCode);
                        pstmt6.setFloat(3, balanceF);
                        pstmt6.executeQuery();
                        String AccountInfoS = "INSERT INTO Savings(Account_id, Penalties, Interest_Rates, Minimum_balance) VALUES (?,?,?,?)";
                        PreparedStatement pstmt7 = conn.prepareStatement(AccountInfoS);
                        pstmt7.setLong(1, newID);
                        pstmt7.setFloat(2, 0);
                        pstmt7.setFloat(3, 7);
                        pstmt7.setFloat(4, 300);
                        pstmt7.executeQuery();
                        System.out.println("A new savings account has been added!");
                    }else{
                        while(balanceD == 0){
                            balanceD = 0;
                            System.out.println("Please enter in the balance you'd like to use for the checking account");
                            balanceD = input3.nextFloat();
                        } 
                        String AccountInfoD = "INSERT INTO ACCOUNTS(Account_id, Branch_code, balance) VALUES (?,?,?)";
                        PreparedStatement pstmt8 = conn.prepareStatement(AccountInfoD);
                        pstmt8.setLong(1, newID);
                        pstmt8.setLong(2, BranchCode);
                        pstmt8.setFloat(3, balanceF);
                        pstmt8.executeQuery();
                        String enterChecking = "INSERT INTO checking(Account_id) VALUES (?)";
                        PreparedStatement pstmt9 = conn.prepareStatement(enterChecking);
                        pstmt9.setLong(1, newID);
                        pstmt9.executeQuery();
                        System.out.println("You have created a new checking account!"); 
                    }
                    break;
            }catch(Exception e){
                 System.out.println("Invalid data given. Please re enter." + e.getMessage());
            }
        }while(!(accept.equals("n")));
    }
    static void PurchaseCard(){
        String cardChoice = "";
        Scanner input4 = new Scanner(System.in);
        float spend = 0;
        int cardIDs = 0;
        int accountIDs = 0;
        float newBalance = 0;
        customerID = 0;
        do{
            try{
                System.out.println("Hello and welcome to making a purchase with a card. What type of card would you like to use today (Debit or Credit)? )");
                cardChoice = input4.nextLine();
            }catch(Exception e){
                 System.out.println("Invalid data given. Please re enter." + e.getMessage());
            }
        } while(!(cardChoice.equals("Debit")) && !(cardChoice.equals("Credit")));
        if(cardChoice.equals("Credit")){
            do{
            try{
                System.out.println("What is your customer ID?");
                customerID = input4.nextLong();
                String cardID = "Select card_id from payment_cards where c_id = ?";
                PreparedStatement cstmt = conn.prepareStatement(cardID);
                cstmt.setLong(1, customerID);
                ResultSet cards = cstmt.executeQuery();
                while(cards.next()){
                    cardIDs = cards.getInt("card_id");
                }
            }catch(Exception e){
                 System.out.println("Invalid data given. Please re enter." + e.getMessage());
            }
        } while(customerID == 0);
        do{
            try{
                System.out.println("How much would you like to spend on your credit card?");
                spend = input4.nextFloat();
                String changeCredit = "UPDATE Credit SET Running_Balance = Running_balance + ? where Card_id = ? AND Credit_Limit - ? > 0";
                PreparedStatement pstmt9 = conn.prepareStatement(changeCredit);
                pstmt9.setFloat(1, spend);
                pstmt9.setLong(2, cardIDs);
                pstmt9.setFloat(3, spend);
                pstmt9.executeQuery();
                System.out.println("If there was no error, you where able to spend the money using your credit card succesfully");
            }catch(Exception e){
                 System.out.println("Invalid data given. Please re enter." + e.getMessage());
            }
        } while(spend == 0);
        
        }else{
            do{
                try{
                    System.out.println("What is your customer ID?");
                    customerID = input4.nextLong();
                }catch(Exception e){
                    System.out.println("Invalid data given. Please re enter." + e.getMessage());
                }
            }while(customerID == 0);
            do{
                try{
                    System.out.println("How much would you like to spend on your Debit card?");
                    spend = input4.nextFloat();
                    String checkAccountID = "Select Account_id from Debit where Card_id = ?";
                    PreparedStatement dstmt = conn.prepareStatement(checkAccountID);
                    dstmt.setInt(1, cardIDs);
                    ResultSet lookingBalance = dstmt.executeQuery();
                    while(lookingBalance.next()){
                        accountIDs = lookingBalance.getInt("Account_id");
                    }
                    String balanceE = "Select balance from Accounts where Account_id = ?";
                    PreparedStatement test = conn.prepareStatement(balanceE);
                    test.setLong(1,accountIDs);
                    ResultSet work = test.executeQuery();
                    while(work.next()){
                        newBalance = work.getFloat("Balance");
                    }
                    newBalance = spend; 
                    String Account_withdrawal = "Update Accounts SET balance = (balance - ?) where Account_id = ?";
                    PreparedStatement dstmts = conn.prepareStatement(Account_withdrawal);
                    dstmts.setFloat(1, newBalance);
                    dstmts.setLong(2, accountIDs);
                    dstmts.executeUpdate();
                    System.out.println("If there was no error, you where able to spend the money using your debit card succesfully.");
                }catch(Exception e){
                    System.out.println("Invalid data given. Please re enter." + e.getMessage());
                }
            }while(spend == 0);
        }
    }
}    
    
    
    
    