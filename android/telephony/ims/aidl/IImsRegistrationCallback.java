package android.telephony.ims.aidl;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsReasonInfo;

public abstract interface IImsRegistrationCallback
  extends IInterface
{
  public abstract void onDeregistered(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void onRegistered(int paramInt)
    throws RemoteException;
  
  public abstract void onRegistering(int paramInt)
    throws RemoteException;
  
  public abstract void onSubscriberAssociatedUriChanged(Uri[] paramArrayOfUri)
    throws RemoteException;
  
  public abstract void onTechnologyChangeFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsRegistrationCallback
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRegistrationCallback";
    static final int TRANSACTION_onDeregistered = 3;
    static final int TRANSACTION_onRegistered = 1;
    static final int TRANSACTION_onRegistering = 2;
    static final int TRANSACTION_onSubscriberAssociatedUriChanged = 5;
    static final int TRANSACTION_onTechnologyChangeFailed = 4;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsRegistrationCallback");
    }
    
    public static IImsRegistrationCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsRegistrationCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IImsRegistrationCallback))) {
        return (IImsRegistrationCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistrationCallback");
          onSubscriberAssociatedUriChanged((Uri[])paramParcel1.createTypedArray(Uri.CREATOR));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistrationCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onTechnologyChangeFailed(paramInt1, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistrationCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onDeregistered(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistrationCallback");
          onRegistering(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsRegistrationCallback");
        onRegistered(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsRegistrationCallback");
      return true;
    }
    
    private static class Proxy
      implements IImsRegistrationCallback
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
        return "android.telephony.ims.aidl.IImsRegistrationCallback";
      }
      
      public void onDeregistered(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistrationCallback");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRegistered(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistrationCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRegistering(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistrationCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSubscriberAssociatedUriChanged(Uri[] paramArrayOfUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistrationCallback");
          localParcel.writeTypedArray(paramArrayOfUri, 0);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTechnologyChangeFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsRegistrationCallback");
          localParcel.writeInt(paramInt);
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
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
