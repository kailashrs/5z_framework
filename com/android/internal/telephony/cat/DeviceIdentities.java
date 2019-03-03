package com.android.internal.telephony.cat;

class DeviceIdentities
  extends ValueObject
{
  public int destinationId;
  public int sourceId;
  
  DeviceIdentities() {}
  
  ComprehensionTlvTag getTag()
  {
    return ComprehensionTlvTag.DEVICE_IDENTITIES;
  }
}
