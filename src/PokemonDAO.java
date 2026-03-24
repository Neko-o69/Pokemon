import java.sql.*;
import java.util.ArrayList;

public class PokemonDAO {

    public static ArrayList<PokemonEspece> chargerPokemons(DatabaseManager db) {
        ArrayList<PokemonEspece> liste = new ArrayList<>();

        try {
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pokemon");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");

                // type simple pour l’instant
                Type type = new Type(1, "Normal");

                PokemonEspece p = new PokemonEspece(id, nom, type);
                liste.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }
}