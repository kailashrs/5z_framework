package android.media;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

class WebVttTrack
  extends SubtitleTrack
  implements WebVttCueListener
{
  private static final String TAG = "WebVttTrack";
  private Long mCurrentRunID;
  private final UnstyledTextExtractor mExtractor = new UnstyledTextExtractor();
  private final WebVttParser mParser = new WebVttParser(this);
  private final Map<String, TextTrackRegion> mRegions = new HashMap();
  private final WebVttRenderingWidget mRenderingWidget;
  private final Vector<Long> mTimestamps = new Vector();
  private final Tokenizer mTokenizer = new Tokenizer(mExtractor);
  
  WebVttTrack(WebVttRenderingWidget paramWebVttRenderingWidget, MediaFormat paramMediaFormat)
  {
    super(paramMediaFormat);
    mRenderingWidget = paramWebVttRenderingWidget;
  }
  
  public WebVttRenderingWidget getRenderingWidget()
  {
    return mRenderingWidget;
  }
  
  public void onCueParsed(TextTrackCue paramTextTrackCue)
  {
    synchronized (mParser)
    {
      if (mRegionId.length() != 0) {
        mRegion = ((TextTrackRegion)mRegions.get(mRegionId));
      }
      StringBuilder localStringBuilder;
      if (DEBUG)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("adding cue ");
        localStringBuilder.append(paramTextTrackCue);
        Log.v("WebVttTrack", localStringBuilder.toString());
      }
      mTokenizer.reset();
      Object localObject1 = mStrings;
      int i = localObject1.length;
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        localStringBuilder = localObject1[k];
        mTokenizer.tokenize(localStringBuilder);
      }
      mLines = mExtractor.getText();
      if (DEBUG)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder = paramTextTrackCue.appendStringsToBuilder(localStringBuilder);
        localStringBuilder.append(" simplified to: ");
        Log.v("WebVttTrack", paramTextTrackCue.appendLinesToBuilder(localStringBuilder).toString());
      }
      localObject1 = mLines;
      int m = localObject1.length;
      for (k = 0; k < m; k++) {
        for (Object localObject2 : localObject1[k]) {
          if ((mTimestampMs > mStartTimeMs) && (mTimestampMs < mEndTimeMs) && (!mTimestamps.contains(Long.valueOf(mTimestampMs)))) {
            mTimestamps.add(Long.valueOf(mTimestampMs));
          }
        }
      }
      if (mTimestamps.size() > 0)
      {
        mInnerTimesMs = new long[mTimestamps.size()];
        for (k = j; k < mTimestamps.size(); k++) {
          mInnerTimesMs[k] = ((Long)mTimestamps.get(k)).longValue();
        }
        mTimestamps.clear();
      }
      else
      {
        mInnerTimesMs = null;
      }
      mRunID = mCurrentRunID.longValue();
      addCue(paramTextTrackCue);
      return;
    }
  }
  
  public void onData(byte[] arg1, boolean paramBoolean, long paramLong)
  {
    try
    {
      Object localObject1 = new java/lang/String;
      ((String)localObject1).<init>(???, "UTF-8");
      synchronized (mParser)
      {
        if ((mCurrentRunID != null) && (paramLong != mCurrentRunID.longValue()))
        {
          IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Run #");
          ((StringBuilder)localObject1).append(mCurrentRunID);
          ((StringBuilder)localObject1).append(" in progress.  Cannot process run #");
          ((StringBuilder)localObject1).append(paramLong);
          localIllegalStateException.<init>(((StringBuilder)localObject1).toString());
          throw localIllegalStateException;
        }
        mCurrentRunID = Long.valueOf(paramLong);
        mParser.parse((String)localObject1);
        if (paramBoolean)
        {
          finishedRun(paramLong);
          mParser.eos();
          mRegions.clear();
          mCurrentRunID = null;
        }
      }
      StringBuilder localStringBuilder;
      return;
    }
    catch (UnsupportedEncodingException ???)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("subtitle data is not UTF-8 encoded: ");
      localStringBuilder.append(???);
      Log.w("WebVttTrack", localStringBuilder.toString());
    }
  }
  
  public void onRegionParsed(TextTrackRegion paramTextTrackRegion)
  {
    synchronized (mParser)
    {
      mRegions.put(mId, paramTextTrackRegion);
      return;
    }
  }
  
  public void updateView(Vector<SubtitleTrack.Cue> paramVector)
  {
    if (!mVisible) {
      return;
    }
    if ((DEBUG) && (mTimeProvider != null)) {
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("at ");
        localStringBuilder.append(mTimeProvider.getCurrentTimeUs(false, true) / 1000L);
        localStringBuilder.append(" ms the active cues are:");
        Log.d("WebVttTrack", localStringBuilder.toString());
      }
      catch (IllegalStateException localIllegalStateException)
      {
        Log.d("WebVttTrack", "at (illegal state) the active cues are:");
      }
    }
    if (mRenderingWidget != null) {
      mRenderingWidget.setActiveCues(paramVector);
    }
  }
}
