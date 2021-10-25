package venusians.gui.main;

import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.buildable.Buildable;
import venusians.data.board.buildable.Building;
import venusians.data.board.buildable.City;
import venusians.data.board.buildable.Road;
import venusians.data.board.buildable.Settlement;
import venusians.data.board.tiles.MapSlot;
import venusians.data.players.Player;
import venusians.data.players.Players;

public class BuildModeController {
	private Point pressedMousePosition = null;
	private Point secondaryPressedMousePosition;
	
	private Point startingRoadPosition = null;
	private IntPoint startingHexRoadPosition = null;
  private boolean isCreatingRoad = false;
	private Rectangle currentRoadGraphic = null;

	private Pane mapPane;
	private BuildableRenderer buildableRenderer;
	private Player currentPlayer;

	private HashSet<Buildable> suggestedBuildables = new HashSet<>();
	private HashMap<City, Settlement> upgradeMap = new HashMap<>();
	private Building[][] suggestedBuildingMap;
	private HashSet<Road>[][] suggestedRoadMap;
	
	@SuppressWarnings("unchecked")
	public BuildModeController(Pane mapPane, BuildableRenderer buildableRenderer) {
		this.mapPane = mapPane;
		this.buildableRenderer = buildableRenderer;
		this.currentPlayer = Players.getCurrentPlayer();
		MapSlot[][] currentMap = Board.getMap();
		int length = currentMap.length;
		int width = currentMap[0].length;
		this.suggestedBuildingMap = new Building[length][width];
		this.suggestedRoadMap = new HashSet[length][width];
		this.suggestedBuildables = new HashSet<>();
	}

	public void applySuggestions() {
		for (Settlement oldSettlement : upgradeMap.values()) {
			Board.removeBuildable(oldSettlement);
		}

		for (Buildable buildable : suggestedBuildables) {
			Board.addBuildable(buildable);
		}

		buildableRenderer.updateGraphics(Board.getBuildables());
	}

	public void updatePreview() {
		HashSet<Buildable> previewedBuildables = new HashSet<Buildable>();
		for (Buildable buildable : Board.getBuildables()) {
			if (buildable instanceof Settlement && upgradeMap.containsValue(buildable))
				continue;

			previewedBuildables.add(buildable);
		}

		previewedBuildables.addAll(suggestedBuildables);

		buildableRenderer.updateGraphics(previewedBuildables);
	}

	public void onMousePressed(MouseEvent event) {
		Point mousePosition = new Point(event.getX(), event.getY()); 
		if (event.getButton() == MouseButton.PRIMARY) {
			pressedMousePosition = mousePosition;
		} else if (event.getButton() == MouseButton.SECONDARY) {
			secondaryPressedMousePosition = mousePosition;
		}
	}

	public void onMouseDragged(MouseEvent event) {
		if (event.getButton() != MouseButton.PRIMARY)
			return;
			
		Point newMousePosition = new Point(event.getX(), event.getY());
		Point hexMousePosition = HexTransform.toHexSpace(newMousePosition);
		IntPoint closestHexCorner = HexTransform.toHexCorner(hexMousePosition);
		
		if (!isCreatingRoad && pressedMousePosition.distanceSquaredFrom(newMousePosition) > 100) {
			isCreatingRoad = true;
			startingHexRoadPosition = closestHexCorner;
			startingRoadPosition = HexTransform.toGuiSpace(closestHexCorner);
			currentRoadGraphic = new Rectangle();
			currentRoadGraphic.setFill(currentPlayer.getColor());
			currentRoadGraphic.setHeight(10);
			mapPane.getChildren().add(currentRoadGraphic);
			resizeRoadGraphic(newMousePosition);
		} else if (isCreatingRoad) {
			resizeRoadGraphic(newMousePosition);
		}
	}

