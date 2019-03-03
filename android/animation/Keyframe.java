package android.animation;

public abstract class Keyframe
  implements Cloneable
{
  float mFraction;
  boolean mHasValue;
  private TimeInterpolator mInterpolator = null;
  Class mValueType;
  boolean mValueWasSetOnStart;
  
  public Keyframe() {}
  
  public static Keyframe ofFloat(float paramFloat)
  {
    return new FloatKeyframe(paramFloat);
  }
  
  public static Keyframe ofFloat(float paramFloat1, float paramFloat2)
  {
    return new FloatKeyframe(paramFloat1, paramFloat2);
  }
  
  public static Keyframe ofInt(float paramFloat)
  {
    return new IntKeyframe(paramFloat);
  }
  
  public static Keyframe ofInt(float paramFloat, int paramInt)
  {
    return new IntKeyframe(paramFloat, paramInt);
  }
  
  public static Keyframe ofObject(float paramFloat)
  {
    return new ObjectKeyframe(paramFloat, null);
  }
  
  public static Keyframe ofObject(float paramFloat, Object paramObject)
  {
    return new ObjectKeyframe(paramFloat, paramObject);
  }
  
  public abstract Keyframe clone();
  
  public float getFraction()
  {
    return mFraction;
  }
  
  public TimeInterpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  public Class getType()
  {
    return mValueType;
  }
  
  public abstract Object getValue();
  
  public boolean hasValue()
  {
    return mHasValue;
  }
  
  public void setFraction(float paramFloat)
  {
    mFraction = paramFloat;
  }
  
  public void setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    mInterpolator = paramTimeInterpolator;
  }
  
  public abstract void setValue(Object paramObject);
  
  void setValueWasSetOnStart(boolean paramBoolean)
  {
    mValueWasSetOnStart = paramBoolean;
  }
  
  boolean valueWasSetOnStart()
  {
    return mValueWasSetOnStart;
  }
  
  static class FloatKeyframe
    extends Keyframe
  {
    float mValue;
    
    FloatKeyframe(float paramFloat)
    {
      mFraction = paramFloat;
      mValueType = Float.TYPE;
    }
    
    FloatKeyframe(float paramFloat1, float paramFloat2)
    {
      mFraction = paramFloat1;
      mValue = paramFloat2;
      mValueType = Float.TYPE;
      mHasValue = true;
    }
    
    public FloatKeyframe clone()
    {
      FloatKeyframe localFloatKeyframe;
      if (mHasValue) {
        localFloatKeyframe = new FloatKeyframe(getFraction(), mValue);
      } else {
        localFloatKeyframe = new FloatKeyframe(getFraction());
      }
      localFloatKeyframe.setInterpolator(getInterpolator());
      mValueWasSetOnStart = mValueWasSetOnStart;
      return localFloatKeyframe;
    }
    
    public float getFloatValue()
    {
      return mValue;
    }
    
    public Object getValue()
    {
      return Float.valueOf(mValue);
    }
    
    public void setValue(Object paramObject)
    {
      if ((paramObject != null) && (paramObject.getClass() == Float.class))
      {
        mValue = ((Float)paramObject).floatValue();
        mHasValue = true;
      }
    }
  }
  
  static class IntKeyframe
    extends Keyframe
  {
    int mValue;
    
    IntKeyframe(float paramFloat)
    {
      mFraction = paramFloat;
      mValueType = Integer.TYPE;
    }
    
    IntKeyframe(float paramFloat, int paramInt)
    {
      mFraction = paramFloat;
      mValue = paramInt;
      mValueType = Integer.TYPE;
      mHasValue = true;
    }
    
    public IntKeyframe clone()
    {
      IntKeyframe localIntKeyframe;
      if (mHasValue) {
        localIntKeyframe = new IntKeyframe(getFraction(), mValue);
      } else {
        localIntKeyframe = new IntKeyframe(getFraction());
      }
      localIntKeyframe.setInterpolator(getInterpolator());
      mValueWasSetOnStart = mValueWasSetOnStart;
      return localIntKeyframe;
    }
    
    public int getIntValue()
    {
      return mValue;
    }
    
    public Object getValue()
    {
      return Integer.valueOf(mValue);
    }
    
    public void setValue(Object paramObject)
    {
      if ((paramObject != null) && (paramObject.getClass() == Integer.class))
      {
        mValue = ((Integer)paramObject).intValue();
        mHasValue = true;
      }
    }
  }
  
  static class ObjectKeyframe
    extends Keyframe
  {
    Object mValue;
    
    ObjectKeyframe(float paramFloat, Object paramObject)
    {
      mFraction = paramFloat;
      mValue = paramObject;
      boolean bool;
      if (paramObject != null) {
        bool = true;
      } else {
        bool = false;
      }
      mHasValue = bool;
      if (mHasValue) {
        paramObject = paramObject.getClass();
      } else {
        paramObject = Object.class;
      }
      mValueType = paramObject;
    }
    
    public ObjectKeyframe clone()
    {
      float f = getFraction();
      if (hasValue()) {
        localObject = mValue;
      } else {
        localObject = null;
      }
      Object localObject = new ObjectKeyframe(f, localObject);
      mValueWasSetOnStart = mValueWasSetOnStart;
      ((ObjectKeyframe)localObject).setInterpolator(getInterpolator());
      return localObject;
    }
    
    public Object getValue()
    {
      return mValue;
    }
    
    public void setValue(Object paramObject)
    {
      mValue = paramObject;
      boolean bool;
      if (paramObject != null) {
        bool = true;
      } else {
        bool = false;
      }
      mHasValue = bool;
    }
  }
}
