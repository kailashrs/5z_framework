package android.renderscript;

public class ProgramVertex
  extends Program
{
  ProgramVertex(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public Element getInput(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mInputs.length)) {
      return mInputs[paramInt];
    }
    throw new IllegalArgumentException("Slot ID out of range.");
  }
  
  public int getInputCount()
  {
    int i;
    if (mInputs != null) {
      i = mInputs.length;
    } else {
      i = 0;
    }
    return i;
  }
  
  public static class Builder
    extends Program.BaseProgramBuilder
  {
    public Builder(RenderScript paramRenderScript)
    {
      super();
    }
    
    public Builder addInput(Element paramElement)
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
    
    public ProgramVertex create()
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
      k = j;
      for (j = i; j < mTextureCount; j++)
      {
        i = k + 1;
        localObject[k] = TEXTURE_TYPEmID;
        k = i + 1;
        localObject[i] = mTextureTypes[j].mID;
        arrayOfString[j] = mTextureNames[j];
      }
      localObject = new ProgramVertex(mRS.nProgramVertexCreate(mShader, arrayOfString, (long[])localObject), mRS);
      initProgram((Program)localObject);
      return localObject;
    }
  }
}
