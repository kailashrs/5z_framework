package android.util;

public abstract class Singleton<T>
{
  private T mInstance;
  
  public Singleton() {}
  
  protected abstract T create();
  
  public final T get()
  {
    try
    {
      if (mInstance == null) {
        mInstance = create();
      }
      Object localObject1 = mInstance;
      return localObject1;
    }
    finally {}
  }
}
