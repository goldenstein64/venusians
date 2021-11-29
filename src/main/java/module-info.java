module venusians {
  requires transitive javafx.controls;
  requires javafx.fxml;

  opens venusians.gui to javafx.fxml;
  opens venusians.gui.main to javafx.fxml;
  opens venusians.gui.results to javafx.fxml;
  opens venusians.gui.setup to javafx.fxml;
  opens venusians.gui.main.artists.tradeRequest to javafx.fxml;
  opens venusians.gui.main.artists.tradeDraft to javafx.fxml;
  opens venusians.gui.main.artists.resourceChoice to javafx.fxml;
  opens venusians.gui.main.artists.resourceMultiChoice to javafx.fxml;
  opens venusians.gui.main.artists.developmentToolTip to javafx.fxml;

  opens venusians.data to javafx.base;
  opens venusians.data.lifecycle to javafx.base;
  opens venusians.data.cards.resource to javafx.base;
  opens venusians.data.board.tiles to javafx.base;

  exports venusians.gui ;
}
