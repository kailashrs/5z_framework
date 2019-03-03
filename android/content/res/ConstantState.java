package android.content.res;

public abstract class ConstantState<T>
{
  public ConstantState() {}
  
  public abstract int getChangingConfigurations();
  
  public abstract T newInstance();
  
  public T newInstance(Resources paramResources)
  {
    return newInstance();
  }
  
  public T newInstance(Resources paramResources, Resources.Theme paramTheme)
  {
    return newInstance(paramResources);
  }
}
