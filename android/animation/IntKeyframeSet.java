package android.animation;

import java.util.List;

class IntKeyframeSet
  extends KeyframeSet
  implements Keyframes.IntKeyframes
{
  public IntKeyframeSet(Keyframe.IntKeyframe... paramVarArgs)
  {
    super(paramVarArgs);
  }
  
  public IntKeyframeSet clone()
  {
    List localList = mKeyframes;
    int i = mKeyframes.size();
    Keyframe.IntKeyframe[] arrayOfIntKeyframe = new Keyframe.IntKeyframe[i];
    for (int j = 0; j < i; j++) {
      arrayOfIntKeyframe[j] = ((Keyframe.IntKeyframe)((Keyframe)localList.get(j)).clone());
    }
    return new IntKeyframeSet(arrayOfIntKeyframe);
  }
  
  public int getIntValue(float paramFloat)
  {
    Keyframe.IntKeyframe localIntKeyframe;
    int i;
    float f1;
    float f2;
    float f3;
    if (paramFloat <= 0.0F)
    {
      localIntKeyframe = (Keyframe.IntKeyframe)mKeyframes.get(0);
      localObject = (Keyframe.IntKeyframe)mKeyframes.get(1);
      i = localIntKeyframe.getIntValue();
      j = ((Keyframe.IntKeyframe)localObject).getIntValue();
      f1 = localIntKeyframe.getFraction();
      f2 = ((Keyframe.IntKeyframe)localObject).getFraction();
      localObject = ((Keyframe.IntKeyframe)localObject).getInterpolator();
      f3 = paramFloat;
      if (localObject != null) {
        f3 = ((TimeInterpolator)localObject).getInterpolation(paramFloat);
      }
      paramFloat = (f3 - f1) / (f2 - f1);
      if (mEvaluator == null) {
        j = (int)((j - i) * paramFloat) + i;
      } else {
        j = ((Number)mEvaluator.evaluate(paramFloat, Integer.valueOf(i), Integer.valueOf(j))).intValue();
      }
      return j;
    }
    if (paramFloat >= 1.0F)
    {
      localIntKeyframe = (Keyframe.IntKeyframe)mKeyframes.get(mNumKeyframes - 2);
      localObject = (Keyframe.IntKeyframe)mKeyframes.get(mNumKeyframes - 1);
      j = localIntKeyframe.getIntValue();
      i = ((Keyframe.IntKeyframe)localObject).getIntValue();
      f2 = localIntKeyframe.getFraction();
      f1 = ((Keyframe.IntKeyframe)localObject).getFraction();
      localObject = ((Keyframe.IntKeyframe)localObject).getInterpolator();
      f3 = paramFloat;
      if (localObject != null) {
        f3 = ((TimeInterpolator)localObject).getInterpolation(paramFloat);
      }
      paramFloat = (f3 - f2) / (f1 - f2);
      if (mEvaluator == null) {
        j = (int)((i - j) * paramFloat) + j;
      } else {
        j = ((Number)mEvaluator.evaluate(paramFloat, Integer.valueOf(j), Integer.valueOf(i))).intValue();
      }
      return j;
    }
    Object localObject = (Keyframe.IntKeyframe)mKeyframes.get(0);
    for (int j = 1; j < mNumKeyframes; j++)
    {
      localIntKeyframe = (Keyframe.IntKeyframe)mKeyframes.get(j);
      if (paramFloat < localIntKeyframe.getFraction())
      {
        TimeInterpolator localTimeInterpolator = localIntKeyframe.getInterpolator();
        f3 = (paramFloat - ((Keyframe.IntKeyframe)localObject).getFraction()) / (localIntKeyframe.getFraction() - ((Keyframe.IntKeyframe)localObject).getFraction());
        j = ((Keyframe.IntKeyframe)localObject).getIntValue();
        i = localIntKeyframe.getIntValue();
        paramFloat = f3;
        if (localTimeInterpolator != null) {
          paramFloat = localTimeInterpolator.getInterpolation(f3);
        }
        if (mEvaluator == null) {
          j = (int)((i - j) * paramFloat) + j;
        } else {
          j = ((Number)mEvaluator.evaluate(paramFloat, Integer.valueOf(j), Integer.valueOf(i))).intValue();
        }
        return j;
      }
      localObject = localIntKeyframe;
    }
    return ((Number)((Keyframe)mKeyframes.get(mNumKeyframes - 1)).getValue()).intValue();
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
