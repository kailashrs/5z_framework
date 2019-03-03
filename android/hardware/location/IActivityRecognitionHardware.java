package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IActivityRecognitionHardware
  extends IInterface
{
  public abstract boolean disableActivityEvent(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean enableActivityEvent(String paramString, int paramInt, long paramLong)
    throws RemoteException;
  
  public abstract boolean flush()
    throws RemoteException;
  
  public abstract String[] getSupportedActivities()
    throws RemoteException;
  
  public abstract boolean isActivitySupported(String paramString)
    throws RemoteException;
  
  public abstract boolean registerSink(IActivityRecognitionHardwareSink paramIActivityRecognitionHardwareSink)
    throws RemoteException;
  
  public abstract boolean unregisterSink(IActivityRecognitionHardwareSink paramIActivityRecognitionHardwareSink)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IActivityRecognitionHardware
  {
    private static final String DESCRIPTOR = "android.hardware.location.IActivityRecognitionHardware";
    static final int TRANSACTION_disableActivityEvent = 6;
    static final int TRANSACTION_enableActivityEvent = 5;
    static final int TRANSACTION_flush = 7;
    static final int TRANSACTION_getSupportedActivities = 1;
    static final int TRANSACTION_isActivitySupported = 2;
    static final int TRANSACTION_registerSink = 3;
    static final int TRANSACTION_unregisterSink = 4;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IActivityRecognitionHardware");
    }
    
    public static IActivityRecognitionHardware asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IActivityRecognitionHardware");
      if ((localIInterface != null) && ((localIInterface instanceof IActivityRecognitionHardware))) {
        return (IActivityRecognitionHardware)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardware");
          paramInt1 = flush();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardware");
          paramInt1 = disableActivityEvent(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardware");
          paramInt1 = enableActivityEvent(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardware");
          paramInt1 = unregisterSink(IActivityRecognitionHardwareSink.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardware");
          paramInt1 = registerSink(IActivityRecognitionHardwareSink.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardware");
          paramInt1 = isActivitySupported(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.location.IActivityRecognitionHardware");
        paramParcel1 = getSupportedActivities();
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.location.IActivityRecognitionHardware");
      return true;
    }
    
    private static class Proxy
      implements IActivityRecognitionHardware
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
      
      public boolean disableActivityEvent(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardware");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean enableActivityEvent(String paramString, int paramInt, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardware");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean flush()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardware");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.location.IActivityRecognitionHardware";
      }
      
      public String[] getSupportedActivities()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardware");
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
      
      public boolean isActivitySupported(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardware");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean registerSink(IActivityRecognitionHardwareSink paramIActivityRecognitionHardwareSink)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardware");
          if (paramIActivityRecognitionHardwareSink != null) {
            paramIActivityRecognitionHardwareSink = paramIActivityRecognitionHardwareSink.asBinder();
          } else {
            paramIActivityRecognitionHardwareSink = null;
          }
          localParcel1.writeStrongBinder(paramIActivityRecognitionHardwareSink);
          paramIActivityRecognitionHardwareSink = mRemote;
          boolean bool = false;
          paramIActivityRecognitionHardwareSink.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean unregisterSink(IActivityRecognitionHardwareSink paramIActivityRecognitionHardwareSink)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.location.IActivityRecognitionHardware");
          if (paramIActivityRecognitionHardwareSink != null) {
            paramIActivityRecognitionHardwareSink = paramIActivityRecognitionHardwareSink.asBinder();
          } else {
            paramIActivityRecognitionHardwareSink = null;
          }
          localParcel1.writeStrongBinder(paramIActivityRecognitionHardwareSink);
          paramIActivityRecognitionHardwareSink = mRemote;
          boolean bool = false;
          paramIActivityRecognitionHardwareSink.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
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
