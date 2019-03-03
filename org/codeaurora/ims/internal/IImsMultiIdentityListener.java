package org.codeaurora.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import org.codeaurora.ims.MultiIdentityLineInfo;

public abstract interface IImsMultiIdentityListener
  extends IInterface
{
  public abstract void onQueryVirtualLineInfoResponse(int paramInt, String paramString, List<String> paramList)
    throws RemoteException;
  
  public abstract void onRegistrationStatusChange(int paramInt, List<MultiIdentityLineInfo> paramList)
    throws RemoteException;
  
  public abstract void onUpdateRegistrationInfoResponse(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsMultiIdentityListener
  {
    private static final String DESCRIPTOR = "org.codeaurora.ims.internal.IImsMultiIdentityListener";
    static final int TRANSACTION_onQueryVirtualLineInfoResponse = 3;
    static final int TRANSACTION_onRegistrationStatusChange = 2;
    static final int TRANSACTION_onUpdateRegistrationInfoResponse = 1;
    
    public Stub()
    {
      attachInterface(this, "org.codeaurora.ims.internal.IImsMultiIdentityListener");
    }
    
    public static IImsMultiIdentityListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("org.codeaurora.ims.internal.IImsMultiIdentityListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsMultiIdentityListener))) {
        return (IImsMultiIdentityListener)localIInterface;
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
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IImsMultiIdentityListener");
          onQueryVirtualLineInfoResponse(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createStringArrayList());
          return true;
        case 2: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IImsMultiIdentityListener");
          onRegistrationStatusChange(paramParcel1.readInt(), paramParcel1.createTypedArrayList(MultiIdentityLineInfo.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("org.codeaurora.ims.internal.IImsMultiIdentityListener");
        onUpdateRegistrationInfoResponse(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("org.codeaurora.ims.internal.IImsMultiIdentityListener");
      return true;
    }
    
    private static class Proxy
      implements IImsMultiIdentityListener
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
        return "org.codeaurora.ims.internal.IImsMultiIdentityListener";
      }
      
      public void onQueryVirtualLineInfoResponse(int paramInt, String paramString, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IImsMultiIdentityListener");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          localParcel.writeStringList(paramList);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRegistrationStatusChange(int paramInt, List<MultiIdentityLineInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IImsMultiIdentityListener");
          localParcel.writeInt(paramInt);
          localParcel.writeTypedList(paramList);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUpdateRegistrationInfoResponse(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IImsMultiIdentityListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
