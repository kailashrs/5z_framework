package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAudioFocusDispatcher
  extends IInterface
{
  public abstract void dispatchAudioFocusChange(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void dispatchFocusResultFromExtPolicy(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAudioFocusDispatcher
  {
    private static final String DESCRIPTOR = "android.media.IAudioFocusDispatcher";
    static final int TRANSACTION_dispatchAudioFocusChange = 1;
    static final int TRANSACTION_dispatchFocusResultFromExtPolicy = 2;
    
    public Stub()
    {
      attachInterface(this, "android.media.IAudioFocusDispatcher");
    }
    
    public static IAudioFocusDispatcher asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IAudioFocusDispatcher");
      if ((localIInterface != null) && ((localIInterface instanceof IAudioFocusDispatcher))) {
        return (IAudioFocusDispatcher)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.media.IAudioFocusDispatcher");
          dispatchFocusResultFromExtPolicy(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.media.IAudioFocusDispatcher");
        dispatchAudioFocusChange(paramParcel1.readInt(), paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.media.IAudioFocusDispatcher");
      return true;
    }
    
    private static class Proxy
      implements IAudioFocusDispatcher
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
      
      public void dispatchAudioFocusChange(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioFocusDispatcher");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchFocusResultFromExtPolicy(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioFocusDispatcher");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
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
        return "android.media.IAudioFocusDispatcher";
      }
    }
  }
}
