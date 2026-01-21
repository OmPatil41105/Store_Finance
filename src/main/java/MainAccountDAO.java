import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainAccountDAO {

    public int validateLogin(String username, String password) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT main_id FROM main_account WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("main_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int createAccount(String username, String password, double balance) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO main_account(username,password,balance) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setDouble(3, balance);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public void deleteMainAccount(int mainId) {
        try {
            Connection con = DBConnection.getConnection();

            // 1. Delete transaction history
            String sql1 =
                    "DELETE t FROM transaction_history t " +
                            "JOIN profile p ON t.profile_id = p.profile_id " +
                            "WHERE p.main_id = ?";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setInt(1, mainId);
            ps1.executeUpdate();

            // 2. Delete profiles
            String sql2 = "DELETE FROM profile WHERE main_id = ?";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, mainId);
            ps2.executeUpdate();

            // 3. Delete main account
            String sql3 = "DELETE FROM main_account WHERE main_id = ?";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.setInt(1, mainId);
            ps3.executeUpdate();

            System.out.println("Main account deleted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public double getBalance(int mainId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT balance FROM main_account WHERE main_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, mainId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateBalance(int mainId, double newBalance) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE main_account SET balance=? WHERE main_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, newBalance);
            ps.setInt(2, mainId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
