package android.print;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.printservice.PrintServiceInfo;
import android.printservice.recommendation.IRecommendationsChangeListener;
import android.printservice.recommendation.IRecommendationsChangeListener.Stub;
import android.printservice.recommendation.RecommendationInfo;
import java.util.ArrayList;
import java.util.List;

public abstract interface IPrintManager
  extends IInterface
{
  public abstract void addPrintJobStateChangeListener(IPrintJobStateChangeListener paramIPrintJobStateChangeListener, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void addPrintServiceRecommendationsChangeListener(IRecommendationsChangeListener paramIRecommendationsChangeListener, int paramInt)
    throws RemoteException;
  
  public abstract void addPrintServicesChangeListener(IPrintServicesChangeListener paramIPrintServicesChangeListener, int paramInt)
    throws RemoteException;
  
  public abstract void cancelPrintJob(PrintJobId paramPrintJobId, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void createPrinterDiscoverySession(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, int paramInt)
    throws RemoteException;
  
  public abstract void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, int paramInt)
    throws RemoteException;
  
  public abstract boolean getBindInstantServiceAllowed(int paramInt)
    throws RemoteException;
  
  public abstract Icon getCustomPrinterIcon(PrinterId paramPrinterId, int paramInt)
    throws RemoteException;
  
  public abstract PrintJobInfo getPrintJobInfo(PrintJobId paramPrintJobId, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract List<PrintJobInfo> getPrintJobInfos(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract List<RecommendationInfo> getPrintServiceRecommendations(int paramInt)
    throws RemoteException;
  
  public abstract List<PrintServiceInfo> getPrintServices(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract Bundle print(String paramString1, IPrintDocumentAdapter paramIPrintDocumentAdapter, PrintAttributes paramPrintAttributes, String paramString2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void removePrintJobStateChangeListener(IPrintJobStateChangeListener paramIPrintJobStateChangeListener, int paramInt)
    throws RemoteException;
  
  public abstract void removePrintServiceRecommendationsChangeListener(IRecommendationsChangeListener paramIRecommendationsChangeListener, int paramInt)
    throws RemoteException;
  
  public abstract void removePrintServicesChangeListener(IPrintServicesChangeListener paramIPrintServicesChangeListener, int paramInt)
    throws RemoteException;
  
  public abstract void restartPrintJob(PrintJobId paramPrintJobId, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setBindInstantServiceAllowed(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPrintServiceEnabled(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void startPrinterDiscovery(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, List<PrinterId> paramList, int paramInt)
    throws RemoteException;
  
  public abstract void startPrinterStateTracking(PrinterId paramPrinterId, int paramInt)
    throws RemoteException;
  
  public abstract void stopPrinterDiscovery(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, int paramInt)
    throws RemoteException;
  
  public abstract void stopPrinterStateTracking(PrinterId paramPrinterId, int paramInt)
    throws RemoteException;
  
  public abstract void validatePrinters(List<PrinterId> paramList, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintManager
  {
    private static final String DESCRIPTOR = "android.print.IPrintManager";
    static final int TRANSACTION_addPrintJobStateChangeListener = 6;
    static final int TRANSACTION_addPrintServiceRecommendationsChangeListener = 12;
    static final int TRANSACTION_addPrintServicesChangeListener = 8;
    static final int TRANSACTION_cancelPrintJob = 4;
    static final int TRANSACTION_createPrinterDiscoverySession = 15;
    static final int TRANSACTION_destroyPrinterDiscoverySession = 22;
    static final int TRANSACTION_getBindInstantServiceAllowed = 23;
    static final int TRANSACTION_getCustomPrinterIcon = 20;
    static final int TRANSACTION_getPrintJobInfo = 2;
    static final int TRANSACTION_getPrintJobInfos = 1;
    static final int TRANSACTION_getPrintServiceRecommendations = 14;
    static final int TRANSACTION_getPrintServices = 10;
    static final int TRANSACTION_print = 3;
    static final int TRANSACTION_removePrintJobStateChangeListener = 7;
    static final int TRANSACTION_removePrintServiceRecommendationsChangeListener = 13;
    static final int TRANSACTION_removePrintServicesChangeListener = 9;
    static final int TRANSACTION_restartPrintJob = 5;
    static final int TRANSACTION_setBindInstantServiceAllowed = 24;
    static final int TRANSACTION_setPrintServiceEnabled = 11;
    static final int TRANSACTION_startPrinterDiscovery = 16;
    static final int TRANSACTION_startPrinterStateTracking = 19;
    static final int TRANSACTION_stopPrinterDiscovery = 17;
    static final int TRANSACTION_stopPrinterStateTracking = 21;
    static final int TRANSACTION_validatePrinters = 18;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintManager");
    }
    
    public static IPrintManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintManager");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintManager))) {
        return (IPrintManager)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        String str = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        IPrintDocumentAdapter localIPrintDocumentAdapter = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 24: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setBindInstantServiceAllowed(paramInt1, bool2);
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          paramInt1 = getBindInstantServiceAllowed(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          destroyPrinterDiscoverySession(IPrinterDiscoveryObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localIPrintDocumentAdapter;
          }
          stopPrinterStateTracking((PrinterId)localObject5, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = str;
          }
          paramParcel1 = getCustomPrinterIcon((PrinterId)localObject5, paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject1;
          }
          startPrinterStateTracking((PrinterId)localObject5, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          validatePrinters(paramParcel1.createTypedArrayList(PrinterId.CREATOR), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          stopPrinterDiscovery(IPrinterDiscoveryObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          startPrinterDiscovery(IPrinterDiscoveryObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createTypedArrayList(PrinterId.CREATOR), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          createPrinterDiscoverySession(IPrinterDiscoveryObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          paramParcel1 = getPrintServiceRecommendations(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          removePrintServiceRecommendationsChangeListener(IRecommendationsChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          addPrintServiceRecommendationsChangeListener(IRecommendationsChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject2;
          }
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setPrintServiceEnabled((ComponentName)localObject5, bool2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          paramParcel1 = getPrintServices(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          removePrintServicesChangeListener(IPrintServicesChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          addPrintServicesChangeListener(IPrintServicesChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          removePrintJobStateChangeListener(IPrintJobStateChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          addPrintJobStateChangeListener(IPrintJobStateChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject3;
          }
          restartPrintJob((PrintJobId)localObject5, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject4;
          }
          cancelPrintJob((PrintJobId)localObject5, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          str = paramParcel1.readString();
          localIPrintDocumentAdapter = IPrintDocumentAdapter.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject5 = (PrintAttributes)PrintAttributes.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = print(str, localIPrintDocumentAdapter, (PrintAttributes)localObject5, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.print.IPrintManager");
          if (paramParcel1.readInt() != 0) {
            localObject5 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject6;
          }
          paramParcel1 = getPrintJobInfo((PrintJobId)localObject5, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        }
        paramParcel1.enforceInterface("android.print.IPrintManager");
        paramParcel1 = getPrintJobInfos(paramParcel1.readInt(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.print.IPrintManager");
      return true;
    }
    
    private static class Proxy
      implements IPrintManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addPrintJobStateChangeListener(IPrintJobStateChangeListener paramIPrintJobStateChangeListener, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrintJobStateChangeListener != null) {
            paramIPrintJobStateChangeListener = paramIPrintJobStateChangeListener.asBinder();
          } else {
            paramIPrintJobStateChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIPrintJobStateChangeListener);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addPrintServiceRecommendationsChangeListener(IRecommendationsChangeListener paramIRecommendationsChangeListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIRecommendationsChangeListener != null) {
            paramIRecommendationsChangeListener = paramIRecommendationsChangeListener.asBinder();
          } else {
            paramIRecommendationsChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIRecommendationsChangeListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addPrintServicesChangeListener(IPrintServicesChangeListener paramIPrintServicesChangeListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrintServicesChangeListener != null) {
            paramIPrintServicesChangeListener = paramIPrintServicesChangeListener.asBinder();
          } else {
            paramIPrintServicesChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIPrintServicesChangeListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void cancelPrintJob(PrintJobId paramPrintJobId, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void createPrinterDiscoverySession(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrinterDiscoveryObserver != null) {
            paramIPrinterDiscoveryObserver = paramIPrinterDiscoveryObserver.asBinder();
          } else {
            paramIPrinterDiscoveryObserver = null;
          }
          localParcel1.writeStrongBinder(paramIPrinterDiscoveryObserver);
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrinterDiscoveryObserver != null) {
            paramIPrinterDiscoveryObserver = paramIPrinterDiscoveryObserver.asBinder();
          } else {
            paramIPrinterDiscoveryObserver = null;
          }
          localParcel1.writeStrongBinder(paramIPrinterDiscoveryObserver);
          localParcel1.writeInt(paramInt);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getBindInstantServiceAllowed(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Icon getCustomPrinterIcon(PrinterId paramPrinterId, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramPrinterId != null)
          {
            localParcel1.writeInt(1);
            paramPrinterId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPrinterId = (Icon)Icon.CREATOR.createFromParcel(localParcel2);
          } else {
            paramPrinterId = null;
          }
          return paramPrinterId;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.print.IPrintManager";
      }
      
      public PrintJobInfo getPrintJobInfo(PrintJobId paramPrintJobId, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPrintJobId = (PrintJobInfo)PrintJobInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramPrintJobId = null;
          }
          return paramPrintJobId;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PrintJobInfo> getPrintJobInfos(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(PrintJobInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<RecommendationInfo> getPrintServiceRecommendations(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(RecommendationInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PrintServiceInfo> getPrintServices(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(PrintServiceInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle print(String paramString1, IPrintDocumentAdapter paramIPrintDocumentAdapter, PrintAttributes paramPrintAttributes, String paramString2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          localParcel1.writeString(paramString1);
          Object localObject = null;
          if (paramIPrintDocumentAdapter != null) {
            paramString1 = paramIPrintDocumentAdapter.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
          if (paramPrintAttributes != null)
          {
            localParcel1.writeInt(1);
            paramPrintAttributes.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = localObject;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removePrintJobStateChangeListener(IPrintJobStateChangeListener paramIPrintJobStateChangeListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrintJobStateChangeListener != null) {
            paramIPrintJobStateChangeListener = paramIPrintJobStateChangeListener.asBinder();
          } else {
            paramIPrintJobStateChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIPrintJobStateChangeListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removePrintServiceRecommendationsChangeListener(IRecommendationsChangeListener paramIRecommendationsChangeListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIRecommendationsChangeListener != null) {
            paramIRecommendationsChangeListener = paramIRecommendationsChangeListener.asBinder();
          } else {
            paramIRecommendationsChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIRecommendationsChangeListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removePrintServicesChangeListener(IPrintServicesChangeListener paramIPrintServicesChangeListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrintServicesChangeListener != null) {
            paramIPrintServicesChangeListener = paramIPrintServicesChangeListener.asBinder();
          } else {
            paramIPrintServicesChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIPrintServicesChangeListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void restartPrintJob(PrintJobId paramPrintJobId, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBindInstantServiceAllowed(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPrintServiceEnabled(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startPrinterDiscovery(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, List<PrinterId> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrinterDiscoveryObserver != null) {
            paramIPrinterDiscoveryObserver = paramIPrinterDiscoveryObserver.asBinder();
          } else {
            paramIPrinterDiscoveryObserver = null;
          }
          localParcel1.writeStrongBinder(paramIPrinterDiscoveryObserver);
          localParcel1.writeTypedList(paramList);
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startPrinterStateTracking(PrinterId paramPrinterId, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramPrinterId != null)
          {
            localParcel1.writeInt(1);
            paramPrinterId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopPrinterDiscovery(IPrinterDiscoveryObserver paramIPrinterDiscoveryObserver, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramIPrinterDiscoveryObserver != null) {
            paramIPrinterDiscoveryObserver = paramIPrinterDiscoveryObserver.asBinder();
          } else {
            paramIPrinterDiscoveryObserver = null;
          }
          localParcel1.writeStrongBinder(paramIPrinterDiscoveryObserver);
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopPrinterStateTracking(PrinterId paramPrinterId, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          if (paramPrinterId != null)
          {
            localParcel1.writeInt(1);
            paramPrinterId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void validatePrinters(List<PrinterId> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.print.IPrintManager");
          localParcel1.writeTypedList(paramList);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
