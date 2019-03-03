package android.graphics.drawable;

public abstract interface Animatable
{
  public abstract boolean isRunning();
  
  public abstract void start();
  
  public abstract void stop();
}
