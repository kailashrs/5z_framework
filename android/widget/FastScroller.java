package android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.IntProperty;
import android.util.MathUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroupOverlay;
import com.android.internal.R.styleable;

class FastScroller
{
  private static Property<View, Integer> BOTTOM = new IntProperty("bottom")
  {
    public Integer get(View paramAnonymousView)
    {
      return Integer.valueOf(paramAnonymousView.getBottom());
    }
    
    public void setValue(View paramAnonymousView, int paramAnonymousInt)
    {
      paramAnonymousView.setBottom(paramAnonymousInt);
    }
  };
  private static final int DURATION_CROSS_FADE = 50;
  private static final int DURATION_FADE_IN = 150;
  private static final int DURATION_FADE_OUT = 300;
  private static final int DURATION_RESIZE = 100;
  private static final long FADE_TIMEOUT = 1500L;
  private static Property<View, Integer> LEFT;
  private static final int MIN_PAGES = 4;
  private static final int OVERLAY_ABOVE_THUMB = 2;
  private static final int OVERLAY_AT_THUMB = 1;
  private static final int OVERLAY_FLOATING = 0;
  private static final int PREVIEW_LEFT = 0;
  private static final int PREVIEW_RIGHT = 1;
  private static Property<View, Integer> RIGHT;
  private static final int STATE_DRAGGING = 2;
  private static final int STATE_NONE = 0;
  private static final int STATE_VISIBLE = 1;
  private static final long TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
  private static final int THUMB_POSITION_INSIDE = 1;
  private static final int THUMB_POSITION_MIDPOINT = 0;
  private static Property<View, Integer> TOP;
  private boolean mAlwaysShow;
  private final Rect mContainerRect = new Rect();
  private int mCurrentSection = -1;
  private AnimatorSet mDecorAnimation;
  private final Runnable mDeferHide = new Runnable()
  {
    public void run()
    {
      FastScroller.this.setState(0);
    }
  };
  private boolean mEnabled;
  private int mFirstVisibleItem;
  private int mHeaderCount;
  private float mInitialTouchY;
  private boolean mLayoutFromRight;
  private final AbsListView mList;
  private Adapter mListAdapter;
  private boolean mLongList;
  private boolean mMatchDragPosition;
  private final int mMinimumTouchTarget;
  private int mOldChildCount;
  private int mOldItemCount;
  private final ViewGroupOverlay mOverlay;
  private int mOverlayPosition;
  private long mPendingDrag = -1L;
  private AnimatorSet mPreviewAnimation;
  private final View mPreviewImage;
  private int mPreviewMinHeight;
  private int mPreviewMinWidth;
  private int mPreviewPadding;
  private final int[] mPreviewResId = new int[2];
  private final TextView mPrimaryText;
  private int mScaledTouchSlop;
  private int mScrollBarStyle;
  private boolean mScrollCompleted;
  private int mScrollbarPosition = -1;
  private final TextView mSecondaryText;
  private SectionIndexer mSectionIndexer;
  private Object[] mSections;
  private boolean mShowingPreview;
  private boolean mShowingPrimary;
  private int mState;
  private final Animator.AnimatorListener mSwitchPrimaryListener = new AnimatorListenerAdapter()
  {
    public void onAnimationEnd(Animator paramAnonymousAnimator)
    {
      FastScroller.access$102(FastScroller.this, mShowingPrimary ^ true);
    }
  };
  private final Rect mTempBounds = new Rect();
  private final Rect mTempMargins = new Rect();
  private int mTextAppearance;
  private ColorStateList mTextColor;
  private float mTextSize;
  private Drawable mThumbDrawable;
  private final ImageView mThumbImage;
  private int mThumbMinHeight;
  private int mThumbMinWidth;
  private float mThumbOffset;
  private int mThumbPosition;
  private float mThumbRange;
  private Drawable mTrackDrawable;
  private final ImageView mTrackImage;
  private boolean mUpdatingLayout;
  private int mWidth;
  
  static
  {
    LEFT = new IntProperty("left")
    {
      public Integer get(View paramAnonymousView)
      {
        return Integer.valueOf(paramAnonymousView.getLeft());
      }
      
      public void setValue(View paramAnonymousView, int paramAnonymousInt)
      {
        paramAnonymousView.setLeft(paramAnonymousInt);
      }
    };
    TOP = new IntProperty("top")
    {
      public Integer get(View paramAnonymousView)
      {
        return Integer.valueOf(paramAnonymousView.getTop());
      }
      
      public void setValue(View paramAnonymousView, int paramAnonymousInt)
      {
        paramAnonymousView.setTop(paramAnonymousInt);
      }
    };
    RIGHT = new IntProperty("right")
    {
      public Integer get(View paramAnonymousView)
      {
        return Integer.valueOf(paramAnonymousView.getRight());
      }
      
      public void setValue(View paramAnonymousView, int paramAnonymousInt)
      {
        paramAnonymousView.setRight(paramAnonymousInt);
      }
    };
  }
  
