package diezMil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import diezMil.game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class MenuController {

    File selectedFile = null;
  
    @FXML
    private Button playButton;

    @FXML
    void loadGame(ActionEvent event) throws IOException {
      Stage primaryStage = (Stage) playButton.getScene().getWindow();
      FileChooser fileChooser = new FileChooser();
      fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
      fileChooser.getExtensionFilters().addAll(new ExtensionFilter("10000 Files", "*.10000"));
      fileChooser.setTitle("Escoge el archivo .10000");
      selectedFile = fileChooser.showOpenDialog(primaryStage);
      
      Gson gson = new Gson();
      
      
      try(BufferedReader render = new BufferedReader(new FileReader(selectedFile))) {
        Game game = gson.fromJson(render, Game.class);
        System.out.println(game);

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/game.fxml"));

      Parent parent = fxmlLoader.load();
      GameController gameController = (GameController) fxmlLoader.getController();
      gameController.setGame(game);
      Scene scene = new Scene(parent);

      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      stage.setScene(scene);
      stage.show();
      } catch (IOException e1) {
        e1.printStackTrace();
      } 
    }
    

    @FXML
    void playNewGame(ActionEvent event) throws IOException {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/namesMenu.fxml"));

      Parent parent = fxmlLoader.load();
      Scene scene = new Scene(parent);

      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      stage.setScene(scene);
      stage.show();
    }

    @FXML
    void showRules(ActionEvent event) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/rules.fxml"));

      Parent parent = fxmlLoader.load();
      Scene scene = new Scene(parent);

      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      stage.setScene(scene);
      stage.show();
    }
}
