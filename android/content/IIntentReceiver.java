package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IIntentReceiver
  extends IInterface
{
  public abstract void performReceive(Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IIntentReceiver
  {
    private static final String DESCRIPTOR = "android.content.IIntentReceiver";
    static final int TRANSACTION_performReceive = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.IIntentReceiver");
    }
    
    public static IIntentReceiver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.IIntentReceiver");
      if ((localIInterface != null) && ((localIInterface instanceof IIntentReceiver))) {
        return (IIntentReceiver)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.content.IIntentReceiver");
        return true;
      }
      paramParcel1.enforceInterface("android.content.IIntentReceiver");
      paramInt1 = paramParcel1.readInt();
      Bundle localBundle = null;
      if (paramInt1 != 0) {
        paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      paramInt1 = paramParcel1.readInt();
      String str = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      }
      for (;;)
      {
        break;
      }
      boolean bool1;
      if (paramParcel1.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramParcel1.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      performReceive(paramParcel2, paramInt1, str, localBundle, bool1, bool2, paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IIntentReceiver
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
        return "android.content.IIntentReceiver";
      }
      
      public void performReceive(Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.IIntentReceiver");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
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
