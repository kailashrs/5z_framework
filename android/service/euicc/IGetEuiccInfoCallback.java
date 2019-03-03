package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.euicc.EuiccInfo;

public abstract interface IGetEuiccInfoCallback
  extends IInterface
{
  public abstract void onSuccess(EuiccInfo paramEuiccInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGetEuiccInfoCallback
  {
    private static final String DESCRIPTOR = "android.service.euicc.IGetEuiccInfoCallback";
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.euicc.IGetEuiccInfoCallback");
    }
    
    public static IGetEuiccInfoCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.euicc.IGetEuiccInfoCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGetEuiccInfoCallback))) {
        return (IGetEuiccInfoCallback)localIInterface;
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
        paramParcel2.writeString("android.service.euicc.IGetEuiccInfoCallback");
        return true;
      }
      paramParcel1.enforceInterface("android.service.euicc.IGetEuiccInfoCallback");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (EuiccInfo)EuiccInfo.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onSuccess(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IGetEuiccInfoCallback
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
        return "android.service.euicc.IGetEuiccInfoCallback";
      }
      
      public void onSuccess(EuiccInfo paramEuiccInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IGetEuiccInfoCallback");
          if (paramEuiccInfo != null)
          {
            localParcel.writeInt(1);
            paramEuiccInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
