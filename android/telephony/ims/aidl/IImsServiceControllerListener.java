package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.stub.ImsFeatureConfiguration;

public abstract interface IImsServiceControllerListener
  extends IInterface
{
  public abstract void onUpdateSupportedImsFeatures(ImsFeatureConfiguration paramImsFeatureConfiguration)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsServiceControllerListener
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsServiceControllerListener";
    static final int TRANSACTION_onUpdateSupportedImsFeatures = 1;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsServiceControllerListener");
    }
    
    public static IImsServiceControllerListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsServiceControllerListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsServiceControllerListener))) {
        return (IImsServiceControllerListener)localIInterface;
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
        paramParcel2.writeString("android.telephony.ims.aidl.IImsServiceControllerListener");
        return true;
      }
      paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceControllerListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (ImsFeatureConfiguration)ImsFeatureConfiguration.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onUpdateSupportedImsFeatures(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IImsServiceControllerListener
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
        return "android.telephony.ims.aidl.IImsServiceControllerListener";
      }
      
      public void onUpdateSupportedImsFeatures(ImsFeatureConfiguration paramImsFeatureConfiguration)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceControllerListener");
          if (paramImsFeatureConfiguration != null)
          {
            localParcel.writeInt(1);
            paramImsFeatureConfiguration.writeToParcel(localParcel, 0);
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
