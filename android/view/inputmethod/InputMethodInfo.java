package android.view.inputmethod;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
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
import android.util.Printer;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public final class InputMethodInfo
  implements Parcelable
{
  public static final Parcelable.Creator<InputMethodInfo> CREATOR = new Parcelable.Creator()
  {
    public InputMethodInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InputMethodInfo(paramAnonymousParcel);
    }
    
    public InputMethodInfo[] newArray(int paramAnonymousInt)
    {
      return new InputMethodInfo[paramAnonymousInt];
    }
  };
  static final String TAG = "InputMethodInfo";
  private final boolean mForceDefault;
  final String mId;
  private final boolean mIsAuxIme;
  final int mIsDefaultResId;
  final boolean mIsVrOnly;
  final ResolveInfo mService;
  final String mSettingsActivityName;
  private final InputMethodSubtypeArray mSubtypes;
  private final boolean mSupportsSwitchingToNextInputMethod;
  
  public InputMethodInfo(Context paramContext, ResolveInfo paramResolveInfo)
    throws XmlPullParserException, IOException
  {
    this(paramContext, paramResolveInfo, null);
  }
  
  /* Error */
  public InputMethodInfo(Context paramContext, ResolveInfo paramResolveInfo, List<InputMethodSubtype> paramList)
    throws XmlPullParserException, IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 52	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: aload_2
    //   6: putfield 54	android/view/inputmethod/InputMethodInfo:mService	Landroid/content/pm/ResolveInfo;
    //   9: aload_2
    //   10: getfield 60	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   13: astore 4
    //   15: aload_0
    //   16: aload_2
    //   17: invokestatic 64	android/view/inputmethod/InputMethodInfo:computeId	(Landroid/content/pm/ResolveInfo;)Ljava/lang/String;
    //   20: putfield 66	android/view/inputmethod/InputMethodInfo:mId	Ljava/lang/String;
    //   23: iconst_1
    //   24: istore 5
    //   26: aload_0
    //   27: iconst_0
    //   28: putfield 68	android/view/inputmethod/InputMethodInfo:mForceDefault	Z
    //   31: aload_1
    //   32: invokevirtual 74	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   35: astore 6
    //   37: aconst_null
    //   38: astore_1
    //   39: aconst_null
    //   40: astore 7
    //   42: new 76	java/util/ArrayList
    //   45: dup
    //   46: invokespecial 77	java/util/ArrayList:<init>	()V
    //   49: astore 8
    //   51: aload 4
    //   53: aload 6
    //   55: ldc 79
    //   57: invokevirtual 85	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   60: astore_2
    //   61: aload_2
    //   62: ifnull +629 -> 691
    //   65: aload_2
    //   66: astore 7
    //   68: aload_2
    //   69: astore_1
    //   70: aload 6
    //   72: aload 4
    //   74: getfield 89	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   77: invokevirtual 95	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   80: astore 9
    //   82: aload_2
    //   83: astore 7
    //   85: aload_2
    //   86: astore_1
    //   87: aload_2
    //   88: invokestatic 101	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   91: astore 10
    //   93: aload_2
    //   94: astore 7
    //   96: aload_2
    //   97: astore_1
    //   98: aload_2
    //   99: invokeinterface 107 1 0
    //   104: istore 11
    //   106: iload 11
    //   108: iconst_1
    //   109: if_icmpeq +12 -> 121
    //   112: iload 11
    //   114: iconst_2
    //   115: if_icmpeq +6 -> 121
    //   118: goto -25 -> 93
    //   121: aload_2
    //   122: astore 7
    //   124: aload_2
    //   125: astore_1
    //   126: ldc 109
    //   128: aload_2
    //   129: invokeinterface 113 1 0
    //   134: invokevirtual 119	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   137: ifeq +541 -> 678
    //   140: aload_2
    //   141: astore 7
    //   143: aload_2
    //   144: astore_1
    //   145: aload 9
    //   147: aload 10
    //   149: getstatic 125	com/android/internal/R$styleable:InputMethod	[I
    //   152: invokevirtual 131	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   155: astore 12
    //   157: aload_2
    //   158: astore 7
    //   160: aload_2
    //   161: astore_1
    //   162: aload 12
    //   164: iconst_1
    //   165: invokevirtual 137	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   168: astore 13
    //   170: aload 12
    //   172: iconst_3
    //   173: iconst_0
    //   174: invokevirtual 141	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   177: istore 14
    //   179: aload 12
    //   181: iconst_0
    //   182: iconst_0
    //   183: invokevirtual 145	android/content/res/TypedArray:getResourceId	(II)I
    //   186: istore 15
    //   188: aload 12
    //   190: iconst_2
    //   191: iconst_0
    //   192: invokevirtual 141	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   195: istore 16
    //   197: aload 12
    //   199: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   202: aload_2
    //   203: invokeinterface 151 1 0
    //   208: istore 11
    //   210: iconst_1
    //   211: istore 5
    //   213: aload 12
    //   215: astore 7
    //   217: aload_2
    //   218: invokeinterface 107 1 0
    //   223: istore 17
    //   225: iload 17
    //   227: iconst_3
    //   228: if_icmpne +32 -> 260
    //   231: aload_2
    //   232: invokeinterface 151 1 0
    //   237: istore 18
    //   239: iload 18
    //   241: iload 11
    //   243: if_icmple +6 -> 249
    //   246: goto +14 -> 260
    //   249: goto +233 -> 482
    //   252: astore_1
    //   253: goto +562 -> 815
    //   256: astore_1
    //   257: goto +468 -> 725
    //   260: iload 17
    //   262: iconst_1
    //   263: if_icmpeq +219 -> 482
    //   266: iload 17
    //   268: iconst_2
    //   269: if_icmpne +210 -> 479
    //   272: ldc -103
    //   274: aload_2
    //   275: invokeinterface 113 1 0
    //   280: invokevirtual 119	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   283: ifeq +168 -> 451
    //   286: aload 9
    //   288: aload 10
    //   290: getstatic 156	com/android/internal/R$styleable:InputMethod_Subtype	[I
    //   293: invokevirtual 131	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   296: astore 12
    //   298: new 158	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder
    //   301: astore_1
    //   302: aload_1
    //   303: invokespecial 159	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:<init>	()V
    //   306: aload_1
    //   307: aload 12
    //   309: iconst_0
    //   310: iconst_0
    //   311: invokevirtual 145	android/content/res/TypedArray:getResourceId	(II)I
    //   314: invokevirtual 163	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setSubtypeNameResId	(I)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   317: aload 12
    //   319: iconst_1
    //   320: iconst_0
    //   321: invokevirtual 145	android/content/res/TypedArray:getResourceId	(II)I
    //   324: invokevirtual 166	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setSubtypeIconResId	(I)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   327: aload 12
    //   329: bipush 9
    //   331: invokevirtual 137	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   334: invokevirtual 170	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setLanguageTag	(Ljava/lang/String;)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   337: aload 12
    //   339: iconst_2
    //   340: invokevirtual 137	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   343: invokevirtual 173	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setSubtypeLocale	(Ljava/lang/String;)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   346: aload 12
    //   348: iconst_3
    //   349: invokevirtual 137	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   352: invokevirtual 176	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setSubtypeMode	(Ljava/lang/String;)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   355: aload 12
    //   357: iconst_4
    //   358: invokevirtual 137	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   361: invokevirtual 179	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setSubtypeExtraValue	(Ljava/lang/String;)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   364: aload 12
    //   366: iconst_5
    //   367: iconst_0
    //   368: invokevirtual 141	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   371: invokevirtual 183	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setIsAuxiliary	(Z)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   374: aload 12
    //   376: bipush 6
    //   378: iconst_0
    //   379: invokevirtual 141	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   382: invokevirtual 186	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setOverridesImplicitlyEnabledSubtype	(Z)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   385: aload 12
    //   387: bipush 7
    //   389: iconst_0
    //   390: invokevirtual 189	android/content/res/TypedArray:getInt	(II)I
    //   393: invokevirtual 192	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setSubtypeId	(I)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   396: aload 12
    //   398: bipush 8
    //   400: iconst_0
    //   401: invokevirtual 141	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   404: invokevirtual 195	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:setIsAsciiCapable	(Z)Landroid/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder;
    //   407: invokevirtual 199	android/view/inputmethod/InputMethodSubtype$InputMethodSubtypeBuilder:build	()Landroid/view/inputmethod/InputMethodSubtype;
    //   410: astore 12
    //   412: aload 12
    //   414: invokevirtual 205	android/view/inputmethod/InputMethodSubtype:isAuxiliary	()Z
    //   417: istore 19
    //   419: iload 19
    //   421: ifne +9 -> 430
    //   424: iconst_0
    //   425: istore 5
    //   427: goto +3 -> 430
    //   430: iload 5
    //   432: istore 19
    //   434: aload_2
    //   435: astore_1
    //   436: aload 8
    //   438: aload 12
    //   440: invokevirtual 208	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   443: pop
    //   444: goto -227 -> 217
    //   447: astore_1
    //   448: goto +277 -> 725
    //   451: new 39	org/xmlpull/v1/XmlPullParserException
    //   454: astore_1
    //   455: aload_1
    //   456: ldc -46
    //   458: invokespecial 213	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   461: aload_1
    //   462: athrow
    //   463: astore_1
    //   464: goto +351 -> 815
    //   467: astore_1
    //   468: goto -211 -> 257
    //   471: astore_1
    //   472: goto +196 -> 668
    //   475: astore_1
    //   476: goto +199 -> 675
    //   479: goto -262 -> 217
    //   482: iconst_0
    //   483: istore 11
    //   485: aload_2
    //   486: ifnull +9 -> 495
    //   489: aload_2
    //   490: invokeinterface 216 1 0
    //   495: aload 8
    //   497: invokevirtual 219	java/util/ArrayList:size	()I
    //   500: ifne +9 -> 509
    //   503: iconst_0
    //   504: istore 5
    //   506: goto +3 -> 509
    //   509: aload_3
    //   510: ifnull +105 -> 615
    //   513: aload_3
    //   514: invokeinterface 222 1 0
    //   519: istore 17
    //   521: iload 11
    //   523: iload 17
    //   525: if_icmpge +90 -> 615
    //   528: aload_3
    //   529: iload 11
    //   531: invokeinterface 226 2 0
    //   536: checkcast 201	android/view/inputmethod/InputMethodSubtype
    //   539: astore_1
    //   540: aload 8
    //   542: aload_1
    //   543: invokevirtual 229	java/util/ArrayList:contains	(Ljava/lang/Object;)Z
    //   546: ifne +13 -> 559
    //   549: aload 8
    //   551: aload_1
    //   552: invokevirtual 208	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   555: pop
    //   556: goto +53 -> 609
    //   559: new 231	java/lang/StringBuilder
    //   562: dup
    //   563: invokespecial 232	java/lang/StringBuilder:<init>	()V
    //   566: astore_2
    //   567: aload_2
    //   568: ldc -22
    //   570: invokevirtual 238	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   573: pop
    //   574: aload_2
    //   575: aload_1
    //   576: invokevirtual 241	android/view/inputmethod/InputMethodSubtype:getLocale	()Ljava/lang/String;
    //   579: invokevirtual 238	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   582: pop
    //   583: aload_2
    //   584: ldc -13
    //   586: invokevirtual 238	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   589: pop
    //   590: aload_2
    //   591: aload_1
    //   592: invokevirtual 246	android/view/inputmethod/InputMethodSubtype:getMode	()Ljava/lang/String;
    //   595: invokevirtual 238	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   598: pop
    //   599: ldc 15
    //   601: aload_2
    //   602: invokevirtual 249	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   605: invokestatic 255	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   608: pop
    //   609: iinc 11 1
    //   612: goto -91 -> 521
    //   615: aload_0
    //   616: new 257	android/view/inputmethod/InputMethodSubtypeArray
    //   619: dup
    //   620: aload 8
    //   622: invokespecial 260	android/view/inputmethod/InputMethodSubtypeArray:<init>	(Ljava/util/List;)V
    //   625: putfield 262	android/view/inputmethod/InputMethodInfo:mSubtypes	Landroid/view/inputmethod/InputMethodSubtypeArray;
    //   628: aload_0
    //   629: aload 13
    //   631: putfield 264	android/view/inputmethod/InputMethodInfo:mSettingsActivityName	Ljava/lang/String;
    //   634: aload_0
    //   635: iload 15
    //   637: putfield 266	android/view/inputmethod/InputMethodInfo:mIsDefaultResId	I
    //   640: aload_0
    //   641: iload 5
    //   643: putfield 268	android/view/inputmethod/InputMethodInfo:mIsAuxIme	Z
    //   646: aload_0
    //   647: iload 16
    //   649: putfield 270	android/view/inputmethod/InputMethodInfo:mSupportsSwitchingToNextInputMethod	Z
    //   652: aload_0
    //   653: iload 14
    //   655: putfield 272	android/view/inputmethod/InputMethodInfo:mIsVrOnly	Z
    //   658: return
    //   659: astore_1
    //   660: goto +155 -> 815
    //   663: astore_1
    //   664: goto +61 -> 725
    //   667: astore_1
    //   668: goto +147 -> 815
    //   671: astore_1
    //   672: iconst_1
    //   673: istore 5
    //   675: goto +50 -> 725
    //   678: new 39	org/xmlpull/v1/XmlPullParserException
    //   681: astore_1
    //   682: aload_1
    //   683: ldc_w 274
    //   686: invokespecial 213	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   689: aload_1
    //   690: athrow
    //   691: new 39	org/xmlpull/v1/XmlPullParserException
    //   694: astore_1
    //   695: aload_1
    //   696: ldc_w 276
    //   699: invokespecial 213	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   702: aload_1
    //   703: athrow
    //   704: astore_1
    //   705: goto +110 -> 815
    //   708: astore_1
    //   709: iconst_1
    //   710: istore 5
    //   712: goto +13 -> 725
    //   715: astore_1
    //   716: aload 7
    //   718: astore_2
    //   719: goto +96 -> 815
    //   722: astore_2
    //   723: aload_1
    //   724: astore_2
    //   725: iload 5
    //   727: istore 19
    //   729: aload_2
    //   730: astore_1
    //   731: new 39	org/xmlpull/v1/XmlPullParserException
    //   734: astore 7
    //   736: iload 5
    //   738: istore 19
    //   740: aload_2
    //   741: astore_1
    //   742: new 231	java/lang/StringBuilder
    //   745: astore_3
    //   746: iload 5
    //   748: istore 19
    //   750: aload_2
    //   751: astore_1
    //   752: aload_3
    //   753: invokespecial 232	java/lang/StringBuilder:<init>	()V
    //   756: iload 5
    //   758: istore 19
    //   760: aload_2
    //   761: astore_1
    //   762: aload_3
    //   763: ldc_w 278
    //   766: invokevirtual 238	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   769: pop
    //   770: iload 5
    //   772: istore 19
    //   774: aload_2
    //   775: astore_1
    //   776: aload_3
    //   777: aload 4
    //   779: getfield 281	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   782: invokevirtual 238	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   785: pop
    //   786: iload 5
    //   788: istore 19
    //   790: aload_2
    //   791: astore_1
    //   792: aload 7
    //   794: aload_3
    //   795: invokevirtual 249	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   798: invokespecial 213	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   801: iload 5
    //   803: istore 19
    //   805: aload_2
    //   806: astore_1
    //   807: aload 7
    //   809: athrow
    //   810: astore_3
    //   811: aload_1
    //   812: astore_2
    //   813: aload_3
    //   814: astore_1
    //   815: aload_2
    //   816: ifnull +9 -> 825
    //   819: aload_2
    //   820: invokeinterface 216 1 0
    //   825: aload_1
    //   826: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	827	0	this	InputMethodInfo
    //   0	827	1	paramContext	Context
    //   0	827	2	paramResolveInfo	ResolveInfo
    //   0	827	3	paramList	List<InputMethodSubtype>
    //   13	765	4	localServiceInfo	ServiceInfo
    //   24	778	5	bool1	boolean
    //   35	36	6	localPackageManager	PackageManager
    //   40	768	7	localObject1	Object
    //   49	572	8	localArrayList	java.util.ArrayList
    //   80	207	9	localResources	Resources
    //   91	198	10	localAttributeSet	android.util.AttributeSet
    //   104	506	11	i	int
    //   155	284	12	localObject2	Object
    //   168	462	13	str	String
    //   177	477	14	bool2	boolean
    //   186	450	15	j	int
    //   195	453	16	bool3	boolean
    //   223	303	17	k	int
    //   237	7	18	m	int
    //   417	387	19	bool4	boolean
    // Exception table:
    //   from	to	target	type
    //   231	239	252	finally
    //   231	239	256	android/content/pm/PackageManager$NameNotFoundException
    //   231	239	256	java/lang/IndexOutOfBoundsException
    //   231	239	256	java/lang/NumberFormatException
    //   436	444	447	android/content/pm/PackageManager$NameNotFoundException
    //   436	444	447	java/lang/IndexOutOfBoundsException
    //   436	444	447	java/lang/NumberFormatException
    //   306	419	463	finally
    //   451	463	463	finally
    //   306	419	467	android/content/pm/PackageManager$NameNotFoundException
    //   306	419	467	java/lang/IndexOutOfBoundsException
    //   306	419	467	java/lang/NumberFormatException
    //   451	463	467	android/content/pm/PackageManager$NameNotFoundException
    //   451	463	467	java/lang/IndexOutOfBoundsException
    //   451	463	467	java/lang/NumberFormatException
    //   272	306	471	finally
    //   272	306	475	android/content/pm/PackageManager$NameNotFoundException
    //   272	306	475	java/lang/IndexOutOfBoundsException
    //   272	306	475	java/lang/NumberFormatException
    //   217	225	659	finally
    //   217	225	663	android/content/pm/PackageManager$NameNotFoundException
    //   217	225	663	java/lang/IndexOutOfBoundsException
    //   217	225	663	java/lang/NumberFormatException
    //   170	210	667	finally
    //   170	210	671	android/content/pm/PackageManager$NameNotFoundException
    //   170	210	671	java/lang/IndexOutOfBoundsException
    //   170	210	671	java/lang/NumberFormatException
    //   678	691	704	finally
    //   691	704	704	finally
    //   678	691	708	android/content/pm/PackageManager$NameNotFoundException
    //   678	691	708	java/lang/IndexOutOfBoundsException
    //   678	691	708	java/lang/NumberFormatException
    //   691	704	708	android/content/pm/PackageManager$NameNotFoundException
    //   691	704	708	java/lang/IndexOutOfBoundsException
    //   691	704	708	java/lang/NumberFormatException
    //   51	61	715	finally
    //   70	82	715	finally
    //   87	93	715	finally
    //   98	106	715	finally
    //   126	140	715	finally
    //   145	157	715	finally
    //   162	170	715	finally
    //   51	61	722	android/content/pm/PackageManager$NameNotFoundException
    //   51	61	722	java/lang/IndexOutOfBoundsException
    //   51	61	722	java/lang/NumberFormatException
    //   70	82	722	android/content/pm/PackageManager$NameNotFoundException
    //   70	82	722	java/lang/IndexOutOfBoundsException
    //   70	82	722	java/lang/NumberFormatException
    //   87	93	722	android/content/pm/PackageManager$NameNotFoundException
    //   87	93	722	java/lang/IndexOutOfBoundsException
    //   87	93	722	java/lang/NumberFormatException
    //   98	106	722	android/content/pm/PackageManager$NameNotFoundException
    //   98	106	722	java/lang/IndexOutOfBoundsException
    //   98	106	722	java/lang/NumberFormatException
    //   126	140	722	android/content/pm/PackageManager$NameNotFoundException
    //   126	140	722	java/lang/IndexOutOfBoundsException
    //   126	140	722	java/lang/NumberFormatException
    //   145	157	722	android/content/pm/PackageManager$NameNotFoundException
    //   145	157	722	java/lang/IndexOutOfBoundsException
    //   145	157	722	java/lang/NumberFormatException
    //   162	170	722	android/content/pm/PackageManager$NameNotFoundException
    //   162	170	722	java/lang/IndexOutOfBoundsException
    //   162	170	722	java/lang/NumberFormatException
    //   436	444	810	finally
    //   731	736	810	finally
    //   742	746	810	finally
    //   752	756	810	finally
    //   762	770	810	finally
    //   776	786	810	finally
    //   792	801	810	finally
    //   807	810	810	finally
  }
  
  public InputMethodInfo(ResolveInfo paramResolveInfo, boolean paramBoolean1, String paramString, List<InputMethodSubtype> paramList, int paramInt, boolean paramBoolean2)
  {
    this(paramResolveInfo, paramBoolean1, paramString, paramList, paramInt, paramBoolean2, true, false);
  }
  
  public InputMethodInfo(ResolveInfo paramResolveInfo, boolean paramBoolean1, String paramString, List<InputMethodSubtype> paramList, int paramInt, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    ServiceInfo localServiceInfo = serviceInfo;
    mService = paramResolveInfo;
    mId = new ComponentName(packageName, name).flattenToShortString();
    mSettingsActivityName = paramString;
    mIsDefaultResId = paramInt;
    mIsAuxIme = paramBoolean1;
    mSubtypes = new InputMethodSubtypeArray(paramList);
    mForceDefault = paramBoolean2;
    mSupportsSwitchingToNextInputMethod = paramBoolean3;
    mIsVrOnly = paramBoolean4;
  }
  
  InputMethodInfo(Parcel paramParcel)
  {
    mId = paramParcel.readString();
    mSettingsActivityName = paramParcel.readString();
    mIsDefaultResId = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = true;
    boolean bool2;
    if (i == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsAuxIme = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    mSupportsSwitchingToNextInputMethod = bool2;
    mIsVrOnly = paramParcel.readBoolean();
    mService = ((ResolveInfo)ResolveInfo.CREATOR.createFromParcel(paramParcel));
    mSubtypes = new InputMethodSubtypeArray(paramParcel);
    mForceDefault = false;
  }
  
  public InputMethodInfo(String paramString1, String paramString2, CharSequence paramCharSequence, String paramString3)
  {
    this(buildDummyResolveInfo(paramString1, paramString2, paramCharSequence), false, paramString3, null, 0, false, true, false);
  }
  
  private static ResolveInfo buildDummyResolveInfo(String paramString1, String paramString2, CharSequence paramCharSequence)
  {
    ResolveInfo localResolveInfo = new ResolveInfo();
    ServiceInfo localServiceInfo = new ServiceInfo();
    ApplicationInfo localApplicationInfo = new ApplicationInfo();
    packageName = paramString1;
    enabled = true;
    applicationInfo = localApplicationInfo;
    enabled = true;
    packageName = paramString1;
    name = paramString2;
    exported = true;
    nonLocalizedLabel = paramCharSequence;
    serviceInfo = localServiceInfo;
    return localResolveInfo;
  }
  
  public static String computeId(ResolveInfo paramResolveInfo)
  {
    paramResolveInfo = serviceInfo;
    return new ComponentName(packageName, name).flattenToShortString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("mId=");
    localStringBuilder.append(mId);
    localStringBuilder.append(" mSettingsActivityName=");
    localStringBuilder.append(mSettingsActivityName);
    localStringBuilder.append(" mIsVrOnly=");
    localStringBuilder.append(mIsVrOnly);
    localStringBuilder.append(" mSupportsSwitchingToNextInputMethod=");
    localStringBuilder.append(mSupportsSwitchingToNextInputMethod);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("mIsDefaultResId=0x");
    localStringBuilder.append(Integer.toHexString(mIsDefaultResId));
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("Service:");
    paramPrinter.println(localStringBuilder.toString());
    ResolveInfo localResolveInfo = mService;
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  ");
    localResolveInfo.dump(paramPrinter, localStringBuilder.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof InputMethodInfo)) {
      return false;
    }
    paramObject = (InputMethodInfo)paramObject;
    return mId.equals(mId);
  }
  
  public ComponentName getComponent()
  {
    return new ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
  }
  
  public String getId()
  {
    return mId;
  }
  
  public int getIsDefaultResourceId()
  {
    return mIsDefaultResId;
  }
  
  public String getPackageName()
  {
    return mService.serviceInfo.packageName;
  }
  
  public ServiceInfo getServiceInfo()
  {
    return mService.serviceInfo;
  }
  
  public String getServiceName()
  {
    return mService.serviceInfo.name;
  }
  
  public String getSettingsActivity()
  {
    return mSettingsActivityName;
  }
  
  public InputMethodSubtype getSubtypeAt(int paramInt)
  {
    return mSubtypes.get(paramInt);
  }
  
  public int getSubtypeCount()
  {
    return mSubtypes.getCount();
  }
  
  public int hashCode()
  {
    return mId.hashCode();
  }
  
  public boolean isAuxiliaryIme()
  {
    return mIsAuxIme;
  }
  
  public boolean isDefault(Context paramContext)
  {
    if (mForceDefault) {
      return true;
    }
    try
    {
      if (getIsDefaultResourceId() == 0) {
        return false;
      }
      boolean bool = paramContext.createPackageContext(getPackageName(), 0).getResources().getBoolean(getIsDefaultResourceId());
      return bool;
    }
    catch (PackageManager.NameNotFoundException|Resources.NotFoundException paramContext) {}
    return false;
  }
  
  public boolean isVrOnly()
  {
    return mIsVrOnly;
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    return mService.loadIcon(paramPackageManager);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    return mService.loadLabel(paramPackageManager);
  }
  
  public boolean supportsSwitchingToNextInputMethod()
  {
    return mSupportsSwitchingToNextInputMethod;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("InputMethodInfo{");
    localStringBuilder.append(mId);
    localStringBuilder.append(", settings: ");
    localStringBuilder.append(mSettingsActivityName);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeString(mSettingsActivityName);
    paramParcel.writeInt(mIsDefaultResId);
    paramParcel.writeInt(mIsAuxIme);
    paramParcel.writeInt(mSupportsSwitchingToNextInputMethod);
    paramParcel.writeBoolean(mIsVrOnly);
    mService.writeToParcel(paramParcel, paramInt);
    mSubtypes.writeToParcel(paramParcel);
  }
}
