package android.view;

public class SoundEffectConstants
{
  public static final int CLICK = 0;
  public static final int NAVIGATION_DOWN = 4;
  public static final int NAVIGATION_LEFT = 1;
  public static final int NAVIGATION_RIGHT = 3;
  public static final int NAVIGATION_UP = 2;
  
  private SoundEffectConstants() {}
  
  public static int getContantForFocusDirection(int paramInt)
  {
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66) {
          if (paramInt == 130) {}
        }
        switch (paramInt)
        {
        default: 
          throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        case 2: 
          return 4;
          return 3;
        }
      }
      return 2;
    }
    return 1;
  }
}
