package android.mtp;

import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaScanner;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.storage.StorageVolume;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Files;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.collect.Sets;
import dalvik.system.CloseGuard;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MtpDatabase
  implements AutoCloseable
{
  private static final int[] AUDIO_PROPERTIES;
  private static final int[] DEVICE_PROPERTIES = { 54273, 54274, 20483, 20481, 54279 };
  private static final int[] FILE_PROPERTIES;
  private static final String[] ID_PROJECTION;
  private static final int[] IMAGE_PROPERTIES;
  private static final String NO_MEDIA = ".nomedia";
  private static final String[] PATH_PROJECTION;
  private static final String PATH_WHERE = "_data=?";
  private static final int[] PLAYBACK_FORMATS;
  private static final String TAG = MtpDatabase.class.getSimpleName();
  private static final int[] VIDEO_PROPERTIES;
  private int mBatteryLevel;
  private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getAction().equals("android.intent.action.BATTERY_CHANGED"))
      {
        MtpDatabase.access$002(MtpDatabase.this, paramAnonymousIntent.getIntExtra("scale", 0));
        int i = paramAnonymousIntent.getIntExtra("level", 0);
        if (i != mBatteryLevel)
        {
          MtpDatabase.access$102(MtpDatabase.this, i);
          if (mServer != null) {
            mServer.sendDevicePropertyChanged(20481);
          }
        }
      }
    }
  };
  private int mBatteryScale;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mClosed = new AtomicBoolean();
  private final Context mContext;
  private SharedPreferences mDeviceProperties;
  private int mDeviceType;
  private MtpStorageManager mManager;
  private final ContentProviderClient mMediaProvider;
  private final MediaScanner mMediaScanner;
  private long mNativeContext;
  private final Uri mObjectsUri;
  private final HashMap<Integer, MtpPropertyGroup> mPropertyGroupsByFormat = new HashMap();
  private final HashMap<Integer, MtpPropertyGroup> mPropertyGroupsByProperty = new HashMap();
  private MtpServer mServer;
  private final HashMap<String, MtpStorage> mStorageMap = new HashMap();
  private final String mVolumeName;
  
  static
  {
    ID_PROJECTION = new String[] { "_id" };
    PATH_PROJECTION = new String[] { "_data" };
    System.loadLibrary("media_jni");
    PLAYBACK_FORMATS = new int[] { 12288, 12289, 12292, 12293, 12296, 12297, 12299, 14337, 14338, 14340, 14343, 14344, 14347, 14349, 47361, 47362, 47363, 47490, 47491, 47492, 47621, 47632, 47633, 47636, 47746, 47366, 14353, 14354 };
    FILE_PROPERTIES = new int[] { 56321, 56322, 56323, 56324, 56327, 56329, 56385, 56331, 56388, 56544, 56398 };
    AUDIO_PROPERTIES = new int[] { 56390, 56474, 56475, 56459, 56473, 56457, 56460, 56470, 56985, 56978, 56986, 56980, 56979 };
    VIDEO_PROPERTIES = new int[] { 56390, 56474, 56457, 56392 };
    IMAGE_PROPERTIES = new int[] { 56392 };
  }
  
  public MtpDatabase(Context paramContext, String paramString, String[] paramArrayOfString)
  {
    native_setup();
    mContext = paramContext;
    mMediaProvider = paramContext.getContentResolver().acquireContentProviderClient("media");
    mVolumeName = paramString;
    mObjectsUri = MediaStore.Files.getMtpObjectsUri(paramString);
    mMediaScanner = new MediaScanner(paramContext, mVolumeName);
    MtpStorageManager.MtpNotifier local2 = new MtpStorageManager.MtpNotifier()
    {
      public void sendObjectAdded(int paramAnonymousInt)
      {
        if (mServer != null) {
          mServer.sendObjectAdded(paramAnonymousInt);
        }
      }
      
      public void sendObjectInfoChanged(int paramAnonymousInt)
      {
        if (mServer != null) {
          mServer.sendObjectInfoChanged(paramAnonymousInt);
        }
      }
      
      public void sendObjectRemoved(int paramAnonymousInt)
      {
        if (mServer != null) {
          mServer.sendObjectRemoved(paramAnonymousInt);
        }
      }
    };
    if (paramArrayOfString == null) {
      paramString = null;
    } else {
      paramString = Sets.newHashSet(paramArrayOfString);
    }
    mManager = new MtpStorageManager(local2, paramString);
    initDeviceProperties(paramContext);
    mDeviceType = SystemProperties.getInt("sys.usb.mtp.device_type", 0);
    mCloseGuard.open("close");
  }
  
  private int beginCopyObject(int paramInt1, int paramInt2, int paramInt3)
  {
    MtpStorageManager.MtpObject localMtpObject1 = mManager.getObject(paramInt1);
    MtpStorageManager.MtpObject localMtpObject2;
    if (paramInt2 == 0) {
      localMtpObject2 = mManager.getStorageRoot(paramInt3);
    } else {
      localMtpObject2 = mManager.getObject(paramInt2);
    }
    if ((localMtpObject1 != null) && (localMtpObject2 != null)) {
      return mManager.beginCopyObject(localMtpObject1, localMtpObject2);
    }
    return 8201;
  }
  
  private int beginDeleteObject(int paramInt)
  {
    MtpStorageManager.MtpObject localMtpObject = mManager.getObject(paramInt);
    if (localMtpObject == null) {
      return 8201;
    }
    if (!mManager.beginRemoveObject(localMtpObject)) {
      return 8194;
    }
    return 8193;
  }
  
  private int beginMoveObject(int paramInt1, int paramInt2, int paramInt3)
  {
    MtpStorageManager.MtpObject localMtpObject1 = mManager.getObject(paramInt1);
    MtpStorageManager.MtpObject localMtpObject2;
    if (paramInt2 == 0) {
      localMtpObject2 = mManager.getStorageRoot(paramInt3);
    } else {
      localMtpObject2 = mManager.getObject(paramInt2);
    }
    if ((localMtpObject1 != null) && (localMtpObject2 != null))
    {
      if (mManager.beginMoveObject(localMtpObject1, localMtpObject2)) {
        paramInt1 = 8193;
      } else {
        paramInt1 = 8194;
      }
      return paramInt1;
    }
    return 8201;
  }
  
  private int beginSendObject(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    MtpStorageManager.MtpObject localMtpObject;
    if (paramInt2 == 0) {
      localMtpObject = mManager.getStorageRoot(paramInt3);
    } else {
      localMtpObject = mManager.getObject(paramInt2);
    }
    if (localMtpObject == null) {
      return -1;
    }
    paramString = Paths.get(paramString, new String[0]);
    return mManager.beginSendObject(localMtpObject, paramString.getFileName().toString(), paramInt1);
  }
  
  private void deleteFromMedia(Path paramPath, boolean paramBoolean)
  {
    if (paramBoolean) {
      try
      {
        ContentProviderClient localContentProviderClient = mMediaProvider;
        localObject2 = mObjectsUri;
        Object localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append(paramPath);
        ((StringBuilder)localObject3).append("/%");
        localObject3 = ((StringBuilder)localObject3).toString();
        String str2 = Integer.toString(paramPath.toString().length() + 1);
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append(paramPath.toString());
        localStringBuilder.append("/");
        localContentProviderClient.delete((Uri)localObject2, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", new String[] { localObject3, str2, localStringBuilder.toString() });
      }
      catch (Exception localException)
      {
        break label306;
      }
    }
    String str1 = paramPath.toString();
    if (mMediaProvider.delete(mObjectsUri, "_data=?", new String[] { str1 }) > 0)
    {
      if (!paramBoolean)
      {
        paramBoolean = paramPath.toString().toLowerCase(Locale.US).endsWith(".nomedia");
        if (paramBoolean) {
          try
          {
            str1 = paramPath.getParent().toString();
            mMediaProvider.call("unhide", str1, null);
          }
          catch (RemoteException localRemoteException)
          {
            for (;;)
            {
              localObject1 = TAG;
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("failed to unhide/rescan for ");
              ((StringBuilder)localObject2).append(paramPath);
              Log.e((String)localObject1, ((StringBuilder)localObject2).toString());
            }
          }
        }
      }
    }
    else
    {
      localObject1 = TAG;
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Mediaprovider didn't delete ");
      ((StringBuilder)localObject2).append(paramPath);
      Log.i((String)localObject1, ((StringBuilder)localObject2).toString());
    }
    return;
    label306:
    Object localObject2 = TAG;
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Failed to delete ");
    ((StringBuilder)localObject1).append(paramPath);
    ((StringBuilder)localObject1).append(" from MediaProvider");
    Log.d((String)localObject2, ((StringBuilder)localObject1).toString());
  }
  
  private void endCopyObject(int paramInt, boolean paramBoolean)
  {
    Object localObject = mManager.getObject(paramInt);
    if ((localObject != null) && (mManager.endCopyObject((MtpStorageManager.MtpObject)localObject, paramBoolean)))
    {
      if (!paramBoolean) {
        return;
      }
      String str = ((MtpStorageManager.MtpObject)localObject).getPath().toString();
      int i = ((MtpStorageManager.MtpObject)localObject).getFormat();
      int j = str.lastIndexOf('.');
      paramInt = i;
      if (j >= 0)
      {
        paramInt = i;
        if (str.substring(j + 1).equalsIgnoreCase("mov"))
        {
          Log.e(TAG, String.format("attempt to put mov file in endCopyObject: %s with format= 0x%04X", new Object[] { str, Integer.valueOf(i) }));
          paramInt = 12288;
        }
      }
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("_data", str);
      localContentValues.put("format", Integer.valueOf(paramInt));
      localContentValues.put("_size", Long.valueOf(((MtpStorageManager.MtpObject)localObject).getSize()));
      localContentValues.put("date_modified", Long.valueOf(((MtpStorageManager.MtpObject)localObject).getModifiedTime()));
      try
      {
        if (((MtpStorageManager.MtpObject)localObject).getParent().isRoot())
        {
          localContentValues.put("parent", Integer.valueOf(0));
        }
        else
        {
          i = findInMedia(((MtpStorageManager.MtpObject)localObject).getParent().getPath());
          if (i == -1) {
            break label309;
          }
          localContentValues.put("parent", Integer.valueOf(i));
        }
        if (((MtpStorageManager.MtpObject)localObject).isDir())
        {
          mMediaScanner.scanDirectories(new String[] { str });
        }
        else
        {
          localObject = mMediaProvider.insert(mObjectsUri, localContentValues);
          if (localObject != null) {
            rescanFile(str, Integer.parseInt((String)((Uri)localObject).getPathSegments().get(2)), paramInt);
          }
        }
        break label324;
        label309:
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(TAG, "RemoteException in beginSendObject", localRemoteException);
      }
      label324:
      return;
    }
    Log.e(TAG, "Failed to end copy object");
  }
  
  private void endDeleteObject(int paramInt, boolean paramBoolean)
  {
    MtpStorageManager.MtpObject localMtpObject = mManager.getObject(paramInt);
    if (localMtpObject == null) {
      return;
    }
    if (!mManager.endRemoveObject(localMtpObject, paramBoolean)) {
      Log.e(TAG, "Failed to end remove object");
    }
    if (paramBoolean) {
      deleteFromMedia(localMtpObject.getPath(), localMtpObject.isDir());
    }
  }
  
  private void endMoveObject(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
  {
    MtpStorageManager.MtpObject localMtpObject1;
    if (paramInt1 == 0) {
      localMtpObject1 = mManager.getStorageRoot(paramInt3);
    } else {
      localMtpObject1 = mManager.getObject(paramInt1);
    }
    Object localObject1;
    if (paramInt2 == 0) {
      localObject1 = mManager.getStorageRoot(paramInt4);
    } else {
      localObject1 = mManager.getObject(paramInt2);
    }
    Object localObject2 = mManager.getObject(paramInt5).getName();
    if ((localObject1 != null) && (localMtpObject1 != null) && (mManager.endMoveObject(localMtpObject1, (MtpStorageManager.MtpObject)localObject1, (String)localObject2, paramBoolean)))
    {
      MtpStorageManager.MtpObject localMtpObject2 = mManager.getObject(paramInt5);
      if ((paramBoolean) && (localMtpObject2 != null))
      {
        ContentValues localContentValues = new ContentValues();
        localObject1 = ((MtpStorageManager.MtpObject)localObject1).getPath().resolve((String)localObject2);
        localObject2 = localMtpObject1.getPath().resolve((String)localObject2);
        String str = ((Path)localObject1).toString();
        localContentValues.put("_data", str);
        paramInt2 = localMtpObject2.getFormat();
        paramInt3 = str.lastIndexOf('.');
        if (paramInt3 >= 0)
        {
          paramInt1 = paramInt2;
          if (str.substring(paramInt3 + 1).equalsIgnoreCase("mov"))
          {
            Log.e(TAG, String.format("attempt to put mov file in endMoveObject: %s with format= 0x%04X", new Object[] { str, Integer.valueOf(paramInt2) }));
            paramInt1 = 12288;
          }
        }
        else
        {
          paramInt1 = paramInt2;
        }
        if (localMtpObject2.getParent().isRoot())
        {
          localContentValues.put("parent", Integer.valueOf(0));
        }
        else
        {
          paramInt2 = findInMedia(((Path)localObject1).getParent());
          if (paramInt2 == -1) {
            break label512;
          }
          localContentValues.put("parent", Integer.valueOf(paramInt2));
        }
        str = ((Path)localObject2).toString();
        paramInt2 = -1;
        try
        {
          paramBoolean = localMtpObject1.isRoot();
          if (!paramBoolean) {
            try
            {
              paramInt2 = findInMedia(((Path)localObject2).getParent());
            }
            catch (RemoteException localRemoteException1)
            {
              break label499;
            }
          }
          if ((!localRemoteException1.isRoot()) && (paramInt2 == -1)) {
            localContentValues.put("format", Integer.valueOf(paramInt1));
          }
          label489:
          try
          {
            localContentValues.put("_size", Long.valueOf(localMtpObject2.getSize()));
            localContentValues.put("date_modified", Long.valueOf(localMtpObject2.getModifiedTime()));
            Uri localUri = mMediaProvider.insert(mObjectsUri, localContentValues);
            if (localUri != null)
            {
              rescanFile(((Path)localObject1).toString(), Integer.parseInt((String)localUri.getPathSegments().get(2)), paramInt1);
              break label489;
              mMediaProvider.update(mObjectsUri, localContentValues, "_data=?", new String[] { str });
            }
          }
          catch (RemoteException localRemoteException2) {}
          Log.e(TAG, "RemoteException in mMediaProvider.update", localRemoteException3);
        }
        catch (RemoteException localRemoteException3) {}
        label499:
        return;
        label512:
        deleteFromMedia((Path)localObject2, localMtpObject2.isDir());
        return;
      }
      return;
    }
    Log.e(TAG, "Failed to end move object");
  }
  
  private void endSendObject(int paramInt, boolean paramBoolean)
  {
    Object localObject = mManager.getObject(paramInt);
    if ((localObject != null) && (mManager.endSendObject((MtpStorageManager.MtpObject)localObject, paramBoolean)))
    {
      if (paramBoolean)
      {
        String str = ((MtpStorageManager.MtpObject)localObject).getPath().toString();
        int i = ((MtpStorageManager.MtpObject)localObject).getFormat();
        int j = str.lastIndexOf('.');
        paramInt = i;
        if (j >= 0)
        {
          paramInt = i;
          if (str.substring(j + 1).equalsIgnoreCase("mov"))
          {
            Log.e(TAG, String.format("attempt to put mov file in endSendObject: %s with format= 0x%04X", new Object[] { str, Integer.valueOf(i) }));
            paramInt = 12288;
          }
        }
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("_data", str);
        localContentValues.put("format", Integer.valueOf(paramInt));
        localContentValues.put("_size", Long.valueOf(((MtpStorageManager.MtpObject)localObject).getSize()));
        localContentValues.put("date_modified", Long.valueOf(((MtpStorageManager.MtpObject)localObject).getModifiedTime()));
        try
        {
          if (((MtpStorageManager.MtpObject)localObject).getParent().isRoot())
          {
            localContentValues.put("parent", Integer.valueOf(0));
          }
          else
          {
            i = findInMedia(((MtpStorageManager.MtpObject)localObject).getParent().getPath());
            if (i == -1) {
              break label282;
            }
            localContentValues.put("parent", Integer.valueOf(i));
          }
          localObject = mMediaProvider.insert(mObjectsUri, localContentValues);
          if (localObject != null) {
            rescanFile(str, Integer.parseInt((String)((Uri)localObject).getPathSegments().get(2)), paramInt);
          }
          break label297;
          label282:
          return;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e(TAG, "RemoteException in beginSendObject", localRemoteException);
        }
      }
      label297:
      return;
    }
    Log.e(TAG, "Failed to successfully end send object");
  }
  
  /* Error */
  private int findInMedia(Path paramPath)
  {
    // Byte code:
    //   0: iconst_m1
    //   1: istore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aload_0
    //   8: getfield 194	android/mtp/MtpDatabase:mMediaProvider	Landroid/content/ContentProviderClient;
    //   11: aload_0
    //   12: getfield 204	android/mtp/MtpDatabase:mObjectsUri	Landroid/net/Uri;
    //   15: getstatic 77	android/mtp/MtpDatabase:ID_PROJECTION	[Ljava/lang/String;
    //   18: ldc 25
    //   20: iconst_1
    //   21: anewarray 73	java/lang/String
    //   24: dup
    //   25: iconst_0
    //   26: aload_1
    //   27: invokeinterface 295 1 0
    //   32: aastore
    //   33: aconst_null
    //   34: aconst_null
    //   35: invokevirtual 534	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   38: astore 5
    //   40: iload_2
    //   41: istore 6
    //   43: aload 5
    //   45: ifnull +40 -> 85
    //   48: iload_2
    //   49: istore 6
    //   51: aload 5
    //   53: astore 4
    //   55: aload 5
    //   57: astore_3
    //   58: aload 5
    //   60: invokeinterface 539 1 0
    //   65: ifeq +20 -> 85
    //   68: aload 5
    //   70: astore 4
    //   72: aload 5
    //   74: astore_3
    //   75: aload 5
    //   77: iconst_0
    //   78: invokeinterface 541 2 0
    //   83: istore 6
    //   85: iload 6
    //   87: istore 7
    //   89: aload 5
    //   91: ifnull +110 -> 201
    //   94: aload 5
    //   96: astore_3
    //   97: aload_3
    //   98: invokeinterface 543 1 0
    //   103: iload 6
    //   105: istore 7
    //   107: goto +94 -> 201
    //   110: astore_1
    //   111: goto +93 -> 204
    //   114: astore 4
    //   116: aload_3
    //   117: astore 4
    //   119: getstatic 71	android/mtp/MtpDatabase:TAG	Ljava/lang/String;
    //   122: astore 8
    //   124: aload_3
    //   125: astore 4
    //   127: new 306	java/lang/StringBuilder
    //   130: astore 5
    //   132: aload_3
    //   133: astore 4
    //   135: aload 5
    //   137: invokespecial 307	java/lang/StringBuilder:<init>	()V
    //   140: aload_3
    //   141: astore 4
    //   143: aload 5
    //   145: ldc_w 545
    //   148: invokevirtual 316	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: aload_3
    //   153: astore 4
    //   155: aload 5
    //   157: aload_1
    //   158: invokevirtual 311	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: aload_3
    //   163: astore 4
    //   165: aload 5
    //   167: ldc_w 547
    //   170: invokevirtual 316	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: pop
    //   174: aload_3
    //   175: astore 4
    //   177: aload 8
    //   179: aload 5
    //   181: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: invokestatic 367	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   187: pop
    //   188: iload_2
    //   189: istore 7
    //   191: aload_3
    //   192: ifnull +9 -> 201
    //   195: iload_2
    //   196: istore 6
    //   198: goto -101 -> 97
    //   201: iload 7
    //   203: ireturn
    //   204: aload 4
    //   206: ifnull +10 -> 216
    //   209: aload 4
    //   211: invokeinterface 543 1 0
    //   216: aload_1
    //   217: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	218	0	this	MtpDatabase
    //   0	218	1	paramPath	Path
    //   1	195	2	i	int
    //   3	189	3	localObject1	Object
    //   5	66	4	localObject2	Object
    //   114	1	4	localRemoteException	RemoteException
    //   117	93	4	localObject3	Object
    //   38	142	5	localObject4	Object
    //   41	156	6	j	int
    //   87	115	7	k	int
    //   122	56	8	str	String
    // Exception table:
    //   from	to	target	type
    //   7	40	110	finally
    //   58	68	110	finally
    //   75	85	110	finally
    //   119	124	110	finally
    //   127	132	110	finally
    //   135	140	110	finally
    //   143	152	110	finally
    //   155	162	110	finally
    //   165	174	110	finally
    //   177	188	110	finally
    //   7	40	114	android/os/RemoteException
    //   58	68	114	android/os/RemoteException
    //   75	85	114	android/os/RemoteException
  }
  
  private int getDeviceProperty(int paramInt, long[] paramArrayOfLong, char[] paramArrayOfChar)
  {
    if (paramInt != 20481)
    {
      if (paramInt != 20483)
      {
        if (paramInt != 54279)
        {
          switch (paramInt)
          {
          default: 
            return 8202;
          }
          paramArrayOfLong = mDeviceProperties.getString(Integer.toString(paramInt), "");
          i = paramArrayOfLong.length();
          paramInt = i;
          if (i > 255) {
            paramInt = 255;
          }
          paramArrayOfLong.getChars(0, paramInt, paramArrayOfChar, 0);
          paramArrayOfChar[paramInt] = ((char)0);
          return 8193;
        }
        paramArrayOfLong[0] = mDeviceType;
        return 8193;
      }
      paramArrayOfLong = ((WindowManager)mContext.getSystemService("window")).getDefaultDisplay();
      paramInt = paramArrayOfLong.getMaximumSizeDimension();
      int i = paramArrayOfLong.getMaximumSizeDimension();
      paramArrayOfLong = new StringBuilder();
      paramArrayOfLong.append(Integer.toString(paramInt));
      paramArrayOfLong.append("x");
      paramArrayOfLong.append(Integer.toString(i));
      paramArrayOfLong = paramArrayOfLong.toString();
      paramArrayOfLong.getChars(0, paramArrayOfLong.length(), paramArrayOfChar, 0);
      paramArrayOfChar[paramArrayOfLong.length()] = ((char)0);
      return 8193;
    }
    paramArrayOfLong[0] = mBatteryLevel;
    paramArrayOfLong[1] = mBatteryScale;
    return 8193;
  }
  
  private int getNumObjects(int paramInt1, int paramInt2, int paramInt3)
  {
    Stream localStream = mManager.getObjects(paramInt3, paramInt2, paramInt1);
    if (localStream == null) {
      return -1;
    }
    return (int)localStream.count();
  }
  
  private int getObjectFilePath(int paramInt, char[] paramArrayOfChar, long[] paramArrayOfLong)
  {
    MtpStorageManager.MtpObject localMtpObject = mManager.getObject(paramInt);
    if (localMtpObject == null) {
      return 8201;
    }
    String str = localMtpObject.getPath().toString();
    paramInt = Integer.min(str.length(), 4096);
    str.getChars(0, paramInt, paramArrayOfChar, 0);
    paramArrayOfChar[paramInt] = ((char)0);
    paramArrayOfLong[0] = localMtpObject.getSize();
    paramArrayOfLong[1] = localMtpObject.getFormat();
    return 8193;
  }
  
  private int getObjectFormat(int paramInt)
  {
    MtpStorageManager.MtpObject localMtpObject = mManager.getObject(paramInt);
    if (localMtpObject == null) {
      return -1;
    }
    return localMtpObject.getFormat();
  }
  
  private boolean getObjectInfo(int paramInt, int[] paramArrayOfInt, char[] paramArrayOfChar, long[] paramArrayOfLong)
  {
    MtpStorageManager.MtpObject localMtpObject = mManager.getObject(paramInt);
    if (localMtpObject == null) {
      return false;
    }
    paramArrayOfInt[0] = localMtpObject.getStorageId();
    paramArrayOfInt[1] = localMtpObject.getFormat();
    if (localMtpObject.getParent().isRoot()) {
      paramInt = 0;
    } else {
      paramInt = localMtpObject.getParent().getId();
    }
    paramArrayOfInt[2] = paramInt;
    paramInt = Integer.min(localMtpObject.getName().length(), 255);
    localMtpObject.getName().getChars(0, paramInt, paramArrayOfChar, 0);
    paramArrayOfChar[paramInt] = ((char)0);
    paramArrayOfLong[0] = localMtpObject.getModifiedTime();
    paramArrayOfLong[1] = localMtpObject.getModifiedTime();
    return true;
  }
  
  private int[] getObjectList(int paramInt1, int paramInt2, int paramInt3)
  {
    Stream localStream = mManager.getObjects(paramInt3, paramInt2, paramInt1);
    if (localStream == null) {
      return null;
    }
    return localStream.mapToInt(_..Lambda.iwOv5HKUnGm7PVU3weoI9_JmsXc.INSTANCE).toArray();
  }
  
  private MtpPropertyList getObjectPropertyList(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = paramInt1;
    if (paramInt3 == 0)
    {
      if (paramInt4 == 0) {
        return new MtpPropertyList(8198);
      }
      return new MtpPropertyList(43015);
    }
    paramInt1 = i;
    paramInt4 = paramInt5;
    if (paramInt5 == -1) {
      if (i != 0)
      {
        paramInt1 = i;
        paramInt4 = paramInt5;
        if (i != -1) {}
      }
      else
      {
        paramInt1 = -1;
        paramInt4 = 0;
      }
    }
    if ((paramInt4 != 0) && (paramInt4 != 1)) {
      return new MtpPropertyList(43016);
    }
    Object localObject1 = Stream.of(new MtpStorageManager.MtpObject[0]);
    Object localObject2;
    if (paramInt1 == -1)
    {
      localObject1 = mManager.getObjects(0, paramInt2, -1);
      localObject2 = localObject1;
      if (localObject1 == null) {
        return new MtpPropertyList(8201);
      }
    }
    else
    {
      localObject2 = localObject1;
      if (paramInt1 != 0)
      {
        localObject3 = mManager.getObject(paramInt1);
        if (localObject3 == null) {
          return new MtpPropertyList(8201);
        }
        if (((MtpStorageManager.MtpObject)localObject3).getFormat() != paramInt2)
        {
          localObject2 = localObject1;
          if (paramInt2 != 0) {}
        }
        else
        {
          localObject2 = Stream.of(localObject3);
        }
      }
    }
    if (paramInt1 != 0)
    {
      localObject1 = localObject2;
      if (paramInt4 != 1) {}
    }
    else
    {
      paramInt4 = paramInt1;
      if (paramInt1 == 0) {
        paramInt4 = -1;
      }
      localObject1 = mManager.getObjects(paramInt4, paramInt2, -1);
      if (localObject1 == null) {
        return new MtpPropertyList(8201);
      }
      localObject1 = Stream.concat((Stream)localObject2, (Stream)localObject1);
    }
    Object localObject3 = new MtpPropertyList(8193);
    Iterator localIterator = ((Stream)localObject1).iterator();
    while (localIterator.hasNext())
    {
      MtpStorageManager.MtpObject localMtpObject = (MtpStorageManager.MtpObject)localIterator.next();
      if (paramInt3 == -1)
      {
        localObject1 = (MtpPropertyGroup)mPropertyGroupsByFormat.get(Integer.valueOf(localMtpObject.getFormat()));
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = getSupportedObjectProperties(paramInt2);
          localObject2 = new MtpPropertyGroup(mMediaProvider, mVolumeName, (int[])localObject2);
          mPropertyGroupsByFormat.put(Integer.valueOf(paramInt2), localObject2);
        }
      }
      else
      {
        localObject1 = (MtpPropertyGroup)mPropertyGroupsByProperty.get(Integer.valueOf(paramInt3));
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new MtpPropertyGroup(mMediaProvider, mVolumeName, new int[] { paramInt3 });
          mPropertyGroupsByProperty.put(Integer.valueOf(paramInt3), localObject2);
        }
      }
      paramInt1 = ((MtpPropertyGroup)localObject2).getPropertyList(localMtpObject, (MtpPropertyList)localObject3);
      if (paramInt1 != 8193) {
        return new MtpPropertyList(paramInt1);
      }
    }
    return localObject3;
  }
  
  /* Error */
  private int[] getObjectReferences(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 225	android/mtp/MtpDatabase:mManager	Landroid/mtp/MtpStorageManager;
    //   4: iload_1
    //   5: invokevirtual 263	android/mtp/MtpStorageManager:getObject	(I)Landroid/mtp/MtpStorageManager$MtpObject;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnonnull +5 -> 15
    //   13: aconst_null
    //   14: areturn
    //   15: aload_0
    //   16: aload_2
    //   17: invokevirtual 389	android/mtp/MtpStorageManager$MtpObject:getPath	()Ljava/nio/file/Path;
    //   20: invokespecial 456	android/mtp/MtpDatabase:findInMedia	(Ljava/nio/file/Path;)I
    //   23: istore_1
    //   24: iload_1
    //   25: iconst_m1
    //   26: if_icmpne +5 -> 31
    //   29: aconst_null
    //   30: areturn
    //   31: aload_0
    //   32: getfield 196	android/mtp/MtpDatabase:mVolumeName	Ljava/lang/String;
    //   35: iload_1
    //   36: i2l
    //   37: invokestatic 682	android/provider/MediaStore$Files:getMtpReferencesUri	(Ljava/lang/String;J)Landroid/net/Uri;
    //   40: astore_3
    //   41: aconst_null
    //   42: astore 4
    //   44: aconst_null
    //   45: astore_2
    //   46: aload_0
    //   47: getfield 194	android/mtp/MtpDatabase:mMediaProvider	Landroid/content/ContentProviderClient;
    //   50: aload_3
    //   51: getstatic 81	android/mtp/MtpDatabase:PATH_PROJECTION	[Ljava/lang/String;
    //   54: aconst_null
    //   55: aconst_null
    //   56: aconst_null
    //   57: aconst_null
    //   58: invokevirtual 534	android/content/ContentProviderClient:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/database/Cursor;
    //   61: astore_3
    //   62: aload_3
    //   63: ifnonnull +15 -> 78
    //   66: aload_3
    //   67: ifnull +9 -> 76
    //   70: aload_3
    //   71: invokeinterface 543 1 0
    //   76: aconst_null
    //   77: areturn
    //   78: aload_3
    //   79: astore_2
    //   80: aload_3
    //   81: astore 4
    //   83: new 684	java/util/ArrayList
    //   86: astore 5
    //   88: aload_3
    //   89: astore_2
    //   90: aload_3
    //   91: astore 4
    //   93: aload 5
    //   95: invokespecial 685	java/util/ArrayList:<init>	()V
    //   98: aload_3
    //   99: astore_2
    //   100: aload_3
    //   101: astore 4
    //   103: aload_3
    //   104: invokeinterface 539 1 0
    //   109: ifeq +60 -> 169
    //   112: aload_3
    //   113: astore_2
    //   114: aload_3
    //   115: astore 4
    //   117: aload_3
    //   118: iconst_0
    //   119: invokeinterface 687 2 0
    //   124: astore 6
    //   126: aload_3
    //   127: astore_2
    //   128: aload_3
    //   129: astore 4
    //   131: aload_0
    //   132: getfield 225	android/mtp/MtpDatabase:mManager	Landroid/mtp/MtpStorageManager;
    //   135: aload 6
    //   137: invokevirtual 691	android/mtp/MtpStorageManager:getByPath	(Ljava/lang/String;)Landroid/mtp/MtpStorageManager$MtpObject;
    //   140: astore 6
    //   142: aload 6
    //   144: ifnull +22 -> 166
    //   147: aload_3
    //   148: astore_2
    //   149: aload_3
    //   150: astore 4
    //   152: aload 5
    //   154: aload 6
    //   156: invokevirtual 607	android/mtp/MtpStorageManager$MtpObject:getId	()I
    //   159: invokestatic 409	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   162: invokevirtual 695	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   165: pop
    //   166: goto -68 -> 98
    //   169: aload_3
    //   170: astore_2
    //   171: aload_3
    //   172: astore 4
    //   174: aload 5
    //   176: invokevirtual 699	java/util/ArrayList:stream	()Ljava/util/stream/Stream;
    //   179: getstatic 704	android/mtp/_$$Lambda$MtpDatabase$UV1wDVoVlbcxpr8zevj_aMFtUGw:INSTANCE	Landroid/mtp/-$$Lambda$MtpDatabase$UV1wDVoVlbcxpr8zevj_aMFtUGw;
    //   182: invokeinterface 619 2 0
    //   187: invokeinterface 625 1 0
    //   192: astore 5
    //   194: aload_3
    //   195: ifnull +9 -> 204
    //   198: aload_3
    //   199: invokeinterface 543 1 0
    //   204: aload 5
    //   206: areturn
    //   207: astore 4
    //   209: goto +32 -> 241
    //   212: astore_3
    //   213: aload 4
    //   215: astore_2
    //   216: getstatic 71	android/mtp/MtpDatabase:TAG	Ljava/lang/String;
    //   219: ldc_w 706
    //   222: aload_3
    //   223: invokestatic 491	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   226: pop
    //   227: aload 4
    //   229: ifnull +10 -> 239
    //   232: aload 4
    //   234: invokeinterface 543 1 0
    //   239: aconst_null
    //   240: areturn
    //   241: aload_2
    //   242: ifnull +9 -> 251
    //   245: aload_2
    //   246: invokeinterface 543 1 0
    //   251: aload 4
    //   253: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	254	0	this	MtpDatabase
    //   0	254	1	paramInt	int
    //   8	238	2	localObject1	Object
    //   40	159	3	localObject2	Object
    //   212	11	3	localRemoteException	RemoteException
    //   42	131	4	localObject3	Object
    //   207	45	4	localObject4	Object
    //   86	119	5	localObject5	Object
    //   124	31	6	localObject6	Object
    // Exception table:
    //   from	to	target	type
    //   46	62	207	finally
    //   83	88	207	finally
    //   93	98	207	finally
    //   103	112	207	finally
    //   117	126	207	finally
    //   131	142	207	finally
    //   152	166	207	finally
    //   174	194	207	finally
    //   216	227	207	finally
    //   46	62	212	android/os/RemoteException
    //   83	88	212	android/os/RemoteException
    //   93	98	212	android/os/RemoteException
    //   103	112	212	android/os/RemoteException
    //   117	126	212	android/os/RemoteException
    //   131	142	212	android/os/RemoteException
    //   152	166	212	android/os/RemoteException
    //   174	194	212	android/os/RemoteException
  }
  
  private int[] getSupportedCaptureFormats()
  {
    return null;
  }
  
  private int[] getSupportedDeviceProperties()
  {
    return DEVICE_PROPERTIES;
  }
  
  private int[] getSupportedObjectProperties(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return FILE_PROPERTIES;
    case 14337: 
    case 14340: 
    case 14343: 
    case 14347: 
    case 14353: 
    case 14354: 
      return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(IMAGE_PROPERTIES)).toArray();
    case 12299: 
    case 47489: 
    case 47492: 
      return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(VIDEO_PROPERTIES)).toArray();
    }
    return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(AUDIO_PROPERTIES)).toArray();
  }
  
  private int[] getSupportedPlaybackFormats()
  {
    return PLAYBACK_FORMATS;
  }
  
  /* Error */
  private void initDeviceProperties(Context paramContext)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: ldc_w 719
    //   5: iconst_0
    //   6: invokevirtual 723	android/content/Context:getSharedPreferences	(Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   9: putfield 551	android/mtp/MtpDatabase:mDeviceProperties	Landroid/content/SharedPreferences;
    //   12: aload_1
    //   13: ldc_w 719
    //   16: invokevirtual 727	android/content/Context:getDatabasePath	(Ljava/lang/String;)Ljava/io/File;
    //   19: invokevirtual 732	java/io/File:exists	()Z
    //   22: ifeq +295 -> 317
    //   25: aconst_null
    //   26: astore_2
    //   27: aconst_null
    //   28: astore_3
    //   29: aconst_null
    //   30: astore 4
    //   32: aconst_null
    //   33: astore 5
    //   35: aconst_null
    //   36: astore 6
    //   38: aconst_null
    //   39: astore 7
    //   41: aconst_null
    //   42: astore 8
    //   44: aload_1
    //   45: ldc_w 719
    //   48: iconst_0
    //   49: aconst_null
    //   50: invokevirtual 736	android/content/Context:openOrCreateDatabase	(Ljava/lang/String;ILandroid/database/sqlite/SQLiteDatabase$CursorFactory;)Landroid/database/sqlite/SQLiteDatabase;
    //   53: astore 9
    //   55: aload 9
    //   57: astore 6
    //   59: aload 5
    //   61: astore_3
    //   62: aload 6
    //   64: ifnull +144 -> 208
    //   67: aload 8
    //   69: astore_3
    //   70: aload 6
    //   72: ldc_w 738
    //   75: iconst_3
    //   76: anewarray 73	java/lang/String
    //   79: dup
    //   80: iconst_0
    //   81: ldc 75
    //   83: aastore
    //   84: dup
    //   85: iconst_1
    //   86: ldc_w 740
    //   89: aastore
    //   90: dup
    //   91: iconst_2
    //   92: ldc_w 742
    //   95: aastore
    //   96: aconst_null
    //   97: aconst_null
    //   98: aconst_null
    //   99: aconst_null
    //   100: aconst_null
    //   101: invokevirtual 747	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   104: astore_2
    //   105: aload_2
    //   106: astore_3
    //   107: aload_2
    //   108: ifnull +100 -> 208
    //   111: aload_2
    //   112: astore_3
    //   113: aload_2
    //   114: astore 4
    //   116: aload_0
    //   117: getfield 551	android/mtp/MtpDatabase:mDeviceProperties	Landroid/content/SharedPreferences;
    //   120: invokeinterface 751 1 0
    //   125: astore 8
    //   127: aload_2
    //   128: astore_3
    //   129: aload_2
    //   130: astore 4
    //   132: aload_2
    //   133: invokeinterface 539 1 0
    //   138: ifeq +33 -> 171
    //   141: aload_2
    //   142: astore_3
    //   143: aload_2
    //   144: astore 4
    //   146: aload 8
    //   148: aload_2
    //   149: iconst_1
    //   150: invokeinterface 687 2 0
    //   155: aload_2
    //   156: iconst_2
    //   157: invokeinterface 687 2 0
    //   162: invokeinterface 757 3 0
    //   167: pop
    //   168: goto -41 -> 127
    //   171: aload_2
    //   172: astore_3
    //   173: aload_2
    //   174: astore 4
    //   176: aload 8
    //   178: invokeinterface 760 1 0
    //   183: pop
    //   184: aload_2
    //   185: astore_3
    //   186: goto +22 -> 208
    //   189: astore_1
    //   190: aload 6
    //   192: astore 4
    //   194: aload_3
    //   195: astore 6
    //   197: goto +96 -> 293
    //   200: astore 8
    //   202: aload 6
    //   204: astore_2
    //   205: goto +39 -> 244
    //   208: aload_3
    //   209: ifnull +9 -> 218
    //   212: aload_3
    //   213: invokeinterface 543 1 0
    //   218: aload 6
    //   220: ifnull +62 -> 282
    //   223: aload 6
    //   225: invokevirtual 761	android/database/sqlite/SQLiteDatabase:close	()V
    //   228: goto +54 -> 282
    //   231: astore_1
    //   232: aload_3
    //   233: astore 4
    //   235: goto +58 -> 293
    //   238: astore 8
    //   240: aload 7
    //   242: astore 4
    //   244: aload_2
    //   245: astore_3
    //   246: aload 4
    //   248: astore 6
    //   250: getstatic 71	android/mtp/MtpDatabase:TAG	Ljava/lang/String;
    //   253: ldc_w 763
    //   256: aload 8
    //   258: invokestatic 491	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   261: pop
    //   262: aload 4
    //   264: ifnull +10 -> 274
    //   267: aload 4
    //   269: invokeinterface 543 1 0
    //   274: aload_2
    //   275: ifnull +7 -> 282
    //   278: aload_2
    //   279: invokevirtual 761	android/database/sqlite/SQLiteDatabase:close	()V
    //   282: aload_1
    //   283: ldc_w 719
    //   286: invokevirtual 766	android/content/Context:deleteDatabase	(Ljava/lang/String;)Z
    //   289: pop
    //   290: goto +27 -> 317
    //   293: aload 6
    //   295: ifnull +10 -> 305
    //   298: aload 6
    //   300: invokeinterface 543 1 0
    //   305: aload 4
    //   307: ifnull +8 -> 315
    //   310: aload 4
    //   312: invokevirtual 761	android/database/sqlite/SQLiteDatabase:close	()V
    //   315: aload_1
    //   316: athrow
    //   317: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	318	0	this	MtpDatabase
    //   0	318	1	paramContext	Context
    //   26	253	2	localObject1	Object
    //   28	218	3	localObject2	Object
    //   30	281	4	localObject3	Object
    //   33	27	5	localObject4	Object
    //   36	263	6	localObject5	Object
    //   39	202	7	localObject6	Object
    //   42	135	8	localEditor	SharedPreferences.Editor
    //   200	1	8	localException1	Exception
    //   238	19	8	localException2	Exception
    //   53	3	9	localSQLiteDatabase	android.database.sqlite.SQLiteDatabase
    // Exception table:
    //   from	to	target	type
    //   70	105	189	finally
    //   116	127	189	finally
    //   132	141	189	finally
    //   146	168	189	finally
    //   176	184	189	finally
    //   70	105	200	java/lang/Exception
    //   116	127	200	java/lang/Exception
    //   132	141	200	java/lang/Exception
    //   146	168	200	java/lang/Exception
    //   176	184	200	java/lang/Exception
    //   44	55	231	finally
    //   250	262	231	finally
    //   44	55	238	java/lang/Exception
  }
  
  private final native void native_finalize();
  
  private final native void native_setup();
  
  private int renameFile(int paramInt, String paramString)
  {
    Object localObject = mManager.getObject(paramInt);
    if (localObject == null) {
      return 8201;
    }
    Path localPath = ((MtpStorageManager.MtpObject)localObject).getPath();
    if (!mManager.beginRenameObject((MtpStorageManager.MtpObject)localObject, paramString)) {
      return 8194;
    }
    paramString = ((MtpStorageManager.MtpObject)localObject).getPath();
    boolean bool = localPath.toFile().renameTo(paramString.toFile());
    try
    {
      Os.access(localPath.toString(), OsConstants.F_OK);
      Os.access(paramString.toString(), OsConstants.F_OK);
    }
    catch (ErrnoException localErrnoException) {}
    if (!mManager.endRenameObject((MtpStorageManager.MtpObject)localObject, localPath.getFileName().toString(), bool)) {
      Log.e(TAG, "Failed to end rename object");
    }
    if (!bool) {
      return 8194;
    }
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("_data", paramString.toString());
    String str2 = localPath.toString();
    try
    {
      mMediaProvider.update(mObjectsUri, localContentValues, "_data=?", new String[] { str2 });
    }
    catch (RemoteException localRemoteException3)
    {
      Log.e(TAG, "RemoteException in mMediaProvider.update", localRemoteException3);
    }
    StringBuilder localStringBuilder;
    if (((MtpStorageManager.MtpObject)localObject).isDir())
    {
      if ((localPath.getFileName().startsWith(".")) && (!paramString.startsWith("."))) {
        try
        {
          mMediaProvider.call("unhide", paramString.toString(), null);
        }
        catch (RemoteException localRemoteException1)
        {
          localObject = TAG;
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("failed to unhide/rescan for ");
          localStringBuilder.append(paramString);
          Log.e((String)localObject, localStringBuilder.toString());
        }
      }
    }
    else if ((localStringBuilder.getFileName().toString().toLowerCase(Locale.US).equals(".nomedia")) && (!paramString.getFileName().toString().toLowerCase(Locale.US).equals(".nomedia"))) {
      try
      {
        mMediaProvider.call("unhide", localStringBuilder.getParent().toString(), null);
      }
      catch (RemoteException localRemoteException2)
      {
        String str1 = TAG;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("failed to unhide/rescan for ");
        ((StringBuilder)localObject).append(paramString);
        Log.e(str1, ((StringBuilder)localObject).toString());
      }
    }
    return 8193;
  }
  
  private void rescanFile(String paramString, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 47621)
    {
      Object localObject1 = paramString;
      int i = ((String)localObject1).lastIndexOf('/');
      Object localObject2 = localObject1;
      if (i >= 0) {
        localObject2 = ((String)localObject1).substring(i + 1);
      }
      localObject1 = localObject2;
      if (((String)localObject2).endsWith(".pla")) {
        localObject1 = ((String)localObject2).substring(0, ((String)localObject2).length() - 4);
      }
      localObject2 = new ContentValues(1);
      ((ContentValues)localObject2).put("_data", paramString);
      ((ContentValues)localObject2).put("name", (String)localObject1);
      ((ContentValues)localObject2).put("format", Integer.valueOf(paramInt2));
      ((ContentValues)localObject2).put("date_modified", Long.valueOf(System.currentTimeMillis() / 1000L));
      ((ContentValues)localObject2).put("media_scanner_new_object_id", Integer.valueOf(paramInt1));
      try
      {
        mMediaProvider.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, (ContentValues)localObject2);
      }
      catch (RemoteException paramString)
      {
        Log.e(TAG, "RemoteException in endSendObject", paramString);
      }
    }
    else
    {
      mMediaScanner.scanMtpFile(paramString, paramInt1, paramInt2);
    }
  }
  
  private int setDeviceProperty(int paramInt, long paramLong, String paramString)
  {
    switch (paramInt)
    {
    default: 
      return 8202;
    }
    SharedPreferences.Editor localEditor = mDeviceProperties.edit();
    localEditor.putString(Integer.toString(paramInt), paramString);
    if (localEditor.commit()) {
      paramInt = 8193;
    } else {
      paramInt = 8194;
    }
    return paramInt;
  }
  
  private int setObjectProperty(int paramInt1, int paramInt2, long paramLong, String paramString)
  {
    if (paramInt2 != 56327) {
      return 43018;
    }
    return renameFile(paramInt1, paramString);
  }
  
  private int setObjectReferences(int paramInt, int[] paramArrayOfInt)
  {
    Object localObject1 = mManager.getObject(paramInt);
    if (localObject1 == null) {
      return 8201;
    }
    paramInt = findInMedia(((MtpStorageManager.MtpObject)localObject1).getPath());
    if (paramInt == -1) {
      return 8194;
    }
    localObject1 = MediaStore.Files.getMtpReferencesUri(mVolumeName, paramInt);
    ArrayList localArrayList = new ArrayList();
    int i = paramArrayOfInt.length;
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      int j = paramArrayOfInt[paramInt];
      Object localObject2 = mManager.getObject(j);
      if (localObject2 != null)
      {
        j = findInMedia(((MtpStorageManager.MtpObject)localObject2).getPath());
        if (j != -1)
        {
          localObject2 = new ContentValues();
          ((ContentValues)localObject2).put("_id", Integer.valueOf(j));
          localArrayList.add(localObject2);
        }
      }
    }
    try
    {
      paramInt = mMediaProvider.bulkInsert((Uri)localObject1, (ContentValues[])localArrayList.toArray(new ContentValues[0]));
      if (paramInt > 0) {
        return 8193;
      }
    }
    catch (RemoteException paramArrayOfInt)
    {
      Log.e(TAG, "RemoteException in setObjectReferences", paramArrayOfInt);
    }
    return 8194;
  }
  
  public void addStorage(StorageVolume paramStorageVolume)
  {
    MtpStorage localMtpStorage = mManager.addMtpStorage(paramStorageVolume);
    mStorageMap.put(paramStorageVolume.getPath(), localMtpStorage);
    if (mServer != null) {
      mServer.addStorage(localMtpStorage);
    }
  }
  
  public void close()
  {
    mManager.close();
    mCloseGuard.close();
    if (mClosed.compareAndSet(false, true))
    {
      mMediaScanner.close();
      if (mMediaProvider != null) {
        mMediaProvider.close();
      }
      native_finalize();
    }
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
  
  public void removeStorage(StorageVolume paramStorageVolume)
  {
    MtpStorage localMtpStorage = (MtpStorage)mStorageMap.get(paramStorageVolume.getPath());
    if (localMtpStorage == null) {
      return;
    }
    if (mServer != null) {
      mServer.removeStorage(localMtpStorage);
    }
    mManager.removeMtpStorage(localMtpStorage);
    mStorageMap.remove(paramStorageVolume.getPath());
  }
  
  public void setServer(MtpServer paramMtpServer)
  {
    mServer = paramMtpServer;
    try
    {
      mContext.unregisterReceiver(mBatteryReceiver);
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    if (paramMtpServer != null) {
      mContext.registerReceiver(mBatteryReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }
  }
}
