package android.appwidget;

import android.app.IServiceConnection;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;
import com.android.internal.appwidget.IAppWidgetService;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AppWidgetManager
{
  public static final String ACTION_APPWIDGET_BIND = "android.appwidget.action.APPWIDGET_BIND";
  public static final String ACTION_APPWIDGET_CONFIGURE = "android.appwidget.action.APPWIDGET_CONFIGURE";
  public static final String ACTION_APPWIDGET_DELETED = "android.appwidget.action.APPWIDGET_DELETED";
  public static final String ACTION_APPWIDGET_DISABLED = "android.appwidget.action.APPWIDGET_DISABLED";
  public static final String ACTION_APPWIDGET_ENABLED = "android.appwidget.action.APPWIDGET_ENABLED";
  public static final String ACTION_APPWIDGET_HOST_RESTORED = "android.appwidget.action.APPWIDGET_HOST_RESTORED";
  public static final String ACTION_APPWIDGET_OPTIONS_CHANGED = "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS";
  public static final String ACTION_APPWIDGET_PICK = "android.appwidget.action.APPWIDGET_PICK";
  public static final String ACTION_APPWIDGET_RESTORED = "android.appwidget.action.APPWIDGET_RESTORED";
  public static final String ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
  public static final String ACTION_KEYGUARD_APPWIDGET_PICK = "android.appwidget.action.KEYGUARD_APPWIDGET_PICK";
  public static final String EXTRA_APPWIDGET_ID = "appWidgetId";
  public static final String EXTRA_APPWIDGET_IDS = "appWidgetIds";
  public static final String EXTRA_APPWIDGET_OLD_IDS = "appWidgetOldIds";
  public static final String EXTRA_APPWIDGET_OPTIONS = "appWidgetOptions";
  public static final String EXTRA_APPWIDGET_PREVIEW = "appWidgetPreview";
  public static final String EXTRA_APPWIDGET_PROVIDER = "appWidgetProvider";
  public static final String EXTRA_APPWIDGET_PROVIDER_PROFILE = "appWidgetProviderProfile";
  public static final String EXTRA_CATEGORY_FILTER = "categoryFilter";
  public static final String EXTRA_CUSTOM_EXTRAS = "customExtras";
  public static final String EXTRA_CUSTOM_INFO = "customInfo";
  public static final String EXTRA_CUSTOM_SORT = "customSort";
  public static final String EXTRA_HOST_ID = "hostId";
  public static final int INVALID_APPWIDGET_ID = 0;
  public static final String META_DATA_APPWIDGET_PROVIDER = "android.appwidget.provider";
  public static final String OPTION_APPWIDGET_HOST_CATEGORY = "appWidgetCategory";
  public static final String OPTION_APPWIDGET_MAX_HEIGHT = "appWidgetMaxHeight";
  public static final String OPTION_APPWIDGET_MAX_WIDTH = "appWidgetMaxWidth";
  public static final String OPTION_APPWIDGET_MIN_HEIGHT = "appWidgetMinHeight";
  public static final String OPTION_APPWIDGET_MIN_WIDTH = "appWidgetMinWidth";
  private final Context mContext;
  private final DisplayMetrics mDisplayMetrics;
  private final String mPackageName;
  private final IAppWidgetService mService;
  
  public AppWidgetManager(Context paramContext, IAppWidgetService paramIAppWidgetService)
  {
    mContext = paramContext;
    mPackageName = paramContext.getOpPackageName();
    mService = paramIAppWidgetService;
    mDisplayMetrics = paramContext.getResources().getDisplayMetrics();
  }
  
  private boolean bindAppWidgetIdIfAllowed(int paramInt1, int paramInt2, ComponentName paramComponentName, Bundle paramBundle)
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.bindAppWidgetId(mPackageName, paramInt1, paramInt2, paramComponentName, paramBundle);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public static AppWidgetManager getInstance(Context paramContext)
  {
    return (AppWidgetManager)paramContext.getSystemService("appwidget");
  }
  
  public void bindAppWidgetId(int paramInt, ComponentName paramComponentName)
  {
    if (mService == null) {
      return;
    }
    bindAppWidgetId(paramInt, paramComponentName, null);
  }
  
  public void bindAppWidgetId(int paramInt, ComponentName paramComponentName, Bundle paramBundle)
  {
    if (mService == null) {
      return;
    }
    bindAppWidgetIdIfAllowed(paramInt, mContext.getUser(), paramComponentName, paramBundle);
  }
  
  public boolean bindAppWidgetIdIfAllowed(int paramInt, ComponentName paramComponentName)
  {
    if (mService == null) {
      return false;
    }
    return bindAppWidgetIdIfAllowed(paramInt, mContext.getUserId(), paramComponentName, null);
  }
  
  public boolean bindAppWidgetIdIfAllowed(int paramInt, ComponentName paramComponentName, Bundle paramBundle)
  {
    if (mService == null) {
      return false;
    }
    return bindAppWidgetIdIfAllowed(paramInt, mContext.getUserId(), paramComponentName, paramBundle);
  }
  
  public boolean bindAppWidgetIdIfAllowed(int paramInt, UserHandle paramUserHandle, ComponentName paramComponentName, Bundle paramBundle)
  {
    if (mService == null) {
      return false;
    }
    return bindAppWidgetIdIfAllowed(paramInt, paramUserHandle.getIdentifier(), paramComponentName, paramBundle);
  }
  
  public boolean bindRemoteViewsService(Context paramContext, int paramInt1, Intent paramIntent, IServiceConnection paramIServiceConnection, int paramInt2)
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.bindRemoteViewsService(paramContext.getOpPackageName(), paramInt1, paramIntent, paramContext.getIApplicationThread(), paramContext.getActivityToken(), paramIServiceConnection, paramInt2);
      return bool;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  public int[] getAppWidgetIds(ComponentName paramComponentName)
  {
    if (mService == null) {
      return new int[0];
    }
    try
    {
      paramComponentName = mService.getAppWidgetIds(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public AppWidgetProviderInfo getAppWidgetInfo(int paramInt)
  {
    if (mService == null) {
      return null;
    }
    try
    {
      AppWidgetProviderInfo localAppWidgetProviderInfo = mService.getAppWidgetInfo(mPackageName, paramInt);
      if (localAppWidgetProviderInfo != null) {
        localAppWidgetProviderInfo.updateDimensions(mDisplayMetrics);
      }
      return localAppWidgetProviderInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Bundle getAppWidgetOptions(int paramInt)
  {
    if (mService == null) {
      return Bundle.EMPTY;
    }
    try
    {
      Bundle localBundle = mService.getAppWidgetOptions(mPackageName, paramInt);
      return localBundle;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<AppWidgetProviderInfo> getInstalledProviders()
  {
    if (mService == null) {
      return Collections.emptyList();
    }
    return getInstalledProvidersForProfile(1, null, null);
  }
  
  public List<AppWidgetProviderInfo> getInstalledProviders(int paramInt)
  {
    if (mService == null) {
      return Collections.emptyList();
    }
    return getInstalledProvidersForProfile(paramInt, null, null);
  }
  
  public List<AppWidgetProviderInfo> getInstalledProvidersForPackage(String paramString, UserHandle paramUserHandle)
  {
    if (paramString != null)
    {
      if (mService == null) {
        return Collections.emptyList();
      }
      return getInstalledProvidersForProfile(1, paramUserHandle, paramString);
    }
    throw new NullPointerException("A non-null package must be passed to this method. If you want all widgets regardless of package, see getInstalledProvidersForProfile(UserHandle)");
  }
  
  public List<AppWidgetProviderInfo> getInstalledProvidersForProfile(int paramInt, UserHandle paramUserHandle, String paramString)
  {
    if (mService == null) {
      return Collections.emptyList();
    }
    UserHandle localUserHandle = paramUserHandle;
    if (paramUserHandle == null) {
      localUserHandle = mContext.getUser();
    }
    try
    {
      paramString = mService.getInstalledProvidersForProfile(paramInt, localUserHandle.getIdentifier(), paramString);
      if (paramString == null) {
        return Collections.emptyList();
      }
      paramUserHandle = paramString.getList().iterator();
      while (paramUserHandle.hasNext()) {
        ((AppWidgetProviderInfo)paramUserHandle.next()).updateDimensions(mDisplayMetrics);
      }
      paramUserHandle = paramString.getList();
      return paramUserHandle;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public List<AppWidgetProviderInfo> getInstalledProvidersForProfile(UserHandle paramUserHandle)
  {
    if (mService == null) {
      return Collections.emptyList();
    }
    return getInstalledProvidersForProfile(1, paramUserHandle, null);
  }
  
  public boolean hasBindAppWidgetPermission(String paramString)
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.hasBindAppWidgetPermission(paramString, mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean hasBindAppWidgetPermission(String paramString, int paramInt)
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.hasBindAppWidgetPermission(paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isBoundWidgetPackage(String paramString, int paramInt)
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.isBoundWidgetPackage(paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isRequestPinAppWidgetSupported()
  {
    try
    {
      boolean bool = mService.isRequestPinAppWidgetSupported();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void notifyAppWidgetViewDataChanged(int paramInt1, int paramInt2)
  {
    if (mService == null) {
      return;
    }
    notifyAppWidgetViewDataChanged(new int[] { paramInt1 }, paramInt2);
  }
  
  public void notifyAppWidgetViewDataChanged(int[] paramArrayOfInt, int paramInt)
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.notifyAppWidgetViewDataChanged(mPackageName, paramArrayOfInt, paramInt);
      return;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public void partiallyUpdateAppWidget(int paramInt, RemoteViews paramRemoteViews)
  {
    if (mService == null) {
      return;
    }
    partiallyUpdateAppWidget(new int[] { paramInt }, paramRemoteViews);
  }
  
  public void partiallyUpdateAppWidget(int[] paramArrayOfInt, RemoteViews paramRemoteViews)
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.partiallyUpdateAppWidgetIds(mPackageName, paramArrayOfInt, paramRemoteViews);
      return;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public boolean requestPinAppWidget(ComponentName paramComponentName, PendingIntent paramPendingIntent)
  {
    return requestPinAppWidget(paramComponentName, null, paramPendingIntent);
  }
  
  public boolean requestPinAppWidget(ComponentName paramComponentName, Bundle paramBundle, PendingIntent paramPendingIntent)
  {
    try
    {
      IAppWidgetService localIAppWidgetService = mService;
      String str = mPackageName;
      if (paramPendingIntent == null) {
        paramPendingIntent = null;
      } else {
        paramPendingIntent = paramPendingIntent.getIntentSender();
      }
      boolean bool = localIAppWidgetService.requestPinAppWidget(str, paramComponentName, paramBundle, paramPendingIntent);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setBindAppWidgetPermission(String paramString, int paramInt, boolean paramBoolean)
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.setBindAppWidgetPermission(paramString, paramInt, paramBoolean);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setBindAppWidgetPermission(String paramString, boolean paramBoolean)
  {
    if (mService == null) {
      return;
    }
    setBindAppWidgetPermission(paramString, mContext.getUserId(), paramBoolean);
  }
  
  public void updateAppWidget(int paramInt, RemoteViews paramRemoteViews)
  {
    if (mService == null) {
      return;
    }
    updateAppWidget(new int[] { paramInt }, paramRemoteViews);
  }
  
  public void updateAppWidget(ComponentName paramComponentName, RemoteViews paramRemoteViews)
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.updateAppWidgetProvider(paramComponentName, paramRemoteViews);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void updateAppWidget(int[] paramArrayOfInt, RemoteViews paramRemoteViews)
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.updateAppWidgetIds(mPackageName, paramArrayOfInt, paramRemoteViews);
      return;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public void updateAppWidgetOptions(int paramInt, Bundle paramBundle)
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.updateAppWidgetOptions(mPackageName, paramInt, paramBundle);
      return;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle.rethrowFromSystemServer();
    }
  }
  
  public void updateAppWidgetProviderInfo(ComponentName paramComponentName, String paramString)
  {
    if (mService == null) {
      return;
    }
    try
    {
      mService.updateAppWidgetProviderInfo(paramComponentName, paramString);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
}
