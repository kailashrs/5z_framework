package android.media;

import java.util.Vector;

class Cea708CaptionTrack
  extends SubtitleTrack
{
  private final Cea708CCParser mCCParser;
  private final Cea708CCWidget mRenderingWidget;
  
  Cea708CaptionTrack(Cea708CCWidget paramCea708CCWidget, MediaFormat paramMediaFormat)
  {
    super(paramMediaFormat);
    mRenderingWidget = paramCea708CCWidget;
    mCCParser = new Cea708CCParser(mRenderingWidget);
  }
  
  public SubtitleTrack.RenderingWidget getRenderingWidget()
  {
    return mRenderingWidget;
  }
  
  public void onData(byte[] paramArrayOfByte, boolean paramBoolean, long paramLong)
  {
    mCCParser.parse(paramArrayOfByte);
  }
  
  public void updateView(Vector<SubtitleTrack.Cue> paramVector) {}
}
