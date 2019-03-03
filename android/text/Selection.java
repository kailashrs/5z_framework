package android.text;

public class Selection
{
  public static final Object SELECTION_END = new END(null);
  private static final Object SELECTION_MEMORY = new MEMORY(null);
  public static final Object SELECTION_START = new START(null);
  
  private Selection() {}
  
  private static int chooseHorizontal(Layout paramLayout, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramLayout.getLineForOffset(paramInt2) == paramLayout.getLineForOffset(paramInt3))
    {
      float f1 = paramLayout.getPrimaryHorizontal(paramInt2);
      float f2 = paramLayout.getPrimaryHorizontal(paramInt3);
      if (paramInt1 < 0)
      {
        if (f1 < f2) {
          return paramInt2;
        }
        return paramInt3;
      }
      if (f1 > f2) {
        return paramInt2;
      }
      return paramInt3;
    }
    if (paramLayout.getParagraphDirection(paramLayout.getLineForOffset(paramInt2)) == paramInt1) {
      return Math.max(paramInt2, paramInt3);
    }
    return Math.min(paramInt2, paramInt3);
  }
  
  public static boolean extendDown(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionEnd(paramSpannable);
    int j = paramLayout.getLineForOffset(i);
    if (j < paramLayout.getLineCount() - 1)
    {
      setSelectionAndMemory(paramSpannable, paramLayout, j, i, 1, true);
      return true;
    }
    if (i != paramSpannable.length())
    {
      extendSelection(paramSpannable, paramSpannable.length(), -1);
      return true;
    }
    return true;
  }
  
  public static boolean extendLeft(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionEnd(paramSpannable);
    int j = paramLayout.getOffsetToLeftOf(i);
    if (j != i)
    {
      extendSelection(paramSpannable, j);
      return true;
    }
    return true;
  }
  
  public static boolean extendRight(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionEnd(paramSpannable);
    int j = paramLayout.getOffsetToRightOf(i);
    if (j != i)
    {
      extendSelection(paramSpannable, j);
      return true;
    }
    return true;
  }
  
  public static final void extendSelection(Spannable paramSpannable, int paramInt)
  {
    extendSelection(paramSpannable, paramInt, -1);
  }
  
  private static void extendSelection(Spannable paramSpannable, int paramInt1, int paramInt2)
  {
    if (paramSpannable.getSpanStart(SELECTION_END) != paramInt1) {
      paramSpannable.setSpan(SELECTION_END, paramInt1, paramInt1, 34);
    }
    updateMemory(paramSpannable, paramInt2);
  }
  
  public static boolean extendToLeftEdge(Spannable paramSpannable, Layout paramLayout)
  {
    extendSelection(paramSpannable, findEdge(paramSpannable, paramLayout, -1));
    return true;
  }
  
  public static boolean extendToRightEdge(Spannable paramSpannable, Layout paramLayout)
  {
    extendSelection(paramSpannable, findEdge(paramSpannable, paramLayout, 1));
    return true;
  }
  
  public static boolean extendUp(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionEnd(paramSpannable);
    int j = paramLayout.getLineForOffset(i);
    if (j > 0)
    {
      setSelectionAndMemory(paramSpannable, paramLayout, j, i, -1, true);
      return true;
    }
    if (i != 0)
    {
      extendSelection(paramSpannable, 0);
      return true;
    }
    return true;
  }
  
  private static int findEdge(Spannable paramSpannable, Layout paramLayout, int paramInt)
  {
    int i = paramLayout.getLineForOffset(getSelectionEnd(paramSpannable));
    if (paramInt * paramLayout.getParagraphDirection(i) < 0) {
      return paramLayout.getLineStart(i);
    }
    paramInt = paramLayout.getLineEnd(i);
    if (i == paramLayout.getLineCount() - 1) {
      return paramInt;
    }
    return paramInt - 1;
  }
  
  public static final int getSelectionEnd(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof Spanned)) {
      return ((Spanned)paramCharSequence).getSpanStart(SELECTION_END);
    }
    return -1;
  }
  
  private static int getSelectionMemory(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof Spanned)) {
      return ((Spanned)paramCharSequence).getSpanStart(SELECTION_MEMORY);
    }
    return -1;
  }
  
  public static final int getSelectionStart(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof Spanned)) {
      return ((Spanned)paramCharSequence).getSpanStart(SELECTION_START);
    }
    return -1;
  }
  
  public static boolean moveDown(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionStart(paramSpannable);
    int j = getSelectionEnd(paramSpannable);
    if (i != j)
    {
      k = Math.min(i, j);
      j = Math.max(i, j);
      setSelection(paramSpannable, j);
      return (k != 0) || (j != paramSpannable.length());
    }
    int k = paramLayout.getLineForOffset(j);
    if (k < paramLayout.getLineCount() - 1)
    {
      setSelectionAndMemory(paramSpannable, paramLayout, k, j, 1, false);
      return true;
    }
    if (j != paramSpannable.length())
    {
      setSelection(paramSpannable, paramSpannable.length());
      return true;
    }
    return false;
  }
  
  public static boolean moveLeft(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionStart(paramSpannable);
    int j = getSelectionEnd(paramSpannable);
    if (i != j)
    {
      setSelection(paramSpannable, chooseHorizontal(paramLayout, -1, i, j));
      return true;
    }
    i = paramLayout.getOffsetToLeftOf(j);
    if (i != j)
    {
      setSelection(paramSpannable, i);
      return true;
    }
    return false;
  }
  
  public static boolean moveRight(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionStart(paramSpannable);
    int j = getSelectionEnd(paramSpannable);
    if (i != j)
    {
      setSelection(paramSpannable, chooseHorizontal(paramLayout, 1, i, j));
      return true;
    }
    i = paramLayout.getOffsetToRightOf(j);
    if (i != j)
    {
      setSelection(paramSpannable, i);
      return true;
    }
    return false;
  }
  
  public static boolean moveToFollowing(Spannable paramSpannable, PositionIterator paramPositionIterator, boolean paramBoolean)
  {
    int i = paramPositionIterator.following(getSelectionEnd(paramSpannable));
    if (i != -1) {
      if (paramBoolean) {
        extendSelection(paramSpannable, i);
      } else {
        setSelection(paramSpannable, i);
      }
    }
    return true;
  }
  
  public static boolean moveToLeftEdge(Spannable paramSpannable, Layout paramLayout)
  {
    setSelection(paramSpannable, findEdge(paramSpannable, paramLayout, -1));
    return true;
  }
  
  public static boolean moveToPreceding(Spannable paramSpannable, PositionIterator paramPositionIterator, boolean paramBoolean)
  {
    int i = paramPositionIterator.preceding(getSelectionEnd(paramSpannable));
    if (i != -1) {
      if (paramBoolean) {
        extendSelection(paramSpannable, i);
      } else {
        setSelection(paramSpannable, i);
      }
    }
    return true;
  }
  
  public static boolean moveToRightEdge(Spannable paramSpannable, Layout paramLayout)
  {
    setSelection(paramSpannable, findEdge(paramSpannable, paramLayout, 1));
    return true;
  }
  
  public static boolean moveUp(Spannable paramSpannable, Layout paramLayout)
  {
    int i = getSelectionStart(paramSpannable);
    int j = getSelectionEnd(paramSpannable);
    if (i != j)
    {
      k = Math.min(i, j);
      j = Math.max(i, j);
      setSelection(paramSpannable, k);
      return (k != 0) || (j != paramSpannable.length());
    }
    int k = paramLayout.getLineForOffset(j);
    if (k > 0)
    {
      setSelectionAndMemory(paramSpannable, paramLayout, k, j, -1, false);
      return true;
    }
    if (j != 0)
    {
      setSelection(paramSpannable, 0);
      return true;
    }
    return false;
  }
  
  private static void removeMemory(Spannable paramSpannable)
  {
    paramSpannable.removeSpan(SELECTION_MEMORY);
    int i = paramSpannable.length();
    int j = 0;
    MemoryTextWatcher[] arrayOfMemoryTextWatcher = (MemoryTextWatcher[])paramSpannable.getSpans(0, i, MemoryTextWatcher.class);
    i = arrayOfMemoryTextWatcher.length;
    while (j < i)
    {
      paramSpannable.removeSpan(arrayOfMemoryTextWatcher[j]);
      j++;
    }
  }
  
  public static final void removeSelection(Spannable paramSpannable)
  {
    paramSpannable.removeSpan(SELECTION_START, 512);
    paramSpannable.removeSpan(SELECTION_END);
    removeMemory(paramSpannable);
  }
  
  public static final void selectAll(Spannable paramSpannable)
  {
    setSelection(paramSpannable, 0, paramSpannable.length());
  }
  
  public static final void setSelection(Spannable paramSpannable, int paramInt)
  {
    setSelection(paramSpannable, paramInt, paramInt);
  }
  
  public static void setSelection(Spannable paramSpannable, int paramInt1, int paramInt2)
  {
    setSelection(paramSpannable, paramInt1, paramInt2, -1);
  }
  
  private static void setSelection(Spannable paramSpannable, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = getSelectionStart(paramSpannable);
    int j = getSelectionEnd(paramSpannable);
    if ((i != paramInt1) || (j != paramInt2))
    {
      paramSpannable.setSpan(SELECTION_START, paramInt1, paramInt1, 546);
      paramSpannable.setSpan(SELECTION_END, paramInt2, paramInt2, 34);
      updateMemory(paramSpannable, paramInt3);
    }
  }
  
  private static void setSelectionAndMemory(Spannable paramSpannable, Layout paramLayout, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if (paramLayout.getParagraphDirection(paramInt1) == paramLayout.getParagraphDirection(paramInt1 + paramInt3))
    {
      int i = getSelectionMemory(paramSpannable);
      if (i > -1)
      {
        paramInt1 = paramLayout.getOffsetForHorizontal(paramInt1 + paramInt3, paramLayout.getPrimaryHorizontal(i));
        paramInt2 = i;
      }
      else
      {
        paramInt1 = paramLayout.getOffsetForHorizontal(paramInt1 + paramInt3, paramLayout.getPrimaryHorizontal(paramInt2));
      }
    }
    else
    {
      paramInt1 = paramLayout.getLineStart(paramInt1 + paramInt3);
      paramInt2 = -1;
    }
    if (paramBoolean) {
      extendSelection(paramSpannable, paramInt1, paramInt2);
    } else {
      setSelection(paramSpannable, paramInt1, paramInt1, paramInt2);
    }
  }
  
  private static void updateMemory(Spannable paramSpannable, int paramInt)
  {
    if (paramInt > -1)
    {
      int i = getSelectionMemory(paramSpannable);
      if (paramInt != i)
      {
        paramSpannable.setSpan(SELECTION_MEMORY, paramInt, paramInt, 34);
        if (i == -1) {
          paramSpannable.setSpan(new MemoryTextWatcher(), 0, paramSpannable.length(), 18);
        }
      }
    }
    else
    {
      removeMemory(paramSpannable);
    }
  }
  
  private static final class END
    implements NoCopySpan
  {
    private END() {}
  }
  
  private static final class MEMORY
    implements NoCopySpan
  {
    private MEMORY() {}
  }
  
  public static final class MemoryTextWatcher
    implements TextWatcher
  {
    public MemoryTextWatcher() {}
    
    public void afterTextChanged(Editable paramEditable)
    {
      paramEditable.removeSpan(Selection.SELECTION_MEMORY);
      paramEditable.removeSpan(this);
    }
    
    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  }
  
  public static abstract interface PositionIterator
  {
    public static final int DONE = -1;
    
    public abstract int following(int paramInt);
    
    public abstract int preceding(int paramInt);
  }
  
  private static final class START
    implements NoCopySpan
  {
    private START() {}
  }
}
