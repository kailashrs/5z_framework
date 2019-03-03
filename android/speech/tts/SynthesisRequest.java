package android.speech.tts;

import android.os.Bundle;

public final class SynthesisRequest
{
  private int mCallerUid;
  private String mCountry;
  private String mLanguage;
  private final Bundle mParams;
  private int mPitch;
  private int mSpeechRate;
  private final CharSequence mText;
  private String mVariant;
  private String mVoiceName;
  
  public SynthesisRequest(CharSequence paramCharSequence, Bundle paramBundle)
  {
    mText = paramCharSequence;
    mParams = new Bundle(paramBundle);
  }
  
  public SynthesisRequest(String paramString, Bundle paramBundle)
  {
    mText = paramString;
    mParams = new Bundle(paramBundle);
  }
  
  public int getCallerUid()
  {
    return mCallerUid;
  }
  
  public CharSequence getCharSequenceText()
  {
    return mText;
  }
  
  public String getCountry()
  {
    return mCountry;
  }
  
  public String getLanguage()
  {
    return mLanguage;
  }
  
  public Bundle getParams()
  {
    return mParams;
  }
  
  public int getPitch()
  {
    return mPitch;
  }
  
  public int getSpeechRate()
  {
    return mSpeechRate;
  }
  
  @Deprecated
  public String getText()
  {
    return mText.toString();
  }
  
  public String getVariant()
  {
    return mVariant;
  }
  
  public String getVoiceName()
  {
    return mVoiceName;
  }
  
  void setCallerUid(int paramInt)
  {
    mCallerUid = paramInt;
  }
  
  void setLanguage(String paramString1, String paramString2, String paramString3)
  {
    mLanguage = paramString1;
    mCountry = paramString2;
    mVariant = paramString3;
  }
  
  void setPitch(int paramInt)
  {
    mPitch = paramInt;
  }
  
  void setSpeechRate(int paramInt)
  {
    mSpeechRate = paramInt;
  }
  
  void setVoiceName(String paramString)
  {
    mVoiceName = paramString;
  }
}
