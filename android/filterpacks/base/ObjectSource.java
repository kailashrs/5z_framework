package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.format.ObjectFormat;

public class ObjectSource
  extends Filter
{
  private Frame mFrame;
  @GenerateFieldPort(name="object")
  private Object mObject;
  @GenerateFinalPort(hasDefault=true, name="format")
  private FrameFormat mOutputFormat = FrameFormat.unspecified();
  @GenerateFieldPort(hasDefault=true, name="repeatFrame")
  boolean mRepeatFrame = false;
  
  public ObjectSource(String paramString)
  {
    super(paramString);
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if ((paramString.equals("object")) && (mFrame != null))
    {
      mFrame.release();
      mFrame = null;
    }
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mFrame == null) {
      if (mObject != null)
      {
        MutableFrameFormat localMutableFrameFormat = ObjectFormat.fromObject(mObject, 1);
        mFrame = paramFilterContext.getFrameManager().newFrame(localMutableFrameFormat);
        mFrame.setObjectValue(mObject);
        mFrame.setTimestamp(-1L);
      }
      else
      {
        throw new NullPointerException("ObjectSource producing frame with no object set!");
      }
    }
    pushOutput("frame", mFrame);
    if (!mRepeatFrame) {
      closeOutputPort("frame");
    }
  }
  
  public void setupPorts()
  {
    addOutputPort("frame", mOutputFormat);
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    mFrame.release();
  }
}
