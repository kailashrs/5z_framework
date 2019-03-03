package android.security.keymaster;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IKeyAttestationApplicationIdProvider
  extends IInterface
{
  public abstract KeyAttestationApplicationId getKeyAttestationApplicationId(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyAttestationApplicationIdProvider
  {
    private static final String DESCRIPTOR = "android.security.keymaster.IKeyAttestationApplicationIdProvider";
    static final int TRANSACTION_getKeyAttestationApplicationId = 1;
    
    public Stub()
    {
      attachInterface(this, "android.security.keymaster.IKeyAttestationApplicationIdProvider");
    }
    
    public static IKeyAttestationApplicationIdProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.security.keymaster.IKeyAttestationApplicationIdProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyAttestationApplicationIdProvider))) {
        return (IKeyAttestationApplicationIdProvider)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.security.keymaster.IKeyAttestationApplicationIdProvider");
        return true;
      }
      paramParcel1.enforceInterface("android.security.keymaster.IKeyAttestationApplicationIdProvider");
      paramParcel1 = getKeyAttestationApplicationId(paramParcel1.readInt());
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
    
    private static class Proxy
      implements IKeyAttestationApplicationIdProvider
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
        return "android.security.keymaster.IKeyAttestationApplicationIdProvider";
      }
      
      public KeyAttestationApplicationId getKeyAttestationApplicationId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.security.keymaster.IKeyAttestationApplicationIdProvider");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          KeyAttestationApplicationId localKeyAttestationApplicationId;
          if (localParcel2.readInt() != 0) {
            localKeyAttestationApplicationId = (KeyAttestationApplicationId)KeyAttestationApplicationId.CREATOR.createFromParcel(localParcel2);
          } else {
            localKeyAttestationApplicationId = null;
          }
          return localKeyAttestationApplicationId;
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
