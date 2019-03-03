package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;

public class RoundRectShape
  extends RectShape
{
  private float[] mInnerRadii;
  private RectF mInnerRect;
  private RectF mInset;
  private float[] mOuterRadii;
  private Path mPath;
  
  public RoundRectShape(float[] paramArrayOfFloat1, RectF paramRectF, float[] paramArrayOfFloat2)
  {
    if ((paramArrayOfFloat1 != null) && (paramArrayOfFloat1.length < 8)) {
      throw new ArrayIndexOutOfBoundsException("outer radii must have >= 8 values");
    }
    if ((paramArrayOfFloat2 != null) && (paramArrayOfFloat2.length < 8)) {
      throw new ArrayIndexOutOfBoundsException("inner radii must have >= 8 values");
    }
    mOuterRadii = paramArrayOfFloat1;
    mInset = paramRectF;
    mInnerRadii = paramArrayOfFloat2;
    if (paramRectF != null) {
      mInnerRect = new RectF();
    }
    mPath = new Path();
  }
  
  public RoundRectShape clone()
    throws CloneNotSupportedException
  {
    RoundRectShape localRoundRectShape = (RoundRectShape)super.clone();
    Object localObject1 = mOuterRadii;
    Object localObject2 = null;
    if (localObject1 != null) {
      localObject1 = (float[])mOuterRadii.clone();
    } else {
      localObject1 = null;
    }
    mOuterRadii = ((float[])localObject1);
    localObject1 = localObject2;
    if (mInnerRadii != null) {
      localObject1 = (float[])mInnerRadii.clone();
    }
    mInnerRadii = ((float[])localObject1);
    mInset = new RectF(mInset);
    mInnerRect = new RectF(mInnerRect);
    mPath = new Path(mPath);
    return localRoundRectShape;
  }
  
  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    paramCanvas.drawPath(mPath, paramPaint);
  }
  
  public void getOutline(Outline paramOutline)
  {
    if (mInnerRect != null) {
      return;
    }
    float f1 = 0.0F;
    if (mOuterRadii != null)
    {
      float f2 = mOuterRadii[0];
      for (int i = 1;; i++)
      {
        f1 = f2;
        if (i >= 8) {
          break;
        }
        if (mOuterRadii[i] != f2)
        {
          paramOutline.setConvexPath(mPath);
          return;
        }
      }
    }
    RectF localRectF = rect();
    paramOutline.setRoundRect((int)Math.ceil(left), (int)Math.ceil(top), (int)Math.floor(right), (int)Math.floor(bottom), f1);
  }
  
  protected void onResize(float paramFloat1, float paramFloat2)
  {
    super.onResize(paramFloat1, paramFloat2);
    RectF localRectF = rect();
    mPath.reset();
    if (mOuterRadii != null) {
      mPath.addRoundRect(localRectF, mOuterRadii, Path.Direction.CW);
    } else {
      mPath.addRect(localRectF, Path.Direction.CW);
    }
    if (mInnerRect != null)
    {
      mInnerRect.set(left + mInset.left, top + mInset.top, right - mInset.right, bottom - mInset.bottom);
      if ((mInnerRect.width() < paramFloat1) && (mInnerRect.height() < paramFloat2)) {
        if (mInnerRadii != null) {
          mPath.addRoundRect(mInnerRect, mInnerRadii, Path.Direction.CCW);
        } else {
          mPath.addRect(mInnerRect, Path.Direction.CCW);
        }
      }
    }
  }
}
