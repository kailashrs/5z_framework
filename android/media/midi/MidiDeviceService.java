package android.media.midi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public abstract class MidiDeviceService
  extends Service
{
  public static final String SERVICE_INTERFACE = "android.media.midi.MidiDeviceService";
  private static final String TAG = "MidiDeviceService";
  private final MidiDeviceServer.Callback mCallback = new MidiDeviceServer.Callback()
  {
    public void onClose()
    {
      MidiDeviceService.this.onClose();
    }
    
    public void onDeviceStatusChanged(MidiDeviceServer paramAnonymousMidiDeviceServer, MidiDeviceStatus paramAnonymousMidiDeviceStatus)
    {
      onDeviceStatusChanged(paramAnonymousMidiDeviceStatus);
    }
  };
  private MidiDeviceInfo mDeviceInfo;
  private IMidiManager mMidiManager;
  private MidiDeviceServer mServer;
  
  public MidiDeviceService() {}
  
  public final MidiDeviceInfo getDeviceInfo()
  {
    return mDeviceInfo;
  }
  
  public final MidiReceiver[] getOutputPortReceivers()
  {
    if (mServer == null) {
      return null;
    }
    return mServer.getOutputPortReceivers();
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if (("android.media.midi.MidiDeviceService".equals(paramIntent.getAction())) && (mServer != null)) {
      return mServer.getBinderInterface().asBinder();
    }
    return null;
  }
  
  public void onClose() {}
  
  public void onCreate()
  {
    mMidiManager = IMidiManager.Stub.asInterface(ServiceManager.getService("midi"));
    Object localObject2;
    try
    {
      MidiDeviceInfo localMidiDeviceInfo = mMidiManager.getServiceDeviceInfo(getPackageName(), getClass().getName());
      if (localMidiDeviceInfo == null)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Could not find MidiDeviceInfo for MidiDeviceService ");
        ((StringBuilder)localObject1).append(this);
        Log.e("MidiDeviceService", ((StringBuilder)localObject1).toString());
        return;
      }
      mDeviceInfo = localMidiDeviceInfo;
      MidiReceiver[] arrayOfMidiReceiver = onGetInputPortReceivers();
      Object localObject1 = arrayOfMidiReceiver;
      if (arrayOfMidiReceiver == null) {
        localObject1 = new MidiReceiver[0];
      }
      localObject1 = new MidiDeviceServer(mMidiManager, (MidiReceiver[])localObject1, localMidiDeviceInfo, mCallback);
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("MidiDeviceService", "RemoteException in IMidiManager.getServiceDeviceInfo");
      localObject2 = null;
    }
    mServer = localObject2;
  }
  
  public void onDeviceStatusChanged(MidiDeviceStatus paramMidiDeviceStatus) {}
  
  public abstract MidiReceiver[] onGetInputPortReceivers();
}
