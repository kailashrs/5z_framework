package android.media.session;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IActiveSessionsListener
  extends IInterface
{
  public abstract void onActiveSessionsChanged(List<MediaSession.Token> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IActiveSessionsListener
  {
    private static final String DESCRIPTOR = "android.media.session.IActiveSessionsListener";
    static final int TRANSACTION_onActiveSessionsChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.session.IActiveSessionsListener");
    }
    
    public static IActiveSessionsListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.session.IActiveSessionsListener");
      if ((localIInterface != null) && ((localIInterface instanceof IActiveSessionsListener))) {
        return (IActiveSessionsListener)localIInterface;
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
        paramParcel2.writeString("android.media.session.IActiveSessionsListener");
        return true;
      }
      paramParcel1.enforceInterface("android.media.session.IActiveSessionsListener");
      onActiveSessionsChanged(paramParcel1.createTypedArrayList(MediaSession.Token.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IActiveSessionsListener
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
        return "android.media.session.IActiveSessionsListener";
      }
      
      public void onActiveSessionsChanged(List<MediaSession.Token> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.IActiveSessionsListener");
          localParcel.writeTypedList(paramList);
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
