package android.location;

import android.os.Bundle;

public abstract interface LocationListener
{
  public abstract void onLocationChanged(Location paramLocation);
  
  public abstract void onProviderDisabled(String paramString);
  
  public abstract void onProviderEnabled(String paramString);
  
  public abstract void onStatusChanged(String paramString, int paramInt, Bundle paramBundle);
}
