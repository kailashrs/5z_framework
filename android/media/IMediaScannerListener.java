package android.media;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMediaScannerListener
  extends IInterface
{
  public abstract void scanCompleted(String paramString, Uri paramUri)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMediaScannerListener
  {
    private static final String DESCRIPTOR = "android.media.IMediaScannerListener";
    static final int TRANSACTION_scanCompleted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.media.IMediaScannerListener");
    }
    
    public static IMediaScannerListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IMediaScannerListener");
      if ((localIInterface != null) && ((localIInterface instanceof IMediaScannerListener))) {
        return (IMediaScannerListener)localIInterface;
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
        paramParcel2.writeString("android.media.IMediaScannerListener");
        return true;
      }
      paramParcel1.enforceInterface("android.media.IMediaScannerListener");
      paramParcel2 = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      scanCompleted(paramParcel2, paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IMediaScannerListener
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
        return "android.media.IMediaScannerListener";
      }
      
      public void scanCompleted(String paramString, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IMediaScannerListener");
          localParcel.writeString(paramString);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
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
