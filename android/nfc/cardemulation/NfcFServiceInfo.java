package android.nfc.cardemulation;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public final class NfcFServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<NfcFServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public NfcFServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      ResolveInfo localResolveInfo = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(paramAnonymousParcel);
      String str1 = paramAnonymousParcel.readString();
      String str2 = paramAnonymousParcel.readString();
      String str3 = null;
      if (paramAnonymousParcel.readInt() != 0) {
        str3 = paramAnonymousParcel.readString();
      }
      String str4 = paramAnonymousParcel.readString();
      String str5 = null;
      if (paramAnonymousParcel.readInt() != 0) {
        str5 = paramAnonymousParcel.readString();
      }
      return new NfcFServiceInfo(localResolveInfo, str1, str2, str3, str4, str5, paramAnonymousParcel.readInt(), paramAnonymousParcel.readString());
    }
    
    public NfcFServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new NfcFServiceInfo[paramAnonymousInt];
    }
  };
  private static final String DEFAULT_T3T_PMM = "FFFFFFFFFFFFFFFF";
  static final String TAG = "NfcFServiceInfo";
  final String mDescription;
  String mDynamicNfcid2;
  String mDynamicSystemCode;
  final String mNfcid2;
  final ResolveInfo mService;
  final String mSystemCode;
  final String mT3tPmm;
  final int mUid;
  
  /* Error */
  public NfcFServiceInfo(PackageManager paramPackageManager, ResolveInfo paramResolveInfo)
    throws org.xmlpull.v1.XmlPullParserException, java.io.IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 44	java/lang/Object:<init>	()V
    //   4: aload_2
    //   5: getfield 50	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   8: astore_3
    //   9: aconst_null
    //   10: astore 4
    //   12: aconst_null
    //   13: astore 5
    //   15: aload_3
    //   16: aload_1
    //   17: ldc 52
    //   19: invokevirtual 58	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   22: astore 6
    //   24: aload 6
    //   26: ifnull +929 -> 955
    //   29: aload 6
    //   31: astore 5
    //   33: aload 6
    //   35: astore 4
    //   37: aload 6
    //   39: invokeinterface 64 1 0
    //   44: istore 7
    //   46: iload 7
    //   48: iconst_2
    //   49: if_icmpeq +29 -> 78
    //   52: iload 7
    //   54: iconst_1
    //   55: if_icmpeq +23 -> 78
    //   58: aload 6
    //   60: astore 5
    //   62: aload 6
    //   64: astore 4
    //   66: aload 6
    //   68: invokeinterface 67 1 0
    //   73: istore 7
    //   75: goto -29 -> 46
    //   78: aload 6
    //   80: astore 5
    //   82: aload 6
    //   84: astore 4
    //   86: ldc 69
    //   88: aload 6
    //   90: invokeinterface 73 1 0
    //   95: invokevirtual 79	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   98: ifeq +821 -> 919
    //   101: aload 6
    //   103: astore 5
    //   105: aload 6
    //   107: astore 4
    //   109: aload_1
    //   110: aload_3
    //   111: getfield 83	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   114: invokevirtual 89	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   117: astore 8
    //   119: aload 6
    //   121: astore 5
    //   123: aload 6
    //   125: astore 4
    //   127: aload 6
    //   129: invokestatic 95	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   132: astore 9
    //   134: aload 6
    //   136: astore 5
    //   138: aload 6
    //   140: astore 4
    //   142: aload 8
    //   144: aload 9
    //   146: getstatic 101	com/android/internal/R$styleable:HostNfcFService	[I
    //   149: invokevirtual 107	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   152: astore_1
    //   153: aload 6
    //   155: astore 5
    //   157: aload 6
    //   159: astore 4
    //   161: aload_0
    //   162: aload_2
    //   163: putfield 109	android/nfc/cardemulation/NfcFServiceInfo:mService	Landroid/content/pm/ResolveInfo;
    //   166: aload 6
    //   168: astore 5
    //   170: aload 6
    //   172: astore 4
    //   174: aload_0
    //   175: aload_1
    //   176: iconst_0
    //   177: invokevirtual 115	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   180: putfield 117	android/nfc/cardemulation/NfcFServiceInfo:mDescription	Ljava/lang/String;
    //   183: aload 6
    //   185: astore 5
    //   187: aload 6
    //   189: astore 4
    //   191: aload_0
    //   192: aconst_null
    //   193: putfield 119	android/nfc/cardemulation/NfcFServiceInfo:mDynamicSystemCode	Ljava/lang/String;
    //   196: aload 6
    //   198: astore 5
    //   200: aload 6
    //   202: astore 4
    //   204: aload_0
    //   205: aconst_null
    //   206: putfield 121	android/nfc/cardemulation/NfcFServiceInfo:mDynamicNfcid2	Ljava/lang/String;
    //   209: aload 6
    //   211: astore 5
    //   213: aload 6
    //   215: astore 4
    //   217: aload_1
    //   218: invokevirtual 124	android/content/res/TypedArray:recycle	()V
    //   221: aconst_null
    //   222: astore 10
    //   224: aconst_null
    //   225: astore_2
    //   226: aconst_null
    //   227: astore_1
    //   228: aload 6
    //   230: astore 5
    //   232: aload 6
    //   234: astore 4
    //   236: aload 6
    //   238: invokeinterface 127 1 0
    //   243: istore 7
    //   245: aload 6
    //   247: astore 5
    //   249: aload 6
    //   251: astore 4
    //   253: aload 6
    //   255: invokeinterface 67 1 0
    //   260: istore 11
    //   262: iload 11
    //   264: iconst_3
    //   265: if_icmpne +26 -> 291
    //   268: aload 6
    //   270: astore 5
    //   272: aload 6
    //   274: astore 4
    //   276: aload 6
    //   278: invokeinterface 127 1 0
    //   283: iload 7
    //   285: if_icmple +538 -> 823
    //   288: goto +3 -> 291
    //   291: iload 11
    //   293: iconst_1
    //   294: if_icmpeq +529 -> 823
    //   297: aload 6
    //   299: astore 5
    //   301: aload 6
    //   303: astore 4
    //   305: aload 6
    //   307: invokeinterface 73 1 0
    //   312: astore 12
    //   314: iload 11
    //   316: iconst_2
    //   317: if_icmpne +203 -> 520
    //   320: aload 6
    //   322: astore 5
    //   324: aload 6
    //   326: astore 4
    //   328: ldc -127
    //   330: aload 12
    //   332: invokevirtual 79	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   335: ifeq +185 -> 520
    //   338: aload 10
    //   340: ifnonnull +180 -> 520
    //   343: aload 6
    //   345: astore 5
    //   347: aload 6
    //   349: astore 4
    //   351: aload 8
    //   353: aload 9
    //   355: getstatic 132	com/android/internal/R$styleable:SystemCodeFilter	[I
    //   358: invokevirtual 107	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   361: astore 13
    //   363: aload 6
    //   365: astore 5
    //   367: aload 6
    //   369: astore 4
    //   371: aload 13
    //   373: iconst_0
    //   374: invokevirtual 115	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   377: invokevirtual 135	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   380: astore 12
    //   382: aload 12
    //   384: astore 10
    //   386: aload 6
    //   388: astore 5
    //   390: aload 6
    //   392: astore 4
    //   394: aload 12
    //   396: invokestatic 141	android/nfc/cardemulation/NfcFCardEmulation:isValidSystemCode	(Ljava/lang/String;)Z
    //   399: ifne +105 -> 504
    //   402: aload 12
    //   404: astore 10
    //   406: aload 6
    //   408: astore 5
    //   410: aload 6
    //   412: astore 4
    //   414: aload 12
    //   416: ldc -113
    //   418: invokevirtual 146	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   421: ifne +83 -> 504
    //   424: aload 6
    //   426: astore 5
    //   428: aload 6
    //   430: astore 4
    //   432: new 148	java/lang/StringBuilder
    //   435: astore 10
    //   437: aload 6
    //   439: astore 5
    //   441: aload 6
    //   443: astore 4
    //   445: aload 10
    //   447: invokespecial 149	java/lang/StringBuilder:<init>	()V
    //   450: aload 6
    //   452: astore 5
    //   454: aload 6
    //   456: astore 4
    //   458: aload 10
    //   460: ldc -105
    //   462: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   465: pop
    //   466: aload 6
    //   468: astore 5
    //   470: aload 6
    //   472: astore 4
    //   474: aload 10
    //   476: aload 12
    //   478: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   481: pop
    //   482: aload 6
    //   484: astore 5
    //   486: aload 6
    //   488: astore 4
    //   490: ldc 18
    //   492: aload 10
    //   494: invokevirtual 158	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   497: invokestatic 164	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   500: pop
    //   501: aconst_null
    //   502: astore 10
    //   504: aload 6
    //   506: astore 5
    //   508: aload 6
    //   510: astore 4
    //   512: aload 13
    //   514: invokevirtual 124	android/content/res/TypedArray:recycle	()V
    //   517: goto -272 -> 245
    //   520: iload 11
    //   522: iconst_2
    //   523: if_icmpne +215 -> 738
    //   526: aload 6
    //   528: astore 5
    //   530: aload 6
    //   532: astore 4
    //   534: ldc -90
    //   536: aload 12
    //   538: invokevirtual 79	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   541: ifeq +197 -> 738
    //   544: aload_2
    //   545: ifnonnull +193 -> 738
    //   548: aload 6
    //   550: astore 5
    //   552: aload 6
    //   554: astore 4
    //   556: aload 8
    //   558: aload 9
    //   560: getstatic 169	com/android/internal/R$styleable:Nfcid2Filter	[I
    //   563: invokevirtual 107	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   566: astore 13
    //   568: aload 6
    //   570: astore 5
    //   572: aload 6
    //   574: astore 4
    //   576: aload 13
    //   578: iconst_0
    //   579: invokevirtual 115	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   582: invokevirtual 135	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   585: astore 12
    //   587: aload 12
    //   589: astore_2
    //   590: aload 6
    //   592: astore 5
    //   594: aload 6
    //   596: astore 4
    //   598: aload 12
    //   600: ldc -85
    //   602: invokevirtual 146	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   605: ifne +117 -> 722
    //   608: aload 12
    //   610: astore_2
    //   611: aload 6
    //   613: astore 5
    //   615: aload 6
    //   617: astore 4
    //   619: aload 12
    //   621: ldc -113
    //   623: invokevirtual 146	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   626: ifne +96 -> 722
    //   629: aload 12
    //   631: astore_2
    //   632: aload 6
    //   634: astore 5
    //   636: aload 6
    //   638: astore 4
    //   640: aload 12
    //   642: invokestatic 174	android/nfc/cardemulation/NfcFCardEmulation:isValidNfcid2	(Ljava/lang/String;)Z
    //   645: ifne +77 -> 722
    //   648: aload 6
    //   650: astore 5
    //   652: aload 6
    //   654: astore 4
    //   656: new 148	java/lang/StringBuilder
    //   659: astore_2
    //   660: aload 6
    //   662: astore 5
    //   664: aload 6
    //   666: astore 4
    //   668: aload_2
    //   669: invokespecial 149	java/lang/StringBuilder:<init>	()V
    //   672: aload 6
    //   674: astore 5
    //   676: aload 6
    //   678: astore 4
    //   680: aload_2
    //   681: ldc -80
    //   683: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   686: pop
    //   687: aload 6
    //   689: astore 5
    //   691: aload 6
    //   693: astore 4
    //   695: aload_2
    //   696: aload 12
    //   698: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   701: pop
    //   702: aload 6
    //   704: astore 5
    //   706: aload 6
    //   708: astore 4
    //   710: ldc 18
    //   712: aload_2
    //   713: invokevirtual 158	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   716: invokestatic 164	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   719: pop
    //   720: aconst_null
    //   721: astore_2
    //   722: aload 6
    //   724: astore 5
    //   726: aload 6
    //   728: astore 4
    //   730: aload 13
    //   732: invokevirtual 124	android/content/res/TypedArray:recycle	()V
    //   735: goto -218 -> 517
    //   738: iload 11
    //   740: iconst_2
    //   741: if_icmpne +79 -> 820
    //   744: aload 6
    //   746: astore 5
    //   748: aload 6
    //   750: astore 4
    //   752: aload 12
    //   754: ldc -78
    //   756: invokevirtual 79	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   759: ifeq +61 -> 820
    //   762: aload_1
    //   763: ifnonnull +57 -> 820
    //   766: aload 6
    //   768: astore 5
    //   770: aload 6
    //   772: astore 4
    //   774: aload 8
    //   776: aload 9
    //   778: getstatic 181	com/android/internal/R$styleable:T3tPmmFilter	[I
    //   781: invokevirtual 107	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   784: astore 12
    //   786: aload 6
    //   788: astore 5
    //   790: aload 6
    //   792: astore 4
    //   794: aload 12
    //   796: iconst_0
    //   797: invokevirtual 115	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   800: invokevirtual 135	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   803: astore_1
    //   804: aload 6
    //   806: astore 5
    //   808: aload 6
    //   810: astore 4
    //   812: aload 12
    //   814: invokevirtual 124	android/content/res/TypedArray:recycle	()V
    //   817: goto +3 -> 820
    //   820: goto -575 -> 245
    //   823: aload 10
    //   825: ifnonnull +10 -> 835
    //   828: ldc -113
    //   830: astore 10
    //   832: goto +3 -> 835
    //   835: aload 6
    //   837: astore 5
    //   839: aload 6
    //   841: astore 4
    //   843: aload_0
    //   844: aload 10
    //   846: putfield 183	android/nfc/cardemulation/NfcFServiceInfo:mSystemCode	Ljava/lang/String;
    //   849: aload_2
    //   850: ifnonnull +9 -> 859
    //   853: ldc -113
    //   855: astore_2
    //   856: goto +3 -> 859
    //   859: aload 6
    //   861: astore 5
    //   863: aload 6
    //   865: astore 4
    //   867: aload_0
    //   868: aload_2
    //   869: putfield 185	android/nfc/cardemulation/NfcFServiceInfo:mNfcid2	Ljava/lang/String;
    //   872: aload_1
    //   873: ifnonnull +9 -> 882
    //   876: ldc 15
    //   878: astore_1
    //   879: goto +3 -> 882
    //   882: aload 6
    //   884: astore 5
    //   886: aload 6
    //   888: astore 4
    //   890: aload_0
    //   891: aload_1
    //   892: putfield 187	android/nfc/cardemulation/NfcFServiceInfo:mT3tPmm	Ljava/lang/String;
    //   895: aload 6
    //   897: ifnull +10 -> 907
    //   900: aload 6
    //   902: invokeinterface 190 1 0
    //   907: aload_0
    //   908: aload_3
    //   909: getfield 83	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   912: getfield 195	android/content/pm/ApplicationInfo:uid	I
    //   915: putfield 197	android/nfc/cardemulation/NfcFServiceInfo:mUid	I
    //   918: return
    //   919: aload 6
    //   921: astore 5
    //   923: aload 6
    //   925: astore 4
    //   927: new 39	org/xmlpull/v1/XmlPullParserException
    //   930: astore_1
    //   931: aload 6
    //   933: astore 5
    //   935: aload 6
    //   937: astore 4
    //   939: aload_1
    //   940: ldc -57
    //   942: invokespecial 202	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   945: aload 6
    //   947: astore 5
    //   949: aload 6
    //   951: astore 4
    //   953: aload_1
    //   954: athrow
    //   955: aload 6
    //   957: astore 5
    //   959: aload 6
    //   961: astore 4
    //   963: new 39	org/xmlpull/v1/XmlPullParserException
    //   966: astore_1
    //   967: aload 6
    //   969: astore 5
    //   971: aload 6
    //   973: astore 4
    //   975: aload_1
    //   976: ldc -52
    //   978: invokespecial 202	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   981: aload 6
    //   983: astore 5
    //   985: aload 6
    //   987: astore 4
    //   989: aload_1
    //   990: athrow
    //   991: astore_1
    //   992: goto +70 -> 1062
    //   995: astore_1
    //   996: aload 4
    //   998: astore 5
    //   1000: new 39	org/xmlpull/v1/XmlPullParserException
    //   1003: astore_1
    //   1004: aload 4
    //   1006: astore 5
    //   1008: new 148	java/lang/StringBuilder
    //   1011: astore_2
    //   1012: aload 4
    //   1014: astore 5
    //   1016: aload_2
    //   1017: invokespecial 149	java/lang/StringBuilder:<init>	()V
    //   1020: aload 4
    //   1022: astore 5
    //   1024: aload_2
    //   1025: ldc -50
    //   1027: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1030: pop
    //   1031: aload 4
    //   1033: astore 5
    //   1035: aload_2
    //   1036: aload_3
    //   1037: getfield 209	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   1040: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1043: pop
    //   1044: aload 4
    //   1046: astore 5
    //   1048: aload_1
    //   1049: aload_2
    //   1050: invokevirtual 158	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1053: invokespecial 202	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   1056: aload 4
    //   1058: astore 5
    //   1060: aload_1
    //   1061: athrow
    //   1062: aload 5
    //   1064: ifnull +10 -> 1074
    //   1067: aload 5
    //   1069: invokeinterface 190 1 0
    //   1074: aload_1
    //   1075: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1076	0	this	NfcFServiceInfo
    //   0	1076	1	paramPackageManager	PackageManager
    //   0	1076	2	paramResolveInfo	ResolveInfo
    //   8	1029	3	localServiceInfo	ServiceInfo
    //   10	1047	4	localObject1	Object
    //   13	1055	5	localObject2	Object
    //   22	964	6	localXmlResourceParser	android.content.res.XmlResourceParser
    //   44	242	7	i	int
    //   117	658	8	localResources	android.content.res.Resources
    //   132	645	9	localAttributeSet	android.util.AttributeSet
    //   222	623	10	localObject3	Object
    //   260	482	11	j	int
    //   312	501	12	localObject4	Object
    //   361	370	13	localTypedArray	android.content.res.TypedArray
    // Exception table:
    //   from	to	target	type
    //   15	24	991	finally
    //   37	46	991	finally
    //   66	75	991	finally
    //   86	101	991	finally
    //   109	119	991	finally
    //   127	134	991	finally
    //   142	153	991	finally
    //   161	166	991	finally
    //   174	183	991	finally
    //   191	196	991	finally
    //   204	209	991	finally
    //   217	221	991	finally
    //   236	245	991	finally
    //   253	262	991	finally
    //   276	288	991	finally
    //   305	314	991	finally
    //   328	338	991	finally
    //   351	363	991	finally
    //   371	382	991	finally
    //   394	402	991	finally
    //   414	424	991	finally
    //   432	437	991	finally
    //   445	450	991	finally
    //   458	466	991	finally
    //   474	482	991	finally
    //   490	501	991	finally
    //   512	517	991	finally
    //   534	544	991	finally
    //   556	568	991	finally
    //   576	587	991	finally
    //   598	608	991	finally
    //   619	629	991	finally
    //   640	648	991	finally
    //   656	660	991	finally
    //   668	672	991	finally
    //   680	687	991	finally
    //   695	702	991	finally
    //   710	720	991	finally
    //   730	735	991	finally
    //   752	762	991	finally
    //   774	786	991	finally
    //   794	804	991	finally
    //   812	817	991	finally
    //   843	849	991	finally
    //   867	872	991	finally
    //   890	895	991	finally
    //   927	931	991	finally
    //   939	945	991	finally
    //   953	955	991	finally
    //   963	967	991	finally
    //   975	981	991	finally
    //   989	991	991	finally
    //   1000	1004	991	finally
    //   1008	1012	991	finally
    //   1016	1020	991	finally
    //   1024	1031	991	finally
    //   1035	1044	991	finally
    //   1048	1056	991	finally
    //   1060	1062	991	finally
    //   15	24	995	android/content/pm/PackageManager$NameNotFoundException
    //   37	46	995	android/content/pm/PackageManager$NameNotFoundException
    //   66	75	995	android/content/pm/PackageManager$NameNotFoundException
    //   86	101	995	android/content/pm/PackageManager$NameNotFoundException
    //   109	119	995	android/content/pm/PackageManager$NameNotFoundException
    //   127	134	995	android/content/pm/PackageManager$NameNotFoundException
    //   142	153	995	android/content/pm/PackageManager$NameNotFoundException
    //   161	166	995	android/content/pm/PackageManager$NameNotFoundException
    //   174	183	995	android/content/pm/PackageManager$NameNotFoundException
    //   191	196	995	android/content/pm/PackageManager$NameNotFoundException
    //   204	209	995	android/content/pm/PackageManager$NameNotFoundException
    //   217	221	995	android/content/pm/PackageManager$NameNotFoundException
    //   236	245	995	android/content/pm/PackageManager$NameNotFoundException
    //   253	262	995	android/content/pm/PackageManager$NameNotFoundException
    //   276	288	995	android/content/pm/PackageManager$NameNotFoundException
    //   305	314	995	android/content/pm/PackageManager$NameNotFoundException
    //   328	338	995	android/content/pm/PackageManager$NameNotFoundException
    //   351	363	995	android/content/pm/PackageManager$NameNotFoundException
    //   371	382	995	android/content/pm/PackageManager$NameNotFoundException
    //   394	402	995	android/content/pm/PackageManager$NameNotFoundException
    //   414	424	995	android/content/pm/PackageManager$NameNotFoundException
    //   432	437	995	android/content/pm/PackageManager$NameNotFoundException
    //   445	450	995	android/content/pm/PackageManager$NameNotFoundException
    //   458	466	995	android/content/pm/PackageManager$NameNotFoundException
    //   474	482	995	android/content/pm/PackageManager$NameNotFoundException
    //   490	501	995	android/content/pm/PackageManager$NameNotFoundException
    //   512	517	995	android/content/pm/PackageManager$NameNotFoundException
    //   534	544	995	android/content/pm/PackageManager$NameNotFoundException
    //   556	568	995	android/content/pm/PackageManager$NameNotFoundException
    //   576	587	995	android/content/pm/PackageManager$NameNotFoundException
    //   598	608	995	android/content/pm/PackageManager$NameNotFoundException
    //   619	629	995	android/content/pm/PackageManager$NameNotFoundException
    //   640	648	995	android/content/pm/PackageManager$NameNotFoundException
    //   656	660	995	android/content/pm/PackageManager$NameNotFoundException
    //   668	672	995	android/content/pm/PackageManager$NameNotFoundException
    //   680	687	995	android/content/pm/PackageManager$NameNotFoundException
    //   695	702	995	android/content/pm/PackageManager$NameNotFoundException
    //   710	720	995	android/content/pm/PackageManager$NameNotFoundException
    //   730	735	995	android/content/pm/PackageManager$NameNotFoundException
    //   752	762	995	android/content/pm/PackageManager$NameNotFoundException
    //   774	786	995	android/content/pm/PackageManager$NameNotFoundException
    //   794	804	995	android/content/pm/PackageManager$NameNotFoundException
    //   812	817	995	android/content/pm/PackageManager$NameNotFoundException
    //   843	849	995	android/content/pm/PackageManager$NameNotFoundException
    //   867	872	995	android/content/pm/PackageManager$NameNotFoundException
    //   890	895	995	android/content/pm/PackageManager$NameNotFoundException
    //   927	931	995	android/content/pm/PackageManager$NameNotFoundException
    //   939	945	995	android/content/pm/PackageManager$NameNotFoundException
    //   953	955	995	android/content/pm/PackageManager$NameNotFoundException
    //   963	967	995	android/content/pm/PackageManager$NameNotFoundException
    //   975	981	995	android/content/pm/PackageManager$NameNotFoundException
    //   989	991	995	android/content/pm/PackageManager$NameNotFoundException
  }
  
  public NfcFServiceInfo(ResolveInfo paramResolveInfo, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt, String paramString6)
  {
    mService = paramResolveInfo;
    mDescription = paramString1;
    mSystemCode = paramString2;
    mDynamicSystemCode = paramString3;
    mNfcid2 = paramString4;
    mDynamicNfcid2 = paramString5;
    mUid = paramInt;
    mT3tPmm = paramString6;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("    ");
    paramFileDescriptor.append(getComponent());
    paramFileDescriptor.append(" (Description: ");
    paramFileDescriptor.append(getDescription());
    paramFileDescriptor.append(")");
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("    System Code: ");
    paramFileDescriptor.append(getSystemCode());
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("    NFCID2: ");
    paramFileDescriptor.append(getNfcid2());
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("    T3tPmm: ");
    paramFileDescriptor.append(getT3tPmm());
    paramPrintWriter.println(paramFileDescriptor.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof NfcFServiceInfo)) {
      return false;
    }
    paramObject = (NfcFServiceInfo)paramObject;
    if (!paramObject.getComponent().equals(getComponent())) {
      return false;
    }
    if (!mSystemCode.equalsIgnoreCase(mSystemCode)) {
      return false;
    }
    if (!mNfcid2.equalsIgnoreCase(mNfcid2)) {
      return false;
    }
    return mT3tPmm.equalsIgnoreCase(mT3tPmm);
  }
  
  public ComponentName getComponent()
  {
    return new ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public String getNfcid2()
  {
    String str;
    if (mDynamicNfcid2 == null) {
      str = mNfcid2;
    } else {
      str = mDynamicNfcid2;
    }
    return str;
  }
  
  public String getSystemCode()
  {
    String str;
    if (mDynamicSystemCode == null) {
      str = mSystemCode;
    } else {
      str = mDynamicSystemCode;
    }
    return str;
  }
  
  public String getT3tPmm()
  {
    return mT3tPmm;
  }
  
  public int getUid()
  {
    return mUid;
  }
  
  public int hashCode()
  {
    return getComponent().hashCode();
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    return mService.loadIcon(paramPackageManager);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    return mService.loadLabel(paramPackageManager);
  }
  
  public void setOrReplaceDynamicNfcid2(String paramString)
  {
    mDynamicNfcid2 = paramString;
  }
  
  public void setOrReplaceDynamicSystemCode(String paramString)
  {
    mDynamicSystemCode = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder("NfcFService: ");
    localStringBuilder1.append(getComponent());
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", description: ");
    localStringBuilder2.append(mDescription);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", System Code: ");
    localStringBuilder2.append(mSystemCode);
    localStringBuilder1.append(localStringBuilder2.toString());
    if (mDynamicSystemCode != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", dynamic System Code: ");
      localStringBuilder2.append(mDynamicSystemCode);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", NFCID2: ");
    localStringBuilder2.append(mNfcid2);
    localStringBuilder1.append(localStringBuilder2.toString());
    if (mDynamicNfcid2 != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", dynamic NFCID2: ");
      localStringBuilder2.append(mDynamicNfcid2);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", T3T PMM:");
    localStringBuilder2.append(mT3tPmm);
    localStringBuilder1.append(localStringBuilder2.toString());
    return localStringBuilder1.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mService.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mDescription);
    paramParcel.writeString(mSystemCode);
    String str = mDynamicSystemCode;
    int i = 0;
    if (str != null) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    paramParcel.writeInt(paramInt);
    if (mDynamicSystemCode != null) {
      paramParcel.writeString(mDynamicSystemCode);
    }
    paramParcel.writeString(mNfcid2);
    paramInt = i;
    if (mDynamicNfcid2 != null) {
      paramInt = 1;
    }
    paramParcel.writeInt(paramInt);
    if (mDynamicNfcid2 != null) {
      paramParcel.writeString(mDynamicNfcid2);
    }
    paramParcel.writeInt(mUid);
    paramParcel.writeString(mT3tPmm);
  }
}
