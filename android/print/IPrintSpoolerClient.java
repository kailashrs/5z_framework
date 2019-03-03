package android.print;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPrintSpoolerClient
  extends IInterface
{
  public abstract void onAllPrintJobsForServiceHandled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void onAllPrintJobsHandled()
    throws RemoteException;
  
  public abstract void onPrintJobQueued(PrintJobInfo paramPrintJobInfo)
    throws RemoteException;
  
  public abstract void onPrintJobStateChanged(PrintJobInfo paramPrintJobInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintSpoolerClient
  {
    private static final String DESCRIPTOR = "android.print.IPrintSpoolerClient";
    static final int TRANSACTION_onAllPrintJobsForServiceHandled = 2;
    static final int TRANSACTION_onAllPrintJobsHandled = 3;
    static final int TRANSACTION_onPrintJobQueued = 1;
    static final int TRANSACTION_onPrintJobStateChanged = 4;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintSpoolerClient");
    }
    
    public static IPrintSpoolerClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintSpoolerClient");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintSpoolerClient))) {
        return (IPrintSpoolerClient)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PrintJobInfo)PrintJobInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onPrintJobStateChanged(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerClient");
          onAllPrintJobsHandled();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.print.IPrintSpoolerClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onAllPrintJobsForServiceHandled(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.print.IPrintSpoolerClient");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (PrintJobInfo)PrintJobInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject2;
        }
        onPrintJobQueued(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.print.IPrintSpoolerClient");
      return true;
    }
    
    private static class Proxy
      implements IPrintSpoolerClient
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
        return "android.print.IPrintSpoolerClient";
      }
      
      public void onAllPrintJobsForServiceHandled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerClient");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
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
      
      public void onAllPrintJobsHandled()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerClient");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrintJobQueued(PrintJobInfo paramPrintJobInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerClient");
          if (paramPrintJobInfo != null)
          {
            localParcel.writeInt(1);
            paramPrintJobInfo.writeToParcel(localParcel, 0);
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
      
      public void onPrintJobStateChanged(PrintJobInfo paramPrintJobInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintSpoolerClient");
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
    }
  }
}
