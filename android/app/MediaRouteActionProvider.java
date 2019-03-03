package android.app;

import android.content.Context;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.media.MediaRouter.SimpleCallback;
import android.util.Log;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import java.lang.ref.WeakReference;

public class MediaRouteActionProvider
  extends ActionProvider
{
  private static final String TAG = "MediaRouteActionProvider";
  private MediaRouteButton mButton;
  private final MediaRouterCallback mCallback;
  private final Context mContext;
  private View.OnClickListener mExtendedSettingsListener;
  private int mRouteTypes;
  private final MediaRouter mRouter;
  
  public MediaRouteActionProvider(Context paramContext)
  {
    super(paramContext);
    mContext = paramContext;
    mRouter = ((MediaRouter)paramContext.getSystemService("media_router"));
    mCallback = new MediaRouterCallback(this);
    setRouteTypes(1);
  }
  
  private void refreshRoute()
  {
    refreshVisibility();
  }
  
  public boolean isVisible()
  {
    return mRouter.isRouteAvailable(mRouteTypes, 1);
  }
  
  public View onCreateActionView()
  {
    throw new UnsupportedOperationException("Use onCreateActionView(MenuItem) instead.");
  }
  
  public View onCreateActionView(MenuItem paramMenuItem)
  {
    if (mButton != null) {
      Log.e("MediaRouteActionProvider", "onCreateActionView: this ActionProvider is already associated with a menu item. Don't reuse MediaRouteActionProvider instances! Abandoning the old one...");
    }
    mButton = new MediaRouteButton(mContext);
    mButton.setRouteTypes(mRouteTypes);
    mButton.setExtendedSettingsClickListener(mExtendedSettingsListener);
    mButton.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
    return mButton;
  }
  
  public boolean onPerformDefaultAction()
  {
    if (mButton != null) {
      return mButton.showDialogInternal();
    }
    return false;
  }
  
  public boolean overridesItemVisibility()
  {
    return true;
  }
  
  public void setExtendedSettingsClickListener(View.OnClickListener paramOnClickListener)
  {
    mExtendedSettingsListener = paramOnClickListener;
    if (mButton != null) {
      mButton.setExtendedSettingsClickListener(paramOnClickListener);
    }
  }
  
  public void setRouteTypes(int paramInt)
  {
    if (mRouteTypes != paramInt)
    {
      if (mRouteTypes != 0) {
        mRouter.removeCallback(mCallback);
      }
      mRouteTypes = paramInt;
      if (paramInt != 0) {
        mRouter.addCallback(paramInt, mCallback, 8);
      }
      refreshRoute();
      if (mButton != null) {
        mButton.setRouteTypes(mRouteTypes);
      }
    }
  }
  
  private static class MediaRouterCallback
    extends MediaRouter.SimpleCallback
  {
    private final WeakReference<MediaRouteActionProvider> mProviderWeak;
    
    public MediaRouterCallback(MediaRouteActionProvider paramMediaRouteActionProvider)
    {
      mProviderWeak = new WeakReference(paramMediaRouteActionProvider);
    }
    
    private void refreshRoute(MediaRouter paramMediaRouter)
    {
      MediaRouteActionProvider localMediaRouteActionProvider = (MediaRouteActionProvider)mProviderWeak.get();
      if (localMediaRouteActionProvider != null) {
        localMediaRouteActionProvider.refreshRoute();
      } else {
        paramMediaRouter.removeCallback(this);
      }
    }
    
    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoute(paramMediaRouter);
    }
    
    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoute(paramMediaRouter);
    }
    
    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoute(paramMediaRouter);
    }
  }
}
