package android.renderscript;

public class ProgramRaster
  extends BaseObj
{
  CullMode mCullMode = CullMode.BACK;
  boolean mPointSprite = false;
  
  ProgramRaster(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static ProgramRaster CULL_BACK(RenderScript paramRenderScript)
  {
    if (mProgramRaster_CULL_BACK == null)
    {
      Builder localBuilder = new Builder(paramRenderScript);
      localBuilder.setCullMode(CullMode.BACK);
      mProgramRaster_CULL_BACK = localBuilder.create();
    }
    return mProgramRaster_CULL_BACK;
  }
  
  public static ProgramRaster CULL_FRONT(RenderScript paramRenderScript)
  {
    if (mProgramRaster_CULL_FRONT == null)
    {
      Builder localBuilder = new Builder(paramRenderScript);
      localBuilder.setCullMode(CullMode.FRONT);
      mProgramRaster_CULL_FRONT = localBuilder.create();
    }
    return mProgramRaster_CULL_FRONT;
  }
  
  public static ProgramRaster CULL_NONE(RenderScript paramRenderScript)
  {
    if (mProgramRaster_CULL_NONE == null)
    {
      Builder localBuilder = new Builder(paramRenderScript);
      localBuilder.setCullMode(CullMode.NONE);
      mProgramRaster_CULL_NONE = localBuilder.create();
    }
    return mProgramRaster_CULL_NONE;
  }
  
  public CullMode getCullMode()
  {
    return mCullMode;
  }
  
  public boolean isPointSpriteEnabled()
  {
    return mPointSprite;
  }
  
  public static class Builder
  {
    ProgramRaster.CullMode mCullMode;
    boolean mPointSprite;
    RenderScript mRS;
    
    public Builder(RenderScript paramRenderScript)
    {
      mRS = paramRenderScript;
      mPointSprite = false;
      mCullMode = ProgramRaster.CullMode.BACK;
    }
    
    public ProgramRaster create()
    {
      mRS.validate();
      ProgramRaster localProgramRaster = new ProgramRaster(mRS.nProgramRasterCreate(mPointSprite, mCullMode.mID), mRS);
      mPointSprite = mPointSprite;
      mCullMode = mCullMode;
      return localProgramRaster;
    }
    
    public Builder setCullMode(ProgramRaster.CullMode paramCullMode)
    {
      mCullMode = paramCullMode;
      return this;
    }
    
    public Builder setPointSpriteEnabled(boolean paramBoolean)
    {
      mPointSprite = paramBoolean;
      return this;
    }
  }
  
  public static enum CullMode
  {
    BACK(0),  FRONT(1),  NONE(2);
    
    int mID;
    
    private CullMode(int paramInt)
    {
      mID = paramInt;
    }
  }
}
