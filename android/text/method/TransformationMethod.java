package android.text.method;

import android.graphics.Rect;
import android.view.View;

public abstract interface TransformationMethod
{
  public abstract CharSequence getTransformation(CharSequence paramCharSequence, View paramView);
  
  public abstract void onFocusChanged(View paramView, CharSequence paramCharSequence, boolean paramBoolean, int paramInt, Rect paramRect);
}
