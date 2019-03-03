package android.view;

import android.graphics.Rect;

public class FocusFinderHelper
{
  private FocusFinder mFocusFinder;
  
  public FocusFinderHelper(FocusFinder paramFocusFinder)
  {
    mFocusFinder = paramFocusFinder;
  }
  
  public static int majorAxisDistance(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    return FocusFinder.majorAxisDistance(paramInt, paramRect1, paramRect2);
  }
  
  public static int majorAxisDistanceToFarEdge(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    return FocusFinder.majorAxisDistanceToFarEdge(paramInt, paramRect1, paramRect2);
  }
  
  public boolean beamBeats(int paramInt, Rect paramRect1, Rect paramRect2, Rect paramRect3)
  {
    return mFocusFinder.beamBeats(paramInt, paramRect1, paramRect2, paramRect3);
  }
  
  public boolean beamsOverlap(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    return mFocusFinder.beamsOverlap(paramInt, paramRect1, paramRect2);
  }
  
  public boolean isBetterCandidate(int paramInt, Rect paramRect1, Rect paramRect2, Rect paramRect3)
  {
    return mFocusFinder.isBetterCandidate(paramInt, paramRect1, paramRect2, paramRect3);
  }
  
  public boolean isCandidate(Rect paramRect1, Rect paramRect2, int paramInt)
  {
    return mFocusFinder.isCandidate(paramRect1, paramRect2, paramInt);
  }
}
