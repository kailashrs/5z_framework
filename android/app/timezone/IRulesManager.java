package android.app.timezone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRulesManager
  extends IInterface
{
  public abstract RulesState getRulesState()
    throws RemoteException;
  
  public abstract int requestInstall(ParcelFileDescriptor paramParcelFileDescriptor, byte[] paramArrayOfByte, ICallback paramICallback)
    throws RemoteException;
  
  public abstract void requestNothing(byte[] paramArrayOfByte, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int requestUninstall(byte[] paramArrayOfByte, ICallback paramICallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRulesManager
  {
    private static final String DESCRIPTOR = "android.app.timezone.IRulesManager";
    static final int TRANSACTION_getRulesState = 1;
    static final int TRANSACTION_requestInstall = 2;
    static final int TRANSACTION_requestNothing = 4;
    static final int TRANSACTION_requestUninstall = 3;
    
    public Stub()
    {
      attachInterface(this, "android.app.timezone.IRulesManager");
    }
    
    public static IRulesManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.timezone.IRulesManager");
      if ((localIInterface != null) && ((localIInterface instanceof IRulesManager))) {
        return (IRulesManager)localIInterface;
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
        boolean bool = false;
        Object localObject;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.app.timezone.IRulesManager");
          localObject = paramParcel1.createByteArray();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          requestNothing((byte[])localObject, bool);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.timezone.IRulesManager");
          paramInt1 = requestUninstall(paramParcel1.createByteArray(), ICallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.timezone.IRulesManager");
          if (paramParcel1.readInt() != 0) {
            localObject = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = null;
          }
          paramInt1 = requestInstall((ParcelFileDescriptor)localObject, paramParcel1.createByteArray(), ICallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.timezone.IRulesManager");
        paramParcel1 = getRulesState();
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
      paramParcel2.writeString("android.app.timezone.IRulesManager");
      return true;
    }
    
    private static class Proxy
      implements IRulesManager
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
        return "android.app.timezone.IRulesManager";
      }
      
      public RulesState getRulesState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.timezone.IRulesManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          RulesState localRulesState;
          if (localParcel2.readInt() != 0) {
            localRulesState = (RulesState)RulesState.CREATOR.createFromParcel(localParcel2);
          } else {
            localRulesState = null;
          }
          return localRulesState;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int requestInstall(ParcelFileDescriptor paramParcelFileDescriptor, byte[] paramArrayOfByte, ICallback paramICallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.timezone.IRulesManager");
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByteArray(paramArrayOfByte);
          if (paramICallback != null) {
            paramParcelFileDescriptor = paramICallback.asBinder();
          } else {
            paramParcelFileDescriptor = null;
          }
          localParcel1.writeStrongBinder(paramParcelFileDescriptor);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestNothing(byte[] paramArrayOfByte, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.timezone.IRulesManager");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramBoolean);
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
      
      public int requestUninstall(byte[] paramArrayOfByte, ICallback paramICallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.timezone.IRulesManager");
          localParcel1.writeByteArray(paramArrayOfByte);
          if (paramICallback != null) {
            paramArrayOfByte = paramICallback.asBinder();
          } else {
            paramArrayOfByte = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfByte);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
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
