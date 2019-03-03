package com.android.internal.graphics.palette;

public final class Target
{
  public static final Target DARK_MUTED;
  public static final Target DARK_VIBRANT;
  static final int INDEX_MAX = 2;
  static final int INDEX_MIN = 0;
  static final int INDEX_TARGET = 1;
  static final int INDEX_WEIGHT_LUMA = 1;
  static final int INDEX_WEIGHT_POP = 2;
  static final int INDEX_WEIGHT_SAT = 0;
  public static final Target LIGHT_MUTED;
  public static final Target LIGHT_VIBRANT = new Target();
  private static final float MAX_DARK_LUMA = 0.45F;
  private static final float MAX_MUTED_SATURATION = 0.4F;
  private static final float MAX_NORMAL_LUMA = 0.7F;
  private static final float MIN_LIGHT_LUMA = 0.55F;
  private static final float MIN_NORMAL_LUMA = 0.3F;
  private static final float MIN_VIBRANT_SATURATION = 0.35F;
  public static final Target MUTED;
  private static final float TARGET_DARK_LUMA = 0.26F;
  private static final float TARGET_LIGHT_LUMA = 0.74F;
  private static final float TARGET_MUTED_SATURATION = 0.3F;
  private static final float TARGET_NORMAL_LUMA = 0.5F;
  private static final float TARGET_VIBRANT_SATURATION = 1.0F;
  public static final Target VIBRANT;
  private static final float WEIGHT_LUMA = 0.52F;
  private static final float WEIGHT_POPULATION = 0.24F;
  private static final float WEIGHT_SATURATION = 0.24F;
  boolean mIsExclusive = true;
  final float[] mLightnessTargets = new float[3];
  final float[] mSaturationTargets = new float[3];
  final float[] mWeights = new float[3];
  
  static
  {
    setDefaultLightLightnessValues(LIGHT_VIBRANT);
    setDefaultVibrantSaturationValues(LIGHT_VIBRANT);
    VIBRANT = new Target();
    setDefaultNormalLightnessValues(VIBRANT);
    setDefaultVibrantSaturationValues(VIBRANT);
    DARK_VIBRANT = new Target();
    setDefaultDarkLightnessValues(DARK_VIBRANT);
    setDefaultVibrantSaturationValues(DARK_VIBRANT);
    LIGHT_MUTED = new Target();
    setDefaultLightLightnessValues(LIGHT_MUTED);
    setDefaultMutedSaturationValues(LIGHT_MUTED);
    MUTED = new Target();
    setDefaultNormalLightnessValues(MUTED);
    setDefaultMutedSaturationValues(MUTED);
    DARK_MUTED = new Target();
    setDefaultDarkLightnessValues(DARK_MUTED);
    setDefaultMutedSaturationValues(DARK_MUTED);
  }
  
  Target()
  {
    setTargetDefaultValues(mSaturationTargets);
    setTargetDefaultValues(mLightnessTargets);
    setDefaultWeights();
  }
  
  Target(Target paramTarget)
  {
    System.arraycopy(mSaturationTargets, 0, mSaturationTargets, 0, mSaturationTargets.length);
    System.arraycopy(mLightnessTargets, 0, mLightnessTargets, 0, mLightnessTargets.length);
    System.arraycopy(mWeights, 0, mWeights, 0, mWeights.length);
  }
  
  private static void setDefaultDarkLightnessValues(Target paramTarget)
  {
    mLightnessTargets[1] = 0.26F;
    mLightnessTargets[2] = 0.45F;
  }
  
  private static void setDefaultLightLightnessValues(Target paramTarget)
  {
    mLightnessTargets[0] = 0.55F;
    mLightnessTargets[1] = 0.74F;
  }
  
  private static void setDefaultMutedSaturationValues(Target paramTarget)
  {
    mSaturationTargets[1] = 0.3F;
    mSaturationTargets[2] = 0.4F;
  }
  
