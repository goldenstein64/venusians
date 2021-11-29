package venusians.gui.main.subControllers;

import java.util.HashMap;
import java.util.HashSet;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import venusians.data.board.AnchorChecker;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.buildable.Building;
import venusians.data.board.buildable.City;
import venusians.data.board.buildable.Road;
import venusians.data.board.buildable.Settlement;
import venusians.data.board.buildable.SuggestedBuildable;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;
import venusians.data.players.Players;
import venusians.gui.main.HexTransform;
import venusians.gui.main.MainGameController;
import venusians.gui.main.artists.RoadArtist;
import venusians.gui.main.updaters.BuildingPaneUpdater;
import venusians.gui.main.updaters.CardUpdater;
import venusians.gui.main.updaters.RoadPaneUpdater;
import venusians.util.Matrix;

public class BuildController {

  private Point pressedMousePosition;
  private Point secondaryPressedMousePosition;

  private Point startingRoadPosition;
  private IntPoint startingHexRoadPosition;
  private boolean isCreatingRoad = false;
  private Line currentRoadGraphic;

  private Player currentPlayer;
  private ResourceCardMap currentResourceHand;

  private Pane roadPane;
  private Pane mainViewPane;
  private HashSet<SuggestedBuildable<? extends Building>> suggestedBuildings = new HashSet<>();
  private HashSet<SuggestedBuildable<Road>> suggestedRoads = new HashSet<>();
  private HashMap<SuggestedBuildable<City>, Settlement> upgradeMap = new HashMap<>();
  private Matrix<SuggestedBuildable<? extends Building>> suggestedBuildingMap;
  private Matrix<HashSet<SuggestedBuildable<Road>>> suggestedRoadMap;

  private BuildingPaneUpdater buildingPaneUpdater;
  private RoadPaneUpdater roadPaneUpdater;
  private CardUpdater<ResourceCard> resourceCardUpdater;

  /**
   * Creates a new BuildModeController from its parentController.
   *
   * @param parentController The MainGameController this was instantiated from.
   */
  public BuildController(MainGameController parentController) {
    this.buildingPaneUpdater = parentController.getBuildingPaneUpdater();
    this.roadPaneUpdater = parentController.getRoadPaneUpdater();
    this.resourceCardUpdater = parentController.getResourceCardUpdater();

    this.roadPane = parentController.getRoadPane();
    this.mainViewPane = parentController.getMainViewPane();

    this.currentPlayer = Players.getCurrentPlayer();
    this.currentResourceHand = currentPlayer.getResourceHand();

    IntPoint mapSize = Board.getMapSize();
    this.suggestedBuildingMap =
      new Matrix<SuggestedBuildable<? extends Building>>(mapSize);
    this.suggestedRoadMap =
      new Matrix<HashSet<SuggestedBuildable<Road>>>(mapSize);
  }

  public void setCurrentPlayer(Player player) {
    this.currentPlayer = player;
    this.currentResourceHand = player.getResourceHand();
  }

  public void bindMouse() {
    mainViewPane.setOnMousePressed(this::onMousePressed);
    mainViewPane.setOnMouseDragged(this::onMouseDragged);
    mainViewPane.setOnMouseReleased(this::onMouseReleased);
  }

  public void unbindMouse() {
    mainViewPane.setOnMousePressed(null);
    mainViewPane.setOnMouseDragged(null);
    mainViewPane.setOnMouseReleased(null);
  }

  /**
   * Adds all suggestions made by this controller to the Board.
   */
  public void applySuggestions() {
    Board.removeBuildables(upgradeMap.values());

    HashSet<Building> realBuildings = toRealBuildings(suggestedBuildings);

    addVictoryPoints(realBuildings);

    Board.addBuildings(realBuildings);

    HashSet<Road> realRoads = toRealRoads(suggestedRoads);
    Board.addRoads(realRoads);

    buildingPaneUpdater.update(Board.getBuildings());
    roadPaneUpdater.update(Board.getRoads());
  }

  private void addVictoryPoints(HashSet<Building> addedBuildings) {
    int deltaPoints = -upgradeMap.size();
    for (Building building : addedBuildings) {
      if (building instanceof Settlement) {
        deltaPoints += 1;
      } else if (building instanceof City) {
        deltaPoints += 2;
      }
    }

    currentPlayer.addVictoryPoints(deltaPoints);
  }

