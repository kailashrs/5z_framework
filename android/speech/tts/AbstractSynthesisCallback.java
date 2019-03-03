package android.speech.tts;

abstract class AbstractSynthesisCallback
  implements SynthesisCallback
{
  protected final boolean mClientIsUsingV2;
  
  AbstractSynthesisCallback(boolean paramBoolean)
  {
    mClientIsUsingV2 = paramBoolean;
  }
  
  int errorCodeOnStop()
  {
    int i;
    if (mClientIsUsingV2) {
      i = -2;
    } else {
      i = -1;
    }
    return i;
  }
  
  abstract void stop();
}
