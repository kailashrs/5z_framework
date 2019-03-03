package com.android.internal.backup;

import android.content.ContentResolver;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.KeyValueListParser;
import android.util.KeyValueSettingObserver;

class LocalTransportParameters
  extends KeyValueSettingObserver
{
  private static final String KEY_FAKE_ENCRYPTION_FLAG = "fake_encryption_flag";
  private static final String KEY_NON_INCREMENTAL_ONLY = "non_incremental_only";
  private static final String SETTING = "backup_local_transport_parameters";
  private static final String TAG = "LocalTransportParams";
  private boolean mFakeEncryptionFlag;
  private boolean mIsNonIncrementalOnly;
  
  LocalTransportParameters(Handler paramHandler, ContentResolver paramContentResolver)
  {
    super(paramHandler, paramContentResolver, Settings.Secure.getUriFor("backup_local_transport_parameters"));
  }
  
  public String getSettingValue(ContentResolver paramContentResolver)
  {
    return Settings.Secure.getString(paramContentResolver, "backup_local_transport_parameters");
  }
  
  boolean isFakeEncryptionFlag()
  {
    return mFakeEncryptionFlag;
  }
  
  boolean isNonIncrementalOnly()
  {
    return mIsNonIncrementalOnly;
  }
  
  public void update(KeyValueListParser paramKeyValueListParser)
  {
    mFakeEncryptionFlag = paramKeyValueListParser.getBoolean("fake_encryption_flag", false);
    mIsNonIncrementalOnly = paramKeyValueListParser.getBoolean("non_incremental_only", false);
  }
}
