package android.mtp;

public class MtpDeviceInfo
{
  private int[] mEventsSupported;
  private String mManufacturer;
  private String mModel;
  private int[] mOperationsSupported;
  private String mSerialNumber;
  private String mVersion;
  
  private MtpDeviceInfo() {}
  
  private static boolean isSupported(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfInt[j] == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  public final int[] getEventsSupported()
  {
    return mEventsSupported;
  }
  
  public final String getManufacturer()
  {
    return mManufacturer;
  }
  
  public final String getModel()
  {
    return mModel;
  }
  
  public final int[] getOperationsSupported()
  {
    return mOperationsSupported;
  }
  
  public final String getSerialNumber()
  {
    return mSerialNumber;
  }
  
  public final String getVersion()
  {
    return mVersion;
  }
  
  public boolean isEventSupported(int paramInt)
  {
    return isSupported(mEventsSupported, paramInt);
  }
  
  public boolean isOperationSupported(int paramInt)
  {
    return isSupported(mOperationsSupported, paramInt);
  }
}
