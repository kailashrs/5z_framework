package android.ddm;

import java.nio.ByteBuffer;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public class DdmHandleAppName
  extends ChunkHandler
{
  public static final int CHUNK_APNM = type("APNM");
  private static volatile String mAppName = "";
  private static DdmHandleAppName mInstance = new DdmHandleAppName();
  
  private DdmHandleAppName() {}
  
  public static String getAppName()
  {
    return mAppName;
  }
  
  public static void register() {}
  
  private static void sendAPNM(String paramString, int paramInt)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(paramString.length() * 2 + 4 + 4);
    localByteBuffer.order(ChunkHandler.CHUNK_ORDER);
    localByteBuffer.putInt(paramString.length());
    putString(localByteBuffer, paramString);
    localByteBuffer.putInt(paramInt);
    DdmServer.sendChunk(new Chunk(CHUNK_APNM, localByteBuffer));
  }
  
  public static void setAppName(String paramString, int paramInt)
  {
    if ((paramString != null) && (paramString.length() != 0))
    {
      mAppName = paramString;
      sendAPNM(paramString, paramInt);
      return;
    }
  }
  
  public void connected() {}
  
  public void disconnected() {}
  
  public Chunk handleChunk(Chunk paramChunk)
  {
    return null;
  }
}
