package com.android.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewOutlineProvider;
import android.view.Window.WindowControllerCallback;
import com.android.internal.policy.PhoneWindow;
import java.util.ArrayList;

public class DecorCaptionView
  extends ViewGroup
  implements View.OnTouchListener, GestureDetector.OnGestureListener
{
  private static final String TAG = "DecorCaptionView";
  private View mCaption;
  private boolean mCheckForDragging;
  private View mClickTarget;
  private View mClose;
  private final Rect mCloseRect = new Rect();
  private View mContent;
  private int mDragSlop;
  private boolean mDragging = false;
  private GestureDetector mGestureDetector;
  private View mMaximize;
  private final Rect mMaximizeRect = new Rect();
  private boolean mOverlayWithAppContent = false;
  private PhoneWindow mOwner = null;
  private boolean mShow = false;
  private ArrayList<View> mTouchDispatchList = new ArrayList(2);
  private int mTouchDownX;
  private int mTouchDownY;
  
  public DecorCaptionView(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }
  
  public DecorCaptionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public DecorCaptionView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private void init(Context paramContext)
  {
    mDragSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    mGestureDetector = new GestureDetector(paramContext, this);
  }
  
  private boolean isFillingScreen()
  {
    boolean bool;
    if (((getWindowSystemUiVisibility() | getSystemUiVisibility()) & 0xA05) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void maximizeWindow()
  {
    Window.WindowControllerCallback localWindowControllerCallback = mOwner.getWindowControllerCallback();
    if (localWindowControllerCallback != null) {
      try
      {
        localWindowControllerCallback.exitFreeformMode();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("DecorCaptionView", "Cannot change task workspace.");
      }
    }
  }
  
  private boolean passedSlop(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((Math.abs(paramInt1 - mTouchDownX) <= mDragSlop) && (Math.abs(paramInt2 - mTouchDownY) <= mDragSlop)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void updateCaptionVisibility()
  {
    boolean bool = isFillingScreen();
    int i = 0;
    int j;
    if ((!bool) && (mShow)) {
      j = 0;
    } else {
      j = 1;
    }
    View localView = mCaption;
    if (j != 0) {
      i = 8;
    }
    localView.setVisibility(i);
    mCaption.setOnTouchListener(this);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams))
    {
      if ((paramInt < 2) && (getChildCount() < 2))
      {
        super.addView(paramView, 0, paramLayoutParams);
        mContent = paramView;
        return;
      }
      throw new IllegalStateException("DecorCaptionView can only handle 1 client view");
    }
    paramView = new StringBuilder();
    paramView.append("params ");
    paramView.append(paramLayoutParams);
    paramView.append(" must subclass MarginLayoutParams");
    throw new IllegalArgumentException(paramView.toString());
  }
  
  public ArrayList<View> buildTouchDispatchChildList()
  {
    mTouchDispatchList.ensureCapacity(3);
    if (mCaption != null) {
      mTouchDispatchList.add(mCaption);
    }
    if (mContent != null) {
      mTouchDispatchList.add(mContent);
    }
    return mTouchDispatchList;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof ViewGroup.MarginLayoutParams;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ViewGroup.MarginLayoutParams(-1, -1);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new ViewGroup.MarginLayoutParams(paramLayoutParams);
  }
  
  public View getCaption()
  {
    return mCaption;
  }
  
  public int getCaptionHeight()
  {
    int i;
    if (mCaption != null) {
      i = mCaption.getHeight();
    } else {
      i = 0;
    }
    return i;
  }
  
  public boolean isCaptionShowing()
  {
    return mShow;
  }
  
  public void onConfigurationChanged(boolean paramBoolean)
  {
    mShow = paramBoolean;
    updateCaptionVisibility();
  }
  
  public boolean onDown(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mCaption = getChildAt(0);
  }
  
  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0)
    {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      if (mMaximizeRect.contains(i, j)) {
        mClickTarget = mMaximize;
      }
      if (mCloseRect.contains(i, j)) {
        mClickTarget = mClose;
      }
    }
    boolean bool;
    if (mClickTarget != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mCaption.getVisibility() != 8)
    {
      mCaption.layout(0, 0, mCaption.getMeasuredWidth(), mCaption.getMeasuredHeight());
      paramInt1 = mCaption.getBottom() - mCaption.getTop();
      mMaximize.getHitRect(mMaximizeRect);
      mClose.getHitRect(mCloseRect);
    }
    else
    {
      paramInt1 = 0;
      mMaximizeRect.setEmpty();
      mCloseRect.setEmpty();
    }
    if (mContent != null) {
      if (mOverlayWithAppContent) {
        mContent.layout(0, 0, mContent.getMeasuredWidth(), mContent.getMeasuredHeight());
      } else {
        mContent.layout(0, paramInt1, mContent.getMeasuredWidth(), mContent.getMeasuredHeight() + paramInt1);
      }
    }
    mOwner.notifyRestrictedCaptionAreaCallback(mMaximize.getLeft(), mMaximize.getTop(), mClose.getRight(), mClose.getBottom());
  }
  
  public void onLongPress(MotionEvent paramMotionEvent) {}
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (mCaption.getVisibility() != 8) {
      measureChildWithMargins(mCaption, paramInt1, 0, paramInt2, 0);
    }
    for (int i = mCaption.getMeasuredHeight();; i = 0) {
      break;
    }
    if (mContent != null) {
      if (mOverlayWithAppContent) {
        measureChildWithMargins(mContent, paramInt1, 0, paramInt2, 0);
      } else {
        measureChildWithMargins(mContent, paramInt1, 0, paramInt2, i);
      }
    }
    setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
  }
  
  public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    return false;
  }
  
  public void onShowPress(MotionEvent paramMotionEvent) {}
  
  public boolean onSingleTapUp(MotionEvent paramMotionEvent)
  {
    if (mClickTarget == mMaximize) {
      maximizeWindow();
    } else if (mClickTarget == mClose) {
      mOwner.dispatchOnWindowDismissed(true, false);
    }
    return true;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    int k = paramMotionEvent.getToolType(paramMotionEvent.getActionIndex());
    boolean bool = false;
    if (k == 3) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if ((paramMotionEvent.getButtonState() & 0x1) != 0) {
      m = 1;
    } else {
      m = 0;
    }
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 2: 
      if ((!mDragging) && (mCheckForDragging) && ((k != 0) || (passedSlop(i, j))))
      {
        mCheckForDragging = false;
        mDragging = true;
        startMovingTask(paramMotionEvent.getRawX(), paramMotionEvent.getRawY());
      }
      break;
    case 1: 
    case 3: 
      if (mDragging)
      {
        mDragging = false;
        return mCheckForDragging ^ true;
      }
      break;
    case 0: 
      if (!mShow) {
        return false;
      }
      if ((k == 0) || (m != 0))
      {
        mCheckForDragging = true;
        mTouchDownX = i;
        mTouchDownY = j;
      }
      break;
    }
    if ((!mDragging) && (!mCheckForDragging)) {
      break label230;
    }
    bool = true;
    label230:
    return bool;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (mClickTarget != null)
    {
      mGestureDetector.onTouchEvent(paramMotionEvent);
      int i = paramMotionEvent.getAction();
      if ((i == 1) || (i == 3)) {
        mClickTarget = null;
      }
      return true;
    }
    return false;
  }
  
  public void removeContentView()
  {
    if (mContent != null)
    {
      removeView(mContent);
      mContent = null;
    }
  }
  
  public void setPhoneWindow(PhoneWindow paramPhoneWindow, boolean paramBoolean)
  {
    mOwner = paramPhoneWindow;
    mShow = paramBoolean;
    mOverlayWithAppContent = paramPhoneWindow.isOverlayWithDecorCaptionEnabled();
    if (mOverlayWithAppContent) {
      mCaption.setBackgroundColor(0);
    }
    updateCaptionVisibility();
    mOwner.getDecorView().setOutlineProvider(ViewOutlineProvider.BOUNDS);
    mMaximize = findViewById(16909109);
    mClose = findViewById(16908864);
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
}
