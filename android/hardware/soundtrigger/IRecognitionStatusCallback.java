package android.hardware.soundtrigger;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRecognitionStatusCallback
  extends IInterface
{
  public abstract void onError(int paramInt)
    throws RemoteException;
  
  public abstract void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent paramGenericRecognitionEvent)
    throws RemoteException;
  
  public abstract void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent paramKeyphraseRecognitionEvent)
    throws RemoteException;
  
  public abstract void onRecognitionPaused()
    throws RemoteException;
  
  public abstract void onRecognitionResumed()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecognitionStatusCallback
  {
    private static final String DESCRIPTOR = "android.hardware.soundtrigger.IRecognitionStatusCallback";
    static final int TRANSACTION_onError = 3;
    static final int TRANSACTION_onGenericSoundTriggerDetected = 2;
    static final int TRANSACTION_onKeyphraseDetected = 1;
    static final int TRANSACTION_onRecognitionPaused = 4;
    static final int TRANSACTION_onRecognitionResumed = 5;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.soundtrigger.IRecognitionStatusCallback");
    }
    
    public static IRecognitionStatusCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.soundtrigger.IRecognitionStatusCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IRecognitionStatusCallback))) {
        return (IRecognitionStatusCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.hardware.soundtrigger.IRecognitionStatusCallback");
          onRecognitionResumed();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.soundtrigger.IRecognitionStatusCallback");
          onRecognitionPaused();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.soundtrigger.IRecognitionStatusCallback");
          onError(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.soundtrigger.IRecognitionStatusCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.GenericRecognitionEvent)SoundTrigger.GenericRecognitionEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onGenericSoundTriggerDetected(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.soundtrigger.IRecognitionStatusCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (SoundTrigger.KeyphraseRecognitionEvent)SoundTrigger.KeyphraseRecognitionEvent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onKeyphraseDetected(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.hardware.soundtrigger.IRecognitionStatusCallback");
      return true;
    }
    
    private static class Proxy
      implements IRecognitionStatusCallback
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
        return "android.hardware.soundtrigger.IRecognitionStatusCallback";
      }
      
      public void onError(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.soundtrigger.IRecognitionStatusCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent paramGenericRecognitionEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.soundtrigger.IRecognitionStatusCallback");
          if (paramGenericRecognitionEvent != null)
          {
            localParcel.writeInt(1);
            paramGenericRecognitionEvent.writeToParcel(localParcel, 0);
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
      
      public void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent paramKeyphraseRecognitionEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.soundtrigger.IRecognitionStatusCallback");
          if (paramKeyphraseRecognitionEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyphraseRecognitionEvent.writeToParcel(localParcel, 0);
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
      
      public void onRecognitionPaused()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.soundtrigger.IRecognitionStatusCallback");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRecognitionResumed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.soundtrigger.IRecognitionStatusCallback");
          mRemote.transact(5, localParcel, null, 1);
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
