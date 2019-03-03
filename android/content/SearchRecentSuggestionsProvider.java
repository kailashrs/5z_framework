package android.content;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;

public class SearchRecentSuggestionsProvider
  extends ContentProvider
{
  public static final int DATABASE_MODE_2LINES = 2;
  public static final int DATABASE_MODE_QUERIES = 1;
  private static final int DATABASE_VERSION = 512;
  private static final String NULL_COLUMN = "query";
  private static final String ORDER_BY = "date DESC";
  private static final String TAG = "SuggestionsProvider";
  private static final int URI_MATCH_SUGGEST = 1;
  private static final String sDatabaseName = "suggestions.db";
  private static final String sSuggestions = "suggestions";
  private String mAuthority;
  private int mMode;
  private SQLiteOpenHelper mOpenHelper;
  private String mSuggestSuggestionClause;
  private String[] mSuggestionProjection;
  private Uri mSuggestionsUri;
  private boolean mTwoLineDisplay;
  private UriMatcher mUriMatcher;
  
  public SearchRecentSuggestionsProvider() {}
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = mOpenHelper.getWritableDatabase();
    if (paramUri.getPathSegments().size() == 1)
    {
      if (((String)paramUri.getPathSegments().get(0)).equals("suggestions"))
      {
        int i = localSQLiteDatabase.delete("suggestions", paramString, paramArrayOfString);
        getContext().getContentResolver().notifyChange(paramUri, null);
        return i;
      }
      throw new IllegalArgumentException("Unknown Uri");
    }
    throw new IllegalArgumentException("Unknown Uri");
  }
  
  public String getType(Uri paramUri)
  {
    if (mUriMatcher.match(paramUri) == 1) {
      return "vnd.android.cursor.dir/vnd.android.search.suggest";
    }
    int i = paramUri.getPathSegments().size();
    if ((i >= 1) && (((String)paramUri.getPathSegments().get(0)).equals("suggestions")))
    {
      if (i == 1) {
        return "vnd.android.cursor.dir/suggestion";
      }
      if (i == 2) {
        return "vnd.android.cursor.item/suggestion";
      }
    }
    throw new IllegalArgumentException("Unknown Uri");
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    SQLiteDatabase localSQLiteDatabase = mOpenHelper.getWritableDatabase();
    int i = paramUri.getPathSegments().size();
    if (i >= 1)
    {
      long l1 = -1L;
      String str = (String)paramUri.getPathSegments().get(0);
      Object localObject = null;
      long l2 = l1;
      paramUri = localObject;
      if (str.equals("suggestions"))
      {
        l2 = l1;
        paramUri = localObject;
        if (i == 1)
        {
          l1 = localSQLiteDatabase.insert("suggestions", "query", paramContentValues);
          l2 = l1;
          paramUri = localObject;
          if (l1 > 0L)
          {
            paramUri = Uri.withAppendedPath(mSuggestionsUri, String.valueOf(l1));
            l2 = l1;
          }
        }
      }
      if (l2 >= 0L)
      {
        getContext().getContentResolver().notifyChange(paramUri, null);
        return paramUri;
      }
      throw new IllegalArgumentException("Unknown Uri");
    }
    throw new IllegalArgumentException("Unknown Uri");
  }
  
  public boolean onCreate()
  {
    if ((mAuthority != null) && (mMode != 0))
    {
      int i = mMode;
      mOpenHelper = new DatabaseHelper(getContext(), 512 + i);
      return true;
    }
    throw new IllegalArgumentException("Provider not configured");
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    SQLiteDatabase localSQLiteDatabase = mOpenHelper.getReadableDatabase();
    if (mUriMatcher.match(paramUri) == 1)
    {
      if (TextUtils.isEmpty(paramArrayOfString2[0]))
      {
        paramString1 = null;
        paramArrayOfString1 = null;
      }
      for (;;)
      {
        break;
        paramArrayOfString1 = new StringBuilder();
        paramArrayOfString1.append("%");
        paramArrayOfString1.append(paramArrayOfString2[0]);
        paramArrayOfString1.append("%");
        paramString1 = paramArrayOfString1.toString();
        if (mTwoLineDisplay)
        {
          paramArrayOfString1 = new String[2];
          paramArrayOfString1[0] = paramString1;
          paramArrayOfString1[1] = paramString1;
        }
        else
        {
          paramArrayOfString1 = new String[1];
          paramArrayOfString1[0] = paramString1;
        }
        paramString1 = mSuggestSuggestionClause;
      }
      paramArrayOfString1 = localSQLiteDatabase.query("suggestions", mSuggestionProjection, paramString1, paramArrayOfString1, null, null, "date DESC", null);
      paramArrayOfString1.setNotificationUri(getContext().getContentResolver(), paramUri);
      return paramArrayOfString1;
    }
    int i = paramUri.getPathSegments().size();
    if ((i != 1) && (i != 2)) {
      throw new IllegalArgumentException("Unknown Uri");
    }
    String str = (String)paramUri.getPathSegments().get(0);
    if (str.equals("suggestions"))
    {
      Object localObject1 = null;
      Object localObject2 = localObject1;
      if (paramArrayOfString1 != null)
      {
        localObject2 = localObject1;
        if (paramArrayOfString1.length > 0)
        {
          localObject2 = new String[paramArrayOfString1.length + 1];
          System.arraycopy(paramArrayOfString1, 0, localObject2, 0, paramArrayOfString1.length);
          localObject2[paramArrayOfString1.length] = "_id AS _id";
        }
      }
      paramArrayOfString1 = new StringBuilder(256);
      if (i == 2)
      {
        paramArrayOfString1.append("(_id = ");
        paramArrayOfString1.append((String)paramUri.getPathSegments().get(1));
        paramArrayOfString1.append(")");
      }
      if ((paramString1 != null) && (paramString1.length() > 0))
      {
        if (paramArrayOfString1.length() > 0) {
          paramArrayOfString1.append(" AND ");
        }
        paramArrayOfString1.append('(');
        paramArrayOfString1.append(paramString1);
        paramArrayOfString1.append(')');
      }
      paramArrayOfString1 = localSQLiteDatabase.query(str, (String[])localObject2, paramArrayOfString1.toString(), paramArrayOfString2, null, null, paramString2, null);
      paramArrayOfString1.setNotificationUri(getContext().getContentResolver(), paramUri);
      return paramArrayOfString1;
    }
    throw new IllegalArgumentException("Unknown Uri");
  }
  
  protected void setupSuggestions(String paramString, int paramInt)
  {
    if ((!TextUtils.isEmpty(paramString)) && ((paramInt & 0x1) != 0))
    {
      boolean bool;
      if ((paramInt & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mTwoLineDisplay = bool;
      mAuthority = new String(paramString);
      mMode = paramInt;
      paramString = new StringBuilder();
      paramString.append("content://");
      paramString.append(mAuthority);
      paramString.append("/suggestions");
      mSuggestionsUri = Uri.parse(paramString.toString());
      mUriMatcher = new UriMatcher(-1);
      mUriMatcher.addURI(mAuthority, "search_suggest_query", 1);
      if (mTwoLineDisplay)
      {
        mSuggestSuggestionClause = "display1 LIKE ? OR display2 LIKE ?";
        mSuggestionProjection = new String[] { "0 AS suggest_format", "'android.resource://system/17301578' AS suggest_icon_1", "display1 AS suggest_text_1", "display2 AS suggest_text_2", "query AS suggest_intent_query", "_id" };
      }
      else
      {
        mSuggestSuggestionClause = "display1 LIKE ?";
        mSuggestionProjection = new String[] { "0 AS suggest_format", "'android.resource://system/17301578' AS suggest_icon_1", "display1 AS suggest_text_1", "query AS suggest_intent_query", "_id" };
      }
      return;
    }
    throw new IllegalArgumentException();
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Not implemented");
  }
  
  private static class DatabaseHelper
    extends SQLiteOpenHelper
  {
    private int mNewVersion;
    
    public DatabaseHelper(Context paramContext, int paramInt)
    {
      super("suggestions.db", null, paramInt);
      mNewVersion = paramInt;
    }
    
    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CREATE TABLE suggestions (_id INTEGER PRIMARY KEY,display1 TEXT UNIQUE ON CONFLICT REPLACE");
      if ((mNewVersion & 0x2) != 0) {
        localStringBuilder.append(",display2 TEXT");
      }
      localStringBuilder.append(",query TEXT,date LONG);");
      paramSQLiteDatabase.execSQL(localStringBuilder.toString());
    }
    
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Upgrading database from version ");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(", which will destroy all old data");
      Log.w("SuggestionsProvider", localStringBuilder.toString());
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS suggestions");
      onCreate(paramSQLiteDatabase);
    }
  }
}
