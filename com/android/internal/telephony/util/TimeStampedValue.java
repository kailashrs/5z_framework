package com.android.internal.telephony.util;

public final class TimeStampedValue<T>
{
  public final long mElapsedRealtime;
  public final T mValue;
  
  public TimeStampedValue(T paramT, long paramLong)
  {
    mValue = paramT;
    mElapsedRealtime = paramLong;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (TimeStampedValue)paramObject;
      if (mElapsedRealtime != mElapsedRealtime) {
        return false;
      }
      if (mValue != null) {
        bool = mValue.equals(mValue);
      } else if (mValue != null) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    int i;
    if (mValue != null) {
      i = mValue.hashCode();
    } else {
      i = 0;
    }
    return 31 * i + (int)(mElapsedRealtime ^ mElapsedRealtime >>> 32);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TimeStampedValue{mValue=");
    localStringBuilder.append(mValue);
    localStringBuilder.append(", elapsedRealtime=");
    localStringBuilder.append(mElapsedRealtime);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
