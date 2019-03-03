package android.webkit;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TracingConfig
{
  public static final int CATEGORIES_ALL = 1;
  public static final int CATEGORIES_ANDROID_WEBVIEW = 2;
  public static final int CATEGORIES_FRAME_VIEWER = 64;
  public static final int CATEGORIES_INPUT_LATENCY = 8;
  public static final int CATEGORIES_JAVASCRIPT_AND_RENDERING = 32;
  public static final int CATEGORIES_NONE = 0;
  public static final int CATEGORIES_RENDERING = 16;
  public static final int CATEGORIES_WEB_DEVELOPER = 4;
  public static final int RECORD_CONTINUOUSLY = 1;
  public static final int RECORD_UNTIL_FULL = 0;
  private final List<String> mCustomIncludedCategories = new ArrayList();
  private int mPredefinedCategories;
  private int mTracingMode;
  
  public TracingConfig(int paramInt1, List<String> paramList, int paramInt2)
  {
    mPredefinedCategories = paramInt1;
    mCustomIncludedCategories.addAll(paramList);
    mTracingMode = paramInt2;
  }
  
  public List<String> getCustomIncludedCategories()
  {
    return mCustomIncludedCategories;
  }
  
  public int getPredefinedCategories()
  {
    return mPredefinedCategories;
  }
  
  public int getTracingMode()
  {
    return mTracingMode;
  }
  
  public static class Builder
  {
    private final List<String> mCustomIncludedCategories = new ArrayList();
    private int mPredefinedCategories = 0;
    private int mTracingMode = 1;
    
    public Builder() {}
    
    public Builder addCategories(Collection<String> paramCollection)
    {
      mCustomIncludedCategories.addAll(paramCollection);
      return this;
    }
    
    public Builder addCategories(int... paramVarArgs)
    {
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++)
      {
        int k = paramVarArgs[j];
        mPredefinedCategories |= k;
      }
      return this;
    }
    
    public Builder addCategories(String... paramVarArgs)
    {
      int i = paramVarArgs.length;
      for (int j = 0; j < i; j++)
      {
        String str = paramVarArgs[j];
        mCustomIncludedCategories.add(str);
      }
      return this;
    }
    
    public TracingConfig build()
    {
      return new TracingConfig(mPredefinedCategories, mCustomIncludedCategories, mTracingMode);
    }
    
    public Builder setTracingMode(int paramInt)
    {
      mTracingMode = paramInt;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PredefinedCategories {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TracingMode {}
}
