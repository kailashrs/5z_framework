package android.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.IAccessibilityServiceClient.Stub;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.IWindow;
import android.view.IWindow.Stub;
import java.util.ArrayList;
import java.util.List;

public abstract interface IAccessibilityManager
  extends IInterface
{
  public abstract int addAccessibilityInteractionConnection(IWindow paramIWindow, IAccessibilityInteractionConnection paramIAccessibilityInteractionConnection, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract long addClient(IAccessibilityManagerClient paramIAccessibilityManagerClient, int paramInt)
    throws RemoteException;
  
  public abstract List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int paramInt)
    throws RemoteException;
  
  public abstract IBinder getWindowToken(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void interrupt(int paramInt)
    throws RemoteException;
  
  public abstract void notifyAccessibilityButtonClicked()
    throws RemoteException;
  
  public abstract void notifyAccessibilityButtonVisibilityChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void performAccessibilityShortcut()
    throws RemoteException;
  
  public abstract void registerUiTestAutomationService(IBinder paramIBinder, IAccessibilityServiceClient paramIAccessibilityServiceClient, AccessibilityServiceInfo paramAccessibilityServiceInfo, int paramInt)
    throws RemoteException;
  
  public abstract void removeAccessibilityInteractionConnection(IWindow paramIWindow)
    throws RemoteException;
  
  public abstract void sendAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent, int paramInt)
    throws RemoteException;
  
  public abstract boolean sendFingerprintGesture(int paramInt)
    throws RemoteException;
  
  public abstract void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection paramIAccessibilityInteractionConnection)
    throws RemoteException;
  
  public abstract void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void unregisterUiTestAutomationService(IAccessibilityServiceClient paramIAccessibilityServiceClient)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccessibilityManager
  {
    private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityManager";
    static final int TRANSACTION_addAccessibilityInteractionConnection = 6;
    static final int TRANSACTION_addClient = 3;
    static final int TRANSACTION_getEnabledAccessibilityServiceList = 5;
    static final int TRANSACTION_getInstalledAccessibilityServiceList = 4;
    static final int TRANSACTION_getWindowToken = 12;
    static final int TRANSACTION_interrupt = 1;
    static final int TRANSACTION_notifyAccessibilityButtonClicked = 13;
    static final int TRANSACTION_notifyAccessibilityButtonVisibilityChanged = 14;
    static final int TRANSACTION_performAccessibilityShortcut = 15;
    static final int TRANSACTION_registerUiTestAutomationService = 9;
    static final int TRANSACTION_removeAccessibilityInteractionConnection = 7;
    static final int TRANSACTION_sendAccessibilityEvent = 2;
    static final int TRANSACTION_sendFingerprintGesture = 16;
    static final int TRANSACTION_setPictureInPictureActionReplacingConnection = 8;
    static final int TRANSACTION_temporaryEnableAccessibilityStateUntilKeyguardRemoved = 11;
    static final int TRANSACTION_unregisterUiTestAutomationService = 10;
    
    public Stub()
    {
      attachInterface(this, "android.view.accessibility.IAccessibilityManager");
    }
    
    public static IAccessibilityManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.accessibility.IAccessibilityManager");
      if ((localIInterface != null) && ((localIInterface instanceof IAccessibilityManager))) {
        return (IAccessibilityManager)localIInterface;
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
        Object localObject = null;
        IAccessibilityServiceClient localIAccessibilityServiceClient = null;
        IBinder localIBinder = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 16: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          paramInt1 = sendFingerprintGesture(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          performAccessibilityShortcut();
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          notifyAccessibilityButtonVisibilityChanged(bool2);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          notifyAccessibilityButtonClicked();
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          paramParcel1 = getWindowToken(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          if (paramParcel1.readInt() != 0) {
            localObject = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = localIBinder;
          }
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          temporaryEnableAccessibilityStateUntilKeyguardRemoved((ComponentName)localObject, bool2);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          unregisterUiTestAutomationService(IAccessibilityServiceClient.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          localIBinder = paramParcel1.readStrongBinder();
          localIAccessibilityServiceClient = IAccessibilityServiceClient.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject = (AccessibilityServiceInfo)AccessibilityServiceInfo.CREATOR.createFromParcel(paramParcel1);
          }
          registerUiTestAutomationService(localIBinder, localIAccessibilityServiceClient, (AccessibilityServiceInfo)localObject, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          removeAccessibilityInteractionConnection(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          paramInt1 = addAccessibilityInteractionConnection(IWindow.Stub.asInterface(paramParcel1.readStrongBinder()), IAccessibilityInteractionConnection.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          paramParcel1 = getEnabledAccessibilityServiceList(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          paramParcel1 = getInstalledAccessibilityServiceList(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          long l = addClient(IAccessibilityManagerClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AccessibilityEvent)AccessibilityEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localIAccessibilityServiceClient;
          }
          sendAccessibilityEvent(paramParcel2, paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManager");
        interrupt(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.view.accessibility.IAccessibilityManager");
      return true;
    }
    
    private static class Proxy
      implements IAccessibilityManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int addAccessibilityInteractionConnection(IWindow paramIWindow, IAccessibilityInteractionConnection paramIAccessibilityInteractionConnection, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          Object localObject = null;
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
          paramIWindow = localObject;
          if (paramIAccessibilityInteractionConnection != null) {
            paramIWindow = paramIAccessibilityInteractionConnection.asBinder();
          }
          localParcel1.writeStrongBinder(paramIWindow);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long addClient(IAccessibilityManagerClient paramIAccessibilityManagerClient, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          if (paramIAccessibilityManagerClient != null) {
            paramIAccessibilityManagerClient = paramIAccessibilityManagerClient.asBinder();
          } else {
            paramIAccessibilityManagerClient = null;
          }
          localParcel1.writeStrongBinder(paramIAccessibilityManagerClient);
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.accessibility.IAccessibilityManager";
      }
      
      public IBinder getWindowToken(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IBinder localIBinder = localParcel2.readStrongBinder();
          return localIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void interrupt(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyAccessibilityButtonClicked()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyAccessibilityButtonVisibilityChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void performAccessibilityShortcut()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerUiTestAutomationService(IBinder paramIBinder, IAccessibilityServiceClient paramIAccessibilityServiceClient, AccessibilityServiceInfo paramAccessibilityServiceInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIAccessibilityServiceClient != null) {
            paramIBinder = paramIAccessibilityServiceClient.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramAccessibilityServiceInfo != null)
          {
            localParcel1.writeInt(1);
            paramAccessibilityServiceInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public void removeAccessibilityInteractionConnection(IWindow paramIWindow)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          if (paramIWindow != null) {
            paramIWindow = paramIWindow.asBinder();
          } else {
            paramIWindow = null;
          }
          localParcel1.writeStrongBinder(paramIWindow);
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
      
      public void sendAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          if (paramAccessibilityEvent != null)
          {
            localParcel.writeInt(1);
            paramAccessibilityEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean sendFingerprintGesture(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(16, localParcel1, localParcel2, 0);
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
      
      public void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection paramIAccessibilityInteractionConnection)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          if (paramIAccessibilityInteractionConnection != null) {
            paramIAccessibilityInteractionConnection = paramIAccessibilityInteractionConnection.asBinder();
          } else {
            paramIAccessibilityInteractionConnection = null;
          }
          localParcel1.writeStrongBinder(paramIAccessibilityInteractionConnection);
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
      
      public void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void unregisterUiTestAutomationService(IAccessibilityServiceClient paramIAccessibilityServiceClient)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.accessibility.IAccessibilityManager");
          if (paramIAccessibilityServiceClient != null) {
            paramIAccessibilityServiceClient = paramIAccessibilityServiceClient.asBinder();
          } else {
            paramIAccessibilityServiceClient = null;
          }
          localParcel1.writeStrongBinder(paramIAccessibilityServiceClient);
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
    }
  }
}
