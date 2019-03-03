package android.app;

import android.content.Intent;
import android.util.AttributeSet;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AliasActivity
  extends Activity
{
  public final String ALIAS_META_DATA = "android.app.alias";
  
  public AliasActivity() {}
  
  private Intent parseAlias(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    AttributeSet localAttributeSet = Xml.asAttributeSet(paramXmlPullParser);
    Object localObject1 = null;
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 1) && (i != 2));
    Object localObject2 = paramXmlPullParser.getName();
    if ("alias".equals(localObject2))
    {
      i = paramXmlPullParser.getDepth();
      for (;;)
      {
        int j = paramXmlPullParser.next();
        if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
          break;
        }
        if ((j != 3) && (j != 4)) {
          if ("intent".equals(paramXmlPullParser.getName()))
          {
            Intent localIntent = Intent.parseIntent(getResources(), paramXmlPullParser, localAttributeSet);
            localObject2 = localObject1;
            if (localObject1 == null) {
              localObject2 = localIntent;
            }
            localObject1 = localObject2;
          }
          else
          {
            XmlUtils.skipCurrentTag(paramXmlPullParser);
          }
        }
      }
      return localObject1;
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Alias meta-data must start with <alias> tag; found");
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(" at ");
    ((StringBuilder)localObject1).append(paramXmlPullParser.getPositionDescription());
    throw new RuntimeException(((StringBuilder)localObject1).toString());
  }
  
  /* Error */
  protected void onCreate(android.os.Bundle paramBundle)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 95	android/app/Activity:onCreate	(Landroid/os/Bundle;)V
    //   5: aconst_null
    //   6: astore_2
    //   7: aconst_null
    //   8: astore_3
    //   9: aconst_null
    //   10: astore 4
    //   12: aconst_null
    //   13: astore_1
    //   14: aload_0
    //   15: invokevirtual 99	android/app/AliasActivity:getPackageManager	()Landroid/content/pm/PackageManager;
    //   18: aload_0
    //   19: invokevirtual 103	android/app/AliasActivity:getComponentName	()Landroid/content/ComponentName;
    //   22: sipush 128
    //   25: invokevirtual 109	android/content/pm/PackageManager:getActivityInfo	(Landroid/content/ComponentName;I)Landroid/content/pm/ActivityInfo;
    //   28: aload_0
    //   29: invokevirtual 99	android/app/AliasActivity:getPackageManager	()Landroid/content/pm/PackageManager;
    //   32: ldc 12
    //   34: invokevirtual 115	android/content/pm/ActivityInfo:loadXmlMetaData	(Landroid/content/pm/PackageManager;Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   37: astore 5
    //   39: aload 5
    //   41: ifnull +132 -> 173
    //   44: aload 5
    //   46: astore_1
    //   47: aload 5
    //   49: astore_2
    //   50: aload 5
    //   52: astore_3
    //   53: aload 5
    //   55: astore 4
    //   57: aload_0
    //   58: aload 5
    //   60: invokespecial 117	android/app/AliasActivity:parseAlias	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/content/Intent;
    //   63: astore 6
    //   65: aload 6
    //   67: ifnull +52 -> 119
    //   70: aload 5
    //   72: astore_1
    //   73: aload 5
    //   75: astore_2
    //   76: aload 5
    //   78: astore_3
    //   79: aload 5
    //   81: astore 4
    //   83: aload_0
    //   84: aload 6
    //   86: invokevirtual 121	android/app/AliasActivity:startActivity	(Landroid/content/Intent;)V
    //   89: aload 5
    //   91: astore_1
    //   92: aload 5
    //   94: astore_2
    //   95: aload 5
    //   97: astore_3
    //   98: aload 5
    //   100: astore 4
    //   102: aload_0
    //   103: invokevirtual 124	android/app/AliasActivity:finish	()V
    //   106: aload 5
    //   108: ifnull +10 -> 118
    //   111: aload 5
    //   113: invokeinterface 129 1 0
    //   118: return
    //   119: aload 5
    //   121: astore_1
    //   122: aload 5
    //   124: astore_2
    //   125: aload 5
    //   127: astore_3
    //   128: aload 5
    //   130: astore 4
    //   132: new 82	java/lang/RuntimeException
    //   135: astore 6
    //   137: aload 5
    //   139: astore_1
    //   140: aload 5
    //   142: astore_2
    //   143: aload 5
    //   145: astore_3
    //   146: aload 5
    //   148: astore 4
    //   150: aload 6
    //   152: ldc -125
    //   154: invokespecial 88	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   157: aload 5
    //   159: astore_1
    //   160: aload 5
    //   162: astore_2
    //   163: aload 5
    //   165: astore_3
    //   166: aload 5
    //   168: astore 4
    //   170: aload 6
    //   172: athrow
    //   173: aload 5
    //   175: astore_1
    //   176: aload 5
    //   178: astore_2
    //   179: aload 5
    //   181: astore_3
    //   182: aload 5
    //   184: astore 4
    //   186: new 82	java/lang/RuntimeException
    //   189: astore 6
    //   191: aload 5
    //   193: astore_1
    //   194: aload 5
    //   196: astore_2
    //   197: aload 5
    //   199: astore_3
    //   200: aload 5
    //   202: astore 4
    //   204: aload 6
    //   206: ldc -123
    //   208: invokespecial 88	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   211: aload 5
    //   213: astore_1
    //   214: aload 5
    //   216: astore_2
    //   217: aload 5
    //   219: astore_3
    //   220: aload 5
    //   222: astore 4
    //   224: aload 6
    //   226: athrow
    //   227: astore 5
    //   229: goto +73 -> 302
    //   232: astore 5
    //   234: aload_2
    //   235: astore_1
    //   236: new 82	java/lang/RuntimeException
    //   239: astore_3
    //   240: aload_2
    //   241: astore_1
    //   242: aload_3
    //   243: ldc -121
    //   245: aload 5
    //   247: invokespecial 138	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   250: aload_2
    //   251: astore_1
    //   252: aload_3
    //   253: athrow
    //   254: astore 5
    //   256: aload_3
    //   257: astore_1
    //   258: new 82	java/lang/RuntimeException
    //   261: astore_2
    //   262: aload_3
    //   263: astore_1
    //   264: aload_2
    //   265: ldc -121
    //   267: aload 5
    //   269: invokespecial 138	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   272: aload_3
    //   273: astore_1
    //   274: aload_2
    //   275: athrow
    //   276: astore_2
    //   277: aload 4
    //   279: astore_1
    //   280: new 82	java/lang/RuntimeException
    //   283: astore 5
    //   285: aload 4
    //   287: astore_1
    //   288: aload 5
    //   290: ldc -121
    //   292: aload_2
    //   293: invokespecial 138	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   296: aload 4
    //   298: astore_1
    //   299: aload 5
    //   301: athrow
    //   302: aload_1
    //   303: ifnull +9 -> 312
    //   306: aload_1
    //   307: invokeinterface 129 1 0
    //   312: aload 5
    //   314: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	315	0	this	AliasActivity
    //   0	315	1	paramBundle	android.os.Bundle
    //   6	269	2	localObject1	Object
    //   276	17	2	localNameNotFoundException	android.content.pm.PackageManager.NameNotFoundException
    //   8	265	3	localObject2	Object
    //   10	287	4	localObject3	Object
    //   37	184	5	localXmlResourceParser	android.content.res.XmlResourceParser
    //   227	1	5	localObject4	Object
    //   232	14	5	localIOException	IOException
    //   254	14	5	localXmlPullParserException	XmlPullParserException
    //   283	30	5	localRuntimeException	RuntimeException
    //   63	162	6	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   14	39	227	finally
    //   57	65	227	finally
    //   83	89	227	finally
    //   102	106	227	finally
    //   132	137	227	finally
    //   150	157	227	finally
    //   170	173	227	finally
    //   186	191	227	finally
    //   204	211	227	finally
    //   224	227	227	finally
    //   236	240	227	finally
    //   242	250	227	finally
    //   252	254	227	finally
    //   258	262	227	finally
    //   264	272	227	finally
    //   274	276	227	finally
    //   280	285	227	finally
    //   288	296	227	finally
    //   299	302	227	finally
    //   14	39	232	java/io/IOException
    //   57	65	232	java/io/IOException
    //   83	89	232	java/io/IOException
    //   102	106	232	java/io/IOException
    //   132	137	232	java/io/IOException
    //   150	157	232	java/io/IOException
    //   170	173	232	java/io/IOException
    //   186	191	232	java/io/IOException
    //   204	211	232	java/io/IOException
    //   224	227	232	java/io/IOException
    //   14	39	254	org/xmlpull/v1/XmlPullParserException
    //   57	65	254	org/xmlpull/v1/XmlPullParserException
    //   83	89	254	org/xmlpull/v1/XmlPullParserException
    //   102	106	254	org/xmlpull/v1/XmlPullParserException
    //   132	137	254	org/xmlpull/v1/XmlPullParserException
    //   150	157	254	org/xmlpull/v1/XmlPullParserException
    //   170	173	254	org/xmlpull/v1/XmlPullParserException
    //   186	191	254	org/xmlpull/v1/XmlPullParserException
    //   204	211	254	org/xmlpull/v1/XmlPullParserException
    //   224	227	254	org/xmlpull/v1/XmlPullParserException
    //   14	39	276	android/content/pm/PackageManager$NameNotFoundException
    //   57	65	276	android/content/pm/PackageManager$NameNotFoundException
    //   83	89	276	android/content/pm/PackageManager$NameNotFoundException
    //   102	106	276	android/content/pm/PackageManager$NameNotFoundException
    //   132	137	276	android/content/pm/PackageManager$NameNotFoundException
    //   150	157	276	android/content/pm/PackageManager$NameNotFoundException
    //   170	173	276	android/content/pm/PackageManager$NameNotFoundException
    //   186	191	276	android/content/pm/PackageManager$NameNotFoundException
    //   204	211	276	android/content/pm/PackageManager$NameNotFoundException
    //   224	227	276	android/content/pm/PackageManager$NameNotFoundException
  }
}
