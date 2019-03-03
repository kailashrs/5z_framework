package android.media;

public class MediaSyncEvent
{
  public static final int SYNC_EVENT_NONE = 0;
  public static final int SYNC_EVENT_PRESENTATION_COMPLETE = 1;
  private int mAudioSession = 0;
  private final int mType;
  
  private MediaSyncEvent(int paramInt)
  {
    mType = paramInt;
  }
  
  public static MediaSyncEvent createEvent(int paramInt)
    throws IllegalArgumentException
  {
    if (isValidType(paramInt)) {
      return new MediaSyncEvent(paramInt);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt);
    localStringBuilder.append("is not a valid MediaSyncEvent type.");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static boolean isValidType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
  }
  
  public int getAudioSessionId()
  {
    return mAudioSession;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public MediaSyncEvent setAudioSessionId(int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt > 0)
    {
      mAudioSession = paramInt;
      return this;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" is not a valid session ID.");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
}
