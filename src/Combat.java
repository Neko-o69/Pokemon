class Combat {
    public Joueur joueur1;
    public Joueur joueur2;
 
    public Combat() {
        if(joueur1.getNbPokemonsActifs() != Joueur.NB_POKEMON ||
           joueur2.getNbPokemonsActifs() != Joueur.NB_POKEMON 
        ){
            System.out.println("Au moins une équipe est incomplète, combat impossible.");

        }

    }
 
    public Joueur victoire() {
        return null; 
    }
 
    public int sauvegarder() {
        return 0; 
    }
 
    public void charger(int numeroSauvegarde) {

        
    }
}