package android.service.trust;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;

public abstract interface ITrustAgentServiceCallback
  extends IInterface
{
  public abstract void addEscrowToken(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract void grantTrust(CharSequence paramCharSequence, long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void isEscrowTokenActive(long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void onConfigureCompleted(boolean paramBoolean, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void removeEscrowToken(long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void revokeTrust()
    throws RemoteException;
  
  public abstract void setManagingTrust(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showKeyguardErrorMessage(CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void unlockUserWithToken(long paramLong, byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITrustAgentServiceCallback
  {
    private static final String DESCRIPTOR = "android.service.trust.ITrustAgentServiceCallback";
    static final int TRANSACTION_addEscrowToken = 5;
    static final int TRANSACTION_grantTrust = 1;
    static final int TRANSACTION_isEscrowTokenActive = 6;
    static final int TRANSACTION_onConfigureCompleted = 4;
    static final int TRANSACTION_removeEscrowToken = 7;
    static final int TRANSACTION_revokeTrust = 2;
    static final int TRANSACTION_setManagingTrust = 3;
    static final int TRANSACTION_showKeyguardErrorMessage = 9;
    static final int TRANSACTION_unlockUserWithToken = 8;
    
    public Stub()
    {
      attachInterface(this, "android.service.trust.ITrustAgentServiceCallback");
    }
    
    public static ITrustAgentServiceCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.trust.ITrustAgentServiceCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITrustAgentServiceCallback))) {
        return (ITrustAgentServiceCallback)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 9: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          showKeyguardErrorMessage(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          unlockUserWithToken(paramParcel1.readLong(), paramParcel1.createByteArray(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          removeEscrowToken(paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          isEscrowTokenActive(paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          addEscrowToken(paramParcel1.createByteArray(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onConfigureCompleted(bool2, paramParcel1.readStrongBinder());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setManagingTrust(bool2);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
          revokeTrust();
          return true;
        }
        paramParcel1.enforceInterface("android.service.trust.ITrustAgentServiceCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = localObject1;
        }
        grantTrust(paramParcel2, paramParcel1.readLong(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.service.trust.ITrustAgentServiceCallback");
      return true;
    }
    
    private static class Proxy
      implements ITrustAgentServiceCallback
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addEscrowToken(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          localParcel.writeByteArray(paramArrayOfByte);
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.trust.ITrustAgentServiceCallback";
      }
      
      public void grantTrust(CharSequence paramCharSequence, long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void isEscrowTokenActive(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onConfigureCompleted(boolean paramBoolean, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          localParcel.writeInt(paramBoolean);
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeEscrowToken(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void revokeTrust()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setManagingTrust(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showKeyguardErrorMessage(CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unlockUserWithToken(long paramLong, byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.trust.ITrustAgentServiceCallback");
          localParcel.writeLong(paramLong);
          localParcel.writeByteArray(paramArrayOfByte);
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
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
