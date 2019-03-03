package android.media;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.database.StaleDataException;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.database.SortCursor;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class RingtoneManager
{
  public static final String ACTION_EXTERNAL_AUDIO_PICKER = "asus.intent.action.AUDIO_PICKER";
  public static final String ACTION_RINGTONE_PICKER = "android.intent.action.RINGTONE_PICKER";
  public static final String EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS = "android.intent.extra.ringtone.AUDIO_ATTRIBUTES_FLAGS";
  public static final String EXTRA_RINGTONE_DEFAULT_URI = "android.intent.extra.ringtone.DEFAULT_URI";
  public static final String EXTRA_RINGTONE_EXISTING_URI = "android.intent.extra.ringtone.EXISTING_URI";
  @Deprecated
  public static final String EXTRA_RINGTONE_INCLUDE_DRM = "android.intent.extra.ringtone.INCLUDE_DRM";
  public static final String EXTRA_RINGTONE_PICKED_URI = "android.intent.extra.ringtone.PICKED_URI";
  public static final String EXTRA_RINGTONE_SHOW_DEFAULT = "android.intent.extra.ringtone.SHOW_DEFAULT";
  public static final String EXTRA_RINGTONE_SHOW_SILENT = "android.intent.extra.ringtone.SHOW_SILENT";
  public static final String EXTRA_RINGTONE_TITLE = "android.intent.extra.ringtone.TITLE";
  public static final String EXTRA_RINGTONE_TYPE = "android.intent.extra.ringtone.TYPE";
  public static final int ID_COLUMN_INDEX = 0;
  private static final String[] INTERNAL_COLUMNS;
  private static final String[] MEDIA_COLUMNS;
  private static final String TAG = "RingtoneManager";
  private static final String TAGD = "RingtoneManager-debug";
  public static final int TITLE_COLUMN_INDEX = 1;
  public static final int TYPE_ALARM = 4;
  public static final int TYPE_ALL = 7;
  public static final int TYPE_CALENDARALERT = 128;
  public static final int TYPE_EXTERNALMUSIC = 8;
  public static final int TYPE_NEWMAIL = 32;
  public static final int TYPE_NOTIFICATION = 2;
  public static final int TYPE_NOTIFICATION_2 = 512;
  public static final int TYPE_RINGTONE = 1;
  public static final int TYPE_RINGTONE_2 = 256;
  public static final int TYPE_SENTMAIL = 64;
  public static final int URI_COLUMN_INDEX = 2;
  private final Activity mActivity;
  private final Context mContext;
  private Cursor mCursor;
  private final List<String> mFilterColumns = new ArrayList();
  private boolean mIncludeParentRingtones;
  private Ringtone mPreviousRingtone;
  private boolean mStopPreviousRingtone = true;
  private int mType = 1;
  
  static
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("\"");
    localStringBuilder.append(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
    localStringBuilder.append("\"");
    INTERNAL_COLUMNS = new String[] { "_id", "title", localStringBuilder.toString(), "title_key" };
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("\"");
    localStringBuilder.append(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    localStringBuilder.append("\"");
    MEDIA_COLUMNS = new String[] { "_id", "title", localStringBuilder.toString(), "title_key" };
  }
  
  public RingtoneManager(Activity paramActivity)
  {
    this(paramActivity, false);
  }
  
  public RingtoneManager(Activity paramActivity, boolean paramBoolean)
  {
    mActivity = paramActivity;
    mContext = paramActivity;
    setType(mType);
    mIncludeParentRingtones = paramBoolean;
  }
  
  public RingtoneManager(Context paramContext)
  {
    this(paramContext, false);
  }
  
  public RingtoneManager(Context paramContext, boolean paramBoolean)
  {
    mActivity = null;
    mContext = paramContext;
    setType(mType);
    mIncludeParentRingtones = paramBoolean;
  }
  
  private static String constructBooleanTrueWhereClause(List<String> paramList)
  {
    if (paramList == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(");
    for (int i = paramList.size() - 1; i >= 0; i--) {
      if (i == paramList.size() - 1)
      {
        localStringBuilder.append((String)paramList.get(i));
        localStringBuilder.append(" and (");
      }
      else
      {
        localStringBuilder.append((String)paramList.get(i));
        localStringBuilder.append("=1 or ");
      }
    }
    if (paramList.size() > 0) {
      localStringBuilder.setLength(localStringBuilder.length() - 4);
    }
    localStringBuilder.append("))");
    return localStringBuilder.toString();
  }
  
  private static Context createPackageContextAsUser(Context paramContext, int paramInt)
  {
    try
    {
      paramContext = paramContext.createPackageContextAsUser(paramContext.getPackageName(), 0, UserHandle.of(paramInt));
      return paramContext;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      Log.e("RingtoneManager", "Unable to create package context", paramContext);
    }
    return null;
  }
  
  public static void disableSyncFromParent(Context paramContext)
  {
    IAudioService localIAudioService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    try
    {
      localIAudioService.disableRingtoneSync(paramContext.getUserId());
    }
    catch (RemoteException paramContext)
    {
      Log.e("RingtoneManager", "Unable to disable ringtone sync.");
    }
  }
  
  public static void enableSyncFromParent(Context paramContext)
  {
    Settings.Secure.putIntForUser(paramContext.getContentResolver(), "sync_parent_sounds", 1, paramContext.getUserId());
  }
  
  public static Uri getActualDefaultRingtoneUri(Context paramContext, int paramInt)
  {
    Object localObject = getSettingForType(paramInt);
    Uri localUri = null;
    if (localObject == null) {
      return null;
    }
    String str = Settings.System.getStringForUser(paramContext.getContentResolver(), (String)localObject, paramContext.getUserId());
    if (str != null) {
      localUri = Uri.parse(str);
    }
    localObject = localUri;
    if (localUri != null)
    {
      localObject = localUri;
      if (ContentProvider.getUserIdFromUri(localUri) == paramContext.getUserId()) {
        localObject = ContentProvider.getUriWithoutUserId(localUri);
      }
    }
    paramContext = new StringBuilder();
    paramContext.append("getActualDefaultRingtoneUri+uriString=");
    paramContext.append(str);
    paramContext.append(",type=");
    paramContext.append(paramInt);
    Log.d("RingtoneManager", paramContext.toString());
    return localObject;
  }
  
  /* Error */
  private static String getAudioTitle(Context paramContext, Uri paramUri)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +7 -> 8
    //   4: ldc_w 317
    //   7: areturn
    //   8: ldc_w 319
    //   11: astore_2
    //   12: aload_2
    //   13: astore_3
    //   14: aload_0
    //   15: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   18: aload_1
    //   19: aconst_null
    //   20: aconst_null
    //   21: aconst_null
    //   22: aconst_null
    //   23: invokevirtual 325	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   26: astore 4
    //   28: aconst_null
    //   29: astore_3
    //   30: aload_2
    //   31: astore_0
    //   32: aload 4
    //   34: ifnull +65 -> 99
    //   37: aload_3
    //   38: astore_1
    //   39: aload_2
    //   40: astore_0
    //   41: aload 4
    //   43: invokeinterface 331 1 0
    //   48: ifeq +51 -> 99
    //   51: aload_3
    //   52: astore_1
    //   53: aload 4
    //   55: aload 4
    //   57: ldc -123
    //   59: invokeinterface 335 2 0
    //   64: invokeinterface 338 2 0
    //   69: astore_0
    //   70: goto +29 -> 99
    //   73: astore_0
    //   74: goto +8 -> 82
    //   77: astore_0
    //   78: aload_0
    //   79: astore_1
    //   80: aload_0
    //   81: athrow
    //   82: aload 4
    //   84: ifnull +11 -> 95
    //   87: aload_2
    //   88: astore_3
    //   89: aload_1
    //   90: aload 4
    //   92: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   95: aload_2
    //   96: astore_3
    //   97: aload_0
    //   98: athrow
    //   99: aload 4
    //   101: ifnull +11 -> 112
    //   104: aload_0
    //   105: astore_3
    //   106: aconst_null
    //   107: aload 4
    //   109: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   112: goto +15 -> 127
    //   115: astore_0
    //   116: ldc 51
    //   118: ldc_w 342
    //   121: invokestatic 260	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   124: pop
    //   125: aload_3
    //   126: astore_0
    //   127: aload_0
    //   128: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	129	0	paramContext	Context
    //   0	129	1	paramUri	Uri
    //   11	85	2	str	String
    //   13	113	3	localObject	Object
    //   26	82	4	localCursor	Cursor
    // Exception table:
    //   from	to	target	type
    //   41	51	73	finally
    //   53	70	73	finally
    //   80	82	73	finally
    //   41	51	77	java/lang/Throwable
    //   53	70	77	java/lang/Throwable
    //   14	28	115	java/lang/SecurityException
    //   89	95	115	java/lang/SecurityException
    //   97	99	115	java/lang/SecurityException
    //   106	112	115	java/lang/SecurityException
  }
  
  public static Uri getCacheForType(int paramInt)
  {
    return getCacheForType(paramInt, UserHandle.getCallingUserId());
  }
  
  public static Uri getCacheForType(int paramInt1, int paramInt2)
  {
    if ((paramInt1 & 0x1) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.RINGTONE_CACHE_URI, paramInt2);
    }
    if ((paramInt1 & 0x2) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.NOTIFICATION_SOUND_CACHE_URI, paramInt2);
    }
    if ((paramInt1 & 0x4) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.ALARM_ALERT_CACHE_URI, paramInt2);
    }
    if ((paramInt1 & 0x100) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.RINGTONE_2_CACHE_URI, paramInt2);
    }
    if ((paramInt1 & 0x200) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.NOTIFICATION_SOUND_2_CACHE_URI, paramInt2);
    }
    if ((paramInt1 & 0x20) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.NEWMAIL_SOUND_CACHE_URI, paramInt2);
    }
    if ((paramInt1 & 0x40) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.SENTMAIL_SOUND_CACHE_URI, paramInt2);
    }
    if ((paramInt1 & 0x80) != 0) {
      return ContentProvider.maybeAddUserId(Settings.System.CALENDARALERT_SOUND_CACHE_URI, paramInt2);
    }
    return null;
  }
  
  public static int getDefaultType(Uri paramUri)
  {
    paramUri = ContentProvider.getUriWithoutUserId(paramUri);
    if (paramUri == null) {
      return -1;
    }
    if (paramUri.equals(Settings.System.DEFAULT_RINGTONE_URI)) {
      return 1;
    }
    if (paramUri.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
      return 2;
    }
    if (paramUri.equals(Settings.System.DEFAULT_RINGTONE_URI_2)) {
      return 256;
    }
    if (paramUri.equals(Settings.System.DEFAULT_NOTIFICATION_URI_2)) {
      return 512;
    }
    if (paramUri.equals(Settings.System.DEFAULT_ALARM_ALERT_URI)) {
      return 4;
    }
    if (paramUri.equals(Settings.System.DEFAULT_NEWMAIL_URI)) {
      return 32;
    }
    if (paramUri.equals(Settings.System.DEFAULT_SENTMAIL_URI)) {
      return 64;
    }
    if (paramUri.equals(Settings.System.DEFAULT_CALENDARALERT_URI)) {
      return 128;
    }
    return -1;
  }
  
  public static Uri getDefaultUri(int paramInt)
  {
    if ((paramInt & 0x1) != 0) {
      return Settings.System.DEFAULT_RINGTONE_URI;
    }
    if ((paramInt & 0x2) != 0) {
      return Settings.System.DEFAULT_NOTIFICATION_URI;
    }
    if ((paramInt & 0x4) != 0) {
      return Settings.System.DEFAULT_ALARM_ALERT_URI;
    }
    if ((paramInt & 0x100) != 0) {
      return Settings.System.DEFAULT_RINGTONE_URI_2;
    }
    if ((paramInt & 0x200) != 0) {
      return Settings.System.DEFAULT_NOTIFICATION_URI_2;
    }
    if ((paramInt & 0x20) != 0) {
      return Settings.System.DEFAULT_NEWMAIL_URI;
    }
    if ((paramInt & 0x40) != 0) {
      return Settings.System.DEFAULT_SENTMAIL_URI;
    }
    if ((paramInt & 0x80) != 0) {
      return Settings.System.DEFAULT_CALENDARALERT_URI;
    }
    return null;
  }
  
  /* Error */
  private static Uri getExistingRingtoneUriFromPath(Context paramContext, String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: getstatic 144	android/provider/MediaStore$Audio$Media:EXTERNAL_CONTENT_URI	Landroid/net/Uri;
    //   7: iconst_1
    //   8: anewarray 129	java/lang/String
    //   11: dup
    //   12: iconst_0
    //   13: ldc -125
    //   15: aastore
    //   16: ldc_w 412
    //   19: iconst_1
    //   20: anewarray 129	java/lang/String
    //   23: dup
    //   24: iconst_0
    //   25: aload_1
    //   26: aastore
    //   27: aconst_null
    //   28: invokevirtual 325	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   31: astore_2
    //   32: aconst_null
    //   33: astore_1
    //   34: aload_2
    //   35: ifnull +135 -> 170
    //   38: aload_1
    //   39: astore_0
    //   40: aload_2
    //   41: invokeinterface 331 1 0
    //   46: ifne +6 -> 52
    //   49: goto +121 -> 170
    //   52: aload_1
    //   53: astore_0
    //   54: aload_2
    //   55: aload_2
    //   56: ldc -125
    //   58: invokeinterface 335 2 0
    //   63: invokeinterface 416 2 0
    //   68: istore_3
    //   69: iload_3
    //   70: iconst_m1
    //   71: if_icmpne +14 -> 85
    //   74: aload_2
    //   75: ifnull +8 -> 83
    //   78: aconst_null
    //   79: aload_2
    //   80: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   83: aconst_null
    //   84: areturn
    //   85: aload_1
    //   86: astore_0
    //   87: getstatic 144	android/provider/MediaStore$Audio$Media:EXTERNAL_CONTENT_URI	Landroid/net/Uri;
    //   90: astore 4
    //   92: aload_1
    //   93: astore_0
    //   94: new 109	java/lang/StringBuilder
    //   97: astore 5
    //   99: aload_1
    //   100: astore_0
    //   101: aload 5
    //   103: invokespecial 112	java/lang/StringBuilder:<init>	()V
    //   106: aload_1
    //   107: astore_0
    //   108: aload 5
    //   110: ldc_w 319
    //   113: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: aload_1
    //   118: astore_0
    //   119: aload 5
    //   121: iload_3
    //   122: invokevirtual 308	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   125: pop
    //   126: aload_1
    //   127: astore_0
    //   128: aload 4
    //   130: aload 5
    //   132: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   135: invokestatic 420	android/net/Uri:withAppendedPath	(Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   138: astore_1
    //   139: aload_2
    //   140: ifnull +8 -> 148
    //   143: aconst_null
    //   144: aload_2
    //   145: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   148: aload_1
    //   149: areturn
    //   150: astore_1
    //   151: goto +8 -> 159
    //   154: astore_1
    //   155: aload_1
    //   156: astore_0
    //   157: aload_1
    //   158: athrow
    //   159: aload_2
    //   160: ifnull +8 -> 168
    //   163: aload_0
    //   164: aload_2
    //   165: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   168: aload_1
    //   169: athrow
    //   170: aload_2
    //   171: ifnull +8 -> 179
    //   174: aconst_null
    //   175: aload_2
    //   176: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   179: aconst_null
    //   180: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	181	0	paramContext	Context
    //   0	181	1	paramString	String
    //   31	145	2	localCursor	Cursor
    //   68	54	3	i	int
    //   90	39	4	localUri	Uri
    //   97	34	5	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   40	49	150	finally
    //   54	69	150	finally
    //   87	92	150	finally
    //   94	99	150	finally
    //   101	106	150	finally
    //   108	117	150	finally
    //   119	126	150	finally
    //   128	139	150	finally
    //   157	159	150	finally
    //   40	49	154	java/lang/Throwable
    //   54	69	154	java/lang/Throwable
    //   87	92	154	java/lang/Throwable
    //   94	99	154	java/lang/Throwable
    //   101	106	154	java/lang/Throwable
    //   108	117	154	java/lang/Throwable
    //   119	126	154	java/lang/Throwable
    //   128	139	154	java/lang/Throwable
  }
  
  private static final String getExternalDirectoryForType(int paramInt)
  {
    if (paramInt != 4)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported ringtone type: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 2: 
        return Environment.DIRECTORY_NOTIFICATIONS;
      }
      return Environment.DIRECTORY_RINGTONES;
    }
    return Environment.DIRECTORY_ALARMS;
  }
  
  private String[] getExternalStorage()
  {
    String[] arrayOfString1 = ((StorageManager)mContext.getSystemService("storage")).getVolumePaths();
    String[] arrayOfString2 = new String[arrayOfString1.length - 1];
    int i = 0;
    int j = 0;
    while (j < arrayOfString1.length)
    {
      int k = i;
      if (!arrayOfString1[j].equals(Environment.getExternalStorageDirectory().getPath().toString()))
      {
        arrayOfString2[i] = arrayOfString1[j];
        k = i + 1;
      }
      j++;
      i = k;
    }
    return arrayOfString2;
  }
  
  private Cursor getInternalRingtones()
  {
    return query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClause(mFilterColumns), null, "title_key");
  }
  
  private Cursor getMediaRingtones()
  {
    return getMediaRingtones(mContext);
  }
  
  private Cursor getMediaRingtones(Context paramContext)
  {
    int i = paramContext.checkPermission("android.permission.READ_EXTERNAL_STORAGE", Process.myPid(), Process.myUid());
    Object localObject = null;
    if (i != 0)
    {
      Log.w("RingtoneManager", "No READ_EXTERNAL_STORAGE permission, ignoring ringtones on ext storage");
      return null;
    }
    String str = Environment.getExternalStorageState();
    if ((!str.equals("mounted")) && (!str.equals("mounted_ro"))) {
      paramContext = localObject;
    } else {
      paramContext = query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MEDIA_COLUMNS, constructBooleanTrueWhereClause(mFilterColumns), null, "title_key", paramContext);
    }
    return paramContext;
  }
  
  private Cursor getParentProfileRingtones()
  {
    UserInfo localUserInfo = UserManager.get(mContext).getProfileParent(mContext.getUserId());
    if ((localUserInfo != null) && (id != mContext.getUserId()))
    {
      Context localContext = createPackageContextAsUser(mContext, id);
      if (localContext != null) {
        return new ExternalRingtonesCursorWrapper(getMediaRingtones(localContext), id);
      }
    }
    return null;
  }
  
  public static Ringtone getRingtone(Context paramContext, Uri paramUri)
  {
    return getRingtone(paramContext, paramUri, -1);
  }
  
  private static Ringtone getRingtone(Context paramContext, Uri paramUri, int paramInt)
  {
    try
    {
      localObject = new android/media/Ringtone;
      ((Ringtone)localObject).<init>(paramContext, true);
      if (paramInt >= 0) {
        ((Ringtone)localObject).setStreamType(paramInt);
      }
      ((Ringtone)localObject).setUri(paramUri);
      return localObject;
    }
    catch (Exception paramContext)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Failed to open ringtone ");
      ((StringBuilder)localObject).append(paramUri);
      ((StringBuilder)localObject).append(": ");
      ((StringBuilder)localObject).append(paramContext);
      Log.e("RingtoneManager", ((StringBuilder)localObject).toString());
    }
    return null;
  }
  
  /* Error */
  private File getRingtonePathFromUri(Uri paramUri)
  {
    // Byte code:
    //   0: aload_0
    //   1: bipush 7
    //   3: invokespecial 549	android/media/RingtoneManager:setFilterColumnsList	(I)V
    //   6: aconst_null
    //   7: astore_2
    //   8: aload_0
    //   9: getfield 158	android/media/RingtoneManager:mFilterColumns	Ljava/util/List;
    //   12: invokestatic 467	android/media/RingtoneManager:constructBooleanTrueWhereClause	(Ljava/util/List;)Ljava/lang/String;
    //   15: astore_3
    //   16: aload_0
    //   17: aload_1
    //   18: iconst_1
    //   19: anewarray 129	java/lang/String
    //   22: dup
    //   23: iconst_0
    //   24: ldc_w 551
    //   27: aastore
    //   28: aload_3
    //   29: aconst_null
    //   30: aconst_null
    //   31: invokespecial 468	android/media/RingtoneManager:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   34: astore 4
    //   36: aconst_null
    //   37: astore 5
    //   39: aconst_null
    //   40: astore 6
    //   42: aload_2
    //   43: astore_3
    //   44: aload 4
    //   46: ifnull +64 -> 110
    //   49: aload 6
    //   51: astore_1
    //   52: aload_2
    //   53: astore_3
    //   54: aload 4
    //   56: invokeinterface 331 1 0
    //   61: ifeq +49 -> 110
    //   64: aload 6
    //   66: astore_1
    //   67: aload 4
    //   69: aload 4
    //   71: ldc_w 551
    //   74: invokeinterface 335 2 0
    //   79: invokeinterface 338 2 0
    //   84: astore_3
    //   85: goto +25 -> 110
    //   88: astore_3
    //   89: goto +8 -> 97
    //   92: astore_3
    //   93: aload_3
    //   94: astore_1
    //   95: aload_3
    //   96: athrow
    //   97: aload 4
    //   99: ifnull +9 -> 108
    //   102: aload_1
    //   103: aload 4
    //   105: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   108: aload_3
    //   109: athrow
    //   110: aload 4
    //   112: ifnull +9 -> 121
    //   115: aconst_null
    //   116: aload 4
    //   118: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   121: aload 5
    //   123: astore_1
    //   124: aload_3
    //   125: ifnull +12 -> 137
    //   128: new 458	java/io/File
    //   131: dup
    //   132: aload_3
    //   133: invokespecial 552	java/io/File:<init>	(Ljava/lang/String;)V
    //   136: astore_1
    //   137: aload_1
    //   138: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	139	0	this	RingtoneManager
    //   0	139	1	paramUri	Uri
    //   7	46	2	localObject1	Object
    //   15	70	3	localObject2	Object
    //   88	1	3	localObject3	Object
    //   92	41	3	localThrowable	Throwable
    //   34	83	4	localCursor	Cursor
    //   37	85	5	localObject4	Object
    //   40	25	6	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   54	64	88	finally
    //   67	85	88	finally
    //   95	97	88	finally
    //   54	64	92	java/lang/Throwable
    //   67	85	92	java/lang/Throwable
  }
  
  private static String getSettingForType(int paramInt)
  {
    if ((paramInt & 0x1) != 0) {
      return "ringtone";
    }
    if ((paramInt & 0x2) != 0) {
      return "notification_sound";
    }
    if ((paramInt & 0x100) != 0) {
      return "ringtone_2";
    }
    if ((paramInt & 0x200) != 0) {
      return "notification_sound_2";
    }
    if ((paramInt & 0x4) != 0) {
      return "alarm_alert";
    }
    if ((paramInt & 0x20) != 0) {
      return "newmail_sound";
    }
    if ((paramInt & 0x40) != 0) {
      return "sentmail_sound";
    }
    if ((paramInt & 0x80) != 0) {
      return "calendaralert_sound";
    }
    return null;
  }
  
  public static String getSystemRingtoneTitle(Context paramContext, int paramInt)
  {
    Object localObject = getSettingForType(paramInt);
    if (TextUtils.isEmpty((CharSequence)localObject)) {
      return paramContext.getString(17040922);
    }
    HashMap localHashMap = loadRingtoneCacheTitle(paramContext, ContentProvider.maybeAddUserId(Settings.System.RINGTONES_TITLE_CACHE_URI, paramContext.getUserId()));
    String str = null;
    if (localHashMap.containsKey(localObject)) {
      str = (String)localHashMap.get(localObject);
    }
    localObject = str;
    if (!TextUtils.isEmpty(str))
    {
      localObject = str;
      if (str.equalsIgnoreCase("com.android.internal.R.string.ringtone_silent")) {
        localObject = paramContext.getString(17040921);
      }
    }
    paramContext = new StringBuilder();
    paramContext.append("[getSystemRingtoneTitle] title: ");
    paramContext.append((String)localObject);
    Log.d("RingtoneManager", paramContext.toString());
    return localObject;
  }
  
  private static Uri getUriFromCursor(Cursor paramCursor)
  {
    if ((paramCursor != null) && (!paramCursor.isClosed())) {
      try
      {
        paramCursor = ContentUris.withAppendedId(Uri.parse(paramCursor.getString(2)), paramCursor.getLong(0));
        return paramCursor;
      }
      catch (StaleDataException localStaleDataException)
      {
        paramCursor = new StringBuilder();
        paramCursor.append("getUriFromCursor return null, exception:\n");
        paramCursor.append(localStaleDataException);
        Log.d("RingtoneManager", paramCursor.toString());
        return null;
      }
    }
    Log.d("RingtoneManager", "getUriFromCursor return null");
    return null;
  }
  
  public static Uri getValidRingtoneUri(Context paramContext)
  {
    RingtoneManager localRingtoneManager = new RingtoneManager(paramContext);
    Uri localUri1 = getValidRingtoneUriFromCursorAndClose(paramContext, localRingtoneManager.getInternalRingtones());
    Uri localUri2 = localUri1;
    if (localUri1 == null) {
      localUri2 = getValidRingtoneUriFromCursorAndClose(paramContext, localRingtoneManager.getMediaRingtones());
    }
    return localUri2;
  }
  
  private static Uri getValidRingtoneUriFromCursorAndClose(Context paramContext, Cursor paramCursor)
  {
    if (paramCursor != null)
    {
      paramContext = null;
      if (paramCursor.moveToFirst()) {
        paramContext = getUriFromCursor(paramCursor);
      }
      paramCursor.close();
      return paramContext;
    }
    return null;
  }
  
  public static boolean isDefault(Uri paramUri)
  {
    boolean bool;
    if (getDefaultType(paramUri) != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isExternalRingtoneUri(Uri paramUri)
  {
    return isRingtoneUriInStorage(paramUri, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
  }
  
  /* Error */
  private static boolean isFileNeedToDisable(Context paramContext, Uri paramUri, String paramString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +5 -> 6
    //   4: iconst_0
    //   5: ireturn
    //   6: aload_2
    //   7: invokestatic 576	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   10: ifne +25 -> 35
    //   13: aload_2
    //   14: ldc_w 649
    //   17: invokevirtual 463	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   20: ifne +13 -> 33
    //   23: aload_2
    //   24: ldc_w 651
    //   27: invokevirtual 463	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   30: ifeq +5 -> 35
    //   33: iconst_1
    //   34: ireturn
    //   35: ldc_w 319
    //   38: astore_3
    //   39: ldc_w 319
    //   42: astore 4
    //   44: aload_0
    //   45: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   48: aload_1
    //   49: aconst_null
    //   50: aconst_null
    //   51: aconst_null
    //   52: aconst_null
    //   53: invokevirtual 325	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   56: astore 5
    //   58: aconst_null
    //   59: astore 6
    //   61: aload_3
    //   62: astore_1
    //   63: aload 4
    //   65: astore_2
    //   66: aload 5
    //   68: ifnull +88 -> 156
    //   71: aload 6
    //   73: astore_0
    //   74: aload_3
    //   75: astore_1
    //   76: aload 4
    //   78: astore_2
    //   79: aload 5
    //   81: invokeinterface 331 1 0
    //   86: ifeq +70 -> 156
    //   89: aload 6
    //   91: astore_0
    //   92: aload 5
    //   94: aload 5
    //   96: ldc_w 653
    //   99: invokeinterface 335 2 0
    //   104: invokeinterface 338 2 0
    //   109: astore_1
    //   110: aload 6
    //   112: astore_0
    //   113: aload 5
    //   115: aload 5
    //   117: ldc_w 655
    //   120: invokeinterface 335 2 0
    //   125: invokeinterface 338 2 0
    //   130: astore_2
    //   131: goto +25 -> 156
    //   134: astore_1
    //   135: goto +8 -> 143
    //   138: astore_1
    //   139: aload_1
    //   140: astore_0
    //   141: aload_1
    //   142: athrow
    //   143: aload 5
    //   145: ifnull +9 -> 154
    //   148: aload_0
    //   149: aload 5
    //   151: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   154: aload_1
    //   155: athrow
    //   156: aload 5
    //   158: ifnull +9 -> 167
    //   161: aconst_null
    //   162: aload 5
    //   164: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   167: aload_2
    //   168: invokestatic 576	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   171: ifne +25 -> 196
    //   174: aload_2
    //   175: ldc_w 649
    //   178: invokevirtual 463	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   181: ifne +13 -> 194
    //   184: aload_2
    //   185: ldc_w 651
    //   188: invokevirtual 463	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   191: ifeq +5 -> 196
    //   194: iconst_1
    //   195: ireturn
    //   196: aload_1
    //   197: invokestatic 576	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   200: ifne +25 -> 225
    //   203: aload_1
    //   204: ldc_w 657
    //   207: invokevirtual 660	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   210: ifne +13 -> 223
    //   213: aload_1
    //   214: ldc_w 662
    //   217: invokevirtual 660	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   220: ifeq +5 -> 225
    //   223: iconst_1
    //   224: ireturn
    //   225: iconst_0
    //   226: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	227	0	paramContext	Context
    //   0	227	1	paramUri	Uri
    //   0	227	2	paramString	String
    //   38	37	3	str1	String
    //   42	35	4	str2	String
    //   56	107	5	localCursor	Cursor
    //   59	52	6	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   79	89	134	finally
    //   92	110	134	finally
    //   113	131	134	finally
    //   141	143	134	finally
    //   79	89	138	java/lang/Throwable
    //   92	110	138	java/lang/Throwable
    //   113	131	138	java/lang/Throwable
  }
  
  private static boolean isInternalRingtoneUri(Uri paramUri)
  {
    return isRingtoneUriInStorage(paramUri, MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
  }
  
  private static boolean isRingtoneUriInStorage(Uri paramUri1, Uri paramUri2)
  {
    paramUri1 = ContentProvider.getUriWithoutUserId(paramUri1);
    boolean bool;
    if (paramUri1 == null) {
      bool = false;
    } else {
      bool = paramUri1.toString().startsWith(paramUri2.toString());
    }
    return bool;
  }
  
  /* Error */
  private static HashMap<String, String> loadRingtoneCacheTitle(Context paramContext, Uri paramUri)
  {
    // Byte code:
    //   0: new 587	java/util/HashMap
    //   3: dup
    //   4: invokespecial 672	java/util/HashMap:<init>	()V
    //   7: astore_2
    //   8: aload_0
    //   9: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   12: astore_0
    //   13: aload_0
    //   14: aload_1
    //   15: invokevirtual 676	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   18: astore_3
    //   19: aconst_null
    //   20: astore 4
    //   22: aload 4
    //   24: astore_0
    //   25: new 678	java/io/BufferedReader
    //   28: astore 5
    //   30: aload 4
    //   32: astore_0
    //   33: new 680	java/io/InputStreamReader
    //   36: astore_1
    //   37: aload 4
    //   39: astore_0
    //   40: aload_1
    //   41: aload_3
    //   42: invokespecial 683	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   45: aload 4
    //   47: astore_0
    //   48: aload 5
    //   50: aload_1
    //   51: invokespecial 686	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   54: aload 5
    //   56: invokevirtual 689	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   59: astore_0
    //   60: aload_0
    //   61: ifnull +38 -> 99
    //   64: aload_0
    //   65: invokestatic 576	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   68: ifne -14 -> 54
    //   71: aload_0
    //   72: ldc_w 691
    //   75: invokevirtual 695	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   78: astore_0
    //   79: aload_0
    //   80: arraylength
    //   81: iconst_2
    //   82: if_icmplt +14 -> 96
    //   85: aload_2
    //   86: aload_0
    //   87: iconst_0
    //   88: aaload
    //   89: aload_0
    //   90: iconst_1
    //   91: aaload
    //   92: invokevirtual 699	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   95: pop
    //   96: goto -42 -> 54
    //   99: aload 4
    //   101: astore_0
    //   102: aconst_null
    //   103: aload 5
    //   105: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   108: aload_3
    //   109: ifnull +81 -> 190
    //   112: aconst_null
    //   113: aload_3
    //   114: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   117: goto +73 -> 190
    //   120: astore 6
    //   122: aconst_null
    //   123: astore_1
    //   124: goto +8 -> 132
    //   127: astore_1
    //   128: aload_1
    //   129: athrow
    //   130: astore 6
    //   132: aload 4
    //   134: astore_0
    //   135: aload_1
    //   136: aload 5
    //   138: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   141: aload 4
    //   143: astore_0
    //   144: aload 6
    //   146: athrow
    //   147: astore_1
    //   148: goto +8 -> 156
    //   151: astore_1
    //   152: aload_1
    //   153: astore_0
    //   154: aload_1
    //   155: athrow
    //   156: aload_3
    //   157: ifnull +8 -> 165
    //   160: aload_0
    //   161: aload_3
    //   162: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   165: aload_1
    //   166: athrow
    //   167: astore_0
    //   168: ldc 51
    //   170: ldc_w 701
    //   173: invokestatic 260	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   176: pop
    //   177: goto +13 -> 190
    //   180: astore_0
    //   181: ldc 51
    //   183: ldc_w 703
    //   186: invokestatic 260	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   189: pop
    //   190: aload_2
    //   191: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	192	0	paramContext	Context
    //   0	192	1	paramUri	Uri
    //   7	184	2	localHashMap	HashMap
    //   18	144	3	localInputStream	InputStream
    //   20	122	4	localObject1	Object
    //   28	109	5	localBufferedReader	java.io.BufferedReader
    //   120	1	6	localObject2	Object
    //   130	15	6	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   54	60	120	finally
    //   64	96	120	finally
    //   54	60	127	java/lang/Throwable
    //   64	96	127	java/lang/Throwable
    //   128	130	130	finally
    //   25	30	147	finally
    //   33	37	147	finally
    //   40	45	147	finally
    //   48	54	147	finally
    //   102	108	147	finally
    //   135	141	147	finally
    //   144	147	147	finally
    //   154	156	147	finally
    //   25	30	151	java/lang/Throwable
    //   33	37	151	java/lang/Throwable
    //   40	45	151	java/lang/Throwable
    //   48	54	151	java/lang/Throwable
    //   102	108	151	java/lang/Throwable
    //   135	141	151	java/lang/Throwable
    //   144	147	151	java/lang/Throwable
    //   13	19	167	java/io/IOException
    //   112	117	167	java/io/IOException
    //   160	165	167	java/io/IOException
    //   165	167	167	java/io/IOException
    //   13	19	180	java/io/FileNotFoundException
    //   112	117	180	java/io/FileNotFoundException
    //   160	165	180	java/io/FileNotFoundException
    //   165	167	180	java/io/FileNotFoundException
  }
  
  private static InputStream openRingtone(Context paramContext, Uri paramUri)
    throws IOException
  {
    Object localObject = paramContext.getContentResolver();
    try
    {
      localObject = ((ContentResolver)localObject).openInputStream(paramUri);
      return localObject;
    }
    catch (SecurityException|IOException localSecurityException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Failed to open directly; attempting failover: ");
      ((StringBuilder)localObject).append(localSecurityException);
      Log.w("RingtoneManager", ((StringBuilder)localObject).toString());
      paramContext = ((AudioManager)paramContext.getSystemService(AudioManager.class)).getRingtonePlayer();
      try
      {
        paramContext = new ParcelFileDescriptor.AutoCloseInputStream(paramContext.openRingtone(paramUri));
        return paramContext;
      }
      catch (Exception paramContext)
      {
        throw new IOException(paramContext);
      }
    }
  }
  
  private Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    return query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, mContext);
  }
  
  private Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, Context paramContext)
  {
    if (mActivity != null) {
      return mActivity.managedQuery(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    }
    return paramContext.getContentResolver().query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
  }
  
  /* Error */
  private static void saveRingtoneCacheTitle(Context paramContext, Uri paramUri, int paramInt)
  {
    // Byte code:
    //   0: iload_2
    //   1: invokestatic 279	android/media/RingtoneManager:getSettingForType	(I)Ljava/lang/String;
    //   4: astore_3
    //   5: aload_0
    //   6: aload_1
    //   7: invokestatic 739	android/media/RingtoneManager:getAudioTitle	(Landroid/content/Context;Landroid/net/Uri;)Ljava/lang/String;
    //   10: astore 4
    //   12: new 109	java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial 112	java/lang/StringBuilder:<init>	()V
    //   19: astore_1
    //   20: aload_1
    //   21: ldc_w 741
    //   24: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload_1
    //   29: aload 4
    //   31: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: pop
    //   35: aload_1
    //   36: ldc_w 743
    //   39: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: pop
    //   43: aload_1
    //   44: aload_3
    //   45: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: pop
    //   49: aload_1
    //   50: ldc_w 745
    //   53: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: pop
    //   57: aload_1
    //   58: iload_2
    //   59: invokevirtual 308	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: ldc 51
    //   65: aload_1
    //   66: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   69: invokestatic 311	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   72: pop
    //   73: aload_0
    //   74: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   77: astore 5
    //   79: getstatic 581	android/provider/Settings$System:RINGTONES_TITLE_CACHE_URI	Landroid/net/Uri;
    //   82: aload_0
    //   83: invokevirtual 250	android/content/Context:getUserId	()I
    //   86: invokestatic 357	android/content/ContentProvider:maybeAddUserId	(Landroid/net/Uri;I)Landroid/net/Uri;
    //   89: astore 6
    //   91: aload_0
    //   92: aload 6
    //   94: invokestatic 585	android/media/RingtoneManager:loadRingtoneCacheTitle	(Landroid/content/Context;Landroid/net/Uri;)Ljava/util/HashMap;
    //   97: astore_1
    //   98: aload_1
    //   99: aload_3
    //   100: aload 4
    //   102: invokevirtual 699	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   105: pop
    //   106: aload 5
    //   108: aload 6
    //   110: invokevirtual 749	android/content/ContentResolver:openOutputStream	(Landroid/net/Uri;)Ljava/io/OutputStream;
    //   113: astore 5
    //   115: aconst_null
    //   116: astore 4
    //   118: aload 4
    //   120: astore_0
    //   121: new 751	java/io/OutputStreamWriter
    //   124: astore 6
    //   126: aload 4
    //   128: astore_0
    //   129: aload 6
    //   131: aload 5
    //   133: invokespecial 754	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;)V
    //   136: aload_1
    //   137: invokevirtual 758	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   140: invokeinterface 764 1 0
    //   145: astore_0
    //   146: aload_0
    //   147: invokeinterface 769 1 0
    //   152: ifeq +71 -> 223
    //   155: aload_0
    //   156: invokeinterface 773 1 0
    //   161: checkcast 775	java/util/Map$Entry
    //   164: astore_3
    //   165: new 109	java/lang/StringBuilder
    //   168: astore_1
    //   169: aload_1
    //   170: invokespecial 112	java/lang/StringBuilder:<init>	()V
    //   173: aload_1
    //   174: aload_3
    //   175: invokeinterface 778 1 0
    //   180: invokevirtual 127	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   183: pop
    //   184: aload_1
    //   185: ldc_w 691
    //   188: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: pop
    //   192: aload_1
    //   193: aload_3
    //   194: invokeinterface 781 1 0
    //   199: invokevirtual 127	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   202: pop
    //   203: aload_1
    //   204: ldc_w 783
    //   207: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: aload 6
    //   213: aload_1
    //   214: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   217: invokevirtual 786	java/io/OutputStreamWriter:write	(Ljava/lang/String;)V
    //   220: goto -74 -> 146
    //   223: aload 4
    //   225: astore_0
    //   226: aconst_null
    //   227: aload 6
    //   229: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   232: aload 5
    //   234: ifnull +9 -> 243
    //   237: aconst_null
    //   238: aload 5
    //   240: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   243: goto +82 -> 325
    //   246: astore_1
    //   247: aconst_null
    //   248: astore_3
    //   249: goto +7 -> 256
    //   252: astore_3
    //   253: aload_3
    //   254: athrow
    //   255: astore_1
    //   256: aload 4
    //   258: astore_0
    //   259: aload_3
    //   260: aload 6
    //   262: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   265: aload 4
    //   267: astore_0
    //   268: aload_1
    //   269: athrow
    //   270: astore_1
    //   271: goto +8 -> 279
    //   274: astore_1
    //   275: aload_1
    //   276: astore_0
    //   277: aload_1
    //   278: athrow
    //   279: aload 5
    //   281: ifnull +9 -> 290
    //   284: aload_0
    //   285: aload 5
    //   287: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   290: aload_1
    //   291: athrow
    //   292: astore_1
    //   293: new 109	java/lang/StringBuilder
    //   296: dup
    //   297: invokespecial 112	java/lang/StringBuilder:<init>	()V
    //   300: astore_0
    //   301: aload_0
    //   302: ldc_w 788
    //   305: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   308: pop
    //   309: aload_0
    //   310: aload_1
    //   311: invokevirtual 127	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   314: pop
    //   315: ldc 51
    //   317: aload_0
    //   318: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   321: invokestatic 491	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   324: pop
    //   325: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	326	0	paramContext	Context
    //   0	326	1	paramUri	Uri
    //   0	326	2	paramInt	int
    //   4	245	3	localObject1	Object
    //   252	8	3	localThrowable	Throwable
    //   10	256	4	str	String
    //   77	209	5	localObject2	Object
    //   89	172	6	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   136	146	246	finally
    //   146	220	246	finally
    //   136	146	252	java/lang/Throwable
    //   146	220	252	java/lang/Throwable
    //   253	255	255	finally
    //   121	126	270	finally
    //   129	136	270	finally
    //   226	232	270	finally
    //   259	265	270	finally
    //   268	270	270	finally
    //   277	279	270	finally
    //   121	126	274	java/lang/Throwable
    //   129	136	274	java/lang/Throwable
    //   226	232	274	java/lang/Throwable
    //   259	265	274	java/lang/Throwable
    //   268	270	274	java/lang/Throwable
    //   106	115	292	java/io/IOException
    //   237	243	292	java/io/IOException
    //   284	290	292	java/io/IOException
    //   290	292	292	java/io/IOException
  }
  
  public static void setActualDefaultRingtoneUri(Context paramContext, int paramInt, Uri paramUri)
  {
    setActualDefaultRingtoneUri(paramContext, paramInt, paramUri, false);
  }
  
  /* Error */
  public static void setActualDefaultRingtoneUri(Context paramContext, int paramInt, Uri paramUri, boolean paramBoolean)
  {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic 279	android/media/RingtoneManager:getSettingForType	(I)Ljava/lang/String;
    //   4: astore 4
    //   6: aload 4
    //   8: ifnonnull +4 -> 12
    //   11: return
    //   12: aload_0
    //   13: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   16: astore 5
    //   18: iload_3
    //   19: ifne +44 -> 63
    //   22: aload_2
    //   23: ifnull +40 -> 63
    //   26: aload_0
    //   27: aload_2
    //   28: aload 5
    //   30: aload_2
    //   31: invokevirtual 797	android/content/ContentResolver:getType	(Landroid/net/Uri;)Ljava/lang/String;
    //   34: invokestatic 799	android/media/RingtoneManager:isFileNeedToDisable	(Landroid/content/Context;Landroid/net/Uri;Ljava/lang/String;)Z
    //   37: ifne +6 -> 43
    //   40: goto +23 -> 63
    //   43: ldc 51
    //   45: ldc_w 801
    //   48: invokestatic 260	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   51: pop
    //   52: new 425	java/lang/IllegalArgumentException
    //   55: dup
    //   56: ldc_w 803
    //   59: invokespecial 428	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   62: athrow
    //   63: new 109	java/lang/StringBuilder
    //   66: dup
    //   67: invokespecial 112	java/lang/StringBuilder:<init>	()V
    //   70: astore 6
    //   72: aload 6
    //   74: ldc_w 805
    //   77: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: aload 6
    //   83: aload_2
    //   84: invokevirtual 127	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: aload 6
    //   90: ldc_w 305
    //   93: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: aload 6
    //   99: iload_1
    //   100: invokevirtual 308	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload 6
    //   106: ldc_w 807
    //   109: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload 6
    //   115: iload_3
    //   116: invokevirtual 810	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: ldc 51
    //   122: aload 6
    //   124: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   127: invokestatic 311	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   130: pop
    //   131: aload 5
    //   133: ldc_w 267
    //   136: iconst_0
    //   137: aload_0
    //   138: invokevirtual 250	android/content/Context:getUserId	()I
    //   141: invokestatic 814	android/provider/Settings$Secure:getIntForUser	(Landroid/content/ContentResolver;Ljava/lang/String;II)I
    //   144: iconst_1
    //   145: if_icmpne +11 -> 156
    //   148: iload_3
    //   149: ifne +7 -> 156
    //   152: aload_0
    //   153: invokestatic 816	android/media/RingtoneManager:disableSyncFromParent	(Landroid/content/Context;)V
    //   156: aload_2
    //   157: astore 6
    //   159: aload_2
    //   160: invokestatic 818	android/media/RingtoneManager:isInternalRingtoneUri	(Landroid/net/Uri;)Z
    //   163: ifne +13 -> 176
    //   166: aload_2
    //   167: aload_0
    //   168: invokevirtual 250	android/content/Context:getUserId	()I
    //   171: invokestatic 357	android/content/ContentProvider:maybeAddUserId	(Landroid/net/Uri;I)Landroid/net/Uri;
    //   174: astore 6
    //   176: aconst_null
    //   177: astore 7
    //   179: aload 6
    //   181: ifnull +12 -> 193
    //   184: aload 6
    //   186: invokevirtual 664	android/net/Uri:toString	()Ljava/lang/String;
    //   189: astore_2
    //   190: goto +5 -> 195
    //   193: aconst_null
    //   194: astore_2
    //   195: aload 5
    //   197: aload 4
    //   199: aload_2
    //   200: aload_0
    //   201: invokevirtual 250	android/content/Context:getUserId	()I
    //   204: invokestatic 822	android/provider/Settings$System:putStringForUser	(Landroid/content/ContentResolver;Ljava/lang/String;Ljava/lang/String;I)Z
    //   207: pop
    //   208: aload 6
    //   210: ifnull +169 -> 379
    //   213: iload_1
    //   214: aload_0
    //   215: invokevirtual 250	android/content/Context:getUserId	()I
    //   218: invokestatic 350	android/media/RingtoneManager:getCacheForType	(II)Landroid/net/Uri;
    //   221: astore 4
    //   223: aload_0
    //   224: aload 6
    //   226: invokestatic 824	android/media/RingtoneManager:openRingtone	(Landroid/content/Context;Landroid/net/Uri;)Ljava/io/InputStream;
    //   229: astore 8
    //   231: aload 7
    //   233: astore_2
    //   234: aload 5
    //   236: aload 4
    //   238: invokevirtual 749	android/content/ContentResolver:openOutputStream	(Landroid/net/Uri;)Ljava/io/OutputStream;
    //   241: astore 9
    //   243: aload 8
    //   245: aload 9
    //   247: invokestatic 830	android/os/FileUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)J
    //   250: pop2
    //   251: aload 9
    //   253: ifnull +12 -> 265
    //   256: aload 7
    //   258: astore_2
    //   259: aconst_null
    //   260: aload 9
    //   262: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   265: aload 8
    //   267: ifnull +9 -> 276
    //   270: aconst_null
    //   271: aload 8
    //   273: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   276: goto +103 -> 379
    //   279: astore 4
    //   281: aconst_null
    //   282: astore 5
    //   284: goto +10 -> 294
    //   287: astore 5
    //   289: aload 5
    //   291: athrow
    //   292: astore 4
    //   294: aload 9
    //   296: ifnull +13 -> 309
    //   299: aload 7
    //   301: astore_2
    //   302: aload 5
    //   304: aload 9
    //   306: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   309: aload 7
    //   311: astore_2
    //   312: aload 4
    //   314: athrow
    //   315: astore 5
    //   317: goto +11 -> 328
    //   320: astore 5
    //   322: aload 5
    //   324: astore_2
    //   325: aload 5
    //   327: athrow
    //   328: aload 8
    //   330: ifnull +9 -> 339
    //   333: aload_2
    //   334: aload 8
    //   336: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   339: aload 5
    //   341: athrow
    //   342: astore_2
    //   343: new 109	java/lang/StringBuilder
    //   346: dup
    //   347: invokespecial 112	java/lang/StringBuilder:<init>	()V
    //   350: astore 5
    //   352: aload 5
    //   354: ldc_w 788
    //   357: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   360: pop
    //   361: aload 5
    //   363: aload_2
    //   364: invokevirtual 127	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   367: pop
    //   368: ldc 51
    //   370: aload 5
    //   372: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   375: invokestatic 491	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   378: pop
    //   379: aload_0
    //   380: aload 6
    //   382: iload_1
    //   383: invokestatic 832	android/media/RingtoneManager:saveRingtoneCacheTitle	(Landroid/content/Context;Landroid/net/Uri;I)V
    //   386: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	387	0	paramContext	Context
    //   0	387	1	paramInt	int
    //   0	387	2	paramUri	Uri
    //   0	387	3	paramBoolean	boolean
    //   4	233	4	localObject1	Object
    //   279	1	4	localObject2	Object
    //   292	21	4	localObject3	Object
    //   16	267	5	localContentResolver	ContentResolver
    //   287	16	5	localThrowable1	Throwable
    //   315	1	5	localObject4	Object
    //   320	20	5	localThrowable2	Throwable
    //   350	21	5	localStringBuilder	StringBuilder
    //   70	311	6	localObject5	Object
    //   177	133	7	localObject6	Object
    //   229	106	8	localInputStream	InputStream
    //   241	64	9	localOutputStream	java.io.OutputStream
    // Exception table:
    //   from	to	target	type
    //   243	251	279	finally
    //   243	251	287	java/lang/Throwable
    //   289	292	292	finally
    //   234	243	315	finally
    //   259	265	315	finally
    //   302	309	315	finally
    //   312	315	315	finally
    //   325	328	315	finally
    //   234	243	320	java/lang/Throwable
    //   259	265	320	java/lang/Throwable
    //   302	309	320	java/lang/Throwable
    //   312	315	320	java/lang/Throwable
    //   223	231	342	java/io/IOException
    //   270	276	342	java/io/IOException
    //   333	339	342	java/io/IOException
    //   339	342	342	java/io/IOException
  }
  
  private void setFilterColumnsList(int paramInt)
  {
    List localList = mFilterColumns;
    localList.clear();
    if (((paramInt & 0x1) != 0) || ((paramInt & 0x100) != 0)) {
      localList.add("is_ringtone");
    }
    if (((paramInt & 0x2) != 0) || ((paramInt & 0x200) != 0) || ((paramInt & 0x20) != 0) || ((paramInt & 0x40) != 0) || ((paramInt & 0x80) != 0)) {
      localList.add("is_notification");
    }
    if ((paramInt & 0x4) != 0) {
      localList.add("is_alarm");
    }
    if ((paramInt & 0x8) != 0) {
      localList.add("is_music");
    }
    if (SystemProperties.get("ro.product.device", "").equals("ASUS_Z01M_1"))
    {
      Log.i("RingtoneManager", ".ape files");
      localList.add("is_music is not null");
    }
    else
    {
      Log.i("RingtoneManager", "no .ape files");
      localList.add("(is_music is not null or is_ringtone is not null)  and _display_name not like '%.ape%' and mime_type is not 'audio/x-ape' and _display_name not like '%.m4a%' and mime_type is not 'audio/mp4'");
    }
  }
  
  /* Error */
  public Uri addCustomExternalRingtone(Uri paramUri, int paramInt)
    throws java.io.FileNotFoundException, IllegalArgumentException, IOException
  {
    // Byte code:
    //   0: invokestatic 494	android/os/Environment:getExternalStorageState	()Ljava/lang/String;
    //   3: ldc_w 496
    //   6: invokevirtual 463	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   9: ifeq +301 -> 310
    //   12: aload_0
    //   13: getfield 164	android/media/RingtoneManager:mContext	Landroid/content/Context;
    //   16: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   19: aload_1
    //   20: invokevirtual 797	android/content/ContentResolver:getType	(Landroid/net/Uri;)Ljava/lang/String;
    //   23: astore_3
    //   24: aload_3
    //   25: ifnull +244 -> 269
    //   28: aload_3
    //   29: ldc_w 871
    //   32: invokevirtual 667	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   35: ifne +13 -> 48
    //   38: aload_3
    //   39: ldc_w 873
    //   42: invokevirtual 463	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   45: ifeq +224 -> 269
    //   48: aload_0
    //   49: getfield 164	android/media/RingtoneManager:mContext	Landroid/content/Context;
    //   52: aload_1
    //   53: aload_3
    //   54: invokestatic 799	android/media/RingtoneManager:isFileNeedToDisable	(Landroid/content/Context;Landroid/net/Uri;Ljava/lang/String;)Z
    //   57: ifne +201 -> 258
    //   60: iload_2
    //   61: invokestatic 875	android/media/RingtoneManager:getExternalDirectoryForType	(I)Ljava/lang/String;
    //   64: astore 4
    //   66: aload_0
    //   67: getfield 164	android/media/RingtoneManager:mContext	Landroid/content/Context;
    //   70: aload 4
    //   72: aload_0
    //   73: getfield 164	android/media/RingtoneManager:mContext	Landroid/content/Context;
    //   76: aload_1
    //   77: invokestatic 880	android/media/Utils:getFileDisplayNameFromUri	(Landroid/content/Context;Landroid/net/Uri;)Ljava/lang/String;
    //   80: aload_3
    //   81: invokestatic 884	android/media/Utils:getUniqueExternalFile	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;
    //   84: astore 5
    //   86: aload_0
    //   87: getfield 164	android/media/RingtoneManager:mContext	Landroid/content/Context;
    //   90: invokevirtual 265	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   93: aload_1
    //   94: invokevirtual 676	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   97: astore 6
    //   99: aconst_null
    //   100: astore 7
    //   102: aconst_null
    //   103: astore_3
    //   104: aload 7
    //   106: astore_1
    //   107: new 886	java/io/FileOutputStream
    //   110: astore 8
    //   112: aload 7
    //   114: astore_1
    //   115: aload 8
    //   117: aload 5
    //   119: invokespecial 889	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   122: aload 6
    //   124: aload 8
    //   126: invokestatic 830	android/os/FileUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)J
    //   129: pop2
    //   130: aload 7
    //   132: astore_1
    //   133: aconst_null
    //   134: aload 8
    //   136: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   139: aload 6
    //   141: ifnull +9 -> 150
    //   144: aconst_null
    //   145: aload 6
    //   147: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   150: new 6	android/media/RingtoneManager$NewRingtoneScanner
    //   153: astore 4
    //   155: aload 4
    //   157: aload_0
    //   158: aload 5
    //   160: invokespecial 892	android/media/RingtoneManager$NewRingtoneScanner:<init>	(Landroid/media/RingtoneManager;Ljava/io/File;)V
    //   163: aload_3
    //   164: astore_1
    //   165: aload 4
    //   167: invokevirtual 896	android/media/RingtoneManager$NewRingtoneScanner:take	()Landroid/net/Uri;
    //   170: astore_3
    //   171: aconst_null
    //   172: aload 4
    //   174: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   177: aload_3
    //   178: areturn
    //   179: astore_3
    //   180: goto +8 -> 188
    //   183: astore_3
    //   184: aload_3
    //   185: astore_1
    //   186: aload_3
    //   187: athrow
    //   188: aload_1
    //   189: aload 4
    //   191: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   194: aload_3
    //   195: athrow
    //   196: astore_1
    //   197: new 671	java/io/IOException
    //   200: dup
    //   201: ldc_w 898
    //   204: aload_1
    //   205: invokespecial 901	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   208: athrow
    //   209: astore 4
    //   211: aconst_null
    //   212: astore_3
    //   213: goto +8 -> 221
    //   216: astore_3
    //   217: aload_3
    //   218: athrow
    //   219: astore 4
    //   221: aload 7
    //   223: astore_1
    //   224: aload_3
    //   225: aload 8
    //   227: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   230: aload 7
    //   232: astore_1
    //   233: aload 4
    //   235: athrow
    //   236: astore_3
    //   237: goto +8 -> 245
    //   240: astore_3
    //   241: aload_3
    //   242: astore_1
    //   243: aload_3
    //   244: athrow
    //   245: aload 6
    //   247: ifnull +9 -> 256
    //   250: aload_1
    //   251: aload 6
    //   253: invokestatic 340	android/media/RingtoneManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   256: aload_3
    //   257: athrow
    //   258: new 425	java/lang/IllegalArgumentException
    //   261: dup
    //   262: ldc_w 903
    //   265: invokespecial 428	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   268: athrow
    //   269: new 109	java/lang/StringBuilder
    //   272: dup
    //   273: invokespecial 112	java/lang/StringBuilder:<init>	()V
    //   276: astore_1
    //   277: aload_1
    //   278: ldc_w 905
    //   281: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   284: pop
    //   285: aload_1
    //   286: aload_3
    //   287: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   290: pop
    //   291: aload_1
    //   292: ldc 114
    //   294: invokevirtual 118	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   297: pop
    //   298: new 425	java/lang/IllegalArgumentException
    //   301: dup
    //   302: aload_1
    //   303: invokevirtual 137	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   306: invokespecial 428	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   309: athrow
    //   310: new 671	java/io/IOException
    //   313: dup
    //   314: ldc_w 907
    //   317: invokespecial 908	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   320: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	321	0	this	RingtoneManager
    //   0	321	1	paramUri	Uri
    //   0	321	2	paramInt	int
    //   23	155	3	localObject1	Object
    //   179	1	3	localObject2	Object
    //   183	12	3	localThrowable1	Throwable
    //   212	1	3	localObject3	Object
    //   216	9	3	localThrowable2	Throwable
    //   236	1	3	localObject4	Object
    //   240	47	3	localThrowable3	Throwable
    //   64	126	4	localObject5	Object
    //   209	1	4	localObject6	Object
    //   219	15	4	localObject7	Object
    //   84	75	5	localFile	File
    //   97	155	6	localInputStream	InputStream
    //   100	131	7	localObject8	Object
    //   110	116	8	localFileOutputStream	java.io.FileOutputStream
    // Exception table:
    //   from	to	target	type
    //   165	171	179	finally
    //   186	188	179	finally
    //   165	171	183	java/lang/Throwable
    //   150	163	196	java/lang/InterruptedException
    //   171	177	196	java/lang/InterruptedException
    //   188	196	196	java/lang/InterruptedException
    //   122	130	209	finally
    //   122	130	216	java/lang/Throwable
    //   217	219	219	finally
    //   107	112	236	finally
    //   115	122	236	finally
    //   133	139	236	finally
    //   224	230	236	finally
    //   233	236	236	finally
    //   243	245	236	finally
    //   107	112	240	java/lang/Throwable
    //   115	122	240	java/lang/Throwable
    //   133	139	240	java/lang/Throwable
    //   224	230	240	java/lang/Throwable
    //   233	236	240	java/lang/Throwable
  }
  
  public boolean deleteExternalRingtone(Uri paramUri)
  {
    if (!isCustomRingtone(paramUri)) {
      return false;
    }
    File localFile = getRingtonePathFromUri(paramUri);
    if (localFile != null) {
      try
      {
        if (mContext.getContentResolver().delete(paramUri, null, null) > 0)
        {
          boolean bool = localFile.delete();
          return bool;
        }
      }
      catch (SecurityException paramUri)
      {
        Log.d("RingtoneManager", "Unable to delete custom ringtone", paramUri);
      }
    }
    return false;
  }
  
  public Uri getActualRingtoneUri()
  {
    Object localObject = Settings.System.getString(mContext.getContentResolver(), "ringtone");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getActualRingtoneUri:system current ringtone uri is ");
    localStringBuilder.append((String)localObject);
    Log.d("RingtoneManager-debug", localStringBuilder.toString());
    if (localObject != null) {
      localObject = Uri.parse((String)localObject);
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public Cursor getCursor()
  {
    if ((mCursor != null) && (!mCursor.isClosed())) {
      return mCursor;
    }
    Object localObject = new ArrayList();
    ((ArrayList)localObject).add(getInternalRingtones());
    ((ArrayList)localObject).add(getMediaRingtones());
    if (mIncludeParentRingtones)
    {
      Cursor localCursor = getParentProfileRingtones();
      if (localCursor != null) {
        ((ArrayList)localObject).add(localCursor);
      }
    }
    localObject = new SortCursor((Cursor[])((ArrayList)localObject).toArray(new Cursor[((ArrayList)localObject).size()]), "title_key");
    mCursor = ((Cursor)localObject);
    return localObject;
  }
  
  public Uri getDefaultRingtoneUri()
  {
    Object localObject = Settings.System.getString(mContext.getContentResolver(), "default_ringtone");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getDefaultRingtoneUri:system current ringtone uri is ");
    localStringBuilder.append((String)localObject);
    Log.d("RingtoneManager-debug", localStringBuilder.toString());
    if (localObject != null) {
      localObject = Uri.parse((String)localObject);
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  @Deprecated
  public boolean getIncludeDrm()
  {
    return false;
  }
  
  public Ringtone getRingtone(int paramInt)
  {
    if ((mStopPreviousRingtone) && (mPreviousRingtone != null)) {
      mPreviousRingtone.stop();
    }
    mPreviousRingtone = getRingtone(mContext, getRingtoneUri(paramInt), inferStreamType());
    return mPreviousRingtone;
  }
  
  public int getRingtonePosition(Uri paramUri)
  {
    if (paramUri == null) {
      return -1;
    }
    Cursor localCursor = getCursor();
    int i = localCursor.getCount();
    if (!localCursor.moveToFirst()) {
      return -1;
    }
    Object localObject = null;
    Uri localUri = null;
    for (int j = 0; j < i; j++)
    {
      String str = localCursor.getString(2);
      if ((localUri == null) || (!str.equals(localObject))) {
        localUri = Uri.parse(str);
      }
      if (paramUri.equals(ContentUris.withAppendedId(localUri, localCursor.getLong(0)))) {
        return j;
      }
      localCursor.move(1);
      localObject = str;
    }
    return -1;
  }
  
  public Uri getRingtoneUri(int paramInt)
  {
    if ((mCursor != null) && (mCursor.moveToPosition(paramInt)) && (!mCursor.isClosed())) {
      return getUriFromCursor(mCursor);
    }
    Log.d("RingtoneManager", "getRingtoneUri return null");
    return null;
  }
  
  public boolean getStopPreviousRingtone()
  {
    return mStopPreviousRingtone;
  }
  
  public int inferStreamType()
  {
    int i = mType;
    if (i != 2) {
      if (i != 4)
      {
        if ((i != 32) && (i != 64) && (i != 128) && (i != 512)) {
          return 2;
        }
      }
      else {
        return 4;
      }
    }
    return 5;
  }
  
  public boolean isCustomRingtone(Uri paramUri)
  {
    if (!isExternalRingtoneUri(paramUri)) {
      return false;
    }
    String[] arrayOfString = null;
    if (paramUri == null) {
      paramUri = null;
    } else {
      paramUri = getRingtonePathFromUri(paramUri);
    }
    if (paramUri == null) {
      paramUri = arrayOfString;
    } else {
      paramUri = paramUri.getParentFile();
    }
    if (paramUri == null) {
      return false;
    }
    arrayOfString = new String[3];
    arrayOfString[0] = Environment.DIRECTORY_RINGTONES;
    arrayOfString[1] = Environment.DIRECTORY_NOTIFICATIONS;
    arrayOfString[2] = Environment.DIRECTORY_ALARMS;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (paramUri.equals(Environment.getExternalStoragePublicDirectory(arrayOfString[j]))) {
        return true;
      }
    }
    return false;
  }
  
  @Deprecated
  public void setIncludeDrm(boolean paramBoolean)
  {
    if (paramBoolean) {
      Log.w("RingtoneManager", "setIncludeDrm no longer supported");
    }
  }
  
  public void setStopPreviousRingtone(boolean paramBoolean)
  {
    mStopPreviousRingtone = paramBoolean;
  }
  
  public void setType(int paramInt)
  {
    if (mCursor == null)
    {
      mType = paramInt;
      setFilterColumnsList(paramInt);
      return;
    }
    throw new IllegalStateException("Setting filter columns should be done before querying for ringtones.");
  }
  
  public void stopPreviousRingtone()
  {
    if (mPreviousRingtone != null) {
      mPreviousRingtone.stop();
    }
  }
  
  public Cursor updateCursor()
  {
    Object localObject1 = constructBooleanTrueWhereClause(mFilterColumns);
    Object localObject2 = (StorageManager)mContext.getSystemService("storage");
    StringBuilder localStringBuilder = new StringBuilder((String)localObject1);
    localObject1 = getExternalStorage();
    for (int i = 0; i < localObject1.length; i++) {
      if (!((StorageManager)localObject2).getVolumeState(localObject1[i]).equals("mounted"))
      {
        localStringBuilder.append(" and ");
        localStringBuilder.append("_data");
        localStringBuilder.append(" not like ");
        localStringBuilder.append(" '%");
        localStringBuilder.append(localObject1[i]);
        localStringBuilder.append("%'");
      }
    }
    localObject2 = localStringBuilder.toString();
    localObject2 = query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MEDIA_COLUMNS, (String)localObject2, null, "title_key");
    if ((mType & 0x8) != 0)
    {
      localObject2 = new SortCursor(new Cursor[] { localObject2 }, "title_key");
      mCursor = ((Cursor)localObject2);
      return localObject2;
    }
    localObject2 = new SortCursor(new Cursor[] { getInternalRingtones(), localObject2 }, "title_key");
    mCursor = ((Cursor)localObject2);
    return localObject2;
  }
  
  private class NewRingtoneScanner
    implements Closeable, MediaScannerConnection.MediaScannerConnectionClient
  {
    private File mFile;
    private MediaScannerConnection mMediaScannerConnection;
    private LinkedBlockingQueue<Uri> mQueue = new LinkedBlockingQueue(1);
    
    public NewRingtoneScanner(File paramFile)
    {
      mFile = paramFile;
      mMediaScannerConnection = new MediaScannerConnection(mContext, this);
      mMediaScannerConnection.connect();
    }
    
    public void close()
    {
      mMediaScannerConnection.disconnect();
    }
    
    public void onMediaScannerConnected()
    {
      mMediaScannerConnection.scanFile(mFile.getAbsolutePath(), null);
    }
    
    public void onScanCompleted(String paramString, Uri paramUri)
    {
      if (paramUri == null)
      {
        mFile.delete();
        return;
      }
      try
      {
        mQueue.put(paramUri);
      }
      catch (InterruptedException paramString)
      {
        Log.e("RingtoneManager", "Unable to put new ringtone Uri in queue", paramString);
      }
    }
    
    public Uri take()
      throws InterruptedException
    {
      return (Uri)mQueue.take();
    }
  }
}
