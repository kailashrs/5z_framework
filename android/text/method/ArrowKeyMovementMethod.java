package android.text.method;

import android.graphics.Rect;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TextView;

public class ArrowKeyMovementMethod
  extends BaseMovementMethod
  implements MovementMethod
{
  private static final Object LAST_TAP_DOWN = new Object();
  private static ArrowKeyMovementMethod sInstance;
  
  public ArrowKeyMovementMethod() {}
  
  private static int getCurrentLineTop(Spannable paramSpannable, Layout paramLayout)
  {
    return paramLayout.getLineTop(paramLayout.getLineForOffset(Selection.getSelectionEnd(paramSpannable)));
  }
  
  public static MovementMethod getInstance()
  {
    if (sInstance == null) {
      sInstance = new ArrowKeyMovementMethod();
    }
    return sInstance;
  }
  
  private static int getPageHeight(TextView paramTextView)
  {
    Rect localRect = new Rect();
    int i;
    if (paramTextView.getGlobalVisibleRect(localRect)) {
      i = localRect.height();
    } else {
      i = 0;
    }
    return i;
  }
  
  private static boolean isSelecting(Spannable paramSpannable)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (MetaKeyKeyListener.getMetaState(paramSpannable, 1) != 1) {
      if (MetaKeyKeyListener.getMetaState(paramSpannable, 2048) != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  protected boolean bottom(TextView paramTextView, Spannable paramSpannable)
  {
    if (isSelecting(paramSpannable)) {
      Selection.extendSelection(paramSpannable, paramSpannable.length());
    } else {
      Selection.setSelection(paramSpannable, paramSpannable.length());
    }
    return true;
  }
  
  public boolean canSelectArbitrarily()
  {
    return true;
  }
  
  protected boolean down(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = paramTextView.getLayout();
    if (isSelecting(paramSpannable)) {
      return Selection.extendDown(paramSpannable, paramTextView);
    }
    return Selection.moveDown(paramSpannable, paramTextView);
  }
  
  protected boolean end(TextView paramTextView, Spannable paramSpannable)
  {
    return lineEnd(paramTextView, paramSpannable);
  }
  
  protected boolean handleMovementKey(TextView paramTextView, Spannable paramSpannable, int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    if ((paramInt1 == 23) && (KeyEvent.metaStateHasNoModifiers(paramInt2)) && (paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0) && (MetaKeyKeyListener.getMetaState(paramSpannable, 2048, paramKeyEvent) != 0)) {
      return paramTextView.showContextMenu();
    }
    return super.handleMovementKey(paramTextView, paramSpannable, paramInt1, paramInt2, paramKeyEvent);
  }
  
  protected boolean home(TextView paramTextView, Spannable paramSpannable)
  {
    return lineStart(paramTextView, paramSpannable);
  }
  
  public void initialize(TextView paramTextView, Spannable paramSpannable)
  {
    Selection.setSelection(paramSpannable, 0);
  }
  
  protected boolean left(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = paramTextView.getLayout();
    if (isSelecting(paramSpannable)) {
      return Selection.extendLeft(paramSpannable, paramTextView);
    }
    return Selection.moveLeft(paramSpannable, paramTextView);
  }
  
  protected boolean leftWord(TextView paramTextView, Spannable paramSpannable)
  {
    int i = paramTextView.getSelectionEnd();
    paramTextView = paramTextView.getWordIterator();
    paramTextView.setCharSequence(paramSpannable, i, i);
    return Selection.moveToPreceding(paramSpannable, paramTextView, isSelecting(paramSpannable));
  }
  
  protected boolean lineEnd(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = paramTextView.getLayout();
    if (isSelecting(paramSpannable)) {
      return Selection.extendToRightEdge(paramSpannable, paramTextView);
    }
    return Selection.moveToRightEdge(paramSpannable, paramTextView);
  }
  
  protected boolean lineStart(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = paramTextView.getLayout();
    if (isSelecting(paramSpannable)) {
      return Selection.extendToLeftEdge(paramSpannable, paramTextView);
    }
    return Selection.moveToLeftEdge(paramSpannable, paramTextView);
  }
  
  public void onTakeFocus(TextView paramTextView, Spannable paramSpannable, int paramInt)
  {
    if ((paramInt & 0x82) != 0)
    {
      if (paramTextView.getLayout() == null) {
        Selection.setSelection(paramSpannable, paramSpannable.length());
      }
    }
    else {
      Selection.setSelection(paramSpannable, paramSpannable.length());
    }
  }
  
  public boolean onTouchEvent(TextView paramTextView, Spannable paramSpannable, MotionEvent paramMotionEvent)
  {
    int i = -1;
    int j = -1;
    int k = paramMotionEvent.getAction();
    if (k == 1)
    {
      i = Touch.getInitialScrollX(paramTextView, paramSpannable);
      j = Touch.getInitialScrollY(paramTextView, paramSpannable);
    }
    boolean bool1 = isSelecting(paramSpannable);
    boolean bool2 = Touch.onTouchEvent(paramTextView, paramSpannable, paramMotionEvent);
    if (paramTextView.didTouchFocusSelect()) {
      return bool2;
    }
    if (k == 0)
    {
      if (isSelecting(paramSpannable))
      {
        if ((!paramTextView.isFocused()) && (!paramTextView.requestFocus())) {
          return bool2;
        }
        j = paramTextView.getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY());
        paramSpannable.setSpan(LAST_TAP_DOWN, j, j, 34);
        paramTextView.getParent().requestDisallowInterceptTouchEvent(true);
      }
    }
    else if (paramTextView.isFocused()) {
      if (k == 2)
      {
        if ((isSelecting(paramSpannable)) && (bool2))
        {
          j = paramSpannable.getSpanStart(LAST_TAP_DOWN);
          paramTextView.cancelLongPress();
          i = paramTextView.getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY());
          Selection.setSelection(paramSpannable, Math.min(j, i), Math.max(j, i));
          return true;
        }
      }
      else if (k == 1)
      {
        if (((j >= 0) && (j != paramTextView.getScrollY())) || ((i >= 0) && (i != paramTextView.getScrollX())))
        {
          paramTextView.moveCursorToVisibleOffset();
          return true;
        }
        if (bool1)
        {
          i = paramSpannable.getSpanStart(LAST_TAP_DOWN);
          j = paramTextView.getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY());
          Selection.setSelection(paramSpannable, Math.min(i, j), Math.max(i, j));
          paramSpannable.removeSpan(LAST_TAP_DOWN);
        }
        MetaKeyKeyListener.adjustMetaAfterKeypress(paramSpannable);
        MetaKeyKeyListener.resetLockedMeta(paramSpannable);
        return true;
      }
    }
    return bool2;
  }
  
  protected boolean pageDown(TextView paramTextView, Spannable paramSpannable)
  {
    Layout localLayout = paramTextView.getLayout();
    boolean bool1 = isSelecting(paramSpannable);
    int i = getCurrentLineTop(paramSpannable, localLayout);
    int j = getPageHeight(paramTextView);
    boolean bool2 = false;
    for (;;)
    {
      int k = Selection.getSelectionEnd(paramSpannable);
      if (bool1) {
        Selection.extendDown(paramSpannable, localLayout);
      } else {
        Selection.moveDown(paramSpannable, localLayout);
      }
      if (Selection.getSelectionEnd(paramSpannable) != k)
      {
        boolean bool3 = true;
        bool2 = true;
        if (getCurrentLineTop(paramSpannable, localLayout) >= i + j) {
          bool2 = bool3;
        }
      }
      else
      {
        return bool2;
      }
    }
  }
  
  protected boolean pageUp(TextView paramTextView, Spannable paramSpannable)
  {
    Layout localLayout = paramTextView.getLayout();
    boolean bool1 = isSelecting(paramSpannable);
    int i = getCurrentLineTop(paramSpannable, localLayout);
    int j = getPageHeight(paramTextView);
    boolean bool2 = false;
    for (;;)
    {
      int k = Selection.getSelectionEnd(paramSpannable);
      if (bool1) {
        Selection.extendUp(paramSpannable, localLayout);
      } else {
        Selection.moveUp(paramSpannable, localLayout);
      }
      if (Selection.getSelectionEnd(paramSpannable) != k)
      {
        boolean bool3 = true;
        bool2 = true;
        if (getCurrentLineTop(paramSpannable, localLayout) <= i - j) {
          bool2 = bool3;
        }
      }
      else
      {
        return bool2;
      }
    }
  }
  
  protected boolean right(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = paramTextView.getLayout();
    if (isSelecting(paramSpannable)) {
      return Selection.extendRight(paramSpannable, paramTextView);
    }
    return Selection.moveRight(paramSpannable, paramTextView);
  }
  
  protected boolean rightWord(TextView paramTextView, Spannable paramSpannable)
  {
    int i = paramTextView.getSelectionEnd();
    paramTextView = paramTextView.getWordIterator();
    paramTextView.setCharSequence(paramSpannable, i, i);
    return Selection.moveToFollowing(paramSpannable, paramTextView, isSelecting(paramSpannable));
  }
  
  protected boolean top(TextView paramTextView, Spannable paramSpannable)
  {
    if (isSelecting(paramSpannable)) {
      Selection.extendSelection(paramSpannable, 0);
    } else {
      Selection.setSelection(paramSpannable, 0);
    }
    return true;
  }
  
  protected boolean up(TextView paramTextView, Spannable paramSpannable)
  {
    paramTextView = paramTextView.getLayout();
    if (isSelecting(paramSpannable)) {
      return Selection.extendUp(paramSpannable, paramTextView);
    }
    return Selection.moveUp(paramSpannable, paramTextView);
  }
}
