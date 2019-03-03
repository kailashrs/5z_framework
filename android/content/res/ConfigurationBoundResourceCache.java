package android.content.res;

public class ConfigurationBoundResourceCache<T>
  extends ThemedResourceCache<ConstantState<T>>
{
  public ConfigurationBoundResourceCache() {}
  
  public T getInstance(long paramLong, Resources paramResources, Resources.Theme paramTheme)
  {
    ConstantState localConstantState = (ConstantState)get(paramLong, paramTheme);
    if (localConstantState != null) {
      return localConstantState.newInstance(paramResources, paramTheme);
    }
    return null;
  }
  
  public boolean shouldInvalidateEntry(ConstantState<T> paramConstantState, int paramInt)
  {
    return Configuration.needNewResources(paramInt, paramConstantState.getChangingConfigurations());
  }
}
