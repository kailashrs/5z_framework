package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSession.Stub;

public abstract interface IImsMmTelListener
  extends IInterface
{
  public abstract void onIncomingCall(IImsCallSession paramIImsCallSession, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onRejectedCall(ImsCallProfile paramImsCallProfile, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void onVoiceMessageCountUpdate(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsMmTelListener
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsMmTelListener";
    static final int TRANSACTION_onIncomingCall = 1;
    static final int TRANSACTION_onRejectedCall = 2;
    static final int TRANSACTION_onVoiceMessageCountUpdate = 3;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsMmTelListener");
    }
    
    public static IImsMmTelListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsMmTelListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsMmTelListener))) {
        return (IImsMmTelListener)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelListener");
          onVoiceMessageCountUpdate(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onRejectedCall(paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsMmTelListener");
        paramParcel2 = IImsCallSession.Stub.asInterface(paramParcel1.readStrongBinder());
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onIncomingCall(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsMmTelListener");
      return true;
    }
    
    private static class Proxy
      implements IImsMmTelListener
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
        return "android.telephony.ims.aidl.IImsMmTelListener";
      }
      
      public void onIncomingCall(IImsCallSession paramIImsCallSession, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelListener");
          if (paramIImsCallSession != null) {
            paramIImsCallSession = paramIImsCallSession.asBinder();
          } else {
            paramIImsCallSession = null;
          }
          localParcel.writeStrongBinder(paramIImsCallSession);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void onRejectedCall(ImsCallProfile paramImsCallProfile, ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelListener");
          if (paramImsCallProfile != null)
          {
            localParcel.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVoiceMessageCountUpdate(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsMmTelListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
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
