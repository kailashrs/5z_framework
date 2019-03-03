package com.android.internal.telephony.euicc;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.euicc.DownloadableSubscription;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public class EuiccOperation
  implements Parcelable
{
  @VisibleForTesting
  static final int ACTION_DOWNLOAD_CONFIRMATION_CODE = 7;
  @VisibleForTesting
  static final int ACTION_DOWNLOAD_DEACTIVATE_SIM = 2;
  @VisibleForTesting
  static final int ACTION_DOWNLOAD_NO_PRIVILEGES = 3;
  @VisibleForTesting
  static final int ACTION_GET_DEFAULT_LIST_DEACTIVATE_SIM = 4;
  @VisibleForTesting
  static final int ACTION_GET_METADATA_DEACTIVATE_SIM = 1;
  @VisibleForTesting
  static final int ACTION_SWITCH_DEACTIVATE_SIM = 5;
  @VisibleForTesting
  static final int ACTION_SWITCH_NO_PRIVILEGES = 6;
  public static final Parcelable.Creator<EuiccOperation> CREATOR = new Parcelable.Creator()
  {
    public EuiccOperation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new EuiccOperation(paramAnonymousParcel);
    }
    
    public EuiccOperation[] newArray(int paramAnonymousInt)
    {
      return new EuiccOperation[paramAnonymousInt];
    }
  };
  private static final String TAG = "EuiccOperation";
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public final int mAction;
  private final String mCallingPackage;
  private final long mCallingToken;
  private final DownloadableSubscription mDownloadableSubscription;
  private final int mSubscriptionId;
  private final boolean mSwitchAfterDownload;
  
  EuiccOperation(int paramInt1, long paramLong, DownloadableSubscription paramDownloadableSubscription, int paramInt2, boolean paramBoolean, String paramString)
  {
    mAction = paramInt1;
    mCallingToken = paramLong;
    mDownloadableSubscription = paramDownloadableSubscription;
    mSubscriptionId = paramInt2;
    mSwitchAfterDownload = paramBoolean;
    mCallingPackage = paramString;
  }
  
  EuiccOperation(Parcel paramParcel)
  {
    mAction = paramParcel.readInt();
    mCallingToken = paramParcel.readLong();
    mDownloadableSubscription = ((DownloadableSubscription)paramParcel.readTypedObject(DownloadableSubscription.CREATOR));
    mSubscriptionId = paramParcel.readInt();
    mSwitchAfterDownload = paramParcel.readBoolean();
    mCallingPackage = paramParcel.readString();
  }
  
  private static void fail(PendingIntent paramPendingIntent)
  {
    EuiccController.get().sendResult(paramPendingIntent, 2, null);
  }
  
  public static EuiccOperation forDownloadConfirmationCode(long paramLong, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, String paramString)
  {
    return new EuiccOperation(7, paramLong, paramDownloadableSubscription, 0, paramBoolean, paramString);
  }
  
  public static EuiccOperation forDownloadDeactivateSim(long paramLong, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, String paramString)
  {
    return new EuiccOperation(2, paramLong, paramDownloadableSubscription, 0, paramBoolean, paramString);
  }
  
  public static EuiccOperation forDownloadNoPrivileges(long paramLong, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, String paramString)
  {
    return new EuiccOperation(3, paramLong, paramDownloadableSubscription, 0, paramBoolean, paramString);
  }
  
  static EuiccOperation forGetDefaultListDeactivateSim(long paramLong, String paramString)
  {
    return new EuiccOperation(4, paramLong, null, 0, false, paramString);
  }
  
  public static EuiccOperation forGetMetadataDeactivateSim(long paramLong, DownloadableSubscription paramDownloadableSubscription, String paramString)
  {
    return new EuiccOperation(1, paramLong, paramDownloadableSubscription, 0, false, paramString);
  }
  
  static EuiccOperation forSwitchDeactivateSim(long paramLong, int paramInt, String paramString)
  {
    return new EuiccOperation(5, paramLong, null, paramInt, false, paramString);
  }
  
  static EuiccOperation forSwitchNoPrivileges(long paramLong, int paramInt, String paramString)
  {
    return new EuiccOperation(6, paramLong, null, paramInt, false, paramString);
  }
  
  private void resolvedDownloadConfirmationCode(String paramString, PendingIntent paramPendingIntent)
  {
    if (TextUtils.isEmpty(paramString))
    {
      fail(paramPendingIntent);
    }
    else
    {
      mDownloadableSubscription.setConfirmationCode(paramString);
      EuiccController.get().downloadSubscription(mDownloadableSubscription, mSwitchAfterDownload, mCallingPackage, true, paramPendingIntent);
    }
  }
  
  private void resolvedDownloadDeactivateSim(boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    if (paramBoolean) {
      EuiccController.get().downloadSubscription(mDownloadableSubscription, mSwitchAfterDownload, mCallingPackage, true, paramPendingIntent);
    } else {
      fail(paramPendingIntent);
    }
  }
  
  private void resolvedDownloadNoPrivileges(boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    long l;
    if (paramBoolean) {
      l = Binder.clearCallingIdentity();
    }
    try
    {
      EuiccController.get().downloadSubscriptionPrivileged(l, mDownloadableSubscription, mSwitchAfterDownload, true, mCallingPackage, paramPendingIntent);
      Binder.restoreCallingIdentity(l);
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private void resolvedGetDefaultListDeactivateSim(boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    if (paramBoolean) {
      EuiccController.get().getDefaultDownloadableSubscriptionList(true, mCallingPackage, paramPendingIntent);
    } else {
      fail(paramPendingIntent);
    }
  }
  
  private void resolvedGetMetadataDeactivateSim(boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    if (paramBoolean) {
      EuiccController.get().getDownloadableSubscriptionMetadata(mDownloadableSubscription, true, mCallingPackage, paramPendingIntent);
    } else {
      fail(paramPendingIntent);
    }
  }
  
  private void resolvedSwitchDeactivateSim(boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    if (paramBoolean) {
      EuiccController.get().switchToSubscription(mSubscriptionId, true, mCallingPackage, paramPendingIntent);
    } else {
      fail(paramPendingIntent);
    }
  }
  
  private void resolvedSwitchNoPrivileges(boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    long l;
    if (paramBoolean) {
      l = Binder.clearCallingIdentity();
    }
    try
    {
      EuiccController.get().switchToSubscriptionPrivileged(l, mSubscriptionId, true, mCallingPackage, paramPendingIntent);
      Binder.restoreCallingIdentity(l);
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public void continueOperation(Bundle paramBundle, PendingIntent paramPendingIntent)
  {
    Binder.restoreCallingIdentity(mCallingToken);
    switch (mAction)
    {
    default: 
      paramBundle = new StringBuilder();
      paramBundle.append("Unknown action: ");
      paramBundle.append(mAction);
      Log.wtf("EuiccOperation", paramBundle.toString());
      break;
    case 7: 
      resolvedDownloadConfirmationCode(paramBundle.getString("android.service.euicc.extra.RESOLUTION_CONFIRMATION_CODE"), paramPendingIntent);
      break;
    case 6: 
      resolvedSwitchNoPrivileges(paramBundle.getBoolean("android.service.euicc.extra.RESOLUTION_CONSENT"), paramPendingIntent);
      break;
    case 5: 
      resolvedSwitchDeactivateSim(paramBundle.getBoolean("android.service.euicc.extra.RESOLUTION_CONSENT"), paramPendingIntent);
      break;
    case 4: 
      resolvedGetDefaultListDeactivateSim(paramBundle.getBoolean("android.service.euicc.extra.RESOLUTION_CONSENT"), paramPendingIntent);
      break;
    case 3: 
      resolvedDownloadNoPrivileges(paramBundle.getBoolean("android.service.euicc.extra.RESOLUTION_CONSENT"), paramPendingIntent);
      break;
    case 2: 
      resolvedDownloadDeactivateSim(paramBundle.getBoolean("android.service.euicc.extra.RESOLUTION_CONSENT"), paramPendingIntent);
      break;
    case 1: 
      resolvedGetMetadataDeactivateSim(paramBundle.getBoolean("android.service.euicc.extra.RESOLUTION_CONSENT"), paramPendingIntent);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAction);
    paramParcel.writeLong(mCallingToken);
    paramParcel.writeTypedObject(mDownloadableSubscription, paramInt);
    paramParcel.writeInt(mSubscriptionId);
    paramParcel.writeBoolean(mSwitchAfterDownload);
    paramParcel.writeString(mCallingPackage);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @VisibleForTesting
  static @interface Action {}
}
