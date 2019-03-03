package android.view;

import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import java.util.Objects;

public class DisplayAdjustments
{
  public static final DisplayAdjustments DEFAULT_DISPLAY_ADJUSTMENTS = new DisplayAdjustments();
  private volatile CompatibilityInfo mCompatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
  private Configuration mConfiguration;
  
  public DisplayAdjustments() {}
  
  public DisplayAdjustments(Configuration paramConfiguration)
  {
    if (paramConfiguration == null) {
      paramConfiguration = Configuration.EMPTY;
    }
    mConfiguration = new Configuration(paramConfiguration);
  }
  
  public DisplayAdjustments(DisplayAdjustments paramDisplayAdjustments)
  {
    setCompatibilityInfo(mCompatInfo);
    if (mConfiguration != null) {
      paramDisplayAdjustments = mConfiguration;
    } else {
      paramDisplayAdjustments = Configuration.EMPTY;
    }
    mConfiguration = new Configuration(paramDisplayAdjustments);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof DisplayAdjustments;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (DisplayAdjustments)paramObject;
    bool1 = bool2;
    if (Objects.equals(mCompatInfo, mCompatInfo))
    {
      bool1 = bool2;
      if (Objects.equals(mConfiguration, mConfiguration)) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public CompatibilityInfo getCompatibilityInfo()
  {
    return mCompatInfo;
  }
  
  public Configuration getConfiguration()
  {
    return mConfiguration;
  }
  
  public int hashCode()
  {
    return (17 * 31 + Objects.hashCode(mCompatInfo)) * 31 + Objects.hashCode(mConfiguration);
  }
  
  public void setCompatibilityInfo(CompatibilityInfo paramCompatibilityInfo)
  {
    if (this != DEFAULT_DISPLAY_ADJUSTMENTS)
    {
      if ((paramCompatibilityInfo != null) && ((paramCompatibilityInfo.isScalingRequired()) || (!paramCompatibilityInfo.supportsScreen()))) {
        mCompatInfo = paramCompatibilityInfo;
      } else {
        mCompatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
      }
      return;
    }
    throw new IllegalArgumentException("setCompatbilityInfo: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
  }
  
  public void setConfiguration(Configuration paramConfiguration)
  {
    if (this != DEFAULT_DISPLAY_ADJUSTMENTS)
    {
      Configuration localConfiguration = mConfiguration;
      if (paramConfiguration == null) {
        paramConfiguration = Configuration.EMPTY;
      }
      localConfiguration.setTo(paramConfiguration);
      return;
    }
    throw new IllegalArgumentException("setConfiguration: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
  }
}
