package android.drm;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrmManagerClient
  implements AutoCloseable
{
  private static final int ACTION_PROCESS_DRM_INFO = 1002;
  private static final int ACTION_REMOVE_ALL_RIGHTS = 1001;
  public static final int ERROR_NONE = 0;
  public static final int ERROR_UNKNOWN = -2000;
  public static final int INVALID_SESSION = -1;
  private static final String TAG = "DrmManagerClient";
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mClosed = new AtomicBoolean();
  private Context mContext;
  private EventHandler mEventHandler;
  HandlerThread mEventThread;
  private InfoHandler mInfoHandler;
  HandlerThread mInfoThread;
  private long mNativeContext;
  private OnErrorListener mOnErrorListener;
  private OnEventListener mOnEventListener;
  private OnInfoListener mOnInfoListener;
  private int mUniqueId;
  
  static
  {
    System.loadLibrary("drmframework_jni");
  }
  
  public DrmManagerClient(Context paramContext)
  {
    mContext = paramContext;
    createEventThreads();
    mUniqueId = _initialize();
    mCloseGuard.open("release");
  }
  
  private native DrmInfo _acquireDrmInfo(int paramInt, DrmInfoRequest paramDrmInfoRequest);
  
  private native boolean _canHandle(int paramInt, String paramString1, String paramString2);
  
  private native int _checkRightsStatus(int paramInt1, String paramString, int paramInt2);
  
  private native DrmConvertedStatus _closeConvertSession(int paramInt1, int paramInt2);
  
  private native DrmConvertedStatus _convertData(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
  
  private native DrmSupportInfo[] _getAllSupportInfo(int paramInt);
  
  private native ContentValues _getConstraints(int paramInt1, String paramString, int paramInt2);
  
  private native int _getDrmObjectType(int paramInt, String paramString1, String paramString2);
  
  private native ContentValues _getMetadata(int paramInt, String paramString);
  
  private native String _getOriginalMimeType(int paramInt, String paramString, FileDescriptor paramFileDescriptor);
  
  private native int _initialize();
  
  private native void _installDrmEngine(int paramInt, String paramString);
  
  private native int _openConvertSession(int paramInt, String paramString);
  
  private native DrmInfoStatus _processDrmInfo(int paramInt, DrmInfo paramDrmInfo);
  
  private native void _release(int paramInt);
  
  private native int _removeAllRights(int paramInt);
  
  private native int _removeRights(int paramInt, String paramString);
  
  private native int _saveRights(int paramInt, DrmRights paramDrmRights, String paramString1, String paramString2);
  
  private native void _setListeners(int paramInt, Object paramObject);
  
  /* Error */
  private String convertUriToPath(Uri paramUri)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_1
    //   3: ifnull +230 -> 233
    //   6: aload_1
    //   7: invokevirtual 177	android/net/Uri:getScheme	()Ljava/lang/String;
    //   10: astore_2
    //   11: aload_2
    //   12: ifnull +216 -> 228
    //   15: aload_2
    //   16: ldc -77
    //   18: invokevirtual 185	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   21: ifne +207 -> 228
    //   24: aload_2
    //   25: ldc -69
    //   27: invokevirtual 185	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   30: ifeq +6 -> 36
    //   33: goto +195 -> 228
    //   36: aload_2
    //   37: ldc -67
    //   39: invokevirtual 185	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   42: ifeq +11 -> 53
    //   45: aload_1
    //   46: invokevirtual 192	android/net/Uri:toString	()Ljava/lang/String;
    //   49: astore_2
    //   50: goto +183 -> 233
    //   53: aload_2
    //   54: ldc -62
    //   56: invokevirtual 185	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   59: ifeq +159 -> 218
    //   62: aconst_null
    //   63: astore_3
    //   64: aconst_null
    //   65: astore_2
    //   66: aload_0
    //   67: getfield 88	android/drm/DrmManagerClient:mContext	Landroid/content/Context;
    //   70: invokevirtual 200	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   73: aload_1
    //   74: iconst_1
    //   75: anewarray 181	java/lang/String
    //   78: dup
    //   79: iconst_0
    //   80: ldc -54
    //   82: aastore
    //   83: aconst_null
    //   84: aconst_null
    //   85: aconst_null
    //   86: invokevirtual 208	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   89: astore_1
    //   90: aload_1
    //   91: ifnull +65 -> 156
    //   94: aload_1
    //   95: astore_2
    //   96: aload_1
    //   97: astore_3
    //   98: aload_1
    //   99: invokeinterface 213 1 0
    //   104: ifeq +52 -> 156
    //   107: aload_1
    //   108: astore_2
    //   109: aload_1
    //   110: astore_3
    //   111: aload_1
    //   112: invokeinterface 217 1 0
    //   117: ifeq +39 -> 156
    //   120: aload_1
    //   121: astore_2
    //   122: aload_1
    //   123: astore_3
    //   124: aload_1
    //   125: aload_1
    //   126: ldc -54
    //   128: invokeinterface 221 2 0
    //   133: invokeinterface 225 2 0
    //   138: astore 4
    //   140: aload 4
    //   142: astore_2
    //   143: aload_1
    //   144: ifnull +9 -> 153
    //   147: aload_1
    //   148: invokeinterface 228 1 0
    //   153: goto +80 -> 233
    //   156: aload_1
    //   157: astore_2
    //   158: aload_1
    //   159: astore_3
    //   160: new 230	java/lang/IllegalArgumentException
    //   163: astore 4
    //   165: aload_1
    //   166: astore_2
    //   167: aload_1
    //   168: astore_3
    //   169: aload 4
    //   171: ldc -24
    //   173: invokespecial 234	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   176: aload_1
    //   177: astore_2
    //   178: aload_1
    //   179: astore_3
    //   180: aload 4
    //   182: athrow
    //   183: astore_1
    //   184: goto +22 -> 206
    //   187: astore_1
    //   188: aload_3
    //   189: astore_2
    //   190: new 230	java/lang/IllegalArgumentException
    //   193: astore_1
    //   194: aload_3
    //   195: astore_2
    //   196: aload_1
    //   197: ldc -20
    //   199: invokespecial 234	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   202: aload_3
    //   203: astore_2
    //   204: aload_1
    //   205: athrow
    //   206: aload_2
    //   207: ifnull +9 -> 216
    //   210: aload_2
    //   211: invokeinterface 228 1 0
    //   216: aload_1
    //   217: athrow
    //   218: new 230	java/lang/IllegalArgumentException
    //   221: dup
    //   222: ldc -18
    //   224: invokespecial 234	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   227: athrow
    //   228: aload_1
    //   229: invokevirtual 241	android/net/Uri:getPath	()Ljava/lang/String;
    //   232: astore_2
    //   233: aload_2
    //   234: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	235	0	this	DrmManagerClient
    //   0	235	1	paramUri	Uri
    //   1	233	2	localObject1	Object
    //   63	140	3	localUri	Uri
    //   138	43	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   66	90	183	finally
    //   98	107	183	finally
    //   111	120	183	finally
    //   124	140	183	finally
    //   160	165	183	finally
    //   169	176	183	finally
    //   180	183	183	finally
    //   190	194	183	finally
    //   196	202	183	finally
    //   204	206	183	finally
    //   66	90	187	android/database/sqlite/SQLiteException
    //   98	107	187	android/database/sqlite/SQLiteException
    //   111	120	187	android/database/sqlite/SQLiteException
    //   124	140	187	android/database/sqlite/SQLiteException
    //   160	165	187	android/database/sqlite/SQLiteException
    //   169	176	187	android/database/sqlite/SQLiteException
    //   180	183	187	android/database/sqlite/SQLiteException
  }
  
  private void createEventThreads()
  {
    if ((mEventHandler == null) && (mInfoHandler == null))
    {
      mInfoThread = new HandlerThread("DrmManagerClient.InfoHandler");
      mInfoThread.start();
      mInfoHandler = new InfoHandler(mInfoThread.getLooper());
      mEventThread = new HandlerThread("DrmManagerClient.EventHandler");
      mEventThread.start();
      mEventHandler = new EventHandler(mEventThread.getLooper());
    }
  }
  
  private void createListeners()
  {
    _setListeners(mUniqueId, new WeakReference(this));
  }
  
  private int getErrorType(int paramInt)
  {
    int i = -1;
    switch (paramInt)
    {
    default: 
      paramInt = i;
      break;
    case 1: 
    case 2: 
    case 3: 
      paramInt = 2006;
    }
    return paramInt;
  }
  
  private int getEventType(int paramInt)
  {
    int i = -1;
    switch (paramInt)
    {
    default: 
      paramInt = i;
      break;
    case 1: 
    case 2: 
    case 3: 
      paramInt = 1002;
    }
    return paramInt;
  }
  
  public static void notify(Object paramObject, int paramInt1, int paramInt2, String paramString)
  {
    paramObject = (DrmManagerClient)((WeakReference)paramObject).get();
    if ((paramObject != null) && (mInfoHandler != null))
    {
      paramString = mInfoHandler.obtainMessage(1, paramInt1, paramInt2, paramString);
      mInfoHandler.sendMessage(paramString);
    }
  }
  
  public DrmInfo acquireDrmInfo(DrmInfoRequest paramDrmInfoRequest)
  {
    if ((paramDrmInfoRequest != null) && (paramDrmInfoRequest.isValid())) {
      return _acquireDrmInfo(mUniqueId, paramDrmInfoRequest);
    }
    throw new IllegalArgumentException("Given drmInfoRequest is invalid/null");
  }
  
  public int acquireRights(DrmInfoRequest paramDrmInfoRequest)
  {
    paramDrmInfoRequest = acquireDrmInfo(paramDrmInfoRequest);
    if (paramDrmInfoRequest == null) {
      return 63536;
    }
    return processDrmInfo(paramDrmInfoRequest);
  }
  
  public boolean canHandle(Uri paramUri, String paramString)
  {
    if (((paramUri != null) && (Uri.EMPTY != paramUri)) || ((paramString != null) && (!paramString.equals("")))) {
      return canHandle(convertUriToPath(paramUri), paramString);
    }
    throw new IllegalArgumentException("Uri or the mimetype should be non null");
  }
  
  public boolean canHandle(String paramString1, String paramString2)
  {
    if (((paramString1 != null) && (!paramString1.equals(""))) || ((paramString2 != null) && (!paramString2.equals("")))) {
      return _canHandle(mUniqueId, paramString1, paramString2);
    }
    throw new IllegalArgumentException("Path or the mimetype should be non null");
  }
  
  public int checkRightsStatus(Uri paramUri)
  {
    if ((paramUri != null) && (Uri.EMPTY != paramUri)) {
      return checkRightsStatus(convertUriToPath(paramUri));
    }
    throw new IllegalArgumentException("Given uri is not valid");
  }
  
  public int checkRightsStatus(Uri paramUri, int paramInt)
  {
    if ((paramUri != null) && (Uri.EMPTY != paramUri)) {
      return checkRightsStatus(convertUriToPath(paramUri), paramInt);
    }
    throw new IllegalArgumentException("Given uri is not valid");
  }
  
  public int checkRightsStatus(String paramString)
  {
    return checkRightsStatus(paramString, 0);
  }
  
  public int checkRightsStatus(String paramString, int paramInt)
  {
    if ((paramString != null) && (!paramString.equals("")) && (DrmStore.Action.isValid(paramInt))) {
      return _checkRightsStatus(mUniqueId, paramString, paramInt);
    }
    throw new IllegalArgumentException("Given path or action is not valid");
  }
  
  public void close()
  {
    mCloseGuard.close();
    if (mClosed.compareAndSet(false, true))
    {
      if (mEventHandler != null)
      {
        mEventThread.quit();
        mEventThread = null;
      }
      if (mInfoHandler != null)
      {
        mInfoThread.quit();
        mInfoThread = null;
      }
      mEventHandler = null;
      mInfoHandler = null;
      mOnEventListener = null;
      mOnInfoListener = null;
      mOnErrorListener = null;
      _release(mUniqueId);
    }
  }
  
  public DrmConvertedStatus closeConvertSession(int paramInt)
  {
    return _closeConvertSession(mUniqueId, paramInt);
  }
  
  public DrmConvertedStatus convertData(int paramInt, byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
      return _convertData(mUniqueId, paramInt, paramArrayOfByte);
    }
    throw new IllegalArgumentException("Given inputData should be non null");
  }
  
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
  
  public String[] getAvailableDrmEngines()
  {
    DrmSupportInfo[] arrayOfDrmSupportInfo = _getAllSupportInfo(mUniqueId);
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < arrayOfDrmSupportInfo.length; i++) {
      localArrayList.add(arrayOfDrmSupportInfo[i].getDescriprition());
    }
    return (String[])localArrayList.toArray(new String[localArrayList.size()]);
  }
  
  public ContentValues getConstraints(Uri paramUri, int paramInt)
  {
    if ((paramUri != null) && (Uri.EMPTY != paramUri)) {
      return getConstraints(convertUriToPath(paramUri), paramInt);
    }
    throw new IllegalArgumentException("Uri should be non null");
  }
  
  public ContentValues getConstraints(String paramString, int paramInt)
  {
    if ((paramString != null) && (!paramString.equals("")) && (DrmStore.Action.isValid(paramInt))) {
      return _getConstraints(mUniqueId, paramString, paramInt);
    }
    throw new IllegalArgumentException("Given usage or path is invalid/null");
  }
  
  public int getDrmObjectType(Uri paramUri, String paramString)
  {
    if (((paramUri != null) && (Uri.EMPTY != paramUri)) || ((paramString != null) && (!paramString.equals(""))))
    {
      String str = "";
      try
      {
        paramUri = convertUriToPath(paramUri);
      }
      catch (Exception paramUri)
      {
        Log.w("DrmManagerClient", "Given Uri could not be found in media store");
        paramUri = str;
      }
      return getDrmObjectType(paramUri, paramString);
    }
    throw new IllegalArgumentException("Uri or the mimetype should be non null");
  }
  
  public int getDrmObjectType(String paramString1, String paramString2)
  {
    if (((paramString1 != null) && (!paramString1.equals(""))) || ((paramString2 != null) && (!paramString2.equals("")))) {
      return _getDrmObjectType(mUniqueId, paramString1, paramString2);
    }
    throw new IllegalArgumentException("Path or the mimetype should be non null");
  }
  
  public ContentValues getMetadata(Uri paramUri)
  {
    if ((paramUri != null) && (Uri.EMPTY != paramUri)) {
      return getMetadata(convertUriToPath(paramUri));
    }
    throw new IllegalArgumentException("Uri should be non null");
  }
  
  public ContentValues getMetadata(String paramString)
  {
    if ((paramString != null) && (!paramString.equals(""))) {
      return _getMetadata(mUniqueId, paramString);
    }
    throw new IllegalArgumentException("Given path is invalid/null");
  }
  
  public String getOriginalMimeType(Uri paramUri)
  {
    if ((paramUri != null) && (Uri.EMPTY != paramUri)) {
      return getOriginalMimeType(convertUriToPath(paramUri));
    }
    throw new IllegalArgumentException("Given uri is not valid");
  }
  
  /* Error */
  public String getOriginalMimeType(String paramString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +198 -> 199
    //   4: aload_1
    //   5: ldc -77
    //   7: invokevirtual 185	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   10: ifne +189 -> 199
    //   13: aconst_null
    //   14: astore_2
    //   15: aconst_null
    //   16: astore_3
    //   17: aconst_null
    //   18: astore 4
    //   20: aconst_null
    //   21: astore 5
    //   23: aconst_null
    //   24: astore 6
    //   26: aconst_null
    //   27: astore 7
    //   29: aload 4
    //   31: astore 8
    //   33: aload 5
    //   35: astore 9
    //   37: new 439	java/io/File
    //   40: astore 10
    //   42: aload 4
    //   44: astore 8
    //   46: aload 5
    //   48: astore 9
    //   50: aload 10
    //   52: aload_1
    //   53: invokespecial 440	java/io/File:<init>	(Ljava/lang/String;)V
    //   56: aload 4
    //   58: astore 8
    //   60: aload 5
    //   62: astore 9
    //   64: aload 10
    //   66: invokevirtual 443	java/io/File:exists	()Z
    //   69: ifeq +46 -> 115
    //   72: aload 4
    //   74: astore 8
    //   76: aload 5
    //   78: astore 9
    //   80: new 445	java/io/FileInputStream
    //   83: astore 6
    //   85: aload 4
    //   87: astore 8
    //   89: aload 5
    //   91: astore 9
    //   93: aload 6
    //   95: aload 10
    //   97: invokespecial 448	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   100: aload 6
    //   102: astore 8
    //   104: aload 6
    //   106: astore 9
    //   108: aload 6
    //   110: invokevirtual 452	java/io/FileInputStream:getFD	()Ljava/io/FileDescriptor;
    //   113: astore 7
    //   115: aload 6
    //   117: astore 8
    //   119: aload 6
    //   121: astore 9
    //   123: aload_0
    //   124: aload_0
    //   125: getfield 97	android/drm/DrmManagerClient:mUniqueId	I
    //   128: aload_1
    //   129: aload 7
    //   131: invokespecial 454	android/drm/DrmManagerClient:_getOriginalMimeType	(ILjava/lang/String;Ljava/io/FileDescriptor;)Ljava/lang/String;
    //   134: astore_1
    //   135: aload_1
    //   136: astore 9
    //   138: aload 9
    //   140: astore_1
    //   141: aload 6
    //   143: ifnull +54 -> 197
    //   146: aload 9
    //   148: astore_1
    //   149: aload 6
    //   151: invokevirtual 455	java/io/FileInputStream:close	()V
    //   154: goto +43 -> 197
    //   157: astore 6
    //   159: goto +38 -> 197
    //   162: astore_1
    //   163: aload 8
    //   165: ifnull +13 -> 178
    //   168: aload 8
    //   170: invokevirtual 455	java/io/FileInputStream:close	()V
    //   173: goto +5 -> 178
    //   176: astore 6
    //   178: aload_1
    //   179: athrow
    //   180: astore_1
    //   181: aload_2
    //   182: astore_1
    //   183: aload 9
    //   185: ifnull +12 -> 197
    //   188: aload_3
    //   189: astore_1
    //   190: aload 9
    //   192: astore 6
    //   194: goto -45 -> 149
    //   197: aload_1
    //   198: areturn
    //   199: new 230	java/lang/IllegalArgumentException
    //   202: dup
    //   203: ldc_w 457
    //   206: invokespecial 234	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   209: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	210	0	this	DrmManagerClient
    //   0	210	1	paramString	String
    //   14	168	2	localObject1	Object
    //   16	173	3	localObject2	Object
    //   18	68	4	localObject3	Object
    //   21	69	5	localObject4	Object
    //   24	126	6	localFileInputStream	java.io.FileInputStream
    //   157	1	6	localIOException1	IOException
    //   176	1	6	localIOException2	IOException
    //   192	1	6	localObject5	Object
    //   27	103	7	localFileDescriptor	FileDescriptor
    //   31	138	8	localObject6	Object
    //   35	156	9	localObject7	Object
    //   40	56	10	localFile	java.io.File
    // Exception table:
    //   from	to	target	type
    //   149	154	157	java/io/IOException
    //   37	42	162	finally
    //   50	56	162	finally
    //   64	72	162	finally
    //   80	85	162	finally
    //   93	100	162	finally
    //   108	115	162	finally
    //   123	135	162	finally
    //   168	173	176	java/io/IOException
    //   37	42	180	java/io/IOException
    //   50	56	180	java/io/IOException
    //   64	72	180	java/io/IOException
    //   80	85	180	java/io/IOException
    //   93	100	180	java/io/IOException
    //   108	115	180	java/io/IOException
    //   123	135	180	java/io/IOException
  }
  
  public void installDrmEngine(String paramString)
  {
    if ((paramString != null) && (!paramString.equals("")))
    {
      _installDrmEngine(mUniqueId, paramString);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Given engineFilePath: ");
    localStringBuilder.append(paramString);
    localStringBuilder.append("is not valid");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int openConvertSession(String paramString)
  {
    if ((paramString != null) && (!paramString.equals(""))) {
      return _openConvertSession(mUniqueId, paramString);
    }
    throw new IllegalArgumentException("Path or the mimeType should be non null");
  }
  
  public int processDrmInfo(DrmInfo paramDrmInfo)
  {
    if ((paramDrmInfo != null) && (paramDrmInfo.isValid()))
    {
      int i = 63536;
      if (mEventHandler != null)
      {
        paramDrmInfo = mEventHandler.obtainMessage(1002, paramDrmInfo);
        if (mEventHandler.sendMessage(paramDrmInfo)) {
          i = 0;
        } else {
          i = 63536;
        }
      }
      return i;
    }
    throw new IllegalArgumentException("Given drmInfo is invalid/null");
  }
  
  @Deprecated
  public void release()
  {
    close();
  }
  
  public int removeAllRights()
  {
    int i = 63536;
    if (mEventHandler != null)
    {
      Message localMessage = mEventHandler.obtainMessage(1001);
      if (mEventHandler.sendMessage(localMessage)) {
        i = 0;
      } else {
        i = 63536;
      }
    }
    return i;
  }
  
  public int removeRights(Uri paramUri)
  {
    if ((paramUri != null) && (Uri.EMPTY != paramUri)) {
      return removeRights(convertUriToPath(paramUri));
    }
    throw new IllegalArgumentException("Given uri is not valid");
  }
  
  public int removeRights(String paramString)
  {
    if ((paramString != null) && (!paramString.equals(""))) {
      return _removeRights(mUniqueId, paramString);
    }
    throw new IllegalArgumentException("Given path should be non null");
  }
  
  public int saveRights(DrmRights paramDrmRights, String paramString1, String paramString2)
    throws IOException
  {
    if ((paramDrmRights != null) && (paramDrmRights.isValid()))
    {
      if ((paramString1 != null) && (!paramString1.equals(""))) {
        DrmUtils.writeToFile(paramString1, paramDrmRights.getData());
      }
      return _saveRights(mUniqueId, paramDrmRights, paramString1, paramString2);
    }
    throw new IllegalArgumentException("Given drmRights or contentPath is not valid");
  }
  
  public void setOnErrorListener(OnErrorListener paramOnErrorListener)
  {
    try
    {
      mOnErrorListener = paramOnErrorListener;
      if (paramOnErrorListener != null) {
        createListeners();
      }
      return;
    }
    finally {}
  }
  
  public void setOnEventListener(OnEventListener paramOnEventListener)
  {
    try
    {
      mOnEventListener = paramOnEventListener;
      if (paramOnEventListener != null) {
        createListeners();
      }
      return;
    }
    finally {}
  }
  
  public void setOnInfoListener(OnInfoListener paramOnInfoListener)
  {
    try
    {
      mOnInfoListener = paramOnInfoListener;
      if (paramOnInfoListener != null) {
        createListeners();
      }
      return;
    }
    finally {}
  }
  
  private class EventHandler
    extends Handler
  {
    public EventHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      Object localObject1 = null;
      Object localObject2 = null;
      HashMap localHashMap = new HashMap();
      switch (what)
      {
      default: 
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Unknown message type ");
        ((StringBuilder)localObject2).append(what);
        Log.e("DrmManagerClient", ((StringBuilder)localObject2).toString());
        return;
      case 1002: 
        DrmInfo localDrmInfo = (DrmInfo)obj;
        paramMessage = DrmManagerClient.this._processDrmInfo(mUniqueId, localDrmInfo);
        localHashMap.put("drm_info_status_object", paramMessage);
        localHashMap.put("drm_info_object", localDrmInfo);
        if ((paramMessage != null) && (1 == statusCode))
        {
          paramMessage = new DrmEvent(mUniqueId, DrmManagerClient.this.getEventType(infoType), null, localHashMap);
        }
        else
        {
          int i;
          if (paramMessage != null) {
            i = infoType;
          } else {
            i = localDrmInfo.getInfoType();
          }
          localObject2 = new DrmErrorEvent(mUniqueId, DrmManagerClient.this.getErrorType(i), null, localHashMap);
          paramMessage = localObject1;
        }
        break;
      case 1001: 
        if (DrmManagerClient.this._removeAllRights(mUniqueId) == 0)
        {
          paramMessage = new DrmEvent(mUniqueId, 1001, null);
        }
        else
        {
          localObject2 = new DrmErrorEvent(mUniqueId, 2007, null);
          paramMessage = localObject1;
        }
        break;
      }
      if ((mOnEventListener != null) && (paramMessage != null)) {
        mOnEventListener.onEvent(DrmManagerClient.this, paramMessage);
      }
      if ((mOnErrorListener != null) && (localObject2 != null)) {
        mOnErrorListener.onError(DrmManagerClient.this, (DrmErrorEvent)localObject2);
      }
    }
  }
  
  private class InfoHandler
    extends Handler
  {
    public static final int INFO_EVENT_TYPE = 1;
    
    public InfoHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      Object localObject1 = null;
      Object localObject2 = null;
      if (what != 1)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Unknown message type ");
        ((StringBuilder)localObject2).append(what);
        Log.e("DrmManagerClient", ((StringBuilder)localObject2).toString());
        return;
      }
      int i = arg1;
      int j = arg2;
      paramMessage = obj.toString();
      switch (j)
      {
      default: 
        localObject2 = new DrmErrorEvent(i, j, paramMessage);
        paramMessage = localObject1;
        break;
      case 2: 
        try
        {
          DrmUtils.removeFile(paramMessage);
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
        paramMessage = new DrmInfoEvent(i, j, paramMessage);
        break;
      case 1: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
        paramMessage = new DrmInfoEvent(i, j, paramMessage);
      }
      if ((mOnInfoListener != null) && (paramMessage != null)) {
        mOnInfoListener.onInfo(DrmManagerClient.this, paramMessage);
      }
      if ((mOnErrorListener != null) && (localObject2 != null)) {
        mOnErrorListener.onError(DrmManagerClient.this, (DrmErrorEvent)localObject2);
      }
    }
  }
  
  public static abstract interface OnErrorListener
  {
    public abstract void onError(DrmManagerClient paramDrmManagerClient, DrmErrorEvent paramDrmErrorEvent);
  }
  
  public static abstract interface OnEventListener
  {
    public abstract void onEvent(DrmManagerClient paramDrmManagerClient, DrmEvent paramDrmEvent);
  }
  
  public static abstract interface OnInfoListener
  {
    public abstract void onInfo(DrmManagerClient paramDrmManagerClient, DrmInfoEvent paramDrmInfoEvent);
  }
}
