package android.speech.tts;

public abstract class UtteranceProgressListener
{
  public UtteranceProgressListener() {}
  
  static UtteranceProgressListener from(TextToSpeech.OnUtteranceCompletedListener paramOnUtteranceCompletedListener)
  {
    new UtteranceProgressListener()
    {
      public void onDone(String paramAnonymousString)
      {
        try
        {
          onUtteranceCompleted(paramAnonymousString);
          return;
        }
        finally
        {
          paramAnonymousString = finally;
          throw paramAnonymousString;
        }
      }
      
      public void onError(String paramAnonymousString)
      {
        onUtteranceCompleted(paramAnonymousString);
      }
      
      public void onStart(String paramAnonymousString) {}
      
      public void onStop(String paramAnonymousString, boolean paramAnonymousBoolean)
      {
        onUtteranceCompleted(paramAnonymousString);
      }
    };
  }
  
  public void onAudioAvailable(String paramString, byte[] paramArrayOfByte) {}
  
  public void onBeginSynthesis(String paramString, int paramInt1, int paramInt2, int paramInt3) {}
  
  public abstract void onDone(String paramString);
  
  @Deprecated
  public abstract void onError(String paramString);
  
  public void onError(String paramString, int paramInt)
  {
    onError(paramString);
  }
  
  public void onRangeStart(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    onUtteranceRangeStart(paramString, paramInt1, paramInt2);
  }
  
  public abstract void onStart(String paramString);
  
  public void onStop(String paramString, boolean paramBoolean) {}
  
  @Deprecated
  public void onUtteranceRangeStart(String paramString, int paramInt1, int paramInt2) {}
}
