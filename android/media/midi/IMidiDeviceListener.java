package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMidiDeviceListener
  extends IInterface
{
  public abstract void onDeviceAdded(MidiDeviceInfo paramMidiDeviceInfo)
    throws RemoteException;
  
  public abstract void onDeviceRemoved(MidiDeviceInfo paramMidiDeviceInfo)
    throws RemoteException;
  
  public abstract void onDeviceStatusChanged(MidiDeviceStatus paramMidiDeviceStatus)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMidiDeviceListener
  {
    private static final String DESCRIPTOR = "android.media.midi.IMidiDeviceListener";
    static final int TRANSACTION_onDeviceAdded = 1;
    static final int TRANSACTION_onDeviceRemoved = 2;
    static final int TRANSACTION_onDeviceStatusChanged = 3;
    
    public Stub()
    {
      attachInterface(this, "android.media.midi.IMidiDeviceListener");
    }
    
    public static IMidiDeviceListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.midi.IMidiDeviceListener");
      if ((localIInterface != null) && ((localIInterface instanceof IMidiDeviceListener))) {
        return (IMidiDeviceListener)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MidiDeviceStatus)MidiDeviceStatus.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onDeviceStatusChanged(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.midi.IMidiDeviceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onDeviceRemoved(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.midi.IMidiDeviceListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (MidiDeviceInfo)MidiDeviceInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject2;
        }
        onDeviceAdded(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.midi.IMidiDeviceListener");
      return true;
    }
    
    private static class Proxy
      implements IMidiDeviceListener
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
      
      public String getInterfaceDescriptor()
      {
        return "android.media.midi.IMidiDeviceListener";
      }
      
      public void onDeviceAdded(MidiDeviceInfo paramMidiDeviceInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.midi.IMidiDeviceListener");
          if (paramMidiDeviceInfo != null)
          {
            localParcel.writeInt(1);
            paramMidiDeviceInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDeviceRemoved(MidiDeviceInfo paramMidiDeviceInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.midi.IMidiDeviceListener");
          if (paramMidiDeviceInfo != null)
          {
            localParcel.writeInt(1);
            paramMidiDeviceInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDeviceStatusChanged(MidiDeviceStatus paramMidiDeviceStatus)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.midi.IMidiDeviceListener");
          if (paramMidiDeviceStatus != null)
          {
            localParcel.writeInt(1);
            paramMidiDeviceStatus.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
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
