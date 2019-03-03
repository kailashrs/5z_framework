package android.service.dreams;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IDreamManager
  extends IInterface
{
  public abstract void awaken()
    throws RemoteException;
  
  public abstract void dream()
    throws RemoteException;
  
  public abstract void finishSelf(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ComponentName getDefaultDreamComponent()
    throws RemoteException;
  
  public abstract ComponentName[] getDreamComponents()
    throws RemoteException;
  
  public abstract boolean isDreaming()
    throws RemoteException;
  
  public abstract void setDreamComponents(ComponentName[] paramArrayOfComponentName)
    throws RemoteException;
  
  public abstract void startDozing(IBinder paramIBinder, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void stopDozing(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void testDream(ComponentName paramComponentName)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDreamManager
  {
    private static final String DESCRIPTOR = "android.service.dreams.IDreamManager";
    static final int TRANSACTION_awaken = 2;
    static final int TRANSACTION_dream = 1;
    static final int TRANSACTION_finishSelf = 8;
    static final int TRANSACTION_getDefaultDreamComponent = 5;
    static final int TRANSACTION_getDreamComponents = 4;
    static final int TRANSACTION_isDreaming = 7;
    static final int TRANSACTION_setDreamComponents = 3;
    static final int TRANSACTION_startDozing = 9;
    static final int TRANSACTION_stopDozing = 10;
    static final int TRANSACTION_testDream = 6;
    
    public Stub()
    {
      attachInterface(this, "android.service.dreams.IDreamManager");
    }
    
    public static IDreamManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.dreams.IDreamManager");
      if ((localIInterface != null) && ((localIInterface instanceof IDreamManager))) {
        return (IDreamManager)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          stopDozing(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          startDozing(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          IBinder localIBinder = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          finishSelf(localIBinder, bool);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          paramInt1 = isDreaming();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          testDream(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          paramParcel1 = getDefaultDreamComponent();
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
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          paramParcel1 = getDreamComponents();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          setDreamComponents((ComponentName[])paramParcel1.createTypedArray(ComponentName.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
          awaken();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.service.dreams.IDreamManager");
        dream();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.service.dreams.IDreamManager");
      return true;
    }
    
    private static class Proxy
      implements IDreamManager
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
      
      public void awaken()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
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
      
      public void dream()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
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
      
      public void finishSelf(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
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
      
      public ComponentName getDefaultDreamComponent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName[] getDreamComponents()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName[] arrayOfComponentName = (ComponentName[])localParcel2.createTypedArray(ComponentName.CREATOR);
          return arrayOfComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.dreams.IDreamManager";
      }
      
      public boolean isDreaming()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
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
      
      public void setDreamComponents(ComponentName[] paramArrayOfComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          localParcel1.writeTypedArray(paramArrayOfComponentName, 0);
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
      
      public void startDozing(IBinder paramIBinder, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void stopDozing(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void testDream(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.dreams.IDreamManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
