package android.telephony.mbms.vendor;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.mbms.IMbmsStreamingSessionCallback;
import android.telephony.mbms.IMbmsStreamingSessionCallback.Stub;
import android.telephony.mbms.IStreamingServiceCallback;
import android.telephony.mbms.IStreamingServiceCallback.Stub;
import java.util.List;

public abstract interface IMbmsStreamingService
  extends IInterface
{
  public abstract void dispose(int paramInt)
    throws RemoteException;
  
  public abstract Uri getPlaybackUri(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int initialize(IMbmsStreamingSessionCallback paramIMbmsStreamingSessionCallback, int paramInt)
    throws RemoteException;
  
  public abstract int requestUpdateStreamingServices(int paramInt, List<String> paramList)
    throws RemoteException;
  
  public abstract int startStreaming(int paramInt, String paramString, IStreamingServiceCallback paramIStreamingServiceCallback)
    throws RemoteException;
  
  public abstract void stopStreaming(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMbmsStreamingService
  {
    private static final String DESCRIPTOR = "android.telephony.mbms.vendor.IMbmsStreamingService";
    static final int TRANSACTION_dispose = 6;
    static final int TRANSACTION_getPlaybackUri = 4;
    static final int TRANSACTION_initialize = 1;
    static final int TRANSACTION_requestUpdateStreamingServices = 2;
    static final int TRANSACTION_startStreaming = 3;
    static final int TRANSACTION_stopStreaming = 5;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.mbms.vendor.IMbmsStreamingService");
    }
    
    public static IMbmsStreamingService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.mbms.vendor.IMbmsStreamingService");
      if ((localIInterface != null) && ((localIInterface instanceof IMbmsStreamingService))) {
        return (IMbmsStreamingService)localIInterface;
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
        case 6: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsStreamingService");
          dispose(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsStreamingService");
          stopStreaming(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsStreamingService");
          paramParcel1 = getPlaybackUri(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsStreamingService");
          paramInt1 = startStreaming(paramParcel1.readInt(), paramParcel1.readString(), IStreamingServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsStreamingService");
          paramInt1 = requestUpdateStreamingServices(paramParcel1.readInt(), paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.mbms.vendor.IMbmsStreamingService");
        paramInt1 = initialize(IMbmsStreamingSessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.telephony.mbms.vendor.IMbmsStreamingService");
      return true;
    }
    
    private static class Proxy
      implements IMbmsStreamingService
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
      
      public void dispose(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsStreamingService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.mbms.vendor.IMbmsStreamingService";
      }
      
      public Uri getPlaybackUri(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsStreamingService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int initialize(IMbmsStreamingSessionCallback paramIMbmsStreamingSessionCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsStreamingService");
          if (paramIMbmsStreamingSessionCallback != null) {
            paramIMbmsStreamingSessionCallback = paramIMbmsStreamingSessionCallback.asBinder();
          } else {
            paramIMbmsStreamingSessionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIMbmsStreamingSessionCallback);
          localParcel1.writeInt(paramInt);
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
      
      public int requestUpdateStreamingServices(int paramInt, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsStreamingService");
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
      
      public int startStreaming(int paramInt, String paramString, IStreamingServiceCallback paramIStreamingServiceCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsStreamingService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          if (paramIStreamingServiceCallback != null) {
            paramString = paramIStreamingServiceCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void stopStreaming(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.mbms.vendor.IMbmsStreamingService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
