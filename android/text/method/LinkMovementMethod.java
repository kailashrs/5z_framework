package android.text.method;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.Layout;
import android.text.NoCopySpan.Concrete;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.textclassifier.TextLinks.TextLinkSpan;
import android.widget.TextView;

public class LinkMovementMethod
  extends ScrollingMovementMethod
{
  private static final int CLICK = 1;
  private static final int DOWN = 3;
  private static Object FROM_BELOW = new NoCopySpan.Concrete();
  private static final int HIDE_FLOATING_TOOLBAR_DELAY_MS = 200;
  private static final int UP = 2;
  private static LinkMovementMethod sInstance;
  
  public LinkMovementMethod() {}
  
  private boolean action(int paramInt, TextView paramTextView, Spannable paramSpannable)
  {
    Object localObject = paramTextView.getLayout();
    int i = paramTextView.getTotalPaddingTop() + paramTextView.getTotalPaddingBottom();
    int j = paramTextView.getScrollY();
    int k = paramTextView.getHeight() + j - i;
    int m = ((Layout)localObject).getLineForVertical(j);
    int n = ((Layout)localObject).getLineForVertical(k);
    int i1 = ((Layout)localObject).getLineStart(m);
    int i2 = ((Layout)localObject).getLineEnd(n);
    localObject = (ClickableSpan[])paramSpannable.getSpans(i1, i2, ClickableSpan.class);
    m = Selection.getSelectionStart(paramSpannable);
    n = Selection.getSelectionEnd(paramSpannable);
    int i3 = Math.min(m, n);
    int i4 = Math.max(m, n);
    m = i3;
    n = i4;
    if (i3 < 0)
    {
      m = i3;
      n = i4;
      if (paramSpannable.getSpanStart(FROM_BELOW) >= 0)
      {
        m = paramSpannable.length();
        n = m;
      }
    }
    i3 = m;
    if (m > i2)
    {
      n = Integer.MAX_VALUE;
      i3 = Integer.MAX_VALUE;
    }
    m = n;
    if (n < i1)
    {
      m = -1;
      i3 = -1;
    }
    switch (paramInt)
    {
    default: 
      break;
    case 3: 
      i4 = Integer.MAX_VALUE;
      k = Integer.MAX_VALUE;
      n = 0;
      paramInt = j;
      while (n < localObject.length)
      {
        i2 = paramSpannable.getSpanStart(localObject[n]);
        if (i2 <= i3)
        {
          i1 = i4;
          j = k;
          if (i3 != m) {}
        }
        else
        {
          i1 = i4;
          j = k;
          if (i2 < i4)
          {
            j = paramSpannable.getSpanEnd(localObject[n]);
            i1 = i2;
          }
        }
        n++;
        i4 = i1;
        k = j;
      }
      if (k < Integer.MAX_VALUE)
      {
        Selection.setSelection(paramSpannable, i4, k);
        return true;
      }
      break;
    case 2: 
      i = -1;
      j = -1;
      n = 0;
      paramInt = k;
      k = j;
      while (n < localObject.length)
      {
        j = paramSpannable.getSpanEnd(localObject[n]);
        if ((j >= m) && (i3 != m)) {
          break label438;
        }
        if (j > i)
        {
          k = paramSpannable.getSpanStart(localObject[n]);
          i = j;
        }
        n++;
      }
      if (k >= 0)
      {
        Selection.setSelection(paramSpannable, i, k);
        return true;
      }
      break;
    case 1: 
      label438:
      if (i3 == m) {
        return false;
      }
      paramSpannable = (ClickableSpan[])paramSpannable.getSpans(i3, m, ClickableSpan.class);
      if (paramSpannable.length != 1) {
        return false;
      }
      paramSpannable = paramSpannable[0];
      if ((paramSpannable instanceof TextLinks.TextLinkSpan)) {
        ((TextLinks.TextLinkSpan)paramSpannable).onClick(paramTextView, 1);
      } else {
        paramSpannable.onClick(paramTextView);
      }
      break;
    }
    return false;
  }
  
  public static MovementMethod getInstance()
  {
    if (sInstance == null) {
      sInstance = new LinkMovementMethod();
    }
    return sInstance;
  }
  
  public boolean canSelectArbitrarily()
  {
    return true;
  }
  
  protected boolean down(TextView paramTextView, Spannable paramSpannable)
  {
    if (action(3, paramTextView, paramSpannable)) {
      return true;
    }
    return super.down(paramTextView, paramSpannable);
  }
  
  protected boolean handleMovementKey(TextView paramTextView, Spannable paramSpannable, int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    if (((paramInt1 == 23) || (paramInt1 == 66)) && (KeyEvent.metaStateHasNoModifiers(paramInt2)) && (paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0) && (action(1, paramTextView, paramSpannable))) {
      return true;
    }
    return super.handleMovementKey(paramTextView, paramSpannable, paramInt1, paramInt2, paramKeyEvent);
  }
  
  public void initialize(TextView paramTextView, Spannable paramSpannable)
  {
    Selection.removeSelection(paramSpannable);
    paramSpannable.removeSpan(FROM_BELOW);
  }
  
  protected boolean left(TextView paramTextView, Spannable paramSpannable)
  {
    if (action(2, paramTextView, paramSpannable)) {
      return true;
    }
    return super.left(paramTextView, paramSpannable);
  }
  
  public void onTakeFocus(TextView paramTextView, Spannable paramSpannable, int paramInt)
  {
    Selection.removeSelection(paramSpannable);
    if ((paramInt & 0x1) != 0) {
      paramSpannable.setSpan(FROM_BELOW, 0, 0, 34);
    } else {
      paramSpannable.removeSpan(FROM_BELOW);
    }
  }
  
  public boolean onTouchEvent(TextView paramTextView, Spannable paramSpannable, MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if ((i == 1) || (i == 0))
    {
      int j = (int)paramMotionEvent.getX();
      int k = (int)paramMotionEvent.getY();
      int m = paramTextView.getTotalPaddingLeft();
      int n = paramTextView.getTotalPaddingTop();
      int i1 = paramTextView.getScrollX();
      int i2 = paramTextView.getScrollY();
      Object localObject = paramTextView.getLayout();
      m = ((Layout)localObject).getOffsetForHorizontal(((Layout)localObject).getLineForVertical(k - n + i2), j - m + i1);
      localObject = (ClickableSpan[])paramSpannable.getSpans(m, m, ClickableSpan.class);
      if (localObject.length != 0)
      {
        paramMotionEvent = localObject[0];
        if (i == 1)
        {
          if ((paramMotionEvent instanceof TextLinks.TextLinkSpan)) {
            ((TextLinks.TextLinkSpan)paramMotionEvent).onClick(paramTextView, 0);
          } else {
            paramMotionEvent.onClick(paramTextView);
          }
        }
        else if (i == 0)
        {
          if (getContextgetApplicationInfotargetSdkVersion >= 28) {
            paramTextView.hideFloatingToolbar(200);
          }
          Selection.setSelection(paramSpannable, paramSpannable.getSpanStart(paramMotionEvent), paramSpannable.getSpanEnd(paramMotionEvent));
        }
        return true;
      }
      Selection.removeSelection(paramSpannable);
    }
    return super.onTouchEvent(paramTextView, paramSpannable, paramMotionEvent);
  }
  
  protected boolean right(TextView paramTextView, Spannable paramSpannable)
  {
    if (action(3, paramTextView, paramSpannable)) {
      return true;
    }
    return super.right(paramTextView, paramSpannable);
  }
  
  protected boolean up(TextView paramTextView, Spannable paramSpannable)
  {
    if (action(2, paramTextView, paramSpannable)) {
      return true;
    }
    return super.up(paramTextView, paramSpannable);
  }
}
