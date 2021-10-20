package venusians.data.board.tiles;

import javafx.scene.image.Image;
import venusians.util.Images;

public enum AnyPort implements PortKind {
	INSTANCE;

	private Image portImage = Images.load(AnyPort.class, "anyPort.png");
	private int portNecessaryCount = 3;
	private int portRequestedCount = 1;

	public static AnyPort getInstance() {
		return INSTANCE;
	}
	
	public Image getPortImage() {
		return portImage;
	}

	public int getPortNecessaryCount() {
		return portNecessaryCount;
	}

	public int getPortRequestedCount() {
		return portRequestedCount;
	}
}
