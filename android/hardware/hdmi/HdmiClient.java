package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.util.Log;

@SystemApi
public abstract class HdmiClient
{
  private static final String TAG = "HdmiClient";
  private IHdmiVendorCommandListener mIHdmiVendorCommandListener;
  final IHdmiControlService mService;
  
  HdmiClient(IHdmiControlService paramIHdmiControlService)
  {
    mService = paramIHdmiControlService;
  }
  
  private static IHdmiVendorCommandListener getListenerWrapper(HdmiControlManager.VendorCommandListener paramVendorCommandListener)
  {
    new IHdmiVendorCommandListener.Stub()
    {
      public void onControlStateChanged(boolean paramAnonymousBoolean, int paramAnonymousInt)
      {
        HdmiClient.this.onControlStateChanged(paramAnonymousBoolean, paramAnonymousInt);
      }
      
      public void onReceived(int paramAnonymousInt1, int paramAnonymousInt2, byte[] paramAnonymousArrayOfByte, boolean paramAnonymousBoolean)
      {
        HdmiClient.this.onReceived(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousArrayOfByte, paramAnonymousBoolean);
      }
    };
  }
  
  public HdmiDeviceInfo getActiveSource()
  {
    try
    {
      HdmiDeviceInfo localHdmiDeviceInfo = mService.getActiveSource();
      return localHdmiDeviceInfo;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("HdmiClient", "getActiveSource threw exception ", localRemoteException);
    }
    return null;
  }
  
  abstract int getDeviceType();
  
  public void sendKeyEvent(int paramInt, boolean paramBoolean)
  {
    try
    {
      mService.sendKeyEvent(getDeviceType(), paramInt, paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("HdmiClient", "sendKeyEvent threw exception ", localRemoteException);
    }
  }
  
  public void sendVendorCommand(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    try
    {
      mService.sendVendorCommand(getDeviceType(), paramInt, paramArrayOfByte, paramBoolean);
    }
    catch (RemoteException paramArrayOfByte)
    {
      Log.e("HdmiClient", "failed to send vendor command: ", paramArrayOfByte);
    }
  }
  
  public void setVendorCommandListener(HdmiControlManager.VendorCommandListener paramVendorCommandListener)
  {
    if (paramVendorCommandListener != null)
    {
      if (mIHdmiVendorCommandListener == null)
      {
        try
        {
          paramVendorCommandListener = getListenerWrapper(paramVendorCommandListener);
          mService.addVendorCommandListener(paramVendorCommandListener, getDeviceType());
          mIHdmiVendorCommandListener = paramVendorCommandListener;
        }
        catch (RemoteException paramVendorCommandListener)
        {
          Log.e("HdmiClient", "failed to set vendor command listener: ", paramVendorCommandListener);
        }
        return;
      }
      throw new IllegalStateException("listener was already set");
    }
    throw new IllegalArgumentException("listener cannot be null");
  }
}
