package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IStreamingServiceCallback
  extends IInterface
{
  public abstract void onBroadcastSignalStrengthUpdated(int paramInt)
    throws RemoteException;
  
  public abstract void onError(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void onMediaDescriptionUpdated()
    throws RemoteException;
  
  public abstract void onStreamMethodUpdated(int paramInt)
    throws RemoteException;
  
  public abstract void onStreamStateUpdated(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStreamingServiceCallback
  {
    private static final String DESCRIPTOR = "android.telephony.mbms.IStreamingServiceCallback";
    static final int TRANSACTION_onBroadcastSignalStrengthUpdated = 4;
    static final int TRANSACTION_onError = 1;
    static final int TRANSACTION_onMediaDescriptionUpdated = 3;
    static final int TRANSACTION_onStreamMethodUpdated = 5;
    static final int TRANSACTION_onStreamStateUpdated = 2;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.mbms.IStreamingServiceCallback");
    }
    
    public static IStreamingServiceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.mbms.IStreamingServiceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IStreamingServiceCallback))) {
        return (IStreamingServiceCallback)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.telephony.mbms.IStreamingServiceCallback");
          onStreamMethodUpdated(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.mbms.IStreamingServiceCallback");
          onBroadcastSignalStrengthUpdated(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.mbms.IStreamingServiceCallback");
          onMediaDescriptionUpdated();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.mbms.IStreamingServiceCallback");
          onStreamStateUpdated(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.mbms.IStreamingServiceCallback");
        onError(paramParcel1.readInt(), paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.telephony.mbms.IStreamingServiceCallback");
      return true;
    }
    
    private static class Proxy
      implements IStreamingServiceCallback
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
        return "android.telephony.mbms.IStreamingServiceCallback";
      }
      
      public void onBroadcastSignalStrengthUpdated(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IStreamingServiceCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onError(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IStreamingServiceCallback");
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
      
      public void onMediaDescriptionUpdated()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IStreamingServiceCallback");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStreamMethodUpdated(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IStreamingServiceCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStreamStateUpdated(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.mbms.IStreamingServiceCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
