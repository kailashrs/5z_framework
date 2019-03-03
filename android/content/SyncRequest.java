package android.content;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SyncRequest
  implements Parcelable
{
  public static final Parcelable.Creator<SyncRequest> CREATOR = new Parcelable.Creator()
  {
    public SyncRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SyncRequest(paramAnonymousParcel, null);
    }
    
    public SyncRequest[] newArray(int paramAnonymousInt)
    {
      return new SyncRequest[paramAnonymousInt];
    }
  };
  private static final String TAG = "SyncRequest";
  private final Account mAccountToSync;
  private final String mAuthority;
  private final boolean mDisallowMetered;
  private final Bundle mExtras;
  private final boolean mIsAuthority;
  private final boolean mIsExpedited;
  private final boolean mIsPeriodic;
  private final long mSyncFlexTimeSecs;
  private final long mSyncRunTimeSecs;
  
  protected SyncRequest(Builder paramBuilder)
  {
    mSyncFlexTimeSecs = mSyncFlexTimeSecs;
    mSyncRunTimeSecs = mSyncRunTimeSecs;
    mAccountToSync = mAccount;
    mAuthority = mAuthority;
    int i = mSyncType;
    boolean bool1 = false;
    if (i == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsPeriodic = bool2;
    boolean bool2 = bool1;
    if (mSyncTarget == 2) {
      bool2 = true;
    }
    mIsAuthority = bool2;
    mIsExpedited = mExpedited;
    mExtras = new Bundle(mCustomExtras);
    mExtras.putAll(mSyncConfigExtras);
    mDisallowMetered = mDisallowMetered;
  }
  
  private SyncRequest(Parcel paramParcel)
  {
    Bundle localBundle = paramParcel.readBundle();
    boolean bool1 = true;
    mExtras = Bundle.setDefusable(localBundle, true);
    mSyncFlexTimeSecs = paramParcel.readLong();
    mSyncRunTimeSecs = paramParcel.readLong();
    boolean bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsPeriodic = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mDisallowMetered = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsAuthority = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    mIsExpedited = bool2;
    mAccountToSync = ((Account)paramParcel.readParcelable(null));
    mAuthority = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Account getAccount()
  {
    return mAccountToSync;
  }
  
  public Bundle getBundle()
  {
    return mExtras;
  }
  
  public String getProvider()
  {
    return mAuthority;
  }
  
  public long getSyncFlexTime()
  {
    return mSyncFlexTimeSecs;
  }
  
  public long getSyncRunTime()
  {
    return mSyncRunTimeSecs;
  }
  
  public boolean isExpedited()
  {
    return mIsExpedited;
  }
  
  public boolean isPeriodic()
  {
    return mIsPeriodic;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBundle(mExtras);
    paramParcel.writeLong(mSyncFlexTimeSecs);
    paramParcel.writeLong(mSyncRunTimeSecs);
    paramParcel.writeInt(mIsPeriodic);
    paramParcel.writeInt(mDisallowMetered);
    paramParcel.writeInt(mIsAuthority);
    paramParcel.writeInt(mIsExpedited);
    paramParcel.writeParcelable(mAccountToSync, paramInt);
    paramParcel.writeString(mAuthority);
  }
  
  public static class Builder
  {
    private static final int SYNC_TARGET_ADAPTER = 2;
    private static final int SYNC_TARGET_UNKNOWN = 0;
    private static final int SYNC_TYPE_ONCE = 2;
    private static final int SYNC_TYPE_PERIODIC = 1;
    private static final int SYNC_TYPE_UNKNOWN = 0;
    private Account mAccount;
    private String mAuthority;
    private Bundle mCustomExtras;
    private boolean mDisallowMetered;
    private boolean mExpedited;
    private boolean mIgnoreBackoff;
    private boolean mIgnoreSettings;
    private boolean mIsManual;
    private boolean mNoRetry;
    private boolean mRequiresCharging;
    private Bundle mSyncConfigExtras;
    private long mSyncFlexTimeSecs;
    private long mSyncRunTimeSecs;
    private int mSyncTarget = 0;
    private int mSyncType = 0;
    
    public Builder() {}
    
    private void setupInterval(long paramLong1, long paramLong2)
    {
      if (paramLong2 <= paramLong1)
      {
        mSyncRunTimeSecs = paramLong1;
        mSyncFlexTimeSecs = paramLong2;
        return;
      }
      throw new IllegalArgumentException("Specified run time for the sync must be after the specified flex time.");
    }
    
    public SyncRequest build()
    {
      ContentResolver.validateSyncExtrasBundle(mCustomExtras);
      if (mCustomExtras == null) {
        mCustomExtras = new Bundle();
      }
      mSyncConfigExtras = new Bundle();
      if (mIgnoreBackoff) {
        mSyncConfigExtras.putBoolean("ignore_backoff", true);
      }
      if (mDisallowMetered) {
        mSyncConfigExtras.putBoolean("allow_metered", true);
      }
      if (mRequiresCharging) {
        mSyncConfigExtras.putBoolean("require_charging", true);
      }
      if (mIgnoreSettings) {
        mSyncConfigExtras.putBoolean("ignore_settings", true);
      }
      if (mNoRetry) {
        mSyncConfigExtras.putBoolean("do_not_retry", true);
      }
      if (mExpedited) {
        mSyncConfigExtras.putBoolean("expedited", true);
      }
      if (mIsManual)
      {
        mSyncConfigExtras.putBoolean("ignore_backoff", true);
        mSyncConfigExtras.putBoolean("ignore_settings", true);
      }
      if ((mSyncType == 1) && ((ContentResolver.invalidPeriodicExtras(mCustomExtras)) || (ContentResolver.invalidPeriodicExtras(mSyncConfigExtras)))) {
        throw new IllegalArgumentException("Illegal extras were set");
      }
      if (mSyncTarget != 0) {
        return new SyncRequest(this);
      }
      throw new IllegalArgumentException("Must specify an adapter with setSyncAdapter(Account, String");
    }
    
    public Builder setDisallowMetered(boolean paramBoolean)
    {
      if ((mIgnoreSettings) && (paramBoolean)) {
        throw new IllegalArgumentException("setDisallowMetered(true) after having specified that settings are ignored.");
      }
      mDisallowMetered = paramBoolean;
      return this;
    }
    
    public Builder setExpedited(boolean paramBoolean)
    {
      mExpedited = paramBoolean;
      return this;
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      mCustomExtras = paramBundle;
      return this;
    }
    
    public Builder setIgnoreBackoff(boolean paramBoolean)
    {
      mIgnoreBackoff = paramBoolean;
      return this;
    }
    
    public Builder setIgnoreSettings(boolean paramBoolean)
    {
      if ((mDisallowMetered) && (paramBoolean)) {
        throw new IllegalArgumentException("setIgnoreSettings(true) after having specified sync settings with this builder.");
      }
      mIgnoreSettings = paramBoolean;
      return this;
    }
    
    public Builder setManual(boolean paramBoolean)
    {
      mIsManual = paramBoolean;
      return this;
    }
    
    public Builder setNoRetry(boolean paramBoolean)
    {
      mNoRetry = paramBoolean;
      return this;
    }
    
    public Builder setRequiresCharging(boolean paramBoolean)
    {
      mRequiresCharging = paramBoolean;
      return this;
    }
    
    public Builder setSyncAdapter(Account paramAccount, String paramString)
    {
      if (mSyncTarget == 0)
      {
        if ((paramString != null) && (paramString.length() == 0)) {
          throw new IllegalArgumentException("Authority must be non-empty");
        }
        mSyncTarget = 2;
        mAccount = paramAccount;
        mAuthority = paramString;
        return this;
      }
      throw new IllegalArgumentException("Sync target has already been defined.");
    }
    
    public Builder syncOnce()
    {
      if (mSyncType == 0)
      {
        mSyncType = 2;
        setupInterval(0L, 0L);
        return this;
      }
      throw new IllegalArgumentException("Sync type has already been defined.");
    }
    
    public Builder syncPeriodic(long paramLong1, long paramLong2)
    {
      if (mSyncType == 0)
      {
        mSyncType = 1;
        setupInterval(paramLong1, paramLong2);
        return this;
      }
      throw new IllegalArgumentException("Sync type has already been defined.");
    }
  }
}
