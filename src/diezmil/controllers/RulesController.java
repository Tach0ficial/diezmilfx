package diezmil.controllers;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class RulesController {

    @FXML
    private WebView webView;

    @FXML
    private void initialize() {
      URL url = this.getClass().getResource("/diezmil/rules.html");
      webView.getEngine().load(url.toString());
    }
    
    @FXML
    void goBack(ActionEvent event) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/diezmil/views/menu.fxml"));

      Parent parent = fxmlLoader.load();
      Scene scene = new Scene(parent);

      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      stage.setScene(scene);
      stage.show();
    }
    
}

