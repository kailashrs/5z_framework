package android.media;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.drm.DrmManagerClient;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video.Media;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.RootElement;
import android.text.TextUtils;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

public class MediaScanner
  implements AutoCloseable
{
  private static final String ALARMS_DIR = "/alarms/";
  private static final int DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX = 2;
  private static final String DEFAULT_RINGTONE_PROPERTY_PREFIX = "ro.config.";
  private static final boolean ENABLE_BULK_INSERTS = true;
  private static final int FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX = 3;
  private static final int FILES_PRESCAN_FORMAT_COLUMN_INDEX = 2;
  private static final int FILES_PRESCAN_ID_COLUMN_INDEX = 0;
  private static final int FILES_PRESCAN_PATH_COLUMN_INDEX = 1;
  private static final String[] FILES_PRESCAN_PROJECTION;
  private static final String[] ID3_GENRES;
  private static final int ID_PLAYLISTS_COLUMN_INDEX = 0;
  private static final String[] ID_PROJECTION;
  public static final String LAST_INTERNAL_SCAN_FINGERPRINT = "lastScanFingerprint";
  private static final String MUSIC_DIR = "/music/";
  private static final String NOTIFICATIONS_DIR = "/notifications/";
  private static final int PATH_PLAYLISTS_COLUMN_INDEX = 1;
  private static final String[] PLAYLIST_MEMBERS_PROJECTION;
  private static final String PODCAST_DIR = "/podcasts/";
  private static final String PRODUCT_SOUNDS_DIR = "/product/media/audio";
  private static final String RINGTONES_DIR = "/ringtones/";
  public static final String SCANNED_BUILD_PREFS_NAME = "MediaScanBuild";
  private static final String SYSTEM_SOUNDS_DIR = "/system/media/audio";
  private static final String TAG = "MediaScanner";
  private static HashMap<String, String> mMediaPaths = new HashMap();
  private static HashMap<String, String> mNoMediaPaths;
  private static String sLastInternalScanFingerprint;
  private final Uri mAudioUri;
  private final BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
  private final MyMediaScannerClient mClient = new MyMediaScannerClient();
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mClosed = new AtomicBoolean();
  private final Context mContext;
  private String mDefaultAlarmAlertFilename;
  private boolean mDefaultAlarmSet;
  private String mDefaultCalendarAlertFilename;
  private boolean mDefaultCalendarAlertSet;
  private String mDefaultNewMailFilename;
  private boolean mDefaultNewMailSet;
  private String mDefaultNotification2Filename;
  private String mDefaultNotificationFilename;
  private boolean mDefaultNotificationSet;
  private String mDefaultRingtone2Filename;
  private String mDefaultRingtoneFilename;
  private boolean mDefaultRingtoneSet;
  private String mDefaultSentMailFilename;
  private boolean mDefaultSentMailSet;
  private DrmManagerClient mDrmManagerClient = null;
  private boolean mEnableTwinApps = false;
  private final Uri mFilesUri;
  private final Uri mFilesUriNoNotify;
  private final Uri mImagesUri;
  private MediaInserter mMediaInserter;
  private final ContentProviderClient mMediaProvider;
  private int mMtpObjectHandle;
  private long mNativeContext;
  private int mOriginalCount;
  private final String mPackageName;
  private final ArrayList<FileEntry> mPlayLists = new ArrayList();
  private final ArrayList<PlaylistEntry> mPlaylistEntries = new ArrayList();
  private final Uri mPlaylistsUri;
  private final boolean mProcessGenres;
  private final boolean mProcessPlaylists;
  private final Uri mVideoUri;
  private final String mVolumeName;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
    FILES_PRESCAN_PROJECTION = new String[] { "_id", "_data", "format", "date_modified" };
    ID_PROJECTION = new String[] { "_id" };
    PLAYLIST_MEMBERS_PROJECTION = new String[] { "playlist_id" };
    ID3_GENRES = new String[] { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "Britpop", null, "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "Synthpop" };
    mNoMediaPaths = new HashMap();
  }
  
  public MediaScanner(Context paramContext, String paramString)
  {
    native_setup();
    mContext = paramContext;
    mPackageName = paramContext.getPackageName();
    mVolumeName = paramString;
    mBitmapOptions.inSampleSize = 1;
    mBitmapOptions.inJustDecodeBounds = true;
    setDefaultRingtoneFileNames();
    mMediaProvider = mContext.getContentResolver().acquireContentProviderClient("media");
    if (sLastInternalScanFingerprint == null) {
      sLastInternalScanFingerprint = mContext.getSharedPreferences("MediaScanBuild", 0).getString("lastScanFingerprint", new String());
    }
    mAudioUri = MediaStore.Audio.Media.getContentUri(paramString);
    mVideoUri = MediaStore.Video.Media.getContentUri(paramString);
    mImagesUri = MediaStore.Images.Media.getContentUri(paramString);
    mFilesUri = MediaStore.Files.getContentUri(paramString);
    mFilesUriNoNotify = mFilesUri.buildUpon().appendQueryParameter("nonotify", "1").build();
    if (!paramString.equals("internal"))
    {
      mProcessPlaylists = true;
      mProcessGenres = true;
      mPlaylistsUri = MediaStore.Audio.Playlists.getContentUri(paramString);
    }
    else
    {
      mProcessPlaylists = false;
      mProcessGenres = false;
      mPlaylistsUri = null;
    }
    paramString = mContext.getResources().getConfiguration().locale;
    if (paramString != null)
    {
      paramContext = paramString.getLanguage();
      paramString = paramString.getCountry();
      if (paramContext != null) {
        if (paramString != null)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(paramContext);
          localStringBuilder.append("_");
          localStringBuilder.append(paramString);
          setLocale(localStringBuilder.toString());
        }
        else
        {
          setLocale(paramContext);
        }
      }
    }
    mCloseGuard.open("close");
    mEnableTwinApps = mContext.getPackageManager().hasSystemFeature("asus.software.twinapps");
  }
  
  private void cachePlaylistEntry(String paramString1, String paramString2)
  {
    PlaylistEntry localPlaylistEntry = new PlaylistEntry(null);
    for (int i = paramString1.length(); (i > 0) && (Character.isWhitespace(paramString1.charAt(i - 1))); i--) {}
    if (i < 3) {
      return;
    }
    int j = paramString1.length();
    int k = 0;
    String str = paramString1;
    if (i < j) {
      str = paramString1.substring(0, i);
    }
    char c = str.charAt(0);
    if ((c != '/') && ((!Character.isLetter(c)) || (str.charAt(1) != ':') || (str.charAt(2) != '\\'))) {
      i = k;
    } else {
      i = 1;
    }
    paramString1 = str;
    if (i == 0)
    {
      paramString1 = new StringBuilder();
      paramString1.append(paramString2);
      paramString1.append(str);
      paramString1 = paramString1.toString();
    }
    path = paramString1;
    mPlaylistEntries.add(localPlaylistEntry);
  }
  
  public static void clearMediaPathCache(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      try
      {
        mMediaPaths.clear();
      }
      finally
      {
        break label34;
      }
    }
    if (paramBoolean2) {
      mNoMediaPaths.clear();
    }
    return;
    label34:
    throw localObject;
  }
  
  private boolean isDrmEnabled()
  {
    String str = SystemProperties.get("drm.service.enabled");
    boolean bool;
    if ((str != null) && (str.equals("true"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isNoMediaFile(String paramString)
  {
    if (new File(paramString).isDirectory()) {
      return false;
    }
    int i = paramString.lastIndexOf('/');
    if ((i >= 0) && (i + 2 < paramString.length()))
    {
      if (paramString.regionMatches(i + 1, "._", 0, 2)) {
        return true;
      }
      if (paramString.regionMatches(true, paramString.length() - 4, ".jpg", 0, 4)) {
        if ((!paramString.regionMatches(true, i + 1, "AlbumArt_{", 0, 10)) && (!paramString.regionMatches(true, i + 1, "AlbumArt.", 0, 9)))
        {
          int j = paramString.length() - i - 1;
          if (((j == 17) && (paramString.regionMatches(true, i + 1, "AlbumArtSmall", 0, 13))) || ((j == 10) && (paramString.regionMatches(true, i + 1, "Folder", 0, 6)))) {
            return true;
          }
        }
        else
        {
          return true;
        }
      }
    }
    return false;
  }
  
  public static boolean isNoMediaPath(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.indexOf("/.") >= 0) {
      return true;
    }
    int i = paramString.lastIndexOf('/');
    if (i <= 0) {
      return false;
    }
    String str = paramString.substring(0, i);
    try
    {
      if (mNoMediaPaths.containsKey(str)) {
        return true;
      }
      if (!mMediaPaths.containsKey(str))
      {
        for (int j = 1; j >= 0; j = i)
        {
          int k = paramString.indexOf('/', j);
          i = k;
          if (k > j)
          {
            i = k + 1;
            File localFile = new java/io/File;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append(paramString.substring(0, i));
            localStringBuilder.append(".nomedia");
            localFile.<init>(localStringBuilder.toString());
            if (localFile.exists())
            {
              mNoMediaPaths.put(str, "");
              return true;
            }
          }
        }
        mMediaPaths.put(str, "");
      }
      return isNoMediaFile(paramString);
    }
    finally {}
  }
  
  private static boolean isSystemSoundWithMetadata(String paramString)
  {
    return (paramString.startsWith("/system/media/audio/alarms/")) || (paramString.startsWith("/system/media/audio/ringtones/")) || (paramString.startsWith("/system/media/audio/notifications/")) || (paramString.startsWith("/product/media/audio/alarms/")) || (paramString.startsWith("/product/media/audio/ringtones/")) || (paramString.startsWith("/product/media/audio/notifications/"));
  }
  
  private boolean matchEntries(long paramLong, String paramString)
  {
    int i = mPlaylistEntries.size();
    boolean bool1 = true;
    for (int j = 0; j < i; j++)
    {
      PlaylistEntry localPlaylistEntry = (PlaylistEntry)mPlaylistEntries.get(j);
      if (bestmatchlevel != Integer.MAX_VALUE)
      {
        boolean bool2 = false;
        if (paramString.equalsIgnoreCase(path))
        {
          bestmatchid = paramLong;
          bestmatchlevel = Integer.MAX_VALUE;
          bool1 = bool2;
        }
        else
        {
          int k = matchPaths(paramString, path);
          bool1 = bool2;
          if (k > bestmatchlevel)
          {
            bestmatchid = paramLong;
            bestmatchlevel = k;
            bool1 = bool2;
          }
        }
      }
    }
    return bool1;
  }
  
  private int matchPaths(String paramString1, String paramString2)
  {
    int i = paramString1.length();
    int j = paramString2.length();
    int k = 0;
    while ((i > 0) && (j > 0))
    {
      int m = paramString1.lastIndexOf('/', i - 1);
      int n = paramString2.lastIndexOf('/', j - 1);
      int i1 = paramString1.lastIndexOf('\\', i - 1);
      int i2 = paramString2.lastIndexOf('\\', j - 1);
      if (m <= i1) {
        m = i1;
      }
      if (n > i2) {
        i2 = n;
      }
      if (m < 0) {}
      for (m = 0;; m++) {
        break;
      }
      if (i2 < 0) {
        i2 = 0;
      } else {
        i2++;
      }
      i -= m;
      if ((j - i2 != i) || (!paramString1.regionMatches(true, m, paramString2, i2, i))) {
        break;
      }
      k++;
      i = m - 1;
      j = i2 - 1;
    }
    return k;
  }
  
  private final native void native_finalize();
  
  private static final native void native_init();
  
  private final native void native_setup();
  
  private void postscan(String[] paramArrayOfString)
    throws RemoteException
  {
    if (mProcessPlaylists) {
      processPlayLists();
    }
    mPlayLists.clear();
  }
  
  /* Error */
  private void prescan(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aconst_null
    //   6: astore 5
    //   8: aload_0
    //   9: getfield 498	android/media/MediaScanner:mPlayLists	Ljava/util/ArrayList;
    //   12: invokevirtual 934	java/util/ArrayList:clear	()V
    //   15: aload_1
    //   16: ifnull +27 -> 43
    //   19: ldc_w 941
    //   22: astore 6
    //   24: iconst_2
    //   25: anewarray 147	java/lang/String
    //   28: dup
    //   29: iconst_0
    //   30: ldc_w 882
    //   33: aastore
    //   34: dup
    //   35: iconst_1
    //   36: aload_1
    //   37: aastore
    //   38: astore 7
    //   40: goto +23 -> 63
    //   43: ldc_w 943
    //   46: astore 6
    //   48: iconst_1
    //   49: anewarray 147	java/lang/String
    //   52: dup
    //   53: iconst_0
    //   54: ldc_w 882
    //   57: aastore
    //   58: astore 7
    //   60: goto -20 -> 40
    //   63: aload_0
    //   64: aload_0
    //   65: ldc_w 945
    //   68: invokespecial 749	android/media/MediaScanner:wasRingtoneAlreadySet	(Ljava/lang/String;)Z
    //   71: putfield 781	android/media/MediaScanner:mDefaultRingtoneSet	Z
    //   74: aload_0
    //   75: aload_0
    //   76: ldc_w 947
    //   79: invokespecial 749	android/media/MediaScanner:wasRingtoneAlreadySet	(Ljava/lang/String;)Z
    //   82: putfield 774	android/media/MediaScanner:mDefaultNotificationSet	Z
    //   85: aload_0
    //   86: aload_0
    //   87: ldc_w 949
    //   90: invokespecial 749	android/media/MediaScanner:wasRingtoneAlreadySet	(Ljava/lang/String;)Z
    //   93: putfield 788	android/media/MediaScanner:mDefaultAlarmSet	Z
    //   96: aload_0
    //   97: getfield 579	android/media/MediaScanner:mFilesUri	Landroid/net/Uri;
    //   100: invokevirtual 585	android/net/Uri:buildUpon	()Landroid/net/Uri$Builder;
    //   103: astore 8
    //   105: aload 8
    //   107: ldc_w 951
    //   110: ldc_w 953
    //   113: invokevirtual 595	android/net/Uri$Builder:appendQueryParameter	(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   116: pop
    //   117: new 13	android/media/MediaScanner$MediaBulkDeleter
    //   120: dup
    //   121: aload_0
    //   122: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   125: aload 8
    //   127: invokevirtual 599	android/net/Uri$Builder:build	()Landroid/net/Uri;
    //   130: invokespecial 956	android/media/MediaScanner$MediaBulkDeleter:<init>	(Landroid/content/ContentProviderClient;Landroid/net/Uri;)V
    //   133: astore_1
    //   134: iload_2
    //   135: ifeq +651 -> 786
    //   138: aload_3
    //   139: astore 4
    //   141: aload 6
    //   143: astore 9
    //   145: aload_1
    //   146: astore 9
    //   148: aload 8
    //   150: astore 10
    //   152: aload_0
    //   153: getfield 579	android/media/MediaScanner:mFilesUri	Landroid/net/Uri;
    //   156: invokevirtual 585	android/net/Uri:buildUpon	()Landroid/net/Uri$Builder;
    //   159: ldc_w 958
    //   162: ldc_w 960
    //   165: invokevirtual 595	android/net/Uri$Builder:appendQueryParameter	(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
    //   168: invokevirtual 599	android/net/Uri$Builder:build	()Landroid/net/Uri;
    //   171: astore 11
    //   173: ldc2_w 961
    //   176: lstore 12
    //   178: aload 5
    //   180: astore 4
    //   182: aload 6
    //   184: astore 9
    //   186: aload_1
    //   187: astore 9
    //   189: aload 8
    //   191: astore 10
    //   193: new 642	java/lang/StringBuilder
    //   196: astore_3
    //   197: aload 5
    //   199: astore 4
    //   201: aload 6
    //   203: astore 9
    //   205: aload_1
    //   206: astore 9
    //   208: aload 8
    //   210: astore 10
    //   212: aload_3
    //   213: invokespecial 643	java/lang/StringBuilder:<init>	()V
    //   216: aload 5
    //   218: astore 4
    //   220: aload 6
    //   222: astore 9
    //   224: aload_1
    //   225: astore 9
    //   227: aload 8
    //   229: astore 10
    //   231: aload_3
    //   232: ldc_w 882
    //   235: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   238: pop
    //   239: aload 5
    //   241: astore 4
    //   243: aload 6
    //   245: astore 9
    //   247: aload_1
    //   248: astore 9
    //   250: aload 8
    //   252: astore 10
    //   254: aload_3
    //   255: lload 12
    //   257: invokevirtual 965	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   260: pop
    //   261: aload 5
    //   263: astore 4
    //   265: aload 6
    //   267: astore 9
    //   269: aload_1
    //   270: astore 9
    //   272: aload 8
    //   274: astore 10
    //   276: aload 7
    //   278: iconst_0
    //   279: aload_3
    //   280: invokevirtual 652	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   283: aastore
    //   284: aload 5
    //   286: astore_3
    //   287: aload 5
    //   289: ifnull +26 -> 315
    //   292: aload 5
    //   294: invokeinterface 969 1 0
    //   299: aconst_null
    //   300: astore_3
    //   301: goto +14 -> 315
    //   304: astore 4
    //   306: aload_1
    //   307: astore 6
    //   309: aload 4
    //   311: astore_1
    //   312: goto +455 -> 767
    //   315: aload_3
    //   316: astore 4
    //   318: aload 6
    //   320: astore 9
    //   322: aload_1
    //   323: astore 9
    //   325: aload 8
    //   327: astore 10
    //   329: aload_0
    //   330: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   333: astore 5
    //   335: aload_3
    //   336: astore 4
    //   338: aload 6
    //   340: astore 9
    //   342: aload_1
    //   343: astore 9
    //   345: aload 8
    //   347: astore 10
    //   349: getstatic 157	android/media/MediaScanner:FILES_PRESCAN_PROJECTION	[Ljava/lang/String;
    //   352: astore 10
    //   354: aload 5
    //   356: aload 11
    //   358: aload 10
    //   360: aload 6
    //   362: aload 7
    //   364: ldc -107
    //   366: aconst_null
    //   367: invokevirtual 975	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   370: astore 5
    //   372: aload 5
    //   374: ifnonnull +6 -> 380
    //   377: goto +22 -> 399
    //   380: aload 5
    //   382: astore 4
    //   384: aload 6
    //   386: astore_3
    //   387: aload_1
    //   388: astore_3
    //   389: aload 5
    //   391: invokeinterface 978 1 0
    //   396: ifne +6 -> 402
    //   399: goto +391 -> 790
    //   402: aload 5
    //   404: astore 4
    //   406: aload 6
    //   408: astore_3
    //   409: aload_1
    //   410: astore_3
    //   411: aload 5
    //   413: invokeinterface 981 1 0
    //   418: ifeq +312 -> 730
    //   421: aload 5
    //   423: astore 4
    //   425: aload 6
    //   427: astore_3
    //   428: aload_1
    //   429: astore_3
    //   430: aload 5
    //   432: iconst_0
    //   433: invokeinterface 985 2 0
    //   438: lstore 14
    //   440: aload 5
    //   442: astore 4
    //   444: aload 6
    //   446: astore_3
    //   447: aload_1
    //   448: astore_3
    //   449: aload 5
    //   451: iconst_1
    //   452: invokeinterface 988 2 0
    //   457: astore 9
    //   459: aload 5
    //   461: astore 4
    //   463: aload 6
    //   465: astore_3
    //   466: aload_1
    //   467: astore_3
    //   468: aload 5
    //   470: iconst_2
    //   471: invokeinterface 991 2 0
    //   476: istore 16
    //   478: aload 5
    //   480: astore 4
    //   482: aload 6
    //   484: astore_3
    //   485: aload_1
    //   486: astore_3
    //   487: aload 5
    //   489: iconst_3
    //   490: invokeinterface 985 2 0
    //   495: pop2
    //   496: lload 14
    //   498: lstore 12
    //   500: aload 9
    //   502: ifnull +225 -> 727
    //   505: aload 5
    //   507: astore 4
    //   509: aload 6
    //   511: astore_3
    //   512: aload_1
    //   513: astore_3
    //   514: aload 9
    //   516: ldc_w 993
    //   519: invokevirtual 891	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   522: istore_2
    //   523: iload_2
    //   524: ifeq +203 -> 727
    //   527: iconst_0
    //   528: istore 17
    //   530: aload 9
    //   532: getstatic 998	android/system/OsConstants:F_OK	I
    //   535: invokestatic 1004	android/system/Os:access	(Ljava/lang/String;I)Z
    //   538: istore_2
    //   539: goto +19 -> 558
    //   542: astore 4
    //   544: aload_1
    //   545: astore 6
    //   547: aload 4
    //   549: astore_1
    //   550: goto +217 -> 767
    //   553: astore 4
    //   555: iload 17
    //   557: istore_2
    //   558: iload_2
    //   559: ifne +168 -> 727
    //   562: aload 5
    //   564: astore 4
    //   566: aload 6
    //   568: astore_3
    //   569: aload_1
    //   570: astore_3
    //   571: iload 16
    //   573: invokestatic 1010	android/mtp/MtpConstants:isAbstractObject	(I)Z
    //   576: ifne +151 -> 727
    //   579: aload 5
    //   581: astore 4
    //   583: aload 6
    //   585: astore_3
    //   586: aload_1
    //   587: astore_3
    //   588: aload 9
    //   590: invokestatic 1016	android/media/MediaFile:getFileType	(Ljava/lang/String;)Landroid/media/MediaFile$MediaFileType;
    //   593: astore 10
    //   595: aload 10
    //   597: ifnonnull +9 -> 606
    //   600: iconst_0
    //   601: istore 16
    //   603: goto +19 -> 622
    //   606: aload 5
    //   608: astore 4
    //   610: aload 6
    //   612: astore_3
    //   613: aload_1
    //   614: astore_3
    //   615: aload 10
    //   617: getfield 1021	android/media/MediaFile$MediaFileType:fileType	I
    //   620: istore 16
    //   622: aload 5
    //   624: astore 4
    //   626: aload 6
    //   628: astore_3
    //   629: aload_1
    //   630: astore_3
    //   631: iload 16
    //   633: invokestatic 1024	android/media/MediaFile:isPlayListFileType	(I)Z
    //   636: istore_2
    //   637: iload_2
    //   638: ifne +89 -> 727
    //   641: aload_1
    //   642: astore 4
    //   644: aload 4
    //   646: lload 14
    //   648: invokevirtual 1028	android/media/MediaScanner$MediaBulkDeleter:delete	(J)V
    //   651: aload 9
    //   653: getstatic 1031	java/util/Locale:US	Ljava/util/Locale;
    //   656: invokevirtual 1035	java/lang/String:toLowerCase	(Ljava/util/Locale;)Ljava/lang/String;
    //   659: ldc_w 1037
    //   662: invokevirtual 1040	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   665: ifeq +51 -> 716
    //   668: aload 4
    //   670: invokevirtual 1043	android/media/MediaScanner$MediaBulkDeleter:flush	()V
    //   673: new 834	java/io/File
    //   676: astore_3
    //   677: aload_3
    //   678: aload 9
    //   680: invokespecial 836	java/io/File:<init>	(Ljava/lang/String;)V
    //   683: aload_3
    //   684: invokevirtual 1046	java/io/File:getParent	()Ljava/lang/String;
    //   687: astore 9
    //   689: aload_0
    //   690: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   693: astore_3
    //   694: aload_3
    //   695: ldc_w 1048
    //   698: aload 9
    //   700: aconst_null
    //   701: invokevirtual 1052	android/content/ContentProviderClient:call	(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;
    //   704: pop
    //   705: goto +22 -> 727
    //   708: astore_1
    //   709: aload 4
    //   711: astore 6
    //   713: goto +54 -> 767
    //   716: goto +11 -> 727
    //   719: astore_1
    //   720: aload 4
    //   722: astore 6
    //   724: goto +43 -> 767
    //   727: goto -325 -> 402
    //   730: goto -552 -> 178
    //   733: astore_1
    //   734: aload_3
    //   735: astore 6
    //   737: aload 4
    //   739: astore 5
    //   741: goto +26 -> 767
    //   744: astore 5
    //   746: aload_1
    //   747: astore 6
    //   749: aload 5
    //   751: astore_1
    //   752: aload_3
    //   753: astore 5
    //   755: goto +12 -> 767
    //   758: astore_1
    //   759: aload 9
    //   761: astore 6
    //   763: aload 4
    //   765: astore 5
    //   767: aload 5
    //   769: ifnull +10 -> 779
    //   772: aload 5
    //   774: invokeinterface 969 1 0
    //   779: aload 6
    //   781: invokevirtual 1043	android/media/MediaScanner$MediaBulkDeleter:flush	()V
    //   784: aload_1
    //   785: athrow
    //   786: aload 4
    //   788: astore 5
    //   790: aload 5
    //   792: ifnull +10 -> 802
    //   795: aload 5
    //   797: invokeinterface 969 1 0
    //   802: aload_1
    //   803: invokevirtual 1043	android/media/MediaScanner$MediaBulkDeleter:flush	()V
    //   806: aload_0
    //   807: iconst_0
    //   808: putfield 1054	android/media/MediaScanner:mOriginalCount	I
    //   811: aload_0
    //   812: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   815: aload_0
    //   816: getfield 574	android/media/MediaScanner:mImagesUri	Landroid/net/Uri;
    //   819: getstatic 159	android/media/MediaScanner:ID_PROJECTION	[Ljava/lang/String;
    //   822: aconst_null
    //   823: aconst_null
    //   824: aconst_null
    //   825: aconst_null
    //   826: invokevirtual 975	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   829: astore_1
    //   830: aload_1
    //   831: ifnull +19 -> 850
    //   834: aload_0
    //   835: aload_1
    //   836: invokeinterface 978 1 0
    //   841: putfield 1054	android/media/MediaScanner:mOriginalCount	I
    //   844: aload_1
    //   845: invokeinterface 969 1 0
    //   850: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	851	0	this	MediaScanner
    //   0	851	1	paramString	String
    //   0	851	2	paramBoolean	boolean
    //   1	752	3	localObject1	Object
    //   3	261	4	localObject2	Object
    //   304	6	4	localObject3	Object
    //   316	192	4	localObject4	Object
    //   542	6	4	localObject5	Object
    //   553	1	4	localErrnoException	android.system.ErrnoException
    //   564	223	4	localObject6	Object
    //   6	734	5	localObject7	Object
    //   744	6	5	localObject8	Object
    //   753	43	5	localObject9	Object
    //   22	758	6	localObject10	Object
    //   38	325	7	arrayOfString	String[]
    //   103	243	8	localBuilder	Uri.Builder
    //   143	617	9	localObject11	Object
    //   150	466	10	localObject12	Object
    //   171	186	11	localUri	Uri
    //   176	323	12	l1	long
    //   438	209	14	l2	long
    //   476	156	16	i	int
    //   528	28	17	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   292	299	304	finally
    //   530	539	542	finally
    //   530	539	553	android/system/ErrnoException
    //   694	705	708	finally
    //   644	694	719	finally
    //   389	399	733	finally
    //   411	421	733	finally
    //   430	440	733	finally
    //   449	459	733	finally
    //   468	478	733	finally
    //   487	496	733	finally
    //   514	523	733	finally
    //   571	579	733	finally
    //   588	595	733	finally
    //   615	622	733	finally
    //   631	637	733	finally
    //   354	372	744	finally
    //   152	173	758	finally
    //   193	197	758	finally
    //   212	216	758	finally
    //   231	239	758	finally
    //   254	261	758	finally
    //   276	284	758	finally
    //   329	335	758	finally
    //   349	354	758	finally
  }
  
  private void processCachedPlaylist(Cursor paramCursor, ContentValues paramContentValues, Uri paramUri)
  {
    paramCursor.moveToPosition(-1);
    int i;
    for (;;)
    {
      boolean bool = paramCursor.moveToNext();
      i = 0;
      if ((!bool) || (matchEntries(paramCursor.getLong(0), paramCursor.getString(1)))) {
        break;
      }
    }
    int j = mPlaylistEntries.size();
    int m;
    for (int k = 0; i < j; k = m)
    {
      paramCursor = (PlaylistEntry)mPlaylistEntries.get(i);
      m = k;
      if (bestmatchlevel > 0) {
        try
        {
          paramContentValues.clear();
          paramContentValues.put("play_order", Integer.valueOf(k));
          paramContentValues.put("audio_id", Long.valueOf(bestmatchid));
          mMediaProvider.insert(paramUri, paramContentValues);
          m = k + 1;
        }
        catch (RemoteException paramCursor)
        {
          Log.e("MediaScanner", "RemoteException in MediaScanner.processCachedPlaylist()", paramCursor);
          return;
        }
      }
      i++;
    }
    mPlaylistEntries.clear();
  }
  
  private native void processDirectory(String paramString, MediaScannerClient paramMediaScannerClient);
  
  private native boolean processFile(String paramString1, String paramString2, MediaScannerClient paramMediaScannerClient);
  
  /* Error */
  private void processM3uPlayList(String paramString1, String paramString2, Uri paramUri, ContentValues paramContentValues, Cursor paramCursor)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: aconst_null
    //   4: astore 7
    //   6: aconst_null
    //   7: astore 8
    //   9: aload 6
    //   11: astore 9
    //   13: aload 7
    //   15: astore 10
    //   17: new 834	java/io/File
    //   20: astore 11
    //   22: aload 6
    //   24: astore 9
    //   26: aload 7
    //   28: astore 10
    //   30: aload 11
    //   32: aload_1
    //   33: invokespecial 836	java/io/File:<init>	(Ljava/lang/String;)V
    //   36: aload 8
    //   38: astore_1
    //   39: aload 6
    //   41: astore 9
    //   43: aload 7
    //   45: astore 10
    //   47: aload 11
    //   49: invokevirtual 880	java/io/File:exists	()Z
    //   52: ifeq +192 -> 244
    //   55: aload 6
    //   57: astore 9
    //   59: aload 7
    //   61: astore 10
    //   63: new 1105	java/io/BufferedReader
    //   66: astore_1
    //   67: aload 6
    //   69: astore 9
    //   71: aload 7
    //   73: astore 10
    //   75: new 1107	java/io/InputStreamReader
    //   78: astore 12
    //   80: aload 6
    //   82: astore 9
    //   84: aload 7
    //   86: astore 10
    //   88: new 1109	java/io/FileInputStream
    //   91: astore 8
    //   93: aload 6
    //   95: astore 9
    //   97: aload 7
    //   99: astore 10
    //   101: aload 8
    //   103: aload 11
    //   105: invokespecial 1112	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   108: aload 6
    //   110: astore 9
    //   112: aload 7
    //   114: astore 10
    //   116: aload 12
    //   118: aload 8
    //   120: invokespecial 1115	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   123: aload 6
    //   125: astore 9
    //   127: aload 7
    //   129: astore 10
    //   131: aload_1
    //   132: aload 12
    //   134: sipush 8192
    //   137: invokespecial 1118	java/io/BufferedReader:<init>	(Ljava/io/Reader;I)V
    //   140: aload_1
    //   141: astore 9
    //   143: aload_1
    //   144: astore 10
    //   146: aload_1
    //   147: invokevirtual 1121	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   150: astore 6
    //   152: aload_1
    //   153: astore 9
    //   155: aload_1
    //   156: astore 10
    //   158: aload_0
    //   159: getfield 496	android/media/MediaScanner:mPlaylistEntries	Ljava/util/ArrayList;
    //   162: invokevirtual 934	java/util/ArrayList:clear	()V
    //   165: aload 6
    //   167: ifnull +62 -> 229
    //   170: aload_1
    //   171: astore 9
    //   173: aload_1
    //   174: astore 10
    //   176: aload 6
    //   178: invokevirtual 796	java/lang/String:length	()I
    //   181: ifle +33 -> 214
    //   184: aload_1
    //   185: astore 9
    //   187: aload_1
    //   188: astore 10
    //   190: aload 6
    //   192: iconst_0
    //   193: invokevirtual 800	java/lang/String:charAt	(I)C
    //   196: bipush 35
    //   198: if_icmpeq +16 -> 214
    //   201: aload_1
    //   202: astore 9
    //   204: aload_1
    //   205: astore 10
    //   207: aload_0
    //   208: aload 6
    //   210: aload_2
    //   211: invokespecial 767	android/media/MediaScanner:cachePlaylistEntry	(Ljava/lang/String;Ljava/lang/String;)V
    //   214: aload_1
    //   215: astore 9
    //   217: aload_1
    //   218: astore 10
    //   220: aload_1
    //   221: invokevirtual 1121	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   224: astore 6
    //   226: goto -61 -> 165
    //   229: aload_1
    //   230: astore 9
    //   232: aload_1
    //   233: astore 10
    //   235: aload_0
    //   236: aload 5
    //   238: aload 4
    //   240: aload_3
    //   241: invokespecial 1123	android/media/MediaScanner:processCachedPlaylist	(Landroid/database/Cursor;Landroid/content/ContentValues;Landroid/net/Uri;)V
    //   244: aload_1
    //   245: ifnull +24 -> 269
    //   248: aload_1
    //   249: invokevirtual 1124	java/io/BufferedReader:close	()V
    //   252: goto +17 -> 269
    //   255: astore_1
    //   256: ldc 76
    //   258: ldc_w 1126
    //   261: aload_1
    //   262: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   265: pop
    //   266: goto +38 -> 304
    //   269: goto +35 -> 304
    //   272: astore_1
    //   273: goto +32 -> 305
    //   276: astore_1
    //   277: aload 10
    //   279: astore 9
    //   281: ldc 76
    //   283: ldc_w 1126
    //   286: aload_1
    //   287: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   290: pop
    //   291: aload 10
    //   293: ifnull -24 -> 269
    //   296: aload 10
    //   298: invokevirtual 1124	java/io/BufferedReader:close	()V
    //   301: goto -32 -> 269
    //   304: return
    //   305: aload 9
    //   307: ifnull +22 -> 329
    //   310: aload 9
    //   312: invokevirtual 1124	java/io/BufferedReader:close	()V
    //   315: goto +14 -> 329
    //   318: astore_2
    //   319: ldc 76
    //   321: ldc_w 1126
    //   324: aload_2
    //   325: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   328: pop
    //   329: aload_1
    //   330: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	331	0	this	MediaScanner
    //   0	331	1	paramString1	String
    //   0	331	2	paramString2	String
    //   0	331	3	paramUri	Uri
    //   0	331	4	paramContentValues	ContentValues
    //   0	331	5	paramCursor	Cursor
    //   1	224	6	str	String
    //   4	124	7	localObject1	Object
    //   7	112	8	localFileInputStream	java.io.FileInputStream
    //   11	300	9	localObject2	Object
    //   15	282	10	localObject3	Object
    //   20	84	11	localFile	File
    //   78	55	12	localInputStreamReader	java.io.InputStreamReader
    // Exception table:
    //   from	to	target	type
    //   248	252	255	java/io/IOException
    //   296	301	255	java/io/IOException
    //   17	22	272	finally
    //   30	36	272	finally
    //   47	55	272	finally
    //   63	67	272	finally
    //   75	80	272	finally
    //   88	93	272	finally
    //   101	108	272	finally
    //   116	123	272	finally
    //   131	140	272	finally
    //   146	152	272	finally
    //   158	165	272	finally
    //   176	184	272	finally
    //   190	201	272	finally
    //   207	214	272	finally
    //   220	226	272	finally
    //   235	244	272	finally
    //   281	291	272	finally
    //   17	22	276	java/io/IOException
    //   30	36	276	java/io/IOException
    //   47	55	276	java/io/IOException
    //   63	67	276	java/io/IOException
    //   75	80	276	java/io/IOException
    //   88	93	276	java/io/IOException
    //   101	108	276	java/io/IOException
    //   116	123	276	java/io/IOException
    //   131	140	276	java/io/IOException
    //   146	152	276	java/io/IOException
    //   158	165	276	java/io/IOException
    //   176	184	276	java/io/IOException
    //   190	201	276	java/io/IOException
    //   207	214	276	java/io/IOException
    //   220	226	276	java/io/IOException
    //   235	244	276	java/io/IOException
    //   310	315	318	java/io/IOException
  }
  
  private void processPlayList(FileEntry paramFileEntry, Cursor paramCursor)
    throws RemoteException
  {
    String str = mPath;
    ContentValues localContentValues = new ContentValues();
    int i = str.lastIndexOf('/');
    if (i >= 0)
    {
      long l = mRowId;
      Object localObject1 = localContentValues.getAsString("name");
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject1 = localContentValues.getAsString("title");
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          j = str.lastIndexOf('.');
          if (j < 0) {
            localObject2 = str.substring(i + 1);
          } else {
            localObject2 = str.substring(i + 1, j);
          }
        }
      }
      localContentValues.put("name", (String)localObject2);
      localContentValues.put("date_modified", Long.valueOf(mLastModified));
      if (l == 0L)
      {
        localContentValues.put("_data", str);
        localObject2 = mMediaProvider.insert(mPlaylistsUri, localContentValues);
        l = ContentUris.parseId((Uri)localObject2);
        paramFileEntry = Uri.withAppendedPath((Uri)localObject2, "members");
      }
      for (;;)
      {
        break;
        localObject2 = ContentUris.withAppendedId(mPlaylistsUri, l);
        mMediaProvider.update((Uri)localObject2, localContentValues, null, null);
        paramFileEntry = Uri.withAppendedPath((Uri)localObject2, "members");
        mMediaProvider.delete(paramFileEntry, null, null);
      }
      int j = 0;
      localObject2 = str.substring(0, i + 1);
      localObject1 = MediaFile.getFileType(str);
      if (localObject1 != null) {
        j = fileType;
      }
      if (j == 41) {
        processM3uPlayList(str, (String)localObject2, paramFileEntry, localContentValues, paramCursor);
      } else if (j == 42) {
        processPlsPlayList(str, (String)localObject2, paramFileEntry, localContentValues, paramCursor);
      } else if (j == 43) {
        processWplPlayList(str, (String)localObject2, paramFileEntry, localContentValues, paramCursor);
      }
      return;
    }
    paramFileEntry = new StringBuilder();
    paramFileEntry.append("bad path ");
    paramFileEntry.append(str);
    throw new IllegalArgumentException(paramFileEntry.toString());
  }
  
  /* Error */
  private void processPlayLists()
    throws RemoteException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 498	android/media/MediaScanner:mPlayLists	Ljava/util/ArrayList;
    //   4: invokevirtual 1189	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: aconst_null
    //   11: astore_3
    //   12: aload_0
    //   13: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   16: aload_0
    //   17: getfield 579	android/media/MediaScanner:mFilesUri	Landroid/net/Uri;
    //   20: getstatic 157	android/media/MediaScanner:FILES_PRESCAN_PROJECTION	[Ljava/lang/String;
    //   23: ldc_w 1191
    //   26: aconst_null
    //   27: aconst_null
    //   28: aconst_null
    //   29: invokevirtual 975	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   32: astore 4
    //   34: aload 4
    //   36: astore_3
    //   37: aload 4
    //   39: astore_2
    //   40: aload_1
    //   41: invokeinterface 1196 1 0
    //   46: ifeq +51 -> 97
    //   49: aload 4
    //   51: astore_3
    //   52: aload 4
    //   54: astore_2
    //   55: aload_1
    //   56: invokeinterface 1200 1 0
    //   61: checkcast 10	android/media/MediaScanner$FileEntry
    //   64: astore 5
    //   66: aload 4
    //   68: astore_3
    //   69: aload 4
    //   71: astore_2
    //   72: aload 5
    //   74: getfield 1203	android/media/MediaScanner$FileEntry:mLastModifiedChanged	Z
    //   77: ifeq +17 -> 94
    //   80: aload 4
    //   82: astore_3
    //   83: aload 4
    //   85: astore_2
    //   86: aload_0
    //   87: aload 5
    //   89: aload 4
    //   91: invokespecial 1205	android/media/MediaScanner:processPlayList	(Landroid/media/MediaScanner$FileEntry;Landroid/database/Cursor;)V
    //   94: goto -60 -> 34
    //   97: aload 4
    //   99: ifnull +37 -> 136
    //   102: aload 4
    //   104: astore_2
    //   105: aload_2
    //   106: invokeinterface 969 1 0
    //   111: goto +25 -> 136
    //   114: astore_2
    //   115: aload_3
    //   116: ifnull +9 -> 125
    //   119: aload_3
    //   120: invokeinterface 969 1 0
    //   125: aload_2
    //   126: athrow
    //   127: astore 4
    //   129: aload_2
    //   130: ifnull +6 -> 136
    //   133: goto -28 -> 105
    //   136: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	this	MediaScanner
    //   7	49	1	localIterator	java.util.Iterator
    //   9	97	2	localObject1	Object
    //   114	16	2	localObject2	Object
    //   11	109	3	localObject3	Object
    //   32	71	4	localCursor	Cursor
    //   127	1	4	localRemoteException	RemoteException
    //   64	24	5	localFileEntry	FileEntry
    // Exception table:
    //   from	to	target	type
    //   12	34	114	finally
    //   40	49	114	finally
    //   55	66	114	finally
    //   72	80	114	finally
    //   86	94	114	finally
    //   12	34	127	android/os/RemoteException
    //   40	49	127	android/os/RemoteException
    //   55	66	127	android/os/RemoteException
    //   72	80	127	android/os/RemoteException
    //   86	94	127	android/os/RemoteException
  }
  
  /* Error */
  private void processPlsPlayList(String paramString1, String paramString2, Uri paramUri, ContentValues paramContentValues, Cursor paramCursor)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: aconst_null
    //   4: astore 7
    //   6: aconst_null
    //   7: astore 8
    //   9: aload 6
    //   11: astore 9
    //   13: aload 7
    //   15: astore 10
    //   17: new 834	java/io/File
    //   20: astore 11
    //   22: aload 6
    //   24: astore 9
    //   26: aload 7
    //   28: astore 10
    //   30: aload 11
    //   32: aload_1
    //   33: invokespecial 836	java/io/File:<init>	(Ljava/lang/String;)V
    //   36: aload 8
    //   38: astore_1
    //   39: aload 6
    //   41: astore 9
    //   43: aload 7
    //   45: astore 10
    //   47: aload 11
    //   49: invokevirtual 880	java/io/File:exists	()Z
    //   52: ifeq +205 -> 257
    //   55: aload 6
    //   57: astore 9
    //   59: aload 7
    //   61: astore 10
    //   63: new 1105	java/io/BufferedReader
    //   66: astore_1
    //   67: aload 6
    //   69: astore 9
    //   71: aload 7
    //   73: astore 10
    //   75: new 1107	java/io/InputStreamReader
    //   78: astore 12
    //   80: aload 6
    //   82: astore 9
    //   84: aload 7
    //   86: astore 10
    //   88: new 1109	java/io/FileInputStream
    //   91: astore 8
    //   93: aload 6
    //   95: astore 9
    //   97: aload 7
    //   99: astore 10
    //   101: aload 8
    //   103: aload 11
    //   105: invokespecial 1112	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   108: aload 6
    //   110: astore 9
    //   112: aload 7
    //   114: astore 10
    //   116: aload 12
    //   118: aload 8
    //   120: invokespecial 1115	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   123: aload 6
    //   125: astore 9
    //   127: aload 7
    //   129: astore 10
    //   131: aload_1
    //   132: aload 12
    //   134: sipush 8192
    //   137: invokespecial 1118	java/io/BufferedReader:<init>	(Ljava/io/Reader;I)V
    //   140: aload_1
    //   141: astore 9
    //   143: aload_1
    //   144: astore 10
    //   146: aload_1
    //   147: invokevirtual 1121	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   150: astore 6
    //   152: aload_1
    //   153: astore 9
    //   155: aload_1
    //   156: astore 10
    //   158: aload_0
    //   159: getfield 496	android/media/MediaScanner:mPlaylistEntries	Ljava/util/ArrayList;
    //   162: invokevirtual 934	java/util/ArrayList:clear	()V
    //   165: aload 6
    //   167: ifnull +75 -> 242
    //   170: aload_1
    //   171: astore 9
    //   173: aload_1
    //   174: astore 10
    //   176: aload 6
    //   178: ldc_w 1207
    //   181: invokevirtual 891	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   184: ifeq +43 -> 227
    //   187: aload_1
    //   188: astore 9
    //   190: aload_1
    //   191: astore 10
    //   193: aload 6
    //   195: bipush 61
    //   197: invokevirtual 1209	java/lang/String:indexOf	(I)I
    //   200: istore 13
    //   202: iload 13
    //   204: ifle +23 -> 227
    //   207: aload_1
    //   208: astore 9
    //   210: aload_1
    //   211: astore 10
    //   213: aload_0
    //   214: aload 6
    //   216: iload 13
    //   218: iconst_1
    //   219: iadd
    //   220: invokevirtual 1144	java/lang/String:substring	(I)Ljava/lang/String;
    //   223: aload_2
    //   224: invokespecial 767	android/media/MediaScanner:cachePlaylistEntry	(Ljava/lang/String;Ljava/lang/String;)V
    //   227: aload_1
    //   228: astore 9
    //   230: aload_1
    //   231: astore 10
    //   233: aload_1
    //   234: invokevirtual 1121	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   237: astore 6
    //   239: goto -74 -> 165
    //   242: aload_1
    //   243: astore 9
    //   245: aload_1
    //   246: astore 10
    //   248: aload_0
    //   249: aload 5
    //   251: aload 4
    //   253: aload_3
    //   254: invokespecial 1123	android/media/MediaScanner:processCachedPlaylist	(Landroid/database/Cursor;Landroid/content/ContentValues;Landroid/net/Uri;)V
    //   257: aload_1
    //   258: ifnull +24 -> 282
    //   261: aload_1
    //   262: invokevirtual 1124	java/io/BufferedReader:close	()V
    //   265: goto +17 -> 282
    //   268: astore_1
    //   269: ldc 76
    //   271: ldc_w 1211
    //   274: aload_1
    //   275: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   278: pop
    //   279: goto +38 -> 317
    //   282: goto +35 -> 317
    //   285: astore_1
    //   286: goto +32 -> 318
    //   289: astore_1
    //   290: aload 10
    //   292: astore 9
    //   294: ldc 76
    //   296: ldc_w 1211
    //   299: aload_1
    //   300: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   303: pop
    //   304: aload 10
    //   306: ifnull -24 -> 282
    //   309: aload 10
    //   311: invokevirtual 1124	java/io/BufferedReader:close	()V
    //   314: goto -32 -> 282
    //   317: return
    //   318: aload 9
    //   320: ifnull +22 -> 342
    //   323: aload 9
    //   325: invokevirtual 1124	java/io/BufferedReader:close	()V
    //   328: goto +14 -> 342
    //   331: astore_2
    //   332: ldc 76
    //   334: ldc_w 1211
    //   337: aload_2
    //   338: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   341: pop
    //   342: aload_1
    //   343: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	344	0	this	MediaScanner
    //   0	344	1	paramString1	String
    //   0	344	2	paramString2	String
    //   0	344	3	paramUri	Uri
    //   0	344	4	paramContentValues	ContentValues
    //   0	344	5	paramCursor	Cursor
    //   1	237	6	str	String
    //   4	124	7	localObject1	Object
    //   7	112	8	localFileInputStream	java.io.FileInputStream
    //   11	313	9	localObject2	Object
    //   15	295	10	localObject3	Object
    //   20	84	11	localFile	File
    //   78	55	12	localInputStreamReader	java.io.InputStreamReader
    //   200	20	13	i	int
    // Exception table:
    //   from	to	target	type
    //   261	265	268	java/io/IOException
    //   309	314	268	java/io/IOException
    //   17	22	285	finally
    //   30	36	285	finally
    //   47	55	285	finally
    //   63	67	285	finally
    //   75	80	285	finally
    //   88	93	285	finally
    //   101	108	285	finally
    //   116	123	285	finally
    //   131	140	285	finally
    //   146	152	285	finally
    //   158	165	285	finally
    //   176	187	285	finally
    //   193	202	285	finally
    //   213	227	285	finally
    //   233	239	285	finally
    //   248	257	285	finally
    //   294	304	285	finally
    //   17	22	289	java/io/IOException
    //   30	36	289	java/io/IOException
    //   47	55	289	java/io/IOException
    //   63	67	289	java/io/IOException
    //   75	80	289	java/io/IOException
    //   88	93	289	java/io/IOException
    //   101	108	289	java/io/IOException
    //   116	123	289	java/io/IOException
    //   131	140	289	java/io/IOException
    //   146	152	289	java/io/IOException
    //   158	165	289	java/io/IOException
    //   176	187	289	java/io/IOException
    //   193	202	289	java/io/IOException
    //   213	227	289	java/io/IOException
    //   233	239	289	java/io/IOException
    //   248	257	289	java/io/IOException
    //   323	328	331	java/io/IOException
  }
  
  /* Error */
  private void processWplPlayList(String paramString1, String paramString2, Uri paramUri, ContentValues paramContentValues, Cursor paramCursor)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: aconst_null
    //   4: astore 7
    //   6: aconst_null
    //   7: astore 8
    //   9: aconst_null
    //   10: astore 9
    //   12: aload 6
    //   14: astore 10
    //   16: aload 7
    //   18: astore 11
    //   20: aload 8
    //   22: astore 12
    //   24: new 834	java/io/File
    //   27: astore 13
    //   29: aload 6
    //   31: astore 10
    //   33: aload 7
    //   35: astore 11
    //   37: aload 8
    //   39: astore 12
    //   41: aload 13
    //   43: aload_1
    //   44: invokespecial 836	java/io/File:<init>	(Ljava/lang/String;)V
    //   47: aload 9
    //   49: astore_1
    //   50: aload 6
    //   52: astore 10
    //   54: aload 7
    //   56: astore 11
    //   58: aload 8
    //   60: astore 12
    //   62: aload 13
    //   64: invokevirtual 880	java/io/File:exists	()Z
    //   67: ifeq +141 -> 208
    //   70: aload 6
    //   72: astore 10
    //   74: aload 7
    //   76: astore 11
    //   78: aload 8
    //   80: astore 12
    //   82: new 1109	java/io/FileInputStream
    //   85: astore_1
    //   86: aload 6
    //   88: astore 10
    //   90: aload 7
    //   92: astore 11
    //   94: aload 8
    //   96: astore 12
    //   98: aload_1
    //   99: aload 13
    //   101: invokespecial 1112	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   104: aload_1
    //   105: astore 10
    //   107: aload_1
    //   108: astore 11
    //   110: aload_1
    //   111: astore 12
    //   113: aload_0
    //   114: getfield 496	android/media/MediaScanner:mPlaylistEntries	Ljava/util/ArrayList;
    //   117: invokevirtual 934	java/util/ArrayList:clear	()V
    //   120: aload_1
    //   121: astore 10
    //   123: aload_1
    //   124: astore 11
    //   126: aload_1
    //   127: astore 12
    //   129: ldc_w 1215
    //   132: invokestatic 1221	android/util/Xml:findEncodingByName	(Ljava/lang/String;)Landroid/util/Xml$Encoding;
    //   135: astore 7
    //   137: aload_1
    //   138: astore 10
    //   140: aload_1
    //   141: astore 11
    //   143: aload_1
    //   144: astore 12
    //   146: new 22	android/media/MediaScanner$WplHandler
    //   149: astore 6
    //   151: aload_1
    //   152: astore 10
    //   154: aload_1
    //   155: astore 11
    //   157: aload_1
    //   158: astore 12
    //   160: aload 6
    //   162: aload_0
    //   163: aload_2
    //   164: aload_3
    //   165: aload 5
    //   167: invokespecial 1224	android/media/MediaScanner$WplHandler:<init>	(Landroid/media/MediaScanner;Ljava/lang/String;Landroid/net/Uri;Landroid/database/Cursor;)V
    //   170: aload_1
    //   171: astore 10
    //   173: aload_1
    //   174: astore 11
    //   176: aload_1
    //   177: astore 12
    //   179: aload_1
    //   180: aload 7
    //   182: aload 6
    //   184: invokevirtual 1228	android/media/MediaScanner$WplHandler:getContentHandler	()Lorg/xml/sax/ContentHandler;
    //   187: invokestatic 1232	android/util/Xml:parse	(Ljava/io/InputStream;Landroid/util/Xml$Encoding;Lorg/xml/sax/ContentHandler;)V
    //   190: aload_1
    //   191: astore 10
    //   193: aload_1
    //   194: astore 11
    //   196: aload_1
    //   197: astore 12
    //   199: aload_0
    //   200: aload 5
    //   202: aload 4
    //   204: aload_3
    //   205: invokespecial 1123	android/media/MediaScanner:processCachedPlaylist	(Landroid/database/Cursor;Landroid/content/ContentValues;Landroid/net/Uri;)V
    //   208: aload_1
    //   209: ifnull +24 -> 233
    //   212: aload_1
    //   213: invokevirtual 1233	java/io/FileInputStream:close	()V
    //   216: goto +17 -> 233
    //   219: astore_1
    //   220: ldc 76
    //   222: ldc_w 1235
    //   225: aload_1
    //   226: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   229: pop
    //   230: goto +54 -> 284
    //   233: goto +51 -> 284
    //   236: astore_1
    //   237: goto +48 -> 285
    //   240: astore_1
    //   241: aload 11
    //   243: astore 10
    //   245: aload_1
    //   246: invokevirtual 1238	java/io/IOException:printStackTrace	()V
    //   249: aload 11
    //   251: ifnull -18 -> 233
    //   254: aload 11
    //   256: invokevirtual 1233	java/io/FileInputStream:close	()V
    //   259: goto -26 -> 233
    //   262: astore_1
    //   263: aload 12
    //   265: astore 10
    //   267: aload_1
    //   268: invokevirtual 1239	org/xml/sax/SAXException:printStackTrace	()V
    //   271: aload 12
    //   273: ifnull -40 -> 233
    //   276: aload 12
    //   278: invokevirtual 1233	java/io/FileInputStream:close	()V
    //   281: goto -48 -> 233
    //   284: return
    //   285: aload 10
    //   287: ifnull +22 -> 309
    //   290: aload 10
    //   292: invokevirtual 1233	java/io/FileInputStream:close	()V
    //   295: goto +14 -> 309
    //   298: astore_2
    //   299: ldc 76
    //   301: ldc_w 1235
    //   304: aload_2
    //   305: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   308: pop
    //   309: aload_1
    //   310: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	311	0	this	MediaScanner
    //   0	311	1	paramString1	String
    //   0	311	2	paramString2	String
    //   0	311	3	paramUri	Uri
    //   0	311	4	paramContentValues	ContentValues
    //   0	311	5	paramCursor	Cursor
    //   1	182	6	localWplHandler	WplHandler
    //   4	177	7	localEncoding	android.util.Xml.Encoding
    //   7	88	8	localObject1	Object
    //   10	38	9	localObject2	Object
    //   14	277	10	localObject3	Object
    //   18	237	11	localObject4	Object
    //   22	255	12	localObject5	Object
    //   27	73	13	localFile	File
    // Exception table:
    //   from	to	target	type
    //   212	216	219	java/io/IOException
    //   254	259	219	java/io/IOException
    //   276	281	219	java/io/IOException
    //   24	29	236	finally
    //   41	47	236	finally
    //   62	70	236	finally
    //   82	86	236	finally
    //   98	104	236	finally
    //   113	120	236	finally
    //   129	137	236	finally
    //   146	151	236	finally
    //   160	170	236	finally
    //   179	190	236	finally
    //   199	208	236	finally
    //   245	249	236	finally
    //   267	271	236	finally
    //   24	29	240	java/io/IOException
    //   41	47	240	java/io/IOException
    //   62	70	240	java/io/IOException
    //   82	86	240	java/io/IOException
    //   98	104	240	java/io/IOException
    //   113	120	240	java/io/IOException
    //   129	137	240	java/io/IOException
    //   146	151	240	java/io/IOException
    //   160	170	240	java/io/IOException
    //   179	190	240	java/io/IOException
    //   199	208	240	java/io/IOException
    //   24	29	262	org/xml/sax/SAXException
    //   41	47	262	org/xml/sax/SAXException
    //   62	70	262	org/xml/sax/SAXException
    //   82	86	262	org/xml/sax/SAXException
    //   98	104	262	org/xml/sax/SAXException
    //   113	120	262	org/xml/sax/SAXException
    //   129	137	262	org/xml/sax/SAXException
    //   146	151	262	org/xml/sax/SAXException
    //   160	170	262	org/xml/sax/SAXException
    //   179	190	262	org/xml/sax/SAXException
    //   199	208	262	org/xml/sax/SAXException
    //   290	295	298	java/io/IOException
  }
  
  private void releaseResources()
  {
    if (mDrmManagerClient != null)
    {
      mDrmManagerClient.close();
      mDrmManagerClient = null;
    }
  }
  
  private void setDefaultRingtoneFileNames()
  {
    mDefaultRingtoneFilename = SystemProperties.get("ro.config.ringtone");
    mDefaultNotificationFilename = SystemProperties.get("ro.config.notification_sound");
    mDefaultAlarmAlertFilename = SystemProperties.get("ro.config.alarm_alert");
    mDefaultNewMailFilename = SystemProperties.get("ro.config.newmail_sound");
    mDefaultSentMailFilename = SystemProperties.get("ro.config.sentmail_sound");
    mDefaultCalendarAlertFilename = SystemProperties.get("ro.config.calendaralert_sound");
    mDefaultRingtone2Filename = SystemProperties.get("ro.config.ringtone");
    mDefaultNotification2Filename = SystemProperties.get("ro.config.notification_sound");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultRingtoneFilename=");
    localStringBuilder.append(mDefaultRingtoneFilename);
    Log.d("MediaScanner", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultNotificationFilename=");
    localStringBuilder.append(mDefaultNotificationFilename);
    Log.d("MediaScanner", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultAlarmAlertFilename=");
    localStringBuilder.append(mDefaultAlarmAlertFilename);
    Log.d("MediaScanner", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultNewMailFilename=");
    localStringBuilder.append(mDefaultNewMailFilename);
    Log.d("MediaScanner", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultSentMailFilename=");
    localStringBuilder.append(mDefaultSentMailFilename);
    Log.d("MediaScanner", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultCalendarAlertFilename=");
    localStringBuilder.append(mDefaultCalendarAlertFilename);
    Log.d("MediaScanner", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultRingtone2Filename=");
    localStringBuilder.append(mDefaultRingtone2Filename);
    Log.d("MediaScanner", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDefaultRingtoneFileNames:mDefaultNotification2Filename=");
    localStringBuilder.append(mDefaultNotification2Filename);
    Log.d("MediaScanner", localStringBuilder.toString());
  }
  
  private native void setLocale(String paramString);
  
  private String settingSetIndicatorName(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("_set");
    return localStringBuilder.toString();
  }
  
  private boolean wasRingtoneAlreadySet(String paramString)
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    paramString = settingSetIndicatorName(paramString);
    boolean bool = false;
    try
    {
      int i = Settings.System.getInt(localContentResolver, paramString);
      if (i != 0) {
        bool = true;
      }
      return bool;
    }
    catch (Settings.SettingNotFoundException paramString) {}
    return false;
  }
  
  public void close()
  {
    mCloseGuard.close();
    if (mClosed.compareAndSet(false, true))
    {
      mMediaProvider.close();
      native_finalize();
    }
  }
  
  public native byte[] extractAlbumArt(FileDescriptor paramFileDescriptor);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  /* Error */
  FileEntry makeEntryFor(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_0
    //   5: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   8: aload_0
    //   9: getfield 601	android/media/MediaScanner:mFilesUriNoNotify	Landroid/net/Uri;
    //   12: getstatic 157	android/media/MediaScanner:FILES_PRESCAN_PROJECTION	[Ljava/lang/String;
    //   15: ldc_w 1306
    //   18: iconst_1
    //   19: anewarray 147	java/lang/String
    //   22: dup
    //   23: iconst_0
    //   24: aload_1
    //   25: aastore
    //   26: aconst_null
    //   27: aconst_null
    //   28: invokevirtual 975	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   31: astore 4
    //   33: aload 4
    //   35: astore_3
    //   36: aload 4
    //   38: astore_2
    //   39: aload 4
    //   41: invokeinterface 1309 1 0
    //   46: ifeq +76 -> 122
    //   49: aload 4
    //   51: astore_3
    //   52: aload 4
    //   54: astore_2
    //   55: aload 4
    //   57: iconst_0
    //   58: invokeinterface 985 2 0
    //   63: lstore 5
    //   65: aload 4
    //   67: astore_3
    //   68: aload 4
    //   70: astore_2
    //   71: aload 4
    //   73: iconst_2
    //   74: invokeinterface 991 2 0
    //   79: istore 7
    //   81: aload 4
    //   83: astore_3
    //   84: aload 4
    //   86: astore_2
    //   87: new 10	android/media/MediaScanner$FileEntry
    //   90: dup
    //   91: lload 5
    //   93: aload_1
    //   94: aload 4
    //   96: iconst_3
    //   97: invokeinterface 985 2 0
    //   102: iload 7
    //   104: invokespecial 1312	android/media/MediaScanner$FileEntry:<init>	(JLjava/lang/String;JI)V
    //   107: astore_1
    //   108: aload 4
    //   110: ifnull +10 -> 120
    //   113: aload 4
    //   115: invokeinterface 969 1 0
    //   120: aload_1
    //   121: areturn
    //   122: aload 4
    //   124: ifnull +36 -> 160
    //   127: aload 4
    //   129: astore_2
    //   130: aload_2
    //   131: invokeinterface 969 1 0
    //   136: goto +24 -> 160
    //   139: astore_1
    //   140: aload_3
    //   141: ifnull +9 -> 150
    //   144: aload_3
    //   145: invokeinterface 969 1 0
    //   150: aload_1
    //   151: athrow
    //   152: astore_1
    //   153: aload_2
    //   154: ifnull +6 -> 160
    //   157: goto -27 -> 130
    //   160: aconst_null
    //   161: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	162	0	this	MediaScanner
    //   0	162	1	paramString	String
    //   1	153	2	localObject1	Object
    //   3	142	3	localObject2	Object
    //   31	97	4	localCursor	Cursor
    //   63	29	5	l	long
    //   79	24	7	i	int
    // Exception table:
    //   from	to	target	type
    //   4	33	139	finally
    //   39	49	139	finally
    //   55	65	139	finally
    //   71	81	139	finally
    //   87	108	139	finally
    //   4	33	152	android/os/RemoteException
    //   39	49	152	android/os/RemoteException
    //   55	65	152	android/os/RemoteException
    //   71	81	152	android/os/RemoteException
    //   87	108	152	android/os/RemoteException
  }
  
  /* Error */
  public void scanDirectories(String[] paramArrayOfString)
  {
    // Byte code:
    //   0: invokestatic 1321	java/lang/System:currentTimeMillis	()J
    //   3: lstore_2
    //   4: aload_0
    //   5: aconst_null
    //   6: iconst_1
    //   7: invokespecial 1323	android/media/MediaScanner:prescan	(Ljava/lang/String;Z)V
    //   10: invokestatic 1321	java/lang/System:currentTimeMillis	()J
    //   13: lstore 4
    //   15: new 1325	android/media/MediaInserter
    //   18: astore 6
    //   20: aload 6
    //   22: aload_0
    //   23: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   26: sipush 500
    //   29: invokespecial 1328	android/media/MediaInserter:<init>	(Landroid/content/ContentProviderClient;I)V
    //   32: aload_0
    //   33: aload 6
    //   35: putfield 708	android/media/MediaScanner:mMediaInserter	Landroid/media/MediaInserter;
    //   38: iconst_0
    //   39: istore 7
    //   41: iload 7
    //   43: aload_1
    //   44: arraylength
    //   45: if_icmpge +21 -> 66
    //   48: aload_0
    //   49: aload_1
    //   50: iload 7
    //   52: aaload
    //   53: aload_0
    //   54: getfield 505	android/media/MediaScanner:mClient	Landroid/media/MediaScanner$MyMediaScannerClient;
    //   57: invokespecial 1330	android/media/MediaScanner:processDirectory	(Ljava/lang/String;Landroid/media/MediaScannerClient;)V
    //   60: iinc 7 1
    //   63: goto -22 -> 41
    //   66: aload_0
    //   67: getfield 708	android/media/MediaScanner:mMediaInserter	Landroid/media/MediaInserter;
    //   70: invokevirtual 1333	android/media/MediaInserter:flushAll	()V
    //   73: aload_0
    //   74: aconst_null
    //   75: putfield 708	android/media/MediaScanner:mMediaInserter	Landroid/media/MediaInserter;
    //   78: invokestatic 1321	java/lang/System:currentTimeMillis	()J
    //   81: lstore 8
    //   83: aload_0
    //   84: aload_1
    //   85: invokespecial 1335	android/media/MediaScanner:postscan	([Ljava/lang/String;)V
    //   88: invokestatic 1321	java/lang/System:currentTimeMillis	()J
    //   91: lstore 10
    //   93: new 642	java/lang/StringBuilder
    //   96: astore_1
    //   97: aload_1
    //   98: invokespecial 643	java/lang/StringBuilder:<init>	()V
    //   101: aload_1
    //   102: ldc_w 1337
    //   105: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload_1
    //   110: lload 4
    //   112: lload_2
    //   113: lsub
    //   114: invokevirtual 965	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: aload_1
    //   119: ldc_w 1339
    //   122: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: pop
    //   126: ldc 76
    //   128: aload_1
    //   129: invokevirtual 652	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   132: invokestatic 1260	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   135: pop
    //   136: new 642	java/lang/StringBuilder
    //   139: astore_1
    //   140: aload_1
    //   141: invokespecial 643	java/lang/StringBuilder:<init>	()V
    //   144: aload_1
    //   145: ldc_w 1341
    //   148: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: aload_1
    //   153: lload 8
    //   155: lload 4
    //   157: lsub
    //   158: invokevirtual 965	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: aload_1
    //   163: ldc_w 1339
    //   166: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: pop
    //   170: ldc 76
    //   172: aload_1
    //   173: invokevirtual 652	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: invokestatic 1260	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   179: pop
    //   180: new 642	java/lang/StringBuilder
    //   183: astore_1
    //   184: aload_1
    //   185: invokespecial 643	java/lang/StringBuilder:<init>	()V
    //   188: aload_1
    //   189: ldc_w 1343
    //   192: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: pop
    //   196: aload_1
    //   197: lload 10
    //   199: lload 8
    //   201: lsub
    //   202: invokevirtual 965	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   205: pop
    //   206: aload_1
    //   207: ldc_w 1339
    //   210: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: pop
    //   214: ldc 76
    //   216: aload_1
    //   217: invokevirtual 652	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   220: invokestatic 1260	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   223: pop
    //   224: new 642	java/lang/StringBuilder
    //   227: astore_1
    //   228: aload_1
    //   229: invokespecial 643	java/lang/StringBuilder:<init>	()V
    //   232: aload_1
    //   233: ldc_w 1345
    //   236: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   239: pop
    //   240: aload_1
    //   241: lload 10
    //   243: lload_2
    //   244: lsub
    //   245: invokevirtual 965	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   248: pop
    //   249: aload_1
    //   250: ldc_w 1339
    //   253: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   256: pop
    //   257: ldc 76
    //   259: aload_1
    //   260: invokevirtual 652	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   263: invokestatic 1260	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   266: pop
    //   267: goto +46 -> 313
    //   270: astore_1
    //   271: goto +47 -> 318
    //   274: astore_1
    //   275: ldc 76
    //   277: ldc_w 1347
    //   280: aload_1
    //   281: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   284: pop
    //   285: goto +28 -> 313
    //   288: astore_1
    //   289: ldc 76
    //   291: ldc_w 1349
    //   294: aload_1
    //   295: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   298: pop
    //   299: goto +14 -> 313
    //   302: astore_1
    //   303: ldc 76
    //   305: ldc_w 1351
    //   308: aload_1
    //   309: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   312: pop
    //   313: aload_0
    //   314: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   317: return
    //   318: aload_0
    //   319: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   322: aload_1
    //   323: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	324	0	this	MediaScanner
    //   0	324	1	paramArrayOfString	String[]
    //   3	241	2	l1	long
    //   13	143	4	l2	long
    //   18	16	6	localMediaInserter	MediaInserter
    //   39	22	7	i	int
    //   81	119	8	l3	long
    //   91	151	10	l4	long
    // Exception table:
    //   from	to	target	type
    //   0	38	270	finally
    //   41	60	270	finally
    //   66	267	270	finally
    //   275	285	270	finally
    //   289	299	270	finally
    //   303	313	270	finally
    //   0	38	274	android/os/RemoteException
    //   41	60	274	android/os/RemoteException
    //   66	267	274	android/os/RemoteException
    //   0	38	288	java/lang/UnsupportedOperationException
    //   41	60	288	java/lang/UnsupportedOperationException
    //   66	267	288	java/lang/UnsupportedOperationException
    //   0	38	302	android/database/SQLException
    //   41	60	302	android/database/SQLException
    //   66	267	302	android/database/SQLException
  }
  
  /* Error */
  public void scanMtpFile(String paramString, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 1016	android/media/MediaFile:getFileType	(Ljava/lang/String;)Landroid/media/MediaFile$MediaFileType;
    //   4: astore 4
    //   6: aload 4
    //   8: ifnonnull +9 -> 17
    //   11: iconst_0
    //   12: istore 5
    //   14: goto +10 -> 24
    //   17: aload 4
    //   19: getfield 1021	android/media/MediaFile$MediaFileType:fileType	I
    //   22: istore 5
    //   24: new 834	java/io/File
    //   27: dup
    //   28: aload_1
    //   29: invokespecial 836	java/io/File:<init>	(Ljava/lang/String;)V
    //   32: astore 6
    //   34: aload 6
    //   36: invokevirtual 1358	java/io/File:lastModified	()J
    //   39: ldc2_w 1359
    //   42: ldiv
    //   43: lstore 7
    //   45: iload 5
    //   47: invokestatic 1363	android/media/MediaFile:isAudioFileType	(I)Z
    //   50: ifne +118 -> 168
    //   53: iload 5
    //   55: invokestatic 1366	android/media/MediaFile:isVideoFileType	(I)Z
    //   58: ifne +110 -> 168
    //   61: iload 5
    //   63: invokestatic 1369	android/media/MediaFile:isImageFileType	(I)Z
    //   66: ifne +102 -> 168
    //   69: iload 5
    //   71: invokestatic 1024	android/media/MediaFile:isPlayListFileType	(I)Z
    //   74: ifne +94 -> 168
    //   77: iload 5
    //   79: invokestatic 1372	android/media/MediaFile:isDrmFileType	(I)Z
    //   82: ifne +86 -> 168
    //   85: new 1063	android/content/ContentValues
    //   88: dup
    //   89: invokespecial 1132	android/content/ContentValues:<init>	()V
    //   92: astore_1
    //   93: aload_1
    //   94: ldc_w 1374
    //   97: aload 6
    //   99: invokevirtual 1376	java/io/File:length	()J
    //   102: invokestatic 1082	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   105: invokevirtual 1085	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   108: aload_1
    //   109: ldc -101
    //   111: lload 7
    //   113: invokestatic 1082	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   116: invokevirtual 1085	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   119: iload_2
    //   120: invokestatic 1378	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   123: astore 9
    //   125: aload_0
    //   126: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   129: aload_0
    //   130: getfield 520	android/media/MediaScanner:mVolumeName	Ljava/lang/String;
    //   133: invokestatic 1381	android/provider/MediaStore$Files:getMtpObjectsUri	(Ljava/lang/String;)Landroid/net/Uri;
    //   136: aload_1
    //   137: ldc_w 1383
    //   140: iconst_1
    //   141: anewarray 147	java/lang/String
    //   144: dup
    //   145: iconst_0
    //   146: aload 9
    //   148: aastore
    //   149: invokevirtual 1169	android/content/ContentProviderClient:update	(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   152: pop
    //   153: goto +14 -> 167
    //   156: astore_1
    //   157: ldc 76
    //   159: ldc_w 1385
    //   162: aload_1
    //   163: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   166: pop
    //   167: return
    //   168: aload_0
    //   169: iload_2
    //   170: putfield 771	android/media/MediaScanner:mMtpObjectHandle	I
    //   173: aconst_null
    //   174: astore 10
    //   176: aconst_null
    //   177: astore 9
    //   179: iload 5
    //   181: invokestatic 1024	android/media/MediaFile:isPlayListFileType	(I)Z
    //   184: istore 11
    //   186: iload 11
    //   188: ifeq +95 -> 283
    //   191: aload_0
    //   192: aconst_null
    //   193: iconst_1
    //   194: invokespecial 1323	android/media/MediaScanner:prescan	(Ljava/lang/String;Z)V
    //   197: aload_0
    //   198: aload_1
    //   199: invokevirtual 1387	android/media/MediaScanner:makeEntryFor	(Ljava/lang/String;)Landroid/media/MediaScanner$FileEntry;
    //   202: astore 6
    //   204: aload 9
    //   206: astore_1
    //   207: aload 6
    //   209: ifnull +53 -> 262
    //   212: aload_0
    //   213: getfield 543	android/media/MediaScanner:mMediaProvider	Landroid/content/ContentProviderClient;
    //   216: aload_0
    //   217: getfield 579	android/media/MediaScanner:mFilesUri	Landroid/net/Uri;
    //   220: getstatic 157	android/media/MediaScanner:FILES_PRESCAN_PROJECTION	[Ljava/lang/String;
    //   223: aconst_null
    //   224: aconst_null
    //   225: aconst_null
    //   226: aconst_null
    //   227: invokevirtual 975	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   230: astore 10
    //   232: aload_0
    //   233: aload 6
    //   235: aload 10
    //   237: invokespecial 1205	android/media/MediaScanner:processPlayList	(Landroid/media/MediaScanner$FileEntry;Landroid/database/Cursor;)V
    //   240: aload 10
    //   242: astore_1
    //   243: goto +19 -> 262
    //   246: astore_1
    //   247: aload 10
    //   249: astore 9
    //   251: goto +186 -> 437
    //   254: astore 9
    //   256: aload 10
    //   258: astore_1
    //   259: goto +139 -> 398
    //   262: goto +93 -> 355
    //   265: astore_1
    //   266: goto +171 -> 437
    //   269: astore_1
    //   270: aload 9
    //   272: astore 10
    //   274: aload_1
    //   275: astore 9
    //   277: aload 10
    //   279: astore_1
    //   280: goto +118 -> 398
    //   283: aload_0
    //   284: aload_1
    //   285: iconst_0
    //   286: invokespecial 1323	android/media/MediaScanner:prescan	(Ljava/lang/String;Z)V
    //   289: aload_0
    //   290: getfield 505	android/media/MediaScanner:mClient	Landroid/media/MediaScanner$MyMediaScannerClient;
    //   293: astore 12
    //   295: aload 4
    //   297: getfield 1390	android/media/MediaFile$MediaFileType:mimeType	Ljava/lang/String;
    //   300: astore 4
    //   302: aload 6
    //   304: invokevirtual 1376	java/io/File:length	()J
    //   307: lstore 13
    //   309: iload_3
    //   310: sipush 12289
    //   313: if_icmpne +9 -> 322
    //   316: iconst_1
    //   317: istore 11
    //   319: goto +6 -> 325
    //   322: iconst_0
    //   323: istore 11
    //   325: aload_1
    //   326: invokestatic 1392	android/media/MediaScanner:isNoMediaPath	(Ljava/lang/String;)Z
    //   329: istore 15
    //   331: aload 12
    //   333: aload_1
    //   334: aload 4
    //   336: lload 7
    //   338: lload 13
    //   340: iload 11
    //   342: iconst_1
    //   343: iload 15
    //   345: invokevirtual 1396	android/media/MediaScanner$MyMediaScannerClient:doScanFile	(Ljava/lang/String;Ljava/lang/String;JJZZZ)Landroid/net/Uri;
    //   348: pop
    //   349: aload 10
    //   351: astore_1
    //   352: goto -90 -> 262
    //   355: aload_0
    //   356: iconst_0
    //   357: putfield 771	android/media/MediaScanner:mMtpObjectHandle	I
    //   360: aload_1
    //   361: ifnull +9 -> 370
    //   364: aload_1
    //   365: invokeinterface 969 1 0
    //   370: aload_0
    //   371: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   374: goto +54 -> 428
    //   377: astore_1
    //   378: goto -112 -> 266
    //   381: astore_1
    //   382: goto -112 -> 270
    //   385: astore_1
    //   386: goto +51 -> 437
    //   389: astore 10
    //   391: aload 9
    //   393: astore_1
    //   394: aload 10
    //   396: astore 9
    //   398: ldc 76
    //   400: ldc_w 1398
    //   403: aload 9
    //   405: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   408: pop
    //   409: aload_0
    //   410: iconst_0
    //   411: putfield 771	android/media/MediaScanner:mMtpObjectHandle	I
    //   414: aload_1
    //   415: ifnull +9 -> 424
    //   418: aload_1
    //   419: invokeinterface 969 1 0
    //   424: aload_0
    //   425: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   428: return
    //   429: astore 10
    //   431: aload_1
    //   432: astore 9
    //   434: aload 10
    //   436: astore_1
    //   437: aload_0
    //   438: iconst_0
    //   439: putfield 771	android/media/MediaScanner:mMtpObjectHandle	I
    //   442: aload 9
    //   444: ifnull +10 -> 454
    //   447: aload 9
    //   449: invokeinterface 969 1 0
    //   454: aload_0
    //   455: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   458: aload_1
    //   459: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	460	0	this	MediaScanner
    //   0	460	1	paramString	String
    //   0	460	2	paramInt1	int
    //   0	460	3	paramInt2	int
    //   4	331	4	localObject1	Object
    //   12	168	5	i	int
    //   32	271	6	localObject2	Object
    //   43	294	7	l1	long
    //   123	127	9	localObject3	Object
    //   254	17	9	localRemoteException1	RemoteException
    //   275	173	9	localObject4	Object
    //   174	176	10	localObject5	Object
    //   389	6	10	localRemoteException2	RemoteException
    //   429	6	10	localObject6	Object
    //   184	157	11	bool1	boolean
    //   293	39	12	localMyMediaScannerClient	MyMediaScannerClient
    //   307	32	13	l2	long
    //   329	15	15	bool2	boolean
    // Exception table:
    //   from	to	target	type
    //   119	153	156	android/os/RemoteException
    //   232	240	246	finally
    //   232	240	254	android/os/RemoteException
    //   191	204	265	finally
    //   212	232	265	finally
    //   191	204	269	android/os/RemoteException
    //   212	232	269	android/os/RemoteException
    //   331	349	377	finally
    //   331	349	381	android/os/RemoteException
    //   179	186	385	finally
    //   283	309	385	finally
    //   325	331	385	finally
    //   179	186	389	android/os/RemoteException
    //   283	309	389	android/os/RemoteException
    //   325	331	389	android/os/RemoteException
    //   398	409	429	finally
  }
  
  /* Error */
  public Uri scanSingleFile(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 486	android/media/MediaScanner:mEnableTwinApps	Z
    //   4: ifeq +100 -> 104
    //   7: ldc_w 1402
    //   10: aload_1
    //   11: invokevirtual 607	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   14: ifeq +90 -> 104
    //   17: aload_0
    //   18: getfield 510	android/media/MediaScanner:mContext	Landroid/content/Context;
    //   21: ldc_w 1404
    //   24: invokevirtual 1408	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   27: checkcast 1410	android/os/UserManager
    //   30: invokevirtual 1413	android/os/UserManager:getTwinAppsId	()I
    //   33: istore_3
    //   34: iload_3
    //   35: iconst_m1
    //   36: if_icmpeq +66 -> 102
    //   39: invokestatic 1418	android/os/UserHandle:myUserId	()I
    //   42: istore 4
    //   44: iload 4
    //   46: ifeq +9 -> 55
    //   49: iload 4
    //   51: iload_3
    //   52: if_icmpne +50 -> 102
    //   55: iload 4
    //   57: ifne +6 -> 63
    //   60: goto +5 -> 65
    //   63: iconst_0
    //   64: istore_3
    //   65: new 642	java/lang/StringBuilder
    //   68: dup
    //   69: invokespecial 643	java/lang/StringBuilder:<init>	()V
    //   72: astore_1
    //   73: aload_1
    //   74: ldc_w 1420
    //   77: invokevirtual 647	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload_1
    //   82: iload_3
    //   83: invokevirtual 1423	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: aload_0
    //   88: iconst_1
    //   89: anewarray 147	java/lang/String
    //   92: dup
    //   93: iconst_0
    //   94: aload_1
    //   95: invokevirtual 652	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   98: aastore
    //   99: invokevirtual 1425	android/media/MediaScanner:scanDirectories	([Ljava/lang/String;)V
    //   102: aconst_null
    //   103: areturn
    //   104: aload_0
    //   105: aload_1
    //   106: iconst_1
    //   107: invokespecial 1323	android/media/MediaScanner:prescan	(Ljava/lang/String;Z)V
    //   110: new 834	java/io/File
    //   113: astore 5
    //   115: aload 5
    //   117: aload_1
    //   118: invokespecial 836	java/io/File:<init>	(Ljava/lang/String;)V
    //   121: aload 5
    //   123: invokevirtual 880	java/io/File:exists	()Z
    //   126: ifeq +58 -> 184
    //   129: aload 5
    //   131: invokevirtual 1428	java/io/File:canRead	()Z
    //   134: ifne +6 -> 140
    //   137: goto +47 -> 184
    //   140: aload 5
    //   142: invokevirtual 1358	java/io/File:lastModified	()J
    //   145: ldc2_w 1359
    //   148: ldiv
    //   149: lstore 6
    //   151: aload_0
    //   152: getfield 505	android/media/MediaScanner:mClient	Landroid/media/MediaScanner$MyMediaScannerClient;
    //   155: aload_1
    //   156: aload_2
    //   157: lload 6
    //   159: aload 5
    //   161: invokevirtual 1376	java/io/File:length	()J
    //   164: aload 5
    //   166: invokevirtual 839	java/io/File:isDirectory	()Z
    //   169: iconst_1
    //   170: aload_1
    //   171: invokestatic 1392	android/media/MediaScanner:isNoMediaPath	(Ljava/lang/String;)Z
    //   174: invokevirtual 1396	android/media/MediaScanner$MyMediaScannerClient:doScanFile	(Ljava/lang/String;Ljava/lang/String;JJZZZ)Landroid/net/Uri;
    //   177: astore_1
    //   178: aload_0
    //   179: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   182: aload_1
    //   183: areturn
    //   184: aload_0
    //   185: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   188: aconst_null
    //   189: areturn
    //   190: astore_1
    //   191: goto +20 -> 211
    //   194: astore_1
    //   195: ldc 76
    //   197: ldc_w 1398
    //   200: aload_1
    //   201: invokestatic 1097	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   204: pop
    //   205: aload_0
    //   206: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   209: aconst_null
    //   210: areturn
    //   211: aload_0
    //   212: invokespecial 1353	android/media/MediaScanner:releaseResources	()V
    //   215: aload_1
    //   216: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	217	0	this	MediaScanner
    //   0	217	1	paramString1	String
    //   0	217	2	paramString2	String
    //   33	50	3	i	int
    //   42	14	4	j	int
    //   113	52	5	localFile	File
    //   149	9	6	l	long
    // Exception table:
    //   from	to	target	type
    //   104	137	190	finally
    //   140	178	190	finally
    //   195	205	190	finally
    //   104	137	194	android/os/RemoteException
    //   140	178	194	android/os/RemoteException
  }
  
  private static class FileEntry
  {
    int mFormat;
    long mLastModified;
    boolean mLastModifiedChanged;
    String mPath;
    long mRowId;
    
    FileEntry(long paramLong1, String paramString, long paramLong2, int paramInt)
    {
      mRowId = paramLong1;
      mPath = paramString;
      mLastModified = paramLong2;
      mFormat = paramInt;
      mLastModifiedChanged = false;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(mPath);
      localStringBuilder.append(" mRowId: ");
      localStringBuilder.append(mRowId);
      return localStringBuilder.toString();
    }
  }
  
  static class MediaBulkDeleter
  {
    final Uri mBaseUri;
    final ContentProviderClient mProvider;
    ArrayList<String> whereArgs = new ArrayList(100);
    StringBuilder whereClause = new StringBuilder();
    
    public MediaBulkDeleter(ContentProviderClient paramContentProviderClient, Uri paramUri)
    {
      mProvider = paramContentProviderClient;
      mBaseUri = paramUri;
    }
    
    public void delete(long paramLong)
      throws RemoteException
    {
      if (whereClause.length() != 0) {
        whereClause.append(",");
      }
      whereClause.append("?");
      ArrayList localArrayList = whereArgs;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("");
      localStringBuilder.append(paramLong);
      localArrayList.add(localStringBuilder.toString());
      if (whereArgs.size() > 100) {
        flush();
      }
    }
    
    public void flush()
      throws RemoteException
    {
      int i = whereArgs.size();
      if (i > 0)
      {
        Object localObject = new String[i];
        String[] arrayOfString = (String[])whereArgs.toArray((Object[])localObject);
        ContentProviderClient localContentProviderClient = mProvider;
        localObject = mBaseUri;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("_id IN (");
        localStringBuilder.append(whereClause.toString());
        localStringBuilder.append(")");
        localContentProviderClient.delete((Uri)localObject, localStringBuilder.toString(), arrayOfString);
        whereClause.setLength(0);
        whereArgs.clear();
      }
    }
  }
  
  private class MyMediaScannerClient
    implements MediaScannerClient
  {
    private String mAlbum;
    private String mAlbumArtist;
    private String mArtist;
    private int mCompilation;
    private String mComposer;
    private long mDate;
    private final SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    private int mDuration;
    private long mFileSize;
    private int mFileType;
    private String mGenre;
    private int mHeight;
    private boolean mIsDrm;
    private long mLastModified;
    private String mMimeType;
    private boolean mNoMedia;
    private String mPath;
    private boolean mScanSuccess;
    private String mTitle;
    private int mTrack;
    private int mWidth;
    private String mWriter;
    private int mYear;
    
    public MyMediaScannerClient()
    {
      mDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    private boolean convertGenreCode(String paramString1, String paramString2)
    {
      String str = getGenreName(paramString1);
      if (str.equals(paramString2)) {
        return true;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("'");
      localStringBuilder.append(paramString1);
      localStringBuilder.append("' -> '");
      localStringBuilder.append(str);
      localStringBuilder.append("', expected '");
      localStringBuilder.append(paramString2);
      localStringBuilder.append("'");
      Log.d("MediaScanner", localStringBuilder.toString());
      return false;
    }
    
    private boolean doesPathHaveFilename(String paramString1, String paramString2)
    {
      int i = paramString1.lastIndexOf(File.separatorChar);
      boolean bool = true;
      i++;
      int j = paramString2.length();
      if ((!paramString1.regionMatches(i, paramString2, 0, j)) || (i + j != paramString1.length())) {
        bool = false;
      }
      return bool;
    }
    
    private Uri endFile(MediaScanner.FileEntry paramFileEntry, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
      throws RemoteException
    {
      if ((mArtist == null) || (mArtist.length() == 0)) {
        mArtist = mAlbumArtist;
      }
      ContentValues localContentValues = toValues();
      Object localObject1 = localContentValues.getAsString("title");
      if (localObject1 != null)
      {
        localObject3 = localObject1;
        if (TextUtils.isEmpty(((String)localObject1).trim())) {}
      }
      for (;;)
      {
        break;
        localObject3 = MediaFile.getFileTitle(localContentValues.getAsString("_data"));
        localContentValues.put("title", (String)localObject3);
      }
      localObject1 = localContentValues.getAsString("album");
      Object localObject3 = localObject1;
      if ("<unknown>".equals(localObject1))
      {
        localObject1 = localContentValues.getAsString("_data");
        i = ((String)localObject1).lastIndexOf('/');
        localObject3 = localObject1;
        if (i >= 0)
        {
          for (j = 0;; j = k)
          {
            k = ((String)localObject1).indexOf('/', j + 1);
            if ((k < 0) || (k >= i)) {
              break;
            }
          }
          localObject3 = localObject1;
          if (j != 0)
          {
            localObject3 = ((String)localObject1).substring(j + 1, i);
            localContentValues.put("album", (String)localObject3);
          }
        }
      }
      long l1 = mRowId;
      if ((MediaFile.isAudioFileType(mFileType)) && ((l1 == 0L) || (mMtpObjectHandle != 0)))
      {
        localContentValues.put("is_ringtone", Boolean.valueOf(paramBoolean1));
        localContentValues.put("is_notification", Boolean.valueOf(paramBoolean2));
        localContentValues.put("is_alarm", Boolean.valueOf(paramBoolean3));
        localContentValues.put("is_music", Boolean.valueOf(paramBoolean4));
        localContentValues.put("is_podcast", Boolean.valueOf(paramBoolean5));
      }
      for (;;)
      {
        break;
        if ((mFileType == 31) || (mFileType == 37) || (MediaFile.isRawImageFileType(mFileType))) {
          if (!mNoMedia)
          {
            localObject3 = null;
            try
            {
              localObject1 = new android/media/ExifInterface;
              ((ExifInterface)localObject1).<init>(mPath);
              localObject3 = localObject1;
            }
            catch (IOException localIOException) {}
            if (localObject3 != null)
            {
              localObject2 = new float[2];
              if (((ExifInterface)localObject3).getLatLong((float[])localObject2))
              {
                localContentValues.put("latitude", Float.valueOf(localObject2[0]));
                localContentValues.put("longitude", Float.valueOf(localObject2[1]));
              }
              long l2 = ((ExifInterface)localObject3).getGpsDateTime();
              if (l2 != -1L)
              {
                localContentValues.put("datetaken", Long.valueOf(l2));
              }
              else
              {
                l2 = ((ExifInterface)localObject3).getDateTime();
                if ((l2 != -1L) && (Math.abs(mLastModified * 1000L - l2) >= 86400000L)) {
                  localContentValues.put("datetaken", Long.valueOf(l2));
                }
              }
              j = ((ExifInterface)localObject3).getAttributeInt("Orientation", -1);
              if (j != -1)
              {
                if (j != 3)
                {
                  if (j != 6)
                  {
                    if (j != 8) {
                      j = 0;
                    } else {
                      j = 270;
                    }
                  }
                  else {
                    j = 90;
                  }
                }
                else {
                  j = 180;
                }
                localContentValues.put("orientation", Integer.valueOf(j));
              }
            }
          }
        }
      }
      Object localObject2 = mFilesUri;
      MediaInserter localMediaInserter = mMediaInserter;
      localObject3 = localObject2;
      if (mScanSuccess)
      {
        localObject3 = localObject2;
        if (!mNoMedia) {
          if (MediaFile.isVideoFileType(mFileType))
          {
            localObject3 = mVideoUri;
          }
          else if (MediaFile.isImageFileType(mFileType))
          {
            localObject3 = mImagesUri;
          }
          else
          {
            localObject3 = localObject2;
            if (MediaFile.isAudioFileType(mFileType)) {
              localObject3 = mAudioUri;
            }
          }
        }
      }
      localObject2 = null;
      int m = 0;
      int k = 0;
      if ((paramBoolean2) && (!mDefaultNotificationSet))
      {
        if ((!TextUtils.isEmpty(mDefaultNotificationFilename)) && (!doesPathHaveFilename(mPath, mDefaultNotificationFilename)) && (!TextUtils.isEmpty(mDefaultNotification2Filename)))
        {
          j = m;
          i = k;
          if (!doesPathHaveFilename(mPath, mDefaultNotification2Filename)) {}
        }
        else
        {
          j = 1;
          i = 2;
        }
      }
      else if ((paramBoolean1) && (!mDefaultRingtoneSet))
      {
        if ((!TextUtils.isEmpty(mDefaultRingtoneFilename)) && (!doesPathHaveFilename(mPath, mDefaultRingtoneFilename)) && (!TextUtils.isEmpty(mDefaultRingtone2Filename)))
        {
          j = m;
          i = k;
          if (!doesPathHaveFilename(mPath, mDefaultRingtone2Filename)) {}
        }
        else
        {
          j = 1;
          i = k;
        }
      }
      else
      {
        j = m;
        i = k;
        if (paramBoolean3)
        {
          j = m;
          i = k;
          if (!mDefaultAlarmSet) {
            if (!TextUtils.isEmpty(mDefaultAlarmAlertFilename))
            {
              j = m;
              i = k;
              if (!doesPathHaveFilename(mPath, mDefaultAlarmAlertFilename)) {}
            }
            else
            {
              j = 1;
              i = k;
            }
          }
        }
      }
      StringBuilder localStringBuilder;
      if ((paramBoolean2) && (!mDefaultNewMailSet))
      {
        if ((!TextUtils.isEmpty(mDefaultNewMailFilename)) && (!doesPathHaveFilename(mPath, mDefaultNewMailFilename)))
        {
          k = j;
          m = i;
        }
        else
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("mDefaultNewMailFilename=");
          localStringBuilder.append(mDefaultNewMailFilename);
          Log.d("MediaScanner", localStringBuilder.toString());
          k = 1;
          m = 32;
        }
      }
      else
      {
        m = i;
        k = j;
      }
      int i = k;
      int j = m;
      if (paramBoolean2)
      {
        i = k;
        j = m;
        if (!mDefaultSentMailSet) {
          if (!TextUtils.isEmpty(mDefaultSentMailFilename))
          {
            i = k;
            j = m;
            if (!doesPathHaveFilename(mPath, mDefaultSentMailFilename)) {}
          }
          else if (TextUtils.isEmpty(mDefaultSentMailFilename))
          {
            MediaScanner.access$2602(MediaScanner.this, true);
            Log.d("MediaScanner", "mDefaultSentMailSet is null");
            i = k;
            j = m;
          }
          else
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("mDefaultSentMailFilename=");
            localStringBuilder.append(mDefaultSentMailFilename);
            Log.d("MediaScanner", localStringBuilder.toString());
            i = 1;
            j = 64;
          }
        }
      }
      m = i;
      k = j;
      if (paramBoolean2)
      {
        m = i;
        k = j;
        if (!mDefaultCalendarAlertSet) {
          if (!TextUtils.isEmpty(mDefaultCalendarAlertFilename))
          {
            m = i;
            k = j;
            if (!doesPathHaveFilename(mPath, mDefaultCalendarAlertFilename)) {}
          }
          else
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("mDefaultCalendarAlertFilename=");
            localStringBuilder.append(mDefaultCalendarAlertFilename);
            Log.d("MediaScanner", localStringBuilder.toString());
            m = 1;
            k = 128;
          }
        }
      }
      if (l1 == 0L)
      {
        if (mMtpObjectHandle != 0) {
          localContentValues.put("media_scanner_new_object_id", Integer.valueOf(mMtpObjectHandle));
        }
        if (localObject3 == mFilesUri)
        {
          i = mFormat;
          j = i;
          if (i == 0) {
            j = MediaFile.getFormatCode(mPath, mMimeType);
          }
          localContentValues.put("format", Integer.valueOf(j));
        }
        if ((localMediaInserter != null) && (m == 0))
        {
          if (mFormat == 12289) {
            localMediaInserter.insertwithPriority((Uri)localObject3, localContentValues);
          } else {
            localMediaInserter.insert((Uri)localObject3, localContentValues);
          }
        }
        else
        {
          if (localMediaInserter != null) {
            localMediaInserter.flushAll();
          }
          localObject2 = mMediaProvider.insert((Uri)localObject3, localContentValues);
        }
        if (localObject2 != null)
        {
          l1 = ContentUris.parseId((Uri)localObject2);
          mRowId = l1;
        }
      }
      else
      {
        localObject2 = ContentUris.withAppendedId((Uri)localObject3, l1);
        localContentValues.remove("_data");
        if ((mScanSuccess) && (!MediaScanner.isNoMediaPath(mPath)))
        {
          j = MediaFile.getFileTypeForMimeType(mMimeType);
          if (MediaFile.isAudioFileType(j)) {
            j = 2;
          }
          for (;;)
          {
            break;
            if (MediaFile.isVideoFileType(j)) {
              j = 3;
            } else if (MediaFile.isImageFileType(j)) {
              j = 1;
            } else if (MediaFile.isPlayListFileType(j)) {
              j = 4;
            } else {
              j = 0;
            }
          }
          localContentValues.put("media_type", Integer.valueOf(j));
        }
        mMediaProvider.update((Uri)localObject2, localContentValues, null, null);
      }
      if (m != 0) {
        if ((paramBoolean2) && (k == 2))
        {
          setRingtoneIfNotSet("notification_sound", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_notification_sound", (Uri)localObject3, l1);
          setRingtoneIfNotSet("notification_sound_2", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_notification_sound_2", (Uri)localObject3, l1);
          MediaScanner.access$502(MediaScanner.this, true);
        }
        else if ((paramBoolean2) && (k == 32))
        {
          setRingtoneIfNotSet("newmail_sound", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_newmail_sound", (Uri)localObject3, l1);
          MediaScanner.access$2402(MediaScanner.this, true);
        }
        else if ((paramBoolean2) && (k == 64))
        {
          setRingtoneIfNotSet("sentmail_sound", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_sentmail_sound", (Uri)localObject3, l1);
          MediaScanner.access$2602(MediaScanner.this, true);
        }
        else if ((paramBoolean2) && (k == 128))
        {
          setRingtoneIfNotSet("calendaralert_sound", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_calendaralert_sound", (Uri)localObject3, l1);
          MediaScanner.access$2802(MediaScanner.this, true);
        }
        else if (paramBoolean1)
        {
          setRingtoneIfNotSet("ringtone", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_ringtone", (Uri)localObject3, l1);
          setRingtoneIfNotSet("ringtone_2", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_ringtone_2", (Uri)localObject3, l1);
          MediaScanner.access$702(MediaScanner.this, true);
        }
        else if (paramBoolean3)
        {
          setRingtoneIfNotSet("alarm_alert", (Uri)localObject3, l1);
          setRingtoneIfNotSet("default_alarm_alert", (Uri)localObject3, l1);
          MediaScanner.access$902(MediaScanner.this, true);
        }
      }
      return localObject2;
    }
    
    private int getFileTypeFromDrm(String paramString)
    {
      if (!MediaScanner.this.isDrmEnabled()) {
        return 0;
      }
      int i = 0;
      if (mDrmManagerClient == null) {
        MediaScanner.access$3402(MediaScanner.this, new DrmManagerClient(mContext));
      }
      int j = i;
      if (mDrmManagerClient.canHandle(paramString, null))
      {
        mIsDrm = true;
        paramString = mDrmManagerClient.getOriginalMimeType(paramString);
        j = i;
        if (paramString != null)
        {
          mMimeType = paramString;
          j = MediaFile.getFileTypeForMimeType(paramString);
        }
      }
      return j;
    }
    
    private long parseDate(String paramString)
    {
      try
      {
        long l = mDateFormatter.parse(paramString).getTime();
        return l;
      }
      catch (ParseException paramString) {}
      return 0L;
    }
    
    private int parseSubstring(String paramString, int paramInt1, int paramInt2)
    {
      int i = paramString.length();
      if (paramInt1 == i) {
        return paramInt2;
      }
      int j = paramInt1 + 1;
      paramInt1 = paramString.charAt(paramInt1);
      if ((paramInt1 >= 48) && (paramInt1 <= 57))
      {
        paramInt1 -= 48;
        paramInt2 = j;
        while (paramInt2 < i)
        {
          j = paramString.charAt(paramInt2);
          if ((j >= 48) && (j <= 57))
          {
            paramInt1 = paramInt1 * 10 + (j - 48);
            paramInt2++;
          }
          else
          {
            return paramInt1;
          }
        }
        return paramInt1;
      }
      return paramInt2;
    }
    
    private boolean processImageFile(String paramString)
    {
      boolean bool1 = false;
      try
      {
        mBitmapOptions.outWidth = 0;
        mBitmapOptions.outHeight = 0;
        BitmapFactory.decodeFile(paramString, mBitmapOptions);
        mWidth = mBitmapOptions.outWidth;
        mHeight = mBitmapOptions.outHeight;
        boolean bool2 = bool1;
        if (mWidth > 0)
        {
          int i = mHeight;
          bool2 = bool1;
          if (i > 0) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (Throwable paramString) {}
      return false;
    }
    
    private void setRingtoneIfNotSet(String paramString, Uri paramUri, long paramLong)
    {
      if (MediaScanner.this.wasRingtoneAlreadySet(paramString)) {
        return;
      }
      ContentResolver localContentResolver = mContext.getContentResolver();
      if (TextUtils.isEmpty(Settings.System.getString(localContentResolver, paramString)))
      {
        Uri localUri = Settings.System.getUriFor(paramString);
        paramUri = ContentUris.withAppendedId(paramUri, paramLong);
        if (paramString.startsWith("default_"))
        {
          if (paramUri != null) {
            paramUri = paramUri.toString();
          } else {
            paramUri = null;
          }
          Settings.System.putStringForUser(localContentResolver, paramString, paramUri, mContext.getUserId());
        }
        else
        {
          UserManager localUserManager = (UserManager)mContext.getSystemService("user");
          RingtoneManager.setActualDefaultRingtoneUri(mContext, RingtoneManager.getDefaultType(localUri), paramUri, localUserManager.isManagedProfile(UserHandle.myUserId()));
        }
      }
      Settings.System.putInt(localContentResolver, MediaScanner.this.settingSetIndicatorName(paramString), 1);
    }
    
    private void testGenreNameConverter()
    {
      convertGenreCode("2", "Country");
      convertGenreCode("(2)", "Country");
      convertGenreCode("(2", "(2");
      convertGenreCode("2 Foo", "Country");
      convertGenreCode("(2) Foo", "Country");
      convertGenreCode("(2 Foo", "(2 Foo");
      convertGenreCode("2Foo", "2Foo");
      convertGenreCode("(2)Foo", "Country");
      convertGenreCode("200 Foo", "Foo");
      convertGenreCode("(200) Foo", "Foo");
      convertGenreCode("200Foo", "200Foo");
      convertGenreCode("(200)Foo", "Foo");
      convertGenreCode("200)Foo", "200)Foo");
      convertGenreCode("200) Foo", "200) Foo");
    }
    
    private ContentValues toValues()
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("_data", mPath);
      localContentValues.put("title", mTitle);
      localContentValues.put("date_modified", Long.valueOf(mLastModified));
      localContentValues.put("_size", Long.valueOf(mFileSize));
      localContentValues.put("mime_type", mMimeType);
      localContentValues.put("is_drm", Boolean.valueOf(mIsDrm));
      String str = null;
      Object localObject = str;
      if (mWidth > 0)
      {
        localObject = str;
        if (mHeight > 0)
        {
          localContentValues.put("width", Integer.valueOf(mWidth));
          localContentValues.put("height", Integer.valueOf(mHeight));
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(mWidth);
          ((StringBuilder)localObject).append("x");
          ((StringBuilder)localObject).append(mHeight);
          localObject = ((StringBuilder)localObject).toString();
        }
      }
      if (!mNoMedia)
      {
        if (MediaFile.isVideoFileType(mFileType))
        {
          if ((mArtist != null) && (mArtist.length() > 0)) {
            str = mArtist;
          } else {
            str = "<unknown>";
          }
          localContentValues.put("artist", str);
          if ((mAlbum != null) && (mAlbum.length() > 0)) {
            str = mAlbum;
          } else {
            str = "<unknown>";
          }
          localContentValues.put("album", str);
          localContentValues.put("duration", Integer.valueOf(mDuration));
          if (localObject != null) {
            localContentValues.put("resolution", (String)localObject);
          }
          if (mDate > 0L) {
            localContentValues.put("datetaken", Long.valueOf(mDate));
          }
        }
        else if ((!MediaFile.isImageFileType(mFileType)) && (mScanSuccess) && (MediaFile.isAudioFileType(mFileType)))
        {
          if ((mArtist != null) && (mArtist.length() > 0)) {
            localObject = mArtist;
          } else {
            localObject = "<unknown>";
          }
          localContentValues.put("artist", (String)localObject);
          if ((mAlbumArtist != null) && (mAlbumArtist.length() > 0)) {
            localObject = mAlbumArtist;
          } else {
            localObject = null;
          }
          localContentValues.put("album_artist", (String)localObject);
          if ((mAlbum != null) && (mAlbum.length() > 0)) {
            localObject = mAlbum;
          } else {
            localObject = "<unknown>";
          }
          localContentValues.put("album", (String)localObject);
          localContentValues.put("composer", mComposer);
          localContentValues.put("genre", mGenre);
          if (mYear != 0) {
            localContentValues.put("year", Integer.valueOf(mYear));
          }
          localContentValues.put("track", Integer.valueOf(mTrack));
          localContentValues.put("duration", Integer.valueOf(mDuration));
          localContentValues.put("compilation", Integer.valueOf(mCompilation));
        }
        if (!mScanSuccess) {
          localContentValues.put("media_type", Integer.valueOf(0));
        }
      }
      return localContentValues;
    }
    
    public MediaScanner.FileEntry beginFile(String paramString1, String paramString2, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2)
    {
      mMimeType = paramString2;
      mFileType = 0;
      mFileSize = paramLong2;
      mIsDrm = false;
      mScanSuccess = true;
      if (!paramBoolean1)
      {
        if ((!paramBoolean2) && (MediaScanner.isNoMediaFile(paramString1))) {
          paramBoolean2 = true;
        }
        mNoMedia = paramBoolean2;
        if (paramString2 != null) {
          mFileType = MediaFile.getFileTypeForMimeType(paramString2);
        }
        if (mFileType == 0)
        {
          paramString2 = MediaFile.getFileType(paramString1);
          if (paramString2 != null)
          {
            mFileType = fileType;
            if (mMimeType == null) {
              mMimeType = mimeType;
            }
          }
        }
        if ((MediaScanner.this.isDrmEnabled()) && (MediaFile.isDrmFileType(mFileType))) {
          mFileType = getFileTypeFromDrm(paramString1);
        }
      }
      paramString2 = makeEntryFor(paramString1);
      if (paramString2 != null) {
        paramLong2 = paramLong1 - mLastModified;
      } else {
        paramLong2 = 0L;
      }
      int i;
      if ((paramLong2 <= 1L) && (paramLong2 >= -1L)) {
        i = 0;
      } else {
        i = 1;
      }
      String str;
      if (paramString2 != null)
      {
        str = paramString2;
        if (i == 0) {}
      }
      else
      {
        if (i != 0)
        {
          mLastModified = paramLong1;
        }
        else
        {
          if (paramBoolean1) {
            i = 12289;
          } else {
            i = 0;
          }
          paramString2 = new MediaScanner.FileEntry(0L, paramString1, paramLong1, i);
        }
        mLastModifiedChanged = true;
        str = paramString2;
      }
      if ((mProcessPlaylists) && (MediaFile.isPlayListFileType(mFileType)))
      {
        mPlayLists.add(str);
        return null;
      }
      mArtist = null;
      mAlbumArtist = null;
      mAlbum = null;
      mTitle = null;
      mComposer = null;
      mGenre = null;
      mTrack = 0;
      mYear = 0;
      mDuration = 0;
      mPath = paramString1;
      mDate = 0L;
      mLastModified = paramLong1;
      mWriter = null;
      mCompilation = 0;
      mWidth = 0;
      mHeight = 0;
      return str;
    }
    
    /* Error */
    public Uri doScanFile(String paramString1, String paramString2, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore 10
      //   3: aconst_null
      //   4: astore 11
      //   6: aload_0
      //   7: aload_1
      //   8: aload_2
      //   9: lload_3
      //   10: lload 5
      //   12: iload 7
      //   14: iload 9
      //   16: invokevirtual 767	android/media/MediaScanner$MyMediaScannerClient:beginFile	(Ljava/lang/String;Ljava/lang/String;JJZZ)Landroid/media/MediaScanner$FileEntry;
      //   19: astore 12
      //   21: aload 12
      //   23: ifnonnull +5 -> 28
      //   26: aconst_null
      //   27: areturn
      //   28: aload_0
      //   29: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   32: invokestatic 192	android/media/MediaScanner:access$400	(Landroid/media/MediaScanner;)I
      //   35: istore 13
      //   37: iload 13
      //   39: ifeq +16 -> 55
      //   42: aload 12
      //   44: lconst_0
      //   45: putfield 182	android/media/MediaScanner$FileEntry:mRowId	J
      //   48: goto +7 -> 55
      //   51: astore_1
      //   52: goto +593 -> 645
      //   55: aload 12
      //   57: getfield 220	android/media/MediaScanner$FileEntry:mPath	Ljava/lang/String;
      //   60: astore 14
      //   62: aload 14
      //   64: ifnull +222 -> 286
      //   67: aload_0
      //   68: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   71: invokestatic 313	android/media/MediaScanner:access$500	(Landroid/media/MediaScanner;)Z
      //   74: ifne +22 -> 96
      //   77: aload_0
      //   78: aload 12
      //   80: getfield 220	android/media/MediaScanner$FileEntry:mPath	Ljava/lang/String;
      //   83: aload_0
      //   84: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   87: invokestatic 317	android/media/MediaScanner:access$600	(Landroid/media/MediaScanner;)Ljava/lang/String;
      //   90: invokespecial 319	android/media/MediaScanner$MyMediaScannerClient:doesPathHaveFilename	(Ljava/lang/String;Ljava/lang/String;)Z
      //   93: ifne +61 -> 154
      //   96: aload_0
      //   97: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   100: invokestatic 325	android/media/MediaScanner:access$700	(Landroid/media/MediaScanner;)Z
      //   103: ifne +22 -> 125
      //   106: aload_0
      //   107: aload 12
      //   109: getfield 220	android/media/MediaScanner$FileEntry:mPath	Ljava/lang/String;
      //   112: aload_0
      //   113: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   116: invokestatic 328	android/media/MediaScanner:access$800	(Landroid/media/MediaScanner;)Ljava/lang/String;
      //   119: invokespecial 319	android/media/MediaScanner$MyMediaScannerClient:doesPathHaveFilename	(Ljava/lang/String;Ljava/lang/String;)Z
      //   122: ifne +32 -> 154
      //   125: aload_0
      //   126: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   129: invokestatic 334	android/media/MediaScanner:access$900	(Landroid/media/MediaScanner;)Z
      //   132: ifne +78 -> 210
      //   135: aload_0
      //   136: aload 12
      //   138: getfield 220	android/media/MediaScanner$FileEntry:mPath	Ljava/lang/String;
      //   141: aload_0
      //   142: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   145: invokestatic 337	android/media/MediaScanner:access$1000	(Landroid/media/MediaScanner;)Ljava/lang/String;
      //   148: invokespecial 319	android/media/MediaScanner$MyMediaScannerClient:doesPathHaveFilename	(Ljava/lang/String;Ljava/lang/String;)Z
      //   151: ifeq +59 -> 210
      //   154: new 82	java/lang/StringBuilder
      //   157: astore 14
      //   159: aload 14
      //   161: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   164: aload 14
      //   166: ldc_w 769
      //   169: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   172: pop
      //   173: aload 14
      //   175: aload 12
      //   177: getfield 220	android/media/MediaScanner$FileEntry:mPath	Ljava/lang/String;
      //   180: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   183: pop
      //   184: aload 14
      //   186: ldc_w 771
      //   189: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   192: pop
      //   193: ldc 95
      //   195: aload 14
      //   197: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   200: invokestatic 774	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   203: pop
      //   204: iconst_1
      //   205: istore 7
      //   207: goto +83 -> 290
      //   210: aload 12
      //   212: getfield 220	android/media/MediaScanner$FileEntry:mPath	Ljava/lang/String;
      //   215: invokestatic 777	android/media/MediaScanner:access$1100	(Ljava/lang/String;)Z
      //   218: ifeq +68 -> 286
      //   221: getstatic 782	android/os/Build:FINGERPRINT	Ljava/lang/String;
      //   224: invokestatic 785	android/media/MediaScanner:access$1200	()Ljava/lang/String;
      //   227: invokevirtual 80	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   230: ifne +56 -> 286
      //   233: new 82	java/lang/StringBuilder
      //   236: astore 14
      //   238: aload 14
      //   240: invokespecial 83	java/lang/StringBuilder:<init>	()V
      //   243: aload 14
      //   245: ldc_w 769
      //   248: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   251: pop
      //   252: aload 14
      //   254: aload 12
      //   256: getfield 220	android/media/MediaScanner$FileEntry:mPath	Ljava/lang/String;
      //   259: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   262: pop
      //   263: aload 14
      //   265: ldc_w 787
      //   268: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   271: pop
      //   272: ldc 95
      //   274: aload 14
      //   276: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   279: invokestatic 790	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   282: pop
      //   283: goto -79 -> 204
      //   286: iload 8
      //   288: istore 7
      //   290: aload 12
      //   292: ifnull +346 -> 638
      //   295: aload 12
      //   297: getfield 749	android/media/MediaScanner$FileEntry:mLastModifiedChanged	Z
      //   300: istore 8
      //   302: iload 8
      //   304: ifne +8 -> 312
      //   307: iload 7
      //   309: ifeq +329 -> 638
      //   312: iload 9
      //   314: ifeq +22 -> 336
      //   317: aload_0
      //   318: aload 12
      //   320: iconst_0
      //   321: iconst_0
      //   322: iconst_0
      //   323: iconst_0
      //   324: iconst_0
      //   325: invokespecial 792	android/media/MediaScanner$MyMediaScannerClient:endFile	(Landroid/media/MediaScanner$FileEntry;ZZZZZ)Landroid/net/Uri;
      //   328: astore_1
      //   329: goto +312 -> 641
      //   332: astore_1
      //   333: goto +312 -> 645
      //   336: aload_0
      //   337: getfield 184	android/media/MediaScanner$MyMediaScannerClient:mFileType	I
      //   340: invokestatic 188	android/media/MediaFile:isAudioFileType	(I)Z
      //   343: istore 8
      //   345: aload_0
      //   346: getfield 184	android/media/MediaScanner$MyMediaScannerClient:mFileType	I
      //   349: invokestatic 297	android/media/MediaFile:isVideoFileType	(I)Z
      //   352: istore 7
      //   354: aload_0
      //   355: getfield 184	android/media/MediaScanner$MyMediaScannerClient:mFileType	I
      //   358: invokestatic 303	android/media/MediaFile:isImageFileType	(I)Z
      //   361: istore 9
      //   363: iload 8
      //   365: ifne +19 -> 384
      //   368: iload 7
      //   370: ifne +14 -> 384
      //   373: iload 9
      //   375: ifeq +6 -> 381
      //   378: goto +6 -> 384
      //   381: goto +23 -> 404
      //   384: new 108	java/io/File
      //   387: astore 11
      //   389: aload 11
      //   391: aload_1
      //   392: invokespecial 793	java/io/File:<init>	(Ljava/lang/String;)V
      //   395: aload 11
      //   397: invokestatic 799	android/os/Environment:maybeTranslateEmulatedPathToInternal	(Ljava/io/File;)Ljava/io/File;
      //   400: invokevirtual 802	java/io/File:getAbsolutePath	()Ljava/lang/String;
      //   403: astore_1
      //   404: iload 8
      //   406: ifne +14 -> 420
      //   409: iload 7
      //   411: ifeq +6 -> 417
      //   414: goto +6 -> 420
      //   417: goto +17 -> 434
      //   420: aload_0
      //   421: aload_0
      //   422: getfield 43	android/media/MediaScanner$MyMediaScannerClient:this$0	Landroid/media/MediaScanner;
      //   425: aload_1
      //   426: aload_2
      //   427: aload_0
      //   428: invokestatic 806	android/media/MediaScanner:access$1300	(Landroid/media/MediaScanner;Ljava/lang/String;Ljava/lang/String;Landroid/media/MediaScannerClient;)Z
      //   431: putfield 294	android/media/MediaScanner$MyMediaScannerClient:mScanSuccess	Z
      //   434: iload 9
      //   436: ifeq +12 -> 448
      //   439: aload_0
      //   440: aload_0
      //   441: aload_1
      //   442: invokespecial 808	android/media/MediaScanner$MyMediaScannerClient:processImageFile	(Ljava/lang/String;)Z
      //   445: putfield 294	android/media/MediaScanner$MyMediaScannerClient:mScanSuccess	Z
      //   448: aload_1
      //   449: getstatic 814	java/util/Locale:ROOT	Ljava/util/Locale;
      //   452: invokevirtual 818	java/lang/String:toLowerCase	(Ljava/util/Locale;)Ljava/lang/String;
      //   455: astore_1
      //   456: aload_0
      //   457: getfield 294	android/media/MediaScanner$MyMediaScannerClient:mScanSuccess	Z
      //   460: ifeq +19 -> 479
      //   463: aload_1
      //   464: ldc_w 820
      //   467: invokevirtual 822	java/lang/String:indexOf	(Ljava/lang/String;)I
      //   470: ifle +9 -> 479
      //   473: iconst_1
      //   474: istore 7
      //   476: goto +6 -> 482
      //   479: iconst_0
      //   480: istore 7
      //   482: aload_0
      //   483: getfield 294	android/media/MediaScanner$MyMediaScannerClient:mScanSuccess	Z
      //   486: ifeq +19 -> 505
      //   489: aload_1
      //   490: ldc_w 824
      //   493: invokevirtual 822	java/lang/String:indexOf	(Ljava/lang/String;)I
      //   496: ifle +9 -> 505
      //   499: iconst_1
      //   500: istore 8
      //   502: goto +6 -> 508
      //   505: iconst_0
      //   506: istore 8
      //   508: aload_0
      //   509: getfield 294	android/media/MediaScanner$MyMediaScannerClient:mScanSuccess	Z
      //   512: ifeq +19 -> 531
      //   515: aload_1
      //   516: ldc_w 826
      //   519: invokevirtual 822	java/lang/String:indexOf	(Ljava/lang/String;)I
      //   522: ifle +9 -> 531
      //   525: iconst_1
      //   526: istore 9
      //   528: goto +6 -> 534
      //   531: iconst_0
      //   532: istore 9
      //   534: aload_0
      //   535: getfield 294	android/media/MediaScanner$MyMediaScannerClient:mScanSuccess	Z
      //   538: ifeq +19 -> 557
      //   541: aload_1
      //   542: ldc_w 828
      //   545: invokevirtual 822	java/lang/String:indexOf	(Ljava/lang/String;)I
      //   548: ifle +9 -> 557
      //   551: iconst_1
      //   552: istore 15
      //   554: goto +6 -> 560
      //   557: iconst_0
      //   558: istore 15
      //   560: aload_0
      //   561: getfield 294	android/media/MediaScanner$MyMediaScannerClient:mScanSuccess	Z
      //   564: ifeq +39 -> 603
      //   567: aload_1
      //   568: ldc_w 830
      //   571: invokevirtual 822	java/lang/String:indexOf	(Ljava/lang/String;)I
      //   574: ifgt +23 -> 597
      //   577: iload 7
      //   579: ifne +24 -> 603
      //   582: iload 8
      //   584: ifne +19 -> 603
      //   587: iload 9
      //   589: ifne +14 -> 603
      //   592: iload 15
      //   594: ifne +9 -> 603
      //   597: iconst_1
      //   598: istore 16
      //   600: goto +6 -> 606
      //   603: iconst_0
      //   604: istore 16
      //   606: aload_0
      //   607: aload 12
      //   609: iload 7
      //   611: iload 8
      //   613: iload 9
      //   615: iload 16
      //   617: iload 15
      //   619: invokespecial 792	android/media/MediaScanner$MyMediaScannerClient:endFile	(Landroid/media/MediaScanner$FileEntry;ZZZZZ)Landroid/net/Uri;
      //   622: astore_1
      //   623: goto +18 -> 641
      //   626: astore_1
      //   627: goto +18 -> 645
      //   630: astore_1
      //   631: goto +14 -> 645
      //   634: astore_1
      //   635: goto +10 -> 645
      //   638: aload 11
      //   640: astore_1
      //   641: goto +17 -> 658
      //   644: astore_1
      //   645: ldc 95
      //   647: ldc_w 832
      //   650: aload_1
      //   651: invokestatic 836	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   654: pop
      //   655: aload 10
      //   657: astore_1
      //   658: aload_1
      //   659: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	660	0	this	MyMediaScannerClient
      //   0	660	1	paramString1	String
      //   0	660	2	paramString2	String
      //   0	660	3	paramLong1	long
      //   0	660	5	paramLong2	long
      //   0	660	7	paramBoolean1	boolean
      //   0	660	8	paramBoolean2	boolean
      //   0	660	9	paramBoolean3	boolean
      //   1	655	10	localObject1	Object
      //   4	635	11	localFile	File
      //   19	589	12	localFileEntry	MediaScanner.FileEntry
      //   35	3	13	i	int
      //   60	215	14	localObject2	Object
      //   552	66	15	bool1	boolean
      //   598	18	16	bool2	boolean
      // Exception table:
      //   from	to	target	type
      //   42	48	51	android/os/RemoteException
      //   67	96	51	android/os/RemoteException
      //   96	125	51	android/os/RemoteException
      //   125	154	51	android/os/RemoteException
      //   154	204	51	android/os/RemoteException
      //   210	283	51	android/os/RemoteException
      //   317	329	332	android/os/RemoteException
      //   420	434	626	android/os/RemoteException
      //   439	448	626	android/os/RemoteException
      //   448	473	626	android/os/RemoteException
      //   482	499	626	android/os/RemoteException
      //   508	525	626	android/os/RemoteException
      //   534	551	626	android/os/RemoteException
      //   560	577	626	android/os/RemoteException
      //   606	623	626	android/os/RemoteException
      //   389	404	630	android/os/RemoteException
      //   295	302	634	android/os/RemoteException
      //   336	363	634	android/os/RemoteException
      //   384	389	634	android/os/RemoteException
      //   6	21	644	android/os/RemoteException
      //   28	37	644	android/os/RemoteException
      //   55	62	644	android/os/RemoteException
    }
    
    public String getGenreName(String paramString)
    {
      if (paramString == null) {
        return null;
      }
      int i = paramString.length();
      if (i > 0)
      {
        int j = 0;
        Object localObject = new StringBuffer();
        char c;
        for (int k = 0; k < i; k++)
        {
          c = paramString.charAt(k);
          if ((k == 0) && (c == '('))
          {
            j = 1;
          }
          else
          {
            if (!Character.isDigit(c)) {
              break;
            }
            ((StringBuffer)localObject).append(c);
          }
        }
        int m;
        if (k < i)
        {
          m = paramString.charAt(k);
          c = m;
        }
        else
        {
          m = 32;
          c = m;
        }
        if (((j != 0) && (c == ')')) || ((j == 0) && (Character.isWhitespace(c)))) {
          try
          {
            m = Short.parseShort(((StringBuffer)localObject).toString());
            if (m >= 0)
            {
              if ((m < MediaScanner.ID3_GENRES.length) && (MediaScanner.ID3_GENRES[m] != null)) {
                return MediaScanner.ID3_GENRES[m];
              }
              if (m == 255) {
                return null;
              }
              if ((m < 255) && (k + 1 < i))
              {
                i = k;
                if (j != 0)
                {
                  i = k;
                  if (c == ')') {
                    i = k + 1;
                  }
                }
                localObject = paramString.substring(i).trim();
                if (((String)localObject).length() != 0) {
                  return localObject;
                }
              }
              else
              {
                localObject = ((StringBuffer)localObject).toString();
                return localObject;
              }
            }
          }
          catch (NumberFormatException localNumberFormatException) {}
        }
      }
      return paramString;
    }
    
    public void handleStringTag(String paramString1, String paramString2)
    {
      if ((!paramString1.equalsIgnoreCase("title")) && (!paramString1.startsWith("title;")))
      {
        if ((!paramString1.equalsIgnoreCase("artist")) && (!paramString1.startsWith("artist;")))
        {
          if ((!paramString1.equalsIgnoreCase("albumartist")) && (!paramString1.startsWith("albumartist;")) && (!paramString1.equalsIgnoreCase("band")) && (!paramString1.startsWith("band;")))
          {
            if ((!paramString1.equalsIgnoreCase("album")) && (!paramString1.startsWith("album;")))
            {
              if ((!paramString1.equalsIgnoreCase("composer")) && (!paramString1.startsWith("composer;")))
              {
                if ((mProcessGenres) && ((paramString1.equalsIgnoreCase("genre")) || (paramString1.startsWith("genre;"))))
                {
                  mGenre = getGenreName(paramString2);
                }
                else
                {
                  boolean bool1 = paramString1.equalsIgnoreCase("year");
                  boolean bool2 = false;
                  if ((!bool1) && (!paramString1.startsWith("year;")))
                  {
                    if ((!paramString1.equalsIgnoreCase("tracknumber")) && (!paramString1.startsWith("tracknumber;")))
                    {
                      if ((!paramString1.equalsIgnoreCase("discnumber")) && (!paramString1.equals("set")) && (!paramString1.startsWith("set;")))
                      {
                        if (paramString1.equalsIgnoreCase("duration")) {
                          mDuration = parseSubstring(paramString2, 0, 0);
                        } else if ((!paramString1.equalsIgnoreCase("writer")) && (!paramString1.startsWith("writer;")))
                        {
                          if (paramString1.equalsIgnoreCase("compilation"))
                          {
                            mCompilation = parseSubstring(paramString2, 0, 0);
                          }
                          else if (paramString1.equalsIgnoreCase("isdrm"))
                          {
                            if (parseSubstring(paramString2, 0, 0) == 1) {
                              bool2 = true;
                            }
                            mIsDrm = bool2;
                          }
                          else if (paramString1.equalsIgnoreCase("date"))
                          {
                            mDate = parseDate(paramString2);
                          }
                          else if (paramString1.equalsIgnoreCase("width"))
                          {
                            mWidth = parseSubstring(paramString2, 0, 0);
                          }
                          else if (paramString1.equalsIgnoreCase("height"))
                          {
                            mHeight = parseSubstring(paramString2, 0, 0);
                          }
                        }
                        else {
                          mWriter = paramString2.trim();
                        }
                      }
                      else {
                        mTrack = (parseSubstring(paramString2, 0, 0) * 1000 + mTrack % 1000);
                      }
                    }
                    else
                    {
                      int i = parseSubstring(paramString2, 0, 0);
                      mTrack = (mTrack / 1000 * 1000 + i);
                    }
                  }
                  else {
                    mYear = parseSubstring(paramString2, 0, 0);
                  }
                }
              }
              else {
                mComposer = paramString2.trim();
              }
            }
            else {
              mAlbum = paramString2.trim();
            }
          }
          else {
            mAlbumArtist = paramString2.trim();
          }
        }
        else {
          mArtist = paramString2.trim();
        }
      }
      else {
        mTitle = paramString2;
      }
    }
    
    public void scanFile(String paramString, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2)
    {
      doScanFile(paramString, null, paramLong1, paramLong2, paramBoolean1, false, paramBoolean2);
    }
    
    public void setMimeType(String paramString)
    {
      if (("audio/mp4".equals(mMimeType)) && (paramString.startsWith("video"))) {
        return;
      }
      mMimeType = paramString;
      mFileType = MediaFile.getFileTypeForMimeType(paramString);
    }
  }
  
  private static class PlaylistEntry
  {
    long bestmatchid;
    int bestmatchlevel;
    String path;
    
    private PlaylistEntry() {}
  }
  
  class WplHandler
    implements ElementListener
  {
    final ContentHandler handler;
    String playListDirectory;
    
    public WplHandler(String paramString, Uri paramUri, Cursor paramCursor)
    {
      playListDirectory = paramString;
      this$1 = new RootElement("smil");
      getChild("body").getChild("seq").getChild("media").setElementListener(this);
      handler = MediaScanner.this.getContentHandler();
    }
    
    public void end() {}
    
    ContentHandler getContentHandler()
    {
      return handler;
    }
    
    public void start(Attributes paramAttributes)
    {
      paramAttributes = paramAttributes.getValue("", "src");
      if (paramAttributes != null) {
        MediaScanner.this.cachePlaylistEntry(paramAttributes, playListDirectory);
      }
    }
  }
}
