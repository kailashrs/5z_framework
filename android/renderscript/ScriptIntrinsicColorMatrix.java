package android.renderscript;

public final class ScriptIntrinsicColorMatrix
  extends ScriptIntrinsic
{
  private final Float4 mAdd = new Float4();
  private final Matrix4f mMatrix = new Matrix4f();
  
  private ScriptIntrinsicColorMatrix(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static ScriptIntrinsicColorMatrix create(RenderScript paramRenderScript)
  {
    return new ScriptIntrinsicColorMatrix(paramRenderScript.nScriptIntrinsicCreate(2, 0L), paramRenderScript);
  }
  
  @Deprecated
  public static ScriptIntrinsicColorMatrix create(RenderScript paramRenderScript, Element paramElement)
  {
    return create(paramRenderScript);
  }
  
  private void setMatrix()
  {
    FieldPacker localFieldPacker = new FieldPacker(64);
    localFieldPacker.addMatrix(mMatrix);
    setVar(0, localFieldPacker);
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEach(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    if ((!paramAllocation1.getElement().isCompatible(Element.U8(mRS))) && (!paramAllocation1.getElement().isCompatible(Element.U8_2(mRS))) && (!paramAllocation1.getElement().isCompatible(Element.U8_3(mRS))) && (!paramAllocation1.getElement().isCompatible(Element.U8_4(mRS))) && (!paramAllocation1.getElement().isCompatible(Element.F32(mRS))) && (!paramAllocation1.getElement().isCompatible(Element.F32_2(mRS))) && (!paramAllocation1.getElement().isCompatible(Element.F32_3(mRS))) && (!paramAllocation1.getElement().isCompatible(Element.F32_4(mRS)))) {
      throw new RSIllegalArgumentException("Unsupported element type.");
    }
    if ((!paramAllocation2.getElement().isCompatible(Element.U8(mRS))) && (!paramAllocation2.getElement().isCompatible(Element.U8_2(mRS))) && (!paramAllocation2.getElement().isCompatible(Element.U8_3(mRS))) && (!paramAllocation2.getElement().isCompatible(Element.U8_4(mRS))) && (!paramAllocation2.getElement().isCompatible(Element.F32(mRS))) && (!paramAllocation2.getElement().isCompatible(Element.F32_2(mRS))) && (!paramAllocation2.getElement().isCompatible(Element.F32_3(mRS))) && (!paramAllocation2.getElement().isCompatible(Element.F32_4(mRS)))) {
      throw new RSIllegalArgumentException("Unsupported element type.");
    }
    forEach(0, paramAllocation1, paramAllocation2, null, paramLaunchOptions);
  }
  
  public Script.KernelID getKernelID()
  {
    return createKernelID(0, 3, null, null);
  }
  
  public void setAdd(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    mAdd.x = paramFloat1;
    mAdd.y = paramFloat2;
    mAdd.z = paramFloat3;
    mAdd.w = paramFloat4;
    FieldPacker localFieldPacker = new FieldPacker(16);
    localFieldPacker.addF32(mAdd.x);
    localFieldPacker.addF32(mAdd.y);
    localFieldPacker.addF32(mAdd.z);
    localFieldPacker.addF32(mAdd.w);
    setVar(1, localFieldPacker);
  }
  
  public void setAdd(Float4 paramFloat4)
  {
    mAdd.x = x;
    mAdd.y = y;
    mAdd.z = z;
    mAdd.w = w;
    FieldPacker localFieldPacker = new FieldPacker(16);
    localFieldPacker.addF32(x);
    localFieldPacker.addF32(y);
    localFieldPacker.addF32(z);
    localFieldPacker.addF32(w);
    setVar(1, localFieldPacker);
  }
  
  public void setColorMatrix(Matrix3f paramMatrix3f)
  {
    mMatrix.load(paramMatrix3f);
    setMatrix();
  }
  
  public void setColorMatrix(Matrix4f paramMatrix4f)
  {
    mMatrix.load(paramMatrix4f);
    setMatrix();
  }
  
  public void setGreyscale()
  {
    mMatrix.loadIdentity();
    mMatrix.set(0, 0, 0.299F);
    mMatrix.set(1, 0, 0.587F);
    mMatrix.set(2, 0, 0.114F);
    mMatrix.set(0, 1, 0.299F);
    mMatrix.set(1, 1, 0.587F);
    mMatrix.set(2, 1, 0.114F);
    mMatrix.set(0, 2, 0.299F);
    mMatrix.set(1, 2, 0.587F);
    mMatrix.set(2, 2, 0.114F);
    setMatrix();
  }
  
  public void setRGBtoYUV()
  {
    mMatrix.loadIdentity();
    mMatrix.set(0, 0, 0.299F);
    mMatrix.set(1, 0, 0.587F);
    mMatrix.set(2, 0, 0.114F);
    mMatrix.set(0, 1, -0.14713F);
    mMatrix.set(1, 1, -0.28886F);
    mMatrix.set(2, 1, 0.436F);
    mMatrix.set(0, 2, 0.615F);
    mMatrix.set(1, 2, -0.51499F);
    mMatrix.set(2, 2, -0.10001F);
    setMatrix();
  }
  
  public void setYUVtoRGB()
  {
    mMatrix.loadIdentity();
    mMatrix.set(0, 0, 1.0F);
    mMatrix.set(1, 0, 0.0F);
    mMatrix.set(2, 0, 1.13983F);
    mMatrix.set(0, 1, 1.0F);
    mMatrix.set(1, 1, -0.39465F);
    mMatrix.set(2, 1, -0.5806F);
    mMatrix.set(0, 2, 1.0F);
    mMatrix.set(1, 2, 2.03211F);
    mMatrix.set(2, 2, 0.0F);
    setMatrix();
  }
}
