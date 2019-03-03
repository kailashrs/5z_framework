package android.provider;

public abstract class OneTimeUseBuilder<T>
{
  private boolean used = false;
  
  public OneTimeUseBuilder() {}
  
  public abstract T build();
  
  protected void checkNotUsed()
  {
    if (!used) {
      return;
    }
    throw new IllegalStateException("This Builder should not be reused. Use a new Builder instance instead");
  }
  
  protected void markUsed()
  {
    checkNotUsed();
    used = true;
  }
}
