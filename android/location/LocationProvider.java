package android.location;

import com.android.internal.location.ProviderProperties;

public class LocationProvider
{
  public static final int AVAILABLE = 2;
  public static final String BAD_CHARS_REGEX = "[^a-zA-Z0-9]";
  public static final int OUT_OF_SERVICE = 0;
  public static final int TEMPORARILY_UNAVAILABLE = 1;
  private final String mName;
  private final ProviderProperties mProperties;
  
  public LocationProvider(String paramString, ProviderProperties paramProviderProperties)
  {
    if (!paramString.matches("[^a-zA-Z0-9]"))
    {
      mName = paramString;
      mProperties = paramProviderProperties;
      return;
    }
    paramProviderProperties = new StringBuilder();
    paramProviderProperties.append("provider name contains illegal character: ");
    paramProviderProperties.append(paramString);
    throw new IllegalArgumentException(paramProviderProperties.toString());
  }
  
  public static boolean propertiesMeetCriteria(String paramString, ProviderProperties paramProviderProperties, Criteria paramCriteria)
  {
    if ("passive".equals(paramString)) {
      return false;
    }
    if (paramProviderProperties == null) {
      return false;
    }
    if ((paramCriteria.getAccuracy() != 0) && (paramCriteria.getAccuracy() < mAccuracy)) {
      return false;
    }
    if ((paramCriteria.getPowerRequirement() != 0) && (paramCriteria.getPowerRequirement() < mPowerRequirement)) {
      return false;
    }
    if ((paramCriteria.isAltitudeRequired()) && (!mSupportsAltitude)) {
      return false;
    }
    if ((paramCriteria.isSpeedRequired()) && (!mSupportsSpeed)) {
      return false;
    }
    if ((paramCriteria.isBearingRequired()) && (!mSupportsBearing)) {
      return false;
    }
    return (paramCriteria.isCostAllowed()) || (!mHasMonetaryCost);
  }
  
  public int getAccuracy()
  {
    return mProperties.mAccuracy;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getPowerRequirement()
  {
    return mProperties.mPowerRequirement;
  }
  
  public boolean hasMonetaryCost()
  {
    return mProperties.mHasMonetaryCost;
  }
  
  public boolean meetsCriteria(Criteria paramCriteria)
  {
    return propertiesMeetCriteria(mName, mProperties, paramCriteria);
  }
  
  public boolean requiresCell()
  {
    return mProperties.mRequiresCell;
  }
  
  public boolean requiresNetwork()
  {
    return mProperties.mRequiresNetwork;
  }
  
  public boolean requiresSatellite()
  {
    return mProperties.mRequiresSatellite;
  }
  
  public boolean supportsAltitude()
  {
    return mProperties.mSupportsAltitude;
  }
  
  public boolean supportsBearing()
  {
    return mProperties.mSupportsBearing;
  }
  
  public boolean supportsSpeed()
  {
    return mProperties.mSupportsSpeed;
  }
}
