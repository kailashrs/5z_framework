package android.drm;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DrmUtils
{
  public DrmUtils() {}
  
  public static ExtendedMetadataParser getExtendedMetadataParser(byte[] paramArrayOfByte)
  {
    return new ExtendedMetadataParser(paramArrayOfByte, null);
  }
  
  private static void quietlyDispose(Closeable paramCloseable)
  {
    if (paramCloseable != null) {
      try
      {
        paramCloseable.close();
      }
      catch (IOException paramCloseable) {}
    }
  }
  
  static byte[] readBytes(File paramFile)
    throws IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream);
    paramFile = null;
    try
    {
      int i = localBufferedInputStream.available();
      if (i > 0)
      {
        paramFile = new byte[i];
        localBufferedInputStream.read(paramFile);
      }
      return paramFile;
    }
    finally
    {
      quietlyDispose(localBufferedInputStream);
      quietlyDispose(localFileInputStream);
    }
  }
  
  static byte[] readBytes(String paramString)
    throws IOException
  {
    return readBytes(new File(paramString));
  }
  
  static void removeFile(String paramString)
    throws IOException
  {
    new File(paramString).delete();
  }
  
  static void writeToFile(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    Object localObject1 = null;
    if ((paramString != null) && (paramArrayOfByte != null))
    {
      Object localObject2 = localObject1;
      try
      {
        FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
        localObject2 = localObject1;
        localFileOutputStream.<init>(paramString);
        paramString = localFileOutputStream;
        localObject2 = paramString;
        paramString.write(paramArrayOfByte);
        quietlyDispose(paramString);
      }
      finally
      {
        quietlyDispose((Closeable)localObject2);
      }
    }
  }
  
  public static class ExtendedMetadataParser
  {
    HashMap<String, String> mMap = new HashMap();
    
    private ExtendedMetadataParser(byte[] paramArrayOfByte)
    {
      int i = 0;
      while (i < paramArrayOfByte.length)
      {
        int j = readByte(paramArrayOfByte, i);
        int k = i + 1;
        i = readByte(paramArrayOfByte, k);
        k++;
        String str1 = readMultipleBytes(paramArrayOfByte, j, k);
        j = k + j;
        String str2 = readMultipleBytes(paramArrayOfByte, i, j);
        String str3 = str2;
        if (str2.equals(" ")) {
          str3 = "";
        }
        i = j + i;
        mMap.put(str1, str3);
      }
    }
    
    private int readByte(byte[] paramArrayOfByte, int paramInt)
    {
      return paramArrayOfByte[paramInt];
    }
    
    private String readMultipleBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      byte[] arrayOfByte = new byte[paramInt1];
      int i = paramInt2;
      for (int j = 0; i < paramInt2 + paramInt1; j++)
      {
        arrayOfByte[j] = ((byte)paramArrayOfByte[i]);
        i++;
      }
      return new String(arrayOfByte);
    }
    
    public String get(String paramString)
    {
      return (String)mMap.get(paramString);
    }
    
    public Iterator<String> iterator()
    {
      return mMap.values().iterator();
    }
    
    public Iterator<String> keyIterator()
    {
      return mMap.keySet().iterator();
    }
  }
}
