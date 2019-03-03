package com.android.internal.telephony.test;

import android.os.Bundle;
import android.util.Log;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TestConferenceEventPackageParser
{
  private static final String LOG_TAG = "TestConferenceEventPackageParser";
  private static final String PARTICIPANT_TAG = "participant";
  private InputStream mInputStream;
  
  public TestConferenceEventPackageParser(InputStream paramInputStream)
  {
    mInputStream = paramInputStream;
  }
  
  private Bundle parseParticipant(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    Bundle localBundle = new Bundle();
    String str1 = "";
    String str2 = "";
    String str3 = "";
    String str4 = "";
    int i = paramXmlPullParser.getDepth();
    while (XmlUtils.nextElementWithin(paramXmlPullParser, i)) {
      if (paramXmlPullParser.getName().equals("user"))
      {
        paramXmlPullParser.next();
        str1 = paramXmlPullParser.getText();
      }
      else if (paramXmlPullParser.getName().equals("display-text"))
      {
        paramXmlPullParser.next();
        str2 = paramXmlPullParser.getText();
      }
      else if (paramXmlPullParser.getName().equals("endpoint"))
      {
        paramXmlPullParser.next();
        str3 = paramXmlPullParser.getText();
      }
      else if (paramXmlPullParser.getName().equals("status"))
      {
        paramXmlPullParser.next();
        str4 = paramXmlPullParser.getText();
      }
    }
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("User: ");
    paramXmlPullParser.append(str1);
    Log.v("TestConferenceEventPackageParser", paramXmlPullParser.toString());
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("DisplayText: ");
    paramXmlPullParser.append(str2);
    Log.v("TestConferenceEventPackageParser", paramXmlPullParser.toString());
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Endpoint: ");
    paramXmlPullParser.append(str3);
    Log.v("TestConferenceEventPackageParser", paramXmlPullParser.toString());
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Status: ");
    paramXmlPullParser.append(str4);
    Log.v("TestConferenceEventPackageParser", paramXmlPullParser.toString());
    localBundle.putString("user", str1);
    localBundle.putString("display-text", str2);
    localBundle.putString("endpoint", str3);
    localBundle.putString("status", str4);
    return localBundle;
  }
  
  /* Error */
  public android.telephony.ims.ImsConferenceState parse()
  {
    // Byte code:
    //   0: new 101	android/telephony/ims/ImsConferenceState
    //   3: dup
    //   4: invokespecial 102	android/telephony/ims/ImsConferenceState:<init>	()V
    //   7: astore_1
    //   8: invokestatic 108	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   11: astore_2
    //   12: aload_2
    //   13: aload_0
    //   14: getfield 20	com/android/internal/telephony/test/TestConferenceEventPackageParser:mInputStream	Ljava/io/InputStream;
    //   17: aconst_null
    //   18: invokeinterface 112 3 0
    //   23: aload_2
    //   24: invokeinterface 115 1 0
    //   29: pop
    //   30: aload_2
    //   31: invokeinterface 38 1 0
    //   36: istore_3
    //   37: aload_2
    //   38: iload_3
    //   39: invokestatic 44	com/android/internal/util/XmlUtils:nextElementWithin	(Lorg/xmlpull/v1/XmlPullParser;I)Z
    //   42: ifeq +52 -> 94
    //   45: aload_2
    //   46: invokeinterface 48 1 0
    //   51: ldc 11
    //   53: invokevirtual 56	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   56: ifeq -19 -> 37
    //   59: ldc 8
    //   61: ldc 117
    //   63: invokestatic 86	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   66: pop
    //   67: aload_0
    //   68: aload_2
    //   69: invokespecial 119	com/android/internal/telephony/test/TestConferenceEventPackageParser:parseParticipant	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/os/Bundle;
    //   72: astore 4
    //   74: aload_1
    //   75: getfield 123	android/telephony/ims/ImsConferenceState:mParticipants	Ljava/util/HashMap;
    //   78: aload 4
    //   80: ldc 66
    //   82: invokevirtual 127	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   85: aload 4
    //   87: invokevirtual 133	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   90: pop
    //   91: goto -54 -> 37
    //   94: aload_0
    //   95: getfield 20	com/android/internal/telephony/test/TestConferenceEventPackageParser:mInputStream	Ljava/io/InputStream;
    //   98: invokevirtual 138	java/io/InputStream:close	()V
    //   101: aload_1
    //   102: areturn
    //   103: astore 4
    //   105: ldc 8
    //   107: ldc -116
    //   109: aload 4
    //   111: invokestatic 144	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   114: pop
    //   115: aconst_null
    //   116: areturn
    //   117: astore 4
    //   119: goto +24 -> 143
    //   122: astore 4
    //   124: ldc 8
    //   126: ldc -110
    //   128: aload 4
    //   130: invokestatic 144	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   133: pop
    //   134: aload_0
    //   135: getfield 20	com/android/internal/telephony/test/TestConferenceEventPackageParser:mInputStream	Ljava/io/InputStream;
    //   138: invokevirtual 138	java/io/InputStream:close	()V
    //   141: aconst_null
    //   142: areturn
    //   143: aload_0
    //   144: getfield 20	com/android/internal/telephony/test/TestConferenceEventPackageParser:mInputStream	Ljava/io/InputStream;
    //   147: invokevirtual 138	java/io/InputStream:close	()V
    //   150: aload 4
    //   152: athrow
    //   153: astore 4
    //   155: ldc 8
    //   157: ldc -116
    //   159: aload 4
    //   161: invokestatic 144	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   164: pop
    //   165: goto -50 -> 115
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	168	0	this	TestConferenceEventPackageParser
    //   7	95	1	localImsConferenceState	android.telephony.ims.ImsConferenceState
    //   11	58	2	localXmlPullParser	XmlPullParser
    //   36	3	3	i	int
    //   72	14	4	localBundle	Bundle
    //   103	7	4	localIOException1	IOException
    //   117	1	4	localObject	Object
    //   122	29	4	localIOException2	IOException
    //   153	7	4	localIOException3	IOException
    // Exception table:
    //   from	to	target	type
    //   94	101	103	java/io/IOException
    //   134	141	103	java/io/IOException
    //   8	37	117	finally
    //   37	91	117	finally
    //   124	134	117	finally
    //   8	37	122	java/io/IOException
    //   8	37	122	org/xmlpull/v1/XmlPullParserException
    //   37	91	122	java/io/IOException
    //   37	91	122	org/xmlpull/v1/XmlPullParserException
    //   143	150	153	java/io/IOException
  }
}
