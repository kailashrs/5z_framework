package com.android.internal.app;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.IRecognitionStatusCallback.Stub;
import android.hardware.soundtrigger.SoundTrigger.GenericSoundModel;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISoundTriggerService
  extends IInterface
{
  public abstract void deleteSoundModel(ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract SoundTrigger.GenericSoundModel getSoundModel(ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract boolean isRecognitionActive(ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract int loadGenericSoundModel(SoundTrigger.GenericSoundModel paramGenericSoundModel)
    throws RemoteException;
  
  public abstract int loadKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel paramKeyphraseSoundModel)
    throws RemoteException;
  
  public abstract int startRecognition(ParcelUuid paramParcelUuid, IRecognitionStatusCallback paramIRecognitionStatusCallback, SoundTrigger.RecognitionConfig paramRecognitionConfig)
    throws RemoteException;
  
  public abstract int startRecognitionForIntent(ParcelUuid paramParcelUuid, PendingIntent paramPendingIntent, SoundTrigger.RecognitionConfig paramRecognitionConfig)
    throws RemoteException;
  
  public abstract int startRecognitionForService(ParcelUuid paramParcelUuid, Bundle paramBundle, ComponentName paramComponentName, SoundTrigger.RecognitionConfig paramRecognitionConfig)
    throws RemoteException;
  
  public abstract int stopRecognition(ParcelUuid paramParcelUuid, IRecognitionStatusCallback paramIRecognitionStatusCallback)
    throws RemoteException;
  
  public abstract int stopRecognitionForIntent(ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract int unloadSoundModel(ParcelUuid paramParcelUuid)
    throws RemoteException;
  
  public abstract void updateSoundModel(SoundTrigger.GenericSoundModel paramGenericSoundModel)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISoundTriggerService
  {
    private static final String DESCRIPTOR = "com.android.internal.app.ISoundTriggerService";
    static final int TRANSACTION_deleteSoundModel = 3;
    static final int TRANSACTION_getSoundModel = 1;
    static final int TRANSACTION_isRecognitionActive = 12;
    static final int TRANSACTION_loadGenericSoundModel = 6;
    static final int TRANSACTION_loadKeyphraseSoundModel = 7;
    static final int TRANSACTION_startRecognition = 4;
    static final int TRANSACTION_startRecognitionForIntent = 8;
    static final int TRANSACTION_startRecognitionForService = 9;
    static final int TRANSACTION_stopRecognition = 5;
    static final int TRANSACTION_stopRecognitionForIntent = 10;
    static final int TRANSACTION_unloadSoundModel = 11;
    static final int TRANSACTION_updateSoundModel = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.ISoundTriggerService");
    }
    
    public static ISoundTriggerService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.ISoundTriggerService");
      if ((localIInterface != null) && ((localIInterface instanceof ISoundTriggerService))) {
        return (ISoundTriggerService)localIInterface;
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
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject12;
          }
          paramInt1 = isRecognitionActive(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = unloadSoundModel(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = stopRecognitionForIntent(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject8 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.RecognitionConfig)SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = startRecognitionForService((ParcelUuid)localObject12, (Bundle)localObject8, (ComponentName)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject8 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.RecognitionConfig)SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject4;
          }
          paramInt1 = startRecognitionForIntent((ParcelUuid)localObject12, (PendingIntent)localObject8, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.KeyphraseSoundModel)SoundTrigger.KeyphraseSoundModel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramInt1 = loadKeyphraseSoundModel(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.GenericSoundModel)SoundTrigger.GenericSoundModel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramInt1 = loadGenericSoundModel(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject7;
          }
          paramInt1 = stopRecognition((ParcelUuid)localObject12, IRecognitionStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = null;
          }
          localObject4 = IRecognitionStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.RecognitionConfig)SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject8;
          }
          paramInt1 = startRecognition((ParcelUuid)localObject12, (IRecognitionStatusCallback)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          deleteSoundModel(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.GenericSoundModel)SoundTrigger.GenericSoundModel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          updateSoundModel(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.ISoundTriggerService");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject11;
        }
        paramParcel1 = getSoundModel(paramParcel1);
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
      }
      paramParcel2.writeString("com.android.internal.app.ISoundTriggerService");
      return true;
    }
    
    private static class Proxy
      implements ISoundTriggerService
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
      
      public void deleteSoundModel(ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.ISoundTriggerService";
      }
      
      public SoundTrigger.GenericSoundModel getSoundModel(ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramParcelUuid = (SoundTrigger.GenericSoundModel)SoundTrigger.GenericSoundModel.CREATOR.createFromParcel(localParcel2);
          } else {
            paramParcelUuid = null;
          }
          return paramParcelUuid;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRecognitionActive(ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          boolean bool = true;
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int loadGenericSoundModel(SoundTrigger.GenericSoundModel paramGenericSoundModel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramGenericSoundModel != null)
          {
            localParcel1.writeInt(1);
            paramGenericSoundModel.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int loadKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel paramKeyphraseSoundModel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramKeyphraseSoundModel != null)
          {
            localParcel1.writeInt(1);
            paramKeyphraseSoundModel.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startRecognition(ParcelUuid paramParcelUuid, IRecognitionStatusCallback paramIRecognitionStatusCallback, SoundTrigger.RecognitionConfig paramRecognitionConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIRecognitionStatusCallback != null) {
            paramParcelUuid = paramIRecognitionStatusCallback.asBinder();
          } else {
            paramParcelUuid = null;
          }
          localParcel1.writeStrongBinder(paramParcelUuid);
          if (paramRecognitionConfig != null)
          {
            localParcel1.writeInt(1);
            paramRecognitionConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startRecognitionForIntent(ParcelUuid paramParcelUuid, PendingIntent paramPendingIntent, SoundTrigger.RecognitionConfig paramRecognitionConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRecognitionConfig != null)
          {
            localParcel1.writeInt(1);
            paramRecognitionConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startRecognitionForService(ParcelUuid paramParcelUuid, Bundle paramBundle, ComponentName paramComponentName, SoundTrigger.RecognitionConfig paramRecognitionConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRecognitionConfig != null)
          {
            localParcel1.writeInt(1);
            paramRecognitionConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int stopRecognition(ParcelUuid paramParcelUuid, IRecognitionStatusCallback paramIRecognitionStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIRecognitionStatusCallback != null) {
            paramParcelUuid = paramIRecognitionStatusCallback.asBinder();
          } else {
            paramParcelUuid = null;
          }
          localParcel1.writeStrongBinder(paramParcelUuid);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int stopRecognitionForIntent(ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int unloadSoundModel(ParcelUuid paramParcelUuid)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramParcelUuid != null)
          {
            localParcel1.writeInt(1);
            paramParcelUuid.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateSoundModel(SoundTrigger.GenericSoundModel paramGenericSoundModel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.ISoundTriggerService");
          if (paramGenericSoundModel != null)
          {
            localParcel1.writeInt(1);
            paramGenericSoundModel.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
