package android.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.PathParser;
import com.android.internal.R.styleable;

public class PatternPathMotion
  extends PathMotion
{
  private Path mOriginalPatternPath;
  private final Path mPatternPath = new Path();
  private final Matrix mTempMatrix = new Matrix();
  
  public PatternPathMotion()
  {
    mPatternPath.lineTo(1.0F, 0.0F);
    mOriginalPatternPath = mPatternPath;
  }
  
  public PatternPathMotion(Context paramContext, AttributeSet paramAttributeSet)
  {
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PatternPathMotion);
    try
    {
      paramAttributeSet = paramContext.getString(0);
      if (paramAttributeSet != null)
      {
        setPatternPath(PathParser.createPathFromPathData(paramAttributeSet));
        return;
      }
      paramAttributeSet = new java/lang/RuntimeException;
      paramAttributeSet.<init>("pathData must be supplied for patternPathMotion");
      throw paramAttributeSet;
    }
    finally
    {
      paramContext.recycle();
    }
  }
  
  public PatternPathMotion(Path paramPath)
  {
    setPatternPath(paramPath);
  }
  
  public Path getPath(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    double d1 = paramFloat3 - paramFloat1;
    double d2 = paramFloat4 - paramFloat2;
    paramFloat3 = (float)Math.hypot(d1, d2);
    d2 = Math.atan2(d2, d1);
    mTempMatrix.setScale(paramFloat3, paramFloat3);
    mTempMatrix.postRotate((float)Math.toDegrees(d2));
    mTempMatrix.postTranslate(paramFloat1, paramFloat2);
    Path localPath = new Path();
    mPatternPath.transform(mTempMatrix, localPath);
    return localPath;
  }
  
  public Path getPatternPath()
  {
    return mOriginalPatternPath;
  }
  
  public void setPatternPath(Path paramPath)
  {
    PathMeasure localPathMeasure = new PathMeasure(paramPath, false);
    float f1 = localPathMeasure.getLength();
    float[] arrayOfFloat = new float[2];
    localPathMeasure.getPosTan(f1, arrayOfFloat, null);
    float f2 = arrayOfFloat[0];
    f1 = arrayOfFloat[1];
    localPathMeasure.getPosTan(0.0F, arrayOfFloat, null);
    float f3 = arrayOfFloat[0];
    float f4 = arrayOfFloat[1];
    if ((f3 == f2) && (f4 == f1)) {
      throw new IllegalArgumentException("pattern must not end at the starting point");
    }
    mTempMatrix.setTranslate(-f3, -f4);
    f3 = f2 - f3;
    f1 -= f4;
    f4 = 1.0F / (float)Math.hypot(f3, f1);
    mTempMatrix.postScale(f4, f4);
    double d = Math.atan2(f1, f3);
    mTempMatrix.postRotate((float)Math.toDegrees(-d));
    paramPath.transform(mTempMatrix, mPatternPath);
    mOriginalPatternPath = paramPath;
  }
}
