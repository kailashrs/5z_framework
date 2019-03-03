package android.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class ArcMotion
  extends PathMotion
{
  private static final float DEFAULT_MAX_ANGLE_DEGREES = 70.0F;
  private static final float DEFAULT_MAX_TANGENT = (float)Math.tan(Math.toRadians(35.0D));
  private static final float DEFAULT_MIN_ANGLE_DEGREES = 0.0F;
  private float mMaximumAngle = 70.0F;
  private float mMaximumTangent = DEFAULT_MAX_TANGENT;
  private float mMinimumHorizontalAngle = 0.0F;
  private float mMinimumHorizontalTangent = 0.0F;
  private float mMinimumVerticalAngle = 0.0F;
  private float mMinimumVerticalTangent = 0.0F;
  
  public ArcMotion() {}
  
  public ArcMotion(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ArcMotion);
    setMinimumVerticalAngle(paramContext.getFloat(1, 0.0F));
    setMinimumHorizontalAngle(paramContext.getFloat(0, 0.0F));
    setMaximumAngle(paramContext.getFloat(2, 70.0F));
    paramContext.recycle();
  }
  
  private static float toTangent(float paramFloat)
  {
    if ((paramFloat >= 0.0F) && (paramFloat <= 90.0F)) {
      return (float)Math.tan(Math.toRadians(paramFloat / 2.0F));
    }
    throw new IllegalArgumentException("Arc must be between 0 and 90 degrees");
  }
  
  public float getMaximumAngle()
  {
    return mMaximumAngle;
  }
  
  public float getMinimumHorizontalAngle()
  {
    return mMinimumHorizontalAngle;
  }
  
  public float getMinimumVerticalAngle()
  {
    return mMinimumVerticalAngle;
  }
  
  public Path getPath(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    Path localPath = new Path();
    localPath.moveTo(paramFloat1, paramFloat2);
    float f1 = paramFloat3 - paramFloat1;
    float f2 = paramFloat4 - paramFloat2;
    float f3 = f1 * f1 + f2 * f2;
    float f4 = (paramFloat1 + paramFloat3) / 2.0F;
    float f5 = (paramFloat2 + paramFloat4) / 2.0F;
    float f6 = f3 * 0.25F;
    float f7 = 0.0F;
    int i;
    if (paramFloat2 > paramFloat4) {
      i = 1;
    } else {
      i = 0;
    }
    if (f2 == 0.0F)
    {
      f3 = f4;
      f1 = Math.abs(f1) * 0.5F * mMinimumHorizontalTangent + f5;
    }
    for (;;)
    {
      break;
      if (f1 == 0.0F)
      {
        f3 = Math.abs(f2) * 0.5F * mMinimumVerticalTangent + f4;
        f1 = f5;
      }
      else if (Math.abs(f1) < Math.abs(f2))
      {
        f3 = Math.abs(f3 / (2.0F * f2));
        if (i != 0)
        {
          f3 = paramFloat4 + f3;
          f1 = paramFloat3;
        }
        else
        {
          f3 = paramFloat2 + f3;
          f1 = paramFloat1;
        }
        f7 = mMinimumVerticalTangent;
        f2 = mMinimumVerticalTangent;
        f2 = f7 * f6 * f2;
        f7 = f1;
        f1 = f3;
        f3 = f7;
        f7 = f2;
      }
      else
      {
        f3 /= 2.0F * f1;
        if (i != 0)
        {
          f3 = paramFloat1 + f3;
          f1 = paramFloat2;
        }
        else
        {
          f3 = paramFloat3 - f3;
          f1 = paramFloat4;
        }
        f7 = mMinimumHorizontalTangent * f6 * mMinimumHorizontalTangent;
      }
    }
    float f8 = f4 - f3;
    f2 = f5 - f1;
    f8 = f8 * f8 + f2 * f2;
    f2 = mMaximumTangent * f6 * mMaximumTangent;
    f6 = 0.0F;
    if ((f8 == 0.0F) || (f8 >= f7))
    {
      f7 = f6;
      if (f8 > f2) {
        f7 = f2;
      }
    }
    f6 = f3;
    f2 = f1;
    if (f7 != 0.0F)
    {
      f7 = (float)Math.sqrt(f7 / f8);
      f6 = f4 + (f3 - f4) * f7;
      f2 = f5 + (f1 - f5) * f7;
    }
    localPath.cubicTo((paramFloat1 + f6) / 2.0F, (paramFloat2 + f2) / 2.0F, (f6 + paramFloat3) / 2.0F, (f2 + paramFloat4) / 2.0F, paramFloat3, paramFloat4);
    return localPath;
  }
  
  public void setMaximumAngle(float paramFloat)
  {
    mMaximumAngle = paramFloat;
    mMaximumTangent = toTangent(paramFloat);
  }
  
  public void setMinimumHorizontalAngle(float paramFloat)
  {
    mMinimumHorizontalAngle = paramFloat;
    mMinimumHorizontalTangent = toTangent(paramFloat);
  }
  
  public void setMinimumVerticalAngle(float paramFloat)
  {
    mMinimumVerticalAngle = paramFloat;
    mMinimumVerticalTangent = toTangent(paramFloat);
  }
}
