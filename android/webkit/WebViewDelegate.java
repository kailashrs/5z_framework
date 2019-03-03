package android.webkit;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.app.Application;
import android.app.ResourcesManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.SparseArray;
import android.view.DisplayListCanvas;
import android.view.View;
import android.view.ViewRootImpl;
import com.android.internal.util.ArrayUtils;

@SystemApi
public final class WebViewDelegate
{
  WebViewDelegate() {}
  
  public void addWebViewAssetPath(Context paramContext)
  {
    String str = getLoadedPackageInfoapplicationInfo.sourceDir;
    paramContext = paramContext.getApplicationInfo();
    String[] arrayOfString1 = sharedLibraryFiles;
    if (!ArrayUtils.contains(arrayOfString1, str))
    {
      int i;
      if (arrayOfString1 != null) {
        i = arrayOfString1.length;
      } else {
        i = 0;
      }
      i++;
      String[] arrayOfString2 = new String[i];
      if (arrayOfString1 != null) {
        System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, arrayOfString1.length);
      }
      arrayOfString2[(i - 1)] = str;
      sharedLibraryFiles = arrayOfString2;
      ResourcesManager.getInstance().appendLibAssetForMainAssetPath(paramContext.getBaseResourcePath(), str);
    }
  }
  
  public void callDrawGlFunction(Canvas paramCanvas, long paramLong)
  {
    if ((paramCanvas instanceof DisplayListCanvas))
    {
      ((DisplayListCanvas)paramCanvas).drawGLFunctor2(paramLong, null);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramCanvas.getClass().getName());
    localStringBuilder.append(" is not a DisplayList canvas");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void callDrawGlFunction(Canvas paramCanvas, long paramLong, Runnable paramRunnable)
  {
    if ((paramCanvas instanceof DisplayListCanvas))
    {
      ((DisplayListCanvas)paramCanvas).drawGLFunctor2(paramLong, paramRunnable);
      return;
    }
    paramRunnable = new StringBuilder();
    paramRunnable.append(paramCanvas.getClass().getName());
    paramRunnable.append(" is not a DisplayList canvas");
    throw new IllegalArgumentException(paramRunnable.toString());
  }
  
  public boolean canInvokeDrawGlFunctor(View paramView)
  {
    return true;
  }
  
  public void detachDrawGlFunctor(View paramView, long paramLong)
  {
    paramView = paramView.getViewRootImpl();
    if ((paramLong != 0L) && (paramView != null)) {
      paramView.detachFunctor(paramLong);
    }
  }
  
  public Application getApplication()
  {
    return ActivityThread.currentApplication();
  }
  
  public String getDataDirectorySuffix()
  {
    return WebViewFactory.getDataDirectorySuffix();
  }
  
  public String getErrorString(Context paramContext, int paramInt)
  {
    return LegacyErrorStrings.getString(paramInt, paramContext);
  }
  
  public int getPackageId(Resources paramResources, String paramString)
  {
    paramResources = paramResources.getAssets().getAssignedPackageIdentifiers();
    for (int i = 0; i < paramResources.size(); i++) {
      if (paramString.equals((String)paramResources.valueAt(i))) {
        return paramResources.keyAt(i);
      }
    }
    paramResources = new StringBuilder();
    paramResources.append("Package not found: ");
    paramResources.append(paramString);
    throw new RuntimeException(paramResources.toString());
  }
  
  public void invokeDrawGlFunctor(View paramView, long paramLong, boolean paramBoolean)
  {
    ViewRootImpl.invokeFunctor(paramLong, paramBoolean);
  }
  
  public boolean isMultiProcessEnabled()
  {
    try
    {
      boolean bool = WebViewFactory.getUpdateService().isMultiProcessEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isTraceTagEnabled()
  {
    return Trace.isTagEnabled(16L);
  }
  
  public void setOnTraceEnabledChangeListener(final OnTraceEnabledChangeListener paramOnTraceEnabledChangeListener)
  {
    SystemProperties.addChangeCallback(new Runnable()
    {
      public void run()
      {
        paramOnTraceEnabledChangeListener.onTraceEnabledChange(isTraceTagEnabled());
      }
    });
  }
  
  public static abstract interface OnTraceEnabledChangeListener
  {
    public abstract void onTraceEnabledChange(boolean paramBoolean);
  }
}
