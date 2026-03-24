import java.util.ArrayList;

public class Joueur {
    private String nom;
    private ArrayList<PokemonInstance> equipe;
    private int indexCourant = 0;

    public Joueur(String nom) {
        this.nom = nom;
        this.equipe = new ArrayList<>();
    }

    public void ajouterPokemon(PokemonInstance p) {
        equipe.add(p);
    }

    public PokemonInstance getPokemonActuel() {
        return equipe.get(indexCourant);
    }

    public void suivant() {
        indexCourant++;
    }

    public boolean aPerdu() {
        return indexCourant >= equipe.size();
    }

    public String getNom() {
        return nom;
    }
}