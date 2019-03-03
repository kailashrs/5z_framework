package android.app;

import android.annotation.SystemApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.Downloads.Impl;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DownloadManager
{
  public static final String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";
  @SystemApi
  public static final String ACTION_DOWNLOAD_COMPLETED = "android.intent.action.DOWNLOAD_COMPLETED";
  public static final String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
  public static final String ACTION_VIEW_DOWNLOADS = "android.intent.action.VIEW_DOWNLOADS";
  public static final String COLUMN_ALLOW_WRITE = "allow_write";
  public static final String COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_LAST_MODIFIED_TIMESTAMP = "last_modified_timestamp";
  @Deprecated
  public static final String COLUMN_LOCAL_FILENAME = "local_filename";
  public static final String COLUMN_LOCAL_URI = "local_uri";
  public static final String COLUMN_MEDIAPROVIDER_URI = "mediaprovider_uri";
  public static final String COLUMN_MEDIA_TYPE = "media_type";
  public static final String COLUMN_REASON = "reason";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_TOTAL_SIZE_BYTES = "total_size";
  public static final String COLUMN_URI = "uri";
  public static final int ERROR_BLOCKED = 1010;
  public static final int ERROR_CANNOT_RESUME = 1008;
  public static final int ERROR_DEVICE_NOT_FOUND = 1007;
  public static final int ERROR_FILE_ALREADY_EXISTS = 1009;
  public static final int ERROR_FILE_ERROR = 1001;
  public static final int ERROR_HTTP_DATA_ERROR = 1004;
  public static final int ERROR_INSUFFICIENT_SPACE = 1006;
  public static final int ERROR_TOO_MANY_REDIRECTS = 1005;
  public static final int ERROR_UNHANDLED_HTTP_CODE = 1002;
  public static final int ERROR_UNKNOWN = 1000;
  public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";
  public static final String EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS = "extra_click_download_ids";
  public static final String INTENT_EXTRAS_SORT_BY_SIZE = "android.app.DownloadManager.extra_sortBySize";
  private static final String NON_DOWNLOADMANAGER_DOWNLOAD = "non-dwnldmngr-download-dont-retry2download";
  public static final int PAUSED_QUEUED_FOR_WIFI = 3;
  public static final int PAUSED_UNKNOWN = 4;
  public static final int PAUSED_WAITING_FOR_NETWORK = 2;
  public static final int PAUSED_WAITING_TO_RETRY = 1;
  public static final int STATUS_FAILED = 16;
  public static final int STATUS_PAUSED = 4;
  public static final int STATUS_PENDING = 1;
  public static final int STATUS_RUNNING = 2;
  public static final int STATUS_SUCCESSFUL = 8;
  public static final String[] UNDERLYING_COLUMNS = { "_id", "_data AS local_filename", "mediaprovider_uri", "destination", "title", "description", "uri", "status", "hint", "mimetype AS media_type", "total_bytes AS total_size", "lastmod AS last_modified_timestamp", "current_bytes AS bytes_so_far", "allow_write", "'placeholder' AS local_uri", "'placeholder' AS reason" };
  private boolean mAccessFilename;
  private Uri mBaseUri = Downloads.Impl.CONTENT_URI;
  private final String mPackageName;
  private final ContentResolver mResolver;
  
  public DownloadManager(Context paramContext)
  {
    mResolver = paramContext.getContentResolver();
    mPackageName = paramContext.getPackageName();
    boolean bool;
    if (getApplicationInfotargetSdkVersion < 24) {
      bool = true;
    } else {
      bool = false;
    }
    mAccessFilename = bool;
  }
  
  public static long getActiveNetworkWarningBytes(Context paramContext)
  {
    return -1L;
  }
  
  public static Long getMaxBytesOverMobile(Context paramContext)
  {
    try
    {
      long l = Settings.Global.getLong(paramContext.getContentResolver(), "download_manager_max_bytes_over_mobile");
      return Long.valueOf(l);
    }
    catch (Settings.SettingNotFoundException paramContext) {}
    return null;
  }
  
  public static Long getRecommendedMaxBytesOverMobile(Context paramContext)
  {
    try
    {
      long l = Settings.Global.getLong(paramContext.getContentResolver(), "download_manager_recommended_max_bytes_over_mobile");
      return Long.valueOf(l);
    }
    catch (Settings.SettingNotFoundException paramContext) {}
    return null;
  }
  
  static String[] getWhereArgsForIds(long[] paramArrayOfLong)
  {
    return getWhereArgsForIds(paramArrayOfLong, new String[paramArrayOfLong.length]);
  }
  
  static String[] getWhereArgsForIds(long[] paramArrayOfLong, String[] paramArrayOfString)
  {
    for (int i = 0; i < paramArrayOfLong.length; i++) {
      paramArrayOfString[i] = Long.toString(paramArrayOfLong[i]);
    }
    return paramArrayOfString;
  }
  
  static String getWhereClauseForIds(long[] paramArrayOfLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(");
    for (int i = 0; i < paramArrayOfLong.length; i++)
    {
      if (i > 0) {
        localStringBuilder.append("OR ");
      }
      localStringBuilder.append("_id");
      localStringBuilder.append(" = ? ");
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public static boolean isActiveNetworkExpensive(Context paramContext)
  {
    return false;
  }
  
  private static void validateArgumentIsNonEmpty(String paramString1, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString2)) {
      return;
    }
    paramString2 = new StringBuilder();
    paramString2.append(paramString1);
    paramString2.append(" can't be null");
    throw new IllegalArgumentException(paramString2.toString());
  }
  
  public long addCompletedDownload(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, String paramString4, long paramLong, boolean paramBoolean2)
  {
    return addCompletedDownload(paramString1, paramString2, paramBoolean1, paramString3, paramString4, paramLong, paramBoolean2, false, null, null);
  }
  
  public long addCompletedDownload(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, String paramString4, long paramLong, boolean paramBoolean2, Uri paramUri1, Uri paramUri2)
  {
    return addCompletedDownload(paramString1, paramString2, paramBoolean1, paramString3, paramString4, paramLong, paramBoolean2, false, paramUri1, paramUri2);
  }
  
  public long addCompletedDownload(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, String paramString4, long paramLong, boolean paramBoolean2, boolean paramBoolean3)
  {
    return addCompletedDownload(paramString1, paramString2, paramBoolean1, paramString3, paramString4, paramLong, paramBoolean2, paramBoolean3, null, null);
  }
  
  public long addCompletedDownload(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, String paramString4, long paramLong, boolean paramBoolean2, boolean paramBoolean3, Uri paramUri1, Uri paramUri2)
  {
    validateArgumentIsNonEmpty("title", paramString1);
    validateArgumentIsNonEmpty("description", paramString2);
    validateArgumentIsNonEmpty("path", paramString4);
    validateArgumentIsNonEmpty("mimeType", paramString3);
    if (paramLong >= 0L)
    {
      if (paramUri1 != null) {
        paramUri1 = new Request(paramUri1);
      } else {
        paramUri1 = new Request("non-dwnldmngr-download-dont-retry2download");
      }
      paramUri1.setTitle(paramString1).setDescription(paramString2).setMimeType(paramString3);
      if (paramUri2 != null) {
        paramUri1.addRequestHeader("Referer", paramUri2.toString());
      }
      paramString1 = paramUri1.toContentValues(null);
      paramString1.put("destination", Integer.valueOf(6));
      paramString1.put("_data", paramString4);
      paramString1.put("status", Integer.valueOf(200));
      paramString1.put("total_bytes", Long.valueOf(paramLong));
      int i = 2;
      if (paramBoolean1) {
        j = 0;
      } else {
        j = 2;
      }
      paramString1.put("scanned", Integer.valueOf(j));
      int j = i;
      if (paramBoolean2) {
        j = 3;
      }
      paramString1.put("visibility", Integer.valueOf(j));
      paramString1.put("allow_write", Integer.valueOf(paramBoolean3));
      paramString1 = mResolver.insert(Downloads.Impl.CONTENT_URI, paramString1);
      if (paramString1 == null) {
        return -1L;
      }
      return Long.parseLong(paramString1.getLastPathSegment());
    }
    throw new IllegalArgumentException(" invalid value for param: totalBytes");
  }
  
  public long enqueue(Request paramRequest)
  {
    paramRequest = paramRequest.toContentValues(mPackageName);
    return Long.parseLong(mResolver.insert(Downloads.Impl.CONTENT_URI, paramRequest).getLastPathSegment());
  }
  
  public void forceDownload(long... paramVarArgs)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Integer.valueOf(190));
    localContentValues.put("control", Integer.valueOf(0));
    localContentValues.put("bypass_recommended_size_limit", Integer.valueOf(1));
    mResolver.update(mBaseUri, localContentValues, getWhereClauseForIds(paramVarArgs), getWhereArgsForIds(paramVarArgs));
  }
  
  public Uri getDownloadUri(long paramLong)
  {
    return ContentUris.withAppendedId(Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI, paramLong);
  }
  
  public String getMimeTypeForDownloadedFile(long paramLong)
  {
    Object localObject1 = new Query().setFilterById(new long[] { paramLong });
    Object localObject3 = null;
    try
    {
      localObject1 = query((Query)localObject1);
      if (localObject1 == null)
      {
        if (localObject1 != null) {
          ((Cursor)localObject1).close();
        }
        return null;
      }
      localObject3 = localObject1;
      if (((Cursor)localObject1).moveToFirst())
      {
        localObject3 = localObject1;
        String str = ((Cursor)localObject1).getString(((Cursor)localObject1).getColumnIndexOrThrow("media_type"));
        if (localObject1 != null) {
          ((Cursor)localObject1).close();
        }
        return str;
      }
      if (localObject1 != null) {
        ((Cursor)localObject1).close();
      }
      return null;
    }
    finally
    {
      if (localObject3 != null) {
        localObject3.close();
      }
    }
  }
  
  public Uri getUriForDownloadedFile(long paramLong)
  {
    Object localObject1 = new Query().setFilterById(new long[] { paramLong });
    Object localObject3 = null;
    try
    {
      localObject1 = query((Query)localObject1);
      if (localObject1 == null)
      {
        if (localObject1 != null) {
          ((Cursor)localObject1).close();
        }
        return null;
      }
      localObject3 = localObject1;
      if (((Cursor)localObject1).moveToFirst())
      {
        localObject3 = localObject1;
        if (8 == ((Cursor)localObject1).getInt(((Cursor)localObject1).getColumnIndexOrThrow("status")))
        {
          localObject3 = localObject1;
          Uri localUri = ContentUris.withAppendedId(Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI, paramLong);
          if (localObject1 != null) {
            ((Cursor)localObject1).close();
          }
          return localUri;
        }
      }
      if (localObject1 != null) {
        ((Cursor)localObject1).close();
      }
      return null;
    }
    finally
    {
      if (localObject3 != null) {
        localObject3.close();
      }
    }
  }
  
  public int markRowDeleted(long... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0)) {
      return mResolver.delete(mBaseUri, getWhereClauseForIds(paramVarArgs), getWhereArgsForIds(paramVarArgs));
    }
    throw new IllegalArgumentException("input param 'ids' can't be null");
  }
  
  public ParcelFileDescriptor openDownloadedFile(long paramLong)
    throws FileNotFoundException
  {
    return mResolver.openFileDescriptor(getDownloadUri(paramLong), "r");
  }
  
  public Cursor query(Query paramQuery)
  {
    paramQuery = paramQuery.runQuery(mResolver, UNDERLYING_COLUMNS, mBaseUri);
    if (paramQuery == null) {
      return null;
    }
    return new CursorTranslator(paramQuery, mBaseUri, mAccessFilename);
  }
  
  public int remove(long... paramVarArgs)
  {
    return markRowDeleted(paramVarArgs);
  }
  
  public boolean rename(Context paramContext, long paramLong, String paramString)
  {
    if (FileUtils.isValidFatFilename(paramString))
    {
      Object localObject1 = new Query().setFilterById(new long[] { paramLong });
      Object localObject2 = null;
      String str1 = null;
      String str2 = null;
      try
      {
        localObject1 = query((Query)localObject1);
        if (localObject1 == null) {
          return false;
        }
        localObject2 = localObject1;
        if (((Cursor)localObject1).moveToFirst())
        {
          localObject2 = localObject1;
          int i = ((Cursor)localObject1).getInt(((Cursor)localObject1).getColumnIndexOrThrow("status"));
          if (8 != i) {
            return false;
          }
          localObject2 = localObject1;
          str1 = ((Cursor)localObject1).getString(((Cursor)localObject1).getColumnIndexOrThrow("title"));
          localObject2 = localObject1;
          str2 = ((Cursor)localObject1).getString(((Cursor)localObject1).getColumnIndexOrThrow("media_type"));
        }
        if (localObject1 != null) {
          ((Cursor)localObject1).close();
        }
        if ((str1 != null) && (str2 != null))
        {
          localObject2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
          localObject1 = new File((File)localObject2, str1);
          localObject2 = new File((File)localObject2, paramString);
          if (!((File)localObject2).exists())
          {
            if (((File)localObject1).renameTo((File)localObject2))
            {
              if (str2.startsWith("image/"))
              {
                paramContext.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data=?", new String[] { ((File)localObject1).getAbsolutePath() });
                localObject1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                ((Intent)localObject1).setData(Uri.fromFile((File)localObject2));
                paramContext.sendBroadcast((Intent)localObject1);
              }
              paramContext = new ContentValues();
              paramContext.put("title", paramString);
              paramContext.put("_data", ((File)localObject2).toString());
              paramContext.putNull("mediaprovider_uri");
              paramString = new long[1];
              boolean bool = false;
              paramString[0] = paramLong;
              if (mResolver.update(mBaseUri, paramContext, getWhereClauseForIds(paramString), getWhereArgsForIds(paramString)) == 1) {
                bool = true;
              }
              return bool;
            }
            paramContext = new StringBuilder();
            paramContext.append("Failed to rename to ");
            paramContext.append(localObject2);
            throw new IllegalStateException(paramContext.toString());
          }
          paramContext = new StringBuilder();
          paramContext.append("Already exists ");
          paramContext.append(localObject2);
          throw new IllegalStateException(paramContext.toString());
        }
        paramContext = new StringBuilder();
        paramContext.append("Document with id ");
        paramContext.append(paramLong);
        paramContext.append(" does not exist");
        throw new IllegalStateException(paramContext.toString());
      }
      finally
      {
        if (localObject2 != null) {
          ((Cursor)localObject2).close();
        }
      }
    }
    paramContext = new StringBuilder();
    paramContext.append(paramString);
    paramContext.append(" is not a valid filename");
    throw new SecurityException(paramContext.toString());
  }
  
  public void restartDownload(long... paramVarArgs)
  {
    Object localObject = query(new Query().setFilterById(paramVarArgs));
    try
    {
      ((Cursor)localObject).moveToFirst();
      while (!((Cursor)localObject).isAfterLast())
      {
        int i = ((Cursor)localObject).getInt(((Cursor)localObject).getColumnIndex("status"));
        if ((i != 8) && (i != 16))
        {
          IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
          paramVarArgs = new java/lang/StringBuilder;
          paramVarArgs.<init>();
          paramVarArgs.append("Cannot restart incomplete download: ");
          paramVarArgs.append(((Cursor)localObject).getLong(((Cursor)localObject).getColumnIndex("_id")));
          localIllegalArgumentException.<init>(paramVarArgs.toString());
          throw localIllegalArgumentException;
        }
        ((Cursor)localObject).moveToNext();
      }
      ((Cursor)localObject).close();
      localObject = new ContentValues();
      ((ContentValues)localObject).put("current_bytes", Integer.valueOf(0));
      ((ContentValues)localObject).put("total_bytes", Integer.valueOf(-1));
      ((ContentValues)localObject).putNull("_data");
      ((ContentValues)localObject).put("status", Integer.valueOf(190));
      ((ContentValues)localObject).put("numfailed", Integer.valueOf(0));
      mResolver.update(mBaseUri, (ContentValues)localObject, getWhereClauseForIds(paramVarArgs), getWhereArgsForIds(paramVarArgs));
      return;
    }
    finally
    {
      ((Cursor)localObject).close();
    }
  }
  
  public void setAccessAllDownloads(boolean paramBoolean)
  {
    if (paramBoolean) {
      mBaseUri = Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI;
    } else {
      mBaseUri = Downloads.Impl.CONTENT_URI;
    }
  }
  
  public void setAccessFilename(boolean paramBoolean)
  {
    mAccessFilename = paramBoolean;
  }
  
  private static class CursorTranslator
    extends CursorWrapper
  {
    private final boolean mAccessFilename;
    private final Uri mBaseUri;
    
    public CursorTranslator(Cursor paramCursor, Uri paramUri, boolean paramBoolean)
    {
      super();
      mBaseUri = paramUri;
      mAccessFilename = paramBoolean;
    }
    
    private long getErrorCode(int paramInt)
    {
      if (((400 <= paramInt) && (paramInt < 488)) || ((500 <= paramInt) && (paramInt < 600))) {
        return paramInt;
      }
      switch (paramInt)
      {
      default: 
        return 1000L;
      case 497: 
        return 1005L;
      case 495: 
        return 1004L;
      case 493: 
      case 494: 
        return 1002L;
      case 492: 
        return 1001L;
      case 489: 
        return 1008L;
      case 488: 
        return 1009L;
      case 199: 
        return 1007L;
      }
      return 1006L;
    }
    
    private String getLocalUri()
    {
      long l = getLong(getColumnIndex("destination"));
      if ((l != 4L) && (l != 0L) && (l != 6L))
      {
        l = getLong(getColumnIndex("_id"));
        return ContentUris.withAppendedId(Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI, l).toString();
      }
      String str = super.getString(getColumnIndex("local_filename"));
      if (str == null) {
        return null;
      }
      return Uri.fromFile(new File(str)).toString();
    }
    
    private long getPausedReason(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 4L;
      case 196: 
        return 3L;
      case 195: 
        return 2L;
      }
      return 1L;
    }
    
    private long getReason(int paramInt)
    {
      int i = translateStatus(paramInt);
      if (i != 4)
      {
        if (i != 16) {
          return 0L;
        }
        return getErrorCode(paramInt);
      }
      return getPausedReason(paramInt);
    }
    
    private int translateStatus(int paramInt)
    {
      if (paramInt != 190)
      {
        if (paramInt != 200)
        {
          switch (paramInt)
          {
          default: 
            return 16;
          case 193: 
          case 194: 
          case 195: 
          case 196: 
            return 4;
          }
          return 2;
        }
        return 8;
      }
      return 1;
    }
    
    public int getInt(int paramInt)
    {
      return (int)getLong(paramInt);
    }
    
    public long getLong(int paramInt)
    {
      if (getColumnName(paramInt).equals("reason")) {
        return getReason(super.getInt(getColumnIndex("status")));
      }
      if (getColumnName(paramInt).equals("status")) {
        return translateStatus(super.getInt(getColumnIndex("status")));
      }
      return super.getLong(paramInt);
    }
    
    public String getString(int paramInt)
    {
      String str = getColumnName(paramInt);
      int i = str.hashCode();
      if (i != -1204869480)
      {
        if ((i == 22072411) && (str.equals("local_filename")))
        {
          i = 1;
          break label56;
        }
      }
      else if (str.equals("local_uri"))
      {
        i = 0;
        break label56;
      }
      i = -1;
      switch (i)
      {
      default: 
        break;
      case 1: 
        if (!mAccessFilename) {
          throw new SecurityException("COLUMN_LOCAL_FILENAME is deprecated; use ContentResolver.openFileDescriptor() instead");
        }
        break;
      case 0: 
        label56:
        return getLocalUri();
      }
      return super.getString(paramInt);
    }
  }
  
  public static class Query
  {
    public static final int ORDER_ASCENDING = 1;
    public static final int ORDER_DESCENDING = 2;
    private String mFilterString = null;
    private long[] mIds = null;
    private boolean mOnlyIncludeVisibleInDownloadsUi = false;
    private String mOrderByColumn = "lastmod";
    private int mOrderDirection = 2;
    private Integer mStatusFlags = null;
    
    public Query() {}
    
    private String joinStrings(String paramString, Iterable<String> paramIterable)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 1;
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
      {
        paramIterable = (String)localIterator.next();
        if (i == 0) {
          localStringBuilder.append(paramString);
        }
        localStringBuilder.append(paramIterable);
        i = 0;
      }
      return localStringBuilder.toString();
    }
    
    private String statusClause(String paramString, int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("status");
      localStringBuilder.append(paramString);
      localStringBuilder.append("'");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("'");
      return localStringBuilder.toString();
    }
    
    public Query orderBy(String paramString, int paramInt)
    {
      if ((paramInt != 1) && (paramInt != 2))
      {
        paramString = new StringBuilder();
        paramString.append("Invalid direction: ");
        paramString.append(paramInt);
        throw new IllegalArgumentException(paramString.toString());
      }
      if (paramString.equals("last_modified_timestamp"))
      {
        mOrderByColumn = "lastmod";
      }
      else
      {
        if (!paramString.equals("total_size")) {
          break label86;
        }
        mOrderByColumn = "total_bytes";
      }
      mOrderDirection = paramInt;
      return this;
      label86:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Cannot order by ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    Cursor runQuery(ContentResolver paramContentResolver, String[] paramArrayOfString, Uri paramUri)
    {
      Object localObject1 = new ArrayList();
      int i;
      if (mIds == null) {
        i = 0;
      } else {
        i = mIds.length;
      }
      if (mFilterString != null) {
        i++;
      }
      String[] arrayOfString = new String[i];
      if (i > 0)
      {
        if (mIds != null)
        {
          ((List)localObject1).add(DownloadManager.getWhereClauseForIds(mIds));
          DownloadManager.getWhereArgsForIds(mIds, arrayOfString);
        }
        if (mFilterString != null)
        {
          ((List)localObject1).add("title LIKE ?");
          i = arrayOfString.length;
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("%");
          ((StringBuilder)localObject2).append(mFilterString);
          ((StringBuilder)localObject2).append("%");
          arrayOfString[(i - 1)] = ((StringBuilder)localObject2).toString();
        }
      }
      if (mStatusFlags != null)
      {
        localObject2 = new ArrayList();
        if ((mStatusFlags.intValue() & 0x1) != 0) {
          ((List)localObject2).add(statusClause("=", 190));
        }
        if ((mStatusFlags.intValue() & 0x2) != 0) {
          ((List)localObject2).add(statusClause("=", 192));
        }
        if ((mStatusFlags.intValue() & 0x4) != 0)
        {
          ((List)localObject2).add(statusClause("=", 193));
          ((List)localObject2).add(statusClause("=", 194));
          ((List)localObject2).add(statusClause("=", 195));
          ((List)localObject2).add(statusClause("=", 196));
        }
        if ((mStatusFlags.intValue() & 0x8) != 0) {
          ((List)localObject2).add(statusClause("=", 200));
        }
        if ((mStatusFlags.intValue() & 0x10) != 0)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("(");
          localStringBuilder.append(statusClause(">=", 400));
          localStringBuilder.append(" AND ");
          localStringBuilder.append(statusClause("<", 600));
          localStringBuilder.append(")");
          ((List)localObject2).add(localStringBuilder.toString());
        }
        ((List)localObject1).add(joinStrings(" OR ", (Iterable)localObject2));
      }
      if (mOnlyIncludeVisibleInDownloadsUi) {
        ((List)localObject1).add("is_visible_in_downloads_ui != '0'");
      }
      ((List)localObject1).add("deleted != '1'");
      Object localObject2 = joinStrings(" AND ", (Iterable)localObject1);
      if (mOrderDirection == 1) {
        localObject1 = "ASC";
      } else {
        localObject1 = "DESC";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(mOrderByColumn);
      localStringBuilder.append(" ");
      localStringBuilder.append((String)localObject1);
      return paramContentResolver.query(paramUri, paramArrayOfString, (String)localObject2, arrayOfString, localStringBuilder.toString());
    }
    
    public Query setFilterById(long... paramVarArgs)
    {
      mIds = paramVarArgs;
      return this;
    }
    
    public Query setFilterByStatus(int paramInt)
    {
      mStatusFlags = Integer.valueOf(paramInt);
      return this;
    }
    
    public Query setFilterByString(String paramString)
    {
      mFilterString = paramString;
      return this;
    }
    
    public Query setOnlyIncludeVisibleInDownloadsUi(boolean paramBoolean)
    {
      mOnlyIncludeVisibleInDownloadsUi = paramBoolean;
      return this;
    }
  }
  
  public static class Request
  {
    @Deprecated
    public static final int NETWORK_BLUETOOTH = 4;
    public static final int NETWORK_MOBILE = 1;
    public static final int NETWORK_WIFI = 2;
    private static final int SCANNABLE_VALUE_NO = 2;
    private static final int SCANNABLE_VALUE_YES = 0;
    public static final int VISIBILITY_HIDDEN = 2;
    public static final int VISIBILITY_VISIBLE = 0;
    public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
    public static final int VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3;
    private int mAllowedNetworkTypes = -1;
    private CharSequence mDescription;
    private Uri mDestinationUri;
    private int mFlags = 0;
    private boolean mIsVisibleInDownloadsUi = true;
    private boolean mMeteredAllowed = true;
    private String mMimeType;
    private int mNotificationVisibility = 0;
    private List<Pair<String, String>> mRequestHeaders = new ArrayList();
    private boolean mRoamingAllowed = true;
    private boolean mScannable = false;
    private CharSequence mTitle;
    private Uri mUri;
    
    public Request(Uri paramUri)
    {
      if (paramUri != null)
      {
        Object localObject = paramUri.getScheme();
        if ((localObject != null) && ((((String)localObject).equals("http")) || (((String)localObject).equals("https"))))
        {
          mUri = paramUri;
          return;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Can only download HTTP/HTTPS URIs: ");
        ((StringBuilder)localObject).append(paramUri);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      }
      throw new NullPointerException();
    }
    
    Request(String paramString)
    {
      mUri = Uri.parse(paramString);
    }
    
    private void encodeHttpHeaders(ContentValues paramContentValues)
    {
      int i = 0;
      Iterator localIterator = mRequestHeaders.iterator();
      while (localIterator.hasNext())
      {
        Object localObject1 = (Pair)localIterator.next();
        Object localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)first);
        ((StringBuilder)localObject2).append(": ");
        ((StringBuilder)localObject2).append((String)second);
        localObject2 = ((StringBuilder)localObject2).toString();
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("http_header_");
        ((StringBuilder)localObject1).append(i);
        paramContentValues.put(((StringBuilder)localObject1).toString(), (String)localObject2);
        i++;
      }
    }
    
    private void putIfNonNull(ContentValues paramContentValues, String paramString, Object paramObject)
    {
      if (paramObject != null) {
        paramContentValues.put(paramString, paramObject.toString());
      }
    }
    
    private void setDestinationFromBase(File paramFile, String paramString)
    {
      if (paramString != null)
      {
        mDestinationUri = Uri.withAppendedPath(Uri.fromFile(paramFile), paramString);
        return;
      }
      throw new NullPointerException("subPath cannot be null");
    }
    
    public Request addRequestHeader(String paramString1, String paramString2)
    {
      if (paramString1 != null)
      {
        if (!paramString1.contains(":"))
        {
          String str = paramString2;
          if (paramString2 == null) {
            str = "";
          }
          mRequestHeaders.add(Pair.create(paramString1, str));
          return this;
        }
        throw new IllegalArgumentException("header may not contain ':'");
      }
      throw new NullPointerException("header cannot be null");
    }
    
    public void allowScanningByMediaScanner()
    {
      mScannable = true;
    }
    
    public Request setAllowedNetworkTypes(int paramInt)
    {
      mAllowedNetworkTypes = paramInt;
      return this;
    }
    
    public Request setAllowedOverMetered(boolean paramBoolean)
    {
      mMeteredAllowed = paramBoolean;
      return this;
    }
    
    public Request setAllowedOverRoaming(boolean paramBoolean)
    {
      mRoamingAllowed = paramBoolean;
      return this;
    }
    
    public Request setDescription(CharSequence paramCharSequence)
    {
      mDescription = paramCharSequence;
      return this;
    }
    
    public Request setDestinationInExternalFilesDir(Context paramContext, String paramString1, String paramString2)
    {
      paramContext = paramContext.getExternalFilesDir(paramString1);
      if (paramContext != null)
      {
        if (paramContext.exists())
        {
          if (!paramContext.isDirectory())
          {
            paramString1 = new StringBuilder();
            paramString1.append(paramContext.getAbsolutePath());
            paramString1.append(" already exists and is not a directory");
            throw new IllegalStateException(paramString1.toString());
          }
        }
        else {
          if (!paramContext.mkdirs()) {
            break label78;
          }
        }
        setDestinationFromBase(paramContext, paramString2);
        return this;
        label78:
        paramString1 = new StringBuilder();
        paramString1.append("Unable to create directory: ");
        paramString1.append(paramContext.getAbsolutePath());
        throw new IllegalStateException(paramString1.toString());
      }
      throw new IllegalStateException("Failed to get external storage files directory");
    }
    
    public Request setDestinationInExternalPublicDir(String paramString1, String paramString2)
    {
      paramString1 = Environment.getExternalStoragePublicDirectory(paramString1);
      if (paramString1 != null)
      {
        if (paramString1.exists())
        {
          if (!paramString1.isDirectory())
          {
            paramString2 = new StringBuilder();
            paramString2.append(paramString1.getAbsolutePath());
            paramString2.append(" already exists and is not a directory");
            throw new IllegalStateException(paramString2.toString());
          }
        }
        else {
          if (!paramString1.mkdirs()) {
            break label77;
          }
        }
        setDestinationFromBase(paramString1, paramString2);
        return this;
        label77:
        paramString2 = new StringBuilder();
        paramString2.append("Unable to create directory: ");
        paramString2.append(paramString1.getAbsolutePath());
        throw new IllegalStateException(paramString2.toString());
      }
      throw new IllegalStateException("Failed to get external storage public directory");
    }
    
    public Request setDestinationUri(Uri paramUri)
    {
      mDestinationUri = paramUri;
      return this;
    }
    
    public Request setMimeType(String paramString)
    {
      mMimeType = paramString;
      return this;
    }
    
    public Request setNotificationVisibility(int paramInt)
    {
      mNotificationVisibility = paramInt;
      return this;
    }
    
    public Request setRequiresCharging(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x1;
      } else {
        mFlags &= 0xFFFFFFFE;
      }
      return this;
    }
    
    public Request setRequiresDeviceIdle(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x2;
      } else {
        mFlags &= 0xFFFFFFFD;
      }
      return this;
    }
    
    @Deprecated
    public Request setShowRunningNotification(boolean paramBoolean)
    {
      Request localRequest;
      if (paramBoolean) {
        localRequest = setNotificationVisibility(0);
      } else {
        localRequest = setNotificationVisibility(2);
      }
      return localRequest;
    }
    
    public Request setTitle(CharSequence paramCharSequence)
    {
      mTitle = paramCharSequence;
      return this;
    }
    
    public Request setVisibleInDownloadsUi(boolean paramBoolean)
    {
      mIsVisibleInDownloadsUi = paramBoolean;
      return this;
    }
    
    ContentValues toContentValues(String paramString)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("uri", mUri.toString());
      localContentValues.put("is_public_api", Boolean.valueOf(true));
      localContentValues.put("notificationpackage", paramString);
      paramString = mDestinationUri;
      int i = 2;
      if (paramString != null)
      {
        localContentValues.put("destination", Integer.valueOf(4));
        localContentValues.put("hint", mDestinationUri.toString());
      }
      else
      {
        localContentValues.put("destination", Integer.valueOf(2));
      }
      if (mScannable) {
        i = 0;
      }
      localContentValues.put("scanned", Integer.valueOf(i));
      if (!mRequestHeaders.isEmpty()) {
        encodeHttpHeaders(localContentValues);
      }
      putIfNonNull(localContentValues, "title", mTitle);
      putIfNonNull(localContentValues, "description", mDescription);
      putIfNonNull(localContentValues, "mimetype", mMimeType);
      localContentValues.put("visibility", Integer.valueOf(mNotificationVisibility));
      localContentValues.put("allowed_network_types", Integer.valueOf(mAllowedNetworkTypes));
      localContentValues.put("allow_roaming", Boolean.valueOf(mRoamingAllowed));
      localContentValues.put("allow_metered", Boolean.valueOf(mMeteredAllowed));
      localContentValues.put("flags", Integer.valueOf(mFlags));
      localContentValues.put("is_visible_in_downloads_ui", Boolean.valueOf(mIsVisibleInDownloadsUi));
      return localContentValues;
    }
  }
}
