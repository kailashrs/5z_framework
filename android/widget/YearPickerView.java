package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

class YearPickerView
  extends ListView
{
  private final YearAdapter mAdapter;
  private final int mChildSize;
  private OnYearSelectedListener mOnYearSelectedListener;
  private final int mViewSize;
  
  public YearPickerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842868);
  }
  
  public YearPickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public YearPickerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    paramContext = paramContext.getResources();
    mViewSize = paramContext.getDimensionPixelOffset(17105122);
    mChildSize = paramContext.getDimensionPixelOffset(17105123);
    setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        paramAnonymousInt = mAdapter.getYearForPosition(paramAnonymousInt);
        mAdapter.setSelection(paramAnonymousInt);
        if (mOnYearSelectedListener != null) {
          mOnYearSelectedListener.onYearChanged(YearPickerView.this, paramAnonymousInt);
        }
      }
    });
    mAdapter = new YearAdapter(getContext());
    setAdapter(mAdapter);
  }
  
  public int getFirstPositionOffset()
  {
    View localView = getChildAt(0);
    if (localView == null) {
      return 0;
    }
    return localView.getTop();
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    if (paramAccessibilityEvent.getEventType() == 4096)
    {
      paramAccessibilityEvent.setFromIndex(0);
      paramAccessibilityEvent.setToIndex(0);
    }
  }
  
  public void setOnYearSelectedListener(OnYearSelectedListener paramOnYearSelectedListener)
  {
    mOnYearSelectedListener = paramOnYearSelectedListener;
  }
  
  public void setRange(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    mAdapter.setRange(paramCalendar1, paramCalendar2);
  }
  
  public void setSelectionCentered(int paramInt)
  {
    setSelectionFromTop(paramInt, mViewSize / 2 - mChildSize / 2);
  }
  
  public void setYear(final int paramInt)
  {
    mAdapter.setSelection(paramInt);
    post(new Runnable()
    {
      public void run()
      {
        int i = mAdapter.getPositionForYear(paramInt);
        if ((i >= 0) && (i < getCount())) {
          setSelectionCentered(i);
        }
      }
    });
  }
  
  public static abstract interface OnYearSelectedListener
  {
    public abstract void onYearChanged(YearPickerView paramYearPickerView, int paramInt);
  }
  
  private static class YearAdapter
    extends BaseAdapter
  {
    private static final int ITEM_LAYOUT = 17367362;
    private static final int ITEM_TEXT_ACTIVATED_APPEARANCE = 16974961;
    private static final int ITEM_TEXT_APPEARANCE = 16974960;
    private int mActivatedYear;
    private int mCount;
    private final LayoutInflater mInflater;
    private int mMinYear;
    
    public YearAdapter(Context paramContext)
    {
      mInflater = LayoutInflater.from(paramContext);
    }
    
    public boolean areAllItemsEnabled()
    {
      return true;
    }
    
    public int getCount()
    {
      return mCount;
    }
    
    public Integer getItem(int paramInt)
    {
      return Integer.valueOf(getYearForPosition(paramInt));
    }
    
    public long getItemId(int paramInt)
    {
      return getYearForPosition(paramInt);
    }
    
    public int getItemViewType(int paramInt)
    {
      return 0;
    }
    
    public int getPositionForYear(int paramInt)
    {
      return paramInt - mMinYear;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      boolean bool = true;
      int i;
      if (paramView == null) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        paramView = (TextView)mInflater.inflate(17367362, paramViewGroup, false);
      } else {
        paramView = (TextView)paramView;
      }
      int j = getYearForPosition(paramInt);
      if (mActivatedYear != j) {
        bool = false;
      }
      if ((i != 0) || (paramView.isActivated() != bool))
      {
        if (bool) {
          paramInt = 16974961;
        } else {
          paramInt = 16974960;
        }
        paramView.setTextAppearance(paramInt);
        paramView.setActivated(bool);
      }
      paramView.setText(Integer.toString(j));
      return paramView;
    }
    
    public int getViewTypeCount()
    {
      return 1;
    }
    
    public int getYearForPosition(int paramInt)
    {
      return mMinYear + paramInt;
    }
    
    public boolean hasStableIds()
    {
      return true;
    }
    
    public boolean isEmpty()
    {
      return false;
    }
    
    public boolean isEnabled(int paramInt)
    {
      return true;
    }
    
    public void setRange(Calendar paramCalendar1, Calendar paramCalendar2)
    {
      int i = paramCalendar1.get(1);
      int j = paramCalendar2.get(1) - i + 1;
      if ((mMinYear != i) || (mCount != j))
      {
        mMinYear = i;
        mCount = j;
        notifyDataSetInvalidated();
      }
    }
    
    public boolean setSelection(int paramInt)
    {
      if (mActivatedYear != paramInt)
      {
        mActivatedYear = paramInt;
        notifyDataSetChanged();
        return true;
      }
      return false;
    }
  }
}
