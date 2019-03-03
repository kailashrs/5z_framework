package android.util;

import android.hardware.camera2.utils.HashCodeHelpers;
import com.android.internal.util.Preconditions;

public final class Range<T extends Comparable<? super T>>
{
  private final T mLower;
  private final T mUpper;
  
  public Range(T paramT1, T paramT2)
  {
    mLower = ((Comparable)Preconditions.checkNotNull(paramT1, "lower must not be null"));
    mUpper = ((Comparable)Preconditions.checkNotNull(paramT2, "upper must not be null"));
    if (paramT1.compareTo(paramT2) <= 0) {
      return;
    }
    throw new IllegalArgumentException("lower must be less than or equal to upper");
  }
  
  public static <T extends Comparable<? super T>> Range<T> create(T paramT1, T paramT2)
  {
    return new Range(paramT1, paramT2);
  }
  
  public T clamp(T paramT)
  {
    Preconditions.checkNotNull(paramT, "value must not be null");
    if (paramT.compareTo(mLower) < 0) {
      return mLower;
    }
    if (paramT.compareTo(mUpper) > 0) {
      return mUpper;
    }
    return paramT;
  }
  
  public boolean contains(Range<T> paramRange)
  {
    Preconditions.checkNotNull(paramRange, "value must not be null");
    int i = mLower.compareTo(mLower);
    boolean bool1 = false;
    if (i >= 0) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if (mUpper.compareTo(mUpper) <= 0) {
      j = 1;
    } else {
      j = 0;
    }
    boolean bool2 = bool1;
    if (i != 0)
    {
      bool2 = bool1;
      if (j != 0) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean contains(T paramT)
  {
    Preconditions.checkNotNull(paramT, "value must not be null");
    int i = paramT.compareTo(mLower);
    boolean bool1 = false;
    if (i >= 0) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if (paramT.compareTo(mUpper) <= 0) {
      j = 1;
    } else {
      j = 0;
    }
    boolean bool2 = bool1;
    if (i != 0)
    {
      bool2 = bool1;
      if (j != 0) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof Range))
    {
      paramObject = (Range)paramObject;
      boolean bool2 = bool1;
      if (mLower.equals(mLower))
      {
        bool2 = bool1;
        if (mUpper.equals(mUpper)) {
          bool2 = true;
        }
      }
      return bool2;
    }
    return false;
  }
  
  public Range<T> extend(Range<T> paramRange)
  {
    Preconditions.checkNotNull(paramRange, "range must not be null");
    int i = mLower.compareTo(mLower);
    int j = mUpper.compareTo(mUpper);
    if ((i <= 0) && (j >= 0)) {
      return paramRange;
    }
    if ((i >= 0) && (j <= 0)) {
      return this;
    }
    Comparable localComparable;
    if (i >= 0) {
      localComparable = mLower;
    } else {
      localComparable = mLower;
    }
    if (j <= 0) {
      paramRange = mUpper;
    } else {
      paramRange = mUpper;
    }
    return create(localComparable, paramRange);
  }
  
  public Range<T> extend(T paramT)
  {
    Preconditions.checkNotNull(paramT, "value must not be null");
    return extend(paramT, paramT);
  }
  
  public Range<T> extend(T paramT1, T paramT2)
  {
    Preconditions.checkNotNull(paramT1, "lower must not be null");
    Preconditions.checkNotNull(paramT2, "upper must not be null");
    int i = paramT1.compareTo(mLower);
    int j = paramT2.compareTo(mUpper);
    if ((i >= 0) && (j <= 0)) {
      return this;
    }
    if (i >= 0) {
      paramT1 = mLower;
    }
    if (j <= 0) {
      paramT2 = mUpper;
    }
    return create(paramT1, paramT2);
  }
  
  public T getLower()
  {
    return mLower;
  }
  
  public T getUpper()
  {
    return mUpper;
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCodeGeneric(new Comparable[] { mLower, mUpper });
  }
  
  public Range<T> intersect(Range<T> paramRange)
  {
    Preconditions.checkNotNull(paramRange, "range must not be null");
    int i = mLower.compareTo(mLower);
    int j = mUpper.compareTo(mUpper);
    if ((i <= 0) && (j >= 0)) {
      return this;
    }
    if ((i >= 0) && (j <= 0)) {
      return paramRange;
    }
    Comparable localComparable;
    if (i <= 0) {
      localComparable = mLower;
    } else {
      localComparable = mLower;
    }
    if (j >= 0) {
      paramRange = mUpper;
    } else {
      paramRange = mUpper;
    }
    return create(localComparable, paramRange);
  }
  
  public Range<T> intersect(T paramT1, T paramT2)
  {
    Preconditions.checkNotNull(paramT1, "lower must not be null");
    Preconditions.checkNotNull(paramT2, "upper must not be null");
    int i = paramT1.compareTo(mLower);
    int j = paramT2.compareTo(mUpper);
    if ((i <= 0) && (j >= 0)) {
      return this;
    }
    if (i <= 0) {
      paramT1 = mLower;
    }
    if (j >= 0) {
      paramT2 = mUpper;
    }
    return create(paramT1, paramT2);
  }
  
  public String toString()
  {
    return String.format("[%s, %s]", new Object[] { mLower, mUpper });
  }
}
