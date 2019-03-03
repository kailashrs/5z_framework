package android.view;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ViewHierarchyEncoder
{
  private static final byte SIG_BOOLEAN = 90;
  private static final byte SIG_BYTE = 66;
  private static final byte SIG_DOUBLE = 68;
  private static final short SIG_END_MAP = 0;
  private static final byte SIG_FLOAT = 70;
  private static final byte SIG_INT = 73;
  private static final byte SIG_LONG = 74;
  private static final byte SIG_MAP = 77;
  private static final byte SIG_SHORT = 83;
  private static final byte SIG_STRING = 82;
  private Charset mCharset = Charset.forName("utf-8");
  private short mPropertyId = (short)1;
  private final Map<String, Short> mPropertyNames = new HashMap(200);
  private final DataOutputStream mStream;
  
  public ViewHierarchyEncoder(ByteArrayOutputStream paramByteArrayOutputStream)
  {
    mStream = new DataOutputStream(paramByteArrayOutputStream);
  }
  
  private short createPropertyIndex(String paramString)
  {
    Short localShort1 = (Short)mPropertyNames.get(paramString);
    Short localShort2 = localShort1;
    if (localShort1 == null)
    {
      short s = mPropertyId;
      mPropertyId = ((short)(short)(s + 1));
      localShort2 = Short.valueOf(s);
      mPropertyNames.put(paramString, localShort2);
    }
    return localShort2.shortValue();
  }
  
  private void endPropertyMap()
  {
    writeShort((short)0);
  }
  
  private void startPropertyMap()
  {
    try
    {
      mStream.write(77);
    }
    catch (IOException localIOException) {}
  }
  
  private void writeBoolean(boolean paramBoolean)
  {
    try
    {
      mStream.write(90);
      mStream.write(paramBoolean);
    }
    catch (IOException localIOException) {}
  }
  
  private void writeFloat(float paramFloat)
  {
    try
    {
      mStream.write(70);
      mStream.writeFloat(paramFloat);
    }
    catch (IOException localIOException) {}
  }
  
  private void writeInt(int paramInt)
  {
    try
    {
      mStream.write(73);
      mStream.writeInt(paramInt);
    }
    catch (IOException localIOException) {}
  }
  
  private void writeShort(short paramShort)
  {
    try
    {
      mStream.write(83);
      mStream.writeShort(paramShort);
    }
    catch (IOException localIOException) {}
  }
  
  private void writeString(String paramString)
  {
    String str = paramString;
    if (paramString == null) {
      str = "";
    }
    try
    {
      mStream.write(82);
      paramString = str.getBytes(mCharset);
      int i = (short)Math.min(paramString.length, 32767);
      mStream.writeShort(i);
      mStream.write(paramString, 0, i);
    }
    catch (IOException paramString) {}
  }
  
  public void addProperty(String paramString, float paramFloat)
  {
    writeShort(createPropertyIndex(paramString));
    writeFloat(paramFloat);
  }
  
  public void addProperty(String paramString, int paramInt)
  {
    writeShort(createPropertyIndex(paramString));
    writeInt(paramInt);
  }
  
  public void addProperty(String paramString1, String paramString2)
  {
    writeShort(createPropertyIndex(paramString1));
    writeString(paramString2);
  }
  
  public void addProperty(String paramString, short paramShort)
  {
    writeShort(createPropertyIndex(paramString));
    writeShort(paramShort);
  }
  
  public void addProperty(String paramString, boolean paramBoolean)
  {
    writeShort(createPropertyIndex(paramString));
    writeBoolean(paramBoolean);
  }
  
  public void addPropertyKey(String paramString)
  {
    writeShort(createPropertyIndex(paramString));
  }
  
  public void beginObject(Object paramObject)
  {
    startPropertyMap();
    addProperty("meta:__name__", paramObject.getClass().getName());
    addProperty("meta:__hash__", paramObject.hashCode());
  }
  
  public void endObject()
  {
    endPropertyMap();
  }
  
  public void endStream()
  {
    startPropertyMap();
    addProperty("__name__", "propertyIndex");
    Iterator localIterator = mPropertyNames.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      writeShort(((Short)localEntry.getValue()).shortValue());
      writeString((String)localEntry.getKey());
    }
    endPropertyMap();
  }
}
