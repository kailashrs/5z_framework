package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IOnPrimaryClipChangedListener
  extends IInterface
{
  public abstract void dispatchPrimaryClipChanged()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOnPrimaryClipChangedListener
  {
    private static final String DESCRIPTOR = "android.content.IOnPrimaryClipChangedListener";
    static final int TRANSACTION_dispatchPrimaryClipChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.IOnPrimaryClipChangedListener");
    }
    
    public static IOnPrimaryClipChangedListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.IOnPrimaryClipChangedListener");
      if ((localIInterface != null) && ((localIInterface instanceof IOnPrimaryClipChangedListener))) {
        return (IOnPrimaryClipChangedListener)localIInterface;
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
        paramParcel2.writeString("android.content.IOnPrimaryClipChangedListener");
        return true;
      }
      paramParcel1.enforceInterface("android.content.IOnPrimaryClipChangedListener");
      dispatchPrimaryClipChanged();
      return true;
    }
    
    private static class Proxy
      implements IOnPrimaryClipChangedListener
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
      
      public void dispatchPrimaryClipChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.IOnPrimaryClipChangedListener");
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
        return "android.content.IOnPrimaryClipChangedListener";
      }
    }
  }
}
