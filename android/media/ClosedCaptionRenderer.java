package android.media;

import android.content.Context;

public class ClosedCaptionRenderer
  extends SubtitleController.Renderer
{
  private Cea608CCWidget mCCWidget;
  private final Context mContext;
  
  public ClosedCaptionRenderer(Context paramContext)
  {
    mContext = paramContext;
  }
  
  public SubtitleTrack createTrack(MediaFormat paramMediaFormat)
  {
    if ("text/cea-608".equals(paramMediaFormat.getString("mime")))
    {
      if (mCCWidget == null) {
        mCCWidget = new Cea608CCWidget(mContext);
      }
      return new Cea608CaptionTrack(mCCWidget, paramMediaFormat);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("No matching format: ");
    localStringBuilder.append(paramMediaFormat.toString());
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public boolean supports(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat.containsKey("mime")) {
      return "text/cea-608".equals(paramMediaFormat.getString("mime"));
    }
    return false;
  }
}
