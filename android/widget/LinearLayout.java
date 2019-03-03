package android.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import com.android.internal.R.styleable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RemoteViews.RemoteView
public class LinearLayout
  extends ViewGroup
{
  public static final int HORIZONTAL = 0;
  private static final int INDEX_BOTTOM = 2;
  private static final int INDEX_CENTER_VERTICAL = 0;
  private static final int INDEX_FILL = 3;
  private static final int INDEX_TOP = 1;
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  public static final int SHOW_DIVIDER_END = 4;
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  public static final int SHOW_DIVIDER_NONE = 0;
  public static final int VERTICAL = 1;
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  private static boolean sCompatibilityDone = false;
  private static boolean sRemeasureWeightedChildren = true;
  private final boolean mAllowInconsistentMeasurement;
  @ViewDebug.ExportedProperty(category="layout")
  private boolean mBaselineAligned;
  @ViewDebug.ExportedProperty(category="layout")
  private int mBaselineAlignedChildIndex;
  @ViewDebug.ExportedProperty(category="measurement")
  private int mBaselineChildTop;
  private Drawable mDivider;
  private int mDividerHeight;
  private int mDividerPadding;
  private int mDividerWidth;
  @ViewDebug.ExportedProperty(category="measurement", flagMapping={@android.view.ViewDebug.FlagToString(equals=-1, mask=-1, name="NONE"), @android.view.ViewDebug.FlagToString(equals=0, mask=0, name="NONE"), @android.view.ViewDebug.FlagToString(equals=48, mask=48, name="TOP"), @android.view.ViewDebug.FlagToString(equals=80, mask=80, name="BOTTOM"), @android.view.ViewDebug.FlagToString(equals=3, mask=3, name="LEFT"), @android.view.ViewDebug.FlagToString(equals=5, mask=5, name="RIGHT"), @android.view.ViewDebug.FlagToString(equals=8388611, mask=8388611, name="START"), @android.view.ViewDebug.FlagToString(equals=8388613, mask=8388613, name="END"), @android.view.ViewDebug.FlagToString(equals=16, mask=16, name="CENTER_VERTICAL"), @android.view.ViewDebug.FlagToString(equals=112, mask=112, name="FILL_VERTICAL"), @android.view.ViewDebug.FlagToString(equals=1, mask=1, name="CENTER_HORIZONTAL"), @android.view.ViewDebug.FlagToString(equals=7, mask=7, name="FILL_HORIZONTAL"), @android.view.ViewDebug.FlagToString(equals=17, mask=17, name="CENTER"), @android.view.ViewDebug.FlagToString(equals=119, mask=119, name="FILL"), @android.view.ViewDebug.FlagToString(equals=8388608, mask=8388608, name="RELATIVE")}, formatToHexString=true)
  private int mGravity;
  private int mLayoutDirection;
  private int[] mMaxAscent;
  private int[] mMaxDescent;
  @ViewDebug.ExportedProperty(category="measurement")
  private int mOrientation;
  private int mShowDividers;
  @ViewDebug.ExportedProperty(category="measurement")
  private int mTotalLength;
  @ViewDebug.ExportedProperty(category="layout")
  private boolean mUseLargestChild;
  @ViewDebug.ExportedProperty(category="layout")
  private float mWeightSum;
  
  public LinearLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public LinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public LinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    boolean bool1 = true;
    mBaselineAligned = true;
    mBaselineAlignedChildIndex = -1;
    mBaselineChildTop = 0;
    mGravity = 8388659;
    mLayoutDirection = -1;
    if ((!sCompatibilityDone) && (paramContext != null))
    {
      if (getApplicationInfotargetSdkVersion >= 28) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sRemeasureWeightedChildren = bool2;
      sCompatibilityDone = true;
    }
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.LinearLayout, paramInt1, paramInt2);
    paramInt1 = paramAttributeSet.getInt(1, -1);
    if (paramInt1 >= 0) {
      setOrientation(paramInt1);
    }
    paramInt1 = paramAttributeSet.getInt(0, -1);
    if (paramInt1 >= 0) {
      setGravity(paramInt1);
    }
    boolean bool2 = paramAttributeSet.getBoolean(2, true);
    if (!bool2) {
      setBaselineAligned(bool2);
    }
    mWeightSum = paramAttributeSet.getFloat(4, -1.0F);
    mBaselineAlignedChildIndex = paramAttributeSet.getInt(3, -1);
    mUseLargestChild = paramAttributeSet.getBoolean(6, false);
    mShowDividers = paramAttributeSet.getInt(7, 0);
    mDividerPadding = paramAttributeSet.getDimensionPixelSize(8, 0);
    setDividerDrawable(paramAttributeSet.getDrawable(5));
    if (getApplicationInfotargetSdkVersion <= 23) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    mAllowInconsistentMeasurement = bool2;
    paramAttributeSet.recycle();
  }
  
  private boolean allViewsAreGoneBefore(int paramInt)
  {
    
    while (paramInt >= 0)
    {
      View localView = getVirtualChildAt(paramInt);
      if ((localView != null) && (localView.getVisibility() != 8)) {
        return false;
      }
      paramInt--;
    }
    return true;
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (int j = 0; j < paramInt1; j++)
    {
      View localView = getVirtualChildAt(j);
      if ((localView != null) && (localView.getVisibility() != 8))
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (height == -1)
        {
          int k = width;
          width = localView.getMeasuredWidth();
          measureChildWithMargins(localView, paramInt2, 0, i, 0);
          width = k;
        }
      }
    }
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (int j = 0; j < paramInt1; j++)
    {
      View localView = getVirtualChildAt(j);
      if ((localView != null) && (localView.getVisibility() != 8))
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (width == -1)
        {
          int k = height;
          height = localView.getMeasuredHeight();
          measureChildWithMargins(localView, i, 0, paramInt2, 0);
          height = k;
        }
      }
    }
  }
  
  private View getLastNonGoneChild()
  {
    for (int i = getVirtualChildCount() - 1; i >= 0; i--)
    {
      View localView = getVirtualChildAt(i);
      if ((localView != null) && (localView.getVisibility() != 8)) {
        return localView;
      }
    }
    return null;
  }
  
  private boolean isShowingDividers()
  {
    boolean bool;
    if ((mShowDividers != 0) && (mDivider != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas)
  {
    int i = getVirtualChildCount();
    boolean bool = isLayoutRtl();
    Object localObject1;
    Object localObject2;
    for (int j = 0; j < i; j++)
    {
      localObject1 = getVirtualChildAt(j);
      if ((localObject1 != null) && (((View)localObject1).getVisibility() != 8) && (hasDividerBeforeChildAt(j)))
      {
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        int k;
        if (bool) {
          k = ((View)localObject1).getRight() + rightMargin;
        } else {
          k = ((View)localObject1).getLeft() - leftMargin - mDividerWidth;
        }
        drawVerticalDivider(paramCanvas, k);
      }
    }
    if (hasDividerBeforeChildAt(i))
    {
      localObject2 = getLastNonGoneChild();
      if (localObject2 == null)
      {
        if (bool) {}
        for (j = getPaddingLeft();; j = getWidth() - getPaddingRight() - mDividerWidth) {
          break;
        }
      }
      localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
      if (bool) {
        j = ((View)localObject2).getLeft() - leftMargin - mDividerWidth;
      } else {
        j = ((View)localObject2).getRight() + rightMargin;
      }
      drawVerticalDivider(paramCanvas, j);
    }
  }
  
  void drawDividersVertical(Canvas paramCanvas)
  {
    int i = getVirtualChildCount();
    Object localObject1;
    Object localObject2;
    for (int j = 0; j < i; j++)
    {
      localObject1 = getVirtualChildAt(j);
      if ((localObject1 != null) && (((View)localObject1).getVisibility() != 8) && (hasDividerBeforeChildAt(j)))
      {
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        drawHorizontalDivider(paramCanvas, ((View)localObject1).getTop() - topMargin - mDividerHeight);
      }
    }
    if (hasDividerBeforeChildAt(i))
    {
      localObject2 = getLastNonGoneChild();
      if (localObject2 == null)
      {
        j = getHeight() - getPaddingBottom() - mDividerHeight;
      }
      else
      {
        localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
        j = ((View)localObject2).getBottom() + bottomMargin;
      }
      drawHorizontalDivider(paramCanvas, j);
    }
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt)
  {
    mDivider.setBounds(getPaddingLeft() + mDividerPadding, paramInt, getWidth() - getPaddingRight() - mDividerPadding, mDividerHeight + paramInt);
    mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt)
  {
    mDivider.setBounds(paramInt, getPaddingTop() + mDividerPadding, mDividerWidth + paramInt, getHeight() - getPaddingBottom() - mDividerPadding);
    mDivider.draw(paramCanvas);
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("layout:baselineAligned", mBaselineAligned);
    paramViewHierarchyEncoder.addProperty("layout:baselineAlignedChildIndex", mBaselineAlignedChildIndex);
    paramViewHierarchyEncoder.addProperty("measurement:baselineChildTop", mBaselineChildTop);
    paramViewHierarchyEncoder.addProperty("measurement:orientation", mOrientation);
    paramViewHierarchyEncoder.addProperty("measurement:gravity", mGravity);
    paramViewHierarchyEncoder.addProperty("measurement:totalLength", mTotalLength);
    paramViewHierarchyEncoder.addProperty("layout:totalLength", mTotalLength);
    paramViewHierarchyEncoder.addProperty("layout:useLargestChild", mUseLargestChild);
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    if (mOrientation == 0) {
      return new LayoutParams(-2, -2);
    }
    if (mOrientation == 1) {
      return new LayoutParams(-1, -2);
    }
    return null;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
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
  
  public CharSequence getAccessibilityClassName()
  {
    return LinearLayout.class.getName();
  }
  
  public int getBaseline()
  {
    if (mBaselineAlignedChildIndex < 0) {
      return super.getBaseline();
    }
    if (getChildCount() > mBaselineAlignedChildIndex)
    {
      View localView = getChildAt(mBaselineAlignedChildIndex);
      int i = localView.getBaseline();
      if (i == -1)
      {
        if (mBaselineAlignedChildIndex == 0) {
          return -1;
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      }
      int j = mBaselineChildTop;
      int k = j;
      if (mOrientation == 1)
      {
        int m = mGravity & 0x70;
        k = j;
        if (m != 48) {
          if (m != 16)
          {
            if (m != 80) {
              k = j;
            } else {
              k = mBottom - mTop - mPaddingBottom - mTotalLength;
            }
          }
          else {
            k = j + (mBottom - mTop - mPaddingTop - mPaddingBottom - mTotalLength) / 2;
          }
        }
      }
      return getLayoutParamstopMargin + k + i;
    }
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex()
  {
    return mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt)
  {
    return 0;
  }
  
  public Drawable getDividerDrawable()
  {
    return mDivider;
  }
  
  public int getDividerPadding()
  {
    return mDividerPadding;
  }
  
  public int getDividerWidth()
  {
    return mDividerWidth;
  }
  
  public int getGravity()
  {
    return mGravity;
  }
  
  int getLocationOffset(View paramView)
  {
    return 0;
  }
  
  int getNextLocationOffset(View paramView)
  {
    return 0;
  }
  
  public int getOrientation()
  {
    return mOrientation;
  }
  
  public int getShowDividers()
  {
    return mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt)
  {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount()
  {
    return getChildCount();
  }
  
  public float getWeightSum()
  {
    return mWeightSum;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt)
  {
    int i = getVirtualChildCount();
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (paramInt == i)
    {
      if ((mShowDividers & 0x4) != 0) {
        bool3 = true;
      }
      return bool3;
    }
    if (allViewsAreGoneBefore(paramInt))
    {
      bool3 = bool1;
      if ((mShowDividers & 0x1) != 0) {
        bool3 = true;
      }
      return bool3;
    }
    bool3 = bool2;
    if ((mShowDividers & 0x2) != 0) {
      bool3 = true;
    }
    return bool3;
  }
  
  public boolean isBaselineAligned()
  {
    return mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled()
  {
    return mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool1 = isLayoutRtl();
    int i = mPaddingTop;
    int j = paramInt4 - paramInt2;
    int k = mPaddingBottom;
    int m = mPaddingBottom;
    int n = getVirtualChildCount();
    int i1 = mGravity & 0x800007;
    int i2 = mGravity;
    boolean bool2 = mBaselineAligned;
    int[] arrayOfInt1 = mMaxAscent;
    int[] arrayOfInt2 = mMaxDescent;
    paramInt2 = Gravity.getAbsoluteGravity(i1, getLayoutDirection());
    if (paramInt2 != 1)
    {
      if (paramInt2 != 5) {}
      for (paramInt1 = mPaddingLeft;; paramInt1 = mPaddingLeft + paramInt3 - paramInt1 - mTotalLength) {
        break;
      }
    }
    paramInt1 = mPaddingLeft + (paramInt3 - paramInt1 - mTotalLength) / 2;
    paramInt4 = 0;
    int i3 = 1;
    if (bool1)
    {
      paramInt4 = n - 1;
      i3 = -1;
    }
    paramInt3 = 0;
    paramInt2 = paramInt1;
    while (paramInt3 < n)
    {
      int i4 = paramInt4 + i3 * paramInt3;
      View localView = getVirtualChildAt(i4);
      if (localView == null)
      {
        paramInt2 += measureNullChild(i4);
      }
      else if (localView.getVisibility() != 8)
      {
        int i5 = localView.getMeasuredWidth();
        int i6 = localView.getMeasuredHeight();
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if ((bool2) && (height != -1)) {
          i7 = localView.getBaseline();
        } else {
          i7 = -1;
        }
        int i8 = gravity;
        paramInt1 = i8;
        if (i8 < 0) {
          paramInt1 = i2 & 0x70;
        }
        paramInt1 &= 0x70;
        if (paramInt1 != 16)
        {
          if (paramInt1 != 48)
          {
            if (paramInt1 != 80)
            {
              paramInt1 = i;
            }
            else
            {
              i8 = j - k - i6 - bottomMargin;
              paramInt1 = i8;
              if (i7 != -1)
              {
                paramInt1 = localView.getMeasuredHeight();
                paramInt1 = i8 - (arrayOfInt2[2] - (paramInt1 - i7));
              }
            }
          }
          else
          {
            i8 = topMargin + i;
            paramInt1 = i8;
            if (i7 != -1) {
              paramInt1 = i8 + (arrayOfInt1[1] - i7);
            }
          }
        }
        else {
          paramInt1 = (j - i - m - i6) / 2 + i + topMargin - bottomMargin;
        }
        int i7 = paramInt2;
        if (hasDividerBeforeChildAt(i4)) {
          i7 = paramInt2 + mDividerWidth;
        }
        paramInt2 = i7 + leftMargin;
        setChildFrame(localView, paramInt2 + getLocationOffset(localView), paramInt1, i5, i6);
        paramInt2 += i5 + rightMargin + getNextLocationOffset(localView);
        paramInt3 += getChildrenSkipCount(localView, i4);
      }
      paramInt3++;
    }
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = mPaddingLeft;
    int j = paramInt3 - paramInt1;
    int k = mPaddingRight;
    int m = mPaddingRight;
    int n = getVirtualChildCount();
    int i1 = mGravity & 0x70;
    int i2 = mGravity;
    if (i1 != 16)
    {
      if (i1 != 80) {
        paramInt1 = mPaddingTop;
      } else {
        paramInt1 = mPaddingTop + paramInt4 - paramInt2 - mTotalLength;
      }
    }
    else {
      paramInt1 = mPaddingTop + (paramInt4 - paramInt2 - mTotalLength) / 2;
    }
    paramInt2 = 0;
    paramInt3 = i;
    paramInt4 = i1;
    for (;;)
    {
      i1 = paramInt2;
      if (i1 >= n) {
        break;
      }
      View localView = getVirtualChildAt(i1);
      if (localView == null)
      {
        paramInt1 += measureNullChild(i1);
      }
      else if (localView.getVisibility() != 8)
      {
        int i3 = localView.getMeasuredWidth();
        int i4 = localView.getMeasuredHeight();
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        int i5 = gravity;
        paramInt2 = i5;
        if (i5 < 0) {
          paramInt2 = i2 & 0x800007;
        }
        paramInt2 = Gravity.getAbsoluteGravity(paramInt2, getLayoutDirection()) & 0x7;
        if (paramInt2 != 1)
        {
          if (paramInt2 != 5) {
            paramInt2 = leftMargin + paramInt3;
          } else {
            paramInt2 = j - k - i3 - rightMargin;
          }
        }
        else {
          paramInt2 = (j - i - m - i3) / 2 + paramInt3 + leftMargin - rightMargin;
        }
        i5 = paramInt1;
        if (hasDividerBeforeChildAt(i1)) {
          i5 = paramInt1 + mDividerHeight;
        }
        paramInt1 = i5 + topMargin;
        setChildFrame(localView, paramInt2, paramInt1 + getLocationOffset(localView), i3, i4);
        paramInt2 = bottomMargin;
        i5 = getNextLocationOffset(localView);
        i1 += getChildrenSkipCount(localView, i1);
        paramInt1 += i4 + paramInt2 + i5;
      }
      paramInt2 = i1 + 1;
    }
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2)
  {
    mTotalLength = 0;
    int i = 0;
    float f1 = 0.0F;
    int j = getVirtualChildCount();
    int k = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    if ((mMaxAscent == null) || (mMaxDescent == null))
    {
      mMaxAscent = new int[4];
      mMaxDescent = new int[4];
    }
    int[] arrayOfInt1 = mMaxAscent;
    int[] arrayOfInt2 = mMaxDescent;
    int n = 0;
    arrayOfInt1[3] = -1;
    arrayOfInt1[2] = -1;
    arrayOfInt1[1] = -1;
    arrayOfInt1[0] = -1;
    arrayOfInt2[3] = -1;
    arrayOfInt2[2] = -1;
    arrayOfInt2[1] = -1;
    arrayOfInt2[0] = -1;
    boolean bool1 = mBaselineAligned;
    int i1 = 0;
    boolean bool2 = mUseLargestChild;
    int i2;
    if (k == 1073741824) {
      i2 = 1;
    } else {
      i2 = 0;
    }
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    int i9 = 1;
    int i10 = Integer.MIN_VALUE;
    Object localObject1;
    Object localObject2;
    while (i8 < j)
    {
      View localView = getVirtualChildAt(i8);
      if (localView == null)
      {
        mTotalLength += measureNullChild(i8);
      }
      else if (localView.getVisibility() == 8)
      {
        i8 += getChildrenSkipCount(localView, i8);
      }
      else
      {
        int i11 = i4 + 1;
        if (hasDividerBeforeChildAt(i8)) {
          mTotalLength += mDividerWidth;
        }
        localObject1 = (LayoutParams)localView.getLayoutParams();
        f1 += weight;
        if ((width == 0) && (weight > 0.0F)) {
          i4 = 1;
        } else {
          i4 = 0;
        }
        if ((k == 1073741824) && (i4 != 0))
        {
          if (i2 != 0)
          {
            mTotalLength += leftMargin + rightMargin;
          }
          else
          {
            i4 = mTotalLength;
            mTotalLength = Math.max(i4, leftMargin + i4 + rightMargin);
          }
          if (bool1)
          {
            i4 = View.MeasureSpec.makeSafeMeasureSpec(View.MeasureSpec.getSize(paramInt1), 0);
            localView.measure(i4, View.MeasureSpec.makeSafeMeasureSpec(View.MeasureSpec.getSize(paramInt2), 0));
          }
          for (;;)
          {
            i4 = i8;
            break;
            i1 = 1;
          }
        }
        if (i4 != 0) {
          width = -2;
        }
        if (f1 == 0.0F) {
          i12 = mTotalLength;
        } else {
          i12 = 0;
        }
        measureChildBeforeLayout(localView, i8, paramInt1, i12, paramInt2, 0);
        i12 = localView.getMeasuredWidth();
        if (i4 != 0)
        {
          width = 0;
          i3 += i12;
        }
        localObject2 = localObject1;
        if (i2 != 0)
        {
          mTotalLength += leftMargin + i12 + rightMargin + getNextLocationOffset(localView);
        }
        else
        {
          i4 = mTotalLength;
          mTotalLength = Math.max(i4, i4 + i12 + leftMargin + rightMargin + getNextLocationOffset(localView));
        }
        if (bool2)
        {
          i10 = Math.max(i12, i10);
          i4 = i8;
        }
        else
        {
          i4 = i8;
        }
        i12 = i;
        i13 = i6;
        i = 0;
        i14 = i;
        i6 = n;
        if (m != 1073741824)
        {
          i14 = i;
          i6 = n;
          if (height == -1)
          {
            i6 = 1;
            i14 = 1;
          }
        }
        i8 = topMargin + bottomMargin;
        n = localView.getMeasuredHeight() + i8;
        i5 = combineMeasuredStates(i5, localView.getMeasuredState());
        if (bool1)
        {
          int i15 = localView.getBaseline();
          if (i15 != -1)
          {
            if (gravity < 0) {
              i = mGravity;
            } else {
              i = gravity;
            }
            i = ((i & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
            arrayOfInt1[i] = Math.max(arrayOfInt1[i], i15);
            arrayOfInt2[i] = Math.max(arrayOfInt2[i], n - i15);
          }
        }
        i7 = Math.max(i7, n);
        if ((i9 != 0) && (height == -1)) {
          i = 1;
        } else {
          i = 0;
        }
        if (weight > 0.0F)
        {
          if (i14 == 0) {
            i8 = n;
          }
          i8 = Math.max(i13, i8);
          i9 = i12;
        }
        else
        {
          if (i14 == 0) {
            i8 = n;
          }
          i9 = Math.max(i12, i8);
          i8 = i13;
        }
        i12 = getChildrenSkipCount(localView, i4);
        n = i;
        i = i9;
        i12 = i4 + i12;
        i4 = i11;
        i9 = n;
        n = i6;
        i6 = i8;
        i8 = i12;
      }
      i8++;
    }
    int i12 = i6;
    i6 = i7;
    if ((i4 > 0) && (hasDividerBeforeChildAt(j))) {
      mTotalLength += mDividerWidth;
    }
    if ((arrayOfInt1[1] == -1) && (arrayOfInt1[0] == -1) && (arrayOfInt1[2] == -1) && (arrayOfInt1[3] == -1)) {
      break label1034;
    }
    i6 = Math.max(i6, Math.max(arrayOfInt1[3], Math.max(arrayOfInt1[0], Math.max(arrayOfInt1[1], arrayOfInt1[2]))) + Math.max(arrayOfInt2[3], Math.max(arrayOfInt2[0], Math.max(arrayOfInt2[1], arrayOfInt2[2]))));
    label1034:
    if ((bool2) && ((k == Integer.MIN_VALUE) || (k == 0)))
    {
      mTotalLength = 0;
      for (i8 = 0; i8 < j; i8++)
      {
        localObject2 = getVirtualChildAt(i8);
        if (localObject2 == null)
        {
          mTotalLength += measureNullChild(i8);
        }
        else if (((View)localObject2).getVisibility() == 8)
        {
          i8 += getChildrenSkipCount((View)localObject2, i8);
        }
        else
        {
          localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
          if (i2 != 0)
          {
            mTotalLength += leftMargin + i10 + rightMargin + getNextLocationOffset((View)localObject2);
          }
          else
          {
            i7 = mTotalLength;
            mTotalLength = Math.max(i7, i7 + i10 + leftMargin + rightMargin + getNextLocationOffset((View)localObject2));
          }
        }
      }
    }
    mTotalLength += mPaddingLeft + mPaddingRight;
    i8 = Math.max(mTotalLength, getSuggestedMinimumWidth());
    i4 = i10;
    int i14 = resolveSizeAndState(i8, paramInt1, 0);
    i10 = i14 & 0xFFFFFF;
    i8 = mTotalLength;
    if (mAllowInconsistentMeasurement) {
      i3 = 0;
    }
    int i13 = i10 - i8 + i3;
    float f2;
    if ((i1 == 0) && (((!sRemeasureWeightedChildren) && (i13 == 0)) || (f1 <= 0.0F)))
    {
      i8 = Math.max(i, i12);
      i = i8;
      i3 = i10;
      i3 = i6;
      f2 = f1;
      if (bool2)
      {
        i = i8;
        i3 = i10;
        i3 = i6;
        f2 = f1;
        if (k != 1073741824) {
          for (i1 = 0;; i1++)
          {
            i = i8;
            i3 = i10;
            i3 = i6;
            f2 = f1;
            if (i1 >= j) {
              break;
            }
            localObject1 = getVirtualChildAt(i1);
            if ((localObject1 != null) && (((View)localObject1).getVisibility() != 8)) {
              if (getLayoutParamsweight > 0.0F) {
                ((View)localObject1).measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(((View)localObject1).getMeasuredHeight(), 1073741824));
              } else {}
            }
          }
        }
      }
      i6 = i3;
      i8 = i;
    }
    else
    {
      if (mWeightSum > 0.0F) {
        f1 = mWeightSum;
      }
      arrayOfInt1[3] = -1;
      arrayOfInt1[2] = -1;
      arrayOfInt1[1] = -1;
      arrayOfInt1[0] = -1;
      arrayOfInt2[3] = -1;
      arrayOfInt2[2] = -1;
      arrayOfInt2[1] = -1;
      arrayOfInt2[0] = -1;
      i1 = -1;
      mTotalLength = 0;
      i8 = i;
      i6 = i5;
      i5 = 0;
      i3 = j;
      i7 = i12;
      i = i13;
      i10 = i4;
      for (j = i5; j < i3; j++)
      {
        localObject1 = getVirtualChildAt(j);
        if ((localObject1 != null) && (((View)localObject1).getVisibility() != 8))
        {
          localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
          f2 = weight;
          if (f2 > 0.0F)
          {
            i4 = (int)(i * f2 / f1);
            f1 -= f2;
            if ((mUseLargestChild) && (k != 1073741824)) {
              i5 = i10;
            }
            for (;;)
            {
              break;
              if ((width == 0) && ((!mAllowInconsistentMeasurement) || (k == 1073741824))) {
                i5 = i4;
              } else {
                i5 = ((View)localObject1).getMeasuredWidth() + i4;
              }
            }
            ((View)localObject1).measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, i5), 1073741824), getChildMeasureSpec(paramInt2, mPaddingTop + mPaddingBottom + topMargin + bottomMargin, height));
            i6 = combineMeasuredStates(i6, ((View)localObject1).getMeasuredState() & 0xFF000000);
            i -= i4;
          }
          if (i2 != 0)
          {
            mTotalLength += ((View)localObject1).getMeasuredWidth() + leftMargin + rightMargin + getNextLocationOffset((View)localObject1);
          }
          else
          {
            i5 = mTotalLength;
            mTotalLength = Math.max(i5, ((View)localObject1).getMeasuredWidth() + i5 + leftMargin + rightMargin + getNextLocationOffset((View)localObject1));
          }
          if ((m != 1073741824) && (height == -1)) {
            i5 = 1;
          } else {
            i5 = 0;
          }
          i13 = topMargin + bottomMargin;
          i12 = ((View)localObject1).getMeasuredHeight() + i13;
          i4 = Math.max(i1, i12);
          if (i5 != 0) {
            i1 = i13;
          } else {
            i1 = i12;
          }
          i1 = Math.max(i8, i1);
          if ((i9 != 0) && (height == -1)) {
            i8 = 1;
          } else {
            i8 = 0;
          }
          if (bool1)
          {
            i5 = ((View)localObject1).getBaseline();
            if (i5 != -1)
            {
              if (gravity < 0) {
                i9 = mGravity;
              } else {
                i9 = gravity;
              }
              i9 = ((i9 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
              arrayOfInt1[i9] = Math.max(arrayOfInt1[i9], i5);
              arrayOfInt2[i9] = Math.max(arrayOfInt2[i9], i12 - i5);
            }
            else {}
          }
          i9 = i8;
          i8 = i1;
          i1 = i4;
        }
      }
      mTotalLength += mPaddingLeft + mPaddingRight;
      if ((arrayOfInt1[1] == -1) && (arrayOfInt1[0] == -1) && (arrayOfInt1[2] == -1))
      {
        i = i1;
        if (arrayOfInt1[3] == -1) {}
      }
      else
      {
        i = Math.max(i1, Math.max(arrayOfInt1[3], Math.max(arrayOfInt1[0], Math.max(arrayOfInt1[1], arrayOfInt1[2]))) + Math.max(arrayOfInt2[3], Math.max(arrayOfInt2[0], Math.max(arrayOfInt2[1], arrayOfInt2[2]))));
      }
      j = i3;
      i5 = i6;
      i6 = i;
    }
    i = i6;
    if (i9 == 0)
    {
      i = i6;
      if (m != 1073741824) {
        i = i8;
      }
    }
    setMeasuredDimension(i5 & 0xFF000000 | i14, resolveSizeAndState(Math.max(i + (mPaddingTop + mPaddingBottom), getSuggestedMinimumHeight()), paramInt2, i5 << 16));
    if (n != 0) {
      forceUniformHeight(j, paramInt1);
    }
  }
  
  int measureNullChild(int paramInt)
  {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2)
  {
    mTotalLength = 0;
    float f1 = 0.0F;
    int i = getVirtualChildCount();
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = 0;
    int n = mBaselineAlignedChildIndex;
    boolean bool = mUseLargestChild;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    int i9 = 1;
    int i10 = Integer.MIN_VALUE;
    Object localObject1;
    Object localObject2;
    while (i6 < i)
    {
      localObject1 = getVirtualChildAt(i6);
      if (localObject1 == null)
      {
        mTotalLength += measureNullChild(i6);
      }
      else if (((View)localObject1).getVisibility() == 8)
      {
        i6 += getChildrenSkipCount((View)localObject1, i6);
      }
      else
      {
        int i11 = i2 + 1;
        if (hasDividerBeforeChildAt(i6)) {
          mTotalLength += mDividerHeight;
        }
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        f1 += weight;
        if ((height == 0) && (weight > 0.0F)) {
          i12 = 1;
        } else {
          i12 = 0;
        }
        if ((k == 1073741824) && (i12 != 0))
        {
          m = mTotalLength;
          mTotalLength = Math.max(m, topMargin + m + bottomMargin);
          m = 1;
        }
        else
        {
          if (i12 != 0) {
            height = -2;
          }
          if (f1 == 0.0F) {
            i13 = mTotalLength;
          } else {
            i13 = 0;
          }
          i2 = m;
          Object localObject3 = localObject1;
          i14 = i10;
          measureChildBeforeLayout((View)localObject1, i6, paramInt1, 0, paramInt2, i13);
          i13 = localObject3.getMeasuredHeight();
          if (i12 != 0)
          {
            height = 0;
            i12 = i1 + i13;
          }
          else
          {
            i12 = i1;
          }
          Object localObject4 = localObject2;
          i10 = mTotalLength;
          mTotalLength = Math.max(i10, i10 + i13 + topMargin + bottomMargin + getNextLocationOffset(localObject3));
          i10 = i14;
          i1 = i12;
          m = i2;
          if (bool)
          {
            i10 = Math.max(i13, i14);
            m = i2;
            i1 = i12;
          }
        }
        if ((n >= 0) && (n == i6 + 1)) {
          mBaselineChildTop = mTotalLength;
        }
        i14 = i6;
        if ((i14 < n) && (weight > 0.0F)) {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        }
        i6 = 0;
        if ((j != 1073741824) && (width == -1))
        {
          i3 = 1;
          i6 = 1;
        }
        i12 = leftMargin + rightMargin;
        i2 = ((View)localObject1).getMeasuredWidth() + i12;
        int i13 = Math.max(i5, i2);
        i4 = combineMeasuredStates(i4, ((View)localObject1).getMeasuredState());
        if ((i9 != 0) && (width == -1)) {
          i5 = 1;
        } else {
          i5 = 0;
        }
        if (weight > 0.0F)
        {
          if (i6 == 0) {
            i12 = i2;
          }
          i9 = Math.max(i8, i12);
          i8 = i7;
        }
        else
        {
          i9 = i8;
          if (i6 != 0) {
            i2 = i12;
          }
          i8 = Math.max(i7, i2);
        }
        i14 += getChildrenSkipCount((View)localObject1, i14);
        i12 = i9;
        i6 = i13;
        i2 = i11;
        i9 = i5;
        i7 = i8;
        i8 = i12;
        i5 = i6;
        i6 = i14;
      }
      i6++;
    }
    int i12 = i8;
    i6 = i5;
    i5 = i4;
    if ((i2 > 0) && (hasDividerBeforeChildAt(i))) {
      mTotalLength += mDividerHeight;
    }
    if (bool)
    {
      i8 = k;
      if ((i8 != Integer.MIN_VALUE) && (i8 != 0)) {
        break label889;
      }
      mTotalLength = 0;
      for (i8 = 0; i8 < i; i8++)
      {
        localObject1 = getVirtualChildAt(i8);
        if (localObject1 == null)
        {
          mTotalLength += measureNullChild(i8);
        }
        else
        {
          if (((View)localObject1).getVisibility() != 8) {
            break label831;
          }
          i8 += getChildrenSkipCount((View)localObject1, i8);
        }
        continue;
        label831:
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        i4 = mTotalLength;
        mTotalLength = Math.max(i4, i4 + i10 + topMargin + bottomMargin + getNextLocationOffset((View)localObject1));
      }
    }
    label889:
    mTotalLength += mPaddingTop + mPaddingBottom;
    int i14 = resolveSizeAndState(Math.max(mTotalLength, getSuggestedMinimumHeight()), paramInt2, 0);
    i8 = i14 & 0xFFFFFF;
    i4 = mTotalLength;
    if (mAllowInconsistentMeasurement) {
      i1 = 0;
    }
    i1 = i8 - i4 + i1;
    float f2;
    if ((m == 0) && (((!sRemeasureWeightedChildren) && (i1 == 0)) || (f1 <= 0.0F)))
    {
      m = Math.max(i7, i12);
      i7 = i8;
      i7 = i1;
      i7 = i12;
      f2 = f1;
      if (bool)
      {
        i7 = i8;
        i7 = i1;
        i7 = i12;
        f2 = f1;
        if (k != 1073741824) {
          for (k = 0;; k++)
          {
            i7 = i8;
            i7 = i1;
            i7 = i12;
            f2 = f1;
            if (k >= i) {
              break;
            }
            localObject2 = getVirtualChildAt(k);
            if ((localObject2 != null) && (((View)localObject2).getVisibility() != 8)) {
              if (getLayoutParamsweight > 0.0F) {
                ((View)localObject2).measure(View.MeasureSpec.makeMeasureSpec(((View)localObject2).getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i10, 1073741824));
              } else {}
            }
          }
        }
      }
      i10 = i;
      i1 = i5;
      i5 = m;
    }
    else
    {
      if (mWeightSum > 0.0F) {
        f1 = mWeightSum;
      }
      mTotalLength = 0;
      i8 = i7;
      i4 = i5;
      i12 = 0;
      i7 = n;
      m = k;
      i5 = i8;
      i8 = i1;
      i1 = i4;
      for (i4 = i12; i4 < i; i4++)
      {
        localObject2 = getVirtualChildAt(i4);
        if ((localObject2 != null) && (((View)localObject2).getVisibility() != 8))
        {
          localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
          f2 = weight;
          if (f2 > 0.0F)
          {
            i12 = (int)(i8 * f2 / f1);
            if ((mUseLargestChild) && (m != 1073741824)) {
              k = i10;
            }
            for (;;)
            {
              break;
              if ((height == 0) && ((!mAllowInconsistentMeasurement) || (m == 1073741824))) {
                k = i12;
              } else {
                k = ((View)localObject2).getMeasuredHeight() + i12;
              }
            }
            i8 -= i12;
            k = View.MeasureSpec.makeMeasureSpec(Math.max(0, k), 1073741824);
            ((View)localObject2).measure(getChildMeasureSpec(paramInt1, mPaddingLeft + mPaddingRight + leftMargin + rightMargin, width), k);
            i1 = combineMeasuredStates(i1, ((View)localObject2).getMeasuredState() & 0xFF00);
            f1 -= f2;
          }
          i12 = leftMargin + rightMargin;
          i2 = ((View)localObject2).getMeasuredWidth() + i12;
          i6 = Math.max(i6, i2);
          if ((j != 1073741824) && (width == -1)) {
            k = 1;
          } else {
            k = 0;
          }
          if (k != 0) {
            k = i12;
          } else {
            k = i2;
          }
          k = Math.max(i5, k);
          if ((i9 != 0) && (width == -1)) {
            i5 = 1;
          } else {
            i5 = 0;
          }
          i9 = mTotalLength;
          mTotalLength = Math.max(i9, i9 + ((View)localObject2).getMeasuredHeight() + topMargin + bottomMargin + getNextLocationOffset((View)localObject2));
          i9 = i5;
          i5 = k;
        }
      }
      i10 = i;
      mTotalLength += mPaddingTop + mPaddingBottom;
    }
    i8 = i6;
    if (i9 == 0)
    {
      i8 = i6;
      if (j != 1073741824) {
        i8 = i5;
      }
    }
    setMeasuredDimension(resolveSizeAndState(Math.max(i8 + (mPaddingLeft + mPaddingRight), getSuggestedMinimumWidth()), paramInt1, i1), i14);
    if (i3 != 0) {
      forceUniformWidth(i10, paramInt2);
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (mDivider == null) {
      return;
    }
    if (mOrientation == 1) {
      drawDividersVertical(paramCanvas);
    } else {
      drawDividersHorizontal(paramCanvas);
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
    } else {
      measureHorizontal(paramInt1, paramInt2);
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    if (paramInt != mLayoutDirection)
    {
      mLayoutDirection = paramInt;
      if (mOrientation == 0) {
        requestLayout();
      }
    }
  }
  
  @RemotableViewMethod
  public void setBaselineAligned(boolean paramBoolean)
  {
    mBaselineAligned = paramBoolean;
  }
  
  @RemotableViewMethod
  public void setBaselineAlignedChildIndex(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < getChildCount()))
    {
      mBaselineAlignedChildIndex = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("base aligned child index out of range (0, ");
    localStringBuilder.append(getChildCount());
    localStringBuilder.append(")");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setDividerDrawable(Drawable paramDrawable)
  {
    if (paramDrawable == mDivider) {
      return;
    }
    mDivider = paramDrawable;
    if (paramDrawable != null)
    {
      mDividerWidth = paramDrawable.getIntrinsicWidth();
      mDividerHeight = paramDrawable.getIntrinsicHeight();
    }
    else
    {
      mDividerWidth = 0;
      mDividerHeight = 0;
    }
    setWillNotDraw(isShowingDividers() ^ true);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt)
  {
    if (paramInt == mDividerPadding) {
      return;
    }
    mDividerPadding = paramInt;
    if (isShowingDividers())
    {
      requestLayout();
      invalidate();
    }
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
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean)
  {
    mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt)
  {
    if (mOrientation != paramInt)
    {
      mOrientation = paramInt;
      requestLayout();
    }
  }
  
  public void setShowDividers(int paramInt)
  {
    if (paramInt == mShowDividers) {
      return;
    }
    mShowDividers = paramInt;
    setWillNotDraw(isShowingDividers() ^ true);
    requestLayout();
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
  
  @RemotableViewMethod
  public void setWeightSum(float paramFloat)
  {
    mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DividerMode {}
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    @ViewDebug.ExportedProperty(category="layout", mapping={@android.view.ViewDebug.IntToString(from=-1, to="NONE"), @android.view.ViewDebug.IntToString(from=0, to="NONE"), @android.view.ViewDebug.IntToString(from=48, to="TOP"), @android.view.ViewDebug.IntToString(from=80, to="BOTTOM"), @android.view.ViewDebug.IntToString(from=3, to="LEFT"), @android.view.ViewDebug.IntToString(from=5, to="RIGHT"), @android.view.ViewDebug.IntToString(from=8388611, to="START"), @android.view.ViewDebug.IntToString(from=8388613, to="END"), @android.view.ViewDebug.IntToString(from=16, to="CENTER_VERTICAL"), @android.view.ViewDebug.IntToString(from=112, to="FILL_VERTICAL"), @android.view.ViewDebug.IntToString(from=1, to="CENTER_HORIZONTAL"), @android.view.ViewDebug.IntToString(from=7, to="FILL_HORIZONTAL"), @android.view.ViewDebug.IntToString(from=17, to="CENTER"), @android.view.ViewDebug.IntToString(from=119, to="FILL")})
    public int gravity = -1;
    @ViewDebug.ExportedProperty(category="layout")
    public float weight;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      weight = 0.0F;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, float paramFloat)
    {
      super(paramInt2);
      weight = paramFloat;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.LinearLayout_Layout);
      weight = paramContext.getFloat(3, 0.0F);
      gravity = paramContext.getInt(0, -1);
      paramContext.recycle();
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
      weight = weight;
      gravity = gravity;
    }
    
    public String debug(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("LinearLayout.LayoutParams={width=");
      localStringBuilder.append(sizeToString(width));
      localStringBuilder.append(", height=");
      localStringBuilder.append(sizeToString(height));
      localStringBuilder.append(" weight=");
      localStringBuilder.append(weight);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      super.encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.addProperty("layout:weight", weight);
      paramViewHierarchyEncoder.addProperty("layout:gravity", gravity);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OrientationMode {}
}
