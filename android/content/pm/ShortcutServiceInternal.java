package android.content.pm;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import java.util.List;

public abstract class ShortcutServiceInternal
{
  public ShortcutServiceInternal() {}
  
  public abstract void addListener(ShortcutChangeListener paramShortcutChangeListener);
  
  public abstract Intent[] createShortcutIntents(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract ParcelFileDescriptor getShortcutIconFd(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2);
  
  public abstract int getShortcutIconResId(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2);
  
  public abstract List<ShortcutInfo> getShortcuts(int paramInt1, String paramString1, long paramLong, String paramString2, List<String> paramList, ComponentName paramComponentName, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  public abstract boolean hasShortcutHostPermission(int paramInt1, String paramString, int paramInt2, int paramInt3);
  
  public abstract boolean isForegroundDefaultLauncher(String paramString, int paramInt);
  
  public abstract boolean isPinnedByCaller(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2);
  
  public abstract boolean isRequestPinItemSupported(int paramInt1, int paramInt2);
  
  public abstract void pinShortcuts(int paramInt1, String paramString1, String paramString2, List<String> paramList, int paramInt2);
  
  public abstract boolean requestPinAppWidget(String paramString, AppWidgetProviderInfo paramAppWidgetProviderInfo, Bundle paramBundle, IntentSender paramIntentSender, int paramInt);
  
  public abstract void setShortcutHostPackage(String paramString1, String paramString2, int paramInt);
  
  public static abstract interface ShortcutChangeListener
  {
    public abstract void onShortcutChanged(String paramString, int paramInt);
  }
}
