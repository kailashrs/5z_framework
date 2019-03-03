package android.accessibilityservice;

import android.graphics.Region;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public abstract interface IAccessibilityServiceClient
  extends IInterface
{
  public abstract void clearAccessibilityCache()
    throws RemoteException;
  
  public abstract void init(IAccessibilityServiceConnection paramIAccessibilityServiceConnection, int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onAccessibilityButtonAvailabilityChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onAccessibilityButtonClicked()
    throws RemoteException;
  
  public abstract void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onFingerprintCapturingGesturesChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onFingerprintGesture(int paramInt)
    throws RemoteException;
  
  public abstract void onGesture(int paramInt)
    throws RemoteException;
  
  public abstract void onInterrupt()
    throws RemoteException;
  
  public abstract void onKeyEvent(KeyEvent paramKeyEvent, int paramInt)
    throws RemoteException;
  
  public abstract void onMagnificationChanged(Region paramRegion, float paramFloat1, float paramFloat2, float paramFloat3)
    throws RemoteException;
  
  public abstract void onPerformGestureResult(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onSoftKeyboardShowModeChanged(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccessibilityServiceClient
  {
    private static final String DESCRIPTOR = "android.accessibilityservice.IAccessibilityServiceClient";
    static final int TRANSACTION_clearAccessibilityCache = 5;
    static final int TRANSACTION_init = 1;
    static final int TRANSACTION_onAccessibilityButtonAvailabilityChanged = 13;
    static final int TRANSACTION_onAccessibilityButtonClicked = 12;
    static final int TRANSACTION_onAccessibilityEvent = 2;
    static final int TRANSACTION_onFingerprintCapturingGesturesChanged = 10;
    static final int TRANSACTION_onFingerprintGesture = 11;
    static final int TRANSACTION_onGesture = 4;
    static final int TRANSACTION_onInterrupt = 3;
    static final int TRANSACTION_onKeyEvent = 6;
    static final int TRANSACTION_onMagnificationChanged = 7;
    static final int TRANSACTION_onPerformGestureResult = 9;
    static final int TRANSACTION_onSoftKeyboardShowModeChanged = 8;
    
    public Stub()
    {
      attachInterface(this, "android.accessibilityservice.IAccessibilityServiceClient");
    }
    
    public static IAccessibilityServiceClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.accessibilityservice.IAccessibilityServiceClient");
      if ((localIInterface != null) && ((localIInterface instanceof IAccessibilityServiceClient))) {
        return (IAccessibilityServiceClient)localIInterface;
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
        Object localObject2 = null;
        Object localObject3 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 13: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onAccessibilityButtonAvailabilityChanged(bool4);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          onAccessibilityButtonClicked();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          onFingerprintGesture(paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onFingerprintCapturingGesturesChanged(bool4);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          paramInt1 = paramParcel1.readInt();
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onPerformGestureResult(paramInt1, bool4);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          onSoftKeyboardShowModeChanged(paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Region)Region.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject3;
          }
          onMagnificationChanged(paramParcel2, paramParcel1.readFloat(), paramParcel1.readFloat(), paramParcel1.readFloat());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          onKeyEvent(paramParcel2, paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          clearAccessibilityCache();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          onGesture(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          onInterrupt();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AccessibilityEvent)AccessibilityEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onAccessibilityEvent(paramParcel2, bool4);
          return true;
        }
        paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceClient");
        init(IAccessibilityServiceConnection.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readStrongBinder());
        return true;
      }
      paramParcel2.writeString("android.accessibilityservice.IAccessibilityServiceClient");
      return true;
    }
    
    private static class Proxy
      implements IAccessibilityServiceClient
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
      
      public void clearAccessibilityCache()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.accessibilityservice.IAccessibilityServiceClient";
      }
      
      public void init(IAccessibilityServiceConnection paramIAccessibilityServiceConnection, int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramIAccessibilityServiceConnection != null) {
            paramIAccessibilityServiceConnection = paramIAccessibilityServiceConnection.asBinder();
          } else {
            paramIAccessibilityServiceConnection = null;
          }
          localParcel.writeStrongBinder(paramIAccessibilityServiceConnection);
          localParcel.writeInt(paramInt);
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAccessibilityButtonAvailabilityChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAccessibilityButtonClicked()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramAccessibilityEvent != null)
          {
            localParcel.writeInt(1);
            paramAccessibilityEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFingerprintCapturingGesturesChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFingerprintGesture(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGesture(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onInterrupt()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onKeyEvent(KeyEvent paramKeyEvent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramKeyEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMagnificationChanged(Region paramRegion, float paramFloat1, float paramFloat2, float paramFloat3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          if (paramRegion != null)
          {
            localParcel.writeInt(1);
            paramRegion.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeFloat(paramFloat1);
          localParcel.writeFloat(paramFloat2);
          localParcel.writeFloat(paramFloat3);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPerformGestureResult(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSoftKeyboardShowModeChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
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
