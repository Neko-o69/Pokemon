public class PokemonEspece {
    private int id;
    private String nom;
    private Type type;

    public PokemonEspece(int id, String nom, Type type) {
        this.id = id;
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public Type getType() {
        return type;
    }
}