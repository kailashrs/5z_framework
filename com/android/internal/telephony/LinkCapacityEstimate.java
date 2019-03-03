package com.android.internal.telephony;

public class LinkCapacityEstimate
{
  public static final int INVALID = -1;
  public static final int STATUS_ACTIVE = 0;
  public static final int STATUS_SUSPENDED = 1;
  public final int confidence;
  public final int downlinkCapacityKbps;
  public final int status;
  public final int uplinkCapacityKbps;
  
  public LinkCapacityEstimate(int paramInt1, int paramInt2)
  {
    downlinkCapacityKbps = paramInt1;
    uplinkCapacityKbps = paramInt2;
    confidence = -1;
    status = -1;
  }
  
  public LinkCapacityEstimate(int paramInt1, int paramInt2, int paramInt3)
  {
    downlinkCapacityKbps = paramInt1;
    confidence = paramInt2;
    status = paramInt3;
    uplinkCapacityKbps = -1;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{downlinkCapacityKbps=");
    localStringBuilder.append(downlinkCapacityKbps);
    localStringBuilder.append(", uplinkCapacityKbps=");
    localStringBuilder.append(uplinkCapacityKbps);
    localStringBuilder.append(", confidence=");
    localStringBuilder.append(confidence);
    localStringBuilder.append(", status=");
    localStringBuilder.append(status);
    return localStringBuilder.toString();
  }
}
