package android.media;

class TextTrackCueSpan
{
  boolean mEnabled;
  String mText;
  long mTimestampMs;
  
  TextTrackCueSpan(String paramString, long paramLong)
  {
    mTimestampMs = paramLong;
    mText = paramString;
    boolean bool;
    if (mTimestampMs < 0L) {
      bool = true;
    } else {
      bool = false;
    }
    mEnabled = bool;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof TextTrackCueSpan;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (TextTrackCueSpan)paramObject;
    bool1 = bool2;
    if (mTimestampMs == mTimestampMs)
    {
      bool1 = bool2;
      if (mText.equals(mText)) {
        bool1 = true;
      }
    }
    return bool1;
  }
}
