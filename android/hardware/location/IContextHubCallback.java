package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IContextHubCallback
  extends IInterface
{
  public abstract void onMessageReceipt(int paramInt1, int paramInt2, ContextHubMessage paramContextHubMessage)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IContextHubCallback
  {
    private static final String DESCRIPTOR = "android.hardware.location.IContextHubCallback";
    static final int TRANSACTION_onMessageReceipt = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IContextHubCallback");
    }
    
    public static IContextHubCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IContextHubCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IContextHubCallback))) {
        return (IContextHubCallback)localIInterface;
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
        paramParcel2.writeString("android.hardware.location.IContextHubCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.location.IContextHubCallback");
      paramInt2 = paramParcel1.readInt();
      paramInt1 = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (ContextHubMessage)ContextHubMessage.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onMessageReceipt(paramInt2, paramInt1, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IContextHubCallback
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
        return "android.hardware.location.IContextHubCallback";
      }
      
      public void onMessageReceipt(int paramInt1, int paramInt2, ContextHubMessage paramContextHubMessage)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramContextHubMessage != null)
          {
            localParcel.writeInt(1);
            paramContextHubMessage.writeToParcel(localParcel, 0);
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
    }
  }
}
