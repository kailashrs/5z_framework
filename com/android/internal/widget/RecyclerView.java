package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import com.android.internal.R.styleable;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView
  extends ViewGroup
  implements ScrollingView, NestedScrollingChild
{
  static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
  private static final boolean ALLOW_THREAD_GAP_WORK;
  private static final int[] CLIP_TO_PADDING_ATTR;
  static final boolean DEBUG = false;
  static final boolean DISPATCH_TEMP_DETACH = false;
  private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
  static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
  static final long FOREVER_NS = Long.MAX_VALUE;
  public static final int HORIZONTAL = 0;
  private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
  private static final int INVALID_POINTER = -1;
  public static final int INVALID_TYPE = -1;
  private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = { Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE };
  static final int MAX_SCROLL_DURATION = 2000;
  private static final int[] NESTED_SCROLLING_ATTRS = { 16843830 };
  public static final long NO_ID = -1L;
  public static final int NO_POSITION = -1;
  static final boolean POST_UPDATES_ON_ANIMATION;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_SETTLING = 2;
  static final String TAG = "RecyclerView";
  public static final int TOUCH_SLOP_DEFAULT = 0;
  public static final int TOUCH_SLOP_PAGING = 1;
  static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
  static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
  private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
  static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
  private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
  private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
  static final String TRACE_PREFETCH_TAG = "RV Prefetch";
  static final String TRACE_SCROLL_TAG = "RV Scroll";
  public static final int VERTICAL = 1;
  static final Interpolator sQuinticInterpolator = new Interpolator()
  {
    public float getInterpolation(float paramAnonymousFloat)
    {
      paramAnonymousFloat -= 1.0F;
      return paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat + 1.0F;
    }
  };
  RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
  private final AccessibilityManager mAccessibilityManager;
  private OnItemTouchListener mActiveOnItemTouchListener;
  Adapter mAdapter;
  AdapterHelper mAdapterHelper;
  boolean mAdapterUpdateDuringMeasure;
  private EdgeEffect mBottomGlow;
  private ChildDrawingOrderCallback mChildDrawingOrderCallback;
  ChildHelper mChildHelper;
  boolean mClipToPadding;
  boolean mDataSetHasChangedAfterLayout = false;
  private int mDispatchScrollCounter = 0;
  private int mEatRequestLayout = 0;
  private int mEatenAccessibilityChangeFlags;
  @VisibleForTesting
  boolean mFirstLayoutComplete;
  GapWorker mGapWorker;
  boolean mHasFixedSize;
  private boolean mIgnoreMotionEventTillDown;
  private int mInitialTouchX;
  private int mInitialTouchY;
  boolean mIsAttached;
  ItemAnimator mItemAnimator = new DefaultItemAnimator();
  private RecyclerView.ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
  private Runnable mItemAnimatorRunner;
  final ArrayList<ItemDecoration> mItemDecorations = new ArrayList();
  boolean mItemsAddedOrRemoved;
  boolean mItemsChanged;
  private int mLastTouchX;
  private int mLastTouchY;
  @VisibleForTesting
  LayoutManager mLayout;
  boolean mLayoutFrozen;
  private int mLayoutOrScrollCounter = 0;
  boolean mLayoutRequestEaten;
  private EdgeEffect mLeftGlow;
  private final int mMaxFlingVelocity;
  private final int mMinFlingVelocity;
  private final int[] mMinMaxLayoutPositions;
  private final int[] mNestedOffsets;
  private final RecyclerViewDataObserver mObserver = new RecyclerViewDataObserver();
  private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
  private OnFlingListener mOnFlingListener;
  private final ArrayList<OnItemTouchListener> mOnItemTouchListeners = new ArrayList();
  @VisibleForTesting
  final List<ViewHolder> mPendingAccessibilityImportanceChange;
  private SavedState mPendingSavedState;
  boolean mPostedAnimatorRunner;
  GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
  private boolean mPreserveFocusAfterLayout = true;
  final Recycler mRecycler = new Recycler();
  RecyclerListener mRecyclerListener;
  private EdgeEffect mRightGlow;
  private final int[] mScrollConsumed;
  private float mScrollFactor = Float.MIN_VALUE;
  private OnScrollListener mScrollListener;
  private List<OnScrollListener> mScrollListeners;
  private final int[] mScrollOffset;
  private int mScrollPointerId = -1;
  private int mScrollState = 0;
  final State mState;
  final Rect mTempRect = new Rect();
  private final Rect mTempRect2 = new Rect();
  final RectF mTempRectF = new RectF();
  private EdgeEffect mTopGlow;
  private int mTouchSlop;
  final Runnable mUpdateChildViewsRunnable = new Runnable()
  {
    public void run()
    {
      if ((mFirstLayoutComplete) && (!isLayoutRequested()))
      {
        if (!mIsAttached)
        {
          requestLayout();
          return;
        }
        if (mLayoutFrozen)
        {
          mLayoutRequestEaten = true;
          return;
        }
        consumePendingUpdateOperations();
        return;
      }
    }
  };
  private VelocityTracker mVelocityTracker;
  final ViewFlinger mViewFlinger = new ViewFlinger();
  private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
  final ViewInfoStore mViewInfoStore = new ViewInfoStore();
  
  static
  {
    CLIP_TO_PADDING_ATTR = new int[] { 16842987 };
    boolean bool;
    if ((Build.VERSION.SDK_INT != 18) && (Build.VERSION.SDK_INT != 19) && (Build.VERSION.SDK_INT != 20)) {
      bool = false;
    } else {
      bool = true;
    }
    FORCE_INVALIDATE_DISPLAY_LIST = bool;
    if (Build.VERSION.SDK_INT >= 23) {
      bool = true;
    } else {
      bool = false;
    }
    ALLOW_SIZE_IN_UNSPECIFIED_SPEC = bool;
    if (Build.VERSION.SDK_INT >= 16) {
      bool = true;
    } else {
      bool = false;
    }
    POST_UPDATES_ON_ANIMATION = bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    }
    ALLOW_THREAD_GAP_WORK = bool;
    if (Build.VERSION.SDK_INT <= 15) {
      bool = true;
    } else {
      bool = false;
    }
    FORCE_ABS_FOCUS_SEARCH_DIRECTION = bool;
    if (Build.VERSION.SDK_INT <= 15) {
      bool = true;
    } else {
      bool = false;
    }
    IGNORE_DETACHED_FOCUSED_CHILD = bool;
  }
  
  public RecyclerView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public RecyclerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public RecyclerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (ALLOW_THREAD_GAP_WORK) {
      localObject = new GapWorker.LayoutPrefetchRegistryImpl();
    } else {
      localObject = null;
    }
    mPrefetchRegistry = ((GapWorker.LayoutPrefetchRegistryImpl)localObject);
    mState = new State();
    mItemsAddedOrRemoved = false;
    mItemsChanged = false;
    mItemAnimatorListener = new ItemAnimatorRestoreListener();
    mPostedAnimatorRunner = false;
    mMinMaxLayoutPositions = new int[2];
    mScrollOffset = new int[2];
    mScrollConsumed = new int[2];
    mNestedOffsets = new int[2];
    mPendingAccessibilityImportanceChange = new ArrayList();
    mItemAnimatorRunner = new Runnable()
    {
      public void run()
      {
        if (mItemAnimator != null) {
          mItemAnimator.runPendingAnimations();
        }
        mPostedAnimatorRunner = false;
      }
    };
    mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback()
    {
      public void processAppeared(RecyclerView.ViewHolder paramAnonymousViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramAnonymousItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramAnonymousItemHolderInfo2)
      {
        animateAppearance(paramAnonymousViewHolder, paramAnonymousItemHolderInfo1, paramAnonymousItemHolderInfo2);
      }
      
      public void processDisappeared(RecyclerView.ViewHolder paramAnonymousViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramAnonymousItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramAnonymousItemHolderInfo2)
      {
        mRecycler.unscrapView(paramAnonymousViewHolder);
        animateDisappearance(paramAnonymousViewHolder, paramAnonymousItemHolderInfo1, paramAnonymousItemHolderInfo2);
      }
      
      public void processPersistent(RecyclerView.ViewHolder paramAnonymousViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramAnonymousItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramAnonymousItemHolderInfo2)
      {
        paramAnonymousViewHolder.setIsRecyclable(false);
        if (mDataSetHasChangedAfterLayout)
        {
          if (mItemAnimator.animateChange(paramAnonymousViewHolder, paramAnonymousViewHolder, paramAnonymousItemHolderInfo1, paramAnonymousItemHolderInfo2)) {
            postAnimationRunner();
          }
        }
        else if (mItemAnimator.animatePersistence(paramAnonymousViewHolder, paramAnonymousItemHolderInfo1, paramAnonymousItemHolderInfo2)) {
          postAnimationRunner();
        }
      }
      
      public void unused(RecyclerView.ViewHolder paramAnonymousViewHolder)
      {
        mLayout.removeAndRecycleView(itemView, mRecycler);
      }
    };
    if (paramAttributeSet != null)
    {
      localObject = paramContext.obtainStyledAttributes(paramAttributeSet, CLIP_TO_PADDING_ATTR, paramInt, 0);
      mClipToPadding = ((TypedArray)localObject).getBoolean(0, true);
      ((TypedArray)localObject).recycle();
    }
    else
    {
      mClipToPadding = true;
    }
    setScrollContainer(true);
    setFocusableInTouchMode(true);
    Object localObject = ViewConfiguration.get(paramContext);
    mTouchSlop = ((ViewConfiguration)localObject).getScaledTouchSlop();
    mMinFlingVelocity = ((ViewConfiguration)localObject).getScaledMinimumFlingVelocity();
    mMaxFlingVelocity = ((ViewConfiguration)localObject).getScaledMaximumFlingVelocity();
    if (getOverScrollMode() == 2) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    setWillNotDraw(bool1);
    mItemAnimator.setListener(mItemAnimatorListener);
    initAdapterManager();
    initChildrenHelper();
    if (getImportantForAccessibility() == 0) {
      setImportantForAccessibility(1);
    }
    mAccessibilityManager = ((AccessibilityManager)getContext().getSystemService("accessibility"));
    setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
    boolean bool2 = true;
    boolean bool1 = true;
    if (paramAttributeSet != null)
    {
      localObject = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecyclerView, paramInt, 0);
      String str = ((TypedArray)localObject).getString(2);
      if (((TypedArray)localObject).getInt(1, -1) == -1) {
        setDescendantFocusability(262144);
      }
      ((TypedArray)localObject).recycle();
      createLayoutManager(paramContext, str, paramAttributeSet, paramInt, 0);
      if (Build.VERSION.SDK_INT >= 21)
      {
        paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, NESTED_SCROLLING_ATTRS, paramInt, 0);
        bool1 = paramContext.getBoolean(0, true);
        paramContext.recycle();
      }
    }
    else
    {
      setDescendantFocusability(262144);
      bool1 = bool2;
    }
    setNestedScrollingEnabled(bool1);
  }
  
  private void addAnimatingView(ViewHolder paramViewHolder)
  {
    View localView = itemView;
    int i;
    if (localView.getParent() == this) {
      i = 1;
    } else {
      i = 0;
    }
    mRecycler.unscrapView(getChildViewHolder(localView));
    if (paramViewHolder.isTmpDetached()) {
      mChildHelper.attachViewToParent(localView, -1, localView.getLayoutParams(), true);
    } else if (i == 0) {
      mChildHelper.addView(localView, true);
    } else {
      mChildHelper.hide(localView);
    }
  }
  
  private void animateChange(ViewHolder paramViewHolder1, ViewHolder paramViewHolder2, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2, boolean paramBoolean1, boolean paramBoolean2)
  {
    paramViewHolder1.setIsRecyclable(false);
    if (paramBoolean1) {
      addAnimatingView(paramViewHolder1);
    }
    if (paramViewHolder1 != paramViewHolder2)
    {
      if (paramBoolean2) {
        addAnimatingView(paramViewHolder2);
      }
      mShadowedHolder = paramViewHolder2;
      addAnimatingView(paramViewHolder1);
      mRecycler.unscrapView(paramViewHolder1);
      paramViewHolder2.setIsRecyclable(false);
      mShadowingHolder = paramViewHolder1;
    }
    if (mItemAnimator.animateChange(paramViewHolder1, paramViewHolder2, paramItemHolderInfo1, paramItemHolderInfo2)) {
      postAnimationRunner();
    }
  }
  
  private void cancelTouch()
  {
    resetTouch();
    setScrollState(0);
  }
  
  static void clearNestedRecyclerViewIfNotNested(ViewHolder paramViewHolder)
  {
    if (mNestedRecyclerView != null)
    {
      Object localObject = (View)mNestedRecyclerView.get();
      while (localObject != null)
      {
        if (localObject == itemView) {
          return;
        }
        localObject = ((View)localObject).getParent();
        if ((localObject instanceof View)) {
          localObject = (View)localObject;
        } else {
          localObject = null;
        }
      }
      mNestedRecyclerView = null;
    }
  }
  
  private void createLayoutManager(Context paramContext, String paramString, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    if (paramString != null)
    {
      paramString = paramString.trim();
      if (paramString.length() != 0)
      {
        String str = getFullClassName(paramContext, paramString);
        try
        {
          if (isInEditMode()) {
            paramString = getClass().getClassLoader();
          } else {
            paramString = paramContext.getClassLoader();
          }
          Class localClass = paramString.loadClass(str).asSubclass(LayoutManager.class);
          paramString = null;
          try
          {
            Constructor localConstructor = localClass.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
            paramString = new Object[] { paramContext, paramAttributeSet, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) };
            paramContext = localConstructor;
          }
          catch (NoSuchMethodException localNoSuchMethodException) {}
          try
          {
            paramContext = localClass.getConstructor(new Class[0]);
            paramContext.setAccessible(true);
            setLayoutManager((LayoutManager)paramContext.newInstance(paramString));
          }
          catch (NoSuchMethodException paramContext)
          {
            paramContext.initCause(localNoSuchMethodException);
            IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
            paramString = new java/lang/StringBuilder;
            paramString.<init>();
            paramString.append(paramAttributeSet.getPositionDescription());
            paramString.append(": Error creating LayoutManager ");
            paramString.append(str);
            localIllegalStateException.<init>(paramString.toString(), paramContext);
            throw localIllegalStateException;
          }
          return;
        }
        catch (ClassCastException paramContext)
        {
          paramString = new StringBuilder();
          paramString.append(paramAttributeSet.getPositionDescription());
          paramString.append(": Class is not a LayoutManager ");
          paramString.append(str);
          throw new IllegalStateException(paramString.toString(), paramContext);
        }
        catch (IllegalAccessException paramContext)
        {
          paramString = new StringBuilder();
          paramString.append(paramAttributeSet.getPositionDescription());
          paramString.append(": Cannot access non-public constructor ");
          paramString.append(str);
          throw new IllegalStateException(paramString.toString(), paramContext);
        }
        catch (InstantiationException paramContext)
        {
          paramString = new StringBuilder();
          paramString.append(paramAttributeSet.getPositionDescription());
          paramString.append(": Could not instantiate the LayoutManager: ");
          paramString.append(str);
          throw new IllegalStateException(paramString.toString(), paramContext);
        }
        catch (InvocationTargetException paramString)
        {
          paramContext = new StringBuilder();
          paramContext.append(paramAttributeSet.getPositionDescription());
          paramContext.append(": Could not instantiate the LayoutManager: ");
          paramContext.append(str);
          throw new IllegalStateException(paramContext.toString(), paramString);
        }
        catch (ClassNotFoundException paramContext)
        {
          paramString = new StringBuilder();
          paramString.append(paramAttributeSet.getPositionDescription());
          paramString.append(": Unable to find LayoutManager ");
          paramString.append(str);
          throw new IllegalStateException(paramString.toString(), paramContext);
        }
      }
    }
  }
  
  private boolean didChildRangeChange(int paramInt1, int paramInt2)
  {
    findMinMaxChildLayoutPositions(mMinMaxLayoutPositions);
    int[] arrayOfInt = mMinMaxLayoutPositions;
    boolean bool = false;
    if ((arrayOfInt[0] == paramInt1) && (mMinMaxLayoutPositions[1] == paramInt2)) {
      break label42;
    }
    bool = true;
    label42:
    return bool;
  }
  
  private void dispatchContentChangedIfNecessary()
  {
    int i = mEatenAccessibilityChangeFlags;
    mEatenAccessibilityChangeFlags = 0;
    if ((i != 0) && (isAccessibilityEnabled()))
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain();
      localAccessibilityEvent.setEventType(2048);
      localAccessibilityEvent.setContentChangeTypes(i);
      sendAccessibilityEventUnchecked(localAccessibilityEvent);
    }
  }
  
  private void dispatchLayoutStep1()
  {
    Object localObject = mState;
    boolean bool = true;
    ((State)localObject).assertLayoutStep(1);
    mState.mIsMeasuring = false;
    eatRequestLayout();
    mViewInfoStore.clear();
    onEnterLayoutOrScroll();
    processAdapterUpdatesAndSetAnimationFlags();
    saveFocusInfo();
    localObject = mState;
    if ((!mState.mRunSimpleAnimations) || (!mItemsChanged)) {
      bool = false;
    }
    mTrackOldChangeHolders = bool;
    mItemsChanged = false;
    mItemsAddedOrRemoved = false;
    mState.mInPreLayout = mState.mRunPredictiveAnimations;
    mState.mItemCount = mAdapter.getItemCount();
    findMinMaxChildLayoutPositions(mMinMaxLayoutPositions);
    int i;
    int j;
    RecyclerView.ItemAnimator.ItemHolderInfo localItemHolderInfo;
    if (mState.mRunSimpleAnimations)
    {
      i = mChildHelper.getChildCount();
      for (j = 0; j < i; j++)
      {
        localObject = getChildViewHolderInt(mChildHelper.getChildAt(j));
        if ((!((ViewHolder)localObject).shouldIgnore()) && ((!((ViewHolder)localObject).isInvalid()) || (mAdapter.hasStableIds())))
        {
          localItemHolderInfo = mItemAnimator.recordPreLayoutInformation(mState, (ViewHolder)localObject, ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)localObject), ((ViewHolder)localObject).getUnmodifiedPayloads());
          mViewInfoStore.addToPreLayout((ViewHolder)localObject, localItemHolderInfo);
          if ((mState.mTrackOldChangeHolders) && (((ViewHolder)localObject).isUpdated()) && (!((ViewHolder)localObject).isRemoved()) && (!((ViewHolder)localObject).shouldIgnore()) && (!((ViewHolder)localObject).isInvalid()))
          {
            long l = getChangedHolderKey((ViewHolder)localObject);
            mViewInfoStore.addToOldChangeHolders(l, (ViewHolder)localObject);
          }
        }
      }
    }
    if (mState.mRunPredictiveAnimations)
    {
      saveOldPositions();
      bool = mState.mStructureChanged;
      mState.mStructureChanged = false;
      mLayout.onLayoutChildren(mRecycler, mState);
      mState.mStructureChanged = bool;
      for (j = 0; j < mChildHelper.getChildCount(); j++)
      {
        localObject = getChildViewHolderInt(mChildHelper.getChildAt(j));
        if ((!((ViewHolder)localObject).shouldIgnore()) && (!mViewInfoStore.isInPreLayout((ViewHolder)localObject)))
        {
          int k = ItemAnimator.buildAdapterChangeFlagsForAnimations((ViewHolder)localObject);
          bool = ((ViewHolder)localObject).hasAnyOfTheFlags(8192);
          i = k;
          if (!bool) {
            i = k | 0x1000;
          }
          localItemHolderInfo = mItemAnimator.recordPreLayoutInformation(mState, (ViewHolder)localObject, i, ((ViewHolder)localObject).getUnmodifiedPayloads());
          if (bool) {
            recordAnimationInfoIfBouncedHiddenView((ViewHolder)localObject, localItemHolderInfo);
          } else {
            mViewInfoStore.addToAppearedInPreLayoutHolders((ViewHolder)localObject, localItemHolderInfo);
          }
        }
      }
      clearOldPositions();
    }
    else
    {
      clearOldPositions();
    }
    onExitLayoutOrScroll();
    resumeRequestLayout(false);
    mState.mLayoutStep = 2;
  }
  
  private void dispatchLayoutStep2()
  {
    eatRequestLayout();
    onEnterLayoutOrScroll();
    mState.assertLayoutStep(6);
    mAdapterHelper.consumeUpdatesInOnePass();
    mState.mItemCount = mAdapter.getItemCount();
    mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
    mState.mInPreLayout = false;
    mLayout.onLayoutChildren(mRecycler, mState);
    mState.mStructureChanged = false;
    mPendingSavedState = null;
    State localState = mState;
    boolean bool;
    if ((mState.mRunSimpleAnimations) && (mItemAnimator != null)) {
      bool = true;
    } else {
      bool = false;
    }
    mRunSimpleAnimations = bool;
    mState.mLayoutStep = 4;
    onExitLayoutOrScroll();
    resumeRequestLayout(false);
  }
  
  private void dispatchLayoutStep3()
  {
    mState.assertLayoutStep(4);
    eatRequestLayout();
    onEnterLayoutOrScroll();
    mState.mLayoutStep = 1;
    if (mState.mRunSimpleAnimations)
    {
      for (int i = mChildHelper.getChildCount() - 1; i >= 0; i--)
      {
        ViewHolder localViewHolder1 = getChildViewHolderInt(mChildHelper.getChildAt(i));
        if (!localViewHolder1.shouldIgnore())
        {
          long l = getChangedHolderKey(localViewHolder1);
          RecyclerView.ItemAnimator.ItemHolderInfo localItemHolderInfo1 = mItemAnimator.recordPostLayoutInformation(mState, localViewHolder1);
          ViewHolder localViewHolder2 = mViewInfoStore.getFromOldChangeHolders(l);
          if ((localViewHolder2 != null) && (!localViewHolder2.shouldIgnore()))
          {
            boolean bool1 = mViewInfoStore.isDisappearing(localViewHolder2);
            boolean bool2 = mViewInfoStore.isDisappearing(localViewHolder1);
            if ((bool1) && (localViewHolder2 == localViewHolder1))
            {
              mViewInfoStore.addToPostLayout(localViewHolder1, localItemHolderInfo1);
            }
            else
            {
              RecyclerView.ItemAnimator.ItemHolderInfo localItemHolderInfo2 = mViewInfoStore.popFromPreLayout(localViewHolder2);
              mViewInfoStore.addToPostLayout(localViewHolder1, localItemHolderInfo1);
              localItemHolderInfo1 = mViewInfoStore.popFromPostLayout(localViewHolder1);
              if (localItemHolderInfo2 == null) {
                handleMissingPreInfoForChangeError(l, localViewHolder1, localViewHolder2);
              } else {
                animateChange(localViewHolder2, localViewHolder1, localItemHolderInfo2, localItemHolderInfo1, bool1, bool2);
              }
            }
          }
          else
          {
            mViewInfoStore.addToPostLayout(localViewHolder1, localItemHolderInfo1);
          }
        }
      }
      mViewInfoStore.process(mViewInfoProcessCallback);
    }
    mLayout.removeAndRecycleScrapInt(mRecycler);
    mState.mPreviousLayoutItemCount = mState.mItemCount;
    mDataSetHasChangedAfterLayout = false;
    mState.mRunSimpleAnimations = false;
    mState.mRunPredictiveAnimations = false;
    mLayout.mRequestedSimpleAnimations = false;
    if (mRecycler.mChangedScrap != null) {
      mRecycler.mChangedScrap.clear();
    }
    if (mLayout.mPrefetchMaxObservedInInitialPrefetch)
    {
      mLayout.mPrefetchMaxCountObserved = 0;
      mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
      mRecycler.updateViewCacheSize();
    }
    mLayout.onLayoutCompleted(mState);
    onExitLayoutOrScroll();
    resumeRequestLayout(false);
    mViewInfoStore.clear();
    if (didChildRangeChange(mMinMaxLayoutPositions[0], mMinMaxLayoutPositions[1])) {
      dispatchOnScrolled(0, 0);
    }
    recoverFocusFromState();
    resetFocusInfo();
  }
  
  private boolean dispatchOnItemTouch(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if (mActiveOnItemTouchListener != null) {
      if (i == 0)
      {
        mActiveOnItemTouchListener = null;
      }
      else
      {
        mActiveOnItemTouchListener.onTouchEvent(this, paramMotionEvent);
        if ((i == 3) || (i == 1)) {
          mActiveOnItemTouchListener = null;
        }
        return true;
      }
    }
    if (i != 0)
    {
      int j = mOnItemTouchListeners.size();
      for (i = 0; i < j; i++)
      {
        OnItemTouchListener localOnItemTouchListener = (OnItemTouchListener)mOnItemTouchListeners.get(i);
        if (localOnItemTouchListener.onInterceptTouchEvent(this, paramMotionEvent))
        {
          mActiveOnItemTouchListener = localOnItemTouchListener;
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean dispatchOnItemTouchIntercept(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if ((i == 3) || (i == 0)) {
      mActiveOnItemTouchListener = null;
    }
    int j = mOnItemTouchListeners.size();
    for (int k = 0; k < j; k++)
    {
      OnItemTouchListener localOnItemTouchListener = (OnItemTouchListener)mOnItemTouchListeners.get(k);
      if ((localOnItemTouchListener.onInterceptTouchEvent(this, paramMotionEvent)) && (i != 3))
      {
        mActiveOnItemTouchListener = localOnItemTouchListener;
        return true;
      }
    }
    return false;
  }
  
  private void findMinMaxChildLayoutPositions(int[] paramArrayOfInt)
  {
    int i = mChildHelper.getChildCount();
    if (i == 0)
    {
      paramArrayOfInt[0] = -1;
      paramArrayOfInt[1] = -1;
      return;
    }
    int j = Integer.MIN_VALUE;
    int k = Integer.MAX_VALUE;
    int m = 0;
    while (m < i)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getChildAt(m));
      int n;
      if (localViewHolder.shouldIgnore())
      {
        n = j;
      }
      else
      {
        int i1 = localViewHolder.getLayoutPosition();
        int i2 = k;
        if (i1 < k) {
          i2 = i1;
        }
        k = i2;
        n = j;
        if (i1 > j)
        {
          n = i1;
          k = i2;
        }
      }
      m++;
      j = n;
    }
    paramArrayOfInt[0] = k;
    paramArrayOfInt[1] = j;
  }
  
  static RecyclerView findNestedRecyclerView(View paramView)
  {
    if (!(paramView instanceof ViewGroup)) {
      return null;
    }
    if ((paramView instanceof RecyclerView)) {
      return (RecyclerView)paramView;
    }
    ViewGroup localViewGroup = (ViewGroup)paramView;
    int i = localViewGroup.getChildCount();
    for (int j = 0; j < i; j++)
    {
      paramView = findNestedRecyclerView(localViewGroup.getChildAt(j));
      if (paramView != null) {
        return paramView;
      }
    }
    return null;
  }
  
  private View findNextViewToFocus()
  {
    if (mState.mFocusedItemPosition != -1) {
      i = mState.mFocusedItemPosition;
    } else {
      i = 0;
    }
    int j = mState.getItemCount();
    ViewHolder localViewHolder;
    for (int k = i; k < j; k++)
    {
      localViewHolder = findViewHolderForAdapterPosition(k);
      if (localViewHolder == null) {
        break;
      }
      if (itemView.hasFocusable()) {
        return itemView;
      }
    }
    for (int i = Math.min(j, i) - 1; i >= 0; i--)
    {
      localViewHolder = findViewHolderForAdapterPosition(i);
      if (localViewHolder == null) {
        return null;
      }
      if (itemView.hasFocusable()) {
        return itemView;
      }
    }
    return null;
  }
  
  static ViewHolder getChildViewHolderInt(View paramView)
  {
    if (paramView == null) {
      return null;
    }
    return getLayoutParamsmViewHolder;
  }
  
  static void getDecoratedBoundsWithMarginsInt(View paramView, Rect paramRect)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect localRect = mDecorInsets;
    paramRect.set(paramView.getLeft() - left - leftMargin, paramView.getTop() - top - topMargin, paramView.getRight() + right + rightMargin, paramView.getBottom() + bottom + bottomMargin);
  }
  
  private int getDeepestFocusedViewWithId(View paramView)
  {
    int i = paramView.getId();
    while ((!paramView.isFocused()) && ((paramView instanceof ViewGroup)) && (paramView.hasFocus()))
    {
      paramView = ((ViewGroup)paramView).getFocusedChild();
      if (paramView.getId() != -1) {
        i = paramView.getId();
      }
    }
    return i;
  }
  
  private String getFullClassName(Context paramContext, String paramString)
  {
    if (paramString.charAt(0) == '.')
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramContext.getPackageName());
      localStringBuilder.append(paramString);
      return localStringBuilder.toString();
    }
    if (paramString.contains(".")) {
      return paramString;
    }
    paramContext = new StringBuilder();
    paramContext.append(RecyclerView.class.getPackage().getName());
    paramContext.append('.');
    paramContext.append(paramString);
    return paramContext.toString();
  }
  
  private float getScrollFactor()
  {
    if (mScrollFactor == Float.MIN_VALUE)
    {
      TypedValue localTypedValue = new TypedValue();
      if (getContext().getTheme().resolveAttribute(16842829, localTypedValue, true)) {
        mScrollFactor = localTypedValue.getDimension(getContext().getResources().getDisplayMetrics());
      } else {
        return 0.0F;
      }
    }
    return mScrollFactor;
  }
  
  private void handleMissingPreInfoForChangeError(long paramLong, ViewHolder paramViewHolder1, ViewHolder paramViewHolder2)
  {
    int i = mChildHelper.getChildCount();
    for (int j = 0; j < i; j++)
    {
      localObject = getChildViewHolderInt(mChildHelper.getChildAt(j));
      if ((localObject != paramViewHolder1) && (getChangedHolderKey((ViewHolder)localObject) == paramLong))
      {
        if ((mAdapter != null) && (mAdapter.hasStableIds()))
        {
          paramViewHolder2 = new StringBuilder();
          paramViewHolder2.append("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:");
          paramViewHolder2.append(localObject);
          paramViewHolder2.append(" \n View Holder 2:");
          paramViewHolder2.append(paramViewHolder1);
          throw new IllegalStateException(paramViewHolder2.toString());
        }
        paramViewHolder2 = new StringBuilder();
        paramViewHolder2.append("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:");
        paramViewHolder2.append(localObject);
        paramViewHolder2.append(" \n View Holder 2:");
        paramViewHolder2.append(paramViewHolder1);
        throw new IllegalStateException(paramViewHolder2.toString());
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Problem while matching changed view holders with the newones. The pre-layout information for the change holder ");
    ((StringBuilder)localObject).append(paramViewHolder2);
    ((StringBuilder)localObject).append(" cannot be found but it is necessary for ");
    ((StringBuilder)localObject).append(paramViewHolder1);
    Log.e("RecyclerView", ((StringBuilder)localObject).toString());
  }
  
  private boolean hasUpdatedView()
  {
    int i = mChildHelper.getChildCount();
    for (int j = 0; j < i; j++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getChildAt(j));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore()) && (localViewHolder.isUpdated())) {
        return true;
      }
    }
    return false;
  }
  
  private void initChildrenHelper()
  {
    mChildHelper = new ChildHelper(new ChildHelper.Callback()
    {
      public void addView(View paramAnonymousView, int paramAnonymousInt)
      {
        RecyclerView.this.addView(paramAnonymousView, paramAnonymousInt);
        dispatchChildAttached(paramAnonymousView);
      }
      
      public void attachViewToParent(View paramAnonymousView, int paramAnonymousInt, ViewGroup.LayoutParams paramAnonymousLayoutParams)
      {
        RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramAnonymousView);
        if (localViewHolder != null)
        {
          if ((!localViewHolder.isTmpDetached()) && (!localViewHolder.shouldIgnore()))
          {
            paramAnonymousView = new StringBuilder();
            paramAnonymousView.append("Called attach on a child which is not detached: ");
            paramAnonymousView.append(localViewHolder);
            throw new IllegalArgumentException(paramAnonymousView.toString());
          }
          localViewHolder.clearTmpDetachFlag();
        }
        RecyclerView.this.attachViewToParent(paramAnonymousView, paramAnonymousInt, paramAnonymousLayoutParams);
      }
      
      public void detachViewFromParent(int paramAnonymousInt)
      {
        Object localObject = getChildAt(paramAnonymousInt);
        if (localObject != null)
        {
          localObject = RecyclerView.getChildViewHolderInt((View)localObject);
          if (localObject != null)
          {
            if ((((RecyclerView.ViewHolder)localObject).isTmpDetached()) && (!((RecyclerView.ViewHolder)localObject).shouldIgnore()))
            {
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("called detach on an already detached child ");
              localStringBuilder.append(localObject);
              throw new IllegalArgumentException(localStringBuilder.toString());
            }
            ((RecyclerView.ViewHolder)localObject).addFlags(256);
          }
        }
        RecyclerView.this.detachViewFromParent(paramAnonymousInt);
      }
      
      public View getChildAt(int paramAnonymousInt)
      {
        return RecyclerView.this.getChildAt(paramAnonymousInt);
      }
      
      public int getChildCount()
      {
        return RecyclerView.this.getChildCount();
      }
      
      public RecyclerView.ViewHolder getChildViewHolder(View paramAnonymousView)
      {
        return RecyclerView.getChildViewHolderInt(paramAnonymousView);
      }
      
      public int indexOfChild(View paramAnonymousView)
      {
        return RecyclerView.this.indexOfChild(paramAnonymousView);
      }
      
      public void onEnteredHiddenState(View paramAnonymousView)
      {
        paramAnonymousView = RecyclerView.getChildViewHolderInt(paramAnonymousView);
        if (paramAnonymousView != null) {
          RecyclerView.ViewHolder.access$200(paramAnonymousView, RecyclerView.this);
        }
      }
      
      public void onLeftHiddenState(View paramAnonymousView)
      {
        paramAnonymousView = RecyclerView.getChildViewHolderInt(paramAnonymousView);
        if (paramAnonymousView != null) {
          RecyclerView.ViewHolder.access$300(paramAnonymousView, RecyclerView.this);
        }
      }
      
      public void removeAllViews()
      {
        int i = getChildCount();
        for (int j = 0; j < i; j++) {
          dispatchChildDetached(getChildAt(j));
        }
        RecyclerView.this.removeAllViews();
      }
      
      public void removeViewAt(int paramAnonymousInt)
      {
        View localView = RecyclerView.this.getChildAt(paramAnonymousInt);
        if (localView != null) {
          dispatchChildDetached(localView);
        }
        RecyclerView.this.removeViewAt(paramAnonymousInt);
      }
    });
  }
  
  private boolean isPreferredNextFocus(View paramView1, View paramView2, int paramInt)
  {
    int i = 0;
    if ((paramView2 != null) && (paramView2 != this))
    {
      if (paramView1 == null) {
        return true;
      }
      if ((paramInt != 2) && (paramInt != 1)) {
        return isPreferredNextFocusAbsolute(paramView1, paramView2, paramInt);
      }
      int j;
      if (mLayout.getLayoutDirection() == 1) {
        j = 1;
      } else {
        j = 0;
      }
      if (paramInt == 2) {
        i = 1;
      }
      if ((i ^ j) != 0) {
        j = 66;
      } else {
        j = 17;
      }
      if (isPreferredNextFocusAbsolute(paramView1, paramView2, j)) {
        return true;
      }
      if (paramInt == 2) {
        return isPreferredNextFocusAbsolute(paramView1, paramView2, 130);
      }
      return isPreferredNextFocusAbsolute(paramView1, paramView2, 33);
    }
    return false;
  }
  
  private boolean isPreferredNextFocusAbsolute(View paramView1, View paramView2, int paramInt)
  {
    mTempRect.set(0, 0, paramView1.getWidth(), paramView1.getHeight());
    mTempRect2.set(0, 0, paramView2.getWidth(), paramView2.getHeight());
    offsetDescendantRectToMyCoords(paramView1, mTempRect);
    offsetDescendantRectToMyCoords(paramView2, mTempRect2);
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    boolean bool4 = true;
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt == 130)
          {
            if (((mTempRect.top >= mTempRect2.top) && (mTempRect.bottom > mTempRect2.top)) || (mTempRect.bottom >= mTempRect2.bottom)) {
              bool4 = false;
            }
            return bool4;
          }
          paramView1 = new StringBuilder();
          paramView1.append("direction must be absolute. received:");
          paramView1.append(paramInt);
          throw new IllegalArgumentException(paramView1.toString());
        }
        if (((mTempRect.left < mTempRect2.left) || (mTempRect.right <= mTempRect2.left)) && (mTempRect.right < mTempRect2.right)) {
          bool4 = bool1;
        } else {
          bool4 = false;
        }
        return bool4;
      }
      if (((mTempRect.bottom > mTempRect2.bottom) || (mTempRect.top >= mTempRect2.bottom)) && (mTempRect.top > mTempRect2.top)) {
        bool4 = bool2;
      } else {
        bool4 = false;
      }
      return bool4;
    }
    if (((mTempRect.right > mTempRect2.right) || (mTempRect.left >= mTempRect2.right)) && (mTempRect.left > mTempRect2.left)) {
      bool4 = bool3;
    } else {
      bool4 = false;
    }
    return bool4;
  }
  
  private void onPointerUp(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == mScrollPointerId)
    {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      }
      mScrollPointerId = paramMotionEvent.getPointerId(i);
      int j = (int)(paramMotionEvent.getX(i) + 0.5F);
      mLastTouchX = j;
      mInitialTouchX = j;
      i = (int)(paramMotionEvent.getY(i) + 0.5F);
      mLastTouchY = i;
      mInitialTouchY = i;
    }
  }
  
  private boolean predictiveItemAnimationsEnabled()
  {
    boolean bool;
    if ((mItemAnimator != null) && (mLayout.supportsPredictiveItemAnimations())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void processAdapterUpdatesAndSetAnimationFlags()
  {
    if (mDataSetHasChangedAfterLayout)
    {
      mAdapterHelper.reset();
      mLayout.onItemsChanged(this);
    }
    if (predictiveItemAnimationsEnabled()) {
      mAdapterHelper.preProcess();
    } else {
      mAdapterHelper.consumeUpdatesInOnePass();
    }
    boolean bool1 = mItemsAddedOrRemoved;
    boolean bool2 = true;
    int i;
    if ((!bool1) && (!mItemsChanged)) {
      i = 0;
    } else {
      i = 1;
    }
    State localState = mState;
    if ((mFirstLayoutComplete) && (mItemAnimator != null) && ((mDataSetHasChangedAfterLayout) || (i != 0) || (mLayout.mRequestedSimpleAnimations)) && ((!mDataSetHasChangedAfterLayout) || (mAdapter.hasStableIds()))) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    mRunSimpleAnimations = bool1;
    localState = mState;
    if ((mState.mRunSimpleAnimations) && (i != 0) && (!mDataSetHasChangedAfterLayout) && (predictiveItemAnimationsEnabled())) {
      bool1 = bool2;
    } else {
      bool1 = false;
    }
    mRunPredictiveAnimations = bool1;
  }
  
  private void pullGlows(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    int i = 0;
    if (paramFloat2 < 0.0F)
    {
      ensureLeftGlow();
      mLeftGlow.onPull(-paramFloat2 / getWidth(), 1.0F - paramFloat3 / getHeight());
      i = 1;
    }
    else if (paramFloat2 > 0.0F)
    {
      ensureRightGlow();
      mRightGlow.onPull(paramFloat2 / getWidth(), paramFloat3 / getHeight());
      i = 1;
    }
    if (paramFloat4 < 0.0F)
    {
      ensureTopGlow();
      mTopGlow.onPull(-paramFloat4 / getHeight(), paramFloat1 / getWidth());
      i = 1;
    }
    else if (paramFloat4 > 0.0F)
    {
      ensureBottomGlow();
      mBottomGlow.onPull(paramFloat4 / getHeight(), 1.0F - paramFloat1 / getWidth());
      i = 1;
    }
    if ((i != 0) || (paramFloat2 != 0.0F) || (paramFloat4 != 0.0F)) {
      postInvalidateOnAnimation();
    }
  }
  
  private void recoverFocusFromState()
  {
    if ((mPreserveFocusAfterLayout) && (mAdapter != null) && (hasFocus()) && (getDescendantFocusability() != 393216) && ((getDescendantFocusability() != 131072) || (!isFocused())))
    {
      if (!isFocused())
      {
        localObject1 = getFocusedChild();
        if ((IGNORE_DETACHED_FOCUSED_CHILD) && ((((View)localObject1).getParent() == null) || (!((View)localObject1).hasFocus())))
        {
          if (mChildHelper.getChildCount() == 0) {
            requestFocus();
          }
        }
        else if (!mChildHelper.isHidden((View)localObject1)) {
          return;
        }
      }
      Object localObject2 = null;
      Object localObject1 = localObject2;
      if (mState.mFocusedItemId != -1L)
      {
        localObject1 = localObject2;
        if (mAdapter.hasStableIds()) {
          localObject1 = findViewHolderForItemId(mState.mFocusedItemId);
        }
      }
      localObject2 = null;
      if ((localObject1 != null) && (!mChildHelper.isHidden(itemView)) && (itemView.hasFocusable()))
      {
        localObject1 = itemView;
      }
      else
      {
        localObject1 = localObject2;
        if (mChildHelper.getChildCount() > 0) {
          localObject1 = findNextViewToFocus();
        }
      }
      if (localObject1 != null)
      {
        localObject2 = localObject1;
        if (mState.mFocusedSubChildId != -1L)
        {
          View localView = ((View)localObject1).findViewById(mState.mFocusedSubChildId);
          localObject2 = localObject1;
          if (localView != null)
          {
            localObject2 = localObject1;
            if (localView.isFocusable()) {
              localObject2 = localView;
            }
          }
        }
        localObject2.requestFocus();
      }
      return;
    }
  }
  
  private void releaseGlows()
  {
    int i = 0;
    if (mLeftGlow != null)
    {
      mLeftGlow.onRelease();
      i = 1;
    }
    if (mTopGlow != null)
    {
      mTopGlow.onRelease();
      i = 1;
    }
    if (mRightGlow != null)
    {
      mRightGlow.onRelease();
      i = 1;
    }
    if (mBottomGlow != null)
    {
      mBottomGlow.onRelease();
      i = 1;
    }
    if (i != 0) {
      postInvalidateOnAnimation();
    }
  }
  
  private void resetFocusInfo()
  {
    mState.mFocusedItemId = -1L;
    mState.mFocusedItemPosition = -1;
    mState.mFocusedSubChildId = -1;
  }
  
  private void resetTouch()
  {
    if (mVelocityTracker != null) {
      mVelocityTracker.clear();
    }
    stopNestedScroll();
    releaseGlows();
  }
  
  private void saveFocusInfo()
  {
    State localState = null;
    Object localObject = localState;
    if (mPreserveFocusAfterLayout)
    {
      localObject = localState;
      if (hasFocus())
      {
        localObject = localState;
        if (mAdapter != null) {
          localObject = getFocusedChild();
        }
      }
    }
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = findContainingViewHolder((View)localObject);
    }
    if (localObject == null)
    {
      resetFocusInfo();
    }
    else
    {
      localState = mState;
      long l;
      if (mAdapter.hasStableIds()) {
        l = ((ViewHolder)localObject).getItemId();
      } else {
        l = -1L;
      }
      mFocusedItemId = l;
      localState = mState;
      int i;
      if (mDataSetHasChangedAfterLayout) {
        i = -1;
      } else if (((ViewHolder)localObject).isRemoved()) {
        i = mOldPosition;
      } else {
        i = ((ViewHolder)localObject).getAdapterPosition();
      }
      mFocusedItemPosition = i;
      mState.mFocusedSubChildId = getDeepestFocusedViewWithId(itemView);
    }
  }
  
  private void setAdapterInternal(Adapter paramAdapter, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mAdapter != null)
    {
      mAdapter.unregisterAdapterDataObserver(mObserver);
      mAdapter.onDetachedFromRecyclerView(this);
    }
    if ((!paramBoolean1) || (paramBoolean2)) {
      removeAndRecycleViews();
    }
    mAdapterHelper.reset();
    Adapter localAdapter = mAdapter;
    mAdapter = paramAdapter;
    if (paramAdapter != null)
    {
      paramAdapter.registerAdapterDataObserver(mObserver);
      paramAdapter.onAttachedToRecyclerView(this);
    }
    if (mLayout != null) {
      mLayout.onAdapterChanged(localAdapter, mAdapter);
    }
    mRecycler.onAdapterChanged(localAdapter, mAdapter, paramBoolean1);
    mState.mStructureChanged = true;
    markKnownViewsInvalid();
  }
  
  private void stopScrollersInternal()
  {
    mViewFlinger.stop();
    if (mLayout != null) {
      mLayout.stopSmoothScroller();
    }
  }
  
  void absorbGlows(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 0)
    {
      ensureLeftGlow();
      mLeftGlow.onAbsorb(-paramInt1);
    }
    else if (paramInt1 > 0)
    {
      ensureRightGlow();
      mRightGlow.onAbsorb(paramInt1);
    }
    if (paramInt2 < 0)
    {
      ensureTopGlow();
      mTopGlow.onAbsorb(-paramInt2);
    }
    else if (paramInt2 > 0)
    {
      ensureBottomGlow();
      mBottomGlow.onAbsorb(paramInt2);
    }
    if ((paramInt1 != 0) || (paramInt2 != 0)) {
      postInvalidateOnAnimation();
    }
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    if ((mLayout == null) || (!mLayout.onAddFocusables(this, paramArrayList, paramInt1, paramInt2))) {
      super.addFocusables(paramArrayList, paramInt1, paramInt2);
    }
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration)
  {
    addItemDecoration(paramItemDecoration, -1);
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration, int paramInt)
  {
    if (mLayout != null) {
      mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
    }
    if (mItemDecorations.isEmpty()) {
      setWillNotDraw(false);
    }
    if (paramInt < 0) {
      mItemDecorations.add(paramItemDecoration);
    } else {
      mItemDecorations.add(paramInt, paramItemDecoration);
    }
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener)
  {
    if (mOnChildAttachStateListeners == null) {
      mOnChildAttachStateListeners = new ArrayList();
    }
    mOnChildAttachStateListeners.add(paramOnChildAttachStateChangeListener);
  }
  
  public void addOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener)
  {
    mOnItemTouchListeners.add(paramOnItemTouchListener);
  }
  
  public void addOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    if (mScrollListeners == null) {
      mScrollListeners = new ArrayList();
    }
    mScrollListeners.add(paramOnScrollListener);
  }
  
  void animateAppearance(ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2)
  {
    paramViewHolder.setIsRecyclable(false);
    if (mItemAnimator.animateAppearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2)) {
      postAnimationRunner();
    }
  }
  
  void animateDisappearance(ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2)
  {
    addAnimatingView(paramViewHolder);
    paramViewHolder.setIsRecyclable(false);
    if (mItemAnimator.animateDisappearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2)) {
      postAnimationRunner();
    }
  }
  
  void assertInLayoutOrScroll(String paramString)
  {
    if (!isComputingLayout())
    {
      if (paramString == null) {
        throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling");
      }
      throw new IllegalStateException(paramString);
    }
  }
  
  void assertNotInLayoutOrScroll(String paramString)
  {
    if (isComputingLayout())
    {
      if (paramString == null) {
        throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling");
      }
      throw new IllegalStateException(paramString);
    }
    if (mDispatchScrollCounter > 0) {
      Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks might be run during a measure & layout pass where you cannot change the RecyclerView data. Any method call that might change the structure of the RecyclerView or the adapter contents should be postponed to the next frame.", new IllegalStateException(""));
    }
  }
  
  boolean canReuseUpdatedViewHolder(ViewHolder paramViewHolder)
  {
    boolean bool;
    if ((mItemAnimator != null) && (!mItemAnimator.canReuseUpdatedViewHolder(paramViewHolder, paramViewHolder.getUnmodifiedPayloads()))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    boolean bool;
    if (((paramLayoutParams instanceof LayoutParams)) && (mLayout.checkLayoutParams((LayoutParams)paramLayoutParams))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void clearOldPositions()
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      if (!localViewHolder.shouldIgnore()) {
        localViewHolder.clearOldPosition();
      }
    }
    mRecycler.clearOldPositions();
  }
  
  public void clearOnChildAttachStateChangeListeners()
  {
    if (mOnChildAttachStateListeners != null) {
      mOnChildAttachStateListeners.clear();
    }
  }
  
  public void clearOnScrollListeners()
  {
    if (mScrollListeners != null) {
      mScrollListeners.clear();
    }
  }
  
  public int computeHorizontalScrollExtent()
  {
    LayoutManager localLayoutManager = mLayout;
    int i = 0;
    if (localLayoutManager == null) {
      return 0;
    }
    if (mLayout.canScrollHorizontally()) {
      i = mLayout.computeHorizontalScrollExtent(mState);
    }
    return i;
  }
  
  public int computeHorizontalScrollOffset()
  {
    LayoutManager localLayoutManager = mLayout;
    int i = 0;
    if (localLayoutManager == null) {
      return 0;
    }
    if (mLayout.canScrollHorizontally()) {
      i = mLayout.computeHorizontalScrollOffset(mState);
    }
    return i;
  }
  
  public int computeHorizontalScrollRange()
  {
    LayoutManager localLayoutManager = mLayout;
    int i = 0;
    if (localLayoutManager == null) {
      return 0;
    }
    if (mLayout.canScrollHorizontally()) {
      i = mLayout.computeHorizontalScrollRange(mState);
    }
    return i;
  }
  
  public int computeVerticalScrollExtent()
  {
    LayoutManager localLayoutManager = mLayout;
    int i = 0;
    if (localLayoutManager == null) {
      return 0;
    }
    if (mLayout.canScrollVertically()) {
      i = mLayout.computeVerticalScrollExtent(mState);
    }
    return i;
  }
  
  public int computeVerticalScrollOffset()
  {
    LayoutManager localLayoutManager = mLayout;
    int i = 0;
    if (localLayoutManager == null) {
      return 0;
    }
    if (mLayout.canScrollVertically()) {
      i = mLayout.computeVerticalScrollOffset(mState);
    }
    return i;
  }
  
  public int computeVerticalScrollRange()
  {
    LayoutManager localLayoutManager = mLayout;
    int i = 0;
    if (localLayoutManager == null) {
      return 0;
    }
    if (mLayout.canScrollVertically()) {
      i = mLayout.computeVerticalScrollRange(mState);
    }
    return i;
  }
  
  void considerReleasingGlowsOnScroll(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = i;
    if (mLeftGlow != null)
    {
      j = i;
      if (!mLeftGlow.isFinished())
      {
        j = i;
        if (paramInt1 > 0)
        {
          mLeftGlow.onRelease();
          j = 1;
        }
      }
    }
    i = j;
    if (mRightGlow != null)
    {
      i = j;
      if (!mRightGlow.isFinished())
      {
        i = j;
        if (paramInt1 < 0)
        {
          mRightGlow.onRelease();
          i = 1;
        }
      }
    }
    paramInt1 = i;
    if (mTopGlow != null)
    {
      paramInt1 = i;
      if (!mTopGlow.isFinished())
      {
        paramInt1 = i;
        if (paramInt2 > 0)
        {
          mTopGlow.onRelease();
          paramInt1 = 1;
        }
      }
    }
    j = paramInt1;
    if (mBottomGlow != null)
    {
      j = paramInt1;
      if (!mBottomGlow.isFinished())
      {
        j = paramInt1;
        if (paramInt2 < 0)
        {
          mBottomGlow.onRelease();
          j = 1;
        }
      }
    }
    if (j != 0) {
      postInvalidateOnAnimation();
    }
  }
  
  void consumePendingUpdateOperations()
  {
    if ((mFirstLayoutComplete) && (!mDataSetHasChangedAfterLayout))
    {
      if (!mAdapterHelper.hasPendingUpdates()) {
        return;
      }
      if ((mAdapterHelper.hasAnyUpdateTypes(4)) && (!mAdapterHelper.hasAnyUpdateTypes(11)))
      {
        Trace.beginSection("RV PartialInvalidate");
        eatRequestLayout();
        onEnterLayoutOrScroll();
        mAdapterHelper.preProcess();
        if (!mLayoutRequestEaten) {
          if (hasUpdatedView()) {
            dispatchLayout();
          } else {
            mAdapterHelper.consumePostponedUpdates();
          }
        }
        resumeRequestLayout(true);
        onExitLayoutOrScroll();
        Trace.endSection();
      }
      else if (mAdapterHelper.hasPendingUpdates())
      {
        Trace.beginSection("RV FullInvalidate");
        dispatchLayout();
        Trace.endSection();
      }
      return;
    }
    Trace.beginSection("RV FullInvalidate");
    dispatchLayout();
    Trace.endSection();
  }
  
  void defaultOnMeasure(int paramInt1, int paramInt2)
  {
    paramInt1 = LayoutManager.chooseSize(paramInt1, getPaddingLeft() + getPaddingRight(), getMinimumWidth());
    setMeasuredDimension(paramInt1, LayoutManager.chooseSize(paramInt2, getPaddingTop() + getPaddingBottom(), getMinimumHeight()));
  }
  
  void dispatchChildAttached(View paramView)
  {
    ViewHolder localViewHolder = getChildViewHolderInt(paramView);
    onChildAttachedToWindow(paramView);
    if ((mAdapter != null) && (localViewHolder != null)) {
      mAdapter.onViewAttachedToWindow(localViewHolder);
    }
    if (mOnChildAttachStateListeners != null) {
      for (int i = mOnChildAttachStateListeners.size() - 1; i >= 0; i--) {
        ((OnChildAttachStateChangeListener)mOnChildAttachStateListeners.get(i)).onChildViewAttachedToWindow(paramView);
      }
    }
  }
  
  void dispatchChildDetached(View paramView)
  {
    ViewHolder localViewHolder = getChildViewHolderInt(paramView);
    onChildDetachedFromWindow(paramView);
    if ((mAdapter != null) && (localViewHolder != null)) {
      mAdapter.onViewDetachedFromWindow(localViewHolder);
    }
    if (mOnChildAttachStateListeners != null) {
      for (int i = mOnChildAttachStateListeners.size() - 1; i >= 0; i--) {
        ((OnChildAttachStateChangeListener)mOnChildAttachStateListeners.get(i)).onChildViewDetachedFromWindow(paramView);
      }
    }
  }
  
  void dispatchLayout()
  {
    if (mAdapter == null)
    {
      Log.e("RecyclerView", "No adapter attached; skipping layout");
      return;
    }
    if (mLayout == null)
    {
      Log.e("RecyclerView", "No layout manager attached; skipping layout");
      return;
    }
    mState.mIsMeasuring = false;
    if (mState.mLayoutStep == 1)
    {
      dispatchLayoutStep1();
      mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    }
    else if ((!mAdapterHelper.hasUpdates()) && (mLayout.getWidth() == getWidth()) && (mLayout.getHeight() == getHeight()))
    {
      mLayout.setExactMeasureSpecsFrom(this);
    }
    else
    {
      mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    }
    dispatchLayoutStep3();
  }
  
  void dispatchOnScrollStateChanged(int paramInt)
  {
    if (mLayout != null) {
      mLayout.onScrollStateChanged(paramInt);
    }
    onScrollStateChanged(paramInt);
    if (mScrollListener != null) {
      mScrollListener.onScrollStateChanged(this, paramInt);
    }
    if (mScrollListeners != null) {
      for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
        ((OnScrollListener)mScrollListeners.get(i)).onScrollStateChanged(this, paramInt);
      }
    }
  }
  
  void dispatchOnScrolled(int paramInt1, int paramInt2)
  {
    mDispatchScrollCounter += 1;
    int i = getScrollX();
    int j = getScrollY();
    onScrollChanged(i, j, i, j);
    onScrolled(paramInt1, paramInt2);
    if (mScrollListener != null) {
      mScrollListener.onScrolled(this, paramInt1, paramInt2);
    }
    if (mScrollListeners != null) {
      for (i = mScrollListeners.size() - 1; i >= 0; i--) {
        ((OnScrollListener)mScrollListeners.get(i)).onScrolled(this, paramInt1, paramInt2);
      }
    }
    mDispatchScrollCounter -= 1;
  }
  
  void dispatchPendingImportantForAccessibilityChanges()
  {
    for (int i = mPendingAccessibilityImportanceChange.size() - 1; i >= 0; i--)
    {
      ViewHolder localViewHolder = (ViewHolder)mPendingAccessibilityImportanceChange.get(i);
      if ((itemView.getParent() == this) && (!localViewHolder.shouldIgnore()))
      {
        int j = mPendingAccessibilityState;
        if (j != -1)
        {
          itemView.setImportantForAccessibility(j);
          mPendingAccessibilityState = -1;
        }
      }
    }
    mPendingAccessibilityImportanceChange.clear();
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchThawSelfOnly(paramSparseArray);
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchFreezeSelfOnly(paramSparseArray);
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    int i = mItemDecorations.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      ((ItemDecoration)mItemDecorations.get(k)).onDrawOver(paramCanvas, this, mState);
    }
    k = 0;
    i = k;
    if (mLeftGlow != null)
    {
      i = k;
      if (!mLeftGlow.isFinished())
      {
        i = paramCanvas.save();
        if (mClipToPadding) {
          k = getPaddingBottom();
        } else {
          k = 0;
        }
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-getHeight() + k, 0.0F);
        if ((mLeftGlow != null) && (mLeftGlow.draw(paramCanvas))) {
          k = 1;
        } else {
          k = 0;
        }
        paramCanvas.restoreToCount(i);
        i = k;
      }
    }
    k = i;
    int m;
    if (mTopGlow != null)
    {
      k = i;
      if (!mTopGlow.isFinished())
      {
        m = paramCanvas.save();
        if (mClipToPadding) {
          paramCanvas.translate(getPaddingLeft(), getPaddingTop());
        }
        if ((mTopGlow != null) && (mTopGlow.draw(paramCanvas))) {
          k = 1;
        } else {
          k = 0;
        }
        k = i | k;
        paramCanvas.restoreToCount(m);
      }
    }
    i = k;
    if (mRightGlow != null)
    {
      i = k;
      if (!mRightGlow.isFinished())
      {
        m = paramCanvas.save();
        int n = getWidth();
        if (mClipToPadding) {
          i = getPaddingTop();
        } else {
          i = 0;
        }
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-i, -n);
        if ((mRightGlow != null) && (mRightGlow.draw(paramCanvas))) {
          i = 1;
        } else {
          i = 0;
        }
        i = k | i;
        paramCanvas.restoreToCount(m);
      }
    }
    k = i;
    if (mBottomGlow != null)
    {
      k = i;
      if (!mBottomGlow.isFinished())
      {
        m = paramCanvas.save();
        paramCanvas.rotate(180.0F);
        if (mClipToPadding) {
          paramCanvas.translate(-getWidth() + getPaddingRight(), -getHeight() + getPaddingBottom());
        } else {
          paramCanvas.translate(-getWidth(), -getHeight());
        }
        k = j;
        if (mBottomGlow != null)
        {
          k = j;
          if (mBottomGlow.draw(paramCanvas)) {
            k = 1;
          }
        }
        k = i | k;
        paramCanvas.restoreToCount(m);
      }
    }
    i = k;
    if (k == 0)
    {
      i = k;
      if (mItemAnimator != null)
      {
        i = k;
        if (mItemDecorations.size() > 0)
        {
          i = k;
          if (mItemAnimator.isRunning()) {
            i = 1;
          }
        }
      }
    }
    if (i != 0) {
      postInvalidateOnAnimation();
    }
  }
  
  public boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  void eatRequestLayout()
  {
    mEatRequestLayout += 1;
    if ((mEatRequestLayout == 1) && (!mLayoutFrozen)) {
      mLayoutRequestEaten = false;
    }
  }
  
  void ensureBottomGlow()
  {
    if (mBottomGlow != null) {
      return;
    }
    mBottomGlow = new EdgeEffect(getContext());
    if (mClipToPadding) {
      mBottomGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    } else {
      mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    }
  }
  
  void ensureLeftGlow()
  {
    if (mLeftGlow != null) {
      return;
    }
    mLeftGlow = new EdgeEffect(getContext());
    if (mClipToPadding) {
      mLeftGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    } else {
      mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    }
  }
  
  void ensureRightGlow()
  {
    if (mRightGlow != null) {
      return;
    }
    mRightGlow = new EdgeEffect(getContext());
    if (mClipToPadding) {
      mRightGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    } else {
      mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    }
  }
  
  void ensureTopGlow()
  {
    if (mTopGlow != null) {
      return;
    }
    mTopGlow = new EdgeEffect(getContext());
    if (mClipToPadding) {
      mTopGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    } else {
      mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    }
  }
  
  public View findChildViewUnder(float paramFloat1, float paramFloat2)
  {
    for (int i = mChildHelper.getChildCount() - 1; i >= 0; i--)
    {
      View localView = mChildHelper.getChildAt(i);
      float f1 = localView.getTranslationX();
      float f2 = localView.getTranslationY();
      if ((paramFloat1 >= localView.getLeft() + f1) && (paramFloat1 <= localView.getRight() + f1) && (paramFloat2 >= localView.getTop() + f2) && (paramFloat2 <= localView.getBottom() + f2)) {
        return localView;
      }
    }
    return null;
  }
  
  public View findContainingItemView(View paramView)
  {
    for (ViewParent localViewParent = paramView.getParent(); (localViewParent != null) && (localViewParent != this) && ((localViewParent instanceof View)); localViewParent = paramView.getParent()) {
      paramView = (View)localViewParent;
    }
    if (localViewParent != this) {
      paramView = null;
    }
    return paramView;
  }
  
  public ViewHolder findContainingViewHolder(View paramView)
  {
    paramView = findContainingItemView(paramView);
    if (paramView == null) {
      paramView = null;
    } else {
      paramView = getChildViewHolder(paramView);
    }
    return paramView;
  }
  
  public ViewHolder findViewHolderForAdapterPosition(int paramInt)
  {
    if (mDataSetHasChangedAfterLayout) {
      return null;
    }
    int i = mChildHelper.getUnfilteredChildCount();
    Object localObject1 = null;
    int j = 0;
    while (j < i)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      Object localObject2 = localObject1;
      if (localViewHolder != null)
      {
        localObject2 = localObject1;
        if (!localViewHolder.isRemoved())
        {
          localObject2 = localObject1;
          if (getAdapterPositionFor(localViewHolder) == paramInt) {
            if (mChildHelper.isHidden(itemView)) {
              localObject2 = localViewHolder;
            } else {
              return localViewHolder;
            }
          }
        }
      }
      j++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public ViewHolder findViewHolderForItemId(long paramLong)
  {
    if ((mAdapter != null) && (mAdapter.hasStableIds()))
    {
      int i = mChildHelper.getUnfilteredChildCount();
      Object localObject1 = null;
      int j = 0;
      while (j < i)
      {
        ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
        Object localObject2 = localObject1;
        if (localViewHolder != null)
        {
          localObject2 = localObject1;
          if (!localViewHolder.isRemoved())
          {
            localObject2 = localObject1;
            if (localViewHolder.getItemId() == paramLong) {
              if (mChildHelper.isHidden(itemView)) {
                localObject2 = localViewHolder;
              } else {
                return localViewHolder;
              }
            }
          }
        }
        j++;
        localObject1 = localObject2;
      }
      return localObject1;
    }
    return null;
  }
  
  public ViewHolder findViewHolderForLayoutPosition(int paramInt)
  {
    return findViewHolderForPosition(paramInt, false);
  }
  
  @Deprecated
  public ViewHolder findViewHolderForPosition(int paramInt)
  {
    return findViewHolderForPosition(paramInt, false);
  }
  
  ViewHolder findViewHolderForPosition(int paramInt, boolean paramBoolean)
  {
    int i = mChildHelper.getUnfilteredChildCount();
    Object localObject1 = null;
    int j = 0;
    while (j < i)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      Object localObject2 = localObject1;
      if (localViewHolder != null)
      {
        localObject2 = localObject1;
        if (!localViewHolder.isRemoved())
        {
          if (paramBoolean)
          {
            if (mPosition != paramInt)
            {
              localObject2 = localObject1;
              break label116;
            }
          }
          else if (localViewHolder.getLayoutPosition() != paramInt)
          {
            localObject2 = localObject1;
            break label116;
          }
          if (mChildHelper.isHidden(itemView)) {
            localObject2 = localViewHolder;
          } else {
            return localViewHolder;
          }
        }
      }
      label116:
      j++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public boolean fling(int paramInt1, int paramInt2)
  {
    if (mLayout == null)
    {
      Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return false;
    }
    if (mLayoutFrozen) {
      return false;
    }
    boolean bool1 = mLayout.canScrollHorizontally();
    boolean bool2 = mLayout.canScrollVertically();
    int i;
    if (bool1)
    {
      i = paramInt1;
      if (Math.abs(paramInt1) >= mMinFlingVelocity) {}
    }
    else
    {
      i = 0;
    }
    if (bool2)
    {
      paramInt1 = paramInt2;
      if (Math.abs(paramInt2) >= mMinFlingVelocity) {}
    }
    else
    {
      paramInt1 = 0;
    }
    if ((i == 0) && (paramInt1 == 0)) {
      return false;
    }
    if (!dispatchNestedPreFling(i, paramInt1))
    {
      if ((!bool1) && (!bool2)) {
        bool1 = false;
      } else {
        bool1 = true;
      }
      dispatchNestedFling(i, paramInt1, bool1);
      if ((mOnFlingListener != null) && (mOnFlingListener.onFling(i, paramInt1))) {
        return true;
      }
      if (bool1)
      {
        paramInt2 = Math.max(-mMaxFlingVelocity, Math.min(i, mMaxFlingVelocity));
        paramInt1 = Math.max(-mMaxFlingVelocity, Math.min(paramInt1, mMaxFlingVelocity));
        mViewFlinger.fling(paramInt2, paramInt1);
        return true;
      }
    }
    return false;
  }
  
  public View focusSearch(View paramView, int paramInt)
  {
    Object localObject = mLayout.onInterceptFocusSearch(paramView, paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = mAdapter;
    int i = 1;
    int j;
    if ((localObject != null) && (mLayout != null) && (!isComputingLayout()) && (!mLayoutFrozen)) {
      j = 1;
    } else {
      j = 0;
    }
    localObject = FocusFinder.getInstance();
    int m;
    if ((j != 0) && ((paramInt == 2) || (paramInt == 1)))
    {
      int k = 0;
      j = paramInt;
      if (mLayout.canScrollVertically())
      {
        if (paramInt == 2) {
          m = 130;
        } else {
          m = 33;
        }
        if (((FocusFinder)localObject).findNextFocus(this, paramView, m) == null) {
          j = 1;
        } else {
          j = 0;
        }
        n = j;
        k = n;
        j = paramInt;
        if (FORCE_ABS_FOCUS_SEARCH_DIRECTION)
        {
          j = m;
          k = n;
        }
      }
      int n = k;
      m = j;
      if (k == 0)
      {
        n = k;
        m = j;
        if (mLayout.canScrollHorizontally())
        {
          if (mLayout.getLayoutDirection() == 1) {
            paramInt = 1;
          } else {
            paramInt = 0;
          }
          if (j == 2) {
            m = 1;
          } else {
            m = 0;
          }
          if ((m ^ paramInt) != 0) {
            paramInt = 66;
          } else {
            paramInt = 17;
          }
          if (((FocusFinder)localObject).findNextFocus(this, paramView, paramInt) == null) {
            m = i;
          } else {
            m = 0;
          }
          k = m;
          n = k;
          m = j;
          if (FORCE_ABS_FOCUS_SEARCH_DIRECTION)
          {
            m = paramInt;
            n = k;
          }
        }
      }
      if (n != 0)
      {
        consumePendingUpdateOperations();
        if (findContainingItemView(paramView) == null) {
          return null;
        }
        eatRequestLayout();
        mLayout.onFocusSearchFailed(paramView, m, mRecycler, mState);
        resumeRequestLayout(false);
      }
      localObject = ((FocusFinder)localObject).findNextFocus(this, paramView, m);
    }
    else
    {
      View localView = ((FocusFinder)localObject).findNextFocus(this, paramView, paramInt);
      localObject = localView;
      m = paramInt;
      if (localView == null)
      {
        localObject = localView;
        m = paramInt;
        if (j != 0)
        {
          consumePendingUpdateOperations();
          if (findContainingItemView(paramView) == null) {
            return null;
          }
          eatRequestLayout();
          localObject = mLayout.onFocusSearchFailed(paramView, paramInt, mRecycler, mState);
          resumeRequestLayout(false);
          m = paramInt;
        }
      }
    }
    if (!isPreferredNextFocus(paramView, (View)localObject, m)) {
      localObject = super.focusSearch(paramView, m);
    }
    return localObject;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    if (mLayout != null) {
      return mLayout.generateDefaultLayoutParams();
    }
    throw new IllegalStateException("RecyclerView has no LayoutManager");
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    if (mLayout != null) {
      return mLayout.generateLayoutParams(getContext(), paramAttributeSet);
    }
    throw new IllegalStateException("RecyclerView has no LayoutManager");
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (mLayout != null) {
      return mLayout.generateLayoutParams(paramLayoutParams);
    }
    throw new IllegalStateException("RecyclerView has no LayoutManager");
  }
  
  public Adapter getAdapter()
  {
    return mAdapter;
  }
  
  int getAdapterPositionFor(ViewHolder paramViewHolder)
  {
    if ((!paramViewHolder.hasAnyOfTheFlags(524)) && (paramViewHolder.isBound())) {
      return mAdapterHelper.applyPendingUpdatesToPosition(mPosition);
    }
    return -1;
  }
  
  public int getBaseline()
  {
    if (mLayout != null) {
      return mLayout.getBaseline();
    }
    return super.getBaseline();
  }
  
  long getChangedHolderKey(ViewHolder paramViewHolder)
  {
    long l;
    if (mAdapter.hasStableIds()) {
      l = paramViewHolder.getItemId();
    } else {
      l = mPosition;
    }
    return l;
  }
  
  public int getChildAdapterPosition(View paramView)
  {
    paramView = getChildViewHolderInt(paramView);
    int i;
    if (paramView != null) {
      i = paramView.getAdapterPosition();
    } else {
      i = -1;
    }
    return i;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (mChildDrawingOrderCallback == null) {
      return super.getChildDrawingOrder(paramInt1, paramInt2);
    }
    return mChildDrawingOrderCallback.onGetChildDrawingOrder(paramInt1, paramInt2);
  }
  
  public long getChildItemId(View paramView)
  {
    Adapter localAdapter = mAdapter;
    long l = -1L;
    if ((localAdapter != null) && (mAdapter.hasStableIds()))
    {
      paramView = getChildViewHolderInt(paramView);
      if (paramView != null) {
        l = paramView.getItemId();
      }
      return l;
    }
    return -1L;
  }
  
  public int getChildLayoutPosition(View paramView)
  {
    paramView = getChildViewHolderInt(paramView);
    int i;
    if (paramView != null) {
      i = paramView.getLayoutPosition();
    } else {
      i = -1;
    }
    return i;
  }
  
  @Deprecated
  public int getChildPosition(View paramView)
  {
    return getChildAdapterPosition(paramView);
  }
  
  public ViewHolder getChildViewHolder(View paramView)
  {
    Object localObject = paramView.getParent();
    if ((localObject != null) && (localObject != this))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("View ");
      ((StringBuilder)localObject).append(paramView);
      ((StringBuilder)localObject).append(" is not a direct child of ");
      ((StringBuilder)localObject).append(this);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    return getChildViewHolderInt(paramView);
  }
  
  public boolean getClipToPadding()
  {
    return mClipToPadding;
  }
  
  public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate()
  {
    return mAccessibilityDelegate;
  }
  
  public void getDecoratedBoundsWithMargins(View paramView, Rect paramRect)
  {
    getDecoratedBoundsWithMarginsInt(paramView, paramRect);
  }
  
  public ItemAnimator getItemAnimator()
  {
    return mItemAnimator;
  }
  
  Rect getItemDecorInsetsForChild(View paramView)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!mInsetsDirty) {
      return mDecorInsets;
    }
    if ((mState.isPreLayout()) && ((localLayoutParams.isItemChanged()) || (localLayoutParams.isViewInvalid()))) {
      return mDecorInsets;
    }
    Rect localRect = mDecorInsets;
    localRect.set(0, 0, 0, 0);
    int i = mItemDecorations.size();
    for (int j = 0; j < i; j++)
    {
      mTempRect.set(0, 0, 0, 0);
      ((ItemDecoration)mItemDecorations.get(j)).getItemOffsets(mTempRect, paramView, this, mState);
      left += mTempRect.left;
      top += mTempRect.top;
      right += mTempRect.right;
      bottom += mTempRect.bottom;
    }
    mInsetsDirty = false;
    return localRect;
  }
  
  public LayoutManager getLayoutManager()
  {
    return mLayout;
  }
  
  public int getMaxFlingVelocity()
  {
    return mMaxFlingVelocity;
  }
  
  public int getMinFlingVelocity()
  {
    return mMinFlingVelocity;
  }
  
  long getNanoTime()
  {
    if (ALLOW_THREAD_GAP_WORK) {
      return System.nanoTime();
    }
    return 0L;
  }
  
  public OnFlingListener getOnFlingListener()
  {
    return mOnFlingListener;
  }
  
  public boolean getPreserveFocusAfterLayout()
  {
    return mPreserveFocusAfterLayout;
  }
  
  public RecycledViewPool getRecycledViewPool()
  {
    return mRecycler.getRecycledViewPool();
  }
  
  public int getScrollState()
  {
    return mScrollState;
  }
  
  public boolean hasFixedSize()
  {
    return mHasFixedSize;
  }
  
  public boolean hasPendingAdapterUpdates()
  {
    boolean bool;
    if ((mFirstLayoutComplete) && (!mDataSetHasChangedAfterLayout) && (!mAdapterHelper.hasPendingUpdates())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  void initAdapterManager()
  {
    mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback()
    {
      void dispatchUpdate(AdapterHelper.UpdateOp paramAnonymousUpdateOp)
      {
        int i = cmd;
        if (i != 4)
        {
          if (i != 8) {
            switch (i)
            {
            default: 
              break;
            case 2: 
              mLayout.onItemsRemoved(RecyclerView.this, positionStart, itemCount);
              break;
            case 1: 
              mLayout.onItemsAdded(RecyclerView.this, positionStart, itemCount);
              break;
            }
          } else {
            mLayout.onItemsMoved(RecyclerView.this, positionStart, itemCount, 1);
          }
        }
        else {
          mLayout.onItemsUpdated(RecyclerView.this, positionStart, itemCount, payload);
        }
      }
      
      public RecyclerView.ViewHolder findViewHolder(int paramAnonymousInt)
      {
        RecyclerView.ViewHolder localViewHolder = findViewHolderForPosition(paramAnonymousInt, true);
        if (localViewHolder == null) {
          return null;
        }
        if (mChildHelper.isHidden(itemView)) {
          return null;
        }
        return localViewHolder;
      }
      
      public void markViewHoldersUpdated(int paramAnonymousInt1, int paramAnonymousInt2, Object paramAnonymousObject)
      {
        viewRangeUpdate(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousObject);
        mItemsChanged = true;
      }
      
      public void offsetPositionsForAdd(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        offsetPositionRecordsForInsert(paramAnonymousInt1, paramAnonymousInt2);
        mItemsAddedOrRemoved = true;
      }
      
      public void offsetPositionsForMove(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        offsetPositionRecordsForMove(paramAnonymousInt1, paramAnonymousInt2);
        mItemsAddedOrRemoved = true;
      }
      
      public void offsetPositionsForRemovingInvisible(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        offsetPositionRecordsForRemove(paramAnonymousInt1, paramAnonymousInt2, true);
        mItemsAddedOrRemoved = true;
        RecyclerView.State localState = mState;
        mDeletedInvisibleItemCountSincePreviousLayout += paramAnonymousInt2;
      }
      
      public void offsetPositionsForRemovingLaidOutOrNewView(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        offsetPositionRecordsForRemove(paramAnonymousInt1, paramAnonymousInt2, false);
        mItemsAddedOrRemoved = true;
      }
      
      public void onDispatchFirstPass(AdapterHelper.UpdateOp paramAnonymousUpdateOp)
      {
        dispatchUpdate(paramAnonymousUpdateOp);
      }
      
      public void onDispatchSecondPass(AdapterHelper.UpdateOp paramAnonymousUpdateOp)
      {
        dispatchUpdate(paramAnonymousUpdateOp);
      }
    });
  }
  
  void invalidateGlows()
  {
    mBottomGlow = null;
    mTopGlow = null;
    mRightGlow = null;
    mLeftGlow = null;
  }
  
  public void invalidateItemDecorations()
  {
    if (mItemDecorations.size() == 0) {
      return;
    }
    if (mLayout != null) {
      mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
    }
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  boolean isAccessibilityEnabled()
  {
    boolean bool;
    if ((mAccessibilityManager != null) && (mAccessibilityManager.isEnabled())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAnimating()
  {
    boolean bool;
    if ((mItemAnimator != null) && (mItemAnimator.isRunning())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAttachedToWindow()
  {
    return mIsAttached;
  }
  
  public boolean isComputingLayout()
  {
    boolean bool;
    if (mLayoutOrScrollCounter > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLayoutFrozen()
  {
    return mLayoutFrozen;
  }
  
  void jumpToPositionForSmoothScroller(int paramInt)
  {
    if (mLayout == null) {
      return;
    }
    mLayout.scrollToPosition(paramInt);
    awakenScrollBars();
  }
  
  void markItemDecorInsetsDirty()
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++) {
      mChildHelper.getUnfilteredChildAt(j).getLayoutParams()).mInsetsDirty = true;
    }
    mRecycler.markItemDecorInsetsDirty();
  }
  
  void markKnownViewsInvalid()
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore())) {
        localViewHolder.addFlags(6);
      }
    }
    markItemDecorInsetsDirty();
    mRecycler.markKnownViewsInvalid();
  }
  
  public void offsetChildrenHorizontal(int paramInt)
  {
    int i = mChildHelper.getChildCount();
    for (int j = 0; j < i; j++) {
      mChildHelper.getChildAt(j).offsetLeftAndRight(paramInt);
    }
  }
  
  public void offsetChildrenVertical(int paramInt)
  {
    int i = mChildHelper.getChildCount();
    for (int j = 0; j < i; j++) {
      mChildHelper.getChildAt(j).offsetTopAndBottom(paramInt);
    }
  }
  
  void offsetPositionRecordsForInsert(int paramInt1, int paramInt2)
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore()) && (mPosition >= paramInt1))
      {
        localViewHolder.offsetPosition(paramInt2, false);
        mState.mStructureChanged = true;
      }
    }
    mRecycler.offsetPositionRecordsForInsert(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForMove(int paramInt1, int paramInt2)
  {
    int i = mChildHelper.getUnfilteredChildCount();
    int j;
    int k;
    int m;
    if (paramInt1 < paramInt2)
    {
      j = paramInt1;
      k = paramInt2;
      m = -1;
    }
    else
    {
      j = paramInt2;
      k = paramInt1;
      m = 1;
    }
    for (int n = 0; n < i; n++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(n));
      if ((localViewHolder != null) && (mPosition >= j) && (mPosition <= k))
      {
        if (mPosition == paramInt1) {
          localViewHolder.offsetPosition(paramInt2 - paramInt1, false);
        } else {
          localViewHolder.offsetPosition(m, false);
        }
        mState.mStructureChanged = true;
      }
    }
    mRecycler.offsetPositionRecordsForMove(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForRemove(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore())) {
        if (mPosition >= paramInt1 + paramInt2)
        {
          localViewHolder.offsetPosition(-paramInt2, paramBoolean);
          mState.mStructureChanged = true;
        }
        else if (mPosition >= paramInt1)
        {
          localViewHolder.flagRemovedAndOffsetPosition(paramInt1 - 1, -paramInt2, paramBoolean);
          mState.mStructureChanged = true;
        }
      }
    }
    mRecycler.offsetPositionRecordsForRemove(paramInt1, paramInt2, paramBoolean);
    requestLayout();
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    mLayoutOrScrollCounter = 0;
    boolean bool = true;
    mIsAttached = true;
    if ((!mFirstLayoutComplete) || (isLayoutRequested())) {
      bool = false;
    }
    mFirstLayoutComplete = bool;
    if (mLayout != null) {
      mLayout.dispatchAttachedToWindow(this);
    }
    mPostedAnimatorRunner = false;
    if (ALLOW_THREAD_GAP_WORK)
    {
      mGapWorker = ((GapWorker)GapWorker.sGapWorker.get());
      if (mGapWorker == null)
      {
        mGapWorker = new GapWorker();
        Display localDisplay = getDisplay();
        float f1 = 60.0F;
        float f2 = f1;
        if (!isInEditMode())
        {
          f2 = f1;
          if (localDisplay != null)
          {
            float f3 = localDisplay.getRefreshRate();
            f2 = f1;
            if (f3 >= 30.0F) {
              f2 = f3;
            }
          }
        }
        mGapWorker.mFrameIntervalNs = ((1.0E9F / f2));
        GapWorker.sGapWorker.set(mGapWorker);
      }
      mGapWorker.add(this);
    }
  }
  
  public void onChildAttachedToWindow(View paramView) {}
  
  public void onChildDetachedFromWindow(View paramView) {}
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mItemAnimator != null) {
      mItemAnimator.endAnimations();
    }
    stopScroll();
    mIsAttached = false;
    if (mLayout != null) {
      mLayout.dispatchDetachedFromWindow(this, mRecycler);
    }
    mPendingAccessibilityImportanceChange.clear();
    removeCallbacks(mItemAnimatorRunner);
    mViewInfoStore.onDetach();
    if (ALLOW_THREAD_GAP_WORK)
    {
      mGapWorker.remove(this);
      mGapWorker = null;
    }
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i = mItemDecorations.size();
    for (int j = 0; j < i; j++) {
      ((ItemDecoration)mItemDecorations.get(j)).onDraw(paramCanvas, this, mState);
    }
  }
  
  void onEnterLayoutOrScroll()
  {
    mLayoutOrScrollCounter += 1;
  }
  
  void onExitLayoutOrScroll()
  {
    mLayoutOrScrollCounter -= 1;
    if (mLayoutOrScrollCounter < 1)
    {
      mLayoutOrScrollCounter = 0;
      dispatchContentChangedIfNecessary();
      dispatchPendingImportantForAccessibilityChanges();
    }
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (mLayout == null) {
      return false;
    }
    if (mLayoutFrozen) {
      return false;
    }
    if (((paramMotionEvent.getSource() & 0x2) != 0) && (paramMotionEvent.getAction() == 8))
    {
      float f1;
      if (mLayout.canScrollVertically()) {
        f1 = -paramMotionEvent.getAxisValue(9);
      } else {
        f1 = 0.0F;
      }
      float f2;
      if (mLayout.canScrollHorizontally()) {
        f2 = paramMotionEvent.getAxisValue(10);
      } else {
        f2 = 0.0F;
      }
      if ((f1 != 0.0F) || (f2 != 0.0F))
      {
        float f3 = getScrollFactor();
        scrollByInternal((int)(f2 * f3), (int)(f1 * f3), paramMotionEvent);
      }
    }
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = mLayoutFrozen;
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    if (dispatchOnItemTouchIntercept(paramMotionEvent))
    {
      cancelTouch();
      return true;
    }
    if (mLayout == null) {
      return false;
    }
    boolean bool3 = mLayout.canScrollHorizontally();
    bool1 = mLayout.canScrollVertically();
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(paramMotionEvent);
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    switch (i)
    {
    case 4: 
    default: 
      break;
    case 6: 
      onPointerUp(paramMotionEvent);
      break;
    case 5: 
      mScrollPointerId = paramMotionEvent.getPointerId(j);
      i = (int)(paramMotionEvent.getX(j) + 0.5F);
      mLastTouchX = i;
      mInitialTouchX = i;
      j = (int)(paramMotionEvent.getY(j) + 0.5F);
      mLastTouchY = j;
      mInitialTouchY = j;
      break;
    case 3: 
      cancelTouch();
      break;
    case 2: 
      i = paramMotionEvent.findPointerIndex(mScrollPointerId);
      if (i < 0)
      {
        paramMotionEvent = new StringBuilder();
        paramMotionEvent.append("Error processing scroll; pointer index for id ");
        paramMotionEvent.append(mScrollPointerId);
        paramMotionEvent.append(" not found. Did any MotionEvents get skipped?");
        Log.e("RecyclerView", paramMotionEvent.toString());
        return false;
      }
      j = (int)(paramMotionEvent.getX(i) + 0.5F);
      i = (int)(paramMotionEvent.getY(i) + 0.5F);
      if (mScrollState != 1)
      {
        int k = j - mInitialTouchX;
        int m = i - mInitialTouchY;
        i = 0;
        j = i;
        if (bool3)
        {
          j = i;
          if (Math.abs(k) > mTouchSlop)
          {
            int n = mInitialTouchX;
            i = mTouchSlop;
            if (k < 0) {
              j = -1;
            } else {
              j = 1;
            }
            mLastTouchX = (n + i * j);
            j = 1;
          }
        }
        i = j;
        if (bool1)
        {
          i = j;
          if (Math.abs(m) > mTouchSlop)
          {
            i = mInitialTouchY;
            k = mTouchSlop;
            if (m < 0) {
              j = -1;
            } else {
              j = 1;
            }
            mLastTouchY = (i + k * j);
            i = 1;
          }
        }
        if (i != 0) {
          setScrollState(1);
        }
      }
      break;
    case 1: 
      mVelocityTracker.clear();
      stopNestedScroll();
      break;
    case 0: 
      if (mIgnoreMotionEventTillDown) {
        mIgnoreMotionEventTillDown = false;
      }
      mScrollPointerId = paramMotionEvent.getPointerId(0);
      j = (int)(paramMotionEvent.getX() + 0.5F);
      mLastTouchX = j;
      mInitialTouchX = j;
      j = (int)(paramMotionEvent.getY() + 0.5F);
      mLastTouchY = j;
      mInitialTouchY = j;
      if (mScrollState == 2)
      {
        getParent().requestDisallowInterceptTouchEvent(true);
        setScrollState(1);
      }
      paramMotionEvent = mNestedOffsets;
      mNestedOffsets[1] = 0;
      paramMotionEvent[0] = 0;
      j = 0;
      if (bool3) {
        j = 0x0 | 0x1;
      }
      i = j;
      if (bool1) {
        i = j | 0x2;
      }
      startNestedScroll(i);
    }
    if (mScrollState == 1) {
      bool2 = true;
    }
    return bool2;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Trace.beginSection("RV OnLayout");
    dispatchLayout();
    Trace.endSection();
    mFirstLayoutComplete = true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (mLayout == null)
    {
      defaultOnMeasure(paramInt1, paramInt2);
      return;
    }
    boolean bool = mLayout.mAutoMeasure;
    int i = 0;
    if (bool)
    {
      int j = View.MeasureSpec.getMode(paramInt1);
      int k = View.MeasureSpec.getMode(paramInt2);
      int m = i;
      if (j == 1073741824)
      {
        m = i;
        if (k == 1073741824) {
          m = 1;
        }
      }
      mLayout.onMeasure(mRecycler, mState, paramInt1, paramInt2);
      if ((m == 0) && (mAdapter != null))
      {
        if (mState.mLayoutStep == 1) {
          dispatchLayoutStep1();
        }
        mLayout.setMeasureSpecs(paramInt1, paramInt2);
        mState.mIsMeasuring = true;
        dispatchLayoutStep2();
        mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
        if (mLayout.shouldMeasureTwice())
        {
          mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
          mState.mIsMeasuring = true;
          dispatchLayoutStep2();
          mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
        }
      }
    }
    else
    {
      if (mHasFixedSize)
      {
        mLayout.onMeasure(mRecycler, mState, paramInt1, paramInt2);
        return;
      }
      if (mAdapterUpdateDuringMeasure)
      {
        eatRequestLayout();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        onExitLayoutOrScroll();
        if (mState.mRunPredictiveAnimations)
        {
          mState.mInPreLayout = true;
        }
        else
        {
          mAdapterHelper.consumeUpdatesInOnePass();
          mState.mInPreLayout = false;
        }
        mAdapterUpdateDuringMeasure = false;
        resumeRequestLayout(false);
      }
      if (mAdapter != null) {
        mState.mItemCount = mAdapter.getItemCount();
      } else {
        mState.mItemCount = 0;
      }
      eatRequestLayout();
      mLayout.onMeasure(mRecycler, mState, paramInt1, paramInt2);
      resumeRequestLayout(false);
      mState.mInPreLayout = false;
    }
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    if (isComputingLayout()) {
      return false;
    }
    return super.onRequestFocusInDescendants(paramInt, paramRect);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    mPendingSavedState = ((SavedState)paramParcelable);
    super.onRestoreInstanceState(mPendingSavedState.getSuperState());
    if ((mLayout != null) && (mPendingSavedState.mLayoutState != null)) {
      mLayout.onRestoreInstanceState(mPendingSavedState.mLayoutState);
    }
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if (mPendingSavedState != null) {
      localSavedState.copyFrom(mPendingSavedState);
    } else if (mLayout != null) {
      mLayoutState = mLayout.onSaveInstanceState();
    } else {
      mLayoutState = null;
    }
    return localSavedState;
  }
  
  public void onScrollStateChanged(int paramInt) {}
  
  public void onScrolled(int paramInt1, int paramInt2) {}
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if ((paramInt1 != paramInt3) || (paramInt2 != paramInt4)) {
      invalidateGlows();
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((!mLayoutFrozen) && (!mIgnoreMotionEventTillDown))
    {
      if (dispatchOnItemTouch(paramMotionEvent))
      {
        cancelTouch();
        return true;
      }
      if (mLayout == null) {
        return false;
      }
      boolean bool1 = mLayout.canScrollHorizontally();
      boolean bool2 = mLayout.canScrollVertically();
      if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
      }
      int i = 0;
      MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
      int j = paramMotionEvent.getActionMasked();
      int k = paramMotionEvent.getActionIndex();
      if (j == 0)
      {
        int[] arrayOfInt = mNestedOffsets;
        mNestedOffsets[1] = 0;
        arrayOfInt[0] = 0;
      }
      localMotionEvent.offsetLocation(mNestedOffsets[0], mNestedOffsets[1]);
      switch (j)
      {
      case 4: 
      default: 
        i = 0;
        break;
      case 6: 
        onPointerUp(paramMotionEvent);
        break;
      case 5: 
        mScrollPointerId = paramMotionEvent.getPointerId(k);
        j = (int)(paramMotionEvent.getX(k) + 0.5F);
        mLastTouchX = j;
        mInitialTouchX = j;
        k = (int)(paramMotionEvent.getY(k) + 0.5F);
        mLastTouchY = k;
        mInitialTouchY = k;
        break;
      case 3: 
        cancelTouch();
        break;
      case 2: 
        i = paramMotionEvent.findPointerIndex(mScrollPointerId);
        if (i < 0)
        {
          paramMotionEvent = new StringBuilder();
          paramMotionEvent.append("Error processing scroll; pointer index for id ");
          paramMotionEvent.append(mScrollPointerId);
          paramMotionEvent.append(" not found. Did any MotionEvents get skipped?");
          Log.e("RecyclerView", paramMotionEvent.toString());
          return false;
        }
        int m = (int)(paramMotionEvent.getX(i) + 0.5F);
        int n = (int)(paramMotionEvent.getY(i) + 0.5F);
        k = mLastTouchX - m;
        i = mLastTouchY - n;
        if (dispatchNestedPreScroll(k, i, mScrollConsumed, mScrollOffset))
        {
          k -= mScrollConsumed[0];
          i -= mScrollConsumed[1];
          localMotionEvent.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
          paramMotionEvent = mNestedOffsets;
          paramMotionEvent[0] += mScrollOffset[0];
          paramMotionEvent = mNestedOffsets;
          paramMotionEvent[1] += mScrollOffset[1];
        }
        int i1 = 0;
        j = k;
        int i2 = i;
        if (mScrollState != 1)
        {
          i2 = 0;
          int i3 = i2;
          j = k;
          if (bool1)
          {
            i3 = i2;
            j = k;
            if (Math.abs(k) > mTouchSlop)
            {
              if (k > 0) {
                j = k - mTouchSlop;
              } else {
                j = k + mTouchSlop;
              }
              i3 = 1;
            }
          }
          k = i3;
          i2 = i;
          if (bool2)
          {
            k = i3;
            i2 = i;
            if (Math.abs(i) > mTouchSlop)
            {
              if (i > 0) {
                i2 = i - mTouchSlop;
              } else {
                i2 = i + mTouchSlop;
              }
              k = 1;
            }
          }
          if (k != 0) {
            setScrollState(1);
          }
        }
        if (mScrollState == 1)
        {
          mLastTouchX = (m - mScrollOffset[0]);
          mLastTouchY = (n - mScrollOffset[1]);
          if (bool1) {
            i = j;
          } else {
            i = 0;
          }
          if (bool2) {
            k = i2;
          } else {
            k = 0;
          }
          if (scrollByInternal(i, k, localMotionEvent)) {
            getParent().requestDisallowInterceptTouchEvent(true);
          }
          if ((mGapWorker != null) && ((j != 0) || (i2 != 0))) {
            mGapWorker.postFromTraversal(this, j, i2);
          }
        }
        i = i1;
        break;
      case 1: 
        mVelocityTracker.addMovement(localMotionEvent);
        i = 1;
        mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
        float f1;
        if (bool1) {
          f1 = -mVelocityTracker.getXVelocity(mScrollPointerId);
        } else {
          f1 = 0.0F;
        }
        float f2;
        if (bool2) {
          f2 = -mVelocityTracker.getYVelocity(mScrollPointerId);
        } else {
          f2 = 0.0F;
        }
        if (((f1 == 0.0F) && (f2 == 0.0F)) || (!fling((int)f1, (int)f2))) {
          setScrollState(0);
        }
        resetTouch();
        break;
      }
      j = 0;
      mScrollPointerId = paramMotionEvent.getPointerId(0);
      i = (int)(paramMotionEvent.getX() + 0.5F);
      mLastTouchX = i;
      mInitialTouchX = i;
      i = (int)(paramMotionEvent.getY() + 0.5F);
      mLastTouchY = i;
      mInitialTouchY = i;
      i = 0;
      if (bool1) {
        i = 0x0 | 0x1;
      }
      k = i;
      if (bool2) {
        k = i | 0x2;
      }
      startNestedScroll(k);
      i = j;
      if (i == 0) {
        mVelocityTracker.addMovement(localMotionEvent);
      }
      localMotionEvent.recycle();
      return true;
    }
    return false;
  }
  
  void postAnimationRunner()
  {
    if ((!mPostedAnimatorRunner) && (mIsAttached))
    {
      postOnAnimation(mItemAnimatorRunner);
      mPostedAnimatorRunner = true;
    }
  }
  
  void recordAnimationInfoIfBouncedHiddenView(ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo)
  {
    paramViewHolder.setFlags(0, 8192);
    if ((mState.mTrackOldChangeHolders) && (paramViewHolder.isUpdated()) && (!paramViewHolder.isRemoved()) && (!paramViewHolder.shouldIgnore()))
    {
      long l = getChangedHolderKey(paramViewHolder);
      mViewInfoStore.addToOldChangeHolders(l, paramViewHolder);
    }
    mViewInfoStore.addToPreLayout(paramViewHolder, paramItemHolderInfo);
  }
  
  void removeAndRecycleViews()
  {
    if (mItemAnimator != null) {
      mItemAnimator.endAnimations();
    }
    if (mLayout != null)
    {
      mLayout.removeAndRecycleAllViews(mRecycler);
      mLayout.removeAndRecycleScrapInt(mRecycler);
    }
    mRecycler.clear();
  }
  
  boolean removeAnimatingView(View paramView)
  {
    eatRequestLayout();
    boolean bool = mChildHelper.removeViewIfHidden(paramView);
    if (bool)
    {
      paramView = getChildViewHolderInt(paramView);
      mRecycler.unscrapView(paramView);
      mRecycler.recycleViewHolderInternal(paramView);
    }
    resumeRequestLayout(bool ^ true);
    return bool;
  }
  
  protected void removeDetachedView(View paramView, boolean paramBoolean)
  {
    ViewHolder localViewHolder = getChildViewHolderInt(paramView);
    if (localViewHolder != null) {
      if (localViewHolder.isTmpDetached())
      {
        localViewHolder.clearTmpDetachFlag();
      }
      else if (!localViewHolder.shouldIgnore())
      {
        paramView = new StringBuilder();
        paramView.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
        paramView.append(localViewHolder);
        throw new IllegalArgumentException(paramView.toString());
      }
    }
    dispatchChildDetached(paramView);
    super.removeDetachedView(paramView, paramBoolean);
  }
  
  public void removeItemDecoration(ItemDecoration paramItemDecoration)
  {
    if (mLayout != null) {
      mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
    }
    mItemDecorations.remove(paramItemDecoration);
    if (mItemDecorations.isEmpty())
    {
      boolean bool;
      if (getOverScrollMode() == 2) {
        bool = true;
      } else {
        bool = false;
      }
      setWillNotDraw(bool);
    }
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener)
  {
    if (mOnChildAttachStateListeners == null) {
      return;
    }
    mOnChildAttachStateListeners.remove(paramOnChildAttachStateChangeListener);
  }
  
  public void removeOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener)
  {
    mOnItemTouchListeners.remove(paramOnItemTouchListener);
    if (mActiveOnItemTouchListener == paramOnItemTouchListener) {
      mActiveOnItemTouchListener = null;
    }
  }
  
  public void removeOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    if (mScrollListeners != null) {
      mScrollListeners.remove(paramOnScrollListener);
    }
  }
  
  void repositionShadowingViews()
  {
    int i = mChildHelper.getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = mChildHelper.getChildAt(j);
      Object localObject = getChildViewHolder(localView);
      if ((localObject != null) && (mShadowingHolder != null))
      {
        localObject = mShadowingHolder.itemView;
        int k = localView.getLeft();
        int m = localView.getTop();
        if ((k != ((View)localObject).getLeft()) || (m != ((View)localObject).getTop())) {
          ((View)localObject).layout(k, m, ((View)localObject).getWidth() + k, ((View)localObject).getHeight() + m);
        }
      }
    }
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    if ((!mLayout.onRequestChildFocus(this, mState, paramView1, paramView2)) && (paramView2 != null))
    {
      mTempRect.set(0, 0, paramView2.getWidth(), paramView2.getHeight());
      Object localObject = paramView2.getLayoutParams();
      if ((localObject instanceof LayoutParams))
      {
        localObject = (LayoutParams)localObject;
        if (!mInsetsDirty)
        {
          localObject = mDecorInsets;
          Rect localRect = mTempRect;
          left -= left;
          localRect = mTempRect;
          right += right;
          localRect = mTempRect;
          top -= top;
          localRect = mTempRect;
          bottom += bottom;
        }
      }
      offsetDescendantRectToMyCoords(paramView2, mTempRect);
      offsetRectIntoDescendantCoords(paramView1, mTempRect);
      requestChildRectangleOnScreen(paramView1, mTempRect, mFirstLayoutComplete ^ true);
    }
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    return mLayout.requestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    int i = mOnItemTouchListeners.size();
    for (int j = 0; j < i; j++) {
      ((OnItemTouchListener)mOnItemTouchListeners.get(j)).onRequestDisallowInterceptTouchEvent(paramBoolean);
    }
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout()
  {
    if ((mEatRequestLayout == 0) && (!mLayoutFrozen)) {
      super.requestLayout();
    } else {
      mLayoutRequestEaten = true;
    }
  }
  
  void resumeRequestLayout(boolean paramBoolean)
  {
    if (mEatRequestLayout < 1) {
      mEatRequestLayout = 1;
    }
    if (!paramBoolean) {
      mLayoutRequestEaten = false;
    }
    if (mEatRequestLayout == 1)
    {
      if ((paramBoolean) && (mLayoutRequestEaten) && (!mLayoutFrozen) && (mLayout != null) && (mAdapter != null)) {
        dispatchLayout();
      }
      if (!mLayoutFrozen) {
        mLayoutRequestEaten = false;
      }
    }
    mEatRequestLayout -= 1;
  }
  
  void saveOldPositions()
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      if (!localViewHolder.shouldIgnore()) {
        localViewHolder.saveOldPosition();
      }
    }
  }
  
  public void scrollBy(int paramInt1, int paramInt2)
  {
    if (mLayout == null)
    {
      Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    }
    if (mLayoutFrozen) {
      return;
    }
    boolean bool1 = mLayout.canScrollHorizontally();
    boolean bool2 = mLayout.canScrollVertically();
    if ((bool1) || (bool2))
    {
      int i = 0;
      if (!bool1) {
        paramInt1 = 0;
      }
      if (bool2) {
        i = paramInt2;
      }
      scrollByInternal(paramInt1, i, null);
    }
  }
  
  boolean scrollByInternal(int paramInt1, int paramInt2, MotionEvent paramMotionEvent)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    consumePendingUpdateOperations();
    Adapter localAdapter = mAdapter;
    boolean bool = false;
    if (localAdapter != null)
    {
      eatRequestLayout();
      onEnterLayoutOrScroll();
      Trace.beginSection("RV Scroll");
      i = j;
      n = i1;
      if (paramInt1 != 0)
      {
        n = mLayout.scrollHorizontallyBy(paramInt1, mRecycler, mState);
        i = paramInt1 - n;
      }
      k = m;
      i2 = i3;
      if (paramInt2 != 0)
      {
        i2 = mLayout.scrollVerticallyBy(paramInt2, mRecycler, mState);
        k = paramInt2 - i2;
      }
      Trace.endSection();
      repositionShadowingViews();
      onExitLayoutOrScroll();
      resumeRequestLayout(false);
    }
    if (!mItemDecorations.isEmpty()) {
      invalidate();
    }
    if (dispatchNestedScroll(n, i2, i, k, mScrollOffset))
    {
      mLastTouchX -= mScrollOffset[0];
      mLastTouchY -= mScrollOffset[1];
      if (paramMotionEvent != null) {
        paramMotionEvent.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
      }
      paramMotionEvent = mNestedOffsets;
      paramMotionEvent[0] += mScrollOffset[0];
      paramMotionEvent = mNestedOffsets;
      paramMotionEvent[1] += mScrollOffset[1];
    }
    else if (getOverScrollMode() != 2)
    {
      if (paramMotionEvent != null) {
        pullGlows(paramMotionEvent.getX(), i, paramMotionEvent.getY(), k);
      }
      considerReleasingGlowsOnScroll(paramInt1, paramInt2);
    }
    if ((n != 0) || (i2 != 0)) {
      dispatchOnScrolled(n, i2);
    }
    if (!awakenScrollBars()) {
      invalidate();
    }
    if ((n == 0) && (i2 == 0)) {
      break label351;
    }
    bool = true;
    label351:
    return bool;
  }
  
  public void scrollTo(int paramInt1, int paramInt2)
  {
    Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
  }
  
  public void scrollToPosition(int paramInt)
  {
    if (mLayoutFrozen) {
      return;
    }
    stopScroll();
    if (mLayout == null)
    {
      Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    }
    mLayout.scrollToPosition(paramInt);
    awakenScrollBars();
  }
  
  public void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent)
  {
    if (shouldDeferAccessibilityEvent(paramAccessibilityEvent)) {
      return;
    }
    super.sendAccessibilityEventUnchecked(paramAccessibilityEvent);
  }
  
  public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate paramRecyclerViewAccessibilityDelegate)
  {
    mAccessibilityDelegate = paramRecyclerViewAccessibilityDelegate;
    setAccessibilityDelegate(mAccessibilityDelegate);
  }
  
  public void setAdapter(Adapter paramAdapter)
  {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, false, true);
    requestLayout();
  }
  
  public void setChildDrawingOrderCallback(ChildDrawingOrderCallback paramChildDrawingOrderCallback)
  {
    if (paramChildDrawingOrderCallback == mChildDrawingOrderCallback) {
      return;
    }
    mChildDrawingOrderCallback = paramChildDrawingOrderCallback;
    boolean bool;
    if (mChildDrawingOrderCallback != null) {
      bool = true;
    } else {
      bool = false;
    }
    setChildrenDrawingOrderEnabled(bool);
  }
  
  @VisibleForTesting
  boolean setChildImportantForAccessibilityInternal(ViewHolder paramViewHolder, int paramInt)
  {
    if (isComputingLayout())
    {
      mPendingAccessibilityState = paramInt;
      mPendingAccessibilityImportanceChange.add(paramViewHolder);
      return false;
    }
    itemView.setImportantForAccessibility(paramInt);
    return true;
  }
  
  public void setClipToPadding(boolean paramBoolean)
  {
    if (paramBoolean != mClipToPadding) {
      invalidateGlows();
    }
    mClipToPadding = paramBoolean;
    super.setClipToPadding(paramBoolean);
    if (mFirstLayoutComplete) {
      requestLayout();
    }
  }
  
  void setDataSetChangedAfterLayout()
  {
    if (mDataSetHasChangedAfterLayout) {
      return;
    }
    mDataSetHasChangedAfterLayout = true;
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      ViewHolder localViewHolder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(j));
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore())) {
        localViewHolder.addFlags(512);
      }
    }
    mRecycler.setAdapterPositionsAsUnknown();
    markKnownViewsInvalid();
  }
  
  public void setHasFixedSize(boolean paramBoolean)
  {
    mHasFixedSize = paramBoolean;
  }
  
  public void setItemAnimator(ItemAnimator paramItemAnimator)
  {
    if (mItemAnimator != null)
    {
      mItemAnimator.endAnimations();
      mItemAnimator.setListener(null);
    }
    mItemAnimator = paramItemAnimator;
    if (mItemAnimator != null) {
      mItemAnimator.setListener(mItemAnimatorListener);
    }
  }
  
  public void setItemViewCacheSize(int paramInt)
  {
    mRecycler.setViewCacheSize(paramInt);
  }
  
  public void setLayoutFrozen(boolean paramBoolean)
  {
    if (paramBoolean != mLayoutFrozen)
    {
      assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
      if (!paramBoolean)
      {
        mLayoutFrozen = false;
        if ((mLayoutRequestEaten) && (mLayout != null) && (mAdapter != null)) {
          requestLayout();
        }
        mLayoutRequestEaten = false;
      }
      else
      {
        long l = SystemClock.uptimeMillis();
        onTouchEvent(MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0));
        mLayoutFrozen = true;
        mIgnoreMotionEventTillDown = true;
        stopScroll();
      }
    }
  }
  
  public void setLayoutManager(LayoutManager paramLayoutManager)
  {
    if (paramLayoutManager == mLayout) {
      return;
    }
    stopScroll();
    if (mLayout != null)
    {
      if (mItemAnimator != null) {
        mItemAnimator.endAnimations();
      }
      mLayout.removeAndRecycleAllViews(mRecycler);
      mLayout.removeAndRecycleScrapInt(mRecycler);
      mRecycler.clear();
      if (mIsAttached) {
        mLayout.dispatchDetachedFromWindow(this, mRecycler);
      }
      mLayout.setRecyclerView(null);
      mLayout = null;
    }
    else
    {
      mRecycler.clear();
    }
    mChildHelper.removeAllViewsUnfiltered();
    mLayout = paramLayoutManager;
    if (paramLayoutManager != null) {
      if (mRecyclerView == null)
      {
        mLayout.setRecyclerView(this);
        if (mIsAttached) {
          mLayout.dispatchAttachedToWindow(this);
        }
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("LayoutManager ");
        localStringBuilder.append(paramLayoutManager);
        localStringBuilder.append(" is already attached to a RecyclerView: ");
        localStringBuilder.append(mRecyclerView);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
    mRecycler.updateViewCacheSize();
    requestLayout();
  }
  
  public void setOnFlingListener(OnFlingListener paramOnFlingListener)
  {
    mOnFlingListener = paramOnFlingListener;
  }
  
  @Deprecated
  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    mScrollListener = paramOnScrollListener;
  }
  
  public void setPreserveFocusAfterLayout(boolean paramBoolean)
  {
    mPreserveFocusAfterLayout = paramBoolean;
  }
  
  public void setRecycledViewPool(RecycledViewPool paramRecycledViewPool)
  {
    mRecycler.setRecycledViewPool(paramRecycledViewPool);
  }
  
  public void setRecyclerListener(RecyclerListener paramRecyclerListener)
  {
    mRecyclerListener = paramRecyclerListener;
  }
  
  void setScrollState(int paramInt)
  {
    if (paramInt == mScrollState) {
      return;
    }
    mScrollState = paramInt;
    if (paramInt != 2) {
      stopScrollersInternal();
    }
    dispatchOnScrollStateChanged(paramInt);
  }
  
  public void setScrollingTouchSlop(int paramInt)
  {
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(getContext());
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setScrollingTouchSlop(): bad argument constant ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("; using default value");
      Log.w("RecyclerView", localStringBuilder.toString());
      break;
    case 1: 
      mTouchSlop = localViewConfiguration.getScaledPagingTouchSlop();
      break;
    }
    mTouchSlop = localViewConfiguration.getScaledTouchSlop();
  }
  
  public void setViewCacheExtension(ViewCacheExtension paramViewCacheExtension)
  {
    mRecycler.setViewCacheExtension(paramViewCacheExtension);
  }
  
  boolean shouldDeferAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (isComputingLayout())
    {
      int i = 0;
      if (paramAccessibilityEvent != null) {
        i = paramAccessibilityEvent.getContentChangeTypes();
      }
      int j = i;
      if (i == 0) {
        j = 0;
      }
      mEatenAccessibilityChangeFlags |= j;
      return true;
    }
    return false;
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2)
  {
    smoothScrollBy(paramInt1, paramInt2, null);
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator)
  {
    if (mLayout == null)
    {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    }
    if (mLayoutFrozen) {
      return;
    }
    if (!mLayout.canScrollHorizontally()) {
      paramInt1 = 0;
    }
    if (!mLayout.canScrollVertically()) {
      paramInt2 = 0;
    }
    if ((paramInt1 != 0) || (paramInt2 != 0)) {
      mViewFlinger.smoothScrollBy(paramInt1, paramInt2, paramInterpolator);
    }
  }
  
  public void smoothScrollToPosition(int paramInt)
  {
    if (mLayoutFrozen) {
      return;
    }
    if (mLayout == null)
    {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    }
    mLayout.smoothScrollToPosition(this, mState, paramInt);
  }
  
  public void stopScroll()
  {
    setScrollState(0);
    stopScrollersInternal();
  }
  
  public void swapAdapter(Adapter paramAdapter, boolean paramBoolean)
  {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, true, paramBoolean);
    setDataSetChangedAfterLayout();
    requestLayout();
  }
  
  void viewRangeUpdate(int paramInt1, int paramInt2, Object paramObject)
  {
    int i = mChildHelper.getUnfilteredChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = mChildHelper.getUnfilteredChildAt(j);
      ViewHolder localViewHolder = getChildViewHolderInt(localView);
      if ((localViewHolder != null) && (!localViewHolder.shouldIgnore()) && (mPosition >= paramInt1) && (mPosition < paramInt1 + paramInt2))
      {
        localViewHolder.addFlags(2);
        localViewHolder.addChangePayload(paramObject);
        getLayoutParamsmInsetsDirty = true;
      }
    }
    mRecycler.viewRangeUpdate(paramInt1, paramInt2);
  }
  
  public static abstract class Adapter<VH extends RecyclerView.ViewHolder>
  {
    private boolean mHasStableIds = false;
    private final RecyclerView.AdapterDataObservable mObservable = new RecyclerView.AdapterDataObservable();
    
    public Adapter() {}
    
    public final void bindViewHolder(VH paramVH, int paramInt)
    {
      mPosition = paramInt;
      if (hasStableIds()) {
        mItemId = getItemId(paramInt);
      }
      paramVH.setFlags(1, 519);
      Trace.beginSection("RV OnBindView");
      onBindViewHolder(paramVH, paramInt, paramVH.getUnmodifiedPayloads());
      paramVH.clearPayload();
      paramVH = itemView.getLayoutParams();
      if ((paramVH instanceof RecyclerView.LayoutParams)) {
        mInsetsDirty = true;
      }
      Trace.endSection();
    }
    
    public final VH createViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      Trace.beginSection("RV CreateView");
      paramViewGroup = onCreateViewHolder(paramViewGroup, paramInt);
      mItemViewType = paramInt;
      Trace.endSection();
      return paramViewGroup;
    }
    
    public abstract int getItemCount();
    
    public long getItemId(int paramInt)
    {
      return -1L;
    }
    
    public int getItemViewType(int paramInt)
    {
      return 0;
    }
    
    public final boolean hasObservers()
    {
      return mObservable.hasObservers();
    }
    
    public final boolean hasStableIds()
    {
      return mHasStableIds;
    }
    
    public final void notifyDataSetChanged()
    {
      mObservable.notifyChanged();
    }
    
    public final void notifyItemChanged(int paramInt)
    {
      mObservable.notifyItemRangeChanged(paramInt, 1);
    }
    
    public final void notifyItemChanged(int paramInt, Object paramObject)
    {
      mObservable.notifyItemRangeChanged(paramInt, 1, paramObject);
    }
    
    public final void notifyItemInserted(int paramInt)
    {
      mObservable.notifyItemRangeInserted(paramInt, 1);
    }
    
    public final void notifyItemMoved(int paramInt1, int paramInt2)
    {
      mObservable.notifyItemMoved(paramInt1, paramInt2);
    }
    
    public final void notifyItemRangeChanged(int paramInt1, int paramInt2)
    {
      mObservable.notifyItemRangeChanged(paramInt1, paramInt2);
    }
    
    public final void notifyItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      mObservable.notifyItemRangeChanged(paramInt1, paramInt2, paramObject);
    }
    
    public final void notifyItemRangeInserted(int paramInt1, int paramInt2)
    {
      mObservable.notifyItemRangeInserted(paramInt1, paramInt2);
    }
    
    public final void notifyItemRangeRemoved(int paramInt1, int paramInt2)
    {
      mObservable.notifyItemRangeRemoved(paramInt1, paramInt2);
    }
    
    public final void notifyItemRemoved(int paramInt)
    {
      mObservable.notifyItemRangeRemoved(paramInt, 1);
    }
    
    public void onAttachedToRecyclerView(RecyclerView paramRecyclerView) {}
    
    public abstract void onBindViewHolder(VH paramVH, int paramInt);
    
    public void onBindViewHolder(VH paramVH, int paramInt, List<Object> paramList)
    {
      onBindViewHolder(paramVH, paramInt);
    }
    
    public abstract VH onCreateViewHolder(ViewGroup paramViewGroup, int paramInt);
    
    public void onDetachedFromRecyclerView(RecyclerView paramRecyclerView) {}
    
    public boolean onFailedToRecycleView(VH paramVH)
    {
      return false;
    }
    
    public void onViewAttachedToWindow(VH paramVH) {}
    
    public void onViewDetachedFromWindow(VH paramVH) {}
    
    public void onViewRecycled(VH paramVH) {}
    
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver paramAdapterDataObserver)
    {
      mObservable.registerObserver(paramAdapterDataObserver);
    }
    
    public void setHasStableIds(boolean paramBoolean)
    {
      if (!hasObservers())
      {
        mHasStableIds = paramBoolean;
        return;
      }
      throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
    }
    
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver paramAdapterDataObserver)
    {
      mObservable.unregisterObserver(paramAdapterDataObserver);
    }
  }
  
  static class AdapterDataObservable
    extends Observable<RecyclerView.AdapterDataObserver>
  {
    AdapterDataObservable() {}
    
    public boolean hasObservers()
    {
      return mObservers.isEmpty() ^ true;
    }
    
    public void notifyChanged()
    {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        ((RecyclerView.AdapterDataObserver)mObservers.get(i)).onChanged();
      }
    }
    
    public void notifyItemMoved(int paramInt1, int paramInt2)
    {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        ((RecyclerView.AdapterDataObserver)mObservers.get(i)).onItemRangeMoved(paramInt1, paramInt2, 1);
      }
    }
    
    public void notifyItemRangeChanged(int paramInt1, int paramInt2)
    {
      notifyItemRangeChanged(paramInt1, paramInt2, null);
    }
    
    public void notifyItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        ((RecyclerView.AdapterDataObserver)mObservers.get(i)).onItemRangeChanged(paramInt1, paramInt2, paramObject);
      }
    }
    
    public void notifyItemRangeInserted(int paramInt1, int paramInt2)
    {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        ((RecyclerView.AdapterDataObserver)mObservers.get(i)).onItemRangeInserted(paramInt1, paramInt2);
      }
    }
    
    public void notifyItemRangeRemoved(int paramInt1, int paramInt2)
    {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        ((RecyclerView.AdapterDataObserver)mObservers.get(i)).onItemRangeRemoved(paramInt1, paramInt2);
      }
    }
  }
  
  public static abstract class AdapterDataObserver
  {
    public AdapterDataObserver() {}
    
    public void onChanged() {}
    
    public void onItemRangeChanged(int paramInt1, int paramInt2) {}
    
    public void onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      onItemRangeChanged(paramInt1, paramInt2);
    }
    
    public void onItemRangeInserted(int paramInt1, int paramInt2) {}
    
    public void onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onItemRangeRemoved(int paramInt1, int paramInt2) {}
  }
  
  public static abstract interface ChildDrawingOrderCallback
  {
    public abstract int onGetChildDrawingOrder(int paramInt1, int paramInt2);
  }
  
  public static abstract class ItemAnimator
  {
    public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    public static final int FLAG_CHANGED = 2;
    public static final int FLAG_INVALIDATED = 4;
    public static final int FLAG_MOVED = 2048;
    public static final int FLAG_REMOVED = 8;
    private long mAddDuration = 120L;
    private long mChangeDuration = 250L;
    private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList();
    private ItemAnimatorListener mListener = null;
    private long mMoveDuration = 250L;
    private long mRemoveDuration = 120L;
    
    public ItemAnimator() {}
    
    static int buildAdapterChangeFlagsForAnimations(RecyclerView.ViewHolder paramViewHolder)
    {
      int i = RecyclerView.ViewHolder.access$1400(paramViewHolder) & 0xE;
      if (paramViewHolder.isInvalid()) {
        return 4;
      }
      int j = i;
      if ((i & 0x4) == 0)
      {
        int k = paramViewHolder.getOldPosition();
        int m = paramViewHolder.getAdapterPosition();
        j = i;
        if (k != -1)
        {
          j = i;
          if (m != -1)
          {
            j = i;
            if (k != m) {
              j = i | 0x800;
            }
          }
        }
      }
      return j;
    }
    
    public abstract boolean animateAppearance(RecyclerView.ViewHolder paramViewHolder, ItemHolderInfo paramItemHolderInfo1, ItemHolderInfo paramItemHolderInfo2);
    
    public abstract boolean animateChange(RecyclerView.ViewHolder paramViewHolder1, RecyclerView.ViewHolder paramViewHolder2, ItemHolderInfo paramItemHolderInfo1, ItemHolderInfo paramItemHolderInfo2);
    
    public abstract boolean animateDisappearance(RecyclerView.ViewHolder paramViewHolder, ItemHolderInfo paramItemHolderInfo1, ItemHolderInfo paramItemHolderInfo2);
    
    public abstract boolean animatePersistence(RecyclerView.ViewHolder paramViewHolder, ItemHolderInfo paramItemHolderInfo1, ItemHolderInfo paramItemHolderInfo2);
    
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder paramViewHolder)
    {
      return true;
    }
    
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder paramViewHolder, List<Object> paramList)
    {
      return canReuseUpdatedViewHolder(paramViewHolder);
    }
    
    public final void dispatchAnimationFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      onAnimationFinished(paramViewHolder);
      if (mListener != null) {
        mListener.onAnimationFinished(paramViewHolder);
      }
    }
    
    public final void dispatchAnimationStarted(RecyclerView.ViewHolder paramViewHolder)
    {
      onAnimationStarted(paramViewHolder);
    }
    
    public final void dispatchAnimationsFinished()
    {
      int i = mFinishedListeners.size();
      for (int j = 0; j < i; j++) {
        ((ItemAnimatorFinishedListener)mFinishedListeners.get(j)).onAnimationsFinished();
      }
      mFinishedListeners.clear();
    }
    
    public abstract void endAnimation(RecyclerView.ViewHolder paramViewHolder);
    
    public abstract void endAnimations();
    
    public long getAddDuration()
    {
      return mAddDuration;
    }
    
    public long getChangeDuration()
    {
      return mChangeDuration;
    }
    
    public long getMoveDuration()
    {
      return mMoveDuration;
    }
    
    public long getRemoveDuration()
    {
      return mRemoveDuration;
    }
    
    public abstract boolean isRunning();
    
    public final boolean isRunning(ItemAnimatorFinishedListener paramItemAnimatorFinishedListener)
    {
      boolean bool = isRunning();
      if (paramItemAnimatorFinishedListener != null) {
        if (!bool) {
          paramItemAnimatorFinishedListener.onAnimationsFinished();
        } else {
          mFinishedListeners.add(paramItemAnimatorFinishedListener);
        }
      }
      return bool;
    }
    
    public ItemHolderInfo obtainHolderInfo()
    {
      return new ItemHolderInfo();
    }
    
    public void onAnimationFinished(RecyclerView.ViewHolder paramViewHolder) {}
    
    public void onAnimationStarted(RecyclerView.ViewHolder paramViewHolder) {}
    
    public ItemHolderInfo recordPostLayoutInformation(RecyclerView.State paramState, RecyclerView.ViewHolder paramViewHolder)
    {
      return obtainHolderInfo().setFrom(paramViewHolder);
    }
    
    public ItemHolderInfo recordPreLayoutInformation(RecyclerView.State paramState, RecyclerView.ViewHolder paramViewHolder, int paramInt, List<Object> paramList)
    {
      return obtainHolderInfo().setFrom(paramViewHolder);
    }
    
    public abstract void runPendingAnimations();
    
    public void setAddDuration(long paramLong)
    {
      mAddDuration = paramLong;
    }
    
    public void setChangeDuration(long paramLong)
    {
      mChangeDuration = paramLong;
    }
    
    void setListener(ItemAnimatorListener paramItemAnimatorListener)
    {
      mListener = paramItemAnimatorListener;
    }
    
    public void setMoveDuration(long paramLong)
    {
      mMoveDuration = paramLong;
    }
    
    public void setRemoveDuration(long paramLong)
    {
      mRemoveDuration = paramLong;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface AdapterChanges {}
    
    public static abstract interface ItemAnimatorFinishedListener
    {
      public abstract void onAnimationsFinished();
    }
    
    static abstract interface ItemAnimatorListener
    {
      public abstract void onAnimationFinished(RecyclerView.ViewHolder paramViewHolder);
    }
    
    public static class ItemHolderInfo
    {
      public int bottom;
      public int changeFlags;
      public int left;
      public int right;
      public int top;
      
      public ItemHolderInfo() {}
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder paramViewHolder)
      {
        return setFrom(paramViewHolder, 0);
      }
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder paramViewHolder, int paramInt)
      {
        paramViewHolder = itemView;
        left = paramViewHolder.getLeft();
        top = paramViewHolder.getTop();
        right = paramViewHolder.getRight();
        bottom = paramViewHolder.getBottom();
        return this;
      }
    }
  }
  
  private class ItemAnimatorRestoreListener
    implements RecyclerView.ItemAnimator.ItemAnimatorListener
  {
    ItemAnimatorRestoreListener() {}
    
    public void onAnimationFinished(RecyclerView.ViewHolder paramViewHolder)
    {
      paramViewHolder.setIsRecyclable(true);
      if ((mShadowedHolder != null) && (mShadowingHolder == null)) {
        mShadowedHolder = null;
      }
      mShadowingHolder = null;
      if ((!RecyclerView.ViewHolder.access$1300(paramViewHolder)) && (!removeAnimatingView(itemView)) && (paramViewHolder.isTmpDetached())) {
        removeDetachedView(itemView, false);
      }
    }
  }
  
  public static abstract class ItemDecoration
  {
    public ItemDecoration() {}
    
    @Deprecated
    public void getItemOffsets(Rect paramRect, int paramInt, RecyclerView paramRecyclerView)
    {
      paramRect.set(0, 0, 0, 0);
    }
    
    public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
      getItemOffsets(paramRect, ((RecyclerView.LayoutParams)paramView.getLayoutParams()).getViewLayoutPosition(), paramRecyclerView);
    }
    
    @Deprecated
    public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView) {}
    
    public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
      onDraw(paramCanvas, paramRecyclerView);
    }
    
    @Deprecated
    public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView) {}
    
    public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
      onDrawOver(paramCanvas, paramRecyclerView);
    }
  }
  
  public static abstract class LayoutManager
  {
    boolean mAutoMeasure = false;
    ChildHelper mChildHelper;
    private int mHeight;
    private int mHeightMode;
    boolean mIsAttachedToWindow = false;
    private boolean mItemPrefetchEnabled = true;
    private boolean mMeasurementCacheEnabled = true;
    int mPrefetchMaxCountObserved;
    boolean mPrefetchMaxObservedInInitialPrefetch;
    RecyclerView mRecyclerView;
    boolean mRequestedSimpleAnimations = false;
    RecyclerView.SmoothScroller mSmoothScroller;
    private int mWidth;
    private int mWidthMode;
    
    public LayoutManager() {}
    
    private void addViewInt(View paramView, int paramInt, boolean paramBoolean)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if ((!paramBoolean) && (!localViewHolder.isRemoved())) {
        mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(localViewHolder);
      } else {
        mRecyclerView.mViewInfoStore.addToDisappearedInLayout(localViewHolder);
      }
      Object localObject = (RecyclerView.LayoutParams)paramView.getLayoutParams();
      if ((!localViewHolder.wasReturnedFromScrap()) && (!localViewHolder.isScrap()))
      {
        if (paramView.getParent() == mRecyclerView)
        {
          int i = mChildHelper.indexOfChild(paramView);
          int j = paramInt;
          if (paramInt == -1) {
            j = mChildHelper.getChildCount();
          }
          if (i != -1)
          {
            if (i != j) {
              mRecyclerView.mLayout.moveView(i, j);
            }
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
            ((StringBuilder)localObject).append(mRecyclerView.indexOfChild(paramView));
            throw new IllegalStateException(((StringBuilder)localObject).toString());
          }
        }
        else
        {
          mChildHelper.addView(paramView, paramInt, false);
          mInsetsDirty = true;
          if ((mSmoothScroller != null) && (mSmoothScroller.isRunning())) {
            mSmoothScroller.onChildAttachedToWindow(paramView);
          }
        }
      }
      else
      {
        if (localViewHolder.isScrap()) {
          localViewHolder.unScrap();
        } else {
          localViewHolder.clearReturnedFromScrapFlag();
        }
        mChildHelper.attachViewToParent(paramView, paramInt, paramView.getLayoutParams(), false);
      }
      if (mPendingInvalidate)
      {
        itemView.invalidate();
        mPendingInvalidate = false;
      }
    }
    
    public static int chooseSize(int paramInt1, int paramInt2, int paramInt3)
    {
      int i = View.MeasureSpec.getMode(paramInt1);
      paramInt1 = View.MeasureSpec.getSize(paramInt1);
      if (i != Integer.MIN_VALUE)
      {
        if (i != 1073741824) {
          return Math.max(paramInt2, paramInt3);
        }
        return paramInt1;
      }
      return Math.min(paramInt1, Math.max(paramInt2, paramInt3));
    }
    
    private void detachViewInternal(int paramInt, View paramView)
    {
      mChildHelper.detachViewFromParent(paramInt);
    }
    
    public static int getChildMeasureSpec(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
    {
      int i = Math.max(0, paramInt1 - paramInt3);
      int j = 0;
      paramInt3 = 0;
      int k = 0;
      paramInt1 = 0;
      if (paramBoolean)
      {
        if (paramInt4 >= 0)
        {
          paramInt3 = paramInt4;
          paramInt1 = 1073741824;
        }
        else if (paramInt4 == -1)
        {
          if (paramInt2 != Integer.MIN_VALUE) {
            if (paramInt2 != 0)
            {
              if (paramInt2 != 1073741824) {
                break label72;
              }
            }
            else
            {
              paramInt3 = 0;
              paramInt1 = 0;
              break label72;
            }
          }
          paramInt3 = i;
          paramInt1 = paramInt2;
        }
        else
        {
          label72:
          paramInt3 = j;
          paramInt1 = k;
          if (paramInt4 == -2)
          {
            paramInt3 = 0;
            paramInt1 = 0;
          }
        }
      }
      else if (paramInt4 >= 0)
      {
        paramInt3 = paramInt4;
        paramInt1 = 1073741824;
      }
      else if (paramInt4 == -1)
      {
        paramInt3 = i;
        paramInt1 = paramInt2;
      }
      else
      {
        paramInt3 = j;
        paramInt1 = k;
        if (paramInt4 == -2)
        {
          paramInt3 = i;
          if ((paramInt2 != Integer.MIN_VALUE) && (paramInt2 != 1073741824)) {
            paramInt1 = 0;
          } else {
            paramInt1 = Integer.MIN_VALUE;
          }
        }
      }
      return View.MeasureSpec.makeMeasureSpec(paramInt3, paramInt1);
    }
    
    @Deprecated
    public static int getChildMeasureSpec(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    {
      int i = Math.max(0, paramInt1 - paramInt2);
      paramInt1 = 0;
      paramInt2 = 0;
      if (paramBoolean)
      {
        if (paramInt3 >= 0)
        {
          paramInt1 = paramInt3;
          paramInt2 = 1073741824;
        }
        else
        {
          paramInt1 = 0;
          paramInt2 = 0;
        }
      }
      else if (paramInt3 >= 0)
      {
        paramInt1 = paramInt3;
        paramInt2 = 1073741824;
      }
      else if (paramInt3 == -1)
      {
        paramInt1 = i;
        paramInt2 = 1073741824;
      }
      else if (paramInt3 == -2)
      {
        paramInt1 = i;
        paramInt2 = Integer.MIN_VALUE;
      }
      return View.MeasureSpec.makeMeasureSpec(paramInt1, paramInt2);
    }
    
    public static Properties getProperties(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
    {
      Properties localProperties = new Properties();
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecyclerView, paramInt1, paramInt2);
      orientation = paramContext.getInt(0, 1);
      spanCount = paramContext.getInt(4, 1);
      reverseLayout = paramContext.getBoolean(3, false);
      stackFromEnd = paramContext.getBoolean(5, false);
      paramContext.recycle();
      return localProperties;
    }
    
    private static boolean isMeasurementUpToDate(int paramInt1, int paramInt2, int paramInt3)
    {
      int i = View.MeasureSpec.getMode(paramInt2);
      paramInt2 = View.MeasureSpec.getSize(paramInt2);
      boolean bool1 = false;
      boolean bool2 = false;
      if ((paramInt3 > 0) && (paramInt1 != paramInt3)) {
        return false;
      }
      if (i != Integer.MIN_VALUE)
      {
        if (i != 0)
        {
          if (i != 1073741824) {
            return false;
          }
          if (paramInt2 == paramInt1) {
            bool2 = true;
          }
          return bool2;
        }
        return true;
      }
      bool2 = bool1;
      if (paramInt2 >= paramInt1) {
        bool2 = true;
      }
      return bool2;
    }
    
    private void onSmoothScrollerStopped(RecyclerView.SmoothScroller paramSmoothScroller)
    {
      if (mSmoothScroller == paramSmoothScroller) {
        mSmoothScroller = null;
      }
    }
    
    private void scrapOrRecycleView(RecyclerView.Recycler paramRecycler, int paramInt, View paramView)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder.shouldIgnore()) {
        return;
      }
      if ((localViewHolder.isInvalid()) && (!localViewHolder.isRemoved()) && (!mRecyclerView.mAdapter.hasStableIds()))
      {
        removeViewAt(paramInt);
        paramRecycler.recycleViewHolderInternal(localViewHolder);
      }
      else
      {
        detachViewAt(paramInt);
        paramRecycler.scrapView(paramView);
        mRecyclerView.mViewInfoStore.onViewDetached(localViewHolder);
      }
    }
    
    public void addDisappearingView(View paramView)
    {
      addDisappearingView(paramView, -1);
    }
    
    public void addDisappearingView(View paramView, int paramInt)
    {
      addViewInt(paramView, paramInt, true);
    }
    
    public void addView(View paramView)
    {
      addView(paramView, -1);
    }
    
    public void addView(View paramView, int paramInt)
    {
      addViewInt(paramView, paramInt, false);
    }
    
    public void assertInLayoutOrScroll(String paramString)
    {
      if (mRecyclerView != null) {
        mRecyclerView.assertInLayoutOrScroll(paramString);
      }
    }
    
    public void assertNotInLayoutOrScroll(String paramString)
    {
      if (mRecyclerView != null) {
        mRecyclerView.assertNotInLayoutOrScroll(paramString);
      }
    }
    
    public void attachView(View paramView)
    {
      attachView(paramView, -1);
    }
    
    public void attachView(View paramView, int paramInt)
    {
      attachView(paramView, paramInt, (RecyclerView.LayoutParams)paramView.getLayoutParams());
    }
    
    public void attachView(View paramView, int paramInt, RecyclerView.LayoutParams paramLayoutParams)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder.isRemoved()) {
        mRecyclerView.mViewInfoStore.addToDisappearedInLayout(localViewHolder);
      } else {
        mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(localViewHolder);
      }
      mChildHelper.attachViewToParent(paramView, paramInt, paramLayoutParams, localViewHolder.isRemoved());
    }
    
    public void calculateItemDecorationsForChild(View paramView, Rect paramRect)
    {
      if (mRecyclerView == null)
      {
        paramRect.set(0, 0, 0, 0);
        return;
      }
      paramRect.set(mRecyclerView.getItemDecorInsetsForChild(paramView));
    }
    
    public boolean canScrollHorizontally()
    {
      return false;
    }
    
    public boolean canScrollVertically()
    {
      return false;
    }
    
    public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams)
    {
      boolean bool;
      if (paramLayoutParams != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void collectAdjacentPrefetchPositions(int paramInt1, int paramInt2, RecyclerView.State paramState, LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {}
    
    public void collectInitialPrefetchPositions(int paramInt, LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {}
    
    public int computeHorizontalScrollExtent(RecyclerView.State paramState)
    {
      return 0;
    }
    
    public int computeHorizontalScrollOffset(RecyclerView.State paramState)
    {
      return 0;
    }
    
    public int computeHorizontalScrollRange(RecyclerView.State paramState)
    {
      return 0;
    }
    
    public int computeVerticalScrollExtent(RecyclerView.State paramState)
    {
      return 0;
    }
    
    public int computeVerticalScrollOffset(RecyclerView.State paramState)
    {
      return 0;
    }
    
    public int computeVerticalScrollRange(RecyclerView.State paramState)
    {
      return 0;
    }
    
    public void detachAndScrapAttachedViews(RecyclerView.Recycler paramRecycler)
    {
      for (int i = getChildCount() - 1; i >= 0; i--) {
        scrapOrRecycleView(paramRecycler, i, getChildAt(i));
      }
    }
    
    public void detachAndScrapView(View paramView, RecyclerView.Recycler paramRecycler)
    {
      scrapOrRecycleView(paramRecycler, mChildHelper.indexOfChild(paramView), paramView);
    }
    
    public void detachAndScrapViewAt(int paramInt, RecyclerView.Recycler paramRecycler)
    {
      scrapOrRecycleView(paramRecycler, paramInt, getChildAt(paramInt));
    }
    
    public void detachView(View paramView)
    {
      int i = mChildHelper.indexOfChild(paramView);
      if (i >= 0) {
        detachViewInternal(i, paramView);
      }
    }
    
    public void detachViewAt(int paramInt)
    {
      detachViewInternal(paramInt, getChildAt(paramInt));
    }
    
    void dispatchAttachedToWindow(RecyclerView paramRecyclerView)
    {
      mIsAttachedToWindow = true;
      onAttachedToWindow(paramRecyclerView);
    }
    
    void dispatchDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler)
    {
      mIsAttachedToWindow = false;
      onDetachedFromWindow(paramRecyclerView, paramRecycler);
    }
    
    public void endAnimation(View paramView)
    {
      if (mRecyclerView.mItemAnimator != null) {
        mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(paramView));
      }
    }
    
    public View findContainingItemView(View paramView)
    {
      if (mRecyclerView == null) {
        return null;
      }
      paramView = mRecyclerView.findContainingItemView(paramView);
      if (paramView == null) {
        return null;
      }
      if (mChildHelper.isHidden(paramView)) {
        return null;
      }
      return paramView;
    }
    
    public View findViewByPosition(int paramInt)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(localView);
        if ((localViewHolder != null) && (localViewHolder.getLayoutPosition() == paramInt) && (!localViewHolder.shouldIgnore()) && ((mRecyclerView.mState.isPreLayout()) || (!localViewHolder.isRemoved()))) {
          return localView;
        }
      }
      return null;
    }
    
    public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();
    
    public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      return new RecyclerView.LayoutParams(paramContext, paramAttributeSet);
    }
    
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      if ((paramLayoutParams instanceof RecyclerView.LayoutParams)) {
        return new RecyclerView.LayoutParams((RecyclerView.LayoutParams)paramLayoutParams);
      }
      if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
        return new RecyclerView.LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
      }
      return new RecyclerView.LayoutParams(paramLayoutParams);
    }
    
    public int getBaseline()
    {
      return -1;
    }
    
    public int getBottomDecorationHeight(View paramView)
    {
      return getLayoutParamsmDecorInsets.bottom;
    }
    
    public View getChildAt(int paramInt)
    {
      View localView;
      if (mChildHelper != null) {
        localView = mChildHelper.getChildAt(paramInt);
      } else {
        localView = null;
      }
      return localView;
    }
    
    public int getChildCount()
    {
      int i;
      if (mChildHelper != null) {
        i = mChildHelper.getChildCount();
      } else {
        i = 0;
      }
      return i;
    }
    
    public boolean getClipToPadding()
    {
      boolean bool;
      if ((mRecyclerView != null) && (mRecyclerView.mClipToPadding)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      paramRecycler = mRecyclerView;
      int i = 1;
      if ((paramRecycler != null) && (mRecyclerView.mAdapter != null))
      {
        if (canScrollHorizontally()) {
          i = mRecyclerView.mAdapter.getItemCount();
        }
        return i;
      }
      return 1;
    }
    
    public int getDecoratedBottom(View paramView)
    {
      return paramView.getBottom() + getBottomDecorationHeight(paramView);
    }
    
    public void getDecoratedBoundsWithMargins(View paramView, Rect paramRect)
    {
      RecyclerView.getDecoratedBoundsWithMarginsInt(paramView, paramRect);
    }
    
    public int getDecoratedLeft(View paramView)
    {
      return paramView.getLeft() - getLeftDecorationWidth(paramView);
    }
    
    public int getDecoratedMeasuredHeight(View paramView)
    {
      Rect localRect = getLayoutParamsmDecorInsets;
      return paramView.getMeasuredHeight() + top + bottom;
    }
    
    public int getDecoratedMeasuredWidth(View paramView)
    {
      Rect localRect = getLayoutParamsmDecorInsets;
      return paramView.getMeasuredWidth() + left + right;
    }
    
    public int getDecoratedRight(View paramView)
    {
      return paramView.getRight() + getRightDecorationWidth(paramView);
    }
    
    public int getDecoratedTop(View paramView)
    {
      return paramView.getTop() - getTopDecorationHeight(paramView);
    }
    
    public View getFocusedChild()
    {
      if (mRecyclerView == null) {
        return null;
      }
      View localView = mRecyclerView.getFocusedChild();
      if ((localView != null) && (!mChildHelper.isHidden(localView))) {
        return localView;
      }
      return null;
    }
    
    public int getHeight()
    {
      return mHeight;
    }
    
    public int getHeightMode()
    {
      return mHeightMode;
    }
    
    public int getItemCount()
    {
      RecyclerView.Adapter localAdapter;
      if (mRecyclerView != null) {
        localAdapter = mRecyclerView.getAdapter();
      } else {
        localAdapter = null;
      }
      int i;
      if (localAdapter != null) {
        i = localAdapter.getItemCount();
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getItemViewType(View paramView)
    {
      return RecyclerView.getChildViewHolderInt(paramView).getItemViewType();
    }
    
    public int getLayoutDirection()
    {
      return mRecyclerView.getLayoutDirection();
    }
    
    public int getLeftDecorationWidth(View paramView)
    {
      return getLayoutParamsmDecorInsets.left;
    }
    
    public int getMinimumHeight()
    {
      return mRecyclerView.getMinimumHeight();
    }
    
    public int getMinimumWidth()
    {
      return mRecyclerView.getMinimumWidth();
    }
    
    public int getPaddingBottom()
    {
      int i;
      if (mRecyclerView != null) {
        i = mRecyclerView.getPaddingBottom();
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getPaddingEnd()
    {
      int i;
      if (mRecyclerView != null) {
        i = mRecyclerView.getPaddingEnd();
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getPaddingLeft()
    {
      int i;
      if (mRecyclerView != null) {
        i = mRecyclerView.getPaddingLeft();
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getPaddingRight()
    {
      int i;
      if (mRecyclerView != null) {
        i = mRecyclerView.getPaddingRight();
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getPaddingStart()
    {
      int i;
      if (mRecyclerView != null) {
        i = mRecyclerView.getPaddingStart();
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getPaddingTop()
    {
      int i;
      if (mRecyclerView != null) {
        i = mRecyclerView.getPaddingTop();
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getPosition(View paramView)
    {
      return ((RecyclerView.LayoutParams)paramView.getLayoutParams()).getViewLayoutPosition();
    }
    
    public int getRightDecorationWidth(View paramView)
    {
      return getLayoutParamsmDecorInsets.right;
    }
    
    public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      paramRecycler = mRecyclerView;
      int i = 1;
      if ((paramRecycler != null) && (mRecyclerView.mAdapter != null))
      {
        if (canScrollVertically()) {
          i = mRecyclerView.mAdapter.getItemCount();
        }
        return i;
      }
      return 1;
    }
    
    public int getSelectionModeForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return 0;
    }
    
    public int getTopDecorationHeight(View paramView)
    {
      return getLayoutParamsmDecorInsets.top;
    }
    
    public void getTransformedBoundingBox(View paramView, boolean paramBoolean, Rect paramRect)
    {
      Object localObject;
      if (paramBoolean)
      {
        localObject = getLayoutParamsmDecorInsets;
        paramRect.set(-left, -top, paramView.getWidth() + right, paramView.getHeight() + bottom);
      }
      else
      {
        paramRect.set(0, 0, paramView.getWidth(), paramView.getHeight());
      }
      if (mRecyclerView != null)
      {
        localObject = paramView.getMatrix();
        if ((localObject != null) && (!((Matrix)localObject).isIdentity()))
        {
          RectF localRectF = mRecyclerView.mTempRectF;
          localRectF.set(paramRect);
          ((Matrix)localObject).mapRect(localRectF);
          paramRect.set((int)Math.floor(left), (int)Math.floor(top), (int)Math.ceil(right), (int)Math.ceil(bottom));
        }
      }
      paramRect.offset(paramView.getLeft(), paramView.getTop());
    }
    
    public int getWidth()
    {
      return mWidth;
    }
    
    public int getWidthMode()
    {
      return mWidthMode;
    }
    
    boolean hasFlexibleChildInBothOrientations()
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        ViewGroup.LayoutParams localLayoutParams = getChildAt(j).getLayoutParams();
        if ((width < 0) && (height < 0)) {
          return true;
        }
      }
      return false;
    }
    
    public boolean hasFocus()
    {
      boolean bool;
      if ((mRecyclerView != null) && (mRecyclerView.hasFocus())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void ignoreView(View paramView)
    {
      if ((paramView.getParent() == mRecyclerView) && (mRecyclerView.indexOfChild(paramView) != -1))
      {
        paramView = RecyclerView.getChildViewHolderInt(paramView);
        paramView.addFlags(128);
        mRecyclerView.mViewInfoStore.removeViewHolder(paramView);
        return;
      }
      throw new IllegalArgumentException("View should be fully attached to be ignored");
    }
    
    public boolean isAttachedToWindow()
    {
      return mIsAttachedToWindow;
    }
    
    public boolean isAutoMeasureEnabled()
    {
      return mAutoMeasure;
    }
    
    public boolean isFocused()
    {
      boolean bool;
      if ((mRecyclerView != null) && (mRecyclerView.isFocused())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public final boolean isItemPrefetchEnabled()
    {
      return mItemPrefetchEnabled;
    }
    
    public boolean isLayoutHierarchical(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return false;
    }
    
    public boolean isMeasurementCacheEnabled()
    {
      return mMeasurementCacheEnabled;
    }
    
    public boolean isSmoothScrolling()
    {
      boolean bool;
      if ((mSmoothScroller != null) && (mSmoothScroller.isRunning())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void layoutDecorated(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      Rect localRect = getLayoutParamsmDecorInsets;
      paramView.layout(left + paramInt1, top + paramInt2, paramInt3 - right, paramInt4 - bottom);
    }
    
    public void layoutDecoratedWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
      Rect localRect = mDecorInsets;
      paramView.layout(left + paramInt1 + leftMargin, top + paramInt2 + topMargin, paramInt3 - right - rightMargin, paramInt4 - bottom - bottomMargin);
    }
    
    public void measureChild(View paramView, int paramInt1, int paramInt2)
    {
      RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
      Rect localRect = mRecyclerView.getItemDecorInsetsForChild(paramView);
      int i = left;
      int j = right;
      int k = top;
      int m = bottom;
      paramInt1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + (paramInt1 + (i + j)), width, canScrollHorizontally());
      paramInt2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + (paramInt2 + (k + m)), height, canScrollVertically());
      if (shouldMeasureChild(paramView, paramInt1, paramInt2, localLayoutParams)) {
        paramView.measure(paramInt1, paramInt2);
      }
    }
    
    public void measureChildWithMargins(View paramView, int paramInt1, int paramInt2)
    {
      RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
      Rect localRect = mRecyclerView.getItemDecorInsetsForChild(paramView);
      int i = left;
      int j = right;
      int k = top;
      int m = bottom;
      paramInt1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + leftMargin + rightMargin + (paramInt1 + (i + j)), width, canScrollHorizontally());
      paramInt2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + topMargin + bottomMargin + (paramInt2 + (k + m)), height, canScrollVertically());
      if (shouldMeasureChild(paramView, paramInt1, paramInt2, localLayoutParams)) {
        paramView.measure(paramInt1, paramInt2);
      }
    }
    
    public void moveView(int paramInt1, int paramInt2)
    {
      Object localObject = getChildAt(paramInt1);
      if (localObject != null)
      {
        detachViewAt(paramInt1);
        attachView((View)localObject, paramInt2);
        return;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Cannot move a child from non-existing index:");
      ((StringBuilder)localObject).append(paramInt1);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    
    public void offsetChildrenHorizontal(int paramInt)
    {
      if (mRecyclerView != null) {
        mRecyclerView.offsetChildrenHorizontal(paramInt);
      }
    }
    
    public void offsetChildrenVertical(int paramInt)
    {
      if (mRecyclerView != null) {
        mRecyclerView.offsetChildrenVertical(paramInt);
      }
    }
    
    public void onAdapterChanged(RecyclerView.Adapter paramAdapter1, RecyclerView.Adapter paramAdapter2) {}
    
    public boolean onAddFocusables(RecyclerView paramRecyclerView, ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
    {
      return false;
    }
    
    public void onAttachedToWindow(RecyclerView paramRecyclerView) {}
    
    @Deprecated
    public void onDetachedFromWindow(RecyclerView paramRecyclerView) {}
    
    public void onDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler)
    {
      onDetachedFromWindow(paramRecyclerView);
    }
    
    public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return null;
    }
    
    public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
    {
      onInitializeAccessibilityEvent(mRecyclerView.mRecycler, mRecyclerView.mState, paramAccessibilityEvent);
    }
    
    public void onInitializeAccessibilityEvent(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AccessibilityEvent paramAccessibilityEvent)
    {
      if ((mRecyclerView != null) && (paramAccessibilityEvent != null))
      {
        paramRecycler = mRecyclerView;
        boolean bool1 = true;
        boolean bool2 = bool1;
        if (!paramRecycler.canScrollVertically(1))
        {
          bool2 = bool1;
          if (!mRecyclerView.canScrollVertically(-1))
          {
            bool2 = bool1;
            if (!mRecyclerView.canScrollHorizontally(-1)) {
              if (mRecyclerView.canScrollHorizontally(1)) {
                bool2 = bool1;
              } else {
                bool2 = false;
              }
            }
          }
        }
        paramAccessibilityEvent.setScrollable(bool2);
        if (mRecyclerView.mAdapter != null) {
          paramAccessibilityEvent.setItemCount(mRecyclerView.mAdapter.getItemCount());
        }
        return;
      }
    }
    
    void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      onInitializeAccessibilityNodeInfo(mRecyclerView.mRecycler, mRecyclerView.mState, paramAccessibilityNodeInfo);
    }
    
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      if ((mRecyclerView.canScrollVertically(-1)) || (mRecyclerView.canScrollHorizontally(-1)))
      {
        paramAccessibilityNodeInfo.addAction(8192);
        paramAccessibilityNodeInfo.setScrollable(true);
      }
      if ((mRecyclerView.canScrollVertically(1)) || (mRecyclerView.canScrollHorizontally(1)))
      {
        paramAccessibilityNodeInfo.addAction(4096);
        paramAccessibilityNodeInfo.setScrollable(true);
      }
      paramAccessibilityNodeInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(getRowCountForAccessibility(paramRecycler, paramState), getColumnCountForAccessibility(paramRecycler, paramState), isLayoutHierarchical(paramRecycler, paramState), getSelectionModeForAccessibility(paramRecycler, paramState)));
    }
    
    void onInitializeAccessibilityNodeInfoForItem(View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if ((localViewHolder != null) && (!localViewHolder.isRemoved()) && (!mChildHelper.isHidden(itemView))) {
        onInitializeAccessibilityNodeInfoForItem(mRecyclerView.mRecycler, mRecyclerView.mState, paramView, paramAccessibilityNodeInfo);
      }
    }
    
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      boolean bool = canScrollVertically();
      int i = 0;
      int j;
      if (bool) {
        j = getPosition(paramView);
      } else {
        j = 0;
      }
      if (canScrollHorizontally()) {
        i = getPosition(paramView);
      }
      for (;;)
      {
        break;
      }
      paramAccessibilityNodeInfo.setCollectionItemInfo(AccessibilityNodeInfo.CollectionItemInfo.obtain(j, 1, i, 1, false, false));
    }
    
    public View onInterceptFocusSearch(View paramView, int paramInt)
    {
      return null;
    }
    
    public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {}
    
    public void onItemsChanged(RecyclerView paramRecyclerView) {}
    
    public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {}
    
    public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {}
    
    public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject)
    {
      onItemsUpdated(paramRecyclerView, paramInt1, paramInt2);
    }
    
    public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
    }
    
    public void onLayoutCompleted(RecyclerView.State paramState) {}
    
    public void onMeasure(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2)
    {
      mRecyclerView.defaultOnMeasure(paramInt1, paramInt2);
    }
    
    @Deprecated
    public boolean onRequestChildFocus(RecyclerView paramRecyclerView, View paramView1, View paramView2)
    {
      boolean bool;
      if ((!isSmoothScrolling()) && (!paramRecyclerView.isComputingLayout())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean onRequestChildFocus(RecyclerView paramRecyclerView, RecyclerView.State paramState, View paramView1, View paramView2)
    {
      return onRequestChildFocus(paramRecyclerView, paramView1, paramView2);
    }
    
    public void onRestoreInstanceState(Parcelable paramParcelable) {}
    
    public Parcelable onSaveInstanceState()
    {
      return null;
    }
    
    public void onScrollStateChanged(int paramInt) {}
    
    boolean performAccessibilityAction(int paramInt, Bundle paramBundle)
    {
      return performAccessibilityAction(mRecyclerView.mRecycler, mRecyclerView.mState, paramInt, paramBundle);
    }
    
    public boolean performAccessibilityAction(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt, Bundle paramBundle)
    {
      if (mRecyclerView == null) {
        return false;
      }
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      if (paramInt != 4096)
      {
        if (paramInt != 8192)
        {
          paramInt = j;
        }
        else
        {
          if (mRecyclerView.canScrollVertically(-1)) {
            k = -(getHeight() - getPaddingTop() - getPaddingBottom());
          }
          paramInt = k;
          if (mRecyclerView.canScrollHorizontally(-1))
          {
            m = -(getWidth() - getPaddingLeft() - getPaddingRight());
            paramInt = k;
          }
        }
      }
      else
      {
        k = i;
        if (mRecyclerView.canScrollVertically(1)) {
          k = getHeight() - getPaddingTop() - getPaddingBottom();
        }
        paramInt = k;
        if (mRecyclerView.canScrollHorizontally(1))
        {
          m = getWidth() - getPaddingLeft() - getPaddingRight();
          paramInt = k;
        }
      }
      if ((paramInt == 0) && (m == 0)) {
        return false;
      }
      mRecyclerView.smoothScrollBy(m, paramInt);
      return true;
    }
    
    boolean performAccessibilityActionForItem(View paramView, int paramInt, Bundle paramBundle)
    {
      return performAccessibilityActionForItem(mRecyclerView.mRecycler, mRecyclerView.mState, paramView, paramInt, paramBundle);
    }
    
    public boolean performAccessibilityActionForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, int paramInt, Bundle paramBundle)
    {
      return false;
    }
    
    public void postOnAnimation(Runnable paramRunnable)
    {
      if (mRecyclerView != null) {
        mRecyclerView.postOnAnimation(paramRunnable);
      }
    }
    
    public void removeAllViews()
    {
      for (int i = getChildCount() - 1; i >= 0; i--) {
        mChildHelper.removeViewAt(i);
      }
    }
    
    public void removeAndRecycleAllViews(RecyclerView.Recycler paramRecycler)
    {
      for (int i = getChildCount() - 1; i >= 0; i--) {
        if (!RecyclerView.getChildViewHolderInt(getChildAt(i)).shouldIgnore()) {
          removeAndRecycleViewAt(i, paramRecycler);
        }
      }
    }
    
    void removeAndRecycleScrapInt(RecyclerView.Recycler paramRecycler)
    {
      int i = paramRecycler.getScrapCount();
      for (int j = i - 1; j >= 0; j--)
      {
        View localView = paramRecycler.getScrapViewAt(j);
        RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(localView);
        if (!localViewHolder.shouldIgnore())
        {
          localViewHolder.setIsRecyclable(false);
          if (localViewHolder.isTmpDetached()) {
            mRecyclerView.removeDetachedView(localView, false);
          }
          if (mRecyclerView.mItemAnimator != null) {
            mRecyclerView.mItemAnimator.endAnimation(localViewHolder);
          }
          localViewHolder.setIsRecyclable(true);
          paramRecycler.quickRecycleScrapView(localView);
        }
      }
      paramRecycler.clearScrap();
      if (i > 0) {
        mRecyclerView.invalidate();
      }
    }
    
    public void removeAndRecycleView(View paramView, RecyclerView.Recycler paramRecycler)
    {
      removeView(paramView);
      paramRecycler.recycleView(paramView);
    }
    
    public void removeAndRecycleViewAt(int paramInt, RecyclerView.Recycler paramRecycler)
    {
      View localView = getChildAt(paramInt);
      removeViewAt(paramInt);
      paramRecycler.recycleView(localView);
    }
    
    public boolean removeCallbacks(Runnable paramRunnable)
    {
      if (mRecyclerView != null) {
        return mRecyclerView.removeCallbacks(paramRunnable);
      }
      return false;
    }
    
    public void removeDetachedView(View paramView)
    {
      mRecyclerView.removeDetachedView(paramView, false);
    }
    
    public void removeView(View paramView)
    {
      mChildHelper.removeView(paramView);
    }
    
    public void removeViewAt(int paramInt)
    {
      if (getChildAt(paramInt) != null) {
        mChildHelper.removeViewAt(paramInt);
      }
    }
    
    public boolean requestChildRectangleOnScreen(RecyclerView paramRecyclerView, View paramView, Rect paramRect, boolean paramBoolean)
    {
      int i = getPaddingLeft();
      int j = getPaddingTop();
      int k = getWidth() - getPaddingRight();
      int m = getHeight();
      int n = getPaddingBottom();
      int i1 = paramView.getLeft() + left - paramView.getScrollX();
      int i2 = paramView.getTop() + top - paramView.getScrollY();
      int i3 = paramRect.width() + i1;
      int i4 = paramRect.height();
      int i5 = Math.min(0, i1 - i);
      int i6 = Math.min(0, i2 - j);
      int i7 = Math.max(0, i3 - k);
      n = Math.max(0, i4 + i2 - (m - n));
      if (getLayoutDirection() == 1)
      {
        if (i7 != 0) {
          i5 = i7;
        } else {
          i5 = Math.max(i5, i3 - k);
        }
      }
      else if (i5 == 0) {
        i5 = Math.min(i1 - i, i7);
      }
      if (i6 == 0) {
        i6 = Math.min(i2 - j, n);
      }
      if ((i5 == 0) && (i6 == 0)) {
        return false;
      }
      if (paramBoolean) {
        paramRecyclerView.scrollBy(i5, i6);
      } else {
        paramRecyclerView.smoothScrollBy(i5, i6);
      }
      return true;
    }
    
    public void requestLayout()
    {
      if (mRecyclerView != null) {
        mRecyclerView.requestLayout();
      }
    }
    
    public void requestSimpleAnimationsInNextLayout()
    {
      mRequestedSimpleAnimations = true;
    }
    
    public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return 0;
    }
    
    public void scrollToPosition(int paramInt) {}
    
    public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
    {
      return 0;
    }
    
    public void setAutoMeasureEnabled(boolean paramBoolean)
    {
      mAutoMeasure = paramBoolean;
    }
    
    void setExactMeasureSpecsFrom(RecyclerView paramRecyclerView)
    {
      setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(paramRecyclerView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(paramRecyclerView.getHeight(), 1073741824));
    }
    
    public final void setItemPrefetchEnabled(boolean paramBoolean)
    {
      if (paramBoolean != mItemPrefetchEnabled)
      {
        mItemPrefetchEnabled = paramBoolean;
        mPrefetchMaxCountObserved = 0;
        if (mRecyclerView != null) {
          mRecyclerView.mRecycler.updateViewCacheSize();
        }
      }
    }
    
    void setMeasureSpecs(int paramInt1, int paramInt2)
    {
      mWidth = View.MeasureSpec.getSize(paramInt1);
      mWidthMode = View.MeasureSpec.getMode(paramInt1);
      if ((mWidthMode == 0) && (!RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)) {
        mWidth = 0;
      }
      mHeight = View.MeasureSpec.getSize(paramInt2);
      mHeightMode = View.MeasureSpec.getMode(paramInt2);
      if ((mHeightMode == 0) && (!RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)) {
        mHeight = 0;
      }
    }
    
    public void setMeasuredDimension(int paramInt1, int paramInt2)
    {
      mRecyclerView.setMeasuredDimension(paramInt1, paramInt2);
    }
    
    public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2)
    {
      int i = paramRect.width();
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = paramRect.height();
      int n = getPaddingTop();
      int i1 = getPaddingBottom();
      setMeasuredDimension(chooseSize(paramInt1, i + j + k, getMinimumWidth()), chooseSize(paramInt2, m + n + i1, getMinimumHeight()));
    }
    
    void setMeasuredDimensionFromChildren(int paramInt1, int paramInt2)
    {
      int i = getChildCount();
      if (i == 0)
      {
        mRecyclerView.defaultOnMeasure(paramInt1, paramInt2);
        return;
      }
      int j = Integer.MAX_VALUE;
      int k = Integer.MAX_VALUE;
      int m = Integer.MIN_VALUE;
      int n = Integer.MIN_VALUE;
      int i1 = 0;
      while (i1 < i)
      {
        View localView = getChildAt(i1);
        Rect localRect = mRecyclerView.mTempRect;
        getDecoratedBoundsWithMargins(localView, localRect);
        int i2 = j;
        if (left < j) {
          i2 = left;
        }
        int i3 = m;
        if (right > m) {
          i3 = right;
        }
        m = k;
        if (top < k) {
          m = top;
        }
        int i4 = n;
        if (bottom > n) {
          i4 = bottom;
        }
        i1++;
        j = i2;
        k = m;
        m = i3;
        n = i4;
      }
      mRecyclerView.mTempRect.set(j, k, m, n);
      setMeasuredDimension(mRecyclerView.mTempRect, paramInt1, paramInt2);
    }
    
    public void setMeasurementCacheEnabled(boolean paramBoolean)
    {
      mMeasurementCacheEnabled = paramBoolean;
    }
    
    void setRecyclerView(RecyclerView paramRecyclerView)
    {
      if (paramRecyclerView == null)
      {
        mRecyclerView = null;
        mChildHelper = null;
        mWidth = 0;
        mHeight = 0;
      }
      else
      {
        mRecyclerView = paramRecyclerView;
        mChildHelper = mChildHelper;
        mWidth = paramRecyclerView.getWidth();
        mHeight = paramRecyclerView.getHeight();
      }
      mWidthMode = 1073741824;
      mHeightMode = 1073741824;
    }
    
    boolean shouldMeasureChild(View paramView, int paramInt1, int paramInt2, RecyclerView.LayoutParams paramLayoutParams)
    {
      boolean bool;
      if ((!paramView.isLayoutRequested()) && (mMeasurementCacheEnabled) && (isMeasurementUpToDate(paramView.getWidth(), paramInt1, width)) && (isMeasurementUpToDate(paramView.getHeight(), paramInt2, height))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    boolean shouldMeasureTwice()
    {
      return false;
    }
    
    boolean shouldReMeasureChild(View paramView, int paramInt1, int paramInt2, RecyclerView.LayoutParams paramLayoutParams)
    {
      boolean bool;
      if ((mMeasurementCacheEnabled) && (isMeasurementUpToDate(paramView.getMeasuredWidth(), paramInt1, width)) && (isMeasurementUpToDate(paramView.getMeasuredHeight(), paramInt2, height))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public void smoothScrollToPosition(RecyclerView paramRecyclerView, RecyclerView.State paramState, int paramInt)
    {
      Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
    }
    
    public void startSmoothScroll(RecyclerView.SmoothScroller paramSmoothScroller)
    {
      if ((mSmoothScroller != null) && (paramSmoothScroller != mSmoothScroller) && (mSmoothScroller.isRunning())) {
        mSmoothScroller.stop();
      }
      mSmoothScroller = paramSmoothScroller;
      mSmoothScroller.start(mRecyclerView, this);
    }
    
    public void stopIgnoringView(View paramView)
    {
      paramView = RecyclerView.getChildViewHolderInt(paramView);
      paramView.stopIgnoring();
      paramView.resetInternal();
      paramView.addFlags(4);
    }
    
    void stopSmoothScroller()
    {
      if (mSmoothScroller != null) {
        mSmoothScroller.stop();
      }
    }
    
    public boolean supportsPredictiveItemAnimations()
    {
      return false;
    }
    
    public static abstract interface LayoutPrefetchRegistry
    {
      public abstract void addPosition(int paramInt1, int paramInt2);
    }
    
    public static class Properties
    {
      public int orientation;
      public boolean reverseLayout;
      public int spanCount;
      public boolean stackFromEnd;
      
      public Properties() {}
    }
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    final Rect mDecorInsets = new Rect();
    boolean mInsetsDirty = true;
    boolean mPendingInvalidate = false;
    RecyclerView.ViewHolder mViewHolder;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public int getViewAdapterPosition()
    {
      return mViewHolder.getAdapterPosition();
    }
    
    public int getViewLayoutPosition()
    {
      return mViewHolder.getLayoutPosition();
    }
    
    @Deprecated
    public int getViewPosition()
    {
      return mViewHolder.getPosition();
    }
    
    public boolean isItemChanged()
    {
      return mViewHolder.isUpdated();
    }
    
    public boolean isItemRemoved()
    {
      return mViewHolder.isRemoved();
    }
    
    public boolean isViewInvalid()
    {
      return mViewHolder.isInvalid();
    }
    
    public boolean viewNeedsUpdate()
    {
      return mViewHolder.needsUpdate();
    }
  }
  
  public static abstract interface OnChildAttachStateChangeListener
  {
    public abstract void onChildViewAttachedToWindow(View paramView);
    
    public abstract void onChildViewDetachedFromWindow(View paramView);
  }
  
  public static abstract class OnFlingListener
  {
    public OnFlingListener() {}
    
    public abstract boolean onFling(int paramInt1, int paramInt2);
  }
  
  public static abstract interface OnItemTouchListener
  {
    public abstract boolean onInterceptTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent);
    
    public abstract void onRequestDisallowInterceptTouchEvent(boolean paramBoolean);
    
    public abstract void onTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent);
  }
  
  public static abstract class OnScrollListener
  {
    public OnScrollListener() {}
    
    public void onScrollStateChanged(RecyclerView paramRecyclerView, int paramInt) {}
    
    public void onScrolled(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {}
  }
  
  public static class RecycledViewPool
  {
    private static final int DEFAULT_MAX_SCRAP = 5;
    private int mAttachCount = 0;
    SparseArray<ScrapData> mScrap = new SparseArray();
    
    public RecycledViewPool() {}
    
    private ScrapData getScrapDataForType(int paramInt)
    {
      ScrapData localScrapData1 = (ScrapData)mScrap.get(paramInt);
      ScrapData localScrapData2 = localScrapData1;
      if (localScrapData1 == null)
      {
        localScrapData2 = new ScrapData();
        mScrap.put(paramInt, localScrapData2);
      }
      return localScrapData2;
    }
    
    void attach(RecyclerView.Adapter paramAdapter)
    {
      mAttachCount += 1;
    }
    
    public void clear()
    {
      for (int i = 0; i < mScrap.size(); i++) {
        mScrap.valueAt(i)).mScrapHeap.clear();
      }
    }
    
    void detach()
    {
      mAttachCount -= 1;
    }
    
    void factorInBindTime(int paramInt, long paramLong)
    {
      ScrapData localScrapData = getScrapDataForType(paramInt);
      mBindRunningAverageNs = runningAverage(mBindRunningAverageNs, paramLong);
    }
    
    void factorInCreateTime(int paramInt, long paramLong)
    {
      ScrapData localScrapData = getScrapDataForType(paramInt);
      mCreateRunningAverageNs = runningAverage(mCreateRunningAverageNs, paramLong);
    }
    
    public RecyclerView.ViewHolder getRecycledView(int paramInt)
    {
      Object localObject = (ScrapData)mScrap.get(paramInt);
      if ((localObject != null) && (!mScrapHeap.isEmpty()))
      {
        localObject = mScrapHeap;
        return (RecyclerView.ViewHolder)((ArrayList)localObject).remove(((ArrayList)localObject).size() - 1);
      }
      return null;
    }
    
    public int getRecycledViewCount(int paramInt)
    {
      return getScrapDataForTypemScrapHeap.size();
    }
    
    void onAdapterChanged(RecyclerView.Adapter paramAdapter1, RecyclerView.Adapter paramAdapter2, boolean paramBoolean)
    {
      if (paramAdapter1 != null) {
        detach();
      }
      if ((!paramBoolean) && (mAttachCount == 0)) {
        clear();
      }
      if (paramAdapter2 != null) {
        attach(paramAdapter2);
      }
    }
    
    public void putRecycledView(RecyclerView.ViewHolder paramViewHolder)
    {
      int i = paramViewHolder.getItemViewType();
      ArrayList localArrayList = getScrapDataForTypemScrapHeap;
      if (mScrap.get(i)).mMaxScrap <= localArrayList.size()) {
        return;
      }
      paramViewHolder.resetInternal();
      localArrayList.add(paramViewHolder);
    }
    
    long runningAverage(long paramLong1, long paramLong2)
    {
      if (paramLong1 == 0L) {
        return paramLong2;
      }
      return paramLong1 / 4L * 3L + paramLong2 / 4L;
    }
    
    public void setMaxRecycledViews(int paramInt1, int paramInt2)
    {
      Object localObject = getScrapDataForType(paramInt1);
      mMaxScrap = paramInt2;
      localObject = mScrapHeap;
      if (localObject != null) {
        while (((ArrayList)localObject).size() > paramInt2) {
          ((ArrayList)localObject).remove(((ArrayList)localObject).size() - 1);
        }
      }
    }
    
    int size()
    {
      int i = 0;
      int j = 0;
      while (j < mScrap.size())
      {
        ArrayList localArrayList = mScrap.valueAt(j)).mScrapHeap;
        int k = i;
        if (localArrayList != null) {
          k = i + localArrayList.size();
        }
        j++;
        i = k;
      }
      return i;
    }
    
    boolean willBindInTime(int paramInt, long paramLong1, long paramLong2)
    {
      long l = getScrapDataForTypemBindRunningAverageNs;
      boolean bool;
      if ((l != 0L) && (paramLong1 + l >= paramLong2)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    boolean willCreateInTime(int paramInt, long paramLong1, long paramLong2)
    {
      long l = getScrapDataForTypemCreateRunningAverageNs;
      boolean bool;
      if ((l != 0L) && (paramLong1 + l >= paramLong2)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    static class ScrapData
    {
      long mBindRunningAverageNs = 0L;
      long mCreateRunningAverageNs = 0L;
      int mMaxScrap = 5;
      ArrayList<RecyclerView.ViewHolder> mScrapHeap = new ArrayList();
      
      ScrapData() {}
    }
  }
  
  public final class Recycler
  {
    static final int DEFAULT_CACHE_SIZE = 2;
    final ArrayList<RecyclerView.ViewHolder> mAttachedScrap = new ArrayList();
    final ArrayList<RecyclerView.ViewHolder> mCachedViews = new ArrayList();
    ArrayList<RecyclerView.ViewHolder> mChangedScrap = null;
    RecyclerView.RecycledViewPool mRecyclerPool;
    private int mRequestedCacheMax = 2;
    private final List<RecyclerView.ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(mAttachedScrap);
    private RecyclerView.ViewCacheExtension mViewCacheExtension;
    int mViewCacheMax = 2;
    
    public Recycler() {}
    
    private void attachAccessibilityDelegate(View paramView)
    {
      if (isAccessibilityEnabled())
      {
        if (paramView.getImportantForAccessibility() == 0) {
          paramView.setImportantForAccessibility(1);
        }
        if (paramView.getAccessibilityDelegate() == null) {
          paramView.setAccessibilityDelegate(mAccessibilityDelegate.getItemDelegate());
        }
      }
    }
    
    private void invalidateDisplayListInt(ViewGroup paramViewGroup, boolean paramBoolean)
    {
      for (int i = paramViewGroup.getChildCount() - 1; i >= 0; i--)
      {
        View localView = paramViewGroup.getChildAt(i);
        if ((localView instanceof ViewGroup)) {
          invalidateDisplayListInt((ViewGroup)localView, true);
        }
      }
      if (!paramBoolean) {
        return;
      }
      if (paramViewGroup.getVisibility() == 4)
      {
        paramViewGroup.setVisibility(0);
        paramViewGroup.setVisibility(4);
      }
      else
      {
        i = paramViewGroup.getVisibility();
        paramViewGroup.setVisibility(4);
        paramViewGroup.setVisibility(i);
      }
    }
    
    private void invalidateDisplayListInt(RecyclerView.ViewHolder paramViewHolder)
    {
      if ((itemView instanceof ViewGroup)) {
        invalidateDisplayListInt((ViewGroup)itemView, false);
      }
    }
    
    private boolean tryBindViewHolderByDeadline(RecyclerView.ViewHolder paramViewHolder, int paramInt1, int paramInt2, long paramLong)
    {
      mOwnerRecyclerView = RecyclerView.this;
      int i = paramViewHolder.getItemViewType();
      long l = getNanoTime();
      if ((paramLong != Long.MAX_VALUE) && (!mRecyclerPool.willBindInTime(i, l, paramLong))) {
        return false;
      }
      mAdapter.bindViewHolder(paramViewHolder, paramInt1);
      paramLong = getNanoTime();
      mRecyclerPool.factorInBindTime(paramViewHolder.getItemViewType(), paramLong - l);
      attachAccessibilityDelegate(itemView);
      if (mState.isPreLayout()) {
        mPreLayoutPosition = paramInt2;
      }
      return true;
    }
    
    void addViewHolderToRecycledViewPool(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean)
    {
      RecyclerView.clearNestedRecyclerViewIfNotNested(paramViewHolder);
      itemView.setAccessibilityDelegate(null);
      if (paramBoolean) {
        dispatchViewRecycled(paramViewHolder);
      }
      mOwnerRecyclerView = null;
      getRecycledViewPool().putRecycledView(paramViewHolder);
    }
    
    public void bindViewToPosition(View paramView, int paramInt)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder != null)
      {
        int i = mAdapterHelper.findPositionOffset(paramInt);
        if ((i >= 0) && (i < mAdapter.getItemCount()))
        {
          tryBindViewHolderByDeadline(localViewHolder, i, paramInt, Long.MAX_VALUE);
          paramView = itemView.getLayoutParams();
          if (paramView == null)
          {
            paramView = (RecyclerView.LayoutParams)generateDefaultLayoutParams();
            itemView.setLayoutParams(paramView);
          }
          else if (!checkLayoutParams(paramView))
          {
            paramView = (RecyclerView.LayoutParams)generateLayoutParams(paramView);
            itemView.setLayoutParams(paramView);
          }
          else
          {
            paramView = (RecyclerView.LayoutParams)paramView;
          }
          boolean bool = true;
          mInsetsDirty = true;
          mViewHolder = localViewHolder;
          if (itemView.getParent() != null) {
            bool = false;
          }
          mPendingInvalidate = bool;
          return;
        }
        paramView = new StringBuilder();
        paramView.append("Inconsistency detected. Invalid item position ");
        paramView.append(paramInt);
        paramView.append("(offset:");
        paramView.append(i);
        paramView.append(").state:");
        paramView.append(mState.getItemCount());
        throw new IndexOutOfBoundsException(paramView.toString());
      }
      throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
    }
    
    public void clear()
    {
      mAttachedScrap.clear();
      recycleAndClearCachedViews();
    }
    
    void clearOldPositions()
    {
      int i = mCachedViews.size();
      int j = 0;
      for (int k = 0; k < i; k++) {
        ((RecyclerView.ViewHolder)mCachedViews.get(k)).clearOldPosition();
      }
      i = mAttachedScrap.size();
      for (k = 0; k < i; k++) {
        ((RecyclerView.ViewHolder)mAttachedScrap.get(k)).clearOldPosition();
      }
      if (mChangedScrap != null)
      {
        i = mChangedScrap.size();
        for (k = j; k < i; k++) {
          ((RecyclerView.ViewHolder)mChangedScrap.get(k)).clearOldPosition();
        }
      }
    }
    
    void clearScrap()
    {
      mAttachedScrap.clear();
      if (mChangedScrap != null) {
        mChangedScrap.clear();
      }
    }
    
    public int convertPreLayoutPositionToPostLayout(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < mState.getItemCount()))
      {
        if (!mState.isPreLayout()) {
          return paramInt;
        }
        return mAdapterHelper.findPositionOffset(paramInt);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid position ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(". State item count is ");
      localStringBuilder.append(mState.getItemCount());
      throw new IndexOutOfBoundsException(localStringBuilder.toString());
    }
    
    void dispatchViewRecycled(RecyclerView.ViewHolder paramViewHolder)
    {
      if (mRecyclerListener != null) {
        mRecyclerListener.onViewRecycled(paramViewHolder);
      }
      if (mAdapter != null) {
        mAdapter.onViewRecycled(paramViewHolder);
      }
      if (mState != null) {
        mViewInfoStore.removeViewHolder(paramViewHolder);
      }
    }
    
    RecyclerView.ViewHolder getChangedScrapViewForPosition(int paramInt)
    {
      if (mChangedScrap != null)
      {
        int i = mChangedScrap.size();
        if (i != 0)
        {
          int j = 0;
          RecyclerView.ViewHolder localViewHolder;
          for (int k = 0; k < i; k++)
          {
            localViewHolder = (RecyclerView.ViewHolder)mChangedScrap.get(k);
            if ((!localViewHolder.wasReturnedFromScrap()) && (localViewHolder.getLayoutPosition() == paramInt))
            {
              localViewHolder.addFlags(32);
              return localViewHolder;
            }
          }
          if (mAdapter.hasStableIds())
          {
            paramInt = mAdapterHelper.findPositionOffset(paramInt);
            if ((paramInt > 0) && (paramInt < mAdapter.getItemCount()))
            {
              long l = mAdapter.getItemId(paramInt);
              for (paramInt = j; paramInt < i; paramInt++)
              {
                localViewHolder = (RecyclerView.ViewHolder)mChangedScrap.get(paramInt);
                if ((!localViewHolder.wasReturnedFromScrap()) && (localViewHolder.getItemId() == l))
                {
                  localViewHolder.addFlags(32);
                  return localViewHolder;
                }
              }
            }
          }
          return null;
        }
      }
      return null;
    }
    
    RecyclerView.RecycledViewPool getRecycledViewPool()
    {
      if (mRecyclerPool == null) {
        mRecyclerPool = new RecyclerView.RecycledViewPool();
      }
      return mRecyclerPool;
    }
    
    int getScrapCount()
    {
      return mAttachedScrap.size();
    }
    
    public List<RecyclerView.ViewHolder> getScrapList()
    {
      return mUnmodifiableAttachedScrap;
    }
    
    RecyclerView.ViewHolder getScrapOrCachedViewForId(long paramLong, int paramInt, boolean paramBoolean)
    {
      RecyclerView.ViewHolder localViewHolder;
      for (int i = mAttachedScrap.size() - 1; i >= 0; i--)
      {
        localViewHolder = (RecyclerView.ViewHolder)mAttachedScrap.get(i);
        if ((localViewHolder.getItemId() == paramLong) && (!localViewHolder.wasReturnedFromScrap()))
        {
          if (paramInt == localViewHolder.getItemViewType())
          {
            localViewHolder.addFlags(32);
            if ((localViewHolder.isRemoved()) && (!mState.isPreLayout())) {
              localViewHolder.setFlags(2, 14);
            }
            return localViewHolder;
          }
          if (!paramBoolean)
          {
            mAttachedScrap.remove(i);
            removeDetachedView(itemView, false);
            quickRecycleScrapView(itemView);
          }
        }
      }
      for (i = mCachedViews.size() - 1; i >= 0; i--)
      {
        localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(i);
        if (localViewHolder.getItemId() == paramLong)
        {
          if (paramInt == localViewHolder.getItemViewType())
          {
            if (!paramBoolean) {
              mCachedViews.remove(i);
            }
            return localViewHolder;
          }
          if (!paramBoolean)
          {
            recycleCachedViewAt(i);
            return null;
          }
        }
      }
      return null;
    }
    
    RecyclerView.ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int paramInt, boolean paramBoolean)
    {
      int i = mAttachedScrap.size();
      int j = 0;
      RecyclerView.ViewHolder localViewHolder;
      for (int k = 0; k < i; k++)
      {
        localViewHolder = (RecyclerView.ViewHolder)mAttachedScrap.get(k);
        if ((!localViewHolder.wasReturnedFromScrap()) && (localViewHolder.getLayoutPosition() == paramInt) && (!localViewHolder.isInvalid()) && ((mState.mInPreLayout) || (!localViewHolder.isRemoved())))
        {
          localViewHolder.addFlags(32);
          return localViewHolder;
        }
      }
      if (!paramBoolean)
      {
        Object localObject = mChildHelper.findHiddenNonRemovedView(paramInt);
        if (localObject != null)
        {
          localViewHolder = RecyclerView.getChildViewHolderInt((View)localObject);
          mChildHelper.unhide((View)localObject);
          paramInt = mChildHelper.indexOfChild((View)localObject);
          if (paramInt != -1)
          {
            mChildHelper.detachViewFromParent(paramInt);
            scrapView((View)localObject);
            localViewHolder.addFlags(8224);
            return localViewHolder;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("layout index should not be -1 after unhiding a view:");
          ((StringBuilder)localObject).append(localViewHolder);
          throw new IllegalStateException(((StringBuilder)localObject).toString());
        }
      }
      i = mCachedViews.size();
      for (k = j; k < i; k++)
      {
        localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(k);
        if ((!localViewHolder.isInvalid()) && (localViewHolder.getLayoutPosition() == paramInt))
        {
          if (!paramBoolean) {
            mCachedViews.remove(k);
          }
          return localViewHolder;
        }
      }
      return null;
    }
    
    View getScrapViewAt(int paramInt)
    {
      return mAttachedScrap.get(paramInt)).itemView;
    }
    
    public View getViewForPosition(int paramInt)
    {
      return getViewForPosition(paramInt, false);
    }
    
    View getViewForPosition(int paramInt, boolean paramBoolean)
    {
      return tryGetViewHolderForPositionByDeadlineMAX_VALUEitemView;
    }
    
    void markItemDecorInsetsDirty()
    {
      int i = mCachedViews.size();
      for (int j = 0; j < i; j++)
      {
        RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)mCachedViews.get(j)).itemView.getLayoutParams();
        if (localLayoutParams != null) {
          mInsetsDirty = true;
        }
      }
    }
    
    void markKnownViewsInvalid()
    {
      if ((mAdapter != null) && (mAdapter.hasStableIds()))
      {
        int i = mCachedViews.size();
        for (int j = 0; j < i; j++)
        {
          RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(j);
          if (localViewHolder != null)
          {
            localViewHolder.addFlags(6);
            localViewHolder.addChangePayload(null);
          }
        }
      }
      else
      {
        recycleAndClearCachedViews();
      }
    }
    
    void offsetPositionRecordsForInsert(int paramInt1, int paramInt2)
    {
      int i = mCachedViews.size();
      for (int j = 0; j < i; j++)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(j);
        if ((localViewHolder != null) && (mPosition >= paramInt1)) {
          localViewHolder.offsetPosition(paramInt2, true);
        }
      }
    }
    
    void offsetPositionRecordsForMove(int paramInt1, int paramInt2)
    {
      int i;
      int j;
      int k;
      if (paramInt1 < paramInt2)
      {
        i = paramInt1;
        j = paramInt2;
        k = -1;
      }
      else
      {
        i = paramInt2;
        j = paramInt1;
        k = 1;
      }
      int m = mCachedViews.size();
      for (int n = 0; n < m; n++)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(n);
        if ((localViewHolder != null) && (mPosition >= i) && (mPosition <= j)) {
          if (mPosition == paramInt1) {
            localViewHolder.offsetPosition(paramInt2 - paramInt1, false);
          } else {
            localViewHolder.offsetPosition(k, false);
          }
        }
      }
    }
    
    void offsetPositionRecordsForRemove(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      for (int i = mCachedViews.size() - 1; i >= 0; i--)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(i);
        if (localViewHolder != null) {
          if (mPosition >= paramInt1 + paramInt2)
          {
            localViewHolder.offsetPosition(-paramInt2, paramBoolean);
          }
          else if (mPosition >= paramInt1)
          {
            localViewHolder.addFlags(8);
            recycleCachedViewAt(i);
          }
        }
      }
    }
    
    void onAdapterChanged(RecyclerView.Adapter paramAdapter1, RecyclerView.Adapter paramAdapter2, boolean paramBoolean)
    {
      clear();
      getRecycledViewPool().onAdapterChanged(paramAdapter1, paramAdapter2, paramBoolean);
    }
    
    void quickRecycleScrapView(View paramView)
    {
      paramView = RecyclerView.getChildViewHolderInt(paramView);
      RecyclerView.ViewHolder.access$802(paramView, null);
      RecyclerView.ViewHolder.access$902(paramView, false);
      paramView.clearReturnedFromScrapFlag();
      recycleViewHolderInternal(paramView);
    }
    
    void recycleAndClearCachedViews()
    {
      for (int i = mCachedViews.size() - 1; i >= 0; i--) {
        recycleCachedViewAt(i);
      }
      mCachedViews.clear();
      if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
        mPrefetchRegistry.clearPrefetchPositions();
      }
    }
    
    void recycleCachedViewAt(int paramInt)
    {
      addViewHolderToRecycledViewPool((RecyclerView.ViewHolder)mCachedViews.get(paramInt), true);
      mCachedViews.remove(paramInt);
    }
    
    public void recycleView(View paramView)
    {
      RecyclerView.ViewHolder localViewHolder = RecyclerView.getChildViewHolderInt(paramView);
      if (localViewHolder.isTmpDetached()) {
        removeDetachedView(paramView, false);
      }
      if (localViewHolder.isScrap()) {
        localViewHolder.unScrap();
      } else if (localViewHolder.wasReturnedFromScrap()) {
        localViewHolder.clearReturnedFromScrapFlag();
      }
      recycleViewHolderInternal(localViewHolder);
    }
    
    void recycleViewHolderInternal(RecyclerView.ViewHolder paramViewHolder)
    {
      boolean bool1 = paramViewHolder.isScrap();
      boolean bool2 = false;
      if ((!bool1) && (itemView.getParent() == null))
      {
        if (!paramViewHolder.isTmpDetached())
        {
          if (!paramViewHolder.shouldIgnore())
          {
            bool2 = RecyclerView.ViewHolder.access$700(paramViewHolder);
            int i;
            if ((mAdapter != null) && (bool2) && (mAdapter.onFailedToRecycleView(paramViewHolder))) {
              i = 1;
            } else {
              i = 0;
            }
            int j = 0;
            int k = 0;
            int m = 0;
            int n;
            if (i == 0)
            {
              n = m;
              if (!paramViewHolder.isRecyclable()) {}
            }
            else
            {
              i = k;
              if (mViewCacheMax > 0)
              {
                i = k;
                if (!paramViewHolder.hasAnyOfTheFlags(526))
                {
                  n = mCachedViews.size();
                  i = n;
                  if (n >= mViewCacheMax)
                  {
                    i = n;
                    if (n > 0)
                    {
                      recycleCachedViewAt(0);
                      i = n - 1;
                    }
                  }
                  j = i;
                  n = j;
                  if (RecyclerView.ALLOW_THREAD_GAP_WORK)
                  {
                    n = j;
                    if (i > 0)
                    {
                      n = j;
                      if (!mPrefetchRegistry.lastPrefetchIncludedPosition(mPosition))
                      {
                        i--;
                        while (i >= 0)
                        {
                          n = mCachedViews.get(i)).mPosition;
                          if (!mPrefetchRegistry.lastPrefetchIncludedPosition(n)) {
                            break;
                          }
                          i--;
                        }
                        n = i + 1;
                      }
                    }
                  }
                  mCachedViews.add(n, paramViewHolder);
                  i = 1;
                }
              }
              j = i;
              n = m;
              if (i == 0)
              {
                addViewHolderToRecycledViewPool(paramViewHolder, true);
                n = 1;
                j = i;
              }
            }
            mViewInfoStore.removeViewHolder(paramViewHolder);
            if ((j == 0) && (n == 0) && (bool2)) {
              mOwnerRecyclerView = null;
            }
            return;
          }
          throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Tmp detached view should be removed from RecyclerView before it can be recycled: ");
        localStringBuilder.append(paramViewHolder);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Scrapped or attached views may not be recycled. isScrap:");
      localStringBuilder.append(paramViewHolder.isScrap());
      localStringBuilder.append(" isAttached:");
      if (itemView.getParent() != null) {
        bool2 = true;
      }
      localStringBuilder.append(bool2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    void recycleViewInternal(View paramView)
    {
      recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(paramView));
    }
    
    void scrapView(View paramView)
    {
      paramView = RecyclerView.getChildViewHolderInt(paramView);
      if ((!paramView.hasAnyOfTheFlags(12)) && (paramView.isUpdated()) && (!canReuseUpdatedViewHolder(paramView)))
      {
        if (mChangedScrap == null) {
          mChangedScrap = new ArrayList();
        }
        paramView.setScrapContainer(this, true);
        mChangedScrap.add(paramView);
      }
      else
      {
        if ((paramView.isInvalid()) && (!paramView.isRemoved()) && (!mAdapter.hasStableIds())) {
          throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
        }
        paramView.setScrapContainer(this, false);
        mAttachedScrap.add(paramView);
      }
    }
    
    void setAdapterPositionsAsUnknown()
    {
      int i = mCachedViews.size();
      for (int j = 0; j < i; j++)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(j);
        if (localViewHolder != null) {
          localViewHolder.addFlags(512);
        }
      }
    }
    
    void setRecycledViewPool(RecyclerView.RecycledViewPool paramRecycledViewPool)
    {
      if (mRecyclerPool != null) {
        mRecyclerPool.detach();
      }
      mRecyclerPool = paramRecycledViewPool;
      if (paramRecycledViewPool != null) {
        mRecyclerPool.attach(getAdapter());
      }
    }
    
    void setViewCacheExtension(RecyclerView.ViewCacheExtension paramViewCacheExtension)
    {
      mViewCacheExtension = paramViewCacheExtension;
    }
    
    public void setViewCacheSize(int paramInt)
    {
      mRequestedCacheMax = paramInt;
      updateViewCacheSize();
    }
    
    RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int paramInt, boolean paramBoolean, long paramLong)
    {
      if ((paramInt >= 0) && (paramInt < mState.getItemCount()))
      {
        int i = 0;
        Object localObject1 = null;
        boolean bool1 = mState.isPreLayout();
        boolean bool2 = true;
        if (bool1)
        {
          localObject1 = getChangedScrapViewForPosition(paramInt);
          if (localObject1 != null) {
            j = 1;
          } else {
            j = 0;
          }
          i = j;
        }
        int j = i;
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject1 = getScrapOrHiddenOrCachedHolderForPosition(paramInt, paramBoolean);
          j = i;
          localObject2 = localObject1;
          if (localObject1 != null) {
            if (!validateViewHolderForOffsetPosition((RecyclerView.ViewHolder)localObject1))
            {
              if (!paramBoolean)
              {
                ((RecyclerView.ViewHolder)localObject1).addFlags(4);
                if (((RecyclerView.ViewHolder)localObject1).isScrap())
                {
                  removeDetachedView(itemView, false);
                  ((RecyclerView.ViewHolder)localObject1).unScrap();
                }
                else if (((RecyclerView.ViewHolder)localObject1).wasReturnedFromScrap())
                {
                  ((RecyclerView.ViewHolder)localObject1).clearReturnedFromScrapFlag();
                }
                recycleViewHolderInternal((RecyclerView.ViewHolder)localObject1);
              }
              localObject2 = null;
              j = i;
            }
            else
            {
              j = 1;
              localObject2 = localObject1;
            }
          }
        }
        int k = j;
        localObject1 = localObject2;
        if (localObject2 == null)
        {
          k = mAdapterHelper.findPositionOffset(paramInt);
          if ((k >= 0) && (k < mAdapter.getItemCount()))
          {
            int m = mAdapter.getItemViewType(k);
            i = j;
            if (mAdapter.hasStableIds())
            {
              localObject1 = getScrapOrCachedViewForId(mAdapter.getItemId(k), m, paramBoolean);
              i = j;
              localObject2 = localObject1;
              if (localObject1 != null)
              {
                mPosition = k;
                i = 1;
                localObject2 = localObject1;
              }
            }
            localObject1 = localObject2;
            if (localObject2 == null)
            {
              localObject1 = localObject2;
              if (mViewCacheExtension != null)
              {
                View localView = mViewCacheExtension.getViewForPositionAndType(this, paramInt, m);
                localObject1 = localObject2;
                if (localView != null)
                {
                  localObject1 = getChildViewHolder(localView);
                  if (localObject1 != null)
                  {
                    if (((RecyclerView.ViewHolder)localObject1).shouldIgnore()) {
                      throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                    }
                  }
                  else {
                    throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder");
                  }
                }
              }
            }
            localObject2 = localObject1;
            if (localObject1 == null)
            {
              localObject1 = getRecycledViewPool().getRecycledView(m);
              localObject2 = localObject1;
              if (localObject1 != null)
              {
                ((RecyclerView.ViewHolder)localObject1).resetInternal();
                localObject2 = localObject1;
                if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST)
                {
                  invalidateDisplayListInt((RecyclerView.ViewHolder)localObject1);
                  localObject2 = localObject1;
                }
              }
            }
            k = i;
            localObject1 = localObject2;
            if (localObject2 == null)
            {
              long l1 = getNanoTime();
              if ((paramLong != Long.MAX_VALUE) && (!mRecyclerPool.willCreateInTime(m, l1, paramLong))) {
                return null;
              }
              localObject1 = mAdapter.createViewHolder(RecyclerView.this, m);
              if (RecyclerView.ALLOW_THREAD_GAP_WORK)
              {
                localObject2 = RecyclerView.findNestedRecyclerView(itemView);
                if (localObject2 != null) {
                  mNestedRecyclerView = new WeakReference(localObject2);
                }
              }
              long l2 = getNanoTime();
              mRecyclerPool.factorInCreateTime(m, l2 - l1);
              k = i;
            }
          }
          else
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Inconsistency detected. Invalid item position ");
            ((StringBuilder)localObject2).append(paramInt);
            ((StringBuilder)localObject2).append("(offset:");
            ((StringBuilder)localObject2).append(k);
            ((StringBuilder)localObject2).append(").state:");
            ((StringBuilder)localObject2).append(mState.getItemCount());
            throw new IndexOutOfBoundsException(((StringBuilder)localObject2).toString());
          }
        }
        if ((k != 0) && (!mState.isPreLayout()) && (((RecyclerView.ViewHolder)localObject1).hasAnyOfTheFlags(8192)))
        {
          ((RecyclerView.ViewHolder)localObject1).setFlags(0, 8192);
          if (mState.mRunSimpleAnimations)
          {
            j = RecyclerView.ItemAnimator.buildAdapterChangeFlagsForAnimations((RecyclerView.ViewHolder)localObject1);
            localObject2 = mItemAnimator.recordPreLayoutInformation(mState, (RecyclerView.ViewHolder)localObject1, j | 0x1000, ((RecyclerView.ViewHolder)localObject1).getUnmodifiedPayloads());
            recordAnimationInfoIfBouncedHiddenView((RecyclerView.ViewHolder)localObject1, (RecyclerView.ItemAnimator.ItemHolderInfo)localObject2);
          }
        }
        paramBoolean = false;
        if ((mState.isPreLayout()) && (((RecyclerView.ViewHolder)localObject1).isBound())) {
          mPreLayoutPosition = paramInt;
        } else if ((!((RecyclerView.ViewHolder)localObject1).isBound()) || (((RecyclerView.ViewHolder)localObject1).needsUpdate()) || (((RecyclerView.ViewHolder)localObject1).isInvalid())) {
          paramBoolean = tryBindViewHolderByDeadline((RecyclerView.ViewHolder)localObject1, mAdapterHelper.findPositionOffset(paramInt), paramInt, paramLong);
        }
        localObject2 = itemView.getLayoutParams();
        if (localObject2 == null)
        {
          localObject2 = (RecyclerView.LayoutParams)generateDefaultLayoutParams();
          itemView.setLayoutParams((ViewGroup.LayoutParams)localObject2);
        }
        else if (!checkLayoutParams((ViewGroup.LayoutParams)localObject2))
        {
          localObject2 = (RecyclerView.LayoutParams)generateLayoutParams((ViewGroup.LayoutParams)localObject2);
          itemView.setLayoutParams((ViewGroup.LayoutParams)localObject2);
        }
        else
        {
          localObject2 = (RecyclerView.LayoutParams)localObject2;
        }
        mViewHolder = ((RecyclerView.ViewHolder)localObject1);
        if ((k != 0) && (paramBoolean)) {
          paramBoolean = bool2;
        } else {
          paramBoolean = false;
        }
        mPendingInvalidate = paramBoolean;
        return localObject1;
      }
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Invalid item position ");
      ((StringBuilder)localObject2).append(paramInt);
      ((StringBuilder)localObject2).append("(");
      ((StringBuilder)localObject2).append(paramInt);
      ((StringBuilder)localObject2).append("). Item count:");
      ((StringBuilder)localObject2).append(mState.getItemCount());
      throw new IndexOutOfBoundsException(((StringBuilder)localObject2).toString());
    }
    
    void unscrapView(RecyclerView.ViewHolder paramViewHolder)
    {
      if (RecyclerView.ViewHolder.access$900(paramViewHolder)) {
        mChangedScrap.remove(paramViewHolder);
      } else {
        mAttachedScrap.remove(paramViewHolder);
      }
      RecyclerView.ViewHolder.access$802(paramViewHolder, null);
      RecyclerView.ViewHolder.access$902(paramViewHolder, false);
      paramViewHolder.clearReturnedFromScrapFlag();
    }
    
    void updateViewCacheSize()
    {
      if (mLayout != null) {
        i = mLayout.mPrefetchMaxCountObserved;
      } else {
        i = 0;
      }
      mViewCacheMax = (mRequestedCacheMax + i);
      for (int i = mCachedViews.size() - 1; (i >= 0) && (mCachedViews.size() > mViewCacheMax); i--) {
        recycleCachedViewAt(i);
      }
    }
    
    boolean validateViewHolderForOffsetPosition(RecyclerView.ViewHolder paramViewHolder)
    {
      if (paramViewHolder.isRemoved()) {
        return mState.isPreLayout();
      }
      if ((mPosition >= 0) && (mPosition < mAdapter.getItemCount()))
      {
        boolean bool1 = mState.isPreLayout();
        boolean bool2 = false;
        if ((!bool1) && (mAdapter.getItemViewType(mPosition) != paramViewHolder.getItemViewType())) {
          return false;
        }
        if (mAdapter.hasStableIds())
        {
          if (paramViewHolder.getItemId() == mAdapter.getItemId(mPosition)) {
            bool2 = true;
          }
          return bool2;
        }
        return true;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Inconsistency detected. Invalid view holder adapter position");
      localStringBuilder.append(paramViewHolder);
      throw new IndexOutOfBoundsException(localStringBuilder.toString());
    }
    
    void viewRangeUpdate(int paramInt1, int paramInt2)
    {
      for (int i = mCachedViews.size() - 1; i >= 0; i--)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)mCachedViews.get(i);
        if (localViewHolder != null)
        {
          int j = localViewHolder.getLayoutPosition();
          if ((j >= paramInt1) && (j < paramInt1 + paramInt2))
          {
            localViewHolder.addFlags(2);
            recycleCachedViewAt(i);
          }
        }
      }
    }
  }
  
  public static abstract interface RecyclerListener
  {
    public abstract void onViewRecycled(RecyclerView.ViewHolder paramViewHolder);
  }
  
  private class RecyclerViewDataObserver
    extends RecyclerView.AdapterDataObserver
  {
    RecyclerViewDataObserver() {}
    
    public void onChanged()
    {
      assertNotInLayoutOrScroll(null);
      mState.mStructureChanged = true;
      setDataSetChangedAfterLayout();
      if (!mAdapterHelper.hasPendingUpdates()) {
        requestLayout();
      }
    }
    
    public void onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
    {
      assertNotInLayoutOrScroll(null);
      if (mAdapterHelper.onItemRangeChanged(paramInt1, paramInt2, paramObject)) {
        triggerUpdateProcessor();
      }
    }
    
    public void onItemRangeInserted(int paramInt1, int paramInt2)
    {
      assertNotInLayoutOrScroll(null);
      if (mAdapterHelper.onItemRangeInserted(paramInt1, paramInt2)) {
        triggerUpdateProcessor();
      }
    }
    
    public void onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3)
    {
      assertNotInLayoutOrScroll(null);
      if (mAdapterHelper.onItemRangeMoved(paramInt1, paramInt2, paramInt3)) {
        triggerUpdateProcessor();
      }
    }
    
    public void onItemRangeRemoved(int paramInt1, int paramInt2)
    {
      assertNotInLayoutOrScroll(null);
      if (mAdapterHelper.onItemRangeRemoved(paramInt1, paramInt2)) {
        triggerUpdateProcessor();
      }
    }
    
    void triggerUpdateProcessor()
    {
      if ((RecyclerView.POST_UPDATES_ON_ANIMATION) && (mHasFixedSize) && (mIsAttached))
      {
        postOnAnimation(mUpdateChildViewsRunnable);
      }
      else
      {
        mAdapterUpdateDuringMeasure = true;
        requestLayout();
      }
    }
  }
  
  public static class SavedState
    extends AbsSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public RecyclerView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RecyclerView.SavedState(paramAnonymousParcel);
      }
      
      public RecyclerView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new RecyclerView.SavedState[paramAnonymousInt];
      }
    };
    Parcelable mLayoutState;
    
    SavedState(Parcel paramParcel)
    {
      super();
      mLayoutState = paramParcel.readParcelable(RecyclerView.LayoutManager.class.getClassLoader());
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    void copyFrom(SavedState paramSavedState)
    {
      mLayoutState = mLayoutState;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(mLayoutState, 0);
    }
  }
  
  public static class SimpleOnItemTouchListener
    implements RecyclerView.OnItemTouchListener
  {
    public SimpleOnItemTouchListener() {}
    
    public boolean onInterceptTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public void onRequestDisallowInterceptTouchEvent(boolean paramBoolean) {}
    
    public void onTouchEvent(RecyclerView paramRecyclerView, MotionEvent paramMotionEvent) {}
  }
  
  public static abstract class SmoothScroller
  {
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean mPendingInitialRun;
    private RecyclerView mRecyclerView;
    private final Action mRecyclingAction = new Action(0, 0);
    private boolean mRunning;
    private int mTargetPosition = -1;
    private View mTargetView;
    
    public SmoothScroller() {}
    
    private void onAnimation(int paramInt1, int paramInt2)
    {
      RecyclerView localRecyclerView = mRecyclerView;
      if ((!mRunning) || (mTargetPosition == -1) || (localRecyclerView == null)) {
        stop();
      }
      mPendingInitialRun = false;
      if (mTargetView != null) {
        if (getChildPosition(mTargetView) == mTargetPosition)
        {
          onTargetFound(mTargetView, mState, mRecyclingAction);
          mRecyclingAction.runIfNecessary(localRecyclerView);
          stop();
        }
        else
        {
          Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
          mTargetView = null;
        }
      }
      if (mRunning)
      {
        onSeekTargetStep(paramInt1, paramInt2, mState, mRecyclingAction);
        boolean bool = mRecyclingAction.hasJumpTarget();
        mRecyclingAction.runIfNecessary(localRecyclerView);
        if (bool) {
          if (mRunning)
          {
            mPendingInitialRun = true;
            mViewFlinger.postOnAnimation();
          }
          else
          {
            stop();
          }
        }
      }
    }
    
    public View findViewByPosition(int paramInt)
    {
      return mRecyclerView.mLayout.findViewByPosition(paramInt);
    }
    
    public int getChildCount()
    {
      return mRecyclerView.mLayout.getChildCount();
    }
    
    public int getChildPosition(View paramView)
    {
      return mRecyclerView.getChildLayoutPosition(paramView);
    }
    
    public RecyclerView.LayoutManager getLayoutManager()
    {
      return mLayoutManager;
    }
    
    public int getTargetPosition()
    {
      return mTargetPosition;
    }
    
    @Deprecated
    public void instantScrollToPosition(int paramInt)
    {
      mRecyclerView.scrollToPosition(paramInt);
    }
    
    public boolean isPendingInitialRun()
    {
      return mPendingInitialRun;
    }
    
    public boolean isRunning()
    {
      return mRunning;
    }
    
    protected void normalize(PointF paramPointF)
    {
      double d = Math.sqrt(x * x + y * y);
      x = ((float)(x / d));
      y = ((float)(y / d));
    }
    
    protected void onChildAttachedToWindow(View paramView)
    {
      if (getChildPosition(paramView) == getTargetPosition()) {
        mTargetView = paramView;
      }
    }
    
    protected abstract void onSeekTargetStep(int paramInt1, int paramInt2, RecyclerView.State paramState, Action paramAction);
    
    protected abstract void onStart();
    
    protected abstract void onStop();
    
    protected abstract void onTargetFound(View paramView, RecyclerView.State paramState, Action paramAction);
    
    public void setTargetPosition(int paramInt)
    {
      mTargetPosition = paramInt;
    }
    
    void start(RecyclerView paramRecyclerView, RecyclerView.LayoutManager paramLayoutManager)
    {
      mRecyclerView = paramRecyclerView;
      mLayoutManager = paramLayoutManager;
      if (mTargetPosition != -1)
      {
        RecyclerView.State.access$1102(mRecyclerView.mState, mTargetPosition);
        mRunning = true;
        mPendingInitialRun = true;
        mTargetView = findViewByPosition(getTargetPosition());
        onStart();
        mRecyclerView.mViewFlinger.postOnAnimation();
        return;
      }
      throw new IllegalArgumentException("Invalid target position");
    }
    
    protected final void stop()
    {
      if (!mRunning) {
        return;
      }
      onStop();
      RecyclerView.State.access$1102(mRecyclerView.mState, -1);
      mTargetView = null;
      mTargetPosition = -1;
      mPendingInitialRun = false;
      mRunning = false;
      mLayoutManager.onSmoothScrollerStopped(this);
      mLayoutManager = null;
      mRecyclerView = null;
    }
    
    public static class Action
    {
      public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
      private boolean mChanged = false;
      private int mConsecutiveUpdates = 0;
      private int mDuration;
      private int mDx;
      private int mDy;
      private Interpolator mInterpolator;
      private int mJumpToPosition = -1;
      
      public Action(int paramInt1, int paramInt2)
      {
        this(paramInt1, paramInt2, Integer.MIN_VALUE, null);
      }
      
      public Action(int paramInt1, int paramInt2, int paramInt3)
      {
        this(paramInt1, paramInt2, paramInt3, null);
      }
      
      public Action(int paramInt1, int paramInt2, int paramInt3, Interpolator paramInterpolator)
      {
        mDx = paramInt1;
        mDy = paramInt2;
        mDuration = paramInt3;
        mInterpolator = paramInterpolator;
      }
      
      private void validate()
      {
        if ((mInterpolator != null) && (mDuration < 1)) {
          throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
        }
        if (mDuration >= 1) {
          return;
        }
        throw new IllegalStateException("Scroll duration must be a positive number");
      }
      
      public int getDuration()
      {
        return mDuration;
      }
      
      public int getDx()
      {
        return mDx;
      }
      
      public int getDy()
      {
        return mDy;
      }
      
      public Interpolator getInterpolator()
      {
        return mInterpolator;
      }
      
      boolean hasJumpTarget()
      {
        boolean bool;
        if (mJumpToPosition >= 0) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      
      public void jumpTo(int paramInt)
      {
        mJumpToPosition = paramInt;
      }
      
      void runIfNecessary(RecyclerView paramRecyclerView)
      {
        if (mJumpToPosition >= 0)
        {
          int i = mJumpToPosition;
          mJumpToPosition = -1;
          paramRecyclerView.jumpToPositionForSmoothScroller(i);
          mChanged = false;
          return;
        }
        if (mChanged)
        {
          validate();
          if (mInterpolator == null)
          {
            if (mDuration == Integer.MIN_VALUE) {
              mViewFlinger.smoothScrollBy(mDx, mDy);
            } else {
              mViewFlinger.smoothScrollBy(mDx, mDy, mDuration);
            }
          }
          else {
            mViewFlinger.smoothScrollBy(mDx, mDy, mDuration, mInterpolator);
          }
          mConsecutiveUpdates += 1;
          if (mConsecutiveUpdates > 10) {
            Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
          }
          mChanged = false;
        }
        else
        {
          mConsecutiveUpdates = 0;
        }
      }
      
      public void setDuration(int paramInt)
      {
        mChanged = true;
        mDuration = paramInt;
      }
      
      public void setDx(int paramInt)
      {
        mChanged = true;
        mDx = paramInt;
      }
      
      public void setDy(int paramInt)
      {
        mChanged = true;
        mDy = paramInt;
      }
      
      public void setInterpolator(Interpolator paramInterpolator)
      {
        mChanged = true;
        mInterpolator = paramInterpolator;
      }
      
      public void update(int paramInt1, int paramInt2, int paramInt3, Interpolator paramInterpolator)
      {
        mDx = paramInt1;
        mDy = paramInt2;
        mDuration = paramInt3;
        mInterpolator = paramInterpolator;
        mChanged = true;
      }
    }
    
    public static abstract interface ScrollVectorProvider
    {
      public abstract PointF computeScrollVectorForPosition(int paramInt);
    }
  }
  
  public static class State
  {
    static final int STEP_ANIMATIONS = 4;
    static final int STEP_LAYOUT = 2;
    static final int STEP_START = 1;
    private SparseArray<Object> mData;
    int mDeletedInvisibleItemCountSincePreviousLayout = 0;
    long mFocusedItemId;
    int mFocusedItemPosition;
    int mFocusedSubChildId;
    boolean mInPreLayout = false;
    boolean mIsMeasuring = false;
    int mItemCount = 0;
    int mLayoutStep = 1;
    int mPreviousLayoutItemCount = 0;
    boolean mRunPredictiveAnimations = false;
    boolean mRunSimpleAnimations = false;
    boolean mStructureChanged = false;
    private int mTargetPosition = -1;
    boolean mTrackOldChangeHolders = false;
    
    public State() {}
    
    void assertLayoutStep(int paramInt)
    {
      if ((mLayoutStep & paramInt) != 0) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Layout state should be one of ");
      localStringBuilder.append(Integer.toBinaryString(paramInt));
      localStringBuilder.append(" but it is ");
      localStringBuilder.append(Integer.toBinaryString(mLayoutStep));
      throw new IllegalStateException(localStringBuilder.toString());
    }
    
    public boolean didStructureChange()
    {
      return mStructureChanged;
    }
    
    public <T> T get(int paramInt)
    {
      if (mData == null) {
        return null;
      }
      return mData.get(paramInt);
    }
    
    public int getItemCount()
    {
      int i;
      if (mInPreLayout) {
        i = mPreviousLayoutItemCount - mDeletedInvisibleItemCountSincePreviousLayout;
      } else {
        i = mItemCount;
      }
      return i;
    }
    
    public int getTargetScrollPosition()
    {
      return mTargetPosition;
    }
    
    public boolean hasTargetScrollPosition()
    {
      boolean bool;
      if (mTargetPosition != -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isMeasuring()
    {
      return mIsMeasuring;
    }
    
    public boolean isPreLayout()
    {
      return mInPreLayout;
    }
    
    void prepareForNestedPrefetch(RecyclerView.Adapter paramAdapter)
    {
      mLayoutStep = 1;
      mItemCount = paramAdapter.getItemCount();
      mStructureChanged = false;
      mInPreLayout = false;
      mTrackOldChangeHolders = false;
      mIsMeasuring = false;
    }
    
    public void put(int paramInt, Object paramObject)
    {
      if (mData == null) {
        mData = new SparseArray();
      }
      mData.put(paramInt, paramObject);
    }
    
    public void remove(int paramInt)
    {
      if (mData == null) {
        return;
      }
      mData.remove(paramInt);
    }
    
    State reset()
    {
      mTargetPosition = -1;
      if (mData != null) {
        mData.clear();
      }
      mItemCount = 0;
      mStructureChanged = false;
      mIsMeasuring = false;
      return this;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("State{mTargetPosition=");
      localStringBuilder.append(mTargetPosition);
      localStringBuilder.append(", mData=");
      localStringBuilder.append(mData);
      localStringBuilder.append(", mItemCount=");
      localStringBuilder.append(mItemCount);
      localStringBuilder.append(", mPreviousLayoutItemCount=");
      localStringBuilder.append(mPreviousLayoutItemCount);
      localStringBuilder.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
      localStringBuilder.append(mDeletedInvisibleItemCountSincePreviousLayout);
      localStringBuilder.append(", mStructureChanged=");
      localStringBuilder.append(mStructureChanged);
      localStringBuilder.append(", mInPreLayout=");
      localStringBuilder.append(mInPreLayout);
      localStringBuilder.append(", mRunSimpleAnimations=");
      localStringBuilder.append(mRunSimpleAnimations);
      localStringBuilder.append(", mRunPredictiveAnimations=");
      localStringBuilder.append(mRunPredictiveAnimations);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public boolean willRunPredictiveAnimations()
    {
      return mRunPredictiveAnimations;
    }
    
    public boolean willRunSimpleAnimations()
    {
      return mRunSimpleAnimations;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    static @interface LayoutState {}
  }
  
  public static abstract class ViewCacheExtension
  {
    public ViewCacheExtension() {}
    
    public abstract View getViewForPositionAndType(RecyclerView.Recycler paramRecycler, int paramInt1, int paramInt2);
  }
  
  class ViewFlinger
    implements Runnable
  {
    private boolean mEatRunOnAnimationRequest = false;
    Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
    private int mLastFlingX;
    private int mLastFlingY;
    private boolean mReSchedulePostAnimationCallback = false;
    private OverScroller mScroller = new OverScroller(getContext(), RecyclerView.sQuinticInterpolator);
    
    ViewFlinger() {}
    
    private int computeScrollDuration(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = Math.abs(paramInt1);
      int j = Math.abs(paramInt2);
      int k;
      if (i > j) {
        k = 1;
      } else {
        k = 0;
      }
      paramInt3 = (int)Math.sqrt(paramInt3 * paramInt3 + paramInt4 * paramInt4);
      paramInt2 = (int)Math.sqrt(paramInt1 * paramInt1 + paramInt2 * paramInt2);
      if (k != 0) {
        paramInt1 = getWidth();
      } else {
        paramInt1 = getHeight();
      }
      paramInt4 = paramInt1 / 2;
      float f1 = Math.min(1.0F, paramInt2 * 1.0F / paramInt1);
      float f2 = paramInt4;
      float f3 = paramInt4;
      f1 = distanceInfluenceForSnapDuration(f1);
      if (paramInt3 > 0)
      {
        paramInt1 = Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramInt3)) * 4;
      }
      else
      {
        if (k != 0) {
          paramInt2 = i;
        } else {
          paramInt2 = j;
        }
        paramInt1 = (int)((paramInt2 / paramInt1 + 1.0F) * 300.0F);
      }
      return Math.min(paramInt1, 2000);
    }
    
    private void disableRunOnAnimationRequests()
    {
      mReSchedulePostAnimationCallback = false;
      mEatRunOnAnimationRequest = true;
    }
    
    private float distanceInfluenceForSnapDuration(float paramFloat)
    {
      return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
    }
    
    private void enableRunOnAnimationRequests()
    {
      mEatRunOnAnimationRequest = false;
      if (mReSchedulePostAnimationCallback) {
        postOnAnimation();
      }
    }
    
    public void fling(int paramInt1, int paramInt2)
    {
      setScrollState(2);
      mLastFlingY = 0;
      mLastFlingX = 0;
      mScroller.fling(0, 0, paramInt1, paramInt2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
      postOnAnimation();
    }
    
    void postOnAnimation()
    {
      if (mEatRunOnAnimationRequest)
      {
        mReSchedulePostAnimationCallback = true;
      }
      else
      {
        removeCallbacks(this);
        postOnAnimation(this);
      }
    }
    
    public void run()
    {
      if (mLayout == null)
      {
        stop();
        return;
      }
      disableRunOnAnimationRequests();
      consumePendingUpdateOperations();
      OverScroller localOverScroller = mScroller;
      RecyclerView.SmoothScroller localSmoothScroller = mLayout.mSmoothScroller;
      if (localOverScroller.computeScrollOffset())
      {
        int i = localOverScroller.getCurrX();
        int j = localOverScroller.getCurrY();
        int k = i - mLastFlingX;
        int m = j - mLastFlingY;
        int n = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        mLastFlingX = i;
        mLastFlingY = j;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        if (mAdapter != null)
        {
          eatRequestLayout();
          onEnterLayoutOrScroll();
          Trace.beginSection("RV Scroll");
          if (k != 0)
          {
            i1 = mLayout.scrollHorizontallyBy(k, mRecycler, mState);
            i5 = k - i1;
          }
          if (m != 0)
          {
            i3 = mLayout.scrollVerticallyBy(m, mRecycler, mState);
            i7 = m - i3;
          }
          Trace.endSection();
          repositionShadowingViews();
          onExitLayoutOrScroll();
          resumeRequestLayout(false);
          n = i1;
          i2 = i3;
          i4 = i5;
          i6 = i7;
          if (localSmoothScroller != null)
          {
            n = i1;
            i2 = i3;
            i4 = i5;
            i6 = i7;
            if (!localSmoothScroller.isPendingInitialRun())
            {
              n = i1;
              i2 = i3;
              i4 = i5;
              i6 = i7;
              if (localSmoothScroller.isRunning())
              {
                n = mState.getItemCount();
                if (n == 0)
                {
                  localSmoothScroller.stop();
                  n = i1;
                  i2 = i3;
                  i4 = i5;
                  i6 = i7;
                }
                else if (localSmoothScroller.getTargetPosition() >= n)
                {
                  localSmoothScroller.setTargetPosition(n - 1);
                  localSmoothScroller.onAnimation(k - i5, m - i7);
                  n = i1;
                  i2 = i3;
                  i4 = i5;
                  i6 = i7;
                }
                else
                {
                  localSmoothScroller.onAnimation(k - i5, m - i7);
                  i6 = i7;
                  i4 = i5;
                  i2 = i3;
                  n = i1;
                }
              }
            }
          }
        }
        if (!mItemDecorations.isEmpty()) {
          invalidate();
        }
        if (getOverScrollMode() != 2) {
          considerReleasingGlowsOnScroll(k, m);
        }
        if ((i4 != 0) || (i6 != 0))
        {
          i5 = (int)localOverScroller.getCurrVelocity();
          i3 = 0;
          if (i4 != i)
          {
            if (i4 < 0) {
              i1 = -i5;
            } else if (i4 > 0) {
              i1 = i5;
            } else {
              i1 = 0;
            }
            i3 = i1;
          }
          i1 = 0;
          if (i6 != j) {
            if (i6 < 0) {
              i1 = -i5;
            } else if (i6 > 0) {
              i1 = i5;
            } else {
              i1 = 0;
            }
          }
          if (getOverScrollMode() != 2) {
            absorbGlows(i3, i1);
          }
          if (((i3 != 0) || (i4 == i) || (localOverScroller.getFinalX() == 0)) && ((i1 != 0) || (i6 == j) || (localOverScroller.getFinalY() == 0))) {
            localOverScroller.abortAnimation();
          }
        }
        if ((n != 0) || (i2 != 0)) {
          dispatchOnScrolled(n, i2);
        }
        if (!awakenScrollBars()) {
          invalidate();
        }
        i7 = 1;
        if ((m != 0) && (mLayout.canScrollVertically()) && (i2 == m)) {
          i1 = 1;
        } else {
          i1 = 0;
        }
        if ((k != 0) && (mLayout.canScrollHorizontally()) && (n == k)) {
          i5 = 1;
        } else {
          i5 = 0;
        }
        if (k == 0)
        {
          i3 = i7;
          if (m == 0) {}
        }
        else
        {
          i3 = i7;
          if (i5 == 0) {
            if (i1 != 0) {
              i3 = i7;
            } else {
              i3 = 0;
            }
          }
        }
        if ((!localOverScroller.isFinished()) && (i3 != 0))
        {
          postOnAnimation();
          if (mGapWorker != null) {
            mGapWorker.postFromTraversal(RecyclerView.this, k, m);
          }
        }
        else
        {
          setScrollState(0);
          if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            mPrefetchRegistry.clearPrefetchPositions();
          }
        }
      }
      if (localSmoothScroller != null)
      {
        if (localSmoothScroller.isPendingInitialRun()) {
          localSmoothScroller.onAnimation(0, 0);
        }
        if (!mReSchedulePostAnimationCallback) {
          localSmoothScroller.stop();
        }
      }
      enableRunOnAnimationRequests();
    }
    
    public void smoothScrollBy(int paramInt1, int paramInt2)
    {
      smoothScrollBy(paramInt1, paramInt2, 0, 0);
    }
    
    public void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3)
    {
      smoothScrollBy(paramInt1, paramInt2, paramInt3, RecyclerView.sQuinticInterpolator);
    }
    
    public void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      smoothScrollBy(paramInt1, paramInt2, computeScrollDuration(paramInt1, paramInt2, paramInt3, paramInt4));
    }
    
    public void smoothScrollBy(int paramInt1, int paramInt2, int paramInt3, Interpolator paramInterpolator)
    {
      if (mInterpolator != paramInterpolator)
      {
        mInterpolator = paramInterpolator;
        mScroller = new OverScroller(getContext(), paramInterpolator);
      }
      setScrollState(2);
      mLastFlingY = 0;
      mLastFlingX = 0;
      mScroller.startScroll(0, 0, paramInt1, paramInt2, paramInt3);
      postOnAnimation();
    }
    
    public void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator)
    {
      int i = computeScrollDuration(paramInt1, paramInt2, 0, 0);
      if (paramInterpolator == null) {
        paramInterpolator = RecyclerView.sQuinticInterpolator;
      }
      smoothScrollBy(paramInt1, paramInt2, i, paramInterpolator);
    }
    
    public void stop()
    {
      removeCallbacks(this);
      mScroller.abortAnimation();
    }
  }
  
  public static abstract class ViewHolder
  {
    static final int FLAG_ADAPTER_FULLUPDATE = 1024;
    static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
    static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
    static final int FLAG_BOUND = 1;
    static final int FLAG_IGNORE = 128;
    static final int FLAG_INVALID = 4;
    static final int FLAG_MOVED = 2048;
    static final int FLAG_NOT_RECYCLABLE = 16;
    static final int FLAG_REMOVED = 8;
    static final int FLAG_RETURNED_FROM_SCRAP = 32;
    static final int FLAG_TMP_DETACHED = 256;
    static final int FLAG_UPDATE = 2;
    private static final List<Object> FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
    static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
    public final View itemView;
    private int mFlags;
    private boolean mInChangeScrap = false;
    private int mIsRecyclableCount = 0;
    long mItemId = -1L;
    int mItemViewType = -1;
    WeakReference<RecyclerView> mNestedRecyclerView;
    int mOldPosition = -1;
    RecyclerView mOwnerRecyclerView;
    List<Object> mPayloads = null;
    @VisibleForTesting
    int mPendingAccessibilityState = -1;
    int mPosition = -1;
    int mPreLayoutPosition = -1;
    private RecyclerView.Recycler mScrapContainer = null;
    ViewHolder mShadowedHolder = null;
    ViewHolder mShadowingHolder = null;
    List<Object> mUnmodifiedPayloads = null;
    private int mWasImportantForAccessibilityBeforeHidden = 0;
    
    public ViewHolder(View paramView)
    {
      if (paramView != null)
      {
        itemView = paramView;
        return;
      }
      throw new IllegalArgumentException("itemView may not be null");
    }
    
    private void createPayloadsIfNeeded()
    {
      if (mPayloads == null)
      {
        mPayloads = new ArrayList();
        mUnmodifiedPayloads = Collections.unmodifiableList(mPayloads);
      }
    }
    
    private boolean doesTransientStatePreventRecycling()
    {
      boolean bool;
      if (((mFlags & 0x10) == 0) && (itemView.hasTransientState())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void onEnteredHiddenState(RecyclerView paramRecyclerView)
    {
      mWasImportantForAccessibilityBeforeHidden = itemView.getImportantForAccessibility();
      paramRecyclerView.setChildImportantForAccessibilityInternal(this, 4);
    }
    
    private void onLeftHiddenState(RecyclerView paramRecyclerView)
    {
      paramRecyclerView.setChildImportantForAccessibilityInternal(this, mWasImportantForAccessibilityBeforeHidden);
      mWasImportantForAccessibilityBeforeHidden = 0;
    }
    
    private boolean shouldBeKeptAsChild()
    {
      boolean bool;
      if ((mFlags & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void addChangePayload(Object paramObject)
    {
      if (paramObject == null)
      {
        addFlags(1024);
      }
      else if ((0x400 & mFlags) == 0)
      {
        createPayloadsIfNeeded();
        mPayloads.add(paramObject);
      }
    }
    
    void addFlags(int paramInt)
    {
      mFlags |= paramInt;
    }
    
    void clearOldPosition()
    {
      mOldPosition = -1;
      mPreLayoutPosition = -1;
    }
    
    void clearPayload()
    {
      if (mPayloads != null) {
        mPayloads.clear();
      }
      mFlags &= 0xFBFF;
    }
    
    void clearReturnedFromScrapFlag()
    {
      mFlags &= 0xFFFFFFDF;
    }
    
    void clearTmpDetachFlag()
    {
      mFlags &= 0xFEFF;
    }
    
    void flagRemovedAndOffsetPosition(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      addFlags(8);
      offsetPosition(paramInt2, paramBoolean);
      mPosition = paramInt1;
    }
    
    public final int getAdapterPosition()
    {
      if (mOwnerRecyclerView == null) {
        return -1;
      }
      return mOwnerRecyclerView.getAdapterPositionFor(this);
    }
    
    public final long getItemId()
    {
      return mItemId;
    }
    
    public final int getItemViewType()
    {
      return mItemViewType;
    }
    
    public final int getLayoutPosition()
    {
      int i;
      if (mPreLayoutPosition == -1) {
        i = mPosition;
      } else {
        i = mPreLayoutPosition;
      }
      return i;
    }
    
    public final int getOldPosition()
    {
      return mOldPosition;
    }
    
    @Deprecated
    public final int getPosition()
    {
      int i;
      if (mPreLayoutPosition == -1) {
        i = mPosition;
      } else {
        i = mPreLayoutPosition;
      }
      return i;
    }
    
    List<Object> getUnmodifiedPayloads()
    {
      if ((mFlags & 0x400) == 0)
      {
        if ((mPayloads != null) && (mPayloads.size() != 0)) {
          return mUnmodifiedPayloads;
        }
        return FULLUPDATE_PAYLOADS;
      }
      return FULLUPDATE_PAYLOADS;
    }
    
    boolean hasAnyOfTheFlags(int paramInt)
    {
      boolean bool;
      if ((mFlags & paramInt) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    boolean isAdapterPositionUnknown()
    {
      boolean bool;
      if (((mFlags & 0x200) == 0) && (!isInvalid())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    boolean isBound()
    {
      int i = mFlags;
      boolean bool = true;
      if ((i & 0x1) == 0) {
        bool = false;
      }
      return bool;
    }
    
    boolean isInvalid()
    {
      boolean bool;
      if ((mFlags & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public final boolean isRecyclable()
    {
      boolean bool;
      if (((mFlags & 0x10) == 0) && (!itemView.hasTransientState())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    boolean isRemoved()
    {
      boolean bool;
      if ((mFlags & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    boolean isScrap()
    {
      boolean bool;
      if (mScrapContainer != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    boolean isTmpDetached()
    {
      boolean bool;
      if ((mFlags & 0x100) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    boolean isUpdated()
    {
      boolean bool;
      if ((mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    boolean needsUpdate()
    {
      boolean bool;
      if ((mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void offsetPosition(int paramInt, boolean paramBoolean)
    {
      if (mOldPosition == -1) {
        mOldPosition = mPosition;
      }
      if (mPreLayoutPosition == -1) {
        mPreLayoutPosition = mPosition;
      }
      if (paramBoolean) {
        mPreLayoutPosition += paramInt;
      }
      mPosition += paramInt;
      if (itemView.getLayoutParams() != null) {
        itemView.getLayoutParams()).mInsetsDirty = true;
      }
    }
    
    void resetInternal()
    {
      mFlags = 0;
      mPosition = -1;
      mOldPosition = -1;
      mItemId = -1L;
      mPreLayoutPosition = -1;
      mIsRecyclableCount = 0;
      mShadowedHolder = null;
      mShadowingHolder = null;
      clearPayload();
      mWasImportantForAccessibilityBeforeHidden = 0;
      mPendingAccessibilityState = -1;
      RecyclerView.clearNestedRecyclerViewIfNotNested(this);
    }
    
    void saveOldPosition()
    {
      if (mOldPosition == -1) {
        mOldPosition = mPosition;
      }
    }
    
    void setFlags(int paramInt1, int paramInt2)
    {
      mFlags = (mFlags & paramInt2 | paramInt1 & paramInt2);
    }
    
    public final void setIsRecyclable(boolean paramBoolean)
    {
      int i;
      if (paramBoolean) {
        i = mIsRecyclableCount - 1;
      } else {
        i = mIsRecyclableCount + 1;
      }
      mIsRecyclableCount = i;
      if (mIsRecyclableCount < 0)
      {
        mIsRecyclableCount = 0;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for ");
        localStringBuilder.append(this);
        Log.e("View", localStringBuilder.toString());
      }
      else if ((!paramBoolean) && (mIsRecyclableCount == 1))
      {
        mFlags |= 0x10;
      }
      else if ((paramBoolean) && (mIsRecyclableCount == 0))
      {
        mFlags &= 0xFFFFFFEF;
      }
    }
    
    void setScrapContainer(RecyclerView.Recycler paramRecycler, boolean paramBoolean)
    {
      mScrapContainer = paramRecycler;
      mInChangeScrap = paramBoolean;
    }
    
    boolean shouldIgnore()
    {
      boolean bool;
      if ((mFlags & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void stopIgnoring()
    {
      mFlags &= 0xFF7F;
    }
    
    public String toString()
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("ViewHolder{");
      ((StringBuilder)localObject).append(Integer.toHexString(hashCode()));
      ((StringBuilder)localObject).append(" position=");
      ((StringBuilder)localObject).append(mPosition);
      ((StringBuilder)localObject).append(" id=");
      ((StringBuilder)localObject).append(mItemId);
      ((StringBuilder)localObject).append(", oldPos=");
      ((StringBuilder)localObject).append(mOldPosition);
      ((StringBuilder)localObject).append(", pLpos:");
      ((StringBuilder)localObject).append(mPreLayoutPosition);
      StringBuilder localStringBuilder = new StringBuilder(((StringBuilder)localObject).toString());
      if (isScrap())
      {
        localStringBuilder.append(" scrap ");
        if (mInChangeScrap) {
          localObject = "[changeScrap]";
        } else {
          localObject = "[attachedScrap]";
        }
        localStringBuilder.append((String)localObject);
      }
      if (isInvalid()) {
        localStringBuilder.append(" invalid");
      }
      if (!isBound()) {
        localStringBuilder.append(" unbound");
      }
      if (needsUpdate()) {
        localStringBuilder.append(" update");
      }
      if (isRemoved()) {
        localStringBuilder.append(" removed");
      }
      if (shouldIgnore()) {
        localStringBuilder.append(" ignored");
      }
      if (isTmpDetached()) {
        localStringBuilder.append(" tmpDetached");
      }
      if (!isRecyclable())
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(" not recyclable(");
        ((StringBuilder)localObject).append(mIsRecyclableCount);
        ((StringBuilder)localObject).append(")");
        localStringBuilder.append(((StringBuilder)localObject).toString());
      }
      if (isAdapterPositionUnknown()) {
        localStringBuilder.append(" undefined adapter position");
      }
      if (itemView.getParent() == null) {
        localStringBuilder.append(" no parent");
      }
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    void unScrap()
    {
      mScrapContainer.unscrapView(this);
    }
    
    boolean wasReturnedFromScrap()
    {
      boolean bool;
      if ((mFlags & 0x20) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
}
