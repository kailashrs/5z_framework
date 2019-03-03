package android.os.storage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IStorageEventListener
  extends IInterface
{
  public abstract void onDiskDestroyed(DiskInfo paramDiskInfo)
    throws RemoteException;
  
  public abstract void onDiskScanned(DiskInfo paramDiskInfo, int paramInt)
    throws RemoteException;
  
  public abstract void onStorageStateChanged(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void onUsbMassStorageConnectionChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onVolumeForgotten(String paramString)
    throws RemoteException;
  
  public abstract void onVolumeRecordChanged(VolumeRecord paramVolumeRecord)
    throws RemoteException;
  
  public abstract void onVolumeStateChanged(VolumeInfo paramVolumeInfo, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStorageEventListener
  {
    private static final String DESCRIPTOR = "android.os.storage.IStorageEventListener";
    static final int TRANSACTION_onDiskDestroyed = 7;
    static final int TRANSACTION_onDiskScanned = 6;
    static final int TRANSACTION_onStorageStateChanged = 2;
    static final int TRANSACTION_onUsbMassStorageConnectionChanged = 1;
    static final int TRANSACTION_onVolumeForgotten = 5;
    static final int TRANSACTION_onVolumeRecordChanged = 4;
    static final int TRANSACTION_onVolumeStateChanged = 3;
    
    public Stub()
    {
      attachInterface(this, "android.os.storage.IStorageEventListener");
    }
    
    public static IStorageEventListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.storage.IStorageEventListener");
      if ((localIInterface != null) && ((localIInterface instanceof IStorageEventListener))) {
        return (IStorageEventListener)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.os.storage.IStorageEventListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (DiskInfo)DiskInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onDiskDestroyed(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.storage.IStorageEventListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (DiskInfo)DiskInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          onDiskScanned(paramParcel2, paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.storage.IStorageEventListener");
          onVolumeForgotten(paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.storage.IStorageEventListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VolumeRecord)VolumeRecord.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onVolumeRecordChanged(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.storage.IStorageEventListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (VolumeInfo)VolumeInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject3;
          }
          onVolumeStateChanged(paramParcel2, paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.storage.IStorageEventListener");
          onStorageStateChanged(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.os.storage.IStorageEventListener");
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        onUsbMassStorageConnectionChanged(bool);
        return true;
      }
      paramParcel2.writeString("android.os.storage.IStorageEventListener");
      return true;
    }
    
    private static class Proxy
      implements IStorageEventListener
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
        return "android.os.storage.IStorageEventListener";
      }
      
      public void onDiskDestroyed(DiskInfo paramDiskInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageEventListener");
          if (paramDiskInfo != null)
          {
            localParcel.writeInt(1);
            paramDiskInfo.writeToParcel(localParcel, 0);
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
      
      public void onDiskScanned(DiskInfo paramDiskInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageEventListener");
          if (paramDiskInfo != null)
          {
            localParcel.writeInt(1);
            paramDiskInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStorageStateChanged(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageEventListener");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUsbMassStorageConnectionChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageEventListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumeForgotten(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageEventListener");
          localParcel.writeString(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumeRecordChanged(VolumeRecord paramVolumeRecord)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageEventListener");
          if (paramVolumeRecord != null)
          {
            localParcel.writeInt(1);
            paramVolumeRecord.writeToParcel(localParcel, 0);
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
      
      public void onVolumeStateChanged(VolumeInfo paramVolumeInfo, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.storage.IStorageEventListener");
          if (paramVolumeInfo != null)
          {
            localParcel.writeInt(1);
            paramVolumeInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
