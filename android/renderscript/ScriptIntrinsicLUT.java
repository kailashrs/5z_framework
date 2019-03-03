package android.renderscript;

public final class ScriptIntrinsicLUT
  extends ScriptIntrinsic
{
  private final byte[] mCache = new byte['Ѐ'];
  private boolean mDirty = true;
  private final Matrix4f mMatrix = new Matrix4f();
  private Allocation mTables;
  
  private ScriptIntrinsicLUT(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
    mTables = Allocation.createSized(paramRenderScript, Element.U8(paramRenderScript), 1024);
    for (int i = 0; i < 256; i++)
    {
      mCache[i] = ((byte)(byte)i);
      mCache[(i + 256)] = ((byte)(byte)i);
      mCache[(i + 512)] = ((byte)(byte)i);
      mCache[(i + 768)] = ((byte)(byte)i);
    }
    setVar(0, mTables);
  }
  
  public static ScriptIntrinsicLUT create(RenderScript paramRenderScript, Element paramElement)
  {
    return new ScriptIntrinsicLUT(paramRenderScript.nScriptIntrinsicCreate(3, paramElement.getID(paramRenderScript)), paramRenderScript);
  }
  
  private void validate(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 255))
    {
      if ((paramInt2 >= 0) && (paramInt2 <= 255)) {
        return;
      }
      throw new RSIllegalArgumentException("Value out of range (0-255).");
    }
    throw new RSIllegalArgumentException("Index out of range (0-255).");
  }
  
  public void destroy()
  {
    mTables.destroy();
    super.destroy();
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2)
  {
    forEach(paramAllocation1, paramAllocation2, null);
  }
  
  public void forEach(Allocation paramAllocation1, Allocation paramAllocation2, Script.LaunchOptions paramLaunchOptions)
  {
    if (mDirty)
    {
      mDirty = false;
      mTables.copyFromUnchecked(mCache);
    }
    forEach(0, paramAllocation1, paramAllocation2, null, paramLaunchOptions);
  }
  
  public Script.KernelID getKernelID()
  {
    return createKernelID(0, 3, null, null);
  }
  
  public void setAlpha(int paramInt1, int paramInt2)
  {
    validate(paramInt1, paramInt2);
    mCache[(paramInt1 + 768)] = ((byte)(byte)paramInt2);
    mDirty = true;
  }
  
  public void setBlue(int paramInt1, int paramInt2)
  {
    validate(paramInt1, paramInt2);
    mCache[(paramInt1 + 512)] = ((byte)(byte)paramInt2);
    mDirty = true;
  }
  
  public void setGreen(int paramInt1, int paramInt2)
  {
    validate(paramInt1, paramInt2);
    mCache[(paramInt1 + 256)] = ((byte)(byte)paramInt2);
    mDirty = true;
  }
  
  public void setRed(int paramInt1, int paramInt2)
  {
    validate(paramInt1, paramInt2);
    mCache[paramInt1] = ((byte)(byte)paramInt2);
    mDirty = true;
  }
}
