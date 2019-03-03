package android.app.admin;

import com.android.server.LocalServices;

public abstract class DevicePolicyCache
{
  protected DevicePolicyCache() {}
  
  public static DevicePolicyCache getInstance()
  {
    Object localObject = (DevicePolicyManagerInternal)LocalServices.getService(DevicePolicyManagerInternal.class);
    if (localObject != null) {
      localObject = ((DevicePolicyManagerInternal)localObject).getDevicePolicyCache();
    } else {
      localObject = EmptyDevicePolicyCache.INSTANCE;
    }
    return localObject;
  }
  
  public abstract boolean getScreenCaptureDisabled(int paramInt);
  
  private static class EmptyDevicePolicyCache
    extends DevicePolicyCache
  {
    private static final EmptyDevicePolicyCache INSTANCE = new EmptyDevicePolicyCache();
    
    private EmptyDevicePolicyCache() {}
    
    public boolean getScreenCaptureDisabled(int paramInt)
    {
      return false;
    }
  }
}
