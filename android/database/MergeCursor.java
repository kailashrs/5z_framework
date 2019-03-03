package android.database;

public class MergeCursor
  extends AbstractCursor
{
  private Cursor mCursor;
  private Cursor[] mCursors;
  private DataSetObserver mObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      mPos = -1;
    }
    
    public void onInvalidated()
    {
      mPos = -1;
    }
  };
  
  public MergeCursor(Cursor[] paramArrayOfCursor)
  {
    mCursors = paramArrayOfCursor;
    int i = 0;
    mCursor = paramArrayOfCursor[0];
    while (i < mCursors.length)
    {
      if (mCursors[i] != null) {
        mCursors[i].registerDataSetObserver(mObserver);
      }
      i++;
    }
  }
  
  public void close()
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].close();
      }
    }
    super.close();
  }
  
  public void deactivate()
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].deactivate();
      }
    }
    super.deactivate();
  }
  
  public byte[] getBlob(int paramInt)
  {
    return mCursor.getBlob(paramInt);
  }
  
  public String[] getColumnNames()
  {
    if (mCursor != null) {
      return mCursor.getColumnNames();
    }
    return new String[0];
  }
  
  public int getCount()
  {
    int i = 0;
    int j = mCursors.length;
    int k = 0;
    while (k < j)
    {
      int m = i;
      if (mCursors[k] != null) {
        m = i + mCursors[k].getCount();
      }
      k++;
      i = m;
    }
    return i;
  }
  
  public double getDouble(int paramInt)
  {
    return mCursor.getDouble(paramInt);
  }
  
  public float getFloat(int paramInt)
  {
    return mCursor.getFloat(paramInt);
  }
  
  public int getInt(int paramInt)
  {
    return mCursor.getInt(paramInt);
  }
  
  public long getLong(int paramInt)
  {
    return mCursor.getLong(paramInt);
  }
  
  public short getShort(int paramInt)
  {
    return mCursor.getShort(paramInt);
  }
  
  public String getString(int paramInt)
  {
    return mCursor.getString(paramInt);
  }
  
  public int getType(int paramInt)
  {
    return mCursor.getType(paramInt);
  }
  
  public boolean isNull(int paramInt)
  {
    return mCursor.isNull(paramInt);
  }
  
  public boolean onMove(int paramInt1, int paramInt2)
  {
    mCursor = null;
    int i = mCursors.length;
    int j = 0;
    for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
      if (mCursors[paramInt1] != null)
      {
        if (paramInt2 < mCursors[paramInt1].getCount() + j)
        {
          mCursor = mCursors[paramInt1];
          break;
        }
        j += mCursors[paramInt1].getCount();
      }
    }
    if (mCursor != null) {
      return mCursor.moveToPosition(paramInt2 - j);
    }
    return false;
  }
  
  public void registerContentObserver(ContentObserver paramContentObserver)
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].registerContentObserver(paramContentObserver);
      }
    }
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].registerDataSetObserver(paramDataSetObserver);
      }
    }
  }
  
  public boolean requery()
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if ((mCursors[j] != null) && (!mCursors[j].requery())) {
        return false;
      }
    }
    return true;
  }
  
  public void unregisterContentObserver(ContentObserver paramContentObserver)
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].unregisterContentObserver(paramContentObserver);
      }
    }
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].unregisterDataSetObserver(paramDataSetObserver);
      }
    }
  }
}
