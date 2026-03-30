abstract class Joueur {
    protected String nom;
    public static final int NB_POKEMON = 6;
    private int nbPokemonsActifs = NB_POKEMON;
    private Pokemon[] equipePokemon;

    public Joueur(String nom) {
        this.nom = nom;
        this.equipePokemon = new Pokemon[NB_POKEMON];
    }

    public Joueur(String nom, Pokemon[] p) {
        this(nom); 
        for (int i = 0; i < NB_POKEMON; i++) {
            this.equipePokemon[i] = p[i];
        }   

    }   
    public int getNbPokemonsActifs(){
        return this.nbPokemonsActifs;

    }
}