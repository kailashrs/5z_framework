package android.media;

class TtmlCue
  extends SubtitleTrack.Cue
{
  public String mText;
  public String mTtmlFragment;
  
  public TtmlCue(long paramLong1, long paramLong2, String paramString1, String paramString2)
  {
    mStartTimeMs = paramLong1;
    mEndTimeMs = paramLong2;
    mText = paramString1;
    mTtmlFragment = paramString2;
  }
}
