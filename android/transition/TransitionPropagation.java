package android.transition;

import android.view.ViewGroup;

public abstract class TransitionPropagation
{
  public TransitionPropagation() {}
  
  public abstract void captureValues(TransitionValues paramTransitionValues);
  
  public abstract String[] getPropagationProperties();
  
  public abstract long getStartDelay(ViewGroup paramViewGroup, Transition paramTransition, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2);
}
