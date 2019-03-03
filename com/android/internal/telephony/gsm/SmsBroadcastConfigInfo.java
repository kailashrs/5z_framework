package com.android.internal.telephony.gsm;

public final class SmsBroadcastConfigInfo
{
  private int mFromCodeScheme;
  private int mFromServiceId;
  private boolean mSelected;
  private int mToCodeScheme;
  private int mToServiceId;
  
  public SmsBroadcastConfigInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    mFromServiceId = paramInt1;
    mToServiceId = paramInt2;
    mFromCodeScheme = paramInt3;
    mToCodeScheme = paramInt4;
    mSelected = paramBoolean;
  }
  
  public int getFromCodeScheme()
  {
    return mFromCodeScheme;
  }
  
  public int getFromServiceId()
  {
    return mFromServiceId;
  }
  
  public int getToCodeScheme()
  {
    return mToCodeScheme;
  }
  
  public int getToServiceId()
  {
    return mToServiceId;
  }
  
  public boolean isSelected()
  {
    return mSelected;
  }
  
  public void setFromCodeScheme(int paramInt)
  {
    mFromCodeScheme = paramInt;
  }
  
  public void setFromServiceId(int paramInt)
  {
    mFromServiceId = paramInt;
  }
  
  public void setSelected(boolean paramBoolean)
  {
    mSelected = paramBoolean;
  }
  
  public void setToCodeScheme(int paramInt)
  {
    mToCodeScheme = paramInt;
  }
  
  public void setToServiceId(int paramInt)
  {
    mToServiceId = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SmsBroadcastConfigInfo: Id [");
    localStringBuilder.append(mFromServiceId);
    localStringBuilder.append(',');
    localStringBuilder.append(mToServiceId);
    localStringBuilder.append("] Code [");
    localStringBuilder.append(mFromCodeScheme);
    localStringBuilder.append(',');
    localStringBuilder.append(mToCodeScheme);
    localStringBuilder.append("] ");
    String str;
    if (mSelected) {
      str = "ENABLED";
    } else {
      str = "DISABLED";
    }
    localStringBuilder.append(str);
    return localStringBuilder.toString();
  }
}
