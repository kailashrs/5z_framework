package android.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FocusFinder
{
  private static final ThreadLocal<FocusFinder> tlFocusFinder = new ThreadLocal()
  {
    protected FocusFinder initialValue()
    {
      return new FocusFinder(null);
    }
  };
  final Rect mBestCandidateRect = new Rect();
  private final FocusSorter mFocusSorter = new FocusSorter();
  final Rect mFocusedRect = new Rect();
  final Rect mOtherRect = new Rect();
  private final ArrayList<View> mTempList = new ArrayList();
  private final UserSpecifiedFocusComparator mUserSpecifiedClusterComparator = new UserSpecifiedFocusComparator(_..Lambda.FocusFinder.P8rLvOJhymJH5ALAgUjGaM5gxKA.INSTANCE);
  private final UserSpecifiedFocusComparator mUserSpecifiedFocusComparator = new UserSpecifiedFocusComparator(_..Lambda.FocusFinder.Pgx6IETuqCkrhJYdiBes48tolG4.INSTANCE);
  
  private FocusFinder() {}
  
  private View findNextFocus(ViewGroup paramViewGroup, View paramView, Rect paramRect, int paramInt)
  {
    ArrayList localArrayList = null;
    ViewGroup localViewGroup = getEffectiveRoot(paramViewGroup, paramView);
    paramViewGroup = localArrayList;
    if (paramView != null) {
      paramViewGroup = findNextUserSpecifiedFocus(localViewGroup, paramView, paramInt);
    }
    if (paramViewGroup != null) {
      return paramViewGroup;
    }
    localArrayList = mTempList;
    try
    {
      localArrayList.clear();
      localViewGroup.addFocusables(localArrayList, paramInt);
      if (!localArrayList.isEmpty()) {
        paramViewGroup = findNextFocus(localViewGroup, paramView, paramRect, paramInt, localArrayList);
      }
      return paramViewGroup;
    }
    finally
    {
      localArrayList.clear();
    }
  }
  
  private View findNextFocus(ViewGroup paramViewGroup, View paramView, Rect paramRect, int paramInt, ArrayList<View> paramArrayList)
  {
    if (paramView != null)
    {
      if (paramRect == null) {
        paramRect = mFocusedRect;
      }
      paramView.getFocusedRect(paramRect);
      paramViewGroup.offsetDescendantRectToMyCoords(paramView, paramRect);
    }
    for (;;)
    {
      break;
      if (paramRect != null) {
        break;
      }
      paramRect = mFocusedRect;
      if ((paramInt != 17) && (paramInt != 33)) {
        if ((paramInt == 66) || (paramInt == 130)) {}
      }
      switch (paramInt)
      {
      default: 
        break;
      case 2: 
        if (paramViewGroup.isLayoutRtl()) {
          setFocusBottomRight(paramViewGroup, paramRect);
        } else {
          setFocusTopLeft(paramViewGroup, paramRect);
        }
        break;
      case 1: 
        if (paramViewGroup.isLayoutRtl())
        {
          setFocusTopLeft(paramViewGroup, paramRect);
        }
        else
        {
          setFocusBottomRight(paramViewGroup, paramRect);
          continue;
          setFocusTopLeft(paramViewGroup, paramRect);
          continue;
          setFocusBottomRight(paramViewGroup, paramRect);
        }
        break;
      }
    }
    if ((paramInt != 17) && (paramInt != 33) && (paramInt != 66) && (paramInt != 130))
    {
      switch (paramInt)
      {
      default: 
        paramViewGroup = new StringBuilder();
        paramViewGroup.append("Unknown direction: ");
        paramViewGroup.append(paramInt);
        throw new IllegalArgumentException(paramViewGroup.toString());
      }
      return findNextFocusInRelativeDirection(paramArrayList, paramViewGroup, paramView, paramRect, paramInt);
    }
    return findNextFocusInAbsoluteDirection(paramArrayList, paramViewGroup, paramView, paramRect, paramInt);
  }
  
  private View findNextFocusInRelativeDirection(ArrayList<View> paramArrayList, ViewGroup paramViewGroup, View paramView, Rect paramRect, int paramInt)
  {
    try
    {
      mUserSpecifiedFocusComparator.setFocusables(paramArrayList, paramViewGroup);
      Collections.sort(paramArrayList, mUserSpecifiedFocusComparator);
      mUserSpecifiedFocusComparator.recycle();
      int i = paramArrayList.size();
      switch (paramInt)
      {
      default: 
        return (View)paramArrayList.get(i - 1);
      case 2: 
        return getNextFocusable(paramView, paramArrayList, i);
      }
      return getPreviousFocusable(paramView, paramArrayList, i);
    }
    finally
    {
      mUserSpecifiedFocusComparator.recycle();
    }
  }
  
  private View findNextKeyboardNavigationCluster(View paramView1, View paramView2, List<View> paramList, int paramInt)
  {
    try
    {
      mUserSpecifiedClusterComparator.setFocusables(paramList, paramView1);
      Collections.sort(paramList, mUserSpecifiedClusterComparator);
      mUserSpecifiedClusterComparator.recycle();
      int i = paramList.size();
      if ((paramInt != 17) && (paramInt != 33))
      {
        if ((paramInt != 66) && (paramInt != 130)) {}
        switch (paramInt)
        {
        default: 
          paramView1 = new StringBuilder();
          paramView1.append("Unknown direction: ");
          paramView1.append(paramInt);
          throw new IllegalArgumentException(paramView1.toString());
        case 2: 
          return getNextKeyboardNavigationCluster(paramView1, paramView2, paramList, i);
        }
      }
      return getPreviousKeyboardNavigationCluster(paramView1, paramView2, paramList, i);
    }
    finally
    {
      mUserSpecifiedClusterComparator.recycle();
    }
  }
  
  private View findNextUserSpecifiedFocus(ViewGroup paramViewGroup, View paramView, int paramInt)
  {
    Object localObject = paramView.findUserSetNextFocus(paramViewGroup, paramInt);
    paramView = (View)localObject;
    int i = 1;
    View localView1;
    View localView2;
    do
    {
      int j;
      int k;
      do
      {
        if (localObject == null) {
          break;
        }
        if ((((View)localObject).isFocusable()) && (((View)localObject).getVisibility() == 0) && ((!((View)localObject).isInTouchMode()) || (((View)localObject).isFocusableInTouchMode()))) {
          return localObject;
        }
        localView1 = ((View)localObject).findUserSetNextFocus(paramViewGroup, paramInt);
        if (i == 0) {
          j = 1;
        } else {
          j = 0;
        }
        k = j;
        i = k;
        localObject = localView1;
      } while (j == 0);
      localView2 = paramView.findUserSetNextFocus(paramViewGroup, paramInt);
      i = k;
      localObject = localView1;
      paramView = localView2;
    } while (localView2 != localView1);
    return null;
  }
  
  private View findNextUserSpecifiedKeyboardNavigationCluster(View paramView1, View paramView2, int paramInt)
  {
    paramView1 = paramView2.findUserSetNextKeyboardNavigationCluster(paramView1, paramInt);
    if ((paramView1 != null) && (paramView1.hasFocusable())) {
      return paramView1;
    }
    return null;
  }
  
  private ViewGroup getEffectiveRoot(ViewGroup paramViewGroup, View paramView)
  {
    if ((paramView != null) && (paramView != paramViewGroup))
    {
      Object localObject1 = null;
      Object localObject2 = paramView.getParent();
      Object localObject3;
      do
      {
        if (localObject2 == paramViewGroup)
        {
          if (localObject1 == null) {
            localObject1 = paramViewGroup;
          }
          return localObject1;
        }
        localObject3 = (ViewGroup)localObject2;
        Object localObject4 = localObject1;
        if (((ViewGroup)localObject3).getTouchscreenBlocksFocus())
        {
          localObject4 = localObject1;
          if (paramView.getContext().getPackageManager().hasSystemFeature("android.hardware.touchscreen"))
          {
            localObject4 = localObject1;
            if (((ViewGroup)localObject3).isKeyboardNavigationCluster()) {
              localObject4 = localObject3;
            }
          }
        }
        localObject3 = ((ViewParent)localObject2).getParent();
        localObject1 = localObject4;
        localObject2 = localObject3;
      } while ((localObject3 instanceof ViewGroup));
      return paramViewGroup;
    }
    return paramViewGroup;
  }
  
  public static FocusFinder getInstance()
  {
    return (FocusFinder)tlFocusFinder.get();
  }
  
  private static View getNextFocusable(View paramView, ArrayList<View> paramArrayList, int paramInt)
  {
    if (paramView != null)
    {
      int i = paramArrayList.lastIndexOf(paramView);
      if ((i >= 0) && (i + 1 < paramInt)) {
        return (View)paramArrayList.get(i + 1);
      }
    }
    if (!paramArrayList.isEmpty()) {
      return (View)paramArrayList.get(0);
    }
    return null;
  }
  
  private static View getNextKeyboardNavigationCluster(View paramView1, View paramView2, List<View> paramList, int paramInt)
  {
    if (paramView2 == null) {
      return (View)paramList.get(0);
    }
    int i = paramList.lastIndexOf(paramView2);
    if ((i >= 0) && (i + 1 < paramInt)) {
      return (View)paramList.get(i + 1);
    }
    return paramView1;
  }
  
  private static View getPreviousFocusable(View paramView, ArrayList<View> paramArrayList, int paramInt)
  {
    if (paramView != null)
    {
      int i = paramArrayList.indexOf(paramView);
      if (i > 0) {
        return (View)paramArrayList.get(i - 1);
      }
    }
    if (!paramArrayList.isEmpty()) {
      return (View)paramArrayList.get(paramInt - 1);
    }
    return null;
  }
  
  private static View getPreviousKeyboardNavigationCluster(View paramView1, View paramView2, List<View> paramList, int paramInt)
  {
    if (paramView2 == null) {
      return (View)paramList.get(paramInt - 1);
    }
    paramInt = paramList.indexOf(paramView2);
    if (paramInt > 0) {
      return (View)paramList.get(paramInt - 1);
    }
    return paramView1;
  }
  
  private boolean isTouchCandidate(int paramInt1, int paramInt2, Rect paramRect, int paramInt3)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    if (paramInt3 != 17)
    {
      if (paramInt3 != 33)
      {
        if (paramInt3 != 66)
        {
          if (paramInt3 == 130)
          {
            bool5 = bool4;
            if (top >= paramInt2)
            {
              bool5 = bool4;
              if (left <= paramInt1)
              {
                bool5 = bool4;
                if (paramInt1 <= right) {
                  bool5 = true;
                }
              }
            }
            return bool5;
          }
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        bool5 = bool1;
        if (left >= paramInt1)
        {
          bool5 = bool1;
          if (top <= paramInt2)
          {
            bool5 = bool1;
            if (paramInt2 <= bottom) {
              bool5 = true;
            }
          }
        }
        return bool5;
      }
      bool5 = bool2;
      if (top <= paramInt2)
      {
        bool5 = bool2;
        if (left <= paramInt1)
        {
          bool5 = bool2;
          if (paramInt1 <= right) {
            bool5 = true;
          }
        }
      }
      return bool5;
    }
    boolean bool5 = bool3;
    if (left <= paramInt1)
    {
      bool5 = bool3;
      if (top <= paramInt2)
      {
        bool5 = bool3;
        if (paramInt2 <= bottom) {
          bool5 = true;
        }
      }
    }
    return bool5;
  }
  
  private static final boolean isValidId(int paramInt)
  {
    boolean bool;
    if ((paramInt != 0) && (paramInt != -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  static int majorAxisDistance(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    return Math.max(0, majorAxisDistanceRaw(paramInt, paramRect1, paramRect2));
  }
  
  static int majorAxisDistanceRaw(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt == 130) {
            return top - bottom;
          }
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        return left - right;
      }
      return top - bottom;
    }
    return left - right;
  }
  
  static int majorAxisDistanceToFarEdge(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    return Math.max(1, majorAxisDistanceToFarEdgeRaw(paramInt, paramRect1, paramRect2));
  }
  
  static int majorAxisDistanceToFarEdgeRaw(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt == 130) {
            return bottom - bottom;
          }
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        return right - right;
      }
      return top - top;
    }
    return left - left;
  }
  
  static int minorAxisDistance(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt == 66) {
          break label66;
        }
        if (paramInt != 130) {
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
      }
      return Math.abs(left + paramRect1.width() / 2 - (left + paramRect2.width() / 2));
    }
    label66:
    return Math.abs(top + paramRect1.height() / 2 - (top + paramRect2.height() / 2));
  }
  
  private void setFocusBottomRight(ViewGroup paramViewGroup, Rect paramRect)
  {
    int i = paramViewGroup.getScrollY() + paramViewGroup.getHeight();
    int j = paramViewGroup.getScrollX() + paramViewGroup.getWidth();
    paramRect.set(j, i, j, i);
  }
  
  private void setFocusTopLeft(ViewGroup paramViewGroup, Rect paramRect)
  {
    int i = paramViewGroup.getScrollY();
    int j = paramViewGroup.getScrollX();
    paramRect.set(j, i, j, i);
  }
  
  public static void sort(View[] paramArrayOfView, int paramInt1, int paramInt2, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    getInstancemFocusSorter.sort(paramArrayOfView, paramInt1, paramInt2, paramViewGroup, paramBoolean);
  }
  
  boolean beamBeats(int paramInt, Rect paramRect1, Rect paramRect2, Rect paramRect3)
  {
    boolean bool1 = beamsOverlap(paramInt, paramRect1, paramRect2);
    boolean bool2 = beamsOverlap(paramInt, paramRect1, paramRect3);
    boolean bool3 = false;
    if ((!bool2) && (bool1))
    {
      if (!isToDirectionOf(paramInt, paramRect1, paramRect3)) {
        return true;
      }
      if ((paramInt != 17) && (paramInt != 66))
      {
        if (majorAxisDistance(paramInt, paramRect1, paramRect2) < majorAxisDistanceToFarEdge(paramInt, paramRect1, paramRect3)) {
          bool3 = true;
        }
        return bool3;
      }
      return true;
    }
    return false;
  }
  
  boolean beamsOverlap(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt == 66) {
          break label81;
        }
        if (paramInt != 130) {
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
      }
      bool3 = bool2;
      if (right > left)
      {
        bool3 = bool2;
        if (left < right) {
          bool3 = true;
        }
      }
      return bool3;
    }
    label81:
    boolean bool3 = bool1;
    if (bottom > top)
    {
      bool3 = bool1;
      if (top < bottom) {
        bool3 = true;
      }
    }
    return bool3;
  }
  
  public View findNearestTouchable(ViewGroup paramViewGroup, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
  {
    ArrayList localArrayList = paramViewGroup.getTouchables();
    Object localObject1 = null;
    int i = localArrayList.size();
    int j = ViewConfiguration.get(mContext).getScaledEdgeSlop();
    Rect localRect1 = new Rect();
    Rect localRect2 = mOtherRect;
    int k = Integer.MAX_VALUE;
    for (int m = 0; m < i; m++)
    {
      View localView = (View)localArrayList.get(m);
      localView.getDrawingRect(localRect2);
      paramViewGroup.offsetRectBetweenParentAndChild(localView, localRect2, true, true);
      Object localObject2;
      int n;
      int i1;
      if (!isTouchCandidate(paramInt1, paramInt2, localRect2, paramInt3))
      {
        localObject2 = localObject1;
        n = k;
      }
      else
      {
        i1 = Integer.MAX_VALUE;
        if (paramInt3 != 17) {
          if (paramInt3 != 33) {
            if (paramInt3 != 66) {
              if (paramInt3 != 130) {
                break label196;
              }
            }
          }
        }
        for (;;)
        {
          i1 = top;
          break;
          i1 = left;
          break;
          i1 = paramInt2 - bottom + 1;
          continue;
          i1 = paramInt1 - right + 1;
        }
        label196:
        localObject2 = localObject1;
        n = k;
        if (i1 < j) {
          if ((localObject1 != null) && (!localRect1.contains(localRect2)))
          {
            localObject2 = localObject1;
            n = k;
            if (!localRect2.contains(localRect1))
            {
              localObject2 = localObject1;
              n = k;
              if (i1 >= k) {}
            }
          }
          else
          {
            k = i1;
            localObject1 = localView;
            localRect1.set(localRect2);
            if (paramInt3 == 17) {
              break label358;
            }
            if (paramInt3 == 33) {
              break label348;
            }
            if (paramInt3 == 66) {
              break label339;
            }
            if (paramInt3 == 130) {
              break label322;
            }
            n = k;
            localObject2 = localObject1;
          }
        }
      }
      for (;;)
      {
        localObject1 = localObject2;
        k = n;
        break;
        label322:
        paramArrayOfInt[1] = i1;
        localObject2 = localObject1;
        n = k;
      }
      label339:
      paramArrayOfInt[0] = i1;
      continue;
      label348:
      paramArrayOfInt[1] = (-i1);
      continue;
      label358:
      paramArrayOfInt[0] = (-i1);
    }
    return localObject1;
  }
  
  public final View findNextFocus(ViewGroup paramViewGroup, View paramView, int paramInt)
  {
    return findNextFocus(paramViewGroup, paramView, null, paramInt);
  }
  
  public View findNextFocusFromRect(ViewGroup paramViewGroup, Rect paramRect, int paramInt)
  {
    mFocusedRect.set(paramRect);
    return findNextFocus(paramViewGroup, null, mFocusedRect, paramInt);
  }
  
  View findNextFocusInAbsoluteDirection(ArrayList<View> paramArrayList, ViewGroup paramViewGroup, View paramView, Rect paramRect, int paramInt)
  {
    mBestCandidateRect.set(paramRect);
    int i = 0;
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt == 130) {
            mBestCandidateRect.offset(0, -(paramRect.height() + 1));
          }
        }
        else {
          mBestCandidateRect.offset(-(paramRect.width() + 1), 0);
        }
      }
      else {
        mBestCandidateRect.offset(0, paramRect.height() + 1);
      }
    }
    else {
      mBestCandidateRect.offset(paramRect.width() + 1, 0);
    }
    Object localObject1 = null;
    int j = paramArrayList.size();
    while (i < j)
    {
      View localView = (View)paramArrayList.get(i);
      Object localObject2 = localObject1;
      if (localView != paramView) {
        if (localView == paramViewGroup)
        {
          localObject2 = localObject1;
        }
        else
        {
          localView.getFocusedRect(mOtherRect);
          paramViewGroup.offsetDescendantRectToMyCoords(localView, mOtherRect);
          localObject2 = localObject1;
          if (isBetterCandidate(paramInt, paramRect, mOtherRect, mBestCandidateRect))
          {
            mBestCandidateRect.set(mOtherRect);
            localObject2 = localView;
          }
        }
      }
      i++;
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public View findNextKeyboardNavigationCluster(View paramView1, View paramView2, int paramInt)
  {
    Object localObject1 = null;
    if (paramView2 != null)
    {
      localObject2 = findNextUserSpecifiedKeyboardNavigationCluster(paramView1, paramView2, paramInt);
      localObject1 = localObject2;
      if (localObject2 != null) {
        return localObject2;
      }
    }
    Object localObject2 = mTempList;
    try
    {
      ((ArrayList)localObject2).clear();
      paramView1.addKeyboardNavigationClusters((Collection)localObject2, paramInt);
      if (!((ArrayList)localObject2).isEmpty()) {
        localObject1 = findNextKeyboardNavigationCluster(paramView1, paramView2, (List)localObject2, paramInt);
      }
      return localObject1;
    }
    finally
    {
      ((ArrayList)localObject2).clear();
    }
  }
  
  long getWeightedDistanceFor(long paramLong1, long paramLong2)
  {
    return 13L * paramLong1 * paramLong1 + paramLong2 * paramLong2;
  }
  
  boolean isBetterCandidate(int paramInt, Rect paramRect1, Rect paramRect2, Rect paramRect3)
  {
    boolean bool1 = isCandidate(paramRect1, paramRect2, paramInt);
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if (!isCandidate(paramRect1, paramRect3, paramInt)) {
      return true;
    }
    if (beamBeats(paramInt, paramRect1, paramRect2, paramRect3)) {
      return true;
    }
    if (beamBeats(paramInt, paramRect1, paramRect3, paramRect2)) {
      return false;
    }
    if (getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect2), minorAxisDistance(paramInt, paramRect1, paramRect2)) < getWeightedDistanceFor(majorAxisDistance(paramInt, paramRect1, paramRect3), minorAxisDistance(paramInt, paramRect1, paramRect3))) {
      bool2 = true;
    }
    return bool2;
  }
  
  boolean isCandidate(Rect paramRect1, Rect paramRect2, int paramInt)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5;
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt == 130)
          {
            if (top >= top)
            {
              bool5 = bool4;
              if (bottom > top) {}
            }
            else
            {
              bool5 = bool4;
              if (bottom < bottom) {
                bool5 = true;
              }
            }
            return bool5;
          }
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        if (left >= left)
        {
          bool5 = bool1;
          if (right > left) {}
        }
        else
        {
          bool5 = bool1;
          if (right < right) {
            bool5 = true;
          }
        }
        return bool5;
      }
      if (bottom <= bottom)
      {
        bool5 = bool2;
        if (top < bottom) {}
      }
      else
      {
        bool5 = bool2;
        if (top > top) {
          bool5 = true;
        }
      }
      return bool5;
    }
    if (right <= right)
    {
      bool5 = bool3;
      if (left < right) {}
    }
    else
    {
      bool5 = bool3;
      if (left > left) {
        bool5 = true;
      }
    }
    return bool5;
  }
  
  boolean isToDirectionOf(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt == 130)
          {
            if (bottom <= top) {
              bool4 = true;
            }
            return bool4;
          }
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        bool4 = bool1;
        if (right <= left) {
          bool4 = true;
        }
        return bool4;
      }
      bool4 = bool2;
      if (top >= bottom) {
        bool4 = true;
      }
      return bool4;
    }
    bool4 = bool3;
    if (left >= right) {
      bool4 = true;
    }
    return bool4;
  }
  
  static final class FocusSorter
  {
    private int mLastPoolRect;
    private HashMap<View, Rect> mRectByView = null;
    private ArrayList<Rect> mRectPool = new ArrayList();
    private int mRtlMult;
    private Comparator<View> mSidesComparator = new _..Lambda.FocusFinder.FocusSorter.h0f2ZYL6peSaaEeCCkAoYs_YZvU(this);
    private Comparator<View> mTopsComparator = new _..Lambda.FocusFinder.FocusSorter.kW7K1t9q7Y62V38r_7g6xRzqqq8(this);
    
    FocusSorter() {}
    
    public void sort(View[] paramArrayOfView, int paramInt1, int paramInt2, ViewGroup paramViewGroup, boolean paramBoolean)
    {
      int i = paramInt2 - paramInt1;
      if (i < 2) {
        return;
      }
      if (mRectByView == null) {
        mRectByView = new HashMap();
      }
      if (paramBoolean) {
        j = -1;
      } else {
        j = 1;
      }
      mRtlMult = j;
      for (int j = mRectPool.size(); j < i; j++) {
        mRectPool.add(new Rect());
      }
      for (j = paramInt1; j < paramInt2; j++)
      {
        Object localObject = mRectPool;
        k = mLastPoolRect;
        mLastPoolRect = (k + 1);
        localObject = (Rect)((ArrayList)localObject).get(k);
        paramArrayOfView[j].getDrawingRect((Rect)localObject);
        paramViewGroup.offsetDescendantRectToMyCoords(paramArrayOfView[j], (Rect)localObject);
        mRectByView.put(paramArrayOfView[j], localObject);
      }
      Arrays.sort(paramArrayOfView, paramInt1, i, mTopsComparator);
      int k = mRectByView.get(paramArrayOfView[paramInt1])).bottom;
      j = paramInt1;
      paramInt1++;
      while (paramInt1 < paramInt2)
      {
        paramViewGroup = (Rect)mRectByView.get(paramArrayOfView[paramInt1]);
        if (top >= k)
        {
          if (paramInt1 - j > 1) {
            Arrays.sort(paramArrayOfView, j, paramInt1, mSidesComparator);
          }
          j = bottom;
          i = paramInt1;
        }
        else
        {
          k = Math.max(k, bottom);
          i = j;
          j = k;
        }
        paramInt1++;
        k = j;
        j = i;
      }
      if (paramInt1 - j > 1) {
        Arrays.sort(paramArrayOfView, j, paramInt1, mSidesComparator);
      }
      mLastPoolRect = 0;
      mRectByView.clear();
    }
  }
  
  private static final class UserSpecifiedFocusComparator
    implements Comparator<View>
  {
    private final ArrayMap<View, View> mHeadsOfChains = new ArrayMap();
    private final ArraySet<View> mIsConnectedTo = new ArraySet();
    private final ArrayMap<View, View> mNextFoci = new ArrayMap();
    private final NextFocusGetter mNextFocusGetter;
    private final ArrayMap<View, Integer> mOriginalOrdinal = new ArrayMap();
    private View mRoot;
    
    UserSpecifiedFocusComparator(NextFocusGetter paramNextFocusGetter)
    {
      mNextFocusGetter = paramNextFocusGetter;
    }
    
    private void setHeadOfChain(View paramView)
    {
      Object localObject = paramView;
      View localView1 = paramView;
      for (paramView = (View)localObject; localView1 != null; paramView = (View)localObject)
      {
        View localView2 = (View)mHeadsOfChains.get(localView1);
        localObject = paramView;
        if (localView2 != null)
        {
          if (localView2 == paramView) {
            return;
          }
          localObject = localView2;
          localView1 = paramView;
        }
        mHeadsOfChains.put(localView1, localObject);
        localView1 = (View)mNextFoci.get(localView1);
      }
    }
    
    public int compare(View paramView1, View paramView2)
    {
      if (paramView1 == paramView2) {
        return 0;
      }
      View localView1 = (View)mHeadsOfChains.get(paramView1);
      View localView2 = (View)mHeadsOfChains.get(paramView2);
      int i = 1;
      if ((localView1 == localView2) && (localView1 != null))
      {
        if (paramView1 == localView1) {
          return -1;
        }
        if (paramView2 == localView1) {
          return 1;
        }
        if (mNextFoci.get(paramView1) != null) {
          return -1;
        }
        return 1;
      }
      int j = 0;
      if (localView1 != null)
      {
        paramView1 = localView1;
        j = 1;
      }
      if (localView2 != null)
      {
        paramView2 = localView2;
        j = 1;
      }
      if (j != 0)
      {
        j = i;
        if (((Integer)mOriginalOrdinal.get(paramView1)).intValue() < ((Integer)mOriginalOrdinal.get(paramView2)).intValue()) {
          j = -1;
        }
        return j;
      }
      return 0;
    }
    
    public void recycle()
    {
      mRoot = null;
      mHeadsOfChains.clear();
      mIsConnectedTo.clear();
      mOriginalOrdinal.clear();
      mNextFoci.clear();
    }
    
    public void setFocusables(List<View> paramList, View paramView)
    {
      mRoot = paramView;
      for (int i = 0; i < paramList.size(); i++) {
        mOriginalOrdinal.put((View)paramList.get(i), Integer.valueOf(i));
      }
      for (i = paramList.size() - 1; i >= 0; i--)
      {
        paramView = (View)paramList.get(i);
        View localView = mNextFocusGetter.get(mRoot, paramView);
        if ((localView != null) && (mOriginalOrdinal.containsKey(localView)))
        {
          mNextFoci.put(paramView, localView);
          mIsConnectedTo.add(localView);
        }
      }
      for (i = paramList.size() - 1; i >= 0; i--)
      {
        paramView = (View)paramList.get(i);
        if (((View)mNextFoci.get(paramView) != null) && (!mIsConnectedTo.contains(paramView))) {
          setHeadOfChain(paramView);
        }
      }
    }
    
    public static abstract interface NextFocusGetter
    {
      public abstract View get(View paramView1, View paramView2);
    }
  }
}
