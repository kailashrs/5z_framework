package android.view.accessibility;

public abstract interface AccessibilityEventSource
{
  public abstract void sendAccessibilityEvent(int paramInt);
  
  public abstract void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent);
}
