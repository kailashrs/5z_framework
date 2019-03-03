package com.android.internal.telephony.cdma;

public class CdmaSmsBroadcastConfigInfo
{
  private int mFromServiceCategory;
  private int mLanguage;
  private boolean mSelected;
  private int mToServiceCategory;
  
  public CdmaSmsBroadcastConfigInfo(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    mFromServiceCategory = paramInt1;
    mToServiceCategory = paramInt2;
    mLanguage = paramInt3;
    mSelected = paramBoolean;
  }
  
  public int getFromServiceCategory()
  {
    return mFromServiceCategory;
  }
  
  public int getLanguage()
  {
    return mLanguage;
  }
  
  public int getToServiceCategory()
  {
    return mToServiceCategory;
  }
  
  public boolean isSelected()
  {
    return mSelected;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CdmaSmsBroadcastConfigInfo: Id [");
    localStringBuilder.append(mFromServiceCategory);
    localStringBuilder.append(", ");
    localStringBuilder.append(mToServiceCategory);
    localStringBuilder.append("] ");
    String str;
    if (isSelected()) {
      str = "ENABLED";
    } else {
      str = "DISABLED";
    }
    localStringBuilder.append(str);
    return localStringBuilder.toString();
  }
}
