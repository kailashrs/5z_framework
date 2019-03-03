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

public class ImageSlicer
  extends Filter
{
  private int mInputHeight;
  private int mInputWidth;
  private Frame mOriginalFrame;
  private int mOutputHeight;
  private int mOutputWidth;
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
  
  public ImageSlicer(String paramString)
  {
    super(paramString);
  }
  
  private void calcOutputFormatForInput(Frame paramFrame)
  {
    mInputWidth = paramFrame.getFormat().getWidth();
    mInputHeight = paramFrame.getFormat().getHeight();
    mSliceWidth = ((mInputWidth + mXSlices - 1) / mXSlices);
    mSliceHeight = ((mInputHeight + mYSlices - 1) / mYSlices);
    mOutputWidth = (mSliceWidth + mPadSize * 2);
    mOutputHeight = (mSliceHeight + mPadSize * 2);
  }
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    if (mSliceIndex == 0)
    {
      mOriginalFrame = pullInput("image");
      calcOutputFormatForInput(mOriginalFrame);
    }
    Object localObject = mOriginalFrame.getFormat().mutableCopy();
    ((MutableFrameFormat)localObject).setDimensions(mOutputWidth, mOutputHeight);
    localObject = paramFilterContext.getFrameManager().newFrame((FrameFormat)localObject);
    if (mProgram == null) {
      mProgram = ShaderProgram.createIdentity(paramFilterContext);
    }
    int i = mSliceIndex;
    int j = mXSlices;
    int k = mSliceIndex / mXSlices;
    float f1 = (mSliceWidth * (i % j) - mPadSize) / mInputWidth;
    float f2 = (mSliceHeight * k - mPadSize) / mInputHeight;
    ((ShaderProgram)mProgram).setSourceRect(f1, f2, mOutputWidth / mInputWidth, mOutputHeight / mInputHeight);
    mProgram.process(mOriginalFrame, (Frame)localObject);
    mSliceIndex += 1;
    if (mSliceIndex == mXSlices * mYSlices)
    {
      mSliceIndex = 0;
      mOriginalFrame.release();
      setWaitsOnInputPort("image", true);
    }
    else
    {
      mOriginalFrame.retain();
      setWaitsOnInputPort("image", false);
    }
    pushOutput("image", (Frame)localObject);
    ((Frame)localObject).release();
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("image", ImageFormat.create(3, 3));
    addOutputBasedOnInput("image", "image");
  }
}
