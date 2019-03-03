package android.net.wifi.p2p.nsd;

import java.util.Locale;

public class WifiP2pUpnpServiceRequest
  extends WifiP2pServiceRequest
{
  protected WifiP2pUpnpServiceRequest()
  {
    super(2, null);
  }
  
  protected WifiP2pUpnpServiceRequest(String paramString)
  {
    super(2, paramString);
  }
  
  public static WifiP2pUpnpServiceRequest newInstance()
  {
    return new WifiP2pUpnpServiceRequest();
  }
  
  public static WifiP2pUpnpServiceRequest newInstance(String paramString)
  {
    if (paramString != null)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(16) }));
      localStringBuffer.append(WifiP2pServiceInfo.bin2HexStr(paramString.getBytes()));
      return new WifiP2pUpnpServiceRequest(localStringBuffer.toString());
    }
    throw new IllegalArgumentException("search target cannot be null");
  }
}
