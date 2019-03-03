package android.content.pm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public final class InstantAppRequest
{
  public final String callingPackage;
  public final InstantAppResolveInfo.InstantAppDigest digest;
  public final Intent origIntent;
  public final boolean resolveForStart;
  public final String resolvedType;
  public final AuxiliaryResolveInfo responseObj;
  public final int userId;
  public final Bundle verificationBundle;
  
  public InstantAppRequest(AuxiliaryResolveInfo paramAuxiliaryResolveInfo, Intent paramIntent, String paramString1, String paramString2, int paramInt, Bundle paramBundle, boolean paramBoolean)
  {
    responseObj = paramAuxiliaryResolveInfo;
    origIntent = paramIntent;
    resolvedType = paramString1;
    callingPackage = paramString2;
    userId = paramInt;
    verificationBundle = paramBundle;
    resolveForStart = paramBoolean;
    if ((paramIntent.getData() != null) && (!TextUtils.isEmpty(paramIntent.getData().getHost()))) {
      digest = new InstantAppResolveInfo.InstantAppDigest(paramIntent.getData().getHost(), 5);
    } else {
      digest = InstantAppResolveInfo.InstantAppDigest.UNDEFINED;
    }
  }
}
