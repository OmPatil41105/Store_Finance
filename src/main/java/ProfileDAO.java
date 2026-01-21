import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProfileDAO {

    public void createProfiles(int mainId, List<String> names) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO profile(profile_name, main_id) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            for (String n : names) {
                ps.setString(1, n);
                ps.setInt(2, mainId);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProfiles(int mainId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT profile_id, profile_name FROM profile WHERE main_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, mainId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("profile_id") + ". " + rs.getString("profile_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
