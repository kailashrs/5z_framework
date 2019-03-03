package android.drm;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DrmRights
{
  private String mAccountId;
  private byte[] mData;
  private String mMimeType;
  private String mSubscriptionId;
  
  public DrmRights(ProcessedData paramProcessedData, String paramString)
  {
    if (paramProcessedData != null)
    {
      mData = paramProcessedData.getData();
      mAccountId = paramProcessedData.getAccountId();
      mSubscriptionId = paramProcessedData.getSubscriptionId();
      mMimeType = paramString;
      if (isValid()) {
        return;
      }
      paramProcessedData = new StringBuilder();
      paramProcessedData.append("mimeType: ");
      paramProcessedData.append(mMimeType);
      paramProcessedData.append(",data: ");
      paramProcessedData.append(Arrays.toString(mData));
      throw new IllegalArgumentException(paramProcessedData.toString());
    }
    throw new IllegalArgumentException("data is null");
  }
  
  public DrmRights(File paramFile, String paramString)
  {
    instantiate(paramFile, paramString);
  }
  
  public DrmRights(String paramString1, String paramString2)
  {
    instantiate(new File(paramString1), paramString2);
  }
  
  public DrmRights(String paramString1, String paramString2, String paramString3)
  {
    this(paramString1, paramString2);
    mAccountId = paramString3;
  }
  
  public DrmRights(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this(paramString1, paramString2);
    mAccountId = paramString3;
    mSubscriptionId = paramString4;
  }
  
  private void instantiate(File paramFile, String paramString)
  {
    try
    {
      mData = DrmUtils.readBytes(paramFile);
    }
    catch (IOException paramFile)
    {
      paramFile.printStackTrace();
    }
    mMimeType = paramString;
    if (isValid()) {
      return;
    }
    paramFile = new StringBuilder();
    paramFile.append("mimeType: ");
    paramFile.append(mMimeType);
    paramFile.append(",data: ");
    paramFile.append(Arrays.toString(mData));
    throw new IllegalArgumentException(paramFile.toString());
  }
  
  public String getAccountId()
  {
    return mAccountId;
  }
  
  public byte[] getData()
  {
    return mData;
  }
  
  public String getMimeType()
  {
    return mMimeType;
  }
  
  public String getSubscriptionId()
  {
    return mSubscriptionId;
  }
  
  boolean isValid()
  {
    boolean bool;
    if ((mMimeType != null) && (!mMimeType.equals("")) && (mData != null) && (mData.length > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
