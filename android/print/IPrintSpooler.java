package android.print;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.List;

public abstract interface IPrintSpooler
  extends IInterface
{
  public abstract void clearCustomPrinterIconCache(IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
    throws RemoteException;
  
  public abstract void createPrintJob(PrintJobInfo paramPrintJobInfo)
    throws RemoteException;
  
  public abstract void getCustomPrinterIcon(PrinterId paramPrinterId, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
    throws RemoteException;
  
  public abstract void getPrintJobInfo(PrintJobId paramPrintJobId, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void getPrintJobInfos(IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, ComponentName paramComponentName, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onCustomPrinterIconLoaded(PrinterId paramPrinterId, Icon paramIcon, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
    throws RemoteException;
  
  public abstract void pruneApprovedPrintServices(List<ComponentName> paramList)
    throws RemoteException;
  
  public abstract void removeObsoletePrintJobs()
    throws RemoteException;
  
  public abstract void setClient(IPrintSpoolerClient paramIPrintSpoolerClient)
    throws RemoteException;
  
  public abstract void setPrintJobCancelling(PrintJobId paramPrintJobId, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPrintJobState(PrintJobId paramPrintJobId, int paramInt1, String paramString, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt2)
    throws RemoteException;
  
  public abstract void setPrintJobTag(PrintJobId paramPrintJobId, String paramString, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
    throws RemoteException;
  
  public abstract void setProgress(PrintJobId paramPrintJobId, float paramFloat)
    throws RemoteException;
  
  public abstract void setStatus(PrintJobId paramPrintJobId, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void setStatusRes(PrintJobId paramPrintJobId, int paramInt, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void writePrintJobData(ParcelFileDescriptor paramParcelFileDescriptor, PrintJobId paramPrintJobId)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintSpooler
  {
    private static final String DESCRIPTOR = "android.print.IPrintSpooler";
    static final int TRANSACTION_clearCustomPrinterIconCache = 11;
    static final int TRANSACTION_createPrintJob = 4;
    static final int TRANSACTION_getCustomPrinterIcon = 10;
    static final int TRANSACTION_getPrintJobInfo = 3;
    static final int TRANSACTION_getPrintJobInfos = 2;
    static final int TRANSACTION_onCustomPrinterIconLoaded = 9;
    static final int TRANSACTION_pruneApprovedPrintServices = 16;
    static final int TRANSACTION_removeObsoletePrintJobs = 1;
    static final int TRANSACTION_setClient = 14;
    static final int TRANSACTION_setPrintJobCancelling = 15;
    static final int TRANSACTION_setPrintJobState = 5;
    static final int TRANSACTION_setPrintJobTag = 12;
    static final int TRANSACTION_setProgress = 6;
    static final int TRANSACTION_setStatus = 7;
    static final int TRANSACTION_setStatusRes = 8;
    static final int TRANSACTION_writePrintJobData = 13;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintSpooler");
    }
    
    public static IPrintSpooler asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintSpooler");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintSpooler))) {
        return (IPrintSpooler)localIInterface;
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
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 16: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          pruneApprovedPrintServices(paramParcel1.createTypedArrayList(ComponentName.CREATOR));
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject12;
          }
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          setPrintJobCancelling(paramParcel2, bool);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          setClient(IPrintSpoolerClient.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          writePrintJobData(paramParcel2, paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          setPrintJobTag(paramParcel2, paramParcel1.readString(), IPrintSpoolerCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          clearCustomPrinterIconCache(IPrintSpoolerCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject3;
          }
          getCustomPrinterIcon(paramParcel2, IPrintSpoolerCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Icon)Icon.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject4;
          }
          onCustomPrinterIconLoaded(paramParcel2, (Icon)localObject1, IPrintSpoolerCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          setStatusRes(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          setStatus(paramParcel2, paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject7;
          }
          setProgress(paramParcel2, paramParcel1.readFloat());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel2 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject8) {
            break;
          }
          setPrintJobState(paramParcel2, paramParcel1.readInt(), paramParcel1.readString(), IPrintSpoolerCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrintJobInfo)PrintJobInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          createPrintJob(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject10;
          }
          getPrintJobInfo(paramParcel2, IPrintSpoolerCallbacks.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.print.IPrintSpooler");
          localObject1 = IPrintSpoolerCallbacks.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel2 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject11) {
            break;
          }
          getPrintJobInfos((IPrintSpoolerCallbacks)localObject1, paramParcel2, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.print.IPrintSpooler");
        removeObsoletePrintJobs();
        return true;
      }
      paramParcel2.writeString("android.print.IPrintSpooler");
      return true;
    }
    
    private static class Proxy
      implements IPrintSpooler
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
      
      public void clearCustomPrinterIconCache(IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramIPrintSpoolerCallbacks != null) {
            paramIPrintSpoolerCallbacks = paramIPrintSpoolerCallbacks.asBinder();
          } else {
            paramIPrintSpoolerCallbacks = null;
          }
          localParcel.writeStrongBinder(paramIPrintSpoolerCallbacks);
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void createPrintJob(PrintJobInfo paramPrintJobInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobInfo != null)
          {
            localParcel.writeInt(1);
            paramPrintJobInfo.writeToParcel(localParcel, 0);
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
      
      public void getCustomPrinterIcon(PrinterId paramPrinterId, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrinterId != null)
          {
            localParcel.writeInt(1);
            paramPrinterId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIPrintSpoolerCallbacks != null) {
            paramPrinterId = paramIPrintSpoolerCallbacks.asBinder();
          } else {
            paramPrinterId = null;
          }
          localParcel.writeStrongBinder(paramPrinterId);
          localParcel.writeInt(paramInt);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.print.IPrintSpooler";
      }
      
      public void getPrintJobInfo(PrintJobId paramPrintJobId, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIPrintSpoolerCallbacks != null) {
            paramPrintJobId = paramIPrintSpoolerCallbacks.asBinder();
          } else {
            paramPrintJobId = null;
          }
          localParcel.writeStrongBinder(paramPrintJobId);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getPrintJobInfos(IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, ComponentName paramComponentName, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramIPrintSpoolerCallbacks != null) {
            paramIPrintSpoolerCallbacks = paramIPrintSpoolerCallbacks.asBinder();
          } else {
            paramIPrintSpoolerCallbacks = null;
          }
          localParcel.writeStrongBinder(paramIPrintSpoolerCallbacks);
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCustomPrinterIconLoaded(PrinterId paramPrinterId, Icon paramIcon, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrinterId != null)
          {
            localParcel.writeInt(1);
            paramPrinterId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIcon != null)
          {
            localParcel.writeInt(1);
            paramIcon.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIPrintSpoolerCallbacks != null) {
            paramPrinterId = paramIPrintSpoolerCallbacks.asBinder();
          } else {
            paramPrinterId = null;
          }
          localParcel.writeStrongBinder(paramPrinterId);
          localParcel.writeInt(paramInt);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void pruneApprovedPrintServices(List<ComponentName> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          localParcel.writeTypedList(paramList);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeObsoletePrintJobs()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setClient(IPrintSpoolerClient paramIPrintSpoolerClient)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramIPrintSpoolerClient != null) {
            paramIPrintSpoolerClient = paramIPrintSpoolerClient.asBinder();
          } else {
            paramIPrintSpoolerClient = null;
          }
          localParcel.writeStrongBinder(paramIPrintSpoolerClient);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPrintJobCancelling(PrintJobId paramPrintJobId, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPrintJobState(PrintJobId paramPrintJobId, int paramInt1, String paramString, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeString(paramString);
          if (paramIPrintSpoolerCallbacks != null) {
            paramPrintJobId = paramIPrintSpoolerCallbacks.asBinder();
          } else {
            paramPrintJobId = null;
          }
          localParcel.writeStrongBinder(paramPrintJobId);
          localParcel.writeInt(paramInt2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPrintJobTag(PrintJobId paramPrintJobId, String paramString, IPrintSpoolerCallbacks paramIPrintSpoolerCallbacks, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          if (paramIPrintSpoolerCallbacks != null) {
            paramPrintJobId = paramIPrintSpoolerCallbacks.asBinder();
          } else {
            paramPrintJobId = null;
          }
          localParcel.writeStrongBinder(paramPrintJobId);
          localParcel.writeInt(paramInt);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setProgress(PrintJobId paramPrintJobId, float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeFloat(paramFloat);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setStatus(PrintJobId paramPrintJobId, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setStatusRes(PrintJobId paramPrintJobId, int paramInt, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
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
      
      public void writePrintJobData(ParcelFileDescriptor paramParcelFileDescriptor, PrintJobId paramPrintJobId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpooler");
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(13, localParcel, null, 1);
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
