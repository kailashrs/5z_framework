package android.util;

public abstract class IntProperty<T>
  extends Property<T, Integer>
{
  public IntProperty(String paramString)
  {
    super(Integer.class, paramString);
  }
  
  public final void set(T paramT, Integer paramInteger)
  {
    setValue(paramT, paramInteger.intValue());
  }
  
  public abstract void setValue(T paramT, int paramInt);
}
