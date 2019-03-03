package android.renderscript;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.SparseArray;
import dalvik.system.CloseGuard;
import java.io.UnsupportedEncodingException;

public class Script
  extends BaseObj
{
  private final SparseArray<FieldID> mFIDs = new SparseArray();
  private final SparseArray<InvokeID> mIIDs = new SparseArray();
  long[] mInIdsBuffer = new long[1];
  private final SparseArray<KernelID> mKIDs = new SparseArray();
  
  Script(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
    guard.open("destroy");
  }
  
  public void bindAllocation(Allocation paramAllocation, int paramInt)
  {
    mRS.validate();
    mRS.validateObject(paramAllocation);
    if (paramAllocation != null)
    {
      if (mRS.getApplicationContext().getApplicationInfo().targetSdkVersion >= 20)
      {
        Type localType = mType;
        if ((localType.hasMipmaps()) || (localType.hasFaces()) || (localType.getY() != 0) || (localType.getZ() != 0)) {
          throw new RSIllegalArgumentException("API 20+ only allows simple 1D allocations to be used with bind.");
        }
      }
      mRS.nScriptBindAllocation(getID(mRS), paramAllocation.getID(mRS), paramInt);
    }
    else
    {
      mRS.nScriptBindAllocation(getID(mRS), 0L, paramInt);
    }
  }
  
  protected FieldID createFieldID(int paramInt, Element paramElement)
  {
    paramElement = (FieldID)mFIDs.get(paramInt);
    if (paramElement != null) {
      return paramElement;
    }
    long l = mRS.nScriptFieldIDCreate(getID(mRS), paramInt);
    if (l != 0L)
    {
      paramElement = new FieldID(l, mRS, this, paramInt);
      mFIDs.put(paramInt, paramElement);
      return paramElement;
    }
    throw new RSDriverException("Failed to create FieldID");
  }
  
  protected InvokeID createInvokeID(int paramInt)
  {
    InvokeID localInvokeID = (InvokeID)mIIDs.get(paramInt);
    if (localInvokeID != null) {
      return localInvokeID;
    }
    long l = mRS.nScriptInvokeIDCreate(getID(mRS), paramInt);
    if (l != 0L)
    {
      localInvokeID = new InvokeID(l, mRS, this, paramInt);
      mIIDs.put(paramInt, localInvokeID);
      return localInvokeID;
    }
    throw new RSDriverException("Failed to create KernelID");
  }
  
  protected KernelID createKernelID(int paramInt1, int paramInt2, Element paramElement1, Element paramElement2)
  {
    paramElement1 = (KernelID)mKIDs.get(paramInt1);
    if (paramElement1 != null) {
      return paramElement1;
    }
    long l = mRS.nScriptKernelIDCreate(getID(mRS), paramInt1, paramInt2);
    if (l != 0L)
    {
      paramElement1 = new KernelID(l, mRS, this, paramInt1, paramInt2);
      mKIDs.put(paramInt1, paramElement1);
      return paramElement1;
    }
    throw new RSDriverException("Failed to create KernelID");
  }
  
  protected void forEach(int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, FieldPacker paramFieldPacker)
  {
    forEach(paramInt, paramAllocation1, paramAllocation2, paramFieldPacker, null);
  }
  
  protected void forEach(int paramInt, Allocation paramAllocation1, Allocation paramAllocation2, FieldPacker paramFieldPacker, LaunchOptions paramLaunchOptions)
  {
    mRS.validate();
    mRS.validateObject(paramAllocation1);
    mRS.validateObject(paramAllocation2);
    if ((paramAllocation1 == null) && (paramAllocation2 == null) && (paramLaunchOptions == null)) {
      throw new RSIllegalArgumentException("At least one of input allocation, output allocation, or LaunchOptions is required to be non-null.");
    }
    long[] arrayOfLong = null;
    if (paramAllocation1 != null)
    {
      arrayOfLong = mInIdsBuffer;
      arrayOfLong[0] = paramAllocation1.getID(mRS);
    }
    long l = 0L;
    if (paramAllocation2 != null) {
      l = paramAllocation2.getID(mRS);
    }
    paramAllocation1 = null;
    if (paramFieldPacker != null) {
      paramAllocation1 = paramFieldPacker.getData();
    }
    paramAllocation2 = null;
    if (paramLaunchOptions != null)
    {
      paramAllocation2 = new int[6];
      paramAllocation2[0] = xstart;
      paramAllocation2[1] = xend;
      paramAllocation2[2] = ystart;
      paramAllocation2[3] = yend;
      paramAllocation2[4] = zstart;
      paramAllocation2[5] = zend;
    }
    mRS.nScriptForEach(getID(mRS), paramInt, arrayOfLong, l, paramAllocation1, paramAllocation2);
  }
  
  protected void forEach(int paramInt, Allocation[] paramArrayOfAllocation, Allocation paramAllocation, FieldPacker paramFieldPacker)
  {
    forEach(paramInt, paramArrayOfAllocation, paramAllocation, paramFieldPacker, null);
  }
  
  protected void forEach(int paramInt, Allocation[] paramArrayOfAllocation, Allocation paramAllocation, FieldPacker paramFieldPacker, LaunchOptions paramLaunchOptions)
  {
    mRS.validate();
    int j;
    if (paramArrayOfAllocation != null)
    {
      int i = paramArrayOfAllocation.length;
      for (j = 0; j < i; j++)
      {
        localObject = paramArrayOfAllocation[j];
        mRS.validateObject((BaseObj)localObject);
      }
    }
    mRS.validateObject(paramAllocation);
    if ((paramArrayOfAllocation == null) && (paramAllocation == null)) {
      throw new RSIllegalArgumentException("At least one of ain or aout is required to be non-null.");
    }
    if (paramArrayOfAllocation != null)
    {
      long[] arrayOfLong = new long[paramArrayOfAllocation.length];
      for (j = 0;; j++)
      {
        localObject = arrayOfLong;
        if (j >= paramArrayOfAllocation.length) {
          break;
        }
        arrayOfLong[j] = paramArrayOfAllocation[j].getID(mRS);
      }
    }
    Object localObject = null;
    long l = 0L;
    if (paramAllocation != null) {
      l = paramAllocation.getID(mRS);
    }
    paramArrayOfAllocation = null;
    if (paramFieldPacker != null) {
      paramArrayOfAllocation = paramFieldPacker.getData();
    }
    paramAllocation = null;
    if (paramLaunchOptions != null)
    {
      paramAllocation = new int[6];
      paramAllocation[0] = xstart;
      paramAllocation[1] = xend;
      paramAllocation[2] = ystart;
      paramAllocation[3] = yend;
      paramAllocation[4] = zstart;
      paramAllocation[5] = zend;
    }
    mRS.nScriptForEach(getID(mRS), paramInt, (long[])localObject, l, paramArrayOfAllocation, paramAllocation);
  }
  
  public boolean getVarB(int paramInt)
  {
    boolean bool;
    if (mRS.nScriptGetVarI(getID(mRS), paramInt) > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public double getVarD(int paramInt)
  {
    return mRS.nScriptGetVarD(getID(mRS), paramInt);
  }
  
  public float getVarF(int paramInt)
  {
    return mRS.nScriptGetVarF(getID(mRS), paramInt);
  }
  
  public int getVarI(int paramInt)
  {
    return mRS.nScriptGetVarI(getID(mRS), paramInt);
  }
  
  public long getVarJ(int paramInt)
  {
    return mRS.nScriptGetVarJ(getID(mRS), paramInt);
  }
  
  public void getVarV(int paramInt, FieldPacker paramFieldPacker)
  {
    mRS.nScriptGetVarV(getID(mRS), paramInt, paramFieldPacker.getData());
  }
  
  protected void invoke(int paramInt)
  {
    mRS.nScriptInvoke(getID(mRS), paramInt);
  }
  
  protected void invoke(int paramInt, FieldPacker paramFieldPacker)
  {
    if (paramFieldPacker != null) {
      mRS.nScriptInvokeV(getID(mRS), paramInt, paramFieldPacker.getData());
    } else {
      mRS.nScriptInvoke(getID(mRS), paramInt);
    }
  }
  
  protected void reduce(int paramInt, Allocation[] paramArrayOfAllocation, Allocation paramAllocation, LaunchOptions paramLaunchOptions)
  {
    mRS.validate();
    if ((paramArrayOfAllocation != null) && (paramArrayOfAllocation.length >= 1))
    {
      if (paramAllocation != null)
      {
        int i = paramArrayOfAllocation.length;
        for (int j = 0; j < i; j++)
        {
          localObject = paramArrayOfAllocation[j];
          mRS.validateObject((BaseObj)localObject);
        }
        Object localObject = new long[paramArrayOfAllocation.length];
        for (j = 0; j < paramArrayOfAllocation.length; j++) {
          localObject[j] = paramArrayOfAllocation[j].getID(mRS);
        }
        long l = paramAllocation.getID(mRS);
        paramArrayOfAllocation = null;
        if (paramLaunchOptions != null)
        {
          paramArrayOfAllocation = new int[6];
          paramArrayOfAllocation[0] = xstart;
          paramArrayOfAllocation[1] = xend;
          paramArrayOfAllocation[2] = ystart;
          paramArrayOfAllocation[3] = yend;
          paramArrayOfAllocation[4] = zstart;
          paramArrayOfAllocation[5] = zend;
        }
        mRS.nScriptReduce(getID(mRS), paramInt, (long[])localObject, l, paramArrayOfAllocation);
        return;
      }
      throw new RSIllegalArgumentException("aout is required to be non-null.");
    }
    throw new RSIllegalArgumentException("At least one input is required.");
  }
  
  public void setTimeZone(String paramString)
  {
    mRS.validate();
    try
    {
      mRS.nScriptSetTimeZone(getID(mRS), paramString.getBytes("UTF-8"));
      return;
    }
    catch (UnsupportedEncodingException paramString)
    {
      throw new RuntimeException(paramString);
    }
  }
  
  public void setVar(int paramInt, double paramDouble)
  {
    mRS.nScriptSetVarD(getID(mRS), paramInt, paramDouble);
  }
  
  public void setVar(int paramInt, float paramFloat)
  {
    mRS.nScriptSetVarF(getID(mRS), paramInt, paramFloat);
  }
  
  public void setVar(int paramInt1, int paramInt2)
  {
    mRS.nScriptSetVarI(getID(mRS), paramInt1, paramInt2);
  }
  
  public void setVar(int paramInt, long paramLong)
  {
    mRS.nScriptSetVarJ(getID(mRS), paramInt, paramLong);
  }
  
  public void setVar(int paramInt, BaseObj paramBaseObj)
  {
    mRS.validate();
    mRS.validateObject(paramBaseObj);
    RenderScript localRenderScript = mRS;
    long l1 = getID(mRS);
    if (paramBaseObj == null) {}
    for (long l2 = 0L;; l2 = paramBaseObj.getID(mRS)) {
      break;
    }
    localRenderScript.nScriptSetVarObj(l1, paramInt, l2);
  }
  
  public void setVar(int paramInt, FieldPacker paramFieldPacker)
  {
    mRS.nScriptSetVarV(getID(mRS), paramInt, paramFieldPacker.getData());
  }
  
  public void setVar(int paramInt, FieldPacker paramFieldPacker, Element paramElement, int[] paramArrayOfInt)
  {
    mRS.nScriptSetVarVE(getID(mRS), paramInt, paramFieldPacker.getData(), paramElement.getID(mRS), paramArrayOfInt);
  }
  
  public void setVar(int paramInt, boolean paramBoolean)
  {
    mRS.nScriptSetVarI(getID(mRS), paramInt, paramBoolean);
  }
  
  public static class Builder
  {
    RenderScript mRS;
    
    Builder(RenderScript paramRenderScript)
    {
      mRS = paramRenderScript;
    }
  }
  
  public static class FieldBase
  {
    protected Allocation mAllocation;
    protected Element mElement;
    
    protected FieldBase() {}
    
    public Allocation getAllocation()
    {
      return mAllocation;
    }
    
    public Element getElement()
    {
      return mElement;
    }
    
    public Type getType()
    {
      return mAllocation.getType();
    }
    
    protected void init(RenderScript paramRenderScript, int paramInt)
    {
      mAllocation = Allocation.createSized(paramRenderScript, mElement, paramInt, 1);
    }
    
    protected void init(RenderScript paramRenderScript, int paramInt1, int paramInt2)
    {
      mAllocation = Allocation.createSized(paramRenderScript, mElement, paramInt1, 0x1 | paramInt2);
    }
    
    public void updateAllocation() {}
  }
  
  public static final class FieldID
    extends BaseObj
  {
    Script mScript;
    int mSlot;
    
    FieldID(long paramLong, RenderScript paramRenderScript, Script paramScript, int paramInt)
    {
      super(paramRenderScript);
      mScript = paramScript;
      mSlot = paramInt;
    }
  }
  
  public static final class InvokeID
    extends BaseObj
  {
    Script mScript;
    int mSlot;
    
    InvokeID(long paramLong, RenderScript paramRenderScript, Script paramScript, int paramInt)
    {
      super(paramRenderScript);
      mScript = paramScript;
      mSlot = paramInt;
    }
  }
  
  public static final class KernelID
    extends BaseObj
  {
    Script mScript;
    int mSig;
    int mSlot;
    
    KernelID(long paramLong, RenderScript paramRenderScript, Script paramScript, int paramInt1, int paramInt2)
    {
      super(paramRenderScript);
      mScript = paramScript;
      mSlot = paramInt1;
      mSig = paramInt2;
    }
  }
  
  public static final class LaunchOptions
  {
    private int strategy;
    private int xend = 0;
    private int xstart = 0;
    private int yend = 0;
    private int ystart = 0;
    private int zend = 0;
    private int zstart = 0;
    
    public LaunchOptions() {}
    
    public int getXEnd()
    {
      return xend;
    }
    
    public int getXStart()
    {
      return xstart;
    }
    
    public int getYEnd()
    {
      return yend;
    }
    
    public int getYStart()
    {
      return ystart;
    }
    
    public int getZEnd()
    {
      return zend;
    }
    
    public int getZStart()
    {
      return zstart;
    }
    
    public LaunchOptions setX(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt2 > paramInt1))
      {
        xstart = paramInt1;
        xend = paramInt2;
        return this;
      }
      throw new RSIllegalArgumentException("Invalid dimensions");
    }
    
    public LaunchOptions setY(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt2 > paramInt1))
      {
        ystart = paramInt1;
        yend = paramInt2;
        return this;
      }
      throw new RSIllegalArgumentException("Invalid dimensions");
    }
    
    public LaunchOptions setZ(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt2 > paramInt1))
      {
        zstart = paramInt1;
        zend = paramInt2;
        return this;
      }
      throw new RSIllegalArgumentException("Invalid dimensions");
    }
  }
}
