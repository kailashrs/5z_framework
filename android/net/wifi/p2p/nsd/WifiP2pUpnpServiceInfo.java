package android.net.wifi.p2p.nsd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class WifiP2pUpnpServiceInfo
  extends WifiP2pServiceInfo
{
  public static final int VERSION_1_0 = 16;
  
  private WifiP2pUpnpServiceInfo(List<String> paramList)
  {
    super(paramList);
  }
  
  private static String createSupplicantQuery(String paramString1, String paramString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("upnp ");
    localStringBuffer.append(String.format(Locale.US, "%02x ", new Object[] { Integer.valueOf(16) }));
    localStringBuffer.append("uuid:");
    localStringBuffer.append(paramString1);
    if (paramString2 != null)
    {
      localStringBuffer.append("::");
      localStringBuffer.append(paramString2);
    }
    return localStringBuffer.toString();
  }
  
  public static WifiP2pUpnpServiceInfo newInstance(String paramString1, String paramString2, List<String> paramList)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      UUID.fromString(paramString1);
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(createSupplicantQuery(paramString1, null));
      localArrayList.add(createSupplicantQuery(paramString1, "upnp:rootdevice"));
      localArrayList.add(createSupplicantQuery(paramString1, paramString2));
      if (paramList != null)
      {
        paramString2 = paramList.iterator();
        while (paramString2.hasNext()) {
          localArrayList.add(createSupplicantQuery(paramString1, (String)paramString2.next()));
        }
      }
      return new WifiP2pUpnpServiceInfo(localArrayList);
    }
    throw new IllegalArgumentException("uuid or device cannnot be null");
  }
}
