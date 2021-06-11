package diezMil;

/**
 * Controlador de la partida 
 * 
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 */

import javafx.util.Duration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.google.gson.Gson;
import diezMil.game.Game;
import diezMil.game.Player;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class GameController {

  private Game game;
  private ArrayList<Player> prePlayers = new ArrayList<Player>();;
  private int currentPlayer = 0;
  private boolean isLastPlayer = false;
  private ImageView[] diceImage;

  @SuppressWarnings("exports")
  public void setGame(ArrayList<Player> players) {
    this.prePlayers = players;
    this.game = new Game(players);
    playerName.setText(game.getPlayers().get(0).getName());
    updatePlayer();
  }
  
  @SuppressWarnings("exports")
  public void setGame( Game game) {
    this.prePlayers.addAll(game.getPlayers());
    this.prePlayers.addAll(game.getLosers());
    System.out.println(prePlayers);
    this.game = game;
    playerName.setText(game.getPlayers().get(0).getName());
    updatePlayer();
  }

  @FXML
  private Text playerName;

  @FXML
  private Text playerPoints;

  @FXML
  private Text totalPlayerPoints;

  @FXML
  private Button showTableButton;

  @FXML
  private Button muteButton;

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
  private Button skipButton;

  @FXML
  private ScrollPane scrollPaneCentre;

  @FXML
  private ScrollPane scrollPaneLeft;

  @FXML
  private GridPane gridLeft;
  
  @FXML
  private GridPane gridNames;
  
  @FXML
  private MenuButton options;

  @FXML
  private CheckMenuItem saveHTML;

  /**
   * Inicializamos el array de imágenes de los dados
   */
  @FXML
  private void initialize() {
    skipButton.setVisible(false);
    diceImage = new ImageView[] {die1, die2, die3, die4, die5, die6};
  }

  @FXML
  private void rollDiceClick(ActionEvent event) {

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

    var trans = new ParallelTransition();

    for (int i = 0; i < game.getDiceCup().getDice().size(); i++) {
      diceImage[i]
          .setImage(new Image(getClass().getResourceAsStream("images/die_roll_animation.gif")));
      diceImage[i].setDisable(true);

      double pause = 0.5 + Math.random();
      int diceIndex = i;

      var delay = new PauseTransition(Duration.seconds(pause));
      delay.setOnFinished(event1 -> updateDiceImage(diceIndex));
      trans.getChildren().add(delay);
    }

    skipButton.setDisable(true);
    rollDiceButton.setDisable(true);
    trans.play();

    int countedPoints = game.getDiceCup().countPoints();
    trans.setOnFinished(event1 -> {
      if (countedPoints != 0 && game.getDiceCup().getDice().size() != game.getDiceCup().diceToRemove()) {
        rollDiceButton.setDisable(false);
        skipButton.setDisable(false);
      }
    });

    currentPoints.setText(countedPoints + "");

    if (countedPoints == 0) {
      getCurrentPlayer().getRoundPoints().set(game.getRound() - 1, 0);
      showRoundPoints();
      playerPoints.setText("0");
      nextPlayer();
    } else {
      addPoints(countedPoints);
      showRoundPoints();
      if (getCurrentPlayer().isWinner()) {
        showAlert("¡" + getCurrentPlayer().getName() + " has ganado!");
        saveScoreboard();
        Platform.exit();
        System.exit(0);
      } else if (getCurrentPlayer().isLoser()) {
        showAlert("Oh no..." + getCurrentPlayer().getName() + ", has perdido.");
        nextPlayerDelete();
        if (game.getPlayers().isEmpty()) {
          showAlert("Todos los jugadores han perdido.");
          saveScoreboard();
          Platform.exit();
          System.exit(0);
        }
      } else if (game.getDiceCup().getDice().size() == game.getDiceCup().diceToRemove()) {
        nextPlayer();
      } else {
        skipButton.setVisible(true);
      }
    }
  }

  /**
   * Cuando se hace click en el botón skip pasa al siguiente jugador y desaparece el botón
   * 
   * @param event
   */
  @FXML
  private void skipButtonAction(ActionEvent event) {
    nextPlayer();
    skipButton.setVisible(false);
  }

  /**
   * Muestra/oculta o no la tabla de puntuaciones
   * @param event
   */
  @FXML
  private void showTableButtonAction(ActionEvent event) {
    if (scrollPaneCentre.isVisible()) {
      setTableVisible(false);
    } else {
      setTableVisible(true);
    }
  }
  
  @FXML
  void resetGame(ActionEvent event) throws IOException {
    this.game = new Game(this.prePlayers);
    this.game.borrarPuntos();
    currentPlayer = 0;
    isLastPlayer = false;
    skipButton.setVisible(false);
    playerName.setText(game.getPlayers().get(0).getName());
    updatePlayer();
    initialize();
    updateTableCentre();
    updateTableLeft();
    showRoundPoints();
    System.out.println(getCurrentPlayer().getTotalPoints());
  }
  
  @FXML
  void saveGame(ActionEvent event) {

    Stage stage = (Stage) playerName.getScene().getWindow();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("10000 Files", "*.10000"));
    fileChooser.setTitle("Escoge el archivo csv.");
    File selectedFile = fileChooser.showOpenDialog(stage);
    
    Gson gson = new Gson();

    try(BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
      gson.toJson(this.game, bw);
      bw.close();
      showAlert("La partida se a guardado satisfactoriamente.");
      Platform.exit();
      System.exit(0);
    } catch (IOException e1) {
      e1.printStackTrace();
    } 
  }

  /**
   * Muestra/oculta la tabla de puntuaciones
   * @param visible
   */
  private void setTableVisible(boolean visible) {
    gridNames.setVisible(visible);
    gridTotalPoints.setVisible(visible);
    scrollPaneCentre.setVisible(visible);
    gridCentre.setVisible(visible);
  }
  
  /**
   * Resetea la tabla indicada
   * @param grid
   * @param scroll
   */
  private void resetTable(GridPane grid, ScrollPane scroll) {
    var columnConstraints = new ColumnConstraints();
    columnConstraints.setHgrow(Priority.NEVER);

    var rowConstraints = new RowConstraints();
    rowConstraints.setVgrow(Priority.NEVER);

    grid.getRowConstraints().add(rowConstraints);
    grid.getColumnConstraints().add(columnConstraints);

    scroll.setContent(grid);

    grid.getChildren().clear();
  }
  
  /**
   * Crea un hbox con el estilo y la etiqueta indicados
   * @param style
   * @param label
   * @return hbox
   */
  private HBox createHbox(String style, Label label) {
    HBox hbox = new HBox(label);
    hbox.setStyle(style);
    hbox.setAlignment(Pos.CENTER);
    hbox.setMaxHeight(30);
    hbox.setMaxWidth(100);
    return hbox;
  }

  /**
   * Actualiza la tabla de puntos de la izquierda
   */
  private void updateTableLeft() {
    // Restricciones del grid
    resetTable(gridLeft, scrollPaneLeft);

    String style = "-fx-background-color: #C4C4C4; -fx-background-radius: 20; ";
    // Pone las rondas y los puntos en la tabla de la izquierda
    int round = 1;
    for (int point : getCurrentPlayer().getRoundPoints()) {
      Label labelRound = new Label(round + "");
      Label labelPoints = new Label(point + "");
      HBox hBoxRound = createHbox(style, labelRound);
      HBox hBoxPoints = createHbox(style, labelPoints);
      gridLeft.addRow(round - 1, hBoxRound, new Label(""), hBoxPoints);
      round++;
    }

    // Pone la altura de las filas al mismo tamaño
    for (int i = 0; i < game.getRound(); i++) {
      setRowConstraints(gridLeft,i);
    }
  }

  /**
   * @param i
   */
  public void setRowConstraints(GridPane grid,int i) {
    grid.getRowConstraints().get(i).setMinHeight(30);
    grid.getRowConstraints().get(i).setPrefHeight(30);
    grid.getRowConstraints().get(i).setMaxHeight(30);
  }

  /**
   * Actualiza la tabla de puntuaciones totales
   */
  private void updateTableCentre() {

    // Restricciones del grid
    resetTable(gridCentre, scrollPaneCentre);
    gridNames.getChildren().clear();
    
    String style1 = "-fx-background-color: #F2C94C; -fx-background-radius: 20;";
    String style2 = "-fx-background-color: #333333; -fx-background-radius: 20;";

    // Poner "Ronda" en la posicion 0,0 del grid
    Label label1 = new Label("Round");
    HBox hbox1 = createHbox(style1, label1);
    gridNames.add(hbox1, 0, 0);

    // Nombre de los jugadores
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Label label = new Label(game.getPlayers().get(i).getName());
      label.setStyle("-fx-text-fill: white;");
      HBox hbox = createHbox(style2, label);
      gridNames.add(hbox, i + 1, 0);
    }

    // Nombre de los perdedores
    for (int i = 0; i < game.getLosers().size(); i++) {
      Label label = new Label(game.getLosers().get(i).getName());
      label.setStyle("-fx-text-fill: white;");
      HBox hbox = createHbox(style2, label);
      hbox.setOpacity(0.4);
      gridNames.add(hbox, game.getPlayers().size() + i + 1, 0);

    }

    // Puntos de los jugadores
    for (int i = 0; i < game.getPlayers().size(); i++) {
      for (int j = 0; j < game.getPlayers().get(i).getRoundPoints().size(); j++) {
        Label label = new Label(game.getPlayers().get(i).getRoundPoints().get(j) + "");
        HBox hbox = createHbox(style1, label);
        gridCentre.add(hbox, i +1 , j );
      }
    }

    // Puntos de los perdedores
    for (int i = 0; i < game.getLosers().size(); i++) {
      for (int j = 0; j < game.getLosers().get(i).getRoundPoints().size(); j++) {
        Label label = new Label(game.getLosers().get(i).getRoundPoints().get(j) + "");
        HBox hbox = createHbox(style1, label);
        hbox.setOpacity(0.4);
        gridCentre.add(hbox, game.getPlayers().size() + i + 1 , j);
      }
    }

    // Numero de la ronda
    for (int i = 0; i < getCurrentPlayer().getRoundPoints().size(); i++) {
      HBox hbox = new HBox(new Label(i + 1 + ""));
      hbox.setAlignment(Pos.CENTER);
      gridCentre.add(hbox, 0, i );
    }

    // Pone la altura de las filas al mismo tamaño
    for (int i = 0; i <  game.getRound(); i++) {
      setRowConstraints(gridCentre,i);
    }

    gridTotalPoints.getChildren().clear();

    // Poner "Total" en la posicion 0,0 del grid
    Label label2 = new Label("Total");
    HBox hbox2 = createHbox(style1, label2);
    gridTotalPoints.add(hbox2, 0, 0);

    // Puntos totales de los jugadores
    for (int i = 0; i < game.getPlayers().size(); i++) {
      Label label = new Label(game.getPlayers().get(i).getTotalPoints() + "");
      HBox hbox = createHbox(style1, label);
      gridTotalPoints.add(hbox, i + 1, 0);
    }

    // Puntos totales de los perdedores
    for (int i = 0; i < game.getLosers().size(); i++) {
      Label label = new Label(game.getLosers().get(i).getTotalPoints() + "");
      HBox hbox = createHbox(style1, label);
      hbox.setOpacity(0.4);
      gridTotalPoints.add(hbox, game.getPlayers().size() + i + 1, 0);
    }
  }
  
  /**
   * Obtiene el jugador actual
   * 
   * @return player
   */
  private Player getCurrentPlayer() {
    return game.getPlayers().get(currentPlayer);
  }

  /**
   * Establece las imágenes de los dados por defecto
   */
  private void setImageDiceDefault() {
    for (int i = 0; i < diceImage.length; i++) {
      diceImage[i].setImage(new Image(getClass().getResourceAsStream("images/die_selected.png")));
    }
  }

  /**
   * Muestra los puntos
   */
  private void showRoundPoints() {
    playerPoints.setText(getCurrentPlayer().getRoundPoints().get(game.getRound() - 1) + "");
    totalPlayerPoints.setText(getCurrentPlayer().getTotalPoints() + "");
    updateTableLeft();
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
    if (isLastPlayer) {
      currentPlayer = 0;
      game.setRound(game.getRound() + 1);
    }
    
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
    var trans = new ParallelTransition();
    var delay = new PauseTransition(Duration.seconds(3));
    skipButton.setVisible(false);
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
    playerPoints.setText("0");
    totalPlayerPoints.setText(getCurrentPlayer().getTotalPoints() + "");
    playerName.setText(getCurrentPlayer().getName());
    gridLeft.getChildren().clear();
    updateTableLeft();
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

  /**
   * Crea y muestra una alerta con el contenido indicado
   * @param content
   * @param header
   * @param type
   */
  private void showAlert(String header) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setContentText(null);
    alert.setHeaderText(header);
    alert.showAndWait();
  }
  
  private void saveScoreboard() {
    if(saveHTML.isSelected()) {
      String scbName="Marcador"+LocalDate.now()+""+LocalDateTime.now().getHour()+"-"
          +LocalDateTime.now().getMinute()+"-"+LocalDateTime.now().getSecond()+".html";
          game.getPlayers().addAll(game.getLosers());
          Scoreboard scoreboard  = new Scoreboard(scbName, game.getPlayers().size(), game.getPlayers(), game.getRound());
          scoreboard.saveHTML();
          System.out.println("Gracias por jugar a 10000.");
          System.out.println("Creado por Carlos Hidalgo Risco y Laura Hidalgo Rivera.");
    }
  }
  
}

