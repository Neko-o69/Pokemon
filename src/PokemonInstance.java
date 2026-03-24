public class PokemonInstance {
    private PokemonEspece espece;
    private int niveau;
    private int pv;
    private int pvMax;

    public PokemonInstance(PokemonEspece espece, int niveau) {
        this.espece = espece;
        this.niveau = niveau;
        this.pvMax = 100 + niveau * 2;
        this.pv = pvMax;
    }

    public boolean estKO() {
        return pv <= 0;
    }

    public void subirDegats(int degats) {
        pv -= degats;
        if (pv < 0) pv = 0;
    }

    public String getNom() {
        return espece.getNom();
    }

    public int getPv() {
        return pv;
    }

    public Type getType() {
        return espece.getType();
    }
}