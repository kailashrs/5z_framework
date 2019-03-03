package com.android.internal.telephony.uicc;

import java.util.HashMap;

class VoiceMailConstants
{
  static final String LOG_TAG = "VoiceMailConstants";
  static final int NAME = 0;
  static final int NUMBER = 1;
  static final String PARTNER_VOICEMAIL_PATH = "etc/voicemail-conf.xml";
  static final int SIZE = 3;
  static final int TAG = 2;
  private HashMap<String, String[]> CarrierVmMap = new HashMap();
  
  VoiceMailConstants()
  {
    loadVoiceMail();
  }
  
  /* Error */
  private void loadVoiceMail()
  {
    // Byte code:
    //   0: new 44	java/io/File
    //   3: dup
    //   4: invokestatic 50	android/os/Environment:getRootDirectory	()Ljava/io/File;
    //   7: ldc 16
    //   9: invokespecial 53	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   12: astore_1
    //   13: new 55	java/io/FileReader
    //   16: dup
    //   17: aload_1
    //   18: invokespecial 58	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   21: astore_1
    //   22: invokestatic 64	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   25: astore_2
    //   26: aload_2
    //   27: aload_1
    //   28: invokeinterface 70 2 0
    //   33: aload_2
    //   34: ldc 72
    //   36: invokestatic 78	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   39: aload_2
    //   40: invokestatic 82	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   43: ldc 72
    //   45: aload_2
    //   46: invokeinterface 86 1 0
    //   51: invokevirtual 92	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   54: istore_3
    //   55: iload_3
    //   56: ifne +17 -> 73
    //   59: aload_1
    //   60: invokevirtual 95	java/io/FileReader:close	()V
    //   63: goto +7 -> 70
    //   66: astore_1
    //   67: goto +179 -> 246
    //   70: goto +176 -> 246
    //   73: aload_2
    //   74: aconst_null
    //   75: ldc 97
    //   77: invokeinterface 101 3 0
    //   82: astore 4
    //   84: aload_2
    //   85: aconst_null
    //   86: ldc 103
    //   88: invokeinterface 101 3 0
    //   93: astore 5
    //   95: aload_2
    //   96: aconst_null
    //   97: ldc 105
    //   99: invokeinterface 101 3 0
    //   104: astore 6
    //   106: aload_2
    //   107: aconst_null
    //   108: ldc 107
    //   110: invokeinterface 101 3 0
    //   115: astore 7
    //   117: aload_0
    //   118: getfield 32	com/android/internal/telephony/uicc/VoiceMailConstants:CarrierVmMap	Ljava/util/HashMap;
    //   121: aload 4
    //   123: iconst_3
    //   124: anewarray 88	java/lang/String
    //   127: dup
    //   128: iconst_0
    //   129: aload 5
    //   131: aastore
    //   132: dup
    //   133: iconst_1
    //   134: aload 6
    //   136: aastore
    //   137: dup
    //   138: iconst_2
    //   139: aload 7
    //   141: aastore
    //   142: invokevirtual 111	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   145: pop
    //   146: goto -107 -> 39
    //   149: astore 6
    //   151: goto +96 -> 247
    //   154: astore 5
    //   156: new 113	java/lang/StringBuilder
    //   159: astore 6
    //   161: aload 6
    //   163: invokespecial 114	java/lang/StringBuilder:<init>	()V
    //   166: aload 6
    //   168: ldc 116
    //   170: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: pop
    //   174: aload 6
    //   176: aload 5
    //   178: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   181: pop
    //   182: ldc 8
    //   184: aload 6
    //   186: invokevirtual 126	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   189: invokestatic 132	android/telephony/Rlog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   192: pop
    //   193: aload_1
    //   194: invokevirtual 95	java/io/FileReader:close	()V
    //   197: goto -127 -> 70
    //   200: astore 6
    //   202: new 113	java/lang/StringBuilder
    //   205: astore 5
    //   207: aload 5
    //   209: invokespecial 114	java/lang/StringBuilder:<init>	()V
    //   212: aload 5
    //   214: ldc 116
    //   216: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   219: pop
    //   220: aload 5
    //   222: aload 6
    //   224: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   227: pop
    //   228: ldc 8
    //   230: aload 5
    //   232: invokevirtual 126	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   235: invokestatic 132	android/telephony/Rlog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   238: pop
    //   239: aload_1
    //   240: invokevirtual 95	java/io/FileReader:close	()V
    //   243: goto -173 -> 70
    //   246: return
    //   247: aload_1
    //   248: invokevirtual 95	java/io/FileReader:close	()V
    //   251: goto +4 -> 255
    //   254: astore_1
    //   255: aload 6
    //   257: athrow
    //   258: astore_1
    //   259: new 113	java/lang/StringBuilder
    //   262: dup
    //   263: invokespecial 114	java/lang/StringBuilder:<init>	()V
    //   266: astore_1
    //   267: aload_1
    //   268: ldc -122
    //   270: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: pop
    //   274: aload_1
    //   275: invokestatic 50	android/os/Environment:getRootDirectory	()Ljava/io/File;
    //   278: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload_1
    //   283: ldc -120
    //   285: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   288: pop
    //   289: aload_1
    //   290: ldc 16
    //   292: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   295: pop
    //   296: ldc 8
    //   298: aload_1
    //   299: invokevirtual 126	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   302: invokestatic 132	android/telephony/Rlog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   305: pop
    //   306: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	307	0	this	VoiceMailConstants
    //   12	48	1	localObject1	Object
    //   66	182	1	localIOException1	java.io.IOException
    //   254	1	1	localIOException2	java.io.IOException
    //   258	1	1	localFileNotFoundException	java.io.FileNotFoundException
    //   266	33	1	localStringBuilder1	StringBuilder
    //   25	82	2	localXmlPullParser	org.xmlpull.v1.XmlPullParser
    //   54	2	3	bool	boolean
    //   82	40	4	str1	String
    //   93	37	5	str2	String
    //   154	23	5	localIOException3	java.io.IOException
    //   205	26	5	localStringBuilder2	StringBuilder
    //   104	31	6	str3	String
    //   149	1	6	localObject2	Object
    //   159	26	6	localStringBuilder3	StringBuilder
    //   200	56	6	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   115	25	7	str4	String
    // Exception table:
    //   from	to	target	type
    //   59	63	66	java/io/IOException
    //   193	197	66	java/io/IOException
    //   239	243	66	java/io/IOException
    //   22	39	149	finally
    //   39	55	149	finally
    //   73	146	149	finally
    //   156	193	149	finally
    //   202	239	149	finally
    //   22	39	154	java/io/IOException
    //   39	55	154	java/io/IOException
    //   73	146	154	java/io/IOException
    //   22	39	200	org/xmlpull/v1/XmlPullParserException
    //   39	55	200	org/xmlpull/v1/XmlPullParserException
    //   73	146	200	org/xmlpull/v1/XmlPullParserException
    //   247	251	254	java/io/IOException
    //   13	22	258	java/io/FileNotFoundException
  }
  
  boolean containsCarrier(String paramString)
  {
    return CarrierVmMap.containsKey(paramString);
  }
  
  String getCarrierName(String paramString)
  {
    return ((String[])CarrierVmMap.get(paramString))[0];
  }
  
  String getVoiceMailNumber(String paramString)
  {
    return ((String[])CarrierVmMap.get(paramString))[1];
  }
  
  String getVoiceMailTag(String paramString)
  {
    return ((String[])CarrierVmMap.get(paramString))[2];
  }
}
