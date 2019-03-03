package android.view;

public abstract interface FallbackEventHandler
{
  public abstract boolean dispatchKeyEvent(KeyEvent paramKeyEvent);
  
  public abstract void preDispatchKeyEvent(KeyEvent paramKeyEvent);
  
  public abstract void setView(View paramView);
}
