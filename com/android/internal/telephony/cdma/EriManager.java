package com.android.internal.telephony.cdma;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import java.util.HashMap;

public class EriManager
{
  private static final boolean DBG = true;
  static final int ERI_FROM_FILE_SYSTEM = 1;
  static final int ERI_FROM_MODEM = 2;
  public static final int ERI_FROM_XML = 0;
  private static final String LOG_TAG = "EriManager";
  private static final boolean VDBG = false;
  private Context mContext;
  private EriFile mEriFile;
  private int mEriFileSource = 0;
  private boolean mIsEriFileLoaded;
  private final Phone mPhone;
  
  public EriManager(Phone paramPhone, Context paramContext, int paramInt)
  {
    mPhone = paramPhone;
    mContext = paramContext;
    mEriFileSource = paramInt;
    mEriFile = new EriFile();
  }
  
  private EriDisplayInformation getEriDisplayInformation(int paramInt1, int paramInt2)
  {
    if (mIsEriFileLoaded)
    {
      localObject = getEriInfo(paramInt1);
      if (localObject != null) {
        return new EriDisplayInformation(iconIndex, iconMode, eriText);
      }
    }
    switch (paramInt1)
    {
    default: 
      if (mIsEriFileLoaded) {
        break label644;
      }
      Rlog.d("EriManager", "ERI File not loaded");
      if (paramInt2 <= 2) {
        break;
      }
    }
    for (Object localObject = new EriDisplayInformation(2, 1, mContext.getText(17040928).toString());; localObject = new EriDisplayInformation(-1, -1, "ERI text"))
    {
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040927).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040926).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040925).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040935).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040934).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040933).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040932).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040931).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040930).toString());
      break;
      localObject = new EriDisplayInformation(paramInt1, 0, mContext.getText(17040929).toString());
      break;
      localObject = new EriDisplayInformation(2, 1, mContext.getText(17040928).toString());
      break;
      localObject = new EriDisplayInformation(1, 0, mContext.getText(17040924).toString());
      break;
      localObject = new EriDisplayInformation(0, 0, mContext.getText(17040923).toString());
      break;
      switch (paramInt2)
      {
      }
    }
    return new EriDisplayInformation(2, 1, mContext.getText(17040928).toString());
    return new EriDisplayInformation(1, 0, mContext.getText(17040924).toString());
    return new EriDisplayInformation(0, 0, mContext.getText(17040923).toString());
    label644:
    EriInfo localEriInfo = getEriInfo(paramInt1);
    localObject = getEriInfo(paramInt2);
    if (localEriInfo == null)
    {
      if (localObject == null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("ERI defRoamInd ");
        ((StringBuilder)localObject).append(paramInt2);
        ((StringBuilder)localObject).append(" not found in ERI file ...on");
        Rlog.e("EriManager", ((StringBuilder)localObject).toString());
      }
      for (localObject = new EriDisplayInformation(0, 0, mContext.getText(17040923).toString());; localObject = new EriDisplayInformation(iconIndex, iconMode, eriText)) {
        break;
      }
    }
    localObject = new EriDisplayInformation(iconIndex, iconMode, eriText);
    return localObject;
  }
  
  private EriInfo getEriInfo(int paramInt)
  {
    if (mEriFile.mRoamIndTable.containsKey(Integer.valueOf(paramInt))) {
      return (EriInfo)mEriFile.mRoamIndTable.get(Integer.valueOf(paramInt));
    }
    return null;
  }
  
  private void loadEriFileFromFileSystem() {}
  
  private void loadEriFileFromModem() {}
  
  /* Error */
  private void loadEriFileFromXml()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: getfield 43	com/android/internal/telephony/cdma/EriManager:mContext	Landroid/content/Context;
    //   8: invokevirtual 159	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   11: astore_3
    //   12: aload_2
    //   13: astore 4
    //   15: aload_1
    //   16: astore 5
    //   18: ldc 23
    //   20: ldc -95
    //   22: invokestatic 79	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   25: pop
    //   26: aload_2
    //   27: astore 4
    //   29: aload_1
    //   30: astore 5
    //   32: new 163	java/io/FileInputStream
    //   35: astore 6
    //   37: aload_2
    //   38: astore 4
    //   40: aload_1
    //   41: astore 5
    //   43: aload 6
    //   45: aload_3
    //   46: ldc -92
    //   48: invokevirtual 170	android/content/res/Resources:getString	(I)Ljava/lang/String;
    //   51: invokespecial 173	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   54: aload 6
    //   56: astore 4
    //   58: aload 6
    //   60: astore 5
    //   62: invokestatic 179	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   65: astore_2
    //   66: aload 6
    //   68: astore 4
    //   70: aload 6
    //   72: astore 5
    //   74: aload_2
    //   75: aload 6
    //   77: aconst_null
    //   78: invokeinterface 185 3 0
    //   83: aload 6
    //   85: astore 4
    //   87: aload 6
    //   89: astore 5
    //   91: ldc 23
    //   93: ldc -69
    //   95: invokestatic 79	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   98: pop
    //   99: aload_2
    //   100: astore 4
    //   102: goto +40 -> 142
    //   105: astore 6
    //   107: ldc 23
    //   109: ldc -67
    //   111: invokestatic 79	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   114: pop
    //   115: aconst_null
    //   116: astore 6
    //   118: aload 4
    //   120: astore 5
    //   122: goto +28 -> 150
    //   125: astore 6
    //   127: ldc 23
    //   129: ldc -65
    //   131: invokestatic 79	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   134: pop
    //   135: aconst_null
    //   136: astore 4
    //   138: aload 5
    //   140: astore 6
    //   142: aload 6
    //   144: astore 5
    //   146: aload 4
    //   148: astore 6
    //   150: aload 6
    //   152: astore 4
    //   154: aload 6
    //   156: ifnonnull +183 -> 339
    //   159: aconst_null
    //   160: astore_2
    //   161: aload_0
    //   162: getfield 43	com/android/internal/telephony/cdma/EriManager:mContext	Landroid/content/Context;
    //   165: ldc -63
    //   167: invokevirtual 197	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   170: checkcast 199	android/telephony/CarrierConfigManager
    //   173: astore_1
    //   174: aload_2
    //   175: astore 4
    //   177: aload_1
    //   178: ifnull +30 -> 208
    //   181: aload_1
    //   182: aload_0
    //   183: getfield 41	com/android/internal/telephony/cdma/EriManager:mPhone	Lcom/android/internal/telephony/Phone;
    //   186: invokevirtual 205	com/android/internal/telephony/Phone:getSubId	()I
    //   189: invokevirtual 209	android/telephony/CarrierConfigManager:getConfigForSubId	(I)Landroid/os/PersistableBundle;
    //   192: astore_1
    //   193: aload_2
    //   194: astore 4
    //   196: aload_1
    //   197: ifnull +11 -> 208
    //   200: aload_1
    //   201: ldc -45
    //   203: invokevirtual 216	android/os/PersistableBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   206: astore 4
    //   208: new 108	java/lang/StringBuilder
    //   211: dup
    //   212: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   215: astore_2
    //   216: aload_2
    //   217: ldc -38
    //   219: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: pop
    //   223: aload_2
    //   224: aload 4
    //   226: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   229: pop
    //   230: ldc 23
    //   232: aload_2
    //   233: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   236: invokestatic 79	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   239: pop
    //   240: aload 4
    //   242: ifnonnull +12 -> 254
    //   245: ldc 23
    //   247: ldc -36
    //   249: invokestatic 124	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   252: pop
    //   253: return
    //   254: invokestatic 179	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   257: astore_2
    //   258: aload_2
    //   259: astore 6
    //   261: aload_2
    //   262: aload_0
    //   263: getfield 43	com/android/internal/telephony/cdma/EriManager:mContext	Landroid/content/Context;
    //   266: invokevirtual 224	android/content/Context:getAssets	()Landroid/content/res/AssetManager;
    //   269: aload 4
    //   271: invokevirtual 230	android/content/res/AssetManager:open	(Ljava/lang/String;)Ljava/io/InputStream;
    //   274: aconst_null
    //   275: invokeinterface 185 3 0
    //   280: aload_2
    //   281: astore 4
    //   283: goto +56 -> 339
    //   286: astore_2
    //   287: new 108	java/lang/StringBuilder
    //   290: dup
    //   291: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   294: astore_1
    //   295: aload_1
    //   296: ldc -24
    //   298: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   301: pop
    //   302: aload_1
    //   303: aload 4
    //   305: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   308: pop
    //   309: aload_1
    //   310: ldc -22
    //   312: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: pop
    //   316: aload_1
    //   317: aload_2
    //   318: invokevirtual 235	java/lang/Exception:toString	()Ljava/lang/String;
    //   321: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   324: pop
    //   325: ldc 23
    //   327: aload_1
    //   328: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   331: invokestatic 124	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   334: pop
    //   335: aload 6
    //   337: astore 4
    //   339: aload 4
    //   341: ldc -20
    //   343: invokestatic 242	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   346: aload_0
    //   347: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   350: aload 4
    //   352: aconst_null
    //   353: ldc -12
    //   355: invokeinterface 248 3 0
    //   360: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   363: putfield 255	com/android/internal/telephony/cdma/EriManager$EriFile:mVersionNumber	I
    //   366: aload_0
    //   367: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   370: aload 4
    //   372: aconst_null
    //   373: ldc_w 257
    //   376: invokeinterface 248 3 0
    //   381: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   384: putfield 260	com/android/internal/telephony/cdma/EriManager$EriFile:mNumberOfEriEntries	I
    //   387: aload_0
    //   388: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   391: aload 4
    //   393: aconst_null
    //   394: ldc_w 262
    //   397: invokeinterface 248 3 0
    //   402: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   405: putfield 265	com/android/internal/telephony/cdma/EriManager$EriFile:mEriFileType	I
    //   408: iconst_0
    //   409: istore 7
    //   411: aload 4
    //   413: invokestatic 269	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   416: aload 4
    //   418: invokeinterface 272 1 0
    //   423: astore 6
    //   425: aload 6
    //   427: ifnonnull +185 -> 612
    //   430: iload 7
    //   432: aload_0
    //   433: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   436: getfield 260	com/android/internal/telephony/cdma/EriManager$EriFile:mNumberOfEriEntries	I
    //   439: if_icmpeq +72 -> 511
    //   442: new 108	java/lang/StringBuilder
    //   445: astore 6
    //   447: aload 6
    //   449: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   452: aload 6
    //   454: ldc_w 274
    //   457: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   460: pop
    //   461: aload 6
    //   463: aload_0
    //   464: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   467: getfield 260	com/android/internal/telephony/cdma/EriManager$EriFile:mNumberOfEriEntries	I
    //   470: invokevirtual 118	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   473: pop
    //   474: aload 6
    //   476: ldc_w 276
    //   479: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   482: pop
    //   483: aload 6
    //   485: iload 7
    //   487: invokevirtual 118	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   490: pop
    //   491: aload 6
    //   493: ldc_w 278
    //   496: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   499: pop
    //   500: ldc 23
    //   502: aload 6
    //   504: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   507: invokestatic 124	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   510: pop
    //   511: new 108	java/lang/StringBuilder
    //   514: astore 6
    //   516: aload 6
    //   518: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   521: aload 6
    //   523: ldc_w 280
    //   526: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   529: pop
    //   530: aload 6
    //   532: aload_0
    //   533: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   536: getfield 255	com/android/internal/telephony/cdma/EriManager$EriFile:mVersionNumber	I
    //   539: invokevirtual 118	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   542: pop
    //   543: aload 6
    //   545: ldc_w 282
    //   548: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   551: pop
    //   552: aload 6
    //   554: aload_0
    //   555: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   558: getfield 260	com/android/internal/telephony/cdma/EriManager$EriFile:mNumberOfEriEntries	I
    //   561: invokevirtual 118	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   564: pop
    //   565: ldc 23
    //   567: aload 6
    //   569: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   572: invokestatic 79	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   575: pop
    //   576: aload_0
    //   577: iconst_1
    //   578: putfield 53	com/android/internal/telephony/cdma/EriManager:mIsEriFileLoaded	Z
    //   581: aload 4
    //   583: instanceof 284
    //   586: ifeq +13 -> 599
    //   589: aload 4
    //   591: checkcast 284	android/content/res/XmlResourceParser
    //   594: invokeinterface 287 1 0
    //   599: aload 5
    //   601: ifnull +346 -> 947
    //   604: aload 5
    //   606: invokevirtual 288	java/io/FileInputStream:close	()V
    //   609: goto +338 -> 947
    //   612: aload 6
    //   614: ldc_w 290
    //   617: invokevirtual 295	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   620: ifeq +112 -> 732
    //   623: aload 4
    //   625: aconst_null
    //   626: ldc_w 297
    //   629: invokeinterface 248 3 0
    //   634: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   637: istore 8
    //   639: aload 4
    //   641: aconst_null
    //   642: ldc_w 299
    //   645: invokeinterface 248 3 0
    //   650: astore 6
    //   652: iload 8
    //   654: iflt +24 -> 678
    //   657: iload 8
    //   659: iconst_2
    //   660: if_icmpgt +18 -> 678
    //   663: aload_0
    //   664: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   667: getfield 303	com/android/internal/telephony/cdma/EriManager$EriFile:mCallPromptId	[Ljava/lang/String;
    //   670: iload 8
    //   672: aload 6
    //   674: aastore
    //   675: goto +50 -> 725
    //   678: new 108	java/lang/StringBuilder
    //   681: astore 6
    //   683: aload 6
    //   685: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   688: aload 6
    //   690: ldc_w 305
    //   693: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   696: pop
    //   697: aload 6
    //   699: iload 8
    //   701: invokevirtual 118	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   704: pop
    //   705: aload 6
    //   707: ldc_w 307
    //   710: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   713: pop
    //   714: ldc 23
    //   716: aload 6
    //   718: invokevirtual 121	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   721: invokestatic 124	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   724: pop
    //   725: iload 7
    //   727: istore 8
    //   729: goto +157 -> 886
    //   732: iload 7
    //   734: istore 8
    //   736: aload 6
    //   738: ldc_w 309
    //   741: invokevirtual 295	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   744: ifeq +142 -> 886
    //   747: aload 4
    //   749: aconst_null
    //   750: ldc_w 311
    //   753: invokeinterface 248 3 0
    //   758: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   761: istore 9
    //   763: aload 4
    //   765: aconst_null
    //   766: ldc_w 313
    //   769: invokeinterface 248 3 0
    //   774: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   777: istore 10
    //   779: aload 4
    //   781: aconst_null
    //   782: ldc_w 315
    //   785: invokeinterface 248 3 0
    //   790: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   793: istore 11
    //   795: aload 4
    //   797: aconst_null
    //   798: ldc_w 317
    //   801: invokeinterface 248 3 0
    //   806: astore_2
    //   807: aload 4
    //   809: aconst_null
    //   810: ldc_w 290
    //   813: invokeinterface 248 3 0
    //   818: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   821: istore 12
    //   823: aload 4
    //   825: aconst_null
    //   826: ldc_w 319
    //   829: invokeinterface 248 3 0
    //   834: invokestatic 252	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   837: istore 13
    //   839: iload 7
    //   841: iconst_1
    //   842: iadd
    //   843: istore 8
    //   845: aload_0
    //   846: getfield 48	com/android/internal/telephony/cdma/EriManager:mEriFile	Lcom/android/internal/telephony/cdma/EriManager$EriFile;
    //   849: getfield 128	com/android/internal/telephony/cdma/EriManager$EriFile:mRoamIndTable	Ljava/util/HashMap;
    //   852: astore_1
    //   853: new 59	com/android/internal/telephony/cdma/EriInfo
    //   856: astore 6
    //   858: aload 6
    //   860: iload 9
    //   862: iload 10
    //   864: iload 11
    //   866: aload_2
    //   867: iload 12
    //   869: iload 13
    //   871: invokespecial 322	com/android/internal/telephony/cdma/EriInfo:<init>	(IIILjava/lang/String;II)V
    //   874: aload_1
    //   875: iload 9
    //   877: invokestatic 134	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   880: aload 6
    //   882: invokevirtual 326	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   885: pop
    //   886: iload 8
    //   888: istore 7
    //   890: goto -479 -> 411
    //   893: astore 6
    //   895: goto +53 -> 948
    //   898: astore 6
    //   900: ldc 23
    //   902: ldc_w 328
    //   905: aload 6
    //   907: invokestatic 331	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   910: pop
    //   911: aload 4
    //   913: instanceof 284
    //   916: ifeq +13 -> 929
    //   919: aload 4
    //   921: checkcast 284	android/content/res/XmlResourceParser
    //   924: invokeinterface 287 1 0
    //   929: aload 5
    //   931: ifnull +16 -> 947
    //   934: aload 5
    //   936: invokevirtual 288	java/io/FileInputStream:close	()V
    //   939: goto +8 -> 947
    //   942: astore 6
    //   944: goto +3 -> 947
    //   947: return
    //   948: aload 4
    //   950: instanceof 284
    //   953: ifeq +13 -> 966
    //   956: aload 4
    //   958: checkcast 284	android/content/res/XmlResourceParser
    //   961: invokeinterface 287 1 0
    //   966: aload 5
    //   968: ifnull +13 -> 981
    //   971: aload 5
    //   973: invokevirtual 288	java/io/FileInputStream:close	()V
    //   976: goto +5 -> 981
    //   979: astore 4
    //   981: aload 6
    //   983: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	984	0	this	EriManager
    //   1	874	1	localObject1	Object
    //   3	278	2	localObject2	Object
    //   286	32	2	localIOException1	java.io.IOException
    //   806	61	2	str	String
    //   11	35	3	localResources	android.content.res.Resources
    //   13	944	4	localObject3	Object
    //   979	1	4	localIOException2	java.io.IOException
    //   16	956	5	localObject4	Object
    //   35	53	6	localFileInputStream	java.io.FileInputStream
    //   105	1	6	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   116	1	6	localObject5	Object
    //   125	1	6	localFileNotFoundException	java.io.FileNotFoundException
    //   140	741	6	localObject6	Object
    //   893	1	6	localObject7	Object
    //   898	8	6	localException	Exception
    //   942	40	6	localIOException3	java.io.IOException
    //   409	480	7	i	int
    //   637	250	8	j	int
    //   761	115	9	k	int
    //   777	86	10	m	int
    //   793	72	11	n	int
    //   821	47	12	i1	int
    //   837	33	13	i2	int
    // Exception table:
    //   from	to	target	type
    //   18	26	105	org/xmlpull/v1/XmlPullParserException
    //   32	37	105	org/xmlpull/v1/XmlPullParserException
    //   43	54	105	org/xmlpull/v1/XmlPullParserException
    //   62	66	105	org/xmlpull/v1/XmlPullParserException
    //   74	83	105	org/xmlpull/v1/XmlPullParserException
    //   91	99	105	org/xmlpull/v1/XmlPullParserException
    //   18	26	125	java/io/FileNotFoundException
    //   32	37	125	java/io/FileNotFoundException
    //   43	54	125	java/io/FileNotFoundException
    //   62	66	125	java/io/FileNotFoundException
    //   74	83	125	java/io/FileNotFoundException
    //   91	99	125	java/io/FileNotFoundException
    //   254	258	286	java/io/IOException
    //   254	258	286	org/xmlpull/v1/XmlPullParserException
    //   261	280	286	java/io/IOException
    //   261	280	286	org/xmlpull/v1/XmlPullParserException
    //   339	408	893	finally
    //   411	425	893	finally
    //   430	511	893	finally
    //   511	581	893	finally
    //   612	652	893	finally
    //   663	675	893	finally
    //   678	725	893	finally
    //   736	839	893	finally
    //   845	886	893	finally
    //   900	911	893	finally
    //   339	408	898	java/lang/Exception
    //   411	425	898	java/lang/Exception
    //   430	511	898	java/lang/Exception
    //   511	581	898	java/lang/Exception
    //   612	652	898	java/lang/Exception
    //   663	675	898	java/lang/Exception
    //   678	725	898	java/lang/Exception
    //   736	839	898	java/lang/Exception
    //   845	886	898	java/lang/Exception
    //   604	609	942	java/io/IOException
    //   934	939	942	java/io/IOException
    //   971	976	979	java/io/IOException
  }
  
  public void dispose()
  {
    mEriFile = new EriFile();
    mIsEriFileLoaded = false;
  }
  
  public int getCdmaEriIconIndex(int paramInt1, int paramInt2)
  {
    return getEriDisplayInformationmEriIconIndex;
  }
  
  public int getCdmaEriIconMode(int paramInt1, int paramInt2)
  {
    return getEriDisplayInformationmEriIconMode;
  }
  
  public String getCdmaEriText(int paramInt1, int paramInt2)
  {
    return getEriDisplayInformationmEriIconText;
  }
  
  public int getEriFileType()
  {
    return mEriFile.mEriFileType;
  }
  
  public int getEriFileVersion()
  {
    return mEriFile.mVersionNumber;
  }
  
  public int getEriNumberOfEntries()
  {
    return mEriFile.mNumberOfEriEntries;
  }
  
  public boolean isEriFileLoaded()
  {
    return mIsEriFileLoaded;
  }
  
  public void loadEriFile()
  {
    switch (mEriFileSource)
    {
    default: 
      loadEriFileFromXml();
      break;
    case 2: 
      loadEriFileFromModem();
      break;
    case 1: 
      loadEriFileFromFileSystem();
    }
  }
  
  class EriDisplayInformation
  {
    int mEriIconIndex;
    int mEriIconMode;
    String mEriIconText;
    
    EriDisplayInformation(int paramInt1, int paramInt2, String paramString)
    {
      mEriIconIndex = paramInt1;
      mEriIconMode = paramInt2;
      mEriIconText = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("EriDisplayInformation: { IconIndex: ");
      localStringBuilder.append(mEriIconIndex);
      localStringBuilder.append(" EriIconMode: ");
      localStringBuilder.append(mEriIconMode);
      localStringBuilder.append(" EriIconText: ");
      localStringBuilder.append(mEriIconText);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  class EriFile
  {
    String[] mCallPromptId = { "", "", "" };
    int mEriFileType = -1;
    int mNumberOfEriEntries = 0;
    HashMap<Integer, EriInfo> mRoamIndTable = new HashMap();
    int mVersionNumber = -1;
    
    EriFile() {}
  }
}
