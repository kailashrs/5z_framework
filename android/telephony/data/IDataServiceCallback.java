package android.telephony.data;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IDataServiceCallback
  extends IInterface
{
  public abstract void onDataCallListChanged(List<DataCallResponse> paramList)
    throws RemoteException;
  
  public abstract void onDeactivateDataCallComplete(int paramInt)
    throws RemoteException;
  
  public abstract void onGetDataCallListComplete(int paramInt, List<DataCallResponse> paramList)
    throws RemoteException;
  
  public abstract void onSetDataProfileComplete(int paramInt)
    throws RemoteException;
  
  public abstract void onSetInitialAttachApnComplete(int paramInt)
    throws RemoteException;
  
  public abstract void onSetupDataCallComplete(int paramInt, DataCallResponse paramDataCallResponse)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDataServiceCallback
  {
    private static final String DESCRIPTOR = "android.telephony.data.IDataServiceCallback";
    static final int TRANSACTION_onDataCallListChanged = 6;
    static final int TRANSACTION_onDeactivateDataCallComplete = 2;
    static final int TRANSACTION_onGetDataCallListComplete = 5;
    static final int TRANSACTION_onSetDataProfileComplete = 4;
    static final int TRANSACTION_onSetInitialAttachApnComplete = 3;
    static final int TRANSACTION_onSetupDataCallComplete = 1;
    
    public Stub()
    {
      attachInterface(this, "android.telephony.data.IDataServiceCallback");
    }
    
    public static IDataServiceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.telephony.data.IDataServiceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IDataServiceCallback))) {
        return (IDataServiceCallback)localIInterface;
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
        case 6: 
          paramParcel1.enforceInterface("android.telephony.data.IDataServiceCallback");
          onDataCallListChanged(paramParcel1.createTypedArrayList(DataCallResponse.CREATOR));
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.telephony.data.IDataServiceCallback");
          onGetDataCallListComplete(paramParcel1.readInt(), paramParcel1.createTypedArrayList(DataCallResponse.CREATOR));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.telephony.data.IDataServiceCallback");
          onSetDataProfileComplete(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.telephony.data.IDataServiceCallback");
          onSetInitialAttachApnComplete(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.telephony.data.IDataServiceCallback");
          onDeactivateDataCallComplete(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.telephony.data.IDataServiceCallback");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (DataCallResponse)DataCallResponse.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onSetupDataCallComplete(paramInt1, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.telephony.data.IDataServiceCallback");
      return true;
    }
    
    private static class Proxy
      implements IDataServiceCallback
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
        return "android.telephony.data.IDataServiceCallback";
      }
      
      public void onDataCallListChanged(List<DataCallResponse> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataServiceCallback");
          localParcel.writeTypedList(paramList);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDeactivateDataCallComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataServiceCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetDataCallListComplete(int paramInt, List<DataCallResponse> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataServiceCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeTypedList(paramList);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetDataProfileComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataServiceCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetInitialAttachApnComplete(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataServiceCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetupDataCallComplete(int paramInt, DataCallResponse paramDataCallResponse)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.telephony.data.IDataServiceCallback");
          localParcel.writeInt(paramInt);
          if (paramDataCallResponse != null)
          {
            localParcel.writeInt(1);
            paramDataCallResponse.writeToParcel(localParcel, 0);
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
    }
  }
}
