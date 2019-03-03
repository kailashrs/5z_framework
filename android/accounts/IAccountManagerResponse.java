package android.accounts;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAccountManagerResponse
  extends IInterface
{
  public abstract void onError(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void onResult(Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccountManagerResponse
  {
    private static final String DESCRIPTOR = "android.accounts.IAccountManagerResponse";
    static final int TRANSACTION_onError = 2;
    static final int TRANSACTION_onResult = 1;
    
    public Stub()
    {
      attachInterface(this, "android.accounts.IAccountManagerResponse");
    }
    
    public static IAccountManagerResponse asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.accounts.IAccountManagerResponse");
      if ((localIInterface != null) && ((localIInterface instanceof IAccountManagerResponse))) {
        return (IAccountManagerResponse)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.accounts.IAccountManagerResponse");
          onError(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.accounts.IAccountManagerResponse");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onResult(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.accounts.IAccountManagerResponse");
      return true;
    }
    
    private static class Proxy
      implements IAccountManagerResponse
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
        return "android.accounts.IAccountManagerResponse";
      }
      
      public void onError(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountManagerResponse");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onResult(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accounts.IAccountManagerResponse");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
