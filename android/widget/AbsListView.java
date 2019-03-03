package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.os.StrictMode.Span;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.StateSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.BaseSavedState;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsListView
  extends AdapterView<ListAdapter>
  implements TextWatcher, ViewTreeObserver.OnGlobalLayoutListener, Filter.FilterListener, ViewTreeObserver.OnTouchModeChangeListener, RemoteViewsAdapter.RemoteAdapterConnectionCallback
{
  private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;
  public static final int CHOICE_MODE_MULTIPLE = 2;
  public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;
  public static final int CHOICE_MODE_NONE = 0;
  public static final int CHOICE_MODE_SINGLE = 1;
  private static final int INVALID_POINTER = -1;
  static final int LAYOUT_FORCE_BOTTOM = 3;
  static final int LAYOUT_FORCE_TOP = 1;
  static final int LAYOUT_MOVE_SELECTION = 6;
  static final int LAYOUT_NORMAL = 0;
  static final int LAYOUT_SET_SELECTION = 2;
  static final int LAYOUT_SPECIFIC = 4;
  static final int LAYOUT_SYNC = 5;
  private static final double MOVE_TOUCH_SLOP = 0.6D;
  private static final boolean OPTS_INPUT = true;
  static final int OVERSCROLL_LIMIT_DIVISOR = 3;
  private static final boolean PROFILE_FLINGING = false;
  private static final boolean PROFILE_SCROLLING = false;
  private static final String TAG = "AbsListView";
  static final int TOUCH_MODE_DONE_WAITING = 2;
  static final int TOUCH_MODE_DOWN = 0;
  static final int TOUCH_MODE_FLING = 4;
  private static final int TOUCH_MODE_OFF = 1;
  private static final int TOUCH_MODE_ON = 0;
  static final int TOUCH_MODE_OVERFLING = 6;
  static final int TOUCH_MODE_OVERSCROLL = 5;
  static final int TOUCH_MODE_REST = -1;
  static final int TOUCH_MODE_SCROLL = 3;
  static final int TOUCH_MODE_TAP = 1;
  private static final int TOUCH_MODE_UNKNOWN = -1;
  private static final double TOUCH_SLOP_MAX = 1.0D;
  private static final double TOUCH_SLOP_MIN = 0.6D;
  public static final int TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;
  public static final int TRANSCRIPT_MODE_DISABLED = 0;
  public static final int TRANSCRIPT_MODE_NORMAL = 1;
  static final Interpolator sLinearInterpolator = new LinearInterpolator();
  private final boolean PREOBTAIN_ENABLE = SystemProperties.getBoolean("ro.vendor.scroll.preobtain.enable", false);
  private ListItemAccessibilityDelegate mAccessibilityDelegate;
  private int mActivePointerId = -1;
  ListAdapter mAdapter;
  boolean mAdapterHasStableIds;
  private int mCacheColorHint;
  boolean mCachingActive;
  boolean mCachingStarted;
  SparseBooleanArray mCheckStates;
  LongSparseArray<Integer> mCheckedIdStates;
  int mCheckedItemCount;
  ActionMode mChoiceActionMode;
  int mChoiceMode = 0;
  private Runnable mClearScrollingCache;
  private ContextMenu.ContextMenuInfo mContextMenuInfo = null;
  AdapterDataSetObserver mDataSetObserver;
  private InputConnection mDefInputConnection;
  private boolean mDeferNotifyDataSetChanged = false;
  private float mDensityScale;
  private int mDirection = 0;
  boolean mDrawSelectorOnTop = false;
  private EdgeEffect mEdgeGlowBottom;
  private EdgeEffect mEdgeGlowTop;
  private FastScroller mFastScroll;
  boolean mFastScrollAlwaysVisible;
  boolean mFastScrollEnabled;
  private int mFastScrollStyle;
  private boolean mFiltered;
  private int mFirstPositionDistanceGuess;
  private boolean mFlingProfilingStarted = false;
  private FlingRunnable mFlingRunnable;
  private StrictMode.Span mFlingStrictSpan = null;
  private boolean mForceTranscriptScroll;
  private boolean mGlobalLayoutListenerAddedFilter;
  private boolean mHasPerformedLongPress;
  private boolean mIsChildViewEnabled;
  private boolean mIsDetaching;
  private boolean mIsFirstTouchMoveEvent = false;
  final boolean[] mIsScrap = new boolean[1];
  private int mLastAccessibilityScrollEventFromIndex;
  private int mLastAccessibilityScrollEventToIndex;
  private int mLastHandledItemCount;
  private int mLastPositionDistanceGuess;
  private int mLastScrollState = 0;
  private int mLastTouchMode = -1;
  int mLastY;
  int mLayoutMode = 0;
  Rect mListPadding = new Rect();
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  int mMotionCorrection;
  int mMotionPosition;
  int mMotionViewNewTop;
  int mMotionViewOriginalTop;
  int mMotionX;
  int mMotionY;
  private int mMoveAcceleration;
  MultiChoiceModeWrapper mMultiChoiceModeCallback;
  private int mNestedYOffset = 0;
  private int mNumTouchMoveEvent = 0;
  int mOldIncrementalDeltaY;
  private OnScrollListener mOnScrollListener;
  int mOverflingDistance;
  int mOverscrollDistance;
  int mOverscrollMax;
  private final Thread mOwnerThread;
  private CheckForKeyLongPress mPendingCheckForKeyLongPress;
  private CheckForLongPress mPendingCheckForLongPress;
  private CheckForTap mPendingCheckForTap;
  private SavedState mPendingSync;
  private PerformClick mPerformClick;
  PopupWindow mPopup;
  private boolean mPopupHidden;
  Runnable mPositionScrollAfterLayout;
  AbsPositionScroller mPositionScroller;
  private InputConnectionWrapper mPublicInputConnection;
  final RecycleBin mRecycler = new RecycleBin();
  private RemoteViewsAdapter mRemoteAdapter;
  int mResurrectToPosition = -1;
  private final int[] mScrollConsumed = new int[2];
  View mScrollDown;
  private final int[] mScrollOffset = new int[2];
  private boolean mScrollProfilingStarted = false;
  private StrictMode.Span mScrollStrictSpan = null;
  View mScrollUp;
  boolean mScrollingCacheEnabled;
  int mSelectedTop = 0;
  int mSelectionBottomPadding = 0;
  int mSelectionLeftPadding = 0;
  int mSelectionRightPadding = 0;
  int mSelectionTopPadding = 0;
  Drawable mSelector;
  int mSelectorPosition = -1;
  Rect mSelectorRect = new Rect();
  private int[] mSelectorState;
  private boolean mSmoothScrollbarEnabled = true;
  boolean mStackFromBottom;
  EditText mTextFilter;
  private boolean mTextFilterEnabled;
  private final float[] mTmpPoint = new float[2];
  private Rect mTouchFrame;
  int mTouchMode = -1;
  private Runnable mTouchModeReset;
  private int mTouchSlop;
  private int mTranscriptMode;
  private float mVelocityScale = 1.0F;
  private VelocityTracker mVelocityTracker;
  private float mVerticalScrollFactor;
  int mWidthMeasureSpec = 0;
  
  public AbsListView(Context paramContext)
  {
    super(paramContext);
    initAbsListView();
    mOwnerThread = Thread.currentThread();
    setVerticalScrollBarEnabled(true);
    paramContext = paramContext.obtainStyledAttributes(R.styleable.View);
    initializeScrollbarsInternal(paramContext);
    paramContext.recycle();
  }
  
  public AbsListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842858);
  }
  
  public AbsListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AbsListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initAbsListView();
    mOwnerThread = Thread.currentThread();
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AbsListView, paramInt1, paramInt2);
    paramAttributeSet = localTypedArray.getDrawable(0);
    if (paramAttributeSet != null) {
      setSelector(paramAttributeSet);
    }
    mDrawSelectorOnTop = localTypedArray.getBoolean(1, false);
    setStackFromBottom(localTypedArray.getBoolean(2, false));
    setScrollingCacheEnabled(localTypedArray.getBoolean(3, true));
    setTextFilterEnabled(localTypedArray.getBoolean(4, false));
    setTranscriptMode(localTypedArray.getInt(5, 0));
    setCacheColorHint(localTypedArray.getColor(6, 0));
    setSmoothScrollbarEnabled(localTypedArray.getBoolean(9, true));
    setChoiceMode(localTypedArray.getInt(7, 0));
    setFastScrollEnabled(localTypedArray.getBoolean(8, false));
    setFastScrollStyle(localTypedArray.getResourceId(11, 0));
    setFastScrollAlwaysVisible(localTypedArray.getBoolean(10, false));
    localTypedArray.recycle();
    if (getResourcesgetConfigurationuiMode == 6) {
      setRevealOnFocusHint(false);
    }
  }
  
  private boolean acceptFilter()
  {
    boolean bool;
    if ((mTextFilterEnabled) && ((getAdapter() instanceof Filterable)) && (((Filterable)getAdapter()).getFilter() != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean canScrollDown()
  {
    int i = getChildCount();
    int j = mFirstPosition;
    int k = mItemCount;
    boolean bool1 = false;
    boolean bool2;
    if (j + i < k) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3 = bool2;
    if (!bool2)
    {
      bool3 = bool2;
      if (i > 0)
      {
        bool2 = bool1;
        if (getChildAt(i - 1).getBottom() > mBottom - mListPadding.bottom) {
          bool2 = true;
        }
        bool3 = bool2;
      }
    }
    return bool3;
  }
  
  private boolean canScrollUp()
  {
    int i = mFirstPosition;
    boolean bool1 = true;
    boolean bool2;
    if (i > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3 = bool2;
    if (!bool2)
    {
      bool3 = bool2;
      if (getChildCount() > 0)
      {
        if (getChildAt(0).getTop() < mListPadding.top) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
        bool3 = bool2;
      }
    }
    return bool3;
  }
  
  private void clearScrollingCache()
  {
    if (!isHardwareAccelerated())
    {
      if (mClearScrollingCache == null) {
        mClearScrollingCache = new Runnable()
        {
          public void run()
          {
            if (mCachingStarted)
            {
              AbsListView localAbsListView = AbsListView.this;
              mCachingActive = false;
              mCachingStarted = false;
              setChildrenDrawnWithCacheEnabled(false);
              if ((mPersistentDrawingCache & 0x2) == 0) {
                setChildrenDrawingCacheEnabled(false);
              }
              if (!isAlwaysDrawnWithCacheEnabled()) {
                invalidate();
              }
            }
          }
        };
      }
      post(mClearScrollingCache);
    }
  }
  
  private boolean contentFits()
  {
    int i = getChildCount();
    boolean bool = true;
    if (i == 0) {
      return true;
    }
    if (i != mItemCount) {
      return false;
    }
    if ((getChildAt(0).getTop() < mListPadding.top) || (getChildAt(i - 1).getBottom() > getHeight() - mListPadding.bottom)) {
      bool = false;
    }
    return bool;
  }
  
  private void createScrollingCache()
  {
    if ((mScrollingCacheEnabled) && (!mCachingStarted) && (!isHardwareAccelerated()))
    {
      setChildrenDrawnWithCacheEnabled(true);
      setChildrenDrawingCacheEnabled(true);
      mCachingActive = true;
      mCachingStarted = true;
    }
  }
  
  private void createTextFilter(boolean paramBoolean)
  {
    if (mPopup == null)
    {
      PopupWindow localPopupWindow = new PopupWindow(getContext());
      localPopupWindow.setFocusable(false);
      localPopupWindow.setTouchable(false);
      localPopupWindow.setInputMethodMode(2);
      localPopupWindow.setContentView(getTextFilterInput());
      localPopupWindow.setWidth(-2);
      localPopupWindow.setHeight(-2);
      localPopupWindow.setBackgroundDrawable(null);
      mPopup = localPopupWindow;
      getViewTreeObserver().addOnGlobalLayoutListener(this);
      mGlobalLayoutListenerAddedFilter = true;
    }
    if (paramBoolean) {
      mPopup.setAnimationStyle(16974597);
    } else {
      mPopup.setAnimationStyle(16974598);
    }
  }
  
  private void dismissPopup()
  {
    if (mPopup != null) {
      mPopup.dismiss();
    }
  }
  
  private void drawSelector(Canvas paramCanvas)
  {
    if (shouldDrawSelector())
    {
      Drawable localDrawable = mSelector;
      localDrawable.setBounds(mSelectorRect);
      localDrawable.draw(paramCanvas);
    }
  }
  
  private void finishGlows()
  {
    if (mEdgeGlowTop != null)
    {
      mEdgeGlowTop.finish();
      mEdgeGlowBottom.finish();
    }
  }
  
  static int getDistance(Rect paramRect1, Rect paramRect2, int paramInt)
  {
    int j;
    int k;
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt != 130)
          {
            switch (paramInt)
            {
            default: 
              throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
            }
            paramInt = right + paramRect1.width() / 2;
            i = top + paramRect1.height() / 2;
            j = left + paramRect2.width() / 2;
            k = top + paramRect2.height() / 2;
          }
          else
          {
            paramInt = left + paramRect1.width() / 2;
            i = bottom;
            j = left + paramRect2.width() / 2;
            k = top;
          }
        }
        else
        {
          paramInt = right;
          i = top + paramRect1.height() / 2;
          j = left;
          k = top + paramRect2.height() / 2;
        }
      }
      else
      {
        paramInt = left + paramRect1.width() / 2;
        i = top;
        j = left + paramRect2.width() / 2;
        k = bottom;
      }
    }
    else
    {
      paramInt = left;
      i = top + paramRect1.height() / 2;
      j = right;
      k = top + paramRect2.height() / 2;
    }
    paramInt = j - paramInt;
    int i = k - i;
    return i * i + paramInt * paramInt;
  }
  
  private int[] getDrawableStateForSelector()
  {
    if (mIsChildViewEnabled) {
      return super.getDrawableState();
    }
    int i = ENABLED_STATE_SET[0];
    int[] arrayOfInt = onCreateDrawableState(1);
    int j = -1;
    int m;
    for (int k = arrayOfInt.length - 1;; k--)
    {
      m = j;
      if (k < 0) {
        break;
      }
      if (arrayOfInt[k] == i)
      {
        m = k;
        break;
      }
    }
    if (m >= 0) {
      System.arraycopy(arrayOfInt, m + 1, arrayOfInt, m, arrayOfInt.length - m - 1);
    }
    return arrayOfInt;
  }
  
  private EditText getTextFilterInput()
  {
    if (mTextFilter == null)
    {
      mTextFilter = ((EditText)LayoutInflater.from(getContext()).inflate(17367350, null));
      mTextFilter.setRawInputType(177);
      mTextFilter.setImeOptions(268435456);
      mTextFilter.addTextChangedListener(this);
    }
    return mTextFilter;
  }
  
  private void initAbsListView()
  {
    setClickable(true);
    setFocusableInTouchMode(true);
    setWillNotDraw(false);
    setAlwaysDrawnWithCacheEnabled(false);
    setScrollingCacheEnabled(true);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(mContext);
    mTouchSlop = localViewConfiguration.getScaledTouchSlop();
    mVerticalScrollFactor = localViewConfiguration.getScaledVerticalScrollFactor();
    if (0.6D > 0.0D)
    {
      if (0.6D < 0.6D) {
        mMoveAcceleration = ((int)(mTouchSlop * 0.6D));
      } else if ((0.6D >= 0.6D) && (0.6D < 1.0D)) {
        mMoveAcceleration = ((int)(mTouchSlop * 0.6D));
      } else {
        mMoveAcceleration = mTouchSlop;
      }
    }
    else {
      mMoveAcceleration = mTouchSlop;
    }
    mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    mOverscrollDistance = localViewConfiguration.getScaledOverscrollDistance();
    mOverflingDistance = localViewConfiguration.getScaledOverflingDistance();
    mDensityScale = getContextgetResourcesgetDisplayMetricsdensity;
  }
  
  private void initOrResetVelocityTracker()
  {
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    } else {
      mVelocityTracker.clear();
    }
  }
  
  private void initVelocityTrackerIfNotExists()
  {
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
  }
  
  private void invalidateBottomGlow()
  {
    if (mEdgeGlowBottom == null) {
      return;
    }
    boolean bool = getClipToPadding();
    int i;
    if (bool) {
      i = getHeight() - mPaddingBottom;
    } else {
      i = getHeight();
    }
    int j;
    if (bool) {
      j = mPaddingLeft;
    } else {
      j = 0;
    }
    int k;
    if (bool) {
      k = getWidth() - mPaddingRight;
    } else {
      k = getWidth();
    }
    invalidate(j, i - mEdgeGlowBottom.getMaxHeight(), k, i);
  }
  
  private void invalidateTopGlow()
  {
    if (mEdgeGlowTop == null) {
      return;
    }
    boolean bool = getClipToPadding();
    int i = 0;
    int j;
    if (bool) {
      j = mPaddingTop;
    } else {
      j = 0;
    }
    if (bool) {
      i = mPaddingLeft;
    }
    int k;
    if (bool) {
      k = getWidth() - mPaddingRight;
    } else {
      k = getWidth();
    }
    invalidate(i, j, k, mEdgeGlowTop.getMaxHeight() + j);
  }
  
  private boolean isItemClickable(View paramView)
  {
    return paramView.hasExplicitFocusable() ^ true;
  }
  
  private boolean isOwnerThread()
  {
    boolean bool;
    if (mOwnerThread == Thread.currentThread()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = (paramMotionEvent.getAction() & 0xFF00) >> 8;
    if (paramMotionEvent.getPointerId(i) == mActivePointerId)
    {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      }
      mMotionX = ((int)paramMotionEvent.getX(i));
      mMotionY = ((int)paramMotionEvent.getY(i));
      mMotionCorrection = 0;
      mActivePointerId = paramMotionEvent.getPointerId(i);
    }
  }
  
  private void onTouchCancel()
  {
    switch (mTouchMode)
    {
    default: 
      mTouchMode = -1;
      setPressed(false);
      View localView = getChildAt(mMotionPosition - mFirstPosition);
      if (localView != null) {
        localView.setPressed(false);
      }
      break;
    case 6: 
      break;
    case 5: 
      if (mFlingRunnable == null) {
        mFlingRunnable = new FlingRunnable();
      }
      mFlingRunnable.startSpringback();
      break;
    }
    clearScrollingCache();
    removeCallbacks(mPendingCheckForLongPress);
    recycleVelocityTracker();
    if (mEdgeGlowTop != null)
    {
      mEdgeGlowTop.onRelease();
      mEdgeGlowBottom.onRelease();
    }
    mActivePointerId = -1;
  }
  
  private void onTouchDown(MotionEvent paramMotionEvent)
  {
    mHasPerformedLongPress = false;
    mActivePointerId = paramMotionEvent.getPointerId(0);
    hideSelector();
    if (mTouchMode == 6)
    {
      mFlingRunnable.endFling();
      if (mPositionScroller != null) {
        mPositionScroller.stop();
      }
      mTouchMode = 5;
      mMotionX = ((int)paramMotionEvent.getX());
      mMotionY = ((int)paramMotionEvent.getY());
      mLastY = mMotionY;
      mMotionCorrection = 0;
      mDirection = 0;
    }
    else
    {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      int k = pointToPosition(i, j);
      int m = k;
      if (!mDataChanged) {
        if (mTouchMode == 4)
        {
          createScrollingCache();
          mTouchMode = 3;
          mMotionCorrection = 0;
          m = findMotionRow(j);
          mFlingRunnable.flywheelTouch();
        }
        else
        {
          m = k;
          if (k >= 0)
          {
            m = k;
            if (((ListAdapter)getAdapter()).isEnabled(k))
            {
              mTouchMode = 0;
              if (mPendingCheckForTap == null) {
                mPendingCheckForTap = new CheckForTap(null);
              }
              mPendingCheckForTap.x = paramMotionEvent.getX();
              mPendingCheckForTap.y = paramMotionEvent.getY();
              postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
              m = k;
            }
          }
        }
      }
      if (m >= 0) {
        mMotionViewOriginalTop = getChildAt(m - mFirstPosition).getTop();
      }
      mMotionX = i;
      mMotionY = j;
      mMotionPosition = m;
      mLastY = Integer.MIN_VALUE;
    }
    if ((mTouchMode == 0) && (mMotionPosition != -1) && (performButtonActionOnTouchDown(paramMotionEvent))) {
      removeCallbacks(mPendingCheckForTap);
    }
  }
  
  private void onTouchMove(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2)
  {
    if (mHasPerformedLongPress) {
      return;
    }
    int i = paramMotionEvent1.findPointerIndex(mActivePointerId);
    int j = i;
    if (i == -1)
    {
      j = 0;
      mActivePointerId = paramMotionEvent1.getPointerId(0);
    }
    if (mDataChanged) {
      layoutChildren();
    }
    int k = (int)paramMotionEvent1.getY(j);
    i = mTouchMode;
    if (i != 5) {
      switch (i)
      {
      default: 
        break;
      case 0: 
      case 1: 
      case 2: 
        if (startScrollIfNeeded((int)paramMotionEvent1.getX(j), k, paramMotionEvent2)) {
          break;
        }
        paramMotionEvent2 = getChildAt(mMotionPosition - mFirstPosition);
        float f = paramMotionEvent1.getX(j);
        if (!pointInView(f, k, mTouchSlop))
        {
          setPressed(false);
          if (paramMotionEvent2 != null) {
            paramMotionEvent2.setPressed(false);
          }
          if (mTouchMode == 0) {
            paramMotionEvent1 = mPendingCheckForTap;
          } else {
            paramMotionEvent1 = mPendingCheckForLongPress;
          }
          removeCallbacks(paramMotionEvent1);
          mTouchMode = 2;
          updateSelectorState();
        }
        else if (paramMotionEvent2 != null)
        {
          paramMotionEvent1 = mTmpPoint;
          paramMotionEvent1[0] = f;
          paramMotionEvent1[1] = k;
          transformPointToViewLocal(paramMotionEvent1, paramMotionEvent2);
          paramMotionEvent2.drawableHotspotChanged(paramMotionEvent1[0], paramMotionEvent1[1]);
        }
        break;
      }
    } else {
      scrollIfNeeded((int)paramMotionEvent1.getX(j), k, paramMotionEvent2);
    }
  }
  
  private void onTouchUp(MotionEvent paramMotionEvent)
  {
    int i = mTouchMode;
    if (i != 5)
    {
      int n;
      switch (i)
      {
      default: 
        break;
      case 3: 
        int j = getChildCount();
        if (j > 0)
        {
          int k = getChildAt(0).getTop();
          int m = getChildAt(j - 1).getBottom();
          n = mListPadding.top;
          int i1 = getHeight() - mListPadding.bottom;
          if ((mFirstPosition == 0) && (k >= n) && (mFirstPosition + j < mItemCount) && (m <= getHeight() - i1))
          {
            mTouchMode = -1;
            reportScrollStateChange(0);
          }
          else
          {
            paramMotionEvent = mVelocityTracker;
            paramMotionEvent.computeCurrentVelocity(1000, mMaximumVelocity);
            int i2 = (int)(paramMotionEvent.getYVelocity(mActivePointerId) * mVelocityScale);
            if (Math.abs(i2) > mMinimumVelocity) {
              i = 1;
            } else {
              i = 0;
            }
            if ((i != 0) && ((mFirstPosition != 0) || (k != n - mOverscrollDistance)) && ((mFirstPosition + j != mItemCount) || (m != mOverscrollDistance + i1)))
            {
              if (!dispatchNestedPreFling(0.0F, -i2))
              {
                if (mFlingRunnable == null) {
                  mFlingRunnable = new FlingRunnable();
                }
                reportScrollStateChange(2);
                mFlingRunnable.start(-i2);
                dispatchNestedFling(0.0F, -i2, true);
              }
              else
              {
                mTouchMode = -1;
                reportScrollStateChange(0);
              }
            }
            else
            {
              mTouchMode = -1;
              reportScrollStateChange(0);
              if (mFlingRunnable != null) {
                mFlingRunnable.endFling();
              }
              if (mPositionScroller != null) {
                mPositionScroller.stop();
              }
              if ((i != 0) && (!dispatchNestedPreFling(0.0F, -i2))) {
                dispatchNestedFling(0.0F, -i2, false);
              }
            }
          }
        }
        else
        {
          mTouchMode = -1;
          reportScrollStateChange(0);
        }
        break;
      case 0: 
      case 1: 
      case 2: 
        n = mMotionPosition;
        final View localView = getChildAt(n - mFirstPosition);
        if (localView != null)
        {
          if (mTouchMode != 0) {
            localView.setPressed(false);
          }
          float f = paramMotionEvent.getX();
          if ((f > mListPadding.left) && (f < getWidth() - mListPadding.right)) {
            i = 1;
          } else {
            i = 0;
          }
          if ((i != 0) && (!localView.hasExplicitFocusable()))
          {
            if (mPerformClick == null) {
              mPerformClick = new PerformClick(null);
            }
            PerformClick localPerformClick = mPerformClick;
            mClickMotionPosition = n;
            localPerformClick.rememberWindowAttachCount();
            mResurrectToPosition = n;
            if ((mTouchMode != 0) && (mTouchMode != 1))
            {
              if ((!mDataChanged) && (mAdapter.isEnabled(n))) {
                localPerformClick.run();
              }
            }
            else
            {
              Object localObject;
              if (mTouchMode == 0) {
                localObject = mPendingCheckForTap;
              } else {
                localObject = mPendingCheckForLongPress;
              }
              removeCallbacks((Runnable)localObject);
              mLayoutMode = 0;
              if ((!mDataChanged) && (mAdapter.isEnabled(n)))
              {
                mTouchMode = 1;
                setSelectedPositionInt(mMotionPosition);
                layoutChildren();
                localView.setPressed(true);
                positionSelector(mMotionPosition, localView);
                setPressed(true);
                if (mSelector != null)
                {
                  localObject = mSelector.getCurrent();
                  if ((localObject != null) && ((localObject instanceof TransitionDrawable))) {
                    ((TransitionDrawable)localObject).resetTransition();
                  }
                  mSelector.setHotspot(f, paramMotionEvent.getY());
                }
                if ((!mDataChanged) && (!mIsDetaching) && (isAttachedToWindow()) && (!post(localPerformClick))) {
                  localPerformClick.run();
                }
                if (mTouchModeReset != null) {
                  removeCallbacks(mTouchModeReset);
                }
                mTouchModeReset = new Runnable()
                {
                  public void run()
                  {
                    AbsListView.access$1602(AbsListView.this, null);
                    mTouchMode = -1;
                    localView.setPressed(false);
                    setPressed(false);
                  }
                };
                postDelayed(mTouchModeReset, ViewConfiguration.getPressedStateDuration());
              }
              else
              {
                mTouchMode = -1;
                updateSelectorState();
              }
              return;
            }
          }
        }
        mTouchMode = -1;
        updateSelectorState();
        break;
      }
    }
    else
    {
      if (mFlingRunnable == null) {
        mFlingRunnable = new FlingRunnable();
      }
      paramMotionEvent = mVelocityTracker;
      paramMotionEvent.computeCurrentVelocity(1000, mMaximumVelocity);
      i = (int)paramMotionEvent.getYVelocity(mActivePointerId);
      reportScrollStateChange(2);
      if (Math.abs(i) > mMinimumVelocity) {
        mFlingRunnable.startOverfling(-i);
      } else {
        mFlingRunnable.startSpringback();
      }
    }
    setPressed(false);
    if (mEdgeGlowTop != null)
    {
      mEdgeGlowTop.onRelease();
      mEdgeGlowBottom.onRelease();
    }
    invalidate();
    removeCallbacks(mPendingCheckForLongPress);
    recycleVelocityTracker();
    mActivePointerId = -1;
    if (mScrollStrictSpan != null)
    {
      mScrollStrictSpan.finish();
      mScrollStrictSpan = null;
    }
  }
  
  private boolean performStylusButtonPressAction(MotionEvent paramMotionEvent)
  {
    if ((mChoiceMode == 3) && (mChoiceActionMode == null))
    {
      paramMotionEvent = getChildAt(mMotionPosition - mFirstPosition);
      if ((paramMotionEvent != null) && (performLongPress(paramMotionEvent, mMotionPosition, mAdapter.getItemId(mMotionPosition))))
      {
        mTouchMode = -1;
        setPressed(false);
        paramMotionEvent.setPressed(false);
        return true;
      }
    }
    return false;
  }
  
  private void positionPopup()
  {
    int i = getResourcesgetDisplayMetricsheightPixels;
    int[] arrayOfInt = new int[2];
    getLocationOnScreen(arrayOfInt);
    i = i - arrayOfInt[1] - getHeight() + (int)(mDensityScale * 20.0F);
    if (!mPopup.isShowing()) {
      mPopup.showAtLocation(this, 81, arrayOfInt[0], i);
    } else {
      mPopup.update(arrayOfInt[0], i, -1, -1);
    }
  }
  
  private void positionSelector(int paramInt, View paramView, boolean paramBoolean, float paramFloat1, float paramFloat2)
  {
    int i;
    if (paramInt != mSelectorPosition) {
      i = 1;
    } else {
      i = 0;
    }
    if (paramInt != -1) {
      mSelectorPosition = paramInt;
    }
    Rect localRect = mSelectorRect;
    localRect.set(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
    if ((paramView instanceof SelectionBoundsAdjuster)) {
      ((SelectionBoundsAdjuster)paramView).adjustListItemSelectionBounds(localRect);
    }
    left -= mSelectionLeftPadding;
    top -= mSelectionTopPadding;
    right += mSelectionRightPadding;
    bottom += mSelectionBottomPadding;
    boolean bool = paramView.isEnabled();
    if (mIsChildViewEnabled != bool) {
      mIsChildViewEnabled = bool;
    }
    paramView = mSelector;
    if (paramView != null)
    {
      if (i != 0)
      {
        paramView.setVisible(false, false);
        paramView.setState(StateSet.NOTHING);
      }
      paramView.setBounds(localRect);
      if (i != 0)
      {
        if (getVisibility() == 0) {
          paramView.setVisible(true, false);
        }
        updateSelectorState();
      }
      if (paramBoolean) {
        paramView.setHotspot(paramFloat1, paramFloat2);
      }
    }
  }
  
  private void recycleVelocityTracker()
  {
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }
  
  private void scrollIfNeeded(int paramInt1, int paramInt2, MotionEvent paramMotionEvent)
  {
    int i = paramInt2 - mMotionY;
    int j = 0;
    int k = 0;
    int m = i;
    if (mLastY == Integer.MIN_VALUE) {
      m = i - mMotionCorrection;
    }
    int n;
    if (mLastY != Integer.MIN_VALUE) {
      n = mLastY - paramInt2;
    } else {
      n = -m;
    }
    i = m;
    if (dispatchNestedPreScroll(0, n, mScrollConsumed, mScrollOffset))
    {
      n = m + mScrollConsumed[1];
      m = -mScrollOffset[1];
      i1 = mScrollConsumed[1];
      i = n;
      j = m;
      k = i1;
      if (paramMotionEvent != null)
      {
        paramMotionEvent.offsetLocation(0.0F, mScrollOffset[1]);
        mNestedYOffset += mScrollOffset[1];
        k = i1;
        j = m;
        i = n;
      }
    }
    if (mLastY != Integer.MIN_VALUE) {
      m = paramInt2 - mLastY + k;
    } else {
      m = i;
    }
    int i2 = 0;
    int i1 = 0;
    if (mTouchMode == 3)
    {
      if (mScrollStrictSpan == null) {
        mScrollStrictSpan = StrictMode.enterCriticalSpan("AbsListView-scroll");
      }
      if (paramInt2 != mLastY)
      {
        if (((mGroupFlags & 0x80000) == 0) && (Math.abs(i) > mTouchSlop))
        {
          localObject = getParent();
          if (localObject != null) {
            ((ViewParent)localObject).requestDisallowInterceptTouchEvent(true);
          }
        }
        if (mMotionPosition >= 0) {
          k = mMotionPosition - mFirstPosition;
        } else {
          k = getChildCount() / 2;
        }
        n = 0;
        Object localObject = getChildAt(k);
        if (localObject != null) {
          n = ((View)localObject).getTop();
        }
        boolean bool = false;
        if (m != 0) {
          bool = trackMotionScroll(i, m);
        }
        localObject = getChildAt(k);
        if (localObject != null)
        {
          i = ((View)localObject).getTop();
          if (bool)
          {
            k = -m - (i - n);
            if (dispatchNestedScroll(0, k - m, 0, k, mScrollOffset))
            {
              i = 0 - mScrollOffset[1];
              if (paramMotionEvent != null)
              {
                paramMotionEvent.offsetLocation(0.0F, mScrollOffset[1]);
                mNestedYOffset += mScrollOffset[1];
              }
            }
            else
            {
              bool = overScrollBy(0, k, 0, mScrollY, 0, 0, 0, mOverscrollDistance, true);
              if ((bool) && (mVelocityTracker != null)) {
                mVelocityTracker.clear();
              }
              i = getOverScrollMode();
              if ((i != 0) && ((i != 1) || (contentFits())))
              {
                i = i1;
              }
              else
              {
                if (!bool)
                {
                  mDirection = 0;
                  mTouchMode = 5;
                }
                if (m > 0)
                {
                  mEdgeGlowTop.onPull(-k / getHeight(), paramInt1 / getWidth());
                  if (!mEdgeGlowBottom.isFinished()) {
                    mEdgeGlowBottom.onRelease();
                  }
                  invalidateTopGlow();
                  i = i1;
                }
                else
                {
                  i = i1;
                  if (m < 0)
                  {
                    mEdgeGlowBottom.onPull(k / getHeight(), 1.0F - paramInt1 / getWidth());
                    if (!mEdgeGlowTop.isFinished()) {
                      mEdgeGlowTop.onRelease();
                    }
                    invalidateBottomGlow();
                    i = i1;
                  }
                }
              }
            }
          }
          else
          {
            i = i1;
          }
          mMotionY = (paramInt2 + i + j);
        }
        else
        {
          i = i2;
        }
        mLastY = (paramInt2 + i + j);
      }
    }
    else if ((mTouchMode == 5) && (paramInt2 != mLastY))
    {
      i2 = mScrollY;
      i1 = i2 - m;
      if (paramInt2 > mLastY) {
        k = 1;
      } else {
        k = -1;
      }
      if (mDirection == 0) {
        mDirection = k;
      }
      n = -m;
      if (((i1 < 0) && (i2 >= 0)) || ((i1 > 0) && (i2 <= 0)))
      {
        n = -i2;
        m += n;
      }
      else
      {
        m = 0;
      }
      if (n != 0)
      {
        overScrollBy(0, n, 0, mScrollY, 0, 0, 0, mOverscrollDistance, true);
        i1 = getOverScrollMode();
        if ((i1 != 0) && ((i1 != 1) || (contentFits()))) {
          break label963;
        }
        if (i > 0)
        {
          mEdgeGlowTop.onPull(n / getHeight(), paramInt1 / getWidth());
          if (!mEdgeGlowBottom.isFinished()) {
            mEdgeGlowBottom.onRelease();
          }
          invalidateTopGlow();
        }
        else if (i < 0)
        {
          mEdgeGlowBottom.onPull(n / getHeight(), 1.0F - paramInt1 / getWidth());
          if (!mEdgeGlowTop.isFinished()) {
            mEdgeGlowTop.onRelease();
          }
          invalidateBottomGlow();
        }
      }
      label963:
      if (m != 0)
      {
        if (mScrollY != 0)
        {
          mScrollY = 0;
          invalidateParentIfNeeded();
        }
        trackMotionScroll(m, m);
        mTouchMode = 3;
        i = findClosestMotionRow(paramInt2);
        paramInt1 = 0;
        mMotionCorrection = 0;
        paramMotionEvent = getChildAt(i - mFirstPosition);
        if (paramMotionEvent != null) {
          paramInt1 = paramMotionEvent.getTop();
        }
        mMotionViewOriginalTop = paramInt1;
        mMotionY = (paramInt2 + j);
        mMotionPosition = i;
      }
      mLastY = (paramInt2 + 0 + j);
      mDirection = k;
    }
  }
  
  private void setFastScrollerAlwaysVisibleUiThread(boolean paramBoolean)
  {
    if (mFastScroll != null) {
      mFastScroll.setAlwaysShow(paramBoolean);
    }
  }
  
  private void setFastScrollerEnabledUiThread(boolean paramBoolean)
  {
    if (mFastScroll != null)
    {
      mFastScroll.setEnabled(paramBoolean);
    }
    else if (paramBoolean)
    {
      mFastScroll = new FastScroller(this, mFastScrollStyle);
      mFastScroll.setEnabled(true);
    }
    resolvePadding();
    if (mFastScroll != null) {
      mFastScroll.updateLayout();
    }
  }
  
  private void setItemViewLayoutParams(View paramView, int paramInt)
  {
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    LayoutParams localLayoutParams1;
    if (localLayoutParams == null) {
      localLayoutParams1 = (LayoutParams)generateDefaultLayoutParams();
    }
    for (;;)
    {
      break;
      if (!checkLayoutParams(localLayoutParams)) {
        localLayoutParams1 = (LayoutParams)generateLayoutParams(localLayoutParams);
      } else {
        localLayoutParams1 = (LayoutParams)localLayoutParams;
      }
    }
    if (mAdapterHasStableIds) {
      itemId = mAdapter.getItemId(paramInt);
    }
    viewType = mAdapter.getItemViewType(paramInt);
    isEnabled = mAdapter.isEnabled(paramInt);
    if (localLayoutParams1 != localLayoutParams) {
      paramView.setLayoutParams(localLayoutParams1);
    }
  }
  
  private boolean showContextMenuForChildInternal(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    int i = getPositionForView(paramView);
    if (i < 0) {
      return false;
    }
    long l = mAdapter.getItemId(i);
    boolean bool1 = false;
    if (mOnItemLongClickListener != null) {
      bool1 = mOnItemLongClickListener.onItemLongClick(this, paramView, i, l);
    }
    boolean bool2 = bool1;
    if (!bool1)
    {
      mContextMenuInfo = createContextMenuInfo(getChildAt(i - mFirstPosition), i, l);
      if (paramBoolean) {
        bool2 = super.showContextMenuForChild(paramView, paramFloat1, paramFloat2);
      } else {
        bool2 = super.showContextMenuForChild(paramView);
      }
    }
    return bool2;
  }
  
  private boolean showContextMenuInternal(float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    int i = pointToPosition((int)paramFloat1, (int)paramFloat2);
    if (i != -1)
    {
      long l = mAdapter.getItemId(i);
      View localView = getChildAt(i - mFirstPosition);
      if (localView != null)
      {
        mContextMenuInfo = createContextMenuInfo(localView, i, l);
        if (paramBoolean) {
          return super.showContextMenuForChild(this, paramFloat1, paramFloat2);
        }
        return super.showContextMenuForChild(this);
      }
    }
    if (paramBoolean) {
      return super.showContextMenu(paramFloat1, paramFloat2);
    }
    return super.showContextMenu();
  }
  
  private void showPopup()
  {
    if (getWindowVisibility() == 0)
    {
      createTextFilter(true);
      positionPopup();
      checkFocus();
    }
  }
  
  private boolean startScrollIfNeeded(int paramInt1, int paramInt2, MotionEvent paramMotionEvent)
  {
    int i = paramInt2 - mMotionY;
    int j = Math.abs(i);
    int k;
    if (mScrollY != 0) {
      k = 1;
    } else {
      k = 0;
    }
    if (mIsFirstTouchMoveEvent)
    {
      if (j > mMoveAcceleration) {
        j = 1;
      } else {
        j = 0;
      }
    }
    else if (j > mTouchSlop) {
      j = 1;
    } else {
      j = 0;
    }
    if (((k != 0) || (j != 0)) && ((getNestedScrollAxes() & 0x2) == 0))
    {
      createScrollingCache();
      if (k != 0)
      {
        mTouchMode = 5;
        mMotionCorrection = 0;
      }
      else
      {
        mTouchMode = 3;
        if (mIsFirstTouchMoveEvent)
        {
          if (i > 0) {
            j = mMoveAcceleration;
          } else {
            j = -mMoveAcceleration;
          }
          mMotionCorrection = j;
        }
        else
        {
          if (i > 0) {
            j = mTouchSlop;
          } else {
            j = -mTouchSlop;
          }
          mMotionCorrection = j;
        }
      }
      removeCallbacks(mPendingCheckForLongPress);
      setPressed(false);
      Object localObject = getChildAt(mMotionPosition - mFirstPosition);
      if (localObject != null) {
        ((View)localObject).setPressed(false);
      }
      reportScrollStateChange(1);
      localObject = getParent();
      if (localObject != null) {
        ((ViewParent)localObject).requestDisallowInterceptTouchEvent(true);
      }
      scrollIfNeeded(paramInt1, paramInt2, paramMotionEvent);
      return true;
    }
    return false;
  }
  
  private void updateOnScreenCheckedViews()
  {
    int i = mFirstPosition;
    int j = getChildCount();
    int k = getContextgetApplicationInfotargetSdkVersion;
    int m = 0;
    if (k >= 11) {
      k = 1;
    } else {
      k = 0;
    }
    while (m < j)
    {
      View localView = getChildAt(m);
      int n = i + m;
      if ((localView instanceof Checkable)) {
        ((Checkable)localView).setChecked(mCheckStates.get(n));
      } else if (k != 0) {
        localView.setActivated(mCheckStates.get(n));
      }
      m++;
    }
  }
  
  private void useDefaultSelector()
  {
    setSelector(getContext().getDrawable(17301602));
  }
  
  public void addTouchables(ArrayList<View> paramArrayList)
  {
    int i = getChildCount();
    int j = mFirstPosition;
    ListAdapter localListAdapter = mAdapter;
    if (localListAdapter == null) {
      return;
    }
    for (int k = 0; k < i; k++)
    {
      View localView = getChildAt(k);
      if (localListAdapter.isEnabled(j + k)) {
        paramArrayList.add(localView);
      }
      localView.addTouchables(paramArrayList);
    }
  }
  
  public void afterTextChanged(Editable paramEditable) {}
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public boolean canScrollList(int paramInt)
  {
    int i = getChildCount();
    boolean bool1 = false;
    boolean bool2 = false;
    if (i == 0) {
      return false;
    }
    int j = mFirstPosition;
    Rect localRect = mListPadding;
    if (paramInt > 0)
    {
      paramInt = getChildAt(i - 1).getBottom();
      if ((j + i >= mItemCount) && (paramInt <= getHeight() - bottom)) {
        break label77;
      }
      bool2 = true;
      label77:
      return bool2;
    }
    paramInt = getChildAt(0).getTop();
    if ((j <= 0) && (paramInt >= top)) {
      bool2 = bool1;
    } else {
      bool2 = true;
    }
    return bool2;
  }
  
  public boolean checkInputConnectionProxy(View paramView)
  {
    boolean bool;
    if (paramView == mTextFilter) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void clearChoices()
  {
    if (mCheckStates != null) {
      mCheckStates.clear();
    }
    if (mCheckedIdStates != null) {
      mCheckedIdStates.clear();
    }
    mCheckedItemCount = 0;
  }
  
  public void clearTextFilter()
  {
    if (mFiltered)
    {
      getTextFilterInput().setText("");
      mFiltered = false;
      if ((mPopup != null) && (mPopup.isShowing())) {
        dismissPopup();
      }
    }
  }
  
  protected int computeVerticalScrollExtent()
  {
    int i = getChildCount();
    if (i > 0)
    {
      if (mSmoothScrollbarEnabled)
      {
        int j = i * 100;
        View localView = getChildAt(0);
        int k = localView.getTop();
        int m = localView.getHeight();
        int n = j;
        if (m > 0) {
          n = j + k * 100 / m;
        }
        localView = getChildAt(i - 1);
        i = localView.getBottom();
        k = localView.getHeight();
        j = n;
        if (k > 0) {
          j = n - (i - getHeight()) * 100 / k;
        }
        return j;
      }
      return 1;
    }
    return 0;
  }
  
  protected int computeVerticalScrollOffset()
  {
    int i = mFirstPosition;
    int j = getChildCount();
    if ((i >= 0) && (j > 0))
    {
      int k;
      int m;
      if (mSmoothScrollbarEnabled)
      {
        View localView = getChildAt(0);
        k = localView.getTop();
        m = localView.getHeight();
        if (m > 0) {
          return Math.max(i * 100 - k * 100 / m + (int)(mScrollY / getHeight() * mItemCount * 100.0F), 0);
        }
      }
      else
      {
        k = mItemCount;
        if (i == 0) {
          m = 0;
        }
        for (;;)
        {
          break;
          if (i + j == k) {
            m = k;
          } else {
            m = j / 2 + i;
          }
        }
        return (int)(i + j * (m / k));
      }
    }
    return 0;
  }
  
  protected int computeVerticalScrollRange()
  {
    int j;
    if (mSmoothScrollbarEnabled)
    {
      int i = Math.max(mItemCount * 100, 0);
      j = i;
      if (mScrollY != 0) {
        j = i + Math.abs((int)(mScrollY / getHeight() * mItemCount * 100.0F));
      }
    }
    else
    {
      j = mItemCount;
    }
    return j;
  }
  
  void confirmCheckedPositionsById()
  {
    mCheckStates.clear();
    int i = 0;
    for (int j = 0; j < mCheckedIdStates.size(); j++)
    {
      long l = mCheckedIdStates.keyAt(j);
      int k = ((Integer)mCheckedIdStates.valueAt(j)).intValue();
      if (l != mAdapter.getItemId(k))
      {
        int m = Math.max(0, k - 20);
        int n = Math.min(k + 20, mItemCount);
        int i1 = 0;
        int i2;
        for (;;)
        {
          i2 = i1;
          if (m >= n) {
            break;
          }
          if (l == mAdapter.getItemId(m))
          {
            i2 = 1;
            mCheckStates.put(m, true);
            mCheckedIdStates.setValueAt(j, Integer.valueOf(m));
            break;
          }
          m++;
        }
        m = j;
        if (i2 == 0)
        {
          mCheckedIdStates.delete(l);
          j--;
          mCheckedItemCount -= 1;
          i2 = 1;
          m = j;
          i = i2;
          if (mChoiceActionMode != null)
          {
            m = j;
            i = i2;
            if (mMultiChoiceModeCallback != null)
            {
              mMultiChoiceModeCallback.onItemCheckedStateChanged(mChoiceActionMode, k, l, false);
              i = i2;
              m = j;
            }
          }
        }
        j = m;
      }
      else
      {
        mCheckStates.put(k, true);
      }
    }
    if ((i != 0) && (mChoiceActionMode != null)) {
      mChoiceActionMode.invalidate();
    }
  }
  
  ContextMenu.ContextMenuInfo createContextMenuInfo(View paramView, int paramInt, long paramLong)
  {
    return new AdapterView.AdapterContextMenuInfo(paramView, paramInt, paramLong);
  }
  
  AbsPositionScroller createPositionScroller()
  {
    return new PositionScroller();
  }
  
  public void deferNotifyDataSetChanged()
  {
    mDeferNotifyDataSetChanged = true;
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    int i = 0;
    int j;
    if ((mGroupFlags & 0x22) == 34) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != 0)
    {
      i = paramCanvas.save();
      int k = mScrollX;
      int m = mScrollY;
      paramCanvas.clipRect(mPaddingLeft + k, mPaddingTop + m, mRight + k - mLeft - mPaddingRight, mBottom + m - mTop - mPaddingBottom);
      mGroupFlags &= 0xFFFFFFDD;
    }
    boolean bool = mDrawSelectorOnTop;
    if (!bool) {
      drawSelector(paramCanvas);
    }
    super.dispatchDraw(paramCanvas);
    if (bool) {
      drawSelector(paramCanvas);
    }
    if (j != 0)
    {
      paramCanvas.restoreToCount(i);
      mGroupFlags = (0x22 | mGroupFlags);
    }
  }
  
  public void dispatchDrawableHotspotChanged(float paramFloat1, float paramFloat2) {}
  
  protected void dispatchSetPressed(boolean paramBoolean) {}
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if (mEdgeGlowTop != null)
    {
      int i = mScrollY;
      boolean bool = getClipToPadding();
      int j = 0;
      int k;
      int m;
      int n;
      int i1;
      if (bool)
      {
        k = getWidth() - mPaddingLeft - mPaddingRight;
        m = getHeight() - mPaddingTop - mPaddingBottom;
        n = mPaddingLeft;
        i1 = mPaddingTop;
      }
      else
      {
        k = getWidth();
        m = getHeight();
        n = 0;
        i1 = 0;
      }
      int i2;
      int i3;
      if (!mEdgeGlowTop.isFinished())
      {
        i2 = paramCanvas.save();
        paramCanvas.clipRect(n, i1, n + k, mEdgeGlowTop.getMaxHeight() + i1);
        i3 = Math.min(0, mFirstPositionDistanceGuess + i);
        paramCanvas.translate(n, i3 + i1);
        mEdgeGlowTop.setSize(k, m);
        if (mEdgeGlowTop.draw(paramCanvas)) {
          invalidateTopGlow();
        }
        paramCanvas.restoreToCount(i2);
      }
      if (!mEdgeGlowBottom.isFinished())
      {
        i3 = paramCanvas.save();
        paramCanvas.clipRect(n, i1 + m - mEdgeGlowBottom.getMaxHeight(), n + k, i1 + m);
        i2 = -k;
        i = Math.max(getHeight(), mLastPositionDistanceGuess + i);
        i1 = j;
        if (bool) {
          i1 = mPaddingBottom;
        }
        paramCanvas.translate(i2 + n, i - i1);
        paramCanvas.rotate(180.0F, k, 0.0F);
        mEdgeGlowBottom.setSize(k, m);
        if (mEdgeGlowBottom.draw(paramCanvas)) {
          invalidateBottomGlow();
        }
        paramCanvas.restoreToCount(i3);
      }
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    updateSelectorState();
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("drawing:cacheColorHint", getCacheColorHint());
    paramViewHierarchyEncoder.addProperty("list:fastScrollEnabled", isFastScrollEnabled());
    paramViewHierarchyEncoder.addProperty("list:scrollingCacheEnabled", isScrollingCacheEnabled());
    paramViewHierarchyEncoder.addProperty("list:smoothScrollbarEnabled", isSmoothScrollbarEnabled());
    paramViewHierarchyEncoder.addProperty("list:stackFromBottom", isStackFromBottom());
    paramViewHierarchyEncoder.addProperty("list:textFilterEnabled", isTextFilterEnabled());
    View localView = getSelectedView();
    if (localView != null)
    {
      paramViewHierarchyEncoder.addPropertyKey("selectedView");
      localView.encode(paramViewHierarchyEncoder);
    }
  }
  
  abstract void fillGap(boolean paramBoolean);
  
  int findClosestMotionRow(int paramInt)
  {
    int i = getChildCount();
    if (i == 0) {
      return -1;
    }
    paramInt = findMotionRow(paramInt);
    if (paramInt == -1) {
      paramInt = mFirstPosition + i - 1;
    }
    return paramInt;
  }
  
  abstract int findMotionRow(int paramInt);
  
  void findNextGap(boolean paramBoolean) {}
  
  public View findViewByAccessibilityIdTraversal(int paramInt)
  {
    if (paramInt == getAccessibilityViewId()) {
      return this;
    }
    return super.findViewByAccessibilityIdTraversal(paramInt);
  }
  
  public void fling(int paramInt)
  {
    if (mFlingRunnable == null) {
      mFlingRunnable = new FlingRunnable();
    }
    reportScrollStateChange(2);
    mFlingRunnable.start(paramInt);
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -2, 0);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return AbsListView.class.getName();
  }
  
  View getAccessibilityFocusedChild(View paramView)
  {
    for (ViewParent localViewParent = paramView.getParent(); ((localViewParent instanceof View)) && (localViewParent != this); localViewParent = localViewParent.getParent()) {
      paramView = (View)localViewParent;
    }
    if (!(localViewParent instanceof View)) {
      return null;
    }
    return paramView;
  }
  
  protected float getBottomFadingEdgeStrength()
  {
    int i = getChildCount();
    float f1 = super.getBottomFadingEdgeStrength();
    if (i == 0) {
      return f1;
    }
    if (mFirstPosition + i - 1 < mItemCount - 1) {
      return 1.0F;
    }
    i = getChildAt(i - 1).getBottom();
    int j = getHeight();
    float f2 = getVerticalFadingEdgeLength();
    if (i > j - mPaddingBottom) {
      f1 = (i - j + mPaddingBottom) / f2;
    }
    return f1;
  }
  
  protected int getBottomPaddingOffset()
  {
    int i;
    if ((mGroupFlags & 0x22) == 34) {
      i = 0;
    } else {
      i = mPaddingBottom;
    }
    return i;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public int getCacheColorHint()
  {
    return mCacheColorHint;
  }
  
  public int getCheckedItemCount()
  {
    return mCheckedItemCount;
  }
  
  public long[] getCheckedItemIds()
  {
    int i = mChoiceMode;
    int j = 0;
    if ((i != 0) && (mCheckedIdStates != null) && (mAdapter != null))
    {
      LongSparseArray localLongSparseArray = mCheckedIdStates;
      i = localLongSparseArray.size();
      long[] arrayOfLong = new long[i];
      while (j < i)
      {
        arrayOfLong[j] = localLongSparseArray.keyAt(j);
        j++;
      }
      return arrayOfLong;
    }
    return new long[0];
  }
  
  public int getCheckedItemPosition()
  {
    if ((mChoiceMode == 1) && (mCheckStates != null) && (mCheckStates.size() == 1)) {
      return mCheckStates.keyAt(0);
    }
    return -1;
  }
  
  public SparseBooleanArray getCheckedItemPositions()
  {
    if (mChoiceMode != 0) {
      return mCheckStates;
    }
    return null;
  }
  
  public int getChoiceMode()
  {
    return mChoiceMode;
  }
  
  protected ContextMenu.ContextMenuInfo getContextMenuInfo()
  {
    return mContextMenuInfo;
  }
  
  public void getFocusedRect(Rect paramRect)
  {
    View localView = getSelectedView();
    if ((localView != null) && (localView.getParent() == this))
    {
      localView.getFocusedRect(paramRect);
      offsetDescendantRectToMyCoords(localView, paramRect);
    }
    else
    {
      super.getFocusedRect(paramRect);
    }
  }
  
  int getFooterViewsCount()
  {
    return 0;
  }
  
  int getHeaderViewsCount()
  {
    return 0;
  }
  
  int getHeightForPosition(int paramInt)
  {
    int i = getFirstVisiblePosition();
    int j = getChildCount();
    i = paramInt - i;
    if ((i >= 0) && (i < j)) {
      return getChildAt(i).getHeight();
    }
    View localView = obtainView(paramInt, mIsScrap);
    localView.measure(mWidthMeasureSpec, 0);
    j = localView.getMeasuredHeight();
    mRecycler.addScrapView(localView, paramInt);
    return j;
  }
  
  protected int getLeftPaddingOffset()
  {
    int i;
    if ((mGroupFlags & 0x22) == 34) {
      i = 0;
    } else {
      i = -mPaddingLeft;
    }
    return i;
  }
  
  public int getListPaddingBottom()
  {
    return mListPadding.bottom;
  }
  
  public int getListPaddingLeft()
  {
    return mListPadding.left;
  }
  
  public int getListPaddingRight()
  {
    return mListPadding.right;
  }
  
  public int getListPaddingTop()
  {
    return mListPadding.top;
  }
  
  protected int getRightPaddingOffset()
  {
    int i;
    if ((mGroupFlags & 0x22) == 34) {
      i = 0;
    } else {
      i = mPaddingRight;
    }
    return i;
  }
  
  @ViewDebug.ExportedProperty
  public View getSelectedView()
  {
    if ((mItemCount > 0) && (mSelectedPosition >= 0)) {
      return getChildAt(mSelectedPosition - mFirstPosition);
    }
    return null;
  }
  
  int getSelectionModeForAccessibility()
  {
    switch (getChoiceMode())
    {
    default: 
      return 0;
    case 2: 
    case 3: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  public Drawable getSelector()
  {
    return mSelector;
  }
  
  public int getSolidColor()
  {
    return mCacheColorHint;
  }
  
  public CharSequence getTextFilter()
  {
    if ((mTextFilterEnabled) && (mTextFilter != null)) {
      return mTextFilter.getText();
    }
    return null;
  }
  
  protected float getTopFadingEdgeStrength()
  {
    int i = getChildCount();
    float f1 = super.getTopFadingEdgeStrength();
    if (i == 0) {
      return f1;
    }
    if (mFirstPosition > 0) {
      return 1.0F;
    }
    i = getChildAt(0).getTop();
    float f2 = getVerticalFadingEdgeLength();
    if (i < mPaddingTop) {
      f1 = -(i - mPaddingTop) / f2;
    }
    return f1;
  }
  
  protected int getTopPaddingOffset()
  {
    int i;
    if ((mGroupFlags & 0x22) == 34) {
      i = 0;
    } else {
      i = -mPaddingTop;
    }
    return i;
  }
  
  public int getTranscriptMode()
  {
    return mTranscriptMode;
  }
  
  public int getVerticalScrollbarWidth()
  {
    if ((mFastScroll != null) && (mFastScroll.isEnabled())) {
      return Math.max(super.getVerticalScrollbarWidth(), mFastScroll.getWidth());
    }
    return super.getVerticalScrollbarWidth();
  }
  
  void handleBoundsChange()
  {
    if (mInLayout) {
      return;
    }
    int i = getChildCount();
    if (i > 0)
    {
      mDataChanged = true;
      rememberSyncState();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
        if ((localLayoutParams == null) || (width < 1) || (height < 1)) {
          localView.forceLayout();
        }
      }
    }
  }
  
  protected void handleDataChanged()
  {
    int i = mItemCount;
    int j = mLastHandledItemCount;
    mLastHandledItemCount = mItemCount;
    if ((mChoiceMode != 0) && (mAdapter != null) && (mAdapter.hasStableIds())) {
      confirmCheckedPositionsById();
    }
    mRecycler.clearTransientStateViews();
    int k = 3;
    int i1;
    if (i > 0)
    {
      int n;
      if (mNeedSync)
      {
        mNeedSync = false;
        mPendingSync = null;
        if (mTranscriptMode == 2)
        {
          mLayoutMode = 3;
          return;
        }
        if (mTranscriptMode == 1)
        {
          if (mForceTranscriptScroll)
          {
            mForceTranscriptScroll = false;
            mLayoutMode = 3;
            return;
          }
          int m = getChildCount();
          n = getHeight() - getPaddingBottom();
          View localView = getChildAt(m - 1);
          if (localView != null) {
            i1 = localView.getBottom();
          } else {
            i1 = n;
          }
          if ((mFirstPosition + m >= j) && (i1 <= n))
          {
            mLayoutMode = 3;
            return;
          }
          awakenScrollBars();
        }
        switch (mSyncMode)
        {
        default: 
          break;
        case 1: 
          mLayoutMode = 5;
          mSyncPosition = Math.min(Math.max(0, mSyncPosition), i - 1);
          return;
        case 0: 
          if (isInTouchMode())
          {
            mLayoutMode = 5;
            mSyncPosition = Math.min(Math.max(0, mSyncPosition), i - 1);
            return;
          }
          i1 = findSyncPosition();
          if ((i1 >= 0) && (lookForSelectablePosition(i1, true) == i1))
          {
            mSyncPosition = i1;
            if (mSyncHeight == getHeight()) {
              mLayoutMode = 5;
            } else {
              mLayoutMode = 2;
            }
            setNextSelectedPositionInt(i1);
            return;
          }
          break;
        }
      }
      if (!isInTouchMode())
      {
        n = getSelectedItemPosition();
        i1 = n;
        if (n >= i) {
          i1 = i - 1;
        }
        n = i1;
        if (i1 < 0) {
          n = 0;
        }
        i1 = lookForSelectablePosition(n, true);
        if (i1 >= 0)
        {
          setNextSelectedPositionInt(i1);
          return;
        }
        i1 = lookForSelectablePosition(n, false);
        if (i1 >= 0) {
          setNextSelectedPositionInt(i1);
        }
      }
      else if (mResurrectToPosition >= 0)
      {
        return;
      }
    }
    if (mStackFromBottom) {
      i1 = k;
    } else {
      i1 = 1;
    }
    mLayoutMode = i1;
    mSelectedPosition = -1;
    mSelectedRowId = Long.MIN_VALUE;
    mNextSelectedPosition = -1;
    mNextSelectedRowId = Long.MIN_VALUE;
    mNeedSync = false;
    mPendingSync = null;
    mSelectorPosition = -1;
    checkSelectionChanged();
  }
  
  protected boolean handleScrollBarDragging(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public boolean hasTextFilter()
  {
    return mFiltered;
  }
  
  void hideSelector()
  {
    if (mSelectedPosition != -1)
    {
      if (mLayoutMode != 4) {
        mResurrectToPosition = mSelectedPosition;
      }
      if ((mNextSelectedPosition >= 0) && (mNextSelectedPosition != mSelectedPosition)) {
        mResurrectToPosition = mNextSelectedPosition;
      }
      setSelectedPositionInt(-1);
      setNextSelectedPositionInt(-1);
      mSelectedTop = 0;
    }
  }
  
  protected void internalSetPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.internalSetPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    if (isLayoutRequested()) {
      handleBoundsChange();
    }
  }
  
  public void invalidateViews()
  {
    mDataChanged = true;
    rememberSyncState();
    requestLayout();
    invalidate();
  }
  
  void invokeOnItemScrollListener()
  {
    if (mFastScroll != null) {
      mFastScroll.onScroll(mFirstPosition, getChildCount(), mItemCount);
    }
    if (mOnScrollListener != null) {
      mOnScrollListener.onScroll(this, mFirstPosition, getChildCount(), mItemCount);
    }
    onScrollChanged(0, 0, 0, 0);
  }
  
  public boolean isFastScrollAlwaysVisible()
  {
    FastScroller localFastScroller = mFastScroll;
    boolean bool1 = false;
    boolean bool2 = false;
    if (localFastScroller == null)
    {
      bool3 = bool2;
      if (mFastScrollEnabled)
      {
        bool3 = bool2;
        if (mFastScrollAlwaysVisible) {
          bool3 = true;
        }
      }
      return bool3;
    }
    boolean bool3 = bool1;
    if (mFastScroll.isEnabled())
    {
      bool3 = bool1;
      if (mFastScroll.isAlwaysShowEnabled()) {
        bool3 = true;
      }
    }
    return bool3;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isFastScrollEnabled()
  {
    if (mFastScroll == null) {
      return mFastScrollEnabled;
    }
    return mFastScroll.isEnabled();
  }
  
  protected boolean isInFilterMode()
  {
    return mFiltered;
  }
  
  public boolean isItemChecked(int paramInt)
  {
    if ((mChoiceMode != 0) && (mCheckStates != null)) {
      return mCheckStates.get(paramInt);
    }
    return false;
  }
  
  protected boolean isPaddingOffsetRequired()
  {
    boolean bool;
    if ((mGroupFlags & 0x22) != 34) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isScrollingCacheEnabled()
  {
    return mScrollingCacheEnabled;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isSmoothScrollbarEnabled()
  {
    return mSmoothScrollbarEnabled;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isStackFromBottom()
  {
    return mStackFromBottom;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isTextFilterEnabled()
  {
    return mTextFilterEnabled;
  }
  
  protected boolean isVerticalScrollBarHidden()
  {
    return isFastScrollEnabled();
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mSelector != null) {
      mSelector.jumpToCurrentState();
    }
  }
  
  void keyPressed()
  {
    if ((isEnabled()) && (isClickable()))
    {
      Drawable localDrawable = mSelector;
      Object localObject = mSelectorRect;
      if ((localDrawable != null) && ((isFocused()) || (touchModeDrawsInPressedState())) && (!((Rect)localObject).isEmpty()))
      {
        localObject = getChildAt(mSelectedPosition - mFirstPosition);
        if (localObject != null)
        {
          if (((View)localObject).hasExplicitFocusable()) {
            return;
          }
          ((View)localObject).setPressed(true);
        }
        setPressed(true);
        boolean bool = isLongClickable();
        localDrawable = localDrawable.getCurrent();
        if ((localDrawable != null) && ((localDrawable instanceof TransitionDrawable))) {
          if (bool) {
            ((TransitionDrawable)localDrawable).startTransition(ViewConfiguration.getLongPressTimeout());
          } else {
            ((TransitionDrawable)localDrawable).resetTransition();
          }
        }
        if ((bool) && (!mDataChanged))
        {
          if (mPendingCheckForKeyLongPress == null) {
            mPendingCheckForKeyLongPress = new CheckForKeyLongPress(null);
          }
          mPendingCheckForKeyLongPress.rememberWindowAttachCount();
          postDelayed(mPendingCheckForKeyLongPress, ViewConfiguration.getLongPressTimeout());
        }
      }
      return;
    }
  }
  
  protected void layoutChildren() {}
  
  View obtainView(int paramInt, boolean[] paramArrayOfBoolean)
  {
    Trace.traceBegin(8L, "obtainView");
    paramArrayOfBoolean[0] = false;
    View localView1 = mRecycler.getTransientStateView(paramInt);
    if (localView1 != null)
    {
      if (getLayoutParamsviewType == mAdapter.getItemViewType(paramInt))
      {
        localView2 = mAdapter.getView(paramInt, localView1, this);
        if (localView2 != localView1)
        {
          setItemViewLayoutParams(localView2, paramInt);
          mRecycler.addScrapView(localView2, paramInt);
        }
      }
      paramArrayOfBoolean[0] = true;
      localView1.dispatchFinishTemporaryDetach();
      return localView1;
    }
    localView1 = mRecycler.getScrapView(paramInt);
    View localView2 = mAdapter.getView(paramInt, localView1, this);
    if (localView1 != null) {
      if (localView2 != localView1)
      {
        mRecycler.addScrapView(localView1, paramInt);
      }
      else if (localView2.isTemporarilyDetached())
      {
        paramArrayOfBoolean[0] = true;
        localView2.dispatchFinishTemporaryDetach();
      }
    }
    if (mCacheColorHint != 0) {
      localView2.setDrawingCacheBackgroundColor(mCacheColorHint);
    }
    if (localView2.getImportantForAccessibility() == 0) {
      localView2.setImportantForAccessibility(1);
    }
    setItemViewLayoutParams(localView2, paramInt);
    if (AccessibilityManager.getInstance(mContext).isEnabled())
    {
      if (mAccessibilityDelegate == null) {
        mAccessibilityDelegate = new ListItemAccessibilityDelegate();
      }
      if (localView2.getAccessibilityDelegate() == null) {
        localView2.setAccessibilityDelegate(mAccessibilityDelegate);
      }
    }
    Trace.traceEnd(8L);
    return localView2;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    localViewTreeObserver.addOnTouchModeChangeListener(this);
    if ((mTextFilterEnabled) && (mPopup != null) && (!mGlobalLayoutListenerAddedFilter)) {
      localViewTreeObserver.addOnGlobalLayoutListener(this);
    }
    if ((mAdapter != null) && (mDataSetObserver == null))
    {
      mDataSetObserver = new AdapterDataSetObserver();
      mAdapter.registerDataSetObserver(mDataSetObserver);
      mDataChanged = true;
      mOldItemCount = mItemCount;
      mItemCount = mAdapter.getCount();
    }
  }
  
  public void onCancelPendingInputEvents()
  {
    super.onCancelPendingInputEvents();
    if (mPerformClick != null) {
      removeCallbacks(mPerformClick);
    }
    if (mPendingCheckForTap != null) {
      removeCallbacks(mPendingCheckForTap);
    }
    if (mPendingCheckForLongPress != null) {
      removeCallbacks(mPendingCheckForLongPress);
    }
    if (mPendingCheckForKeyLongPress != null) {
      removeCallbacks(mPendingCheckForKeyLongPress);
    }
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo)
  {
    if (isTextFilterEnabled())
    {
      if (mPublicInputConnection == null)
      {
        mDefInputConnection = new BaseInputConnection(this, false);
        mPublicInputConnection = new InputConnectionWrapper(paramEditorInfo);
      }
      inputType = 177;
      imeOptions = 6;
      return mPublicInputConnection;
    }
    return null;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    mIsDetaching = true;
    dismissPopup();
    mRecycler.clear();
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    localViewTreeObserver.removeOnTouchModeChangeListener(this);
    if ((mTextFilterEnabled) && (mPopup != null))
    {
      localViewTreeObserver.removeOnGlobalLayoutListener(this);
      mGlobalLayoutListenerAddedFilter = false;
    }
    if ((mAdapter != null) && (mDataSetObserver != null))
    {
      mAdapter.unregisterDataSetObserver(mDataSetObserver);
      mDataSetObserver = null;
    }
    if (mScrollStrictSpan != null)
    {
      mScrollStrictSpan.finish();
      mScrollStrictSpan = null;
    }
    if (mFlingStrictSpan != null)
    {
      mFlingStrictSpan.finish();
      mFlingStrictSpan = null;
    }
    if (mFlingRunnable != null) {
      removeCallbacks(mFlingRunnable);
    }
    if (mPositionScroller != null) {
      mPositionScroller.stop();
    }
    if (mClearScrollingCache != null) {
      removeCallbacks(mClearScrollingCache);
    }
    if (mPerformClick != null) {
      removeCallbacks(mPerformClick);
    }
    if (mTouchModeReset != null)
    {
      removeCallbacks(mTouchModeReset);
      mTouchModeReset.run();
    }
    mIsDetaching = false;
  }
  
  protected void onDisplayHint(int paramInt)
  {
    super.onDisplayHint(paramInt);
    if (paramInt != 0)
    {
      if ((paramInt == 4) && (mPopup != null) && (mPopup.isShowing())) {
        dismissPopup();
      }
    }
    else if ((mFiltered) && (mPopup != null) && (!mPopup.isShowing())) {
      showPopup();
    }
    boolean bool;
    if (paramInt == 4) {
      bool = true;
    } else {
      bool = false;
    }
    mPopupHidden = bool;
  }
  
  public void onFilterComplete(int paramInt)
  {
    if ((mSelectedPosition < 0) && (paramInt > 0))
    {
      mResurrectToPosition = -1;
      resurrectSelection();
    }
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if ((paramBoolean) && (mSelectedPosition < 0) && (!isInTouchMode()))
    {
      if ((!isAttachedToWindow()) && (mAdapter != null))
      {
        mDataChanged = true;
        mOldItemCount = mItemCount;
        mItemCount = mAdapter.getCount();
      }
      resurrectSelection();
    }
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if (i != 8)
    {
      if ((i == 11) && (paramMotionEvent.isFromSource(2)))
      {
        i = paramMotionEvent.getActionButton();
        if (((i == 32) || (i == 2)) && ((mTouchMode == 0) || (mTouchMode == 1)) && (performStylusButtonPressAction(paramMotionEvent)))
        {
          removeCallbacks(mPendingCheckForLongPress);
          removeCallbacks(mPendingCheckForTap);
        }
      }
    }
    else
    {
      float f;
      if (paramMotionEvent.isFromSource(2)) {
        f = paramMotionEvent.getAxisValue(9);
      }
      for (;;)
      {
        break;
        if (paramMotionEvent.isFromSource(4194304)) {
          f = paramMotionEvent.getAxisValue(26);
        } else {
          f = 0.0F;
        }
      }
      i = Math.round(mVerticalScrollFactor * f);
      if ((i != 0) && (!trackMotionScroll(i, i))) {
        return true;
      }
    }
    return super.onGenericMotionEvent(paramMotionEvent);
  }
  
  public void onGlobalLayout()
  {
    if (isShown())
    {
      if ((mFiltered) && (mPopup != null) && (!mPopup.isShowing()) && (!mPopupHidden)) {
        showPopup();
      }
    }
    else if ((mPopup != null) && (mPopup.isShowing())) {
      dismissPopup();
    }
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(View paramView, int paramInt, AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    if (paramInt == -1) {
      return;
    }
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    boolean bool;
    if ((localLayoutParams instanceof LayoutParams)) {
      bool = isEnabled;
    } else {
      bool = false;
    }
    if ((isEnabled()) && (bool))
    {
      if (paramInt == getSelectedItemPosition())
      {
        paramAccessibilityNodeInfo.setSelected(true);
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_SELECTION);
      }
      else
      {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SELECT);
      }
      if (isItemClickable(paramView))
      {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
        paramAccessibilityNodeInfo.setClickable(true);
      }
      if (isLongClickable())
      {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK);
        paramAccessibilityNodeInfo.setLongClickable(true);
      }
      return;
    }
    paramAccessibilityNodeInfo.setEnabled(false);
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (isEnabled())
    {
      if (canScrollUp())
      {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
        paramAccessibilityNodeInfo.setScrollable(true);
      }
      if (canScrollDown())
      {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN);
        paramAccessibilityNodeInfo.setScrollable(true);
      }
    }
    paramAccessibilityNodeInfo.removeAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
    paramAccessibilityNodeInfo.setClickable(false);
  }
  
  public boolean onInterceptHoverEvent(MotionEvent paramMotionEvent)
  {
    if ((mFastScroll != null) && (mFastScroll.onInterceptHoverEvent(paramMotionEvent))) {
      return true;
    }
    return super.onInterceptHoverEvent(paramMotionEvent);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (mPositionScroller != null) {
      mPositionScroller.stop();
    }
    if ((!mIsDetaching) && (isAttachedToWindow()))
    {
      if ((mFastScroll != null) && (mFastScroll.onInterceptTouchEvent(paramMotionEvent))) {
        return true;
      }
      if (i != 6)
      {
        int j;
        switch (i)
        {
        default: 
          break;
        case 2: 
          mNumTouchMoveEvent += 1;
          if (mNumTouchMoveEvent == 1) {
            mIsFirstTouchMoveEvent = true;
          } else {
            mIsFirstTouchMoveEvent = false;
          }
          if (mTouchMode == 0)
          {
            j = paramMotionEvent.findPointerIndex(mActivePointerId);
            i = j;
            if (j == -1)
            {
              i = 0;
              mActivePointerId = paramMotionEvent.getPointerId(0);
            }
            j = (int)paramMotionEvent.getY(i);
            initVelocityTrackerIfNotExists();
            mVelocityTracker.addMovement(paramMotionEvent);
            if (startScrollIfNeeded((int)paramMotionEvent.getX(i), j, null)) {
              return true;
            }
          }
          break;
        case 1: 
        case 3: 
          mNumTouchMoveEvent = 0;
          mTouchMode = -1;
          mActivePointerId = -1;
          recycleVelocityTracker();
          reportScrollStateChange(0);
          stopNestedScroll();
          break;
        case 0: 
          mNumTouchMoveEvent = 0;
          j = mTouchMode;
          if ((j != 6) && (j != 5))
          {
            int k = (int)paramMotionEvent.getX();
            int m = (int)paramMotionEvent.getY();
            mActivePointerId = paramMotionEvent.getPointerId(0);
            i = findMotionRow(m);
            if ((j != 4) && (i >= 0))
            {
              mMotionViewOriginalTop = getChildAt(i - mFirstPosition).getTop();
              mMotionX = k;
              mMotionY = m;
              mMotionPosition = i;
              mTouchMode = 0;
              clearScrollingCache();
            }
            mLastY = Integer.MIN_VALUE;
            initOrResetVelocityTracker();
            mVelocityTracker.addMovement(paramMotionEvent);
            mNestedYOffset = 0;
            startNestedScroll(2);
            if (j != 4) {
              break;
            }
            return true;
          }
          mMotionCorrection = 0;
          return true;
        }
      }
      else
      {
        mNumTouchMoveEvent = 0;
        onSecondaryPointerUp(paramMotionEvent);
      }
      return false;
    }
    return false;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (KeyEvent.isConfirmKey(paramInt))
    {
      if (!isEnabled()) {
        return true;
      }
      if ((isClickable()) && (isPressed()) && (mSelectedPosition >= 0) && (mAdapter != null) && (mSelectedPosition < mAdapter.getCount()))
      {
        paramKeyEvent = getChildAt(mSelectedPosition - mFirstPosition);
        if (paramKeyEvent != null)
        {
          performItemClick(paramKeyEvent, mSelectedPosition, mSelectedRowId);
          paramKeyEvent.setPressed(false);
        }
        setPressed(false);
        return true;
      }
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    mInLayout = true;
    paramInt3 = getChildCount();
    if (paramBoolean)
    {
      for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
        getChildAt(paramInt1).forceLayout();
      }
      mRecycler.markChildrenDirty();
    }
    layoutChildren();
    mOverscrollMax = ((paramInt4 - paramInt2) / 3);
    if (mFastScroll != null) {
      mFastScroll.onItemCountChanged(getChildCount(), mItemCount);
    }
    mInLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (mSelector == null) {
      useDefaultSelector();
    }
    Object localObject = mListPadding;
    left = (mSelectionLeftPadding + mPaddingLeft);
    top = (mSelectionTopPadding + mPaddingTop);
    right = (mSelectionRightPadding + mPaddingRight);
    bottom = (mSelectionBottomPadding + mPaddingBottom);
    paramInt1 = mTranscriptMode;
    boolean bool = true;
    if (paramInt1 == 1)
    {
      int i = getChildCount();
      paramInt2 = getHeight() - getPaddingBottom();
      localObject = getChildAt(i - 1);
      if (localObject != null) {
        paramInt1 = ((View)localObject).getBottom();
      } else {
        paramInt1 = paramInt2;
      }
      if ((mFirstPosition + i < mLastHandledItemCount) || (paramInt1 > paramInt2)) {
        bool = false;
      }
      mForceTranscriptScroll = bool;
    }
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    int i = getChildCount();
    if ((!paramBoolean) && (i > 0) && (canScrollList((int)paramFloat2)) && (Math.abs(paramFloat2) > mMinimumVelocity))
    {
      reportScrollStateChange(2);
      if (mFlingRunnable == null) {
        mFlingRunnable = new FlingRunnable();
      }
      if (!dispatchNestedPreFling(0.0F, paramFloat2)) {
        mFlingRunnable.start((int)paramFloat2);
      }
      return true;
    }
    return dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramView = getChildAt(getChildCount() / 2);
    if (paramView != null) {
      paramInt1 = paramView.getTop();
    } else {
      paramInt1 = 0;
    }
    if ((paramView == null) || (trackMotionScroll(-paramInt4, -paramInt4)))
    {
      if (paramView != null)
      {
        paramInt2 = paramView.getTop() - paramInt1;
        paramInt1 = paramInt4 - paramInt2;
      }
      else
      {
        paramInt1 = paramInt4;
        paramInt2 = 0;
      }
      dispatchNestedScroll(0, paramInt2, 0, paramInt1, null);
    }
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt)
  {
    super.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    startNestedScroll(2);
  }
  
  protected void onOverScrolled(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mScrollY != paramInt2)
    {
      onScrollChanged(mScrollX, paramInt2, mScrollX, mScrollY);
      mScrollY = paramInt2;
      invalidateParentIfNeeded();
      awakenScrollBars();
    }
  }
  
  public boolean onRemoteAdapterConnected()
  {
    if (mRemoteAdapter != mAdapter)
    {
      setAdapter(mRemoteAdapter);
      if (mDeferNotifyDataSetChanged)
      {
        mRemoteAdapter.notifyDataSetChanged();
        mDeferNotifyDataSetChanged = false;
      }
      return false;
    }
    if (mRemoteAdapter != null)
    {
      mRemoteAdapter.superNotifyDataSetChanged();
      return true;
    }
    return false;
  }
  
  public void onRemoteAdapterDisconnected() {}
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    if (mFastScroll != null)
    {
      PointerIcon localPointerIcon = mFastScroll.onResolvePointerIcon(paramMotionEvent, paramInt);
      if (localPointerIcon != null) {
        return localPointerIcon;
      }
    }
    return super.onResolvePointerIcon(paramMotionEvent, paramInt);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    mDataChanged = true;
    mSyncHeight = height;
    if (selectedId >= 0L)
    {
      mNeedSync = true;
      mPendingSync = paramParcelable;
      mSyncRowId = selectedId;
      mSyncPosition = position;
      mSpecificTop = viewTop;
      mSyncMode = 0;
    }
    else if (firstId >= 0L)
    {
      setSelectedPositionInt(-1);
      setNextSelectedPositionInt(-1);
      mSelectorPosition = -1;
      mNeedSync = true;
      mPendingSync = paramParcelable;
      mSyncRowId = firstId;
      mSyncPosition = position;
      mSpecificTop = viewTop;
      mSyncMode = 1;
    }
    setFilterText(filter);
    if (checkState != null) {
      mCheckStates = checkState;
    }
    if (checkIdState != null) {
      mCheckedIdStates = checkIdState;
    }
    mCheckedItemCount = checkedItemCount;
    if ((inActionMode) && (mChoiceMode == 3) && (mMultiChoiceModeCallback != null)) {
      mChoiceActionMode = startActionMode(mMultiChoiceModeCallback);
    }
    requestLayout();
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    if (mFastScroll != null) {
      mFastScroll.setScrollbarPosition(getVerticalScrollbarPosition());
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    dismissPopup();
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if (mPendingSync != null)
    {
      selectedId = mPendingSync.selectedId;
      firstId = mPendingSync.firstId;
      viewTop = mPendingSync.viewTop;
      position = mPendingSync.position;
      height = mPendingSync.height;
      filter = mPendingSync.filter;
      inActionMode = mPendingSync.inActionMode;
      checkedItemCount = mPendingSync.checkedItemCount;
      checkState = mPendingSync.checkState;
      checkIdState = mPendingSync.checkIdState;
      return localSavedState;
    }
    int i = getChildCount();
    boolean bool = true;
    int j = 0;
    if ((i > 0) && (mItemCount > 0)) {
      i = 1;
    } else {
      i = 0;
    }
    long l = getSelectedItemId();
    selectedId = l;
    height = getHeight();
    int k;
    if (l >= 0L)
    {
      viewTop = mSelectedTop;
      position = getSelectedItemPosition();
      firstId = -1L;
    }
    else if ((i != 0) && (mFirstPosition > 0))
    {
      viewTop = getChildAt(0).getTop();
      k = mFirstPosition;
      i = k;
      if (k >= mItemCount) {
        i = mItemCount - 1;
      }
      position = i;
      firstId = mAdapter.getItemId(i);
    }
    else
    {
      viewTop = 0;
      firstId = -1L;
      position = 0;
    }
    filter = null;
    Object localObject;
    if (mFiltered)
    {
      localObject = mTextFilter;
      if (localObject != null)
      {
        localObject = ((EditText)localObject).getText();
        if (localObject != null) {
          filter = localObject.toString();
        }
      }
    }
    if ((mChoiceMode != 3) || (mChoiceActionMode == null)) {
      bool = false;
    }
    inActionMode = bool;
    if (mCheckStates != null) {
      checkState = mCheckStates.clone();
    }
    if (mCheckedIdStates != null)
    {
      localObject = new LongSparseArray();
      k = mCheckedIdStates.size();
      for (i = j; i < k; i++) {
        ((LongSparseArray)localObject).put(mCheckedIdStates.keyAt(i), (Integer)mCheckedIdStates.valueAt(i));
      }
      checkIdState = ((LongSparseArray)localObject);
    }
    checkedItemCount = mCheckedItemCount;
    if (mRemoteAdapter != null) {
      mRemoteAdapter.saveRemoteViewsCache();
    }
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    handleBoundsChange();
    if (mFastScroll != null) {
      mFastScroll.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt)
  {
    boolean bool;
    if ((paramInt & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if (isTextFilterEnabled())
    {
      createTextFilter(true);
      paramInt1 = paramCharSequence.length();
      boolean bool = mPopup.isShowing();
      if ((!bool) && (paramInt1 > 0))
      {
        showPopup();
        mFiltered = true;
      }
      else if ((bool) && (paramInt1 == 0))
      {
        dismissPopup();
        mFiltered = false;
      }
      if ((mAdapter instanceof Filterable))
      {
        Filter localFilter = ((Filterable)mAdapter).getFilter();
        if (localFilter != null) {
          localFilter.filter(paramCharSequence, this);
        } else {
          throw new IllegalStateException("You cannot call onTextChanged with a non filterable adapter");
        }
      }
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = isEnabled();
    boolean bool2 = true;
    if (!bool1)
    {
      bool1 = bool2;
      if (!isClickable()) {
        if (isLongClickable()) {
          bool1 = bool2;
        } else {
          bool1 = false;
        }
      }
      return bool1;
    }
    if (mPositionScroller != null) {
      mPositionScroller.stop();
    }
    if ((!mIsDetaching) && (isAttachedToWindow()))
    {
      startNestedScroll(2);
      if ((mFastScroll != null) && (mFastScroll.onTouchEvent(paramMotionEvent))) {
        return true;
      }
      initVelocityTrackerIfNotExists();
      MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
      int i = paramMotionEvent.getActionMasked();
      if (i == 0) {
        mNestedYOffset = 0;
      }
      localMotionEvent.offsetLocation(0.0F, mNestedYOffset);
      int j;
      switch (i)
      {
      case 4: 
      default: 
        break;
      case 6: 
        onSecondaryPointerUp(paramMotionEvent);
        j = mMotionX;
        i = mMotionY;
        j = pointToPosition(j, i);
        if (j >= 0)
        {
          mMotionViewOriginalTop = getChildAt(j - mFirstPosition).getTop();
          mMotionPosition = j;
        }
        mLastY = i;
        mNumTouchMoveEvent = 0;
        break;
      case 5: 
        j = paramMotionEvent.getActionIndex();
        i = paramMotionEvent.getPointerId(j);
        int k = (int)paramMotionEvent.getX(j);
        j = (int)paramMotionEvent.getY(j);
        mMotionCorrection = 0;
        mActivePointerId = i;
        mMotionX = k;
        mMotionY = j;
        i = pointToPosition(k, j);
        if (i >= 0)
        {
          mMotionViewOriginalTop = getChildAt(i - mFirstPosition).getTop();
          mMotionPosition = i;
        }
        mLastY = j;
        mNumTouchMoveEvent = 0;
        break;
      case 3: 
        onTouchCancel();
        mNumTouchMoveEvent = 0;
        break;
      case 2: 
        mNumTouchMoveEvent += 1;
        if (mNumTouchMoveEvent == 1) {
          mIsFirstTouchMoveEvent = true;
        } else {
          mIsFirstTouchMoveEvent = false;
        }
        onTouchMove(paramMotionEvent, localMotionEvent);
        break;
      case 1: 
        onTouchUp(paramMotionEvent);
        mNumTouchMoveEvent = 0;
        break;
      case 0: 
        onTouchDown(paramMotionEvent);
        mNumTouchMoveEvent = 0;
      }
      if (mVelocityTracker != null) {
        mVelocityTracker.addMovement(localMotionEvent);
      }
      localMotionEvent.recycle();
      return true;
    }
    return false;
  }
  
  public void onTouchModeChanged(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      hideSelector();
      if ((getHeight() > 0) && (getChildCount() > 0)) {
        layoutChildren();
      }
      updateSelectorState();
    }
    else
    {
      int i = mTouchMode;
      if ((i == 5) || (i == 6))
      {
        if (mFlingRunnable != null) {
          mFlingRunnable.endFling();
        }
        if (mPositionScroller != null) {
          mPositionScroller.stop();
        }
        if (mScrollY != 0)
        {
          mScrollY = 0;
          invalidateParentCaches();
          finishGlows();
          invalidate();
        }
      }
    }
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    int i = isInTouchMode() ^ true;
    if (!paramBoolean)
    {
      setChildrenDrawingCacheEnabled(false);
      if (mFlingRunnable != null)
      {
        removeCallbacks(mFlingRunnable);
        FlingRunnable.access$502(mFlingRunnable, false);
        mFlingRunnable.endFling();
        if (mPositionScroller != null) {
          mPositionScroller.stop();
        }
        if (mScrollY != 0)
        {
          mScrollY = 0;
          invalidateParentCaches();
          finishGlows();
          invalidate();
        }
      }
      dismissPopup();
      if (i == 1) {
        mResurrectToPosition = mSelectedPosition;
      }
    }
    else
    {
      if ((mFiltered) && (!mPopupHidden)) {
        showPopup();
      }
      if ((i != mLastTouchMode) && (mLastTouchMode != -1)) {
        if (i == 1)
        {
          resurrectSelection();
        }
        else
        {
          hideSelector();
          mLayoutMode = 0;
          layoutChildren();
        }
      }
    }
    mLastTouchMode = i;
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (paramInt != 4096) {
      if ((paramInt != 8192) && (paramInt != 16908344))
      {
        if (paramInt != 16908346) {
          return false;
        }
      }
      else
      {
        if ((isEnabled()) && (canScrollUp()))
        {
          smoothScrollBy(-(getHeight() - mListPadding.top - mListPadding.bottom), 200);
          return true;
        }
        return false;
      }
    }
    if ((isEnabled()) && (canScrollDown()))
    {
      smoothScrollBy(getHeight() - mListPadding.top - mListPadding.bottom, 200);
      return true;
    }
    return false;
  }
  
  public boolean performItemClick(View paramView, int paramInt, long paramLong)
  {
    int i = 1;
    int j = 1;
    int k = 1;
    boolean bool2;
    if (mChoiceMode != 0)
    {
      bool1 = true;
      j = 0;
      if ((mChoiceMode != 2) && ((mChoiceMode != 3) || (mChoiceActionMode == null)))
      {
        k = i;
        if (mChoiceMode == 1)
        {
          if ((mCheckStates.get(paramInt, false) ^ true))
          {
            mCheckStates.clear();
            mCheckStates.put(paramInt, true);
            if ((mCheckedIdStates != null) && (mAdapter.hasStableIds()))
            {
              mCheckedIdStates.clear();
              mCheckedIdStates.put(mAdapter.getItemId(paramInt), Integer.valueOf(paramInt));
            }
            mCheckedItemCount = 1;
          }
          else if ((mCheckStates.size() == 0) || (!mCheckStates.valueAt(0)))
          {
            mCheckedItemCount = 0;
          }
          j = 1;
          k = i;
        }
      }
      else
      {
        bool2 = mCheckStates.get(paramInt, false) ^ true;
        mCheckStates.put(paramInt, bool2);
        if ((mCheckedIdStates != null) && (mAdapter.hasStableIds())) {
          if (bool2) {
            mCheckedIdStates.put(mAdapter.getItemId(paramInt), Integer.valueOf(paramInt));
          } else {
            mCheckedIdStates.delete(mAdapter.getItemId(paramInt));
          }
        }
        if (bool2) {
          mCheckedItemCount += 1;
        } else {
          mCheckedItemCount -= 1;
        }
        if (mChoiceActionMode != null)
        {
          mMultiChoiceModeCallback.onItemCheckedStateChanged(mChoiceActionMode, paramInt, paramLong, bool2);
          k = 0;
        }
        j = 1;
      }
      i = k;
      bool2 = bool1;
      if (j != 0)
      {
        updateOnScreenCheckedViews();
        i = k;
        bool2 = bool1;
      }
    }
    else
    {
      bool2 = false;
      i = j;
    }
    boolean bool1 = bool2;
    if (i != 0) {
      bool1 = bool2 | super.performItemClick(paramView, paramInt, paramLong);
    }
    return bool1;
  }
  
  boolean performLongPress(View paramView, int paramInt, long paramLong)
  {
    return performLongPress(paramView, paramInt, paramLong, -1.0F, -1.0F);
  }
  
  boolean performLongPress(View paramView, int paramInt, long paramLong, float paramFloat1, float paramFloat2)
  {
    if (mChoiceMode == 3)
    {
      if (mChoiceActionMode == null)
      {
        paramView = startActionMode(mMultiChoiceModeCallback);
        mChoiceActionMode = paramView;
        if (paramView != null)
        {
          setItemChecked(paramInt, true);
          performHapticFeedback(0);
        }
      }
      return true;
    }
    boolean bool1 = false;
    if (mOnItemLongClickListener != null) {
      bool1 = mOnItemLongClickListener.onItemLongClick(this, paramView, paramInt, paramLong);
    }
    boolean bool2 = bool1;
    if (!bool1)
    {
      mContextMenuInfo = createContextMenuInfo(paramView, paramInt, paramLong);
      if ((paramFloat1 != -1.0F) && (paramFloat2 != -1.0F)) {
        bool2 = super.showContextMenuForChild(this, paramFloat1, paramFloat2);
      } else {
        bool2 = super.showContextMenuForChild(this);
      }
    }
    if (bool2) {
      performHapticFeedback(0);
    }
    return bool2;
  }
  
  public int pointToPosition(int paramInt1, int paramInt2)
  {
    Object localObject1 = mTouchFrame;
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      mTouchFrame = new Rect();
      localObject2 = mTouchFrame;
    }
    for (int i = getChildCount() - 1; i >= 0; i--)
    {
      localObject1 = getChildAt(i);
      if (((View)localObject1).getVisibility() == 0)
      {
        ((View)localObject1).getHitRect((Rect)localObject2);
        if (((Rect)localObject2).contains(paramInt1, paramInt2)) {
          return mFirstPosition + i;
        }
      }
    }
    return -1;
  }
  
  public long pointToRowId(int paramInt1, int paramInt2)
  {
    paramInt1 = pointToPosition(paramInt1, paramInt2);
    if (paramInt1 >= 0) {
      return mAdapter.getItemId(paramInt1);
    }
    return Long.MIN_VALUE;
  }
  
  void positionSelector(int paramInt, View paramView)
  {
    positionSelector(paramInt, paramView, false, -1.0F, -1.0F);
  }
  
  void positionSelectorLikeFocus(int paramInt, View paramView)
  {
    if ((mSelector != null) && (mSelectorPosition != paramInt) && (paramInt != -1))
    {
      Rect localRect = mSelectorRect;
      positionSelector(paramInt, paramView, true, localRect.exactCenterX(), localRect.exactCenterY());
    }
    else
    {
      positionSelector(paramInt, paramView);
    }
  }
  
  void positionSelectorLikeTouch(int paramInt, View paramView, float paramFloat1, float paramFloat2)
  {
    positionSelector(paramInt, paramView, true, paramFloat1, paramFloat2);
  }
  
  public void reclaimViews(List<View> paramList)
  {
    int i = getChildCount();
    RecyclerListener localRecyclerListener = mRecycler.mRecyclerListener;
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if ((localLayoutParams != null) && (mRecycler.shouldRecycleViewType(viewType)))
      {
        paramList.add(localView);
        localView.setAccessibilityDelegate(null);
        if (localRecyclerListener != null) {
          localRecyclerListener.onMovedToScrapHeap(localView);
        }
      }
    }
    mRecycler.reclaimScrapViews(paramList);
    removeAllViewsInLayout();
  }
  
  int reconcileSelectedPosition()
  {
    int i = mSelectedPosition;
    int j = i;
    if (i < 0) {
      j = mResurrectToPosition;
    }
    return Math.min(Math.max(0, j), mItemCount - 1);
  }
  
  void reportScrollStateChange(int paramInt)
  {
    if ((paramInt != mLastScrollState) && (mOnScrollListener != null))
    {
      mLastScrollState = paramInt;
      mOnScrollListener.onScrollStateChanged(this, paramInt);
    }
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    if (paramBoolean) {
      recycleVelocityTracker();
    }
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout()
  {
    if ((!mBlockLayoutRequests) && (!mInLayout)) {
      super.requestLayout();
    }
  }
  
  void requestLayoutIfNecessary()
  {
    if (getChildCount() > 0)
    {
      resetList();
      requestLayout();
      invalidate();
    }
  }
  
  void resetList()
  {
    removeAllViewsInLayout();
    mFirstPosition = 0;
    mDataChanged = false;
    mPositionScrollAfterLayout = null;
    mNeedSync = false;
    mPendingSync = null;
    mOldSelectedPosition = -1;
    mOldSelectedRowId = Long.MIN_VALUE;
    setSelectedPositionInt(-1);
    setNextSelectedPositionInt(-1);
    mSelectedTop = 0;
    mSelectorPosition = -1;
    mSelectorRect.setEmpty();
    invalidate();
  }
  
  boolean resurrectSelection()
  {
    int i = getChildCount();
    if (i <= 0) {
      return false;
    }
    int j = 0;
    int k = mListPadding.top;
    int m = mBottom - mTop - mListPadding.bottom;
    int n = mFirstPosition;
    int i1 = mResurrectToPosition;
    boolean bool = true;
    View localView;
    int i2;
    if ((i1 >= n) && (i1 < n + i))
    {
      localView = getChildAt(i1 - mFirstPosition);
      j = localView.getTop();
      i2 = localView.getBottom();
      if (j < k)
      {
        k += getVerticalFadingEdgeLength();
      }
      else
      {
        k = j;
        if (i2 > m) {
          k = m - localView.getMeasuredHeight() - getVerticalFadingEdgeLength();
        }
      }
    }
    else
    {
      int i3;
      int i4;
      if (i1 < n)
      {
        i3 = n;
        m = 0;
        i4 = 0;
        for (;;)
        {
          i2 = m;
          j = i3;
          i1 = k;
          if (i4 >= i) {
            break;
          }
          j = getChildAt(i4).getTop();
          i1 = k;
          if (i4 == 0)
          {
            i2 = j;
            if (n <= 0)
            {
              m = i2;
              i1 = k;
              if (j >= k) {}
            }
            else
            {
              i1 = k + getVerticalFadingEdgeLength();
              m = i2;
            }
          }
          if (j >= i1)
          {
            k = n + i4;
            i2 = j;
            j = k;
            break;
          }
          i4++;
          k = i1;
        }
        k = i2;
        i1 = j;
      }
      else
      {
        i4 = mItemCount;
        bool = false;
        i1 = i - 1;
        k = j;
        while (i1 >= 0)
        {
          localView = getChildAt(i1);
          j = localView.getTop();
          int i5 = localView.getBottom();
          i3 = k;
          i2 = m;
          if (i1 == i - 1)
          {
            k = j;
            if (n + i >= i4)
            {
              i3 = k;
              i2 = m;
              if (i5 <= m) {}
            }
            else
            {
              i2 = m - getVerticalFadingEdgeLength();
              i3 = k;
            }
          }
          if (i5 <= i2)
          {
            k = j;
            i1 = n + i1;
            break label393;
          }
          i1--;
          k = i3;
          m = i2;
        }
        i1 = n + i - 1;
      }
    }
    label393:
    mResurrectToPosition = -1;
    removeCallbacks(mFlingRunnable);
    if (mPositionScroller != null) {
      mPositionScroller.stop();
    }
    mTouchMode = -1;
    clearScrollingCache();
    mSpecificTop = k;
    k = lookForSelectablePosition(i1, bool);
    if ((k >= n) && (k <= getLastVisiblePosition()))
    {
      mLayoutMode = 4;
      updateSelectorState();
      setSelectionInt(k);
      invokeOnItemScrollListener();
    }
    else
    {
      k = -1;
    }
    reportScrollStateChange(0);
    if (k >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean resurrectSelectionIfNeeded()
  {
    if ((mSelectedPosition < 0) && (resurrectSelection()))
    {
      updateSelectorState();
      return true;
    }
    return false;
  }
  
  public void scrollListBy(int paramInt)
  {
    trackMotionScroll(-paramInt, -paramInt);
  }
  
  public void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent)
  {
    if (paramAccessibilityEvent.getEventType() == 4096)
    {
      int i = getFirstVisiblePosition();
      int j = getLastVisiblePosition();
      if ((mLastAccessibilityScrollEventFromIndex == i) && (mLastAccessibilityScrollEventToIndex == j)) {
        return;
      }
      mLastAccessibilityScrollEventFromIndex = i;
      mLastAccessibilityScrollEventToIndex = j;
    }
    super.sendAccessibilityEventUnchecked(paramAccessibilityEvent);
  }
  
  boolean sendToTextFilter(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    if (!acceptFilter()) {
      return false;
    }
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = true;
    Object localObject;
    if (paramInt1 != 4)
    {
      if (paramInt1 != 62)
      {
        if (paramInt1 != 66) {
          switch (paramInt1)
          {
          default: 
            bool2 = bool3;
            break;
          }
        } else {
          bool2 = false;
        }
      }
      else {
        bool2 = mFiltered;
      }
    }
    else
    {
      bool3 = bool2;
      if (mFiltered)
      {
        bool3 = bool2;
        if (mPopup != null)
        {
          bool3 = bool2;
          if (mPopup.isShowing()) {
            if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
            {
              localObject = getKeyDispatcherState();
              if (localObject != null) {
                ((KeyEvent.DispatcherState)localObject).startTracking(paramKeyEvent, this);
              }
              bool3 = true;
            }
            else
            {
              bool3 = bool2;
              if (paramKeyEvent.getAction() == 1)
              {
                bool3 = bool2;
                if (paramKeyEvent.isTracking())
                {
                  bool3 = bool2;
                  if (!paramKeyEvent.isCanceled())
                  {
                    bool3 = true;
                    mTextFilter.setText("");
                  }
                }
              }
            }
          }
        }
      }
      bool2 = false;
      bool1 = bool3;
    }
    bool3 = bool1;
    if (bool2)
    {
      createTextFilter(true);
      KeyEvent localKeyEvent = paramKeyEvent;
      localObject = localKeyEvent;
      if (localKeyEvent.getRepeatCount() > 0) {
        localObject = KeyEvent.changeTimeRepeat(paramKeyEvent, paramKeyEvent.getEventTime(), 0);
      }
      switch (paramKeyEvent.getAction())
      {
      default: 
        bool3 = bool1;
        break;
      case 2: 
        bool3 = mTextFilter.onKeyMultiple(paramInt1, paramInt2, paramKeyEvent);
        break;
      case 1: 
        bool3 = mTextFilter.onKeyUp(paramInt1, (KeyEvent)localObject);
        break;
      case 0: 
        bool3 = mTextFilter.onKeyDown(paramInt1, (KeyEvent)localObject);
      }
    }
    return bool3;
  }
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    if (paramListAdapter != null)
    {
      mAdapterHasStableIds = mAdapter.hasStableIds();
      if ((mChoiceMode != 0) && (mAdapterHasStableIds) && (mCheckedIdStates == null)) {
        mCheckedIdStates = new LongSparseArray();
      }
    }
    clearChoices();
  }
  
  public void setCacheColorHint(int paramInt)
  {
    if (paramInt != mCacheColorHint)
    {
      mCacheColorHint = paramInt;
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        getChildAt(j).setDrawingCacheBackgroundColor(paramInt);
      }
      mRecycler.setCacheColorHint(paramInt);
    }
  }
  
  public void setChoiceMode(int paramInt)
  {
    mChoiceMode = paramInt;
    if (mChoiceActionMode != null)
    {
      mChoiceActionMode.finish();
      mChoiceActionMode = null;
    }
    if (mChoiceMode != 0)
    {
      if (mCheckStates == null) {
        mCheckStates = new SparseBooleanArray(0);
      }
      if ((mCheckedIdStates == null) && (mAdapter != null) && (mAdapter.hasStableIds())) {
        mCheckedIdStates = new LongSparseArray(0);
      }
      if (mChoiceMode == 3)
      {
        clearChoices();
        setLongClickable(true);
      }
    }
  }
  
  public void setDrawSelectorOnTop(boolean paramBoolean)
  {
    mDrawSelectorOnTop = paramBoolean;
  }
  
  public void setFastScrollAlwaysVisible(final boolean paramBoolean)
  {
    if (mFastScrollAlwaysVisible != paramBoolean)
    {
      if ((paramBoolean) && (!mFastScrollEnabled)) {
        setFastScrollEnabled(true);
      }
      mFastScrollAlwaysVisible = paramBoolean;
      if (isOwnerThread()) {
        setFastScrollerAlwaysVisibleUiThread(paramBoolean);
      } else {
        post(new Runnable()
        {
          public void run()
          {
            AbsListView.this.setFastScrollerAlwaysVisibleUiThread(paramBoolean);
          }
        });
      }
    }
  }
  
  public void setFastScrollEnabled(final boolean paramBoolean)
  {
    if (mFastScrollEnabled != paramBoolean)
    {
      mFastScrollEnabled = paramBoolean;
      if (isOwnerThread()) {
        setFastScrollerEnabledUiThread(paramBoolean);
      } else {
        post(new Runnable()
        {
          public void run()
          {
            AbsListView.this.setFastScrollerEnabledUiThread(paramBoolean);
          }
        });
      }
    }
  }
  
  public void setFastScrollStyle(int paramInt)
  {
    if (mFastScroll == null) {
      mFastScrollStyle = paramInt;
    } else {
      mFastScroll.setStyle(paramInt);
    }
  }
  
  public void setFilterText(String paramString)
  {
    if ((mTextFilterEnabled) && (!TextUtils.isEmpty(paramString)))
    {
      createTextFilter(false);
      mTextFilter.setText(paramString);
      mTextFilter.setSelection(paramString.length());
      if ((mAdapter instanceof Filterable))
      {
        if (mPopup == null) {
          ((Filterable)mAdapter).getFilter().filter(paramString);
        }
        mFiltered = true;
        mDataSetObserver.clearSavedState();
      }
    }
  }
  
  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    if (bool)
    {
      if (getWindowVisibility() == 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = 0;
      }
      if ((mFiltered) && (paramInt1 != 0) && (mPopup != null) && (mPopup.isShowing())) {
        positionPopup();
      }
    }
    return bool;
  }
  
  public void setFriction(float paramFloat)
  {
    if (mFlingRunnable == null) {
      mFlingRunnable = new FlingRunnable();
    }
    mFlingRunnable.mScroller.setFriction(paramFloat);
  }
  
  public void setItemChecked(int paramInt, boolean paramBoolean)
  {
    if (mChoiceMode == 0) {
      return;
    }
    if ((paramBoolean) && (mChoiceMode == 3) && (mChoiceActionMode == null)) {
      if ((mMultiChoiceModeCallback != null) && (mMultiChoiceModeCallback.hasWrappedCallback())) {
        mChoiceActionMode = startActionMode(mMultiChoiceModeCallback);
      } else {
        throw new IllegalStateException("AbsListView: attempted to start selection mode for CHOICE_MODE_MULTIPLE_MODAL but no choice mode callback was supplied. Call setMultiChoiceModeListener to set a callback.");
      }
    }
    int i = mChoiceMode;
    int j = 0;
    if ((i != 2) && (mChoiceMode != 3))
    {
      if ((mCheckedIdStates != null) && (mAdapter.hasStableIds())) {
        i = 1;
      } else {
        i = 0;
      }
      if (isItemChecked(paramInt) != paramBoolean) {
        j = 1;
      } else {
        j = 0;
      }
      if ((paramBoolean) || (isItemChecked(paramInt)))
      {
        mCheckStates.clear();
        if (i != 0) {
          mCheckedIdStates.clear();
        }
      }
      if (paramBoolean)
      {
        mCheckStates.put(paramInt, true);
        if (i != 0) {
          mCheckedIdStates.put(mAdapter.getItemId(paramInt), Integer.valueOf(paramInt));
        }
        mCheckedItemCount = 1;
        paramInt = j;
      }
      else if (mCheckStates.size() != 0)
      {
        paramInt = j;
        if (mCheckStates.valueAt(0)) {}
      }
      else
      {
        mCheckedItemCount = 0;
        paramInt = j;
      }
    }
    else
    {
      boolean bool = mCheckStates.get(paramInt);
      mCheckStates.put(paramInt, paramBoolean);
      if ((mCheckedIdStates != null) && (mAdapter.hasStableIds())) {
        if (paramBoolean) {
          mCheckedIdStates.put(mAdapter.getItemId(paramInt), Integer.valueOf(paramInt));
        } else {
          mCheckedIdStates.delete(mAdapter.getItemId(paramInt));
        }
      }
      if (bool != paramBoolean) {
        j = 1;
      }
      if (j != 0) {
        if (paramBoolean) {
          mCheckedItemCount += 1;
        } else {
          mCheckedItemCount -= 1;
        }
      }
      if (mChoiceActionMode != null)
      {
        long l = mAdapter.getItemId(paramInt);
        mMultiChoiceModeCallback.onItemCheckedStateChanged(mChoiceActionMode, paramInt, l, paramBoolean);
      }
      paramInt = j;
    }
    if ((!mInLayout) && (!mBlockLayoutRequests) && (paramInt != 0))
    {
      mDataChanged = true;
      rememberSyncState();
      requestLayout();
    }
  }
  
  public void setMultiChoiceModeListener(MultiChoiceModeListener paramMultiChoiceModeListener)
  {
    if (mMultiChoiceModeCallback == null) {
      mMultiChoiceModeCallback = new MultiChoiceModeWrapper();
    }
    mMultiChoiceModeCallback.setWrapped(paramMultiChoiceModeListener);
  }
  
  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    mOnScrollListener = paramOnScrollListener;
    invokeOnItemScrollListener();
  }
  
  public void setOverScrollMode(int paramInt)
  {
    if (paramInt != 2)
    {
      if (mEdgeGlowTop == null)
      {
        Context localContext = getContext();
        mEdgeGlowTop = new EdgeEffect(localContext);
        mEdgeGlowBottom = new EdgeEffect(localContext);
      }
    }
    else
    {
      mEdgeGlowTop = null;
      mEdgeGlowBottom = null;
    }
    super.setOverScrollMode(paramInt);
  }
  
  public void setRecyclerListener(RecyclerListener paramRecyclerListener)
  {
    RecycleBin.access$4302(mRecycler, paramRecyclerListener);
  }
  
  public void setRemoteViewsAdapter(Intent paramIntent)
  {
    setRemoteViewsAdapter(paramIntent, false);
  }
  
  public void setRemoteViewsAdapter(Intent paramIntent, boolean paramBoolean)
  {
    if ((mRemoteAdapter != null) && (new Intent.FilterComparison(paramIntent).equals(new Intent.FilterComparison(mRemoteAdapter.getRemoteViewsServiceIntent())))) {
      return;
    }
    mDeferNotifyDataSetChanged = false;
    mRemoteAdapter = new RemoteViewsAdapter(getContext(), paramIntent, this, paramBoolean);
    if (mRemoteAdapter.isDataReady()) {
      setAdapter(mRemoteAdapter);
    }
  }
  
  public Runnable setRemoteViewsAdapterAsync(Intent paramIntent)
  {
    return new RemoteViewsAdapter.AsyncRemoteAdapterAction(this, paramIntent);
  }
  
  public void setRemoteViewsOnClickHandler(RemoteViews.OnClickHandler paramOnClickHandler)
  {
    if (mRemoteAdapter != null) {
      mRemoteAdapter.setRemoteViewsOnClickHandler(paramOnClickHandler);
    }
  }
  
  public void setScrollBarStyle(int paramInt)
  {
    super.setScrollBarStyle(paramInt);
    if (mFastScroll != null) {
      mFastScroll.setScrollBarStyle(paramInt);
    }
  }
  
  public void setScrollIndicators(View paramView1, View paramView2)
  {
    mScrollUp = paramView1;
    mScrollDown = paramView2;
  }
  
  public void setScrollingCacheEnabled(boolean paramBoolean)
  {
    if ((mScrollingCacheEnabled) && (!paramBoolean)) {
      clearScrollingCache();
    }
    mScrollingCacheEnabled = paramBoolean;
  }
  
  public void setSelectionFromTop(int paramInt1, int paramInt2)
  {
    if (mAdapter == null) {
      return;
    }
    if (!isInTouchMode())
    {
      int i = lookForSelectablePosition(paramInt1, true);
      paramInt1 = i;
      if (i >= 0)
      {
        setNextSelectedPositionInt(i);
        paramInt1 = i;
      }
    }
    else
    {
      mResurrectToPosition = paramInt1;
    }
    if (paramInt1 >= 0)
    {
      mLayoutMode = 4;
      mSpecificTop = (mListPadding.top + paramInt2);
      if (mNeedSync)
      {
        mSyncPosition = paramInt1;
        mSyncRowId = mAdapter.getItemId(paramInt1);
      }
      if (mPositionScroller != null) {
        mPositionScroller.stop();
      }
      requestLayout();
    }
  }
  
  abstract void setSelectionInt(int paramInt);
  
  public void setSelector(int paramInt)
  {
    setSelector(getContext().getDrawable(paramInt));
  }
  
  public void setSelector(Drawable paramDrawable)
  {
    if (mSelector != null)
    {
      mSelector.setCallback(null);
      unscheduleDrawable(mSelector);
    }
    mSelector = paramDrawable;
    Rect localRect = new Rect();
    paramDrawable.getPadding(localRect);
    mSelectionLeftPadding = left;
    mSelectionTopPadding = top;
    mSelectionRightPadding = right;
    mSelectionBottomPadding = bottom;
    paramDrawable.setCallback(this);
    updateSelectorState();
  }
  
  public void setSmoothScrollbarEnabled(boolean paramBoolean)
  {
    mSmoothScrollbarEnabled = paramBoolean;
  }
  
  public void setStackFromBottom(boolean paramBoolean)
  {
    if (mStackFromBottom != paramBoolean)
    {
      mStackFromBottom = paramBoolean;
      requestLayoutIfNecessary();
    }
  }
  
  public void setTextFilterEnabled(boolean paramBoolean)
  {
    mTextFilterEnabled = paramBoolean;
  }
  
  public void setTranscriptMode(int paramInt)
  {
    mTranscriptMode = paramInt;
  }
  
  public void setVelocityScale(float paramFloat)
  {
    mVelocityScale = paramFloat;
  }
  
  public void setVerticalScrollbarPosition(int paramInt)
  {
    super.setVerticalScrollbarPosition(paramInt);
    if (mFastScroll != null) {
      mFastScroll.setScrollbarPosition(paramInt);
    }
  }
  
  void setVisibleRangeHint(int paramInt1, int paramInt2)
  {
    if (mRemoteAdapter != null) {
      mRemoteAdapter.setVisibleRangeHint(paramInt1, paramInt2);
    }
  }
  
  public final boolean shouldDrawSelector()
  {
    return mSelectorRect.isEmpty() ^ true;
  }
  
  boolean shouldShowSelector()
  {
    boolean bool;
    if (((isFocused()) && (!isInTouchMode())) || ((touchModeDrawsInPressedState()) && (isPressed()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean showContextMenu()
  {
    return showContextMenuInternal(0.0F, 0.0F, false);
  }
  
  public boolean showContextMenu(float paramFloat1, float paramFloat2)
  {
    return showContextMenuInternal(paramFloat1, paramFloat2, true);
  }
  
  public boolean showContextMenuForChild(View paramView)
  {
    if (isShowingContextMenuWithCoords()) {
      return false;
    }
    return showContextMenuForChildInternal(paramView, 0.0F, 0.0F, false);
  }
  
  public boolean showContextMenuForChild(View paramView, float paramFloat1, float paramFloat2)
  {
    return showContextMenuForChildInternal(paramView, paramFloat1, paramFloat2, true);
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2)
  {
    smoothScrollBy(paramInt1, paramInt2, false, false);
  }
  
  void smoothScrollBy(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mFlingRunnable == null) {
      mFlingRunnable = new FlingRunnable();
    }
    int i = mFirstPosition;
    int j = getChildCount();
    int k = getPaddingTop();
    int m = getHeight();
    int n = getPaddingBottom();
    if ((paramInt1 != 0) && (mItemCount != 0) && (j != 0) && ((i != 0) || (getChildAt(0).getTop() != k) || (paramInt1 >= 0)) && ((i + j != mItemCount) || (getChildAt(j - 1).getBottom() != m - n) || (paramInt1 <= 0)))
    {
      reportScrollStateChange(2);
      mFlingRunnable.startScroll(paramInt1, paramInt2, paramBoolean1, paramBoolean2);
    }
    else
    {
      mFlingRunnable.endFling();
      if (mPositionScroller != null) {
        mPositionScroller.stop();
      }
    }
  }
  
  void smoothScrollByOffset(int paramInt)
  {
    int i = -1;
    if (paramInt < 0) {
      i = getFirstVisiblePosition();
    } else if (paramInt > 0) {
      i = getLastVisiblePosition();
    }
    if (i > -1)
    {
      View localView = getChildAt(i - getFirstVisiblePosition());
      if (localView != null)
      {
        Rect localRect = new Rect();
        int j = i;
        if (localView.getGlobalVisibleRect(localRect))
        {
          int k = localView.getWidth();
          j = localView.getHeight();
          float f = localRect.width() * localRect.height() / (k * j);
          if ((paramInt < 0) && (f < 0.75F))
          {
            j = i + 1;
          }
          else
          {
            j = i;
            if (paramInt > 0)
            {
              j = i;
              if (f < 0.75F) {
                j = i - 1;
              }
            }
          }
        }
        smoothScrollToPosition(Math.max(0, Math.min(getCount(), j + paramInt)));
      }
    }
  }
  
  public void smoothScrollToPosition(int paramInt)
  {
    if (mPositionScroller == null) {
      mPositionScroller = createPositionScroller();
    }
    mPositionScroller.start(paramInt);
  }
  
  public void smoothScrollToPosition(int paramInt1, int paramInt2)
  {
    if (mPositionScroller == null) {
      mPositionScroller = createPositionScroller();
    }
    mPositionScroller.start(paramInt1, paramInt2);
  }
  
  public void smoothScrollToPositionFromTop(int paramInt1, int paramInt2)
  {
    if (mPositionScroller == null) {
      mPositionScroller = createPositionScroller();
    }
    mPositionScroller.startWithOffset(paramInt1, paramInt2);
  }
  
  public void smoothScrollToPositionFromTop(int paramInt1, int paramInt2, int paramInt3)
  {
    if (mPositionScroller == null) {
      mPositionScroller = createPositionScroller();
    }
    mPositionScroller.startWithOffset(paramInt1, paramInt2, paramInt3);
  }
  
  boolean touchModeDrawsInPressedState()
  {
    switch (mTouchMode)
    {
    default: 
      return false;
    }
    return true;
  }
  
  boolean trackMotionScroll(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    if (i == 0) {
      return true;
    }
    int j = getChildAt(0).getTop();
    int k = getChildAt(i - 1).getBottom();
    Object localObject = mListPadding;
    int m = 0;
    int n = 0;
    if ((mGroupFlags & 0x22) == 34)
    {
      m = top;
      n = bottom;
    }
    int i1 = getHeight() - n;
    n = getHeight() - mPaddingBottom - mPaddingTop;
    if (paramInt1 < 0) {}
    for (int i2 = Math.max(-(n - 1), paramInt1);; i2 = Math.min(n - 1, paramInt1)) {
      break;
    }
    if (paramInt2 < 0) {}
    for (int i3 = Math.max(-(n - 1), paramInt2);; i3 = Math.min(n - 1, paramInt2)) {
      break;
    }
    int i4 = mFirstPosition;
    if (i4 == 0) {
      mFirstPositionDistanceGuess = (j - top);
    } else {
      mFirstPositionDistanceGuess += i3;
    }
    if (i4 + i == mItemCount) {
      mLastPositionDistanceGuess = (bottom + k);
    } else {
      mLastPositionDistanceGuess += i3;
    }
    if ((i4 == 0) && (j >= top) && (i3 >= 0)) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    }
    if ((i4 + i == mItemCount) && (k <= getHeight() - bottom) && (i3 <= 0)) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    }
    if ((paramInt1 == 0) && (paramInt2 == 0))
    {
      if (i3 < 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2 = isInTouchMode();
      if (bool2) {
        hideSelector();
      }
      int i5 = getHeaderViewsCount();
      int i6 = mItemCount - getFooterViewsCount();
      paramInt2 = 0;
      int i7 = 0;
      n = 0;
      paramInt1 = 0;
      int i8;
      if (bool1)
      {
        n = -i3;
        paramInt2 = n;
        if ((mGroupFlags & 0x22) == 34) {
          paramInt2 = n + top;
        }
        i8 = 0;
        n = i1;
        while (i8 < i)
        {
          localObject = getChildAt(i8);
          if (((View)localObject).getBottom() >= paramInt2)
          {
            paramInt2 = i7;
            break label486;
          }
          paramInt1++;
          int i9 = i4 + i8;
          if ((i9 >= i5) && (i9 < i6))
          {
            ((View)localObject).clearAccessibilityFocus();
            mRecycler.addScrapView((View)localObject, i9);
          }
          i8++;
        }
        paramInt2 = i7;
      }
      else
      {
        label486:
        paramInt1 = getHeight() - i3;
        i8 = paramInt1;
        if ((mGroupFlags & 0x22) == 34) {
          i8 = paramInt1 - bottom;
        }
        i7 = i - 1;
        paramInt1 = n;
        for (n = i7;; n--)
        {
          if (n < 0) {
            break;
          }
          localObject = getChildAt(n);
          if (((View)localObject).getTop() <= i8) {
            break;
          }
          paramInt2 = n;
          paramInt1++;
          i7 = i4 + n;
          if ((i7 >= i5) && (i7 < i6))
          {
            ((View)localObject).clearAccessibilityFocus();
            mRecycler.addScrapView((View)localObject, i7);
          }
        }
      }
      mMotionViewNewTop = (mMotionViewOriginalTop + i2);
      mBlockLayoutRequests = true;
      if (paramInt1 > 0)
      {
        detachViewsFromParent(paramInt2, paramInt1);
        mRecycler.removeSkippedScrap();
      }
      if (!awakenScrollBars()) {
        invalidate();
      }
      offsetChildrenTopAndBottom(i3);
      if (bool1) {
        mFirstPosition += paramInt1;
      }
      paramInt1 = Math.abs(i3);
      if ((m - j < paramInt1) || (k - i1 < paramInt1)) {
        fillGap(bool1);
      }
      if (PREOBTAIN_ENABLE)
      {
        mOldIncrementalDeltaY = paramInt1;
        findNextGap(bool1);
      }
      mRecycler.fullyDetachScrapViews();
      paramInt2 = 0;
      paramInt1 = 0;
      if (!bool2) {
        if (mSelectedPosition != -1)
        {
          paramInt2 = mSelectedPosition - mFirstPosition;
          if ((paramInt2 >= 0) && (paramInt2 < getChildCount()))
          {
            positionSelector(mSelectedPosition, getChildAt(paramInt2));
            paramInt1 = 1;
          }
          break label849;
        }
      }
      paramInt1 = paramInt2;
      if (mSelectorPosition != -1)
      {
        n = mSelectorPosition - mFirstPosition;
        paramInt1 = paramInt2;
        if (n >= 0)
        {
          paramInt1 = paramInt2;
          if (n < getChildCount())
          {
            positionSelector(mSelectorPosition, getChildAt(n));
            paramInt1 = 1;
          }
        }
      }
      label849:
      if (paramInt1 == 0) {
        mSelectorRect.setEmpty();
      }
      mBlockLayoutRequests = false;
      invokeOnItemScrollListener();
      return false;
    }
    boolean bool1 = false;
    if (i3 != 0) {
      bool1 = true;
    }
    return bool1;
  }
  
  void updateScrollIndicators()
  {
    View localView = mScrollUp;
    int i = 4;
    int j;
    if (localView != null)
    {
      localView = mScrollUp;
      if (canScrollUp()) {
        j = 0;
      } else {
        j = 4;
      }
      localView.setVisibility(j);
    }
    if (mScrollDown != null)
    {
      localView = mScrollDown;
      j = i;
      if (canScrollDown()) {
        j = 0;
      }
      localView.setVisibility(j);
    }
  }
  
  void updateSelectorState()
  {
    Drawable localDrawable = mSelector;
    if ((localDrawable != null) && (localDrawable.isStateful())) {
      if (shouldShowSelector())
      {
        if (localDrawable.setState(getDrawableStateForSelector())) {
          invalidateDrawable(localDrawable);
        }
      }
      else {
        localDrawable.setState(StateSet.NOTHING);
      }
    }
  }
  
  public boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((mSelector != paramDrawable) && (!super.verifyDrawable(paramDrawable))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static abstract class AbsPositionScroller
  {
    AbsPositionScroller() {}
    
    public abstract void start(int paramInt);
    
    public abstract void start(int paramInt1, int paramInt2);
    
    public abstract void startWithOffset(int paramInt1, int paramInt2);
    
    public abstract void startWithOffset(int paramInt1, int paramInt2, int paramInt3);
    
    public abstract void stop();
  }
  
  class AdapterDataSetObserver
    extends AdapterView<ListAdapter>.AdapterDataSetObserver
  {
    AdapterDataSetObserver()
    {
      super();
    }
    
    public void onChanged()
    {
      super.onChanged();
      if (mFastScroll != null) {
        mFastScroll.onSectionsChanged();
      }
    }
    
    public void onInvalidated()
    {
      super.onInvalidated();
      if (mFastScroll != null) {
        mFastScroll.onSectionsChanged();
      }
    }
  }
  
  private class CheckForKeyLongPress
    extends AbsListView.WindowRunnnable
    implements Runnable
  {
    private CheckForKeyLongPress()
    {
      super(null);
    }
    
    public void run()
    {
      if ((isPressed()) && (mSelectedPosition >= 0))
      {
        int i = mSelectedPosition;
        int j = mFirstPosition;
        View localView = getChildAt(i - j);
        if (!mDataChanged)
        {
          boolean bool = false;
          if (sameWindow()) {
            bool = performLongPress(localView, mSelectedPosition, mSelectedRowId);
          }
          if (bool)
          {
            setPressed(false);
            localView.setPressed(false);
          }
        }
        else
        {
          setPressed(false);
          if (localView != null) {
            localView.setPressed(false);
          }
        }
      }
    }
  }
  
  private class CheckForLongPress
    extends AbsListView.WindowRunnnable
    implements Runnable
  {
    private static final int INVALID_COORD = -1;
    private float mX = -1.0F;
    private float mY = -1.0F;
    
    private CheckForLongPress()
    {
      super(null);
    }
    
    private void setCoords(float paramFloat1, float paramFloat2)
    {
      mX = paramFloat1;
      mY = paramFloat2;
    }
    
    public void run()
    {
      int i = mMotionPosition;
      View localView = getChildAt(i - mFirstPosition);
      if (localView != null)
      {
        i = mMotionPosition;
        long l = mAdapter.getItemId(mMotionPosition);
        boolean bool1 = false;
        boolean bool2 = bool1;
        if (sameWindow())
        {
          bool2 = bool1;
          if (!mDataChanged) {
            if ((mX != -1.0F) && (mY != -1.0F)) {
              bool2 = performLongPress(localView, i, l, mX, mY);
            } else {
              bool2 = performLongPress(localView, i, l);
            }
          }
        }
        if (bool2)
        {
          AbsListView.access$902(AbsListView.this, true);
          mTouchMode = -1;
          setPressed(false);
          localView.setPressed(false);
        }
        else
        {
          mTouchMode = 2;
        }
      }
    }
  }
  
  private final class CheckForTap
    implements Runnable
  {
    float x;
    float y;
    
    private CheckForTap() {}
    
    public void run()
    {
      if (mTouchMode == 0)
      {
        mTouchMode = 1;
        Object localObject = getChildAt(mMotionPosition - mFirstPosition);
        if ((localObject != null) && (!((View)localObject).hasExplicitFocusable()))
        {
          mLayoutMode = 0;
          if (!mDataChanged)
          {
            float[] arrayOfFloat = mTmpPoint;
            arrayOfFloat[0] = x;
            arrayOfFloat[1] = y;
            transformPointToViewLocal(arrayOfFloat, (View)localObject);
            ((View)localObject).drawableHotspotChanged(arrayOfFloat[0], arrayOfFloat[1]);
            ((View)localObject).setPressed(true);
            setPressed(true);
            layoutChildren();
            positionSelector(mMotionPosition, (View)localObject);
            refreshDrawableState();
            int i = ViewConfiguration.getLongPressTimeout();
            boolean bool = isLongClickable();
            if (mSelector != null)
            {
              localObject = mSelector.getCurrent();
              if ((localObject != null) && ((localObject instanceof TransitionDrawable))) {
                if (bool) {
                  ((TransitionDrawable)localObject).startTransition(i);
                } else {
                  ((TransitionDrawable)localObject).resetTransition();
                }
              }
              mSelector.setHotspot(x, y);
            }
            if (bool)
            {
              if (mPendingCheckForLongPress == null) {
                AbsListView.access$1102(AbsListView.this, new AbsListView.CheckForLongPress(AbsListView.this, null));
              }
              mPendingCheckForLongPress.setCoords(x, y);
              mPendingCheckForLongPress.rememberWindowAttachCount();
              postDelayed(mPendingCheckForLongPress, i);
            }
            else
            {
              mTouchMode = 2;
            }
          }
          else
          {
            mTouchMode = 2;
          }
        }
      }
    }
  }
  
  private class FlingRunnable
    implements Runnable
  {
    private static final int FLYWHEEL_TIMEOUT = 40;
    private final Runnable mCheckFlywheel = new Runnable()
    {
      public void run()
      {
        int i = mActivePointerId;
        VelocityTracker localVelocityTracker = mVelocityTracker;
        OverScroller localOverScroller = mScroller;
        if ((localVelocityTracker != null) && (i != -1))
        {
          localVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
          float f = -localVelocityTracker.getYVelocity(i);
          if ((Math.abs(f) >= mMinimumVelocity) && (localOverScroller.isScrollingInDirection(0.0F, f)))
          {
            postDelayed(this, 40L);
          }
          else
          {
            endFling();
            mTouchMode = 3;
            reportScrollStateChange(1);
          }
          return;
        }
      }
    };
    private int mLastFlingY;
    private final OverScroller mScroller = new OverScroller(getContext());
    private boolean mSuppressIdleStateChangeCall;
    
    FlingRunnable() {}
    
    void edgeReached(int paramInt)
    {
      mScroller.notifyVerticalEdgeReached(mScrollY, 0, mOverflingDistance);
      int i = getOverScrollMode();
      if ((i != 0) && ((i != 1) || (AbsListView.this.contentFits())))
      {
        mTouchMode = -1;
        if (mPositionScroller != null) {
          mPositionScroller.stop();
        }
      }
      else
      {
        mTouchMode = 6;
        i = (int)mScroller.getCurrVelocity();
        if (paramInt > 0) {
          mEdgeGlowTop.onAbsorb(i);
        } else {
          mEdgeGlowBottom.onAbsorb(i);
        }
      }
      invalidate();
      postOnAnimation(this);
    }
    
    void endFling()
    {
      mTouchMode = -1;
      removeCallbacks(this);
      removeCallbacks(mCheckFlywheel);
      if (!mSuppressIdleStateChangeCall) {
        reportScrollStateChange(0);
      }
      AbsListView.this.clearScrollingCache();
      mScroller.abortAnimation();
      if (mFlingStrictSpan != null)
      {
        mFlingStrictSpan.finish();
        AbsListView.access$2202(AbsListView.this, null);
      }
    }
    
    void flywheelTouch()
    {
      postDelayed(mCheckFlywheel, 40L);
    }
    
    public void run()
    {
      int i = mTouchMode;
      int j = 0;
      int k = 0;
      Object localObject;
      int m;
      int n;
      if (i != 6)
      {
        switch (i)
        {
        default: 
          endFling();
          return;
        case 3: 
          if (mScroller.isFinished()) {
            return;
          }
          break;
        }
        if (mDataChanged) {
          layoutChildren();
        }
        if ((mItemCount != 0) && (getChildCount() != 0))
        {
          localObject = mScroller;
          boolean bool1 = ((OverScroller)localObject).computeScrollOffset();
          m = ((OverScroller)localObject).getCurrY();
          n = mLastFlingY - m;
          if (n > 0)
          {
            mMotionPosition = mFirstPosition;
            localObject = getChildAt(0);
            mMotionViewOriginalTop = ((View)localObject).getTop();
            i = Math.min(getHeight() - mPaddingBottom - mPaddingTop - 1, n);
          }
          else
          {
            i = getChildCount() - 1;
            mMotionPosition = (mFirstPosition + i);
            localObject = getChildAt(i);
            mMotionViewOriginalTop = ((View)localObject).getTop();
            i = Math.max(-(getHeight() - mPaddingBottom - mPaddingTop - 1), n);
          }
          localObject = getChildAt(mMotionPosition - mFirstPosition);
          n = 0;
          if (localObject != null) {
            n = ((View)localObject).getTop();
          }
          boolean bool2 = trackMotionScroll(i, i);
          j = k;
          if (bool2)
          {
            j = k;
            if (i != 0) {
              j = 1;
            }
          }
          if (j != 0)
          {
            if (localObject != null)
            {
              n = -(i - (((View)localObject).getTop() - n));
              overScrollBy(0, n, 0, mScrollY, 0, 0, 0, mOverflingDistance, false);
            }
            if (bool1) {
              edgeReached(i);
            }
          }
          else if ((bool1) && (j == 0))
          {
            if (bool2) {
              invalidate();
            }
            mLastFlingY = m;
            postOnAnimation(this);
          }
          else
          {
            endFling();
          }
        }
        else
        {
          endFling();
        }
      }
      else
      {
        localObject = mScroller;
        if (((OverScroller)localObject).computeScrollOffset())
        {
          k = mScrollY;
          m = ((OverScroller)localObject).getCurrY();
          if (overScrollBy(0, m - k, 0, k, 0, 0, 0, mOverflingDistance, false))
          {
            if ((k <= 0) && (m > 0)) {
              n = 1;
            } else {
              n = 0;
            }
            i = j;
            if (k >= 0)
            {
              i = j;
              if (m < 0) {
                i = 1;
              }
            }
            if ((n == 0) && (i == 0))
            {
              startSpringback();
            }
            else
            {
              j = (int)((OverScroller)localObject).getCurrVelocity();
              n = j;
              if (i != 0) {
                n = -j;
              }
              ((OverScroller)localObject).abortAnimation();
              start(n);
            }
          }
          else
          {
            invalidate();
            postOnAnimation(this);
          }
        }
        else
        {
          endFling();
        }
      }
    }
    
    void start(int paramInt)
    {
      int i;
      if (paramInt < 0) {
        i = Integer.MAX_VALUE;
      } else {
        i = 0;
      }
      mLastFlingY = i;
      mScroller.setInterpolator(null);
      mScroller.fling(0, i, 0, paramInt, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
      mTouchMode = 4;
      mSuppressIdleStateChangeCall = false;
      postOnAnimation(this);
      if (mFlingStrictSpan == null) {
        AbsListView.access$2202(AbsListView.this, StrictMode.enterCriticalSpan("AbsListView-fling"));
      }
    }
    
    void startOverfling(int paramInt)
    {
      mScroller.setInterpolator(null);
      mScroller.fling(0, mScrollY, 0, paramInt, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, getHeight());
      mTouchMode = 6;
      mSuppressIdleStateChangeCall = false;
      invalidate();
      postOnAnimation(this);
    }
    
    void startScroll(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
      int i;
      if (paramInt1 < 0) {
        i = Integer.MAX_VALUE;
      } else {
        i = 0;
      }
      mLastFlingY = i;
      OverScroller localOverScroller = mScroller;
      Interpolator localInterpolator;
      if (paramBoolean1) {
        localInterpolator = AbsListView.sLinearInterpolator;
      } else {
        localInterpolator = null;
      }
      localOverScroller.setInterpolator(localInterpolator);
      mScroller.startScroll(0, i, 0, paramInt1, paramInt2);
      mTouchMode = 4;
      mSuppressIdleStateChangeCall = paramBoolean2;
      postOnAnimation(this);
    }
    
    void startSpringback()
    {
      mSuppressIdleStateChangeCall = false;
      if (mScroller.springBack(0, mScrollY, 0, 0, 0, 0))
      {
        mTouchMode = 6;
        invalidate();
        postOnAnimation(this);
      }
      else
      {
        mTouchMode = -1;
        reportScrollStateChange(0);
      }
    }
  }
  
  private class InputConnectionWrapper
    implements InputConnection
  {
    private final EditorInfo mOutAttrs;
    private InputConnection mTarget;
    
    public InputConnectionWrapper(EditorInfo paramEditorInfo)
    {
      mOutAttrs = paramEditorInfo;
    }
    
    private InputConnection getTarget()
    {
      if (mTarget == null) {
        mTarget = AbsListView.this.getTextFilterInput().onCreateInputConnection(mOutAttrs);
      }
      return mTarget;
    }
    
    public boolean beginBatchEdit()
    {
      return getTarget().beginBatchEdit();
    }
    
    public boolean clearMetaKeyStates(int paramInt)
    {
      return getTarget().clearMetaKeyStates(paramInt);
    }
    
    public void closeConnection()
    {
      getTarget().closeConnection();
    }
    
    public boolean commitCompletion(CompletionInfo paramCompletionInfo)
    {
      return getTarget().commitCompletion(paramCompletionInfo);
    }
    
    public boolean commitContent(InputContentInfo paramInputContentInfo, int paramInt, Bundle paramBundle)
    {
      return getTarget().commitContent(paramInputContentInfo, paramInt, paramBundle);
    }
    
    public boolean commitCorrection(CorrectionInfo paramCorrectionInfo)
    {
      return getTarget().commitCorrection(paramCorrectionInfo);
    }
    
    public boolean commitText(CharSequence paramCharSequence, int paramInt)
    {
      return getTarget().commitText(paramCharSequence, paramInt);
    }
    
    public boolean deleteSurroundingText(int paramInt1, int paramInt2)
    {
      return getTarget().deleteSurroundingText(paramInt1, paramInt2);
    }
    
    public boolean deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2)
    {
      return getTarget().deleteSurroundingTextInCodePoints(paramInt1, paramInt2);
    }
    
    public boolean endBatchEdit()
    {
      return getTarget().endBatchEdit();
    }
    
    public boolean finishComposingText()
    {
      boolean bool;
      if ((mTarget != null) && (!mTarget.finishComposingText())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public int getCursorCapsMode(int paramInt)
    {
      if (mTarget == null) {
        return 16384;
      }
      return mTarget.getCursorCapsMode(paramInt);
    }
    
    public ExtractedText getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt)
    {
      return getTarget().getExtractedText(paramExtractedTextRequest, paramInt);
    }
    
    public Handler getHandler()
    {
      return getTarget().getHandler();
    }
    
    public CharSequence getSelectedText(int paramInt)
    {
      if (mTarget == null) {
        return "";
      }
      return mTarget.getSelectedText(paramInt);
    }
    
    public CharSequence getTextAfterCursor(int paramInt1, int paramInt2)
    {
      if (mTarget == null) {
        return "";
      }
      return mTarget.getTextAfterCursor(paramInt1, paramInt2);
    }
    
    public CharSequence getTextBeforeCursor(int paramInt1, int paramInt2)
    {
      if (mTarget == null) {
        return "";
      }
      return mTarget.getTextBeforeCursor(paramInt1, paramInt2);
    }
    
    public boolean performContextMenuAction(int paramInt)
    {
      return getTarget().performContextMenuAction(paramInt);
    }
    
    public boolean performEditorAction(int paramInt)
    {
      if (paramInt == 6)
      {
        InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService(InputMethodManager.class);
        if (localInputMethodManager != null) {
          localInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return true;
      }
      return false;
    }
    
    public boolean performPrivateCommand(String paramString, Bundle paramBundle)
    {
      return getTarget().performPrivateCommand(paramString, paramBundle);
    }
    
    public boolean reportFullscreenMode(boolean paramBoolean)
    {
      return mDefInputConnection.reportFullscreenMode(paramBoolean);
    }
    
    public boolean requestCursorUpdates(int paramInt)
    {
      return getTarget().requestCursorUpdates(paramInt);
    }
    
    public boolean sendKeyEvent(KeyEvent paramKeyEvent)
    {
      return mDefInputConnection.sendKeyEvent(paramKeyEvent);
    }
    
    public boolean setComposingRegion(int paramInt1, int paramInt2)
    {
      return getTarget().setComposingRegion(paramInt1, paramInt2);
    }
    
    public boolean setComposingText(CharSequence paramCharSequence, int paramInt)
    {
      return getTarget().setComposingText(paramCharSequence, paramInt);
    }
    
    public boolean setSelection(int paramInt1, int paramInt2)
    {
      return getTarget().setSelection(paramInt1, paramInt2);
    }
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
  {
    @ViewDebug.ExportedProperty(category="list")
    boolean forceAdd;
    boolean isEnabled;
    long itemId = -1L;
    @ViewDebug.ExportedProperty(category="list")
    boolean recycledHeaderFooter;
    int scrappedFromPosition;
    @ViewDebug.ExportedProperty(category="list", mapping={@android.view.ViewDebug.IntToString(from=-1, to="ITEM_VIEW_TYPE_IGNORE"), @android.view.ViewDebug.IntToString(from=-2, to="ITEM_VIEW_TYPE_HEADER_OR_FOOTER")})
    int viewType;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
    {
      super(paramInt2);
      viewType = paramInt3;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      super.encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.addProperty("list:viewType", viewType);
      paramViewHierarchyEncoder.addProperty("list:recycledHeaderFooter", recycledHeaderFooter);
      paramViewHierarchyEncoder.addProperty("list:forceAdd", forceAdd);
      paramViewHierarchyEncoder.addProperty("list:isEnabled", isEnabled);
    }
  }
  
  class ListItemAccessibilityDelegate
    extends View.AccessibilityDelegate
  {
    ListItemAccessibilityDelegate() {}
    
    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfo);
      int i = getPositionForView(paramView);
      onInitializeAccessibilityNodeInfoForItem(paramView, i, paramAccessibilityNodeInfo);
    }
    
    public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
    {
      if (super.performAccessibilityAction(paramView, paramInt, paramBundle)) {
        return true;
      }
      int i = getPositionForView(paramView);
      if ((i != -1) && (mAdapter != null))
      {
        if (i >= mAdapter.getCount()) {
          return false;
        }
        paramBundle = paramView.getLayoutParams();
        boolean bool;
        if ((paramBundle instanceof AbsListView.LayoutParams)) {
          bool = isEnabled;
        } else {
          bool = false;
        }
        if ((isEnabled()) && (bool))
        {
          if (paramInt != 4)
          {
            if (paramInt != 8)
            {
              long l;
              if (paramInt != 16)
              {
                if (paramInt != 32) {
                  return false;
                }
                if (isLongClickable())
                {
                  l = getItemIdAtPosition(i);
                  return performLongPress(paramView, i, l);
                }
                return false;
              }
              if (AbsListView.this.isItemClickable(paramView))
              {
                l = getItemIdAtPosition(i);
                return performItemClick(paramView, i, l);
              }
              return false;
            }
            if (getSelectedItemPosition() == i)
            {
              setSelection(-1);
              return true;
            }
            return false;
          }
          if (getSelectedItemPosition() != i)
          {
            setSelection(i);
            return true;
          }
          return false;
        }
        return false;
      }
      return false;
    }
  }
  
  public static abstract interface MultiChoiceModeListener
    extends ActionMode.Callback
  {
    public abstract void onItemCheckedStateChanged(ActionMode paramActionMode, int paramInt, long paramLong, boolean paramBoolean);
  }
  
  class MultiChoiceModeWrapper
    implements AbsListView.MultiChoiceModeListener
  {
    private AbsListView.MultiChoiceModeListener mWrapped;
    
    MultiChoiceModeWrapper() {}
    
    public boolean hasWrappedCallback()
    {
      boolean bool;
      if (mWrapped != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
    {
      return mWrapped.onActionItemClicked(paramActionMode, paramMenuItem);
    }
    
    public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      if (mWrapped.onCreateActionMode(paramActionMode, paramMenu))
      {
        setLongClickable(false);
        return true;
      }
      return false;
    }
    
    public void onDestroyActionMode(ActionMode paramActionMode)
    {
      mWrapped.onDestroyActionMode(paramActionMode);
      mChoiceActionMode = null;
      clearChoices();
      mDataChanged = true;
      rememberSyncState();
      requestLayout();
      setLongClickable(true);
    }
    
    public void onItemCheckedStateChanged(ActionMode paramActionMode, int paramInt, long paramLong, boolean paramBoolean)
    {
      mWrapped.onItemCheckedStateChanged(paramActionMode, paramInt, paramLong, paramBoolean);
      if (getCheckedItemCount() == 0) {
        paramActionMode.finish();
      }
    }
    
    public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return mWrapped.onPrepareActionMode(paramActionMode, paramMenu);
    }
    
    public void setWrapped(AbsListView.MultiChoiceModeListener paramMultiChoiceModeListener)
    {
      mWrapped = paramMultiChoiceModeListener;
    }
  }
  
  public static abstract interface OnScrollListener
  {
    public static final int SCROLL_STATE_FLING = 2;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
    
    public abstract void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3);
    
    public abstract void onScrollStateChanged(AbsListView paramAbsListView, int paramInt);
  }
  
  private class PerformClick
    extends AbsListView.WindowRunnnable
    implements Runnable
  {
    int mClickMotionPosition;
    
    private PerformClick()
    {
      super(null);
    }
    
    public void run()
    {
      if (mDataChanged) {
        return;
      }
      ListAdapter localListAdapter = mAdapter;
      int i = mClickMotionPosition;
      if ((localListAdapter != null) && (mItemCount > 0) && (i != -1) && (i < localListAdapter.getCount()) && (sameWindow()) && (localListAdapter.isEnabled(i)))
      {
        View localView = getChildAt(i - mFirstPosition);
        if (localView != null) {
          performItemClick(localView, i, localListAdapter.getItemId(i));
        }
      }
    }
  }
  
  class PositionScroller
    extends AbsListView.AbsPositionScroller
    implements Runnable
  {
    private static final int MOVE_DOWN_BOUND = 3;
    private static final int MOVE_DOWN_POS = 1;
    private static final int MOVE_OFFSET = 5;
    private static final int MOVE_UP_BOUND = 4;
    private static final int MOVE_UP_POS = 2;
    private static final int SCROLL_DURATION = 200;
    private int mBoundPos;
    private final int mExtraScroll = ViewConfiguration.get(mContext).getScaledFadingEdgeLength();
    private int mLastSeenPos;
    private int mMode;
    private int mOffsetFromTop;
    private int mScrollDuration;
    private int mTargetPos;
    
    PositionScroller() {}
    
    private void scrollToVisible(int paramInt1, int paramInt2, int paramInt3)
    {
      int i = paramInt2;
      int j = mFirstPosition;
      int k = j + getChildCount() - 1;
      int m = mListPadding.top;
      int n = getHeight() - mListPadding.bottom;
      if ((paramInt1 < j) || (paramInt1 > k))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("scrollToVisible called with targetPos ");
        ((StringBuilder)localObject).append(paramInt1);
        ((StringBuilder)localObject).append(" not visible [");
        ((StringBuilder)localObject).append(j);
        ((StringBuilder)localObject).append(", ");
        ((StringBuilder)localObject).append(k);
        ((StringBuilder)localObject).append("]");
        Log.w("AbsListView", ((StringBuilder)localObject).toString());
      }
      if (i >= j)
      {
        paramInt2 = i;
        if (i <= k) {}
      }
      else
      {
        paramInt2 = -1;
      }
      Object localObject = getChildAt(paramInt1 - j);
      i = ((View)localObject).getTop();
      k = ((View)localObject).getBottom();
      paramInt1 = 0;
      if (k > n) {
        paramInt1 = k - n;
      }
      if (i < m) {
        paramInt1 = i - m;
      }
      if (paramInt1 == 0) {
        return;
      }
      if (paramInt2 >= 0)
      {
        localObject = getChildAt(paramInt2 - j);
        i = ((View)localObject).getTop();
        paramInt2 = ((View)localObject).getBottom();
        j = Math.abs(paramInt1);
        if ((paramInt1 < 0) && (paramInt2 + j > n)) {
          paramInt1 = Math.max(0, paramInt2 - n);
        }
      }
      for (;;)
      {
        paramInt2 = paramInt1;
        break;
        paramInt2 = paramInt1;
        if (paramInt1 <= 0) {
          break;
        }
        paramInt2 = paramInt1;
        if (i - j >= m) {
          break;
        }
        paramInt2 = Math.min(0, i - m);
        break;
      }
      smoothScrollBy(paramInt2, paramInt3);
    }
    
    public void run()
    {
      int i = getHeight();
      int j = mFirstPosition;
      int k = mMode;
      boolean bool = false;
      int m;
      int n;
      Object localObject;
      int i1;
      switch (k)
      {
      default: 
        break;
      case 5: 
        if (mLastSeenPos == j)
        {
          postOnAnimation(this);
          return;
        }
        mLastSeenPos = j;
        m = getChildCount();
        k = mTargetPos;
        n = j + m - 1;
        localObject = getChildAt(0);
        i1 = ((View)localObject).getHeight();
        View localView = getChildAt(m - 1);
        i = localView.getHeight();
        if (i1 == 0.0F) {
          f1 = 1.0F;
        } else {
          f1 = (((View)localObject).getTop() + i1) / i1;
        }
        float f2;
        if (i == 0.0F) {
          f2 = 1.0F;
        } else {
          f2 = (getHeight() + i - localView.getBottom()) / i;
        }
        float f3 = 0.0F;
        if (k < j)
        {
          f1 = j - k + (1.0F - f1) + 1.0F;
        }
        else
        {
          f1 = f3;
          if (k > n) {
            f1 = k - n + (1.0F - f2);
          }
        }
        float f1 = Math.min(Math.abs(f1 / m), 1.0F);
        if (k < j)
        {
          k = (int)(-getHeight() * f1);
          i = (int)(mScrollDuration * f1);
          smoothScrollBy(k, i, true, true);
          postOnAnimation(this);
        }
        else if (k > n)
        {
          i = (int)(getHeight() * f1);
          k = (int)(mScrollDuration * f1);
          smoothScrollBy(i, k, true, true);
          postOnAnimation(this);
        }
        else
        {
          i = getChildAt(k - j).getTop() - mOffsetFromTop;
          k = (int)(mScrollDuration * (Math.abs(i) / getHeight()));
          smoothScrollBy(i, k, true, false);
        }
        break;
      case 4: 
        k = getChildCount() - 2;
        if (k < 0) {
          return;
        }
        j += k;
        if (j == mLastSeenPos)
        {
          postOnAnimation(this);
          return;
        }
        localObject = getChildAt(k);
        i1 = ((View)localObject).getHeight();
        k = ((View)localObject).getTop();
        m = Math.max(mListPadding.top, mExtraScroll);
        mLastSeenPos = j;
        if (j > mBoundPos)
        {
          smoothScrollBy(-(i - k - m), mScrollDuration, true, true);
          postOnAnimation(this);
        }
        else
        {
          i -= m;
          k += i1;
          if (i > k) {
            smoothScrollBy(-(i - k), mScrollDuration, true, false);
          } else {
            reportScrollStateChange(0);
          }
        }
        break;
      case 3: 
        k = getChildCount();
        if ((j != mBoundPos) && (k > 1) && (j + k < mItemCount))
        {
          k = j + 1;
          if (k == mLastSeenPos)
          {
            postOnAnimation(this);
            return;
          }
          localObject = getChildAt(1);
          i = ((View)localObject).getHeight();
          i1 = ((View)localObject).getTop();
          j = Math.max(mListPadding.bottom, mExtraScroll);
          if (k < mBoundPos)
          {
            smoothScrollBy(Math.max(0, i + i1 - j), mScrollDuration, true, true);
            mLastSeenPos = k;
            postOnAnimation(this);
          }
          else if (i1 > j)
          {
            smoothScrollBy(i1 - j, mScrollDuration, true, false);
          }
          else
          {
            reportScrollStateChange(0);
          }
        }
        else
        {
          reportScrollStateChange(0);
          return;
        }
        break;
      case 2: 
        if (j == mLastSeenPos)
        {
          postOnAnimation(this);
          return;
        }
        localObject = AbsListView.this;
        bool = false;
        localObject = ((AbsListView)localObject).getChildAt(0);
        if (localObject == null) {
          return;
        }
        i = ((View)localObject).getTop();
        if (j > 0) {
          k = Math.max(mExtraScroll, mListPadding.top);
        } else {
          k = mListPadding.top;
        }
        localObject = AbsListView.this;
        i1 = mScrollDuration;
        if (j > mTargetPos) {
          bool = true;
        }
        ((AbsListView)localObject).smoothScrollBy(i - k, i1, true, bool);
        mLastSeenPos = j;
        if (j > mTargetPos) {
          postOnAnimation(this);
        }
        break;
      case 1: 
        k = getChildCount() - 1;
        j += k;
        if (k < 0) {
          return;
        }
        if (j == mLastSeenPos)
        {
          postOnAnimation(this);
          return;
        }
        localObject = getChildAt(k);
        i1 = ((View)localObject).getHeight();
        m = ((View)localObject).getTop();
        if (j < mItemCount - 1) {
          k = Math.max(mListPadding.bottom, mExtraScroll);
        } else {
          k = mListPadding.bottom;
        }
        localObject = AbsListView.this;
        n = mScrollDuration;
        if (j < mTargetPos) {
          bool = true;
        }
        ((AbsListView)localObject).smoothScrollBy(i1 - (i - m) + k, n, true, bool);
        mLastSeenPos = j;
        if (j < mTargetPos) {
          postOnAnimation(this);
        }
        break;
      }
    }
    
    public void start(final int paramInt)
    {
      stop();
      if (mDataChanged)
      {
        mPositionScrollAfterLayout = new Runnable()
        {
          public void run()
          {
            start(paramInt);
          }
        };
        return;
      }
      int i = getChildCount();
      if (i == 0) {
        return;
      }
      int j = mFirstPosition;
      int k = j + i - 1;
      i = Math.max(0, Math.min(getCount() - 1, paramInt));
      if (i < j)
      {
        paramInt = j - i + 1;
        mMode = 2;
      }
      else
      {
        if (i <= k) {
          break label161;
        }
        paramInt = i - k + 1;
        mMode = 1;
      }
      if (paramInt > 0) {
        mScrollDuration = (200 / paramInt);
      } else {
        mScrollDuration = 200;
      }
      mTargetPos = i;
      mBoundPos = -1;
      mLastSeenPos = -1;
      postOnAnimation(this);
      return;
      label161:
      scrollToVisible(i, -1, 200);
    }
    
    public void start(final int paramInt1, final int paramInt2)
    {
      stop();
      if (paramInt2 == -1)
      {
        start(paramInt1);
        return;
      }
      if (mDataChanged)
      {
        mPositionScrollAfterLayout = new Runnable()
        {
          public void run()
          {
            start(paramInt1, paramInt2);
          }
        };
        return;
      }
      int i = getChildCount();
      if (i == 0) {
        return;
      }
      int j = mFirstPosition;
      int k = j + i - 1;
      i = Math.max(0, Math.min(getCount() - 1, paramInt1));
      if (i < j)
      {
        k -= paramInt2;
        if (k < 1) {
          return;
        }
        paramInt1 = j - i + 1;
        j = k - 1;
        if (j < paramInt1)
        {
          paramInt1 = j;
          mMode = 4;
        }
        else
        {
          mMode = 2;
        }
      }
      else
      {
        if (i <= k) {
          break label246;
        }
        j = paramInt2 - j;
        if (j < 1) {
          return;
        }
        paramInt1 = i - k + 1;
        j--;
        if (j < paramInt1)
        {
          mMode = 3;
          paramInt1 = j;
        }
        else
        {
          mMode = 1;
        }
      }
      if (paramInt1 > 0) {
        mScrollDuration = (200 / paramInt1);
      } else {
        mScrollDuration = 200;
      }
      mTargetPos = i;
      mBoundPos = paramInt2;
      mLastSeenPos = -1;
      postOnAnimation(this);
      return;
      label246:
      scrollToVisible(i, paramInt2, 200);
    }
    
    public void startWithOffset(int paramInt1, int paramInt2)
    {
      startWithOffset(paramInt1, paramInt2, 200);
    }
    
    public void startWithOffset(final int paramInt1, final int paramInt2, final int paramInt3)
    {
      stop();
      if (mDataChanged)
      {
        mPositionScrollAfterLayout = new Runnable()
        {
          public void run()
          {
            startWithOffset(paramInt1, paramInt2, paramInt3);
          }
        };
        return;
      }
      int i = getChildCount();
      if (i == 0) {
        return;
      }
      paramInt2 += getPaddingTop();
      mTargetPos = Math.max(0, Math.min(getCount() - 1, paramInt1));
      mOffsetFromTop = paramInt2;
      mBoundPos = -1;
      mLastSeenPos = -1;
      mMode = 5;
      int j = mFirstPosition;
      paramInt1 = j + i - 1;
      if (mTargetPos < j) {}
      for (paramInt1 = j - mTargetPos;; paramInt1 = mTargetPos - paramInt1)
      {
        break;
        if (mTargetPos <= paramInt1) {
          break label198;
        }
      }
      float f = paramInt1 / i;
      if (f >= 1.0F) {
        paramInt3 = (int)(paramInt3 / f);
      }
      mScrollDuration = paramInt3;
      mLastSeenPos = -1;
      postOnAnimation(this);
      return;
      label198:
      paramInt1 = getChildAt(mTargetPos - j).getTop();
      smoothScrollBy(paramInt1 - paramInt2, paramInt3, true, false);
    }
    
    public void stop()
    {
      removeCallbacks(this);
    }
  }
  
  class RecycleBin
  {
    private View[] mActiveViews = new View[0];
    private ArrayList<View> mCurrentScrap;
    private int mFirstActivePosition;
    private AbsListView.RecyclerListener mRecyclerListener;
    private ArrayList<View>[] mScrapViews;
    private ArrayList<View> mSkippedScrap;
    private SparseArray<View> mTransientStateViews;
    private LongSparseArray<View> mTransientStateViewsById;
    private int mViewTypeCount;
    
    RecycleBin() {}
    
    private void clearScrap(ArrayList<View> paramArrayList)
    {
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++) {
        removeDetachedView((View)paramArrayList.remove(i - 1 - j), false);
      }
    }
    
    private void clearScrapForRebind(View paramView)
    {
      paramView.clearAccessibilityFocus();
      paramView.setAccessibilityDelegate(null);
    }
    
    private ArrayList<View> getSkippedScrap()
    {
      if (mSkippedScrap == null) {
        mSkippedScrap = new ArrayList();
      }
      return mSkippedScrap;
    }
    
    private void pruneScrapViews()
    {
      int i = mActiveViews.length;
      int j = mViewTypeCount;
      Object localObject1 = mScrapViews;
      int m;
      for (int k = 0; k < j; k++)
      {
        localObject2 = localObject1[k];
        m = ((ArrayList)localObject2).size();
        while (m > i)
        {
          m--;
          ((ArrayList)localObject2).remove(m);
        }
      }
      Object localObject2 = mTransientStateViews;
      if (localObject2 != null) {
        for (k = 0; k < ((SparseArray)localObject2).size(); k = m + 1)
        {
          localObject1 = (View)((SparseArray)localObject2).valueAt(k);
          m = k;
          if (!((View)localObject1).hasTransientState())
          {
            removeDetachedView((View)localObject1, false);
            ((SparseArray)localObject2).removeAt(k);
            m = k - 1;
          }
        }
      }
      localObject2 = mTransientStateViewsById;
      if (localObject2 != null) {
        for (k = 0; k < ((LongSparseArray)localObject2).size(); k = m + 1)
        {
          localObject1 = (View)((LongSparseArray)localObject2).valueAt(k);
          m = k;
          if (!((View)localObject1).hasTransientState())
          {
            removeDetachedView((View)localObject1, false);
            ((LongSparseArray)localObject2).removeAt(k);
            m = k - 1;
          }
        }
      }
    }
    
    private void removeDetachedView(View paramView, boolean paramBoolean)
    {
      paramView.setAccessibilityDelegate(null);
      AbsListView.this.removeDetachedView(paramView, paramBoolean);
    }
    
    private View retrieveFromScrap(ArrayList<View> paramArrayList, int paramInt)
    {
      int i = paramArrayList.size();
      if (i > 0)
      {
        for (int j = i - 1; j >= 0; j--)
        {
          Object localObject = (View)paramArrayList.get(j);
          localObject = (AbsListView.LayoutParams)((View)localObject).getLayoutParams();
          if (mAdapterHasStableIds)
          {
            if (mAdapter.getItemId(paramInt) == itemId) {
              return (View)paramArrayList.remove(j);
            }
          }
          else if (scrappedFromPosition == paramInt)
          {
            paramArrayList = (View)paramArrayList.remove(j);
            clearScrapForRebind(paramArrayList);
            return paramArrayList;
          }
        }
        paramArrayList = (View)paramArrayList.remove(i - 1);
        clearScrapForRebind(paramArrayList);
        return paramArrayList;
      }
      return null;
    }
    
    void addScrapView(View paramView, int paramInt)
    {
      AbsListView.LayoutParams localLayoutParams = (AbsListView.LayoutParams)paramView.getLayoutParams();
      if (localLayoutParams == null) {
        return;
      }
      scrappedFromPosition = paramInt;
      int i = viewType;
      if (!shouldRecycleViewType(i))
      {
        if (i != -2) {
          getSkippedScrap().add(paramView);
        }
        return;
      }
      paramView.dispatchStartTemporaryDetach();
      notifyViewAccessibilityStateChangedIfNeeded(1);
      if (paramView.hasTransientState())
      {
        if ((mAdapter != null) && (mAdapterHasStableIds))
        {
          if (mTransientStateViewsById == null) {
            mTransientStateViewsById = new LongSparseArray();
          }
          mTransientStateViewsById.put(itemId, paramView);
        }
        else if (!mDataChanged)
        {
          if (mTransientStateViews == null) {
            mTransientStateViews = new SparseArray();
          }
          mTransientStateViews.put(paramInt, paramView);
        }
        else
        {
          clearScrapForRebind(paramView);
          getSkippedScrap().add(paramView);
        }
      }
      else
      {
        clearScrapForRebind(paramView);
        if (mViewTypeCount == 1) {
          mCurrentScrap.add(paramView);
        } else {
          mScrapViews[i].add(paramView);
        }
        if (mRecyclerListener != null) {
          mRecyclerListener.onMovedToScrapHeap(paramView);
        }
      }
    }
    
    void clear()
    {
      if (mViewTypeCount == 1)
      {
        clearScrap(mCurrentScrap);
      }
      else
      {
        int i = mViewTypeCount;
        for (int j = 0; j < i; j++) {
          clearScrap(mScrapViews[j]);
        }
      }
      clearTransientStateViews();
    }
    
    void clearTransientStateViews()
    {
      Object localObject = mTransientStateViews;
      int i;
      int j;
      if (localObject != null)
      {
        i = ((SparseArray)localObject).size();
        for (j = 0; j < i; j++) {
          removeDetachedView((View)((SparseArray)localObject).valueAt(j), false);
        }
        ((SparseArray)localObject).clear();
      }
      localObject = mTransientStateViewsById;
      if (localObject != null)
      {
        i = ((LongSparseArray)localObject).size();
        for (j = 0; j < i; j++) {
          removeDetachedView((View)((LongSparseArray)localObject).valueAt(j), false);
        }
        ((LongSparseArray)localObject).clear();
      }
    }
    
    void fillActiveViews(int paramInt1, int paramInt2)
    {
      if (mActiveViews.length < paramInt1) {
        mActiveViews = new View[paramInt1];
      }
      mFirstActivePosition = paramInt2;
      View[] arrayOfView = mActiveViews;
      for (int i = 0; i < paramInt1; i++)
      {
        View localView = getChildAt(i);
        AbsListView.LayoutParams localLayoutParams = (AbsListView.LayoutParams)localView.getLayoutParams();
        if ((localLayoutParams != null) && (viewType != -2))
        {
          arrayOfView[i] = localView;
          scrappedFromPosition = (paramInt2 + i);
        }
      }
    }
    
    void fullyDetachScrapViews()
    {
      int i = mViewTypeCount;
      ArrayList[] arrayOfArrayList = mScrapViews;
      for (int j = 0; j < i; j++)
      {
        ArrayList localArrayList = arrayOfArrayList[j];
        for (int k = localArrayList.size() - 1; k >= 0; k--)
        {
          View localView = (View)localArrayList.get(k);
          if (localView.isTemporarilyDetached()) {
            removeDetachedView(localView, false);
          }
        }
      }
    }
    
    View getActiveView(int paramInt)
    {
      paramInt -= mFirstActivePosition;
      View[] arrayOfView = mActiveViews;
      if ((paramInt >= 0) && (paramInt < arrayOfView.length))
      {
        View localView = arrayOfView[paramInt];
        arrayOfView[paramInt] = null;
        return localView;
      }
      return null;
    }
    
    View getScrapView(int paramInt)
    {
      int i = mAdapter.getItemViewType(paramInt);
      if (i < 0) {
        return null;
      }
      if (mViewTypeCount == 1) {
        return retrieveFromScrap(mCurrentScrap, paramInt);
      }
      if (i < mScrapViews.length) {
        return retrieveFromScrap(mScrapViews[i], paramInt);
      }
      return null;
    }
    
    View getTransientStateView(int paramInt)
    {
      View localView;
      if ((mAdapter != null) && (mAdapterHasStableIds) && (mTransientStateViewsById != null))
      {
        long l = mAdapter.getItemId(paramInt);
        localView = (View)mTransientStateViewsById.get(l);
        mTransientStateViewsById.remove(l);
        return localView;
      }
      if (mTransientStateViews != null)
      {
        paramInt = mTransientStateViews.indexOfKey(paramInt);
        if (paramInt >= 0)
        {
          localView = (View)mTransientStateViews.valueAt(paramInt);
          mTransientStateViews.removeAt(paramInt);
          return localView;
        }
      }
      return null;
    }
    
    public void markChildrenDirty()
    {
      int i = mViewTypeCount;
      int j = 0;
      ArrayList localArrayList;
      int k;
      if (i == 1)
      {
        localArrayList = mCurrentScrap;
        k = localArrayList.size();
        for (i = 0; i < k; i++) {
          ((View)localArrayList.get(i)).forceLayout();
        }
      }
      else
      {
        int m = mViewTypeCount;
        for (i = 0; i < m; i++)
        {
          localArrayList = mScrapViews[i];
          int n = localArrayList.size();
          for (k = 0; k < n; k++) {
            ((View)localArrayList.get(k)).forceLayout();
          }
        }
      }
      if (mTransientStateViews != null)
      {
        k = mTransientStateViews.size();
        for (i = 0; i < k; i++) {
          ((View)mTransientStateViews.valueAt(i)).forceLayout();
        }
      }
      if (mTransientStateViewsById != null)
      {
        k = mTransientStateViewsById.size();
        for (i = j; i < k; i++) {
          ((View)mTransientStateViewsById.valueAt(i)).forceLayout();
        }
      }
    }
    
    void reclaimScrapViews(List<View> paramList)
    {
      if (mViewTypeCount == 1)
      {
        paramList.addAll(mCurrentScrap);
      }
      else
      {
        int i = mViewTypeCount;
        ArrayList[] arrayOfArrayList = mScrapViews;
        for (int j = 0; j < i; j++) {
          paramList.addAll(arrayOfArrayList[j]);
        }
      }
    }
    
    void removeSkippedScrap()
    {
      if (mSkippedScrap == null) {
        return;
      }
      int i = mSkippedScrap.size();
      for (int j = 0; j < i; j++) {
        removeDetachedView((View)mSkippedScrap.get(j), false);
      }
      mSkippedScrap.clear();
    }
    
    void scrapActiveViews()
    {
      View[] arrayOfView = mActiveViews;
      Object localObject1 = mRecyclerListener;
      int i = 1;
      int j;
      if (localObject1 != null) {
        j = 1;
      } else {
        j = 0;
      }
      if (mViewTypeCount <= 1) {
        i = 0;
      }
      localObject1 = mCurrentScrap;
      int k = arrayOfView.length - 1;
      while (k >= 0)
      {
        View localView = arrayOfView[k];
        Object localObject2 = localObject1;
        if (localView != null)
        {
          localObject2 = (AbsListView.LayoutParams)localView.getLayoutParams();
          int m = viewType;
          arrayOfView[k] = null;
          if (localView.hasTransientState())
          {
            localView.dispatchStartTemporaryDetach();
            if ((mAdapter != null) && (mAdapterHasStableIds))
            {
              if (mTransientStateViewsById == null) {
                mTransientStateViewsById = new LongSparseArray();
              }
              long l = mAdapter.getItemId(mFirstActivePosition + k);
              mTransientStateViewsById.put(l, localView);
              localObject2 = localObject1;
            }
            else if (!mDataChanged)
            {
              if (mTransientStateViews == null) {
                mTransientStateViews = new SparseArray();
              }
              mTransientStateViews.put(mFirstActivePosition + k, localView);
              localObject2 = localObject1;
            }
            else
            {
              localObject2 = localObject1;
              if (m != -2)
              {
                removeDetachedView(localView, false);
                localObject2 = localObject1;
              }
            }
          }
          else if (!shouldRecycleViewType(m))
          {
            localObject2 = localObject1;
            if (m != -2)
            {
              removeDetachedView(localView, false);
              localObject2 = localObject1;
            }
          }
          else
          {
            if (i != 0) {
              localObject1 = mScrapViews[m];
            }
            scrappedFromPosition = (mFirstActivePosition + k);
            removeDetachedView(localView, false);
            ((ArrayList)localObject1).add(localView);
            localObject2 = localObject1;
            if (j != 0)
            {
              mRecyclerListener.onMovedToScrapHeap(localView);
              localObject2 = localObject1;
            }
          }
        }
        k--;
        localObject1 = localObject2;
      }
      pruneScrapViews();
    }
    
    void setCacheColorHint(int paramInt)
    {
      int i = mViewTypeCount;
      int j = 0;
      ArrayList localArrayList;
      if (i == 1)
      {
        localArrayList = mCurrentScrap;
        k = localArrayList.size();
        for (i = 0; i < k; i++) {
          ((View)localArrayList.get(i)).setDrawingCacheBackgroundColor(paramInt);
        }
      }
      else
      {
        int m = mViewTypeCount;
        for (i = 0; i < m; i++)
        {
          localArrayList = mScrapViews[i];
          int n = localArrayList.size();
          for (k = 0; k < n; k++) {
            ((View)localArrayList.get(k)).setDrawingCacheBackgroundColor(paramInt);
          }
        }
      }
      View[] arrayOfView = mActiveViews;
      int k = arrayOfView.length;
      for (i = j; i < k; i++)
      {
        localArrayList = arrayOfView[i];
        if (localArrayList != null) {
          localArrayList.setDrawingCacheBackgroundColor(paramInt);
        }
      }
    }
    
    public void setViewTypeCount(int paramInt)
    {
      if (paramInt >= 1)
      {
        ArrayList[] arrayOfArrayList = new ArrayList[paramInt];
        for (int i = 0; i < paramInt; i++) {
          arrayOfArrayList[i] = new ArrayList();
        }
        mViewTypeCount = paramInt;
        mCurrentScrap = arrayOfArrayList[0];
        mScrapViews = arrayOfArrayList;
        return;
      }
      throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
    }
    
    public boolean shouldRecycleViewType(int paramInt)
    {
      boolean bool;
      if (paramInt >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public static abstract interface RecyclerListener
  {
    public abstract void onMovedToScrapHeap(View paramView);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public AbsListView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AbsListView.SavedState(paramAnonymousParcel, null);
      }
      
      public AbsListView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new AbsListView.SavedState[paramAnonymousInt];
      }
    };
    LongSparseArray<Integer> checkIdState;
    SparseBooleanArray checkState;
    int checkedItemCount;
    String filter;
    long firstId;
    int height;
    boolean inActionMode;
    int position;
    long selectedId;
    int viewTop;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      selectedId = paramParcel.readLong();
      firstId = paramParcel.readLong();
      viewTop = paramParcel.readInt();
      position = paramParcel.readInt();
      height = paramParcel.readInt();
      filter = paramParcel.readString();
      int i = paramParcel.readByte();
      int j = 0;
      boolean bool;
      if (i != 0) {
        bool = true;
      } else {
        bool = false;
      }
      inActionMode = bool;
      checkedItemCount = paramParcel.readInt();
      checkState = paramParcel.readSparseBooleanArray();
      i = paramParcel.readInt();
      if (i > 0)
      {
        checkIdState = new LongSparseArray();
        while (j < i)
        {
          long l = paramParcel.readLong();
          int k = paramParcel.readInt();
          checkIdState.put(l, Integer.valueOf(k));
          j++;
        }
      }
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AbsListView.SavedState{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" selectedId=");
      localStringBuilder.append(selectedId);
      localStringBuilder.append(" firstId=");
      localStringBuilder.append(firstId);
      localStringBuilder.append(" viewTop=");
      localStringBuilder.append(viewTop);
      localStringBuilder.append(" position=");
      localStringBuilder.append(position);
      localStringBuilder.append(" height=");
      localStringBuilder.append(height);
      localStringBuilder.append(" filter=");
      localStringBuilder.append(filter);
      localStringBuilder.append(" checkState=");
      localStringBuilder.append(checkState);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeLong(selectedId);
      paramParcel.writeLong(firstId);
      paramParcel.writeInt(viewTop);
      paramParcel.writeInt(position);
      paramParcel.writeInt(height);
      paramParcel.writeString(filter);
      paramParcel.writeByte((byte)inActionMode);
      paramParcel.writeInt(checkedItemCount);
      paramParcel.writeSparseBooleanArray(checkState);
      LongSparseArray localLongSparseArray = checkIdState;
      int i = 0;
      if (localLongSparseArray != null) {
        paramInt = checkIdState.size();
      } else {
        paramInt = 0;
      }
      paramParcel.writeInt(paramInt);
      while (i < paramInt)
      {
        paramParcel.writeLong(checkIdState.keyAt(i));
        paramParcel.writeInt(((Integer)checkIdState.valueAt(i)).intValue());
        i++;
      }
    }
  }
  
  public static abstract interface SelectionBoundsAdjuster
  {
    public abstract void adjustListItemSelectionBounds(Rect paramRect);
  }
  
  private class WindowRunnnable
  {
    private int mOriginalAttachCount;
    
    private WindowRunnnable() {}
    
    public void rememberWindowAttachCount()
    {
      mOriginalAttachCount = getWindowAttachCount();
    }
    
    public boolean sameWindow()
    {
      boolean bool;
      if (getWindowAttachCount() == mOriginalAttachCount) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
}
