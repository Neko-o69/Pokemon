public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private Combat combat;
    private Pokemon pokemon1;
    private Pokemon pokemon2;

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.combat = new Combat(joueur1, joueur2);
    }

    public void jouerPartie(){
        Boolean combatBool;

        do {
            combatBool = this.combat.demarrerCombat();
        }while(!combatBool);

        this.bouclePrincipale();    
    }
        
    private void bouclePrincipale() {

        Attaque attaque1 = pokemon1.getAttaqueByIndex(0);
        Attaque attaque2 = pokemon2.getAttaqueByIndex(0);
    
        int degats1 = pokemon1.attaquer(attaque1, pokemon2);
        System.out.println(pokemon1.getNom() + " attaque");
    
        int degats2 = pokemon2.attaquer(attaque2, pokemon1);
        System.out.println(pokemon2.getNom() + " attaque");
    
        if (degats1 > degats2) {
            System.out.println("Le gagnant est " + joueur1.getNom());
        } else if (degats2 > degats1) {
            System.out.println("Le gagnant est " + joueur2.getNom());
        } else {
            System.out.println("Match nul");
        }
    }
          
}