package com.android.internal.app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View.OnClickListener;

public class MediaRouteChooserDialogFragment
  extends DialogFragment
{
  private final String ARGUMENT_ROUTE_TYPES = "routeTypes";
  private View.OnClickListener mExtendedSettingsClickListener;
  
  public MediaRouteChooserDialogFragment()
  {
    int i;
    if (MediaRouteChooserDialog.isLightTheme(getContext())) {
      i = 16974130;
    } else {
      i = 16974126;
    }
    setCancelable(true);
    setStyle(0, i);
  }
  
  public int getRouteTypes()
  {
    Bundle localBundle = getArguments();
    int i;
    if (localBundle != null) {
      i = localBundle.getInt("routeTypes");
    } else {
      i = 0;
    }
    return i;
  }
  
  public MediaRouteChooserDialog onCreateChooserDialog(Context paramContext, Bundle paramBundle)
  {
    return new MediaRouteChooserDialog(paramContext, getTheme());
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = onCreateChooserDialog(getActivity(), paramBundle);
    paramBundle.setRouteTypes(getRouteTypes());
    paramBundle.setExtendedSettingsClickListener(mExtendedSettingsClickListener);
    return paramBundle;
  }
  
  public void setExtendedSettingsClickListener(View.OnClickListener paramOnClickListener)
  {
    if (paramOnClickListener != mExtendedSettingsClickListener)
    {
      mExtendedSettingsClickListener = paramOnClickListener;
      MediaRouteChooserDialog localMediaRouteChooserDialog = (MediaRouteChooserDialog)getDialog();
      if (localMediaRouteChooserDialog != null) {
        localMediaRouteChooserDialog.setExtendedSettingsClickListener(paramOnClickListener);
      }
    }
  }
  
  public void setRouteTypes(int paramInt)
  {
    if (paramInt != getRouteTypes())
    {
      Bundle localBundle = getArguments();
      Object localObject = localBundle;
      if (localBundle == null) {
        localObject = new Bundle();
      }
      ((Bundle)localObject).putInt("routeTypes", paramInt);
      setArguments((Bundle)localObject);
      localObject = (MediaRouteChooserDialog)getDialog();
      if (localObject != null) {
        ((MediaRouteChooserDialog)localObject).setRouteTypes(paramInt);
      }
    }
  }
}
