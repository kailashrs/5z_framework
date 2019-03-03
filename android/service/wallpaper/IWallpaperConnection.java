package android.service.wallpaper;

import android.app.WallpaperColors;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWallpaperConnection
  extends IInterface
{
  public abstract void attachEngine(IWallpaperEngine paramIWallpaperEngine)
    throws RemoteException;
  
  public abstract void engineShown(IWallpaperEngine paramIWallpaperEngine)
    throws RemoteException;
  
  public abstract void onWallpaperColorsChanged(WallpaperColors paramWallpaperColors)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor setWallpaper(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWallpaperConnection
  {
    private static final String DESCRIPTOR = "android.service.wallpaper.IWallpaperConnection";
    static final int TRANSACTION_attachEngine = 1;
    static final int TRANSACTION_engineShown = 2;
    static final int TRANSACTION_onWallpaperColorsChanged = 4;
    static final int TRANSACTION_setWallpaper = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.wallpaper.IWallpaperConnection");
    }
    
    public static IWallpaperConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.wallpaper.IWallpaperConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IWallpaperConnection))) {
        return (IWallpaperConnection)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperConnection");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WallpaperColors)WallpaperColors.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onWallpaperColorsChanged(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperConnection");
          paramParcel1 = setWallpaper(paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperConnection");
          engineShown(IWallpaperEngine.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperConnection");
        attachEngine(IWallpaperEngine.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.service.wallpaper.IWallpaperConnection");
      return true;
    }
    
    private static class Proxy
      implements IWallpaperConnection
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
      
      public void attachEngine(IWallpaperEngine paramIWallpaperEngine)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.wallpaper.IWallpaperConnection");
          if (paramIWallpaperEngine != null) {
            paramIWallpaperEngine = paramIWallpaperEngine.asBinder();
          } else {
            paramIWallpaperEngine = null;
          }
          localParcel1.writeStrongBinder(paramIWallpaperEngine);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void engineShown(IWallpaperEngine paramIWallpaperEngine)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.wallpaper.IWallpaperConnection");
          if (paramIWallpaperEngine != null) {
            paramIWallpaperEngine = paramIWallpaperEngine.asBinder();
          } else {
            paramIWallpaperEngine = null;
          }
          localParcel1.writeStrongBinder(paramIWallpaperEngine);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.wallpaper.IWallpaperConnection";
      }
      
      public void onWallpaperColorsChanged(WallpaperColors paramWallpaperColors)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.wallpaper.IWallpaperConnection");
          if (paramWallpaperColors != null)
          {
            localParcel1.writeInt(1);
            paramWallpaperColors.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor setWallpaper(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.wallpaper.IWallpaperConnection");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
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
