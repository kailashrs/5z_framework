package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import com.android.internal.R.styleable;
import java.util.Locale;
import libcore.icu.LocaleData;

class CalendarViewLegacyDelegate
  extends CalendarView.AbstractCalendarViewDelegate
{
  private static final int ADJUSTMENT_SCROLL_DURATION = 500;
  private static final int DAYS_PER_WEEK = 7;
  private static final int DEFAULT_DATE_TEXT_SIZE = 14;
  private static final int DEFAULT_SHOWN_WEEK_COUNT = 6;
  private static final boolean DEFAULT_SHOW_WEEK_NUMBER = true;
  private static final int DEFAULT_WEEK_DAY_TEXT_APPEARANCE_RES_ID = -1;
  private static final int GOTO_SCROLL_DURATION = 1000;
  private static final long MILLIS_IN_DAY = 86400000L;
  private static final long MILLIS_IN_WEEK = 604800000L;
  private static final int SCROLL_CHANGE_DELAY = 40;
  private static final int SCROLL_HYST_WEEKS = 2;
  private static final int UNSCALED_BOTTOM_BUFFER = 20;
  private static final int UNSCALED_LIST_SCROLL_TOP_OFFSET = 2;
  private static final int UNSCALED_SELECTED_DATE_VERTICAL_BAR_WIDTH = 6;
  private static final int UNSCALED_WEEK_MIN_VISIBLE_HEIGHT = 12;
  private static final int UNSCALED_WEEK_SEPARATOR_LINE_WIDTH = 1;
  private WeeksAdapter mAdapter;
  private int mBottomBuffer = 20;
  private int mCurrentMonthDisplayed = -1;
  private int mCurrentScrollState = 0;
  private int mDateTextAppearanceResId;
  private int mDateTextSize;
  private ViewGroup mDayNamesHeader;
  private String[] mDayNamesLong;
  private String[] mDayNamesShort;
  private int mDaysPerWeek = 7;
  private Calendar mFirstDayOfMonth;
  private int mFirstDayOfWeek;
  private int mFocusedMonthDateColor;
  private float mFriction = 0.05F;
  private boolean mIsScrollingUp = false;
  private int mListScrollTopOffset = 2;
  private ListView mListView;
  private Calendar mMaxDate;
  private Calendar mMinDate;
  private TextView mMonthName;
  private CalendarView.OnDateChangeListener mOnDateChangeListener;
  private long mPreviousScrollPosition;
  private int mPreviousScrollState = 0;
  private ScrollStateRunnable mScrollStateChangedRunnable = new ScrollStateRunnable(null);
  private Drawable mSelectedDateVerticalBar;
  private final int mSelectedDateVerticalBarWidth;
  private int mSelectedWeekBackgroundColor;
  private boolean mShowWeekNumber;
  private int mShownWeekCount;
  private Calendar mTempDate;
  private int mUnfocusedMonthDateColor;
  private float mVelocityScale = 0.333F;
  private int mWeekDayTextAppearanceResId;
  private int mWeekMinVisibleHeight = 12;
  private int mWeekNumberColor;
  private int mWeekSeparatorLineColor;
  private final int mWeekSeparatorLineWidth;
  
  CalendarViewLegacyDelegate(CalendarView paramCalendarView, Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramCalendarView, paramContext);
    paramCalendarView = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CalendarView, paramInt1, paramInt2);
    mShowWeekNumber = paramCalendarView.getBoolean(1, true);
    mFirstDayOfWeek = paramCalendarView.getInt(0, getgetDefaultfirstDayOfWeek.intValue());
    if (!CalendarView.parseDate(paramCalendarView.getString(2), mMinDate)) {
      CalendarView.parseDate("01/01/1900", mMinDate);
    }
    if (!CalendarView.parseDate(paramCalendarView.getString(3), mMaxDate)) {
      CalendarView.parseDate("01/01/2100", mMaxDate);
    }
    if (!mMaxDate.before(mMinDate))
    {
      mShownWeekCount = paramCalendarView.getInt(4, 6);
      mSelectedWeekBackgroundColor = paramCalendarView.getColor(5, 0);
      mFocusedMonthDateColor = paramCalendarView.getColor(6, 0);
      mUnfocusedMonthDateColor = paramCalendarView.getColor(7, 0);
      mWeekSeparatorLineColor = paramCalendarView.getColor(9, 0);
      mWeekNumberColor = paramCalendarView.getColor(8, 0);
      mSelectedDateVerticalBar = paramCalendarView.getDrawable(10);
      mDateTextAppearanceResId = paramCalendarView.getResourceId(12, 16973894);
      updateDateTextSize();
      mWeekDayTextAppearanceResId = paramCalendarView.getResourceId(11, -1);
      paramCalendarView.recycle();
      paramCalendarView = mDelegator.getResources().getDisplayMetrics();
      mWeekMinVisibleHeight = ((int)TypedValue.applyDimension(1, 12.0F, paramCalendarView));
      mListScrollTopOffset = ((int)TypedValue.applyDimension(1, 2.0F, paramCalendarView));
      mBottomBuffer = ((int)TypedValue.applyDimension(1, 20.0F, paramCalendarView));
      mSelectedDateVerticalBarWidth = ((int)TypedValue.applyDimension(1, 6.0F, paramCalendarView));
      mWeekSeparatorLineWidth = ((int)TypedValue.applyDimension(1, 1.0F, paramCalendarView));
      paramCalendarView = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(17367137, null, false);
      mDelegator.addView(paramCalendarView);
      mListView = ((ListView)mDelegator.findViewById(16908298));
      mDayNamesHeader = ((ViewGroup)paramCalendarView.findViewById(16908892));
      mMonthName = ((TextView)paramCalendarView.findViewById(16909143));
      setUpHeader();
      setUpListView();
      setUpAdapter();
      mTempDate.setTimeInMillis(System.currentTimeMillis());
      if (mTempDate.before(mMinDate)) {
        goTo(mMinDate, false, true, true);
      } else if (mMaxDate.before(mTempDate)) {
        goTo(mMaxDate, false, true, true);
      } else {
        goTo(mTempDate, false, true, true);
      }
      mDelegator.invalidate();
      return;
    }
    throw new IllegalArgumentException("Max date cannot be before min date.");
  }
  
  private static Calendar getCalendarForLocale(Calendar paramCalendar, Locale paramLocale)
  {
    if (paramCalendar == null) {
      return Calendar.getInstance(paramLocale);
    }
    long l = paramCalendar.getTimeInMillis();
    paramCalendar = Calendar.getInstance(paramLocale);
    paramCalendar.setTimeInMillis(l);
    return paramCalendar;
  }
  
  private int getWeeksSinceMinDate(Calendar paramCalendar)
  {
    if (!paramCalendar.before(mMinDate)) {
      return (int)((paramCalendar.getTimeInMillis() + paramCalendar.getTimeZone().getOffset(paramCalendar.getTimeInMillis()) - (mMinDate.getTimeInMillis() + mMinDate.getTimeZone().getOffset(mMinDate.getTimeInMillis())) + (mMinDate.get(7) - mFirstDayOfWeek) * 86400000L) / 604800000L);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("fromDate: ");
    localStringBuilder.append(mMinDate.getTime());
    localStringBuilder.append(" does not precede toDate: ");
    localStringBuilder.append(paramCalendar.getTime());
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void goTo(Calendar paramCalendar, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if ((!paramCalendar.before(mMinDate)) && (!paramCalendar.after(mMaxDate)))
    {
      int i = mListView.getFirstVisiblePosition();
      View localView = mListView.getChildAt(0);
      int j = i;
      if (localView != null)
      {
        j = i;
        if (localView.getTop() < 0) {
          j = i + 1;
        }
      }
      int k = mShownWeekCount + j - 1;
      i = k;
      if (localView != null)
      {
        i = k;
        if (localView.getTop() > mBottomBuffer) {
          i = k - 1;
        }
      }
      if (paramBoolean2) {
        mAdapter.setSelectedDay(paramCalendar);
      }
      k = getWeeksSinceMinDate(paramCalendar);
      if ((k >= j) && (k <= i) && (!paramBoolean3))
      {
        if (paramBoolean2) {
          setMonthDisplayed(paramCalendar);
        }
      }
      else
      {
        mFirstDayOfMonth.setTimeInMillis(paramCalendar.getTimeInMillis());
        mFirstDayOfMonth.set(5, 1);
        setMonthDisplayed(mFirstDayOfMonth);
        if (mFirstDayOfMonth.before(mMinDate)) {}
        for (j = 0;; j = getWeeksSinceMinDate(mFirstDayOfMonth)) {
          break;
        }
        mPreviousScrollState = 2;
        if (paramBoolean1)
        {
          mListView.smoothScrollToPositionFromTop(j, mListScrollTopOffset, 1000);
        }
        else
        {
          mListView.setSelectionFromTop(j, mListScrollTopOffset);
          onScrollStateChanged(mListView, 0);
        }
      }
      return;
    }
    throw new IllegalArgumentException("timeInMillis must be between the values of getMinDate() and getMaxDate()");
  }
  
  private void invalidateAllWeekViews()
  {
    int i = mListView.getChildCount();
    for (int j = 0; j < i; j++) {
      mListView.getChildAt(j).invalidate();
    }
  }
  
  private static boolean isSameDate(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    int i = paramCalendar1.get(6);
    int j = paramCalendar2.get(6);
    boolean bool = true;
    if ((i != j) || (paramCalendar1.get(1) != paramCalendar2.get(1))) {
      bool = false;
    }
    return bool;
  }
  
  private void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    paramInt1 = 0;
    WeekView localWeekView = (WeekView)paramAbsListView.getChildAt(0);
    if (localWeekView == null) {
      return;
    }
    long l = paramAbsListView.getFirstVisiblePosition() * localWeekView.getHeight() - localWeekView.getBottom();
    if (l < mPreviousScrollPosition)
    {
      mIsScrollingUp = true;
    }
    else
    {
      if (l <= mPreviousScrollPosition) {
        return;
      }
      mIsScrollingUp = false;
    }
    if (localWeekView.getBottom() < mWeekMinVisibleHeight) {
      paramInt1 = 1;
    }
    if (mIsScrollingUp) {
      localWeekView = (WeekView)paramAbsListView.getChildAt(2 + paramInt1);
    } else if (paramInt1 != 0) {
      localWeekView = (WeekView)paramAbsListView.getChildAt(paramInt1);
    }
    if (localWeekView != null)
    {
      if (mIsScrollingUp) {
        paramInt1 = localWeekView.getMonthOfFirstWeekDay();
      } else {
        paramInt1 = localWeekView.getMonthOfLastWeekDay();
      }
      if ((mCurrentMonthDisplayed == 11) && (paramInt1 == 0)) {
        paramInt1 = 1;
      }
      for (;;)
      {
        break;
        if ((mCurrentMonthDisplayed == 0) && (paramInt1 == 11)) {
          paramInt1 = -1;
        } else {
          paramInt1 -= mCurrentMonthDisplayed;
        }
      }
      if (((!mIsScrollingUp) && (paramInt1 > 0)) || ((mIsScrollingUp) && (paramInt1 < 0)))
      {
        paramAbsListView = localWeekView.getFirstDay();
        if (mIsScrollingUp) {
          paramAbsListView.add(5, -7);
        } else {
          paramAbsListView.add(5, 7);
        }
        setMonthDisplayed(paramAbsListView);
      }
    }
    mPreviousScrollPosition = l;
    mPreviousScrollState = mCurrentScrollState;
    return;
  }
  
  private void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    mScrollStateChangedRunnable.doScrollStateChange(paramAbsListView, paramInt);
  }
  
  private void setMonthDisplayed(Calendar paramCalendar)
  {
    mCurrentMonthDisplayed = paramCalendar.get(2);
    mAdapter.setFocusMonth(mCurrentMonthDisplayed);
    long l = paramCalendar.getTimeInMillis();
    paramCalendar = DateUtils.formatDateRange(mContext, l, l, 52);
    mMonthName.setText(paramCalendar);
    mMonthName.invalidate();
  }
  
  private void setUpAdapter()
  {
    if (mAdapter == null)
    {
      mAdapter = new WeeksAdapter(mContext);
      mAdapter.registerDataSetObserver(new DataSetObserver()
      {
        public void onChanged()
        {
          if (mOnDateChangeListener != null)
          {
            Calendar localCalendar = mAdapter.getSelectedDay();
            mOnDateChangeListener.onSelectedDayChange(mDelegator, localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
          }
        }
      });
      mListView.setAdapter(mAdapter);
    }
    mAdapter.notifyDataSetChanged();
  }
  
  private void setUpHeader()
  {
    mDayNamesShort = new String[mDaysPerWeek];
    mDayNamesLong = new String[mDaysPerWeek];
    int i = mFirstDayOfWeek;
    int j = mFirstDayOfWeek;
    int k = mDaysPerWeek;
    while (i < j + k)
    {
      if (i > 7) {
        m = i - 7;
      } else {
        m = i;
      }
      mDayNamesShort[(i - mFirstDayOfWeek)] = DateUtils.getDayOfWeekString(m, 50);
      mDayNamesLong[(i - mFirstDayOfWeek)] = DateUtils.getDayOfWeekString(m, 10);
      i++;
    }
    TextView localTextView = (TextView)mDayNamesHeader.getChildAt(0);
    if (mShowWeekNumber) {
      localTextView.setVisibility(0);
    } else {
      localTextView.setVisibility(8);
    }
    i = 1;
    int m = mDayNamesHeader.getChildCount();
    while (i < m)
    {
      localTextView = (TextView)mDayNamesHeader.getChildAt(i);
      if (mWeekDayTextAppearanceResId > -1) {
        localTextView.setTextAppearance(mWeekDayTextAppearanceResId);
      }
      if (i < mDaysPerWeek + 1)
      {
        localTextView.setText(mDayNamesShort[(i - 1)]);
        localTextView.setContentDescription(mDayNamesLong[(i - 1)]);
        localTextView.setVisibility(0);
      }
      else
      {
        localTextView.setVisibility(8);
      }
      i++;
    }
    mDayNamesHeader.invalidate();
  }
  
  private void setUpListView()
  {
    mListView.setDivider(null);
    mListView.setItemsCanFocus(true);
    mListView.setVerticalScrollBarEnabled(false);
    mListView.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAnonymousAbsListView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        CalendarViewLegacyDelegate.this.onScroll(paramAnonymousAbsListView, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
      }
      
      public void onScrollStateChanged(AbsListView paramAnonymousAbsListView, int paramAnonymousInt)
      {
        CalendarViewLegacyDelegate.this.onScrollStateChanged(paramAnonymousAbsListView, paramAnonymousInt);
      }
    });
    mListView.setFriction(mFriction);
    mListView.setVelocityScale(mVelocityScale);
  }
  
  private void updateDateTextSize()
  {
    TypedArray localTypedArray = mDelegator.getContext().obtainStyledAttributes(mDateTextAppearanceResId, R.styleable.TextAppearance);
    mDateTextSize = localTypedArray.getDimensionPixelSize(0, 14);
    localTypedArray.recycle();
  }
  
  public boolean getBoundsForDate(long paramLong, Rect paramRect)
  {
    Object localObject = Calendar.getInstance();
    ((Calendar)localObject).setTimeInMillis(paramLong);
    int i = mListView.getCount();
    for (int j = 0; j < i; j++)
    {
      WeekView localWeekView = (WeekView)mListView.getChildAt(j);
      if (localWeekView.getBoundsForDate((Calendar)localObject, paramRect))
      {
        localObject = new int[2];
        int[] arrayOfInt = new int[2];
        localWeekView.getLocationOnScreen((int[])localObject);
        mDelegator.getLocationOnScreen(arrayOfInt);
        j = localObject[1] - arrayOfInt[1];
        top += j;
        bottom += j;
        return true;
      }
    }
    return false;
  }
  
  public long getDate()
  {
    return mAdapter.mSelectedDate.getTimeInMillis();
  }
  
  public int getDateTextAppearance()
  {
    return mDateTextAppearanceResId;
  }
  
  public int getFirstDayOfWeek()
  {
    return mFirstDayOfWeek;
  }
  
  public int getFocusedMonthDateColor()
  {
    return mFocusedMonthDateColor;
  }
  
  public long getMaxDate()
  {
    return mMaxDate.getTimeInMillis();
  }
  
  public long getMinDate()
  {
    return mMinDate.getTimeInMillis();
  }
  
  public Drawable getSelectedDateVerticalBar()
  {
    return mSelectedDateVerticalBar;
  }
  
  public int getSelectedWeekBackgroundColor()
  {
    return mSelectedWeekBackgroundColor;
  }
  
  public boolean getShowWeekNumber()
  {
    return mShowWeekNumber;
  }
  
  public int getShownWeekCount()
  {
    return mShownWeekCount;
  }
  
  public int getUnfocusedMonthDateColor()
  {
    return mUnfocusedMonthDateColor;
  }
  
  public int getWeekDayTextAppearance()
  {
    return mWeekDayTextAppearanceResId;
  }
  
  public int getWeekNumberColor()
  {
    return mWeekNumberColor;
  }
  
  public int getWeekSeparatorLineColor()
  {
    return mWeekSeparatorLineColor;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    setCurrentLocale(locale);
  }
  
  protected void setCurrentLocale(Locale paramLocale)
  {
    super.setCurrentLocale(paramLocale);
    mTempDate = getCalendarForLocale(mTempDate, paramLocale);
    mFirstDayOfMonth = getCalendarForLocale(mFirstDayOfMonth, paramLocale);
    mMinDate = getCalendarForLocale(mMinDate, paramLocale);
    mMaxDate = getCalendarForLocale(mMaxDate, paramLocale);
  }
  
  public void setDate(long paramLong)
  {
    setDate(paramLong, false, false);
  }
  
  public void setDate(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    mTempDate.setTimeInMillis(paramLong);
    if (isSameDate(mTempDate, mAdapter.mSelectedDate)) {
      return;
    }
    goTo(mTempDate, paramBoolean1, true, paramBoolean2);
  }
  
  public void setDateTextAppearance(int paramInt)
  {
    if (mDateTextAppearanceResId != paramInt)
    {
      mDateTextAppearanceResId = paramInt;
      updateDateTextSize();
      invalidateAllWeekViews();
    }
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    if (mFirstDayOfWeek == paramInt) {
      return;
    }
    mFirstDayOfWeek = paramInt;
    mAdapter.init();
    mAdapter.notifyDataSetChanged();
    setUpHeader();
  }
  
  public void setFocusedMonthDateColor(int paramInt)
  {
    if (mFocusedMonthDateColor != paramInt)
    {
      mFocusedMonthDateColor = paramInt;
      int i = mListView.getChildCount();
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        WeekView localWeekView = (WeekView)mListView.getChildAt(paramInt);
        if (mHasFocusedDay) {
          localWeekView.invalidate();
        }
      }
    }
  }
  
  public void setMaxDate(long paramLong)
  {
    mTempDate.setTimeInMillis(paramLong);
    if (isSameDate(mTempDate, mMaxDate)) {
      return;
    }
    mMaxDate.setTimeInMillis(paramLong);
    mAdapter.init();
    Calendar localCalendar = mAdapter.mSelectedDate;
    if (localCalendar.after(mMaxDate)) {
      setDate(mMaxDate.getTimeInMillis());
    } else {
      goTo(localCalendar, false, true, false);
    }
  }
  
  public void setMinDate(long paramLong)
  {
    mTempDate.setTimeInMillis(paramLong);
    if (isSameDate(mTempDate, mMinDate)) {
      return;
    }
    mMinDate.setTimeInMillis(paramLong);
    Calendar localCalendar = mAdapter.mSelectedDate;
    if (localCalendar.before(mMinDate)) {
      mAdapter.setSelectedDay(mMinDate);
    }
    mAdapter.init();
    if (localCalendar.before(mMinDate)) {
      setDate(mTempDate.getTimeInMillis());
    } else {
      goTo(localCalendar, false, true, false);
    }
  }
  
  public void setOnDateChangeListener(CalendarView.OnDateChangeListener paramOnDateChangeListener)
  {
    mOnDateChangeListener = paramOnDateChangeListener;
  }
  
  public void setSelectedDateVerticalBar(int paramInt)
  {
    setSelectedDateVerticalBar(mDelegator.getContext().getDrawable(paramInt));
  }
  
  public void setSelectedDateVerticalBar(Drawable paramDrawable)
  {
    if (mSelectedDateVerticalBar != paramDrawable)
    {
      mSelectedDateVerticalBar = paramDrawable;
      int i = mListView.getChildCount();
      for (int j = 0; j < i; j++)
      {
        paramDrawable = (WeekView)mListView.getChildAt(j);
        if (mHasSelectedDay) {
          paramDrawable.invalidate();
        }
      }
    }
  }
  
  public void setSelectedWeekBackgroundColor(int paramInt)
  {
    if (mSelectedWeekBackgroundColor != paramInt)
    {
      mSelectedWeekBackgroundColor = paramInt;
      int i = mListView.getChildCount();
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        WeekView localWeekView = (WeekView)mListView.getChildAt(paramInt);
        if (mHasSelectedDay) {
          localWeekView.invalidate();
        }
      }
    }
  }
  
  public void setShowWeekNumber(boolean paramBoolean)
  {
    if (mShowWeekNumber == paramBoolean) {
      return;
    }
    mShowWeekNumber = paramBoolean;
    mAdapter.notifyDataSetChanged();
    setUpHeader();
  }
  
  public void setShownWeekCount(int paramInt)
  {
    if (mShownWeekCount != paramInt)
    {
      mShownWeekCount = paramInt;
      mDelegator.invalidate();
    }
  }
  
  public void setUnfocusedMonthDateColor(int paramInt)
  {
    if (mUnfocusedMonthDateColor != paramInt)
    {
      mUnfocusedMonthDateColor = paramInt;
      int i = mListView.getChildCount();
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        WeekView localWeekView = (WeekView)mListView.getChildAt(paramInt);
        if (mHasUnfocusedDay) {
          localWeekView.invalidate();
        }
      }
    }
  }
  
  public void setWeekDayTextAppearance(int paramInt)
  {
    if (mWeekDayTextAppearanceResId != paramInt)
    {
      mWeekDayTextAppearanceResId = paramInt;
      setUpHeader();
    }
  }
  
  public void setWeekNumberColor(int paramInt)
  {
    if (mWeekNumberColor != paramInt)
    {
      mWeekNumberColor = paramInt;
      if (mShowWeekNumber) {
        invalidateAllWeekViews();
      }
    }
  }
  
  public void setWeekSeparatorLineColor(int paramInt)
  {
    if (mWeekSeparatorLineColor != paramInt)
    {
      mWeekSeparatorLineColor = paramInt;
      invalidateAllWeekViews();
    }
  }
  
  private class ScrollStateRunnable
    implements Runnable
  {
    private int mNewState;
    private AbsListView mView;
    
    private ScrollStateRunnable() {}
    
    public void doScrollStateChange(AbsListView paramAbsListView, int paramInt)
    {
      mView = paramAbsListView;
      mNewState = paramInt;
      mDelegator.removeCallbacks(this);
      mDelegator.postDelayed(this, 40L);
    }
    
    public void run()
    {
      CalendarViewLegacyDelegate.access$1002(CalendarViewLegacyDelegate.this, mNewState);
      if ((mNewState == 0) && (mPreviousScrollState != 0))
      {
        View localView = mView.getChildAt(0);
        if (localView == null) {
          return;
        }
        int i = localView.getBottom() - mListScrollTopOffset;
        if (i > mListScrollTopOffset) {
          if (mIsScrollingUp) {
            mView.smoothScrollBy(i - localView.getHeight(), 500);
          } else {
            mView.smoothScrollBy(i, 500);
          }
        }
      }
      CalendarViewLegacyDelegate.access$1102(CalendarViewLegacyDelegate.this, mNewState);
    }
  }
  
  private class WeekView
    extends View
  {
    private String[] mDayNumbers;
    private final Paint mDrawPaint = new Paint();
    private Calendar mFirstDay;
    private boolean[] mFocusDay;
    private boolean mHasFocusedDay;
    private boolean mHasSelectedDay = false;
    private boolean mHasUnfocusedDay;
    private int mHeight;
    private int mLastWeekDayMonth = -1;
    private final Paint mMonthNumDrawPaint = new Paint();
    private int mMonthOfFirstWeekDay = -1;
    private int mNumCells;
    private int mSelectedDay = -1;
    private int mSelectedLeft = -1;
    private int mSelectedRight = -1;
    private final Rect mTempRect = new Rect();
    private int mWeek = -1;
    private int mWidth;
    
    public WeekView(Context paramContext)
    {
      super();
      initializePaints();
    }
    
    private void drawBackground(Canvas paramCanvas)
    {
      if (!mHasSelectedDay) {
        return;
      }
      mDrawPaint.setColor(mSelectedWeekBackgroundColor);
      mTempRect.top = mWeekSeparatorLineWidth;
      mTempRect.bottom = mHeight;
      boolean bool = isLayoutRtl();
      int i = 0;
      Rect localRect;
      if (bool)
      {
        mTempRect.left = 0;
        mTempRect.right = (mSelectedLeft - 2);
      }
      else
      {
        localRect = mTempRect;
        if (mShowWeekNumber) {
          i = mWidth / mNumCells;
        }
        left = i;
        mTempRect.right = (mSelectedLeft - 2);
      }
      paramCanvas.drawRect(mTempRect, mDrawPaint);
      if (bool)
      {
        mTempRect.left = (mSelectedRight + 3);
        localRect = mTempRect;
        if (mShowWeekNumber) {
          i = mWidth - mWidth / mNumCells;
        } else {
          i = mWidth;
        }
        right = i;
      }
      else
      {
        mTempRect.left = (mSelectedRight + 3);
        mTempRect.right = mWidth;
      }
      paramCanvas.drawRect(mTempRect, mDrawPaint);
    }
    
    private void drawSelectedDateVerticalBars(Canvas paramCanvas)
    {
      if (!mHasSelectedDay) {
        return;
      }
      mSelectedDateVerticalBar.setBounds(mSelectedLeft - mSelectedDateVerticalBarWidth / 2, mWeekSeparatorLineWidth, mSelectedLeft + mSelectedDateVerticalBarWidth / 2, mHeight);
      mSelectedDateVerticalBar.draw(paramCanvas);
      mSelectedDateVerticalBar.setBounds(mSelectedRight - mSelectedDateVerticalBarWidth / 2, mWeekSeparatorLineWidth, mSelectedRight + mSelectedDateVerticalBarWidth / 2, mHeight);
      mSelectedDateVerticalBar.draw(paramCanvas);
    }
    
    private void drawWeekNumbersAndDates(Canvas paramCanvas)
    {
      float f = mDrawPaint.getTextSize();
      int i = (int)((mHeight + f) / 2.0F) - mWeekSeparatorLineWidth;
      int j = mNumCells;
      int k = 2 * j;
      mDrawPaint.setTextAlign(Paint.Align.CENTER);
      mDrawPaint.setTextSize(mDateTextSize);
      int m = 0;
      int n = 0;
      Paint localPaint;
      if (isLayoutRtl())
      {
        while (n < j - 1)
        {
          localPaint = mMonthNumDrawPaint;
          if (mFocusDay[n] != 0) {
            m = mFocusedMonthDateColor;
          } else {
            m = mUnfocusedMonthDateColor;
          }
          localPaint.setColor(m);
          m = (2 * n + 1) * mWidth / k;
          paramCanvas.drawText(mDayNumbers[(j - 1 - n)], m, i, mMonthNumDrawPaint);
          n++;
        }
        if (mShowWeekNumber)
        {
          mDrawPaint.setColor(mWeekNumberColor);
          n = mWidth;
          m = mWidth / k;
          paramCanvas.drawText(mDayNumbers[0], n - m, i, mDrawPaint);
        }
      }
      else
      {
        n = m;
        if (mShowWeekNumber)
        {
          mDrawPaint.setColor(mWeekNumberColor);
          n = mWidth / k;
          paramCanvas.drawText(mDayNumbers[0], n, i, mDrawPaint);
        }
        for (n = 0 + 1; n < j; n++)
        {
          localPaint = mMonthNumDrawPaint;
          if (mFocusDay[n] != 0) {
            m = mFocusedMonthDateColor;
          } else {
            m = mUnfocusedMonthDateColor;
          }
          localPaint.setColor(m);
          m = (2 * n + 1) * mWidth / k;
          paramCanvas.drawText(mDayNumbers[n], m, i, mMonthNumDrawPaint);
        }
      }
    }
    
    private void drawWeekSeparators(Canvas paramCanvas)
    {
      int i = mListView.getFirstVisiblePosition();
      int j = i;
      if (mListView.getChildAt(0).getTop() < 0) {
        j = i + 1;
      }
      if (j == mWeek) {
        return;
      }
      mDrawPaint.setColor(mWeekSeparatorLineColor);
      mDrawPaint.setStrokeWidth(mWeekSeparatorLineWidth);
      float f1;
      float f2;
      if (isLayoutRtl())
      {
        f1 = 0.0F;
        if (mShowWeekNumber) {}
        for (j = mWidth - mWidth / mNumCells;; j = mWidth)
        {
          f2 = j;
          break;
        }
      }
      for (;;)
      {
        break;
        if (mShowWeekNumber) {
          f1 = mWidth / mNumCells;
        } else {
          f1 = 0.0F;
        }
        f2 = mWidth;
      }
      paramCanvas.drawLine(f1, 0.0F, f2, 0.0F, mDrawPaint);
    }
    
    private void initializePaints()
    {
      mDrawPaint.setFakeBoldText(false);
      mDrawPaint.setAntiAlias(true);
      mDrawPaint.setStyle(Paint.Style.FILL);
      mMonthNumDrawPaint.setFakeBoldText(true);
      mMonthNumDrawPaint.setAntiAlias(true);
      mMonthNumDrawPaint.setStyle(Paint.Style.FILL);
      mMonthNumDrawPaint.setTextAlign(Paint.Align.CENTER);
      mMonthNumDrawPaint.setTextSize(mDateTextSize);
    }
    
    private void updateSelectionPositions()
    {
      if (mHasSelectedDay)
      {
        boolean bool = isLayoutRtl();
        int i = mSelectedDay - mFirstDayOfWeek;
        int j = i;
        if (i < 0) {
          j = i + 7;
        }
        i = j;
        if (mShowWeekNumber)
        {
          i = j;
          if (!bool) {
            i = j + 1;
          }
        }
        if (bool) {
          mSelectedLeft = ((mDaysPerWeek - 1 - i) * mWidth / mNumCells);
        } else {
          mSelectedLeft = (mWidth * i / mNumCells);
        }
        mSelectedRight = (mSelectedLeft + mWidth / mNumCells);
      }
    }
    
    public boolean getBoundsForDate(Calendar paramCalendar, Rect paramRect)
    {
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTime(mFirstDay.getTime());
      for (int i = 0; i < mDaysPerWeek; i++)
      {
        if ((paramCalendar.get(1) == localCalendar.get(1)) && (paramCalendar.get(2) == localCalendar.get(2)) && (paramCalendar.get(5) == localCalendar.get(5)))
        {
          int j = mWidth / mNumCells;
          if (isLayoutRtl())
          {
            if (mShowWeekNumber) {
              i = mNumCells - i - 2;
            } else {
              i = mNumCells - i - 1;
            }
            left = (i * j);
          }
          else
          {
            if (mShowWeekNumber) {
              i++;
            }
            left = (i * j);
          }
          top = 0;
          right = (left + j);
          bottom = getHeight();
          return true;
        }
        localCalendar.add(5, 1);
      }
      return false;
    }
    
    public boolean getDayFromLocation(float paramFloat, Calendar paramCalendar)
    {
      boolean bool = isLayoutRtl();
      int i;
      int j;
      int k;
      if (bool)
      {
        i = 0;
        if (mShowWeekNumber) {
          j = mWidth - mWidth / mNumCells;
        } else {
          j = mWidth;
        }
        k = j;
      }
      else
      {
        if (mShowWeekNumber) {
          j = mWidth / mNumCells;
        } else {
          j = 0;
        }
        k = mWidth;
        i = j;
      }
      if ((paramFloat >= i) && (paramFloat <= k))
      {
        k = (int)((paramFloat - i) * mDaysPerWeek / (k - i));
        j = k;
        if (bool) {
          j = mDaysPerWeek - 1 - k;
        }
        paramCalendar.setTimeInMillis(mFirstDay.getTimeInMillis());
        paramCalendar.add(5, j);
        return true;
      }
      paramCalendar.clear();
      return false;
    }
    
    public Calendar getFirstDay()
    {
      return mFirstDay;
    }
    
    public int getMonthOfFirstWeekDay()
    {
      return mMonthOfFirstWeekDay;
    }
    
    public int getMonthOfLastWeekDay()
    {
      return mLastWeekDayMonth;
    }
    
    public void init(int paramInt1, int paramInt2, int paramInt3)
    {
      mSelectedDay = paramInt2;
      boolean bool;
      if (mSelectedDay != -1) {
        bool = true;
      } else {
        bool = false;
      }
      mHasSelectedDay = bool;
      if (mShowWeekNumber) {
        paramInt2 = mDaysPerWeek + 1;
      } else {
        paramInt2 = mDaysPerWeek;
      }
      mNumCells = paramInt2;
      mWeek = paramInt1;
      mTempDate.setTimeInMillis(mMinDate.getTimeInMillis());
      mTempDate.add(3, mWeek);
      mTempDate.setFirstDayOfWeek(mFirstDayOfWeek);
      mDayNumbers = new String[mNumCells];
      mFocusDay = new boolean[mNumCells];
      paramInt1 = 0;
      if (mShowWeekNumber)
      {
        mDayNumbers[0] = String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(mTempDate.get(3)) });
        paramInt1 = 0 + 1;
      }
      paramInt2 = mFirstDayOfWeek;
      int i = mTempDate.get(7);
      mTempDate.add(5, paramInt2 - i);
      mFirstDay = ((Calendar)mTempDate.clone());
      mMonthOfFirstWeekDay = mTempDate.get(2);
      mHasUnfocusedDay = true;
      while (paramInt1 < mNumCells)
      {
        if (mTempDate.get(2) == paramInt3) {
          bool = true;
        } else {
          bool = false;
        }
        mFocusDay[paramInt1] = bool;
        mHasFocusedDay |= bool;
        int j = mHasUnfocusedDay;
        if (!bool) {
          paramInt2 = 1;
        } else {
          paramInt2 = 0;
        }
        mHasUnfocusedDay = (j & paramInt2);
        if ((!mTempDate.before(mMinDate)) && (!mTempDate.after(mMaxDate))) {
          mDayNumbers[paramInt1] = String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(mTempDate.get(5)) });
        } else {
          mDayNumbers[paramInt1] = "";
        }
        mTempDate.add(5, 1);
        paramInt1++;
      }
      if (mTempDate.get(5) == 1) {
        mTempDate.add(5, -1);
      }
      mLastWeekDayMonth = mTempDate.get(2);
      updateSelectionPositions();
    }
    
    protected void onDraw(Canvas paramCanvas)
    {
      drawBackground(paramCanvas);
      drawWeekNumbersAndDates(paramCanvas);
      drawWeekSeparators(paramCanvas);
      drawSelectedDateVerticalBars(paramCanvas);
    }
    
    protected void onMeasure(int paramInt1, int paramInt2)
    {
      mHeight = ((mListView.getHeight() - mListView.getPaddingTop() - mListView.getPaddingBottom()) / mShownWeekCount);
      setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), mHeight);
    }
    
    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      mWidth = paramInt1;
      updateSelectionPositions();
    }
  }
  
  private class WeeksAdapter
    extends BaseAdapter
    implements View.OnTouchListener
  {
    private int mFocusedMonth;
    private GestureDetector mGestureDetector;
    private final Calendar mSelectedDate = Calendar.getInstance();
    private int mSelectedWeek;
    private int mTotalWeekCount;
    
    public WeeksAdapter(Context paramContext)
    {
      mContext = paramContext;
      mGestureDetector = new GestureDetector(mContext, new CalendarGestureListener());
      init();
    }
    
    private void init()
    {
      mSelectedWeek = CalendarViewLegacyDelegate.this.getWeeksSinceMinDate(mSelectedDate);
      mTotalWeekCount = CalendarViewLegacyDelegate.this.getWeeksSinceMinDate(mMaxDate);
      if ((mMinDate.get(7) != mFirstDayOfWeek) || (mMaxDate.get(7) != mFirstDayOfWeek)) {
        mTotalWeekCount += 1;
      }
      notifyDataSetChanged();
    }
    
    private void onDateTapped(Calendar paramCalendar)
    {
      setSelectedDay(paramCalendar);
      CalendarViewLegacyDelegate.this.setMonthDisplayed(paramCalendar);
    }
    
    public int getCount()
    {
      return mTotalWeekCount;
    }
    
    public Object getItem(int paramInt)
    {
      return null;
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public Calendar getSelectedDay()
    {
      return mSelectedDate;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView != null)
      {
        paramView = (CalendarViewLegacyDelegate.WeekView)paramView;
      }
      else
      {
        paramView = new CalendarViewLegacyDelegate.WeekView(CalendarViewLegacyDelegate.this, mContext);
        paramView.setLayoutParams(new AbsListView.LayoutParams(-2, -2));
        paramView.setClickable(true);
        paramView.setOnTouchListener(this);
      }
      int i;
      if (mSelectedWeek == paramInt) {
        i = mSelectedDate.get(7);
      } else {
        i = -1;
      }
      paramView.init(paramInt, i, mFocusedMonth);
      return paramView;
    }
    
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      if ((mListView.isEnabled()) && (mGestureDetector.onTouchEvent(paramMotionEvent)))
      {
        if (!((CalendarViewLegacyDelegate.WeekView)paramView).getDayFromLocation(paramMotionEvent.getX(), mTempDate)) {
          return true;
        }
        if ((!mTempDate.before(mMinDate)) && (!mTempDate.after(mMaxDate)))
        {
          onDateTapped(mTempDate);
          return true;
        }
        return true;
      }
      return false;
    }
    
    public void setFocusMonth(int paramInt)
    {
      if (mFocusedMonth == paramInt) {
        return;
      }
      mFocusedMonth = paramInt;
      notifyDataSetChanged();
    }
    
    public void setSelectedDay(Calendar paramCalendar)
    {
      if ((paramCalendar.get(6) == mSelectedDate.get(6)) && (paramCalendar.get(1) == mSelectedDate.get(1))) {
        return;
      }
      mSelectedDate.setTimeInMillis(paramCalendar.getTimeInMillis());
      mSelectedWeek = CalendarViewLegacyDelegate.this.getWeeksSinceMinDate(mSelectedDate);
      mFocusedMonth = mSelectedDate.get(2);
      notifyDataSetChanged();
    }
    
    class CalendarGestureListener
      extends GestureDetector.SimpleOnGestureListener
    {
      CalendarGestureListener() {}
      
      public boolean onSingleTapUp(MotionEvent paramMotionEvent)
      {
        return true;
      }
    }
  }
}
