package android.net.wifi.p2p.nsd;

import android.net.nsd.DnsSdTxtRecord;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class WifiP2pDnsSdServiceInfo
  extends WifiP2pServiceInfo
{
  public static final int DNS_TYPE_PTR = 12;
  public static final int DNS_TYPE_TXT = 16;
  public static final int VERSION_1 = 1;
  private static final Map<String, String> sVmPacket = new HashMap();
  
  static
  {
    sVmPacket.put("_tcp.local.", "c00c");
    sVmPacket.put("local.", "c011");
    sVmPacket.put("_udp.local.", "c01c");
  }
  
  private WifiP2pDnsSdServiceInfo(List<String> paramList)
  {
    super(paramList);
  }
  
  private static String compressDnsName(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      String str = (String)sVmPacket.get(paramString);
      int i;
      if (str != null)
      {
        localStringBuffer.append(str);
      }
      else
      {
        i = paramString.indexOf('.');
        if (i != -1) {
          break label104;
        }
        if (paramString.length() > 0)
        {
          localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(paramString.length()) }));
          localStringBuffer.append(WifiP2pServiceInfo.bin2HexStr(paramString.getBytes()));
        }
        localStringBuffer.append("00");
      }
      return localStringBuffer.toString();
      label104:
      str = paramString.substring(0, i);
      paramString = paramString.substring(i + 1);
      localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(str.length()) }));
      localStringBuffer.append(WifiP2pServiceInfo.bin2HexStr(str.getBytes()));
    }
  }
  
  private static String createPtrServiceQuery(String paramString1, String paramString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("bonjour ");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString2);
    localStringBuilder.append(".local.");
    localStringBuffer.append(createRequest(localStringBuilder.toString(), 12, 1));
    localStringBuffer.append(" ");
    paramString1 = paramString1.getBytes();
    localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(paramString1.length) }));
    localStringBuffer.append(WifiP2pServiceInfo.bin2HexStr(paramString1));
    localStringBuffer.append("c027");
    return localStringBuffer.toString();
  }
  
  static String createRequest(String paramString, int paramInt1, int paramInt2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = paramString;
    if (paramInt1 == 16) {
      str = paramString.toLowerCase(Locale.ROOT);
    }
    localStringBuffer.append(compressDnsName(str));
    localStringBuffer.append(String.format(Locale.US, "%04x", new Object[] { Integer.valueOf(paramInt1) }));
    localStringBuffer.append(String.format(Locale.US, "%02x", new Object[] { Integer.valueOf(paramInt2) }));
    return localStringBuffer.toString();
  }
  
  private static String createTxtServiceQuery(String paramString1, String paramString2, DnsSdTxtRecord paramDnsSdTxtRecord)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("bonjour ");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(".");
    localStringBuilder.append(paramString2);
    localStringBuilder.append(".local.");
    localStringBuffer.append(createRequest(localStringBuilder.toString(), 16, 1));
    localStringBuffer.append(" ");
    paramString1 = paramDnsSdTxtRecord.getRawData();
    if (paramString1.length == 0) {
      localStringBuffer.append("00");
    } else {
      localStringBuffer.append(bin2HexStr(paramString1));
    }
    return localStringBuffer.toString();
  }
  
  public static WifiP2pDnsSdServiceInfo newInstance(String paramString1, String paramString2, Map<String, String> paramMap)
  {
    if ((!TextUtils.isEmpty(paramString1)) && (!TextUtils.isEmpty(paramString2)))
    {
      DnsSdTxtRecord localDnsSdTxtRecord = new DnsSdTxtRecord();
      if (paramMap != null)
      {
        Iterator localIterator = paramMap.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          localDnsSdTxtRecord.set(str, (String)paramMap.get(str));
        }
      }
      paramMap = new ArrayList();
      paramMap.add(createPtrServiceQuery(paramString1, paramString2));
      paramMap.add(createTxtServiceQuery(paramString1, paramString2, localDnsSdTxtRecord));
      return new WifiP2pDnsSdServiceInfo(paramMap);
    }
    throw new IllegalArgumentException("instance name or service type cannot be empty");
  }
}