  private static void setDefaultNormalLightnessValues(Target paramTarget)
  {
    mLightnessTargets[0] = 0.3F;
    mLightnessTargets[1] = 0.5F;
    mLightnessTargets[2] = 0.7F;
  }
  
  private static void setDefaultVibrantSaturationValues(Target paramTarget)
  {
    mSaturationTargets[0] = 0.35F;
    mSaturationTargets[1] = 1.0F;
  }
  
  private void setDefaultWeights()
  {
    mWeights[0] = 0.24F;
    mWeights[1] = 0.52F;
    mWeights[2] = 0.24F;
  }
  
  private static void setTargetDefaultValues(float[] paramArrayOfFloat)
  {
    paramArrayOfFloat[0] = 0.0F;
    paramArrayOfFloat[1] = 0.5F;
    paramArrayOfFloat[2] = 1.0F;
  }
  
  public float getLightnessWeight()
  {
    return mWeights[1];
  }
  
  public float getMaximumLightness()
  {
    return mLightnessTargets[2];
  }
  
  public float getMaximumSaturation()
  {
    return mSaturationTargets[2];
  }
  
  public float getMinimumLightness()
  {
    return mLightnessTargets[0];
  }
  
  public float getMinimumSaturation()
  {
    return mSaturationTargets[0];
  }
  
  public float getPopulationWeight()
  {
    return mWeights[2];
  }
  
  public float getSaturationWeight()
  {
    return mWeights[0];
  }
  
  public float getTargetLightness()
  {
    return mLightnessTargets[1];
  }
  
  public float getTargetSaturation()
  {
    return mSaturationTargets[1];
  }
  
  public boolean isExclusive()
  {
    return mIsExclusive;
  }
  
  void normalizeWeights()
  {
    float f1 = 0.0F;
    int i = 0;
    int j = mWeights.length;
    while (i < j)
    {
      float f2 = mWeights[i];
      float f3 = f1;
      if (f2 > 0.0F) {
        f3 = f1 + f2;
      }
      i++;
      f1 = f3;
    }
    if (f1 != 0.0F)
    {
      i = 0;
      j = mWeights.length;
      while (i < j)
      {
        if (mWeights[i] > 0.0F)
        {
          float[] arrayOfFloat = mWeights;
          arrayOfFloat[i] /= f1;
        }
        i++;
      }
    }
  }
  
  public static final class Builder
  {
    private final Target mTarget;
    
    public Builder()
    {
      mTarget = new Target();
    }
    
    public Builder(Target paramTarget)
    {
      mTarget = new Target(paramTarget);
    }
    
    public Target build()
    {
      return mTarget;
    }
    
    public Builder setExclusive(boolean paramBoolean)
    {
      mTarget.mIsExclusive = paramBoolean;
      return this;
    }
    
    public Builder setLightnessWeight(float paramFloat)
    {
      mTarget.mWeights[1] = paramFloat;
      return this;
    }
    
    public Builder setMaximumLightness(float paramFloat)
    {
      mTarget.mLightnessTargets[2] = paramFloat;
      return this;
    }
    
    public Builder setMaximumSaturation(float paramFloat)
    {
      mTarget.mSaturationTargets[2] = paramFloat;
      return this;
    }
    
    public Builder setMinimumLightness(float paramFloat)
    {
      mTarget.mLightnessTargets[0] = paramFloat;
      return this;
    }
    
    public Builder setMinimumSaturation(float paramFloat)
    {
      mTarget.mSaturationTargets[0] = paramFloat;
      return this;
    }
    
    public Builder setPopulationWeight(float paramFloat)
    {
      mTarget.mWeights[2] = paramFloat;
      return this;
    }
    
    public Builder setSaturationWeight(float paramFloat)
    {
      mTarget.mWeights[0] = paramFloat;
      return this;
    }
    
    public Builder setTargetLightness(float paramFloat)
    {
      mTarget.mLightnessTargets[1] = paramFloat;
      return this;
    }
    
    public Builder setTargetSaturation(float paramFloat)
    {
      mTarget.mSaturationTargets[1] = paramFloat;
      return this;
    }
  }
}
