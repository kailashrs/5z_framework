package android.media;

import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParserException;

class TtmlTrack
  extends SubtitleTrack
  implements TtmlNodeListener
{
  private static final String TAG = "TtmlTrack";
  private Long mCurrentRunID;
  private final TtmlParser mParser = new TtmlParser(this);
  private String mParsingData;
  private final TtmlRenderingWidget mRenderingWidget;
  private TtmlNode mRootNode;
  private final TreeSet<Long> mTimeEvents = new TreeSet();
  private final LinkedList<TtmlNode> mTtmlNodes = new LinkedList();
  
  TtmlTrack(TtmlRenderingWidget paramTtmlRenderingWidget, MediaFormat paramMediaFormat)
  {
    super(paramMediaFormat);
    mRenderingWidget = paramTtmlRenderingWidget;
    mParsingData = "";
  }
  
  private void addTimeEvents(TtmlNode paramTtmlNode)
  {
    mTimeEvents.add(Long.valueOf(mStartTimeMs));
    mTimeEvents.add(Long.valueOf(mEndTimeMs));
    for (int i = 0; i < mChildren.size(); i++) {
      addTimeEvents((TtmlNode)mChildren.get(i));
    }
  }
  
  private List<TtmlNode> getActiveNodes(long paramLong1, long paramLong2)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < mTtmlNodes.size(); i++)
    {
      TtmlNode localTtmlNode = (TtmlNode)mTtmlNodes.get(i);
      if (localTtmlNode.isActive(paramLong1, paramLong2)) {
        localArrayList.add(localTtmlNode);
      }
    }
    return localArrayList;
  }
  
  public TtmlCue getNextResult()
  {
    while (mTimeEvents.size() >= 2)
    {
      long l1 = ((Long)mTimeEvents.pollFirst()).longValue();
      long l2 = ((Long)mTimeEvents.first()).longValue();
      if (!getActiveNodes(l1, l2).isEmpty()) {
        return new TtmlCue(l1, l2, TtmlUtils.applySpacePolicy(TtmlUtils.extractText(mRootNode, l1, l2), false), TtmlUtils.extractTtmlFragment(mRootNode, l1, l2));
      }
    }
    return null;
  }
  
  public TtmlRenderingWidget getRenderingWidget()
  {
    return mRenderingWidget;
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
          localObject3 = new java/lang/IllegalStateException;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Run #");
          ((StringBuilder)localObject1).append(mCurrentRunID);
          ((StringBuilder)localObject1).append(" in progress.  Cannot process run #");
          ((StringBuilder)localObject1).append(paramLong);
          ((IllegalStateException)localObject3).<init>(((StringBuilder)localObject1).toString());
          throw ((Throwable)localObject3);
        }
        mCurrentRunID = Long.valueOf(paramLong);
        Object localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append(mParsingData);
        ((StringBuilder)localObject3).append((String)localObject1);
        mParsingData = ((StringBuilder)localObject3).toString();
        if (paramBoolean)
        {
          try
          {
            mParser.parse(mParsingData, mCurrentRunID.longValue());
          }
          catch (IOException localIOException)
          {
            localIOException.printStackTrace();
          }
          catch (XmlPullParserException localXmlPullParserException)
          {
            localXmlPullParserException.printStackTrace();
          }
          finishedRun(paramLong);
          mParsingData = "";
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
      Log.w("TtmlTrack", localStringBuilder.toString());
    }
  }
  
  public void onRootNodeParsed(TtmlNode paramTtmlNode)
  {
    mRootNode = paramTtmlNode;
    for (;;)
    {
      paramTtmlNode = getNextResult();
      if (paramTtmlNode == null) {
        break;
      }
      addCue(paramTtmlNode);
    }
    mRootNode = null;
    mTtmlNodes.clear();
    mTimeEvents.clear();
  }
  
  public void onTtmlNodeParsed(TtmlNode paramTtmlNode)
  {
    mTtmlNodes.addLast(paramTtmlNode);
    addTimeEvents(paramTtmlNode);
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
        Log.d("TtmlTrack", localStringBuilder.toString());
      }
      catch (IllegalStateException localIllegalStateException)
      {
        Log.d("TtmlTrack", "at (illegal state) the active cues are:");
      }
    }
    mRenderingWidget.setActiveCues(paramVector);
  }
}
