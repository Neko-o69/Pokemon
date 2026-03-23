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


    public Pokemon(String nom){
        this.nom = nom;
        this.attaques = new Attaque[NB_ATTAQUES];
        for(int i=0; i<this.NB_ATTAQUES;i++){
            this.attaques[i] = new Attaque(i, i, nom, type1, i); 
        }
    }

    public Attaque getAttaqueByIndex(int index){
        if(index > NB_ATTAQUES){
            return null;
        }
        return this.attaques[index];
    }
    
    public void attaquer(Attaque[] attaques){}

    public int getPvMax(){
        return this.pvMax;
    }

    public void setPvMax(int pvMax){
        this.pvMax = pvMax;
        
    }

}
