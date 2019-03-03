package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ITvInputServiceCallback
  extends IInterface
{
  public abstract void addHardwareInput(int paramInt, TvInputInfo paramTvInputInfo)
    throws RemoteException;
  
  public abstract void addHdmiInput(int paramInt, TvInputInfo paramTvInputInfo)
    throws RemoteException;
  
  public abstract void removeHardwareInput(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputServiceCallback
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputServiceCallback";
    static final int TRANSACTION_addHardwareInput = 1;
    static final int TRANSACTION_addHdmiInput = 2;
    static final int TRANSACTION_removeHardwareInput = 3;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputServiceCallback");
    }
    
    public static ITvInputServiceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputServiceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputServiceCallback))) {
        return (ITvInputServiceCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputServiceCallback");
          removeHardwareInput(paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputServiceCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TvInputInfo)TvInputInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          addHdmiInput(paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputServiceCallback");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (TvInputInfo)TvInputInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        addHardwareInput(paramInt1, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputServiceCallback");
      return true;
    }
    
    private static class Proxy
      implements ITvInputServiceCallback
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addHardwareInput(int paramInt, TvInputInfo paramTvInputInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputServiceCallback");
          localParcel.writeInt(paramInt);
          if (paramTvInputInfo != null)
          {
            localParcel.writeInt(1);
            paramTvInputInfo.writeToParcel(localParcel, 0);
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
      
      public void addHdmiInput(int paramInt, TvInputInfo paramTvInputInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputServiceCallback");
          localParcel.writeInt(paramInt);
          if (paramTvInputInfo != null)
          {
            localParcel.writeInt(1);
            paramTvInputInfo.writeToParcel(localParcel, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.tv.ITvInputServiceCallback";
      }
      
      public void removeHardwareInput(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputServiceCallback");
          localParcel.writeString(paramString);
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
