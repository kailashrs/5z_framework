package android.media;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SyncParams
{
  public static final int AUDIO_ADJUST_MODE_DEFAULT = 0;
  public static final int AUDIO_ADJUST_MODE_RESAMPLE = 2;
  public static final int AUDIO_ADJUST_MODE_STRETCH = 1;
  private static final int SET_AUDIO_ADJUST_MODE = 2;
  private static final int SET_FRAME_RATE = 8;
  private static final int SET_SYNC_SOURCE = 1;
  private static final int SET_TOLERANCE = 4;
  public static final int SYNC_SOURCE_AUDIO = 2;
  public static final int SYNC_SOURCE_DEFAULT = 0;
  public static final int SYNC_SOURCE_SYSTEM_CLOCK = 1;
  public static final int SYNC_SOURCE_VSYNC = 3;
  private int mAudioAdjustMode = 0;
  private float mFrameRate = 0.0F;
  private int mSet = 0;
  private int mSyncSource = 0;
  private float mTolerance = 0.0F;
  
  public SyncParams() {}
  
  public SyncParams allowDefaults()
  {
    mSet |= 0x7;
    return this;
  }
  
  public int getAudioAdjustMode()
  {
    if ((mSet & 0x2) != 0) {
      return mAudioAdjustMode;
    }
    throw new IllegalStateException("audio adjust mode not set");
  }
  
  public float getFrameRate()
  {
    if ((mSet & 0x8) != 0) {
      return mFrameRate;
    }
    throw new IllegalStateException("frame rate not set");
  }
  
  public int getSyncSource()
  {
    if ((mSet & 0x1) != 0) {
      return mSyncSource;
    }
    throw new IllegalStateException("sync source not set");
  }
  
  public float getTolerance()
  {
    if ((mSet & 0x4) != 0) {
      return mTolerance;
    }
    throw new IllegalStateException("tolerance not set");
  }
  
  public SyncParams setAudioAdjustMode(int paramInt)
  {
    mAudioAdjustMode = paramInt;
    mSet |= 0x2;
    return this;
  }
  
  public SyncParams setFrameRate(float paramFloat)
  {
    mFrameRate = paramFloat;
    mSet |= 0x8;
    return this;
  }
  
  public SyncParams setSyncSource(int paramInt)
  {
    mSyncSource = paramInt;
    mSet |= 0x1;
    return this;
  }
  
  public SyncParams setTolerance(float paramFloat)
  {
    if ((paramFloat >= 0.0F) && (paramFloat < 1.0F))
    {
      mTolerance = paramFloat;
      mSet |= 0x4;
      return this;
    }
    throw new IllegalArgumentException("tolerance must be less than one and non-negative");
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AudioAdjustMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SyncSource {}
}
