package android.service.autofill;

import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;

public abstract interface ISaveCallback
  extends IInterface
{
  public abstract void onFailure(CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void onSuccess(IntentSender paramIntentSender)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISaveCallback
  {
    private static final String DESCRIPTOR = "android.service.autofill.ISaveCallback";
    static final int TRANSACTION_onFailure = 2;
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.autofill.ISaveCallback");
    }
    
    public static ISaveCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.autofill.ISaveCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISaveCallback))) {
        return (ISaveCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.service.autofill.ISaveCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onFailure(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.service.autofill.ISaveCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onSuccess(paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.service.autofill.ISaveCallback");
      return true;
    }
    
    private static class Proxy
      implements ISaveCallback
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
        return "android.service.autofill.ISaveCallback";
      }
      
      public void onFailure(CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.autofill.ISaveCallback");
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
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
      
      public void onSuccess(IntentSender paramIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.autofill.ISaveCallback");
          if (paramIntentSender != null)
          {
            localParcel1.writeInt(1);
            paramIntentSender.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
