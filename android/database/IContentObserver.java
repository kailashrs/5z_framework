package android.database;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IContentObserver
  extends IInterface
{
  public abstract void onChange(boolean paramBoolean, Uri paramUri, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IContentObserver
  {
    private static final String DESCRIPTOR = "android.database.IContentObserver";
    static final int TRANSACTION_onChange = 1;
    
    public Stub()
    {
      attachInterface(this, "android.database.IContentObserver");
    }
    
    public static IContentObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.database.IContentObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IContentObserver))) {
        return (IContentObserver)localIInterface;
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
        paramParcel2.writeString("android.database.IContentObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.database.IContentObserver");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      onChange(bool, paramParcel2, paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IContentObserver
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
        return "android.database.IContentObserver";
      }
      
      public void onChange(boolean paramBoolean, Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.database.IContentObserver");
          localParcel.writeInt(paramBoolean);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
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
