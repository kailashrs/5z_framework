package com.android.internal.app;

import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IVoiceInteractorCallback
  extends IInterface
{
  public abstract void deliverAbortVoiceResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void deliverCancel(IVoiceInteractorRequest paramIVoiceInteractorRequest)
    throws RemoteException;
  
  public abstract void deliverCommandResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, boolean paramBoolean, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void deliverCompleteVoiceResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void deliverConfirmationResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, boolean paramBoolean, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void deliverPickOptionResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, boolean paramBoolean, VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractorCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractorCallback";
    static final int TRANSACTION_deliverAbortVoiceResult = 4;
    static final int TRANSACTION_deliverCancel = 6;
    static final int TRANSACTION_deliverCommandResult = 5;
    static final int TRANSACTION_deliverCompleteVoiceResult = 3;
    static final int TRANSACTION_deliverConfirmationResult = 1;
    static final int TRANSACTION_deliverPickOptionResult = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IVoiceInteractorCallback");
    }
    
    public static IVoiceInteractorCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IVoiceInteractorCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractorCallback))) {
        return (IVoiceInteractorCallback)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        VoiceInteractor.PickOptionRequest.Option[] arrayOfOption = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractorCallback");
          deliverCancel(IVoiceInteractorRequest.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractorCallback");
          paramParcel2 = IVoiceInteractorRequest.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = arrayOfOption;
          }
          deliverCommandResult(paramParcel2, bool3, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractorCallback");
          paramParcel2 = IVoiceInteractorRequest.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          deliverAbortVoiceResult(paramParcel2, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractorCallback");
          paramParcel2 = IVoiceInteractorRequest.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          deliverCompleteVoiceResult(paramParcel2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractorCallback");
          paramParcel2 = IVoiceInteractorRequest.Stub.asInterface(paramParcel1.readStrongBinder());
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          arrayOfOption = (VoiceInteractor.PickOptionRequest.Option[])paramParcel1.createTypedArray(VoiceInteractor.PickOptionRequest.Option.CREATOR);
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          deliverPickOptionResult(paramParcel2, bool3, arrayOfOption, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.IVoiceInteractorCallback");
        paramParcel2 = IVoiceInteractorRequest.Stub.asInterface(paramParcel1.readStrongBinder());
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject4;
        }
        deliverConfirmationResult(paramParcel2, bool3, paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IVoiceInteractorCallback");
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractorCallback
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
      
      public void deliverAbortVoiceResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractorCallback");
          if (paramIVoiceInteractorRequest != null) {
            paramIVoiceInteractorRequest = paramIVoiceInteractorRequest.asBinder();
          } else {
            paramIVoiceInteractorRequest = null;
          }
          localParcel.writeStrongBinder(paramIVoiceInteractorRequest);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deliverCancel(IVoiceInteractorRequest paramIVoiceInteractorRequest)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractorCallback");
          if (paramIVoiceInteractorRequest != null) {
            paramIVoiceInteractorRequest = paramIVoiceInteractorRequest.asBinder();
          } else {
            paramIVoiceInteractorRequest = null;
          }
          localParcel.writeStrongBinder(paramIVoiceInteractorRequest);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deliverCommandResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, boolean paramBoolean, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractorCallback");
          if (paramIVoiceInteractorRequest != null) {
            paramIVoiceInteractorRequest = paramIVoiceInteractorRequest.asBinder();
          } else {
            paramIVoiceInteractorRequest = null;
          }
          localParcel.writeStrongBinder(paramIVoiceInteractorRequest);
          localParcel.writeInt(paramBoolean);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deliverCompleteVoiceResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractorCallback");
          if (paramIVoiceInteractorRequest != null) {
            paramIVoiceInteractorRequest = paramIVoiceInteractorRequest.asBinder();
          } else {
            paramIVoiceInteractorRequest = null;
          }
          localParcel.writeStrongBinder(paramIVoiceInteractorRequest);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void deliverConfirmationResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, boolean paramBoolean, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractorCallback");
          if (paramIVoiceInteractorRequest != null) {
            paramIVoiceInteractorRequest = paramIVoiceInteractorRequest.asBinder();
          } else {
            paramIVoiceInteractorRequest = null;
          }
          localParcel.writeStrongBinder(paramIVoiceInteractorRequest);
          localParcel.writeInt(paramBoolean);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void deliverPickOptionResult(IVoiceInteractorRequest paramIVoiceInteractorRequest, boolean paramBoolean, VoiceInteractor.PickOptionRequest.Option[] paramArrayOfOption, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IVoiceInteractorCallback");
          if (paramIVoiceInteractorRequest != null) {
            paramIVoiceInteractorRequest = paramIVoiceInteractorRequest.asBinder();
          } else {
            paramIVoiceInteractorRequest = null;
          }
          localParcel.writeStrongBinder(paramIVoiceInteractorRequest);
          localParcel.writeInt(paramBoolean);
          localParcel.writeTypedArray(paramArrayOfOption, 0);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.IVoiceInteractorCallback";
      }
    }
  }
}
