package android.provider;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.SeempLog;
import android.webkit.WebIconDatabase.IconListener;

public class Browser
{
  public static final Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");
  public static final String EXTRA_APPLICATION_ID = "com.android.browser.application_id";
  public static final String EXTRA_CREATE_NEW_TAB = "create_new_tab";
  public static final String EXTRA_HEADERS = "com.android.browser.headers";
  public static final String EXTRA_SHARE_FAVICON = "share_favicon";
  public static final String EXTRA_SHARE_SCREENSHOT = "share_screenshot";
  public static final String[] HISTORY_PROJECTION = { "_id", "url", "visits", "date", "bookmark", "title", "favicon", "thumbnail", "touch_icon", "user_entered" };
  public static final int HISTORY_PROJECTION_BOOKMARK_INDEX = 4;
  public static final int HISTORY_PROJECTION_DATE_INDEX = 3;
  public static final int HISTORY_PROJECTION_FAVICON_INDEX = 6;
  public static final int HISTORY_PROJECTION_ID_INDEX = 0;
  public static final int HISTORY_PROJECTION_THUMBNAIL_INDEX = 7;
  public static final int HISTORY_PROJECTION_TITLE_INDEX = 5;
  public static final int HISTORY_PROJECTION_TOUCH_ICON_INDEX = 8;
  public static final int HISTORY_PROJECTION_URL_INDEX = 1;
  public static final int HISTORY_PROJECTION_VISITS_INDEX = 2;
  public static final String INITIAL_ZOOM_LEVEL = "browser.initialZoomLevel";
  private static final String LOGTAG = "browser";
  private static final int MAX_HISTORY_COUNT = 250;
  public static final String[] SEARCHES_PROJECTION = { "_id", "search", "date" };
  public static final int SEARCHES_PROJECTION_DATE_INDEX = 2;
  public static final int SEARCHES_PROJECTION_SEARCH_INDEX = 1;
  public static final Uri SEARCHES_URI;
  public static final String[] TRUNCATE_HISTORY_PROJECTION = { "_id", "date" };
  public static final int TRUNCATE_HISTORY_PROJECTION_ID_INDEX = 0;
  public static final int TRUNCATE_N_OLDEST = 5;
  
  static
  {
    SEARCHES_URI = Uri.parse("content://browser/searches");
  }
  
  public Browser() {}
  
  private static final void addOrUrlEquals(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append(" OR url = ");
  }
  
  public static final void addSearchUrl(ContentResolver paramContentResolver, String paramString) {}
  
  public static final boolean canClearHistory(ContentResolver paramContentResolver)
  {
    return false;
  }
  
  public static final void clearHistory(ContentResolver paramContentResolver)
  {
    SeempLog.record(37);
  }
  
  public static final void clearSearches(ContentResolver paramContentResolver) {}
  
  public static final void deleteFromHistory(ContentResolver paramContentResolver, String paramString) {}
  
  public static final void deleteHistoryTimeFrame(ContentResolver paramContentResolver, long paramLong1, long paramLong2) {}
  
  public static final Cursor getAllBookmarks(ContentResolver paramContentResolver)
    throws IllegalStateException
  {
    SeempLog.record(32);
    return new MatrixCursor(new String[] { "url" }, 0);
  }
  
  public static final Cursor getAllVisitedUrls(ContentResolver paramContentResolver)
    throws IllegalStateException
  {
    SeempLog.record(33);
    return new MatrixCursor(new String[] { "url" }, 0);
  }
  
  @Deprecated
  public static final String[] getVisitedHistory(ContentResolver paramContentResolver)
  {
    SeempLog.record(35);
    return new String[0];
  }
  
  private static final Cursor getVisitedLike(ContentResolver paramContentResolver, String paramString)
  {
    SeempLog.record(34);
    int i = 0;
    Object localObject1 = paramString;
    if (((String)localObject1).startsWith("http://"))
    {
      paramString = ((String)localObject1).substring(7);
    }
    else
    {
      paramString = (String)localObject1;
      if (((String)localObject1).startsWith("https://"))
      {
        paramString = ((String)localObject1).substring(8);
        i = 1;
      }
    }
    localObject1 = paramString;
    if (paramString.startsWith("www.")) {
      localObject1 = paramString.substring(4);
    }
    Object localObject2;
    if (i != 0)
    {
      paramString = new StringBuilder("url = ");
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("https://");
      ((StringBuilder)localObject2).append((String)localObject1);
      DatabaseUtils.appendEscapedSQLString(paramString, ((StringBuilder)localObject2).toString());
      addOrUrlEquals(paramString);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("https://www.");
      ((StringBuilder)localObject2).append((String)localObject1);
      DatabaseUtils.appendEscapedSQLString(paramString, ((StringBuilder)localObject2).toString());
    }
    else
    {
      paramString = new StringBuilder("url = ");
      DatabaseUtils.appendEscapedSQLString(paramString, (String)localObject1);
      addOrUrlEquals(paramString);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("www.");
      ((StringBuilder)localObject2).append((String)localObject1);
      localObject2 = ((StringBuilder)localObject2).toString();
      DatabaseUtils.appendEscapedSQLString(paramString, (String)localObject2);
      addOrUrlEquals(paramString);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://");
      localStringBuilder.append((String)localObject1);
      DatabaseUtils.appendEscapedSQLString(paramString, localStringBuilder.toString());
      addOrUrlEquals(paramString);
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("http://");
      ((StringBuilder)localObject1).append((String)localObject2);
      DatabaseUtils.appendEscapedSQLString(paramString, ((StringBuilder)localObject1).toString());
    }
    localObject1 = BrowserContract.History.CONTENT_URI;
    paramString = paramString.toString();
    return paramContentResolver.query((Uri)localObject1, new String[] { "_id", "visits" }, paramString, null, null);
  }
  
  public static final void requestAllIcons(ContentResolver paramContentResolver, String paramString, WebIconDatabase.IconListener paramIconListener)
  {
    SeempLog.record(36);
  }
  
  public static final void saveBookmark(Context paramContext, String paramString1, String paramString2) {}
  
  public static final void sendString(Context paramContext, String paramString)
  {
    sendString(paramContext, paramString, paramContext.getString(17040967));
  }
  
  public static final void sendString(Context paramContext, String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("text/plain");
    localIntent.putExtra("android.intent.extra.TEXT", paramString1);
    try
    {
      paramString1 = Intent.createChooser(localIntent, paramString2);
      paramString1.setFlags(268435456);
      paramContext.startActivity(paramString1);
    }
    catch (ActivityNotFoundException paramContext) {}
  }
  
  public static final void truncateHistory(ContentResolver paramContentResolver) {}
  
  public static final void updateVisitedHistory(ContentResolver paramContentResolver, String paramString, boolean paramBoolean) {}
  
  public static class BookmarkColumns
    implements BaseColumns
  {
    public static final String BOOKMARK = "bookmark";
    public static final String CREATED = "created";
    public static final String DATE = "date";
    public static final String FAVICON = "favicon";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TITLE = "title";
    public static final String TOUCH_ICON = "touch_icon";
    public static final String URL = "url";
    public static final String USER_ENTERED = "user_entered";
    public static final String VISITS = "visits";
    
    public BookmarkColumns() {}
  }
  
  public static class SearchColumns
    implements BaseColumns
  {
    public static final String DATE = "date";
    public static final String SEARCH = "search";
    @Deprecated
    public static final String URL = "url";
    
    public SearchColumns() {}
  }
}
