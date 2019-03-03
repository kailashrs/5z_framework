package android.app;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadSystemException;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.IWindowManager;
import android.view.IWindowSession;
import android.view.WindowManagerGlobal;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WallpaperManager
{
  public static final String ACTION_CHANGE_LIVE_WALLPAPER = "android.service.wallpaper.CHANGE_LIVE_WALLPAPER";
  public static final String ACTION_CROP_AND_SET_WALLPAPER = "android.service.wallpaper.CROP_AND_SET_WALLPAPER";
  public static final String ACTION_LIVE_WALLPAPER_CHOOSER = "android.service.wallpaper.LIVE_WALLPAPER_CHOOSER";
  private static final String AMAX_FINGER_PRINT = "6b16979905b73b62dc0aa4c038149cca5a1df0ad";
  public static final String COMMAND_DROP = "android.home.drop";
  public static final String COMMAND_SECONDARY_TAP = "android.wallpaper.secondaryTap";
  public static final String COMMAND_TAP = "android.wallpaper.tap";
  private static boolean DEBUG = false;
  public static final String EXTRA_LIVE_WALLPAPER_COMPONENT = "android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT";
  public static final String EXTRA_NEW_WALLPAPER_ID = "android.service.wallpaper.extra.ID";
  public static final int FLAG_LOCK = 2;
  public static final int FLAG_SYSTEM = 1;
  private static final String PROP_LOCK_WALLPAPER = "ro.config.lock_wallpaper";
  private static final String PROP_WALLPAPER = "ro.config.wallpaper";
  private static final String PROP_WALLPAPER_COMPONENT = "ro.config.wallpaper_component";
  private static String TAG = "WallpaperManager";
  private static final String WALLPAPER_OPTION_ACTION = "com.asus.lockscreen.wallpaper.option.setting";
  private static final String WALLPAPER_OPTION_COMPONENT = "com.asus.lockscreen2/.slideshow.wallpaperoption.WallpaperOptionSettingService";
  private static final String WALLPAPER_OPTION_EXTRA_OPTION = "option";
  private static final String WALLPAPER_OPTION_EXTRA_WHICH = "which";
  private static final String WALLPAPER_OPTION_EXTRA_WHO = "who";
  private static final int WALLPAPER_OPTION_NORMAL = 0;
  private static final String WALLPAPER_OPTION_PACKAGE = "com.asus.lockscreen2";
  private static final String WALLPAPER_OPTION_SUPPORT_JOB_INTENTSERVICE = "support_job_intentservice";
  private static final String WALLPAPER_OPTION_WALLPAPER_MANAGER = "WallpaperManager";
  public static final String WALLPAPER_PREVIEW_META_DATA = "android.wallpaper.preview";
  private static Globals sGlobals;
  private static final Object sSync = new Object[0];
  private final Context mContext;
  private float mWallpaperXStep = -1.0F;
  private float mWallpaperYStep = -1.0F;
  
  WallpaperManager(IWallpaperManager paramIWallpaperManager, Context paramContext, Handler paramHandler)
  {
    mContext = paramContext;
    initGlobals(paramIWallpaperManager, paramContext.getMainLooper());
  }
  
  /* Error */
  private void copyBitmapToWallpaperFileProtected(Bitmap paramBitmap, FileOutputStream paramFileOutputStream)
  {
    // Byte code:
    //   0: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   3: lstore_3
    //   4: aconst_null
    //   5: astore 5
    //   7: aconst_null
    //   8: astore 6
    //   10: aload 6
    //   12: astore 7
    //   14: aload 5
    //   16: astore 8
    //   18: aload_0
    //   19: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   22: invokevirtual 161	android/content/Context:getUserId	()I
    //   25: invokestatic 141	android/app/WallpaperManager:getKey	(I)[[B
    //   28: astore 9
    //   30: aload 6
    //   32: astore 7
    //   34: aload 5
    //   36: astore 8
    //   38: ldc -93
    //   40: invokestatic 169	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
    //   43: aload 9
    //   45: iconst_0
    //   46: aaload
    //   47: invokevirtual 173	java/security/MessageDigest:digest	([B)[B
    //   50: astore 10
    //   52: aload 9
    //   54: iconst_1
    //   55: aaload
    //   56: astore 9
    //   58: aload 6
    //   60: astore 7
    //   62: aload 5
    //   64: astore 8
    //   66: new 175	javax/crypto/spec/SecretKeySpec
    //   69: astore 11
    //   71: aload 6
    //   73: astore 7
    //   75: aload 5
    //   77: astore 8
    //   79: aload 11
    //   81: aload 10
    //   83: ldc -79
    //   85: invokespecial 180	javax/crypto/spec/SecretKeySpec:<init>	([BLjava/lang/String;)V
    //   88: aload 6
    //   90: astore 7
    //   92: aload 5
    //   94: astore 8
    //   96: ldc -74
    //   98: invokestatic 187	javax/crypto/Cipher:getInstance	(Ljava/lang/String;)Ljavax/crypto/Cipher;
    //   101: astore 10
    //   103: aload 6
    //   105: astore 7
    //   107: aload 5
    //   109: astore 8
    //   111: new 189	javax/crypto/spec/IvParameterSpec
    //   114: astore 12
    //   116: aload 6
    //   118: astore 7
    //   120: aload 5
    //   122: astore 8
    //   124: aload 12
    //   126: aload 9
    //   128: invokespecial 192	javax/crypto/spec/IvParameterSpec:<init>	([B)V
    //   131: aload 6
    //   133: astore 7
    //   135: aload 5
    //   137: astore 8
    //   139: aload 10
    //   141: iconst_1
    //   142: aload 11
    //   144: aload 12
    //   146: invokevirtual 196	javax/crypto/Cipher:init	(ILjava/security/Key;Ljava/security/spec/AlgorithmParameterSpec;)V
    //   149: aload 6
    //   151: astore 7
    //   153: aload 5
    //   155: astore 8
    //   157: new 198	javax/crypto/CipherOutputStream
    //   160: astore 9
    //   162: aload 6
    //   164: astore 7
    //   166: aload 5
    //   168: astore 8
    //   170: aload 9
    //   172: aload_2
    //   173: aload 10
    //   175: invokespecial 201	javax/crypto/CipherOutputStream:<init>	(Ljava/io/OutputStream;Ljavax/crypto/Cipher;)V
    //   178: aload 9
    //   180: astore 6
    //   182: aload 6
    //   184: astore 7
    //   186: aload 6
    //   188: astore 8
    //   190: aload_1
    //   191: getstatic 207	android/graphics/Bitmap$CompressFormat:PNG	Landroid/graphics/Bitmap$CompressFormat;
    //   194: bipush 90
    //   196: aload 6
    //   198: invokevirtual 213	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   201: pop
    //   202: aload 6
    //   204: astore 7
    //   206: aload 6
    //   208: astore 8
    //   210: aload_2
    //   211: invokevirtual 218	java/io/FileOutputStream:flush	()V
    //   214: aload 6
    //   216: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   219: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   222: lstore 13
    //   224: getstatic 110	android/app/WallpaperManager:DEBUG	Z
    //   227: ifeq +151 -> 378
    //   230: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   233: astore_1
    //   234: new 226	java/lang/StringBuilder
    //   237: dup
    //   238: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   241: astore_2
    //   242: goto +101 -> 343
    //   245: astore_1
    //   246: goto +133 -> 379
    //   249: astore 6
    //   251: aload 8
    //   253: astore 7
    //   255: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   258: astore_2
    //   259: aload 8
    //   261: astore 7
    //   263: new 226	java/lang/StringBuilder
    //   266: astore_1
    //   267: aload 8
    //   269: astore 7
    //   271: aload_1
    //   272: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   275: aload 8
    //   277: astore 7
    //   279: aload_1
    //   280: ldc -27
    //   282: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: aload 8
    //   288: astore 7
    //   290: aload_1
    //   291: aload 6
    //   293: invokevirtual 236	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   296: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   299: pop
    //   300: aload 8
    //   302: astore 7
    //   304: aload_2
    //   305: aload_1
    //   306: invokevirtual 239	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   309: aload 6
    //   311: invokestatic 245	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   314: pop
    //   315: aload 8
    //   317: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   320: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   323: lstore 13
    //   325: getstatic 110	android/app/WallpaperManager:DEBUG	Z
    //   328: ifeq +50 -> 378
    //   331: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   334: astore_1
    //   335: new 226	java/lang/StringBuilder
    //   338: dup
    //   339: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   342: astore_2
    //   343: aload_2
    //   344: ldc -9
    //   346: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   349: pop
    //   350: aload_2
    //   351: lload 13
    //   353: lload_3
    //   354: lsub
    //   355: invokestatic 253	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   358: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: pop
    //   362: aload_2
    //   363: ldc -1
    //   365: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   368: pop
    //   369: aload_1
    //   370: aload_2
    //   371: invokevirtual 239	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   374: invokestatic 259	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   377: pop
    //   378: return
    //   379: aload 7
    //   381: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   384: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   387: lstore 13
    //   389: getstatic 110	android/app/WallpaperManager:DEBUG	Z
    //   392: ifeq +55 -> 447
    //   395: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   398: astore_2
    //   399: new 226	java/lang/StringBuilder
    //   402: dup
    //   403: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   406: astore 7
    //   408: aload 7
    //   410: ldc -9
    //   412: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   415: pop
    //   416: aload 7
    //   418: lload 13
    //   420: lload_3
    //   421: lsub
    //   422: invokestatic 253	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   425: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   428: pop
    //   429: aload 7
    //   431: ldc -1
    //   433: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   436: pop
    //   437: aload_2
    //   438: aload 7
    //   440: invokevirtual 239	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   443: invokestatic 259	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   446: pop
    //   447: aload_1
    //   448: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	449	0	this	WallpaperManager
    //   0	449	1	paramBitmap	Bitmap
    //   0	449	2	paramFileOutputStream	FileOutputStream
    //   3	418	3	l1	long
    //   5	162	5	localObject1	Object
    //   8	207	6	localObject2	Object
    //   249	61	6	localException	Exception
    //   12	427	7	localObject3	Object
    //   16	300	8	localObject4	Object
    //   28	151	9	localObject5	Object
    //   50	124	10	localObject6	Object
    //   69	74	11	localSecretKeySpec	javax.crypto.spec.SecretKeySpec
    //   114	31	12	localIvParameterSpec	javax.crypto.spec.IvParameterSpec
    //   222	197	13	l2	long
    // Exception table:
    //   from	to	target	type
    //   18	30	245	finally
    //   38	52	245	finally
    //   66	71	245	finally
    //   79	88	245	finally
    //   96	103	245	finally
    //   111	116	245	finally
    //   124	131	245	finally
    //   139	149	245	finally
    //   157	162	245	finally
    //   170	178	245	finally
    //   190	202	245	finally
    //   210	214	245	finally
    //   255	259	245	finally
    //   263	267	245	finally
    //   271	275	245	finally
    //   279	286	245	finally
    //   290	300	245	finally
    //   304	315	245	finally
    //   18	30	249	java/lang/Exception
    //   38	52	249	java/lang/Exception
    //   66	71	249	java/lang/Exception
    //   79	88	249	java/lang/Exception
    //   96	103	249	java/lang/Exception
    //   111	116	249	java/lang/Exception
    //   124	131	249	java/lang/Exception
    //   139	149	249	java/lang/Exception
    //   157	162	249	java/lang/Exception
    //   170	178	249	java/lang/Exception
    //   190	202	249	java/lang/Exception
    //   210	214	249	java/lang/Exception
  }
  
  private void copyStreamToWallpaperFile(InputStream paramInputStream, FileOutputStream paramFileOutputStream)
    throws IOException
  {
    FileUtils.copy(paramInputStream, paramFileOutputStream);
  }
  
  /* Error */
  private void copyStreamToWallpaperFileProtected(InputStream paramInputStream, FileOutputStream paramFileOutputStream)
  {
    // Byte code:
    //   0: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   3: lstore_3
    //   4: aconst_null
    //   5: astore 5
    //   7: aconst_null
    //   8: astore 6
    //   10: aload 6
    //   12: astore 7
    //   14: aload 5
    //   16: astore 8
    //   18: aload_0
    //   19: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   22: invokevirtual 161	android/content/Context:getUserId	()I
    //   25: invokestatic 141	android/app/WallpaperManager:getKey	(I)[[B
    //   28: astore 9
    //   30: aload 6
    //   32: astore 7
    //   34: aload 5
    //   36: astore 8
    //   38: ldc -93
    //   40: invokestatic 169	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
    //   43: aload 9
    //   45: iconst_0
    //   46: aaload
    //   47: invokevirtual 173	java/security/MessageDigest:digest	([B)[B
    //   50: astore 10
    //   52: aload 9
    //   54: iconst_1
    //   55: aaload
    //   56: astore 11
    //   58: aload 6
    //   60: astore 7
    //   62: aload 5
    //   64: astore 8
    //   66: new 175	javax/crypto/spec/SecretKeySpec
    //   69: astore 9
    //   71: aload 6
    //   73: astore 7
    //   75: aload 5
    //   77: astore 8
    //   79: aload 9
    //   81: aload 10
    //   83: ldc -79
    //   85: invokespecial 180	javax/crypto/spec/SecretKeySpec:<init>	([BLjava/lang/String;)V
    //   88: aload 6
    //   90: astore 7
    //   92: aload 5
    //   94: astore 8
    //   96: ldc -74
    //   98: invokestatic 187	javax/crypto/Cipher:getInstance	(Ljava/lang/String;)Ljavax/crypto/Cipher;
    //   101: astore 10
    //   103: aload 6
    //   105: astore 7
    //   107: aload 5
    //   109: astore 8
    //   111: new 189	javax/crypto/spec/IvParameterSpec
    //   114: astore 12
    //   116: aload 6
    //   118: astore 7
    //   120: aload 5
    //   122: astore 8
    //   124: aload 12
    //   126: aload 11
    //   128: invokespecial 192	javax/crypto/spec/IvParameterSpec:<init>	([B)V
    //   131: aload 6
    //   133: astore 7
    //   135: aload 5
    //   137: astore 8
    //   139: aload 10
    //   141: iconst_1
    //   142: aload 9
    //   144: aload 12
    //   146: invokevirtual 196	javax/crypto/Cipher:init	(ILjava/security/Key;Ljava/security/spec/AlgorithmParameterSpec;)V
    //   149: aload 6
    //   151: astore 7
    //   153: aload 5
    //   155: astore 8
    //   157: new 198	javax/crypto/CipherOutputStream
    //   160: astore 9
    //   162: aload 6
    //   164: astore 7
    //   166: aload 5
    //   168: astore 8
    //   170: aload 9
    //   172: aload_2
    //   173: aload 10
    //   175: invokespecial 201	javax/crypto/CipherOutputStream:<init>	(Ljava/io/OutputStream;Ljavax/crypto/Cipher;)V
    //   178: aload 9
    //   180: astore 6
    //   182: aload 6
    //   184: astore 7
    //   186: aload 6
    //   188: astore 8
    //   190: ldc_w 272
    //   193: newarray byte
    //   195: astore 5
    //   197: aload 6
    //   199: astore 7
    //   201: aload 6
    //   203: astore 8
    //   205: aload_1
    //   206: aload 5
    //   208: invokevirtual 278	java/io/InputStream:read	([B)I
    //   211: istore 13
    //   213: iload 13
    //   215: ifle +24 -> 239
    //   218: aload 6
    //   220: astore 7
    //   222: aload 6
    //   224: astore 8
    //   226: aload 6
    //   228: aload 5
    //   230: iconst_0
    //   231: iload 13
    //   233: invokevirtual 282	javax/crypto/CipherOutputStream:write	([BII)V
    //   236: goto -39 -> 197
    //   239: aload 6
    //   241: astore 7
    //   243: aload 6
    //   245: astore 8
    //   247: aload_2
    //   248: invokevirtual 218	java/io/FileOutputStream:flush	()V
    //   251: aload 6
    //   253: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   256: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   259: lstore 14
    //   261: getstatic 110	android/app/WallpaperManager:DEBUG	Z
    //   264: ifeq +150 -> 414
    //   267: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   270: astore_1
    //   271: new 226	java/lang/StringBuilder
    //   274: dup
    //   275: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   278: astore_2
    //   279: goto +100 -> 379
    //   282: astore_1
    //   283: goto +132 -> 415
    //   286: astore_2
    //   287: aload 8
    //   289: astore 7
    //   291: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   294: astore 6
    //   296: aload 8
    //   298: astore 7
    //   300: new 226	java/lang/StringBuilder
    //   303: astore_1
    //   304: aload 8
    //   306: astore 7
    //   308: aload_1
    //   309: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   312: aload 8
    //   314: astore 7
    //   316: aload_1
    //   317: ldc -27
    //   319: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   322: pop
    //   323: aload 8
    //   325: astore 7
    //   327: aload_1
    //   328: aload_2
    //   329: invokevirtual 236	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   332: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   335: pop
    //   336: aload 8
    //   338: astore 7
    //   340: aload 6
    //   342: aload_1
    //   343: invokevirtual 239	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   346: aload_2
    //   347: invokestatic 245	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   350: pop
    //   351: aload 8
    //   353: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   356: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   359: lstore 14
    //   361: getstatic 110	android/app/WallpaperManager:DEBUG	Z
    //   364: ifeq +50 -> 414
    //   367: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   370: astore_1
    //   371: new 226	java/lang/StringBuilder
    //   374: dup
    //   375: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   378: astore_2
    //   379: aload_2
    //   380: ldc -9
    //   382: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   385: pop
    //   386: aload_2
    //   387: lload 14
    //   389: lload_3
    //   390: lsub
    //   391: invokestatic 253	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   394: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   397: pop
    //   398: aload_2
    //   399: ldc -1
    //   401: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   404: pop
    //   405: aload_1
    //   406: aload_2
    //   407: invokevirtual 239	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   410: invokestatic 259	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   413: pop
    //   414: return
    //   415: aload 7
    //   417: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   420: invokestatic 157	java/lang/System:currentTimeMillis	()J
    //   423: lstore 14
    //   425: getstatic 110	android/app/WallpaperManager:DEBUG	Z
    //   428: ifeq +52 -> 480
    //   431: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   434: astore 7
    //   436: new 226	java/lang/StringBuilder
    //   439: dup
    //   440: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   443: astore_2
    //   444: aload_2
    //   445: ldc -9
    //   447: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   450: pop
    //   451: aload_2
    //   452: lload 14
    //   454: lload_3
    //   455: lsub
    //   456: invokestatic 253	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   459: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   462: pop
    //   463: aload_2
    //   464: ldc -1
    //   466: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   469: pop
    //   470: aload 7
    //   472: aload_2
    //   473: invokevirtual 239	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   476: invokestatic 259	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   479: pop
    //   480: aload_1
    //   481: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	482	0	this	WallpaperManager
    //   0	482	1	paramInputStream	InputStream
    //   0	482	2	paramFileOutputStream	FileOutputStream
    //   3	452	3	l1	long
    //   5	224	5	arrayOfByte1	byte[]
    //   8	333	6	localObject1	Object
    //   12	459	7	localObject2	Object
    //   16	336	8	localObject3	Object
    //   28	151	9	localObject4	Object
    //   50	124	10	localObject5	Object
    //   56	71	11	arrayOfByte2	byte[]
    //   114	31	12	localIvParameterSpec	javax.crypto.spec.IvParameterSpec
    //   211	21	13	i	int
    //   259	194	14	l2	long
    // Exception table:
    //   from	to	target	type
    //   18	30	282	finally
    //   38	52	282	finally
    //   66	71	282	finally
    //   79	88	282	finally
    //   96	103	282	finally
    //   111	116	282	finally
    //   124	131	282	finally
    //   139	149	282	finally
    //   157	162	282	finally
    //   170	178	282	finally
    //   190	197	282	finally
    //   205	213	282	finally
    //   226	236	282	finally
    //   247	251	282	finally
    //   291	296	282	finally
    //   300	304	282	finally
    //   308	312	282	finally
    //   316	323	282	finally
    //   327	336	282	finally
    //   340	351	282	finally
    //   18	30	286	java/lang/Exception
    //   38	52	286	java/lang/Exception
    //   66	71	286	java/lang/Exception
    //   79	88	286	java/lang/Exception
    //   96	103	286	java/lang/Exception
    //   111	116	286	java/lang/Exception
    //   124	131	286	java/lang/Exception
    //   139	149	286	java/lang/Exception
    //   157	162	286	java/lang/Exception
    //   170	178	286	java/lang/Exception
    //   190	197	286	java/lang/Exception
    //   205	213	286	java/lang/Exception
    //   226	236	286	java/lang/Exception
    //   247	251	286	java/lang/Exception
  }
  
  public static ComponentName getDefaultWallpaperComponent(Context paramContext)
  {
    Object localObject = SystemProperties.get("ro.config.wallpaper_component");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = ComponentName.unflattenFromString((String)localObject);
      if (localObject != null) {
        return localObject;
      }
    }
    paramContext = paramContext.getString(17039875);
    if (!TextUtils.isEmpty(paramContext))
    {
      paramContext = ComponentName.unflattenFromString(paramContext);
      if (paramContext != null) {
        return paramContext;
      }
    }
    return null;
  }
  
  public static WallpaperManager getInstance(Context paramContext)
  {
    return (WallpaperManager)paramContext.getSystemService("wallpaper");
  }
  
  private static byte[][] getKey(int paramInt)
  {
    byte[][] arrayOfByte1 = new byte[2][];
    try
    {
      byte[][] arrayOfByte2 = new byte[3][];
      arrayOfByte2[0] = MessageDigest.getInstance("SHA1").digest(String.valueOf(paramInt).getBytes("UTF-8"));
      arrayOfByte2[1] = { 6, 12, 4, 13, 5, 3, 8, 14, 0, 15, 11, 2, 7, 9, 1, 10 };
      arrayOfByte2[2] = { 13, 3, 1, 0, 10, 7, 5, 4, 12, 6, 2, 11, 14, 8, 15, 9 };
      byte[] arrayOfByte = new byte[(arrayOfByte2[2][(arrayOfByte2[0][(arrayOfByte2[0].length / 2)] & 0xF)] << 4 | arrayOfByte2[1][((arrayOfByte2[0][(arrayOfByte2[0].length / 2)] & 0xF0) >>> 4)]) + 31];
      int i;
      for (paramInt = arrayOfByte.length;; paramInt = i)
      {
        i = paramInt - 1;
        if (paramInt <= 0) {
          break;
        }
        paramInt = (byte)(arrayOfByte2[0][(i % arrayOfByte2[0].length)] + i);
        arrayOfByte[i] = ((byte)(byte)(arrayOfByte2[1][(paramInt & 0xF)] << 4 | arrayOfByte2[2][((paramInt & 0xF0) >>> 4)]));
      }
      arrayOfByte1[0] = Arrays.copyOfRange(arrayOfByte, 0, arrayOfByte.length - 16);
      arrayOfByte1[1] = Arrays.copyOfRange(arrayOfByte, arrayOfByte1[0].length, arrayOfByte.length);
    }
    catch (Exception localException) {}
    return arrayOfByte1;
  }
  
  private static RectF getMaxCropRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2)
  {
    RectF localRectF = new RectF();
    if (paramInt1 / paramInt2 > paramInt3 / paramInt4)
    {
      top = 0.0F;
      bottom = paramInt2;
      paramFloat2 = paramInt3 * (paramInt2 / paramInt4);
      left = ((paramInt1 - paramFloat2) * paramFloat1);
      right = (left + paramFloat2);
    }
    else
    {
      left = 0.0F;
      right = paramInt1;
      paramFloat1 = paramInt4 * (paramInt1 / paramInt3);
      top = ((paramInt2 - paramFloat1) * paramFloat2);
      bottom = (top + paramFloat1);
    }
    return localRectF;
  }
  
  static void initGlobals(IWallpaperManager paramIWallpaperManager, Looper paramLooper)
  {
    synchronized (sSync)
    {
      if (sGlobals == null)
      {
        Globals localGlobals = new android/app/WallpaperManager$Globals;
        localGlobals.<init>(paramIWallpaperManager, paramLooper);
        sGlobals = localGlobals;
      }
      return;
    }
  }
  
  public static InputStream openDefaultWallpaper(Context paramContext, int paramInt)
  {
    Object localObject = WallpaperUtilities.getAsusDefaultWallpaper(paramContext);
    if (localObject != null) {
      return localObject;
    }
    if (paramInt == 2) {
      return null;
    }
    localObject = SystemProperties.get("ro.config.wallpaper");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = new File((String)localObject);
      if (((File)localObject).exists()) {
        try
        {
          localObject = new FileInputStream((File)localObject);
          return localObject;
        }
        catch (IOException localIOException) {}
      }
    }
    try
    {
      paramContext = paramContext.getResources().openRawResource(17302386);
      return paramContext;
    }
    catch (Resources.NotFoundException paramContext) {}
    return null;
  }
  
  /* Error */
  private int setBitmap(Bitmap paramBitmap, Rect paramRect, boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_2
    //   2: invokespecial 406	android/app/WallpaperManager:validateRect	(Landroid/graphics/Rect;)V
    //   5: getstatic 147	android/app/WallpaperManager:sGlobals	Landroid/app/WallpaperManager$Globals;
    //   8: invokestatic 410	android/app/WallpaperManager$Globals:access$400	(Landroid/app/WallpaperManager$Globals;)Landroid/app/IWallpaperManager;
    //   11: ifnull +175 -> 186
    //   14: new 412	android/os/Bundle
    //   17: dup
    //   18: invokespecial 413	android/os/Bundle:<init>	()V
    //   21: astore 8
    //   23: new 20	android/app/WallpaperManager$WallpaperSetCompletion
    //   26: dup
    //   27: aload_0
    //   28: invokespecial 416	android/app/WallpaperManager$WallpaperSetCompletion:<init>	(Landroid/app/WallpaperManager;)V
    //   31: astore 9
    //   33: getstatic 147	android/app/WallpaperManager:sGlobals	Landroid/app/WallpaperManager$Globals;
    //   36: invokestatic 410	android/app/WallpaperManager$Globals:access$400	(Landroid/app/WallpaperManager$Globals;)Landroid/app/IWallpaperManager;
    //   39: aconst_null
    //   40: aload_0
    //   41: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   44: invokevirtual 419	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   47: aload_2
    //   48: iload_3
    //   49: aload 8
    //   51: iload 4
    //   53: aload 9
    //   55: iload 5
    //   57: invokeinterface 425 9 0
    //   62: astore 10
    //   64: aload 10
    //   66: ifnull +105 -> 171
    //   69: aconst_null
    //   70: astore 11
    //   72: aload 11
    //   74: astore_2
    //   75: new 427	android/os/ParcelFileDescriptor$AutoCloseOutputStream
    //   78: astore 12
    //   80: aload 11
    //   82: astore_2
    //   83: aload 12
    //   85: aload 10
    //   87: invokespecial 430	android/os/ParcelFileDescriptor$AutoCloseOutputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
    //   90: aload 12
    //   92: astore 11
    //   94: iload 6
    //   96: ifeq +16 -> 112
    //   99: aload 11
    //   101: astore_2
    //   102: aload_0
    //   103: aload_1
    //   104: aload 11
    //   106: invokespecial 432	android/app/WallpaperManager:copyBitmapToWallpaperFileProtected	(Landroid/graphics/Bitmap;Ljava/io/FileOutputStream;)V
    //   109: goto +18 -> 127
    //   112: aload 11
    //   114: astore_2
    //   115: aload_1
    //   116: getstatic 207	android/graphics/Bitmap$CompressFormat:PNG	Landroid/graphics/Bitmap$CompressFormat;
    //   119: bipush 90
    //   121: aload 11
    //   123: invokevirtual 213	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   126: pop
    //   127: aload_0
    //   128: iload 7
    //   130: iload 4
    //   132: invokespecial 436	android/app/WallpaperManager:setWallpaperOption	(II)V
    //   135: aload 11
    //   137: invokevirtual 439	java/io/FileOutputStream:close	()V
    //   140: aload 9
    //   142: invokevirtual 442	android/app/WallpaperManager$WallpaperSetCompletion:waitForCompletion	()V
    //   145: aload 11
    //   147: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   150: goto +21 -> 171
    //   153: astore_1
    //   154: aload 11
    //   156: astore_2
    //   157: goto +4 -> 161
    //   160: astore_1
    //   161: aload_2
    //   162: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   165: aload_1
    //   166: athrow
    //   167: astore_1
    //   168: goto +13 -> 181
    //   171: aload 8
    //   173: ldc 52
    //   175: iconst_0
    //   176: invokevirtual 446	android/os/Bundle:getInt	(Ljava/lang/String;I)I
    //   179: ireturn
    //   180: astore_1
    //   181: aload_1
    //   182: invokevirtual 450	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   185: athrow
    //   186: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   189: ldc_w 452
    //   192: invokestatic 259	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   195: pop
    //   196: new 454	java/lang/RuntimeException
    //   199: dup
    //   200: new 456	android/os/DeadSystemException
    //   203: dup
    //   204: invokespecial 457	android/os/DeadSystemException:<init>	()V
    //   207: invokespecial 460	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   210: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	211	0	this	WallpaperManager
    //   0	211	1	paramBitmap	Bitmap
    //   0	211	2	paramRect	Rect
    //   0	211	3	paramBoolean1	boolean
    //   0	211	4	paramInt1	int
    //   0	211	5	paramInt2	int
    //   0	211	6	paramBoolean2	boolean
    //   0	211	7	paramInt3	int
    //   21	151	8	localBundle	Bundle
    //   31	110	9	localWallpaperSetCompletion	WallpaperSetCompletion
    //   62	24	10	localParcelFileDescriptor	ParcelFileDescriptor
    //   70	85	11	localObject	Object
    //   78	13	12	localAutoCloseOutputStream	android.os.ParcelFileDescriptor.AutoCloseOutputStream
    // Exception table:
    //   from	to	target	type
    //   127	145	153	finally
    //   75	80	160	finally
    //   83	90	160	finally
    //   102	109	160	finally
    //   115	127	160	finally
    //   145	150	167	android/os/RemoteException
    //   161	167	167	android/os/RemoteException
    //   33	64	180	android/os/RemoteException
  }
  
  /* Error */
  private int setResource(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
    throws IOException
  {
    // Byte code:
    //   0: getstatic 147	android/app/WallpaperManager:sGlobals	Landroid/app/WallpaperManager$Globals;
    //   3: invokestatic 410	android/app/WallpaperManager$Globals:access$400	(Landroid/app/WallpaperManager$Globals;)Landroid/app/IWallpaperManager;
    //   6: ifnull +250 -> 256
    //   9: new 412	android/os/Bundle
    //   12: dup
    //   13: invokespecial 413	android/os/Bundle:<init>	()V
    //   16: astore 5
    //   18: new 20	android/app/WallpaperManager$WallpaperSetCompletion
    //   21: dup
    //   22: aload_0
    //   23: invokespecial 416	android/app/WallpaperManager$WallpaperSetCompletion:<init>	(Landroid/app/WallpaperManager;)V
    //   26: astore 6
    //   28: aload_0
    //   29: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   32: invokevirtual 391	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   35: astore 7
    //   37: getstatic 147	android/app/WallpaperManager:sGlobals	Landroid/app/WallpaperManager$Globals;
    //   40: invokestatic 410	android/app/WallpaperManager$Globals:access$400	(Landroid/app/WallpaperManager$Globals;)Landroid/app/IWallpaperManager;
    //   43: astore 8
    //   45: new 226	java/lang/StringBuilder
    //   48: astore 9
    //   50: aload 9
    //   52: invokespecial 227	java/lang/StringBuilder:<init>	()V
    //   55: aload 9
    //   57: ldc_w 464
    //   60: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: aload 9
    //   66: aload 7
    //   68: iload_1
    //   69: invokevirtual 467	android/content/res/Resources:getResourceName	(I)Ljava/lang/String;
    //   72: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: pop
    //   76: aload 8
    //   78: aload 9
    //   80: invokevirtual 239	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   83: aload_0
    //   84: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   87: invokevirtual 419	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   90: aconst_null
    //   91: iconst_0
    //   92: aload 5
    //   94: iload_2
    //   95: aload 6
    //   97: aload_0
    //   98: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   101: invokevirtual 161	android/content/Context:getUserId	()I
    //   104: invokeinterface 425 9 0
    //   109: astore 10
    //   111: aload 10
    //   113: ifnull +126 -> 239
    //   116: aconst_null
    //   117: astore 8
    //   119: aload 8
    //   121: astore 9
    //   123: new 427	android/os/ParcelFileDescriptor$AutoCloseOutputStream
    //   126: astore 11
    //   128: aload 8
    //   130: astore 9
    //   132: aload 11
    //   134: aload 10
    //   136: invokespecial 430	android/os/ParcelFileDescriptor$AutoCloseOutputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
    //   139: aload 11
    //   141: astore 8
    //   143: iload_3
    //   144: ifeq +22 -> 166
    //   147: aload 8
    //   149: astore 9
    //   151: aload_0
    //   152: aload 7
    //   154: iload_1
    //   155: invokevirtual 398	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   158: aload 8
    //   160: invokespecial 469	android/app/WallpaperManager:copyStreamToWallpaperFileProtected	(Ljava/io/InputStream;Ljava/io/FileOutputStream;)V
    //   163: goto +19 -> 182
    //   166: aload 8
    //   168: astore 9
    //   170: aload_0
    //   171: aload 7
    //   173: iload_1
    //   174: invokevirtual 398	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   177: aload 8
    //   179: invokespecial 471	android/app/WallpaperManager:copyStreamToWallpaperFile	(Ljava/io/InputStream;Ljava/io/FileOutputStream;)V
    //   182: aload_0
    //   183: iload 4
    //   185: iload_2
    //   186: invokespecial 436	android/app/WallpaperManager:setWallpaperOption	(II)V
    //   189: aload 8
    //   191: invokevirtual 439	java/io/FileOutputStream:close	()V
    //   194: aload 6
    //   196: invokevirtual 442	android/app/WallpaperManager$WallpaperSetCompletion:waitForCompletion	()V
    //   199: aload 8
    //   201: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   204: goto +35 -> 239
    //   207: astore 9
    //   209: aload 8
    //   211: astore 11
    //   213: goto +13 -> 226
    //   216: astore 8
    //   218: aload 9
    //   220: astore 11
    //   222: aload 8
    //   224: astore 9
    //   226: aload 11
    //   228: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   231: aload 9
    //   233: athrow
    //   234: astore 9
    //   236: goto +14 -> 250
    //   239: aload 5
    //   241: ldc 52
    //   243: iconst_0
    //   244: invokevirtual 446	android/os/Bundle:getInt	(Ljava/lang/String;I)I
    //   247: ireturn
    //   248: astore 9
    //   250: aload 9
    //   252: invokevirtual 450	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   255: athrow
    //   256: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   259: ldc_w 452
    //   262: invokestatic 259	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   265: pop
    //   266: new 454	java/lang/RuntimeException
    //   269: dup
    //   270: new 456	android/os/DeadSystemException
    //   273: dup
    //   274: invokespecial 457	android/os/DeadSystemException:<init>	()V
    //   277: invokespecial 460	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   280: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	281	0	this	WallpaperManager
    //   0	281	1	paramInt1	int
    //   0	281	2	paramInt2	int
    //   0	281	3	paramBoolean	boolean
    //   0	281	4	paramInt3	int
    //   16	224	5	localBundle	Bundle
    //   26	169	6	localWallpaperSetCompletion	WallpaperSetCompletion
    //   35	137	7	localResources	Resources
    //   43	167	8	localObject1	Object
    //   216	7	8	localObject2	Object
    //   48	121	9	localObject3	Object
    //   207	12	9	localObject4	Object
    //   224	8	9	localObject5	Object
    //   234	1	9	localRemoteException1	RemoteException
    //   248	3	9	localRemoteException2	RemoteException
    //   109	26	10	localParcelFileDescriptor	ParcelFileDescriptor
    //   126	101	11	localObject6	Object
    // Exception table:
    //   from	to	target	type
    //   182	199	207	finally
    //   123	128	216	finally
    //   132	139	216	finally
    //   151	163	216	finally
    //   170	182	216	finally
    //   199	204	234	android/os/RemoteException
    //   226	234	234	android/os/RemoteException
    //   28	111	248	android/os/RemoteException
  }
  
  /* Error */
  private int setStream(InputStream paramInputStream, Rect paramRect, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_2
    //   2: invokespecial 406	android/app/WallpaperManager:validateRect	(Landroid/graphics/Rect;)V
    //   5: getstatic 147	android/app/WallpaperManager:sGlobals	Landroid/app/WallpaperManager$Globals;
    //   8: invokestatic 410	android/app/WallpaperManager$Globals:access$400	(Landroid/app/WallpaperManager$Globals;)Landroid/app/IWallpaperManager;
    //   11: ifnull +175 -> 186
    //   14: new 412	android/os/Bundle
    //   17: dup
    //   18: invokespecial 413	android/os/Bundle:<init>	()V
    //   21: astore 7
    //   23: new 20	android/app/WallpaperManager$WallpaperSetCompletion
    //   26: dup
    //   27: aload_0
    //   28: invokespecial 416	android/app/WallpaperManager$WallpaperSetCompletion:<init>	(Landroid/app/WallpaperManager;)V
    //   31: astore 8
    //   33: getstatic 147	android/app/WallpaperManager:sGlobals	Landroid/app/WallpaperManager$Globals;
    //   36: invokestatic 410	android/app/WallpaperManager$Globals:access$400	(Landroid/app/WallpaperManager$Globals;)Landroid/app/IWallpaperManager;
    //   39: aconst_null
    //   40: aload_0
    //   41: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   44: invokevirtual 419	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   47: aload_2
    //   48: iload_3
    //   49: aload 7
    //   51: iload 4
    //   53: aload 8
    //   55: aload_0
    //   56: getfield 124	android/app/WallpaperManager:mContext	Landroid/content/Context;
    //   59: invokevirtual 161	android/content/Context:getUserId	()I
    //   62: invokeinterface 425 9 0
    //   67: astore 9
    //   69: aload 9
    //   71: ifnull +100 -> 171
    //   74: aconst_null
    //   75: astore 10
    //   77: aload 10
    //   79: astore_2
    //   80: new 427	android/os/ParcelFileDescriptor$AutoCloseOutputStream
    //   83: astore 11
    //   85: aload 10
    //   87: astore_2
    //   88: aload 11
    //   90: aload 9
    //   92: invokespecial 430	android/os/ParcelFileDescriptor$AutoCloseOutputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
    //   95: aload 11
    //   97: astore 10
    //   99: iload 5
    //   101: ifeq +16 -> 117
    //   104: aload 10
    //   106: astore_2
    //   107: aload_0
    //   108: aload_1
    //   109: aload 10
    //   111: invokespecial 469	android/app/WallpaperManager:copyStreamToWallpaperFileProtected	(Ljava/io/InputStream;Ljava/io/FileOutputStream;)V
    //   114: goto +13 -> 127
    //   117: aload 10
    //   119: astore_2
    //   120: aload_0
    //   121: aload_1
    //   122: aload 10
    //   124: invokespecial 471	android/app/WallpaperManager:copyStreamToWallpaperFile	(Ljava/io/InputStream;Ljava/io/FileOutputStream;)V
    //   127: aload_0
    //   128: iload 6
    //   130: iload 4
    //   132: invokespecial 436	android/app/WallpaperManager:setWallpaperOption	(II)V
    //   135: aload 10
    //   137: invokevirtual 439	java/io/FileOutputStream:close	()V
    //   140: aload 8
    //   142: invokevirtual 442	android/app/WallpaperManager$WallpaperSetCompletion:waitForCompletion	()V
    //   145: aload 10
    //   147: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   150: goto +21 -> 171
    //   153: astore_1
    //   154: aload 10
    //   156: astore_2
    //   157: goto +4 -> 161
    //   160: astore_1
    //   161: aload_2
    //   162: invokestatic 224	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   165: aload_1
    //   166: athrow
    //   167: astore_1
    //   168: goto +13 -> 181
    //   171: aload 7
    //   173: ldc 52
    //   175: iconst_0
    //   176: invokevirtual 446	android/os/Bundle:getInt	(Ljava/lang/String;I)I
    //   179: ireturn
    //   180: astore_1
    //   181: aload_1
    //   182: invokevirtual 450	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   185: athrow
    //   186: getstatic 108	android/app/WallpaperManager:TAG	Ljava/lang/String;
    //   189: ldc_w 452
    //   192: invokestatic 259	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   195: pop
    //   196: new 454	java/lang/RuntimeException
    //   199: dup
    //   200: new 456	android/os/DeadSystemException
    //   203: dup
    //   204: invokespecial 457	android/os/DeadSystemException:<init>	()V
    //   207: invokespecial 460	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   210: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	211	0	this	WallpaperManager
    //   0	211	1	paramInputStream	InputStream
    //   0	211	2	paramRect	Rect
    //   0	211	3	paramBoolean1	boolean
    //   0	211	4	paramInt1	int
    //   0	211	5	paramBoolean2	boolean
    //   0	211	6	paramInt2	int
    //   21	151	7	localBundle	Bundle
    //   31	110	8	localWallpaperSetCompletion	WallpaperSetCompletion
    //   67	24	9	localParcelFileDescriptor	ParcelFileDescriptor
    //   75	80	10	localObject	Object
    //   83	13	11	localAutoCloseOutputStream	android.os.ParcelFileDescriptor.AutoCloseOutputStream
    // Exception table:
    //   from	to	target	type
    //   127	145	153	finally
    //   80	85	160	finally
    //   88	95	160	finally
    //   107	114	160	finally
    //   120	127	160	finally
    //   145	150	167	android/os/RemoteException
    //   161	167	167	android/os/RemoteException
    //   33	69	180	android/os/RemoteException
  }
  
  private void setWallpaperOption(int paramInt1, int paramInt2)
  {
    if (mContext != null)
    {
      boolean bool = supportJobIntentService();
      Intent localIntent = new Intent("com.asus.lockscreen.wallpaper.option.setting");
      if (bool) {
        localIntent.setPackage("com.asus.lockscreen2");
      } else {
        localIntent.setComponent(ComponentName.unflattenFromString("com.asus.lockscreen2/.slideshow.wallpaperoption.WallpaperOptionSettingService"));
      }
      localIntent.putExtra("which", paramInt2);
      localIntent.putExtra("option", paramInt1);
      localIntent.putExtra("who", "WallpaperManager");
      if (bool) {
        try
        {
          mContext.sendBroadcast(localIntent);
        }
        catch (Exception localException)
        {
          return;
        }
      } else {
        mContext.startService(localException);
      }
    }
  }
  
  private boolean supportJobIntentService()
  {
    boolean bool = false;
    try
    {
      ApplicationInfo localApplicationInfo;
      if (mContext == null) {
        localApplicationInfo = null;
      } else {
        localApplicationInfo = mContext.getPackageManager().getApplicationInfo("com.asus.lockscreen2", 128);
      }
      if ((localApplicationInfo != null) && (metaData != null))
      {
        int i = metaData.getInt("support_job_intentservice", 0);
        if (i == 1) {
          bool = true;
        }
      }
      return bool;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return false;
  }
  
  private final void validateRect(Rect paramRect)
  {
    if ((paramRect != null) && (paramRect.isEmpty())) {
      throw new IllegalArgumentException("visibleCrop rectangle must be valid and non-empty");
    }
  }
  
  public void addOnColorsChangedListener(OnColorsChangedListener paramOnColorsChangedListener, Handler paramHandler)
  {
    addOnColorsChangedListener(paramOnColorsChangedListener, paramHandler, mContext.getUserId());
  }
  
  public void addOnColorsChangedListener(OnColorsChangedListener paramOnColorsChangedListener, Handler paramHandler, int paramInt)
  {
    sGlobals.addOnColorsChangedListener(paramOnColorsChangedListener, paramHandler, paramInt);
  }
  
  public float[] asusGetWallpaperOffset()
  {
    Object localObject = null;
    try
    {
      float[] arrayOfFloat = WindowManagerGlobal.getWindowManagerService().asusGetWallpaperOffset();
      localObject = arrayOfFloat;
    }
    catch (RemoteException localRemoteException) {}
    return localObject;
  }
  
  public void clear()
    throws IOException
  {
    setStream(openDefaultWallpaper(mContext, 1), null, false);
  }
  
  public void clear(int paramInt)
    throws IOException
  {
    if ((paramInt & 0x1) != 0) {
      clear();
    }
    if ((paramInt & 0x2) != 0)
    {
      clearWallpaper(2, mContext.getUserId());
      setWallpaperOption(0, 2);
    }
  }
  
  public void clearWallpaper()
  {
    clearWallpaper(2, mContext.getUserId());
    clearWallpaper(1, mContext.getUserId());
    setWallpaperOption(0, 3);
  }
  
  @SystemApi
  public void clearWallpaper(int paramInt1, int paramInt2)
  {
    if (sGlobalsmService != null) {
      try
      {
        sGlobalsmService.clearWallpaper(mContext.getOpPackageName(), paramInt1, paramInt2);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public void clearWallpaperOffsets(IBinder paramIBinder)
  {
    try
    {
      WindowManagerGlobal.getWindowSession().setWallpaperPosition(paramIBinder, -1.0F, -1.0F, -1.0F, -1.0F);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void forgetLoadedWallpaper()
  {
    sGlobals.forgetLoadedWallpaper();
  }
  
  public Bitmap getBitmap()
  {
    return getBitmap(false);
  }
  
  public Bitmap getBitmap(int paramInt)
  {
    return getBitmapAsUser(paramInt, mContext.getUserId());
  }
  
  public Bitmap getBitmap(int paramInt, boolean paramBoolean)
  {
    return getBitmapAsUser(paramInt, mContext.getUserId(), paramBoolean);
  }
  
  public Bitmap getBitmap(boolean paramBoolean)
  {
    return getBitmapAsUser(mContext.getUserId(), paramBoolean);
  }
  
  public Bitmap getBitmapAsUser(int paramInt1, int paramInt2)
  {
    return getBitmapAsUser(paramInt1, paramInt2, false);
  }
  
  public Bitmap getBitmapAsUser(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    return sGlobals.peekWallpaperBitmap(mContext, true, paramInt1, paramInt2, paramBoolean);
  }
  
  public Bitmap getBitmapAsUser(int paramInt, boolean paramBoolean)
  {
    return sGlobals.peekWallpaperBitmap(mContext, true, 1, paramInt, paramBoolean);
  }
  
  public Drawable getBuiltInDrawable()
  {
    return getBuiltInDrawable(0, 0, false, 0.0F, 0.0F, 1);
  }
  
  public Drawable getBuiltInDrawable(int paramInt)
  {
    return getBuiltInDrawable(0, 0, false, 0.0F, 0.0F, paramInt);
  }
  
  public Drawable getBuiltInDrawable(int paramInt1, int paramInt2, boolean paramBoolean, float paramFloat1, float paramFloat2)
  {
    return getBuiltInDrawable(paramInt1, paramInt2, paramBoolean, paramFloat1, paramFloat2, 1);
  }
  
  public Drawable getBuiltInDrawable(int paramInt1, int paramInt2, boolean paramBoolean, float paramFloat1, float paramFloat2, int paramInt3)
  {
    if (sGlobalsmService != null)
    {
      if ((paramInt3 != 1) && (paramInt3 != 2)) {
        throw new IllegalArgumentException("Must request exactly one kind of wallpaper");
      }
      Resources localResources = mContext.getResources();
      paramFloat1 = Math.max(0.0F, Math.min(1.0F, paramFloat1));
      float f = Math.max(0.0F, Math.min(1.0F, paramFloat2));
      Object localObject1 = openDefaultWallpaper(mContext, paramInt3);
      Object localObject3;
      if (localObject1 == null)
      {
        if (DEBUG)
        {
          localObject3 = TAG;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("default wallpaper stream ");
          ((StringBuilder)localObject1).append(paramInt3);
          ((StringBuilder)localObject1).append(" is null");
          Log.w((String)localObject3, ((StringBuilder)localObject1).toString());
        }
        return null;
      }
      localObject1 = new BufferedInputStream((InputStream)localObject1);
      Object localObject2;
      if ((paramInt1 > 0) && (paramInt2 > 0))
      {
        localObject3 = new BitmapFactory.Options();
        inJustDecodeBounds = true;
        BitmapFactory.decodeStream((InputStream)localObject1, null, (BitmapFactory.Options)localObject3);
        if ((outWidth != 0) && (outHeight != 0))
        {
          int i = outWidth;
          int j = outHeight;
          Object localObject4 = new BufferedInputStream(openDefaultWallpaper(mContext, paramInt3));
          paramInt1 = Math.min(i, paramInt1);
          int k = Math.min(j, paramInt2);
          if (paramBoolean)
          {
            localObject1 = getMaxCropRect(i, j, paramInt1, k, paramFloat1, f);
          }
          else
          {
            paramInt2 = paramInt1;
            paramFloat2 = (i - paramInt2) * paramFloat1;
            paramFloat1 = paramInt2;
            f = (j - k) * f;
            localObject1 = new RectF(paramFloat2, f, paramFloat1 + paramFloat2, k + f);
          }
          Object localObject5 = new Rect();
          ((RectF)localObject1).roundOut((Rect)localObject5);
          if ((((Rect)localObject5).width() > 0) && (((Rect)localObject5).height() > 0))
          {
            paramInt2 = Math.min(((Rect)localObject5).width() / paramInt1, ((Rect)localObject5).height() / k);
            localObject3 = null;
            try
            {
              localObject1 = BitmapRegionDecoder.newInstance((InputStream)localObject4, true);
              localObject3 = localObject1;
            }
            catch (IOException localIOException)
            {
              Log.w(TAG, "cannot open region decoder for default wallpaper");
            }
            localObject2 = null;
            if (localObject3 != null)
            {
              localObject2 = new BitmapFactory.Options();
              if (paramInt2 > 1) {
                inSampleSize = paramInt2;
              }
              localObject2 = ((BitmapRegionDecoder)localObject3).decodeRegion((Rect)localObject5, (BitmapFactory.Options)localObject2);
              ((BitmapRegionDecoder)localObject3).recycle();
            }
            if (localObject2 == null)
            {
              localObject4 = new BufferedInputStream(openDefaultWallpaper(mContext, paramInt3));
              localObject3 = new BitmapFactory.Options();
              if (paramInt2 > 1) {
                inSampleSize = paramInt2;
              }
              localObject3 = BitmapFactory.decodeStream((InputStream)localObject4, null, (BitmapFactory.Options)localObject3);
              if (localObject3 != null) {
                localObject2 = Bitmap.createBitmap((Bitmap)localObject3, left, top, ((Rect)localObject5).width(), ((Rect)localObject5).height());
              } else {}
            }
            if (localObject2 == null)
            {
              Log.w(TAG, "cannot decode default wallpaper");
              return null;
            }
            if ((paramInt1 > 0) && (k > 0))
            {
              if ((((Bitmap)localObject2).getWidth() == paramInt1) && (((Bitmap)localObject2).getHeight() == k)) {
                break label729;
              }
              localObject5 = new Matrix();
              localObject4 = new RectF(0.0F, 0.0F, ((Bitmap)localObject2).getWidth(), ((Bitmap)localObject2).getHeight());
              localObject3 = new RectF(0.0F, 0.0F, paramInt1, k);
              ((Matrix)localObject5).setRectToRect((RectF)localObject4, (RectF)localObject3, Matrix.ScaleToFit.FILL);
              localObject3 = Bitmap.createBitmap((int)((RectF)localObject3).width(), (int)((RectF)localObject3).height(), Bitmap.Config.ARGB_8888);
              if (localObject3 != null)
              {
                localObject4 = new Canvas((Bitmap)localObject3);
                Paint localPaint = new Paint();
                localPaint.setFilterBitmap(true);
                ((Canvas)localObject4).drawBitmap((Bitmap)localObject2, (Matrix)localObject5, localPaint);
                localObject2 = localObject3;
              }
              else {}
            }
            label729:
            return new BitmapDrawable(localResources, (Bitmap)localObject2);
          }
          Log.w(TAG, "crop has bad values for full size image");
          return null;
        }
        Log.e(TAG, "default wallpaper dimensions are 0");
        return null;
      }
      return new BitmapDrawable(localResources, BitmapFactory.decodeStream((InputStream)localObject2, null, null));
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public Intent getCropAndSetWallpaperIntent(Uri paramUri)
  {
    if (paramUri != null)
    {
      if ("content".equals(paramUri.getScheme()))
      {
        PackageManager localPackageManager = mContext.getPackageManager();
        paramUri = new Intent("android.service.wallpaper.CROP_AND_SET_WALLPAPER", paramUri);
        paramUri.addFlags(1);
        ResolveInfo localResolveInfo = localPackageManager.resolveActivity(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 65536);
        if (localResolveInfo != null)
        {
          paramUri.setPackage(activityInfo.packageName);
          if (localPackageManager.queryIntentActivities(paramUri, 0).size() > 0) {
            return paramUri;
          }
        }
        paramUri.setPackage(mContext.getString(17039752));
        if (localPackageManager.queryIntentActivities(paramUri, 0).size() > 0) {
          return paramUri;
        }
        throw new IllegalArgumentException("Cannot use passed URI to set wallpaper; check that the type returned by ContentProvider matches image/*");
      }
      throw new IllegalArgumentException("Image URI must be of the content scheme type");
    }
    throw new IllegalArgumentException("Image URI must not be null");
  }
  
  public int getDesiredMinimumHeight()
  {
    if (sGlobalsmService != null) {
      try
      {
        int i = sGlobalsmService.getHeightHint();
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public int getDesiredMinimumWidth()
  {
    if (sGlobalsmService != null) {
      try
      {
        int i = sGlobalsmService.getWidthHint();
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public Drawable getDrawable()
  {
    Object localObject = sGlobals.peekWallpaperBitmap(mContext, true, 1);
    if (localObject != null)
    {
      localObject = new BitmapDrawable(mContext.getResources(), (Bitmap)localObject);
      ((Drawable)localObject).setDither(false);
      return localObject;
    }
    return null;
  }
  
  public Drawable getFastDrawable()
  {
    Bitmap localBitmap = sGlobals.peekWallpaperBitmap(mContext, true, 1);
    if (localBitmap != null) {
      return new FastBitmapDrawable(localBitmap, null);
    }
    return null;
  }
  
  public IWallpaperManager getIWallpaperManager()
  {
    return sGlobalsmService;
  }
  
  public WallpaperColors getWallpaperColors(int paramInt)
  {
    return getWallpaperColors(paramInt, mContext.getUserId());
  }
  
  public WallpaperColors getWallpaperColors(int paramInt1, int paramInt2)
  {
    return sGlobals.getWallpaperColors(paramInt1, paramInt2);
  }
  
  public ParcelFileDescriptor getWallpaperFile(int paramInt)
  {
    return getWallpaperFile(paramInt, mContext.getUserId());
  }
  
  public ParcelFileDescriptor getWallpaperFile(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != 1) && (paramInt1 != 2)) {
      throw new IllegalArgumentException("Must request exactly one kind of wallpaper");
    }
    if (sGlobalsmService != null) {
      try
      {
        Object localObject = new android/os/Bundle;
        ((Bundle)localObject).<init>();
        localObject = sGlobalsmService.getWallpaper(mContext.getOpPackageName(), null, paramInt1, (Bundle)localObject, paramInt2);
        return localObject;
      }
      catch (SecurityException localSecurityException)
      {
        if (mContext.getApplicationInfo().targetSdkVersion < 27)
        {
          Log.w(TAG, "No permission to access wallpaper, suppressing exception to avoid crashing legacy app.");
          return null;
        }
        throw localSecurityException;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public int getWallpaperId(int paramInt)
  {
    return getWallpaperIdForUser(paramInt, mContext.getUserId());
  }
  
  public int getWallpaperIdForUser(int paramInt1, int paramInt2)
  {
    try
    {
      if (sGlobalsmService != null) {
        return sGlobalsmService.getWallpaperIdForUser(paramInt1, paramInt2);
      }
      Log.w(TAG, "WallpaperService not running");
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      DeadSystemException localDeadSystemException = new android/os/DeadSystemException;
      localDeadSystemException.<init>();
      localRuntimeException.<init>(localDeadSystemException);
      throw localRuntimeException;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public WallpaperInfo getWallpaperInfo()
  {
    try
    {
      if (sGlobalsmService != null) {
        return sGlobalsmService.getWallpaperInfo(mContext.getUserId());
      }
      Log.w(TAG, "WallpaperService not running");
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      DeadSystemException localDeadSystemException = new android/os/DeadSystemException;
      localDeadSystemException.<init>();
      localRuntimeException.<init>(localDeadSystemException);
      throw localRuntimeException;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean hasResourceWallpaper(int paramInt)
  {
    if (sGlobalsmService != null) {
      try
      {
        Resources localResources = mContext.getResources();
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("res:");
        ((StringBuilder)localObject).append(localResources.getResourceName(paramInt));
        localObject = ((StringBuilder)localObject).toString();
        boolean bool = sGlobalsmService.hasNamedWallpaper((String)localObject);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public boolean isSetWallpaperAllowed()
  {
    if (sGlobalsmService != null) {
      try
      {
        boolean bool = sGlobalsmService.isSetWallpaperAllowed(mContext.getOpPackageName());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public boolean isWallpaperBackupEligible(int paramInt)
  {
    if (sGlobalsmService != null) {
      try
      {
        boolean bool = sGlobalsmService.isWallpaperBackupEligible(paramInt, mContext.getUserId());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        String str = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Exception querying wallpaper backup eligibility: ");
        localStringBuilder.append(localRemoteException.getMessage());
        Log.e(str, localStringBuilder.toString());
        return false;
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public boolean isWallpaperSupported()
  {
    if (sGlobalsmService != null) {
      try
      {
        boolean bool = sGlobalsmService.isWallpaperSupported(mContext.getOpPackageName());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public Drawable peekDrawable()
  {
    Object localObject = sGlobals.peekWallpaperBitmap(mContext, false, 1);
    if (localObject != null)
    {
      localObject = new BitmapDrawable(mContext.getResources(), (Bitmap)localObject);
      ((Drawable)localObject).setDither(false);
      return localObject;
    }
    return null;
  }
  
  public Drawable peekFastDrawable()
  {
    Bitmap localBitmap = sGlobals.peekWallpaperBitmap(mContext, false, 1);
    if (localBitmap != null) {
      return new FastBitmapDrawable(localBitmap, null);
    }
    return null;
  }
  
  public void removeOnColorsChangedListener(OnColorsChangedListener paramOnColorsChangedListener)
  {
    removeOnColorsChangedListener(paramOnColorsChangedListener, mContext.getUserId());
  }
  
  public void removeOnColorsChangedListener(OnColorsChangedListener paramOnColorsChangedListener, int paramInt)
  {
    sGlobals.removeOnColorsChangedListener(paramOnColorsChangedListener, paramInt);
  }
  
  public void sendWallpaperCommand(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
  {
    try
    {
      WindowManagerGlobal.getWindowSession().sendWallpaperCommand(paramIBinder, paramString, paramInt1, paramInt2, paramInt3, paramBundle, false);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public int setBitmap(Bitmap paramBitmap, Rect paramRect, boolean paramBoolean)
    throws IOException
  {
    return setBitmap(paramBitmap, paramRect, paramBoolean, 3);
  }
  
  public int setBitmap(Bitmap paramBitmap, Rect paramRect, boolean paramBoolean, int paramInt)
    throws IOException
  {
    return setBitmap(paramBitmap, paramRect, paramBoolean, paramInt, mContext.getUserId());
  }
  
  public int setBitmap(Bitmap paramBitmap, Rect paramRect, boolean paramBoolean, int paramInt1, int paramInt2)
    throws IOException
  {
    return setBitmap(paramBitmap, paramRect, paramBoolean, paramInt1, paramInt2, false, 0);
  }
  
  public void setBitmap(Bitmap paramBitmap)
    throws IOException
  {
    setBitmap(paramBitmap, null, true);
  }
  
  public int setBitmapProtected(Bitmap paramBitmap, Rect paramRect)
    throws IOException
  {
    return setBitmapProtected(paramBitmap, paramRect, 3);
  }
  
  public int setBitmapProtected(Bitmap paramBitmap, Rect paramRect, int paramInt)
    throws IOException
  {
    return setBitmapProtected(paramBitmap, paramRect, paramInt, UserHandle.myUserId());
  }
  
  public int setBitmapProtected(Bitmap paramBitmap, Rect paramRect, int paramInt1, int paramInt2)
    throws IOException
  {
    return setBitmap(paramBitmap, paramRect, false, paramInt1, paramInt2, true, 0);
  }
  
  public void setBitmapProtected(Bitmap paramBitmap)
    throws IOException
  {
    setBitmapProtected(paramBitmap, null);
  }
  
  public int setBitmapProtectedWithOption(Bitmap paramBitmap, Rect paramRect, int paramInt)
    throws IOException
  {
    return setBitmapProtectedWithOption(paramBitmap, paramRect, 3, paramInt);
  }
  
  public int setBitmapProtectedWithOption(Bitmap paramBitmap, Rect paramRect, int paramInt1, int paramInt2)
    throws IOException
  {
    return setBitmapProtectedWithOption(paramBitmap, paramRect, paramInt1, UserHandle.myUserId(), paramInt2);
  }
  
  public int setBitmapProtectedWithOption(Bitmap paramBitmap, Rect paramRect, int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    return setBitmap(paramBitmap, paramRect, false, paramInt1, paramInt2, true, paramInt3);
  }
  
  public void setBitmapProtectedWithOption(Bitmap paramBitmap, int paramInt)
    throws IOException
  {
    setBitmapProtectedWithOption(paramBitmap, null, paramInt);
  }
  
  @SystemApi
  public void setDisplayOffset(IBinder paramIBinder, int paramInt1, int paramInt2)
  {
    try
    {
      WindowManagerGlobal.getWindowSession().setWallpaperDisplayOffset(paramIBinder, paramInt1, paramInt2);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void setDisplayPadding(Rect paramRect)
  {
    try
    {
      if (sGlobalsmService != null)
      {
        sGlobalsmService.setDisplayPadding(paramRect, mContext.getOpPackageName());
        return;
      }
      Log.w(TAG, "WallpaperService not running");
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      paramRect = new android/os/DeadSystemException;
      paramRect.<init>();
      localRuntimeException.<init>(paramRect);
      throw localRuntimeException;
    }
    catch (RemoteException paramRect)
    {
      throw paramRect.rethrowFromSystemServer();
    }
  }
  
  public boolean setLockWallpaperCallback(IWallpaperManagerCallback paramIWallpaperManagerCallback)
  {
    if (sGlobalsmService != null) {
      try
      {
        boolean bool = sGlobalsmService.setLockWallpaperCallback(paramIWallpaperManagerCallback);
        return bool;
      }
      catch (RemoteException paramIWallpaperManagerCallback)
      {
        throw paramIWallpaperManagerCallback.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public int setResource(int paramInt1, int paramInt2)
    throws IOException
  {
    return setResource(paramInt1, paramInt2, false, 0);
  }
  
  public void setResource(int paramInt)
    throws IOException
  {
    setResource(paramInt, 3);
  }
  
  public int setResourceProtected(int paramInt1, int paramInt2)
    throws IOException
  {
    return setResource(paramInt1, paramInt2, true, 0);
  }
  
  public void setResourceProtected(int paramInt)
    throws IOException
  {
    setResourceProtected(paramInt, 3);
  }
  
  public int setResourceProtectedWithOption(int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    return setResource(paramInt1, paramInt2, true, paramInt3);
  }
  
  public void setResourceProtectedWithOption(int paramInt1, int paramInt2)
    throws IOException
  {
    setResourceProtectedWithOption(paramInt1, 3, paramInt2);
  }
  
  public int setStream(InputStream paramInputStream, Rect paramRect, boolean paramBoolean)
    throws IOException
  {
    return setStream(paramInputStream, paramRect, paramBoolean, 3);
  }
  
  public int setStream(InputStream paramInputStream, Rect paramRect, boolean paramBoolean, int paramInt)
    throws IOException
  {
    return setStream(paramInputStream, paramRect, paramBoolean, paramInt, false, 0);
  }
  
  public void setStream(InputStream paramInputStream)
    throws IOException
  {
    setStream(paramInputStream, null, true);
  }
  
  public int setStreamProtected(InputStream paramInputStream, Rect paramRect)
    throws IOException
  {
    return setStreamProtected(paramInputStream, paramRect, 3);
  }
  
  public int setStreamProtected(InputStream paramInputStream, Rect paramRect, int paramInt)
    throws IOException
  {
    return setStream(paramInputStream, paramRect, false, paramInt, true, 0);
  }
  
  public void setStreamProtected(InputStream paramInputStream)
    throws IOException
  {
    setStreamProtected(paramInputStream, null);
  }
  
  public int setStreamProtectedWithOption(InputStream paramInputStream, Rect paramRect, int paramInt)
    throws IOException
  {
    return setStreamProtectedWithOption(paramInputStream, paramRect, 3, paramInt);
  }
  
  public int setStreamProtectedWithOption(InputStream paramInputStream, Rect paramRect, int paramInt1, int paramInt2)
    throws IOException
  {
    return setStream(paramInputStream, paramRect, false, paramInt1, true, paramInt2);
  }
  
  public void setStreamProtectedWithOption(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    setStreamProtectedWithOption(paramInputStream, null, paramInt);
  }
  
  @SystemApi
  public boolean setWallpaperComponent(ComponentName paramComponentName)
  {
    return setWallpaperComponent(paramComponentName, mContext.getUserId());
  }
  
  public boolean setWallpaperComponent(ComponentName paramComponentName, int paramInt)
  {
    if (sGlobalsmService != null) {
      try
      {
        sGlobalsmService.setWallpaperComponentChecked(paramComponentName, mContext.getOpPackageName(), paramInt);
        setWallpaperOption(0, 1);
        return true;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    Log.w(TAG, "WallpaperService not running");
    throw new RuntimeException(new DeadSystemException());
  }
  
  public void setWallpaperOffsetSteps(float paramFloat1, float paramFloat2)
  {
    mWallpaperXStep = paramFloat1;
    mWallpaperYStep = paramFloat2;
  }
  
  public void setWallpaperOffsets(IBinder paramIBinder, float paramFloat1, float paramFloat2)
  {
    try
    {
      WindowManagerGlobal.getWindowSession().setWallpaperPosition(paramIBinder, paramFloat1, paramFloat2, mWallpaperXStep, mWallpaperYStep);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void suggestDesiredDimensions(int paramInt1, int paramInt2)
  {
    int i = 0;
    try
    {
      j = SystemProperties.getInt("sys.max_texture_size", 0);
      i = j;
    }
    catch (RemoteException localRemoteException)
    {
      break label164;
    }
    catch (Exception localException) {}
    int j = paramInt1;
    int k = paramInt2;
    if (i > 0) {
      if (paramInt1 <= i)
      {
        j = paramInt1;
        k = paramInt2;
        if (paramInt2 <= i) {}
      }
      else
      {
        float f = paramInt2 / paramInt1;
        if (paramInt1 > paramInt2)
        {
          k = (int)(i * f + 0.5D);
          j = i;
        }
        else
        {
          j = (int)(i / f + 0.5D);
          k = i;
        }
      }
    }
    if (sGlobalsmService != null)
    {
      sGlobalsmService.setDimensionHints(j, k, mContext.getOpPackageName());
      return;
    }
    Log.w(TAG, "WallpaperService not running");
    RuntimeException localRuntimeException = new java/lang/RuntimeException;
    DeadSystemException localDeadSystemException = new android/os/DeadSystemException;
    localDeadSystemException.<init>();
    localRuntimeException.<init>(localDeadSystemException);
    throw localRuntimeException;
    label164:
    throw localRuntimeException.rethrowFromSystemServer();
  }
  
  static class FastBitmapDrawable
    extends Drawable
  {
    private final Bitmap mBitmap;
    private int mDrawLeft;
    private int mDrawTop;
    private final int mHeight;
    private final Paint mPaint;
    private final int mWidth;
    
    private FastBitmapDrawable(Bitmap paramBitmap)
    {
      mBitmap = paramBitmap;
      mWidth = paramBitmap.getWidth();
      mHeight = paramBitmap.getHeight();
      setBounds(0, 0, mWidth, mHeight);
      mPaint = new Paint();
      mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }
    
    public void draw(Canvas paramCanvas)
    {
      paramCanvas.drawBitmap(mBitmap, mDrawLeft, mDrawTop, mPaint);
    }
    
    public int getIntrinsicHeight()
    {
      return mHeight;
    }
    
    public int getIntrinsicWidth()
    {
      return mWidth;
    }
    
    public int getMinimumHeight()
    {
      return mHeight;
    }
    
    public int getMinimumWidth()
    {
      return mWidth;
    }
    
    public int getOpacity()
    {
      return -1;
    }
    
    public void setAlpha(int paramInt)
    {
      throw new UnsupportedOperationException("Not supported with this drawable");
    }
    
    public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      mDrawLeft = ((paramInt3 - paramInt1 - mWidth) / 2 + paramInt1);
      mDrawTop = ((paramInt4 - paramInt2 - mHeight) / 2 + paramInt2);
    }
    
    public void setColorFilter(ColorFilter paramColorFilter)
    {
      throw new UnsupportedOperationException("Not supported with this drawable");
    }
    
    public void setDither(boolean paramBoolean)
    {
      throw new UnsupportedOperationException("Not supported with this drawable");
    }
    
    public void setFilterBitmap(boolean paramBoolean)
    {
      throw new UnsupportedOperationException("Not supported with this drawable");
    }
  }
  
  private static class Globals
    extends IWallpaperManagerCallback.Stub
  {
    private Bitmap mCachedWallpaper;
    private int mCachedWallpaperUserId;
    private int mCachedWallpaperWhich;
    private boolean mColorCallbackRegistered;
    private final ArrayList<Pair<WallpaperManager.OnColorsChangedListener, Handler>> mColorListeners = new ArrayList();
    private Bitmap mDefaultWallpaper;
    private Handler mMainLooperHandler;
    private final IWallpaperManager mService;
    
    Globals(IWallpaperManager paramIWallpaperManager, Looper paramLooper)
    {
      mService = paramIWallpaperManager;
      mMainLooperHandler = new Handler(paramLooper);
      forgetLoadedWallpaper();
    }
    
    /* Error */
    private Bitmap getCurrentWallpaperLocked(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 34	android/app/WallpaperManager$Globals:mService	Landroid/app/IWallpaperManager;
      //   4: ifnonnull +14 -> 18
      //   7: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   10: ldc 59
      //   12: invokestatic 65	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   15: pop
      //   16: aconst_null
      //   17: areturn
      //   18: new 67	android/os/Bundle
      //   21: astore 5
      //   23: aload 5
      //   25: invokespecial 68	android/os/Bundle:<init>	()V
      //   28: aload_0
      //   29: getfield 34	android/app/WallpaperManager$Globals:mService	Landroid/app/IWallpaperManager;
      //   32: aload_1
      //   33: invokevirtual 73	android/content/Context:getOpPackageName	()Ljava/lang/String;
      //   36: aload_0
      //   37: iload_2
      //   38: aload 5
      //   40: iload_3
      //   41: invokeinterface 79 6 0
      //   46: astore 6
      //   48: aload 6
      //   50: ifnull +134 -> 184
      //   53: aload_0
      //   54: aload 6
      //   56: invokevirtual 85	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   59: invokespecial 89	android/app/WallpaperManager$Globals:isEncryptedWallpaper	(Ljava/io/FileDescriptor;)Z
      //   62: istore 7
      //   64: new 91	android/graphics/BitmapFactory$Options
      //   67: astore 5
      //   69: aload 5
      //   71: invokespecial 92	android/graphics/BitmapFactory$Options:<init>	()V
      //   74: iload 4
      //   76: ifeq +11 -> 87
      //   79: aload 5
      //   81: getstatic 98	android/graphics/Bitmap$Config:HARDWARE	Landroid/graphics/Bitmap$Config;
      //   84: putfield 101	android/graphics/BitmapFactory$Options:inPreferredConfig	Landroid/graphics/Bitmap$Config;
      //   87: iload 7
      //   89: ifeq +46 -> 135
      //   92: aload_0
      //   93: aload_1
      //   94: aload 6
      //   96: iload_3
      //   97: aload 5
      //   99: invokespecial 104	android/app/WallpaperManager$Globals:getWallpaper	(Landroid/content/Context;Landroid/os/ParcelFileDescriptor;ILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   102: astore 5
      //   104: aload_0
      //   105: aload_1
      //   106: iload_3
      //   107: invokespecial 108	android/app/WallpaperManager$Globals:isUntrustedApp	(Landroid/content/Context;I)Z
      //   110: ifeq +15 -> 125
      //   113: aload_0
      //   114: aload 5
      //   116: ldc 110
      //   118: invokespecial 114	android/app/WallpaperManager$Globals:watermark	(Landroid/graphics/Bitmap;Ljava/lang/String;)Landroid/graphics/Bitmap;
      //   121: astore_1
      //   122: goto +6 -> 128
      //   125: aload 5
      //   127: astore_1
      //   128: aload 6
      //   130: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   133: aload_1
      //   134: areturn
      //   135: aload 6
      //   137: invokevirtual 85	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   140: aconst_null
      //   141: aload 5
      //   143: invokestatic 126	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   146: astore_1
      //   147: aload 6
      //   149: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   152: aload_1
      //   153: areturn
      //   154: astore_1
      //   155: goto +22 -> 177
      //   158: astore_1
      //   159: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   162: ldc -128
      //   164: aload_1
      //   165: invokestatic 131	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   168: pop
      //   169: aload 6
      //   171: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   174: goto +10 -> 184
      //   177: aload 6
      //   179: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   182: aload_1
      //   183: athrow
      //   184: aconst_null
      //   185: areturn
      //   186: astore_1
      //   187: aload_1
      //   188: invokevirtual 135	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
      //   191: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	192	0	this	Globals
      //   0	192	1	paramContext	Context
      //   0	192	2	paramInt1	int
      //   0	192	3	paramInt2	int
      //   0	192	4	paramBoolean	boolean
      //   21	121	5	localObject	Object
      //   46	132	6	localParcelFileDescriptor	ParcelFileDescriptor
      //   62	26	7	bool	boolean
      // Exception table:
      //   from	to	target	type
      //   64	74	154	finally
      //   79	87	154	finally
      //   92	122	154	finally
      //   135	147	154	finally
      //   159	169	154	finally
      //   64	74	158	java/lang/OutOfMemoryError
      //   79	87	158	java/lang/OutOfMemoryError
      //   92	122	158	java/lang/OutOfMemoryError
      //   135	147	158	java/lang/OutOfMemoryError
      //   18	48	186	android/os/RemoteException
      //   53	64	186	android/os/RemoteException
      //   128	133	186	android/os/RemoteException
      //   147	152	186	android/os/RemoteException
      //   169	174	186	android/os/RemoteException
      //   177	184	186	android/os/RemoteException
    }
    
    /* Error */
    private Bitmap getDefaultWallpaper(Context paramContext, int paramInt)
    {
      // Byte code:
      //   0: aload_1
      //   1: iload_2
      //   2: invokestatic 141	android/app/WallpaperManager:openDefaultWallpaper	(Landroid/content/Context;I)Ljava/io/InputStream;
      //   5: astore_1
      //   6: aload_1
      //   7: ifnull +52 -> 59
      //   10: new 91	android/graphics/BitmapFactory$Options
      //   13: astore_3
      //   14: aload_3
      //   15: invokespecial 92	android/graphics/BitmapFactory$Options:<init>	()V
      //   18: aload_1
      //   19: aconst_null
      //   20: aload_3
      //   21: invokestatic 145	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   24: astore_3
      //   25: aload_1
      //   26: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   29: aload_3
      //   30: areturn
      //   31: astore_3
      //   32: goto +21 -> 53
      //   35: astore_3
      //   36: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   39: ldc -109
      //   41: aload_3
      //   42: invokestatic 131	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   45: pop
      //   46: aload_1
      //   47: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   50: goto +9 -> 59
      //   53: aload_1
      //   54: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   57: aload_3
      //   58: athrow
      //   59: aconst_null
      //   60: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	61	0	this	Globals
      //   0	61	1	paramContext	Context
      //   0	61	2	paramInt	int
      //   13	17	3	localObject1	Object
      //   31	1	3	localObject2	Object
      //   35	23	3	localOutOfMemoryError	OutOfMemoryError
      // Exception table:
      //   from	to	target	type
      //   10	25	31	finally
      //   36	46	31	finally
      //   10	25	35	java/lang/OutOfMemoryError
    }
    
    /* Error */
    private Bitmap getWallpaper(Context paramContext, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt, BitmapFactory.Options paramOptions)
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_2
      //   2: invokevirtual 85	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   5: invokespecial 89	android/app/WallpaperManager$Globals:isEncryptedWallpaper	(Ljava/io/FileDescriptor;)Z
      //   8: ifeq +562 -> 570
      //   11: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   14: lstore 5
      //   16: aconst_null
      //   17: astore 7
      //   19: aconst_null
      //   20: astore 8
      //   22: aconst_null
      //   23: astore 9
      //   25: aconst_null
      //   26: astore 10
      //   28: aconst_null
      //   29: astore 11
      //   31: aconst_null
      //   32: astore 12
      //   34: new 157	android/os/ParcelFileDescriptor$AutoCloseInputStream
      //   37: astore_1
      //   38: aload 10
      //   40: astore 8
      //   42: aload 7
      //   44: astore 9
      //   46: aload_1
      //   47: aload_2
      //   48: invokespecial 160	android/os/ParcelFileDescriptor$AutoCloseInputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
      //   51: aload_1
      //   52: astore 8
      //   54: aload_1
      //   55: astore 9
      //   57: iload_3
      //   58: invokestatic 164	android/app/WallpaperManager:access$100	(I)[[B
      //   61: astore_2
      //   62: aload_1
      //   63: astore 8
      //   65: aload_1
      //   66: astore 9
      //   68: ldc -90
      //   70: invokestatic 172	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
      //   73: aload_2
      //   74: iconst_0
      //   75: aaload
      //   76: invokevirtual 176	java/security/MessageDigest:digest	([B)[B
      //   79: astore 7
      //   81: aload_2
      //   82: iconst_1
      //   83: aaload
      //   84: astore_2
      //   85: aload_1
      //   86: astore 8
      //   88: aload_1
      //   89: astore 9
      //   91: new 178	javax/crypto/spec/SecretKeySpec
      //   94: astore 10
      //   96: aload_1
      //   97: astore 8
      //   99: aload_1
      //   100: astore 9
      //   102: aload 10
      //   104: aload 7
      //   106: ldc -76
      //   108: invokespecial 183	javax/crypto/spec/SecretKeySpec:<init>	([BLjava/lang/String;)V
      //   111: aload_1
      //   112: astore 8
      //   114: aload_1
      //   115: astore 9
      //   117: ldc -71
      //   119: invokestatic 190	javax/crypto/Cipher:getInstance	(Ljava/lang/String;)Ljavax/crypto/Cipher;
      //   122: astore 7
      //   124: aload_1
      //   125: astore 8
      //   127: aload_1
      //   128: astore 9
      //   130: new 192	javax/crypto/spec/IvParameterSpec
      //   133: astore 13
      //   135: aload_1
      //   136: astore 8
      //   138: aload_1
      //   139: astore 9
      //   141: aload 13
      //   143: aload_2
      //   144: invokespecial 195	javax/crypto/spec/IvParameterSpec:<init>	([B)V
      //   147: aload_1
      //   148: astore 8
      //   150: aload_1
      //   151: astore 9
      //   153: aload 7
      //   155: iconst_2
      //   156: aload 10
      //   158: aload 13
      //   160: invokevirtual 199	javax/crypto/Cipher:init	(ILjava/security/Key;Ljava/security/spec/AlgorithmParameterSpec;)V
      //   163: aload_1
      //   164: astore 8
      //   166: aload_1
      //   167: astore 9
      //   169: new 201	javax/crypto/CipherInputStream
      //   172: astore_2
      //   173: aload_1
      //   174: astore 8
      //   176: aload_1
      //   177: astore 9
      //   179: new 203	java/io/BufferedInputStream
      //   182: astore 10
      //   184: aload_1
      //   185: astore 8
      //   187: aload_1
      //   188: astore 9
      //   190: aload 10
      //   192: aload_1
      //   193: sipush 8192
      //   196: invokespecial 206	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
      //   199: aload_1
      //   200: astore 8
      //   202: aload_1
      //   203: astore 9
      //   205: aload_2
      //   206: aload 10
      //   208: aload 7
      //   210: invokespecial 209	javax/crypto/CipherInputStream:<init>	(Ljava/io/InputStream;Ljavax/crypto/Cipher;)V
      //   213: aload_2
      //   214: astore 9
      //   216: aload_1
      //   217: astore_2
      //   218: aload 9
      //   220: astore 8
      //   222: aload 9
      //   224: aconst_null
      //   225: aload 4
      //   227: invokestatic 145	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   230: astore 4
      //   232: aload 9
      //   234: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   237: aload_1
      //   238: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   241: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   244: lstore 14
      //   246: invokestatic 213	android/app/WallpaperManager:access$200	()Z
      //   249: ifeq +54 -> 303
      //   252: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   255: astore_2
      //   256: new 215	java/lang/StringBuilder
      //   259: dup
      //   260: invokespecial 216	java/lang/StringBuilder:<init>	()V
      //   263: astore_1
      //   264: aload_1
      //   265: ldc -38
      //   267: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   270: pop
      //   271: aload_1
      //   272: lload 14
      //   274: lload 5
      //   276: lsub
      //   277: invokestatic 228	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   280: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   283: pop
      //   284: aload_1
      //   285: ldc -26
      //   287: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   290: pop
      //   291: aload_2
      //   292: aload_1
      //   293: invokevirtual 233	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   296: invokestatic 65	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   299: pop
      //   300: goto +3 -> 303
      //   303: aload 4
      //   305: areturn
      //   306: astore 4
      //   308: goto +36 -> 344
      //   311: astore_1
      //   312: goto +11 -> 323
      //   315: astore_2
      //   316: aload 9
      //   318: astore_1
      //   319: goto +18 -> 337
      //   322: astore_1
      //   323: aload 8
      //   325: astore_2
      //   326: aload 11
      //   328: astore 8
      //   330: goto +168 -> 498
      //   333: astore_2
      //   334: aload 9
      //   336: astore_1
      //   337: aload 12
      //   339: astore 9
      //   341: aload_2
      //   342: astore 4
      //   344: aload_1
      //   345: astore_2
      //   346: aload 9
      //   348: astore 8
      //   350: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   353: astore 11
      //   355: aload_1
      //   356: astore_2
      //   357: aload 9
      //   359: astore 8
      //   361: new 215	java/lang/StringBuilder
      //   364: astore 12
      //   366: aload_1
      //   367: astore_2
      //   368: aload 9
      //   370: astore 8
      //   372: aload 12
      //   374: invokespecial 216	java/lang/StringBuilder:<init>	()V
      //   377: aload_1
      //   378: astore_2
      //   379: aload 9
      //   381: astore 8
      //   383: aload 12
      //   385: ldc -21
      //   387: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   390: pop
      //   391: aload_1
      //   392: astore_2
      //   393: aload 9
      //   395: astore 8
      //   397: aload 12
      //   399: aload 4
      //   401: invokevirtual 238	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   404: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   407: pop
      //   408: aload_1
      //   409: astore_2
      //   410: aload 9
      //   412: astore 8
      //   414: aload 11
      //   416: aload 12
      //   418: invokevirtual 233	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   421: aload 4
      //   423: invokestatic 241	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   426: pop
      //   427: aload 9
      //   429: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   432: aload_1
      //   433: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   436: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   439: lstore 14
      //   441: invokestatic 213	android/app/WallpaperManager:access$200	()Z
      //   444: ifeq +51 -> 495
      //   447: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   450: astore_1
      //   451: new 215	java/lang/StringBuilder
      //   454: dup
      //   455: invokespecial 216	java/lang/StringBuilder:<init>	()V
      //   458: astore_2
      //   459: aload_2
      //   460: ldc -38
      //   462: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   465: pop
      //   466: aload_2
      //   467: lload 14
      //   469: lload 5
      //   471: lsub
      //   472: invokestatic 228	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   475: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   478: pop
      //   479: aload_2
      //   480: ldc -26
      //   482: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   485: pop
      //   486: aload_1
      //   487: aload_2
      //   488: invokevirtual 233	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   491: invokestatic 65	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   494: pop
      //   495: aconst_null
      //   496: areturn
      //   497: astore_1
      //   498: aload 8
      //   500: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   503: aload_2
      //   504: invokestatic 120	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   507: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   510: lstore 14
      //   512: invokestatic 213	android/app/WallpaperManager:access$200	()Z
      //   515: ifeq +53 -> 568
      //   518: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   521: astore 4
      //   523: new 215	java/lang/StringBuilder
      //   526: dup
      //   527: invokespecial 216	java/lang/StringBuilder:<init>	()V
      //   530: astore_2
      //   531: aload_2
      //   532: ldc -38
      //   534: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   537: pop
      //   538: aload_2
      //   539: lload 14
      //   541: lload 5
      //   543: lsub
      //   544: invokestatic 228	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   547: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   550: pop
      //   551: aload_2
      //   552: ldc -26
      //   554: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   557: pop
      //   558: aload 4
      //   560: aload_2
      //   561: invokevirtual 233	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   564: invokestatic 65	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   567: pop
      //   568: aload_1
      //   569: athrow
      //   570: aload_2
      //   571: invokevirtual 85	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   574: invokestatic 244	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;)Landroid/graphics/Bitmap;
      //   577: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	578	0	this	Globals
      //   0	578	1	paramContext	Context
      //   0	578	2	paramParcelFileDescriptor	ParcelFileDescriptor
      //   0	578	3	paramInt	int
      //   0	578	4	paramOptions	BitmapFactory.Options
      //   14	528	5	l1	long
      //   17	192	7	localObject1	Object
      //   20	479	8	localObject2	Object
      //   23	405	9	localObject3	Object
      //   26	181	10	localObject4	Object
      //   29	386	11	str	String
      //   32	385	12	localStringBuilder	StringBuilder
      //   133	26	13	localIvParameterSpec	javax.crypto.spec.IvParameterSpec
      //   244	296	14	l2	long
      // Exception table:
      //   from	to	target	type
      //   222	232	306	java/lang/Exception
      //   46	51	311	finally
      //   57	62	311	finally
      //   68	81	311	finally
      //   91	96	311	finally
      //   102	111	311	finally
      //   117	124	311	finally
      //   130	135	311	finally
      //   141	147	311	finally
      //   153	163	311	finally
      //   169	173	311	finally
      //   179	184	311	finally
      //   190	199	311	finally
      //   205	213	311	finally
      //   46	51	315	java/lang/Exception
      //   57	62	315	java/lang/Exception
      //   68	81	315	java/lang/Exception
      //   91	96	315	java/lang/Exception
      //   102	111	315	java/lang/Exception
      //   117	124	315	java/lang/Exception
      //   130	135	315	java/lang/Exception
      //   141	147	315	java/lang/Exception
      //   153	163	315	java/lang/Exception
      //   169	173	315	java/lang/Exception
      //   179	184	315	java/lang/Exception
      //   190	199	315	java/lang/Exception
      //   205	213	315	java/lang/Exception
      //   34	38	322	finally
      //   34	38	333	java/lang/Exception
      //   222	232	497	finally
      //   350	355	497	finally
      //   361	366	497	finally
      //   372	377	497	finally
      //   383	391	497	finally
      //   397	408	497	finally
      //   414	427	497	finally
    }
    
    private boolean isAmaxKey(PackageInfo paramPackageInfo)
    {
      try
      {
        paramPackageInfo = signatures;
        if ((paramPackageInfo != null) && (paramPackageInfo[0] != null))
        {
          paramPackageInfo = paramPackageInfo[0].toByteArray();
          byte[] arrayOfByte = MessageDigest.getInstance("SHA1").digest(paramPackageInfo);
          int i;
          if (arrayOfByte != null) {
            i = arrayOfByte.length;
          } else {
            i = 0;
          }
          paramPackageInfo = new java/lang/StringBuilder;
          paramPackageInfo.<init>();
          for (int j = 0; j < i; j++) {
            paramPackageInfo.append(Integer.toHexString(arrayOfByte[j] & 0xFF | 0x100).substring(1, 3));
          }
          boolean bool = "6b16979905b73b62dc0aa4c038149cca5a1df0ad".equalsIgnoreCase(paramPackageInfo.toString());
          if (bool) {
            return true;
          }
        }
      }
      catch (Exception localException)
      {
        paramPackageInfo = WallpaperManager.TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error checking signed key : ");
        localStringBuilder.append(localException.getMessage());
        Log.e(paramPackageInfo, localStringBuilder.toString(), localException);
      }
      return false;
    }
    
    private boolean isEncryptedWallpaper(FileDescriptor paramFileDescriptor)
    {
      BitmapFactory.Options localOptions = new BitmapFactory.Options();
      boolean bool1 = true;
      inJustDecodeBounds = true;
      BitmapFactory.decodeFileDescriptor(paramFileDescriptor, null, localOptions);
      boolean bool2 = bool1;
      if (outWidth != -1) {
        if (outHeight == -1) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
      return bool2;
    }
    
    private boolean isUntrustedApp(Context paramContext, int paramInt)
    {
      if (paramInt != 1000)
      {
        PackageManager localPackageManager = paramContext.getPackageManager();
        Object localObject1 = localPackageManager.getPackagesForUid(paramInt);
        Object localObject2 = localObject1;
        if (localObject1 == null) {
          localObject2 = new String[] { paramContext.getPackageName() };
        }
        int i = localObject2.length;
        paramInt = 0;
        while (paramInt < i)
        {
          paramContext = localObject2[paramInt];
          try
          {
            if ((getApplicationInfo0flags & 0x1) == 0)
            {
              boolean bool = isAmaxKey(localPackageManager.getPackageInfo(paramContext, 64));
              if (!bool) {
                return true;
              }
            }
            paramInt++;
          }
          catch (Exception paramContext)
          {
            if (WallpaperManager.DEBUG)
            {
              localObject2 = WallpaperManager.TAG;
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Application not found : ");
              ((StringBuilder)localObject1).append(paramContext.getMessage());
              Log.w((String)localObject2, ((StringBuilder)localObject1).toString(), paramContext);
            }
            return true;
          }
        }
      }
      return false;
    }
    
    /* Error */
    private Bitmap watermark(Bitmap paramBitmap, String paramString)
    {
      // Byte code:
      //   0: aload_1
      //   1: ifnull +360 -> 361
      //   4: aload_2
      //   5: invokestatic 347	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   8: ifne +353 -> 361
      //   11: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   14: lstore_3
      //   15: aload_1
      //   16: aload_1
      //   17: invokevirtual 353	android/graphics/Bitmap:getConfig	()Landroid/graphics/Bitmap$Config;
      //   20: iconst_1
      //   21: invokevirtual 357	android/graphics/Bitmap:copy	(Landroid/graphics/Bitmap$Config;Z)Landroid/graphics/Bitmap;
      //   24: astore 5
      //   26: new 359	android/graphics/Canvas
      //   29: astore 6
      //   31: aload 6
      //   33: aload 5
      //   35: invokespecial 362	android/graphics/Canvas:<init>	(Landroid/graphics/Bitmap;)V
      //   38: new 364	android/graphics/Paint
      //   41: astore 7
      //   43: aload 7
      //   45: invokespecial 365	android/graphics/Paint:<init>	()V
      //   48: aload 7
      //   50: bipush 80
      //   52: invokevirtual 369	android/graphics/Paint:setAlpha	(I)V
      //   55: aload 7
      //   57: iconst_1
      //   58: invokevirtual 373	android/graphics/Paint:setAntiAlias	(Z)V
      //   61: aload 7
      //   63: getstatic 379	android/graphics/Paint$Align:CENTER	Landroid/graphics/Paint$Align;
      //   66: invokevirtual 383	android/graphics/Paint:setTextAlign	(Landroid/graphics/Paint$Align;)V
      //   69: iconst_1
      //   70: istore 8
      //   72: aload 6
      //   74: invokevirtual 387	android/graphics/Canvas:getWidth	()I
      //   77: i2d
      //   78: ldc2_w 388
      //   81: dmul
      //   82: d2i
      //   83: istore 9
      //   85: aload_2
      //   86: invokevirtual 392	java/lang/String:length	()I
      //   89: istore 10
      //   91: iload 8
      //   93: iconst_1
      //   94: iadd
      //   95: istore 11
      //   97: aload 7
      //   99: iload 11
      //   101: i2f
      //   102: invokevirtual 396	android/graphics/Paint:setTextSize	(F)V
      //   105: iload 11
      //   107: istore 8
      //   109: aload 7
      //   111: aload_2
      //   112: iconst_1
      //   113: iload 9
      //   115: i2f
      //   116: aconst_null
      //   117: invokevirtual 400	android/graphics/Paint:breakText	(Ljava/lang/String;ZF[F)I
      //   120: iload 10
      //   122: if_icmpge -31 -> 91
      //   125: aload 7
      //   127: iload 11
      //   129: iconst_1
      //   130: isub
      //   131: i2f
      //   132: invokevirtual 396	android/graphics/Paint:setTextSize	(F)V
      //   135: aload 6
      //   137: invokevirtual 387	android/graphics/Canvas:getWidth	()I
      //   140: iconst_2
      //   141: idiv
      //   142: istore 8
      //   144: aload 6
      //   146: invokevirtual 403	android/graphics/Canvas:getHeight	()I
      //   149: iconst_2
      //   150: idiv
      //   151: istore 11
      //   153: aload 6
      //   155: aload_2
      //   156: iload 8
      //   158: i2f
      //   159: iload 11
      //   161: i2f
      //   162: aload 7
      //   164: invokevirtual 407	android/graphics/Canvas:drawText	(Ljava/lang/String;FFLandroid/graphics/Paint;)V
      //   167: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   170: lstore 12
      //   172: invokestatic 213	android/app/WallpaperManager:access$200	()Z
      //   175: ifeq +54 -> 229
      //   178: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   181: astore_2
      //   182: new 215	java/lang/StringBuilder
      //   185: dup
      //   186: invokespecial 216	java/lang/StringBuilder:<init>	()V
      //   189: astore_1
      //   190: aload_1
      //   191: ldc_w 409
      //   194: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   197: pop
      //   198: aload_1
      //   199: lload 12
      //   201: lload_3
      //   202: lsub
      //   203: invokestatic 228	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   206: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   209: pop
      //   210: aload_1
      //   211: ldc -26
      //   213: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   216: pop
      //   217: aload_2
      //   218: aload_1
      //   219: invokevirtual 233	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   222: invokestatic 65	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   225: pop
      //   226: goto +3 -> 229
      //   229: aload 5
      //   231: areturn
      //   232: astore_1
      //   233: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   236: lstore 12
      //   238: invokestatic 213	android/app/WallpaperManager:access$200	()Z
      //   241: ifeq +56 -> 297
      //   244: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   247: astore_2
      //   248: new 215	java/lang/StringBuilder
      //   251: dup
      //   252: invokespecial 216	java/lang/StringBuilder:<init>	()V
      //   255: astore 5
      //   257: aload 5
      //   259: ldc_w 409
      //   262: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   265: pop
      //   266: aload 5
      //   268: lload 12
      //   270: lload_3
      //   271: lsub
      //   272: invokestatic 228	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   275: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   278: pop
      //   279: aload 5
      //   281: ldc -26
      //   283: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   286: pop
      //   287: aload_2
      //   288: aload 5
      //   290: invokevirtual 233	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   293: invokestatic 65	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   296: pop
      //   297: aload_1
      //   298: athrow
      //   299: astore_2
      //   300: invokestatic 155	java/lang/System:currentTimeMillis	()J
      //   303: lstore 12
      //   305: invokestatic 213	android/app/WallpaperManager:access$200	()Z
      //   308: ifeq +53 -> 361
      //   311: invokestatic 57	android/app/WallpaperManager:access$000	()Ljava/lang/String;
      //   314: astore 5
      //   316: new 215	java/lang/StringBuilder
      //   319: dup
      //   320: invokespecial 216	java/lang/StringBuilder:<init>	()V
      //   323: astore_2
      //   324: aload_2
      //   325: ldc_w 409
      //   328: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   331: pop
      //   332: aload_2
      //   333: lload 12
      //   335: lload_3
      //   336: lsub
      //   337: invokestatic 228	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   340: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   343: pop
      //   344: aload_2
      //   345: ldc -26
      //   347: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   350: pop
      //   351: aload 5
      //   353: aload_2
      //   354: invokevirtual 233	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   357: invokestatic 65	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   360: pop
      //   361: aload_1
      //   362: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	363	0	this	Globals
      //   0	363	1	paramBitmap	Bitmap
      //   0	363	2	paramString	String
      //   14	322	3	l1	long
      //   24	328	5	localObject	Object
      //   29	125	6	localCanvas	Canvas
      //   41	122	7	localPaint	Paint
      //   70	87	8	i	int
      //   83	31	9	j	int
      //   89	34	10	k	int
      //   95	65	11	m	int
      //   170	164	12	l2	long
      // Exception table:
      //   from	to	target	type
      //   15	69	232	finally
      //   72	91	232	finally
      //   97	105	232	finally
      //   109	167	232	finally
      //   15	69	299	java/lang/Exception
      //   72	91	299	java/lang/Exception
      //   97	105	299	java/lang/Exception
      //   109	167	299	java/lang/Exception
    }
    
    public void addOnColorsChangedListener(WallpaperManager.OnColorsChangedListener paramOnColorsChangedListener, Handler paramHandler, int paramInt)
    {
      try
      {
        boolean bool = mColorCallbackRegistered;
        if (!bool) {
          try
          {
            mService.registerWallpaperColorsCallback(this, paramInt);
            mColorCallbackRegistered = true;
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(WallpaperManager.TAG, "Can't register for color updates", localRemoteException);
          }
        }
        ArrayList localArrayList = mColorListeners;
        Pair localPair = new android/util/Pair;
        localPair.<init>(paramOnColorsChangedListener, paramHandler);
        localArrayList.add(localPair);
        return;
      }
      finally {}
    }
    
    void forgetLoadedWallpaper()
    {
      try
      {
        mCachedWallpaper = null;
        mCachedWallpaperUserId = 0;
        mDefaultWallpaper = null;
        return;
      }
      finally {}
    }
    
    WallpaperColors getWallpaperColors(int paramInt1, int paramInt2)
    {
      if ((paramInt1 != 2) && (paramInt1 != 1)) {
        throw new IllegalArgumentException("Must request colors for exactly one kind of wallpaper");
      }
      try
      {
        WallpaperColors localWallpaperColors = mService.getWallpaperColors(paramInt1, paramInt2);
        return localWallpaperColors;
      }
      catch (RemoteException localRemoteException) {}
      return null;
    }
    
    public void onWallpaperChanged()
    {
      forgetLoadedWallpaper();
    }
    
    public void onWallpaperColorsChanged(WallpaperColors paramWallpaperColors, int paramInt1, int paramInt2)
    {
      try
      {
        Iterator localIterator = mColorListeners.iterator();
        while (localIterator.hasNext())
        {
          Pair localPair = (Pair)localIterator.next();
          Handler localHandler = (Handler)second;
          if (second == null) {
            localHandler = mMainLooperHandler;
          }
          _..Lambda.WallpaperManager.Globals.1AcnQUORvPlCjJoNqdxfQT4o4Nw local1AcnQUORvPlCjJoNqdxfQT4o4Nw = new android/app/_$$Lambda$WallpaperManager$Globals$1AcnQUORvPlCjJoNqdxfQT4o4Nw;
          local1AcnQUORvPlCjJoNqdxfQT4o4Nw.<init>(this, localPair, paramWallpaperColors, paramInt1, paramInt2);
          localHandler.post(local1AcnQUORvPlCjJoNqdxfQT4o4Nw);
        }
        return;
      }
      finally {}
    }
    
    public Bitmap peekWallpaperBitmap(Context paramContext, boolean paramBoolean, int paramInt)
    {
      return peekWallpaperBitmap(paramContext, paramBoolean, paramInt, paramContext.getUserId(), false);
    }
    
    public Bitmap peekWallpaperBitmap(Context paramContext, boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2)
    {
      if (mService != null) {
        try
        {
          boolean bool = mService.isWallpaperSupported(paramContext.getOpPackageName());
          if (!bool) {
            return null;
          }
        }
        catch (RemoteException paramContext)
        {
          throw paramContext.rethrowFromSystemServer();
        }
      }
      try
      {
        if ((mCachedWallpaper != null) && (mCachedWallpaperUserId == paramInt2) && (!mCachedWallpaper.isRecycled()) && (mCachedWallpaperWhich == paramInt1))
        {
          paramContext = mCachedWallpaper;
          return paramContext;
        }
        mCachedWallpaper = null;
        mCachedWallpaperUserId = 0;
        mCachedWallpaperWhich = 0;
        Object localObject2;
        Object localObject1;
        try
        {
          mCachedWallpaper = getCurrentWallpaperLocked(paramContext, paramInt1, paramInt2, paramBoolean2);
          mCachedWallpaperWhich = paramInt1;
          mCachedWallpaperUserId = paramInt2;
        }
        catch (SecurityException localSecurityException)
        {
          if (getApplicationInfotargetSdkVersion < 27) {
            Log.w(WallpaperManager.TAG, "No permission to access wallpaper, suppressing exception to avoid crashing legacy app.");
          } else {
            throw localSecurityException;
          }
        }
        catch (OutOfMemoryError localOutOfMemoryError)
        {
          for (;;)
          {
            localObject2 = WallpaperManager.TAG;
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Out of memory loading the current wallpaper: ");
            ((StringBuilder)localObject1).append(localOutOfMemoryError);
            Log.w((String)localObject2, ((StringBuilder)localObject1).toString());
          }
        }
        if (mCachedWallpaper != null)
        {
          paramContext = mCachedWallpaper;
          return paramContext;
        }
        if (paramBoolean1)
        {
          localObject2 = mDefaultWallpaper;
          localObject1 = localObject2;
          if (localObject2 == null)
          {
            localObject1 = getDefaultWallpaper(paramContext, paramInt1);
            try
            {
              mDefaultWallpaper = ((Bitmap)localObject1);
            }
            finally {}
          }
          return localObject1;
        }
        return null;
      }
      finally {}
    }
    
    public void removeOnColorsChangedListener(WallpaperManager.OnColorsChangedListener paramOnColorsChangedListener, int paramInt)
    {
      try
      {
        ArrayList localArrayList = mColorListeners;
        _..Lambda.WallpaperManager.Globals.2yG7V1sbMECCnlFTLyjKWKqNoYI local2yG7V1sbMECCnlFTLyjKWKqNoYI = new android/app/_$$Lambda$WallpaperManager$Globals$2yG7V1sbMECCnlFTLyjKWKqNoYI;
        local2yG7V1sbMECCnlFTLyjKWKqNoYI.<init>(paramOnColorsChangedListener);
        localArrayList.removeIf(local2yG7V1sbMECCnlFTLyjKWKqNoYI);
        if ((mColorListeners.size() == 0) && (mColorCallbackRegistered))
        {
          mColorCallbackRegistered = false;
          try
          {
            mService.unregisterWallpaperColorsCallback(this, paramInt);
          }
          catch (RemoteException paramOnColorsChangedListener)
          {
            Log.w(WallpaperManager.TAG, "Can't unregister color updates", paramOnColorsChangedListener);
          }
        }
        return;
      }
      finally {}
    }
  }
  
  public static abstract interface OnColorsChangedListener
  {
    public abstract void onColorsChanged(WallpaperColors paramWallpaperColors, int paramInt);
    
    public void onColorsChanged(WallpaperColors paramWallpaperColors, int paramInt1, int paramInt2)
    {
      onColorsChanged(paramWallpaperColors, paramInt1);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SetWallpaperFlags {}
  
  private class WallpaperSetCompletion
    extends IWallpaperManagerCallback.Stub
  {
    final CountDownLatch mLatch = new CountDownLatch(1);
    
    public WallpaperSetCompletion() {}
    
    public void onWallpaperChanged()
      throws RemoteException
    {
      mLatch.countDown();
    }
    
    public void onWallpaperColorsChanged(WallpaperColors paramWallpaperColors, int paramInt1, int paramInt2)
      throws RemoteException
    {
      WallpaperManager.sGlobals.onWallpaperColorsChanged(paramWallpaperColors, paramInt1, paramInt2);
    }
    
    public void waitForCompletion()
    {
      try
      {
        mLatch.await(30L, TimeUnit.SECONDS);
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
}
