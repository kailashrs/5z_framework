package android.os;

public abstract interface IProcessInfoService
  extends IInterface
{
  public abstract void getProcessStatesAndOomScoresFromPids(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
    throws RemoteException;
  
  public abstract void getProcessStatesFromPids(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IProcessInfoService
  {
    private static final String DESCRIPTOR = "android.os.IProcessInfoService";
    static final int TRANSACTION_getProcessStatesAndOomScoresFromPids = 2;
    static final int TRANSACTION_getProcessStatesFromPids = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IProcessInfoService");
    }
    
    public static IProcessInfoService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IProcessInfoService");
      if ((localIInterface != null) && ((localIInterface instanceof IProcessInfoService))) {
        return (IProcessInfoService)localIInterface;
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
          paramParcel1.enforceInterface("android.os.IProcessInfoService");
          int[] arrayOfInt1 = paramParcel1.createIntArray();
          paramInt1 = paramParcel1.readInt();
          if (paramInt1 < 0) {
            arrayOfInt2 = null;
          } else {
            arrayOfInt2 = new int[paramInt1];
          }
          paramInt1 = paramParcel1.readInt();
          if (paramInt1 < 0) {
            paramParcel1 = null;
          } else {
            paramParcel1 = new int[paramInt1];
          }
          getProcessStatesAndOomScoresFromPids(arrayOfInt1, arrayOfInt2, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(arrayOfInt2);
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IProcessInfoService");
        int[] arrayOfInt2 = paramParcel1.createIntArray();
        paramInt1 = paramParcel1.readInt();
        if (paramInt1 < 0) {
          paramParcel1 = null;
        } else {
          paramParcel1 = new int[paramInt1];
        }
        getProcessStatesFromPids(arrayOfInt2, paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeIntArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.os.IProcessInfoService");
      return true;
    }
    
    private static class Proxy
      implements IProcessInfoService
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
        return "android.os.IProcessInfoService";
      }
      
      public void getProcessStatesAndOomScoresFromPids(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IProcessInfoService");
          localParcel1.writeIntArray(paramArrayOfInt1);
          if (paramArrayOfInt2 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfInt2.length);
          }
          if (paramArrayOfInt3 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfInt3.length);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          localParcel2.readIntArray(paramArrayOfInt2);
          localParcel2.readIntArray(paramArrayOfInt3);
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getProcessStatesFromPids(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IProcessInfoService");
          localParcel1.writeIntArray(paramArrayOfInt1);
          if (paramArrayOfInt2 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfInt2.length);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          localParcel2.readIntArray(paramArrayOfInt2);
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
