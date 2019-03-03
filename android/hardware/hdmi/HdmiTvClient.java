package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.util.Log;
import java.util.Collections;
import java.util.List;
import libcore.util.EmptyArray;

@SystemApi
public final class HdmiTvClient
  extends HdmiClient
{
  private static final String TAG = "HdmiTvClient";
  public static final int VENDOR_DATA_SIZE = 16;
  
  HdmiTvClient(IHdmiControlService paramIHdmiControlService)
  {
    super(paramIHdmiControlService);
  }
  
  private void checkTimerRecordingSourceType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid source type:");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
  
  static HdmiTvClient create(IHdmiControlService paramIHdmiControlService)
  {
    return new HdmiTvClient(paramIHdmiControlService);
  }
  
  private static IHdmiControlCallback getCallbackWrapper(SelectCallback paramSelectCallback)
  {
    new IHdmiControlCallback.Stub()
    {
      public void onComplete(int paramAnonymousInt)
      {
        HdmiTvClient.this.onComplete(paramAnonymousInt);
      }
    };
  }
  
  private static IHdmiInputChangeListener getListenerWrapper(InputChangeListener paramInputChangeListener)
  {
    new IHdmiInputChangeListener.Stub()
    {
      public void onChanged(HdmiDeviceInfo paramAnonymousHdmiDeviceInfo)
      {
        HdmiTvClient.this.onChanged(paramAnonymousHdmiDeviceInfo);
      }
    };
  }
  
  private IHdmiMhlVendorCommandListener getListenerWrapper(final HdmiMhlVendorCommandListener paramHdmiMhlVendorCommandListener)
  {
    new IHdmiMhlVendorCommandListener.Stub()
    {
      public void onReceived(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, byte[] paramAnonymousArrayOfByte)
      {
        paramHdmiMhlVendorCommandListener.onReceived(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousArrayOfByte);
      }
    };
  }
  
  private static IHdmiRecordListener getListenerWrapper(HdmiRecordListener paramHdmiRecordListener)
  {
    new IHdmiRecordListener.Stub()
    {
      public byte[] getOneTouchRecordSource(int paramAnonymousInt)
      {
        HdmiRecordSources.RecordSource localRecordSource = onOneTouchRecordSourceRequested(paramAnonymousInt);
        if (localRecordSource == null) {
          return EmptyArray.BYTE;
        }
        byte[] arrayOfByte = new byte[localRecordSource.getDataSize(true)];
        localRecordSource.toByteArray(true, arrayOfByte, 0);
        return arrayOfByte;
      }
      
      public void onClearTimerRecordingResult(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        HdmiTvClient.this.onClearTimerRecordingResult(paramAnonymousInt1, paramAnonymousInt2);
      }
      
      public void onOneTouchRecordResult(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        HdmiTvClient.this.onOneTouchRecordResult(paramAnonymousInt1, paramAnonymousInt2);
      }
      
      public void onTimerRecordingResult(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        onTimerRecordingResult(paramAnonymousInt1, HdmiRecordListener.TimerStatusData.parseFrom(paramAnonymousInt2));
      }
    };
  }
  
  public void clearTimerRecording(int paramInt1, int paramInt2, HdmiTimerRecordSources.TimerRecordSource paramTimerRecordSource)
  {
    if (paramTimerRecordSource != null)
    {
      checkTimerRecordingSourceType(paramInt2);
      try
      {
        byte[] arrayOfByte = new byte[paramTimerRecordSource.getDataSize()];
        paramTimerRecordSource.toByteArray(arrayOfByte, 0);
        mService.clearTimerRecording(paramInt1, paramInt2, arrayOfByte);
      }
      catch (RemoteException paramTimerRecordSource)
      {
        Log.e("HdmiTvClient", "failed to start record: ", paramTimerRecordSource);
      }
      return;
    }
    throw new IllegalArgumentException("source must not be null.");
  }
  
  public void deviceSelect(int paramInt, SelectCallback paramSelectCallback)
  {
    if (paramSelectCallback != null)
    {
      try
      {
        mService.deviceSelect(paramInt, getCallbackWrapper(paramSelectCallback));
      }
      catch (RemoteException paramSelectCallback)
      {
        Log.e("HdmiTvClient", "failed to select device: ", paramSelectCallback);
      }
      return;
    }
    throw new IllegalArgumentException("callback must not be null.");
  }
  
  public List<HdmiDeviceInfo> getDeviceList()
  {
    try
    {
      List localList = mService.getDeviceList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("TAG", "Failed to call getDeviceList():", localRemoteException);
    }
    return Collections.emptyList();
  }
  
  public int getDeviceType()
  {
    return 0;
  }
  
  public void portSelect(int paramInt, SelectCallback paramSelectCallback)
  {
    if (paramSelectCallback != null)
    {
      try
      {
        mService.portSelect(paramInt, getCallbackWrapper(paramSelectCallback));
      }
      catch (RemoteException paramSelectCallback)
      {
        Log.e("HdmiTvClient", "failed to select port: ", paramSelectCallback);
      }
      return;
    }
    throw new IllegalArgumentException("Callback must not be null");
  }
  
  public void sendMhlVendorCommand(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length == 16))
    {
      if ((paramInt2 >= 0) && (paramInt2 < 16))
      {
        if ((paramInt3 >= 0) && (paramInt2 + paramInt3 <= 16))
        {
          try
          {
            mService.sendMhlVendorCommand(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
          }
          catch (RemoteException paramArrayOfByte)
          {
            Log.e("HdmiTvClient", "failed to send vendor command: ", paramArrayOfByte);
          }
          return;
        }
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("Invalid length:");
        paramArrayOfByte.append(paramInt3);
        throw new IllegalArgumentException(paramArrayOfByte.toString());
      }
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Invalid offset:");
      paramArrayOfByte.append(paramInt2);
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    throw new IllegalArgumentException("Invalid vendor command data.");
  }
  
  public void sendStandby(int paramInt)
  {
    try
    {
      mService.sendStandby(getDeviceType(), paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("HdmiTvClient", "sendStandby threw exception ", localRemoteException);
    }
  }
  
  public void setHdmiMhlVendorCommandListener(HdmiMhlVendorCommandListener paramHdmiMhlVendorCommandListener)
  {
    if (paramHdmiMhlVendorCommandListener != null)
    {
      try
      {
        mService.addHdmiMhlVendorCommandListener(getListenerWrapper(paramHdmiMhlVendorCommandListener));
      }
      catch (RemoteException paramHdmiMhlVendorCommandListener)
      {
        Log.e("HdmiTvClient", "failed to set hdmi mhl vendor command listener: ", paramHdmiMhlVendorCommandListener);
      }
      return;
    }
    throw new IllegalArgumentException("listener must not be null.");
  }
  
  public void setInputChangeListener(InputChangeListener paramInputChangeListener)
  {
    if (paramInputChangeListener != null)
    {
      try
      {
        mService.setInputChangeListener(getListenerWrapper(paramInputChangeListener));
      }
      catch (RemoteException paramInputChangeListener)
      {
        Log.e("TAG", "Failed to set InputChangeListener:", paramInputChangeListener);
      }
      return;
    }
    throw new IllegalArgumentException("listener must not be null.");
  }
  
  public void setRecordListener(HdmiRecordListener paramHdmiRecordListener)
  {
    if (paramHdmiRecordListener != null)
    {
      try
      {
        mService.setHdmiRecordListener(getListenerWrapper(paramHdmiRecordListener));
      }
      catch (RemoteException paramHdmiRecordListener)
      {
        Log.e("HdmiTvClient", "failed to set record listener.", paramHdmiRecordListener);
      }
      return;
    }
    throw new IllegalArgumentException("listener must not be null.");
  }
  
  public void setSystemAudioMode(boolean paramBoolean, SelectCallback paramSelectCallback)
  {
    try
    {
      mService.setSystemAudioMode(paramBoolean, getCallbackWrapper(paramSelectCallback));
    }
    catch (RemoteException paramSelectCallback)
    {
      Log.e("HdmiTvClient", "failed to set system audio mode:", paramSelectCallback);
    }
  }
  
  public void setSystemAudioMute(boolean paramBoolean)
  {
    try
    {
      mService.setSystemAudioMute(paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("HdmiTvClient", "failed to set mute: ", localRemoteException);
    }
  }
  
  public void setSystemAudioVolume(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mService.setSystemAudioVolume(paramInt1, paramInt2, paramInt3);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("HdmiTvClient", "failed to set volume: ", localRemoteException);
    }
  }
  
  public void startOneTouchRecord(int paramInt, HdmiRecordSources.RecordSource paramRecordSource)
  {
    if (paramRecordSource != null)
    {
      try
      {
        byte[] arrayOfByte = new byte[paramRecordSource.getDataSize(true)];
        paramRecordSource.toByteArray(true, arrayOfByte, 0);
        mService.startOneTouchRecord(paramInt, arrayOfByte);
      }
      catch (RemoteException paramRecordSource)
      {
        Log.e("HdmiTvClient", "failed to start record: ", paramRecordSource);
      }
      return;
    }
    throw new IllegalArgumentException("source must not be null.");
  }
  
  public void startTimerRecording(int paramInt1, int paramInt2, HdmiTimerRecordSources.TimerRecordSource paramTimerRecordSource)
  {
    if (paramTimerRecordSource != null)
    {
      checkTimerRecordingSourceType(paramInt2);
      try
      {
        byte[] arrayOfByte = new byte[paramTimerRecordSource.getDataSize()];
        paramTimerRecordSource.toByteArray(arrayOfByte, 0);
        mService.startTimerRecording(paramInt1, paramInt2, arrayOfByte);
      }
      catch (RemoteException paramTimerRecordSource)
      {
        Log.e("HdmiTvClient", "failed to start record: ", paramTimerRecordSource);
      }
      return;
    }
    throw new IllegalArgumentException("source must not be null.");
  }
  
  public void stopOneTouchRecord(int paramInt)
  {
    try
    {
      mService.stopOneTouchRecord(paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("HdmiTvClient", "failed to stop record: ", localRemoteException);
    }
  }
  
  public static abstract interface HdmiMhlVendorCommandListener
  {
    public abstract void onReceived(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);
  }
  
  public static abstract interface InputChangeListener
  {
    public abstract void onChanged(HdmiDeviceInfo paramHdmiDeviceInfo);
  }
  
  public static abstract interface SelectCallback
  {
    public abstract void onComplete(int paramInt);
  }
}
