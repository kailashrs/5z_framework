package android.webkit;

public abstract class SafeBrowsingResponse
{
  public SafeBrowsingResponse() {}
  
  public abstract void backToSafety(boolean paramBoolean);
  
  public abstract void proceed(boolean paramBoolean);
  
  public abstract void showInterstitial(boolean paramBoolean);
}
