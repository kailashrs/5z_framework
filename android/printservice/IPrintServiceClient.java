package android.printservice;

import android.content.pm.ParceledListSlice;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.print.PrintJobId;
import android.print.PrintJobInfo;
import android.print.PrinterId;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public abstract interface IPrintServiceClient
  extends IInterface
{
  public abstract PrintJobInfo getPrintJobInfo(PrintJobId paramPrintJobId)
    throws RemoteException;
  
  public abstract List<PrintJobInfo> getPrintJobInfos()
    throws RemoteException;
  
  public abstract void onCustomPrinterIconLoaded(PrinterId paramPrinterId, Icon paramIcon)
    throws RemoteException;
  
  public abstract void onPrintersAdded(ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void onPrintersRemoved(ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract boolean setPrintJobState(PrintJobId paramPrintJobId, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean setPrintJobTag(PrintJobId paramPrintJobId, String paramString)
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
    implements IPrintServiceClient
  {
    private static final String DESCRIPTOR = "android.printservice.IPrintServiceClient";
    static final int TRANSACTION_getPrintJobInfo = 2;
    static final int TRANSACTION_getPrintJobInfos = 1;
    static final int TRANSACTION_onCustomPrinterIconLoaded = 11;
    static final int TRANSACTION_onPrintersAdded = 9;
    static final int TRANSACTION_onPrintersRemoved = 10;
    static final int TRANSACTION_setPrintJobState = 3;
    static final int TRANSACTION_setPrintJobTag = 4;
    static final int TRANSACTION_setProgress = 6;
    static final int TRANSACTION_setStatus = 7;
    static final int TRANSACTION_setStatusRes = 8;
    static final int TRANSACTION_writePrintJobData = 5;
    
    public Stub()
    {
      attachInterface(this, "android.printservice.IPrintServiceClient");
    }
    
    public static IPrintServiceClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.printservice.IPrintServiceClient");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintServiceClient))) {
        return (IPrintServiceClient)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PrinterId)PrinterId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Icon)Icon.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          onCustomPrinterIconLoaded((PrinterId)localObject6, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onPrintersRemoved(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onPrintersAdded(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          setStatusRes((PrintJobId)localObject6, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          setStatus((PrintJobId)localObject6, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject5;
          }
          setProgress((PrintJobId)localObject6, paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject6;
          }
          writePrintJobData(paramParcel2, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject7;
          }
          paramInt1 = setPrintJobTag((PrintJobId)localObject6, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            localObject6 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject6 = localObject8;
          }
          paramInt1 = setPrintJobState((PrintJobId)localObject6, paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          paramParcel1 = getPrintJobInfo(paramParcel1);
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
        paramParcel1.enforceInterface("android.printservice.IPrintServiceClient");
        paramParcel1 = getPrintJobInfos();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.printservice.IPrintServiceClient");
      return true;
    }
    
    private static class Proxy
      implements IPrintServiceClient
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
        return "android.printservice.IPrintServiceClient";
      }
      
      public PrintJobInfo getPrintJobInfo(PrintJobId paramPrintJobId)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public List<PrintJobInfo> getPrintJobInfos()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
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
      
      public void onCustomPrinterIconLoaded(PrinterId paramPrinterId, Icon paramIcon)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          if (paramPrinterId != null)
          {
            localParcel1.writeInt(1);
            paramPrinterId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIcon != null)
          {
            localParcel1.writeInt(1);
            paramIcon.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void onPrintersAdded(ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void onPrintersRemoved(ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPrintJobState(PrintJobId paramPrintJobId, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          boolean bool = true;
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPrintJobTag(PrintJobId paramPrintJobId, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          boolean bool = true;
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setProgress(PrintJobId paramPrintJobId, float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeFloat(paramFloat);
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
      
      public void setStatus(PrintJobId paramPrintJobId, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setStatusRes(PrintJobId paramPrintJobId, int paramInt, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.printservice.IPrintServiceClient");
          if (paramPrintJobId != null)
          {
            localParcel1.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void writePrintJobData(ParcelFileDescriptor paramParcelFileDescriptor, PrintJobId paramPrintJobId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.printservice.IPrintServiceClient");
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
          mRemote.transact(5, localParcel, null, 1);
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
