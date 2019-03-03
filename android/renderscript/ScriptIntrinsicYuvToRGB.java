package android.renderscript;

public final class ScriptIntrinsicYuvToRGB
  extends ScriptIntrinsic
{
  private Allocation mInput;
  
  ScriptIntrinsicYuvToRGB(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicYuvToRGB create(RenderScript paramRenderScript, Element paramElement)
  {
    return new ScriptIntrinsicYuvToRGB(paramRenderScript.nScriptIntrinsicCreate(6, paramElement.getID(paramRenderScript)), paramRenderScript);
  }
  
  public void forEach(Allocation paramAllocation)
  {
    forEach(0, (Allocation)null, paramAllocation, null);
  }
  
  public Script.FieldID getFieldID_Input()
  {
    return createFieldID(0, null);
  }
  
  public Script.KernelID getKernelID()
  {
    return createKernelID(0, 2, null, null);
  }
  
  public void setInput(Allocation paramAllocation)
  {
    mInput = paramAllocation;
    setVar(0, paramAllocation);
  }
}
