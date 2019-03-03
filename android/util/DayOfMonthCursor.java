package android.util;

public class DayOfMonthCursor
  extends MonthDisplayHelper
{
  private int mColumn;
  private int mRow;
  
  public DayOfMonthCursor(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super(paramInt1, paramInt2, paramInt4);
    mRow = getRowOf(paramInt3);
    mColumn = getColumnOf(paramInt3);
  }
  
  public boolean down()
  {
    if (isWithinCurrentMonth(mRow + 1, mColumn))
    {
      mRow += 1;
      return false;
    }
    nextMonth();
    for (mRow = 0; !isWithinCurrentMonth(mRow, mColumn); mRow += 1) {}
    return true;
  }
  
  public int getSelectedColumn()
  {
    return mColumn;
  }
  
  public int getSelectedDayOfMonth()
  {
    return getDayAt(mRow, mColumn);
  }
  
  public int getSelectedMonthOffset()
  {
    if (isWithinCurrentMonth(mRow, mColumn)) {
      return 0;
    }
    if (mRow == 0) {
      return -1;
    }
    return 1;
  }
  
  public int getSelectedRow()
  {
    return mRow;
  }
  
  public boolean isSelected(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((mRow == paramInt1) && (mColumn == paramInt2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean left()
  {
    if (mColumn == 0)
    {
      mRow -= 1;
      mColumn = 6;
    }
    else
    {
      mColumn -= 1;
    }
    if (isWithinCurrentMonth(mRow, mColumn)) {
      return false;
    }
    previousMonth();
    int i = getNumberOfDaysInMonth();
    mRow = getRowOf(i);
    mColumn = getColumnOf(i);
    return true;
  }
  
  public boolean right()
  {
    if (mColumn == 6)
    {
      mRow += 1;
      mColumn = 0;
    }
    else
    {
      mColumn += 1;
    }
    if (isWithinCurrentMonth(mRow, mColumn)) {
      return false;
    }
    nextMonth();
    mRow = 0;
    for (mColumn = 0; !isWithinCurrentMonth(mRow, mColumn); mColumn += 1) {}
    return true;
  }
  
  public void setSelectedDayOfMonth(int paramInt)
  {
    mRow = getRowOf(paramInt);
    mColumn = getColumnOf(paramInt);
  }
  
  public void setSelectedRowColumn(int paramInt1, int paramInt2)
  {
    mRow = paramInt1;
    mColumn = paramInt2;
  }
  
  public boolean up()
  {
    if (isWithinCurrentMonth(mRow - 1, mColumn))
    {
      mRow -= 1;
      return false;
    }
    previousMonth();
    for (mRow = 5; !isWithinCurrentMonth(mRow, mColumn); mRow -= 1) {}
    return true;
  }
}
