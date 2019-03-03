package android.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class MediaStore
{
  public static final String ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE";
  public static final String ACTION_IMAGE_CAPTURE_SECURE = "android.media.action.IMAGE_CAPTURE_SECURE";
  public static final String ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE";
  public static final String AUTHORITY = "media";
  private static final String CONTENT_AUTHORITY_SLASH = "content://media/";
  public static final String EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit";
  public static final String EXTRA_FINISH_ON_COMPLETION = "android.intent.extra.finishOnCompletion";
  public static final String EXTRA_FULL_SCREEN = "android.intent.extra.fullScreen";
  public static final String EXTRA_MEDIA_ALBUM = "android.intent.extra.album";
  public static final String EXTRA_MEDIA_ARTIST = "android.intent.extra.artist";
  public static final String EXTRA_MEDIA_FOCUS = "android.intent.extra.focus";
  public static final String EXTRA_MEDIA_GENRE = "android.intent.extra.genre";
  public static final String EXTRA_MEDIA_PLAYLIST = "android.intent.extra.playlist";
  public static final String EXTRA_MEDIA_RADIO_CHANNEL = "android.intent.extra.radio_channel";
  public static final String EXTRA_MEDIA_TITLE = "android.intent.extra.title";
  public static final String EXTRA_OUTPUT = "output";
  public static final String EXTRA_SCREEN_ORIENTATION = "android.intent.extra.screenOrientation";
  public static final String EXTRA_SHOW_ACTION_ICONS = "android.intent.extra.showActionIcons";
  public static final String EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit";
  public static final String EXTRA_VIDEO_QUALITY = "android.intent.extra.videoQuality";
  public static final String INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH = "android.media.action.MEDIA_PLAY_FROM_SEARCH";
  public static final String INTENT_ACTION_MEDIA_SEARCH = "android.intent.action.MEDIA_SEARCH";
  @Deprecated
  public static final String INTENT_ACTION_MUSIC_PLAYER = "android.intent.action.MUSIC_PLAYER";
  public static final String INTENT_ACTION_STILL_IMAGE_CAMERA = "android.media.action.STILL_IMAGE_CAMERA";
  public static final String INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE = "android.media.action.STILL_IMAGE_CAMERA_SECURE";
  public static final String INTENT_ACTION_TEXT_OPEN_FROM_SEARCH = "android.media.action.TEXT_OPEN_FROM_SEARCH";
  public static final String INTENT_ACTION_VIDEO_CAMERA = "android.media.action.VIDEO_CAMERA";
  public static final String INTENT_ACTION_VIDEO_PLAY_FROM_SEARCH = "android.media.action.VIDEO_PLAY_FROM_SEARCH";
  public static final String MEDIA_IGNORE_FILENAME = ".nomedia";
  public static final String MEDIA_SCANNER_VOLUME = "volume";
  public static final String META_DATA_STILL_IMAGE_CAMERA_PREWARM_SERVICE = "android.media.still_image_camera_preview_service";
  public static final String PARAM_DELETE_DATA = "deletedata";
  public static final String RETRANSLATE_CALL = "update_titles";
  private static final String TAG = "MediaStore";
  public static final String UNHIDE_CALL = "unhide";
  public static final String UNKNOWN_STRING = "<unknown>";
  
  public MediaStore() {}
  
  /* Error */
  private static Uri getDocumentUri(ContentResolver paramContentResolver, String paramString, java.util.List<android.content.UriPermission> paramList)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc -42
    //   3: invokevirtual 220	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   6: astore_3
    //   7: aconst_null
    //   8: astore 4
    //   10: aload 4
    //   12: astore_0
    //   13: new 222	android/os/Bundle
    //   16: astore 5
    //   18: aload 4
    //   20: astore_0
    //   21: aload 5
    //   23: invokespecial 223	android/os/Bundle:<init>	()V
    //   26: aload 4
    //   28: astore_0
    //   29: aload 5
    //   31: ldc -31
    //   33: aload_2
    //   34: invokevirtual 229	android/os/Bundle:putParcelableList	(Ljava/lang/String;Ljava/util/List;)V
    //   37: aload 4
    //   39: astore_0
    //   40: aload_3
    //   41: ldc -25
    //   43: aload_1
    //   44: aload 5
    //   46: invokevirtual 237	android/content/ContentProviderClient:call	(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;
    //   49: ldc -17
    //   51: invokevirtual 243	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   54: checkcast 245	android/net/Uri
    //   57: astore_1
    //   58: aload_3
    //   59: ifnull +8 -> 67
    //   62: aconst_null
    //   63: aload_3
    //   64: invokestatic 247	android/provider/MediaStore:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   67: aload_1
    //   68: areturn
    //   69: astore_1
    //   70: goto +8 -> 78
    //   73: astore_1
    //   74: aload_1
    //   75: astore_0
    //   76: aload_1
    //   77: athrow
    //   78: aload_3
    //   79: ifnull +8 -> 87
    //   82: aload_0
    //   83: aload_3
    //   84: invokestatic 247	android/provider/MediaStore:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   87: aload_1
    //   88: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	paramContentResolver	ContentResolver
    //   0	89	1	paramString	String
    //   0	89	2	paramList	java.util.List<android.content.UriPermission>
    //   6	78	3	localContentProviderClient	android.content.ContentProviderClient
    //   8	30	4	localObject	Object
    //   16	29	5	localBundle	android.os.Bundle
    // Exception table:
    //   from	to	target	type
    //   13	18	69	finally
    //   21	26	69	finally
    //   29	37	69	finally
    //   40	58	69	finally
    //   76	78	69	finally
    //   13	18	73	java/lang/Throwable
    //   21	26	73	java/lang/Throwable
    //   29	37	73	java/lang/Throwable
    //   40	58	73	java/lang/Throwable
  }
  
  public static Uri getDocumentUri(Context paramContext, Uri paramUri)
  {
    try
    {
      paramContext = paramContext.getContentResolver();
      paramContext = getDocumentUri(paramContext, getFilePath(paramContext, paramUri), paramContext.getPersistedUriPermissions());
      return paramContext;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowAsRuntimeException();
    }
  }
  
  /* Error */
  private static String getFilePath(ContentResolver paramContentResolver, Uri paramUri)
    throws RemoteException
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc 93
    //   3: invokevirtual 220	android/content/ContentResolver:acquireUnstableContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
    //   6: astore_2
    //   7: aconst_null
    //   8: astore_3
    //   9: aload_3
    //   10: astore_0
    //   11: aload_2
    //   12: aload_1
    //   13: iconst_1
    //   14: anewarray 273	java/lang/String
    //   17: dup
    //   18: iconst_0
    //   19: ldc_w 275
    //   22: aastore
    //   23: aconst_null
    //   24: aconst_null
    //   25: aconst_null
    //   26: invokevirtual 279	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   29: astore 4
    //   31: aload 4
    //   33: invokeinterface 285 1 0
    //   38: ifeq +53 -> 91
    //   41: aload 4
    //   43: invokeinterface 289 1 0
    //   48: ifeq +30 -> 78
    //   51: aload 4
    //   53: iconst_0
    //   54: invokeinterface 293 2 0
    //   59: astore_1
    //   60: aload_3
    //   61: astore_0
    //   62: aload 4
    //   64: invokestatic 299	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   67: aload_2
    //   68: ifnull +8 -> 76
    //   71: aconst_null
    //   72: aload_2
    //   73: invokestatic 247	android/provider/MediaStore:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   76: aload_1
    //   77: areturn
    //   78: new 301	java/lang/IllegalStateException
    //   81: astore_0
    //   82: aload_0
    //   83: ldc_w 303
    //   86: invokespecial 306	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   89: aload_0
    //   90: athrow
    //   91: new 301	java/lang/IllegalStateException
    //   94: astore_0
    //   95: new 308	java/lang/StringBuilder
    //   98: astore 5
    //   100: aload 5
    //   102: invokespecial 309	java/lang/StringBuilder:<init>	()V
    //   105: aload 5
    //   107: ldc_w 311
    //   110: invokevirtual 315	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: pop
    //   114: aload 5
    //   116: aload_1
    //   117: invokevirtual 318	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: aload_0
    //   122: aload 5
    //   124: invokevirtual 322	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   127: invokespecial 306	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   130: aload_0
    //   131: athrow
    //   132: astore_1
    //   133: aload_3
    //   134: astore_0
    //   135: aload 4
    //   137: invokestatic 299	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   140: aload_3
    //   141: astore_0
    //   142: aload_1
    //   143: athrow
    //   144: astore_1
    //   145: goto +8 -> 153
    //   148: astore_1
    //   149: aload_1
    //   150: astore_0
    //   151: aload_1
    //   152: athrow
    //   153: aload_2
    //   154: ifnull +8 -> 162
    //   157: aload_0
    //   158: aload_2
    //   159: invokestatic 247	android/provider/MediaStore:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   162: aload_1
    //   163: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	164	0	paramContentResolver	ContentResolver
    //   0	164	1	paramUri	Uri
    //   6	153	2	localContentProviderClient	android.content.ContentProviderClient
    //   8	133	3	localObject	Object
    //   29	107	4	localCursor	Cursor
    //   98	25	5	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   31	60	132	finally
    //   78	91	132	finally
    //   91	132	132	finally
    //   11	31	144	finally
    //   62	67	144	finally
    //   135	140	144	finally
    //   142	144	144	finally
    //   151	153	144	finally
    //   11	31	148	java/lang/Throwable
    //   62	67	148	java/lang/Throwable
    //   135	140	148	java/lang/Throwable
    //   142	144	148	java/lang/Throwable
  }
  
  public static Uri getMediaScannerUri()
  {
    return Uri.parse("content://media/none/media_scanner");
  }
  
  public static String getVersion(Context paramContext)
  {
    paramContext = paramContext.getContentResolver().query(Uri.parse("content://media/none/version"), null, null, null, null);
    if (paramContext != null) {
      try
      {
        if (paramContext.moveToFirst())
        {
          String str = paramContext.getString(0);
          return str;
        }
        paramContext.close();
      }
      finally
      {
        paramContext.close();
      }
    }
    return null;
  }
  
  public static final class Audio
  {
    public Audio() {}
    
    public static String keyFor(String paramString)
    {
      if (paramString != null)
      {
        int i = 0;
        if (paramString.equals("<unknown>")) {
          return "\001";
        }
        if (paramString.startsWith("\001")) {
          i = 1;
        }
        paramString = paramString.trim().toLowerCase();
        Object localObject = paramString;
        if (paramString.startsWith("the ")) {
          localObject = paramString.substring(4);
        }
        paramString = (String)localObject;
        if (((String)localObject).startsWith("an ")) {
          paramString = ((String)localObject).substring(3);
        }
        localObject = paramString;
        if (paramString.startsWith("a ")) {
          localObject = paramString.substring(2);
        }
        boolean bool = ((String)localObject).endsWith(", the");
        int j = 0;
        if ((!bool) && (!((String)localObject).endsWith(",the")) && (!((String)localObject).endsWith(", an")) && (!((String)localObject).endsWith(",an")) && (!((String)localObject).endsWith(", a")))
        {
          paramString = (String)localObject;
          if (!((String)localObject).endsWith(",a")) {}
        }
        else
        {
          paramString = ((String)localObject).substring(0, ((String)localObject).lastIndexOf(','));
        }
        paramString = paramString.replaceAll("[\\[\\]\\(\\)\"'.,?!]", "").trim();
        if (paramString.length() > 0)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append('.');
          int k = paramString.length();
          while (j < k)
          {
            ((StringBuilder)localObject).append(paramString.charAt(j));
            ((StringBuilder)localObject).append('.');
            j++;
          }
          localObject = DatabaseUtils.getCollationKey(((StringBuilder)localObject).toString());
          paramString = (String)localObject;
          if (i != 0)
          {
            paramString = new StringBuilder();
            paramString.append("\001");
            paramString.append((String)localObject);
            paramString = paramString.toString();
          }
          return paramString;
        }
        return "";
      }
      return null;
    }
    
    public static abstract interface AlbumColumns
    {
      public static final String ALBUM = "album";
      public static final String ALBUM_ART = "album_art";
      public static final String ALBUM_ID = "album_id";
      public static final String ALBUM_KEY = "album_key";
      public static final String ARTIST = "artist";
      public static final String FIRST_YEAR = "minyear";
      public static final String LAST_YEAR = "maxyear";
      public static final String NUMBER_OF_SONGS = "numsongs";
      public static final String NUMBER_OF_SONGS_FOR_ARTIST = "numsongs_by_artist";
    }
    
    public static final class Albums
      implements BaseColumns, MediaStore.Audio.AlbumColumns
    {
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/albums";
      public static final String DEFAULT_SORT_ORDER = "album_key";
      public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/album";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      
      public Albums() {}
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/audio/albums");
        return Uri.parse(localStringBuilder.toString());
      }
    }
    
    public static abstract interface ArtistColumns
    {
      public static final String ARTIST = "artist";
      public static final String ARTIST_KEY = "artist_key";
      public static final String NUMBER_OF_ALBUMS = "number_of_albums";
      public static final String NUMBER_OF_TRACKS = "number_of_tracks";
    }
    
    public static final class Artists
      implements BaseColumns, MediaStore.Audio.ArtistColumns
    {
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/artists";
      public static final String DEFAULT_SORT_ORDER = "artist_key";
      public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/artist";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      
      public Artists() {}
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/audio/artists");
        return Uri.parse(localStringBuilder.toString());
      }
      
      public static final class Albums
        implements MediaStore.Audio.AlbumColumns
      {
        public Albums() {}
        
        public static final Uri getContentUri(String paramString, long paramLong)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("content://media/");
          localStringBuilder.append(paramString);
          localStringBuilder.append("/audio/artists/");
          localStringBuilder.append(paramLong);
          localStringBuilder.append("/albums");
          return Uri.parse(localStringBuilder.toString());
        }
      }
    }
    
    public static abstract interface AudioColumns
      extends MediaStore.MediaColumns
    {
      public static final String ALBUM = "album";
      public static final String ALBUM_ARTIST = "album_artist";
      public static final String ALBUM_ID = "album_id";
      public static final String ALBUM_KEY = "album_key";
      public static final String ARTIST = "artist";
      public static final String ARTIST_ID = "artist_id";
      public static final String ARTIST_KEY = "artist_key";
      public static final String BOOKMARK = "bookmark";
      public static final String COMPILATION = "compilation";
      public static final String COMPOSER = "composer";
      public static final String DURATION = "duration";
      public static final String GENRE = "genre";
      public static final String IS_ALARM = "is_alarm";
      public static final String IS_MUSIC = "is_music";
      public static final String IS_NOTIFICATION = "is_notification";
      public static final String IS_PODCAST = "is_podcast";
      public static final String IS_RINGTONE = "is_ringtone";
      public static final String TITLE_KEY = "title_key";
      public static final String TITLE_RESOURCE_URI = "title_resource_uri";
      public static final String TRACK = "track";
      public static final String YEAR = "year";
    }
    
    public static final class Genres
      implements BaseColumns, MediaStore.Audio.GenresColumns
    {
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/genre";
      public static final String DEFAULT_SORT_ORDER = "name";
      public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/genre";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      
      public Genres() {}
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/audio/genres");
        return Uri.parse(localStringBuilder.toString());
      }
      
      public static Uri getContentUriForAudioId(String paramString, int paramInt)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/audio/media/");
        localStringBuilder.append(paramInt);
        localStringBuilder.append("/genres");
        return Uri.parse(localStringBuilder.toString());
      }
      
      public static final class Members
        implements MediaStore.Audio.AudioColumns
      {
        public static final String AUDIO_ID = "audio_id";
        public static final String CONTENT_DIRECTORY = "members";
        public static final String DEFAULT_SORT_ORDER = "title_key";
        public static final String GENRE_ID = "genre_id";
        
        public Members() {}
        
        public static final Uri getContentUri(String paramString, long paramLong)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("content://media/");
          localStringBuilder.append(paramString);
          localStringBuilder.append("/audio/genres/");
          localStringBuilder.append(paramLong);
          localStringBuilder.append("/members");
          return Uri.parse(localStringBuilder.toString());
        }
      }
    }
    
    public static abstract interface GenresColumns
    {
      public static final String NAME = "name";
    }
    
    public static final class Media
      implements MediaStore.Audio.AudioColumns
    {
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/audio";
      public static final String DEFAULT_SORT_ORDER = "title_key";
      public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/audio";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      private static final String[] EXTERNAL_PATHS;
      public static final String EXTRA_MAX_BYTES = "android.provider.MediaStore.extra.MAX_BYTES";
      public static final Uri INTERNAL_CONTENT_URI;
      public static final String RECORD_SOUND_ACTION = "android.provider.MediaStore.RECORD_SOUND";
      
      static
      {
        String str = System.getenv("SECONDARY_STORAGE");
        if (str != null) {
          EXTERNAL_PATHS = str.split(":");
        } else {
          EXTERNAL_PATHS = new String[0];
        }
        INTERNAL_CONTENT_URI = getContentUri("internal");
      }
      
      public Media() {}
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/audio/media");
        return Uri.parse(localStringBuilder.toString());
      }
      
      public static Uri getContentUriForPath(String paramString)
      {
        String[] arrayOfString = EXTERNAL_PATHS;
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++) {
          if (paramString.startsWith(arrayOfString[j])) {
            return EXTERNAL_CONTENT_URI;
          }
        }
        if (paramString.startsWith(Environment.getExternalStorageDirectory().getPath())) {
          paramString = EXTERNAL_CONTENT_URI;
        } else {
          paramString = INTERNAL_CONTENT_URI;
        }
        return paramString;
      }
    }
    
    public static final class Playlists
      implements BaseColumns, MediaStore.Audio.PlaylistsColumns
    {
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/playlist";
      public static final String DEFAULT_SORT_ORDER = "name";
      public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/playlist";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      
      public Playlists() {}
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/audio/playlists");
        return Uri.parse(localStringBuilder.toString());
      }
      
      public static final class Members
        implements MediaStore.Audio.AudioColumns
      {
        public static final String AUDIO_ID = "audio_id";
        public static final String CONTENT_DIRECTORY = "members";
        public static final String DEFAULT_SORT_ORDER = "play_order";
        public static final String PLAYLIST_ID = "playlist_id";
        public static final String PLAY_ORDER = "play_order";
        public static final String _ID = "_id";
        
        public Members() {}
        
        public static final Uri getContentUri(String paramString, long paramLong)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("content://media/");
          localStringBuilder.append(paramString);
          localStringBuilder.append("/audio/playlists/");
          localStringBuilder.append(paramLong);
          localStringBuilder.append("/members");
          return Uri.parse(localStringBuilder.toString());
        }
        
        public static final boolean moveItem(ContentResolver paramContentResolver, long paramLong, int paramInt1, int paramInt2)
        {
          Uri localUri = getContentUri("external", paramLong).buildUpon().appendEncodedPath(String.valueOf(paramInt1)).appendQueryParameter("move", "true").build();
          ContentValues localContentValues = new ContentValues();
          localContentValues.put("play_order", Integer.valueOf(paramInt2));
          boolean bool;
          if (paramContentResolver.update(localUri, localContentValues, null, null) != 0) {
            bool = true;
          } else {
            bool = false;
          }
          return bool;
        }
      }
    }
    
    public static abstract interface PlaylistsColumns
    {
      public static final String DATA = "_data";
      public static final String DATE_ADDED = "date_added";
      public static final String DATE_MODIFIED = "date_modified";
      public static final String NAME = "name";
    }
    
    public static final class Radio
    {
      public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/radio";
      
      private Radio() {}
    }
  }
  
  public static final class Files
  {
    public Files() {}
    
    public static Uri getContentUri(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("content://media/");
      localStringBuilder.append(paramString);
      localStringBuilder.append("/file");
      return Uri.parse(localStringBuilder.toString());
    }
    
    public static final Uri getContentUri(String paramString, long paramLong)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("content://media/");
      localStringBuilder.append(paramString);
      localStringBuilder.append("/file/");
      localStringBuilder.append(paramLong);
      return Uri.parse(localStringBuilder.toString());
    }
    
    public static final Uri getDirectoryUri(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("content://media/");
      localStringBuilder.append(paramString);
      localStringBuilder.append("/dir");
      return Uri.parse(localStringBuilder.toString());
    }
    
    public static Uri getMtpObjectsUri(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("content://media/");
      localStringBuilder.append(paramString);
      localStringBuilder.append("/object");
      return Uri.parse(localStringBuilder.toString());
    }
    
    public static final Uri getMtpObjectsUri(String paramString, long paramLong)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("content://media/");
      localStringBuilder.append(paramString);
      localStringBuilder.append("/object/");
      localStringBuilder.append(paramLong);
      return Uri.parse(localStringBuilder.toString());
    }
    
    public static final Uri getMtpReferencesUri(String paramString, long paramLong)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("content://media/");
      localStringBuilder.append(paramString);
      localStringBuilder.append("/object/");
      localStringBuilder.append(paramLong);
      localStringBuilder.append("/references");
      return Uri.parse(localStringBuilder.toString());
    }
    
    public static abstract interface FileColumns
      extends MediaStore.MediaColumns
    {
      public static final String FORMAT = "format";
      public static final String MEDIA_TYPE = "media_type";
      public static final int MEDIA_TYPE_AUDIO = 2;
      public static final int MEDIA_TYPE_IMAGE = 1;
      public static final int MEDIA_TYPE_NONE = 0;
      public static final int MEDIA_TYPE_PLAYLIST = 4;
      public static final int MEDIA_TYPE_VIDEO = 3;
      public static final String MIME_TYPE = "mime_type";
      public static final String PARENT = "parent";
      public static final String STORAGE_ID = "storage_id";
      public static final String TITLE = "title";
    }
  }
  
  public static final class Images
  {
    public Images() {}
    
    public static abstract interface ImageColumns
      extends MediaStore.MediaColumns
    {
      public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
      public static final String BUCKET_ID = "bucket_id";
      public static final String DATE_TAKEN = "datetaken";
      public static final String DESCRIPTION = "description";
      public static final String IS_PRIVATE = "isprivate";
      public static final String LATITUDE = "latitude";
      public static final String LONGITUDE = "longitude";
      public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
      public static final String ORIENTATION = "orientation";
      public static final String PICASA_ID = "picasa_id";
    }
    
    public static final class Media
      implements MediaStore.Images.ImageColumns
    {
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image";
      public static final String DEFAULT_SORT_ORDER = "bucket_display_name";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      
      public Media() {}
      
      private static final Bitmap StoreThumbnail(ContentResolver paramContentResolver, Bitmap paramBitmap, long paramLong, float paramFloat1, float paramFloat2, int paramInt)
      {
        Object localObject = new Matrix();
        ((Matrix)localObject).setScale(paramFloat1 / paramBitmap.getWidth(), paramFloat2 / paramBitmap.getHeight());
        paramBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), (Matrix)localObject, true);
        localObject = new ContentValues(4);
        ((ContentValues)localObject).put("kind", Integer.valueOf(paramInt));
        ((ContentValues)localObject).put("image_id", Integer.valueOf((int)paramLong));
        ((ContentValues)localObject).put("height", Integer.valueOf(paramBitmap.getHeight()));
        ((ContentValues)localObject).put("width", Integer.valueOf(paramBitmap.getWidth()));
        localObject = paramContentResolver.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, (ContentValues)localObject);
        try
        {
          paramContentResolver = paramContentResolver.openOutputStream((Uri)localObject);
          paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, paramContentResolver);
          paramContentResolver.close();
          return paramBitmap;
        }
        catch (IOException paramContentResolver)
        {
          return null;
        }
        catch (FileNotFoundException paramContentResolver) {}
        return null;
      }
      
      public static final Bitmap getBitmap(ContentResolver paramContentResolver, Uri paramUri)
        throws FileNotFoundException, IOException
      {
        paramContentResolver = paramContentResolver.openInputStream(paramUri);
        paramUri = BitmapFactory.decodeStream(paramContentResolver);
        paramContentResolver.close();
        return paramUri;
      }
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/images/media");
        return Uri.parse(localStringBuilder.toString());
      }
      
      /* Error */
      public static final String insertImage(ContentResolver paramContentResolver, Bitmap paramBitmap, String paramString1, String paramString2)
      {
        // Byte code:
        //   0: new 68	android/content/ContentValues
        //   3: dup
        //   4: invokespecial 158	android/content/ContentValues:<init>	()V
        //   7: astore 4
        //   9: aload 4
        //   11: ldc -96
        //   13: aload_2
        //   14: invokevirtual 163	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   17: aload 4
        //   19: ldc -91
        //   21: aload_3
        //   22: invokevirtual 163	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   25: aload 4
        //   27: ldc -89
        //   29: ldc -87
        //   31: invokevirtual 163	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
        //   34: aconst_null
        //   35: astore_3
        //   36: aload_0
        //   37: getstatic 36	android/provider/MediaStore$Images$Media:EXTERNAL_CONTENT_URI	Landroid/net/Uri;
        //   40: aload 4
        //   42: invokevirtual 98	android/content/ContentResolver:insert	(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
        //   45: astore_2
        //   46: aload_1
        //   47: ifnull +70 -> 117
        //   50: aload_0
        //   51: aload_2
        //   52: invokevirtual 102	android/content/ContentResolver:openOutputStream	(Landroid/net/Uri;)Ljava/io/OutputStream;
        //   55: astore 4
        //   57: aload_1
        //   58: getstatic 108	android/graphics/Bitmap$CompressFormat:JPEG	Landroid/graphics/Bitmap$CompressFormat;
        //   61: bipush 50
        //   63: aload 4
        //   65: invokevirtual 112	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
        //   68: pop
        //   69: aload 4
        //   71: invokevirtual 117	java/io/OutputStream:close	()V
        //   74: aload_2
        //   75: invokestatic 175	android/content/ContentUris:parseId	(Landroid/net/Uri;)J
        //   78: lstore 5
        //   80: aload_0
        //   81: aload_0
        //   82: lload 5
        //   84: iconst_1
        //   85: aconst_null
        //   86: invokestatic 179	android/provider/MediaStore$Images$Thumbnails:getThumbnail	(Landroid/content/ContentResolver;JILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   89: lload 5
        //   91: ldc -76
        //   93: ldc -76
        //   95: iconst_3
        //   96: invokestatic 182	android/provider/MediaStore$Images$Media:StoreThumbnail	(Landroid/content/ContentResolver;Landroid/graphics/Bitmap;JFFI)Landroid/graphics/Bitmap;
        //   99: pop
        //   100: aload_2
        //   101: astore_1
        //   102: goto +33 -> 135
        //   105: astore_1
        //   106: aload 4
        //   108: invokevirtual 117	java/io/OutputStream:close	()V
        //   111: aload_1
        //   112: athrow
        //   113: astore_1
        //   114: goto +27 -> 141
        //   117: ldc -72
        //   119: ldc -70
        //   121: invokestatic 192	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
        //   124: pop
        //   125: aload_0
        //   126: aload_2
        //   127: aconst_null
        //   128: aconst_null
        //   129: invokevirtual 196	android/content/ContentResolver:delete	(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
        //   132: pop
        //   133: aconst_null
        //   134: astore_1
        //   135: goto +31 -> 166
        //   138: astore_1
        //   139: aconst_null
        //   140: astore_2
        //   141: ldc -72
        //   143: ldc -58
        //   145: aload_1
        //   146: invokestatic 201	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   149: pop
        //   150: aload_2
        //   151: astore_1
        //   152: aload_2
        //   153: ifnull +13 -> 166
        //   156: aload_0
        //   157: aload_2
        //   158: aconst_null
        //   159: aconst_null
        //   160: invokevirtual 196	android/content/ContentResolver:delete	(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
        //   163: pop
        //   164: aconst_null
        //   165: astore_1
        //   166: aload_3
        //   167: astore_0
        //   168: aload_1
        //   169: ifnull +8 -> 177
        //   172: aload_1
        //   173: invokevirtual 202	android/net/Uri:toString	()Ljava/lang/String;
        //   176: astore_0
        //   177: aload_0
        //   178: areturn
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	179	0	paramContentResolver	ContentResolver
        //   0	179	1	paramBitmap	Bitmap
        //   0	179	2	paramString1	String
        //   0	179	3	paramString2	String
        //   7	100	4	localObject	Object
        //   78	12	5	l	long
        // Exception table:
        //   from	to	target	type
        //   57	69	105	finally
        //   50	57	113	java/lang/Exception
        //   69	74	113	java/lang/Exception
        //   74	100	113	java/lang/Exception
        //   106	113	113	java/lang/Exception
        //   117	133	113	java/lang/Exception
        //   36	46	138	java/lang/Exception
      }
      
      public static final String insertImage(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3)
        throws FileNotFoundException
      {
        FileInputStream localFileInputStream = new FileInputStream(paramString1);
        try
        {
          paramString1 = BitmapFactory.decodeFile(paramString1);
          paramContentResolver = insertImage(paramContentResolver, paramString1, paramString2, paramString3);
          paramString1.recycle();
          return paramContentResolver;
        }
        finally
        {
          try
          {
            localFileInputStream.close();
          }
          catch (IOException paramString1) {}
        }
      }
      
      public static final Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString)
      {
        return paramContentResolver.query(paramUri, paramArrayOfString, null, null, "bucket_display_name");
      }
      
      public static final Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString, String paramString1, String paramString2)
      {
        if (paramString2 == null) {
          paramString2 = "bucket_display_name";
        }
        return paramContentResolver.query(paramUri, paramArrayOfString, paramString1, null, paramString2);
      }
      
      public static final Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
      {
        if (paramString2 == null) {
          paramString2 = "bucket_display_name";
        }
        return paramContentResolver.query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
      }
    }
    
    public static class Thumbnails
      implements BaseColumns
    {
      public static final String DATA = "_data";
      public static final String DEFAULT_SORT_ORDER = "image_id ASC";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final int FULL_SCREEN_KIND = 2;
      public static final String HEIGHT = "height";
      public static final String IMAGE_ID = "image_id";
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      public static final String KIND = "kind";
      public static final int MICRO_KIND = 3;
      public static final int MINI_KIND = 1;
      public static final String THUMB_DATA = "thumb_data";
      public static final String WIDTH = "width";
      
      public Thumbnails() {}
      
      public static void cancelThumbnailRequest(ContentResolver paramContentResolver, long paramLong)
      {
        MediaStore.InternalThumbnails.cancelThumbnailRequest(paramContentResolver, paramLong, EXTERNAL_CONTENT_URI, 0L);
      }
      
      public static void cancelThumbnailRequest(ContentResolver paramContentResolver, long paramLong1, long paramLong2)
      {
        MediaStore.InternalThumbnails.cancelThumbnailRequest(paramContentResolver, paramLong1, EXTERNAL_CONTENT_URI, paramLong2);
      }
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/images/thumbnails");
        return Uri.parse(localStringBuilder.toString());
      }
      
      public static Bitmap getThumbnail(ContentResolver paramContentResolver, long paramLong, int paramInt, BitmapFactory.Options paramOptions)
      {
        return MediaStore.InternalThumbnails.getThumbnail(paramContentResolver, paramLong, 0L, paramInt, paramOptions, EXTERNAL_CONTENT_URI, false);
      }
      
      public static Bitmap getThumbnail(ContentResolver paramContentResolver, long paramLong1, long paramLong2, int paramInt, BitmapFactory.Options paramOptions)
      {
        return MediaStore.InternalThumbnails.getThumbnail(paramContentResolver, paramLong1, paramLong2, paramInt, paramOptions, EXTERNAL_CONTENT_URI, false);
      }
      
      public static final Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString)
      {
        return paramContentResolver.query(paramUri, paramArrayOfString, null, null, "image_id ASC");
      }
      
      public static final Cursor queryMiniThumbnail(ContentResolver paramContentResolver, long paramLong, int paramInt, String[] paramArrayOfString)
      {
        Uri localUri = EXTERNAL_CONTENT_URI;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("image_id = ");
        localStringBuilder.append(paramLong);
        localStringBuilder.append(" AND ");
        localStringBuilder.append("kind");
        localStringBuilder.append(" = ");
        localStringBuilder.append(paramInt);
        return paramContentResolver.query(localUri, paramArrayOfString, localStringBuilder.toString(), null, null);
      }
      
      public static final Cursor queryMiniThumbnails(ContentResolver paramContentResolver, Uri paramUri, int paramInt, String[] paramArrayOfString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("kind = ");
        localStringBuilder.append(paramInt);
        return paramContentResolver.query(paramUri, paramArrayOfString, localStringBuilder.toString(), null, "image_id ASC");
      }
    }
  }
  
  private static class InternalThumbnails
    implements BaseColumns
  {
    static final int DEFAULT_GROUP_ID = 0;
    private static final int FULL_SCREEN_KIND = 2;
    private static final int MICRO_KIND = 3;
    private static final int MINI_KIND = 1;
    private static final String[] PROJECTION = { "_id", "_data" };
    private static byte[] sThumbBuf;
    private static final Object sThumbBufLock = new Object();
    
    private InternalThumbnails() {}
    
    static void cancelThumbnailRequest(ContentResolver paramContentResolver, long paramLong1, Uri paramUri, long paramLong2)
    {
      paramUri = paramUri.buildUpon().appendQueryParameter("cancel", "1").appendQueryParameter("orig_id", String.valueOf(paramLong1)).appendQueryParameter("group_id", String.valueOf(paramLong2)).build();
      try
      {
        paramContentResolver = paramContentResolver.query(paramUri, PROJECTION, null, null, null);
        if (paramContentResolver != null) {
          paramContentResolver.close();
        }
        return;
      }
      finally
      {
        if (0 != 0) {
          throw new NullPointerException();
        }
      }
    }
    
    private static Bitmap getMiniThumbFromFile(Cursor paramCursor, Uri paramUri, ContentResolver paramContentResolver, BitmapFactory.Options paramOptions)
    {
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3 = null;
      Object localObject4 = null;
      Object localObject5 = null;
      Object localObject6 = null;
      Object localObject7 = localObject3;
      Object localObject8 = localObject6;
      Object localObject9 = localObject1;
      Object localObject10 = localObject4;
      Object localObject11 = localObject2;
      Object localObject12 = localObject5;
      try
      {
        long l = paramCursor.getLong(0);
        localObject7 = localObject3;
        localObject8 = localObject6;
        localObject9 = localObject1;
        localObject10 = localObject4;
        localObject11 = localObject2;
        localObject12 = localObject5;
        paramCursor.getString(1);
        localObject7 = localObject3;
        localObject8 = localObject6;
        localObject9 = localObject1;
        localObject10 = localObject4;
        localObject11 = localObject2;
        localObject12 = localObject5;
        paramUri = ContentUris.withAppendedId(paramUri, l);
        localObject7 = localObject3;
        localObject8 = paramUri;
        localObject9 = localObject1;
        localObject10 = paramUri;
        localObject11 = localObject2;
        localObject12 = paramUri;
        paramContentResolver = paramContentResolver.openFileDescriptor(paramUri, "r");
        localObject7 = localObject3;
        localObject8 = paramUri;
        localObject9 = localObject1;
        localObject10 = paramUri;
        localObject11 = localObject2;
        localObject12 = paramUri;
        paramCursor = BitmapFactory.decodeFileDescriptor(paramContentResolver.getFileDescriptor(), null, paramOptions);
        localObject7 = paramCursor;
        localObject8 = paramUri;
        localObject9 = paramCursor;
        localObject10 = paramUri;
        localObject11 = paramCursor;
        localObject12 = paramUri;
        paramContentResolver.close();
      }
      catch (OutOfMemoryError paramUri)
      {
        paramCursor = new StringBuilder();
        paramCursor.append("failed to allocate memory for thumbnail ");
        paramCursor.append(localObject8);
        paramCursor.append("; ");
        paramCursor.append(paramUri);
        Log.e("MediaStore", paramCursor.toString());
        paramCursor = (Cursor)localObject7;
      }
      catch (IOException paramCursor)
      {
        paramUri = new StringBuilder();
        paramUri.append("couldn't open thumbnail ");
        paramUri.append(localObject10);
        paramUri.append("; ");
        paramUri.append(paramCursor);
        Log.e("MediaStore", paramUri.toString());
        paramCursor = (Cursor)localObject9;
      }
      catch (FileNotFoundException paramCursor)
      {
        paramUri = new StringBuilder();
        paramUri.append("couldn't open thumbnail ");
        paramUri.append(localObject12);
        paramUri.append("; ");
        paramUri.append(paramCursor);
        Log.e("MediaStore", paramUri.toString());
        paramCursor = (Cursor)localObject11;
      }
      return paramCursor;
    }
    
    /* Error */
    static Bitmap getThumbnail(ContentResolver paramContentResolver, long paramLong1, long paramLong2, int paramInt, BitmapFactory.Options paramOptions, Uri paramUri, boolean paramBoolean)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore 9
      //   3: aconst_null
      //   4: astore 10
      //   6: aconst_null
      //   7: astore 11
      //   9: aconst_null
      //   10: astore 12
      //   12: aconst_null
      //   13: astore 13
      //   15: iload 8
      //   17: ifeq +11 -> 28
      //   20: getstatic 163	android/provider/MediaStore$Video$Media:EXTERNAL_CONTENT_URI	Landroid/net/Uri;
      //   23: astore 14
      //   25: goto +8 -> 33
      //   28: getstatic 166	android/provider/MediaStore$Images$Media:EXTERNAL_CONTENT_URI	Landroid/net/Uri;
      //   31: astore 14
      //   33: aload 14
      //   35: invokestatic 172	android/media/MiniThumbFile:instance	(Landroid/net/Uri;)Landroid/media/MiniThumbFile;
      //   38: astore 15
      //   40: aconst_null
      //   41: astore 16
      //   43: aconst_null
      //   44: astore 17
      //   46: aconst_null
      //   47: astore 18
      //   49: aconst_null
      //   50: astore 14
      //   52: aload 15
      //   54: lload_1
      //   55: invokevirtual 176	android/media/MiniThumbFile:getMagic	(J)J
      //   58: lstore 19
      //   60: lload 19
      //   62: lconst_0
      //   63: lcmp
      //   64: ifeq +415 -> 479
      //   67: iload 5
      //   69: iconst_3
      //   70: if_icmpne +168 -> 238
      //   73: aload 10
      //   75: astore 6
      //   77: aload 11
      //   79: astore_0
      //   80: getstatic 39	android/provider/MediaStore$InternalThumbnails:sThumbBufLock	Ljava/lang/Object;
      //   83: astore 12
      //   85: aload 10
      //   87: astore 6
      //   89: aload 11
      //   91: astore_0
      //   92: aload 12
      //   94: monitorenter
      //   95: aload 9
      //   97: astore_0
      //   98: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   101: ifnonnull +14 -> 115
      //   104: aload 9
      //   106: astore_0
      //   107: sipush 10000
      //   110: newarray byte
      //   112: putstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   115: aload 13
      //   117: astore 6
      //   119: aload 9
      //   121: astore_0
      //   122: aload 15
      //   124: lload_1
      //   125: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   128: invokevirtual 181	android/media/MiniThumbFile:getMiniThumbFromFile	(J[B)[B
      //   131: ifnull +43 -> 174
      //   134: aload 9
      //   136: astore_0
      //   137: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   140: iconst_0
      //   141: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   144: arraylength
      //   145: invokestatic 185	android/graphics/BitmapFactory:decodeByteArray	([BII)Landroid/graphics/Bitmap;
      //   148: astore 7
      //   150: aload 7
      //   152: astore 6
      //   154: aload 7
      //   156: ifnonnull +18 -> 174
      //   159: aload 7
      //   161: astore_0
      //   162: ldc -115
      //   164: ldc -69
      //   166: invokestatic 190	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   169: pop
      //   170: aload 7
      //   172: astore 6
      //   174: aload 6
      //   176: astore_0
      //   177: aload 12
      //   179: monitorexit
      //   180: aload 14
      //   182: ifnull +10 -> 192
      //   185: aload 14
      //   187: invokeinterface 81 1 0
      //   192: aload 15
      //   194: invokevirtual 193	android/media/MiniThumbFile:deactivate	()V
      //   197: aload 6
      //   199: areturn
      //   200: astore 7
      //   202: aload 12
      //   204: monitorexit
      //   205: aload_0
      //   206: astore 6
      //   208: aload 7
      //   210: athrow
      //   211: astore_0
      //   212: aload 14
      //   214: astore 6
      //   216: goto +1107 -> 1323
      //   219: astore 6
      //   221: aload_0
      //   222: astore 7
      //   224: aload 16
      //   226: astore 14
      //   228: aload 6
      //   230: astore_0
      //   231: aload 14
      //   233: astore 6
      //   235: goto +1060 -> 1295
      //   238: iload 5
      //   240: iconst_1
      //   241: if_icmpne +238 -> 479
      //   244: iload 8
      //   246: ifeq +10 -> 256
      //   249: ldc -61
      //   251: astore 16
      //   253: goto +7 -> 260
      //   256: ldc -59
      //   258: astore 16
      //   260: getstatic 34	android/provider/MediaStore$InternalThumbnails:PROJECTION	[Ljava/lang/String;
      //   263: astore 9
      //   265: new 127	java/lang/StringBuilder
      //   268: astore 13
      //   270: aload 13
      //   272: invokespecial 128	java/lang/StringBuilder:<init>	()V
      //   275: aload 13
      //   277: aload 16
      //   279: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   282: pop
      //   283: aload 13
      //   285: lload_1
      //   286: invokevirtual 200	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   289: pop
      //   290: aload 13
      //   292: invokevirtual 145	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   295: astore 16
      //   297: aconst_null
      //   298: astore 12
      //   300: aconst_null
      //   301: astore 10
      //   303: aconst_null
      //   304: astore 13
      //   306: aload_0
      //   307: aload 7
      //   309: aload 9
      //   311: aload 16
      //   313: aconst_null
      //   314: aconst_null
      //   315: invokevirtual 76	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   318: astore 16
      //   320: aload 16
      //   322: astore 12
      //   324: aload 10
      //   326: astore 14
      //   328: aload 16
      //   330: ifnull +160 -> 490
      //   333: aload 16
      //   335: astore 18
      //   337: aload 16
      //   339: astore 17
      //   341: aload 13
      //   343: astore 9
      //   345: aload 16
      //   347: astore 12
      //   349: aload 10
      //   351: astore 14
      //   353: aload 16
      //   355: invokeinterface 204 1 0
      //   360: ifeq +130 -> 490
      //   363: aload 16
      //   365: astore 18
      //   367: aload 16
      //   369: astore 17
      //   371: aload 13
      //   373: astore 9
      //   375: aload 16
      //   377: aload 7
      //   379: aload_0
      //   380: aload 6
      //   382: invokestatic 206	android/provider/MediaStore$InternalThumbnails:getMiniThumbFromFile	(Landroid/database/Cursor;Landroid/net/Uri;Landroid/content/ContentResolver;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   385: astore 14
      //   387: aload 14
      //   389: ifnull +23 -> 412
      //   392: aload 16
      //   394: ifnull +10 -> 404
      //   397: aload 16
      //   399: invokeinterface 81 1 0
      //   404: aload 15
      //   406: invokevirtual 193	android/media/MiniThumbFile:deactivate	()V
      //   409: aload 14
      //   411: areturn
      //   412: aload 16
      //   414: astore 12
      //   416: goto +74 -> 490
      //   419: astore_0
      //   420: aload 18
      //   422: astore 6
      //   424: goto -208 -> 216
      //   427: astore_0
      //   428: aload 9
      //   430: astore 12
      //   432: aload 17
      //   434: astore 6
      //   436: goto +16 -> 452
      //   439: astore_0
      //   440: aload 14
      //   442: astore 6
      //   444: goto +879 -> 1323
      //   447: astore_0
      //   448: aload 14
      //   450: astore 6
      //   452: aload 12
      //   454: astore 7
      //   456: goto +839 -> 1295
      //   459: astore_0
      //   460: aload 18
      //   462: astore 6
      //   464: goto +859 -> 1323
      //   467: astore_0
      //   468: aload 17
      //   470: astore 6
      //   472: aload 12
      //   474: astore 7
      //   476: goto +819 -> 1295
      //   479: aconst_null
      //   480: astore 16
      //   482: aload 14
      //   484: astore 12
      //   486: aload 16
      //   488: astore 14
      //   490: aload 7
      //   492: invokevirtual 48	android/net/Uri:buildUpon	()Landroid/net/Uri$Builder;
      //   495: ldc -48
      //   497: ldc 52
      //   499: invokevirtual 58	android/net/Uri$Builder:appendQueryParameter	(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   502: ldc 60
      //   504: lload_1
      //   505: invokestatic 64	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   508: invokevirtual 58	android/net/Uri$Builder:appendQueryParameter	(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   511: ldc 66
      //   513: lload_3
      //   514: invokestatic 64	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   517: invokevirtual 58	android/net/Uri$Builder:appendQueryParameter	(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   520: invokevirtual 70	android/net/Uri$Builder:build	()Landroid/net/Uri;
      //   523: astore 16
      //   525: aload 12
      //   527: ifnull +22 -> 549
      //   530: aload 12
      //   532: astore 18
      //   534: aload 12
      //   536: astore 17
      //   538: aload 14
      //   540: astore 9
      //   542: aload 12
      //   544: invokeinterface 81 1 0
      //   549: getstatic 34	android/provider/MediaStore$InternalThumbnails:PROJECTION	[Ljava/lang/String;
      //   552: astore 17
      //   554: aload_0
      //   555: aload 16
      //   557: aload 17
      //   559: aconst_null
      //   560: aconst_null
      //   561: aconst_null
      //   562: invokevirtual 76	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   565: astore 16
      //   567: aload 16
      //   569: ifnonnull +22 -> 591
      //   572: aload 16
      //   574: ifnull +10 -> 584
      //   577: aload 16
      //   579: invokeinterface 81 1 0
      //   584: aload 15
      //   586: invokevirtual 193	android/media/MiniThumbFile:deactivate	()V
      //   589: aconst_null
      //   590: areturn
      //   591: iload 5
      //   593: iconst_3
      //   594: if_icmpne +174 -> 768
      //   597: aload 14
      //   599: astore 17
      //   601: getstatic 39	android/provider/MediaStore$InternalThumbnails:sThumbBufLock	Ljava/lang/Object;
      //   604: astore 18
      //   606: aload 14
      //   608: astore 17
      //   610: aload 18
      //   612: monitorenter
      //   613: aload 14
      //   615: astore 6
      //   617: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   620: ifnonnull +15 -> 635
      //   623: aload 14
      //   625: astore 6
      //   627: sipush 10000
      //   630: newarray byte
      //   632: putstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   635: aload 14
      //   637: astore 6
      //   639: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   642: iconst_0
      //   643: invokestatic 214	java/util/Arrays:fill	([BB)V
      //   646: aload 14
      //   648: astore 12
      //   650: aload 14
      //   652: astore 6
      //   654: aload 15
      //   656: lload_1
      //   657: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   660: invokevirtual 181	android/media/MiniThumbFile:getMiniThumbFromFile	(J[B)[B
      //   663: ifnull +45 -> 708
      //   666: aload 14
      //   668: astore 6
      //   670: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   673: iconst_0
      //   674: getstatic 178	android/provider/MediaStore$InternalThumbnails:sThumbBuf	[B
      //   677: arraylength
      //   678: invokestatic 185	android/graphics/BitmapFactory:decodeByteArray	([BII)Landroid/graphics/Bitmap;
      //   681: astore 14
      //   683: aload 14
      //   685: astore 12
      //   687: aload 14
      //   689: ifnonnull +19 -> 708
      //   692: aload 14
      //   694: astore 6
      //   696: ldc -115
      //   698: ldc -69
      //   700: invokestatic 190	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   703: pop
      //   704: aload 14
      //   706: astore 12
      //   708: aload 12
      //   710: astore 6
      //   712: aload 18
      //   714: monitorexit
      //   715: goto +97 -> 812
      //   718: astore_0
      //   719: aload 18
      //   721: monitorexit
      //   722: aload_0
      //   723: athrow
      //   724: astore_0
      //   725: aload 16
      //   727: astore 14
      //   729: goto -517 -> 212
      //   732: astore_0
      //   733: aload 16
      //   735: astore 14
      //   737: aload 6
      //   739: astore 7
      //   741: goto -510 -> 231
      //   744: astore_0
      //   745: goto -26 -> 719
      //   748: astore_0
      //   749: aload 16
      //   751: astore 6
      //   753: goto -537 -> 216
      //   756: astore_0
      //   757: aload 16
      //   759: astore 6
      //   761: aload 17
      //   763: astore 12
      //   765: goto -329 -> 436
      //   768: iload 5
      //   770: iconst_1
      //   771: if_icmpne +377 -> 1148
      //   774: aload 14
      //   776: astore 17
      //   778: aload 16
      //   780: invokeinterface 204 1 0
      //   785: istore 21
      //   787: aload 14
      //   789: astore 12
      //   791: iload 21
      //   793: ifeq +19 -> 812
      //   796: aload 14
      //   798: astore 17
      //   800: aload 16
      //   802: aload 7
      //   804: aload_0
      //   805: aload 6
      //   807: invokestatic 206	android/provider/MediaStore$InternalThumbnails:getMiniThumbFromFile	(Landroid/database/Cursor;Landroid/net/Uri;Landroid/content/ContentResolver;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   810: astore 12
      //   812: aload 12
      //   814: ifnonnull +284 -> 1098
      //   817: aload 12
      //   819: astore 17
      //   821: new 127	java/lang/StringBuilder
      //   824: astore 6
      //   826: aload 12
      //   828: astore 17
      //   830: aload 6
      //   832: invokespecial 128	java/lang/StringBuilder:<init>	()V
      //   835: aload 12
      //   837: astore 17
      //   839: aload 6
      //   841: ldc -40
      //   843: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   846: pop
      //   847: aload 12
      //   849: astore 17
      //   851: aload 6
      //   853: lload_1
      //   854: invokevirtual 200	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   857: pop
      //   858: aload 12
      //   860: astore 17
      //   862: aload 6
      //   864: ldc -38
      //   866: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   869: pop
      //   870: aload 12
      //   872: astore 17
      //   874: aload 6
      //   876: iload 5
      //   878: invokevirtual 221	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   881: pop
      //   882: aload 12
      //   884: astore 17
      //   886: aload 6
      //   888: ldc -33
      //   890: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   893: pop
      //   894: aload 12
      //   896: astore 17
      //   898: aload 6
      //   900: iload 8
      //   902: invokevirtual 226	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   905: pop
      //   906: aload 12
      //   908: astore 17
      //   910: ldc -115
      //   912: aload 6
      //   914: invokevirtual 145	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   917: invokestatic 229	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   920: pop
      //   921: aload 12
      //   923: astore 17
      //   925: aload 7
      //   927: invokevirtual 48	android/net/Uri:buildUpon	()Landroid/net/Uri$Builder;
      //   930: lload_1
      //   931: invokestatic 64	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   934: invokevirtual 233	android/net/Uri$Builder:appendPath	(Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   937: invokevirtual 234	android/net/Uri$Builder:toString	()Ljava/lang/String;
      //   940: ldc -20
      //   942: ldc -18
      //   944: invokevirtual 242	java/lang/String:replaceFirst	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   947: invokestatic 246	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
      //   950: astore 7
      //   952: aload 16
      //   954: ifnull +14 -> 968
      //   957: aload 12
      //   959: astore 17
      //   961: aload 16
      //   963: invokeinterface 81 1 0
      //   968: aload 12
      //   970: astore 17
      //   972: getstatic 34	android/provider/MediaStore$InternalThumbnails:PROJECTION	[Ljava/lang/String;
      //   975: astore 14
      //   977: aload 12
      //   979: astore 6
      //   981: aload_0
      //   982: aload 7
      //   984: aload 14
      //   986: aconst_null
      //   987: aconst_null
      //   988: aconst_null
      //   989: invokevirtual 76	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   992: astore_0
      //   993: aload_0
      //   994: astore 6
      //   996: aload 6
      //   998: ifnull +81 -> 1079
      //   1001: aload 6
      //   1003: invokeinterface 204 1 0
      //   1008: ifne +6 -> 1014
      //   1011: goto +68 -> 1079
      //   1014: aload 6
      //   1016: iconst_1
      //   1017: invokeinterface 100 2 0
      //   1022: astore 14
      //   1024: aload 6
      //   1026: astore 7
      //   1028: aload 12
      //   1030: astore_0
      //   1031: aload 14
      //   1033: ifnull +72 -> 1105
      //   1036: iload 8
      //   1038: ifeq +18 -> 1056
      //   1041: aload 14
      //   1043: iload 5
      //   1045: invokestatic 252	android/media/ThumbnailUtils:createVideoThumbnail	(Ljava/lang/String;I)Landroid/graphics/Bitmap;
      //   1048: astore_0
      //   1049: aload 6
      //   1051: astore 7
      //   1053: goto +52 -> 1105
      //   1056: aload 14
      //   1058: iload 5
      //   1060: invokestatic 255	android/media/ThumbnailUtils:createImageThumbnail	(Ljava/lang/String;I)Landroid/graphics/Bitmap;
      //   1063: astore_0
      //   1064: aload 6
      //   1066: astore 7
      //   1068: goto +37 -> 1105
      //   1071: astore_0
      //   1072: goto +251 -> 1323
      //   1075: astore_0
      //   1076: goto -624 -> 452
      //   1079: aload 6
      //   1081: ifnull +10 -> 1091
      //   1084: aload 6
      //   1086: invokeinterface 81 1 0
      //   1091: aload 15
      //   1093: invokevirtual 193	android/media/MiniThumbFile:deactivate	()V
      //   1096: aconst_null
      //   1097: areturn
      //   1098: aload 16
      //   1100: astore 7
      //   1102: aload 12
      //   1104: astore_0
      //   1105: aload 7
      //   1107: ifnull +10 -> 1117
      //   1110: aload 7
      //   1112: invokeinterface 81 1 0
      //   1117: aload 15
      //   1119: invokevirtual 193	android/media/MiniThumbFile:deactivate	()V
      //   1122: aload_0
      //   1123: astore 7
      //   1125: goto +194 -> 1319
      //   1128: astore_0
      //   1129: aload 16
      //   1131: astore 6
      //   1133: goto +190 -> 1323
      //   1136: astore_0
      //   1137: aload 17
      //   1139: astore 7
      //   1141: aload 16
      //   1143: astore 6
      //   1145: goto +150 -> 1295
      //   1148: aload 14
      //   1150: astore 6
      //   1152: new 257	java/lang/IllegalArgumentException
      //   1155: astore_0
      //   1156: aload 14
      //   1158: astore 6
      //   1160: new 127	java/lang/StringBuilder
      //   1163: astore 7
      //   1165: aload 14
      //   1167: astore 6
      //   1169: aload 7
      //   1171: invokespecial 128	java/lang/StringBuilder:<init>	()V
      //   1174: aload 14
      //   1176: astore 6
      //   1178: aload 7
      //   1180: ldc_w 259
      //   1183: invokevirtual 134	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1186: pop
      //   1187: aload 14
      //   1189: astore 6
      //   1191: aload 7
      //   1193: iload 5
      //   1195: invokevirtual 221	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   1198: pop
      //   1199: aload 14
      //   1201: astore 6
      //   1203: aload_0
      //   1204: aload 7
      //   1206: invokevirtual 145	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1209: invokespecial 262	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
      //   1212: aload 14
      //   1214: astore 6
      //   1216: aload_0
      //   1217: athrow
      //   1218: astore_0
      //   1219: aload 16
      //   1221: astore 6
      //   1223: goto +100 -> 1323
      //   1226: astore_0
      //   1227: aload 6
      //   1229: astore 7
      //   1231: aload 16
      //   1233: astore 6
      //   1235: goto +60 -> 1295
      //   1238: astore_0
      //   1239: aload 12
      //   1241: astore 6
      //   1243: goto +80 -> 1323
      //   1246: astore_0
      //   1247: aload 12
      //   1249: astore 6
      //   1251: aload 14
      //   1253: astore 7
      //   1255: goto +40 -> 1295
      //   1258: astore_0
      //   1259: aload 12
      //   1261: astore 6
      //   1263: goto +60 -> 1323
      //   1266: astore_0
      //   1267: aload 14
      //   1269: astore 7
      //   1271: aload 12
      //   1273: astore 6
      //   1275: goto +20 -> 1295
      //   1278: astore_0
      //   1279: aload 18
      //   1281: astore 6
      //   1283: goto +40 -> 1323
      //   1286: astore_0
      //   1287: aload 12
      //   1289: astore 7
      //   1291: aload 17
      //   1293: astore 6
      //   1295: ldc -115
      //   1297: aload_0
      //   1298: invokestatic 265	android/util/Log:w	(Ljava/lang/String;Ljava/lang/Throwable;)I
      //   1301: pop
      //   1302: aload 6
      //   1304: ifnull +10 -> 1314
      //   1307: aload 6
      //   1309: invokeinterface 81 1 0
      //   1314: aload 15
      //   1316: invokevirtual 193	android/media/MiniThumbFile:deactivate	()V
      //   1319: aload 7
      //   1321: areturn
      //   1322: astore_0
      //   1323: aload 6
      //   1325: ifnull +10 -> 1335
      //   1328: aload 6
      //   1330: invokeinterface 81 1 0
      //   1335: aload 15
      //   1337: invokevirtual 193	android/media/MiniThumbFile:deactivate	()V
      //   1340: aload_0
      //   1341: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	1342	0	paramContentResolver	ContentResolver
      //   0	1342	1	paramLong1	long
      //   0	1342	3	paramLong2	long
      //   0	1342	5	paramInt	int
      //   0	1342	6	paramOptions	BitmapFactory.Options
      //   0	1342	7	paramUri	Uri
      //   0	1342	8	paramBoolean	boolean
      //   1	540	9	localObject1	Object
      //   4	346	10	localObject2	Object
      //   7	83	11	localObject3	Object
      //   10	1278	12	localObject4	Object
      //   13	359	13	localStringBuilder	StringBuilder
      //   23	1245	14	localObject5	Object
      //   38	1298	15	localMiniThumbFile	android.media.MiniThumbFile
      //   41	1191	16	localObject6	Object
      //   44	1248	17	localObject7	Object
      //   47	1233	18	localObject8	Object
      //   58	3	19	l	long
      //   785	7	21	bool	boolean
      // Exception table:
      //   from	to	target	type
      //   98	104	200	finally
      //   107	115	200	finally
      //   122	134	200	finally
      //   137	150	200	finally
      //   162	170	200	finally
      //   177	180	200	finally
      //   202	205	200	finally
      //   80	85	211	finally
      //   92	95	211	finally
      //   208	211	211	finally
      //   80	85	219	android/database/sqlite/SQLiteException
      //   92	95	219	android/database/sqlite/SQLiteException
      //   208	211	219	android/database/sqlite/SQLiteException
      //   353	363	419	finally
      //   375	387	419	finally
      //   542	549	419	finally
      //   353	363	427	android/database/sqlite/SQLiteException
      //   375	387	427	android/database/sqlite/SQLiteException
      //   542	549	427	android/database/sqlite/SQLiteException
      //   306	320	439	finally
      //   306	320	447	android/database/sqlite/SQLiteException
      //   260	297	459	finally
      //   260	297	467	android/database/sqlite/SQLiteException
      //   617	623	718	finally
      //   627	635	718	finally
      //   639	646	718	finally
      //   654	666	718	finally
      //   670	683	718	finally
      //   696	704	718	finally
      //   712	715	718	finally
      //   722	724	724	finally
      //   722	724	732	android/database/sqlite/SQLiteException
      //   719	722	744	finally
      //   601	606	748	finally
      //   610	613	748	finally
      //   800	812	748	finally
      //   961	968	748	finally
      //   601	606	756	android/database/sqlite/SQLiteException
      //   610	613	756	android/database/sqlite/SQLiteException
      //   800	812	756	android/database/sqlite/SQLiteException
      //   961	968	756	android/database/sqlite/SQLiteException
      //   1001	1011	1071	finally
      //   1014	1024	1071	finally
      //   1041	1049	1071	finally
      //   1056	1064	1071	finally
      //   1001	1011	1075	android/database/sqlite/SQLiteException
      //   1014	1024	1075	android/database/sqlite/SQLiteException
      //   1041	1049	1075	android/database/sqlite/SQLiteException
      //   1056	1064	1075	android/database/sqlite/SQLiteException
      //   778	787	1128	finally
      //   821	826	1128	finally
      //   830	835	1128	finally
      //   839	847	1128	finally
      //   851	858	1128	finally
      //   862	870	1128	finally
      //   874	882	1128	finally
      //   886	894	1128	finally
      //   898	906	1128	finally
      //   910	921	1128	finally
      //   925	952	1128	finally
      //   972	977	1128	finally
      //   778	787	1136	android/database/sqlite/SQLiteException
      //   821	826	1136	android/database/sqlite/SQLiteException
      //   830	835	1136	android/database/sqlite/SQLiteException
      //   839	847	1136	android/database/sqlite/SQLiteException
      //   851	858	1136	android/database/sqlite/SQLiteException
      //   862	870	1136	android/database/sqlite/SQLiteException
      //   874	882	1136	android/database/sqlite/SQLiteException
      //   886	894	1136	android/database/sqlite/SQLiteException
      //   898	906	1136	android/database/sqlite/SQLiteException
      //   910	921	1136	android/database/sqlite/SQLiteException
      //   925	952	1136	android/database/sqlite/SQLiteException
      //   972	977	1136	android/database/sqlite/SQLiteException
      //   981	993	1218	finally
      //   1152	1156	1218	finally
      //   1160	1165	1218	finally
      //   1169	1174	1218	finally
      //   1178	1187	1218	finally
      //   1191	1199	1218	finally
      //   1203	1212	1218	finally
      //   1216	1218	1218	finally
      //   981	993	1226	android/database/sqlite/SQLiteException
      //   1152	1156	1226	android/database/sqlite/SQLiteException
      //   1160	1165	1226	android/database/sqlite/SQLiteException
      //   1169	1174	1226	android/database/sqlite/SQLiteException
      //   1178	1187	1226	android/database/sqlite/SQLiteException
      //   1191	1199	1226	android/database/sqlite/SQLiteException
      //   1203	1212	1226	android/database/sqlite/SQLiteException
      //   1216	1218	1226	android/database/sqlite/SQLiteException
      //   554	567	1238	finally
      //   554	567	1246	android/database/sqlite/SQLiteException
      //   490	525	1258	finally
      //   549	554	1258	finally
      //   490	525	1266	android/database/sqlite/SQLiteException
      //   549	554	1266	android/database/sqlite/SQLiteException
      //   52	60	1278	finally
      //   52	60	1286	android/database/sqlite/SQLiteException
      //   1295	1302	1322	finally
    }
  }
  
  public static abstract interface MediaColumns
    extends BaseColumns
  {
    public static final String DATA = "_data";
    public static final String DATE_ADDED = "date_added";
    public static final String DATE_MODIFIED = "date_modified";
    public static final String DISPLAY_NAME = "_display_name";
    public static final String HEIGHT = "height";
    public static final String IS_DRM = "is_drm";
    public static final String MEDIA_SCANNER_NEW_OBJECT_ID = "media_scanner_new_object_id";
    public static final String MIME_TYPE = "mime_type";
    public static final String SIZE = "_size";
    public static final String TITLE = "title";
    public static final String WIDTH = "width";
  }
  
  public static final class Video
  {
    public static final String DEFAULT_SORT_ORDER = "_display_name";
    
    public Video() {}
    
    public static final Cursor query(ContentResolver paramContentResolver, Uri paramUri, String[] paramArrayOfString)
    {
      return paramContentResolver.query(paramUri, paramArrayOfString, null, null, "_display_name");
    }
    
    public static final class Media
      implements MediaStore.Video.VideoColumns
    {
      public static final String CONTENT_TYPE = "vnd.android.cursor.dir/video";
      public static final String DEFAULT_SORT_ORDER = "title";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      
      public Media() {}
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/video/media");
        return Uri.parse(localStringBuilder.toString());
      }
    }
    
    public static class Thumbnails
      implements BaseColumns
    {
      public static final String DATA = "_data";
      public static final String DEFAULT_SORT_ORDER = "video_id ASC";
      public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");
      public static final int FULL_SCREEN_KIND = 2;
      public static final String HEIGHT = "height";
      public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
      public static final String KIND = "kind";
      public static final int MICRO_KIND = 3;
      public static final int MINI_KIND = 1;
      public static final String VIDEO_ID = "video_id";
      public static final String WIDTH = "width";
      
      public Thumbnails() {}
      
      public static void cancelThumbnailRequest(ContentResolver paramContentResolver, long paramLong)
      {
        MediaStore.InternalThumbnails.cancelThumbnailRequest(paramContentResolver, paramLong, EXTERNAL_CONTENT_URI, 0L);
      }
      
      public static void cancelThumbnailRequest(ContentResolver paramContentResolver, long paramLong1, long paramLong2)
      {
        MediaStore.InternalThumbnails.cancelThumbnailRequest(paramContentResolver, paramLong1, EXTERNAL_CONTENT_URI, paramLong2);
      }
      
      public static Uri getContentUri(String paramString)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("content://media/");
        localStringBuilder.append(paramString);
        localStringBuilder.append("/video/thumbnails");
        return Uri.parse(localStringBuilder.toString());
      }
      
      public static Bitmap getThumbnail(ContentResolver paramContentResolver, long paramLong, int paramInt, BitmapFactory.Options paramOptions)
      {
        return MediaStore.InternalThumbnails.getThumbnail(paramContentResolver, paramLong, 0L, paramInt, paramOptions, EXTERNAL_CONTENT_URI, true);
      }
      
      public static Bitmap getThumbnail(ContentResolver paramContentResolver, long paramLong1, long paramLong2, int paramInt, BitmapFactory.Options paramOptions)
      {
        return MediaStore.InternalThumbnails.getThumbnail(paramContentResolver, paramLong1, paramLong2, paramInt, paramOptions, EXTERNAL_CONTENT_URI, true);
      }
    }
    
    public static abstract interface VideoColumns
      extends MediaStore.MediaColumns
    {
      public static final String ALBUM = "album";
      public static final String ARTIST = "artist";
      public static final String BOOKMARK = "bookmark";
      public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
      public static final String BUCKET_ID = "bucket_id";
      public static final String CATEGORY = "category";
      public static final String DATE_TAKEN = "datetaken";
      public static final String DESCRIPTION = "description";
      public static final String DURATION = "duration";
      public static final String IS_PRIVATE = "isprivate";
      public static final String LANGUAGE = "language";
      public static final String LATITUDE = "latitude";
      public static final String LONGITUDE = "longitude";
      public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
      public static final String RESOLUTION = "resolution";
      public static final String TAGS = "tags";
    }
  }
}
