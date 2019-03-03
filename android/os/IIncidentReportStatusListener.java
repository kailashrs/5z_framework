package android.os;

public abstract interface IIncidentReportStatusListener
  extends IInterface
{
  public static final int STATUS_FINISHED = 2;
  public static final int STATUS_STARTING = 1;
  
  public abstract void onReportFailed()
    throws RemoteException;
  
  public abstract void onReportFinished()
    throws RemoteException;
  
  public abstract void onReportSectionStatus(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onReportStarted()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IIncidentReportStatusListener
  {
    private static final String DESCRIPTOR = "android.os.IIncidentReportStatusListener";
    static final int TRANSACTION_onReportFailed = 4;
    static final int TRANSACTION_onReportFinished = 3;
    static final int TRANSACTION_onReportSectionStatus = 2;
    static final int TRANSACTION_onReportStarted = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IIncidentReportStatusListener");
    }
    
    public static IIncidentReportStatusListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IIncidentReportStatusListener");
      if ((localIInterface != null) && ((localIInterface instanceof IIncidentReportStatusListener))) {
        return (IIncidentReportStatusListener)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("android.os.IIncidentReportStatusListener");
          onReportFailed();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IIncidentReportStatusListener");
          onReportFinished();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IIncidentReportStatusListener");
          onReportSectionStatus(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.os.IIncidentReportStatusListener");
        onReportStarted();
        return true;
      }
      paramParcel2.writeString("android.os.IIncidentReportStatusListener");
      return true;
    }
    
    private static class Proxy
      implements IIncidentReportStatusListener
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
        return "android.os.IIncidentReportStatusListener";
      }
      
      public void onReportFailed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IIncidentReportStatusListener");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onReportFinished()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IIncidentReportStatusListener");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onReportSectionStatus(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IIncidentReportStatusListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onReportStarted()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.os.IIncidentReportStatusListener");
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
