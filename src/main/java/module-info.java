module venusians {
  requires transitive javafx.controls;
  requires javafx.fxml;

  opens venusians to javafx.fxml;
  opens venusians.data.lifecycle to javafx.base;

  exports venusians ;
}
