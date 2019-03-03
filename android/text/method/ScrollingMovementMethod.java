package android.text.method;

import android.text.Layout;
import android.text.Spannable;
import android.view.MotionEvent;
import android.widget.TextView;

public class ScrollingMovementMethod
  extends BaseMovementMethod
  implements MovementMethod
{
  private static ScrollingMovementMethod sInstance;
  
  public ScrollingMovementMethod() {}
  
  public static MovementMethod getInstance()
  {
    if (sInstance == null) {
      sInstance = new ScrollingMovementMethod();
    }
    return sInstance;
  }
  
  protected boolean bottom(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollBottom(paramTextView, paramSpannable);
  }
  
  protected boolean down(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollDown(paramTextView, paramSpannable, 1);
  }
  
  protected boolean end(TextView paramTextView, Spannable paramSpannable)
  {
    return bottom(paramTextView, paramSpannable);
  }
  
  protected boolean home(TextView paramTextView, Spannable paramSpannable)
  {
    return top(paramTextView, paramSpannable);
  }
  
  protected boolean left(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollLeft(paramTextView, paramSpannable, 1);
  }
  
  protected boolean lineEnd(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollLineEnd(paramTextView, paramSpannable);
  }
  
  protected boolean lineStart(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollLineStart(paramTextView, paramSpannable);
  }
  
  public void onTakeFocus(TextView paramTextView, Spannable paramSpannable, int paramInt)
  {
    paramSpannable = paramTextView.getLayout();
    if ((paramSpannable != null) && ((paramInt & 0x2) != 0)) {
      paramTextView.scrollTo(paramTextView.getScrollX(), paramSpannable.getLineTop(0));
    }
    if ((paramSpannable != null) && ((paramInt & 0x1) != 0))
    {
      paramInt = paramTextView.getTotalPaddingTop();
      int i = paramTextView.getTotalPaddingBottom();
      int j = paramSpannable.getLineCount();
      paramTextView.scrollTo(paramTextView.getScrollX(), paramSpannable.getLineTop(j - 1 + 1) - (paramTextView.getHeight() - (paramInt + i)));
    }
  }
  
  public boolean onTouchEvent(TextView paramTextView, Spannable paramSpannable, MotionEvent paramMotionEvent)
  {
    return Touch.onTouchEvent(paramTextView, paramSpannable, paramMotionEvent);
  }
  
  protected boolean pageDown(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollPageDown(paramTextView, paramSpannable);
  }
  
  protected boolean pageUp(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollPageUp(paramTextView, paramSpannable);
  }
  
  protected boolean right(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollRight(paramTextView, paramSpannable, 1);
  }
  
  protected boolean top(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollTop(paramTextView, paramSpannable);
  }
  
  protected boolean up(TextView paramTextView, Spannable paramSpannable)
  {
    return scrollUp(paramTextView, paramSpannable, 1);
  }
}
