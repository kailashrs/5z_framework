package android.permissionpresenterservice;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.permission.IRuntimePermissionPresenter.Stub;
import android.content.pm.permission.RuntimePermissionPresentationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallback;
import com.android.internal.os.SomeArgs;
import java.util.List;

@SystemApi
public abstract class RuntimePermissionPresenterService
  extends Service
{
  public static final String SERVICE_INTERFACE = "android.permissionpresenterservice.RuntimePermissionPresenterService";
  private Handler mHandler;
  
  public RuntimePermissionPresenterService() {}
  
  public final void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    mHandler = new MyHandler(paramContext.getMainLooper());
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    new IRuntimePermissionPresenter.Stub()
    {
      public void getAppPermissions(String paramAnonymousString, RemoteCallback paramAnonymousRemoteCallback)
      {
        SomeArgs localSomeArgs = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = paramAnonymousRemoteCallback;
        mHandler.obtainMessage(1, localSomeArgs).sendToTarget();
      }
      
      public void revokeRuntimePermission(String paramAnonymousString1, String paramAnonymousString2)
      {
        SomeArgs localSomeArgs = SomeArgs.obtain();
        arg1 = paramAnonymousString1;
        arg2 = paramAnonymousString2;
        mHandler.obtainMessage(3, localSomeArgs).sendToTarget();
      }
    };
  }
  
  public abstract List<RuntimePermissionPresentationInfo> onGetAppPermissions(String paramString);
  
  public abstract void onRevokeRuntimePermission(String paramString1, String paramString2);
  
  private final class MyHandler
    extends Handler
  {
    public static final int MSG_GET_APPS_USING_PERMISSIONS = 2;
    public static final int MSG_GET_APP_PERMISSIONS = 1;
    public static final int MSG_REVOKE_APP_PERMISSION = 3;
    
    public MyHandler(Looper paramLooper)
    {
      super(null, false);
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      Object localObject1;
      Object localObject2;
      if (i != 1)
      {
        if (i == 3)
        {
          localObject1 = (SomeArgs)obj;
          paramMessage = (String)arg1;
          localObject2 = (String)arg2;
          ((SomeArgs)localObject1).recycle();
          onRevokeRuntimePermission(paramMessage, (String)localObject2);
        }
      }
      else
      {
        localObject1 = (SomeArgs)obj;
        localObject2 = (String)arg1;
        paramMessage = (RemoteCallback)arg2;
        ((SomeArgs)localObject1).recycle();
        localObject1 = onGetAppPermissions((String)localObject2);
        if ((localObject1 != null) && (!((List)localObject1).isEmpty()))
        {
          localObject2 = new Bundle();
          ((Bundle)localObject2).putParcelableList("android.content.pm.permission.RuntimePermissionPresenter.key.result", (List)localObject1);
          paramMessage.sendResult((Bundle)localObject2);
        }
        else
        {
          paramMessage.sendResult(null);
        }
      }
    }
  }
}
