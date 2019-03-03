package com.android.internal.app;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;

public class ResolverTargetActionsDialogFragment
  extends DialogFragment
  implements DialogInterface.OnClickListener
{
  private static final int APP_INFO_INDEX = 1;
  private static final String NAME_KEY = "componentName";
  private static final String PINNED_KEY = "pinned";
  private static final String TITLE_KEY = "title";
  private static final int TOGGLE_PIN_INDEX = 0;
  
  public ResolverTargetActionsDialogFragment() {}
  
  public ResolverTargetActionsDialogFragment(CharSequence paramCharSequence, ComponentName paramComponentName, boolean paramBoolean)
  {
    Bundle localBundle = new Bundle();
    localBundle.putCharSequence("title", paramCharSequence);
    localBundle.putParcelable("componentName", paramComponentName);
    localBundle.putBoolean("pinned", paramBoolean);
    setArguments(localBundle);
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    ComponentName localComponentName = (ComponentName)getArguments().getParcelable("componentName");
    switch (paramInt)
    {
    default: 
      break;
    case 1: 
      startActivity(new Intent().setAction("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", localComponentName.getPackageName(), null)).addFlags(524288));
      break;
    case 0: 
      SharedPreferences localSharedPreferences = ChooserActivity.getPinnedSharedPrefs(getContext());
      paramDialogInterface = localComponentName.flattenToString();
      if (localSharedPreferences.getBoolean(localComponentName.flattenToString(), false)) {
        localSharedPreferences.edit().remove(paramDialogInterface).apply();
      } else {
        localSharedPreferences.edit().putBoolean(paramDialogInterface, true).apply();
      }
      getActivity().recreate();
    }
    dismiss();
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = getArguments();
    int i;
    if (paramBundle.getBoolean("pinned", false)) {
      i = 17236080;
    } else {
      i = 17236079;
    }
    return new AlertDialog.Builder(getContext()).setCancelable(true).setItems(i, this).setTitle(paramBundle.getCharSequence("title")).create();
  }
}
