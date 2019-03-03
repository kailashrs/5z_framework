package android.security.net.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.XmlResourceParser;
import android.util.ArraySet;
import android.util.Base64;
import android.util.Pair;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlConfigSource
  implements ConfigSource
{
  private static final int CONFIG_BASE = 0;
  private static final int CONFIG_DEBUG = 2;
  private static final int CONFIG_DOMAIN = 1;
  private final ApplicationInfo mApplicationInfo;
  private Context mContext;
  private final boolean mDebugBuild;
  private NetworkSecurityConfig mDefaultConfig;
  private Set<Pair<Domain, NetworkSecurityConfig>> mDomainMap;
  private boolean mInitialized;
  private final Object mLock = new Object();
  private final int mResourceId;
  
  public XmlConfigSource(Context paramContext, int paramInt, ApplicationInfo paramApplicationInfo)
  {
    mContext = paramContext;
    mResourceId = paramInt;
    mApplicationInfo = new ApplicationInfo(paramApplicationInfo);
    boolean bool;
    if ((mApplicationInfo.flags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mDebugBuild = bool;
  }
  
  private void addDebugAnchorsIfNeeded(NetworkSecurityConfig.Builder paramBuilder1, NetworkSecurityConfig.Builder paramBuilder2)
  {
    if ((paramBuilder1 != null) && (paramBuilder1.hasCertificatesEntryRefs()))
    {
      if (!paramBuilder2.hasCertificatesEntryRefs()) {
        return;
      }
      paramBuilder2.addCertificatesEntryRefs(paramBuilder1.getCertificatesEntryRefs());
      return;
    }
  }
  
  /* Error */
  private void ensureInitialized()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 52	android/security/net/config/XmlConfigSource:mLock	Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 93	android/security/net/config/XmlConfigSource:mInitialized	Z
    //   11: ifeq +6 -> 17
    //   14: aload_1
    //   15: monitorexit
    //   16: return
    //   17: aload_0
    //   18: getfield 54	android/security/net/config/XmlConfigSource:mContext	Landroid/content/Context;
    //   21: invokevirtual 99	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   24: aload_0
    //   25: getfield 56	android/security/net/config/XmlConfigSource:mResourceId	I
    //   28: invokevirtual 105	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   31: astore_2
    //   32: aconst_null
    //   33: astore_3
    //   34: aload_3
    //   35: astore 4
    //   37: aload_0
    //   38: aload_2
    //   39: invokespecial 109	android/security/net/config/XmlConfigSource:parseNetworkSecurityConfig	(Landroid/content/res/XmlResourceParser;)V
    //   42: aload_3
    //   43: astore 4
    //   45: aload_0
    //   46: aconst_null
    //   47: putfield 54	android/security/net/config/XmlConfigSource:mContext	Landroid/content/Context;
    //   50: aload_3
    //   51: astore 4
    //   53: aload_0
    //   54: iconst_1
    //   55: putfield 93	android/security/net/config/XmlConfigSource:mInitialized	Z
    //   58: aload_2
    //   59: ifnull +8 -> 67
    //   62: aconst_null
    //   63: aload_2
    //   64: invokestatic 111	android/security/net/config/XmlConfigSource:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   67: aload_1
    //   68: monitorexit
    //   69: return
    //   70: astore_3
    //   71: goto +9 -> 80
    //   74: astore_3
    //   75: aload_3
    //   76: astore 4
    //   78: aload_3
    //   79: athrow
    //   80: aload_2
    //   81: ifnull +9 -> 90
    //   84: aload 4
    //   86: aload_2
    //   87: invokestatic 111	android/security/net/config/XmlConfigSource:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   90: aload_3
    //   91: athrow
    //   92: astore 4
    //   94: new 113	java/lang/RuntimeException
    //   97: astore_2
    //   98: new 115	java/lang/StringBuilder
    //   101: astore_3
    //   102: aload_3
    //   103: invokespecial 116	java/lang/StringBuilder:<init>	()V
    //   106: aload_3
    //   107: ldc 118
    //   109: invokevirtual 122	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload_3
    //   114: aload_0
    //   115: getfield 54	android/security/net/config/XmlConfigSource:mContext	Landroid/content/Context;
    //   118: invokevirtual 99	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   121: aload_0
    //   122: getfield 56	android/security/net/config/XmlConfigSource:mResourceId	I
    //   125: invokevirtual 126	android/content/res/Resources:getResourceEntryName	(I)Ljava/lang/String;
    //   128: invokevirtual 122	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload_2
    //   133: aload_3
    //   134: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   137: aload 4
    //   139: invokespecial 133	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   142: aload_2
    //   143: athrow
    //   144: astore 4
    //   146: aload_1
    //   147: monitorexit
    //   148: aload 4
    //   150: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	151	0	this	XmlConfigSource
    //   4	143	1	localObject1	Object
    //   31	112	2	localObject2	Object
    //   33	18	3	localObject3	Object
    //   70	1	3	localObject4	Object
    //   74	17	3	localThrowable	Throwable
    //   101	33	3	localStringBuilder	StringBuilder
    //   35	50	4	localObject5	Object
    //   92	46	4	localNotFoundException	android.content.res.Resources.NotFoundException
    //   144	5	4	localObject6	Object
    // Exception table:
    //   from	to	target	type
    //   37	42	70	finally
    //   45	50	70	finally
    //   53	58	70	finally
    //   78	80	70	finally
    //   37	42	74	java/lang/Throwable
    //   45	50	74	java/lang/Throwable
    //   53	58	74	java/lang/Throwable
    //   17	32	92	android/content/res/Resources$NotFoundException
    //   17	32	92	org/xmlpull/v1/XmlPullParserException
    //   17	32	92	java/io/IOException
    //   17	32	92	android/security/net/config/XmlConfigSource$ParserException
    //   62	67	92	android/content/res/Resources$NotFoundException
    //   62	67	92	org/xmlpull/v1/XmlPullParserException
    //   62	67	92	java/io/IOException
    //   62	67	92	android/security/net/config/XmlConfigSource$ParserException
    //   84	90	92	android/content/res/Resources$NotFoundException
    //   84	90	92	org/xmlpull/v1/XmlPullParserException
    //   84	90	92	java/io/IOException
    //   84	90	92	android/security/net/config/XmlConfigSource$ParserException
    //   90	92	92	android/content/res/Resources$NotFoundException
    //   90	92	92	org/xmlpull/v1/XmlPullParserException
    //   90	92	92	java/io/IOException
    //   90	92	92	android/security/net/config/XmlConfigSource$ParserException
    //   7	16	144	finally
    //   17	32	144	finally
    //   62	67	144	finally
    //   67	69	144	finally
    //   84	90	144	finally
    //   90	92	144	finally
    //   94	144	144	finally
    //   146	148	144	finally
  }
  
  private static final String getConfigString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown config type: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 2: 
      return "debug-overrides";
    case 1: 
      return "domain-config";
    }
    return "base-config";
  }
  
  private CertificatesEntryRef parseCertificatesEntry(XmlResourceParser paramXmlResourceParser, boolean paramBoolean)
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    paramBoolean = paramXmlResourceParser.getAttributeBooleanValue(null, "overridePins", paramBoolean);
    int i = paramXmlResourceParser.getAttributeResourceValue(null, "src", -1);
    Object localObject = paramXmlResourceParser.getAttributeValue(null, "src");
    if (localObject != null)
    {
      if (i != -1) {
        localObject = new ResourceCertificateSource(i, mContext);
      }
      for (;;)
      {
        break;
        if ("system".equals(localObject))
        {
          localObject = SystemCertificateSource.getInstance();
        }
        else
        {
          if (!"user".equals(localObject)) {
            break label111;
          }
          localObject = UserCertificateSource.getInstance();
        }
      }
      XmlUtils.skipCurrentTag(paramXmlResourceParser);
      return new CertificatesEntryRef((CertificateSource)localObject, paramBoolean);
      label111:
      throw new ParserException(paramXmlResourceParser, "Unknown certificates src. Should be one of system|user|@resourceVal");
    }
    throw new ParserException(paramXmlResourceParser, "certificates element missing src attribute");
  }
  
  private List<Pair<NetworkSecurityConfig.Builder, Set<Domain>>> parseConfigEntry(XmlResourceParser paramXmlResourceParser, Set<String> paramSet, NetworkSecurityConfig.Builder paramBuilder, int paramInt)
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    ArrayList localArrayList = new ArrayList();
    NetworkSecurityConfig.Builder localBuilder = new NetworkSecurityConfig.Builder();
    localBuilder.setParent(paramBuilder);
    ArraySet localArraySet = new ArraySet();
    int i = 0;
    int j = 0;
    boolean bool;
    if (paramInt == 2) {
      bool = true;
    } else {
      bool = false;
    }
    paramXmlResourceParser.getName();
    int k = paramXmlResourceParser.getDepth();
    localArrayList.add(new Pair(localBuilder, localArraySet));
    int n;
    int i1;
    for (int m = 0;; m++)
    {
      n = i;
      i1 = j;
      if (m >= paramXmlResourceParser.getAttributeCount()) {
        break;
      }
      paramBuilder = paramXmlResourceParser.getAttributeName(m);
      if ("hstsEnforced".equals(paramBuilder)) {
        localBuilder.setHstsEnforced(paramXmlResourceParser.getAttributeBooleanValue(m, false));
      } else if ("cleartextTrafficPermitted".equals(paramBuilder)) {
        localBuilder.setCleartextTrafficPermitted(paramXmlResourceParser.getAttributeBooleanValue(m, true));
      }
    }
    for (;;)
    {
      paramBuilder = this;
      if (!XmlUtils.nextElementWithin(paramXmlResourceParser, k)) {
        break;
      }
      String str = paramXmlResourceParser.getName();
      if ("domain".equals(str))
      {
        if (paramInt == 1)
        {
          localArraySet.add(parseDomain(paramXmlResourceParser, paramSet));
        }
        else
        {
          paramSet = new StringBuilder();
          paramSet.append("domain element not allowed in ");
          paramSet.append(getConfigString(paramInt));
          throw new ParserException(paramXmlResourceParser, paramSet.toString());
        }
      }
      else
      {
        if (!"trust-anchors".equals(str)) {
          break label317;
        }
        if (i1 != 0) {
          break label305;
        }
        localBuilder.addCertificatesEntryRefs(paramBuilder.parseTrustAnchors(paramXmlResourceParser, bool));
        i1 = 1;
      }
      for (;;)
      {
        break;
        label305:
        throw new ParserException(paramXmlResourceParser, "Multiple trust-anchor elements not allowed");
        label317:
        if ("pin-set".equals(str))
        {
          if (paramInt == 1)
          {
            if (n == 0)
            {
              localBuilder.setPinSet(parsePinSet(paramXmlResourceParser));
              n = 1;
            }
            else
            {
              throw new ParserException(paramXmlResourceParser, "Multiple pin-set elements not allowed");
            }
          }
          else
          {
            paramSet = new StringBuilder();
            paramSet.append("pin-set element not allowed in ");
            paramSet.append(getConfigString(paramInt));
            throw new ParserException(paramXmlResourceParser, paramSet.toString());
          }
        }
        else if ("domain-config".equals(str))
        {
          if (paramInt == 1)
          {
            localArrayList.addAll(paramBuilder.parseConfigEntry(paramXmlResourceParser, paramSet, localBuilder, paramInt));
          }
          else
          {
            paramSet = new StringBuilder();
            paramSet.append("Nested domain-config not allowed in ");
            paramSet.append(getConfigString(paramInt));
            throw new ParserException(paramXmlResourceParser, paramSet.toString());
          }
        }
        else {
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
      }
    }
    if ((paramInt == 1) && (localArraySet.isEmpty())) {
      throw new ParserException(paramXmlResourceParser, "No domain elements in domain-config");
    }
    return localArrayList;
  }
  
  /* Error */
  private NetworkSecurityConfig.Builder parseDebugOverridesResource()
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 54	android/security/net/config/XmlConfigSource:mContext	Landroid/content/Context;
    //   4: invokevirtual 99	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   7: astore_1
    //   8: aload_1
    //   9: aload_0
    //   10: getfield 56	android/security/net/config/XmlConfigSource:mResourceId	I
    //   13: invokevirtual 324	android/content/res/Resources:getResourcePackageName	(I)Ljava/lang/String;
    //   16: astore_2
    //   17: aload_1
    //   18: aload_0
    //   19: getfield 56	android/security/net/config/XmlConfigSource:mResourceId	I
    //   22: invokevirtual 126	android/content/res/Resources:getResourceEntryName	(I)Ljava/lang/String;
    //   25: astore_3
    //   26: new 115	java/lang/StringBuilder
    //   29: dup
    //   30: invokespecial 116	java/lang/StringBuilder:<init>	()V
    //   33: astore 4
    //   35: aload 4
    //   37: aload_3
    //   38: invokevirtual 122	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload 4
    //   44: ldc_w 326
    //   47: invokevirtual 122	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload_1
    //   52: aload 4
    //   54: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   57: ldc_w 328
    //   60: aload_2
    //   61: invokevirtual 332	android/content/res/Resources:getIdentifier	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
    //   64: istore 5
    //   66: aconst_null
    //   67: astore 4
    //   69: aconst_null
    //   70: astore 6
    //   72: iload 5
    //   74: ifne +5 -> 79
    //   77: aconst_null
    //   78: areturn
    //   79: aconst_null
    //   80: astore 7
    //   82: aconst_null
    //   83: astore_3
    //   84: aload_1
    //   85: iload 5
    //   87: invokevirtual 105	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   90: astore 8
    //   92: aload 6
    //   94: astore_1
    //   95: aload_3
    //   96: astore_2
    //   97: aload 8
    //   99: ldc_w 334
    //   102: invokestatic 337	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   105: aload 6
    //   107: astore_1
    //   108: aload_3
    //   109: astore_2
    //   110: aload 8
    //   112: invokeinterface 235 1 0
    //   117: istore 9
    //   119: aconst_null
    //   120: astore_1
    //   121: iconst_0
    //   122: istore 5
    //   124: aload 8
    //   126: iload 9
    //   128: invokestatic 269	com/android/internal/util/XmlUtils:nextElementWithin	(Lorg/xmlpull/v1/XmlPullParser;I)Z
    //   131: ifeq +94 -> 225
    //   134: ldc -110
    //   136: aload 8
    //   138: invokeinterface 231 1 0
    //   143: invokevirtual 183	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   146: ifeq +71 -> 217
    //   149: iload 5
    //   151: ifne +51 -> 202
    //   154: aload_0
    //   155: getfield 68	android/security/net/config/XmlConfigSource:mDebugBuild	Z
    //   158: ifeq +33 -> 191
    //   161: aload_0
    //   162: aload 8
    //   164: aconst_null
    //   165: aconst_null
    //   166: iconst_2
    //   167: invokespecial 306	android/security/net/config/XmlConfigSource:parseConfigEntry	(Landroid/content/res/XmlResourceParser;Ljava/util/Set;Landroid/security/net/config/NetworkSecurityConfig$Builder;I)Ljava/util/List;
    //   170: iconst_0
    //   171: invokeinterface 341 2 0
    //   176: checkcast 237	android/util/Pair
    //   179: getfield 344	android/util/Pair:first	Ljava/lang/Object;
    //   182: checkcast 72	android/security/net/config/NetworkSecurityConfig$Builder
    //   185: astore_3
    //   186: aload_3
    //   187: astore_1
    //   188: goto +8 -> 196
    //   191: aload 8
    //   193: invokestatic 202	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   196: iconst_1
    //   197: istore 5
    //   199: goto -75 -> 124
    //   202: new 8	android/security/net/config/XmlConfigSource$ParserException
    //   205: astore_3
    //   206: aload_3
    //   207: aload 8
    //   209: ldc_w 346
    //   212: invokespecial 212	android/security/net/config/XmlConfigSource$ParserException:<init>	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   215: aload_3
    //   216: athrow
    //   217: aload 8
    //   219: invokestatic 202	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   222: goto -98 -> 124
    //   225: aload 8
    //   227: ifnull +9 -> 236
    //   230: aconst_null
    //   231: aload 8
    //   233: invokestatic 111	android/security/net/config/XmlConfigSource:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   236: aload_1
    //   237: areturn
    //   238: astore_3
    //   239: aload 4
    //   241: astore_1
    //   242: goto +21 -> 263
    //   245: astore_3
    //   246: aload_1
    //   247: astore_2
    //   248: goto +11 -> 259
    //   251: astore_3
    //   252: goto +11 -> 263
    //   255: astore_3
    //   256: aload 7
    //   258: astore_2
    //   259: aload_3
    //   260: astore_1
    //   261: aload_3
    //   262: athrow
    //   263: aload 8
    //   265: ifnull +9 -> 274
    //   268: aload_1
    //   269: aload 8
    //   271: invokestatic 111	android/security/net/config/XmlConfigSource:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   274: aload_3
    //   275: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	276	0	this	XmlConfigSource
    //   7	262	1	localObject1	Object
    //   16	243	2	localObject2	Object
    //   25	191	3	localObject3	Object
    //   238	1	3	localObject4	Object
    //   245	1	3	localThrowable1	Throwable
    //   251	1	3	localObject5	Object
    //   255	20	3	localThrowable2	Throwable
    //   33	207	4	localStringBuilder	StringBuilder
    //   64	134	5	i	int
    //   70	36	6	localObject6	Object
    //   80	177	7	localObject7	Object
    //   90	180	8	localXmlResourceParser	XmlResourceParser
    //   117	10	9	j	int
    // Exception table:
    //   from	to	target	type
    //   124	149	238	finally
    //   154	186	238	finally
    //   191	196	238	finally
    //   202	217	238	finally
    //   217	222	238	finally
    //   124	149	245	java/lang/Throwable
    //   154	186	245	java/lang/Throwable
    //   191	196	245	java/lang/Throwable
    //   202	217	245	java/lang/Throwable
    //   217	222	245	java/lang/Throwable
    //   97	105	251	finally
    //   110	119	251	finally
    //   261	263	251	finally
    //   97	105	255	java/lang/Throwable
    //   110	119	255	java/lang/Throwable
  }
  
  private Domain parseDomain(XmlResourceParser paramXmlResourceParser, Set<String> paramSet)
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    boolean bool = paramXmlResourceParser.getAttributeBooleanValue(null, "includeSubdomains", false);
    if (paramXmlResourceParser.next() == 4)
    {
      String str = paramXmlResourceParser.getText().trim().toLowerCase(Locale.US);
      if (paramXmlResourceParser.next() == 3)
      {
        if (paramSet.add(str)) {
          return new Domain(str, bool);
        }
        paramSet = new StringBuilder();
        paramSet.append(str);
        paramSet.append(" has already been specified");
        throw new ParserException(paramXmlResourceParser, paramSet.toString());
      }
      throw new ParserException(paramXmlResourceParser, "domain contains additional elements");
    }
    throw new ParserException(paramXmlResourceParser, "Domain name missing");
  }
  
  private void parseNetworkSecurityConfig(XmlResourceParser paramXmlResourceParser)
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    Object localObject1 = new ArraySet();
    Object localObject2 = new ArrayList();
    Object localObject3 = null;
    Object localObject4 = null;
    int i = 0;
    int j = 0;
    XmlUtils.beginDocument(paramXmlResourceParser, "network-security-config");
    int k = paramXmlResourceParser.getDepth();
    while (XmlUtils.nextElementWithin(paramXmlResourceParser, k)) {
      if ("base-config".equals(paramXmlResourceParser.getName()))
      {
        if (j == 0)
        {
          j = 1;
          localObject3 = (NetworkSecurityConfig.Builder)parseConfigEntry0get0first;
        }
        else
        {
          throw new ParserException(paramXmlResourceParser, "Only one base-config allowed");
        }
      }
      else if ("domain-config".equals(paramXmlResourceParser.getName())) {
        ((List)localObject2).addAll(parseConfigEntry(paramXmlResourceParser, (Set)localObject1, (NetworkSecurityConfig.Builder)localObject3, 1));
      } else if ("debug-overrides".equals(paramXmlResourceParser.getName()))
      {
        if (i == 0)
        {
          if (mDebugBuild) {
            localObject4 = (NetworkSecurityConfig.Builder)parseConfigEntry2get0first;
          } else {
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
          i = 1;
        }
        else
        {
          throw new ParserException(paramXmlResourceParser, "Only one debug-overrides allowed");
        }
      }
      else {
        XmlUtils.skipCurrentTag(paramXmlResourceParser);
      }
    }
    paramXmlResourceParser = (XmlResourceParser)localObject4;
    if (mDebugBuild)
    {
      paramXmlResourceParser = (XmlResourceParser)localObject4;
      if (localObject4 == null) {
        paramXmlResourceParser = parseDebugOverridesResource();
      }
    }
    localObject4 = NetworkSecurityConfig.getDefaultBuilder(mApplicationInfo);
    addDebugAnchorsIfNeeded(paramXmlResourceParser, (NetworkSecurityConfig.Builder)localObject4);
    if (localObject3 != null)
    {
      ((NetworkSecurityConfig.Builder)localObject3).setParent((NetworkSecurityConfig.Builder)localObject4);
      addDebugAnchorsIfNeeded(paramXmlResourceParser, (NetworkSecurityConfig.Builder)localObject3);
    }
    else
    {
      localObject3 = localObject4;
    }
    ArraySet localArraySet = new ArraySet();
    localObject2 = ((List)localObject2).iterator();
    localObject4 = localObject1;
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (Pair)((Iterator)localObject2).next();
      Object localObject5 = (NetworkSecurityConfig.Builder)first;
      localObject1 = (Set)second;
      if (((NetworkSecurityConfig.Builder)localObject5).getParent() == null) {
        ((NetworkSecurityConfig.Builder)localObject5).setParent((NetworkSecurityConfig.Builder)localObject3);
      }
      addDebugAnchorsIfNeeded(paramXmlResourceParser, (NetworkSecurityConfig.Builder)localObject5);
      localObject5 = ((NetworkSecurityConfig.Builder)localObject5).build();
      localObject1 = ((Set)localObject1).iterator();
      while (((Iterator)localObject1).hasNext()) {
        localArraySet.add(new Pair((Domain)((Iterator)localObject1).next(), localObject5));
      }
    }
    mDefaultConfig = ((NetworkSecurityConfig.Builder)localObject3).build();
    mDomainMap = localArraySet;
  }
  
  private Pin parsePin(XmlResourceParser paramXmlResourceParser)
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    String str = paramXmlResourceParser.getAttributeValue(null, "digest");
    if (Pin.isSupportedDigestAlgorithm(str))
    {
      if (paramXmlResourceParser.next() == 4)
      {
        localObject = paramXmlResourceParser.getText().trim();
        try
        {
          byte[] arrayOfByte = Base64.decode((String)localObject, 0);
          int i = Pin.getDigestLength(str);
          if (arrayOfByte.length == i)
          {
            if (paramXmlResourceParser.next() == 3) {
              return new Pin(str, arrayOfByte);
            }
            throw new ParserException(paramXmlResourceParser, "pin contains additional elements");
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("digest length ");
          ((StringBuilder)localObject).append(arrayOfByte.length);
          ((StringBuilder)localObject).append(" does not match expected length for ");
          ((StringBuilder)localObject).append(str);
          ((StringBuilder)localObject).append(" of ");
          ((StringBuilder)localObject).append(i);
          throw new ParserException(paramXmlResourceParser, ((StringBuilder)localObject).toString());
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw new ParserException(paramXmlResourceParser, "Invalid pin digest", localIllegalArgumentException);
        }
      }
      throw new ParserException(paramXmlResourceParser, "Missing pin digest");
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unsupported pin digest algorithm: ");
    ((StringBuilder)localObject).append(localIllegalArgumentException);
    throw new ParserException(paramXmlResourceParser, ((StringBuilder)localObject).toString());
  }
  
  private PinSet parsePinSet(XmlResourceParser paramXmlResourceParser)
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    String str = paramXmlResourceParser.getAttributeValue(null, "expiration");
    long l = Long.MAX_VALUE;
    if (str != null) {
      try
      {
        Object localObject = new java/text/SimpleDateFormat;
        ((SimpleDateFormat)localObject).<init>("yyyy-MM-dd");
        ((SimpleDateFormat)localObject).setLenient(false);
        localObject = ((SimpleDateFormat)localObject).parse(str);
        if (localObject != null)
        {
          l = ((Date)localObject).getTime();
        }
        else
        {
          localObject = new android/security/net/config/XmlConfigSource$ParserException;
          ((ParserException)localObject).<init>(paramXmlResourceParser, "Invalid expiration date in pin-set");
          throw ((Throwable)localObject);
        }
      }
      catch (ParseException localParseException)
      {
        throw new ParserException(paramXmlResourceParser, "Invalid expiration date in pin-set", localParseException);
      }
    }
    int i = paramXmlResourceParser.getDepth();
    ArraySet localArraySet = new ArraySet();
    while (XmlUtils.nextElementWithin(paramXmlResourceParser, i)) {
      if (paramXmlResourceParser.getName().equals("pin")) {
        localArraySet.add(parsePin(paramXmlResourceParser));
      } else {
        XmlUtils.skipCurrentTag(paramXmlResourceParser);
      }
    }
    return new PinSet(localArraySet, l);
  }
  
  private Collection<CertificatesEntryRef> parseTrustAnchors(XmlResourceParser paramXmlResourceParser, boolean paramBoolean)
    throws IOException, XmlPullParserException, XmlConfigSource.ParserException
  {
    int i = paramXmlResourceParser.getDepth();
    ArrayList localArrayList = new ArrayList();
    while (XmlUtils.nextElementWithin(paramXmlResourceParser, i)) {
      if (paramXmlResourceParser.getName().equals("certificates")) {
        localArrayList.add(parseCertificatesEntry(paramXmlResourceParser, paramBoolean));
      } else {
        XmlUtils.skipCurrentTag(paramXmlResourceParser);
      }
    }
    return localArrayList;
  }
  
  public NetworkSecurityConfig getDefaultConfig()
  {
    ensureInitialized();
    return mDefaultConfig;
  }
  
  public Set<Pair<Domain, NetworkSecurityConfig>> getPerDomainConfigs()
  {
    ensureInitialized();
    return mDomainMap;
  }
  
  public static class ParserException
    extends Exception
  {
    public ParserException(XmlPullParser paramXmlPullParser, String paramString)
    {
      this(paramXmlPullParser, paramString, null);
    }
    
    public ParserException(XmlPullParser paramXmlPullParser, String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }
}
