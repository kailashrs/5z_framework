package android.os;

public abstract interface IHardwarePropertiesManager
  extends IInterface
{
  public abstract CpuUsageInfo[] getCpuUsages(String paramString)
    throws RemoteException;
  
  public abstract float[] getDeviceTemperatures(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract float[] getFanSpeeds(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IHardwarePropertiesManager
  {
    private static final String DESCRIPTOR = "android.os.IHardwarePropertiesManager";
    static final int TRANSACTION_getCpuUsages = 2;
    static final int TRANSACTION_getDeviceTemperatures = 1;
    static final int TRANSACTION_getFanSpeeds = 3;
    
    public Stub()
    {
      attachInterface(this, "android.os.IHardwarePropertiesManager");
    }
    
    public static IHardwarePropertiesManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IHardwarePropertiesManager");
      if ((localIInterface != null) && ((localIInterface instanceof IHardwarePropertiesManager))) {
        return (IHardwarePropertiesManager)localIInterface;
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
          paramParcel1.enforceInterface("android.os.IHardwarePropertiesManager");
          paramParcel1 = getFanSpeeds(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeFloatArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IHardwarePropertiesManager");
          paramParcel1 = getCpuUsages(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IHardwarePropertiesManager");
        paramParcel1 = getDeviceTemperatures(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeFloatArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.os.IHardwarePropertiesManager");
      return true;
    }
    
    private static class Proxy
      implements IHardwarePropertiesManager
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
      
      public CpuUsageInfo[] getCpuUsages(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IHardwarePropertiesManager");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = (CpuUsageInfo[])localParcel2.createTypedArray(CpuUsageInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public float[] getDeviceTemperatures(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IHardwarePropertiesManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createFloatArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public float[] getFanSpeeds(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IHardwarePropertiesManager");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createFloatArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IHardwarePropertiesManager";
      }
    }
  }
}
