package diezMil.game;

/**
 * Class Player
 * Representación de jugador
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 */

import java.util.ArrayList;

public class Player {
  private String name;
  private ArrayList<Integer> roundPoints = new ArrayList<Integer>();

  public Player(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

  public final ArrayList<Integer> getRoundPoints() {
    return roundPoints;
  }
  
  /**
   * Devuelve los puntos totales acumulados
   * @return total
   */
  public int getTotalPoints() {
    int total = 0;
    for (int i : this.roundPoints) {
      total += i;
    }
    return total;
  }

  /**
   * Devuelve si el jugador ha ganado
   */
  public boolean isWinner() {
    return this.getTotalPoints() == 10000;
  }
  
  /**
   * Devuelve si el jugador ha perdido
   */
  public boolean isLoser() {
    return this.getTotalPoints() > 10000;
  }

}
