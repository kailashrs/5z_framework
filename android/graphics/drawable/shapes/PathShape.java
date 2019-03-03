package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class PathShape
  extends Shape
{
  private Path mPath;
  private float mScaleX;
  private float mScaleY;
  private final float mStdHeight;
  private final float mStdWidth;
  
  public PathShape(Path paramPath, float paramFloat1, float paramFloat2)
  {
    mPath = paramPath;
    mStdWidth = paramFloat1;
    mStdHeight = paramFloat2;
  }
  
  public PathShape clone()
    throws CloneNotSupportedException
  {
    PathShape localPathShape = (PathShape)super.clone();
    mPath = new Path(mPath);
    return localPathShape;
  }
  
  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    paramCanvas.save();
    paramCanvas.scale(mScaleX, mScaleY);
    paramCanvas.drawPath(mPath, paramPaint);
    paramCanvas.restore();
  }
  
  protected void onResize(float paramFloat1, float paramFloat2)
  {
    mScaleX = (paramFloat1 / mStdWidth);
    mScaleY = (paramFloat2 / mStdHeight);
  }
}
