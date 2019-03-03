package android.hardware.location;

import android.annotation.SystemApi;

@SystemApi
public class ContextHubClientCallback
{
  public ContextHubClientCallback() {}
  
  public void onHubReset(ContextHubClient paramContextHubClient) {}
  
  public void onMessageFromNanoApp(ContextHubClient paramContextHubClient, NanoAppMessage paramNanoAppMessage) {}
  
  public void onNanoAppAborted(ContextHubClient paramContextHubClient, long paramLong, int paramInt) {}
  
  public void onNanoAppDisabled(ContextHubClient paramContextHubClient, long paramLong) {}
  
  public void onNanoAppEnabled(ContextHubClient paramContextHubClient, long paramLong) {}
  
  public void onNanoAppLoaded(ContextHubClient paramContextHubClient, long paramLong) {}
  
  public void onNanoAppUnloaded(ContextHubClient paramContextHubClient, long paramLong) {}
}
