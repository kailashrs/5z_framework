package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Process;
import android.os.RemoteException;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import com.android.internal.midi.MidiDispatcher;
import com.android.internal.midi.MidiDispatcher.MidiReceiverFailureHandler;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import libcore.io.IoUtils;

public final class MidiDeviceServer
  implements Closeable
{
  private static final String TAG = "MidiDeviceServer";
  private final Callback mCallback;
  private MidiDeviceInfo mDeviceInfo;
  private final CloseGuard mGuard = CloseGuard.get();
  private final HashMap<MidiInputPort, PortClient> mInputPortClients = new HashMap();
  private final int mInputPortCount;
  private final MidiDispatcher.MidiReceiverFailureHandler mInputPortFailureHandler = new MidiDispatcher.MidiReceiverFailureHandler()
  {
    public void onReceiverFailure(MidiReceiver paramAnonymousMidiReceiver, IOException arg2)
    {
      Log.e("MidiDeviceServer", "MidiInputPort failed to send data", ???);
      synchronized (mInputPortClients)
      {
        paramAnonymousMidiReceiver = (MidiDeviceServer.PortClient)mInputPortClients.remove(paramAnonymousMidiReceiver);
        if (paramAnonymousMidiReceiver != null) {
          paramAnonymousMidiReceiver.close();
        }
        return;
      }
    }
  };
  private final boolean[] mInputPortOpen;
  private final MidiOutputPort[] mInputPortOutputPorts;
  private final MidiReceiver[] mInputPortReceivers;
  private final CopyOnWriteArrayList<MidiInputPort> mInputPorts = new CopyOnWriteArrayList();
  private boolean mIsClosed;
  private final IMidiManager mMidiManager;
  private final int mOutputPortCount;
  private MidiDispatcher[] mOutputPortDispatchers;
  private final int[] mOutputPortOpenCount;
  private final HashMap<IBinder, PortClient> mPortClients = new HashMap();
  private final IMidiDeviceServer mServer = new IMidiDeviceServer.Stub()
  {
    public void closeDevice()
    {
      if (mCallback != null) {
        mCallback.onClose();
      }
      IoUtils.closeQuietly(MidiDeviceServer.this);
    }
    
    public void closePort(IBinder paramAnonymousIBinder)
    {
      ??? = null;
      synchronized (mPortClients)
      {
        MidiDeviceServer.PortClient localPortClient = (MidiDeviceServer.PortClient)mPortClients.remove(paramAnonymousIBinder);
        paramAnonymousIBinder = ???;
        if (localPortClient != null)
        {
          paramAnonymousIBinder = localPortClient.getInputPort();
          localPortClient.close();
        }
        if (paramAnonymousIBinder != null) {
          synchronized (mInputPortClients)
          {
            mInputPortClients.remove(paramAnonymousIBinder);
          }
        }
        return;
      }
    }
    
    public int connectPorts(IBinder arg1, FileDescriptor paramAnonymousFileDescriptor, int paramAnonymousInt)
    {
      paramAnonymousFileDescriptor = new MidiInputPort(paramAnonymousFileDescriptor, paramAnonymousInt);
      synchronized (mOutputPortDispatchers[paramAnonymousInt])
      {
        ((MidiDispatcher)???).getSender().connect(paramAnonymousFileDescriptor);
        int i = ((MidiDispatcher)???).getReceiverCount();
        mOutputPortOpenCount[paramAnonymousInt] = i;
        MidiDeviceServer.this.updateDeviceStatus();
        mInputPorts.add(paramAnonymousFileDescriptor);
        ??? = new MidiDeviceServer.OutputPortClient(MidiDeviceServer.this, ???, paramAnonymousFileDescriptor);
        synchronized (mPortClients)
        {
          mPortClients.put(???, ???);
          synchronized (mInputPortClients)
          {
            mInputPortClients.put(paramAnonymousFileDescriptor, ???);
            return Process.myPid();
          }
        }
      }
    }
    
    public MidiDeviceInfo getDeviceInfo()
    {
      return mDeviceInfo;
    }
    
    public FileDescriptor openInputPort(IBinder paramAnonymousIBinder, int paramAnonymousInt)
    {
      if ((mDeviceInfo.isPrivate()) && (Binder.getCallingUid() != Process.myUid())) {
        throw new SecurityException("Can't access private device from different UID");
      }
      if ((paramAnonymousInt >= 0) && (paramAnonymousInt < mInputPortCount)) {
        synchronized (mInputPortOutputPorts)
        {
          if (mInputPortOutputPorts[paramAnonymousInt] != null)
          {
            paramAnonymousIBinder = new java/lang/StringBuilder;
            paramAnonymousIBinder.<init>();
            paramAnonymousIBinder.append("port ");
            paramAnonymousIBinder.append(paramAnonymousInt);
            paramAnonymousIBinder.append(" already open");
            Log.d("MidiDeviceServer", paramAnonymousIBinder.toString());
            return null;
          }
          try
          {
            FileDescriptor[] arrayOfFileDescriptor = MidiDeviceServer.access$800();
            ??? = new android/media/midi/MidiOutputPort;
            ((MidiOutputPort)???).<init>(arrayOfFileDescriptor[0], paramAnonymousInt);
            mInputPortOutputPorts[paramAnonymousInt] = ???;
            ((MidiOutputPort)???).connect(mInputPortReceivers[paramAnonymousInt]);
            MidiDeviceServer.InputPortClient localInputPortClient = new android/media/midi/MidiDeviceServer$InputPortClient;
            localInputPortClient.<init>(MidiDeviceServer.this, paramAnonymousIBinder, (MidiOutputPort)???);
            synchronized (mPortClients)
            {
              mPortClients.put(paramAnonymousIBinder, localInputPortClient);
              mInputPortOpen[paramAnonymousInt] = 1;
              MidiDeviceServer.this.updateDeviceStatus();
              paramAnonymousIBinder = arrayOfFileDescriptor[1];
              return paramAnonymousIBinder;
            }
            paramAnonymousIBinder = finally;
          }
          catch (IOException paramAnonymousIBinder)
          {
            Log.e("MidiDeviceServer", "unable to create FileDescriptors in openInputPort");
            return null;
          }
        }
      }
      paramAnonymousIBinder = new StringBuilder();
      paramAnonymousIBinder.append("portNumber out of range in openInputPort: ");
      paramAnonymousIBinder.append(paramAnonymousInt);
      Log.e("MidiDeviceServer", paramAnonymousIBinder.toString());
      return null;
    }
    
    public FileDescriptor openOutputPort(IBinder arg1, int paramAnonymousInt)
    {
      if ((mDeviceInfo.isPrivate()) && (Binder.getCallingUid() != Process.myUid())) {
        throw new SecurityException("Can't access private device from different UID");
      }
      if ((paramAnonymousInt >= 0) && (paramAnonymousInt < mOutputPortCount)) {
        try
        {
          FileDescriptor[] arrayOfFileDescriptor = MidiDeviceServer.access$800();
          MidiInputPort localMidiInputPort = new android/media/midi/MidiInputPort;
          localMidiInputPort.<init>(arrayOfFileDescriptor[0], paramAnonymousInt);
          if (mDeviceInfo.getType() != 2) {
            IoUtils.setBlocking(arrayOfFileDescriptor[0], false);
          }
          synchronized (mOutputPortDispatchers[paramAnonymousInt])
          {
            ((MidiDispatcher)???).getSender().connect(localMidiInputPort);
            int i = ((MidiDispatcher)???).getReceiverCount();
            mOutputPortOpenCount[paramAnonymousInt] = i;
            MidiDeviceServer.this.updateDeviceStatus();
            mInputPorts.add(localMidiInputPort);
            MidiDeviceServer.OutputPortClient localOutputPortClient = new android/media/midi/MidiDeviceServer$OutputPortClient;
            localOutputPortClient.<init>(MidiDeviceServer.this, ???, localMidiInputPort);
            synchronized (mPortClients)
            {
              mPortClients.put(???, localOutputPortClient);
              synchronized (mInputPortClients)
              {
                mInputPortClients.put(localMidiInputPort, localOutputPortClient);
                ??? = arrayOfFileDescriptor[1];
                return ???;
              }
            }
          }
          ??? = new StringBuilder();
        }
        catch (IOException ???)
        {
          Log.e("MidiDeviceServer", "unable to create FileDescriptors in openOutputPort");
          return null;
        }
      }
      ???.append("portNumber out of range in openOutputPort: ");
      ???.append(paramAnonymousInt);
      Log.e("MidiDeviceServer", ???.toString());
      return null;
    }
    
    public void setDeviceInfo(MidiDeviceInfo paramAnonymousMidiDeviceInfo)
    {
      if (Binder.getCallingUid() == 1000)
      {
        if (mDeviceInfo == null)
        {
          MidiDeviceServer.access$602(MidiDeviceServer.this, paramAnonymousMidiDeviceInfo);
          return;
        }
        throw new IllegalStateException("setDeviceInfo should only be called once");
      }
      throw new SecurityException("setDeviceInfo should only be called by MidiService");
    }
  };
  
  MidiDeviceServer(IMidiManager paramIMidiManager, MidiReceiver[] paramArrayOfMidiReceiver, int paramInt, Callback paramCallback)
  {
    mMidiManager = paramIMidiManager;
    mInputPortReceivers = paramArrayOfMidiReceiver;
    mInputPortCount = paramArrayOfMidiReceiver.length;
    mOutputPortCount = paramInt;
    mCallback = paramCallback;
    mInputPortOutputPorts = new MidiOutputPort[mInputPortCount];
    mOutputPortDispatchers = new MidiDispatcher[paramInt];
    for (int i = 0; i < paramInt; i++) {
      mOutputPortDispatchers[i] = new MidiDispatcher(mInputPortFailureHandler);
    }
    mInputPortOpen = new boolean[mInputPortCount];
    mOutputPortOpenCount = new int[paramInt];
    mGuard.open("close");
  }
  
  MidiDeviceServer(IMidiManager paramIMidiManager, MidiReceiver[] paramArrayOfMidiReceiver, MidiDeviceInfo paramMidiDeviceInfo, Callback paramCallback)
  {
    this(paramIMidiManager, paramArrayOfMidiReceiver, paramMidiDeviceInfo.getOutputPortCount(), paramCallback);
    mDeviceInfo = paramMidiDeviceInfo;
  }
  
  private static FileDescriptor[] createSeqPacketSocketPair()
    throws IOException
  {
    try
    {
      FileDescriptor localFileDescriptor1 = new java/io/FileDescriptor;
      localFileDescriptor1.<init>();
      FileDescriptor localFileDescriptor2 = new java/io/FileDescriptor;
      localFileDescriptor2.<init>();
      Os.socketpair(OsConstants.AF_UNIX, OsConstants.SOCK_SEQPACKET, 0, localFileDescriptor1, localFileDescriptor2);
      return new FileDescriptor[] { localFileDescriptor1, localFileDescriptor2 };
    }
    catch (ErrnoException localErrnoException)
    {
      throw localErrnoException.rethrowAsIOException();
    }
  }
  
  /* Error */
  private void updateDeviceStatus()
  {
    // Byte code:
    //   0: invokestatic 204	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore_1
    //   4: new 206	android/media/midi/MidiDeviceStatus
    //   7: dup
    //   8: aload_0
    //   9: getfield 137	android/media/midi/MidiDeviceServer:mDeviceInfo	Landroid/media/midi/MidiDeviceInfo;
    //   12: aload_0
    //   13: getfield 117	android/media/midi/MidiDeviceServer:mInputPortOpen	[Z
    //   16: aload_0
    //   17: getfield 119	android/media/midi/MidiDeviceServer:mOutputPortOpenCount	[I
    //   20: invokespecial 209	android/media/midi/MidiDeviceStatus:<init>	(Landroid/media/midi/MidiDeviceInfo;[Z[I)V
    //   23: astore_3
    //   24: aload_0
    //   25: getfield 104	android/media/midi/MidiDeviceServer:mCallback	Landroid/media/midi/MidiDeviceServer$Callback;
    //   28: ifnull +14 -> 42
    //   31: aload_0
    //   32: getfield 104	android/media/midi/MidiDeviceServer:mCallback	Landroid/media/midi/MidiDeviceServer$Callback;
    //   35: aload_0
    //   36: aload_3
    //   37: invokeinterface 213 3 0
    //   42: aload_0
    //   43: getfield 96	android/media/midi/MidiDeviceServer:mMidiManager	Landroid/media/midi/IMidiManager;
    //   46: aload_0
    //   47: getfield 91	android/media/midi/MidiDeviceServer:mServer	Landroid/media/midi/IMidiDeviceServer;
    //   50: aload_3
    //   51: invokeinterface 219 3 0
    //   56: lload_1
    //   57: invokestatic 223	android/os/Binder:restoreCallingIdentity	(J)V
    //   60: goto +19 -> 79
    //   63: astore_3
    //   64: goto +16 -> 80
    //   67: astore_3
    //   68: ldc 26
    //   70: ldc -31
    //   72: invokestatic 231	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   75: pop
    //   76: goto -20 -> 56
    //   79: return
    //   80: lload_1
    //   81: invokestatic 223	android/os/Binder:restoreCallingIdentity	(J)V
    //   84: aload_3
    //   85: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	86	0	this	MidiDeviceServer
    //   3	78	1	l	long
    //   23	28	3	localMidiDeviceStatus	MidiDeviceStatus
    //   63	1	3	localObject	Object
    //   67	18	3	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   42	56	63	finally
    //   68	76	63	finally
    //   42	56	67	android/os/RemoteException
  }
  
  public IBinder asBinder()
  {
    return mServer.asBinder();
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
      for (int i = 0; i < mInputPortCount; i++)
      {
        localObject1 = mInputPortOutputPorts[i];
        if (localObject1 != null)
        {
          IoUtils.closeQuietly((AutoCloseable)localObject1);
          mInputPortOutputPorts[i] = null;
        }
      }
      Object localObject1 = mInputPorts.iterator();
      while (((Iterator)localObject1).hasNext()) {
        IoUtils.closeQuietly((MidiInputPort)((Iterator)localObject1).next());
      }
      mInputPorts.clear();
      try
      {
        mMidiManager.unregisterDeviceServer(mServer);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("MidiDeviceServer", "RemoteException in unregisterDeviceServer");
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
  
  IMidiDeviceServer getBinderInterface()
  {
    return mServer;
  }
  
  public MidiReceiver[] getOutputPortReceivers()
  {
    MidiReceiver[] arrayOfMidiReceiver = new MidiReceiver[mOutputPortCount];
    System.arraycopy(mOutputPortDispatchers, 0, arrayOfMidiReceiver, 0, mOutputPortCount);
    return arrayOfMidiReceiver;
  }
  
  public static abstract interface Callback
  {
    public abstract void onClose();
    
    public abstract void onDeviceStatusChanged(MidiDeviceServer paramMidiDeviceServer, MidiDeviceStatus paramMidiDeviceStatus);
  }
  
  private class InputPortClient
    extends MidiDeviceServer.PortClient
  {
    private final MidiOutputPort mOutputPort;
    
    InputPortClient(IBinder paramIBinder, MidiOutputPort paramMidiOutputPort)
    {
      super(paramIBinder);
      mOutputPort = paramMidiOutputPort;
    }
    
    void close()
    {
      mToken.unlinkToDeath(this, 0);
      synchronized (mInputPortOutputPorts)
      {
        int i = mOutputPort.getPortNumber();
        mInputPortOutputPorts[i] = null;
        mInputPortOpen[i] = 0;
        MidiDeviceServer.this.updateDeviceStatus();
        IoUtils.closeQuietly(mOutputPort);
        return;
      }
    }
  }
  
  private class OutputPortClient
    extends MidiDeviceServer.PortClient
  {
    private final MidiInputPort mInputPort;
    
    OutputPortClient(IBinder paramIBinder, MidiInputPort paramMidiInputPort)
    {
      super(paramIBinder);
      mInputPort = paramMidiInputPort;
    }
    
    void close()
    {
      mToken.unlinkToDeath(this, 0);
      int i = mInputPort.getPortNumber();
      synchronized (mOutputPortDispatchers[i])
      {
        ???.getSender().disconnect(mInputPort);
        int j = ???.getReceiverCount();
        mOutputPortOpenCount[i] = j;
        MidiDeviceServer.this.updateDeviceStatus();
        mInputPorts.remove(mInputPort);
        IoUtils.closeQuietly(mInputPort);
        return;
      }
    }
    
    MidiInputPort getInputPort()
    {
      return mInputPort;
    }
  }
  
  private abstract class PortClient
    implements IBinder.DeathRecipient
  {
    final IBinder mToken;
    
    PortClient(IBinder paramIBinder)
    {
      mToken = paramIBinder;
      try
      {
        paramIBinder.linkToDeath(this, 0);
      }
      catch (RemoteException this$1)
      {
        close();
      }
    }
    
    public void binderDied()
    {
      close();
    }
    
    abstract void close();
    
    MidiInputPort getInputPort()
    {
      return null;
    }
  }
}
