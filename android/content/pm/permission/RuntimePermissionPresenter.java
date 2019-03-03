package android.content.pm.permission;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallback;
import android.os.RemoteCallback.OnResultListener;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RuntimePermissionPresenter
{
  public static final String KEY_RESULT = "android.content.pm.permission.RuntimePermissionPresenter.key.result";
  private static final String TAG = "RuntimePermPresenter";
  @GuardedBy("sLock")
  private static RuntimePermissionPresenter sInstance;
  private static final Object sLock = new Object();
  private final RemoteService mRemoteService;
  
  private RuntimePermissionPresenter(Context paramContext)
  {
    mRemoteService = new RemoteService(paramContext);
  }
  
  public static RuntimePermissionPresenter getInstance(Context paramContext)
  {
    synchronized (sLock)
    {
      if (sInstance == null)
      {
        RuntimePermissionPresenter localRuntimePermissionPresenter = new android/content/pm/permission/RuntimePermissionPresenter;
        localRuntimePermissionPresenter.<init>(paramContext.getApplicationContext());
        sInstance = localRuntimePermissionPresenter;
      }
      paramContext = sInstance;
      return paramContext;
    }
  }
  
  public void getAppPermissions(String paramString, OnResultCallback paramOnResultCallback, Handler paramHandler)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramString;
    arg2 = paramOnResultCallback;
    arg3 = paramHandler;
    paramString = mRemoteService.obtainMessage(1, localSomeArgs);
    mRemoteService.processMessage(paramString);
  }
  
  public void revokeRuntimePermission(String paramString1, String paramString2)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramString1;
    arg2 = paramString2;
    paramString1 = mRemoteService.obtainMessage(4, localSomeArgs);
    mRemoteService.processMessage(paramString1);
  }
  
  public static abstract class OnResultCallback
  {
    public OnResultCallback() {}
    
    public void onGetAppPermissions(List<RuntimePermissionPresentationInfo> paramList) {}
  }
  
  private static final class RemoteService
    extends Handler
    implements ServiceConnection
  {
    public static final int MSG_GET_APPS_USING_PERMISSIONS = 2;
    public static final int MSG_GET_APP_PERMISSIONS = 1;
    public static final int MSG_REVOKE_APP_PERMISSIONS = 4;
    public static final int MSG_UNBIND = 3;
    private static final long UNBIND_TIMEOUT_MILLIS = 10000L;
    @GuardedBy("mLock")
    private boolean mBound;
    private final Context mContext;
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private final List<Message> mPendingWork = new ArrayList();
    @GuardedBy("mLock")
    private IRuntimePermissionPresenter mRemoteInstance;
    
    public RemoteService(Context paramContext)
    {
      super(null, false);
      mContext = paramContext;
    }
    
    @GuardedBy("mLock")
    private void scheduleNextMessageIfNeededLocked()
    {
      if ((mBound) && (mRemoteInstance != null) && (!mPendingWork.isEmpty())) {
        sendMessage((Message)mPendingWork.remove(0));
      }
    }
    
    private void scheduleUnbind()
    {
      removeMessages(3);
      sendEmptyMessageDelayed(3, 10000L);
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      Object localObject3;
      if (i != 1)
      {
        switch (i)
        {
        default: 
          break;
        case 4: 
          ??? = (SomeArgs)obj;
          ??? = (String)arg1;
          paramMessage = (String)arg2;
          ((SomeArgs)???).recycle();
          synchronized (mLock)
          {
            localObject3 = mRemoteInstance;
            if (localObject3 == null) {
              return;
            }
            try
            {
              ((IRuntimePermissionPresenter)localObject3).revokeRuntimePermission((String)???, paramMessage);
            }
            catch (RemoteException paramMessage)
            {
              Log.e("RuntimePermPresenter", "Error getting app permissions", paramMessage);
            }
          }
        case 3: 
          synchronized (mLock)
          {
            if (mBound)
            {
              mContext.unbindService(this);
              mBound = false;
            }
            mRemoteInstance = null;
          }
        }
      }
      else
      {
        localObject3 = (SomeArgs)obj;
        paramMessage = (String)arg1;
        ??? = (RuntimePermissionPresenter.OnResultCallback)arg2;
        ??? = (Handler)arg3;
        ((SomeArgs)localObject3).recycle();
      }
      synchronized (mLock)
      {
        localObject3 = mRemoteInstance;
        if (localObject3 == null) {
          return;
        }
        try
        {
          RemoteCallback localRemoteCallback = new android/os/RemoteCallback;
          ??? = new android/content/pm/permission/RuntimePermissionPresenter$RemoteService$1;
          ((1)???).<init>(this, (Handler)???, (RuntimePermissionPresenter.OnResultCallback)???);
          localRemoteCallback.<init>((RemoteCallback.OnResultListener)???, this);
          ((IRuntimePermissionPresenter)localObject3).getAppPermissions(paramMessage, localRemoteCallback);
        }
        catch (RemoteException paramMessage)
        {
          Log.e("RuntimePermPresenter", "Error getting app permissions", paramMessage);
        }
        scheduleUnbind();
        synchronized (mLock)
        {
          scheduleNextMessageIfNeededLocked();
          return;
        }
      }
    }
    
    public void onServiceConnected(ComponentName arg1, IBinder paramIBinder)
    {
      synchronized (mLock)
      {
        mRemoteInstance = IRuntimePermissionPresenter.Stub.asInterface(paramIBinder);
        scheduleNextMessageIfNeededLocked();
        return;
      }
    }
    
    public void onServiceDisconnected(ComponentName arg1)
    {
      synchronized (mLock)
      {
        mRemoteInstance = null;
        return;
      }
    }
    
    public void processMessage(Message paramMessage)
    {
      synchronized (mLock)
      {
        if (!mBound)
        {
          Intent localIntent = new android/content/Intent;
          localIntent.<init>("android.permissionpresenterservice.RuntimePermissionPresenterService");
          localIntent.setPackage(mContext.getPackageManager().getPermissionControllerPackageName());
          mBound = mContext.bindService(localIntent, this, 1);
        }
        mPendingWork.add(paramMessage);
        scheduleNextMessageIfNeededLocked();
        return;
      }
    }
  }
}
