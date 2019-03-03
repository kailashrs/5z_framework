package android.app.admin;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Printer;
import android.util.SparseArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class DeviceAdminInfo
  implements Parcelable
{
  public static final Parcelable.Creator<DeviceAdminInfo> CREATOR = new Parcelable.Creator()
  {
    public DeviceAdminInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DeviceAdminInfo(paramAnonymousParcel);
    }
    
    public DeviceAdminInfo[] newArray(int paramAnonymousInt)
    {
      return new DeviceAdminInfo[paramAnonymousInt];
    }
  };
  static final String TAG = "DeviceAdminInfo";
  public static final int USES_ENCRYPTED_STORAGE = 7;
  public static final int USES_POLICY_DEVICE_OWNER = -2;
  public static final int USES_POLICY_DISABLE_CAMERA = 8;
  public static final int USES_POLICY_DISABLE_KEYGUARD_FEATURES = 9;
  public static final int USES_POLICY_EXPIRE_PASSWORD = 6;
  public static final int USES_POLICY_FORCE_LOCK = 3;
  public static final int USES_POLICY_LIMIT_PASSWORD = 0;
  public static final int USES_POLICY_PROFILE_OWNER = -1;
  public static final int USES_POLICY_RESET_PASSWORD = 2;
  public static final int USES_POLICY_SETS_GLOBAL_PROXY = 5;
  public static final int USES_POLICY_WATCH_LOGIN = 1;
  public static final int USES_POLICY_WIPE_DATA = 4;
  static HashMap<String, Integer> sKnownPolicies;
  static ArrayList<PolicyInfo> sPoliciesDisplayOrder = new ArrayList();
  static SparseArray<PolicyInfo> sRevKnownPolicies;
  final ActivityInfo mActivityInfo;
  boolean mSupportsTransferOwnership;
  int mUsesPolicies;
  boolean mVisible;
  
  static
  {
    sKnownPolicies = new HashMap();
    sRevKnownPolicies = new SparseArray();
    sPoliciesDisplayOrder.add(new PolicyInfo(4, "wipe-data", 17040846, 17040834, 17040847, 17040835));
    sPoliciesDisplayOrder.add(new PolicyInfo(2, "reset-password", 17040843, 17040830));
    Object localObject = sPoliciesDisplayOrder;
    int i = 0;
    ((ArrayList)localObject).add(new PolicyInfo(0, "limit-password", 17040842, 17040829));
    sPoliciesDisplayOrder.add(new PolicyInfo(1, "watch-login", 17040845, 17040832, 17040845, 17040833));
    sPoliciesDisplayOrder.add(new PolicyInfo(3, "force-lock", 17040841, 17040828));
    sPoliciesDisplayOrder.add(new PolicyInfo(5, "set-global-proxy", 17040844, 17040831));
    sPoliciesDisplayOrder.add(new PolicyInfo(6, "expire-password", 17040840, 17040827));
    sPoliciesDisplayOrder.add(new PolicyInfo(7, "encrypted-storage", 17040839, 17040826));
    sPoliciesDisplayOrder.add(new PolicyInfo(8, "disable-camera", 17040836, 17040823));
    sPoliciesDisplayOrder.add(new PolicyInfo(9, "disable-keyguard-features", 17040837, 17040824));
    while (i < sPoliciesDisplayOrder.size())
    {
      localObject = (PolicyInfo)sPoliciesDisplayOrder.get(i);
      sRevKnownPolicies.put(ident, localObject);
      sKnownPolicies.put(tag, Integer.valueOf(ident));
      i++;
    }
  }
  
  /* Error */
  public DeviceAdminInfo(Context paramContext, ActivityInfo paramActivityInfo)
    throws XmlPullParserException, IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 169	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: aload_2
    //   6: putfield 171	android/app/admin/DeviceAdminInfo:mActivityInfo	Landroid/content/pm/ActivityInfo;
    //   9: aload_1
    //   10: invokevirtual 177	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   13: astore_1
    //   14: aconst_null
    //   15: astore_3
    //   16: aconst_null
    //   17: astore_2
    //   18: aload_0
    //   19: getfield 171	android/app/admin/DeviceAdminInfo:mActivityInfo	Landroid/content/pm/ActivityInfo;
    //   22: aload_1
    //   23: ldc -77
    //   25: invokevirtual 185	android/content/pm/ActivityInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   28: astore 4
    //   30: aload 4
    //   32: ifnull +639 -> 671
    //   35: aload 4
    //   37: astore_2
    //   38: aload 4
    //   40: astore_3
    //   41: aload_1
    //   42: aload_0
    //   43: getfield 171	android/app/admin/DeviceAdminInfo:mActivityInfo	Landroid/content/pm/ActivityInfo;
    //   46: getfield 189	android/content/pm/ActivityInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   49: invokevirtual 195	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   52: astore_1
    //   53: aload 4
    //   55: astore_2
    //   56: aload 4
    //   58: astore_3
    //   59: aload 4
    //   61: invokestatic 201	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   64: astore 5
    //   66: aload 4
    //   68: astore_2
    //   69: aload 4
    //   71: astore_3
    //   72: aload 4
    //   74: invokeinterface 206 1 0
    //   79: istore 6
    //   81: iconst_1
    //   82: istore 7
    //   84: iload 6
    //   86: iconst_1
    //   87: if_icmpeq +12 -> 99
    //   90: iload 6
    //   92: iconst_2
    //   93: if_icmpeq +6 -> 99
    //   96: goto -30 -> 66
    //   99: aload 4
    //   101: astore_2
    //   102: aload 4
    //   104: astore_3
    //   105: ldc -48
    //   107: aload 4
    //   109: invokeinterface 212 1 0
    //   114: invokevirtual 217	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   117: ifeq +523 -> 640
    //   120: aload 4
    //   122: astore_2
    //   123: aload 4
    //   125: astore_3
    //   126: aload_1
    //   127: aload 5
    //   129: getstatic 223	com/android/internal/R$styleable:DeviceAdmin	[I
    //   132: invokevirtual 229	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   135: astore 5
    //   137: aload 4
    //   139: astore_2
    //   140: aload 4
    //   142: astore_3
    //   143: aload_0
    //   144: aload 5
    //   146: iconst_0
    //   147: iconst_1
    //   148: invokevirtual 235	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   151: putfield 237	android/app/admin/DeviceAdminInfo:mVisible	Z
    //   154: aload 4
    //   156: astore_2
    //   157: aload 4
    //   159: astore_3
    //   160: aload 5
    //   162: invokevirtual 240	android/content/res/TypedArray:recycle	()V
    //   165: aload 4
    //   167: astore_2
    //   168: aload 4
    //   170: astore_3
    //   171: aload 4
    //   173: invokeinterface 243 1 0
    //   178: istore 6
    //   180: aload 4
    //   182: astore_2
    //   183: aload 4
    //   185: astore_3
    //   186: aload 4
    //   188: invokeinterface 206 1 0
    //   193: istore 8
    //   195: iload 8
    //   197: iload 7
    //   199: if_icmpeq +428 -> 627
    //   202: iload 8
    //   204: iconst_3
    //   205: if_icmpne +21 -> 226
    //   208: aload 4
    //   210: astore_2
    //   211: aload 4
    //   213: astore_3
    //   214: aload 4
    //   216: invokeinterface 243 1 0
    //   221: iload 6
    //   223: if_icmple +404 -> 627
    //   226: iload 8
    //   228: iconst_3
    //   229: if_icmpeq +395 -> 624
    //   232: iload 8
    //   234: iconst_4
    //   235: if_icmpne +6 -> 241
    //   238: goto +386 -> 624
    //   241: aload 4
    //   243: astore_2
    //   244: aload 4
    //   246: astore_3
    //   247: aload 4
    //   249: invokeinterface 212 1 0
    //   254: astore 9
    //   256: aload 4
    //   258: astore_2
    //   259: aload 4
    //   261: astore_3
    //   262: aload 9
    //   264: ldc -11
    //   266: invokevirtual 217	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   269: ifeq +261 -> 530
    //   272: aload 4
    //   274: astore_2
    //   275: aload 4
    //   277: astore_3
    //   278: aload 4
    //   280: invokeinterface 243 1 0
    //   285: istore 8
    //   287: aload 4
    //   289: astore_2
    //   290: aload 4
    //   292: astore_3
    //   293: aload 4
    //   295: invokeinterface 206 1 0
    //   300: istore 10
    //   302: iload 10
    //   304: iload 7
    //   306: if_icmpeq +221 -> 527
    //   309: iload 10
    //   311: iconst_3
    //   312: if_icmpne +27 -> 339
    //   315: aload 4
    //   317: astore_2
    //   318: aload 4
    //   320: astore_3
    //   321: aload 4
    //   323: invokeinterface 243 1 0
    //   328: iload 8
    //   330: if_icmple +6 -> 336
    //   333: goto +6 -> 339
    //   336: goto +191 -> 527
    //   339: iload 10
    //   341: iconst_3
    //   342: if_icmpeq +179 -> 521
    //   345: iload 10
    //   347: iconst_4
    //   348: if_icmpne +6 -> 354
    //   351: goto +170 -> 521
    //   354: aload 4
    //   356: astore_2
    //   357: aload 4
    //   359: astore_3
    //   360: aload 4
    //   362: invokeinterface 212 1 0
    //   367: astore 5
    //   369: aload 4
    //   371: astore_2
    //   372: aload 4
    //   374: astore_3
    //   375: getstatic 72	android/app/admin/DeviceAdminInfo:sKnownPolicies	Ljava/util/HashMap;
    //   378: aload 5
    //   380: invokevirtual 248	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   383: checkcast 150	java/lang/Integer
    //   386: astore 9
    //   388: aload 9
    //   390: ifnull +29 -> 419
    //   393: aload 4
    //   395: astore_2
    //   396: aload 4
    //   398: astore_3
    //   399: aload_0
    //   400: aload_0
    //   401: getfield 250	android/app/admin/DeviceAdminInfo:mUsesPolicies	I
    //   404: iload 7
    //   406: aload 9
    //   408: invokevirtual 253	java/lang/Integer:intValue	()I
    //   411: ishl
    //   412: ior
    //   413: putfield 250	android/app/admin/DeviceAdminInfo:mUsesPolicies	I
    //   416: goto +102 -> 518
    //   419: aload 4
    //   421: astore_2
    //   422: aload 4
    //   424: astore_3
    //   425: new 255	java/lang/StringBuilder
    //   428: astore 9
    //   430: aload 4
    //   432: astore_2
    //   433: aload 4
    //   435: astore_3
    //   436: aload 9
    //   438: invokespecial 256	java/lang/StringBuilder:<init>	()V
    //   441: aload 4
    //   443: astore_2
    //   444: aload 4
    //   446: astore_3
    //   447: aload 9
    //   449: ldc_w 258
    //   452: invokevirtual 262	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   455: pop
    //   456: aload 4
    //   458: astore_2
    //   459: aload 4
    //   461: astore_3
    //   462: aload 9
    //   464: aload_0
    //   465: invokevirtual 266	android/app/admin/DeviceAdminInfo:getComponent	()Landroid/content/ComponentName;
    //   468: invokevirtual 269	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   471: pop
    //   472: aload 4
    //   474: astore_2
    //   475: aload 4
    //   477: astore_3
    //   478: aload 9
    //   480: ldc_w 271
    //   483: invokevirtual 262	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   486: pop
    //   487: aload 4
    //   489: astore_2
    //   490: aload 4
    //   492: astore_3
    //   493: aload 9
    //   495: aload 5
    //   497: invokevirtual 262	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   500: pop
    //   501: aload 4
    //   503: astore_2
    //   504: aload 4
    //   506: astore_3
    //   507: ldc 18
    //   509: aload 9
    //   511: invokevirtual 274	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   514: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   517: pop
    //   518: goto +3 -> 521
    //   521: iconst_1
    //   522: istore 7
    //   524: goto -237 -> 287
    //   527: goto +91 -> 618
    //   530: aload_1
    //   531: astore 5
    //   533: aload 5
    //   535: astore_1
    //   536: aload 4
    //   538: astore_2
    //   539: aload 4
    //   541: astore_3
    //   542: aload 9
    //   544: ldc_w 282
    //   547: invokevirtual 217	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   550: ifeq -23 -> 527
    //   553: aload 4
    //   555: astore_2
    //   556: aload 4
    //   558: astore_3
    //   559: aload 4
    //   561: invokeinterface 206 1 0
    //   566: iconst_3
    //   567: if_icmpne +20 -> 587
    //   570: aload 4
    //   572: astore_2
    //   573: aload 4
    //   575: astore_3
    //   576: aload_0
    //   577: iconst_1
    //   578: putfield 284	android/app/admin/DeviceAdminInfo:mSupportsTransferOwnership	Z
    //   581: aload 5
    //   583: astore_1
    //   584: goto +34 -> 618
    //   587: aload 4
    //   589: astore_2
    //   590: aload 4
    //   592: astore_3
    //   593: new 164	org/xmlpull/v1/XmlPullParserException
    //   596: astore_1
    //   597: aload 4
    //   599: astore_2
    //   600: aload 4
    //   602: astore_3
    //   603: aload_1
    //   604: ldc_w 286
    //   607: invokespecial 289	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   610: aload 4
    //   612: astore_2
    //   613: aload 4
    //   615: astore_3
    //   616: aload_1
    //   617: athrow
    //   618: iconst_1
    //   619: istore 7
    //   621: goto +3 -> 624
    //   624: goto -444 -> 180
    //   627: aload 4
    //   629: ifnull +10 -> 639
    //   632: aload 4
    //   634: invokeinterface 292 1 0
    //   639: return
    //   640: aload 4
    //   642: astore_2
    //   643: aload 4
    //   645: astore_3
    //   646: new 164	org/xmlpull/v1/XmlPullParserException
    //   649: astore_1
    //   650: aload 4
    //   652: astore_2
    //   653: aload 4
    //   655: astore_3
    //   656: aload_1
    //   657: ldc_w 294
    //   660: invokespecial 289	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   663: aload 4
    //   665: astore_2
    //   666: aload 4
    //   668: astore_3
    //   669: aload_1
    //   670: athrow
    //   671: aload 4
    //   673: astore_2
    //   674: aload 4
    //   676: astore_3
    //   677: new 164	org/xmlpull/v1/XmlPullParserException
    //   680: astore_1
    //   681: aload 4
    //   683: astore_2
    //   684: aload 4
    //   686: astore_3
    //   687: aload_1
    //   688: ldc_w 296
    //   691: invokespecial 289	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   694: aload 4
    //   696: astore_2
    //   697: aload 4
    //   699: astore_3
    //   700: aload_1
    //   701: athrow
    //   702: astore_1
    //   703: goto +65 -> 768
    //   706: astore_1
    //   707: aload_3
    //   708: astore_2
    //   709: new 164	org/xmlpull/v1/XmlPullParserException
    //   712: astore_1
    //   713: aload_3
    //   714: astore_2
    //   715: new 255	java/lang/StringBuilder
    //   718: astore 5
    //   720: aload_3
    //   721: astore_2
    //   722: aload 5
    //   724: invokespecial 256	java/lang/StringBuilder:<init>	()V
    //   727: aload_3
    //   728: astore_2
    //   729: aload 5
    //   731: ldc_w 298
    //   734: invokevirtual 262	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   737: pop
    //   738: aload_3
    //   739: astore_2
    //   740: aload 5
    //   742: aload_0
    //   743: getfield 171	android/app/admin/DeviceAdminInfo:mActivityInfo	Landroid/content/pm/ActivityInfo;
    //   746: getfield 301	android/content/pm/ActivityInfo:packageName	Ljava/lang/String;
    //   749: invokevirtual 262	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   752: pop
    //   753: aload_3
    //   754: astore_2
    //   755: aload_1
    //   756: aload 5
    //   758: invokevirtual 274	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   761: invokespecial 289	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   764: aload_3
    //   765: astore_2
    //   766: aload_1
    //   767: athrow
    //   768: aload_2
    //   769: ifnull +9 -> 778
    //   772: aload_2
    //   773: invokeinterface 292 1 0
    //   778: aload_1
    //   779: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	780	0	this	DeviceAdminInfo
    //   0	780	1	paramContext	Context
    //   0	780	2	paramActivityInfo	ActivityInfo
    //   15	750	3	localObject1	Object
    //   28	670	4	localXmlResourceParser	android.content.res.XmlResourceParser
    //   64	693	5	localObject2	Object
    //   79	145	6	i	int
    //   82	538	7	j	int
    //   193	138	8	k	int
    //   254	289	9	localObject3	Object
    //   300	49	10	m	int
    // Exception table:
    //   from	to	target	type
    //   18	30	702	finally
    //   41	53	702	finally
    //   59	66	702	finally
    //   72	81	702	finally
    //   105	120	702	finally
    //   126	137	702	finally
    //   143	154	702	finally
    //   160	165	702	finally
    //   171	180	702	finally
    //   186	195	702	finally
    //   214	226	702	finally
    //   247	256	702	finally
    //   262	272	702	finally
    //   278	287	702	finally
    //   293	302	702	finally
    //   321	333	702	finally
    //   360	369	702	finally
    //   375	388	702	finally
    //   399	416	702	finally
    //   425	430	702	finally
    //   436	441	702	finally
    //   447	456	702	finally
    //   462	472	702	finally
    //   478	487	702	finally
    //   493	501	702	finally
    //   507	518	702	finally
    //   542	553	702	finally
    //   559	570	702	finally
    //   576	581	702	finally
    //   593	597	702	finally
    //   603	610	702	finally
    //   616	618	702	finally
    //   646	650	702	finally
    //   656	663	702	finally
    //   669	671	702	finally
    //   677	681	702	finally
    //   687	694	702	finally
    //   700	702	702	finally
    //   709	713	702	finally
    //   715	720	702	finally
    //   722	727	702	finally
    //   729	738	702	finally
    //   740	753	702	finally
    //   755	764	702	finally
    //   766	768	702	finally
    //   18	30	706	android/content/pm/PackageManager$NameNotFoundException
    //   41	53	706	android/content/pm/PackageManager$NameNotFoundException
    //   59	66	706	android/content/pm/PackageManager$NameNotFoundException
    //   72	81	706	android/content/pm/PackageManager$NameNotFoundException
    //   105	120	706	android/content/pm/PackageManager$NameNotFoundException
    //   126	137	706	android/content/pm/PackageManager$NameNotFoundException
    //   143	154	706	android/content/pm/PackageManager$NameNotFoundException
    //   160	165	706	android/content/pm/PackageManager$NameNotFoundException
    //   171	180	706	android/content/pm/PackageManager$NameNotFoundException
    //   186	195	706	android/content/pm/PackageManager$NameNotFoundException
    //   214	226	706	android/content/pm/PackageManager$NameNotFoundException
    //   247	256	706	android/content/pm/PackageManager$NameNotFoundException
    //   262	272	706	android/content/pm/PackageManager$NameNotFoundException
    //   278	287	706	android/content/pm/PackageManager$NameNotFoundException
    //   293	302	706	android/content/pm/PackageManager$NameNotFoundException
    //   321	333	706	android/content/pm/PackageManager$NameNotFoundException
    //   360	369	706	android/content/pm/PackageManager$NameNotFoundException
    //   375	388	706	android/content/pm/PackageManager$NameNotFoundException
    //   399	416	706	android/content/pm/PackageManager$NameNotFoundException
    //   425	430	706	android/content/pm/PackageManager$NameNotFoundException
    //   436	441	706	android/content/pm/PackageManager$NameNotFoundException
    //   447	456	706	android/content/pm/PackageManager$NameNotFoundException
    //   462	472	706	android/content/pm/PackageManager$NameNotFoundException
    //   478	487	706	android/content/pm/PackageManager$NameNotFoundException
    //   493	501	706	android/content/pm/PackageManager$NameNotFoundException
    //   507	518	706	android/content/pm/PackageManager$NameNotFoundException
    //   542	553	706	android/content/pm/PackageManager$NameNotFoundException
    //   559	570	706	android/content/pm/PackageManager$NameNotFoundException
    //   576	581	706	android/content/pm/PackageManager$NameNotFoundException
    //   593	597	706	android/content/pm/PackageManager$NameNotFoundException
    //   603	610	706	android/content/pm/PackageManager$NameNotFoundException
    //   616	618	706	android/content/pm/PackageManager$NameNotFoundException
    //   646	650	706	android/content/pm/PackageManager$NameNotFoundException
    //   656	663	706	android/content/pm/PackageManager$NameNotFoundException
    //   669	671	706	android/content/pm/PackageManager$NameNotFoundException
    //   677	681	706	android/content/pm/PackageManager$NameNotFoundException
    //   687	694	706	android/content/pm/PackageManager$NameNotFoundException
    //   700	702	706	android/content/pm/PackageManager$NameNotFoundException
  }
  
  public DeviceAdminInfo(Context paramContext, ResolveInfo paramResolveInfo)
    throws XmlPullParserException, IOException
  {
    this(paramContext, activityInfo);
  }
  
  DeviceAdminInfo(Parcel paramParcel)
  {
    mActivityInfo = ((ActivityInfo)ActivityInfo.CREATOR.createFromParcel(paramParcel));
    mUsesPolicies = paramParcel.readInt();
    mSupportsTransferOwnership = paramParcel.readBoolean();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("Receiver:");
    paramPrinter.println(localStringBuilder.toString());
    ActivityInfo localActivityInfo = mActivityInfo;
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  ");
    localActivityInfo.dump(paramPrinter, localStringBuilder.toString());
  }
  
  public ActivityInfo getActivityInfo()
  {
    return mActivityInfo;
  }
  
  public ComponentName getComponent()
  {
    return new ComponentName(mActivityInfo.packageName, mActivityInfo.name);
  }
  
  public String getPackageName()
  {
    return mActivityInfo.packageName;
  }
  
  public String getReceiverName()
  {
    return mActivityInfo.name;
  }
  
  public String getTagForPolicy(int paramInt)
  {
    return sRevKnownPoliciesgettag;
  }
  
  public ArrayList<PolicyInfo> getUsedPolicies()
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < sPoliciesDisplayOrder.size(); i++)
    {
      PolicyInfo localPolicyInfo = (PolicyInfo)sPoliciesDisplayOrder.get(i);
      if (usesPolicy(ident)) {
        localArrayList.add(localPolicyInfo);
      }
    }
    return localArrayList;
  }
  
  public boolean isVisible()
  {
    return mVisible;
  }
  
  public CharSequence loadDescription(PackageManager paramPackageManager)
    throws Resources.NotFoundException
  {
    if (mActivityInfo.descriptionRes != 0) {
      return paramPackageManager.getText(mActivityInfo.packageName, mActivityInfo.descriptionRes, mActivityInfo.applicationInfo);
    }
    throw new Resources.NotFoundException();
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    return mActivityInfo.loadIcon(paramPackageManager);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    return mActivityInfo.loadLabel(paramPackageManager);
  }
  
  public void readPoliciesFromXml(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    mUsesPolicies = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "flags"));
  }
  
  public boolean supportsTransferOwnership()
  {
    return mSupportsTransferOwnership;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DeviceAdminInfo{");
    localStringBuilder.append(mActivityInfo.name);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public boolean usesPolicy(int paramInt)
  {
    int i = mUsesPolicies;
    boolean bool = true;
    if ((i & 1 << paramInt) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public void writePoliciesToXml(XmlSerializer paramXmlSerializer)
    throws IllegalArgumentException, IllegalStateException, IOException
  {
    paramXmlSerializer.attribute(null, "flags", Integer.toString(mUsesPolicies));
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mActivityInfo.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(mUsesPolicies);
    paramParcel.writeBoolean(mSupportsTransferOwnership);
  }
  
  public static class PolicyInfo
  {
    public final int description;
    public final int descriptionForSecondaryUsers;
    public final int ident;
    public final int label;
    public final int labelForSecondaryUsers;
    public final String tag;
    
    public PolicyInfo(int paramInt1, String paramString, int paramInt2, int paramInt3)
    {
      this(paramInt1, paramString, paramInt2, paramInt3, paramInt2, paramInt3);
    }
    
    public PolicyInfo(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      ident = paramInt1;
      tag = paramString;
      label = paramInt2;
      description = paramInt3;
      labelForSecondaryUsers = paramInt4;
      descriptionForSecondaryUsers = paramInt5;
    }
  }
}
