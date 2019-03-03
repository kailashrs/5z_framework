package android.hardware.radio;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IAnnouncementListener
  extends IInterface
{
  public abstract void onListUpdated(List<Announcement> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAnnouncementListener
  {
    private static final String DESCRIPTOR = "android.hardware.radio.IAnnouncementListener";
    static final int TRANSACTION_onListUpdated = 1;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.radio.IAnnouncementListener");
    }
    
    public static IAnnouncementListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.radio.IAnnouncementListener");
      if ((localIInterface != null) && ((localIInterface instanceof IAnnouncementListener))) {
        return (IAnnouncementListener)localIInterface;
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
        paramParcel2.writeString("android.hardware.radio.IAnnouncementListener");
        return true;
      }
      paramParcel1.enforceInterface("android.hardware.radio.IAnnouncementListener");
      onListUpdated(paramParcel1.createTypedArrayList(Announcement.CREATOR));
      return true;
    }
    
    private static class Proxy
      implements IAnnouncementListener
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
        return "android.hardware.radio.IAnnouncementListener";
      }
      
      public void onListUpdated(List<Announcement> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.hardware.radio.IAnnouncementListener");
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
