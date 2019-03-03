package android.media;

import java.util.ArrayList;
import java.util.List;

class TtmlNode
{
  public final String mAttributes;
  public final List<TtmlNode> mChildren = new ArrayList();
  public final long mEndTimeMs;
  public final String mName;
  public final TtmlNode mParent;
  public final long mRunId;
  public final long mStartTimeMs;
  public final String mText;
  
  public TtmlNode(String paramString1, String paramString2, String paramString3, long paramLong1, long paramLong2, TtmlNode paramTtmlNode, long paramLong3)
  {
    mName = paramString1;
    mAttributes = paramString2;
    mText = paramString3;
    mStartTimeMs = paramLong1;
    mEndTimeMs = paramLong2;
    mParent = paramTtmlNode;
    mRunId = paramLong3;
  }
  
  public boolean isActive(long paramLong1, long paramLong2)
  {
    boolean bool;
    if ((mEndTimeMs > paramLong1) && (mStartTimeMs < paramLong2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
