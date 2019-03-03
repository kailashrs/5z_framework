package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IEthernetServiceListener
  extends IInterface
{
  public abstract void onAvailabilityChanged(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IEthernetServiceListener
  {
    private static final String DESCRIPTOR = "android.net.IEthernetServiceListener";
    static final int TRANSACTION_onAvailabilityChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.IEthernetServiceListener");
    }
    
    public static IEthernetServiceListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.IEthernetServiceListener");
      if ((localIInterface != null) && ((localIInterface instanceof IEthernetServiceListener))) {
        return (IEthernetServiceListener)localIInterface;
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
        paramParcel2.writeString("android.net.IEthernetServiceListener");
        return true;
      }
      paramParcel1.enforceInterface("android.net.IEthernetServiceListener");
      paramParcel2 = paramParcel1.readString();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onAvailabilityChanged(paramParcel2, bool);
      return true;
    }
    
    private static class Proxy
      implements IEthernetServiceListener
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
        return "android.net.IEthernetServiceListener";
      }
      
      public void onAvailabilityChanged(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.IEthernetServiceListener");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
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
