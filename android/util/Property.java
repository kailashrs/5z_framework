package android.util;

public abstract class Property<T, V>
{
  private final String mName;
  private final Class<V> mType;
  
  public Property(Class<V> paramClass, String paramString)
  {
    mName = paramString;
    mType = paramClass;
  }
  
  public static <T, V> Property<T, V> of(Class<T> paramClass, Class<V> paramClass1, String paramString)
  {
    return new ReflectiveProperty(paramClass, paramClass1, paramString);
  }
  
  public abstract V get(T paramT);
  
  public String getName()
  {
    return mName;
  }
  
  public Class<V> getType()
  {
    return mType;
  }
  
  public boolean isReadOnly()
  {
    return false;
  }
  
  public void set(T paramT, V paramV)
  {
    paramT = new StringBuilder();
    paramT.append("Property ");
    paramT.append(getName());
    paramT.append(" is read-only");
    throw new UnsupportedOperationException(paramT.toString());
  }
}
