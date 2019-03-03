package com.android.internal.telephony;

public abstract class SmsAddress
{
  public static final int TON_ABBREVIATED = 6;
  public static final int TON_ALPHANUMERIC = 5;
  public static final int TON_INTERNATIONAL = 1;
  public static final int TON_NATIONAL = 2;
  public static final int TON_NETWORK = 3;
  public static final int TON_SUBSCRIBER = 4;
  public static final int TON_UNKNOWN = 0;
  public String address;
  public byte[] origBytes;
  public int ton;
  
  public SmsAddress() {}
  
  public boolean couldBeEmailGateway()
  {
    boolean bool;
    if (address.length() <= 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String getAddressString()
  {
    return address;
  }
  
  public boolean isAlphanumeric()
  {
    boolean bool;
    if (ton == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isNetworkSpecific()
  {
    boolean bool;
    if (ton == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
