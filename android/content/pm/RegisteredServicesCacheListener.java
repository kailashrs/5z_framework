package android.content.pm;

public abstract interface RegisteredServicesCacheListener<V>
{
  public abstract void onServiceChanged(V paramV, int paramInt, boolean paramBoolean);
}
