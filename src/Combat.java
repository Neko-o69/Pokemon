import java.util.Scanner;

public class Combat {

    public static void lancerCombat(Joueur j1, Joueur j2) {
        Scanner sc = new Scanner(System.in);

        while (!j1.aPerdu() && !j2.aPerdu()) {

            PokemonInstance p1 = j1.getPokemonActuel();
            PokemonInstance p2 = j2.getPokemonActuel();

            System.out.println("\n" + j1.getNom() + " : " + p1.getNom() + " (" + p1.getPv() + " PV)");
            System.out.println(j2.getNom() + " : " + p2.getNom() + " (" + p2.getPv() + " PV)");

            System.out.println("Choisis une attaque (1 = attaque simple)");
            sc.nextInt();

            attaquer(p1, p2);

            if (p2.estKO()) {
                System.out.println(p2.getNom() + " est KO !");
                j2.suivant();
                continue;
            }

            attaquer(p2, p1);

            if (p1.estKO()) {
                System.out.println(p1.getNom() + " est KO !");
                j1.suivant();
            }
        }

        if (j1.aPerdu()) {
            System.out.println(j2.getNom() + " gagne !");
        } else {
            System.out.println(j1.getNom() + " gagne !");
        }
    }

    public static void attaquer(PokemonInstance attaquant, PokemonInstance defenseur) {
        int degats = 10;

        defenseur.subirDegats(degats);

        System.out.println(attaquant.getNom() + " attaque !");
        System.out.println(defenseur.getNom() + " perd " + degats + " PV");
    }
}