package com.android.internal.telephony.cat;

abstract class ValueObject
{
  ValueObject() {}
  
  abstract ComprehensionTlvTag getTag();
}
