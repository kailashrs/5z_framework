package com.android.internal.telephony;

import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class DebugService
{
  private static String TAG = "DebugService";
  
  public DebugService()
  {
    log("DebugService:");
  }
  
  private static void log(String paramString)
  {
    String str = TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DebugService ");
    localStringBuilder.append(paramString);
    Rlog.d(str, localStringBuilder.toString());
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0) && ((TextUtils.equals(paramArrayOfString[0], "--metrics")) || (TextUtils.equals(paramArrayOfString[0], "--metricsproto"))))
    {
      log("Collecting telephony metrics..");
      TelephonyMetrics.getInstance().dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      return;
    }
    log("Dump telephony.");
    PhoneFactory.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
}
