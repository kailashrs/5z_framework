package android.net.wifi.aware;

import java.util.List;

public class DiscoverySessionCallback
{
  public DiscoverySessionCallback() {}
  
  public void onMessageReceived(PeerHandle paramPeerHandle, byte[] paramArrayOfByte) {}
  
  public void onMessageSendFailed(int paramInt) {}
  
  public void onMessageSendSucceeded(int paramInt) {}
  
  public void onPublishStarted(PublishDiscoverySession paramPublishDiscoverySession) {}
  
  public void onServiceDiscovered(PeerHandle paramPeerHandle, byte[] paramArrayOfByte, List<byte[]> paramList) {}
  
  public void onServiceDiscoveredWithinRange(PeerHandle paramPeerHandle, byte[] paramArrayOfByte, List<byte[]> paramList, int paramInt) {}
  
  public void onSessionConfigFailed() {}
  
  public void onSessionConfigUpdated() {}
  
  public void onSessionTerminated() {}
  
  public void onSubscribeStarted(SubscribeDiscoverySession paramSubscribeDiscoverySession) {}
}
