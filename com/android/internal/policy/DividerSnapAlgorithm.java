package com.android.internal.policy;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DisplayInfo;
import java.util.ArrayList;

public class DividerSnapAlgorithm
{
  private static final int MIN_DISMISS_VELOCITY_DP_PER_SECOND = 600;
  private static final int MIN_FLING_VELOCITY_DP_PER_SECOND = 400;
  private static final int SNAP_FIXED_RATIO = 1;
  private static final int SNAP_MODE_16_9 = 0;
  private static final int SNAP_MODE_MINIMIZED = 3;
  private static final int SNAP_ONLY_1_1 = 2;
  private final SnapTarget mDismissEndTarget;
  private final SnapTarget mDismissStartTarget;
  private final int mDisplayHeight;
  private final int mDisplayWidth;
  private final int mDividerSize;
  private final SnapTarget mFirstSplitTarget;
  private final float mFixedRatio;
  private final Rect mInsets = new Rect();
  private boolean mIsHorizontalDivision;
  private final SnapTarget mLastSplitTarget;
  private final SnapTarget mMiddleTarget;
  private final float mMinDismissVelocityPxPerSecond;
  private final float mMinFlingVelocityPxPerSecond;
  private final int mMinimalSizeResizableTask;
  private final int mSnapMode;
  private final ArrayList<SnapTarget> mTargets = new ArrayList();
  private final int mTaskHeightInMinimizedMode;
  
  public DividerSnapAlgorithm(Resources paramResources, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, Rect paramRect)
  {
    this(paramResources, paramInt1, paramInt2, paramInt3, paramBoolean, paramRect, -1, false);
  }
  
  public DividerSnapAlgorithm(Resources paramResources, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, Rect paramRect, int paramInt4)
  {
    this(paramResources, paramInt1, paramInt2, paramInt3, paramBoolean, paramRect, paramInt4, false);
  }
  
  public DividerSnapAlgorithm(Resources paramResources, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, Rect paramRect, int paramInt4, boolean paramBoolean2)
  {
    mMinFlingVelocityPxPerSecond = (400.0F * getDisplayMetricsdensity);
    mMinDismissVelocityPxPerSecond = (600.0F * getDisplayMetricsdensity);
    mDividerSize = paramInt3;
    mDisplayWidth = paramInt1;
    mDisplayHeight = paramInt2;
    mIsHorizontalDivision = paramBoolean1;
    mInsets.set(paramRect);
    if (paramBoolean2) {
      paramInt1 = 3;
    } else {
      paramInt1 = paramResources.getInteger(17694780);
    }
    mSnapMode = paramInt1;
    mFixedRatio = paramResources.getFraction(18022404, 1, 1);
    mMinimalSizeResizableTask = paramResources.getDimensionPixelSize(17105133);
    mTaskHeightInMinimizedMode = paramResources.getDimensionPixelSize(17105435);
    calculateTargets(paramBoolean1, paramInt4);
    mFirstSplitTarget = ((SnapTarget)mTargets.get(1));
    mLastSplitTarget = ((SnapTarget)mTargets.get(mTargets.size() - 2));
    mDismissStartTarget = ((SnapTarget)mTargets.get(0));
    mDismissEndTarget = ((SnapTarget)mTargets.get(mTargets.size() - 1));
    mMiddleTarget = ((SnapTarget)mTargets.get(mTargets.size() / 2));
  }
  
  private void addFixedDivisionTargets(boolean paramBoolean, int paramInt)
  {
    int i;
    if (paramBoolean) {
      i = mInsets.top;
    } else {
      i = mInsets.left;
    }
    int j;
    if (paramBoolean) {
      j = mDisplayHeight - mInsets.bottom;
    } else {
      j = mDisplayWidth - mInsets.right;
    }
    int k = (int)(mFixedRatio * (j - i)) - mDividerSize / 2;
    addNonDismissingTargets(paramBoolean, i + k, j - k - mDividerSize, paramInt);
  }
  
  private void addMiddleTarget(boolean paramBoolean)
  {
    int i = DockedDividerUtils.calculateMiddlePosition(paramBoolean, mInsets, mDisplayWidth, mDisplayHeight, mDividerSize);
    mTargets.add(new SnapTarget(i, i, 0));
  }
  
  private void addMinimizedTarget(boolean paramBoolean, int paramInt)
  {
    int i = mTaskHeightInMinimizedMode + mInsets.top;
    int j = i;
    if (!paramBoolean) {
      if (paramInt == 1)
      {
        j = i + mInsets.left;
      }
      else
      {
        j = i;
        if (paramInt == 3) {
          j = mDisplayWidth - i - mInsets.right - mDividerSize;
        }
      }
    }
    mTargets.add(new SnapTarget(j, j, 0));
  }
  
