package com.android.internal.telephony.cat;

class LanguageParams
  extends CommandParams
{
  String mLanguage;
  
  LanguageParams(CommandDetails paramCommandDetails, String paramString)
  {
    super(paramCommandDetails);
    mLanguage = paramString;
  }
}
