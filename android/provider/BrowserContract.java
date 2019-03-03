package android.provider;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.RemoteException;
import android.util.Pair;

public class BrowserContract
{
  public static final String AUTHORITY = "com.android.browser";
  public static final Uri AUTHORITY_URI = Uri.parse("content://com.android.browser");
  public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";
  public static final String PARAM_LIMIT = "limit";
  
  public BrowserContract() {}
  
  public static final class Accounts
  {
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final Uri CONTENT_URI = BrowserContract.AUTHORITY_URI.buildUpon().appendPath("accounts").build();
    public static final String ROOT_ID = "root_id";
    
    public Accounts() {}
  }
  
  static abstract interface BaseSyncColumns
  {
    public static final String SYNC1 = "sync1";
    public static final String SYNC2 = "sync2";
    public static final String SYNC3 = "sync3";
    public static final String SYNC4 = "sync4";
    public static final String SYNC5 = "sync5";
  }
  
  public static final class Bookmarks
    implements BrowserContract.CommonColumns, BrowserContract.ImageColumns, BrowserContract.SyncColumns
  {
    public static final int BOOKMARK_TYPE_BOOKMARK = 1;
    public static final int BOOKMARK_TYPE_BOOKMARK_BAR_FOLDER = 3;
    public static final int BOOKMARK_TYPE_FOLDER = 2;
    public static final int BOOKMARK_TYPE_MOBILE_FOLDER = 5;
    public static final int BOOKMARK_TYPE_OTHER_FOLDER = 4;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/bookmark";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/bookmark";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "bookmarks");
    public static final Uri CONTENT_URI_DEFAULT_FOLDER = Uri.withAppendedPath(CONTENT_URI, "folder");
    public static final String INSERT_AFTER = "insert_after";
    public static final String INSERT_AFTER_SOURCE_ID = "insert_after_source";
    public static final String IS_DELETED = "deleted";
    public static final String IS_FOLDER = "folder";
    public static final String PARAM_ACCOUNT_NAME = "acct_name";
    public static final String PARAM_ACCOUNT_TYPE = "acct_type";
    public static final String PARENT = "parent";
    public static final String PARENT_SOURCE_ID = "parent_source";
    public static final String POSITION = "position";
    public static final String QUERY_PARAMETER_SHOW_DELETED = "show_deleted";
    public static final String TYPE = "type";
    
    private Bookmarks() {}
    
