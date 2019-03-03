package android.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectiveProperty<T, V>
  extends Property<T, V>
{
  private static final String PREFIX_GET = "get";
  private static final String PREFIX_IS = "is";
  private static final String PREFIX_SET = "set";
  private Field mField;
  private Method mGetter;
  private Method mSetter;
  
  public ReflectiveProperty(Class<T> paramClass, Class<V> paramClass1, String paramString)
  {
    super(paramClass1, paramString);
    char c = Character.toUpperCase(paramString.charAt(0));
    String str = paramString.substring(1);
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(c);
    ((StringBuilder)localObject1).append(str);
    str = ((StringBuilder)localObject1).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("get");
    ((StringBuilder)localObject1).append(str);
    localObject1 = ((StringBuilder)localObject1).toString();
    Object localObject2;
    try
    {
      mGetter = paramClass.getMethod((String)localObject1, (Class[])null);
    }
    catch (NoSuchMethodException localNoSuchMethodException2)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("is");
      ((StringBuilder)localObject2).append(str);
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    try
    {
      mGetter = paramClass.getMethod((String)localObject2, (Class[])null);
      paramString = mGetter.getReturnType();
      if (typesMatch(paramClass1, paramString))
      {
        paramClass1 = new StringBuilder();
        paramClass1.append("set");
        paramClass1.append(str);
        paramClass1 = paramClass1.toString();
        try
        {
          mSetter = paramClass.getMethod(paramClass1, new Class[] { paramString });
        }
        catch (NoSuchMethodException paramClass) {}
        return;
      }
      paramClass = new StringBuilder();
      paramClass.append("Underlying type (");
      paramClass.append(paramString);
      paramClass.append(") does not match Property type (");
      paramClass.append(paramClass1);
      paramClass.append(")");
      throw new NoSuchPropertyException(paramClass.toString());
    }
    catch (NoSuchMethodException localNoSuchMethodException1)
    {
      try
      {
        mField = paramClass.getField(paramString);
        localObject2 = mField.getType();
        if (typesMatch(paramClass1, (Class)localObject2)) {
          return;
        }
        paramClass = new android/util/NoSuchPropertyException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Underlying type (");
        localStringBuilder.append(localObject2);
        localStringBuilder.append(") does not match Property type (");
        localStringBuilder.append(paramClass1);
        localStringBuilder.append(")");
        paramClass.<init>(localStringBuilder.toString());
        throw paramClass;
      }
      catch (NoSuchFieldException paramClass)
      {
        paramClass = new StringBuilder();
        paramClass.append("No accessor method or field found for property with name ");
        paramClass.append(paramString);
        throw new NoSuchPropertyException(paramClass.toString());
      }
    }
  }
  
  private boolean typesMatch(Class<V> paramClass, Class paramClass1)
  {
    boolean bool = true;
    if (paramClass1 != paramClass)
    {
      if (paramClass1.isPrimitive())
      {
        if (((paramClass1 == Float.TYPE) && (paramClass == Float.class)) || ((paramClass1 == Integer.TYPE) && (paramClass == Integer.class)) || ((paramClass1 == Boolean.TYPE) && (paramClass == Boolean.class)) || ((paramClass1 == Long.TYPE) && (paramClass == Long.class)) || ((paramClass1 == Double.TYPE) && (paramClass == Double.class)) || ((paramClass1 == Short.TYPE) && (paramClass == Short.class)) || ((paramClass1 == Byte.TYPE) && (paramClass == Byte.class)) || ((paramClass1 != Character.TYPE) || (paramClass != Character.class))) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    return true;
  }
  
  public V get(T paramT)
  {
    if (mGetter != null) {
      try
      {
        paramT = mGetter.invoke(paramT, (Object[])null);
        return paramT;
      }
      catch (InvocationTargetException paramT)
      {
        throw new RuntimeException(paramT.getCause());
      }
      catch (IllegalAccessException paramT)
      {
        throw new AssertionError();
      }
    }
    if (mField != null) {
      try
      {
        paramT = mField.get(paramT);
        return paramT;
      }
      catch (IllegalAccessException paramT)
      {
        throw new AssertionError();
      }
    }
    throw new AssertionError();
  }
  
  public boolean isReadOnly()
  {
    boolean bool;
    if ((mSetter == null) && (mField == null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void set(T paramT, V paramV)
  {
    if (mSetter != null) {
      try
      {
        mSetter.invoke(paramT, new Object[] { paramV });
      }
      catch (InvocationTargetException paramT)
      {
        throw new RuntimeException(paramT.getCause());
      }
      catch (IllegalAccessException paramT)
      {
        throw new AssertionError();
      }
    } else {
      if (mField == null) {
        break label75;
      }
    }
    try
    {
      mField.set(paramT, paramV);
      return;
    }
    catch (IllegalAccessException paramT)
    {
      throw new AssertionError();
    }
    label75:
    paramT = new StringBuilder();
    paramT.append("Property ");
    paramT.append(getName());
    paramT.append(" is read-only");
    throw new UnsupportedOperationException(paramT.toString());
  }
}
