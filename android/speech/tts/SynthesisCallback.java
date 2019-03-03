package android.speech.tts;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract interface SynthesisCallback
{
  public abstract int audioAvailable(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract int done();
  
  public abstract void error();
  
  public abstract void error(int paramInt);
  
  public abstract int getMaxBufferSize();
  
  public abstract boolean hasFinished();
  
  public abstract boolean hasStarted();
  
  public void rangeStart(int paramInt1, int paramInt2, int paramInt3) {}
  
  public abstract int start(int paramInt1, int paramInt2, int paramInt3);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SupportedAudioFormat {}
}
