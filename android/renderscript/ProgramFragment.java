package android.renderscript;

public class ProgramFragment
  extends Program
{
  ProgramFragment(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
  }
  
  public static class Builder
    extends Program.BaseProgramBuilder
  {
    public Builder(RenderScript paramRenderScript)
    {
      super();
    }
    
    public ProgramFragment create()
    {
      mRS.validate();
      long[] arrayOfLong = new long[(mInputCount + mOutputCount + mConstantCount + mTextureCount) * 2];
      Object localObject = new String[mTextureCount];
      int i = 0;
      int j = 0;
      int m;
      for (int k = 0; k < mInputCount; k++)
      {
        m = j + 1;
        arrayOfLong[j] = INPUTmID;
        j = m + 1;
        arrayOfLong[m] = mInputs[k].getID(mRS);
      }
      for (k = 0; k < mOutputCount; k++)
      {
        m = j + 1;
        arrayOfLong[j] = OUTPUTmID;
        j = m + 1;
        arrayOfLong[m] = mOutputs[k].getID(mRS);
      }
      for (k = 0; k < mConstantCount; k++)
      {
        m = j + 1;
        arrayOfLong[j] = CONSTANTmID;
        j = m + 1;
        arrayOfLong[m] = mConstants[k].getID(mRS);
      }
      k = j;
      for (j = i; j < mTextureCount; j++)
      {
        i = k + 1;
        arrayOfLong[k] = TEXTURE_TYPEmID;
        k = i + 1;
        arrayOfLong[i] = mTextureTypes[j].mID;
        localObject[j] = mTextureNames[j];
      }
      localObject = new ProgramFragment(mRS.nProgramFragmentCreate(mShader, (String[])localObject, arrayOfLong), mRS);
      initProgram((Program)localObject);
      return localObject;
    }
  }
}
