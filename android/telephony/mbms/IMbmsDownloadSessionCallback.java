package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IMbmsDownloadSessionCallback
  extends IInterface
{
  public abstract void onError(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void onFileServicesUpdated(List<FileServiceInfo> paramList)
    throws RemoteException;
  
  public abstract void onMiddlewareReady()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMbmsDownloadSessionCallback
  {
    private static final String DESCRIPTOR = "android.telephony.mbms.IMbmsDownloadSessionCallback";
    static final int TRANSACTION_onError = 1;
    static final int TRANSACTION_onFileServicesUpdated = 2;
    static final int TRANSACTION_onMiddlewareReady = 3;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.mbms.IMbmsDownloadSessionCallback");
    }
    
    public static IMbmsDownloadSessionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.mbms.IMbmsDownloadSessionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IMbmsDownloadSessionCallback))) {
        return (IMbmsDownloadSessionCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.telephony.mbms.IMbmsDownloadSessionCallback");
          onMiddlewareReady();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.mbms.IMbmsDownloadSessionCallback");
          onFileServicesUpdated(paramParcel1.createTypedArrayList(FileServiceInfo.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.mbms.IMbmsDownloadSessionCallback");
        onError(paramParcel1.readInt(), paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.telephony.mbms.IMbmsDownloadSessionCallback");
      return true;
    }
    
    private static class Proxy
      implements IMbmsDownloadSessionCallback
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
        return "android.telephony.mbms.IMbmsDownloadSessionCallback";
      }
      
      public void onError(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IMbmsDownloadSessionCallback");
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
      
      public void onFileServicesUpdated(List<FileServiceInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IMbmsDownloadSessionCallback");
          localParcel.writeTypedList(paramList);
          mRemote.transact(2, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("android.telephony.mbms.IMbmsDownloadSessionCallback");
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
