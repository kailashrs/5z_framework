package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPlayer
  extends IInterface
{
  public abstract void applyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation)
    throws RemoteException;
  
  public abstract void pause()
    throws RemoteException;
  
  public abstract void setPan(float paramFloat)
    throws RemoteException;
  
  public abstract void setStartDelayMs(int paramInt)
    throws RemoteException;
  
  public abstract void setVolume(float paramFloat)
    throws RemoteException;
  
  public abstract void start()
    throws RemoteException;
  
  public abstract void stop()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPlayer
  {
    private static final String DESCRIPTOR = "android.media.IPlayer";
    static final int TRANSACTION_applyVolumeShaper = 7;
    static final int TRANSACTION_pause = 2;
    static final int TRANSACTION_setPan = 5;
    static final int TRANSACTION_setStartDelayMs = 6;
    static final int TRANSACTION_setVolume = 4;
    static final int TRANSACTION_start = 1;
    static final int TRANSACTION_stop = 3;
    
    public Stub()
    {
      attachInterface(this, "android.media.IPlayer");
    }
    
    public static IPlayer asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IPlayer");
      if ((localIInterface != null) && ((localIInterface instanceof IPlayer))) {
        return (IPlayer)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.media.IPlayer");
          paramInt1 = paramParcel1.readInt();
          Object localObject = null;
          if (paramInt1 != 0) {
            paramParcel2 = (VolumeShaper.Configuration)VolumeShaper.Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VolumeShaper.Operation)VolumeShaper.Operation.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject;
          }
          applyVolumeShaper(paramParcel2, paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.IPlayer");
          setStartDelayMs(paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.IPlayer");
          setPan(paramParcel1.readFloat());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.IPlayer");
          setVolume(paramParcel1.readFloat());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.IPlayer");
          stop();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.IPlayer");
          pause();
          return true;
        }
        paramParcel1.enforceInterface("android.media.IPlayer");
        start();
        return true;
      }
      paramParcel2.writeString("android.media.IPlayer");
      return true;
    }
    
    private static class Proxy
      implements IPlayer
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void applyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlayer");
          if (paramConfiguration != null)
          {
            localParcel.writeInt(1);
            paramConfiguration.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramOperation != null)
          {
            localParcel.writeInt(1);
            paramOperation.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.IPlayer";
      }
      
      public void pause()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlayer");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPan(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlayer");
          localParcel.writeFloat(paramFloat);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setStartDelayMs(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlayer");
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setVolume(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlayer");
          localParcel.writeFloat(paramFloat);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void start()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlayer");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stop()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IPlayer");
          mRemote.transact(3, localParcel, null, 1);
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
