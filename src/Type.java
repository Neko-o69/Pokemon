import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Type {
    private String nom;

    public Type(String nom) {
        this.nom = nom;
    }

    public boolean havePenaltyVs(Type typeAdverse) {
        return false;
    }

    public boolean haveBonusVs(Type typeAdverse) {
        return false;
    }

    public boolean isEfficient(Type typeAdverse) {
        return false;
    }

    public boolean isSameAs(Pokemon pokemon) {
        return false;
    }

    public double recupMulti(int fkAtt,int fkDef){
    String sql = "SELECT multi from efficacite WHERE fkAtt = ? and fkDef= ? LIMIT 1;";
    DatabaseManager dbm = new DatabaseManager();
    try {
        dbm.connect();
        System.out.println("Connexion réussie !");
    PreparedStatement pstmt = dbm.getConnection().prepareStatement(sql);
    pstmt.setInt(1,fkAtt);
    pstmt.setInt(2,fkDef);
    ResultSet multi = pstmt.executeQuery(sql);  


    return 1 ;
            } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

  return 2;
}

}