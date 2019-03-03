package android.service.voice;

import android.os.Bundle;
import android.os.IBinder;

public abstract class VoiceInteractionManagerInternal
{
  public VoiceInteractionManagerInternal() {}
  
  public abstract void startLocalVoiceInteraction(IBinder paramIBinder, Bundle paramBundle);
  
  public abstract void stopLocalVoiceInteraction(IBinder paramIBinder);
  
  public abstract boolean supportsLocalVoiceInteraction();
}
