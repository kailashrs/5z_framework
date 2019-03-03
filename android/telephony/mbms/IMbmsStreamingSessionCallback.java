package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IMbmsStreamingSessionCallback
  extends IInterface
{
  public abstract void onError(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void onMiddlewareReady()
    throws RemoteException;
  
  public abstract void onStreamingServicesUpdated(List<StreamingServiceInfo> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMbmsStreamingSessionCallback
  {
    private static final String DESCRIPTOR = "android.telephony.mbms.IMbmsStreamingSessionCallback";
    static final int TRANSACTION_onError = 1;
    static final int TRANSACTION_onMiddlewareReady = 3;
    static final int TRANSACTION_onStreamingServicesUpdated = 2;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.mbms.IMbmsStreamingSessionCallback");
    }
    
    public static IMbmsStreamingSessionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.mbms.IMbmsStreamingSessionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IMbmsStreamingSessionCallback))) {
        return (IMbmsStreamingSessionCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.telephony.mbms.IMbmsStreamingSessionCallback");
          onMiddlewareReady();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.mbms.IMbmsStreamingSessionCallback");
          onStreamingServicesUpdated(paramParcel1.createTypedArrayList(StreamingServiceInfo.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.mbms.IMbmsStreamingSessionCallback");
        onError(paramParcel1.readInt(), paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.telephony.mbms.IMbmsStreamingSessionCallback");
      return true;
    }
    
    private static class Proxy
      implements IMbmsStreamingSessionCallback
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
        return "android.telephony.mbms.IMbmsStreamingSessionCallback";
      }
      
      public void onError(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IMbmsStreamingSessionCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMiddlewareReady()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IMbmsStreamingSessionCallback");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStreamingServicesUpdated(List<StreamingServiceInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IMbmsStreamingSessionCallback");
          localParcel.writeTypedList(paramList);
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
