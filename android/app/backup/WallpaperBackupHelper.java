package android.app.backup;

import android.app.WallpaperManager;
import android.content.Context;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import java.io.File;

public class WallpaperBackupHelper
  extends FileBackupHelperBase
  implements BackupHelper
{
  private static final boolean DEBUG = false;
  private static final String STAGE_FILE = new File(Environment.getUserSystemDirectory(0), "wallpaper-tmp").getAbsolutePath();
  private static final String TAG = "WallpaperBackupHelper";
  public static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
  public static final String WALLPAPER_INFO_KEY = "/data/system/wallpaper_info.xml";
  private final String[] mKeys;
  private final WallpaperManager mWpm;
  
  public WallpaperBackupHelper(Context paramContext, String[] paramArrayOfString)
  {
    super(paramContext);
    mContext = paramContext;
    mKeys = paramArrayOfString;
    mWpm = ((WallpaperManager)paramContext.getSystemService("wallpaper"));
  }
  
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2) {}
  
  /* Error */
  public void restoreEntity(BackupDataInputStream paramBackupDataInputStream)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 82	android/app/backup/BackupDataInputStream:getKey	()Ljava/lang/String;
    //   4: astore_2
    //   5: aload_0
    //   6: aload_2
    //   7: aload_0
    //   8: getfield 57	android/app/backup/WallpaperBackupHelper:mKeys	[Ljava/lang/String;
    //   11: invokevirtual 86	android/app/backup/WallpaperBackupHelper:isKeyInList	(Ljava/lang/String;[Ljava/lang/String;)Z
    //   14: ifeq +167 -> 181
    //   17: aload_2
    //   18: ldc 17
    //   20: invokevirtual 92	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   23: ifeq +158 -> 181
    //   26: new 28	java/io/File
    //   29: dup
    //   30: getstatic 46	android/app/backup/WallpaperBackupHelper:STAGE_FILE	Ljava/lang/String;
    //   33: invokespecial 95	java/io/File:<init>	(Ljava/lang/String;)V
    //   36: astore_3
    //   37: aload_0
    //   38: aload_3
    //   39: aload_1
    //   40: invokevirtual 99	android/app/backup/WallpaperBackupHelper:writeFile	(Ljava/io/File;Landroid/app/backup/BackupDataInputStream;)Z
    //   43: istore 4
    //   45: iload 4
    //   47: ifeq +110 -> 157
    //   50: new 101	java/io/FileInputStream
    //   53: astore 5
    //   55: aload 5
    //   57: aload_3
    //   58: invokespecial 104	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   61: aconst_null
    //   62: astore_1
    //   63: aload_0
    //   64: getfield 69	android/app/backup/WallpaperBackupHelper:mWpm	Landroid/app/WallpaperManager;
    //   67: aload 5
    //   69: invokevirtual 108	android/app/WallpaperManager:setStream	(Ljava/io/InputStream;)V
    //   72: aload 5
    //   74: invokevirtual 111	java/io/FileInputStream:close	()V
    //   77: goto +88 -> 165
    //   80: astore_2
    //   81: goto +8 -> 89
    //   84: astore_2
    //   85: aload_2
    //   86: astore_1
    //   87: aload_2
    //   88: athrow
    //   89: aload_1
    //   90: ifnull +22 -> 112
    //   93: aload 5
    //   95: invokevirtual 111	java/io/FileInputStream:close	()V
    //   98: goto +19 -> 117
    //   101: astore 5
    //   103: aload_1
    //   104: aload 5
    //   106: invokevirtual 115	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   109: goto +8 -> 117
    //   112: aload 5
    //   114: invokevirtual 111	java/io/FileInputStream:close	()V
    //   117: aload_2
    //   118: athrow
    //   119: astore_2
    //   120: new 117	java/lang/StringBuilder
    //   123: astore_1
    //   124: aload_1
    //   125: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   128: aload_1
    //   129: ldc 121
    //   131: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   134: pop
    //   135: aload_1
    //   136: aload_2
    //   137: invokevirtual 128	java/io/IOException:getMessage	()Ljava/lang/String;
    //   140: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: pop
    //   144: ldc 14
    //   146: aload_1
    //   147: invokevirtual 131	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   150: invokestatic 137	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   153: pop
    //   154: goto -77 -> 77
    //   157: ldc 14
    //   159: ldc -117
    //   161: invokestatic 137	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   164: pop
    //   165: aload_3
    //   166: invokevirtual 143	java/io/File:delete	()Z
    //   169: pop
    //   170: goto +11 -> 181
    //   173: astore_1
    //   174: aload_3
    //   175: invokevirtual 143	java/io/File:delete	()Z
    //   178: pop
    //   179: aload_1
    //   180: athrow
    //   181: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	182	0	this	WallpaperBackupHelper
    //   0	182	1	paramBackupDataInputStream	BackupDataInputStream
    //   4	14	2	str	String
    //   80	1	2	localObject	Object
    //   84	34	2	localThrowable1	Throwable
    //   119	18	2	localIOException	java.io.IOException
    //   36	139	3	localFile	File
    //   43	3	4	bool	boolean
    //   53	41	5	localFileInputStream	java.io.FileInputStream
    //   101	12	5	localThrowable2	Throwable
    // Exception table:
    //   from	to	target	type
    //   63	72	80	finally
    //   87	89	80	finally
    //   63	72	84	java/lang/Throwable
    //   93	98	101	java/lang/Throwable
    //   50	61	119	java/io/IOException
    //   72	77	119	java/io/IOException
    //   93	98	119	java/io/IOException
    //   103	109	119	java/io/IOException
    //   112	117	119	java/io/IOException
    //   117	119	119	java/io/IOException
    //   37	45	173	finally
    //   50	61	173	finally
    //   72	77	173	finally
    //   93	98	173	finally
    //   103	109	173	finally
    //   112	117	173	finally
    //   117	119	173	finally
    //   120	154	173	finally
    //   157	165	173	finally
  }
}
