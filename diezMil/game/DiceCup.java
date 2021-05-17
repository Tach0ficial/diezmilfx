package diezMil.game;

/**
 * Class DiceCup
 * Representación de cubilete
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DiceCup {
  private ArrayList<Die> dice = new ArrayList<Die>();

  public DiceCup() {
    for (int i = 0; i < 6; i++) {
      this.dice.add(new Die());
    }
  }

  public ArrayList<Die> getDice() {
    return dice;
  }

  /**
   * Tira los dados que haya en el cubilete
   */
  public void throwDice() {
    for (Die die : dice) {
      die.throwDie();
    }
  }

  /**
   * Imprime los dados en consola
   */
  /*
  public void printDice() {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < dice.size(); j++) {
        String[] die;
        die = dice.get(j).drawDie();
        System.out.print(die[i]);
      }
      System.out.println();
    }
  }
  */
  
  /**
   * Rellena el cubilete con 6 dados
   */
  public void reset() {
    this.dice.clear();
    for (int i = 0; i < 6; i++) {
      this.dice.add(new Die());
    }
  }

  /**
   * Cuenta los puntos de una tirada y borra los dados que dan puntos
   * 
   * @return points
   */
  public int countPoints() {
    /*
     * Este mapa lo utilizamos para relacionar la cantidad de dados de un mismo valor con el
     * multiplicador de puntos, de manera que: cantidad: 3, 4, 5, 6 multiplicador: 1, 2, 4, 8 Para
     * el multiplicador: 2^0=1, 2^1=2, 2^2=4, 2^3=8
     */
    Map<Integer, Integer> multi = new HashMap<>();
    for (int j = 0; j < 4; j++) {
      multi.put(j + 3, (int) Math.pow(2, j));
    }

    int points = 0;
    if (this.dice.containsAll(new ArrayList<Die>(
        Arrays.asList(new Die(1), new Die(2), new Die(3), new Die(4), new Die(5), new Die(6))))) {
      return 1500;
    } else {
      for (int i = 1; i <= 6; i++) {
        int side = i;

        // cuenta los dados con el mismo valor
        int count = (int) this.dice.stream().filter(die -> die.getValue() == side).count();

        // cuenta los puntos
        if (side == 1) {
          if (count >= 3) {
            points += side * 1000 * multi.get(count);
          } else {
            points += count * 100;
          }
        } else if (count >= 3) {
          points += side * 100 * multi.get(count);
        } else if (side == 5) {
          points += count * 50;
        }

      }
    }
    return points;
  }

  /**
   * Borra los dados que puntúan del cubilete
   */
  public void removeDice() {

    if (this.dice.containsAll(new ArrayList<Die>(
        Arrays.asList(new Die(1), new Die(2), new Die(3), new Die(4), new Die(5), new Die(6))))) {
      this.dice.clear();
    } else {
      for (int i = 1; i <= 6; i++) {
        int side = i;

        // cuenta los dados con el mismo valor
        int count = (int) this.dice.stream().filter(die -> die.getValue() == side).count();

        // borra los dados que puntuan
        this.dice.removeIf(die -> die.getValue() == side
            && (count >= 3 || (die.getValue() == 1 || die.getValue() == 5)));
      }
    }
  }
  
  /**
   * Cuenta los dados que hay que borrar del cubilete
   * @return diceToRemove
   */
  public int diceToRemove() {
    int diceToRemove = 0;
    if (this.dice.containsAll(new ArrayList<Die>(
        Arrays.asList(new Die(1), new Die(2), new Die(3), new Die(4), new Die(5), new Die(6))))) {
      return 6;
    } else {
      for (int i = 1; i <= 6; i++) {
        int side = i;

        // cuenta los dados con el mismo valor
        int count = (int) this.dice.stream().filter(die -> die.getValue() == side).count();
        
        if (side == 1 || side == 5 || count >= 3) {
          diceToRemove += count;
        }    
      }
      return diceToRemove;
    }
  }
  

}
