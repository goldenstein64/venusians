module venusians {
  requires transitive javafx.controls;
  requires javafx.fxml;

  opens venusians to javafx.fxml;
  exports venusians ;
}
