package android.media;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface ISessionTokensListener
  extends IInterface
{
  public abstract void onSessionTokensChanged(List<Bundle> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISessionTokensListener
  {
    private static final String DESCRIPTOR = "android.media.ISessionTokensListener";
    static final int TRANSACTION_onSessionTokensChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.ISessionTokensListener");
    }
    
    public static ISessionTokensListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.ISessionTokensListener");
      if ((localIInterface != null) && ((localIInterface instanceof ISessionTokensListener))) {
        return (ISessionTokensListener)localIInterface;
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
        paramParcel2.writeString("android.media.ISessionTokensListener");
        return true;
      }
      paramParcel1.enforceInterface("android.media.ISessionTokensListener");
      onSessionTokensChanged(paramParcel1.createTypedArrayList(Bundle.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements ISessionTokensListener
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
        return "android.media.ISessionTokensListener";
      }
      
      public void onSessionTokensChanged(List<Bundle> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.ISessionTokensListener");
          localParcel.writeTypedList(paramList);
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
