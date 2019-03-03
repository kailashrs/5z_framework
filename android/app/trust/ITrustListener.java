package android.app.trust;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;

public abstract interface ITrustListener
  extends IInterface
{
  public abstract void onTrustChanged(boolean paramBoolean, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onTrustError(CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void onTrustManagedChanged(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITrustListener
  {
    private static final String DESCRIPTOR = "android.app.trust.ITrustListener";
    static final int TRANSACTION_onTrustChanged = 1;
    static final int TRANSACTION_onTrustError = 3;
    static final int TRANSACTION_onTrustManagedChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.app.trust.ITrustListener");
    }
    
    public static ITrustListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.trust.ITrustListener");
      if ((localIInterface != null) && ((localIInterface instanceof ITrustListener))) {
        return (ITrustListener)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.app.trust.ITrustListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onTrustError(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.trust.ITrustListener");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onTrustManagedChanged(bool2, paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.trust.ITrustListener");
        bool2 = bool1;
        if (paramParcel1.readInt() != 0) {
          bool2 = true;
        }
        onTrustChanged(bool2, paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.app.trust.ITrustListener");
      return true;
    }
    
    private static class Proxy
      implements ITrustListener
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
        return "android.app.trust.ITrustListener";
      }
      
      public void onTrustChanged(boolean paramBoolean, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.trust.ITrustListener");
          localParcel.writeInt(paramBoolean);
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
      
      public void onTrustError(CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.trust.ITrustListener");
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTrustManagedChanged(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.trust.ITrustListener");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
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
