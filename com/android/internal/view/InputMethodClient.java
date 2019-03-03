package com.android.internal.view;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class InputMethodClient
{
  public static final int START_INPUT_REASON_ACTIVATED_BY_IMMS = 7;
  public static final int START_INPUT_REASON_APP_CALLED_RESTART_INPUT_API = 3;
  public static final int START_INPUT_REASON_BOUND_TO_IMMS = 5;
  public static final int START_INPUT_REASON_CHECK_FOCUS = 4;
  public static final int START_INPUT_REASON_DEACTIVATED_BY_IMMS = 8;
  public static final int START_INPUT_REASON_SESSION_CREATED_BY_IME = 9;
  public static final int START_INPUT_REASON_UNBOUND_FROM_IMMS = 6;
  public static final int START_INPUT_REASON_UNSPECIFIED = 0;
  public static final int START_INPUT_REASON_WINDOW_FOCUS_GAIN = 1;
  public static final int START_INPUT_REASON_WINDOW_FOCUS_GAIN_REPORT_ONLY = 2;
  public static final int UNBIND_REASON_DISCONNECT_IME = 3;
  public static final int UNBIND_REASON_NO_IME = 4;
  public static final int UNBIND_REASON_SWITCH_CLIENT = 1;
  public static final int UNBIND_REASON_SWITCH_IME = 2;
  public static final int UNBIND_REASON_SWITCH_IME_FAILED = 5;
  public static final int UNBIND_REASON_SWITCH_USER = 6;
  public static final int UNBIND_REASON_UNSPECIFIED = 0;
  
  public InputMethodClient() {}
  
  public static String getStartInputReason(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown=");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 9: 
      return "SESSION_CREATED_BY_IME";
    case 8: 
      return "DEACTIVATED_BY_IMMS";
    case 7: 
      return "ACTIVATED_BY_IMMS";
    case 6: 
      return "UNBOUND_FROM_IMMS";
    case 5: 
      return "BOUND_TO_IMMS";
    case 4: 
      return "CHECK_FOCUS";
    case 3: 
      return "APP_CALLED_RESTART_INPUT_API";
    case 2: 
      return "WINDOW_FOCUS_GAIN_REPORT_ONLY";
    case 1: 
      return "WINDOW_FOCUS_GAIN";
    }
    return "UNSPECIFIED";
  }
  
  public static String getUnbindReason(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown=");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 6: 
      return "SWITCH_USER";
    case 5: 
      return "SWITCH_IME_FAILED";
    case 4: 
      return "NO_IME";
    case 3: 
      return "DISCONNECT_IME";
    case 2: 
      return "SWITCH_IME";
    case 1: 
      return "SWITCH_CLIENT";
    }
    return "UNSPECIFIED";
  }
  
  public static String softInputModeToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramInt & 0xF;
    int j = paramInt & 0xF0;
    if ((paramInt & 0x100) != 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    switch (i)
    {
    default: 
      localStringBuilder.append("STATE_UNKNOWN(");
      localStringBuilder.append(i);
      localStringBuilder.append(")");
      break;
    case 5: 
      localStringBuilder.append("STATE_ALWAYS_VISIBLE");
      break;
    case 4: 
      localStringBuilder.append("STATE_VISIBLE");
      break;
    case 3: 
      localStringBuilder.append("STATE_ALWAYS_HIDDEN");
      break;
    case 2: 
      localStringBuilder.append("STATE_HIDDEN");
      break;
    case 1: 
      localStringBuilder.append("STATE_UNCHANGED");
      break;
    case 0: 
      localStringBuilder.append("STATE_UNSPECIFIED");
    }
    if (j != 0)
    {
      if (j != 16)
      {
        if (j != 32)
        {
          if (j != 48)
          {
            localStringBuilder.append("|ADJUST_UNKNOWN(");
            localStringBuilder.append(j);
            localStringBuilder.append(")");
          }
          else
          {
            localStringBuilder.append("|ADJUST_NOTHING");
          }
        }
        else {
          localStringBuilder.append("|ADJUST_PAN");
        }
      }
      else {
        localStringBuilder.append("|ADJUST_RESIZE");
      }
    }
    else {
      localStringBuilder.append("|ADJUST_UNSPECIFIED");
    }
    if (paramInt != 0) {
      localStringBuilder.append("|IS_FORWARD_NAVIGATION");
    }
    return localStringBuilder.toString();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StartInputReason {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UnbindReason {}
}
