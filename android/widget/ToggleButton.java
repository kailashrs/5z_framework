package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class ToggleButton
  extends CompoundButton
{
  private static final int NO_ALPHA = 255;
  private float mDisabledAlpha;
  private Drawable mIndicatorDrawable;
  private CharSequence mTextOff;
  private CharSequence mTextOn;
  
  public ToggleButton(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ToggleButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842827);
  }
  
  public ToggleButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ToggleButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ToggleButton, paramInt1, paramInt2);
    mTextOn = paramContext.getText(1);
    mTextOff = paramContext.getText(2);
    mDisabledAlpha = paramContext.getFloat(0, 0.5F);
    syncTextState();
    paramContext.recycle();
  }
  
  private void syncTextState()
  {
    boolean bool = isChecked();
    if ((bool) && (mTextOn != null)) {
      setText(mTextOn);
    } else if ((!bool) && (mTextOff != null)) {
      setText(mTextOff);
    }
  }
  
  private void updateReferenceToIndicatorDrawable(Drawable paramDrawable)
  {
    if ((paramDrawable instanceof LayerDrawable)) {
      mIndicatorDrawable = ((LayerDrawable)paramDrawable).findDrawableByLayerId(16908311);
    } else {
      mIndicatorDrawable = null;
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if (mIndicatorDrawable != null)
    {
      Drawable localDrawable = mIndicatorDrawable;
      int i;
      if (isEnabled()) {
        i = 255;
      } else {
        i = (int)(255.0F * mDisabledAlpha);
      }
      localDrawable.setAlpha(i);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ToggleButton.class.getName();
  }
  
  public CharSequence getTextOff()
  {
    return mTextOff;
  }
  
  public CharSequence getTextOn()
  {
    return mTextOn;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    updateReferenceToIndicatorDrawable(getBackground());
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    super.setBackgroundDrawable(paramDrawable);
    updateReferenceToIndicatorDrawable(paramDrawable);
  }
  
  public void setChecked(boolean paramBoolean)
  {
    super.setChecked(paramBoolean);
    syncTextState();
  }
  
  public void setTextOff(CharSequence paramCharSequence)
  {
    mTextOff = paramCharSequence;
  }
  
  public void setTextOn(CharSequence paramCharSequence)
  {
    mTextOn = paramCharSequence;
  }
}
