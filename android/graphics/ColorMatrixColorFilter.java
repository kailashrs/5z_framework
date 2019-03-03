package android.graphics;

public class ColorMatrixColorFilter
  extends ColorFilter
{
  private final ColorMatrix mMatrix = new ColorMatrix();
  
  public ColorMatrixColorFilter(ColorMatrix paramColorMatrix)
  {
    mMatrix.set(paramColorMatrix);
  }
  
  public ColorMatrixColorFilter(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length >= 20)
    {
      mMatrix.set(paramArrayOfFloat);
      return;
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  private static native long nativeColorMatrixFilter(float[] paramArrayOfFloat);
  
  long createNativeInstance()
  {
    return nativeColorMatrixFilter(mMatrix.getArray());
  }
  
  public void getColorMatrix(ColorMatrix paramColorMatrix)
  {
    paramColorMatrix.set(mMatrix);
  }
  
  public void setColorMatrix(ColorMatrix paramColorMatrix)
  {
    discardNativeInstance();
    if (paramColorMatrix == null) {
      mMatrix.reset();
    } else {
      mMatrix.set(paramColorMatrix);
    }
  }
  
  public void setColorMatrixArray(float[] paramArrayOfFloat)
  {
    discardNativeInstance();
    if (paramArrayOfFloat == null)
    {
      mMatrix.reset();
    }
    else
    {
      if (paramArrayOfFloat.length < 20) {
        break label34;
      }
      mMatrix.set(paramArrayOfFloat);
    }
    return;
    label34:
    throw new ArrayIndexOutOfBoundsException();
  }
}