  private HashSet<Building> toRealBuildings(
    HashSet<SuggestedBuildable<? extends Building>> suggestedSet
  ) {
    HashSet<Building> realSet = new HashSet<>();
    for (SuggestedBuildable<? extends Building> suggestedBuildable : suggestedSet) {
      realSet.add(suggestedBuildable.getBuildable());
    }

    return realSet;
  }

  private HashSet<Road> toRealRoads(
    HashSet<SuggestedBuildable<Road>> suggestedSet
  ) {
    HashSet<Road> realSet = new HashSet<>();
    for (SuggestedBuildable<Road> suggestedBuildable : suggestedSet) {
      realSet.add(suggestedBuildable.getBuildable());
    }

    return realSet;
  }

  /**
   * Cancels and removes all suggestions made by this controller.
   */
  public void discardSuggestions() {
    for (SuggestedBuildable<? extends Building> suggestedBuilding : suggestedBuildings) {
      Building building = suggestedBuilding.getBuildable();
      if (
        building instanceof City && upgradeMap.get(suggestedBuilding) == null
      ) {
        currentPlayer.getResourceHand().add(Settlement.BLUEPRINT);
      }
      currentPlayer.getResourceHand().add(building.getBlueprint());
    }

    for (int i = 0; i < suggestedRoads.size(); i++) {
      currentResourceHand.add(Road.BLUEPRINT);
    }

    buildingPaneUpdater.update(Board.getBuildings());
    roadPaneUpdater.update(Board.getRoads());
    updateCards();
  }

  /**
   * Updates the map so that it displays previewed buildings.
   */
  public void updatePreview() {
    HashSet<Building> previewedBuildings = new HashSet<>();
    for (Building building : Board.getBuildings()) {
      if (
        building instanceof Settlement && upgradeMap.containsValue(building)
      ) continue;

      previewedBuildings.add(building);
    }

    previewedBuildings.addAll(toRealBuildings(suggestedBuildings));

    HashSet<Road> previewedRoads = new HashSet<>(Board.getRoads());
    previewedRoads.addAll(toRealRoads(suggestedRoads));

    buildingPaneUpdater.update(previewedBuildings);
    roadPaneUpdater.update(previewedRoads);
  }

  /**
   * Describes what happens when a button on the mouse is pressed.
   *
   * @param event The information found from this event.
   */
  public void onMousePressed(MouseEvent event) {
    Point mousePosition = new Point(event.getX(), event.getY());

    if (event.getButton() == MouseButton.PRIMARY) {
      onPrimaryMousePressed(mousePosition);
    } else if (event.getButton() == MouseButton.SECONDARY) {
      onSecondaryMousePressed(mousePosition);
    }
  }

  private void onPrimaryMousePressed(Point mousePosition) {
    pressedMousePosition = mousePosition;
    Point hexMousePosition = HexTransform.guiToHexSpace(mousePosition);
    startingHexRoadPosition = HexTransform.hexToHexCorner(hexMousePosition);
    startingRoadPosition = HexTransform.hexToGuiSpace(startingHexRoadPosition);
  }

  private void onSecondaryMousePressed(Point mousePosition) {
    secondaryPressedMousePosition = mousePosition;
  }

  /**
   * Describes what happens when the mouse is dragged (the mouse is moved while a button is held down).
   *
   * @param event The information found from this event.
   */
  public void onMouseDragged(MouseEvent event) {
    if (event.getButton() != MouseButton.PRIMARY) return;

    Point newMousePosition = new Point(event.getX(), event.getY());
    Point pressedDelta = newMousePosition.minus(pressedMousePosition);

    if (canStartCreatingRoad(pressedDelta)) {
      startCreatingRoad(newMousePosition);
    } else if (isCreatingRoad) {
      RoadArtist.reRender(
        currentRoadGraphic,
        startingRoadPosition,
        newMousePosition
      );
    }
  }

  private void startCreatingRoad(Point newMousePosition) {
    isCreatingRoad = true;

    currentRoadGraphic =
      RoadArtist.render(
        startingRoadPosition,
        newMousePosition,
        currentPlayer.getColor()
      );
    roadPane.getChildren().add(currentRoadGraphic);
  }

