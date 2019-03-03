package android.util;

public abstract interface TrustedTime
{
  public abstract long currentTimeMillis();
  
  public abstract boolean forceRefresh();
  
  public abstract boolean forceSync();
  
  public abstract long getCacheAge();
  
  public abstract long getCacheCertainty();
  
  public abstract boolean hasCache();
}
