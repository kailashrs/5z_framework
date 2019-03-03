package com.android.internal.app;

import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.app.VoiceInteractor.Prompt;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IVoiceInteractor
  extends IInterface
{
  public abstract IVoiceInteractorRequest startAbortVoice(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract IVoiceInteractorRequest startCommand(String paramString1, IVoiceInteractorCallback paramIVoiceInteractorCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract IVoiceInteractorRequest startCompleteVoice(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract IVoiceInteractorRequest startConfirmation(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract IVoiceInteractorRequest startPickOption(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean[] supportsCommands(String paramString, String[] paramArrayOfString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractor
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractor";
    static final int TRANSACTION_startAbortVoice = 4;
    static final int TRANSACTION_startCommand = 5;
    static final int TRANSACTION_startCompleteVoice = 3;
    static final int TRANSACTION_startConfirmation = 1;
    static final int TRANSACTION_startPickOption = 2;
    static final int TRANSACTION_supportsCommands = 6;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IVoiceInteractor");
    }
    
    public static IVoiceInteractor asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IVoiceInteractor");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractor))) {
        return (IVoiceInteractor)localIInterface;
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
        VoiceInteractor.PickOptionRequest.Option[] arrayOfOption = null;
        Object localObject4 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractor");
          paramParcel1 = supportsCommands(paramParcel1.readString(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeBooleanArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractor");
          localObject3 = paramParcel1.readString();
          localObject2 = IVoiceInteractorCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localObject3 = startCommand((String)localObject3, (IVoiceInteractorCallback)localObject2, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject4;
          if (localObject3 != null) {
            paramParcel1 = ((IVoiceInteractorRequest)localObject3).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractor");
          localObject3 = paramParcel1.readString();
          localObject2 = IVoiceInteractorCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject4 = (VoiceInteractor.Prompt)VoiceInteractor.Prompt.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localObject4 = startAbortVoice((String)localObject3, (IVoiceInteractorCallback)localObject2, (VoiceInteractor.Prompt)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject1;
          if (localObject4 != null) {
            paramParcel1 = ((IVoiceInteractorRequest)localObject4).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractor");
          localObject3 = paramParcel1.readString();
          localObject1 = IVoiceInteractorCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject4 = (VoiceInteractor.Prompt)VoiceInteractor.Prompt.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localObject4 = startCompleteVoice((String)localObject3, (IVoiceInteractorCallback)localObject1, (VoiceInteractor.Prompt)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject2;
          if (localObject4 != null) {
            paramParcel1 = ((IVoiceInteractorRequest)localObject4).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractor");
          localObject2 = paramParcel1.readString();
          localObject1 = IVoiceInteractorCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject4 = (VoiceInteractor.Prompt)VoiceInteractor.Prompt.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          arrayOfOption = (VoiceInteractor.PickOptionRequest.Option[])paramParcel1.createTypedArray(VoiceInteractor.PickOptionRequest.Option.CREATOR);
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          localObject4 = startPickOption((String)localObject2, (IVoiceInteractorCallback)localObject1, (VoiceInteractor.Prompt)localObject4, arrayOfOption, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject3;
          if (localObject4 != null) {
            paramParcel1 = ((IVoiceInteractorRequest)localObject4).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractor");
        localObject1 = paramParcel1.readString();
        localObject3 = IVoiceInteractorCallback.Stub.asInterface(paramParcel1.readStrongBinder());
        if (paramParcel1.readInt() != 0) {
          localObject4 = (VoiceInteractor.Prompt)VoiceInteractor.Prompt.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject4 = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        localObject4 = startConfirmation((String)localObject1, (IVoiceInteractorCallback)localObject3, (VoiceInteractor.Prompt)localObject4, paramParcel1);
        paramParcel2.writeNoException();
        paramParcel1 = arrayOfOption;
        if (localObject4 != null) {
          paramParcel1 = ((IVoiceInteractorRequest)localObject4).asBinder();
        }
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IVoiceInteractor");
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractor
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
        return "com.android.internal.app.IVoiceInteractor";
      }
      
      public IVoiceInteractorRequest startAbortVoice(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractor");
          localParcel1.writeString(paramString);
          if (paramIVoiceInteractorCallback != null) {
            paramString = paramIVoiceInteractorCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          if (paramPrompt != null)
          {
            localParcel1.writeInt(1);
            paramPrompt.writeToParcel(localParcel1, 0);
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
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IVoiceInteractorRequest.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IVoiceInteractorRequest startCommand(String paramString1, IVoiceInteractorCallback paramIVoiceInteractorCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractor");
          localParcel1.writeString(paramString1);
          if (paramIVoiceInteractorCallback != null) {
            paramString1 = paramIVoiceInteractorCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
          localParcel1.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = IVoiceInteractorRequest.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IVoiceInteractorRequest startCompleteVoice(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractor");
          localParcel1.writeString(paramString);
          if (paramIVoiceInteractorCallback != null) {
            paramString = paramIVoiceInteractorCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          if (paramPrompt != null)
          {
            localParcel1.writeInt(1);
            paramPrompt.writeToParcel(localParcel1, 0);
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
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IVoiceInteractorRequest.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IVoiceInteractorRequest startConfirmation(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractor");
          localParcel1.writeString(paramString);
          if (paramIVoiceInteractorCallback != null) {
            paramString = paramIVoiceInteractorCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          if (paramPrompt != null)
          {
            localParcel1.writeInt(1);
            paramPrompt.writeToParcel(localParcel1, 0);
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
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IVoiceInteractorRequest.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IVoiceInteractorRequest startPickOption(String paramString, IVoiceInteractorCallback paramIVoiceInteractorCallback, VoiceInteractor.Prompt paramPrompt, VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractor");
          localParcel1.writeString(paramString);
          if (paramIVoiceInteractorCallback != null) {
            paramString = paramIVoiceInteractorCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          if (paramPrompt != null)
          {
            localParcel1.writeInt(1);
            paramPrompt.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeTypedArray(paramArrayOfOption, 0);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = IVoiceInteractorRequest.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean[] supportsCommands(String paramString, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IVoiceInteractor");
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createBooleanArray();
          return paramString;
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