	public void onMouseReleased(MouseEvent event) {
		Point newMousePosition = new Point(event.getX(), event.getY());

		if (event.getButton() == MouseButton.PRIMARY) { // add mode
			trySuggestBuildable(newMousePosition);
		} else if (event.getButton() == MouseButton.SECONDARY) { // delete mode
			tryDeleteBuildable(newMousePosition);
		}
	}

	private void trySuggestBuildable(Point newMousePosition) {
		if (isCreatingRoad) {
			trySuggestRoad(newMousePosition);
		} else {
			trySuggestBuilding(newMousePosition);
		}
	}

	private void tryDeleteBuildable(Point newMousePosition) {
		Point pressedClosestCorner = HexTransform.toGuiCorner(secondaryPressedMousePosition);

		double distance = pressedClosestCorner.distanceSquaredFrom(newMousePosition);
		if (distance < 100) {
			tryDeleteBuilding(newMousePosition);
		} else {
			tryDeleteRoad(newMousePosition);
		}
	}

	private void trySuggestBuilding(Point newMousePosition) {
		IntPoint closestHexCorner = HexTransform.toHexCorner(HexTransform.toHexSpace(newMousePosition));
		Building realBuilding = Board.getBuildingAt(closestHexCorner);
		Building suggestedBuilding = getSuggestedBuildingAt(closestHexCorner);
		if (realBuilding != null && realBuilding.getOwner() == currentPlayer && realBuilding instanceof Settlement) {
			Settlement realSettlement = (Settlement) realBuilding;
			City suggestedCity = new City(currentPlayer, closestHexCorner);
			upgradeMap.put(suggestedCity, realSettlement);
			addSuggestedBuilding(suggestedCity);
		} else if (suggestedBuilding != null && suggestedBuilding instanceof Settlement) {
			City suggestedCity = new City(currentPlayer, closestHexCorner);
			removeSuggestedBuildingNoPreview(suggestedBuilding);
			addSuggestedBuilding(suggestedCity);
		} else if (suggestedBuilding == null && realBuilding == null) {
			Settlement suggestedSettlement = new Settlement(currentPlayer, closestHexCorner);
			addSuggestedBuilding(suggestedSettlement);
		}
	}

	private void trySuggestRoad(Point newMousePosition) {
		Point hexMousePosition = HexTransform.toHexSpace(newMousePosition);
		IntPoint closestHexCorner = HexTransform.toHexCorner(hexMousePosition);

		IntPoint delta = closestHexCorner.minus(startingHexRoadPosition);
		Point normalDelta = HexTransform.toNormalSpace(delta);
		double distanceSquared = normalDelta.magnitudeSquared();
		if (isCloseTo(distanceSquared, 1, 0.0001)) {
			resizeRoadGraphic(HexTransform.toGuiSpace(closestHexCorner));
			Road suggestedRoad = new Road(currentPlayer, startingHexRoadPosition, closestHexCorner);
			addSuggestedRoad(suggestedRoad);
		}
		mapPane.getChildren().remove(currentRoadGraphic);
		currentRoadGraphic = null;
		startingRoadPosition = null;
		startingHexRoadPosition = null;
		isCreatingRoad = false;
	}

	private void tryDeleteBuilding(Point newMousePosition) {
		Point hexMousePosition = HexTransform.toHexSpace(newMousePosition);
		IntPoint buildingPosition = HexTransform.toHexCorner(hexMousePosition);
		Building suggestedBuilding = getSuggestedBuildingAt(buildingPosition);
		if (suggestedBuilding != null) {

			Building realBuilding = Board.getBuildingAt(buildingPosition);
			if (suggestedBuilding instanceof City) {
				if (realBuilding != null) {
					upgradeMap.remove(suggestedBuilding);
					removeSuggestedBuilding(suggestedBuilding);
				} else {
					Settlement revertedSettlement = new Settlement(
						suggestedBuilding.getOwner(), 
						suggestedBuilding.getPosition()
					);
					removeSuggestedBuildingNoPreview(suggestedBuilding);
					addSuggestedBuilding(revertedSettlement);
				}
			} else if (suggestedBuilding instanceof Settlement) {
				removeSuggestedBuilding(suggestedBuilding);
			}
		}
	}

