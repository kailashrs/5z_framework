package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IDexModuleRegisterCallback
  extends IInterface
{
  public abstract void onDexModuleRegistered(String paramString1, boolean paramBoolean, String paramString2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDexModuleRegisterCallback
  {
    private static final String DESCRIPTOR = "android.content.pm.IDexModuleRegisterCallback";
    static final int TRANSACTION_onDexModuleRegistered = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IDexModuleRegisterCallback");
    }
    
    public static IDexModuleRegisterCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IDexModuleRegisterCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IDexModuleRegisterCallback))) {
        return (IDexModuleRegisterCallback)localIInterface;
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
        paramParcel2.writeString("android.content.pm.IDexModuleRegisterCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.content.pm.IDexModuleRegisterCallback");
      paramParcel2 = paramParcel1.readString();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onDexModuleRegistered(paramParcel2, bool, paramParcel1.readString());
      return true;
    }
    
    private static class Proxy
      implements IDexModuleRegisterCallback
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
        return "android.content.pm.IDexModuleRegisterCallback";
      }
      
      public void onDexModuleRegistered(String paramString1, boolean paramBoolean, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IDexModuleRegisterCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramBoolean);
          localParcel.writeString(paramString2);
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
