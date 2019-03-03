package android.app;

import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.IAccessibilityServiceClient.Stub;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.InputEvent;
import android.view.WindowAnimationFrameStats;
import android.view.WindowContentFrameStats;

public abstract interface IUiAutomationConnection
  extends IInterface
{
  public abstract void clearWindowAnimationFrameStats()
    throws RemoteException;
  
  public abstract boolean clearWindowContentFrameStats(int paramInt)
    throws RemoteException;
  
  public abstract void connect(IAccessibilityServiceClient paramIAccessibilityServiceClient, int paramInt)
    throws RemoteException;
  
  public abstract void disconnect()
    throws RemoteException;
  
  public abstract void executeShellCommand(String paramString, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2)
    throws RemoteException;
  
  public abstract WindowAnimationFrameStats getWindowAnimationFrameStats()
    throws RemoteException;
  
  public abstract WindowContentFrameStats getWindowContentFrameStats(int paramInt)
    throws RemoteException;
  
  public abstract void grantRuntimePermission(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract boolean injectInputEvent(InputEvent paramInputEvent, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void revokeRuntimePermission(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract boolean setRotation(int paramInt)
    throws RemoteException;
  
  public abstract void shutdown()
    throws RemoteException;
  
  public abstract Bitmap takeScreenshot(Rect paramRect, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUiAutomationConnection
  {
    private static final String DESCRIPTOR = "android.app.IUiAutomationConnection";
    static final int TRANSACTION_clearWindowAnimationFrameStats = 8;
    static final int TRANSACTION_clearWindowContentFrameStats = 6;
    static final int TRANSACTION_connect = 1;
    static final int TRANSACTION_disconnect = 2;
    static final int TRANSACTION_executeShellCommand = 10;
    static final int TRANSACTION_getWindowAnimationFrameStats = 9;
    static final int TRANSACTION_getWindowContentFrameStats = 7;
    static final int TRANSACTION_grantRuntimePermission = 11;
    static final int TRANSACTION_injectInputEvent = 3;
    static final int TRANSACTION_revokeRuntimePermission = 12;
    static final int TRANSACTION_setRotation = 4;
    static final int TRANSACTION_shutdown = 13;
    static final int TRANSACTION_takeScreenshot = 5;
    
    public Stub()
    {
      attachInterface(this, "android.app.IUiAutomationConnection");
    }
    
    public static IUiAutomationConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IUiAutomationConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IUiAutomationConnection))) {
        return (IUiAutomationConnection)localIInterface;
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
        boolean bool = false;
        Object localObject1 = null;
        String str = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 13: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          shutdown();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          revokeRuntimePermission(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          grantRuntimePermission(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          executeShellCommand(str, (ParcelFileDescriptor)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          paramParcel1 = getWindowAnimationFrameStats();
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
        case 8: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          clearWindowAnimationFrameStats();
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          paramParcel1 = getWindowContentFrameStats(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          paramInt1 = clearWindowContentFrameStats(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          }
          paramParcel1 = takeScreenshot((Rect)localObject1, paramParcel1.readInt());
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
        case 4: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          paramInt1 = setRotation(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (InputEvent)InputEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = str;
          }
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          paramInt1 = injectInputEvent((InputEvent)localObject1, bool);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
          disconnect();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.IUiAutomationConnection");
        connect(IAccessibilityServiceClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.IUiAutomationConnection");
      return true;
    }
    
    private static class Proxy
      implements IUiAutomationConnection
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
      
      public void clearWindowAnimationFrameStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean clearWindowContentFrameStats(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void connect(IAccessibilityServiceClient paramIAccessibilityServiceClient, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          if (paramIAccessibilityServiceClient != null) {
            paramIAccessibilityServiceClient = paramIAccessibilityServiceClient.asBinder();
          } else {
            paramIAccessibilityServiceClient = null;
          }
          localParcel1.writeStrongBinder(paramIAccessibilityServiceClient);
          localParcel1.writeInt(paramInt);
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
      
      public void disconnect()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
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
      
      public void executeShellCommand(String paramString, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          localParcel1.writeString(paramString);
          if (paramParcelFileDescriptor1 != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramParcelFileDescriptor2 != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
        return "android.app.IUiAutomationConnection";
      }
      
      public WindowAnimationFrameStats getWindowAnimationFrameStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WindowAnimationFrameStats localWindowAnimationFrameStats;
          if (localParcel2.readInt() != 0) {
            localWindowAnimationFrameStats = (WindowAnimationFrameStats)WindowAnimationFrameStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localWindowAnimationFrameStats = null;
          }
          return localWindowAnimationFrameStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WindowContentFrameStats getWindowContentFrameStats(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WindowContentFrameStats localWindowContentFrameStats;
          if (localParcel2.readInt() != 0) {
            localWindowContentFrameStats = (WindowContentFrameStats)WindowContentFrameStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localWindowContentFrameStats = null;
          }
          return localWindowContentFrameStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantRuntimePermission(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
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
      
      public boolean injectInputEvent(InputEvent paramInputEvent, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          boolean bool = true;
          if (paramInputEvent != null)
          {
            localParcel1.writeInt(1);
            paramInputEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void revokeRuntimePermission(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
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
      
      public boolean setRotation(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void shutdown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IUiAutomationConnection");
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public Bitmap takeScreenshot(Rect paramRect, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IUiAutomationConnection");
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramRect = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
          } else {
            paramRect = null;
          }
          return paramRect;
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
