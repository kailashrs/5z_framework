package android.hardware.input;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITabletModeChangedListener
  extends IInterface
{
  public abstract void onTabletModeChanged(long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITabletModeChangedListener
  {
    private static final String DESCRIPTOR = "android.hardware.input.ITabletModeChangedListener";
    static final int TRANSACTION_onTabletModeChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.input.ITabletModeChangedListener");
    }
    
    public static ITabletModeChangedListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.input.ITabletModeChangedListener");
      if ((localIInterface != null) && ((localIInterface instanceof ITabletModeChangedListener))) {
        return (ITabletModeChangedListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.input.ITabletModeChangedListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.input.ITabletModeChangedListener");
      long l = paramParcel1.readLong();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onTabletModeChanged(l, bool);
      return true;
    }
    
    private static class Proxy
      implements ITabletModeChangedListener
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
        return "android.hardware.input.ITabletModeChangedListener";
      }
      
      public void onTabletModeChanged(long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.input.ITabletModeChangedListener");
          localParcel.writeLong(paramLong);
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
