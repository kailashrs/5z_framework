package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import java.util.Collections;
import java.util.List;

public final class AuxiliaryResolveInfo
{
  public final Intent failureIntent;
  public final List<AuxiliaryFilter> filters;
  public final ComponentName installFailureActivity;
  public final boolean needsPhaseTwo;
  public final String token;
  
  public AuxiliaryResolveInfo(ComponentName paramComponentName, Intent paramIntent, List<AuxiliaryFilter> paramList)
  {
    installFailureActivity = paramComponentName;
    filters = paramList;
    token = null;
    needsPhaseTwo = false;
    failureIntent = paramIntent;
  }
  
  public AuxiliaryResolveInfo(ComponentName paramComponentName, String paramString1, long paramLong, String paramString2)
  {
    this(paramComponentName, null, Collections.singletonList(new AuxiliaryFilter(paramString1, paramLong, paramString2)));
  }
  
  public AuxiliaryResolveInfo(String paramString, boolean paramBoolean, Intent paramIntent, List<AuxiliaryFilter> paramList)
  {
    token = paramString;
    needsPhaseTwo = paramBoolean;
    failureIntent = paramIntent;
    filters = paramList;
    installFailureActivity = null;
  }
  
  public static final class AuxiliaryFilter
    extends IntentFilter
  {
    public final Bundle extras;
    public final String packageName;
    public final InstantAppResolveInfo resolveInfo;
    public final String splitName;
    public final long versionCode;
    
    public AuxiliaryFilter(IntentFilter paramIntentFilter, InstantAppResolveInfo paramInstantAppResolveInfo, String paramString, Bundle paramBundle)
    {
      super();
      resolveInfo = paramInstantAppResolveInfo;
      packageName = paramInstantAppResolveInfo.getPackageName();
      versionCode = paramInstantAppResolveInfo.getLongVersionCode();
      splitName = paramString;
      extras = paramBundle;
    }
    
    public AuxiliaryFilter(InstantAppResolveInfo paramInstantAppResolveInfo, String paramString, Bundle paramBundle)
    {
      resolveInfo = paramInstantAppResolveInfo;
      packageName = paramInstantAppResolveInfo.getPackageName();
      versionCode = paramInstantAppResolveInfo.getLongVersionCode();
      splitName = paramString;
      extras = paramBundle;
    }
    
    public AuxiliaryFilter(String paramString1, long paramLong, String paramString2)
    {
      resolveInfo = null;
      packageName = paramString1;
      versionCode = paramLong;
      splitName = paramString2;
      extras = null;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AuxiliaryFilter{packageName='");
      localStringBuilder.append(packageName);
      localStringBuilder.append('\'');
      localStringBuilder.append(", versionCode=");
      localStringBuilder.append(versionCode);
      localStringBuilder.append(", splitName='");
      localStringBuilder.append(splitName);
      localStringBuilder.append('\'');
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
}
