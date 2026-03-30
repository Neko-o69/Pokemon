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
}