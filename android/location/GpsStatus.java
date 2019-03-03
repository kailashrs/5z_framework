package android.location;

import android.util.SparseArray;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Deprecated
public final class GpsStatus
{
  private static final int BEIDOU_SVID_OFFSET = 200;
  private static final int GLONASS_SVID_OFFSET = 64;
  public static final int GPS_EVENT_FIRST_FIX = 3;
  public static final int GPS_EVENT_SATELLITE_STATUS = 4;
  public static final int GPS_EVENT_STARTED = 1;
  public static final int GPS_EVENT_STOPPED = 2;
  private static final int NUM_SATELLITES = 255;
  private static final int SBAS_SVID_OFFSET = -87;
  private Iterable<GpsSatellite> mSatelliteList = new Iterable()
  {
    public Iterator<GpsSatellite> iterator()
    {
      return new GpsStatus.SatelliteIterator(GpsStatus.this);
    }
  };
  private final SparseArray<GpsSatellite> mSatellites = new SparseArray();
  private int mTimeToFirstFix;
  
  GpsStatus() {}
  
  private void clearSatellites()
  {
    int i = mSatellites.size();
    for (int j = 0; j < i; j++) {
      mSatellites.valueAt(j)).mValid = false;
    }
  }
  
  private void setStatus(int paramInt, int[] paramArrayOfInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3)
  {
    clearSatellites();
    for (int i = 0; i < paramInt; i++)
    {
      int j = paramArrayOfInt[i] >> 4 & 0xF;
      int k = paramArrayOfInt[i] >> 8;
      boolean bool1 = true;
      int m;
      if (j == 3)
      {
        m = k + 64;
      }
      else if (j == 5)
      {
        m = k + 200;
      }
      else if (j == 2)
      {
        m = k - 87;
      }
      else
      {
        m = k;
        if (j != 1)
        {
          m = k;
          if (j != 4) {
            continue;
          }
        }
      }
      if ((m > 0) && (m <= 255))
      {
        GpsSatellite localGpsSatellite1 = (GpsSatellite)mSatellites.get(m);
        GpsSatellite localGpsSatellite2 = localGpsSatellite1;
        if (localGpsSatellite1 == null)
        {
          localGpsSatellite2 = new GpsSatellite(m);
          mSatellites.put(m, localGpsSatellite2);
        }
        mValid = true;
        mSnr = paramArrayOfFloat1[i];
        mElevation = paramArrayOfFloat2[i];
        mAzimuth = paramArrayOfFloat3[i];
        boolean bool2;
        if ((paramArrayOfInt[i] & 0x1) != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mHasEphemeris = bool2;
        if ((0x2 & paramArrayOfInt[i]) != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mHasAlmanac = bool2;
        if ((0x4 & paramArrayOfInt[i]) != 0) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
        mUsedInFix = bool2;
      }
    }
  }
  
  public int getMaxSatellites()
  {
    return 255;
  }
  
  public Iterable<GpsSatellite> getSatellites()
  {
    return mSatelliteList;
  }
  
  public int getTimeToFirstFix()
  {
    return mTimeToFirstFix;
  }
  
  void setStatus(GnssStatus paramGnssStatus, int paramInt)
  {
    mTimeToFirstFix = paramInt;
    setStatus(mSvCount, mSvidWithFlags, mCn0DbHz, mElevations, mAzimuths);
  }
  
  void setTimeToFirstFix(int paramInt)
  {
    mTimeToFirstFix = paramInt;
  }
  
  @Deprecated
  public static abstract interface Listener
  {
    public abstract void onGpsStatusChanged(int paramInt);
  }
  
  @Deprecated
  public static abstract interface NmeaListener
  {
    public abstract void onNmeaReceived(long paramLong, String paramString);
  }
  
  private final class SatelliteIterator
    implements Iterator<GpsSatellite>
  {
    private int mIndex = 0;
    private final int mSatellitesCount = mSatellites.size();
    
    SatelliteIterator() {}
    
    public boolean hasNext()
    {
      while (mIndex < mSatellitesCount)
      {
        if (mSatellites.valueAt(mIndex)).mValid) {
          return true;
        }
        mIndex += 1;
      }
      return false;
    }
    
    public GpsSatellite next()
    {
      while (mIndex < mSatellitesCount)
      {
        GpsSatellite localGpsSatellite = (GpsSatellite)mSatellites.valueAt(mIndex);
        mIndex += 1;
        if (mValid) {
          return localGpsSatellite;
        }
      }
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
