package android.os;

public abstract interface IVibratorService
  extends IInterface
{
  public abstract void cancelVibrate(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean hasAmplitudeControl()
    throws RemoteException;
  
  public abstract boolean hasVibrator()
    throws RemoteException;
  
  public abstract void vibrate(int paramInt1, String paramString, VibrationEffect paramVibrationEffect, int paramInt2, IBinder paramIBinder)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVibratorService
  {
    private static final String DESCRIPTOR = "android.os.IVibratorService";
    static final int TRANSACTION_cancelVibrate = 4;
    static final int TRANSACTION_hasAmplitudeControl = 2;
    static final int TRANSACTION_hasVibrator = 1;
    static final int TRANSACTION_vibrate = 3;
    
    public Stub()
    {
      attachInterface(this, "android.os.IVibratorService");
    }
    
    public static IVibratorService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IVibratorService");
      if ((localIInterface != null) && ((localIInterface instanceof IVibratorService))) {
        return (IVibratorService)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.os.IVibratorService");
          cancelVibrate(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IVibratorService");
          paramInt1 = paramParcel1.readInt();
          String str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (VibrationEffect localVibrationEffect = (VibrationEffect)VibrationEffect.CREATOR.createFromParcel(paramParcel1);; localVibrationEffect = null) {
            break;
          }
          vibrate(paramInt1, str, localVibrationEffect, paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IVibratorService");
          paramInt1 = hasAmplitudeControl();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IVibratorService");
        paramInt1 = hasVibrator();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.os.IVibratorService");
      return true;
    }
    
    private static class Proxy
      implements IVibratorService
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
      
      public void cancelVibrate(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVibratorService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
        return "android.os.IVibratorService";
      }
      
      public boolean hasAmplitudeControl()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVibratorService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean hasVibrator()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVibratorService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
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
      
      public void vibrate(int paramInt1, String paramString, VibrationEffect paramVibrationEffect, int paramInt2, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IVibratorService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          if (paramVibrationEffect != null)
          {
            localParcel1.writeInt(1);
            paramVibrationEffect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
