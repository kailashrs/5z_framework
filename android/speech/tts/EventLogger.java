package android.speech.tts;

import android.text.TextUtils;

class EventLogger
  extends AbstractEventLogger
{
  private final SynthesisRequest mRequest;
  
  EventLogger(SynthesisRequest paramSynthesisRequest, int paramInt1, int paramInt2, String paramString)
  {
    super(paramInt1, paramInt2, paramString);
    mRequest = paramSynthesisRequest;
  }
  
  private String getLocaleString()
  {
    StringBuilder localStringBuilder = new StringBuilder(mRequest.getLanguage());
    if (!TextUtils.isEmpty(mRequest.getCountry()))
    {
      localStringBuilder.append('-');
      localStringBuilder.append(mRequest.getCountry());
      if (!TextUtils.isEmpty(mRequest.getVariant()))
      {
        localStringBuilder.append('-');
        localStringBuilder.append(mRequest.getVariant());
      }
    }
    return localStringBuilder.toString();
  }
  
  private int getUtteranceLength()
  {
    String str = mRequest.getText();
    int i;
    if (str == null) {
      i = 0;
    } else {
      i = str.length();
    }
    return i;
  }
  
  protected void logFailure(int paramInt)
  {
    if (paramInt != -2) {
      EventLogTags.writeTtsSpeakFailure(mServiceApp, mCallerUid, mCallerPid, getUtteranceLength(), getLocaleString(), mRequest.getSpeechRate(), mRequest.getPitch());
    }
  }
  
  protected void logSuccess(long paramLong1, long paramLong2, long paramLong3)
  {
    EventLogTags.writeTtsSpeakSuccess(mServiceApp, mCallerUid, mCallerPid, getUtteranceLength(), getLocaleString(), mRequest.getSpeechRate(), mRequest.getPitch(), paramLong2, paramLong3, paramLong1);
  }
}
