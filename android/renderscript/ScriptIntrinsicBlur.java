package android.renderscript;

public final class ScriptIntrinsicBlur
  extends ScriptIntrinsic
{
  private Allocation mInput;
  private final float[] mValues = new float[9];
  
  private ScriptIntrinsicBlur(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicBlur create(RenderScript paramRenderScript, Element paramElement)
  {
    if ((!paramElement.isCompatible(Element.U8_4(paramRenderScript))) && (!paramElement.isCompatible(Element.U8(paramRenderScript)))) {
      throw new RSIllegalArgumentException("Unsupported element type.");
    }
    paramRenderScript = new ScriptIntrinsicBlur(paramRenderScript.nScriptIntrinsicCreate(5, paramElement.getID(paramRenderScript)), paramRenderScript);
    paramRenderScript.setRadius(5.0F);
    return paramRenderScript;
  }
  
  public void forEach(Allocation paramAllocation)
  {
    if (paramAllocation.getType().getY() != 0)
    {
      forEach(0, (Allocation)null, paramAllocation, null);
      return;
    }
    throw new RSIllegalArgumentException("Output is a 1D Allocation");
  }
  
  public void forEach(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions)
  {
    if (paramAllocation.getType().getY() != 0)
    {
      forEach(0, (Allocation)null, paramAllocation, null, paramLaunchOptions);
      return;
    }
    throw new RSIllegalArgumentException("Output is a 1D Allocation");
  }
  
  public Script.FieldID getFieldID_Input()
  {
    return createFieldID(1, null);
  }
  
  public Script.KernelID getKernelID()
  {
    return createKernelID(0, 2, null, null);
  }
  
  public void setInput(Allocation paramAllocation)
  {
    if (paramAllocation.getType().getY() != 0)
    {
      mInput = paramAllocation;
      setVar(1, paramAllocation);
      return;
    }
    throw new RSIllegalArgumentException("Input set to a 1D Allocation");
  }
  
  public void setRadius(float paramFloat)
  {
    if ((paramFloat > 0.0F) && (paramFloat <= 25.0F))
    {
      setVar(0, paramFloat);
      return;
    }
    throw new RSIllegalArgumentException("Radius out of range (0 < r <= 25).");
  }
}
