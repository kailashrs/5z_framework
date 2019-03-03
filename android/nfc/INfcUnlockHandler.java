package android.nfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INfcUnlockHandler
  extends IInterface
{
  public abstract boolean onUnlockAttempted(Tag paramTag)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INfcUnlockHandler
  {
    private static final String DESCRIPTOR = "android.nfc.INfcUnlockHandler";
    static final int TRANSACTION_onUnlockAttempted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.nfc.INfcUnlockHandler");
    }
    
    public static INfcUnlockHandler asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.nfc.INfcUnlockHandler");
      if ((localIInterface != null) && ((localIInterface instanceof INfcUnlockHandler))) {
        return (INfcUnlockHandler)localIInterface;
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
        paramParcel2.writeString("android.nfc.INfcUnlockHandler");
        return true;
      }
      paramParcel1.enforceInterface("android.nfc.INfcUnlockHandler");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Tag)Tag.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      paramInt1 = onUnlockAttempted(paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(paramInt1);
      return true;
    }
    
    private static class Proxy
      implements INfcUnlockHandler
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
        return "android.nfc.INfcUnlockHandler";
      }
      
      public boolean onUnlockAttempted(Tag paramTag)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcUnlockHandler");
          boolean bool = true;
          if (paramTag != null)
          {
            localParcel1.writeInt(1);
            paramTag.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
