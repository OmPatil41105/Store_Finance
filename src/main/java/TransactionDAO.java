import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionDAO {

    public void logTransaction(int profileId, double amount, String type, double balanceAfter) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO transaction_history(profile_id,amount,txn_type,balance_after) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, profileId);
            ps.setDouble(2, amount);
            ps.setString(3, type);
            ps.setDouble(4, balanceAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHistory(int mainId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql =
                    "SELECT p.profile_name, t.amount, t.txn_type, t.balance_after, t.txn_time " +
                            "FROM transaction_history t JOIN profile p ON t.profile_id=p.profile_id " +
                            "WHERE p.main_id=? ORDER BY t.txn_time";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, mainId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(
                        rs.getString("profile_name") + " | " +
                                rs.getString("txn_type") + " | " +
                                rs.getDouble("amount") + " | Balance: " +
                                rs.getDouble("balance_after") + " | " +
                                rs.getTimestamp("txn_time")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
