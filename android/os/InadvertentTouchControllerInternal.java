package android.os;

public abstract class InadvertentTouchControllerInternal
{
  public InadvertentTouchControllerInternal() {}
  
  public abstract void combinePowerFPKey();
  
  public abstract void fingerInadvertentTouch(int paramInt);
  
  public abstract void notifyHardwarekeyPressed();
  
  public abstract void notifyKeyguardUnlock();
  
  public abstract void notifyLaunchInstantCamera();
  
  public abstract void notifyLidSwitchState(int paramInt);
}
