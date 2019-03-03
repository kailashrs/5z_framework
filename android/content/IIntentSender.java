package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IIntentSender
  extends IInterface
{
  public abstract void send(int paramInt, Intent paramIntent, String paramString1, IBinder paramIBinder, IIntentReceiver paramIIntentReceiver, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IIntentSender
  {
    private static final String DESCRIPTOR = "android.content.IIntentSender";
    static final int TRANSACTION_send = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.IIntentSender");
    }
    
    public static IIntentSender asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.IIntentSender");
      if ((localIInterface != null) && ((localIInterface instanceof IIntentSender))) {
        return (IIntentSender)localIInterface;
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
        paramParcel2.writeString("android.content.IIntentSender");
        return true;
      }
      paramParcel1.enforceInterface("android.content.IIntentSender");
      paramInt1 = paramParcel1.readInt();
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      String str1 = paramParcel1.readString();
      IBinder localIBinder = paramParcel1.readStrongBinder();
      IIntentReceiver localIIntentReceiver = IIntentReceiver.Stub.asInterface(paramParcel1.readStrongBinder());
      String str2 = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      send(paramInt1, paramParcel2, str1, localIBinder, localIIntentReceiver, str2, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IIntentSender
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
        return "android.content.IIntentSender";
      }
      
      public void send(int paramInt, Intent paramIntent, String paramString1, IBinder paramIBinder, IIntentReceiver paramIIntentReceiver, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.IIntentSender");
          localParcel.writeInt(paramInt);
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString1);
          localParcel.writeStrongBinder(paramIBinder);
          if (paramIIntentReceiver != null) {
            paramIntent = paramIIntentReceiver.asBinder();
          } else {
            paramIntent = null;
          }
          localParcel.writeStrongBinder(paramIntent);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
