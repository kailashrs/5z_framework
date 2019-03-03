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

public abstract interface IWriteResultCallback
  extends IInterface
{
  public abstract void onWriteCanceled(int paramInt)
    throws RemoteException;
  
  public abstract void onWriteFailed(CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public abstract void onWriteFinished(PageRange[] paramArrayOfPageRange, int paramInt)
    throws RemoteException;
  
  public abstract void onWriteStarted(ICancellationSignal paramICancellationSignal, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWriteResultCallback
  {
    private static final String DESCRIPTOR = "android.print.IWriteResultCallback";
    static final int TRANSACTION_onWriteCanceled = 4;
    static final int TRANSACTION_onWriteFailed = 3;
    static final int TRANSACTION_onWriteFinished = 2;
    static final int TRANSACTION_onWriteStarted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.print.IWriteResultCallback");
    }
    
    public static IWriteResultCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IWriteResultCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IWriteResultCallback))) {
        return (IWriteResultCallback)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.print.IWriteResultCallback");
          onWriteCanceled(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.print.IWriteResultCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          onWriteFailed(paramParcel2, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.print.IWriteResultCallback");
          onWriteFinished((PageRange[])paramParcel1.createTypedArray(PageRange.CREATOR), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.print.IWriteResultCallback");
        onWriteStarted(ICancellationSignal.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.print.IWriteResultCallback");
      return true;
    }
    
    private static class Proxy
      implements IWriteResultCallback
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
        return "android.print.IWriteResultCallback";
      }
      
      public void onWriteCanceled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IWriteResultCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onWriteFailed(CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IWriteResultCallback");
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
      
      public void onWriteFinished(PageRange[] paramArrayOfPageRange, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IWriteResultCallback");
          localParcel.writeTypedArray(paramArrayOfPageRange, 0);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onWriteStarted(ICancellationSignal paramICancellationSignal, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IWriteResultCallback");
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