  private void addNonDismissingTargets(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    maybeAddTarget(paramInt1, paramInt1 - mInsets.top);
    addMiddleTarget(paramBoolean);
    maybeAddTarget(paramInt2, paramInt3 - mInsets.bottom - (mDividerSize + paramInt2));
  }
  
  private void addRatio16_9Targets(boolean paramBoolean, int paramInt)
  {
    int i;
    if (paramBoolean) {
      i = mInsets.top;
    } else {
      i = mInsets.left;
    }
    int j;
    if (paramBoolean) {
      j = mDisplayHeight - mInsets.bottom;
    } else {
      j = mDisplayWidth - mInsets.right;
    }
    if (paramBoolean) {
      k = mInsets.left;
    } else {
      k = mInsets.top;
    }
    int m;
    if (paramBoolean) {
      m = mDisplayWidth - mInsets.right;
    } else {
      m = mDisplayHeight - mInsets.bottom;
    }
    int k = (int)Math.floor(0.5625F * (m - k));
    addNonDismissingTargets(paramBoolean, i + k, j - k - mDividerSize, paramInt);
  }
  
  private void calculateTargets(boolean paramBoolean, int paramInt)
  {
    mTargets.clear();
    int i;
    if (paramBoolean) {
      i = mDisplayHeight;
    } else {
      i = mDisplayWidth;
    }
    int j;
    if (paramBoolean) {
      j = mInsets.bottom;
    } else {
      j = mInsets.right;
    }
    int k = -mDividerSize;
    int m = k;
    if (paramInt == 3) {
      m = k + mInsets.left;
    }
    mTargets.add(new SnapTarget(m, m, 1, 0.35F));
    switch (mSnapMode)
    {
    default: 
      break;
    case 3: 
      addMinimizedTarget(paramBoolean, paramInt);
      break;
    case 2: 
      addMiddleTarget(paramBoolean);
      break;
    case 1: 
      addFixedDivisionTargets(paramBoolean, i);
      break;
    case 0: 
      addRatio16_9Targets(paramBoolean, i);
    }
    mTargets.add(new SnapTarget(i - j, i, 2, 0.35F));
  }
  
  public static DividerSnapAlgorithm create(Context paramContext, Rect paramRect)
  {
    DisplayInfo localDisplayInfo = new DisplayInfo();
    ((DisplayManager)paramContext.getSystemService(DisplayManager.class)).getDisplay(0).getDisplayInfo(localDisplayInfo);
    int i = paramContext.getResources().getDimensionPixelSize(17105150);
    int j = paramContext.getResources().getDimensionPixelSize(17105149);
    Resources localResources = paramContext.getResources();
    int k = logicalWidth;
    int m = logicalHeight;
    int n = getApplicationContextgetResourcesgetConfigurationorientation;
    boolean bool = true;
    if (n != 1) {
      bool = false;
    }
    return new DividerSnapAlgorithm(localResources, k, m, i - 2 * j, bool, paramRect);
  }
  
  private int getEndInset()
  {
    if (mIsHorizontalDivision) {
      return mInsets.bottom;
    }
    return mInsets.right;
  }
  
  private int getStartInset()
  {
    if (mIsHorizontalDivision) {
      return mInsets.top;
    }
    return mInsets.left;
  }
  
  private void maybeAddTarget(int paramInt1, int paramInt2)
  {
    if (paramInt2 >= mMinimalSizeResizableTask) {
      mTargets.add(new SnapTarget(paramInt1, paramInt1, 0));
    }
  }
  
  private SnapTarget snap(int paramInt, boolean paramBoolean)
  {
    int i = -1;
    float f1 = Float.MAX_VALUE;
    int j = mTargets.size();
    int k = 0;
    while (k < j)
    {
      SnapTarget localSnapTarget = (SnapTarget)mTargets.get(k);
      float f2 = Math.abs(paramInt - position);
      float f3 = f2;
      if (paramBoolean) {
        f3 = f2 / distanceMultiplier;
      }
      f2 = f1;
      if (f3 < f1)
      {
        i = k;
        f2 = f3;
      }
      k++;
      f1 = f2;
    }
    return (SnapTarget)mTargets.get(i);
  }
  
  public float calculateDismissingFraction(int paramInt)
  {
    if (paramInt < mFirstSplitTarget.position) {
      return 1.0F - (paramInt - getStartInset()) / (mFirstSplitTarget.position - getStartInset());
    }
    if (paramInt > mLastSplitTarget.position) {
      return (paramInt - mLastSplitTarget.position) / (mDismissEndTarget.position - mLastSplitTarget.position - mDividerSize);
    }
    return 0.0F;
  }
  
  public SnapTarget calculateNonDismissingSnapTarget(int paramInt)
  {
    SnapTarget localSnapTarget = snap(paramInt, false);
    if (localSnapTarget == mDismissStartTarget) {
      return mFirstSplitTarget;
    }
    if (localSnapTarget == mDismissEndTarget) {
      return mLastSplitTarget;
    }
    return localSnapTarget;
  }
  
