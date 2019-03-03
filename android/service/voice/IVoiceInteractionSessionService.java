package android.service.voice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IVoiceInteractionSessionService
  extends IInterface
{
  public abstract void newSession(IBinder paramIBinder, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractionSessionService
  {
    private static final String DESCRIPTOR = "android.service.voice.IVoiceInteractionSessionService";
    static final int TRANSACTION_newSession = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.voice.IVoiceInteractionSessionService");
    }
    
    public static IVoiceInteractionSessionService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.voice.IVoiceInteractionSessionService");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractionSessionService))) {
        return (IVoiceInteractionSessionService)localIInterface;
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
        paramParcel2.writeString("android.service.voice.IVoiceInteractionSessionService");
        return true;
      }
      paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSessionService");
      IBinder localIBinder = paramParcel1.readStrongBinder();
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      newSession(localIBinder, paramParcel2, paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractionSessionService
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
        return "android.service.voice.IVoiceInteractionSessionService";
      }
      
      public void newSession(IBinder paramIBinder, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSessionService");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
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
