package android.net.metrics;

import android.net.NetworkCapabilities;
import com.android.internal.util.BitUtils;
import java.util.Arrays;

public final class DnsEvent
{
  private static final int SIZE_LIMIT = 20000;
  public int eventCount;
  public byte[] eventTypes;
  public int[] latenciesMs;
  public final int netId;
  public byte[] returnCodes;
  public int successCount;
  public final long transports;
  
  public DnsEvent(int paramInt1, long paramLong, int paramInt2)
  {
    netId = paramInt1;
    transports = paramLong;
    eventTypes = new byte[paramInt2];
    returnCodes = new byte[paramInt2];
    latenciesMs = new int[paramInt2];
  }
  
  boolean addResult(byte paramByte1, byte paramByte2, int paramInt)
  {
    boolean bool;
    if (paramByte2 == 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (eventCount >= 20000) {
      return bool;
    }
    if (eventCount == eventTypes.length) {
      resize((int)(1.4D * eventCount));
    }
    eventTypes[eventCount] = ((byte)paramByte1);
    returnCodes[eventCount] = ((byte)paramByte2);
    latenciesMs[eventCount] = paramInt;
    eventCount += 1;
    if (bool) {
      successCount += 1;
    }
    return bool;
  }
  
  public void resize(int paramInt)
  {
    eventTypes = Arrays.copyOf(eventTypes, paramInt);
    returnCodes = Arrays.copyOf(returnCodes, paramInt);
    latenciesMs = Arrays.copyOf(latenciesMs, paramInt);
  }
  
  public String toString()
  {
    Object localObject = new StringBuilder("DnsEvent(");
    ((StringBuilder)localObject).append("netId=");
    ((StringBuilder)localObject).append(netId);
    StringBuilder localStringBuilder = ((StringBuilder)localObject).append(", ");
    localObject = BitUtils.unpackBits(transports);
    int i = localObject.length;
    for (int j = 0; j < i; j++)
    {
      localStringBuilder.append(NetworkCapabilities.transportNameOf(localObject[j]));
      localStringBuilder.append(", ");
    }
    localStringBuilder.append(String.format("%d events, ", new Object[] { Integer.valueOf(eventCount) }));
    localStringBuilder.append(String.format("%d success)", new Object[] { Integer.valueOf(successCount) }));
    return localStringBuilder.toString();
  }
}
