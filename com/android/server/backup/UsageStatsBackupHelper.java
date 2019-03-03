package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.app.usage.UsageStatsManagerInternal;
import android.content.Context;
import com.android.server.LocalServices;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UsageStatsBackupHelper
  extends BlobBackupHelper
{
  static final int BLOB_VERSION = 1;
  static final boolean DEBUG = false;
  static final String KEY_USAGE_STATS = "usage_stats";
  static final String TAG = "UsgStatsBackupHelper";
  
  public UsageStatsBackupHelper(Context paramContext)
  {
    super(1, new String[] { "usage_stats" });
  }
  
  protected void applyRestoredPayload(String paramString, byte[] paramArrayOfByte)
  {
    if ("usage_stats".equals(paramString))
    {
      UsageStatsManagerInternal localUsageStatsManagerInternal = (UsageStatsManagerInternal)LocalServices.getService(UsageStatsManagerInternal.class);
      DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
      try
      {
        int i = localDataInputStream.readInt();
        paramArrayOfByte = new byte[paramArrayOfByte.length - 4];
        localDataInputStream.read(paramArrayOfByte, 0, paramArrayOfByte.length);
        localUsageStatsManagerInternal.applyRestoredPayload(i, paramString, paramArrayOfByte);
      }
      catch (IOException paramString) {}
    }
  }
  
  protected byte[] getBackupPayload(String paramString)
  {
    if ("usage_stats".equals(paramString))
    {
      UsageStatsManagerInternal localUsageStatsManagerInternal = (UsageStatsManagerInternal)LocalServices.getService(UsageStatsManagerInternal.class);
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
      try
      {
        localDataOutputStream.writeInt(0);
        localDataOutputStream.write(localUsageStatsManagerInternal.getBackupPayload(0, paramString));
      }
      catch (IOException paramString)
      {
        localByteArrayOutputStream.reset();
      }
      return localByteArrayOutputStream.toByteArray();
    }
    return null;
  }
}
