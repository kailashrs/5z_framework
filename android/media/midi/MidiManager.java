package android.media.midi;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.ConcurrentHashMap;

public final class MidiManager
{
  public static final String BLUETOOTH_MIDI_SERVICE_CLASS = "com.android.bluetoothmidiservice.BluetoothMidiService";
  public static final String BLUETOOTH_MIDI_SERVICE_INTENT = "android.media.midi.BluetoothMidiService";
  public static final String BLUETOOTH_MIDI_SERVICE_PACKAGE = "com.android.bluetoothmidiservice";
  private static final String TAG = "MidiManager";
  private ConcurrentHashMap<DeviceCallback, DeviceListener> mDeviceListeners = new ConcurrentHashMap();
  private final IMidiManager mService;
  private final IBinder mToken = new Binder();
  
  public MidiManager(IMidiManager paramIMidiManager)
  {
    mService = paramIMidiManager;
  }
  
  private void sendOpenDeviceResponse(final MidiDevice paramMidiDevice, final OnDeviceOpenedListener paramOnDeviceOpenedListener, Handler paramHandler)
  {
    if (paramHandler != null) {
      paramHandler.post(new Runnable()
      {
        public void run()
        {
          paramOnDeviceOpenedListener.onDeviceOpened(paramMidiDevice);
        }
      });
    } else {
      paramOnDeviceOpenedListener.onDeviceOpened(paramMidiDevice);
    }
  }
  
  public MidiDeviceServer createDeviceServer(MidiReceiver[] paramArrayOfMidiReceiver, int paramInt1, String[] paramArrayOfString1, String[] paramArrayOfString2, Bundle paramBundle, int paramInt2, MidiDeviceServer.Callback paramCallback)
  {
    try
    {
      MidiDeviceServer localMidiDeviceServer = new android/media/midi/MidiDeviceServer;
      IMidiManager localIMidiManager = mService;
      try
      {
        localMidiDeviceServer.<init>(localIMidiManager, paramArrayOfMidiReceiver, paramInt1, paramCallback);
        if (mService.registerDeviceServer(localMidiDeviceServer.getBinderInterface(), paramArrayOfMidiReceiver.length, paramInt1, paramArrayOfString1, paramArrayOfString2, paramBundle, paramInt2) == null)
        {
          Log.e("MidiManager", "registerVirtualDevice failed");
          return null;
        }
        return localMidiDeviceServer;
      }
      catch (RemoteException paramArrayOfMidiReceiver) {}
      throw paramArrayOfMidiReceiver.rethrowFromSystemServer();
    }
    catch (RemoteException paramArrayOfMidiReceiver) {}
  }
  
