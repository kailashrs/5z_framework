package android.animation;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;

public class PathKeyframes
  implements Keyframes
{
  private static final ArrayList<Keyframe> EMPTY_KEYFRAMES = new ArrayList();
  private static final int FRACTION_OFFSET = 0;
  private static final int NUM_COMPONENTS = 3;
  private static final int X_OFFSET = 1;
  private static final int Y_OFFSET = 2;
  private float[] mKeyframeData;
  private PointF mTempPointF = new PointF();
  
  public PathKeyframes(Path paramPath)
  {
    this(paramPath, 0.5F);
  }
  
  public PathKeyframes(Path paramPath, float paramFloat)
  {
    if ((paramPath != null) && (!paramPath.isEmpty()))
    {
      mKeyframeData = paramPath.approximate(paramFloat);
      return;
    }
    throw new IllegalArgumentException("The path must not be null or empty");
  }
  
  private static float interpolate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (paramFloat3 - paramFloat2) * paramFloat1 + paramFloat2;
  }
  
  private PointF interpolateInRange(float paramFloat, int paramInt1, int paramInt2)
  {
    paramInt1 *= 3;
    paramInt2 *= 3;
    float f1 = mKeyframeData[(paramInt1 + 0)];
    f1 = (paramFloat - f1) / (mKeyframeData[(paramInt2 + 0)] - f1);
    float f2 = mKeyframeData[(paramInt1 + 1)];
    float f3 = mKeyframeData[(paramInt2 + 1)];
    float f4 = mKeyframeData[(paramInt1 + 2)];
    paramFloat = mKeyframeData[(paramInt2 + 2)];
    f3 = interpolate(f1, f2, f3);
    paramFloat = interpolate(f1, f4, paramFloat);
    mTempPointF.set(f3, paramFloat);
    return mTempPointF;
  }
  
  private PointF pointForIndex(int paramInt)
  {
    paramInt *= 3;
    mTempPointF.set(mKeyframeData[(paramInt + 1)], mKeyframeData[(paramInt + 2)]);
    return mTempPointF;
  }
  
  public Keyframes clone()
  {
    Object localObject = null;
    try
    {
      Keyframes localKeyframes = (Keyframes)super.clone();
      localObject = localKeyframes;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return localObject;
  }
  
  public Keyframes.FloatKeyframes createXFloatKeyframes()
  {
    new FloatKeyframesBase()
    {
      public float getFloatValue(float paramAnonymousFloat)
      {
        return getValue(paramAnonymousFloat)).x;
      }
    };
  }
  
  public Keyframes.IntKeyframes createXIntKeyframes()
  {
    new IntKeyframesBase()
    {
      public int getIntValue(float paramAnonymousFloat)
      {
        return Math.round(getValue(paramAnonymousFloat)).x);
      }
    };
  }
  
  public Keyframes.FloatKeyframes createYFloatKeyframes()
  {
    new FloatKeyframesBase()
    {
      public float getFloatValue(float paramAnonymousFloat)
      {
        return getValue(paramAnonymousFloat)).y;
      }
    };
  }
  
  public Keyframes.IntKeyframes createYIntKeyframes()
  {
    new IntKeyframesBase()
    {
      public int getIntValue(float paramAnonymousFloat)
      {
        return Math.round(getValue(paramAnonymousFloat)).y);
      }
    };
  }
  
  public ArrayList<Keyframe> getKeyframes()
  {
    return EMPTY_KEYFRAMES;
  }
  
  public Class getType()
  {
    return PointF.class;
  }
  
  public Object getValue(float paramFloat)
  {
    int i = mKeyframeData.length / 3;
    if (paramFloat < 0.0F) {
      return interpolateInRange(paramFloat, 0, 1);
    }
    if (paramFloat > 1.0F) {
      return interpolateInRange(paramFloat, i - 2, i - 1);
    }
    if (paramFloat == 0.0F) {
      return pointForIndex(0);
    }
    if (paramFloat == 1.0F) {
      return pointForIndex(i - 1);
    }
    int j = 0;
    i--;
    while (j <= i)
    {
      int k = (j + i) / 2;
      float f = mKeyframeData[(k * 3 + 0)];
      if (paramFloat < f)
      {
        i = k - 1;
      }
      else
      {
        if (paramFloat <= f) {
          break label126;
        }
        j = k + 1;
      }
      continue;
      label126:
      return pointForIndex(k);
    }
    return interpolateInRange(paramFloat, i, j);
  }
  
  public void setEvaluator(TypeEvaluator paramTypeEvaluator) {}
  
  static abstract class FloatKeyframesBase
    extends PathKeyframes.SimpleKeyframes
    implements Keyframes.FloatKeyframes
  {
    FloatKeyframesBase()
    {
      super();
    }
    
    public Class getType()
    {
      return Float.class;
    }
    
    public Object getValue(float paramFloat)
    {
      return Float.valueOf(getFloatValue(paramFloat));
    }
  }
  
  static abstract class IntKeyframesBase
    extends PathKeyframes.SimpleKeyframes
    implements Keyframes.IntKeyframes
  {
    IntKeyframesBase()
    {
      super();
    }
    
    public Class getType()
    {
      return Integer.class;
    }
    
    public Object getValue(float paramFloat)
    {
      return Integer.valueOf(getIntValue(paramFloat));
    }
  }
  
  private static abstract class SimpleKeyframes
    implements Keyframes
  {
    private SimpleKeyframes() {}
    
    public Keyframes clone()
    {
      Object localObject = null;
      try
      {
        Keyframes localKeyframes = (Keyframes)super.clone();
        localObject = localKeyframes;
      }
      catch (CloneNotSupportedException localCloneNotSupportedException) {}
      return localObject;
    }
    
    public ArrayList<Keyframe> getKeyframes()
    {
      return PathKeyframes.EMPTY_KEYFRAMES;
    }
    
    public void setEvaluator(TypeEvaluator paramTypeEvaluator) {}
  }
}
