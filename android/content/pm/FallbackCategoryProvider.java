package android.content.pm;

import android.util.ArrayMap;

public class FallbackCategoryProvider
{
  private static final String TAG = "FallbackCategoryProvider";
  private static final ArrayMap<String, Integer> sFallbacks = new ArrayMap();
  
  public FallbackCategoryProvider() {}
  
  public static int getFallbackCategory(String paramString)
  {
    return ((Integer)sFallbacks.getOrDefault(paramString, Integer.valueOf(-1))).intValue();
  }
  
  /* Error */
  public static void loadFallbacks()
  {
    // Byte code:
    //   0: getstatic 20	android/content/pm/FallbackCategoryProvider:sFallbacks	Landroid/util/ArrayMap;
    //   3: invokevirtual 48	android/util/ArrayMap:clear	()V
    //   6: ldc 50
    //   8: iconst_0
    //   9: invokestatic 56	android/os/SystemProperties:getBoolean	(Ljava/lang/String;Z)Z
    //   12: ifeq +12 -> 24
    //   15: ldc 8
    //   17: ldc 58
    //   19: invokestatic 64	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   22: pop
    //   23: return
    //   24: new 66	android/content/res/AssetManager
    //   27: dup
    //   28: invokespecial 67	android/content/res/AssetManager:<init>	()V
    //   31: astore_0
    //   32: aload_0
    //   33: ldc 69
    //   35: invokevirtual 72	android/content/res/AssetManager:addAssetPath	(Ljava/lang/String;)I
    //   38: pop
    //   39: aconst_null
    //   40: astore_1
    //   41: new 74	android/content/res/Resources
    //   44: dup
    //   45: aload_0
    //   46: aconst_null
    //   47: aconst_null
    //   48: invokespecial 77	android/content/res/Resources:<init>	(Landroid/content/res/AssetManager;Landroid/util/DisplayMetrics;Landroid/content/res/Configuration;)V
    //   51: astore_2
    //   52: new 79	java/io/BufferedReader
    //   55: astore_3
    //   56: new 81	java/io/InputStreamReader
    //   59: astore_0
    //   60: aload_0
    //   61: aload_2
    //   62: ldc 82
    //   64: invokevirtual 86	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   67: invokespecial 89	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   70: aload_3
    //   71: aload_0
    //   72: invokespecial 92	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   75: aload_1
    //   76: astore_0
    //   77: aload_3
    //   78: invokevirtual 96	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   81: astore_2
    //   82: aload_2
    //   83: ifnull +59 -> 142
    //   86: aload_1
    //   87: astore_0
    //   88: aload_2
    //   89: iconst_0
    //   90: invokevirtual 102	java/lang/String:charAt	(I)C
    //   93: bipush 35
    //   95: if_icmpne +6 -> 101
    //   98: goto -23 -> 75
    //   101: aload_1
    //   102: astore_0
    //   103: aload_2
    //   104: ldc 104
    //   106: invokevirtual 108	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   109: astore_2
    //   110: aload_1
    //   111: astore_0
    //   112: aload_2
    //   113: arraylength
    //   114: iconst_2
    //   115: if_icmpne +24 -> 139
    //   118: aload_1
    //   119: astore_0
    //   120: getstatic 20	android/content/pm/FallbackCategoryProvider:sFallbacks	Landroid/util/ArrayMap;
    //   123: aload_2
    //   124: iconst_0
    //   125: aaload
    //   126: aload_2
    //   127: iconst_1
    //   128: aaload
    //   129: invokestatic 111	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   132: invokestatic 30	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   135: invokevirtual 114	android/util/ArrayMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   138: pop
    //   139: goto -64 -> 75
    //   142: aload_1
    //   143: astore_0
    //   144: new 116	java/lang/StringBuilder
    //   147: astore_2
    //   148: aload_1
    //   149: astore_0
    //   150: aload_2
    //   151: invokespecial 117	java/lang/StringBuilder:<init>	()V
    //   154: aload_1
    //   155: astore_0
    //   156: aload_2
    //   157: ldc 119
    //   159: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: aload_1
    //   164: astore_0
    //   165: aload_2
    //   166: getstatic 20	android/content/pm/FallbackCategoryProvider:sFallbacks	Landroid/util/ArrayMap;
    //   169: invokevirtual 126	android/util/ArrayMap:size	()I
    //   172: invokevirtual 129	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   175: pop
    //   176: aload_1
    //   177: astore_0
    //   178: aload_2
    //   179: ldc -125
    //   181: invokevirtual 123	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload_1
    //   186: astore_0
    //   187: ldc 8
    //   189: aload_2
    //   190: invokevirtual 134	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   193: invokestatic 64	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   196: pop
    //   197: aload_3
    //   198: invokevirtual 137	java/io/BufferedReader:close	()V
    //   201: goto +48 -> 249
    //   204: astore_1
    //   205: goto +8 -> 213
    //   208: astore_1
    //   209: aload_1
    //   210: astore_0
    //   211: aload_1
    //   212: athrow
    //   213: aload_0
    //   214: ifnull +19 -> 233
    //   217: aload_3
    //   218: invokevirtual 137	java/io/BufferedReader:close	()V
    //   221: goto +16 -> 237
    //   224: astore_3
    //   225: aload_0
    //   226: aload_3
    //   227: invokevirtual 141	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   230: goto +7 -> 237
    //   233: aload_3
    //   234: invokevirtual 137	java/io/BufferedReader:close	()V
    //   237: aload_1
    //   238: athrow
    //   239: astore_0
    //   240: ldc 8
    //   242: ldc -113
    //   244: aload_0
    //   245: invokestatic 147	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   248: pop
    //   249: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   31	195	0	localObject1	Object
    //   239	6	0	localIOException	java.io.IOException
    //   40	146	1	localObject2	Object
    //   204	1	1	localObject3	Object
    //   208	30	1	localThrowable1	Throwable
    //   51	139	2	localObject4	Object
    //   55	163	3	localBufferedReader	java.io.BufferedReader
    //   224	10	3	localThrowable2	Throwable
    // Exception table:
    //   from	to	target	type
    //   77	82	204	finally
    //   88	98	204	finally
    //   103	110	204	finally
    //   112	118	204	finally
    //   120	139	204	finally
    //   144	148	204	finally
    //   150	154	204	finally
    //   156	163	204	finally
    //   165	176	204	finally
    //   178	185	204	finally
    //   187	197	204	finally
    //   211	213	204	finally
    //   77	82	208	java/lang/Throwable
    //   88	98	208	java/lang/Throwable
    //   103	110	208	java/lang/Throwable
    //   112	118	208	java/lang/Throwable
    //   120	139	208	java/lang/Throwable
    //   144	148	208	java/lang/Throwable
    //   150	154	208	java/lang/Throwable
    //   156	163	208	java/lang/Throwable
    //   165	176	208	java/lang/Throwable
    //   178	185	208	java/lang/Throwable
    //   187	197	208	java/lang/Throwable
    //   217	221	224	java/lang/Throwable
    //   52	75	239	java/io/IOException
    //   52	75	239	java/lang/NumberFormatException
    //   197	201	239	java/io/IOException
    //   197	201	239	java/lang/NumberFormatException
    //   217	221	239	java/io/IOException
    //   217	221	239	java/lang/NumberFormatException
    //   225	230	239	java/io/IOException
    //   225	230	239	java/lang/NumberFormatException
    //   233	237	239	java/io/IOException
    //   233	237	239	java/lang/NumberFormatException
    //   237	239	239	java/io/IOException
    //   237	239	239	java/lang/NumberFormatException
  }
}
