package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IMidiDeviceOpenCallback
  extends IInterface
{
  public abstract void onDeviceOpened(IMidiDeviceServer paramIMidiDeviceServer, IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMidiDeviceOpenCallback
  {
    private static final String DESCRIPTOR = "android.media.midi.IMidiDeviceOpenCallback";
    static final int TRANSACTION_onDeviceOpened = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.midi.IMidiDeviceOpenCallback");
    }
    
    public static IMidiDeviceOpenCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.midi.IMidiDeviceOpenCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IMidiDeviceOpenCallback))) {
        return (IMidiDeviceOpenCallback)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.media.midi.IMidiDeviceOpenCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.media.midi.IMidiDeviceOpenCallback");
      onDeviceOpened(IMidiDeviceServer.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readStrongBinder());
      return true;
    }
    
    private static class Proxy
      implements IMidiDeviceOpenCallback
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
        return "android.media.midi.IMidiDeviceOpenCallback";
      }
      
      public void onDeviceOpened(IMidiDeviceServer paramIMidiDeviceServer, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.midi.IMidiDeviceOpenCallback");
          if (paramIMidiDeviceServer != null) {
            paramIMidiDeviceServer = paramIMidiDeviceServer.asBinder();
          } else {
            paramIMidiDeviceServer = null;
          }
          localParcel.writeStrongBinder(paramIMidiDeviceServer);
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel, null, 1);
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
