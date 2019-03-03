package android.app;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstantAppResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import com.android.internal.os.SomeArgs;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SystemApi
public abstract class InstantAppResolverService
  extends Service
{
  private static final boolean DEBUG_INSTANT = Build.IS_DEBUGGABLE;
  public static final String EXTRA_RESOLVE_INFO = "android.app.extra.RESOLVE_INFO";
  public static final String EXTRA_SEQUENCE = "android.app.extra.SEQUENCE";
  private static final String TAG = "PackageManager";
  Handler mHandler;
  
  public InstantAppResolverService() {}
  
  public final void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    mHandler = new ServiceHandler(getLooper());
  }
  
  Looper getLooper()
  {
    return getBaseContext().getMainLooper();
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    new IInstantAppResolver.Stub()
    {
      public void getInstantAppIntentFilterList(Intent paramAnonymousIntent, int[] paramAnonymousArrayOfInt, String paramAnonymousString, IRemoteCallback paramAnonymousIRemoteCallback)
      {
        if (InstantAppResolverService.DEBUG_INSTANT)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("[");
          ((StringBuilder)localObject).append(paramAnonymousString);
          ((StringBuilder)localObject).append("] Phase2 called; posting");
          Slog.v("PackageManager", ((StringBuilder)localObject).toString());
        }
        Object localObject = SomeArgs.obtain();
        arg1 = paramAnonymousIRemoteCallback;
        arg2 = paramAnonymousArrayOfInt;
        arg3 = paramAnonymousString;
        arg4 = paramAnonymousIntent;
        mHandler.obtainMessage(2, paramAnonymousIRemoteCallback).sendToTarget();
      }
      
      public void getInstantAppResolveInfoList(Intent paramAnonymousIntent, int[] paramAnonymousArrayOfInt, String paramAnonymousString, int paramAnonymousInt, IRemoteCallback paramAnonymousIRemoteCallback)
      {
        if (InstantAppResolverService.DEBUG_INSTANT)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("[");
          ((StringBuilder)localObject).append(paramAnonymousString);
          ((StringBuilder)localObject).append("] Phase1 called; posting");
          Slog.v("PackageManager", ((StringBuilder)localObject).toString());
        }
        Object localObject = SomeArgs.obtain();
        arg1 = paramAnonymousIRemoteCallback;
        arg2 = paramAnonymousArrayOfInt;
        arg3 = paramAnonymousString;
        arg4 = paramAnonymousIntent;
        mHandler.obtainMessage(1, paramAnonymousInt, 0, localObject).sendToTarget();
      }
    };
  }
  
  public void onGetInstantAppIntentFilter(Intent paramIntent, int[] paramArrayOfInt, String paramString, InstantAppResolutionCallback paramInstantAppResolutionCallback)
  {
    Log.e("PackageManager", "New onGetInstantAppIntentFilter is not overridden");
    if (paramIntent.isWebIntent()) {
      onGetInstantAppIntentFilter(paramArrayOfInt, paramString, paramInstantAppResolutionCallback);
    } else {
      paramInstantAppResolutionCallback.onInstantAppResolveInfo(Collections.emptyList());
    }
  }
  
  @Deprecated
  public void onGetInstantAppIntentFilter(int[] paramArrayOfInt, String paramString, InstantAppResolutionCallback paramInstantAppResolutionCallback)
  {
    throw new IllegalStateException("Must define onGetInstantAppIntentFilter");
  }
  
  public void onGetInstantAppResolveInfo(Intent paramIntent, int[] paramArrayOfInt, String paramString, InstantAppResolutionCallback paramInstantAppResolutionCallback)
  {
    if (paramIntent.isWebIntent()) {
      onGetInstantAppResolveInfo(paramArrayOfInt, paramString, paramInstantAppResolutionCallback);
    } else {
      paramInstantAppResolutionCallback.onInstantAppResolveInfo(Collections.emptyList());
    }
  }
  
  @Deprecated
  public void onGetInstantAppResolveInfo(int[] paramArrayOfInt, String paramString, InstantAppResolutionCallback paramInstantAppResolutionCallback)
  {
    throw new IllegalStateException("Must define onGetInstantAppResolveInfo");
  }
  
  public static final class InstantAppResolutionCallback
  {
    private final IRemoteCallback mCallback;
    private final int mSequence;
    
    InstantAppResolutionCallback(int paramInt, IRemoteCallback paramIRemoteCallback)
    {
      mCallback = paramIRemoteCallback;
      mSequence = paramInt;
    }
    
    public void onInstantAppResolveInfo(List<InstantAppResolveInfo> paramList)
    {
      Bundle localBundle = new Bundle();
      localBundle.putParcelableList("android.app.extra.RESOLVE_INFO", paramList);
      localBundle.putInt("android.app.extra.SEQUENCE", mSequence);
      try
      {
        mCallback.sendResult(localBundle);
      }
      catch (RemoteException paramList) {}
    }
  }
  
  private final class ServiceHandler
    extends Handler
  {
    public static final int MSG_GET_INSTANT_APP_INTENT_FILTER = 2;
    public static final int MSG_GET_INSTANT_APP_RESOLVE_INFO = 1;
    
    public ServiceHandler(Looper paramLooper)
    {
      super(null, true);
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      Object localObject1;
      Object localObject2;
      Object localObject3;
      Object localObject4;
      switch (i)
      {
      default: 
        paramMessage = new StringBuilder();
        paramMessage.append("Unknown message: ");
        paramMessage.append(i);
        throw new IllegalArgumentException(paramMessage.toString());
      case 2: 
        localObject1 = (SomeArgs)obj;
        localObject2 = (IRemoteCallback)arg1;
        localObject3 = (int[])arg2;
        paramMessage = (String)arg3;
        localObject1 = (Intent)arg4;
        if (InstantAppResolverService.DEBUG_INSTANT)
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("[");
          ((StringBuilder)localObject4).append(paramMessage);
          ((StringBuilder)localObject4).append("] Phase2 request; prefix: ");
          ((StringBuilder)localObject4).append(Arrays.toString((int[])localObject3));
          Slog.d("PackageManager", ((StringBuilder)localObject4).toString());
        }
        onGetInstantAppIntentFilter((Intent)localObject1, (int[])localObject3, paramMessage, new InstantAppResolverService.InstantAppResolutionCallback(-1, (IRemoteCallback)localObject2));
        break;
      case 1: 
        localObject4 = (SomeArgs)obj;
        localObject3 = (IRemoteCallback)arg1;
        localObject1 = (int[])arg2;
        localObject2 = (String)arg3;
        localObject4 = (Intent)arg4;
        i = arg1;
        if (InstantAppResolverService.DEBUG_INSTANT)
        {
          paramMessage = new StringBuilder();
          paramMessage.append("[");
          paramMessage.append((String)localObject2);
          paramMessage.append("] Phase1 request; prefix: ");
          paramMessage.append(Arrays.toString((int[])localObject1));
          Slog.d("PackageManager", paramMessage.toString());
        }
        onGetInstantAppResolveInfo((Intent)localObject4, (int[])localObject1, (String)localObject2, new InstantAppResolverService.InstantAppResolutionCallback(i, (IRemoteCallback)localObject3));
      }
    }
  }
}
