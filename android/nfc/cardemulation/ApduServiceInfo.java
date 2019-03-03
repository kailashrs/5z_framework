package android.nfc.cardemulation;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class ApduServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ApduServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public ApduServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      ResolveInfo localResolveInfo = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(paramAnonymousParcel);
      String str = paramAnonymousParcel.readString();
      boolean bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      ArrayList localArrayList1 = new ArrayList();
      if (paramAnonymousParcel.readInt() > 0) {
        paramAnonymousParcel.readTypedList(localArrayList1, AidGroup.CREATOR);
      }
      ArrayList localArrayList2 = new ArrayList();
      if (paramAnonymousParcel.readInt() > 0) {
        paramAnonymousParcel.readTypedList(localArrayList2, AidGroup.CREATOR);
      }
      boolean bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      return new ApduServiceInfo(localResolveInfo, bool1, str, localArrayList1, localArrayList2, bool2, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readString());
    }
    
    public ApduServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new ApduServiceInfo[paramAnonymousInt];
    }
  };
  static final String TAG = "ApduServiceInfo";
  protected int mBannerResourceId;
  protected String mDescription;
  protected HashMap<String, AidGroup> mDynamicAidGroups;
  protected boolean mOnHost;
  protected boolean mRequiresDeviceUnlock;
  protected ResolveInfo mService;
  protected String mSettingsActivityName;
  protected HashMap<String, AidGroup> mStaticAidGroups;
  protected int mUid;
  
  /* Error */
  public ApduServiceInfo(PackageManager paramPackageManager, ResolveInfo paramResolveInfo, boolean paramBoolean)
    throws org.xmlpull.v1.XmlPullParserException, java.io.IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 45	java/lang/Object:<init>	()V
    //   4: aload_2
    //   5: getfield 51	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   8: astore 4
    //   10: aconst_null
    //   11: astore 5
    //   13: aconst_null
    //   14: astore 6
    //   16: iload_3
    //   17: ifeq +65 -> 82
    //   20: aload 4
    //   22: aload_1
    //   23: ldc 53
    //   25: invokevirtual 59	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   28: astore 7
    //   30: aload 7
    //   32: ifnull +6 -> 38
    //   35: goto +62 -> 97
    //   38: aload 7
    //   40: astore 6
    //   42: aload 7
    //   44: astore 5
    //   46: new 40	org/xmlpull/v1/XmlPullParserException
    //   49: astore_1
    //   50: aload 7
    //   52: astore 6
    //   54: aload 7
    //   56: astore 5
    //   58: aload_1
    //   59: ldc 61
    //   61: invokespecial 64	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   64: aload 7
    //   66: astore 6
    //   68: aload 7
    //   70: astore 5
    //   72: aload_1
    //   73: athrow
    //   74: astore_1
    //   75: goto +1817 -> 1892
    //   78: astore_1
    //   79: goto +1745 -> 1824
    //   82: aload 4
    //   84: aload_1
    //   85: ldc 66
    //   87: invokevirtual 59	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   90: astore 7
    //   92: aload 7
    //   94: ifnull +1693 -> 1787
    //   97: aload 7
    //   99: astore 6
    //   101: aload 7
    //   103: astore 5
    //   105: aload 7
    //   107: invokeinterface 72 1 0
    //   112: istore 8
    //   114: iload 8
    //   116: iconst_2
    //   117: if_icmpeq +29 -> 146
    //   120: iload 8
    //   122: iconst_1
    //   123: if_icmpeq +23 -> 146
    //   126: aload 7
    //   128: astore 6
    //   130: aload 7
    //   132: astore 5
    //   134: aload 7
    //   136: invokeinterface 75 1 0
    //   141: istore 8
    //   143: goto -29 -> 114
    //   146: aload 7
    //   148: astore 6
    //   150: aload 7
    //   152: astore 5
    //   154: aload 7
    //   156: invokeinterface 79 1 0
    //   161: astore 9
    //   163: iload_3
    //   164: ifeq +60 -> 224
    //   167: aload 7
    //   169: astore 6
    //   171: aload 7
    //   173: astore 5
    //   175: ldc 81
    //   177: aload 9
    //   179: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   182: ifeq +6 -> 188
    //   185: goto +39 -> 224
    //   188: aload 7
    //   190: astore 6
    //   192: aload 7
    //   194: astore 5
    //   196: new 40	org/xmlpull/v1/XmlPullParserException
    //   199: astore_1
    //   200: aload 7
    //   202: astore 6
    //   204: aload 7
    //   206: astore 5
    //   208: aload_1
    //   209: ldc 89
    //   211: invokespecial 64	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   214: aload 7
    //   216: astore 6
    //   218: aload 7
    //   220: astore 5
    //   222: aload_1
    //   223: athrow
    //   224: iload_3
    //   225: ifne +60 -> 285
    //   228: aload 7
    //   230: astore 6
    //   232: aload 7
    //   234: astore 5
    //   236: ldc 91
    //   238: aload 9
    //   240: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   243: ifeq +6 -> 249
    //   246: goto +39 -> 285
    //   249: aload 7
    //   251: astore 6
    //   253: aload 7
    //   255: astore 5
    //   257: new 40	org/xmlpull/v1/XmlPullParserException
    //   260: astore_1
    //   261: aload 7
    //   263: astore 6
    //   265: aload 7
    //   267: astore 5
    //   269: aload_1
    //   270: ldc 93
    //   272: invokespecial 64	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   275: aload 7
    //   277: astore 6
    //   279: aload 7
    //   281: astore 5
    //   283: aload_1
    //   284: athrow
    //   285: aload 7
    //   287: astore 6
    //   289: aload 7
    //   291: astore 5
    //   293: aload_1
    //   294: aload 4
    //   296: getfield 97	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   299: invokevirtual 103	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   302: astore 9
    //   304: aload 7
    //   306: astore 6
    //   308: aload 7
    //   310: astore 5
    //   312: aload 7
    //   314: invokestatic 109	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   317: astore 10
    //   319: iload_3
    //   320: ifeq +120 -> 440
    //   323: aload 7
    //   325: astore 6
    //   327: aload 7
    //   329: astore 5
    //   331: aload 9
    //   333: aload 10
    //   335: getstatic 115	com/android/internal/R$styleable:HostApduService	[I
    //   338: invokevirtual 121	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   341: astore_1
    //   342: aload 7
    //   344: astore 6
    //   346: aload 7
    //   348: astore 5
    //   350: aload_0
    //   351: aload_2
    //   352: putfield 123	android/nfc/cardemulation/ApduServiceInfo:mService	Landroid/content/pm/ResolveInfo;
    //   355: aload 7
    //   357: astore 6
    //   359: aload 7
    //   361: astore 5
    //   363: aload_0
    //   364: aload_1
    //   365: iconst_0
    //   366: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   369: putfield 131	android/nfc/cardemulation/ApduServiceInfo:mDescription	Ljava/lang/String;
    //   372: aload 7
    //   374: astore 6
    //   376: aload 7
    //   378: astore 5
    //   380: aload_0
    //   381: aload_1
    //   382: iconst_2
    //   383: iconst_0
    //   384: invokevirtual 135	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   387: putfield 137	android/nfc/cardemulation/ApduServiceInfo:mRequiresDeviceUnlock	Z
    //   390: aload 7
    //   392: astore 6
    //   394: aload 7
    //   396: astore 5
    //   398: aload_0
    //   399: aload_1
    //   400: iconst_3
    //   401: iconst_m1
    //   402: invokevirtual 141	android/content/res/TypedArray:getResourceId	(II)I
    //   405: putfield 143	android/nfc/cardemulation/ApduServiceInfo:mBannerResourceId	I
    //   408: aload 7
    //   410: astore 6
    //   412: aload 7
    //   414: astore 5
    //   416: aload_0
    //   417: aload_1
    //   418: iconst_1
    //   419: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   422: putfield 145	android/nfc/cardemulation/ApduServiceInfo:mSettingsActivityName	Ljava/lang/String;
    //   425: aload 7
    //   427: astore 6
    //   429: aload 7
    //   431: astore 5
    //   433: aload_1
    //   434: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   437: goto +112 -> 549
    //   440: aload 7
    //   442: astore 6
    //   444: aload 7
    //   446: astore 5
    //   448: aload 9
    //   450: aload 10
    //   452: getstatic 151	com/android/internal/R$styleable:OffHostApduService	[I
    //   455: invokevirtual 121	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   458: astore_1
    //   459: aload 7
    //   461: astore 6
    //   463: aload 7
    //   465: astore 5
    //   467: aload_0
    //   468: aload_2
    //   469: putfield 123	android/nfc/cardemulation/ApduServiceInfo:mService	Landroid/content/pm/ResolveInfo;
    //   472: aload 7
    //   474: astore 6
    //   476: aload 7
    //   478: astore 5
    //   480: aload_0
    //   481: aload_1
    //   482: iconst_0
    //   483: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   486: putfield 131	android/nfc/cardemulation/ApduServiceInfo:mDescription	Ljava/lang/String;
    //   489: aload 7
    //   491: astore 6
    //   493: aload 7
    //   495: astore 5
    //   497: aload_0
    //   498: iconst_0
    //   499: putfield 137	android/nfc/cardemulation/ApduServiceInfo:mRequiresDeviceUnlock	Z
    //   502: aload 7
    //   504: astore 6
    //   506: aload 7
    //   508: astore 5
    //   510: aload_0
    //   511: aload_1
    //   512: iconst_2
    //   513: iconst_m1
    //   514: invokevirtual 141	android/content/res/TypedArray:getResourceId	(II)I
    //   517: putfield 143	android/nfc/cardemulation/ApduServiceInfo:mBannerResourceId	I
    //   520: aload 7
    //   522: astore 6
    //   524: aload 7
    //   526: astore 5
    //   528: aload_0
    //   529: aload_1
    //   530: iconst_1
    //   531: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   534: putfield 145	android/nfc/cardemulation/ApduServiceInfo:mSettingsActivityName	Ljava/lang/String;
    //   537: aload 7
    //   539: astore 6
    //   541: aload 7
    //   543: astore 5
    //   545: aload_1
    //   546: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   549: aload 7
    //   551: astore 6
    //   553: aload 7
    //   555: astore 5
    //   557: new 153	java/util/HashMap
    //   560: astore_1
    //   561: aload 7
    //   563: astore 6
    //   565: aload 7
    //   567: astore 5
    //   569: aload_1
    //   570: invokespecial 154	java/util/HashMap:<init>	()V
    //   573: aload 7
    //   575: astore 6
    //   577: aload 7
    //   579: astore 5
    //   581: aload_0
    //   582: aload_1
    //   583: putfield 156	android/nfc/cardemulation/ApduServiceInfo:mStaticAidGroups	Ljava/util/HashMap;
    //   586: aload 7
    //   588: astore 6
    //   590: aload 7
    //   592: astore 5
    //   594: new 153	java/util/HashMap
    //   597: astore_1
    //   598: aload 7
    //   600: astore 6
    //   602: aload 7
    //   604: astore 5
    //   606: aload_1
    //   607: invokespecial 154	java/util/HashMap:<init>	()V
    //   610: aload 7
    //   612: astore 6
    //   614: aload 7
    //   616: astore 5
    //   618: aload_0
    //   619: aload_1
    //   620: putfield 158	android/nfc/cardemulation/ApduServiceInfo:mDynamicAidGroups	Ljava/util/HashMap;
    //   623: aload 7
    //   625: astore 6
    //   627: aload 7
    //   629: astore 5
    //   631: aload_0
    //   632: iload_3
    //   633: putfield 160	android/nfc/cardemulation/ApduServiceInfo:mOnHost	Z
    //   636: aload 7
    //   638: astore 6
    //   640: aload 7
    //   642: astore 5
    //   644: aload 7
    //   646: invokeinterface 163 1 0
    //   651: istore 8
    //   653: aconst_null
    //   654: astore_1
    //   655: aload 7
    //   657: astore 6
    //   659: aload 7
    //   661: astore 5
    //   663: aload 7
    //   665: invokeinterface 75 1 0
    //   670: istore 11
    //   672: iload 11
    //   674: iconst_3
    //   675: if_icmpne +23 -> 698
    //   678: aload 7
    //   680: astore 6
    //   682: aload 7
    //   684: astore 5
    //   686: aload 7
    //   688: invokeinterface 163 1 0
    //   693: iload 8
    //   695: if_icmple +1067 -> 1762
    //   698: iload 11
    //   700: iconst_1
    //   701: if_icmpeq +1061 -> 1762
    //   704: aload 7
    //   706: astore 6
    //   708: aload 7
    //   710: astore 5
    //   712: aload 7
    //   714: invokeinterface 79 1 0
    //   719: astore_2
    //   720: iload 11
    //   722: iconst_2
    //   723: if_icmpne +269 -> 992
    //   726: aload 7
    //   728: astore 6
    //   730: aload 7
    //   732: astore 5
    //   734: ldc -91
    //   736: aload_2
    //   737: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   740: ifeq +252 -> 992
    //   743: aload_1
    //   744: ifnonnull +248 -> 992
    //   747: aload 7
    //   749: astore 6
    //   751: aload 7
    //   753: astore 5
    //   755: aload 9
    //   757: aload 10
    //   759: getstatic 168	com/android/internal/R$styleable:AidGroup	[I
    //   762: invokevirtual 121	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   765: astore 12
    //   767: aload 7
    //   769: astore 6
    //   771: aload 7
    //   773: astore 5
    //   775: aload 12
    //   777: iconst_1
    //   778: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   781: astore_2
    //   782: aload 7
    //   784: astore 6
    //   786: aload 7
    //   788: astore 5
    //   790: aload 12
    //   792: iconst_0
    //   793: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   796: astore 13
    //   798: aload 7
    //   800: astore 6
    //   802: aload 7
    //   804: astore 5
    //   806: aload_2
    //   807: astore_1
    //   808: ldc -86
    //   810: aload_2
    //   811: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   814: ifne +6 -> 820
    //   817: ldc -84
    //   819: astore_1
    //   820: aload 7
    //   822: astore 6
    //   824: aload 7
    //   826: astore 5
    //   828: aload_0
    //   829: getfield 156	android/nfc/cardemulation/ApduServiceInfo:mStaticAidGroups	Ljava/util/HashMap;
    //   832: aload_1
    //   833: invokevirtual 176	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   836: checkcast 178	android/nfc/cardemulation/AidGroup
    //   839: astore_2
    //   840: aload_2
    //   841: ifnull +116 -> 957
    //   844: aload 7
    //   846: astore 6
    //   848: aload 7
    //   850: astore 5
    //   852: ldc -84
    //   854: aload_1
    //   855: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   858: ifne +94 -> 952
    //   861: aload 7
    //   863: astore 6
    //   865: aload 7
    //   867: astore 5
    //   869: new 180	java/lang/StringBuilder
    //   872: astore_2
    //   873: aload 7
    //   875: astore 6
    //   877: aload 7
    //   879: astore 5
    //   881: aload_2
    //   882: invokespecial 181	java/lang/StringBuilder:<init>	()V
    //   885: aload 7
    //   887: astore 6
    //   889: aload 7
    //   891: astore 5
    //   893: aload_2
    //   894: ldc -73
    //   896: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   899: pop
    //   900: aload 7
    //   902: astore 6
    //   904: aload 7
    //   906: astore 5
    //   908: aload_2
    //   909: aload_1
    //   910: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   913: pop
    //   914: aload 7
    //   916: astore 6
    //   918: aload 7
    //   920: astore 5
    //   922: aload_2
    //   923: ldc -67
    //   925: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   928: pop
    //   929: aload 7
    //   931: astore 6
    //   933: aload 7
    //   935: astore 5
    //   937: ldc 15
    //   939: aload_2
    //   940: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   943: invokestatic 198	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   946: pop
    //   947: aconst_null
    //   948: astore_1
    //   949: goto +27 -> 976
    //   952: aload_2
    //   953: astore_1
    //   954: goto +22 -> 976
    //   957: aload 7
    //   959: astore 6
    //   961: aload 7
    //   963: astore 5
    //   965: new 178	android/nfc/cardemulation/AidGroup
    //   968: dup
    //   969: aload_1
    //   970: aload 13
    //   972: invokespecial 201	android/nfc/cardemulation/AidGroup:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   975: astore_1
    //   976: aload 7
    //   978: astore 6
    //   980: aload 7
    //   982: astore 5
    //   984: aload 12
    //   986: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   989: goto +770 -> 1759
    //   992: iload 11
    //   994: iconst_3
    //   995: if_icmpne +111 -> 1106
    //   998: aload 7
    //   1000: astore 6
    //   1002: aload 7
    //   1004: astore 5
    //   1006: ldc -91
    //   1008: aload_2
    //   1009: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1012: ifeq +94 -> 1106
    //   1015: aload_1
    //   1016: ifnull +90 -> 1106
    //   1019: aload 7
    //   1021: astore 6
    //   1023: aload 7
    //   1025: astore 5
    //   1027: aload_1
    //   1028: getfield 205	android/nfc/cardemulation/AidGroup:aids	Ljava/util/List;
    //   1031: invokeinterface 210 1 0
    //   1036: ifle +49 -> 1085
    //   1039: aload 7
    //   1041: astore 6
    //   1043: aload 7
    //   1045: astore 5
    //   1047: aload_0
    //   1048: getfield 156	android/nfc/cardemulation/ApduServiceInfo:mStaticAidGroups	Ljava/util/HashMap;
    //   1051: aload_1
    //   1052: getfield 213	android/nfc/cardemulation/AidGroup:category	Ljava/lang/String;
    //   1055: invokevirtual 216	java/util/HashMap:containsKey	(Ljava/lang/Object;)Z
    //   1058: ifne +43 -> 1101
    //   1061: aload 7
    //   1063: astore 6
    //   1065: aload 7
    //   1067: astore 5
    //   1069: aload_0
    //   1070: getfield 156	android/nfc/cardemulation/ApduServiceInfo:mStaticAidGroups	Ljava/util/HashMap;
    //   1073: aload_1
    //   1074: getfield 213	android/nfc/cardemulation/AidGroup:category	Ljava/lang/String;
    //   1077: aload_1
    //   1078: invokevirtual 220	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1081: pop
    //   1082: goto +19 -> 1101
    //   1085: aload 7
    //   1087: astore 6
    //   1089: aload 7
    //   1091: astore 5
    //   1093: ldc 15
    //   1095: ldc -34
    //   1097: invokestatic 198	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   1100: pop
    //   1101: aconst_null
    //   1102: astore_1
    //   1103: goto +656 -> 1759
    //   1106: iload 11
    //   1108: iconst_2
    //   1109: if_icmpne +212 -> 1321
    //   1112: aload 7
    //   1114: astore 6
    //   1116: aload 7
    //   1118: astore 5
    //   1120: ldc -32
    //   1122: aload_2
    //   1123: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1126: ifeq +195 -> 1321
    //   1129: aload_1
    //   1130: ifnull +191 -> 1321
    //   1133: aload 7
    //   1135: astore 6
    //   1137: aload 7
    //   1139: astore 5
    //   1141: aload 9
    //   1143: aload 10
    //   1145: getstatic 227	com/android/internal/R$styleable:AidFilter	[I
    //   1148: invokevirtual 121	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   1151: astore 13
    //   1153: aload 7
    //   1155: astore 6
    //   1157: aload 7
    //   1159: astore 5
    //   1161: aload 13
    //   1163: iconst_0
    //   1164: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   1167: invokevirtual 230	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   1170: astore 12
    //   1172: aload 7
    //   1174: astore 6
    //   1176: aload 7
    //   1178: astore 5
    //   1180: aload 12
    //   1182: invokestatic 236	android/nfc/cardemulation/CardEmulation:isValidAid	(Ljava/lang/String;)Z
    //   1185: ifeq +48 -> 1233
    //   1188: aload 7
    //   1190: astore 6
    //   1192: aload 7
    //   1194: astore 5
    //   1196: aload_1
    //   1197: getfield 205	android/nfc/cardemulation/AidGroup:aids	Ljava/util/List;
    //   1200: aload 12
    //   1202: invokeinterface 239 2 0
    //   1207: ifne +26 -> 1233
    //   1210: aload 7
    //   1212: astore 6
    //   1214: aload 7
    //   1216: astore 5
    //   1218: aload_1
    //   1219: getfield 205	android/nfc/cardemulation/AidGroup:aids	Ljava/util/List;
    //   1222: aload 12
    //   1224: invokeinterface 242 2 0
    //   1229: pop
    //   1230: goto +75 -> 1305
    //   1233: aload 7
    //   1235: astore 6
    //   1237: aload 7
    //   1239: astore 5
    //   1241: new 180	java/lang/StringBuilder
    //   1244: astore_2
    //   1245: aload 7
    //   1247: astore 6
    //   1249: aload 7
    //   1251: astore 5
    //   1253: aload_2
    //   1254: invokespecial 181	java/lang/StringBuilder:<init>	()V
    //   1257: aload 7
    //   1259: astore 6
    //   1261: aload 7
    //   1263: astore 5
    //   1265: aload_2
    //   1266: ldc -12
    //   1268: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1271: pop
    //   1272: aload 7
    //   1274: astore 6
    //   1276: aload 7
    //   1278: astore 5
    //   1280: aload_2
    //   1281: aload 12
    //   1283: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1286: pop
    //   1287: aload 7
    //   1289: astore 6
    //   1291: aload 7
    //   1293: astore 5
    //   1295: ldc 15
    //   1297: aload_2
    //   1298: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1301: invokestatic 198	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   1304: pop
    //   1305: aload 7
    //   1307: astore 6
    //   1309: aload 7
    //   1311: astore 5
    //   1313: aload 13
    //   1315: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   1318: goto +441 -> 1759
    //   1321: iload 11
    //   1323: iconst_2
    //   1324: if_icmpne +217 -> 1541
    //   1327: aload 7
    //   1329: astore 6
    //   1331: aload 7
    //   1333: astore 5
    //   1335: ldc -10
    //   1337: aload_2
    //   1338: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1341: ifeq +200 -> 1541
    //   1344: aload_1
    //   1345: ifnull +196 -> 1541
    //   1348: aload 7
    //   1350: astore 6
    //   1352: aload 7
    //   1354: astore 5
    //   1356: aload 9
    //   1358: aload 10
    //   1360: getstatic 227	com/android/internal/R$styleable:AidFilter	[I
    //   1363: invokevirtual 121	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   1366: astore 12
    //   1368: aload 7
    //   1370: astore 6
    //   1372: aload 7
    //   1374: astore 5
    //   1376: aload 12
    //   1378: iconst_0
    //   1379: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   1382: invokevirtual 230	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   1385: ldc -8
    //   1387: invokevirtual 252	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
    //   1390: astore 13
    //   1392: aload 7
    //   1394: astore 6
    //   1396: aload 7
    //   1398: astore 5
    //   1400: aload 13
    //   1402: invokestatic 236	android/nfc/cardemulation/CardEmulation:isValidAid	(Ljava/lang/String;)Z
    //   1405: ifeq +48 -> 1453
    //   1408: aload 7
    //   1410: astore 6
    //   1412: aload 7
    //   1414: astore 5
    //   1416: aload_1
    //   1417: getfield 205	android/nfc/cardemulation/AidGroup:aids	Ljava/util/List;
    //   1420: aload 13
    //   1422: invokeinterface 239 2 0
    //   1427: ifne +26 -> 1453
    //   1430: aload 7
    //   1432: astore 6
    //   1434: aload 7
    //   1436: astore 5
    //   1438: aload_1
    //   1439: getfield 205	android/nfc/cardemulation/AidGroup:aids	Ljava/util/List;
    //   1442: aload 13
    //   1444: invokeinterface 242 2 0
    //   1449: pop
    //   1450: goto +75 -> 1525
    //   1453: aload 7
    //   1455: astore 6
    //   1457: aload 7
    //   1459: astore 5
    //   1461: new 180	java/lang/StringBuilder
    //   1464: astore_2
    //   1465: aload 7
    //   1467: astore 6
    //   1469: aload 7
    //   1471: astore 5
    //   1473: aload_2
    //   1474: invokespecial 181	java/lang/StringBuilder:<init>	()V
    //   1477: aload 7
    //   1479: astore 6
    //   1481: aload 7
    //   1483: astore 5
    //   1485: aload_2
    //   1486: ldc -12
    //   1488: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1491: pop
    //   1492: aload 7
    //   1494: astore 6
    //   1496: aload 7
    //   1498: astore 5
    //   1500: aload_2
    //   1501: aload 13
    //   1503: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1506: pop
    //   1507: aload 7
    //   1509: astore 6
    //   1511: aload 7
    //   1513: astore 5
    //   1515: ldc 15
    //   1517: aload_2
    //   1518: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1521: invokestatic 198	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   1524: pop
    //   1525: aload 7
    //   1527: astore 6
    //   1529: aload 7
    //   1531: astore 5
    //   1533: aload 12
    //   1535: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   1538: goto +221 -> 1759
    //   1541: iload 11
    //   1543: iconst_2
    //   1544: if_icmpne +215 -> 1759
    //   1547: aload 7
    //   1549: astore 6
    //   1551: aload 7
    //   1553: astore 5
    //   1555: aload_2
    //   1556: ldc -2
    //   1558: invokevirtual 87	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1561: ifeq +198 -> 1759
    //   1564: aload_1
    //   1565: ifnull +194 -> 1759
    //   1568: aload 7
    //   1570: astore 6
    //   1572: aload 7
    //   1574: astore 5
    //   1576: aload 9
    //   1578: aload 10
    //   1580: getstatic 227	com/android/internal/R$styleable:AidFilter	[I
    //   1583: invokevirtual 121	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   1586: astore 13
    //   1588: aload 7
    //   1590: astore 6
    //   1592: aload 7
    //   1594: astore 5
    //   1596: aload 13
    //   1598: iconst_0
    //   1599: invokevirtual 129	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   1602: invokevirtual 230	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   1605: ldc_w 256
    //   1608: invokevirtual 252	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
    //   1611: astore 12
    //   1613: aload 7
    //   1615: astore 6
    //   1617: aload 7
    //   1619: astore 5
    //   1621: aload 12
    //   1623: invokestatic 236	android/nfc/cardemulation/CardEmulation:isValidAid	(Ljava/lang/String;)Z
    //   1626: ifeq +48 -> 1674
    //   1629: aload 7
    //   1631: astore 6
    //   1633: aload 7
    //   1635: astore 5
    //   1637: aload_1
    //   1638: getfield 205	android/nfc/cardemulation/AidGroup:aids	Ljava/util/List;
    //   1641: aload 12
    //   1643: invokeinterface 239 2 0
    //   1648: ifne +26 -> 1674
    //   1651: aload 7
    //   1653: astore 6
    //   1655: aload 7
    //   1657: astore 5
    //   1659: aload_1
    //   1660: getfield 205	android/nfc/cardemulation/AidGroup:aids	Ljava/util/List;
    //   1663: aload 12
    //   1665: invokeinterface 242 2 0
    //   1670: pop
    //   1671: goto +75 -> 1746
    //   1674: aload 7
    //   1676: astore 6
    //   1678: aload 7
    //   1680: astore 5
    //   1682: new 180	java/lang/StringBuilder
    //   1685: astore_2
    //   1686: aload 7
    //   1688: astore 6
    //   1690: aload 7
    //   1692: astore 5
    //   1694: aload_2
    //   1695: invokespecial 181	java/lang/StringBuilder:<init>	()V
    //   1698: aload 7
    //   1700: astore 6
    //   1702: aload 7
    //   1704: astore 5
    //   1706: aload_2
    //   1707: ldc -12
    //   1709: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1712: pop
    //   1713: aload 7
    //   1715: astore 6
    //   1717: aload 7
    //   1719: astore 5
    //   1721: aload_2
    //   1722: aload 12
    //   1724: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1727: pop
    //   1728: aload 7
    //   1730: astore 6
    //   1732: aload 7
    //   1734: astore 5
    //   1736: ldc 15
    //   1738: aload_2
    //   1739: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1742: invokestatic 198	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   1745: pop
    //   1746: aload 7
    //   1748: astore 6
    //   1750: aload 7
    //   1752: astore 5
    //   1754: aload 13
    //   1756: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   1759: goto -1104 -> 655
    //   1762: aload 7
    //   1764: ifnull +10 -> 1774
    //   1767: aload 7
    //   1769: invokeinterface 259 1 0
    //   1774: aload_0
    //   1775: aload 4
    //   1777: getfield 97	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   1780: getfield 264	android/content/pm/ApplicationInfo:uid	I
    //   1783: putfield 266	android/nfc/cardemulation/ApduServiceInfo:mUid	I
    //   1786: return
    //   1787: aload 7
    //   1789: astore 6
    //   1791: aload 7
    //   1793: astore 5
    //   1795: new 40	org/xmlpull/v1/XmlPullParserException
    //   1798: astore_1
    //   1799: aload 7
    //   1801: astore 6
    //   1803: aload 7
    //   1805: astore 5
    //   1807: aload_1
    //   1808: ldc_w 268
    //   1811: invokespecial 64	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   1814: aload 7
    //   1816: astore 6
    //   1818: aload 7
    //   1820: astore 5
    //   1822: aload_1
    //   1823: athrow
    //   1824: aload 5
    //   1826: astore 6
    //   1828: new 40	org/xmlpull/v1/XmlPullParserException
    //   1831: astore_1
    //   1832: aload 5
    //   1834: astore 6
    //   1836: new 180	java/lang/StringBuilder
    //   1839: astore_2
    //   1840: aload 5
    //   1842: astore 6
    //   1844: aload_2
    //   1845: invokespecial 181	java/lang/StringBuilder:<init>	()V
    //   1848: aload 5
    //   1850: astore 6
    //   1852: aload_2
    //   1853: ldc_w 270
    //   1856: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1859: pop
    //   1860: aload 5
    //   1862: astore 6
    //   1864: aload_2
    //   1865: aload 4
    //   1867: getfield 273	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   1870: invokevirtual 187	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1873: pop
    //   1874: aload 5
    //   1876: astore 6
    //   1878: aload_1
    //   1879: aload_2
    //   1880: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1883: invokespecial 64	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   1886: aload 5
    //   1888: astore 6
    //   1890: aload_1
    //   1891: athrow
    //   1892: aload 6
    //   1894: ifnull +10 -> 1904
    //   1897: aload 6
    //   1899: invokeinterface 259 1 0
    //   1904: aload_1
    //   1905: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1906	0	this	ApduServiceInfo
    //   0	1906	1	paramPackageManager	PackageManager
    //   0	1906	2	paramResolveInfo	ResolveInfo
    //   0	1906	3	paramBoolean	boolean
    //   8	1858	4	localServiceInfo	ServiceInfo
    //   11	1876	5	localObject1	Object
    //   14	1884	6	localObject2	Object
    //   28	1791	7	localXmlResourceParser	android.content.res.XmlResourceParser
    //   112	584	8	i	int
    //   161	1416	9	localObject3	Object
    //   317	1262	10	localAttributeSet	android.util.AttributeSet
    //   670	875	11	j	int
    //   765	958	12	localObject4	Object
    //   796	959	13	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   20	30	74	finally
    //   46	50	74	finally
    //   58	64	74	finally
    //   72	74	74	finally
    //   82	92	74	finally
    //   105	114	74	finally
    //   134	143	74	finally
    //   154	163	74	finally
    //   175	185	74	finally
    //   196	200	74	finally
    //   208	214	74	finally
    //   222	224	74	finally
    //   236	246	74	finally
    //   257	261	74	finally
    //   269	275	74	finally
    //   283	285	74	finally
    //   293	304	74	finally
    //   312	319	74	finally
    //   331	342	74	finally
    //   350	355	74	finally
    //   363	372	74	finally
    //   380	390	74	finally
    //   398	408	74	finally
    //   416	425	74	finally
    //   433	437	74	finally
    //   448	459	74	finally
    //   467	472	74	finally
    //   480	489	74	finally
    //   497	502	74	finally
    //   510	520	74	finally
    //   528	537	74	finally
    //   545	549	74	finally
    //   557	561	74	finally
    //   569	573	74	finally
    //   581	586	74	finally
    //   594	598	74	finally
    //   606	610	74	finally
    //   618	623	74	finally
    //   631	636	74	finally
    //   644	653	74	finally
    //   663	672	74	finally
    //   686	698	74	finally
    //   712	720	74	finally
    //   734	743	74	finally
    //   755	767	74	finally
    //   775	782	74	finally
    //   790	798	74	finally
    //   808	817	74	finally
    //   828	840	74	finally
    //   852	861	74	finally
    //   869	873	74	finally
    //   881	885	74	finally
    //   893	900	74	finally
    //   908	914	74	finally
    //   922	929	74	finally
    //   937	947	74	finally
    //   965	976	74	finally
    //   984	989	74	finally
    //   1006	1015	74	finally
    //   1027	1039	74	finally
    //   1047	1061	74	finally
    //   1069	1082	74	finally
    //   1093	1101	74	finally
    //   1120	1129	74	finally
    //   1141	1153	74	finally
    //   1161	1172	74	finally
    //   1180	1188	74	finally
    //   1196	1210	74	finally
    //   1218	1230	74	finally
    //   1241	1245	74	finally
    //   1253	1257	74	finally
    //   1265	1272	74	finally
    //   1280	1287	74	finally
    //   1295	1305	74	finally
    //   1313	1318	74	finally
    //   1335	1344	74	finally
    //   1356	1368	74	finally
    //   1376	1392	74	finally
    //   1400	1408	74	finally
    //   1416	1430	74	finally
    //   1438	1450	74	finally
    //   1461	1465	74	finally
    //   1473	1477	74	finally
    //   1485	1492	74	finally
    //   1500	1507	74	finally
    //   1515	1525	74	finally
    //   1533	1538	74	finally
    //   1555	1564	74	finally
    //   1576	1588	74	finally
    //   1596	1613	74	finally
    //   1621	1629	74	finally
    //   1637	1651	74	finally
    //   1659	1671	74	finally
    //   1682	1686	74	finally
    //   1694	1698	74	finally
    //   1706	1713	74	finally
    //   1721	1728	74	finally
    //   1736	1746	74	finally
    //   1754	1759	74	finally
    //   1795	1799	74	finally
    //   1807	1814	74	finally
    //   1822	1824	74	finally
    //   1828	1832	74	finally
    //   1836	1840	74	finally
    //   1844	1848	74	finally
    //   1852	1860	74	finally
    //   1864	1874	74	finally
    //   1878	1886	74	finally
    //   1890	1892	74	finally
    //   20	30	78	android/content/pm/PackageManager$NameNotFoundException
    //   46	50	78	android/content/pm/PackageManager$NameNotFoundException
    //   58	64	78	android/content/pm/PackageManager$NameNotFoundException
    //   72	74	78	android/content/pm/PackageManager$NameNotFoundException
    //   82	92	78	android/content/pm/PackageManager$NameNotFoundException
    //   105	114	78	android/content/pm/PackageManager$NameNotFoundException
    //   134	143	78	android/content/pm/PackageManager$NameNotFoundException
    //   154	163	78	android/content/pm/PackageManager$NameNotFoundException
    //   175	185	78	android/content/pm/PackageManager$NameNotFoundException
    //   196	200	78	android/content/pm/PackageManager$NameNotFoundException
    //   208	214	78	android/content/pm/PackageManager$NameNotFoundException
    //   222	224	78	android/content/pm/PackageManager$NameNotFoundException
    //   236	246	78	android/content/pm/PackageManager$NameNotFoundException
    //   257	261	78	android/content/pm/PackageManager$NameNotFoundException
    //   269	275	78	android/content/pm/PackageManager$NameNotFoundException
    //   283	285	78	android/content/pm/PackageManager$NameNotFoundException
    //   293	304	78	android/content/pm/PackageManager$NameNotFoundException
    //   312	319	78	android/content/pm/PackageManager$NameNotFoundException
    //   331	342	78	android/content/pm/PackageManager$NameNotFoundException
    //   350	355	78	android/content/pm/PackageManager$NameNotFoundException
    //   363	372	78	android/content/pm/PackageManager$NameNotFoundException
    //   380	390	78	android/content/pm/PackageManager$NameNotFoundException
    //   398	408	78	android/content/pm/PackageManager$NameNotFoundException
    //   416	425	78	android/content/pm/PackageManager$NameNotFoundException
    //   433	437	78	android/content/pm/PackageManager$NameNotFoundException
    //   448	459	78	android/content/pm/PackageManager$NameNotFoundException
    //   467	472	78	android/content/pm/PackageManager$NameNotFoundException
    //   480	489	78	android/content/pm/PackageManager$NameNotFoundException
    //   497	502	78	android/content/pm/PackageManager$NameNotFoundException
    //   510	520	78	android/content/pm/PackageManager$NameNotFoundException
    //   528	537	78	android/content/pm/PackageManager$NameNotFoundException
    //   545	549	78	android/content/pm/PackageManager$NameNotFoundException
    //   557	561	78	android/content/pm/PackageManager$NameNotFoundException
    //   569	573	78	android/content/pm/PackageManager$NameNotFoundException
    //   581	586	78	android/content/pm/PackageManager$NameNotFoundException
    //   594	598	78	android/content/pm/PackageManager$NameNotFoundException
    //   606	610	78	android/content/pm/PackageManager$NameNotFoundException
    //   618	623	78	android/content/pm/PackageManager$NameNotFoundException
    //   631	636	78	android/content/pm/PackageManager$NameNotFoundException
    //   644	653	78	android/content/pm/PackageManager$NameNotFoundException
    //   663	672	78	android/content/pm/PackageManager$NameNotFoundException
    //   686	698	78	android/content/pm/PackageManager$NameNotFoundException
    //   712	720	78	android/content/pm/PackageManager$NameNotFoundException
    //   734	743	78	android/content/pm/PackageManager$NameNotFoundException
    //   755	767	78	android/content/pm/PackageManager$NameNotFoundException
    //   775	782	78	android/content/pm/PackageManager$NameNotFoundException
    //   790	798	78	android/content/pm/PackageManager$NameNotFoundException
    //   808	817	78	android/content/pm/PackageManager$NameNotFoundException
    //   828	840	78	android/content/pm/PackageManager$NameNotFoundException
    //   852	861	78	android/content/pm/PackageManager$NameNotFoundException
    //   869	873	78	android/content/pm/PackageManager$NameNotFoundException
    //   881	885	78	android/content/pm/PackageManager$NameNotFoundException
    //   893	900	78	android/content/pm/PackageManager$NameNotFoundException
    //   908	914	78	android/content/pm/PackageManager$NameNotFoundException
    //   922	929	78	android/content/pm/PackageManager$NameNotFoundException
    //   937	947	78	android/content/pm/PackageManager$NameNotFoundException
    //   965	976	78	android/content/pm/PackageManager$NameNotFoundException
    //   984	989	78	android/content/pm/PackageManager$NameNotFoundException
    //   1006	1015	78	android/content/pm/PackageManager$NameNotFoundException
    //   1027	1039	78	android/content/pm/PackageManager$NameNotFoundException
    //   1047	1061	78	android/content/pm/PackageManager$NameNotFoundException
    //   1069	1082	78	android/content/pm/PackageManager$NameNotFoundException
    //   1093	1101	78	android/content/pm/PackageManager$NameNotFoundException
    //   1120	1129	78	android/content/pm/PackageManager$NameNotFoundException
    //   1141	1153	78	android/content/pm/PackageManager$NameNotFoundException
    //   1161	1172	78	android/content/pm/PackageManager$NameNotFoundException
    //   1180	1188	78	android/content/pm/PackageManager$NameNotFoundException
    //   1196	1210	78	android/content/pm/PackageManager$NameNotFoundException
    //   1218	1230	78	android/content/pm/PackageManager$NameNotFoundException
    //   1241	1245	78	android/content/pm/PackageManager$NameNotFoundException
    //   1253	1257	78	android/content/pm/PackageManager$NameNotFoundException
    //   1265	1272	78	android/content/pm/PackageManager$NameNotFoundException
    //   1280	1287	78	android/content/pm/PackageManager$NameNotFoundException
    //   1295	1305	78	android/content/pm/PackageManager$NameNotFoundException
    //   1313	1318	78	android/content/pm/PackageManager$NameNotFoundException
    //   1335	1344	78	android/content/pm/PackageManager$NameNotFoundException
    //   1356	1368	78	android/content/pm/PackageManager$NameNotFoundException
    //   1376	1392	78	android/content/pm/PackageManager$NameNotFoundException
    //   1400	1408	78	android/content/pm/PackageManager$NameNotFoundException
    //   1416	1430	78	android/content/pm/PackageManager$NameNotFoundException
    //   1438	1450	78	android/content/pm/PackageManager$NameNotFoundException
    //   1461	1465	78	android/content/pm/PackageManager$NameNotFoundException
    //   1473	1477	78	android/content/pm/PackageManager$NameNotFoundException
    //   1485	1492	78	android/content/pm/PackageManager$NameNotFoundException
    //   1500	1507	78	android/content/pm/PackageManager$NameNotFoundException
    //   1515	1525	78	android/content/pm/PackageManager$NameNotFoundException
    //   1533	1538	78	android/content/pm/PackageManager$NameNotFoundException
    //   1555	1564	78	android/content/pm/PackageManager$NameNotFoundException
    //   1576	1588	78	android/content/pm/PackageManager$NameNotFoundException
    //   1596	1613	78	android/content/pm/PackageManager$NameNotFoundException
    //   1621	1629	78	android/content/pm/PackageManager$NameNotFoundException
    //   1637	1651	78	android/content/pm/PackageManager$NameNotFoundException
    //   1659	1671	78	android/content/pm/PackageManager$NameNotFoundException
    //   1682	1686	78	android/content/pm/PackageManager$NameNotFoundException
    //   1694	1698	78	android/content/pm/PackageManager$NameNotFoundException
    //   1706	1713	78	android/content/pm/PackageManager$NameNotFoundException
    //   1721	1728	78	android/content/pm/PackageManager$NameNotFoundException
    //   1736	1746	78	android/content/pm/PackageManager$NameNotFoundException
    //   1754	1759	78	android/content/pm/PackageManager$NameNotFoundException
    //   1795	1799	78	android/content/pm/PackageManager$NameNotFoundException
    //   1807	1814	78	android/content/pm/PackageManager$NameNotFoundException
    //   1822	1824	78	android/content/pm/PackageManager$NameNotFoundException
  }
  
  public ApduServiceInfo(ResolveInfo paramResolveInfo, boolean paramBoolean1, String paramString1, ArrayList<AidGroup> paramArrayList1, ArrayList<AidGroup> paramArrayList2, boolean paramBoolean2, int paramInt1, int paramInt2, String paramString2)
  {
    mService = paramResolveInfo;
    mDescription = paramString1;
    mStaticAidGroups = new HashMap();
    mDynamicAidGroups = new HashMap();
    mOnHost = paramBoolean1;
    mRequiresDeviceUnlock = paramBoolean2;
    paramString1 = paramArrayList1.iterator();
    while (paramString1.hasNext())
    {
      paramResolveInfo = (AidGroup)paramString1.next();
      mStaticAidGroups.put(category, paramResolveInfo);
    }
    paramResolveInfo = paramArrayList2.iterator();
    while (paramResolveInfo.hasNext())
    {
      paramString1 = (AidGroup)paramResolveInfo.next();
      mDynamicAidGroups.put(category, paramString1);
    }
    mBannerResourceId = paramInt1;
    mUid = paramInt2;
    mSettingsActivityName = paramString2;
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
    paramPrintWriter.println("    Static AID groups:");
    paramFileDescriptor = mStaticAidGroups.values().iterator();
    Object localObject1;
    Object localObject2;
    while (paramFileDescriptor.hasNext())
    {
      paramArrayOfString = (AidGroup)paramFileDescriptor.next();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("        Category: ");
      ((StringBuilder)localObject1).append(category);
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = aids.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        paramArrayOfString = (String)((Iterator)localObject1).next();
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("            AID: ");
        ((StringBuilder)localObject2).append(paramArrayOfString);
        paramPrintWriter.println(((StringBuilder)localObject2).toString());
      }
    }
    paramPrintWriter.println("    Dynamic AID groups:");
    paramFileDescriptor = mDynamicAidGroups.values().iterator();
    while (paramFileDescriptor.hasNext())
    {
      paramArrayOfString = (AidGroup)paramFileDescriptor.next();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("        Category: ");
      ((StringBuilder)localObject1).append(category);
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject2 = aids.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (String)((Iterator)localObject2).next();
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("            AID: ");
        paramArrayOfString.append((String)localObject1);
        paramPrintWriter.println(paramArrayOfString.toString());
      }
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("    Settings Activity: ");
    paramFileDescriptor.append(mSettingsActivityName);
    paramPrintWriter.println(paramFileDescriptor.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof ApduServiceInfo)) {
      return false;
    }
    return ((ApduServiceInfo)paramObject).getComponent().equals(getComponent());
  }
  
  public ArrayList<AidGroup> getAidGroups()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = mDynamicAidGroups.entrySet().iterator();
    while (localIterator.hasNext()) {
      localArrayList.add((AidGroup)((Map.Entry)localIterator.next()).getValue());
    }
    localIterator = mStaticAidGroups.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (!mDynamicAidGroups.containsKey(localEntry.getKey())) {
        localArrayList.add((AidGroup)localEntry.getValue());
      }
    }
    return localArrayList;
  }
  
  public List<String> getAids()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = getAidGroups().iterator();
    while (localIterator.hasNext()) {
      localArrayList.addAll(nextaids);
    }
    return localArrayList;
  }
  
  public String getCategoryForAid(String paramString)
  {
    Iterator localIterator = getAidGroups().iterator();
    while (localIterator.hasNext())
    {
      AidGroup localAidGroup = (AidGroup)localIterator.next();
      if (aids.contains(paramString.toUpperCase())) {
        return category;
      }
    }
    return null;
  }
  
  public ComponentName getComponent()
  {
    return new ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public AidGroup getDynamicAidGroupForCategory(String paramString)
  {
    return (AidGroup)mDynamicAidGroups.get(paramString);
  }
  
  public List<String> getPrefixAids()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator1 = getAidGroups().iterator();
    while (localIterator1.hasNext())
    {
      Iterator localIterator2 = nextaids.iterator();
      while (localIterator2.hasNext())
      {
        String str = (String)localIterator2.next();
        if (str.endsWith("*")) {
          localArrayList.add(str);
        }
      }
    }
    return localArrayList;
  }
  
  public String getSettingsActivityName()
  {
    return mSettingsActivityName;
  }
  
  public List<String> getSubsetAids()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator1 = getAidGroups().iterator();
    while (localIterator1.hasNext())
    {
      Iterator localIterator2 = nextaids.iterator();
      while (localIterator2.hasNext())
      {
        String str = (String)localIterator2.next();
        if (str.endsWith("#")) {
          localArrayList.add(str);
        }
      }
    }
    return localArrayList;
  }
  
  public int getUid()
  {
    return mUid;
  }
  
  public boolean hasCategory(String paramString)
  {
    boolean bool;
    if ((!mStaticAidGroups.containsKey(paramString)) && (!mDynamicAidGroups.containsKey(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return getComponent().hashCode();
  }
  
  public boolean isOnHost()
  {
    return mOnHost;
  }
  
  public CharSequence loadAppLabel(PackageManager paramPackageManager)
  {
    try
    {
      paramPackageManager = paramPackageManager.getApplicationLabel(paramPackageManager.getApplicationInfo(mService.resolvePackageName, 128));
      return paramPackageManager;
    }
    catch (PackageManager.NameNotFoundException paramPackageManager) {}
    return null;
  }
  
  public Drawable loadBanner(PackageManager paramPackageManager)
  {
    try
    {
      paramPackageManager = paramPackageManager.getResourcesForApplication(mService.serviceInfo.packageName).getDrawable(mBannerResourceId);
      return paramPackageManager;
    }
    catch (PackageManager.NameNotFoundException paramPackageManager)
    {
      Log.e("ApduServiceInfo", "Could not load banner.");
      return null;
    }
    catch (Resources.NotFoundException paramPackageManager)
    {
      Log.e("ApduServiceInfo", "Could not load banner.");
    }
    return null;
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    return mService.loadIcon(paramPackageManager);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    return mService.loadLabel(paramPackageManager);
  }
  
  public boolean removeDynamicAidGroupForCategory(String paramString)
  {
    boolean bool;
    if (mDynamicAidGroups.remove(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean requiresUnlock()
  {
    return mRequiresDeviceUnlock;
  }
  
  public void setOrReplaceDynamicAidGroup(AidGroup paramAidGroup)
  {
    mDynamicAidGroups.put(paramAidGroup.getCategory(), paramAidGroup);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("ApduService: ");
    localStringBuilder.append(getComponent());
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", description: ");
    ((StringBuilder)localObject).append(mDescription);
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localStringBuilder.append(", Static AID Groups: ");
    localObject = mStaticAidGroups.values().iterator();
    while (((Iterator)localObject).hasNext()) {
      localStringBuilder.append(((AidGroup)((Iterator)localObject).next()).toString());
    }
    localStringBuilder.append(", Dynamic AID Groups: ");
    localObject = mDynamicAidGroups.values().iterator();
    while (((Iterator)localObject).hasNext()) {
      localStringBuilder.append(((AidGroup)((Iterator)localObject).next()).toString());
    }
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mService.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mDescription);
    paramParcel.writeInt(mOnHost);
    paramParcel.writeInt(mStaticAidGroups.size());
    if (mStaticAidGroups.size() > 0) {
      paramParcel.writeTypedList(new ArrayList(mStaticAidGroups.values()));
    }
    paramParcel.writeInt(mDynamicAidGroups.size());
    if (mDynamicAidGroups.size() > 0) {
      paramParcel.writeTypedList(new ArrayList(mDynamicAidGroups.values()));
    }
    paramParcel.writeInt(mRequiresDeviceUnlock);
    paramParcel.writeInt(mBannerResourceId);
    paramParcel.writeInt(mUid);
    paramParcel.writeString(mSettingsActivityName);
  }
}
