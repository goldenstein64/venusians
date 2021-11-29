package venusians.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Contains code used for creating ChangeListeners that don't use the ObservableValue
 */
public class ChangeListenerBuilder {

  /**
   * The functional interface for change listeners with two arguments
   */
  @FunctionalInterface
  public static interface SimplePairedChangeListener<T> {
    public abstract void changed(T oldValue, T newValue);
  }

  /**
   * Creates a ChangeListener<T> from the provided function with two arguments
   * @param <T> The type of ChangeListener
   * @param changeListener The function to create the ChangeListener from
   * @return The wrapped ChangeListener<T>
   */
  public static <T> ChangeListener<T> from(SimplePairedChangeListener<T> changeListener) {
    return new ChangeListener<T>() {
      public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
        changeListener.changed(oldValue, newValue);
      }
    };
  }
  
  /**
   * The functional interface for change listeners with one argument
   */
  @FunctionalInterface
  public static interface SimpleChangeListener<T> {
    public abstract void changed(T newValue);
  }

  /**
   * Creates a ChangeListener<T> from the provided function with one argument
   * @param <T> The type of ChangeListener
   * @param changeListener The function to create the ChangeListener from
   * @return The wrapped ChangeListener<T>
   */
  public static <T> ChangeListener<T> from(SimpleChangeListener<T> changeListener) {
    return new ChangeListener<T>() {
      public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
        changeListener.changed(newValue);
      }
    };
  }
  
  /**
   * The functional interface for change listeners with zero arguments
   */
  @FunctionalInterface
  public static interface SimplePureChangeListener<T> {
    public abstract void changed();
  }

  /**
   * Creates a ChangeListener<T> from the provided function with zero arguments
   * @param <T> The type of ChangeListener
   * @param changeListener The function to create the ChangeListener from
   * @return The wrapped ChangeListener<T>
   */
  public static <T> ChangeListener<T> from(SimplePureChangeListener<T> changeListener) {
    return new ChangeListener<T>() {
      public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
        changeListener.changed();
      }
    };
  }
}
