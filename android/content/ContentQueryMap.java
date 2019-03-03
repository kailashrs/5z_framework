package android.content;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ContentQueryMap
  extends Observable
{
  private String[] mColumnNames;
  private ContentObserver mContentObserver;
  private volatile Cursor mCursor;
  private boolean mDirty = false;
  private Handler mHandlerForUpdateNotifications = null;
  private boolean mKeepUpdated = false;
  private int mKeyColumn;
  private Map<String, ContentValues> mValues = null;
  
  public ContentQueryMap(Cursor paramCursor, String paramString, boolean paramBoolean, Handler paramHandler)
  {
    mCursor = paramCursor;
    mColumnNames = mCursor.getColumnNames();
    mKeyColumn = mCursor.getColumnIndexOrThrow(paramString);
    mHandlerForUpdateNotifications = paramHandler;
    setKeepUpdated(paramBoolean);
    if (!paramBoolean) {
      readCursorIntoCache(paramCursor);
    }
  }
  
  private void readCursorIntoCache(Cursor paramCursor)
  {
    try
    {
      int i;
      if (mValues != null) {
        i = mValues.size();
      } else {
        i = 0;
      }
      Object localObject = new java/util/HashMap;
      ((HashMap)localObject).<init>(i);
      mValues = ((Map)localObject);
      while (paramCursor.moveToNext())
      {
        localObject = new android/content/ContentValues;
        ((ContentValues)localObject).<init>();
        for (i = 0; i < mColumnNames.length; i++) {
          if (i != mKeyColumn) {
            ((ContentValues)localObject).put(mColumnNames[i], paramCursor.getString(i));
          }
        }
        mValues.put(paramCursor.getString(mKeyColumn), localObject);
      }
      return;
    }
    finally {}
  }
  
  public void close()
  {
    try
    {
      if (mContentObserver != null)
      {
        mCursor.unregisterContentObserver(mContentObserver);
        mContentObserver = null;
      }
      mCursor.close();
      mCursor = null;
      return;
    }
    finally {}
  }
  
  protected void finalize()
    throws Throwable
  {
    if (mCursor != null) {
      close();
    }
    super.finalize();
  }
  
  public Map<String, ContentValues> getRows()
  {
    try
    {
      if (mDirty) {
        requery();
      }
      Map localMap = mValues;
      return localMap;
    }
    finally {}
  }
  
  public ContentValues getValues(String paramString)
  {
    try
    {
      if (mDirty) {
        requery();
      }
      paramString = (ContentValues)mValues.get(paramString);
      return paramString;
    }
    finally {}
  }
  
  public void requery()
  {
    Cursor localCursor = mCursor;
    if (localCursor == null) {
      return;
    }
    mDirty = false;
    if (!localCursor.requery()) {
      return;
    }
    readCursorIntoCache(localCursor);
    setChanged();
    notifyObservers();
  }
  
  public void setKeepUpdated(boolean paramBoolean)
  {
    if (paramBoolean == mKeepUpdated) {
      return;
    }
    mKeepUpdated = paramBoolean;
    if (!mKeepUpdated)
    {
      mCursor.unregisterContentObserver(mContentObserver);
      mContentObserver = null;
    }
    else
    {
      if (mHandlerForUpdateNotifications == null) {
        mHandlerForUpdateNotifications = new Handler();
      }
      if (mContentObserver == null) {
        mContentObserver = new ContentObserver(mHandlerForUpdateNotifications)
        {
          public void onChange(boolean paramAnonymousBoolean)
          {
            if (countObservers() != 0) {
              requery();
            } else {
              ContentQueryMap.access$002(ContentQueryMap.this, true);
            }
          }
        };
      }
      mCursor.registerContentObserver(mContentObserver);
      mDirty = true;
    }
  }
}
