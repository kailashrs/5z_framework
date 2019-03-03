package android.media;

import android.util.IntArray;

public abstract class AudioManagerInternal
{
  public AudioManagerInternal() {}
  
  public abstract void adjustStreamVolumeForUid(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4);
  
  public abstract void adjustSuggestedStreamVolumeForUid(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4);
  
  public abstract int getRingerModeInternal();
  
  public abstract void setAccessibilityServiceUids(IntArray paramIntArray);
  
  public abstract void setRingerModeDelegate(RingerModeDelegate paramRingerModeDelegate);
  
  public abstract void setRingerModeInternal(int paramInt, String paramString);
  
  public abstract void setStreamVolumeForUid(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4);
  
  public abstract void silenceRingerModeInternal(String paramString);
  
  public abstract void updateRingerModeAffectedStreamsInternal();
  
  public static abstract interface RingerModeDelegate
  {
    public abstract boolean canVolumeDownEnterSilent();
    
    public abstract int getRingerModeAffectedStreams(int paramInt);
    
    public abstract int onSetRingerModeExternal(int paramInt1, int paramInt2, String paramString, int paramInt3, VolumePolicy paramVolumePolicy);
    
    public abstract int onSetRingerModeInternal(int paramInt1, int paramInt2, String paramString, int paramInt3, VolumePolicy paramVolumePolicy);
  }
}
