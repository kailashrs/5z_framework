package android.renderscript;

public final class ScriptIntrinsicConvolve3x3
  extends ScriptIntrinsic
{
  private Allocation mInput;
  private final float[] mValues = new float[9];
  
  private ScriptIntrinsicConvolve3x3(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicConvolve3x3 create(RenderScript paramRenderScript, Element paramElement)
  {
    if ((!paramElement.isCompatible(Element.U8(paramRenderScript))) && (!paramElement.isCompatible(Element.U8_2(paramRenderScript))) && (!paramElement.isCompatible(Element.U8_3(paramRenderScript))) && (!paramElement.isCompatible(Element.U8_4(paramRenderScript))) && (!paramElement.isCompatible(Element.F32(paramRenderScript))) && (!paramElement.isCompatible(Element.F32_2(paramRenderScript))) && (!paramElement.isCompatible(Element.F32_3(paramRenderScript))) && (!paramElement.isCompatible(Element.F32_4(paramRenderScript)))) {
      throw new RSIllegalArgumentException("Unsupported element type.");
    }
    paramRenderScript = new ScriptIntrinsicConvolve3x3(paramRenderScript.nScriptIntrinsicCreate(1, paramElement.getID(paramRenderScript)), paramRenderScript);
    paramRenderScript.setCoefficients(new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F });
    return paramRenderScript;
  }
  
  public void forEach(Allocation paramAllocation)
  {
    forEach(0, (Allocation)null, paramAllocation, null);
  }
  
  public void forEach(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions)
  {
    forEach(0, (Allocation)null, paramAllocation, null, paramLaunchOptions);
  }
  
  public Script.FieldID getFieldID_Input()
  {
    return createFieldID(1, null);
  }
  
  public Script.KernelID getKernelID()
  {
    return createKernelID(0, 2, null, null);
  }
  
  public void setCoefficients(float[] paramArrayOfFloat)
  {
    FieldPacker localFieldPacker = new FieldPacker(36);
    for (int i = 0; i < mValues.length; i++)
    {
      mValues[i] = paramArrayOfFloat[i];
      localFieldPacker.addF32(mValues[i]);
    }
    setVar(0, localFieldPacker);
  }
  
  public void setInput(Allocation paramAllocation)
  {
    mInput = paramAllocation;
    setVar(1, paramAllocation);
  }
}
