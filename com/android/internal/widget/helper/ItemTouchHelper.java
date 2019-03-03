package com.android.internal.widget.helper;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import com.android.internal.widget.RecyclerView;
import com.android.internal.widget.RecyclerView.ChildDrawingOrderCallback;
import com.android.internal.widget.RecyclerView.ItemAnimator;
import com.android.internal.widget.RecyclerView.ItemDecoration;
import com.android.internal.widget.RecyclerView.LayoutManager;
import com.android.internal.widget.RecyclerView.OnChildAttachStateChangeListener;
import com.android.internal.widget.RecyclerView.OnItemTouchListener;
import com.android.internal.widget.RecyclerView.State;
import com.android.internal.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper
  extends RecyclerView.ItemDecoration
  implements RecyclerView.OnChildAttachStateChangeListener
{
  static final int ACTION_MODE_DRAG_MASK = 16711680;
  private static final int ACTION_MODE_IDLE_MASK = 255;
  static final int ACTION_MODE_SWIPE_MASK = 65280;
  public static final int ACTION_STATE_DRAG = 2;
  public static final int ACTION_STATE_IDLE = 0;
  public static final int ACTION_STATE_SWIPE = 1;
  static final int ACTIVE_POINTER_ID_NONE = -1;
  public static final int ANIMATION_TYPE_DRAG = 8;
  public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
  public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
  static final boolean DEBUG = false;
  static final int DIRECTION_FLAG_COUNT = 8;
  public static final int DOWN = 2;
  public static final int END = 32;
  public static final int LEFT = 4;
  private static final int PIXELS_PER_SECOND = 1000;
  public static final int RIGHT = 8;
  public static final int START = 16;
  static final String TAG = "ItemTouchHelper";
  public static final int UP = 1;
  int mActionState = 0;
  int mActivePointerId = -1;
  Callback mCallback;
  private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
  private List<Integer> mDistances;
  private long mDragScrollStartTimeInMs;
  float mDx;
  float mDy;
  GestureDetector mGestureDetector;
  float mInitialTouchX;
  float mInitialTouchY;
  float mMaxSwipeVelocity;
  private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener()
  {
    public boolean onInterceptTouchEvent(RecyclerView paramAnonymousRecyclerView, MotionEvent paramAnonymousMotionEvent)
    {
      mGestureDetector.onTouchEvent(paramAnonymousMotionEvent);
      int i = paramAnonymousMotionEvent.getActionMasked();
      boolean bool = true;
      if (i == 0)
      {
        mActivePointerId = paramAnonymousMotionEvent.getPointerId(0);
        mInitialTouchX = paramAnonymousMotionEvent.getX();
        mInitialTouchY = paramAnonymousMotionEvent.getY();
        obtainVelocityTracker();
        if (mSelected == null)
        {
          paramAnonymousRecyclerView = findAnimation(paramAnonymousMotionEvent);
          if (paramAnonymousRecyclerView != null)
          {
            ItemTouchHelper localItemTouchHelper = ItemTouchHelper.this;
            mInitialTouchX -= mX;
            localItemTouchHelper = ItemTouchHelper.this;
            mInitialTouchY -= mY;
            endRecoverAnimation(mViewHolder, true);
            if (mPendingCleanup.remove(mViewHolder.itemView)) {
              mCallback.clearView(mRecyclerView, mViewHolder);
            }
            select(mViewHolder, mActionState);
            updateDxDy(paramAnonymousMotionEvent, mSelectedFlags, 0);
          }
        }
      }
      else if ((i != 3) && (i != 1))
      {
        if (mActivePointerId != -1)
        {
          int j = paramAnonymousMotionEvent.findPointerIndex(mActivePointerId);
          if (j >= 0) {
            checkSelectForSwipe(i, paramAnonymousMotionEvent, j);
          }
        }
      }
      else
      {
        mActivePointerId = -1;
        select(null, 0);
      }
      if (mVelocityTracker != null) {
        mVelocityTracker.addMovement(paramAnonymousMotionEvent);
      }
      if (mSelected == null) {
        bool = false;
      }
      return bool;
    }
    
    public void onRequestDisallowInterceptTouchEvent(boolean paramAnonymousBoolean)
    {
      if (!paramAnonymousBoolean) {
        return;
      }
      select(null, 0);
    }
    
    public void onTouchEvent(RecyclerView paramAnonymousRecyclerView, MotionEvent paramAnonymousMotionEvent)
    {
      mGestureDetector.onTouchEvent(paramAnonymousMotionEvent);
      if (mVelocityTracker != null) {
        mVelocityTracker.addMovement(paramAnonymousMotionEvent);
      }
      if (mActivePointerId == -1) {
        return;
      }
      int i = paramAnonymousMotionEvent.getActionMasked();
      int j = paramAnonymousMotionEvent.findPointerIndex(mActivePointerId);
      if (j >= 0) {
        checkSelectForSwipe(i, paramAnonymousMotionEvent, j);
      }
      paramAnonymousRecyclerView = mSelected;
      if (paramAnonymousRecyclerView == null) {
        return;
      }
      int k = 0;
      if (i != 6)
      {
        switch (i)
        {
        default: 
          break;
        case 3: 
          if (mVelocityTracker != null) {
            mVelocityTracker.clear();
          }
          break;
        case 2: 
          if (j < 0) {
            return;
          }
          updateDxDy(paramAnonymousMotionEvent, mSelectedFlags, j);
          moveIfNecessary(paramAnonymousRecyclerView);
          mRecyclerView.removeCallbacks(mScrollRunnable);
          mScrollRunnable.run();
          mRecyclerView.invalidate();
          break;
        }
        select(null, 0);
        mActivePointerId = -1;
      }
      else
      {
        j = paramAnonymousMotionEvent.getActionIndex();
        if (paramAnonymousMotionEvent.getPointerId(j) == mActivePointerId)
        {
          if (j == 0) {
            k = 1;
          }
          mActivePointerId = paramAnonymousMotionEvent.getPointerId(k);
          updateDxDy(paramAnonymousMotionEvent, mSelectedFlags, j);
        }
      }
    }
  };
  View mOverdrawChild = null;
  int mOverdrawChildPosition = -1;
  final List<View> mPendingCleanup = new ArrayList();
  List<RecoverAnimation> mRecoverAnimations = new ArrayList();
  RecyclerView mRecyclerView;
  final Runnable mScrollRunnable = new Runnable()
  {
    public void run()
    {
      if ((mSelected != null) && (scrollIfNecessary()))
      {
        if (mSelected != null) {
          moveIfNecessary(mSelected);
        }
        mRecyclerView.removeCallbacks(mScrollRunnable);
        mRecyclerView.postOnAnimation(this);
      }
    }
  };
  RecyclerView.ViewHolder mSelected = null;
  int mSelectedFlags;
  float mSelectedStartX;
  float mSelectedStartY;
  private int mSlop;
  private List<RecyclerView.ViewHolder> mSwapTargets;
  float mSwipeEscapeVelocity;
  private final float[] mTmpPosition = new float[2];
  private Rect mTmpRect;
  VelocityTracker mVelocityTracker;
  
  public ItemTouchHelper(Callback paramCallback)
  {
    mCallback = paramCallback;
  }
  
  private void addChildDrawingOrderCallback()
  {
    if (Build.VERSION.SDK_INT >= 21) {
      return;
    }
    if (mChildDrawingOrderCallback == null) {
      mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback()
      {
        public int onGetChildDrawingOrder(int paramAnonymousInt1, int paramAnonymousInt2)
        {
          if (mOverdrawChild == null) {
            return paramAnonymousInt2;
          }
          int i = mOverdrawChildPosition;
          int j = i;
          if (i == -1)
          {
            j = mRecyclerView.indexOfChild(mOverdrawChild);
            mOverdrawChildPosition = j;
          }
          if (paramAnonymousInt2 == paramAnonymousInt1 - 1) {
            return j;
          }
          if (paramAnonymousInt2 < j) {
            paramAnonymousInt1 = paramAnonymousInt2;
          } else {
            paramAnonymousInt1 = paramAnonymousInt2 + 1;
          }
          return paramAnonymousInt1;
        }
      };
    }
    mRecyclerView.setChildDrawingOrderCallback(mChildDrawingOrderCallback);
  }
  
  private int checkHorizontalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    if ((paramInt & 0xC) != 0)
    {
      float f1 = mDx;
      int i = 4;
      int j;
      if (f1 > 0.0F) {
        j = 8;
      } else {
        j = 4;
      }
      if ((mVelocityTracker != null) && (mActivePointerId > -1))
      {
        mVelocityTracker.computeCurrentVelocity(1000, mCallback.getSwipeVelocityThreshold(mMaxSwipeVelocity));
        f2 = mVelocityTracker.getXVelocity(mActivePointerId);
        f1 = mVelocityTracker.getYVelocity(mActivePointerId);
        if (f2 > 0.0F) {
          i = 8;
        }
        f2 = Math.abs(f2);
        if (((i & paramInt) != 0) && (j == i) && (f2 >= mCallback.getSwipeEscapeVelocity(mSwipeEscapeVelocity)) && (f2 > Math.abs(f1))) {
          return i;
        }
      }
      f1 = mRecyclerView.getWidth();
      float f2 = mCallback.getSwipeThreshold(paramViewHolder);
      if (((paramInt & j) != 0) && (Math.abs(mDx) > f1 * f2)) {
        return j;
      }
    }
    return 0;
  }
  
  private int checkVerticalSwipe(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    if ((paramInt & 0x3) != 0)
    {
      float f1 = mDy;
      int i = 1;
      int j;
      if (f1 > 0.0F) {
        j = 2;
      } else {
        j = 1;
      }
      if ((mVelocityTracker != null) && (mActivePointerId > -1))
      {
        mVelocityTracker.computeCurrentVelocity(1000, mCallback.getSwipeVelocityThreshold(mMaxSwipeVelocity));
        f1 = mVelocityTracker.getXVelocity(mActivePointerId);
        f2 = mVelocityTracker.getYVelocity(mActivePointerId);
        if (f2 > 0.0F) {
          i = 2;
        }
        f2 = Math.abs(f2);
        if (((i & paramInt) != 0) && (i == j) && (f2 >= mCallback.getSwipeEscapeVelocity(mSwipeEscapeVelocity)) && (f2 > Math.abs(f1))) {
          return i;
        }
      }
      float f2 = mRecyclerView.getHeight();
      f1 = mCallback.getSwipeThreshold(paramViewHolder);
      if (((paramInt & j) != 0) && (Math.abs(mDy) > f2 * f1)) {
        return j;
      }
    }
    return 0;
  }
  
  private void destroyCallbacks()
  {
    mRecyclerView.removeItemDecoration(this);
    mRecyclerView.removeOnItemTouchListener(mOnItemTouchListener);
    mRecyclerView.removeOnChildAttachStateChangeListener(this);
    for (int i = mRecoverAnimations.size() - 1; i >= 0; i--)
    {
      RecoverAnimation localRecoverAnimation = (RecoverAnimation)mRecoverAnimations.get(0);
      mCallback.clearView(mRecyclerView, mViewHolder);
    }
    mRecoverAnimations.clear();
    mOverdrawChild = null;
    mOverdrawChildPosition = -1;
    releaseVelocityTracker();
  }
  
  private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder paramViewHolder)
  {
    Object localObject = paramViewHolder;
    if (mSwapTargets == null)
    {
      mSwapTargets = new ArrayList();
      mDistances = new ArrayList();
    }
    else
    {
      mSwapTargets.clear();
      mDistances.clear();
    }
    int i = mCallback.getBoundingBoxMargin();
    int j = Math.round(mSelectedStartX + mDx) - i;
    int k = Math.round(mSelectedStartY + mDy) - i;
    int m = itemView.getWidth() + j + 2 * i;
    int n = itemView.getHeight() + k + 2 * i;
    int i1 = (j + m) / 2;
    int i2 = (k + n) / 2;
    RecyclerView.LayoutManager localLayoutManager = mRecyclerView.getLayoutManager();
    int i3 = localLayoutManager.getChildCount();
    for (int i4 = 0; i4 < i3; i4++)
    {
      localObject = localLayoutManager.getChildAt(i4);
      if (localObject != itemView) {
        while ((((View)localObject).getBottom() >= k) && (((View)localObject).getTop() <= n) && (((View)localObject).getRight() >= j)) {
          if (((View)localObject).getLeft() <= m)
          {
            RecyclerView.ViewHolder localViewHolder = mRecyclerView.getChildViewHolder((View)localObject);
            if (mCallback.canDropOver(mRecyclerView, mSelected, localViewHolder))
            {
              int i5 = Math.abs(i1 - (((View)localObject).getLeft() + ((View)localObject).getRight()) / 2);
              int i6 = Math.abs(i2 - (((View)localObject).getTop() + ((View)localObject).getBottom()) / 2);
              int i7 = i5 * i5 + i6 * i6;
              i5 = mSwapTargets.size();
              int i8 = 0;
              for (i6 = 0; (i6 < i5) && (i7 > ((Integer)mDistances.get(i6)).intValue()); i6++) {
                i8++;
              }
              mSwapTargets.add(i8, localViewHolder);
              mDistances.add(i8, Integer.valueOf(i7));
            }
          }
        }
      }
    }
    return mSwapTargets;
  }
  
  private RecyclerView.ViewHolder findSwipedView(MotionEvent paramMotionEvent)
  {
    RecyclerView.LayoutManager localLayoutManager = mRecyclerView.getLayoutManager();
    if (mActivePointerId == -1) {
      return null;
    }
    int i = paramMotionEvent.findPointerIndex(mActivePointerId);
    float f1 = paramMotionEvent.getX(i);
    float f2 = mInitialTouchX;
    float f3 = paramMotionEvent.getY(i);
    float f4 = mInitialTouchY;
    f1 = Math.abs(f1 - f2);
    f4 = Math.abs(f3 - f4);
    if ((f1 < mSlop) && (f4 < mSlop)) {
      return null;
    }
    if ((f1 > f4) && (localLayoutManager.canScrollHorizontally())) {
      return null;
    }
    if ((f4 > f1) && (localLayoutManager.canScrollVertically())) {
      return null;
    }
    paramMotionEvent = findChildView(paramMotionEvent);
    if (paramMotionEvent == null) {
      return null;
    }
    return mRecyclerView.getChildViewHolder(paramMotionEvent);
  }
  
  private void getSelectedDxDy(float[] paramArrayOfFloat)
  {
    if ((mSelectedFlags & 0xC) != 0) {
      paramArrayOfFloat[0] = (mSelectedStartX + mDx - mSelected.itemView.getLeft());
    } else {
      paramArrayOfFloat[0] = mSelected.itemView.getTranslationX();
    }
    if ((mSelectedFlags & 0x3) != 0) {
      paramArrayOfFloat[1] = (mSelectedStartY + mDy - mSelected.itemView.getTop());
    } else {
      paramArrayOfFloat[1] = mSelected.itemView.getTranslationY();
    }
  }
  
  private static boolean hitTest(View paramView, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    boolean bool;
    if ((paramFloat1 >= paramFloat3) && (paramFloat1 <= paramView.getWidth() + paramFloat3) && (paramFloat2 >= paramFloat4) && (paramFloat2 <= paramView.getHeight() + paramFloat4)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void initGestureDetector()
  {
    if (mGestureDetector != null) {
      return;
    }
    mGestureDetector = new GestureDetector(mRecyclerView.getContext(), new ItemTouchHelperGestureListener());
  }
  
  private void releaseVelocityTracker()
  {
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }
  
  private void setupCallbacks()
  {
    mSlop = ViewConfiguration.get(mRecyclerView.getContext()).getScaledTouchSlop();
    mRecyclerView.addItemDecoration(this);
    mRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
    mRecyclerView.addOnChildAttachStateChangeListener(this);
    initGestureDetector();
  }
  
  private int swipeIfNecessary(RecyclerView.ViewHolder paramViewHolder)
  {
    if (mActionState == 2) {
      return 0;
    }
    int i = mCallback.getMovementFlags(mRecyclerView, paramViewHolder);
    int j = (mCallback.convertToAbsoluteDirection(i, mRecyclerView.getLayoutDirection()) & 0xFF00) >> 8;
    if (j == 0) {
      return 0;
    }
    i = (0xFF00 & i) >> 8;
    int k;
    if (Math.abs(mDx) > Math.abs(mDy))
    {
      k = checkHorizontalSwipe(paramViewHolder, j);
      if (k > 0)
      {
        if ((i & k) == 0) {
          return Callback.convertToRelativeDirection(k, mRecyclerView.getLayoutDirection());
        }
        return k;
      }
      j = checkVerticalSwipe(paramViewHolder, j);
      if (j > 0) {
        return j;
      }
    }
    else
    {
      k = checkVerticalSwipe(paramViewHolder, j);
      if (k > 0) {
        return k;
      }
      j = checkHorizontalSwipe(paramViewHolder, j);
      if (j > 0)
      {
        if ((i & j) == 0) {
          return Callback.convertToRelativeDirection(j, mRecyclerView.getLayoutDirection());
        }
        return j;
      }
    }
    return 0;
  }
  
  public void attachToRecyclerView(RecyclerView paramRecyclerView)
  {
    if (mRecyclerView == paramRecyclerView) {
      return;
    }
    if (mRecyclerView != null) {
      destroyCallbacks();
    }
    mRecyclerView = paramRecyclerView;
    if (mRecyclerView != null)
    {
      paramRecyclerView = paramRecyclerView.getResources();
      mSwipeEscapeVelocity = paramRecyclerView.getDimension(17105206);
      mMaxSwipeVelocity = paramRecyclerView.getDimension(17105205);
      setupCallbacks();
    }
  }
  
  boolean checkSelectForSwipe(int paramInt1, MotionEvent paramMotionEvent, int paramInt2)
  {
    if ((mSelected == null) && (paramInt1 == 2) && (mActionState != 2) && (mCallback.isItemViewSwipeEnabled()))
    {
      if (mRecyclerView.getScrollState() == 1) {
        return false;
      }
      RecyclerView.ViewHolder localViewHolder = findSwipedView(paramMotionEvent);
      if (localViewHolder == null) {
        return false;
      }
      paramInt1 = (0xFF00 & mCallback.getAbsoluteMovementFlags(mRecyclerView, localViewHolder)) >> 8;
      if (paramInt1 == 0) {
        return false;
      }
      float f1 = paramMotionEvent.getX(paramInt2);
      float f2 = paramMotionEvent.getY(paramInt2);
      f1 -= mInitialTouchX;
      float f3 = f2 - mInitialTouchY;
      f2 = Math.abs(f1);
      float f4 = Math.abs(f3);
      if ((f2 < mSlop) && (f4 < mSlop)) {
        return false;
      }
      if (f2 > f4)
      {
        if ((f1 < 0.0F) && ((paramInt1 & 0x4) == 0)) {
          return false;
        }
        if ((f1 > 0.0F) && ((paramInt1 & 0x8) == 0)) {
          return false;
        }
      }
      else
      {
        if ((f3 < 0.0F) && ((paramInt1 & 0x1) == 0)) {
          return false;
        }
        if ((f3 > 0.0F) && ((paramInt1 & 0x2) == 0)) {
          return false;
        }
      }
      mDy = 0.0F;
      mDx = 0.0F;
      mActivePointerId = paramMotionEvent.getPointerId(0);
      select(localViewHolder, 1);
      return true;
    }
    return false;
  }
  
  int endRecoverAnimation(RecyclerView.ViewHolder paramViewHolder, boolean paramBoolean)
  {
    for (int i = mRecoverAnimations.size() - 1; i >= 0; i--)
    {
      RecoverAnimation localRecoverAnimation = (RecoverAnimation)mRecoverAnimations.get(i);
      if (mViewHolder == paramViewHolder)
      {
        mOverridden |= paramBoolean;
        if (!mEnded) {
          localRecoverAnimation.cancel();
        }
        mRecoverAnimations.remove(i);
        return mAnimationType;
      }
    }
    return 0;
  }
  
  RecoverAnimation findAnimation(MotionEvent paramMotionEvent)
  {
    if (mRecoverAnimations.isEmpty()) {
      return null;
    }
    paramMotionEvent = findChildView(paramMotionEvent);
    for (int i = mRecoverAnimations.size() - 1; i >= 0; i--)
    {
      RecoverAnimation localRecoverAnimation = (RecoverAnimation)mRecoverAnimations.get(i);
      if (mViewHolder.itemView == paramMotionEvent) {
        return localRecoverAnimation;
      }
    }
    return null;
  }
  
  View findChildView(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    if (mSelected != null)
    {
      paramMotionEvent = mSelected.itemView;
      if (hitTest(paramMotionEvent, f1, f2, mSelectedStartX + mDx, mSelectedStartY + mDy)) {
        return paramMotionEvent;
      }
    }
    for (int i = mRecoverAnimations.size() - 1; i >= 0; i--)
    {
      paramMotionEvent = (RecoverAnimation)mRecoverAnimations.get(i);
      View localView = mViewHolder.itemView;
      if (hitTest(localView, f1, f2, mX, mY)) {
        return localView;
      }
    }
    return mRecyclerView.findChildViewUnder(f1, f2);
  }
  
  public void getItemOffsets(Rect paramRect, View paramView, RecyclerView paramRecyclerView, RecyclerView.State paramState)
  {
    paramRect.setEmpty();
  }
  
  boolean hasRunningRecoverAnim()
  {
    int i = mRecoverAnimations.size();
    for (int j = 0; j < i; j++) {
      if (!mRecoverAnimations.get(j)).mEnded) {
        return true;
      }
    }
    return false;
  }
  
  void moveIfNecessary(RecyclerView.ViewHolder paramViewHolder)
  {
    if (mRecyclerView.isLayoutRequested()) {
      return;
    }
    if (mActionState != 2) {
      return;
    }
    float f = mCallback.getMoveThreshold(paramViewHolder);
    int i = (int)(mSelectedStartX + mDx);
    int j = (int)(mSelectedStartY + mDy);
    if ((Math.abs(j - itemView.getTop()) < itemView.getHeight() * f) && (Math.abs(i - itemView.getLeft()) < itemView.getWidth() * f)) {
      return;
    }
    Object localObject = findSwapTargets(paramViewHolder);
    if (((List)localObject).size() == 0) {
      return;
    }
    localObject = mCallback.chooseDropTarget(paramViewHolder, (List)localObject, i, j);
    if (localObject == null)
    {
      mSwapTargets.clear();
      mDistances.clear();
      return;
    }
    int k = ((RecyclerView.ViewHolder)localObject).getAdapterPosition();
    int m = paramViewHolder.getAdapterPosition();
    if (mCallback.onMove(mRecyclerView, paramViewHolder, (RecyclerView.ViewHolder)localObject)) {
      mCallback.onMoved(mRecyclerView, paramViewHolder, m, (RecyclerView.ViewHolder)localObject, k, i, j);
    }
  }
  
  void obtainVelocityTracker()
  {
    if (mVelocityTracker != null) {
      mVelocityTracker.recycle();
    }
    mVelocityTracker = VelocityTracker.obtain();
  }
  
  public void onChildViewAttachedToWindow(View paramView) {}
  
  public void onChildViewDetachedFromWindow(View paramView)
  {
    removeChildDrawingOrderCallbackIfNecessary(paramView);
    paramView = mRecyclerView.getChildViewHolder(paramView);
    if (paramView == null) {
      return;
    }
    if ((mSelected != null) && (paramView == mSelected))
    {
      select(null, 0);
    }
    else
    {
      endRecoverAnimation(paramView, false);
      if (mPendingCleanup.remove(itemView)) {
        mCallback.clearView(mRecyclerView, paramView);
      }
    }
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
  {
    mOverdrawChildPosition = -1;
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (mSelected != null)
    {
      getSelectedDxDy(mTmpPosition);
      f1 = mTmpPosition[0];
      f2 = mTmpPosition[1];
    }
    mCallback.onDraw(paramCanvas, paramRecyclerView, mSelected, mRecoverAnimations, mActionState, f1, f2);
  }
  
  public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
  {
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (mSelected != null)
    {
      getSelectedDxDy(mTmpPosition);
      f1 = mTmpPosition[0];
      f2 = mTmpPosition[1];
    }
    mCallback.onDrawOver(paramCanvas, paramRecyclerView, mSelected, mRecoverAnimations, mActionState, f1, f2);
  }
  
  void postDispatchSwipe(final RecoverAnimation paramRecoverAnimation, final int paramInt)
  {
    mRecyclerView.post(new Runnable()
    {
      public void run()
      {
        if ((mRecyclerView != null) && (mRecyclerView.isAttachedToWindow()) && (!paramRecoverAnimationmOverridden) && (paramRecoverAnimationmViewHolder.getAdapterPosition() != -1))
        {
          RecyclerView.ItemAnimator localItemAnimator = mRecyclerView.getItemAnimator();
          if (((localItemAnimator == null) || (!localItemAnimator.isRunning(null))) && (!hasRunningRecoverAnim())) {
            mCallback.onSwiped(paramRecoverAnimationmViewHolder, paramInt);
          } else {
            mRecyclerView.post(this);
          }
        }
      }
    });
  }
  
  void removeChildDrawingOrderCallbackIfNecessary(View paramView)
  {
    if (paramView == mOverdrawChild)
    {
      mOverdrawChild = null;
      if (mChildDrawingOrderCallback != null) {
        mRecyclerView.setChildDrawingOrderCallback(null);
      }
    }
  }
  
  boolean scrollIfNecessary()
  {
    if (mSelected == null)
    {
      mDragScrollStartTimeInMs = Long.MIN_VALUE;
      return false;
    }
    long l1 = System.currentTimeMillis();
    if (mDragScrollStartTimeInMs == Long.MIN_VALUE) {}
    for (long l2 = 0L;; l2 = l1 - mDragScrollStartTimeInMs) {
      break;
    }
    RecyclerView.LayoutManager localLayoutManager = mRecyclerView.getLayoutManager();
    if (mTmpRect == null) {
      mTmpRect = new Rect();
    }
    int i = 0;
    int j = 0;
    localLayoutManager.calculateItemDecorationsForChild(mSelected.itemView, mTmpRect);
    int k = i;
    int m;
    if (localLayoutManager.canScrollHorizontally())
    {
      m = (int)(mSelectedStartX + mDx);
      k = m - mTmpRect.left - mRecyclerView.getPaddingLeft();
      if ((mDx >= 0.0F) || (k >= 0))
      {
        k = i;
        if (mDx > 0.0F)
        {
          m = mSelected.itemView.getWidth() + m + mTmpRect.right - (mRecyclerView.getWidth() - mRecyclerView.getPaddingRight());
          k = i;
          if (m > 0) {
            k = m;
          }
        }
      }
    }
    i = j;
    if (localLayoutManager.canScrollVertically())
    {
      m = (int)(mSelectedStartY + mDy);
      i = m - mTmpRect.top - mRecyclerView.getPaddingTop();
      if ((mDy >= 0.0F) || (i >= 0))
      {
        i = j;
        if (mDy > 0.0F)
        {
          m = mSelected.itemView.getHeight() + m + mTmpRect.bottom - (mRecyclerView.getHeight() - mRecyclerView.getPaddingBottom());
          i = j;
          if (m > 0) {
            i = m;
          }
        }
      }
    }
    j = k;
    if (k != 0) {
      j = mCallback.interpolateOutOfBoundsScroll(mRecyclerView, mSelected.itemView.getWidth(), k, mRecyclerView.getWidth(), l2);
    }
    k = i;
    if (i != 0) {
      k = mCallback.interpolateOutOfBoundsScroll(mRecyclerView, mSelected.itemView.getHeight(), i, mRecyclerView.getHeight(), l2);
    }
    if ((j == 0) && (k == 0))
    {
      mDragScrollStartTimeInMs = Long.MIN_VALUE;
      return false;
    }
    if (mDragScrollStartTimeInMs == Long.MIN_VALUE) {
      mDragScrollStartTimeInMs = l1;
    }
    mRecyclerView.scrollBy(j, k);
    return true;
  }
  
  void select(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    if ((paramViewHolder == mSelected) && (paramInt == mActionState)) {
      return;
    }
    mDragScrollStartTimeInMs = Long.MIN_VALUE;
    int i = mActionState;
    endRecoverAnimation(paramViewHolder, true);
    mActionState = paramInt;
    if (paramInt == 2)
    {
      mOverdrawChild = itemView;
      addChildDrawingOrderCallback();
    }
    final int j = 0;
    int k = 0;
    if (mSelected != null)
    {
      Object localObject = mSelected;
      if (itemView.getParent() != null)
      {
        if (i == 2) {
          j = 0;
        } else {
          j = swipeIfNecessary((RecyclerView.ViewHolder)localObject);
        }
        releaseVelocityTracker();
        k = 4;
        float f1;
        float f2;
        if ((j != 4) && (j != 8) && (j != 16) && (j != 32)) {
          switch (j)
          {
          default: 
            f1 = 0.0F;
            f2 = 0.0F;
          }
        }
        for (;;)
        {
          break;
          f1 = 0.0F;
          f2 = Math.signum(mDy) * mRecyclerView.getHeight();
          continue;
          f2 = 0.0F;
          f1 = Math.signum(mDx) * mRecyclerView.getWidth();
        }
        if (i == 2) {}
        for (k = 8;; k = 2)
        {
          break;
          if (j <= 0) {
            break;
          }
        }
        getSelectedDxDy(mTmpPosition);
        float f3 = mTmpPosition[0];
        float f4 = mTmpPosition[1];
        localObject = new RecoverAnimation((RecyclerView.ViewHolder)localObject, k, i, f3, f4, f1, f2, j)
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            super.onAnimationEnd(paramAnonymousAnimator);
            if (mOverridden) {
              return;
            }
            if (j <= 0)
            {
              mCallback.clearView(mRecyclerView, val$prevSelected);
            }
            else
            {
              mPendingCleanup.add(val$prevSelected.itemView);
              mIsPendingCleanup = true;
              if (j > 0) {
                postDispatchSwipe(this, j);
              }
            }
            if (mOverdrawChild == val$prevSelected.itemView) {
              removeChildDrawingOrderCallbackIfNecessary(val$prevSelected.itemView);
            }
          }
        };
        ((RecoverAnimation)localObject).setDuration(mCallback.getAnimationDuration(mRecyclerView, k, f1 - f3, f2 - f4));
        mRecoverAnimations.add(localObject);
        ((RecoverAnimation)localObject).start();
        k = 1;
      }
      else
      {
        removeChildDrawingOrderCallbackIfNecessary(itemView);
        mCallback.clearView(mRecyclerView, (RecyclerView.ViewHolder)localObject);
      }
      mSelected = null;
    }
    else
    {
      k = j;
    }
    if (paramViewHolder != null)
    {
      mSelectedFlags = ((mCallback.getAbsoluteMovementFlags(mRecyclerView, paramViewHolder) & (1 << 8 * paramInt + 8) - 1) >> mActionState * 8);
      mSelectedStartX = itemView.getLeft();
      mSelectedStartY = itemView.getTop();
      mSelected = paramViewHolder;
      if (paramInt == 2) {
        mSelected.itemView.performHapticFeedback(0);
      }
    }
    boolean bool = false;
    paramViewHolder = mRecyclerView.getParent();
    if (paramViewHolder != null)
    {
      if (mSelected != null) {
        bool = true;
      }
      paramViewHolder.requestDisallowInterceptTouchEvent(bool);
    }
    if (k == 0) {
      mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
    }
    mCallback.onSelectedChanged(mSelected, mActionState);
    mRecyclerView.invalidate();
  }
  
  public void startDrag(RecyclerView.ViewHolder paramViewHolder)
  {
    if (!mCallback.hasDragFlag(mRecyclerView, paramViewHolder))
    {
      Log.e("ItemTouchHelper", "Start drag has been called but dragging is not enabled");
      return;
    }
    if (itemView.getParent() != mRecyclerView)
    {
      Log.e("ItemTouchHelper", "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
      return;
    }
    obtainVelocityTracker();
    mDy = 0.0F;
    mDx = 0.0F;
    select(paramViewHolder, 2);
  }
  
  public void startSwipe(RecyclerView.ViewHolder paramViewHolder)
  {
    if (!mCallback.hasSwipeFlag(mRecyclerView, paramViewHolder))
    {
      Log.e("ItemTouchHelper", "Start swipe has been called but swiping is not enabled");
      return;
    }
    if (itemView.getParent() != mRecyclerView)
    {
      Log.e("ItemTouchHelper", "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
      return;
    }
    obtainVelocityTracker();
    mDy = 0.0F;
    mDx = 0.0F;
    select(paramViewHolder, 1);
  }
  
  void updateDxDy(MotionEvent paramMotionEvent, int paramInt1, int paramInt2)
  {
    float f1 = paramMotionEvent.getX(paramInt2);
    float f2 = paramMotionEvent.getY(paramInt2);
    mDx = (f1 - mInitialTouchX);
    mDy = (f2 - mInitialTouchY);
    if ((paramInt1 & 0x4) == 0) {
      mDx = Math.max(0.0F, mDx);
    }
    if ((paramInt1 & 0x8) == 0) {
      mDx = Math.min(0.0F, mDx);
    }
    if ((paramInt1 & 0x1) == 0) {
      mDy = Math.max(0.0F, mDy);
    }
    if ((paramInt1 & 0x2) == 0) {
      mDy = Math.min(0.0F, mDy);
    }
  }
  
  public static abstract class Callback
  {
    private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
    public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
    public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
    private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000L;
    static final int RELATIVE_DIR_FLAGS = 3158064;
    private static final Interpolator sDragScrollInterpolator = new Interpolator()
    {
      public float getInterpolation(float paramAnonymousFloat)
      {
        return paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat;
      }
    };
    private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator()
    {
      public float getInterpolation(float paramAnonymousFloat)
      {
        paramAnonymousFloat -= 1.0F;
        return paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat + 1.0F;
      }
    };
    private static final ItemTouchUIUtil sUICallback = new ItemTouchUIUtilImpl();
    private int mCachedMaxScrollSpeed = -1;
    
    public Callback() {}
    
    public static int convertToRelativeDirection(int paramInt1, int paramInt2)
    {
      int i = paramInt1 & 0xC0C0C;
      if (i == 0) {
        return paramInt1;
      }
      paramInt1 &= i;
      if (paramInt2 == 0) {
        return paramInt1 | i << 2;
      }
      return paramInt1 | i << 1 & 0xFFF3F3F3 | (0xC0C0C & i << 1) << 2;
    }
    
    public static ItemTouchUIUtil getDefaultUIUtil()
    {
      return sUICallback;
    }
    
    private int getMaxDragScroll(RecyclerView paramRecyclerView)
    {
      if (mCachedMaxScrollSpeed == -1) {
        mCachedMaxScrollSpeed = paramRecyclerView.getResources().getDimensionPixelSize(17105204);
      }
      return mCachedMaxScrollSpeed;
    }
    
    public static int makeFlag(int paramInt1, int paramInt2)
    {
      return paramInt2 << paramInt1 * 8;
    }
    
    public static int makeMovementFlags(int paramInt1, int paramInt2)
    {
      return makeFlag(0, paramInt2 | paramInt1) | makeFlag(1, paramInt2) | makeFlag(2, paramInt1);
    }
    
    public boolean canDropOver(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder1, RecyclerView.ViewHolder paramViewHolder2)
    {
      return true;
    }
    
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder paramViewHolder, List<RecyclerView.ViewHolder> paramList, int paramInt1, int paramInt2)
    {
      int i = itemView.getWidth();
      int j = itemView.getHeight();
      Object localObject1 = null;
      int k = -1;
      int m = paramInt1 - itemView.getLeft();
      int n = paramInt2 - itemView.getTop();
      int i1 = paramList.size();
      for (int i2 = 0; i2 < i1; i2++)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)paramList.get(i2);
        Object localObject2 = localObject1;
        int i3 = k;
        int i4;
        if (m > 0)
        {
          i4 = itemView.getRight() - (paramInt1 + i);
          localObject2 = localObject1;
          i3 = k;
          if (i4 < 0)
          {
            localObject2 = localObject1;
            i3 = k;
            if (itemView.getRight() > itemView.getRight())
            {
              i4 = Math.abs(i4);
              localObject2 = localObject1;
              i3 = k;
              if (i4 > k)
              {
                i3 = i4;
                localObject2 = localViewHolder;
              }
            }
          }
        }
        localObject1 = localObject2;
        k = i3;
        if (m < 0)
        {
          i4 = itemView.getLeft() - paramInt1;
          localObject1 = localObject2;
          k = i3;
          if (i4 > 0)
          {
            localObject1 = localObject2;
            k = i3;
            if (itemView.getLeft() < itemView.getLeft())
            {
              i4 = Math.abs(i4);
              localObject1 = localObject2;
              k = i3;
              if (i4 > i3)
              {
                k = i4;
                localObject1 = localViewHolder;
              }
            }
          }
        }
        localObject2 = localObject1;
        i3 = k;
        if (n < 0)
        {
          i4 = itemView.getTop() - paramInt2;
          localObject2 = localObject1;
          i3 = k;
          if (i4 > 0)
          {
            localObject2 = localObject1;
            i3 = k;
            if (itemView.getTop() < itemView.getTop())
            {
              i4 = Math.abs(i4);
              localObject2 = localObject1;
              i3 = k;
              if (i4 > k)
              {
                i3 = i4;
                localObject2 = localViewHolder;
              }
            }
          }
        }
        localObject1 = localObject2;
        k = i3;
        if (n > 0)
        {
          i4 = itemView.getBottom() - (paramInt2 + j);
          localObject1 = localObject2;
          k = i3;
          if (i4 < 0)
          {
            localObject1 = localObject2;
            k = i3;
            if (itemView.getBottom() > itemView.getBottom())
            {
              i4 = Math.abs(i4);
              localObject1 = localObject2;
              k = i3;
              if (i4 > i3)
              {
                k = i4;
                localObject1 = localViewHolder;
              }
            }
          }
        }
      }
      return localObject1;
    }
    
    public void clearView(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder)
    {
      sUICallback.clearView(itemView);
    }
    
    public int convertToAbsoluteDirection(int paramInt1, int paramInt2)
    {
      int i = paramInt1 & 0x303030;
      if (i == 0) {
        return paramInt1;
      }
      paramInt1 &= i;
      if (paramInt2 == 0) {
        return paramInt1 | i >> 2;
      }
      return paramInt1 | i >> 1 & 0xFFCFCFCF | (0x303030 & i >> 1) >> 2;
    }
    
    final int getAbsoluteMovementFlags(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder)
    {
      return convertToAbsoluteDirection(getMovementFlags(paramRecyclerView, paramViewHolder), paramRecyclerView.getLayoutDirection());
    }
    
    public long getAnimationDuration(RecyclerView paramRecyclerView, int paramInt, float paramFloat1, float paramFloat2)
    {
      paramRecyclerView = paramRecyclerView.getItemAnimator();
      long l;
      if (paramRecyclerView == null)
      {
        if (paramInt == 8) {
          l = 200L;
        } else {
          l = 250L;
        }
        return l;
      }
      if (paramInt == 8) {
        l = paramRecyclerView.getMoveDuration();
      } else {
        l = paramRecyclerView.getRemoveDuration();
      }
      return l;
    }
    
    public int getBoundingBoxMargin()
    {
      return 0;
    }
    
    public float getMoveThreshold(RecyclerView.ViewHolder paramViewHolder)
    {
      return 0.5F;
    }
    
    public abstract int getMovementFlags(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder);
    
    public float getSwipeEscapeVelocity(float paramFloat)
    {
      return paramFloat;
    }
    
    public float getSwipeThreshold(RecyclerView.ViewHolder paramViewHolder)
    {
      return 0.5F;
    }
    
    public float getSwipeVelocityThreshold(float paramFloat)
    {
      return paramFloat;
    }
    
    boolean hasDragFlag(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder)
    {
      boolean bool;
      if ((0xFF0000 & getAbsoluteMovementFlags(paramRecyclerView, paramViewHolder)) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    boolean hasSwipeFlag(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder)
    {
      boolean bool;
      if ((0xFF00 & getAbsoluteMovementFlags(paramRecyclerView, paramViewHolder)) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int interpolateOutOfBoundsScroll(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3, long paramLong)
    {
      int i = getMaxDragScroll(paramRecyclerView);
      paramInt3 = Math.abs(paramInt2);
      int j = (int)Math.signum(paramInt2);
      float f = Math.min(1.0F, paramInt3 * 1.0F / paramInt1);
      paramInt1 = (int)(j * i * sDragViewScrollCapInterpolator.getInterpolation(f));
      if (paramLong > 2000L) {
        f = 1.0F;
      } else {
        f = (float)paramLong / 2000.0F;
      }
      paramInt1 = (int)(paramInt1 * sDragScrollInterpolator.getInterpolation(f));
      if (paramInt1 == 0)
      {
        if (paramInt2 > 0) {
          paramInt1 = 1;
        } else {
          paramInt1 = -1;
        }
        return paramInt1;
      }
      return paramInt1;
    }
    
    public boolean isItemViewSwipeEnabled()
    {
      return true;
    }
    
    public boolean isLongPressDragEnabled()
    {
      return true;
    }
    
    public void onChildDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean)
    {
      sUICallback.onDraw(paramCanvas, paramRecyclerView, itemView, paramFloat1, paramFloat2, paramInt, paramBoolean);
    }
    
    public void onChildDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean)
    {
      sUICallback.onDrawOver(paramCanvas, paramRecyclerView, itemView, paramFloat1, paramFloat2, paramInt, paramBoolean);
    }
    
    void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder, List<ItemTouchHelper.RecoverAnimation> paramList, int paramInt, float paramFloat1, float paramFloat2)
    {
      int i = paramList.size();
      for (int j = 0; j < i; j++)
      {
        ItemTouchHelper.RecoverAnimation localRecoverAnimation = (ItemTouchHelper.RecoverAnimation)paramList.get(j);
        localRecoverAnimation.update();
        int k = paramCanvas.save();
        onChildDraw(paramCanvas, paramRecyclerView, mViewHolder, mX, mY, mActionState, false);
        paramCanvas.restoreToCount(k);
      }
      if (paramViewHolder != null)
      {
        j = paramCanvas.save();
        onChildDraw(paramCanvas, paramRecyclerView, paramViewHolder, paramFloat1, paramFloat2, paramInt, true);
        paramCanvas.restoreToCount(j);
      }
    }
    
    void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder, List<ItemTouchHelper.RecoverAnimation> paramList, int paramInt, float paramFloat1, float paramFloat2)
    {
      int i = paramList.size();
      for (int j = 0; j < i; j++)
      {
        ItemTouchHelper.RecoverAnimation localRecoverAnimation = (ItemTouchHelper.RecoverAnimation)paramList.get(j);
        int k = paramCanvas.save();
        onChildDrawOver(paramCanvas, paramRecyclerView, mViewHolder, mX, mY, mActionState, false);
        paramCanvas.restoreToCount(k);
      }
      if (paramViewHolder != null)
      {
        j = paramCanvas.save();
        onChildDrawOver(paramCanvas, paramRecyclerView, paramViewHolder, paramFloat1, paramFloat2, paramInt, true);
        paramCanvas.restoreToCount(j);
      }
      j = 0;
      for (paramInt = i - 1; paramInt >= 0; paramInt--)
      {
        paramCanvas = (ItemTouchHelper.RecoverAnimation)paramList.get(paramInt);
        if ((mEnded) && (!mIsPendingCleanup)) {
          paramList.remove(paramInt);
        } else if (!mEnded) {
          j = 1;
        }
      }
      if (j != 0) {
        paramRecyclerView.invalidate();
      }
    }
    
    public abstract boolean onMove(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder1, RecyclerView.ViewHolder paramViewHolder2);
    
    public void onMoved(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder1, int paramInt1, RecyclerView.ViewHolder paramViewHolder2, int paramInt2, int paramInt3, int paramInt4)
    {
      RecyclerView.LayoutManager localLayoutManager = paramRecyclerView.getLayoutManager();
      if ((localLayoutManager instanceof ItemTouchHelper.ViewDropHandler))
      {
        ((ItemTouchHelper.ViewDropHandler)localLayoutManager).prepareForDrop(itemView, itemView, paramInt3, paramInt4);
        return;
      }
      if (localLayoutManager.canScrollHorizontally())
      {
        if (localLayoutManager.getDecoratedLeft(itemView) <= paramRecyclerView.getPaddingLeft()) {
          paramRecyclerView.scrollToPosition(paramInt2);
        }
        if (localLayoutManager.getDecoratedRight(itemView) >= paramRecyclerView.getWidth() - paramRecyclerView.getPaddingRight()) {
          paramRecyclerView.scrollToPosition(paramInt2);
        }
      }
      if (localLayoutManager.canScrollVertically())
      {
        if (localLayoutManager.getDecoratedTop(itemView) <= paramRecyclerView.getPaddingTop()) {
          paramRecyclerView.scrollToPosition(paramInt2);
        }
        if (localLayoutManager.getDecoratedBottom(itemView) >= paramRecyclerView.getHeight() - paramRecyclerView.getPaddingBottom()) {
          paramRecyclerView.scrollToPosition(paramInt2);
        }
      }
    }
    
    public void onSelectedChanged(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      if (paramViewHolder != null) {
        sUICallback.onSelected(itemView);
      }
    }
    
    public abstract void onSwiped(RecyclerView.ViewHolder paramViewHolder, int paramInt);
  }
  
  private class ItemTouchHelperGestureListener
    extends GestureDetector.SimpleOnGestureListener
  {
    ItemTouchHelperGestureListener() {}
    
    public boolean onDown(MotionEvent paramMotionEvent)
    {
      return true;
    }
    
    public void onLongPress(MotionEvent paramMotionEvent)
    {
      Object localObject = findChildView(paramMotionEvent);
      if (localObject != null)
      {
        localObject = mRecyclerView.getChildViewHolder((View)localObject);
        if (localObject != null)
        {
          if (!mCallback.hasDragFlag(mRecyclerView, (RecyclerView.ViewHolder)localObject)) {
            return;
          }
          if (paramMotionEvent.getPointerId(0) == mActivePointerId)
          {
            int i = paramMotionEvent.findPointerIndex(mActivePointerId);
            float f1 = paramMotionEvent.getX(i);
            float f2 = paramMotionEvent.getY(i);
            mInitialTouchX = f1;
            mInitialTouchY = f2;
            paramMotionEvent = ItemTouchHelper.this;
            mDy = 0.0F;
            mDx = 0.0F;
            if (mCallback.isLongPressDragEnabled()) {
              select((RecyclerView.ViewHolder)localObject, 2);
            }
          }
        }
      }
    }
  }
  
  private class RecoverAnimation
    implements Animator.AnimatorListener
  {
    final int mActionState;
    final int mAnimationType;
    boolean mEnded = false;
    private float mFraction;
    public boolean mIsPendingCleanup;
    boolean mOverridden = false;
    final float mStartDx;
    final float mStartDy;
    final float mTargetX;
    final float mTargetY;
    private final ValueAnimator mValueAnimator;
    final RecyclerView.ViewHolder mViewHolder;
    float mX;
    float mY;
    
    RecoverAnimation(RecyclerView.ViewHolder paramViewHolder, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
      mActionState = paramInt2;
      mAnimationType = paramInt1;
      mViewHolder = paramViewHolder;
      mStartDx = paramFloat1;
      mStartDy = paramFloat2;
      mTargetX = paramFloat3;
      mTargetY = paramFloat4;
      mValueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
      mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          setFraction(paramAnonymousValueAnimator.getAnimatedFraction());
        }
      });
      mValueAnimator.setTarget(itemView);
      mValueAnimator.addListener(this);
      setFraction(0.0F);
    }
    
    public void cancel()
    {
      mValueAnimator.cancel();
    }
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      setFraction(1.0F);
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      if (!mEnded) {
        mViewHolder.setIsRecyclable(true);
      }
      mEnded = true;
    }
    
    public void onAnimationRepeat(Animator paramAnimator) {}
    
    public void onAnimationStart(Animator paramAnimator) {}
    
    public void setDuration(long paramLong)
    {
      mValueAnimator.setDuration(paramLong);
    }
    
    public void setFraction(float paramFloat)
    {
      mFraction = paramFloat;
    }
    
    public void start()
    {
      mViewHolder.setIsRecyclable(false);
      mValueAnimator.start();
    }
    
    public void update()
    {
      if (mStartDx == mTargetX) {
        mX = mViewHolder.itemView.getTranslationX();
      } else {
        mX = (mStartDx + mFraction * (mTargetX - mStartDx));
      }
      if (mStartDy == mTargetY) {
        mY = mViewHolder.itemView.getTranslationY();
      } else {
        mY = (mStartDy + mFraction * (mTargetY - mStartDy));
      }
    }
  }
  
  public static abstract class SimpleCallback
    extends ItemTouchHelper.Callback
  {
    private int mDefaultDragDirs;
    private int mDefaultSwipeDirs;
    
    public SimpleCallback(int paramInt1, int paramInt2)
    {
      mDefaultSwipeDirs = paramInt2;
      mDefaultDragDirs = paramInt1;
    }
    
    public int getDragDirs(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder)
    {
      return mDefaultDragDirs;
    }
    
    public int getMovementFlags(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder)
    {
      return makeMovementFlags(getDragDirs(paramRecyclerView, paramViewHolder), getSwipeDirs(paramRecyclerView, paramViewHolder));
    }
    
    public int getSwipeDirs(RecyclerView paramRecyclerView, RecyclerView.ViewHolder paramViewHolder)
    {
      return mDefaultSwipeDirs;
    }
    
    public void setDefaultDragDirs(int paramInt)
    {
      mDefaultDragDirs = paramInt;
    }
    
    public void setDefaultSwipeDirs(int paramInt)
    {
      mDefaultSwipeDirs = paramInt;
    }
  }
  
  public static abstract interface ViewDropHandler
  {
    public abstract void prepareForDrop(View paramView1, View paramView2, int paramInt1, int paramInt2);
  }
}
