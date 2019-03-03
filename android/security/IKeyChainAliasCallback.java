package android.security;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IKeyChainAliasCallback
  extends IInterface
{
  public abstract void alias(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyChainAliasCallback
  {
    private static final String DESCRIPTOR = "android.security.IKeyChainAliasCallback";
    static final int TRANSACTION_alias = 1;
    
    public Stub()
    {
      attachInterface(this, "android.security.IKeyChainAliasCallback");
    }
    
    public static IKeyChainAliasCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.security.IKeyChainAliasCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyChainAliasCallback))) {
        return (IKeyChainAliasCallback)localIInterface;
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
        paramParcel2.writeString("android.security.IKeyChainAliasCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.security.IKeyChainAliasCallback");
      alias(paramParcel1.readString());
      return true;
    }
    
    private static class Proxy
      implements IKeyChainAliasCallback
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void alias(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.security.IKeyChainAliasCallback");
          localParcel.writeString(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.security.IKeyChainAliasCallback";
      }
    }
  }
}
