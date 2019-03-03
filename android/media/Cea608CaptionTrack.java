package android.media;

import java.util.Vector;

class Cea608CaptionTrack
  extends SubtitleTrack
{
  private final Cea608CCParser mCCParser;
  private final Cea608CCWidget mRenderingWidget;
  
  Cea608CaptionTrack(Cea608CCWidget paramCea608CCWidget, MediaFormat paramMediaFormat)
  {
    super(paramMediaFormat);
    mRenderingWidget = paramCea608CCWidget;
    mCCParser = new Cea608CCParser(mRenderingWidget);
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
