package android.renderscript;

public final class ScriptIntrinsic3DLUT
  extends ScriptIntrinsic
{
  private Element mElement;
  private Allocation mLUT;
  
  private ScriptIntrinsic3DLUT(long paramLong, RenderScript paramRenderScript, Element paramElement)
  {
    super(paramLong, paramRenderScript);
    mElement = paramElement;
  }
  
  public static ScriptIntrinsic3DLUT create(RenderScript paramRenderScript, Element paramElement)
  {
    long l = paramRenderScript.nScriptIntrinsicCreate(8, paramElement.getID(paramRenderScript));
    if (paramElement.isCompatible(Element.U8_4(paramRenderScript))) {
      return new ScriptIntrinsic3DLUT(l, paramRenderScript, paramElement);
    }
    throw new RSIllegalArgumentException("Element must be compatible with uchar4.");
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEach(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    forEach(0, paramAllocation1, paramAllocation2, null, paramLaunchOptions);
  }
  
  public Script.KernelID getKernelID()
  {
    return createKernelID(0, 3, null, null);
  }
  
  public void setLUT(Allocation paramAllocation)
  {
    Type localType = paramAllocation.getType();
    if (localType.getZ() != 0)
    {
      if (localType.getElement().isCompatible(mElement))
      {
        mLUT = paramAllocation;
        setVar(0, mLUT);
        return;
      }
      throw new RSIllegalArgumentException("LUT element type must match.");
    }
    throw new RSIllegalArgumentException("LUT must be 3d.");
  }
}
