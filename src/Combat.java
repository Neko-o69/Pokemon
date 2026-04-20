class Combat {
    public Joueur joueur1;
    public Joueur joueur2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;

    public Combat(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;

        if (joueur1.getNbPokemonsActifs() != Joueur.NB_POKEMON || joueur2.getNbPokemonsActifs() != Joueur.NB_POKEMON) {
            System.out.println("Au moins une équipe est incomplète, combat impossible.");
        }
    }

    public Boolean demarrerCombat() {
        pokemon1 = joueur1.getEquipePokemon()[0];
        pokemon2 = joueur2.getEquipePokemon()[0];

        if (!(pokemon1 instanceof Pokemon) || !(pokemon2 instanceof Pokemon)) {
            System.out.println("Pas de pokemon pour combattre");
            return false;
        }

        System.out.println(joueur1.getNom() + " choisi " + pokemon1.getNom());
        System.out.println(joueur2.getNom() + " choisi " + pokemon2.getNom());
        return true;
    }

    public void lancerCombat() {
        Attaque attaque1 = pokemon1.getAttaqueByIndex(0);
        Attaque attaque2 = pokemon2.getAttaqueByIndex(0);
        
        int degats1 = pokemon1.attaquer(attaque1, pokemon2);
        System.out.println(pokemon1.getNom() + " attaque (" + degats1 + " degats)");
        System.out.println(pokemon2.getNom() + " a " + pokemon2.getPv() + " PV");
    
        int degats2 = pokemon2.attaquer(attaque2, pokemon1);
        System.out.println(pokemon2.getNom() + " attaque (" + degats2 + " degats)");
        System.out.println(pokemon1.getNom() + " a " + pokemon1.getPv() + " PV");

        if (degats1 > degats2) {
            System.out.println("Le gagnant est " + joueur1.getNom());
        } else if (degats2 > degats1) {
            System.out.println("Le gagnant est " + joueur2.getNom());
        } else {
            System.out.println("Match nul");
        }
    }

    private Joueur victoire() {
        if (joueur1.getNbPokemonsActifs() == 0) {
            return joueur2;
        }
        return joueur1;
    }

    public Boolean finPartie() {
        if (joueur1.getNbPokemonsActifs() == 0 || joueur2.getNbPokemonsActifs() == 0) {
            System.out.println("Le gagnant est :" + this.victoire().getNom());
            return true;
        }
        return false;
    }

    public int sauvegarder() {
        return 0;
    }

    public void charger(int numeroSauvegarde) {

    }
}