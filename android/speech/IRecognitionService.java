package android.speech;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRecognitionService
  extends IInterface
{
  public abstract void cancel(IRecognitionListener paramIRecognitionListener)
    throws RemoteException;
  
  public abstract void startListening(Intent paramIntent, IRecognitionListener paramIRecognitionListener)
    throws RemoteException;
  
  public abstract void stopListening(IRecognitionListener paramIRecognitionListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecognitionService
  {
    private static final String DESCRIPTOR = "android.speech.IRecognitionService";
    static final int TRANSACTION_cancel = 3;
    static final int TRANSACTION_startListening = 1;
    static final int TRANSACTION_stopListening = 2;
    
    public Stub()
    {
      attachInterface(this, "android.speech.IRecognitionService");
    }
    
    public static IRecognitionService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.speech.IRecognitionService");
      if ((localIInterface != null) && ((localIInterface instanceof IRecognitionService))) {
        return (IRecognitionService)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.speech.IRecognitionService");
          cancel(IRecognitionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.speech.IRecognitionService");
          stopListening(IRecognitionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.speech.IRecognitionService");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        startListening(paramParcel2, IRecognitionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.speech.IRecognitionService");
      return true;
    }
    
    private static class Proxy
      implements IRecognitionService
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
      
      public void cancel(IRecognitionListener paramIRecognitionListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionService");
          if (paramIRecognitionListener != null) {
            paramIRecognitionListener = paramIRecognitionListener.asBinder();
          } else {
            paramIRecognitionListener = null;
          }
          localParcel.writeStrongBinder(paramIRecognitionListener);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.speech.IRecognitionService";
      }
      
      public void startListening(Intent paramIntent, IRecognitionListener paramIRecognitionListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionService");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIRecognitionListener != null) {
            paramIntent = paramIRecognitionListener.asBinder();
          } else {
            paramIntent = null;
          }
          localParcel.writeStrongBinder(paramIntent);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopListening(IRecognitionListener paramIRecognitionListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionService");
          if (paramIRecognitionListener != null) {
            paramIRecognitionListener = paramIRecognitionListener.asBinder();
          } else {
            paramIRecognitionListener = null;
          }
          localParcel.writeStrongBinder(paramIRecognitionListener);
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
