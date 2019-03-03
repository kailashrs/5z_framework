package android.print;

import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IPrintSpoolerCallbacks
  extends IInterface
{
  public abstract void customPrinterIconCacheCleared(int paramInt)
    throws RemoteException;
  
  public abstract void onCancelPrintJobResult(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void onCustomPrinterIconCached(int paramInt)
    throws RemoteException;
  
  public abstract void onGetCustomPrinterIconResult(Icon paramIcon, int paramInt)
    throws RemoteException;
  
  public abstract void onGetPrintJobInfoResult(PrintJobInfo paramPrintJobInfo, int paramInt)
    throws RemoteException;
  
  public abstract void onGetPrintJobInfosResult(List<PrintJobInfo> paramList, int paramInt)
    throws RemoteException;
  
  public abstract void onSetPrintJobStateResult(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void onSetPrintJobTagResult(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintSpoolerCallbacks
  {
    private static final String DESCRIPTOR = "android.print.IPrintSpoolerCallbacks";
    static final int TRANSACTION_customPrinterIconCacheCleared = 8;
    static final int TRANSACTION_onCancelPrintJobResult = 2;
    static final int TRANSACTION_onCustomPrinterIconCached = 7;
    static final int TRANSACTION_onGetCustomPrinterIconResult = 6;
    static final int TRANSACTION_onGetPrintJobInfoResult = 5;
    static final int TRANSACTION_onGetPrintJobInfosResult = 1;
    static final int TRANSACTION_onSetPrintJobStateResult = 3;
    static final int TRANSACTION_onSetPrintJobTagResult = 4;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintSpoolerCallbacks");
    }
    
    public static IPrintSpoolerCallbacks asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintSpoolerCallbacks");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintSpoolerCallbacks))) {
        return (IPrintSpoolerCallbacks)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
          customPrinterIconCacheCleared(paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
          onCustomPrinterIconCached(paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Icon)Icon.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          onGetCustomPrinterIconResult(paramParcel2, paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintJobInfo)PrintJobInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          onGetPrintJobInfoResult(paramParcel2, paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onSetPrintJobTagResult(bool3, paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onSetPrintJobStateResult(bool3, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
          bool3 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onCancelPrintJobResult(bool3, paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.print.IPrintSpoolerCallbacks");
        onGetPrintJobInfosResult(paramParcel1.createTypedArrayList(PrintJobInfo.CREATOR), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.print.IPrintSpoolerCallbacks");
      return true;
    }
    
    private static class Proxy
      implements IPrintSpoolerCallbacks
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
      
      public void customPrinterIconCacheCleared(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.print.IPrintSpoolerCallbacks";
      }
      
      public void onCancelPrintJobResult(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCustomPrinterIconCached(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetCustomPrinterIconResult(Icon paramIcon, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          if (paramIcon != null)
          {
            localParcel.writeInt(1);
            paramIcon.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetPrintJobInfoResult(PrintJobInfo paramPrintJobInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          if (paramPrintJobInfo != null)
          {
            localParcel.writeInt(1);
            paramPrintJobInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetPrintJobInfosResult(List<PrintJobInfo> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          localParcel.writeTypedList(paramList);
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetPrintJobStateResult(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetPrintJobTagResult(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerCallbacks");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
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
