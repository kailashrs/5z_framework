package android.text;

import android.graphics.BaseCanvas;
import android.graphics.Paint;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.reflect.Array;
import java.util.IdentityHashMap;
import libcore.util.EmptyArray;

public class SpannableStringBuilder
  implements CharSequence, GetChars, Spannable, Editable, Appendable, GraphicsOperations
{
  private static final int END_MASK = 15;
  private static final int MARK = 1;
  private static final InputFilter[] NO_FILTERS = new InputFilter[0];
  private static final int PARAGRAPH = 3;
  private static final int POINT = 2;
  private static final int SPAN_ADDED = 2048;
  private static final int SPAN_END_AT_END = 32768;
  private static final int SPAN_END_AT_START = 16384;
  private static final int SPAN_START_AT_END = 8192;
  private static final int SPAN_START_AT_START = 4096;
  private static final int SPAN_START_END_MASK = 61440;
  private static final int START_MASK = 240;
  private static final int START_SHIFT = 4;
  private static final String TAG = "SpannableStringBuilder";
  @GuardedBy("sCachedIntBuffer")
  private static final int[][] sCachedIntBuffer = new int[6][0];
  private InputFilter[] mFilters = NO_FILTERS;
  private int mGapLength;
  private int mGapStart;
  private IdentityHashMap<Object, Integer> mIndexOfSpan;
  private int mLowWaterMark;
  private int mSpanCount;
  private int[] mSpanEnds;
  private int[] mSpanFlags;
  private int mSpanInsertCount;
  private int[] mSpanMax;
  private int[] mSpanOrder;
  private int[] mSpanStarts;
  private Object[] mSpans;
  private char[] mText;
  private int mTextWatcherDepth;
  
  public SpannableStringBuilder()
  {
    this("");
  }
  
  public SpannableStringBuilder(CharSequence paramCharSequence)
  {
    this(paramCharSequence, 0, paramCharSequence.length());
  }
  
  public SpannableStringBuilder(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    int i = paramInt2 - paramInt1;
    if (i >= 0)
    {
      mText = ArrayUtils.newUnpaddedCharArray(GrowingArrayUtils.growSize(i));
      mGapStart = i;
      mGapLength = (mText.length - i);
      Object localObject = mText;
      int j = 0;
      TextUtils.getChars(paramCharSequence, paramInt1, paramInt2, (char[])localObject, 0);
      mSpanCount = 0;
      mSpanInsertCount = 0;
      mSpans = EmptyArray.OBJECT;
      mSpanStarts = EmptyArray.INT;
      mSpanEnds = EmptyArray.INT;
      mSpanFlags = EmptyArray.INT;
      mSpanMax = EmptyArray.INT;
      mSpanOrder = EmptyArray.INT;
      if ((paramCharSequence instanceof Spanned))
      {
        paramCharSequence = (Spanned)paramCharSequence;
        localObject = paramCharSequence.getSpans(paramInt1, paramInt2, Object.class);
        while (j < localObject.length)
        {
          if (!(localObject[j] instanceof NoCopySpan))
          {
            int k = paramCharSequence.getSpanStart(localObject[j]) - paramInt1;
            int m = paramCharSequence.getSpanEnd(localObject[j]) - paramInt1;
            int n = paramCharSequence.getSpanFlags(localObject[j]);
            i = k;
            if (k < 0) {
              i = 0;
            }
            k = i;
            if (i > paramInt2 - paramInt1) {
              k = paramInt2 - paramInt1;
            }
            i = m;
            if (m < 0) {
              i = 0;
            }
            if (i > paramInt2 - paramInt1) {
              i = paramInt2 - paramInt1;
            }
            setSpan(false, localObject[j], k, i, n, false);
          }
          j++;
        }
        restoreInvariants();
      }
      return;
    }
    throw new StringIndexOutOfBoundsException();
  }
  
  private int calcMax(int paramInt)
  {
    int i = 0;
    if ((paramInt & 0x1) != 0) {
      i = calcMax(leftChild(paramInt));
    }
    int j = i;
    if (paramInt < mSpanCount)
    {
      i = Math.max(i, mSpanEnds[paramInt]);
      j = i;
      if ((paramInt & 0x1) != 0) {
        j = Math.max(i, calcMax(rightChild(paramInt)));
      }
    }
    mSpanMax[paramInt] = j;
    return j;
  }
  
  private void change(int paramInt1, int paramInt2, CharSequence paramCharSequence, int paramInt3, int paramInt4)
  {
    int i = paramInt3;
    int j = paramInt4;
    int k = paramInt2 - paramInt1;
    int m = j - i;
    int n = m - k;
    int i1 = mSpanCount - 1;
    int i4;
    int i5;
    int i7;
    Object localObject;
    for (int i2 = 0; i1 >= 0; i2 = i7)
    {
      i3 = mSpanStarts[i1];
      i4 = i3;
      if (i3 > mGapStart) {
        i4 = i3 - mGapLength;
      }
      i5 = mSpanEnds[i1];
      i3 = i5;
      if (i5 > mGapStart) {
        i3 = i5 - mGapLength;
      }
      int i6 = i4;
      i5 = i3;
      i7 = i2;
      if ((mSpanFlags[i1] & 0x33) == 51)
      {
        i7 = length();
        i6 = i4;
        if (i4 > paramInt1)
        {
          i6 = i4;
          if (i4 <= paramInt2) {
            for (i5 = paramInt2;; i5++)
            {
              i6 = i5;
              if (i5 >= i7) {
                break;
              }
              if ((i5 > paramInt2) && (charAt(i5 - 1) == '\n'))
              {
                i6 = i5;
                break;
              }
            }
          }
        }
        if ((i3 > paramInt1) && (i3 <= paramInt2)) {
          for (i5 = paramInt2; (i5 < i7) && ((i5 <= paramInt2) || (charAt(i5 - 1) != '\n')); i5++) {}
        } else {
          i5 = i3;
        }
        if ((i6 == i4) && (i5 == i3))
        {
          i7 = i2;
        }
        else
        {
          setSpan(false, mSpans[i1], i6, i5, mSpanFlags[i1], true);
          i7 = 1;
        }
      }
      i4 = 0;
      if (i6 == paramInt1) {
        i4 = 0x0 | 0x1000;
      } else if (i6 == paramInt2 + n) {
        i4 = 0x0 | 0x2000;
      }
      if (i5 == paramInt1)
      {
        i3 = i4 | 0x4000;
      }
      else
      {
        i3 = i4;
        if (i5 == paramInt2 + n) {
          i3 = i4 | 0x8000;
        }
      }
      localObject = mSpanFlags;
      localObject[i1] |= i3;
      i1--;
    }
    if (i2 != 0) {
      restoreInvariants();
    }
    moveGapTo(paramInt2);
    if (n >= mGapLength) {
      resizeFor(mText.length + n - mGapLength);
    }
    int i3 = 0;
    boolean bool1;
    if (m == 0) {
      bool1 = true;
    }
    while ((k > 0) && (mSpanCount > 0) && (removeSpansForChange(paramInt1, paramInt2, bool1, treeRoot()))) {}
    mGapStart += n;
    mGapLength -= n;
    if (mGapLength < 1) {
      new Exception("mGapLength < 1").printStackTrace();
    }
    TextUtils.getChars(paramCharSequence, i, j, mText, paramInt1);
    if (k > 0)
    {
      boolean bool2;
      if (mGapStart + mGapLength == mText.length) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      for (paramInt2 = 0; paramInt2 < mSpanCount; paramInt2++)
      {
        i4 = mSpanFlags[paramInt2];
        mSpanStarts[paramInt2] = updatedIntervalBound(mSpanStarts[paramInt2], paramInt1, n, (i4 & 0xF0) >> 4, bool2, bool1);
        i4 = mSpanFlags[paramInt2];
        mSpanEnds[paramInt2] = updatedIntervalBound(mSpanEnds[paramInt2], paramInt1, n, i4 & 0xF, bool2, bool1);
      }
      i4 = j;
      paramInt2 = i;
      restoreInvariants();
    }
    else
    {
      i4 = j;
      paramInt2 = i;
    }
    if ((paramCharSequence instanceof Spanned))
    {
      paramCharSequence = (Spanned)paramCharSequence;
      localObject = paramCharSequence.getSpans(paramInt2, i4, Object.class);
      while (i3 < localObject.length)
      {
        j = paramCharSequence.getSpanStart(localObject[i3]);
        i = paramCharSequence.getSpanEnd(localObject[i3]);
        i5 = j;
        if (j < paramInt2) {
          i5 = paramInt2;
        }
        j = i;
        if (i > i4) {
          j = i4;
        }
        if (getSpanStart(localObject[i3]) < 0)
        {
          i4 = paramCharSequence.getSpanFlags(localObject[i3]);
          setSpan(false, localObject[i3], i5 - paramInt2 + paramInt1, j - paramInt2 + paramInt1, i4 | 0x800, false);
        }
        i3++;
        paramInt2 = paramInt3;
        i4 = paramInt4;
      }
      restoreInvariants();
    }
  }
  
  private void checkRange(String paramString, int paramInt1, int paramInt2)
  {
    if (paramInt2 >= paramInt1)
    {
      int i = length();
      if ((paramInt1 <= i) && (paramInt2 <= i))
      {
        if ((paramInt1 >= 0) && (paramInt2 >= 0)) {
          return;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append(" ");
        localStringBuilder.append(region(paramInt1, paramInt2));
        localStringBuilder.append(" starts before 0");
        throw new IndexOutOfBoundsException(localStringBuilder.toString());
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" ");
      localStringBuilder.append(region(paramInt1, paramInt2));
      localStringBuilder.append(" ends beyond length ");
      localStringBuilder.append(i);
      throw new IndexOutOfBoundsException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" ");
    localStringBuilder.append(region(paramInt1, paramInt2));
    localStringBuilder.append(" has end before start");
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  private static int[] checkSortBuffer(int[] paramArrayOfInt, int paramInt)
  {
    if ((paramArrayOfInt != null) && (paramInt <= paramArrayOfInt.length)) {
      return paramArrayOfInt;
    }
    return ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(paramInt));
  }
  
  private final int compareSpans(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = paramArrayOfInt1[paramInt1];
    int j = paramArrayOfInt1[paramInt2];
    if (i == j) {
      return Integer.compare(paramArrayOfInt2[paramInt1], paramArrayOfInt2[paramInt2]);
    }
    return Integer.compare(j, i);
  }
  
  private int countSpans(int paramInt1, int paramInt2, Class paramClass, int paramInt3)
  {
    int i = 0;
    int j = i;
    int k;
    int m;
    if ((paramInt3 & 0x1) != 0)
    {
      k = leftChild(paramInt3);
      j = mSpanMax[k];
      m = j;
      if (j > mGapStart) {
        m = j - mGapLength;
      }
      j = i;
      if (m >= paramInt1) {
        j = countSpans(paramInt1, paramInt2, paramClass, k);
      }
    }
    i = j;
    if (paramInt3 < mSpanCount)
    {
      m = mSpanStarts[paramInt3];
      k = m;
      if (m > mGapStart) {
        k = m - mGapLength;
      }
      i = j;
      if (k <= paramInt2)
      {
        m = mSpanEnds[paramInt3];
        i = m;
        if (m > mGapStart) {
          i = m - mGapLength;
        }
        m = j;
        if (i >= paramInt1) {
          if ((k != i) && (paramInt1 != paramInt2))
          {
            m = j;
            if (k != paramInt2)
            {
              m = j;
              if (i == paramInt1) {}
            }
          }
          else if (Object.class != paramClass)
          {
            m = j;
            if (!paramClass.isInstance(mSpans[paramInt3])) {}
          }
          else
          {
            m = j + 1;
          }
        }
        i = m;
        if ((paramInt3 & 0x1) != 0) {
          i = m + countSpans(paramInt1, paramInt2, paramClass, rightChild(paramInt3));
        }
      }
    }
    return i;
  }
  
  private <T> int getSpansRec(int paramInt1, int paramInt2, Class<T> paramClass, int paramInt3, T[] paramArrayOfT, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4, boolean paramBoolean)
  {
    int i;
    if ((paramInt3 & 0x1) != 0)
    {
      i = leftChild(paramInt3);
      j = mSpanMax[i];
      k = j;
      if (j > mGapStart) {
        k = j - mGapLength;
      }
      if (k >= paramInt1) {
        paramInt4 = getSpansRec(paramInt1, paramInt2, paramClass, i, paramArrayOfT, paramArrayOfInt1, paramArrayOfInt2, paramInt4, paramBoolean);
      }
    }
    if (paramInt3 >= mSpanCount) {
      return paramInt4;
    }
    int k = mSpanStarts[paramInt3];
    int j = k;
    if (k > mGapStart) {
      j = k - mGapLength;
    }
    if (j <= paramInt2)
    {
      k = mSpanEnds[paramInt3];
      i = k;
      if (k > mGapStart) {
        i = k - mGapLength;
      }
      k = paramInt4;
      if (i >= paramInt1) {
        if ((j != i) && (paramInt1 != paramInt2))
        {
          k = paramInt4;
          if (j != paramInt2)
          {
            k = paramInt4;
            if (i == paramInt1) {}
          }
        }
        else if (Object.class != paramClass)
        {
          k = paramInt4;
          if (!paramClass.isInstance(mSpans[paramInt3])) {}
        }
        else
        {
          j = mSpanFlags[paramInt3] & 0xFF0000;
          k = paramInt4;
          if (paramBoolean)
          {
            paramArrayOfInt1[k] = j;
            paramArrayOfInt2[k] = mSpanOrder[paramInt3];
          }
          else if (j != 0)
          {
            for (k = 0; (k < paramInt4) && (j <= (getSpanFlags(paramArrayOfT[k]) & 0xFF0000)); k++) {}
            System.arraycopy(paramArrayOfT, k, paramArrayOfT, k + 1, paramInt4 - k);
          }
          paramArrayOfT[k] = mSpans[paramInt3];
          k = paramInt4 + 1;
        }
      }
      if ((k < paramArrayOfT.length) && ((paramInt3 & 0x1) != 0)) {
        paramInt4 = getSpansRec(paramInt1, paramInt2, paramClass, rightChild(paramInt3), paramArrayOfT, paramArrayOfInt1, paramArrayOfInt2, k, paramBoolean);
      } else {
        paramInt4 = k;
      }
    }
    return paramInt4;
  }
  
  private static boolean hasNonExclusiveExclusiveSpanAt(CharSequence paramCharSequence, int paramInt)
  {
    if ((paramCharSequence instanceof Spanned))
    {
      Spanned localSpanned = (Spanned)paramCharSequence;
      paramCharSequence = localSpanned.getSpans(paramInt, paramInt, Object.class);
      int i = paramCharSequence.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        if (localSpanned.getSpanFlags(paramCharSequence[paramInt]) != 33) {
          return true;
        }
      }
    }
    return false;
  }
  
  private void invalidateIndex(int paramInt)
  {
    mLowWaterMark = Math.min(paramInt, mLowWaterMark);
  }
  
  private boolean isInvalidParagraph(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt2 == 3) && (paramInt1 != 0) && (paramInt1 != length()) && (charAt(paramInt1 - 1) != '\n')) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static int leftChild(int paramInt)
  {
    return paramInt - ((paramInt + 1 & paramInt) >> 1);
  }
  
  private void moveGapTo(int paramInt)
  {
    if (paramInt == mGapStart) {
      return;
    }
    int i = length();
    int j = 0;
    int k;
    if (paramInt == i) {
      k = 1;
    } else {
      k = 0;
    }
    if (paramInt < mGapStart)
    {
      i = mGapStart - paramInt;
      System.arraycopy(mText, paramInt, mText, mGapStart + mGapLength - i, i);
    }
    else
    {
      i = paramInt - mGapStart;
      System.arraycopy(mText, mGapLength + paramInt - i, mText, mGapStart, i);
    }
    if (mSpanCount != 0)
    {
      while (j < mSpanCount)
      {
        i = mSpanStarts[j];
        int m = mSpanEnds[j];
        int n = i;
        if (i > mGapStart) {
          n = i - mGapLength;
        }
        int i1;
        if (n > paramInt)
        {
          i = n + mGapLength;
        }
        else
        {
          i = n;
          if (n == paramInt)
          {
            i1 = (mSpanFlags[j] & 0xF0) >> 4;
            if (i1 != 2)
            {
              i = n;
              if (k != 0)
              {
                i = n;
                if (i1 != 3) {}
              }
            }
            else
            {
              i = n + mGapLength;
            }
          }
        }
        n = m;
        if (m > mGapStart) {
          n = m - mGapLength;
        }
        if (n > paramInt)
        {
          m = n + mGapLength;
        }
        else
        {
          m = n;
          if (n == paramInt)
          {
            i1 = mSpanFlags[j] & 0xF;
            if (i1 != 2)
            {
              m = n;
              if (k != 0)
              {
                m = n;
                if (i1 != 3) {}
              }
            }
            else
            {
              m = n + mGapLength;
            }
          }
        }
        mSpanStarts[j] = i;
        mSpanEnds[j] = m;
        j++;
      }
      calcMax(treeRoot());
    }
    mGapStart = paramInt;
  }
  
  private int nextSpanTransitionRec(int paramInt1, int paramInt2, Class paramClass, int paramInt3)
  {
    int i = paramInt2;
    if ((paramInt3 & 0x1) != 0)
    {
      j = leftChild(paramInt3);
      i = paramInt2;
      if (resolveGap(mSpanMax[j]) > paramInt1) {
        i = nextSpanTransitionRec(paramInt1, paramInt2, paramClass, j);
      }
    }
    int j = i;
    if (paramInt3 < mSpanCount)
    {
      int k = resolveGap(mSpanStarts[paramInt3]);
      int m = resolveGap(mSpanEnds[paramInt3]);
      j = i;
      if (k > paramInt1)
      {
        j = i;
        if (k < i)
        {
          j = i;
          if (paramClass.isInstance(mSpans[paramInt3])) {
            j = k;
          }
        }
      }
      paramInt2 = j;
      if (m > paramInt1)
      {
        paramInt2 = j;
        if (m < j)
        {
          paramInt2 = j;
          if (paramClass.isInstance(mSpans[paramInt3])) {
            paramInt2 = m;
          }
        }
      }
      j = paramInt2;
      if (k < paramInt2)
      {
        j = paramInt2;
        if ((paramInt3 & 0x1) != 0) {
          j = nextSpanTransitionRec(paramInt1, paramInt2, paramClass, rightChild(paramInt3));
        }
      }
    }
    return j;
  }
  
  private static int[] obtain(int paramInt)
  {
    int[] arrayOfInt = null;
    int[][] arrayOfInt1 = sCachedIntBuffer;
    int i = -1;
    try
    {
      int j = sCachedIntBuffer.length - 1;
      int k;
      for (;;)
      {
        k = i;
        if (j < 0) {
          break;
        }
        k = i;
        if (sCachedIntBuffer[j] != null)
        {
          if (sCachedIntBuffer[j].length >= paramInt)
          {
            k = j;
            break;
          }
          k = i;
          if (i == -1) {
            k = j;
          }
        }
        j--;
        i = k;
      }
      if (k != -1)
      {
        arrayOfInt = sCachedIntBuffer[k];
        sCachedIntBuffer[k] = null;
      }
      return checkSortBuffer(arrayOfInt, paramInt);
    }
    finally {}
  }
  
  private static void recycle(int[] paramArrayOfInt)
  {
    int[][] arrayOfInt = sCachedIntBuffer;
    int i = 0;
    try
    {
      while (i < sCachedIntBuffer.length) {
        if ((sCachedIntBuffer[i] != null) && (paramArrayOfInt.length <= sCachedIntBuffer[i].length)) {
          i++;
        } else {
          sCachedIntBuffer[i] = paramArrayOfInt;
        }
      }
      return;
    }
    finally {}
  }
  
  private static String region(int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" ... ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  private void removeSpan(int paramInt1, int paramInt2)
  {
    Object localObject = mSpans[paramInt1];
    int i = mSpanStarts[paramInt1];
    int j = mSpanEnds[paramInt1];
    int k = i;
    if (i > mGapStart) {
      k = i - mGapLength;
    }
    i = j;
    if (j > mGapStart) {
      i = j - mGapLength;
    }
    j = mSpanCount - (paramInt1 + 1);
    System.arraycopy(mSpans, paramInt1 + 1, mSpans, paramInt1, j);
    System.arraycopy(mSpanStarts, paramInt1 + 1, mSpanStarts, paramInt1, j);
    System.arraycopy(mSpanEnds, paramInt1 + 1, mSpanEnds, paramInt1, j);
    System.arraycopy(mSpanFlags, paramInt1 + 1, mSpanFlags, paramInt1, j);
    System.arraycopy(mSpanOrder, paramInt1 + 1, mSpanOrder, paramInt1, j);
    mSpanCount -= 1;
    invalidateIndex(paramInt1);
    mSpans[mSpanCount] = null;
    restoreInvariants();
    if ((paramInt2 & 0x200) == 0) {
      sendSpanRemoved(localObject, k, i);
    }
  }
  
  private boolean removeSpansForChange(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    boolean bool = true;
    if (((paramInt3 & 0x1) != 0) && (resolveGap(mSpanMax[paramInt3]) >= paramInt1) && (removeSpansForChange(paramInt1, paramInt2, paramBoolean, leftChild(paramInt3)))) {
      return true;
    }
    if (paramInt3 < mSpanCount)
    {
      if (((mSpanFlags[paramInt3] & 0x21) == 33) && (mSpanStarts[paramInt3] >= paramInt1) && (mSpanStarts[paramInt3] < mGapStart + mGapLength) && (mSpanEnds[paramInt3] >= paramInt1) && (mSpanEnds[paramInt3] < mGapStart + mGapLength) && ((paramBoolean) || (mSpanStarts[paramInt3] > paramInt1) || (mSpanEnds[paramInt3] < mGapStart)))
      {
        mIndexOfSpan.remove(mSpans[paramInt3]);
        removeSpan(paramInt3, 0);
        return true;
      }
      if ((resolveGap(mSpanStarts[paramInt3]) <= paramInt2) && ((paramInt3 & 0x1) != 0) && (removeSpansForChange(paramInt1, paramInt2, paramBoolean, rightChild(paramInt3)))) {
        paramBoolean = bool;
      } else {
        paramBoolean = false;
      }
      return paramBoolean;
    }
    return false;
  }
  
  private void resizeFor(int paramInt)
  {
    int i = mText.length;
    if (paramInt + 1 <= i) {
      return;
    }
    Object localObject = ArrayUtils.newUnpaddedCharArray(GrowingArrayUtils.growSize(paramInt));
    char[] arrayOfChar = mText;
    int j = mGapStart;
    paramInt = 0;
    System.arraycopy(arrayOfChar, 0, localObject, 0, j);
    int k = localObject.length;
    j = k - i;
    int m = i - (mGapStart + mGapLength);
    System.arraycopy(mText, i - m, localObject, k - m, m);
    mText = ((char[])localObject);
    mGapLength += j;
    if (mGapLength < 1) {
      new Exception("mGapLength < 1").printStackTrace();
    }
    if (mSpanCount != 0)
    {
      while (paramInt < mSpanCount)
      {
        if (mSpanStarts[paramInt] > mGapStart)
        {
          localObject = mSpanStarts;
          localObject[paramInt] += j;
        }
        if (mSpanEnds[paramInt] > mGapStart)
        {
          localObject = mSpanEnds;
          localObject[paramInt] += j;
        }
        paramInt++;
      }
      calcMax(treeRoot());
    }
  }
  
  private int resolveGap(int paramInt)
  {
    if (paramInt > mGapStart) {
      paramInt -= mGapLength;
    }
    return paramInt;
  }
  
  private void restoreInvariants()
  {
    if (mSpanCount == 0) {
      return;
    }
    Object localObject;
    for (int i = 1; i < mSpanCount; i++) {
      if (mSpanStarts[i] < mSpanStarts[(i - 1)])
      {
        localObject = mSpans[i];
        int j = mSpanStarts[i];
        int k = mSpanEnds[i];
        int m = mSpanFlags[i];
        int n = mSpanOrder[i];
        int i1 = i;
        int i2;
        do
        {
          mSpans[i1] = mSpans[(i1 - 1)];
          mSpanStarts[i1] = mSpanStarts[(i1 - 1)];
          mSpanEnds[i1] = mSpanEnds[(i1 - 1)];
          mSpanFlags[i1] = mSpanFlags[(i1 - 1)];
          mSpanOrder[i1] = mSpanOrder[(i1 - 1)];
          i2 = i1 - 1;
          if (i2 <= 0) {
            break;
          }
          i1 = i2;
        } while (j < mSpanStarts[(i2 - 1)]);
        mSpans[i2] = localObject;
        mSpanStarts[i2] = j;
        mSpanEnds[i2] = k;
        mSpanFlags[i2] = m;
        mSpanOrder[i2] = n;
        invalidateIndex(i2);
      }
    }
    calcMax(treeRoot());
    if (mIndexOfSpan == null) {
      mIndexOfSpan = new IdentityHashMap();
    }
    for (i = mLowWaterMark; i < mSpanCount; i++)
    {
      localObject = (Integer)mIndexOfSpan.get(mSpans[i]);
      if ((localObject == null) || (((Integer)localObject).intValue() != i)) {
        mIndexOfSpan.put(mSpans[i], Integer.valueOf(i));
      }
    }
    mLowWaterMark = Integer.MAX_VALUE;
  }
  
  private static int rightChild(int paramInt)
  {
    return ((paramInt + 1 & paramInt) >> 1) + paramInt;
  }
  
  private void sendAfterTextChanged(TextWatcher[] paramArrayOfTextWatcher)
  {
    int i = paramArrayOfTextWatcher.length;
    mTextWatcherDepth += 1;
    for (int j = 0; j < i; j++) {
      paramArrayOfTextWatcher[j].afterTextChanged(this);
    }
    mTextWatcherDepth -= 1;
  }
  
  private void sendBeforeTextChanged(TextWatcher[] paramArrayOfTextWatcher, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramArrayOfTextWatcher.length;
    mTextWatcherDepth += 1;
    for (int j = 0; j < i; j++) {
      paramArrayOfTextWatcher[j].beforeTextChanged(this, paramInt1, paramInt2, paramInt3);
    }
    mTextWatcherDepth -= 1;
  }
  
  private void sendSpanAdded(Object paramObject, int paramInt1, int paramInt2)
  {
    SpanWatcher[] arrayOfSpanWatcher = (SpanWatcher[])getSpans(paramInt1, paramInt2, SpanWatcher.class);
    int i = arrayOfSpanWatcher.length;
    for (int j = 0; j < i; j++) {
      arrayOfSpanWatcher[j].onSpanAdded(this, paramObject, paramInt1, paramInt2);
    }
  }
  
  private void sendSpanChanged(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    SpanWatcher[] arrayOfSpanWatcher = (SpanWatcher[])getSpans(Math.min(paramInt1, paramInt3), Math.min(Math.max(paramInt2, paramInt4), length()), SpanWatcher.class);
    int i = arrayOfSpanWatcher.length;
    for (int j = 0; j < i; j++) {
      arrayOfSpanWatcher[j].onSpanChanged(this, paramObject, paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  private void sendSpanRemoved(Object paramObject, int paramInt1, int paramInt2)
  {
    SpanWatcher[] arrayOfSpanWatcher = (SpanWatcher[])getSpans(paramInt1, paramInt2, SpanWatcher.class);
    int i = arrayOfSpanWatcher.length;
    for (int j = 0; j < i; j++) {
      arrayOfSpanWatcher[j].onSpanRemoved(this, paramObject, paramInt1, paramInt2);
    }
  }
  
  private void sendTextChanged(TextWatcher[] paramArrayOfTextWatcher, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramArrayOfTextWatcher.length;
    mTextWatcherDepth += 1;
    for (int j = 0; j < i; j++) {
      paramArrayOfTextWatcher[j].onTextChanged(this, paramInt1, paramInt2, paramInt3);
    }
    mTextWatcherDepth -= 1;
  }
  
  private void sendToSpanWatchers(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int n;
    int[] arrayOfInt;
    for (int j = 0; j < mSpanCount; j++)
    {
      int k = mSpanFlags[j];
      if ((k & 0x800) == 0)
      {
        int m = mSpanStarts[j];
        n = mSpanEnds[j];
        int i1 = m;
        if (m > mGapStart) {
          i1 = m - mGapLength;
        }
        m = n;
        if (n > mGapStart) {
          m = n - mGapLength;
        }
        int i2 = paramInt2 + paramInt3;
        int i3 = 0;
        int i4 = i1;
        int i5;
        if (i1 > i2)
        {
          n = i3;
          i5 = i4;
          if (paramInt3 != 0)
          {
            i5 = i4 - paramInt3;
            n = 1;
          }
        }
        else
        {
          n = i3;
          i5 = i4;
          if (i1 >= paramInt1) {
            if (i1 == paramInt1)
            {
              n = i3;
              i5 = i4;
              if ((k & 0x1000) == 4096) {}
            }
            else if (i1 == i2)
            {
              n = i3;
              i5 = i4;
              if ((k & 0x2000) == 8192) {}
            }
            else
            {
              n = 1;
              i5 = i4;
            }
          }
        }
        int i6 = m;
        if (m > i2)
        {
          i4 = n;
          i3 = i6;
          if (paramInt3 != 0)
          {
            i3 = i6 - paramInt3;
            i4 = 1;
          }
        }
        else
        {
          i4 = n;
          i3 = i6;
          if (m >= paramInt1) {
            if (m == paramInt1)
            {
              i4 = n;
              i3 = i6;
              if ((k & 0x4000) == 16384) {}
            }
            else if (m == i2)
            {
              i4 = n;
              i3 = i6;
              if ((k & 0x8000) == 32768) {}
            }
            else
            {
              i4 = 1;
              i3 = i6;
            }
          }
        }
        if (i4 != 0) {
          sendSpanChanged(mSpans[j], i5, i3, i1, m);
        }
        arrayOfInt = mSpanFlags;
        arrayOfInt[j] &= 0xFFFF0FFF;
      }
    }
    for (paramInt1 = i; paramInt1 < mSpanCount; paramInt1++) {
      if ((mSpanFlags[paramInt1] & 0x800) != 0)
      {
        arrayOfInt = mSpanFlags;
        arrayOfInt[paramInt1] &= 0xF7FF;
        paramInt3 = mSpanStarts[paramInt1];
        n = mSpanEnds[paramInt1];
        paramInt2 = paramInt3;
        if (paramInt3 > mGapStart) {
          paramInt2 = paramInt3 - mGapLength;
        }
        paramInt3 = n;
        if (n > mGapStart) {
          paramInt3 = n - mGapLength;
        }
        sendSpanAdded(mSpans[paramInt1], paramInt2, paramInt3);
      }
    }
  }
  
  private void setSpan(boolean paramBoolean1, Object paramObject, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean2)
  {
    int i = paramInt1;
    checkRange("setSpan", i, paramInt2);
    int j = (paramInt3 & 0xF0) >> 4;
    if (isInvalidParagraph(i, j))
    {
      if (!paramBoolean2) {
        return;
      }
      paramObject = new StringBuilder();
      paramObject.append("PARAGRAPH span must start at paragraph boundary (");
      paramObject.append(i);
      paramObject.append(" follows ");
      paramObject.append(charAt(i - 1));
      paramObject.append(")");
      throw new RuntimeException(paramObject.toString());
    }
    int k = paramInt3 & 0xF;
    if (isInvalidParagraph(paramInt2, k))
    {
      if (!paramBoolean2) {
        return;
      }
      paramObject = new StringBuilder();
      paramObject.append("PARAGRAPH span must end at paragraph boundary (");
      paramObject.append(paramInt2);
      paramObject.append(" follows ");
      paramObject.append(charAt(paramInt2 - 1));
      paramObject.append(")");
      throw new RuntimeException(paramObject.toString());
    }
    if ((j == 2) && (k == 1) && (i == paramInt2))
    {
      if (paramBoolean1) {
        Log.e("SpannableStringBuilder", "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length");
      }
      return;
    }
    if (i > mGapStart) {
      paramInt1 = i + mGapLength;
    }
    int m;
    for (;;)
    {
      m = paramInt1;
      break;
      paramInt1 = i;
      if (i == mGapStart) {
        if (j != 2)
        {
          paramInt1 = i;
          if (j == 3)
          {
            paramInt1 = i;
            if (i != length()) {}
          }
        }
        else
        {
          paramInt1 = i + mGapLength;
        }
      }
    }
    if (paramInt2 > mGapStart) {
      paramInt1 = mGapLength + paramInt2;
    }
    for (;;)
    {
      break;
      if ((paramInt2 == mGapStart) && ((k == 2) || ((k == 3) && (paramInt2 == length())))) {
        paramInt1 = mGapLength + paramInt2;
      } else {
        paramInt1 = paramInt2;
      }
    }
    if (mIndexOfSpan != null)
    {
      Integer localInteger = (Integer)mIndexOfSpan.get(paramObject);
      if (localInteger != null)
      {
        int n = localInteger.intValue();
        j = mSpanStarts[n];
        int i1 = mSpanEnds[n];
        k = j;
        if (j > mGapStart) {
          k = j - mGapLength;
        }
        j = i1;
        if (i1 > mGapStart) {
          j = i1 - mGapLength;
        }
        mSpanStarts[n] = m;
        mSpanEnds[n] = paramInt1;
        mSpanFlags[n] = paramInt3;
        if (paramBoolean1)
        {
          restoreInvariants();
          sendSpanChanged(paramObject, k, j, i, paramInt2);
        }
        return;
      }
    }
    mSpans = GrowingArrayUtils.append(mSpans, mSpanCount, paramObject);
    mSpanStarts = GrowingArrayUtils.append(mSpanStarts, mSpanCount, m);
    mSpanEnds = GrowingArrayUtils.append(mSpanEnds, mSpanCount, paramInt1);
    mSpanFlags = GrowingArrayUtils.append(mSpanFlags, mSpanCount, paramInt3);
    mSpanOrder = GrowingArrayUtils.append(mSpanOrder, mSpanCount, mSpanInsertCount);
    invalidateIndex(mSpanCount);
    mSpanCount += 1;
    mSpanInsertCount += 1;
    paramInt1 = 2 * treeRoot() + 1;
    if (mSpanMax.length < paramInt1) {
      mSpanMax = new int[paramInt1];
    }
    if (paramBoolean1)
    {
      restoreInvariants();
      sendSpanAdded(paramObject, i, paramInt2);
    }
  }
  
  private final <T> void siftDown(int paramInt1, T[] paramArrayOfT, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = 2 * paramInt1 + 1;
    for (int j = paramInt1; i < paramInt2; j = paramInt1)
    {
      paramInt1 = i;
      if (i < paramInt2 - 1)
      {
        paramInt1 = i;
        if (compareSpans(i, i + 1, paramArrayOfInt1, paramArrayOfInt2) < 0) {
          paramInt1 = i + 1;
        }
      }
      if (compareSpans(j, paramInt1, paramArrayOfInt1, paramArrayOfInt2) >= 0) {
        break;
      }
      T ? = paramArrayOfT[j];
      paramArrayOfT[j] = paramArrayOfT[paramInt1];
      paramArrayOfT[paramInt1] = ?;
      i = paramArrayOfInt1[j];
      paramArrayOfInt1[j] = paramArrayOfInt1[paramInt1];
      paramArrayOfInt1[paramInt1] = i;
      i = paramArrayOfInt2[j];
      paramArrayOfInt2[j] = paramArrayOfInt2[paramInt1];
      paramArrayOfInt2[paramInt1] = i;
      i = 2 * paramInt1 + 1;
    }
  }
  
  private final <T> void sort(T[] paramArrayOfT, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = paramArrayOfT.length;
    for (int j = i / 2 - 1; j >= 0; j--) {
      siftDown(j, paramArrayOfT, i, paramArrayOfInt1, paramArrayOfInt2);
    }
    for (j = i - 1; j > 0; j--)
    {
      T ? = paramArrayOfT[0];
      paramArrayOfT[0] = paramArrayOfT[j];
      paramArrayOfT[j] = ?;
      i = paramArrayOfInt1[0];
      paramArrayOfInt1[0] = paramArrayOfInt1[j];
      paramArrayOfInt1[j] = i;
      i = paramArrayOfInt2[0];
      paramArrayOfInt2[0] = paramArrayOfInt2[j];
      paramArrayOfInt2[j] = i;
      siftDown(0, paramArrayOfT, j, paramArrayOfInt1, paramArrayOfInt2);
    }
  }
  
  private int treeRoot()
  {
    return Integer.highestOneBit(mSpanCount) - 1;
  }
  
  private int updatedIntervalBound(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramInt1 >= paramInt2) && (paramInt1 < mGapStart + mGapLength)) {
      if (paramInt4 == 2)
      {
        if ((paramBoolean2) || (paramInt1 > paramInt2)) {
          return mGapStart + mGapLength;
        }
      }
      else if (paramInt4 == 3)
      {
        if (paramBoolean1) {
          return mGapStart + mGapLength;
        }
      }
      else
      {
        if ((!paramBoolean2) && (paramInt1 >= mGapStart - paramInt3)) {
          return mGapStart;
        }
        return paramInt2;
      }
    }
    return paramInt1;
  }
  
  public static SpannableStringBuilder valueOf(CharSequence paramCharSequence)
  {
    if ((paramCharSequence instanceof SpannableStringBuilder)) {
      return (SpannableStringBuilder)paramCharSequence;
    }
    return new SpannableStringBuilder(paramCharSequence);
  }
  
  public SpannableStringBuilder append(char paramChar)
  {
    return append(String.valueOf(paramChar));
  }
  
  public SpannableStringBuilder append(CharSequence paramCharSequence)
  {
    int i = length();
    return replace(i, i, paramCharSequence, 0, paramCharSequence.length());
  }
  
  public SpannableStringBuilder append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    int i = length();
    return replace(i, i, paramCharSequence, paramInt1, paramInt2);
  }
  
  public SpannableStringBuilder append(CharSequence paramCharSequence, Object paramObject, int paramInt)
  {
    int i = length();
    append(paramCharSequence);
    setSpan(paramObject, i, length(), paramInt);
    return this;
  }
  
  public char charAt(int paramInt)
  {
    int i = length();
    if (paramInt >= 0)
    {
      if (paramInt < i)
      {
        if (paramInt >= mGapStart) {
          return mText[(mGapLength + paramInt)];
        }
        return mText[paramInt];
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("charAt: ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" >= length ");
      localStringBuilder.append(i);
      throw new IndexOutOfBoundsException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("charAt: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" < 0");
    throw new IndexOutOfBoundsException(localStringBuilder.toString());
  }
  
  public void clear()
  {
    replace(0, length(), "", 0, 0);
    mSpanInsertCount = 0;
  }
  
  public void clearSpans()
  {
    for (int i = mSpanCount - 1; i >= 0; i--)
    {
      Object localObject = mSpans[i];
      int j = mSpanStarts[i];
      int k = mSpanEnds[i];
      int m = j;
      if (j > mGapStart) {
        m = j - mGapLength;
      }
      j = k;
      if (k > mGapStart) {
        j = k - mGapLength;
      }
      mSpanCount = i;
      mSpans[i] = null;
      sendSpanRemoved(localObject, m, j);
    }
    if (mIndexOfSpan != null) {
      mIndexOfSpan.clear();
    }
    mSpanInsertCount = 0;
  }
  
  public SpannableStringBuilder delete(int paramInt1, int paramInt2)
  {
    SpannableStringBuilder localSpannableStringBuilder = replace(paramInt1, paramInt2, "", 0, 0);
    if (mGapLength > 2 * length()) {
      resizeFor(length());
    }
    return localSpannableStringBuilder;
  }
  
  public void drawText(BaseCanvas paramBaseCanvas, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    checkRange("drawText", paramInt1, paramInt2);
    if (paramInt2 <= mGapStart)
    {
      paramBaseCanvas.drawText(mText, paramInt1, paramInt2 - paramInt1, paramFloat1, paramFloat2, paramPaint);
    }
    else if (paramInt1 >= mGapStart)
    {
      paramBaseCanvas.drawText(mText, paramInt1 + mGapLength, paramInt2 - paramInt1, paramFloat1, paramFloat2, paramPaint);
    }
    else
    {
      char[] arrayOfChar = TextUtils.obtain(paramInt2 - paramInt1);
      getChars(paramInt1, paramInt2, arrayOfChar, 0);
      paramBaseCanvas.drawText(arrayOfChar, 0, paramInt2 - paramInt1, paramFloat1, paramFloat2, paramPaint);
      TextUtils.recycle(arrayOfChar);
    }
  }
  
  public void drawTextRun(BaseCanvas paramBaseCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, boolean paramBoolean, Paint paramPaint)
  {
    checkRange("drawTextRun", paramInt1, paramInt2);
    int i = paramInt4 - paramInt3;
    paramInt2 -= paramInt1;
    if (paramInt4 <= mGapStart)
    {
      paramBaseCanvas.drawTextRun(mText, paramInt1, paramInt2, paramInt3, i, paramFloat1, paramFloat2, paramBoolean, paramPaint);
    }
    else if (paramInt3 >= mGapStart)
    {
      paramBaseCanvas.drawTextRun(mText, paramInt1 + mGapLength, paramInt2, paramInt3 + mGapLength, i, paramFloat1, paramFloat2, paramBoolean, paramPaint);
    }
    else
    {
      char[] arrayOfChar = TextUtils.obtain(i);
      getChars(paramInt3, paramInt4, arrayOfChar, 0);
      paramBaseCanvas.drawTextRun(arrayOfChar, paramInt1 - paramInt3, paramInt2, 0, i, paramFloat1, paramFloat2, paramBoolean, paramPaint);
      TextUtils.recycle(arrayOfChar);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (((paramObject instanceof Spanned)) && (toString().equals(paramObject.toString())))
    {
      Spanned localSpanned = (Spanned)paramObject;
      Object[] arrayOfObject = localSpanned.getSpans(0, localSpanned.length(), Object.class);
      if (mSpanCount == arrayOfObject.length)
      {
        int i = 0;
        while (i < mSpanCount)
        {
          Object localObject = mSpans[i];
          paramObject = arrayOfObject[i];
          if (localObject == this)
          {
            if ((localSpanned != paramObject) || (getSpanStart(localObject) != localSpanned.getSpanStart(paramObject)) || (getSpanEnd(localObject) != localSpanned.getSpanEnd(paramObject)) || (getSpanFlags(localObject) != localSpanned.getSpanFlags(paramObject))) {
              return false;
            }
          }
          else {
            if ((!localObject.equals(paramObject)) || (getSpanStart(localObject) != localSpanned.getSpanStart(paramObject)) || (getSpanEnd(localObject) != localSpanned.getSpanEnd(paramObject)) || (getSpanFlags(localObject) != localSpanned.getSpanFlags(paramObject))) {
              break label204;
            }
          }
          i++;
          continue;
          label204:
          return false;
        }
        return true;
      }
    }
    return false;
  }
  
  public void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
  {
    checkRange("getChars", paramInt1, paramInt2);
    if (paramInt2 <= mGapStart)
    {
      System.arraycopy(mText, paramInt1, paramArrayOfChar, paramInt3, paramInt2 - paramInt1);
    }
    else if (paramInt1 >= mGapStart)
    {
      System.arraycopy(mText, mGapLength + paramInt1, paramArrayOfChar, paramInt3, paramInt2 - paramInt1);
    }
    else
    {
      System.arraycopy(mText, paramInt1, paramArrayOfChar, paramInt3, mGapStart - paramInt1);
      System.arraycopy(mText, mGapStart + mGapLength, paramArrayOfChar, mGapStart - paramInt1 + paramInt3, paramInt2 - mGapStart);
    }
  }
  
  public InputFilter[] getFilters()
  {
    return mFilters;
  }
  
  public int getSpanEnd(Object paramObject)
  {
    IdentityHashMap localIdentityHashMap = mIndexOfSpan;
    int i = -1;
    if (localIdentityHashMap == null) {
      return -1;
    }
    paramObject = (Integer)mIndexOfSpan.get(paramObject);
    if (paramObject != null) {
      i = resolveGap(mSpanEnds[paramObject.intValue()]);
    }
    return i;
  }
  
  public int getSpanFlags(Object paramObject)
  {
    IdentityHashMap localIdentityHashMap = mIndexOfSpan;
    int i = 0;
    if (localIdentityHashMap == null) {
      return 0;
    }
    paramObject = (Integer)mIndexOfSpan.get(paramObject);
    if (paramObject != null) {
      i = mSpanFlags[paramObject.intValue()];
    }
    return i;
  }
  
  public int getSpanStart(Object paramObject)
  {
    IdentityHashMap localIdentityHashMap = mIndexOfSpan;
    int i = -1;
    if (localIdentityHashMap == null) {
      return -1;
    }
    paramObject = (Integer)mIndexOfSpan.get(paramObject);
    if (paramObject != null) {
      i = resolveGap(mSpanStarts[paramObject.intValue()]);
    }
    return i;
  }
  
  public <T> T[] getSpans(int paramInt1, int paramInt2, Class<T> paramClass)
  {
    return getSpans(paramInt1, paramInt2, paramClass, true);
  }
  
  public <T> T[] getSpans(int paramInt1, int paramInt2, Class<T> paramClass, boolean paramBoolean)
  {
    if (paramClass == null) {
      return ArrayUtils.emptyArray(Object.class);
    }
    if (mSpanCount == 0) {
      return ArrayUtils.emptyArray(paramClass);
    }
    int i = countSpans(paramInt1, paramInt2, paramClass, treeRoot());
    if (i == 0) {
      return ArrayUtils.emptyArray(paramClass);
    }
    Object[] arrayOfObject = (Object[])Array.newInstance(paramClass, i);
    int[] arrayOfInt1;
    if (paramBoolean) {
      arrayOfInt1 = obtain(i);
    } else {
      arrayOfInt1 = EmptyArray.INT;
    }
    int[] arrayOfInt2;
    if (paramBoolean) {
      arrayOfInt2 = obtain(i);
    } else {
      arrayOfInt2 = EmptyArray.INT;
    }
    getSpansRec(paramInt1, paramInt2, paramClass, treeRoot(), arrayOfObject, arrayOfInt1, arrayOfInt2, 0, paramBoolean);
    if (paramBoolean)
    {
      sort(arrayOfObject, arrayOfInt1, arrayOfInt2);
      recycle(arrayOfInt1);
      recycle(arrayOfInt2);
    }
    return arrayOfObject;
  }
  
  public float getTextRunAdvances(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, float[] paramArrayOfFloat, int paramInt5, Paint paramPaint)
  {
    int i = paramInt4 - paramInt3;
    int j = paramInt2 - paramInt1;
    float f;
    if (paramInt2 <= mGapStart) {
      f = paramPaint.getTextRunAdvances(mText, paramInt1, j, paramInt3, i, paramBoolean, paramArrayOfFloat, paramInt5);
    }
    for (;;)
    {
      break;
      if (paramInt1 >= mGapStart)
      {
        f = paramPaint.getTextRunAdvances(mText, paramInt1 + mGapLength, j, paramInt3 + mGapLength, i, paramBoolean, paramArrayOfFloat, paramInt5);
      }
      else
      {
        char[] arrayOfChar = TextUtils.obtain(i);
        getChars(paramInt3, paramInt4, arrayOfChar, 0);
        f = paramPaint.getTextRunAdvances(arrayOfChar, paramInt1 - paramInt3, j, 0, i, paramBoolean, paramArrayOfFloat, paramInt5);
        TextUtils.recycle(arrayOfChar);
      }
    }
    return f;
  }
  
  @Deprecated
  public int getTextRunCursor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint)
  {
    int i = paramInt2 - paramInt1;
    if (paramInt2 <= mGapStart) {
      paramInt1 = paramPaint.getTextRunCursor(mText, paramInt1, i, paramInt3, paramInt4, paramInt5);
    }
    for (;;)
    {
      break;
      if (paramInt1 >= mGapStart)
      {
        paramInt1 = paramPaint.getTextRunCursor(mText, paramInt1 + mGapLength, i, paramInt3, paramInt4 + mGapLength, paramInt5) - mGapLength;
      }
      else
      {
        char[] arrayOfChar = TextUtils.obtain(i);
        getChars(paramInt1, paramInt2, arrayOfChar, 0);
        paramInt1 = paramPaint.getTextRunCursor(arrayOfChar, 0, i, paramInt3, paramInt4 - paramInt1, paramInt5) + paramInt1;
        TextUtils.recycle(arrayOfChar);
      }
    }
    return paramInt1;
  }
  
  public int getTextWatcherDepth()
  {
    return mTextWatcherDepth;
  }
  
  public int getTextWidths(int paramInt1, int paramInt2, float[] paramArrayOfFloat, Paint paramPaint)
  {
    checkRange("getTextWidths", paramInt1, paramInt2);
    if (paramInt2 <= mGapStart) {
      paramInt1 = paramPaint.getTextWidths(mText, paramInt1, paramInt2 - paramInt1, paramArrayOfFloat);
    }
    for (;;)
    {
      break;
      if (paramInt1 >= mGapStart)
      {
        paramInt1 = paramPaint.getTextWidths(mText, mGapLength + paramInt1, paramInt2 - paramInt1, paramArrayOfFloat);
      }
      else
      {
        char[] arrayOfChar = TextUtils.obtain(paramInt2 - paramInt1);
        getChars(paramInt1, paramInt2, arrayOfChar, 0);
        paramInt1 = paramPaint.getTextWidths(arrayOfChar, 0, paramInt2 - paramInt1, paramArrayOfFloat);
        TextUtils.recycle(arrayOfChar);
      }
    }
    return paramInt1;
  }
  
  public int hashCode()
  {
    int i = toString().hashCode() * 31 + mSpanCount;
    for (int j = 0; j < mSpanCount; j++)
    {
      Object localObject = mSpans[j];
      int k = i;
      if (localObject != this) {
        k = i * 31 + localObject.hashCode();
      }
      i = ((k * 31 + getSpanStart(localObject)) * 31 + getSpanEnd(localObject)) * 31 + getSpanFlags(localObject);
    }
    return i;
  }
  
  public SpannableStringBuilder insert(int paramInt, CharSequence paramCharSequence)
  {
    return replace(paramInt, paramInt, paramCharSequence, 0, paramCharSequence.length());
  }
  
  public SpannableStringBuilder insert(int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3)
  {
    return replace(paramInt1, paramInt1, paramCharSequence, paramInt2, paramInt3);
  }
  
  public int length()
  {
    return mText.length - mGapLength;
  }
  
  public float measureText(int paramInt1, int paramInt2, Paint paramPaint)
  {
    checkRange("measureText", paramInt1, paramInt2);
    float f;
    if (paramInt2 <= mGapStart) {
      f = paramPaint.measureText(mText, paramInt1, paramInt2 - paramInt1);
    }
    for (;;)
    {
      break;
      if (paramInt1 >= mGapStart)
      {
        f = paramPaint.measureText(mText, mGapLength + paramInt1, paramInt2 - paramInt1);
      }
      else
      {
        char[] arrayOfChar = TextUtils.obtain(paramInt2 - paramInt1);
        getChars(paramInt1, paramInt2, arrayOfChar, 0);
        f = paramPaint.measureText(arrayOfChar, 0, paramInt2 - paramInt1);
        TextUtils.recycle(arrayOfChar);
      }
    }
    return f;
  }
  
  public int nextSpanTransition(int paramInt1, int paramInt2, Class paramClass)
  {
    if (mSpanCount == 0) {
      return paramInt2;
    }
    Object localObject = paramClass;
    if (paramClass == null) {
      localObject = Object.class;
    }
    return nextSpanTransitionRec(paramInt1, paramInt2, (Class)localObject, treeRoot());
  }
  
  public void removeSpan(Object paramObject)
  {
    removeSpan(paramObject, 0);
  }
  
  public void removeSpan(Object paramObject, int paramInt)
  {
    if (mIndexOfSpan == null) {
      return;
    }
    paramObject = (Integer)mIndexOfSpan.remove(paramObject);
    if (paramObject != null) {
      removeSpan(paramObject.intValue(), paramInt);
    }
  }
  
  public SpannableStringBuilder replace(int paramInt1, int paramInt2, CharSequence paramCharSequence)
  {
    return replace(paramInt1, paramInt2, paramCharSequence, 0, paramCharSequence.length());
  }
  
  public SpannableStringBuilder replace(int paramInt1, int paramInt2, CharSequence paramCharSequence, int paramInt3, int paramInt4)
  {
    checkRange("replace", paramInt1, paramInt2);
    int i = mFilters.length;
    int j = 0;
    int k = 0;
    int m = paramInt3;
    for (paramInt3 = k; paramInt3 < i; paramInt3++)
    {
      localObject = mFilters[paramInt3].filter(paramCharSequence, m, paramInt4, this, paramInt1, paramInt2);
      if (localObject != null)
      {
        paramInt4 = ((CharSequence)localObject).length();
        paramCharSequence = (CharSequence)localObject;
        m = 0;
      }
    }
    int n = paramInt2 - paramInt1;
    i = paramInt4 - m;
    if ((n == 0) && (i == 0) && (!hasNonExclusiveExclusiveSpanAt(paramCharSequence, m))) {
      return this;
    }
    Object localObject = (TextWatcher[])getSpans(paramInt1, paramInt1 + n, TextWatcher.class);
    sendBeforeTextChanged((TextWatcher[])localObject, paramInt1, n, i);
    k = j;
    if (n != 0)
    {
      k = j;
      if (i != 0) {
        k = 1;
      }
    }
    j = 0;
    paramInt3 = 0;
    if (k != 0)
    {
      j = Selection.getSelectionStart(this);
      paramInt3 = Selection.getSelectionEnd(this);
    }
    change(paramInt1, paramInt2, paramCharSequence, m, paramInt4);
    if (k != 0)
    {
      paramInt4 = 0;
      long l;
      if ((j > paramInt1) && (j < paramInt2))
      {
        l = j - paramInt1;
        paramInt4 = paramInt1 + Math.toIntExact(i * l / n);
        setSpan(false, Selection.SELECTION_START, paramInt4, paramInt4, 34, true);
        paramInt4 = 1;
      }
      if ((paramInt3 > paramInt1) && (paramInt3 < paramInt2))
      {
        l = paramInt3 - paramInt1;
        paramInt4 = paramInt1 + Math.toIntExact(i * l / n);
        paramCharSequence = Selection.SELECTION_END;
        paramInt3 = paramInt4;
        setSpan(false, paramCharSequence, paramInt3, paramInt4, 34, true);
        paramInt4 = 1;
      }
      if (paramInt4 != 0) {
        restoreInvariants();
      }
    }
    sendTextChanged((TextWatcher[])localObject, paramInt1, n, i);
    sendAfterTextChanged((TextWatcher[])localObject);
    sendToSpanWatchers(paramInt1, paramInt2, i - n);
    return this;
  }
  
  public void setFilters(InputFilter[] paramArrayOfInputFilter)
  {
    if (paramArrayOfInputFilter != null)
    {
      mFilters = paramArrayOfInputFilter;
      return;
    }
    throw new IllegalArgumentException();
  }
  
  public void setSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
  {
    setSpan(true, paramObject, paramInt1, paramInt2, paramInt3, true);
  }
  
  public CharSequence subSequence(int paramInt1, int paramInt2)
  {
    return new SpannableStringBuilder(this, paramInt1, paramInt2);
  }
  
  public String substring(int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2 - paramInt1];
    getChars(paramInt1, paramInt2, arrayOfChar, 0);
    return new String(arrayOfChar);
  }
  
  public String toString()
  {
    int i = length();
    char[] arrayOfChar = new char[i];
    getChars(0, i, arrayOfChar, 0);
    return new String(arrayOfChar);
  }
}
