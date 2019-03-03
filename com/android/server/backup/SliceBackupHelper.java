package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.app.slice.ISliceManager;
import android.app.slice.ISliceManager.Stub;
import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;

public class SliceBackupHelper
  extends BlobBackupHelper
{
  static final int BLOB_VERSION = 1;
  static final boolean DEBUG = Log.isLoggable("SliceBackupHelper", 3);
  static final String KEY_SLICES = "slices";
  static final String TAG = "SliceBackupHelper";
  
  public SliceBackupHelper(Context paramContext)
  {
    super(1, new String[] { "slices" });
  }
  
  protected void applyRestoredPayload(String paramString, byte[] paramArrayOfByte)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Got restore of ");
      localStringBuilder.append(paramString);
      Slog.v("SliceBackupHelper", localStringBuilder.toString());
    }
    if ("slices".equals(paramString)) {
      try
      {
        ISliceManager.Stub.asInterface(ServiceManager.getService("slice")).applyRestore(paramArrayOfByte, 0);
      }
      catch (Exception paramString)
      {
        Slog.e("SliceBackupHelper", "Couldn't communicate with slice manager");
      }
    }
  }
  
  protected byte[] getBackupPayload(String paramString)
  {
    byte[] arrayOfByte = null;
    if ("slices".equals(paramString)) {
      try
      {
        arrayOfByte = ISliceManager.Stub.asInterface(ServiceManager.getService("slice")).getBackupPayload(0);
      }
      catch (Exception paramString)
      {
        Slog.e("SliceBackupHelper", "Couldn't communicate with slice manager");
        arrayOfByte = null;
      }
    }
    return arrayOfByte;
  }
}
