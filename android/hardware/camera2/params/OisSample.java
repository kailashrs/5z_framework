package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import com.android.internal.util.Preconditions;

public final class OisSample
{
  private final long mTimestampNs;
  private final float mXShift;
  private final float mYShift;
  
  public OisSample(long paramLong, float paramFloat1, float paramFloat2)
  {
    mTimestampNs = paramLong;
    mXShift = Preconditions.checkArgumentFinite(paramFloat1, "xShift must be finite");
    mYShift = Preconditions.checkArgumentFinite(paramFloat2, "yShift must be finite");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof OisSample))
    {
      paramObject = (OisSample)paramObject;
      boolean bool2 = bool1;
      if (mTimestampNs == mTimestampNs)
      {
        bool2 = bool1;
        if (mXShift == mXShift)
        {
          bool2 = bool1;
          if (mYShift == mYShift) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  public long getTimestamp()
  {
    return mTimestampNs;
  }
  
  public float getXshift()
  {
    return mXShift;
  }
  
  public float getYshift()
  {
    return mYShift;
  }
  
  public int hashCode()
  {
    int i = HashCodeHelpers.hashCode(new float[] { (float)mTimestampNs });
    return HashCodeHelpers.hashCode(new float[] { mXShift, mYShift, i });
  }
  
  public String toString()
  {
    return String.format("OisSample{timestamp:%d, shift_x:%f, shift_y:%f}", new Object[] { Long.valueOf(mTimestampNs), Float.valueOf(mXShift), Float.valueOf(mYShift) });
  }
}
