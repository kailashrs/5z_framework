package android.service.autofill;

import android.os.Binder;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ICancellationSignal.Stub;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;

public abstract interface IFillCallback
  extends IInterface
{
  public abstract void onCancellable(ICancellationSignal paramICancellationSignal)
    throws RemoteException;
  
  public abstract void onFailure(int paramInt, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void onSuccess(FillResponse paramFillResponse)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IFillCallback
  {
    private static final String DESCRIPTOR = "android.service.autofill.IFillCallback";
    static final int TRANSACTION_onCancellable = 1;
    static final int TRANSACTION_onFailure = 3;
    static final int TRANSACTION_onSuccess = 2;
    
    public Stub()
    {
      attachInterface(this, "android.service.autofill.IFillCallback");
    }
    
    public static IFillCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.autofill.IFillCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IFillCallback))) {
        return (IFillCallback)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.service.autofill.IFillCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onFailure(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.autofill.IFillCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (FillResponse)FillResponse.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onSuccess(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.service.autofill.IFillCallback");
        onCancellable(ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.service.autofill.IFillCallback");
      return true;
    }
    
    private static class Proxy
      implements IFillCallback
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
        return "android.service.autofill.IFillCallback";
      }
      
      public void onCancellable(ICancellationSignal paramICancellationSignal)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.autofill.IFillCallback");
          if (paramICancellationSignal != null) {
            paramICancellationSignal = paramICancellationSignal.asBinder();
          } else {
            paramICancellationSignal = null;
          }
          localParcel1.writeStrongBinder(paramICancellationSignal);
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
      
      public void onFailure(int paramInt, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.autofill.IFillCallback");
          localParcel1.writeInt(paramInt);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
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
      
      public void onSuccess(FillResponse paramFillResponse)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.autofill.IFillCallback");
          if (paramFillResponse != null)
          {
            localParcel1.writeInt(1);
            paramFillResponse.writeToParcel(localParcel1, 0);
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
    }
  }
}
