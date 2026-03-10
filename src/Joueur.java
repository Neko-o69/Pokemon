public class Joueur {
    private String nom; 
    private static final int NB_POKEMON = 6;
    private int nbPokemonsActifs = this.NB_POKEMON;
    private Pokemon [] equipePokemon;


    public Joueur(String nom){
        this.nom = nom;
        equipePokemon = new Pokemon [this.NB_POKEMON];
    }


    public Joueur(String nom, Pokemon p[]){
        super(nom);
        for(int i =0; i<this.NB_POKEMON;i++){
            this.equipePokemon[i] = p[i];
        }
    }

    
}

