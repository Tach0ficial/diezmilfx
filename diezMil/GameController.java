package diezMil;

/**
 * Controlador de la partida 
 * 
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 */

import javafx.util.Duration;
import java.util.ArrayList;
import diezMil.game.Game;
import diezMil.game.Player;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameController {

  private Game game;
  private int currentPlayer = 0;
  private boolean isLastPlayer = false;
  private ImageView[] diceImage = new ImageView[6];


  public void setGame(ArrayList<Player> players) {
    this.game = new Game(players);
    PlayerName.setText(game.getPlayers().get(0).getName());
    updatePlayer();
  }

  @FXML
  private Text PlayerName;

  @FXML
  private Text PlayerPoints;

  @FXML
  private Text TotalPlayerPoints;

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
  private GridPane gridCentre;

  @FXML
  private GridPane gridTotalPoints;

  @FXML
  private Button skipBtn;

  @FXML
  private ScrollPane scrollPaneCentre;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private GridPane grid;

  /**
   * Inicializamos el array de imágenes de los dados
   */
  @FXML
  private void initialize() {
    skipBtn.setVisible(false);

    diceImage[0] = die1;
    diceImage[1] = die2;
    diceImage[2] = die3;
    diceImage[3] = die4;
    diceImage[4] = die5;
    diceImage[5] = die6;
  }

  /**
   * Obtiene el jugador actual
   * 
   * @return player
   */
  private Player getCurrentPlayer() {
    return game.getPlayers().get(currentPlayer);
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

    skipBtn.setDisable(true);
    rollDiceButton.setDisable(true);
    trans.play();

    // System.out.println("Ronda: " + game.getRound());
    // System.out.println(getCurrentPlayer().getName());
    // game.getDiceCup().printDice();

    int countedPoints = game.getDiceCup().countPoints();
    trans.setOnFinished(event1 -> {
      if (countedPoints != 0) {
        rollDiceButton.setDisable(false);
        skipBtn.setDisable(false);
      }
    });

    currentPoints.setText(countedPoints + "");

    if (countedPoints == 0) {
      getCurrentPlayer().getRoundPoints().set(game.getRound() - 1, 0);
      showRoundPoints();
      PlayerPoints.setText("0");
      nextPlayer();
    } else {
      addPoints(countedPoints);
      showRoundPoints();
      if (getCurrentPlayer().isWinner()) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText("¡" + getCurrentPlayer().getName() + " has ganado!");
        alert.setContentText(null);

        alert.showAndWait();
        // System.out.println("Has ganadooo.");
        Platform.exit();
        System.exit(0);
      } else if (getCurrentPlayer().isLoser()) {
        Alert alert1 = new Alert(AlertType.INFORMATION);
        alert1.setTitle(null);
        alert1.setHeaderText("Oh no..." + getCurrentPlayer().getName() + ", has perdido.");
        alert1.setContentText(null);

        alert1.showAndWait();
        // System.out.println("Has perdido.");
        nextPlayerDelete();
        if (game.getPlayers().isEmpty()) {
          Alert alert2 = new Alert(AlertType.INFORMATION);
          alert2.setTitle(null);
          alert2.setHeaderText("Todos los jugadores han perdido.");
          alert2.setContentText(null);

          alert2.showAndWait();
          // System.out.println("Todos han perdido.");
          Platform.exit();
          System.exit(0);
        }
      } else if (game.getDiceCup().getDice().size() == game.getDiceCup().diceToRemove()) {
        nextPlayer();
      } else {
        skipBtn.setVisible(true);
      }
    }
  }

  /**
   * Cuando se hace click en el botón skip pasa al siguiente jugador y desaparece el botón
   * 
   * @param event
   */
  @FXML
  void skipClick(ActionEvent event) {
    nextPlayer();
    skipBtn.setVisible(false);
  }

  /**
   * Muestra o no la tabla de puntuaciones
   * 
   * @param event
   */
  @FXML
  void btnShowTableAction(ActionEvent event) {
    if (scrollPaneCentre.isVisible()) {
      gridTotalPoints.setVisible(false);
      scrollPaneCentre.setVisible(false);
      gridCentre.setVisible(false);
    } else {
      gridTotalPoints.setVisible(true);
      scrollPaneCentre.setVisible(true);
      gridCentre.setVisible(true);
    }
  }

  /**
   * Establece las imagenes de los dados por defecto
   */
  private void setImageDiceDefault() {
    for (int i = 0; i < diceImage.length; i++) {
      diceImage[i].setImage(new Image(getClass().getResourceAsStream("images/die_selected.png")));
    }
  }

  /**
   * Actualiza la tabla de puntos de la izquierda
   */
  private void updateTable() {
    // Restricciones del grid
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setHgrow(Priority.NEVER);

    RowConstraints rowConstraints = new RowConstraints();
    rowConstraints.setVgrow(Priority.NEVER);

    grid.getRowConstraints().add(rowConstraints);
    grid.getColumnConstraints().add(columnConstraints);

    scrollPane.setContent(grid);

    grid.getChildren().clear();

    // Pone las rondas y los puntos en la tabla de la izquierda
    int round = 1;
    for (int point : getCurrentPlayer().getRoundPoints()) {
      HBox HBoxRound = new HBox(new Label(round + ""));
      HBox HBoxPoints = new HBox(new Label(point + ""));
      HBoxRound.setStyle("-fx-background-color: #C4C4C4; -fx-background-radius: 20; ");
      HBoxRound.setAlignment(Pos.CENTER);
      HBoxRound.setMaxHeight(30);
      HBoxPoints.setStyle("-fx-background-color: #C4C4C4; -fx-background-radius: 20;");
      HBoxPoints.setAlignment(Pos.CENTER);
      HBoxPoints.setMaxHeight(30);
      HBoxPoints.setMaxWidth(100);
      grid.addRow(round - 1, HBoxRound, new Label(""), HBoxPoints);
      round++;
    }

    // Pone la altura de las filas al mismo tamaño
    for (int i = 0; i < grid.getRowConstraints().size(); i++) {
      grid.getRowConstraints().get(i).setMinHeight(30);
      grid.getRowConstraints().get(i).setPrefHeight(30);
      grid.getRowConstraints().get(i).setMaxHeight(30);
    }
  }

  /**
   * Actualiza la tabla de puntuaciones totales
   */
  private void updateTableCentre() {

    // Restricciones del grid
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setHgrow(Priority.NEVER);
    columnConstraints.setPercentWidth(100.00);

    RowConstraints rowConstraints = new RowConstraints();
    rowConstraints.setVgrow(Priority.NEVER);

    gridCentre.getRowConstraints().add(rowConstraints);
    gridCentre.getColumnConstraints().add(columnConstraints);

    scrollPaneCentre.setContent(gridCentre);
    gridCentre.getChildren().clear();

    // Poner "Ronda" en la posicion 0,0 del grid
    HBox hbox1 = new HBox(new Label("Ronda"));
    hbox1.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
    hbox1.setAlignment(Pos.CENTER);
    hbox1.setMaxHeight(30);
    hbox1.setMaxWidth(100);
    gridCentre.add(hbox1, 0, 0);

    // Nombre de los jugadores
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Label label = new Label(game.getPlayers().get(i).getName());
      label.setStyle("-fx-text-fill: white;");
      HBox hbox = new HBox(label);
      hbox.setAlignment(Pos.CENTER);
      hbox.setStyle("-fx-background-color: #333333; -fx-background-radius: 20;");
      gridCentre.add(hbox, i + 1, 0);
    }

    // Nombre de los perdedores
    for (int i = 0; i < game.getLosers().size(); i++) {
      Label label = new Label(game.getLosers().get(i).getName());
      label.setStyle("-fx-text-fill: white;");
      HBox hbox = new HBox(label);
      hbox.setAlignment(Pos.CENTER);
      hbox.setStyle("-fx-background-color: #333333; -fx-background-radius: 20;");
      hbox.setOpacity(0.4);
      gridCentre.add(hbox, game.getPlayers().size() + i + 1, 0);

    }

    // Puntos de los jugadores
    for (int i = 0; i < game.getPlayers().size(); i++) {
      for (int j = 0; j < game.getPlayers().get(i).getRoundPoints().size(); j++) {
        HBox hbox = new HBox(new Label(game.getPlayers().get(i).getRoundPoints().get(j) + ""));
        hbox.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
        hbox.setAlignment(Pos.CENTER);
        hbox.setMaxHeight(30);
        hbox.setMaxWidth(100);
        gridCentre.add(hbox, i + 1, j + 1);
      }
    }

    // Puntos de los perdedores
    for (int i = 0; i < game.getLosers().size(); i++) {
      for (int j = 0; j < game.getLosers().get(i).getRoundPoints().size(); j++) {
        HBox hbox = new HBox(new Label(game.getLosers().get(i).getRoundPoints().get(j) + ""));
        hbox.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
        hbox.setAlignment(Pos.CENTER);
        hbox.setMaxHeight(30);
        hbox.setMaxWidth(100);
        hbox.setOpacity(0.4);
        gridCentre.add(hbox, game.getPlayers().size() + i + 1, j + 1);
      }
    }

    // Numero de la ronda
    for (int i = 0; i < getCurrentPlayer().getRoundPoints().size(); i++) {
      HBox hbox = new HBox(new Label(i + 1 + ""));
      hbox.setAlignment(Pos.CENTER);
      gridCentre.add(hbox, 0, i + 1);
    }

    // Pone la altura de las filas al mismo tamaño
    for (int i = 0; i < gridCentre.getRowConstraints().size(); i++) {
      gridCentre.getRowConstraints().get(i).setMinHeight(30);
      gridCentre.getRowConstraints().get(i).setPrefHeight(30);
      gridCentre.getRowConstraints().get(i).setMaxHeight(30);
    }

    gridTotalPoints.getChildren().clear();

    // Poner "Total" en la posicion 0,0 del grid
    HBox hbox = new HBox(new Label("Total"));
    hbox.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
    hbox.setAlignment(Pos.CENTER);
    hbox.setMaxHeight(30);
    hbox.setMaxWidth(100);
    gridTotalPoints.add(hbox, 0, 0);

    // Puntos totales de los jugadores
    for (int i = 0; i < game.getPlayers().size(); i++) {
      HBox hbox2 = new HBox(new Label(game.getPlayers().get(i).getTotalPoints() + ""));
      hbox2.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
      hbox2.setAlignment(Pos.CENTER);
      hbox2.setMaxHeight(30);
      hbox2.setMaxWidth(100);
      gridTotalPoints.add(hbox2, i + 1, 0);
    }

    // Puntos totales de los perdedores
    for (int i = 0; i < game.getLosers().size(); i++) {
      HBox hbox2 = new HBox(new Label(game.getLosers().get(i).getTotalPoints() + ""));
      hbox2.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
      hbox2.setAlignment(Pos.CENTER);
      hbox2.setMaxHeight(30);
      hbox2.setMaxWidth(100);
      hbox2.setOpacity(0.4);
      gridTotalPoints.add(hbox2, game.getPlayers().size() + i + 1, 0);
    }
  }

  /**
   * Muestra los puntos
   */
  private void showRoundPoints() {
    PlayerPoints.setText(getCurrentPlayer().getRoundPoints().get(game.getRound() - 1) + "");
    TotalPlayerPoints.setText(getCurrentPlayer().getTotalPoints() + "");
    updateTable();
    updateTableCentre();
  }

  /**
   * Pasa al siguiente jugador
   */
  private void nextPlayer() {
    if (isLastPlayer) {
      currentPlayer = 0;
      game.setRound(game.getRound() + 1);
    } else {
      currentPlayer++;
    }
    rollDiceButton.setDisable(true);
    transitionToNextPlayer();
  }

  /**
   * Pasa al siguiente jugador borrando al jugador que ha perdido
   */
  private void nextPlayerDelete() {
    game.getLosers().add(getCurrentPlayer());
    game.getPlayers().remove(getCurrentPlayer());
    if (!game.getPlayers().isEmpty()) {
      rollDiceButton.setDisable(true);
      transitionToNextPlayer();
    }
  }

  /**
   * Hace la pausa entre un jugador y otro
   */
  private void transitionToNextPlayer() {
    getCurrentPlayer().getRoundPoints().add(game.getRound() - 1, 0);
    ParallelTransition trans = new ParallelTransition();
    PauseTransition delay = new PauseTransition(Duration.seconds(3));
    skipBtn.setVisible(false);
    delay.setOnFinished(event -> updatePlayer());
    trans.getChildren().add(delay);
    trans.play();
    trans.setOnFinished(event -> rollDiceButton.setDisable(false));
  }

  /**
   * Actualiza los datos al jugador actual
   */
  private void updatePlayer() {
    setImageDiceDefault();
    currentPoints.clear();
    PlayerPoints.setText("0");
    TotalPlayerPoints.setText(getCurrentPlayer().getTotalPoints() + "");
    PlayerName.setText(getCurrentPlayer().getName());
    grid.getChildren().clear();
    updateTable();
    updateTableCentre();
  }

  /**
   * Añade los puntos al arraylist de puntos del jugador
   * 
   * @param countedPoints
   */
  private void addPoints(int countedPoints) {
    getCurrentPlayer().getRoundPoints().set(game.getRound() - 1,
        getCurrentPlayer().getRoundPoints().get(game.getRound() - 1) + countedPoints);
  }

  /**
   * Actualiza la imagen del dado según su valor
   * @param i
   */
  private void updateDiceImage(int i) {
    int value = game.getDiceCup().getDice().get(i).getValue();
    diceImage[i]
        .setImage(new Image(getClass().getResourceAsStream("images/die" + value + ".png")));

    diceImage[i].setDisable(false);
  }

}

