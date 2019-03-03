package android.printservice;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class PrintServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PrintServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public PrintServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PrintServiceInfo(paramAnonymousParcel);
    }
    
    public PrintServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new PrintServiceInfo[paramAnonymousInt];
    }
  };
  private static final String LOG_TAG = PrintServiceInfo.class.getSimpleName();
  private static final String TAG_PRINT_SERVICE = "print-service";
  private final String mAddPrintersActivityName;
  private final String mAdvancedPrintOptionsActivityName;
  private final String mId;
  private boolean mIsEnabled;
  private final ResolveInfo mResolveInfo;
  private final String mSettingsActivityName;
  
  public PrintServiceInfo(ResolveInfo paramResolveInfo, String paramString1, String paramString2, String paramString3)
  {
    mId = new ComponentName(serviceInfo.packageName, serviceInfo.name).flattenToString();
    mResolveInfo = paramResolveInfo;
    mSettingsActivityName = paramString1;
    mAddPrintersActivityName = paramString2;
    mAdvancedPrintOptionsActivityName = paramString3;
  }
  
  public PrintServiceInfo(Parcel paramParcel)
  {
    mId = paramParcel.readString();
    boolean bool;
    if (paramParcel.readByte() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mIsEnabled = bool;
    mResolveInfo = ((ResolveInfo)paramParcel.readParcelable(null));
    mSettingsActivityName = paramParcel.readString();
    mAddPrintersActivityName = paramParcel.readString();
    mAdvancedPrintOptionsActivityName = paramParcel.readString();
  }
  
  /* Error */
  public static PrintServiceInfo create(android.content.Context paramContext, ResolveInfo paramResolveInfo)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aconst_null
    //   8: astore 5
    //   10: aconst_null
    //   11: astore 6
    //   13: aconst_null
    //   14: astore 7
    //   16: aconst_null
    //   17: astore 8
    //   19: aconst_null
    //   20: astore 9
    //   22: aconst_null
    //   23: astore 10
    //   25: aconst_null
    //   26: astore 11
    //   28: aconst_null
    //   29: astore 12
    //   31: aconst_null
    //   32: astore 13
    //   34: aconst_null
    //   35: astore 14
    //   37: aconst_null
    //   38: astore 15
    //   40: aconst_null
    //   41: astore 16
    //   43: aload_0
    //   44: invokevirtual 105	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   47: astore 17
    //   49: aload_1
    //   50: getfield 51	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   53: aload 17
    //   55: ldc 107
    //   57: invokevirtual 111	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   60: astore 18
    //   62: aload 5
    //   64: astore_0
    //   65: aload 18
    //   67: ifnull +674 -> 741
    //   70: iconst_0
    //   71: istore 19
    //   73: iload 19
    //   75: iconst_1
    //   76: if_icmpeq +55 -> 131
    //   79: iload 19
    //   81: iconst_2
    //   82: if_icmpeq +49 -> 131
    //   85: aload_2
    //   86: astore 20
    //   88: aload 7
    //   90: astore 21
    //   92: aload 12
    //   94: astore 5
    //   96: aload_3
    //   97: astore 22
    //   99: aload 8
    //   101: astore 23
    //   103: aload 13
    //   105: astore 24
    //   107: aload 4
    //   109: astore 25
    //   111: aload 9
    //   113: astore 26
    //   115: aload 14
    //   117: astore 27
    //   119: aload 18
    //   121: invokeinterface 117 1 0
    //   126: istore 19
    //   128: goto -55 -> 73
    //   131: aload_2
    //   132: astore 20
    //   134: aload 7
    //   136: astore 21
    //   138: aload 12
    //   140: astore 5
    //   142: aload_3
    //   143: astore 22
    //   145: aload 8
    //   147: astore 23
    //   149: aload 13
    //   151: astore 24
    //   153: aload 4
    //   155: astore 25
    //   157: aload 9
    //   159: astore 26
    //   161: aload 14
    //   163: astore 27
    //   165: ldc 17
    //   167: aload 18
    //   169: invokeinterface 120 1 0
    //   174: invokevirtual 126	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   177: ifne +61 -> 238
    //   180: aload_2
    //   181: astore 20
    //   183: aload 7
    //   185: astore 21
    //   187: aload 12
    //   189: astore 5
    //   191: aload_3
    //   192: astore 22
    //   194: aload 8
    //   196: astore 23
    //   198: aload 13
    //   200: astore 24
    //   202: aload 4
    //   204: astore 25
    //   206: aload 9
    //   208: astore 26
    //   210: aload 14
    //   212: astore 27
    //   214: getstatic 35	android/printservice/PrintServiceInfo:LOG_TAG	Ljava/lang/String;
    //   217: ldc -128
    //   219: invokestatic 134	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   222: pop
    //   223: aload 6
    //   225: astore 5
    //   227: aload 11
    //   229: astore 27
    //   231: aload 16
    //   233: astore 24
    //   235: goto +234 -> 469
    //   238: aload_2
    //   239: astore 20
    //   241: aload 7
    //   243: astore 21
    //   245: aload 12
    //   247: astore 5
    //   249: aload_3
    //   250: astore 22
    //   252: aload 8
    //   254: astore 23
    //   256: aload 13
    //   258: astore 24
    //   260: aload 4
    //   262: astore 25
    //   264: aload 9
    //   266: astore 26
    //   268: aload 14
    //   270: astore 27
    //   272: aload 17
    //   274: aload_1
    //   275: getfield 51	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   278: getfield 138	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   281: invokevirtual 144	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   284: aload 18
    //   286: invokestatic 150	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   289: getstatic 156	com/android/internal/R$styleable:PrintService	[I
    //   292: invokevirtual 162	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   295: astore 16
    //   297: aload_2
    //   298: astore 20
    //   300: aload 7
    //   302: astore 21
    //   304: aload 12
    //   306: astore 5
    //   308: aload_3
    //   309: astore 22
    //   311: aload 8
    //   313: astore 23
    //   315: aload 13
    //   317: astore 24
    //   319: aload 4
    //   321: astore 25
    //   323: aload 9
    //   325: astore 26
    //   327: aload 14
    //   329: astore 27
    //   331: aload 16
    //   333: iconst_0
    //   334: invokevirtual 168	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   337: astore_0
    //   338: aload_0
    //   339: astore 20
    //   341: aload 7
    //   343: astore 21
    //   345: aload 12
    //   347: astore 5
    //   349: aload_0
    //   350: astore 22
    //   352: aload 8
    //   354: astore 23
    //   356: aload 13
    //   358: astore 24
    //   360: aload_0
    //   361: astore 25
    //   363: aload 9
    //   365: astore 26
    //   367: aload 14
    //   369: astore 27
    //   371: aload 16
    //   373: iconst_1
    //   374: invokevirtual 168	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   377: astore 10
    //   379: aload_0
    //   380: astore 20
    //   382: aload 10
    //   384: astore 21
    //   386: aload 12
    //   388: astore 5
    //   390: aload_0
    //   391: astore 22
    //   393: aload 10
    //   395: astore 23
    //   397: aload 13
    //   399: astore 24
    //   401: aload_0
    //   402: astore 25
    //   404: aload 10
    //   406: astore 26
    //   408: aload 14
    //   410: astore 27
    //   412: aload 16
    //   414: iconst_3
    //   415: invokevirtual 168	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   418: astore 15
    //   420: aload_0
    //   421: astore 20
    //   423: aload 10
    //   425: astore 21
    //   427: aload 15
    //   429: astore 5
    //   431: aload_0
    //   432: astore 22
    //   434: aload 10
    //   436: astore 23
    //   438: aload 15
    //   440: astore 24
    //   442: aload_0
    //   443: astore 25
    //   445: aload 10
    //   447: astore 26
    //   449: aload 15
    //   451: astore 27
    //   453: aload 16
    //   455: invokevirtual 171	android/content/res/TypedArray:recycle	()V
    //   458: aload 15
    //   460: astore 24
    //   462: aload 10
    //   464: astore 27
    //   466: aload_0
    //   467: astore 5
    //   469: aload 5
    //   471: astore_0
    //   472: aload 27
    //   474: astore 10
    //   476: aload 24
    //   478: astore 15
    //   480: aload 18
    //   482: ifnull +259 -> 741
    //   485: aload 24
    //   487: astore 15
    //   489: aload 27
    //   491: astore 10
    //   493: aload 5
    //   495: astore_0
    //   496: aload 18
    //   498: invokeinterface 174 1 0
    //   503: goto +238 -> 741
    //   506: astore_0
    //   507: goto +220 -> 727
    //   510: astore_0
    //   511: getstatic 35	android/printservice/PrintServiceInfo:LOG_TAG	Ljava/lang/String;
    //   514: astore 10
    //   516: new 176	java/lang/StringBuilder
    //   519: astore_0
    //   520: aload_0
    //   521: invokespecial 177	java/lang/StringBuilder:<init>	()V
    //   524: aload_0
    //   525: ldc -77
    //   527: invokevirtual 183	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   530: pop
    //   531: aload_0
    //   532: aload_1
    //   533: getfield 51	android/content/pm/ResolveInfo:serviceInfo	Landroid/content/pm/ServiceInfo;
    //   536: getfield 56	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   539: invokevirtual 183	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   542: pop
    //   543: aload 10
    //   545: aload_0
    //   546: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   549: invokestatic 134	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   552: pop
    //   553: aload 20
    //   555: astore_0
    //   556: aload 21
    //   558: astore 10
    //   560: aload 5
    //   562: astore 15
    //   564: aload 18
    //   566: ifnull +175 -> 741
    //   569: aload 20
    //   571: astore_0
    //   572: aload 21
    //   574: astore 10
    //   576: aload 5
    //   578: astore 15
    //   580: goto -84 -> 496
    //   583: astore_0
    //   584: getstatic 35	android/printservice/PrintServiceInfo:LOG_TAG	Ljava/lang/String;
    //   587: astore 10
    //   589: new 176	java/lang/StringBuilder
    //   592: astore 15
    //   594: aload 15
    //   596: invokespecial 177	java/lang/StringBuilder:<init>	()V
    //   599: aload 15
    //   601: ldc -68
    //   603: invokevirtual 183	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   606: pop
    //   607: aload 15
    //   609: aload_0
    //   610: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   613: pop
    //   614: aload 10
    //   616: aload 15
    //   618: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   621: invokestatic 194	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   624: pop
    //   625: aload 22
    //   627: astore_0
    //   628: aload 23
    //   630: astore 10
    //   632: aload 24
    //   634: astore 15
    //   636: aload 18
    //   638: ifnull +103 -> 741
    //   641: aload 22
    //   643: astore_0
    //   644: aload 23
    //   646: astore 10
    //   648: aload 24
    //   650: astore 15
    //   652: goto -156 -> 496
    //   655: astore_0
    //   656: getstatic 35	android/printservice/PrintServiceInfo:LOG_TAG	Ljava/lang/String;
    //   659: astore 10
    //   661: new 176	java/lang/StringBuilder
    //   664: astore 15
    //   666: aload 15
    //   668: invokespecial 177	java/lang/StringBuilder:<init>	()V
    //   671: aload 15
    //   673: ldc -68
    //   675: invokevirtual 183	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   678: pop
    //   679: aload 15
    //   681: aload_0
    //   682: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   685: pop
    //   686: aload 10
    //   688: aload 15
    //   690: invokevirtual 186	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   693: invokestatic 194	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   696: pop
    //   697: aload 25
    //   699: astore_0
    //   700: aload 26
    //   702: astore 10
    //   704: aload 27
    //   706: astore 15
    //   708: aload 18
    //   710: ifnull +31 -> 741
    //   713: aload 25
    //   715: astore_0
    //   716: aload 26
    //   718: astore 10
    //   720: aload 27
    //   722: astore 15
    //   724: goto -228 -> 496
    //   727: aload 18
    //   729: ifnull +10 -> 739
    //   732: aload 18
    //   734: invokeinterface 174 1 0
    //   739: aload_0
    //   740: athrow
    //   741: new 2	android/printservice/PrintServiceInfo
    //   744: dup
    //   745: aload_1
    //   746: aload_0
    //   747: aload 10
    //   749: aload 15
    //   751: invokespecial 196	android/printservice/PrintServiceInfo:<init>	(Landroid/content/pm/ResolveInfo;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   754: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	755	0	paramContext	android.content.Context
    //   0	755	1	paramResolveInfo	ResolveInfo
    //   1	297	2	localObject1	Object
    //   3	306	3	localObject2	Object
    //   5	315	4	localObject3	Object
    //   8	569	5	localObject4	Object
    //   11	213	6	localObject5	Object
    //   14	328	7	localObject6	Object
    //   17	336	8	localObject7	Object
    //   20	344	9	localObject8	Object
    //   23	725	10	localObject9	Object
    //   26	202	11	localObject10	Object
    //   29	358	12	localObject11	Object
    //   32	366	13	localObject12	Object
    //   35	374	14	localObject13	Object
    //   38	712	15	localObject14	Object
    //   41	413	16	localTypedArray	android.content.res.TypedArray
    //   47	226	17	localPackageManager	android.content.pm.PackageManager
    //   60	673	18	localXmlResourceParser	android.content.res.XmlResourceParser
    //   71	56	19	i	int
    //   86	484	20	localObject15	Object
    //   90	483	21	localObject16	Object
    //   97	545	22	localObject17	Object
    //   101	544	23	localObject18	Object
    //   105	544	24	localObject19	Object
    //   109	605	25	localObject20	Object
    //   113	604	26	localObject21	Object
    //   117	604	27	localObject22	Object
    // Exception table:
    //   from	to	target	type
    //   119	128	506	finally
    //   165	180	506	finally
    //   214	223	506	finally
    //   272	297	506	finally
    //   331	338	506	finally
    //   371	379	506	finally
    //   412	420	506	finally
    //   453	458	506	finally
    //   511	553	506	finally
    //   584	625	506	finally
    //   656	697	506	finally
    //   119	128	510	android/content/pm/PackageManager$NameNotFoundException
    //   165	180	510	android/content/pm/PackageManager$NameNotFoundException
    //   214	223	510	android/content/pm/PackageManager$NameNotFoundException
    //   272	297	510	android/content/pm/PackageManager$NameNotFoundException
    //   331	338	510	android/content/pm/PackageManager$NameNotFoundException
    //   371	379	510	android/content/pm/PackageManager$NameNotFoundException
    //   412	420	510	android/content/pm/PackageManager$NameNotFoundException
    //   453	458	510	android/content/pm/PackageManager$NameNotFoundException
    //   119	128	583	org/xmlpull/v1/XmlPullParserException
    //   165	180	583	org/xmlpull/v1/XmlPullParserException
    //   214	223	583	org/xmlpull/v1/XmlPullParserException
    //   272	297	583	org/xmlpull/v1/XmlPullParserException
    //   331	338	583	org/xmlpull/v1/XmlPullParserException
    //   371	379	583	org/xmlpull/v1/XmlPullParserException
    //   412	420	583	org/xmlpull/v1/XmlPullParserException
    //   453	458	583	org/xmlpull/v1/XmlPullParserException
    //   119	128	655	java/io/IOException
    //   165	180	655	java/io/IOException
    //   214	223	655	java/io/IOException
    //   272	297	655	java/io/IOException
    //   331	338	655	java/io/IOException
    //   371	379	655	java/io/IOException
    //   412	420	655	java/io/IOException
    //   453	458	655	java/io/IOException
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
    paramObject = (PrintServiceInfo)paramObject;
    if (mId == null)
    {
      if (mId != null) {
        return false;
      }
    }
    else if (!mId.equals(mId)) {
      return false;
    }
    return true;
  }
  
  public String getAddPrintersActivityName()
  {
    return mAddPrintersActivityName;
  }
  
  public String getAdvancedOptionsActivityName()
  {
    return mAdvancedPrintOptionsActivityName;
  }
  
  public ComponentName getComponentName()
  {
    return new ComponentName(mResolveInfo.serviceInfo.packageName, mResolveInfo.serviceInfo.name);
  }
  
  public String getId()
  {
    return mId;
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
    if (mId == null) {
      i = 0;
    } else {
      i = mId.hashCode();
    }
    return 31 + i;
  }
  
  public boolean isEnabled()
  {
    return mIsEnabled;
  }
  
  public void setIsEnabled(boolean paramBoolean)
  {
    mIsEnabled = paramBoolean;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PrintServiceInfo{");
    localStringBuilder.append("id=");
    localStringBuilder.append(mId);
    localStringBuilder.append("isEnabled=");
    localStringBuilder.append(mIsEnabled);
    localStringBuilder.append(", resolveInfo=");
    localStringBuilder.append(mResolveInfo);
    localStringBuilder.append(", settingsActivityName=");
    localStringBuilder.append(mSettingsActivityName);
    localStringBuilder.append(", addPrintersActivityName=");
    localStringBuilder.append(mAddPrintersActivityName);
    localStringBuilder.append(", advancedPrintOptionsActivityName=");
    localStringBuilder.append(mAdvancedPrintOptionsActivityName);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeByte((byte)mIsEnabled);
    paramParcel.writeParcelable(mResolveInfo, 0);
    paramParcel.writeString(mSettingsActivityName);
    paramParcel.writeString(mAddPrintersActivityName);
    paramParcel.writeString(mAdvancedPrintOptionsActivityName);
  }
}
