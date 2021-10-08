module venusians {
  requires transitive javafx.controls;
  requires javafx.fxml;

  opens venusians.gui to javafx.fxml;
  opens venusians.gui.main to javafx.fxml;
  opens venusians.gui.results to javafx.fxml;
  opens venusians.gui.setup to javafx.fxml;

  opens venusians.data.lifecycle to javafx.base;
  opens venusians.data.cards.resource to javafx.base;
  opens venusians.data.board.tiles to javafx.base;

  exports venusians.gui ;
}
