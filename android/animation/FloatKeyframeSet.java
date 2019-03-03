package android.animation;

import java.util.List;

class FloatKeyframeSet
  extends KeyframeSet
  implements Keyframes.FloatKeyframes
{
  public FloatKeyframeSet(Keyframe.FloatKeyframe... paramVarArgs)
  {
    super(paramVarArgs);
  }
  
  public FloatKeyframeSet clone()
  {
    List localList = mKeyframes;
    int i = mKeyframes.size();
    Keyframe.FloatKeyframe[] arrayOfFloatKeyframe = new Keyframe.FloatKeyframe[i];
    for (int j = 0; j < i; j++) {
      arrayOfFloatKeyframe[j] = ((Keyframe.FloatKeyframe)((Keyframe)localList.get(j)).clone());
    }
    return new FloatKeyframeSet(arrayOfFloatKeyframe);
  }
  
  public float getFloatValue(float paramFloat)
  {
    Keyframe.FloatKeyframe localFloatKeyframe;
    float f1;
    float f2;
    float f3;
    float f4;
    float f5;
    if (paramFloat <= 0.0F)
    {
      localFloatKeyframe = (Keyframe.FloatKeyframe)mKeyframes.get(0);
      localObject = (Keyframe.FloatKeyframe)mKeyframes.get(1);
      f1 = localFloatKeyframe.getFloatValue();
      f2 = ((Keyframe.FloatKeyframe)localObject).getFloatValue();
      f3 = localFloatKeyframe.getFraction();
      f4 = ((Keyframe.FloatKeyframe)localObject).getFraction();
      localObject = ((Keyframe.FloatKeyframe)localObject).getInterpolator();
      f5 = paramFloat;
      if (localObject != null) {
        f5 = ((TimeInterpolator)localObject).getInterpolation(paramFloat);
      }
      paramFloat = (f5 - f3) / (f4 - f3);
      if (mEvaluator == null) {
        paramFloat = (f2 - f1) * paramFloat + f1;
      } else {
        paramFloat = ((Number)mEvaluator.evaluate(paramFloat, Float.valueOf(f1), Float.valueOf(f2))).floatValue();
      }
      return paramFloat;
    }
    if (paramFloat >= 1.0F)
    {
      localFloatKeyframe = (Keyframe.FloatKeyframe)mKeyframes.get(mNumKeyframes - 2);
      localObject = (Keyframe.FloatKeyframe)mKeyframes.get(mNumKeyframes - 1);
      f1 = localFloatKeyframe.getFloatValue();
      f2 = ((Keyframe.FloatKeyframe)localObject).getFloatValue();
      f3 = localFloatKeyframe.getFraction();
      f4 = ((Keyframe.FloatKeyframe)localObject).getFraction();
      localObject = ((Keyframe.FloatKeyframe)localObject).getInterpolator();
      f5 = paramFloat;
      if (localObject != null) {
        f5 = ((TimeInterpolator)localObject).getInterpolation(paramFloat);
      }
      paramFloat = (f5 - f3) / (f4 - f3);
      if (mEvaluator == null) {
        paramFloat = (f2 - f1) * paramFloat + f1;
      } else {
        paramFloat = ((Number)mEvaluator.evaluate(paramFloat, Float.valueOf(f1), Float.valueOf(f2))).floatValue();
      }
      return paramFloat;
    }
    Object localObject = (Keyframe.FloatKeyframe)mKeyframes.get(0);
    for (int i = 1; i < mNumKeyframes; i++)
    {
      localFloatKeyframe = (Keyframe.FloatKeyframe)mKeyframes.get(i);
      if (paramFloat < localFloatKeyframe.getFraction())
      {
        TimeInterpolator localTimeInterpolator = localFloatKeyframe.getInterpolator();
        f5 = (paramFloat - ((Keyframe.FloatKeyframe)localObject).getFraction()) / (localFloatKeyframe.getFraction() - ((Keyframe.FloatKeyframe)localObject).getFraction());
        f2 = ((Keyframe.FloatKeyframe)localObject).getFloatValue();
        f1 = localFloatKeyframe.getFloatValue();
        paramFloat = f5;
        if (localTimeInterpolator != null) {
          paramFloat = localTimeInterpolator.getInterpolation(f5);
        }
        if (mEvaluator == null) {
          paramFloat = (f1 - f2) * paramFloat + f2;
        } else {
          paramFloat = ((Number)mEvaluator.evaluate(paramFloat, Float.valueOf(f2), Float.valueOf(f1))).floatValue();
        }
        return paramFloat;
      }
      localObject = localFloatKeyframe;
    }
    return ((Number)((Keyframe)mKeyframes.get(mNumKeyframes - 1)).getValue()).floatValue();
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
