package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWallpaperManagerCallback
  extends IInterface
{
  public abstract void onWallpaperChanged()
    throws RemoteException;
  
  public abstract void onWallpaperColorsChanged(WallpaperColors paramWallpaperColors, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWallpaperManagerCallback
  {
    private static final String DESCRIPTOR = "android.app.IWallpaperManagerCallback";
    static final int TRANSACTION_onWallpaperChanged = 1;
    static final int TRANSACTION_onWallpaperColorsChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.app.IWallpaperManagerCallback");
    }
    
    public static IWallpaperManagerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IWallpaperManagerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IWallpaperManagerCallback))) {
        return (IWallpaperManagerCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.app.IWallpaperManagerCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (WallpaperColors)WallpaperColors.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          onWallpaperColorsChanged(paramParcel2, paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.IWallpaperManagerCallback");
        onWallpaperChanged();
        return true;
      }
      paramParcel2.writeString("android.app.IWallpaperManagerCallback");
      return true;
    }
    
    private static class Proxy
      implements IWallpaperManagerCallback
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
        return "android.app.IWallpaperManagerCallback";
      }
      
      public void onWallpaperChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IWallpaperManagerCallback");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onWallpaperColorsChanged(WallpaperColors paramWallpaperColors, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IWallpaperManagerCallback");
          if (paramWallpaperColors != null)
          {
            localParcel.writeInt(1);
            paramWallpaperColors.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(2, localParcel, null, 1);
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
