package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IMediaHTTPConnection
  extends IInterface
{
  public abstract IBinder connect(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void disconnect()
    throws RemoteException;
  
  public abstract String getMIMEType()
    throws RemoteException;
  
  public abstract long getSize()
    throws RemoteException;
  
  public abstract String getUri()
    throws RemoteException;
  
  public abstract int readAt(long paramLong, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaHTTPConnection
  {
    private static final String DESCRIPTOR = "android.media.IMediaHTTPConnection";
    static final int TRANSACTION_connect = 1;
    static final int TRANSACTION_disconnect = 2;
    static final int TRANSACTION_getMIMEType = 5;
    static final int TRANSACTION_getSize = 4;
    static final int TRANSACTION_getUri = 6;
    static final int TRANSACTION_readAt = 3;
    
    public Stub()
    {
      attachInterface(this, "android.media.IMediaHTTPConnection");
    }
    
    public static IMediaHTTPConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IMediaHTTPConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaHTTPConnection))) {
        return (IMediaHTTPConnection)localIInterface;
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
          paramParcel1.enforceInterface("android.media.IMediaHTTPConnection");
          paramParcel1 = getUri();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.IMediaHTTPConnection");
          paramParcel1 = getMIMEType();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.IMediaHTTPConnection");
          long l = getSize();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.IMediaHTTPConnection");
          paramInt1 = readAt(paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.IMediaHTTPConnection");
          disconnect();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.media.IMediaHTTPConnection");
        paramParcel1 = connect(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.IMediaHTTPConnection");
      return true;
    }
    
    private static class Proxy
      implements IMediaHTTPConnection
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
      
      public IBinder connect(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaHTTPConnection");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readStrongBinder();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disconnect()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaHTTPConnection");
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
        return "android.media.IMediaHTTPConnection";
      }
      
      public String getMIMEType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaHTTPConnection");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getSize()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaHTTPConnection");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getUri()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaHTTPConnection");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int readAt(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IMediaHTTPConnection");
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
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
