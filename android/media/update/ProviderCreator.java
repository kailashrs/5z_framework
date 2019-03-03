package android.media.update;

@FunctionalInterface
public abstract interface ProviderCreator<T, U>
{
  public abstract U createProvider(T paramT);
}
