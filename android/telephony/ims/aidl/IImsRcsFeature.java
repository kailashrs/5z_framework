package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsRcsFeature
  extends IInterface
{
  public static abstract class Stub
    extends Binder
    implements IImsRcsFeature
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRcsFeature";
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsRcsFeature");
    }
    
    public static IImsRcsFeature asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsRcsFeature");
      if ((localIInterface != null) && ((localIInterface instanceof IImsRcsFeature))) {
        return (IImsRcsFeature)localIInterface;
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
      if (paramInt1 != 1598968902) {
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsRcsFeature");
      return true;
    }
    
    private static class Proxy
      implements IImsRcsFeature
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
        return "android.telephony.ims.aidl.IImsRcsFeature";
      }
    }
  }
}
