package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IGnssStatusListener
  extends IInterface
{
  public abstract void onFirstFix(int paramInt)
    throws RemoteException;
  
  public abstract void onGnssStarted()
    throws RemoteException;
  
  public abstract void onGnssStopped()
    throws RemoteException;
  
  public abstract void onNmeaReceived(long paramLong, String paramString)
    throws RemoteException;
  
  public abstract void onSvStatusChanged(int paramInt, int[] paramArrayOfInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGnssStatusListener
  {
    private static final String DESCRIPTOR = "android.location.IGnssStatusListener";
    static final int TRANSACTION_onFirstFix = 3;
    static final int TRANSACTION_onGnssStarted = 1;
    static final int TRANSACTION_onGnssStopped = 2;
    static final int TRANSACTION_onNmeaReceived = 5;
    static final int TRANSACTION_onSvStatusChanged = 4;
    
    public Stub()
    {
      attachInterface(this, "android.location.IGnssStatusListener");
    }
    
    public static IGnssStatusListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IGnssStatusListener");
      if ((localIInterface != null) && ((localIInterface instanceof IGnssStatusListener))) {
        return (IGnssStatusListener)localIInterface;
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
          paramParcel1.enforceInterface("android.location.IGnssStatusListener");
          onNmeaReceived(paramParcel1.readLong(), paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.location.IGnssStatusListener");
          onSvStatusChanged(paramParcel1.readInt(), paramParcel1.createIntArray(), paramParcel1.createFloatArray(), paramParcel1.createFloatArray(), paramParcel1.createFloatArray(), paramParcel1.createFloatArray());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.location.IGnssStatusListener");
          onFirstFix(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.location.IGnssStatusListener");
          onGnssStopped();
          return true;
        }
        paramParcel1.enforceInterface("android.location.IGnssStatusListener");
        onGnssStarted();
        return true;
      }
      paramParcel2.writeString("android.location.IGnssStatusListener");
      return true;
    }
    
    private static class Proxy
      implements IGnssStatusListener
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
        return "android.location.IGnssStatusListener";
      }
      
      public void onFirstFix(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssStatusListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGnssStarted()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssStatusListener");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGnssStopped()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssStatusListener");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNmeaReceived(long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssStatusListener");
          localParcel.writeLong(paramLong);
          localParcel.writeString(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSvStatusChanged(int paramInt, int[] paramArrayOfInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.IGnssStatusListener");
          localParcel.writeInt(paramInt);
          localParcel.writeIntArray(paramArrayOfInt);
          localParcel.writeFloatArray(paramArrayOfFloat1);
          localParcel.writeFloatArray(paramArrayOfFloat2);
          localParcel.writeFloatArray(paramArrayOfFloat3);
          localParcel.writeFloatArray(paramArrayOfFloat4);
          mRemote.transact(4, localParcel, null, 1);
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
