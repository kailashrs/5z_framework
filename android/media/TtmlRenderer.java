package android.media;

import android.content.Context;

public class TtmlRenderer
  extends SubtitleController.Renderer
{
  private static final String MEDIA_MIMETYPE_TEXT_TTML = "application/ttml+xml";
  private final Context mContext;
  private TtmlRenderingWidget mRenderingWidget;
  
  public TtmlRenderer(Context paramContext)
  {
    mContext = paramContext;
  }
  
  public SubtitleTrack createTrack(MediaFormat paramMediaFormat)
  {
    if (mRenderingWidget == null) {
      mRenderingWidget = new TtmlRenderingWidget(mContext);
    }
    return new TtmlTrack(mRenderingWidget, paramMediaFormat);
  }
  
  public boolean supports(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat.containsKey("mime")) {
      return paramMediaFormat.getString("mime").equals("application/ttml+xml");
    }
    return false;
  }
}
