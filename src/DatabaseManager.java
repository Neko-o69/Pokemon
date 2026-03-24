import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsable de la connexion à la base de données.
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/pokemon?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // adapte si besoin
    private static final String PASSWORD = ""; // adapte si besoin

    private Connection connection;

    /**
     * Ouvre la connexion à la base.
     */
    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à la base réussie !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion à la base !");
            e.printStackTrace();
        }
    }

    /**
     * Retourne la connexion active.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Ferme la connexion.
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Connexion fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}