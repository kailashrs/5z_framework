package android.media.audiopolicy;

import android.media.AudioFocusInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAudioPolicyCallback
  extends IInterface
{
  public abstract void notifyAudioFocusAbandon(AudioFocusInfo paramAudioFocusInfo)
    throws RemoteException;
  
  public abstract void notifyAudioFocusGrant(AudioFocusInfo paramAudioFocusInfo, int paramInt)
    throws RemoteException;
  
  public abstract void notifyAudioFocusLoss(AudioFocusInfo paramAudioFocusInfo, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyAudioFocusRequest(AudioFocusInfo paramAudioFocusInfo, int paramInt)
    throws RemoteException;
  
  public abstract void notifyMixStateUpdate(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void notifyVolumeAdjust(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAudioPolicyCallback
  {
    private static final String DESCRIPTOR = "android.media.audiopolicy.IAudioPolicyCallback";
    static final int TRANSACTION_notifyAudioFocusAbandon = 4;
    static final int TRANSACTION_notifyAudioFocusGrant = 1;
    static final int TRANSACTION_notifyAudioFocusLoss = 2;
    static final int TRANSACTION_notifyAudioFocusRequest = 3;
    static final int TRANSACTION_notifyMixStateUpdate = 5;
    static final int TRANSACTION_notifyVolumeAdjust = 6;
    
    public Stub()
    {
      attachInterface(this, "android.media.audiopolicy.IAudioPolicyCallback");
    }
    
    public static IAudioPolicyCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.audiopolicy.IAudioPolicyCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IAudioPolicyCallback))) {
        return (IAudioPolicyCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.media.audiopolicy.IAudioPolicyCallback");
          notifyVolumeAdjust(paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.audiopolicy.IAudioPolicyCallback");
          notifyMixStateUpdate(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.audiopolicy.IAudioPolicyCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AudioFocusInfo)AudioFocusInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          notifyAudioFocusAbandon(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.audiopolicy.IAudioPolicyCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AudioFocusInfo)AudioFocusInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          notifyAudioFocusRequest(paramParcel2, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.audiopolicy.IAudioPolicyCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AudioFocusInfo)AudioFocusInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          notifyAudioFocusLoss(paramParcel2, bool);
          return true;
        }
        paramParcel1.enforceInterface("android.media.audiopolicy.IAudioPolicyCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (AudioFocusInfo)AudioFocusInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = localObject3;
        }
        notifyAudioFocusGrant(paramParcel2, paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.media.audiopolicy.IAudioPolicyCallback");
      return true;
    }
    
    private static class Proxy
      implements IAudioPolicyCallback
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
        return "android.media.audiopolicy.IAudioPolicyCallback";
      }
      
      public void notifyAudioFocusAbandon(AudioFocusInfo paramAudioFocusInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.audiopolicy.IAudioPolicyCallback");
          if (paramAudioFocusInfo != null)
          {
            localParcel.writeInt(1);
            paramAudioFocusInfo.writeToParcel(localParcel, 0);
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
      
      public void notifyAudioFocusGrant(AudioFocusInfo paramAudioFocusInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.audiopolicy.IAudioPolicyCallback");
          if (paramAudioFocusInfo != null)
          {
            localParcel.writeInt(1);
            paramAudioFocusInfo.writeToParcel(localParcel, 0);
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
      
      public void notifyAudioFocusLoss(AudioFocusInfo paramAudioFocusInfo, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.audiopolicy.IAudioPolicyCallback");
          if (paramAudioFocusInfo != null)
          {
            localParcel.writeInt(1);
            paramAudioFocusInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyAudioFocusRequest(AudioFocusInfo paramAudioFocusInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.audiopolicy.IAudioPolicyCallback");
          if (paramAudioFocusInfo != null)
          {
            localParcel.writeInt(1);
            paramAudioFocusInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyMixStateUpdate(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.audiopolicy.IAudioPolicyCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyVolumeAdjust(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.audiopolicy.IAudioPolicyCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
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
