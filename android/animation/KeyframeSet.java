package android.animation;

import android.graphics.Path;
import android.util.Log;
import java.util.Arrays;
import java.util.List;

public class KeyframeSet
  implements Keyframes
{
  TypeEvaluator mEvaluator;
  Keyframe mFirstKeyframe;
  TimeInterpolator mInterpolator;
  List<Keyframe> mKeyframes;
  Keyframe mLastKeyframe;
  int mNumKeyframes;
  
  public KeyframeSet(Keyframe... paramVarArgs)
  {
    mNumKeyframes = paramVarArgs.length;
    mKeyframes = Arrays.asList(paramVarArgs);
    mFirstKeyframe = paramVarArgs[0];
    mLastKeyframe = paramVarArgs[(mNumKeyframes - 1)];
    mInterpolator = mLastKeyframe.getInterpolator();
  }
  
  public static KeyframeSet ofFloat(float... paramVarArgs)
  {
    int i = 0;
    int j = 0;
    int k = paramVarArgs.length;
    Keyframe.FloatKeyframe[] arrayOfFloatKeyframe = new Keyframe.FloatKeyframe[Math.max(k, 2)];
    int m = 1;
    if (k == 1)
    {
      arrayOfFloatKeyframe[0] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F));
      arrayOfFloatKeyframe[1] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(1.0F, paramVarArgs[0]));
      if (Float.isNaN(paramVarArgs[0])) {
        i = 1;
      }
    }
    else
    {
      arrayOfFloatKeyframe[0] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F, paramVarArgs[0]));
      for (;;)
      {
        i = j;
        if (m >= k) {
          break;
        }
        arrayOfFloatKeyframe[m] = ((Keyframe.FloatKeyframe)Keyframe.ofFloat(m / (k - 1), paramVarArgs[m]));
        if (Float.isNaN(paramVarArgs[m])) {
          j = 1;
        }
        m++;
      }
    }
    if (i != 0) {
      Log.w("Animator", "Bad value (NaN) in float animator");
    }
    return new FloatKeyframeSet(arrayOfFloatKeyframe);
  }
  
  public static KeyframeSet ofInt(int... paramVarArgs)
  {
    int i = paramVarArgs.length;
    Keyframe.IntKeyframe[] arrayOfIntKeyframe = new Keyframe.IntKeyframe[Math.max(i, 2)];
    int j = 1;
    if (i == 1)
    {
      arrayOfIntKeyframe[0] = ((Keyframe.IntKeyframe)Keyframe.ofInt(0.0F));
      arrayOfIntKeyframe[1] = ((Keyframe.IntKeyframe)Keyframe.ofInt(1.0F, paramVarArgs[0]));
    }
    else
    {
      arrayOfIntKeyframe[0] = ((Keyframe.IntKeyframe)Keyframe.ofInt(0.0F, paramVarArgs[0]));
      while (j < i)
      {
        arrayOfIntKeyframe[j] = ((Keyframe.IntKeyframe)Keyframe.ofInt(j / (i - 1), paramVarArgs[j]));
        j++;
      }
    }
    return new IntKeyframeSet(arrayOfIntKeyframe);
  }
  
  public static KeyframeSet ofKeyframe(Keyframe... paramVarArgs)
  {
    int i = paramVarArgs.length;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    for (int i2 = 0; i2 < i; i2++) {
      if ((paramVarArgs[i2] instanceof Keyframe.FloatKeyframe)) {
        i1 = 1;
      } else if ((paramVarArgs[i2] instanceof Keyframe.IntKeyframe)) {
        n = 1;
      } else {
        m = 1;
      }
    }
    Object localObject;
    if ((i1 != 0) && (n == 0) && (m == 0))
    {
      localObject = new Keyframe.FloatKeyframe[i];
      for (m = k; m < i; m++) {
        localObject[m] = ((Keyframe.FloatKeyframe)paramVarArgs[m]);
      }
      return new FloatKeyframeSet((Keyframe.FloatKeyframe[])localObject);
    }
    if ((n != 0) && (i1 == 0) && (m == 0))
    {
      localObject = new Keyframe.IntKeyframe[i];
      for (m = j; m < i; m++) {
        localObject[m] = ((Keyframe.IntKeyframe)paramVarArgs[m]);
      }
      return new IntKeyframeSet((Keyframe.IntKeyframe[])localObject);
    }
    return new KeyframeSet(paramVarArgs);
  }
  
  public static KeyframeSet ofObject(Object... paramVarArgs)
  {
    int i = paramVarArgs.length;
    Keyframe.ObjectKeyframe[] arrayOfObjectKeyframe = new Keyframe.ObjectKeyframe[Math.max(i, 2)];
    int j = 1;
    if (i == 1)
    {
      arrayOfObjectKeyframe[0] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F));
      arrayOfObjectKeyframe[1] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(1.0F, paramVarArgs[0]));
    }
    else
    {
      arrayOfObjectKeyframe[0] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F, paramVarArgs[0]));
      while (j < i)
      {
        arrayOfObjectKeyframe[j] = ((Keyframe.ObjectKeyframe)Keyframe.ofObject(j / (i - 1), paramVarArgs[j]));
        j++;
      }
    }
    return new KeyframeSet(arrayOfObjectKeyframe);
  }
  
  public static PathKeyframes ofPath(Path paramPath)
  {
    return new PathKeyframes(paramPath);
  }
  
  public static PathKeyframes ofPath(Path paramPath, float paramFloat)
  {
    return new PathKeyframes(paramPath, paramFloat);
  }
  
  public KeyframeSet clone()
  {
    List localList = mKeyframes;
    int i = mKeyframes.size();
    Keyframe[] arrayOfKeyframe = new Keyframe[i];
    for (int j = 0; j < i; j++) {
      arrayOfKeyframe[j] = ((Keyframe)localList.get(j)).clone();
    }
    return new KeyframeSet(arrayOfKeyframe);
  }
  
  public List<Keyframe> getKeyframes()
  {
    return mKeyframes;
  }
  
  public Class getType()
  {
    return mFirstKeyframe.getType();
  }
  
  public Object getValue(float paramFloat)
  {
    float f;
    if (mNumKeyframes == 2)
    {
      f = paramFloat;
      if (mInterpolator != null) {
        f = mInterpolator.getInterpolation(paramFloat);
      }
      return mEvaluator.evaluate(f, mFirstKeyframe.getValue(), mLastKeyframe.getValue());
    }
    int i = 1;
    Object localObject2;
    if (paramFloat <= 0.0F)
    {
      localObject1 = (Keyframe)mKeyframes.get(1);
      localObject2 = ((Keyframe)localObject1).getInterpolator();
      f = paramFloat;
      if (localObject2 != null) {
        f = ((TimeInterpolator)localObject2).getInterpolation(paramFloat);
      }
      paramFloat = mFirstKeyframe.getFraction();
      paramFloat = (f - paramFloat) / (((Keyframe)localObject1).getFraction() - paramFloat);
      return mEvaluator.evaluate(paramFloat, mFirstKeyframe.getValue(), ((Keyframe)localObject1).getValue());
    }
    if (paramFloat >= 1.0F)
    {
      localObject2 = (Keyframe)mKeyframes.get(mNumKeyframes - 2);
      localObject1 = mLastKeyframe.getInterpolator();
      f = paramFloat;
      if (localObject1 != null) {
        f = ((TimeInterpolator)localObject1).getInterpolation(paramFloat);
      }
      paramFloat = ((Keyframe)localObject2).getFraction();
      paramFloat = (f - paramFloat) / (mLastKeyframe.getFraction() - paramFloat);
      return mEvaluator.evaluate(paramFloat, ((Keyframe)localObject2).getValue(), mLastKeyframe.getValue());
    }
    Object localObject1 = mFirstKeyframe;
    while (i < mNumKeyframes)
    {
      localObject2 = (Keyframe)mKeyframes.get(i);
      if (paramFloat < ((Keyframe)localObject2).getFraction())
      {
        TimeInterpolator localTimeInterpolator = ((Keyframe)localObject2).getInterpolator();
        f = ((Keyframe)localObject1).getFraction();
        f = (paramFloat - f) / (((Keyframe)localObject2).getFraction() - f);
        paramFloat = f;
        if (localTimeInterpolator != null) {
          paramFloat = localTimeInterpolator.getInterpolation(f);
        }
        return mEvaluator.evaluate(paramFloat, ((Keyframe)localObject1).getValue(), ((Keyframe)localObject2).getValue());
      }
      localObject1 = localObject2;
      i++;
    }
    return mLastKeyframe.getValue();
  }
  
  public void setEvaluator(TypeEvaluator paramTypeEvaluator)
  {
    mEvaluator = paramTypeEvaluator;
  }
  
  public String toString()
  {
    String str = " ";
    for (int i = 0; i < mNumKeyframes; i++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str);
      localStringBuilder.append(((Keyframe)mKeyframes.get(i)).getValue());
      localStringBuilder.append("  ");
      str = localStringBuilder.toString();
    }
    return str;
  }
}
