package android.media;

import java.util.Arrays;
import java.util.UUID;

public abstract class DrmInitData
{
  DrmInitData() {}
  
  public abstract SchemeInitData get(UUID paramUUID);
  
  public static final class SchemeInitData
  {
    public final byte[] data;
    public final String mimeType;
    
    public SchemeInitData(String paramString, byte[] paramArrayOfByte)
    {
      mimeType = paramString;
      data = paramArrayOfByte;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof SchemeInitData)) {
        return false;
      }
      boolean bool = true;
      if (paramObject == this) {
        return true;
      }
      paramObject = (SchemeInitData)paramObject;
      if ((!mimeType.equals(mimeType)) || (!Arrays.equals(data, data))) {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return mimeType.hashCode() + 31 * Arrays.hashCode(data);
    }
  }
}
