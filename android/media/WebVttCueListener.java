package android.media;

abstract interface WebVttCueListener
{
  public abstract void onCueParsed(TextTrackCue paramTextTrackCue);
  
  public abstract void onRegionParsed(TextTrackRegion paramTextTrackRegion);
}
