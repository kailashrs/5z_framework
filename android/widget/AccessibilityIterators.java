package android.widget;

import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.view.AccessibilityIterators.AbstractTextSegmentIterator;

final class AccessibilityIterators
{
  AccessibilityIterators() {}
  
  static class LineTextSegmentIterator
    extends AccessibilityIterators.AbstractTextSegmentIterator
  {
    protected static final int DIRECTION_END = 1;
    protected static final int DIRECTION_START = -1;
    private static LineTextSegmentIterator sLineInstance;
    protected Layout mLayout;
    
    LineTextSegmentIterator() {}
    
    public static LineTextSegmentIterator getInstance()
    {
      if (sLineInstance == null) {
        sLineInstance = new LineTextSegmentIterator();
      }
      return sLineInstance;
    }
    
    public int[] following(int paramInt)
    {
      if (mText.length() <= 0) {
        return null;
      }
      if (paramInt >= mText.length()) {
        return null;
      }
      if (paramInt < 0)
      {
        paramInt = mLayout.getLineForOffset(0);
      }
      else
      {
        int i = mLayout.getLineForOffset(paramInt);
        if (getLineEdgeIndex(i, -1) == paramInt) {
          paramInt = i;
        } else {
          paramInt = i + 1;
        }
      }
      if (paramInt >= mLayout.getLineCount()) {
        return null;
      }
      return getRange(getLineEdgeIndex(paramInt, -1), getLineEdgeIndex(paramInt, 1) + 1);
    }
    
    protected int getLineEdgeIndex(int paramInt1, int paramInt2)
    {
      if (paramInt2 * mLayout.getParagraphDirection(paramInt1) < 0) {
        return mLayout.getLineStart(paramInt1);
      }
      return mLayout.getLineEnd(paramInt1) - 1;
    }
    
    public void initialize(Spannable paramSpannable, Layout paramLayout)
    {
      mText = paramSpannable.toString();
      mLayout = paramLayout;
    }
    
    public int[] preceding(int paramInt)
    {
      if (mText.length() <= 0) {
        return null;
      }
      if (paramInt <= 0) {
        return null;
      }
      if (paramInt > mText.length())
      {
        paramInt = mLayout.getLineForOffset(mText.length());
      }
      else
      {
        int i = mLayout.getLineForOffset(paramInt);
        if (getLineEdgeIndex(i, 1) + 1 == paramInt) {
          paramInt = i;
        } else {
          paramInt = i - 1;
        }
      }
      if (paramInt < 0) {
        return null;
      }
      return getRange(getLineEdgeIndex(paramInt, -1), getLineEdgeIndex(paramInt, 1) + 1);
    }
  }
  
  static class PageTextSegmentIterator
    extends AccessibilityIterators.LineTextSegmentIterator
  {
    private static PageTextSegmentIterator sPageInstance;
    private final Rect mTempRect = new Rect();
    private TextView mView;
    
    PageTextSegmentIterator() {}
    
    public static PageTextSegmentIterator getInstance()
    {
      if (sPageInstance == null) {
        sPageInstance = new PageTextSegmentIterator();
      }
      return sPageInstance;
    }
    
    public int[] following(int paramInt)
    {
      if (mText.length() <= 0) {
        return null;
      }
      if (paramInt >= mText.length()) {
        return null;
      }
      if (!mView.getGlobalVisibleRect(mTempRect)) {
        return null;
      }
      int i = Math.max(0, paramInt);
      paramInt = mLayout.getLineForOffset(i);
      paramInt = mLayout.getLineTop(paramInt) + (mTempRect.height() - mView.getTotalPaddingTop() - mView.getTotalPaddingBottom());
      if (paramInt < mLayout.getLineTop(mLayout.getLineCount() - 1)) {}
      for (paramInt = mLayout.getLineForVertical(paramInt);; paramInt = mLayout.getLineCount()) {
        break;
      }
      return getRange(i, getLineEdgeIndex(paramInt - 1, 1) + 1);
    }
    
    public void initialize(TextView paramTextView)
    {
      super.initialize((Spannable)paramTextView.getIterableTextForAccessibility(), paramTextView.getLayout());
      mView = paramTextView;
    }
    
    public int[] preceding(int paramInt)
    {
      if (mText.length() <= 0) {
        return null;
      }
      if (paramInt <= 0) {
        return null;
      }
      if (!mView.getGlobalVisibleRect(mTempRect)) {
        return null;
      }
      int i = Math.min(mText.length(), paramInt);
      int j = mLayout.getLineForOffset(i);
      paramInt = mLayout.getLineTop(j) - (mTempRect.height() - mView.getTotalPaddingTop() - mView.getTotalPaddingBottom());
      if (paramInt > 0) {
        paramInt = mLayout.getLineForVertical(paramInt);
      } else {
        paramInt = 0;
      }
      int k = paramInt;
      if (i == mText.length())
      {
        k = paramInt;
        if (paramInt < j) {
          k = paramInt + 1;
        }
      }
      return getRange(getLineEdgeIndex(k, -1), i);
    }
  }
}
