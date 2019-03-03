package android.text;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SpanSet<E>
{
  private final Class<? extends E> classType;
  int numberOfSpans;
  int[] spanEnds;
  int[] spanFlags;
  int[] spanStarts;
  E[] spans;
  
  SpanSet(Class<? extends E> paramClass)
  {
    classType = paramClass;
    numberOfSpans = 0;
  }
  
  int getNextTransition(int paramInt1, int paramInt2)
  {
    for (int i = 0; i < numberOfSpans; i++)
    {
      int j = spanStarts[i];
      int k = spanEnds[i];
      int m = paramInt2;
      if (j > paramInt1)
      {
        m = paramInt2;
        if (j < paramInt2) {
          m = j;
        }
      }
      paramInt2 = m;
      if (k > paramInt1)
      {
        paramInt2 = m;
        if (k < m) {
          paramInt2 = k;
        }
      }
    }
    return paramInt2;
  }
  
  public boolean hasSpansIntersecting(int paramInt1, int paramInt2)
  {
    for (int i = 0; i < numberOfSpans; i++) {
      if ((spanStarts[i] < paramInt2) && (spanEnds[i] > paramInt1)) {
        return true;
      }
    }
    return false;
  }
  
  public void init(Spanned paramSpanned, int paramInt1, int paramInt2)
  {
    Object[] arrayOfObject = paramSpanned.getSpans(paramInt1, paramInt2, classType);
    paramInt2 = arrayOfObject.length;
    if ((paramInt2 > 0) && ((spans == null) || (spans.length < paramInt2)))
    {
      spans = ((Object[])Array.newInstance(classType, paramInt2));
      spanStarts = new int[paramInt2];
      spanEnds = new int[paramInt2];
      spanFlags = new int[paramInt2];
    }
    int i = numberOfSpans;
    paramInt1 = 0;
    numberOfSpans = 0;
    while (paramInt1 < paramInt2)
    {
      Object localObject = arrayOfObject[paramInt1];
      int j = paramSpanned.getSpanStart(localObject);
      int k = paramSpanned.getSpanEnd(localObject);
      if (j != k)
      {
        int m = paramSpanned.getSpanFlags(localObject);
        spans[numberOfSpans] = localObject;
        spanStarts[numberOfSpans] = j;
        spanEnds[numberOfSpans] = k;
        spanFlags[numberOfSpans] = m;
        numberOfSpans += 1;
      }
      paramInt1++;
    }
    if (numberOfSpans < i) {
      Arrays.fill(spans, numberOfSpans, i, null);
    }
  }
  
  public void recycle()
  {
    if (spans != null) {
      Arrays.fill(spans, 0, numberOfSpans, null);
    }
  }
}
