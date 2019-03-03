package android.database.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.os.FileUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.io.File;

public abstract class SQLiteOpenHelper
{
  private static final String TAG = SQLiteOpenHelper.class.getSimpleName();
  private final Context mContext;
  private SQLiteDatabase mDatabase;
  private boolean mIsInitializing;
  private final int mMinimumSupportedVersion;
  private final String mName;
  private final int mNewVersion;
  private SQLiteDatabase.OpenParams.Builder mOpenParamsBuilder;
  
  private SQLiteOpenHelper(Context paramContext, String paramString, int paramInt1, int paramInt2, SQLiteDatabase.OpenParams.Builder paramBuilder)
  {
    Preconditions.checkNotNull(paramBuilder);
    if (paramInt1 >= 1)
    {
      mContext = paramContext;
      mName = paramString;
      mNewVersion = paramInt1;
      mMinimumSupportedVersion = Math.max(0, paramInt2);
      setOpenParamsBuilder(paramBuilder);
      return;
    }
    paramContext = new StringBuilder();
    paramContext.append("Version must be >= 1, was ");
    paramContext.append(paramInt1);
    throw new IllegalArgumentException(paramContext.toString());
  }
  
  public SQLiteOpenHelper(Context paramContext, String paramString, int paramInt, SQLiteDatabase.OpenParams paramOpenParams)
  {
    this(paramContext, paramString, paramInt, 0, paramOpenParams.toBuilder());
  }
  
