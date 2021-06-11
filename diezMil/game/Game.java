package diezMil.game;



/**
 * <h1>Partida de 10000</h1>
 * 
 * <p>El <b>objetivo</b> del juego es llegar a los 10000 puntos exactos.</p>
 * 
 * <p>Los jugadores por turnos tiran seis dados.</p>
 * <p>Si les quedan dados que no les han dado puntos pueden volver a tirarlos
 * hasta que no les queden dados, pero si en una tirada no obtienen puntos
 * pierden todos los puntos obtenidos en esa ronda.</p>
 * 
 * <p><b>Puntuación:</b></p>
 * <pre>
 * 1 = 100 puntos
 * 5 = 50 puntos
 * 
 * 3 x 1 = 1000   | 4 x 1 = 2000 | 5 x 1 = 4000 | 6 x 1 = 8000
 * 3 x 2 = 200    | 4 x 2 = 400  | 5 x 2 = 800  | 6 x 2 = 1600
 * 3 x 3 = 300    | 4 x 3 = 600  | 5 x 3 = 1200 | 6 x 3 = 2400
 * 3 x 4 = 400    | 4 x 4 = 800  | 5 x 4 = 1600 | 6 x 4 = 3200
 * 3 x 5 = 500    | 4 x 5 = 1000 | 5 x 5 = 2000 | 6 x 5 = 4000
 * 3 x 6 = 600    | 4 x 6 = 1200 | 5 x 6 = 2400 | 6 x 6 = 4800
 * 
 * Escalera (1 + 2 + 3 + 4 + 5 + 6) = 1500
 * </pre>
 * 
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 *
 */

import java.util.ArrayList;
import com.google.gson.Gson;

public class Game {
  private int round = 1;
  private ArrayList<Player> players = new ArrayList<Player>();
  private ArrayList<Player> losers = new ArrayList<Player>();
  private DiceCup diceCup = new DiceCup();

  public Game(ArrayList<Player> p) {
    this.players.addAll(p);
    this.players.get(0).getRoundPoints().add(round - 1, 0);
  }


  /**
   * Devuelve si la partida ha terminado porque
   * todos los jugadores han perdido.
   */
  public boolean isGameOver() {
    return this.players.isEmpty();
  }

  public final int getRound() {
    return round;
  }

  public final void setRound(int round) {
    this.round = round;
  }

  public final ArrayList<Player> getPlayers() {
    return players;
  }

  public final void setPlayers(ArrayList<Player> players) {
    this.players = players;
  }

  public final ArrayList<Player> getLosers() {
    return losers;
  }

  public final void setLosers(ArrayList<Player> losers) {
    this.losers = losers;
  }

  public final DiceCup getDiceCup() {
    return diceCup;
  }

  public final void setDiceCup(DiceCup diceCup) {
    this.diceCup = diceCup;
  }
  
  public void borrarPuntos() {
    for (Player player : players) {
      player.getRoundPoints().clear();
    }
    players.get(0).getRoundPoints().add(0);
  }
}
