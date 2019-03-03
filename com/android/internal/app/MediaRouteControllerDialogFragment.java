package com.android.internal.app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class MediaRouteControllerDialogFragment
  extends DialogFragment
{
  public MediaRouteControllerDialogFragment()
  {
    setCancelable(true);
  }
  
  public MediaRouteControllerDialog onCreateControllerDialog(Context paramContext, Bundle paramBundle)
  {
    return new MediaRouteControllerDialog(paramContext, getTheme());
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    return onCreateControllerDialog(getContext(), paramBundle);
  }
}
