package android.os;

public abstract interface IServiceManager
  extends IInterface
{
  public static final int ADD_SERVICE_TRANSACTION = 3;
  public static final int CHECK_SERVICES_TRANSACTION = 5;
  public static final int CHECK_SERVICE_TRANSACTION = 2;
  public static final int DUMP_FLAG_PRIORITY_ALL = 15;
  public static final int DUMP_FLAG_PRIORITY_CRITICAL = 1;
  public static final int DUMP_FLAG_PRIORITY_DEFAULT = 8;
  public static final int DUMP_FLAG_PRIORITY_HIGH = 2;
  public static final int DUMP_FLAG_PRIORITY_NORMAL = 4;
  public static final int DUMP_FLAG_PROTO = 16;
  public static final int GET_SERVICE_TRANSACTION = 1;
  public static final int LIST_SERVICES_TRANSACTION = 4;
  public static final int SET_PERMISSION_CONTROLLER_TRANSACTION = 6;
  public static final String descriptor = "android.os.IServiceManager";
  
  public abstract void addService(String paramString, IBinder paramIBinder, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract IBinder checkService(String paramString)
    throws RemoteException;
  
  public abstract IBinder getService(String paramString)
    throws RemoteException;
  
  public abstract String[] listServices(int paramInt)
    throws RemoteException;
  
  public abstract void setPermissionController(IPermissionController paramIPermissionController)
    throws RemoteException;
}
