package android.ddm;

import android.util.Log;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public class DdmHandleNativeHeap
  extends ChunkHandler
{
  public static final int CHUNK_NHGT = type("NHGT");
  private static DdmHandleNativeHeap mInstance = new DdmHandleNativeHeap();
  
  private DdmHandleNativeHeap() {}
  
  private native byte[] getLeakInfo();
  
  private Chunk handleNHGT(Chunk paramChunk)
  {
    byte[] arrayOfByte = getLeakInfo();
    if (arrayOfByte != null)
    {
      paramChunk = new StringBuilder();
      paramChunk.append("Sending ");
      paramChunk.append(arrayOfByte.length);
      paramChunk.append(" bytes");
      Log.i("ddm-nativeheap", paramChunk.toString());
      return new Chunk(ChunkHandler.type("NHGT"), arrayOfByte, 0, arrayOfByte.length);
    }
    return createFailChunk(1, "Something went wrong");
  }
  
  public static void register()
  {
    DdmServer.registerHandler(CHUNK_NHGT, mInstance);
  }
  
  public void connected() {}
  
  public void disconnected() {}
  
  public Chunk handleChunk(Chunk paramChunk)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Handling ");
    localStringBuilder.append(name(type));
    localStringBuilder.append(" chunk");
    Log.i("ddm-nativeheap", localStringBuilder.toString());
    int i = type;
    if (i == CHUNK_NHGT) {
      return handleNHGT(paramChunk);
    }
    paramChunk = new StringBuilder();
    paramChunk.append("Unknown packet ");
    paramChunk.append(ChunkHandler.name(i));
    throw new RuntimeException(paramChunk.toString());
  }
}
