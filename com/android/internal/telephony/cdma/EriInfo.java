package com.android.internal.telephony.cdma;

public final class EriInfo
{
  public static final int ROAMING_ICON_MODE_FLASH = 1;
  public static final int ROAMING_ICON_MODE_NORMAL = 0;
  public static final int ROAMING_INDICATOR_FLASH = 2;
  public static final int ROAMING_INDICATOR_OFF = 1;
  public static final int ROAMING_INDICATOR_ON = 0;
  public int alertId;
  public int callPromptId;
  public String eriText;
  public int iconIndex;
  public int iconMode;
  public int roamingIndicator;
  
  public EriInfo(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4, int paramInt5)
  {
    roamingIndicator = paramInt1;
    iconIndex = paramInt2;
    iconMode = paramInt3;
    eriText = paramString;
    callPromptId = paramInt4;
    alertId = paramInt5;
  }
}
