package android.database;

import java.util.ArrayList;
import java.util.Iterator;

public class MatrixCursor
  extends AbstractCursor
{
  private final int columnCount;
  private final String[] columnNames;
  private Object[] data;
  private int rowCount = 0;
  
  public MatrixCursor(String[] paramArrayOfString)
  {
    this(paramArrayOfString, 16);
  }
  
  public MatrixCursor(String[] paramArrayOfString, int paramInt)
  {
    columnNames = paramArrayOfString;
    columnCount = paramArrayOfString.length;
    int i = paramInt;
    if (paramInt < 1) {
      i = 1;
    }
    data = new Object[columnCount * i];
  }
  
  private void addRow(ArrayList<?> paramArrayList, int paramInt)
  {
    int i = paramArrayList.size();
    if (i == columnCount)
    {
      rowCount += 1;
      Object[] arrayOfObject = data;
      for (int j = 0; j < i; j++) {
        arrayOfObject[(paramInt + j)] = paramArrayList.get(j);
      }
      return;
    }
    paramArrayList = new StringBuilder();
    paramArrayList.append("columnNames.length = ");
    paramArrayList.append(columnCount);
    paramArrayList.append(", columnValues.size() = ");
    paramArrayList.append(i);
    throw new IllegalArgumentException(paramArrayList.toString());
  }
  
  private void ensureCapacity(int paramInt)
  {
    if (paramInt > data.length)
    {
      Object[] arrayOfObject = data;
      int i = data.length * 2;
      int j = i;
      if (i < paramInt) {
        j = paramInt;
      }
      data = new Object[j];
      System.arraycopy(arrayOfObject, 0, data, 0, arrayOfObject.length);
    }
  }
  
  private Object get(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < columnCount))
    {
      if (mPos >= 0)
      {
        if (mPos < rowCount) {
          return data[(mPos * columnCount + paramInt)];
        }
        throw new CursorIndexOutOfBoundsException("After last row.");
      }
      throw new CursorIndexOutOfBoundsException("Before first row.");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Requested column: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(", # of columns: ");
    localStringBuilder.append(columnCount);
    throw new CursorIndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public void addRow(Iterable<?> paramIterable)
  {
    int i = rowCount * columnCount;
    int j = columnCount + i;
    ensureCapacity(j);
    if ((paramIterable instanceof ArrayList))
    {
      addRow((ArrayList)paramIterable, i);
      return;
    }
    Object[] arrayOfObject = data;
    paramIterable = paramIterable.iterator();
    while (paramIterable.hasNext())
    {
      Object localObject = paramIterable.next();
      if (i != j)
      {
        arrayOfObject[i] = localObject;
        i++;
      }
      else
      {
        throw new IllegalArgumentException("columnValues.size() > columnNames.length");
      }
    }
    if (i == j)
    {
      rowCount += 1;
      return;
    }
    throw new IllegalArgumentException("columnValues.size() < columnNames.length");
  }
  
  public void addRow(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject.length == columnCount)
    {
      int i = rowCount;
      rowCount = (i + 1);
      i *= columnCount;
      ensureCapacity(columnCount + i);
      System.arraycopy(paramArrayOfObject, 0, data, i, columnCount);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("columnNames.length = ");
    localStringBuilder.append(columnCount);
    localStringBuilder.append(", columnValues.length = ");
    localStringBuilder.append(paramArrayOfObject.length);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public byte[] getBlob(int paramInt)
  {
    return (byte[])get(paramInt);
  }
  
  public String[] getColumnNames()
  {
    return columnNames;
  }
  
  public int getCount()
  {
    return rowCount;
  }
  
  public double getDouble(int paramInt)
  {
    Object localObject = get(paramInt);
    if (localObject == null) {
      return 0.0D;
    }
    if ((localObject instanceof Number)) {
      return ((Number)localObject).doubleValue();
    }
    return Double.parseDouble(localObject.toString());
  }
  
  public float getFloat(int paramInt)
  {
    Object localObject = get(paramInt);
    if (localObject == null) {
      return 0.0F;
    }
    if ((localObject instanceof Number)) {
      return ((Number)localObject).floatValue();
    }
    return Float.parseFloat(localObject.toString());
  }
  
  public int getInt(int paramInt)
  {
    Object localObject = get(paramInt);
    if (localObject == null) {
      return 0;
    }
    if ((localObject instanceof Number)) {
      return ((Number)localObject).intValue();
    }
    return Integer.parseInt(localObject.toString());
  }
  
  public long getLong(int paramInt)
  {
    Object localObject = get(paramInt);
    if (localObject == null) {
      return 0L;
    }
    if ((localObject instanceof Number)) {
      return ((Number)localObject).longValue();
    }
    return Long.parseLong(localObject.toString());
  }
  
  public short getShort(int paramInt)
  {
    Object localObject = get(paramInt);
    if (localObject == null) {
      return 0;
    }
    if ((localObject instanceof Number)) {
      return ((Number)localObject).shortValue();
    }
    return Short.parseShort(localObject.toString());
  }
  
  public String getString(int paramInt)
  {
    Object localObject = get(paramInt);
    if (localObject == null) {
      return null;
    }
    return localObject.toString();
  }
  
  public int getType(int paramInt)
  {
    return DatabaseUtils.getTypeOfObject(get(paramInt));
  }
  
  public boolean isNull(int paramInt)
  {
    boolean bool;
    if (get(paramInt) == null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public RowBuilder newRow()
  {
    int i = rowCount;
    rowCount = (i + 1);
    ensureCapacity(rowCount * columnCount);
    return new RowBuilder(i);
  }
  
  public class RowBuilder
  {
    private final int endIndex;
    private int index;
    private final int row;
    
    RowBuilder(int paramInt)
    {
      row = paramInt;
      index = (columnCount * paramInt);
      endIndex = (index + columnCount);
    }
    
    public RowBuilder add(Object paramObject)
    {
      if (index != endIndex)
      {
        Object[] arrayOfObject = data;
        int i = index;
        index = (i + 1);
        arrayOfObject[i] = paramObject;
        return this;
      }
      throw new CursorIndexOutOfBoundsException("No more columns left.");
    }
    
    public RowBuilder add(String paramString, Object paramObject)
    {
      for (int i = 0; i < columnNames.length; i++) {
        if (paramString.equals(columnNames[i])) {
          data[(row * columnCount + i)] = paramObject;
        }
      }
      return this;
    }
  }
}
