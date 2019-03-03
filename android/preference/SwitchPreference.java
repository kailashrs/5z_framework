package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.android.internal.R.styleable;

public class SwitchPreference
  extends TwoStatePreference
{
  private final Listener mListener = new Listener(null);
  private CharSequence mSwitchOff;
  private CharSequence mSwitchOn;
  
  public SwitchPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SwitchPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843629);
  }
  
  public SwitchPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public SwitchPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SwitchPreference, paramInt1, paramInt2);
    setSummaryOn(paramContext.getString(0));
    setSummaryOff(paramContext.getString(1));
    setSwitchTextOn(paramContext.getString(3));
    setSwitchTextOff(paramContext.getString(4));
    setDisableDependentsState(paramContext.getBoolean(2, false));
    paramContext.recycle();
  }
  
  public CharSequence getSwitchTextOff()
  {
    return mSwitchOff;
  }
  
  public CharSequence getSwitchTextOn()
  {
    return mSwitchOn;
  }
  
  protected void onBindView(View paramView)
  {
    super.onBindView(paramView);
    Object localObject = paramView.findViewById(16908352);
    if ((localObject != null) && ((localObject instanceof Checkable)))
    {
      if ((localObject instanceof Switch)) {
        ((Switch)localObject).setOnCheckedChangeListener(null);
      }
      ((Checkable)localObject).setChecked(mChecked);
      if ((localObject instanceof Switch))
      {
        localObject = (Switch)localObject;
        ((Switch)localObject).setTextOn(mSwitchOn);
        ((Switch)localObject).setTextOff(mSwitchOff);
        ((Switch)localObject).setOnCheckedChangeListener(mListener);
      }
    }
    syncSummaryView(paramView);
  }
  
  public void setSwitchTextOff(int paramInt)
  {
    setSwitchTextOff(getContext().getString(paramInt));
  }
  
  public void setSwitchTextOff(CharSequence paramCharSequence)
  {
    mSwitchOff = paramCharSequence;
    notifyChanged();
  }
  
  public void setSwitchTextOn(int paramInt)
  {
    setSwitchTextOn(getContext().getString(paramInt));
  }
  
  public void setSwitchTextOn(CharSequence paramCharSequence)
  {
    mSwitchOn = paramCharSequence;
    notifyChanged();
  }
  
  private class Listener
    implements CompoundButton.OnCheckedChangeListener
  {
    private Listener() {}
    
    public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
    {
      if (!callChangeListener(Boolean.valueOf(paramBoolean)))
      {
        paramCompoundButton.setChecked(paramBoolean ^ true);
        return;
      }
      setChecked(paramBoolean);
    }
  }
}