  private boolean canStartCreatingRoad(Point delta) {
    return (
      !isCreatingRoad &&
      delta.magnitudeSquared() > 100 &&
      currentPlayer.hasResourcesForRoad() &&
      startingHexRoadPosition != null &&
      Board.positionIsNextToTile(startingHexRoadPosition) &&
      (
        Board.positionHasRoad(currentPlayer, startingHexRoadPosition) ||
        positionHasSuggestedRoad(startingHexRoadPosition)
      )
    );
  }

  /**
   * Describes what happens when a button on the mouse is released.
   *
   * @param event The information found from this event.
   */
  public void onMouseReleased(MouseEvent event) {
    Point newMousePosition = new Point(event.getX(), event.getY());

    if (event.getButton() == MouseButton.PRIMARY) { // add mode
      suggestBuildable(newMousePosition);
    } else if (event.getButton() == MouseButton.SECONDARY) { // delete mode
      removeBuildable(newMousePosition);
    }
  }

  private void suggestBuildable(Point newMousePosition) {
    if (isCreatingRoad) {
      suggestRoad(newMousePosition);
    } else {
      suggestBuilding(newMousePosition);
    }
  }

  private void removeBuildable(Point newMousePosition) {
    Point pressedCorner = HexTransform.guiToGuiCorner(
      secondaryPressedMousePosition
    );

    double distance = pressedCorner.distanceSquaredFrom(newMousePosition);
    if (distance < 100) {
      removeBuilding(newMousePosition);
    } else {
      removeRoad(newMousePosition);
    }
  }

  private void suggestBuilding(Point newMousePosition) {
    IntPoint hexCorner = HexTransform.guiToHexCorner(newMousePosition);

    Building realBuilding = Board.getBuildingAt(hexCorner);
    if (
      realBuilding != null &&
      realBuilding.getOwner() == currentPlayer &&
      realBuilding instanceof Settlement
    ) {
      Settlement realSettlement = (Settlement) realBuilding;
      upgradeExistingSettlement(realSettlement);
      return;
    }

    SuggestedBuildable<? extends Building> suggestedBuilding = getSuggestedBuildingAt(
      hexCorner
    );

    if (
      suggestedBuilding != null &&
      suggestedBuilding.getBuildable() instanceof Settlement
    ) {
      @SuppressWarnings("unchecked")
      SuggestedBuildable<Settlement> suggestedSettlement = (SuggestedBuildable<Settlement>) suggestedBuilding;
      upgradeSuggestedSettlement(suggestedSettlement);
    } else if (
      suggestedBuilding == null &&
      realBuilding == null &&
      Board.positionIsNextToTile(hexCorner) &&
      (
        Board.positionHasRoad(currentPlayer, hexCorner) ||
        positionHasSuggestedRoad(hexCorner)
      )
    ) {
      suggestSettlement(hexCorner);
    }
  }

  private void upgradeExistingSettlement(Settlement realSettlement) {
    if (!currentPlayer.hasResourcesForCity()) return;

    City city = new City(currentPlayer, realSettlement.getPosition());
    SuggestedBuildable<City> suggestedCity = new SuggestedBuildable<>(city);
    upgradeMap.put(suggestedCity, realSettlement);
    registerBuilding(suggestedCity);
  }

  private void upgradeSuggestedSettlement(
    SuggestedBuildable<Settlement> suggestedSettlement
  ) {
    if (!currentPlayer.hasResourcesForCity()) return;

    Settlement settlement = suggestedSettlement.getBuildable();

    City newCity = new City(currentPlayer, settlement.getPosition());
    SuggestedBuildable<City> suggestedCity = new SuggestedBuildable<>(newCity);
    unregisterBuildingNoPreview(suggestedSettlement);
    currentResourceHand.remove(Settlement.BLUEPRINT);
    registerBuilding(suggestedCity);
  }

  private void suggestSettlement(IntPoint position) {
    if (!currentPlayer.hasResourcesForSettlement()) return;

    Settlement settlement = new Settlement(currentPlayer, position);
    SuggestedBuildable<Settlement> suggestedSettlement = new SuggestedBuildable<>(
      settlement
    );
    registerBuilding(suggestedSettlement);
  }

