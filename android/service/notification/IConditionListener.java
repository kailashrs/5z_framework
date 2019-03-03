package android.service.notification;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IConditionListener
  extends IInterface
{
  public abstract void onConditionsReceived(Condition[] paramArrayOfCondition)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IConditionListener
  {
    private static final String DESCRIPTOR = "android.service.notification.IConditionListener";
    static final int TRANSACTION_onConditionsReceived = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.notification.IConditionListener");
    }
    
    public static IConditionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.notification.IConditionListener");
      if ((localIInterface != null) && ((localIInterface instanceof IConditionListener))) {
        return (IConditionListener)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.service.notification.IConditionListener");
        return true;
      }
      paramParcel1.enforceInterface("android.service.notification.IConditionListener");
      onConditionsReceived((Condition[])paramParcel1.createTypedArray(Condition.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IConditionListener
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
        return "android.service.notification.IConditionListener";
      }
      
      public void onConditionsReceived(Condition[] paramArrayOfCondition)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.IConditionListener");
          localParcel.writeTypedArray(paramArrayOfCondition, 0);
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
