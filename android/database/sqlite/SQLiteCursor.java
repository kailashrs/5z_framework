package android.database.sqlite;

import android.database.AbstractWindowedCursor;
import android.database.CursorWindow;
import android.database.DatabaseUtils;
import android.os.StrictMode;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.Map;

public class SQLiteCursor
  extends AbstractWindowedCursor
{
  static final int NO_COUNT = -1;
  static final String TAG = "SQLiteCursor";
  private Map<String, Integer> mColumnNameMap;
  private final String[] mColumns;
  private int mCount = -1;
  private int mCursorWindowCapacity;
  private final SQLiteCursorDriver mDriver;
  private final String mEditTable;
  private boolean mFillWindowForwardOnly;
  private final SQLiteQuery mQuery;
  private final Throwable mStackTrace;
  
  public SQLiteCursor(SQLiteCursorDriver paramSQLiteCursorDriver, String paramString, SQLiteQuery paramSQLiteQuery)
  {
    if (paramSQLiteQuery != null)
    {
      if (StrictMode.vmSqliteObjectLeaksEnabled()) {
        mStackTrace = new DatabaseObjectNotClosedException().fillInStackTrace();
      } else {
        mStackTrace = null;
      }
      mDriver = paramSQLiteCursorDriver;
      mEditTable = paramString;
      mColumnNameMap = null;
      mQuery = paramSQLiteQuery;
      mColumns = paramSQLiteQuery.getColumnNames();
      return;
    }
    throw new IllegalArgumentException("query object cannot be null");
  }
  
  @Deprecated
  public SQLiteCursor(SQLiteDatabase paramSQLiteDatabase, SQLiteCursorDriver paramSQLiteCursorDriver, String paramString, SQLiteQuery paramSQLiteQuery)
  {
    this(paramSQLiteCursorDriver, paramString, paramSQLiteQuery);
  }
  
  private void fillWindow(int paramInt)
  {
    clearOrCreateWindow(getDatabase().getPath());
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("requiredPos cannot be negative, but was ");
      localStringBuilder.append(paramInt);
      Preconditions.checkArgumentNonnegative(paramInt, localStringBuilder.toString());
      if (mCount == -1)
      {
        mCount = mQuery.fillWindow(mWindow, paramInt, paramInt, true);
        mCursorWindowCapacity = mWindow.getNumRows();
        if (Log.isLoggable("SQLiteCursor", 3))
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("received count(*) from native_fill_window: ");
          localStringBuilder.append(mCount);
          Log.d("SQLiteCursor", localStringBuilder.toString());
        }
      }
      else
      {
        int i;
        if (mFillWindowForwardOnly) {
          i = paramInt;
        } else {
          i = DatabaseUtils.cursorPickFillWindowStartPosition(paramInt, mCursorWindowCapacity);
        }
        mQuery.fillWindow(mWindow, i, paramInt, false);
      }
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      closeWindow();
      throw localRuntimeException;
    }
  }
  
  public void close()
  {
    super.close();
    try
    {
      mQuery.close();
      mDriver.cursorClosed();
      return;
    }
    finally {}
  }
  
  public void deactivate()
  {
    super.deactivate();
    mDriver.cursorDeactivated();
  }
  
  protected void finalize()
  {
    try
    {
      if (mWindow != null)
      {
        if (mStackTrace != null)
        {
          String str = mQuery.getSql();
          int i = str.length();
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Finalizing a Cursor that has not been deactivated or closed. database = ");
          localStringBuilder.append(mQuery.getDatabase().getLabel());
          localStringBuilder.append(", table = ");
          localStringBuilder.append(mEditTable);
          localStringBuilder.append(", query = ");
          int j = 1000;
          if (i > 1000) {
            i = j;
          }
          localStringBuilder.append(str.substring(0, i));
          StrictMode.onSqliteObjectLeaked(localStringBuilder.toString(), mStackTrace);
        }
        close();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getColumnIndex(String paramString)
  {
    Object localObject2;
    if (mColumnNameMap == null)
    {
      localObject1 = mColumns;
      int i = localObject1.length;
      localObject2 = new HashMap(i, 1.0F);
      for (j = 0; j < i; j++) {
        ((HashMap)localObject2).put(localObject1[j], Integer.valueOf(j));
      }
      mColumnNameMap = ((Map)localObject2);
    }
    int j = paramString.lastIndexOf('.');
    Object localObject1 = paramString;
    if (j != -1)
    {
      localObject2 = new Exception();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("requesting column name with table name -- ");
      ((StringBuilder)localObject1).append(paramString);
      Log.e("SQLiteCursor", ((StringBuilder)localObject1).toString(), (Throwable)localObject2);
      localObject1 = paramString.substring(j + 1);
    }
    paramString = (Integer)mColumnNameMap.get(localObject1);
    if (paramString != null) {
      return paramString.intValue();
    }
    return -1;
  }
  
  public String[] getColumnNames()
  {
    return mColumns;
  }
  
  public int getCount()
  {
    if (mCount == -1) {
      fillWindow(0);
    }
    return mCount;
  }
  
  public SQLiteDatabase getDatabase()
  {
    return mQuery.getDatabase();
  }
  
  public boolean onMove(int paramInt1, int paramInt2)
  {
    if ((mWindow == null) || (paramInt2 < mWindow.getStartPosition()) || (paramInt2 >= mWindow.getStartPosition() + mWindow.getNumRows())) {
      fillWindow(paramInt2);
    }
    return true;
  }
  
  /* Error */
  public boolean requery()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 255	android/database/sqlite/SQLiteCursor:isClosed	()Z
    //   4: ifeq +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield 57	android/database/sqlite/SQLiteCursor:mQuery	Landroid/database/sqlite/SQLiteQuery;
    //   15: invokevirtual 181	android/database/sqlite/SQLiteQuery:getDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   18: invokevirtual 258	android/database/sqlite/SQLiteDatabase:isOpen	()Z
    //   21: ifne +7 -> 28
    //   24: aload_0
    //   25: monitorexit
    //   26: iconst_0
    //   27: ireturn
    //   28: aload_0
    //   29: getfield 120	android/database/sqlite/SQLiteCursor:mWindow	Landroid/database/CursorWindow;
    //   32: ifnull +10 -> 42
    //   35: aload_0
    //   36: getfield 120	android/database/sqlite/SQLiteCursor:mWindow	Landroid/database/CursorWindow;
    //   39: invokevirtual 261	android/database/CursorWindow:clear	()V
    //   42: aload_0
    //   43: iconst_m1
    //   44: putfield 264	android/database/sqlite/SQLiteCursor:mPos	I
    //   47: aload_0
    //   48: iconst_m1
    //   49: putfield 34	android/database/sqlite/SQLiteCursor:mCount	I
    //   52: aload_0
    //   53: getfield 51	android/database/sqlite/SQLiteCursor:mDriver	Landroid/database/sqlite/SQLiteCursorDriver;
    //   56: aload_0
    //   57: invokeinterface 268 2 0
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_0
    //   65: invokespecial 270	android/database/AbstractWindowedCursor:requery	()Z
    //   68: istore_1
    //   69: iload_1
    //   70: ireturn
    //   71: astore_2
    //   72: new 97	java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial 98	java/lang/StringBuilder:<init>	()V
    //   79: astore_3
    //   80: aload_3
    //   81: ldc_w 272
    //   84: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: aload_3
    //   89: aload_2
    //   90: invokevirtual 275	java/lang/IllegalStateException:getMessage	()Ljava/lang/String;
    //   93: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: ldc 11
    //   99: aload_3
    //   100: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   103: aload_2
    //   104: invokestatic 278	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   107: pop
    //   108: iconst_0
    //   109: ireturn
    //   110: astore_2
    //   111: aload_0
    //   112: monitorexit
    //   113: aload_2
    //   114: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	115	0	this	SQLiteCursor
    //   68	2	1	bool	boolean
    //   71	33	2	localIllegalStateException	IllegalStateException
    //   110	4	2	localObject	Object
    //   79	21	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   64	69	71	java/lang/IllegalStateException
    //   11	26	110	finally
    //   28	42	110	finally
    //   42	64	110	finally
    //   111	113	110	finally
  }
  
  public void setFillWindowForwardOnly(boolean paramBoolean)
  {
    mFillWindowForwardOnly = paramBoolean;
  }
  
  public void setSelectionArguments(String[] paramArrayOfString)
  {
    mDriver.setBindArguments(paramArrayOfString);
  }
  
  public void setWindow(CursorWindow paramCursorWindow)
  {
    super.setWindow(paramCursorWindow);
    mCount = -1;
  }
}
