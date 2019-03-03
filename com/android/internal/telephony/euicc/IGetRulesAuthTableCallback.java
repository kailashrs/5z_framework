package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.euicc.EuiccRulesAuthTable;

public abstract interface IGetRulesAuthTableCallback
  extends IInterface
{
  public abstract void onComplete(int paramInt, EuiccRulesAuthTable paramEuiccRulesAuthTable)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGetRulesAuthTableCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IGetRulesAuthTableCallback";
    static final int TRANSACTION_onComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.euicc.IGetRulesAuthTableCallback");
    }
    
    public static IGetRulesAuthTableCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.euicc.IGetRulesAuthTableCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IGetRulesAuthTableCallback))) {
        return (IGetRulesAuthTableCallback)localIInterface;
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
        paramParcel2.writeString("com.android.internal.telephony.euicc.IGetRulesAuthTableCallback");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IGetRulesAuthTableCallback");
      paramInt1 = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (EuiccRulesAuthTable)EuiccRulesAuthTable.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onComplete(paramInt1, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IGetRulesAuthTableCallback
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
        return "com.android.internal.telephony.euicc.IGetRulesAuthTableCallback";
      }
      
      public void onComplete(int paramInt, EuiccRulesAuthTable paramEuiccRulesAuthTable)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IGetRulesAuthTableCallback");
          localParcel.writeInt(paramInt);
          if (paramEuiccRulesAuthTable != null)
          {
            localParcel.writeInt(1);
            paramEuiccRulesAuthTable.writeToParcel(localParcel, 0);
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
