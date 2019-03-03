package android.animation;

public abstract class TypeConverter<T, V>
{
  private Class<T> mFromClass;
  private Class<V> mToClass;
  
  public TypeConverter(Class<T> paramClass, Class<V> paramClass1)
  {
    mFromClass = paramClass;
    mToClass = paramClass1;
  }
  
  public abstract V convert(T paramT);
  
  Class<T> getSourceType()
  {
    return mFromClass;
  }
  
  Class<V> getTargetType()
  {
    return mToClass;
  }
}
