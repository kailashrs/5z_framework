package android.service.wallpaper;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWallpaperService
  extends IInterface
{
  public abstract void attach(IWallpaperConnection paramIWallpaperConnection, IBinder paramIBinder, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, Rect paramRect)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWallpaperService
  {
    private static final String DESCRIPTOR = "android.service.wallpaper.IWallpaperService";
    static final int TRANSACTION_attach = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.wallpaper.IWallpaperService");
    }
    
    public static IWallpaperService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.wallpaper.IWallpaperService");
      if ((localIInterface != null) && ((localIInterface instanceof IWallpaperService))) {
        return (IWallpaperService)localIInterface;
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
        paramParcel2.writeString("android.service.wallpaper.IWallpaperService");
        return true;
      }
      paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperService");
      IWallpaperConnection localIWallpaperConnection = IWallpaperConnection.Stub.asInterface(paramParcel1.readStrongBinder());
      paramParcel2 = paramParcel1.readStrongBinder();
      paramInt1 = paramParcel1.readInt();
      boolean bool;
      if (paramParcel1.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      paramInt2 = paramParcel1.readInt();
      int i = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {}
      for (paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = null) {
        break;
      }
      attach(localIWallpaperConnection, paramParcel2, paramInt1, bool, paramInt2, i, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IWallpaperService
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
      
      public void attach(IWallpaperConnection paramIWallpaperConnection, IBinder paramIBinder, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperService");
          if (paramIWallpaperConnection != null) {
            paramIWallpaperConnection = paramIWallpaperConnection.asBinder();
          } else {
            paramIWallpaperConnection = null;
          }
          localParcel.writeStrongBinder(paramIWallpaperConnection);
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.service.wallpaper.IWallpaperService";
      }
    }
  }
}
