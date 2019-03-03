package android.app.usage;

import android.util.LongSparseArray;
import android.util.Slog;

public class TimeSparseArray<E>
  extends LongSparseArray<E>
{
  private static final String TAG = TimeSparseArray.class.getSimpleName();
  private boolean mWtfReported;
  
  public TimeSparseArray() {}
  
  public int closestIndexOnOrAfter(long paramLong)
  {
    int i = size();
    int j = 0;
    int k = i - 1;
    int m = -1;
    long l = -1L;
    while (j <= k)
    {
      m = j + (k - j) / 2;
      l = keyAt(m);
      if (paramLong > l) {
        j = m + 1;
      } else if (paramLong < l) {
        k = m - 1;
      } else {
        return m;
      }
    }
    if (paramLong < l) {
      return m;
    }
    if ((paramLong > l) && (j < i)) {
      return j;
    }
    return -1;
  }
  
  public int closestIndexOnOrBefore(long paramLong)
  {
    int i = closestIndexOnOrAfter(paramLong);
    if (i < 0) {
      return size() - 1;
    }
    if (keyAt(i) == paramLong) {
      return i;
    }
    return i - 1;
  }
  
  public void put(long paramLong, E paramE)
  {
    if ((indexOfKey(paramLong) >= 0) && (!mWtfReported))
    {
      String str = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Overwriting value ");
      localStringBuilder.append(get(paramLong));
      localStringBuilder.append(" by ");
      localStringBuilder.append(paramE);
      Slog.wtf(str, localStringBuilder.toString());
      mWtfReported = true;
    }
    super.put(paramLong, paramE);
  }
}
