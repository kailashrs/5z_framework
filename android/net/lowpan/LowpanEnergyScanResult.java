package android.net.lowpan;

public class LowpanEnergyScanResult
{
  public static final int UNKNOWN = Integer.MAX_VALUE;
  private int mChannel = Integer.MAX_VALUE;
  private int mMaxRssi = Integer.MAX_VALUE;
  
  LowpanEnergyScanResult() {}
  
  public int getChannel()
  {
    return mChannel;
  }
  
  public int getMaxRssi()
  {
    return mMaxRssi;
  }
  
  void setChannel(int paramInt)
  {
    mChannel = paramInt;
  }
  
  void setMaxRssi(int paramInt)
  {
    mMaxRssi = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("LowpanEnergyScanResult(channel: ");
    localStringBuilder.append(mChannel);
    localStringBuilder.append(", maxRssi:");
    localStringBuilder.append(mMaxRssi);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}
