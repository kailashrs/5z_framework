package com.android.internal.telephony;

import android.app.AppGlobals;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings.Global;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.util.AtomicFile;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SmsUsageMonitor
{
  private static final String ATTR_COUNTRY = "country";
  private static final String ATTR_FREE = "free";
  private static final String ATTR_PACKAGE_NAME = "name";
  private static final String ATTR_PACKAGE_SMS_POLICY = "sms-policy";
  private static final String ATTR_PATTERN = "pattern";
  private static final String ATTR_PREMIUM = "premium";
  private static final String ATTR_STANDARD = "standard";
  static final int CATEGORY_FREE_SHORT_CODE = 1;
  static final int CATEGORY_NOT_SHORT_CODE = 0;
  public static final int CATEGORY_POSSIBLE_PREMIUM_SHORT_CODE = 3;
  static final int CATEGORY_PREMIUM_SHORT_CODE = 4;
  static final int CATEGORY_STANDARD_SHORT_CODE = 2;
  private static final boolean DBG = false;
  private static final int DEFAULT_SMS_CHECK_PERIOD = 30000;
  private static final int DEFAULT_SMS_MAX_COUNT = 100;
  static final int ERROR_CODE_BLOCKED = 191286;
  public static final int PREMIUM_SMS_PERMISSION_ALWAYS_ALLOW = 3;
  public static final int PREMIUM_SMS_PERMISSION_ASK_USER = 1;
  public static final int PREMIUM_SMS_PERMISSION_NEVER_ALLOW = 2;
  public static final int PREMIUM_SMS_PERMISSION_UNKNOWN = 0;
  private static final String SHORT_CODE_PATH = "/data/misc/sms/codes";
  private static final String SMS_POLICY_FILE_DIRECTORY = "/data/misc/sms";
  private static final String SMS_POLICY_FILE_NAME = "premium_sms_policy.xml";
  private static final String TAG = "SmsUsageMonitor";
  private static final String TAG_PACKAGE = "package";
  private static final String TAG_SHORTCODE = "shortcode";
  private static final String TAG_SHORTCODES = "shortcodes";
  private static final String TAG_SMS_POLICY_BODY = "premium-sms-policy";
  private static final boolean VDBG = false;
  private final AtomicBoolean mCheckEnabled = new AtomicBoolean(true);
  private final int mCheckPeriod;
  private final Context mContext;
  private String mCurrentCountry;
  private ShortCodePatternMatcher mCurrentPatternMatcher;
  private final int mMaxAllowed;
  private final File mPatternFile = new File("/data/misc/sms/codes");
  private long mPatternFileLastModified = 0L;
  private AtomicFile mPolicyFile;
  private final HashMap<String, Integer> mPremiumSmsPolicy = new HashMap();
  private final SettingsObserverHandler mSettingsObserverHandler;
  private final HashMap<String, ArrayList<Long>> mSmsStamp = new HashMap();
  
  public SmsUsageMonitor(Context paramContext)
  {
    mContext = paramContext;
    paramContext = paramContext.getContentResolver();
    mMaxAllowed = Settings.Global.getInt(paramContext, "sms_outgoing_check_max_count", 100);
    mCheckPeriod = Settings.Global.getInt(paramContext, "sms_outgoing_check_interval_ms", 30000);
    mSettingsObserverHandler = new SettingsObserverHandler(mContext, mCheckEnabled);
    loadPremiumSmsPolicyDb();
  }
  
  private static void checkCallerIsSystemOrPhoneApp()
  {
    int i = Binder.getCallingUid();
    int j = UserHandle.getAppId(i);
    if ((j != 1000) && (j != 1001) && (i != 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Disallowed call for uid ");
      localStringBuilder.append(i);
      throw new SecurityException(localStringBuilder.toString());
    }
  }
  
  private static void checkCallerIsSystemOrPhoneOrSameApp(String paramString)
  {
    int i = Binder.getCallingUid();
    int j = UserHandle.getAppId(i);
    if ((j != 1000) && (j != 1001) && (i != 0)) {
      try
      {
        ApplicationInfo localApplicationInfo = AppGlobals.getPackageManager().getApplicationInfo(paramString, 0, UserHandle.getCallingUserId());
        if (UserHandle.isSameApp(uid, i)) {
          return;
        }
        SecurityException localSecurityException = new java/lang/SecurityException;
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Calling uid ");
        localStringBuilder.append(i);
        localStringBuilder.append(" gave package");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" which is owned by uid ");
        localStringBuilder.append(uid);
        localSecurityException.<init>(localStringBuilder.toString());
        throw localSecurityException;
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown package ");
        localStringBuilder.append(paramString);
        localStringBuilder.append("\n");
        localStringBuilder.append(localRemoteException);
        throw new SecurityException(localStringBuilder.toString());
      }
    }
  }
  
  /* Error */
  private ShortCodePatternMatcher getPatternMatcherFromFile(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aload 4
    //   9: astore 5
    //   11: aload_2
    //   12: astore 6
    //   14: aload_3
    //   15: astore 7
    //   17: new 256	java/io/FileReader
    //   20: astore 8
    //   22: aload 4
    //   24: astore 5
    //   26: aload_2
    //   27: astore 6
    //   29: aload_3
    //   30: astore 7
    //   32: aload 8
    //   34: aload_0
    //   35: getfield 134	com/android/internal/telephony/SmsUsageMonitor:mPatternFile	Ljava/io/File;
    //   38: invokespecial 259	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   41: aload 8
    //   43: astore 5
    //   45: aload 8
    //   47: astore 6
    //   49: aload 8
    //   51: astore 7
    //   53: invokestatic 265	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   56: astore 4
    //   58: aload 8
    //   60: astore 5
    //   62: aload 8
    //   64: astore 6
    //   66: aload 8
    //   68: astore 7
    //   70: aload 4
    //   72: aload 8
    //   74: invokeinterface 271 2 0
    //   79: aload 8
    //   81: astore 5
    //   83: aload 8
    //   85: astore 6
    //   87: aload 8
    //   89: astore 7
    //   91: aload_0
    //   92: aload 4
    //   94: aload_1
    //   95: invokespecial 275	com/android/internal/telephony/SmsUsageMonitor:getPatternMatcherFromXmlParser	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)Lcom/android/internal/telephony/SmsUsageMonitor$ShortCodePatternMatcher;
    //   98: astore_1
    //   99: aload_0
    //   100: aload_0
    //   101: getfield 134	com/android/internal/telephony/SmsUsageMonitor:mPatternFile	Ljava/io/File;
    //   104: invokevirtual 279	java/io/File:lastModified	()J
    //   107: putfield 136	com/android/internal/telephony/SmsUsageMonitor:mPatternFileLastModified	J
    //   110: aload 8
    //   112: invokevirtual 282	java/io/FileReader:close	()V
    //   115: goto +5 -> 120
    //   118: astore 5
    //   120: aload_1
    //   121: areturn
    //   122: astore_1
    //   123: goto +85 -> 208
    //   126: astore_1
    //   127: aload 6
    //   129: astore 5
    //   131: ldc 75
    //   133: ldc_w 284
    //   136: aload_1
    //   137: invokestatic 290	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   140: pop
    //   141: aload_0
    //   142: aload_0
    //   143: getfield 134	com/android/internal/telephony/SmsUsageMonitor:mPatternFile	Ljava/io/File;
    //   146: invokevirtual 279	java/io/File:lastModified	()J
    //   149: putfield 136	com/android/internal/telephony/SmsUsageMonitor:mPatternFileLastModified	J
    //   152: aload 6
    //   154: ifnull +52 -> 206
    //   157: aload 6
    //   159: astore 7
    //   161: aload 7
    //   163: invokevirtual 282	java/io/FileReader:close	()V
    //   166: goto +40 -> 206
    //   169: astore_1
    //   170: goto +36 -> 206
    //   173: astore_1
    //   174: aload 7
    //   176: astore 5
    //   178: ldc 75
    //   180: ldc_w 292
    //   183: invokestatic 295	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   186: pop
    //   187: aload_0
    //   188: aload_0
    //   189: getfield 134	com/android/internal/telephony/SmsUsageMonitor:mPatternFile	Ljava/io/File;
    //   192: invokevirtual 279	java/io/File:lastModified	()J
    //   195: putfield 136	com/android/internal/telephony/SmsUsageMonitor:mPatternFileLastModified	J
    //   198: aload 7
    //   200: ifnull +6 -> 206
    //   203: goto -42 -> 161
    //   206: aconst_null
    //   207: areturn
    //   208: aload_0
    //   209: aload_0
    //   210: getfield 134	com/android/internal/telephony/SmsUsageMonitor:mPatternFile	Ljava/io/File;
    //   213: invokevirtual 279	java/io/File:lastModified	()J
    //   216: putfield 136	com/android/internal/telephony/SmsUsageMonitor:mPatternFileLastModified	J
    //   219: aload 5
    //   221: ifnull +13 -> 234
    //   224: aload 5
    //   226: invokevirtual 282	java/io/FileReader:close	()V
    //   229: goto +5 -> 234
    //   232: astore 5
    //   234: aload_1
    //   235: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	236	0	this	SmsUsageMonitor
    //   0	236	1	paramString	String
    //   1	26	2	localObject1	Object
    //   3	27	3	localObject2	Object
    //   5	88	4	localXmlPullParser	XmlPullParser
    //   9	73	5	localObject3	Object
    //   118	1	5	localIOException1	IOException
    //   129	96	5	localObject4	Object
    //   232	1	5	localIOException2	IOException
    //   12	146	6	localObject5	Object
    //   15	184	7	localObject6	Object
    //   20	91	8	localFileReader	java.io.FileReader
    // Exception table:
    //   from	to	target	type
    //   110	115	118	java/io/IOException
    //   17	22	122	finally
    //   32	41	122	finally
    //   53	58	122	finally
    //   70	79	122	finally
    //   91	99	122	finally
    //   131	141	122	finally
    //   178	187	122	finally
    //   17	22	126	org/xmlpull/v1/XmlPullParserException
    //   32	41	126	org/xmlpull/v1/XmlPullParserException
    //   53	58	126	org/xmlpull/v1/XmlPullParserException
    //   70	79	126	org/xmlpull/v1/XmlPullParserException
    //   91	99	126	org/xmlpull/v1/XmlPullParserException
    //   161	166	169	java/io/IOException
    //   17	22	173	java/io/FileNotFoundException
    //   32	41	173	java/io/FileNotFoundException
    //   53	58	173	java/io/FileNotFoundException
    //   70	79	173	java/io/FileNotFoundException
    //   91	99	173	java/io/FileNotFoundException
    //   224	229	232	java/io/IOException
  }
  
  private ShortCodePatternMatcher getPatternMatcherFromResource(String paramString)
  {
    Object localObject = null;
    try
    {
      XmlResourceParser localXmlResourceParser = mContext.getResources().getXml(18284567);
      localObject = localXmlResourceParser;
      paramString = getPatternMatcherFromXmlParser(localXmlResourceParser, paramString);
      return paramString;
    }
    finally
    {
      if (localObject != null) {
        localObject.close();
      }
    }
  }
  
  private ShortCodePatternMatcher getPatternMatcherFromXmlParser(XmlPullParser paramXmlPullParser, String paramString)
  {
    try
    {
      XmlUtils.beginDocument(paramXmlPullParser, "shortcodes");
      for (;;)
      {
        XmlUtils.nextElement(paramXmlPullParser);
        String str = paramXmlPullParser.getName();
        if (str == null)
        {
          Rlog.e("SmsUsageMonitor", "Parsing pattern data found null");
          break;
        }
        if (str.equals("shortcode"))
        {
          if (paramString.equals(paramXmlPullParser.getAttributeValue(null, "country"))) {
            return new ShortCodePatternMatcher(paramXmlPullParser.getAttributeValue(null, "pattern"), paramXmlPullParser.getAttributeValue(null, "premium"), paramXmlPullParser.getAttributeValue(null, "free"), paramXmlPullParser.getAttributeValue(null, "standard"));
          }
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Error: skipping unknown XML tag ");
          localStringBuilder.append(str);
          Rlog.e("SmsUsageMonitor", localStringBuilder.toString());
        }
      }
      return null;
    }
    catch (IOException paramXmlPullParser)
    {
      Rlog.e("SmsUsageMonitor", "I/O exception reading short code patterns", paramXmlPullParser);
    }
    catch (XmlPullParserException paramXmlPullParser)
    {
      Rlog.e("SmsUsageMonitor", "XML parser exception reading short code patterns", paramXmlPullParser);
    }
  }
  
  private boolean isUnderLimit(ArrayList<Long> paramArrayList, int paramInt)
  {
    Long localLong = Long.valueOf(System.currentTimeMillis());
    long l1 = localLong.longValue();
    long l2 = mCheckPeriod;
    int i;
    for (;;)
    {
      boolean bool = paramArrayList.isEmpty();
      i = 0;
      if ((bool) || (((Long)paramArrayList.get(0)).longValue() >= l1 - l2)) {
        break;
      }
      paramArrayList.remove(0);
    }
    if (paramArrayList.size() + paramInt <= mMaxAllowed)
    {
      while (i < paramInt)
      {
        paramArrayList.add(localLong);
        i++;
      }
      return true;
    }
    return false;
  }
  
  /* Error */
  private void loadPremiumSmsPolicyDb()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 138	com/android/internal/telephony/SmsUsageMonitor:mPremiumSmsPolicy	Ljava/util/HashMap;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 385	com/android/internal/telephony/SmsUsageMonitor:mPolicyFile	Landroid/util/AtomicFile;
    //   11: ifnonnull +706 -> 717
    //   14: new 129	java/io/File
    //   17: astore_2
    //   18: aload_2
    //   19: ldc 69
    //   21: invokespecial 132	java/io/File:<init>	(Ljava/lang/String;)V
    //   24: new 387	android/util/AtomicFile
    //   27: astore_3
    //   28: new 129	java/io/File
    //   31: astore 4
    //   33: aload 4
    //   35: aload_2
    //   36: ldc 72
    //   38: invokespecial 390	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   41: aload_3
    //   42: aload 4
    //   44: invokespecial 391	android/util/AtomicFile:<init>	(Ljava/io/File;)V
    //   47: aload_0
    //   48: aload_3
    //   49: putfield 385	com/android/internal/telephony/SmsUsageMonitor:mPolicyFile	Landroid/util/AtomicFile;
    //   52: aload_0
    //   53: getfield 138	com/android/internal/telephony/SmsUsageMonitor:mPremiumSmsPolicy	Ljava/util/HashMap;
    //   56: invokevirtual 394	java/util/HashMap:clear	()V
    //   59: aconst_null
    //   60: astore 4
    //   62: aconst_null
    //   63: astore 5
    //   65: aconst_null
    //   66: astore 6
    //   68: aconst_null
    //   69: astore 7
    //   71: aconst_null
    //   72: astore_3
    //   73: aload_0
    //   74: getfield 385	com/android/internal/telephony/SmsUsageMonitor:mPolicyFile	Landroid/util/AtomicFile;
    //   77: invokevirtual 398	android/util/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   80: astore_2
    //   81: aload_2
    //   82: astore_3
    //   83: aload_2
    //   84: astore 4
    //   86: aload_2
    //   87: astore 5
    //   89: aload_2
    //   90: astore 6
    //   92: aload_2
    //   93: astore 7
    //   95: invokestatic 265	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   98: astore 8
    //   100: aload_2
    //   101: astore_3
    //   102: aload_2
    //   103: astore 4
    //   105: aload_2
    //   106: astore 5
    //   108: aload_2
    //   109: astore 6
    //   111: aload_2
    //   112: astore 7
    //   114: aload 8
    //   116: aload_2
    //   117: getstatic 404	java/nio/charset/StandardCharsets:UTF_8	Ljava/nio/charset/Charset;
    //   120: invokevirtual 408	java/nio/charset/Charset:name	()Ljava/lang/String;
    //   123: invokeinterface 411 3 0
    //   128: aload_2
    //   129: astore_3
    //   130: aload_2
    //   131: astore 4
    //   133: aload_2
    //   134: astore 5
    //   136: aload_2
    //   137: astore 6
    //   139: aload_2
    //   140: astore 7
    //   142: aload 8
    //   144: ldc 87
    //   146: invokestatic 316	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   149: aload_2
    //   150: astore_3
    //   151: aload_2
    //   152: astore 4
    //   154: aload_2
    //   155: astore 5
    //   157: aload_2
    //   158: astore 6
    //   160: aload_2
    //   161: astore 7
    //   163: aload 8
    //   165: invokestatic 320	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   168: aload_2
    //   169: astore_3
    //   170: aload_2
    //   171: astore 4
    //   173: aload_2
    //   174: astore 5
    //   176: aload_2
    //   177: astore 6
    //   179: aload_2
    //   180: astore 7
    //   182: aload 8
    //   184: invokeinterface 323 1 0
    //   189: astore 9
    //   191: aload 9
    //   193: ifnonnull +14 -> 207
    //   196: aload_2
    //   197: ifnull +520 -> 717
    //   200: aload_2
    //   201: invokevirtual 414	java/io/FileInputStream:close	()V
    //   204: goto +506 -> 710
    //   207: aload_2
    //   208: astore_3
    //   209: aload_2
    //   210: astore 4
    //   212: aload_2
    //   213: astore 5
    //   215: aload_2
    //   216: astore 6
    //   218: aload_2
    //   219: astore 7
    //   221: aload 9
    //   223: ldc 78
    //   225: invokevirtual 331	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   228: ifeq +261 -> 489
    //   231: aload_2
    //   232: astore_3
    //   233: aload_2
    //   234: astore 4
    //   236: aload_2
    //   237: astore 5
    //   239: aload_2
    //   240: astore 6
    //   242: aload_2
    //   243: astore 7
    //   245: aload 8
    //   247: aconst_null
    //   248: ldc 28
    //   250: invokeinterface 335 3 0
    //   255: astore 9
    //   257: aload_2
    //   258: astore_3
    //   259: aload_2
    //   260: astore 4
    //   262: aload_2
    //   263: astore 5
    //   265: aload_2
    //   266: astore 6
    //   268: aload_2
    //   269: astore 7
    //   271: aload 8
    //   273: aconst_null
    //   274: ldc 31
    //   276: invokeinterface 335 3 0
    //   281: astore 10
    //   283: aload 9
    //   285: ifnonnull +29 -> 314
    //   288: aload_2
    //   289: astore_3
    //   290: aload_2
    //   291: astore 4
    //   293: aload_2
    //   294: astore 5
    //   296: aload_2
    //   297: astore 6
    //   299: aload_2
    //   300: astore 7
    //   302: ldc 75
    //   304: ldc_w 416
    //   307: invokestatic 295	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   310: pop
    //   311: goto +175 -> 486
    //   314: aload 10
    //   316: ifnonnull +29 -> 345
    //   319: aload_2
    //   320: astore_3
    //   321: aload_2
    //   322: astore 4
    //   324: aload_2
    //   325: astore 5
    //   327: aload_2
    //   328: astore 6
    //   330: aload_2
    //   331: astore 7
    //   333: ldc 75
    //   335: ldc_w 418
    //   338: invokestatic 295	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   341: pop
    //   342: goto +144 -> 486
    //   345: aload_2
    //   346: astore_3
    //   347: aload_2
    //   348: astore 4
    //   350: aload_2
    //   351: astore 6
    //   353: aload_2
    //   354: astore 7
    //   356: aload_0
    //   357: getfield 138	com/android/internal/telephony/SmsUsageMonitor:mPremiumSmsPolicy	Ljava/util/HashMap;
    //   360: aload 9
    //   362: aload 10
    //   364: invokestatic 424	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   367: invokestatic 427	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   370: invokevirtual 431	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   373: pop
    //   374: goto +112 -> 486
    //   377: astore_3
    //   378: aload_2
    //   379: astore_3
    //   380: aload_2
    //   381: astore 4
    //   383: aload_2
    //   384: astore 5
    //   386: aload_2
    //   387: astore 6
    //   389: aload_2
    //   390: astore 7
    //   392: new 189	java/lang/StringBuilder
    //   395: astore 9
    //   397: aload_2
    //   398: astore_3
    //   399: aload_2
    //   400: astore 4
    //   402: aload_2
    //   403: astore 5
    //   405: aload_2
    //   406: astore 6
    //   408: aload_2
    //   409: astore 7
    //   411: aload 9
    //   413: invokespecial 190	java/lang/StringBuilder:<init>	()V
    //   416: aload_2
    //   417: astore_3
    //   418: aload_2
    //   419: astore 4
    //   421: aload_2
    //   422: astore 5
    //   424: aload_2
    //   425: astore 6
    //   427: aload_2
    //   428: astore 7
    //   430: aload 9
    //   432: ldc_w 433
    //   435: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   438: pop
    //   439: aload_2
    //   440: astore_3
    //   441: aload_2
    //   442: astore 4
    //   444: aload_2
    //   445: astore 5
    //   447: aload_2
    //   448: astore 6
    //   450: aload_2
    //   451: astore 7
    //   453: aload 9
    //   455: aload 10
    //   457: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   460: pop
    //   461: aload_2
    //   462: astore_3
    //   463: aload_2
    //   464: astore 4
    //   466: aload_2
    //   467: astore 5
    //   469: aload_2
    //   470: astore 6
    //   472: aload_2
    //   473: astore 7
    //   475: ldc 75
    //   477: aload 9
    //   479: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   482: invokestatic 295	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   485: pop
    //   486: goto +111 -> 597
    //   489: aload_2
    //   490: astore_3
    //   491: aload_2
    //   492: astore 4
    //   494: aload_2
    //   495: astore 5
    //   497: aload_2
    //   498: astore 6
    //   500: aload_2
    //   501: astore 7
    //   503: new 189	java/lang/StringBuilder
    //   506: astore 10
    //   508: aload_2
    //   509: astore_3
    //   510: aload_2
    //   511: astore 4
    //   513: aload_2
    //   514: astore 5
    //   516: aload_2
    //   517: astore 6
    //   519: aload_2
    //   520: astore 7
    //   522: aload 10
    //   524: invokespecial 190	java/lang/StringBuilder:<init>	()V
    //   527: aload_2
    //   528: astore_3
    //   529: aload_2
    //   530: astore 4
    //   532: aload_2
    //   533: astore 5
    //   535: aload_2
    //   536: astore 6
    //   538: aload_2
    //   539: astore 7
    //   541: aload 10
    //   543: ldc_w 340
    //   546: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   549: pop
    //   550: aload_2
    //   551: astore_3
    //   552: aload_2
    //   553: astore 4
    //   555: aload_2
    //   556: astore 5
    //   558: aload_2
    //   559: astore 6
    //   561: aload_2
    //   562: astore 7
    //   564: aload 10
    //   566: aload 9
    //   568: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   571: pop
    //   572: aload_2
    //   573: astore_3
    //   574: aload_2
    //   575: astore 4
    //   577: aload_2
    //   578: astore 5
    //   580: aload_2
    //   581: astore 6
    //   583: aload_2
    //   584: astore 7
    //   586: ldc 75
    //   588: aload 10
    //   590: invokevirtual 205	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   593: invokestatic 295	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   596: pop
    //   597: goto -448 -> 149
    //   600: astore_2
    //   601: goto +84 -> 685
    //   604: astore_2
    //   605: aload 4
    //   607: astore_3
    //   608: ldc 75
    //   610: ldc_w 435
    //   613: aload_2
    //   614: invokestatic 290	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   617: pop
    //   618: aload 4
    //   620: ifnull +97 -> 717
    //   623: aload 4
    //   625: invokevirtual 414	java/io/FileInputStream:close	()V
    //   628: goto +82 -> 710
    //   631: astore_2
    //   632: aload 5
    //   634: astore_3
    //   635: ldc 75
    //   637: ldc_w 435
    //   640: aload_2
    //   641: invokestatic 290	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   644: pop
    //   645: aload 5
    //   647: ifnull +70 -> 717
    //   650: aload 5
    //   652: invokevirtual 414	java/io/FileInputStream:close	()V
    //   655: goto +55 -> 710
    //   658: astore_2
    //   659: aload 6
    //   661: astore_3
    //   662: ldc 75
    //   664: ldc_w 437
    //   667: aload_2
    //   668: invokestatic 290	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   671: pop
    //   672: aload 6
    //   674: ifnull +43 -> 717
    //   677: aload 6
    //   679: invokevirtual 414	java/io/FileInputStream:close	()V
    //   682: goto +28 -> 710
    //   685: aload_3
    //   686: ifnull +11 -> 697
    //   689: aload_3
    //   690: invokevirtual 414	java/io/FileInputStream:close	()V
    //   693: goto +4 -> 697
    //   696: astore_3
    //   697: aload_2
    //   698: athrow
    //   699: astore_3
    //   700: aload 7
    //   702: ifnull +15 -> 717
    //   705: aload 7
    //   707: invokevirtual 414	java/io/FileInputStream:close	()V
    //   710: goto +7 -> 717
    //   713: astore_3
    //   714: goto -4 -> 710
    //   717: aload_1
    //   718: monitorexit
    //   719: return
    //   720: astore_3
    //   721: aload_1
    //   722: monitorexit
    //   723: aload_3
    //   724: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	725	0	this	SmsUsageMonitor
    //   4	718	1	localHashMap	HashMap
    //   17	567	2	localObject1	Object
    //   600	1	2	localObject2	Object
    //   604	10	2	localXmlPullParserException	XmlPullParserException
    //   631	10	2	localNumberFormatException1	NumberFormatException
    //   658	40	2	localIOException1	IOException
    //   27	320	3	localObject3	Object
    //   377	1	3	localNumberFormatException2	NumberFormatException
    //   379	311	3	localObject4	Object
    //   696	1	3	localIOException2	IOException
    //   699	1	3	localFileNotFoundException	java.io.FileNotFoundException
    //   713	1	3	localIOException3	IOException
    //   720	4	3	localObject5	Object
    //   31	593	4	localObject6	Object
    //   63	588	5	localObject7	Object
    //   66	612	6	localObject8	Object
    //   69	637	7	localObject9	Object
    //   98	174	8	localXmlPullParser	XmlPullParser
    //   189	378	9	localObject10	Object
    //   281	308	10	localObject11	Object
    // Exception table:
    //   from	to	target	type
    //   356	374	377	java/lang/NumberFormatException
    //   73	81	600	finally
    //   95	100	600	finally
    //   114	128	600	finally
    //   142	149	600	finally
    //   163	168	600	finally
    //   182	191	600	finally
    //   221	231	600	finally
    //   245	257	600	finally
    //   271	283	600	finally
    //   302	311	600	finally
    //   333	342	600	finally
    //   356	374	600	finally
    //   392	397	600	finally
    //   411	416	600	finally
    //   430	439	600	finally
    //   453	461	600	finally
    //   475	486	600	finally
    //   503	508	600	finally
    //   522	527	600	finally
    //   541	550	600	finally
    //   564	572	600	finally
    //   586	597	600	finally
    //   608	618	600	finally
    //   635	645	600	finally
    //   662	672	600	finally
    //   73	81	604	org/xmlpull/v1/XmlPullParserException
    //   95	100	604	org/xmlpull/v1/XmlPullParserException
    //   114	128	604	org/xmlpull/v1/XmlPullParserException
    //   142	149	604	org/xmlpull/v1/XmlPullParserException
    //   163	168	604	org/xmlpull/v1/XmlPullParserException
    //   182	191	604	org/xmlpull/v1/XmlPullParserException
    //   221	231	604	org/xmlpull/v1/XmlPullParserException
    //   245	257	604	org/xmlpull/v1/XmlPullParserException
    //   271	283	604	org/xmlpull/v1/XmlPullParserException
    //   302	311	604	org/xmlpull/v1/XmlPullParserException
    //   333	342	604	org/xmlpull/v1/XmlPullParserException
    //   356	374	604	org/xmlpull/v1/XmlPullParserException
    //   392	397	604	org/xmlpull/v1/XmlPullParserException
    //   411	416	604	org/xmlpull/v1/XmlPullParserException
    //   430	439	604	org/xmlpull/v1/XmlPullParserException
    //   453	461	604	org/xmlpull/v1/XmlPullParserException
    //   475	486	604	org/xmlpull/v1/XmlPullParserException
    //   503	508	604	org/xmlpull/v1/XmlPullParserException
    //   522	527	604	org/xmlpull/v1/XmlPullParserException
    //   541	550	604	org/xmlpull/v1/XmlPullParserException
    //   564	572	604	org/xmlpull/v1/XmlPullParserException
    //   586	597	604	org/xmlpull/v1/XmlPullParserException
    //   73	81	631	java/lang/NumberFormatException
    //   95	100	631	java/lang/NumberFormatException
    //   114	128	631	java/lang/NumberFormatException
    //   142	149	631	java/lang/NumberFormatException
    //   163	168	631	java/lang/NumberFormatException
    //   182	191	631	java/lang/NumberFormatException
    //   221	231	631	java/lang/NumberFormatException
    //   245	257	631	java/lang/NumberFormatException
    //   271	283	631	java/lang/NumberFormatException
    //   302	311	631	java/lang/NumberFormatException
    //   333	342	631	java/lang/NumberFormatException
    //   392	397	631	java/lang/NumberFormatException
    //   411	416	631	java/lang/NumberFormatException
    //   430	439	631	java/lang/NumberFormatException
    //   453	461	631	java/lang/NumberFormatException
    //   475	486	631	java/lang/NumberFormatException
    //   503	508	631	java/lang/NumberFormatException
    //   522	527	631	java/lang/NumberFormatException
    //   541	550	631	java/lang/NumberFormatException
    //   564	572	631	java/lang/NumberFormatException
    //   586	597	631	java/lang/NumberFormatException
    //   73	81	658	java/io/IOException
    //   95	100	658	java/io/IOException
    //   114	128	658	java/io/IOException
    //   142	149	658	java/io/IOException
    //   163	168	658	java/io/IOException
    //   182	191	658	java/io/IOException
    //   221	231	658	java/io/IOException
    //   245	257	658	java/io/IOException
    //   271	283	658	java/io/IOException
    //   302	311	658	java/io/IOException
    //   333	342	658	java/io/IOException
    //   356	374	658	java/io/IOException
    //   392	397	658	java/io/IOException
    //   411	416	658	java/io/IOException
    //   430	439	658	java/io/IOException
    //   453	461	658	java/io/IOException
    //   475	486	658	java/io/IOException
    //   503	508	658	java/io/IOException
    //   522	527	658	java/io/IOException
    //   541	550	658	java/io/IOException
    //   564	572	658	java/io/IOException
    //   586	597	658	java/io/IOException
    //   689	693	696	java/io/IOException
    //   73	81	699	java/io/FileNotFoundException
    //   95	100	699	java/io/FileNotFoundException
    //   114	128	699	java/io/FileNotFoundException
    //   142	149	699	java/io/FileNotFoundException
    //   163	168	699	java/io/FileNotFoundException
    //   182	191	699	java/io/FileNotFoundException
    //   221	231	699	java/io/FileNotFoundException
    //   245	257	699	java/io/FileNotFoundException
    //   271	283	699	java/io/FileNotFoundException
    //   302	311	699	java/io/FileNotFoundException
    //   333	342	699	java/io/FileNotFoundException
    //   356	374	699	java/io/FileNotFoundException
    //   392	397	699	java/io/FileNotFoundException
    //   411	416	699	java/io/FileNotFoundException
    //   430	439	699	java/io/FileNotFoundException
    //   453	461	699	java/io/FileNotFoundException
    //   475	486	699	java/io/FileNotFoundException
    //   503	508	699	java/io/FileNotFoundException
    //   522	527	699	java/io/FileNotFoundException
    //   541	550	699	java/io/FileNotFoundException
    //   564	572	699	java/io/FileNotFoundException
    //   586	597	699	java/io/FileNotFoundException
    //   200	204	713	java/io/IOException
    //   623	628	713	java/io/IOException
    //   650	655	713	java/io/IOException
    //   677	682	713	java/io/IOException
    //   705	710	713	java/io/IOException
    //   7	59	720	finally
    //   200	204	720	finally
    //   623	628	720	finally
    //   650	655	720	finally
    //   677	682	720	finally
    //   689	693	720	finally
    //   697	699	720	finally
    //   705	710	720	finally
    //   717	719	720	finally
    //   721	723	720	finally
  }
  
  private static void log(String paramString)
  {
    Rlog.d("SmsUsageMonitor", paramString);
  }
  
  public static int mergeShortCodeCategories(int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramInt2) {
      return paramInt1;
    }
    return paramInt2;
  }
  
  private void removeExpiredTimestamps()
  {
    long l1 = System.currentTimeMillis();
    long l2 = mCheckPeriod;
    synchronized (mSmsStamp)
    {
      Iterator localIterator = mSmsStamp.entrySet().iterator();
      while (localIterator.hasNext())
      {
        ArrayList localArrayList = (ArrayList)((Map.Entry)localIterator.next()).getValue();
        if ((localArrayList.isEmpty()) || (((Long)localArrayList.get(localArrayList.size() - 1)).longValue() < l1 - l2)) {
          localIterator.remove();
        }
      }
      return;
    }
  }
  
  /* Error */
  private void writePremiumSmsPolicyDb()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 138	com/android/internal/telephony/SmsUsageMonitor:mPremiumSmsPolicy	Ljava/util/HashMap;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aconst_null
    //   8: astore_2
    //   9: aload_0
    //   10: getfield 385	com/android/internal/telephony/SmsUsageMonitor:mPolicyFile	Landroid/util/AtomicFile;
    //   13: invokevirtual 474	android/util/AtomicFile:startWrite	()Ljava/io/FileOutputStream;
    //   16: astore_3
    //   17: aload_3
    //   18: astore_2
    //   19: new 476	com/android/internal/util/FastXmlSerializer
    //   22: astore 4
    //   24: aload_3
    //   25: astore_2
    //   26: aload 4
    //   28: invokespecial 477	com/android/internal/util/FastXmlSerializer:<init>	()V
    //   31: aload_3
    //   32: astore_2
    //   33: aload 4
    //   35: aload_3
    //   36: getstatic 404	java/nio/charset/StandardCharsets:UTF_8	Ljava/nio/charset/Charset;
    //   39: invokevirtual 408	java/nio/charset/Charset:name	()Ljava/lang/String;
    //   42: invokeinterface 483 3 0
    //   47: aload_3
    //   48: astore_2
    //   49: aload 4
    //   51: aconst_null
    //   52: iconst_1
    //   53: invokestatic 488	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   56: invokeinterface 492 3 0
    //   61: aload_3
    //   62: astore_2
    //   63: aload 4
    //   65: aconst_null
    //   66: ldc 87
    //   68: invokeinterface 496 3 0
    //   73: pop
    //   74: aload_3
    //   75: astore_2
    //   76: aload_0
    //   77: getfield 138	com/android/internal/telephony/SmsUsageMonitor:mPremiumSmsPolicy	Ljava/util/HashMap;
    //   80: invokevirtual 448	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   83: invokeinterface 454 1 0
    //   88: astore 5
    //   90: aload_3
    //   91: astore_2
    //   92: aload 5
    //   94: invokeinterface 459 1 0
    //   99: ifeq +95 -> 194
    //   102: aload_3
    //   103: astore_2
    //   104: aload 5
    //   106: invokeinterface 463 1 0
    //   111: checkcast 465	java/util/Map$Entry
    //   114: astore 6
    //   116: aload_3
    //   117: astore_2
    //   118: aload 4
    //   120: aconst_null
    //   121: ldc 78
    //   123: invokeinterface 496 3 0
    //   128: pop
    //   129: aload_3
    //   130: astore_2
    //   131: aload 4
    //   133: aconst_null
    //   134: ldc 28
    //   136: aload 6
    //   138: invokeinterface 499 1 0
    //   143: checkcast 327	java/lang/String
    //   146: invokeinterface 503 4 0
    //   151: pop
    //   152: aload_3
    //   153: astore_2
    //   154: aload 4
    //   156: aconst_null
    //   157: ldc 31
    //   159: aload 6
    //   161: invokeinterface 468 1 0
    //   166: checkcast 420	java/lang/Integer
    //   169: invokevirtual 504	java/lang/Integer:toString	()Ljava/lang/String;
    //   172: invokeinterface 503 4 0
    //   177: pop
    //   178: aload_3
    //   179: astore_2
    //   180: aload 4
    //   182: aconst_null
    //   183: ldc 78
    //   185: invokeinterface 507 3 0
    //   190: pop
    //   191: goto -101 -> 90
    //   194: aload_3
    //   195: astore_2
    //   196: aload 4
    //   198: aconst_null
    //   199: ldc 87
    //   201: invokeinterface 507 3 0
    //   206: pop
    //   207: aload_3
    //   208: astore_2
    //   209: aload 4
    //   211: invokeinterface 510 1 0
    //   216: aload_3
    //   217: astore_2
    //   218: aload_0
    //   219: getfield 385	com/android/internal/telephony/SmsUsageMonitor:mPolicyFile	Landroid/util/AtomicFile;
    //   222: aload_3
    //   223: invokevirtual 514	android/util/AtomicFile:finishWrite	(Ljava/io/FileOutputStream;)V
    //   226: goto +30 -> 256
    //   229: astore_2
    //   230: goto +29 -> 259
    //   233: astore_3
    //   234: ldc 75
    //   236: ldc_w 516
    //   239: aload_3
    //   240: invokestatic 290	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   243: pop
    //   244: aload_2
    //   245: ifnull +11 -> 256
    //   248: aload_0
    //   249: getfield 385	com/android/internal/telephony/SmsUsageMonitor:mPolicyFile	Landroid/util/AtomicFile;
    //   252: aload_2
    //   253: invokevirtual 519	android/util/AtomicFile:failWrite	(Ljava/io/FileOutputStream;)V
    //   256: aload_1
    //   257: monitorexit
    //   258: return
    //   259: aload_1
    //   260: monitorexit
    //   261: aload_2
    //   262: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	263	0	this	SmsUsageMonitor
    //   4	256	1	localHashMap	HashMap
    //   8	210	2	localObject	Object
    //   229	33	2	localFileOutputStream1	java.io.FileOutputStream
    //   16	207	3	localFileOutputStream2	java.io.FileOutputStream
    //   233	7	3	localIOException	IOException
    //   22	188	4	localFastXmlSerializer	com.android.internal.util.FastXmlSerializer
    //   88	17	5	localIterator	Iterator
    //   114	46	6	localEntry	Map.Entry
    // Exception table:
    //   from	to	target	type
    //   9	17	229	finally
    //   19	24	229	finally
    //   26	31	229	finally
    //   33	47	229	finally
    //   49	61	229	finally
    //   63	74	229	finally
    //   76	90	229	finally
    //   92	102	229	finally
    //   104	116	229	finally
    //   118	129	229	finally
    //   131	152	229	finally
    //   154	178	229	finally
    //   180	191	229	finally
    //   196	207	229	finally
    //   209	216	229	finally
    //   218	226	229	finally
    //   234	244	229	finally
    //   248	256	229	finally
    //   256	258	229	finally
    //   259	261	229	finally
    //   9	17	233	java/io/IOException
    //   19	24	233	java/io/IOException
    //   26	31	233	java/io/IOException
    //   33	47	233	java/io/IOException
    //   49	61	233	java/io/IOException
    //   63	74	233	java/io/IOException
    //   76	90	233	java/io/IOException
    //   92	102	233	java/io/IOException
    //   104	116	233	java/io/IOException
    //   118	129	233	java/io/IOException
    //   131	152	233	java/io/IOException
    //   154	178	233	java/io/IOException
    //   180	191	233	java/io/IOException
    //   196	207	233	java/io/IOException
    //   209	216	233	java/io/IOException
    //   218	226	233	java/io/IOException
  }
  
  public void authorizeOutgoingSms(PackageInfo paramPackageInfo, String paramString1, String paramString2, SmsAuthorizationCallback paramSmsAuthorizationCallback, Handler paramHandler)
  {
    paramSmsAuthorizationCallback.onAuthorizationResult(true);
  }
  
  public boolean check(String paramString, int paramInt)
  {
    synchronized (mSmsStamp)
    {
      removeExpiredTimestamps();
      ArrayList localArrayList1 = (ArrayList)mSmsStamp.get(paramString);
      ArrayList localArrayList2 = localArrayList1;
      if (localArrayList1 == null)
      {
        localArrayList2 = new java/util/ArrayList;
        localArrayList2.<init>();
        mSmsStamp.put(paramString, localArrayList2);
      }
      boolean bool = isUnderLimit(localArrayList2, paramInt);
      return bool;
    }
  }
  
  public int checkDestination(String paramString1, String paramString2)
  {
    synchronized (mSettingsObserverHandler)
    {
      if (PhoneNumberUtils.isEmergencyNumber(paramString1, paramString2)) {
        return 0;
      }
      if (!mCheckEnabled.get()) {
        return 0;
      }
      if ((paramString2 != null) && ((mCurrentCountry == null) || (!paramString2.equals(mCurrentCountry)) || (mPatternFile.lastModified() != mPatternFileLastModified)))
      {
        if (mPatternFile.exists()) {
          mCurrentPatternMatcher = getPatternMatcherFromFile(paramString2);
        } else {
          mCurrentPatternMatcher = getPatternMatcherFromResource(paramString2);
        }
        mCurrentCountry = paramString2;
      }
      if (mCurrentPatternMatcher != null)
      {
        int i = mCurrentPatternMatcher.getNumberCategory(paramString1);
        return i;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("No patterns for \"");
      localStringBuilder.append(paramString2);
      localStringBuilder.append("\": using generic short code rule");
      Rlog.e("SmsUsageMonitor", localStringBuilder.toString());
      if (paramString1.length() <= 5) {
        return 3;
      }
      return 0;
    }
  }
  
  void dispose()
  {
    mSmsStamp.clear();
  }
  
  public int getPremiumSmsPermission(String paramString)
  {
    checkCallerIsSystemOrPhoneOrSameApp(paramString);
    synchronized (mPremiumSmsPolicy)
    {
      paramString = (Integer)mPremiumSmsPolicy.get(paramString);
      if (paramString == null) {
        return 0;
      }
      int i = paramString.intValue();
      return i;
    }
  }
  
  public boolean isSmsAuthorizationEnabled()
  {
    return mContext.getResources().getBoolean(17957032);
  }
  
  public void setPremiumSmsPermission(String paramString, int paramInt)
  {
    
    if ((paramInt >= 1) && (paramInt <= 3)) {
      synchronized (mPremiumSmsPolicy)
      {
        mPremiumSmsPolicy.put(paramString, Integer.valueOf(paramInt));
        new Thread(new Runnable()
        {
          public void run()
          {
            SmsUsageMonitor.this.writePremiumSmsPolicyDb();
          }
        }).start();
        return;
      }
    }
    paramString = new StringBuilder();
    paramString.append("invalid SMS permission type ");
    paramString.append(paramInt);
    throw new IllegalArgumentException(paramString.toString());
  }
  
  private static class SettingsObserver
    extends ContentObserver
  {
    private final Context mContext;
    private final AtomicBoolean mEnabled;
    
    SettingsObserver(Handler paramHandler, Context paramContext, AtomicBoolean paramAtomicBoolean)
    {
      super();
      mContext = paramContext;
      mEnabled = paramAtomicBoolean;
      onChange(false);
    }
    
    public void onChange(boolean paramBoolean)
    {
      AtomicBoolean localAtomicBoolean = mEnabled;
      ContentResolver localContentResolver = mContext.getContentResolver();
      paramBoolean = true;
      if (Settings.Global.getInt(localContentResolver, "sms_short_code_confirmation", 1) == 0) {
        paramBoolean = false;
      }
      localAtomicBoolean.set(paramBoolean);
    }
  }
  
  private static class SettingsObserverHandler
    extends Handler
  {
    SettingsObserverHandler(Context paramContext, AtomicBoolean paramAtomicBoolean)
    {
      ContentResolver localContentResolver = paramContext.getContentResolver();
      paramContext = new SmsUsageMonitor.SettingsObserver(this, paramContext, paramAtomicBoolean);
      localContentResolver.registerContentObserver(Settings.Global.getUriFor("sms_short_code_confirmation"), false, paramContext);
    }
  }
  
  private static final class ShortCodePatternMatcher
  {
    private final Pattern mFreeShortCodePattern;
    private final Pattern mPremiumShortCodePattern;
    private final Pattern mShortCodePattern;
    private final Pattern mStandardShortCodePattern;
    
    ShortCodePatternMatcher(String paramString1, String paramString2, String paramString3, String paramString4)
    {
      Object localObject = null;
      if (paramString1 != null) {
        paramString1 = Pattern.compile(paramString1);
      } else {
        paramString1 = null;
      }
      mShortCodePattern = paramString1;
      if (paramString2 != null) {
        paramString1 = Pattern.compile(paramString2);
      } else {
        paramString1 = null;
      }
      mPremiumShortCodePattern = paramString1;
      if (paramString3 != null) {
        paramString1 = Pattern.compile(paramString3);
      } else {
        paramString1 = null;
      }
      mFreeShortCodePattern = paramString1;
      paramString1 = localObject;
      if (paramString4 != null) {
        paramString1 = Pattern.compile(paramString4);
      }
      mStandardShortCodePattern = paramString1;
    }
    
    int getNumberCategory(String paramString)
    {
      if ((mFreeShortCodePattern != null) && (mFreeShortCodePattern.matcher(paramString).matches())) {
        return 1;
      }
      if ((mStandardShortCodePattern != null) && (mStandardShortCodePattern.matcher(paramString).matches())) {
        return 2;
      }
      if ((mPremiumShortCodePattern != null) && (mPremiumShortCodePattern.matcher(paramString).matches())) {
        return 4;
      }
      if ((mShortCodePattern != null) && (mShortCodePattern.matcher(paramString).matches())) {
        return 3;
      }
      return 0;
    }
  }
  
  public static abstract interface SmsAuthorizationCallback
  {
    public abstract void onAuthorizationResult(boolean paramBoolean);
  }
}
