package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.FrameManager;
import android.filterfw.core.Program;
import android.filterfw.format.ImageFormat;
import java.lang.reflect.Field;

public abstract class ImageCombineFilter
  extends Filter
{
  protected int mCurrentTarget = 0;
  protected String[] mInputNames;
  protected String mOutputName;
  protected String mParameterName;
  protected Program mProgram;
  
  public ImageCombineFilter(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3)
  {
    super(paramString1);
    mInputNames = paramArrayOfString;
    mOutputName = paramString2;
    mParameterName = paramString3;
  }
  
  private void assertAllInputTargetsMatch()
  {
    Object localObject = mInputNames;
    int i = 0;
    int j = getInputFormat(localObject[0]).getTarget();
    localObject = mInputNames;
    int k = localObject.length;
    while (i < k) {
      if (j == getInputFormat(localObject[i]).getTarget())
      {
        i++;
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Type mismatch of input formats in filter ");
        ((StringBuilder)localObject).append(this);
        ((StringBuilder)localObject).append(". All input frames must have the same target!");
        throw new RuntimeException(((StringBuilder)localObject).toString());
      }
    }
  }
  
  protected abstract Program getNativeProgram(FilterContext paramFilterContext);
  
  public FrameFormat getOutputFormat(String paramString, FrameFormat paramFrameFormat)
  {
    return paramFrameFormat;
  }
  
  protected abstract Program getShaderProgram(FilterContext paramFilterContext);
  
  public void process(FilterContext paramFilterContext)
  {
    Frame[] arrayOfFrame = new Frame[mInputNames.length];
    Object localObject = mInputNames;
    int i = localObject.length;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      arrayOfFrame[j] = pullInput(localObject[k]);
      k++;
      j++;
    }
    localObject = paramFilterContext.getFrameManager().newFrame(arrayOfFrame[0].getFormat());
    updateProgramWithTarget(arrayOfFrame[0].getFormat().getTarget(), paramFilterContext);
    mProgram.process(arrayOfFrame, (Frame)localObject);
    pushOutput(mOutputName, (Frame)localObject);
    ((Frame)localObject).release();
  }
  
  public void setupPorts()
  {
    if (mParameterName != null) {
      try
      {
        Field localField = ImageCombineFilter.class.getDeclaredField("mProgram");
        addProgramPort(mParameterName, mParameterName, localField, Float.TYPE, false);
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        throw new RuntimeException("Internal Error: mProgram field not found!");
      }
    }
    String[] arrayOfString = mInputNames;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      addMaskedInputPort(arrayOfString[j], ImageFormat.create(3));
    }
    addOutputBasedOnInput(mOutputName, mInputNames[0]);
  }
  
  protected void updateProgramWithTarget(int paramInt, FilterContext paramFilterContext)
  {
    if (paramInt != mCurrentTarget)
    {
      switch (paramInt)
      {
      default: 
        mProgram = null;
        break;
      case 3: 
        mProgram = getShaderProgram(paramFilterContext);
        break;
      case 2: 
        mProgram = getNativeProgram(paramFilterContext);
      }
      if (mProgram != null)
      {
        initProgramInputs(mProgram, paramFilterContext);
        mCurrentTarget = paramInt;
      }
      else
      {
        paramFilterContext = new StringBuilder();
        paramFilterContext.append("Could not create a program for image filter ");
        paramFilterContext.append(this);
        paramFilterContext.append("!");
        throw new RuntimeException(paramFilterContext.toString());
      }
    }
  }
}
