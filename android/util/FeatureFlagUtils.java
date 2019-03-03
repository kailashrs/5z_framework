package android.util;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class FeatureFlagUtils
{
  private static final Map<String, String> DEFAULT_FLAGS = new HashMap();
  public static final String FFLAG_OVERRIDE_PREFIX = "sys.fflag.override.";
  public static final String FFLAG_PREFIX = "sys.fflag.";
  
  static
  {
    DEFAULT_FLAGS.put("settings_battery_display_app_list", "false");
    DEFAULT_FLAGS.put("settings_zone_picker_v2", "true");
    DEFAULT_FLAGS.put("settings_about_phone_v2", "true");
    DEFAULT_FLAGS.put("settings_bluetooth_while_driving", "false");
    DEFAULT_FLAGS.put("settings_data_usage_v2", "true");
    DEFAULT_FLAGS.put("settings_audio_switcher", "true");
    DEFAULT_FLAGS.put("settings_systemui_theme", "true");
  }
  
  public FeatureFlagUtils() {}
  
  public static Map<String, String> getAllFeatureFlags()
  {
    return DEFAULT_FLAGS;
  }
  
  public static boolean isEnabled(Context paramContext, String paramString)
  {
    if (paramContext != null)
    {
      paramContext = Settings.Global.getString(paramContext.getContentResolver(), paramString);
      if (!TextUtils.isEmpty(paramContext)) {
        return Boolean.parseBoolean(paramContext);
      }
    }
    paramContext = new StringBuilder();
    paramContext.append("sys.fflag.override.");
    paramContext.append(paramString);
    paramContext = SystemProperties.get(paramContext.toString());
    if (!TextUtils.isEmpty(paramContext)) {
      return Boolean.parseBoolean(paramContext);
    }
    return Boolean.parseBoolean((String)getAllFeatureFlags().get(paramString));
  }
  
  public static void setEnabled(Context paramContext, String paramString, boolean paramBoolean)
  {
    paramContext = new StringBuilder();
    paramContext.append("sys.fflag.override.");
    paramContext.append(paramString);
    paramString = paramContext.toString();
    if (paramBoolean) {
      paramContext = "true";
    } else {
      paramContext = "false";
    }
    SystemProperties.set(paramString, paramContext);
  }
}
