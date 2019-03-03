package android.media;

public final class MediaTimestamp
{
  public static final MediaTimestamp TIMESTAMP_UNKNOWN = new MediaTimestamp(-1L, -1L, 0.0F);
  public final float clockRate;
  public final long mediaTimeUs;
  public final long nanoTime;
  
  MediaTimestamp()
  {
    mediaTimeUs = 0L;
    nanoTime = 0L;
    clockRate = 1.0F;
  }
  
  MediaTimestamp(long paramLong1, long paramLong2, float paramFloat)
  {
    mediaTimeUs = paramLong1;
    nanoTime = paramLong2;
    clockRate = paramFloat;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (MediaTimestamp)paramObject;
      if ((mediaTimeUs != mediaTimeUs) || (nanoTime != nanoTime) || (clockRate != clockRate)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public long getAnchorMediaTimeUs()
  {
    return mediaTimeUs;
  }
  
  public long getAnchorSytemNanoTime()
  {
    return nanoTime;
  }
  
  public float getMediaClockRate()
  {
    return clockRate;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getClass().getName());
    localStringBuilder.append("{AnchorMediaTimeUs=");
    localStringBuilder.append(mediaTimeUs);
    localStringBuilder.append(" AnchorSystemNanoTime=");
    localStringBuilder.append(nanoTime);
    localStringBuilder.append(" clockRate=");
    localStringBuilder.append(clockRate);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
