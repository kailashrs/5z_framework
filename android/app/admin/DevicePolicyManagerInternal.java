package android.app.admin;

import android.content.Intent;
import java.util.List;

public abstract class DevicePolicyManagerInternal
{
  public DevicePolicyManagerInternal() {}
  
  public abstract void addOnCrossProfileWidgetProvidersChangeListener(OnCrossProfileWidgetProvidersChangeListener paramOnCrossProfileWidgetProvidersChangeListener);
  
  public abstract boolean canUserHaveUntrustedCredentialReset(int paramInt);
  
  public abstract Intent createShowAdminSupportIntent(int paramInt, boolean paramBoolean);
  
  public abstract Intent createUserRestrictionSupportIntent(int paramInt, String paramString);
  
  public abstract List<String> getCrossProfileWidgetProviders(int paramInt);
  
  protected abstract DevicePolicyCache getDevicePolicyCache();
  
  public abstract CharSequence getPrintingDisabledReasonForUser(int paramInt);
  
  public abstract boolean isActiveAdminWithPolicy(int paramInt1, int paramInt2);
  
  public abstract boolean isUserAffiliatedWithDevice(int paramInt);
  
  public abstract void reportSeparateProfileChallengeChanged(int paramInt);
  
  public static abstract interface OnCrossProfileWidgetProvidersChangeListener
  {
    public abstract void onCrossProfileWidgetProvidersChanged(int paramInt, List<String> paramList);
  }
}
