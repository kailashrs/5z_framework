package android.media;

import java.util.Vector;

class UnstyledTextExtractor
  implements Tokenizer.OnTokenListener
{
  Vector<TextTrackCueSpan> mCurrentLine = new Vector();
  long mLastTimestamp;
  StringBuilder mLine = new StringBuilder();
  Vector<TextTrackCueSpan[]> mLines = new Vector();
  
  UnstyledTextExtractor()
  {
    init();
  }
  
  private void init()
  {
    mLine.delete(0, mLine.length());
    mLines.clear();
    mCurrentLine.clear();
    mLastTimestamp = -1L;
  }
  
  public TextTrackCueSpan[][] getText()
  {
    if ((mLine.length() > 0) || (mCurrentLine.size() > 0)) {
      onLineEnd();
    }
    TextTrackCueSpan[][] arrayOfTextTrackCueSpan; = new TextTrackCueSpan[mLines.size()][];
    mLines.toArray(arrayOfTextTrackCueSpan;);
    init();
    return arrayOfTextTrackCueSpan;;
  }
  
  public void onData(String paramString)
  {
    mLine.append(paramString);
  }
  
  public void onEnd(String paramString) {}
  
  public void onLineEnd()
  {
    if (mLine.length() > 0)
    {
      mCurrentLine.add(new TextTrackCueSpan(mLine.toString(), mLastTimestamp));
      mLine.delete(0, mLine.length());
    }
    TextTrackCueSpan[] arrayOfTextTrackCueSpan = new TextTrackCueSpan[mCurrentLine.size()];
    mCurrentLine.toArray(arrayOfTextTrackCueSpan);
    mCurrentLine.clear();
    mLines.add(arrayOfTextTrackCueSpan);
  }
  
  public void onStart(String paramString1, String[] paramArrayOfString, String paramString2) {}
  
  public void onTimeStamp(long paramLong)
  {
    if ((mLine.length() > 0) && (paramLong != mLastTimestamp))
    {
      mCurrentLine.add(new TextTrackCueSpan(mLine.toString(), mLastTimestamp));
      mLine.delete(0, mLine.length());
    }
    mLastTimestamp = paramLong;
  }
}
