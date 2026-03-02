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

}