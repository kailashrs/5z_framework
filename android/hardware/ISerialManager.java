package android.hardware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISerialManager
  extends IInterface
{
  public abstract String[] getSerialPorts()
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openSerialPort(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISerialManager
  {
    private static final String DESCRIPTOR = "android.hardware.ISerialManager";
    static final int TRANSACTION_getSerialPorts = 1;
    static final int TRANSACTION_openSerialPort = 2;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.ISerialManager");
    }
    
    public static ISerialManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.ISerialManager");
      if ((localIInterface != null) && ((localIInterface instanceof ISerialManager))) {
        return (ISerialManager)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.hardware.ISerialManager");
          paramParcel1 = openSerialPort(paramParcel1.readString());
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
        }
        paramParcel1.enforceInterface("android.hardware.ISerialManager");
        paramParcel1 = getSerialPorts();
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.ISerialManager");
      return true;
    }
    
    private static class Proxy
      implements ISerialManager
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
        return "android.hardware.ISerialManager";
      }
      
      public String[] getSerialPorts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ISerialManager");
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
      
      public ParcelFileDescriptor openSerialPort(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.ISerialManager");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
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
    }
  }
}
