package android.renderscript;

public final class ScriptIntrinsicHistogram
  extends ScriptIntrinsic
{
  private Allocation mOut;
  
  private ScriptIntrinsicHistogram(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicHistogram create(RenderScript paramRenderScript, Element paramElement)
  {
    if ((!paramElement.isCompatible(Element.U8_4(paramRenderScript))) && (!paramElement.isCompatible(Element.U8_3(paramRenderScript))) && (!paramElement.isCompatible(Element.U8_2(paramRenderScript))) && (!paramElement.isCompatible(Element.U8(paramRenderScript)))) {
      throw new RSIllegalArgumentException("Unsupported element type.");
    }
    return new ScriptIntrinsicHistogram(paramRenderScript.nScriptIntrinsicCreate(9, paramElement.getID(paramRenderScript)), paramRenderScript);
  }
  
  public void forEach(Allocation paramAllocation)
  {
    forEach(paramAllocation, null);
  }
  
  public void forEach(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions)
  {
    if (paramAllocation.getType().getElement().getVectorSize() >= mOut.getType().getElement().getVectorSize())
    {
      if ((!paramAllocation.getType().getElement().isCompatible(Element.U8(mRS))) && (!paramAllocation.getType().getElement().isCompatible(Element.U8_2(mRS))) && (!paramAllocation.getType().getElement().isCompatible(Element.U8_3(mRS))) && (!paramAllocation.getType().getElement().isCompatible(Element.U8_4(mRS)))) {
        throw new RSIllegalArgumentException("Input type must be U8, U8_1, U8_2 or U8_4.");
      }
      forEach(0, paramAllocation, null, null, paramLaunchOptions);
      return;
    }
    throw new RSIllegalArgumentException("Input vector size must be >= output vector size.");
  }
  
  public void forEach_Dot(Allocation paramAllocation)
  {
    forEach_Dot(paramAllocation, null);
  }
  
  public void forEach_Dot(Allocation paramAllocation, Script.LaunchOptions paramLaunchOptions)
  {
    if (mOut.getType().getElement().getVectorSize() == 1)
    {
      if ((!paramAllocation.getType().getElement().isCompatible(Element.U8(mRS))) && (!paramAllocation.getType().getElement().isCompatible(Element.U8_2(mRS))) && (!paramAllocation.getType().getElement().isCompatible(Element.U8_3(mRS))) && (!paramAllocation.getType().getElement().isCompatible(Element.U8_4(mRS)))) {
        throw new RSIllegalArgumentException("Input type must be U8, U8_1, U8_2 or U8_4.");
      }
      forEach(1, paramAllocation, null, null, paramLaunchOptions);
      return;
    }
    throw new RSIllegalArgumentException("Output vector size must be one.");
  }
  
  public Script.FieldID getFieldID_Input()
  {
    return createFieldID(1, null);
  }
  
  public Script.KernelID getKernelID_Separate()
  {
    return createKernelID(0, 3, null, null);
  }
  
  public void setDotCoefficients(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if ((paramFloat1 >= 0.0F) && (paramFloat2 >= 0.0F) && (paramFloat3 >= 0.0F) && (paramFloat4 >= 0.0F))
    {
      if (paramFloat1 + paramFloat2 + paramFloat3 + paramFloat4 <= 1.0F)
      {
        FieldPacker localFieldPacker = new FieldPacker(16);
        localFieldPacker.addF32(paramFloat1);
        localFieldPacker.addF32(paramFloat2);
        localFieldPacker.addF32(paramFloat3);
        localFieldPacker.addF32(paramFloat4);
        setVar(0, localFieldPacker);
        return;
      }
      throw new RSIllegalArgumentException("Sum of coefficients must be 1.0 or less.");
    }
    throw new RSIllegalArgumentException("Coefficient may not be negative.");
  }
  
  public void setOutput(Allocation paramAllocation)
  {
    mOut = paramAllocation;
    if ((mOut.getType().getElement() != Element.U32(mRS)) && (mOut.getType().getElement() != Element.U32_2(mRS)) && (mOut.getType().getElement() != Element.U32_3(mRS)) && (mOut.getType().getElement() != Element.U32_4(mRS)) && (mOut.getType().getElement() != Element.I32(mRS)) && (mOut.getType().getElement() != Element.I32_2(mRS)) && (mOut.getType().getElement() != Element.I32_3(mRS)) && (mOut.getType().getElement() != Element.I32_4(mRS))) {
      throw new RSIllegalArgumentException("Output type must be U32 or I32.");
    }
    if ((mOut.getType().getX() == 256) && (mOut.getType().getY() == 0) && (!mOut.getType().hasMipmaps()) && (mOut.getType().getYuv() == 0))
    {
      setVar(1, paramAllocation);
      return;
    }
    throw new RSIllegalArgumentException("Output must be 1D, 256 elements.");
  }
}
