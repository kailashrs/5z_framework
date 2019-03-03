package com.android.internal.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.View.OnClickListener;

public abstract class MediaRouteDialogPresenter
{
  private static final String CHOOSER_FRAGMENT_TAG = "android.app.MediaRouteButton:MediaRouteChooserDialogFragment";
  private static final String CONTROLLER_FRAGMENT_TAG = "android.app.MediaRouteButton:MediaRouteControllerDialogFragment";
  private static final String TAG = "MediaRouter";
  
  public MediaRouteDialogPresenter() {}
  
  public static Dialog createDialog(Context paramContext, int paramInt, View.OnClickListener paramOnClickListener)
  {
    Object localObject = (MediaRouter)paramContext.getSystemService("media_router");
    int i;
    if (MediaRouteChooserDialog.isLightTheme(paramContext)) {
      i = 16974130;
    } else {
      i = 16974126;
    }
    localObject = ((MediaRouter)localObject).getSelectedRoute();
    if ((!((MediaRouter.RouteInfo)localObject).isDefault()) && (((MediaRouter.RouteInfo)localObject).matchesTypes(paramInt))) {
      return new MediaRouteControllerDialog(paramContext, i);
    }
    paramContext = new MediaRouteChooserDialog(paramContext, i);
    paramContext.setRouteTypes(paramInt);
    paramContext.setExtendedSettingsClickListener(paramOnClickListener);
    return paramContext;
  }
  
  public static DialogFragment showDialogFragment(Activity paramActivity, int paramInt, View.OnClickListener paramOnClickListener)
  {
    Object localObject = (MediaRouter)paramActivity.getSystemService("media_router");
    paramActivity = paramActivity.getFragmentManager();
    localObject = ((MediaRouter)localObject).getSelectedRoute();
    if ((!((MediaRouter.RouteInfo)localObject).isDefault()) && (((MediaRouter.RouteInfo)localObject).matchesTypes(paramInt)))
    {
      if (paramActivity.findFragmentByTag("android.app.MediaRouteButton:MediaRouteControllerDialogFragment") != null)
      {
        Log.w("MediaRouter", "showDialog(): Route controller dialog already showing!");
        return null;
      }
      paramOnClickListener = new MediaRouteControllerDialogFragment();
      paramOnClickListener.show(paramActivity, "android.app.MediaRouteButton:MediaRouteControllerDialogFragment");
      return paramOnClickListener;
    }
    if (paramActivity.findFragmentByTag("android.app.MediaRouteButton:MediaRouteChooserDialogFragment") != null)
    {
      Log.w("MediaRouter", "showDialog(): Route chooser dialog already showing!");
      return null;
    }
    localObject = new MediaRouteChooserDialogFragment();
    ((MediaRouteChooserDialogFragment)localObject).setRouteTypes(paramInt);
    ((MediaRouteChooserDialogFragment)localObject).setExtendedSettingsClickListener(paramOnClickListener);
    ((MediaRouteChooserDialogFragment)localObject).show(paramActivity, "android.app.MediaRouteButton:MediaRouteChooserDialogFragment");
    return localObject;
  }
}
