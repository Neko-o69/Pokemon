public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private Combat combat;

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    public void jouerPartie(){
        Boolean combatBool;

        do {
            combatBool = this.combat.demarrerCombat();
        }while(!combatBool);

        this.bouclePrincipale();


    }
}