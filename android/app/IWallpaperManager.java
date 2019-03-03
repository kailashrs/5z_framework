package android.app;

import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWallpaperManager
  extends IInterface
{
  public abstract void clearWallpaper(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int getHeightHint()
    throws RemoteException;
  
  public abstract String getName()
    throws RemoteException;
  
  public abstract ParcelFileDescriptor getWallpaper(String paramString, IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt1, Bundle paramBundle, int paramInt2)
    throws RemoteException;
  
  public abstract WallpaperColors getWallpaperColors(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int getWallpaperIdForUser(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract WallpaperInfo getWallpaperInfo(int paramInt)
    throws RemoteException;
  
  public abstract int getWidthHint()
    throws RemoteException;
  
  public abstract boolean hasNamedWallpaper(String paramString)
    throws RemoteException;
  
  public abstract boolean isSetWallpaperAllowed(String paramString)
    throws RemoteException;
  
  public abstract boolean isWallpaperBackupEligible(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean isWallpaperSupported(String paramString)
    throws RemoteException;
  
  public abstract void registerWallpaperColorsCallback(IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt)
    throws RemoteException;
  
  public abstract void setDimensionHints(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void setDisplayPadding(Rect paramRect, String paramString)
    throws RemoteException;
  
  public abstract void setInAmbientMode(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract boolean setLockWallpaperCallback(IWallpaperManagerCallback paramIWallpaperManagerCallback)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor setWallpaper(String paramString1, String paramString2, Rect paramRect, boolean paramBoolean, Bundle paramBundle, int paramInt1, IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt2)
    throws RemoteException;
  
  public abstract void setWallpaperComponent(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void setWallpaperComponentChecked(ComponentName paramComponentName, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void settingsRestored()
    throws RemoteException;
  
  public abstract void unregisterWallpaperColorsCallback(IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWallpaperManager
  {
    private static final String DESCRIPTOR = "android.app.IWallpaperManager";
    static final int TRANSACTION_clearWallpaper = 7;
    static final int TRANSACTION_getHeightHint = 11;
    static final int TRANSACTION_getName = 13;
    static final int TRANSACTION_getWallpaper = 4;
    static final int TRANSACTION_getWallpaperColors = 19;
    static final int TRANSACTION_getWallpaperIdForUser = 5;
    static final int TRANSACTION_getWallpaperInfo = 6;
    static final int TRANSACTION_getWidthHint = 10;
    static final int TRANSACTION_hasNamedWallpaper = 8;
    static final int TRANSACTION_isSetWallpaperAllowed = 16;
    static final int TRANSACTION_isWallpaperBackupEligible = 17;
    static final int TRANSACTION_isWallpaperSupported = 15;
    static final int TRANSACTION_registerWallpaperColorsCallback = 20;
    static final int TRANSACTION_setDimensionHints = 9;
    static final int TRANSACTION_setDisplayPadding = 12;
    static final int TRANSACTION_setInAmbientMode = 22;
    static final int TRANSACTION_setLockWallpaperCallback = 18;
    static final int TRANSACTION_setWallpaper = 1;
    static final int TRANSACTION_setWallpaperComponent = 3;
    static final int TRANSACTION_setWallpaperComponentChecked = 2;
    static final int TRANSACTION_settingsRestored = 14;
    static final int TRANSACTION_unregisterWallpaperColorsCallback = 21;
    
    public Stub()
    {
      attachInterface(this, "android.app.IWallpaperManager");
    }
    
    public static IWallpaperManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IWallpaperManager");
      if ((localIInterface != null) && ((localIInterface instanceof IWallpaperManager))) {
        return (IWallpaperManager)localIInterface;
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
        Object localObject1 = null;
        Bundle localBundle = null;
        Object localObject2 = null;
        String str = null;
        boolean bool1 = false;
        boolean bool2;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 22: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setInAmbientMode(bool2, bool1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          unregisterWallpaperColorsCallback(IWallpaperManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          registerWallpaperColorsCallback(IWallpaperManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramParcel1 = getWallpaperColors(paramParcel1.readInt(), paramParcel1.readInt());
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
        case 18: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = setLockWallpaperCallback(IWallpaperManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = isWallpaperBackupEligible(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = isSetWallpaperAllowed(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = isWallpaperSupported(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          settingsRestored();
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramParcel1 = getName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = str;
          }
          setDisplayPadding((Rect)localObject2, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = getHeightHint();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = getWidthHint();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          setDimensionHints(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = hasNamedWallpaper(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          clearWallpaper(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramParcel1 = getWallpaperInfo(paramParcel1.readInt());
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
        case 5: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          paramInt1 = getWallpaperIdForUser(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          str = paramParcel1.readString();
          localObject1 = IWallpaperManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          localObject2 = new Bundle();
          paramParcel1 = getWallpaper(str, (IWallpaperManagerCallback)localObject1, paramInt1, (Bundle)localObject2, paramParcel1.readInt());
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
          paramParcel2.writeInt(1);
          ((Bundle)localObject2).writeToParcel(paramParcel2, 1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          setWallpaperComponent(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IWallpaperManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localBundle;
          }
          setWallpaperComponentChecked((ComponentName)localObject2, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.IWallpaperManager");
        localObject1 = paramParcel1.readString();
        str = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
        }
        for (;;)
        {
          break;
        }
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        localBundle = new Bundle();
        paramParcel1 = setWallpaper((String)localObject1, str, (Rect)localObject2, bool2, localBundle, paramParcel1.readInt(), IWallpaperManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
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
        paramParcel2.writeInt(1);
        localBundle.writeToParcel(paramParcel2, 1);
        return true;
      }
      paramParcel2.writeString("android.app.IWallpaperManager");
      return true;
    }
    
    private static class Proxy
      implements IWallpaperManager
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
      
      public void clearWallpaper(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getHeightHint()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IWallpaperManager";
      }
      
      public String getName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor getWallpaper(String paramString, IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt1, Bundle paramBundle, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeString(paramString);
          Object localObject = null;
          if (paramIWallpaperManagerCallback != null) {
            paramString = paramIWallpaperManagerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = localObject;
          }
          if (localParcel2.readInt() != 0) {
            paramBundle.readFromParcel(localParcel2);
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WallpaperColors getWallpaperColors(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WallpaperColors localWallpaperColors;
          if (localParcel2.readInt() != 0) {
            localWallpaperColors = (WallpaperColors)WallpaperColors.CREATOR.createFromParcel(localParcel2);
          } else {
            localWallpaperColors = null;
          }
          return localWallpaperColors;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getWallpaperIdForUser(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WallpaperInfo getWallpaperInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WallpaperInfo localWallpaperInfo;
          if (localParcel2.readInt() != 0) {
            localWallpaperInfo = (WallpaperInfo)WallpaperInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localWallpaperInfo = null;
          }
          return localWallpaperInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getWidthHint()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasNamedWallpaper(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSetWallpaperAllowed(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isWallpaperBackupEligible(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isWallpaperSupported(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerWallpaperColorsCallback(IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          if (paramIWallpaperManagerCallback != null) {
            paramIWallpaperManagerCallback = paramIWallpaperManagerCallback.asBinder();
          } else {
            paramIWallpaperManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIWallpaperManagerCallback);
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDimensionHints(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDisplayPadding(Rect paramRect, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInAmbientMode(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean setLockWallpaperCallback(IWallpaperManagerCallback paramIWallpaperManagerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          if (paramIWallpaperManagerCallback != null) {
            paramIWallpaperManagerCallback = paramIWallpaperManagerCallback.asBinder();
          } else {
            paramIWallpaperManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIWallpaperManagerCallback);
          paramIWallpaperManagerCallback = mRemote;
          boolean bool = false;
          paramIWallpaperManagerCallback.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor setWallpaper(String paramString1, String paramString2, Rect paramRect, boolean paramBoolean, Bundle paramBundle, int paramInt1, IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt1);
          paramString2 = null;
          if (paramIWallpaperManagerCallback != null) {
            paramString1 = paramIWallpaperManagerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = paramString2;
          }
          if (localParcel2.readInt() != 0) {
            paramBundle.readFromParcel(localParcel2);
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setWallpaperComponent(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setWallpaperComponentChecked(ComponentName paramComponentName, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void settingsRestored()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterWallpaperColorsCallback(IWallpaperManagerCallback paramIWallpaperManagerCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IWallpaperManager");
          if (paramIWallpaperManagerCallback != null) {
            paramIWallpaperManagerCallback = paramIWallpaperManagerCallback.asBinder();
          } else {
            paramIWallpaperManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIWallpaperManagerCallback);
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
