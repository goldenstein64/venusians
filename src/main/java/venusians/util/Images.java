package venusians.util;

import java.net.URISyntaxException;

import javafx.scene.image.Image;

public class Images {
	public static <T> Image load(T instance, String filename) {
		return load(instance.getClass(), filename);
	}
	
	public static <T> Image load(Class<T> aClass, String filename) {
		try {
			return new Image(aClass.getResource(filename).toURI().toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
