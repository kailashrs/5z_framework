package android.webkit;

import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IWebViewUpdateService
  extends IInterface
{
  public abstract String changeProviderAndSetting(String paramString)
    throws RemoteException;
  
  public abstract void enableFallbackLogic(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void enableMultiProcess(boolean paramBoolean)
    throws RemoteException;
  
  public abstract WebViewProviderInfo[] getAllWebViewPackages()
    throws RemoteException;
  
  public abstract PackageInfo getCurrentWebViewPackage()
    throws RemoteException;
  
  public abstract String getCurrentWebViewPackageName()
    throws RemoteException;
  
  public abstract WebViewProviderInfo[] getValidWebViewPackages()
    throws RemoteException;
  
  public abstract boolean isFallbackPackage(String paramString)
    throws RemoteException;
  
  public abstract boolean isMultiProcessEnabled()
    throws RemoteException;
  
  public abstract void notifyRelroCreationCompleted()
    throws RemoteException;
  
  public abstract WebViewProviderResponse waitForAndGetProvider()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWebViewUpdateService
  {
    private static final String DESCRIPTOR = "android.webkit.IWebViewUpdateService";
    static final int TRANSACTION_changeProviderAndSetting = 3;
    static final int TRANSACTION_enableFallbackLogic = 9;
    static final int TRANSACTION_enableMultiProcess = 11;
    static final int TRANSACTION_getAllWebViewPackages = 5;
    static final int TRANSACTION_getCurrentWebViewPackage = 7;
    static final int TRANSACTION_getCurrentWebViewPackageName = 6;
    static final int TRANSACTION_getValidWebViewPackages = 4;
    static final int TRANSACTION_isFallbackPackage = 8;
    static final int TRANSACTION_isMultiProcessEnabled = 10;
    static final int TRANSACTION_notifyRelroCreationCompleted = 1;
    static final int TRANSACTION_waitForAndGetProvider = 2;
    
    public Stub()
    {
      attachInterface(this, "android.webkit.IWebViewUpdateService");
    }
    
    public static IWebViewUpdateService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.webkit.IWebViewUpdateService");
      if ((localIInterface != null) && ((localIInterface instanceof IWebViewUpdateService))) {
        return (IWebViewUpdateService)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          enableMultiProcess(bool2);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramInt1 = isMultiProcessEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          enableFallbackLogic(bool2);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramInt1 = isFallbackPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramParcel1 = getCurrentWebViewPackage();
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
        case 6: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramParcel1 = getCurrentWebViewPackageName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramParcel1 = getAllWebViewPackages();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramParcel1 = getValidWebViewPackages();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramParcel1 = changeProviderAndSetting(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
          paramParcel1 = waitForAndGetProvider();
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
        }
        paramParcel1.enforceInterface("android.webkit.IWebViewUpdateService");
        notifyRelroCreationCompleted();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.webkit.IWebViewUpdateService");
      return true;
    }
    
    private static class Proxy
      implements IWebViewUpdateService
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
      
      public String changeProviderAndSetting(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableFallbackLogic(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          localParcel1.writeInt(paramBoolean);
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
      
      public void enableMultiProcess(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WebViewProviderInfo[] getAllWebViewPackages()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WebViewProviderInfo[] arrayOfWebViewProviderInfo = (WebViewProviderInfo[])localParcel2.createTypedArray(WebViewProviderInfo.CREATOR);
          return arrayOfWebViewProviderInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PackageInfo getCurrentWebViewPackage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PackageInfo localPackageInfo;
          if (localParcel2.readInt() != 0) {
            localPackageInfo = (PackageInfo)PackageInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localPackageInfo = null;
          }
          return localPackageInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCurrentWebViewPackageName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.webkit.IWebViewUpdateService";
      }
      
      public WebViewProviderInfo[] getValidWebViewPackages()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WebViewProviderInfo[] arrayOfWebViewProviderInfo = (WebViewProviderInfo[])localParcel2.createTypedArray(WebViewProviderInfo.CREATOR);
          return arrayOfWebViewProviderInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isFallbackPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
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
      
      public boolean isMultiProcessEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
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
      
      public void notifyRelroCreationCompleted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
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
      
      public WebViewProviderResponse waitForAndGetProvider()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.webkit.IWebViewUpdateService");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WebViewProviderResponse localWebViewProviderResponse;
          if (localParcel2.readInt() != 0) {
            localWebViewProviderResponse = (WebViewProviderResponse)WebViewProviderResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            localWebViewProviderResponse = null;
          }
          return localWebViewProviderResponse;
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
