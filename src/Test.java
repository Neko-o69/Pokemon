import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test{
    public static void main(String[] args) {
        //TODO : Connexion à la bd et tests divers 

        DatabaseManager dbm = new DatabaseManager();

        try{
            dbm.connect();
        }catch(SQLException e){
            System.out.println("Erreur : " + e.getMessage());
        }



    }
    /**
     * Créer une requête de selection simple 
     */

    String sql = "SELECT * FROM pokemon"
    try{
        PreparedStatement requete = dbm.getConnection().PreparedStatement(sql);
        ResultSet donnees = requete.executeQuery();
            while(donnees.next()){
                System.out.println(donnees.getInt("id")+":"+donnees.getString("nom")+";"
            }
   }catch(SQLException e){
        System.out.println(e.getErrorCode());
    }
}