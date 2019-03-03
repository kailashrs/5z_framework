package android.service.textclassifier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.textclassifier.TextSelection;

public abstract interface ITextSelectionCallback
  extends IInterface
{
  public abstract void onFailure()
    throws RemoteException;
  
  public abstract void onSuccess(TextSelection paramTextSelection)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextSelectionCallback
  {
    private static final String DESCRIPTOR = "android.service.textclassifier.ITextSelectionCallback";
    static final int TRANSACTION_onFailure = 2;
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.textclassifier.ITextSelectionCallback");
    }
    
    public static ITextSelectionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.textclassifier.ITextSelectionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITextSelectionCallback))) {
        return (ITextSelectionCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.service.textclassifier.ITextSelectionCallback");
          onFailure();
          return true;
        }
        paramParcel1.enforceInterface("android.service.textclassifier.ITextSelectionCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (TextSelection)TextSelection.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onSuccess(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.service.textclassifier.ITextSelectionCallback");
      return true;
    }
    
    private static class Proxy
      implements ITextSelectionCallback
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
        return "android.service.textclassifier.ITextSelectionCallback";
      }
      
      public void onFailure()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextSelectionCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSuccess(TextSelection paramTextSelection)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextSelectionCallback");
          if (paramTextSelection != null)
          {
            localParcel.writeInt(1);
            paramTextSelection.writeToParcel(localParcel, 0);
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