  private void suggestRoad(Point newMousePosition) {
    Point hexMousePosition = HexTransform.guiToHexSpace(newMousePosition);
    IntPoint hexMouseCorner = HexTransform.hexToHexCorner(hexMousePosition);

    if (
      Board.positionIsNextToTile(startingHexRoadPosition) &&
      Board.positionIsNextToTile(hexMouseCorner) &&
      (
        positionHasSuggestedRoad(startingHexRoadPosition) ||
        positionHasSuggestedRoad(hexMouseCorner)
      ) &&
      getSuggestedRoadBetween(startingHexRoadPosition, hexMouseCorner) == null
    ) {
      Road road = new Road(
        currentPlayer,
        startingHexRoadPosition,
        hexMouseCorner
      );
      SuggestedBuildable<Road> suggestedRoad = new SuggestedBuildable<>(road);
      registerRoad(suggestedRoad);
      updateCards();
    }

    cleanUpRoadGraphic();
  }

  private void cleanUpRoadGraphic() {
    roadPane.getChildren().remove(currentRoadGraphic);
    currentRoadGraphic = null;
    startingRoadPosition = null;
    startingHexRoadPosition = null;
    isCreatingRoad = false;
  }

  private void removeBuilding(Point newMousePosition) {
    Point hexMousePosition = HexTransform.guiToHexSpace(newMousePosition);
    IntPoint buildingPosition = HexTransform.hexToHexCorner(hexMousePosition);
    SuggestedBuildable<? extends Building> suggestedBuilding = getSuggestedBuildingAt(
      buildingPosition
    );
    if (suggestedBuilding != null) {
      Building building = suggestedBuilding.getBuildable();
      if (building instanceof City) {
        @SuppressWarnings("unchecked")
        SuggestedBuildable<City> suggestedCity = (SuggestedBuildable<City>) suggestedBuilding;
        unregisterCity(suggestedCity);
      } else if (building instanceof Settlement) {
        @SuppressWarnings("unchecked")
        SuggestedBuildable<Settlement> suggestedSettlement = (SuggestedBuildable<Settlement>) suggestedBuilding;
        unregisterBuilding(suggestedSettlement);
      }
    }
  }

  private void unregisterCity(SuggestedBuildable<City> suggestedCity) {
    City realCity = suggestedCity.getBuildable();
    Building realBuilding = Board.getBuildingAt(realCity.getPosition());
    if (realBuilding != null) {
      upgradeMap.remove(suggestedCity);
      unregisterBuilding(suggestedCity);
    } else {
      Settlement revertedSettlement = new Settlement(
        realCity.getOwner(),
        realCity.getPosition()
      );
      unregisterBuildingNoPreview(suggestedCity);
      currentResourceHand.add(Settlement.BLUEPRINT);
      registerBuilding(new SuggestedBuildable<Settlement>(revertedSettlement));
    }
  }

  private void removeRoad(Point newMousePosition) {
    Point hexMousePosition = HexTransform.guiToHexSpace(newMousePosition);

    IntPoint position1 = HexTransform.hexToHexCorner(hexMousePosition);
    IntPoint position2 = HexTransform.hexEstimateHexCorner(
      position1,
      hexMousePosition
    );

    SuggestedBuildable<Road> selectedRoad = getSuggestedRoadBetween(
      position1,
      position2
    );

    if (selectedRoad != null && buildablesStayAnchoredWithout(selectedRoad)) {
      unregisterRoad(selectedRoad);
      updateCards();
    }
  }

  private SuggestedBuildable<Road> getSuggestedRoadBetween(
    IntPoint position1,
    IntPoint position2
  ) {
    HashSet<SuggestedBuildable<Road>> set1 = getSuggestedRoadsAt(position1);
    HashSet<SuggestedBuildable<Road>> set2 = getSuggestedRoadsAt(position2);

    HashSet<SuggestedBuildable<Road>> intersection = new HashSet<>(set1);
    intersection.retainAll(set2);

    int intersectionSize = intersection.size();
    if (intersectionSize > 1) {
      throw new RuntimeException("Two roads overlap the same position pair");
    } else if (intersectionSize < 1) {
      return null;
    }

    return intersection.iterator().next();
  }

