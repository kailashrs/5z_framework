package android.telephony.cdma;

import android.os.Bundle;
import android.telephony.CellLocation;

public class CdmaCellLocation
  extends CellLocation
{
  public static final int INVALID_LAT_LONG = Integer.MAX_VALUE;
  private int mBaseStationId = -1;
  private int mBaseStationLatitude = Integer.MAX_VALUE;
  private int mBaseStationLongitude = Integer.MAX_VALUE;
  private int mNetworkId = -1;
  private int mSystemId = -1;
  
  public CdmaCellLocation()
  {
    mBaseStationId = -1;
    mBaseStationLatitude = Integer.MAX_VALUE;
    mBaseStationLongitude = Integer.MAX_VALUE;
    mSystemId = -1;
    mNetworkId = -1;
  }
  
  public CdmaCellLocation(Bundle paramBundle)
  {
    mBaseStationId = paramBundle.getInt("baseStationId", mBaseStationId);
    mBaseStationLatitude = paramBundle.getInt("baseStationLatitude", mBaseStationLatitude);
    mBaseStationLongitude = paramBundle.getInt("baseStationLongitude", mBaseStationLongitude);
    mSystemId = paramBundle.getInt("systemId", mSystemId);
    mNetworkId = paramBundle.getInt("networkId", mNetworkId);
  }
  
  public static double convertQuartSecToDecDegrees(int paramInt)
  {
    if ((!Double.isNaN(paramInt)) && (paramInt >= -2592000) && (paramInt <= 2592000)) {
      return paramInt / 14400.0D;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid coordiante value:");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static boolean equalsHandlesNulls(Object paramObject1, Object paramObject2)
  {
    boolean bool;
    if (paramObject1 == null)
    {
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      }
    }
    else {
      bool = paramObject1.equals(paramObject2);
    }
    return bool;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    try
    {
      CdmaCellLocation localCdmaCellLocation = (CdmaCellLocation)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (equalsHandlesNulls(Integer.valueOf(mBaseStationId), Integer.valueOf(mBaseStationId)))
      {
        bool2 = bool1;
        if (equalsHandlesNulls(Integer.valueOf(mBaseStationLatitude), Integer.valueOf(mBaseStationLatitude)))
        {
          bool2 = bool1;
          if (equalsHandlesNulls(Integer.valueOf(mBaseStationLongitude), Integer.valueOf(mBaseStationLongitude)))
          {
            bool2 = bool1;
            if (equalsHandlesNulls(Integer.valueOf(mSystemId), Integer.valueOf(mSystemId)))
            {
              bool2 = bool1;
              if (equalsHandlesNulls(Integer.valueOf(mNetworkId), Integer.valueOf(mNetworkId))) {
                bool2 = true;
              }
            }
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public void fillInNotifierBundle(Bundle paramBundle)
  {
    paramBundle.putInt("baseStationId", mBaseStationId);
    paramBundle.putInt("baseStationLatitude", mBaseStationLatitude);
    paramBundle.putInt("baseStationLongitude", mBaseStationLongitude);
    paramBundle.putInt("systemId", mSystemId);
    paramBundle.putInt("networkId", mNetworkId);
  }
  
  public int getBaseStationId()
  {
    return mBaseStationId;
  }
  
  public int getBaseStationLatitude()
  {
    return mBaseStationLatitude;
  }
  
  public int getBaseStationLongitude()
  {
    return mBaseStationLongitude;
  }
  
  public int getNetworkId()
  {
    return mNetworkId;
  }
  
  public int getSystemId()
  {
    return mSystemId;
  }
  
  public int hashCode()
  {
    return mBaseStationId ^ mBaseStationLatitude ^ mBaseStationLongitude ^ mSystemId ^ mNetworkId;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((mBaseStationId == -1) && (mBaseStationLatitude == Integer.MAX_VALUE) && (mBaseStationLongitude == Integer.MAX_VALUE) && (mSystemId == -1) && (mNetworkId == -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setCellLocationData(int paramInt1, int paramInt2, int paramInt3)
  {
    mBaseStationId = paramInt1;
    mBaseStationLatitude = paramInt2;
    mBaseStationLongitude = paramInt3;
  }
  
  public void setCellLocationData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mBaseStationId = paramInt1;
    mBaseStationLatitude = paramInt2;
    mBaseStationLongitude = paramInt3;
    mSystemId = paramInt4;
    mNetworkId = paramInt5;
  }
  
  public void setStateInvalid()
  {
    mBaseStationId = -1;
    mBaseStationLatitude = Integer.MAX_VALUE;
    mBaseStationLongitude = Integer.MAX_VALUE;
    mSystemId = -1;
    mNetworkId = -1;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mBaseStationId);
    localStringBuilder.append(",");
    localStringBuilder.append(mBaseStationLatitude);
    localStringBuilder.append(",");
    localStringBuilder.append(mBaseStationLongitude);
    localStringBuilder.append(",");
    localStringBuilder.append(mSystemId);
    localStringBuilder.append(",");
    localStringBuilder.append(mNetworkId);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
