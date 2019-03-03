package com.android.internal.telephony;

import android.os.Build;
import android.telephony.Rlog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.SparseIntArray;
import com.android.internal.telephony.cdma.sms.UserData;

public class Sms7BitEncodingTranslator
{
  private static final boolean DBG = Build.IS_DEBUGGABLE;
  private static final String TAG = "Sms7BitEncodingTranslator";
  private static final String XML_CHARACTOR_TAG = "Character";
  private static final String XML_FROM_TAG = "from";
  private static final String XML_START_TAG = "SmsEnforce7BitTranslationTable";
  private static final String XML_TO_TAG = "to";
  private static final String XML_TRANSLATION_TYPE_TAG = "TranslationType";
  private static boolean mIs7BitTranslationTableLoaded = false;
  private static SparseIntArray mTranslationTable = null;
  private static SparseIntArray mTranslationTableCDMA = null;
  private static SparseIntArray mTranslationTableCommon = null;
  private static SparseIntArray mTranslationTableGSM = null;
  
  public Sms7BitEncodingTranslator() {}
  
  /* Error */
  private static void load7BitTranslationTableFromXml()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: invokestatic 63	android/content/res/Resources:getSystem	()Landroid/content/res/Resources;
    //   5: astore_1
    //   6: iconst_0
    //   7: ifne +24 -> 31
    //   10: getstatic 40	com/android/internal/telephony/Sms7BitEncodingTranslator:DBG	Z
    //   13: ifeq +11 -> 24
    //   16: ldc 10
    //   18: ldc 65
    //   20: invokestatic 71	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   23: pop
    //   24: aload_1
    //   25: ldc 72
    //   27: invokevirtual 76	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   30: astore_0
    //   31: aload_0
    //   32: ldc 19
    //   34: invokestatic 82	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   37: aload_0
    //   38: invokestatic 86	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   41: aload_0
    //   42: invokeinterface 92 1 0
    //   47: astore_1
    //   48: getstatic 40	com/android/internal/telephony/Sms7BitEncodingTranslator:DBG	Z
    //   51: ifeq +34 -> 85
    //   54: new 94	java/lang/StringBuilder
    //   57: astore_2
    //   58: aload_2
    //   59: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   62: aload_2
    //   63: ldc 97
    //   65: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload_2
    //   70: aload_1
    //   71: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: ldc 10
    //   77: aload_2
    //   78: invokevirtual 104	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   81: invokestatic 71	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   84: pop
    //   85: ldc 25
    //   87: aload_1
    //   88: invokevirtual 110	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   91: ifeq +138 -> 229
    //   94: aload_0
    //   95: aconst_null
    //   96: ldc 112
    //   98: invokeinterface 116 3 0
    //   103: astore_1
    //   104: getstatic 40	com/android/internal/telephony/Sms7BitEncodingTranslator:DBG	Z
    //   107: ifeq +34 -> 141
    //   110: new 94	java/lang/StringBuilder
    //   113: astore_2
    //   114: aload_2
    //   115: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   118: aload_2
    //   119: ldc 118
    //   121: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload_2
    //   126: aload_1
    //   127: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   130: pop
    //   131: ldc 10
    //   133: aload_2
    //   134: invokevirtual 104	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   137: invokestatic 71	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   140: pop
    //   141: aload_1
    //   142: ldc 120
    //   144: invokevirtual 110	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   147: ifeq +12 -> 159
    //   150: getstatic 46	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTableCommon	Landroid/util/SparseIntArray;
    //   153: putstatic 44	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTable	Landroid/util/SparseIntArray;
    //   156: goto +70 -> 226
    //   159: aload_1
    //   160: ldc 122
    //   162: invokevirtual 110	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   165: ifeq +12 -> 177
    //   168: getstatic 48	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTableGSM	Landroid/util/SparseIntArray;
    //   171: putstatic 44	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTable	Landroid/util/SparseIntArray;
    //   174: goto +52 -> 226
    //   177: aload_1
    //   178: ldc 124
    //   180: invokevirtual 110	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   183: ifeq +12 -> 195
    //   186: getstatic 50	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTableCDMA	Landroid/util/SparseIntArray;
    //   189: putstatic 44	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTable	Landroid/util/SparseIntArray;
    //   192: goto +34 -> 226
    //   195: new 94	java/lang/StringBuilder
    //   198: astore_2
    //   199: aload_2
    //   200: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   203: aload_2
    //   204: ldc 126
    //   206: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload_2
    //   211: aload_1
    //   212: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: pop
    //   216: ldc 10
    //   218: aload_2
    //   219: invokevirtual 104	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   222: invokestatic 129	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   225: pop
    //   226: goto +135 -> 361
    //   229: ldc 13
    //   231: aload_1
    //   232: invokevirtual 110	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   235: ifeq +129 -> 364
    //   238: getstatic 44	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTable	Landroid/util/SparseIntArray;
    //   241: ifnull +123 -> 364
    //   244: aload_0
    //   245: aconst_null
    //   246: ldc 16
    //   248: iconst_m1
    //   249: invokeinterface 133 4 0
    //   254: istore_3
    //   255: aload_0
    //   256: aconst_null
    //   257: ldc 22
    //   259: iconst_m1
    //   260: invokeinterface 133 4 0
    //   265: istore 4
    //   267: iload_3
    //   268: iconst_m1
    //   269: if_icmpeq +84 -> 353
    //   272: iload 4
    //   274: iconst_m1
    //   275: if_icmpeq +78 -> 353
    //   278: getstatic 40	com/android/internal/telephony/Sms7BitEncodingTranslator:DBG	Z
    //   281: ifeq +60 -> 341
    //   284: new 94	java/lang/StringBuilder
    //   287: astore_1
    //   288: aload_1
    //   289: invokespecial 95	java/lang/StringBuilder:<init>	()V
    //   292: aload_1
    //   293: ldc -121
    //   295: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   298: pop
    //   299: aload_1
    //   300: iload_3
    //   301: invokestatic 141	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   304: invokevirtual 144	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   307: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: pop
    //   311: aload_1
    //   312: ldc -110
    //   314: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   317: pop
    //   318: aload_1
    //   319: iload 4
    //   321: invokestatic 141	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   324: invokevirtual 144	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   327: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   330: pop
    //   331: ldc 10
    //   333: aload_1
    //   334: invokevirtual 104	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   337: invokestatic 71	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   340: pop
    //   341: getstatic 44	com/android/internal/telephony/Sms7BitEncodingTranslator:mTranslationTable	Landroid/util/SparseIntArray;
    //   344: iload_3
    //   345: iload 4
    //   347: invokevirtual 152	android/util/SparseIntArray:put	(II)V
    //   350: goto +11 -> 361
    //   353: ldc 10
    //   355: ldc -102
    //   357: invokestatic 71	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   360: pop
    //   361: goto -324 -> 37
    //   364: getstatic 40	com/android/internal/telephony/Sms7BitEncodingTranslator:DBG	Z
    //   367: ifeq +11 -> 378
    //   370: ldc 10
    //   372: ldc -100
    //   374: invokestatic 71	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   377: pop
    //   378: aload_0
    //   379: instanceof 88
    //   382: ifeq +33 -> 415
    //   385: goto +24 -> 409
    //   388: astore_1
    //   389: goto +27 -> 416
    //   392: astore_1
    //   393: ldc 10
    //   395: ldc -98
    //   397: aload_1
    //   398: invokestatic 161	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   401: pop
    //   402: aload_0
    //   403: instanceof 88
    //   406: ifeq +9 -> 415
    //   409: aload_0
    //   410: invokeinterface 164 1 0
    //   415: return
    //   416: aload_0
    //   417: instanceof 88
    //   420: ifeq +9 -> 429
    //   423: aload_0
    //   424: invokeinterface 164 1 0
    //   429: aload_1
    //   430: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   1	423	0	localXmlResourceParser	android.content.res.XmlResourceParser
    //   5	329	1	localObject1	Object
    //   388	1	1	localObject2	Object
    //   392	38	1	localException	Exception
    //   57	162	2	localStringBuilder	StringBuilder
    //   254	91	3	i	int
    //   265	81	4	j	int
    // Exception table:
    //   from	to	target	type
    //   31	37	388	finally
    //   37	85	388	finally
    //   85	141	388	finally
    //   141	156	388	finally
    //   159	174	388	finally
    //   177	192	388	finally
    //   195	226	388	finally
    //   229	267	388	finally
    //   278	341	388	finally
    //   341	350	388	finally
    //   353	361	388	finally
    //   364	378	388	finally
    //   393	402	388	finally
    //   31	37	392	java/lang/Exception
    //   37	85	392	java/lang/Exception
    //   85	141	392	java/lang/Exception
    //   141	156	392	java/lang/Exception
    //   159	174	392	java/lang/Exception
    //   177	192	392	java/lang/Exception
    //   195	226	392	java/lang/Exception
    //   229	267	392	java/lang/Exception
    //   278	341	392	java/lang/Exception
    //   341	350	392	java/lang/Exception
    //   353	361	392	java/lang/Exception
    //   364	378	392	java/lang/Exception
  }
  
  private static boolean noTranslationNeeded(char paramChar, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if ((GsmAlphabet.isGsmSeptets(paramChar)) && (UserData.charToAscii.get(paramChar, -1) != -1)) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      return paramBoolean;
    }
    return GsmAlphabet.isGsmSeptets(paramChar);
  }
  
  public static String translate(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null)
    {
      Rlog.w("Sms7BitEncodingTranslator", "Null message can not be translated");
      return null;
    }
    int i = paramCharSequence.length();
    if (i <= 0) {
      return "";
    }
    if (!mIs7BitTranslationTableLoaded)
    {
      mTranslationTableCommon = new SparseIntArray();
      mTranslationTableGSM = new SparseIntArray();
      mTranslationTableCDMA = new SparseIntArray();
      load7BitTranslationTableFromXml();
      mIs7BitTranslationTableLoaded = true;
    }
    if (((mTranslationTableCommon != null) && (mTranslationTableCommon.size() > 0)) || ((mTranslationTableGSM != null) && (mTranslationTableGSM.size() > 0)) || ((mTranslationTableCDMA != null) && (mTranslationTableCDMA.size() > 0)))
    {
      char[] arrayOfChar = new char[i];
      boolean bool = useCdmaFormatForMoSms();
      for (int j = 0; j < i; j++) {
        arrayOfChar[j] = translateIfNeeded(paramCharSequence.charAt(j), bool);
      }
      return String.valueOf(arrayOfChar);
    }
    return null;
  }
  
  private static char translateIfNeeded(char paramChar, boolean paramBoolean)
  {
    StringBuilder localStringBuilder;
    if (noTranslationNeeded(paramChar, paramBoolean))
    {
      if (DBG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("No translation needed for ");
        localStringBuilder.append(Integer.toHexString(paramChar));
        Rlog.v("Sms7BitEncodingTranslator", localStringBuilder.toString());
      }
      return paramChar;
    }
    int i = -1;
    if (mTranslationTableCommon != null) {
      i = mTranslationTableCommon.get(paramChar, -1);
    }
    int j = i;
    if (i == -1) {
      if (paramBoolean)
      {
        j = i;
        if (mTranslationTableCDMA != null) {
          j = mTranslationTableCDMA.get(paramChar, -1);
        }
      }
      else
      {
        j = i;
        if (mTranslationTableGSM != null) {
          j = mTranslationTableGSM.get(paramChar, -1);
        }
      }
    }
    if (j != -1)
    {
      if (DBG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(Integer.toHexString(paramChar));
        localStringBuilder.append(" (");
        localStringBuilder.append(paramChar);
        localStringBuilder.append(") translated to ");
        localStringBuilder.append(Integer.toHexString(j));
        localStringBuilder.append(" (");
        localStringBuilder.append((char)j);
        localStringBuilder.append(")");
        Rlog.v("Sms7BitEncodingTranslator", localStringBuilder.toString());
      }
      return (char)j;
    }
    if (DBG)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("No translation found for ");
      localStringBuilder.append(Integer.toHexString(paramChar));
      localStringBuilder.append("! Replacing for empty space");
      Rlog.w("Sms7BitEncodingTranslator", localStringBuilder.toString());
    }
    return ' ';
  }
  
  private static boolean useCdmaFormatForMoSms()
  {
    if (!SmsManager.getDefault().isImsSmsSupported())
    {
      boolean bool;
      if (TelephonyManager.getDefault().getCurrentPhoneType() == 2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    return "3gpp2".equals(SmsManager.getDefault().getImsSmsFormat());
  }
}
