package android.renderscript;

public class AllocationAdapter
  extends Allocation
{
  Type mWindow;
  
  AllocationAdapter(long paramLong, RenderScript paramRenderScript, Allocation paramAllocation, Type paramType)
  {
    super(paramLong, paramRenderScript, mType, mUsage);
    mAdaptedAllocation = paramAllocation;
    mWindow = paramType;
  }
  
  public static AllocationAdapter create1D(RenderScript paramRenderScript, Allocation paramAllocation)
  {
    paramRenderScript.validate();
    return createTyped(paramRenderScript, paramAllocation, Type.createX(paramRenderScript, paramAllocation.getElement(), paramAllocation.getType().getX()));
  }
  
  public static AllocationAdapter create2D(RenderScript paramRenderScript, Allocation paramAllocation)
  {
    paramRenderScript.validate();
    return createTyped(paramRenderScript, paramAllocation, Type.createXY(paramRenderScript, paramAllocation.getElement(), paramAllocation.getType().getX(), paramAllocation.getType().getY()));
  }
  
  public static AllocationAdapter createTyped(RenderScript paramRenderScript, Allocation paramAllocation, Type paramType)
  {
    paramRenderScript.validate();
    if (mAdaptedAllocation == null)
    {
      if (paramAllocation.getType().getElement().equals(paramType.getElement()))
      {
        if ((!paramType.hasFaces()) && (!paramType.hasMipmaps()))
        {
          Type localType = paramAllocation.getType();
          if ((paramType.getX() <= localType.getX()) && (paramType.getY() <= localType.getY()) && (paramType.getZ() <= localType.getZ()) && (paramType.getArrayCount() <= localType.getArrayCount()))
          {
            if (paramType.getArrayCount() > 0)
            {
              int i = 0;
              while (i < paramType.getArray(i)) {
                if (paramType.getArray(i) <= localType.getArray(i)) {
                  i++;
                } else {
                  throw new RSInvalidStateException("Type cannot have dimension larger than the source allocation.");
                }
              }
            }
            long l = paramRenderScript.nAllocationAdapterCreate(paramAllocation.getID(paramRenderScript), paramType.getID(paramRenderScript));
            if (l != 0L) {
              return new AllocationAdapter(l, paramRenderScript, paramAllocation, paramType);
            }
            throw new RSRuntimeException("AllocationAdapter creation failed.");
          }
          throw new RSInvalidStateException("Type cannot have dimension larger than the source allocation.");
        }
        throw new RSInvalidStateException("Adapters do not support window types with Mipmaps or Faces.");
      }
      throw new RSInvalidStateException("Element must match Allocation type.");
    }
    throw new RSInvalidStateException("Adapters cannot be nested.");
  }
  
  private void updateOffsets()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = i2;
    if (mSelectedArray != null)
    {
      if (mSelectedArray.length > 0) {
        j = mSelectedArray[0];
      }
      if (mSelectedArray.length > 1) {
        m = mSelectedArray[2];
      }
      if (mSelectedArray.length > 2) {
        i1 = mSelectedArray[2];
      }
      i = j;
      k = m;
      n = i1;
      i3 = i2;
      if (mSelectedArray.length > 3)
      {
        i3 = mSelectedArray[3];
        n = i1;
        k = m;
        i = j;
      }
    }
    mRS.nAllocationAdapterOffset(getID(mRS), mSelectedX, mSelectedY, mSelectedZ, mSelectedLOD, mSelectedFace.mID, i, k, n, i3);
  }
  
  void initLOD(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = mAdaptedAllocation.mType.getX();
      int j = mAdaptedAllocation.mType.getY();
      int k = mAdaptedAllocation.mType.getZ();
      int m = 0;
      while (m < paramInt)
      {
        if ((i == 1) && (j == 1) && (k == 1))
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Attempting to set lod (");
          localStringBuilder.append(paramInt);
          localStringBuilder.append(") out of range.");
          throw new RSIllegalArgumentException(localStringBuilder.toString());
        }
        int n = i;
        if (i > 1) {
          n = i >> 1;
        }
        int i1 = j;
        if (j > 1) {
          i1 = j >> 1;
        }
        int i2 = k;
        if (k > 1) {
          i2 = k >> 1;
        }
        m++;
        i = n;
        j = i1;
        k = i2;
      }
      mCurrentDimX = i;
      mCurrentDimY = j;
      mCurrentDimZ = k;
      mCurrentCount = mCurrentDimX;
      if (mCurrentDimY > 1) {
        mCurrentCount *= mCurrentDimY;
      }
      if (mCurrentDimZ > 1) {
        mCurrentCount *= mCurrentDimZ;
      }
      mSelectedY = 0;
      mSelectedZ = 0;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Attempting to set negative lod (");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(").");
    throw new RSIllegalArgumentException(localStringBuilder.toString());
  }
  
  public void resize(int paramInt)
  {
    try
    {
      RSInvalidStateException localRSInvalidStateException = new android/renderscript/RSInvalidStateException;
      localRSInvalidStateException.<init>("Resize not allowed for Adapters.");
      throw localRSInvalidStateException;
    }
    finally {}
  }
  
  public void setArray(int paramInt1, int paramInt2)
  {
    if (mAdaptedAllocation.getType().getArray(paramInt1) != 0)
    {
      if (mAdaptedAllocation.getType().getArray(paramInt1) > paramInt2)
      {
        if (mWindow.getArray(paramInt1) != mAdaptedAllocation.getType().getArray(paramInt1))
        {
          if (mWindow.getArray(paramInt1) + paramInt2 < mAdaptedAllocation.getType().getArray(paramInt1))
          {
            mSelectedArray[paramInt1] = paramInt2;
            updateOffsets();
            return;
          }
          throw new RSInvalidStateException("Cannot set (arrayNum + window) which would be larger than dimension of allocation.");
        }
        throw new RSInvalidStateException("Cannot set arrayNum when the adapter includes arrayNum.");
      }
      throw new RSInvalidStateException("Cannot set arrayNum greater than dimension of allocation.");
    }
    throw new RSInvalidStateException("Cannot set arrayNum when the allocation type does not include arrayNum dim.");
  }
  
  public void setFace(Type.CubemapFace paramCubemapFace)
  {
    if (mAdaptedAllocation.getType().hasFaces())
    {
      if (!mWindow.hasFaces())
      {
        if (paramCubemapFace != null)
        {
          mSelectedFace = paramCubemapFace;
          updateOffsets();
          return;
        }
        throw new RSIllegalArgumentException("Cannot set null face.");
      }
      throw new RSInvalidStateException("Cannot set face when the adapter includes faces.");
    }
    throw new RSInvalidStateException("Cannot set Face when the allocation type does not include faces.");
  }
  
  public void setLOD(int paramInt)
  {
    if (mAdaptedAllocation.getType().hasMipmaps())
    {
      if (!mWindow.hasMipmaps())
      {
        initLOD(paramInt);
        mSelectedLOD = paramInt;
        updateOffsets();
        return;
      }
      throw new RSInvalidStateException("Cannot set LOD when the adapter includes mipmaps.");
    }
    throw new RSInvalidStateException("Cannot set LOD when the allocation type does not include mipmaps.");
  }
  
  public void setX(int paramInt)
  {
    if (mAdaptedAllocation.getType().getX() > paramInt)
    {
      if (mWindow.getX() != mAdaptedAllocation.getType().getX())
      {
        if (mWindow.getX() + paramInt < mAdaptedAllocation.getType().getX())
        {
          mSelectedX = paramInt;
          updateOffsets();
          return;
        }
        throw new RSInvalidStateException("Cannot set (X + window) which would be larger than dimension of allocation.");
      }
      throw new RSInvalidStateException("Cannot set X when the adapter includes X.");
    }
    throw new RSInvalidStateException("Cannot set X greater than dimension of allocation.");
  }
  
  public void setY(int paramInt)
  {
    if (mAdaptedAllocation.getType().getY() != 0)
    {
      if (mAdaptedAllocation.getType().getY() > paramInt)
      {
        if (mWindow.getY() != mAdaptedAllocation.getType().getY())
        {
          if (mWindow.getY() + paramInt < mAdaptedAllocation.getType().getY())
          {
            mSelectedY = paramInt;
            updateOffsets();
            return;
          }
          throw new RSInvalidStateException("Cannot set (Y + window) which would be larger than dimension of allocation.");
        }
        throw new RSInvalidStateException("Cannot set Y when the adapter includes Y.");
      }
      throw new RSInvalidStateException("Cannot set Y greater than dimension of allocation.");
    }
    throw new RSInvalidStateException("Cannot set Y when the allocation type does not include Y dim.");
  }
  
  public void setZ(int paramInt)
  {
    if (mAdaptedAllocation.getType().getZ() != 0)
    {
      if (mAdaptedAllocation.getType().getZ() > paramInt)
      {
        if (mWindow.getZ() != mAdaptedAllocation.getType().getZ())
        {
          if (mWindow.getZ() + paramInt < mAdaptedAllocation.getType().getZ())
          {
            mSelectedZ = paramInt;
            updateOffsets();
            return;
          }
          throw new RSInvalidStateException("Cannot set (Z + window) which would be larger than dimension of allocation.");
        }
        throw new RSInvalidStateException("Cannot set Z when the adapter includes Z.");
      }
      throw new RSInvalidStateException("Cannot set Z greater than dimension of allocation.");
    }
    throw new RSInvalidStateException("Cannot set Z when the allocation type does not include Z dim.");
  }
}