  public MidiDeviceInfo[] getDevices()
  {
    try
    {
      MidiDeviceInfo[] arrayOfMidiDeviceInfo = mService.getDevices();
      return arrayOfMidiDeviceInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void openBluetoothDevice(BluetoothDevice paramBluetoothDevice, final OnDeviceOpenedListener paramOnDeviceOpenedListener, final Handler paramHandler)
  {
    paramOnDeviceOpenedListener = new IMidiDeviceOpenCallback.Stub()
    {
      public void onDeviceOpened(IMidiDeviceServer paramAnonymousIMidiDeviceServer, IBinder paramAnonymousIBinder)
      {
        Object localObject1 = null;
        Object localObject2 = localObject1;
        if (paramAnonymousIMidiDeviceServer != null) {
          try
          {
            MidiDeviceInfo localMidiDeviceInfo = paramAnonymousIMidiDeviceServer.getDeviceInfo();
            localObject2 = new android/media/midi/MidiDevice;
            ((MidiDevice)localObject2).<init>(localMidiDeviceInfo, paramAnonymousIMidiDeviceServer, mService, mToken, paramAnonymousIBinder);
          }
          catch (RemoteException paramAnonymousIMidiDeviceServer)
          {
            Log.e("MidiManager", "remote exception in getDeviceInfo()");
            localObject2 = localObject1;
          }
        }
        MidiManager.this.sendOpenDeviceResponse((MidiDevice)localObject2, paramOnDeviceOpenedListener, paramHandler);
      }
    };
    try
    {
      mService.openBluetoothDevice(mToken, paramBluetoothDevice, paramOnDeviceOpenedListener);
      return;
    }
    catch (RemoteException paramBluetoothDevice)
    {
      throw paramBluetoothDevice.rethrowFromSystemServer();
    }
  }
  
  public void openDevice(final MidiDeviceInfo paramMidiDeviceInfo, final OnDeviceOpenedListener paramOnDeviceOpenedListener, final Handler paramHandler)
  {
    paramOnDeviceOpenedListener = new IMidiDeviceOpenCallback.Stub()
    {
      public void onDeviceOpened(IMidiDeviceServer paramAnonymousIMidiDeviceServer, IBinder paramAnonymousIBinder)
      {
        if (paramAnonymousIMidiDeviceServer != null) {
          paramAnonymousIMidiDeviceServer = new MidiDevice(paramMidiDeviceInfo, paramAnonymousIMidiDeviceServer, mService, mToken, paramAnonymousIBinder);
        } else {
          paramAnonymousIMidiDeviceServer = null;
        }
        MidiManager.this.sendOpenDeviceResponse(paramAnonymousIMidiDeviceServer, paramOnDeviceOpenedListener, paramHandler);
      }
    };
    try
    {
      mService.openDevice(mToken, paramMidiDeviceInfo, paramOnDeviceOpenedListener);
      return;
    }
    catch (RemoteException paramMidiDeviceInfo)
    {
      throw paramMidiDeviceInfo.rethrowFromSystemServer();
    }
  }
  
  public void registerDeviceCallback(DeviceCallback paramDeviceCallback, Handler paramHandler)
  {
    paramHandler = new DeviceListener(paramDeviceCallback, paramHandler);
    try
    {
      mService.registerListener(mToken, paramHandler);
      mDeviceListeners.put(paramDeviceCallback, paramHandler);
      return;
    }
    catch (RemoteException paramDeviceCallback)
    {
      throw paramDeviceCallback.rethrowFromSystemServer();
    }
  }
  
  public void unregisterDeviceCallback(DeviceCallback paramDeviceCallback)
  {
    paramDeviceCallback = (DeviceListener)mDeviceListeners.remove(paramDeviceCallback);
    if (paramDeviceCallback != null) {
      try
      {
        mService.unregisterListener(mToken, paramDeviceCallback);
      }
      catch (RemoteException paramDeviceCallback)
      {
        throw paramDeviceCallback.rethrowFromSystemServer();
      }
    }
  }
  
  public static class DeviceCallback
  {
    public DeviceCallback() {}
    
    public void onDeviceAdded(MidiDeviceInfo paramMidiDeviceInfo) {}
    
    public void onDeviceRemoved(MidiDeviceInfo paramMidiDeviceInfo) {}
    
    public void onDeviceStatusChanged(MidiDeviceStatus paramMidiDeviceStatus) {}
  }
  
  private class DeviceListener
    extends IMidiDeviceListener.Stub
  {
    private final MidiManager.DeviceCallback mCallback;
    private final Handler mHandler;
    
    public DeviceListener(MidiManager.DeviceCallback paramDeviceCallback, Handler paramHandler)
    {
      mCallback = paramDeviceCallback;
      mHandler = paramHandler;
    }
    
    public void onDeviceAdded(final MidiDeviceInfo paramMidiDeviceInfo)
    {
      if (mHandler != null) {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mCallback.onDeviceAdded(paramMidiDeviceInfo);
          }
        });
      } else {
        mCallback.onDeviceAdded(paramMidiDeviceInfo);
      }
    }
    
    public void onDeviceRemoved(final MidiDeviceInfo paramMidiDeviceInfo)
    {
      if (mHandler != null) {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mCallback.onDeviceRemoved(paramMidiDeviceInfo);
          }
        });
      } else {
        mCallback.onDeviceRemoved(paramMidiDeviceInfo);
      }
    }
    
    public void onDeviceStatusChanged(final MidiDeviceStatus paramMidiDeviceStatus)
    {
      if (mHandler != null) {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mCallback.onDeviceStatusChanged(paramMidiDeviceStatus);
          }
        });
      } else {
        mCallback.onDeviceStatusChanged(paramMidiDeviceStatus);
      }
    }
  }
  
  public static abstract interface OnDeviceOpenedListener
  {
    public abstract void onDeviceOpened(MidiDevice paramMidiDevice);
  }
}
