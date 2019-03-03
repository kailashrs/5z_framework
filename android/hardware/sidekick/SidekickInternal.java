package android.hardware.sidekick;

public abstract class SidekickInternal
{
  public SidekickInternal() {}
  
  public abstract void endDisplayControl();
  
  public abstract boolean reset();
  
  public abstract boolean startDisplayControl(int paramInt);
}
