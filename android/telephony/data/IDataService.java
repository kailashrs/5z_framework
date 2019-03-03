package android.telephony.data;

import android.net.LinkProperties;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IDataService
  extends IInterface
{
  public abstract void createDataServiceProvider(int paramInt)
    throws RemoteException;
  
  public abstract void deactivateDataCall(int paramInt1, int paramInt2, int paramInt3, IDataServiceCallback paramIDataServiceCallback)
    throws RemoteException;
  
  public abstract void getDataCallList(int paramInt, IDataServiceCallback paramIDataServiceCallback)
    throws RemoteException;
  
  public abstract void registerForDataCallListChanged(int paramInt, IDataServiceCallback paramIDataServiceCallback)
    throws RemoteException;
  
  public abstract void removeDataServiceProvider(int paramInt)
    throws RemoteException;
  
  public abstract void setDataProfile(int paramInt, List<DataProfile> paramList, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
    throws RemoteException;
  
  public abstract void setInitialAttachApn(int paramInt, DataProfile paramDataProfile, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
    throws RemoteException;
  
  public abstract void setupDataCall(int paramInt1, int paramInt2, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, LinkProperties paramLinkProperties, IDataServiceCallback paramIDataServiceCallback)
    throws RemoteException;
  
  public abstract void unregisterForDataCallListChanged(int paramInt, IDataServiceCallback paramIDataServiceCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDataService
  {
    private static final String DESCRIPTOR = "android.telephony.data.IDataService";
    static final int TRANSACTION_createDataServiceProvider = 1;
    static final int TRANSACTION_deactivateDataCall = 4;
    static final int TRANSACTION_getDataCallList = 7;
    static final int TRANSACTION_registerForDataCallListChanged = 8;
    static final int TRANSACTION_removeDataServiceProvider = 2;
    static final int TRANSACTION_setDataProfile = 6;
    static final int TRANSACTION_setInitialAttachApn = 5;
    static final int TRANSACTION_setupDataCall = 3;
    static final int TRANSACTION_unregisterForDataCallListChanged = 9;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.data.IDataService");
    }
    
    public static IDataService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.data.IDataService");
      if ((localIInterface != null) && ((localIInterface instanceof IDataService))) {
        return (IDataService)localIInterface;
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
        LinkProperties localLinkProperties = null;
        Object localObject = null;
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 9: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          unregisterForDataCallListChanged(paramParcel1.readInt(), IDataServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          registerForDataCallListChanged(paramParcel1.readInt(), IDataServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          getDataCallList(paramParcel1.readInt(), IDataServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = paramParcel1.createTypedArrayList(DataProfile.CREATOR);
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setDataProfile(paramInt1, paramParcel2, bool2, IDataServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (DataProfile)DataProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject;
          }
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setInitialAttachApn(paramInt1, paramParcel2, bool2, IDataServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          deactivateDataCall(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), IDataServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (DataProfile)DataProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          int i = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localLinkProperties = (LinkProperties)LinkProperties.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          setupDataCall(paramInt1, paramInt2, paramParcel2, bool2, bool1, i, localLinkProperties, IDataServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.data.IDataService");
          removeDataServiceProvider(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.data.IDataService");
        createDataServiceProvider(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.telephony.data.IDataService");
      return true;
    }
    
    private static class Proxy
      implements IDataService
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
      
      public void createDataServiceProvider(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deactivateDataCall(int paramInt1, int paramInt2, int paramInt3, IDataServiceCallback paramIDataServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramIDataServiceCallback != null) {
            paramIDataServiceCallback = paramIDataServiceCallback.asBinder();
          } else {
            paramIDataServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramIDataServiceCallback);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getDataCallList(int paramInt, IDataServiceCallback paramIDataServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt);
          if (paramIDataServiceCallback != null) {
            paramIDataServiceCallback = paramIDataServiceCallback.asBinder();
          } else {
            paramIDataServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramIDataServiceCallback);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.telephony.data.IDataService";
      }
      
      public void registerForDataCallListChanged(int paramInt, IDataServiceCallback paramIDataServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt);
          if (paramIDataServiceCallback != null) {
            paramIDataServiceCallback = paramIDataServiceCallback.asBinder();
          } else {
            paramIDataServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramIDataServiceCallback);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeDataServiceProvider(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setDataProfile(int paramInt, List<DataProfile> paramList, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt);
          localParcel.writeTypedList(paramList);
          localParcel.writeInt(paramBoolean);
          if (paramIDataServiceCallback != null) {
            paramList = paramIDataServiceCallback.asBinder();
          } else {
            paramList = null;
          }
          localParcel.writeStrongBinder(paramList);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setInitialAttachApn(int paramInt, DataProfile paramDataProfile, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt);
          if (paramDataProfile != null)
          {
            localParcel.writeInt(1);
            paramDataProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          if (paramIDataServiceCallback != null) {
            paramDataProfile = paramIDataServiceCallback.asBinder();
          } else {
            paramDataProfile = null;
          }
          localParcel.writeStrongBinder(paramDataProfile);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setupDataCall(int paramInt1, int paramInt2, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, LinkProperties paramLinkProperties, IDataServiceCallback paramIDataServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramDataProfile != null)
          {
            localParcel.writeInt(1);
            paramDataProfile.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeInt(paramInt3);
          if (paramLinkProperties != null)
          {
            localParcel.writeInt(1);
            paramLinkProperties.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIDataServiceCallback != null) {
            paramDataProfile = paramIDataServiceCallback.asBinder();
          } else {
            paramDataProfile = null;
          }
          localParcel.writeStrongBinder(paramDataProfile);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterForDataCallListChanged(int paramInt, IDataServiceCallback paramIDataServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataService");
          localParcel.writeInt(paramInt);
          if (paramIDataServiceCallback != null) {
            paramIDataServiceCallback = paramIDataServiceCallback.asBinder();
          } else {
            paramIDataServiceCallback = null;
          }
          localParcel.writeStrongBinder(paramIDataServiceCallback);
          mRemote.transact(9, localParcel, null, 1);
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
