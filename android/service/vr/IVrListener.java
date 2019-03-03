package android.service.vr;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IVrListener
  extends IInterface
{
  public abstract void focusedActivityChanged(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVrListener
  {
    private static final String DESCRIPTOR = "android.service.vr.IVrListener";
    static final int TRANSACTION_focusedActivityChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.vr.IVrListener");
    }
    
    public static IVrListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.vr.IVrListener");
      if ((localIInterface != null) && ((localIInterface instanceof IVrListener))) {
        return (IVrListener)localIInterface;
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
        paramParcel2.writeString("android.service.vr.IVrListener");
        return true;
      }
      paramParcel1.enforceInterface("android.service.vr.IVrListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      focusedActivityChanged(paramParcel2, bool, paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IVrListener
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
      
      public void focusedActivityChanged(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.vr.IVrListener");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.vr.IVrListener";
      }
    }
  }
}
