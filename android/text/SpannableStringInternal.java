package android.text;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.reflect.Array;
import libcore.util.EmptyArray;

abstract class SpannableStringInternal
{
  private static final int COLUMNS = 3;
  static final Object[] EMPTY = new Object[0];
  private static final int END = 1;
  private static final int FLAGS = 2;
  private static final int START = 0;
  private int mSpanCount;
  private int[] mSpanData;
  private Object[] mSpans;
  private String mText;
  
  SpannableStringInternal(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    this(paramCharSequence, paramInt1, paramInt2, false);
  }
  
  SpannableStringInternal(CharSequence paramCharSequence, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramInt1 == 0) && (paramInt2 == paramCharSequence.length())) {
      mText = paramCharSequence.toString();
    } else {
      mText = paramCharSequence.toString().substring(paramInt1, paramInt2);
    }
    mSpans = EmptyArray.OBJECT;
    mSpanData = EmptyArray.INT;
    if ((paramCharSequence instanceof Spanned)) {
      if ((paramCharSequence instanceof SpannableStringInternal)) {
        copySpans((SpannableStringInternal)paramCharSequence, paramInt1, paramInt2, paramBoolean);
      } else {
        copySpans((Spanned)paramCharSequence, paramInt1, paramInt2, paramBoolean);
      }
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
  
  private void copySpans(SpannableStringInternal paramSpannableStringInternal, int paramInt1, int paramInt2)
  {
    copySpans(paramSpannableStringInternal, paramInt1, paramInt2, false);
  }
  
  private void copySpans(SpannableStringInternal paramSpannableStringInternal, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int[] arrayOfInt = mSpanData;
    Object[] arrayOfObject = mSpans;
    int i = mSpanCount;
    int j = 0;
    int k = 0;
    int n;
    for (int m = 0; m < i; m++) {
      if (!isOutOfCopyRange(paramInt1, paramInt2, arrayOfInt[(m * 3 + 0)], arrayOfInt[(m * 3 + 1)]))
      {
        if ((arrayOfObject[m] instanceof NoCopySpan))
        {
          n = 1;
          j = 1;
          if (paramBoolean)
          {
            j = n;
            continue;
          }
        }
        k++;
      }
    }
    if (k == 0) {
      return;
    }
    if ((j == 0) && (paramInt1 == 0) && (paramInt2 == paramSpannableStringInternal.length()))
    {
      mSpans = ArrayUtils.newUnpaddedObjectArray(mSpans.length);
      mSpanData = new int[mSpanData.length];
      mSpanCount = mSpanCount;
      System.arraycopy(mSpans, 0, mSpans, 0, mSpans.length);
      System.arraycopy(mSpanData, 0, mSpanData, 0, mSpanData.length);
    }
    else
    {
      mSpanCount = k;
      mSpans = ArrayUtils.newUnpaddedObjectArray(mSpanCount);
      mSpanData = new int[mSpans.length * 3];
      j = 0;
      for (m = 0; j < i; m = k)
      {
        n = arrayOfInt[(j * 3 + 0)];
        int i1 = arrayOfInt[(j * 3 + 1)];
        k = m;
        if (!isOutOfCopyRange(paramInt1, paramInt2, n, i1)) {
          if ((paramBoolean) && ((arrayOfObject[j] instanceof NoCopySpan)))
          {
            k = m;
          }
          else
          {
            k = n;
            if (n < paramInt1) {
              k = paramInt1;
            }
            n = i1;
            if (i1 > paramInt2) {
              n = paramInt2;
            }
            mSpans[m] = arrayOfObject[j];
            mSpanData[(m * 3 + 0)] = (k - paramInt1);
            mSpanData[(m * 3 + 1)] = (n - paramInt1);
            mSpanData[(m * 3 + 2)] = arrayOfInt[(j * 3 + 2)];
            k = m + 1;
          }
        }
        j++;
      }
    }
  }
  
  private void copySpans(Spanned paramSpanned, int paramInt1, int paramInt2)
  {
    copySpans(paramSpanned, paramInt1, paramInt2, false);
  }
  
  private void copySpans(Spanned paramSpanned, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Object[] arrayOfObject = paramSpanned.getSpans(paramInt1, paramInt2, Object.class);
    for (int i = 0; i < arrayOfObject.length; i++) {
      if ((!paramBoolean) || (!(arrayOfObject[i] instanceof NoCopySpan)))
      {
        int j = paramSpanned.getSpanStart(arrayOfObject[i]);
        int k = paramSpanned.getSpanEnd(arrayOfObject[i]);
        int m = paramSpanned.getSpanFlags(arrayOfObject[i]);
        int n = j;
        if (j < paramInt1) {
          n = paramInt1;
        }
        j = k;
        if (k > paramInt2) {
          j = paramInt2;
        }
        setSpan(arrayOfObject[i], n - paramInt1, j - paramInt1, m, false);
      }
    }
  }
  
  private boolean isIndexFollowsNextLine(int paramInt)
  {
    boolean bool;
    if ((paramInt != 0) && (paramInt != length()) && (charAt(paramInt - 1) != '\n')) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private final boolean isOutOfCopyRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt3 <= paramInt2) && (paramInt4 >= paramInt1)) {
      return (paramInt3 != paramInt4) && (paramInt1 != paramInt2) && ((paramInt3 == paramInt2) || (paramInt4 == paramInt1));
    }
    return true;
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
  
  private void sendSpanAdded(Object paramObject, int paramInt1, int paramInt2)
  {
    SpanWatcher[] arrayOfSpanWatcher = (SpanWatcher[])getSpans(paramInt1, paramInt2, SpanWatcher.class);
    int i = arrayOfSpanWatcher.length;
    for (int j = 0; j < i; j++) {
      arrayOfSpanWatcher[j].onSpanAdded((Spannable)this, paramObject, paramInt1, paramInt2);
    }
  }
  
  private void sendSpanChanged(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    SpanWatcher[] arrayOfSpanWatcher = (SpanWatcher[])getSpans(Math.min(paramInt1, paramInt3), Math.max(paramInt2, paramInt4), SpanWatcher.class);
    int i = arrayOfSpanWatcher.length;
    for (int j = 0; j < i; j++) {
      arrayOfSpanWatcher[j].onSpanChanged((Spannable)this, paramObject, paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  private void sendSpanRemoved(Object paramObject, int paramInt1, int paramInt2)
  {
    SpanWatcher[] arrayOfSpanWatcher = (SpanWatcher[])getSpans(paramInt1, paramInt2, SpanWatcher.class);
    int i = arrayOfSpanWatcher.length;
    for (int j = 0; j < i; j++) {
      arrayOfSpanWatcher[j].onSpanRemoved((Spannable)this, paramObject, paramInt1, paramInt2);
    }
  }
  
  private void setSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    checkRange("setSpan", paramInt1, paramInt2);
    if ((paramInt3 & 0x33) == 51)
    {
      if (isIndexFollowsNextLine(paramInt1))
      {
        if (!paramBoolean) {
          return;
        }
        paramObject = new StringBuilder();
        paramObject.append("PARAGRAPH span must start at paragraph boundary (");
        paramObject.append(paramInt1);
        paramObject.append(" follows ");
        paramObject.append(charAt(paramInt1 - 1));
        paramObject.append(")");
        throw new RuntimeException(paramObject.toString());
      }
      if (isIndexFollowsNextLine(paramInt2))
      {
        if (!paramBoolean) {
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
    }
    int i = mSpanCount;
    Object[] arrayOfObject = mSpans;
    int[] arrayOfInt = mSpanData;
    for (int j = 0; j < i; j++) {
      if (arrayOfObject[j] == paramObject)
      {
        int k = arrayOfInt[(j * 3 + 0)];
        i = arrayOfInt[(j * 3 + 1)];
        arrayOfInt[(j * 3 + 0)] = paramInt1;
        arrayOfInt[(j * 3 + 1)] = paramInt2;
        arrayOfInt[(j * 3 + 2)] = paramInt3;
        sendSpanChanged(paramObject, k, i, paramInt1, paramInt2);
        return;
      }
    }
    if (mSpanCount + 1 >= mSpans.length)
    {
      arrayOfObject = ArrayUtils.newUnpaddedObjectArray(GrowingArrayUtils.growSize(mSpanCount));
      arrayOfInt = new int[arrayOfObject.length * 3];
      System.arraycopy(mSpans, 0, arrayOfObject, 0, mSpanCount);
      System.arraycopy(mSpanData, 0, arrayOfInt, 0, mSpanCount * 3);
      mSpans = arrayOfObject;
      mSpanData = arrayOfInt;
    }
    mSpans[mSpanCount] = paramObject;
    mSpanData[(mSpanCount * 3 + 0)] = paramInt1;
    mSpanData[(mSpanCount * 3 + 1)] = paramInt2;
    mSpanData[(mSpanCount * 3 + 2)] = paramInt3;
    mSpanCount += 1;
    if ((this instanceof Spannable)) {
      sendSpanAdded(paramObject, paramInt1, paramInt2);
    }
  }
  
  public final char charAt(int paramInt)
  {
    return mText.charAt(paramInt);
  }
  
  public boolean equals(Object paramObject)
  {
    if (((paramObject instanceof Spanned)) && (toString().equals(paramObject.toString())))
    {
      Spanned localSpanned = (Spanned)paramObject;
      paramObject = localSpanned.getSpans(0, localSpanned.length(), Object.class);
      if (mSpanCount == paramObject.length)
      {
        int i = 0;
        while (i < mSpanCount)
        {
          Object localObject1 = mSpans[i];
          Object localObject2 = paramObject[i];
          if (localObject1 == this)
          {
            if ((localSpanned != localObject2) || (getSpanStart(localObject1) != localSpanned.getSpanStart(localObject2)) || (getSpanEnd(localObject1) != localSpanned.getSpanEnd(localObject2)) || (getSpanFlags(localObject1) != localSpanned.getSpanFlags(localObject2))) {
              return false;
            }
          }
          else {
            if ((!localObject1.equals(localObject2)) || (getSpanStart(localObject1) != localSpanned.getSpanStart(localObject2)) || (getSpanEnd(localObject1) != localSpanned.getSpanEnd(localObject2)) || (getSpanFlags(localObject1) != localSpanned.getSpanFlags(localObject2))) {
              break label209;
            }
          }
          i++;
          continue;
          label209:
          return false;
        }
        return true;
      }
    }
    return false;
  }
  
  public final void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
  {
    mText.getChars(paramInt1, paramInt2, paramArrayOfChar, paramInt3);
  }
  
  public int getSpanEnd(Object paramObject)
  {
    int i = mSpanCount;
    Object[] arrayOfObject = mSpans;
    int[] arrayOfInt = mSpanData;
    i--;
    while (i >= 0)
    {
      if (arrayOfObject[i] == paramObject) {
        return arrayOfInt[(i * 3 + 1)];
      }
      i--;
    }
    return -1;
  }
  
  public int getSpanFlags(Object paramObject)
  {
    int i = mSpanCount;
    Object[] arrayOfObject = mSpans;
    int[] arrayOfInt = mSpanData;
    i--;
    while (i >= 0)
    {
      if (arrayOfObject[i] == paramObject) {
        return arrayOfInt[(i * 3 + 2)];
      }
      i--;
    }
    return 0;
  }
  
  public int getSpanStart(Object paramObject)
  {
    int i = mSpanCount;
    Object[] arrayOfObject = mSpans;
    int[] arrayOfInt = mSpanData;
    i--;
    while (i >= 0)
    {
      if (arrayOfObject[i] == paramObject) {
        return arrayOfInt[(i * 3 + 0)];
      }
      i--;
    }
    return -1;
  }
  
  public <T> T[] getSpans(int paramInt1, int paramInt2, Class<T> paramClass)
  {
    int i = mSpanCount;
    Object[] arrayOfObject1 = mSpans;
    int[] arrayOfInt = mSpanData;
    Object localObject = null;
    Object[] arrayOfObject2 = null;
    int j = 0;
    for (int k = 0;; k++)
    {
      int m = paramInt1;
      if (k >= i) {
        break;
      }
      int n = arrayOfInt[(k * 3 + 0)];
      int i1 = arrayOfInt[(k * 3 + 1)];
      if ((n <= paramInt2) && (i1 >= m) && ((n == i1) || (m == paramInt2) || ((n != paramInt2) && (i1 != m))) && ((paramClass == null) || (paramClass == Object.class) || (paramClass.isInstance(arrayOfObject1[k])))) {
        if (j == 0)
        {
          localObject = arrayOfObject1[k];
          j++;
        }
        else
        {
          if (j == 1)
          {
            arrayOfObject2 = (Object[])Array.newInstance(paramClass, i - k + 1);
            arrayOfObject2[0] = localObject;
          }
          i1 = arrayOfInt[(k * 3 + 2)] & 0xFF0000;
          if (i1 != 0)
          {
            for (m = 0; (m < j) && (i1 <= (getSpanFlags(arrayOfObject2[m]) & 0xFF0000)); m++) {}
            System.arraycopy(arrayOfObject2, m, arrayOfObject2, m + 1, j - m);
            arrayOfObject2[m] = arrayOfObject1[k];
            j++;
          }
          else
          {
            arrayOfObject2[j] = arrayOfObject1[k];
            j++;
          }
        }
      }
    }
    if (j == 0) {
      return ArrayUtils.emptyArray(paramClass);
    }
    if (j == 1)
    {
      paramClass = (Object[])Array.newInstance(paramClass, 1);
      paramClass[0] = localObject;
      return paramClass;
    }
    if (j == arrayOfObject2.length) {
      return arrayOfObject2;
    }
    paramClass = (Object[])Array.newInstance(paramClass, j);
    System.arraycopy(arrayOfObject2, 0, paramClass, 0, j);
    return paramClass;
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
  
  public final int length()
  {
    return mText.length();
  }
  
  public int nextSpanTransition(int paramInt1, int paramInt2, Class paramClass)
  {
    int i = mSpanCount;
    Object[] arrayOfObject = mSpans;
    int[] arrayOfInt = mSpanData;
    Object localObject = paramClass;
    if (paramClass == null) {
      localObject = Object.class;
    }
    for (int j = 0; j < i; j++)
    {
      int k = arrayOfInt[(j * 3 + 0)];
      int m = arrayOfInt[(j * 3 + 1)];
      int n = paramInt2;
      if (k > paramInt1)
      {
        n = paramInt2;
        if (k < paramInt2)
        {
          n = paramInt2;
          if (((Class)localObject).isInstance(arrayOfObject[j])) {
            n = k;
          }
        }
      }
      paramInt2 = n;
      if (m > paramInt1)
      {
        paramInt2 = n;
        if (m < n)
        {
          paramInt2 = n;
          if (((Class)localObject).isInstance(arrayOfObject[j])) {
            paramInt2 = m;
          }
        }
      }
    }
    return paramInt2;
  }
  
  void removeSpan(Object paramObject)
  {
    removeSpan(paramObject, 0);
  }
  
  public void removeSpan(Object paramObject, int paramInt)
  {
    int i = mSpanCount;
    Object[] arrayOfObject = mSpans;
    int[] arrayOfInt = mSpanData;
    for (int j = i - 1; j >= 0; j--) {
      if (arrayOfObject[j] == paramObject)
      {
        int k = arrayOfInt[(j * 3 + 0)];
        int m = arrayOfInt[(j * 3 + 1)];
        i -= j + 1;
        System.arraycopy(arrayOfObject, j + 1, arrayOfObject, j, i);
        System.arraycopy(arrayOfInt, (j + 1) * 3, arrayOfInt, j * 3, i * 3);
        mSpanCount -= 1;
        if ((paramInt & 0x200) == 0) {
          sendSpanRemoved(paramObject, k, m);
        }
        return;
      }
    }
  }
  
  void setSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
  {
    setSpan(paramObject, paramInt1, paramInt2, paramInt3, true);
  }
  
  public final String toString()
  {
    return mText;
  }
}
