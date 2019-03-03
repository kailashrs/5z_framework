package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewStructure;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import com.android.internal.R.styleable;

public class RadioGroup
  extends LinearLayout
{
  private static final String LOG_TAG = RadioGroup.class.getSimpleName();
  private int mCheckedId = -1;
  private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
  private int mInitialCheckedId = -1;
  private OnCheckedChangeListener mOnCheckedChangeListener;
  private PassThroughHierarchyChangeListener mPassThroughListener;
  private boolean mProtectFromCheckedChange = false;
  
  public RadioGroup(Context paramContext)
  {
    super(paramContext);
    setOrientation(1);
    init();
  }
  
  public RadioGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (getImportantForAutofill() == 0) {
      setImportantForAutofill(1);
    }
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RadioGroup, 16842878, 0);
    int i = paramContext.getResourceId(1, -1);
    if (i != -1)
    {
      mCheckedId = i;
      mInitialCheckedId = i;
    }
    setOrientation(paramContext.getInt(0, 1));
    paramContext.recycle();
    init();
  }
  
  private void init()
  {
    mChildOnCheckedChangeListener = new CheckedStateTracker(null);
    mPassThroughListener = new PassThroughHierarchyChangeListener(null);
    super.setOnHierarchyChangeListener(mPassThroughListener);
  }
  
  private void setCheckedId(int paramInt)
  {
    int i;
    if (paramInt != mCheckedId) {
      i = 1;
    } else {
      i = 0;
    }
    mCheckedId = paramInt;
    if (mOnCheckedChangeListener != null) {
      mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
    }
    if (i != 0)
    {
      AutofillManager localAutofillManager = (AutofillManager)mContext.getSystemService(AutofillManager.class);
      if (localAutofillManager != null) {
        localAutofillManager.notifyValueChanged(this);
      }
    }
  }
  
  private void setCheckedStateForView(int paramInt, boolean paramBoolean)
  {
    View localView = findViewById(paramInt);
    if ((localView != null) && ((localView instanceof RadioButton))) {
      ((RadioButton)localView).setChecked(paramBoolean);
    }
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramView instanceof RadioButton))
    {
      RadioButton localRadioButton = (RadioButton)paramView;
      if (localRadioButton.isChecked())
      {
        mProtectFromCheckedChange = true;
        if (mCheckedId != -1) {
          setCheckedStateForView(mCheckedId, false);
        }
        mProtectFromCheckedChange = false;
        setCheckedId(localRadioButton.getId());
      }
    }
    super.addView(paramView, paramInt, paramLayoutParams);
  }
  
  public void autofill(AutofillValue paramAutofillValue)
  {
    if (!isEnabled()) {
      return;
    }
    if (!paramAutofillValue.isList())
    {
      String str = LOG_TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramAutofillValue);
      localStringBuilder.append(" could not be autofilled into ");
      localStringBuilder.append(this);
      Log.w(str, localStringBuilder.toString());
      return;
    }
    int i = paramAutofillValue.getListValue();
    paramAutofillValue = getChildAt(i);
    if (paramAutofillValue == null)
    {
      paramAutofillValue = new StringBuilder();
      paramAutofillValue.append("RadioGroup.autoFill(): no child with index ");
      paramAutofillValue.append(i);
      Log.w("View", paramAutofillValue.toString());
      return;
    }
    check(paramAutofillValue.getId());
  }
  
  public void check(int paramInt)
  {
    if ((paramInt != -1) && (paramInt == mCheckedId)) {
      return;
    }
    if (mCheckedId != -1) {
      setCheckedStateForView(mCheckedId, false);
    }
    if (paramInt != -1) {
      setCheckedStateForView(paramInt, true);
    }
    setCheckedId(paramInt);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void clearCheck()
  {
    check(-1);
  }
  
  protected LinearLayout.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return RadioGroup.class.getName();
  }
  
  public int getAutofillType()
  {
    int i;
    if (isEnabled()) {
      i = 3;
    } else {
      i = 0;
    }
    return i;
  }
  
  public AutofillValue getAutofillValue()
  {
    if (!isEnabled()) {
      return null;
    }
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      if (getChildAt(j).getId() == mCheckedId) {
        return AutofillValue.forList(j);
      }
    }
    return null;
  }
  
  public int getCheckedRadioButtonId()
  {
    return mCheckedId;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    if (mCheckedId != -1)
    {
      mProtectFromCheckedChange = true;
      setCheckedStateForView(mCheckedId, true);
      mProtectFromCheckedChange = false;
      setCheckedId(mCheckedId);
    }
  }
  
  public void onProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    super.onProvideAutofillStructure(paramViewStructure, paramInt);
    boolean bool;
    if (mCheckedId != mInitialCheckedId) {
      bool = true;
    } else {
      bool = false;
    }
    paramViewStructure.setDataIsSensitive(bool);
  }
  
  public void setOnCheckedChangeListener(OnCheckedChangeListener paramOnCheckedChangeListener)
  {
    mOnCheckedChangeListener = paramOnCheckedChangeListener;
  }
  
  public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener)
  {
    PassThroughHierarchyChangeListener.access$202(mPassThroughListener, paramOnHierarchyChangeListener);
  }
  
  private class CheckedStateTracker
    implements CompoundButton.OnCheckedChangeListener
  {
    private CheckedStateTracker() {}
    
    public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
    {
      if (mProtectFromCheckedChange) {
        return;
      }
      RadioGroup.access$302(RadioGroup.this, true);
      if (mCheckedId != -1) {
        RadioGroup.this.setCheckedStateForView(mCheckedId, false);
      }
      RadioGroup.access$302(RadioGroup.this, false);
      int i = paramCompoundButton.getId();
      RadioGroup.this.setCheckedId(i);
    }
  }
  
  public static class LayoutParams
    extends LinearLayout.LayoutParams
  {
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(int paramInt1, int paramInt2, float paramFloat)
    {
      super(paramInt2, paramFloat);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
    
    protected void setBaseAttributes(TypedArray paramTypedArray, int paramInt1, int paramInt2)
    {
      if (paramTypedArray.hasValue(paramInt1)) {
        width = paramTypedArray.getLayoutDimension(paramInt1, "layout_width");
      } else {
        width = -2;
      }
      if (paramTypedArray.hasValue(paramInt2)) {
        height = paramTypedArray.getLayoutDimension(paramInt2, "layout_height");
      } else {
        height = -2;
      }
    }
  }
  
  public static abstract interface OnCheckedChangeListener
  {
    public abstract void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt);
  }
  
  private class PassThroughHierarchyChangeListener
    implements ViewGroup.OnHierarchyChangeListener
  {
    private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    
    private PassThroughHierarchyChangeListener() {}
    
    public void onChildViewAdded(View paramView1, View paramView2)
    {
      if ((paramView1 == RadioGroup.this) && ((paramView2 instanceof RadioButton)))
      {
        if (paramView2.getId() == -1) {
          paramView2.setId(View.generateViewId());
        }
        ((RadioButton)paramView2).setOnCheckedChangeWidgetListener(mChildOnCheckedChangeListener);
      }
      if (mOnHierarchyChangeListener != null) {
        mOnHierarchyChangeListener.onChildViewAdded(paramView1, paramView2);
      }
    }
    
    public void onChildViewRemoved(View paramView1, View paramView2)
    {
      if ((paramView1 == RadioGroup.this) && ((paramView2 instanceof RadioButton))) {
        ((RadioButton)paramView2).setOnCheckedChangeWidgetListener(null);
      }
      if (mOnHierarchyChangeListener != null) {
        mOnHierarchyChangeListener.onChildViewRemoved(paramView1, paramView2);
      }
    }
  }
}
