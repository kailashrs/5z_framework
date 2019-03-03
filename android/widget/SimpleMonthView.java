package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.MathUtils;
import android.util.StateSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import com.android.internal.R.styleable;
import com.android.internal.widget.ExploreByTouchHelper;
import java.text.NumberFormat;
import java.util.Locale;
import libcore.icu.LocaleData;

class SimpleMonthView
  extends View
{
  private static final int DAYS_IN_WEEK = 7;
  private static final int DEFAULT_SELECTED_DAY = -1;
  private static final int DEFAULT_WEEK_START = 1;
  private static final int MAX_WEEKS_IN_MONTH = 6;
  private static final String MONTH_YEAR_FORMAT = "MMMMy";
  private static final int SELECTED_HIGHLIGHT_ALPHA = 176;
  private int mActivatedDay = -1;
  private final Calendar mCalendar;
  private int mCellWidth;
  private final NumberFormat mDayFormatter;
  private int mDayHeight;
  private final Paint mDayHighlightPaint = new Paint();
  private final Paint mDayHighlightSelectorPaint = new Paint();
  private int mDayOfWeekHeight;
  private final String[] mDayOfWeekLabels = new String[7];
  private final TextPaint mDayOfWeekPaint = new TextPaint();
  private int mDayOfWeekStart;
  private final TextPaint mDayPaint = new TextPaint();
  private final Paint mDaySelectorPaint = new Paint();
  private int mDaySelectorRadius;
  private ColorStateList mDayTextColor;
  private int mDaysInMonth;
  private final int mDesiredCellWidth;
  private final int mDesiredDayHeight;
  private final int mDesiredDayOfWeekHeight;
  private final int mDesiredDaySelectorRadius;
  private final int mDesiredMonthHeight;
  private int mEnabledDayEnd = 31;
  private int mEnabledDayStart = 1;
  private int mHighlightedDay = -1;
  private boolean mIsTouchHighlighted = false;
  private final Locale mLocale;
  private int mMonth;
  private int mMonthHeight;
  private final TextPaint mMonthPaint = new TextPaint();
  private String mMonthYearLabel;
  private OnDayClickListener mOnDayClickListener;
  private int mPaddedHeight;
  private int mPaddedWidth;
  private int mPreviouslyHighlightedDay = -1;
  private int mToday = -1;
  private final MonthViewTouchHelper mTouchHelper;
  private int mWeekStart = 1;
  private int mYear;
  
  public SimpleMonthView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SimpleMonthView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843612);
  }
  
  public SimpleMonthView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public SimpleMonthView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.getResources();
    mDesiredMonthHeight = paramContext.getDimensionPixelSize(17105110);
    mDesiredDayOfWeekHeight = paramContext.getDimensionPixelSize(17105105);
    mDesiredDayHeight = paramContext.getDimensionPixelSize(17105104);
    mDesiredCellWidth = paramContext.getDimensionPixelSize(17105109);
    mDesiredDaySelectorRadius = paramContext.getDimensionPixelSize(17105107);
    mTouchHelper = new MonthViewTouchHelper(this);
    setAccessibilityDelegate(mTouchHelper);
    setImportantForAccessibility(1);
    mLocale = getConfigurationlocale;
    mCalendar = Calendar.getInstance(mLocale);
    mDayFormatter = NumberFormat.getIntegerInstance(mLocale);
    updateMonthYearLabel();
    updateDayOfWeekLabels();
    initPaints(paramContext);
  }
  
  private ColorStateList applyTextAppearance(Paint paramPaint, int paramInt)
  {
    TypedArray localTypedArray = mContext.obtainStyledAttributes(null, R.styleable.TextAppearance, 0, paramInt);
    Object localObject = localTypedArray.getString(12);
    if (localObject != null) {
      paramPaint.setTypeface(Typeface.create((String)localObject, 0));
    }
    paramPaint.setTextSize(localTypedArray.getDimensionPixelSize(0, (int)paramPaint.getTextSize()));
    localObject = localTypedArray.getColorStateList(3);
    if (localObject != null) {
      paramPaint.setColor(((ColorStateList)localObject).getColorForState(ENABLED_STATE_SET, 0));
    }
    localTypedArray.recycle();
    return localObject;
  }
  
  private void drawDays(Canvas paramCanvas)
  {
    TextPaint localTextPaint = mDayPaint;
    int i = mMonthHeight + mDayOfWeekHeight;
    int j = mDayHeight;
    int k = mCellWidth;
    float f = (localTextPaint.ascent() + localTextPaint.descent()) / 2.0F;
    int m = j / 2 + i;
    int n = 1;
    int i3;
    for (int i1 = findDayOffset(); n <= mDaysInMonth; i1 = i3)
    {
      int i2 = k * i1 + k / 2;
      if (isLayoutRtl()) {
        i2 = mPaddedWidth - i2;
      }
      i3 = 0;
      boolean bool = isDayEnabled(n);
      if (bool) {
        i3 = 0x0 | 0x8;
      }
      int i4 = mActivatedDay;
      int i5 = 1;
      int i6;
      if (i4 == n) {
        i6 = 1;
      } else {
        i6 = 0;
      }
      int i7;
      if (mHighlightedDay == n) {
        i7 = 1;
      } else {
        i7 = 0;
      }
      Object localObject;
      if (i6 != 0)
      {
        if (i7 != 0) {
          localObject = mDayHighlightSelectorPaint;
        } else {
          localObject = mDaySelectorPaint;
        }
        paramCanvas.drawCircle(i2, m, mDaySelectorRadius, (Paint)localObject);
        i4 = i3 | 0x20;
      }
      else
      {
        i4 = i3;
        if (i7 != 0)
        {
          i4 = i3 | 0x10;
          if (bool) {
            paramCanvas.drawCircle(i2, m, mDaySelectorRadius, mDayHighlightPaint);
          }
        }
      }
      if (mToday == n) {
        i3 = i5;
      } else {
        i3 = 0;
      }
      if ((i3 != 0) && (i6 == 0))
      {
        i3 = mDaySelectorPaint.getColor();
      }
      else
      {
        localObject = StateSet.get(i4);
        i3 = mDayTextColor.getColorForState((int[])localObject, 0);
      }
      localTextPaint.setColor(i3);
      paramCanvas.drawText(mDayFormatter.format(n), i2, m - f, localTextPaint);
      i1++;
      i4 = m;
      i3 = i1;
      if (i1 == 7)
      {
        i4 = m + j;
        i3 = 0;
      }
      n++;
      m = i4;
    }
  }
  
  private void drawDaysOfWeek(Canvas paramCanvas)
  {
    TextPaint localTextPaint = mDayOfWeekPaint;
    int i = mMonthHeight;
    int j = mDayOfWeekHeight;
    int k = mCellWidth;
    float f = (localTextPaint.ascent() + localTextPaint.descent()) / 2.0F;
    int m = j / 2;
    for (j = 0; j < 7; j++)
    {
      int n = k * j + k / 2;
      if (isLayoutRtl()) {
        n = mPaddedWidth - n;
      }
      paramCanvas.drawText(mDayOfWeekLabels[j], n, m + i - f, localTextPaint);
    }
  }
  
  private void drawMonth(Canvas paramCanvas)
  {
    float f1 = mPaddedWidth / 2.0F;
    float f2 = mMonthPaint.ascent();
    float f3 = mMonthPaint.descent();
    f2 = (mMonthHeight - (f2 + f3)) / 2.0F;
    paramCanvas.drawText(mMonthYearLabel, f1, f2, mMonthPaint);
  }
  
  private void ensureFocusedDay()
  {
    if (mHighlightedDay != -1) {
      return;
    }
    if (mPreviouslyHighlightedDay != -1)
    {
      mHighlightedDay = mPreviouslyHighlightedDay;
      return;
    }
    if (mActivatedDay != -1)
    {
      mHighlightedDay = mActivatedDay;
      return;
    }
    mHighlightedDay = 1;
  }
  
  private int findClosestColumn(Rect paramRect)
  {
    if (paramRect == null) {
      return 3;
    }
    if (mCellWidth == 0) {
      return 0;
    }
    int i = MathUtils.constrain((paramRect.centerX() - mPaddingLeft) / mCellWidth, 0, 6);
    if (isLayoutRtl()) {
      i = 7 - i - 1;
    }
    return i;
  }
  
  private int findClosestRow(Rect paramRect)
  {
    if (paramRect == null) {
      return 3;
    }
    if (mDayHeight == 0) {
      return 0;
    }
    int i = paramRect.centerY();
    paramRect = mDayPaint;
    int j = mMonthHeight;
    int k = mDayOfWeekHeight;
    int m = mDayHeight;
    float f = (paramRect.ascent() + paramRect.descent()) / 2.0F;
    int n = m / 2;
    j = Math.round((int)(i - (n + (j + k) - f)) / m);
    i = findDayOffset() + mDaysInMonth;
    n = i / 7;
    if (i % 7 == 0) {
      i = 1;
    } else {
      i = 0;
    }
    return MathUtils.constrain(j, 0, n - i);
  }
  
  private int findDayOffset()
  {
    int i = mDayOfWeekStart - mWeekStart;
    if (mDayOfWeekStart < mWeekStart) {
      return i + 7;
    }
    return i;
  }
  
  private int getDayAtLocation(int paramInt1, int paramInt2)
  {
    paramInt1 -= getPaddingLeft();
    if ((paramInt1 >= 0) && (paramInt1 < mPaddedWidth))
    {
      int i = mMonthHeight + mDayOfWeekHeight;
      paramInt2 -= getPaddingTop();
      if ((paramInt2 >= i) && (paramInt2 < mPaddedHeight))
      {
        if (isLayoutRtl()) {
          paramInt1 = mPaddedWidth - paramInt1;
        }
        paramInt1 = (paramInt2 - i) / mDayHeight * 7 + paramInt1 * 7 / mPaddedWidth + 1 - findDayOffset();
        if (!isValidDayOfMonth(paramInt1)) {
          return -1;
        }
        return paramInt1;
      }
      return -1;
    }
    return -1;
  }
  
  private static int getDaysInMonth(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IllegalArgumentException("Invalid Month");
    case 3: 
    case 5: 
    case 8: 
    case 10: 
      return 30;
    case 1: 
      if (paramInt2 % 4 == 0) {
        paramInt1 = 29;
      } else {
        paramInt1 = 28;
      }
      return paramInt1;
    }
    return 31;
  }
  
  private void initPaints(Resources paramResources)
  {
    String str1 = paramResources.getString(17039853);
    String str2 = paramResources.getString(17039843);
    String str3 = paramResources.getString(17039844);
    int i = paramResources.getDimensionPixelSize(17105111);
    int j = paramResources.getDimensionPixelSize(17105106);
    int k = paramResources.getDimensionPixelSize(17105108);
    mMonthPaint.setAntiAlias(true);
    mMonthPaint.setTextSize(i);
    mMonthPaint.setTypeface(Typeface.create(str1, 0));
    mMonthPaint.setTextAlign(Paint.Align.CENTER);
    mMonthPaint.setStyle(Paint.Style.FILL);
    mDayOfWeekPaint.setAntiAlias(true);
    mDayOfWeekPaint.setTextSize(j);
    mDayOfWeekPaint.setTypeface(Typeface.create(str2, 0));
    mDayOfWeekPaint.setTextAlign(Paint.Align.CENTER);
    mDayOfWeekPaint.setStyle(Paint.Style.FILL);
    mDaySelectorPaint.setAntiAlias(true);
    mDaySelectorPaint.setStyle(Paint.Style.FILL);
    mDayHighlightPaint.setAntiAlias(true);
    mDayHighlightPaint.setStyle(Paint.Style.FILL);
    mDayHighlightSelectorPaint.setAntiAlias(true);
    mDayHighlightSelectorPaint.setStyle(Paint.Style.FILL);
    mDayPaint.setAntiAlias(true);
    mDayPaint.setTextSize(k);
    mDayPaint.setTypeface(Typeface.create(str3, 0));
    mDayPaint.setTextAlign(Paint.Align.CENTER);
    mDayPaint.setStyle(Paint.Style.FILL);
  }
  
  private boolean isDayEnabled(int paramInt)
  {
    boolean bool;
    if ((paramInt >= mEnabledDayStart) && (paramInt <= mEnabledDayEnd)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isFirstDayOfWeek(int paramInt)
  {
    int i = findDayOffset();
    boolean bool = true;
    if ((i + paramInt - 1) % 7 != 0) {
      bool = false;
    }
    return bool;
  }
  
  private boolean isLastDayOfWeek(int paramInt)
  {
    boolean bool;
    if ((findDayOffset() + paramInt) % 7 == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isValidDayOfMonth(int paramInt)
  {
    boolean bool = true;
    if ((paramInt < 1) || (paramInt > mDaysInMonth)) {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidDayOfWeek(int paramInt)
  {
    boolean bool = true;
    if ((paramInt < 1) || (paramInt > 7)) {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidMonth(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 11)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean moveOneDay(boolean paramBoolean)
  {
    ensureFocusedDay();
    boolean bool = false;
    if (paramBoolean)
    {
      paramBoolean = bool;
      if (!isLastDayOfWeek(mHighlightedDay))
      {
        paramBoolean = bool;
        if (mHighlightedDay < mDaysInMonth)
        {
          mHighlightedDay += 1;
          paramBoolean = true;
        }
      }
    }
    else
    {
      paramBoolean = bool;
      if (!isFirstDayOfWeek(mHighlightedDay))
      {
        paramBoolean = bool;
        if (mHighlightedDay > 1)
        {
          mHighlightedDay -= 1;
          paramBoolean = true;
        }
      }
    }
    return paramBoolean;
  }
  
  private boolean onDayClicked(int paramInt)
  {
    if ((isValidDayOfMonth(paramInt)) && (isDayEnabled(paramInt)))
    {
      if (mOnDayClickListener != null)
      {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(mYear, mMonth, paramInt);
        mOnDayClickListener.onDayClick(this, localCalendar);
      }
      mTouchHelper.sendEventForVirtualView(paramInt, 1);
      return true;
    }
    return false;
  }
  
  private boolean sameDay(int paramInt, Calendar paramCalendar)
  {
    int i = mYear;
    boolean bool = true;
    if ((i != paramCalendar.get(1)) || (mMonth != paramCalendar.get(2)) || (paramInt != paramCalendar.get(5))) {
      bool = false;
    }
    return bool;
  }
  
  private void updateDayOfWeekLabels()
  {
    String[] arrayOfString = getmLocale).tinyWeekdayNames;
    for (int i = 0; i < 7; i++) {
      mDayOfWeekLabels[i] = arrayOfString[((mWeekStart + i - 1) % 7 + 1)];
    }
  }
  
  private void updateMonthYearLabel()
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(DateFormat.getBestDateTimePattern(mLocale, "MMMMy"), mLocale);
    localSimpleDateFormat.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
    mMonthYearLabel = localSimpleDateFormat.format(mCalendar.getTime());
  }
  
  public boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    boolean bool;
    if ((!mTouchHelper.dispatchHoverEvent(paramMotionEvent)) && (!super.dispatchHoverEvent(paramMotionEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean getBoundsForDay(int paramInt, Rect paramRect)
  {
    if (!isValidDayOfMonth(paramInt)) {
      return false;
    }
    int i = paramInt - 1 + findDayOffset();
    paramInt = i % 7;
    int j = mCellWidth;
    if (isLayoutRtl()) {
      paramInt = getWidth() - getPaddingRight() - (paramInt + 1) * j;
    } else {
      paramInt = getPaddingLeft() + paramInt * j;
    }
    int k = i / 7;
    i = mDayHeight;
    int m = mMonthHeight;
    int n = mDayOfWeekHeight;
    n = getPaddingTop() + (m + n) + k * i;
    paramRect.set(paramInt, n, paramInt + j, n + i);
    return true;
  }
  
  public int getCellWidth()
  {
    return mCellWidth;
  }
  
  public void getFocusedRect(Rect paramRect)
  {
    if (mHighlightedDay > 0) {
      getBoundsForDay(mHighlightedDay, paramRect);
    } else {
      super.getFocusedRect(paramRect);
    }
  }
  
  public int getMonthHeight()
  {
    return mMonthHeight;
  }
  
  public String getMonthYearLabel()
  {
    return mMonthYearLabel;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    paramCanvas.translate(i, j);
    drawMonth(paramCanvas);
    drawDaysOfWeek(paramCanvas);
    drawDays(paramCanvas);
    paramCanvas.translate(-i, -j);
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    if (paramBoolean)
    {
      int i = findDayOffset();
      int j = 1;
      if (paramInt != 17)
      {
        if (paramInt != 33)
        {
          if (paramInt != 66)
          {
            if (paramInt == 130)
            {
              j = findClosestColumn(paramRect) - i + 1;
              if (j < 1) {
                j += 7;
              }
              mHighlightedDay = j;
            }
          }
          else
          {
            int k = findClosestRow(paramRect);
            if (k != 0) {
              j = 1 + (k * 7 - i);
            }
            mHighlightedDay = j;
          }
        }
        else
        {
          j = findClosestColumn(paramRect) - i + 7 * ((mDaysInMonth + i) / 7) + 1;
          if (j > mDaysInMonth) {
            j -= 7;
          }
          mHighlightedDay = j;
        }
      }
      else
      {
        j = findClosestRow(paramRect);
        mHighlightedDay = Math.min(mDaysInMonth, (j + 1) * 7 - i);
      }
      ensureFocusedDay();
      invalidate();
    }
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
  }
  
  protected void onFocusLost()
  {
    if (!mIsTouchHighlighted)
    {
      mPreviouslyHighlightedDay = mHighlightedDay;
      mHighlightedDay = -1;
      invalidate();
    }
    super.onFocusLost();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool1 = false;
    int i = paramKeyEvent.getKeyCode();
    boolean bool2;
    if (i != 61)
    {
      if (i != 66)
      {
        switch (i)
        {
        default: 
          bool2 = bool1;
          break;
        case 22: 
          bool2 = bool1;
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          bool2 = moveOneDay(isLayoutRtl() ^ true);
          break;
        case 21: 
          bool2 = bool1;
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          bool2 = moveOneDay(isLayoutRtl());
          break;
        case 20: 
          bool2 = bool1;
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          ensureFocusedDay();
          bool2 = bool1;
          if (mHighlightedDay > mDaysInMonth - 7) {
            break;
          }
          mHighlightedDay += 7;
          bool2 = true;
          break;
        case 19: 
          bool2 = bool1;
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          ensureFocusedDay();
          bool2 = bool1;
          if (mHighlightedDay <= 7) {
            break;
          }
          mHighlightedDay -= 7;
          bool2 = true;
          break;
        }
      }
      else
      {
        bool2 = bool1;
        if (mHighlightedDay != -1)
        {
          onDayClicked(mHighlightedDay);
          return true;
        }
      }
    }
    else
    {
      i = 0;
      if (paramKeyEvent.hasNoModifiers()) {
        i = 2;
      } else if (paramKeyEvent.hasModifiers(1)) {
        i = 1;
      }
      bool2 = bool1;
      if (i != 0)
      {
        ViewParent localViewParent = getParent();
        Object localObject = this;
        View localView;
        do
        {
          localView = ((View)localObject).focusSearch(i);
          if ((localView == null) || (localView == this)) {
            break;
          }
          localObject = localView;
        } while (localView.getParent() == localViewParent);
        if (localView != null)
        {
          localView.requestFocus();
          return true;
        }
        bool2 = bool1;
      }
    }
    if (bool2)
    {
      invalidate();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!paramBoolean) {
      return;
    }
    int i = getPaddingLeft();
    int j = getPaddingTop();
    int k = getPaddingRight();
    int m = getPaddingBottom();
    paramInt1 = paramInt3 - paramInt1 - k - i;
    paramInt2 = paramInt4 - paramInt2 - m - j;
    if ((paramInt1 != mPaddedWidth) && (paramInt2 != mPaddedHeight))
    {
      mPaddedWidth = paramInt1;
      mPaddedHeight = paramInt2;
      paramInt1 = getMeasuredHeight();
      float f = paramInt2 / (paramInt1 - j - m);
      paramInt2 = (int)(mDesiredMonthHeight * f);
      paramInt1 = mPaddedWidth / 7;
      mMonthHeight = paramInt2;
      mDayOfWeekHeight = ((int)(mDesiredDayOfWeekHeight * f));
      mDayHeight = ((int)(mDesiredDayHeight * f));
      mCellWidth = paramInt1;
      paramInt1 /= 2;
      paramInt2 = Math.min(i, k);
      paramInt3 = mDayHeight / 2;
      mDaySelectorRadius = Math.min(mDesiredDaySelectorRadius, Math.min(paramInt1 + paramInt2, paramInt3 + m));
      mTouchHelper.invalidateRoot();
      return;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = mDesiredDayHeight;
    int j = mDesiredDayOfWeekHeight;
    int k = mDesiredMonthHeight;
    int m = getPaddingTop();
    int n = getPaddingBottom();
    setMeasuredDimension(resolveSize(mDesiredCellWidth * 7 + getPaddingStart() + getPaddingEnd(), paramInt1), resolveSize(i * 6 + j + k + m + n, paramInt2));
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    if (!isEnabled()) {
      return null;
    }
    if (getDayAtLocation((int)(paramMotionEvent.getX() + 0.5F), (int)(paramMotionEvent.getY() + 0.5F)) >= 0) {
      return PointerIcon.getSystemIcon(getContext(), 1002);
    }
    return super.onResolvePointerIcon(paramMotionEvent, paramInt);
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    requestLayout();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = (int)(paramMotionEvent.getX() + 0.5F);
    int j = (int)(paramMotionEvent.getY() + 0.5F);
    int k = paramMotionEvent.getAction();
    switch (k)
    {
    default: 
      break;
    case 1: 
      onDayClicked(getDayAtLocation(i, j));
    case 3: 
      mHighlightedDay = -1;
      mIsTouchHighlighted = false;
      invalidate();
      break;
    case 0: 
    case 2: 
      i = getDayAtLocation(i, j);
      mIsTouchHighlighted = true;
      if (mHighlightedDay != i)
      {
        mHighlightedDay = i;
        mPreviouslyHighlightedDay = i;
        invalidate();
      }
      if ((k == 0) && (i < 0)) {
        return false;
      }
      break;
    }
    return true;
  }
  
  void setDayHighlightColor(ColorStateList paramColorStateList)
  {
    int i = paramColorStateList.getColorForState(StateSet.get(24), 0);
    mDayHighlightPaint.setColor(i);
    invalidate();
  }
  
  public void setDayOfWeekTextAppearance(int paramInt)
  {
    applyTextAppearance(mDayOfWeekPaint, paramInt);
    invalidate();
  }
  
  void setDayOfWeekTextColor(ColorStateList paramColorStateList)
  {
    int i = paramColorStateList.getColorForState(ENABLED_STATE_SET, 0);
    mDayOfWeekPaint.setColor(i);
    invalidate();
  }
  
  void setDaySelectorColor(ColorStateList paramColorStateList)
  {
    int i = paramColorStateList.getColorForState(StateSet.get(40), 0);
    mDaySelectorPaint.setColor(i);
    mDayHighlightSelectorPaint.setColor(i);
    mDayHighlightSelectorPaint.setAlpha(176);
    invalidate();
  }
  
  public void setDayTextAppearance(int paramInt)
  {
    ColorStateList localColorStateList = applyTextAppearance(mDayPaint, paramInt);
    if (localColorStateList != null) {
      mDayTextColor = localColorStateList;
    }
    invalidate();
  }
  
  void setDayTextColor(ColorStateList paramColorStateList)
  {
    mDayTextColor = paramColorStateList;
    invalidate();
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    if (isValidDayOfWeek(paramInt)) {
      mWeekStart = paramInt;
    } else {
      mWeekStart = mCalendar.getFirstDayOfWeek();
    }
    updateDayOfWeekLabels();
    mTouchHelper.invalidateRoot();
    invalidate();
  }
  
  void setMonthParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    mActivatedDay = paramInt1;
    if (isValidMonth(paramInt2)) {
      mMonth = paramInt2;
    }
    mYear = paramInt3;
    mCalendar.set(2, mMonth);
    mCalendar.set(1, mYear);
    mCalendar.set(5, 1);
    mDayOfWeekStart = mCalendar.get(7);
    if (isValidDayOfWeek(paramInt4)) {
      mWeekStart = paramInt4;
    } else {
      mWeekStart = mCalendar.getFirstDayOfWeek();
    }
    Calendar localCalendar = Calendar.getInstance();
    mToday = -1;
    mDaysInMonth = getDaysInMonth(mMonth, mYear);
    for (paramInt1 = 0; paramInt1 < mDaysInMonth; paramInt1++)
    {
      paramInt2 = paramInt1 + 1;
      if (sameDay(paramInt2, localCalendar)) {
        mToday = paramInt2;
      }
    }
    mEnabledDayStart = MathUtils.constrain(paramInt5, 1, mDaysInMonth);
    mEnabledDayEnd = MathUtils.constrain(paramInt6, mEnabledDayStart, mDaysInMonth);
    updateMonthYearLabel();
    updateDayOfWeekLabels();
    mTouchHelper.invalidateRoot();
    invalidate();
  }
  
  public void setMonthTextAppearance(int paramInt)
  {
    applyTextAppearance(mMonthPaint, paramInt);
    invalidate();
  }
  
  void setMonthTextColor(ColorStateList paramColorStateList)
  {
    int i = paramColorStateList.getColorForState(ENABLED_STATE_SET, 0);
    mMonthPaint.setColor(i);
    invalidate();
  }
  
  public void setOnDayClickListener(OnDayClickListener paramOnDayClickListener)
  {
    mOnDayClickListener = paramOnDayClickListener;
  }
  
  public void setSelectedDay(int paramInt)
  {
    mActivatedDay = paramInt;
    mTouchHelper.invalidateRoot();
    invalidate();
  }
  
  private class MonthViewTouchHelper
    extends ExploreByTouchHelper
  {
    private static final String DATE_FORMAT = "dd MMMM yyyy";
    private final Calendar mTempCalendar = Calendar.getInstance();
    private final Rect mTempRect = new Rect();
    
    public MonthViewTouchHelper(View paramView)
    {
      super();
    }
    
    private CharSequence getDayDescription(int paramInt)
    {
      if (SimpleMonthView.this.isValidDayOfMonth(paramInt))
      {
        mTempCalendar.set(mYear, mMonth, paramInt);
        return DateFormat.format("dd MMMM yyyy", mTempCalendar.getTimeInMillis());
      }
      return "";
    }
    
    private CharSequence getDayText(int paramInt)
    {
      if (SimpleMonthView.this.isValidDayOfMonth(paramInt)) {
        return mDayFormatter.format(paramInt);
      }
      return null;
    }
    
    protected int getVirtualViewAt(float paramFloat1, float paramFloat2)
    {
      int i = SimpleMonthView.this.getDayAtLocation((int)(paramFloat1 + 0.5F), (int)(0.5F + paramFloat2));
      if (i != -1) {
        return i;
      }
      return Integer.MIN_VALUE;
    }
    
    protected void getVisibleVirtualViews(IntArray paramIntArray)
    {
      for (int i = 1; i <= mDaysInMonth; i++) {
        paramIntArray.add(i);
      }
    }
    
    protected boolean onPerformActionForVirtualView(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      if (paramInt2 != 16) {
        return false;
      }
      return SimpleMonthView.this.onDayClicked(paramInt1);
    }
    
    protected void onPopulateEventForVirtualView(int paramInt, AccessibilityEvent paramAccessibilityEvent)
    {
      paramAccessibilityEvent.setContentDescription(getDayDescription(paramInt));
    }
    
    protected void onPopulateNodeForVirtualView(int paramInt, AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      if (!getBoundsForDay(paramInt, mTempRect))
      {
        mTempRect.setEmpty();
        paramAccessibilityNodeInfo.setContentDescription("");
        paramAccessibilityNodeInfo.setBoundsInParent(mTempRect);
        paramAccessibilityNodeInfo.setVisibleToUser(false);
        return;
      }
      paramAccessibilityNodeInfo.setText(getDayText(paramInt));
      paramAccessibilityNodeInfo.setContentDescription(getDayDescription(paramInt));
      paramAccessibilityNodeInfo.setBoundsInParent(mTempRect);
      boolean bool = SimpleMonthView.this.isDayEnabled(paramInt);
      if (bool) {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
      }
      paramAccessibilityNodeInfo.setEnabled(bool);
      if (paramInt == mActivatedDay) {
        paramAccessibilityNodeInfo.setChecked(true);
      }
    }
  }
  
  public static abstract interface OnDayClickListener
  {
    public abstract void onDayClick(SimpleMonthView paramSimpleMonthView, Calendar paramCalendar);
  }
}
