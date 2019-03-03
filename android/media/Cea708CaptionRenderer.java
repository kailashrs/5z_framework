package android.media;

import android.content.Context;

public class Cea708CaptionRenderer
  extends SubtitleController.Renderer
{
  private Cea708CCWidget mCCWidget;
  private final Context mContext;
  
  public Cea708CaptionRenderer(Context paramContext)
  {
    mContext = paramContext;
  }
  
  public SubtitleTrack createTrack(MediaFormat paramMediaFormat)
  {
    if ("text/cea-708".equals(paramMediaFormat.getString("mime")))
    {
      if (mCCWidget == null) {
        mCCWidget = new Cea708CCWidget(mContext);
      }
      return new Cea708CaptionTrack(mCCWidget, paramMediaFormat);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("No matching format: ");
    localStringBuilder.append(paramMediaFormat.toString());
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public boolean supports(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat.containsKey("mime")) {
      return "text/cea-708".equals(paramMediaFormat.getString("mime"));
    }
    return false;
  }
}
