package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import com.android.internal.R.styleable;

public class CheckBoxPreference
  extends TwoStatePreference
{
  public CheckBoxPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CheckBoxPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842895);
  }
  
  public CheckBoxPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public CheckBoxPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CheckBoxPreference, paramInt1, paramInt2);
    setSummaryOn(paramContext.getString(0));
    setSummaryOff(paramContext.getString(1));
    setDisableDependentsState(paramContext.getBoolean(2, false));
    paramContext.recycle();
  }
  
  protected void onBindView(View paramView)
  {
    super.onBindView(paramView);
    View localView = paramView.findViewById(16908289);
    if ((localView != null) && ((localView instanceof Checkable))) {
      ((Checkable)localView).setChecked(mChecked);
    }
    syncSummaryView(paramView);
  }
}
