package android.service.textclassifier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.textclassifier.TextLinks;

public abstract interface ITextLinksCallback
  extends IInterface
{
  public abstract void onFailure()
    throws RemoteException;
  
  public abstract void onSuccess(TextLinks paramTextLinks)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextLinksCallback
  {
    private static final String DESCRIPTOR = "android.service.textclassifier.ITextLinksCallback";
    static final int TRANSACTION_onFailure = 2;
    static final int TRANSACTION_onSuccess = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.textclassifier.ITextLinksCallback");
    }
    
    public static ITextLinksCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.textclassifier.ITextLinksCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITextLinksCallback))) {
        return (ITextLinksCallback)localIInterface;
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
          paramParcel1.enforceInterface("android.service.textclassifier.ITextLinksCallback");
          onFailure();
          return true;
        }
        paramParcel1.enforceInterface("android.service.textclassifier.ITextLinksCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (TextLinks)TextLinks.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onSuccess(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.service.textclassifier.ITextLinksCallback");
      return true;
    }
    
    private static class Proxy
      implements ITextLinksCallback
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
        return "android.service.textclassifier.ITextLinksCallback";
      }
      
      public void onFailure()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextLinksCallback");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSuccess(TextLinks paramTextLinks)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.textclassifier.ITextLinksCallback");
          if (paramTextLinks != null)
          {
            localParcel.writeInt(1);
            paramTextLinks.writeToParcel(localParcel, 0);
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
