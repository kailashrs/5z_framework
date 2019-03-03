package android.telephony.mbms.vendor;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.mbms.DownloadRequest;
import android.telephony.mbms.FileInfo;
import android.telephony.mbms.IDownloadProgressListener;
import android.telephony.mbms.IDownloadProgressListener.Stub;
import android.telephony.mbms.IDownloadStatusListener;
import android.telephony.mbms.IDownloadStatusListener.Stub;
import android.telephony.mbms.IMbmsDownloadSessionCallback;
import android.telephony.mbms.IMbmsDownloadSessionCallback.Stub;
import java.util.ArrayList;
import java.util.List;

public abstract interface IMbmsDownloadService
  extends IInterface
{
  public abstract int addProgressListener(DownloadRequest paramDownloadRequest, IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException;
  
  public abstract int addStatusListener(DownloadRequest paramDownloadRequest, IDownloadStatusListener paramIDownloadStatusListener)
    throws RemoteException;
  
  public abstract int cancelDownload(DownloadRequest paramDownloadRequest)
    throws RemoteException;
  
  public abstract void dispose(int paramInt)
    throws RemoteException;
  
  public abstract int download(DownloadRequest paramDownloadRequest)
    throws RemoteException;
  
  public abstract int initialize(int paramInt, IMbmsDownloadSessionCallback paramIMbmsDownloadSessionCallback)
    throws RemoteException;
  
  public abstract List<DownloadRequest> listPendingDownloads(int paramInt)
    throws RemoteException;
  
  public abstract int removeProgressListener(DownloadRequest paramDownloadRequest, IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException;
  
  public abstract int removeStatusListener(DownloadRequest paramDownloadRequest, IDownloadStatusListener paramIDownloadStatusListener)
    throws RemoteException;
  
  public abstract int requestDownloadState(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo)
    throws RemoteException;
  
  public abstract int requestUpdateFileServices(int paramInt, List<String> paramList)
    throws RemoteException;
  
  public abstract int resetDownloadKnowledge(DownloadRequest paramDownloadRequest)
    throws RemoteException;
  
  public abstract int setTempFileRootDirectory(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMbmsDownloadService
  {
    private static final String DESCRIPTOR = "android.telephony.mbms.vendor.IMbmsDownloadService";
    static final int TRANSACTION_addProgressListener = 7;
    static final int TRANSACTION_addStatusListener = 5;
    static final int TRANSACTION_cancelDownload = 10;
    static final int TRANSACTION_dispose = 13;
    static final int TRANSACTION_download = 4;
    static final int TRANSACTION_initialize = 1;
    static final int TRANSACTION_listPendingDownloads = 9;
    static final int TRANSACTION_removeProgressListener = 8;
    static final int TRANSACTION_removeStatusListener = 6;
    static final int TRANSACTION_requestDownloadState = 11;
    static final int TRANSACTION_requestUpdateFileServices = 2;
    static final int TRANSACTION_resetDownloadKnowledge = 12;
    static final int TRANSACTION_setTempFileRootDirectory = 3;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.mbms.vendor.IMbmsDownloadService");
    }
    
    public static IMbmsDownloadService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
      if ((localIInterface != null) && ((localIInterface instanceof IMbmsDownloadService))) {
        return (IMbmsDownloadService)localIInterface;
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
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 13: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          dispose(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject8;
          }
          paramInt1 = resetDownloadKnowledge(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (FileInfo)FileInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = requestDownloadState((DownloadRequest)localObject8, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = cancelDownload(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          paramParcel1 = listPendingDownloads(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject3;
          }
          paramInt1 = removeProgressListener((DownloadRequest)localObject8, IDownloadProgressListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject4;
          }
          paramInt1 = addProgressListener((DownloadRequest)localObject8, IDownloadProgressListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject5;
          }
          paramInt1 = removeStatusListener((DownloadRequest)localObject8, IDownloadStatusListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject6;
          }
          paramInt1 = addStatusListener((DownloadRequest)localObject8, IDownloadStatusListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramInt1 = download(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          paramInt1 = setTempFileRootDirectory(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
          paramInt1 = requestUpdateFileServices(paramParcel1.readInt(), paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsDownloadService");
        paramInt1 = initialize(paramParcel1.readInt(), IMbmsDownloadSessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.telephony.mbms.vendor.IMbmsDownloadService");
      return true;
    }
    
    private static class Proxy
      implements IMbmsDownloadService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int addProgressListener(DownloadRequest paramDownloadRequest, IDownloadProgressListener paramIDownloadProgressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIDownloadProgressListener != null) {
            paramDownloadRequest = paramIDownloadProgressListener.asBinder();
          } else {
            paramDownloadRequest = null;
          }
          localParcel1.writeStrongBinder(paramDownloadRequest);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int addStatusListener(DownloadRequest paramDownloadRequest, IDownloadStatusListener paramIDownloadStatusListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIDownloadStatusListener != null) {
            paramDownloadRequest = paramIDownloadStatusListener.asBinder();
          } else {
            paramDownloadRequest = null;
          }
          localParcel1.writeStrongBinder(paramDownloadRequest);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public int cancelDownload(DownloadRequest paramDownloadRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dispose(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int download(DownloadRequest paramDownloadRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.mbms.vendor.IMbmsDownloadService";
      }
      
      public int initialize(int paramInt, IMbmsDownloadSessionCallback paramIMbmsDownloadSessionCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          localParcel1.writeInt(paramInt);
          if (paramIMbmsDownloadSessionCallback != null) {
            paramIMbmsDownloadSessionCallback = paramIMbmsDownloadSessionCallback.asBinder();
          } else {
            paramIMbmsDownloadSessionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIMbmsDownloadSessionCallback);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<DownloadRequest> listPendingDownloads(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(DownloadRequest.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int removeProgressListener(DownloadRequest paramDownloadRequest, IDownloadProgressListener paramIDownloadProgressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIDownloadProgressListener != null) {
            paramDownloadRequest = paramIDownloadProgressListener.asBinder();
          } else {
            paramDownloadRequest = null;
          }
          localParcel1.writeStrongBinder(paramDownloadRequest);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int removeStatusListener(DownloadRequest paramDownloadRequest, IDownloadStatusListener paramIDownloadStatusListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIDownloadStatusListener != null) {
            paramDownloadRequest = paramIDownloadStatusListener.asBinder();
          } else {
            paramDownloadRequest = null;
          }
          localParcel1.writeStrongBinder(paramDownloadRequest);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int requestDownloadState(DownloadRequest paramDownloadRequest, FileInfo paramFileInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
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
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int requestUpdateFileServices(int paramInt, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStringList(paramList);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int resetDownloadKnowledge(DownloadRequest paramDownloadRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setTempFileRootDirectory(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsDownloadService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
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
