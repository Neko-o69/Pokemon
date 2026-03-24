import java.util.ArrayList;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        DatabaseManager db = new DatabaseManager();
        db.connect();

        ArrayList<PokemonEspece> liste = PokemonDAO.chargerPokemons(db);

        System.out.print("Nom joueur 1 : ");
        Joueur j1 = new Joueur(sc.nextLine());

        System.out.print("Nom joueur 2 : ");
        Joueur j2 = new Joueur(sc.nextLine());

        SelectionPokemon.choisirEquipe(j1, liste);
        SelectionPokemon.choisirEquipe(j2, liste);

        Combat.lancerCombat(j1, j2);

        db.disconnect();
    }
}