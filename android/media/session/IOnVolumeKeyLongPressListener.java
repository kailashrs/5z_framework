package android.media.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.KeyEvent;

public abstract interface IOnVolumeKeyLongPressListener
  extends IInterface
{
  public abstract void onVolumeKeyLongPress(KeyEvent paramKeyEvent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOnVolumeKeyLongPressListener
  {
    private static final String DESCRIPTOR = "android.media.session.IOnVolumeKeyLongPressListener";
    static final int TRANSACTION_onVolumeKeyLongPress = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.session.IOnVolumeKeyLongPressListener");
    }
    
    public static IOnVolumeKeyLongPressListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.session.IOnVolumeKeyLongPressListener");
      if ((localIInterface != null) && ((localIInterface instanceof IOnVolumeKeyLongPressListener))) {
        return (IOnVolumeKeyLongPressListener)localIInterface;
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
        paramParcel2.writeString("android.media.session.IOnVolumeKeyLongPressListener");
        return true;
      }
      paramParcel1.enforceInterface("android.media.session.IOnVolumeKeyLongPressListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onVolumeKeyLongPress(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IOnVolumeKeyLongPressListener
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
        return "android.media.session.IOnVolumeKeyLongPressListener";
      }
      
      public void onVolumeKeyLongPress(KeyEvent paramKeyEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.IOnVolumeKeyLongPressListener");
          if (paramKeyEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel, 0);
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
