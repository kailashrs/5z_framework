package android.view.animation;

import android.graphics.Matrix;
import android.graphics.Rect;
import java.io.PrintWriter;

public class Transformation
{
  public static final int TYPE_ALPHA = 1;
  public static final int TYPE_BOTH = 3;
  public static final int TYPE_IDENTITY = 0;
  public static final int TYPE_MATRIX = 2;
  protected float mAlpha;
  private Rect mClipRect = new Rect();
  private boolean mHasClipRect;
  protected Matrix mMatrix;
  protected int mTransformationType;
  
  public Transformation()
  {
    clear();
  }
  
  public void clear()
  {
    if (mMatrix == null) {
      mMatrix = new Matrix();
    } else {
      mMatrix.reset();
    }
    mClipRect.setEmpty();
    mHasClipRect = false;
    mAlpha = 1.0F;
    mTransformationType = 3;
  }
  
  public void compose(Transformation paramTransformation)
  {
    mAlpha *= paramTransformation.getAlpha();
    mMatrix.preConcat(paramTransformation.getMatrix());
    if (mHasClipRect)
    {
      paramTransformation = paramTransformation.getClipRect();
      if (mHasClipRect) {
        setClipRect(mClipRect.left + left, mClipRect.top + top, mClipRect.right + right, mClipRect.bottom + bottom);
      } else {
        setClipRect(paramTransformation);
      }
    }
  }
  
  public float getAlpha()
  {
    return mAlpha;
  }
  
  public Rect getClipRect()
  {
    return mClipRect;
  }
  
  public Matrix getMatrix()
  {
    return mMatrix;
  }
  
  public int getTransformationType()
  {
    return mTransformationType;
  }
  
  public boolean hasClipRect()
  {
    return mHasClipRect;
  }
  
  public void postCompose(Transformation paramTransformation)
  {
    mAlpha *= paramTransformation.getAlpha();
    mMatrix.postConcat(paramTransformation.getMatrix());
    if (mHasClipRect)
    {
      paramTransformation = paramTransformation.getClipRect();
      if (mHasClipRect) {
        setClipRect(mClipRect.left + left, mClipRect.top + top, mClipRect.right + right, mClipRect.bottom + bottom);
      } else {
        setClipRect(paramTransformation);
      }
    }
  }
  
  public void printShortString(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print("{alpha=");
    paramPrintWriter.print(mAlpha);
    paramPrintWriter.print(" matrix=");
    mMatrix.printShortString(paramPrintWriter);
    paramPrintWriter.print('}');
  }
  
  public void set(Transformation paramTransformation)
  {
    mAlpha = paramTransformation.getAlpha();
    mMatrix.set(paramTransformation.getMatrix());
    if (mHasClipRect)
    {
      setClipRect(paramTransformation.getClipRect());
    }
    else
    {
      mHasClipRect = false;
      mClipRect.setEmpty();
    }
    mTransformationType = paramTransformation.getTransformationType();
  }
  
  public void setAlpha(float paramFloat)
  {
    mAlpha = paramFloat;
  }
  
  public void setClipRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mClipRect.set(paramInt1, paramInt2, paramInt3, paramInt4);
    mHasClipRect = true;
  }
  
  public void setClipRect(Rect paramRect)
  {
    setClipRect(left, top, right, bottom);
  }
  
  public void setTransformationType(int paramInt)
  {
    mTransformationType = paramInt;
  }
  
  public String toShortString()
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    toShortString(localStringBuilder);
    return localStringBuilder.toString();
  }
  
  public void toShortString(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append("{alpha=");
    paramStringBuilder.append(mAlpha);
    paramStringBuilder.append(" matrix=");
    mMatrix.toShortString(paramStringBuilder);
    paramStringBuilder.append('}');
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    localStringBuilder.append("Transformation");
    toShortString(localStringBuilder);
    return localStringBuilder.toString();
  }
}
