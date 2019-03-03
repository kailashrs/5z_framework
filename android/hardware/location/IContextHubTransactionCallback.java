package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IContextHubTransactionCallback
  extends IInterface
{
  public abstract void onQueryResponse(int paramInt, List<NanoAppState> paramList)
    throws RemoteException;
  
  public abstract void onTransactionComplete(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IContextHubTransactionCallback
  {
    private static final String DESCRIPTOR = "android.hardware.location.IContextHubTransactionCallback";
    static final int TRANSACTION_onQueryResponse = 1;
    static final int TRANSACTION_onTransactionComplete = 2;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.location.IContextHubTransactionCallback");
    }
    
    public static IContextHubTransactionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.location.IContextHubTransactionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IContextHubTransactionCallback))) {
        return (IContextHubTransactionCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.hardware.location.IContextHubTransactionCallback");
          onTransactionComplete(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.location.IContextHubTransactionCallback");
        onQueryResponse(paramParcel1.readInt(), paramParcel1.createTypedArrayList(NanoAppState.CREATOR));
        return true;
      }
      paramParcel2.writeString("android.hardware.location.IContextHubTransactionCallback");
      return true;
    }
    
    private static class Proxy
      implements IContextHubTransactionCallback
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
        return "android.hardware.location.IContextHubTransactionCallback";
      }
      
      public void onQueryResponse(int paramInt, List<NanoAppState> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubTransactionCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeTypedList(paramList);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTransactionComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.location.IContextHubTransactionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
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
