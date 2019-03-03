package android.webkit;

import android.annotation.SystemApi;
import java.util.Map;

public class WebStorage
{
  @SystemApi
  public WebStorage() {}
  
  public static WebStorage getInstance()
  {
    return WebViewFactory.getProvider().getWebStorage();
  }
  
  public void deleteAllData() {}
  
  public void deleteOrigin(String paramString) {}
  
  public void getOrigins(ValueCallback<Map> paramValueCallback) {}
  
  public void getQuotaForOrigin(String paramString, ValueCallback<Long> paramValueCallback) {}
  
  public void getUsageForOrigin(String paramString, ValueCallback<Long> paramValueCallback) {}
  
  @Deprecated
  public void setQuotaForOrigin(String paramString, long paramLong) {}
  
  public static class Origin
  {
    private String mOrigin = null;
    private long mQuota = 0L;
    private long mUsage = 0L;
    
    @SystemApi
    protected Origin(String paramString, long paramLong1, long paramLong2)
    {
      mOrigin = paramString;
      mQuota = paramLong1;
      mUsage = paramLong2;
    }
    
    public String getOrigin()
    {
      return mOrigin;
    }
    
    public long getQuota()
    {
      return mQuota;
    }
    
    public long getUsage()
    {
      return mUsage;
    }
  }
  
  @Deprecated
  public static abstract interface QuotaUpdater
  {
    public abstract void updateQuota(long paramLong);
  }
}
