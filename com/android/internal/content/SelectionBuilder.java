package com.android.internal.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;

public class SelectionBuilder
{
  private StringBuilder mSelection = new StringBuilder();
  private ArrayList<String> mSelectionArgs = new ArrayList();
  
  public SelectionBuilder() {}
  
  public SelectionBuilder append(String paramString, Object... paramVarArgs)
  {
    if (TextUtils.isEmpty(paramString))
    {
      if ((paramVarArgs != null) && (paramVarArgs.length > 0)) {
        throw new IllegalArgumentException("Valid selection required when including arguments");
      }
      return this;
    }
    if (mSelection.length() > 0) {
      mSelection.append(" AND ");
    }
    StringBuilder localStringBuilder = mSelection;
    localStringBuilder.append("(");
    localStringBuilder.append(paramString);
    localStringBuilder.append(")");
    if (paramVarArgs != null)
    {
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++)
      {
        paramString = paramVarArgs[j];
        mSelectionArgs.add(String.valueOf(paramString));
      }
    }
    return this;
  }
  
  public int delete(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    return paramSQLiteDatabase.delete(paramString, getSelection(), getSelectionArgs());
  }
  
  public String getSelection()
  {
    return mSelection.toString();
  }
  
  public String[] getSelectionArgs()
  {
    return (String[])mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
  }
  
  public Cursor query(SQLiteDatabase paramSQLiteDatabase, String paramString1, String[] paramArrayOfString, String paramString2)
  {
    return query(paramSQLiteDatabase, paramString1, paramArrayOfString, null, null, paramString2, null);
  }
  
  public Cursor query(SQLiteDatabase paramSQLiteDatabase, String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    return paramSQLiteDatabase.query(paramString1, paramArrayOfString, getSelection(), getSelectionArgs(), paramString2, paramString3, paramString4, paramString5);
  }
  
  public SelectionBuilder reset()
  {
    mSelection.setLength(0);
    mSelectionArgs.clear();
    return this;
  }
  
  public int update(SQLiteDatabase paramSQLiteDatabase, String paramString, ContentValues paramContentValues)
  {
    return paramSQLiteDatabase.update(paramString, paramContentValues, getSelection(), getSelectionArgs());
  }
}
