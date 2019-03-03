package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerDialog
  extends AlertDialog
  implements DialogInterface.OnClickListener, TimePicker.OnTimeChangedListener
{
  private static final String HOUR = "hour";
  private static final String IS_24_HOUR = "is24hour";
  private static final String MINUTE = "minute";
  private final int mInitialHourOfDay;
  private final int mInitialMinute;
  private final boolean mIs24HourView;
  private final TimePicker mTimePicker;
  private final OnTimeSetListener mTimeSetListener;
  
  public TimePickerDialog(Context paramContext, int paramInt1, OnTimeSetListener paramOnTimeSetListener, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    super(paramContext, resolveDialogTheme(paramContext, paramInt1));
    mTimeSetListener = paramOnTimeSetListener;
    mInitialHourOfDay = paramInt2;
    mInitialMinute = paramInt3;
    mIs24HourView = paramBoolean;
    paramContext = getContext();
    paramOnTimeSetListener = LayoutInflater.from(paramContext).inflate(17367341, null);
    setView(paramOnTimeSetListener);
    setButton(-1, paramContext.getString(17039370), this);
    setButton(-2, paramContext.getString(17039360), this);
    setButtonPanelLayoutHint(1);
    mTimePicker = ((TimePicker)paramOnTimeSetListener.findViewById(16909469));
    mTimePicker.setIs24HourView(Boolean.valueOf(mIs24HourView));
    mTimePicker.setCurrentHour(Integer.valueOf(mInitialHourOfDay));
    mTimePicker.setCurrentMinute(Integer.valueOf(mInitialMinute));
    mTimePicker.setOnTimeChangedListener(this);
  }
  
  public TimePickerDialog(Context paramContext, OnTimeSetListener paramOnTimeSetListener, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this(paramContext, 0, paramOnTimeSetListener, paramInt1, paramInt2, paramBoolean);
  }
  
  static int resolveDialogTheme(Context paramContext, int paramInt)
  {
    if (paramInt == 0)
    {
      TypedValue localTypedValue = new TypedValue();
      paramContext.getTheme().resolveAttribute(16843934, localTypedValue, true);
      return resourceId;
    }
    return paramInt;
  }
  
  public TimePicker getTimePicker()
  {
    return mTimePicker;
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case -1: 
      if (mTimeSetListener != null) {
        mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour().intValue(), mTimePicker.getCurrentMinute().intValue());
      }
      break;
    case -2: 
      cancel();
    }
  }
  
  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    int i = paramBundle.getInt("hour");
    int j = paramBundle.getInt("minute");
    mTimePicker.setIs24HourView(Boolean.valueOf(paramBundle.getBoolean("is24hour")));
    mTimePicker.setCurrentHour(Integer.valueOf(i));
    mTimePicker.setCurrentMinute(Integer.valueOf(j));
  }
  
  public Bundle onSaveInstanceState()
  {
    Bundle localBundle = super.onSaveInstanceState();
    localBundle.putInt("hour", mTimePicker.getCurrentHour().intValue());
    localBundle.putInt("minute", mTimePicker.getCurrentMinute().intValue());
    localBundle.putBoolean("is24hour", mTimePicker.is24HourView());
    return localBundle;
  }
  
  public void onTimeChanged(TimePicker paramTimePicker, int paramInt1, int paramInt2) {}
  
  public void show()
  {
    super.show();
    getButton(-1).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (mTimePicker.validateInput())
        {
          onClick(TimePickerDialog.this, -1);
          mTimePicker.clearFocus();
          dismiss();
        }
      }
    });
  }
  
  public void updateTime(int paramInt1, int paramInt2)
  {
    mTimePicker.setCurrentHour(Integer.valueOf(paramInt1));
    mTimePicker.setCurrentMinute(Integer.valueOf(paramInt2));
  }
  
  public static abstract interface OnTimeSetListener
  {
    public abstract void onTimeSet(TimePicker paramTimePicker, int paramInt1, int paramInt2);
  }
}
