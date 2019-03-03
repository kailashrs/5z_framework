package android.media;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;

public abstract interface IRingtonePlayer
  extends IInterface
{
  public abstract String getTitle(Uri paramUri)
    throws RemoteException;
  
  public abstract boolean isPlaying(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openRingtone(Uri paramUri)
    throws RemoteException;
  
  public abstract void play(IBinder paramIBinder, Uri paramUri, AudioAttributes paramAudioAttributes, float paramFloat, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void playAsync(Uri paramUri, UserHandle paramUserHandle, boolean paramBoolean, AudioAttributes paramAudioAttributes)
    throws RemoteException;
  
  public abstract void setPlaybackProperties(IBinder paramIBinder, float paramFloat, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void stop(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void stopAsync()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRingtonePlayer
  {
    private static final String DESCRIPTOR = "android.media.IRingtonePlayer";
    static final int TRANSACTION_getTitle = 7;
    static final int TRANSACTION_isPlaying = 3;
    static final int TRANSACTION_openRingtone = 8;
    static final int TRANSACTION_play = 1;
    static final int TRANSACTION_playAsync = 5;
    static final int TRANSACTION_setPlaybackProperties = 4;
    static final int TRANSACTION_stop = 2;
    static final int TRANSACTION_stopAsync = 6;
    
    public Stub()
    {
      attachInterface(this, "android.media.IRingtonePlayer");
    }
    
    public static IRingtonePlayer asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IRingtonePlayer");
      if ((localIInterface != null) && ((localIInterface instanceof IRingtonePlayer))) {
        return (IRingtonePlayer)localIInterface;
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
        Object localObject1 = null;
        IBinder localIBinder = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.media.IRingtonePlayer");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramParcel1 = openRingtone(paramParcel1);
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
        case 7: 
          paramParcel1.enforceInterface("android.media.IRingtonePlayer");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramParcel1 = getTitle(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.IRingtonePlayer");
          stopAsync();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.IRingtonePlayer");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIBinder;
          }
          playAsync(paramParcel2, (UserHandle)localObject2, bool2, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.IRingtonePlayer");
          paramParcel2 = paramParcel1.readStrongBinder();
          f = paramParcel1.readFloat();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setPlaybackProperties(paramParcel2, f, bool2);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.IRingtonePlayer");
          paramInt1 = isPlaying(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.IRingtonePlayer");
          stop(paramParcel1.readStrongBinder());
          return true;
        }
        paramParcel1.enforceInterface("android.media.IRingtonePlayer");
        localIBinder = paramParcel1.readStrongBinder();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localObject2 = (AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel1);
        }
        for (;;)
        {
          break;
        }
        float f = paramParcel1.readFloat();
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        play(localIBinder, paramParcel2, (AudioAttributes)localObject2, f, bool2);
        return true;
      }
      paramParcel2.writeString("android.media.IRingtonePlayer");
      return true;
    }
    
    private static class Proxy
      implements IRingtonePlayer
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
        return "android.media.IRingtonePlayer";
      }
      
      public String getTitle(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IRingtonePlayer");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramUri = localParcel2.readString();
          return paramUri;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isPlaying(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IRingtonePlayer");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(3, localParcel1, localParcel2, 0);
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
      
      public ParcelFileDescriptor openRingtone(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IRingtonePlayer");
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramUri = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramUri = null;
          }
          return paramUri;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void play(IBinder paramIBinder, Uri paramUri, AudioAttributes paramAudioAttributes, float paramFloat, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRingtonePlayer");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramAudioAttributes != null)
          {
            localParcel.writeInt(1);
            paramAudioAttributes.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeFloat(paramFloat);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void playAsync(Uri paramUri, UserHandle paramUserHandle, boolean paramBoolean, AudioAttributes paramAudioAttributes)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRingtonePlayer");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          if (paramAudioAttributes != null)
          {
            localParcel.writeInt(1);
            paramAudioAttributes.writeToParcel(localParcel, 0);
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
      
      public void setPlaybackProperties(IBinder paramIBinder, float paramFloat, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRingtonePlayer");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeFloat(paramFloat);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stop(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRingtonePlayer");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopAsync()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IRingtonePlayer");
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
