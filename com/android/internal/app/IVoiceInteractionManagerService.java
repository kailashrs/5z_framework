package com.android.internal.app;

import android.content.ComponentName;
import android.content.Intent;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.IRecognitionStatusCallback.Stub;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel;
import android.hardware.soundtrigger.SoundTrigger.ModuleProperties;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.service.voice.IVoiceInteractionService;
import android.service.voice.IVoiceInteractionService.Stub;
import android.service.voice.IVoiceInteractionSession;
import android.service.voice.IVoiceInteractionSession.Stub;

public abstract interface IVoiceInteractionManagerService
  extends IInterface
{
  public abstract boolean activeServiceSupportsAssist()
    throws RemoteException;
  
  public abstract boolean activeServiceSupportsLaunchFromKeyguard()
    throws RemoteException;
  
  public abstract void closeSystemDialogs(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int deleteKeyphraseSoundModel(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean deliverNewSession(IBinder paramIBinder, IVoiceInteractionSession paramIVoiceInteractionSession, IVoiceInteractor paramIVoiceInteractor)
    throws RemoteException;
  
  public abstract void finish(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract ComponentName getActiveServiceComponentName()
    throws RemoteException;
  
  public abstract int getDisabledShowContext()
    throws RemoteException;
  
  public abstract SoundTrigger.ModuleProperties getDspModuleProperties(IVoiceInteractionService paramIVoiceInteractionService)
    throws RemoteException;
  
  public abstract SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int getUserDisabledShowContext()
    throws RemoteException;
  
  public abstract void hideCurrentSession()
    throws RemoteException;
  
  public abstract boolean hideSessionFromSession(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean isEnrolledForKeyphrase(IVoiceInteractionService paramIVoiceInteractionService, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isSessionRunning()
    throws RemoteException;
  
  public abstract void launchVoiceAssistFromKeyguard()
    throws RemoteException;
  
  public abstract void onLockscreenShown()
    throws RemoteException;
  
  public abstract void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener paramIVoiceInteractionSessionListener)
    throws RemoteException;
  
  public abstract void setDisabledShowContext(int paramInt)
    throws RemoteException;
  
  public abstract void setKeepAwake(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showSession(IVoiceInteractionService paramIVoiceInteractionService, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract boolean showSessionForActiveService(Bundle paramBundle, int paramInt, IVoiceInteractionSessionShowCallback paramIVoiceInteractionSessionShowCallback, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean showSessionFromSession(IBinder paramIBinder, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract int startAssistantActivity(IBinder paramIBinder, Intent paramIntent, String paramString)
    throws RemoteException;
  
  public abstract int startRecognition(IVoiceInteractionService paramIVoiceInteractionService, int paramInt, String paramString, IRecognitionStatusCallback paramIRecognitionStatusCallback, SoundTrigger.RecognitionConfig paramRecognitionConfig)
    throws RemoteException;
  
  public abstract int startVoiceActivity(IBinder paramIBinder, Intent paramIntent, String paramString)
    throws RemoteException;
  
  public abstract int stopRecognition(IVoiceInteractionService paramIVoiceInteractionService, int paramInt, IRecognitionStatusCallback paramIRecognitionStatusCallback)
    throws RemoteException;
  
  public abstract int updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel paramKeyphraseSoundModel)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractionManagerService
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractionManagerService";
    static final int TRANSACTION_activeServiceSupportsAssist = 25;
    static final int TRANSACTION_activeServiceSupportsLaunchFromKeyguard = 26;
    static final int TRANSACTION_closeSystemDialogs = 8;
    static final int TRANSACTION_deleteKeyphraseSoundModel = 15;
    static final int TRANSACTION_deliverNewSession = 2;
    static final int TRANSACTION_finish = 9;
    static final int TRANSACTION_getActiveServiceComponentName = 20;
    static final int TRANSACTION_getDisabledShowContext = 11;
    static final int TRANSACTION_getDspModuleProperties = 16;
    static final int TRANSACTION_getKeyphraseSoundModel = 13;
    static final int TRANSACTION_getUserDisabledShowContext = 12;
    static final int TRANSACTION_hideCurrentSession = 22;
    static final int TRANSACTION_hideSessionFromSession = 4;
    static final int TRANSACTION_isEnrolledForKeyphrase = 17;
    static final int TRANSACTION_isSessionRunning = 24;
    static final int TRANSACTION_launchVoiceAssistFromKeyguard = 23;
    static final int TRANSACTION_onLockscreenShown = 27;
    static final int TRANSACTION_registerVoiceInteractionSessionListener = 28;
    static final int TRANSACTION_setDisabledShowContext = 10;
    static final int TRANSACTION_setKeepAwake = 7;
    static final int TRANSACTION_showSession = 1;
    static final int TRANSACTION_showSessionForActiveService = 21;
    static final int TRANSACTION_showSessionFromSession = 3;
    static final int TRANSACTION_startAssistantActivity = 6;
    static final int TRANSACTION_startRecognition = 18;
    static final int TRANSACTION_startVoiceActivity = 5;
    static final int TRANSACTION_stopRecognition = 19;
    static final int TRANSACTION_updateKeyphraseSoundModel = 14;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IVoiceInteractionManagerService");
    }
    
    public static IVoiceInteractionManagerService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IVoiceInteractionManagerService");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractionManagerService))) {
        return (IVoiceInteractionManagerService)localIInterface;
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
        boolean bool = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          onLockscreenShown();
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = activeServiceSupportsLaunchFromKeyguard();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = activeServiceSupportsAssist();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = isSessionRunning();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          launchVoiceAssistFromKeyguard();
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          hideCurrentSession();
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject6;
          }
          paramInt1 = showSessionForActiveService((Bundle)localObject2, paramParcel1.readInt(), IVoiceInteractionSessionShowCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramParcel1 = getActiveServiceComponentName();
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
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = stopRecognition(IVoiceInteractionService.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), IRecognitionStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          localObject4 = IVoiceInteractionService.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          localObject3 = paramParcel1.readString();
          localObject2 = IRecognitionStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.RecognitionConfig)SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          paramInt1 = startRecognition((IVoiceInteractionService)localObject4, paramInt1, (String)localObject3, (IRecognitionStatusCallback)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = isEnrolledForKeyphrase(IVoiceInteractionService.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramParcel1 = getDspModuleProperties(IVoiceInteractionService.Stub.asInterface(paramParcel1.readStrongBinder()));
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
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = deleteKeyphraseSoundModel(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SoundTrigger.KeyphraseSoundModel)SoundTrigger.KeyphraseSoundModel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = updateKeyphraseSoundModel(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramParcel1 = getKeyphraseSoundModel(paramParcel1.readInt(), paramParcel1.readString());
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
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = getUserDisabledShowContext();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = getDisabledShowContext();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          setDisabledShowContext(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          finish(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          closeSystemDialogs(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          localObject2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setKeepAwake((IBinder)localObject2, bool);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          }
          paramInt1 = startAssistantActivity((IBinder)localObject3, (Intent)localObject2, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject3;
          }
          paramInt1 = startVoiceActivity((IBinder)localObject4, (Intent)localObject2, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = hideSessionFromSession(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject4;
          }
          paramInt1 = showSessionFromSession((IBinder)localObject3, (Bundle)localObject2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
          paramInt1 = deliverNewSession(paramParcel1.readStrongBinder(), IVoiceInteractionSession.Stub.asInterface(paramParcel1.readStrongBinder()), IVoiceInteractor.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractionManagerService");
        localObject3 = IVoiceInteractionService.Stub.asInterface(paramParcel1.readStrongBinder());
        if (paramParcel1.readInt() != 0) {
          localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject2 = localObject5;
        }
        showSession((IVoiceInteractionService)localObject3, (Bundle)localObject2, paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IVoiceInteractionManagerService");
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractionManagerService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean activeServiceSupportsAssist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean activeServiceSupportsLaunchFromKeyguard()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void closeSystemDialogs(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int deleteKeyphraseSoundModel(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean deliverNewSession(IBinder paramIBinder, IVoiceInteractionSession paramIVoiceInteractionSession, IVoiceInteractor paramIVoiceInteractor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          Object localObject = null;
          if (paramIVoiceInteractionSession != null) {
            paramIBinder = paramIVoiceInteractionSession.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = localObject;
          if (paramIVoiceInteractor != null) {
            paramIBinder = paramIVoiceInteractor.asBinder();
          }
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finish(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getActiveServiceComponentName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDisabledShowContext()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
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
      
      public SoundTrigger.ModuleProperties getDspModuleProperties(IVoiceInteractionService paramIVoiceInteractionService)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          Object localObject = null;
          if (paramIVoiceInteractionService != null) {
            paramIVoiceInteractionService = paramIVoiceInteractionService.asBinder();
          } else {
            paramIVoiceInteractionService = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionService);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIVoiceInteractionService = (SoundTrigger.ModuleProperties)SoundTrigger.ModuleProperties.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIVoiceInteractionService = localObject;
          }
          return paramIVoiceInteractionService;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.IVoiceInteractionManagerService";
      }
      
      public SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (SoundTrigger.KeyphraseSoundModel)SoundTrigger.KeyphraseSoundModel.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUserDisabledShowContext()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          mRemote.transact(12, localParcel1, localParcel2, 0);
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
      
      public void hideCurrentSession()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hideSessionFromSession(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isEnrolledForKeyphrase(IVoiceInteractionService paramIVoiceInteractionService, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          if (paramIVoiceInteractionService != null) {
            paramIVoiceInteractionService = paramIVoiceInteractionService.asBinder();
          } else {
            paramIVoiceInteractionService = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionService);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramIVoiceInteractionService = mRemote;
          boolean bool = false;
          paramIVoiceInteractionService.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSessionRunning()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void launchVoiceAssistFromKeyguard()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onLockscreenShown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener paramIVoiceInteractionSessionListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          if (paramIVoiceInteractionSessionListener != null) {
            paramIVoiceInteractionSessionListener = paramIVoiceInteractionSessionListener.asBinder();
          } else {
            paramIVoiceInteractionSessionListener = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionSessionListener);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDisabledShowContext(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setKeepAwake(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showSession(IVoiceInteractionService paramIVoiceInteractionService, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          if (paramIVoiceInteractionService != null) {
            paramIVoiceInteractionService = paramIVoiceInteractionService.asBinder();
          } else {
            paramIVoiceInteractionService = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionService);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
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
      
      public boolean showSessionForActiveService(Bundle paramBundle, int paramInt, IVoiceInteractionSessionShowCallback paramIVoiceInteractionSessionShowCallback, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramIVoiceInteractionSessionShowCallback != null) {
            paramBundle = paramIVoiceInteractionSessionShowCallback.asBinder();
          } else {
            paramBundle = null;
          }
          localParcel1.writeStrongBinder(paramBundle);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public boolean showSessionFromSession(IBinder paramIBinder, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public int startAssistantActivity(IBinder paramIBinder, Intent paramIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public int startRecognition(IVoiceInteractionService paramIVoiceInteractionService, int paramInt, String paramString, IRecognitionStatusCallback paramIRecognitionStatusCallback, SoundTrigger.RecognitionConfig paramRecognitionConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          Object localObject = null;
          if (paramIVoiceInteractionService != null) {
            paramIVoiceInteractionService = paramIVoiceInteractionService.asBinder();
          } else {
            paramIVoiceInteractionService = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionService);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramIVoiceInteractionService = localObject;
          if (paramIRecognitionStatusCallback != null) {
            paramIVoiceInteractionService = paramIRecognitionStatusCallback.asBinder();
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionService);
          if (paramRecognitionConfig != null)
          {
            localParcel1.writeInt(1);
            paramRecognitionConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startVoiceActivity(IBinder paramIBinder, Intent paramIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public int stopRecognition(IVoiceInteractionService paramIVoiceInteractionService, int paramInt, IRecognitionStatusCallback paramIRecognitionStatusCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          Object localObject = null;
          if (paramIVoiceInteractionService != null) {
            paramIVoiceInteractionService = paramIVoiceInteractionService.asBinder();
          } else {
            paramIVoiceInteractionService = null;
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionService);
          localParcel1.writeInt(paramInt);
          paramIVoiceInteractionService = localObject;
          if (paramIRecognitionStatusCallback != null) {
            paramIVoiceInteractionService = paramIRecognitionStatusCallback.asBinder();
          }
          localParcel1.writeStrongBinder(paramIVoiceInteractionService);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel paramKeyphraseSoundModel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractionManagerService");
          if (paramKeyphraseSoundModel != null)
          {
            localParcel1.writeInt(1);
            paramKeyphraseSoundModel.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
    }
  }
}
