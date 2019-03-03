package android.util;

import android.annotation.SystemApi;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class EventLog
{
  private static final String COMMENT_PATTERN = "^\\s*(#.*)?$";
  private static final String TAG = "EventLog";
  private static final String TAGS_FILE = "/system/etc/event-log-tags";
  private static final String TAG_PATTERN = "^\\s*(\\d+)\\s+(\\w+)\\s*(\\(.*\\))?\\s*$";
  private static HashMap<String, Integer> sTagCodes = null;
  private static HashMap<Integer, String> sTagNames = null;
  
  public EventLog() {}
  
  public static int getTagCode(String paramString)
  {
    readTagsFile();
    paramString = (Integer)sTagCodes.get(paramString);
    int i;
    if (paramString != null) {
      i = paramString.intValue();
    } else {
      i = -1;
    }
    return i;
  }
  
  public static String getTagName(int paramInt)
  {
    readTagsFile();
    return (String)sTagNames.get(Integer.valueOf(paramInt));
  }
  
  public static native void readEvents(int[] paramArrayOfInt, Collection<Event> paramCollection)
    throws IOException;
  
  @SystemApi
  public static native void readEventsOnWrapping(int[] paramArrayOfInt, long paramLong, Collection<Event> paramCollection)
    throws IOException;
  
  /* Error */
  private static void readTagsFile()
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 29	android/util/EventLog:sTagCodes	Ljava/util/HashMap;
    //   6: ifnull +15 -> 21
    //   9: getstatic 31	android/util/EventLog:sTagNames	Ljava/util/HashMap;
    //   12: astore_0
    //   13: aload_0
    //   14: ifnull +7 -> 21
    //   17: ldc 2
    //   19: monitorexit
    //   20: return
    //   21: new 42	java/util/HashMap
    //   24: astore_0
    //   25: aload_0
    //   26: invokespecial 70	java/util/HashMap:<init>	()V
    //   29: aload_0
    //   30: putstatic 29	android/util/EventLog:sTagCodes	Ljava/util/HashMap;
    //   33: new 42	java/util/HashMap
    //   36: astore_0
    //   37: aload_0
    //   38: invokespecial 70	java/util/HashMap:<init>	()V
    //   41: aload_0
    //   42: putstatic 31	android/util/EventLog:sTagNames	Ljava/util/HashMap;
    //   45: ldc 11
    //   47: invokestatic 76	java/util/regex/Pattern:compile	(Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   50: astore_1
    //   51: ldc 20
    //   53: invokestatic 76	java/util/regex/Pattern:compile	(Ljava/lang/String;)Ljava/util/regex/Pattern;
    //   56: astore_2
    //   57: aconst_null
    //   58: astore_3
    //   59: aconst_null
    //   60: astore 4
    //   62: aload 4
    //   64: astore_0
    //   65: aload_3
    //   66: astore 5
    //   68: new 78	java/io/BufferedReader
    //   71: astore 6
    //   73: aload 4
    //   75: astore_0
    //   76: aload_3
    //   77: astore 5
    //   79: new 80	java/io/FileReader
    //   82: astore 7
    //   84: aload 4
    //   86: astore_0
    //   87: aload_3
    //   88: astore 5
    //   90: aload 7
    //   92: ldc 17
    //   94: invokespecial 83	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   97: aload 4
    //   99: astore_0
    //   100: aload_3
    //   101: astore 5
    //   103: aload 6
    //   105: aload 7
    //   107: sipush 256
    //   110: invokespecial 86	java/io/BufferedReader:<init>	(Ljava/io/Reader;I)V
    //   113: aload 6
    //   115: astore 4
    //   117: aload 4
    //   119: astore_0
    //   120: aload 4
    //   122: astore 5
    //   124: aload 4
    //   126: invokevirtual 90	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   129: astore 6
    //   131: aload 6
    //   133: ifnull +270 -> 403
    //   136: aload 4
    //   138: astore_0
    //   139: aload 4
    //   141: astore 5
    //   143: aload_1
    //   144: aload 6
    //   146: invokevirtual 94	java/util/regex/Pattern:matcher	(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   149: invokevirtual 100	java/util/regex/Matcher:matches	()Z
    //   152: ifeq +6 -> 158
    //   155: goto -38 -> 117
    //   158: aload 4
    //   160: astore_0
    //   161: aload 4
    //   163: astore 5
    //   165: aload_2
    //   166: aload 6
    //   168: invokevirtual 94	java/util/regex/Pattern:matcher	(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   171: astore_3
    //   172: aload 4
    //   174: astore_0
    //   175: aload 4
    //   177: astore 5
    //   179: aload_3
    //   180: invokevirtual 100	java/util/regex/Matcher:matches	()Z
    //   183: ifne +73 -> 256
    //   186: aload 4
    //   188: astore_0
    //   189: aload 4
    //   191: astore 5
    //   193: new 102	java/lang/StringBuilder
    //   196: astore_3
    //   197: aload 4
    //   199: astore_0
    //   200: aload 4
    //   202: astore 5
    //   204: aload_3
    //   205: invokespecial 103	java/lang/StringBuilder:<init>	()V
    //   208: aload 4
    //   210: astore_0
    //   211: aload 4
    //   213: astore 5
    //   215: aload_3
    //   216: ldc 105
    //   218: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: pop
    //   222: aload 4
    //   224: astore_0
    //   225: aload 4
    //   227: astore 5
    //   229: aload_3
    //   230: aload 6
    //   232: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: pop
    //   236: aload 4
    //   238: astore_0
    //   239: aload 4
    //   241: astore 5
    //   243: ldc 14
    //   245: aload_3
    //   246: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   249: invokestatic 118	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;)I
    //   252: pop
    //   253: goto -136 -> 117
    //   256: aload 4
    //   258: astore_0
    //   259: aload 4
    //   261: astore 5
    //   263: aload_3
    //   264: iconst_1
    //   265: invokevirtual 121	java/util/regex/Matcher:group	(I)Ljava/lang/String;
    //   268: invokestatic 124	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   271: istore 8
    //   273: aload 4
    //   275: astore_0
    //   276: aload 4
    //   278: astore 5
    //   280: aload_3
    //   281: iconst_2
    //   282: invokevirtual 121	java/util/regex/Matcher:group	(I)Ljava/lang/String;
    //   285: astore_3
    //   286: aload 4
    //   288: astore_0
    //   289: aload 4
    //   291: astore 5
    //   293: getstatic 29	android/util/EventLog:sTagCodes	Ljava/util/HashMap;
    //   296: aload_3
    //   297: iload 8
    //   299: invokestatic 58	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   302: invokevirtual 128	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   305: pop
    //   306: aload 4
    //   308: astore_0
    //   309: aload 4
    //   311: astore 5
    //   313: getstatic 31	android/util/EventLog:sTagNames	Ljava/util/HashMap;
    //   316: iload 8
    //   318: invokestatic 58	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   321: aload_3
    //   322: invokevirtual 128	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   325: pop
    //   326: goto +74 -> 400
    //   329: astore 7
    //   331: aload 4
    //   333: astore_0
    //   334: aload 4
    //   336: astore 5
    //   338: new 102	java/lang/StringBuilder
    //   341: astore_3
    //   342: aload 4
    //   344: astore_0
    //   345: aload 4
    //   347: astore 5
    //   349: aload_3
    //   350: invokespecial 103	java/lang/StringBuilder:<init>	()V
    //   353: aload 4
    //   355: astore_0
    //   356: aload 4
    //   358: astore 5
    //   360: aload_3
    //   361: ldc -126
    //   363: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   366: pop
    //   367: aload 4
    //   369: astore_0
    //   370: aload 4
    //   372: astore 5
    //   374: aload_3
    //   375: aload 6
    //   377: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   380: pop
    //   381: aload 4
    //   383: astore_0
    //   384: aload 4
    //   386: astore 5
    //   388: ldc 14
    //   390: aload_3
    //   391: invokevirtual 112	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   394: aload 7
    //   396: invokestatic 133	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   399: pop
    //   400: goto -283 -> 117
    //   403: aload 4
    //   405: invokevirtual 136	java/io/BufferedReader:close	()V
    //   408: goto +37 -> 445
    //   411: astore_0
    //   412: goto +33 -> 445
    //   415: astore 5
    //   417: goto +32 -> 449
    //   420: astore 4
    //   422: aload 5
    //   424: astore_0
    //   425: ldc 14
    //   427: ldc -118
    //   429: aload 4
    //   431: invokestatic 133	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   434: pop
    //   435: aload 5
    //   437: ifnull +8 -> 445
    //   440: aload 5
    //   442: invokevirtual 136	java/io/BufferedReader:close	()V
    //   445: ldc 2
    //   447: monitorexit
    //   448: return
    //   449: aload_0
    //   450: ifnull +11 -> 461
    //   453: aload_0
    //   454: invokevirtual 136	java/io/BufferedReader:close	()V
    //   457: goto +4 -> 461
    //   460: astore_0
    //   461: aload 5
    //   463: athrow
    //   464: astore_0
    //   465: ldc 2
    //   467: monitorexit
    //   468: aload_0
    //   469: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   12	372	0	localObject1	Object
    //   411	1	0	localIOException1	IOException
    //   424	30	0	localObject2	Object
    //   460	1	0	localIOException2	IOException
    //   464	5	0	localObject3	Object
    //   50	94	1	localPattern1	java.util.regex.Pattern
    //   56	110	2	localPattern2	java.util.regex.Pattern
    //   58	333	3	localObject4	Object
    //   60	344	4	localObject5	Object
    //   420	10	4	localIOException3	IOException
    //   66	321	5	localObject6	Object
    //   415	47	5	localObject7	Object
    //   71	305	6	localObject8	Object
    //   82	24	7	localFileReader	java.io.FileReader
    //   329	66	7	localNumberFormatException	NumberFormatException
    //   271	46	8	i	int
    // Exception table:
    //   from	to	target	type
    //   263	273	329	java/lang/NumberFormatException
    //   280	286	329	java/lang/NumberFormatException
    //   293	306	329	java/lang/NumberFormatException
    //   313	326	329	java/lang/NumberFormatException
    //   403	408	411	java/io/IOException
    //   440	445	411	java/io/IOException
    //   68	73	415	finally
    //   79	84	415	finally
    //   90	97	415	finally
    //   103	113	415	finally
    //   124	131	415	finally
    //   143	155	415	finally
    //   165	172	415	finally
    //   179	186	415	finally
    //   193	197	415	finally
    //   204	208	415	finally
    //   215	222	415	finally
    //   229	236	415	finally
    //   243	253	415	finally
    //   263	273	415	finally
    //   280	286	415	finally
    //   293	306	415	finally
    //   313	326	415	finally
    //   338	342	415	finally
    //   349	353	415	finally
    //   360	367	415	finally
    //   374	381	415	finally
    //   388	400	415	finally
    //   425	435	415	finally
    //   68	73	420	java/io/IOException
    //   79	84	420	java/io/IOException
    //   90	97	420	java/io/IOException
    //   103	113	420	java/io/IOException
    //   124	131	420	java/io/IOException
    //   143	155	420	java/io/IOException
    //   165	172	420	java/io/IOException
    //   179	186	420	java/io/IOException
    //   193	197	420	java/io/IOException
    //   204	208	420	java/io/IOException
    //   215	222	420	java/io/IOException
    //   229	236	420	java/io/IOException
    //   243	253	420	java/io/IOException
    //   263	273	420	java/io/IOException
    //   280	286	420	java/io/IOException
    //   293	306	420	java/io/IOException
    //   313	326	420	java/io/IOException
    //   338	342	420	java/io/IOException
    //   349	353	420	java/io/IOException
    //   360	367	420	java/io/IOException
    //   374	381	420	java/io/IOException
    //   388	400	420	java/io/IOException
    //   453	457	460	java/io/IOException
    //   3	13	464	finally
    //   21	57	464	finally
    //   403	408	464	finally
    //   440	445	464	finally
    //   453	457	464	finally
    //   461	464	464	finally
  }
  
  public static native int writeEvent(int paramInt, float paramFloat);
  
  public static native int writeEvent(int paramInt1, int paramInt2);
  
  public static native int writeEvent(int paramInt, long paramLong);
  
  public static native int writeEvent(int paramInt, String paramString);
  
  public static native int writeEvent(int paramInt, Object... paramVarArgs);
  
  public static final class Event
  {
    private static final int DATA_OFFSET = 4;
    private static final byte FLOAT_TYPE = 4;
    private static final int HEADER_SIZE_OFFSET = 2;
    private static final byte INT_TYPE = 0;
    private static final int LENGTH_OFFSET = 0;
    private static final byte LIST_TYPE = 3;
    private static final byte LONG_TYPE = 1;
    private static final int NANOSECONDS_OFFSET = 16;
    private static final int PROCESS_OFFSET = 4;
    private static final int SECONDS_OFFSET = 12;
    private static final byte STRING_TYPE = 2;
    private static final int THREAD_OFFSET = 8;
    private static final int UID_OFFSET = 24;
    private static final int V1_PAYLOAD_START = 20;
    private final ByteBuffer mBuffer;
    private Exception mLastWtf;
    
    Event(byte[] paramArrayOfByte)
    {
      mBuffer = ByteBuffer.wrap(paramArrayOfByte);
      mBuffer.order(ByteOrder.nativeOrder());
    }
    
    private Object decodeObject()
    {
      int i = mBuffer.get();
      Object localObject;
      int j;
      switch (i)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown entry type: ");
        ((StringBuilder)localObject).append(i);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      case 4: 
        return Float.valueOf(mBuffer.getFloat());
      case 3: 
        j = mBuffer.get();
        i = j;
        if (j < 0) {
          i = j + 256;
        }
        localObject = new Object[i];
        for (j = 0; j < i; j++) {
          localObject[j] = decodeObject();
        }
        return localObject;
      case 2: 
        try
        {
          i = mBuffer.getInt();
          j = mBuffer.position();
          mBuffer.position(j + i);
          localObject = new String(mBuffer.array(), j, i, "UTF-8");
          return localObject;
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          Log.wtf("EventLog", "UTF-8 is not supported", localUnsupportedEncodingException);
          mLastWtf = localUnsupportedEncodingException;
          return null;
        }
      case 1: 
        return Long.valueOf(mBuffer.getLong());
      }
      return Integer.valueOf(mBuffer.getInt());
    }
    
    public static Event fromBytes(byte[] paramArrayOfByte)
    {
      return new Event(paramArrayOfByte);
    }
    
    public void clearError()
    {
      mLastWtf = null;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (Event)paramObject;
        return Arrays.equals(mBuffer.array(), mBuffer.array());
      }
      return false;
    }
    
    public byte[] getBytes()
    {
      byte[] arrayOfByte = mBuffer.array();
      return Arrays.copyOf(arrayOfByte, arrayOfByte.length);
    }
    
    /* Error */
    public Object getData()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 50	android/util/EventLog$Event:mBuffer	Ljava/nio/ByteBuffer;
      //   6: iconst_2
      //   7: invokevirtual 177	java/nio/ByteBuffer:getShort	(I)S
      //   10: istore_1
      //   11: iload_1
      //   12: istore_2
      //   13: iload_1
      //   14: ifne +6 -> 20
      //   17: bipush 20
      //   19: istore_2
      //   20: aload_0
      //   21: getfield 50	android/util/EventLog$Event:mBuffer	Ljava/nio/ByteBuffer;
      //   24: aload_0
      //   25: getfield 50	android/util/EventLog$Event:mBuffer	Ljava/nio/ByteBuffer;
      //   28: iconst_0
      //   29: invokevirtual 177	java/nio/ByteBuffer:getShort	(I)S
      //   32: iload_2
      //   33: iadd
      //   34: invokevirtual 180	java/nio/ByteBuffer:limit	(I)Ljava/nio/Buffer;
      //   37: pop
      //   38: aload_0
      //   39: getfield 50	android/util/EventLog$Event:mBuffer	Ljava/nio/ByteBuffer;
      //   42: invokevirtual 182	java/nio/ByteBuffer:limit	()I
      //   45: istore_1
      //   46: iload_2
      //   47: iconst_4
      //   48: iadd
      //   49: iload_1
      //   50: if_icmplt +7 -> 57
      //   53: aload_0
      //   54: monitorexit
      //   55: aconst_null
      //   56: areturn
      //   57: aload_0
      //   58: getfield 50	android/util/EventLog$Event:mBuffer	Ljava/nio/ByteBuffer;
      //   61: iload_2
      //   62: iconst_4
      //   63: iadd
      //   64: invokevirtual 112	java/nio/ByteBuffer:position	(I)Ljava/nio/Buffer;
      //   67: pop
      //   68: aload_0
      //   69: invokespecial 102	android/util/EventLog$Event:decodeObject	()Ljava/lang/Object;
      //   72: astore_3
      //   73: aload_0
      //   74: monitorexit
      //   75: aload_3
      //   76: areturn
      //   77: astore_3
      //   78: goto +99 -> 177
      //   81: astore 4
      //   83: new 71	java/lang/StringBuilder
      //   86: astore_3
      //   87: aload_3
      //   88: invokespecial 72	java/lang/StringBuilder:<init>	()V
      //   91: aload_3
      //   92: ldc -72
      //   94: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   97: pop
      //   98: aload_3
      //   99: aload_0
      //   100: invokevirtual 187	android/util/EventLog$Event:getTag	()I
      //   103: invokevirtual 81	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   106: pop
      //   107: ldc 125
      //   109: aload_3
      //   110: invokevirtual 87	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   113: aload 4
      //   115: invokestatic 133	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   118: pop
      //   119: aload_0
      //   120: aload 4
      //   122: putfield 135	android/util/EventLog$Event:mLastWtf	Ljava/lang/Exception;
      //   125: aload_0
      //   126: monitorexit
      //   127: aconst_null
      //   128: areturn
      //   129: astore 4
      //   131: new 71	java/lang/StringBuilder
      //   134: astore_3
      //   135: aload_3
      //   136: invokespecial 72	java/lang/StringBuilder:<init>	()V
      //   139: aload_3
      //   140: ldc -67
      //   142: invokevirtual 78	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   145: pop
      //   146: aload_3
      //   147: aload_0
      //   148: invokevirtual 187	android/util/EventLog$Event:getTag	()I
      //   151: invokevirtual 81	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   154: pop
      //   155: ldc 125
      //   157: aload_3
      //   158: invokevirtual 87	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   161: aload 4
      //   163: invokestatic 133	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   166: pop
      //   167: aload_0
      //   168: aload 4
      //   170: putfield 135	android/util/EventLog$Event:mLastWtf	Ljava/lang/Exception;
      //   173: aload_0
      //   174: monitorexit
      //   175: aconst_null
      //   176: areturn
      //   177: aload_0
      //   178: monitorexit
      //   179: aload_3
      //   180: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	181	0	this	Event
      //   10	41	1	i	int
      //   12	52	2	j	int
      //   72	4	3	localObject1	Object
      //   77	1	3	localObject2	Object
      //   86	94	3	localStringBuilder	StringBuilder
      //   81	40	4	localBufferUnderflowException	java.nio.BufferUnderflowException
      //   129	40	4	localIllegalArgumentException	IllegalArgumentException
      // Exception table:
      //   from	to	target	type
      //   2	11	77	finally
      //   20	46	77	finally
      //   57	73	77	finally
      //   83	125	77	finally
      //   131	173	77	finally
      //   2	11	81	java/nio/BufferUnderflowException
      //   20	46	81	java/nio/BufferUnderflowException
      //   57	73	81	java/nio/BufferUnderflowException
      //   2	11	129	java/lang/IllegalArgumentException
      //   20	46	129	java/lang/IllegalArgumentException
      //   57	73	129	java/lang/IllegalArgumentException
    }
    
    public Exception getLastError()
    {
      return mLastWtf;
    }
    
    public int getProcessId()
    {
      return mBuffer.getInt(4);
    }
    
    public int getTag()
    {
      int i = mBuffer.getShort(2);
      int j = i;
      if (i == 0) {
        j = 20;
      }
      return mBuffer.getInt(j);
    }
    
    public int getThreadId()
    {
      return mBuffer.getInt(8);
    }
    
    public long getTimeNanos()
    {
      return mBuffer.getInt(12) * 1000000000L + mBuffer.getInt(16);
    }
    
    @SystemApi
    public int getUid()
    {
      try
      {
        int i = mBuffer.getInt(24);
        return i;
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}
      return -1;
    }
    
    public int hashCode()
    {
      return Arrays.hashCode(mBuffer.array());
    }
  }
}