    public static final Uri buildFolderUri(long paramLong)
    {
      return ContentUris.withAppendedId(CONTENT_URI_DEFAULT_FOLDER, paramLong);
    }
  }
  
  public static final class ChromeSyncColumns
  {
    public static final String CLIENT_UNIQUE = "sync4";
    public static final String FOLDER_NAME_BOOKMARKS = "google_chrome_bookmarks";
    public static final String FOLDER_NAME_BOOKMARKS_BAR = "bookmark_bar";
    public static final String FOLDER_NAME_OTHER_BOOKMARKS = "other_bookmarks";
    public static final String FOLDER_NAME_ROOT = "google_chrome";
    public static final String SERVER_UNIQUE = "sync3";
    
    private ChromeSyncColumns() {}
  }
  
  public static final class Combined
    implements BrowserContract.CommonColumns, BrowserContract.HistoryColumns, BrowserContract.ImageColumns
  {
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "combined");
    public static final String IS_BOOKMARK = "bookmark";
    
    private Combined() {}
  }
  
  static abstract interface CommonColumns
  {
    public static final String DATE_CREATED = "created";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String _ID = "_id";
  }
  
  public static final class History
    implements BrowserContract.CommonColumns, BrowserContract.HistoryColumns, BrowserContract.ImageColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/browser-history";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/browser-history";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "history");
    
    private History() {}
  }
  
  static abstract interface HistoryColumns
  {
    public static final String DATE_LAST_VISITED = "date";
    public static final String USER_ENTERED = "user_entered";
    public static final String VISITS = "visits";
  }
  
  static abstract interface ImageColumns
  {
    public static final String FAVICON = "favicon";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TOUCH_ICON = "touch_icon";
  }
  
  static abstract interface ImageMappingColumns
  {
    public static final String IMAGE_ID = "image_id";
    public static final String URL = "url";
  }
  
  public static final class ImageMappings
    implements BrowserContract.ImageMappingColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/image_mappings";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image_mappings";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "image_mappings");
    
    private ImageMappings() {}
  }
  
  public static final class Images
    implements BrowserContract.ImageColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/images";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/images";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "images");
    public static final String DATA = "data";
    public static final int IMAGE_TYPE_FAVICON = 1;
    public static final int IMAGE_TYPE_PRECOMPOSED_TOUCH_ICON = 2;
    public static final int IMAGE_TYPE_TOUCH_ICON = 4;
    public static final String TYPE = "type";
    public static final String URL = "url_key";
    
    private Images() {}
  }
  
  public static final class Searches
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/searches";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/searches";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "searches");
    public static final String DATE = "date";
    public static final String SEARCH = "search";
    public static final String _ID = "_id";
    
    private Searches() {}
  }
  
  public static final class Settings
  {
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "settings");
    public static final String KEY = "key";
    public static final String KEY_SYNC_ENABLED = "sync_enabled";
    public static final String VALUE = "value";
    
    private Settings() {}
    
    public static boolean isSyncEnabled(Context paramContext)
    {
      Context localContext = null;
      try
      {
        paramContext = paramContext.getContentResolver().query(CONTENT_URI, new String[] { "value" }, "key=?", new String[] { "sync_enabled" }, null);
        boolean bool = false;
        if (paramContext != null)
        {
          localContext = paramContext;
          if (paramContext.moveToFirst())
          {
            localContext = paramContext;
            int i = paramContext.getInt(0);
            if (i != 0) {
              bool = true;
            }
            return bool;
          }
        }
        return false;
      }
      finally
      {
        if (localContext != null) {
          localContext.close();
        }
      }
    }
    
    public static void setSyncEnabled(Context paramContext, boolean paramBoolean)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("key", "sync_enabled");
      localContentValues.put("value", Integer.valueOf(paramBoolean));
      paramContext.getContentResolver().insert(CONTENT_URI, localContentValues);
    }
  }
  
  static abstract interface SyncColumns
    extends BrowserContract.BaseSyncColumns
  {
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String DATE_MODIFIED = "modified";
    public static final String DIRTY = "dirty";
    public static final String SOURCE_ID = "sourceid";
    public static final String VERSION = "version";
  }
  
  public static final class SyncState
    implements SyncStateContract.Columns
  {
    public static final String CONTENT_DIRECTORY = "syncstate";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "syncstate");
    
    private SyncState() {}
    
    public static byte[] get(ContentProviderClient paramContentProviderClient, Account paramAccount)
      throws RemoteException
    {
      return SyncStateContract.Helpers.get(paramContentProviderClient, CONTENT_URI, paramAccount);
    }
    
    public static Pair<Uri, byte[]> getWithUri(ContentProviderClient paramContentProviderClient, Account paramAccount)
      throws RemoteException
    {
      return SyncStateContract.Helpers.getWithUri(paramContentProviderClient, CONTENT_URI, paramAccount);
    }
    
    public static ContentProviderOperation newSetOperation(Account paramAccount, byte[] paramArrayOfByte)
    {
      return SyncStateContract.Helpers.newSetOperation(CONTENT_URI, paramAccount, paramArrayOfByte);
    }
    
    public static void set(ContentProviderClient paramContentProviderClient, Account paramAccount, byte[] paramArrayOfByte)
      throws RemoteException
    {
      SyncStateContract.Helpers.set(paramContentProviderClient, CONTENT_URI, paramAccount, paramArrayOfByte);
    }
  }
}
