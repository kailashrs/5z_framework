package android.animation;

import android.graphics.Path;
import android.graphics.PointF;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.Log;
import android.util.PathParser.PathData;
import android.util.Property;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class PropertyValuesHolder
  implements Cloneable
{
  private static Class[] DOUBLE_VARIANTS;
  private static Class[] FLOAT_VARIANTS;
  private static Class[] INTEGER_VARIANTS;
  private static final TypeEvaluator sFloatEvaluator;
  private static final HashMap<Class, HashMap<String, Method>> sGetterPropertyMap = new HashMap();
  private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
  private static final HashMap<Class, HashMap<String, Method>> sSetterPropertyMap;
  private Object mAnimatedValue;
  private TypeConverter mConverter;
  private TypeEvaluator mEvaluator;
  private Method mGetter = null;
  Keyframes mKeyframes = null;
  protected Property mProperty;
  String mPropertyName;
  Method mSetter = null;
  final Object[] mTmpValueArray = new Object[1];
  Class mValueType;
  
  static
  {
    sFloatEvaluator = new FloatEvaluator();
    FLOAT_VARIANTS = new Class[] { Float.TYPE, Float.class, Double.TYPE, Integer.TYPE, Double.class, Integer.class };
    INTEGER_VARIANTS = new Class[] { Integer.TYPE, Integer.class, Float.TYPE, Double.TYPE, Float.class, Double.class };
    DOUBLE_VARIANTS = new Class[] { Double.TYPE, Double.class, Float.TYPE, Integer.TYPE, Float.class, Integer.class };
    sSetterPropertyMap = new HashMap();
  }
  
  private PropertyValuesHolder(Property paramProperty)
  {
    mProperty = paramProperty;
    if (paramProperty != null) {
      mPropertyName = paramProperty.getName();
    }
  }
  
  private PropertyValuesHolder(String paramString)
  {
    mPropertyName = paramString;
  }
  
  private Object convertBack(Object paramObject)
  {
    Object localObject = paramObject;
    if (mConverter != null) {
      if ((mConverter instanceof BidirectionalTypeConverter))
      {
        localObject = ((BidirectionalTypeConverter)mConverter).convertBack(paramObject);
      }
      else
      {
        paramObject = new StringBuilder();
        paramObject.append("Converter ");
        paramObject.append(mConverter.getClass().getName());
        paramObject.append(" must be a BidirectionalTypeConverter");
        throw new IllegalArgumentException(paramObject.toString());
      }
    }
    return localObject;
  }
  
  static String getMethodName(String paramString1, String paramString2)
  {
    if ((paramString2 != null) && (paramString2.length() != 0))
    {
      char c = Character.toUpperCase(paramString2.charAt(0));
      String str = paramString2.substring(1);
      paramString2 = new StringBuilder();
      paramString2.append(paramString1);
      paramString2.append(c);
      paramString2.append(str);
      return paramString2.toString();
    }
    return paramString1;
  }
  
  private Method getPropertyFunction(Class paramClass1, String paramString, Class paramClass2)
  {
    Object localObject1 = null;
    String str = getMethodName(paramString, mPropertyName);
    Object localObject2;
    if (paramClass2 == null)
    {
      try
      {
        Method localMethod1 = paramClass1.getMethod(str, null);
        localObject1 = localMethod1;
      }
      catch (NoSuchMethodException localNoSuchMethodException1)
      {
        for (;;) {}
      }
    }
    else
    {
      Class[] arrayOfClass = new Class[1];
      if (paramClass2.equals(Float.class)) {
        localObject1 = FLOAT_VARIANTS;
      }
      for (;;)
      {
        localObject2 = localObject1;
        break label110;
        if (paramClass2.equals(Integer.class))
        {
          localObject1 = INTEGER_VARIANTS;
        }
        else
        {
          if (!paramClass2.equals(Double.class)) {
            break;
          }
          localObject1 = DOUBLE_VARIANTS;
        }
      }
      localObject2 = new Class[1];
      localObject2[0] = paramClass2;
      label110:
      int i = localObject2.length;
      localObject1 = null;
      int j = 0;
      while (j < i)
      {
        Object localObject3 = localObject2[j];
        arrayOfClass[0] = localObject3;
        try
        {
          Method localMethod2 = paramClass1.getMethod(str, arrayOfClass);
          localObject1 = localMethod2;
          if (mConverter == null)
          {
            localObject1 = localMethod2;
            mValueType = localObject3;
          }
          return localMethod2;
        }
        catch (NoSuchMethodException localNoSuchMethodException2)
        {
          j++;
        }
      }
    }
    if (localObject1 == null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Method ");
      ((StringBuilder)localObject2).append(getMethodName(paramString, mPropertyName));
      ((StringBuilder)localObject2).append("() with type ");
      ((StringBuilder)localObject2).append(paramClass2);
      ((StringBuilder)localObject2).append(" not found on target class ");
      ((StringBuilder)localObject2).append(paramClass1);
      Log.w("PropertyValuesHolder", ((StringBuilder)localObject2).toString());
    }
    return localObject1;
  }
  
  private static native void nCallFloatMethod(Object paramObject, long paramLong, float paramFloat);
  
  private static native void nCallFourFloatMethod(Object paramObject, long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  private static native void nCallFourIntMethod(Object paramObject, long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  private static native void nCallIntMethod(Object paramObject, long paramLong, int paramInt);
  
  private static native void nCallMultipleFloatMethod(Object paramObject, long paramLong, float[] paramArrayOfFloat);
  
  private static native void nCallMultipleIntMethod(Object paramObject, long paramLong, int[] paramArrayOfInt);
  
  private static native void nCallTwoFloatMethod(Object paramObject, long paramLong, float paramFloat1, float paramFloat2);
  
  private static native void nCallTwoIntMethod(Object paramObject, long paramLong, int paramInt1, int paramInt2);
  
  private static native long nGetFloatMethod(Class paramClass, String paramString);
  
  private static native long nGetIntMethod(Class paramClass, String paramString);
  
  private static native long nGetMultipleFloatMethod(Class paramClass, String paramString, int paramInt);
  
  private static native long nGetMultipleIntMethod(Class paramClass, String paramString, int paramInt);
  
  public static PropertyValuesHolder ofFloat(Property<?, Float> paramProperty, float... paramVarArgs)
  {
    return new FloatPropertyValuesHolder(paramProperty, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofFloat(String paramString, float... paramVarArgs)
  {
    return new FloatPropertyValuesHolder(paramString, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofInt(Property<?, Integer> paramProperty, int... paramVarArgs)
  {
    return new IntPropertyValuesHolder(paramProperty, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofInt(String paramString, int... paramVarArgs)
  {
    return new IntPropertyValuesHolder(paramString, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofKeyframe(Property paramProperty, Keyframe... paramVarArgs)
  {
    return ofKeyframes(paramProperty, KeyframeSet.ofKeyframe(paramVarArgs));
  }
  
  public static PropertyValuesHolder ofKeyframe(String paramString, Keyframe... paramVarArgs)
  {
    return ofKeyframes(paramString, KeyframeSet.ofKeyframe(paramVarArgs));
  }
  
  static PropertyValuesHolder ofKeyframes(Property paramProperty, Keyframes paramKeyframes)
  {
    if ((paramKeyframes instanceof Keyframes.IntKeyframes)) {
      return new IntPropertyValuesHolder(paramProperty, (Keyframes.IntKeyframes)paramKeyframes);
    }
    if ((paramKeyframes instanceof Keyframes.FloatKeyframes)) {
      return new FloatPropertyValuesHolder(paramProperty, (Keyframes.FloatKeyframes)paramKeyframes);
    }
    paramProperty = new PropertyValuesHolder(paramProperty);
    mKeyframes = paramKeyframes;
    mValueType = paramKeyframes.getType();
    return paramProperty;
  }
  
  static PropertyValuesHolder ofKeyframes(String paramString, Keyframes paramKeyframes)
  {
    if ((paramKeyframes instanceof Keyframes.IntKeyframes)) {
      return new IntPropertyValuesHolder(paramString, (Keyframes.IntKeyframes)paramKeyframes);
    }
    if ((paramKeyframes instanceof Keyframes.FloatKeyframes)) {
      return new FloatPropertyValuesHolder(paramString, (Keyframes.FloatKeyframes)paramKeyframes);
    }
    paramString = new PropertyValuesHolder(paramString);
    mKeyframes = paramKeyframes;
    mValueType = paramKeyframes.getType();
    return paramString;
  }
  
  public static <T> PropertyValuesHolder ofMultiFloat(String paramString, TypeConverter<T, float[]> paramTypeConverter, TypeEvaluator<T> paramTypeEvaluator, Keyframe... paramVarArgs)
  {
    return new MultiFloatValuesHolder(paramString, paramTypeConverter, paramTypeEvaluator, KeyframeSet.ofKeyframe(paramVarArgs));
  }
  
  @SafeVarargs
  public static <V> PropertyValuesHolder ofMultiFloat(String paramString, TypeConverter<V, float[]> paramTypeConverter, TypeEvaluator<V> paramTypeEvaluator, V... paramVarArgs)
  {
    return new MultiFloatValuesHolder(paramString, paramTypeConverter, paramTypeEvaluator, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofMultiFloat(String paramString, Path paramPath)
  {
    paramPath = KeyframeSet.ofPath(paramPath);
    return new MultiFloatValuesHolder(paramString, new PointFToFloatArray(), null, paramPath);
  }
  
  public static PropertyValuesHolder ofMultiFloat(String paramString, float[][] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length >= 2)
    {
      int i = 0;
      int j = 0;
      while (j < paramArrayOfFloat.length) {
        if (paramArrayOfFloat[j] != null)
        {
          int k = paramArrayOfFloat[j].length;
          if (j == 0) {
            i = k;
          } else {
            if (k != i) {
              break label50;
            }
          }
          j++;
          continue;
          label50:
          throw new IllegalArgumentException("Values must all have the same length");
        }
        else
        {
          throw new IllegalArgumentException("values must not be null");
        }
      }
      return new MultiFloatValuesHolder(paramString, null, new FloatArrayEvaluator(new float[i]), (Object[])paramArrayOfFloat);
    }
    throw new IllegalArgumentException("At least 2 values must be supplied");
  }
  
  public static <T> PropertyValuesHolder ofMultiInt(String paramString, TypeConverter<T, int[]> paramTypeConverter, TypeEvaluator<T> paramTypeEvaluator, Keyframe... paramVarArgs)
  {
    return new MultiIntValuesHolder(paramString, paramTypeConverter, paramTypeEvaluator, KeyframeSet.ofKeyframe(paramVarArgs));
  }
  
  @SafeVarargs
  public static <V> PropertyValuesHolder ofMultiInt(String paramString, TypeConverter<V, int[]> paramTypeConverter, TypeEvaluator<V> paramTypeEvaluator, V... paramVarArgs)
  {
    return new MultiIntValuesHolder(paramString, paramTypeConverter, paramTypeEvaluator, paramVarArgs);
  }
  
  public static PropertyValuesHolder ofMultiInt(String paramString, Path paramPath)
  {
    paramPath = KeyframeSet.ofPath(paramPath);
    return new MultiIntValuesHolder(paramString, new PointFToIntArray(), null, paramPath);
  }
  
  public static PropertyValuesHolder ofMultiInt(String paramString, int[][] paramArrayOfInt)
  {
    if (paramArrayOfInt.length >= 2)
    {
      int i = 0;
      int j = 0;
      while (j < paramArrayOfInt.length) {
        if (paramArrayOfInt[j] != null)
        {
          int k = paramArrayOfInt[j].length;
          if (j == 0) {
            i = k;
          } else {
            if (k != i) {
              break label50;
            }
          }
          j++;
          continue;
          label50:
          throw new IllegalArgumentException("Values must all have the same length");
        }
        else
        {
          throw new IllegalArgumentException("values must not be null");
        }
      }
      return new MultiIntValuesHolder(paramString, null, new IntArrayEvaluator(new int[i]), (Object[])paramArrayOfInt);
    }
    throw new IllegalArgumentException("At least 2 values must be supplied");
  }
  
  @SafeVarargs
  public static <T, V> PropertyValuesHolder ofObject(Property<?, V> paramProperty, TypeConverter<T, V> paramTypeConverter, TypeEvaluator<T> paramTypeEvaluator, T... paramVarArgs)
  {
    paramProperty = new PropertyValuesHolder(paramProperty);
    paramProperty.setConverter(paramTypeConverter);
    paramProperty.setObjectValues(paramVarArgs);
    paramProperty.setEvaluator(paramTypeEvaluator);
    return paramProperty;
  }
  
  public static <V> PropertyValuesHolder ofObject(Property<?, V> paramProperty, TypeConverter<PointF, V> paramTypeConverter, Path paramPath)
  {
    paramProperty = new PropertyValuesHolder(paramProperty);
    mKeyframes = KeyframeSet.ofPath(paramPath);
    mValueType = PointF.class;
    paramProperty.setConverter(paramTypeConverter);
    return paramProperty;
  }
  
  @SafeVarargs
  public static <V> PropertyValuesHolder ofObject(Property paramProperty, TypeEvaluator<V> paramTypeEvaluator, V... paramVarArgs)
  {
    paramProperty = new PropertyValuesHolder(paramProperty);
    paramProperty.setObjectValues(paramVarArgs);
    paramProperty.setEvaluator(paramTypeEvaluator);
    return paramProperty;
  }
  
  public static PropertyValuesHolder ofObject(String paramString, TypeConverter<PointF, ?> paramTypeConverter, Path paramPath)
  {
    paramString = new PropertyValuesHolder(paramString);
    mKeyframes = KeyframeSet.ofPath(paramPath);
    mValueType = PointF.class;
    paramString.setConverter(paramTypeConverter);
    return paramString;
  }
  
  public static PropertyValuesHolder ofObject(String paramString, TypeEvaluator paramTypeEvaluator, Object... paramVarArgs)
  {
    paramString = new PropertyValuesHolder(paramString);
    paramString.setObjectValues(paramVarArgs);
    paramString.setEvaluator(paramTypeEvaluator);
    return paramString;
  }
  
  private void setupGetter(Class paramClass)
  {
    mGetter = setupSetterOrGetter(paramClass, sGetterPropertyMap, "get", null);
  }
  
  private Method setupSetterOrGetter(Class paramClass1, HashMap<Class, HashMap<String, Method>> paramHashMap, String paramString, Class paramClass2)
  {
    Object localObject1 = null;
    try
    {
      HashMap localHashMap = (HashMap)paramHashMap.get(paramClass1);
      int i = 0;
      Object localObject2 = localObject1;
      if (localHashMap != null)
      {
        boolean bool = localHashMap.containsKey(mPropertyName);
        localObject2 = localObject1;
        i = bool;
        if (bool)
        {
          localObject2 = (Method)localHashMap.get(mPropertyName);
          i = bool;
        }
      }
      if (i == 0)
      {
        localObject2 = getPropertyFunction(paramClass1, paramString, paramClass2);
        paramString = localHashMap;
        if (localHashMap == null)
        {
          paramString = new java/util/HashMap;
          paramString.<init>();
          paramHashMap.put(paramClass1, paramString);
        }
        paramString.put(mPropertyName, localObject2);
      }
      return localObject2;
    }
    finally {}
  }
  
  private void setupValue(Object paramObject, Keyframe paramKeyframe)
  {
    if (mProperty != null) {
      paramKeyframe.setValue(convertBack(mProperty.get(paramObject)));
    } else {
      try
      {
        if (mGetter == null)
        {
          setupGetter(paramObject.getClass());
          if (mGetter == null) {
            return;
          }
        }
        paramKeyframe.setValue(convertBack(mGetter.invoke(paramObject, new Object[0])));
      }
      catch (IllegalAccessException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
      }
      catch (InvocationTargetException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
      }
    }
  }
  
  void calculateValue(float paramFloat)
  {
    Object localObject = mKeyframes.getValue(paramFloat);
    if (mConverter != null) {
      localObject = mConverter.convert(localObject);
    }
    mAnimatedValue = localObject;
  }
  
  public PropertyValuesHolder clone()
  {
    try
    {
      PropertyValuesHolder localPropertyValuesHolder = (PropertyValuesHolder)super.clone();
      mPropertyName = mPropertyName;
      mProperty = mProperty;
      mKeyframes = mKeyframes.clone();
      mEvaluator = mEvaluator;
      return localPropertyValuesHolder;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return null;
  }
  
  Object getAnimatedValue()
  {
    return mAnimatedValue;
  }
  
  public String getPropertyName()
  {
    return mPropertyName;
  }
  
  public void getPropertyValues(PropertyValues paramPropertyValues)
  {
    init();
    propertyName = mPropertyName;
    type = mValueType;
    startValue = mKeyframes.getValue(0.0F);
    if ((startValue instanceof PathParser.PathData)) {
      startValue = new PathParser.PathData((PathParser.PathData)startValue);
    }
    endValue = mKeyframes.getValue(1.0F);
    if ((endValue instanceof PathParser.PathData)) {
      endValue = new PathParser.PathData((PathParser.PathData)endValue);
    }
    if ((!(mKeyframes instanceof PathKeyframes.FloatKeyframesBase)) && (!(mKeyframes instanceof PathKeyframes.IntKeyframesBase)) && ((mKeyframes.getKeyframes() == null) || (mKeyframes.getKeyframes().size() <= 2))) {
      dataSource = null;
    } else {
      dataSource = new PropertyValuesHolder.PropertyValues.DataSource()
      {
        public Object getValueAtFraction(float paramAnonymousFloat)
        {
          return mKeyframes.getValue(paramAnonymousFloat);
        }
      };
    }
  }
  
  public Class getValueType()
  {
    return mValueType;
  }
  
  void init()
  {
    if (mEvaluator == null)
    {
      TypeEvaluator localTypeEvaluator;
      if (mValueType == Integer.class) {
        localTypeEvaluator = sIntEvaluator;
      } else if (mValueType == Float.class) {
        localTypeEvaluator = sFloatEvaluator;
      } else {
        localTypeEvaluator = null;
      }
      mEvaluator = localTypeEvaluator;
    }
    if (mEvaluator != null) {
      mKeyframes.setEvaluator(mEvaluator);
    }
  }
  
  void setAnimatedValue(Object paramObject)
  {
    if (mProperty != null) {
      mProperty.set(paramObject, getAnimatedValue());
    }
    if (mSetter != null) {
      try
      {
        mTmpValueArray[0] = getAnimatedValue();
        mSetter.invoke(paramObject, mTmpValueArray);
      }
      catch (IllegalAccessException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
      }
      catch (InvocationTargetException paramObject)
      {
        Log.e("PropertyValuesHolder", paramObject.toString());
      }
    }
  }
  
  public void setConverter(TypeConverter paramTypeConverter)
  {
    mConverter = paramTypeConverter;
  }
  
  public void setEvaluator(TypeEvaluator paramTypeEvaluator)
  {
    mEvaluator = paramTypeEvaluator;
    mKeyframes.setEvaluator(paramTypeEvaluator);
  }
  
  public void setFloatValues(float... paramVarArgs)
  {
    mValueType = Float.TYPE;
    mKeyframes = KeyframeSet.ofFloat(paramVarArgs);
  }
  
  public void setIntValues(int... paramVarArgs)
  {
    mValueType = Integer.TYPE;
    mKeyframes = KeyframeSet.ofInt(paramVarArgs);
  }
  
  public void setKeyframes(Keyframe... paramVarArgs)
  {
    int i = paramVarArgs.length;
    Keyframe[] arrayOfKeyframe = new Keyframe[Math.max(i, 2)];
    int j = 0;
    mValueType = paramVarArgs[0].getType();
    while (j < i)
    {
      arrayOfKeyframe[j] = paramVarArgs[j];
      j++;
    }
    mKeyframes = new KeyframeSet(arrayOfKeyframe);
  }
  
  public void setObjectValues(Object... paramVarArgs)
  {
    mValueType = paramVarArgs[0].getClass();
    mKeyframes = KeyframeSet.ofObject(paramVarArgs);
    if (mEvaluator != null) {
      mKeyframes.setEvaluator(mEvaluator);
    }
  }
  
  public void setProperty(Property paramProperty)
  {
    mProperty = paramProperty;
  }
  
  public void setPropertyName(String paramString)
  {
    mPropertyName = paramString;
  }
  
  void setupEndValue(Object paramObject)
  {
    List localList = mKeyframes.getKeyframes();
    if (!localList.isEmpty()) {
      setupValue(paramObject, (Keyframe)localList.get(localList.size() - 1));
    }
  }
  
  void setupSetter(Class paramClass)
  {
    Class localClass;
    if (mConverter == null) {
      localClass = mValueType;
    } else {
      localClass = mConverter.getTargetType();
    }
    mSetter = setupSetterOrGetter(paramClass, sSetterPropertyMap, "set", localClass);
  }
  
  void setupSetterAndGetter(Object paramObject)
  {
    Object localObject1;
    int i;
    int j;
    Object localObject4;
    Object localObject3;
    if (mProperty != null) {
      try
      {
        localObject1 = mKeyframes.getKeyframes();
        if (localObject1 == null) {
          i = 0;
        } else {
          i = ((List)localObject1).size();
        }
        Object localObject2 = null;
        j = 0;
        while (j < i)
        {
          Keyframe localKeyframe = (Keyframe)((List)localObject1).get(j);
          if (localKeyframe.hasValue())
          {
            localObject4 = localObject2;
            if (!localKeyframe.valueWasSetOnStart()) {}
          }
          else
          {
            localObject4 = localObject2;
            if (localObject2 == null) {
              localObject4 = convertBack(mProperty.get(paramObject));
            }
            localKeyframe.setValue(localObject4);
            localKeyframe.setValueWasSetOnStart(true);
          }
          j++;
          localObject2 = localObject4;
        }
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("No such property (");
        ((StringBuilder)localObject3).append(mProperty.getName());
        ((StringBuilder)localObject3).append(") on target object ");
        ((StringBuilder)localObject3).append(paramObject);
        ((StringBuilder)localObject3).append(". Trying reflection instead");
        Log.w("PropertyValuesHolder", ((StringBuilder)localObject3).toString());
        mProperty = null;
      }
    }
    if (mProperty == null)
    {
      localObject3 = paramObject.getClass();
      if (mSetter == null) {
        setupSetter((Class)localObject3);
      }
      localObject4 = mKeyframes.getKeyframes();
      if (localObject4 == null) {
        i = 0;
      } else {
        i = ((List)localObject4).size();
      }
      for (j = 0; j < i; j++)
      {
        localObject1 = (Keyframe)((List)localObject4).get(j);
        if ((!((Keyframe)localObject1).hasValue()) || (((Keyframe)localObject1).valueWasSetOnStart()))
        {
          if (mGetter == null)
          {
            setupGetter((Class)localObject3);
            if (mGetter == null) {
              return;
            }
          }
          try
          {
            ((Keyframe)localObject1).setValue(convertBack(mGetter.invoke(paramObject, new Object[0])));
            ((Keyframe)localObject1).setValueWasSetOnStart(true);
          }
          catch (IllegalAccessException localIllegalAccessException)
          {
            Log.e("PropertyValuesHolder", localIllegalAccessException.toString());
          }
          catch (InvocationTargetException localInvocationTargetException)
          {
            Log.e("PropertyValuesHolder", localInvocationTargetException.toString());
          }
        }
      }
    }
  }
  
  void setupStartValue(Object paramObject)
  {
    List localList = mKeyframes.getKeyframes();
    if (!localList.isEmpty()) {
      setupValue(paramObject, (Keyframe)localList.get(0));
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mPropertyName);
    localStringBuilder.append(": ");
    localStringBuilder.append(mKeyframes.toString());
    return localStringBuilder.toString();
  }
  
  static class FloatPropertyValuesHolder
    extends PropertyValuesHolder
  {
    private static final HashMap<Class, HashMap<String, Long>> sJNISetterPropertyMap = new HashMap();
    float mFloatAnimatedValue;
    Keyframes.FloatKeyframes mFloatKeyframes;
    private FloatProperty mFloatProperty;
    long mJniSetter;
    
    public FloatPropertyValuesHolder(Property paramProperty, Keyframes.FloatKeyframes paramFloatKeyframes)
    {
      super(null);
      mValueType = Float.TYPE;
      mKeyframes = paramFloatKeyframes;
      mFloatKeyframes = paramFloatKeyframes;
      if ((paramProperty instanceof FloatProperty)) {
        mFloatProperty = ((FloatProperty)mProperty);
      }
    }
    
    public FloatPropertyValuesHolder(Property paramProperty, float... paramVarArgs)
    {
      super(null);
      setFloatValues(paramVarArgs);
      if ((paramProperty instanceof FloatProperty)) {
        mFloatProperty = ((FloatProperty)mProperty);
      }
    }
    
    public FloatPropertyValuesHolder(String paramString, Keyframes.FloatKeyframes paramFloatKeyframes)
    {
      super(null);
      mValueType = Float.TYPE;
      mKeyframes = paramFloatKeyframes;
      mFloatKeyframes = paramFloatKeyframes;
    }
    
    public FloatPropertyValuesHolder(String paramString, float... paramVarArgs)
    {
      super(null);
      setFloatValues(paramVarArgs);
    }
    
    void calculateValue(float paramFloat)
    {
      mFloatAnimatedValue = mFloatKeyframes.getFloatValue(paramFloat);
    }
    
    public FloatPropertyValuesHolder clone()
    {
      FloatPropertyValuesHolder localFloatPropertyValuesHolder = (FloatPropertyValuesHolder)super.clone();
      mFloatKeyframes = ((Keyframes.FloatKeyframes)mKeyframes);
      return localFloatPropertyValuesHolder;
    }
    
    Object getAnimatedValue()
    {
      return Float.valueOf(mFloatAnimatedValue);
    }
    
    void setAnimatedValue(Object paramObject)
    {
      if (mFloatProperty != null)
      {
        mFloatProperty.setValue(paramObject, mFloatAnimatedValue);
        return;
      }
      if (mProperty != null)
      {
        mProperty.set(paramObject, Float.valueOf(mFloatAnimatedValue));
        return;
      }
      if (mJniSetter != 0L)
      {
        PropertyValuesHolder.nCallFloatMethod(paramObject, mJniSetter, mFloatAnimatedValue);
        return;
      }
      if (mSetter != null) {
        try
        {
          mTmpValueArray[0] = Float.valueOf(mFloatAnimatedValue);
          mSetter.invoke(paramObject, mTmpValueArray);
        }
        catch (IllegalAccessException paramObject)
        {
          Log.e("PropertyValuesHolder", paramObject.toString());
        }
        catch (InvocationTargetException paramObject)
        {
          Log.e("PropertyValuesHolder", paramObject.toString());
        }
      }
    }
    
    public void setFloatValues(float... paramVarArgs)
    {
      super.setFloatValues(paramVarArgs);
      mFloatKeyframes = ((Keyframes.FloatKeyframes)mKeyframes);
    }
    
    public void setProperty(Property paramProperty)
    {
      if ((paramProperty instanceof FloatProperty)) {
        mFloatProperty = ((FloatProperty)paramProperty);
      } else {
        super.setProperty(paramProperty);
      }
    }
    
    void setupSetter(Class paramClass)
    {
      if (mProperty != null) {
        return;
      }
      synchronized (sJNISetterPropertyMap)
      {
        HashMap localHashMap2 = (HashMap)sJNISetterPropertyMap.get(paramClass);
        int i = 0;
        Object localObject;
        if (localHashMap2 != null)
        {
          boolean bool = localHashMap2.containsKey(mPropertyName);
          i = bool;
          if (bool)
          {
            localObject = (Long)localHashMap2.get(mPropertyName);
            i = bool;
            if (localObject != null)
            {
              mJniSetter = ((Long)localObject).longValue();
              i = bool;
            }
          }
        }
        if (i == 0)
        {
          localObject = getMethodName("set", mPropertyName);
          try
          {
            mJniSetter = PropertyValuesHolder.nGetFloatMethod(paramClass, (String)localObject);
          }
          catch (NoSuchMethodError localNoSuchMethodError) {}
          HashMap localHashMap3 = localHashMap2;
          if (localHashMap2 == null)
          {
            localHashMap3 = new java/util/HashMap;
            localHashMap3.<init>();
            sJNISetterPropertyMap.put(paramClass, localHashMap3);
          }
          localHashMap3.put(mPropertyName, Long.valueOf(mJniSetter));
        }
        if (mJniSetter == 0L) {
          super.setupSetter(paramClass);
        }
        return;
      }
    }
  }
  
  static class IntPropertyValuesHolder
    extends PropertyValuesHolder
  {
    private static final HashMap<Class, HashMap<String, Long>> sJNISetterPropertyMap = new HashMap();
    int mIntAnimatedValue;
    Keyframes.IntKeyframes mIntKeyframes;
    private IntProperty mIntProperty;
    long mJniSetter;
    
    public IntPropertyValuesHolder(Property paramProperty, Keyframes.IntKeyframes paramIntKeyframes)
    {
      super(null);
      mValueType = Integer.TYPE;
      mKeyframes = paramIntKeyframes;
      mIntKeyframes = paramIntKeyframes;
      if ((paramProperty instanceof IntProperty)) {
        mIntProperty = ((IntProperty)mProperty);
      }
    }
    
    public IntPropertyValuesHolder(Property paramProperty, int... paramVarArgs)
    {
      super(null);
      setIntValues(paramVarArgs);
      if ((paramProperty instanceof IntProperty)) {
        mIntProperty = ((IntProperty)mProperty);
      }
    }
    
    public IntPropertyValuesHolder(String paramString, Keyframes.IntKeyframes paramIntKeyframes)
    {
      super(null);
      mValueType = Integer.TYPE;
      mKeyframes = paramIntKeyframes;
      mIntKeyframes = paramIntKeyframes;
    }
    
    public IntPropertyValuesHolder(String paramString, int... paramVarArgs)
    {
      super(null);
      setIntValues(paramVarArgs);
    }
    
    void calculateValue(float paramFloat)
    {
      mIntAnimatedValue = mIntKeyframes.getIntValue(paramFloat);
    }
    
    public IntPropertyValuesHolder clone()
    {
      IntPropertyValuesHolder localIntPropertyValuesHolder = (IntPropertyValuesHolder)super.clone();
      mIntKeyframes = ((Keyframes.IntKeyframes)mKeyframes);
      return localIntPropertyValuesHolder;
    }
    
    Object getAnimatedValue()
    {
      return Integer.valueOf(mIntAnimatedValue);
    }
    
    void setAnimatedValue(Object paramObject)
    {
      if (mIntProperty != null)
      {
        mIntProperty.setValue(paramObject, mIntAnimatedValue);
        return;
      }
      if (mProperty != null)
      {
        mProperty.set(paramObject, Integer.valueOf(mIntAnimatedValue));
        return;
      }
      if (mJniSetter != 0L)
      {
        PropertyValuesHolder.nCallIntMethod(paramObject, mJniSetter, mIntAnimatedValue);
        return;
      }
      if (mSetter != null) {
        try
        {
          mTmpValueArray[0] = Integer.valueOf(mIntAnimatedValue);
          mSetter.invoke(paramObject, mTmpValueArray);
        }
        catch (IllegalAccessException paramObject)
        {
          Log.e("PropertyValuesHolder", paramObject.toString());
        }
        catch (InvocationTargetException paramObject)
        {
          Log.e("PropertyValuesHolder", paramObject.toString());
        }
      }
    }
    
    public void setIntValues(int... paramVarArgs)
    {
      super.setIntValues(paramVarArgs);
      mIntKeyframes = ((Keyframes.IntKeyframes)mKeyframes);
    }
    
    public void setProperty(Property paramProperty)
    {
      if ((paramProperty instanceof IntProperty)) {
        mIntProperty = ((IntProperty)paramProperty);
      } else {
        super.setProperty(paramProperty);
      }
    }
    
    void setupSetter(Class paramClass)
    {
      if (mProperty != null) {
        return;
      }
      synchronized (sJNISetterPropertyMap)
      {
        HashMap localHashMap2 = (HashMap)sJNISetterPropertyMap.get(paramClass);
        int i = 0;
        Object localObject;
        if (localHashMap2 != null)
        {
          boolean bool = localHashMap2.containsKey(mPropertyName);
          i = bool;
          if (bool)
          {
            localObject = (Long)localHashMap2.get(mPropertyName);
            i = bool;
            if (localObject != null)
            {
              mJniSetter = ((Long)localObject).longValue();
              i = bool;
            }
          }
        }
        if (i == 0)
        {
          localObject = getMethodName("set", mPropertyName);
          try
          {
            mJniSetter = PropertyValuesHolder.nGetIntMethod(paramClass, (String)localObject);
          }
          catch (NoSuchMethodError localNoSuchMethodError) {}
          HashMap localHashMap3 = localHashMap2;
          if (localHashMap2 == null)
          {
            localHashMap3 = new java/util/HashMap;
            localHashMap3.<init>();
            sJNISetterPropertyMap.put(paramClass, localHashMap3);
          }
          localHashMap3.put(mPropertyName, Long.valueOf(mJniSetter));
        }
        if (mJniSetter == 0L) {
          super.setupSetter(paramClass);
        }
        return;
      }
    }
  }
  
  static class MultiFloatValuesHolder
    extends PropertyValuesHolder
  {
    private static final HashMap<Class, HashMap<String, Long>> sJNISetterPropertyMap = new HashMap();
    private long mJniSetter;
    
    public MultiFloatValuesHolder(String paramString, TypeConverter paramTypeConverter, TypeEvaluator paramTypeEvaluator, Keyframes paramKeyframes)
    {
      super(null);
      setConverter(paramTypeConverter);
      mKeyframes = paramKeyframes;
      setEvaluator(paramTypeEvaluator);
    }
    
    public MultiFloatValuesHolder(String paramString, TypeConverter paramTypeConverter, TypeEvaluator paramTypeEvaluator, Object... paramVarArgs)
    {
      super(null);
      setConverter(paramTypeConverter);
      setObjectValues(paramVarArgs);
      setEvaluator(paramTypeEvaluator);
    }
    
    void setAnimatedValue(Object paramObject)
    {
      float[] arrayOfFloat = (float[])getAnimatedValue();
      int i = arrayOfFloat.length;
      if (mJniSetter != 0L) {
        if (i != 4) {
          switch (i)
          {
          default: 
            PropertyValuesHolder.nCallMultipleFloatMethod(paramObject, mJniSetter, arrayOfFloat);
            break;
          case 2: 
            PropertyValuesHolder.nCallTwoFloatMethod(paramObject, mJniSetter, arrayOfFloat[0], arrayOfFloat[1]);
            break;
          case 1: 
            PropertyValuesHolder.nCallFloatMethod(paramObject, mJniSetter, arrayOfFloat[0]);
            break;
          }
        } else {
          PropertyValuesHolder.nCallFourFloatMethod(paramObject, mJniSetter, arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
        }
      }
    }
    
    void setupSetter(Class paramClass)
    {
      if (mJniSetter != 0L) {
        return;
      }
      synchronized (sJNISetterPropertyMap)
      {
        HashMap localHashMap2 = (HashMap)sJNISetterPropertyMap.get(paramClass);
        int i = 0;
        Object localObject;
        if (localHashMap2 != null)
        {
          boolean bool = localHashMap2.containsKey(mPropertyName);
          i = bool;
          if (bool)
          {
            localObject = (Long)localHashMap2.get(mPropertyName);
            i = bool;
            if (localObject != null)
            {
              mJniSetter = ((Long)localObject).longValue();
              i = bool;
            }
          }
        }
        if (i == 0)
        {
          localObject = getMethodName("set", mPropertyName);
          calculateValue(0.0F);
          int j = ((float[])getAnimatedValue()).length;
          try
          {
            mJniSetter = PropertyValuesHolder.nGetMultipleFloatMethod(paramClass, (String)localObject, j);
          }
          catch (NoSuchMethodError localNoSuchMethodError1)
          {
            try
            {
              mJniSetter = PropertyValuesHolder.nGetMultipleFloatMethod(paramClass, mPropertyName, j);
            }
            catch (NoSuchMethodError localNoSuchMethodError2) {}
          }
          HashMap localHashMap3 = localHashMap2;
          if (localHashMap2 == null)
          {
            localHashMap3 = new java/util/HashMap;
            localHashMap3.<init>();
            sJNISetterPropertyMap.put(paramClass, localHashMap3);
          }
          localHashMap3.put(mPropertyName, Long.valueOf(mJniSetter));
        }
        return;
      }
    }
    
    void setupSetterAndGetter(Object paramObject)
    {
      setupSetter(paramObject.getClass());
    }
  }
  
  static class MultiIntValuesHolder
    extends PropertyValuesHolder
  {
    private static final HashMap<Class, HashMap<String, Long>> sJNISetterPropertyMap = new HashMap();
    private long mJniSetter;
    
    public MultiIntValuesHolder(String paramString, TypeConverter paramTypeConverter, TypeEvaluator paramTypeEvaluator, Keyframes paramKeyframes)
    {
      super(null);
      setConverter(paramTypeConverter);
      mKeyframes = paramKeyframes;
      setEvaluator(paramTypeEvaluator);
    }
    
    public MultiIntValuesHolder(String paramString, TypeConverter paramTypeConverter, TypeEvaluator paramTypeEvaluator, Object... paramVarArgs)
    {
      super(null);
      setConverter(paramTypeConverter);
      setObjectValues(paramVarArgs);
      setEvaluator(paramTypeEvaluator);
    }
    
    void setAnimatedValue(Object paramObject)
    {
      int[] arrayOfInt = (int[])getAnimatedValue();
      int i = arrayOfInt.length;
      if (mJniSetter != 0L) {
        if (i != 4) {
          switch (i)
          {
          default: 
            PropertyValuesHolder.nCallMultipleIntMethod(paramObject, mJniSetter, arrayOfInt);
            break;
          case 2: 
            PropertyValuesHolder.nCallTwoIntMethod(paramObject, mJniSetter, arrayOfInt[0], arrayOfInt[1]);
            break;
          case 1: 
            PropertyValuesHolder.nCallIntMethod(paramObject, mJniSetter, arrayOfInt[0]);
            break;
          }
        } else {
          PropertyValuesHolder.nCallFourIntMethod(paramObject, mJniSetter, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
        }
      }
    }
    
    void setupSetter(Class paramClass)
    {
      if (mJniSetter != 0L) {
        return;
      }
      synchronized (sJNISetterPropertyMap)
      {
        HashMap localHashMap2 = (HashMap)sJNISetterPropertyMap.get(paramClass);
        int i = 0;
        Object localObject;
        if (localHashMap2 != null)
        {
          boolean bool = localHashMap2.containsKey(mPropertyName);
          i = bool;
          if (bool)
          {
            localObject = (Long)localHashMap2.get(mPropertyName);
            i = bool;
            if (localObject != null)
            {
              mJniSetter = ((Long)localObject).longValue();
              i = bool;
            }
          }
        }
        if (i == 0)
        {
          localObject = getMethodName("set", mPropertyName);
          calculateValue(0.0F);
          int j = ((int[])getAnimatedValue()).length;
          try
          {
            mJniSetter = PropertyValuesHolder.nGetMultipleIntMethod(paramClass, (String)localObject, j);
          }
          catch (NoSuchMethodError localNoSuchMethodError1)
          {
            try
            {
              mJniSetter = PropertyValuesHolder.nGetMultipleIntMethod(paramClass, mPropertyName, j);
            }
            catch (NoSuchMethodError localNoSuchMethodError2) {}
          }
          HashMap localHashMap3 = localHashMap2;
          if (localHashMap2 == null)
          {
            localHashMap3 = new java/util/HashMap;
            localHashMap3.<init>();
            sJNISetterPropertyMap.put(paramClass, localHashMap3);
          }
          localHashMap3.put(mPropertyName, Long.valueOf(mJniSetter));
        }
        return;
      }
    }
    
    void setupSetterAndGetter(Object paramObject)
    {
      setupSetter(paramObject.getClass());
    }
  }
  
  private static class PointFToFloatArray
    extends TypeConverter<PointF, float[]>
  {
    private float[] mCoordinates = new float[2];
    
    public PointFToFloatArray()
    {
      super([F.class);
    }
    
    public float[] convert(PointF paramPointF)
    {
      mCoordinates[0] = x;
      mCoordinates[1] = y;
      return mCoordinates;
    }
  }
  
  private static class PointFToIntArray
    extends TypeConverter<PointF, int[]>
  {
    private int[] mCoordinates = new int[2];
    
    public PointFToIntArray()
    {
      super([I.class);
    }
    
    public int[] convert(PointF paramPointF)
    {
      mCoordinates[0] = Math.round(x);
      mCoordinates[1] = Math.round(y);
      return mCoordinates;
    }
  }
  
  public static class PropertyValues
  {
    public DataSource dataSource = null;
    public Object endValue;
    public String propertyName;
    public Object startValue;
    public Class type;
    
    public PropertyValues() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("property name: ");
      localStringBuilder.append(propertyName);
      localStringBuilder.append(", type: ");
      localStringBuilder.append(type);
      localStringBuilder.append(", startValue: ");
      localStringBuilder.append(startValue.toString());
      localStringBuilder.append(", endValue: ");
      localStringBuilder.append(endValue.toString());
      return localStringBuilder.toString();
    }
    
    public static abstract interface DataSource
    {
      public abstract Object getValueAtFraction(float paramFloat);
    }
  }
}
