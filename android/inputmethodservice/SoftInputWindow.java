package android.inputmethodservice;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class SoftInputWindow
  extends Dialog
{
  private final Rect mBounds = new Rect();
  final Callback mCallback;
  final KeyEvent.DispatcherState mDispatcherState;
  final int mGravity;
  final KeyEvent.Callback mKeyEventCallback;
  final String mName;
  final boolean mTakesFocus;
  final int mWindowType;
  
  public SoftInputWindow(Context paramContext, String paramString, int paramInt1, Callback paramCallback, KeyEvent.Callback paramCallback1, KeyEvent.DispatcherState paramDispatcherState, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    super(paramContext, paramInt1);
    mName = paramString;
    mCallback = paramCallback;
    mKeyEventCallback = paramCallback1;
    mDispatcherState = paramDispatcherState;
    mWindowType = paramInt2;
    mGravity = paramInt3;
    mTakesFocus = paramBoolean;
    initDockWindow();
  }
  
  private void initDockWindow()
  {
    WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
    type = mWindowType;
    localLayoutParams.setTitle(mName);
    gravity = mGravity;
    updateWidthHeight(localLayoutParams);
    getWindow().setAttributes(localLayoutParams);
    int i = 266;
    int j;
    if (!mTakesFocus)
    {
      j = 0x100 | 0x8;
    }
    else
    {
      j = 0x100 | 0x20;
      i = 0x10A | 0x20;
    }
    getWindow().setFlags(j, i);
  }
  
  private void updateWidthHeight(WindowManager.LayoutParams paramLayoutParams)
  {
    if ((gravity != 48) && (gravity != 80))
    {
      width = -2;
      height = -1;
    }
    else
    {
      width = -1;
      height = -2;
    }
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    getWindow().getDecorView().getHitRect(mBounds);
    if (paramMotionEvent.isWithinBoundsNoHistory(mBounds.left, mBounds.top, mBounds.right - 1, mBounds.bottom - 1)) {
      return super.dispatchTouchEvent(paramMotionEvent);
    }
    paramMotionEvent = paramMotionEvent.clampNoHistory(mBounds.left, mBounds.top, mBounds.right - 1, mBounds.bottom - 1);
    boolean bool = super.dispatchTouchEvent(paramMotionEvent);
    paramMotionEvent.recycle();
    return bool;
  }
  
  public int getGravity()
  {
    return getWindowgetAttributesgravity;
  }
  
  public void onBackPressed()
  {
    if (mCallback != null) {
      mCallback.onBackPressed();
    } else {
      super.onBackPressed();
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((mKeyEventCallback != null) && (mKeyEventCallback.onKeyDown(paramInt, paramKeyEvent))) {
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((mKeyEventCallback != null) && (mKeyEventCallback.onKeyLongPress(paramInt, paramKeyEvent))) {
      return true;
    }
    return super.onKeyLongPress(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    if ((mKeyEventCallback != null) && (mKeyEventCallback.onKeyMultiple(paramInt1, paramInt2, paramKeyEvent))) {
      return true;
    }
    return super.onKeyMultiple(paramInt1, paramInt2, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((mKeyEventCallback != null) && (mKeyEventCallback.onKeyUp(paramInt, paramKeyEvent))) {
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    mDispatcherState.reset();
  }
  
  public void setGravity(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
    gravity = paramInt;
    updateWidthHeight(localLayoutParams);
    getWindow().setAttributes(localLayoutParams);
  }
  
  public void setToken(IBinder paramIBinder)
  {
    WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
    token = paramIBinder;
    getWindow().setAttributes(localLayoutParams);
  }
  
  public static abstract interface Callback
  {
    public abstract void onBackPressed();
  }
}