  public FastScroller(AbsListView paramAbsListView, int paramInt)
  {
    mList = paramAbsListView;
    mOldItemCount = paramAbsListView.getCount();
    mOldChildCount = paramAbsListView.getChildCount();
    Object localObject = paramAbsListView.getContext();
    mScaledTouchSlop = ViewConfiguration.get((Context)localObject).getScaledTouchSlop();
    mScrollBarStyle = paramAbsListView.getScrollBarStyle();
    boolean bool = true;
    mScrollCompleted = true;
    mState = 1;
    if (getApplicationInfotargetSdkVersion < 11) {
      bool = false;
    }
    mMatchDragPosition = bool;
    mTrackImage = new ImageView((Context)localObject);
    mTrackImage.setScaleType(ImageView.ScaleType.FIT_XY);
    mThumbImage = new ImageView((Context)localObject);
    mThumbImage.setScaleType(ImageView.ScaleType.FIT_XY);
    mPreviewImage = new View((Context)localObject);
    mPreviewImage.setAlpha(0.0F);
    mPrimaryText = createPreviewTextView((Context)localObject);
    mSecondaryText = createPreviewTextView((Context)localObject);
    mMinimumTouchTarget = paramAbsListView.getResources().getDimensionPixelSize(17105160);
    setStyle(paramInt);
    localObject = paramAbsListView.getOverlay();
    mOverlay = ((ViewGroupOverlay)localObject);
    ((ViewGroupOverlay)localObject).add(mTrackImage);
    ((ViewGroupOverlay)localObject).add(mThumbImage);
    ((ViewGroupOverlay)localObject).add(mPreviewImage);
    ((ViewGroupOverlay)localObject).add(mPrimaryText);
    ((ViewGroupOverlay)localObject).add(mSecondaryText);
    getSectionsFromIndexer();
    updateLongList(mOldChildCount, mOldItemCount);
    setScrollbarPosition(paramAbsListView.getVerticalScrollbarPosition());
    postAutoHide();
  }
  
  private static Animator animateAlpha(View paramView, float paramFloat)
  {
    return ObjectAnimator.ofFloat(paramView, View.ALPHA, new float[] { paramFloat });
  }
  
  private static Animator animateBounds(View paramView, Rect paramRect)
  {
    return ObjectAnimator.ofPropertyValuesHolder(paramView, new PropertyValuesHolder[] { PropertyValuesHolder.ofInt(LEFT, new int[] { left }), PropertyValuesHolder.ofInt(TOP, new int[] { top }), PropertyValuesHolder.ofInt(RIGHT, new int[] { right }), PropertyValuesHolder.ofInt(BOTTOM, new int[] { bottom }) });
  }
  
  private static Animator animateScaleX(View paramView, float paramFloat)
  {
    return ObjectAnimator.ofFloat(paramView, View.SCALE_X, new float[] { paramFloat });
  }
  
  private void applyLayout(View paramView, Rect paramRect)
  {
    paramView.layout(left, top, right, bottom);
    float f;
    if (mLayoutFromRight) {
      f = right - left;
    } else {
      f = 0.0F;
    }
    paramView.setPivotX(f);
  }
  
  private void beginDrag()
  {
    mPendingDrag = -1L;
    setState(2);
    if ((mListAdapter == null) && (mList != null)) {
      getSectionsFromIndexer();
    }
    if (mList != null)
    {
      mList.requestDisallowInterceptTouchEvent(true);
      mList.reportScrollStateChange(1);
    }
    cancelFling();
  }
  
  private void cancelFling()
  {
    MotionEvent localMotionEvent = MotionEvent.obtain(0L, 0L, 3, 0.0F, 0.0F, 0);
    mList.onTouchEvent(localMotionEvent);
    localMotionEvent.recycle();
  }
  
  private void cancelPendingDrag()
  {
    mPendingDrag = -1L;
  }
  
