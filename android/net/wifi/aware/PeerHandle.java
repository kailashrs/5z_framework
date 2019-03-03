package android.net.wifi.aware;

public class PeerHandle
{
  public int peerId;
  
  public PeerHandle(int paramInt)
  {
    peerId = paramInt;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof PeerHandle)) {
      return false;
    }
    if (peerId != peerId) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return peerId;
  }
}
