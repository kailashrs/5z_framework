package android.view.accessibility;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAccessibilityManagerClient
  extends IInterface
{
  public abstract void notifyServicesStateChanged()
    throws RemoteException;
  
  public abstract void setRelevantEventTypes(int paramInt)
    throws RemoteException;
  
  public abstract void setState(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccessibilityManagerClient
  {
    private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityManagerClient";
    static final int TRANSACTION_notifyServicesStateChanged = 2;
    static final int TRANSACTION_setRelevantEventTypes = 3;
    static final int TRANSACTION_setState = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.accessibility.IAccessibilityManagerClient");
    }
    
    public static IAccessibilityManagerClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.accessibility.IAccessibilityManagerClient");
      if ((localIInterface != null) && ((localIInterface instanceof IAccessibilityManagerClient))) {
        return (IAccessibilityManagerClient)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManagerClient");
          setRelevantEventTypes(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManagerClient");
          notifyServicesStateChanged();
          return true;
        }
        paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityManagerClient");
        setState(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.view.accessibility.IAccessibilityManagerClient");
      return true;
    }
    
    private static class Proxy
      implements IAccessibilityManagerClient
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
        return "android.view.accessibility.IAccessibilityManagerClient";
      }
      
      public void notifyServicesStateChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityManagerClient");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setRelevantEventTypes(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityManagerClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityManagerClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
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
