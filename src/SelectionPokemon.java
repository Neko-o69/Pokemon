import java.util.ArrayList;
import java.util.Scanner;

public class SelectionPokemon {

    public static void choisirEquipe(Joueur joueur, ArrayList<PokemonEspece> liste) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choisis 6 Pokémon :");

        for (int i = 0; i < liste.size(); i++) {
            System.out.println(i + " - " + liste.get(i).getNom());
        }

        for (int i = 0; i < 6; i++) {
            System.out.print("Choix " + (i+1) + " : ");
            int choix = sc.nextInt();

            PokemonInstance p = new PokemonInstance(liste.get(choix), 10);
            joueur.ajouterPokemon(p);
        }
    }
}