package android.ddm;

import android.os.Debug;
import android.os.Process;
import android.os.UserHandle;
import dalvik.system.VMRuntime;
import java.nio.ByteBuffer;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public class DdmHandleHello
  extends ChunkHandler
{
  public static final int CHUNK_FEAT;
  public static final int CHUNK_HELO = type("HELO");
  public static final int CHUNK_WAIT = type("WAIT");
  private static final String[] FRAMEWORK_FEATURES = { "opengl-tracing", "view-hierarchy" };
  private static DdmHandleHello mInstance;
  
  static
  {
    CHUNK_FEAT = type("FEAT");
    mInstance = new DdmHandleHello();
  }
  
  private DdmHandleHello() {}
  
  private Chunk handleFEAT(Chunk paramChunk)
  {
    paramChunk = Debug.getVmFeatureList();
    int i = 4 + (paramChunk.length + FRAMEWORK_FEATURES.length) * 4;
    for (int j = paramChunk.length - 1; j >= 0; j--) {
      i += paramChunk[j].length() * 2;
    }
    int k = FRAMEWORK_FEATURES.length - 1;
    j = i;
    for (i = k; i >= 0; i--) {
      j += FRAMEWORK_FEATURES[i].length() * 2;
    }
    ByteBuffer localByteBuffer = ByteBuffer.allocate(j);
    localByteBuffer.order(ChunkHandler.CHUNK_ORDER);
    localByteBuffer.putInt(paramChunk.length + FRAMEWORK_FEATURES.length);
    for (i = paramChunk.length - 1; i >= 0; i--)
    {
      localByteBuffer.putInt(paramChunk[i].length());
      putString(localByteBuffer, paramChunk[i]);
    }
    for (i = FRAMEWORK_FEATURES.length - 1; i >= 0; i--)
    {
      localByteBuffer.putInt(FRAMEWORK_FEATURES[i].length());
      putString(localByteBuffer, FRAMEWORK_FEATURES[i]);
    }
    return new Chunk(CHUNK_FEAT, localByteBuffer);
  }
  
  private Chunk handleHELO(Chunk paramChunk)
  {
    wrapChunk(paramChunk).getInt();
    Object localObject1 = System.getProperty("java.vm.name", "?");
    paramChunk = System.getProperty("java.vm.version", "?");
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(" v");
    ((StringBuilder)localObject2).append(paramChunk);
    localObject2 = ((StringBuilder)localObject2).toString();
    String str = DdmHandleAppName.getAppName();
    Object localObject3 = VMRuntime.getRuntime();
    if (((VMRuntime)localObject3).is64Bit()) {
      paramChunk = "64-bit";
    } else {
      paramChunk = "32-bit";
    }
    Object localObject4 = ((VMRuntime)localObject3).vmInstructionSet();
    localObject1 = paramChunk;
    if (localObject4 != null)
    {
      localObject1 = paramChunk;
      if (((String)localObject4).length() > 0)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append(paramChunk);
        ((StringBuilder)localObject1).append(" (");
        ((StringBuilder)localObject1).append((String)localObject4);
        ((StringBuilder)localObject1).append(")");
        localObject1 = ((StringBuilder)localObject1).toString();
      }
    }
    localObject4 = new StringBuilder();
    ((StringBuilder)localObject4).append("CheckJNI=");
    if (((VMRuntime)localObject3).isCheckJniEnabled()) {
      paramChunk = "true";
    } else {
      paramChunk = "false";
    }
    ((StringBuilder)localObject4).append(paramChunk);
    paramChunk = ((StringBuilder)localObject4).toString();
    boolean bool = ((VMRuntime)localObject3).isNativeDebuggable();
    localObject3 = ByteBuffer.allocate(28 + ((String)localObject2).length() * 2 + str.length() * 2 + ((String)localObject1).length() * 2 + paramChunk.length() * 2 + 1);
    ((ByteBuffer)localObject3).order(ChunkHandler.CHUNK_ORDER);
    ((ByteBuffer)localObject3).putInt(1);
    ((ByteBuffer)localObject3).putInt(Process.myPid());
    ((ByteBuffer)localObject3).putInt(((String)localObject2).length());
    ((ByteBuffer)localObject3).putInt(str.length());
    putString((ByteBuffer)localObject3, (String)localObject2);
    putString((ByteBuffer)localObject3, str);
    ((ByteBuffer)localObject3).putInt(UserHandle.myUserId());
    ((ByteBuffer)localObject3).putInt(((String)localObject1).length());
    putString((ByteBuffer)localObject3, (String)localObject1);
    ((ByteBuffer)localObject3).putInt(paramChunk.length());
    putString((ByteBuffer)localObject3, paramChunk);
    ((ByteBuffer)localObject3).put((byte)bool);
    paramChunk = new Chunk(CHUNK_HELO, (ByteBuffer)localObject3);
    if (Debug.waitingForDebugger()) {
      sendWAIT(0);
    }
    return paramChunk;
  }
  
  public static void register()
  {
    DdmServer.registerHandler(CHUNK_HELO, mInstance);
    DdmServer.registerHandler(CHUNK_FEAT, mInstance);
  }
  
  public static void sendWAIT(int paramInt)
  {
    int i = (byte)paramInt;
    DdmServer.sendChunk(new Chunk(CHUNK_WAIT, new byte[] { i }, 0, 1));
  }
  
  public void connected() {}
  
  public void disconnected() {}
  
  public Chunk handleChunk(Chunk paramChunk)
  {
    int i = type;
    if (i == CHUNK_HELO) {
      return handleHELO(paramChunk);
    }
    if (i == CHUNK_FEAT) {
      return handleFEAT(paramChunk);
    }
    paramChunk = new StringBuilder();
    paramChunk.append("Unknown packet ");
    paramChunk.append(ChunkHandler.name(i));
    throw new RuntimeException(paramChunk.toString());
  }
}
