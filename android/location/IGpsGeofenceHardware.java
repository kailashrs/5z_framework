package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IGpsGeofenceHardware
  extends IInterface
{
  public abstract boolean addCircularHardwareGeofence(int paramInt1, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract boolean isHardwareGeofenceSupported()
    throws RemoteException;
  
  public abstract boolean pauseHardwareGeofence(int paramInt)
    throws RemoteException;
  
  public abstract boolean removeHardwareGeofence(int paramInt)
    throws RemoteException;
  
  public abstract boolean resumeHardwareGeofence(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IGpsGeofenceHardware
  {
    private static final String DESCRIPTOR = "android.location.IGpsGeofenceHardware";
    static final int TRANSACTION_addCircularHardwareGeofence = 2;
    static final int TRANSACTION_isHardwareGeofenceSupported = 1;
    static final int TRANSACTION_pauseHardwareGeofence = 4;
    static final int TRANSACTION_removeHardwareGeofence = 3;
    static final int TRANSACTION_resumeHardwareGeofence = 5;
    
    public Stub()
    {
      attachInterface(this, "android.location.IGpsGeofenceHardware");
    }
    
    public static IGpsGeofenceHardware asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.IGpsGeofenceHardware");
      if ((localIInterface != null) && ((localIInterface instanceof IGpsGeofenceHardware))) {
        return (IGpsGeofenceHardware)localIInterface;
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
          paramParcel1.enforceInterface("android.location.IGpsGeofenceHardware");
          paramInt1 = resumeHardwareGeofence(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.location.IGpsGeofenceHardware");
          paramInt1 = pauseHardwareGeofence(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.location.IGpsGeofenceHardware");
          paramInt1 = removeHardwareGeofence(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.location.IGpsGeofenceHardware");
          paramInt1 = addCircularHardwareGeofence(paramParcel1.readInt(), paramParcel1.readDouble(), paramParcel1.readDouble(), paramParcel1.readDouble(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.location.IGpsGeofenceHardware");
        paramInt1 = isHardwareGeofenceSupported();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.location.IGpsGeofenceHardware");
      return true;
    }
    
    private static class Proxy
      implements IGpsGeofenceHardware
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      /* Error */
      public boolean addCircularHardwareGeofence(int paramInt1, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 32
        //   14: invokevirtual 36	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 12
        //   19: iload_1
        //   20: invokevirtual 40	android/os/Parcel:writeInt	(I)V
        //   23: aload 12
        //   25: dload_2
        //   26: invokevirtual 44	android/os/Parcel:writeDouble	(D)V
        //   29: aload 12
        //   31: dload 4
        //   33: invokevirtual 44	android/os/Parcel:writeDouble	(D)V
        //   36: aload 12
        //   38: dload 6
        //   40: invokevirtual 44	android/os/Parcel:writeDouble	(D)V
        //   43: aload 12
        //   45: iload 8
        //   47: invokevirtual 40	android/os/Parcel:writeInt	(I)V
        //   50: aload 12
        //   52: iload 9
        //   54: invokevirtual 40	android/os/Parcel:writeInt	(I)V
        //   57: aload 12
        //   59: iload 10
        //   61: invokevirtual 40	android/os/Parcel:writeInt	(I)V
        //   64: aload 12
        //   66: iload 11
        //   68: invokevirtual 40	android/os/Parcel:writeInt	(I)V
        //   71: aload_0
        //   72: getfield 19	android/location/IGpsGeofenceHardware$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   75: astore 14
        //   77: iconst_0
        //   78: istore 15
        //   80: aload 14
        //   82: iconst_2
        //   83: aload 12
        //   85: aload 13
        //   87: iconst_0
        //   88: invokeinterface 50 5 0
        //   93: pop
        //   94: aload 13
        //   96: invokevirtual 53	android/os/Parcel:readException	()V
        //   99: aload 13
        //   101: invokevirtual 57	android/os/Parcel:readInt	()I
        //   104: istore_1
        //   105: iload_1
        //   106: ifeq +6 -> 112
        //   109: iconst_1
        //   110: istore 15
        //   112: aload 13
        //   114: invokevirtual 60	android/os/Parcel:recycle	()V
        //   117: aload 12
        //   119: invokevirtual 60	android/os/Parcel:recycle	()V
        //   122: iload 15
        //   124: ireturn
        //   125: astore 14
        //   127: goto +40 -> 167
        //   130: astore 14
        //   132: goto +35 -> 167
        //   135: astore 14
        //   137: goto +30 -> 167
        //   140: astore 14
        //   142: goto +25 -> 167
        //   145: astore 14
        //   147: goto +20 -> 167
        //   150: astore 14
        //   152: goto +15 -> 167
        //   155: astore 14
        //   157: goto +10 -> 167
        //   160: astore 14
        //   162: goto +5 -> 167
        //   165: astore 14
        //   167: aload 13
        //   169: invokevirtual 60	android/os/Parcel:recycle	()V
        //   172: aload 12
        //   174: invokevirtual 60	android/os/Parcel:recycle	()V
        //   177: aload 14
        //   179: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	180	0	this	Proxy
        //   0	180	1	paramInt1	int
        //   0	180	2	paramDouble1	double
        //   0	180	4	paramDouble2	double
        //   0	180	6	paramDouble3	double
        //   0	180	8	paramInt2	int
        //   0	180	9	paramInt3	int
        //   0	180	10	paramInt4	int
        //   0	180	11	paramInt5	int
        //   3	170	12	localParcel1	Parcel
        //   8	160	13	localParcel2	Parcel
        //   75	6	14	localIBinder	IBinder
        //   125	1	14	localObject1	Object
        //   130	1	14	localObject2	Object
        //   135	1	14	localObject3	Object
        //   140	1	14	localObject4	Object
        //   145	1	14	localObject5	Object
        //   150	1	14	localObject6	Object
        //   155	1	14	localObject7	Object
        //   160	1	14	localObject8	Object
        //   165	13	14	localObject9	Object
        //   78	45	15	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   71	77	125	finally
        //   80	105	125	finally
        //   64	71	130	finally
        //   57	64	135	finally
        //   50	57	140	finally
        //   43	50	145	finally
        //   36	43	150	finally
        //   29	36	155	finally
        //   23	29	160	finally
        //   10	23	165	finally
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.location.IGpsGeofenceHardware";
      }
      
      public boolean isHardwareGeofenceSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IGpsGeofenceHardware");
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
      
      public boolean pauseHardwareGeofence(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IGpsGeofenceHardware");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
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
      
      public boolean removeHardwareGeofence(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IGpsGeofenceHardware");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean resumeHardwareGeofence(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.IGpsGeofenceHardware");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
