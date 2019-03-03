package android.text.method;

import android.text.Layout;
import android.text.Spannable;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

public class BaseMovementMethod
  implements MovementMethod
{
  public BaseMovementMethod() {}
  
  private int getBottomLine(TextView paramTextView)
  {
    return paramTextView.getLayout().getLineForVertical(paramTextView.getScrollY() + getInnerHeight(paramTextView));
  }
  
  private int getCharacterWidth(TextView paramTextView)
  {
    return (int)Math.ceil(paramTextView.getPaint().getFontSpacing());
  }
  
  private int getInnerHeight(TextView paramTextView)
  {
    return paramTextView.getHeight() - paramTextView.getTotalPaddingTop() - paramTextView.getTotalPaddingBottom();
  }
  
  private int getInnerWidth(TextView paramTextView)
  {
    return paramTextView.getWidth() - paramTextView.getTotalPaddingLeft() - paramTextView.getTotalPaddingRight();
  }
  
  private int getScrollBoundsLeft(TextView paramTextView)
  {
    Layout localLayout = paramTextView.getLayout();
    int i = getTopLine(paramTextView);
    int j = getBottomLine(paramTextView);
    if (i > j) {
      return 0;
    }
    int n;
    for (int k = Integer.MAX_VALUE; i <= j; k = n)
    {
      int m = (int)Math.floor(localLayout.getLineLeft(i));
      n = k;
      if (m < k) {
        n = m;
      }
      i++;
    }
    return k;
  }
  
  private int getScrollBoundsRight(TextView paramTextView)
  {
    Layout localLayout = paramTextView.getLayout();
    int i = getTopLine(paramTextView);
    int j = getBottomLine(paramTextView);
    if (i > j) {
      return 0;
    }
    int n;
    for (int k = Integer.MIN_VALUE; i <= j; k = n)
    {
      int m = (int)Math.ceil(localLayout.getLineRight(i));
      n = k;
      if (m > k) {
        n = m;
      }
      i++;
    }
    return k;
  }
  
  private int getTopLine(TextView paramTextView)
  {
    return paramTextView.getLayout().getLineForVertical(paramTextView.getScrollY());
  }
  
  protected boolean bottom(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  public boolean canSelectArbitrarily()
  {
    return false;
  }
  
  protected boolean down(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean end(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected int getMovementMetaState(Spannable paramSpannable, KeyEvent paramKeyEvent)
  {
    return KeyEvent.normalizeMetaState(MetaKeyKeyListener.getMetaState(paramSpannable, paramKeyEvent) & 0xF9FF) & 0xFF3E;
  }
  
  protected boolean handleMovementKey(TextView paramTextView, Spannable paramSpannable, int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    switch (paramInt1)
    {
    default: 
      break;
    case 123: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return end(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 4096)) {
        return bottom(paramTextView, paramSpannable);
      }
      break;
    case 122: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return home(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 4096)) {
        return top(paramTextView, paramSpannable);
      }
      break;
    case 93: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return pageDown(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 2)) {
        return bottom(paramTextView, paramSpannable);
      }
      break;
    case 92: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return pageUp(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 2)) {
        return top(paramTextView, paramSpannable);
      }
      break;
    case 22: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return right(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 4096)) {
        return rightWord(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 2)) {
        return lineEnd(paramTextView, paramSpannable);
      }
      break;
    case 21: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return left(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 4096)) {
        return leftWord(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 2)) {
        return lineStart(paramTextView, paramSpannable);
      }
      break;
    case 20: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return down(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 2)) {
        return bottom(paramTextView, paramSpannable);
      }
      break;
    case 19: 
      if (KeyEvent.metaStateHasNoModifiers(paramInt2)) {
        return up(paramTextView, paramSpannable);
      }
      if (KeyEvent.metaStateHasModifiers(paramInt2, 2)) {
        return top(paramTextView, paramSpannable);
      }
      break;
    }
    return false;
  }
  
  protected boolean home(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  public void initialize(TextView paramTextView, Spannable paramSpannable) {}
  
  protected boolean left(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean leftWord(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean lineEnd(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean lineStart(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  public boolean onGenericMotionEvent(TextView paramTextView, Spannable paramSpannable, MotionEvent paramMotionEvent)
  {
    if (((paramMotionEvent.getSource() & 0x2) != 0) && (paramMotionEvent.getAction() == 8))
    {
      float f1;
      float f2;
      if ((paramMotionEvent.getMetaState() & 0x1) != 0)
      {
        f1 = 0.0F;
        f2 = paramMotionEvent.getAxisValue(9);
      }
      else
      {
        f1 = -paramMotionEvent.getAxisValue(9);
        f2 = paramMotionEvent.getAxisValue(10);
      }
      boolean bool1 = false;
      if (f2 < 0.0F) {
        bool1 = false | scrollLeft(paramTextView, paramSpannable, (int)Math.ceil(-f2));
      } else if (f2 > 0.0F) {
        bool1 = false | scrollRight(paramTextView, paramSpannable, (int)Math.ceil(f2));
      }
      boolean bool2;
      if (f1 < 0.0F)
      {
        bool2 = bool1 | scrollUp(paramTextView, paramSpannable, (int)Math.ceil(-f1));
      }
      else
      {
        bool2 = bool1;
        if (f1 > 0.0F) {
          bool2 = bool1 | scrollDown(paramTextView, paramSpannable, (int)Math.ceil(f1));
        }
      }
      return bool2;
    }
    return false;
  }
  
  public boolean onKeyDown(TextView paramTextView, Spannable paramSpannable, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = handleMovementKey(paramTextView, paramSpannable, paramInt, getMovementMetaState(paramSpannable, paramKeyEvent), paramKeyEvent);
    if (bool)
    {
      MetaKeyKeyListener.adjustMetaAfterKeypress(paramSpannable);
      MetaKeyKeyListener.resetLockedMeta(paramSpannable);
    }
    return bool;
  }
  
  public boolean onKeyOther(TextView paramTextView, Spannable paramSpannable, KeyEvent paramKeyEvent)
  {
    int i = getMovementMetaState(paramSpannable, paramKeyEvent);
    int j = paramKeyEvent.getKeyCode();
    int k = 0;
    if ((j != 0) && (paramKeyEvent.getAction() == 2))
    {
      int m = paramKeyEvent.getRepeatCount();
      boolean bool = false;
      while ((k < m) && (handleMovementKey(paramTextView, paramSpannable, j, i, paramKeyEvent)))
      {
        bool = true;
        k++;
      }
      if (bool)
      {
        MetaKeyKeyListener.adjustMetaAfterKeypress(paramSpannable);
        MetaKeyKeyListener.resetLockedMeta(paramSpannable);
      }
      return bool;
    }
    return false;
  }
  
  public boolean onKeyUp(TextView paramTextView, Spannable paramSpannable, int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public void onTakeFocus(TextView paramTextView, Spannable paramSpannable, int paramInt) {}
  
  public boolean onTouchEvent(TextView paramTextView, Spannable paramSpannable, MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public boolean onTrackballEvent(TextView paramTextView, Spannable paramSpannable, MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  protected boolean pageDown(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean pageUp(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean right(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean rightWord(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean scrollBottom(TextView paramTextView, Spannable paramSpannable)
  {
    paramSpannable = paramTextView.getLayout();
    int i = paramSpannable.getLineCount();
    if (getBottomLine(paramTextView) <= i - 1)
    {
      Touch.scrollTo(paramTextView, paramSpannable, paramTextView.getScrollX(), paramSpannable.getLineTop(i) - getInnerHeight(paramTextView));
      return true;
    }
    return false;
  }
  
  protected boolean scrollDown(TextView paramTextView, Spannable paramSpannable, int paramInt)
  {
    paramSpannable = paramTextView.getLayout();
    int i = getInnerHeight(paramTextView);
    int j = paramTextView.getScrollY() + i;
    int k = paramSpannable.getLineForVertical(j);
    int m = k;
    if (paramSpannable.getLineTop(k + 1) < j + 1) {
      m = k + 1;
    }
    k = paramSpannable.getLineCount() - 1;
    if (m <= k)
    {
      paramInt = Math.min(m + paramInt - 1, k);
      Touch.scrollTo(paramTextView, paramSpannable, paramTextView.getScrollX(), paramSpannable.getLineTop(paramInt + 1) - i);
      return true;
    }
    return false;
  }
  
  protected boolean scrollLeft(TextView paramTextView, Spannable paramSpannable, int paramInt)
  {
    int i = getScrollBoundsLeft(paramTextView);
    int j = paramTextView.getScrollX();
    if (j > i)
    {
      paramTextView.scrollTo(Math.max(j - getCharacterWidth(paramTextView) * paramInt, i), paramTextView.getScrollY());
      return true;
    }
    return false;
  }
  
  protected boolean scrollLineEnd(TextView paramTextView, Spannable paramSpannable)
  {
    int i = getScrollBoundsRight(paramTextView) - getInnerWidth(paramTextView);
    if (paramTextView.getScrollX() < i)
    {
      paramTextView.scrollTo(i, paramTextView.getScrollY());
      return true;
    }
    return false;
  }
  
  protected boolean scrollLineStart(TextView paramTextView, Spannable paramSpannable)
  {
    int i = getScrollBoundsLeft(paramTextView);
    if (paramTextView.getScrollX() > i)
    {
      paramTextView.scrollTo(i, paramTextView.getScrollY());
      return true;
    }
    return false;
  }
  
  protected boolean scrollPageDown(TextView paramTextView, Spannable paramSpannable)
  {
    paramSpannable = paramTextView.getLayout();
    int i = getInnerHeight(paramTextView);
    int j = paramSpannable.getLineForVertical(paramTextView.getScrollY() + i + i);
    if (j <= paramSpannable.getLineCount() - 1)
    {
      Touch.scrollTo(paramTextView, paramSpannable, paramTextView.getScrollX(), paramSpannable.getLineTop(j + 1) - i);
      return true;
    }
    return false;
  }
  
  protected boolean scrollPageUp(TextView paramTextView, Spannable paramSpannable)
  {
    paramSpannable = paramTextView.getLayout();
    int i = paramSpannable.getLineForVertical(paramTextView.getScrollY() - getInnerHeight(paramTextView));
    if (i >= 0)
    {
      Touch.scrollTo(paramTextView, paramSpannable, paramTextView.getScrollX(), paramSpannable.getLineTop(i));
      return true;
    }
    return false;
  }
  
  protected boolean scrollRight(TextView paramTextView, Spannable paramSpannable, int paramInt)
  {
    int i = getScrollBoundsRight(paramTextView) - getInnerWidth(paramTextView);
    int j = paramTextView.getScrollX();
    if (j < i)
    {
      paramTextView.scrollTo(Math.min(getCharacterWidth(paramTextView) * paramInt + j, i), paramTextView.getScrollY());
      return true;
    }
    return false;
  }
  
  protected boolean scrollTop(TextView paramTextView, Spannable paramSpannable)
  {
    paramSpannable = paramTextView.getLayout();
    if (getTopLine(paramTextView) >= 0)
    {
      Touch.scrollTo(paramTextView, paramSpannable, paramTextView.getScrollX(), paramSpannable.getLineTop(0));
      return true;
    }
    return false;
  }
  
  protected boolean scrollUp(TextView paramTextView, Spannable paramSpannable, int paramInt)
  {
    paramSpannable = paramTextView.getLayout();
    int i = paramTextView.getScrollY();
    int j = paramSpannable.getLineForVertical(i);
    int k = j;
    if (paramSpannable.getLineTop(j) == i) {
      k = j - 1;
    }
    if (k >= 0)
    {
      paramInt = Math.max(k - paramInt + 1, 0);
      Touch.scrollTo(paramTextView, paramSpannable, paramTextView.getScrollX(), paramSpannable.getLineTop(paramInt));
      return true;
    }
    return false;
  }
  
  protected boolean top(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
  
  protected boolean up(TextView paramTextView, Spannable paramSpannable)
  {
    return false;
  }
}
