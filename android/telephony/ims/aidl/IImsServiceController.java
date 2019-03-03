package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.ims.internal.IImsFeatureStatusCallback.Stub;

public abstract interface IImsServiceController
  extends IInterface
{
  public abstract IImsMmTelFeature createMmTelFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException;
  
  public abstract IImsRcsFeature createRcsFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException;
  
  public abstract void disableIms(int paramInt)
    throws RemoteException;
  
  public abstract void enableIms(int paramInt)
    throws RemoteException;
  
  public abstract IImsConfig getConfig(int paramInt)
    throws RemoteException;
  
  public abstract IImsRegistration getRegistration(int paramInt)
    throws RemoteException;
  
  public abstract void notifyImsServiceReadyForFeatureCreation()
    throws RemoteException;
  
  public abstract ImsFeatureConfiguration querySupportedImsFeatures()
    throws RemoteException;
  
  public abstract void removeImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException;
  
  public abstract void setListener(IImsServiceControllerListener paramIImsServiceControllerListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsServiceController
  {
    private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsServiceController";
    static final int TRANSACTION_createMmTelFeature = 2;
    static final int TRANSACTION_createRcsFeature = 3;
    static final int TRANSACTION_disableIms = 10;
    static final int TRANSACTION_enableIms = 9;
    static final int TRANSACTION_getConfig = 7;
    static final int TRANSACTION_getRegistration = 8;
    static final int TRANSACTION_notifyImsServiceReadyForFeatureCreation = 5;
    static final int TRANSACTION_querySupportedImsFeatures = 4;
    static final int TRANSACTION_removeImsFeature = 6;
    static final int TRANSACTION_setListener = 1;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.ims.aidl.IImsServiceController");
    }
    
    public static IImsServiceController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.ims.aidl.IImsServiceController");
      if ((localIInterface != null) && ((localIInterface instanceof IImsServiceController))) {
        return (IImsServiceController)localIInterface;
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
        IImsRegistration localIImsRegistration = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          disableIms(paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          enableIms(paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          localIImsRegistration = getRegistration(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject3;
          if (localIImsRegistration != null) {
            paramParcel1 = localIImsRegistration.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          localObject3 = getConfig(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localIImsRegistration;
          if (localObject3 != null) {
            paramParcel1 = ((IImsConfig)localObject3).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          removeImsFeature(paramParcel1.readInt(), paramParcel1.readInt(), IImsFeatureStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          notifyImsServiceReadyForFeatureCreation();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          paramParcel1 = querySupportedImsFeatures();
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
        case 3: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          localObject3 = createRcsFeature(paramParcel1.readInt(), IImsFeatureStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localObject1;
          if (localObject3 != null) {
            paramParcel1 = ((IImsRcsFeature)localObject3).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
          localObject3 = createMmTelFeature(paramParcel1.readInt(), IImsFeatureStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localObject2;
          if (localObject3 != null) {
            paramParcel1 = ((IImsMmTelFeature)localObject3).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.ims.aidl.IImsServiceController");
        setListener(IImsServiceControllerListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.telephony.ims.aidl.IImsServiceController");
      return true;
    }
    
    private static class Proxy
      implements IImsServiceController
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
      
      public IImsMmTelFeature createMmTelFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          localParcel1.writeInt(paramInt);
          if (paramIImsFeatureStatusCallback != null) {
            paramIImsFeatureStatusCallback = paramIImsFeatureStatusCallback.asBinder();
          } else {
            paramIImsFeatureStatusCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsFeatureStatusCallback);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIImsFeatureStatusCallback = IImsMmTelFeature.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIImsFeatureStatusCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsRcsFeature createRcsFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          localParcel1.writeInt(paramInt);
          if (paramIImsFeatureStatusCallback != null) {
            paramIImsFeatureStatusCallback = paramIImsFeatureStatusCallback.asBinder();
          } else {
            paramIImsFeatureStatusCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsFeatureStatusCallback);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIImsFeatureStatusCallback = IImsRcsFeature.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIImsFeatureStatusCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableIms(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          localParcel.writeInt(paramInt);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void enableIms(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          localParcel.writeInt(paramInt);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IImsConfig getConfig(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsConfig localIImsConfig = IImsConfig.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.ims.aidl.IImsServiceController";
      }
      
      public IImsRegistration getRegistration(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsRegistration localIImsRegistration = IImsRegistration.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsRegistration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyImsServiceReadyForFeatureCreation()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ImsFeatureConfiguration querySupportedImsFeatures()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ImsFeatureConfiguration localImsFeatureConfiguration;
          if (localParcel2.readInt() != 0) {
            localImsFeatureConfiguration = (ImsFeatureConfiguration)ImsFeatureConfiguration.CREATOR.createFromParcel(localParcel2);
          } else {
            localImsFeatureConfiguration = null;
          }
          return localImsFeatureConfiguration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIImsFeatureStatusCallback != null) {
            paramIImsFeatureStatusCallback = paramIImsFeatureStatusCallback.asBinder();
          } else {
            paramIImsFeatureStatusCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsFeatureStatusCallback);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setListener(IImsServiceControllerListener paramIImsServiceControllerListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.telephony.ims.aidl.IImsServiceController");
          if (paramIImsServiceControllerListener != null) {
            paramIImsServiceControllerListener = paramIImsServiceControllerListener.asBinder();
          } else {
            paramIImsServiceControllerListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsServiceControllerListener);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
