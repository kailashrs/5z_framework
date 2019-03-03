package android.media;

import android.content.Context;
import android.os.Handler;

public class SRTRenderer
  extends SubtitleController.Renderer
{
  private final Context mContext;
  private final Handler mEventHandler;
  private final boolean mRender;
  private WebVttRenderingWidget mRenderingWidget;
  
  public SRTRenderer(Context paramContext)
  {
    this(paramContext, null);
  }
  
  SRTRenderer(Context paramContext, Handler paramHandler)
  {
    mContext = paramContext;
    boolean bool;
    if (paramHandler == null) {
      bool = true;
    } else {
      bool = false;
    }
    mRender = bool;
    mEventHandler = paramHandler;
  }
  
  public SubtitleTrack createTrack(MediaFormat paramMediaFormat)
  {
    if ((mRender) && (mRenderingWidget == null)) {
      mRenderingWidget = new WebVttRenderingWidget(mContext);
    }
    if (mRender) {
      return new SRTTrack(mRenderingWidget, paramMediaFormat);
    }
    return new SRTTrack(mEventHandler, paramMediaFormat);
  }
  
  public boolean supports(MediaFormat paramMediaFormat)
  {
    boolean bool1 = paramMediaFormat.containsKey("mime");
    boolean bool2 = false;
    if (bool1)
    {
      if (!paramMediaFormat.getString("mime").equals("application/x-subrip")) {
        return false;
      }
      boolean bool3 = mRender;
      if (paramMediaFormat.getInteger("is-timed-text", 0) == 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      if (bool3 == bool1) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
}
