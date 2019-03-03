package android.renderscript;

public class ScriptIntrinsicBlend
  extends ScriptIntrinsic
{
  ScriptIntrinsicBlend(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  private void blend(int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    if (paramAllocation1.getElement().isCompatible(Element.U8_4(mRS)))
    {
      if (paramAllocation2.getElement().isCompatible(Element.U8_4(mRS)))
      {
        forEach(paramInt, paramAllocation1, paramAllocation2, null, paramLaunchOptions);
        return;
      }
      throw new RSIllegalArgumentException("Output is not of expected format.");
    }
    throw new RSIllegalArgumentException("Input is not of expected format.");
  }
  
  public static ScriptIntrinsicBlend create(RenderScript paramRenderScript, Element paramElement)
  {
    return new ScriptIntrinsicBlend(paramRenderScript.nScriptIntrinsicCreate(7, paramElement.getID(paramRenderScript)), paramRenderScript);
  }
  
  public void forEachAdd(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachAdd(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachAdd(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(34, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachClear(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachClear(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachClear(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(0, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachDst(Allocation paramAllocation1, Allocation paramAllocation2) {}
  
  public void forEachDst(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions) {}
  
  public void forEachDstAtop(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachDstAtop(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachDstAtop(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(10, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachDstIn(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachDstIn(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachDstIn(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(6, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachDstOut(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachDstOut(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachDstOut(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(8, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachDstOver(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachDstOver(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachDstOver(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(4, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachMultiply(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachMultiply(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachMultiply(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(14, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachSrc(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachSrc(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachSrc(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(1, paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachSrcAtop(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachSrcAtop(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachSrcAtop(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(9, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachSrcIn(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachSrcIn(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachSrcIn(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(5, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachSrcOut(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachSrcOut(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachSrcOut(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(7, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachSrcOver(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachSrcOver(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachSrcOver(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(3, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachSubtract(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachSubtract(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachSubtract(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(35, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public void forEachXor(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEachXor(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEachXor(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    blend(11, paramAllocation1, paramAllocation2, paramLaunchOptions);
  }
  
  public Script.KernelID getKernelIDAdd()
  {
    return createKernelID(34, 3, null, null);
  }
  
  public Script.KernelID getKernelIDClear()
  {
    return createKernelID(0, 3, null, null);
  }
  
  public Script.KernelID getKernelIDDst()
  {
    return createKernelID(2, 3, null, null);
  }
  
  public Script.KernelID getKernelIDDstAtop()
  {
    return createKernelID(10, 3, null, null);
  }
  
  public Script.KernelID getKernelIDDstIn()
  {
    return createKernelID(6, 3, null, null);
  }
  
  public Script.KernelID getKernelIDDstOut()
  {
    return createKernelID(8, 3, null, null);
  }
  
  public Script.KernelID getKernelIDDstOver()
  {
    return createKernelID(4, 3, null, null);
  }
  
  public Script.KernelID getKernelIDMultiply()
  {
    return createKernelID(14, 3, null, null);
  }
  
  public Script.KernelID getKernelIDSrc()
  {
    return createKernelID(1, 3, null, null);
  }
  
  public Script.KernelID getKernelIDSrcAtop()
  {
    return createKernelID(9, 3, null, null);
  }
  
  public Script.KernelID getKernelIDSrcIn()
  {
    return createKernelID(5, 3, null, null);
  }
  
  public Script.KernelID getKernelIDSrcOut()
  {
    return createKernelID(7, 3, null, null);
  }
  
  public Script.KernelID getKernelIDSrcOver()
  {
    return createKernelID(3, 3, null, null);
  }
  
  public Script.KernelID getKernelIDSubtract()
  {
    return createKernelID(35, 3, null, null);
  }
  
  public Script.KernelID getKernelIDXor()
  {
    return createKernelID(11, 3, null, null);
  }
}
