package android.animation;

public abstract class AnimatorListenerAdapter
  implements Animator.AnimatorListener, Animator.AnimatorPauseListener
{
  public AnimatorListenerAdapter() {}
  
  public void onAnimationCancel(Animator paramAnimator) {}
  
  public void onAnimationEnd(Animator paramAnimator) {}
  
  public void onAnimationPause(Animator paramAnimator) {}
  
  public void onAnimationRepeat(Animator paramAnimator) {}
  
  public void onAnimationResume(Animator paramAnimator) {}
  
  public void onAnimationStart(Animator paramAnimator) {}
}
