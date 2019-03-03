package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IWallpaperVisibilityListener
  extends IInterface
{
  public abstract void onWallpaperVisibilityChanged(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWallpaperVisibilityListener
  {
    private static final String DESCRIPTOR = "android.view.IWallpaperVisibilityListener";
    static final int TRANSACTION_onWallpaperVisibilityChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IWallpaperVisibilityListener");
    }
    
    public static IWallpaperVisibilityListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IWallpaperVisibilityListener");
      if ((localIInterface != null) && ((localIInterface instanceof IWallpaperVisibilityListener))) {
        return (IWallpaperVisibilityListener)localIInterface;
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
        paramParcel2.writeString("android.view.IWallpaperVisibilityListener");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IWallpaperVisibilityListener");
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      onWallpaperVisibilityChanged(bool, paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IWallpaperVisibilityListener
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
        return "android.view.IWallpaperVisibilityListener";
      }
      
      public void onWallpaperVisibilityChanged(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWallpaperVisibilityListener");
          localParcel.writeInt(paramBoolean);
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
