package com.android.internal.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import com.android.internal.util.ArrayUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AccessibilityShortcutController
{
  public static final ComponentName COLOR_INVERSION_COMPONENT_NAME = new ComponentName("com.android.server.accessibility", "ColorInversion");
  public static final ComponentName DALTONIZER_COMPONENT_NAME = new ComponentName("com.android.server.accessibility", "Daltonizer");
  private static final String TAG = "AccessibilityShortcutController";
  private static final AudioAttributes VIBRATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(11).build();
  private static Map<ComponentName, ToggleableFrameworkFeatureInfo> sFrameworkShortcutFeaturesMap;
  private AlertDialog mAlertDialog;
  private final Context mContext;
  private boolean mEnabledOnLockScreen;
  public FrameworkObjectProvider mFrameworkObjectProvider = new FrameworkObjectProvider();
  private boolean mIsShortcutEnabled;
  private int mUserId;
  
  public AccessibilityShortcutController(Context paramContext, Handler paramHandler, int paramInt)
  {
    mContext = paramContext;
    mUserId = paramInt;
    paramContext = new ContentObserver(paramHandler)
    {
      public void onChange(boolean paramAnonymousBoolean, Uri paramAnonymousUri, int paramAnonymousInt)
      {
        if (paramAnonymousInt == mUserId) {
          onSettingsChanged();
        }
      }
    };
    mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_shortcut_target_service"), false, paramContext, -1);
    mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_shortcut_enabled"), false, paramContext, -1);
    mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_shortcut_on_lock_screen"), false, paramContext, -1);
    mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("accessibility_shortcut_dialog_shown"), false, paramContext, -1);
    setCurrentUser(mUserId);
  }
  
  private AlertDialog createShortcutWarningDialog(int paramInt)
  {
    String str = getShortcutFeatureDescription(true);
    if (str == null) {
      return null;
    }
    str = String.format(mContext.getString(17039427), new Object[] { str });
    return mFrameworkObjectProvider.getAlertDialogBuilder(mFrameworkObjectProvider.getSystemUiContext()).setTitle(17039428).setMessage(str).setCancelable(false).setPositiveButton(17040234, null).setNegativeButton(17039889, new _..Lambda.AccessibilityShortcutController.2NcDVJHkpsPbwr45v1_NfIM8row(this, paramInt)).setOnCancelListener(new _..Lambda.AccessibilityShortcutController.T96D356_n5VObNOonEIYV8s83Fc(this, paramInt)).create();
  }
  
  public static Map<ComponentName, ToggleableFrameworkFeatureInfo> getFrameworkShortcutFeaturesMap()
  {
    if (sFrameworkShortcutFeaturesMap == null)
    {
      ArrayMap localArrayMap = new ArrayMap(2);
      localArrayMap.put(COLOR_INVERSION_COMPONENT_NAME, new ToggleableFrameworkFeatureInfo("accessibility_display_inversion_enabled", "1", "0", 17039658));
      localArrayMap.put(DALTONIZER_COMPONENT_NAME, new ToggleableFrameworkFeatureInfo("accessibility_display_daltonizer_enabled", "1", "0", 17039657));
      sFrameworkShortcutFeaturesMap = Collections.unmodifiableMap(localArrayMap);
    }
    return sFrameworkShortcutFeaturesMap;
  }
  
  private AccessibilityServiceInfo getInfoForTargetService()
  {
    String str = getTargetServiceComponentNameString(mContext, -2);
    if (str == null) {
      return null;
    }
    AccessibilityManager localAccessibilityManager = mFrameworkObjectProvider.getAccessibilityManagerInstance(mContext);
    return localAccessibilityManager.getInstalledServiceInfoWithComponentName(ComponentName.unflattenFromString(str));
  }
  
  private String getShortcutFeatureDescription(boolean paramBoolean)
  {
    Object localObject1 = getTargetServiceComponentNameString(mContext, -2);
    if (localObject1 == null) {
      return null;
    }
    Object localObject2 = ComponentName.unflattenFromString((String)localObject1);
    localObject1 = (ToggleableFrameworkFeatureInfo)getFrameworkShortcutFeaturesMap().get(localObject2);
    if (localObject1 != null) {
      return ((ToggleableFrameworkFeatureInfo)localObject1).getLabel(mContext);
    }
    localObject2 = mFrameworkObjectProvider.getAccessibilityManagerInstance(mContext).getInstalledServiceInfoWithComponentName((ComponentName)localObject2);
    if (localObject2 == null) {
      return null;
    }
    PackageManager localPackageManager = mContext.getPackageManager();
    localObject1 = ((AccessibilityServiceInfo)localObject2).getResolveInfo().loadLabel(localPackageManager).toString();
    localObject2 = ((AccessibilityServiceInfo)localObject2).loadSummary(localPackageManager);
    if ((paramBoolean) && (!TextUtils.isEmpty((CharSequence)localObject2))) {
      return String.format("%s\n%s", new Object[] { localObject1, localObject2 });
    }
    return localObject1;
  }
  
  public static String getTargetServiceComponentNameString(Context paramContext, int paramInt)
  {
    String str = Settings.Secure.getStringForUser(paramContext.getContentResolver(), "accessibility_shortcut_target_service", paramInt);
    if (str != null) {
      return str;
    }
    return paramContext.getString(17039684);
  }
  
  private boolean hasFeatureLeanback()
  {
    return mContext.getPackageManager().hasSystemFeature("android.software.leanback");
  }
  
  private boolean isServiceEnabled(AccessibilityServiceInfo paramAccessibilityServiceInfo)
  {
    return mFrameworkObjectProvider.getAccessibilityManagerInstance(mContext).getEnabledAccessibilityServiceList(-1).contains(paramAccessibilityServiceInfo);
  }
  
  public boolean isAccessibilityShortcutAvailable(boolean paramBoolean)
  {
    if ((mIsShortcutEnabled) && ((!paramBoolean) || (mEnabledOnLockScreen))) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  public void onSettingsChanged()
  {
    boolean bool1 = TextUtils.isEmpty(getTargetServiceComponentNameString(mContext, mUserId));
    boolean bool2 = true;
    ContentResolver localContentResolver = mContext.getContentResolver();
    int i;
    if (Settings.Secure.getIntForUser(localContentResolver, "accessibility_shortcut_enabled", 1, mUserId) == 1) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool3;
    if (Settings.Secure.getIntForUser(localContentResolver, "accessibility_shortcut_on_lock_screen", Settings.Secure.getIntForUser(localContentResolver, "accessibility_shortcut_dialog_shown", 0, mUserId), mUserId) == 1) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    mEnabledOnLockScreen = bool3;
    if ((i != 0) && ((bool1 ^ true))) {
      bool3 = bool2;
    } else {
      bool3 = false;
    }
    mIsShortcutEnabled = bool3;
  }
  
  public void performAccessibilityShortcut()
  {
    Slog.d("AccessibilityShortcutController", "Accessibility shortcut activated");
    Object localObject1 = mContext.getContentResolver();
    int i = ActivityManager.getCurrentUser();
    int j = Settings.Secure.getIntForUser((ContentResolver)localObject1, "accessibility_shortcut_dialog_shown", 0, i);
    int k;
    if (hasFeatureLeanback()) {
      k = 11;
    } else {
      k = 10;
    }
    Object localObject2 = RingtoneManager.getRingtone(mContext, Settings.System.DEFAULT_NOTIFICATION_URI);
    if (localObject2 != null)
    {
      ((Ringtone)localObject2).setAudioAttributes(new AudioAttributes.Builder().setUsage(k).build());
      ((Ringtone)localObject2).play();
    }
    localObject2 = (Vibrator)mContext.getSystemService("vibrator");
    if ((localObject2 != null) && (((Vibrator)localObject2).hasVibrator())) {
      ((Vibrator)localObject2).vibrate(ArrayUtils.convertToLongArray(mContext.getResources().getIntArray(17236018)), -1, VIBRATION_ATTRIBUTES);
    }
    Object localObject3;
    if (j == 0)
    {
      mAlertDialog = createShortcutWarningDialog(i);
      if (mAlertDialog == null) {
        return;
      }
      localObject3 = mAlertDialog.getWindow();
      localObject2 = ((Window)localObject3).getAttributes();
      type = 2009;
      ((Window)localObject3).setAttributes((WindowManager.LayoutParams)localObject2);
      mAlertDialog.show();
      Settings.Secure.putIntForUser((ContentResolver)localObject1, "accessibility_shortcut_dialog_shown", 1, i);
    }
    else
    {
      if (mAlertDialog != null)
      {
        mAlertDialog.dismiss();
        mAlertDialog = null;
      }
      localObject2 = getShortcutFeatureDescription(false);
      if (localObject2 == null)
      {
        Slog.e("AccessibilityShortcutController", "Accessibility shortcut set to invalid service");
        return;
      }
      localObject1 = getInfoForTargetService();
      if (localObject1 != null)
      {
        localObject3 = mContext;
        if (isServiceEnabled((AccessibilityServiceInfo)localObject1)) {
          k = 17039425;
        } else {
          k = 17039426;
        }
        localObject1 = String.format(((Context)localObject3).getString(k), new Object[] { localObject2 });
        localObject1 = mFrameworkObjectProvider.makeToastFromText(mContext, (CharSequence)localObject1, 1);
        localObject2 = ((Toast)localObject1).getWindowParams();
        privateFlags |= 0x10;
        ((Toast)localObject1).show();
      }
      mFrameworkObjectProvider.getAccessibilityManagerInstance(mContext).performAccessibilityShortcut();
    }
  }
  
  public void setCurrentUser(int paramInt)
  {
    mUserId = paramInt;
    onSettingsChanged();
  }
  
  public static class FrameworkObjectProvider
  {
    public FrameworkObjectProvider() {}
    
    public AccessibilityManager getAccessibilityManagerInstance(Context paramContext)
    {
      return AccessibilityManager.getInstance(paramContext);
    }
    
    public AlertDialog.Builder getAlertDialogBuilder(Context paramContext)
    {
      return new AlertDialog.Builder(paramContext);
    }
    
    public Context getSystemUiContext()
    {
      return ActivityThread.currentActivityThread().getSystemUiContext();
    }
    
    public Toast makeToastFromText(Context paramContext, CharSequence paramCharSequence, int paramInt)
    {
      return Toast.makeText(paramContext, paramCharSequence, paramInt);
    }
  }
  
  public static class ToggleableFrameworkFeatureInfo
  {
    private int mIconDrawableId;
    private final int mLabelStringResourceId;
    private final String mSettingKey;
    private final String mSettingOffValue;
    private final String mSettingOnValue;
    
    ToggleableFrameworkFeatureInfo(String paramString1, String paramString2, String paramString3, int paramInt)
    {
      mSettingKey = paramString1;
      mSettingOnValue = paramString2;
      mSettingOffValue = paramString3;
      mLabelStringResourceId = paramInt;
    }
    
    public String getLabel(Context paramContext)
    {
      return paramContext.getString(mLabelStringResourceId);
    }
    
    public String getSettingKey()
    {
      return mSettingKey;
    }
    
    public String getSettingOffValue()
    {
      return mSettingOffValue;
    }
    
    public String getSettingOnValue()
    {
      return mSettingOnValue;
    }
  }
}
