package android.drm;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DrmInfo
{
  private final HashMap<String, Object> mAttributes = new HashMap();
  private byte[] mData;
  private final int mInfoType;
  private final String mMimeType;
  
  public DrmInfo(int paramInt, String paramString1, String paramString2)
  {
    mInfoType = paramInt;
    mMimeType = paramString2;
    try
    {
      mData = DrmUtils.readBytes(paramString1);
    }
    catch (IOException paramString1)
    {
      mData = null;
    }
    if (isValid()) {
      return;
    }
    paramString1 = new StringBuilder();
    paramString1.append("infoType: ");
    paramString1.append(paramInt);
    paramString1.append(",mimeType: ");
    paramString1.append(paramString2);
    paramString1.append(",data: ");
    paramString1.append(Arrays.toString(mData));
    paramString1.toString();
    throw new IllegalArgumentException();
  }
  
  public DrmInfo(int paramInt, byte[] paramArrayOfByte, String paramString)
  {
    mInfoType = paramInt;
    mMimeType = paramString;
    mData = paramArrayOfByte;
    if (isValid()) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("infoType: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(",mimeType: ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(",data: ");
    localStringBuilder.append(Arrays.toString(paramArrayOfByte));
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public Object get(String paramString)
  {
    return mAttributes.get(paramString);
  }
  
  public byte[] getData()
  {
    return mData;
  }
  
  public int getInfoType()
  {
    return mInfoType;
  }
  
  public String getMimeType()
  {
    return mMimeType;
  }
  
  boolean isValid()
  {
    boolean bool;
    if ((mMimeType != null) && (!mMimeType.equals("")) && (mData != null) && (mData.length > 0) && (DrmInfoRequest.isValidType(mInfoType))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Iterator<Object> iterator()
  {
    return mAttributes.values().iterator();
  }
  
  public Iterator<String> keyIterator()
  {
    return mAttributes.keySet().iterator();
  }
  
  public void put(String paramString, Object paramObject)
  {
    mAttributes.put(paramString, paramObject);
  }
}
