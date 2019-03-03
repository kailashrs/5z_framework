package android.ddm;

import java.nio.ByteBuffer;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public class DdmHandleExit
  extends ChunkHandler
{
  public static final int CHUNK_EXIT = type("EXIT");
  private static DdmHandleExit mInstance = new DdmHandleExit();
  
  private DdmHandleExit() {}
  
  public static void register()
  {
    DdmServer.registerHandler(CHUNK_EXIT, mInstance);
  }
  
  public void connected() {}
  
  public void disconnected() {}
  
  public Chunk handleChunk(Chunk paramChunk)
  {
    int i = wrapChunk(paramChunk).getInt();
    Runtime.getRuntime().halt(i);
    return null;
  }
}
