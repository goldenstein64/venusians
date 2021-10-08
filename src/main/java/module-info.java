module venusians {
  requires transitive javafx.controls;
  requires javafx.fxml;

  opens venusians to javafx.fxml;
  opens venusians.data.lifecycle to javafx.base;
  opens venusians.data.cards.resource to javafx.base;
  opens venusians.data.board.tiles to javafx.base;

  exports venusians ;
}
