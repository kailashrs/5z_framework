package android.drm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DrmInfoRequest
{
  public static final String ACCOUNT_ID = "account_id";
  public static final String SUBSCRIPTION_ID = "subscription_id";
  public static final int TYPE_REGISTRATION_INFO = 1;
  public static final int TYPE_RIGHTS_ACQUISITION_INFO = 3;
  public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 4;
  public static final int TYPE_UNREGISTRATION_INFO = 2;
  private final int mInfoType;
  private final String mMimeType;
  private final HashMap<String, Object> mRequestInformation = new HashMap();
  
  public DrmInfoRequest(int paramInt, String paramString)
  {
    mInfoType = paramInt;
    mMimeType = paramString;
    if (isValid()) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("infoType: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(",mimeType: ");
    localStringBuilder.append(paramString);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  static boolean isValidType(int paramInt)
  {
    boolean bool = false;
    switch (paramInt)
    {
    default: 
      break;
    case 1: 
    case 2: 
    case 3: 
    case 4: 
      bool = true;
    }
    return bool;
  }
  
  public Object get(String paramString)
  {
    return mRequestInformation.get(paramString);
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
    if ((mMimeType != null) && (!mMimeType.equals("")) && (mRequestInformation != null) && (isValidType(mInfoType))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Iterator<Object> iterator()
  {
    return mRequestInformation.values().iterator();
  }
  
  public Iterator<String> keyIterator()
  {
    return mRequestInformation.keySet().iterator();
  }
  
  public void put(String paramString, Object paramObject)
  {
    mRequestInformation.put(paramString, paramObject);
  }
}
