import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        DatabaseManager dbm = new DatabaseManager();
        try {
            dbm.connect();
            System.out.println("Connexion réussie !");

            String sql = "SELECT * FROM pokemons";
            PreparedStatement requete = dbm.getConnection().prepareStatement(sql);
            ResultSet donnees = requete.executeQuery();
            while (donnees.next()) {
                System.out.println(donnees.getInt("id") + ":" + donnees.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }


        /**
         * Test Classe partie
         */

        Partie maPartie = new Partie();
        System.out.println(maPartie);
        


    }
}