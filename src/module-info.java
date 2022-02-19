module diezmil {
  requires transitive javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires javafx.media;
  requires com.google.gson;
  
  opens diezmil to javafx.fxml;
  opens diezmil.controllers to javafx.fxml;
  
  exports diezmil;
}
