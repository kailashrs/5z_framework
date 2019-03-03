package org.codeaurora.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IQtiImsExtListener
  extends IInterface
{
  public abstract void notifyParticipantStatusInfo(int paramInt1, int paramInt2, int paramInt3, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifySsacStatus(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notifyVopsStatus(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onGetCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString, int paramInt8)
    throws RemoteException;
  
  public abstract void onGetHandoverConfig(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onGetPacketCount(int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public abstract void onGetPacketErrorCount(int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public abstract void onSetCallForwardUncondTimer(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onSetHandoverConfig(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onUTReqFailed(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void onVoltePreferenceQueried(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onVoltePreferenceUpdated(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void receiveCallTransferResponse(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void receiveCancelModifyCallResponse(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IQtiImsExtListener
  {
    private static final String DESCRIPTOR = "org.codeaurora.ims.internal.IQtiImsExtListener";
    static final int TRANSACTION_notifyParticipantStatusInfo = 10;
    static final int TRANSACTION_notifySsacStatus = 9;
    static final int TRANSACTION_notifyVopsStatus = 8;
    static final int TRANSACTION_onGetCallForwardUncondTimer = 2;
    static final int TRANSACTION_onGetHandoverConfig = 14;
    static final int TRANSACTION_onGetPacketCount = 4;
    static final int TRANSACTION_onGetPacketErrorCount = 5;
    static final int TRANSACTION_onSetCallForwardUncondTimer = 1;
    static final int TRANSACTION_onSetHandoverConfig = 13;
    static final int TRANSACTION_onUTReqFailed = 3;
    static final int TRANSACTION_onVoltePreferenceQueried = 12;
    static final int TRANSACTION_onVoltePreferenceUpdated = 11;
    static final int TRANSACTION_receiveCallTransferResponse = 6;
    static final int TRANSACTION_receiveCancelModifyCallResponse = 7;
    
    public Stub()
    {
      attachInterface(this, "org.codeaurora.ims.internal.IQtiImsExtListener");
    }
    
    public static IQtiImsExtListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
      if ((localIInterface != null) && ((localIInterface instanceof IQtiImsExtListener))) {
        return (IQtiImsExtListener)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 14: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onGetHandoverConfig(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 13: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onSetHandoverConfig(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onVoltePreferenceQueried(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onVoltePreferenceUpdated(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          int i = paramParcel1.readInt();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          notifyParticipantStatusInfo(paramInt2, paramInt1, i, paramParcel2, bool2);
          return true;
        case 9: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          notifySsacStatus(paramInt1, bool2);
          return true;
        case 8: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          paramInt1 = paramParcel1.readInt();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          notifyVopsStatus(paramInt1, bool2);
          return true;
        case 7: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          receiveCancelModifyCallResponse(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          receiveCallTransferResponse(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onGetPacketErrorCount(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          return true;
        case 4: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onGetPacketCount(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          return true;
        case 3: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onUTReqFailed(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
          onGetCallForwardUncondTimer(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("org.codeaurora.ims.internal.IQtiImsExtListener");
        onSetCallForwardUncondTimer(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("org.codeaurora.ims.internal.IQtiImsExtListener");
      return true;
    }
    
    private static class Proxy
      implements IQtiImsExtListener
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
        return "org.codeaurora.ims.internal.IQtiImsExtListener";
      }
      
      public void notifyParticipantStatusInfo(int paramInt1, int paramInt2, int paramInt3, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifySsacStatus(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyVopsStatus(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString, int paramInt8)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          localParcel.writeInt(paramInt5);
          localParcel.writeInt(paramInt6);
          localParcel.writeInt(paramInt7);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt8);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetHandoverConfig(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetPacketCount(int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeLong(paramLong);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetPacketErrorCount(int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeLong(paramLong);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetCallForwardUncondTimer(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
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
      
      public void onSetHandoverConfig(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUTReqFailed(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVoltePreferenceQueried(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVoltePreferenceUpdated(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void receiveCallTransferResponse(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void receiveCancelModifyCallResponse(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("org.codeaurora.ims.internal.IQtiImsExtListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(7, localParcel, null, 1);
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
