package android.util.jar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;

class StrictJarManifestReader
{
  private final HashMap<String, Attributes.Name> attributeNameCache = new HashMap();
  private final byte[] buf;
  private int consecutiveLineBreaks = 0;
  private final int endOfMainSection;
  private Attributes.Name name;
  private int pos;
  private String value;
  private final ByteArrayOutputStream valueBuffer = new ByteArrayOutputStream(80);
  
  public StrictJarManifestReader(byte[] paramArrayOfByte, Attributes paramAttributes)
    throws IOException
  {
    buf = paramArrayOfByte;
    while (readHeader()) {
      paramAttributes.put(name, value);
    }
    endOfMainSection = pos;
  }
  
  private boolean readHeader()
    throws IOException
  {
    int i = consecutiveLineBreaks;
    boolean bool = true;
    if (i > 1)
    {
      consecutiveLineBreaks = 0;
      return false;
    }
    readName();
    consecutiveLineBreaks = 0;
    readValue();
    if (consecutiveLineBreaks <= 0) {
      bool = false;
    }
    return bool;
  }
  
  private void readName()
    throws IOException
  {
    int i = pos;
    while (pos < buf.length)
    {
      Object localObject1 = buf;
      int j = pos;
      pos = (j + 1);
      if (localObject1[j] == 58)
      {
        localObject1 = new String(buf, i, pos - i - 1, StandardCharsets.US_ASCII);
        Object localObject2 = buf;
        j = pos;
        pos = (j + 1);
        if (localObject2[j] == 32) {
          try
          {
            name = ((Attributes.Name)attributeNameCache.get(localObject1));
            if (name == null)
            {
              localObject2 = new java/util/jar/Attributes$Name;
              ((Attributes.Name)localObject2).<init>((String)localObject1);
              name = ((Attributes.Name)localObject2);
              attributeNameCache.put(localObject1, name);
            }
            return;
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            throw new IOException(localIllegalArgumentException.getMessage());
          }
        }
        throw new IOException(String.format("Invalid value for attribute '%s'", new Object[] { localIllegalArgumentException }));
      }
    }
  }
  
  private void readValue()
    throws IOException
  {
    int i = 0;
    int j = pos;
    int k = pos;
    valueBuffer.reset();
    while (pos < buf.length)
    {
      byte[] arrayOfByte = buf;
      int m = pos;
      pos = (m + 1);
      m = arrayOfByte[m];
      if (m != 0)
      {
        if (m != 10)
        {
          if (m != 13)
          {
            if ((m == 32) && (consecutiveLineBreaks == 1))
            {
              valueBuffer.write(buf, j, k - j);
              j = pos;
              consecutiveLineBreaks = 0;
            }
            else if (consecutiveLineBreaks >= 1)
            {
              pos -= 1;
            }
            else
            {
              k = pos;
            }
          }
          else
          {
            i = 1;
            consecutiveLineBreaks += 1;
          }
        }
        else if (i != 0) {
          i = 0;
        } else {
          consecutiveLineBreaks += 1;
        }
      }
      else {
        throw new IOException("NUL character in a manifest");
      }
    }
    valueBuffer.write(buf, j, k - j);
    value = valueBuffer.toString(StandardCharsets.UTF_8.name());
  }
  
  public int getEndOfMainSection()
  {
    return endOfMainSection;
  }
  
  public void readEntries(Map<String, Attributes> paramMap, Map<String, StrictJarManifest.Chunk> paramMap1)
    throws IOException
  {
    int i = pos;
    while (readHeader()) {
      if (Attributes.Name.NAME.equals(name))
      {
        String str = value;
        Attributes localAttributes1 = (Attributes)paramMap.get(str);
        Attributes localAttributes2 = localAttributes1;
        if (localAttributes1 == null) {
          localAttributes2 = new Attributes(12);
        }
        while (readHeader()) {
          localAttributes2.put(name, value);
        }
        int j = i;
        if (paramMap1 != null) {
          if (paramMap1.get(str) == null)
          {
            paramMap1.put(str, new StrictJarManifest.Chunk(i, pos));
            j = pos;
          }
          else
          {
            throw new IOException("A jar verifier does not support more than one entry with the same name");
          }
        }
        paramMap.put(str, localAttributes2);
        i = j;
      }
      else
      {
        throw new IOException("Entry is not named");
      }
    }
  }
}
