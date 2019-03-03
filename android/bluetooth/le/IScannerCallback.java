package android.bluetooth.le;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IScannerCallback
  extends IInterface
{
  public abstract void onBatchScanResults(List<ScanResult> paramList)
    throws RemoteException;
  
  public abstract void onFoundOrLost(boolean paramBoolean, ScanResult paramScanResult)
    throws RemoteException;
  
  public abstract void onScanManagerErrorCallback(int paramInt)
    throws RemoteException;
  
  public abstract void onScanResult(ScanResult paramScanResult)
    throws RemoteException;
  
  public abstract void onScannerRegistered(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IScannerCallback
  {
    private static final String DESCRIPTOR = "android.bluetooth.le.IScannerCallback";
    static final int TRANSACTION_onBatchScanResults = 3;
    static final int TRANSACTION_onFoundOrLost = 4;
    static final int TRANSACTION_onScanManagerErrorCallback = 5;
    static final int TRANSACTION_onScanResult = 2;
    static final int TRANSACTION_onScannerRegistered = 1;
    
    public Stub()
    {
      attachInterface(this, "android.bluetooth.le.IScannerCallback");
    }
    
    public static IScannerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.bluetooth.le.IScannerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IScannerCallback))) {
        return (IScannerCallback)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.bluetooth.le.IScannerCallback");
          onScanManagerErrorCallback(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.bluetooth.le.IScannerCallback");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ScanResult)ScanResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onFoundOrLost(bool, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.bluetooth.le.IScannerCallback");
          onBatchScanResults(paramParcel1.createTypedArrayList(ScanResult.CREATOR));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.bluetooth.le.IScannerCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ScanResult)ScanResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onScanResult(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.bluetooth.le.IScannerCallback");
        onScannerRegistered(paramParcel1.readInt(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.bluetooth.le.IScannerCallback");
      return true;
    }
    
    private static class Proxy
      implements IScannerCallback
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
        return "android.bluetooth.le.IScannerCallback";
      }
      
      public void onBatchScanResults(List<ScanResult> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IScannerCallback");
          localParcel.writeTypedList(paramList);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFoundOrLost(boolean paramBoolean, ScanResult paramScanResult)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IScannerCallback");
          localParcel.writeInt(paramBoolean);
          if (paramScanResult != null)
          {
            localParcel.writeInt(1);
            paramScanResult.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onScanManagerErrorCallback(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IScannerCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onScanResult(ScanResult paramScanResult)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IScannerCallback");
          if (paramScanResult != null)
          {
            localParcel.writeInt(1);
            paramScanResult.writeToParcel(localParcel, 0);
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
      
      public void onScannerRegistered(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.bluetooth.le.IScannerCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
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
