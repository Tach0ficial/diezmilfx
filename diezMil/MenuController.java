package diezMil;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import diezMil.game.Player;
import javafx.event.ActionEvent; 

/**
 * Controls the GUI Menu
 */
public class MenuController {
  private TextField[] playerNames;

  @FXML
  private TextField namePlayerOne;

  @FXML
  private TextField namePlayerTwo;

  @FXML
  private TextField namePlayerThree;

  @FXML
  private TextField namePlayerFour;

  @FXML
  private TextField namePlayerFive;

  @FXML
  private TextField namePlayerSix;

  @FXML
  private Button playButton;

  @FXML
  public void initialize() {
    playerNames = new TextField[] {namePlayerOne, namePlayerTwo, namePlayerThree, namePlayerFour,
        namePlayerFive, namePlayerSix};

    playButton.disableProperty().bind(Bindings.isEmpty(namePlayerOne.textProperty()));

    namePlayerTwo.disableProperty().bind(Bindings.isEmpty(namePlayerOne.textProperty()));

    namePlayerThree.disableProperty().bind(Bindings.isEmpty(namePlayerOne.textProperty())
        .or(Bindings.isEmpty(namePlayerTwo.textProperty())));

    namePlayerFour.disableProperty()
        .bind(Bindings.isEmpty(namePlayerOne.textProperty())
            .or(Bindings.isEmpty(namePlayerTwo.textProperty()))
            .or(Bindings.isEmpty(namePlayerThree.textProperty())));

    namePlayerFive.disableProperty()
        .bind(Bindings.isEmpty(namePlayerOne.textProperty())
            .or(Bindings.isEmpty(namePlayerTwo.textProperty()))
            .or(Bindings.isEmpty(namePlayerThree.textProperty()))
            .or(Bindings.isEmpty(namePlayerFour.textProperty())));

    namePlayerSix.disableProperty()
        .bind(Bindings.isEmpty(namePlayerOne.textProperty())
            .or(Bindings.isEmpty(namePlayerTwo.textProperty()))
            .or(Bindings.isEmpty(namePlayerThree.textProperty()))
            .or(Bindings.isEmpty(namePlayerFour.textProperty()))
            .or(Bindings.isEmpty(namePlayerFive.textProperty())));
  }
  
  @FXML
  void callGameController(ActionEvent event) throws IOException {
    ArrayList<Player> players = setupPlayers();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/game.fxml"));

    Parent parent = fxmlLoader.load();
    GameController gameController = (GameController) fxmlLoader.getController();
    gameController.setGame(players);
    Scene scene = new Scene(parent);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    stage.setScene(scene);
    stage.show();
  }


  private ArrayList<Player> setupPlayers() {
    ArrayList<Player> players = new ArrayList<>();

    for (TextField nameField : playerNames) {
      if (!nameField.isDisabled() && !nameField.getText().isEmpty()) {
        players.add(new Player(nameField.getText()));
      }

    }
    return players;
  }

}
