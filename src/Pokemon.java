public class Pokemon {
    private static final int NB_ATTAQUES = 4;
    private String nom;
    private int pvMax;
    private int pv;
    private int attaque;
    private int defense;
    private int vitesse;
    private Type type1;
    private Type type2;
    private Attaque[] attaques;

    public Pokemon(String nom) {
        this.nom = nom;
        this.attaques = new Attaque[NB_ATTAQUES];
    }

    public Attaque getAttaqueByIndex(int index) {
        if (index < 0 || index >= NB_ATTAQUES) {
            return null;
        }
        return this.attaques[index];
    }

    public int attaquer(Attaque attaque, Pokemon adversaire) {
        if (attaque == null || adversaire == null) {
            return 0;
        }

        int degats = 10;
        return degats;
    }

    public int getPvMax() {
        return this.pvMax;
    }

    public String getNom() {
        return this.nom;
    }

    public void setPvMax(int pvMax) {
        this.pvMax = pvMax;
    }


    public void setPv(int pv) {
        this.pv = pv;
    }

    public void setAttaque(int attaque) {
        this.attaque = attaque;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }

    public void setType1(Type type1) {
        this.type1 = type1;
    }
    
    public void setType2(Type type2) {
        this.type2 = type2;
    }
}
