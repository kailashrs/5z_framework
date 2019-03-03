package android.renderscript;

public class ProgramVertexFixedFunction
  extends ProgramVertex
{
  ProgramVertexFixedFunction(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public void bindConstants(Constants paramConstants)
  {
    mRS.validate();
    bindConstants(paramConstants.getAllocation(), 0);
  }
  
  public static class Builder
  {
    RenderScript mRS;
    String mShader;
    boolean mTextureMatrixEnable;
    
    public Builder(RenderScript paramRenderScript)
    {
      mRS = paramRenderScript;
    }
    
    private void buildShaderString()
    {
      mShader = "//rs_shader_internal\n";
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(mShader);
      localStringBuilder.append("varying vec4 varColor;\n");
      mShader = localStringBuilder.toString();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(mShader);
      localStringBuilder.append("varying vec2 varTex0;\n");
      mShader = localStringBuilder.toString();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(mShader);
      localStringBuilder.append("void main() {\n");
      mShader = localStringBuilder.toString();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(mShader);
      localStringBuilder.append("  gl_Position = UNI_MVP * ATTRIB_position;\n");
      mShader = localStringBuilder.toString();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(mShader);
      localStringBuilder.append("  gl_PointSize = 1.0;\n");
      mShader = localStringBuilder.toString();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(mShader);
      localStringBuilder.append("  varColor = ATTRIB_color;\n");
      mShader = localStringBuilder.toString();
      if (mTextureMatrixEnable)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(mShader);
        localStringBuilder.append("  varTex0 = (UNI_TexMatrix * vec4(ATTRIB_texture0, 0.0, 1.0)).xy;\n");
        mShader = localStringBuilder.toString();
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(mShader);
        localStringBuilder.append("  varTex0 = ATTRIB_texture0;\n");
        mShader = localStringBuilder.toString();
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(mShader);
      localStringBuilder.append("}\n");
      mShader = localStringBuilder.toString();
    }
    
    static Type getConstantInputType(RenderScript paramRenderScript)
    {
      Element.Builder localBuilder = new Element.Builder(paramRenderScript);
      localBuilder.add(Element.MATRIX4X4(paramRenderScript), "MV");
      localBuilder.add(Element.MATRIX4X4(paramRenderScript), "P");
      localBuilder.add(Element.MATRIX4X4(paramRenderScript), "TexMatrix");
      localBuilder.add(Element.MATRIX4X4(paramRenderScript), "MVP");
      paramRenderScript = new Type.Builder(paramRenderScript, localBuilder.create());
      paramRenderScript.setX(1);
      return paramRenderScript.create();
    }
    
    public ProgramVertexFixedFunction create()
    {
      buildShaderString();
      ProgramVertexFixedFunction.InternalBuilder localInternalBuilder = new ProgramVertexFixedFunction.InternalBuilder(mRS);
      localInternalBuilder.setShader(mShader);
      localInternalBuilder.addConstant(getConstantInputType(mRS));
      Element.Builder localBuilder = new Element.Builder(mRS);
      localBuilder.add(Element.F32_4(mRS), "position");
      localBuilder.add(Element.F32_4(mRS), "color");
      localBuilder.add(Element.F32_3(mRS), "normal");
      localBuilder.add(Element.F32_2(mRS), "texture0");
      localInternalBuilder.addInput(localBuilder.create());
      return localInternalBuilder.create();
    }
    
    public Builder setTextureMatrixEnable(boolean paramBoolean)
    {
      mTextureMatrixEnable = paramBoolean;
      return this;
    }
  }
  
  public static class Constants
  {
    static final int MODELVIEW_OFFSET = 0;
    static final int PROJECTION_OFFSET = 16;
    static final int TEXTURE_OFFSET = 32;
    Allocation mAlloc;
    private FieldPacker mIOBuffer;
    Matrix4f mModel;
    Matrix4f mProjection;
    Matrix4f mTexture;
    
    public Constants(RenderScript paramRenderScript)
    {
      Type localType = ProgramVertexFixedFunction.Builder.getConstantInputType(paramRenderScript);
      mAlloc = Allocation.createTyped(paramRenderScript, localType);
      mIOBuffer = new FieldPacker(localType.getElement().getBytesSize() * localType.getCount());
      mModel = new Matrix4f();
      mProjection = new Matrix4f();
      mTexture = new Matrix4f();
      setModelview(new Matrix4f());
      setProjection(new Matrix4f());
      setTexture(new Matrix4f());
    }
    
    private void addToBuffer(int paramInt, Matrix4f paramMatrix4f)
    {
      mIOBuffer.reset(paramInt);
      for (paramInt = 0; paramInt < 16; paramInt++) {
        mIOBuffer.addF32(mMat[paramInt]);
      }
      mIOBuffer.reset(mIOBuffer.getData().length);
      mAlloc.setFromFieldPacker(0, mIOBuffer);
    }
    
    public void destroy()
    {
      mAlloc.destroy();
      mAlloc = null;
    }
    
    Allocation getAllocation()
    {
      return mAlloc;
    }
    
    public void setModelview(Matrix4f paramMatrix4f)
    {
      mModel.load(paramMatrix4f);
      addToBuffer(0, paramMatrix4f);
    }
    
    public void setProjection(Matrix4f paramMatrix4f)
    {
      mProjection.load(paramMatrix4f);
      addToBuffer(64, paramMatrix4f);
    }
    
    public void setTexture(Matrix4f paramMatrix4f)
    {
      mTexture.load(paramMatrix4f);
      addToBuffer(128, paramMatrix4f);
    }
  }
  
  static class InternalBuilder
    extends Program.BaseProgramBuilder
  {
    public InternalBuilder(RenderScript paramRenderScript)
    {
      super();
    }
    
    public InternalBuilder addInput(Element paramElement)
      throws IllegalStateException
    {
      if (mInputCount < 8)
      {
        if (!paramElement.isComplex())
        {
          Element[] arrayOfElement = mInputs;
          int i = mInputCount;
          mInputCount = (i + 1);
          arrayOfElement[i] = paramElement;
          return this;
        }
        throw new RSIllegalArgumentException("Complex elements not allowed.");
      }
      throw new RSIllegalArgumentException("Max input count exceeded.");
    }
    
    public ProgramVertexFixedFunction create()
    {
      mRS.validate();
      Object localObject = new long[(mInputCount + mOutputCount + mConstantCount + mTextureCount) * 2];
      String[] arrayOfString = new String[mTextureCount];
      int i = 0;
      int j = 0;
      int m;
      for (int k = 0; k < mInputCount; k++)
      {
        m = j + 1;
        localObject[j] = INPUTmID;
        j = m + 1;
        localObject[m] = mInputs[k].getID(mRS);
      }
      for (k = 0; k < mOutputCount; k++)
      {
        m = j + 1;
        localObject[j] = OUTPUTmID;
        j = m + 1;
        localObject[m] = mOutputs[k].getID(mRS);
      }
      for (k = 0; k < mConstantCount; k++)
      {
        m = j + 1;
        localObject[j] = CONSTANTmID;
        j = m + 1;
        localObject[m] = mConstants[k].getID(mRS);
      }
      for (k = i; k < mTextureCount; k++)
      {
        i = j + 1;
        localObject[j] = TEXTURE_TYPEmID;
        j = i + 1;
        localObject[i] = mTextureTypes[k].mID;
        arrayOfString[k] = mTextureNames[k];
      }
      localObject = new ProgramVertexFixedFunction(mRS.nProgramVertexCreate(mShader, arrayOfString, (long[])localObject), mRS);
      initProgram((Program)localObject);
      return localObject;
    }
  }
}
