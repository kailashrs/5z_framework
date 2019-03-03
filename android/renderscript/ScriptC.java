package android.renderscript;

import android.content.res.Resources;

public class ScriptC
  extends Script
{
  private static final String TAG = "ScriptC";
  
  protected ScriptC(int paramInt, RenderScript paramRenderScript)
  {
    super(paramInt, paramRenderScript);
  }
  
  protected ScriptC(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  protected ScriptC(RenderScript paramRenderScript, Resources paramResources, int paramInt)
  {
    super(0L, paramRenderScript);
    long l = internalCreate(paramRenderScript, paramResources, paramInt);
    if (l != 0L)
    {
      setID(l);
      return;
    }
    throw new RSRuntimeException("Loading of ScriptC script failed.");
  }
  
  protected ScriptC(RenderScript paramRenderScript, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    super(0L, paramRenderScript);
    long l;
    if (RenderScript.sPointerSize == 4) {
      l = internalStringCreate(paramRenderScript, paramString, paramArrayOfByte1);
    } else {
      l = internalStringCreate(paramRenderScript, paramString, paramArrayOfByte2);
    }
    if (l != 0L)
    {
      setID(l);
      return;
    }
    throw new RSRuntimeException("Loading of ScriptC script failed.");
  }
  
  /* Error */
  private static long internalCreate(RenderScript paramRenderScript, Resources paramResources, int paramInt)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: aload_1
    //   4: iload_2
    //   5: invokevirtual 49	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   8: astore_3
    //   9: sipush 1024
    //   12: newarray byte
    //   14: astore 4
    //   16: iconst_0
    //   17: istore 5
    //   19: aload 4
    //   21: arraylength
    //   22: iload 5
    //   24: isub
    //   25: istore 6
    //   27: aload 4
    //   29: astore 7
    //   31: iload 6
    //   33: istore 8
    //   35: iload 6
    //   37: ifne +32 -> 69
    //   40: aload 4
    //   42: arraylength
    //   43: iconst_2
    //   44: imul
    //   45: newarray byte
    //   47: astore 7
    //   49: aload 4
    //   51: iconst_0
    //   52: aload 7
    //   54: iconst_0
    //   55: aload 4
    //   57: arraylength
    //   58: invokestatic 55	java/lang/System:arraycopy	([BI[BII)V
    //   61: aload 7
    //   63: arraylength
    //   64: iload 5
    //   66: isub
    //   67: istore 8
    //   69: aload_3
    //   70: aload 7
    //   72: iload 5
    //   74: iload 8
    //   76: invokevirtual 61	java/io/InputStream:read	([BII)I
    //   79: istore 8
    //   81: iload 8
    //   83: ifgt +31 -> 114
    //   86: aload_3
    //   87: invokevirtual 65	java/io/InputStream:close	()V
    //   90: aload_0
    //   91: aload_1
    //   92: iload_2
    //   93: invokevirtual 69	android/content/res/Resources:getResourceEntryName	(I)Ljava/lang/String;
    //   96: invokestatic 73	android/renderscript/RenderScript:getCachePath	()Ljava/lang/String;
    //   99: aload 7
    //   101: iload 5
    //   103: invokevirtual 77	android/renderscript/RenderScript:nScriptCCreate	(Ljava/lang/String;Ljava/lang/String;[BI)J
    //   106: lstore 9
    //   108: ldc 2
    //   110: monitorexit
    //   111: lload 9
    //   113: lreturn
    //   114: iload 5
    //   116: iload 8
    //   118: iadd
    //   119: istore 5
    //   121: aload 7
    //   123: astore 4
    //   125: goto -106 -> 19
    //   128: astore_0
    //   129: aload_3
    //   130: invokevirtual 65	java/io/InputStream:close	()V
    //   133: aload_0
    //   134: athrow
    //   135: astore_0
    //   136: new 79	android/content/res/Resources$NotFoundException
    //   139: astore_0
    //   140: aload_0
    //   141: invokespecial 81	android/content/res/Resources$NotFoundException:<init>	()V
    //   144: aload_0
    //   145: athrow
    //   146: astore_0
    //   147: ldc 2
    //   149: monitorexit
    //   150: aload_0
    //   151: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	152	0	paramRenderScript	RenderScript
    //   0	152	1	paramResources	Resources
    //   0	152	2	paramInt	int
    //   8	122	3	localInputStream	java.io.InputStream
    //   14	110	4	localObject1	Object
    //   17	103	5	i	int
    //   25	11	6	j	int
    //   29	93	7	localObject2	Object
    //   33	86	8	k	int
    //   106	6	9	l	long
    // Exception table:
    //   from	to	target	type
    //   9	16	128	finally
    //   19	27	128	finally
    //   40	61	128	finally
    //   61	69	128	finally
    //   69	81	128	finally
    //   86	90	135	java/io/IOException
    //   129	135	135	java/io/IOException
    //   3	9	146	finally
    //   86	90	146	finally
    //   90	108	146	finally
    //   129	135	146	finally
    //   136	146	146	finally
  }
  
  private static long internalStringCreate(RenderScript paramRenderScript, String paramString, byte[] paramArrayOfByte)
  {
    try
    {
      long l = paramRenderScript.nScriptCCreate(paramString, RenderScript.getCachePath(), paramArrayOfByte, paramArrayOfByte.length);
      return l;
    }
    finally
    {
      paramRenderScript = finally;
      throw paramRenderScript;
    }
  }
}
