package com.android.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.IntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

public abstract class ExploreByTouchHelper
  extends View.AccessibilityDelegate
{
  private static final String DEFAULT_CLASS_NAME = View.class.getName();
  public static final int HOST_ID = -1;
  public static final int INVALID_ID = Integer.MIN_VALUE;
  private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
  private final Context mContext;
  private int mFocusedVirtualViewId = Integer.MIN_VALUE;
  private int mHoveredVirtualViewId = Integer.MIN_VALUE;
  private final AccessibilityManager mManager;
  private ExploreByTouchNodeProvider mNodeProvider;
  private IntArray mTempArray;
  private int[] mTempGlobalRect;
  private Rect mTempParentRect;
  private Rect mTempScreenRect;
  private Rect mTempVisibleRect;
  private final View mView;
  
  public ExploreByTouchHelper(View paramView)
  {
    if (paramView != null)
    {
      mView = paramView;
      mContext = paramView.getContext();
      mManager = ((AccessibilityManager)mContext.getSystemService("accessibility"));
      return;
    }
    throw new IllegalArgumentException("View may not be null");
  }
  
  private boolean clearAccessibilityFocus(int paramInt)
  {
    if (isAccessibilityFocused(paramInt))
    {
      mFocusedVirtualViewId = Integer.MIN_VALUE;
      mView.invalidate();
      sendEventForVirtualView(paramInt, 65536);
      return true;
    }
    return false;
  }
  
  private AccessibilityEvent createEvent(int paramInt1, int paramInt2)
  {
    if (paramInt1 != -1) {
      return createEventForChild(paramInt1, paramInt2);
    }
    return createEventForHost(paramInt2);
  }
  
  private AccessibilityEvent createEventForChild(int paramInt1, int paramInt2)
  {
    AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt2);
    localAccessibilityEvent.setEnabled(true);
    localAccessibilityEvent.setClassName(DEFAULT_CLASS_NAME);
    onPopulateEventForVirtualView(paramInt1, localAccessibilityEvent);
    if ((localAccessibilityEvent.getText().isEmpty()) && (localAccessibilityEvent.getContentDescription() == null)) {
      throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
    }
    localAccessibilityEvent.setPackageName(mView.getContext().getPackageName());
    localAccessibilityEvent.setSource(mView, paramInt1);
    return localAccessibilityEvent;
  }
  
  private AccessibilityEvent createEventForHost(int paramInt)
  {
    AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt);
    mView.onInitializeAccessibilityEvent(localAccessibilityEvent);
    onPopulateEventForHost(localAccessibilityEvent);
    return localAccessibilityEvent;
  }
  
  private AccessibilityNodeInfo createNode(int paramInt)
  {
    if (paramInt != -1) {
      return createNodeForChild(paramInt);
    }
    return createNodeForHost();
  }
  
  private AccessibilityNodeInfo createNodeForChild(int paramInt)
  {
    ensureTempRects();
    Rect localRect1 = mTempParentRect;
    int[] arrayOfInt = mTempGlobalRect;
    Rect localRect2 = mTempScreenRect;
    AccessibilityNodeInfo localAccessibilityNodeInfo = AccessibilityNodeInfo.obtain();
    localAccessibilityNodeInfo.setEnabled(true);
    localAccessibilityNodeInfo.setClassName(DEFAULT_CLASS_NAME);
    localAccessibilityNodeInfo.setBoundsInParent(INVALID_PARENT_BOUNDS);
    onPopulateNodeForVirtualView(paramInt, localAccessibilityNodeInfo);
    if ((localAccessibilityNodeInfo.getText() == null) && (localAccessibilityNodeInfo.getContentDescription() == null)) {
      throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
    }
    localAccessibilityNodeInfo.getBoundsInParent(localRect1);
    if (!localRect1.equals(INVALID_PARENT_BOUNDS))
    {
      int i = localAccessibilityNodeInfo.getActions();
      if ((i & 0x40) == 0)
      {
        if ((i & 0x80) == 0)
        {
          localAccessibilityNodeInfo.setPackageName(mView.getContext().getPackageName());
          localAccessibilityNodeInfo.setSource(mView, paramInt);
          localAccessibilityNodeInfo.setParent(mView);
          if (mFocusedVirtualViewId == paramInt)
          {
            localAccessibilityNodeInfo.setAccessibilityFocused(true);
            localAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
          }
          else
          {
            localAccessibilityNodeInfo.setAccessibilityFocused(false);
            localAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
          }
          if (intersectVisibleToUser(localRect1))
          {
            localAccessibilityNodeInfo.setVisibleToUser(true);
            localAccessibilityNodeInfo.setBoundsInParent(localRect1);
          }
          mView.getLocationOnScreen(arrayOfInt);
          i = arrayOfInt[0];
          paramInt = arrayOfInt[1];
          localRect2.set(localRect1);
          localRect2.offset(i, paramInt);
          localAccessibilityNodeInfo.setBoundsInScreen(localRect2);
          return localAccessibilityNodeInfo;
        }
        throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
      }
      throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
    }
    throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
  }
  
  private AccessibilityNodeInfo createNodeForHost()
  {
    AccessibilityNodeInfo localAccessibilityNodeInfo = AccessibilityNodeInfo.obtain(mView);
    mView.onInitializeAccessibilityNodeInfo(localAccessibilityNodeInfo);
    int i = localAccessibilityNodeInfo.getChildCount();
    onPopulateNodeForHost(localAccessibilityNodeInfo);
    if (mTempArray == null) {
      mTempArray = new IntArray();
    } else {
      mTempArray.clear();
    }
    IntArray localIntArray = mTempArray;
    getVisibleVirtualViews(localIntArray);
    if ((i > 0) && (localIntArray.size() > 0)) {
      throw new RuntimeException("Views cannot have both real and virtual children");
    }
    int j = localIntArray.size();
    for (i = 0; i < j; i++) {
      localAccessibilityNodeInfo.addChild(mView, localIntArray.get(i));
    }
    return localAccessibilityNodeInfo;
  }
  
  private void ensureTempRects()
  {
    mTempGlobalRect = new int[2];
    mTempParentRect = new Rect();
    mTempScreenRect = new Rect();
  }
  
  private boolean intersectVisibleToUser(Rect paramRect)
  {
    if ((paramRect != null) && (!paramRect.isEmpty()))
    {
      if (mView.getWindowVisibility() != 0) {
        return false;
      }
      Object localObject = mView.getParent();
      while ((localObject instanceof View))
      {
        localObject = (View)localObject;
        if ((((View)localObject).getAlpha() > 0.0F) && (((View)localObject).getVisibility() == 0)) {
          localObject = ((View)localObject).getParent();
        } else {
          return false;
        }
      }
      if (localObject == null) {
        return false;
      }
      if (mTempVisibleRect == null) {
        mTempVisibleRect = new Rect();
      }
      localObject = mTempVisibleRect;
      if (!mView.getLocalVisibleRect((Rect)localObject)) {
        return false;
      }
      return paramRect.intersect((Rect)localObject);
    }
    return false;
  }
  
  private boolean isAccessibilityFocused(int paramInt)
  {
    boolean bool;
    if (mFocusedVirtualViewId == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean manageFocusForChild(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 64)
    {
      if (paramInt2 != 128) {
        return false;
      }
      return clearAccessibilityFocus(paramInt1);
    }
    return requestAccessibilityFocus(paramInt1);
  }
  
  private boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    if (paramInt1 != -1) {
      return performActionForChild(paramInt1, paramInt2, paramBundle);
    }
    return performActionForHost(paramInt2, paramBundle);
  }
  
  private boolean performActionForChild(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    if ((paramInt2 != 64) && (paramInt2 != 128)) {
      return onPerformActionForVirtualView(paramInt1, paramInt2, paramBundle);
    }
    return manageFocusForChild(paramInt1, paramInt2);
  }
  
  private boolean performActionForHost(int paramInt, Bundle paramBundle)
  {
    return mView.performAccessibilityAction(paramInt, paramBundle);
  }
  
  private boolean requestAccessibilityFocus(int paramInt)
  {
    AccessibilityManager localAccessibilityManager = (AccessibilityManager)mContext.getSystemService("accessibility");
    if ((mManager.isEnabled()) && (localAccessibilityManager.isTouchExplorationEnabled()))
    {
      if (!isAccessibilityFocused(paramInt))
      {
        if (mFocusedVirtualViewId != Integer.MIN_VALUE) {
          sendEventForVirtualView(mFocusedVirtualViewId, 65536);
        }
        mFocusedVirtualViewId = paramInt;
        mView.invalidate();
        sendEventForVirtualView(paramInt, 32768);
        return true;
      }
      return false;
    }
    return false;
  }
  
  private void updateHoveredVirtualView(int paramInt)
  {
    if (mHoveredVirtualViewId == paramInt) {
      return;
    }
    int i = mHoveredVirtualViewId;
    mHoveredVirtualViewId = paramInt;
    sendEventForVirtualView(paramInt, 128);
    sendEventForVirtualView(i, 256);
  }
  
  public boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = mManager.isEnabled();
    boolean bool2 = false;
    if ((bool1) && (mManager.isTouchExplorationEnabled()))
    {
      int i = paramMotionEvent.getAction();
      if (i != 7) {
        switch (i)
        {
        default: 
          return false;
        case 10: 
          if (mFocusedVirtualViewId != Integer.MIN_VALUE)
          {
            updateHoveredVirtualView(Integer.MIN_VALUE);
            return true;
          }
          return false;
        }
      }
      i = getVirtualViewAt(paramMotionEvent.getX(), paramMotionEvent.getY());
      updateHoveredVirtualView(i);
      if (i != Integer.MIN_VALUE) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public AccessibilityNodeProvider getAccessibilityNodeProvider(View paramView)
  {
    if (mNodeProvider == null) {
      mNodeProvider = new ExploreByTouchNodeProvider(null);
    }
    return mNodeProvider;
  }
  
  public int getFocusedVirtualView()
  {
    return mFocusedVirtualViewId;
  }
  
  protected abstract int getVirtualViewAt(float paramFloat1, float paramFloat2);
  
  protected abstract void getVisibleVirtualViews(IntArray paramIntArray);
  
  public void invalidateRoot()
  {
    invalidateVirtualView(-1, 1);
  }
  
  public void invalidateVirtualView(int paramInt)
  {
    invalidateVirtualView(paramInt, 0);
  }
  
  public void invalidateVirtualView(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != Integer.MIN_VALUE) && (mManager.isEnabled()))
    {
      ViewParent localViewParent = mView.getParent();
      if (localViewParent != null)
      {
        AccessibilityEvent localAccessibilityEvent = createEvent(paramInt1, 2048);
        localAccessibilityEvent.setContentChangeTypes(paramInt2);
        localViewParent.requestSendAccessibilityEvent(mView, localAccessibilityEvent);
      }
    }
  }
  
  protected abstract boolean onPerformActionForVirtualView(int paramInt1, int paramInt2, Bundle paramBundle);
  
  protected void onPopulateEventForHost(AccessibilityEvent paramAccessibilityEvent) {}
  
  protected abstract void onPopulateEventForVirtualView(int paramInt, AccessibilityEvent paramAccessibilityEvent);
  
  protected void onPopulateNodeForHost(AccessibilityNodeInfo paramAccessibilityNodeInfo) {}
  
  protected abstract void onPopulateNodeForVirtualView(int paramInt, AccessibilityNodeInfo paramAccessibilityNodeInfo);
  
  public boolean sendEventForVirtualView(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != Integer.MIN_VALUE) && (mManager.isEnabled()))
    {
      ViewParent localViewParent = mView.getParent();
      if (localViewParent == null) {
        return false;
      }
      AccessibilityEvent localAccessibilityEvent = createEvent(paramInt1, paramInt2);
      return localViewParent.requestSendAccessibilityEvent(mView, localAccessibilityEvent);
    }
    return false;
  }
  
  private class ExploreByTouchNodeProvider
    extends AccessibilityNodeProvider
  {
    private ExploreByTouchNodeProvider() {}
    
    public AccessibilityNodeInfo createAccessibilityNodeInfo(int paramInt)
    {
      return ExploreByTouchHelper.this.createNode(paramInt);
    }
    
    public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      return ExploreByTouchHelper.this.performAction(paramInt1, paramInt2, paramBundle);
    }
  }
}
