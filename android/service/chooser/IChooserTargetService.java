package android.service.chooser;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IChooserTargetService
  extends IInterface
{
  public abstract void getChooserTargets(ComponentName paramComponentName, IntentFilter paramIntentFilter, IChooserTargetResult paramIChooserTargetResult)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IChooserTargetService
  {
    private static final String DESCRIPTOR = "android.service.chooser.IChooserTargetService";
    static final int TRANSACTION_getChooserTargets = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.chooser.IChooserTargetService");
    }
    
    public static IChooserTargetService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.chooser.IChooserTargetService");
      if ((localIInterface != null) && ((localIInterface instanceof IChooserTargetService))) {
        return (IChooserTargetService)localIInterface;
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
        paramParcel2.writeString("android.service.chooser.IChooserTargetService");
        return true;
      }
      paramParcel1.enforceInterface("android.service.chooser.IChooserTargetService");
      paramInt1 = paramParcel1.readInt();
      IntentFilter localIntentFilter = null;
      if (paramInt1 != 0) {
        paramParcel2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      if (paramParcel1.readInt() != 0) {
        localIntentFilter = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);
      }
      getChooserTargets(paramParcel2, localIntentFilter, IChooserTargetResult.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements IChooserTargetService
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
      
      public void getChooserTargets(ComponentName paramComponentName, IntentFilter paramIntentFilter, IChooserTargetResult paramIChooserTargetResult)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.chooser.IChooserTargetService");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIntentFilter != null)
          {
            localParcel.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIChooserTargetResult != null) {
            paramComponentName = paramIChooserTargetResult.asBinder();
          } else {
            paramComponentName = null;
          }
          localParcel.writeStrongBinder(paramComponentName);
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
        return "android.service.chooser.IChooserTargetService";
      }
    }
  }
}
