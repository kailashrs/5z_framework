package com.android.internal.globalactions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListView;
import com.android.internal.app.AlertController;
import com.android.internal.app.AlertController.AlertParams;
import java.util.List;

public final class ActionsDialog
  extends Dialog
  implements DialogInterface
{
  private final ActionsAdapter mAdapter;
  private final AlertController mAlert = AlertController.create(mContext, this, getWindow());
  private final Context mContext = getContext();
  
  public ActionsDialog(Context paramContext, AlertController.AlertParams paramAlertParams)
  {
    super(paramContext, getDialogTheme(paramContext));
    mAdapter = ((ActionsAdapter)mAdapter);
    paramAlertParams.apply(mAlert);
  }
  
  private static int getDialogTheme(Context paramContext)
  {
    TypedValue localTypedValue = new TypedValue();
    paramContext.getTheme().resolveAttribute(16843529, localTypedValue, true);
    return resourceId;
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (paramAccessibilityEvent.getEventType() == 32) {
      for (int i = 0; i < mAdapter.getCount(); i++)
      {
        CharSequence localCharSequence = mAdapter.getItem(i).getLabelForAccessibility(getContext());
        if (localCharSequence != null) {
          paramAccessibilityEvent.getText().add(localCharSequence);
        }
      }
    }
    return super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
  }
  
  public ListView getListView()
  {
    return mAlert.getListView();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mAlert.installContent();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (mAlert.onKeyDown(paramInt, paramKeyEvent)) {
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (mAlert.onKeyUp(paramInt, paramKeyEvent)) {
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  protected void onStart()
  {
    super.setCanceledOnTouchOutside(true);
    super.onStart();
  }
}
