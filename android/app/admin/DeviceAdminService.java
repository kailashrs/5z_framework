package android.app.admin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DeviceAdminService
  extends Service
{
  private final IDeviceAdminServiceImpl mImpl = new IDeviceAdminServiceImpl(null);
  
  public DeviceAdminService() {}
  
  public final IBinder onBind(Intent paramIntent)
  {
    return mImpl.asBinder();
  }
  
  private class IDeviceAdminServiceImpl
    extends IDeviceAdminService.Stub
  {
    private IDeviceAdminServiceImpl() {}
  }
}
