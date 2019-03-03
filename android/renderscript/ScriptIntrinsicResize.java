package android.renderscript;

public final class ScriptIntrinsicResize
  extends ScriptIntrinsic
{
  private Allocation mInput;
  
  private ScriptIntrinsicResize(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicResize create(RenderScript paramRenderScript)
  {
    return new ScriptIntrinsicResize(paramRenderScript.nScriptIntrinsicCreate(12, 0L), paramRenderScript);
  }
  
  public void forEach_bicubic(Allocation paramAllocation)
  {
    if (paramAllocation != mInput)
    {
      forEach_bicubic(paramAllocation, null);
      return;
    }
    throw new RSIllegalArgumentException("Output cannot be same as Input.");
  }
  
  public void forEach_bicubic(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions)
  {
    forEach(0, (Allocation)null, paramAllocation, null, paramLaunchOptions);
  }
  
  public Script.FieldID getFieldID_Input()
  {
    return createFieldID(0, null);
  }
  
  public Script.KernelID getKernelID_bicubic()
  {
    return createKernelID(0, 2, null, null);
  }
  
  public void setInput(Allocation paramAllocation)
  {
    Element localElement = paramAllocation.getElement();
    if ((!localElement.isCompatible(Element.U8(mRS))) && (!localElement.isCompatible(Element.U8_2(mRS))) && (!localElement.isCompatible(Element.U8_3(mRS))) && (!localElement.isCompatible(Element.U8_4(mRS))) && (!localElement.isCompatible(Element.F32(mRS))) && (!localElement.isCompatible(Element.F32_2(mRS))) && (!localElement.isCompatible(Element.F32_3(mRS))) && (!localElement.isCompatible(Element.F32_4(mRS)))) {
      throw new RSIllegalArgumentException("Unsupported element type.");
    }
    mInput = paramAllocation;
    setVar(0, paramAllocation);
  }
}
