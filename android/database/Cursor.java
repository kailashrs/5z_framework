package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import java.io.Closeable;

public abstract interface Cursor
  extends Closeable
{
  public static final int FIELD_TYPE_BLOB = 4;
  public static final int FIELD_TYPE_FLOAT = 2;
  public static final int FIELD_TYPE_INTEGER = 1;
  public static final int FIELD_TYPE_NULL = 0;
  public static final int FIELD_TYPE_STRING = 3;
  
  public abstract void close();
  
  public abstract void copyStringToBuffer(int paramInt, CharArrayBuffer paramCharArrayBuffer);
  
  @Deprecated
  public abstract void deactivate();
  
  public abstract byte[] getBlob(int paramInt);
  
  public abstract int getColumnCount();
  
  public abstract int getColumnIndex(String paramString);
  
  public abstract int getColumnIndexOrThrow(String paramString)
    throws IllegalArgumentException;
  
  public abstract String getColumnName(int paramInt);
  
  public abstract String[] getColumnNames();
  
  public abstract int getCount();
  
  public abstract double getDouble(int paramInt);
  
  public abstract Bundle getExtras();
  
  public abstract float getFloat(int paramInt);
  
  public abstract int getInt(int paramInt);
  
  public abstract long getLong(int paramInt);
  
  public abstract Uri getNotificationUri();
  
  public abstract int getPosition();
  
  public abstract short getShort(int paramInt);
  
  public abstract String getString(int paramInt);
  
  public abstract int getType(int paramInt);
  
  public abstract boolean getWantsAllOnMoveCalls();
  
  public abstract boolean isAfterLast();
  
  public abstract boolean isBeforeFirst();
  
  public abstract boolean isClosed();
  
  public abstract boolean isFirst();
  
  public abstract boolean isLast();
  
  public abstract boolean isNull(int paramInt);
  
  public abstract boolean move(int paramInt);
  
  public abstract boolean moveToFirst();
  
  public abstract boolean moveToLast();
  
  public abstract boolean moveToNext();
  
  public abstract boolean moveToPosition(int paramInt);
  
  public abstract boolean moveToPrevious();
  
  public abstract void registerContentObserver(ContentObserver paramContentObserver);
  
  public abstract void registerDataSetObserver(DataSetObserver paramDataSetObserver);
  
  @Deprecated
  public abstract boolean requery();
  
  public abstract Bundle respond(Bundle paramBundle);
  
  public abstract void setExtras(Bundle paramBundle);
  
  public abstract void setNotificationUri(ContentResolver paramContentResolver, Uri paramUri);
  
  public abstract void unregisterContentObserver(ContentObserver paramContentObserver);
  
  public abstract void unregisterDataSetObserver(DataSetObserver paramDataSetObserver);
}
