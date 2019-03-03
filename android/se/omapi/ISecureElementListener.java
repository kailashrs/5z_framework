package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ISecureElementListener
  extends IInterface
{
  public static abstract class Stub
    extends Binder
    implements ISecureElementListener
  {
    private static final String DESCRIPTOR = "android.se.omapi.ISecureElementListener";
    
    public Stub()
    {
      attachInterface(this, "android.se.omapi.ISecureElementListener");
    }
    
    public static ISecureElementListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.se.omapi.ISecureElementListener");
      if ((localIInterface != null) && ((localIInterface instanceof ISecureElementListener))) {
        return (ISecureElementListener)localIInterface;
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
      if (paramInt1 != 1598968902) {
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      }
      paramParcel2.writeString("android.se.omapi.ISecureElementListener");
      return true;
    }
    
    private static class Proxy
      implements ISecureElementListener
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
        return "android.se.omapi.ISecureElementListener";
      }
    }
  }
}
