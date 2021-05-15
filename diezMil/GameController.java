package diezMil;


import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import diezMil.game.Game;
import diezMil.game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;

public class GameController {

  private Game game;
  private int currentPlayer = 0;
  private boolean isLastPlayer = false;
  private ImageView[] diceImage = new ImageView[6];

  public final void setGame(ArrayList<Player> players) {
    this.game = new Game(players);
    PlayerName.setText(game.getPlayers().get(0).getName());
    updatePlayer();
  }

  @FXML
  private Pane izquierda;

  @FXML
  private Text PlayerName;

  @FXML
  private Text PlayerPoints;

  @FXML
  private TableView<?> tableRounds;

  @FXML
  private TableColumn<?, ?> tableRoundsColunm;

  @FXML
  private TableColumn<?, ?> tablePointsColunm;

  @FXML
  private Text TotalPlayerPoints;

  @FXML
  private VBox centro;

  @FXML
  private Button btnShowTable;

  @FXML
  private Button btnMute;

  @FXML
  private ImageView die1;

  @FXML
  private ImageView die2;

  @FXML
  private ImageView die3;

  @FXML
  private ImageView die4;

  @FXML
  private ImageView die5;

  @FXML
  private ImageView die6;

  @FXML
  private TextField currentPoints;

  @FXML
  private Button rollDiceButton;

  @FXML
  private VBox derecha;

  @FXML
  private Button skipBtn;



  @FXML
  private void initialize() {

    diceImage[0] = die1;
    diceImage[1] = die2;
    diceImage[2] = die3;
    diceImage[3] = die4;
    diceImage[4] = die5;
    diceImage[5] = die6;
  }

  @FXML
  void rollDiceClick(ActionEvent event) throws InterruptedException {

    setImageDiceDefault();

    if (getCurrentPlayer().getRoundPoints().get(game.getRound() - 1) != 0) {
      game.getDiceCup().removeDice();
    } else {
      game.getDiceCup().reset();
    }

    if (currentPlayer + 1 == game.getPlayers().size()) {
      isLastPlayer = true;
    } else {
      isLastPlayer = false;
    }

    game.getDiceCup().throwDice();

    ParallelTransition trans = new ParallelTransition();

    for (int i = 0; i < game.getDiceCup().getDice().size(); i++) {
      diceImage[i]
          .setImage(new Image(getClass().getResourceAsStream("images/die_roll_animation.gif")));
      diceImage[i].setDisable(true);

      double pause = 0.5 + Math.random();
      int diceIndex = i;

      PauseTransition delay = new PauseTransition(Duration.seconds(pause));
      delay.setOnFinished(event1 -> updateDiceImage(diceIndex));
      trans.getChildren().add(delay);

    }

    trans.play();
    System.out.println("Ronda: " + game.getRound());
    System.out.println(getCurrentPlayer().getName());
    System.out.println("Puntos: " + getCurrentPlayer().getRoundPoints().get(game.getRound() - 1));
    game.getDiceCup().printDice();
    int countedPoints = game.getDiceCup().countPoints();
    currentPoints.setText(countedPoints + "");
    if (countedPoints == 0) {
      getCurrentPlayer().getRoundPoints().set(game.getRound() - 1, 0);
      showRoundPoints();
      // Thread.sleep(3000);
      nextPlayer();
    } else {
      addPoints(countedPoints);
      showRoundPoints();
      if (getCurrentPlayer().isWinner()) {
        // Dialogo --> ShowAndWait de que ha ganado.
        System.out.println("Has ganadooo.");
      } else if (getCurrentPlayer().isLoser()) {
        // Dialogo --> ShowAndWait de que ha perdido.
        System.out.println("Has perdido.");
        game.getLosers().add(getCurrentPlayer());
        game.getPlayers().remove(getCurrentPlayer());
        if (game.getPlayers().isEmpty()) {
          // Dialogo --> ShowAndWait de que todos han perdido.
          System.out.println("Todos han perdido.");
        }
      }

      if (game.getDiceCup().getDice().size() == game.getDiceCup().diceToRemove()) {
        // Thread.sleep(3000);
        nextPlayer();
      } else {

        /*
         * TODO - Preguntar si quiere tirar de nuevo. if() {
         * 
         * }else { nextPlayer(); }
         */
      }
    }
    updateTable();
  }

  private void setImageDiceDefault() {
    for (int i = 0; i < diceImage.length; i++) {
      diceImage[i].setImage(new Image(getClass().getResourceAsStream("images/die_selected.png")));
    }
  }

  private void updateTable() {
    // TODO Actualizar la tabla de la izquierda.

  }

  private void showRoundPoints() {
    PlayerPoints.setText(getCurrentPlayer().getRoundPoints().get(game.getRound() - 1) + "");
  }

  private void nextPlayer() {

    if (isLastPlayer) {
      currentPlayer = 0;
      game.setRound(game.getRound() + 1);
    } else {
      currentPlayer++;
    }
    getCurrentPlayer().getRoundPoints().add(game.getRound() - 1, 0);

    ParallelTransition trans = new ParallelTransition();

    PauseTransition delay = new PauseTransition(Duration.seconds(4));
    delay.setOnFinished(event1 -> updatePlayer());
    trans.getChildren().add(delay);
    trans.play();
  }

  private void updatePlayer() {
    setImageDiceDefault();
    currentPoints.clear();
    PlayerPoints.setText("0");
    PlayerName.setText(getCurrentPlayer().getName());
  }

  private Player getCurrentPlayer() {
    return game.getPlayers().get(currentPlayer);
  }

  private void addPoints(int countedPoints) {
    getCurrentPlayer().getRoundPoints().set(game.getRound() - 1,
        getCurrentPlayer().getRoundPoints().get(game.getRound() - 1) + countedPoints);
  }

  void setEverythingDefault() {
    PlayerPoints.setText("0");

  }

  private void updateDiceImage(int index) {
    int faceValue = game.getDiceCup().getDice().get(index).getValue();

    switch (faceValue) {
      case 1:
        diceImage[index].setImage(new Image(getClass().getResourceAsStream("images/die1.png")));
        break;
      case 2:
        diceImage[index].setImage(new Image(getClass().getResourceAsStream("images/die2.png")));
        break;
      case 3:
        diceImage[index].setImage(new Image(getClass().getResourceAsStream("images/die3.png")));
        break;
      case 4:
        diceImage[index].setImage(new Image(getClass().getResourceAsStream("images/die4.png")));
        break;
      case 5:
        diceImage[index].setImage(new Image(getClass().getResourceAsStream("images/die5.png")));
        break;
      case 6:
        diceImage[index].setImage(new Image(getClass().getResourceAsStream("images/die6.png")));
        break;
      default:
        diceImage[index].setImage(null); // should never occur
        break;
    }

    diceImage[index].setDisable(false);
  }


}

