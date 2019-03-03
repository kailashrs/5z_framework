package com.android.internal.telephony;

public class RestrictedState
{
  private boolean mCsEmergencyRestricted;
  private boolean mCsNormalRestricted;
  private boolean mPsRestricted;
  
  public RestrictedState()
  {
    setPsRestricted(false);
    setCsNormalRestricted(false);
    setCsEmergencyRestricted(false);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    try
    {
      RestrictedState localRestrictedState = (RestrictedState)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mPsRestricted == mPsRestricted)
      {
        bool2 = bool1;
        if (mCsNormalRestricted == mCsNormalRestricted)
        {
          bool2 = bool1;
          if (mCsEmergencyRestricted == mCsEmergencyRestricted) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public boolean isAnyCsRestricted()
  {
    boolean bool;
    if ((!mCsNormalRestricted) && (!mCsEmergencyRestricted)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isCsEmergencyRestricted()
  {
    return mCsEmergencyRestricted;
  }
  
  public boolean isCsNormalRestricted()
  {
    return mCsNormalRestricted;
  }
  
  public boolean isCsRestricted()
  {
    boolean bool;
    if ((mCsNormalRestricted) && (mCsEmergencyRestricted)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPsRestricted()
  {
    return mPsRestricted;
  }
  
  public void setCsEmergencyRestricted(boolean paramBoolean)
  {
    mCsEmergencyRestricted = paramBoolean;
  }
  
  public void setCsNormalRestricted(boolean paramBoolean)
  {
    mCsNormalRestricted = paramBoolean;
  }
  
  public void setPsRestricted(boolean paramBoolean)
  {
    mPsRestricted = paramBoolean;
  }
  
  public String toString()
  {
    Object localObject1 = "none";
    Object localObject2;
    if ((mCsEmergencyRestricted) && (mCsNormalRestricted))
    {
      localObject2 = "all";
    }
    else if ((mCsEmergencyRestricted) && (!mCsNormalRestricted))
    {
      localObject2 = "emergency";
    }
    else
    {
      localObject2 = localObject1;
      if (!mCsEmergencyRestricted)
      {
        localObject2 = localObject1;
        if (mCsNormalRestricted) {
          localObject2 = "normal call";
        }
      }
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Restricted State CS: ");
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(" PS:");
    ((StringBuilder)localObject1).append(mPsRestricted);
    return ((StringBuilder)localObject1).toString();
  }
}
