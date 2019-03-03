package android.graphics.drawable;

public abstract interface Animatable2
  extends Animatable
{
  public abstract void clearAnimationCallbacks();
  
  public abstract void registerAnimationCallback(AnimationCallback paramAnimationCallback);
  
  public abstract boolean unregisterAnimationCallback(AnimationCallback paramAnimationCallback);
  
  public static abstract class AnimationCallback
  {
    public AnimationCallback() {}
    
    public void onAnimationEnd(Drawable paramDrawable) {}
    
    public void onAnimationStart(Drawable paramDrawable) {}
  }
}
