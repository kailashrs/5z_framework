package android.security;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IConfirmationPromptCallback
  extends IInterface
{
  public abstract void onConfirmationPromptCompleted(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IConfirmationPromptCallback
  {
    private static final String DESCRIPTOR = "android.security.IConfirmationPromptCallback";
    static final int TRANSACTION_onConfirmationPromptCompleted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.security.IConfirmationPromptCallback");
    }
    
    public static IConfirmationPromptCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.security.IConfirmationPromptCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IConfirmationPromptCallback))) {
        return (IConfirmationPromptCallback)localIInterface;
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
        paramParcel2.writeString("android.security.IConfirmationPromptCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.security.IConfirmationPromptCallback");
      onConfirmationPromptCompleted(paramParcel1.readInt(), paramParcel1.createByteArray());
      return true;
    }
    
    private static class Proxy
      implements IConfirmationPromptCallback
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
        return "android.security.IConfirmationPromptCallback";
      }
      
      public void onConfirmationPromptCompleted(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.security.IConfirmationPromptCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeByteArray(paramArrayOfByte);
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
