package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsConfigCallback
  extends IInterface
{
  public abstract void onIntConfigChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onStringConfigChanged(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsConfigCallback
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsConfigCallback";
    static final int TRANSACTION_onIntConfigChanged = 1;
    static final int TRANSACTION_onStringConfigChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsConfigCallback");
    }
    
    public static IImsConfigCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsConfigCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IImsConfigCallback))) {
        return (IImsConfigCallback)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfigCallback");
          onStringConfigChanged(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsConfigCallback");
        onIntConfigChanged(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsConfigCallback");
      return true;
    }
    
    private static class Proxy
      implements IImsConfigCallback
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
        return "android.telephony.ims.aidl.IImsConfigCallback";
      }
      
      public void onIntConfigChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsConfigCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStringConfigChanged(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsConfigCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
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
