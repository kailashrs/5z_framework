package android.content.pm;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build.FEATURES;
import android.provider.Settings.Secure;
import android.util.ArraySet;
import android.util.Slog;
import android.view.WindowManager.LayoutParams;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AspectRatioChecker
{
  private static final String ANDROID_APP_PREFIX = "com.android";
  private static final String ANDROID_SYSTEM = "android";
  private static final String ASUS_APP_PREFIX = "com.asus";
  private static final String COLUMN_NAME_IS_GAME = "is_game";
  private static final String COLUMN_NAME_PACKAGE_NAME = "packagename";
  public static final String DATA_SCALING_FOLDER = "/data/system/appscaling/";
  public static final String FILE_ASPECT_RATIO_CONFIG = "app_aspect_ratio_config.xml";
  public static final String FILE_NOTCH_CONFIG = "app_notch_config.xml";
  private static final String GAME_APP_PROVIDER_URI = "content://com.asus.focusapplistener.game.GameAppProvider";
  private static final String GOOGLE_APP_PREFIX = "com.google";
  private static final float MAX_ASPECT_FULL_SCREEN = Float.MAX_VALUE;
  private static final float MAX_ASPECT_SIXTEEN_NINE = 1.78F;
  private static final String SYS_SCALING_FOLDER = "/system/etc/appscaling/";
  private static final String TAG = "AspectRatioChecker";
  private static AspectRatioChecker sInstance;
  private final HashMap<String, Float> mAppAspectConfigs = new HashMap();
  private final HashMap<String, NotchConfig> mAppNotchConfigs = new HashMap();
  private long mAspectConfigLastModified = -1L;
  private boolean mGameGenieScalingCtrlEnabled = true;
  private long mNotchConfigLastModified = -1L;
  private boolean mSysScalingCtrlEnabled = true;
  private final ArraySet<String> mSystemApps = new ArraySet();
  private boolean mSystemReady = false;
  private final HashMap<String, NotchConfig> mWinNotchConfigs = new HashMap();
  
  private AspectRatioChecker()
  {
    refresh();
  }
  
  public static AspectRatioChecker getInstance()
  {
    if (sInstance == null) {
      sInstance = new AspectRatioChecker();
    }
    return sInstance;
  }
  
  public static boolean isVipApp(String paramString)
  {
    return (paramString != null) && ((paramString.equals("android")) || (paramString.startsWith("com.android")) || (paramString.startsWith("com.google")) || (paramString.startsWith("com.asus")));
  }
  
  /* Error */
  private void parseAspectConfig(File arg1)
  {
    // Byte code:
    //   0: new 80	java/util/HashMap
    //   3: dup
    //   4: invokespecial 81	java/util/HashMap:<init>	()V
    //   7: astore_2
    //   8: new 125	java/io/FileReader
    //   11: dup
    //   12: aload_1
    //   13: invokespecial 127	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   16: astore_3
    //   17: invokestatic 133	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   20: astore 4
    //   22: aload 4
    //   24: aload_3
    //   25: invokeinterface 139 2 0
    //   30: aload 4
    //   32: invokeinterface 143 1 0
    //   37: istore 5
    //   39: iload 5
    //   41: iconst_2
    //   42: if_icmpeq +12 -> 54
    //   45: iload 5
    //   47: iconst_1
    //   48: if_icmpeq +6 -> 54
    //   51: goto -21 -> 30
    //   54: iload 5
    //   56: iconst_2
    //   57: if_icmpne +274 -> 331
    //   60: aload 4
    //   62: invokeinterface 147 1 0
    //   67: ldc -107
    //   69: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   72: ifne +6 -> 78
    //   75: goto +256 -> 331
    //   78: aload 4
    //   80: invokestatic 155	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   83: aload 4
    //   85: invokeinterface 158 1 0
    //   90: istore 5
    //   92: iload 5
    //   94: iconst_1
    //   95: if_icmpne +10 -> 105
    //   98: aload_3
    //   99: invokestatic 164	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   102: goto +301 -> 403
    //   105: aload 4
    //   107: invokeinterface 147 1 0
    //   112: astore 6
    //   114: ldc -90
    //   116: aload 6
    //   118: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   121: ifeq +100 -> 221
    //   124: aload 4
    //   126: aconst_null
    //   127: ldc -88
    //   129: invokeinterface 172 3 0
    //   134: astore 6
    //   136: aload 6
    //   138: ifnonnull +63 -> 201
    //   141: new 174	java/lang/StringBuilder
    //   144: astore 6
    //   146: aload 6
    //   148: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   151: aload 6
    //   153: ldc -79
    //   155: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   158: pop
    //   159: aload 6
    //   161: aload_1
    //   162: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload 6
    //   168: ldc -70
    //   170: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: pop
    //   174: aload 6
    //   176: aload 4
    //   178: invokeinterface 189 1 0
    //   183: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: ldc 51
    //   189: aload 6
    //   191: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   194: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   197: pop
    //   198: goto +15 -> 213
    //   201: aload_2
    //   202: aload 6
    //   204: ldc 45
    //   206: invokestatic 204	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   209: invokevirtual 208	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   212: pop
    //   213: aload 4
    //   215: invokestatic 211	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   218: goto -140 -> 78
    //   221: ldc -43
    //   223: aload 6
    //   225: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   228: ifeq +100 -> 328
    //   231: aload 4
    //   233: aconst_null
    //   234: ldc -88
    //   236: invokeinterface 172 3 0
    //   241: astore 6
    //   243: aload 6
    //   245: ifnonnull +63 -> 308
    //   248: new 174	java/lang/StringBuilder
    //   251: astore 6
    //   253: aload 6
    //   255: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   258: aload 6
    //   260: ldc -41
    //   262: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   265: pop
    //   266: aload 6
    //   268: aload_1
    //   269: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   272: pop
    //   273: aload 6
    //   275: ldc -70
    //   277: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   280: pop
    //   281: aload 6
    //   283: aload 4
    //   285: invokeinterface 189 1 0
    //   290: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: pop
    //   294: ldc 51
    //   296: aload 6
    //   298: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   301: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   304: pop
    //   305: goto +15 -> 320
    //   308: aload_2
    //   309: aload 6
    //   311: ldc 43
    //   313: invokestatic 204	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   316: invokevirtual 208	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   319: pop
    //   320: aload 4
    //   322: invokestatic 211	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   325: goto -247 -> 78
    //   328: goto -250 -> 78
    //   331: aload_3
    //   332: invokestatic 164	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   335: return
    //   336: astore_1
    //   337: goto +96 -> 433
    //   340: astore 4
    //   342: new 174	java/lang/StringBuilder
    //   345: astore 6
    //   347: aload 6
    //   349: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   352: aload 6
    //   354: ldc -39
    //   356: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   359: pop
    //   360: aload 6
    //   362: aload_1
    //   363: invokevirtual 220	java/io/File:getName	()Ljava/lang/String;
    //   366: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   369: pop
    //   370: aload 6
    //   372: ldc -34
    //   374: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   377: pop
    //   378: aload 6
    //   380: aload 4
    //   382: invokevirtual 225	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   385: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   388: pop
    //   389: ldc 51
    //   391: aload 6
    //   393: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   396: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   399: pop
    //   400: goto -302 -> 98
    //   403: aload_0
    //   404: getfield 83	android/content/pm/AspectRatioChecker:mAppAspectConfigs	Ljava/util/HashMap;
    //   407: astore_1
    //   408: aload_1
    //   409: monitorenter
    //   410: aload_0
    //   411: getfield 83	android/content/pm/AspectRatioChecker:mAppAspectConfigs	Ljava/util/HashMap;
    //   414: invokevirtual 228	java/util/HashMap:clear	()V
    //   417: aload_0
    //   418: getfield 83	android/content/pm/AspectRatioChecker:mAppAspectConfigs	Ljava/util/HashMap;
    //   421: aload_2
    //   422: invokevirtual 232	java/util/HashMap:putAll	(Ljava/util/Map;)V
    //   425: aload_1
    //   426: monitorexit
    //   427: return
    //   428: astore_2
    //   429: aload_1
    //   430: monitorexit
    //   431: aload_2
    //   432: athrow
    //   433: aload_3
    //   434: invokestatic 164	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   437: aload_1
    //   438: athrow
    //   439: astore_2
    //   440: new 174	java/lang/StringBuilder
    //   443: dup
    //   444: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   447: astore_2
    //   448: aload_2
    //   449: ldc -22
    //   451: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   454: pop
    //   455: aload_2
    //   456: aload_1
    //   457: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   460: pop
    //   461: ldc 51
    //   463: aload_2
    //   464: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   467: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   470: pop
    //   471: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	472	0	this	AspectRatioChecker
    //   7	415	2	localHashMap	HashMap
    //   428	4	2	localObject1	Object
    //   439	1	2	localException1	Exception
    //   447	17	2	localStringBuilder	StringBuilder
    //   16	418	3	localFileReader	java.io.FileReader
    //   20	301	4	localXmlPullParser	org.xmlpull.v1.XmlPullParser
    //   340	41	4	localException2	Exception
    //   37	59	5	i	int
    //   112	280	6	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   17	30	336	finally
    //   30	39	336	finally
    //   60	75	336	finally
    //   78	92	336	finally
    //   105	136	336	finally
    //   141	198	336	finally
    //   201	213	336	finally
    //   213	218	336	finally
    //   221	243	336	finally
    //   248	305	336	finally
    //   308	320	336	finally
    //   320	325	336	finally
    //   342	400	336	finally
    //   17	30	340	java/lang/Exception
    //   30	39	340	java/lang/Exception
    //   60	75	340	java/lang/Exception
    //   78	92	340	java/lang/Exception
    //   105	136	340	java/lang/Exception
    //   141	198	340	java/lang/Exception
    //   201	213	340	java/lang/Exception
    //   213	218	340	java/lang/Exception
    //   221	243	340	java/lang/Exception
    //   248	305	340	java/lang/Exception
    //   308	320	340	java/lang/Exception
    //   320	325	340	java/lang/Exception
    //   410	427	428	finally
    //   429	431	428	finally
    //   8	17	439	java/lang/Exception
  }
  
  /* Error */
  private void parseNotchConfig(File arg1)
  {
    // Byte code:
    //   0: new 80	java/util/HashMap
    //   3: dup
    //   4: invokespecial 81	java/util/HashMap:<init>	()V
    //   7: astore_2
    //   8: new 80	java/util/HashMap
    //   11: dup
    //   12: invokespecial 81	java/util/HashMap:<init>	()V
    //   15: astore_3
    //   16: aload_0
    //   17: iconst_1
    //   18: putfield 97	android/content/pm/AspectRatioChecker:mSysScalingCtrlEnabled	Z
    //   21: aload_0
    //   22: iconst_1
    //   23: putfield 99	android/content/pm/AspectRatioChecker:mGameGenieScalingCtrlEnabled	Z
    //   26: new 125	java/io/FileReader
    //   29: dup
    //   30: aload_1
    //   31: invokespecial 127	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   34: astore 4
    //   36: invokestatic 133	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   39: astore 5
    //   41: aload 5
    //   43: aload 4
    //   45: invokeinterface 139 2 0
    //   50: aload 5
    //   52: invokeinterface 143 1 0
    //   57: istore 6
    //   59: iload 6
    //   61: iconst_2
    //   62: if_icmpeq +12 -> 74
    //   65: iload 6
    //   67: iconst_1
    //   68: if_icmpeq +6 -> 74
    //   71: goto -21 -> 50
    //   74: iload 6
    //   76: iconst_2
    //   77: if_icmpne +629 -> 706
    //   80: aload 5
    //   82: invokeinterface 147 1 0
    //   87: ldc -107
    //   89: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   92: ifne +6 -> 98
    //   95: goto +611 -> 706
    //   98: aload 5
    //   100: invokestatic 155	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   103: aload 5
    //   105: invokeinterface 158 1 0
    //   110: istore 6
    //   112: iload 6
    //   114: iconst_1
    //   115: if_icmpne +11 -> 126
    //   118: aload 4
    //   120: invokestatic 164	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   123: goto +656 -> 779
    //   126: aload 5
    //   128: invokeinterface 147 1 0
    //   133: astore 7
    //   135: ldc -88
    //   137: aload 7
    //   139: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   142: ifeq +212 -> 354
    //   145: aload 5
    //   147: aconst_null
    //   148: ldc -19
    //   150: invokeinterface 172 3 0
    //   155: astore 8
    //   157: aload 8
    //   159: ifnonnull +63 -> 222
    //   162: new 174	java/lang/StringBuilder
    //   165: astore 7
    //   167: aload 7
    //   169: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   172: aload 7
    //   174: ldc -17
    //   176: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: aload 7
    //   182: aload_1
    //   183: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload 7
    //   189: ldc -70
    //   191: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: aload 7
    //   197: aload 5
    //   199: invokeinterface 189 1 0
    //   204: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: ldc 51
    //   210: aload 7
    //   212: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   215: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   218: pop
    //   219: goto +127 -> 346
    //   222: aload_2
    //   223: aload 8
    //   225: invokevirtual 243	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   228: checkcast 8	android/content/pm/AspectRatioChecker$NotchConfig
    //   231: astore 9
    //   233: aload 9
    //   235: astore 7
    //   237: aload 9
    //   239: ifnonnull +23 -> 262
    //   242: new 8	android/content/pm/AspectRatioChecker$NotchConfig
    //   245: astore 7
    //   247: aload 7
    //   249: aconst_null
    //   250: invokespecial 246	android/content/pm/AspectRatioChecker$NotchConfig:<init>	(Landroid/content/pm/AspectRatioChecker$1;)V
    //   253: aload_2
    //   254: aload 8
    //   256: aload 7
    //   258: invokevirtual 208	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   261: pop
    //   262: aload 7
    //   264: aload 5
    //   266: aconst_null
    //   267: ldc -8
    //   269: invokeinterface 172 3 0
    //   274: invokestatic 253	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   277: putfield 255	android/content/pm/AspectRatioChecker$NotchConfig:fillNotch	Z
    //   280: aload 5
    //   282: aconst_null
    //   283: ldc_w 257
    //   286: invokeinterface 172 3 0
    //   291: astore 9
    //   293: aload 9
    //   295: ifnull +13 -> 308
    //   298: aload 7
    //   300: aload 9
    //   302: invokestatic 253	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   305: putfield 259	android/content/pm/AspectRatioChecker$NotchConfig:canfillNotchInLand	Z
    //   308: aload 7
    //   310: aload 5
    //   312: aconst_null
    //   313: ldc_w 261
    //   316: invokeinterface 172 3 0
    //   321: invokestatic 253	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   324: putfield 263	android/content/pm/AspectRatioChecker$NotchConfig:locked	Z
    //   327: aload 7
    //   329: aload 5
    //   331: aconst_null
    //   332: ldc_w 265
    //   335: invokeinterface 172 3 0
    //   340: invokestatic 253	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   343: putfield 268	android/content/pm/AspectRatioChecker$NotchConfig:mustRestart	Z
    //   346: aload 5
    //   348: invokestatic 211	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   351: goto -253 -> 98
    //   354: ldc_w 270
    //   357: aload 7
    //   359: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   362: ifeq +257 -> 619
    //   365: aload 5
    //   367: aconst_null
    //   368: ldc -19
    //   370: invokeinterface 172 3 0
    //   375: astore 8
    //   377: aload 8
    //   379: ifnonnull +64 -> 443
    //   382: new 174	java/lang/StringBuilder
    //   385: astore 7
    //   387: aload 7
    //   389: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   392: aload 7
    //   394: ldc_w 272
    //   397: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   400: pop
    //   401: aload 7
    //   403: aload_1
    //   404: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   407: pop
    //   408: aload 7
    //   410: ldc -70
    //   412: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   415: pop
    //   416: aload 7
    //   418: aload 5
    //   420: invokeinterface 189 1 0
    //   425: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   428: pop
    //   429: ldc 51
    //   431: aload 7
    //   433: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   436: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   439: pop
    //   440: goto +171 -> 611
    //   443: aload_3
    //   444: aload 8
    //   446: invokevirtual 243	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   449: checkcast 8	android/content/pm/AspectRatioChecker$NotchConfig
    //   452: astore 9
    //   454: aload 9
    //   456: astore 7
    //   458: aload 9
    //   460: ifnonnull +23 -> 483
    //   463: new 8	android/content/pm/AspectRatioChecker$NotchConfig
    //   466: astore 7
    //   468: aload 7
    //   470: aconst_null
    //   471: invokespecial 246	android/content/pm/AspectRatioChecker$NotchConfig:<init>	(Landroid/content/pm/AspectRatioChecker$1;)V
    //   474: aload_3
    //   475: aload 8
    //   477: aload 7
    //   479: invokevirtual 208	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   482: pop
    //   483: aload 7
    //   485: aload 5
    //   487: aconst_null
    //   488: ldc -8
    //   490: invokeinterface 172 3 0
    //   495: invokestatic 253	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   498: putfield 255	android/content/pm/AspectRatioChecker$NotchConfig:fillNotch	Z
    //   501: aload 7
    //   503: aload 5
    //   505: aconst_null
    //   506: ldc_w 261
    //   509: invokeinterface 172 3 0
    //   514: invokestatic 253	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   517: putfield 263	android/content/pm/AspectRatioChecker$NotchConfig:locked	Z
    //   520: aload 5
    //   522: aconst_null
    //   523: ldc_w 274
    //   526: invokeinterface 172 3 0
    //   531: astore 9
    //   533: aload 9
    //   535: ifnull +76 -> 611
    //   538: aload 7
    //   540: aload 9
    //   542: invokestatic 278	java/lang/Float:parseFloat	(Ljava/lang/String;)F
    //   545: putfield 280	android/content/pm/AspectRatioChecker$NotchConfig:minAspect	F
    //   548: goto +63 -> 611
    //   551: astore 9
    //   553: new 174	java/lang/StringBuilder
    //   556: astore 7
    //   558: aload 7
    //   560: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   563: aload 7
    //   565: ldc_w 282
    //   568: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   571: pop
    //   572: aload 7
    //   574: aload 8
    //   576: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   579: pop
    //   580: aload 7
    //   582: ldc_w 284
    //   585: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   588: pop
    //   589: aload 7
    //   591: aload 9
    //   593: invokevirtual 225	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   596: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   599: pop
    //   600: ldc 51
    //   602: aload 7
    //   604: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   607: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   610: pop
    //   611: aload 5
    //   613: invokestatic 211	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   616: goto +87 -> 703
    //   619: ldc_w 286
    //   622: aload 7
    //   624: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   627: ifeq +76 -> 703
    //   630: aload 5
    //   632: aconst_null
    //   633: ldc_w 288
    //   636: invokeinterface 172 3 0
    //   641: invokestatic 253	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   644: istore 10
    //   646: aload 5
    //   648: aconst_null
    //   649: ldc -19
    //   651: invokeinterface 172 3 0
    //   656: astore 7
    //   658: ldc_w 290
    //   661: aload 7
    //   663: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   666: ifeq +12 -> 678
    //   669: aload_0
    //   670: iload 10
    //   672: putfield 97	android/content/pm/AspectRatioChecker:mSysScalingCtrlEnabled	Z
    //   675: goto +20 -> 695
    //   678: ldc_w 292
    //   681: aload 7
    //   683: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   686: ifeq +9 -> 695
    //   689: aload_0
    //   690: iload 10
    //   692: putfield 99	android/content/pm/AspectRatioChecker:mGameGenieScalingCtrlEnabled	Z
    //   695: aload 5
    //   697: invokestatic 211	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   700: goto +3 -> 703
    //   703: goto -605 -> 98
    //   706: aload 4
    //   708: invokestatic 164	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   711: return
    //   712: astore_1
    //   713: goto +133 -> 846
    //   716: astore 7
    //   718: new 174	java/lang/StringBuilder
    //   721: astore 9
    //   723: aload 9
    //   725: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   728: aload 9
    //   730: ldc -39
    //   732: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   735: pop
    //   736: aload 9
    //   738: aload_1
    //   739: invokevirtual 220	java/io/File:getName	()Ljava/lang/String;
    //   742: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   745: pop
    //   746: aload 9
    //   748: ldc -34
    //   750: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   753: pop
    //   754: aload 9
    //   756: aload 7
    //   758: invokevirtual 225	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   761: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   764: pop
    //   765: ldc 51
    //   767: aload 9
    //   769: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   772: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   775: pop
    //   776: goto -658 -> 118
    //   779: aload_0
    //   780: getfield 85	android/content/pm/AspectRatioChecker:mAppNotchConfigs	Ljava/util/HashMap;
    //   783: astore_1
    //   784: aload_1
    //   785: monitorenter
    //   786: aload_0
    //   787: getfield 85	android/content/pm/AspectRatioChecker:mAppNotchConfigs	Ljava/util/HashMap;
    //   790: invokevirtual 228	java/util/HashMap:clear	()V
    //   793: aload_0
    //   794: getfield 85	android/content/pm/AspectRatioChecker:mAppNotchConfigs	Ljava/util/HashMap;
    //   797: aload_2
    //   798: invokevirtual 232	java/util/HashMap:putAll	(Ljava/util/Map;)V
    //   801: aload_1
    //   802: monitorexit
    //   803: aload_0
    //   804: getfield 87	android/content/pm/AspectRatioChecker:mWinNotchConfigs	Ljava/util/HashMap;
    //   807: astore_1
    //   808: aload_1
    //   809: monitorenter
    //   810: aload_0
    //   811: getfield 87	android/content/pm/AspectRatioChecker:mWinNotchConfigs	Ljava/util/HashMap;
    //   814: invokevirtual 228	java/util/HashMap:clear	()V
    //   817: aload_0
    //   818: getfield 87	android/content/pm/AspectRatioChecker:mWinNotchConfigs	Ljava/util/HashMap;
    //   821: aload_3
    //   822: invokevirtual 232	java/util/HashMap:putAll	(Ljava/util/Map;)V
    //   825: aload_1
    //   826: monitorexit
    //   827: aload_0
    //   828: invokespecial 295	android/content/pm/AspectRatioChecker:refreshSettings	()V
    //   831: return
    //   832: astore 7
    //   834: aload_1
    //   835: monitorexit
    //   836: aload 7
    //   838: athrow
    //   839: astore 7
    //   841: aload_1
    //   842: monitorexit
    //   843: aload 7
    //   845: athrow
    //   846: aload 4
    //   848: invokestatic 164	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   851: aload_1
    //   852: athrow
    //   853: astore 7
    //   855: new 174	java/lang/StringBuilder
    //   858: dup
    //   859: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   862: astore 7
    //   864: aload 7
    //   866: ldc -22
    //   868: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   871: pop
    //   872: aload 7
    //   874: aload_1
    //   875: invokevirtual 184	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   878: pop
    //   879: ldc 51
    //   881: aload 7
    //   883: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   886: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   889: pop
    //   890: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	891	0	this	AspectRatioChecker
    //   7	791	2	localHashMap1	HashMap
    //   15	807	3	localHashMap2	HashMap
    //   34	813	4	localFileReader	java.io.FileReader
    //   39	657	5	localXmlPullParser	org.xmlpull.v1.XmlPullParser
    //   57	59	6	i	int
    //   133	549	7	localObject1	Object
    //   716	41	7	localException1	Exception
    //   832	5	7	localObject2	Object
    //   839	5	7	localObject3	Object
    //   853	1	7	localException2	Exception
    //   862	20	7	localStringBuilder1	StringBuilder
    //   155	420	8	str	String
    //   231	310	9	localObject4	Object
    //   551	41	9	localException3	Exception
    //   721	47	9	localStringBuilder2	StringBuilder
    //   644	47	10	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   538	548	551	java/lang/Exception
    //   36	50	712	finally
    //   50	59	712	finally
    //   80	95	712	finally
    //   98	112	712	finally
    //   126	157	712	finally
    //   162	219	712	finally
    //   222	233	712	finally
    //   242	253	712	finally
    //   253	262	712	finally
    //   262	293	712	finally
    //   298	308	712	finally
    //   308	346	712	finally
    //   346	351	712	finally
    //   354	377	712	finally
    //   382	440	712	finally
    //   443	454	712	finally
    //   463	474	712	finally
    //   474	483	712	finally
    //   483	533	712	finally
    //   538	548	712	finally
    //   553	611	712	finally
    //   611	616	712	finally
    //   619	675	712	finally
    //   678	695	712	finally
    //   695	700	712	finally
    //   718	776	712	finally
    //   36	50	716	java/lang/Exception
    //   50	59	716	java/lang/Exception
    //   80	95	716	java/lang/Exception
    //   98	112	716	java/lang/Exception
    //   126	157	716	java/lang/Exception
    //   162	219	716	java/lang/Exception
    //   222	233	716	java/lang/Exception
    //   242	253	716	java/lang/Exception
    //   253	262	716	java/lang/Exception
    //   262	293	716	java/lang/Exception
    //   298	308	716	java/lang/Exception
    //   308	346	716	java/lang/Exception
    //   346	351	716	java/lang/Exception
    //   354	377	716	java/lang/Exception
    //   382	440	716	java/lang/Exception
    //   443	454	716	java/lang/Exception
    //   463	474	716	java/lang/Exception
    //   474	483	716	java/lang/Exception
    //   483	533	716	java/lang/Exception
    //   553	611	716	java/lang/Exception
    //   611	616	716	java/lang/Exception
    //   619	675	716	java/lang/Exception
    //   678	695	716	java/lang/Exception
    //   695	700	716	java/lang/Exception
    //   810	827	832	finally
    //   834	836	832	finally
    //   786	803	839	finally
    //   841	843	839	finally
    //   26	36	853	java/lang/Exception
  }
  
  private void refreshSettings()
  {
    if (!mSystemReady) {
      return;
    }
    ActivityThread localActivityThread = ActivityThread.currentActivityThread();
    if (localActivityThread == null) {
      return;
    }
    ContentResolver localContentResolver = localActivityThread.getSystemContext().getContentResolver();
    try
    {
      Settings.Secure.putInt(localContentResolver, "sys_scaling_ctrl", mSysScalingCtrlEnabled);
    }
    catch (Exception localException1)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Write settings SYSTEM_SCALING_CONTROL failed, err: ");
      localStringBuilder2.append(localException1.getMessage());
      Slog.w("AspectRatioChecker", localStringBuilder2.toString());
    }
    try
    {
      Settings.Secure.putInt(localContentResolver, "gg_scaling_ctrl", mGameGenieScalingCtrlEnabled);
    }
    catch (Exception localException2)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Write settings GAME_GENIE_SCALING_CONTROL failed, err: ");
      localStringBuilder1.append(localException2.getMessage());
      Slog.w("AspectRatioChecker", localStringBuilder1.toString());
    }
  }
  
  public float getActualAspectRatio(String paramString, boolean paramBoolean)
  {
    synchronized (mAppAspectConfigs)
    {
      Float localFloat = (Float)mAppAspectConfigs.get(paramString);
      if (localFloat != null)
      {
        float f = localFloat.floatValue();
        return f;
      }
      ??? = mSystemApps;
      if (!paramBoolean) {
        try
        {
          if (!mSystemApps.contains(paramString)) {
            break label80;
          }
        }
        finally
        {
          break label99;
        }
      }
      if (isVipApp(paramString)) {
        return Float.MAX_VALUE;
      }
      label80:
      if (paramBoolean) {
        mSystemApps.add(paramString);
      }
      return -1.0F;
      label99:
      throw paramString;
    }
  }
  
  public float getMinAspect(WindowManager.LayoutParams arg1)
  {
    Object localObject1 = ???.getTitle().toString();
    synchronized (mWinNotchConfigs)
    {
      localObject1 = (NotchConfig)mWinNotchConfigs.get(localObject1);
      if (localObject1 == null) {
        return 0.0F;
      }
      float f = minAspect;
      return f;
    }
  }
  
  public List<String> getRecordedApps()
  {
    synchronized (mAppAspectConfigs)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mAppAspectConfigs.keySet());
      return localArrayList;
    }
  }
  
  public boolean isFillNotchRegion(String paramString, PackageUserState paramPackageUserState)
  {
    if (mustFillNotchRegion(paramString)) {
      return true;
    }
    if (mustNotFillNotchRegion(paramString)) {
      return false;
    }
    return fillNotchRegion;
  }
  
  /* Error */
  public boolean isGameApp(String paramString)
  {
    // Byte code:
    //   0: invokestatic 301	android/app/ActivityThread:currentActivityThread	()Landroid/app/ActivityThread;
    //   3: astore_2
    //   4: aload_2
    //   5: ifnonnull +5 -> 10
    //   8: iconst_0
    //   9: ireturn
    //   10: aload_2
    //   11: invokevirtual 305	android/app/ActivityThread:getSystemContext	()Landroid/app/ContextImpl;
    //   14: invokevirtual 311	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   17: astore_3
    //   18: aconst_null
    //   19: astore 4
    //   21: aconst_null
    //   22: astore_2
    //   23: aload_3
    //   24: ldc 37
    //   26: invokestatic 384	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   29: iconst_1
    //   30: anewarray 112	java/lang/String
    //   33: dup
    //   34: iconst_0
    //   35: ldc 22
    //   37: aastore
    //   38: ldc_w 386
    //   41: iconst_1
    //   42: anewarray 112	java/lang/String
    //   45: dup
    //   46: iconst_0
    //   47: aload_1
    //   48: aastore
    //   49: aconst_null
    //   50: invokevirtual 392	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   53: astore_1
    //   54: aload_1
    //   55: ifnull +61 -> 116
    //   58: aload_1
    //   59: astore_2
    //   60: aload_1
    //   61: astore 4
    //   63: aload_1
    //   64: invokeinterface 398 1 0
    //   69: ifeq +47 -> 116
    //   72: aload_1
    //   73: astore_2
    //   74: aload_1
    //   75: astore 4
    //   77: ldc_w 400
    //   80: aload_1
    //   81: aload_1
    //   82: ldc 22
    //   84: invokeinterface 404 2 0
    //   89: invokeinterface 408 2 0
    //   94: invokevirtual 116	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   97: istore 5
    //   99: aload_1
    //   100: ifnull +13 -> 113
    //   103: aload_1
    //   104: invokeinterface 411 1 0
    //   109: goto +4 -> 113
    //   112: astore_1
    //   113: iload 5
    //   115: ireturn
    //   116: aload_1
    //   117: ifnull +86 -> 203
    //   120: aload_1
    //   121: invokeinterface 411 1 0
    //   126: goto +77 -> 203
    //   129: astore_1
    //   130: goto -4 -> 126
    //   133: astore_1
    //   134: goto +71 -> 205
    //   137: astore_3
    //   138: aload 4
    //   140: astore_2
    //   141: new 174	java/lang/StringBuilder
    //   144: astore_1
    //   145: aload 4
    //   147: astore_2
    //   148: aload_1
    //   149: invokespecial 175	java/lang/StringBuilder:<init>	()V
    //   152: aload 4
    //   154: astore_2
    //   155: aload_1
    //   156: ldc_w 413
    //   159: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: aload 4
    //   165: astore_2
    //   166: aload_1
    //   167: aload_3
    //   168: invokevirtual 225	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   171: invokevirtual 181	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload 4
    //   177: astore_2
    //   178: ldc 51
    //   180: aload_1
    //   181: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: invokestatic 198	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   187: pop
    //   188: aload 4
    //   190: ifnull +13 -> 203
    //   193: aload 4
    //   195: invokeinterface 411 1 0
    //   200: goto -74 -> 126
    //   203: iconst_0
    //   204: ireturn
    //   205: aload_2
    //   206: ifnull +13 -> 219
    //   209: aload_2
    //   210: invokeinterface 411 1 0
    //   215: goto +4 -> 219
    //   218: astore_2
    //   219: aload_1
    //   220: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	221	0	this	AspectRatioChecker
    //   0	221	1	paramString	String
    //   3	207	2	localObject	Object
    //   218	1	2	localException1	Exception
    //   17	7	3	localContentResolver	ContentResolver
    //   137	31	3	localException2	Exception
    //   19	175	4	str	String
    //   97	17	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   103	109	112	java/lang/Exception
    //   120	126	129	java/lang/Exception
    //   193	200	129	java/lang/Exception
    //   23	54	133	finally
    //   63	72	133	finally
    //   77	99	133	finally
    //   141	145	133	finally
    //   148	152	133	finally
    //   155	163	133	finally
    //   166	175	133	finally
    //   178	188	133	finally
    //   23	54	137	java/lang/Exception
    //   63	72	137	java/lang/Exception
    //   77	99	137	java/lang/Exception
    //   209	215	218	java/lang/Exception
  }
  
  public boolean mustFillNotchRegion(WindowManager.LayoutParams arg1)
  {
    if (mSysScalingCtrlEnabled) {
      return false;
    }
    int i = systemUiVisibility;
    int j = subtreeSystemUiVisibility;
    if (((flags & 0x400) == 0) && (((i | j) & 0x4) == 0)) {
      j = 0;
    } else {
      j = 1;
    }
    if (j == 0) {
      return false;
    }
    Object localObject1 = ???.getTitle().toString();
    synchronized (mWinNotchConfigs)
    {
      localObject1 = (NotchConfig)mWinNotchConfigs.get(localObject1);
      if (localObject1 == null) {
        return false;
      }
      boolean bool = fillNotch;
      return bool;
    }
  }
  
  public boolean mustFillNotchRegion(String paramString)
  {
    synchronized (mAppNotchConfigs)
    {
      NotchConfig localNotchConfig = (NotchConfig)mAppNotchConfigs.get(paramString);
      if (localNotchConfig != null)
      {
        if ((fillNotch) && (locked)) {
          return true;
        }
        if ((!fillNotch) && (locked)) {
          return false;
        }
      }
      return (paramString != null) && (paramString.startsWith("com.asus"));
    }
  }
  
  public boolean mustNotFillNotchRegion(WindowManager.LayoutParams arg1)
  {
    Object localObject1 = ???.getTitle().toString();
    synchronized (mWinNotchConfigs)
    {
      localObject1 = (NotchConfig)mWinNotchConfigs.get(localObject1);
      return (localObject1 != null) && (!fillNotch) && (locked);
    }
  }
  
  public boolean mustNotFillNotchRegion(String paramString)
  {
    synchronized (mAppNotchConfigs)
    {
      paramString = (NotchConfig)mAppNotchConfigs.get(paramString);
      return (paramString != null) && (!fillNotch) && (locked);
    }
  }
  
  public boolean mustNotFillNotchRegionInLandscape(String paramString)
  {
    synchronized (mAppNotchConfigs)
    {
      NotchConfig localNotchConfig = (NotchConfig)mAppNotchConfigs.get(paramString);
      if (localNotchConfig != null)
      {
        boolean bool = canfillNotchInLand;
        return true ^ bool;
      }
      return (paramString != null) && (paramString.startsWith("com.asus"));
    }
  }
  
  public boolean mustRestart(PackageParser.Package paramPackage)
  {
    if (!Build.FEATURES.ENABLE_NOTCH_UI) {
      return false;
    }
    if (reqFeatures != null)
    {
      ??? = reqFeatures.iterator();
      while (((Iterator)???).hasNext()) {
        if ("android.software.vr.mode".equals(nextname)) {
          return true;
        }
      }
    }
    if (isGameApp(packageName)) {
      return true;
    }
    synchronized (mAppNotchConfigs)
    {
      paramPackage = (NotchConfig)mAppNotchConfigs.get(packageName);
      if (paramPackage != null)
      {
        boolean bool = mustRestart;
        return bool;
      }
      return false;
    }
  }
  
  public void refresh()
  {
    File localFile1 = new File("/data/system/appscaling/app_aspect_ratio_config.xml");
    File localFile2;
    if (localFile1.exists())
    {
      localFile2 = localFile1;
      if (localFile1.canRead()) {}
    }
    else
    {
      localFile2 = new File("/system/etc/appscaling/app_aspect_ratio_config.xml");
    }
    long l;
    if ((localFile2.exists()) && (localFile2.canRead()))
    {
      l = localFile2.lastModified();
      if (mAspectConfigLastModified != l)
      {
        parseAspectConfig(localFile2);
        mAspectConfigLastModified = l;
      }
    }
    localFile1 = new File("/data/system/appscaling/app_notch_config.xml");
    if (localFile1.exists())
    {
      localFile2 = localFile1;
      if (localFile1.canRead()) {}
    }
    else
    {
      localFile2 = new File("/system/etc/appscaling/app_notch_config.xml");
    }
    if ((localFile2.exists()) && (localFile2.canRead()))
    {
      l = localFile2.lastModified();
      if (mNotchConfigLastModified != l)
      {
        parseNotchConfig(localFile2);
        mNotchConfigLastModified = l;
      }
    }
  }
  
  public void systemReady()
  {
    mSystemReady = true;
    refreshSettings();
  }
  
  private static class NotchConfig
  {
    boolean canfillNotchInLand = true;
    boolean fillNotch = false;
    boolean locked = false;
    float minAspect = 0.0F;
    boolean mustRestart = false;
    
    private NotchConfig() {}
  }
}
