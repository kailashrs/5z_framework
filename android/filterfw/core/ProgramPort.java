package android.filterfw.core;

import java.lang.reflect.Field;

public class ProgramPort
  extends FieldPort
{
  protected String mVarName;
  
  public ProgramPort(Filter paramFilter, String paramString1, String paramString2, Field paramField, boolean paramBoolean)
  {
    super(paramFilter, paramString1, paramField, paramBoolean);
    mVarName = paramString2;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Program ");
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
          paramFilterContext = mField.get(mFilter);
          if (paramFilterContext != null)
          {
            ((Program)paramFilterContext).setHostValue(mVarName, mValue);
            mValueWaiting = false;
          }
        }
        catch (ClassCastException paramFilterContext)
        {
          localObject = new java/lang/RuntimeException;
          paramFilterContext = new java/lang/StringBuilder;
          paramFilterContext.<init>();
          paramFilterContext.append("Non Program field '");
          paramFilterContext.append(mField.getName());
          paramFilterContext.append("' annotated with ProgramParameter!");
          ((RuntimeException)localObject).<init>(paramFilterContext.toString());
          throw ((Throwable)localObject);
        }
        catch (IllegalAccessException paramFilterContext)
        {
          paramFilterContext = new java/lang/RuntimeException;
          Object localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Access to program field '");
          ((StringBuilder)localObject).append(mField.getName());
          ((StringBuilder)localObject).append("' was denied!");
          paramFilterContext.<init>(((StringBuilder)localObject).toString());
          throw paramFilterContext;
        }
      }
      return;
    }
    finally {}
  }
}
