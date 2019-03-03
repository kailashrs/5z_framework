package com.android.internal.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;

public abstract class AlertActivity
  extends Activity
  implements DialogInterface
{
  protected AlertController mAlert;
  protected AlertController.AlertParams mAlertParams;
  
  public AlertActivity() {}
  
  public static boolean dispatchPopulateAccessibilityEvent(Activity paramActivity, AccessibilityEvent paramAccessibilityEvent)
  {
    paramAccessibilityEvent.setClassName(Dialog.class.getName());
    paramAccessibilityEvent.setPackageName(paramActivity.getPackageName());
    paramActivity = paramActivity.getWindow().getAttributes();
    boolean bool;
    if ((width == -1) && (height == -1)) {
      bool = true;
    } else {
      bool = false;
    }
    paramAccessibilityEvent.setFullScreen(bool);
    return false;
  }
  
  public void cancel()
  {
    finish();
  }
  
  public void dismiss()
  {
    if (!isFinishing()) {
      finish();
    }
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    return dispatchPopulateAccessibilityEvent(this, paramAccessibilityEvent);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mAlert = AlertController.create(this, this, getWindow());
    mAlertParams = new AlertController.AlertParams(this);
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
  
  protected void setupAlert()
  {
    mAlert.installContent(mAlertParams);
  }
}
