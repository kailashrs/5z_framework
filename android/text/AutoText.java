package android.text;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import java.util.Locale;

public class AutoText
{
  private static final int DEFAULT = 14337;
  private static final int INCREMENT = 1024;
  private static final int RIGHT = 9300;
  private static final int TRIE_C = 0;
  private static final int TRIE_CHILD = 2;
  private static final int TRIE_NEXT = 3;
  private static final char TRIE_NULL = '￿';
  private static final int TRIE_OFF = 1;
  private static final int TRIE_ROOT = 0;
  private static final int TRIE_SIZEOF = 4;
  private static AutoText sInstance = new AutoText(Resources.getSystem());
  private static Object sLock = new Object();
  private Locale mLocale;
  private int mSize;
  private String mText;
  private char[] mTrie;
  private char mTrieUsed;
  
  private AutoText(Resources paramResources)
  {
    mLocale = getConfigurationlocale;
    init(paramResources);
  }
  
  private void add(String paramString, char paramChar)
  {
    int i = paramString.length();
    mSize += 1;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      int m = paramString.charAt(k);
      int n = 0;
      int i2;
      for (int i1 = j;; i1 = mTrie[i1] + '\003')
      {
        i2 = n;
        j = i1;
        if (mTrie[i1] == 65535) {
          break;
        }
        if (m == mTrie[(mTrie[i1] + '\000')])
        {
          if (k == i - 1)
          {
            mTrie[(mTrie[i1] + '\001')] = ((char)paramChar);
            return;
          }
          j = mTrie[i1] + '\002';
          i2 = 1;
          break;
        }
      }
      if (i2 == 0)
      {
        i1 = newTrieNode();
        mTrie[j] = ((char)i1);
        mTrie[(mTrie[j] + '\000')] = ((char)m);
        mTrie[(mTrie[j] + '\001')] = ((char)65535);
        mTrie[(mTrie[j] + '\003')] = ((char)65535);
        mTrie[(mTrie[j] + '\002')] = ((char)65535);
        if (k == i - 1)
        {
          mTrie[(mTrie[j] + '\001')] = ((char)paramChar);
          return;
        }
        j = mTrie[j] + '\002';
      }
    }
  }
  
  public static String get(CharSequence paramCharSequence, int paramInt1, int paramInt2, View paramView)
  {
    return getInstance(paramView).lookup(paramCharSequence, paramInt1, paramInt2);
  }
  
  private static AutoText getInstance(View paramView)
  {
    Resources localResources = paramView.getContext().getResources();
    Locale localLocale = getConfigurationlocale;
    synchronized (sLock)
    {
      AutoText localAutoText = sInstance;
      paramView = localAutoText;
      if (!localLocale.equals(mLocale))
      {
        paramView = new android/text/AutoText;
        paramView.<init>(localResources);
        sInstance = paramView;
      }
      return paramView;
    }
  }
  
  private int getSize()
  {
    return mSize;
  }
  
  public static int getSize(View paramView)
  {
    return getInstance(paramView).getSize();
  }
  
  /* Error */
  private void init(Resources paramResources)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 126
    //   3: invokevirtual 130	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   6: astore_2
    //   7: new 132	java/lang/StringBuilder
    //   10: dup
    //   11: sipush 9300
    //   14: invokespecial 135	java/lang/StringBuilder:<init>	(I)V
    //   17: astore_3
    //   18: aload_0
    //   19: sipush 14337
    //   22: newarray char
    //   24: putfield 86	android/text/AutoText:mTrie	[C
    //   27: aload_0
    //   28: getfield 86	android/text/AutoText:mTrie	[C
    //   31: iconst_0
    //   32: ldc 20
    //   34: i2c
    //   35: castore
    //   36: aload_0
    //   37: iconst_1
    //   38: i2c
    //   39: putfield 137	android/text/AutoText:mTrieUsed	C
    //   42: aload_2
    //   43: ldc -117
    //   45: invokestatic 145	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   48: aload_2
    //   49: invokestatic 149	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   52: aload_2
    //   53: invokeinterface 155 1 0
    //   58: astore 4
    //   60: aload 4
    //   62: ifnull +105 -> 167
    //   65: aload 4
    //   67: ldc -99
    //   69: invokevirtual 158	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   72: ifne +6 -> 78
    //   75: goto +92 -> 167
    //   78: aload_2
    //   79: aconst_null
    //   80: ldc -96
    //   82: invokeinterface 164 3 0
    //   87: astore 5
    //   89: aload_2
    //   90: invokeinterface 167 1 0
    //   95: iconst_4
    //   96: if_icmpne +68 -> 164
    //   99: aload_2
    //   100: invokeinterface 170 1 0
    //   105: astore 4
    //   107: aload 4
    //   109: ldc -84
    //   111: invokevirtual 158	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   114: ifeq +13 -> 127
    //   117: iconst_0
    //   118: istore 6
    //   120: iload 6
    //   122: istore 7
    //   124: goto +32 -> 156
    //   127: aload_3
    //   128: invokevirtual 173	java/lang/StringBuilder:length	()I
    //   131: i2c
    //   132: istore 6
    //   134: aload_3
    //   135: aload 4
    //   137: invokevirtual 78	java/lang/String:length	()I
    //   140: i2c
    //   141: invokevirtual 177	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   144: pop
    //   145: aload_3
    //   146: aload 4
    //   148: invokevirtual 180	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: iload 6
    //   154: istore 7
    //   156: aload_0
    //   157: aload 5
    //   159: iload 7
    //   161: invokespecial 182	android/text/AutoText:add	(Ljava/lang/String;C)V
    //   164: goto -116 -> 48
    //   167: aload_1
    //   168: invokevirtual 185	android/content/res/Resources:flushLayoutCache	()V
    //   171: aload_2
    //   172: invokeinterface 188 1 0
    //   177: aload_0
    //   178: aload_3
    //   179: invokevirtual 191	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   182: putfield 193	android/text/AutoText:mText	Ljava/lang/String;
    //   185: return
    //   186: astore_1
    //   187: goto +27 -> 214
    //   190: astore_3
    //   191: new 195	java/lang/RuntimeException
    //   194: astore_1
    //   195: aload_1
    //   196: aload_3
    //   197: invokespecial 198	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   200: aload_1
    //   201: athrow
    //   202: astore_3
    //   203: new 195	java/lang/RuntimeException
    //   206: astore_1
    //   207: aload_1
    //   208: aload_3
    //   209: invokespecial 198	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   212: aload_1
    //   213: athrow
    //   214: aload_2
    //   215: invokeinterface 188 1 0
    //   220: aload_1
    //   221: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	222	0	this	AutoText
    //   0	222	1	paramResources	Resources
    //   6	209	2	localXmlResourceParser	android.content.res.XmlResourceParser
    //   17	162	3	localStringBuilder	StringBuilder
    //   190	7	3	localIOException	java.io.IOException
    //   202	7	3	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   58	89	4	str1	String
    //   87	71	5	str2	String
    //   118	35	6	c1	char
    //   122	38	7	c2	char
    // Exception table:
    //   from	to	target	type
    //   42	48	186	finally
    //   48	60	186	finally
    //   65	75	186	finally
    //   78	117	186	finally
    //   127	152	186	finally
    //   156	164	186	finally
    //   167	171	186	finally
    //   191	202	186	finally
    //   203	214	186	finally
    //   42	48	190	java/io/IOException
    //   48	60	190	java/io/IOException
    //   65	75	190	java/io/IOException
    //   78	117	190	java/io/IOException
    //   127	152	190	java/io/IOException
    //   156	164	190	java/io/IOException
    //   167	171	190	java/io/IOException
    //   42	48	202	org/xmlpull/v1/XmlPullParserException
    //   48	60	202	org/xmlpull/v1/XmlPullParserException
    //   65	75	202	org/xmlpull/v1/XmlPullParserException
    //   78	117	202	org/xmlpull/v1/XmlPullParserException
    //   127	152	202	org/xmlpull/v1/XmlPullParserException
    //   156	164	202	org/xmlpull/v1/XmlPullParserException
    //   167	171	202	org/xmlpull/v1/XmlPullParserException
  }
  
  private String lookup(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    int i = mTrie[0];
    int j = paramInt1;
    paramInt1 = i;
    while (j < paramInt2)
    {
      int k = paramCharSequence.charAt(j);
      for (i = paramInt1;; i = mTrie[(i + 3)])
      {
        paramInt1 = i;
        if (i == 65535) {
          break;
        }
        if (k == mTrie[(i + 0)])
        {
          if ((j == paramInt2 - 1) && (mTrie[(i + 1)] != 65535))
          {
            paramInt2 = mTrie[(i + 1)];
            paramInt1 = mText.charAt(paramInt2);
            return mText.substring(paramInt2 + 1, paramInt2 + 1 + paramInt1);
          }
          paramInt1 = mTrie[(i + 2)];
          break;
        }
      }
      if (paramInt1 == 65535) {
        return null;
      }
      j++;
    }
    return null;
  }
  
  private char newTrieNode()
  {
    if (mTrieUsed + '\004' > mTrie.length)
    {
      char[] arrayOfChar = new char[mTrie.length + 1024];
      System.arraycopy(mTrie, 0, arrayOfChar, 0, mTrie.length);
      mTrie = arrayOfChar;
    }
    char c = mTrieUsed;
    mTrieUsed = ((char)(char)(mTrieUsed + '\004'));
    return c;
  }
}
