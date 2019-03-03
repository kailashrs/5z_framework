package android.view.autofill;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.transition.Transition;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class AutofillPopupWindow
  extends PopupWindow
{
  private static final String TAG = "AutofillPopupWindow";
  private boolean mFullScreen;
  private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
  {
    public void onViewAttachedToWindow(View paramAnonymousView) {}
    
    public void onViewDetachedFromWindow(View paramAnonymousView)
    {
      dismiss();
    }
  };
  private WindowManager.LayoutParams mWindowLayoutParams;
  private final WindowPresenter mWindowPresenter;
  
  public AutofillPopupWindow(IAutofillWindowPresenter paramIAutofillWindowPresenter)
  {
    mWindowPresenter = new WindowPresenter(paramIAutofillWindowPresenter);
    setTouchModal(false);
    setOutsideTouchable(true);
    setInputMethodMode(2);
    setFocusable(true);
  }
  
  protected void attachToAnchor(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    super.attachToAnchor(paramView, paramInt1, paramInt2, paramInt3);
    paramView.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
  }
  
  protected void detachFromAnchor()
  {
    View localView = getAnchor();
    if (localView != null) {
      localView.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
    }
    super.detachFromAnchor();
  }
  
  public void dismiss()
  {
    if ((isShowing()) && (!isTransitioningToDismiss()))
    {
      setShowing(false);
      setTransitioningToDismiss(true);
      mWindowPresenter.hide(getTransitionEpicenter());
      detachFromAnchor();
      if (getOnDismissListener() != null) {
        getOnDismissListener().onDismiss();
      }
      return;
    }
  }
  
  protected boolean findDropDownPosition(View paramView, WindowManager.LayoutParams paramLayoutParams, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
  {
    if (mFullScreen)
    {
      x = paramInt1;
      y = paramInt2;
      width = paramInt3;
      height = paramInt4;
      gravity = paramInt5;
      return false;
    }
    return super.findDropDownPosition(paramView, paramLayoutParams, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramBoolean);
  }
  
  public int getAnimationStyle()
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public Drawable getBackground()
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public View getContentView()
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  protected WindowManager.LayoutParams getDecorViewLayoutParams()
  {
    return mWindowLayoutParams;
  }
  
  public float getElevation()
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public Transition getEnterTransition()
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public Transition getExitTransition()
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  protected boolean hasContentView()
  {
    return true;
  }
  
  protected boolean hasDecorView()
  {
    return true;
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public void setContentView(View paramView)
  {
    if (paramView == null) {
      return;
    }
    throw new IllegalStateException("You can't call this!");
  }
  
  public void setElevation(float paramFloat)
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public void setEnterTransition(Transition paramTransition)
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public void setExitTransition(Transition paramTransition)
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public void setTouchInterceptor(View.OnTouchListener paramOnTouchListener)
  {
    throw new IllegalStateException("You can't call this!");
  }
  
  public void showAsDropDown(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (Helper.sVerbose)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("showAsDropDown(): anchor=");
      ((StringBuilder)localObject).append(paramView);
      ((StringBuilder)localObject).append(", xoff=");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(", yoff=");
      ((StringBuilder)localObject).append(paramInt2);
      ((StringBuilder)localObject).append(", isShowing(): ");
      ((StringBuilder)localObject).append(isShowing());
      Log.v("AutofillPopupWindow", ((StringBuilder)localObject).toString());
    }
    if (isShowing()) {
      return;
    }
    setShowing(true);
    setDropDown(true);
    attachToAnchor(paramView, paramInt1, paramInt2, paramInt3);
    Object localObject = createPopupLayoutParams(paramView.getWindowToken());
    mWindowLayoutParams = ((WindowManager.LayoutParams)localObject);
    updateAboveAnchor(findDropDownPosition(paramView, (WindowManager.LayoutParams)localObject, paramInt1, paramInt2, width, height, paramInt3, getAllowScrollingAnchorParent()));
    accessibilityIdOfAnchor = paramView.getAccessibilityViewId();
    packageName = paramView.getContext().getPackageName();
    mWindowPresenter.show((WindowManager.LayoutParams)localObject, getTransitionEpicenter(), isLayoutInsetDecor(), paramView.getLayoutDirection());
  }
  
  public void update(final View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rect paramRect)
  {
    boolean bool;
    if (paramInt3 == -1) {
      bool = true;
    } else {
      bool = false;
    }
    mFullScreen = bool;
    int i;
    if (mFullScreen) {
      i = 2008;
    } else {
      i = 1005;
    }
    setWindowLayoutType(i);
    if (mFullScreen)
    {
      i = 0;
      paramInt1 = 0;
      paramRect = new Point();
      paramView.getContext().getDisplay().getSize(paramRect);
      paramInt3 = x;
      if (paramInt4 != -1) {
        paramInt1 = y - paramInt4;
      }
      paramInt2 = paramInt1;
    }
    else if (paramRect != null)
    {
      final int[] arrayOfInt = new int[2];
      arrayOfInt[0] = left;
      arrayOfInt[1] = top;
      View local2 = new View(paramView.getContext())
      {
        public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener paramAnonymousOnAttachStateChangeListener)
        {
          paramView.addOnAttachStateChangeListener(paramAnonymousOnAttachStateChangeListener);
        }
        
        public int getAccessibilityViewId()
        {
          return paramView.getAccessibilityViewId();
        }
        
        public IBinder getApplicationWindowToken()
        {
          return paramView.getApplicationWindowToken();
        }
        
        public int getLayoutDirection()
        {
          return paramView.getLayoutDirection();
        }
        
        public void getLocationOnScreen(int[] paramAnonymousArrayOfInt)
        {
          paramAnonymousArrayOfInt[0] = arrayOfInt[0];
          paramAnonymousArrayOfInt[1] = arrayOfInt[1];
        }
        
        public View getRootView()
        {
          return paramView.getRootView();
        }
        
        public ViewTreeObserver getViewTreeObserver()
        {
          return paramView.getViewTreeObserver();
        }
        
        public void getWindowDisplayFrame(Rect paramAnonymousRect)
        {
          paramView.getWindowDisplayFrame(paramAnonymousRect);
        }
        
        public IBinder getWindowToken()
        {
          return paramView.getWindowToken();
        }
        
        public boolean isAttachedToWindow()
        {
          return paramView.isAttachedToWindow();
        }
        
        public void removeOnAttachStateChangeListener(View.OnAttachStateChangeListener paramAnonymousOnAttachStateChangeListener)
        {
          paramView.removeOnAttachStateChangeListener(paramAnonymousOnAttachStateChangeListener);
        }
        
        public boolean requestRectangleOnScreen(Rect paramAnonymousRect, boolean paramAnonymousBoolean)
        {
          return paramView.requestRectangleOnScreen(paramAnonymousRect, paramAnonymousBoolean);
        }
      };
      local2.setLeftTopRightBottom(left, top, right, bottom);
      local2.setScrollX(paramView.getScrollX());
      local2.setScrollY(paramView.getScrollY());
      paramView.setOnScrollChangeListener(new _..Lambda.AutofillPopupWindow.DnLs9aVkSgQ89oSTe4P9EweBBks(arrayOfInt));
      local2.setWillNotDraw(true);
      paramView = local2;
      i = paramInt1;
    }
    else
    {
      i = paramInt1;
    }
    if (!mFullScreen) {
      setAnimationStyle(-1);
    } else if (paramInt4 == -1) {
      setAnimationStyle(0);
    } else {
      setAnimationStyle(16974605);
    }
    if (!isShowing())
    {
      setWidth(paramInt3);
      setHeight(paramInt4);
      showAsDropDown(paramView, i, paramInt2);
    }
    else
    {
      update(paramView, i, paramInt2, paramInt3, paramInt4);
    }
  }
  
  protected void update(View paramView, WindowManager.LayoutParams paramLayoutParams)
  {
    int i;
    if (paramView != null) {
      i = paramView.getLayoutDirection();
    } else {
      i = 3;
    }
    mWindowPresenter.show(paramLayoutParams, getTransitionEpicenter(), isLayoutInsetDecor(), i);
  }
  
  private class WindowPresenter
  {
    final IAutofillWindowPresenter mPresenter;
    
    WindowPresenter(IAutofillWindowPresenter paramIAutofillWindowPresenter)
    {
      mPresenter = paramIAutofillWindowPresenter;
    }
    
    void hide(Rect paramRect)
    {
      try
      {
        mPresenter.hide(paramRect);
      }
      catch (RemoteException paramRect)
      {
        Log.w("AutofillPopupWindow", "Error hiding fill window", paramRect);
        paramRect.rethrowFromSystemServer();
      }
    }
    
    void show(WindowManager.LayoutParams paramLayoutParams, Rect paramRect, boolean paramBoolean, int paramInt)
    {
      try
      {
        mPresenter.show(paramLayoutParams, paramRect, paramBoolean, paramInt);
      }
      catch (RemoteException paramLayoutParams)
      {
        Log.w("AutofillPopupWindow", "Error showing fill window", paramLayoutParams);
        paramLayoutParams.rethrowFromSystemServer();
      }
    }
  }
}
