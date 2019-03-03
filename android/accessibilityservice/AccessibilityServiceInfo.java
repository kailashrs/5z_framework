package android.accessibilityservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import android.view.accessibility.AccessibilityEvent;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessibilityServiceInfo
  implements Parcelable
{
  public static final int CAPABILITY_CAN_CONTROL_MAGNIFICATION = 16;
  public static final int CAPABILITY_CAN_PERFORM_GESTURES = 32;
  public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
  public static final int CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS = 8;
  public static final int CAPABILITY_CAN_REQUEST_FINGERPRINT_GESTURES = 64;
  public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
  public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
  public static final Parcelable.Creator<AccessibilityServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public AccessibilityServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      AccessibilityServiceInfo localAccessibilityServiceInfo = new AccessibilityServiceInfo();
      localAccessibilityServiceInfo.initFromParcel(paramAnonymousParcel);
      return localAccessibilityServiceInfo;
    }
    
    public AccessibilityServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new AccessibilityServiceInfo[paramAnonymousInt];
    }
  };
  public static final int DEFAULT = 1;
  public static final int FEEDBACK_ALL_MASK = -1;
  public static final int FEEDBACK_AUDIBLE = 4;
  public static final int FEEDBACK_BRAILLE = 32;
  public static final int FEEDBACK_GENERIC = 16;
  public static final int FEEDBACK_HAPTIC = 2;
  public static final int FEEDBACK_SPOKEN = 1;
  public static final int FEEDBACK_VISUAL = 8;
  public static final int FLAG_ENABLE_ACCESSIBILITY_VOLUME = 128;
  public static final int FLAG_FORCE_DIRECT_BOOT_AWARE = 65536;
  public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
  public static final int FLAG_REPORT_VIEW_IDS = 16;
  public static final int FLAG_REQUEST_ACCESSIBILITY_BUTTON = 256;
  public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
  public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
  public static final int FLAG_REQUEST_FINGERPRINT_GESTURES = 512;
  public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
  public static final int FLAG_RETRIEVE_INTERACTIVE_WINDOWS = 64;
  private static final String TAG_ACCESSIBILITY_SERVICE = "accessibility-service";
  private static SparseArray<CapabilityInfo> sAvailableCapabilityInfos;
  public boolean crashed;
  public int eventTypes;
  public int feedbackType;
  public int flags;
  private int mCapabilities;
  private ComponentName mComponentName;
  private int mDescriptionResId;
  private String mNonLocalizedDescription;
  private String mNonLocalizedSummary;
  private ResolveInfo mResolveInfo;
  private String mSettingsActivityName;
  private int mSummaryResId;
  public long notificationTimeout;
  public String[] packageNames;
  
  public AccessibilityServiceInfo() {}
  
  /* Error */
  public AccessibilityServiceInfo(ResolveInfo paramResolveInfo, Context paramContext)
    throws org.xmlpull.v1.XmlPullParserException, java.io.IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 90	java/lang/Object:<init>	()V
    //   4: aload_1
    //   5: getfield 103	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   8: astore_3
    //   9: aload_0
    //   10: new 105	android/content/ComponentName
    //   13: dup
    //   14: aload_3
    //   15: getfield 110	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   18: aload_3
    //   19: getfield 113	android/content/pm/ServiceInfo:name	Ljava/lang/String;
    //   22: invokespecial 116	android/content/ComponentName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   25: putfield 118	android/accessibilityservice/AccessibilityServiceInfo:mComponentName	Landroid/content/ComponentName;
    //   28: aload_0
    //   29: aload_1
    //   30: putfield 120	android/accessibilityservice/AccessibilityServiceInfo:mResolveInfo	Landroid/content/pm/ResolveInfo;
    //   33: aconst_null
    //   34: astore 4
    //   36: aconst_null
    //   37: astore 5
    //   39: aload 5
    //   41: astore_1
    //   42: aload 4
    //   44: astore 6
    //   46: aload_2
    //   47: invokevirtual 126	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   50: astore 7
    //   52: aload 5
    //   54: astore_1
    //   55: aload 4
    //   57: astore 6
    //   59: aload_3
    //   60: aload 7
    //   62: ldc -128
    //   64: invokevirtual 132	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   67: astore_2
    //   68: aload_2
    //   69: ifnonnull +14 -> 83
    //   72: aload_2
    //   73: ifnull +9 -> 82
    //   76: aload_2
    //   77: invokeinterface 137 1 0
    //   82: return
    //   83: iconst_0
    //   84: istore 8
    //   86: iload 8
    //   88: iconst_1
    //   89: if_icmpeq +25 -> 114
    //   92: iload 8
    //   94: iconst_2
    //   95: if_icmpeq +19 -> 114
    //   98: aload_2
    //   99: astore_1
    //   100: aload_2
    //   101: astore 6
    //   103: aload_2
    //   104: invokeinterface 141 1 0
    //   109: istore 8
    //   111: goto -25 -> 86
    //   114: aload_2
    //   115: astore_1
    //   116: aload_2
    //   117: astore 6
    //   119: ldc 59
    //   121: aload_2
    //   122: invokeinterface 145 1 0
    //   127: invokevirtual 151	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   130: ifeq +501 -> 631
    //   133: aload_2
    //   134: astore_1
    //   135: aload_2
    //   136: astore 6
    //   138: aload_2
    //   139: invokestatic 157	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   142: astore 5
    //   144: aload_2
    //   145: astore_1
    //   146: aload_2
    //   147: astore 6
    //   149: aload 7
    //   151: aload_3
    //   152: getfield 161	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   155: invokevirtual 167	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   158: aload 5
    //   160: getstatic 173	com/android/internal/R$styleable:AccessibilityService	[I
    //   163: invokevirtual 179	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   166: astore 5
    //   168: aload_2
    //   169: astore_1
    //   170: aload_2
    //   171: astore 6
    //   173: aload_0
    //   174: aload 5
    //   176: iconst_3
    //   177: iconst_0
    //   178: invokevirtual 185	android/content/res/TypedArray:getInt	(II)I
    //   181: putfield 187	android/accessibilityservice/AccessibilityServiceInfo:eventTypes	I
    //   184: aload_2
    //   185: astore_1
    //   186: aload_2
    //   187: astore 6
    //   189: aload 5
    //   191: iconst_4
    //   192: invokevirtual 191	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   195: astore 4
    //   197: aload 4
    //   199: ifnull +19 -> 218
    //   202: aload_2
    //   203: astore_1
    //   204: aload_2
    //   205: astore 6
    //   207: aload_0
    //   208: aload 4
    //   210: ldc -63
    //   212: invokevirtual 197	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   215: putfield 199	android/accessibilityservice/AccessibilityServiceInfo:packageNames	[Ljava/lang/String;
    //   218: aload_2
    //   219: astore_1
    //   220: aload_2
    //   221: astore 6
    //   223: aload_0
    //   224: aload 5
    //   226: iconst_5
    //   227: iconst_0
    //   228: invokevirtual 185	android/content/res/TypedArray:getInt	(II)I
    //   231: putfield 201	android/accessibilityservice/AccessibilityServiceInfo:feedbackType	I
    //   234: aload_2
    //   235: astore_1
    //   236: aload_2
    //   237: astore 6
    //   239: aload_0
    //   240: aload 5
    //   242: bipush 6
    //   244: iconst_0
    //   245: invokevirtual 185	android/content/res/TypedArray:getInt	(II)I
    //   248: i2l
    //   249: putfield 203	android/accessibilityservice/AccessibilityServiceInfo:notificationTimeout	J
    //   252: aload_2
    //   253: astore_1
    //   254: aload_2
    //   255: astore 6
    //   257: aload_0
    //   258: aload 5
    //   260: bipush 7
    //   262: iconst_0
    //   263: invokevirtual 185	android/content/res/TypedArray:getInt	(II)I
    //   266: putfield 205	android/accessibilityservice/AccessibilityServiceInfo:flags	I
    //   269: aload_2
    //   270: astore_1
    //   271: aload_2
    //   272: astore 6
    //   274: aload_0
    //   275: aload 5
    //   277: iconst_2
    //   278: invokevirtual 191	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   281: putfield 207	android/accessibilityservice/AccessibilityServiceInfo:mSettingsActivityName	Ljava/lang/String;
    //   284: aload_2
    //   285: astore_1
    //   286: aload_2
    //   287: astore 6
    //   289: aload 5
    //   291: bipush 8
    //   293: iconst_0
    //   294: invokevirtual 211	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   297: ifeq +18 -> 315
    //   300: aload_2
    //   301: astore_1
    //   302: aload_2
    //   303: astore 6
    //   305: aload_0
    //   306: aload_0
    //   307: getfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   310: iconst_1
    //   311: ior
    //   312: putfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   315: aload_2
    //   316: astore_1
    //   317: aload_2
    //   318: astore 6
    //   320: aload 5
    //   322: bipush 9
    //   324: iconst_0
    //   325: invokevirtual 211	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   328: ifeq +18 -> 346
    //   331: aload_2
    //   332: astore_1
    //   333: aload_2
    //   334: astore 6
    //   336: aload_0
    //   337: iconst_2
    //   338: aload_0
    //   339: getfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   342: ior
    //   343: putfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   346: aload_2
    //   347: astore_1
    //   348: aload_2
    //   349: astore 6
    //   351: aload 5
    //   353: bipush 11
    //   355: iconst_0
    //   356: invokevirtual 211	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   359: ifeq +19 -> 378
    //   362: aload_2
    //   363: astore_1
    //   364: aload_2
    //   365: astore 6
    //   367: aload_0
    //   368: aload_0
    //   369: getfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   372: bipush 8
    //   374: ior
    //   375: putfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   378: aload_2
    //   379: astore_1
    //   380: aload_2
    //   381: astore 6
    //   383: aload 5
    //   385: bipush 12
    //   387: iconst_0
    //   388: invokevirtual 211	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   391: ifeq +19 -> 410
    //   394: aload_2
    //   395: astore_1
    //   396: aload_2
    //   397: astore 6
    //   399: aload_0
    //   400: aload_0
    //   401: getfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   404: bipush 16
    //   406: ior
    //   407: putfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   410: aload_2
    //   411: astore_1
    //   412: aload_2
    //   413: astore 6
    //   415: aload 5
    //   417: bipush 13
    //   419: iconst_0
    //   420: invokevirtual 211	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   423: ifeq +19 -> 442
    //   426: aload_2
    //   427: astore_1
    //   428: aload_2
    //   429: astore 6
    //   431: aload_0
    //   432: aload_0
    //   433: getfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   436: bipush 32
    //   438: ior
    //   439: putfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   442: aload_2
    //   443: astore_1
    //   444: aload_2
    //   445: astore 6
    //   447: aload 5
    //   449: bipush 14
    //   451: iconst_0
    //   452: invokevirtual 211	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   455: ifeq +19 -> 474
    //   458: aload_2
    //   459: astore_1
    //   460: aload_2
    //   461: astore 6
    //   463: aload_0
    //   464: aload_0
    //   465: getfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   468: bipush 64
    //   470: ior
    //   471: putfield 213	android/accessibilityservice/AccessibilityServiceInfo:mCapabilities	I
    //   474: aload_2
    //   475: astore_1
    //   476: aload_2
    //   477: astore 6
    //   479: aload 5
    //   481: iconst_0
    //   482: invokevirtual 217	android/content/res/TypedArray:peekValue	(I)Landroid/util/TypedValue;
    //   485: astore 4
    //   487: aload 4
    //   489: ifnull +53 -> 542
    //   492: aload_2
    //   493: astore_1
    //   494: aload_2
    //   495: astore 6
    //   497: aload_0
    //   498: aload 4
    //   500: getfield 222	android/util/TypedValue:resourceId	I
    //   503: putfield 224	android/accessibilityservice/AccessibilityServiceInfo:mDescriptionResId	I
    //   506: aload_2
    //   507: astore_1
    //   508: aload_2
    //   509: astore 6
    //   511: aload 4
    //   513: invokevirtual 228	android/util/TypedValue:coerceToString	()Ljava/lang/CharSequence;
    //   516: astore 4
    //   518: aload 4
    //   520: ifnull +22 -> 542
    //   523: aload_2
    //   524: astore_1
    //   525: aload_2
    //   526: astore 6
    //   528: aload_0
    //   529: aload 4
    //   531: invokeinterface 233 1 0
    //   536: invokevirtual 236	java/lang/String:trim	()Ljava/lang/String;
    //   539: putfield 238	android/accessibilityservice/AccessibilityServiceInfo:mNonLocalizedDescription	Ljava/lang/String;
    //   542: aload_2
    //   543: astore_1
    //   544: aload_2
    //   545: astore 6
    //   547: aload 5
    //   549: iconst_1
    //   550: invokevirtual 217	android/content/res/TypedArray:peekValue	(I)Landroid/util/TypedValue;
    //   553: astore 4
    //   555: aload 4
    //   557: ifnull +53 -> 610
    //   560: aload_2
    //   561: astore_1
    //   562: aload_2
    //   563: astore 6
    //   565: aload_0
    //   566: aload 4
    //   568: getfield 222	android/util/TypedValue:resourceId	I
    //   571: putfield 240	android/accessibilityservice/AccessibilityServiceInfo:mSummaryResId	I
    //   574: aload_2
    //   575: astore_1
    //   576: aload_2
    //   577: astore 6
    //   579: aload 4
    //   581: invokevirtual 228	android/util/TypedValue:coerceToString	()Ljava/lang/CharSequence;
    //   584: astore 4
    //   586: aload 4
    //   588: ifnull +22 -> 610
    //   591: aload_2
    //   592: astore_1
    //   593: aload_2
    //   594: astore 6
    //   596: aload_0
    //   597: aload 4
    //   599: invokeinterface 233 1 0
    //   604: invokevirtual 236	java/lang/String:trim	()Ljava/lang/String;
    //   607: putfield 242	android/accessibilityservice/AccessibilityServiceInfo:mNonLocalizedSummary	Ljava/lang/String;
    //   610: aload_2
    //   611: astore_1
    //   612: aload_2
    //   613: astore 6
    //   615: aload 5
    //   617: invokevirtual 245	android/content/res/TypedArray:recycle	()V
    //   620: aload_2
    //   621: ifnull +9 -> 630
    //   624: aload_2
    //   625: invokeinterface 137 1 0
    //   630: return
    //   631: aload_2
    //   632: astore_1
    //   633: aload_2
    //   634: astore 6
    //   636: new 93	org/xmlpull/v1/XmlPullParserException
    //   639: astore 5
    //   641: aload_2
    //   642: astore_1
    //   643: aload_2
    //   644: astore 6
    //   646: aload 5
    //   648: ldc -9
    //   650: invokespecial 250	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   653: aload_2
    //   654: astore_1
    //   655: aload_2
    //   656: astore 6
    //   658: aload 5
    //   660: athrow
    //   661: astore_2
    //   662: goto +68 -> 730
    //   665: astore_1
    //   666: aload 6
    //   668: astore_1
    //   669: new 93	org/xmlpull/v1/XmlPullParserException
    //   672: astore_2
    //   673: aload 6
    //   675: astore_1
    //   676: new 252	java/lang/StringBuilder
    //   679: astore 5
    //   681: aload 6
    //   683: astore_1
    //   684: aload 5
    //   686: invokespecial 253	java/lang/StringBuilder:<init>	()V
    //   689: aload 6
    //   691: astore_1
    //   692: aload 5
    //   694: ldc -1
    //   696: invokevirtual 259	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   699: pop
    //   700: aload 6
    //   702: astore_1
    //   703: aload 5
    //   705: aload_3
    //   706: getfield 110	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   709: invokevirtual 259	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   712: pop
    //   713: aload 6
    //   715: astore_1
    //   716: aload_2
    //   717: aload 5
    //   719: invokevirtual 260	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   722: invokespecial 250	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   725: aload 6
    //   727: astore_1
    //   728: aload_2
    //   729: athrow
    //   730: aload_1
    //   731: ifnull +9 -> 740
    //   734: aload_1
    //   735: invokeinterface 137 1 0
    //   740: aload_2
    //   741: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	742	0	this	AccessibilityServiceInfo
    //   0	742	1	paramResolveInfo	ResolveInfo
    //   0	742	2	paramContext	Context
    //   8	698	3	localServiceInfo	ServiceInfo
    //   34	564	4	localObject1	Object
    //   37	681	5	localObject2	Object
    //   44	682	6	localObject3	Object
    //   50	100	7	localPackageManager	PackageManager
    //   84	26	8	i	int
    // Exception table:
    //   from	to	target	type
    //   46	52	661	finally
    //   59	68	661	finally
    //   103	111	661	finally
    //   119	133	661	finally
    //   138	144	661	finally
    //   149	168	661	finally
    //   173	184	661	finally
    //   189	197	661	finally
    //   207	218	661	finally
    //   223	234	661	finally
    //   239	252	661	finally
    //   257	269	661	finally
    //   274	284	661	finally
    //   289	300	661	finally
    //   305	315	661	finally
    //   320	331	661	finally
    //   336	346	661	finally
    //   351	362	661	finally
    //   367	378	661	finally
    //   383	394	661	finally
    //   399	410	661	finally
    //   415	426	661	finally
    //   431	442	661	finally
    //   447	458	661	finally
    //   463	474	661	finally
    //   479	487	661	finally
    //   497	506	661	finally
    //   511	518	661	finally
    //   528	542	661	finally
    //   547	555	661	finally
    //   565	574	661	finally
    //   579	586	661	finally
    //   596	610	661	finally
    //   615	620	661	finally
    //   636	641	661	finally
    //   646	653	661	finally
    //   658	661	661	finally
    //   669	673	661	finally
    //   676	681	661	finally
    //   684	689	661	finally
    //   692	700	661	finally
    //   703	713	661	finally
    //   716	725	661	finally
    //   728	730	661	finally
    //   46	52	665	android/content/pm/PackageManager$NameNotFoundException
    //   59	68	665	android/content/pm/PackageManager$NameNotFoundException
    //   103	111	665	android/content/pm/PackageManager$NameNotFoundException
    //   119	133	665	android/content/pm/PackageManager$NameNotFoundException
    //   138	144	665	android/content/pm/PackageManager$NameNotFoundException
    //   149	168	665	android/content/pm/PackageManager$NameNotFoundException
    //   173	184	665	android/content/pm/PackageManager$NameNotFoundException
    //   189	197	665	android/content/pm/PackageManager$NameNotFoundException
    //   207	218	665	android/content/pm/PackageManager$NameNotFoundException
    //   223	234	665	android/content/pm/PackageManager$NameNotFoundException
    //   239	252	665	android/content/pm/PackageManager$NameNotFoundException
    //   257	269	665	android/content/pm/PackageManager$NameNotFoundException
    //   274	284	665	android/content/pm/PackageManager$NameNotFoundException
    //   289	300	665	android/content/pm/PackageManager$NameNotFoundException
    //   305	315	665	android/content/pm/PackageManager$NameNotFoundException
    //   320	331	665	android/content/pm/PackageManager$NameNotFoundException
    //   336	346	665	android/content/pm/PackageManager$NameNotFoundException
    //   351	362	665	android/content/pm/PackageManager$NameNotFoundException
    //   367	378	665	android/content/pm/PackageManager$NameNotFoundException
    //   383	394	665	android/content/pm/PackageManager$NameNotFoundException
    //   399	410	665	android/content/pm/PackageManager$NameNotFoundException
    //   415	426	665	android/content/pm/PackageManager$NameNotFoundException
    //   431	442	665	android/content/pm/PackageManager$NameNotFoundException
    //   447	458	665	android/content/pm/PackageManager$NameNotFoundException
    //   463	474	665	android/content/pm/PackageManager$NameNotFoundException
    //   479	487	665	android/content/pm/PackageManager$NameNotFoundException
    //   497	506	665	android/content/pm/PackageManager$NameNotFoundException
    //   511	518	665	android/content/pm/PackageManager$NameNotFoundException
    //   528	542	665	android/content/pm/PackageManager$NameNotFoundException
    //   547	555	665	android/content/pm/PackageManager$NameNotFoundException
    //   565	574	665	android/content/pm/PackageManager$NameNotFoundException
    //   579	586	665	android/content/pm/PackageManager$NameNotFoundException
    //   596	610	665	android/content/pm/PackageManager$NameNotFoundException
    //   615	620	665	android/content/pm/PackageManager$NameNotFoundException
    //   636	641	665	android/content/pm/PackageManager$NameNotFoundException
    //   646	653	665	android/content/pm/PackageManager$NameNotFoundException
    //   658	661	665	android/content/pm/PackageManager$NameNotFoundException
  }
  
  private static void appendCapabilities(StringBuilder paramStringBuilder, int paramInt)
  {
    paramStringBuilder.append("capabilities:");
    paramStringBuilder.append("[");
    while (paramInt != 0)
    {
      int i = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramStringBuilder.append(capabilityToString(i));
      paramInt &= i;
      if (paramInt != 0) {
        paramStringBuilder.append(", ");
      }
    }
    paramStringBuilder.append("]");
  }
  
  private static void appendEventTypes(StringBuilder paramStringBuilder, int paramInt)
  {
    paramStringBuilder.append("eventTypes:");
    paramStringBuilder.append("[");
    while (paramInt != 0)
    {
      int i = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramStringBuilder.append(AccessibilityEvent.eventTypeToString(i));
      paramInt &= i;
      if (paramInt != 0) {
        paramStringBuilder.append(", ");
      }
    }
    paramStringBuilder.append("]");
  }
  
  private static void appendFeedbackTypes(StringBuilder paramStringBuilder, int paramInt)
  {
    paramStringBuilder.append("feedbackTypes:");
    paramStringBuilder.append("[");
    while (paramInt != 0)
    {
      int i = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramStringBuilder.append(feedbackTypeToString(i));
      paramInt &= i;
      if (paramInt != 0) {
        paramStringBuilder.append(", ");
      }
    }
    paramStringBuilder.append("]");
  }
  
  private static void appendFlags(StringBuilder paramStringBuilder, int paramInt)
  {
    paramStringBuilder.append("flags:");
    paramStringBuilder.append("[");
    while (paramInt != 0)
    {
      int i = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramStringBuilder.append(flagToString(i));
      paramInt &= i;
      if (paramInt != 0) {
        paramStringBuilder.append(", ");
      }
    }
    paramStringBuilder.append("]");
  }
  
  private static void appendPackageNames(StringBuilder paramStringBuilder, String[] paramArrayOfString)
  {
    paramStringBuilder.append("packageNames:");
    paramStringBuilder.append("[");
    if (paramArrayOfString != null)
    {
      int i = paramArrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        paramStringBuilder.append(paramArrayOfString[j]);
        if (j < i - 1) {
          paramStringBuilder.append(", ");
        }
      }
    }
    paramStringBuilder.append("]");
  }
  
  public static String capabilityToString(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 8)
      {
        if (paramInt != 16)
        {
          if (paramInt != 32)
          {
            if (paramInt != 64)
            {
              switch (paramInt)
              {
              default: 
                return "UNKNOWN";
              case 2: 
                return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
              }
              return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
            }
            return "CAPABILITY_CAN_REQUEST_FINGERPRINT_GESTURES";
          }
          return "CAPABILITY_CAN_PERFORM_GESTURES";
        }
        return "CAPABILITY_CAN_CONTROL_MAGNIFICATION";
      }
      return "CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS";
    }
    return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
  }
  
  public static String feedbackTypeToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    while (paramInt != 0)
    {
      int i = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramInt &= i;
      if (i != 4)
      {
        if (i != 8)
        {
          if (i != 16)
          {
            if (i != 32)
            {
              switch (i)
              {
              default: 
                break;
              case 2: 
                if (localStringBuilder.length() > 1) {
                  localStringBuilder.append(", ");
                }
                localStringBuilder.append("FEEDBACK_HAPTIC");
                break;
              case 1: 
                if (localStringBuilder.length() > 1) {
                  localStringBuilder.append(", ");
                }
                localStringBuilder.append("FEEDBACK_SPOKEN");
                break;
              }
            }
            else
            {
              if (localStringBuilder.length() > 1) {
                localStringBuilder.append(", ");
              }
              localStringBuilder.append("FEEDBACK_BRAILLE");
            }
          }
          else
          {
            if (localStringBuilder.length() > 1) {
              localStringBuilder.append(", ");
            }
            localStringBuilder.append("FEEDBACK_GENERIC");
          }
        }
        else
        {
          if (localStringBuilder.length() > 1) {
            localStringBuilder.append(", ");
          }
          localStringBuilder.append("FEEDBACK_VISUAL");
        }
      }
      else
      {
        if (localStringBuilder.length() > 1) {
          localStringBuilder.append(", ");
        }
        localStringBuilder.append("FEEDBACK_AUDIBLE");
      }
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  private static boolean fingerprintAvailable(Context paramContext)
  {
    boolean bool;
    if ((paramContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) && (((FingerprintManager)paramContext.getSystemService(FingerprintManager.class)).isHardwareDetected())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static String flagToString(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 8)
      {
        if (paramInt != 16)
        {
          if (paramInt != 32)
          {
            if (paramInt != 64)
            {
              if (paramInt != 128)
              {
                if (paramInt != 256)
                {
                  if (paramInt != 512)
                  {
                    switch (paramInt)
                    {
                    default: 
                      return null;
                    case 2: 
                      return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
                    }
                    return "DEFAULT";
                  }
                  return "FLAG_REQUEST_FINGERPRINT_GESTURES";
                }
                return "FLAG_REQUEST_ACCESSIBILITY_BUTTON";
              }
              return "FLAG_ENABLE_ACCESSIBILITY_VOLUME";
            }
            return "FLAG_RETRIEVE_INTERACTIVE_WINDOWS";
          }
          return "FLAG_REQUEST_FILTER_KEY_EVENTS";
        }
        return "FLAG_REPORT_VIEW_IDS";
      }
      return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
    }
    return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
  }
  
  private static SparseArray<CapabilityInfo> getCapabilityInfoSparseArray(Context paramContext)
  {
    if (sAvailableCapabilityInfos == null)
    {
      sAvailableCapabilityInfos = new SparseArray();
      sAvailableCapabilityInfos.put(1, new CapabilityInfo(1, 17039638, 17039632));
      sAvailableCapabilityInfos.put(2, new CapabilityInfo(2, 17039637, 17039631));
      sAvailableCapabilityInfos.put(8, new CapabilityInfo(8, 17039636, 17039630));
      sAvailableCapabilityInfos.put(16, new CapabilityInfo(16, 17039634, 17039628));
      sAvailableCapabilityInfos.put(32, new CapabilityInfo(32, 17039635, 17039629));
      if ((paramContext == null) || (fingerprintAvailable(paramContext))) {
        sAvailableCapabilityInfos.put(64, new CapabilityInfo(64, 17039633, 17039627));
      }
    }
    return sAvailableCapabilityInfos;
  }
  
  private void initFromParcel(Parcel paramParcel)
  {
    eventTypes = paramParcel.readInt();
    packageNames = paramParcel.readStringArray();
    feedbackType = paramParcel.readInt();
    notificationTimeout = paramParcel.readLong();
    flags = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    crashed = bool;
    mComponentName = ((ComponentName)paramParcel.readParcelable(getClass().getClassLoader()));
    mResolveInfo = ((ResolveInfo)paramParcel.readParcelable(null));
    mSettingsActivityName = paramParcel.readString();
    mCapabilities = paramParcel.readInt();
    mSummaryResId = paramParcel.readInt();
    mNonLocalizedSummary = paramParcel.readString();
    mDescriptionResId = paramParcel.readInt();
    mNonLocalizedDescription = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (AccessibilityServiceInfo)paramObject;
    if (mComponentName == null)
    {
      if (mComponentName != null) {
        return false;
      }
    }
    else if (!mComponentName.equals(mComponentName)) {
      return false;
    }
    return true;
  }
  
  public boolean getCanRetrieveWindowContent()
  {
    int i = mCapabilities;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public int getCapabilities()
  {
    return mCapabilities;
  }
  
  public List<CapabilityInfo> getCapabilityInfos()
  {
    return getCapabilityInfos(null);
  }
  
  public List<CapabilityInfo> getCapabilityInfos(Context paramContext)
  {
    if (mCapabilities == 0) {
      return Collections.emptyList();
    }
    int i = mCapabilities;
    ArrayList localArrayList = new ArrayList();
    SparseArray localSparseArray = getCapabilityInfoSparseArray(paramContext);
    while (i != 0)
    {
      int j = 1 << Integer.numberOfTrailingZeros(i);
      i &= j;
      paramContext = (CapabilityInfo)localSparseArray.get(j);
      if (paramContext != null) {
        localArrayList.add(paramContext);
      }
    }
    return localArrayList;
  }
  
  public ComponentName getComponentName()
  {
    return mComponentName;
  }
  
  public String getDescription()
  {
    return mNonLocalizedDescription;
  }
  
  public String getId()
  {
    return mComponentName.flattenToShortString();
  }
  
  public ResolveInfo getResolveInfo()
  {
    return mResolveInfo;
  }
  
  public String getSettingsActivityName()
  {
    return mSettingsActivityName;
  }
  
  public int hashCode()
  {
    int i;
    if (mComponentName == null) {
      i = 0;
    } else {
      i = mComponentName.hashCode();
    }
    return 31 + i;
  }
  
  public boolean isDirectBootAware()
  {
    boolean bool;
    if (((flags & 0x10000) == 0) && (!mResolveInfo.serviceInfo.directBootAware)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String loadDescription(PackageManager paramPackageManager)
  {
    if (mDescriptionResId == 0) {
      return mNonLocalizedDescription;
    }
    ServiceInfo localServiceInfo = mResolveInfo.serviceInfo;
    paramPackageManager = paramPackageManager.getText(packageName, mDescriptionResId, applicationInfo);
    if (paramPackageManager != null) {
      return paramPackageManager.toString().trim();
    }
    return null;
  }
  
  public CharSequence loadSummary(PackageManager paramPackageManager)
  {
    if (mSummaryResId == 0) {
      return mNonLocalizedSummary;
    }
    ServiceInfo localServiceInfo = mResolveInfo.serviceInfo;
    paramPackageManager = paramPackageManager.getText(packageName, mSummaryResId, applicationInfo);
    if (paramPackageManager != null) {
      return paramPackageManager.toString().trim();
    }
    return null;
  }
  
  public void setCapabilities(int paramInt)
  {
    mCapabilities = paramInt;
  }
  
  public void setComponentName(ComponentName paramComponentName)
  {
    mComponentName = paramComponentName;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    appendEventTypes(localStringBuilder, eventTypes);
    localStringBuilder.append(", ");
    appendPackageNames(localStringBuilder, packageNames);
    localStringBuilder.append(", ");
    appendFeedbackTypes(localStringBuilder, feedbackType);
    localStringBuilder.append(", ");
    localStringBuilder.append("notificationTimeout: ");
    localStringBuilder.append(notificationTimeout);
    localStringBuilder.append(", ");
    appendFlags(localStringBuilder, flags);
    localStringBuilder.append(", ");
    localStringBuilder.append("id: ");
    localStringBuilder.append(getId());
    localStringBuilder.append(", ");
    localStringBuilder.append("resolveInfo: ");
    localStringBuilder.append(mResolveInfo);
    localStringBuilder.append(", ");
    localStringBuilder.append("settingsActivityName: ");
    localStringBuilder.append(mSettingsActivityName);
    localStringBuilder.append(", ");
    localStringBuilder.append("summary: ");
    localStringBuilder.append(mNonLocalizedSummary);
    localStringBuilder.append(", ");
    appendCapabilities(localStringBuilder, mCapabilities);
    return localStringBuilder.toString();
  }
  
  public void updateDynamicallyConfigurableProperties(AccessibilityServiceInfo paramAccessibilityServiceInfo)
  {
    eventTypes = eventTypes;
    packageNames = packageNames;
    feedbackType = feedbackType;
    notificationTimeout = notificationTimeout;
    flags = flags;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(eventTypes);
    paramParcel.writeStringArray(packageNames);
    paramParcel.writeInt(feedbackType);
    paramParcel.writeLong(notificationTimeout);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(crashed);
    paramParcel.writeParcelable(mComponentName, paramInt);
    paramParcel.writeParcelable(mResolveInfo, 0);
    paramParcel.writeString(mSettingsActivityName);
    paramParcel.writeInt(mCapabilities);
    paramParcel.writeInt(mSummaryResId);
    paramParcel.writeString(mNonLocalizedSummary);
    paramParcel.writeInt(mDescriptionResId);
    paramParcel.writeString(mNonLocalizedDescription);
  }
  
  public static final class CapabilityInfo
  {
    public final int capability;
    public final int descResId;
    public final int titleResId;
    
    public CapabilityInfo(int paramInt1, int paramInt2, int paramInt3)
    {
      capability = paramInt1;
      titleResId = paramInt2;
      descResId = paramInt3;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FeedbackType {}
}
