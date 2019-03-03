package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.io.FileDescriptor;

public abstract interface IMidiDeviceServer
  extends IInterface
{
  public abstract void closeDevice()
    throws RemoteException;
  
  public abstract void closePort(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int connectPorts(IBinder paramIBinder, FileDescriptor paramFileDescriptor, int paramInt)
    throws RemoteException;
  
  public abstract MidiDeviceInfo getDeviceInfo()
    throws RemoteException;
  
  public abstract FileDescriptor openInputPort(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract FileDescriptor openOutputPort(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void setDeviceInfo(MidiDeviceInfo paramMidiDeviceInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMidiDeviceServer
  {
    private static final String DESCRIPTOR = "android.media.midi.IMidiDeviceServer";
    static final int TRANSACTION_closeDevice = 4;
    static final int TRANSACTION_closePort = 3;
    static final int TRANSACTION_connectPorts = 5;
    static final int TRANSACTION_getDeviceInfo = 6;
    static final int TRANSACTION_openInputPort = 1;
    static final int TRANSACTION_openOutputPort = 2;
    static final int TRANSACTION_setDeviceInfo = 7;
    
    public Stub()
    {
      attachInterface(this, "android.media.midi.IMidiDeviceServer");
    }
    
    public static IMidiDeviceServer asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.midi.IMidiDeviceServer");
      if ((localIInterface != null) && ((localIInterface instanceof IMidiDeviceServer))) {
        return (IMidiDeviceServer)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceServer");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          setDeviceInfo(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceServer");
          paramParcel1 = getDeviceInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceServer");
          paramInt1 = connectPorts(paramParcel1.readStrongBinder(), paramParcel1.readRawFileDescriptor(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceServer");
          closeDevice();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceServer");
          closePort(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceServer");
          paramParcel1 = openOutputPort(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeRawFileDescriptor(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.midi.IMidiDeviceServer");
        paramParcel1 = openInputPort(paramParcel1.readStrongBinder(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeRawFileDescriptor(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.midi.IMidiDeviceServer");
      return true;
    }
    
    private static class Proxy
      implements IMidiDeviceServer
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void closeDevice()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.midi.IMidiDeviceServer");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void closePort(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiDeviceServer");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int connectPorts(IBinder paramIBinder, FileDescriptor paramFileDescriptor, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiDeviceServer");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeRawFileDescriptor(paramFileDescriptor);
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public MidiDeviceInfo getDeviceInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiDeviceServer");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          MidiDeviceInfo localMidiDeviceInfo;
          if (localParcel2.readInt() != 0) {
            localMidiDeviceInfo = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localMidiDeviceInfo = null;
          }
          return localMidiDeviceInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.midi.IMidiDeviceServer";
      }
      
      public FileDescriptor openInputPort(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiDeviceServer");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = localParcel2.readRawFileDescriptor();
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public FileDescriptor openOutputPort(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.midi.IMidiDeviceServer");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = localParcel2.readRawFileDescriptor();
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDeviceInfo(MidiDeviceInfo paramMidiDeviceInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.midi.IMidiDeviceServer");
          if (paramMidiDeviceInfo != null)
          {
            localParcel.writeInt(1);
            paramMidiDeviceInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
