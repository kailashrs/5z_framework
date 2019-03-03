package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.R.styleable;
import com.android.internal.widget.ViewPager;
import com.android.internal.widget.ViewPager.OnPageChangeListener;
import java.util.Locale;
import libcore.icu.LocaleData;

class DayPickerView
  extends ViewGroup
{
  private static final int[] ATTRS_TEXT_COLOR = { 16842904 };
  private static final int DEFAULT_END_YEAR = 2100;
  private static final int DEFAULT_LAYOUT = 17367156;
  private static final int DEFAULT_START_YEAR = 1900;
  private final AccessibilityManager mAccessibilityManager;
  private final DayPickerPagerAdapter mAdapter;
  private final Calendar mMaxDate = Calendar.getInstance();
  private final Calendar mMinDate = Calendar.getInstance();
  private final ImageButton mNextButton;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (paramAnonymousView == mPrevButton) {}
      for (int i = -1;; i = 1)
      {
        break;
        if (paramAnonymousView != mNextButton) {
          return;
        }
      }
      boolean bool = mAccessibilityManager.isEnabled();
      int j = mViewPager.getCurrentItem();
      mViewPager.setCurrentItem(j + i, bool ^ true);
      return;
    }
  };
  private OnDaySelectedListener mOnDaySelectedListener;
  private final ViewPager.OnPageChangeListener mOnPageChangedListener = new ViewPager.OnPageChangeListener()
  {
    public void onPageScrollStateChanged(int paramAnonymousInt) {}
    
    public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
    {
      paramAnonymousFloat = Math.abs(0.5F - paramAnonymousFloat) * 2.0F;
      mPrevButton.setAlpha(paramAnonymousFloat);
      mNextButton.setAlpha(paramAnonymousFloat);
    }
    
    public void onPageSelected(int paramAnonymousInt)
    {
      DayPickerView.this.updateButtonVisibility(paramAnonymousInt);
    }
  };
  private final ImageButton mPrevButton;
  private final Calendar mSelectedDay = Calendar.getInstance();
  private Calendar mTempCalendar;
  private final ViewPager mViewPager;
  
  public DayPickerView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public DayPickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843613);
  }
  
  public DayPickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public DayPickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mAccessibilityManager = ((AccessibilityManager)paramContext.getSystemService("accessibility"));
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CalendarView, paramInt1, paramInt2);
    int i = localTypedArray.getInt(0, getgetDefaultfirstDayOfWeek.intValue());
    paramAttributeSet = localTypedArray.getString(2);
    String str = localTypedArray.getString(3);
    int j = localTypedArray.getResourceId(16, 16974982);
    paramInt1 = localTypedArray.getResourceId(11, 16974981);
    paramInt2 = localTypedArray.getResourceId(12, 16974980);
    Object localObject = localTypedArray.getColorStateList(15);
    localTypedArray.recycle();
    mAdapter = new DayPickerPagerAdapter(paramContext, 17367154, 16909144);
    mAdapter.setMonthTextAppearance(j);
    mAdapter.setDayOfWeekTextAppearance(paramInt1);
    mAdapter.setDayTextAppearance(paramInt2);
    mAdapter.setDaySelectorColor((ColorStateList)localObject);
    localObject = (ViewGroup)LayoutInflater.from(paramContext).inflate(17367156, this, false);
    while (((ViewGroup)localObject).getChildCount() > 0)
    {
      paramContext = ((ViewGroup)localObject).getChildAt(0);
      ((ViewGroup)localObject).removeViewAt(0);
      addView(paramContext);
    }
    mPrevButton = ((ImageButton)findViewById(16909259));
    mPrevButton.setOnClickListener(mOnClickListener);
    mNextButton = ((ImageButton)findViewById(16909159));
    mNextButton.setOnClickListener(mOnClickListener);
    mViewPager = ((ViewPager)findViewById(16908893));
    mViewPager.setAdapter(mAdapter);
    mViewPager.setOnPageChangeListener(mOnPageChangedListener);
    if (j != 0)
    {
      paramContext = mContext.obtainStyledAttributes(null, ATTRS_TEXT_COLOR, 0, j);
      localObject = paramContext.getColorStateList(0);
      if (localObject != null)
      {
        mPrevButton.setImageTintList((ColorStateList)localObject);
        mNextButton.setImageTintList((ColorStateList)localObject);
      }
      paramContext.recycle();
    }
    paramContext = Calendar.getInstance();
    if (!CalendarView.parseDate(paramAttributeSet, paramContext)) {
      paramContext.set(1900, 0, 1);
    }
    long l1 = paramContext.getTimeInMillis();
    if (!CalendarView.parseDate(str, paramContext)) {
      paramContext.set(2100, 11, 31);
    }
    long l2 = paramContext.getTimeInMillis();
    if (l2 >= l1)
    {
      long l3 = MathUtils.constrain(System.currentTimeMillis(), l1, l2);
      setFirstDayOfWeek(i);
      setMinDate(l1);
      setMaxDate(l2);
      setDate(l3, false);
      mAdapter.setOnDaySelectedListener(new DayPickerPagerAdapter.OnDaySelectedListener()
      {
        public void onDaySelected(DayPickerPagerAdapter paramAnonymousDayPickerPagerAdapter, Calendar paramAnonymousCalendar)
        {
          if (mOnDaySelectedListener != null) {
            mOnDaySelectedListener.onDaySelected(DayPickerView.this, paramAnonymousCalendar);
          }
        }
      });
      return;
    }
    throw new IllegalArgumentException("maxDate must be >= minDate");
  }
  
  private int getDiffMonths(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    int i = paramCalendar2.get(1);
    int j = paramCalendar1.get(1);
    return paramCalendar2.get(2) - paramCalendar1.get(2) + 12 * (i - j);
  }
  
  private int getPositionFromDay(long paramLong)
  {
    int i = getDiffMonths(mMinDate, mMaxDate);
    return MathUtils.constrain(getDiffMonths(mMinDate, getTempCalendarForTime(paramLong)), 0, i);
  }
  
  private Calendar getTempCalendarForTime(long paramLong)
  {
    if (mTempCalendar == null) {
      mTempCalendar = Calendar.getInstance();
    }
    mTempCalendar.setTimeInMillis(paramLong);
    return mTempCalendar;
  }
  
  private void setDate(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    long l;
    if (paramLong < mMinDate.getTimeInMillis())
    {
      l = mMinDate.getTimeInMillis();
      i = 1;
    }
    else
    {
      l = paramLong;
      if (paramLong > mMaxDate.getTimeInMillis())
      {
        l = mMaxDate.getTimeInMillis();
        i = 1;
      }
    }
    getTempCalendarForTime(l);
    if ((paramBoolean2) || (i != 0)) {
      mSelectedDay.setTimeInMillis(l);
    }
    i = getPositionFromDay(l);
    if (i != mViewPager.getCurrentItem()) {
      mViewPager.setCurrentItem(i, paramBoolean1);
    }
    mAdapter.setSelectedDay(mTempCalendar);
  }
  
  private void updateButtonVisibility(int paramInt)
  {
    int i = 1;
    int j = 0;
    int k;
    if (paramInt > 0) {
      k = 1;
    } else {
      k = 0;
    }
    if (paramInt < mAdapter.getCount() - 1) {
      paramInt = i;
    } else {
      paramInt = 0;
    }
    ImageButton localImageButton = mPrevButton;
    if (k != 0) {
      k = 0;
    } else {
      k = 4;
    }
    localImageButton.setVisibility(k);
    localImageButton = mNextButton;
    if (paramInt != 0) {
      paramInt = j;
    } else {
      paramInt = 4;
    }
    localImageButton.setVisibility(paramInt);
  }
  
  public boolean getBoundsForDate(long paramLong, Rect paramRect)
  {
    if (getPositionFromDay(paramLong) != mViewPager.getCurrentItem()) {
      return false;
    }
    mTempCalendar.setTimeInMillis(paramLong);
    return mAdapter.getBoundsForDate(mTempCalendar, paramRect);
  }
  
  public long getDate()
  {
    return mSelectedDay.getTimeInMillis();
  }
  
  public int getDayOfWeekTextAppearance()
  {
    return mAdapter.getDayOfWeekTextAppearance();
  }
  
  public int getDayTextAppearance()
  {
    return mAdapter.getDayTextAppearance();
  }
  
  public int getFirstDayOfWeek()
  {
    return mAdapter.getFirstDayOfWeek();
  }
  
  public long getMaxDate()
  {
    return mMaxDate.getTimeInMillis();
  }
  
  public long getMinDate()
  {
    return mMinDate.getTimeInMillis();
  }
  
  public int getMostVisiblePosition()
  {
    return mViewPager.getCurrentItem();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ImageButton localImageButton1;
    ImageButton localImageButton2;
    if (isLayoutRtl())
    {
      localImageButton1 = mNextButton;
      localImageButton2 = mPrevButton;
    }
    else
    {
      localImageButton1 = mPrevButton;
      localImageButton2 = mNextButton;
    }
    paramInt1 = paramInt3 - paramInt1;
    mViewPager.layout(0, 0, paramInt1, paramInt4 - paramInt2);
    SimpleMonthView localSimpleMonthView = (SimpleMonthView)mViewPager.getChildAt(0);
    paramInt3 = localSimpleMonthView.getMonthHeight();
    paramInt2 = localSimpleMonthView.getCellWidth();
    paramInt4 = localImageButton1.getMeasuredWidth();
    int i = localImageButton1.getMeasuredHeight();
    int j = localSimpleMonthView.getPaddingTop() + (paramInt3 - i) / 2;
    int k = localSimpleMonthView.getPaddingLeft() + (paramInt2 - paramInt4) / 2;
    localImageButton1.layout(k, j, k + paramInt4, j + i);
    paramInt4 = localImageButton2.getMeasuredWidth();
    k = localImageButton2.getMeasuredHeight();
    paramInt3 = localSimpleMonthView.getPaddingTop() + (paramInt3 - k) / 2;
    paramInt1 = paramInt1 - localSimpleMonthView.getPaddingRight() - (paramInt2 - paramInt4) / 2;
    localImageButton2.layout(paramInt1 - paramInt4, paramInt3, paramInt1, paramInt3 + k);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    ViewPager localViewPager = mViewPager;
    measureChild(localViewPager, paramInt1, paramInt2);
    setMeasuredDimension(localViewPager.getMeasuredWidthAndState(), localViewPager.getMeasuredHeightAndState());
    paramInt2 = localViewPager.getMeasuredWidth();
    paramInt1 = localViewPager.getMeasuredHeight();
    paramInt2 = View.MeasureSpec.makeMeasureSpec(paramInt2, Integer.MIN_VALUE);
    paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, Integer.MIN_VALUE);
    mPrevButton.measure(paramInt2, paramInt1);
    mNextButton.measure(paramInt2, paramInt1);
  }
  
  public void onRangeChanged()
  {
    mAdapter.setRange(mMinDate, mMaxDate);
    setDate(mSelectedDay.getTimeInMillis(), false, false);
    updateButtonVisibility(mViewPager.getCurrentItem());
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    requestLayout();
  }
  
  public void setDate(long paramLong)
  {
    setDate(paramLong, false);
  }
  
  public void setDate(long paramLong, boolean paramBoolean)
  {
    setDate(paramLong, paramBoolean, true);
  }
  
  public void setDayOfWeekTextAppearance(int paramInt)
  {
    mAdapter.setDayOfWeekTextAppearance(paramInt);
  }
  
  public void setDayTextAppearance(int paramInt)
  {
    mAdapter.setDayTextAppearance(paramInt);
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    mAdapter.setFirstDayOfWeek(paramInt);
  }
  
  public void setMaxDate(long paramLong)
  {
    mMaxDate.setTimeInMillis(paramLong);
    onRangeChanged();
  }
  
  public void setMinDate(long paramLong)
  {
    mMinDate.setTimeInMillis(paramLong);
    onRangeChanged();
  }
  
  public void setOnDaySelectedListener(OnDaySelectedListener paramOnDaySelectedListener)
  {
    mOnDaySelectedListener = paramOnDaySelectedListener;
  }
  
  public void setPosition(int paramInt)
  {
    mViewPager.setCurrentItem(paramInt, false);
  }
  
  public static abstract interface OnDaySelectedListener
  {
    public abstract void onDaySelected(DayPickerView paramDayPickerView, Calendar paramCalendar);
  }
}
