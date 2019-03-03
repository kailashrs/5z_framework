package android.net;

public abstract class NetworkSpecifier
{
  public NetworkSpecifier() {}
  
  public void assertValidFromUid(int paramInt) {}
  
  public abstract boolean satisfiedBy(NetworkSpecifier paramNetworkSpecifier);
}
