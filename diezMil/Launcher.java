package diezMil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main entry point of the application
 */
public class Launcher extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("views/menu.fxml"));

    Scene scene = new Scene(root);

    stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));

    stage.setTitle("10000!");
    stage.setScene(scene);
    stage.show();

    stage.setResizable(false);
  }
}
