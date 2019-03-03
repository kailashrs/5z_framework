package android.filterfw.core;

public class ProgramVariable
{
  private Program mProgram;
  private String mVarName;
  
  public ProgramVariable(Program paramProgram, String paramString)
  {
    mProgram = paramProgram;
    mVarName = paramString;
  }
  
  public Program getProgram()
  {
    return mProgram;
  }
  
  public Object getValue()
  {
    if (mProgram != null) {
      return mProgram.getHostValue(mVarName);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Attempting to get program variable '");
    localStringBuilder.append(mVarName);
    localStringBuilder.append("' but the program is null!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public String getVariableName()
  {
    return mVarName;
  }
  
  public void setValue(Object paramObject)
  {
    if (mProgram != null)
    {
      mProgram.setHostValue(mVarName, paramObject);
      return;
    }
    paramObject = new StringBuilder();
    paramObject.append("Attempting to set program variable '");
    paramObject.append(mVarName);
    paramObject.append("' but the program is null!");
    throw new RuntimeException(paramObject.toString());
  }
}
