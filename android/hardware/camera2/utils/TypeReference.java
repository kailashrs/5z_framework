package android.hardware.camera2.utils;

import com.android.internal.util.Preconditions;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public abstract class TypeReference<T>
{
  private final int mHash;
  private final Type mType;
  
  protected TypeReference()
  {
    mType = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    if (!containsTypeVariable(mType))
    {
      mHash = mType.hashCode();
      return;
    }
    throw new IllegalArgumentException("Including a type variable in a type reference is not allowed");
  }
  
  private TypeReference(Type paramType)
  {
    mType = paramType;
    if (!containsTypeVariable(mType))
    {
      mHash = mType.hashCode();
      return;
    }
    throw new IllegalArgumentException("Including a type variable in a type reference is not allowed");
  }
  
  public static boolean containsTypeVariable(Type paramType)
  {
    boolean bool = false;
    if (paramType == null) {
      return false;
    }
    if ((paramType instanceof TypeVariable)) {
      return true;
    }
    if ((paramType instanceof Class))
    {
      paramType = (Class)paramType;
      if (paramType.getTypeParameters().length != 0) {
        return true;
      }
      return containsTypeVariable(paramType.getDeclaringClass());
    }
    if ((paramType instanceof ParameterizedType))
    {
      paramType = ((ParameterizedType)paramType).getActualTypeArguments();
      int i = paramType.length;
      for (int j = 0; j < i; j++) {
        if (containsTypeVariable(paramType[j])) {
          return true;
        }
      }
      return false;
    }
    if ((paramType instanceof WildcardType))
    {
      paramType = (WildcardType)paramType;
      if ((!containsTypeVariable(paramType.getLowerBounds())) && (!containsTypeVariable(paramType.getUpperBounds()))) {
        break label137;
      }
      bool = true;
      label137:
      return bool;
    }
    return false;
  }
  
  private static boolean containsTypeVariable(Type[] paramArrayOfType)
  {
    if (paramArrayOfType == null) {
      return false;
    }
    int i = paramArrayOfType.length;
    for (int j = 0; j < i; j++) {
      if (containsTypeVariable(paramArrayOfType[j])) {
        return true;
      }
    }
    return false;
  }
  
  public static <T> TypeReference<T> createSpecializedTypeReference(Class<T> paramClass)
  {
    return new SpecializedTypeReference(paramClass);
  }
  
  public static TypeReference<?> createSpecializedTypeReference(Type paramType)
  {
    return new SpecializedBaseTypeReference(paramType);
  }
  
  private static final Class<?> getArrayClass(Class<?> paramClass)
  {
    return Array.newInstance(paramClass, 0).getClass();
  }
  
  private static Type getComponentType(Type paramType)
  {
    Preconditions.checkNotNull(paramType, "type must not be null");
    if ((paramType instanceof Class)) {
      return ((Class)paramType).getComponentType();
    }
    if ((paramType instanceof ParameterizedType)) {
      return null;
    }
    if ((paramType instanceof GenericArrayType)) {
      return ((GenericArrayType)paramType).getGenericComponentType();
    }
    if (!(paramType instanceof WildcardType))
    {
      if ((paramType instanceof TypeVariable)) {
        throw new AssertionError("Type variables are not allowed in type references");
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unhandled branch to get component type for type ");
      localStringBuilder.append(paramType);
      throw new AssertionError(localStringBuilder.toString());
    }
    throw new UnsupportedOperationException("TODO: support wild card components");
  }
  
  private static final Class<?> getRawType(Type paramType)
  {
    if (paramType != null)
    {
      if ((paramType instanceof Class)) {
        return (Class)paramType;
      }
      if ((paramType instanceof ParameterizedType)) {
        return (Class)((ParameterizedType)paramType).getRawType();
      }
      if ((paramType instanceof GenericArrayType)) {
        return getArrayClass(getRawType(((GenericArrayType)paramType).getGenericComponentType()));
      }
      if ((paramType instanceof WildcardType)) {
        return getRawType(((WildcardType)paramType).getUpperBounds());
      }
      if ((paramType instanceof TypeVariable)) {
        throw new AssertionError("Type variables are not allowed in type references");
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unhandled branch to get raw type for type ");
      localStringBuilder.append(paramType);
      throw new AssertionError(localStringBuilder.toString());
    }
    throw new NullPointerException("type must not be null");
  }
  
  private static final Class<?> getRawType(Type[] paramArrayOfType)
  {
    if (paramArrayOfType == null) {
      return null;
    }
    int i = paramArrayOfType.length;
    for (int j = 0; j < i; j++)
    {
      Class localClass = getRawType(paramArrayOfType[j]);
      if (localClass != null) {
        return localClass;
      }
    }
    return null;
  }
  
  private static void toString(Type paramType, StringBuilder paramStringBuilder)
  {
    if (paramType == null) {
      return;
    }
    if ((paramType instanceof TypeVariable))
    {
      paramStringBuilder.append(((TypeVariable)paramType).getName());
    }
    else if ((paramType instanceof Class))
    {
      paramType = (Class)paramType;
      paramStringBuilder.append(paramType.getName());
      toString(paramType.getTypeParameters(), paramStringBuilder);
    }
    else if ((paramType instanceof ParameterizedType))
    {
      paramType = (ParameterizedType)paramType;
      paramStringBuilder.append(((Class)paramType.getRawType()).getName());
      toString(paramType.getActualTypeArguments(), paramStringBuilder);
    }
    else if ((paramType instanceof GenericArrayType))
    {
      toString(((GenericArrayType)paramType).getGenericComponentType(), paramStringBuilder);
      paramStringBuilder.append("[]");
    }
    else
    {
      paramStringBuilder.append(paramType.toString());
    }
  }
  
  private static void toString(Type[] paramArrayOfType, StringBuilder paramStringBuilder)
  {
    if (paramArrayOfType == null) {
      return;
    }
    if (paramArrayOfType.length == 0) {
      return;
    }
    paramStringBuilder.append("<");
    for (int i = 0; i < paramArrayOfType.length; i++)
    {
      toString(paramArrayOfType[i], paramStringBuilder);
      if (i != paramArrayOfType.length - 1) {
        paramStringBuilder.append(", ");
      }
    }
    paramStringBuilder.append(">");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof TypeReference)) && (mType.equals(mType))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public TypeReference<?> getComponentType()
  {
    Object localObject = getComponentType(mType);
    if (localObject != null) {
      localObject = createSpecializedTypeReference((Type)localObject);
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public final Class<? super T> getRawType()
  {
    return getRawType(mType);
  }
  
  public Type getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    return mHash;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TypeReference<");
    toString(getType(), localStringBuilder);
    localStringBuilder.append(">");
    return localStringBuilder.toString();
  }
  
  private static class SpecializedBaseTypeReference
    extends TypeReference
  {
    public SpecializedBaseTypeReference(Type paramType)
    {
      super(null);
    }
  }
  
  private static class SpecializedTypeReference<T>
    extends TypeReference<T>
  {
    public SpecializedTypeReference(Class<T> paramClass)
    {
      super(null);
    }
  }
}
