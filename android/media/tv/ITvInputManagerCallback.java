package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ITvInputManagerCallback
  extends IInterface
{
  public abstract void onInputAdded(String paramString)
    throws RemoteException;
  
  public abstract void onInputRemoved(String paramString)
    throws RemoteException;
  
  public abstract void onInputStateChanged(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onInputUpdated(String paramString)
    throws RemoteException;
  
  public abstract void onTvInputInfoUpdated(TvInputInfo paramTvInputInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputManagerCallback
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputManagerCallback";
    static final int TRANSACTION_onInputAdded = 1;
    static final int TRANSACTION_onInputRemoved = 2;
    static final int TRANSACTION_onInputStateChanged = 4;
    static final int TRANSACTION_onInputUpdated = 3;
    static final int TRANSACTION_onTvInputInfoUpdated = 5;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputManagerCallback");
    }
    
    public static ITvInputManagerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputManagerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputManagerCallback))) {
        return (ITvInputManagerCallback)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManagerCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TvInputInfo)TvInputInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onTvInputInfoUpdated(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManagerCallback");
          onInputStateChanged(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManagerCallback");
          onInputUpdated(paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputManagerCallback");
          onInputRemoved(paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputManagerCallback");
        onInputAdded(paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputManagerCallback");
      return true;
    }
    
    private static class Proxy
      implements ITvInputManagerCallback
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
        return "android.media.tv.ITvInputManagerCallback";
      }
      
      public void onInputAdded(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputManagerCallback");
          localParcel.writeString(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onInputRemoved(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputManagerCallback");
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onInputStateChanged(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputManagerCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onInputUpdated(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputManagerCallback");
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTvInputInfoUpdated(TvInputInfo paramTvInputInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputManagerCallback");
          if (paramTvInputInfo != null)
          {
            localParcel.writeInt(1);
            paramTvInputInfo.writeToParcel(localParcel, 0);
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
    }
  }
}
