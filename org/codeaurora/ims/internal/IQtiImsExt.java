package org.codeaurora.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IQtiImsExt
  extends IInterface
{
  public abstract void getCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void getHandoverConfig(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract IImsMultiIdentityInterface getMultiIdentityInterface(int paramInt)
    throws RemoteException;
  
  public abstract void getPacketCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void getPacketErrorCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract int getRcsAppConfig(int paramInt)
    throws RemoteException;
  
  public abstract int getVvmAppConfig(int paramInt)
    throws RemoteException;
  
  public abstract void querySsacStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void queryVoltePreference(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void queryVopsStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void registerForParticipantStatusInfo(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void resumePendingCall(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void sendCallTransferRequest(int paramInt1, int paramInt2, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void sendCancelModifyCall(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void setCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract void setHandoverConfig(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public abstract int setRcsAppConfig(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int setVvmAppConfig(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void updateVoltePreference(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IQtiImsExt
  {
    private static final String DESCRIPTOR = "org.codeaurora.ims.internal.IQtiImsExt";
    static final int TRANSACTION_getCallForwardUncondTimer = 2;
    static final int TRANSACTION_getHandoverConfig = 13;
    static final int TRANSACTION_getMultiIdentityInterface = 19;
    static final int TRANSACTION_getPacketCount = 3;
    static final int TRANSACTION_getPacketErrorCount = 4;
    static final int TRANSACTION_getRcsAppConfig = 15;
    static final int TRANSACTION_getVvmAppConfig = 17;
    static final int TRANSACTION_querySsacStatus = 8;
    static final int TRANSACTION_queryVoltePreference = 12;
    static final int TRANSACTION_queryVopsStatus = 7;
    static final int TRANSACTION_registerForParticipantStatusInfo = 10;
    static final int TRANSACTION_resumePendingCall = 9;
    static final int TRANSACTION_sendCallTransferRequest = 5;
    static final int TRANSACTION_sendCancelModifyCall = 6;
    static final int TRANSACTION_setCallForwardUncondTimer = 1;
    static final int TRANSACTION_setHandoverConfig = 14;
    static final int TRANSACTION_setRcsAppConfig = 16;
    static final int TRANSACTION_setVvmAppConfig = 18;
    static final int TRANSACTION_updateVoltePreference = 11;
    
    public Stub()
    {
      attachInterface(this, "org.codeaurora.ims.internal.IQtiImsExt");
    }
    
    public static IQtiImsExt asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("org.codeaurora.ims.internal.IQtiImsExt");
      if ((localIInterface != null) && ((localIInterface instanceof IQtiImsExt))) {
        return (IQtiImsExt)localIInterface;
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
        case 19: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          paramParcel1 = getMultiIdentityInterface(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null) {
            paramParcel1 = paramParcel1.asBinder();
          } else {
            paramParcel1 = null;
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          paramInt1 = setVvmAppConfig(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          paramInt1 = getVvmAppConfig(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          paramInt1 = setRcsAppConfig(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          paramInt1 = getRcsAppConfig(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          setHandoverConfig(paramParcel1.readInt(), paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 13: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          getHandoverConfig(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 12: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          queryVoltePreference(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 11: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          updateVoltePreference(paramParcel1.readInt(), paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 10: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          registerForParticipantStatusInfo(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 9: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          resumePendingCall(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          querySsacStatus(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 7: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          queryVopsStatus(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          sendCancelModifyCall(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          sendCallTransferRequest(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          getPacketErrorCount(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          getPacketCount(paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
          getCallForwardUncondTimer(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExt");
        setCallForwardUncondTimer(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), IQtiImsExtListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("org.codeaurora.ims.internal.IQtiImsExt");
      return true;
    }
    
    private static class Proxy
      implements IQtiImsExt
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
      
      public void getCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getHandoverConfig(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "org.codeaurora.ims.internal.IQtiImsExt";
      }
      
      public IImsMultiIdentityInterface getMultiIdentityInterface(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsMultiIdentityInterface localIImsMultiIdentityInterface = IImsMultiIdentityInterface.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsMultiIdentityInterface;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getPacketCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getPacketErrorCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int getRcsAppConfig(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getVvmAppConfig(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void querySsacStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void queryVoltePreference(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void queryVopsStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registerForParticipantStatusInfo(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void resumePendingCall(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendCallTransferRequest(int paramInt1, int paramInt2, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString);
          if (paramIQtiImsExtListener != null) {
            paramString = paramIQtiImsExtListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendCancelModifyCall(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          localParcel.writeInt(paramInt5);
          localParcel.writeInt(paramInt6);
          localParcel.writeInt(paramInt7);
          localParcel.writeInt(paramInt8);
          localParcel.writeString(paramString);
          if (paramIQtiImsExtListener != null) {
            paramString = paramIQtiImsExtListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setHandoverConfig(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int setRcsAppConfig(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setVvmAppConfig(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateVoltePreference(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExt");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIQtiImsExtListener != null) {
            paramIQtiImsExtListener = paramIQtiImsExtListener.asBinder();
          } else {
            paramIQtiImsExtListener = null;
          }
          localParcel.writeStrongBinder(paramIQtiImsExtListener);
          mRemote.transact(11, localParcel, null, 1);
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
