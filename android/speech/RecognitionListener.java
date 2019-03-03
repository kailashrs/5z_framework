package android.speech;

import android.os.Bundle;

public abstract interface RecognitionListener
{
  public abstract void onBeginningOfSpeech();
  
  public abstract void onBufferReceived(byte[] paramArrayOfByte);
  
  public abstract void onEndOfSpeech();
  
  public abstract void onError(int paramInt);
  
  public abstract void onEvent(int paramInt, Bundle paramBundle);
  
  public abstract void onPartialResults(Bundle paramBundle);
  
  public abstract void onReadyForSpeech(Bundle paramBundle);
  
  public abstract void onResults(Bundle paramBundle);
  
  public abstract void onRmsChanged(float paramFloat);
}
