package com.android.ims.internal.uce.presence;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.StatusCode;

public abstract interface IPresenceListener
  extends IInterface
{
  public abstract void capInfoReceived(String paramString, PresTupleInfo[] paramArrayOfPresTupleInfo)
    throws RemoteException;
  
  public abstract void cmdStatus(PresCmdStatus paramPresCmdStatus)
    throws RemoteException;
  
  public abstract void getVersionCb(String paramString)
    throws RemoteException;
  
  public abstract void listCapInfoReceived(PresRlmiInfo paramPresRlmiInfo, PresResInfo[] paramArrayOfPresResInfo)
    throws RemoteException;
  
  public abstract void publishTriggering(PresPublishTriggerType paramPresPublishTriggerType)
    throws RemoteException;
  
  public abstract void serviceAvailable(StatusCode paramStatusCode)
    throws RemoteException;
  
  public abstract void serviceUnAvailable(StatusCode paramStatusCode)
    throws RemoteException;
  
  public abstract void sipResponseReceived(PresSipResponse paramPresSipResponse)
    throws RemoteException;
  
  public abstract void unpublishMessageSent()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPresenceListener
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.uce.presence.IPresenceListener";
    static final int TRANSACTION_capInfoReceived = 7;
    static final int TRANSACTION_cmdStatus = 5;
    static final int TRANSACTION_getVersionCb = 1;
    static final int TRANSACTION_listCapInfoReceived = 8;
    static final int TRANSACTION_publishTriggering = 4;
    static final int TRANSACTION_serviceAvailable = 2;
    static final int TRANSACTION_serviceUnAvailable = 3;
    static final int TRANSACTION_sipResponseReceived = 6;
    static final int TRANSACTION_unpublishMessageSent = 9;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.uce.presence.IPresenceListener");
    }
    
    public static IPresenceListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.uce.presence.IPresenceListener");
      if ((localIInterface != null) && ((localIInterface instanceof IPresenceListener))) {
        return (IPresenceListener)localIInterface;
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
        Object localObject5 = null;
        PresRlmiInfo localPresRlmiInfo = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          unpublishMessageSent();
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramParcel1.readInt() != 0) {
            localPresRlmiInfo = (PresRlmiInfo)PresRlmiInfo.CREATOR.createFromParcel(paramParcel1);
          }
          listCapInfoReceived(localPresRlmiInfo, (PresResInfo[])paramParcel1.createTypedArray(PresResInfo.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          capInfoReceived(paramParcel1.readString(), (PresTupleInfo[])paramParcel1.createTypedArray(PresTupleInfo.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PresSipResponse)PresSipResponse.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          sipResponseReceived(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PresCmdStatus)PresCmdStatus.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          cmdStatus(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PresPublishTriggerType)PresPublishTriggerType.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          publishTriggering(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (StatusCode)StatusCode.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          serviceUnAvailable(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (StatusCode)StatusCode.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          serviceAvailable(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.uce.presence.IPresenceListener");
        getVersionCb(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.uce.presence.IPresenceListener");
      return true;
    }
    
    private static class Proxy
      implements IPresenceListener
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
      
      public void capInfoReceived(String paramString, PresTupleInfo[] paramArrayOfPresTupleInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          localParcel1.writeString(paramString);
          localParcel1.writeTypedArray(paramArrayOfPresTupleInfo, 0);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void cmdStatus(PresCmdStatus paramPresCmdStatus)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramPresCmdStatus != null)
          {
            localParcel1.writeInt(1);
            paramPresCmdStatus.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.uce.presence.IPresenceListener";
      }
      
      public void getVersionCb(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void listCapInfoReceived(PresRlmiInfo paramPresRlmiInfo, PresResInfo[] paramArrayOfPresResInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramPresRlmiInfo != null)
          {
            localParcel1.writeInt(1);
            paramPresRlmiInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeTypedArray(paramArrayOfPresResInfo, 0);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void publishTriggering(PresPublishTriggerType paramPresPublishTriggerType)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramPresPublishTriggerType != null)
          {
            localParcel1.writeInt(1);
            paramPresPublishTriggerType.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void serviceAvailable(StatusCode paramStatusCode)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramStatusCode != null)
          {
            localParcel1.writeInt(1);
            paramStatusCode.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void serviceUnAvailable(StatusCode paramStatusCode)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramStatusCode != null)
          {
            localParcel1.writeInt(1);
            paramStatusCode.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sipResponseReceived(PresSipResponse paramPresSipResponse)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          if (paramPresSipResponse != null)
          {
            localParcel1.writeInt(1);
            paramPresSipResponse.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unpublishMessageSent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.uce.presence.IPresenceListener");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
