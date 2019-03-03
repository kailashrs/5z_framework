package android.text.method;

import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.NoCopySpan;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class Touch
{
  private Touch() {}
  
  public static int getInitialScrollX(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = (DragState[])paramSpannable.getSpans(0, paramSpannable.length(), DragState.class);
    int i;
    if (paramTextView.length > 0) {
      i = 0mScrollX;
    } else {
      i = -1;
    }
    return i;
  }
  
  public static int getInitialScrollY(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = (DragState[])paramSpannable.getSpans(0, paramSpannable.length(), DragState.class);
    int i;
    if (paramTextView.length > 0) {
      i = 0mScrollY;
    } else {
      i = -1;
    }
    return i;
  }
  
  public static boolean onTouchEvent(TextView paramTextView, Spannable paramSpannable, MotionEvent paramMotionEvent)
  {
    DragState[] arrayOfDragState;
    int i;
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 2: 
      arrayOfDragState = (DragState[])paramSpannable.getSpans(0, paramSpannable.length(), DragState.class);
      if (arrayOfDragState.length > 0)
      {
        if (!0mFarEnough)
        {
          i = ViewConfiguration.get(paramTextView.getContext()).getScaledTouchSlop();
          if ((Math.abs(paramMotionEvent.getX() - 0mX) >= i) || (Math.abs(paramMotionEvent.getY() - 0mY) >= i)) {
            0mFarEnough = true;
          }
        }
        if (0mFarEnough)
        {
          0mUsed = true;
          if (((paramMotionEvent.getMetaState() & 0x1) == 0) && (MetaKeyKeyListener.getMetaState(paramSpannable, 1) != 1) && (MetaKeyKeyListener.getMetaState(paramSpannable, 2048) == 0)) {
            i = 0;
          } else {
            i = 1;
          }
          float f1;
          float f2;
          if (i != 0)
          {
            f1 = paramMotionEvent.getX() - 0mX;
            f2 = paramMotionEvent.getY() - 0mY;
          }
          else
          {
            f1 = 0mX - paramMotionEvent.getX();
            f2 = 0mY - paramMotionEvent.getY();
          }
          0mX = paramMotionEvent.getX();
          0mY = paramMotionEvent.getY();
          int j = paramTextView.getScrollX();
          i = (int)f1;
          int k = paramTextView.getScrollY();
          int m = (int)f2;
          int n = paramTextView.getTotalPaddingTop();
          int i1 = paramTextView.getTotalPaddingBottom();
          paramSpannable = paramTextView.getLayout();
          k = Math.max(Math.min(k + m, paramSpannable.getHeight() - (paramTextView.getHeight() - (n + i1))), 0);
          m = paramTextView.getScrollX();
          i1 = paramTextView.getScrollY();
          scrollTo(paramTextView, paramSpannable, j + i, k);
          if ((m != paramTextView.getScrollX()) || (i1 != paramTextView.getScrollY())) {
            paramTextView.cancelLongPress();
          }
          return true;
        }
      }
      break;
    case 1: 
      paramTextView = (DragState[])paramSpannable.getSpans(0, paramSpannable.length(), DragState.class);
      for (i = 0; i < paramTextView.length; i++) {
        paramSpannable.removeSpan(paramTextView[i]);
      }
      return (paramTextView.length > 0) && (0mUsed);
    case 0: 
      arrayOfDragState = (DragState[])paramSpannable.getSpans(0, paramSpannable.length(), DragState.class);
      for (i = 0; i < arrayOfDragState.length; i++) {
        paramSpannable.removeSpan(arrayOfDragState[i]);
      }
      paramSpannable.setSpan(new DragState(paramMotionEvent.getX(), paramMotionEvent.getY(), paramTextView.getScrollX(), paramTextView.getScrollY()), 0, 0, 17);
      return true;
    }
    return false;
  }
  
  public static void scrollTo(TextView paramTextView, Layout paramLayout, int paramInt1, int paramInt2)
  {
    int i = paramTextView.getTotalPaddingLeft();
    int j = paramTextView.getTotalPaddingRight();
    int k = paramTextView.getWidth() - (i + j);
    int m = paramLayout.getLineForVertical(paramInt2);
    Layout.Alignment localAlignment = paramLayout.getParagraphAlignment(m);
    int n;
    if (paramLayout.getParagraphDirection(m) > 0) {
      n = 1;
    } else {
      n = 0;
    }
    if (paramTextView.getHorizontallyScrolling())
    {
      j = paramTextView.getTotalPaddingTop();
      i = paramTextView.getTotalPaddingBottom();
      int i1 = paramLayout.getLineForVertical(paramTextView.getHeight() + paramInt2 - (j + i));
      i = 0;
      j = Integer.MAX_VALUE;
      while (m <= i1)
      {
        j = (int)Math.min(j, paramLayout.getLineLeft(m));
        i = (int)Math.max(i, paramLayout.getLineRight(m));
        m++;
      }
    }
    else
    {
      j = 0;
      i = k;
    }
    m = i - j;
    if (m < k)
    {
      if (localAlignment == Layout.Alignment.ALIGN_CENTER) {
        paramInt1 = j - (k - m) / 2;
      } else if (((n != 0) && (localAlignment == Layout.Alignment.ALIGN_OPPOSITE)) || ((n == 0) && (localAlignment == Layout.Alignment.ALIGN_NORMAL)) || (localAlignment == Layout.Alignment.ALIGN_RIGHT)) {
        paramInt1 = j - (k - m);
      } else {
        paramInt1 = j;
      }
    }
    else {
      paramInt1 = Math.max(Math.min(paramInt1, i - k), j);
    }
    paramTextView.scrollTo(paramInt1, paramInt2);
  }
  
  private static class DragState
    implements NoCopySpan
  {
    public boolean mFarEnough;
    public int mScrollX;
    public int mScrollY;
    public boolean mUsed;
    public float mX;
    public float mY;
    
    public DragState(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
    {
      mX = paramFloat1;
      mY = paramFloat2;
      mScrollX = paramInt1;
      mScrollY = paramInt2;
    }
  }
}
