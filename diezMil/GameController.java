package diezMil;


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
  private GridPane gridCentre;

  @FXML
  private GridPane gridTotalPoints;

  @FXML
  private VBox derecha;

  @FXML
  private Button skipBtn;

  @FXML
  private ScrollPane scrollPaneCentre;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private GridPane grid;

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

    System.out.println("Ronda: " + game.getRound());
    System.out.println(getCurrentPlayer().getName());
    game.getDiceCup().printDice();
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
        System.out.println("Has ganadooo.");
        Platform.exit();
        System.exit(0);
      } else if (getCurrentPlayer().isLoser()) {
        Alert alert1 = new Alert(AlertType.INFORMATION);
        alert1.setTitle(null);
        alert1.setHeaderText("Oh no..." + getCurrentPlayer().getName() + ", has perdido.");
        alert1.setContentText(null);

        alert1.showAndWait();
        System.out.println("Has perdido.");
        nextPlayerDelete();
        if (game.getPlayers().isEmpty()) {
          // Dialogo --> ShowAndWait de que todos han perdido.
          Alert alert2 = new Alert(AlertType.INFORMATION);
          alert2.setTitle(null);
          alert2.setHeaderText("Todos los jugadores han perdido.");
          alert2.setContentText(null);

          alert2.showAndWait();
          System.out.println("Todos han perdido.");
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

  @FXML
  void skipClick(ActionEvent event) {
    nextPlayer();
    skipBtn.setVisible(false);
  }

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

  private void setImageDiceDefault() {
    for (int i = 0; i < diceImage.length; i++) {
      diceImage[i].setImage(new Image(getClass().getResourceAsStream("images/die_selected.png")));
    }
  }

  private void updateTable() {
    // create new constraints for columns and set their percentage
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setHgrow(Priority.NEVER);

    // create new constraints for row and set their percentage
    RowConstraints rowConstraints = new RowConstraints();
    rowConstraints.setVgrow(Priority.NEVER);


    // don't set preferred size or anything on gridpane

    grid.getRowConstraints().add(rowConstraints);
    grid.getColumnConstraints().add(columnConstraints);

    // suppose your scroll pane id is scrollPane
    scrollPane.setContent(grid);

    grid.getChildren().clear();
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
    for (int i = 0; i < grid.getRowConstraints().size(); i++) {
      grid.getRowConstraints().get(i).setMinHeight(30);
      grid.getRowConstraints().get(i).setPrefHeight(30);
      grid.getRowConstraints().get(i).setMaxHeight(30);
    }


  }

  private void updateTableCentre() {
    // create new constraints for columns and set their percentage
    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setHgrow(Priority.NEVER);
    columnConstraints.setPercentWidth(100.00);

    // create new constraints for row and set their percentage
    RowConstraints rowConstraints = new RowConstraints();
    rowConstraints.setVgrow(Priority.NEVER);

    columnConstraints.setPercentWidth(100.0);

    // don't set preferred size or anything on gridpane

    gridCentre.getRowConstraints().add(rowConstraints);
    gridCentre.getColumnConstraints().add(columnConstraints);

    // suppose your scroll pane id is scrollPane
    scrollPaneCentre.setContent(gridCentre);
    gridCentre.getChildren().clear();
    // gridCentre.setPrefHeight(65*getCurrentPlayer().getRoundPoints().size());
    // gridCentre.setMaxHeight(65*getCurrentPlayer().getRoundPoints().size());

    HBox hbox1 = new HBox(new Label("Ronda"));
    hbox1.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
    hbox1.setAlignment(Pos.CENTER);
    hbox1.setMaxHeight(30);
    hbox1.setMaxWidth(100);
    gridCentre.add(hbox1, 0, 0);
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Label label = new Label(game.getPlayers().get(i).getName());
      label.setStyle("-fx-text-fill: white;");
      HBox hbox = new HBox(label);
      hbox.setAlignment(Pos.CENTER);
      hbox.setStyle("-fx-background-color: #333333; -fx-background-radius: 20;");
      gridCentre.add(hbox, i + 1, 0);
    }

    // perdedores
    for (int i = 0; i < game.getLosers().size(); i++) {
      Label label = new Label(game.getLosers().get(i).getName());
      label.setStyle("-fx-text-fill: white;");
      HBox hbox = new HBox(label);
      hbox.setAlignment(Pos.CENTER);
      hbox.setStyle("-fx-background-color: #333333; -fx-background-radius: 20;");
      hbox.setOpacity(0.4);
      gridCentre.add(hbox, game.getPlayers().size() + i + 1, 0);

    }

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

    for (int i = 0; i < getCurrentPlayer().getRoundPoints().size(); i++) {
      HBox hbox = new HBox(new Label(i + 1 + ""));
      hbox.setAlignment(Pos.CENTER);
      gridCentre.add(hbox, 0, i + 1);
    }

    for (int i = 0; i < gridCentre.getRowConstraints().size(); i++) {
      gridCentre.getRowConstraints().get(i).setMinHeight(30);
      gridCentre.getRowConstraints().get(i).setPrefHeight(30);
      gridCentre.getRowConstraints().get(i).setMaxHeight(30);
    }
    gridTotalPoints.getChildren().clear();
    HBox hbox = new HBox(new Label("Total"));
    hbox.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
    hbox.setAlignment(Pos.CENTER);
    hbox.setMaxHeight(30);
    hbox.setMaxWidth(100);
    gridTotalPoints.add(hbox, 0, 0);

    for (int i = 0; i < game.getPlayers().size(); i++) {
      HBox hbox2 = new HBox(new Label(game.getPlayers().get(i).getTotalPoints() + ""));
      hbox2.setStyle("-fx-background-color: #F2C94C; -fx-background-radius: 20;");
      hbox2.setAlignment(Pos.CENTER);
      hbox2.setMaxHeight(30);
      hbox2.setMaxWidth(100);
      gridTotalPoints.add(hbox2, i + 1, 0);
    }

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

  private void showRoundPoints() {
    PlayerPoints.setText(getCurrentPlayer().getRoundPoints().get(game.getRound() - 1) + "");
    TotalPlayerPoints.setText(getCurrentPlayer().getTotalPoints() + "");
    updateTable();
    updateTableCentre();
  }

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

  private void nextPlayerDelete() {
    game.getLosers().add(getCurrentPlayer());
    game.getPlayers().remove(getCurrentPlayer());
    if (!game.getPlayers().isEmpty()) {
      rollDiceButton.setDisable(true);
      transitionToNextPlayer();
    }
  }

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

