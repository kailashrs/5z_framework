package com.android.internal.telephony.cat;

class ItemsIconId
  extends ValueObject
{
  int[] recordNumbers;
  boolean selfExplanatory;
  
  ItemsIconId() {}
  
  ComprehensionTlvTag getTag()
  {
    return ComprehensionTlvTag.ITEM_ICON_ID_LIST;
  }
}
