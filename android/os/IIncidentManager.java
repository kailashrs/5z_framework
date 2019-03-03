package android.os;

import java.io.FileDescriptor;

public abstract interface IIncidentManager
  extends IInterface
{
  public abstract void reportIncident(IncidentReportArgs paramIncidentReportArgs)
    throws RemoteException;
  
  public abstract void reportIncidentToStream(IncidentReportArgs paramIncidentReportArgs, IIncidentReportStatusListener paramIIncidentReportStatusListener, FileDescriptor paramFileDescriptor)
    throws RemoteException;
  
  public abstract void systemRunning()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IIncidentManager
  {
    private static final String DESCRIPTOR = "android.os.IIncidentManager";
    static final int TRANSACTION_reportIncident = 1;
    static final int TRANSACTION_reportIncidentToStream = 2;
    static final int TRANSACTION_systemRunning = 3;
    
    public Stub()
    {
      attachInterface(this, "android.os.IIncidentManager");
    }
    
    public static IIncidentManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IIncidentManager");
      if ((localIInterface != null) && ((localIInterface instanceof IIncidentManager))) {
        return (IIncidentManager)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.os.IIncidentManager");
          systemRunning();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IIncidentManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (IncidentReportArgs)IncidentReportArgs.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          reportIncidentToStream(paramParcel2, IIncidentReportStatusListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readRawFileDescriptor());
          return true;
        }
        paramParcel1.enforceInterface("android.os.IIncidentManager");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (IncidentReportArgs)IncidentReportArgs.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        reportIncident(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.os.IIncidentManager");
      return true;
    }
    
    private static class Proxy
      implements IIncidentManager
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
        return "android.os.IIncidentManager";
      }
      
      public void reportIncident(IncidentReportArgs paramIncidentReportArgs)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IIncidentManager");
          if (paramIncidentReportArgs != null)
          {
            localParcel.writeInt(1);
            paramIncidentReportArgs.writeToParcel(localParcel, 0);
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
      
      public void reportIncidentToStream(IncidentReportArgs paramIncidentReportArgs, IIncidentReportStatusListener paramIIncidentReportStatusListener, FileDescriptor paramFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IIncidentManager");
          if (paramIncidentReportArgs != null)
          {
            localParcel.writeInt(1);
            paramIncidentReportArgs.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIIncidentReportStatusListener != null) {
            paramIncidentReportArgs = paramIIncidentReportStatusListener.asBinder();
          } else {
            paramIncidentReportArgs = null;
          }
          localParcel.writeStrongBinder(paramIncidentReportArgs);
          localParcel.writeRawFileDescriptor(paramFileDescriptor);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void systemRunning()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IIncidentManager");
          mRemote.transact(3, localParcel, null, 1);
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
