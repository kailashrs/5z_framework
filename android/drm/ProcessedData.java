package android.drm;

public class ProcessedData
{
  private String mAccountId = "_NO_USER";
  private final byte[] mData;
  private String mSubscriptionId = "";
  
  ProcessedData(byte[] paramArrayOfByte, String paramString)
  {
    mData = paramArrayOfByte;
    mAccountId = paramString;
  }
  
  ProcessedData(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    mData = paramArrayOfByte;
    mAccountId = paramString1;
    mSubscriptionId = paramString2;
  }
  
  public String getAccountId()
  {
    return mAccountId;
  }
  
  public byte[] getData()
  {
    return mData;
  }
  
  public String getSubscriptionId()
  {
    return mSubscriptionId;
  }
}
