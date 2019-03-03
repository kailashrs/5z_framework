package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCursor
  implements CrossProcessCursor
{
  private static final String TAG = "Cursor";
  @Deprecated
  protected boolean mClosed;
  private final ContentObservable mContentObservable = new ContentObservable();
  @Deprecated
  protected ContentResolver mContentResolver;
  protected Long mCurrentRowID;
  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  private Bundle mExtras = Bundle.EMPTY;
  private Uri mNotifyUri;
  @Deprecated
  protected int mPos = -1;
  protected int mRowIdColumnIndex;
  private ContentObserver mSelfObserver;
  private final Object mSelfObserverLock = new Object();
  private boolean mSelfObserverRegistered;
  protected HashMap<Long, Map<String, Object>> mUpdatedRows;
  
  public AbstractCursor() {}
  
  protected void checkPosition()
  {
    if ((-1 != mPos) && (getCount() != mPos)) {
      return;
    }
    throw new CursorIndexOutOfBoundsException(mPos, getCount());
  }
  
  public void close()
  {
    mClosed = true;
    mContentObservable.unregisterAll();
    onDeactivateOrClose();
  }
  
  public void copyStringToBuffer(int paramInt, CharArrayBuffer paramCharArrayBuffer)
  {
    String str = getString(paramInt);
    if (str != null)
    {
      char[] arrayOfChar = data;
      if ((arrayOfChar != null) && (arrayOfChar.length >= str.length())) {
        str.getChars(0, str.length(), arrayOfChar, 0);
      } else {
        data = str.toCharArray();
      }
      sizeCopied = str.length();
    }
    else
    {
      sizeCopied = 0;
    }
  }
  
  public void deactivate()
  {
    onDeactivateOrClose();
  }
  
  public void fillWindow(int paramInt, CursorWindow paramCursorWindow)
  {
    DatabaseUtils.cursorFillWindow(this, paramInt, paramCursorWindow);
  }
  
  protected void finalize()
  {
    if ((mSelfObserver != null) && (mSelfObserverRegistered == true)) {
      mContentResolver.unregisterContentObserver(mSelfObserver);
    }
    try
    {
      if (!mClosed) {
        close();
      }
    }
    catch (Exception localException) {}
  }
  
  public byte[] getBlob(int paramInt)
  {
    throw new UnsupportedOperationException("getBlob is not supported");
  }
  
  public int getColumnCount()
  {
    return getColumnNames().length;
  }
  
  public int getColumnIndex(String paramString)
  {
    int i = paramString.lastIndexOf('.');
    Object localObject = paramString;
    if (i != -1)
    {
      localObject = new Exception();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("requesting column name with table name -- ");
      localStringBuilder.append(paramString);
      Log.e("Cursor", localStringBuilder.toString(), (Throwable)localObject);
      localObject = paramString.substring(i + 1);
    }
    paramString = getColumnNames();
    int j = paramString.length;
    for (i = 0; i < j; i++) {
      if (paramString[i].equalsIgnoreCase((String)localObject)) {
        return i;
      }
    }
    return -1;
  }
  
  public int getColumnIndexOrThrow(String paramString)
  {
    int i = getColumnIndex(paramString);
    if (i >= 0) {
      return i;
    }
    Object localObject = "";
    try
    {
      String str = Arrays.toString(getColumnNames());
      localObject = str;
    }
    catch (Exception localException)
    {
      Log.d("Cursor", "Cannot collect column names for debug purposes", localException);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("column '");
    localStringBuilder.append(paramString);
    localStringBuilder.append("' does not exist. Available columns: ");
    localStringBuilder.append((String)localObject);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public String getColumnName(int paramInt)
  {
    return getColumnNames()[paramInt];
  }
  
  public abstract String[] getColumnNames();
  
  public abstract int getCount();
  
  public abstract double getDouble(int paramInt);
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public abstract float getFloat(int paramInt);
  
  public abstract int getInt(int paramInt);
  
  public abstract long getLong(int paramInt);
  
  public Uri getNotificationUri()
  {
    synchronized (mSelfObserverLock)
    {
      Uri localUri = mNotifyUri;
      return localUri;
    }
  }
  
  public final int getPosition()
  {
    return mPos;
  }
  
  public abstract short getShort(int paramInt);
  
  public abstract String getString(int paramInt);
  
  public int getType(int paramInt)
  {
    return 3;
  }
  
  @Deprecated
  protected Object getUpdatedField(int paramInt)
  {
    return null;
  }
  
  public boolean getWantsAllOnMoveCalls()
  {
    return false;
  }
  
  public CursorWindow getWindow()
  {
    return null;
  }
  
  public final boolean isAfterLast()
  {
    int i = getCount();
    boolean bool = true;
    if (i == 0) {
      return true;
    }
    if (mPos != getCount()) {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isBeforeFirst()
  {
    int i = getCount();
    boolean bool = true;
    if (i == 0) {
      return true;
    }
    if (mPos != -1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isClosed()
  {
    return mClosed;
  }
  
  @Deprecated
  protected boolean isFieldUpdated(int paramInt)
  {
    return false;
  }
  
  public final boolean isFirst()
  {
    boolean bool;
    if ((mPos == 0) && (getCount() != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isLast()
  {
    int i = getCount();
    boolean bool;
    if ((mPos == i - 1) && (i != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public abstract boolean isNull(int paramInt);
  
  public final boolean move(int paramInt)
  {
    return moveToPosition(mPos + paramInt);
  }
  
  public final boolean moveToFirst()
  {
    return moveToPosition(0);
  }
  
  public final boolean moveToLast()
  {
    return moveToPosition(getCount() - 1);
  }
  
  public final boolean moveToNext()
  {
    return moveToPosition(mPos + 1);
  }
  
  public final boolean moveToPosition(int paramInt)
  {
    int i = getCount();
    if (paramInt >= i)
    {
      mPos = i;
      return false;
    }
    if (paramInt < 0)
    {
      mPos = -1;
      return false;
    }
    if (paramInt == mPos) {
      return true;
    }
    boolean bool = onMove(mPos, paramInt);
    if (!bool) {
      mPos = -1;
    } else {
      mPos = paramInt;
    }
    return bool;
  }
  
  public final boolean moveToPrevious()
  {
    return moveToPosition(mPos - 1);
  }
  
  protected void onChange(boolean paramBoolean)
  {
    synchronized (mSelfObserverLock)
    {
      mContentObservable.dispatchChange(paramBoolean, null);
      if ((mNotifyUri != null) && (paramBoolean)) {
        mContentResolver.notifyChange(mNotifyUri, mSelfObserver);
      }
      return;
    }
  }
  
  protected void onDeactivateOrClose()
  {
    if (mSelfObserver != null)
    {
      mContentResolver.unregisterContentObserver(mSelfObserver);
      mSelfObserverRegistered = false;
    }
    mDataSetObservable.notifyInvalidated();
  }
  
  public boolean onMove(int paramInt1, int paramInt2)
  {
    return true;
  }
  
  public void registerContentObserver(ContentObserver paramContentObserver)
  {
    mContentObservable.registerObserver(paramContentObserver);
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    mDataSetObservable.registerObserver(paramDataSetObserver);
  }
  
  public boolean requery()
  {
    if ((mSelfObserver != null) && (!mSelfObserverRegistered))
    {
      mContentResolver.registerContentObserver(mNotifyUri, true, mSelfObserver);
      mSelfObserverRegistered = true;
    }
    mDataSetObservable.notifyChanged();
    return true;
  }
  
  public Bundle respond(Bundle paramBundle)
  {
    return Bundle.EMPTY;
  }
  
  public void setExtras(Bundle paramBundle)
  {
    if (paramBundle == null) {
      paramBundle = Bundle.EMPTY;
    }
    mExtras = paramBundle;
  }
  
  public void setNotificationUri(ContentResolver paramContentResolver, Uri paramUri)
  {
    setNotificationUri(paramContentResolver, paramUri, paramContentResolver.getUserId());
  }
  
  public void setNotificationUri(ContentResolver paramContentResolver, Uri paramUri, int paramInt)
  {
    synchronized (mSelfObserverLock)
    {
      mNotifyUri = paramUri;
      mContentResolver = paramContentResolver;
      if (mSelfObserver != null) {
        mContentResolver.unregisterContentObserver(mSelfObserver);
      }
      paramContentResolver = new android/database/AbstractCursor$SelfContentObserver;
      paramContentResolver.<init>(this);
      mSelfObserver = paramContentResolver;
      mContentResolver.registerContentObserver(mNotifyUri, true, mSelfObserver, paramInt);
      mSelfObserverRegistered = true;
      return;
    }
  }
  
  public void unregisterContentObserver(ContentObserver paramContentObserver)
  {
    if (!mClosed) {
      mContentObservable.unregisterObserver(paramContentObserver);
    }
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    mDataSetObservable.unregisterObserver(paramDataSetObserver);
  }
  
  protected static class SelfContentObserver
    extends ContentObserver
  {
    WeakReference<AbstractCursor> mCursor;
    
    public SelfContentObserver(AbstractCursor paramAbstractCursor)
    {
      super();
      mCursor = new WeakReference(paramAbstractCursor);
    }
    
    public boolean deliverSelfNotifications()
    {
      return false;
    }
    
    public void onChange(boolean paramBoolean)
    {
      AbstractCursor localAbstractCursor = (AbstractCursor)mCursor.get();
      if (localAbstractCursor != null) {
        localAbstractCursor.onChange(false);
      }
    }
  }
}
