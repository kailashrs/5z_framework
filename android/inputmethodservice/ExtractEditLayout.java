package android.inputmethodservice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class ExtractEditLayout
  extends LinearLayout
{
  Button mExtractActionButton;
  
  public ExtractEditLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public ExtractEditLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    mExtractActionButton = ((Button)findViewById(16909040));
  }
}