	private void tryDeleteRoad(Point newMousePosition) {
		Point hexMousePosition = HexTransform.toHexSpace(newMousePosition);
		IntPoint position1 = HexTransform.toHexCorner(hexMousePosition);

		Point delta = hexMousePosition.minus(position1);
		Point direction = delta.unit();

		Point position2Estimate = position1.plus(direction);

		IntPoint position2 = HexTransform.toHexCorner(position2Estimate);

		HashSet<Road> set1 = getSuggestedRoadsAt(position1);
		HashSet<Road> set2 = getSuggestedRoadsAt(position2);

		HashSet<Road> intersection = new HashSet<>(set1);
		intersection.retainAll(set2);

		if (intersection.size() > 1) {
			throw new RuntimeException("Two roads overlap the same position pair");
		} else if (intersection.size() < 1) {
			return;
		}

		Road selectedRoad = intersection.iterator().next();
		removeSuggestedRoad(selectedRoad);
	}

	private boolean isCloseTo(double distanceSquared, double target, double epsilon) {
		return Math.abs(distanceSquared - target) < epsilon;
	}
	
	private void resizeRoadGraphic(Point newMousePosition) {
		Point center = startingRoadPosition.plus(newMousePosition).over(2);
		Point delta = newMousePosition.minus(startingRoadPosition);
		double length = delta.magnitude();
		double angle = Math.atan2(delta.y, delta.x);
		currentRoadGraphic.setWidth(length);
		currentRoadGraphic.setLayoutX(center.x - length / 2);
		currentRoadGraphic.setLayoutY(center.y - 5.0);
		currentRoadGraphic.setRotate(Math.toDegrees(angle));
	}
	
	private void addSuggestedBuilding(Building building) {
		addSuggestedBuildingNoPreview(building);
		updatePreview();
	}

	private void addSuggestedBuildingNoPreview(Building building) {
		IntPoint position = building.getPosition();
		suggestedBuildingMap[position.x][position.y] = building;

		suggestedBuildables.add(building);
	}
	
	private void removeSuggestedBuilding(Building building) {
		removeSuggestedBuildingNoPreview(building);
		updatePreview();
	}

	private void removeSuggestedBuildingNoPreview(Building building) {
		IntPoint position = building.getPosition();
		suggestedBuildingMap[position.x][position.y] = null;

		suggestedBuildables.remove(building);
	}
	
	private Building getSuggestedBuildingAt(IntPoint position) {
		return suggestedBuildingMap[position.x][position.y];
	}
	
	private void addSuggestedRoad(Road road) {
		IntPoint position1 = road.getPosition1();
		IntPoint position2 = road.getPosition2();

		HashSet<Road> set1 = getSuggestedRoadsAt(position1);
		HashSet<Road> set2 = getSuggestedRoadsAt(position2);

		set1.add(road);
		set2.add(road);

		suggestedBuildables.add(road);
		updatePreview();
	}

	private void removeSuggestedRoad(Road road) {
		IntPoint position1 = road.getPosition1();
		IntPoint position2 = road.getPosition2();

		HashSet<Road> set1 = getSuggestedRoadsAt(position1);
		HashSet<Road> set2 = getSuggestedRoadsAt(position2);

		set1.remove(road);
		set2.remove(road);

		suggestedBuildables.remove(road);
		updatePreview();
	}

	private HashSet<Road> getSuggestedRoadsAt(IntPoint position) {
		HashSet<Road> result = suggestedRoadMap[position.x][position.y];
		if (result == null) {
			result = new HashSet<Road>();
			suggestedRoadMap[position.x][position.y] = result;
		}

		return result;
	}
}
