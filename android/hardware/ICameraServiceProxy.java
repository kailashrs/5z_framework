package android.hardware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ICameraServiceProxy
  extends IInterface
{
  public static final int CAMERA_API_LEVEL_1 = 1;
  public static final int CAMERA_API_LEVEL_2 = 2;
  public static final int CAMERA_FACING_BACK = 0;
  public static final int CAMERA_FACING_EXTERNAL = 2;
  public static final int CAMERA_FACING_FRONT = 1;
  public static final int CAMERA_STATE_ACTIVE = 1;
  public static final int CAMERA_STATE_CLOSED = 3;
  public static final int CAMERA_STATE_IDLE = 2;
  public static final int CAMERA_STATE_OPEN = 0;
  
  public abstract void notifyCameraState(String paramString1, int paramInt1, int paramInt2, String paramString2, int paramInt3)
    throws RemoteException;
  
  public abstract void pingForUserUpdate()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICameraServiceProxy
  {
    private static final String DESCRIPTOR = "android.hardware.ICameraServiceProxy";
    static final int TRANSACTION_notifyCameraState = 2;
    static final int TRANSACTION_pingForUserUpdate = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.ICameraServiceProxy");
    }
    
    public static ICameraServiceProxy asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.ICameraServiceProxy");
      if ((localIInterface != null) && ((localIInterface instanceof ICameraServiceProxy))) {
        return (ICameraServiceProxy)localIInterface;
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
          paramParcel1.enforceInterface("android.hardware.ICameraServiceProxy");
          notifyCameraState(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.ICameraServiceProxy");
        pingForUserUpdate();
        return true;
      }
      paramParcel2.writeString("android.hardware.ICameraServiceProxy");
      return true;
    }
    
    private static class Proxy
      implements ICameraServiceProxy
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
        return "android.hardware.ICameraServiceProxy";
      }
      
      public void notifyCameraState(String paramString1, int paramInt1, int paramInt2, String paramString2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.ICameraServiceProxy");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void pingForUserUpdate()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.ICameraServiceProxy");
          mRemote.transact(1, localParcel, null, 1);
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
