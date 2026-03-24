public class Attaque {
    private String nom;
    private int puissance;
    private Type type;

    public Attaque(String nom, int puissance, Type type) {
        this.nom = nom;
        this.puissance = puissance;
        this.type = type;
    }

    public int getPuissance() {
        return puissance;
    }

    public Type getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }
}