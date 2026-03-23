class JoueurHumain extends Joueur {
    public int nbBadge = 0;
    public boolean[] badges = new boolean[8];
    public int nbVictoire = 0;
 
    public JoueurHumain(String nom) {
        super(nom);
    }
}
 