package android.app.slice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISliceListener
  extends IInterface
{
  public abstract void onSliceUpdated(Slice paramSlice)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISliceListener
  {
    private static final String DESCRIPTOR = "android.app.slice.ISliceListener";
    static final int TRANSACTION_onSliceUpdated = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.slice.ISliceListener");
    }
    
    public static ISliceListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.slice.ISliceListener");
      if ((localIInterface != null) && ((localIInterface instanceof ISliceListener))) {
        return (ISliceListener)localIInterface;
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
        paramParcel2.writeString("android.app.slice.ISliceListener");
        return true;
      }
      paramParcel1.enforceInterface("android.app.slice.ISliceListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Slice)Slice.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onSliceUpdated(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements ISliceListener
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
        return "android.app.slice.ISliceListener";
      }
      
      public void onSliceUpdated(Slice paramSlice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.slice.ISliceListener");
          if (paramSlice != null)
          {
            localParcel.writeInt(1);
            paramSlice.writeToParcel(localParcel, 0);
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
