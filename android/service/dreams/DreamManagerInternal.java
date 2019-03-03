package android.service.dreams;

public abstract class DreamManagerInternal
{
  public DreamManagerInternal() {}
  
  public abstract boolean isDreaming();
  
  public abstract void startDream(boolean paramBoolean);
  
  public abstract void stopDream(boolean paramBoolean);
}
