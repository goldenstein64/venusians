package venusians.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ChangeListenerBuilder<T> {

  public static interface SimpleChangeListener<T> {
    public abstract void changed(T oldValue, T newValue);
  }

  public ChangeListener<T> from(SimpleChangeListener<T> changeListener) {
    return new ChangeListener<T>() {
      public void changed(
        ObservableValue<? extends T> value,
        T oldValue,
        T newValue
      ) {
        changeListener.changed(oldValue, newValue);
      }
    };
  }
}
