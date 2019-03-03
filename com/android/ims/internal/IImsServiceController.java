package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IImsServiceController
  extends IInterface
{
  public abstract IImsMMTelFeature createEmergencyMMTelFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException;
  
  public abstract IImsMMTelFeature createMMTelFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException;
  
  public abstract IImsRcsFeature createRcsFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException;
  
  public abstract void removeImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsServiceController
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsServiceController";
    static final int TRANSACTION_createEmergencyMMTelFeature = 1;
    static final int TRANSACTION_createMMTelFeature = 2;
    static final int TRANSACTION_createRcsFeature = 3;
    static final int TRANSACTION_removeImsFeature = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsServiceController");
    }
    
    public static IImsServiceController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsServiceController");
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
        IImsRcsFeature localIImsRcsFeature = null;
        Object localObject = null;
        IImsMMTelFeature localIImsMMTelFeature = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsServiceController");
          removeImsFeature(paramParcel1.readInt(), paramParcel1.readInt(), IImsFeatureStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsServiceController");
          localIImsRcsFeature = createRcsFeature(paramParcel1.readInt(), IImsFeatureStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localIImsMMTelFeature;
          if (localIImsRcsFeature != null) {
            paramParcel1 = localIImsRcsFeature.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsServiceController");
          localIImsMMTelFeature = createMMTelFeature(paramParcel1.readInt(), IImsFeatureStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localIImsRcsFeature;
          if (localIImsMMTelFeature != null) {
            paramParcel1 = localIImsMMTelFeature.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsServiceController");
        localIImsMMTelFeature = createEmergencyMMTelFeature(paramParcel1.readInt(), IImsFeatureStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel1 = localObject;
        if (localIImsMMTelFeature != null) {
          paramParcel1 = localIImsMMTelFeature.asBinder();
        }
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsServiceController");
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
      
      public IImsMMTelFeature createEmergencyMMTelFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsServiceController");
          localParcel1.writeInt(paramInt);
          if (paramIImsFeatureStatusCallback != null) {
            paramIImsFeatureStatusCallback = paramIImsFeatureStatusCallback.asBinder();
          } else {
            paramIImsFeatureStatusCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsFeatureStatusCallback);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIImsFeatureStatusCallback = IImsMMTelFeature.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIImsFeatureStatusCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsMMTelFeature createMMTelFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsServiceController");
          localParcel1.writeInt(paramInt);
          if (paramIImsFeatureStatusCallback != null) {
            paramIImsFeatureStatusCallback = paramIImsFeatureStatusCallback.asBinder();
          } else {
            paramIImsFeatureStatusCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsFeatureStatusCallback);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIImsFeatureStatusCallback = IImsMMTelFeature.Stub.asInterface(localParcel2.readStrongBinder());
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
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsServiceController");
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.IImsServiceController";
      }
      
      public void removeImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsServiceController");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIImsFeatureStatusCallback != null) {
            paramIImsFeatureStatusCallback = paramIImsFeatureStatusCallback.asBinder();
          } else {
            paramIImsFeatureStatusCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsFeatureStatusCallback);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
