package com.android.internal.database;

import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;

public class SortCursor
  extends AbstractCursor
{
  private static final String TAG = "SortCursor";
  private final int ROWCACHESIZE = 64;
  private int[][] mCurRowNumCache;
  private Cursor mCursor;
  private int[] mCursorCache = new int[64];
  private Cursor[] mCursors;
  private int mLastCacheHit = -1;
  private DataSetObserver mObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      SortCursor.access$002(SortCursor.this, -1);
    }
    
    public void onInvalidated()
    {
      SortCursor.access$102(SortCursor.this, -1);
    }
  };
  private int[] mRowNumCache = new int[64];
  private int[] mSortColumns;
  
  public SortCursor(Cursor[] paramArrayOfCursor, String paramString)
  {
    mCursors = paramArrayOfCursor;
    int i = mCursors.length;
    mSortColumns = new int[i];
    int j = 0;
    for (int k = 0; k < i; k++) {
      if (mCursors[k] != null)
      {
        mCursors[k].registerDataSetObserver(mObserver);
        mCursors[k].moveToFirst();
        mSortColumns[k] = mCursors[k].getColumnIndexOrThrow(paramString);
      }
    }
    mCursor = null;
    paramString = "";
    k = j;
    while (k < i)
    {
      paramArrayOfCursor = paramString;
      if (mCursors[k] != null) {
        if (mCursors[k].isAfterLast())
        {
          paramArrayOfCursor = paramString;
        }
        else
        {
          String str = mCursors[k].getString(mSortColumns[k]);
          if (mCursor != null)
          {
            paramArrayOfCursor = paramString;
            if (str.compareToIgnoreCase(paramString) >= 0) {}
          }
          else
          {
            paramArrayOfCursor = str;
            mCursor = mCursors[k];
          }
        }
      }
      k++;
      paramString = paramArrayOfCursor;
    }
    for (k = mRowNumCache.length - 1; k >= 0; k--) {
      mRowNumCache[k] = -2;
    }
    mCurRowNumCache = new int[64][i];
  }
  
  public void close()
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].close();
      }
    }
  }
  
  public void deactivate()
  {
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        mCursors[j].deactivate();
      }
    }
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
    int i = mCursors.length;
    for (int j = 0; j < i; j++) {
      if (mCursors[j] != null) {
        return mCursors[j].getColumnNames();
      }
    }
    throw new IllegalStateException("No cursor that can return names");
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
    if (paramInt1 == paramInt2) {
      return true;
    }
    int i = paramInt2 % 64;
    int j = mRowNumCache[i];
    int k = 0;
    if (j == paramInt2)
    {
      paramInt1 = mCursorCache[i];
      mCursor = mCursors[paramInt1];
      if (mCursor == null)
      {
        Log.w("SortCursor", "onMove: cache results in a null cursor.");
        return false;
      }
      mCursor.moveToPosition(mCurRowNumCache[i][paramInt1]);
      mLastCacheHit = i;
      return true;
    }
    mCursor = null;
    int m = mCursors.length;
    if (mLastCacheHit >= 0) {
      for (j = 0; j < m; j++) {
        if (mCursors[j] != null) {
          mCursors[j].moveToPosition(mCurRowNumCache[mLastCacheHit][j]);
        }
      }
    }
    if (paramInt2 >= paramInt1)
    {
      j = paramInt1;
      if (paramInt1 != -1) {}
    }
    else
    {
      for (paramInt1 = 0; paramInt1 < m; paramInt1++) {
        if (mCursors[paramInt1] != null) {
          mCursors[paramInt1].moveToFirst();
        }
      }
      j = 0;
    }
    paramInt1 = j;
    if (j < 0) {
      paramInt1 = 0;
    }
    j = -1;
    int n = paramInt1;
    paramInt1 = j;
    while (n <= paramInt2)
    {
      Object localObject1 = "";
      paramInt1 = -1;
      j = 0;
      while (j < m)
      {
        int i1 = paramInt1;
        Object localObject2 = localObject1;
        if (mCursors[j] != null) {
          if (mCursors[j].isAfterLast())
          {
            i1 = paramInt1;
            localObject2 = localObject1;
          }
          else
          {
            String str = mCursors[j].getString(mSortColumns[j]);
            if (paramInt1 >= 0)
            {
              i1 = paramInt1;
              localObject2 = localObject1;
              if (str.compareToIgnoreCase((String)localObject1) >= 0) {}
            }
            else
            {
              localObject2 = str;
              i1 = j;
            }
          }
        }
        j++;
        paramInt1 = i1;
        localObject1 = localObject2;
      }
      if (n == paramInt2) {
        break;
      }
      if (mCursors[paramInt1] != null) {
        mCursors[paramInt1].moveToNext();
      }
      n++;
    }
    mCursor = mCursors[paramInt1];
    mRowNumCache[i] = paramInt2;
    mCursorCache[i] = paramInt1;
    for (paramInt1 = k; paramInt1 < m; paramInt1++) {
      if (mCursors[paramInt1] != null) {
        mCurRowNumCache[i][paramInt1] = mCursors[paramInt1].getPosition();
      }
    }
    mLastCacheHit = -1;
    return true;
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
