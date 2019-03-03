package android.os;

public abstract interface IVoldListener
  extends IInterface
{
  public abstract void onDiskCreated(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onDiskDestroyed(String paramString)
    throws RemoteException;
  
  public abstract void onDiskMetadataChanged(String paramString1, long paramLong, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void onDiskScanned(String paramString)
    throws RemoteException;
  
  public abstract void onVolumeCreated(String paramString1, int paramInt, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void onVolumeDestroyed(String paramString)
    throws RemoteException;
  
  public abstract void onVolumeInternalPathChanged(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void onVolumeMetadataChanged(String paramString1, String paramString2, String paramString3, String paramString4)
    throws RemoteException;
  
  public abstract void onVolumePathChanged(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void onVolumeStateChanged(String paramString, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoldListener
  {
    private static final String DESCRIPTOR = "android.os.IVoldListener";
    static final int TRANSACTION_onDiskCreated = 1;
    static final int TRANSACTION_onDiskDestroyed = 4;
    static final int TRANSACTION_onDiskMetadataChanged = 3;
    static final int TRANSACTION_onDiskScanned = 2;
    static final int TRANSACTION_onVolumeCreated = 5;
    static final int TRANSACTION_onVolumeDestroyed = 10;
    static final int TRANSACTION_onVolumeInternalPathChanged = 9;
    static final int TRANSACTION_onVolumeMetadataChanged = 7;
    static final int TRANSACTION_onVolumePathChanged = 8;
    static final int TRANSACTION_onVolumeStateChanged = 6;
    
    public Stub()
    {
      attachInterface(this, "android.os.IVoldListener");
    }
    
    public static IVoldListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IVoldListener");
      if ((localIInterface != null) && ((localIInterface instanceof IVoldListener))) {
        return (IVoldListener)localIInterface;
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
        case 10: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onVolumeDestroyed(paramParcel1.readString());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onVolumeInternalPathChanged(paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onVolumePathChanged(paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onVolumeMetadataChanged(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onVolumeStateChanged(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onVolumeCreated(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onDiskDestroyed(paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onDiskMetadataChanged(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IVoldListener");
          onDiskScanned(paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.os.IVoldListener");
        onDiskCreated(paramParcel1.readString(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.os.IVoldListener");
      return true;
    }
    
    private static class Proxy
      implements IVoldListener
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
        return "android.os.IVoldListener";
      }
      
      public void onDiskCreated(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDiskDestroyed(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDiskMetadataChanged(String paramString1, long paramLong, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString1);
          localParcel.writeLong(paramLong);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDiskScanned(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumeCreated(String paramString1, int paramInt, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumeDestroyed(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumeInternalPathChanged(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumeMetadataChanged(String paramString1, String paramString2, String paramString3, String paramString4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          localParcel.writeString(paramString4);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumePathChanged(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVolumeStateChanged(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IVoldListener");
          localParcel.writeString(paramString);
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
