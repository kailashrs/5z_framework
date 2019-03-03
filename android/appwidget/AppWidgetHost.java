package android.appwidget;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.widget.RemoteViews;
import android.widget.RemoteViews.OnClickHandler;
import com.android.internal.appwidget.IAppWidgetHost.Stub;
import com.android.internal.appwidget.IAppWidgetService;
import com.android.internal.appwidget.IAppWidgetService.Stub;
import java.lang.ref.WeakReference;
import java.util.List;

public class AppWidgetHost
{
  static final int HANDLE_PROVIDERS_CHANGED = 3;
  static final int HANDLE_PROVIDER_CHANGED = 2;
  static final int HANDLE_UPDATE = 1;
  static final int HANDLE_VIEW_DATA_CHANGED = 4;
  static IAppWidgetService sService;
  static boolean sServiceInitialized = false;
  static final Object sServiceLock = new Object();
  private final Callbacks mCallbacks;
  private String mContextOpPackageName;
  private DisplayMetrics mDisplayMetrics;
  private final Handler mHandler;
  private final int mHostId;
  private RemoteViews.OnClickHandler mOnClickHandler;
  private final SparseArray<AppWidgetHostView> mViews = new SparseArray();
  
  public AppWidgetHost(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, null, paramContext.getMainLooper());
  }
  
  public AppWidgetHost(Context paramContext, int paramInt, RemoteViews.OnClickHandler paramOnClickHandler, Looper paramLooper)
  {
    mContextOpPackageName = paramContext.getOpPackageName();
    mHostId = paramInt;
    mOnClickHandler = paramOnClickHandler;
    mHandler = new UpdateHandler(paramLooper);
    mCallbacks = new Callbacks(mHandler);
    mDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    bindService(paramContext);
  }
  
  private static void bindService(Context paramContext)
  {
    synchronized (sServiceLock)
    {
      if (sServiceInitialized) {
        return;
      }
      sServiceInitialized = true;
      if ((!paramContext.getPackageManager().hasSystemFeature("android.software.app_widgets")) && (!paramContext.getResources().getBoolean(17956949))) {
        return;
      }
      sService = IAppWidgetService.Stub.asInterface(ServiceManager.getService("appwidget"));
      return;
    }
  }
  
  public static void deleteAllHosts()
  {
    if (sService == null) {
      return;
    }
    try
    {
      sService.deleteAllHosts();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("system server dead?", localRemoteException);
    }
  }
  
  public int allocateAppWidgetId()
  {
    if (sService == null) {
      return -1;
    }
    try
    {
      int i = sService.allocateAppWidgetId(mContextOpPackageName, mHostId);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("system server dead?", localRemoteException);
    }
  }
  
  protected void clearViews()
  {
    synchronized (mViews)
    {
      mViews.clear();
      return;
    }
  }
  
  public final AppWidgetHostView createView(Context paramContext, int paramInt, AppWidgetProviderInfo arg3)
  {
    if (sService == null) {
      return null;
    }
    paramContext = onCreateView(paramContext, paramInt, ???);
    paramContext.setOnClickHandler(mOnClickHandler);
    paramContext.setAppWidget(paramInt, ???);
    synchronized (mViews)
    {
      mViews.put(paramInt, paramContext);
      try
      {
        ??? = sService.getAppWidgetViews(mContextOpPackageName, paramInt);
        paramContext.updateAppWidget(???);
        return paramContext;
      }
      catch (RemoteException paramContext)
      {
        throw new RuntimeException("system server dead?", paramContext);
      }
    }
  }
  
  public void deleteAppWidgetId(int paramInt)
  {
    if (sService == null) {
      return;
    }
    synchronized (mViews)
    {
      mViews.remove(paramInt);
      try
      {
        sService.deleteAppWidgetId(mContextOpPackageName, paramInt);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        RuntimeException localRuntimeException = new java/lang/RuntimeException;
        localRuntimeException.<init>("system server dead?", localRemoteException);
        throw localRuntimeException;
      }
    }
  }
  
  public void deleteHost()
  {
    if (sService == null) {
      return;
    }
    try
    {
      sService.deleteHost(mContextOpPackageName, mHostId);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("system server dead?", localRemoteException);
    }
  }
  
  public int[] getAppWidgetIds()
  {
    if (sService == null) {
      return new int[0];
    }
    try
    {
      int[] arrayOfInt = sService.getAppWidgetIdsForHost(mContextOpPackageName, mHostId);
      return arrayOfInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("system server dead?", localRemoteException);
    }
  }
  
  protected AppWidgetHostView onCreateView(Context paramContext, int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    return new AppWidgetHostView(paramContext, mOnClickHandler);
  }
  
  protected void onProviderChanged(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    paramAppWidgetProviderInfo.updateDimensions(mDisplayMetrics);
    synchronized (mViews)
    {
      AppWidgetHostView localAppWidgetHostView = (AppWidgetHostView)mViews.get(paramInt);
      if (localAppWidgetHostView != null) {
        localAppWidgetHostView.resetAppWidget(paramAppWidgetProviderInfo);
      }
      return;
    }
  }
  
  protected void onProvidersChanged() {}
  
  public final void startAppWidgetConfigureActivityForResult(Activity paramActivity, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
  {
    if (sService == null) {
      return;
    }
    try
    {
      IntentSender localIntentSender = sService.createAppWidgetConfigIntentSender(mContextOpPackageName, paramInt1, paramInt2);
      if (localIntentSender != null)
      {
        paramActivity.startIntentSenderForResult(localIntentSender, paramInt3, null, 0, 0, 0, paramBundle);
        return;
      }
      paramActivity = new android/content/ActivityNotFoundException;
      paramActivity.<init>();
      throw paramActivity;
    }
    catch (RemoteException paramActivity)
    {
      throw new RuntimeException("system server dead?", paramActivity);
    }
    catch (IntentSender.SendIntentException paramActivity)
    {
      throw new ActivityNotFoundException();
    }
  }
  
  public void startListening()
  {
    if (sService == null) {
      return;
    }
    synchronized (mViews)
    {
      int i = mViews.size();
      Object localObject2 = new int[i];
      int j = 0;
      for (int k = 0; k < i; k++) {
        localObject2[k] = mViews.keyAt(k);
      }
      try
      {
        ??? = sService.startListening(mCallbacks, mContextOpPackageName, mHostId, (int[])localObject2).getList();
        i = ((List)???).size();
        for (k = j; k < i; k++)
        {
          localObject2 = (PendingHostUpdate)((List)???).get(k);
          switch (type)
          {
          default: 
            break;
          case 2: 
            viewDataChanged(appWidgetId, viewId);
            break;
          case 1: 
            onProviderChanged(appWidgetId, widgetInfo);
            break;
          case 0: 
            updateAppWidgetView(appWidgetId, views);
          }
        }
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw new RuntimeException("system server dead?", localRemoteException);
      }
    }
  }
  
  public void stopListening()
  {
    if (sService == null) {
      return;
    }
    try
    {
      sService.stopListening(mContextOpPackageName, mHostId);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException("system server dead?", localRemoteException);
    }
  }
  
  void updateAppWidgetView(int paramInt, RemoteViews paramRemoteViews)
  {
    synchronized (mViews)
    {
      AppWidgetHostView localAppWidgetHostView = (AppWidgetHostView)mViews.get(paramInt);
      if (localAppWidgetHostView != null) {
        localAppWidgetHostView.updateAppWidget(paramRemoteViews);
      }
      return;
    }
  }
  
  void viewDataChanged(int paramInt1, int paramInt2)
  {
    synchronized (mViews)
    {
      AppWidgetHostView localAppWidgetHostView = (AppWidgetHostView)mViews.get(paramInt1);
      if (localAppWidgetHostView != null) {
        localAppWidgetHostView.viewDataChanged(paramInt2);
      }
      return;
    }
  }
  
  static class Callbacks
    extends IAppWidgetHost.Stub
  {
    private final WeakReference<Handler> mWeakHandler;
    
    public Callbacks(Handler paramHandler)
    {
      mWeakHandler = new WeakReference(paramHandler);
    }
    
    private static boolean isLocalBinder()
    {
      boolean bool;
      if (Process.myPid() == Binder.getCallingPid()) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void providerChanged(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
    {
      AppWidgetProviderInfo localAppWidgetProviderInfo = paramAppWidgetProviderInfo;
      if (isLocalBinder())
      {
        localAppWidgetProviderInfo = paramAppWidgetProviderInfo;
        if (paramAppWidgetProviderInfo != null) {
          localAppWidgetProviderInfo = paramAppWidgetProviderInfo.clone();
        }
      }
      paramAppWidgetProviderInfo = (Handler)mWeakHandler.get();
      if (paramAppWidgetProviderInfo == null) {
        return;
      }
      paramAppWidgetProviderInfo.obtainMessage(2, paramInt, 0, localAppWidgetProviderInfo).sendToTarget();
    }
    
    public void providersChanged()
    {
      Handler localHandler = (Handler)mWeakHandler.get();
      if (localHandler == null) {
        return;
      }
      localHandler.obtainMessage(3).sendToTarget();
    }
    
    public void updateAppWidget(int paramInt, RemoteViews paramRemoteViews)
    {
      RemoteViews localRemoteViews = paramRemoteViews;
      if (isLocalBinder())
      {
        localRemoteViews = paramRemoteViews;
        if (paramRemoteViews != null) {
          localRemoteViews = paramRemoteViews.clone();
        }
      }
      paramRemoteViews = (Handler)mWeakHandler.get();
      if (paramRemoteViews == null) {
        return;
      }
      paramRemoteViews.obtainMessage(1, paramInt, 0, localRemoteViews).sendToTarget();
    }
    
    public void viewDataChanged(int paramInt1, int paramInt2)
    {
      Handler localHandler = (Handler)mWeakHandler.get();
      if (localHandler == null) {
        return;
      }
      localHandler.obtainMessage(4, paramInt1, paramInt2).sendToTarget();
    }
  }
  
  class UpdateHandler
    extends Handler
  {
    public UpdateHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 4: 
        viewDataChanged(arg1, arg2);
        break;
      case 3: 
        onProvidersChanged();
        break;
      case 2: 
        onProviderChanged(arg1, (AppWidgetProviderInfo)obj);
        break;
      case 1: 
        updateAppWidgetView(arg1, (RemoteViews)obj);
      }
    }
  }
}
