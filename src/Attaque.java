public class Attaque {
    private String nom;
    private int puissance;
    private int precision;
    private int priorite;
    private Type type;

    
    
    public Attaque(int puissance, int precision, String nom, Type type, int priorite){
        this.puissance = puissance;
        this.puissance = precision;
        this.nom = nom;
        this.type = type;
        this.priorite = priorite;

    }
    public int getPuissance() { 
        return puissance; 
    }
    public String getNom() { 
        return nom; 
    }
    public int getPrecision() {
         return precision; 
        }
    public Type getType() { 
        return type; 
    }
    public int getPriorite() { 
        return priorite;
    }
}
    
