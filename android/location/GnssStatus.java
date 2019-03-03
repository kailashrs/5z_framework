package android.location;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class GnssStatus
{
  public static final int CONSTELLATION_BEIDOU = 5;
  public static final int CONSTELLATION_GALILEO = 6;
  public static final int CONSTELLATION_GLONASS = 3;
  public static final int CONSTELLATION_GPS = 1;
  public static final int CONSTELLATION_QZSS = 4;
  public static final int CONSTELLATION_SBAS = 2;
  public static final int CONSTELLATION_TYPE_MASK = 15;
  public static final int CONSTELLATION_TYPE_SHIFT_WIDTH = 4;
  public static final int CONSTELLATION_UNKNOWN = 0;
  public static final int GNSS_SV_FLAGS_HAS_ALMANAC_DATA = 2;
  public static final int GNSS_SV_FLAGS_HAS_CARRIER_FREQUENCY = 8;
  public static final int GNSS_SV_FLAGS_HAS_EPHEMERIS_DATA = 1;
  public static final int GNSS_SV_FLAGS_NONE = 0;
  public static final int GNSS_SV_FLAGS_USED_IN_FIX = 4;
  public static final int SVID_SHIFT_WIDTH = 8;
  final float[] mAzimuths;
  final float[] mCarrierFrequencies;
  final float[] mCn0DbHz;
  final float[] mElevations;
  final int mSvCount;
  final int[] mSvidWithFlags;
  
  GnssStatus(int paramInt, int[] paramArrayOfInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
  {
    mSvCount = paramInt;
    mSvidWithFlags = paramArrayOfInt;
    mCn0DbHz = paramArrayOfFloat1;
    mElevations = paramArrayOfFloat2;
    mAzimuths = paramArrayOfFloat3;
    mCarrierFrequencies = paramArrayOfFloat4;
  }
  
  public float getAzimuthDegrees(int paramInt)
  {
    return mAzimuths[paramInt];
  }
  
  public float getCarrierFrequencyHz(int paramInt)
  {
    return mCarrierFrequencies[paramInt];
  }
  
  public float getCn0DbHz(int paramInt)
  {
    return mCn0DbHz[paramInt];
  }
  
  public int getConstellationType(int paramInt)
  {
    return mSvidWithFlags[paramInt] >> 4 & 0xF;
  }
  
  public float getElevationDegrees(int paramInt)
  {
    return mElevations[paramInt];
  }
  
  public int getSatelliteCount()
  {
    return mSvCount;
  }
  
  public int getSvid(int paramInt)
  {
    return mSvidWithFlags[paramInt] >> 8;
  }
  
  public boolean hasAlmanacData(int paramInt)
  {
    boolean bool;
    if ((mSvidWithFlags[paramInt] & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasCarrierFrequencyHz(int paramInt)
  {
    boolean bool;
    if ((mSvidWithFlags[paramInt] & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasEphemerisData(int paramInt)
  {
    paramInt = mSvidWithFlags[paramInt];
    boolean bool = true;
    if ((paramInt & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean usedInFix(int paramInt)
  {
    boolean bool;
    if ((mSvidWithFlags[paramInt] & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onFirstFix(int paramInt) {}
    
    public void onSatelliteStatusChanged(GnssStatus paramGnssStatus) {}
    
    public void onStarted() {}
    
    public void onStopped() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ConstellationType {}
}
