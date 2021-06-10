package diezMil;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button playButton;

    @FXML
    void loadGame(ActionEvent event) {

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
