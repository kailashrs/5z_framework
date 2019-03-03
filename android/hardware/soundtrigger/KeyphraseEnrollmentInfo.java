package android.hardware.soundtrigger;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Slog;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class KeyphraseEnrollmentInfo
{
  public static final String ACTION_MANAGE_VOICE_KEYPHRASES = "com.android.intent.action.MANAGE_VOICE_KEYPHRASES";
  public static final String EXTRA_VOICE_KEYPHRASE_ACTION = "com.android.intent.extra.VOICE_KEYPHRASE_ACTION";
  public static final String EXTRA_VOICE_KEYPHRASE_HINT_TEXT = "com.android.intent.extra.VOICE_KEYPHRASE_HINT_TEXT";
  public static final String EXTRA_VOICE_KEYPHRASE_LOCALE = "com.android.intent.extra.VOICE_KEYPHRASE_LOCALE";
  private static final String TAG = "KeyphraseEnrollmentInfo";
  private static final String VOICE_KEYPHRASE_META_DATA = "android.voice_enrollment";
  private final Map<KeyphraseMetadata, String> mKeyphrasePackageMap;
  private final KeyphraseMetadata[] mKeyphrases;
  private String mParseError;
  
  public KeyphraseEnrollmentInfo(PackageManager paramPackageManager)
  {
    Object localObject1 = paramPackageManager.queryIntentActivities(new Intent("com.android.intent.action.MANAGE_VOICE_KEYPHRASES"), 65536);
    if ((localObject1 != null) && (!((List)localObject1).isEmpty()))
    {
      LinkedList localLinkedList = new LinkedList();
      mKeyphrasePackageMap = new HashMap();
      localObject1 = ((List)localObject1).iterator();
      while (((Iterator)localObject1).hasNext())
      {
        Object localObject2 = (ResolveInfo)((Iterator)localObject1).next();
        try
        {
          ApplicationInfo localApplicationInfo = paramPackageManager.getApplicationInfo(activityInfo.packageName, 128);
          if ((privateFlags & 0x8) == 0)
          {
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append(packageName);
            ((StringBuilder)localObject3).append("is not a privileged system app");
            Slog.w("KeyphraseEnrollmentInfo", ((StringBuilder)localObject3).toString());
          }
          else if (!"android.permission.MANAGE_VOICE_KEYPHRASES".equals(permission))
          {
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append(packageName);
            ((StringBuilder)localObject3).append(" does not require MANAGE_VOICE_KEYPHRASES");
            Slog.w("KeyphraseEnrollmentInfo", ((StringBuilder)localObject3).toString());
          }
          else
          {
            localObject3 = getKeyphraseMetadataFromApplicationInfo(paramPackageManager, localApplicationInfo, localLinkedList);
            if (localObject3 != null) {
              mKeyphrasePackageMap.put(localObject3, packageName);
            }
          }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          Object localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("error parsing voice enrollment meta-data for ");
          ((StringBuilder)localObject3).append(activityInfo.packageName);
          localObject3 = ((StringBuilder)localObject3).toString();
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append((String)localObject3);
          ((StringBuilder)localObject2).append(": ");
          ((StringBuilder)localObject2).append(localNameNotFoundException);
          localLinkedList.add(((StringBuilder)localObject2).toString());
          Slog.w("KeyphraseEnrollmentInfo", (String)localObject3, localNameNotFoundException);
        }
      }
      if (mKeyphrasePackageMap.isEmpty())
      {
        localLinkedList.add("No suitable enrollment application found");
        Slog.w("KeyphraseEnrollmentInfo", "No suitable enrollment application found");
        mKeyphrases = null;
      }
      else
      {
        mKeyphrases = ((KeyphraseMetadata[])mKeyphrasePackageMap.keySet().toArray(new KeyphraseMetadata[mKeyphrasePackageMap.size()]));
      }
      if (!localLinkedList.isEmpty()) {
        mParseError = TextUtils.join("\n", localLinkedList);
      }
      return;
    }
    mParseError = "No enrollment applications found";
    mKeyphrasePackageMap = Collections.emptyMap();
    mKeyphrases = null;
  }
  
  private KeyphraseMetadata getKeyphraseFromTypedArray(TypedArray paramTypedArray, String paramString, List<String> paramList)
  {
    int i = 0;
    int j = paramTypedArray.getInt(0, -1);
    if (j <= 0)
    {
      paramTypedArray = new StringBuilder();
      paramTypedArray.append("No valid searchKeyphraseId specified in meta-data for ");
      paramTypedArray.append(paramString);
      paramTypedArray = paramTypedArray.toString();
      paramList.add(paramTypedArray);
      Slog.w("KeyphraseEnrollmentInfo", paramTypedArray);
      return null;
    }
    String str = paramTypedArray.getString(1);
    if (str == null)
    {
      paramTypedArray = new StringBuilder();
      paramTypedArray.append("No valid searchKeyphrase specified in meta-data for ");
      paramTypedArray.append(paramString);
      paramTypedArray = paramTypedArray.toString();
      paramList.add(paramTypedArray);
      Slog.w("KeyphraseEnrollmentInfo", paramTypedArray);
      return null;
    }
    Object localObject = paramTypedArray.getString(2);
    if (localObject == null)
    {
      paramTypedArray = new StringBuilder();
      paramTypedArray.append("No valid searchKeyphraseSupportedLocales specified in meta-data for ");
      paramTypedArray.append(paramString);
      paramTypedArray = paramTypedArray.toString();
      paramList.add(paramTypedArray);
      Slog.w("KeyphraseEnrollmentInfo", paramTypedArray);
      return null;
    }
    ArraySet localArraySet = new ArraySet();
    if (!TextUtils.isEmpty((CharSequence)localObject)) {
      try
      {
        localObject = ((String)localObject).split(",");
        while (i < localObject.length)
        {
          localArraySet.add(Locale.forLanguageTag(localObject[i]));
          i++;
        }
      }
      catch (Exception paramTypedArray)
      {
        paramTypedArray = new StringBuilder();
        paramTypedArray.append("Error reading searchKeyphraseSupportedLocales from meta-data for ");
        paramTypedArray.append(paramString);
        paramTypedArray = paramTypedArray.toString();
        paramList.add(paramTypedArray);
        Slog.w("KeyphraseEnrollmentInfo", paramTypedArray);
        return null;
      }
    }
    i = paramTypedArray.getInt(3, -1);
    if (i < 0)
    {
      paramTypedArray = new StringBuilder();
      paramTypedArray.append("No valid searchKeyphraseRecognitionFlags specified in meta-data for ");
      paramTypedArray.append(paramString);
      paramTypedArray = paramTypedArray.toString();
      paramList.add(paramTypedArray);
      Slog.w("KeyphraseEnrollmentInfo", paramTypedArray);
      return null;
    }
    return new KeyphraseMetadata(j, str, localArraySet, i);
  }
  
  /* Error */
  private KeyphraseMetadata getKeyphraseMetadataFromApplicationInfo(PackageManager paramPackageManager, ApplicationInfo paramApplicationInfo, List<String> paramList)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aconst_null
    //   4: astore 5
    //   6: aconst_null
    //   7: astore 6
    //   9: aconst_null
    //   10: astore 7
    //   12: aload_2
    //   13: getfield 100	android/content/pm/ApplicationInfo:packageName	Ljava/lang/String;
    //   16: astore 8
    //   18: aconst_null
    //   19: astore 9
    //   21: aconst_null
    //   22: astore 10
    //   24: aconst_null
    //   25: astore 11
    //   27: aload 11
    //   29: astore 12
    //   31: aload 9
    //   33: astore 13
    //   35: aload 10
    //   37: astore 14
    //   39: aload_2
    //   40: aload_1
    //   41: ldc 23
    //   43: invokevirtual 249	android/content/pm/ApplicationInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   46: astore 15
    //   48: aload 15
    //   50: ifnonnull +255 -> 305
    //   53: aload 15
    //   55: astore 7
    //   57: aload 15
    //   59: astore 4
    //   61: aload 11
    //   63: astore 12
    //   65: aload 15
    //   67: astore 5
    //   69: aload 9
    //   71: astore 13
    //   73: aload 15
    //   75: astore 6
    //   77: aload 10
    //   79: astore 14
    //   81: new 98	java/lang/StringBuilder
    //   84: astore_1
    //   85: aload 15
    //   87: astore 7
    //   89: aload 15
    //   91: astore 4
    //   93: aload 11
    //   95: astore 12
    //   97: aload 15
    //   99: astore 5
    //   101: aload 9
    //   103: astore 13
    //   105: aload 15
    //   107: astore 6
    //   109: aload 10
    //   111: astore 14
    //   113: aload_1
    //   114: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   117: aload 15
    //   119: astore 7
    //   121: aload 15
    //   123: astore 4
    //   125: aload 11
    //   127: astore 12
    //   129: aload 15
    //   131: astore 5
    //   133: aload 9
    //   135: astore 13
    //   137: aload 15
    //   139: astore 6
    //   141: aload 10
    //   143: astore 14
    //   145: aload_1
    //   146: ldc -5
    //   148: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: aload 15
    //   154: astore 7
    //   156: aload 15
    //   158: astore 4
    //   160: aload 11
    //   162: astore 12
    //   164: aload 15
    //   166: astore 5
    //   168: aload 9
    //   170: astore 13
    //   172: aload 15
    //   174: astore 6
    //   176: aload 10
    //   178: astore 14
    //   180: aload_1
    //   181: aload 8
    //   183: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload 15
    //   189: astore 7
    //   191: aload 15
    //   193: astore 4
    //   195: aload 11
    //   197: astore 12
    //   199: aload 15
    //   201: astore 5
    //   203: aload 9
    //   205: astore 13
    //   207: aload 15
    //   209: astore 6
    //   211: aload 10
    //   213: astore 14
    //   215: aload_1
    //   216: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   219: astore_1
    //   220: aload 15
    //   222: astore 7
    //   224: aload 15
    //   226: astore 4
    //   228: aload 11
    //   230: astore 12
    //   232: aload 15
    //   234: astore 5
    //   236: aload 9
    //   238: astore 13
    //   240: aload 15
    //   242: astore 6
    //   244: aload 10
    //   246: astore 14
    //   248: aload_3
    //   249: aload_1
    //   250: invokeinterface 149 2 0
    //   255: pop
    //   256: aload 15
    //   258: astore 7
    //   260: aload 15
    //   262: astore 4
    //   264: aload 11
    //   266: astore 12
    //   268: aload 15
    //   270: astore 5
    //   272: aload 9
    //   274: astore 13
    //   276: aload 15
    //   278: astore 6
    //   280: aload 10
    //   282: astore 14
    //   284: ldc 20
    //   286: aload_1
    //   287: invokestatic 116	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   290: pop
    //   291: aload 15
    //   293: ifnull +10 -> 303
    //   296: aload 15
    //   298: invokeinterface 256 1 0
    //   303: aconst_null
    //   304: areturn
    //   305: aload 15
    //   307: astore 7
    //   309: aload 15
    //   311: astore 4
    //   313: aload 11
    //   315: astore 12
    //   317: aload 15
    //   319: astore 5
    //   321: aload 9
    //   323: astore 13
    //   325: aload 15
    //   327: astore 6
    //   329: aload 10
    //   331: astore 14
    //   333: aload_1
    //   334: aload_2
    //   335: invokevirtual 260	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   338: astore_2
    //   339: aload 15
    //   341: astore 7
    //   343: aload 15
    //   345: astore 4
    //   347: aload 11
    //   349: astore 12
    //   351: aload 15
    //   353: astore 5
    //   355: aload 9
    //   357: astore 13
    //   359: aload 15
    //   361: astore 6
    //   363: aload 10
    //   365: astore 14
    //   367: aload 15
    //   369: invokestatic 266	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   372: astore_1
    //   373: aload 15
    //   375: astore 7
    //   377: aload 15
    //   379: astore 4
    //   381: aload 11
    //   383: astore 12
    //   385: aload 15
    //   387: astore 5
    //   389: aload 9
    //   391: astore 13
    //   393: aload 15
    //   395: astore 6
    //   397: aload 10
    //   399: astore 14
    //   401: aload 15
    //   403: invokeinterface 268 1 0
    //   408: istore 16
    //   410: iload 16
    //   412: iconst_1
    //   413: if_icmpeq +12 -> 425
    //   416: iload 16
    //   418: iconst_2
    //   419: if_icmpeq +6 -> 425
    //   422: goto -49 -> 373
    //   425: aload 15
    //   427: astore 7
    //   429: aload 15
    //   431: astore 4
    //   433: aload 11
    //   435: astore 12
    //   437: aload 15
    //   439: astore 5
    //   441: aload 9
    //   443: astore 13
    //   445: aload 15
    //   447: astore 6
    //   449: aload 10
    //   451: astore 14
    //   453: ldc_w 270
    //   456: aload 15
    //   458: invokeinterface 273 1 0
    //   463: invokevirtual 127	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   466: ifne +256 -> 722
    //   469: aload 15
    //   471: astore 7
    //   473: aload 15
    //   475: astore 4
    //   477: aload 11
    //   479: astore 12
    //   481: aload 15
    //   483: astore 5
    //   485: aload 9
    //   487: astore 13
    //   489: aload 15
    //   491: astore 6
    //   493: aload 10
    //   495: astore 14
    //   497: new 98	java/lang/StringBuilder
    //   500: astore_1
    //   501: aload 15
    //   503: astore 7
    //   505: aload 15
    //   507: astore 4
    //   509: aload 11
    //   511: astore 12
    //   513: aload 15
    //   515: astore 5
    //   517: aload 9
    //   519: astore 13
    //   521: aload 15
    //   523: astore 6
    //   525: aload 10
    //   527: astore 14
    //   529: aload_1
    //   530: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   533: aload 15
    //   535: astore 7
    //   537: aload 15
    //   539: astore 4
    //   541: aload 11
    //   543: astore 12
    //   545: aload 15
    //   547: astore 5
    //   549: aload 9
    //   551: astore 13
    //   553: aload 15
    //   555: astore 6
    //   557: aload 10
    //   559: astore 14
    //   561: aload_1
    //   562: ldc_w 275
    //   565: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   568: pop
    //   569: aload 15
    //   571: astore 7
    //   573: aload 15
    //   575: astore 4
    //   577: aload 11
    //   579: astore 12
    //   581: aload 15
    //   583: astore 5
    //   585: aload 9
    //   587: astore 13
    //   589: aload 15
    //   591: astore 6
    //   593: aload 10
    //   595: astore 14
    //   597: aload_1
    //   598: aload 8
    //   600: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   603: pop
    //   604: aload 15
    //   606: astore 7
    //   608: aload 15
    //   610: astore 4
    //   612: aload 11
    //   614: astore 12
    //   616: aload 15
    //   618: astore 5
    //   620: aload 9
    //   622: astore 13
    //   624: aload 15
    //   626: astore 6
    //   628: aload 10
    //   630: astore 14
    //   632: aload_1
    //   633: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   636: astore_1
    //   637: aload 15
    //   639: astore 7
    //   641: aload 15
    //   643: astore 4
    //   645: aload 11
    //   647: astore 12
    //   649: aload 15
    //   651: astore 5
    //   653: aload 9
    //   655: astore 13
    //   657: aload 15
    //   659: astore 6
    //   661: aload 10
    //   663: astore 14
    //   665: aload_3
    //   666: aload_1
    //   667: invokeinterface 149 2 0
    //   672: pop
    //   673: aload 15
    //   675: astore 7
    //   677: aload 15
    //   679: astore 4
    //   681: aload 11
    //   683: astore 12
    //   685: aload 15
    //   687: astore 5
    //   689: aload 9
    //   691: astore 13
    //   693: aload 15
    //   695: astore 6
    //   697: aload 10
    //   699: astore 14
    //   701: ldc 20
    //   703: aload_1
    //   704: invokestatic 116	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   707: pop
    //   708: aload 15
    //   710: ifnull +10 -> 720
    //   713: aload 15
    //   715: invokeinterface 256 1 0
    //   720: aconst_null
    //   721: areturn
    //   722: aload 15
    //   724: astore 7
    //   726: aload 15
    //   728: astore 4
    //   730: aload 11
    //   732: astore 12
    //   734: aload 15
    //   736: astore 5
    //   738: aload 9
    //   740: astore 13
    //   742: aload 15
    //   744: astore 6
    //   746: aload 10
    //   748: astore 14
    //   750: aload_2
    //   751: aload_1
    //   752: getstatic 281	com/android/internal/R$styleable:VoiceEnrollmentApplication	[I
    //   755: invokevirtual 287	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   758: astore_2
    //   759: aload 15
    //   761: astore 7
    //   763: aload 15
    //   765: astore 4
    //   767: aload 11
    //   769: astore 12
    //   771: aload 15
    //   773: astore 5
    //   775: aload 9
    //   777: astore 13
    //   779: aload 15
    //   781: astore 6
    //   783: aload 10
    //   785: astore 14
    //   787: aload_0
    //   788: aload_2
    //   789: aload 8
    //   791: aload_3
    //   792: invokespecial 289	android/hardware/soundtrigger/KeyphraseEnrollmentInfo:getKeyphraseFromTypedArray	(Landroid/content/res/TypedArray;Ljava/lang/String;Ljava/util/List;)Landroid/hardware/soundtrigger/KeyphraseMetadata;
    //   795: astore_1
    //   796: aload 15
    //   798: astore 7
    //   800: aload 15
    //   802: astore 4
    //   804: aload_1
    //   805: astore 12
    //   807: aload 15
    //   809: astore 5
    //   811: aload_1
    //   812: astore 13
    //   814: aload 15
    //   816: astore 6
    //   818: aload_1
    //   819: astore 14
    //   821: aload_2
    //   822: invokevirtual 292	android/content/res/TypedArray:recycle	()V
    //   825: aload_1
    //   826: astore_2
    //   827: aload 15
    //   829: ifnull +460 -> 1289
    //   832: aload 15
    //   834: invokeinterface 256 1 0
    //   839: aload_1
    //   840: astore_2
    //   841: goto +448 -> 1289
    //   844: astore_1
    //   845: goto +446 -> 1291
    //   848: astore_1
    //   849: aload 4
    //   851: astore 7
    //   853: new 98	java/lang/StringBuilder
    //   856: astore_2
    //   857: aload 4
    //   859: astore 7
    //   861: aload_2
    //   862: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   865: aload 4
    //   867: astore 7
    //   869: aload_2
    //   870: ldc_w 294
    //   873: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   876: pop
    //   877: aload 4
    //   879: astore 7
    //   881: aload_2
    //   882: aload 8
    //   884: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   887: pop
    //   888: aload 4
    //   890: astore 7
    //   892: aload_2
    //   893: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   896: astore_2
    //   897: aload 4
    //   899: astore 7
    //   901: new 98	java/lang/StringBuilder
    //   904: astore 15
    //   906: aload 4
    //   908: astore 7
    //   910: aload 15
    //   912: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   915: aload 4
    //   917: astore 7
    //   919: aload 15
    //   921: aload_2
    //   922: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   925: pop
    //   926: aload 4
    //   928: astore 7
    //   930: aload 15
    //   932: ldc -113
    //   934: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   937: pop
    //   938: aload 4
    //   940: astore 7
    //   942: aload 15
    //   944: aload_1
    //   945: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   948: pop
    //   949: aload 4
    //   951: astore 7
    //   953: aload_3
    //   954: aload 15
    //   956: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   959: invokeinterface 149 2 0
    //   964: pop
    //   965: aload 4
    //   967: astore 7
    //   969: ldc 20
    //   971: aload_2
    //   972: aload_1
    //   973: invokestatic 152	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   976: pop
    //   977: aload 12
    //   979: astore_2
    //   980: aload 4
    //   982: ifnull +307 -> 1289
    //   985: aload 4
    //   987: astore 15
    //   989: aload 12
    //   991: astore_1
    //   992: goto -160 -> 832
    //   995: astore_1
    //   996: aload 5
    //   998: astore 7
    //   1000: new 98	java/lang/StringBuilder
    //   1003: astore_2
    //   1004: aload 5
    //   1006: astore 7
    //   1008: aload_2
    //   1009: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   1012: aload 5
    //   1014: astore 7
    //   1016: aload_2
    //   1017: ldc_w 294
    //   1020: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1023: pop
    //   1024: aload 5
    //   1026: astore 7
    //   1028: aload_2
    //   1029: aload 8
    //   1031: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1034: pop
    //   1035: aload 5
    //   1037: astore 7
    //   1039: aload_2
    //   1040: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1043: astore_2
    //   1044: aload 5
    //   1046: astore 7
    //   1048: new 98	java/lang/StringBuilder
    //   1051: astore 15
    //   1053: aload 5
    //   1055: astore 7
    //   1057: aload 15
    //   1059: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   1062: aload 5
    //   1064: astore 7
    //   1066: aload 15
    //   1068: aload_2
    //   1069: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1072: pop
    //   1073: aload 5
    //   1075: astore 7
    //   1077: aload 15
    //   1079: ldc -113
    //   1081: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1084: pop
    //   1085: aload 5
    //   1087: astore 7
    //   1089: aload 15
    //   1091: aload_1
    //   1092: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1095: pop
    //   1096: aload 5
    //   1098: astore 7
    //   1100: aload_3
    //   1101: aload 15
    //   1103: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1106: invokeinterface 149 2 0
    //   1111: pop
    //   1112: aload 5
    //   1114: astore 7
    //   1116: ldc 20
    //   1118: aload_2
    //   1119: aload_1
    //   1120: invokestatic 152	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1123: pop
    //   1124: aload 13
    //   1126: astore_2
    //   1127: aload 5
    //   1129: ifnull +160 -> 1289
    //   1132: aload 5
    //   1134: astore 15
    //   1136: aload 13
    //   1138: astore_1
    //   1139: goto -307 -> 832
    //   1142: astore_1
    //   1143: aload 6
    //   1145: astore 7
    //   1147: new 98	java/lang/StringBuilder
    //   1150: astore_2
    //   1151: aload 6
    //   1153: astore 7
    //   1155: aload_2
    //   1156: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   1159: aload 6
    //   1161: astore 7
    //   1163: aload_2
    //   1164: ldc_w 294
    //   1167: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1170: pop
    //   1171: aload 6
    //   1173: astore 7
    //   1175: aload_2
    //   1176: aload 8
    //   1178: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1181: pop
    //   1182: aload 6
    //   1184: astore 7
    //   1186: aload_2
    //   1187: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1190: astore_2
    //   1191: aload 6
    //   1193: astore 7
    //   1195: new 98	java/lang/StringBuilder
    //   1198: astore 15
    //   1200: aload 6
    //   1202: astore 7
    //   1204: aload 15
    //   1206: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   1209: aload 6
    //   1211: astore 7
    //   1213: aload 15
    //   1215: aload_2
    //   1216: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1219: pop
    //   1220: aload 6
    //   1222: astore 7
    //   1224: aload 15
    //   1226: ldc -113
    //   1228: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1231: pop
    //   1232: aload 6
    //   1234: astore 7
    //   1236: aload 15
    //   1238: aload_1
    //   1239: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1242: pop
    //   1243: aload 6
    //   1245: astore 7
    //   1247: aload_3
    //   1248: aload 15
    //   1250: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1253: invokeinterface 149 2 0
    //   1258: pop
    //   1259: aload 6
    //   1261: astore 7
    //   1263: ldc 20
    //   1265: aload_2
    //   1266: aload_1
    //   1267: invokestatic 152	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1270: pop
    //   1271: aload 14
    //   1273: astore_2
    //   1274: aload 6
    //   1276: ifnull +13 -> 1289
    //   1279: aload 6
    //   1281: astore 15
    //   1283: aload 14
    //   1285: astore_1
    //   1286: goto -454 -> 832
    //   1289: aload_2
    //   1290: areturn
    //   1291: aload 7
    //   1293: ifnull +10 -> 1303
    //   1296: aload 7
    //   1298: invokeinterface 256 1 0
    //   1303: aload_1
    //   1304: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1305	0	this	KeyphraseEnrollmentInfo
    //   0	1305	1	paramPackageManager	PackageManager
    //   0	1305	2	paramApplicationInfo	ApplicationInfo
    //   0	1305	3	paramList	List<String>
    //   1	985	4	localObject1	Object
    //   4	1129	5	localObject2	Object
    //   7	1273	6	localObject3	Object
    //   10	1287	7	localObject4	Object
    //   16	1161	8	str	String
    //   19	757	9	localObject5	Object
    //   22	762	10	localObject6	Object
    //   25	743	11	localObject7	Object
    //   29	961	12	localObject8	Object
    //   33	1104	13	localObject9	Object
    //   37	1247	14	localObject10	Object
    //   46	1236	15	localObject11	Object
    //   408	12	16	i	int
    // Exception table:
    //   from	to	target	type
    //   39	48	844	finally
    //   81	85	844	finally
    //   113	117	844	finally
    //   145	152	844	finally
    //   180	187	844	finally
    //   215	220	844	finally
    //   248	256	844	finally
    //   284	291	844	finally
    //   333	339	844	finally
    //   367	373	844	finally
    //   401	410	844	finally
    //   453	469	844	finally
    //   497	501	844	finally
    //   529	533	844	finally
    //   561	569	844	finally
    //   597	604	844	finally
    //   632	637	844	finally
    //   665	673	844	finally
    //   701	708	844	finally
    //   750	759	844	finally
    //   787	796	844	finally
    //   821	825	844	finally
    //   853	857	844	finally
    //   861	865	844	finally
    //   869	877	844	finally
    //   881	888	844	finally
    //   892	897	844	finally
    //   901	906	844	finally
    //   910	915	844	finally
    //   919	926	844	finally
    //   930	938	844	finally
    //   942	949	844	finally
    //   953	965	844	finally
    //   969	977	844	finally
    //   1000	1004	844	finally
    //   1008	1012	844	finally
    //   1016	1024	844	finally
    //   1028	1035	844	finally
    //   1039	1044	844	finally
    //   1048	1053	844	finally
    //   1057	1062	844	finally
    //   1066	1073	844	finally
    //   1077	1085	844	finally
    //   1089	1096	844	finally
    //   1100	1112	844	finally
    //   1116	1124	844	finally
    //   1147	1151	844	finally
    //   1155	1159	844	finally
    //   1163	1171	844	finally
    //   1175	1182	844	finally
    //   1186	1191	844	finally
    //   1195	1200	844	finally
    //   1204	1209	844	finally
    //   1213	1220	844	finally
    //   1224	1232	844	finally
    //   1236	1243	844	finally
    //   1247	1259	844	finally
    //   1263	1271	844	finally
    //   39	48	848	android/content/pm/PackageManager$NameNotFoundException
    //   81	85	848	android/content/pm/PackageManager$NameNotFoundException
    //   113	117	848	android/content/pm/PackageManager$NameNotFoundException
    //   145	152	848	android/content/pm/PackageManager$NameNotFoundException
    //   180	187	848	android/content/pm/PackageManager$NameNotFoundException
    //   215	220	848	android/content/pm/PackageManager$NameNotFoundException
    //   248	256	848	android/content/pm/PackageManager$NameNotFoundException
    //   284	291	848	android/content/pm/PackageManager$NameNotFoundException
    //   333	339	848	android/content/pm/PackageManager$NameNotFoundException
    //   367	373	848	android/content/pm/PackageManager$NameNotFoundException
    //   401	410	848	android/content/pm/PackageManager$NameNotFoundException
    //   453	469	848	android/content/pm/PackageManager$NameNotFoundException
    //   497	501	848	android/content/pm/PackageManager$NameNotFoundException
    //   529	533	848	android/content/pm/PackageManager$NameNotFoundException
    //   561	569	848	android/content/pm/PackageManager$NameNotFoundException
    //   597	604	848	android/content/pm/PackageManager$NameNotFoundException
    //   632	637	848	android/content/pm/PackageManager$NameNotFoundException
    //   665	673	848	android/content/pm/PackageManager$NameNotFoundException
    //   701	708	848	android/content/pm/PackageManager$NameNotFoundException
    //   750	759	848	android/content/pm/PackageManager$NameNotFoundException
    //   787	796	848	android/content/pm/PackageManager$NameNotFoundException
    //   821	825	848	android/content/pm/PackageManager$NameNotFoundException
    //   39	48	995	java/io/IOException
    //   81	85	995	java/io/IOException
    //   113	117	995	java/io/IOException
    //   145	152	995	java/io/IOException
    //   180	187	995	java/io/IOException
    //   215	220	995	java/io/IOException
    //   248	256	995	java/io/IOException
    //   284	291	995	java/io/IOException
    //   333	339	995	java/io/IOException
    //   367	373	995	java/io/IOException
    //   401	410	995	java/io/IOException
    //   453	469	995	java/io/IOException
    //   497	501	995	java/io/IOException
    //   529	533	995	java/io/IOException
    //   561	569	995	java/io/IOException
    //   597	604	995	java/io/IOException
    //   632	637	995	java/io/IOException
    //   665	673	995	java/io/IOException
    //   701	708	995	java/io/IOException
    //   750	759	995	java/io/IOException
    //   787	796	995	java/io/IOException
    //   821	825	995	java/io/IOException
    //   39	48	1142	org/xmlpull/v1/XmlPullParserException
    //   81	85	1142	org/xmlpull/v1/XmlPullParserException
    //   113	117	1142	org/xmlpull/v1/XmlPullParserException
    //   145	152	1142	org/xmlpull/v1/XmlPullParserException
    //   180	187	1142	org/xmlpull/v1/XmlPullParserException
    //   215	220	1142	org/xmlpull/v1/XmlPullParserException
    //   248	256	1142	org/xmlpull/v1/XmlPullParserException
    //   284	291	1142	org/xmlpull/v1/XmlPullParserException
    //   333	339	1142	org/xmlpull/v1/XmlPullParserException
    //   367	373	1142	org/xmlpull/v1/XmlPullParserException
    //   401	410	1142	org/xmlpull/v1/XmlPullParserException
    //   453	469	1142	org/xmlpull/v1/XmlPullParserException
    //   497	501	1142	org/xmlpull/v1/XmlPullParserException
    //   529	533	1142	org/xmlpull/v1/XmlPullParserException
    //   561	569	1142	org/xmlpull/v1/XmlPullParserException
    //   597	604	1142	org/xmlpull/v1/XmlPullParserException
    //   632	637	1142	org/xmlpull/v1/XmlPullParserException
    //   665	673	1142	org/xmlpull/v1/XmlPullParserException
    //   701	708	1142	org/xmlpull/v1/XmlPullParserException
    //   750	759	1142	org/xmlpull/v1/XmlPullParserException
    //   787	796	1142	org/xmlpull/v1/XmlPullParserException
    //   821	825	1142	org/xmlpull/v1/XmlPullParserException
  }
  
  public KeyphraseMetadata getKeyphraseMetadata(String paramString, Locale paramLocale)
  {
    if ((mKeyphrases != null) && (mKeyphrases.length > 0)) {
      for (localObject : mKeyphrases) {
        if ((((KeyphraseMetadata)localObject).supportsPhrase(paramString)) && (((KeyphraseMetadata)localObject).supportsLocale(paramLocale))) {
          return localObject;
        }
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("No enrollment application supports the given keyphrase/locale: '");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("'/");
    ((StringBuilder)localObject).append(paramLocale);
    Slog.w("KeyphraseEnrollmentInfo", ((StringBuilder)localObject).toString());
    return null;
  }
  
  public Intent getManageKeyphraseIntent(int paramInt, String paramString, Locale paramLocale)
  {
    if ((mKeyphrasePackageMap != null) && (!mKeyphrasePackageMap.isEmpty()))
    {
      KeyphraseMetadata localKeyphraseMetadata = getKeyphraseMetadata(paramString, paramLocale);
      if (localKeyphraseMetadata != null) {
        return new Intent("com.android.intent.action.MANAGE_VOICE_KEYPHRASES").setPackage((String)mKeyphrasePackageMap.get(localKeyphraseMetadata)).putExtra("com.android.intent.extra.VOICE_KEYPHRASE_HINT_TEXT", paramString).putExtra("com.android.intent.extra.VOICE_KEYPHRASE_LOCALE", paramLocale.toLanguageTag()).putExtra("com.android.intent.extra.VOICE_KEYPHRASE_ACTION", paramInt);
      }
      return null;
    }
    Slog.w("KeyphraseEnrollmentInfo", "No enrollment application exists");
    return null;
  }
  
  public String getParseError()
  {
    return mParseError;
  }
  
  public KeyphraseMetadata[] listKeyphraseMetadata()
  {
    return mKeyphrases;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("KeyphraseEnrollmentInfo [Keyphrases=");
    localStringBuilder.append(mKeyphrasePackageMap.toString());
    localStringBuilder.append(", ParseError=");
    localStringBuilder.append(mParseError);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
