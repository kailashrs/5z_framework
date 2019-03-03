package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IInstantAppResolver
  extends IInterface
{
  public abstract void getInstantAppIntentFilterList(Intent paramIntent, int[] paramArrayOfInt, String paramString, IRemoteCallback paramIRemoteCallback)
    throws RemoteException;
  
  public abstract void getInstantAppResolveInfoList(Intent paramIntent, int[] paramArrayOfInt, String paramString, int paramInt, IRemoteCallback paramIRemoteCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInstantAppResolver
  {
    private static final String DESCRIPTOR = "android.app.IInstantAppResolver";
    static final int TRANSACTION_getInstantAppIntentFilterList = 2;
    static final int TRANSACTION_getInstantAppResolveInfoList = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.IInstantAppResolver");
    }
    
    public static IInstantAppResolver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IInstantAppResolver");
      if ((localIInterface != null) && ((localIInterface instanceof IInstantAppResolver))) {
        return (IInstantAppResolver)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.app.IInstantAppResolver");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          getInstantAppIntentFilterList(paramParcel2, paramParcel1.createIntArray(), paramParcel1.readString(), IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.app.IInstantAppResolver");
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject1) {
          break;
        }
        getInstantAppResolveInfoList(paramParcel2, paramParcel1.createIntArray(), paramParcel1.readString(), paramParcel1.readInt(), IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.app.IInstantAppResolver");
      return true;
    }
    
    private static class Proxy
      implements IInstantAppResolver
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
      
      public void getInstantAppIntentFilterList(Intent paramIntent, int[] paramArrayOfInt, String paramString, IRemoteCallback paramIRemoteCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IInstantAppResolver");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeIntArray(paramArrayOfInt);
          localParcel.writeString(paramString);
          if (paramIRemoteCallback != null) {
            paramIntent = paramIRemoteCallback.asBinder();
          } else {
            paramIntent = null;
          }
          localParcel.writeStrongBinder(paramIntent);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getInstantAppResolveInfoList(Intent paramIntent, int[] paramArrayOfInt, String paramString, int paramInt, IRemoteCallback paramIRemoteCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IInstantAppResolver");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeIntArray(paramArrayOfInt);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          if (paramIRemoteCallback != null) {
            paramIntent = paramIRemoteCallback.asBinder();
          } else {
            paramIntent = null;
          }
          localParcel.writeStrongBinder(paramIntent);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IInstantAppResolver";
      }
    }
  }
}
