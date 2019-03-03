package android.media;

import java.util.Arrays;

class TextTrackCue
  extends SubtitleTrack.Cue
{
  static final int ALIGNMENT_END = 202;
  static final int ALIGNMENT_LEFT = 203;
  static final int ALIGNMENT_MIDDLE = 200;
  static final int ALIGNMENT_RIGHT = 204;
  static final int ALIGNMENT_START = 201;
  private static final String TAG = "TTCue";
  static final int WRITING_DIRECTION_HORIZONTAL = 100;
  static final int WRITING_DIRECTION_VERTICAL_LR = 102;
  static final int WRITING_DIRECTION_VERTICAL_RL = 101;
  int mAlignment = 200;
  boolean mAutoLinePosition;
  String mId = "";
  Integer mLinePosition = null;
  TextTrackCueSpan[][] mLines = null;
  boolean mPauseOnExit = false;
  TextTrackRegion mRegion = null;
  String mRegionId = "";
  int mSize = 100;
  boolean mSnapToLines = true;
  String[] mStrings;
  int mTextPosition = 50;
  int mWritingDirection = 100;
  
  TextTrackCue() {}
  
  public StringBuilder appendLinesToBuilder(StringBuilder paramStringBuilder)
  {
    if (mLines == null)
    {
      paramStringBuilder.append("null");
    }
    else
    {
      paramStringBuilder.append("[");
      TextTrackCueSpan[][] arrayOfTextTrackCueSpan = mLines;
      int i = arrayOfTextTrackCueSpan.length;
      int j = 1;
      for (int k = 0; k < i; k++)
      {
        TextTrackCueSpan[] arrayOfTextTrackCueSpan1 = arrayOfTextTrackCueSpan[k];
        if (j == 0) {
          paramStringBuilder.append(", ");
        }
        if (arrayOfTextTrackCueSpan1 == null)
        {
          paramStringBuilder.append("null");
        }
        else
        {
          paramStringBuilder.append("\"");
          int m = arrayOfTextTrackCueSpan1.length;
          long l1 = -1L;
          int n = 1;
          j = 0;
          while (j < m)
          {
            TextTrackCueSpan localTextTrackCueSpan = arrayOfTextTrackCueSpan1[j];
            if (n == 0) {
              paramStringBuilder.append(" ");
            }
            long l2 = l1;
            if (mTimestampMs != l1)
            {
              paramStringBuilder.append("<");
              paramStringBuilder.append(WebVttParser.timeToString(mTimestampMs));
              paramStringBuilder.append(">");
              l2 = mTimestampMs;
            }
            paramStringBuilder.append(mText);
            n = 0;
            j++;
            l1 = l2;
          }
          paramStringBuilder.append("\"");
        }
        j = 0;
      }
      paramStringBuilder.append("]");
    }
    return paramStringBuilder;
  }
  
  public StringBuilder appendStringsToBuilder(StringBuilder paramStringBuilder)
  {
    if (mStrings == null)
    {
      paramStringBuilder.append("null");
    }
    else
    {
      paramStringBuilder.append("[");
      int i = 1;
      for (String str : mStrings)
      {
        if (i == 0) {
          paramStringBuilder.append(", ");
        }
        if (str == null)
        {
          paramStringBuilder.append("null");
        }
        else
        {
          paramStringBuilder.append("\"");
          paramStringBuilder.append(str);
          paramStringBuilder.append("\"");
        }
        i = 0;
      }
      paramStringBuilder.append("]");
    }
    return paramStringBuilder;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof TextTrackCue)) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    try
    {
      paramObject = (TextTrackCue)paramObject;
      boolean bool1;
      if ((mId.equals(mId)) && (mPauseOnExit == mPauseOnExit) && (mWritingDirection == mWritingDirection) && (mRegionId.equals(mRegionId)) && (mSnapToLines == mSnapToLines) && (mAutoLinePosition == mAutoLinePosition) && ((mAutoLinePosition) || ((mLinePosition != null) && (mLinePosition.equals(mLinePosition))) || ((mLinePosition == null) && (mLinePosition == null))) && (mTextPosition == mTextPosition) && (mSize == mSize) && (mAlignment == mAlignment) && (mLines.length == mLines.length)) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      if (bool1 == true) {
        for (int i = 0; i < mLines.length; i++)
        {
          boolean bool2 = Arrays.equals(mLines[i], mLines[i]);
          if (!bool2) {
            return false;
          }
        }
      }
      return bool1;
    }
    catch (IncompatibleClassChangeError paramObject) {}
    return false;
  }
  
  public int hashCode()
  {
    return toString().hashCode();
  }
  
  public void onTime(long paramLong)
  {
    TextTrackCueSpan[][] arrayOfTextTrackCueSpan = mLines;
    int i = arrayOfTextTrackCueSpan.length;
    for (int j = 0; j < i; j++) {
      for (TextTrackCueSpan localTextTrackCueSpan : arrayOfTextTrackCueSpan[j])
      {
        boolean bool;
        if (paramLong >= mTimestampMs) {
          bool = true;
        } else {
          bool = false;
        }
        mEnabled = bool;
      }
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(WebVttParser.timeToString(mStartTimeMs));
    localStringBuilder.append(" --> ");
    localStringBuilder.append(WebVttParser.timeToString(mEndTimeMs));
    localStringBuilder.append(" {id:\"");
    localStringBuilder.append(mId);
    localStringBuilder.append("\", pauseOnExit:");
    localStringBuilder.append(mPauseOnExit);
    localStringBuilder.append(", direction:");
    Object localObject;
    if (mWritingDirection == 100) {
      localObject = "horizontal";
    } else if (mWritingDirection == 102) {
      localObject = "vertical_lr";
    } else if (mWritingDirection == 101) {
      localObject = "vertical_rl";
    } else {
      localObject = "INVALID";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", regionId:\"");
    localStringBuilder.append(mRegionId);
    localStringBuilder.append("\", snapToLines:");
    localStringBuilder.append(mSnapToLines);
    localStringBuilder.append(", linePosition:");
    if (mAutoLinePosition) {
      localObject = "auto";
    } else {
      localObject = mLinePosition;
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", textPosition:");
    localStringBuilder.append(mTextPosition);
    localStringBuilder.append(", size:");
    localStringBuilder.append(mSize);
    localStringBuilder.append(", alignment:");
    if (mAlignment == 202) {
      localObject = "end";
    } else if (mAlignment == 203) {
      localObject = "left";
    } else if (mAlignment == 200) {
      localObject = "middle";
    } else if (mAlignment == 204) {
      localObject = "right";
    } else if (mAlignment == 201) {
      localObject = "start";
    } else {
      localObject = "INVALID";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(", text:");
    appendStringsToBuilder(localStringBuilder).append("}");
    return localStringBuilder.toString();
  }
}
