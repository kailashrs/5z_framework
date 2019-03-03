package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;

public class ImageStitcher
  extends Filter
{
  private int mImageHeight;
  private int mImageWidth;
  private int mInputHeight;
  private int mInputWidth;
  private Frame mOutputFrame;
  @GenerateFieldPort(name="padSize")
  private int mPadSize;
  private Program mProgram;
  private int mSliceHeight;
  private int mSliceIndex = 0;
  private int mSliceWidth;
  @GenerateFieldPort(name="xSlices")
  private int mXSlices;
  @GenerateFieldPort(name="ySlices")
  private int mYSlices;
  
  public ImageStitcher(String paramString)
  {
    super(paramString);
  }
  
  private FrameFormat calcOutputFormatForInput(FrameFormat paramFrameFormat)
  {
    MutableFrameFormat localMutableFrameFormat = paramFrameFormat.mutableCopy();
    mInputWidth = paramFrameFormat.getWidth();
    mInputHeight = paramFrameFormat.getHeight();
    mSliceWidth = (mInputWidth - mPadSize * 2);
    mSliceHeight = (mInputHeight - 2 * mPadSize);
    mImageWidth = (mSliceWidth * mXSlices);
    mImageHeight = (mSliceHeight * mYSlices);
    localMutableFrameFormat.setDimensions(mImageWidth, mImageHeight);
    return localMutableFrameFormat;
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    Frame localFrame = pullInput("image");
    FrameFormat localFrameFormat = localFrame.getFormat();
    if (mSliceIndex == 0) {
      mOutputFrame = paramFilterContext.getFrameManager().newFrame(calcOutputFormatForInput(localFrameFormat));
    } else {
      if ((localFrameFormat.getWidth() != mInputWidth) || (localFrameFormat.getHeight() != mInputHeight)) {
        break label303;
      }
    }
    if (mProgram == null) {
      mProgram = ShaderProgram.createIdentity(paramFilterContext);
    }
    float f1 = mPadSize / mInputWidth;
    float f2 = mPadSize / mInputHeight;
    int i = mSliceIndex % mXSlices * mSliceWidth;
    int j = mSliceIndex / mXSlices * mSliceHeight;
    float f3 = Math.min(mSliceWidth, mImageWidth - i);
    float f4 = Math.min(mSliceHeight, mImageHeight - j);
    ((ShaderProgram)mProgram).setSourceRect(f1, f2, f3 / mInputWidth, f4 / mInputHeight);
    ((ShaderProgram)mProgram).setTargetRect(i / mImageWidth, j / mImageHeight, f3 / mImageWidth, f4 / mImageHeight);
    mProgram.process(localFrame, mOutputFrame);
    mSliceIndex += 1;
    if (mSliceIndex == mXSlices * mYSlices)
    {
      pushOutput("image", mOutputFrame);
      mOutputFrame.release();
      mSliceIndex = 0;
    }
    return;
    label303:
    throw new RuntimeException("Image size should not change.");
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3, 3));
    addOutputBasedOnInput("image", "image");
  }
}
