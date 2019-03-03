package android.animation;

import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public final class ObjectAnimator
  extends ValueAnimator
{
  private static final boolean DBG = false;
  private static final String LOG_TAG = "ObjectAnimator";
  private boolean mAutoCancel = false;
  private Property mProperty;
  private String mPropertyName;
  private WeakReference<Object> mTarget;
  
  public ObjectAnimator() {}
  
  private <T> ObjectAnimator(T paramT, Property<T, ?> paramProperty)
  {
    setTarget(paramT);
    setProperty(paramProperty);
  }
  
  private ObjectAnimator(Object paramObject, String paramString)
  {
    setTarget(paramObject);
    setPropertyName(paramString);
  }
  
  private boolean hasSameTargetAndProperties(Animator paramAnimator)
  {
    if ((paramAnimator instanceof ObjectAnimator))
    {
      PropertyValuesHolder[] arrayOfPropertyValuesHolder = ((ObjectAnimator)paramAnimator).getValues();
      if ((((ObjectAnimator)paramAnimator).getTarget() == getTarget()) && (mValues.length == arrayOfPropertyValuesHolder.length))
      {
        int i = 0;
        while (i < mValues.length)
        {
          paramAnimator = mValues[i];
          PropertyValuesHolder localPropertyValuesHolder = arrayOfPropertyValuesHolder[i];
          if ((paramAnimator.getPropertyName() != null) && (paramAnimator.getPropertyName().equals(localPropertyValuesHolder.getPropertyName()))) {
            i++;
          } else {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }
  
  public static <T> ObjectAnimator ofArgb(T paramT, Property<T, Integer> paramProperty, int... paramVarArgs)
  {
    paramT = ofInt(paramT, paramProperty, paramVarArgs);
    paramT.setEvaluator(ArgbEvaluator.getInstance());
    return paramT;
  }
  
  public static ObjectAnimator ofArgb(Object paramObject, String paramString, int... paramVarArgs)
  {
    paramObject = ofInt(paramObject, paramString, paramVarArgs);
    paramObject.setEvaluator(ArgbEvaluator.getInstance());
    return paramObject;
  }
  
  public static <T> ObjectAnimator ofFloat(T paramT, Property<T, Float> paramProperty1, Property<T, Float> paramProperty2, Path paramPath)
  {
    paramPath = KeyframeSet.ofPath(paramPath);
    paramProperty1 = PropertyValuesHolder.ofKeyframes(paramProperty1, paramPath.createXFloatKeyframes());
    return ofPropertyValuesHolder(paramT, new PropertyValuesHolder[] { paramProperty1, PropertyValuesHolder.ofKeyframes(paramProperty2, paramPath.createYFloatKeyframes()) });
  }
  
  public static <T> ObjectAnimator ofFloat(T paramT, Property<T, Float> paramProperty, float... paramVarArgs)
  {
    paramT = new ObjectAnimator(paramT, paramProperty);
    paramT.setFloatValues(paramVarArgs);
    return paramT;
  }
  
  public static ObjectAnimator ofFloat(Object paramObject, String paramString1, String paramString2, Path paramPath)
  {
    paramPath = KeyframeSet.ofPath(paramPath);
    paramString1 = PropertyValuesHolder.ofKeyframes(paramString1, paramPath.createXFloatKeyframes());
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { paramString1, PropertyValuesHolder.ofKeyframes(paramString2, paramPath.createYFloatKeyframes()) });
  }
  
  public static ObjectAnimator ofFloat(Object paramObject, String paramString, float... paramVarArgs)
  {
    paramObject = new ObjectAnimator(paramObject, paramString);
    paramObject.setFloatValues(paramVarArgs);
    return paramObject;
  }
  
  public static <T> ObjectAnimator ofInt(T paramT, Property<T, Integer> paramProperty1, Property<T, Integer> paramProperty2, Path paramPath)
  {
    paramPath = KeyframeSet.ofPath(paramPath);
    paramProperty1 = PropertyValuesHolder.ofKeyframes(paramProperty1, paramPath.createXIntKeyframes());
    return ofPropertyValuesHolder(paramT, new PropertyValuesHolder[] { paramProperty1, PropertyValuesHolder.ofKeyframes(paramProperty2, paramPath.createYIntKeyframes()) });
  }
  
  public static <T> ObjectAnimator ofInt(T paramT, Property<T, Integer> paramProperty, int... paramVarArgs)
  {
    paramT = new ObjectAnimator(paramT, paramProperty);
    paramT.setIntValues(paramVarArgs);
    return paramT;
  }
  
  public static ObjectAnimator ofInt(Object paramObject, String paramString1, String paramString2, Path paramPath)
  {
    paramPath = KeyframeSet.ofPath(paramPath);
    paramString1 = PropertyValuesHolder.ofKeyframes(paramString1, paramPath.createXIntKeyframes());
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { paramString1, PropertyValuesHolder.ofKeyframes(paramString2, paramPath.createYIntKeyframes()) });
  }
  
  public static ObjectAnimator ofInt(Object paramObject, String paramString, int... paramVarArgs)
  {
    paramObject = new ObjectAnimator(paramObject, paramString);
    paramObject.setIntValues(paramVarArgs);
    return paramObject;
  }
  
  @SafeVarargs
  public static <T> ObjectAnimator ofMultiFloat(Object paramObject, String paramString, TypeConverter<T, float[]> paramTypeConverter, TypeEvaluator<T> paramTypeEvaluator, T... paramVarArgs)
  {
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofMultiFloat(paramString, paramTypeConverter, paramTypeEvaluator, paramVarArgs) });
  }
  
  public static ObjectAnimator ofMultiFloat(Object paramObject, String paramString, Path paramPath)
  {
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofMultiFloat(paramString, paramPath) });
  }
  
  public static ObjectAnimator ofMultiFloat(Object paramObject, String paramString, float[][] paramArrayOfFloat)
  {
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofMultiFloat(paramString, paramArrayOfFloat) });
  }
  
  @SafeVarargs
  public static <T> ObjectAnimator ofMultiInt(Object paramObject, String paramString, TypeConverter<T, int[]> paramTypeConverter, TypeEvaluator<T> paramTypeEvaluator, T... paramVarArgs)
  {
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofMultiInt(paramString, paramTypeConverter, paramTypeEvaluator, paramVarArgs) });
  }
  
  public static ObjectAnimator ofMultiInt(Object paramObject, String paramString, Path paramPath)
  {
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofMultiInt(paramString, paramPath) });
  }
  
  public static ObjectAnimator ofMultiInt(Object paramObject, String paramString, int[][] paramArrayOfInt)
  {
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofMultiInt(paramString, paramArrayOfInt) });
  }
  
  @SafeVarargs
  public static <T, V, P> ObjectAnimator ofObject(T paramT, Property<T, P> paramProperty, TypeConverter<V, P> paramTypeConverter, TypeEvaluator<V> paramTypeEvaluator, V... paramVarArgs)
  {
    return ofPropertyValuesHolder(paramT, new PropertyValuesHolder[] { PropertyValuesHolder.ofObject(paramProperty, paramTypeConverter, paramTypeEvaluator, paramVarArgs) });
  }
  
  public static <T, V> ObjectAnimator ofObject(T paramT, Property<T, V> paramProperty, TypeConverter<PointF, V> paramTypeConverter, Path paramPath)
  {
    return ofPropertyValuesHolder(paramT, new PropertyValuesHolder[] { PropertyValuesHolder.ofObject(paramProperty, paramTypeConverter, paramPath) });
  }
  
  @SafeVarargs
  public static <T, V> ObjectAnimator ofObject(T paramT, Property<T, V> paramProperty, TypeEvaluator<V> paramTypeEvaluator, V... paramVarArgs)
  {
    paramT = new ObjectAnimator(paramT, paramProperty);
    paramT.setObjectValues(paramVarArgs);
    paramT.setEvaluator(paramTypeEvaluator);
    return paramT;
  }
  
  public static ObjectAnimator ofObject(Object paramObject, String paramString, TypeConverter<PointF, ?> paramTypeConverter, Path paramPath)
  {
    return ofPropertyValuesHolder(paramObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofObject(paramString, paramTypeConverter, paramPath) });
  }
  
  public static ObjectAnimator ofObject(Object paramObject, String paramString, TypeEvaluator paramTypeEvaluator, Object... paramVarArgs)
  {
    paramObject = new ObjectAnimator(paramObject, paramString);
    paramObject.setObjectValues(paramVarArgs);
    paramObject.setEvaluator(paramTypeEvaluator);
    return paramObject;
  }
  
  public static ObjectAnimator ofPropertyValuesHolder(Object paramObject, PropertyValuesHolder... paramVarArgs)
  {
    ObjectAnimator localObjectAnimator = new ObjectAnimator();
    localObjectAnimator.setTarget(paramObject);
    localObjectAnimator.setValues(paramVarArgs);
    return localObjectAnimator;
  }
  
  void animateValue(float paramFloat)
  {
    Object localObject = getTarget();
    if ((mTarget != null) && (localObject == null))
    {
      cancel();
      return;
    }
    super.animateValue(paramFloat);
    int i = mValues.length;
    for (int j = 0; j < i; j++) {
      mValues[j].setAnimatedValue(localObject);
    }
  }
  
  public ObjectAnimator clone()
  {
    return (ObjectAnimator)super.clone();
  }
  
  String getNameForTrace()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("animator:");
    localStringBuilder.append(getPropertyName());
    return localStringBuilder.toString();
  }
  
  public String getPropertyName()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3;
    if (mPropertyName != null)
    {
      localObject3 = mPropertyName;
    }
    else if (mProperty != null)
    {
      localObject3 = mProperty.getName();
    }
    else
    {
      localObject3 = localObject1;
      if (mValues != null)
      {
        localObject3 = localObject1;
        if (mValues.length > 0) {
          for (int i = 0;; i++)
          {
            localObject3 = localObject2;
            if (i >= mValues.length) {
              break;
            }
            if (i == 0)
            {
              localObject3 = "";
            }
            else
            {
              localObject3 = new StringBuilder();
              ((StringBuilder)localObject3).append((String)localObject2);
              ((StringBuilder)localObject3).append(",");
              localObject3 = ((StringBuilder)localObject3).toString();
            }
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append((String)localObject3);
            ((StringBuilder)localObject2).append(mValues[i].getPropertyName());
            localObject2 = ((StringBuilder)localObject2).toString();
          }
        }
      }
    }
    return localObject3;
  }
  
  public Object getTarget()
  {
    Object localObject;
    if (mTarget == null) {
      localObject = null;
    } else {
      localObject = mTarget.get();
    }
    return localObject;
  }
  
  void initAnimation()
  {
    if (!mInitialized)
    {
      Object localObject = getTarget();
      if (localObject != null)
      {
        int i = mValues.length;
        for (int j = 0; j < i; j++) {
          mValues[j].setupSetterAndGetter(localObject);
        }
      }
      super.initAnimation();
    }
  }
  
  boolean isInitialized()
  {
    return mInitialized;
  }
  
  public void setAutoCancel(boolean paramBoolean)
  {
    mAutoCancel = paramBoolean;
  }
  
  public ObjectAnimator setDuration(long paramLong)
  {
    super.setDuration(paramLong);
    return this;
  }
  
  public void setFloatValues(float... paramVarArgs)
  {
    if ((mValues != null) && (mValues.length != 0)) {
      super.setFloatValues(paramVarArgs);
    } else if (mProperty != null) {
      setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat(mProperty, paramVarArgs) });
    } else {
      setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat(mPropertyName, paramVarArgs) });
    }
  }
  
  public void setIntValues(int... paramVarArgs)
  {
    if ((mValues != null) && (mValues.length != 0)) {
      super.setIntValues(paramVarArgs);
    } else if (mProperty != null) {
      setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofInt(mProperty, paramVarArgs) });
    } else {
      setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofInt(mPropertyName, paramVarArgs) });
    }
  }
  
  public void setObjectValues(Object... paramVarArgs)
  {
    if ((mValues != null) && (mValues.length != 0)) {
      super.setObjectValues(paramVarArgs);
    } else if (mProperty != null) {
      setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofObject(mProperty, (TypeEvaluator)null, paramVarArgs) });
    } else {
      setValues(new PropertyValuesHolder[] { PropertyValuesHolder.ofObject(mPropertyName, (TypeEvaluator)null, paramVarArgs) });
    }
  }
  
  public void setProperty(Property paramProperty)
  {
    if (mValues != null)
    {
      PropertyValuesHolder localPropertyValuesHolder = mValues[0];
      String str = localPropertyValuesHolder.getPropertyName();
      localPropertyValuesHolder.setProperty(paramProperty);
      mValuesMap.remove(str);
      mValuesMap.put(mPropertyName, localPropertyValuesHolder);
    }
    if (mProperty != null) {
      mPropertyName = paramProperty.getName();
    }
    mProperty = paramProperty;
    mInitialized = false;
  }
  
  public void setPropertyName(String paramString)
  {
    if (mValues != null)
    {
      PropertyValuesHolder localPropertyValuesHolder = mValues[0];
      String str = localPropertyValuesHolder.getPropertyName();
      localPropertyValuesHolder.setPropertyName(paramString);
      mValuesMap.remove(str);
      mValuesMap.put(paramString, localPropertyValuesHolder);
    }
    mPropertyName = paramString;
    mInitialized = false;
  }
  
  public void setTarget(Object paramObject)
  {
    if (getTarget() != paramObject)
    {
      if (isStarted()) {
        cancel();
      }
      if (paramObject == null) {
        paramObject = null;
      } else {
        paramObject = new WeakReference(paramObject);
      }
      mTarget = paramObject;
      mInitialized = false;
    }
  }
  
  public void setupEndValues()
  {
    initAnimation();
    Object localObject = getTarget();
    if (localObject != null)
    {
      int i = mValues.length;
      for (int j = 0; j < i; j++) {
        mValues[j].setupEndValue(localObject);
      }
    }
  }
  
  public void setupStartValues()
  {
    initAnimation();
    Object localObject = getTarget();
    if (localObject != null)
    {
      int i = mValues.length;
      for (int j = 0; j < i; j++) {
        mValues[j].setupStartValue(localObject);
      }
    }
  }
  
  boolean shouldAutoCancel(AnimationHandler.AnimationFrameCallback paramAnimationFrameCallback)
  {
    if (paramAnimationFrameCallback == null) {
      return false;
    }
    if ((paramAnimationFrameCallback instanceof ObjectAnimator))
    {
      paramAnimationFrameCallback = (ObjectAnimator)paramAnimationFrameCallback;
      if ((mAutoCancel) && (hasSameTargetAndProperties(paramAnimationFrameCallback))) {
        return true;
      }
    }
    return false;
  }
  
  public void start()
  {
    AnimationHandler.getInstance().autoCancelBasedOn(this);
    super.start();
  }
  
  public String toString()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("ObjectAnimator@");
    ((StringBuilder)localObject1).append(Integer.toHexString(hashCode()));
    ((StringBuilder)localObject1).append(", target ");
    ((StringBuilder)localObject1).append(getTarget());
    localObject1 = ((StringBuilder)localObject1).toString();
    Object localObject2 = localObject1;
    if (mValues != null) {
      for (int i = 0;; i++)
      {
        localObject2 = localObject1;
        if (i >= mValues.length) {
          break;
        }
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append("\n    ");
        ((StringBuilder)localObject2).append(mValues[i].toString());
        localObject1 = ((StringBuilder)localObject2).toString();
      }
    }
    return localObject2;
  }
}
