package android.print;

import android.os.Binder;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ICancellationSignal.Stub;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;

public abstract interface ILayoutResultCallback
  extends IInterface
{
  public abstract void onLayoutCanceled(int paramInt)
    throws RemoteException;
  
  public abstract void onLayoutFailed(CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public abstract void onLayoutFinished(PrintDocumentInfo paramPrintDocumentInfo, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void onLayoutStarted(ICancellationSignal paramICancellationSignal, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILayoutResultCallback
  {
    private static final String DESCRIPTOR = "android.print.ILayoutResultCallback";
    static final int TRANSACTION_onLayoutCanceled = 4;
    static final int TRANSACTION_onLayoutFailed = 3;
    static final int TRANSACTION_onLayoutFinished = 2;
    static final int TRANSACTION_onLayoutStarted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.print.ILayoutResultCallback");
    }
    
    public static ILayoutResultCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.ILayoutResultCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ILayoutResultCallback))) {
        return (ILayoutResultCallback)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.print.ILayoutResultCallback");
          onLayoutCanceled(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.print.ILayoutResultCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          onLayoutFailed(paramParcel2, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.print.ILayoutResultCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintDocumentInfo)PrintDocumentInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          onLayoutFinished(paramParcel2, bool, paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.print.ILayoutResultCallback");
        onLayoutStarted(ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.print.ILayoutResultCallback");
      return true;
    }
    
    private static class Proxy
      implements ILayoutResultCallback
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
        return "android.print.ILayoutResultCallback";
      }
      
      public void onLayoutCanceled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.ILayoutResultCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLayoutFailed(CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.ILayoutResultCallback");
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLayoutFinished(PrintDocumentInfo paramPrintDocumentInfo, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.ILayoutResultCallback");
          if (paramPrintDocumentInfo != null)
          {
            localParcel.writeInt(1);
            paramPrintDocumentInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
      
      public void onLayoutStarted(ICancellationSignal paramICancellationSignal, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.ILayoutResultCallback");
          if (paramICancellationSignal != null) {
            paramICancellationSignal = paramICancellationSignal.asBinder();
          } else {
            paramICancellationSignal = null;
          }
          localParcel.writeStrongBinder(paramICancellationSignal);
          localParcel.writeInt(paramInt);
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
