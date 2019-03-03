package android.service.voice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IVoiceInteractionService
  extends IInterface
{
  public abstract void launchVoiceAssistFromKeyguard()
    throws RemoteException;
  
  public abstract void ready()
    throws RemoteException;
  
  public abstract void shutdown()
    throws RemoteException;
  
  public abstract void soundModelsChanged()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractionService
  {
    private static final String DESCRIPTOR = "android.service.voice.IVoiceInteractionService";
    static final int TRANSACTION_launchVoiceAssistFromKeyguard = 4;
    static final int TRANSACTION_ready = 1;
    static final int TRANSACTION_shutdown = 3;
    static final int TRANSACTION_soundModelsChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.service.voice.IVoiceInteractionService");
    }
    
    public static IVoiceInteractionService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.voice.IVoiceInteractionService");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractionService))) {
        return (IVoiceInteractionService)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionService");
          launchVoiceAssistFromKeyguard();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionService");
          shutdown();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionService");
          soundModelsChanged();
          return true;
        }
        paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionService");
        ready();
        return true;
      }
      paramParcel2.writeString("android.service.voice.IVoiceInteractionService");
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractionService
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
        return "android.service.voice.IVoiceInteractionService";
      }
      
      public void launchVoiceAssistFromKeyguard()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionService");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void ready()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionService");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void shutdown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionService");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void soundModelsChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionService");
          mRemote.transact(2, localParcel, null, 1);
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
