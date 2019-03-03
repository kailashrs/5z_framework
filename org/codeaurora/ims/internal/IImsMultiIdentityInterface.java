package org.codeaurora.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import org.codeaurora.ims.MultiIdentityLineInfo;

public abstract interface IImsMultiIdentityInterface
  extends IInterface
{
  public abstract void queryVirtualLineInfo(String paramString)
    throws RemoteException;
  
  public abstract void setMultiIdentityListener(IImsMultiIdentityListener paramIImsMultiIdentityListener)
    throws RemoteException;
  
  public abstract void updateRegistrationStatus(List<MultiIdentityLineInfo> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsMultiIdentityInterface
  {
    private static final String DESCRIPTOR = "org.codeaurora.ims.internal.IImsMultiIdentityInterface";
    static final int TRANSACTION_queryVirtualLineInfo = 3;
    static final int TRANSACTION_setMultiIdentityListener = 1;
    static final int TRANSACTION_updateRegistrationStatus = 2;
    
    public Stub()
    {
      attachInterface(this, "org.codeaurora.ims.internal.IImsMultiIdentityInterface");
    }
    
    public static IImsMultiIdentityInterface asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
      if ((localIInterface != null) && ((localIInterface instanceof IImsMultiIdentityInterface))) {
        return (IImsMultiIdentityInterface)localIInterface;
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
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
          queryVirtualLineInfo(paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
          updateRegistrationStatus(paramParcel1.createTypedArrayList(MultiIdentityLineInfo.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
        setMultiIdentityListener(IImsMultiIdentityListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
      return true;
    }
    
    private static class Proxy
      implements IImsMultiIdentityInterface
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
        return "org.codeaurora.ims.internal.IImsMultiIdentityInterface";
      }
      
      public void queryVirtualLineInfo(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setMultiIdentityListener(IImsMultiIdentityListener paramIImsMultiIdentityListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
          if (paramIImsMultiIdentityListener != null) {
            paramIImsMultiIdentityListener = paramIImsMultiIdentityListener.asBinder();
          } else {
            paramIImsMultiIdentityListener = null;
          }
          localParcel.writeStrongBinder(paramIImsMultiIdentityListener);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateRegistrationStatus(List<MultiIdentityLineInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IImsMultiIdentityInterface");
          localParcel.writeTypedList(paramList);
          mRemote.transact(2, localParcel, null, 1);
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
