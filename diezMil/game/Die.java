package diezMil.game;

/**
 * Class Die
 * Representación de dado tradicional
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 */

public class Die {
  private int value;
  private final int SIDES = 6;

  /**
   * Crea un dado con valor aleatorio
   */
  public Die() {
    this.throwDie();
  }

  /**
   * Crea un dado con el valor que se pasa como parámetro
   * @param value
   */
  public Die(int value) {
    this.value = value;
  }

  public final int getValue() {
    return value;
  }

  /**
   * Tira un dado (valor entre 1 y 6)
   */
  public void throwDie() {
    this.value = (int) (Math.random() * SIDES + 1);
  }

  /**
   * <b>NOTA</b>: Los dados no se visualizarán correctamente
   * si no se utiliza una fuente monoespaciada (Ej: Consolas, Courrier New)
   * Devuelve un array con el dibujo del dado
   * @return String[]
   */
  /*public String[] drawDie() {
    switch (this.value) {
      case 1:
        return new String[] {
          "╔═══════╗",
          "║       ║",
          "║   ■   ║",
          "║       ║",
          "╚═══════╝"
        };
      case 2:
        return new String[] {
          "╔═══════╗",
          "║     ■ ║",
          "║       ║",
          "║ ■     ║",
          "╚═══════╝"
        };
      case 3:
        return new String[] {
          "╔═══════╗",
          "║     ■ ║",
          "║   ■   ║",
          "║ ■     ║",
          "╚═══════╝"
        };
      case 4:
        return new String[] {
          "╔═══════╗",
          "║ ■   ■ ║",
          "║       ║",
          "║ ■   ■ ║",
          "╚═══════╝"
        };
      case 5:
        return new String[] {
          "╔═══════╗",
          "║ ■   ■ ║",
          "║   ■   ║",
          "║ ■   ■ ║",
          "╚═══════╝"
        };
      case 6:
        return new String[] {
          "╔═══════╗",
          "║ ■ ■ ■ ║",
          "║       ║",
          "║ ■ ■ ■ ║",
          "╚═══════╝"
        };
      default:
        return null;
    }
  }
  */

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + value;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Die other = (Die) obj;
    if (value != other.value)
      return false;
    return true;
  }

  
  
}
