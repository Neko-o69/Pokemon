import java.sql.*;

public class TestSQL {

    public static void main(String[] args) {

        DatabaseManager db = new DatabaseManager();
        db.connect();

        try {
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pokemon");

            while (rs.next()) {
                System.out.println(rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.disconnect();
    }
}