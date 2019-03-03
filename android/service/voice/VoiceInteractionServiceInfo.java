package android.service.voice;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;

public class VoiceInteractionServiceInfo
{
  static final String TAG = "VoiceInteractionServiceInfo";
  private String mParseError;
  private String mRecognitionService;
  private ServiceInfo mServiceInfo;
  private String mSessionService;
  private String mSettingsActivity;
  private boolean mSupportsAssist;
  private boolean mSupportsLaunchFromKeyguard;
  private boolean mSupportsLocalInteraction;
  
  public VoiceInteractionServiceInfo(PackageManager paramPackageManager, ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    this(paramPackageManager, paramPackageManager.getServiceInfo(paramComponentName, 128));
  }
  
  public VoiceInteractionServiceInfo(PackageManager paramPackageManager, ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    this(paramPackageManager, getServiceInfoOrThrow(paramComponentName, paramInt));
  }
  
  /* Error */
  public VoiceInteractionServiceInfo(PackageManager paramPackageManager, ServiceInfo paramServiceInfo)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 44	java/lang/Object:<init>	()V
    //   4: aload_2
    //   5: ifnonnull +10 -> 15
    //   8: aload_0
    //   9: ldc 46
    //   11: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   14: return
    //   15: ldc 50
    //   17: aload_2
    //   18: getfield 55	android/content/pm/ServiceInfo:permission	Ljava/lang/String;
    //   21: invokevirtual 61	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   24: ifne +10 -> 34
    //   27: aload_0
    //   28: ldc 63
    //   30: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   33: return
    //   34: aconst_null
    //   35: astore_3
    //   36: aconst_null
    //   37: astore 4
    //   39: aconst_null
    //   40: astore 5
    //   42: aconst_null
    //   43: astore 6
    //   45: aload_2
    //   46: aload_1
    //   47: ldc 65
    //   49: invokevirtual 69	android/content/pm/ServiceInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   52: astore 7
    //   54: aload 7
    //   56: ifnonnull +123 -> 179
    //   59: aload 7
    //   61: astore 6
    //   63: aload 7
    //   65: astore_3
    //   66: aload 7
    //   68: astore 4
    //   70: aload 7
    //   72: astore 5
    //   74: new 71	java/lang/StringBuilder
    //   77: astore_1
    //   78: aload 7
    //   80: astore 6
    //   82: aload 7
    //   84: astore_3
    //   85: aload 7
    //   87: astore 4
    //   89: aload 7
    //   91: astore 5
    //   93: aload_1
    //   94: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   97: aload 7
    //   99: astore 6
    //   101: aload 7
    //   103: astore_3
    //   104: aload 7
    //   106: astore 4
    //   108: aload 7
    //   110: astore 5
    //   112: aload_1
    //   113: ldc 74
    //   115: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: aload 7
    //   121: astore 6
    //   123: aload 7
    //   125: astore_3
    //   126: aload 7
    //   128: astore 4
    //   130: aload 7
    //   132: astore 5
    //   134: aload_1
    //   135: aload_2
    //   136: getfield 81	android/content/pm/ServiceInfo:packageName	Ljava/lang/String;
    //   139: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: aload 7
    //   145: astore 6
    //   147: aload 7
    //   149: astore_3
    //   150: aload 7
    //   152: astore 4
    //   154: aload 7
    //   156: astore 5
    //   158: aload_0
    //   159: aload_1
    //   160: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   163: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   166: aload 7
    //   168: ifnull +10 -> 178
    //   171: aload 7
    //   173: invokeinterface 90 1 0
    //   178: return
    //   179: aload 7
    //   181: astore 6
    //   183: aload 7
    //   185: astore_3
    //   186: aload 7
    //   188: astore 4
    //   190: aload 7
    //   192: astore 5
    //   194: aload_1
    //   195: aload_2
    //   196: getfield 94	android/content/pm/ServiceInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   199: invokevirtual 98	android/content/pm/PackageManager:getResourcesForApplication	(Landroid/content/pm/ApplicationInfo;)Landroid/content/res/Resources;
    //   202: astore 8
    //   204: aload 7
    //   206: astore 6
    //   208: aload 7
    //   210: astore_3
    //   211: aload 7
    //   213: astore 4
    //   215: aload 7
    //   217: astore 5
    //   219: aload 7
    //   221: invokestatic 104	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   224: astore_1
    //   225: aload 7
    //   227: astore 6
    //   229: aload 7
    //   231: astore_3
    //   232: aload 7
    //   234: astore 4
    //   236: aload 7
    //   238: astore 5
    //   240: aload 7
    //   242: invokeinterface 108 1 0
    //   247: istore 9
    //   249: iload 9
    //   251: iconst_1
    //   252: if_icmpeq +12 -> 264
    //   255: iload 9
    //   257: iconst_2
    //   258: if_icmpeq +6 -> 264
    //   261: goto -36 -> 225
    //   264: aload 7
    //   266: astore 6
    //   268: aload 7
    //   270: astore_3
    //   271: aload 7
    //   273: astore 4
    //   275: aload 7
    //   277: astore 5
    //   279: ldc 110
    //   281: aload 7
    //   283: invokeinterface 113 1 0
    //   288: invokevirtual 61	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   291: ifne +37 -> 328
    //   294: aload 7
    //   296: astore 6
    //   298: aload 7
    //   300: astore_3
    //   301: aload 7
    //   303: astore 4
    //   305: aload 7
    //   307: astore 5
    //   309: aload_0
    //   310: ldc 115
    //   312: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   315: aload 7
    //   317: ifnull +10 -> 327
    //   320: aload 7
    //   322: invokeinterface 90 1 0
    //   327: return
    //   328: aload 7
    //   330: astore 6
    //   332: aload 7
    //   334: astore_3
    //   335: aload 7
    //   337: astore 4
    //   339: aload 7
    //   341: astore 5
    //   343: aload 8
    //   345: aload_1
    //   346: getstatic 121	com/android/internal/R$styleable:VoiceInteractionService	[I
    //   349: invokevirtual 127	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   352: astore_1
    //   353: aload 7
    //   355: astore 6
    //   357: aload 7
    //   359: astore_3
    //   360: aload 7
    //   362: astore 4
    //   364: aload 7
    //   366: astore 5
    //   368: aload_0
    //   369: aload_1
    //   370: iconst_1
    //   371: invokevirtual 133	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   374: putfield 135	android/service/voice/VoiceInteractionServiceInfo:mSessionService	Ljava/lang/String;
    //   377: aload 7
    //   379: astore 6
    //   381: aload 7
    //   383: astore_3
    //   384: aload 7
    //   386: astore 4
    //   388: aload 7
    //   390: astore 5
    //   392: aload_0
    //   393: aload_1
    //   394: iconst_2
    //   395: invokevirtual 133	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   398: putfield 137	android/service/voice/VoiceInteractionServiceInfo:mRecognitionService	Ljava/lang/String;
    //   401: aload 7
    //   403: astore 6
    //   405: aload 7
    //   407: astore_3
    //   408: aload 7
    //   410: astore 4
    //   412: aload 7
    //   414: astore 5
    //   416: aload_0
    //   417: aload_1
    //   418: iconst_0
    //   419: invokevirtual 133	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   422: putfield 139	android/service/voice/VoiceInteractionServiceInfo:mSettingsActivity	Ljava/lang/String;
    //   425: aload 7
    //   427: astore 6
    //   429: aload 7
    //   431: astore_3
    //   432: aload 7
    //   434: astore 4
    //   436: aload 7
    //   438: astore 5
    //   440: aload_0
    //   441: aload_1
    //   442: iconst_3
    //   443: iconst_0
    //   444: invokevirtual 143	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   447: putfield 145	android/service/voice/VoiceInteractionServiceInfo:mSupportsAssist	Z
    //   450: aload 7
    //   452: astore 6
    //   454: aload 7
    //   456: astore_3
    //   457: aload 7
    //   459: astore 4
    //   461: aload 7
    //   463: astore 5
    //   465: aload_0
    //   466: aload_1
    //   467: iconst_4
    //   468: iconst_0
    //   469: invokevirtual 143	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   472: putfield 147	android/service/voice/VoiceInteractionServiceInfo:mSupportsLaunchFromKeyguard	Z
    //   475: aload 7
    //   477: astore 6
    //   479: aload 7
    //   481: astore_3
    //   482: aload 7
    //   484: astore 4
    //   486: aload 7
    //   488: astore 5
    //   490: aload_0
    //   491: aload_1
    //   492: iconst_5
    //   493: iconst_0
    //   494: invokevirtual 143	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   497: putfield 149	android/service/voice/VoiceInteractionServiceInfo:mSupportsLocalInteraction	Z
    //   500: aload 7
    //   502: astore 6
    //   504: aload 7
    //   506: astore_3
    //   507: aload 7
    //   509: astore 4
    //   511: aload 7
    //   513: astore 5
    //   515: aload_1
    //   516: invokevirtual 152	android/content/res/TypedArray:recycle	()V
    //   519: aload 7
    //   521: astore 6
    //   523: aload 7
    //   525: astore_3
    //   526: aload 7
    //   528: astore 4
    //   530: aload 7
    //   532: astore 5
    //   534: aload_0
    //   535: getfield 135	android/service/voice/VoiceInteractionServiceInfo:mSessionService	Ljava/lang/String;
    //   538: ifnonnull +37 -> 575
    //   541: aload 7
    //   543: astore 6
    //   545: aload 7
    //   547: astore_3
    //   548: aload 7
    //   550: astore 4
    //   552: aload 7
    //   554: astore 5
    //   556: aload_0
    //   557: ldc -102
    //   559: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   562: aload 7
    //   564: ifnull +10 -> 574
    //   567: aload 7
    //   569: invokeinterface 90 1 0
    //   574: return
    //   575: aload 7
    //   577: astore 6
    //   579: aload 7
    //   581: astore_3
    //   582: aload 7
    //   584: astore 4
    //   586: aload 7
    //   588: astore 5
    //   590: aload_0
    //   591: getfield 137	android/service/voice/VoiceInteractionServiceInfo:mRecognitionService	Ljava/lang/String;
    //   594: ifnonnull +37 -> 631
    //   597: aload 7
    //   599: astore 6
    //   601: aload 7
    //   603: astore_3
    //   604: aload 7
    //   606: astore 4
    //   608: aload 7
    //   610: astore 5
    //   612: aload_0
    //   613: ldc -100
    //   615: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   618: aload 7
    //   620: ifnull +10 -> 630
    //   623: aload 7
    //   625: invokeinterface 90 1 0
    //   630: return
    //   631: aload 7
    //   633: ifnull +10 -> 643
    //   636: aload 7
    //   638: invokeinterface 90 1 0
    //   643: aload_0
    //   644: aload_2
    //   645: putfield 158	android/service/voice/VoiceInteractionServiceInfo:mServiceInfo	Landroid/content/pm/ServiceInfo;
    //   648: return
    //   649: astore_1
    //   650: goto +223 -> 873
    //   653: astore_2
    //   654: aload_3
    //   655: astore 6
    //   657: new 71	java/lang/StringBuilder
    //   660: astore_1
    //   661: aload_3
    //   662: astore 6
    //   664: aload_1
    //   665: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   668: aload_3
    //   669: astore 6
    //   671: aload_1
    //   672: ldc -96
    //   674: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   677: pop
    //   678: aload_3
    //   679: astore 6
    //   681: aload_1
    //   682: aload_2
    //   683: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   686: pop
    //   687: aload_3
    //   688: astore 6
    //   690: aload_0
    //   691: aload_1
    //   692: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   695: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   698: aload_3
    //   699: astore 6
    //   701: ldc 8
    //   703: ldc -91
    //   705: aload_2
    //   706: invokestatic 171	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   709: pop
    //   710: aload_3
    //   711: ifnull +9 -> 720
    //   714: aload_3
    //   715: invokeinterface 90 1 0
    //   720: return
    //   721: astore_2
    //   722: aload 4
    //   724: astore 6
    //   726: new 71	java/lang/StringBuilder
    //   729: astore_1
    //   730: aload 4
    //   732: astore 6
    //   734: aload_1
    //   735: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   738: aload 4
    //   740: astore 6
    //   742: aload_1
    //   743: ldc -96
    //   745: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   748: pop
    //   749: aload 4
    //   751: astore 6
    //   753: aload_1
    //   754: aload_2
    //   755: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   758: pop
    //   759: aload 4
    //   761: astore 6
    //   763: aload_0
    //   764: aload_1
    //   765: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   768: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   771: aload 4
    //   773: astore 6
    //   775: ldc 8
    //   777: ldc -91
    //   779: aload_2
    //   780: invokestatic 171	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   783: pop
    //   784: aload 4
    //   786: ifnull +10 -> 796
    //   789: aload 4
    //   791: invokeinterface 90 1 0
    //   796: return
    //   797: astore_1
    //   798: aload 5
    //   800: astore 6
    //   802: new 71	java/lang/StringBuilder
    //   805: astore_2
    //   806: aload 5
    //   808: astore 6
    //   810: aload_2
    //   811: invokespecial 72	java/lang/StringBuilder:<init>	()V
    //   814: aload 5
    //   816: astore 6
    //   818: aload_2
    //   819: ldc -96
    //   821: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   824: pop
    //   825: aload 5
    //   827: astore 6
    //   829: aload_2
    //   830: aload_1
    //   831: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   834: pop
    //   835: aload 5
    //   837: astore 6
    //   839: aload_0
    //   840: aload_2
    //   841: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   844: putfield 48	android/service/voice/VoiceInteractionServiceInfo:mParseError	Ljava/lang/String;
    //   847: aload 5
    //   849: astore 6
    //   851: ldc 8
    //   853: ldc -91
    //   855: aload_1
    //   856: invokestatic 171	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   859: pop
    //   860: aload 5
    //   862: ifnull +10 -> 872
    //   865: aload 5
    //   867: invokeinterface 90 1 0
    //   872: return
    //   873: aload 6
    //   875: ifnull +10 -> 885
    //   878: aload 6
    //   880: invokeinterface 90 1 0
    //   885: aload_1
    //   886: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	887	0	this	VoiceInteractionServiceInfo
    //   0	887	1	paramPackageManager	PackageManager
    //   0	887	2	paramServiceInfo	ServiceInfo
    //   35	680	3	localObject1	Object
    //   37	753	4	localObject2	Object
    //   40	826	5	localObject3	Object
    //   43	836	6	localObject4	Object
    //   52	585	7	localXmlResourceParser	android.content.res.XmlResourceParser
    //   202	142	8	localResources	android.content.res.Resources
    //   247	12	9	i	int
    // Exception table:
    //   from	to	target	type
    //   45	54	649	finally
    //   74	78	649	finally
    //   93	97	649	finally
    //   112	119	649	finally
    //   134	143	649	finally
    //   158	166	649	finally
    //   194	204	649	finally
    //   219	225	649	finally
    //   240	249	649	finally
    //   279	294	649	finally
    //   309	315	649	finally
    //   343	353	649	finally
    //   368	377	649	finally
    //   392	401	649	finally
    //   416	425	649	finally
    //   440	450	649	finally
    //   465	475	649	finally
    //   490	500	649	finally
    //   515	519	649	finally
    //   534	541	649	finally
    //   556	562	649	finally
    //   590	597	649	finally
    //   612	618	649	finally
    //   657	661	649	finally
    //   664	668	649	finally
    //   671	678	649	finally
    //   681	687	649	finally
    //   690	698	649	finally
    //   701	710	649	finally
    //   726	730	649	finally
    //   734	738	649	finally
    //   742	749	649	finally
    //   753	759	649	finally
    //   763	771	649	finally
    //   775	784	649	finally
    //   802	806	649	finally
    //   810	814	649	finally
    //   818	825	649	finally
    //   829	835	649	finally
    //   839	847	649	finally
    //   851	860	649	finally
    //   45	54	653	android/content/pm/PackageManager$NameNotFoundException
    //   74	78	653	android/content/pm/PackageManager$NameNotFoundException
    //   93	97	653	android/content/pm/PackageManager$NameNotFoundException
    //   112	119	653	android/content/pm/PackageManager$NameNotFoundException
    //   134	143	653	android/content/pm/PackageManager$NameNotFoundException
    //   158	166	653	android/content/pm/PackageManager$NameNotFoundException
    //   194	204	653	android/content/pm/PackageManager$NameNotFoundException
    //   219	225	653	android/content/pm/PackageManager$NameNotFoundException
    //   240	249	653	android/content/pm/PackageManager$NameNotFoundException
    //   279	294	653	android/content/pm/PackageManager$NameNotFoundException
    //   309	315	653	android/content/pm/PackageManager$NameNotFoundException
    //   343	353	653	android/content/pm/PackageManager$NameNotFoundException
    //   368	377	653	android/content/pm/PackageManager$NameNotFoundException
    //   392	401	653	android/content/pm/PackageManager$NameNotFoundException
    //   416	425	653	android/content/pm/PackageManager$NameNotFoundException
    //   440	450	653	android/content/pm/PackageManager$NameNotFoundException
    //   465	475	653	android/content/pm/PackageManager$NameNotFoundException
    //   490	500	653	android/content/pm/PackageManager$NameNotFoundException
    //   515	519	653	android/content/pm/PackageManager$NameNotFoundException
    //   534	541	653	android/content/pm/PackageManager$NameNotFoundException
    //   556	562	653	android/content/pm/PackageManager$NameNotFoundException
    //   590	597	653	android/content/pm/PackageManager$NameNotFoundException
    //   612	618	653	android/content/pm/PackageManager$NameNotFoundException
    //   45	54	721	java/io/IOException
    //   74	78	721	java/io/IOException
    //   93	97	721	java/io/IOException
    //   112	119	721	java/io/IOException
    //   134	143	721	java/io/IOException
    //   158	166	721	java/io/IOException
    //   194	204	721	java/io/IOException
    //   219	225	721	java/io/IOException
    //   240	249	721	java/io/IOException
    //   279	294	721	java/io/IOException
    //   309	315	721	java/io/IOException
    //   343	353	721	java/io/IOException
    //   368	377	721	java/io/IOException
    //   392	401	721	java/io/IOException
    //   416	425	721	java/io/IOException
    //   440	450	721	java/io/IOException
    //   465	475	721	java/io/IOException
    //   490	500	721	java/io/IOException
    //   515	519	721	java/io/IOException
    //   534	541	721	java/io/IOException
    //   556	562	721	java/io/IOException
    //   590	597	721	java/io/IOException
    //   612	618	721	java/io/IOException
    //   45	54	797	org/xmlpull/v1/XmlPullParserException
    //   74	78	797	org/xmlpull/v1/XmlPullParserException
    //   93	97	797	org/xmlpull/v1/XmlPullParserException
    //   112	119	797	org/xmlpull/v1/XmlPullParserException
    //   134	143	797	org/xmlpull/v1/XmlPullParserException
    //   158	166	797	org/xmlpull/v1/XmlPullParserException
    //   194	204	797	org/xmlpull/v1/XmlPullParserException
    //   219	225	797	org/xmlpull/v1/XmlPullParserException
    //   240	249	797	org/xmlpull/v1/XmlPullParserException
    //   279	294	797	org/xmlpull/v1/XmlPullParserException
    //   309	315	797	org/xmlpull/v1/XmlPullParserException
    //   343	353	797	org/xmlpull/v1/XmlPullParserException
    //   368	377	797	org/xmlpull/v1/XmlPullParserException
    //   392	401	797	org/xmlpull/v1/XmlPullParserException
    //   416	425	797	org/xmlpull/v1/XmlPullParserException
    //   440	450	797	org/xmlpull/v1/XmlPullParserException
    //   465	475	797	org/xmlpull/v1/XmlPullParserException
    //   490	500	797	org/xmlpull/v1/XmlPullParserException
    //   515	519	797	org/xmlpull/v1/XmlPullParserException
    //   534	541	797	org/xmlpull/v1/XmlPullParserException
    //   556	562	797	org/xmlpull/v1/XmlPullParserException
    //   590	597	797	org/xmlpull/v1/XmlPullParserException
    //   612	618	797	org/xmlpull/v1/XmlPullParserException
  }
  
  static ServiceInfo getServiceInfoOrThrow(ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      ServiceInfo localServiceInfo = AppGlobals.getPackageManager().getServiceInfo(paramComponentName, 269222016, paramInt);
      if (localServiceInfo != null) {
        return localServiceInfo;
      }
    }
    catch (RemoteException localRemoteException) {}
    throw new PackageManager.NameNotFoundException(paramComponentName.toString());
  }
  
  public String getParseError()
  {
    return mParseError;
  }
  
  public String getRecognitionService()
  {
    return mRecognitionService;
  }
  
  public ServiceInfo getServiceInfo()
  {
    return mServiceInfo;
  }
  
  public String getSessionService()
  {
    return mSessionService;
  }
  
  public String getSettingsActivity()
  {
    return mSettingsActivity;
  }
  
  public boolean getSupportsAssist()
  {
    return mSupportsAssist;
  }
  
  public boolean getSupportsLaunchFromKeyguard()
  {
    return mSupportsLaunchFromKeyguard;
  }
  
  public boolean getSupportsLocalInteraction()
  {
    return mSupportsLocalInteraction;
  }
}
