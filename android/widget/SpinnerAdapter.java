package android.widget;

import android.view.View;
import android.view.ViewGroup;

public abstract interface SpinnerAdapter
  extends Adapter
{
  public abstract View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup);
}
