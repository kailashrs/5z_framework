package android.service.wallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class WallpaperSettingsActivity
  extends PreferenceActivity
{
  public static final String EXTRA_PREVIEW_MODE = "android.service.wallpaper.PREVIEW_MODE";
  
  public WallpaperSettingsActivity() {}
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }
}
