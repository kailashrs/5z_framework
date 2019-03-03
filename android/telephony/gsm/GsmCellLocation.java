package android.telephony.gsm;

import android.os.Bundle;
import android.telephony.CellLocation;

public class GsmCellLocation
  extends CellLocation
{
  private int mCid;
  private int mLac;
  private int mPsc;
  
  public GsmCellLocation()
  {
    mLac = -1;
    mCid = -1;
    mPsc = -1;
  }
  
  public GsmCellLocation(Bundle paramBundle)
  {
    mLac = paramBundle.getInt("lac", -1);
    mCid = paramBundle.getInt("cid", -1);
    mPsc = paramBundle.getInt("psc", -1);
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
      GsmCellLocation localGsmCellLocation = (GsmCellLocation)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (equalsHandlesNulls(Integer.valueOf(mLac), Integer.valueOf(mLac)))
      {
        bool2 = bool1;
        if (equalsHandlesNulls(Integer.valueOf(mCid), Integer.valueOf(mCid)))
        {
          bool2 = bool1;
          if (equalsHandlesNulls(Integer.valueOf(mPsc), Integer.valueOf(mPsc))) {
            bool2 = true;
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
    paramBundle.putInt("lac", mLac);
    paramBundle.putInt("cid", mCid);
    paramBundle.putInt("psc", mPsc);
  }
  
  public int getCid()
  {
    return mCid;
  }
  
  public int getLac()
  {
    return mLac;
  }
  
  public int getPsc()
  {
    return mPsc;
  }
  
  public int hashCode()
  {
    return mLac ^ mCid;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((mLac == -1) && (mCid == -1) && (mPsc == -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setLacAndCid(int paramInt1, int paramInt2)
  {
    mLac = paramInt1;
    mCid = paramInt2;
  }
  
  public void setPsc(int paramInt)
  {
    mPsc = paramInt;
  }
  
  public void setStateInvalid()
  {
    mLac = -1;
    mCid = -1;
    mPsc = -1;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mLac);
    localStringBuilder.append(",");
    localStringBuilder.append(mCid);
    localStringBuilder.append(",");
    localStringBuilder.append(mPsc);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
