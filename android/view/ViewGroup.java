package android.view;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pools.SimplePool;
import android.util.Pools.SynchronizedPool;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LayoutAnimationController.AnimationParameters;
import android.view.animation.Transformation;
import android.view.autofill.Helper;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ViewGroup
  extends View
  implements ViewParent, ViewManager
{
  private static final int ARRAY_CAPACITY_INCREMENT = 12;
  private static final int ARRAY_INITIAL_CAPACITY = 12;
  private static final int CHILD_LEFT_INDEX = 0;
  private static final int CHILD_TOP_INDEX = 1;
  protected static final int CLIP_TO_PADDING_MASK = 34;
  private static final boolean DBG = false;
  private static final int[] DESCENDANT_FOCUSABILITY_FLAGS = { 131072, 262144, 393216 };
  private static final int FLAG_ADD_STATES_FROM_CHILDREN = 8192;
  @Deprecated
  private static final int FLAG_ALWAYS_DRAWN_WITH_CACHE = 16384;
  @Deprecated
  private static final int FLAG_ANIMATION_CACHE = 64;
  static final int FLAG_ANIMATION_DONE = 16;
  @Deprecated
  private static final int FLAG_CHILDREN_DRAWN_WITH_CACHE = 32768;
  static final int FLAG_CLEAR_TRANSFORMATION = 256;
  static final int FLAG_CLIP_CHILDREN = 1;
  private static final int FLAG_CLIP_TO_PADDING = 2;
  protected static final int FLAG_DISALLOW_INTERCEPT = 524288;
  static final int FLAG_INVALIDATE_REQUIRED = 4;
  static final int FLAG_IS_TRANSITION_GROUP = 16777216;
  static final int FLAG_IS_TRANSITION_GROUP_SET = 33554432;
  private static final int FLAG_LAYOUT_MODE_WAS_EXPLICITLY_SET = 8388608;
  private static final int FLAG_MASK_FOCUSABILITY = 393216;
  private static final int FLAG_NOTIFY_ANIMATION_LISTENER = 512;
  private static final int FLAG_NOTIFY_CHILDREN_ON_DRAWABLE_STATE_CHANGE = 65536;
  static final int FLAG_OPTIMIZE_INVALIDATE = 128;
  private static final int FLAG_PADDING_NOT_NULL = 32;
  private static final int FLAG_PREVENT_DISPATCH_ATTACHED_TO_WINDOW = 4194304;
  private static final int FLAG_RUN_ANIMATION = 8;
  private static final int FLAG_SHOW_CONTEXT_MENU_WITH_COORDS = 536870912;
  private static final int FLAG_SPLIT_MOTION_EVENTS = 2097152;
  private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_NOT_TYPED = 268435456;
  private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_TYPED = 134217728;
  protected static final int FLAG_SUPPORT_STATIC_TRANSFORMATIONS = 2048;
  static final int FLAG_TOUCHSCREEN_BLOCKS_FOCUS = 67108864;
  protected static final int FLAG_USE_CHILD_DRAWING_ORDER = 1024;
  public static final int FOCUS_AFTER_DESCENDANTS = 262144;
  public static final int FOCUS_BEFORE_DESCENDANTS = 131072;
  public static final int FOCUS_BLOCK_DESCENDANTS = 393216;
  public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
  public static int LAYOUT_MODE_DEFAULT = 0;
  public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
  private static final int LAYOUT_MODE_UNDEFINED = -1;
  @Deprecated
  public static final int PERSISTENT_ALL_CACHES = 3;
  @Deprecated
  public static final int PERSISTENT_ANIMATION_CACHE = 1;
  @Deprecated
  public static final int PERSISTENT_NO_CACHE = 0;
  @Deprecated
  public static final int PERSISTENT_SCROLLING_CACHE = 2;
  private static final ActionMode SENTINEL_ACTION_MODE = new ActionMode()
  {
    public void finish() {}
    
    public View getCustomView()
    {
      return null;
    }
    
    public Menu getMenu()
    {
      return null;
    }
    
    public MenuInflater getMenuInflater()
    {
      return null;
    }
    
    public CharSequence getSubtitle()
    {
      return null;
    }
    
    public CharSequence getTitle()
    {
      return null;
    }
    
    public void invalidate() {}
    
    public void setCustomView(View paramAnonymousView) {}
    
    public void setSubtitle(int paramAnonymousInt) {}
    
    public void setSubtitle(CharSequence paramAnonymousCharSequence) {}
    
    public void setTitle(int paramAnonymousInt) {}
    
    public void setTitle(CharSequence paramAnonymousCharSequence) {}
  };
  private static final String TAG = "ViewGroup";
  private static float[] sDebugLines;
  private Animation.AnimationListener mAnimationListener;
  Paint mCachePaint;
  @ViewDebug.ExportedProperty(category="layout")
  private int mChildCountWithTransientState = 0;
  private Transformation mChildTransformation;
  int mChildUnhandledKeyListeners = 0;
  private View[] mChildren;
  private int mChildrenCount;
  private HashSet<View> mChildrenInterestedInDrag;
  private View mCurrentDragChild;
  private DragEvent mCurrentDragStartEvent;
  private View mDefaultFocus;
  protected ArrayList<View> mDisappearingChildren;
  private HoverTarget mFirstHoverTarget;
  private TouchTarget mFirstTouchTarget;
  private View mFocused;
  View mFocusedInCluster;
  @ViewDebug.ExportedProperty(flagMapping={@ViewDebug.FlagToString(equals=1, mask=1, name="CLIP_CHILDREN"), @ViewDebug.FlagToString(equals=2, mask=2, name="CLIP_TO_PADDING"), @ViewDebug.FlagToString(equals=32, mask=32, name="PADDING_NOT_NULL")}, formatToHexString=true)
  protected int mGroupFlags;
  private boolean mHoveredSelf;
  RectF mInvalidateRegion;
  Transformation mInvalidationTransformation;
  private boolean mIsInterestedInDrag;
  @ViewDebug.ExportedProperty(category="events")
  private int mLastTouchDownIndex = -1;
  @ViewDebug.ExportedProperty(category="events")
  private long mLastTouchDownTime;
  @ViewDebug.ExportedProperty(category="events")
  private float mLastTouchDownX;
  @ViewDebug.ExportedProperty(category="events")
  private float mLastTouchDownY;
  private LayoutAnimationController mLayoutAnimationController;
  private boolean mLayoutCalledWhileSuppressed = false;
  private int mLayoutMode = -1;
  private LayoutTransition.TransitionListener mLayoutTransitionListener = new LayoutTransition.TransitionListener()
  {
    public void endTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt)
    {
      if ((mLayoutCalledWhileSuppressed) && (!paramAnonymousLayoutTransition.isChangingLayout()))
      {
        requestLayout();
        ViewGroup.access$302(ViewGroup.this, false);
      }
      if ((paramAnonymousInt == 3) && (mTransitioningViews != null)) {
        endViewTransition(paramAnonymousView);
      }
    }
    
    public void startTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt)
    {
      if (paramAnonymousInt == 3) {
        startViewTransition(paramAnonymousView);
      }
    }
  };
  private PointF mLocalPoint;
  private int mNestedScrollAxes;
  protected OnHierarchyChangeListener mOnHierarchyChangeListener;
  protected int mPersistentDrawingCache;
  private ArrayList<View> mPreSortedChildren;
  boolean mSuppressLayout = false;
  private float[] mTempPoint;
  private View mTooltipHoverTarget;
  private boolean mTooltipHoveredSelf;
  private List<Integer> mTransientIndices = null;
  private List<View> mTransientViews = null;
  private LayoutTransition mTransition;
  private ArrayList<View> mTransitioningViews;
  private ArrayList<View> mVisibilityChangingChildren;
  
  public ViewGroup(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ViewGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ViewGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ViewGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initViewGroup();
    initFromAttributes(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void addDisappearingView(View paramView)
  {
    ArrayList localArrayList1 = mDisappearingChildren;
    ArrayList localArrayList2 = localArrayList1;
    if (localArrayList1 == null)
    {
      localArrayList2 = new ArrayList();
      mDisappearingChildren = localArrayList2;
    }
    localArrayList2.add(paramView);
  }
  
  private void addInArray(View paramView, int paramInt)
  {
    View[] arrayOfView1 = mChildren;
    int i = mChildrenCount;
    int j = arrayOfView1.length;
    if (paramInt == i)
    {
      View[] arrayOfView2 = arrayOfView1;
      if (j == i)
      {
        mChildren = new View[j + 12];
        System.arraycopy(arrayOfView1, 0, mChildren, 0, j);
        arrayOfView2 = mChildren;
      }
      paramInt = mChildrenCount;
      mChildrenCount = (paramInt + 1);
      arrayOfView2[paramInt] = paramView;
    }
    else
    {
      if (paramInt >= i) {
        break label187;
      }
      if (j == i)
      {
        mChildren = new View[j + 12];
        System.arraycopy(arrayOfView1, 0, mChildren, 0, paramInt);
        System.arraycopy(arrayOfView1, paramInt, mChildren, paramInt + 1, i - paramInt);
        arrayOfView1 = mChildren;
      }
      else
      {
        System.arraycopy(arrayOfView1, paramInt, arrayOfView1, paramInt + 1, i - paramInt);
      }
      arrayOfView1[paramInt] = paramView;
      mChildrenCount += 1;
      if (mLastTouchDownIndex >= paramInt) {
        mLastTouchDownIndex += 1;
      }
    }
    return;
    label187:
    paramView = new StringBuilder();
    paramView.append("index=");
    paramView.append(paramInt);
    paramView.append(" count=");
    paramView.append(i);
    throw new IndexOutOfBoundsException(paramView.toString());
  }
  
  private TouchTarget addTouchTarget(View paramView, int paramInt)
  {
    paramView = TouchTarget.obtain(paramView, paramInt);
    next = mFirstTouchTarget;
    mFirstTouchTarget = paramView;
    return paramView;
  }
  
  private void addViewInner(View paramView, int paramInt, LayoutParams paramLayoutParams, boolean paramBoolean)
  {
    if (mTransition != null) {
      mTransition.cancel(3);
    }
    if (paramView.getParent() == null)
    {
      if (mTransition != null) {
        mTransition.addChild(this, paramView);
      }
      LayoutParams localLayoutParams = paramLayoutParams;
      if (!checkLayoutParams(paramLayoutParams)) {
        localLayoutParams = generateLayoutParams(paramLayoutParams);
      }
      if (paramBoolean) {
        mLayoutParams = localLayoutParams;
      } else {
        paramView.setLayoutParams(localLayoutParams);
      }
      int i = paramInt;
      if (paramInt < 0) {
        i = mChildrenCount;
      }
      addInArray(paramView, i);
      if (paramBoolean) {
        paramView.assignParent(this);
      } else {
        mParent = this;
      }
      if (paramView.hasUnhandledKeyListener()) {
        incrementChildUnhandledKeyListeners();
      }
      if (paramView.hasFocus()) {
        requestChildFocus(paramView, paramView.findFocus());
      }
      paramLayoutParams = mAttachInfo;
      paramInt = 0;
      if ((paramLayoutParams != null) && ((mGroupFlags & 0x400000) == 0))
      {
        paramBoolean = mKeepScreenOn;
        mKeepScreenOn = false;
        paramView.dispatchAttachedToWindow(mAttachInfo, mViewFlags & 0xC);
        if (mKeepScreenOn) {
          needGlobalAttributesUpdate(true);
        }
        mKeepScreenOn = paramBoolean;
      }
      if (paramView.isLayoutDirectionInherited()) {
        paramView.resetRtlProperties();
      }
      dispatchViewAdded(paramView);
      if ((mViewFlags & 0x400000) == 4194304) {
        mGroupFlags |= 0x10000;
      }
      if (paramView.hasTransientState()) {
        childHasTransientStateChanged(paramView, true);
      }
      if (paramView.getVisibility() != 8) {
        notifySubtreeAccessibilityStateChangedIfNeeded();
      }
      if (mTransientIndices != null)
      {
        int j = mTransientIndices.size();
        while (paramInt < j)
        {
          int k = ((Integer)mTransientIndices.get(paramInt)).intValue();
          if (i <= k) {
            mTransientIndices.set(paramInt, Integer.valueOf(k + 1));
          }
          paramInt++;
        }
      }
      if ((mCurrentDragStartEvent != null) && (paramView.getVisibility() == 0)) {
        notifyChildOfDragStart(paramView);
      }
      if (paramView.hasDefaultFocus()) {
        setDefaultFocus(paramView);
      }
      touchAccessibilityNodeProviderIfNeeded(paramView);
      return;
    }
    throw new IllegalStateException("The specified child already has a parent. You must call removeView() on the child's parent first.");
  }
  
  private void bindLayoutAnimation(View paramView)
  {
    paramView.setAnimation(mLayoutAnimationController.getAnimationForView(paramView));
  }
  
  private static boolean canViewReceivePointerEvents(View paramView)
  {
    boolean bool;
    if (((mViewFlags & 0xC) != 0) && (paramView.getAnimation() == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void cancelAndClearTouchTargets(MotionEvent paramMotionEvent)
  {
    if (mFirstTouchTarget != null)
    {
      int i = 0;
      MotionEvent localMotionEvent = paramMotionEvent;
      if (paramMotionEvent == null)
      {
        long l = SystemClock.uptimeMillis();
        localMotionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        localMotionEvent.setSource(4098);
        i = 1;
      }
      for (paramMotionEvent = mFirstTouchTarget; paramMotionEvent != null; paramMotionEvent = next)
      {
        resetCancelNextUpFlag(child);
        dispatchTransformedTouchEvent(localMotionEvent, true, child, pointerIdBits);
      }
      clearTouchTargets();
      if (i != 0) {
        localMotionEvent.recycle();
      }
    }
  }
  
  private void cancelHoverTarget(View paramView)
  {
    Object localObject1 = null;
    HoverTarget localHoverTarget;
    for (Object localObject2 = mFirstHoverTarget; localObject2 != null; localObject2 = localHoverTarget)
    {
      localHoverTarget = next;
      if (child == paramView)
      {
        if (localObject1 == null) {
          mFirstHoverTarget = localHoverTarget;
        } else {
          next = localHoverTarget;
        }
        ((HoverTarget)localObject2).recycle();
        long l = SystemClock.uptimeMillis();
        localObject2 = MotionEvent.obtain(l, l, 10, 0.0F, 0.0F, 0);
        ((MotionEvent)localObject2).setSource(4098);
        paramView.dispatchHoverEvent((MotionEvent)localObject2);
        ((MotionEvent)localObject2).recycle();
        return;
      }
      localObject1 = localObject2;
    }
  }
  
  private void cancelTouchTarget(View paramView)
  {
    Object localObject1 = null;
    TouchTarget localTouchTarget;
    for (Object localObject2 = mFirstTouchTarget; localObject2 != null; localObject2 = localTouchTarget)
    {
      localTouchTarget = next;
      if (child == paramView)
      {
        if (localObject1 == null) {
          mFirstTouchTarget = localTouchTarget;
        } else {
          next = localTouchTarget;
        }
        ((TouchTarget)localObject2).recycle();
        long l = SystemClock.uptimeMillis();
        localObject2 = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        ((MotionEvent)localObject2).setSource(4098);
        paramView.dispatchTouchEvent((MotionEvent)localObject2);
        ((MotionEvent)localObject2).recycle();
        return;
      }
      localObject1 = localObject2;
    }
  }
  
  private void clearCachedLayoutMode()
  {
    if (!hasBooleanFlag(8388608)) {
      mLayoutMode = -1;
    }
  }
  
  private void clearTouchTargets()
  {
    Object localObject = mFirstTouchTarget;
    if (localObject != null)
    {
      TouchTarget localTouchTarget;
      do
      {
        localTouchTarget = next;
        ((TouchTarget)localObject).recycle();
        localObject = localTouchTarget;
      } while (localTouchTarget != null);
      mFirstTouchTarget = null;
    }
  }
  
  private PointerIcon dispatchResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt, View paramView)
  {
    if (!paramView.hasIdentityMatrix())
    {
      MotionEvent localMotionEvent = getTransformedMotionEvent(paramMotionEvent, paramView);
      paramMotionEvent = paramView.onResolvePointerIcon(localMotionEvent, paramInt);
      localMotionEvent.recycle();
    }
    else
    {
      float f1 = mScrollX - mLeft;
      float f2 = mScrollY - mTop;
      paramMotionEvent.offsetLocation(f1, f2);
      paramView = paramView.onResolvePointerIcon(paramMotionEvent, paramInt);
      paramMotionEvent.offsetLocation(-f1, -f2);
      paramMotionEvent = paramView;
    }
    return paramMotionEvent;
  }
  
  private boolean dispatchTooltipHoverEvent(MotionEvent paramMotionEvent, View paramView)
  {
    boolean bool;
    if (!paramView.hasIdentityMatrix())
    {
      paramMotionEvent = getTransformedMotionEvent(paramMotionEvent, paramView);
      bool = paramView.dispatchTooltipHoverEvent(paramMotionEvent);
      paramMotionEvent.recycle();
    }
    else
    {
      float f1 = mScrollX - mLeft;
      float f2 = mScrollY - mTop;
      paramMotionEvent.offsetLocation(f1, f2);
      bool = paramView.dispatchTooltipHoverEvent(paramMotionEvent);
      paramMotionEvent.offsetLocation(-f1, -f2);
    }
    return bool;
  }
  
  private boolean dispatchTransformedGenericPointerEvent(MotionEvent paramMotionEvent, View paramView)
  {
    boolean bool;
    if (!paramView.hasIdentityMatrix())
    {
      paramMotionEvent = getTransformedMotionEvent(paramMotionEvent, paramView);
      bool = paramView.dispatchGenericMotionEvent(paramMotionEvent);
      paramMotionEvent.recycle();
    }
    else
    {
      float f1 = mScrollX - mLeft;
      float f2 = mScrollY - mTop;
      paramMotionEvent.offsetLocation(f1, f2);
      bool = paramView.dispatchGenericMotionEvent(paramMotionEvent);
      paramMotionEvent.offsetLocation(-f1, -f2);
    }
    return bool;
  }
  
  private boolean dispatchTransformedTouchEvent(MotionEvent paramMotionEvent, boolean paramBoolean, View paramView, int paramInt)
  {
    int i = paramMotionEvent.getAction();
    if ((!paramBoolean) && (i != 3))
    {
      i = paramMotionEvent.getPointerIdBits();
      paramInt = i & paramInt;
      if (paramInt == 0) {
        return false;
      }
      if (paramInt == i)
      {
        if ((paramView != null) && (!paramView.hasIdentityMatrix()))
        {
          paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
        }
        else
        {
          if (paramView == null)
          {
            paramBoolean = super.dispatchTouchEvent(paramMotionEvent);
          }
          else
          {
            float f1 = mScrollX - mLeft;
            float f2 = mScrollY - mTop;
            paramMotionEvent.offsetLocation(f1, f2);
            paramBoolean = paramView.dispatchTouchEvent(paramMotionEvent);
            paramMotionEvent.offsetLocation(-f1, -f2);
          }
          return paramBoolean;
        }
      }
      else {
        paramMotionEvent = paramMotionEvent.split(paramInt);
      }
      if (paramView == null)
      {
        paramBoolean = super.dispatchTouchEvent(paramMotionEvent);
      }
      else
      {
        paramMotionEvent.offsetLocation(mScrollX - mLeft, mScrollY - mTop);
        if (!paramView.hasIdentityMatrix()) {
          paramMotionEvent.transform(paramView.getInverseMatrix());
        }
        paramBoolean = paramView.dispatchTouchEvent(paramMotionEvent);
      }
      paramMotionEvent.recycle();
      return paramBoolean;
    }
    paramMotionEvent.setAction(3);
    if (paramView == null) {
      paramBoolean = super.dispatchTouchEvent(paramMotionEvent);
    } else {
      paramBoolean = paramView.dispatchTouchEvent(paramMotionEvent);
    }
    paramMotionEvent.setAction(i);
    return paramBoolean;
  }
  
  private static void drawCorner(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    fillRect(paramCanvas, paramPaint, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + sign(paramInt4) * paramInt5);
    fillRect(paramCanvas, paramPaint, paramInt1, paramInt2, paramInt1 + sign(paramInt3) * paramInt5, paramInt2 + paramInt4);
  }
  
  private static void drawRect(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (sDebugLines == null) {
      sDebugLines = new float[16];
    }
    sDebugLines[0] = paramInt1;
    sDebugLines[1] = paramInt2;
    sDebugLines[2] = paramInt3;
    sDebugLines[3] = paramInt2;
    sDebugLines[4] = paramInt3;
    sDebugLines[5] = paramInt2;
    sDebugLines[6] = paramInt3;
    sDebugLines[7] = paramInt4;
    sDebugLines[8] = paramInt3;
    sDebugLines[9] = paramInt4;
    sDebugLines[10] = paramInt1;
    sDebugLines[11] = paramInt4;
    sDebugLines[12] = paramInt1;
    sDebugLines[13] = paramInt4;
    sDebugLines[14] = paramInt1;
    sDebugLines[15] = paramInt2;
    paramCanvas.drawLines(sDebugLines, paramPaint);
  }
  
  private static void drawRectCorners(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Paint paramPaint, int paramInt5, int paramInt6)
  {
    drawCorner(paramCanvas, paramPaint, paramInt1, paramInt2, paramInt5, paramInt5, paramInt6);
    drawCorner(paramCanvas, paramPaint, paramInt1, paramInt4, paramInt5, -paramInt5, paramInt6);
    drawCorner(paramCanvas, paramPaint, paramInt3, paramInt2, -paramInt5, paramInt5, paramInt6);
    drawCorner(paramCanvas, paramPaint, paramInt3, paramInt4, -paramInt5, -paramInt5, paramInt6);
  }
  
  private void exitHoverTargets()
  {
    if ((mHoveredSelf) || (mFirstHoverTarget != null))
    {
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 10, 0.0F, 0.0F, 0);
      localMotionEvent.setSource(4098);
      dispatchHoverEvent(localMotionEvent);
      localMotionEvent.recycle();
    }
  }
  
  private void exitTooltipHoverTargets()
  {
    if ((mTooltipHoveredSelf) || (mTooltipHoverTarget != null))
    {
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 10, 0.0F, 0.0F, 0);
      localMotionEvent.setSource(4098);
      dispatchTooltipHoverEvent(localMotionEvent);
      localMotionEvent.recycle();
    }
  }
  
  private static void fillDifference(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Paint paramPaint)
  {
    paramInt5 = paramInt1 - paramInt5;
    paramInt7 = paramInt3 + paramInt7;
    fillRect(paramCanvas, paramPaint, paramInt5, paramInt2 - paramInt6, paramInt7, paramInt2);
    fillRect(paramCanvas, paramPaint, paramInt5, paramInt2, paramInt1, paramInt4);
    fillRect(paramCanvas, paramPaint, paramInt3, paramInt2, paramInt7, paramInt4);
    fillRect(paramCanvas, paramPaint, paramInt5, paramInt4, paramInt7, paramInt4 + paramInt8);
  }
  
  private static void fillRect(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt1 != paramInt3) && (paramInt2 != paramInt4))
    {
      int i = paramInt1;
      int j = paramInt3;
      if (paramInt1 > paramInt3)
      {
        j = paramInt1;
        i = paramInt3;
      }
      paramInt3 = paramInt2;
      paramInt1 = paramInt4;
      if (paramInt2 > paramInt4)
      {
        paramInt3 = paramInt4;
        paramInt1 = paramInt2;
      }
      paramCanvas.drawRect(i, paramInt3, j, paramInt1, paramPaint);
    }
  }
  
  private View findChildWithAccessibilityFocus()
  {
    Object localObject = getViewRootImpl();
    if (localObject == null) {
      return null;
    }
    View localView = ((ViewRootImpl)localObject).getAccessibilityFocusedHost();
    if (localView == null) {
      return null;
    }
    for (localObject = localView.getParent(); (localObject instanceof View); localObject = localView.getParent())
    {
      if (localObject == this) {
        return localView;
      }
      localView = (View)localObject;
    }
    return null;
  }
  
  private int getAndVerifyPreorderedIndex(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramInt2 = getChildDrawingOrder(paramInt1, paramInt2);
      if (paramInt2 >= paramInt1)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("getChildDrawingOrder() returned invalid index ");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" (child count is ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(")");
        throw new IndexOutOfBoundsException(localStringBuilder.toString());
      }
    }
    return paramInt2;
  }
  
  private static View getAndVerifyPreorderedView(ArrayList<View> paramArrayList, View[] paramArrayOfView, int paramInt)
  {
    if (paramArrayList != null)
    {
      paramArrayList = (View)paramArrayList.get(paramInt);
      if (paramArrayList == null)
      {
        paramArrayList = new StringBuilder();
        paramArrayList.append("Invalid preorderedList contained null child at index ");
        paramArrayList.append(paramInt);
        throw new RuntimeException(paramArrayList.toString());
      }
    }
    else
    {
      paramArrayList = paramArrayOfView[paramInt];
    }
    return paramArrayList;
  }
  
  public static int getChildMeasureSpec(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = 0;
    paramInt1 = 0;
    j = Math.max(0, j - paramInt2);
    int m = 0;
    paramInt2 = 0;
    if (i != Integer.MIN_VALUE)
    {
      if (i != 0)
      {
        if (i != 1073741824)
        {
          paramInt1 = m;
        }
        else if (paramInt3 >= 0)
        {
          paramInt1 = paramInt3;
          paramInt2 = 1073741824;
        }
        else if (paramInt3 == -1)
        {
          paramInt1 = j;
          paramInt2 = 1073741824;
        }
        else
        {
          paramInt1 = m;
          if (paramInt3 == -2)
          {
            paramInt1 = j;
            paramInt2 = Integer.MIN_VALUE;
          }
        }
      }
      else if (paramInt3 >= 0)
      {
        paramInt1 = paramInt3;
        paramInt2 = 1073741824;
      }
      else if (paramInt3 == -1)
      {
        if (!View.sUseZeroUnspecifiedMeasureSpec) {
          paramInt1 = j;
        }
        paramInt2 = 0;
      }
      else
      {
        paramInt1 = m;
        if (paramInt3 == -2)
        {
          if (View.sUseZeroUnspecifiedMeasureSpec) {
            paramInt1 = k;
          } else {
            paramInt1 = j;
          }
          paramInt2 = 0;
        }
      }
    }
    else if (paramInt3 >= 0)
    {
      paramInt1 = paramInt3;
      paramInt2 = 1073741824;
    }
    else if (paramInt3 == -1)
    {
      paramInt1 = j;
      paramInt2 = Integer.MIN_VALUE;
    }
    else
    {
      paramInt1 = m;
      if (paramInt3 == -2)
      {
        paramInt1 = j;
        paramInt2 = Integer.MIN_VALUE;
      }
    }
    return View.MeasureSpec.makeMeasureSpec(paramInt1, paramInt2);
  }
  
  private ChildListForAutoFill getChildrenForAutofill(int paramInt)
  {
    ChildListForAutoFill localChildListForAutoFill = ChildListForAutoFill.obtain();
    populateChildrenForAutofill(localChildListForAutoFill, paramInt);
    return localChildListForAutoFill;
  }
  
  private PointF getLocalPoint()
  {
    if (mLocalPoint == null) {
      mLocalPoint = new PointF();
    }
    return mLocalPoint;
  }
  
  private float[] getTempPoint()
  {
    if (mTempPoint == null) {
      mTempPoint = new float[2];
    }
    return mTempPoint;
  }
  
  private TouchTarget getTouchTarget(View paramView)
  {
    for (TouchTarget localTouchTarget = mFirstTouchTarget; localTouchTarget != null; localTouchTarget = next) {
      if (child == paramView) {
        return localTouchTarget;
      }
    }
    return null;
  }
  
  private MotionEvent getTransformedMotionEvent(MotionEvent paramMotionEvent, View paramView)
  {
    float f1 = mScrollX - mLeft;
    float f2 = mScrollY - mTop;
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.offsetLocation(f1, f2);
    if (!paramView.hasIdentityMatrix()) {
      paramMotionEvent.transform(paramView.getInverseMatrix());
    }
    return paramMotionEvent;
  }
  
  private boolean hasBooleanFlag(int paramInt)
  {
    boolean bool;
    if ((mGroupFlags & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean hasChildWithZ()
  {
    for (int i = 0; i < mChildrenCount; i++) {
      if (mChildren[i].getZ() != 0.0F) {
        return true;
      }
    }
    return false;
  }
  
  private void initFromAttributes(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ViewGroup, paramInt1, paramInt2);
    paramInt2 = paramContext.getIndexCount();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      int i = paramContext.getIndex(paramInt1);
      switch (i)
      {
      default: 
        break;
      case 12: 
        setTouchscreenBlocksFocus(paramContext.getBoolean(i, false));
        break;
      case 11: 
        setTransitionGroup(paramContext.getBoolean(i, false));
        break;
      case 10: 
        setLayoutMode(paramContext.getInt(i, -1));
        break;
      case 9: 
        if (paramContext.getBoolean(i, false)) {
          setLayoutTransition(new LayoutTransition());
        }
        break;
      case 8: 
        setMotionEventSplittingEnabled(paramContext.getBoolean(i, false));
        break;
      case 7: 
        setDescendantFocusability(DESCENDANT_FOCUSABILITY_FLAGS[paramContext.getInt(i, 0)]);
        break;
      case 6: 
        setAddStatesFromChildren(paramContext.getBoolean(i, false));
        break;
      case 5: 
        setAlwaysDrawnWithCacheEnabled(paramContext.getBoolean(i, true));
        break;
      case 4: 
        setPersistentDrawingCache(paramContext.getInt(i, 2));
        break;
      case 3: 
        setAnimationCacheEnabled(paramContext.getBoolean(i, true));
        break;
      case 2: 
        i = paramContext.getResourceId(i, -1);
        if (i > 0) {
          setLayoutAnimation(AnimationUtils.loadLayoutAnimation(mContext, i));
        }
        break;
      case 1: 
        setClipToPadding(paramContext.getBoolean(i, true));
        break;
      case 0: 
        setClipChildren(paramContext.getBoolean(i, true));
      }
    }
    paramContext.recycle();
  }
  
  private void initViewGroup()
  {
    if (!debugDraw()) {
      setFlags(128, 128);
    }
    mGroupFlags |= 0x1;
    mGroupFlags |= 0x2;
    mGroupFlags |= 0x10;
    mGroupFlags |= 0x40;
    mGroupFlags |= 0x4000;
    if (mContext.getApplicationInfo().targetSdkVersion >= 11) {
      mGroupFlags |= 0x200000;
    }
    setDescendantFocusability(131072);
    mChildren = new View[12];
    mChildrenCount = 0;
    mPersistentDrawingCache = 2;
  }
  
  private void notifyAnimationListener()
  {
    mGroupFlags &= 0xFDFF;
    mGroupFlags |= 0x10;
    if (mAnimationListener != null) {
      post(new Runnable()
      {
        public void run()
        {
          mAnimationListener.onAnimationEnd(mLayoutAnimationController.getAnimation());
        }
      });
    }
    invalidate(true);
  }
  
  private static MotionEvent obtainMotionEventNoHistoryOrSelf(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getHistorySize() == 0) {
      return paramMotionEvent;
    }
    return MotionEvent.obtainNoHistory(paramMotionEvent);
  }
  
  private void populateChildrenForAutofill(ArrayList<View> paramArrayList, int paramInt)
  {
    int i = mChildrenCount;
    if (i <= 0) {
      return;
    }
    ArrayList localArrayList = buildOrderedChildList();
    int j = 0;
    boolean bool;
    if ((localArrayList == null) && (isChildrenDrawingOrderEnabled())) {
      bool = true;
    } else {
      bool = false;
    }
    while (j < i)
    {
      int k = getAndVerifyPreorderedIndex(i, j, bool);
      View localView;
      if (localArrayList == null) {
        localView = mChildren[k];
      } else {
        localView = (View)localArrayList.get(k);
      }
      if (((paramInt & 0x1) == 0) && (!localView.isImportantForAutofill()))
      {
        if ((localView instanceof ViewGroup)) {
          ((ViewGroup)localView).populateChildrenForAutofill(paramArrayList, paramInt);
        }
      }
      else {
        paramArrayList.add(localView);
      }
      j++;
    }
  }
  
  private void recreateChildDisplayList(View paramView)
  {
    boolean bool;
    if ((mPrivateFlags & 0x80000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mRecreateDisplayList = bool;
    mPrivateFlags &= 0x7FFFFFFF;
    paramView.updateDisplayListIfDirty();
    mRecreateDisplayList = false;
  }
  
  private void removeFromArray(int paramInt)
  {
    View[] arrayOfView = mChildren;
    if ((mTransitioningViews == null) || (!mTransitioningViews.contains(arrayOfView[paramInt]))) {
      mParent = null;
    }
    int i = mChildrenCount;
    if (paramInt == i - 1)
    {
      i = mChildrenCount - 1;
      mChildrenCount = i;
      arrayOfView[i] = null;
    }
    else
    {
      if ((paramInt < 0) || (paramInt >= i)) {
        break label142;
      }
      System.arraycopy(arrayOfView, paramInt + 1, arrayOfView, paramInt, i - paramInt - 1);
      i = mChildrenCount - 1;
      mChildrenCount = i;
      arrayOfView[i] = null;
    }
    if (mLastTouchDownIndex == paramInt)
    {
      mLastTouchDownTime = 0L;
      mLastTouchDownIndex = -1;
    }
    else if (mLastTouchDownIndex > paramInt)
    {
      mLastTouchDownIndex -= 1;
    }
    return;
    label142:
    throw new IndexOutOfBoundsException();
  }
  
  private void removeFromArray(int paramInt1, int paramInt2)
  {
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    paramInt1 = Math.max(0, paramInt1);
    int j = Math.min(i, paramInt1 + paramInt2);
    if (paramInt1 == j) {
      return;
    }
    if (j == i) {
      for (paramInt2 = paramInt1; paramInt2 < j; paramInt2++)
      {
        mParent = null;
        arrayOfView[paramInt2] = null;
      }
    }
    for (paramInt2 = paramInt1; paramInt2 < j; paramInt2++) {
      mParent = null;
    }
    System.arraycopy(arrayOfView, j, arrayOfView, paramInt1, i - j);
    for (paramInt2 = i - (j - paramInt1); paramInt2 < i; paramInt2++) {
      arrayOfView[paramInt2] = null;
    }
    mChildrenCount -= j - paramInt1;
  }
  
  private void removePointersFromTouchTargets(int paramInt)
  {
    Object localObject1 = null;
    Object localObject2 = mFirstTouchTarget;
    while (localObject2 != null)
    {
      TouchTarget localTouchTarget = next;
      if ((pointerIdBits & paramInt) != 0)
      {
        pointerIdBits &= paramInt;
        if (pointerIdBits == 0)
        {
          if (localObject1 == null) {
            mFirstTouchTarget = localTouchTarget;
          } else {
            next = localTouchTarget;
          }
          ((TouchTarget)localObject2).recycle();
          localObject2 = localTouchTarget;
          continue;
        }
      }
      localObject1 = localObject2;
      localObject2 = localTouchTarget;
    }
  }
  
  private void removeViewInternal(int paramInt, View paramView)
  {
    if (mTransition != null) {
      mTransition.removeChild(this, paramView);
    }
    int i = 0;
    if (paramView == mFocused)
    {
      paramView.unFocus(null);
      i = 1;
    }
    if (paramView == mFocusedInCluster) {
      clearFocusedInCluster(paramView);
    }
    paramView.clearAccessibilityFocus();
    cancelTouchTarget(paramView);
    cancelHoverTarget(paramView);
    if ((paramView.getAnimation() == null) && ((mTransitioningViews == null) || (!mTransitioningViews.contains(paramView))))
    {
      if (mAttachInfo != null) {
        paramView.dispatchDetachedFromWindow();
      }
    }
    else {
      addDisappearingView(paramView);
    }
    boolean bool = paramView.hasTransientState();
    int j = 0;
    if (bool) {
      childHasTransientStateChanged(paramView, false);
    }
    needGlobalAttributesUpdate(false);
    removeFromArray(paramInt);
    if (paramView.hasUnhandledKeyListener()) {
      decrementChildUnhandledKeyListeners();
    }
    if (paramView == mDefaultFocus) {
      clearDefaultFocus(paramView);
    }
    if (i != 0)
    {
      clearChildFocus(paramView);
      if (!rootViewRequestFocus()) {
        notifyGlobalFocusCleared(this);
      }
    }
    dispatchViewRemoved(paramView);
    if (paramView.getVisibility() != 8) {
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
    if (mTransientIndices == null) {
      i = 0;
    } else {
      i = mTransientIndices.size();
    }
    while (j < i)
    {
      int k = ((Integer)mTransientIndices.get(j)).intValue();
      if (paramInt < k) {
        mTransientIndices.set(j, Integer.valueOf(k - 1));
      }
      j++;
    }
    if (mCurrentDragStartEvent != null) {
      mChildrenInterestedInDrag.remove(paramView);
    }
  }
  
  private boolean removeViewInternal(View paramView)
  {
    int i = indexOfChild(paramView);
    if (i >= 0)
    {
      removeViewInternal(i, paramView);
      return true;
    }
    return false;
  }
  
  private void removeViewsInternal(int paramInt1, int paramInt2)
  {
    int i = paramInt1 + paramInt2;
    if ((paramInt1 >= 0) && (paramInt2 >= 0) && (i <= mChildrenCount))
    {
      View localView1 = mFocused;
      int j;
      if (mAttachInfo != null) {
        j = 1;
      } else {
        j = 0;
      }
      Object localObject = null;
      View[] arrayOfView = mChildren;
      int k = 0;
      for (int m = paramInt1; m < i; m++)
      {
        View localView2 = arrayOfView[m];
        if (mTransition != null) {
          mTransition.removeChild(this, localView2);
        }
        if (localView2 == localView1)
        {
          localView2.unFocus(null);
          k = 1;
        }
        if (localView2 == mDefaultFocus) {
          localObject = localView2;
        }
        if (localView2 == mFocusedInCluster) {
          clearFocusedInCluster(localView2);
        }
        localView2.clearAccessibilityFocus();
        cancelTouchTarget(localView2);
        cancelHoverTarget(localView2);
        if ((localView2.getAnimation() == null) && ((mTransitioningViews == null) || (!mTransitioningViews.contains(localView2))))
        {
          if (j != 0) {
            localView2.dispatchDetachedFromWindow();
          }
        }
        else {
          addDisappearingView(localView2);
        }
        if (localView2.hasTransientState()) {
          childHasTransientStateChanged(localView2, false);
        }
        needGlobalAttributesUpdate(false);
        dispatchViewRemoved(localView2);
      }
      removeFromArray(paramInt1, paramInt2);
      if (localObject != null) {
        clearDefaultFocus(localObject);
      }
      if (k != 0)
      {
        clearChildFocus(localView1);
        if (!rootViewRequestFocus()) {
          notifyGlobalFocusCleared(localView1);
        }
      }
      return;
    }
    throw new IndexOutOfBoundsException();
  }
  
  private static boolean resetCancelNextUpFlag(View paramView)
  {
    if ((mPrivateFlags & 0x4000000) != 0)
    {
      mPrivateFlags &= 0xFBFFFFFF;
      return true;
    }
    return false;
  }
  
  private void resetTouchState()
  {
    clearTouchTargets();
    resetCancelNextUpFlag(this);
    mGroupFlags &= 0xFFF7FFFF;
    mNestedScrollAxes = 0;
  }
  
  private boolean restoreFocusInClusterInternal(int paramInt)
  {
    if ((mFocusedInCluster != null) && (getDescendantFocusability() != 393216) && ((mFocusedInCluster.mViewFlags & 0xC) == 0) && (mFocusedInCluster.restoreFocusInCluster(paramInt))) {
      return true;
    }
    return super.restoreFocusInCluster(paramInt);
  }
  
  private void setBooleanFlag(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean) {
      mGroupFlags |= paramInt;
    } else {
      mGroupFlags &= paramInt;
    }
  }
  
  private void setLayoutMode(int paramInt, boolean paramBoolean)
  {
    mLayoutMode = paramInt;
    setBooleanFlag(8388608, paramBoolean);
  }
  
  private void setTouchscreenBlocksFocusNoRefocus(boolean paramBoolean)
  {
    if (paramBoolean) {
      mGroupFlags |= 0x4000000;
    } else {
      mGroupFlags &= 0xFBFFFFFF;
    }
  }
  
  private static int sign(int paramInt)
  {
    if (paramInt >= 0) {
      paramInt = 1;
    } else {
      paramInt = -1;
    }
    return paramInt;
  }
  
  private void touchAccessibilityNodeProviderIfNeeded(View paramView)
  {
    if (mContext.isAutofillCompatibilityEnabled()) {
      paramView.getAccessibilityNodeProvider();
    }
  }
  
  public void addChildrenForAccessibility(ArrayList<View> paramArrayList)
  {
    if (getAccessibilityNodeProvider() != null) {
      return;
    }
    ChildListForAccessibility localChildListForAccessibility = ChildListForAccessibility.obtain(this, true);
    try
    {
      int i = localChildListForAccessibility.getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = localChildListForAccessibility.getChildAt(j);
        if ((mViewFlags & 0xC) == 0) {
          if (localView.includeForAccessibility()) {
            paramArrayList.add(localView);
          } else {
            localView.addChildrenForAccessibility(paramArrayList);
          }
        }
      }
      return;
    }
    finally
    {
      localChildListForAccessibility.recycle();
    }
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    boolean bool1 = shouldBlockFocusForTouchscreen();
    boolean bool2 = isFocusableInTouchMode();
    int k = 0;
    int m;
    if ((!bool2) && (bool1)) {
      m = 0;
    } else {
      m = 1;
    }
    if (j == 393216)
    {
      if (m != 0) {
        super.addFocusables(paramArrayList, paramInt1, paramInt2);
      }
      return;
    }
    int n = paramInt2;
    if (bool1) {
      n = paramInt2 | 0x1;
    }
    if ((j == 131072) && (m != 0)) {
      super.addFocusables(paramArrayList, paramInt1, n);
    }
    View[] arrayOfView = new View[mChildrenCount];
    paramInt2 = 0;
    int i1 = 0;
    while (i1 < mChildrenCount)
    {
      View localView = mChildren[i1];
      int i2 = paramInt2;
      if ((mViewFlags & 0xC) == 0)
      {
        arrayOfView[paramInt2] = localView;
        i2 = paramInt2 + 1;
      }
      i1++;
      paramInt2 = i2;
    }
    FocusFinder.sort(arrayOfView, 0, paramInt2, this, isLayoutRtl());
    for (i1 = k; i1 < paramInt2; i1++) {
      arrayOfView[i1].addFocusables(paramArrayList, paramInt1, n);
    }
    if ((j == 262144) && (m != 0) && (i == paramArrayList.size())) {
      super.addFocusables(paramArrayList, paramInt1, n);
    }
  }
  
  public void addKeyboardNavigationClusters(Collection<View> paramCollection, int paramInt)
  {
    int i = paramCollection.size();
    boolean bool = isKeyboardNavigationCluster();
    int j = 0;
    if (bool) {
      bool = getTouchscreenBlocksFocus();
    }
    try
    {
      setTouchscreenBlocksFocusNoRefocus(false);
      super.addKeyboardNavigationClusters(paramCollection, paramInt);
      setTouchscreenBlocksFocusNoRefocus(bool);
    }
    finally
    {
      setTouchscreenBlocksFocusNoRefocus(bool);
    }
    if (i != paramCollection.size()) {
      return;
    }
    if (getDescendantFocusability() == 393216) {
      return;
    }
    View[] arrayOfView = new View[mChildrenCount];
    i = 0;
    int k = 0;
    while (k < mChildrenCount)
    {
      View localView = mChildren[k];
      m = i;
      if ((mViewFlags & 0xC) == 0)
      {
        arrayOfView[i] = localView;
        m = i + 1;
      }
      k++;
      i = m;
    }
    FocusFinder.sort(arrayOfView, 0, i, this, isLayoutRtl());
    for (int m = j; m < i; m++) {
      arrayOfView[m].addKeyboardNavigationClusters(paramCollection, paramInt);
    }
  }
  
  public boolean addStatesFromChildren()
  {
    boolean bool;
    if ((mGroupFlags & 0x2000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void addTouchables(ArrayList<View> paramArrayList)
  {
    super.addTouchables(paramArrayList);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((mViewFlags & 0xC) == 0) {
        localView.addTouchables(paramArrayList);
      }
    }
  }
  
  public void addTransientView(View paramView, int paramInt)
  {
    if (paramInt < 0) {
      return;
    }
    if (mTransientIndices == null)
    {
      mTransientIndices = new ArrayList();
      mTransientViews = new ArrayList();
    }
    int i = mTransientIndices.size();
    if (i > 0)
    {
      for (int j = 0; (j < i) && (paramInt >= ((Integer)mTransientIndices.get(j)).intValue()); j++) {}
      mTransientIndices.add(j, Integer.valueOf(paramInt));
      mTransientViews.add(j, paramView);
    }
    else
    {
      mTransientIndices.add(Integer.valueOf(paramInt));
      mTransientViews.add(paramView);
    }
    mParent = this;
    paramView.dispatchAttachedToWindow(mAttachInfo, mViewFlags & 0xC);
    invalidate(true);
  }
  
  public void addView(View paramView)
  {
    addView(paramView, -1);
  }
  
  public void addView(View paramView, int paramInt)
  {
    if (paramView != null)
    {
      LayoutParams localLayoutParams1 = paramView.getLayoutParams();
      LayoutParams localLayoutParams2 = localLayoutParams1;
      if (localLayoutParams1 == null)
      {
        localLayoutParams2 = generateDefaultLayoutParams();
        if (localLayoutParams2 == null) {
          throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
        }
      }
      addView(paramView, paramInt, localLayoutParams2);
      return;
    }
    throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
  }
  
  public void addView(View paramView, int paramInt1, int paramInt2)
  {
    LayoutParams localLayoutParams = generateDefaultLayoutParams();
    width = paramInt1;
    height = paramInt2;
    addView(paramView, -1, localLayoutParams);
  }
  
  public void addView(View paramView, int paramInt, LayoutParams paramLayoutParams)
  {
    if (paramView != null)
    {
      requestLayout();
      invalidate(true);
      addViewInner(paramView, paramInt, paramLayoutParams, false);
      return;
    }
    throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
  }
  
  public void addView(View paramView, LayoutParams paramLayoutParams)
  {
    addView(paramView, -1, paramLayoutParams);
  }
  
  protected boolean addViewInLayout(View paramView, int paramInt, LayoutParams paramLayoutParams)
  {
    return addViewInLayout(paramView, paramInt, paramLayoutParams, false);
  }
  
  protected boolean addViewInLayout(View paramView, int paramInt, LayoutParams paramLayoutParams, boolean paramBoolean)
  {
    if (paramView != null)
    {
      mParent = null;
      addViewInner(paramView, paramInt, paramLayoutParams, paramBoolean);
      mPrivateFlags = (mPrivateFlags & 0xFF9FFFFF | 0x20);
      return true;
    }
    throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
  }
  
  protected void attachLayoutAnimationParameters(View paramView, LayoutParams paramLayoutParams, int paramInt1, int paramInt2)
  {
    LayoutAnimationController.AnimationParameters localAnimationParameters = layoutAnimationParameters;
    paramView = localAnimationParameters;
    if (localAnimationParameters == null)
    {
      paramView = new LayoutAnimationController.AnimationParameters();
      layoutAnimationParameters = paramView;
    }
    count = paramInt2;
    index = paramInt1;
  }
  
  protected void attachViewToParent(View paramView, int paramInt, LayoutParams paramLayoutParams)
  {
    mLayoutParams = paramLayoutParams;
    int i = paramInt;
    if (paramInt < 0) {
      i = mChildrenCount;
    }
    addInArray(paramView, i);
    mParent = this;
    mPrivateFlags = (mPrivateFlags & 0xFF9FFFFF & 0xFFFF7FFF | 0x20 | 0x80000000);
    mPrivateFlags |= 0x80000000;
    if (paramView.hasFocus()) {
      requestChildFocus(paramView, paramView.findFocus());
    }
    boolean bool;
    if ((isAttachedToWindow()) && (getWindowVisibility() == 0) && (isShown())) {
      bool = true;
    } else {
      bool = false;
    }
    dispatchVisibilityAggregated(bool);
    notifySubtreeAccessibilityStateChangedIfNeeded();
  }
  
  public void bringChildToFront(View paramView)
  {
    int i = indexOfChild(paramView);
    if (i >= 0)
    {
      removeFromArray(i);
      addInArray(paramView, mChildrenCount);
      mParent = this;
      requestLayout();
      invalidate();
    }
  }
  
  ArrayList<View> buildOrderedChildList()
  {
    int i = mChildrenCount;
    if ((i > 1) && (hasChildWithZ()))
    {
      if (mPreSortedChildren == null)
      {
        mPreSortedChildren = new ArrayList(i);
      }
      else
      {
        mPreSortedChildren.clear();
        mPreSortedChildren.ensureCapacity(i);
      }
      boolean bool = isChildrenDrawingOrderEnabled();
      for (int j = 0; j < i; j++)
      {
        int k = getAndVerifyPreorderedIndex(i, j, bool);
        View localView = mChildren[k];
        float f = localView.getZ();
        for (k = j; (k > 0) && (((View)mPreSortedChildren.get(k - 1)).getZ() > f); k--) {}
        mPreSortedChildren.add(k, localView);
      }
      return mPreSortedChildren;
    }
    return null;
  }
  
  public ArrayList<View> buildTouchDispatchChildList()
  {
    return buildOrderedChildList();
  }
  
  protected boolean canAnimate()
  {
    boolean bool;
    if (mLayoutAnimationController != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void captureTransitioningViews(List<View> paramList)
  {
    if (getVisibility() != 0) {
      return;
    }
    if (isTransitionGroup())
    {
      paramList.add(this);
    }
    else
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        getChildAt(j).captureTransitioningViews(paramList);
      }
    }
  }
  
  protected boolean checkLayoutParams(LayoutParams paramLayoutParams)
  {
    boolean bool;
    if (paramLayoutParams != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void childDrawableStateChanged(View paramView)
  {
    if ((mGroupFlags & 0x2000) != 0) {
      refreshDrawableState();
    }
  }
  
  public void childHasTransientStateChanged(View paramView, boolean paramBoolean)
  {
    boolean bool = hasTransientState();
    if (paramBoolean) {
      mChildCountWithTransientState += 1;
    } else {
      mChildCountWithTransientState -= 1;
    }
    paramBoolean = hasTransientState();
    if ((mParent != null) && (bool != paramBoolean)) {
      try
      {
        mParent.childHasTransientStateChanged(this, paramBoolean);
      }
      catch (AbstractMethodError paramView)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(mParent.getClass().getSimpleName());
        localStringBuilder.append(" does not fully implement ViewParent");
        Log.e("ViewGroup", localStringBuilder.toString(), paramView);
      }
    }
  }
  
  protected void cleanupLayoutState(View paramView)
  {
    mPrivateFlags &= 0xEFFF;
  }
  
  public void clearChildFocus(View paramView)
  {
    mFocused = null;
    if (mParent != null) {
      mParent.clearChildFocus(this);
    }
  }
  
  void clearDefaultFocus(View paramView)
  {
    if ((mDefaultFocus != paramView) && (mDefaultFocus != null) && (mDefaultFocus.isFocusedByDefault())) {
      return;
    }
    mDefaultFocus = null;
    for (int i = 0; i < mChildrenCount; i++)
    {
      paramView = mChildren[i];
      if (paramView.isFocusedByDefault())
      {
        mDefaultFocus = paramView;
        return;
      }
      if ((mDefaultFocus == null) && (paramView.hasDefaultFocus())) {
        mDefaultFocus = paramView;
      }
    }
    if ((mParent instanceof ViewGroup)) {
      ((ViewGroup)mParent).clearDefaultFocus(this);
    }
  }
  
  public void clearDisappearingChildren()
  {
    ArrayList localArrayList = mDisappearingChildren;
    if (localArrayList != null)
    {
      int i = localArrayList.size();
      for (int j = 0; j < i; j++)
      {
        View localView = (View)localArrayList.get(j);
        if (mAttachInfo != null) {
          localView.dispatchDetachedFromWindow();
        }
        localView.clearAnimation();
      }
      localArrayList.clear();
      invalidate();
    }
  }
  
  public void clearFocus()
  {
    if (mFocused == null)
    {
      super.clearFocus();
    }
    else
    {
      View localView = mFocused;
      mFocused = null;
      localView.clearFocus();
    }
  }
  
  void clearFocusedInCluster()
  {
    View localView = findKeyboardNavigationCluster();
    Object localObject = this;
    ViewParent localViewParent;
    do
    {
      mFocusedInCluster = null;
      if (localObject == localView) {
        break;
      }
      localViewParent = ((ViewParent)localObject).getParent();
      localObject = localViewParent;
    } while ((localViewParent instanceof ViewGroup));
  }
  
  void clearFocusedInCluster(View paramView)
  {
    if (mFocusedInCluster != paramView) {
      return;
    }
    clearFocusedInCluster();
  }
  
  Insets computeOpticalInsets()
  {
    if (isLayoutModeOptical())
    {
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      while (n < mChildrenCount)
      {
        Object localObject = getChildAt(n);
        int i1 = i;
        int i2 = j;
        int i3 = k;
        int i4 = m;
        if (((View)localObject).getVisibility() == 0)
        {
          localObject = ((View)localObject).getOpticalInsets();
          i1 = Math.max(i, left);
          i2 = Math.max(j, top);
          i3 = Math.max(k, right);
          i4 = Math.max(m, bottom);
        }
        n++;
        i = i1;
        j = i2;
        k = i3;
        m = i4;
      }
      return Insets.of(i, j, k, m);
    }
    return Insets.NONE;
  }
  
  public Bitmap createSnapshot(ViewDebug.CanvasProvider paramCanvasProvider, boolean paramBoolean)
  {
    int i = mChildrenCount;
    Object localObject1 = null;
    int j = 0;
    int k = 0;
    Object localObject2;
    int m;
    if (paramBoolean)
    {
      localObject2 = new int[i];
      for (m = 0;; m++)
      {
        localObject1 = localObject2;
        if (m >= i) {
          break;
        }
        localObject1 = getChildAt(m);
        localObject2[m] = ((View)localObject1).getVisibility();
        if (localObject2[m] == 0) {
          mViewFlags = (mViewFlags & 0xFFFFFFF3 | 0x4);
        }
      }
    }
    try
    {
      paramCanvasProvider = super.createSnapshot(paramCanvasProvider, paramBoolean);
      if (paramBoolean) {
        for (m = k; m < i; m++)
        {
          localObject2 = getChildAt(m);
          mViewFlags = (mViewFlags & 0xFFFFFFF3 | localObject1[m] & 0xC);
        }
      }
      return paramCanvasProvider;
    }
    finally
    {
      if (paramBoolean) {
        for (m = j; m < i; m++)
        {
          paramCanvasProvider = getChildAt(m);
          mViewFlags = (mViewFlags & 0xFFFFFFF3 | localObject1[m] & 0xC);
        }
      }
    }
  }
  
  protected void debug(int paramInt)
  {
    super.debug(paramInt);
    Object localObject1;
    Object localObject2;
    if (mFocused != null)
    {
      localObject1 = debugIndent(paramInt);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("mFocused");
      Log.d("View", ((StringBuilder)localObject2).toString());
      mFocused.debug(paramInt + 1);
    }
    if (mDefaultFocus != null)
    {
      localObject2 = debugIndent(paramInt);
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("mDefaultFocus");
      Log.d("View", ((StringBuilder)localObject1).toString());
      mDefaultFocus.debug(paramInt + 1);
    }
    if (mFocusedInCluster != null)
    {
      localObject2 = debugIndent(paramInt);
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("mFocusedInCluster");
      Log.d("View", ((StringBuilder)localObject1).toString());
      mFocusedInCluster.debug(paramInt + 1);
    }
    if (mChildrenCount != 0)
    {
      localObject1 = debugIndent(paramInt);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("{");
      Log.d("View", ((StringBuilder)localObject2).toString());
    }
    int i = mChildrenCount;
    for (int j = 0; j < i; j++) {
      mChildren[j].debug(paramInt + 1);
    }
    if (mChildrenCount != 0)
    {
      localObject2 = debugIndent(paramInt);
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("}");
      Log.d("View", ((StringBuilder)localObject1).toString());
    }
  }
  
  void decrementChildUnhandledKeyListeners()
  {
    mChildUnhandledKeyListeners -= 1;
    if ((mChildUnhandledKeyListeners == 0) && ((mParent instanceof ViewGroup))) {
      ((ViewGroup)mParent).decrementChildUnhandledKeyListeners();
    }
  }
  
  protected void destroyHardwareResources()
  {
    super.destroyHardwareResources();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).destroyHardwareResources();
    }
  }
  
  protected void detachAllViewsFromParent()
  {
    int i = mChildrenCount;
    if (i <= 0) {
      return;
    }
    View[] arrayOfView = mChildren;
    mChildrenCount = 0;
    i--;
    while (i >= 0)
    {
      mParent = null;
      arrayOfView[i] = null;
      i--;
    }
  }
  
  protected void detachViewFromParent(int paramInt)
  {
    removeFromArray(paramInt);
  }
  
  protected void detachViewFromParent(View paramView)
  {
    removeFromArray(indexOfChild(paramView));
  }
  
  protected void detachViewsFromParent(int paramInt1, int paramInt2)
  {
    removeFromArray(paramInt1, paramInt2);
  }
  
  public boolean dispatchActivityResult(String paramString, int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (super.dispatchActivityResult(paramString, paramInt1, paramInt2, paramIntent)) {
      return true;
    }
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      if (getChildAt(j).dispatchActivityResult(paramString, paramInt1, paramInt2, paramIntent)) {
        return true;
      }
    }
    return false;
  }
  
  public WindowInsets dispatchApplyWindowInsets(WindowInsets paramWindowInsets)
  {
    WindowInsets localWindowInsets = super.dispatchApplyWindowInsets(paramWindowInsets);
    paramWindowInsets = localWindowInsets;
    if (!localWindowInsets.isConsumed())
    {
      int i = getChildCount();
      int j = 0;
      for (;;)
      {
        paramWindowInsets = localWindowInsets;
        if (j >= i) {
          break;
        }
        paramWindowInsets = getChildAt(j).dispatchApplyWindowInsets(localWindowInsets);
        if (paramWindowInsets.isConsumed()) {
          break;
        }
        j++;
        localWindowInsets = paramWindowInsets;
      }
    }
    return paramWindowInsets;
  }
  
  void dispatchAttachedToWindow(View.AttachInfo paramAttachInfo, int paramInt)
  {
    mGroupFlags |= 0x400000;
    super.dispatchAttachedToWindow(paramAttachInfo, paramInt);
    mGroupFlags &= 0xFFBFFFFF;
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    int j = 0;
    View localView;
    for (int k = 0; k < i; k++)
    {
      localView = arrayOfView[k];
      localView.dispatchAttachedToWindow(paramAttachInfo, combineVisibility(paramInt, localView.getVisibility()));
    }
    if (mTransientIndices == null) {
      k = 0;
    } else {
      k = mTransientIndices.size();
    }
    while (j < k)
    {
      localView = (View)mTransientViews.get(j);
      localView.dispatchAttachedToWindow(paramAttachInfo, combineVisibility(paramInt, localView.getVisibility()));
      j++;
    }
  }
  
  void dispatchCancelPendingInputEvents()
  {
    super.dispatchCancelPendingInputEvents();
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchCancelPendingInputEvents();
    }
  }
  
  public boolean dispatchCapturedPointerEvent(MotionEvent paramMotionEvent)
  {
    if ((mPrivateFlags & 0x12) == 18)
    {
      if (super.dispatchCapturedPointerEvent(paramMotionEvent)) {
        return true;
      }
    }
    else if ((mFocused != null) && ((mFocused.mPrivateFlags & 0x10) == 16) && (mFocused.dispatchCapturedPointerEvent(paramMotionEvent))) {
      return true;
    }
    return false;
  }
  
  void dispatchCollectViewAttributes(View.AttachInfo paramAttachInfo, int paramInt)
  {
    if ((paramInt & 0xC) == 0)
    {
      super.dispatchCollectViewAttributes(paramAttachInfo, paramInt);
      int i = mChildrenCount;
      View[] arrayOfView = mChildren;
      for (int j = 0; j < i; j++)
      {
        View localView = arrayOfView[j];
        localView.dispatchCollectViewAttributes(paramAttachInfo, mViewFlags & 0xC | paramInt);
      }
    }
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration)
  {
    super.dispatchConfigurationChanged(paramConfiguration);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchConfigurationChanged(paramConfiguration);
    }
  }
  
  void dispatchDetachedFromWindow()
  {
    cancelAndClearTouchTargets(null);
    exitHoverTargets();
    exitTooltipHoverTargets();
    int i = 0;
    mLayoutCalledWhileSuppressed = false;
    mChildrenInterestedInDrag = null;
    mIsInterestedInDrag = false;
    if (mCurrentDragStartEvent != null)
    {
      mCurrentDragStartEvent.recycle();
      mCurrentDragStartEvent = null;
    }
    int j = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int k = 0; k < j; k++) {
      arrayOfView[k].dispatchDetachedFromWindow();
    }
    clearDisappearingChildren();
    if (mTransientViews == null) {
      k = 0;
    } else {
      k = mTransientIndices.size();
    }
    while (i < k)
    {
      ((View)mTransientViews.get(i)).dispatchDetachedFromWindow();
      i++;
    }
    super.dispatchDetachedFromWindow();
  }
  
  public void dispatchDisplayHint(int paramInt)
  {
    super.dispatchDisplayHint(paramInt);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchDisplayHint(paramInt);
    }
  }
  
  boolean dispatchDragEnterExitInPreN(DragEvent paramDragEvent)
  {
    if ((mAction == 6) && (mCurrentDragChild != null))
    {
      mCurrentDragChild.dispatchDragEnterExitInPreN(paramDragEvent);
      mCurrentDragChild = null;
    }
    boolean bool;
    if ((mIsInterestedInDrag) && (super.dispatchDragEnterExitInPreN(paramDragEvent))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean dispatchDragEvent(DragEvent paramDragEvent)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    float f1 = mX;
    float f2 = mY;
    Object localObject1 = mClipData;
    PointF localPointF = getLocalPoint();
    int i = mAction;
    int j = 0;
    Object localObject2;
    switch (i)
    {
    default: 
      bool1 = bool3;
      break;
    case 4: 
      localObject1 = mChildrenInterestedInDrag;
      if (localObject1 != null)
      {
        localObject2 = ((HashSet)localObject1).iterator();
        bool1 = bool4;
        while (((Iterator)localObject2).hasNext()) {
          if (((View)((Iterator)localObject2).next()).dispatchDragEvent(paramDragEvent)) {
            bool1 = true;
          }
        }
        ((HashSet)localObject1).clear();
      }
      if (mCurrentDragStartEvent != null)
      {
        mCurrentDragStartEvent.recycle();
        mCurrentDragStartEvent = null;
      }
      bool4 = bool1;
      if (mIsInterestedInDrag)
      {
        if (super.dispatchDragEvent(paramDragEvent)) {
          bool1 = true;
        }
        mIsInterestedInDrag = false;
        bool4 = bool1;
      }
      bool1 = bool4;
      break;
    case 2: 
    case 3: 
      localObject2 = findFrontmostDroppableChildAt(mX, mY, localPointF);
      if (localObject2 != mCurrentDragChild)
      {
        if (sCascadedDragDrop)
        {
          j = mAction;
          mX = 0.0F;
          mY = 0.0F;
          mClipData = null;
          if (mCurrentDragChild != null)
          {
            mAction = 6;
            mCurrentDragChild.dispatchDragEnterExitInPreN(paramDragEvent);
          }
          if (localObject2 != null)
          {
            mAction = 5;
            ((View)localObject2).dispatchDragEnterExitInPreN(paramDragEvent);
          }
          mAction = j;
          mX = f1;
          mY = f2;
          mClipData = ((ClipData)localObject1);
        }
        mCurrentDragChild = ((View)localObject2);
      }
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = localObject2;
        if (mIsInterestedInDrag) {
          localObject1 = this;
        }
      }
      bool1 = bool3;
      if (localObject1 != null) {
        if (localObject1 != this)
        {
          mX = x;
          mY = y;
          bool4 = ((View)localObject1).dispatchDragEvent(paramDragEvent);
          mX = f1;
          mY = f2;
          bool1 = bool4;
          if (mIsInterestedInDrag)
          {
            if (sCascadedDragDrop) {
              bool2 = bool4;
            } else {
              bool2 = mEventHandlerWasCalled;
            }
            bool1 = bool4;
            if (!bool2) {
              bool1 = super.dispatchDragEvent(paramDragEvent);
            }
          }
        }
        else
        {
          bool1 = super.dispatchDragEvent(paramDragEvent);
        }
      }
      break;
    case 1: 
      mCurrentDragChild = null;
      mCurrentDragStartEvent = DragEvent.obtain(paramDragEvent);
      if (mChildrenInterestedInDrag == null) {
        mChildrenInterestedInDrag = new HashSet();
      } else {
        mChildrenInterestedInDrag.clear();
      }
      i = mChildrenCount;
      localObject2 = mChildren;
      for (bool1 = bool2; j < i; bool1 = bool4)
      {
        localObject1 = localObject2[j];
        mPrivateFlags2 &= 0xFFFFFFFC;
        bool4 = bool1;
        if (((View)localObject1).getVisibility() == 0)
        {
          bool4 = bool1;
          if (notifyChildOfDragStart(localObject2[j])) {
            bool4 = true;
          }
        }
        j++;
      }
      mIsInterestedInDrag = super.dispatchDragEvent(paramDragEvent);
      if (mIsInterestedInDrag) {
        bool1 = true;
      }
      if (!bool1)
      {
        mCurrentDragStartEvent.recycle();
        mCurrentDragStartEvent = null;
      }
      break;
    }
    return bool1;
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    boolean bool1 = paramCanvas.isRecordingFor(mRenderNode);
    int i = mChildrenCount;
    Object localObject1 = mChildren;
    int j = mGroupFlags;
    int n = 0;
    Object localObject2;
    if (((j & 0x8) != 0) && (canAnimate()))
    {
      isHardwareAccelerated();
      for (i1 = 0; i1 < i; i1++)
      {
        localObject2 = localObject1[i1];
        if ((mViewFlags & 0xC) == 0)
        {
          attachLayoutAnimationParameters((View)localObject2, ((View)localObject2).getLayoutParams(), i1, i);
          bindLayoutAnimation((View)localObject2);
        }
      }
      localObject2 = mLayoutAnimationController;
      if (((LayoutAnimationController)localObject2).willOverlap()) {
        mGroupFlags |= 0x80;
      }
      ((LayoutAnimationController)localObject2).start();
      mGroupFlags &= 0xFFFFFFF7;
      mGroupFlags &= 0xFFFFFFEF;
      if (mAnimationListener != null) {
        mAnimationListener.onAnimationStart(((LayoutAnimationController)localObject2).getAnimation());
      }
    }
    int i4 = 0;
    int i5;
    if ((j & 0x22) == 34) {
      i5 = 1;
    } else {
      i5 = 0;
    }
    if (i5 != 0)
    {
      i4 = paramCanvas.save(2);
      paramCanvas.clipRect(mScrollX + mPaddingLeft, mScrollY + mPaddingTop, mScrollX + mRight - mLeft - mPaddingRight, mScrollY + mBottom - mTop - mPaddingBottom);
    }
    mPrivateFlags &= 0xFFFFFFBF;
    mGroupFlags &= 0xFFFFFFFB;
    int i1 = 0;
    long l = getDrawingTime();
    if (bool1) {
      paramCanvas.insertReorderBarrier();
    }
    int i6;
    if (mTransientIndices == null) {
      i6 = 0;
    } else {
      i6 = mTransientIndices.size();
    }
    int i7;
    if (i6 != 0) {
      i7 = 0;
    } else {
      i7 = -1;
    }
    if (bool1) {
      localObject2 = null;
    } else {
      localObject2 = buildOrderedChildList();
    }
    boolean bool2;
    if ((localObject2 == null) && (isChildrenDrawingOrderEnabled())) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    while (n < i)
    {
      int i9 = i7;
      int i10 = i1;
      i1 = j;
      while ((i9 >= 0) && (((Integer)mTransientIndices.get(i9)).intValue() == n))
      {
        localView = (View)mTransientViews.get(i9);
        if ((mViewFlags & 0xC) != 0)
        {
          j = i10;
          if (localView.getAnimation() == null) {}
        }
        else
        {
          k = drawChild(paramCanvas, localView, l) | i10;
        }
        i10 = i9 + 1;
        i7 = i10;
        if (i10 >= i6) {
          i7 = -1;
        }
        i10 = k;
        i9 = i7;
      }
      View localView = getAndVerifyPreorderedView((ArrayList)localObject2, (View[])localObject1, getAndVerifyPreorderedIndex(i, n, bool2));
      if ((mViewFlags & 0xC) != 0)
      {
        i7 = i10;
        if (localView.getAnimation() == null) {}
      }
      else
      {
        i8 = i10 | drawChild(paramCanvas, localView, l);
      }
      n++;
      k = i1;
      i1 = i8;
      i8 = i9;
    }
    int k = i8;
    int i2;
    for (int i8 = i1;; i8 = i2)
    {
      i1 = i8;
      if (k < 0) {
        break;
      }
      localObject1 = (View)mTransientViews.get(k);
      if ((mViewFlags & 0xC) != 0)
      {
        i1 = i8;
        if (((View)localObject1).getAnimation() == null) {}
      }
      else
      {
        i2 = drawChild(paramCanvas, (View)localObject1, l) | i8;
      }
      k++;
      if (k >= i6) {
        break;
      }
    }
    if (localObject2 != null) {
      ((ArrayList)localObject2).clear();
    }
    i8 = i2;
    if (mDisappearingChildren != null)
    {
      localObject2 = mDisappearingChildren;
      for (int m = ((ArrayList)localObject2).size() - 1;; m--)
      {
        i8 = i2;
        if (m < 0) {
          break;
        }
        i2 |= drawChild(paramCanvas, (View)((ArrayList)localObject2).get(m), l);
      }
    }
    if (bool1) {
      paramCanvas.insertInorderBarrier();
    }
    if (debugDraw()) {
      onDebugDraw(paramCanvas);
    }
    if (i5 != 0) {
      paramCanvas.restoreToCount(i4);
    }
    int i3 = mGroupFlags;
    if ((i3 & 0x4) == 4) {
      invalidate(true);
    }
    if (((i3 & 0x10) == 0) && ((i3 & 0x200) == 0) && (mLayoutAnimationController.isDone()) && (i8 == 0))
    {
      mGroupFlags |= 0x200;
      post(new Runnable()
      {
        public void run()
        {
          ViewGroup.this.notifyAnimationListener();
        }
      });
    }
  }
  
  public void dispatchDrawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    int i = mChildrenCount;
    if (i == 0) {
      return;
    }
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      int k;
      if ((!localView.isClickable()) && (!localView.isLongClickable())) {
        k = 1;
      } else {
        k = 0;
      }
      int m;
      if ((mViewFlags & 0x400000) != 0) {
        m = 1;
      } else {
        m = 0;
      }
      if ((k != 0) || (m != 0))
      {
        float[] arrayOfFloat = getTempPoint();
        arrayOfFloat[0] = paramFloat1;
        arrayOfFloat[1] = paramFloat2;
        transformPointToViewLocal(arrayOfFloat, localView);
        localView.drawableHotspotChanged(arrayOfFloat[0], arrayOfFloat[1]);
      }
    }
  }
  
  public void dispatchFinishTemporaryDetach()
  {
    super.dispatchFinishTemporaryDetach();
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchFinishTemporaryDetach();
    }
  }
  
  protected void dispatchFreezeSelfOnly(SparseArray<Parcelable> paramSparseArray)
  {
    super.dispatchSaveInstanceState(paramSparseArray);
  }
  
  protected boolean dispatchGenericFocusedEvent(MotionEvent paramMotionEvent)
  {
    if ((mPrivateFlags & 0x12) == 18) {
      return super.dispatchGenericFocusedEvent(paramMotionEvent);
    }
    if ((mFocused != null) && ((mFocused.mPrivateFlags & 0x10) == 16)) {
      return mFocused.dispatchGenericMotionEvent(paramMotionEvent);
    }
    return false;
  }
  
  protected boolean dispatchGenericPointerEvent(MotionEvent paramMotionEvent)
  {
    int i = mChildrenCount;
    if (i != 0)
    {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      ArrayList localArrayList = buildOrderedChildList();
      boolean bool;
      if ((localArrayList == null) && (isChildrenDrawingOrderEnabled())) {
        bool = true;
      } else {
        bool = false;
      }
      View[] arrayOfView = mChildren;
      for (int j = i - 1; j >= 0; j--)
      {
        View localView = getAndVerifyPreorderedView(localArrayList, arrayOfView, getAndVerifyPreorderedIndex(i, j, bool));
        if ((canViewReceivePointerEvents(localView)) && (isTransformedTouchPointInView(f1, f2, localView, null)) && (dispatchTransformedGenericPointerEvent(paramMotionEvent, localView)))
        {
          if (localArrayList != null) {
            localArrayList.clear();
          }
          return true;
        }
      }
      if (localArrayList != null) {
        localArrayList.clear();
      }
    }
    return super.dispatchGenericPointerEvent(paramMotionEvent);
  }
  
  protected void dispatchGetDisplayList()
  {
    int i = mChildrenCount;
    Object localObject = mChildren;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      View localView = localObject[k];
      if (((mViewFlags & 0xC) == 0) || (localView.getAnimation() != null)) {
        recreateChildDisplayList(localView);
      }
    }
    if (mTransientViews == null) {
      k = 0;
    } else {
      k = mTransientIndices.size();
    }
    for (i = 0; i < k; i++)
    {
      localObject = (View)mTransientViews.get(i);
      if (((mViewFlags & 0xC) == 0) || (((View)localObject).getAnimation() != null)) {
        recreateChildDisplayList((View)localObject);
      }
    }
    if (mOverlay != null) {
      recreateChildDisplayList(mOverlay.getOverlayView());
    }
    if (mDisappearingChildren != null)
    {
      localObject = mDisappearingChildren;
      i = ((ArrayList)localObject).size();
      for (k = j; k < i; k++) {
        recreateChildDisplayList((View)((ArrayList)localObject).get(k));
      }
    }
  }
  
  protected boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    boolean bool1 = onInterceptHoverEvent(paramMotionEvent);
    paramMotionEvent.setAction(i);
    boolean bool2 = false;
    boolean bool3 = false;
    Object localObject1 = mFirstHoverTarget;
    mFirstHoverTarget = null;
    boolean bool4;
    if ((!bool1) && (i != 10))
    {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      int j = mChildrenCount;
      if (j != 0)
      {
        ArrayList localArrayList = buildOrderedChildList();
        if ((localArrayList == null) && (isChildrenDrawingOrderEnabled())) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        View[] arrayOfView = mChildren;
        int k = j - 1;
        localObject2 = paramMotionEvent;
        Object localObject3 = null;
        localObject4 = localObject1;
        while (k >= 0)
        {
          View localView = getAndVerifyPreorderedView(localArrayList, arrayOfView, getAndVerifyPreorderedIndex(j, k, bool2));
          if (canViewReceivePointerEvents(localView))
          {
            if (!isTransformedTouchPointInView(f1, f2, localView, null))
            {
              localObject5 = localObject4;
              bool4 = bool2;
            }
            else
            {
              localObject5 = localObject4;
              Object localObject6 = null;
              for (localObject1 = localObject4;; localObject1 = next)
              {
                int m;
                if (localObject1 == null)
                {
                  localObject1 = HoverTarget.obtain(localView);
                  m = 0;
                  localObject4 = localObject5;
                }
                else
                {
                  if (child != localView) {
                    break label432;
                  }
                  if (localObject6 != null)
                  {
                    next = next;
                    localObject4 = localObject5;
                  }
                  else
                  {
                    localObject4 = next;
                  }
                  next = null;
                  m = 1;
                }
                bool4 = bool2;
                if (localObject3 != null) {
                  next = ((HoverTarget)localObject1);
                } else {
                  mFirstHoverTarget = ((HoverTarget)localObject1);
                }
                localObject3 = localObject1;
                if (i == 9)
                {
                  bool2 = bool3;
                  localObject1 = localObject2;
                  if (m == 0)
                  {
                    bool2 = bool3 | dispatchTransformedGenericPointerEvent(paramMotionEvent, localView);
                    localObject1 = localObject2;
                  }
                }
                else
                {
                  bool2 = bool3;
                  localObject1 = localObject2;
                  if (i == 7) {
                    if (m == 0)
                    {
                      localObject1 = obtainMotionEventNoHistoryOrSelf((MotionEvent)localObject2);
                      ((MotionEvent)localObject1).setAction(9);
                      bool2 = dispatchTransformedGenericPointerEvent((MotionEvent)localObject1, localView);
                      ((MotionEvent)localObject1).setAction(i);
                      bool2 = bool3 | bool2 | dispatchTransformedGenericPointerEvent((MotionEvent)localObject1, localView);
                    }
                    else
                    {
                      bool2 = bool3 | dispatchTransformedGenericPointerEvent(paramMotionEvent, localView);
                      localObject1 = localObject2;
                    }
                  }
                }
                bool3 = bool2;
                localObject2 = localObject1;
                localObject5 = localObject4;
                if (!bool2) {
                  break;
                }
                bool3 = bool2;
                localObject5 = localObject4;
                break label476;
                label432:
                localObject6 = localObject1;
              }
            }
          }
          else
          {
            bool4 = bool2;
            localObject5 = localObject4;
          }
          localObject4 = localObject5;
          k--;
          bool2 = bool4;
        }
        localObject1 = localObject2;
        Object localObject5 = localObject4;
        label476:
        bool2 = bool3;
        localObject2 = localObject5;
        localObject4 = localObject1;
        if (localArrayList == null) {
          break label520;
        }
        localArrayList.clear();
        bool2 = bool3;
        localObject2 = localObject5;
        localObject4 = localObject1;
        break label520;
      }
    }
    Object localObject4 = paramMotionEvent;
    for (Object localObject2 = localObject1; localObject2 != null; localObject2 = localObject1)
    {
      label520:
      localObject1 = child;
      if (i == 10)
      {
        bool2 = dispatchTransformedGenericPointerEvent(paramMotionEvent, (View)localObject1) | bool2;
      }
      else
      {
        if (i == 7)
        {
          bool3 = paramMotionEvent.isHoverExitPending();
          paramMotionEvent.setHoverExitPending(true);
          dispatchTransformedGenericPointerEvent(paramMotionEvent, (View)localObject1);
          paramMotionEvent.setHoverExitPending(bool3);
        }
        localObject4 = obtainMotionEventNoHistoryOrSelf((MotionEvent)localObject4);
        ((MotionEvent)localObject4).setAction(10);
        dispatchTransformedGenericPointerEvent((MotionEvent)localObject4, (View)localObject1);
        ((MotionEvent)localObject4).setAction(i);
      }
      localObject1 = next;
      ((HoverTarget)localObject2).recycle();
    }
    if ((!bool2) && (i != 10) && (!paramMotionEvent.isHoverExitPending())) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    if (bool1 == mHoveredSelf)
    {
      bool3 = bool2;
      localObject1 = localObject4;
      if (bool1)
      {
        bool3 = bool2 | super.dispatchHoverEvent(paramMotionEvent);
        localObject1 = localObject4;
      }
    }
    else
    {
      bool4 = bool2;
      localObject2 = localObject4;
      if (mHoveredSelf)
      {
        if (i == 10)
        {
          bool2 = super.dispatchHoverEvent(paramMotionEvent) | bool2;
        }
        else
        {
          if (i == 7) {
            super.dispatchHoverEvent(paramMotionEvent);
          }
          localObject4 = obtainMotionEventNoHistoryOrSelf((MotionEvent)localObject4);
          ((MotionEvent)localObject4).setAction(10);
          super.dispatchHoverEvent((MotionEvent)localObject4);
          ((MotionEvent)localObject4).setAction(i);
        }
        mHoveredSelf = false;
        localObject2 = localObject4;
        bool4 = bool2;
      }
      bool3 = bool4;
      localObject1 = localObject2;
      if (bool1) {
        if (i == 9)
        {
          bool3 = bool4 | super.dispatchHoverEvent(paramMotionEvent);
          mHoveredSelf = true;
          localObject1 = localObject2;
        }
        else
        {
          bool3 = bool4;
          localObject1 = localObject2;
          if (i == 7)
          {
            localObject1 = obtainMotionEventNoHistoryOrSelf((MotionEvent)localObject2);
            ((MotionEvent)localObject1).setAction(9);
            bool2 = super.dispatchHoverEvent((MotionEvent)localObject1);
            ((MotionEvent)localObject1).setAction(i);
            bool3 = super.dispatchHoverEvent((MotionEvent)localObject1) | bool2 | bool4;
            mHoveredSelf = true;
          }
        }
      }
    }
    if (localObject1 != paramMotionEvent) {
      ((MotionEvent)localObject1).recycle();
    }
    return bool3;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onKeyEvent(paramKeyEvent, 1);
    }
    if ((mPrivateFlags & 0x12) == 18)
    {
      if (super.dispatchKeyEvent(paramKeyEvent)) {
        return true;
      }
    }
    else if ((mFocused != null) && ((mFocused.mPrivateFlags & 0x10) == 16) && (mFocused.dispatchKeyEvent(paramKeyEvent))) {
      return true;
    }
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramKeyEvent, 1);
    }
    return false;
  }
  
  public boolean dispatchKeyEventPreIme(KeyEvent paramKeyEvent)
  {
    if ((mPrivateFlags & 0x12) == 18) {
      return super.dispatchKeyEventPreIme(paramKeyEvent);
    }
    if ((mFocused != null) && ((mFocused.mPrivateFlags & 0x10) == 16)) {
      return mFocused.dispatchKeyEventPreIme(paramKeyEvent);
    }
    return false;
  }
  
  public boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    if ((mPrivateFlags & 0x12) == 18) {
      return super.dispatchKeyShortcutEvent(paramKeyEvent);
    }
    if ((mFocused != null) && ((mFocused.mPrivateFlags & 0x10) == 16)) {
      return mFocused.dispatchKeyShortcutEvent(paramKeyEvent);
    }
    return false;
  }
  
  void dispatchMovedToDisplay(Display paramDisplay, Configuration paramConfiguration)
  {
    super.dispatchMovedToDisplay(paramDisplay, paramConfiguration);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchMovedToDisplay(paramDisplay, paramConfiguration);
    }
  }
  
  public void dispatchPointerCaptureChanged(boolean paramBoolean)
  {
    exitHoverTargets();
    super.dispatchPointerCaptureChanged(paramBoolean);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchPointerCaptureChanged(paramBoolean);
    }
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    bool1 = false;
    if (includeForAccessibility())
    {
      boolean bool2 = super.dispatchPopulateAccessibilityEventInternal(paramAccessibilityEvent);
      bool1 = bool2;
      if (bool2) {
        return bool2;
      }
    }
    localChildListForAccessibility = ChildListForAccessibility.obtain(this, true);
    try
    {
      i = localChildListForAccessibility.getChildCount();
      j = 0;
    }
    finally
    {
      try
      {
        int i;
        int j;
        View localView;
        return bool1;
        j++;
      }
      finally
      {
        break label103;
        localChildListForAccessibility.recycle();
        return false;
        paramAccessibilityEvent = finally;
        localChildListForAccessibility.recycle();
      }
    }
    if (j >= i) {}
  }
  
  public void dispatchProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    super.dispatchProvideAutofillStructure(paramViewStructure, paramInt);
    if (paramViewStructure.getChildCount() != 0) {
      return;
    }
    if (!isLaidOut())
    {
      if (Helper.sVerbose)
      {
        paramViewStructure = new StringBuilder();
        paramViewStructure.append("dispatchProvideAutofillStructure(): not laid out, ignoring ");
        paramViewStructure.append(mChildrenCount);
        paramViewStructure.append(" children of ");
        paramViewStructure.append(getAutofillId());
        Log.v("View", paramViewStructure.toString());
      }
      return;
    }
    ChildListForAutoFill localChildListForAutoFill = getChildrenForAutofill(paramInt);
    int i = localChildListForAutoFill.size();
    paramViewStructure.setChildCount(i);
    for (int j = 0; j < i; j++) {
      ((View)localChildListForAutoFill.get(j)).dispatchProvideAutofillStructure(paramViewStructure.newChild(j), paramInt);
    }
    localChildListForAutoFill.recycle();
  }
  
  public void dispatchProvideStructure(ViewStructure paramViewStructure)
  {
    super.dispatchProvideStructure(paramViewStructure);
    if ((!isAssistBlocked()) && (paramViewStructure.getChildCount() == 0))
    {
      int i = mChildrenCount;
      if (i <= 0) {
        return;
      }
      if (!isLaidOut())
      {
        if (Helper.sVerbose)
        {
          paramViewStructure = new StringBuilder();
          paramViewStructure.append("dispatchProvideStructure(): not laid out, ignoring ");
          paramViewStructure.append(i);
          paramViewStructure.append(" children of ");
          paramViewStructure.append(getAccessibilityViewId());
          Log.v("View", paramViewStructure.toString());
        }
        return;
      }
      paramViewStructure.setChildCount(i);
      Object localObject = buildOrderedChildList();
      boolean bool1;
      if ((localObject == null) && (isChildrenDrawingOrderEnabled())) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      int j = 0;
      while (j < i)
      {
        int m;
        try
        {
          k = getAndVerifyPreorderedIndex(i, j, bool1);
        }
        catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
        {
          m = j;
          if (mContext.getApplicationInfo().targetSdkVersion >= 23) {
            break label413;
          }
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Bad getChildDrawingOrder while collecting assist @ ");
        localStringBuilder.append(j);
        localStringBuilder.append(" of ");
        localStringBuilder.append(i);
        Log.w("ViewGroup", localStringBuilder.toString(), localIndexOutOfBoundsException);
        boolean bool2 = false;
        bool1 = bool2;
        int k = m;
        int[] arrayOfInt;
        if (j > 0)
        {
          arrayOfInt = new int[i];
          localObject = new SparseBooleanArray();
          for (k = 0; k < j; k++)
          {
            arrayOfInt[k] = getChildDrawingOrder(i, k);
            ((SparseBooleanArray)localObject).put(arrayOfInt[k], true);
          }
          k = 0;
          for (int n = j; n < i; n++)
          {
            while (((SparseBooleanArray)localObject).get(k, false)) {
              k++;
            }
            arrayOfInt[n] = k;
            k++;
          }
          localObject = new ArrayList(i);
          for (k = 0; k < i; k++)
          {
            n = arrayOfInt[k];
            ((ArrayList)localObject).add(mChildren[n]);
          }
          k = m;
          bool1 = bool2;
        }
        getAndVerifyPreorderedView((ArrayList)localObject, mChildren, k).dispatchProvideStructure(paramViewStructure.newChild(j));
        j++;
        continue;
        label413:
        throw arrayOfInt;
      }
      if (localObject != null) {
        ((ArrayList)localObject).clear();
      }
      return;
    }
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    super.dispatchRestoreInstanceState(paramSparseArray);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((mViewFlags & 0x20000000) != 536870912) {
        localView.dispatchRestoreInstanceState(paramSparseArray);
      }
    }
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    super.dispatchSaveInstanceState(paramSparseArray);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((mViewFlags & 0x20000000) != 536870912) {
        localView.dispatchSaveInstanceState(paramSparseArray);
      }
    }
  }
  
  void dispatchScreenStateChanged(int paramInt)
  {
    super.dispatchScreenStateChanged(paramInt);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchScreenStateChanged(paramInt);
    }
  }
  
  public void dispatchSetActivated(boolean paramBoolean)
  {
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].setActivated(paramBoolean);
    }
  }
  
  protected void dispatchSetPressed(boolean paramBoolean)
  {
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((!paramBoolean) || ((!localView.isClickable()) && (!localView.isLongClickable()))) {
        localView.setPressed(paramBoolean);
      }
    }
  }
  
  public void dispatchSetSelected(boolean paramBoolean)
  {
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].setSelected(paramBoolean);
    }
  }
  
  public void dispatchStartTemporaryDetach()
  {
    super.dispatchStartTemporaryDetach();
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchStartTemporaryDetach();
    }
  }
  
  public void dispatchSystemUiVisibilityChanged(int paramInt)
  {
    super.dispatchSystemUiVisibilityChanged(paramInt);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchSystemUiVisibilityChanged(paramInt);
    }
  }
  
  protected void dispatchThawSelfOnly(SparseArray<Parcelable> paramSparseArray)
  {
    super.dispatchRestoreInstanceState(paramSparseArray);
  }
  
  boolean dispatchTooltipHoverEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if (i != 7)
    {
      switch (i)
      {
      default: 
        break;
      case 10: 
        if (mTooltipHoverTarget != null)
        {
          mTooltipHoverTarget.dispatchTooltipHoverEvent(paramMotionEvent);
          mTooltipHoverTarget = null;
        }
        else if (mTooltipHoveredSelf)
        {
          super.dispatchTooltipHoverEvent(paramMotionEvent);
          mTooltipHoveredSelf = false;
        }
        break;
      }
      return false;
    }
    Object localObject1 = null;
    Object localObject2 = null;
    int j = mChildrenCount;
    if (j != 0)
    {
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      ArrayList localArrayList = buildOrderedChildList();
      boolean bool;
      if ((localArrayList == null) && (isChildrenDrawingOrderEnabled())) {
        bool = true;
      } else {
        bool = false;
      }
      localObject1 = mChildren;
      Object localObject3;
      for (int k = j - 1;; k--)
      {
        localObject3 = localObject2;
        if (k < 0) {
          break;
        }
        int m = getAndVerifyPreorderedIndex(j, k, bool);
        localObject3 = getAndVerifyPreorderedView(localArrayList, (View[])localObject1, m);
        if ((canViewReceivePointerEvents((View)localObject3)) && (isTransformedTouchPointInView(f1, f2, (View)localObject3, null)) && (dispatchTooltipHoverEvent(paramMotionEvent, (View)localObject3))) {
          break;
        }
      }
      localObject1 = localObject3;
      if (localArrayList != null)
      {
        localArrayList.clear();
        localObject1 = localObject3;
      }
    }
    if (mTooltipHoverTarget != localObject1)
    {
      if (mTooltipHoverTarget != null)
      {
        paramMotionEvent.setAction(10);
        mTooltipHoverTarget.dispatchTooltipHoverEvent(paramMotionEvent);
        paramMotionEvent.setAction(i);
      }
      mTooltipHoverTarget = ((View)localObject1);
    }
    if (mTooltipHoverTarget != null)
    {
      if (mTooltipHoveredSelf)
      {
        mTooltipHoveredSelf = false;
        paramMotionEvent.setAction(10);
        super.dispatchTooltipHoverEvent(paramMotionEvent);
        paramMotionEvent.setAction(i);
      }
      return true;
    }
    mTooltipHoveredSelf = super.dispatchTooltipHoverEvent(paramMotionEvent);
    return mTooltipHoveredSelf;
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onTouchEvent(paramMotionEvent, 1);
    }
    if ((paramMotionEvent.isTargetAccessibilityFocus()) && (isAccessibilityFocusedViewOrHost())) {
      paramMotionEvent.setTargetAccessibilityFocus(false);
    }
    boolean bool1 = false;
    if (onFilterTouchEventForSecurity(paramMotionEvent))
    {
      int i = paramMotionEvent.getAction();
      int j = i & 0xFF;
      if (j == 0)
      {
        cancelAndClearTouchTargets(paramMotionEvent);
        resetTouchState();
      }
      int k;
      int m;
      if ((j != 0) && (mFirstTouchTarget == null))
      {
        k = 1;
      }
      else
      {
        if ((mGroupFlags & 0x80000) != 0) {
          m = 1;
        } else {
          m = 0;
        }
        if (m == 0)
        {
          bool2 = onInterceptTouchEvent(paramMotionEvent);
          paramMotionEvent.setAction(i);
        }
        else
        {
          bool2 = false;
        }
        k = bool2;
      }
      if ((k != 0) || (mFirstTouchTarget != null)) {
        paramMotionEvent.setTargetAccessibilityFocus(false);
      }
      boolean bool3;
      if ((!resetCancelNextUpFlag(this)) && (j != 3)) {
        bool3 = false;
      } else {
        bool3 = true;
      }
      int n;
      if ((mGroupFlags & 0x200000) != 0) {
        n = 1;
      } else {
        n = 0;
      }
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3 = null;
      i = 0;
      Object localObject4;
      if ((!bool3) && (k == 0))
      {
        if (paramMotionEvent.isTargetAccessibilityFocus()) {
          localObject4 = findChildWithAccessibilityFocus();
        } else {
          localObject4 = null;
        }
        if ((j != 0) && ((n == 0) || (j != 5)) && (j != 7)) {
          break label711;
        }
        int i1 = paramMotionEvent.getActionIndex();
        int i2;
        if (n != 0) {
          i2 = 1 << paramMotionEvent.getPointerId(i1);
        } else {
          i2 = -1;
        }
        removePointersFromTouchTargets(i2);
        int i3 = mChildrenCount;
        if ((0 == 0) && (i3 != 0))
        {
          float f1 = paramMotionEvent.getX(i1);
          float f2 = paramMotionEvent.getY(i1);
          localObject1 = buildTouchDispatchChildList();
          if ((localObject1 == null) && (isChildrenDrawingOrderEnabled())) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          localObject2 = mChildren;
          for (m = i3 - 1; m >= 0; m--)
          {
            int i4 = getAndVerifyPreorderedIndex(i3, m, bool2);
            View localView = getAndVerifyPreorderedView((ArrayList)localObject1, (View[])localObject2, i4);
            if (localObject4 != null)
            {
              if (localObject4 != localView) {
                continue;
              }
              localObject4 = null;
              m = i3 - 1;
            }
            if ((canViewReceivePointerEvents(localView)) && (isTransformedTouchPointInView(f1, f2, localView, null)))
            {
              localObject3 = getTouchTarget(localView);
              if (localObject3 != null)
              {
                pointerIdBits |= i2;
                localObject4 = localObject3;
              }
              for (;;)
              {
                m = i;
                break label622;
                resetCancelNextUpFlag(localView);
                if (!dispatchTransformedTouchEvent(paramMotionEvent, false, localView, i2)) {
                  break;
                }
                mLastTouchDownTime = paramMotionEvent.getDownTime();
                if (localObject1 != null) {
                  for (m = 0; m < i3; m++) {
                    if (localObject2[i4] == mChildren[m])
                    {
                      mLastTouchDownIndex = m;
                      break;
                    }
                  }
                }
                mLastTouchDownIndex = i4;
                mLastTouchDownX = paramMotionEvent.getX();
                mLastTouchDownY = paramMotionEvent.getY();
                localObject4 = addTouchTarget(localView, i2);
                i = 1;
              }
              paramMotionEvent.setTargetAccessibilityFocus(false);
            }
            else
            {
              paramMotionEvent.setTargetAccessibilityFocus(false);
            }
          }
          m = i;
          localObject4 = localObject3;
          label622:
          if (localObject1 != null) {
            ((ArrayList)localObject1).clear();
          }
        }
        else
        {
          m = 0;
          localObject4 = localObject1;
        }
        localObject3 = localObject4;
        i = m;
        if (localObject4 != null) {
          break label717;
        }
        localObject3 = localObject4;
        i = m;
        if (mFirstTouchTarget == null) {
          break label717;
        }
        for (localObject3 = mFirstTouchTarget; next != null; localObject3 = next) {}
        pointerIdBits |= i2;
        i = m;
        break label717;
      }
      label711:
      i = 0;
      localObject3 = localObject2;
      label717:
      boolean bool2 = false;
      if (mFirstTouchTarget == null)
      {
        bool2 = dispatchTransformedTouchEvent(paramMotionEvent, bool3, null, -1);
      }
      else
      {
        localObject1 = null;
        localObject4 = mFirstTouchTarget;
        while (localObject4 != null)
        {
          localObject2 = next;
          if ((i != 0) && (localObject4 == localObject3))
          {
            bool2 = true;
          }
          else
          {
            boolean bool4;
            if ((!resetCancelNextUpFlag(child)) && (k == 0)) {
              bool4 = false;
            } else {
              bool4 = true;
            }
            bool1 = bool2;
            if (dispatchTransformedTouchEvent(paramMotionEvent, bool4, child, pointerIdBits)) {
              bool1 = true;
            }
            bool2 = bool1;
            if (bool4)
            {
              if (localObject1 == null) {
                mFirstTouchTarget = ((TouchTarget)localObject2);
              } else {
                next = ((TouchTarget)localObject2);
              }
              ((TouchTarget)localObject4).recycle();
              localObject4 = localObject2;
              bool2 = bool1;
              continue;
            }
          }
          localObject1 = localObject4;
          localObject4 = localObject2;
        }
      }
      if ((!bool3) && (j != 1) && (j != 7))
      {
        bool1 = bool2;
        if (n != 0)
        {
          bool1 = bool2;
          if (j == 6)
          {
            removePointersFromTouchTargets(1 << paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex()));
            bool1 = bool2;
          }
        }
      }
      else
      {
        resetTouchState();
        bool1 = bool2;
      }
    }
    if ((!bool1) && (mInputEventConsistencyVerifier != null)) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramMotionEvent, 1);
    }
    return bool1;
  }
  
  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onTrackballEvent(paramMotionEvent, 1);
    }
    if ((mPrivateFlags & 0x12) == 18)
    {
      if (super.dispatchTrackballEvent(paramMotionEvent)) {
        return true;
      }
    }
    else if ((mFocused != null) && ((mFocused.mPrivateFlags & 0x10) == 16) && (mFocused.dispatchTrackballEvent(paramMotionEvent))) {
      return true;
    }
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramMotionEvent, 1);
    }
    return false;
  }
  
  View dispatchUnhandledKeyEvent(KeyEvent paramKeyEvent)
  {
    if (!hasUnhandledKeyListener()) {
      return null;
    }
    Object localObject = buildOrderedChildList();
    if (localObject != null) {
      try
      {
        for (i = ((ArrayList)localObject).size() - 1; i >= 0; i--)
        {
          View localView = ((View)((ArrayList)localObject).get(i)).dispatchUnhandledKeyEvent(paramKeyEvent);
          if (localView != null) {
            return localView;
          }
        }
      }
      finally
      {
        ((ArrayList)localObject).clear();
      }
    }
    for (int i = getChildCount() - 1; i >= 0; i--)
    {
      localObject = getChildAt(i).dispatchUnhandledKeyEvent(paramKeyEvent);
      if (localObject != null) {
        return localObject;
      }
    }
    if (onUnhandledKeyEvent(paramKeyEvent)) {
      return this;
    }
    return null;
  }
  
  public boolean dispatchUnhandledMove(View paramView, int paramInt)
  {
    boolean bool;
    if ((mFocused != null) && (mFocused.dispatchUnhandledMove(paramView, paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void dispatchViewAdded(View paramView)
  {
    onViewAdded(paramView);
    if (mOnHierarchyChangeListener != null) {
      mOnHierarchyChangeListener.onChildViewAdded(this, paramView);
    }
  }
  
  void dispatchViewRemoved(View paramView)
  {
    onViewRemoved(paramView);
    if (mOnHierarchyChangeListener != null) {
      mOnHierarchyChangeListener.onChildViewRemoved(this, paramView);
    }
  }
  
  boolean dispatchVisibilityAggregated(boolean paramBoolean)
  {
    paramBoolean = super.dispatchVisibilityAggregated(paramBoolean);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      if (arrayOfView[j].getVisibility() == 0) {
        arrayOfView[j].dispatchVisibilityAggregated(paramBoolean);
      }
    }
    return paramBoolean;
  }
  
  protected void dispatchVisibilityChanged(View paramView, int paramInt)
  {
    super.dispatchVisibilityChanged(paramView, paramInt);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchVisibilityChanged(paramView, paramInt);
    }
  }
  
  public void dispatchWindowFocusChanged(boolean paramBoolean)
  {
    super.dispatchWindowFocusChanged(paramBoolean);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchWindowFocusChanged(paramBoolean);
    }
  }
  
  public void dispatchWindowSystemUiVisiblityChanged(int paramInt)
  {
    super.dispatchWindowSystemUiVisiblityChanged(paramInt);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchWindowSystemUiVisiblityChanged(paramInt);
    }
  }
  
  public void dispatchWindowVisibilityChanged(int paramInt)
  {
    super.dispatchWindowVisibilityChanged(paramInt);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].dispatchWindowVisibilityChanged(paramInt);
    }
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    return paramView.draw(paramCanvas, this, paramLong);
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if ((mGroupFlags & 0x10000) != 0)
    {
      if ((mGroupFlags & 0x2000) == 0)
      {
        View[] arrayOfView = mChildren;
        int i = mChildrenCount;
        for (int j = 0; j < i; j++)
        {
          View localView = arrayOfView[j];
          if ((mViewFlags & 0x400000) != 0) {
            localView.refreshDrawableState();
          }
        }
      }
      throw new IllegalStateException("addStateFromChildren cannot be enabled if a child has duplicateParentState set to true");
    }
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("focus:descendantFocusability", getDescendantFocusability());
    paramViewHierarchyEncoder.addProperty("drawing:clipChildren", getClipChildren());
    paramViewHierarchyEncoder.addProperty("drawing:clipToPadding", getClipToPadding());
    paramViewHierarchyEncoder.addProperty("drawing:childrenDrawingOrderEnabled", isChildrenDrawingOrderEnabled());
    paramViewHierarchyEncoder.addProperty("drawing:persistentDrawingCache", getPersistentDrawingCache());
    int i = getChildCount();
    paramViewHierarchyEncoder.addProperty("meta:__childCount__", (short)i);
    for (int j = 0; j < i; j++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("meta:__child__");
      localStringBuilder.append(j);
      paramViewHierarchyEncoder.addPropertyKey(localStringBuilder.toString());
      getChildAt(j).encode(paramViewHierarchyEncoder);
    }
  }
  
  public void endViewTransition(View paramView)
  {
    if (mTransitioningViews != null)
    {
      mTransitioningViews.remove(paramView);
      ArrayList localArrayList = mDisappearingChildren;
      if ((localArrayList != null) && (localArrayList.contains(paramView)))
      {
        localArrayList.remove(paramView);
        if ((mVisibilityChangingChildren != null) && (mVisibilityChangingChildren.contains(paramView)))
        {
          mVisibilityChangingChildren.remove(paramView);
        }
        else
        {
          if (mAttachInfo != null) {
            paramView.dispatchDetachedFromWindow();
          }
          if (mParent != null) {
            mParent = null;
          }
        }
        invalidate();
      }
    }
  }
  
  public View findFocus()
  {
    if (isFocused()) {
      return this;
    }
    if (mFocused != null) {
      return mFocused.findFocus();
    }
    return null;
  }
  
  View findFrontmostDroppableChildAt(float paramFloat1, float paramFloat2, PointF paramPointF)
  {
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    i--;
    while (i >= 0)
    {
      View localView = arrayOfView[i];
      if ((localView.canAcceptDrag()) && (isTransformedTouchPointInView(paramFloat1, paramFloat2, localView, paramPointF))) {
        return localView;
      }
      i--;
    }
    return null;
  }
  
  public void findNamedViews(Map<String, View> paramMap)
  {
    if ((getVisibility() != 0) && (mGhostView == null)) {
      return;
    }
    super.findNamedViews(paramMap);
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).findNamedViews(paramMap);
    }
  }
  
  public View findViewByAccessibilityIdTraversal(int paramInt)
  {
    Object localObject = super.findViewByAccessibilityIdTraversal(paramInt);
    if (localObject != null) {
      return localObject;
    }
    if (getAccessibilityNodeProvider() != null) {
      return null;
    }
    int i = mChildrenCount;
    localObject = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = localObject[j].findViewByAccessibilityIdTraversal(paramInt);
      if (localView != null) {
        return localView;
      }
    }
    return null;
  }
  
  public View findViewByAutofillIdTraversal(int paramInt)
  {
    Object localObject = super.findViewByAutofillIdTraversal(paramInt);
    if (localObject != null) {
      return localObject;
    }
    int i = mChildrenCount;
    localObject = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = localObject[j].findViewByAutofillIdTraversal(paramInt);
      if (localView != null) {
        return localView;
      }
    }
    return null;
  }
  
  protected <T extends View> T findViewByPredicateTraversal(Predicate<View> paramPredicate, View paramView)
  {
    if (paramPredicate.test(this)) {
      return this;
    }
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((localView != paramView) && ((mPrivateFlags & 0x8) == 0))
      {
        localView = localView.findViewByPredicate(paramPredicate);
        if (localView != null) {
          return localView;
        }
      }
    }
    return null;
  }
  
  protected <T extends View> T findViewTraversal(int paramInt)
  {
    if (paramInt == mID) {
      return this;
    }
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((mPrivateFlags & 0x8) == 0)
      {
        localView = localView.findViewById(paramInt);
        if (localView != null) {
          return localView;
        }
      }
    }
    return null;
  }
  
  protected <T extends View> T findViewWithTagTraversal(Object paramObject)
  {
    if ((paramObject != null) && (paramObject.equals(mTag))) {
      return this;
    }
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((mPrivateFlags & 0x8) == 0)
      {
        localView = localView.findViewWithTag(paramObject);
        if (localView != null) {
          return localView;
        }
      }
    }
    return null;
  }
  
  public void findViewsWithText(ArrayList<View> paramArrayList, CharSequence paramCharSequence, int paramInt)
  {
    super.findViewsWithText(paramArrayList, paramCharSequence, paramInt);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if (((mViewFlags & 0xC) == 0) && ((mPrivateFlags & 0x8) == 0)) {
        localView.findViewsWithText(paramArrayList, paramCharSequence, paramInt);
      }
    }
  }
  
  void finishAnimatingView(View paramView, Animation paramAnimation)
  {
    ArrayList localArrayList = mDisappearingChildren;
    if ((localArrayList != null) && (localArrayList.contains(paramView)))
    {
      localArrayList.remove(paramView);
      if (mAttachInfo != null) {
        paramView.dispatchDetachedFromWindow();
      }
      paramView.clearAnimation();
      mGroupFlags |= 0x4;
    }
    if ((paramAnimation != null) && (!paramAnimation.getFillAfter())) {
      paramView.clearAnimation();
    }
    if ((mPrivateFlags & 0x10000) == 65536)
    {
      paramView.onAnimationEnd();
      mPrivateFlags &= 0xFFFEFFFF;
      mGroupFlags |= 0x4;
    }
  }
  
  public View focusSearch(View paramView, int paramInt)
  {
    if (isRootNamespace()) {
      return FocusFinder.getInstance().findNextFocus(this, paramView, paramInt);
    }
    if (mParent != null) {
      return mParent.focusSearch(paramView, paramInt);
    }
    return null;
  }
  
  public void focusableViewAvailable(View paramView)
  {
    if ((mParent != null) && (getDescendantFocusability() != 393216) && ((mViewFlags & 0xC) == 0) && ((isFocusableInTouchMode()) || (!shouldBlockFocusForTouchscreen())) && ((!isFocused()) || (getDescendantFocusability() == 262144))) {
      mParent.focusableViewAvailable(paramView);
    }
  }
  
  public boolean gatherTransparentRegion(Region paramRegion)
  {
    int i = mPrivateFlags;
    boolean bool1 = false;
    int j;
    if ((i & 0x200) == 0) {
      j = 1;
    } else {
      j = 0;
    }
    if ((j != 0) && (paramRegion == null)) {
      return true;
    }
    super.gatherTransparentRegion(paramRegion);
    int k = mChildrenCount;
    i = 1;
    boolean bool2;
    if (k > 0)
    {
      ArrayList localArrayList = buildOrderedChildList();
      if ((localArrayList == null) && (isChildrenDrawingOrderEnabled())) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      View[] arrayOfView = mChildren;
      i = 1;
      int m = 0;
      while (m < k)
      {
        View localView = getAndVerifyPreorderedView(localArrayList, arrayOfView, getAndVerifyPreorderedIndex(k, m, bool2));
        int n;
        if ((mViewFlags & 0xC) != 0)
        {
          n = i;
          if (localView.getAnimation() == null) {}
        }
        else
        {
          n = i;
          if (!localView.gatherTransparentRegion(paramRegion)) {
            n = 0;
          }
        }
        m++;
        i = n;
      }
      if (localArrayList != null) {
        localArrayList.clear();
      }
    }
    if ((j == 0) && (i == 0)) {
      bool2 = bool1;
    } else {
      bool2 = true;
    }
    return bool2;
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(LayoutParams paramLayoutParams)
  {
    return paramLayoutParams;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ViewGroup.class.getName();
  }
  
  public View getChildAt(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mChildrenCount)) {
      return mChildren[paramInt];
    }
    return null;
  }
  
  public int getChildCount()
  {
    return mChildrenCount;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    return paramInt2;
  }
  
  protected boolean getChildStaticTransformation(View paramView, Transformation paramTransformation)
  {
    return false;
  }
  
  Transformation getChildTransformation()
  {
    if (mChildTransformation == null) {
      mChildTransformation = new Transformation();
    }
    return mChildTransformation;
  }
  
  public boolean getChildVisibleRect(View paramView, Rect paramRect, Point paramPoint)
  {
    return getChildVisibleRect(paramView, paramRect, paramPoint, false);
  }
  
  public boolean getChildVisibleRect(View paramView, Rect paramRect, Point paramPoint, boolean paramBoolean)
  {
    RectF localRectF;
    if (mAttachInfo != null) {
      localRectF = mAttachInfo.mTmpTransformRect;
    } else {
      localRectF = new RectF();
    }
    localRectF.set(paramRect);
    if (!paramView.hasIdentityMatrix()) {
      paramView.getMatrix().mapRect(localRectF);
    }
    int i = mLeft - mScrollX;
    int j = mTop - mScrollY;
    localRectF.offset(i, j);
    if (paramPoint != null)
    {
      if (!paramView.hasIdentityMatrix())
      {
        float[] arrayOfFloat;
        if (mAttachInfo != null) {
          arrayOfFloat = mAttachInfo.mTmpTransformLocation;
        } else {
          arrayOfFloat = new float[2];
        }
        arrayOfFloat[0] = x;
        arrayOfFloat[1] = y;
        paramView.getMatrix().mapPoints(arrayOfFloat);
        x = Math.round(arrayOfFloat[0]);
        y = Math.round(arrayOfFloat[1]);
      }
      x += i;
      y += j;
    }
    i = mRight - mLeft;
    j = mBottom - mTop;
    boolean bool1 = true;
    boolean bool2;
    if (mParent != null)
    {
      bool2 = bool1;
      if ((mParent instanceof ViewGroup))
      {
        bool2 = bool1;
        if (!((ViewGroup)mParent).getClipChildren()) {}
      }
    }
    else
    {
      bool2 = localRectF.intersect(0.0F, 0.0F, i, j);
    }
    if (!paramBoolean)
    {
      bool1 = bool2;
      if (!bool2) {}
    }
    else
    {
      bool1 = bool2;
      if ((mGroupFlags & 0x22) == 34) {
        bool1 = localRectF.intersect(mPaddingLeft, mPaddingTop, i - mPaddingRight, j - mPaddingBottom);
      }
    }
    if (!paramBoolean)
    {
      bool2 = bool1;
      if (!bool1) {}
    }
    else
    {
      bool2 = bool1;
      if (mClipBounds != null) {
        bool2 = localRectF.intersect(mClipBounds.left, mClipBounds.top, mClipBounds.right, mClipBounds.bottom);
      }
    }
    paramRect.set((int)Math.floor(left), (int)Math.floor(top), (int)Math.ceil(right), (int)Math.ceil(bottom));
    if (!paramBoolean)
    {
      bool1 = bool2;
      if (!bool2) {}
    }
    else
    {
      bool1 = bool2;
      if (mParent != null) {
        if ((mParent instanceof ViewGroup)) {
          bool1 = ((ViewGroup)mParent).getChildVisibleRect(this, paramRect, paramPoint, paramBoolean);
        } else {
          bool1 = mParent.getChildVisibleRect(this, paramRect, paramPoint);
        }
      }
    }
    return bool1;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public boolean getClipChildren()
  {
    int i = mGroupFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public boolean getClipToPadding()
  {
    return hasBooleanFlag(2);
  }
  
  View getDeepestFocusedChild()
  {
    View localView;
    for (Object localObject = this;; localObject = localView)
    {
      localView = null;
      if (localObject == null) {
        break;
      }
      if (((View)localObject).isFocused()) {
        return localObject;
      }
      if ((localObject instanceof ViewGroup)) {
        localView = ((ViewGroup)localObject).getFocusedChild();
      }
    }
    return null;
  }
  
  @ViewDebug.ExportedProperty(category="focus", mapping={@ViewDebug.IntToString(from=131072, to="FOCUS_BEFORE_DESCENDANTS"), @ViewDebug.IntToString(from=262144, to="FOCUS_AFTER_DESCENDANTS"), @ViewDebug.IntToString(from=393216, to="FOCUS_BLOCK_DESCENDANTS")})
  public int getDescendantFocusability()
  {
    return mGroupFlags & 0x60000;
  }
  
  public View getFocusedChild()
  {
    return mFocused;
  }
  
  public LayoutAnimationController getLayoutAnimation()
  {
    return mLayoutAnimationController;
  }
  
  public Animation.AnimationListener getLayoutAnimationListener()
  {
    return mAnimationListener;
  }
  
  public int getLayoutMode()
  {
    if (mLayoutMode == -1)
    {
      int i;
      if ((mParent instanceof ViewGroup)) {
        i = ((ViewGroup)mParent).getLayoutMode();
      } else {
        i = LAYOUT_MODE_DEFAULT;
      }
      setLayoutMode(i, false);
    }
    return mLayoutMode;
  }
  
  public LayoutTransition getLayoutTransition()
  {
    return mTransition;
  }
  
  public int getNestedScrollAxes()
  {
    return mNestedScrollAxes;
  }
  
  int getNumChildrenForAccessibility()
  {
    int i = 0;
    int j = 0;
    while (j < getChildCount())
    {
      View localView = getChildAt(j);
      int k;
      if (localView.includeForAccessibility())
      {
        k = i + 1;
      }
      else
      {
        k = i;
        if ((localView instanceof ViewGroup)) {
          k = i + ((ViewGroup)localView).getNumChildrenForAccessibility();
        }
      }
      j++;
      i = k;
    }
    return i;
  }
  
  public ViewGroupOverlay getOverlay()
  {
    if (mOverlay == null) {
      mOverlay = new ViewGroupOverlay(mContext, this);
    }
    return (ViewGroupOverlay)mOverlay;
  }
  
  @ViewDebug.ExportedProperty(category="drawing", mapping={@ViewDebug.IntToString(from=0, to="NONE"), @ViewDebug.IntToString(from=1, to="ANIMATION"), @ViewDebug.IntToString(from=2, to="SCROLLING"), @ViewDebug.IntToString(from=3, to="ALL")})
  @Deprecated
  public int getPersistentDrawingCache()
  {
    return mPersistentDrawingCache;
  }
  
  void getScrollIndicatorBounds(Rect paramRect)
  {
    super.getScrollIndicatorBounds(paramRect);
    int i;
    if ((mGroupFlags & 0x22) == 34) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      left += mPaddingLeft;
      right -= mPaddingRight;
      top += mPaddingTop;
      bottom -= mPaddingBottom;
    }
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public boolean getTouchscreenBlocksFocus()
  {
    boolean bool;
    if ((mGroupFlags & 0x4000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public View getTransientView(int paramInt)
  {
    if ((mTransientViews != null) && (paramInt < mTransientViews.size())) {
      return (View)mTransientViews.get(paramInt);
    }
    return null;
  }
  
  public int getTransientViewCount()
  {
    int i;
    if (mTransientIndices == null) {
      i = 0;
    } else {
      i = mTransientIndices.size();
    }
    return i;
  }
  
  public int getTransientViewIndex(int paramInt)
  {
    if ((paramInt >= 0) && (mTransientIndices != null) && (paramInt < mTransientIndices.size())) {
      return ((Integer)mTransientIndices.get(paramInt)).intValue();
    }
    return -1;
  }
  
  void handleFocusGainInternal(int paramInt, Rect paramRect)
  {
    if (mFocused != null)
    {
      mFocused.unFocus(this);
      mFocused = null;
      mFocusedInCluster = null;
    }
    super.handleFocusGainInternal(paramInt, paramRect);
  }
  
  boolean hasDefaultFocus()
  {
    boolean bool;
    if ((mDefaultFocus == null) && (!super.hasDefaultFocus())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasFocus()
  {
    boolean bool;
    if (((mPrivateFlags & 0x2) == 0) && (mFocused == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  boolean hasFocusable(boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((mViewFlags & 0xC) != 0) {
      return false;
    }
    if (((paramBoolean1) || (getFocusable() != 16)) && (isFocusable())) {
      return true;
    }
    if (getDescendantFocusability() != 393216) {
      return hasFocusableChild(paramBoolean2);
    }
    return false;
  }
  
  boolean hasFocusableChild(boolean paramBoolean)
  {
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if (((paramBoolean) && (localView.hasExplicitFocusable())) || ((!paramBoolean) && (localView.hasFocusable()))) {
        return true;
      }
    }
    return false;
  }
  
  protected boolean hasHoveredChild()
  {
    boolean bool;
    if (mFirstHoverTarget != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasTransientState()
  {
    boolean bool;
    if ((mChildCountWithTransientState <= 0) && (!super.hasTransientState())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  boolean hasUnhandledKeyListener()
  {
    boolean bool;
    if ((mChildUnhandledKeyListeners <= 0) && (!super.hasUnhandledKeyListener())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  void incrementChildUnhandledKeyListeners()
  {
    mChildUnhandledKeyListeners += 1;
    if ((mChildUnhandledKeyListeners == 1) && ((mParent instanceof ViewGroup))) {
      ((ViewGroup)mParent).incrementChildUnhandledKeyListeners();
    }
  }
  
  public int indexOfChild(View paramView)
  {
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      if (arrayOfView[j] == paramView) {
        return j;
      }
    }
    return -1;
  }
  
  protected void internalSetPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.internalSetPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    if ((mPaddingLeft | mPaddingTop | mPaddingRight | mPaddingBottom) != 0) {
      mGroupFlags |= 0x20;
    } else {
      mGroupFlags &= 0xFFFFFFDF;
    }
  }
  
  @Deprecated
  public final void invalidateChild(View paramView, Rect paramRect)
  {
    View.AttachInfo localAttachInfo = mAttachInfo;
    if ((localAttachInfo != null) && (mHardwareAccelerated))
    {
      onDescendantInvalidated(paramView, paramView);
      return;
    }
    Object localObject1 = this;
    if (localAttachInfo != null)
    {
      int i;
      if ((mPrivateFlags & 0x40) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      Object localObject2 = paramView.getMatrix();
      int j;
      if ((paramView.isOpaque()) && (i == 0) && (paramView.getAnimation() == null) && (((Matrix)localObject2).isIdentity())) {
        j = 1;
      } else {
        j = 0;
      }
      int k;
      if (j != 0) {
        k = 4194304;
      } else {
        k = 2097152;
      }
      if (mLayerType != 0)
      {
        mPrivateFlags |= 0x80000000;
        mPrivateFlags &= 0xFFFF7FFF;
      }
      int[] arrayOfInt = mInvalidateChildLocation;
      arrayOfInt[0] = mLeft;
      arrayOfInt[1] = mTop;
      if ((((Matrix)localObject2).isIdentity()) && ((mGroupFlags & 0x800) == 0)) {
        break label326;
      }
      RectF localRectF = mTmpTransformRect;
      localRectF.set(paramRect);
      if ((mGroupFlags & 0x800) != 0)
      {
        Transformation localTransformation = mTmpTransformation;
        if (getChildStaticTransformation(paramView, localTransformation))
        {
          Matrix localMatrix = mTmpMatrix;
          localMatrix.set(localTransformation.getMatrix());
          paramView = localMatrix;
          if (!((Matrix)localObject2).isIdentity())
          {
            localMatrix.preConcat((Matrix)localObject2);
            paramView = localMatrix;
          }
        }
        else
        {
          paramView = (View)localObject2;
        }
        localObject2 = paramView;
      }
      ((Matrix)localObject2).mapRect(localRectF);
      paramRect.set((int)Math.floor(left), (int)Math.floor(top), (int)Math.ceil(right), (int)Math.ceil(bottom));
      label326:
      do
      {
        paramView = null;
        if ((localObject1 instanceof View)) {
          paramView = (View)localObject1;
        }
        if (i != 0) {
          if (paramView != null) {
            mPrivateFlags |= 0x40;
          } else if ((localObject1 instanceof ViewRootImpl)) {
            mIsAnimating = true;
          }
        }
        if (paramView != null)
        {
          j = k;
          if ((mViewFlags & 0x3000) != 0)
          {
            j = k;
            if (paramView.getSolidColor() == 0) {
              j = 2097152;
            }
          }
          k = j;
          if ((mPrivateFlags & 0x600000) != 2097152)
          {
            mPrivateFlags = (mPrivateFlags & 0xFF9FFFFF | j);
            k = j;
          }
        }
        localObject2 = ((ViewParent)localObject1).invalidateChildInParent(arrayOfInt, paramRect);
        if (paramView != null)
        {
          paramView = paramView.getMatrix();
          if (!paramView.isIdentity())
          {
            localObject1 = mTmpTransformRect;
            ((RectF)localObject1).set(paramRect);
            paramView.mapRect((RectF)localObject1);
            paramRect.set((int)Math.floor(left), (int)Math.floor(top), (int)Math.ceil(right), (int)Math.ceil(bottom));
          }
        }
        localObject1 = localObject2;
      } while (localObject2 != null);
    }
  }
  
  @Deprecated
  public ViewParent invalidateChildInParent(int[] paramArrayOfInt, Rect paramRect)
  {
    if ((mPrivateFlags & 0x8020) != 0)
    {
      if ((mGroupFlags & 0x90) != 128)
      {
        paramRect.offset(paramArrayOfInt[0] - mScrollX, paramArrayOfInt[1] - mScrollY);
        if ((mGroupFlags & 0x1) == 0) {
          paramRect.union(0, 0, mRight - mLeft, mBottom - mTop);
        }
        int i = mLeft;
        int j = mTop;
        if (((mGroupFlags & 0x1) == 1) && (!paramRect.intersect(0, 0, mRight - i, mBottom - j))) {
          paramRect.setEmpty();
        }
        paramArrayOfInt[0] = i;
        paramArrayOfInt[1] = j;
      }
      else
      {
        if ((mGroupFlags & 0x1) == 1) {
          paramRect.set(0, 0, mRight - mLeft, mBottom - mTop);
        } else {
          paramRect.union(0, 0, mRight - mLeft, mBottom - mTop);
        }
        paramArrayOfInt[0] = mLeft;
        paramArrayOfInt[1] = mTop;
        mPrivateFlags &= 0xFFFFFFDF;
      }
      mPrivateFlags &= 0xFFFF7FFF;
      if (mLayerType != 0) {
        mPrivateFlags |= 0x80000000;
      }
      return mParent;
    }
    return null;
  }
  
  void invalidateInheritedLayoutMode(int paramInt)
  {
    if ((mLayoutMode != -1) && (mLayoutMode != paramInt) && (!hasBooleanFlag(8388608)))
    {
      setLayoutMode(-1, false);
      int i = 0;
      int j = getChildCount();
      while (i < j)
      {
        getChildAt(i).invalidateInheritedLayoutMode(paramInt);
        i++;
      }
      return;
    }
  }
  
  @Deprecated
  public boolean isAlwaysDrawnWithCacheEnabled()
  {
    boolean bool;
    if ((mGroupFlags & 0x4000) == 16384) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isAnimationCacheEnabled()
  {
    boolean bool;
    if ((mGroupFlags & 0x40) == 64) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  protected boolean isChildrenDrawingOrderEnabled()
  {
    boolean bool;
    if ((mGroupFlags & 0x400) == 1024) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  protected boolean isChildrenDrawnWithCacheEnabled()
  {
    boolean bool;
    if ((mGroupFlags & 0x8000) == 32768) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isLayoutModeOptical()
  {
    int i = mLayoutMode;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLayoutSuppressed()
  {
    return mSuppressLayout;
  }
  
  public boolean isMotionEventSplittingEnabled()
  {
    boolean bool;
    if ((mGroupFlags & 0x200000) == 2097152) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isShowingContextMenuWithCoords()
  {
    boolean bool;
    if ((mGroupFlags & 0x20000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean isTransformedTouchPointInView(float paramFloat1, float paramFloat2, View paramView, PointF paramPointF)
  {
    float[] arrayOfFloat = getTempPoint();
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    transformPointToViewLocal(arrayOfFloat, paramView);
    boolean bool = paramView.pointInView(arrayOfFloat[0], arrayOfFloat[1]);
    if ((bool) && (paramPointF != null)) {
      paramPointF.set(arrayOfFloat[0], arrayOfFloat[1]);
    }
    return bool;
  }
  
  public boolean isTransitionGroup()
  {
    int i = mGroupFlags;
    boolean bool1 = false;
    boolean bool2 = false;
    if ((i & 0x2000000) != 0)
    {
      if ((mGroupFlags & 0x1000000) != 0) {
        bool2 = true;
      }
      return bool2;
    }
    ViewOutlineProvider localViewOutlineProvider = getOutlineProvider();
    if ((getBackground() == null) && (getTransitionName() == null) && ((localViewOutlineProvider == null) || (localViewOutlineProvider == ViewOutlineProvider.BACKGROUND))) {
      bool2 = bool1;
    } else {
      bool2 = true;
    }
    return bool2;
  }
  
  boolean isViewTransitioning(View paramView)
  {
    boolean bool;
    if ((mTransitioningViews != null) && (mTransitioningViews.contains(paramView))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].jumpDrawablesToCurrentState();
    }
  }
  
  public final void layout(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((!mSuppressLayout) && ((mTransition == null) || (!mTransition.isChangingLayout())))
    {
      if (mTransition != null) {
        mTransition.layoutChange(this);
      }
      super.layout(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    else
    {
      mLayoutCalledWhileSuppressed = true;
    }
  }
  
  public void makeOptionalFitsSystemWindows()
  {
    super.makeOptionalFitsSystemWindows();
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].makeOptionalFitsSystemWindows();
    }
  }
  
  protected void measureChild(View paramView, int paramInt1, int paramInt2)
  {
    LayoutParams localLayoutParams = paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, mPaddingLeft + mPaddingRight, width), getChildMeasureSpec(paramInt2, mPaddingTop + mPaddingBottom, height));
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    MarginLayoutParams localMarginLayoutParams = (MarginLayoutParams)paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, mPaddingLeft + mPaddingRight + leftMargin + rightMargin + paramInt2, width), getChildMeasureSpec(paramInt3, mPaddingTop + mPaddingBottom + topMargin + bottomMargin + paramInt4, height));
  }
  
  protected void measureChildren(int paramInt1, int paramInt2)
  {
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++)
    {
      View localView = arrayOfView[j];
      if ((mViewFlags & 0xC) != 8) {
        measureChild(localView, paramInt1, paramInt2);
      }
    }
  }
  
  boolean notifyChildOfDragStart(View paramView)
  {
    float f1 = mCurrentDragStartEvent.mX;
    float f2 = mCurrentDragStartEvent.mY;
    float[] arrayOfFloat = getTempPoint();
    arrayOfFloat[0] = f1;
    arrayOfFloat[1] = f2;
    transformPointToViewLocal(arrayOfFloat, paramView);
    mCurrentDragStartEvent.mX = arrayOfFloat[0];
    mCurrentDragStartEvent.mY = arrayOfFloat[1];
    boolean bool = paramView.dispatchDragEvent(mCurrentDragStartEvent);
    mCurrentDragStartEvent.mX = f1;
    mCurrentDragStartEvent.mY = f2;
    mCurrentDragStartEvent.mEventHandlerWasCalled = false;
    if (bool)
    {
      mChildrenInterestedInDrag.add(paramView);
      if (!paramView.canAcceptDrag())
      {
        mPrivateFlags2 |= 0x1;
        paramView.refreshDrawableState();
      }
    }
    return bool;
  }
  
  public void notifySubtreeAccessibilityStateChanged(View paramView1, View paramView2, int paramInt)
  {
    if (getAccessibilityLiveRegion() != 0) {
      notifyViewAccessibilityStateChangedIfNeeded(1);
    } else if (mParent != null) {
      try
      {
        mParent.notifySubtreeAccessibilityStateChanged(this, paramView2, paramInt);
      }
      catch (AbstractMethodError paramView1)
      {
        paramView2 = new StringBuilder();
        paramView2.append(mParent.getClass().getSimpleName());
        paramView2.append(" does not fully implement ViewParent");
        Log.e("View", paramView2.toString(), paramView1);
      }
    }
  }
  
  public void notifySubtreeAccessibilityStateChangedIfNeeded()
  {
    if ((AccessibilityManager.getInstance(mContext).isEnabled()) && (mAttachInfo != null))
    {
      if ((getImportantForAccessibility() != 4) && (!isImportantForAccessibility()) && (getChildCount() > 0))
      {
        ViewParent localViewParent = getParentForAccessibility();
        if ((localViewParent instanceof View))
        {
          ((View)localViewParent).notifySubtreeAccessibilityStateChangedIfNeeded();
          return;
        }
      }
      super.notifySubtreeAccessibilityStateChangedIfNeeded();
      return;
    }
  }
  
  public void offsetChildrenTopAndBottom(int paramInt)
  {
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      View localView = arrayOfView[k];
      mTop += paramInt;
      mBottom += paramInt;
      if (mRenderNode != null)
      {
        j = 1;
        mRenderNode.offsetTopAndBottom(paramInt);
      }
    }
    if (j != 0) {
      invalidateViewProperty(false, false);
    }
    notifySubtreeAccessibilityStateChangedIfNeeded();
  }
  
  public final void offsetDescendantRectToMyCoords(View paramView, Rect paramRect)
  {
    offsetRectBetweenParentAndChild(paramView, paramRect, true, false);
  }
  
  void offsetRectBetweenParentAndChild(View paramView, Rect paramRect, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramView == this) {
      return;
    }
    Object localObject = mParent;
    View localView = paramView;
    for (paramView = (View)localObject; (paramView != null) && ((paramView instanceof View)) && (paramView != this); paramView = mParent)
    {
      if (paramBoolean1)
      {
        paramRect.offset(mLeft - mScrollX, mTop - mScrollY);
        if (paramBoolean2)
        {
          localView = (View)paramView;
          if (!paramRect.intersect(0, 0, mRight - mLeft, mBottom - mTop)) {
            paramRect.setEmpty();
          }
        }
      }
      else
      {
        if (paramBoolean2)
        {
          localObject = (View)paramView;
          if (!paramRect.intersect(0, 0, mRight - mLeft, mBottom - mTop)) {
            paramRect.setEmpty();
          }
        }
        paramRect.offset(mScrollX - mLeft, mScrollY - mTop);
      }
      localView = (View)paramView;
    }
    if (paramView == this)
    {
      if (paramBoolean1) {
        paramRect.offset(mLeft - mScrollX, mTop - mScrollY);
      } else {
        paramRect.offset(mScrollX - mLeft, mScrollY - mTop);
      }
      return;
    }
    throw new IllegalArgumentException("parameter must be a descendant of this view");
  }
  
  public final void offsetRectIntoDescendantCoords(View paramView, Rect paramRect)
  {
    offsetRectBetweenParentAndChild(paramView, paramRect, false, false);
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    clearCachedLayoutMode();
  }
  
  protected void onChildVisibilityChanged(View paramView, int paramInt1, int paramInt2)
  {
    if (mTransition != null) {
      if (paramInt2 == 0)
      {
        mTransition.showChild(this, paramView, paramInt1);
      }
      else
      {
        mTransition.hideChild(this, paramView, paramInt2);
        if ((mTransitioningViews != null) && (mTransitioningViews.contains(paramView)))
        {
          if (mVisibilityChangingChildren == null) {
            mVisibilityChangingChildren = new ArrayList();
          }
          mVisibilityChangingChildren.add(paramView);
          addDisappearingView(paramView);
        }
      }
    }
    if ((paramInt2 == 0) && (mCurrentDragStartEvent != null) && (!mChildrenInterestedInDrag.contains(paramView))) {
      notifyChildOfDragStart(paramView);
    }
  }
  
  protected int[] onCreateDrawableState(int paramInt)
  {
    if ((mGroupFlags & 0x2000) == 0) {
      return super.onCreateDrawableState(paramInt);
    }
    int i = getChildCount();
    int j = 0;
    int k = 0;
    int m = 0;
    while (m < i)
    {
      localObject1 = getChildAt(m).getDrawableState();
      int n = k;
      if (localObject1 != null) {
        n = k + localObject1.length;
      }
      m++;
      k = n;
    }
    Object localObject1 = super.onCreateDrawableState(paramInt + k);
    paramInt = j;
    while (paramInt < i)
    {
      int[] arrayOfInt = getChildAt(paramInt).getDrawableState();
      Object localObject2 = localObject1;
      if (arrayOfInt != null) {
        localObject2 = mergeDrawableStates((int[])localObject1, arrayOfInt);
      }
      paramInt++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  protected void onDebugDraw(Canvas paramCanvas)
  {
    Paint localPaint = getDebugPaint();
    localPaint.setColor(-65536);
    localPaint.setStyle(Paint.Style.STROKE);
    int i = 0;
    Object localObject;
    for (int j = 0; j < getChildCount(); j++)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() != 8)
      {
        localObject = localView.getOpticalInsets();
        int k = localView.getLeft();
        m = left;
        n = localView.getTop();
        drawRect(paramCanvas, localPaint, m + k, top + n, localView.getRight() - right - 1, localView.getBottom() - bottom - 1);
      }
    }
    localPaint.setColor(Color.argb(63, 255, 0, 255));
    localPaint.setStyle(Paint.Style.FILL);
    onDebugDrawMargins(paramCanvas, localPaint);
    localPaint.setColor(DEBUG_CORNERS_COLOR);
    localPaint.setStyle(Paint.Style.FILL);
    int n = dipsToPixels(8);
    int m = dipsToPixels(1);
    for (j = i; j < getChildCount(); j++)
    {
      localObject = getChildAt(j);
      if (((View)localObject).getVisibility() != 8) {
        drawRectCorners(paramCanvas, ((View)localObject).getLeft(), ((View)localObject).getTop(), ((View)localObject).getRight(), ((View)localObject).getBottom(), localPaint, n, m);
      }
    }
  }
  
  protected void onDebugDrawMargins(Canvas paramCanvas, Paint paramPaint)
  {
    for (int i = 0; i < getChildCount(); i++)
    {
      View localView = getChildAt(i);
      localView.getLayoutParams().onDebugDraw(localView, paramCanvas, paramPaint);
    }
  }
  
  public void onDescendantInvalidated(View paramView1, View paramView2)
  {
    mPrivateFlags |= mPrivateFlags & 0x40;
    if ((mPrivateFlags & 0xFF9FFFFF) != 0)
    {
      mPrivateFlags = (mPrivateFlags & 0xFF9FFFFF | 0x200000);
      mPrivateFlags &= 0xFFFF7FFF;
    }
    paramView1 = paramView2;
    if (mLayerType == 1)
    {
      mPrivateFlags |= 0x80200000;
      paramView1 = this;
    }
    if (mParent != null) {
      mParent.onDescendantInvalidated(this, paramView1);
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    clearCachedLayoutMode();
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (getAccessibilityNodeProvider() != null) {
      return;
    }
    if (mAttachInfo != null)
    {
      ArrayList localArrayList = mAttachInfo.mTempArrayList;
      localArrayList.clear();
      addChildrenForAccessibility(localArrayList);
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        paramAccessibilityNodeInfo.addChildUnchecked((View)localArrayList.get(j));
      }
      localArrayList.clear();
    }
  }
  
  public boolean onInterceptHoverEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.isFromSource(8194))
    {
      int i = paramMotionEvent.getAction();
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      if (((i == 7) || (i == 9)) && (isOnScrollbar(f1, f2))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return (paramMotionEvent.isFromSource(8194)) && (paramMotionEvent.getAction() == 0) && (paramMotionEvent.isButtonPressed(1)) && (isOnScrollbarThumb(paramMotionEvent.getX(), paramMotionEvent.getY()));
  }
  
  protected abstract void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    return dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2)
  {
    return dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public boolean onNestedPrePerformAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
  {
    return false;
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfInt, null);
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, null);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt)
  {
    mNestedScrollAxes = paramInt;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int i = mChildrenCount;
    int j;
    int k;
    if ((paramInt & 0x2) != 0)
    {
      j = 0;
      k = 1;
    }
    else
    {
      j = i - 1;
      k = -1;
      i = -1;
    }
    View[] arrayOfView = mChildren;
    while (j != i)
    {
      View localView = arrayOfView[j];
      if (((mViewFlags & 0xC) == 0) && (localView.requestFocus(paramInt, paramRect))) {
        return true;
      }
      j += k;
    }
    return false;
  }
  
  public boolean onRequestSendAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    if (mAccessibilityDelegate != null) {
      return mAccessibilityDelegate.onRequestSendAccessibilityEvent(this, paramView, paramAccessibilityEvent);
    }
    return onRequestSendAccessibilityEventInternal(paramView, paramAccessibilityEvent);
  }
  
  public boolean onRequestSendAccessibilityEventInternal(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    return true;
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    float f1 = paramMotionEvent.getX(paramInt);
    float f2 = paramMotionEvent.getY(paramInt);
    if ((!isOnScrollbarThumb(f1, f2)) && (!isDraggingScrollBar()))
    {
      int i = mChildrenCount;
      if (i != 0)
      {
        ArrayList localArrayList = buildOrderedChildList();
        boolean bool;
        if ((localArrayList == null) && (isChildrenDrawingOrderEnabled())) {
          bool = true;
        } else {
          bool = false;
        }
        View[] arrayOfView = mChildren;
        for (int j = i - 1; j >= 0; j--)
        {
          Object localObject = getAndVerifyPreorderedView(localArrayList, arrayOfView, getAndVerifyPreorderedIndex(i, j, bool));
          if ((canViewReceivePointerEvents((View)localObject)) && (isTransformedTouchPointInView(f1, f2, (View)localObject, null)))
          {
            localObject = dispatchResolvePointerIcon(paramMotionEvent, paramInt, (View)localObject);
            if (localObject != null)
            {
              if (localArrayList != null) {
                localArrayList.clear();
              }
              return localObject;
            }
          }
        }
        if (localArrayList != null) {
          localArrayList.clear();
        }
      }
      return super.onResolvePointerIcon(paramMotionEvent, paramInt);
    }
    return PointerIcon.getSystemIcon(mContext, 1000);
  }
  
  protected void onSetLayoutParams(View paramView, LayoutParams paramLayoutParams)
  {
    requestLayout();
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt)
  {
    return false;
  }
  
  public void onStopNestedScroll(View paramView)
  {
    stopNestedScroll();
    mNestedScrollAxes = 0;
  }
  
  public void onViewAdded(View paramView) {}
  
  public void onViewRemoved(View paramView) {}
  
  public void recomputeViewAttributes(View paramView)
  {
    if ((mAttachInfo != null) && (!mAttachInfo.mRecomputeGlobalAttributes))
    {
      paramView = mParent;
      if (paramView != null) {
        paramView.recomputeViewAttributes(this);
      }
    }
  }
  
  public void removeAllViews()
  {
    removeAllViewsInLayout();
    requestLayout();
    invalidate(true);
  }
  
  public void removeAllViewsInLayout()
  {
    int i = mChildrenCount;
    if (i <= 0) {
      return;
    }
    View[] arrayOfView = mChildren;
    mChildrenCount = 0;
    View localView1 = mFocused;
    int j;
    if (mAttachInfo != null) {
      j = 1;
    } else {
      j = 0;
    }
    int k = 0;
    needGlobalAttributesUpdate(false);
    i--;
    while (i >= 0)
    {
      View localView2 = arrayOfView[i];
      if (mTransition != null) {
        mTransition.removeChild(this, localView2);
      }
      if (localView2 == localView1)
      {
        localView2.unFocus(null);
        k = 1;
      }
      localView2.clearAccessibilityFocus();
      cancelTouchTarget(localView2);
      cancelHoverTarget(localView2);
      if ((localView2.getAnimation() == null) && ((mTransitioningViews == null) || (!mTransitioningViews.contains(localView2))))
      {
        if (j != 0) {
          localView2.dispatchDetachedFromWindow();
        }
      }
      else {
        addDisappearingView(localView2);
      }
      if (localView2.hasTransientState()) {
        childHasTransientStateChanged(localView2, false);
      }
      dispatchViewRemoved(localView2);
      mParent = null;
      arrayOfView[i] = null;
      i--;
    }
    if (mDefaultFocus != null) {
      clearDefaultFocus(mDefaultFocus);
    }
    if (mFocusedInCluster != null) {
      clearFocusedInCluster(mFocusedInCluster);
    }
    if (k != 0)
    {
      clearChildFocus(localView1);
      if (!rootViewRequestFocus()) {
        notifyGlobalFocusCleared(localView1);
      }
    }
  }
  
  protected void removeDetachedView(View paramView, boolean paramBoolean)
  {
    if (mTransition != null) {
      mTransition.removeChild(this, paramView);
    }
    if (paramView == mFocused) {
      paramView.clearFocus();
    }
    if (paramView == mDefaultFocus) {
      clearDefaultFocus(paramView);
    }
    if (paramView == mFocusedInCluster) {
      clearFocusedInCluster(paramView);
    }
    paramView.clearAccessibilityFocus();
    cancelTouchTarget(paramView);
    cancelHoverTarget(paramView);
    if (((paramBoolean) && (paramView.getAnimation() != null)) || ((mTransitioningViews != null) && (mTransitioningViews.contains(paramView)))) {
      addDisappearingView(paramView);
    } else if (mAttachInfo != null) {
      paramView.dispatchDetachedFromWindow();
    }
    if (paramView.hasTransientState()) {
      childHasTransientStateChanged(paramView, false);
    }
    dispatchViewRemoved(paramView);
  }
  
  public void removeTransientView(View paramView)
  {
    if (mTransientViews == null) {
      return;
    }
    int i = mTransientViews.size();
    for (int j = 0; j < i; j++) {
      if (paramView == mTransientViews.get(j))
      {
        mTransientViews.remove(j);
        mTransientIndices.remove(j);
        mParent = null;
        paramView.dispatchDetachedFromWindow();
        invalidate(true);
        return;
      }
    }
  }
  
  public void removeView(View paramView)
  {
    if (removeViewInternal(paramView))
    {
      requestLayout();
      invalidate(true);
    }
  }
  
  public void removeViewAt(int paramInt)
  {
    removeViewInternal(paramInt, getChildAt(paramInt));
    requestLayout();
    invalidate(true);
  }
  
  public void removeViewInLayout(View paramView)
  {
    removeViewInternal(paramView);
  }
  
  public void removeViews(int paramInt1, int paramInt2)
  {
    removeViewsInternal(paramInt1, paramInt2);
    requestLayout();
    invalidate(true);
  }
  
  public void removeViewsInLayout(int paramInt1, int paramInt2)
  {
    removeViewsInternal(paramInt1, paramInt2);
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    if (getDescendantFocusability() == 393216) {
      return;
    }
    super.unFocus(paramView2);
    if (mFocused != paramView1)
    {
      if (mFocused != null) {
        mFocused.unFocus(paramView2);
      }
      mFocused = paramView1;
    }
    if (mParent != null) {
      mParent.requestChildFocus(this, paramView2);
    }
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    return false;
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    boolean bool;
    if ((mGroupFlags & 0x80000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (paramBoolean == bool) {
      return;
    }
    if (paramBoolean) {
      mGroupFlags |= 0x80000;
    } else {
      mGroupFlags &= 0xFFF7FFFF;
    }
    if (mParent != null) {
      mParent.requestDisallowInterceptTouchEvent(paramBoolean);
    }
  }
  
  public boolean requestFocus(int paramInt, Rect paramRect)
  {
    int i = getDescendantFocusability();
    boolean bool;
    if (i != 131072)
    {
      if (i != 262144)
      {
        if (i == 393216)
        {
          bool = super.requestFocus(paramInt, paramRect);
        }
        else
        {
          paramRect = new StringBuilder();
          paramRect.append("descendant focusability must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS but is ");
          paramRect.append(i);
          throw new IllegalStateException(paramRect.toString());
        }
      }
      else
      {
        bool = onRequestFocusInDescendants(paramInt, paramRect);
        if (!bool) {
          bool = super.requestFocus(paramInt, paramRect);
        }
      }
    }
    else
    {
      bool = super.requestFocus(paramInt, paramRect);
      if (!bool) {
        bool = onRequestFocusInDescendants(paramInt, paramRect);
      }
    }
    if ((bool) && (!isLayoutValid()) && ((mPrivateFlags & 0x1) == 0)) {
      mPrivateFlags |= 0x1;
    }
    return bool;
  }
  
  public boolean requestSendAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    ViewParent localViewParent = mParent;
    if (localViewParent == null) {
      return false;
    }
    if (!onRequestSendAccessibilityEvent(paramView, paramAccessibilityEvent)) {
      return false;
    }
    return localViewParent.requestSendAccessibilityEvent(this, paramAccessibilityEvent);
  }
  
  public void requestTransitionStart(LayoutTransition paramLayoutTransition)
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    if (localViewRootImpl != null) {
      localViewRootImpl.requestTransitionStart(paramLayoutTransition);
    }
  }
  
  public void requestTransparentRegion(View paramView)
  {
    if (paramView != null)
    {
      mPrivateFlags |= 0x200;
      if (mParent != null) {
        mParent.requestTransparentRegion(this);
      }
    }
  }
  
  protected void resetResolvedDrawables()
  {
    super.resetResolvedDrawables();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.isLayoutDirectionInherited()) {
        localView.resetResolvedDrawables();
      }
    }
  }
  
  public void resetResolvedLayoutDirection()
  {
    super.resetResolvedLayoutDirection();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.isLayoutDirectionInherited()) {
        localView.resetResolvedLayoutDirection();
      }
    }
  }
  
  public void resetResolvedPadding()
  {
    super.resetResolvedPadding();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.isLayoutDirectionInherited()) {
        localView.resetResolvedPadding();
      }
    }
  }
  
  public void resetResolvedTextAlignment()
  {
    super.resetResolvedTextAlignment();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.isTextAlignmentInherited()) {
        localView.resetResolvedTextAlignment();
      }
    }
  }
  
  public void resetResolvedTextDirection()
  {
    super.resetResolvedTextDirection();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (localView.isTextDirectionInherited()) {
        localView.resetResolvedTextDirection();
      }
    }
  }
  
  void resetSubtreeAccessibilityStateChanged()
  {
    super.resetSubtreeAccessibilityStateChanged();
    View[] arrayOfView = mChildren;
    int i = mChildrenCount;
    for (int j = 0; j < i; j++) {
      arrayOfView[j].resetSubtreeAccessibilityStateChanged();
    }
  }
  
  protected void resolveDrawables()
  {
    super.resolveDrawables();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if ((localView.isLayoutDirectionInherited()) && (!localView.areDrawablesResolved())) {
        localView.resolveDrawables();
      }
    }
  }
  
  public boolean resolveLayoutDirection()
  {
    boolean bool = super.resolveLayoutDirection();
    if (bool)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        if (localView.isLayoutDirectionInherited()) {
          localView.resolveLayoutDirection();
        }
      }
    }
    return bool;
  }
  
  public void resolveLayoutParams()
  {
    super.resolveLayoutParams();
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      getChildAt(j).resolveLayoutParams();
    }
  }
  
  public void resolvePadding()
  {
    super.resolvePadding();
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if ((localView.isLayoutDirectionInherited()) && (!localView.isPaddingResolved())) {
        localView.resolvePadding();
      }
    }
  }
  
  public boolean resolveRtlPropertiesIfNeeded()
  {
    boolean bool = super.resolveRtlPropertiesIfNeeded();
    if (bool)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        if (localView.isLayoutDirectionInherited()) {
          localView.resolveRtlPropertiesIfNeeded();
        }
      }
    }
    return bool;
  }
  
  public boolean resolveTextAlignment()
  {
    boolean bool = super.resolveTextAlignment();
    if (bool)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        if (localView.isTextAlignmentInherited()) {
          localView.resolveTextAlignment();
        }
      }
    }
    return bool;
  }
  
  public boolean resolveTextDirection()
  {
    boolean bool = super.resolveTextDirection();
    if (bool)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        if (localView.isTextDirectionInherited()) {
          localView.resolveTextDirection();
        }
      }
    }
    return bool;
  }
  
  public boolean restoreDefaultFocus()
  {
    if ((mDefaultFocus != null) && (getDescendantFocusability() != 393216) && ((mDefaultFocus.mViewFlags & 0xC) == 0) && (mDefaultFocus.restoreDefaultFocus())) {
      return true;
    }
    return super.restoreDefaultFocus();
  }
  
  public boolean restoreFocusInCluster(int paramInt)
  {
    if (isKeyboardNavigationCluster())
    {
      boolean bool1 = getTouchscreenBlocksFocus();
      try
      {
        setTouchscreenBlocksFocusNoRefocus(false);
        boolean bool2 = restoreFocusInClusterInternal(paramInt);
        return bool2;
      }
      finally
      {
        setTouchscreenBlocksFocusNoRefocus(bool1);
      }
    }
    return restoreFocusInClusterInternal(paramInt);
  }
  
  public boolean restoreFocusNotInCluster()
  {
    if (mFocusedInCluster != null) {
      return restoreFocusInCluster(130);
    }
    if ((!isKeyboardNavigationCluster()) && ((mViewFlags & 0xC) == 0))
    {
      int i = getDescendantFocusability();
      if (i == 393216) {
        return super.requestFocus(130, null);
      }
      if ((i == 131072) && (super.requestFocus(130, null))) {
        return true;
      }
      for (int j = 0; j < mChildrenCount; j++)
      {
        View localView = mChildren[j];
        if ((!localView.isKeyboardNavigationCluster()) && (localView.restoreFocusNotInCluster())) {
          return true;
        }
      }
      if ((i == 262144) && (!hasFocusableChild(false))) {
        return super.requestFocus(130, null);
      }
      return false;
    }
    return false;
  }
  
  public void scheduleLayoutAnimation()
  {
    mGroupFlags |= 0x8;
  }
  
  public void setAddStatesFromChildren(boolean paramBoolean)
  {
    if (paramBoolean) {
      mGroupFlags |= 0x2000;
    } else {
      mGroupFlags &= 0xDFFF;
    }
    refreshDrawableState();
  }
  
  @Deprecated
  public void setAlwaysDrawnWithCacheEnabled(boolean paramBoolean)
  {
    setBooleanFlag(16384, paramBoolean);
  }
  
  @Deprecated
  public void setAnimationCacheEnabled(boolean paramBoolean)
  {
    setBooleanFlag(64, paramBoolean);
  }
  
  @Deprecated
  protected void setChildrenDrawingCacheEnabled(boolean paramBoolean)
  {
    if ((paramBoolean) || ((mPersistentDrawingCache & 0x3) != 3))
    {
      View[] arrayOfView = mChildren;
      int i = mChildrenCount;
      for (int j = 0; j < i; j++) {
        arrayOfView[j].setDrawingCacheEnabled(paramBoolean);
      }
    }
  }
  
  protected void setChildrenDrawingOrderEnabled(boolean paramBoolean)
  {
    setBooleanFlag(1024, paramBoolean);
  }
  
  @Deprecated
  protected void setChildrenDrawnWithCacheEnabled(boolean paramBoolean)
  {
    setBooleanFlag(32768, paramBoolean);
  }
  
  public void setClipChildren(boolean paramBoolean)
  {
    int i = mGroupFlags;
    int j = 0;
    boolean bool;
    if ((i & 0x1) == 1) {
      bool = true;
    } else {
      bool = false;
    }
    if (paramBoolean != bool)
    {
      setBooleanFlag(1, paramBoolean);
      while (j < mChildrenCount)
      {
        View localView = getChildAt(j);
        if (mRenderNode != null) {
          mRenderNode.setClipToBounds(paramBoolean);
        }
        j++;
      }
      invalidate(true);
    }
  }
  
  public void setClipToPadding(boolean paramBoolean)
  {
    if (hasBooleanFlag(2) != paramBoolean)
    {
      setBooleanFlag(2, paramBoolean);
      invalidate(true);
    }
  }
  
  void setDefaultFocus(View paramView)
  {
    if ((mDefaultFocus != null) && (mDefaultFocus.isFocusedByDefault())) {
      return;
    }
    mDefaultFocus = paramView;
    if ((mParent instanceof ViewGroup)) {
      ((ViewGroup)mParent).setDefaultFocus(this);
    }
  }
  
  public void setDescendantFocusability(int paramInt)
  {
    if ((paramInt != 131072) && (paramInt != 262144) && (paramInt != 393216)) {
      throw new IllegalArgumentException("must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS");
    }
    mGroupFlags &= 0xFFF9FFFF;
    mGroupFlags |= 0x60000 & paramInt;
  }
  
  public void setLayoutAnimation(LayoutAnimationController paramLayoutAnimationController)
  {
    mLayoutAnimationController = paramLayoutAnimationController;
    if (mLayoutAnimationController != null) {
      mGroupFlags |= 0x8;
    }
  }
  
  public void setLayoutAnimationListener(Animation.AnimationListener paramAnimationListener)
  {
    mAnimationListener = paramAnimationListener;
  }
  
  public void setLayoutMode(int paramInt)
  {
    if (mLayoutMode != paramInt)
    {
      invalidateInheritedLayoutMode(paramInt);
      boolean bool;
      if (paramInt != -1) {
        bool = true;
      } else {
        bool = false;
      }
      setLayoutMode(paramInt, bool);
      requestLayout();
    }
  }
  
  public void setLayoutTransition(LayoutTransition paramLayoutTransition)
  {
    if (mTransition != null)
    {
      LayoutTransition localLayoutTransition = mTransition;
      localLayoutTransition.cancel();
      localLayoutTransition.removeTransitionListener(mLayoutTransitionListener);
    }
    mTransition = paramLayoutTransition;
    if (mTransition != null) {
      mTransition.addTransitionListener(mLayoutTransitionListener);
    }
  }
  
  public void setMotionEventSplittingEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {
      mGroupFlags |= 0x200000;
    } else {
      mGroupFlags &= 0xFFDFFFFF;
    }
  }
  
  public void setOnHierarchyChangeListener(OnHierarchyChangeListener paramOnHierarchyChangeListener)
  {
    mOnHierarchyChangeListener = paramOnHierarchyChangeListener;
  }
  
  @Deprecated
  public void setPersistentDrawingCache(int paramInt)
  {
    mPersistentDrawingCache = (paramInt & 0x3);
  }
  
  protected void setStaticTransformationsEnabled(boolean paramBoolean)
  {
    setBooleanFlag(2048, paramBoolean);
  }
  
  public void setTouchscreenBlocksFocus(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      mGroupFlags |= 0x4000000;
      if ((hasFocus()) && (!isKeyboardNavigationCluster())) {
        if (!getDeepestFocusedChild().isFocusableInTouchMode())
        {
          View localView = focusSearch(2);
          if (localView != null) {
            localView.requestFocus();
          }
        }
      }
    }
    else
    {
      mGroupFlags &= 0xFBFFFFFF;
    }
  }
  
  public void setTransitionGroup(boolean paramBoolean)
  {
    mGroupFlags |= 0x2000000;
    if (paramBoolean) {
      mGroupFlags |= 0x1000000;
    } else {
      mGroupFlags &= 0xFEFFFFFF;
    }
  }
  
  boolean shouldBlockFocusForTouchscreen()
  {
    boolean bool;
    if ((getTouchscreenBlocksFocus()) && (mContext.getPackageManager().hasSystemFeature("android.hardware.touchscreen")) && ((!isKeyboardNavigationCluster()) || ((!hasFocus()) && (findKeyboardNavigationCluster() == this)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return true;
  }
  
  public boolean showContextMenuForChild(View paramView)
  {
    boolean bool1 = isShowingContextMenuWithCoords();
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    bool1 = bool2;
    if (mParent != null)
    {
      bool1 = bool2;
      if (mParent.showContextMenuForChild(paramView)) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  /* Error */
  public boolean showContextMenuForChild(View paramView, float paramFloat1, float paramFloat2)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   5: ldc 96
    //   7: ior
    //   8: putfield 409	android/view/ViewGroup:mGroupFlags	I
    //   11: aload_0
    //   12: aload_1
    //   13: invokevirtual 2587	android/view/ViewGroup:showContextMenuForChild	(Landroid/view/View;)Z
    //   16: istore 4
    //   18: iconst_1
    //   19: istore 5
    //   21: iload 4
    //   23: ifeq +17 -> 40
    //   26: aload_0
    //   27: ldc_w 2588
    //   30: aload_0
    //   31: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   34: iand
    //   35: putfield 409	android/view/ViewGroup:mGroupFlags	I
    //   38: iconst_1
    //   39: ireturn
    //   40: aload_0
    //   41: ldc_w 2588
    //   44: aload_0
    //   45: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   48: iand
    //   49: putfield 409	android/view/ViewGroup:mGroupFlags	I
    //   52: aload_0
    //   53: getfield 1156	android/view/ViewGroup:mParent	Landroid/view/ViewParent;
    //   56: ifnull +21 -> 77
    //   59: aload_0
    //   60: getfield 1156	android/view/ViewGroup:mParent	Landroid/view/ViewParent;
    //   63: aload_1
    //   64: fload_2
    //   65: fload_3
    //   66: invokeinterface 2590 4 0
    //   71: ifeq +6 -> 77
    //   74: goto +6 -> 80
    //   77: iconst_0
    //   78: istore 5
    //   80: iload 5
    //   82: ireturn
    //   83: astore_1
    //   84: aload_0
    //   85: ldc_w 2588
    //   88: aload_0
    //   89: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   92: iand
    //   93: putfield 409	android/view/ViewGroup:mGroupFlags	I
    //   96: aload_1
    //   97: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	98	0	this	ViewGroup
    //   0	98	1	paramView	View
    //   0	98	2	paramFloat1	float
    //   0	98	3	paramFloat2	float
    //   16	6	4	bool1	boolean
    //   19	62	5	bool2	boolean
    // Exception table:
    //   from	to	target	type
    //   0	18	83	finally
  }
  
  /* Error */
  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   4: ldc 102
    //   6: iand
    //   7: ifne +51 -> 58
    //   10: aload_0
    //   11: aload_0
    //   12: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   15: ldc 100
    //   17: ior
    //   18: putfield 409	android/view/ViewGroup:mGroupFlags	I
    //   21: aload_0
    //   22: aload_1
    //   23: aload_2
    //   24: iconst_0
    //   25: invokevirtual 2595	android/view/ViewGroup:startActionModeForChild	(Landroid/view/View;Landroid/view/ActionMode$Callback;I)Landroid/view/ActionMode;
    //   28: astore_1
    //   29: aload_0
    //   30: ldc_w 2596
    //   33: aload_0
    //   34: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   37: iand
    //   38: putfield 409	android/view/ViewGroup:mGroupFlags	I
    //   41: aload_1
    //   42: areturn
    //   43: astore_1
    //   44: aload_0
    //   45: ldc_w 2596
    //   48: aload_0
    //   49: getfield 409	android/view/ViewGroup:mGroupFlags	I
    //   52: iand
    //   53: putfield 409	android/view/ViewGroup:mGroupFlags	I
    //   56: aload_1
    //   57: athrow
    //   58: getstatic 221	android/view/ViewGroup:SENTINEL_ACTION_MODE	Landroid/view/ActionMode;
    //   61: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	62	0	this	ViewGroup
    //   0	62	1	paramView	View
    //   0	62	2	paramCallback	ActionMode.Callback
    // Exception table:
    //   from	to	target	type
    //   10	29	43	finally
  }
  
  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback, int paramInt)
  {
    ActionMode localActionMode;
    if (((mGroupFlags & 0x10000000) == 0) && (paramInt == 0)) {
      try
      {
        mGroupFlags |= 0x8000000;
        localActionMode = startActionModeForChild(paramView, paramCallback);
        mGroupFlags = (0xF7FFFFFF & mGroupFlags);
        if (localActionMode != SENTINEL_ACTION_MODE) {
          return localActionMode;
        }
      }
      finally
      {
        mGroupFlags = (0xF7FFFFFF & mGroupFlags);
      }
    }
    if (mParent != null) {
      try
      {
        localActionMode = mParent.startActionModeForChild(paramView, paramCallback, paramInt);
        return localActionMode;
      }
      catch (AbstractMethodError localAbstractMethodError)
      {
        return mParent.startActionModeForChild(paramView, paramCallback);
      }
    }
    return null;
  }
  
  public void startLayoutAnimation()
  {
    if (mLayoutAnimationController != null)
    {
      mGroupFlags |= 0x8;
      requestLayout();
    }
  }
  
  public void startViewTransition(View paramView)
  {
    if (mParent == this)
    {
      if (mTransitioningViews == null) {
        mTransitioningViews = new ArrayList();
      }
      mTransitioningViews.add(paramView);
    }
  }
  
  public void suppressLayout(boolean paramBoolean)
  {
    mSuppressLayout = paramBoolean;
    if ((!paramBoolean) && (mLayoutCalledWhileSuppressed))
    {
      requestLayout();
      mLayoutCalledWhileSuppressed = false;
    }
  }
  
  public void transformPointToViewLocal(float[] paramArrayOfFloat, View paramView)
  {
    paramArrayOfFloat[0] += mScrollX - mLeft;
    paramArrayOfFloat[1] += mScrollY - mTop;
    if (!paramView.hasIdentityMatrix()) {
      paramView.getInverseMatrix().mapPoints(paramArrayOfFloat);
    }
  }
  
  void unFocus(View paramView)
  {
    if (mFocused == null)
    {
      super.unFocus(paramView);
    }
    else
    {
      mFocused.unFocus(paramView);
      mFocused = null;
    }
  }
  
  boolean updateLocalSystemUiVisibility(int paramInt1, int paramInt2)
  {
    boolean bool = super.updateLocalSystemUiVisibility(paramInt1, paramInt2);
    int i = mChildrenCount;
    View[] arrayOfView = mChildren;
    for (int j = 0; j < i; j++) {
      bool |= arrayOfView[j].updateLocalSystemUiVisibility(paramInt1, paramInt2);
    }
    return bool;
  }
  
  public void updateViewLayout(View paramView, LayoutParams paramLayoutParams)
  {
    if (checkLayoutParams(paramLayoutParams))
    {
      if (mParent == this)
      {
        paramView.setLayoutParams(paramLayoutParams);
        return;
      }
      paramView = new StringBuilder();
      paramView.append("Given view not a child of ");
      paramView.append(this);
      throw new IllegalArgumentException(paramView.toString());
    }
    paramView = new StringBuilder();
    paramView.append("Invalid LayoutParams supplied to ");
    paramView.append(this);
    throw new IllegalArgumentException(paramView.toString());
  }
  
  static class ChildListForAccessibility
  {
    private static final int MAX_POOL_SIZE = 32;
    private static final Pools.SynchronizedPool<ChildListForAccessibility> sPool = new Pools.SynchronizedPool(32);
    private final ArrayList<View> mChildren = new ArrayList();
    private final ArrayList<ViewGroup.ViewLocationHolder> mHolders = new ArrayList();
    
    ChildListForAccessibility() {}
    
    private void clear()
    {
      mChildren.clear();
    }
    
    private void init(ViewGroup paramViewGroup, boolean paramBoolean)
    {
      ArrayList localArrayList1 = mChildren;
      int i = paramViewGroup.getChildCount();
      int j = 0;
      for (int k = 0; k < i; k++) {
        localArrayList1.add(paramViewGroup.getChildAt(k));
      }
      if (paramBoolean)
      {
        ArrayList localArrayList2 = mHolders;
        for (k = 0; k < i; k++) {
          localArrayList2.add(ViewGroup.ViewLocationHolder.obtain(paramViewGroup, (View)localArrayList1.get(k)));
        }
        sort(localArrayList2);
        for (k = j; k < i; k++)
        {
          paramViewGroup = (ViewGroup.ViewLocationHolder)localArrayList2.get(k);
          localArrayList1.set(k, mView);
          paramViewGroup.recycle();
        }
        localArrayList2.clear();
      }
    }
    
    public static ChildListForAccessibility obtain(ViewGroup paramViewGroup, boolean paramBoolean)
    {
      ChildListForAccessibility localChildListForAccessibility1 = (ChildListForAccessibility)sPool.acquire();
      ChildListForAccessibility localChildListForAccessibility2 = localChildListForAccessibility1;
      if (localChildListForAccessibility1 == null) {
        localChildListForAccessibility2 = new ChildListForAccessibility();
      }
      localChildListForAccessibility2.init(paramViewGroup, paramBoolean);
      return localChildListForAccessibility2;
    }
    
    private void sort(ArrayList<ViewGroup.ViewLocationHolder> paramArrayList)
    {
      try
      {
        ViewGroup.ViewLocationHolder.setComparisonStrategy(1);
        Collections.sort(paramArrayList);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        ViewGroup.ViewLocationHolder.setComparisonStrategy(2);
        Collections.sort(paramArrayList);
      }
    }
    
    public View getChildAt(int paramInt)
    {
      return (View)mChildren.get(paramInt);
    }
    
    public int getChildCount()
    {
      return mChildren.size();
    }
    
    public void recycle()
    {
      clear();
      sPool.release(this);
    }
  }
  
  static class ChildListForAutoFill
    extends ArrayList<View>
  {
    private static final int MAX_POOL_SIZE = 32;
    private static final Pools.SimplePool<ChildListForAutoFill> sPool = new Pools.SimplePool(32);
    
    ChildListForAutoFill() {}
    
    public static ChildListForAutoFill obtain()
    {
      ChildListForAutoFill localChildListForAutoFill1 = (ChildListForAutoFill)sPool.acquire();
      ChildListForAutoFill localChildListForAutoFill2 = localChildListForAutoFill1;
      if (localChildListForAutoFill1 == null) {
        localChildListForAutoFill2 = new ChildListForAutoFill();
      }
      return localChildListForAutoFill2;
    }
    
    public void recycle()
    {
      clear();
      sPool.release(this);
    }
  }
  
  private static final class HoverTarget
  {
    private static final int MAX_RECYCLED = 32;
    private static HoverTarget sRecycleBin;
    private static final Object sRecycleLock = new Object[0];
    private static int sRecycledCount;
    public View child;
    public HoverTarget next;
    
    private HoverTarget() {}
    
    public static HoverTarget obtain(View paramView)
    {
      if (paramView != null) {
        synchronized (sRecycleLock)
        {
          HoverTarget localHoverTarget;
          if (sRecycleBin == null)
          {
            localHoverTarget = new android/view/ViewGroup$HoverTarget;
            localHoverTarget.<init>();
          }
          else
          {
            localHoverTarget = sRecycleBin;
            sRecycleBin = next;
            sRecycledCount -= 1;
            next = null;
          }
          child = paramView;
          return localHoverTarget;
        }
      }
      throw new IllegalArgumentException("child must be non-null");
    }
    
    public void recycle()
    {
      if (child != null) {
        synchronized (sRecycleLock)
        {
          if (sRecycledCount < 32)
          {
            next = sRecycleBin;
            sRecycleBin = this;
            sRecycledCount += 1;
          }
          else
          {
            next = null;
          }
          child = null;
          return;
        }
      }
      throw new IllegalStateException("already recycled once");
    }
  }
  
  public static class LayoutParams
  {
    @Deprecated
    public static final int FILL_PARENT = -1;
    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;
    @ViewDebug.ExportedProperty(category="layout", mapping={@ViewDebug.IntToString(from=-1, to="MATCH_PARENT"), @ViewDebug.IntToString(from=-2, to="WRAP_CONTENT")})
    public int height;
    public LayoutAnimationController.AnimationParameters layoutAnimationParameters;
    @ViewDebug.ExportedProperty(category="layout", mapping={@ViewDebug.IntToString(from=-1, to="MATCH_PARENT"), @ViewDebug.IntToString(from=-2, to="WRAP_CONTENT")})
    public int width;
    
    LayoutParams() {}
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      width = paramInt1;
      height = paramInt2;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ViewGroup_Layout);
      setBaseAttributes(paramContext, 0, 1);
      paramContext.recycle();
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      width = width;
      height = height;
    }
    
    protected static String sizeToString(int paramInt)
    {
      if (paramInt == -2) {
        return "wrap-content";
      }
      if (paramInt == -1) {
        return "match-parent";
      }
      return String.valueOf(paramInt);
    }
    
    public String debug(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("ViewGroup.LayoutParams={ width=");
      localStringBuilder.append(sizeToString(width));
      localStringBuilder.append(", height=");
      localStringBuilder.append(sizeToString(height));
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
    
    void encode(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      paramViewHierarchyEncoder.beginObject(this);
      encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.endObject();
    }
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      paramViewHierarchyEncoder.addProperty("width", width);
      paramViewHierarchyEncoder.addProperty("height", height);
    }
    
    public void onDebugDraw(View paramView, Canvas paramCanvas, Paint paramPaint) {}
    
    public void resolveLayoutDirection(int paramInt) {}
    
    protected void setBaseAttributes(TypedArray paramTypedArray, int paramInt1, int paramInt2)
    {
      width = paramTypedArray.getLayoutDimension(paramInt1, "layout_width");
      height = paramTypedArray.getLayoutDimension(paramInt2, "layout_height");
    }
  }
  
  public static class MarginLayoutParams
    extends ViewGroup.LayoutParams
  {
    public static final int DEFAULT_MARGIN_RELATIVE = Integer.MIN_VALUE;
    private static final int DEFAULT_MARGIN_RESOLVED = 0;
    private static final int LAYOUT_DIRECTION_MASK = 3;
    private static final int LEFT_MARGIN_UNDEFINED_MASK = 4;
    private static final int NEED_RESOLUTION_MASK = 32;
    private static final int RIGHT_MARGIN_UNDEFINED_MASK = 8;
    private static final int RTL_COMPATIBILITY_MODE_MASK = 16;
    private static final int UNDEFINED_MARGIN = Integer.MIN_VALUE;
    @ViewDebug.ExportedProperty(category="layout")
    public int bottomMargin;
    @ViewDebug.ExportedProperty(category="layout")
    private int endMargin = Integer.MIN_VALUE;
    @ViewDebug.ExportedProperty(category="layout")
    public int leftMargin;
    @ViewDebug.ExportedProperty(category="layout", flagMapping={@ViewDebug.FlagToString(equals=3, mask=3, name="LAYOUT_DIRECTION"), @ViewDebug.FlagToString(equals=4, mask=4, name="LEFT_MARGIN_UNDEFINED_MASK"), @ViewDebug.FlagToString(equals=8, mask=8, name="RIGHT_MARGIN_UNDEFINED_MASK"), @ViewDebug.FlagToString(equals=16, mask=16, name="RTL_COMPATIBILITY_MODE_MASK"), @ViewDebug.FlagToString(equals=32, mask=32, name="NEED_RESOLUTION_MASK")}, formatToHexString=true)
    byte mMarginFlags;
    @ViewDebug.ExportedProperty(category="layout")
    public int rightMargin;
    @ViewDebug.ExportedProperty(category="layout")
    private int startMargin = Integer.MIN_VALUE;
    @ViewDebug.ExportedProperty(category="layout")
    public int topMargin;
    
    public MarginLayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      mMarginFlags = ((byte)(byte)(mMarginFlags | 0x4));
      mMarginFlags = ((byte)(byte)(mMarginFlags | 0x8));
      mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFDF));
      mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFEF));
    }
    
    public MarginLayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ViewGroup_MarginLayout);
      setBaseAttributes(paramAttributeSet, 0, 1);
      int i = paramAttributeSet.getDimensionPixelSize(2, -1);
      if (i >= 0)
      {
        leftMargin = i;
        topMargin = i;
        rightMargin = i;
        bottomMargin = i;
      }
      else
      {
        int j = paramAttributeSet.getDimensionPixelSize(9, -1);
        i = paramAttributeSet.getDimensionPixelSize(10, -1);
        if (j >= 0)
        {
          leftMargin = j;
          rightMargin = j;
        }
        else
        {
          leftMargin = paramAttributeSet.getDimensionPixelSize(3, Integer.MIN_VALUE);
          if (leftMargin == Integer.MIN_VALUE)
          {
            mMarginFlags = ((byte)(byte)(mMarginFlags | 0x4));
            leftMargin = 0;
          }
          rightMargin = paramAttributeSet.getDimensionPixelSize(5, Integer.MIN_VALUE);
          if (rightMargin == Integer.MIN_VALUE)
          {
            mMarginFlags = ((byte)(byte)(mMarginFlags | 0x8));
            rightMargin = 0;
          }
        }
        startMargin = paramAttributeSet.getDimensionPixelSize(7, Integer.MIN_VALUE);
        endMargin = paramAttributeSet.getDimensionPixelSize(8, Integer.MIN_VALUE);
        if (i >= 0)
        {
          topMargin = i;
          bottomMargin = i;
        }
        else
        {
          topMargin = paramAttributeSet.getDimensionPixelSize(4, 0);
          bottomMargin = paramAttributeSet.getDimensionPixelSize(6, 0);
        }
        if (isMarginRelative()) {
          mMarginFlags = ((byte)(byte)(mMarginFlags | 0x20));
        }
      }
      boolean bool = paramContext.getApplicationInfo().hasRtlSupport();
      if ((getApplicationInfotargetSdkVersion < 17) || (!bool)) {
        mMarginFlags = ((byte)(byte)(mMarginFlags | 0x10));
      }
      mMarginFlags = ((byte)(byte)(0x0 | mMarginFlags));
      paramAttributeSet.recycle();
    }
    
    public MarginLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
      mMarginFlags = ((byte)(byte)(mMarginFlags | 0x4));
      mMarginFlags = ((byte)(byte)(mMarginFlags | 0x8));
      mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFDF));
      mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFEF));
    }
    
    public MarginLayoutParams(MarginLayoutParams paramMarginLayoutParams)
    {
      width = width;
      height = height;
      leftMargin = leftMargin;
      topMargin = topMargin;
      rightMargin = rightMargin;
      bottomMargin = bottomMargin;
      startMargin = startMargin;
      endMargin = endMargin;
      mMarginFlags = ((byte)mMarginFlags);
    }
    
    private void doResolveMargins()
    {
      if ((mMarginFlags & 0x10) == 16)
      {
        if (((mMarginFlags & 0x4) == 4) && (startMargin > Integer.MIN_VALUE)) {
          leftMargin = startMargin;
        }
        if (((mMarginFlags & 0x8) == 8) && (endMargin > Integer.MIN_VALUE)) {
          rightMargin = endMargin;
        }
      }
      else
      {
        int i = mMarginFlags;
        int j = 0;
        int k = 0;
        if ((i & 0x3) != 1)
        {
          if (startMargin > Integer.MIN_VALUE) {
            i = startMargin;
          } else {
            i = 0;
          }
          leftMargin = i;
          i = k;
          if (endMargin > Integer.MIN_VALUE) {
            i = endMargin;
          }
          rightMargin = i;
        }
        else
        {
          if (endMargin > Integer.MIN_VALUE) {
            i = endMargin;
          } else {
            i = 0;
          }
          leftMargin = i;
          i = j;
          if (startMargin > Integer.MIN_VALUE) {
            i = startMargin;
          }
          rightMargin = i;
        }
      }
      mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFDF));
    }
    
    public final void copyMarginsFrom(MarginLayoutParams paramMarginLayoutParams)
    {
      leftMargin = leftMargin;
      topMargin = topMargin;
      rightMargin = rightMargin;
      bottomMargin = bottomMargin;
      startMargin = startMargin;
      endMargin = endMargin;
      mMarginFlags = ((byte)mMarginFlags);
    }
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      super.encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.addProperty("leftMargin", leftMargin);
      paramViewHierarchyEncoder.addProperty("topMargin", topMargin);
      paramViewHierarchyEncoder.addProperty("rightMargin", rightMargin);
      paramViewHierarchyEncoder.addProperty("bottomMargin", bottomMargin);
      paramViewHierarchyEncoder.addProperty("startMargin", startMargin);
      paramViewHierarchyEncoder.addProperty("endMargin", endMargin);
    }
    
    public int getLayoutDirection()
    {
      return mMarginFlags & 0x3;
    }
    
    public int getMarginEnd()
    {
      if (endMargin != Integer.MIN_VALUE) {
        return endMargin;
      }
      if ((mMarginFlags & 0x20) == 32) {
        doResolveMargins();
      }
      if ((mMarginFlags & 0x3) != 1) {
        return rightMargin;
      }
      return leftMargin;
    }
    
    public int getMarginStart()
    {
      if (startMargin != Integer.MIN_VALUE) {
        return startMargin;
      }
      if ((mMarginFlags & 0x20) == 32) {
        doResolveMargins();
      }
      if ((mMarginFlags & 0x3) != 1) {
        return leftMargin;
      }
      return rightMargin;
    }
    
    public boolean isLayoutRtl()
    {
      int i = mMarginFlags;
      boolean bool = true;
      if ((i & 0x3) != 1) {
        bool = false;
      }
      return bool;
    }
    
    public boolean isMarginRelative()
    {
      boolean bool;
      if ((startMargin == Integer.MIN_VALUE) && (endMargin == Integer.MIN_VALUE)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public void onDebugDraw(View paramView, Canvas paramCanvas, Paint paramPaint)
    {
      Insets localInsets;
      if (View.isLayoutModeOptical(mParent)) {
        localInsets = paramView.getOpticalInsets();
      } else {
        localInsets = Insets.NONE;
      }
      ViewGroup.fillDifference(paramCanvas, paramView.getLeft() + left, paramView.getTop() + top, paramView.getRight() - right, paramView.getBottom() - bottom, leftMargin, topMargin, rightMargin, bottomMargin, paramPaint);
    }
    
    public void resolveLayoutDirection(int paramInt)
    {
      setLayoutDirection(paramInt);
      if ((isMarginRelative()) && ((mMarginFlags & 0x20) == 32))
      {
        doResolveMargins();
        return;
      }
    }
    
    public void setLayoutDirection(int paramInt)
    {
      if ((paramInt != 0) && (paramInt != 1)) {
        return;
      }
      if (paramInt != (mMarginFlags & 0x3))
      {
        mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFFC));
        mMarginFlags = ((byte)(byte)(mMarginFlags | paramInt & 0x3));
        if (isMarginRelative()) {
          mMarginFlags = ((byte)(byte)(mMarginFlags | 0x20));
        } else {
          mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFDF));
        }
      }
    }
    
    public void setMarginEnd(int paramInt)
    {
      endMargin = paramInt;
      mMarginFlags = ((byte)(byte)(mMarginFlags | 0x20));
    }
    
    public void setMarginStart(int paramInt)
    {
      startMargin = paramInt;
      mMarginFlags = ((byte)(byte)(mMarginFlags | 0x20));
    }
    
    public void setMargins(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      leftMargin = paramInt1;
      topMargin = paramInt2;
      rightMargin = paramInt3;
      bottomMargin = paramInt4;
      mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFFB));
      mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFF7));
      if (isMarginRelative()) {
        mMarginFlags = ((byte)(byte)(mMarginFlags | 0x20));
      } else {
        mMarginFlags = ((byte)(byte)(mMarginFlags & 0xFFFFFFDF));
      }
    }
    
    public void setMarginsRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      startMargin = paramInt1;
      topMargin = paramInt2;
      endMargin = paramInt3;
      bottomMargin = paramInt4;
      mMarginFlags = ((byte)(byte)(mMarginFlags | 0x20));
    }
  }
  
  public static abstract interface OnHierarchyChangeListener
  {
    public abstract void onChildViewAdded(View paramView1, View paramView2);
    
    public abstract void onChildViewRemoved(View paramView1, View paramView2);
  }
  
  private static final class TouchTarget
  {
    public static final int ALL_POINTER_IDS = -1;
    private static final int MAX_RECYCLED = 32;
    private static TouchTarget sRecycleBin;
    private static final Object sRecycleLock = new Object[0];
    private static int sRecycledCount;
    public View child;
    public TouchTarget next;
    public int pointerIdBits;
    
    private TouchTarget() {}
    
    public static TouchTarget obtain(View paramView, int paramInt)
    {
      if (paramView != null) {
        synchronized (sRecycleLock)
        {
          TouchTarget localTouchTarget;
          if (sRecycleBin == null)
          {
            localTouchTarget = new android/view/ViewGroup$TouchTarget;
            localTouchTarget.<init>();
          }
          else
          {
            localTouchTarget = sRecycleBin;
            sRecycleBin = next;
            sRecycledCount -= 1;
            next = null;
          }
          child = paramView;
          pointerIdBits = paramInt;
          return localTouchTarget;
        }
      }
      throw new IllegalArgumentException("child must be non-null");
    }
    
    public void recycle()
    {
      if (child != null) {
        synchronized (sRecycleLock)
        {
          if (sRecycledCount < 32)
          {
            next = sRecycleBin;
            sRecycleBin = this;
            sRecycledCount += 1;
          }
          else
          {
            next = null;
          }
          child = null;
          return;
        }
      }
      throw new IllegalStateException("already recycled once");
    }
  }
  
  static class ViewLocationHolder
    implements Comparable<ViewLocationHolder>
  {
    public static final int COMPARISON_STRATEGY_LOCATION = 2;
    public static final int COMPARISON_STRATEGY_STRIPE = 1;
    private static final int MAX_POOL_SIZE = 32;
    private static int sComparisonStrategy = 1;
    private static final Pools.SynchronizedPool<ViewLocationHolder> sPool = new Pools.SynchronizedPool(32);
    private int mLayoutDirection;
    private final Rect mLocation = new Rect();
    private ViewGroup mRoot;
    public View mView;
    
    ViewLocationHolder() {}
    
    private void clear()
    {
      mView = null;
      mLocation.set(0, 0, 0, 0);
    }
    
    private static int compareBoundsOfTree(ViewLocationHolder paramViewLocationHolder1, ViewLocationHolder paramViewLocationHolder2)
    {
      if (sComparisonStrategy == 1)
      {
        if (mLocation.bottom - mLocation.top <= 0) {
          return -1;
        }
        if (mLocation.top - mLocation.bottom >= 0) {
          return 1;
        }
      }
      if (mLayoutDirection == 0)
      {
        i = mLocation.left - mLocation.left;
        if (i != 0) {
          return i;
        }
      }
      else
      {
        i = mLocation.right - mLocation.right;
        if (i != 0) {
          return -i;
        }
      }
      int i = mLocation.top - mLocation.top;
      if (i != 0) {
        return i;
      }
      i = mLocation.height() - mLocation.height();
      if (i != 0) {
        return -i;
      }
      i = mLocation.width() - mLocation.width();
      if (i != 0) {
        return -i;
      }
      Object localObject = new Rect();
      Rect localRect1 = new Rect();
      Rect localRect2 = new Rect();
      mView.getBoundsOnScreen((Rect)localObject, true);
      mView.getBoundsOnScreen(localRect1, true);
      localObject = mView.findViewByPredicateTraversal(new _..Lambda.ViewGroup.ViewLocationHolder.QbO7cM0ULKe25a7bfXG3VH6DB0c(localRect2, (Rect)localObject), null);
      paramViewLocationHolder2 = mView.findViewByPredicateTraversal(new _..Lambda.ViewGroup.ViewLocationHolder.AjKvqdj7SGGIzA5qrlZUuu71jl8(localRect2, localRect1), null);
      if ((localObject != null) && (paramViewLocationHolder2 != null)) {
        return compareBoundsOfTree(obtain(mRoot, (View)localObject), obtain(mRoot, paramViewLocationHolder2));
      }
      if (localObject != null) {
        return 1;
      }
      if (paramViewLocationHolder2 != null) {
        return -1;
      }
      return 0;
    }
    
    private void init(ViewGroup paramViewGroup, View paramView)
    {
      Rect localRect = mLocation;
      paramView.getDrawingRect(localRect);
      paramViewGroup.offsetDescendantRectToMyCoords(paramView, localRect);
      mView = paramView;
      mRoot = paramViewGroup;
      mLayoutDirection = paramViewGroup.getLayoutDirection();
    }
    
    public static ViewLocationHolder obtain(ViewGroup paramViewGroup, View paramView)
    {
      ViewLocationHolder localViewLocationHolder1 = (ViewLocationHolder)sPool.acquire();
      ViewLocationHolder localViewLocationHolder2 = localViewLocationHolder1;
      if (localViewLocationHolder1 == null) {
        localViewLocationHolder2 = new ViewLocationHolder();
      }
      localViewLocationHolder2.init(paramViewGroup, paramView);
      return localViewLocationHolder2;
    }
    
    public static void setComparisonStrategy(int paramInt)
    {
      sComparisonStrategy = paramInt;
    }
    
    public int compareTo(ViewLocationHolder paramViewLocationHolder)
    {
      if (paramViewLocationHolder == null) {
        return 1;
      }
      int i = compareBoundsOfTree(this, paramViewLocationHolder);
      if (i != 0) {
        return i;
      }
      return mView.getAccessibilityViewId() - mView.getAccessibilityViewId();
    }
    
    public void recycle()
    {
      clear();
      sPool.release(this);
    }
  }
}
