package android.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Pools.SynchronizedPool;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.R.styleable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

@RemoteViews.RemoteView
public class RelativeLayout
  extends ViewGroup
{
  public static final int ABOVE = 2;
  public static final int ALIGN_BASELINE = 4;
  public static final int ALIGN_BOTTOM = 8;
  public static final int ALIGN_END = 19;
  public static final int ALIGN_LEFT = 5;
  public static final int ALIGN_PARENT_BOTTOM = 12;
  public static final int ALIGN_PARENT_END = 21;
  public static final int ALIGN_PARENT_LEFT = 9;
  public static final int ALIGN_PARENT_RIGHT = 11;
  public static final int ALIGN_PARENT_START = 20;
  public static final int ALIGN_PARENT_TOP = 10;
  public static final int ALIGN_RIGHT = 7;
  public static final int ALIGN_START = 18;
  public static final int ALIGN_TOP = 6;
  public static final int BELOW = 3;
  public static final int CENTER_HORIZONTAL = 14;
  public static final int CENTER_IN_PARENT = 13;
  public static final int CENTER_VERTICAL = 15;
  private static final int DEFAULT_WIDTH = 65536;
  public static final int END_OF = 17;
  public static final int LEFT_OF = 0;
  public static final int RIGHT_OF = 1;
  private static final int[] RULES_HORIZONTAL = { 0, 1, 5, 7, 16, 17, 18, 19 };
  private static final int[] RULES_VERTICAL = { 2, 3, 4, 6, 8 };
  public static final int START_OF = 16;
  public static final int TRUE = -1;
  private static final int VALUE_NOT_SET = Integer.MIN_VALUE;
  private static final int VERB_COUNT = 22;
  private boolean mAllowBrokenMeasureSpecs = false;
  private View mBaselineView = null;
  private final Rect mContentBounds = new Rect();
  private boolean mDirtyHierarchy;
  private final DependencyGraph mGraph = new DependencyGraph(null);
  private int mGravity = 8388659;
  private int mIgnoreGravity;
  private boolean mMeasureVerticalWithPaddingMargin = false;
  private final Rect mSelfBounds = new Rect();
  private View[] mSortedHorizontalChildren;
  private View[] mSortedVerticalChildren;
  private SortedSet<View> mTopToBottomLeftToRightSet = null;
  
  public RelativeLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public RelativeLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public RelativeLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public RelativeLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initFromAttributes(paramContext, paramAttributeSet, paramInt1, paramInt2);
    queryCompatibilityModes(paramContext);
  }
  
  private void applyHorizontalSizeRules(LayoutParams paramLayoutParams, int paramInt, int[] paramArrayOfInt)
  {
    LayoutParams.access$102(paramLayoutParams, Integer.MIN_VALUE);
    LayoutParams.access$202(paramLayoutParams, Integer.MIN_VALUE);
    LayoutParams localLayoutParams = getRelatedViewParams(paramArrayOfInt, 0);
    if (localLayoutParams != null) {
      LayoutParams.access$202(paramLayoutParams, mLeft - (leftMargin + rightMargin));
    } else if ((alignWithParent) && (paramArrayOfInt[0] != 0) && (paramInt >= 0)) {
      LayoutParams.access$202(paramLayoutParams, paramInt - mPaddingRight - rightMargin);
    }
    localLayoutParams = getRelatedViewParams(paramArrayOfInt, 1);
    if (localLayoutParams != null) {
      LayoutParams.access$102(paramLayoutParams, mRight + (rightMargin + leftMargin));
    } else if ((alignWithParent) && (paramArrayOfInt[1] != 0)) {
      LayoutParams.access$102(paramLayoutParams, mPaddingLeft + leftMargin);
    }
    localLayoutParams = getRelatedViewParams(paramArrayOfInt, 5);
    if (localLayoutParams != null) {
      LayoutParams.access$102(paramLayoutParams, mLeft + leftMargin);
    } else if ((alignWithParent) && (paramArrayOfInt[5] != 0)) {
      LayoutParams.access$102(paramLayoutParams, mPaddingLeft + leftMargin);
    }
    localLayoutParams = getRelatedViewParams(paramArrayOfInt, 7);
    if (localLayoutParams != null) {
      LayoutParams.access$202(paramLayoutParams, mRight - rightMargin);
    } else if ((alignWithParent) && (paramArrayOfInt[7] != 0) && (paramInt >= 0)) {
      LayoutParams.access$202(paramLayoutParams, paramInt - mPaddingRight - rightMargin);
    }
    if (paramArrayOfInt[9] != 0) {
      LayoutParams.access$102(paramLayoutParams, mPaddingLeft + leftMargin);
    }
    if ((paramArrayOfInt[11] != 0) && (paramInt >= 0)) {
      LayoutParams.access$202(paramLayoutParams, paramInt - mPaddingRight - rightMargin);
    }
  }
  
  private void applyVerticalSizeRules(LayoutParams paramLayoutParams, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = paramLayoutParams.getRules();
    int i = getRelatedViewBaselineOffset(arrayOfInt);
    if (i != -1)
    {
      paramInt1 = i;
      if (paramInt2 != -1) {
        paramInt1 = i - paramInt2;
      }
      LayoutParams.access$402(paramLayoutParams, paramInt1);
      LayoutParams.access$302(paramLayoutParams, Integer.MIN_VALUE);
      return;
    }
    LayoutParams.access$402(paramLayoutParams, Integer.MIN_VALUE);
    LayoutParams.access$302(paramLayoutParams, Integer.MIN_VALUE);
    LayoutParams localLayoutParams = getRelatedViewParams(arrayOfInt, 2);
    if (localLayoutParams != null) {
      LayoutParams.access$302(paramLayoutParams, mTop - (topMargin + bottomMargin));
    } else if ((alignWithParent) && (arrayOfInt[2] != 0) && (paramInt1 >= 0)) {
      LayoutParams.access$302(paramLayoutParams, paramInt1 - mPaddingBottom - bottomMargin);
    }
    localLayoutParams = getRelatedViewParams(arrayOfInt, 3);
    if (localLayoutParams != null) {
      LayoutParams.access$402(paramLayoutParams, mBottom + (bottomMargin + topMargin));
    } else if ((alignWithParent) && (arrayOfInt[3] != 0)) {
      LayoutParams.access$402(paramLayoutParams, mPaddingTop + topMargin);
    }
    localLayoutParams = getRelatedViewParams(arrayOfInt, 6);
    if (localLayoutParams != null) {
      LayoutParams.access$402(paramLayoutParams, mTop + topMargin);
    } else if ((alignWithParent) && (arrayOfInt[6] != 0)) {
      LayoutParams.access$402(paramLayoutParams, mPaddingTop + topMargin);
    }
    localLayoutParams = getRelatedViewParams(arrayOfInt, 8);
    if (localLayoutParams != null) {
      LayoutParams.access$302(paramLayoutParams, mBottom - bottomMargin);
    } else if ((alignWithParent) && (arrayOfInt[8] != 0) && (paramInt1 >= 0)) {
      LayoutParams.access$302(paramLayoutParams, paramInt1 - mPaddingBottom - bottomMargin);
    }
    if (arrayOfInt[10] != 0) {
      LayoutParams.access$402(paramLayoutParams, mPaddingTop + topMargin);
    }
    if ((arrayOfInt[12] != 0) && (paramInt1 >= 0)) {
      LayoutParams.access$302(paramLayoutParams, paramInt1 - mPaddingBottom - bottomMargin);
    }
  }
  
  private static void centerHorizontal(View paramView, LayoutParams paramLayoutParams, int paramInt)
  {
    int i = paramView.getMeasuredWidth();
    paramInt = (paramInt - i) / 2;
    LayoutParams.access$102(paramLayoutParams, paramInt);
    LayoutParams.access$202(paramLayoutParams, paramInt + i);
  }
  
  private static void centerVertical(View paramView, LayoutParams paramLayoutParams, int paramInt)
  {
    int i = paramView.getMeasuredHeight();
    paramInt = (paramInt - i) / 2;
    LayoutParams.access$402(paramLayoutParams, paramInt);
    LayoutParams.access$302(paramLayoutParams, paramInt + i);
  }
  
  private int compareLayoutPosition(LayoutParams paramLayoutParams1, LayoutParams paramLayoutParams2)
  {
    int i = mTop - mTop;
    if (i != 0) {
      return i;
    }
    return mLeft - mLeft;
  }
  
  private int getChildMeasureSpec(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    int i = 0;
    int j = 0;
    int k;
    if (paramInt8 < 0) {
      k = 1;
    } else {
      k = 0;
    }
    if ((k != 0) && (!mAllowBrokenMeasureSpecs))
    {
      if ((paramInt1 != Integer.MIN_VALUE) && (paramInt2 != Integer.MIN_VALUE))
      {
        paramInt2 = Math.max(0, paramInt2 - paramInt1);
        paramInt1 = 1073741824;
      }
      else if (paramInt3 >= 0)
      {
        paramInt2 = paramInt3;
        paramInt1 = 1073741824;
      }
      else
      {
        paramInt2 = 0;
        paramInt1 = 0;
      }
      return View.MeasureSpec.makeMeasureSpec(paramInt2, paramInt1);
    }
    int m = paramInt1;
    int n = paramInt2;
    int i1 = m;
    if (m == Integer.MIN_VALUE) {
      i1 = paramInt6 + paramInt4;
    }
    paramInt4 = n;
    if (n == Integer.MIN_VALUE) {
      paramInt4 = paramInt8 - paramInt7 - paramInt5;
    }
    paramInt5 = paramInt4 - i1;
    paramInt4 = 1073741824;
    if ((paramInt1 != Integer.MIN_VALUE) && (paramInt2 != Integer.MIN_VALUE))
    {
      paramInt1 = paramInt4;
      if (k != 0) {
        paramInt1 = 0;
      }
      paramInt3 = Math.max(0, paramInt5);
      paramInt2 = paramInt1;
      paramInt1 = paramInt3;
    }
    else if (paramInt3 >= 0)
    {
      paramInt2 = 1073741824;
      if (paramInt5 >= 0) {
        paramInt1 = Math.min(paramInt5, paramInt3);
      } else {
        paramInt1 = paramInt3;
      }
    }
    else if (paramInt3 == -1)
    {
      paramInt1 = paramInt4;
      if (k != 0) {
        paramInt1 = 0;
      }
      paramInt3 = Math.max(0, paramInt5);
      paramInt2 = paramInt1;
      paramInt1 = paramInt3;
    }
    else
    {
      paramInt2 = i;
      paramInt1 = j;
      if (paramInt3 == -2) {
        if (paramInt5 >= 0)
        {
          paramInt2 = Integer.MIN_VALUE;
          paramInt1 = paramInt5;
        }
        else
        {
          paramInt2 = 0;
          paramInt1 = 0;
        }
      }
    }
    return View.MeasureSpec.makeMeasureSpec(paramInt1, paramInt2);
  }
  
  private View getRelatedView(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt[paramInt];
    if (i != 0)
    {
      paramArrayOfInt = (RelativeLayout.DependencyGraph.Node)mGraph.mKeyNodes.get(i);
      if (paramArrayOfInt == null) {
        return null;
      }
      paramArrayOfInt = view;
      while (paramArrayOfInt.getVisibility() == 8)
      {
        Object localObject = ((LayoutParams)paramArrayOfInt.getLayoutParams()).getRules(paramArrayOfInt.getLayoutDirection());
        localObject = (RelativeLayout.DependencyGraph.Node)mGraph.mKeyNodes.get(localObject[paramInt]);
        if ((localObject != null) && (paramArrayOfInt != view)) {
          paramArrayOfInt = view;
        } else {
          return null;
        }
      }
      return paramArrayOfInt;
    }
    return null;
  }
  
  private int getRelatedViewBaselineOffset(int[] paramArrayOfInt)
  {
    paramArrayOfInt = getRelatedView(paramArrayOfInt, 4);
    if (paramArrayOfInt != null)
    {
      int i = paramArrayOfInt.getBaseline();
      if ((i != -1) && ((paramArrayOfInt.getLayoutParams() instanceof LayoutParams))) {
        return getLayoutParamsmTop + i;
      }
    }
    return -1;
  }
  
  private LayoutParams getRelatedViewParams(int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt = getRelatedView(paramArrayOfInt, paramInt);
    if ((paramArrayOfInt != null) && ((paramArrayOfInt.getLayoutParams() instanceof LayoutParams))) {
      return (LayoutParams)paramArrayOfInt.getLayoutParams();
    }
    return null;
  }
  
  private void initFromAttributes(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RelativeLayout, paramInt1, paramInt2);
    mIgnoreGravity = paramContext.getResourceId(1, -1);
    mGravity = paramContext.getInt(0, mGravity);
    paramContext.recycle();
  }
  
  private void measureChild(View paramView, LayoutParams paramLayoutParams, int paramInt1, int paramInt2)
  {
    paramView.measure(getChildMeasureSpec(mLeft, mRight, width, leftMargin, rightMargin, mPaddingLeft, mPaddingRight, paramInt1), getChildMeasureSpec(mTop, mBottom, height, topMargin, bottomMargin, mPaddingTop, mPaddingBottom, paramInt2));
  }
  
  private void measureChildHorizontal(View paramView, LayoutParams paramLayoutParams, int paramInt1, int paramInt2)
  {
    int i = getChildMeasureSpec(mLeft, mRight, width, leftMargin, rightMargin, mPaddingLeft, mPaddingRight, paramInt1);
    if ((paramInt2 < 0) && (!mAllowBrokenMeasureSpecs)) {
      if (height >= 0) {
        paramInt1 = View.MeasureSpec.makeMeasureSpec(height, 1073741824);
      }
    }
    for (;;)
    {
      break;
      paramInt1 = View.MeasureSpec.makeMeasureSpec(0, 0);
      continue;
      if (mMeasureVerticalWithPaddingMargin) {
        paramInt1 = Math.max(0, paramInt2 - mPaddingTop - mPaddingBottom - topMargin - bottomMargin);
      } else {
        paramInt1 = Math.max(0, paramInt2);
      }
      if (height == -1) {
        paramInt2 = 1073741824;
      } else {
        paramInt2 = Integer.MIN_VALUE;
      }
      paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, paramInt2);
    }
    paramView.measure(i, paramInt1);
  }
  
  private void positionAtEdge(View paramView, LayoutParams paramLayoutParams, int paramInt)
  {
    if (isLayoutRtl())
    {
      LayoutParams.access$202(paramLayoutParams, paramInt - mPaddingRight - rightMargin);
      LayoutParams.access$102(paramLayoutParams, mRight - paramView.getMeasuredWidth());
    }
    else
    {
      LayoutParams.access$102(paramLayoutParams, mPaddingLeft + leftMargin);
      LayoutParams.access$202(paramLayoutParams, mLeft + paramView.getMeasuredWidth());
    }
  }
  
  private boolean positionChildHorizontal(View paramView, LayoutParams paramLayoutParams, int paramInt, boolean paramBoolean)
  {
    int[] arrayOfInt = paramLayoutParams.getRules(getLayoutDirection());
    int i = mLeft;
    boolean bool = true;
    if ((i == Integer.MIN_VALUE) && (mRight != Integer.MIN_VALUE)) {
      LayoutParams.access$102(paramLayoutParams, mRight - paramView.getMeasuredWidth());
    } else if ((mLeft != Integer.MIN_VALUE) && (mRight == Integer.MIN_VALUE)) {
      LayoutParams.access$202(paramLayoutParams, mLeft + paramView.getMeasuredWidth());
    } else if ((mLeft == Integer.MIN_VALUE) && (mRight == Integer.MIN_VALUE)) {
      if ((arrayOfInt[13] == 0) && (arrayOfInt[14] == 0))
      {
        positionAtEdge(paramView, paramLayoutParams, paramInt);
      }
      else
      {
        if (!paramBoolean) {
          centerHorizontal(paramView, paramLayoutParams, paramInt);
        } else {
          positionAtEdge(paramView, paramLayoutParams, paramInt);
        }
        return true;
      }
    }
    if (arrayOfInt[21] != 0) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  private boolean positionChildVertical(View paramView, LayoutParams paramLayoutParams, int paramInt, boolean paramBoolean)
  {
    int[] arrayOfInt = paramLayoutParams.getRules();
    int i = mTop;
    boolean bool = true;
    if ((i == Integer.MIN_VALUE) && (mBottom != Integer.MIN_VALUE)) {
      LayoutParams.access$402(paramLayoutParams, mBottom - paramView.getMeasuredHeight());
    } else if ((mTop != Integer.MIN_VALUE) && (mBottom == Integer.MIN_VALUE)) {
      LayoutParams.access$302(paramLayoutParams, mTop + paramView.getMeasuredHeight());
    } else if ((mTop == Integer.MIN_VALUE) && (mBottom == Integer.MIN_VALUE)) {
      if ((arrayOfInt[13] == 0) && (arrayOfInt[15] == 0))
      {
        LayoutParams.access$402(paramLayoutParams, mPaddingTop + topMargin);
        LayoutParams.access$302(paramLayoutParams, mTop + paramView.getMeasuredHeight());
      }
      else
      {
        if (!paramBoolean)
        {
          centerVertical(paramView, paramLayoutParams, paramInt);
        }
        else
        {
          LayoutParams.access$402(paramLayoutParams, mPaddingTop + topMargin);
          LayoutParams.access$302(paramLayoutParams, mTop + paramView.getMeasuredHeight());
        }
        return true;
      }
    }
    if (arrayOfInt[12] != 0) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  private void queryCompatibilityModes(Context paramContext)
  {
    int i = getApplicationInfotargetSdkVersion;
    boolean bool1 = false;
    if (i <= 17) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mAllowBrokenMeasureSpecs = bool2;
    boolean bool2 = bool1;
    if (i >= 18) {
      bool2 = true;
    }
    mMeasureVerticalWithPaddingMargin = bool2;
  }
  
  private void sortChildren()
  {
    int i = getChildCount();
    if ((mSortedVerticalChildren == null) || (mSortedVerticalChildren.length != i)) {
      mSortedVerticalChildren = new View[i];
    }
    if ((mSortedHorizontalChildren == null) || (mSortedHorizontalChildren.length != i)) {
      mSortedHorizontalChildren = new View[i];
    }
    DependencyGraph localDependencyGraph = mGraph;
    localDependencyGraph.clear();
    for (int j = 0; j < i; j++) {
      localDependencyGraph.add(getChildAt(j));
    }
    localDependencyGraph.getSortedViews(mSortedVerticalChildren, RULES_VERTICAL);
    localDependencyGraph.getSortedViews(mSortedHorizontalChildren, RULES_HORIZONTAL);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    if (mTopToBottomLeftToRightSet == null) {
      mTopToBottomLeftToRightSet = new TreeSet(new TopToBottomLeftToRightComparator(null));
    }
    int i = 0;
    int j = getChildCount();
    while (i < j)
    {
      mTopToBottomLeftToRightSet.add(getChildAt(i));
      i++;
    }
    Iterator localIterator = mTopToBottomLeftToRightSet.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      if ((localView.getVisibility() == 0) && (localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent)))
      {
        mTopToBottomLeftToRightSet.clear();
        return true;
      }
    }
    mTopToBottomLeftToRightSet.clear();
    return false;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (sPreserveMarginParamsInLayoutParamConversion)
    {
      if ((paramLayoutParams instanceof LayoutParams)) {
        return new LayoutParams((LayoutParams)paramLayoutParams);
      }
      if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
        return new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
      }
    }
    return new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return RelativeLayout.class.getName();
  }
  
  public int getBaseline()
  {
    int i;
    if (mBaselineView != null) {
      i = mBaselineView.getBaseline();
    } else {
      i = super.getBaseline();
    }
    return i;
  }
  
  public int getGravity()
  {
    return mGravity;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramInt2 = getChildCount();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      View localView = getChildAt(paramInt1);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        localView.layout(mLeft, mTop, mRight, mBottom);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (mDirtyHierarchy)
    {
      mDirtyHierarchy = false;
      sortChildren();
    }
    int i = -1;
    int j = -1;
    int k = 0;
    int m = 0;
    int n = View.MeasureSpec.getMode(paramInt1);
    int i1 = View.MeasureSpec.getMode(paramInt2);
    int i2 = View.MeasureSpec.getSize(paramInt1);
    int i3 = View.MeasureSpec.getSize(paramInt2);
    if (n != 0) {
      i = i2;
    }
    if (i1 != 0) {
      j = i3;
    }
    if (n == 1073741824) {
      k = i;
    }
    if (i1 == 1073741824) {
      m = j;
    }
    Object localObject1 = null;
    i3 = mGravity & 0x800007;
    int i4;
    if ((i3 != 8388611) && (i3 != 0)) {
      i4 = 1;
    } else {
      i4 = 0;
    }
    i3 = mGravity & 0x70;
    int i5;
    if ((i3 != 48) && (i3 != 0)) {
      i5 = 1;
    } else {
      i5 = 0;
    }
    int i6 = 0;
    if (i4 == 0)
    {
      localObject2 = localObject1;
      if (i5 == 0) {}
    }
    else
    {
      localObject2 = localObject1;
      if (mIgnoreGravity != -1) {
        localObject2 = findViewById(mIgnoreGravity);
      }
    }
    boolean bool1;
    if (n != 1073741824) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if (i1 != 1073741824) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    int i7 = getLayoutDirection();
    if (isLayoutRtl())
    {
      i3 = i;
      if (i == -1) {
        i3 = 65536;
      }
    }
    else
    {
      i3 = i;
    }
    localObject1 = mSortedHorizontalChildren;
    i2 = localObject1.length;
    int i8 = 0;
    int i9 = 0;
    i = i1;
    i1 = i2;
    while (i9 < i1)
    {
      localObject3 = localObject1[i9];
      i2 = i8;
      if (((View)localObject3).getVisibility() != 8)
      {
        localObject4 = (LayoutParams)((View)localObject3).getLayoutParams();
        applyHorizontalSizeRules((LayoutParams)localObject4, i3, ((LayoutParams)localObject4).getRules(i7));
        measureChildHorizontal((View)localObject3, (LayoutParams)localObject4, i3, j);
        i2 = i8;
        if (positionChildHorizontal((View)localObject3, (LayoutParams)localObject4, i3, bool1)) {
          i2 = 1;
        }
      }
      i9++;
      i8 = i2;
    }
    Object localObject3 = mSortedVerticalChildren;
    n = localObject3.length;
    int i10 = getContextgetApplicationInfotargetSdkVersion;
    i1 = i7;
    i2 = Integer.MAX_VALUE;
    int i11 = Integer.MAX_VALUE;
    int i12 = Integer.MIN_VALUE;
    i7 = Integer.MIN_VALUE;
    i = m;
    int i13 = 0;
    i9 = i6;
    m = k;
    int i14 = j;
    k = i11;
    while (i13 < n)
    {
      localObject4 = localObject3[i13];
      int i15;
      int i16;
      int i17;
      int i18;
      if (((View)localObject4).getVisibility() != 8)
      {
        localObject1 = (LayoutParams)((View)localObject4).getLayoutParams();
        applyVerticalSizeRules((LayoutParams)localObject1, i14, ((View)localObject4).getBaseline());
        measureChild((View)localObject4, (LayoutParams)localObject1, i3, i14);
        if (positionChildVertical((View)localObject4, (LayoutParams)localObject1, i14, bool2)) {
          i9 = 1;
        }
        if (bool1)
        {
          if (isLayoutRtl())
          {
            if (i10 < 19) {
              j = Math.max(m, i3 - mLeft);
            } else {
              j = Math.max(m, i3 - mLeft + leftMargin);
            }
          }
          else if (i10 < 19) {
            j = Math.max(m, mRight);
          } else {
            j = Math.max(m, mRight + rightMargin);
          }
        }
        else {
          j = m;
        }
        m = i;
        if (bool2) {
          if (i10 < 19) {
            m = Math.max(i, mBottom);
          } else {
            m = Math.max(i, mBottom + bottomMargin);
          }
        }
        if (localObject4 == localObject2)
        {
          i = k;
          i6 = i2;
          if (i5 == 0) {}
        }
        else
        {
          i6 = Math.min(i2, mLeft - leftMargin);
          i = Math.min(k, mTop - topMargin);
        }
        if (localObject4 == localObject2)
        {
          k = i;
          i15 = j;
          i16 = m;
          i2 = i6;
          i17 = i9;
          i11 = i12;
          i18 = i7;
          if (i4 == 0) {}
        }
        else
        {
          i11 = Math.max(i12, mRight + rightMargin);
          i18 = Math.max(i7, mBottom + bottomMargin);
          k = i;
          i15 = j;
          i16 = m;
          i2 = i6;
          i17 = i9;
        }
      }
      else
      {
        i18 = i7;
        i11 = i12;
        i17 = i9;
        i16 = i;
        i15 = m;
      }
      i13++;
      m = i15;
      i = i16;
      i9 = i17;
      i12 = i11;
      i7 = i18;
    }
    Object localObject5 = null;
    localObject1 = null;
    i6 = 0;
    Object localObject4 = localObject2;
    j = i10;
    Object localObject7;
    LayoutParams localLayoutParams;
    for (Object localObject2 = localObject5; i6 < n; localObject2 = localObject5)
    {
      Object localObject6 = localObject3[i6];
      localObject7 = localObject1;
      localObject5 = localObject2;
      if (localObject6.getVisibility() != 8)
      {
        localLayoutParams = (LayoutParams)localObject6.getLayoutParams();
        if ((localObject1 != null) && (localObject2 != null))
        {
          localObject7 = localObject1;
          localObject5 = localObject2;
          if (compareLayoutPosition(localLayoutParams, (LayoutParams)localObject2) >= 0) {}
        }
        else
        {
          localObject7 = localObject6;
          localObject5 = localLayoutParams;
        }
      }
      i6++;
      localObject1 = localObject7;
    }
    mBaselineView = ((View)localObject1);
    localObject5 = localObject1;
    i6 = m;
    j = i1;
    if (bool1)
    {
      m += mPaddingRight;
      j = m;
      if (mLayoutParams != null)
      {
        j = m;
        if (mLayoutParams.width >= 0) {
          j = Math.max(m, mLayoutParams.width);
        }
      }
      m = resolveSize(Math.max(j, getSuggestedMinimumWidth()), paramInt1);
      localObject5 = localObject1;
      i6 = m;
      j = i1;
      if (i8 != 0) {
        for (paramInt1 = 0;; paramInt1++)
        {
          localObject5 = localObject1;
          i6 = m;
          j = i1;
          if (paramInt1 >= n) {
            break;
          }
          localLayoutParams = localObject3[paramInt1];
          if (localLayoutParams.getVisibility() != 8)
          {
            localObject5 = (LayoutParams)localLayoutParams.getLayoutParams();
            localObject7 = ((LayoutParams)localObject5).getRules(i1);
            if ((localObject7[13] == 0) && (localObject7[14] == 0))
            {
              if (localObject7[11] != 0)
              {
                j = localLayoutParams.getMeasuredWidth();
                LayoutParams.access$102((LayoutParams)localObject5, m - mPaddingRight - j);
                LayoutParams.access$202((LayoutParams)localObject5, mLeft + j);
              }
            }
            else {
              centerHorizontal(localLayoutParams, (LayoutParams)localObject5, m);
            }
          }
        }
      }
    }
    localObject1 = localObject2;
    paramInt1 = i;
    if (bool2)
    {
      i += mPaddingBottom;
      paramInt1 = i;
      if (mLayoutParams != null)
      {
        paramInt1 = i;
        if (mLayoutParams.height >= 0) {
          paramInt1 = Math.max(i, mLayoutParams.height);
        }
      }
      i = resolveSize(Math.max(paramInt1, getSuggestedMinimumHeight()), paramInt2);
      localObject1 = localObject2;
      paramInt1 = i;
      if (i9 != 0) {
        for (paramInt2 = 0;; paramInt2++)
        {
          localObject1 = localObject2;
          paramInt1 = i;
          if (paramInt2 >= n) {
            break;
          }
          localObject7 = localObject3[paramInt2];
          if (((View)localObject7).getVisibility() != 8)
          {
            localObject5 = (LayoutParams)((View)localObject7).getLayoutParams();
            localObject1 = ((LayoutParams)localObject5).getRules(j);
            if ((localObject1[13] == 0) && (localObject1[15] == 0))
            {
              if (localObject1[12] != 0)
              {
                paramInt1 = ((View)localObject7).getMeasuredHeight();
                LayoutParams.access$402((LayoutParams)localObject5, i - mPaddingBottom - paramInt1);
                LayoutParams.access$302((LayoutParams)localObject5, mTop + paramInt1);
              }
            }
            else {
              centerVertical((View)localObject7, (LayoutParams)localObject5, i);
            }
          }
        }
      }
    }
    if ((i4 == 0) && (i5 == 0)) {}
    for (;;)
    {
      break;
      localObject1 = mSelfBounds;
      ((Rect)localObject1).set(mPaddingLeft, mPaddingTop, i6 - mPaddingRight, paramInt1 - mPaddingBottom);
      localObject2 = mContentBounds;
      Gravity.apply(mGravity, i12 - i2, i7 - k, (Rect)localObject1, (Rect)localObject2, j);
      i = left - i2;
      k = top - k;
      if ((i != 0) || (k != 0)) {
        for (paramInt2 = 0; paramInt2 < n; paramInt2++)
        {
          localObject5 = localObject3[paramInt2];
          if ((((View)localObject5).getVisibility() != 8) && (localObject5 != localObject4))
          {
            localObject5 = (LayoutParams)((View)localObject5).getLayoutParams();
            if (i4 != 0)
            {
              LayoutParams.access$112((LayoutParams)localObject5, i);
              LayoutParams.access$212((LayoutParams)localObject5, i);
            }
            if (i5 != 0)
            {
              LayoutParams.access$412((LayoutParams)localObject5, k);
              LayoutParams.access$312((LayoutParams)localObject5, k);
            }
          }
        }
      }
    }
    if (isLayoutRtl())
    {
      j = i3 - i6;
      for (paramInt2 = 0; paramInt2 < n; paramInt2++)
      {
        localObject2 = localObject3[paramInt2];
        if (((View)localObject2).getVisibility() != 8)
        {
          localObject2 = (LayoutParams)((View)localObject2).getLayoutParams();
          LayoutParams.access$120((LayoutParams)localObject2, j);
          LayoutParams.access$220((LayoutParams)localObject2, j);
        }
      }
    }
    setMeasuredDimension(i6, paramInt1);
  }
  
  public void requestLayout()
  {
    super.requestLayout();
    mDirtyHierarchy = true;
  }
  
  @RemotableViewMethod
  public void setGravity(int paramInt)
  {
    if (mGravity != paramInt)
    {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0) {
        i = paramInt | 0x800003;
      }
      paramInt = i;
      if ((i & 0x70) == 0) {
        paramInt = i | 0x30;
      }
      mGravity = paramInt;
      requestLayout();
    }
  }
  
  @RemotableViewMethod
  public void setHorizontalGravity(int paramInt)
  {
    paramInt &= 0x800007;
    if ((0x800007 & mGravity) != paramInt)
    {
      mGravity = (mGravity & 0xFF7FFFF8 | paramInt);
      requestLayout();
    }
  }
  
  @RemotableViewMethod
  public void setIgnoreGravity(int paramInt)
  {
    mIgnoreGravity = paramInt;
  }
  
  @RemotableViewMethod
  public void setVerticalGravity(int paramInt)
  {
    paramInt &= 0x70;
    if ((mGravity & 0x70) != paramInt)
    {
      mGravity = (mGravity & 0xFFFFFF8F | paramInt);
      requestLayout();
    }
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  private static class DependencyGraph
  {
    private SparseArray<Node> mKeyNodes = new SparseArray();
    private ArrayList<Node> mNodes = new ArrayList();
    private ArrayDeque<Node> mRoots = new ArrayDeque();
    
    private DependencyGraph() {}
    
    private ArrayDeque<Node> findRoots(int[] paramArrayOfInt)
    {
      Object localObject = mKeyNodes;
      ArrayList localArrayList = mNodes;
      int i = localArrayList.size();
      int j = 0;
      Node localNode1;
      for (int k = 0; k < i; k++)
      {
        localNode1 = (Node)localArrayList.get(k);
        dependents.clear();
        dependencies.clear();
      }
      for (k = 0; k < i; k++)
      {
        Node localNode2 = (Node)localArrayList.get(k);
        int[] arrayOfInt = RelativeLayout.LayoutParams.access$700((RelativeLayout.LayoutParams)view.getLayoutParams());
        int m = paramArrayOfInt.length;
        for (int n = 0; n < m; n++)
        {
          int i1 = arrayOfInt[paramArrayOfInt[n]];
          if (i1 > 0)
          {
            localNode1 = (Node)((SparseArray)localObject).get(i1);
            if ((localNode1 != null) && (localNode1 != localNode2))
            {
              dependents.put(localNode2, this);
              dependencies.put(i1, localNode1);
            }
          }
        }
      }
      localObject = mRoots;
      ((ArrayDeque)localObject).clear();
      for (k = j; k < i; k++)
      {
        paramArrayOfInt = (Node)localArrayList.get(k);
        if (dependencies.size() == 0) {
          ((ArrayDeque)localObject).addLast(paramArrayOfInt);
        }
      }
      return localObject;
    }
    
    void add(View paramView)
    {
      int i = paramView.getId();
      paramView = Node.acquire(paramView);
      if (i != -1) {
        mKeyNodes.put(i, paramView);
      }
      mNodes.add(paramView);
    }
    
    void clear()
    {
      ArrayList localArrayList = mNodes;
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        ((Node)localArrayList.get(j)).release();
      }
      localArrayList.clear();
      mKeyNodes.clear();
      mRoots.clear();
    }
    
    void getSortedViews(View[] paramArrayOfView, int... paramVarArgs)
    {
      paramVarArgs = findRoots(paramVarArgs);
      for (int i = 0;; i++)
      {
        Object localObject1 = (Node)paramVarArgs.pollLast();
        if (localObject1 == null) {
          break;
        }
        Object localObject2 = view;
        int j = ((View)localObject2).getId();
        paramArrayOfView[i] = localObject2;
        localObject2 = dependents;
        int k = ((ArrayMap)localObject2).size();
        for (int m = 0; m < k; m++)
        {
          Node localNode = (Node)((ArrayMap)localObject2).keyAt(m);
          localObject1 = dependencies;
          ((SparseArray)localObject1).remove(j);
          if (((SparseArray)localObject1).size() == 0) {
            paramVarArgs.add(localNode);
          }
        }
      }
      if (i >= paramArrayOfView.length) {
        return;
      }
      throw new IllegalStateException("Circular dependencies cannot exist in RelativeLayout");
    }
    
    static class Node
    {
      private static final int POOL_LIMIT = 100;
      private static final Pools.SynchronizedPool<Node> sPool = new Pools.SynchronizedPool(100);
      final SparseArray<Node> dependencies = new SparseArray();
      final ArrayMap<Node, RelativeLayout.DependencyGraph> dependents = new ArrayMap();
      View view;
      
      Node() {}
      
      static Node acquire(View paramView)
      {
        Node localNode1 = (Node)sPool.acquire();
        Node localNode2 = localNode1;
        if (localNode1 == null) {
          localNode2 = new Node();
        }
        view = paramView;
        return localNode2;
      }
      
      void release()
      {
        view = null;
        dependents.clear();
        dependencies.clear();
        sPool.release(this);
      }
    }
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    @ViewDebug.ExportedProperty(category="layout")
    public boolean alignWithParent;
    private int mBottom;
    private int[] mInitialRules = new int[22];
    private boolean mIsRtlCompatibilityMode = false;
    private int mLeft;
    private boolean mNeedsLayoutResolution;
    private int mRight;
    @ViewDebug.ExportedProperty(category="layout", indexMapping={@android.view.ViewDebug.IntToString(from=2, to="above"), @android.view.ViewDebug.IntToString(from=4, to="alignBaseline"), @android.view.ViewDebug.IntToString(from=8, to="alignBottom"), @android.view.ViewDebug.IntToString(from=5, to="alignLeft"), @android.view.ViewDebug.IntToString(from=12, to="alignParentBottom"), @android.view.ViewDebug.IntToString(from=9, to="alignParentLeft"), @android.view.ViewDebug.IntToString(from=11, to="alignParentRight"), @android.view.ViewDebug.IntToString(from=10, to="alignParentTop"), @android.view.ViewDebug.IntToString(from=7, to="alignRight"), @android.view.ViewDebug.IntToString(from=6, to="alignTop"), @android.view.ViewDebug.IntToString(from=3, to="below"), @android.view.ViewDebug.IntToString(from=14, to="centerHorizontal"), @android.view.ViewDebug.IntToString(from=13, to="center"), @android.view.ViewDebug.IntToString(from=15, to="centerVertical"), @android.view.ViewDebug.IntToString(from=0, to="leftOf"), @android.view.ViewDebug.IntToString(from=1, to="rightOf"), @android.view.ViewDebug.IntToString(from=18, to="alignStart"), @android.view.ViewDebug.IntToString(from=19, to="alignEnd"), @android.view.ViewDebug.IntToString(from=20, to="alignParentStart"), @android.view.ViewDebug.IntToString(from=21, to="alignParentEnd"), @android.view.ViewDebug.IntToString(from=16, to="startOf"), @android.view.ViewDebug.IntToString(from=17, to="endOf")}, mapping={@android.view.ViewDebug.IntToString(from=-1, to="true"), @android.view.ViewDebug.IntToString(from=0, to="false/NO_ID")}, resolveId=true)
    private int[] mRules = new int[22];
    private boolean mRulesChanged = false;
    private int mTop;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RelativeLayout_Layout);
      boolean bool;
      if ((getApplicationInfotargetSdkVersion >= 17) && (paramContext.getApplicationInfo().hasRtlSupport())) {
        bool = false;
      } else {
        bool = true;
      }
      mIsRtlCompatibilityMode = bool;
      int[] arrayOfInt = mRules;
      paramContext = mInitialRules;
      int i = paramAttributeSet.getIndexCount();
      for (int j = 0; j < i; j++)
      {
        int k = paramAttributeSet.getIndex(j);
        int m = -1;
        switch (k)
        {
        default: 
          break;
        case 22: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[21] = m;
          break;
        case 21: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[20] = m;
          break;
        case 20: 
          arrayOfInt[19] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 19: 
          arrayOfInt[18] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 18: 
          arrayOfInt[17] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 17: 
          arrayOfInt[16] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 16: 
          alignWithParent = paramAttributeSet.getBoolean(k, false);
          break;
        case 15: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[15] = m;
          break;
        case 14: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[14] = m;
          break;
        case 13: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[13] = m;
          break;
        case 12: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[12] = m;
          break;
        case 11: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[11] = m;
          break;
        case 10: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[10] = m;
          break;
        case 9: 
          if (!paramAttributeSet.getBoolean(k, false)) {
            m = 0;
          }
          arrayOfInt[9] = m;
          break;
        case 8: 
          arrayOfInt[8] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 7: 
          arrayOfInt[7] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 6: 
          arrayOfInt[6] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 5: 
          arrayOfInt[5] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 4: 
          arrayOfInt[4] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 3: 
          arrayOfInt[3] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 2: 
          arrayOfInt[2] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 1: 
          arrayOfInt[1] = paramAttributeSet.getResourceId(k, 0);
          break;
        case 0: 
          arrayOfInt[0] = paramAttributeSet.getResourceId(k, 0);
        }
      }
      mRulesChanged = true;
      System.arraycopy(arrayOfInt, 0, paramContext, 0, 22);
      paramAttributeSet.recycle();
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
      mIsRtlCompatibilityMode = mIsRtlCompatibilityMode;
      mRulesChanged = mRulesChanged;
      alignWithParent = alignWithParent;
      System.arraycopy(mRules, 0, mRules, 0, 22);
      System.arraycopy(mInitialRules, 0, mInitialRules, 0, 22);
    }
    
    private boolean hasRelativeRules()
    {
      boolean bool;
      if ((mInitialRules[16] == 0) && (mInitialRules[17] == 0) && (mInitialRules[18] == 0) && (mInitialRules[19] == 0) && (mInitialRules[20] == 0) && (mInitialRules[21] == 0)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    private boolean isRelativeRule(int paramInt)
    {
      boolean bool;
      if ((paramInt != 16) && (paramInt != 17) && (paramInt != 18) && (paramInt != 19) && (paramInt != 20) && (paramInt != 21)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    private void resolveRules(int paramInt)
    {
      int i = 1;
      if (paramInt == 1) {
        paramInt = 1;
      } else {
        paramInt = 0;
      }
      System.arraycopy(mInitialRules, 0, mRules, 0, 22);
      if (mIsRtlCompatibilityMode)
      {
        if (mRules[18] != 0)
        {
          if (mRules[5] == 0) {
            mRules[5] = mRules[18];
          }
          mRules[18] = 0;
        }
        if (mRules[19] != 0)
        {
          if (mRules[7] == 0) {
            mRules[7] = mRules[19];
          }
          mRules[19] = 0;
        }
        if (mRules[16] != 0)
        {
          if (mRules[0] == 0) {
            mRules[0] = mRules[16];
          }
          mRules[16] = 0;
        }
        if (mRules[17] != 0)
        {
          if (mRules[1] == 0) {
            mRules[1] = mRules[17];
          }
          mRules[17] = 0;
        }
        if (mRules[20] != 0)
        {
          if (mRules[9] == 0) {
            mRules[9] = mRules[20];
          }
          mRules[20] = 0;
        }
        if (mRules[21] != 0)
        {
          if (mRules[11] == 0) {
            mRules[11] = mRules[21];
          }
          mRules[21] = 0;
        }
      }
      else
      {
        if (((mRules[18] != 0) || (mRules[19] != 0)) && ((mRules[5] != 0) || (mRules[7] != 0)))
        {
          mRules[5] = 0;
          mRules[7] = 0;
        }
        int[] arrayOfInt;
        int j;
        if (mRules[18] != 0)
        {
          arrayOfInt = mRules;
          if (paramInt != 0) {
            j = 7;
          } else {
            j = 5;
          }
          arrayOfInt[j] = mRules[18];
          mRules[18] = 0;
        }
        if (mRules[19] != 0)
        {
          arrayOfInt = mRules;
          if (paramInt != 0) {
            j = 5;
          } else {
            j = 7;
          }
          arrayOfInt[j] = mRules[19];
          mRules[19] = 0;
        }
        if (((mRules[16] != 0) || (mRules[17] != 0)) && ((mRules[0] != 0) || (mRules[1] != 0)))
        {
          mRules[0] = 0;
          mRules[1] = 0;
        }
        if (mRules[16] != 0)
        {
          arrayOfInt = mRules;
          if (paramInt != 0) {
            j = 1;
          } else {
            j = 0;
          }
          arrayOfInt[j] = mRules[16];
          mRules[16] = 0;
        }
        if (mRules[17] != 0)
        {
          arrayOfInt = mRules;
          j = i;
          if (paramInt != 0) {
            j = 0;
          }
          arrayOfInt[j] = mRules[17];
          mRules[17] = 0;
        }
        if ((mRules[20] == 0) && (mRules[21] == 0)) {
          break label643;
        }
        if (mRules[9] == 0) {
          if (mRules[11] == 0) {
            break label643;
          }
        }
        mRules[9] = 0;
        mRules[11] = 0;
        label643:
        i = 11;
        if (mRules[20] != 0)
        {
          arrayOfInt = mRules;
          if (paramInt != 0) {
            j = 11;
          } else {
            j = 9;
          }
          arrayOfInt[j] = mRules[20];
          mRules[20] = 0;
        }
        if (mRules[21] != 0)
        {
          arrayOfInt = mRules;
          j = i;
          if (paramInt != 0) {
            j = 9;
          }
          arrayOfInt[j] = mRules[21];
          mRules[21] = 0;
        }
      }
      mRulesChanged = false;
      mNeedsLayoutResolution = false;
    }
    
    private boolean shouldResolveLayoutDirection(int paramInt)
    {
      boolean bool;
      if (((!mNeedsLayoutResolution) && (!hasRelativeRules())) || ((!mRulesChanged) && (paramInt == getLayoutDirection()))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public void addRule(int paramInt)
    {
      addRule(paramInt, -1);
    }
    
    public void addRule(int paramInt1, int paramInt2)
    {
      if ((!mNeedsLayoutResolution) && (isRelativeRule(paramInt1)) && (mInitialRules[paramInt1] != 0) && (paramInt2 == 0)) {
        mNeedsLayoutResolution = true;
      }
      mRules[paramInt1] = paramInt2;
      mInitialRules[paramInt1] = paramInt2;
      mRulesChanged = true;
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
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      super.encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.addProperty("layout:alignWithParent", alignWithParent);
    }
    
    public int getRule(int paramInt)
    {
      return mRules[paramInt];
    }
    
    public int[] getRules()
    {
      return mRules;
    }
    
    public int[] getRules(int paramInt)
    {
      resolveLayoutDirection(paramInt);
      return mRules;
    }
    
    public void removeRule(int paramInt)
    {
      addRule(paramInt, 0);
    }
    
    public void resolveLayoutDirection(int paramInt)
    {
      if (shouldResolveLayoutDirection(paramInt)) {
        resolveRules(paramInt);
      }
      super.resolveLayoutDirection(paramInt);
    }
  }
  
  private class TopToBottomLeftToRightComparator
    implements Comparator<View>
  {
    private TopToBottomLeftToRightComparator() {}
    
    public int compare(View paramView1, View paramView2)
    {
      int i = paramView1.getTop() - paramView2.getTop();
      if (i != 0) {
        return i;
      }
      i = paramView1.getLeft() - paramView2.getLeft();
      if (i != 0) {
        return i;
      }
      i = paramView1.getHeight() - paramView2.getHeight();
      if (i != 0) {
        return i;
      }
      i = paramView1.getWidth() - paramView2.getWidth();
      if (i != 0) {
        return i;
      }
      return 0;
    }
  }
}
