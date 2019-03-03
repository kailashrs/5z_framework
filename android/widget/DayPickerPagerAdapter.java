package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.PagerAdapter;

class DayPickerPagerAdapter
  extends PagerAdapter
{
  private static final int MONTHS_IN_YEAR = 12;
  private ColorStateList mCalendarTextColor;
  private final int mCalendarViewId;
  private int mCount;
  private ColorStateList mDayHighlightColor;
  private int mDayOfWeekTextAppearance;
  private ColorStateList mDaySelectorColor;
  private int mDayTextAppearance;
  private int mFirstDayOfWeek;
  private final LayoutInflater mInflater;
  private final SparseArray<ViewHolder> mItems = new SparseArray();
  private final int mLayoutResId;
  private final Calendar mMaxDate = Calendar.getInstance();
  private final Calendar mMinDate = Calendar.getInstance();
  private int mMonthTextAppearance;
  private final SimpleMonthView.OnDayClickListener mOnDayClickListener = new SimpleMonthView.OnDayClickListener()
  {
    public void onDayClick(SimpleMonthView paramAnonymousSimpleMonthView, Calendar paramAnonymousCalendar)
    {
      if (paramAnonymousCalendar != null)
      {
        setSelectedDay(paramAnonymousCalendar);
        if (mOnDaySelectedListener != null) {
          mOnDaySelectedListener.onDaySelected(DayPickerPagerAdapter.this, paramAnonymousCalendar);
        }
      }
    }
  };
  private OnDaySelectedListener mOnDaySelectedListener;
  private Calendar mSelectedDay = null;
  
  public DayPickerPagerAdapter(Context paramContext, int paramInt1, int paramInt2)
  {
    mInflater = LayoutInflater.from(paramContext);
    mLayoutResId = paramInt1;
    mCalendarViewId = paramInt2;
    paramContext = paramContext.obtainStyledAttributes(new int[] { 16843820 });
    mDayHighlightColor = paramContext.getColorStateList(0);
    paramContext.recycle();
  }
  
  private int getMonthForPosition(int paramInt)
  {
    return (mMinDate.get(2) + paramInt) % 12;
  }
  
  private int getPositionForDay(Calendar paramCalendar)
  {
    if (paramCalendar == null) {
      return -1;
    }
    return (paramCalendar.get(1) - mMinDate.get(1)) * 12 + (paramCalendar.get(2) - mMinDate.get(2));
  }
  
  private int getYearForPosition(int paramInt)
  {
    paramInt = (mMinDate.get(2) + paramInt) / 12;
    return mMinDate.get(1) + paramInt;
  }
  
  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    paramViewGroup.removeView(container);
    mItems.remove(paramInt);
  }
  
  public boolean getBoundsForDate(Calendar paramCalendar, Rect paramRect)
  {
    int i = getPositionForDay(paramCalendar);
    ViewHolder localViewHolder = (ViewHolder)mItems.get(i, null);
    if (localViewHolder == null) {
      return false;
    }
    i = paramCalendar.get(5);
    return calendar.getBoundsForDay(i, paramRect);
  }
  
  public int getCount()
  {
    return mCount;
  }
  
  int getDayOfWeekTextAppearance()
  {
    return mDayOfWeekTextAppearance;
  }
  
  int getDayTextAppearance()
  {
    return mDayTextAppearance;
  }
  
  public int getFirstDayOfWeek()
  {
    return mFirstDayOfWeek;
  }
  
  public int getItemPosition(Object paramObject)
  {
    return position;
  }
  
  public CharSequence getPageTitle(int paramInt)
  {
    SimpleMonthView localSimpleMonthView = mItems.get(paramInt)).calendar;
    if (localSimpleMonthView != null) {
      return localSimpleMonthView.getMonthYearLabel();
    }
    return null;
  }
  
  SimpleMonthView getView(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return calendar;
  }
  
  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    View localView = mInflater.inflate(mLayoutResId, paramViewGroup, false);
    Object localObject = (SimpleMonthView)localView.findViewById(mCalendarViewId);
    ((SimpleMonthView)localObject).setOnDayClickListener(mOnDayClickListener);
    ((SimpleMonthView)localObject).setMonthTextAppearance(mMonthTextAppearance);
    ((SimpleMonthView)localObject).setDayOfWeekTextAppearance(mDayOfWeekTextAppearance);
    ((SimpleMonthView)localObject).setDayTextAppearance(mDayTextAppearance);
    if (mDaySelectorColor != null) {
      ((SimpleMonthView)localObject).setDaySelectorColor(mDaySelectorColor);
    }
    if (mDayHighlightColor != null) {
      ((SimpleMonthView)localObject).setDayHighlightColor(mDayHighlightColor);
    }
    if (mCalendarTextColor != null)
    {
      ((SimpleMonthView)localObject).setMonthTextColor(mCalendarTextColor);
      ((SimpleMonthView)localObject).setDayOfWeekTextColor(mCalendarTextColor);
      ((SimpleMonthView)localObject).setDayTextColor(mCalendarTextColor);
    }
    int i = getMonthForPosition(paramInt);
    int j = getYearForPosition(paramInt);
    int k;
    if ((mSelectedDay != null) && (mSelectedDay.get(2) == i)) {
      k = mSelectedDay.get(5);
    } else {
      k = -1;
    }
    int m;
    if ((mMinDate.get(2) == i) && (mMinDate.get(1) == j)) {
      m = mMinDate.get(5);
    } else {
      m = 1;
    }
    int n;
    if ((mMaxDate.get(2) == i) && (mMaxDate.get(1) == j)) {
      n = mMaxDate.get(5);
    } else {
      n = 31;
    }
    ((SimpleMonthView)localObject).setMonthParams(k, i, j, mFirstDayOfWeek, m, n);
    localObject = new ViewHolder(paramInt, localView, (SimpleMonthView)localObject);
    mItems.put(paramInt, localObject);
    paramViewGroup.addView(localView);
    return localObject;
  }
  
  public boolean isViewFromObject(View paramView, Object paramObject)
  {
    boolean bool;
    if (paramView == container) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void setCalendarTextColor(ColorStateList paramColorStateList)
  {
    mCalendarTextColor = paramColorStateList;
    notifyDataSetChanged();
  }
  
  void setDayOfWeekTextAppearance(int paramInt)
  {
    mDayOfWeekTextAppearance = paramInt;
    notifyDataSetChanged();
  }
  
  void setDaySelectorColor(ColorStateList paramColorStateList)
  {
    mDaySelectorColor = paramColorStateList;
    notifyDataSetChanged();
  }
  
  void setDayTextAppearance(int paramInt)
  {
    mDayTextAppearance = paramInt;
    notifyDataSetChanged();
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    mFirstDayOfWeek = paramInt;
    int i = mItems.size();
    for (int j = 0; j < i; j++) {
      mItems.valueAt(j)).calendar.setFirstDayOfWeek(paramInt);
    }
  }
  
  void setMonthTextAppearance(int paramInt)
  {
    mMonthTextAppearance = paramInt;
    notifyDataSetChanged();
  }
  
  public void setOnDaySelectedListener(OnDaySelectedListener paramOnDaySelectedListener)
  {
    mOnDaySelectedListener = paramOnDaySelectedListener;
  }
  
  public void setRange(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    mMinDate.setTimeInMillis(paramCalendar1.getTimeInMillis());
    mMaxDate.setTimeInMillis(paramCalendar2.getTimeInMillis());
    mCount = (12 * (mMaxDate.get(1) - mMinDate.get(1)) + (mMaxDate.get(2) - mMinDate.get(2)) + 1);
    notifyDataSetChanged();
  }
  
  public void setSelectedDay(Calendar paramCalendar)
  {
    int i = getPositionForDay(mSelectedDay);
    int j = getPositionForDay(paramCalendar);
    ViewHolder localViewHolder;
    if ((i != j) && (i >= 0))
    {
      localViewHolder = (ViewHolder)mItems.get(i, null);
      if (localViewHolder != null) {
        calendar.setSelectedDay(-1);
      }
    }
    if (j >= 0)
    {
      localViewHolder = (ViewHolder)mItems.get(j, null);
      if (localViewHolder != null)
      {
        j = paramCalendar.get(5);
        calendar.setSelectedDay(j);
      }
    }
    mSelectedDay = paramCalendar;
  }
  
  public static abstract interface OnDaySelectedListener
  {
    public abstract void onDaySelected(DayPickerPagerAdapter paramDayPickerPagerAdapter, Calendar paramCalendar);
  }
  
  private static class ViewHolder
  {
    public final SimpleMonthView calendar;
    public final View container;
    public final int position;
    
    public ViewHolder(int paramInt, View paramView, SimpleMonthView paramSimpleMonthView)
    {
      position = paramInt;
      container = paramView;
      calendar = paramSimpleMonthView;
    }
  }
}
