package android.os;

import android.annotation.SystemApi;

@SystemApi
public class UpdateEngine
{
  private static final String TAG = "UpdateEngine";
  private static final String UPDATE_ENGINE_SERVICE = "android.os.UpdateEngineService";
  private IUpdateEngine mUpdateEngine = IUpdateEngine.Stub.asInterface(ServiceManager.getService("android.os.UpdateEngineService"));
  private IUpdateEngineCallback mUpdateEngineCallback = null;
  private final Object mUpdateEngineCallbackLock = new Object();
  
  @SystemApi
  public UpdateEngine() {}
  
  @SystemApi
  public void applyPayload(String paramString, long paramLong1, long paramLong2, String[] paramArrayOfString)
  {
    try
    {
      mUpdateEngine.applyPayload(paramString, paramLong1, paramLong2, paramArrayOfString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean bind(UpdateEngineCallback paramUpdateEngineCallback)
  {
    return bind(paramUpdateEngineCallback, null);
  }
  
  @SystemApi
  public boolean bind(UpdateEngineCallback paramUpdateEngineCallback, Handler paramHandler)
  {
    synchronized (mUpdateEngineCallbackLock)
    {
      IUpdateEngineCallback.Stub local1 = new android/os/UpdateEngine$1;
      local1.<init>(this, paramHandler, paramUpdateEngineCallback);
      mUpdateEngineCallback = local1;
      try
      {
        boolean bool = mUpdateEngine.bind(mUpdateEngineCallback);
        return bool;
      }
      catch (RemoteException paramUpdateEngineCallback)
      {
        throw paramUpdateEngineCallback.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  public void cancel()
  {
    try
    {
      mUpdateEngine.cancel();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void resetStatus()
  {
    try
    {
      mUpdateEngine.resetStatus();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void resume()
  {
    try
    {
      mUpdateEngine.resume();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void suspend()
  {
    try
    {
      mUpdateEngine.suspend();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean unbind()
  {
    synchronized (mUpdateEngineCallbackLock)
    {
      if (mUpdateEngineCallback == null) {
        return true;
      }
      try
      {
        boolean bool = mUpdateEngine.unbind(mUpdateEngineCallback);
        mUpdateEngineCallback = null;
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  public boolean verifyPayloadMetadata(String paramString)
  {
    try
    {
      boolean bool = mUpdateEngine.verifyPayloadApplicable(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public static final class ErrorCodeConstants
  {
    public static final int DOWNLOAD_PAYLOAD_VERIFICATION_ERROR = 12;
    public static final int DOWNLOAD_TRANSFER_ERROR = 9;
    public static final int ERROR = 1;
    public static final int FILESYSTEM_COPIER_ERROR = 4;
    public static final int INSTALL_DEVICE_OPEN_ERROR = 7;
    public static final int KERNEL_DEVICE_OPEN_ERROR = 8;
    public static final int PAYLOAD_HASH_MISMATCH_ERROR = 10;
    public static final int PAYLOAD_MISMATCHED_TYPE_ERROR = 6;
    public static final int PAYLOAD_SIZE_MISMATCH_ERROR = 11;
    public static final int POST_INSTALL_RUNNER_ERROR = 5;
    public static final int SUCCESS = 0;
    public static final int UPDATED_BUT_NOT_ACTIVE = 52;
    
    public ErrorCodeConstants() {}
  }
  
  @SystemApi
  public static final class UpdateStatusConstants
  {
    public static final int ATTEMPTING_ROLLBACK = 8;
    public static final int CHECKING_FOR_UPDATE = 1;
    public static final int DISABLED = 9;
    public static final int DOWNLOADING = 3;
    public static final int FINALIZING = 5;
    public static final int IDLE = 0;
    public static final int REPORTING_ERROR_EVENT = 7;
    public static final int UPDATED_NEED_REBOOT = 6;
    public static final int UPDATE_AVAILABLE = 2;
    public static final int VERIFYING = 4;
    
    public UpdateStatusConstants() {}
  }
}
