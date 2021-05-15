package diezMil;

import java.util.ArrayList;
import diezMil.game.Game;
import diezMil.game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

public class GameController {

  private Game game;
  private int currentPlayer = 0;
  private boolean isLastPlayer = false;

  public final void setGame(ArrayList<Player> players) {
    this.game = new Game(players);
    PlayerName.setText(game.getPlayers().get(0).getName());
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
  private Pane centro;

  @FXML
  private Button rollDiceButton;

  @FXML
  private TextField currentPoints;

  @FXML
  private TextArea visorDados;


  @FXML
  private ImageView derecha;

  @FXML
  private void initialize() {
    PlayerPoints.setText("0");
  }

  @FXML
  void rollDiceClick(ActionEvent event) throws InterruptedException {
    if (currentPlayer+1 == game.getPlayers().size()) {
      isLastPlayer = true;
    } else {
      isLastPlayer = false;
    }

    game.getDiceCup().throwDice();
    game.getDiceCup().printDice();
    int countedPoints = game.getDiceCup().countPoints();
    currentPoints.setText(countedPoints + "");
    if (countedPoints == 0) {
      getCurrentPlayer().getRoundPoints().set(game.getRound() - 1, 0);
      showRoundPoints();
      Thread.sleep(3000);
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
      Thread.sleep(3000);
      if (game.getDiceCup().getDice().isEmpty()) {
        nextPlayer();
      } else {
        
        /* TODO - Preguntar si quiere tirar de nuevo.
        if() {
          
        }else {
          nextPlayer();
        }
        */
      }
    }
    updateTable();
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
    currentPoints.clear();
    PlayerPoints.setText("0");
    PlayerName.setText(getCurrentPlayer().getName());
    game.getDiceCup().reset();
    getCurrentPlayer().getRoundPoints().add(game.getRound() - 1, 0);
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



}

