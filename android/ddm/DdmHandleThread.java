package android.ddm;

import java.nio.ByteBuffer;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;
import org.apache.harmony.dalvik.ddmc.DdmVmInternal;

public class DdmHandleThread
  extends ChunkHandler
{
  public static final int CHUNK_STKL = type("STKL");
  public static final int CHUNK_THCR;
  public static final int CHUNK_THDE;
  public static final int CHUNK_THEN = type("THEN");
  public static final int CHUNK_THST;
  private static DdmHandleThread mInstance = new DdmHandleThread();
  
  static
  {
    CHUNK_THCR = type("THCR");
    CHUNK_THDE = type("THDE");
    CHUNK_THST = type("THST");
  }
  
  private DdmHandleThread() {}
  
  private Chunk createStackChunk(StackTraceElement[] paramArrayOfStackTraceElement, int paramInt)
  {
    int i = paramArrayOfStackTraceElement.length;
    int j = 0 + 4 + 4 + 4;
    for (int k = 0; k < i; k++)
    {
      localObject = paramArrayOfStackTraceElement[k];
      int m = j + (((StackTraceElement)localObject).getClassName().length() * 2 + 4) + (((StackTraceElement)localObject).getMethodName().length() * 2 + 4) + 4;
      j = m;
      if (((StackTraceElement)localObject).getFileName() != null) {
        j = m + ((StackTraceElement)localObject).getFileName().length() * 2;
      }
      j += 4;
    }
    Object localObject = ByteBuffer.allocate(j);
    ((ByteBuffer)localObject).putInt(0);
    ((ByteBuffer)localObject).putInt(paramInt);
    ((ByteBuffer)localObject).putInt(paramArrayOfStackTraceElement.length);
    k = paramArrayOfStackTraceElement.length;
    for (paramInt = 0; paramInt < k; paramInt++)
    {
      StackTraceElement localStackTraceElement = paramArrayOfStackTraceElement[paramInt];
      ((ByteBuffer)localObject).putInt(localStackTraceElement.getClassName().length());
      putString((ByteBuffer)localObject, localStackTraceElement.getClassName());
      ((ByteBuffer)localObject).putInt(localStackTraceElement.getMethodName().length());
      putString((ByteBuffer)localObject, localStackTraceElement.getMethodName());
      if (localStackTraceElement.getFileName() != null)
      {
        ((ByteBuffer)localObject).putInt(localStackTraceElement.getFileName().length());
        putString((ByteBuffer)localObject, localStackTraceElement.getFileName());
      }
      else
      {
        ((ByteBuffer)localObject).putInt(0);
      }
      ((ByteBuffer)localObject).putInt(localStackTraceElement.getLineNumber());
    }
    return new Chunk(CHUNK_STKL, (ByteBuffer)localObject);
  }
  
  private Chunk handleSTKL(Chunk paramChunk)
  {
    int i = wrapChunk(paramChunk).getInt();
    paramChunk = DdmVmInternal.getStackTraceById(i);
    if (paramChunk == null) {
      return createFailChunk(1, "Stack trace unavailable");
    }
    return createStackChunk(paramChunk, i);
  }
  
  private Chunk handleTHEN(Chunk paramChunk)
  {
    boolean bool;
    if (wrapChunk(paramChunk).get() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    DdmVmInternal.threadNotify(bool);
    return null;
  }
  
  private Chunk handleTHST(Chunk paramChunk)
  {
    wrapChunk(paramChunk);
    paramChunk = DdmVmInternal.getThreadStats();
    if (paramChunk != null) {
      return new Chunk(CHUNK_THST, paramChunk, 0, paramChunk.length);
    }
    return createFailChunk(1, "Can't build THST chunk");
  }
  
  public static void register()
  {
    DdmServer.registerHandler(CHUNK_THEN, mInstance);
    DdmServer.registerHandler(CHUNK_THST, mInstance);
    DdmServer.registerHandler(CHUNK_STKL, mInstance);
  }
  
  public void connected() {}
  
  public void disconnected() {}
  
  public Chunk handleChunk(Chunk paramChunk)
  {
    int i = type;
    if (i == CHUNK_THEN) {
      return handleTHEN(paramChunk);
    }
    if (i == CHUNK_THST) {
      return handleTHST(paramChunk);
    }
    if (i == CHUNK_STKL) {
      return handleSTKL(paramChunk);
    }
    paramChunk = new StringBuilder();
    paramChunk.append("Unknown packet ");
    paramChunk.append(ChunkHandler.name(i));
    throw new RuntimeException(paramChunk.toString());
  }
}
