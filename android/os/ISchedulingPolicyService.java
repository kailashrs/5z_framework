package android.os;

public abstract interface ISchedulingPolicyService
  extends IInterface
{
  public abstract int requestCpusetBoost(boolean paramBoolean, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int requestPriority(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISchedulingPolicyService
  {
    private static final String DESCRIPTOR = "android.os.ISchedulingPolicyService";
    static final int TRANSACTION_requestCpusetBoost = 2;
    static final int TRANSACTION_requestPriority = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.ISchedulingPolicyService");
    }
    
    public static ISchedulingPolicyService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.ISchedulingPolicyService");
      if ((localIInterface != null) && ((localIInterface instanceof ISchedulingPolicyService))) {
        return (ISchedulingPolicyService)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.os.ISchedulingPolicyService");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramInt1 = requestCpusetBoost(bool2, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.ISchedulingPolicyService");
        int i = paramParcel1.readInt();
        paramInt1 = paramParcel1.readInt();
        paramInt2 = paramParcel1.readInt();
        bool2 = bool1;
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        }
        paramInt1 = requestPriority(i, paramInt1, paramInt2, bool2);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.os.ISchedulingPolicyService");
      return true;
    }
    
    private static class Proxy
      implements ISchedulingPolicyService
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
        return "android.os.ISchedulingPolicyService";
      }
      
      public int requestCpusetBoost(boolean paramBoolean, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.ISchedulingPolicyService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int requestPriority(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.ISchedulingPolicyService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
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
