package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface INetworkPolicyListener
  extends IInterface
{
  public abstract void onMeteredIfacesChanged(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void onRestrictBackgroundChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onSubscriptionOverride(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onUidPoliciesChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onUidRulesChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkPolicyListener
  {
    private static final String DESCRIPTOR = "android.net.INetworkPolicyListener";
    static final int TRANSACTION_onMeteredIfacesChanged = 2;
    static final int TRANSACTION_onRestrictBackgroundChanged = 3;
    static final int TRANSACTION_onSubscriptionOverride = 5;
    static final int TRANSACTION_onUidPoliciesChanged = 4;
    static final int TRANSACTION_onUidRulesChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkPolicyListener");
    }
    
    public static INetworkPolicyListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkPolicyListener");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkPolicyListener))) {
        return (INetworkPolicyListener)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyListener");
          onSubscriptionOverride(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyListener");
          onUidPoliciesChanged(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyListener");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          onRestrictBackgroundChanged(bool);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.INetworkPolicyListener");
          onMeteredIfacesChanged(paramParcel1.createStringArray());
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetworkPolicyListener");
        onUidRulesChanged(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.net.INetworkPolicyListener");
      return true;
    }
    
    private static class Proxy
      implements INetworkPolicyListener
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
        return "android.net.INetworkPolicyListener";
      }
      
      public void onMeteredIfacesChanged(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkPolicyListener");
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRestrictBackgroundChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkPolicyListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSubscriptionOverride(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkPolicyListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUidPoliciesChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkPolicyListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUidRulesChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.INetworkPolicyListener");
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
