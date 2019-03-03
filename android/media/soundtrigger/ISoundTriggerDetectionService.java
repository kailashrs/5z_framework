package android.media.soundtrigger;

import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISoundTriggerDetectionService
  extends IInterface
{
  public abstract void onError(ParcelUuid paramParcelUuid, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onGenericRecognitionEvent(ParcelUuid paramParcelUuid, int paramInt, SoundTrigger.GenericRecognitionEvent paramGenericRecognitionEvent)
    throws RemoteException;
  
  public abstract void onStopOperation(ParcelUuid paramParcelUuid, int paramInt)
    throws RemoteException;
  
  public abstract void removeClient(ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract void setClient(ParcelUuid paramParcelUuid, Bundle paramBundle, ISoundTriggerDetectionServiceClient paramISoundTriggerDetectionServiceClient)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISoundTriggerDetectionService
  {
    private static final String DESCRIPTOR = "android.media.soundtrigger.ISoundTriggerDetectionService";
    static final int TRANSACTION_onError = 4;
    static final int TRANSACTION_onGenericRecognitionEvent = 3;
    static final int TRANSACTION_onStopOperation = 5;
    static final int TRANSACTION_removeClient = 2;
    static final int TRANSACTION_setClient = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.soundtrigger.ISoundTriggerDetectionService");
    }
    
    public static ISoundTriggerDetectionService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.soundtrigger.ISoundTriggerDetectionService");
      if ((localIInterface != null) && ((localIInterface instanceof ISoundTriggerDetectionService))) {
        return (ISoundTriggerDetectionService)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject5;
          }
          onStopOperation(paramParcel2, paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          onError(paramParcel2, paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.GenericRecognitionEvent)SoundTrigger.GenericRecognitionEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          onGenericRecognitionEvent(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          removeClient(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.soundtrigger.ISoundTriggerDetectionService");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject2 = localObject4;
        }
        setClient(paramParcel2, (Bundle)localObject2, ISoundTriggerDetectionServiceClient.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.media.soundtrigger.ISoundTriggerDetectionService");
      return true;
    }
    
    private static class Proxy
      implements ISoundTriggerDetectionService
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
        return "android.media.soundtrigger.ISoundTriggerDetectionService";
      }
      
      public void onError(ParcelUuid paramParcelUuid, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcelUuid != null)
          {
            localParcel.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGenericRecognitionEvent(ParcelUuid paramParcelUuid, int paramInt, SoundTrigger.GenericRecognitionEvent paramGenericRecognitionEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcelUuid != null)
          {
            localParcel.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramGenericRecognitionEvent != null)
          {
            localParcel.writeInt(1);
            paramGenericRecognitionEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStopOperation(ParcelUuid paramParcelUuid, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcelUuid != null)
          {
            localParcel.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeClient(ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcelUuid != null)
          {
            localParcel.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setClient(ParcelUuid paramParcelUuid, Bundle paramBundle, ISoundTriggerDetectionServiceClient paramISoundTriggerDetectionServiceClient)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.soundtrigger.ISoundTriggerDetectionService");
          if (paramParcelUuid != null)
          {
            localParcel.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramISoundTriggerDetectionServiceClient != null) {
            paramParcelUuid = paramISoundTriggerDetectionServiceClient.asBinder();
          } else {
            paramParcelUuid = null;
          }
          localParcel.writeStrongBinder(paramParcelUuid);
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
