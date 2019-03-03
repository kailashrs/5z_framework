package android.print;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPrintDocumentAdapter
  extends IInterface
{
  public abstract void finish()
    throws RemoteException;
  
  public abstract void kill(String paramString)
    throws RemoteException;
  
  public abstract void layout(PrintAttributes paramPrintAttributes1, PrintAttributes paramPrintAttributes2, ILayoutResultCallback paramILayoutResultCallback, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract void setObserver(IPrintDocumentAdapterObserver paramIPrintDocumentAdapterObserver)
    throws RemoteException;
  
  public abstract void start()
    throws RemoteException;
  
  public abstract void write(PageRange[] paramArrayOfPageRange, ParcelFileDescriptor paramParcelFileDescriptor, IWriteResultCallback paramIWriteResultCallback, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintDocumentAdapter
  {
    private static final String DESCRIPTOR = "android.print.IPrintDocumentAdapter";
    static final int TRANSACTION_finish = 5;
    static final int TRANSACTION_kill = 6;
    static final int TRANSACTION_layout = 3;
    static final int TRANSACTION_setObserver = 1;
    static final int TRANSACTION_start = 2;
    static final int TRANSACTION_write = 4;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintDocumentAdapter");
    }
    
    public static IPrintDocumentAdapter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintDocumentAdapter");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintDocumentAdapter))) {
        return (IPrintDocumentAdapter)localIInterface;
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
        Object localObject = null;
        PrintAttributes localPrintAttributes = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("android.print.IPrintDocumentAdapter");
          kill(paramParcel1.readString());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.print.IPrintDocumentAdapter");
          finish();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.print.IPrintDocumentAdapter");
          localObject = (PageRange[])paramParcel1.createTypedArray(PageRange.CREATOR);
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localPrintAttributes;
          }
          write((PageRange[])localObject, paramParcel2, IWriteResultCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.print.IPrintDocumentAdapter");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PrintAttributes)PrintAttributes.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localPrintAttributes = (PrintAttributes)PrintAttributes.CREATOR.createFromParcel(paramParcel1);
          } else {
            localPrintAttributes = null;
          }
          ILayoutResultCallback localILayoutResultCallback = ILayoutResultCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          layout(paramParcel2, localPrintAttributes, localILayoutResultCallback, (Bundle)localObject, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.print.IPrintDocumentAdapter");
          start();
          return true;
        }
        paramParcel1.enforceInterface("android.print.IPrintDocumentAdapter");
        setObserver(IPrintDocumentAdapterObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.print.IPrintDocumentAdapter");
      return true;
    }
    
    private static class Proxy
      implements IPrintDocumentAdapter
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
      
      public void finish()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintDocumentAdapter");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.print.IPrintDocumentAdapter";
      }
      
      public void kill(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintDocumentAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void layout(PrintAttributes paramPrintAttributes1, PrintAttributes paramPrintAttributes2, ILayoutResultCallback paramILayoutResultCallback, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintDocumentAdapter");
          if (paramPrintAttributes1 != null)
          {
            localParcel.writeInt(1);
            paramPrintAttributes1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramPrintAttributes2 != null)
          {
            localParcel.writeInt(1);
            paramPrintAttributes2.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramILayoutResultCallback != null) {
            paramPrintAttributes1 = paramILayoutResultCallback.asBinder();
          } else {
            paramPrintAttributes1 = null;
          }
          localParcel.writeStrongBinder(paramPrintAttributes1);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setObserver(IPrintDocumentAdapterObserver paramIPrintDocumentAdapterObserver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintDocumentAdapter");
          if (paramIPrintDocumentAdapterObserver != null) {
            paramIPrintDocumentAdapterObserver = paramIPrintDocumentAdapterObserver.asBinder();
          } else {
            paramIPrintDocumentAdapterObserver = null;
          }
          localParcel.writeStrongBinder(paramIPrintDocumentAdapterObserver);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void start()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintDocumentAdapter");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void write(PageRange[] paramArrayOfPageRange, ParcelFileDescriptor paramParcelFileDescriptor, IWriteResultCallback paramIWriteResultCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintDocumentAdapter");
          localParcel.writeTypedArray(paramArrayOfPageRange, 0);
          if (paramParcelFileDescriptor != null)
          {
            localParcel.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIWriteResultCallback != null) {
            paramArrayOfPageRange = paramIWriteResultCallback.asBinder();
          } else {
            paramArrayOfPageRange = null;
          }
          localParcel.writeStrongBinder(paramArrayOfPageRange);
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
