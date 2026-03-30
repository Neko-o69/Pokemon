public class Etat {
    private String nom;
    private int nbTour = 0;

    public Etat(String nom) {
        this.nom = nom;
    }

    public void effet() {
        System.out.println("Etat : " + nom);
    }
}