package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.SubscriptionPlan;

public abstract interface INetworkPolicyManager
  extends IInterface
{
  public abstract void addUidPolicy(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void factoryReset(String paramString)
    throws RemoteException;
  
  public abstract NetworkPolicy[] getNetworkPolicies(String paramString)
    throws RemoteException;
  
  public abstract NetworkQuotaInfo getNetworkQuotaInfo(NetworkState paramNetworkState)
    throws RemoteException;
  
  public abstract boolean getRestrictBackground()
    throws RemoteException;
  
  public abstract int getRestrictBackgroundByCaller()
    throws RemoteException;
  
  public abstract SubscriptionPlan[] getSubscriptionPlans(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getSubscriptionPlansOwner(int paramInt)
    throws RemoteException;
  
  public abstract int getUidPolicy(int paramInt)
    throws RemoteException;
  
  public abstract int[] getUidsWithPolicy(int paramInt)
    throws RemoteException;
  
  public abstract boolean isUidNetworkingBlocked(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onTetheringChanged(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void registerListener(INetworkPolicyListener paramINetworkPolicyListener)
    throws RemoteException;
  
  public abstract void removeUidPolicy(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setDeviceIdleMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNetworkPolicies(NetworkPolicy[] paramArrayOfNetworkPolicy)
    throws RemoteException;
  
  public abstract void setRestrictBackground(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSubscriptionOverride(int paramInt1, int paramInt2, int paramInt3, long paramLong, String paramString)
    throws RemoteException;
  
  public abstract void setSubscriptionPlans(int paramInt, SubscriptionPlan[] paramArrayOfSubscriptionPlan, String paramString)
    throws RemoteException;
  
  public abstract void setUidPolicy(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setWifiMeteredOverride(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void snoozeLimit(NetworkTemplate paramNetworkTemplate)
    throws RemoteException;
  
  public abstract void unregisterListener(INetworkPolicyListener paramINetworkPolicyListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkPolicyManager
  {
    private static final String DESCRIPTOR = "android.net.INetworkPolicyManager";
    static final int TRANSACTION_addUidPolicy = 2;
    static final int TRANSACTION_factoryReset = 22;
    static final int TRANSACTION_getNetworkPolicies = 9;
    static final int TRANSACTION_getNetworkQuotaInfo = 17;
    static final int TRANSACTION_getRestrictBackground = 12;
    static final int TRANSACTION_getRestrictBackgroundByCaller = 14;
    static final int TRANSACTION_getSubscriptionPlans = 18;
    static final int TRANSACTION_getSubscriptionPlansOwner = 20;
    static final int TRANSACTION_getUidPolicy = 4;
    static final int TRANSACTION_getUidsWithPolicy = 5;
    static final int TRANSACTION_isUidNetworkingBlocked = 23;
    static final int TRANSACTION_onTetheringChanged = 13;
    static final int TRANSACTION_registerListener = 6;
    static final int TRANSACTION_removeUidPolicy = 3;
    static final int TRANSACTION_setDeviceIdleMode = 15;
    static final int TRANSACTION_setNetworkPolicies = 8;
    static final int TRANSACTION_setRestrictBackground = 11;
    static final int TRANSACTION_setSubscriptionOverride = 21;
    static final int TRANSACTION_setSubscriptionPlans = 19;
    static final int TRANSACTION_setUidPolicy = 1;
    static final int TRANSACTION_setWifiMeteredOverride = 16;
    static final int TRANSACTION_snoozeLimit = 10;
    static final int TRANSACTION_unregisterListener = 7;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkPolicyManager");
    }
    
    public static INetworkPolicyManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkPolicyManager");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkPolicyManager))) {
        return (INetworkPolicyManager)localIInterface;
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
        Object localObject = null;
        String str = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 23: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          paramInt1 = isUidNetworkingBlocked(paramInt1, bool4);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          factoryReset(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          setSubscriptionOverride(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramParcel1 = getSubscriptionPlansOwner(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          setSubscriptionPlans(paramParcel1.readInt(), (SubscriptionPlan[])paramParcel1.createTypedArray(SubscriptionPlan.CREATOR), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramParcel1 = getSubscriptionPlans(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NetworkState)NetworkState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          paramParcel1 = getNetworkQuotaInfo(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          setWifiMeteredOverride(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setDeviceIdleMode(bool4);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramInt1 = getRestrictBackgroundByCaller();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          str = paramParcel1.readString();
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onTetheringChanged(str, bool4);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramInt1 = getRestrictBackground();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setRestrictBackground(bool4);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NetworkTemplate)NetworkTemplate.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject;
          }
          snoozeLimit(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramParcel1 = getNetworkPolicies(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          setNetworkPolicies((NetworkPolicy[])paramParcel1.createTypedArray(NetworkPolicy.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          unregisterListener(INetworkPolicyListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          registerListener(INetworkPolicyListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramParcel1 = getUidsWithPolicy(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          paramInt1 = getUidPolicy(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          removeUidPolicy(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
          addUidPolicy(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetworkPolicyManager");
        setUidPolicy(paramParcel1.readInt(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.net.INetworkPolicyManager");
      return true;
    }
    
    private static class Proxy
      implements INetworkPolicyManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addUidPolicy(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void factoryReset(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeString(paramString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
        return "android.net.INetworkPolicyManager";
      }
      
      public NetworkPolicy[] getNetworkPolicies(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = (NetworkPolicy[])localParcel2.createTypedArray(NetworkPolicy.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkQuotaInfo getNetworkQuotaInfo(NetworkState paramNetworkState)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          if (paramNetworkState != null)
          {
            localParcel1.writeInt(1);
            paramNetworkState.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkState = (NetworkQuotaInfo)NetworkQuotaInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkState = null;
          }
          return paramNetworkState;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getRestrictBackground()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRestrictBackgroundByCaller()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SubscriptionPlan[] getSubscriptionPlans(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = (SubscriptionPlan[])localParcel2.createTypedArray(SubscriptionPlan.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSubscriptionPlansOwner(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUidPolicy(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public int[] getUidsWithPolicy(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUidNetworkingBlocked(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onTetheringChanged(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerListener(INetworkPolicyListener paramINetworkPolicyListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          if (paramINetworkPolicyListener != null) {
            paramINetworkPolicyListener = paramINetworkPolicyListener.asBinder();
          } else {
            paramINetworkPolicyListener = null;
          }
          localParcel1.writeStrongBinder(paramINetworkPolicyListener);
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
      
      public void removeUidPolicy(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void setDeviceIdleMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNetworkPolicies(NetworkPolicy[] paramArrayOfNetworkPolicy)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeTypedArray(paramArrayOfNetworkPolicy, 0);
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
      
      public void setRestrictBackground(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSubscriptionOverride(int paramInt1, int paramInt2, int paramInt3, long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSubscriptionPlans(int paramInt, SubscriptionPlan[] paramArrayOfSubscriptionPlan, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedArray(paramArrayOfSubscriptionPlan, 0);
          localParcel1.writeString(paramString);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUidPolicy(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void setWifiMeteredOverride(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void snoozeLimit(NetworkTemplate paramNetworkTemplate)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          if (paramNetworkTemplate != null)
          {
            localParcel1.writeInt(1);
            paramNetworkTemplate.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterListener(INetworkPolicyListener paramINetworkPolicyListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkPolicyManager");
          if (paramINetworkPolicyListener != null) {
            paramINetworkPolicyListener = paramINetworkPolicyListener.asBinder();
          } else {
            paramINetworkPolicyListener = null;
          }
          localParcel1.writeStrongBinder(paramINetworkPolicyListener);
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
    }
  }
}
