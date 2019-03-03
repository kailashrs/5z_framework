package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashSet;
import libcore.io.IoUtils;

public final class MidiDevice
  implements Closeable
{
  private static final String TAG = "MidiDevice";
  private static HashSet<MidiDevice> mMirroredDevices = new HashSet();
  private final IBinder mClientToken;
  private final MidiDeviceInfo mDeviceInfo;
  private final IMidiDeviceServer mDeviceServer;
  private final IBinder mDeviceToken;
  private final CloseGuard mGuard = CloseGuard.get();
  private boolean mIsDeviceClosed;
  private final IMidiManager mMidiManager;
  private long mNativeHandle;
  
  static
  {
    System.loadLibrary("media_jni");
  }
  
  MidiDevice(MidiDeviceInfo paramMidiDeviceInfo, IMidiDeviceServer paramIMidiDeviceServer, IMidiManager paramIMidiManager, IBinder paramIBinder1, IBinder paramIBinder2)
  {
    mDeviceInfo = paramMidiDeviceInfo;
    mDeviceServer = paramIMidiDeviceServer;
    mMidiManager = paramIMidiManager;
    mClientToken = paramIBinder1;
    mDeviceToken = paramIBinder2;
    mGuard.open("close");
  }
  
  private native long native_mirrorToNative(IBinder paramIBinder, int paramInt);
  
  private native void native_removeFromNative(long paramLong);
  
  public void close()
    throws IOException
  {
    synchronized (mGuard)
    {
      if (!mIsDeviceClosed)
      {
        removeFromNative();
        mGuard.close();
        mIsDeviceClosed = true;
        try
        {
          mMidiManager.closeDevice(mClientToken, mDeviceToken);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MidiDevice", "RemoteException in closeDevice");
        }
      }
      return;
    }
  }
  
  public MidiConnection connectPorts(MidiInputPort paramMidiInputPort, int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mDeviceInfo.getOutputPortCount()))
    {
      if (mIsDeviceClosed) {
        return null;
      }
      FileDescriptor localFileDescriptor = paramMidiInputPort.claimFileDescriptor();
      if (localFileDescriptor == null) {
        return null;
      }
      try
      {
        Binder localBinder = new android/os/Binder;
        localBinder.<init>();
        if (mDeviceServer.connectPorts(localBinder, localFileDescriptor, paramInt) != Process.myPid()) {
          IoUtils.closeQuietly(localFileDescriptor);
        }
        paramMidiInputPort = new MidiConnection(localBinder, paramMidiInputPort);
        return paramMidiInputPort;
      }
      catch (RemoteException paramMidiInputPort)
      {
        Log.e("MidiDevice", "RemoteException in connectPorts");
        return null;
      }
    }
    throw new IllegalArgumentException("outputPortNumber out of range");
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mGuard != null) {
        mGuard.warnIfOpen();
      }
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public MidiDeviceInfo getInfo()
  {
    return mDeviceInfo;
  }
  
  public long mirrorToNative()
    throws IOException
  {
    if ((!mIsDeviceClosed) && (mNativeHandle == 0L))
    {
      mNativeHandle = native_mirrorToNative(mDeviceServer.asBinder(), mDeviceInfo.getId());
      if (mNativeHandle != 0L) {
        synchronized (mMirroredDevices)
        {
          mMirroredDevices.add(this);
          return mNativeHandle;
        }
      }
      throw new IOException("Failed mirroring to native");
    }
    return 0L;
  }
  
  public MidiInputPort openInputPort(int paramInt)
  {
    if (mIsDeviceClosed) {
      return null;
    }
    try
    {
      Binder localBinder = new android/os/Binder;
      localBinder.<init>();
      Object localObject = mDeviceServer.openInputPort(localBinder, paramInt);
      if (localObject == null) {
        return null;
      }
      localObject = new MidiInputPort(mDeviceServer, localBinder, (FileDescriptor)localObject, paramInt);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("MidiDevice", "RemoteException in openInputPort");
    }
    return null;
  }
  
  public MidiOutputPort openOutputPort(int paramInt)
  {
    if (mIsDeviceClosed) {
      return null;
    }
    try
    {
      Object localObject = new android/os/Binder;
      ((Binder)localObject).<init>();
      FileDescriptor localFileDescriptor = mDeviceServer.openOutputPort((IBinder)localObject, paramInt);
      if (localFileDescriptor == null) {
        return null;
      }
      localObject = new MidiOutputPort(mDeviceServer, (IBinder)localObject, localFileDescriptor, paramInt);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("MidiDevice", "RemoteException in openOutputPort");
    }
    return null;
  }
  
  public void removeFromNative()
  {
    if (mNativeHandle == 0L) {
      return;
    }
    synchronized (mGuard)
    {
      native_removeFromNative(mNativeHandle);
      mNativeHandle = 0L;
      synchronized (mMirroredDevices)
      {
        mMirroredDevices.remove(this);
        return;
      }
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MidiDevice: ");
    localStringBuilder.append(mDeviceInfo.toString());
    return localStringBuilder.toString();
  }
  
  public class MidiConnection
    implements Closeable
  {
    private final CloseGuard mGuard = CloseGuard.get();
    private final IMidiDeviceServer mInputPortDeviceServer;
    private final IBinder mInputPortToken;
    private boolean mIsClosed;
    private final IBinder mOutputPortToken;
    
    MidiConnection(IBinder paramIBinder, MidiInputPort paramMidiInputPort)
    {
      mInputPortDeviceServer = paramMidiInputPort.getDeviceServer();
      mInputPortToken = paramMidiInputPort.getToken();
      mOutputPortToken = paramIBinder;
      mGuard.open("close");
    }
    
    public void close()
      throws IOException
    {
      synchronized (mGuard)
      {
        if (mIsClosed) {
          return;
        }
        mGuard.close();
        try
        {
          mInputPortDeviceServer.closePort(mInputPortToken);
          mDeviceServer.closePort(mOutputPortToken);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MidiDevice", "RemoteException in MidiConnection.close");
        }
        mIsClosed = true;
        return;
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if (mGuard != null) {
          mGuard.warnIfOpen();
        }
        close();
        return;
      }
      finally
      {
        super.finalize();
      }
    }
  }
}
