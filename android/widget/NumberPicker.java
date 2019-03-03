package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R.styleable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import libcore.icu.LocaleData;

public class NumberPicker
  extends LinearLayout
{
  private static final int DEFAULT_LAYOUT_RESOURCE_ID = 17367237;
  private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300L;
  private static final char[] DIGIT_CHARACTERS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 1776, 1777, 1778, 1779, 1780, 1781, 1782, 1783, 1784, 1785, 2406, 2407, 2408, 2409, 2410, 2411, 2412, 2413, 2414, 2415, 2534, 2535, 2536, 2537, 2538, 2539, 2540, 2541, 2542, 2543, 3302, 3303, 3304, 3305, 3306, 3307, 3308, 3309, 3310, 3311 };
  private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 800;
  private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
  private static final int SELECTOR_MIDDLE_ITEM_INDEX = 1;
  private static final int SELECTOR_WHEEL_ITEM_COUNT = 3;
  private static final int SIZE_UNSPECIFIED = -1;
  private static final int SNAP_SCROLL_DURATION = 300;
  private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9F;
  private static final int UNSCALED_DEFAULT_SELECTION_DIVIDERS_DISTANCE = 48;
  private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
  private static final TwoDigitFormatter sTwoDigitFormatter = new TwoDigitFormatter();
  private AccessibilityNodeProviderImpl mAccessibilityNodeProvider;
  private final Scroller mAdjustScroller;
  private BeginSoftInputOnLongPressCommand mBeginSoftInputOnLongPressCommand;
  private int mBottomSelectionDividerBottom;
  private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
  private final boolean mComputeMaxWidth;
  private int mCurrentScrollOffset;
  private final ImageButton mDecrementButton;
  private boolean mDecrementVirtualButtonPressed;
  private String[] mDisplayedValues;
  private final Scroller mFlingScroller;
  private Formatter mFormatter;
  private final boolean mHasSelectorWheel;
  private boolean mHideWheelUntilFocused;
  private boolean mIgnoreMoveEvents;
  private final ImageButton mIncrementButton;
  private boolean mIncrementVirtualButtonPressed;
  private int mInitialScrollOffset = Integer.MIN_VALUE;
  private final EditText mInputText;
  private long mLastDownEventTime;
  private float mLastDownEventY;
  private float mLastDownOrMoveEventY;
  private int mLastHandledDownDpadKeyCode = -1;
  private int mLastHoveredChildVirtualViewId;
  private long mLongPressUpdateInterval = 300L;
  private final int mMaxHeight;
  private int mMaxValue;
  private int mMaxWidth;
  private int mMaximumFlingVelocity;
  private final int mMinHeight;
  private int mMinValue;
  private final int mMinWidth;
  private int mMinimumFlingVelocity;
  private OnScrollListener mOnScrollListener;
  private OnValueChangeListener mOnValueChangeListener;
  private boolean mPerformClickOnTap;
  private final PressedStateHelper mPressedStateHelper;
  private int mPreviousScrollerY;
  private int mScrollState = 0;
  private final Drawable mSelectionDivider;
  private final int mSelectionDividerHeight;
  private final int mSelectionDividersDistance;
  private int mSelectorElementHeight;
  private final SparseArray<String> mSelectorIndexToStringCache = new SparseArray();
  private final int[] mSelectorIndices = new int[3];
  private int mSelectorTextGapHeight;
  private final Paint mSelectorWheelPaint;
  private SetSelectionCommand mSetSelectionCommand;
  private final int mSolidColor;
  private final int mTextSize;
  private int mTopSelectionDividerTop;
  private int mTouchSlop;
  private int mValue;
  private VelocityTracker mVelocityTracker;
  private final Drawable mVirtualButtonPressedDrawable;
  private boolean mWrapSelectorWheel;
  private boolean mWrapSelectorWheelPreferred = true;
  
  public NumberPicker(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public NumberPicker(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16844068);
  }
  
  public NumberPicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public NumberPicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.NumberPicker, paramInt1, paramInt2);
    paramInt1 = paramAttributeSet.getResourceId(2, 17367237);
    boolean bool;
    if (paramInt1 != 17367237) {
      bool = true;
    } else {
      bool = false;
    }
    mHasSelectorWheel = bool;
    mHideWheelUntilFocused = paramAttributeSet.getBoolean(1, false);
    mSolidColor = paramAttributeSet.getColor(0, 0);
    Object localObject = paramAttributeSet.getDrawable(7);
    if (localObject != null)
    {
      ((Drawable)localObject).setCallback(this);
      ((Drawable)localObject).setLayoutDirection(getLayoutDirection());
      if (((Drawable)localObject).isStateful()) {
        ((Drawable)localObject).setState(getDrawableState());
      }
    }
    mSelectionDivider = ((Drawable)localObject);
    mSelectionDividerHeight = paramAttributeSet.getDimensionPixelSize(8, (int)TypedValue.applyDimension(1, 2.0F, getResources().getDisplayMetrics()));
    mSelectionDividersDistance = paramAttributeSet.getDimensionPixelSize(9, (int)TypedValue.applyDimension(1, 48.0F, getResources().getDisplayMetrics()));
    mMinHeight = paramAttributeSet.getDimensionPixelSize(5, -1);
    mMaxHeight = paramAttributeSet.getDimensionPixelSize(3, -1);
    if ((mMinHeight != -1) && (mMaxHeight != -1) && (mMinHeight > mMaxHeight)) {
      throw new IllegalArgumentException("minHeight > maxHeight");
    }
    mMinWidth = paramAttributeSet.getDimensionPixelSize(6, -1);
    mMaxWidth = paramAttributeSet.getDimensionPixelSize(4, -1);
    if ((mMinWidth != -1) && (mMaxWidth != -1) && (mMinWidth > mMaxWidth)) {
      throw new IllegalArgumentException("minWidth > maxWidth");
    }
    if (mMaxWidth == -1) {
      bool = true;
    } else {
      bool = false;
    }
    mComputeMaxWidth = bool;
    mVirtualButtonPressedDrawable = paramAttributeSet.getDrawable(10);
    paramAttributeSet.recycle();
    mPressedStateHelper = new PressedStateHelper();
    setWillNotDraw(mHasSelectorWheel ^ true);
    ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(paramInt1, this, true);
    localObject = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        NumberPicker.this.hideSoftInput();
        mInputText.clearFocus();
        if (paramAnonymousView.getId() == 16909034) {
          NumberPicker.this.changeValueByOne(true);
        } else {
          NumberPicker.this.changeValueByOne(false);
        }
      }
    };
    paramAttributeSet = new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramAnonymousView)
      {
        NumberPicker.this.hideSoftInput();
        mInputText.clearFocus();
        if (paramAnonymousView.getId() == 16909034) {
          NumberPicker.this.postChangeCurrentByOneFromLongPress(true, 0L);
        } else {
          NumberPicker.this.postChangeCurrentByOneFromLongPress(false, 0L);
        }
        return true;
      }
    };
    if (!mHasSelectorWheel)
    {
      mIncrementButton = ((ImageButton)findViewById(16909034));
      mIncrementButton.setOnClickListener((View.OnClickListener)localObject);
      mIncrementButton.setOnLongClickListener(paramAttributeSet);
    }
    else
    {
      mIncrementButton = null;
    }
    if (!mHasSelectorWheel)
    {
      mDecrementButton = ((ImageButton)findViewById(16908896));
      mDecrementButton.setOnClickListener((View.OnClickListener)localObject);
      mDecrementButton.setOnLongClickListener(paramAttributeSet);
    }
    else
    {
      mDecrementButton = null;
    }
    mInputText = ((EditText)findViewById(16909191));
    mInputText.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          mInputText.selectAll();
        }
        else
        {
          mInputText.setSelection(0, 0);
          NumberPicker.this.validateInputTextView(paramAnonymousView);
        }
      }
    });
    mInputText.setFilters(new InputFilter[] { new InputTextFilter() });
    mInputText.setAccessibilityLiveRegion(1);
    mInputText.setRawInputType(2);
    mInputText.setImeOptions(6);
    paramContext = ViewConfiguration.get(paramContext);
    mTouchSlop = paramContext.getScaledTouchSlop();
    mMinimumFlingVelocity = paramContext.getScaledMinimumFlingVelocity();
    mMaximumFlingVelocity = (paramContext.getScaledMaximumFlingVelocity() / 8);
    mTextSize = ((int)mInputText.getTextSize());
    paramContext = new Paint();
    paramContext.setAntiAlias(true);
    paramContext.setTextAlign(Paint.Align.CENTER);
    paramContext.setTextSize(mTextSize);
    paramContext.setTypeface(mInputText.getTypeface());
    paramContext.setColor(mInputText.getTextColors().getColorForState(ENABLED_STATE_SET, -1));
    mSelectorWheelPaint = paramContext;
    mFlingScroller = new Scroller(getContext(), null, true);
    mAdjustScroller = new Scroller(getContext(), new DecelerateInterpolator(2.5F));
    updateInputTextView();
    if (getImportantForAccessibility() == 0) {
      setImportantForAccessibility(1);
    }
    if (getFocusable() == 16)
    {
      setFocusable(1);
      setFocusableInTouchMode(true);
    }
  }
  
  private void changeValueByOne(boolean paramBoolean)
  {
    if (mHasSelectorWheel)
    {
      hideSoftInput();
      if (!moveToFinalScrollerPosition(mFlingScroller)) {
        moveToFinalScrollerPosition(mAdjustScroller);
      }
      mPreviousScrollerY = 0;
      if (paramBoolean) {
        mFlingScroller.startScroll(0, 0, 0, -mSelectorElementHeight, 300);
      } else {
        mFlingScroller.startScroll(0, 0, 0, mSelectorElementHeight, 300);
      }
      invalidate();
    }
    else if (paramBoolean)
    {
      setValueInternal(mValue + 1, true);
    }
    else
    {
      setValueInternal(mValue - 1, true);
    }
  }
  
  private void decrementSelectorIndices(int[] paramArrayOfInt)
  {
    for (int i = paramArrayOfInt.length - 1; i > 0; i--) {
      paramArrayOfInt[i] = paramArrayOfInt[(i - 1)];
    }
    int j = paramArrayOfInt[1] - 1;
    i = j;
    if (mWrapSelectorWheel)
    {
      i = j;
      if (j < mMinValue) {
        i = mMaxValue;
      }
    }
    paramArrayOfInt[0] = i;
    ensureCachedScrollSelectorValue(i);
  }
  
  private void ensureCachedScrollSelectorValue(int paramInt)
  {
    SparseArray localSparseArray = mSelectorIndexToStringCache;
    if ((String)localSparseArray.get(paramInt) != null) {
      return;
    }
    String str;
    if ((paramInt >= mMinValue) && (paramInt <= mMaxValue))
    {
      if (mDisplayedValues != null)
      {
        int i = mMinValue;
        str = mDisplayedValues[(paramInt - i)];
      }
      else
      {
        str = formatNumber(paramInt);
      }
    }
    else {
      str = "";
    }
    localSparseArray.put(paramInt, str);
  }
  
  private boolean ensureScrollWheelAdjusted()
  {
    int i = mInitialScrollOffset - mCurrentScrollOffset;
    if (i != 0)
    {
      mPreviousScrollerY = 0;
      int j = i;
      if (Math.abs(i) > mSelectorElementHeight / 2)
      {
        if (i > 0) {
          j = -mSelectorElementHeight;
        } else {
          j = mSelectorElementHeight;
        }
        j = i + j;
      }
      mAdjustScroller.startScroll(0, 0, 0, j, 800);
      invalidate();
      return true;
    }
    return false;
  }
  
  private void fling(int paramInt)
  {
    mPreviousScrollerY = 0;
    if (paramInt > 0) {
      mFlingScroller.fling(0, 0, 0, paramInt, 0, 0, 0, Integer.MAX_VALUE);
    } else {
      mFlingScroller.fling(0, Integer.MAX_VALUE, 0, paramInt, 0, 0, 0, Integer.MAX_VALUE);
    }
    invalidate();
  }
  
  private String formatNumber(int paramInt)
  {
    String str;
    if (mFormatter != null) {
      str = mFormatter.format(paramInt);
    } else {
      str = formatNumberWithLocale(paramInt);
    }
    return str;
  }
  
  private static String formatNumberWithLocale(int paramInt)
  {
    return String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(paramInt) });
  }
  
  private int getSelectedPos(String paramString)
  {
    int i;
    if (mDisplayedValues == null)
    {
      try
      {
        i = Integer.parseInt(paramString);
        return i;
      }
      catch (NumberFormatException paramString) {}
    }
    else
    {
      for (i = 0; i < mDisplayedValues.length; i++)
      {
        paramString = paramString.toLowerCase();
        if (mDisplayedValues[i].toLowerCase().startsWith(paramString)) {
          return mMinValue + i;
        }
      }
      try
      {
        i = Integer.parseInt(paramString);
        return i;
      }
      catch (NumberFormatException paramString) {}
    }
    return mMinValue;
  }
  
  public static final Formatter getTwoDigitFormatter()
  {
    return sTwoDigitFormatter;
  }
  
  private int getWrappedSelectorIndex(int paramInt)
  {
    if (paramInt > mMaxValue) {
      return mMinValue + (paramInt - mMaxValue) % (mMaxValue - mMinValue) - 1;
    }
    if (paramInt < mMinValue) {
      return mMaxValue - (mMinValue - paramInt) % (mMaxValue - mMinValue) + 1;
    }
    return paramInt;
  }
  
  private void hideSoftInput()
  {
    InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
    if ((localInputMethodManager != null) && (localInputMethodManager.isActive(mInputText))) {
      localInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
    }
    if (mHasSelectorWheel) {
      mInputText.setVisibility(4);
    }
  }
  
  private void incrementSelectorIndices(int[] paramArrayOfInt)
  {
    for (int i = 0; i < paramArrayOfInt.length - 1; i++) {
      paramArrayOfInt[i] = paramArrayOfInt[(i + 1)];
    }
    int j = paramArrayOfInt[(paramArrayOfInt.length - 2)] + 1;
    i = j;
    if (mWrapSelectorWheel)
    {
      i = j;
      if (j > mMaxValue) {
        i = mMinValue;
      }
    }
    paramArrayOfInt[(paramArrayOfInt.length - 1)] = i;
    ensureCachedScrollSelectorValue(i);
  }
  
  private void initializeFadingEdges()
  {
    setVerticalFadingEdgeEnabled(true);
    setFadingEdgeLength((mBottom - mTop - mTextSize) / 2);
  }
  
  private void initializeSelectorWheel()
  {
    initializeSelectorWheelIndices();
    int[] arrayOfInt = mSelectorIndices;
    int i = arrayOfInt.length;
    int j = mTextSize;
    mSelectorTextGapHeight = ((int)((mBottom - mTop - i * j) / arrayOfInt.length + 0.5F));
    mSelectorElementHeight = (mTextSize + mSelectorTextGapHeight);
    mInitialScrollOffset = (mInputText.getBaseline() + mInputText.getTop() - mSelectorElementHeight * 1);
    mCurrentScrollOffset = mInitialScrollOffset;
    updateInputTextView();
  }
  
  private void initializeSelectorWheelIndices()
  {
    mSelectorIndexToStringCache.clear();
    int[] arrayOfInt = mSelectorIndices;
    int i = getValue();
    for (int j = 0; j < mSelectorIndices.length; j++)
    {
      int k = j - 1 + i;
      int m = k;
      if (mWrapSelectorWheel) {
        m = getWrappedSelectorIndex(k);
      }
      arrayOfInt[j] = m;
      ensureCachedScrollSelectorValue(arrayOfInt[j]);
    }
  }
  
  private int makeMeasureSpec(int paramInt1, int paramInt2)
  {
    if (paramInt2 == -1) {
      return paramInt1;
    }
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt1);
    if (j != Integer.MIN_VALUE)
    {
      if (j != 0)
      {
        if (j == 1073741824) {
          return paramInt1;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown measure mode: ");
        localStringBuilder.append(j);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      return View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    }
    return View.MeasureSpec.makeMeasureSpec(Math.min(i, paramInt2), 1073741824);
  }
  
  private boolean moveToFinalScrollerPosition(Scroller paramScroller)
  {
    paramScroller.forceFinished(true);
    int i = paramScroller.getFinalY() - paramScroller.getCurrY();
    int j = mCurrentScrollOffset;
    int k = mSelectorElementHeight;
    k = mInitialScrollOffset - (j + i) % k;
    if (k != 0)
    {
      j = k;
      if (Math.abs(k) > mSelectorElementHeight / 2) {
        if (k > 0) {
          j = k - mSelectorElementHeight;
        } else {
          j = k + mSelectorElementHeight;
        }
      }
      scrollBy(0, i + j);
      return true;
    }
    return false;
  }
  
  private void notifyChange(int paramInt1, int paramInt2)
  {
    if (mOnValueChangeListener != null) {
      mOnValueChangeListener.onValueChange(this, paramInt1, mValue);
    }
  }
  
  private void onScrollStateChange(int paramInt)
  {
    if (mScrollState == paramInt) {
      return;
    }
    mScrollState = paramInt;
    if (mOnScrollListener != null) {
      mOnScrollListener.onScrollStateChange(this, paramInt);
    }
  }
  
  private void onScrollerFinished(Scroller paramScroller)
  {
    if (paramScroller == mFlingScroller)
    {
      ensureScrollWheelAdjusted();
      updateInputTextView();
      onScrollStateChange(0);
    }
    else if (mScrollState != 1)
    {
      updateInputTextView();
    }
  }
  
  private void postBeginSoftInputOnLongPressCommand()
  {
    if (mBeginSoftInputOnLongPressCommand == null) {
      mBeginSoftInputOnLongPressCommand = new BeginSoftInputOnLongPressCommand();
    } else {
      removeCallbacks(mBeginSoftInputOnLongPressCommand);
    }
    postDelayed(mBeginSoftInputOnLongPressCommand, ViewConfiguration.getLongPressTimeout());
  }
  
  private void postChangeCurrentByOneFromLongPress(boolean paramBoolean, long paramLong)
  {
    if (mChangeCurrentByOneFromLongPressCommand == null) {
      mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
    } else {
      removeCallbacks(mChangeCurrentByOneFromLongPressCommand);
    }
    mChangeCurrentByOneFromLongPressCommand.setStep(paramBoolean);
    postDelayed(mChangeCurrentByOneFromLongPressCommand, paramLong);
  }
  
  private void postSetSelectionCommand(int paramInt1, int paramInt2)
  {
    if (mSetSelectionCommand == null) {
      mSetSelectionCommand = new SetSelectionCommand(mInputText);
    }
    mSetSelectionCommand.post(paramInt1, paramInt2);
  }
  
  private void removeAllCallbacks()
  {
    if (mChangeCurrentByOneFromLongPressCommand != null) {
      removeCallbacks(mChangeCurrentByOneFromLongPressCommand);
    }
    if (mSetSelectionCommand != null) {
      mSetSelectionCommand.cancel();
    }
    if (mBeginSoftInputOnLongPressCommand != null) {
      removeCallbacks(mBeginSoftInputOnLongPressCommand);
    }
    mPressedStateHelper.cancel();
  }
  
  private void removeBeginSoftInputCommand()
  {
    if (mBeginSoftInputOnLongPressCommand != null) {
      removeCallbacks(mBeginSoftInputOnLongPressCommand);
    }
  }
  
  private void removeChangeCurrentByOneFromLongPress()
  {
    if (mChangeCurrentByOneFromLongPressCommand != null) {
      removeCallbacks(mChangeCurrentByOneFromLongPressCommand);
    }
  }
  
  private int resolveSizeAndStateRespectingMinSize(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 != -1) {
      return resolveSizeAndState(Math.max(paramInt1, paramInt2), paramInt3, 0);
    }
    return paramInt2;
  }
  
  private void setValueInternal(int paramInt, boolean paramBoolean)
  {
    if (mValue == paramInt) {
      return;
    }
    if (mWrapSelectorWheel) {
      paramInt = getWrappedSelectorIndex(paramInt);
    } else {
      paramInt = Math.min(Math.max(paramInt, mMinValue), mMaxValue);
    }
    int i = mValue;
    mValue = paramInt;
    if (mScrollState != 2) {
      updateInputTextView();
    }
    if (paramBoolean) {
      notifyChange(i, paramInt);
    }
    initializeSelectorWheelIndices();
    invalidate();
  }
  
  private void showSoftInput()
  {
    InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
    if (localInputMethodManager != null)
    {
      if (mHasSelectorWheel) {
        mInputText.setVisibility(0);
      }
      mInputText.requestFocus();
      localInputMethodManager.showSoftInput(mInputText, 0);
    }
  }
  
  private void tryComputeMaxWidth()
  {
    if (!mComputeMaxWidth) {
      return;
    }
    int i = 0;
    String[] arrayOfString = mDisplayedValues;
    int j = 0;
    int k = 0;
    float f1;
    if (arrayOfString == null)
    {
      f1 = 0.0F;
      i = k;
      while (i <= 9)
      {
        float f2 = mSelectorWheelPaint.measureText(formatNumberWithLocale(i));
        float f3 = f1;
        if (f2 > f1) {
          f3 = f2;
        }
        i++;
        f1 = f3;
      }
      j = 0;
      i = mMaxValue;
      while (i > 0)
      {
        j++;
        i /= 10;
      }
      k = (int)(j * f1);
    }
    else
    {
      int m = mDisplayedValues.length;
      for (;;)
      {
        k = i;
        if (j >= m) {
          break;
        }
        f1 = mSelectorWheelPaint.measureText(mDisplayedValues[j]);
        k = i;
        if (f1 > i) {
          k = (int)f1;
        }
        j++;
        i = k;
      }
    }
    i = k + (mInputText.getPaddingLeft() + mInputText.getPaddingRight());
    if (mMaxWidth != i)
    {
      if (i > mMinWidth) {
        mMaxWidth = i;
      } else {
        mMaxWidth = mMinWidth;
      }
      invalidate();
    }
  }
  
  private boolean updateInputTextView()
  {
    String str;
    if (mDisplayedValues == null) {
      str = formatNumber(mValue);
    } else {
      str = mDisplayedValues[(mValue - mMinValue)];
    }
    if (!TextUtils.isEmpty(str))
    {
      Editable localEditable = mInputText.getText();
      if (!str.equals(localEditable.toString()))
      {
        mInputText.setText(str);
        if (AccessibilityManager.getInstance(mContext).isEnabled())
        {
          AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(16);
          mInputText.onInitializeAccessibilityEvent(localAccessibilityEvent);
          mInputText.onPopulateAccessibilityEvent(localAccessibilityEvent);
          localAccessibilityEvent.setFromIndex(0);
          localAccessibilityEvent.setRemovedCount(localEditable.length());
          localAccessibilityEvent.setAddedCount(str.length());
          localAccessibilityEvent.setBeforeText(localEditable);
          localAccessibilityEvent.setSource(this, 2);
          requestSendAccessibilityEvent(this, localAccessibilityEvent);
        }
        return true;
      }
    }
    return false;
  }
  
  private void updateWrapSelectorWheel()
  {
    int i = mMaxValue;
    int j = mMinValue;
    int k = mSelectorIndices.length;
    boolean bool1 = false;
    if (i - j >= k) {
      k = 1;
    } else {
      k = 0;
    }
    boolean bool2 = bool1;
    if (k != 0)
    {
      bool2 = bool1;
      if (mWrapSelectorWheelPreferred) {
        bool2 = true;
      }
    }
    mWrapSelectorWheel = bool2;
  }
  
  private void validateInputTextView(View paramView)
  {
    paramView = String.valueOf(((TextView)paramView).getText());
    if (TextUtils.isEmpty(paramView)) {
      updateInputTextView();
    } else {
      setValueInternal(getSelectedPos(paramView.toString()), true);
    }
  }
  
  public void computeScroll()
  {
    Scroller localScroller1 = mFlingScroller;
    Scroller localScroller2 = localScroller1;
    if (localScroller1.isFinished())
    {
      localScroller1 = mAdjustScroller;
      localScroller2 = localScroller1;
      if (localScroller1.isFinished()) {
        return;
      }
    }
    localScroller2.computeScrollOffset();
    int i = localScroller2.getCurrY();
    if (mPreviousScrollerY == 0) {
      mPreviousScrollerY = localScroller2.getStartY();
    }
    scrollBy(0, i - mPreviousScrollerY);
    mPreviousScrollerY = i;
    if (localScroller2.isFinished()) {
      onScrollerFinished(localScroller2);
    } else {
      invalidate();
    }
  }
  
  protected int computeVerticalScrollExtent()
  {
    return getHeight();
  }
  
  protected int computeVerticalScrollOffset()
  {
    return mCurrentScrollOffset;
  }
  
  protected int computeVerticalScrollRange()
  {
    return (mMaxValue - mMinValue + 1) * mSelectorElementHeight;
  }
  
  protected boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    if (!mHasSelectorWheel) {
      return super.dispatchHoverEvent(paramMotionEvent);
    }
    if (AccessibilityManager.getInstance(mContext).isEnabled())
    {
      int i = (int)paramMotionEvent.getY();
      if (i < mTopSelectionDividerTop) {
        i = 3;
      }
      for (;;)
      {
        break;
        if (i > mBottomSelectionDividerBottom) {
          i = 1;
        } else {
          i = 2;
        }
      }
      int j = paramMotionEvent.getActionMasked();
      paramMotionEvent = (AccessibilityNodeProviderImpl)getAccessibilityNodeProvider();
      if (j != 7)
      {
        switch (j)
        {
        default: 
          break;
        case 10: 
          paramMotionEvent.sendAccessibilityEventForVirtualView(i, 256);
          mLastHoveredChildVirtualViewId = -1;
          break;
        case 9: 
          paramMotionEvent.sendAccessibilityEventForVirtualView(i, 128);
          mLastHoveredChildVirtualViewId = i;
          paramMotionEvent.performAction(i, 64, null);
          break;
        }
      }
      else if ((mLastHoveredChildVirtualViewId != i) && (mLastHoveredChildVirtualViewId != -1))
      {
        paramMotionEvent.sendAccessibilityEventForVirtualView(mLastHoveredChildVirtualViewId, 256);
        paramMotionEvent.sendAccessibilityEventForVirtualView(i, 128);
        mLastHoveredChildVirtualViewId = i;
        paramMotionEvent.performAction(i, 64, null);
      }
    }
    return false;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if ((i != 23) && (i != 66)) {}
    switch (i)
    {
    default: 
      break;
    case 19: 
    case 20: 
      if (mHasSelectorWheel) {
        switch (paramKeyEvent.getAction())
        {
        default: 
          break;
        case 1: 
          if (mLastHandledDownDpadKeyCode == i)
          {
            mLastHandledDownDpadKeyCode = -1;
            return true;
          }
          break;
        case 0: 
          if ((mWrapSelectorWheel) || (i == 20 ? getValue() < getMaxValue() : getValue() > getMinValue()))
          {
            requestFocus();
            mLastHandledDownDpadKeyCode = i;
            removeAllCallbacks();
            if (mFlingScroller.isFinished())
            {
              boolean bool;
              if (i == 20) {
                bool = true;
              } else {
                bool = false;
              }
              changeValueByOne(bool);
            }
            return true;
            removeAllCallbacks();
          }
          break;
        }
      }
      break;
    }
    return super.dispatchKeyEvent(paramKeyEvent);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if ((i == 1) || (i == 3)) {
      removeAllCallbacks();
    }
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if ((i == 1) || (i == 3)) {
      removeAllCallbacks();
    }
    return super.dispatchTrackballEvent(paramMotionEvent);
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = mSelectionDivider;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
  }
  
  public AccessibilityNodeProvider getAccessibilityNodeProvider()
  {
    if (!mHasSelectorWheel) {
      return super.getAccessibilityNodeProvider();
    }
    if (mAccessibilityNodeProvider == null) {
      mAccessibilityNodeProvider = new AccessibilityNodeProviderImpl();
    }
    return mAccessibilityNodeProvider;
  }
  
  protected float getBottomFadingEdgeStrength()
  {
    return 0.9F;
  }
  
  public CharSequence getDisplayedValueForCurrentSelection()
  {
    return (CharSequence)mSelectorIndexToStringCache.get(getValue());
  }
  
  public String[] getDisplayedValues()
  {
    return mDisplayedValues;
  }
  
  public int getMaxValue()
  {
    return mMaxValue;
  }
  
  public int getMinValue()
  {
    return mMinValue;
  }
  
  public int getSolidColor()
  {
    return mSolidColor;
  }
  
  protected float getTopFadingEdgeStrength()
  {
    return 0.9F;
  }
  
  public int getValue()
  {
    return mValue;
  }
  
  public boolean getWrapSelectorWheel()
  {
    return mWrapSelectorWheel;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mSelectionDivider != null) {
      mSelectionDivider.jumpToCurrentState();
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeAllCallbacks();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (!mHasSelectorWheel)
    {
      super.onDraw(paramCanvas);
      return;
    }
    boolean bool;
    if (mHideWheelUntilFocused) {
      bool = hasFocus();
    } else {
      bool = true;
    }
    float f1 = (mRight - mLeft) / 2;
    float f2 = mCurrentScrollOffset;
    if ((bool) && (mVirtualButtonPressedDrawable != null) && (mScrollState == 0))
    {
      if (mDecrementVirtualButtonPressed)
      {
        mVirtualButtonPressedDrawable.setState(PRESSED_STATE_SET);
        mVirtualButtonPressedDrawable.setBounds(0, 0, mRight, mTopSelectionDividerTop);
        mVirtualButtonPressedDrawable.draw(paramCanvas);
      }
      if (mIncrementVirtualButtonPressed)
      {
        mVirtualButtonPressedDrawable.setState(PRESSED_STATE_SET);
        mVirtualButtonPressedDrawable.setBounds(0, mBottomSelectionDividerBottom, mRight, mBottom);
        mVirtualButtonPressedDrawable.draw(paramCanvas);
      }
    }
    int[] arrayOfInt = mSelectorIndices;
    int j;
    for (int i = 0; i < arrayOfInt.length; i++)
    {
      j = arrayOfInt[i];
      String str = (String)mSelectorIndexToStringCache.get(j);
      if (((bool) && (i != 1)) || ((i == 1) && (mInputText.getVisibility() != 0))) {
        paramCanvas.drawText(str, f1, f2, mSelectorWheelPaint);
      }
      f2 += mSelectorElementHeight;
    }
    if ((bool) && (mSelectionDivider != null))
    {
      j = mTopSelectionDividerTop;
      i = mSelectionDividerHeight;
      mSelectionDivider.setBounds(0, j, mRight, i + j);
      mSelectionDivider.draw(paramCanvas);
      j = mBottomSelectionDividerBottom;
      i = mSelectionDividerHeight;
      mSelectionDivider.setBounds(0, j - i, mRight, j);
      mSelectionDivider.draw(paramCanvas);
    }
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(NumberPicker.class.getName());
    paramAccessibilityEvent.setScrollable(true);
    paramAccessibilityEvent.setScrollY((mMinValue + mValue) * mSelectorElementHeight);
    paramAccessibilityEvent.setMaxScrollY((mMaxValue - mMinValue) * mSelectorElementHeight);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((mHasSelectorWheel) && (isEnabled()))
    {
      if (paramMotionEvent.getActionMasked() != 0) {
        return false;
      }
      removeAllCallbacks();
      hideSoftInput();
      float f = paramMotionEvent.getY();
      mLastDownEventY = f;
      mLastDownOrMoveEventY = f;
      mLastDownEventTime = paramMotionEvent.getEventTime();
      mIgnoreMoveEvents = false;
      mPerformClickOnTap = false;
      if (mLastDownEventY < mTopSelectionDividerTop)
      {
        if (mScrollState == 0) {
          mPressedStateHelper.buttonPressDelayed(2);
        }
      }
      else if ((mLastDownEventY > mBottomSelectionDividerBottom) && (mScrollState == 0)) {
        mPressedStateHelper.buttonPressDelayed(1);
      }
      getParent().requestDisallowInterceptTouchEvent(true);
      if (!mFlingScroller.isFinished())
      {
        mFlingScroller.forceFinished(true);
        mAdjustScroller.forceFinished(true);
        onScrollStateChange(0);
      }
      else if (!mAdjustScroller.isFinished())
      {
        mFlingScroller.forceFinished(true);
        mAdjustScroller.forceFinished(true);
      }
      else if (mLastDownEventY < mTopSelectionDividerTop)
      {
        postChangeCurrentByOneFromLongPress(false, ViewConfiguration.getLongPressTimeout());
      }
      else if (mLastDownEventY > mBottomSelectionDividerBottom)
      {
        postChangeCurrentByOneFromLongPress(true, ViewConfiguration.getLongPressTimeout());
      }
      else
      {
        mPerformClickOnTap = true;
        postBeginSoftInputOnLongPressCommand();
      }
      return true;
    }
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!mHasSelectorWheel)
    {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    }
    paramInt3 = getMeasuredWidth();
    paramInt4 = getMeasuredHeight();
    paramInt2 = mInputText.getMeasuredWidth();
    paramInt1 = mInputText.getMeasuredHeight();
    paramInt3 = (paramInt3 - paramInt2) / 2;
    paramInt4 = (paramInt4 - paramInt1) / 2;
    mInputText.layout(paramInt3, paramInt4, paramInt3 + paramInt2, paramInt4 + paramInt1);
    if (paramBoolean)
    {
      initializeSelectorWheel();
      initializeFadingEdges();
      mTopSelectionDividerTop = ((getHeight() - mSelectionDividersDistance) / 2 - mSelectionDividerHeight);
      mBottomSelectionDividerBottom = (mTopSelectionDividerTop + 2 * mSelectionDividerHeight + mSelectionDividersDistance);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (!mHasSelectorWheel)
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    super.onMeasure(makeMeasureSpec(paramInt1, mMaxWidth), makeMeasureSpec(paramInt2, mMaxHeight));
    setMeasuredDimension(resolveSizeAndStateRespectingMinSize(mMinWidth, getMeasuredWidth(), paramInt1), resolveSizeAndStateRespectingMinSize(mMinHeight, getMeasuredHeight(), paramInt2));
  }
  
  public void onResolveDrawables(int paramInt)
  {
    super.onResolveDrawables(paramInt);
    if (mSelectionDivider != null) {
      mSelectionDivider.setLayoutDirection(paramInt);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((isEnabled()) && (mHasSelectorWheel))
    {
      if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
      }
      mVelocityTracker.addMovement(paramMotionEvent);
      switch (paramMotionEvent.getActionMasked())
      {
      default: 
        break;
      case 2: 
        if (!mIgnoreMoveEvents)
        {
          float f = paramMotionEvent.getY();
          if (mScrollState != 1)
          {
            if ((int)Math.abs(f - mLastDownEventY) > mTouchSlop)
            {
              removeAllCallbacks();
              onScrollStateChange(1);
            }
          }
          else
          {
            scrollBy(0, (int)(f - mLastDownOrMoveEventY));
            invalidate();
          }
          mLastDownOrMoveEventY = f;
        }
        break;
      case 1: 
        removeBeginSoftInputCommand();
        removeChangeCurrentByOneFromLongPress();
        mPressedStateHelper.cancel();
        VelocityTracker localVelocityTracker = mVelocityTracker;
        localVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        int i = (int)localVelocityTracker.getYVelocity();
        if (Math.abs(i) > mMinimumFlingVelocity)
        {
          fling(i);
          onScrollStateChange(2);
        }
        else
        {
          i = (int)paramMotionEvent.getY();
          int j = (int)Math.abs(i - mLastDownEventY);
          long l1 = paramMotionEvent.getEventTime();
          long l2 = mLastDownEventTime;
          if ((j <= mTouchSlop) && (l1 - l2 < ViewConfiguration.getTapTimeout()))
          {
            if (mPerformClickOnTap)
            {
              mPerformClickOnTap = false;
              performClick();
            }
            else
            {
              i = i / mSelectorElementHeight - 1;
              if (i > 0)
              {
                changeValueByOne(true);
                mPressedStateHelper.buttonTapped(1);
              }
              else if (i < 0)
              {
                changeValueByOne(false);
                mPressedStateHelper.buttonTapped(2);
              }
            }
          }
          else {
            ensureScrollWheelAdjusted();
          }
          onScrollStateChange(0);
        }
        mVelocityTracker.recycle();
        mVelocityTracker = null;
      }
      return true;
    }
    return false;
  }
  
  public boolean performClick()
  {
    if (!mHasSelectorWheel) {
      return super.performClick();
    }
    if (!super.performClick()) {
      showSoftInput();
    }
    return true;
  }
  
  public boolean performLongClick()
  {
    if (!mHasSelectorWheel) {
      return super.performLongClick();
    }
    if (!super.performLongClick())
    {
      showSoftInput();
      mIgnoreMoveEvents = true;
    }
    return true;
  }
  
  public void scrollBy(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = mSelectorIndices;
    paramInt1 = mCurrentScrollOffset;
    if ((!mWrapSelectorWheel) && (paramInt2 > 0) && (arrayOfInt[1] <= mMinValue))
    {
      mCurrentScrollOffset = mInitialScrollOffset;
      return;
    }
    if ((!mWrapSelectorWheel) && (paramInt2 < 0) && (arrayOfInt[1] >= mMaxValue))
    {
      mCurrentScrollOffset = mInitialScrollOffset;
      return;
    }
    for (mCurrentScrollOffset += paramInt2; mCurrentScrollOffset - mInitialScrollOffset > mSelectorTextGapHeight; mCurrentScrollOffset = mInitialScrollOffset)
    {
      label80:
      mCurrentScrollOffset -= mSelectorElementHeight;
      decrementSelectorIndices(arrayOfInt);
      setValueInternal(arrayOfInt[1], true);
      if ((mWrapSelectorWheel) || (arrayOfInt[1] > mMinValue)) {
        break label80;
      }
    }
    while (mCurrentScrollOffset - mInitialScrollOffset < -mSelectorTextGapHeight)
    {
      mCurrentScrollOffset += mSelectorElementHeight;
      incrementSelectorIndices(arrayOfInt);
      setValueInternal(arrayOfInt[1], true);
      if ((!mWrapSelectorWheel) && (arrayOfInt[1] >= mMaxValue)) {
        mCurrentScrollOffset = mInitialScrollOffset;
      }
    }
    if (paramInt1 != mCurrentScrollOffset) {
      onScrollChanged(0, mCurrentScrollOffset, 0, paramInt1);
    }
  }
  
  public void setDisplayedValues(String[] paramArrayOfString)
  {
    if (mDisplayedValues == paramArrayOfString) {
      return;
    }
    mDisplayedValues = paramArrayOfString;
    if (mDisplayedValues != null) {
      mInputText.setRawInputType(524289);
    } else {
      mInputText.setRawInputType(2);
    }
    updateInputTextView();
    initializeSelectorWheelIndices();
    tryComputeMaxWidth();
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    if (!mHasSelectorWheel) {
      mIncrementButton.setEnabled(paramBoolean);
    }
    if (!mHasSelectorWheel) {
      mDecrementButton.setEnabled(paramBoolean);
    }
    mInputText.setEnabled(paramBoolean);
  }
  
  public void setFormatter(Formatter paramFormatter)
  {
    if (paramFormatter == mFormatter) {
      return;
    }
    mFormatter = paramFormatter;
    initializeSelectorWheelIndices();
    updateInputTextView();
  }
  
  public void setMaxValue(int paramInt)
  {
    if (mMaxValue == paramInt) {
      return;
    }
    if (paramInt >= 0)
    {
      mMaxValue = paramInt;
      if (mMaxValue < mValue) {
        mValue = mMaxValue;
      }
      updateWrapSelectorWheel();
      initializeSelectorWheelIndices();
      updateInputTextView();
      tryComputeMaxWidth();
      invalidate();
      return;
    }
    throw new IllegalArgumentException("maxValue must be >= 0");
  }
  
  public void setMinValue(int paramInt)
  {
    if (mMinValue == paramInt) {
      return;
    }
    if (paramInt >= 0)
    {
      mMinValue = paramInt;
      if (mMinValue > mValue) {
        mValue = mMinValue;
      }
      updateWrapSelectorWheel();
      initializeSelectorWheelIndices();
      updateInputTextView();
      tryComputeMaxWidth();
      invalidate();
      return;
    }
    throw new IllegalArgumentException("minValue must be >= 0");
  }
  
  public void setOnLongPressUpdateInterval(long paramLong)
  {
    mLongPressUpdateInterval = paramLong;
  }
  
  public void setOnScrollListener(OnScrollListener paramOnScrollListener)
  {
    mOnScrollListener = paramOnScrollListener;
  }
  
  public void setOnValueChangedListener(OnValueChangeListener paramOnValueChangeListener)
  {
    mOnValueChangeListener = paramOnValueChangeListener;
  }
  
  public void setValue(int paramInt)
  {
    setValueInternal(paramInt, false);
  }
  
  public void setWrapSelectorWheel(boolean paramBoolean)
  {
    mWrapSelectorWheelPreferred = paramBoolean;
    updateWrapSelectorWheel();
  }
  
  class AccessibilityNodeProviderImpl
    extends AccessibilityNodeProvider
  {
    private static final int UNDEFINED = Integer.MIN_VALUE;
    private static final int VIRTUAL_VIEW_ID_DECREMENT = 3;
    private static final int VIRTUAL_VIEW_ID_INCREMENT = 1;
    private static final int VIRTUAL_VIEW_ID_INPUT = 2;
    private int mAccessibilityFocusedView = Integer.MIN_VALUE;
    private final int[] mTempArray = new int[2];
    private final Rect mTempRect = new Rect();
    
    AccessibilityNodeProviderImpl() {}
    
    private AccessibilityNodeInfo createAccessibilityNodeInfoForNumberPicker(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = AccessibilityNodeInfo.obtain();
      localAccessibilityNodeInfo.setClassName(NumberPicker.class.getName());
      localAccessibilityNodeInfo.setPackageName(mContext.getPackageName());
      localAccessibilityNodeInfo.setSource(NumberPicker.this);
      if (hasVirtualDecrementButton()) {
        localAccessibilityNodeInfo.addChild(NumberPicker.this, 3);
      }
      localAccessibilityNodeInfo.addChild(NumberPicker.this, 2);
      if (hasVirtualIncrementButton()) {
        localAccessibilityNodeInfo.addChild(NumberPicker.this, 1);
      }
      localAccessibilityNodeInfo.setParent((View)getParentForAccessibility());
      localAccessibilityNodeInfo.setEnabled(isEnabled());
      localAccessibilityNodeInfo.setScrollable(true);
      float f = getContext().getResources().getCompatibilityInfo().applicationScale;
      Rect localRect = mTempRect;
      localRect.set(paramInt1, paramInt2, paramInt3, paramInt4);
      localRect.scale(f);
      localAccessibilityNodeInfo.setBoundsInParent(localRect);
      localAccessibilityNodeInfo.setVisibleToUser(isVisibleToUser());
      int[] arrayOfInt = mTempArray;
      getLocationOnScreen(arrayOfInt);
      localRect.offset(arrayOfInt[0], arrayOfInt[1]);
      localRect.scale(f);
      localAccessibilityNodeInfo.setBoundsInScreen(localRect);
      if (mAccessibilityFocusedView != -1) {
        localAccessibilityNodeInfo.addAction(64);
      }
      if (mAccessibilityFocusedView == -1) {
        localAccessibilityNodeInfo.addAction(128);
      }
      if (isEnabled())
      {
        if ((getWrapSelectorWheel()) || (getValue() < getMaxValue())) {
          localAccessibilityNodeInfo.addAction(4096);
        }
        if ((getWrapSelectorWheel()) || (getValue() > getMinValue())) {
          localAccessibilityNodeInfo.addAction(8192);
        }
      }
      return localAccessibilityNodeInfo;
    }
    
    private AccessibilityNodeInfo createAccessibilityNodeInfoForVirtualButton(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = AccessibilityNodeInfo.obtain();
      localAccessibilityNodeInfo.setClassName(Button.class.getName());
      localAccessibilityNodeInfo.setPackageName(mContext.getPackageName());
      localAccessibilityNodeInfo.setSource(NumberPicker.this, paramInt1);
      localAccessibilityNodeInfo.setParent(NumberPicker.this);
      localAccessibilityNodeInfo.setText(paramString);
      localAccessibilityNodeInfo.setClickable(true);
      localAccessibilityNodeInfo.setLongClickable(true);
      localAccessibilityNodeInfo.setEnabled(isEnabled());
      Rect localRect = mTempRect;
      localRect.set(paramInt2, paramInt3, paramInt4, paramInt5);
      localAccessibilityNodeInfo.setVisibleToUser(isVisibleToUser(localRect));
      localAccessibilityNodeInfo.setBoundsInParent(localRect);
      paramString = mTempArray;
      getLocationOnScreen(paramString);
      localRect.offset(paramString[0], paramString[1]);
      localAccessibilityNodeInfo.setBoundsInScreen(localRect);
      if (mAccessibilityFocusedView != paramInt1) {
        localAccessibilityNodeInfo.addAction(64);
      }
      if (mAccessibilityFocusedView == paramInt1) {
        localAccessibilityNodeInfo.addAction(128);
      }
      if (isEnabled()) {
        localAccessibilityNodeInfo.addAction(16);
      }
      return localAccessibilityNodeInfo;
    }
    
    private AccessibilityNodeInfo createAccessibiltyNodeInfoForInputText(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = mInputText.createAccessibilityNodeInfo();
      localAccessibilityNodeInfo.setSource(NumberPicker.this, 2);
      if (mAccessibilityFocusedView != 2) {
        localAccessibilityNodeInfo.addAction(64);
      }
      if (mAccessibilityFocusedView == 2) {
        localAccessibilityNodeInfo.addAction(128);
      }
      Rect localRect = mTempRect;
      localRect.set(paramInt1, paramInt2, paramInt3, paramInt4);
      localAccessibilityNodeInfo.setVisibleToUser(isVisibleToUser(localRect));
      localAccessibilityNodeInfo.setBoundsInParent(localRect);
      int[] arrayOfInt = mTempArray;
      getLocationOnScreen(arrayOfInt);
      localRect.offset(arrayOfInt[0], arrayOfInt[1]);
      localAccessibilityNodeInfo.setBoundsInScreen(localRect);
      return localAccessibilityNodeInfo;
    }
    
    private void findAccessibilityNodeInfosByTextInChild(String paramString, int paramInt, List<AccessibilityNodeInfo> paramList)
    {
      Object localObject;
      switch (paramInt)
      {
      default: 
        break;
      case 3: 
        localObject = getVirtualDecrementButtonText();
        if ((!TextUtils.isEmpty((CharSequence)localObject)) && (((String)localObject).toString().toLowerCase().contains(paramString))) {
          paramList.add(createAccessibilityNodeInfo(3));
        }
        return;
      case 2: 
        localObject = mInputText.getText();
        if ((!TextUtils.isEmpty((CharSequence)localObject)) && (((CharSequence)localObject).toString().toLowerCase().contains(paramString)))
        {
          paramList.add(createAccessibilityNodeInfo(2));
          return;
        }
        localObject = mInputText.getText();
        if ((!TextUtils.isEmpty((CharSequence)localObject)) && (((CharSequence)localObject).toString().toLowerCase().contains(paramString)))
        {
          paramList.add(createAccessibilityNodeInfo(2));
          return;
        }
        break;
      case 1: 
        localObject = getVirtualIncrementButtonText();
        if ((!TextUtils.isEmpty((CharSequence)localObject)) && (((String)localObject).toString().toLowerCase().contains(paramString))) {
          paramList.add(createAccessibilityNodeInfo(1));
        }
        return;
      }
    }
    
    private String getVirtualDecrementButtonText()
    {
      int i = mValue - 1;
      int j = i;
      if (mWrapSelectorWheel) {
        j = NumberPicker.this.getWrappedSelectorIndex(i);
      }
      if (j >= mMinValue)
      {
        String str;
        if (mDisplayedValues == null) {
          str = NumberPicker.this.formatNumber(j);
        } else {
          str = mDisplayedValues[(j - mMinValue)];
        }
        return str;
      }
      return null;
    }
    
    private String getVirtualIncrementButtonText()
    {
      int i = mValue + 1;
      int j = i;
      if (mWrapSelectorWheel) {
        j = NumberPicker.this.getWrappedSelectorIndex(i);
      }
      if (j <= mMaxValue)
      {
        String str;
        if (mDisplayedValues == null) {
          str = NumberPicker.this.formatNumber(j);
        } else {
          str = mDisplayedValues[(j - mMinValue)];
        }
        return str;
      }
      return null;
    }
    
    private boolean hasVirtualDecrementButton()
    {
      boolean bool;
      if ((!getWrapSelectorWheel()) && (getValue() <= getMinValue())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    private boolean hasVirtualIncrementButton()
    {
      boolean bool;
      if ((!getWrapSelectorWheel()) && (getValue() >= getMaxValue())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    private void sendAccessibilityEventForVirtualButton(int paramInt1, int paramInt2, String paramString)
    {
      if (AccessibilityManager.getInstance(mContext).isEnabled())
      {
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt2);
        localAccessibilityEvent.setClassName(Button.class.getName());
        localAccessibilityEvent.setPackageName(mContext.getPackageName());
        localAccessibilityEvent.getText().add(paramString);
        localAccessibilityEvent.setEnabled(isEnabled());
        localAccessibilityEvent.setSource(NumberPicker.this, paramInt1);
        requestSendAccessibilityEvent(NumberPicker.this, localAccessibilityEvent);
      }
    }
    
    private void sendAccessibilityEventForVirtualText(int paramInt)
    {
      if (AccessibilityManager.getInstance(mContext).isEnabled())
      {
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(paramInt);
        mInputText.onInitializeAccessibilityEvent(localAccessibilityEvent);
        mInputText.onPopulateAccessibilityEvent(localAccessibilityEvent);
        localAccessibilityEvent.setSource(NumberPicker.this, 2);
        requestSendAccessibilityEvent(NumberPicker.this, localAccessibilityEvent);
      }
    }
    
    public AccessibilityNodeInfo createAccessibilityNodeInfo(int paramInt)
    {
      if (paramInt != -1)
      {
        switch (paramInt)
        {
        default: 
          return super.createAccessibilityNodeInfo(paramInt);
        case 3: 
          return createAccessibilityNodeInfoForVirtualButton(3, getVirtualDecrementButtonText(), mScrollX, mScrollY, mScrollX + (mRight - mLeft), mTopSelectionDividerTop + mSelectionDividerHeight);
        case 2: 
          return createAccessibiltyNodeInfoForInputText(mScrollX, mTopSelectionDividerTop + mSelectionDividerHeight, mScrollX + (mRight - mLeft), mBottomSelectionDividerBottom - mSelectionDividerHeight);
        }
        return createAccessibilityNodeInfoForVirtualButton(1, getVirtualIncrementButtonText(), mScrollX, mBottomSelectionDividerBottom - mSelectionDividerHeight, mScrollX + (mRight - mLeft), mScrollY + (mBottom - mTop));
      }
      return createAccessibilityNodeInfoForNumberPicker(mScrollX, mScrollY, mScrollX + (mRight - mLeft), mScrollY + (mBottom - mTop));
    }
    
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String paramString, int paramInt)
    {
      if (TextUtils.isEmpty(paramString)) {
        return Collections.emptyList();
      }
      String str = paramString.toLowerCase();
      ArrayList localArrayList = new ArrayList();
      if (paramInt != -1)
      {
        switch (paramInt)
        {
        default: 
          return super.findAccessibilityNodeInfosByText(paramString, paramInt);
        }
        findAccessibilityNodeInfosByTextInChild(str, paramInt, localArrayList);
        return localArrayList;
      }
      findAccessibilityNodeInfosByTextInChild(str, 3, localArrayList);
      findAccessibilityNodeInfosByTextInChild(str, 2, localArrayList);
      findAccessibilityNodeInfosByTextInChild(str, 1, localArrayList);
      return localArrayList;
    }
    
    public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      boolean bool = false;
      if (paramInt1 != -1)
      {
        switch (paramInt1)
        {
        default: 
          break;
        case 3: 
          if (paramInt2 != 16)
          {
            if (paramInt2 != 64)
            {
              if (paramInt2 != 128) {
                return false;
              }
              if (mAccessibilityFocusedView == paramInt1)
              {
                mAccessibilityFocusedView = Integer.MIN_VALUE;
                sendAccessibilityEventForVirtualView(paramInt1, 65536);
                invalidate(0, 0, mRight, mTopSelectionDividerTop);
                return true;
              }
              return false;
            }
            if (mAccessibilityFocusedView != paramInt1)
            {
              mAccessibilityFocusedView = paramInt1;
              sendAccessibilityEventForVirtualView(paramInt1, 32768);
              invalidate(0, 0, mRight, mTopSelectionDividerTop);
              return true;
            }
            return false;
          }
          if (isEnabled())
          {
            if (paramInt1 == 1) {
              bool = true;
            }
            NumberPicker.this.changeValueByOne(bool);
            sendAccessibilityEventForVirtualView(paramInt1, 1);
            return true;
          }
          return false;
        case 2: 
          if (paramInt2 != 16)
          {
            if (paramInt2 != 32)
            {
              if (paramInt2 != 64)
              {
                if (paramInt2 != 128)
                {
                  switch (paramInt2)
                  {
                  default: 
                    return mInputText.performAccessibilityAction(paramInt2, paramBundle);
                  case 2: 
                    if ((isEnabled()) && (mInputText.isFocused()))
                    {
                      mInputText.clearFocus();
                      return true;
                    }
                    return false;
                  }
                  if ((isEnabled()) && (!mInputText.isFocused())) {
                    return mInputText.requestFocus();
                  }
                  return false;
                }
                if (mAccessibilityFocusedView == paramInt1)
                {
                  mAccessibilityFocusedView = Integer.MIN_VALUE;
                  sendAccessibilityEventForVirtualView(paramInt1, 65536);
                  mInputText.invalidate();
                  return true;
                }
                return false;
              }
              if (mAccessibilityFocusedView != paramInt1)
              {
                mAccessibilityFocusedView = paramInt1;
                sendAccessibilityEventForVirtualView(paramInt1, 32768);
                mInputText.invalidate();
                return true;
              }
              return false;
            }
            if (isEnabled())
            {
              performLongClick();
              return true;
            }
            return false;
          }
          if (isEnabled())
          {
            performClick();
            return true;
          }
          return false;
        case 1: 
          if (paramInt2 != 16)
          {
            if (paramInt2 != 64)
            {
              if (paramInt2 != 128) {
                return false;
              }
              if (mAccessibilityFocusedView == paramInt1)
              {
                mAccessibilityFocusedView = Integer.MIN_VALUE;
                sendAccessibilityEventForVirtualView(paramInt1, 65536);
                invalidate(0, mBottomSelectionDividerBottom, mRight, mBottom);
                return true;
              }
              return false;
            }
            if (mAccessibilityFocusedView != paramInt1)
            {
              mAccessibilityFocusedView = paramInt1;
              sendAccessibilityEventForVirtualView(paramInt1, 32768);
              invalidate(0, mBottomSelectionDividerBottom, mRight, mBottom);
              return true;
            }
            return false;
          }
          if (isEnabled())
          {
            NumberPicker.this.changeValueByOne(true);
            sendAccessibilityEventForVirtualView(paramInt1, 1);
            return true;
          }
          return false;
        }
      }
      else
      {
        if (paramInt2 == 64) {
          break label761;
        }
        if (paramInt2 == 128) {
          break label736;
        }
        if (paramInt2 == 4096) {
          break label687;
        }
        if (paramInt2 == 8192) {
          break label638;
        }
      }
      return super.performAction(paramInt1, paramInt2, paramBundle);
      label638:
      if ((isEnabled()) && ((getWrapSelectorWheel()) || (getValue() > getMinValue())))
      {
        NumberPicker.this.changeValueByOne(false);
        return true;
      }
      return false;
      label687:
      if ((isEnabled()) && ((getWrapSelectorWheel()) || (getValue() < getMaxValue())))
      {
        NumberPicker.this.changeValueByOne(true);
        return true;
      }
      return false;
      label736:
      if (mAccessibilityFocusedView == paramInt1)
      {
        mAccessibilityFocusedView = Integer.MIN_VALUE;
        clearAccessibilityFocus();
        return true;
      }
      return false;
      label761:
      if (mAccessibilityFocusedView != paramInt1)
      {
        mAccessibilityFocusedView = paramInt1;
        requestAccessibilityFocus();
        return true;
      }
      return false;
    }
    
    public void sendAccessibilityEventForVirtualView(int paramInt1, int paramInt2)
    {
      switch (paramInt1)
      {
      default: 
        break;
      case 3: 
        if (hasVirtualDecrementButton()) {
          sendAccessibilityEventForVirtualButton(paramInt1, paramInt2, getVirtualDecrementButtonText());
        }
        break;
      case 2: 
        sendAccessibilityEventForVirtualText(paramInt2);
        break;
      case 1: 
        if (hasVirtualIncrementButton()) {
          sendAccessibilityEventForVirtualButton(paramInt1, paramInt2, getVirtualIncrementButtonText());
        }
        break;
      }
    }
  }
  
  class BeginSoftInputOnLongPressCommand
    implements Runnable
  {
    BeginSoftInputOnLongPressCommand() {}
    
    public void run()
    {
      performLongClick();
    }
  }
  
  class ChangeCurrentByOneFromLongPressCommand
    implements Runnable
  {
    private boolean mIncrement;
    
    ChangeCurrentByOneFromLongPressCommand() {}
    
    private void setStep(boolean paramBoolean)
    {
      mIncrement = paramBoolean;
    }
    
    public void run()
    {
      NumberPicker.this.changeValueByOne(mIncrement);
      postDelayed(this, mLongPressUpdateInterval);
    }
  }
  
  public static class CustomEditText
    extends EditText
  {
    public CustomEditText(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public void onEditorAction(int paramInt)
    {
      super.onEditorAction(paramInt);
      if (paramInt == 6) {
        clearFocus();
      }
    }
  }
  
  public static abstract interface Formatter
  {
    public abstract String format(int paramInt);
  }
  
  class InputTextFilter
    extends NumberKeyListener
  {
    InputTextFilter() {}
    
    public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      if (mSetSelectionCommand != null) {
        mSetSelectionCommand.cancel();
      }
      Object localObject1 = mDisplayedValues;
      int i = 0;
      if (localObject1 == null)
      {
        localObject2 = super.filter(paramCharSequence, paramInt1, paramInt2, paramSpanned, paramInt3, paramInt4);
        localObject1 = localObject2;
        if (localObject2 == null) {
          localObject1 = paramCharSequence.subSequence(paramInt1, paramInt2);
        }
        paramCharSequence = new StringBuilder();
        paramCharSequence.append(String.valueOf(paramSpanned.subSequence(0, paramInt3)));
        paramCharSequence.append(localObject1);
        paramCharSequence.append(paramSpanned.subSequence(paramInt4, paramSpanned.length()));
        paramCharSequence = paramCharSequence.toString();
        if ("".equals(paramCharSequence)) {
          return paramCharSequence;
        }
        if ((NumberPicker.this.getSelectedPos(paramCharSequence) <= mMaxValue) && (paramCharSequence.length() <= String.valueOf(mMaxValue).length())) {
          return localObject1;
        }
        return "";
      }
      paramCharSequence = String.valueOf(paramCharSequence.subSequence(paramInt1, paramInt2));
      if (TextUtils.isEmpty(paramCharSequence)) {
        return "";
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(String.valueOf(paramSpanned.subSequence(0, paramInt3)));
      ((StringBuilder)localObject1).append(paramCharSequence);
      ((StringBuilder)localObject1).append(paramSpanned.subSequence(paramInt4, paramSpanned.length()));
      paramCharSequence = ((StringBuilder)localObject1).toString();
      paramSpanned = String.valueOf(paramCharSequence).toLowerCase();
      Object localObject2 = mDisplayedValues;
      paramInt2 = localObject2.length;
      for (paramInt1 = i; paramInt1 < paramInt2; paramInt1++)
      {
        localObject1 = localObject2[paramInt1];
        if (((String)localObject1).toLowerCase().startsWith(paramSpanned))
        {
          NumberPicker.this.postSetSelectionCommand(paramCharSequence.length(), ((String)localObject1).length());
          return ((String)localObject1).subSequence(paramInt3, ((String)localObject1).length());
        }
      }
      return "";
    }
    
    protected char[] getAcceptedChars()
    {
      return NumberPicker.DIGIT_CHARACTERS;
    }
    
    public int getInputType()
    {
      return 1;
    }
  }
  
  public static abstract interface OnScrollListener
  {
    public static final int SCROLL_STATE_FLING = 2;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
    
    public abstract void onScrollStateChange(NumberPicker paramNumberPicker, int paramInt);
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface ScrollState {}
  }
  
  public static abstract interface OnValueChangeListener
  {
    public abstract void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2);
  }
  
  class PressedStateHelper
    implements Runnable
  {
    public static final int BUTTON_DECREMENT = 2;
    public static final int BUTTON_INCREMENT = 1;
    private final int MODE_PRESS = 1;
    private final int MODE_TAPPED = 2;
    private int mManagedButton;
    private int mMode;
    
    PressedStateHelper() {}
    
    public void buttonPressDelayed(int paramInt)
    {
      cancel();
      mMode = 1;
      mManagedButton = paramInt;
      postDelayed(this, ViewConfiguration.getTapTimeout());
    }
    
    public void buttonTapped(int paramInt)
    {
      cancel();
      mMode = 2;
      mManagedButton = paramInt;
      post(this);
    }
    
    public void cancel()
    {
      mMode = 0;
      mManagedButton = 0;
      removeCallbacks(this);
      if (mIncrementVirtualButtonPressed)
      {
        NumberPicker.access$1202(NumberPicker.this, false);
        invalidate(0, mBottomSelectionDividerBottom, mRight, mBottom);
      }
      NumberPicker.access$1602(NumberPicker.this, false);
      if (mDecrementVirtualButtonPressed) {
        invalidate(0, 0, mRight, mTopSelectionDividerTop);
      }
    }
    
    public void run()
    {
      switch (mMode)
      {
      default: 
        break;
      case 2: 
        switch (mManagedButton)
        {
        default: 
          break;
        case 2: 
          if (!mDecrementVirtualButtonPressed) {
            postDelayed(this, ViewConfiguration.getPressedStateDuration());
          }
          NumberPicker.access$1680(NumberPicker.this, 1);
          invalidate(0, 0, mRight, mTopSelectionDividerTop);
          break;
        case 1: 
          if (!mIncrementVirtualButtonPressed) {
            postDelayed(this, ViewConfiguration.getPressedStateDuration());
          }
          NumberPicker.access$1280(NumberPicker.this, 1);
          invalidate(0, mBottomSelectionDividerBottom, mRight, mBottom);
        }
        break;
      case 1: 
        switch (mManagedButton)
        {
        default: 
          break;
        case 2: 
          NumberPicker.access$1602(NumberPicker.this, true);
          invalidate(0, 0, mRight, mTopSelectionDividerTop);
          break;
        case 1: 
          NumberPicker.access$1202(NumberPicker.this, true);
          invalidate(0, mBottomSelectionDividerBottom, mRight, mBottom);
        }
        break;
      }
    }
  }
  
  private static class SetSelectionCommand
    implements Runnable
  {
    private final EditText mInputText;
    private boolean mPosted;
    private int mSelectionEnd;
    private int mSelectionStart;
    
    public SetSelectionCommand(EditText paramEditText)
    {
      mInputText = paramEditText;
    }
    
    public void cancel()
    {
      if (mPosted)
      {
        mInputText.removeCallbacks(this);
        mPosted = false;
      }
    }
    
    public void post(int paramInt1, int paramInt2)
    {
      mSelectionStart = paramInt1;
      mSelectionEnd = paramInt2;
      if (!mPosted)
      {
        mInputText.post(this);
        mPosted = true;
      }
    }
    
    public void run()
    {
      mPosted = false;
      mInputText.setSelection(mSelectionStart, mSelectionEnd);
    }
  }
  
  private static class TwoDigitFormatter
    implements NumberPicker.Formatter
  {
    final Object[] mArgs = new Object[1];
    final StringBuilder mBuilder = new StringBuilder();
    Formatter mFmt;
    char mZeroDigit;
    
    TwoDigitFormatter()
    {
      init(Locale.getDefault());
    }
    
    private Formatter createFormatter(Locale paramLocale)
    {
      return new Formatter(mBuilder, paramLocale);
    }
    
    private static char getZeroDigit(Locale paramLocale)
    {
      return getzeroDigit;
    }
    
    private void init(Locale paramLocale)
    {
      mFmt = createFormatter(paramLocale);
      mZeroDigit = getZeroDigit(paramLocale);
    }
    
    public String format(int paramInt)
    {
      Locale localLocale = Locale.getDefault();
      if (mZeroDigit != getZeroDigit(localLocale)) {
        init(localLocale);
      }
      mArgs[0] = Integer.valueOf(paramInt);
      mBuilder.delete(0, mBuilder.length());
      mFmt.format("%02d", mArgs);
      return mFmt.toString();
    }
  }
}
