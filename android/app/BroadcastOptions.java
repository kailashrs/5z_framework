package android.app;

import android.annotation.SystemApi;
import android.os.Bundle;

@SystemApi
public class BroadcastOptions
{
  static final String KEY_DONT_SEND_TO_RESTRICTED_APPS = "android:broadcast.dontSendToRestrictedApps";
  static final String KEY_MAX_MANIFEST_RECEIVER_API_LEVEL = "android:broadcast.maxManifestReceiverApiLevel";
  static final String KEY_MIN_MANIFEST_RECEIVER_API_LEVEL = "android:broadcast.minManifestReceiverApiLevel";
  static final String KEY_TEMPORARY_APP_WHITELIST_DURATION = "android:broadcast.temporaryAppWhitelistDuration";
  private boolean mDontSendToRestrictedApps = false;
  private int mMaxManifestReceiverApiLevel = 10000;
  private int mMinManifestReceiverApiLevel = 0;
  private long mTemporaryAppWhitelistDuration;
  
  private BroadcastOptions() {}
  
  public BroadcastOptions(Bundle paramBundle)
  {
    mTemporaryAppWhitelistDuration = paramBundle.getLong("android:broadcast.temporaryAppWhitelistDuration");
    mMinManifestReceiverApiLevel = paramBundle.getInt("android:broadcast.minManifestReceiverApiLevel", 0);
    mMaxManifestReceiverApiLevel = paramBundle.getInt("android:broadcast.maxManifestReceiverApiLevel", 10000);
    mDontSendToRestrictedApps = paramBundle.getBoolean("android:broadcast.dontSendToRestrictedApps", false);
  }
  
  public static BroadcastOptions makeBasic()
  {
    return new BroadcastOptions();
  }
  
  public int getMaxManifestReceiverApiLevel()
  {
    return mMaxManifestReceiverApiLevel;
  }
  
  public int getMinManifestReceiverApiLevel()
  {
    return mMinManifestReceiverApiLevel;
  }
  
  public long getTemporaryAppWhitelistDuration()
  {
    return mTemporaryAppWhitelistDuration;
  }
  
  public boolean isDontSendToRestrictedApps()
  {
    return mDontSendToRestrictedApps;
  }
  
  public void setDontSendToRestrictedApps(boolean paramBoolean)
  {
    mDontSendToRestrictedApps = paramBoolean;
  }
  
  public void setMaxManifestReceiverApiLevel(int paramInt)
  {
    mMaxManifestReceiverApiLevel = paramInt;
  }
  
  public void setMinManifestReceiverApiLevel(int paramInt)
  {
    mMinManifestReceiverApiLevel = paramInt;
  }
  
  public void setTemporaryAppWhitelistDuration(long paramLong)
  {
    mTemporaryAppWhitelistDuration = paramLong;
  }
  
  public Bundle toBundle()
  {
    Bundle localBundle = new Bundle();
    if (mTemporaryAppWhitelistDuration > 0L) {
      localBundle.putLong("android:broadcast.temporaryAppWhitelistDuration", mTemporaryAppWhitelistDuration);
    }
    if (mMinManifestReceiverApiLevel != 0) {
      localBundle.putInt("android:broadcast.minManifestReceiverApiLevel", mMinManifestReceiverApiLevel);
    }
    if (mMaxManifestReceiverApiLevel != 10000) {
      localBundle.putInt("android:broadcast.maxManifestReceiverApiLevel", mMaxManifestReceiverApiLevel);
    }
    if (mDontSendToRestrictedApps) {
      localBundle.putBoolean("android:broadcast.dontSendToRestrictedApps", true);
    }
    if (localBundle.isEmpty()) {
      localBundle = null;
    }
    return localBundle;
  }
}
