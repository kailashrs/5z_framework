package android.os.storage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IObbActionListener
  extends IInterface
{
  public abstract void onObbResult(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IObbActionListener
  {
    private static final String DESCRIPTOR = "android.os.storage.IObbActionListener";
    static final int TRANSACTION_onObbResult = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.storage.IObbActionListener");
    }
    
    public static IObbActionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.storage.IObbActionListener");
      if ((localIInterface != null) && ((localIInterface instanceof IObbActionListener))) {
        return (IObbActionListener)localIInterface;
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
        paramParcel2.writeString("android.os.storage.IObbActionListener");
        return true;
      }
      paramParcel1.enforceInterface("android.os.storage.IObbActionListener");
      onObbResult(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IObbActionListener
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
        return "android.os.storage.IObbActionListener";
      }
      
      public void onObbResult(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IObbActionListener");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
