package android.nfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAppCallback
  extends IInterface
{
  public abstract BeamShareData createBeamShareData(byte paramByte)
    throws RemoteException;
  
  public abstract void onNdefPushComplete(byte paramByte)
    throws RemoteException;
  
  public abstract void onTagDiscovered(Tag paramTag)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAppCallback
  {
    private static final String DESCRIPTOR = "android.nfc.IAppCallback";
    static final int TRANSACTION_createBeamShareData = 1;
    static final int TRANSACTION_onNdefPushComplete = 2;
    static final int TRANSACTION_onTagDiscovered = 3;
    
    public Stub()
    {
      attachInterface(this, "android.nfc.IAppCallback");
    }
    
    public static IAppCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.nfc.IAppCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IAppCallback))) {
        return (IAppCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.nfc.IAppCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Tag)Tag.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onTagDiscovered(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.nfc.IAppCallback");
          onNdefPushComplete(paramParcel1.readByte());
          return true;
        }
        paramParcel1.enforceInterface("android.nfc.IAppCallback");
        paramParcel1 = createBeamShareData(paramParcel1.readByte());
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
      }
      paramParcel2.writeString("android.nfc.IAppCallback");
      return true;
    }
    
    private static class Proxy
      implements IAppCallback
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
      
      public BeamShareData createBeamShareData(byte paramByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.IAppCallback");
          localParcel1.writeByte(paramByte);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          BeamShareData localBeamShareData;
          if (localParcel2.readInt() != 0) {
            localBeamShareData = (BeamShareData)BeamShareData.CREATOR.createFromParcel(localParcel2);
          } else {
            localBeamShareData = null;
          }
          return localBeamShareData;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.nfc.IAppCallback";
      }
      
      public void onNdefPushComplete(byte paramByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.nfc.IAppCallback");
          localParcel.writeByte(paramByte);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTagDiscovered(Tag paramTag)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.nfc.IAppCallback");
          if (paramTag != null)
          {
            localParcel.writeInt(1);
            paramTag.writeToParcel(localParcel, 0);
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
    }
  }
}
