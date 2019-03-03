package android.media;

import android.content.Context;

public class WebVttRenderer
  extends SubtitleController.Renderer
{
  private final Context mContext;
  private WebVttRenderingWidget mRenderingWidget;
  
  public WebVttRenderer(Context paramContext)
  {
    mContext = paramContext;
  }
  
  public SubtitleTrack createTrack(MediaFormat paramMediaFormat)
  {
    if (mRenderingWidget == null) {
      mRenderingWidget = new WebVttRenderingWidget(mContext);
    }
    return new WebVttTrack(mRenderingWidget, paramMediaFormat);
  }
  
  public boolean supports(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat.containsKey("mime")) {
      return paramMediaFormat.getString("mime").equals("text/vtt");
    }
    return false;
  }
}
