package android.database.sqlite;

import android.database.DatabaseUtils;
import android.os.CancellationSignal;
import java.util.Arrays;

public abstract class SQLiteProgram
  extends SQLiteClosable
{
  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  private final Object[] mBindArgs;
  private final String[] mColumnNames;
  private final SQLiteDatabase mDatabase;
  private final int mNumParameters;
  private final boolean mReadOnly;
  private final String mSql;
  
  SQLiteProgram(SQLiteDatabase paramSQLiteDatabase, String paramString, Object[] paramArrayOfObject, CancellationSignal paramCancellationSignal)
  {
    mDatabase = paramSQLiteDatabase;
    mSql = paramString.trim();
    int i = DatabaseUtils.getSqlStatementType(mSql);
    switch (i)
    {
    default: 
      bool = true;
      if (i != 1) {
        break;
      }
      break;
    case 4: 
    case 5: 
    case 6: 
      mReadOnly = false;
      mColumnNames = EMPTY_STRING_ARRAY;
      mNumParameters = 0;
      break;
    }
    boolean bool = false;
    paramString = new SQLiteStatementInfo();
    paramSQLiteDatabase.getThreadSession().prepare(mSql, paramSQLiteDatabase.getThreadDefaultConnectionFlags(bool), paramCancellationSignal, paramString);
    mReadOnly = readOnly;
    mColumnNames = columnNames;
    mNumParameters = numParameters;
    if ((paramArrayOfObject != null) && (paramArrayOfObject.length > mNumParameters))
    {
      paramSQLiteDatabase = new StringBuilder();
      paramSQLiteDatabase.append("Too many bind arguments.  ");
      paramSQLiteDatabase.append(paramArrayOfObject.length);
      paramSQLiteDatabase.append(" arguments were provided but the statement needs ");
      paramSQLiteDatabase.append(mNumParameters);
      paramSQLiteDatabase.append(" arguments.");
      throw new IllegalArgumentException(paramSQLiteDatabase.toString());
    }
    if (mNumParameters != 0)
    {
      mBindArgs = new Object[mNumParameters];
      if (paramArrayOfObject != null) {
        System.arraycopy(paramArrayOfObject, 0, mBindArgs, 0, paramArrayOfObject.length);
      }
    }
    else
    {
      mBindArgs = null;
    }
  }
  
  private void bind(int paramInt, Object paramObject)
  {
    if ((paramInt >= 1) && (paramInt <= mNumParameters))
    {
      mBindArgs[(paramInt - 1)] = paramObject;
      return;
    }
    paramObject = new StringBuilder();
    paramObject.append("Cannot bind argument at index ");
    paramObject.append(paramInt);
    paramObject.append(" because the index is out of range.  The statement has ");
    paramObject.append(mNumParameters);
    paramObject.append(" parameters.");
    throw new IllegalArgumentException(paramObject.toString());
  }
  
  public void bindAllArgsAsStrings(String[] paramArrayOfString)
  {
    if (paramArrayOfString != null) {
      for (int i = paramArrayOfString.length; i != 0; i--) {
        bindString(i, paramArrayOfString[(i - 1)]);
      }
    }
  }
  
  public void bindBlob(int paramInt, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      bind(paramInt, paramArrayOfByte);
      return;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("the bind value at index ");
    paramArrayOfByte.append(paramInt);
    paramArrayOfByte.append(" is null");
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  public void bindDouble(int paramInt, double paramDouble)
  {
    bind(paramInt, Double.valueOf(paramDouble));
  }
  
  public void bindLong(int paramInt, long paramLong)
  {
    bind(paramInt, Long.valueOf(paramLong));
  }
  
  public void bindNull(int paramInt)
  {
    bind(paramInt, null);
  }
  
  public void bindString(int paramInt, String paramString)
  {
    if (paramString != null)
    {
      bind(paramInt, paramString);
      return;
    }
    paramString = new StringBuilder();
    paramString.append("the bind value at index ");
    paramString.append(paramInt);
    paramString.append(" is null");
    throw new IllegalArgumentException(paramString.toString());
  }
  
  public void clearBindings()
  {
    if (mBindArgs != null) {
      Arrays.fill(mBindArgs, null);
    }
  }
  
  final Object[] getBindArgs()
  {
    return mBindArgs;
  }
  
  final String[] getColumnNames()
  {
    return mColumnNames;
  }
  
  protected final int getConnectionFlags()
  {
    return mDatabase.getThreadDefaultConnectionFlags(mReadOnly);
  }
  
  final SQLiteDatabase getDatabase()
  {
    return mDatabase;
  }
  
  protected final SQLiteSession getSession()
  {
    return mDatabase.getThreadSession();
  }
  
  final String getSql()
  {
    return mSql;
  }
  
  @Deprecated
  public final int getUniqueId()
  {
    return -1;
  }
  
  protected void onAllReferencesReleased()
  {
    clearBindings();
  }
  
  protected final void onCorruption()
  {
    mDatabase.onCorruption();
  }
}
