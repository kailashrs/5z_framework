package android.view.textservice;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class SpellCheckerInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SpellCheckerInfo> CREATOR = new Parcelable.Creator()
  {
    public SpellCheckerInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SpellCheckerInfo(paramAnonymousParcel);
    }
    
    public SpellCheckerInfo[] newArray(int paramAnonymousInt)
    {
      return new SpellCheckerInfo[paramAnonymousInt];
    }
  };
  private static final String TAG = SpellCheckerInfo.class.getSimpleName();
  private final String mId;
  private final int mLabel;
  private final ResolveInfo mService;
  private final String mSettingsActivityName;
  private final ArrayList<SpellCheckerSubtype> mSubtypes = new ArrayList();
  
  /* Error */
  public SpellCheckerInfo(android.content.Context paramContext, ResolveInfo paramResolveInfo)
    throws org.xmlpull.v1.XmlPullParserException, java.io.IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 46	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: new 48	java/util/ArrayList
    //   8: dup
    //   9: invokespecial 49	java/util/ArrayList:<init>	()V
    //   12: putfield 51	android/view/textservice/SpellCheckerInfo:mSubtypes	Ljava/util/ArrayList;
    //   15: aload_0
    //   16: aload_2
    //   17: putfield 53	android/view/textservice/SpellCheckerInfo:mService	Landroid/content/pm/ResolveInfo;
    //   20: aload_2
    //   21: getfield 59	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   24: astore_3
    //   25: aload_0
    //   26: new 61	android/content/ComponentName
    //   29: dup
    //   30: aload_3
    //   31: getfield 66	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   34: aload_3
    //   35: getfield 69	android/content/pm/ServiceInfo:name	Ljava/lang/String;
    //   38: invokespecial 72	android/content/ComponentName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   41: invokevirtual 75	android/content/ComponentName:flattenToShortString	()Ljava/lang/String;
    //   44: putfield 77	android/view/textservice/SpellCheckerInfo:mId	Ljava/lang/String;
    //   47: aload_1
    //   48: invokevirtual 83	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   51: astore 4
    //   53: aconst_null
    //   54: astore_2
    //   55: aconst_null
    //   56: astore_1
    //   57: aload_3
    //   58: aload 4
    //   60: ldc 85
    //   62: invokevirtual 89	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   65: astore 5
    //   67: aload 5
    //   69: ifnull +414 -> 483
    //   72: aload 5
    //   74: astore_1
    //   75: aload 5
    //   77: astore_2
    //   78: aload 4
    //   80: aload_3
    //   81: getfield 93	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   84: invokevirtual 99	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   87: astore 4
    //   89: aload 5
    //   91: astore_1
    //   92: aload 5
    //   94: astore_2
    //   95: aload 5
    //   97: invokestatic 105	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   100: astore 6
    //   102: aload 5
    //   104: astore_1
    //   105: aload 5
    //   107: astore_2
    //   108: aload 5
    //   110: invokeinterface 111 1 0
    //   115: istore 7
    //   117: iload 7
    //   119: iconst_1
    //   120: if_icmpeq +12 -> 132
    //   123: iload 7
    //   125: iconst_2
    //   126: if_icmpeq +6 -> 132
    //   129: goto -27 -> 102
    //   132: aload 5
    //   134: astore_1
    //   135: aload 5
    //   137: astore_2
    //   138: ldc 113
    //   140: aload 5
    //   142: invokeinterface 116 1 0
    //   147: invokevirtual 122	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   150: ifeq +300 -> 450
    //   153: aload 5
    //   155: astore_1
    //   156: aload 5
    //   158: astore_2
    //   159: aload 4
    //   161: aload 6
    //   163: getstatic 128	com/android/internal/R$styleable:SpellChecker	[I
    //   166: invokevirtual 134	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   169: astore 8
    //   171: aload 5
    //   173: astore_1
    //   174: aload 5
    //   176: astore_2
    //   177: aload 8
    //   179: iconst_0
    //   180: iconst_0
    //   181: invokevirtual 140	android/content/res/TypedArray:getResourceId	(II)I
    //   184: istore 9
    //   186: aload 5
    //   188: astore_1
    //   189: aload 5
    //   191: astore_2
    //   192: aload 8
    //   194: iconst_1
    //   195: invokevirtual 144	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   198: astore 10
    //   200: aload 5
    //   202: astore_1
    //   203: aload 5
    //   205: astore_2
    //   206: aload 8
    //   208: invokevirtual 147	android/content/res/TypedArray:recycle	()V
    //   211: aload 5
    //   213: astore_1
    //   214: aload 5
    //   216: astore_2
    //   217: aload 5
    //   219: invokeinterface 150 1 0
    //   224: istore 7
    //   226: aload 5
    //   228: astore_1
    //   229: aload 5
    //   231: astore_2
    //   232: aload 5
    //   234: invokeinterface 111 1 0
    //   239: istore 11
    //   241: iload 11
    //   243: iconst_3
    //   244: if_icmpne +21 -> 265
    //   247: aload 5
    //   249: astore_1
    //   250: aload 5
    //   252: astore_2
    //   253: aload 5
    //   255: invokeinterface 150 1 0
    //   260: iload 7
    //   262: if_icmple +163 -> 425
    //   265: iload 11
    //   267: iconst_1
    //   268: if_icmpeq +157 -> 425
    //   271: iload 11
    //   273: iconst_2
    //   274: if_icmpne +148 -> 422
    //   277: aload 5
    //   279: astore_1
    //   280: aload 5
    //   282: astore_2
    //   283: ldc -104
    //   285: aload 5
    //   287: invokeinterface 116 1 0
    //   292: invokevirtual 122	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   295: ifeq +94 -> 389
    //   298: aload 5
    //   300: astore_1
    //   301: aload 5
    //   303: astore_2
    //   304: aload 4
    //   306: aload 6
    //   308: getstatic 155	com/android/internal/R$styleable:SpellChecker_Subtype	[I
    //   311: invokevirtual 134	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   314: astore 8
    //   316: aload 5
    //   318: astore_1
    //   319: aload 5
    //   321: astore_2
    //   322: new 157	android/view/textservice/SpellCheckerSubtype
    //   325: astore 12
    //   327: aload 5
    //   329: astore_1
    //   330: aload 5
    //   332: astore_2
    //   333: aload 12
    //   335: aload 8
    //   337: iconst_0
    //   338: iconst_0
    //   339: invokevirtual 140	android/content/res/TypedArray:getResourceId	(II)I
    //   342: aload 8
    //   344: iconst_1
    //   345: invokevirtual 144	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   348: aload 8
    //   350: iconst_4
    //   351: invokevirtual 144	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   354: aload 8
    //   356: iconst_2
    //   357: invokevirtual 144	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   360: aload 8
    //   362: iconst_3
    //   363: iconst_0
    //   364: invokevirtual 160	android/content/res/TypedArray:getInt	(II)I
    //   367: invokespecial 163	android/view/textservice/SpellCheckerSubtype:<init>	(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
    //   370: aload 5
    //   372: astore_1
    //   373: aload 5
    //   375: astore_2
    //   376: aload_0
    //   377: getfield 51	android/view/textservice/SpellCheckerInfo:mSubtypes	Ljava/util/ArrayList;
    //   380: aload 12
    //   382: invokevirtual 166	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   385: pop
    //   386: goto +36 -> 422
    //   389: aload 5
    //   391: astore_1
    //   392: aload 5
    //   394: astore_2
    //   395: new 41	org/xmlpull/v1/XmlPullParserException
    //   398: astore 4
    //   400: aload 5
    //   402: astore_1
    //   403: aload 5
    //   405: astore_2
    //   406: aload 4
    //   408: ldc -88
    //   410: invokespecial 171	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   413: aload 5
    //   415: astore_1
    //   416: aload 5
    //   418: astore_2
    //   419: aload 4
    //   421: athrow
    //   422: goto -196 -> 226
    //   425: aload 5
    //   427: ifnull +10 -> 437
    //   430: aload 5
    //   432: invokeinterface 174 1 0
    //   437: aload_0
    //   438: iload 9
    //   440: putfield 176	android/view/textservice/SpellCheckerInfo:mLabel	I
    //   443: aload_0
    //   444: aload 10
    //   446: putfield 178	android/view/textservice/SpellCheckerInfo:mSettingsActivityName	Ljava/lang/String;
    //   449: return
    //   450: aload 5
    //   452: astore_1
    //   453: aload 5
    //   455: astore_2
    //   456: new 41	org/xmlpull/v1/XmlPullParserException
    //   459: astore 4
    //   461: aload 5
    //   463: astore_1
    //   464: aload 5
    //   466: astore_2
    //   467: aload 4
    //   469: ldc -76
    //   471: invokespecial 171	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   474: aload 5
    //   476: astore_1
    //   477: aload 5
    //   479: astore_2
    //   480: aload 4
    //   482: athrow
    //   483: aload 5
    //   485: astore_1
    //   486: aload 5
    //   488: astore_2
    //   489: new 41	org/xmlpull/v1/XmlPullParserException
    //   492: astore 4
    //   494: aload 5
    //   496: astore_1
    //   497: aload 5
    //   499: astore_2
    //   500: aload 4
    //   502: ldc -74
    //   504: invokespecial 171	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   507: aload 5
    //   509: astore_1
    //   510: aload 5
    //   512: astore_2
    //   513: aload 4
    //   515: athrow
    //   516: astore_2
    //   517: goto +119 -> 636
    //   520: astore 4
    //   522: aload_2
    //   523: astore_1
    //   524: getstatic 32	android/view/textservice/SpellCheckerInfo:TAG	Ljava/lang/String;
    //   527: astore 5
    //   529: aload_2
    //   530: astore_1
    //   531: new 184	java/lang/StringBuilder
    //   534: astore 10
    //   536: aload_2
    //   537: astore_1
    //   538: aload 10
    //   540: invokespecial 185	java/lang/StringBuilder:<init>	()V
    //   543: aload_2
    //   544: astore_1
    //   545: aload 10
    //   547: ldc -69
    //   549: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   552: pop
    //   553: aload_2
    //   554: astore_1
    //   555: aload 10
    //   557: aload 4
    //   559: invokevirtual 194	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   562: pop
    //   563: aload_2
    //   564: astore_1
    //   565: aload 5
    //   567: aload 10
    //   569: invokevirtual 197	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   572: invokestatic 203	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   575: pop
    //   576: aload_2
    //   577: astore_1
    //   578: new 41	org/xmlpull/v1/XmlPullParserException
    //   581: astore 5
    //   583: aload_2
    //   584: astore_1
    //   585: new 184	java/lang/StringBuilder
    //   588: astore 4
    //   590: aload_2
    //   591: astore_1
    //   592: aload 4
    //   594: invokespecial 185	java/lang/StringBuilder:<init>	()V
    //   597: aload_2
    //   598: astore_1
    //   599: aload 4
    //   601: ldc -51
    //   603: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   606: pop
    //   607: aload_2
    //   608: astore_1
    //   609: aload 4
    //   611: aload_3
    //   612: getfield 66	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   615: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   618: pop
    //   619: aload_2
    //   620: astore_1
    //   621: aload 5
    //   623: aload 4
    //   625: invokevirtual 197	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   628: invokespecial 171	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   631: aload_2
    //   632: astore_1
    //   633: aload 5
    //   635: athrow
    //   636: aload_1
    //   637: ifnull +9 -> 646
    //   640: aload_1
    //   641: invokeinterface 174 1 0
    //   646: aload_2
    //   647: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	648	0	this	SpellCheckerInfo
    //   0	648	1	paramContext	android.content.Context
    //   0	648	2	paramResolveInfo	ResolveInfo
    //   24	588	3	localServiceInfo	ServiceInfo
    //   51	463	4	localObject1	Object
    //   520	38	4	localException	Exception
    //   588	36	4	localStringBuilder	StringBuilder
    //   65	569	5	localObject2	Object
    //   100	207	6	localAttributeSet	android.util.AttributeSet
    //   115	148	7	i	int
    //   169	192	8	localTypedArray	android.content.res.TypedArray
    //   184	255	9	j	int
    //   198	370	10	localObject3	Object
    //   239	36	11	k	int
    //   325	56	12	localSpellCheckerSubtype	SpellCheckerSubtype
    // Exception table:
    //   from	to	target	type
    //   57	67	516	finally
    //   78	89	516	finally
    //   95	102	516	finally
    //   108	117	516	finally
    //   138	153	516	finally
    //   159	171	516	finally
    //   177	186	516	finally
    //   192	200	516	finally
    //   206	211	516	finally
    //   217	226	516	finally
    //   232	241	516	finally
    //   253	265	516	finally
    //   283	298	516	finally
    //   304	316	516	finally
    //   322	327	516	finally
    //   333	370	516	finally
    //   376	386	516	finally
    //   395	400	516	finally
    //   406	413	516	finally
    //   419	422	516	finally
    //   456	461	516	finally
    //   467	474	516	finally
    //   480	483	516	finally
    //   489	494	516	finally
    //   500	507	516	finally
    //   513	516	516	finally
    //   524	529	516	finally
    //   531	536	516	finally
    //   538	543	516	finally
    //   545	553	516	finally
    //   555	563	516	finally
    //   565	576	516	finally
    //   578	583	516	finally
    //   585	590	516	finally
    //   592	597	516	finally
    //   599	607	516	finally
    //   609	619	516	finally
    //   621	631	516	finally
    //   633	636	516	finally
    //   57	67	520	java/lang/Exception
    //   78	89	520	java/lang/Exception
    //   95	102	520	java/lang/Exception
    //   108	117	520	java/lang/Exception
    //   138	153	520	java/lang/Exception
    //   159	171	520	java/lang/Exception
    //   177	186	520	java/lang/Exception
    //   192	200	520	java/lang/Exception
    //   206	211	520	java/lang/Exception
    //   217	226	520	java/lang/Exception
    //   232	241	520	java/lang/Exception
    //   253	265	520	java/lang/Exception
    //   283	298	520	java/lang/Exception
    //   304	316	520	java/lang/Exception
    //   322	327	520	java/lang/Exception
    //   333	370	520	java/lang/Exception
    //   376	386	520	java/lang/Exception
    //   395	400	520	java/lang/Exception
    //   406	413	520	java/lang/Exception
    //   419	422	520	java/lang/Exception
    //   456	461	520	java/lang/Exception
    //   467	474	520	java/lang/Exception
    //   480	483	520	java/lang/Exception
    //   489	494	520	java/lang/Exception
    //   500	507	520	java/lang/Exception
    //   513	516	520	java/lang/Exception
  }
  
  public SpellCheckerInfo(Parcel paramParcel)
  {
    mLabel = paramParcel.readInt();
    mId = paramParcel.readString();
    mSettingsActivityName = paramParcel.readString();
    mService = ((ResolveInfo)ResolveInfo.CREATOR.createFromParcel(paramParcel));
    paramParcel.readTypedList(mSubtypes, SpellCheckerSubtype.CREATOR);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append("mId=");
    ((StringBuilder)localObject1).append(mId);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append("mSettingsActivityName=");
    ((StringBuilder)localObject1).append(mSettingsActivityName);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append("Service:");
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    Object localObject2 = mService;
    localObject1 = new PrintWriterPrinter(paramPrintWriter);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  ");
    ((ResolveInfo)localObject2).dump((Printer)localObject1, localStringBuilder.toString());
    int i = getSubtypeCount();
    for (int j = 0; j < i; j++)
    {
      localObject1 = getSubtypeAt(j);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  Subtype #");
      ((StringBuilder)localObject2).append(j);
      ((StringBuilder)localObject2).append(":");
      paramPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("    locale=");
      ((StringBuilder)localObject2).append(((SpellCheckerSubtype)localObject1).getLocale());
      ((StringBuilder)localObject2).append(" languageTag=");
      ((StringBuilder)localObject2).append(((SpellCheckerSubtype)localObject1).getLanguageTag());
      paramPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("    extraValue=");
      ((StringBuilder)localObject2).append(((SpellCheckerSubtype)localObject1).getExtraValue());
      paramPrintWriter.println(((StringBuilder)localObject2).toString());
    }
  }
  
  public ComponentName getComponent()
  {
    return new ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
  }
  
  public String getId()
  {
    return mId;
  }
  
  public String getPackageName()
  {
    return mService.serviceInfo.packageName;
  }
  
  public ServiceInfo getServiceInfo()
  {
    return mService.serviceInfo;
  }
  
  public String getSettingsActivity()
  {
    return mSettingsActivityName;
  }
  
  public SpellCheckerSubtype getSubtypeAt(int paramInt)
  {
    return (SpellCheckerSubtype)mSubtypes.get(paramInt);
  }
  
  public int getSubtypeCount()
  {
    return mSubtypes.size();
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    return mService.loadIcon(paramPackageManager);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    if ((mLabel != 0) && (paramPackageManager != null)) {
      return paramPackageManager.getText(getPackageName(), mLabel, mService.serviceInfo.applicationInfo);
    }
    return "";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mLabel);
    paramParcel.writeString(mId);
    paramParcel.writeString(mSettingsActivityName);
    mService.writeToParcel(paramParcel, paramInt);
    paramParcel.writeTypedList(mSubtypes);
  }
}
