package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.format.ImageFormat;

public class GLTextureSource
  extends Filter
{
  private Frame mFrame;
  @GenerateFieldPort(name="height")
  private int mHeight;
  @GenerateFieldPort(hasDefault=true, name="repeatFrame")
  private boolean mRepeatFrame = false;
  @GenerateFieldPort(name="texId")
  private int mTexId;
  @GenerateFieldPort(hasDefault=true, name="timestamp")
  private long mTimestamp = -1L;
  @GenerateFieldPort(name="width")
  private int mWidth;
  
  public GLTextureSource(String paramString)
  {
    super(paramString);
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mFrame != null)
    {
      mFrame.release();
      mFrame = null;
    }
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mFrame == null)
    {
      MutableFrameFormat localMutableFrameFormat = ImageFormat.create(mWidth, mHeight, 3, 3);
      mFrame = paramFilterContext.getFrameManager().newBoundFrame(localMutableFrameFormat, 100, mTexId);
      mFrame.setTimestamp(mTimestamp);
    }
    pushOutput("frame", mFrame);
    if (!mRepeatFrame) {
      closeOutputPort("frame");
    }
  }
  
  public void setupPorts()
  {
    addOutputPort("frame", ImageFormat.create(3, 3));
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mFrame != null) {
      mFrame.release();
    }
  }
}
