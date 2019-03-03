package android.speech.tts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public class TtsEngines
{
  private static final boolean DBG = false;
  private static final String LOCALE_DELIMITER_NEW = "_";
  private static final String LOCALE_DELIMITER_OLD = "-";
  private static final String TAG = "TtsEngines";
  private static final String XML_TAG_NAME = "tts-engine";
  private static final Map<String, String> sNormalizeCountry;
  private static final Map<String, String> sNormalizeLanguage;
  private final Context mContext;
  
  static
  {
    HashMap localHashMap = new HashMap();
    String[] arrayOfString = Locale.getISOLanguages();
    int i = arrayOfString.length;
    int j = 0;
    String str;
    for (int k = 0; k < i; k++)
    {
      str = arrayOfString[k];
      try
      {
        Locale localLocale1 = new java/util/Locale;
        localLocale1.<init>(str);
        localHashMap.put(localLocale1.getISO3Language(), str);
      }
      catch (MissingResourceException localMissingResourceException1) {}
    }
    sNormalizeLanguage = Collections.unmodifiableMap(localHashMap);
    localHashMap = new HashMap();
    arrayOfString = Locale.getISOCountries();
    i = arrayOfString.length;
    for (k = j; k < i; k++)
    {
      str = arrayOfString[k];
      try
      {
        Locale localLocale2 = new java/util/Locale;
        localLocale2.<init>("", str);
        localHashMap.put(localLocale2.getISO3Country(), str);
      }
      catch (MissingResourceException localMissingResourceException2) {}
    }
    sNormalizeCountry = Collections.unmodifiableMap(localHashMap);
  }
  
  public TtsEngines(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private TextToSpeech.EngineInfo getEngineInfo(ResolveInfo paramResolveInfo, PackageManager paramPackageManager)
  {
    ServiceInfo localServiceInfo = serviceInfo;
    if (localServiceInfo != null)
    {
      TextToSpeech.EngineInfo localEngineInfo = new TextToSpeech.EngineInfo();
      name = packageName;
      paramPackageManager = localServiceInfo.loadLabel(paramPackageManager);
      if (TextUtils.isEmpty(paramPackageManager)) {
        paramPackageManager = name;
      } else {
        paramPackageManager = paramPackageManager.toString();
      }
      label = paramPackageManager;
      icon = localServiceInfo.getIconResource();
      priority = priority;
      system = isSystemEngine(localServiceInfo);
      return localEngineInfo;
    }
    return null;
  }
  
  private boolean isSystemEngine(ServiceInfo paramServiceInfo)
  {
    paramServiceInfo = applicationInfo;
    boolean bool = true;
    if ((paramServiceInfo == null) || ((flags & 0x1) == 0)) {
      bool = false;
    }
    return bool;
  }
  
  public static Locale normalizeTTSLocale(Locale paramLocale)
  {
    Object localObject1 = paramLocale.getLanguage();
    Object localObject2 = localObject1;
    if (!TextUtils.isEmpty((CharSequence)localObject1))
    {
      str1 = (String)sNormalizeLanguage.get(localObject1);
      localObject2 = localObject1;
      if (str1 != null) {
        localObject2 = str1;
      }
    }
    String str1 = paramLocale.getCountry();
    localObject1 = str1;
    if (!TextUtils.isEmpty(str1))
    {
      String str2 = (String)sNormalizeCountry.get(str1);
      localObject1 = str1;
      if (str2 != null) {
        localObject1 = str2;
      }
    }
    return new Locale(localObject2, (String)localObject1, paramLocale.getVariant());
  }
  
  private static String parseEnginePrefFromList(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1)) {
      return null;
    }
    for (Object localObject : paramString1.split(","))
    {
      int k = localObject.indexOf(':');
      if ((k > 0) && (paramString2.equals(localObject.substring(0, k)))) {
        return localObject.substring(k + 1);
      }
    }
    return null;
  }
  
  /* Error */
  private String settingsActivityFromServiceInfo(ServiceInfo paramServiceInfo, PackageManager paramPackageManager)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aconst_null
    //   6: astore 5
    //   8: aconst_null
    //   9: astore 6
    //   11: aload_1
    //   12: aload_2
    //   13: ldc -55
    //   15: invokevirtual 205	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   18: astore 7
    //   20: aload 7
    //   22: ifnonnull +123 -> 145
    //   25: aload 7
    //   27: astore 6
    //   29: aload 7
    //   31: astore_3
    //   32: aload 7
    //   34: astore 4
    //   36: aload 7
    //   38: astore 5
    //   40: new 207	java/lang/StringBuilder
    //   43: astore_2
    //   44: aload 7
    //   46: astore 6
    //   48: aload 7
    //   50: astore_3
    //   51: aload 7
    //   53: astore 4
    //   55: aload 7
    //   57: astore 5
    //   59: aload_2
    //   60: invokespecial 208	java/lang/StringBuilder:<init>	()V
    //   63: aload 7
    //   65: astore 6
    //   67: aload 7
    //   69: astore_3
    //   70: aload 7
    //   72: astore 4
    //   74: aload 7
    //   76: astore 5
    //   78: aload_2
    //   79: ldc -46
    //   81: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: aload 7
    //   87: astore 6
    //   89: aload 7
    //   91: astore_3
    //   92: aload 7
    //   94: astore 4
    //   96: aload 7
    //   98: astore 5
    //   100: aload_2
    //   101: aload_1
    //   102: invokevirtual 217	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: aload 7
    //   108: astore 6
    //   110: aload 7
    //   112: astore_3
    //   113: aload 7
    //   115: astore 4
    //   117: aload 7
    //   119: astore 5
    //   121: ldc 20
    //   123: aload_2
    //   124: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   127: invokestatic 224	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   130: pop
    //   131: aload 7
    //   133: ifnull +10 -> 143
    //   136: aload 7
    //   138: invokeinterface 229 1 0
    //   143: aconst_null
    //   144: areturn
    //   145: aload 7
    //   147: astore 6
    //   149: aload 7
    //   151: astore_3
    //   152: aload 7
    //   154: astore 4
    //   156: aload 7
    //   158: astore 5
    //   160: aload_2
    //   161: aload_1
    //   162: getfield 141	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   165: invokevirtual 235	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   168: astore_2
    //   169: aload 7
    //   171: astore 6
    //   173: aload 7
    //   175: astore_3
    //   176: aload 7
    //   178: astore 4
    //   180: aload 7
    //   182: astore 5
    //   184: aload 7
    //   186: invokeinterface 238 1 0
    //   191: istore 8
    //   193: iload 8
    //   195: iconst_1
    //   196: if_icmpeq +292 -> 488
    //   199: iload 8
    //   201: iconst_2
    //   202: if_icmpne -33 -> 169
    //   205: aload 7
    //   207: astore 6
    //   209: aload 7
    //   211: astore_3
    //   212: aload 7
    //   214: astore 4
    //   216: aload 7
    //   218: astore 5
    //   220: ldc 23
    //   222: aload 7
    //   224: invokeinterface 241 1 0
    //   229: invokevirtual 188	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   232: ifne +172 -> 404
    //   235: aload 7
    //   237: astore 6
    //   239: aload 7
    //   241: astore_3
    //   242: aload 7
    //   244: astore 4
    //   246: aload 7
    //   248: astore 5
    //   250: new 207	java/lang/StringBuilder
    //   253: astore_2
    //   254: aload 7
    //   256: astore 6
    //   258: aload 7
    //   260: astore_3
    //   261: aload 7
    //   263: astore 4
    //   265: aload 7
    //   267: astore 5
    //   269: aload_2
    //   270: invokespecial 208	java/lang/StringBuilder:<init>	()V
    //   273: aload 7
    //   275: astore 6
    //   277: aload 7
    //   279: astore_3
    //   280: aload 7
    //   282: astore 4
    //   284: aload 7
    //   286: astore 5
    //   288: aload_2
    //   289: ldc -13
    //   291: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   294: pop
    //   295: aload 7
    //   297: astore 6
    //   299: aload 7
    //   301: astore_3
    //   302: aload 7
    //   304: astore 4
    //   306: aload 7
    //   308: astore 5
    //   310: aload_2
    //   311: aload_1
    //   312: invokevirtual 217	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   315: pop
    //   316: aload 7
    //   318: astore 6
    //   320: aload 7
    //   322: astore_3
    //   323: aload 7
    //   325: astore 4
    //   327: aload 7
    //   329: astore 5
    //   331: aload_2
    //   332: ldc -11
    //   334: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   337: pop
    //   338: aload 7
    //   340: astore 6
    //   342: aload 7
    //   344: astore_3
    //   345: aload 7
    //   347: astore 4
    //   349: aload 7
    //   351: astore 5
    //   353: aload_2
    //   354: aload 7
    //   356: invokeinterface 241 1 0
    //   361: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: pop
    //   365: aload 7
    //   367: astore 6
    //   369: aload 7
    //   371: astore_3
    //   372: aload 7
    //   374: astore 4
    //   376: aload 7
    //   378: astore 5
    //   380: ldc 20
    //   382: aload_2
    //   383: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   386: invokestatic 224	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   389: pop
    //   390: aload 7
    //   392: ifnull +10 -> 402
    //   395: aload 7
    //   397: invokeinterface 229 1 0
    //   402: aconst_null
    //   403: areturn
    //   404: aload 7
    //   406: astore 6
    //   408: aload 7
    //   410: astore_3
    //   411: aload 7
    //   413: astore 4
    //   415: aload 7
    //   417: astore 5
    //   419: aload_2
    //   420: aload 7
    //   422: invokestatic 251	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   425: getstatic 257	com/android/internal/R$styleable:TextToSpeechEngine	[I
    //   428: invokevirtual 263	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   431: astore_2
    //   432: aload 7
    //   434: astore 6
    //   436: aload 7
    //   438: astore_3
    //   439: aload 7
    //   441: astore 4
    //   443: aload 7
    //   445: astore 5
    //   447: aload_2
    //   448: iconst_0
    //   449: invokevirtual 268	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   452: astore 9
    //   454: aload 7
    //   456: astore 6
    //   458: aload 7
    //   460: astore_3
    //   461: aload 7
    //   463: astore 4
    //   465: aload 7
    //   467: astore 5
    //   469: aload_2
    //   470: invokevirtual 271	android/content/res/TypedArray:recycle	()V
    //   473: aload 7
    //   475: ifnull +10 -> 485
    //   478: aload 7
    //   480: invokeinterface 229 1 0
    //   485: aload 9
    //   487: areturn
    //   488: aload 7
    //   490: ifnull +10 -> 500
    //   493: aload 7
    //   495: invokeinterface 229 1 0
    //   500: aconst_null
    //   501: areturn
    //   502: astore_1
    //   503: goto +248 -> 751
    //   506: astore_2
    //   507: aload_3
    //   508: astore 6
    //   510: new 207	java/lang/StringBuilder
    //   513: astore 7
    //   515: aload_3
    //   516: astore 6
    //   518: aload 7
    //   520: invokespecial 208	java/lang/StringBuilder:<init>	()V
    //   523: aload_3
    //   524: astore 6
    //   526: aload 7
    //   528: ldc_w 273
    //   531: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   534: pop
    //   535: aload_3
    //   536: astore 6
    //   538: aload 7
    //   540: aload_1
    //   541: invokevirtual 217	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   544: pop
    //   545: aload_3
    //   546: astore 6
    //   548: aload 7
    //   550: ldc_w 275
    //   553: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   556: pop
    //   557: aload_3
    //   558: astore 6
    //   560: aload 7
    //   562: aload_2
    //   563: invokevirtual 217	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   566: pop
    //   567: aload_3
    //   568: astore 6
    //   570: ldc 20
    //   572: aload 7
    //   574: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   577: invokestatic 224	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   580: pop
    //   581: aload_3
    //   582: ifnull +9 -> 591
    //   585: aload_3
    //   586: invokeinterface 229 1 0
    //   591: aconst_null
    //   592: areturn
    //   593: astore 7
    //   595: aload 4
    //   597: astore 6
    //   599: new 207	java/lang/StringBuilder
    //   602: astore_2
    //   603: aload 4
    //   605: astore 6
    //   607: aload_2
    //   608: invokespecial 208	java/lang/StringBuilder:<init>	()V
    //   611: aload 4
    //   613: astore 6
    //   615: aload_2
    //   616: ldc_w 273
    //   619: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   622: pop
    //   623: aload 4
    //   625: astore 6
    //   627: aload_2
    //   628: aload_1
    //   629: invokevirtual 217	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   632: pop
    //   633: aload 4
    //   635: astore 6
    //   637: aload_2
    //   638: ldc_w 275
    //   641: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   644: pop
    //   645: aload 4
    //   647: astore 6
    //   649: aload_2
    //   650: aload 7
    //   652: invokevirtual 217	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   655: pop
    //   656: aload 4
    //   658: astore 6
    //   660: ldc 20
    //   662: aload_2
    //   663: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   666: invokestatic 224	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   669: pop
    //   670: aload 4
    //   672: ifnull +10 -> 682
    //   675: aload 4
    //   677: invokeinterface 229 1 0
    //   682: aconst_null
    //   683: areturn
    //   684: astore_2
    //   685: aload 5
    //   687: astore 6
    //   689: new 207	java/lang/StringBuilder
    //   692: astore_2
    //   693: aload 5
    //   695: astore 6
    //   697: aload_2
    //   698: invokespecial 208	java/lang/StringBuilder:<init>	()V
    //   701: aload 5
    //   703: astore 6
    //   705: aload_2
    //   706: ldc_w 277
    //   709: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   712: pop
    //   713: aload 5
    //   715: astore 6
    //   717: aload_2
    //   718: aload_1
    //   719: invokevirtual 217	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   722: pop
    //   723: aload 5
    //   725: astore 6
    //   727: ldc 20
    //   729: aload_2
    //   730: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   733: invokestatic 224	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   736: pop
    //   737: aload 5
    //   739: ifnull +10 -> 749
    //   742: aload 5
    //   744: invokeinterface 229 1 0
    //   749: aconst_null
    //   750: areturn
    //   751: aload 6
    //   753: ifnull +10 -> 763
    //   756: aload 6
    //   758: invokeinterface 229 1 0
    //   763: aload_1
    //   764: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	765	0	this	TtsEngines
    //   0	765	1	paramServiceInfo	ServiceInfo
    //   0	765	2	paramPackageManager	PackageManager
    //   1	585	3	localObject1	Object
    //   3	673	4	localObject2	Object
    //   6	737	5	localObject3	Object
    //   9	748	6	localObject4	Object
    //   18	555	7	localObject5	Object
    //   593	58	7	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   191	12	8	i	int
    //   452	34	9	str	String
    // Exception table:
    //   from	to	target	type
    //   11	20	502	finally
    //   40	44	502	finally
    //   59	63	502	finally
    //   78	85	502	finally
    //   100	106	502	finally
    //   121	131	502	finally
    //   160	169	502	finally
    //   184	193	502	finally
    //   220	235	502	finally
    //   250	254	502	finally
    //   269	273	502	finally
    //   288	295	502	finally
    //   310	316	502	finally
    //   331	338	502	finally
    //   353	365	502	finally
    //   380	390	502	finally
    //   419	432	502	finally
    //   447	454	502	finally
    //   469	473	502	finally
    //   510	515	502	finally
    //   518	523	502	finally
    //   526	535	502	finally
    //   538	545	502	finally
    //   548	557	502	finally
    //   560	567	502	finally
    //   570	581	502	finally
    //   599	603	502	finally
    //   607	611	502	finally
    //   615	623	502	finally
    //   627	633	502	finally
    //   637	645	502	finally
    //   649	656	502	finally
    //   660	670	502	finally
    //   689	693	502	finally
    //   697	701	502	finally
    //   705	713	502	finally
    //   717	723	502	finally
    //   727	737	502	finally
    //   11	20	506	java/io/IOException
    //   40	44	506	java/io/IOException
    //   59	63	506	java/io/IOException
    //   78	85	506	java/io/IOException
    //   100	106	506	java/io/IOException
    //   121	131	506	java/io/IOException
    //   160	169	506	java/io/IOException
    //   184	193	506	java/io/IOException
    //   220	235	506	java/io/IOException
    //   250	254	506	java/io/IOException
    //   269	273	506	java/io/IOException
    //   288	295	506	java/io/IOException
    //   310	316	506	java/io/IOException
    //   331	338	506	java/io/IOException
    //   353	365	506	java/io/IOException
    //   380	390	506	java/io/IOException
    //   419	432	506	java/io/IOException
    //   447	454	506	java/io/IOException
    //   469	473	506	java/io/IOException
    //   11	20	593	org/xmlpull/v1/XmlPullParserException
    //   40	44	593	org/xmlpull/v1/XmlPullParserException
    //   59	63	593	org/xmlpull/v1/XmlPullParserException
    //   78	85	593	org/xmlpull/v1/XmlPullParserException
    //   100	106	593	org/xmlpull/v1/XmlPullParserException
    //   121	131	593	org/xmlpull/v1/XmlPullParserException
    //   160	169	593	org/xmlpull/v1/XmlPullParserException
    //   184	193	593	org/xmlpull/v1/XmlPullParserException
    //   220	235	593	org/xmlpull/v1/XmlPullParserException
    //   250	254	593	org/xmlpull/v1/XmlPullParserException
    //   269	273	593	org/xmlpull/v1/XmlPullParserException
    //   288	295	593	org/xmlpull/v1/XmlPullParserException
    //   310	316	593	org/xmlpull/v1/XmlPullParserException
    //   331	338	593	org/xmlpull/v1/XmlPullParserException
    //   353	365	593	org/xmlpull/v1/XmlPullParserException
    //   380	390	593	org/xmlpull/v1/XmlPullParserException
    //   419	432	593	org/xmlpull/v1/XmlPullParserException
    //   447	454	593	org/xmlpull/v1/XmlPullParserException
    //   469	473	593	org/xmlpull/v1/XmlPullParserException
    //   11	20	684	android/content/pm/PackageManager$NameNotFoundException
    //   40	44	684	android/content/pm/PackageManager$NameNotFoundException
    //   59	63	684	android/content/pm/PackageManager$NameNotFoundException
    //   78	85	684	android/content/pm/PackageManager$NameNotFoundException
    //   100	106	684	android/content/pm/PackageManager$NameNotFoundException
    //   121	131	684	android/content/pm/PackageManager$NameNotFoundException
    //   160	169	684	android/content/pm/PackageManager$NameNotFoundException
    //   184	193	684	android/content/pm/PackageManager$NameNotFoundException
    //   220	235	684	android/content/pm/PackageManager$NameNotFoundException
    //   250	254	684	android/content/pm/PackageManager$NameNotFoundException
    //   269	273	684	android/content/pm/PackageManager$NameNotFoundException
    //   288	295	684	android/content/pm/PackageManager$NameNotFoundException
    //   310	316	684	android/content/pm/PackageManager$NameNotFoundException
    //   331	338	684	android/content/pm/PackageManager$NameNotFoundException
    //   353	365	684	android/content/pm/PackageManager$NameNotFoundException
    //   380	390	684	android/content/pm/PackageManager$NameNotFoundException
    //   419	432	684	android/content/pm/PackageManager$NameNotFoundException
    //   447	454	684	android/content/pm/PackageManager$NameNotFoundException
    //   469	473	684	android/content/pm/PackageManager$NameNotFoundException
  }
  
  public static String[] toOldLocaleStringFormat(Locale paramLocale)
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "";
    arrayOfString[1] = "";
    arrayOfString[2] = "";
    try
    {
      arrayOfString[0] = paramLocale.getISO3Language();
      arrayOfString[1] = paramLocale.getISO3Country();
      arrayOfString[2] = paramLocale.getVariant();
      return arrayOfString;
    }
    catch (MissingResourceException paramLocale) {}
    return tmp60_54;
  }
  
  private String updateValueInCommaSeparatedList(String paramString1, String paramString2, String paramString3)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (TextUtils.isEmpty(paramString1))
    {
      localStringBuilder.append(paramString2);
      localStringBuilder.append(':');
      localStringBuilder.append(paramString3);
    }
    else
    {
      String[] arrayOfString = paramString1.split(",");
      int i = 0;
      int j = arrayOfString.length;
      int k = 1;
      int m = 0;
      while (m < j)
      {
        paramString1 = arrayOfString[m];
        int n = paramString1.indexOf(':');
        int i1 = i;
        int i2 = k;
        if (n > 0) {
          if (paramString2.equals(paramString1.substring(0, n)))
          {
            if (k != 0) {
              k = 0;
            } else {
              localStringBuilder.append(',');
            }
            i1 = 1;
            localStringBuilder.append(paramString2);
            localStringBuilder.append(':');
            localStringBuilder.append(paramString3);
            i2 = k;
          }
          else
          {
            if (k != 0) {
              k = 0;
            } else {
              localStringBuilder.append(',');
            }
            localStringBuilder.append(paramString1);
            i2 = k;
            i1 = i;
          }
        }
        m++;
        i = i1;
        k = i2;
      }
      if (i == 0)
      {
        localStringBuilder.append(',');
        localStringBuilder.append(paramString2);
        localStringBuilder.append(':');
        localStringBuilder.append(paramString3);
      }
    }
    return localStringBuilder.toString();
  }
  
  public String getDefaultEngine()
  {
    String str = Settings.Secure.getString(mContext.getContentResolver(), "tts_default_synth");
    if (!isEngineInstalled(str)) {
      str = getHighestRankedEngineName();
    }
    return str;
  }
  
  public TextToSpeech.EngineInfo getEngineInfo(String paramString)
  {
    PackageManager localPackageManager = mContext.getPackageManager();
    Intent localIntent = new Intent("android.intent.action.TTS_SERVICE");
    localIntent.setPackage(paramString);
    paramString = localPackageManager.queryIntentServices(localIntent, 65536);
    if ((paramString != null) && (paramString.size() == 1)) {
      return getEngineInfo((ResolveInfo)paramString.get(0), localPackageManager);
    }
    return null;
  }
  
  public List<TextToSpeech.EngineInfo> getEngines()
  {
    PackageManager localPackageManager = mContext.getPackageManager();
    Object localObject1 = new Intent("android.intent.action.TTS_SERVICE");
    Object localObject2 = localPackageManager.queryIntentServices((Intent)localObject1, 65536);
    if (localObject2 == null) {
      return Collections.emptyList();
    }
    localObject1 = new ArrayList(((List)localObject2).size());
    localObject2 = ((List)localObject2).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      TextToSpeech.EngineInfo localEngineInfo = getEngineInfo((ResolveInfo)((Iterator)localObject2).next(), localPackageManager);
      if (localEngineInfo != null) {
        ((List)localObject1).add(localEngineInfo);
      }
    }
    Collections.sort((List)localObject1, EngineInfoComparator.INSTANCE);
    return localObject1;
  }
  
  public String getHighestRankedEngineName()
  {
    List localList = getEngines();
    if ((localList.size() > 0) && (get0system)) {
      return get0name;
    }
    return null;
  }
  
  public Locale getLocalePrefForEngine(String paramString)
  {
    return getLocalePrefForEngine(paramString, Settings.Secure.getString(mContext.getContentResolver(), "tts_default_locale"));
  }
  
  public Locale getLocalePrefForEngine(String paramString1, String paramString2)
  {
    String str = parseEnginePrefFromList(paramString2, paramString1);
    if (TextUtils.isEmpty(str)) {
      return Locale.getDefault();
    }
    paramString2 = parseLocaleString(str);
    paramString1 = paramString2;
    if (paramString2 == null)
    {
      paramString1 = new StringBuilder();
      paramString1.append("Failed to parse locale ");
      paramString1.append(str);
      paramString1.append(", returning en_US instead");
      Log.w("TtsEngines", paramString1.toString());
      paramString1 = Locale.US;
    }
    return paramString1;
  }
  
  public Intent getSettingsIntent(String paramString)
  {
    Object localObject1 = mContext.getPackageManager();
    Object localObject2 = new Intent("android.intent.action.TTS_SERVICE");
    ((Intent)localObject2).setPackage(paramString);
    localObject2 = ((PackageManager)localObject1).queryIntentServices((Intent)localObject2, 65664);
    if ((localObject2 != null) && (((List)localObject2).size() == 1))
    {
      localObject2 = get0serviceInfo;
      if (localObject2 != null)
      {
        localObject2 = settingsActivityFromServiceInfo((ServiceInfo)localObject2, (PackageManager)localObject1);
        if (localObject2 != null)
        {
          localObject1 = new Intent();
          ((Intent)localObject1).setClassName(paramString, (String)localObject2);
          return localObject1;
        }
      }
    }
    return null;
  }
  
  public boolean isEngineInstalled(String paramString)
  {
    boolean bool = false;
    if (paramString == null) {
      return false;
    }
    if (getEngineInfo(paramString) != null) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isLocaleSetToDefaultForEngine(String paramString)
  {
    return TextUtils.isEmpty(parseEnginePrefFromList(Settings.Secure.getString(mContext.getContentResolver(), "tts_default_locale"), paramString));
  }
  
  public Locale parseLocaleString(String paramString)
  {
    Object localObject1 = "";
    String str1 = "";
    String str2 = "";
    Object localObject2 = str1;
    String str3 = str2;
    if (!TextUtils.isEmpty(paramString))
    {
      String[] arrayOfString = paramString.split("[-_]");
      String str4 = arrayOfString[0].toLowerCase();
      if (arrayOfString.length == 0)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Failed to convert ");
        ((StringBuilder)localObject2).append(paramString);
        ((StringBuilder)localObject2).append(" to a valid Locale object. Only separators");
        Log.w("TtsEngines", ((StringBuilder)localObject2).toString());
        return null;
      }
      if (arrayOfString.length > 3)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Failed to convert ");
        ((StringBuilder)localObject2).append(paramString);
        ((StringBuilder)localObject2).append(" to a valid Locale object. Too many separators");
        Log.w("TtsEngines", ((StringBuilder)localObject2).toString());
        return null;
      }
      if (arrayOfString.length >= 2) {
        str1 = arrayOfString[1].toUpperCase();
      }
      localObject1 = str4;
      localObject2 = str1;
      str3 = str2;
      if (arrayOfString.length >= 3)
      {
        str3 = arrayOfString[2];
        localObject2 = str1;
        localObject1 = str4;
      }
    }
    str1 = (String)sNormalizeLanguage.get(localObject1);
    if (str1 != null) {
      localObject1 = str1;
    }
    str1 = (String)sNormalizeCountry.get(localObject2);
    if (str1 != null) {
      localObject2 = str1;
    }
    localObject2 = new Locale((String)localObject1, (String)localObject2, str3);
    try
    {
      ((Locale)localObject2).getISO3Language();
      ((Locale)localObject2).getISO3Country();
      return localObject2;
    }
    catch (MissingResourceException localMissingResourceException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to convert ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" to a valid Locale object.");
      Log.w("TtsEngines", localStringBuilder.toString());
    }
    return null;
  }
  
  public void updateLocalePrefForEngine(String paramString, Locale paramLocale)
  {
    try
    {
      String str = Settings.Secure.getString(mContext.getContentResolver(), "tts_default_locale");
      if (paramLocale != null) {
        paramLocale = paramLocale.toString();
      } else {
        paramLocale = "";
      }
      paramString = updateValueInCommaSeparatedList(str, paramString, paramLocale);
      Settings.Secure.putString(mContext.getContentResolver(), "tts_default_locale", paramString.toString());
      return;
    }
    finally {}
  }
  
  private static class EngineInfoComparator
    implements Comparator<TextToSpeech.EngineInfo>
  {
    static EngineInfoComparator INSTANCE = new EngineInfoComparator();
    
    private EngineInfoComparator() {}
    
    public int compare(TextToSpeech.EngineInfo paramEngineInfo1, TextToSpeech.EngineInfo paramEngineInfo2)
    {
      if ((system) && (!system)) {
        return -1;
      }
      if ((system) && (!system)) {
        return 1;
      }
      return priority - priority;
    }
  }
}
