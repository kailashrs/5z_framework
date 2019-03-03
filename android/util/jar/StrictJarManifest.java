package android.util.jar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import libcore.io.Streams;

public class StrictJarManifest
  implements Cloneable
{
  static final int LINE_LENGTH_LIMIT = 72;
  private static final byte[] LINE_SEPARATOR = { 13, 10 };
  private static final byte[] VALUE_SEPARATOR = { 58, 32 };
  private HashMap<String, Chunk> chunks;
  private final HashMap<String, Attributes> entries;
  private final Attributes mainAttributes;
  private int mainEnd;
  
  public StrictJarManifest()
  {
    entries = new HashMap();
    mainAttributes = new Attributes();
  }
  
  public StrictJarManifest(StrictJarManifest paramStrictJarManifest)
  {
    mainAttributes = ((Attributes)mainAttributes.clone());
    entries = ((HashMap)((HashMap)paramStrictJarManifest.getEntries()).clone());
  }
  
  public StrictJarManifest(InputStream paramInputStream)
    throws IOException
  {
    this();
    read(Streams.readFully(paramInputStream));
  }
  
  StrictJarManifest(byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException
  {
    this();
    if (paramBoolean) {
      chunks = new HashMap();
    }
    read(paramArrayOfByte);
  }
  
  private void read(byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte.length == 0) {
      return;
    }
    paramArrayOfByte = new StrictJarManifestReader(paramArrayOfByte, mainAttributes);
    mainEnd = paramArrayOfByte.getEndOfMainSection();
    paramArrayOfByte.readEntries(entries, chunks);
  }
  
  static void write(StrictJarManifest paramStrictJarManifest, OutputStream paramOutputStream)
    throws IOException
  {
    CharsetEncoder localCharsetEncoder = StandardCharsets.UTF_8.newEncoder();
    ByteBuffer localByteBuffer = ByteBuffer.allocate(72);
    Object localObject1 = Attributes.Name.MANIFEST_VERSION;
    Object localObject2 = mainAttributes.getValue((Attributes.Name)localObject1);
    Object localObject3 = localObject2;
    if (localObject2 == null)
    {
      localObject1 = Attributes.Name.SIGNATURE_VERSION;
      localObject3 = mainAttributes.getValue((Attributes.Name)localObject1);
    }
    if (localObject3 != null)
    {
      writeEntry(paramOutputStream, (Attributes.Name)localObject1, (String)localObject3, localCharsetEncoder, localByteBuffer);
      localObject3 = mainAttributes.keySet().iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject2 = (Attributes.Name)((Iterator)localObject3).next();
        if (!((Attributes.Name)localObject2).equals(localObject1)) {
          writeEntry(paramOutputStream, (Attributes.Name)localObject2, mainAttributes.getValue((Attributes.Name)localObject2), localCharsetEncoder, localByteBuffer);
        }
      }
    }
    paramOutputStream.write(LINE_SEPARATOR);
    localObject1 = paramStrictJarManifest.getEntries().keySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject3 = (String)((Iterator)localObject1).next();
      writeEntry(paramOutputStream, Attributes.Name.NAME, (String)localObject3, localCharsetEncoder, localByteBuffer);
      localObject2 = (Attributes)entries.get(localObject3);
      Iterator localIterator = ((Attributes)localObject2).keySet().iterator();
      while (localIterator.hasNext())
      {
        localObject3 = (Attributes.Name)localIterator.next();
        writeEntry(paramOutputStream, (Attributes.Name)localObject3, ((Attributes)localObject2).getValue((Attributes.Name)localObject3), localCharsetEncoder, localByteBuffer);
      }
      paramOutputStream.write(LINE_SEPARATOR);
    }
  }
  
  private static void writeEntry(OutputStream paramOutputStream, Attributes.Name paramName, String paramString, CharsetEncoder paramCharsetEncoder, ByteBuffer paramByteBuffer)
    throws IOException
  {
    paramName = paramName.toString();
    paramOutputStream.write(paramName.getBytes(StandardCharsets.US_ASCII));
    paramOutputStream.write(VALUE_SEPARATOR);
    paramCharsetEncoder.reset();
    paramByteBuffer.clear().limit(72 - paramName.length() - 2);
    CharBuffer localCharBuffer = CharBuffer.wrap(paramString);
    for (;;)
    {
      paramString = paramCharsetEncoder.encode(localCharBuffer, paramByteBuffer, true);
      paramName = paramString;
      if (CoderResult.UNDERFLOW == paramString) {
        paramName = paramCharsetEncoder.flush(paramByteBuffer);
      }
      paramOutputStream.write(paramByteBuffer.array(), paramByteBuffer.arrayOffset(), paramByteBuffer.position());
      paramOutputStream.write(LINE_SEPARATOR);
      if (CoderResult.UNDERFLOW == paramName) {
        return;
      }
      paramOutputStream.write(32);
      paramByteBuffer.clear().limit(71);
    }
  }
  
  public void clear()
  {
    entries.clear();
    mainAttributes.clear();
  }
  
  public Object clone()
  {
    return new StrictJarManifest(this);
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (paramObject.getClass() != getClass()) {
      return false;
    }
    if (!mainAttributes.equals(mainAttributes)) {
      return false;
    }
    return getEntries().equals(((StrictJarManifest)paramObject).getEntries());
  }
  
  public Attributes getAttributes(String paramString)
  {
    return (Attributes)getEntries().get(paramString);
  }
  
  Chunk getChunk(String paramString)
  {
    return (Chunk)chunks.get(paramString);
  }
  
  public Map<String, Attributes> getEntries()
  {
    return entries;
  }
  
  public Attributes getMainAttributes()
  {
    return mainAttributes;
  }
  
  int getMainAttributesEnd()
  {
    return mainEnd;
  }
  
  public int hashCode()
  {
    return mainAttributes.hashCode() ^ getEntries().hashCode();
  }
  
  public void read(InputStream paramInputStream)
    throws IOException
  {
    read(Streams.readFullyNoClose(paramInputStream));
  }
  
  void removeChunks()
  {
    chunks = null;
  }
  
  public void write(OutputStream paramOutputStream)
    throws IOException
  {
    write(this, paramOutputStream);
  }
  
  static final class Chunk
  {
    final int end;
    final int start;
    
    Chunk(int paramInt1, int paramInt2)
    {
      start = paramInt1;
      end = paramInt2;
    }
  }
}