  public SnapTarget calculateSnapTarget(int paramInt, float paramFloat)
  {
    return calculateSnapTarget(paramInt, paramFloat, true);
  }
  
  public SnapTarget calculateSnapTarget(int paramInt, float paramFloat, boolean paramBoolean)
  {
    if ((paramInt < mFirstSplitTarget.position) && (paramFloat < -mMinDismissVelocityPxPerSecond)) {
      return mDismissStartTarget;
    }
    if ((paramInt > mLastSplitTarget.position) && (paramFloat > mMinDismissVelocityPxPerSecond)) {
      return mDismissEndTarget;
    }
    if (Math.abs(paramFloat) < mMinFlingVelocityPxPerSecond) {
      return snap(paramInt, paramBoolean);
    }
    if (paramFloat < 0.0F) {
      return mFirstSplitTarget;
    }
    return mLastSplitTarget;
  }
  
  public SnapTarget cycleNonDismissTarget(SnapTarget paramSnapTarget, int paramInt)
  {
    int i = mTargets.indexOf(paramSnapTarget);
    if (i != -1)
    {
      paramSnapTarget = (SnapTarget)mTargets.get((mTargets.size() + i + paramInt) % mTargets.size());
      if (paramSnapTarget == mDismissStartTarget) {
        return mLastSplitTarget;
      }
      if (paramSnapTarget == mDismissEndTarget) {
        return mFirstSplitTarget;
      }
      return paramSnapTarget;
    }
    return paramSnapTarget;
  }
  
  public SnapTarget getClosestDismissTarget(int paramInt)
  {
    if (paramInt < mFirstSplitTarget.position) {
      return mDismissStartTarget;
    }
    if (paramInt > mLastSplitTarget.position) {
      return mDismissEndTarget;
    }
    if (paramInt - mDismissStartTarget.position < mDismissEndTarget.position - paramInt) {
      return mDismissStartTarget;
    }
    return mDismissEndTarget;
  }
  
  public SnapTarget getDismissEndTarget()
  {
    return mDismissEndTarget;
  }
  
  public SnapTarget getDismissStartTarget()
  {
    return mDismissStartTarget;
  }
  
  public SnapTarget getFirstSplitTarget()
  {
    return mFirstSplitTarget;
  }
  
  public SnapTarget getLastSplitTarget()
  {
    return mLastSplitTarget;
  }
  
  public SnapTarget getMiddleTarget()
  {
    return mMiddleTarget;
  }
  
  public SnapTarget getNextTarget(SnapTarget paramSnapTarget)
  {
    int i = mTargets.indexOf(paramSnapTarget);
    if ((i != -1) && (i < mTargets.size() - 1)) {
      return (SnapTarget)mTargets.get(i + 1);
    }
    return paramSnapTarget;
  }
  
  public SnapTarget getPreviousTarget(SnapTarget paramSnapTarget)
  {
    int i = mTargets.indexOf(paramSnapTarget);
    if ((i != -1) && (i > 0)) {
      return (SnapTarget)mTargets.get(i - 1);
    }
    return paramSnapTarget;
  }
  
  public boolean isFirstSplitTargetAvailable()
  {
    boolean bool;
    if (mFirstSplitTarget != mMiddleTarget) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLastSplitTargetAvailable()
  {
    boolean bool;
    if (mLastSplitTarget != mMiddleTarget) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSplitScreenFeasible()
  {
    int i = mInsets.top;
    int j;
    if (mIsHorizontalDivision) {
      j = mInsets.bottom;
    } else {
      j = mInsets.right;
    }
    int k;
    if (mIsHorizontalDivision) {
      k = mDisplayHeight;
    } else {
      k = mDisplayWidth;
    }
    boolean bool;
    if ((k - j - i - mDividerSize) / 2 >= mMinimalSizeResizableTask) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean showMiddleSplitTargetForAccessibility()
  {
    int i = mTargets.size();
    boolean bool = true;
    if (i - 2 <= 1) {
      bool = false;
    }
    return bool;
  }
  
  public static class SnapTarget
  {
    public static final int FLAG_DISMISS_END = 2;
    public static final int FLAG_DISMISS_START = 1;
    public static final int FLAG_NONE = 0;
    private final float distanceMultiplier;
    public final int flag;
    public final int position;
    public final int taskPosition;
    
    public SnapTarget(int paramInt1, int paramInt2, int paramInt3)
    {
      this(paramInt1, paramInt2, paramInt3, 1.0F);
    }
    
    public SnapTarget(int paramInt1, int paramInt2, int paramInt3, float paramFloat)
    {
      position = paramInt1;
      taskPosition = paramInt2;
      flag = paramInt3;
      distanceMultiplier = paramFloat;
    }
  }
}