  private boolean buildablesStayAnchoredWithout(
    SuggestedBuildable<Road> removingSuggestedRoad
  ) {
    Road removingRoad = removingSuggestedRoad.getBuildable();

    AnchorChecker anchorChecker = new AnchorChecker();
    anchorChecker.setOwner(currentPlayer);
    anchorChecker.setSuggestedBuildingMap(suggestedBuildingMap);
    anchorChecker.setSuggestedRoadMap(suggestedRoadMap);

    return anchorChecker.buildablesStayAnchoredWithout(removingRoad);
  }

  private void updateCards() {
    resourceCardUpdater.update(currentResourceHand.toList());
  }

  private void registerBuilding(
    SuggestedBuildable<? extends Building> suggestedBuilding
  ) {
    registerBuildingNoPreview(suggestedBuilding);
    updatePreview();
    updateCards();
  }

  private <T extends Building> void registerBuildingNoPreview(
    SuggestedBuildable<T> suggestedBuilding
  ) {
    Building building = suggestedBuilding.getBuildable();
    IntPoint position = building.getPosition();
    suggestedBuildingMap.put(position, suggestedBuilding);

    suggestedBuildings.add(suggestedBuilding);
    currentResourceHand.remove(building.getBlueprint());
  }

  private <T extends Building> void unregisterBuilding(
    SuggestedBuildable<T> suggestedBuilding
  ) {
    unregisterBuildingNoPreview(suggestedBuilding);
    updatePreview();
    updateCards();
  }

  private <T extends Building> void unregisterBuildingNoPreview(
    SuggestedBuildable<T> suggestedBuilding
  ) {
    Building building = suggestedBuilding.getBuildable();
    IntPoint position = building.getPosition();
    suggestedBuildingMap.remove(position);

    suggestedBuildings.remove(suggestedBuilding);
    currentResourceHand.add(building.getBlueprint());
  }

  private SuggestedBuildable<? extends Building> getSuggestedBuildingAt(
    IntPoint position
  ) {
    SuggestedBuildable<? extends Building> suggestedBuilding = suggestedBuildingMap.get(
      position
    );
    return suggestedBuilding;
  }

  private void registerRoad(SuggestedBuildable<Road> suggestedRoad) {
    Road road = suggestedRoad.getBuildable();

    IntPoint position1 = road.getPosition1();
    IntPoint position2 = road.getPosition2();

    HashSet<SuggestedBuildable<Road>> set1 = getSuggestedRoadsAt(position1);
    HashSet<SuggestedBuildable<Road>> set2 = getSuggestedRoadsAt(position2);

    set1.add(suggestedRoad);
    set2.add(suggestedRoad);

    suggestedRoads.add(suggestedRoad);
    currentResourceHand.remove(Road.BLUEPRINT);
    updatePreview();
  }

  private void unregisterRoad(SuggestedBuildable<Road> suggestedRoad) {
    Road road = suggestedRoad.getBuildable();

    IntPoint position1 = road.getPosition1();
    IntPoint position2 = road.getPosition2();

    HashSet<SuggestedBuildable<Road>> set1 = getSuggestedRoadsAt(position1);
    HashSet<SuggestedBuildable<Road>> set2 = getSuggestedRoadsAt(position2);

    set1.remove(suggestedRoad);
    set2.remove(suggestedRoad);

    suggestedRoads.remove(suggestedRoad);
    currentResourceHand.add(Road.BLUEPRINT);
    updatePreview();
  }

  private HashSet<SuggestedBuildable<Road>> getSuggestedRoadsAt(
    IntPoint position
  ) {
    HashSet<SuggestedBuildable<Road>> result = suggestedRoadMap.get(position);
    if (result == null) {
      result = new HashSet<SuggestedBuildable<Road>>();
      suggestedRoadMap.put(position, result);
    }

    return result;
  }

  private boolean positionHasSuggestedRoad(IntPoint position) {
    HashSet<SuggestedBuildable<Road>> roads = suggestedRoadMap.get(position);
    return (
      Board.positionHasRoad(currentPlayer, position) ||
      (roads != null && roads.size() > 0)
    );
  }
}
