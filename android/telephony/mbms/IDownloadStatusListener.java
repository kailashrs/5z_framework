package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IDownloadStatusListener
  extends IInterface
{
  public abstract void onStatusUpdated(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDownloadStatusListener
  {
    private static final String DESCRIPTOR = "android.telephony.mbms.IDownloadStatusListener";
    static final int TRANSACTION_onStatusUpdated = 1;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.mbms.IDownloadStatusListener");
    }
    
    public static IDownloadStatusListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.mbms.IDownloadStatusListener");
      if ((localIInterface != null) && ((localIInterface instanceof IDownloadStatusListener))) {
        return (IDownloadStatusListener)localIInterface;
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
        paramParcel2.writeString("android.telephony.mbms.IDownloadStatusListener");
        return true;
      }
      paramParcel1.enforceInterface("android.telephony.mbms.IDownloadStatusListener");
      paramInt1 = paramParcel1.readInt();
      FileInfo localFileInfo = null;
      DownloadRequest localDownloadRequest;
      if (paramInt1 != 0) {
        localDownloadRequest = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
      } else {
        localDownloadRequest = null;
      }
      if (paramParcel1.readInt() != 0) {
        localFileInfo = (FileInfo)FileInfo.CREATOR.createFromParcel(paramParcel1);
      }
      onStatusUpdated(localDownloadRequest, localFileInfo, paramParcel1.readInt());
      paramParcel2.writeNoException();
      return true;
    }
    
    private static class Proxy
      implements IDownloadStatusListener
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
        return "android.telephony.mbms.IDownloadStatusListener";
      }
      
      public void onStatusUpdated(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.IDownloadStatusListener");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramFileInfo != null)
          {
            localParcel1.writeInt(1);
            paramFileInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
    }
  }
}
