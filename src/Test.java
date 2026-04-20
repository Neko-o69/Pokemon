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


        JoueurHumain joueur1 = new JoueurHumain("Othmane");
        JoueurHumain joueur2 = new JoueurHumain("Jessim");

        
        Pokemon pikachu = new Pokemon("Pikachu");
        Pokemon salameche = new Pokemon("Salameche");

        
        pikachu.setPvMax(100);
        pikachu.setPv(100);
        pikachu.setAttaque(20);

        salameche.setPvMax(100);
        salameche.setPv(100);
        salameche.setAttaque(18);

        
        joueur1.setPokemon(0, pikachu);
        joueur2.setPokemon(0, salameche);

       
        Partie partie = new Partie(joueur1, joueur2);
        partie.jouerPartie();
    }


}