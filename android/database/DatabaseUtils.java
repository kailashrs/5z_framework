package android.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteProgram;
import android.database.sqlite.SQLiteStatement;
import android.os.OperationCanceledException;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.CollationKey;
import java.text.Collator;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseUtils
{
  private static final boolean DEBUG = false;
  private static final char[] DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  public static final int STATEMENT_ABORT = 6;
  public static final int STATEMENT_ATTACH = 3;
  public static final int STATEMENT_BEGIN = 4;
  public static final int STATEMENT_COMMIT = 5;
  public static final int STATEMENT_DDL = 8;
  public static final int STATEMENT_OTHER = 99;
  public static final int STATEMENT_PRAGMA = 7;
  public static final int STATEMENT_SELECT = 1;
  public static final int STATEMENT_UNPREPARED = 9;
  public static final int STATEMENT_UPDATE = 2;
  private static final String TAG = "DatabaseUtils";
  private static Collator mColl = null;
  
  public DatabaseUtils() {}
  
  public static void appendEscapedSQLString(StringBuilder paramStringBuilder, String paramString)
  {
    paramStringBuilder.append('\'');
    if (paramString.indexOf('\'') != -1)
    {
      int i = paramString.length();
      for (int j = 0; j < i; j++)
      {
        char c = paramString.charAt(j);
        if (c == '\'') {
          paramStringBuilder.append('\'');
        }
        paramStringBuilder.append(c);
      }
    }
    else
    {
      paramStringBuilder.append(paramString);
    }
    paramStringBuilder.append('\'');
  }
  
  public static String[] appendSelectionArgs(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if ((paramArrayOfString1 != null) && (paramArrayOfString1.length != 0))
    {
      String[] arrayOfString = new String[paramArrayOfString1.length + paramArrayOfString2.length];
      System.arraycopy(paramArrayOfString1, 0, arrayOfString, 0, paramArrayOfString1.length);
      System.arraycopy(paramArrayOfString2, 0, arrayOfString, paramArrayOfString1.length, paramArrayOfString2.length);
      return arrayOfString;
    }
    return paramArrayOfString2;
  }
  
  public static final void appendValueToSql(StringBuilder paramStringBuilder, Object paramObject)
  {
    if (paramObject == null) {
      paramStringBuilder.append("NULL");
    } else if ((paramObject instanceof Boolean))
    {
      if (((Boolean)paramObject).booleanValue()) {
        paramStringBuilder.append('1');
      } else {
        paramStringBuilder.append('0');
      }
    }
    else {
      appendEscapedSQLString(paramStringBuilder, paramObject.toString());
    }
  }
  
  public static void bindObjectToProgram(SQLiteProgram paramSQLiteProgram, int paramInt, Object paramObject)
  {
    if (paramObject == null) {
      paramSQLiteProgram.bindNull(paramInt);
    } else if ((!(paramObject instanceof Double)) && (!(paramObject instanceof Float)))
    {
      if ((paramObject instanceof Number)) {
        paramSQLiteProgram.bindLong(paramInt, ((Number)paramObject).longValue());
      } else if ((paramObject instanceof Boolean))
      {
        if (((Boolean)paramObject).booleanValue()) {
          paramSQLiteProgram.bindLong(paramInt, 1L);
        } else {
          paramSQLiteProgram.bindLong(paramInt, 0L);
        }
      }
      else if ((paramObject instanceof byte[])) {
        paramSQLiteProgram.bindBlob(paramInt, (byte[])paramObject);
      } else {
        paramSQLiteProgram.bindString(paramInt, paramObject.toString());
      }
    }
    else {
      paramSQLiteProgram.bindDouble(paramInt, ((Number)paramObject).doubleValue());
    }
  }
  
  public static ParcelFileDescriptor blobFileDescriptorForQuery(SQLiteDatabase paramSQLiteDatabase, String paramString, String[] paramArrayOfString)
  {
    paramSQLiteDatabase = paramSQLiteDatabase.compileStatement(paramString);
    try
    {
      paramString = blobFileDescriptorForQuery(paramSQLiteDatabase, paramArrayOfString);
      return paramString;
    }
    finally
    {
      paramSQLiteDatabase.close();
    }
  }
  
  public static ParcelFileDescriptor blobFileDescriptorForQuery(SQLiteStatement paramSQLiteStatement, String[] paramArrayOfString)
  {
    paramSQLiteStatement.bindAllArgsAsStrings(paramArrayOfString);
    return paramSQLiteStatement.simpleQueryForBlobFileDescriptor();
  }
  
  public static String concatenateWhere(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1)) {
      return paramString2;
    }
    if (TextUtils.isEmpty(paramString2)) {
      return paramString1;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(") AND (");
    localStringBuilder.append(paramString2);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public static void createDbFromSqlStatements(Context paramContext, String paramString1, int paramInt, String paramString2)
  {
    int i = 0;
    paramContext = paramContext.openOrCreateDatabase(paramString1, 0, null);
    paramString1 = TextUtils.split(paramString2, ";\n");
    int j = paramString1.length;
    while (i < j)
    {
      paramString2 = paramString1[i];
      if (!TextUtils.isEmpty(paramString2)) {
        paramContext.execSQL(paramString2);
      }
      i++;
    }
    paramContext.setVersion(paramInt);
    paramContext.close();
  }
  
  public static void cursorDoubleToContentValues(Cursor paramCursor, String paramString1, ContentValues paramContentValues, String paramString2)
  {
    int i = paramCursor.getColumnIndex(paramString1);
    if (!paramCursor.isNull(i)) {
      paramContentValues.put(paramString2, Double.valueOf(paramCursor.getDouble(i)));
    } else {
      paramContentValues.put(paramString2, (Double)null);
    }
  }
  
  public static void cursorDoubleToContentValuesIfPresent(Cursor paramCursor, ContentValues paramContentValues, String paramString)
  {
    int i = paramCursor.getColumnIndex(paramString);
    if ((i != -1) && (!paramCursor.isNull(i))) {
      paramContentValues.put(paramString, Double.valueOf(paramCursor.getDouble(i)));
    }
  }
  
  public static void cursorDoubleToCursorValues(Cursor paramCursor, String paramString, ContentValues paramContentValues)
  {
    cursorDoubleToContentValues(paramCursor, paramString, paramContentValues, paramString);
  }
  
  public static void cursorFillWindow(Cursor paramCursor, int paramInt, CursorWindow paramCursorWindow)
  {
    if ((paramInt >= 0) && (paramInt < paramCursor.getCount()))
    {
      int i = paramCursor.getPosition();
      int j = paramCursor.getColumnCount();
      paramCursorWindow.clear();
      paramCursorWindow.setStartPosition(paramInt);
      paramCursorWindow.setNumColumns(j);
      if (paramCursor.moveToPosition(paramInt)) {
        do
        {
          if (!paramCursorWindow.allocRow()) {
            break;
          }
          for (int k = 0; k < j; k++)
          {
            int m = paramCursor.getType(k);
            Object localObject;
            boolean bool;
            if (m != 4)
            {
              switch (m)
              {
              default: 
                localObject = paramCursor.getString(k);
                if (localObject != null) {
                  bool = paramCursorWindow.putString((String)localObject, paramInt, k);
                } else {
                  bool = paramCursorWindow.putNull(paramInt, k);
                }
                break;
              case 2: 
                bool = paramCursorWindow.putDouble(paramCursor.getDouble(k), paramInt, k);
                break;
              case 1: 
                bool = paramCursorWindow.putLong(paramCursor.getLong(k), paramInt, k);
                break;
              case 0: 
                bool = paramCursorWindow.putNull(paramInt, k);
                break;
              }
            }
            else
            {
              localObject = paramCursor.getBlob(k);
              if (localObject != null) {
                bool = paramCursorWindow.putBlob((byte[])localObject, paramInt, k);
              } else {
                bool = paramCursorWindow.putNull(paramInt, k);
              }
            }
            if (!bool)
            {
              paramCursorWindow.freeLastRow();
              break label285;
            }
          }
          paramInt++;
        } while (paramCursor.moveToNext());
      }
      label285:
      paramCursor.moveToPosition(i);
      return;
    }
  }
  
  public static void cursorFloatToContentValuesIfPresent(Cursor paramCursor, ContentValues paramContentValues, String paramString)
  {
    int i = paramCursor.getColumnIndex(paramString);
    if ((i != -1) && (!paramCursor.isNull(i))) {
      paramContentValues.put(paramString, Float.valueOf(paramCursor.getFloat(i)));
    }
  }
  
  public static void cursorIntToContentValues(Cursor paramCursor, String paramString, ContentValues paramContentValues)
  {
    cursorIntToContentValues(paramCursor, paramString, paramContentValues, paramString);
  }
  
  public static void cursorIntToContentValues(Cursor paramCursor, String paramString1, ContentValues paramContentValues, String paramString2)
  {
    int i = paramCursor.getColumnIndex(paramString1);
    if (!paramCursor.isNull(i)) {
      paramContentValues.put(paramString2, Integer.valueOf(paramCursor.getInt(i)));
    } else {
      paramContentValues.put(paramString2, (Integer)null);
    }
  }
  
  public static void cursorIntToContentValuesIfPresent(Cursor paramCursor, ContentValues paramContentValues, String paramString)
  {
    int i = paramCursor.getColumnIndex(paramString);
    if ((i != -1) && (!paramCursor.isNull(i))) {
      paramContentValues.put(paramString, Integer.valueOf(paramCursor.getInt(i)));
    }
  }
  
  public static void cursorLongToContentValues(Cursor paramCursor, String paramString, ContentValues paramContentValues)
  {
    cursorLongToContentValues(paramCursor, paramString, paramContentValues, paramString);
  }
  
  public static void cursorLongToContentValues(Cursor paramCursor, String paramString1, ContentValues paramContentValues, String paramString2)
  {
    int i = paramCursor.getColumnIndex(paramString1);
    if (!paramCursor.isNull(i)) {
      paramContentValues.put(paramString2, Long.valueOf(paramCursor.getLong(i)));
    } else {
      paramContentValues.put(paramString2, (Long)null);
    }
  }
  
  public static void cursorLongToContentValuesIfPresent(Cursor paramCursor, ContentValues paramContentValues, String paramString)
  {
    int i = paramCursor.getColumnIndex(paramString);
    if ((i != -1) && (!paramCursor.isNull(i))) {
      paramContentValues.put(paramString, Long.valueOf(paramCursor.getLong(i)));
    }
  }
  
  public static int cursorPickFillWindowStartPosition(int paramInt1, int paramInt2)
  {
    return Math.max(paramInt1 - paramInt2 / 3, 0);
  }
  
  public static void cursorRowToContentValues(Cursor paramCursor, ContentValues paramContentValues)
  {
    String[] arrayOfString = paramCursor.getColumnNames();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (paramCursor.getType(j) == 4) {
        paramContentValues.put(arrayOfString[j], paramCursor.getBlob(j));
      } else {
        paramContentValues.put(arrayOfString[j], paramCursor.getString(j));
      }
    }
  }
  
  public static void cursorShortToContentValuesIfPresent(Cursor paramCursor, ContentValues paramContentValues, String paramString)
  {
    int i = paramCursor.getColumnIndex(paramString);
    if ((i != -1) && (!paramCursor.isNull(i))) {
      paramContentValues.put(paramString, Short.valueOf(paramCursor.getShort(i)));
    }
  }
  
  public static void cursorStringToContentValues(Cursor paramCursor, String paramString, ContentValues paramContentValues)
  {
    cursorStringToContentValues(paramCursor, paramString, paramContentValues, paramString);
  }
  
  public static void cursorStringToContentValues(Cursor paramCursor, String paramString1, ContentValues paramContentValues, String paramString2)
  {
    paramContentValues.put(paramString2, paramCursor.getString(paramCursor.getColumnIndexOrThrow(paramString1)));
  }
  
  public static void cursorStringToContentValuesIfPresent(Cursor paramCursor, ContentValues paramContentValues, String paramString)
  {
    int i = paramCursor.getColumnIndex(paramString);
    if ((i != -1) && (!paramCursor.isNull(i))) {
      paramContentValues.put(paramString, paramCursor.getString(i));
    }
  }
  
  public static void cursorStringToInsertHelper(Cursor paramCursor, String paramString, InsertHelper paramInsertHelper, int paramInt)
  {
    paramInsertHelper.bind(paramInt, paramCursor.getString(paramCursor.getColumnIndexOrThrow(paramString)));
  }
  
  public static void dumpCurrentRow(Cursor paramCursor)
  {
    dumpCurrentRow(paramCursor, System.out);
  }
  
  public static void dumpCurrentRow(Cursor paramCursor, PrintStream paramPrintStream)
  {
    String[] arrayOfString = paramCursor.getColumnNames();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("");
    ((StringBuilder)localObject).append(paramCursor.getPosition());
    ((StringBuilder)localObject).append(" {");
    paramPrintStream.println(((StringBuilder)localObject).toString());
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str;
      try
      {
        localObject = paramCursor.getString(j);
      }
      catch (SQLiteException localSQLiteException)
      {
        str = "<unprintable>";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("   ");
      localStringBuilder.append(arrayOfString[j]);
      localStringBuilder.append('=');
      localStringBuilder.append(str);
      paramPrintStream.println(localStringBuilder.toString());
    }
    paramPrintStream.println("}");
  }
  
  public static void dumpCurrentRow(Cursor paramCursor, StringBuilder paramStringBuilder)
  {
    String[] arrayOfString = paramCursor.getColumnNames();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("");
    ((StringBuilder)localObject).append(paramCursor.getPosition());
    ((StringBuilder)localObject).append(" {\n");
    paramStringBuilder.append(((StringBuilder)localObject).toString());
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str;
      try
      {
        localObject = paramCursor.getString(j);
      }
      catch (SQLiteException localSQLiteException)
      {
        str = "<unprintable>";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("   ");
      localStringBuilder.append(arrayOfString[j]);
      localStringBuilder.append('=');
      localStringBuilder.append(str);
      localStringBuilder.append("\n");
      paramStringBuilder.append(localStringBuilder.toString());
    }
    paramStringBuilder.append("}\n");
  }
  
  public static String dumpCurrentRowToString(Cursor paramCursor)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    dumpCurrentRow(paramCursor, localStringBuilder);
    return localStringBuilder.toString();
  }
  
  public static void dumpCursor(Cursor paramCursor)
  {
    dumpCursor(paramCursor, System.out);
  }
  
  public static void dumpCursor(Cursor paramCursor, PrintStream paramPrintStream)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(">>>>> Dumping cursor ");
    localStringBuilder.append(paramCursor);
    paramPrintStream.println(localStringBuilder.toString());
    if (paramCursor != null)
    {
      int i = paramCursor.getPosition();
      paramCursor.moveToPosition(-1);
      while (paramCursor.moveToNext()) {
        dumpCurrentRow(paramCursor, paramPrintStream);
      }
      paramCursor.moveToPosition(i);
    }
    paramPrintStream.println("<<<<<");
  }
  
  public static void dumpCursor(Cursor paramCursor, StringBuilder paramStringBuilder)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(">>>>> Dumping cursor ");
    localStringBuilder.append(paramCursor);
    localStringBuilder.append("\n");
    paramStringBuilder.append(localStringBuilder.toString());
    if (paramCursor != null)
    {
      int i = paramCursor.getPosition();
      paramCursor.moveToPosition(-1);
      while (paramCursor.moveToNext()) {
        dumpCurrentRow(paramCursor, paramStringBuilder);
      }
      paramCursor.moveToPosition(i);
    }
    paramStringBuilder.append("<<<<<\n");
  }
  
  public static String dumpCursorToString(Cursor paramCursor)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    dumpCursor(paramCursor, localStringBuilder);
    return localStringBuilder.toString();
  }
  
  private static char[] encodeHex(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    char[] arrayOfChar = new char[i << 1];
    int j = 0;
    int k = 0;
    while (j < i)
    {
      int m = k + 1;
      arrayOfChar[k] = ((char)DIGITS[((0xF0 & paramArrayOfByte[j]) >>> 4)]);
      k = m + 1;
      arrayOfChar[m] = ((char)DIGITS[(0xF & paramArrayOfByte[j])]);
      j++;
    }
    return arrayOfChar;
  }
  
  public static int findRowIdColumnIndex(String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfString[j].equals("_id")) {
        return j;
      }
    }
    return -1;
  }
  
  public static String getCollationKey(String paramString)
  {
    paramString = getCollationKeyInBytes(paramString);
    try
    {
      paramString = new String(paramString, 0, getKeyLen(paramString), "ISO8859_1");
      return paramString;
    }
    catch (Exception paramString) {}
    return "";
  }
  
  private static byte[] getCollationKeyInBytes(String paramString)
  {
    if (mColl == null)
    {
      mColl = Collator.getInstance();
      mColl.setStrength(0);
    }
    return mColl.getCollationKey(paramString).toByteArray();
  }
  
  public static String getHexCollationKey(String paramString)
  {
    paramString = getCollationKeyInBytes(paramString);
    return new String(encodeHex(paramString), 0, getKeyLen(paramString) * 2);
  }
  
  private static int getKeyLen(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte[(paramArrayOfByte.length - 1)] != 0) {
      return paramArrayOfByte.length;
    }
    return paramArrayOfByte.length - 1;
  }
  
  public static int getSqlStatementType(String paramString)
  {
    paramString = paramString.trim();
    if (paramString.length() < 3) {
      return 99;
    }
    Object localObject = paramString.substring(0, 3).toUpperCase(Locale.ROOT);
    if (((String)localObject).equals("SEL")) {
      return 1;
    }
    if ((!((String)localObject).equals("INS")) && (!((String)localObject).equals("UPD")) && (!((String)localObject).equals("REP")) && (!((String)localObject).equals("DEL")))
    {
      if (((String)localObject).equals("ATT")) {
        return 3;
      }
      if (((String)localObject).equals("COM")) {
        return 5;
      }
      if (((String)localObject).equals("END")) {
        return 5;
      }
      if (((String)localObject).equals("ROL"))
      {
        if (paramString.toUpperCase(Locale.ROOT).contains(" TO "))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Statement '");
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append("' may not work on API levels 16-27, use ';");
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append("' instead");
          Log.w("DatabaseUtils", ((StringBuilder)localObject).toString());
          return 99;
        }
        return 6;
      }
      if (((String)localObject).equals("BEG")) {
        return 4;
      }
      if (((String)localObject).equals("PRA")) {
        return 7;
      }
      if ((!((String)localObject).equals("CRE")) && (!((String)localObject).equals("DRO")) && (!((String)localObject).equals("ALT")))
      {
        if ((!((String)localObject).equals("ANA")) && (!((String)localObject).equals("DET"))) {
          return 99;
        }
        return 9;
      }
      return 8;
    }
    return 2;
  }
  
  public static int getTypeOfObject(Object paramObject)
  {
    if (paramObject == null) {
      return 0;
    }
    if ((paramObject instanceof byte[])) {
      return 4;
    }
    if ((!(paramObject instanceof Float)) && (!(paramObject instanceof Double)))
    {
      if ((!(paramObject instanceof Long)) && (!(paramObject instanceof Integer)) && (!(paramObject instanceof Short)) && (!(paramObject instanceof Byte))) {
        return 3;
      }
      return 1;
    }
    return 2;
  }
  
  public static long longForQuery(SQLiteDatabase paramSQLiteDatabase, String paramString, String[] paramArrayOfString)
  {
    paramSQLiteDatabase = paramSQLiteDatabase.compileStatement(paramString);
    try
    {
      long l = longForQuery(paramSQLiteDatabase, paramArrayOfString);
      return l;
    }
    finally
    {
      paramSQLiteDatabase.close();
    }
  }
  
  public static long longForQuery(SQLiteStatement paramSQLiteStatement, String[] paramArrayOfString)
  {
    paramSQLiteStatement.bindAllArgsAsStrings(paramArrayOfString);
    return paramSQLiteStatement.simpleQueryForLong();
  }
  
  public static boolean queryIsEmpty(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("select exists(select 1 from ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(")");
    boolean bool;
    if (longForQuery(paramSQLiteDatabase, localStringBuilder.toString(), null) == 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static long queryNumEntries(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    return queryNumEntries(paramSQLiteDatabase, paramString, null, null);
  }
  
  public static long queryNumEntries(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2)
  {
    return queryNumEntries(paramSQLiteDatabase, paramString1, paramString2, null);
  }
  
  public static long queryNumEntries(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, String[] paramArrayOfString)
  {
    if (!TextUtils.isEmpty(paramString2))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(" where ");
      localStringBuilder.append(paramString2);
      paramString2 = localStringBuilder.toString();
    }
    else
    {
      paramString2 = "";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("select count(*) from ");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(paramString2);
    return longForQuery(paramSQLiteDatabase, localStringBuilder.toString(), paramArrayOfString);
  }
  
  public static final void readExceptionFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readExceptionCode();
    if (i == 0) {
      return;
    }
    readExceptionFromParcel(paramParcel, paramParcel.readString(), i);
  }
  
  private static final void readExceptionFromParcel(Parcel paramParcel, String paramString, int paramInt)
  {
    switch (paramInt)
    {
    case 10: 
    default: 
      paramParcel.readException(paramInt, paramString);
      return;
    case 11: 
      throw new OperationCanceledException(paramString);
    case 9: 
      throw new SQLiteException(paramString);
    case 8: 
      throw new SQLiteDiskIOException(paramString);
    case 7: 
      throw new SQLiteFullException(paramString);
    case 6: 
      throw new SQLiteDatabaseCorruptException(paramString);
    case 5: 
      throw new SQLiteConstraintException(paramString);
    case 4: 
      throw new SQLiteAbortException(paramString);
    case 3: 
      throw new UnsupportedOperationException(paramString);
    }
    throw new IllegalArgumentException(paramString);
  }
  
  public static void readExceptionWithFileNotFoundExceptionFromParcel(Parcel paramParcel)
    throws FileNotFoundException
  {
    int i = paramParcel.readExceptionCode();
    if (i == 0) {
      return;
    }
    String str = paramParcel.readString();
    if (i != 1)
    {
      readExceptionFromParcel(paramParcel, str, i);
      return;
    }
    throw new FileNotFoundException(str);
  }
  
  public static void readExceptionWithOperationApplicationExceptionFromParcel(Parcel paramParcel)
    throws OperationApplicationException
  {
    int i = paramParcel.readExceptionCode();
    if (i == 0) {
      return;
    }
    String str = paramParcel.readString();
    if (i != 10)
    {
      readExceptionFromParcel(paramParcel, str, i);
      return;
    }
    throw new OperationApplicationException(str);
  }
  
  public static String sqlEscapeString(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    appendEscapedSQLString(localStringBuilder, paramString);
    return localStringBuilder.toString();
  }
  
  public static String stringForQuery(SQLiteDatabase paramSQLiteDatabase, String paramString, String[] paramArrayOfString)
  {
    paramSQLiteDatabase = paramSQLiteDatabase.compileStatement(paramString);
    try
    {
      paramString = stringForQuery(paramSQLiteDatabase, paramArrayOfString);
      return paramString;
    }
    finally
    {
      paramSQLiteDatabase.close();
    }
  }
  
  public static String stringForQuery(SQLiteStatement paramSQLiteStatement, String[] paramArrayOfString)
  {
    paramSQLiteStatement.bindAllArgsAsStrings(paramArrayOfString);
    return paramSQLiteStatement.simpleQueryForString();
  }
  
  public static final void writeExceptionToParcel(Parcel paramParcel, Exception paramException)
  {
    int i = 1;
    int j;
    if ((paramException instanceof FileNotFoundException))
    {
      j = 1;
      i = 0;
    }
    else if ((paramException instanceof IllegalArgumentException))
    {
      j = 2;
    }
    else if ((paramException instanceof UnsupportedOperationException))
    {
      j = 3;
    }
    else if ((paramException instanceof SQLiteAbortException))
    {
      j = 4;
    }
    else if ((paramException instanceof SQLiteConstraintException))
    {
      j = 5;
    }
    else if ((paramException instanceof SQLiteDatabaseCorruptException))
    {
      j = 6;
    }
    else if ((paramException instanceof SQLiteFullException))
    {
      j = 7;
    }
    else if ((paramException instanceof SQLiteDiskIOException))
    {
      j = 8;
    }
    else if ((paramException instanceof SQLiteException))
    {
      j = 9;
    }
    else if ((paramException instanceof OperationApplicationException))
    {
      j = 10;
    }
    else
    {
      if (!(paramException instanceof OperationCanceledException)) {
        break label169;
      }
      j = 11;
      i = 0;
    }
    paramParcel.writeInt(j);
    paramParcel.writeString(paramException.getMessage());
    if (i != 0) {
      Log.e("DatabaseUtils", "Writing exception to parcel", paramException);
    }
    return;
    label169:
    paramParcel.writeException(paramException);
    Log.e("DatabaseUtils", "Writing exception to parcel", paramException);
  }
  
  @Deprecated
  public static class InsertHelper
  {
    public static final int TABLE_INFO_PRAGMA_COLUMNNAME_INDEX = 1;
    public static final int TABLE_INFO_PRAGMA_DEFAULT_INDEX = 4;
    private HashMap<String, Integer> mColumns;
    private final SQLiteDatabase mDb;
    private String mInsertSQL = null;
    private SQLiteStatement mInsertStatement = null;
    private SQLiteStatement mPreparedStatement = null;
    private SQLiteStatement mReplaceStatement = null;
    private final String mTableName;
    
    public InsertHelper(SQLiteDatabase paramSQLiteDatabase, String paramString)
    {
      mDb = paramSQLiteDatabase;
      mTableName = paramString;
    }
    
    private void buildSQL()
      throws SQLException
    {
      StringBuilder localStringBuilder1 = new StringBuilder(128);
      localStringBuilder1.append("INSERT INTO ");
      localStringBuilder1.append(mTableName);
      localStringBuilder1.append(" (");
      StringBuilder localStringBuilder2 = new StringBuilder(128);
      localStringBuilder2.append("VALUES (");
      int i = 1;
      Object localObject1 = null;
      Object localObject3 = localObject1;
      try
      {
        Object localObject4 = mDb;
        localObject3 = localObject1;
        Object localObject5 = new java/lang/StringBuilder;
        localObject3 = localObject1;
        ((StringBuilder)localObject5).<init>();
        localObject3 = localObject1;
        ((StringBuilder)localObject5).append("PRAGMA table_info(");
        localObject3 = localObject1;
        ((StringBuilder)localObject5).append(mTableName);
        localObject3 = localObject1;
        ((StringBuilder)localObject5).append(")");
        localObject3 = localObject1;
        localObject4 = ((SQLiteDatabase)localObject4).rawQuery(((StringBuilder)localObject5).toString(), null);
        localObject3 = localObject4;
        localObject1 = new java/util/HashMap;
        localObject3 = localObject4;
        ((HashMap)localObject1).<init>(((Cursor)localObject4).getCount());
        localObject3 = localObject4;
        mColumns = ((HashMap)localObject1);
        for (;;)
        {
          localObject3 = localObject4;
          if (!((Cursor)localObject4).moveToNext()) {
            break;
          }
          localObject3 = localObject4;
          localObject5 = ((Cursor)localObject4).getString(1);
          localObject3 = localObject4;
          localObject1 = ((Cursor)localObject4).getString(4);
          localObject3 = localObject4;
          mColumns.put(localObject5, Integer.valueOf(i));
          localObject3 = localObject4;
          localStringBuilder1.append("'");
          localObject3 = localObject4;
          localStringBuilder1.append((String)localObject5);
          localObject3 = localObject4;
          localStringBuilder1.append("'");
          if (localObject1 == null)
          {
            localObject3 = localObject4;
            localStringBuilder2.append("?");
          }
          else
          {
            localObject3 = localObject4;
            localStringBuilder2.append("COALESCE(?, ");
            localObject3 = localObject4;
            localStringBuilder2.append((String)localObject1);
            localObject3 = localObject4;
            localStringBuilder2.append(")");
          }
          localObject3 = localObject4;
          if (i == ((Cursor)localObject4).getCount()) {
            localObject1 = ") ";
          } else {
            localObject1 = ", ";
          }
          localObject3 = localObject4;
          localStringBuilder1.append((String)localObject1);
          localObject3 = localObject4;
          if (i == ((Cursor)localObject4).getCount()) {
            localObject1 = ");";
          } else {
            localObject1 = ", ";
          }
          localObject3 = localObject4;
          localStringBuilder2.append((String)localObject1);
          i++;
        }
        if (localObject4 != null) {
          ((Cursor)localObject4).close();
        }
        localStringBuilder1.append(localStringBuilder2);
        mInsertSQL = localStringBuilder1.toString();
        return;
      }
      finally
      {
        if (localObject3 != null) {
          localObject3.close();
        }
      }
    }
    
    private SQLiteStatement getStatement(boolean paramBoolean)
      throws SQLException
    {
      if (paramBoolean)
      {
        if (mReplaceStatement == null)
        {
          if (mInsertSQL == null) {
            buildSQL();
          }
          Object localObject = new StringBuilder();
          ((StringBuilder)localObject).append("INSERT OR REPLACE");
          ((StringBuilder)localObject).append(mInsertSQL.substring(6));
          localObject = ((StringBuilder)localObject).toString();
          mReplaceStatement = mDb.compileStatement((String)localObject);
        }
        return mReplaceStatement;
      }
      if (mInsertStatement == null)
      {
        if (mInsertSQL == null) {
          buildSQL();
        }
        mInsertStatement = mDb.compileStatement(mInsertSQL);
      }
      return mInsertStatement;
    }
    
    /* Error */
    private long insertInternal(ContentValues paramContentValues, boolean paramBoolean)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 40	android/database/DatabaseUtils$InsertHelper:mDb	Landroid/database/sqlite/SQLiteDatabase;
      //   4: invokevirtual 144	android/database/sqlite/SQLiteDatabase:beginTransactionNonExclusive	()V
      //   7: aload_0
      //   8: iload_2
      //   9: invokespecial 146	android/database/DatabaseUtils$InsertHelper:getStatement	(Z)Landroid/database/sqlite/SQLiteStatement;
      //   12: astore_3
      //   13: aload_3
      //   14: invokevirtual 151	android/database/sqlite/SQLiteStatement:clearBindings	()V
      //   17: aload_1
      //   18: invokevirtual 157	android/content/ContentValues:valueSet	()Ljava/util/Set;
      //   21: invokeinterface 163 1 0
      //   26: astore 4
      //   28: aload 4
      //   30: invokeinterface 168 1 0
      //   35: ifeq +43 -> 78
      //   38: aload 4
      //   40: invokeinterface 172 1 0
      //   45: checkcast 174	java/util/Map$Entry
      //   48: astore 5
      //   50: aload_3
      //   51: aload_0
      //   52: aload 5
      //   54: invokeinterface 177 1 0
      //   59: checkcast 132	java/lang/String
      //   62: invokevirtual 181	android/database/DatabaseUtils$InsertHelper:getColumnIndex	(Ljava/lang/String;)I
      //   65: aload 5
      //   67: invokeinterface 184 1 0
      //   72: invokestatic 188	android/database/DatabaseUtils:bindObjectToProgram	(Landroid/database/sqlite/SQLiteProgram;ILjava/lang/Object;)V
      //   75: goto -47 -> 28
      //   78: aload_3
      //   79: invokevirtual 192	android/database/sqlite/SQLiteStatement:executeInsert	()J
      //   82: lstore 6
      //   84: aload_0
      //   85: getfield 40	android/database/DatabaseUtils$InsertHelper:mDb	Landroid/database/sqlite/SQLiteDatabase;
      //   88: invokevirtual 195	android/database/sqlite/SQLiteDatabase:setTransactionSuccessful	()V
      //   91: aload_0
      //   92: getfield 40	android/database/DatabaseUtils$InsertHelper:mDb	Landroid/database/sqlite/SQLiteDatabase;
      //   95: invokevirtual 198	android/database/sqlite/SQLiteDatabase:endTransaction	()V
      //   98: lload 6
      //   100: lreturn
      //   101: astore_1
      //   102: goto +72 -> 174
      //   105: astore 5
      //   107: new 48	java/lang/StringBuilder
      //   110: astore 4
      //   112: aload 4
      //   114: invokespecial 62	java/lang/StringBuilder:<init>	()V
      //   117: aload 4
      //   119: ldc -56
      //   121: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   124: pop
      //   125: aload 4
      //   127: aload_1
      //   128: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   131: pop
      //   132: aload 4
      //   134: ldc -51
      //   136: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   139: pop
      //   140: aload 4
      //   142: aload_0
      //   143: getfield 42	android/database/DatabaseUtils$InsertHelper:mTableName	Ljava/lang/String;
      //   146: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   149: pop
      //   150: ldc -49
      //   152: aload 4
      //   154: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   157: aload 5
      //   159: invokestatic 213	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   162: pop
      //   163: aload_0
      //   164: getfield 40	android/database/DatabaseUtils$InsertHelper:mDb	Landroid/database/sqlite/SQLiteDatabase;
      //   167: invokevirtual 198	android/database/sqlite/SQLiteDatabase:endTransaction	()V
      //   170: ldc2_w 214
      //   173: lreturn
      //   174: aload_0
      //   175: getfield 40	android/database/DatabaseUtils$InsertHelper:mDb	Landroid/database/sqlite/SQLiteDatabase;
      //   178: invokevirtual 198	android/database/sqlite/SQLiteDatabase:endTransaction	()V
      //   181: aload_1
      //   182: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	183	0	this	InsertHelper
      //   0	183	1	paramContentValues	ContentValues
      //   0	183	2	paramBoolean	boolean
      //   12	67	3	localSQLiteStatement	SQLiteStatement
      //   26	127	4	localObject	Object
      //   48	18	5	localEntry	java.util.Map.Entry
      //   105	53	5	localSQLException	SQLException
      //   82	17	6	l	long
      // Exception table:
      //   from	to	target	type
      //   7	28	101	finally
      //   28	75	101	finally
      //   78	91	101	finally
      //   107	163	101	finally
      //   7	28	105	android/database/SQLException
      //   28	75	105	android/database/SQLException
      //   78	91	105	android/database/SQLException
    }
    
    public void bind(int paramInt, double paramDouble)
    {
      mPreparedStatement.bindDouble(paramInt, paramDouble);
    }
    
    public void bind(int paramInt, float paramFloat)
    {
      mPreparedStatement.bindDouble(paramInt, paramFloat);
    }
    
    public void bind(int paramInt1, int paramInt2)
    {
      mPreparedStatement.bindLong(paramInt1, paramInt2);
    }
    
    public void bind(int paramInt, long paramLong)
    {
      mPreparedStatement.bindLong(paramInt, paramLong);
    }
    
    public void bind(int paramInt, String paramString)
    {
      if (paramString == null) {
        mPreparedStatement.bindNull(paramInt);
      } else {
        mPreparedStatement.bindString(paramInt, paramString);
      }
    }
    
    public void bind(int paramInt, boolean paramBoolean)
    {
      SQLiteStatement localSQLiteStatement = mPreparedStatement;
      long l;
      if (paramBoolean) {
        l = 1L;
      } else {
        l = 0L;
      }
      localSQLiteStatement.bindLong(paramInt, l);
    }
    
    public void bind(int paramInt, byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte == null) {
        mPreparedStatement.bindNull(paramInt);
      } else {
        mPreparedStatement.bindBlob(paramInt, paramArrayOfByte);
      }
    }
    
    public void bindNull(int paramInt)
    {
      mPreparedStatement.bindNull(paramInt);
    }
    
    public void close()
    {
      if (mInsertStatement != null)
      {
        mInsertStatement.close();
        mInsertStatement = null;
      }
      if (mReplaceStatement != null)
      {
        mReplaceStatement.close();
        mReplaceStatement = null;
      }
      mInsertSQL = null;
      mColumns = null;
    }
    
    /* Error */
    public long execute()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 38	android/database/DatabaseUtils$InsertHelper:mPreparedStatement	Landroid/database/sqlite/SQLiteStatement;
      //   4: ifnull +76 -> 80
      //   7: aload_0
      //   8: getfield 38	android/database/DatabaseUtils$InsertHelper:mPreparedStatement	Landroid/database/sqlite/SQLiteStatement;
      //   11: invokevirtual 192	android/database/sqlite/SQLiteStatement:executeInsert	()J
      //   14: lstore_1
      //   15: aload_0
      //   16: aconst_null
      //   17: putfield 38	android/database/DatabaseUtils$InsertHelper:mPreparedStatement	Landroid/database/sqlite/SQLiteStatement;
      //   20: lload_1
      //   21: lreturn
      //   22: astore_3
      //   23: goto +50 -> 73
      //   26: astore 4
      //   28: new 48	java/lang/StringBuilder
      //   31: astore_3
      //   32: aload_3
      //   33: invokespecial 62	java/lang/StringBuilder:<init>	()V
      //   36: aload_3
      //   37: ldc -14
      //   39: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   42: pop
      //   43: aload_3
      //   44: aload_0
      //   45: getfield 42	android/database/DatabaseUtils$InsertHelper:mTableName	Ljava/lang/String;
      //   48: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   51: pop
      //   52: ldc -49
      //   54: aload_3
      //   55: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   58: aload 4
      //   60: invokestatic 213	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   63: pop
      //   64: aload_0
      //   65: aconst_null
      //   66: putfield 38	android/database/DatabaseUtils$InsertHelper:mPreparedStatement	Landroid/database/sqlite/SQLiteStatement;
      //   69: ldc2_w 214
      //   72: lreturn
      //   73: aload_0
      //   74: aconst_null
      //   75: putfield 38	android/database/DatabaseUtils$InsertHelper:mPreparedStatement	Landroid/database/sqlite/SQLiteStatement;
      //   78: aload_3
      //   79: athrow
      //   80: new 244	java/lang/IllegalStateException
      //   83: dup
      //   84: ldc -10
      //   86: invokespecial 249	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
      //   89: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	90	0	this	InsertHelper
      //   14	7	1	l	long
      //   22	1	3	localObject	Object
      //   31	48	3	localStringBuilder	StringBuilder
      //   26	33	4	localSQLException	SQLException
      // Exception table:
      //   from	to	target	type
      //   7	15	22	finally
      //   28	64	22	finally
      //   7	15	26	android/database/SQLException
    }
    
    public int getColumnIndex(String paramString)
    {
      getStatement(false);
      Object localObject = (Integer)mColumns.get(paramString);
      if (localObject != null) {
        return ((Integer)localObject).intValue();
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("column '");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("' is invalid");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    
    public long insert(ContentValues paramContentValues)
    {
      return insertInternal(paramContentValues, false);
    }
    
    public void prepareForInsert()
    {
      mPreparedStatement = getStatement(false);
      mPreparedStatement.clearBindings();
    }
    
    public void prepareForReplace()
    {
      mPreparedStatement = getStatement(true);
      mPreparedStatement.clearBindings();
    }
    
    public long replace(ContentValues paramContentValues)
    {
      return insertInternal(paramContentValues, true);
    }
  }
}
