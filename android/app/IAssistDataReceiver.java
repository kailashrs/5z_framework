package android.app;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAssistDataReceiver
  extends IInterface
{
  public abstract void onHandleAssistData(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onHandleAssistScreenshot(Bitmap paramBitmap)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAssistDataReceiver
  {
    private static final String DESCRIPTOR = "android.app.IAssistDataReceiver";
    static final int TRANSACTION_onHandleAssistData = 1;
    static final int TRANSACTION_onHandleAssistScreenshot = 2;
    
    public Stub()
    {
      attachInterface(this, "android.app.IAssistDataReceiver");
    }
    
    public static IAssistDataReceiver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IAssistDataReceiver");
      if ((localIInterface != null) && ((localIInterface instanceof IAssistDataReceiver))) {
        return (IAssistDataReceiver)localIInterface;
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
          paramParcel1.enforceInterface("android.app.IAssistDataReceiver");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onHandleAssistScreenshot(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.IAssistDataReceiver");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onHandleAssistData(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.app.IAssistDataReceiver");
      return true;
    }
    
    private static class Proxy
      implements IAssistDataReceiver
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
        return "android.app.IAssistDataReceiver";
      }
      
      public void onHandleAssistData(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IAssistDataReceiver");
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
      
      public void onHandleAssistScreenshot(Bitmap paramBitmap)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IAssistDataReceiver");
          if (paramBitmap != null)
          {
            localParcel.writeInt(1);
            paramBitmap.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
