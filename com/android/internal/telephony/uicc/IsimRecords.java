package com.android.internal.telephony.uicc;

public abstract interface IsimRecords
{
  public abstract String getIsimDomain();
  
  public abstract String getIsimImpi();
  
  public abstract String[] getIsimImpu();
  
  public abstract String getIsimIst();
  
  public abstract String[] getIsimPcscf();
}
