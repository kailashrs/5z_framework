package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.format.ImageFormat;
import android.graphics.Bitmap;

public class BitmapSource
  extends Filter
{
  @GenerateFieldPort(name="bitmap")
  private Bitmap mBitmap;
  private Frame mImageFrame;
  @GenerateFieldPort(hasDefault=true, name="recycleBitmap")
  private boolean mRecycleBitmap = true;
  @GenerateFieldPort(hasDefault=true, name="repeatFrame")
  boolean mRepeatFrame = false;
  private int mTarget;
  @GenerateFieldPort(name="target")
  String mTargetString;
  
  public BitmapSource(String paramString)
  {
    super(paramString);
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (((paramString.equals("bitmap")) || (paramString.equals("target"))) && (mImageFrame != null))
    {
      mImageFrame.release();
      mImageFrame = null;
    }
  }
  
  public void loadImage(FilterContext paramFilterContext)
  {
    mTarget = FrameFormat.readTargetString(mTargetString);
    MutableFrameFormat localMutableFrameFormat = ImageFormat.create(mBitmap.getWidth(), mBitmap.getHeight(), 3, mTarget);
    mImageFrame = paramFilterContext.getFrameManager().newFrame(localMutableFrameFormat);
    mImageFrame.setBitmap(mBitmap);
    mImageFrame.setTimestamp(-1L);
    if (mRecycleBitmap) {
      mBitmap.recycle();
    }
    mBitmap = null;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mImageFrame == null) {
      loadImage(paramFilterContext);
    }
    pushOutput("image", mImageFrame);
    if (!mRepeatFrame) {
      closeOutputPort("image");
    }
  }
  
  public void setupPorts()
  {
    addOutputPort("image", ImageFormat.create(3, 0));
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mImageFrame != null)
    {
      mImageFrame.release();
      mImageFrame = null;
    }
  }
}
