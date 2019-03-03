package android.location;

@Deprecated
public final class GpsSatellite
{
  float mAzimuth;
  float mElevation;
  boolean mHasAlmanac;
  boolean mHasEphemeris;
  int mPrn;
  float mSnr;
  boolean mUsedInFix;
  boolean mValid;
  
  GpsSatellite(int paramInt)
  {
    mPrn = paramInt;
  }
  
  public float getAzimuth()
  {
    return mAzimuth;
  }
  
  public float getElevation()
  {
    return mElevation;
  }
  
  public int getPrn()
  {
    return mPrn;
  }
  
  public float getSnr()
  {
    return mSnr;
  }
  
  public boolean hasAlmanac()
  {
    return mHasAlmanac;
  }
  
  public boolean hasEphemeris()
  {
    return mHasEphemeris;
  }
  
  void setStatus(GpsSatellite paramGpsSatellite)
  {
    if (paramGpsSatellite == null)
    {
      mValid = false;
    }
    else
    {
      mValid = mValid;
      mHasEphemeris = mHasEphemeris;
      mHasAlmanac = mHasAlmanac;
      mUsedInFix = mUsedInFix;
      mSnr = mSnr;
      mElevation = mElevation;
      mAzimuth = mAzimuth;
    }
  }
  
  public boolean usedInFix()
  {
    return mUsedInFix;
  }
}
