package android.service.wallpaper;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.MotionEvent;

public abstract interface IWallpaperEngine
  extends IInterface
{
  public abstract void destroy()
    throws RemoteException;
  
  public abstract void dispatchPointer(MotionEvent paramMotionEvent)
    throws RemoteException;
  
  public abstract void dispatchWallpaperCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void requestWallpaperColors()
    throws RemoteException;
  
  public abstract void setDesiredSize(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setDisplayPadding(Rect paramRect)
    throws RemoteException;
  
  public abstract void setInAmbientMode(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void setVisibility(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWallpaperEngine
  {
    private static final String DESCRIPTOR = "android.service.wallpaper.IWallpaperEngine";
    static final int TRANSACTION_destroy = 8;
    static final int TRANSACTION_dispatchPointer = 5;
    static final int TRANSACTION_dispatchWallpaperCommand = 6;
    static final int TRANSACTION_requestWallpaperColors = 7;
    static final int TRANSACTION_setDesiredSize = 1;
    static final int TRANSACTION_setDisplayPadding = 2;
    static final int TRANSACTION_setInAmbientMode = 4;
    static final int TRANSACTION_setVisibility = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.wallpaper.IWallpaperEngine");
    }
    
    public static IWallpaperEngine asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.wallpaper.IWallpaperEngine");
      if ((localIInterface != null) && ((localIInterface instanceof IWallpaperEngine))) {
        return (IWallpaperEngine)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
          destroy();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
          requestWallpaperColors();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
          paramParcel2 = paramParcel1.readString();
          int i = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          dispatchWallpaperCommand(paramParcel2, i, paramInt2, paramInt1, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (MotionEvent)MotionEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          dispatchPointer(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setInAmbientMode(bool1, bool2);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setVisibility(bool1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          setDisplayPadding(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.service.wallpaper.IWallpaperEngine");
        setDesiredSize(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.service.wallpaper.IWallpaperEngine");
      return true;
    }
    
    private static class Proxy
      implements IWallpaperEngine
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
      
      public void destroy()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchPointer(MotionEvent paramMotionEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
          if (paramMotionEvent != null)
          {
            localParcel.writeInt(1);
            paramMotionEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchWallpaperCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.wallpaper.IWallpaperEngine";
      }
      
      public void requestWallpaperColors()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setDesiredSize(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
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
      
      public void setDisplayPadding(Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setInAmbientMode(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setVisibility(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.wallpaper.IWallpaperEngine");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
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
