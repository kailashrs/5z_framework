package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

@Deprecated
public class TwoLineListItem
  extends RelativeLayout
{
  private TextView mText1;
  private TextView mText2;
  
  public TwoLineListItem(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  public TwoLineListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TwoLineListItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TwoLineListItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TwoLineListItem, paramInt1, paramInt2).recycle();
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return TwoLineListItem.class.getName();
  }
  
  public TextView getText1()
  {
    return mText1;
  }
  
  public TextView getText2()
  {
    return mText2;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mText1 = ((TextView)findViewById(16908308));
    mText2 = ((TextView)findViewById(16908309));
  }
}
