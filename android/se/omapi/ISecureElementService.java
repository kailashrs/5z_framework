package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISecureElementService
  extends IInterface
{
  public abstract ISecureElementReader getReader(String paramString)
    throws RemoteException;
  
  public abstract String[] getReaders()
    throws RemoteException;
  
  public abstract boolean[] isNFCEventAllowed(String paramString, byte[] paramArrayOfByte, String[] paramArrayOfString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISecureElementService
  {
    private static final String DESCRIPTOR = "android.se.omapi.ISecureElementService";
    static final int TRANSACTION_getReader = 2;
    static final int TRANSACTION_getReaders = 1;
    static final int TRANSACTION_isNFCEventAllowed = 3;
    
    public Stub()
    {
      attachInterface(this, "android.se.omapi.ISecureElementService");
    }
    
    public static ISecureElementService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.se.omapi.ISecureElementService");
      if ((localIInterface != null) && ((localIInterface instanceof ISecureElementService))) {
        return (ISecureElementService)localIInterface;
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
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementService");
          paramParcel1 = isNFCEventAllowed(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeBooleanArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.se.omapi.ISecureElementService");
          paramParcel1 = getReader(paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null) {
            paramParcel1 = paramParcel1.asBinder();
          } else {
            paramParcel1 = null;
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.se.omapi.ISecureElementService");
        paramParcel1 = getReaders();
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.se.omapi.ISecureElementService");
      return true;
    }
    
    private static class Proxy
      implements ISecureElementService
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
        return "android.se.omapi.ISecureElementService";
      }
      
      public ISecureElementReader getReader(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementService");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = ISecureElementReader.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getReaders()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementService");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean[] isNFCEventAllowed(String paramString, byte[] paramArrayOfByte, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.se.omapi.ISecureElementService");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createBooleanArray();
          return paramString;
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
