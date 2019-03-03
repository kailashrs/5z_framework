package android.view;

public abstract interface WindowManagerPolicyConstants
{
  public static final String ACTION_HDMI_PLUGGED = "android.intent.action.HDMI_PLUGGED";
  public static final String ACTION_USER_ACTIVITY_NOTIFICATION = "android.intent.action.USER_ACTIVITY_NOTIFICATION";
  public static final int APPLICATION_ABOVE_SUB_PANEL_SUBLAYER = 3;
  public static final int APPLICATION_LAYER = 2;
  public static final int APPLICATION_MEDIA_OVERLAY_SUBLAYER = -1;
  public static final int APPLICATION_MEDIA_SUBLAYER = -2;
  public static final int APPLICATION_PANEL_SUBLAYER = 1;
  public static final int APPLICATION_SUB_PANEL_SUBLAYER = 2;
  public static final String EXTRA_FROM_HOME_KEY = "android.intent.extra.FROM_HOME_KEY";
  public static final String EXTRA_HDMI_PLUGGED_STATE = "state";
  public static final int FLAG_DISABLE_KEY_REPEAT = 134217728;
  public static final int FLAG_FILTERED = 67108864;
  public static final int FLAG_INJECTED = 16777216;
  public static final int FLAG_INTERACTIVE = 536870912;
  public static final int FLAG_PASS_TO_USER = 1073741824;
  public static final int FLAG_TRUSTED = 33554432;
  public static final int FLAG_UNLOCK = 128;
  public static final int FLAG_VIRTUAL = 2;
  public static final int FLAG_WAKE = 1;
  public static final int KEYGUARD_GOING_AWAY_FLAG_NO_WINDOW_ANIMATIONS = 2;
  public static final int KEYGUARD_GOING_AWAY_FLAG_TO_SHADE = 1;
  public static final int KEYGUARD_GOING_AWAY_FLAG_WITH_WALLPAPER = 4;
  public static final int NAV_BAR_BOTTOM = 4;
  public static final int NAV_BAR_LEFT = 1;
  public static final int NAV_BAR_RIGHT = 2;
  public static final int OFF_BECAUSE_OF_ADMIN = 1;
  public static final int OFF_BECAUSE_OF_TIMEOUT = 3;
  public static final int OFF_BECAUSE_OF_TIMEOUT_POSTPONED = 11;
  public static final int OFF_BECAUSE_OF_UNKNOWN = 10;
  public static final int OFF_BECAUSE_OF_USER = 2;
  public static final int OFF_BECAUSE_OF_WM_TURN_ON = 12;
  public static final int PRESENCE_EXTERNAL = 2;
  public static final int PRESENCE_INTERNAL = 1;
  
  public static String offReasonToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        return Integer.toString(paramInt);
      case 12: 
        return "OFF_BECAUSE_OF_WM_TURN_ON";
      case 11: 
        return "OFF_BECAUSE_OF_TIMEOUT_POSTPONED";
      }
      return "OFF_BECAUSE_OF_UNKNOWN";
    case 3: 
      return "OFF_BECAUSE_OF_TIMEOUT";
    case 2: 
      return "OFF_BECAUSE_OF_USER";
    }
    return "OFF_BECAUSE_OF_ADMIN";
  }
  
  public static abstract interface PointerEventListener
  {
    public abstract void onPointerEvent(MotionEvent paramMotionEvent);
    
    public void onPointerEvent(MotionEvent paramMotionEvent, int paramInt)
    {
      if (paramInt == 0) {
        onPointerEvent(paramMotionEvent);
      }
    }
  }
}
