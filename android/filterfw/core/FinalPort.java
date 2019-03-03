package android.filterfw.core;

import java.lang.reflect.Field;

public class FinalPort
  extends FieldPort
{
  public FinalPort(Filter paramFilter, String paramString, Field paramField, boolean paramBoolean)
  {
    super(paramFilter, paramString, paramField, paramBoolean);
  }
  
  protected void setFieldFrame(Frame paramFrame, boolean paramBoolean)
  {
    try
    {
      assertPortIsOpen();
      checkFrameType(paramFrame, paramBoolean);
      if (mFilter.getStatus() == 0)
      {
        super.setFieldFrame(paramFrame, paramBoolean);
        super.transfer(null);
        return;
      }
      paramFrame = new java/lang/RuntimeException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Attempting to modify ");
      localStringBuilder.append(this);
      localStringBuilder.append("!");
      paramFrame.<init>(localStringBuilder.toString());
      throw paramFrame;
    }
    finally {}
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("final ");
    localStringBuilder.append(super.toString());
    return localStringBuilder.toString();
  }
}