  public SQLiteOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt)
  {
    this(paramContext, paramString, paramCursorFactory, paramInt, null);
  }
  
  public SQLiteOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt1, int paramInt2, DatabaseErrorHandler paramDatabaseErrorHandler)
  {
    this(paramContext, paramString, paramInt1, paramInt2, new SQLiteDatabase.OpenParams.Builder());
    mOpenParamsBuilder.setCursorFactory(paramCursorFactory);
    mOpenParamsBuilder.setErrorHandler(paramDatabaseErrorHandler);
  }
  
  public SQLiteOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt, DatabaseErrorHandler paramDatabaseErrorHandler)
  {
    this(paramContext, paramString, paramCursorFactory, paramInt, 0, paramDatabaseErrorHandler);
  }
  
  private SQLiteDatabase getDatabaseLocked(boolean paramBoolean)
  {
    if (mDatabase != null) {
      if (!mDatabase.isOpen()) {
        mDatabase = null;
      } else if ((!paramBoolean) || (!mDatabase.isReadOnly())) {
        return mDatabase;
      }
    }
    Object localObject4;
    if (!mIsInitializing)
    {
      SQLiteDatabase localSQLiteDatabase1 = mDatabase;
      localObject4 = localSQLiteDatabase1;
      try
      {
        mIsInitializing = true;
        Object localObject6;
        if (localSQLiteDatabase1 != null)
        {
          localSQLiteDatabase2 = localSQLiteDatabase1;
          if (paramBoolean)
          {
            localSQLiteDatabase2 = localSQLiteDatabase1;
            localObject4 = localSQLiteDatabase1;
            if (localSQLiteDatabase1.isReadOnly())
            {
              localObject4 = localSQLiteDatabase1;
              localSQLiteDatabase1.reopenReadWrite();
              localSQLiteDatabase2 = localSQLiteDatabase1;
            }
          }
        }
        else
        {
          localObject4 = localSQLiteDatabase1;
          if (mName == null)
          {
            localObject4 = localSQLiteDatabase1;
            localSQLiteDatabase2 = SQLiteDatabase.createInMemory(mOpenParamsBuilder.build());
          }
          else
          {
            localObject4 = localSQLiteDatabase1;
            File localFile = mContext.getDatabasePath(mName);
            localObject4 = localSQLiteDatabase1;
            localObject6 = mOpenParamsBuilder.build();
            localSQLiteDatabase2 = localSQLiteDatabase1;
            localObject4 = localSQLiteDatabase1;
            try
            {
              localSQLiteDatabase1 = SQLiteDatabase.openDatabase(localFile, (SQLiteDatabase.OpenParams)localObject6);
              localSQLiteDatabase2 = localSQLiteDatabase1;
              localObject4 = localSQLiteDatabase1;
              setFilePermissionsForDb(localFile.getPath());
              localSQLiteDatabase2 = localSQLiteDatabase1;
            }
            catch (SQLException localSQLException)
            {
              if (paramBoolean) {
                break label853;
              }
            }
            localObject4 = localSQLiteDatabase2;
            String str = TAG;
            localObject4 = localSQLiteDatabase2;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localObject4 = localSQLiteDatabase2;
            localStringBuilder.<init>();
            localObject4 = localSQLiteDatabase2;
            localStringBuilder.append("Couldn't open ");
            localObject4 = localSQLiteDatabase2;
            localStringBuilder.append(mName);
            localObject4 = localSQLiteDatabase2;
            localStringBuilder.append(" for writing (will try read-only):");
            localObject4 = localSQLiteDatabase2;
            Log.e(str, localStringBuilder.toString(), localSQLException);
            localObject4 = localSQLiteDatabase2;
            localSQLiteDatabase2 = SQLiteDatabase.openDatabase(localFile, ((SQLiteDatabase.OpenParams)localObject6).toBuilder().addOpenFlags(1).build());
          }
        }
        localObject4 = localSQLiteDatabase2;
        onConfigure(localSQLiteDatabase2);
        localObject4 = localSQLiteDatabase2;
        int i = localSQLiteDatabase2.getVersion();
        localObject4 = localSQLiteDatabase2;
        if (i != mNewVersion)
        {
          localObject4 = localSQLiteDatabase2;
          if (!localSQLiteDatabase2.isReadOnly())
          {
            if (i > 0)
            {
              localObject4 = localSQLiteDatabase2;
              if (i < mMinimumSupportedVersion)
              {
                localObject4 = localSQLiteDatabase2;
                Object localObject1 = new java/io/File;
                localObject4 = localSQLiteDatabase2;
                ((File)localObject1).<init>(localSQLiteDatabase2.getPath());
                localObject4 = localSQLiteDatabase2;
                onBeforeDelete(localSQLiteDatabase2);
                localObject4 = localSQLiteDatabase2;
                localSQLiteDatabase2.close();
                localObject4 = localSQLiteDatabase2;
                if (SQLiteDatabase.deleteDatabase((File)localObject1))
                {
                  localObject4 = localSQLiteDatabase2;
                  mIsInitializing = false;
                  localObject4 = localSQLiteDatabase2;
                  localObject1 = getDatabaseLocked(paramBoolean);
                  mIsInitializing = false;
                  if ((localSQLiteDatabase2 != null) && (localSQLiteDatabase2 != mDatabase)) {
                    localSQLiteDatabase2.close();
                  }
                  return localObject1;
                }
                localObject4 = localSQLiteDatabase2;
                localObject1 = new java/lang/IllegalStateException;
                localObject4 = localSQLiteDatabase2;
                localObject6 = new java/lang/StringBuilder;
                localObject4 = localSQLiteDatabase2;
                ((StringBuilder)localObject6).<init>();
                localObject4 = localSQLiteDatabase2;
                ((StringBuilder)localObject6).append("Unable to delete obsolete database ");
                localObject4 = localSQLiteDatabase2;
                ((StringBuilder)localObject6).append(mName);
                localObject4 = localSQLiteDatabase2;
                ((StringBuilder)localObject6).append(" with version ");
                localObject4 = localSQLiteDatabase2;
                ((StringBuilder)localObject6).append(i);
                localObject4 = localSQLiteDatabase2;
                ((IllegalStateException)localObject1).<init>(((StringBuilder)localObject6).toString());
                localObject4 = localSQLiteDatabase2;
                throw ((Throwable)localObject1);
              }
            }
            localObject4 = localSQLiteDatabase2;
            localSQLiteDatabase2.beginTransaction();
            if (i == 0) {
              try
              {
                onCreate(localSQLiteDatabase2);
              }
              finally
              {
                break label604;
              }
            } else if (i > mNewVersion) {
              onDowngrade(localSQLiteDatabase2, i, mNewVersion);
            } else {
              onUpgrade(localSQLiteDatabase2, i, mNewVersion);
            }
            localSQLiteDatabase2.setVersion(mNewVersion);
            localSQLiteDatabase2.setTransactionSuccessful();
            localObject4 = localSQLiteDatabase2;
            localSQLiteDatabase2.endTransaction();
            break label730;
            label604:
            localObject4 = localSQLiteDatabase2;
            localSQLiteDatabase2.endTransaction();
            localObject4 = localSQLiteDatabase2;
            throw localObject2;
          }
          else
          {
            localObject4 = localSQLiteDatabase2;
            localObject3 = new android/database/sqlite/SQLiteException;
            localObject4 = localSQLiteDatabase2;
            localObject6 = new java/lang/StringBuilder;
            localObject4 = localSQLiteDatabase2;
            ((StringBuilder)localObject6).<init>();
            localObject4 = localSQLiteDatabase2;
            ((StringBuilder)localObject6).append("Can't upgrade read-only database from version ");
            localObject4 = localSQLiteDatabase2;
            ((StringBuilder)localObject6).append(localSQLiteDatabase2.getVersion());
            localObject4 = localSQLiteDatabase2;
            ((StringBuilder)localObject6).append(" to ");
            localObject4 = localSQLiteDatabase2;
            ((StringBuilder)localObject6).append(mNewVersion);
            localObject4 = localSQLiteDatabase2;
            ((StringBuilder)localObject6).append(": ");
            localObject4 = localSQLiteDatabase2;
            ((StringBuilder)localObject6).append(mName);
            localObject4 = localSQLiteDatabase2;
            ((SQLiteException)localObject3).<init>(((StringBuilder)localObject6).toString());
            localObject4 = localSQLiteDatabase2;
            throw ((Throwable)localObject3);
          }
        }
        label730:
        localObject4 = localSQLiteDatabase2;
        onOpen(localSQLiteDatabase2);
        localObject4 = localSQLiteDatabase2;
        if (localSQLiteDatabase2.isReadOnly())
        {
          localObject4 = localSQLiteDatabase2;
          localObject6 = TAG;
          localObject4 = localSQLiteDatabase2;
          localObject3 = new java/lang/StringBuilder;
          localObject4 = localSQLiteDatabase2;
          ((StringBuilder)localObject3).<init>();
          localObject4 = localSQLiteDatabase2;
          ((StringBuilder)localObject3).append("Opened ");
          localObject4 = localSQLiteDatabase2;
          ((StringBuilder)localObject3).append(mName);
          localObject4 = localSQLiteDatabase2;
          ((StringBuilder)localObject3).append(" in read-only mode");
          localObject4 = localSQLiteDatabase2;
          Log.w((String)localObject6, ((StringBuilder)localObject3).toString());
        }
        localObject4 = localSQLiteDatabase2;
        mDatabase = localSQLiteDatabase2;
        mIsInitializing = false;
        if ((localSQLiteDatabase2 != null) && (localSQLiteDatabase2 != mDatabase)) {
          localSQLiteDatabase2.close();
        }
        return localSQLiteDatabase2;
      }
      finally
      {
        SQLiteDatabase localSQLiteDatabase2;
        Object localObject3;
        label853:
        mIsInitializing = false;
        if ((localObject4 == null) || (localObject4 == mDatabase)) {
          break label881;
        }
        ((SQLiteDatabase)localObject4).close();
      }
      localObject4 = localSQLiteDatabase2;
      throw ((Throwable)localObject3);
    }
    label881:
    throw new IllegalStateException("getDatabase called recursively");
  }
  
  private static void setFilePermissionsForDb(String paramString)
  {
    FileUtils.setPermissions(paramString, 432, -1, -1);
  }
  
  private void setOpenParamsBuilder(SQLiteDatabase.OpenParams.Builder paramBuilder)
  {
    mOpenParamsBuilder = paramBuilder;
    mOpenParamsBuilder.addOpenFlags(268435456);
  }
  
  public void close()
  {
    try
    {
      if (!mIsInitializing)
      {
        if ((mDatabase != null) && (mDatabase.isOpen()))
        {
          mDatabase.close();
          mDatabase = null;
        }
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Closed during initialization");
      throw localIllegalStateException;
    }
    finally {}
  }
  
  public String getDatabaseName()
  {
    return mName;
  }
  
  public SQLiteDatabase getReadableDatabase()
  {
    try
    {
      SQLiteDatabase localSQLiteDatabase = getDatabaseLocked(false);
      return localSQLiteDatabase;
    }
    finally {}
  }
  
  public SQLiteDatabase getWritableDatabase()
  {
    try
    {
      SQLiteDatabase localSQLiteDatabase = getDatabaseLocked(true);
      return localSQLiteDatabase;
    }
    finally {}
  }
  
  public void onBeforeDelete(SQLiteDatabase paramSQLiteDatabase) {}
  
  public void onConfigure(SQLiteDatabase paramSQLiteDatabase) {}
  
  public abstract void onCreate(SQLiteDatabase paramSQLiteDatabase);
  
  public void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    paramSQLiteDatabase = new StringBuilder();
    paramSQLiteDatabase.append("Can't downgrade database from version ");
    paramSQLiteDatabase.append(paramInt1);
    paramSQLiteDatabase.append(" to ");
    paramSQLiteDatabase.append(paramInt2);
    throw new SQLiteException(paramSQLiteDatabase.toString());
  }
  
  public void onOpen(SQLiteDatabase paramSQLiteDatabase) {}
  
  public abstract void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2);
  
  public void setIdleConnectionTimeout(long paramLong)
  {
    try
    {
      if ((mDatabase != null) && (mDatabase.isOpen()))
      {
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localIllegalStateException.<init>("Connection timeout setting cannot be changed after opening the database");
        throw localIllegalStateException;
      }
      mOpenParamsBuilder.setIdleConnectionTimeout(paramLong);
      return;
    }
    finally {}
  }
  
  public void setLookasideConfig(int paramInt1, int paramInt2)
  {
    try
    {
      if ((mDatabase != null) && (mDatabase.isOpen()))
      {
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localIllegalStateException.<init>("Lookaside memory config cannot be changed after opening the database");
        throw localIllegalStateException;
      }
      mOpenParamsBuilder.setLookasideConfig(paramInt1, paramInt2);
      return;
    }
    finally {}
  }
  
  public void setOpenParams(SQLiteDatabase.OpenParams paramOpenParams)
  {
    Preconditions.checkNotNull(paramOpenParams);
    try
    {
      if ((mDatabase != null) && (mDatabase.isOpen()))
      {
        paramOpenParams = new java/lang/IllegalStateException;
        paramOpenParams.<init>("OpenParams cannot be set after opening the database");
        throw paramOpenParams;
      }
      SQLiteDatabase.OpenParams.Builder localBuilder = new android/database/sqlite/SQLiteDatabase$OpenParams$Builder;
      localBuilder.<init>(paramOpenParams);
      setOpenParamsBuilder(localBuilder);
      return;
    }
    finally {}
  }
  
  public void setWriteAheadLoggingEnabled(boolean paramBoolean)
  {
    try
    {
      if (mOpenParamsBuilder.isWriteAheadLoggingEnabled() != paramBoolean)
      {
        if ((mDatabase != null) && (mDatabase.isOpen()) && (!mDatabase.isReadOnly())) {
          if (paramBoolean) {
            mDatabase.enableWriteAheadLogging();
          } else {
            mDatabase.disableWriteAheadLogging();
          }
        }
        mOpenParamsBuilder.setWriteAheadLoggingEnabled(paramBoolean);
      }
      mOpenParamsBuilder.addOpenFlags(1073741824);
      return;
    }
    finally {}
  }
}