  private TextView createPreviewTextView(Context paramContext)
  {
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-2, -2);
    paramContext = new TextView(paramContext);
    paramContext.setLayoutParams(localLayoutParams);
    paramContext.setSingleLine(true);
    paramContext.setEllipsize(TextUtils.TruncateAt.MIDDLE);
    paramContext.setGravity(17);
    paramContext.setAlpha(0.0F);
    paramContext.setLayoutDirection(mList.getLayoutDirection());
    return paramContext;
  }
  
  private float getPosFromItemCount(int paramInt1, int paramInt2, int paramInt3)
  {
    SectionIndexer localSectionIndexer = mSectionIndexer;
    if ((localSectionIndexer == null) || (mListAdapter == null)) {
      getSectionsFromIndexer();
    }
    if ((paramInt2 != 0) && (paramInt3 != 0))
    {
      int i;
      if ((localSectionIndexer != null) && (mSections != null) && (mSections.length > 0)) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i != 0) && (mMatchDragPosition))
      {
        i = paramInt1 - mHeaderCount;
        if (i < 0) {
          return 0.0F;
        }
        paramInt3 -= mHeaderCount;
        View localView = mList.getChildAt(0);
        if ((localView != null) && (localView.getHeight() != 0)) {
          f = (mList.getPaddingTop() - localView.getTop()) / localView.getHeight();
        } else {
          f = 0.0F;
        }
        int j = localSectionIndexer.getSectionForPosition(i);
        int k = localSectionIndexer.getPositionForSection(j);
        int m = mSections.length;
        if (j < m - 1)
        {
          if (j + 1 < m) {
            paramInt1 = localSectionIndexer.getPositionForSection(j + 1);
          } else {
            paramInt1 = paramInt3 - 1;
          }
          paramInt1 -= k;
        }
        else
        {
          paramInt1 = paramInt3 - k;
        }
        if (paramInt1 == 0) {
          f = 0.0F;
        } else {
          f = (i + f - k) / paramInt1;
        }
        float f = (j + f) / m;
        if ((i > 0) && (i + paramInt2 == paramInt3))
        {
          localView = mList.getChildAt(paramInt2 - 1);
          paramInt3 = mList.getPaddingBottom();
          if (mList.getClipToPadding())
          {
            paramInt1 = localView.getHeight();
            paramInt2 = mList.getHeight() - paramInt3 - localView.getTop();
          }
          else
          {
            paramInt1 = localView.getHeight();
            paramInt2 = mList.getHeight() - localView.getTop();
            paramInt1 += paramInt3;
          }
          if ((paramInt2 > 0) && (paramInt1 > 0)) {
            f += (1.0F - f) * (paramInt2 / paramInt1);
          } else {}
        }
        return f;
      }
      if (paramInt2 == paramInt3) {
        return 0.0F;
      }
      return paramInt1 / (paramInt3 - paramInt2);
    }
    return 0.0F;
  }
  
  private float getPosFromMotionEvent(float paramFloat)
  {
    if (mThumbRange <= 0.0F) {
      return 0.0F;
    }
    return MathUtils.constrain((paramFloat - mThumbOffset) / mThumbRange, 0.0F, 1.0F);
  }
  
  private void getSectionsFromIndexer()
  {
    mSectionIndexer = null;
    Object localObject1 = mList.getAdapter();
    Object localObject2 = localObject1;
    if ((localObject1 instanceof HeaderViewListAdapter))
    {
      mHeaderCount = ((HeaderViewListAdapter)localObject1).getHeadersCount();
      localObject2 = ((HeaderViewListAdapter)localObject1).getWrappedAdapter();
    }
    if ((localObject2 instanceof ExpandableListConnector))
    {
      localObject1 = ((ExpandableListConnector)localObject2).getAdapter();
      if ((localObject1 instanceof SectionIndexer))
      {
        mSectionIndexer = ((SectionIndexer)localObject1);
        mListAdapter = ((Adapter)localObject2);
        mSections = mSectionIndexer.getSections();
      }
    }
    else if ((localObject2 instanceof SectionIndexer))
    {
      mListAdapter = ((Adapter)localObject2);
      mSectionIndexer = ((SectionIndexer)localObject2);
      mSections = mSectionIndexer.getSections();
    }
    else
    {
      mListAdapter = ((Adapter)localObject2);
      mSections = null;
    }
  }
  
  private static Animator groupAnimatorOfFloat(Property<View, Float> paramProperty, float paramFloat, View... paramVarArgs)
  {
    AnimatorSet localAnimatorSet = new AnimatorSet();
    AnimatorSet.Builder localBuilder = null;
    for (int i = paramVarArgs.length - 1; i >= 0; i--)
    {
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramVarArgs[i], paramProperty, new float[] { paramFloat });
      if (localBuilder == null) {
        localBuilder = localAnimatorSet.play(localObjectAnimator);
      } else {
        localBuilder.with(localObjectAnimator);
      }
    }
    return localAnimatorSet;
  }
  
  private boolean isPointInside(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if ((isPointInsideX(paramFloat1)) && ((mTrackDrawable != null) || (isPointInsideY(paramFloat2)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isPointInsideX(float paramFloat)
  {
    float f1 = mThumbImage.getTranslationX();
    float f2 = mThumbImage.getLeft();
    float f3 = mThumbImage.getRight();
    f1 = mMinimumTouchTarget - (f3 + f1 - (f2 + f1));
    f3 = 0.0F;
    if (f1 > 0.0F) {
      f3 = f1;
    }
    boolean bool1 = mLayoutFromRight;
    boolean bool2 = false;
    boolean bool3 = false;
    if (bool1)
    {
      if (paramFloat >= mThumbImage.getLeft() - f3) {
        bool3 = true;
      }
      return bool3;
    }
    bool3 = bool2;
    if (paramFloat <= mThumbImage.getRight() + f3) {
      bool3 = true;
    }
    return bool3;
  }
  
  private boolean isPointInsideY(float paramFloat)
  {
    float f1 = mThumbImage.getTranslationY();
    float f2 = mThumbImage.getTop() + f1;
    float f3 = mThumbImage.getBottom() + f1;
    float f4 = mMinimumTouchTarget - (f3 - f2);
    f1 = 0.0F;
    if (f4 > 0.0F) {
      f1 = f4 / 2.0F;
    }
    boolean bool;
    if ((paramFloat >= f2 - f1) && (paramFloat <= f3 + f1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void layoutThumb()
  {
    Rect localRect = mTempBounds;
    measureViewToSide(mThumbImage, null, null, localRect);
    applyLayout(mThumbImage, localRect);
  }
  
  private void layoutTrack()
  {
    ImageView localImageView1 = mTrackImage;
    ImageView localImageView2 = mThumbImage;
    Rect localRect = mContainerRect;
    int i = Math.max(0, localRect.width());
    int j = Math.max(0, localRect.height());
    localImageView1.measure(View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), View.MeasureSpec.makeSafeMeasureSpec(j, 0));
    if (mThumbPosition == 1)
    {
      j = top;
      i = bottom;
    }
    else
    {
      i = localImageView2.getHeight() / 2;
      j = top + i;
      i = bottom - i;
    }
    int k = localImageView1.getMeasuredWidth();
    int m = localImageView2.getLeft() + (localImageView2.getWidth() - k) / 2;
    localImageView1.layout(m, j, m + k, i);
  }
  
  private void measureFloating(View paramView, Rect paramRect1, Rect paramRect2)
  {
    if (paramRect1 == null)
    {
      i = 0;
      j = 0;
      k = 0;
    }
    else
    {
      i = left;
      j = top;
      k = right;
    }
    paramRect1 = mContainerRect;
    int m = paramRect1.width();
    int n = Math.max(0, paramRect1.height());
    paramView.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, m - i - k), Integer.MIN_VALUE), View.MeasureSpec.makeSafeMeasureSpec(n, 0));
    int k = paramRect1.height();
    int i = paramView.getMeasuredWidth();
    k = k / 10 + j + top;
    int j = paramView.getMeasuredHeight();
    m = (m - i) / 2 + left;
    paramRect2.set(m, k, m + i, j + k);
  }
  
  private void measurePreview(View paramView, Rect paramRect)
  {
    Rect localRect = mTempMargins;
    left = mPreviewImage.getPaddingLeft();
    top = mPreviewImage.getPaddingTop();
    right = mPreviewImage.getPaddingRight();
    bottom = mPreviewImage.getPaddingBottom();
    if (mOverlayPosition == 0) {
      measureFloating(paramView, localRect, paramRect);
    } else {
      measureViewToSide(paramView, mThumbImage, localRect, paramRect);
    }
  }
  
  private void measureViewToSide(View paramView1, View paramView2, Rect paramRect1, Rect paramRect2)
  {
    int i;
    int j;
    int k;
    if (paramRect1 == null)
    {
      i = 0;
      j = 0;
      k = 0;
    }
    else
    {
      i = left;
      j = top;
      k = right;
    }
    paramRect1 = mContainerRect;
    int m = paramRect1.width();
    if (paramView2 != null)
    {
      while (mLayoutFromRight) {
        m = paramView2.getLeft();
      }
      m -= paramView2.getRight();
    }
    int n = Math.max(0, paramRect1.height());
    m = Math.max(0, m - i - k);
    paramView1.measure(View.MeasureSpec.makeMeasureSpec(m, Integer.MIN_VALUE), View.MeasureSpec.makeSafeMeasureSpec(n, 0));
    n = Math.min(m, paramView1.getMeasuredWidth());
    if (mLayoutFromRight)
    {
      if (paramView2 == null) {
        m = right;
      } else {
        m = paramView2.getLeft();
      }
      i = m - k;
      m = i - n;
    }
    for (;;)
    {
      break;
      if (paramView2 == null) {
        m = left;
      } else {
        m = paramView2.getRight();
      }
      m += i;
      i = m + n;
    }
    paramRect2.set(m, j, i, j + paramView1.getMeasuredHeight());
  }
  
  private void onStateDependencyChanged(boolean paramBoolean)
  {
    if (isEnabled())
    {
      if (isAlwaysShowEnabled())
      {
        setState(1);
      }
      else if (mState == 1)
      {
        postAutoHide();
      }
      else if (paramBoolean)
      {
        setState(1);
        postAutoHide();
      }
    }
    else {
      stop();
    }
    mList.resolvePadding();
  }
  
  private void postAutoHide()
  {
    mList.removeCallbacks(mDeferHide);
    mList.postDelayed(mDeferHide, 1500L);
  }
  
  private void refreshDrawablePressedState()
  {
    boolean bool;
    if (mState == 2) {
      bool = true;
    } else {
      bool = false;
    }
    mThumbImage.setPressed(bool);
    mTrackImage.setPressed(bool);
  }
  
  private void scrollTo(float paramFloat)
  {
    mScrollCompleted = false;
    int i = mList.getCount();
    Object localObject = mSections;
    int j;
    if (localObject == null) {
      j = 0;
    } else {
      j = localObject.length;
    }
    int m;
    int i7;
    if ((localObject != null) && (j > 1))
    {
      int k = MathUtils.constrain((int)(j * paramFloat), 0, j - 1);
      m = k;
      int n = mSectionIndexer.getPositionForSection(m);
      int i1 = m;
      int i2 = i;
      int i3 = n;
      int i4 = m;
      int i5 = m + 1;
      if (m < j - 1) {
        i2 = mSectionIndexer.getPositionForSection(m + 1);
      }
      int i6 = m;
      i7 = i1;
      int i8 = i3;
      int i9 = i4;
      if (i2 == n)
      {
        i8 = i3;
        i3 = m;
        int i10;
        do
        {
          i6 = i3;
          i7 = i1;
          i9 = i4;
          if (i3 <= 0) {
            break;
          }
          m = i3 - 1;
          i10 = mSectionIndexer.getPositionForSection(m);
          if (i10 != n)
          {
            i9 = m;
            i7 = m;
            i6 = m;
            i8 = i10;
            break;
          }
          i3 = m;
          i8 = i10;
        } while (m != 0);
        i7 = 0;
        i9 = i4;
        i8 = i10;
        i6 = m;
      }
      i6 = i5 + 1;
      for (m = i5; (i6 < j) && (mSectionIndexer.getPositionForSection(i6) == i2); m++) {
        i6++;
      }
      float f1 = i9 / j;
      float f2 = m / j;
      float f3;
      if (i == 0) {
        f3 = Float.MAX_VALUE;
      } else {
        f3 = 0.125F / i;
      }
      if ((i9 != k) || (paramFloat - f1 >= f3)) {
        i8 = (int)((i2 - i8) * (paramFloat - f1) / (f2 - f1)) + i8;
      }
      m = MathUtils.constrain(i8, 0, i - 1);
      if ((mList instanceof ExpandableListView))
      {
        localObject = (ExpandableListView)mList;
        ((ExpandableListView)localObject).setSelectionFromTop(((ExpandableListView)localObject).getFlatListPosition(ExpandableListView.getPackedPositionForGroup(mHeaderCount + m)), 0);
      }
      else if ((mList instanceof ListView))
      {
        ((ListView)mList).setSelectionFromTop(mHeaderCount + m, 0);
      }
      else
      {
        mList.setSelection(mHeaderCount + m);
      }
    }
    else
    {
      m = MathUtils.constrain((int)(i * paramFloat), 0, i - 1);
      if ((mList instanceof ExpandableListView))
      {
        localObject = (ExpandableListView)mList;
        ((ExpandableListView)localObject).setSelectionFromTop(((ExpandableListView)localObject).getFlatListPosition(ExpandableListView.getPackedPositionForGroup(mHeaderCount + m)), 0);
      }
      else if ((mList instanceof ListView))
      {
        ((ListView)mList).setSelectionFromTop(mHeaderCount + m, 0);
      }
      else
      {
        mList.setSelection(mHeaderCount + m);
      }
      i7 = -1;
    }
    if (mCurrentSection != i7)
    {
      mCurrentSection = i7;
      boolean bool = transitionPreviewLayout(i7);
      if ((!mShowingPreview) && (bool)) {
        transitionToDragging();
      } else if ((mShowingPreview) && (!bool)) {
        transitionToVisible();
      }
    }
  }
  
  private void setState(int paramInt)
  {
    mList.removeCallbacks(mDeferHide);
    int i = paramInt;
    if (mAlwaysShow)
    {
      i = paramInt;
      if (paramInt == 0) {
        i = 1;
      }
    }
    if (i == mState) {
      return;
    }
    switch (i)
    {
    default: 
      break;
    case 2: 
      if (transitionPreviewLayout(mCurrentSection)) {
        transitionToDragging();
      } else {
        transitionToVisible();
      }
      break;
    case 1: 
      transitionToVisible();
      break;
    case 0: 
      transitionToHidden();
    }
    mState = i;
    refreshDrawablePressedState();
  }
  
  private void setThumbPos(float paramFloat)
  {
    paramFloat = mThumbRange * paramFloat + mThumbOffset;
    mThumbImage.setTranslationY(paramFloat - mThumbImage.getHeight() / 2.0F);
    View localView = mPreviewImage;
    float f = localView.getHeight() / 2.0F;
    switch (mOverlayPosition)
    {
    default: 
      paramFloat = 0.0F;
      break;
    case 2: 
      paramFloat -= f;
      break;
    }
    Rect localRect = mContainerRect;
    int i = top;
    int j = bottom;
    paramFloat = MathUtils.constrain(paramFloat, i + f, j - f) - f;
    localView.setTranslationY(paramFloat);
    mPrimaryText.setTranslationY(paramFloat);
    mSecondaryText.setTranslationY(paramFloat);
  }
  
  private void startPendingDrag()
  {
    mPendingDrag = (SystemClock.uptimeMillis() + TAP_TIMEOUT);
  }
  
  private boolean transitionPreviewLayout(int paramInt)
  {
    Object localObject1 = mSections;
    TextView localTextView = null;
    Object localObject2 = localTextView;
    if (localObject1 != null)
    {
      localObject2 = localTextView;
      if (paramInt >= 0)
      {
        localObject2 = localTextView;
        if (paramInt < localObject1.length)
        {
          localObject1 = localObject1[paramInt];
          localObject2 = localTextView;
          if (localObject1 != null) {
            localObject2 = localObject1.toString();
          }
        }
      }
    }
    Object localObject3 = mTempBounds;
    View localView = mPreviewImage;
    if (mShowingPrimary)
    {
      localTextView = mPrimaryText;
      localObject1 = mSecondaryText;
    }
    else
    {
      localTextView = mSecondaryText;
      localObject1 = mPrimaryText;
    }
    ((TextView)localObject1).setText((CharSequence)localObject2);
    measurePreview((View)localObject1, (Rect)localObject3);
    applyLayout((View)localObject1, (Rect)localObject3);
    if (mPreviewAnimation != null) {
      mPreviewAnimation.cancel();
    }
    Animator localAnimator = animateAlpha((View)localObject1, 1.0F).setDuration(50L);
    Object localObject4 = animateAlpha(localTextView, 0.0F).setDuration(50L);
    ((Animator)localObject4).addListener(mSwitchPrimaryListener);
    left -= localView.getPaddingLeft();
    top -= localView.getPaddingTop();
    right += localView.getPaddingRight();
    bottom += localView.getPaddingBottom();
    localObject3 = animateBounds(localView, (Rect)localObject3);
    ((Animator)localObject3).setDuration(100L);
    mPreviewAnimation = new AnimatorSet();
    localObject4 = mPreviewAnimation.play((Animator)localObject4).with(localAnimator);
    ((AnimatorSet.Builder)localObject4).with((Animator)localObject3);
    int i = localView.getWidth() - localView.getPaddingLeft() - localView.getPaddingRight();
    paramInt = ((TextView)localObject1).getWidth();
    if (paramInt > i)
    {
      ((TextView)localObject1).setScaleX(i / paramInt);
      ((AnimatorSet.Builder)localObject4).with(animateScaleX((View)localObject1, 1.0F).setDuration(100L));
    }
    else
    {
      ((TextView)localObject1).setScaleX(1.0F);
    }
    i = localTextView.getWidth();
    if (i > paramInt) {
      ((AnimatorSet.Builder)localObject4).with(animateScaleX(localTextView, paramInt / i).setDuration(100L));
    }
    mPreviewAnimation.start();
    return TextUtils.isEmpty((CharSequence)localObject2) ^ true;
  }
  
  private void transitionToDragging()
  {
    if (mDecorAnimation != null) {
      mDecorAnimation.cancel();
    }
    Animator localAnimator1 = groupAnimatorOfFloat(View.ALPHA, 1.0F, new View[] { mThumbImage, mTrackImage, mPreviewImage }).setDuration(150L);
    Animator localAnimator2 = groupAnimatorOfFloat(View.TRANSLATION_X, 0.0F, new View[] { mThumbImage, mTrackImage }).setDuration(150L);
    mDecorAnimation = new AnimatorSet();
    mDecorAnimation.playTogether(new Animator[] { localAnimator1, localAnimator2 });
    mDecorAnimation.start();
    mShowingPreview = true;
  }
  
  private void transitionToHidden()
  {
    if (mDecorAnimation != null) {
      mDecorAnimation.cancel();
    }
    Animator localAnimator1 = groupAnimatorOfFloat(View.ALPHA, 0.0F, new View[] { mThumbImage, mTrackImage, mPreviewImage, mPrimaryText, mSecondaryText }).setDuration(300L);
    if (mLayoutFromRight) {}
    float f;
    for (int i = mThumbImage.getWidth();; i = -mThumbImage.getWidth())
    {
      f = i;
      break;
    }
    Animator localAnimator2 = groupAnimatorOfFloat(View.TRANSLATION_X, f, new View[] { mThumbImage, mTrackImage }).setDuration(300L);
    mDecorAnimation = new AnimatorSet();
    mDecorAnimation.playTogether(new Animator[] { localAnimator1, localAnimator2 });
    mDecorAnimation.start();
    mShowingPreview = false;
  }
  
  private void transitionToVisible()
  {
    if (mDecorAnimation != null) {
      mDecorAnimation.cancel();
    }
    Animator localAnimator1 = groupAnimatorOfFloat(View.ALPHA, 1.0F, new View[] { mThumbImage, mTrackImage }).setDuration(150L);
    Animator localAnimator2 = groupAnimatorOfFloat(View.ALPHA, 0.0F, new View[] { mPreviewImage, mPrimaryText, mSecondaryText }).setDuration(300L);
    Animator localAnimator3 = groupAnimatorOfFloat(View.TRANSLATION_X, 0.0F, new View[] { mThumbImage, mTrackImage }).setDuration(150L);
    mDecorAnimation = new AnimatorSet();
    mDecorAnimation.playTogether(new Animator[] { localAnimator1, localAnimator2, localAnimator3 });
    mDecorAnimation.start();
    mShowingPreview = false;
  }
  
  private void updateAppearance()
  {
    int i = 0;
    mTrackImage.setImageDrawable(mTrackDrawable);
    if (mTrackDrawable != null) {
      i = Math.max(0, mTrackDrawable.getIntrinsicWidth());
    }
    mThumbImage.setImageDrawable(mThumbDrawable);
    mThumbImage.setMinimumWidth(mThumbMinWidth);
    mThumbImage.setMinimumHeight(mThumbMinHeight);
    int j = i;
    if (mThumbDrawable != null) {
      j = Math.max(i, mThumbDrawable.getIntrinsicWidth());
    }
    mWidth = Math.max(j, mThumbMinWidth);
    if (mTextAppearance != 0)
    {
      mPrimaryText.setTextAppearance(mTextAppearance);
      mSecondaryText.setTextAppearance(mTextAppearance);
    }
    if (mTextColor != null)
    {
      mPrimaryText.setTextColor(mTextColor);
      mSecondaryText.setTextColor(mTextColor);
    }
    if (mTextSize > 0.0F)
    {
      mPrimaryText.setTextSize(0, mTextSize);
      mSecondaryText.setTextSize(0, mTextSize);
    }
    i = mPreviewPadding;
    mPrimaryText.setIncludeFontPadding(false);
    mPrimaryText.setPadding(i, i, i, i);
    mSecondaryText.setIncludeFontPadding(false);
    mSecondaryText.setPadding(i, i, i, i);
    refreshDrawablePressedState();
  }
  
  private void updateContainerRect()
  {
    AbsListView localAbsListView = mList;
    localAbsListView.resolvePadding();
    Rect localRect = mContainerRect;
    left = 0;
    top = 0;
    right = localAbsListView.getWidth();
    bottom = localAbsListView.getHeight();
    int i = mScrollBarStyle;
    if ((i == 16777216) || (i == 0))
    {
      left += localAbsListView.getPaddingLeft();
      top += localAbsListView.getPaddingTop();
      right -= localAbsListView.getPaddingRight();
      bottom -= localAbsListView.getPaddingBottom();
      if (i == 16777216)
      {
        i = getWidth();
        if (mScrollbarPosition == 2) {
          right += i;
        } else {
          left -= i;
        }
      }
    }
  }
  
  private void updateLongList(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 > 0) && (paramInt2 / paramInt1 >= 4)) {
      bool = true;
    } else {
      bool = false;
    }
    if (mLongList != bool)
    {
      mLongList = bool;
      onStateDependencyChanged(false);
    }
  }
  
  private void updateOffsetAndRange()
  {
    ImageView localImageView1 = mTrackImage;
    ImageView localImageView2 = mThumbImage;
    float f1;
    float f2;
    if (mThumbPosition == 1)
    {
      f1 = localImageView2.getHeight() / 2.0F;
      f2 = localImageView1.getTop() + f1;
      f1 = localImageView1.getBottom() - f1;
    }
    else
    {
      f2 = localImageView1.getTop();
      f1 = localImageView1.getBottom();
    }
    mThumbOffset = f2;
    mThumbRange = (f1 - f2);
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public boolean isAlwaysShowEnabled()
  {
    return mAlwaysShow;
  }
  
  public boolean isEnabled()
  {
    boolean bool;
    if ((mEnabled) && ((mLongList) || (mAlwaysShow))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean onInterceptHoverEvent(MotionEvent paramMotionEvent)
  {
    if (!isEnabled()) {
      return false;
    }
    int i = paramMotionEvent.getActionMasked();
    if (((i == 9) || (i == 7)) && (mState == 0) && (isPointInside(paramMotionEvent.getX(), paramMotionEvent.getY())))
    {
      setState(1);
      postAutoHide();
    }
    return false;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!isEnabled()) {
      return false;
    }
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 2: 
      if (!isPointInside(paramMotionEvent.getX(), paramMotionEvent.getY()))
      {
        cancelPendingDrag();
      }
      else if ((mPendingDrag >= 0L) && (mPendingDrag <= SystemClock.uptimeMillis()))
      {
        beginDrag();
        scrollTo(getPosFromMotionEvent(mInitialTouchY));
        return onTouchEvent(paramMotionEvent);
      }
      break;
    case 1: 
    case 3: 
      cancelPendingDrag();
      break;
    case 0: 
      if (isPointInside(paramMotionEvent.getX(), paramMotionEvent.getY()))
      {
        if (!mList.isInScrollingContainer()) {
          return true;
        }
        mInitialTouchY = paramMotionEvent.getY();
        startPendingDrag();
      }
      break;
    }
    return false;
  }
  
  public void onItemCountChanged(int paramInt1, int paramInt2)
  {
    if ((mOldItemCount != paramInt2) || (mOldChildCount != paramInt1))
    {
      mOldItemCount = paramInt2;
      mOldChildCount = paramInt1;
      int i;
      if (paramInt2 - paramInt1 > 0) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i != 0) && (mState != 2)) {
        setThumbPos(getPosFromItemCount(mList.getFirstVisiblePosition(), paramInt1, paramInt2));
      }
      updateLongList(paramInt1, paramInt2);
    }
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    if ((mState != 2) && (!isPointInside(paramMotionEvent.getX(), paramMotionEvent.getY()))) {
      return null;
    }
    return PointerIcon.getSystemIcon(mList.getContext(), 1000);
  }
  
  public void onScroll(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool = isEnabled();
    int i = 0;
    if (!bool)
    {
      setState(0);
      return;
    }
    if (paramInt3 - paramInt2 > 0) {
      i = 1;
    }
    if ((i != 0) && (mState != 2)) {
      setThumbPos(getPosFromItemCount(paramInt1, paramInt2, paramInt3));
    }
    mScrollCompleted = true;
    if (mFirstVisibleItem != paramInt1)
    {
      mFirstVisibleItem = paramInt1;
      if (mState != 2)
      {
        setState(1);
        postAutoHide();
      }
    }
  }
  
  public void onSectionsChanged()
  {
    mListAdapter = null;
  }
  
  public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    updateLayout();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!isEnabled()) {
      return false;
    }
    float f;
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 3: 
      cancelPendingDrag();
      break;
    case 2: 
      if ((mPendingDrag >= 0L) && (Math.abs(paramMotionEvent.getY() - mInitialTouchY) > mScaledTouchSlop)) {
        beginDrag();
      }
      if (mState == 2)
      {
        f = getPosFromMotionEvent(paramMotionEvent.getY());
        setThumbPos(f);
        if (mScrollCompleted) {
          scrollTo(f);
        }
        return true;
      }
      break;
    case 1: 
      if (mPendingDrag >= 0L)
      {
        beginDrag();
        f = getPosFromMotionEvent(paramMotionEvent.getY());
        setThumbPos(f);
        scrollTo(f);
      }
      if (mState == 2)
      {
        if (mList != null)
        {
          mList.requestDisallowInterceptTouchEvent(false);
          mList.reportScrollStateChange(0);
        }
        setState(1);
        postAutoHide();
        return true;
      }
      break;
    case 0: 
      if ((isPointInside(paramMotionEvent.getX(), paramMotionEvent.getY())) && (!mList.isInScrollingContainer()))
      {
        beginDrag();
        return true;
      }
      break;
    }
    return false;
  }
  
  public void remove()
  {
    mOverlay.remove(mTrackImage);
    mOverlay.remove(mThumbImage);
    mOverlay.remove(mPreviewImage);
    mOverlay.remove(mPrimaryText);
    mOverlay.remove(mSecondaryText);
  }
  
  public void setAlwaysShow(boolean paramBoolean)
  {
    if (mAlwaysShow != paramBoolean)
    {
      mAlwaysShow = paramBoolean;
      onStateDependencyChanged(false);
    }
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if (mEnabled != paramBoolean)
    {
      mEnabled = paramBoolean;
      onStateDependencyChanged(true);
    }
  }
  
  public void setScrollBarStyle(int paramInt)
  {
    if (mScrollBarStyle != paramInt)
    {
      mScrollBarStyle = paramInt;
      updateLayout();
    }
  }
  
  public void setScrollbarPosition(int paramInt)
  {
    boolean bool = true;
    int i = paramInt;
    if (paramInt == 0)
    {
      if (mList.isLayoutRtl()) {
        paramInt = 1;
      } else {
        paramInt = 2;
      }
      i = paramInt;
    }
    if (mScrollbarPosition != i)
    {
      mScrollbarPosition = i;
      if (i == 1) {
        bool = false;
      }
      mLayoutFromRight = bool;
      paramInt = mPreviewResId[mLayoutFromRight];
      mPreviewImage.setBackgroundResource(paramInt);
      paramInt = Math.max(0, mPreviewMinWidth - mPreviewImage.getPaddingLeft() - mPreviewImage.getPaddingRight());
      mPrimaryText.setMinimumWidth(paramInt);
      mSecondaryText.setMinimumWidth(paramInt);
      paramInt = Math.max(0, mPreviewMinHeight - mPreviewImage.getPaddingTop() - mPreviewImage.getPaddingBottom());
      mPrimaryText.setMinimumHeight(paramInt);
      mSecondaryText.setMinimumHeight(paramInt);
      updateLayout();
    }
  }
  
  public void setStyle(int paramInt)
  {
    TypedArray localTypedArray = mList.getContext().obtainStyledAttributes(null, R.styleable.FastScroll, 16843767, paramInt);
    int i = localTypedArray.getIndexCount();
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      int j = localTypedArray.getIndex(paramInt);
      switch (j)
      {
      default: 
        break;
      case 13: 
        mTrackDrawable = localTypedArray.getDrawable(j);
        break;
      case 12: 
        mThumbMinWidth = localTypedArray.getDimensionPixelSize(j, 0);
        break;
      case 11: 
        mThumbMinHeight = localTypedArray.getDimensionPixelSize(j, 0);
        break;
      case 10: 
        mThumbDrawable = localTypedArray.getDrawable(j);
        break;
      case 9: 
        mOverlayPosition = localTypedArray.getInt(j, 0);
        break;
      case 8: 
        mPreviewResId[1] = localTypedArray.getResourceId(j, 0);
        break;
      case 7: 
        mPreviewResId[0] = localTypedArray.getResourceId(j, 0);
        break;
      case 6: 
        mThumbPosition = localTypedArray.getInt(j, 0);
        break;
      case 5: 
        mPreviewMinHeight = localTypedArray.getDimensionPixelSize(j, 0);
        break;
      case 4: 
        mPreviewMinWidth = localTypedArray.getDimensionPixelSize(j, 0);
        break;
      case 3: 
        mPreviewPadding = localTypedArray.getDimensionPixelSize(j, 0);
        break;
      case 2: 
        mTextColor = localTypedArray.getColorStateList(j);
        break;
      case 1: 
        mTextSize = localTypedArray.getDimensionPixelSize(j, 0);
        break;
      case 0: 
        mTextAppearance = localTypedArray.getResourceId(j, 0);
      }
    }
    localTypedArray.recycle();
    updateAppearance();
  }
  
  public void stop()
  {
    setState(0);
  }
  
  public void updateLayout()
  {
    if (mUpdatingLayout) {
      return;
    }
    mUpdatingLayout = true;
    updateContainerRect();
    layoutThumb();
    layoutTrack();
    updateOffsetAndRange();
    Rect localRect = mTempBounds;
    measurePreview(mPrimaryText, localRect);
    applyLayout(mPrimaryText, localRect);
    measurePreview(mSecondaryText, localRect);
    applyLayout(mSecondaryText, localRect);
    if (mPreviewImage != null)
    {
      left -= mPreviewImage.getPaddingLeft();
      top -= mPreviewImage.getPaddingTop();
      right += mPreviewImage.getPaddingRight();
      bottom += mPreviewImage.getPaddingBottom();
      applyLayout(mPreviewImage, localRect);
    }
    mUpdatingLayout = false;
  }
}
