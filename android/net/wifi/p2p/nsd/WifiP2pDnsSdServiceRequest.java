package android.net.wifi.p2p.nsd;

public class WifiP2pDnsSdServiceRequest
  extends WifiP2pServiceRequest
{
  private WifiP2pDnsSdServiceRequest()
  {
    super(1, null);
  }
  
  private WifiP2pDnsSdServiceRequest(String paramString)
  {
    super(1, paramString);
  }
  
  private WifiP2pDnsSdServiceRequest(String paramString, int paramInt1, int paramInt2)
  {
    super(1, WifiP2pDnsSdServiceInfo.createRequest(paramString, paramInt1, paramInt2));
  }
  
  public static WifiP2pDnsSdServiceRequest newInstance()
  {
    return new WifiP2pDnsSdServiceRequest();
  }
  
  public static WifiP2pDnsSdServiceRequest newInstance(String paramString)
  {
    if (paramString != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(".local.");
      return new WifiP2pDnsSdServiceRequest(localStringBuilder.toString(), 12, 1);
    }
    throw new IllegalArgumentException("service type cannot be null");
  }
  
  public static WifiP2pDnsSdServiceRequest newInstance(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append(".");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(".local.");
      return new WifiP2pDnsSdServiceRequest(localStringBuilder.toString(), 16, 1);
    }
    throw new IllegalArgumentException("instance name or service type cannot be null");
  }
}
