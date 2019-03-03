package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.DatePicker.ValidationCallback;
import java.util.Calendar;

public class DatePickerDialog
  extends AlertDialog
  implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener
{
  private static final String DAY = "day";
  private static final String MONTH = "month";
  private static final String YEAR = "year";
  private final DatePicker mDatePicker;
  private OnDateSetListener mDateSetListener;
  private final DatePicker.ValidationCallback mValidationCallback = new DatePicker.ValidationCallback()
  {
    public void onValidationChanged(boolean paramAnonymousBoolean)
    {
      Button localButton = getButton(-1);
      if (localButton != null) {
        localButton.setEnabled(paramAnonymousBoolean);
      }
    }
  };
  
  public DatePickerDialog(Context paramContext)
  {
    this(paramContext, 0, null, Calendar.getInstance(), -1, -1, -1);
  }
  
  public DatePickerDialog(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, null, Calendar.getInstance(), -1, -1, -1);
  }
  
  public DatePickerDialog(Context paramContext, int paramInt1, OnDateSetListener paramOnDateSetListener, int paramInt2, int paramInt3, int paramInt4)
  {
    this(paramContext, paramInt1, paramOnDateSetListener, null, paramInt2, paramInt3, paramInt4);
  }
  
  private DatePickerDialog(Context paramContext, int paramInt1, OnDateSetListener paramOnDateSetListener, Calendar paramCalendar, int paramInt2, int paramInt3, int paramInt4)
  {
    super(paramContext, resolveDialogTheme(paramContext, paramInt1));
    paramContext = getContext();
    View localView = LayoutInflater.from(paramContext).inflate(17367149, null);
    setView(localView);
    setButton(-1, paramContext.getString(17039370), this);
    setButton(-2, paramContext.getString(17039360), this);
    setButtonPanelLayoutHint(1);
    if (paramCalendar != null)
    {
      paramInt2 = paramCalendar.get(1);
      paramInt3 = paramCalendar.get(2);
      paramInt4 = paramCalendar.get(5);
    }
    mDatePicker = ((DatePicker)localView.findViewById(16908884));
    mDatePicker.init(paramInt2, paramInt3, paramInt4, this);
    mDatePicker.setValidationCallback(mValidationCallback);
    mDateSetListener = paramOnDateSetListener;
  }
  
  public DatePickerDialog(Context paramContext, OnDateSetListener paramOnDateSetListener, int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramContext, 0, paramOnDateSetListener, null, paramInt1, paramInt2, paramInt3);
  }
  
  static int resolveDialogTheme(Context paramContext, int paramInt)
  {
    if (paramInt == 0)
    {
      TypedValue localTypedValue = new TypedValue();
      paramContext.getTheme().resolveAttribute(16843948, localTypedValue, true);
      return resourceId;
    }
    return paramInt;
  }
  
  public DatePicker getDatePicker()
  {
    return mDatePicker;
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case -1: 
      if (mDateSetListener != null)
      {
        mDatePicker.clearFocus();
        mDateSetListener.onDateSet(mDatePicker, mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
      }
      break;
    case -2: 
      cancel();
    }
  }
  
  public void onDateChanged(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3)
  {
    mDatePicker.init(paramInt1, paramInt2, paramInt3, this);
  }
  
  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    int i = paramBundle.getInt("year");
    int j = paramBundle.getInt("month");
    int k = paramBundle.getInt("day");
    mDatePicker.init(i, j, k, this);
  }
  
  public Bundle onSaveInstanceState()
  {
    Bundle localBundle = super.onSaveInstanceState();
    localBundle.putInt("year", mDatePicker.getYear());
    localBundle.putInt("month", mDatePicker.getMonth());
    localBundle.putInt("day", mDatePicker.getDayOfMonth());
    return localBundle;
  }
  
  public void setOnDateSetListener(OnDateSetListener paramOnDateSetListener)
  {
    mDateSetListener = paramOnDateSetListener;
  }
  
  public void updateDate(int paramInt1, int paramInt2, int paramInt3)
  {
    mDatePicker.updateDate(paramInt1, paramInt2, paramInt3);
  }
  
  public static abstract interface OnDateSetListener
  {
    public abstract void onDateSet(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3);
  }
}
