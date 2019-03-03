package android.printservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.print.PrintJobInfo;
import android.print.PrinterId;
import java.util.List;

public abstract interface IPrintService
  extends IInterface
{
  public abstract void createPrinterDiscoverySession()
    throws RemoteException;
  
  public abstract void destroyPrinterDiscoverySession()
    throws RemoteException;
  
  public abstract void onPrintJobQueued(PrintJobInfo paramPrintJobInfo)
    throws RemoteException;
  
  public abstract void requestCancelPrintJob(PrintJobInfo paramPrintJobInfo)
    throws RemoteException;
  
  public abstract void requestCustomPrinterIcon(PrinterId paramPrinterId)
    throws RemoteException;
  
  public abstract void setClient(IPrintServiceClient paramIPrintServiceClient)
    throws RemoteException;
  
  public abstract void startPrinterDiscovery(List<PrinterId> paramList)
    throws RemoteException;
  
  public abstract void startPrinterStateTracking(PrinterId paramPrinterId)
    throws RemoteException;
  
  public abstract void stopPrinterDiscovery()
    throws RemoteException;
  
  public abstract void stopPrinterStateTracking(PrinterId paramPrinterId)
    throws RemoteException;
  
  public abstract void validatePrinters(List<PrinterId> paramList)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintService
  {
    private static final String DESCRIPTOR = "android.printservice.IPrintService";
    static final int TRANSACTION_createPrinterDiscoverySession = 4;
    static final int TRANSACTION_destroyPrinterDiscoverySession = 11;
    static final int TRANSACTION_onPrintJobQueued = 3;
    static final int TRANSACTION_requestCancelPrintJob = 2;
    static final int TRANSACTION_requestCustomPrinterIcon = 9;
    static final int TRANSACTION_setClient = 1;
    static final int TRANSACTION_startPrinterDiscovery = 5;
    static final int TRANSACTION_startPrinterStateTracking = 8;
    static final int TRANSACTION_stopPrinterDiscovery = 6;
    static final int TRANSACTION_stopPrinterStateTracking = 10;
    static final int TRANSACTION_validatePrinters = 7;
    
    public Stub()
    {
      attachInterface(this, "android.printservice.IPrintService");
    }
    
    public static IPrintService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.printservice.IPrintService");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintService))) {
        return (IPrintService)localIInterface;
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
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          destroyPrinterDiscoverySession();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          stopPrinterStateTracking(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          requestCustomPrinterIcon(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          startPrinterStateTracking(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          validatePrinters(paramParcel1.createTypedArrayList(PrinterId.CREATOR));
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          stopPrinterDiscovery();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          startPrinterDiscovery(paramParcel1.createTypedArrayList(PrinterId.CREATOR));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          createPrinterDiscoverySession();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrintJobInfo)PrintJobInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onPrintJobQueued(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.printservice.IPrintService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrintJobInfo)PrintJobInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          requestCancelPrintJob(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.printservice.IPrintService");
        setClient(IPrintServiceClient.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.printservice.IPrintService");
      return true;
    }
    
    private static class Proxy
      implements IPrintService
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
      
      public void createPrinterDiscoverySession()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void destroyPrinterDiscoverySession()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.printservice.IPrintService";
      }
      
      public void onPrintJobQueued(PrintJobInfo paramPrintJobInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          if (paramPrintJobInfo != null)
          {
            localParcel.writeInt(1);
            paramPrintJobInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void requestCancelPrintJob(PrintJobInfo paramPrintJobInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          if (paramPrintJobInfo != null)
          {
            localParcel.writeInt(1);
            paramPrintJobInfo.writeToParcel(localParcel, 0);
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
      
      public void requestCustomPrinterIcon(PrinterId paramPrinterId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          if (paramPrinterId != null)
          {
            localParcel.writeInt(1);
            paramPrinterId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setClient(IPrintServiceClient paramIPrintServiceClient)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          if (paramIPrintServiceClient != null) {
            paramIPrintServiceClient = paramIPrintServiceClient.asBinder();
          } else {
            paramIPrintServiceClient = null;
          }
          localParcel.writeStrongBinder(paramIPrintServiceClient);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startPrinterDiscovery(List<PrinterId> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          localParcel.writeTypedList(paramList);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startPrinterStateTracking(PrinterId paramPrinterId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          if (paramPrinterId != null)
          {
            localParcel.writeInt(1);
            paramPrinterId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopPrinterDiscovery()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopPrinterStateTracking(PrinterId paramPrinterId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          if (paramPrinterId != null)
          {
            localParcel.writeInt(1);
            paramPrinterId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void validatePrinters(List<PrinterId> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintService");
          localParcel.writeTypedList(paramList);
          mRemote.transact(7, localParcel, null, 1);
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
