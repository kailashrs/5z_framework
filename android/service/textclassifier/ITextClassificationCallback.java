package android.service.textclassifier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.textclassifier.TextClassification;

public abstract interface ITextClassificationCallback
  extends IInterface
{
  public abstract void onFailure()
    throws RemoteException;
  
  public abstract void onSuccess(TextClassification paramTextClassification)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextClassificationCallback
  {
    private static final String DESCRIPTOR = "android.service.textclassifier.ITextClassificationCallback";
    static final int TRANSACTION_onFailure = 2;
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.textclassifier.ITextClassificationCallback");
    }
    
    public static ITextClassificationCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.textclassifier.ITextClassificationCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITextClassificationCallback))) {
        return (ITextClassificationCallback)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.service.textclassifier.ITextClassificationCallback");
          onFailure();
          return true;
        }
        paramParcel1.enforceInterface("android.service.textclassifier.ITextClassificationCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (TextClassification)TextClassification.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onSuccess(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.service.textclassifier.ITextClassificationCallback");
      return true;
    }
    
    private static class Proxy
      implements ITextClassificationCallback
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
        return "android.service.textclassifier.ITextClassificationCallback";
      }
      
      public void onFailure()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassificationCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSuccess(TextClassification paramTextClassification)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextClassificationCallback");
          if (paramTextClassification != null)
          {
            localParcel.writeInt(1);
            paramTextClassification.writeToParcel(localParcel, 0);
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
