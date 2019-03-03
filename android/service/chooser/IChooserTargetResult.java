package android.service.chooser;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IChooserTargetResult
  extends IInterface
{
  public abstract void sendResult(List<ChooserTarget> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IChooserTargetResult
  {
    private static final String DESCRIPTOR = "android.service.chooser.IChooserTargetResult";
    static final int TRANSACTION_sendResult = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.chooser.IChooserTargetResult");
    }
    
    public static IChooserTargetResult asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.chooser.IChooserTargetResult");
      if ((localIInterface != null) && ((localIInterface instanceof IChooserTargetResult))) {
        return (IChooserTargetResult)localIInterface;
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
        paramParcel2.writeString("android.service.chooser.IChooserTargetResult");
        return true;
      }
      paramParcel1.enforceInterface("android.service.chooser.IChooserTargetResult");
      sendResult(paramParcel1.createTypedArrayList(ChooserTarget.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IChooserTargetResult
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
        return "android.service.chooser.IChooserTargetResult";
      }
      
      public void sendResult(List<ChooserTarget> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.chooser.IChooserTargetResult");
          localParcel.writeTypedList(paramList);
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
