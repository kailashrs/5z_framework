package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.Surface;

public abstract interface ITvInputHardware
  extends IInterface
{
  public abstract void overrideAudioSink(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void setStreamVolume(float paramFloat)
    throws RemoteException;
  
  public abstract boolean setSurface(Surface paramSurface, TvStreamConfig paramTvStreamConfig)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputHardware
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputHardware";
    static final int TRANSACTION_overrideAudioSink = 3;
    static final int TRANSACTION_setStreamVolume = 2;
    static final int TRANSACTION_setSurface = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputHardware");
    }
    
    public static ITvInputHardware asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputHardware");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputHardware))) {
        return (ITvInputHardware)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputHardware");
          overrideAudioSink(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputHardware");
          setStreamVolume(paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputHardware");
        paramInt1 = paramParcel1.readInt();
        Object localObject = null;
        Surface localSurface;
        if (paramInt1 != 0) {
          localSurface = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
        } else {
          localSurface = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (TvStreamConfig)TvStreamConfig.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject;
        }
        paramInt1 = setSurface(localSurface, paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputHardware");
      return true;
    }
    
    private static class Proxy
      implements ITvInputHardware
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
        return "android.media.tv.ITvInputHardware";
      }
      
      public void overrideAudioSink(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputHardware");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
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
      
      public void setStreamVolume(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputHardware");
          localParcel1.writeFloat(paramFloat);
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
      
      public boolean setSurface(Surface paramSurface, TvStreamConfig paramTvStreamConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.tv.ITvInputHardware");
          boolean bool = true;
          if (paramSurface != null)
          {
            localParcel1.writeInt(1);
            paramSurface.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramTvStreamConfig != null)
          {
            localParcel1.writeInt(1);
            paramTvStreamConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
    }
  }
}
