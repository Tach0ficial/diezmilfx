package diezMil.game;

/**
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 */

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    title();
    Game game = new Game(createPlayers());
    game.play();
  }

  public static void title() {
    System.out.println("   _     __      __      __      __     \r\n"
        + " /' \\  /'__`\\  /'__`\\  /'__`\\  /'__`\\   \r\n"
        + "/\\_, \\/\\ \\/\\ \\/\\ \\/\\ \\/\\ \\/\\ \\/\\ \\/\\ \\  \r\n"
        + "\\/_/\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \r\n"
        + "   \\ \\ \\ \\ \\_\\ \\ \\ \\_\\ \\ \\ \\_\\ \\ \\ \\_\\ \\\r\n"
        + "    \\ \\_\\ \\____/\\ \\____/\\ \\____/\\ \\____/\r\n"
        + "     \\/_/\\/___/  \\/___/  \\/___/  \\/___/ \n");
  }

  public static ArrayList<Player> createPlayers() {
    Scanner s = new Scanner(System.in);
    int numPlayers = 0;

    try {
      do {
        System.out.print("¿Cuántos jugadores? (1-6): ");
        numPlayers = s.nextInt();
        s.nextLine();
      } while (numPlayers > 6 || numPlayers < 1);
    } catch (InputMismatchException e) {
      System.out.println("Respuesta no válida. Por favor, introduce un número (1-6).");
    }

    ArrayList<Player> players = new ArrayList<Player>();

    for (int i = 0; i < numPlayers; i++) {
      String name;
      do {
        System.out.print("Nombre del jugador " + (i + 1) + ": ");
        name = s.nextLine();
      } while (name == "");
      players.add(new Player(name));
    }

    return players;

  }
}
