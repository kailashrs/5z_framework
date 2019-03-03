package android.service.vr;

import android.app.Vr2dDisplayProperties;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IVrManager
  extends IInterface
{
  public abstract boolean getPersistentVrModeEnabled()
    throws RemoteException;
  
  public abstract int getVr2dDisplayId()
    throws RemoteException;
  
  public abstract boolean getVrModeState()
    throws RemoteException;
  
  public abstract void registerListener(IVrStateCallbacks paramIVrStateCallbacks)
    throws RemoteException;
  
  public abstract void registerPersistentVrStateListener(IPersistentVrStateCallbacks paramIPersistentVrStateCallbacks)
    throws RemoteException;
  
  public abstract void setAndBindCompositor(String paramString)
    throws RemoteException;
  
  public abstract void setPersistentVrModeEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setStandbyEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setVr2dDisplayProperties(Vr2dDisplayProperties paramVr2dDisplayProperties)
    throws RemoteException;
  
  public abstract void setVrInputMethod(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void unregisterListener(IVrStateCallbacks paramIVrStateCallbacks)
    throws RemoteException;
  
  public abstract void unregisterPersistentVrStateListener(IPersistentVrStateCallbacks paramIPersistentVrStateCallbacks)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVrManager
  {
    private static final String DESCRIPTOR = "android.service.vr.IVrManager";
    static final int TRANSACTION_getPersistentVrModeEnabled = 6;
    static final int TRANSACTION_getVr2dDisplayId = 9;
    static final int TRANSACTION_getVrModeState = 5;
    static final int TRANSACTION_registerListener = 1;
    static final int TRANSACTION_registerPersistentVrStateListener = 3;
    static final int TRANSACTION_setAndBindCompositor = 10;
    static final int TRANSACTION_setPersistentVrModeEnabled = 7;
    static final int TRANSACTION_setStandbyEnabled = 11;
    static final int TRANSACTION_setVr2dDisplayProperties = 8;
    static final int TRANSACTION_setVrInputMethod = 12;
    static final int TRANSACTION_unregisterListener = 2;
    static final int TRANSACTION_unregisterPersistentVrStateListener = 4;
    
    public Stub()
    {
      attachInterface(this, "android.service.vr.IVrManager");
    }
    
    public static IVrManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.vr.IVrManager");
      if ((localIInterface != null) && ((localIInterface instanceof IVrManager))) {
        return (IVrManager)localIInterface;
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
        case 12: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          setVrInputMethod(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setStandbyEnabled(bool2);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          setAndBindCompositor(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          paramInt1 = getVr2dDisplayId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Vr2dDisplayProperties)Vr2dDisplayProperties.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          setVr2dDisplayProperties(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setPersistentVrModeEnabled(bool2);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          paramInt1 = getPersistentVrModeEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          paramInt1 = getVrModeState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          unregisterPersistentVrStateListener(IPersistentVrStateCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          registerPersistentVrStateListener(IPersistentVrStateCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.vr.IVrManager");
          unregisterListener(IVrStateCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.service.vr.IVrManager");
        registerListener(IVrStateCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.service.vr.IVrManager");
      return true;
    }
    
    private static class Proxy
      implements IVrManager
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
        return "android.service.vr.IVrManager";
      }
      
      public boolean getPersistentVrModeEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(6, localParcel1, localParcel2, 0);
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
      
      public int getVr2dDisplayId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public boolean getVrModeState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
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
      
      public void registerListener(IVrStateCallbacks paramIVrStateCallbacks)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          if (paramIVrStateCallbacks != null) {
            paramIVrStateCallbacks = paramIVrStateCallbacks.asBinder();
          } else {
            paramIVrStateCallbacks = null;
          }
          localParcel1.writeStrongBinder(paramIVrStateCallbacks);
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
      
      public void registerPersistentVrStateListener(IPersistentVrStateCallbacks paramIPersistentVrStateCallbacks)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          if (paramIPersistentVrStateCallbacks != null) {
            paramIPersistentVrStateCallbacks = paramIPersistentVrStateCallbacks.asBinder();
          } else {
            paramIPersistentVrStateCallbacks = null;
          }
          localParcel1.writeStrongBinder(paramIPersistentVrStateCallbacks);
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
      
      public void setAndBindCompositor(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          localParcel1.writeString(paramString);
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
      
      public void setPersistentVrModeEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void setStandbyEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
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
      
      public void setVr2dDisplayProperties(Vr2dDisplayProperties paramVr2dDisplayProperties)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          if (paramVr2dDisplayProperties != null)
          {
            localParcel1.writeInt(1);
            paramVr2dDisplayProperties.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setVrInputMethod(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void unregisterListener(IVrStateCallbacks paramIVrStateCallbacks)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          if (paramIVrStateCallbacks != null) {
            paramIVrStateCallbacks = paramIVrStateCallbacks.asBinder();
          } else {
            paramIVrStateCallbacks = null;
          }
          localParcel1.writeStrongBinder(paramIVrStateCallbacks);
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
      
      public void unregisterPersistentVrStateListener(IPersistentVrStateCallbacks paramIPersistentVrStateCallbacks)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.vr.IVrManager");
          if (paramIPersistentVrStateCallbacks != null) {
            paramIPersistentVrStateCallbacks = paramIPersistentVrStateCallbacks.asBinder();
          } else {
            paramIPersistentVrStateCallbacks = null;
          }
          localParcel1.writeStrongBinder(paramIPersistentVrStateCallbacks);
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
    }
  }
}
