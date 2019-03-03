package android.filterfw.core;

import java.lang.reflect.Field;

public class FieldPort
  extends InputPort
{
  protected Field mField;
  protected boolean mHasFrame;
  protected Object mValue;
  protected boolean mValueWaiting = false;
  
  public FieldPort(Filter paramFilter, String paramString, Field paramField, boolean paramBoolean)
  {
    super(paramFilter, paramString);
    mField = paramField;
    mHasFrame = paramBoolean;
  }
  
  public boolean acceptsFrame()
  {
    try
    {
      boolean bool = mValueWaiting;
      return bool ^ true;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void clear() {}
  
  public Object getTarget()
  {
    try
    {
      Object localObject = mField.get(mFilter);
      return localObject;
    }
    catch (IllegalAccessException localIllegalAccessException) {}
    return null;
  }
  
  public boolean hasFrame()
  {
    try
    {
      boolean bool = mHasFrame;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Frame pullFrame()
  {
    try
    {
      RuntimeException localRuntimeException = new java/lang/RuntimeException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Cannot pull frame on ");
      localStringBuilder.append(this);
      localStringBuilder.append("!");
      localRuntimeException.<init>(localStringBuilder.toString());
      throw localRuntimeException;
    }
    finally {}
  }
  
  public void pushFrame(Frame paramFrame)
  {
    setFieldFrame(paramFrame, false);
  }
  
  protected void setFieldFrame(Frame paramFrame, boolean paramBoolean)
  {
    try
    {
      assertPortIsOpen();
      checkFrameType(paramFrame, paramBoolean);
      paramFrame = paramFrame.getObjectValue();
      if (((paramFrame == null) && (mValue != null)) || (!paramFrame.equals(mValue)))
      {
        mValue = paramFrame;
        mValueWaiting = true;
      }
      mHasFrame = true;
      return;
    }
    finally {}
  }
  
  public void setFrame(Frame paramFrame)
  {
    setFieldFrame(paramFrame, true);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("field ");
    localStringBuilder.append(super.toString());
    return localStringBuilder.toString();
  }
  
  public void transfer(FilterContext paramFilterContext)
  {
    try
    {
      boolean bool = mValueWaiting;
      if (bool) {
        try
        {
          mField.set(mFilter, mValue);
          mValueWaiting = false;
          if (paramFilterContext != null) {
            mFilter.notifyFieldPortValueUpdated(mName, paramFilterContext);
          }
        }
        catch (IllegalAccessException paramFilterContext)
        {
          paramFilterContext = new java/lang/RuntimeException;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Access to field '");
          localStringBuilder.append(mField.getName());
          localStringBuilder.append("' was denied!");
          paramFilterContext.<init>(localStringBuilder.toString());
          throw paramFilterContext;
        }
      }
      return;
    }
    finally {}
  }
}
