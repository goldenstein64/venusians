package venusians.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import venusians.data.board.IntPoint;

public class Matrix<T> implements Collection<T> {

  public static class MatrixIterator<T> implements Iterator<T> {

    private Iterator<ArrayList<T>> rowIterator;
    private Iterator<T> columnIterator;

    public MatrixIterator(Matrix<T> source) {
      this.rowIterator = source.data.iterator();
      if (rowIterator.hasNext()) {
        ArrayList<T> row = rowIterator.next();
        this.columnIterator = row.iterator();
      }
    }

    @Override
    public boolean hasNext() {
      while (!columnIterator.hasNext() && rowIterator.hasNext()) {
        ArrayList<T> row = rowIterator.next();
        columnIterator = row.iterator();
      }

      return rowIterator.hasNext();
    }

    @Override
    public T next() {
      while (!columnIterator.hasNext() && rowIterator.hasNext()) {
        ArrayList<T> row = rowIterator.next();
        columnIterator = row.iterator();
      }

      if (!columnIterator.hasNext() && !rowIterator.hasNext()) {
        throw new NoSuchElementException();
      }

      return columnIterator.next();
    }
  }

  public static class MatrixEntry<T> implements Entry<IntPoint, T> {

    private final Matrix<T> source;
    private final IntPoint key;

    public MatrixEntry(Matrix<T> source, IntPoint key) {
      this.source = source;
      this.key = key;
    }

    @Override
    public IntPoint getKey() {
      return key;
    }

    @Override
    public T getValue() {
      return source.get(key.x, key.y);
    }

    @Override
    public T setValue(T newValue) {
      T oldValue = getValue();
      source.put(key.x, key.y, newValue);
      return oldValue;
    }
  }

  private final ArrayList<ArrayList<T>> data;
  private final IntPoint boundsValue;

  public Matrix(IntPoint size) {
    this.data = new ArrayList<ArrayList<T>>();
    for (int y = 0; y < size.y; y++) {
      ArrayList<T> row = new ArrayList<T>();
      for (int x = 0; x < size.x; x++) {
        row.add(null);
      }
      data.add(row);
    }
    this.boundsValue = size;
  }

  public Matrix(int sizeX, int sizeY) {
    this(new IntPoint(sizeX, sizeY));
  }

  public T get(int x, int y) {
    throwIfPositionIsOutOfBounds(x, y);

    return data.get(y).get(x);
  }

  public T get(IntPoint position) {
    return get(position.x, position.y);
  }

  public void put(int x, int y, T value) {
    throwIfPositionIsOutOfBounds(x, y);

    data.get(y).set(x, value);
  }

  public void put(IntPoint position, T value) {
    put(position.x, position.y, value);
  }

  @Override
  public boolean add(T value) {
    throw new UnsupportedOperationException(
      "The Matrix class doesn't support adding values"
    );
  }

  @Override
  public boolean addAll(@SuppressWarnings("rawtypes") Collection collection) {
    throw new UnsupportedOperationException(
      "The Matrix class doesn't support adding values"
    );
  }

  @Override
  public boolean retainAll(
    @SuppressWarnings("rawtypes") Collection collection
  ) {
    boolean changed = false;
    for (ArrayList<T> row : data) {
      for (int i = 0; i < row.size(); i++) {
        T value = row.get(i);
        if (!collection.contains(value)) {
          row.set(i, null);
          changed = true;
        }
      }
    }

    return changed;
  }

  @Override
  public boolean removeAll(
    @SuppressWarnings("rawtypes") Collection collection
  ) {
    boolean changed = false;
    for (ArrayList<T> row : data) {
      for (int i = 0; i < row.size(); i++) {
        T value = row.get(i);
        if (collection.contains(value)) {
          row.set(i, null);
          changed = true;
        }
      }
    }

    return changed;
  }

  public boolean remove(int x, int y) {
    T oldValue = get(x, y);
    put(x, y, null);
    return oldValue != null;
  }

  public boolean remove(IntPoint position) {
    return remove(position.x, position.y);
  }

  @Override
  public boolean remove(Object testValue) {
    for (ArrayList<T> row : data) {
      for (int i = 0; i < row.size(); i++) {
        T value = row.get(i);
        if (testValue.equals(value)) {
          row.set(i, null);
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public boolean containsAll(
    @SuppressWarnings("rawtypes") Collection collection
  ) {
    for (T value : this) {
      if (!collection.contains(value)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean contains(Object testValue) {
    for (ArrayList<T> row : data) {
      if (row.contains(testValue)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void clear() {
    for (ArrayList<T> row : data) {
      for (int i = 0; i < row.size(); i++) {
        row.set(i, null);
      }
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new MatrixIterator<T>(this);
  }

  public HashSet<Entry<IntPoint, T>> entrySet() {
    HashSet<Entry<IntPoint, T>> result = new HashSet<>();
    for (int y = 0; y < data.size(); y++) {
      ArrayList<T> row = data.get(y);
      for (int x = 0; x < row.size(); x++) {
        T value = row.get(x);
        if (value != null) {
          result.add(new MatrixEntry<T>(this, new IntPoint(x, y)));
        }
      }
    }

    return result;
  }

  public ArrayList<T> flatten() {
    ArrayList<T> result = new ArrayList<>();
    for (ArrayList<T> row : data) {
      for (T value : row) {
        result.add(value);
      }
    }

    return result;
  }

  public @SuppressWarnings("unchecked") Object[] toArray(Object[] array) {
    return this.flatten().toArray(array);
  }

  public Object[] toArray() {
    return this.flatten().toArray();
  }

  public boolean isEmpty() {
    for (ArrayList<T> row : data) {
      for (T value : row) {
        if (value != null) {
          return false;
        }
      }
    }

    return true;
  }

  public IntPoint bounds() {
    return boundsValue;
  }

  public int size() {
    int result = 0;
    for (ArrayList<T> row : data) {
      for (T value : row) {
        if (value != null) {
          result++;
        }
      }
    }

    return result;
  }

  public boolean positionIsOutOfBounds(int x, int y) {
    return x < 0 || x >= boundsValue.x || y < 0 || y >= boundsValue.y;
  }

  public boolean positionIsOutOfBounds(IntPoint position) {
    return positionIsOutOfBounds(position.x, position.y);
  }

  private void throwIfPositionIsOutOfBounds(int x, int y) {
    if (positionIsOutOfBounds(x, y)) {
      throw new IndexOutOfBoundsException(
        String.format(
          "Expected index to be in range of ([0, %d], [0, %d]), got (%d, %d)",
          boundsValue.x - 1,
          boundsValue.y - 1,
          x,
          y
        )
      );
    }
  }
}
