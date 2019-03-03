package android.net.lowpan;

import java.util.Map;

public abstract class LowpanProperty<T>
{
  public LowpanProperty() {}
  
  public T getFromMap(Map paramMap)
  {
    return paramMap.get(getName());
  }
  
  public abstract String getName();
  
  public abstract Class<T> getType();
  
  public void putInMap(Map paramMap, T paramT)
  {
    paramMap.put(getName(), paramT);
  }
}
