package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ICountryListener
  extends IInterface
{
  public abstract void onCountryDetected(Country paramCountry)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICountryListener
  {
    private static final String DESCRIPTOR = "android.location.ICountryListener";
    static final int TRANSACTION_onCountryDetected = 1;
    
    public Stub()
    {
      attachInterface(this, "android.location.ICountryListener");
    }
    
    public static ICountryListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.ICountryListener");
      if ((localIInterface != null) && ((localIInterface instanceof ICountryListener))) {
        return (ICountryListener)localIInterface;
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
        paramParcel2.writeString("android.location.ICountryListener");
        return true;
      }
      paramParcel1.enforceInterface("android.location.ICountryListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Country)Country.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onCountryDetected(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements ICountryListener
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
        return "android.location.ICountryListener";
      }
      
      public void onCountryDetected(Country paramCountry)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.location.ICountryListener");
          if (paramCountry != null)
          {
            localParcel.writeInt(1);
            paramCountry.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
