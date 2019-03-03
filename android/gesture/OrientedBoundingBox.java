package android.gesture;

import android.graphics.Matrix;
import android.graphics.Path;

public class OrientedBoundingBox
{
  public final float centerX;
  public final float centerY;
  public final float height;
  public final float orientation;
  public final float squareness;
  public final float width;
  
  OrientedBoundingBox(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    orientation = paramFloat1;
    width = paramFloat4;
    height = paramFloat5;
    centerX = paramFloat2;
    centerY = paramFloat3;
    paramFloat1 = paramFloat4 / paramFloat5;
    if (paramFloat1 > 1.0F) {
      squareness = (1.0F / paramFloat1);
    } else {
      squareness = paramFloat1;
    }
  }
  
  public Path toPath()
  {
    Path localPath = new Path();
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = (-width / 2.0F);
    arrayOfFloat[1] = (height / 2.0F);
    Matrix localMatrix = new Matrix();
    localMatrix.setRotate(orientation);
    localMatrix.postTranslate(centerX, centerY);
    localMatrix.mapPoints(arrayOfFloat);
    localPath.moveTo(arrayOfFloat[0], arrayOfFloat[1]);
    arrayOfFloat[0] = (-width / 2.0F);
    arrayOfFloat[1] = (-height / 2.0F);
    localMatrix.mapPoints(arrayOfFloat);
    localPath.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
    arrayOfFloat[0] = (width / 2.0F);
    arrayOfFloat[1] = (-height / 2.0F);
    localMatrix.mapPoints(arrayOfFloat);
    localPath.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
    arrayOfFloat[0] = (width / 2.0F);
    arrayOfFloat[1] = (height / 2.0F);
    localMatrix.mapPoints(arrayOfFloat);
    localPath.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
    localPath.close();
    return localPath;
  }
}
