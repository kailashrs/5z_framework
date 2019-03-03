package android.ddm;

import android.os.Debug;
import android.util.Log;
import java.nio.ByteBuffer;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public class DdmHandleProfiling
  extends ChunkHandler
{
  public static final int CHUNK_MPRE;
  public static final int CHUNK_MPRQ;
  public static final int CHUNK_MPRS = type("MPRS");
  public static final int CHUNK_MPSE;
  public static final int CHUNK_MPSS;
  public static final int CHUNK_SPSE = type("SPSE");
  public static final int CHUNK_SPSS;
  private static final boolean DEBUG = false;
  private static DdmHandleProfiling mInstance = new DdmHandleProfiling();
  
  static
  {
    CHUNK_MPRE = type("MPRE");
    CHUNK_MPSS = type("MPSS");
    CHUNK_MPSE = type("MPSE");
    CHUNK_MPRQ = type("MPRQ");
    CHUNK_SPSS = type("SPSS");
  }
  
  private DdmHandleProfiling() {}
  
  private Chunk handleMPRE(Chunk paramChunk)
  {
    int i;
    try
    {
      Debug.stopMethodTracing();
      i = 0;
    }
    catch (RuntimeException paramChunk)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Method profiling end failed: ");
      localStringBuilder.append(paramChunk.getMessage());
      Log.w("ddm-heap", localStringBuilder.toString());
      i = 1;
    }
    paramChunk = new byte[1];
    paramChunk[0] = ((byte)i);
    return new Chunk(CHUNK_MPRE, paramChunk, 0, paramChunk.length);
  }
  
  private Chunk handleMPRQ(Chunk paramChunk)
  {
    int i = Debug.getMethodTracingMode();
    paramChunk = new byte[1];
    paramChunk[0] = ((byte)(byte)i);
    return new Chunk(CHUNK_MPRQ, paramChunk, 0, paramChunk.length);
  }
  
  private Chunk handleMPRS(Chunk paramChunk)
  {
    paramChunk = wrapChunk(paramChunk);
    int i = paramChunk.getInt();
    int j = paramChunk.getInt();
    paramChunk = getString(paramChunk, paramChunk.getInt());
    try
    {
      Debug.startMethodTracing(paramChunk, i, j);
      return null;
    }
    catch (RuntimeException paramChunk) {}
    return createFailChunk(1, paramChunk.getMessage());
  }
  
  private Chunk handleMPSEOrSPSE(Chunk paramChunk, String paramString)
  {
    try
    {
      Debug.stopMethodTracing();
      return null;
    }
    catch (RuntimeException paramChunk)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" prof stream end failed: ");
      localStringBuilder.append(paramChunk.getMessage());
      Log.w("ddm-heap", localStringBuilder.toString());
    }
    return createFailChunk(1, paramChunk.getMessage());
  }
  
  private Chunk handleMPSS(Chunk paramChunk)
  {
    paramChunk = wrapChunk(paramChunk);
    int i = paramChunk.getInt();
    int j = paramChunk.getInt();
    try
    {
      Debug.startMethodTracingDdms(i, j, false, 0);
      return null;
    }
    catch (RuntimeException paramChunk) {}
    return createFailChunk(1, paramChunk.getMessage());
  }
  
  private Chunk handleSPSS(Chunk paramChunk)
  {
    paramChunk = wrapChunk(paramChunk);
    int i = paramChunk.getInt();
    int j = paramChunk.getInt();
    int k = paramChunk.getInt();
    try
    {
      Debug.startMethodTracingDdms(i, j, true, k);
      return null;
    }
    catch (RuntimeException paramChunk) {}
    return createFailChunk(1, paramChunk.getMessage());
  }
  
  public static void register()
  {
    DdmServer.registerHandler(CHUNK_MPRS, mInstance);
    DdmServer.registerHandler(CHUNK_MPRE, mInstance);
    DdmServer.registerHandler(CHUNK_MPSS, mInstance);
    DdmServer.registerHandler(CHUNK_MPSE, mInstance);
    DdmServer.registerHandler(CHUNK_MPRQ, mInstance);
    DdmServer.registerHandler(CHUNK_SPSS, mInstance);
    DdmServer.registerHandler(CHUNK_SPSE, mInstance);
  }
  
  public void connected() {}
  
  public void disconnected() {}
  
  public Chunk handleChunk(Chunk paramChunk)
  {
    int i = type;
    if (i == CHUNK_MPRS) {
      return handleMPRS(paramChunk);
    }
    if (i == CHUNK_MPRE) {
      return handleMPRE(paramChunk);
    }
    if (i == CHUNK_MPSS) {
      return handleMPSS(paramChunk);
    }
    if (i == CHUNK_MPSE) {
      return handleMPSEOrSPSE(paramChunk, "Method");
    }
    if (i == CHUNK_MPRQ) {
      return handleMPRQ(paramChunk);
    }
    if (i == CHUNK_SPSS) {
      return handleSPSS(paramChunk);
    }
    if (i == CHUNK_SPSE) {
      return handleMPSEOrSPSE(paramChunk, "Sample");
    }
    paramChunk = new StringBuilder();
    paramChunk.append("Unknown packet ");
    paramChunk.append(ChunkHandler.name(i));
    throw new RuntimeException(paramChunk.toString());
  }
}
