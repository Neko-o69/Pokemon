import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    PreparedStatement pstmt = dbm.prepareStatement(sql);
    pstmt.setInt(1,fkAtt);
    pstmt.setInt(2,fkDef);
    ResultSet multi = pstmt.executeQuery(sql);
    return multi ;
}

}