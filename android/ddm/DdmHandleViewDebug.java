package android.ddm;

import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManagerGlobal;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public class DdmHandleViewDebug
  extends ChunkHandler
{
  private static final int CHUNK_VULW = type("VULW");
  private static final int CHUNK_VUOP = type("VUOP");
  private static final int CHUNK_VURT = type("VURT");
  private static final int ERR_EXCEPTION = -3;
  private static final int ERR_INVALID_OP = -1;
  private static final int ERR_INVALID_PARAM = -2;
  private static final String TAG = "DdmViewDebug";
  private static final int VUOP_CAPTURE_VIEW = 1;
  private static final int VUOP_DUMP_DISPLAYLIST = 2;
  private static final int VUOP_INVOKE_VIEW_METHOD = 4;
  private static final int VUOP_PROFILE_VIEW = 3;
  private static final int VUOP_SET_LAYOUT_PARAMETER = 5;
  private static final int VURT_CAPTURE_LAYERS = 2;
  private static final int VURT_DUMP_HIERARCHY = 1;
  private static final int VURT_DUMP_THEME = 3;
  private static final DdmHandleViewDebug sInstance = new DdmHandleViewDebug();
  
  private DdmHandleViewDebug() {}
  
  /* Error */
  private Chunk captureLayers(View paramView)
  {
    // Byte code:
    //   0: new 66	java/io/ByteArrayOutputStream
    //   3: dup
    //   4: sipush 1024
    //   7: invokespecial 69	java/io/ByteArrayOutputStream:<init>	(I)V
    //   10: astore_2
    //   11: new 71	java/io/DataOutputStream
    //   14: dup
    //   15: aload_2
    //   16: invokespecial 74	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   19: astore_3
    //   20: aload_1
    //   21: aload_3
    //   22: invokestatic 79	android/view/ViewDebug:captureLayers	(Landroid/view/View;Ljava/io/DataOutputStream;)V
    //   25: aload_3
    //   26: invokevirtual 82	java/io/DataOutputStream:close	()V
    //   29: goto +4 -> 33
    //   32: astore_1
    //   33: aload_2
    //   34: invokevirtual 86	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   37: astore_1
    //   38: new 88	org/apache/harmony/dalvik/ddmc/Chunk
    //   41: dup
    //   42: getstatic 49	android/ddm/DdmHandleViewDebug:CHUNK_VURT	I
    //   45: aload_1
    //   46: iconst_0
    //   47: aload_1
    //   48: arraylength
    //   49: invokespecial 91	org/apache/harmony/dalvik/ddmc/Chunk:<init>	(I[BII)V
    //   52: areturn
    //   53: astore_1
    //   54: goto +47 -> 101
    //   57: astore_1
    //   58: new 93	java/lang/StringBuilder
    //   61: astore_2
    //   62: aload_2
    //   63: invokespecial 94	java/lang/StringBuilder:<init>	()V
    //   66: aload_2
    //   67: ldc 96
    //   69: invokevirtual 100	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_2
    //   74: aload_1
    //   75: invokevirtual 104	java/io/IOException:getMessage	()Ljava/lang/String;
    //   78: invokevirtual 100	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: pop
    //   82: iconst_1
    //   83: aload_2
    //   84: invokevirtual 107	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   87: invokestatic 111	android/ddm/DdmHandleViewDebug:createFailChunk	(ILjava/lang/String;)Lorg/apache/harmony/dalvik/ddmc/Chunk;
    //   90: astore_1
    //   91: aload_3
    //   92: invokevirtual 82	java/io/DataOutputStream:close	()V
    //   95: goto +4 -> 99
    //   98: astore_3
    //   99: aload_1
    //   100: areturn
    //   101: aload_3
    //   102: invokevirtual 82	java/io/DataOutputStream:close	()V
    //   105: goto +4 -> 109
    //   108: astore_3
    //   109: aload_1
    //   110: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	111	0	this	DdmHandleViewDebug
    //   0	111	1	paramView	View
    //   10	74	2	localObject	Object
    //   19	73	3	localDataOutputStream	java.io.DataOutputStream
    //   98	4	3	localIOException1	IOException
    //   108	1	3	localIOException2	IOException
    // Exception table:
    //   from	to	target	type
    //   25	29	32	java/io/IOException
    //   20	25	53	finally
    //   58	91	53	finally
    //   20	25	57	java/io/IOException
    //   91	95	98	java/io/IOException
    //   101	105	108	java/io/IOException
  }
  
  private Chunk captureView(View paramView1, View paramView2)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(1024);
    try
    {
      ViewDebug.capture(paramView1, localByteArrayOutputStream, paramView2);
      paramView1 = localByteArrayOutputStream.toByteArray();
      return new Chunk(CHUNK_VUOP, paramView1, 0, paramView1.length);
    }
    catch (IOException paramView2)
    {
      paramView1 = new StringBuilder();
      paramView1.append("Unexpected error while capturing view: ");
      paramView1.append(paramView2.getMessage());
    }
    return createFailChunk(1, paramView1.toString());
  }
  
  private Chunk dumpDisplayLists(final View paramView1, final View paramView2)
  {
    paramView1.post(new Runnable()
    {
      public void run()
      {
        ViewDebug.outputDisplayList(paramView1, paramView2);
      }
    });
    return null;
  }
  
  private Chunk dumpHierarchy(View paramView, ByteBuffer paramByteBuffer)
  {
    boolean bool1;
    if (paramByteBuffer.getInt() > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if (paramByteBuffer.getInt() > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    int i;
    if ((paramByteBuffer.hasRemaining()) && (paramByteBuffer.getInt() > 0)) {
      i = 1;
    } else {
      i = 0;
    }
    long l1 = System.currentTimeMillis();
    paramByteBuffer = new ByteArrayOutputStream(2097152);
    if (i != 0) {
      try
      {
        ViewDebug.dumpv2(paramView, paramByteBuffer);
      }
      catch (IOException|InterruptedException paramView)
      {
        break label153;
      }
    } else {
      ViewDebug.dump(paramView, bool1, bool2, paramByteBuffer);
    }
    long l2 = System.currentTimeMillis();
    paramView = new StringBuilder();
    paramView.append("Time to obtain view hierarchy (ms): ");
    paramView.append(l2 - l1);
    Log.d("DdmViewDebug", paramView.toString());
    paramView = paramByteBuffer.toByteArray();
    return new Chunk(CHUNK_VURT, paramView, 0, paramView.length);
    label153:
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("Unexpected error while obtaining view hierarchy: ");
    paramByteBuffer.append(paramView.getMessage());
    return createFailChunk(1, paramByteBuffer.toString());
  }
  
  private Chunk dumpTheme(View paramView)
  {
    Object localObject = new ByteArrayOutputStream(1024);
    try
    {
      ViewDebug.dumpTheme(paramView, (OutputStream)localObject);
      paramView = ((ByteArrayOutputStream)localObject).toByteArray();
      return new Chunk(CHUNK_VURT, paramView, 0, paramView.length);
    }
    catch (IOException paramView)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unexpected error while dumping the theme: ");
      ((StringBuilder)localObject).append(paramView.getMessage());
    }
    return createFailChunk(1, ((StringBuilder)localObject).toString());
  }
  
  private View getRootView(ByteBuffer paramByteBuffer)
  {
    try
    {
      paramByteBuffer = getString(paramByteBuffer, paramByteBuffer.getInt());
      paramByteBuffer = WindowManagerGlobal.getInstance().getRootView(paramByteBuffer);
      return paramByteBuffer;
    }
    catch (BufferUnderflowException paramByteBuffer) {}
    return null;
  }
  
  private View getTargetView(View paramView, ByteBuffer paramByteBuffer)
  {
    try
    {
      paramByteBuffer = getString(paramByteBuffer, paramByteBuffer.getInt());
      return ViewDebug.findView(paramView, paramByteBuffer);
    }
    catch (BufferUnderflowException paramView) {}
    return null;
  }
  
  private Chunk invokeViewMethod(View paramView1, View paramView2, ByteBuffer paramByteBuffer)
  {
    String str = getString(paramByteBuffer, paramByteBuffer.getInt());
    if (!paramByteBuffer.hasRemaining())
    {
      paramView1 = new Class[0];
      paramByteBuffer = new Object[0];
    }
    else
    {
      int i = paramByteBuffer.getInt();
      paramView1 = new Class[i];
      Object[] arrayOfObject = new Object[i];
      for (int j = 0; j < i; j++)
      {
        char c = paramByteBuffer.getChar();
        if (c != 'F')
        {
          if (c != 'S')
          {
            if (c != 'Z')
            {
              switch (c)
              {
              default: 
                switch (c)
                {
                default: 
                  paramView1 = new StringBuilder();
                  paramView1.append("arg ");
                  paramView1.append(j);
                  paramView1.append(", unrecognized type: ");
                  paramView1.append(c);
                  Log.e("DdmViewDebug", paramView1.toString());
                  paramView1 = new StringBuilder();
                  paramView1.append("Unsupported parameter type (");
                  paramView1.append(c);
                  paramView1.append(") to invoke view method.");
                  return createFailChunk(-2, paramView1.toString());
                case 'J': 
                  paramView1[j] = Long.TYPE;
                  arrayOfObject[j] = Long.valueOf(paramByteBuffer.getLong());
                  break;
                case 'I': 
                  paramView1[j] = Integer.TYPE;
                  arrayOfObject[j] = Integer.valueOf(paramByteBuffer.getInt());
                }
                break;
              case 'D': 
                paramView1[j] = Double.TYPE;
                arrayOfObject[j] = Double.valueOf(paramByteBuffer.getDouble());
                break;
              case 'C': 
                paramView1[j] = Character.TYPE;
                arrayOfObject[j] = Character.valueOf(paramByteBuffer.getChar());
                break;
              case 'B': 
                paramView1[j] = Byte.TYPE;
                arrayOfObject[j] = Byte.valueOf(paramByteBuffer.get());
                break;
              }
            }
            else
            {
              paramView1[j] = Boolean.TYPE;
              boolean bool;
              if (paramByteBuffer.get() == 0) {
                bool = false;
              } else {
                bool = true;
              }
              arrayOfObject[j] = Boolean.valueOf(bool);
            }
          }
          else
          {
            paramView1[j] = Short.TYPE;
            arrayOfObject[j] = Short.valueOf(paramByteBuffer.getShort());
          }
        }
        else
        {
          paramView1[j] = Float.TYPE;
          arrayOfObject[j] = Float.valueOf(paramByteBuffer.getFloat());
        }
      }
      paramByteBuffer = arrayOfObject;
    }
    try
    {
      paramView1 = paramView2.getClass().getMethod(str, paramView1);
      try
      {
        ViewDebug.invokeViewMethod(paramView2, paramView1, paramByteBuffer);
        return null;
      }
      catch (Exception paramByteBuffer)
      {
        paramView1 = new StringBuilder();
        paramView1.append("Exception while invoking method: ");
        paramView1.append(paramByteBuffer.getCause().getMessage());
        Log.e("DdmViewDebug", paramView1.toString());
        paramView2 = paramByteBuffer.getCause().getMessage();
        paramView1 = paramView2;
        if (paramView2 == null) {
          paramView1 = paramByteBuffer.getCause().toString();
        }
        return createFailChunk(-3, paramView1);
      }
      return createFailChunk(-2, paramView2.toString());
    }
    catch (NoSuchMethodException paramView1)
    {
      paramView2 = new StringBuilder();
      paramView2.append("No such method: ");
      paramView2.append(paramView1.getMessage());
      Log.e("DdmViewDebug", paramView2.toString());
      paramView2 = new StringBuilder();
      paramView2.append("No such method: ");
      paramView2.append(paramView1.getMessage());
    }
  }
  
  private Chunk listWindows()
  {
    String[] arrayOfString = WindowManagerGlobal.getInstance().getViewRootNames();
    int i = arrayOfString.length;
    int j = 0;
    int k = 4;
    for (int m = 0; m < i; m++) {
      k = k + 4 + arrayOfString[m].length() * 2;
    }
    ByteBuffer localByteBuffer = ByteBuffer.allocate(k);
    localByteBuffer.order(ChunkHandler.CHUNK_ORDER);
    localByteBuffer.putInt(arrayOfString.length);
    k = arrayOfString.length;
    for (m = j; m < k; m++)
    {
      String str = arrayOfString[m];
      localByteBuffer.putInt(str.length());
      putString(localByteBuffer, str);
    }
    return new Chunk(CHUNK_VULW, localByteBuffer);
  }
  
  /* Error */
  private Chunk profileView(View paramView1, View paramView2)
  {
    // Byte code:
    //   0: new 66	java/io/ByteArrayOutputStream
    //   3: dup
    //   4: ldc_w 360
    //   7: invokespecial 69	java/io/ByteArrayOutputStream:<init>	(I)V
    //   10: astore_3
    //   11: new 362	java/io/BufferedWriter
    //   14: dup
    //   15: new 364	java/io/OutputStreamWriter
    //   18: dup
    //   19: aload_3
    //   20: invokespecial 365	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;)V
    //   23: ldc_w 360
    //   26: invokespecial 368	java/io/BufferedWriter:<init>	(Ljava/io/Writer;I)V
    //   29: astore_1
    //   30: aload_2
    //   31: aload_1
    //   32: invokestatic 372	android/view/ViewDebug:profileViewAndChildren	(Landroid/view/View;Ljava/io/BufferedWriter;)V
    //   35: aload_1
    //   36: invokevirtual 373	java/io/BufferedWriter:close	()V
    //   39: goto +4 -> 43
    //   42: astore_1
    //   43: aload_3
    //   44: invokevirtual 86	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   47: astore_1
    //   48: new 88	org/apache/harmony/dalvik/ddmc/Chunk
    //   51: dup
    //   52: getstatic 53	android/ddm/DdmHandleViewDebug:CHUNK_VUOP	I
    //   55: aload_1
    //   56: iconst_0
    //   57: aload_1
    //   58: arraylength
    //   59: invokespecial 91	org/apache/harmony/dalvik/ddmc/Chunk:<init>	(I[BII)V
    //   62: areturn
    //   63: astore_2
    //   64: goto +48 -> 112
    //   67: astore_2
    //   68: new 93	java/lang/StringBuilder
    //   71: astore_3
    //   72: aload_3
    //   73: invokespecial 94	java/lang/StringBuilder:<init>	()V
    //   76: aload_3
    //   77: ldc_w 375
    //   80: invokevirtual 100	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload_3
    //   85: aload_2
    //   86: invokevirtual 104	java/io/IOException:getMessage	()Ljava/lang/String;
    //   89: invokevirtual 100	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: iconst_1
    //   94: aload_3
    //   95: invokevirtual 107	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   98: invokestatic 111	android/ddm/DdmHandleViewDebug:createFailChunk	(ILjava/lang/String;)Lorg/apache/harmony/dalvik/ddmc/Chunk;
    //   101: astore_2
    //   102: aload_1
    //   103: invokevirtual 373	java/io/BufferedWriter:close	()V
    //   106: goto +4 -> 110
    //   109: astore_1
    //   110: aload_2
    //   111: areturn
    //   112: aload_1
    //   113: invokevirtual 373	java/io/BufferedWriter:close	()V
    //   116: goto +4 -> 120
    //   119: astore_1
    //   120: aload_2
    //   121: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	122	0	this	DdmHandleViewDebug
    //   0	122	1	paramView1	View
    //   0	122	2	paramView2	View
    //   10	85	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   35	39	42	java/io/IOException
    //   30	35	63	finally
    //   68	102	63	finally
    //   30	35	67	java/io/IOException
    //   102	106	109	java/io/IOException
    //   112	116	119	java/io/IOException
  }
  
  public static void register()
  {
    DdmServer.registerHandler(CHUNK_VULW, sInstance);
    DdmServer.registerHandler(CHUNK_VURT, sInstance);
    DdmServer.registerHandler(CHUNK_VUOP, sInstance);
  }
  
  private Chunk setLayoutParameter(View paramView1, View paramView2, ByteBuffer paramByteBuffer)
  {
    paramView1 = getString(paramByteBuffer, paramByteBuffer.getInt());
    int i = paramByteBuffer.getInt();
    try
    {
      ViewDebug.setLayoutParameter(paramView2, paramView1, i);
      return null;
    }
    catch (Exception paramView2)
    {
      paramByteBuffer = new StringBuilder();
      paramByteBuffer.append("Exception setting layout parameter: ");
      paramByteBuffer.append(paramView2);
      Log.e("DdmViewDebug", paramByteBuffer.toString());
      paramByteBuffer = new StringBuilder();
      paramByteBuffer.append("Error accessing field ");
      paramByteBuffer.append(paramView1);
      paramByteBuffer.append(":");
      paramByteBuffer.append(paramView2.getMessage());
    }
    return createFailChunk(-3, paramByteBuffer.toString());
  }
  
  public void connected() {}
  
  public void disconnected() {}
  
  public Chunk handleChunk(Chunk paramChunk)
  {
    int i = type;
    if (i == CHUNK_VULW) {
      return listWindows();
    }
    ByteBuffer localByteBuffer = wrapChunk(paramChunk);
    int j = localByteBuffer.getInt();
    View localView = getRootView(localByteBuffer);
    if (localView == null) {
      return createFailChunk(-2, "Invalid View Root");
    }
    if (i == CHUNK_VURT)
    {
      if (j == 1) {
        return dumpHierarchy(localView, localByteBuffer);
      }
      if (j == 2) {
        return captureLayers(localView);
      }
      if (j == 3) {
        return dumpTheme(localView);
      }
      paramChunk = new StringBuilder();
      paramChunk.append("Unknown view root operation: ");
      paramChunk.append(j);
      return createFailChunk(-1, paramChunk.toString());
    }
    paramChunk = getTargetView(localView, localByteBuffer);
    if (paramChunk == null) {
      return createFailChunk(-2, "Invalid target view");
    }
    if (i == CHUNK_VUOP)
    {
      switch (j)
      {
      default: 
        paramChunk = new StringBuilder();
        paramChunk.append("Unknown view operation: ");
        paramChunk.append(j);
        return createFailChunk(-1, paramChunk.toString());
      case 5: 
        return setLayoutParameter(localView, paramChunk, localByteBuffer);
      case 4: 
        return invokeViewMethod(localView, paramChunk, localByteBuffer);
      case 3: 
        return profileView(localView, paramChunk);
      case 2: 
        return dumpDisplayLists(localView, paramChunk);
      }
      return captureView(localView, paramChunk);
    }
    paramChunk = new StringBuilder();
    paramChunk.append("Unknown packet ");
    paramChunk.append(ChunkHandler.name(i));
    throw new RuntimeException(paramChunk.toString());
  }
}
