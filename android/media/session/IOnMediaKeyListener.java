package android.media.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.view.KeyEvent;

public abstract interface IOnMediaKeyListener
  extends IInterface
{
  public abstract void onMediaKey(KeyEvent paramKeyEvent, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOnMediaKeyListener
  {
    private static final String DESCRIPTOR = "android.media.session.IOnMediaKeyListener";
    static final int TRANSACTION_onMediaKey = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.session.IOnMediaKeyListener");
    }
    
    public static IOnMediaKeyListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.session.IOnMediaKeyListener");
      if ((localIInterface != null) && ((localIInterface instanceof IOnMediaKeyListener))) {
        return (IOnMediaKeyListener)localIInterface;
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
        paramParcel2.writeString("android.media.session.IOnMediaKeyListener");
        return true;
      }
      paramParcel1.enforceInterface("android.media.session.IOnMediaKeyListener");
      paramInt1 = paramParcel1.readInt();
      Object localObject = null;
      if (paramInt1 != 0) {
        paramParcel2 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = localObject;
      }
      onMediaKey(paramParcel2, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IOnMediaKeyListener
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
        return "android.media.session.IOnMediaKeyListener";
      }
      
      public void onMediaKey(KeyEvent paramKeyEvent, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.IOnMediaKeyListener");
          if (paramKeyEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
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
