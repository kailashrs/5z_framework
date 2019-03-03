package android.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.util.EmptyArray;

public class SQLiteQueryBuilder
{
  private static final String TAG = "SQLiteQueryBuilder";
  private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
  private boolean mDistinct = false;
  private SQLiteDatabase.CursorFactory mFactory = null;
  private Map<String, String> mProjectionMap = null;
  private boolean mStrict;
  private String mTables = "";
  private StringBuilder mWhereClause = null;
  
  public SQLiteQueryBuilder() {}
  
  private static void appendClause(StringBuilder paramStringBuilder, String paramString1, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString2))
    {
      paramStringBuilder.append(paramString1);
      paramStringBuilder.append(paramString2);
    }
  }
  
  public static void appendColumns(StringBuilder paramStringBuilder, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if (str != null)
      {
        if (j > 0) {
          paramStringBuilder.append(", ");
        }
        paramStringBuilder.append(str);
      }
    }
    paramStringBuilder.append(' ');
  }
  
  public static String buildQueryString(boolean paramBoolean, String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    if ((TextUtils.isEmpty(paramString3)) && (!TextUtils.isEmpty(paramString4))) {
      throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
    }
    if ((!TextUtils.isEmpty(paramString6)) && (!sLimitPattern.matcher(paramString6).matches()))
    {
      paramString1 = new StringBuilder();
      paramString1.append("invalid LIMIT clauses:");
      paramString1.append(paramString6);
      throw new IllegalArgumentException(paramString1.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder(120);
    localStringBuilder.append("SELECT ");
    if (paramBoolean) {
      localStringBuilder.append("DISTINCT ");
    }
    if ((paramArrayOfString != null) && (paramArrayOfString.length != 0)) {
      appendColumns(localStringBuilder, paramArrayOfString);
    } else {
      localStringBuilder.append("* ");
    }
    localStringBuilder.append("FROM ");
    localStringBuilder.append(paramString1);
    appendClause(localStringBuilder, " WHERE ", paramString2);
    appendClause(localStringBuilder, " GROUP BY ", paramString3);
    appendClause(localStringBuilder, " HAVING ", paramString4);
    appendClause(localStringBuilder, " ORDER BY ", paramString5);
    appendClause(localStringBuilder, " LIMIT ", paramString6);
    return localStringBuilder.toString();
  }
  
  private String[] computeProjection(String[] paramArrayOfString)
  {
    int i = 0;
    int j = 0;
    Object localObject1;
    Object localObject2;
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      if (mProjectionMap != null)
      {
        localObject1 = new String[paramArrayOfString.length];
        i = paramArrayOfString.length;
        while (j < i)
        {
          String str = paramArrayOfString[j];
          localObject2 = (String)mProjectionMap.get(str);
          if (localObject2 != null)
          {
            localObject1[j] = localObject2;
          }
          else
          {
            if ((mStrict) || ((!str.contains(" AS ")) && (!str.contains(" as ")))) {
              break label109;
            }
            localObject1[j] = str;
          }
          j++;
          continue;
          label109:
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Invalid column ");
          ((StringBuilder)localObject1).append(paramArrayOfString[j]);
          throw new IllegalArgumentException(((StringBuilder)localObject1).toString());
        }
        return localObject1;
      }
      return paramArrayOfString;
    }
    if (mProjectionMap != null)
    {
      localObject1 = mProjectionMap.entrySet();
      paramArrayOfString = new String[((Set)localObject1).size()];
      localObject2 = ((Set)localObject1).iterator();
      j = i;
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (Map.Entry)((Iterator)localObject2).next();
        if (!((String)((Map.Entry)localObject1).getKey()).equals("_count"))
        {
          paramArrayOfString[j] = ((String)((Map.Entry)localObject1).getValue());
          j++;
        }
      }
      return paramArrayOfString;
    }
    return null;
  }
  
  private String computeWhere(String paramString)
  {
    boolean bool1 = TextUtils.isEmpty(mWhereClause) ^ true;
    boolean bool2 = TextUtils.isEmpty(paramString) ^ true;
    if ((!bool1) && (!bool2)) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    if (bool1)
    {
      localStringBuilder.append('(');
      localStringBuilder.append(mWhereClause);
      localStringBuilder.append(')');
    }
    if ((bool1) && (bool2)) {
      localStringBuilder.append(" AND ");
    }
    if (bool2)
    {
      localStringBuilder.append('(');
      localStringBuilder.append(paramString);
      localStringBuilder.append(')');
    }
    return localStringBuilder.toString();
  }
  
  private String wrap(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramString;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(");
    localStringBuilder.append(paramString);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void appendWhere(CharSequence paramCharSequence)
  {
    if (mWhereClause == null) {
      mWhereClause = new StringBuilder(paramCharSequence.length() + 16);
    }
    mWhereClause.append(paramCharSequence);
  }
  
  public void appendWhereEscapeString(String paramString)
  {
    if (mWhereClause == null) {
      mWhereClause = new StringBuilder(paramString.length() + 16);
    }
    DatabaseUtils.appendEscapedSQLString(mWhereClause, paramString);
  }
  
  public String buildDelete(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(120);
    localStringBuilder.append("DELETE FROM ");
    localStringBuilder.append(mTables);
    appendClause(localStringBuilder, " WHERE ", computeWhere(paramString));
    return localStringBuilder.toString();
  }
  
  public String buildQuery(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    paramArrayOfString = computeProjection(paramArrayOfString);
    paramString1 = computeWhere(paramString1);
    return buildQueryString(mDistinct, mTables, paramArrayOfString, paramString1, paramString2, paramString3, paramString4, paramString5);
  }
  
  @Deprecated
  public String buildQuery(String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    return buildQuery(paramArrayOfString1, paramString1, paramString2, paramString3, paramString4, paramString5);
  }
  
  public String buildUnionQuery(String[] paramArrayOfString, String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    int i = paramArrayOfString.length;
    String str;
    if (mDistinct) {
      str = " UNION ";
    } else {
      str = " UNION ALL ";
    }
    for (int j = 0; j < i; j++)
    {
      if (j > 0) {
        localStringBuilder.append(str);
      }
      localStringBuilder.append(paramArrayOfString[j]);
    }
    appendClause(localStringBuilder, " ORDER BY ", paramString1);
    appendClause(localStringBuilder, " LIMIT ", paramString2);
    return localStringBuilder.toString();
  }
  
  public String buildUnionSubQuery(String paramString1, String[] paramArrayOfString, Set<String> paramSet, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    int i = paramArrayOfString.length;
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++)
    {
      Object localObject = paramArrayOfString[j];
      if (((String)localObject).equals(paramString1))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("'");
        ((StringBuilder)localObject).append(paramString2);
        ((StringBuilder)localObject).append("' AS ");
        ((StringBuilder)localObject).append(paramString1);
        arrayOfString[j] = ((StringBuilder)localObject).toString();
      }
      else if ((j > paramInt) && (!paramSet.contains(localObject)))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("NULL AS ");
        localStringBuilder.append((String)localObject);
        arrayOfString[j] = localStringBuilder.toString();
      }
      else
      {
        arrayOfString[j] = localObject;
      }
    }
    return buildQuery(arrayOfString, paramString3, paramString4, paramString5, null, null);
  }
  
  @Deprecated
  public String buildUnionSubQuery(String paramString1, String[] paramArrayOfString1, Set<String> paramSet, int paramInt, String paramString2, String paramString3, String[] paramArrayOfString2, String paramString4, String paramString5)
  {
    return buildUnionSubQuery(paramString1, paramArrayOfString1, paramSet, paramInt, paramString2, paramString3, paramString4, paramString5);
  }
  
  public String buildUpdate(ContentValues paramContentValues, String paramString)
  {
    if ((paramContentValues != null) && (paramContentValues.size() != 0))
    {
      StringBuilder localStringBuilder = new StringBuilder(120);
      localStringBuilder.append("UPDATE ");
      localStringBuilder.append(mTables);
      localStringBuilder.append(" SET ");
      paramContentValues = (String[])paramContentValues.keySet().toArray(EmptyArray.STRING);
      for (int i = 0; i < paramContentValues.length; i++)
      {
        if (i > 0) {
          localStringBuilder.append(',');
        }
        localStringBuilder.append(paramContentValues[i]);
        localStringBuilder.append("=?");
      }
      appendClause(localStringBuilder, " WHERE ", computeWhere(paramString));
      return localStringBuilder.toString();
    }
    throw new IllegalArgumentException("Empty values");
  }
  
  public int delete(SQLiteDatabase paramSQLiteDatabase, String paramString, String[] paramArrayOfString)
  {
    Objects.requireNonNull(mTables, "No tables defined");
    Objects.requireNonNull(paramSQLiteDatabase, "No database defined");
    Object localObject = buildDelete(paramString);
    if (mStrict)
    {
      paramSQLiteDatabase.validateSql((String)localObject, null);
      paramString = buildDelete(wrap(paramString));
    }
    else
    {
      paramString = (String)localObject;
    }
    if (Log.isLoggable("SQLiteQueryBuilder", 3)) {
      if (Build.IS_DEBUGGABLE)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(" with args ");
        ((StringBuilder)localObject).append(Arrays.toString(paramArrayOfString));
        Log.d("SQLiteQueryBuilder", ((StringBuilder)localObject).toString());
      }
      else
      {
        Log.d("SQLiteQueryBuilder", paramString);
      }
    }
    return paramSQLiteDatabase.executeSql(paramString, paramArrayOfString);
  }
  
  public String getTables()
  {
    return mTables;
  }
  
  public Cursor query(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, String paramString3, String paramString4)
  {
    return query(paramSQLiteDatabase, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, paramString3, paramString4, null, null);
  }
  
  public Cursor query(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    return query(paramSQLiteDatabase, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, paramString3, paramString4, paramString5, null);
  }
  
  public Cursor query(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, String paramString3, String paramString4, String paramString5, CancellationSignal paramCancellationSignal)
  {
    if (mTables == null) {
      return null;
    }
    String str = buildQuery(paramArrayOfString1, paramString1, paramString2, paramString3, paramString4, paramString5);
    if ((mStrict) && (paramString1 != null) && (paramString1.length() > 0))
    {
      paramSQLiteDatabase.validateSql(str, paramCancellationSignal);
      paramArrayOfString1 = buildQuery(paramArrayOfString1, wrap(paramString1), paramString2, paramString3, paramString4, paramString5);
    }
    else
    {
      paramArrayOfString1 = str;
    }
    if (Log.isLoggable("SQLiteQueryBuilder", 3)) {
      if (Build.IS_DEBUGGABLE)
      {
        paramString1 = new StringBuilder();
        paramString1.append(paramArrayOfString1);
        paramString1.append(" with args ");
        paramString1.append(Arrays.toString(paramArrayOfString2));
        Log.d("SQLiteQueryBuilder", paramString1.toString());
      }
      else
      {
        Log.d("SQLiteQueryBuilder", paramArrayOfString1);
      }
    }
    return paramSQLiteDatabase.rawQueryWithFactory(mFactory, paramArrayOfString1, paramArrayOfString2, SQLiteDatabase.findEditTable(mTables), paramCancellationSignal);
  }
  
  public void setCursorFactory(SQLiteDatabase.CursorFactory paramCursorFactory)
  {
    mFactory = paramCursorFactory;
  }
  
  public void setDistinct(boolean paramBoolean)
  {
    mDistinct = paramBoolean;
  }
  
  public void setProjectionMap(Map<String, String> paramMap)
  {
    mProjectionMap = paramMap;
  }
  
  public void setStrict(boolean paramBoolean)
  {
    mStrict = paramBoolean;
  }
  
  public void setTables(String paramString)
  {
    mTables = paramString;
  }
  
  public int update(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    Objects.requireNonNull(mTables, "No tables defined");
    Objects.requireNonNull(paramSQLiteDatabase, "No database defined");
    Objects.requireNonNull(paramContentValues, "No values defined");
    Object localObject = buildUpdate(paramContentValues, paramString);
    if (mStrict)
    {
      paramSQLiteDatabase.validateSql((String)localObject, null);
      paramString = buildUpdate(paramContentValues, wrap(paramString));
    }
    else
    {
      paramString = (String)localObject;
    }
    localObject = paramArrayOfString;
    if (paramArrayOfString == null) {
      localObject = EmptyArray.STRING;
    }
    String[] arrayOfString = (String[])paramContentValues.keySet().toArray(EmptyArray.STRING);
    int i = arrayOfString.length;
    paramArrayOfString = new Object[localObject.length + i];
    for (int j = 0; j < paramArrayOfString.length; j++) {
      if (j < i) {
        paramArrayOfString[j] = paramContentValues.get(arrayOfString[j]);
      } else {
        paramArrayOfString[j] = localObject[(j - i)];
      }
    }
    if (Log.isLoggable("SQLiteQueryBuilder", 3)) {
      if (Build.IS_DEBUGGABLE)
      {
        paramContentValues = new StringBuilder();
        paramContentValues.append(paramString);
        paramContentValues.append(" with args ");
        paramContentValues.append(Arrays.toString(paramArrayOfString));
        Log.d("SQLiteQueryBuilder", paramContentValues.toString());
      }
      else
      {
        Log.d("SQLiteQueryBuilder", paramString);
      }
    }
    return paramSQLiteDatabase.executeSql(paramString, paramArrayOfString);
  }
}
