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
    private void bouclePrincipale() {
        this.combat.lancerCombat();
    }

    public void jouerPartie(){
        Boolean combatBool;

        do {
            combatBool = this.combat.demarrerCombat();
        }while(!combatBool);

        this.bouclePrincipale();    
    }
        
}