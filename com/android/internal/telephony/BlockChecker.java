package com.android.internal.telephony;

import android.content.Context;
import android.os.Bundle;
import android.provider.BlockedNumberContract.SystemContract;
import android.telephony.Rlog;

public class BlockChecker
{
  private static final String TAG = "BlockChecker";
  private static final boolean VDBG = false;
  
  public BlockChecker() {}
  
  @Deprecated
  public static boolean isBlocked(Context paramContext, String paramString)
  {
    return isBlocked(paramContext, paramString, null);
  }
  
  public static boolean isBlocked(Context paramContext, String paramString, Bundle paramBundle)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    long l = System.nanoTime();
    try
    {
      if (BlockedNumberContract.SystemContract.shouldSystemBlockNumber(paramContext, paramString, paramBundle))
      {
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append(paramString);
        paramContext.append(" is blocked.");
        Rlog.d("BlockChecker", paramContext.toString());
        bool2 = true;
      }
    }
    catch (Exception paramContext)
    {
      paramString = new StringBuilder();
      paramString.append("Exception checking for blocked number: ");
      paramString.append(paramContext);
      Rlog.e("BlockChecker", paramString.toString());
      bool2 = bool1;
    }
    int i = (int)((System.nanoTime() - l) / 1000000L);
    if (i > 500)
    {
      paramContext = new StringBuilder();
      paramContext.append("Blocked number lookup took: ");
      paramContext.append(i);
      paramContext.append(" ms.");
      Rlog.d("BlockChecker", paramContext.toString());
    }
    return bool2;
  }
}
