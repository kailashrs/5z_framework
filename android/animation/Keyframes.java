package android.animation;

import java.util.List;

public abstract interface Keyframes
  extends Cloneable
{
  public abstract Keyframes clone();
  
  public abstract List<Keyframe> getKeyframes();
  
  public abstract Class getType();
  
  public abstract Object getValue(float paramFloat);
  
  public abstract void setEvaluator(TypeEvaluator paramTypeEvaluator);
  
  public static abstract interface FloatKeyframes
    extends Keyframes
  {
    public abstract float getFloatValue(float paramFloat);
  }
  
  public static abstract interface IntKeyframes
    extends Keyframes
  {
    public abstract int getIntValue(float paramFloat);
  }
}
