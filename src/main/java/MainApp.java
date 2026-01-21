import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MainAccountDAO mainDAO = new MainAccountDAO();
        ProfileDAO profileDAO = new ProfileDAO();
        TransactionDAO txnDAO = new TransactionDAO();

        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.print("Choose: ");
        int opt = sc.nextInt();
        sc.nextLine();

        int mainId;

        if (opt == 1) {
            System.out.print("Username: ");
            String u = sc.nextLine();
            System.out.print("Password: ");
            String p = sc.nextLine();
            mainId = mainDAO.validateLogin(u, p);
            if (mainId == -1) {
                System.out.println("Invalid credentials");
                return;
            }
        } else if (opt == 2) {
            System.out.print("Create username: ");
            String u = sc.nextLine();
            System.out.print("Create password: ");
            String p = sc.nextLine();
            System.out.print("Initial balance: ");
            double b = sc.nextDouble();
            sc.nextLine();

            mainId = mainDAO.createAccount(u, p, b);
            if (mainId == -1) {
                System.out.println("Signup failed");
                return;
            }

            System.out.print("Number of profiles: ");
            int n = sc.nextInt();
            sc.nextLine();

            List<String> names = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                System.out.print("Profile " + i + " name: ");
                names.add(sc.nextLine());
            }
            profileDAO.createProfiles(mainId, names);
            System.out.println("Account created.");
        } else {
            System.out.println("Invalid option");
            return;
        }

        System.out.println("\nSelect Profile:");
        profileDAO.showProfiles(mainId);
        System.out.print("Profile ID: ");
        int profileId = sc.nextInt();

        while (true) {
            System.out.println("\n1. Withdraw");
            System.out.println("2. Deposit");
            System.out.println("3. Check Balance");
            System.out.println("4. Transaction History");
            System.out.println("5. Delete Main Account");
            System.out.println("6. Exit");

            int c = sc.nextInt();

            if (c == 6) break;

            double bal = mainDAO.getBalance(mainId);

            switch (c) {
                case 1:
                    System.out.print("Amount: ");
                    double w = sc.nextDouble();
                    bal -= w;
                    mainDAO.updateBalance(mainId, bal);
                    txnDAO.logTransaction(profileId, w, "WITHDRAW", bal);
                    break;
                case 2:
                    System.out.print("Amount: ");
                    double d = sc.nextDouble();
                    bal += d;
                    mainDAO.updateBalance(mainId, bal);
                    txnDAO.logTransaction(profileId, d, "DEPOSIT", bal);
                    break;
                case 3:
                    System.out.println("Balance: " + bal);
                    break;
                case 4:
                    txnDAO.showHistory(mainId);
                    break;
                case 5:
                    System.out.print("Are you sure? (yes/no): ");
                    sc.nextLine(); // consume newline
                    String confirm = sc.nextLine();

                    if (confirm.equalsIgnoreCase("yes")) {
                        mainDAO.deleteMainAccount(mainId);
                        return; // exit program after deletion
                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                    break;

            }
        }

        System.out.println("Done.");
    }
}
