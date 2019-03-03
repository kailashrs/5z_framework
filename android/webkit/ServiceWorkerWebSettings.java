package android.webkit;

public abstract class ServiceWorkerWebSettings
{
  public ServiceWorkerWebSettings() {}
  
  public abstract boolean getAllowContentAccess();
  
  public abstract boolean getAllowFileAccess();
  
  public abstract boolean getBlockNetworkLoads();
  
  public abstract int getCacheMode();
  
  public abstract void setAllowContentAccess(boolean paramBoolean);
  
  public abstract void setAllowFileAccess(boolean paramBoolean);
  
  public abstract void setBlockNetworkLoads(boolean paramBoolean);
  
  public abstract void setCacheMode(int paramInt);
}
