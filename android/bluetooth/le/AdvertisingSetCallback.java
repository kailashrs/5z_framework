package android.bluetooth.le;

public abstract class AdvertisingSetCallback
{
  public static final int ADVERTISE_FAILED_ALREADY_STARTED = 3;
  public static final int ADVERTISE_FAILED_DATA_TOO_LARGE = 1;
  public static final int ADVERTISE_FAILED_FEATURE_UNSUPPORTED = 5;
  public static final int ADVERTISE_FAILED_INTERNAL_ERROR = 4;
  public static final int ADVERTISE_FAILED_TOO_MANY_ADVERTISERS = 2;
  public static final int ADVERTISE_SUCCESS = 0;
  
  public AdvertisingSetCallback() {}
  
  public void onAdvertisingDataSet(AdvertisingSet paramAdvertisingSet, int paramInt) {}
  
  public void onAdvertisingEnabled(AdvertisingSet paramAdvertisingSet, boolean paramBoolean, int paramInt) {}
  
  public void onAdvertisingParametersUpdated(AdvertisingSet paramAdvertisingSet, int paramInt1, int paramInt2) {}
  
  public void onAdvertisingSetStarted(AdvertisingSet paramAdvertisingSet, int paramInt1, int paramInt2) {}
  
  public void onAdvertisingSetStopped(AdvertisingSet paramAdvertisingSet) {}
  
  public void onOwnAddressRead(AdvertisingSet paramAdvertisingSet, int paramInt, String paramString) {}
  
  public void onPeriodicAdvertisingDataSet(AdvertisingSet paramAdvertisingSet, int paramInt) {}
  
  public void onPeriodicAdvertisingEnabled(AdvertisingSet paramAdvertisingSet, boolean paramBoolean, int paramInt) {}
  
  public void onPeriodicAdvertisingParametersUpdated(AdvertisingSet paramAdvertisingSet, int paramInt) {}
  
  public void onScanResponseDataSet(AdvertisingSet paramAdvertisingSet, int paramInt) {}
}
